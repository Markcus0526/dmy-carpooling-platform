package com.damytech.MainApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-8
 * Time: 上午4:01
 * To change this template use File | Settings | File Templates.
 */
public class VerifyActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private ImageView imgIDFore = null, imgIDBack = null;
	private ImageButton btnIDFore = null, btnIDBack = null;
	private Button btnUpload = null;

	private Bitmap bmpForeImage = null, bmpBackImage = null;

	private int REQCODE_IDFOREIMAGE = 1;
	private int REQCODE_IDBACKIMAGE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_verifyperson);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		imgIDFore = (ImageView)findViewById(R.id.img_idcard_fore);
		imgIDBack = (ImageView)findViewById(R.id.img_idcard_back);
		btnIDFore = (ImageButton)findViewById(R.id.btn_idcard_foreimage);
		btnIDBack = (ImageButton)findViewById(R.id.btn_idcard_backimage);
		btnUpload = (Button)findViewById(R.id.btn_upload);

		btnIDFore.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectForeImage();
			}
		});
		btnIDBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectBackImage();
			}
		});
		btnUpload.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickUpload();
			}
		});

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});

		setResult(RESULT_CANCELED);

        updateUserImageWithPath(Global.loadPersonFrontPath(getApplicationContext()), true);
        updateUserImageWithPath(Global.loadPersonBackPath(getApplicationContext()), false);
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

	private void onSelectForeImage()
	{
		Intent intent = new Intent(VerifyActivity.this, SelectPhotoActivity.class);
		VerifyActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
        intent.putExtra(Global.PHOTO_TYPE(), Global.PHOTO_TYPE_PERSON_FRONT());
		startActivityForResult(intent, REQCODE_IDFOREIMAGE);
	}

	private void onSelectBackImage()
	{
		Intent intent = new Intent(VerifyActivity.this, SelectPhotoActivity.class);
		VerifyActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
        intent.putExtra(Global.PHOTO_TYPE(), Global.PHOTO_TYPE_PERSON_BACK());
		startActivityForResult(intent, REQCODE_IDBACKIMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_IDFOREIMAGE)
			updateUserImage(data, true);
		else if (requestCode == REQCODE_IDBACKIMAGE)
			updateUserImage(data, false);
	}

	/* Image mamagement methods */
	private void updateUserImage(Intent data, boolean isFore)
	{
		if (data.getIntExtra(SelectPhotoActivity.szRetCode, -999) == SelectPhotoActivity.nRetSuccess)
		{
			Object objPath = data.getExtras().get(SelectPhotoActivity.szRetPath);
			String szPath = "";

			if (objPath != null) {
                szPath = (String) objPath;
                if(isFore)
                {
                    Global.savePersonFrontPath(getApplicationContext(), szPath);
                }
                else
                {
                    Global.savePersonBackPath(getApplicationContext(), szPath);
                }
            }

			if (szPath != null && !szPath.equals(""))
				updateUserImageWithPath(szPath, isFore);
		}
	}

	private void updateUserImageWithPath(String szPath, boolean isFore)
	{
        if(szPath == null || szPath.length() == 0)
            return;

		startProgress();

		try {
			/* Update user photo info view */
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(szPath, options);

			if (bitmap != null)
			{
				int nWidth = bitmap.getWidth(), nHeight = bitmap.getHeight();
				int nScaledWidth = 0, nScaledHeight = 0;
				if (nWidth > nHeight)
				{
					nScaledWidth = SelectPhotoActivity.IMAGE_WIDTH;
					nScaledHeight = nScaledWidth * nHeight / nWidth;
				}
				else
				{
					nScaledHeight = SelectPhotoActivity.IMAGE_HEIGHT;
					nScaledWidth = nScaledHeight * nWidth / nHeight;
				}

				if (isFore)
				{
					bmpForeImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
					imgIDFore.setImageBitmap(bmpForeImage);
				}
				else
				{
					bmpBackImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
					imgIDBack.setImageBitmap(bmpBackImage);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		stopProgress();
	}


	private void onClickUpload()
	{
		if (bmpForeImage == null)
		{
			Global.showAdvancedToast(VerifyActivity.this, getResources().getString(R.string.STR_VERIFYPERSON_FOREIMAGE_EMPTY), Gravity.CENTER);
			return;
		}

		if (bmpBackImage == null)
		{
			Global.showAdvancedToast(VerifyActivity.this, getResources().getString(R.string.STR_VERIFYPERSON_BACKIMAGE_EMPTY), Gravity.CENTER);
			return;
		}

		startProgress();
		CommManager.verifyPersonInfo(Global.loadUserID(getApplicationContext()), Global.encodeWithBase64(bmpForeImage), Global.encodeWithBase64(bmpBackImage), Global.getIMEI(getApplicationContext()), upload_handler);
	}

	private AsyncHttpResponseHandler upload_handler = new AsyncHttpResponseHandler()
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

				if (nRetcode == 0)
				{
					Global.showAdvancedToast(VerifyActivity.this, getResources().getString(R.string.STR_VERIFYPERSON_APPLY_SUCCESS), Gravity.CENTER);
					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetcode == -2)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(VerifyActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            Global.showAdvancedToast(VerifyActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};
}
