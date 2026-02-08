package com.damytech.PincheApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STApplication;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-9
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class AboutActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private ImageButton btn_story = null, btn_version = null, btn_agreement = null, btn_call = null, btn_feedback = null;
	private TextView txt_version = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_about_oo);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_story = (ImageButton)findViewById(R.id.btn_story);
		btn_version = (ImageButton)findViewById(R.id.btn_version);
		btn_agreement = (ImageButton)findViewById(R.id.btn_agreement);
		btn_call = (ImageButton)findViewById(R.id.btn_call);
		btn_feedback = (ImageButton)findViewById(R.id.btn_feedback);

		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});

		btn_story.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this, StoryActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				AboutActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});

		btn_version.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				startProgress();
				CommManager.getLatestAppVersion(AboutActivity.this.getPackageName(), version_handler);
			}
		});

		btn_agreement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this, AgreementActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				AboutActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});

		btn_call.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				CommonAlertDialog dialog = new CommonAlertDialog.Builder(AboutActivity.this)
						.message(getResources().getString(R.string.STR_ABOUT_CONFIRM_CALL))
						.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
						.positiveListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Global.callPhone(Global.getServiceCall(), AboutActivity.this);
							}
						})
						.build();
				dialog.show();
			}
		});

		btn_feedback.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this, FeedbackActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				AboutActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});

		txt_version = (TextView)findViewById(R.id.txt_version);
		txt_version.setText(Global.getSelfAppVersionName(getApplicationContext()));
	}

	private void initResolution()
	{
		RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
		parent_layout.getViewTreeObserver().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Point ptTemp = getScreenSize();
				boolean bNeedUpdate = false;

				if (mScrSize.x == 0 && mScrSize.y == 0)
				{
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}
				else if (mScrSize.x != ptTemp.x || mScrSize.y != ptTemp.y)
				{
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}

				if (bNeedUpdate)
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ResolutionSet.instance.iterateChild(findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);
						}
					});
				}
			}
		});
	}

	private AsyncHttpResponseHandler version_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					String ver = retdata.getString("latestver");
					int verCode = retdata.getInt("latestver_code");
					String downloadurl = retdata.getString("downloadurl");

					updateApp(ver, verCode, downloadurl);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(AboutActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(AboutActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void updateApp(String version, int verCode, String downloadurl)
	{
		int curVerInt = Global.getSelfAppVersionCode(getApplicationContext());

		if (verCode <= curVerInt)
		{
			Global.showAdvancedToast(AboutActivity.this, getString(R.string.STR_MSG_LAST_VERSION), Gravity.CENTER);
			return;
		}

		showUpdateAlert(version, downloadurl);
	}

	private void showUpdateAlert(final String szVersion, final String szUrl)
	{
		String szMsg = getResources().getString(R.string.STR_ABOUT_UPDATABLE1);
		szMsg += szVersion;
		szMsg += getResources().getString(R.string.STR_ABOUT_UPDATABLE2);

		CommonAlertDialog dialog = new CommonAlertDialog.Builder(AboutActivity.this)
				.message(szMsg)
				.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
				.positiveListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						InstallNewApp(szUrl);
					}
				})
				.build();
		dialog.show();
	}

}
