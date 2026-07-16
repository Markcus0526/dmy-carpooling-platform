package com.damytech.PincheApp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.damytech.Misc.Global;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-7-29
 * Time: 上午8:43
 * To change this template use File | Settings | File Templates.
 */
public class SuperActivity extends Activity
{
	private static final String TAG ="erik_app_track";
    public com.damytech.Utils.ProgressDialog m_dlgProg = null;

	private final int BACK_PRESS_MAX_INTERVAL = 3000;
	private boolean isPressedBack = false;
	private long backPressedTime = 0;

	private String szLocalPath = "";

	public static SuperActivity mInstance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Activity name is ------>" + this.getClass().getName());
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		Thread.setDefaultUncaughtExceptionHandler(new MyUnhandledExceptionHandler());

		mInstance = this;
	}

	protected void onResume()
	{
		super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

		int nDir = getIntent().getIntExtra(Global.ANIM_DIRECTION(), -1);
		if (nDir == Global.ANIM_COVER_FROM_LEFT())
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		else if (nDir == Global.ANIM_COVER_FROM_RIGHT())
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
		else
			overridePendingTransition(0, 0);
	}

	@Override
	protected void onPause()
	{
		super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

		ViewGroup rootView = (ViewGroup)getWindow().getDecorView().findViewById(R.id.parent_layout);
		hideKeyboardsInView(rootView);

//		if (getIntent().getExtras() != null)
//			getIntent().getExtras().clear();
	}

	private void hideKeyboardsInView(ViewGroup view)
	{
		if (view == null)
			return;

		int nCount = view.getChildCount();
		for (int i = 0; i < nCount; i++)
		{
			View childView = view.getChildAt(i);
			if (childView instanceof ViewGroup)
			{
				hideKeyboardsInView((ViewGroup)childView);
			}
			else if (childView instanceof EditText)
			{
				Global.hideKeyboardFromText((EditText)childView, SuperActivity.this);
			}
		}
	}

	protected void onNewIntent(Intent intent) {
		super.setIntent(intent);    //To change body of overridden methods use File | Settings | File Templates.
	}


	public void startProgress()
	{
		startProgress(getResources().getString(R.string.STR_QINGSHAOHOU));
	}


	public void startProgress(String szMsg)
	{
		if (m_dlgProg != null && m_dlgProg.isShowing())
			return;

		if (m_dlgProg == null)
		{
			m_dlgProg = new com.damytech.Utils.ProgressDialog(SuperActivity.this);
			m_dlgProg.setMessage(szMsg);
			m_dlgProg.setCancelable(true);
		}

		m_dlgProg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		m_dlgProg.show();
	}

    public void startProgress(String szMsg, boolean cancelable)
    {
        if (m_dlgProg != null && m_dlgProg.isShowing())
            return;

        if (m_dlgProg == null)
        {
            m_dlgProg = new com.damytech.Utils.ProgressDialog(SuperActivity.this);
            m_dlgProg.setMessage(szMsg);
            m_dlgProg.setCancelable(cancelable);
        }

        m_dlgProg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        m_dlgProg.show();
    }

	public void stopProgress()
	{
		if (m_dlgProg != null)
		{
			m_dlgProg.dismiss();
			m_dlgProg = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (isLastActivity())
			{
				try
				{
					if (!isPressedBack)
					{
						Global.showAdvancedToast(SuperActivity.this, getResources().getString(R.string.STR_PRESS_ONCEMORE_EXIT), Gravity.BOTTOM);
						backPressedTime = Calendar.getInstance().getTimeInMillis();
						isPressedBack = true;
					}
					else
					{
						if (Calendar.getInstance().getTimeInMillis() - backPressedTime < BACK_PRESS_MAX_INTERVAL)
						{
							SuperActivity.this.finish();
							System.exit(1);
						}
						else
						{
							Global.showAdvancedToast(SuperActivity.this, getResources().getString(R.string.STR_PRESS_ONCEMORE_EXIT), Gravity.BOTTOM);
							backPressedTime = Calendar.getInstance().getTimeInMillis();
						}
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			else
			{
				finishWithAnimation();
			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}


	public boolean isLastActivity()
	{
		ActivityManager mngr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

		if (taskList.get(0).numActivities != 1 || !taskList.get(0).topActivity.getClassName().equals(this.getClass().getName()))
			return false;

		return true;
	}

	public void finishWithAnimation()
	{

        SuperActivity.this.finish();
	}


	public Point mScrSize = new Point(0, 0);
	public Point getScreenSize()
	{
		Point ptTemp = Global.getScreenSize(getApplicationContext());
		ptTemp.y -= Global.statusBarHeight(this);

		return ptTemp;
	}


	public void logout(String szMsg)
	{
		int nIdentify = Global.loadIdentify(getApplicationContext());

		Global.clearUserInfo(getApplicationContext());

		if (szMsg != null && !szMsg.equals(""))
			Global.showAdvancedToast(SuperActivity.this, szMsg, Gravity.CENTER);

		if (nIdentify == Global.IDENTIFY_DRIVER())      // Work as driver
		{
			if (this instanceof DrvMainActivity) {
				((DrvMainActivity)this).onSelectShouYeTab();
				((DrvMainActivity)this).resetUserImage();
			} else {
				Intent intent = new Intent(SuperActivity.this, DrvMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		}
		else                // Work as passenger
		{
			if (this instanceof PassMainActivity) {
				((PassMainActivity)this).onSelectShouYeTab();
			} else {
				Intent intent = new Intent(SuperActivity.this, PassMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		}
	}

	private class MyUnhandledExceptionHandler implements Thread.UncaughtExceptionHandler
	{
		private Thread.UncaughtExceptionHandler defaultUEH;

		public MyUnhandledExceptionHandler() {
			this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			ex.printStackTrace();
			defaultUEH.uncaughtException(thread, ex);
		}

	}

	public void InstallNewApp(final String szUrl)
	{
		Thread thr = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					int nBytesRead = 0, nByteWritten = 0;
					byte[] buf = new byte[1024];

					URLConnection urlConn = null;
					URL fileUrl = null;
					InputStream inStream = null;
					OutputStream outStream = null;

					File dir_item = null, file_item = null;

					// Show progress dialog
					runOnUiThread(runnable_showProgress);

					// Downloading file from address
					fileUrl = new URL(szUrl);
					urlConn = fileUrl.openConnection();
					inStream = urlConn.getInputStream();
					szLocalPath = szUrl.substring(szUrl.lastIndexOf("/") + 1);
					dir_item = new File(Environment.getExternalStorageDirectory(), "download");
					dir_item.mkdirs();
					file_item = new File(dir_item, szLocalPath);

					outStream = new BufferedOutputStream(new FileOutputStream(file_item));

					while ((nBytesRead = inStream.read(buf)) != -1)
					{
						outStream.write(buf, 0, nBytesRead);
						nByteWritten += nBytesRead;
						UpdateProgress(nByteWritten);
					}

					UpdateProgress(getResources().getString(R.string.STR_DOWNLOAD_OK));

					inStream.close();
					outStream.flush();
					outStream.close();
					/////////////////////////////////////////////////////////////////////////

					// Hide progress dialog
					runOnUiThread(runnable_hideProgress);

					// Finish downloading and install
					runOnUiThread(runnable_finish_download);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					runOnUiThread(runnable_download_error);
				}
			}
		});

		thr.start();
	}


	private void UpdateProgress(int nValue)
	{
		UpdateProgress("" + nValue + "Bytes...");
	}

	private void UpdateProgress(final String szMsg)
	{
		Runnable runnable_update = new Runnable() {
			@Override
			public void run() {
				m_dlgProg.setMessage(szMsg);
			}
		};
		runOnUiThread(runnable_update);
	}

	private Runnable runnable_showProgress = new Runnable() {
		@Override
		public void run() {
			startProgress();
		}
	};

	private Runnable runnable_hideProgress = new Runnable() {
		@Override
		public void run() {
			stopProgress();
		}
	};

	Runnable runnable_finish_download = new Runnable()
	{
		public void run()
		{
			// Install update app
			Intent intent_install = new Intent( Intent.ACTION_VIEW);
			intent_install.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/download/" + szLocalPath)), "application/vnd.android.package-archive");
			intent_install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent_install);

			// Uninstall current app
			Intent intent_uninstall = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", SuperActivity.this.getPackageName(), null));
			intent_uninstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent_uninstall);

			SuperActivity.this.finish();
		}
	};

	Runnable runnable_download_error = new Runnable() {
		@Override
		public void run() {
			Global.showAdvancedToast(SuperActivity.this, getResources().getString(R.string.STR_ERR_VERSION_INFO), Gravity.CENTER);
			stopProgress();
		}
	};


    public void showHintText(RelativeLayout itemLayout, String content, int id)
    {
        try
        {
            TextView txtHint = (TextView) itemLayout.findViewById(id);
            if (txtHint != null)
            {
                txtHint.setVisibility(View.VISIBLE);
            }
            else
            {
                TextView txtView = new TextView(itemLayout.getContext());
                RelativeLayout.LayoutParams txt_layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                txtView.setLayoutParams(txt_layoutParams);
                txtView.setTextColor(Color.rgb(0x60, 0x60, 0x60));
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 25);
                txtView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                txtView.setPadding(10, 10, 10, 10);
                txtView.setText(content);
                txtView.setId(id);
                itemLayout.addView(txtView);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void hideHintText(RelativeLayout itemLayout, int id)
    {
        try {
            TextView txtView = (TextView) itemLayout.findViewById(id);
            if (txtView != null)
            {
                txtView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
