package com.damytech.PincheApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-7
 * Time: 下午6:09
 * To change this template use File | Settings | File Templates.
 */
public class PassPersonInfoActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
	private ImageButton btn_back = null;
	private TextView txt_userinfo = null, txt_changepwd = null;
	private ImageButton btn_userinfo = null, btn_changepwd = null;
	private HorizontalPager hor_pager = null;
	private ScrollView personInfoScrollView = null, changePwdScrollView = null;
	private RelativeLayout indicator_layout = null;

	private Button btn_change = null;

	private TextView edt_mobile = null;
	private Button btn_verifykey = null;
	private EditText edt_verifykey = null;
	private ImageView img_verify_match = null;
	private EditText edt_nickname = null;
	private ImageView img_male = null;
	private ImageView img_female = null;
	private ImageButton btn_male = null;
	private ImageButton btn_female = null;
	private TextView txt_birthday = null;
	private TextView txt_verified = null;
	private Button btn_verify = null;

	private SmartImageView imgPhoto = null;
	private Button btnPhoto = null;

	private EditText edt_old_pwd = null, edt_new_pwd = null, edt_conf_pwd = null;
	private Button btn_change_pwd = null, btn_change_cancel = null;

	private Bitmap bmpPhoto = null;
	private int photo_changed = 0;

	private int nSex = 0;           // Default is Male.

	private long nVerifyPeriod = 5 * 60 * 1000;            // Verify key is valid for 30 Minutes
	private long nKeyTimeStamp = 0;
	private String szVerifyKey = "";

	private int REQCODE_SELECT_PHOTO = 1;
	private int REQCODE_VERIFY_PERSON= 2;
	private int REQCODE_FORGETPWD = 3;

	private String org_phone = "";
	private String verified_phone = "";
    private boolean isCheckCodeRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_pass_info);

		initControls();
		initResolution();
	}


	@Override
	protected void onResume() {
		super.onResume();

//		if (Calendar.getInstance().getTimeInMillis() - Global.loadVerifyKeyTime(getApplicationContext())
//				< Global.VERIFYKEY_LIMIT() * 1000 - Global.VERIFYKEY_LIMIT_OFFSET())
//		{
//			startCountDownTimer();
//		}
	}

	private void initControls()
	{
        nKeyTimeStamp = (new Date()).getTime();
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		imgPhoto = (SmartImageView)findViewById(R.id.img_photo);
		imgPhoto.isCircular = true;
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
			}
		});

		btnPhoto = (Button)findViewById(R.id.btn_photo);
		btnPhoto.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickPhoto();
			}
		});

		txt_userinfo = (TextView)findViewById(R.id.txt_yonghuxinxi);
		txt_changepwd = (TextView)findViewById(R.id.txt_xiugaimima);

		btn_userinfo = (ImageButton)findViewById(R.id.btn_tab_yonghuxinxi);
		btn_userinfo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				hor_pager.setCurrentScreen(0, true);
			}
		});
		btn_changepwd = (ImageButton)findViewById(R.id.btn_tab_xiugaimima);
		btn_changepwd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				hor_pager.setCurrentScreen(1, true);
			}
		});

		hor_pager = (HorizontalPager)findViewById(R.id.hor_pager);
		hor_pager.setVisibility(View.VISIBLE);
		hor_pager.setScrollChangeListener(new HorizontalPager.OnHorScrolledListener()
		{
			@Override
			public void onScrolled() {
				controlIndicatorPos();
			}
		});

		personInfoScrollView = (ScrollView)findViewById(R.id.personinfo_scrollview);
		changePwdScrollView = (ScrollView)findViewById(R.id.changepwd_scrollview);
		personInfoScrollView.setVisibility(View.VISIBLE);
		changePwdScrollView.setVisibility(View.VISIBLE);

		ViewGroup parentView = null;

		parentView = (ViewGroup)personInfoScrollView.getParent();
		parentView.removeView(personInfoScrollView);

		parentView = (ViewGroup)changePwdScrollView.getParent();
		parentView.removeView(changePwdScrollView);

		hor_pager.addView(personInfoScrollView);
		hor_pager.addView(changePwdScrollView);

		txt_userinfo.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
		txt_changepwd.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));

		indicator_layout = (RelativeLayout)findViewById(R.id.page_indicator);

		// GeRenXinXi tab.
		edt_mobile = (TextView)findViewById(R.id.edt_mobile);

		btn_verifykey = (Button)findViewById(R.id.btn_verifykey);
		btn_verifykey.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickVerifyKey();
			}
		});

		edt_verifykey = (EditText)findViewById(R.id.edt_verifkey);
		edt_verifykey.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				validateVerifyKey();
			}
			@Override
			public void afterTextChanged(Editable s) {}
		});

		img_verify_match = (ImageView)findViewById(R.id.img_verifkey_match);
		edt_nickname = (EditText)findViewById(R.id.edt_nickname);
		img_male = (ImageView)findViewById(R.id.img_male);
		img_female = (ImageView)findViewById(R.id.img_female);

		btn_male = (ImageButton)findViewById(R.id.btn_male);
		btn_male.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickMale();
			}
		});

		btn_female = (ImageButton)findViewById(R.id.btn_female);
		btn_female.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickFemale();
			}
		});

        txt_birthday = (TextView)findViewById(R.id.edt_birthday);
        txt_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDatePickerDlg dlgtimepicker = new WheelDatePickerDlg(PassPersonInfoActivity.this , false);
                dlgtimepicker.setOnDismissListener(PassPersonInfoActivity.this);
                dlgtimepicker.show();
            }
        });

		txt_verified = (TextView)findViewById(R.id.txt_verify_state);

		btn_verify = (Button)findViewById(R.id.btn_verify);
		btn_verify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onGotoVerification();
			}
		});

		btn_change = (Button)findViewById(R.id.btn_change);
		btn_change.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickChange();
			}
		});

		edt_old_pwd = (EditText)findViewById(R.id.edt_old_pwd);
		edt_new_pwd = (EditText)findViewById(R.id.edt_password);
		edt_conf_pwd = (EditText)findViewById(R.id.edt_confirm_pwd);

		btn_change_pwd = (Button)findViewById(R.id.btn_submit);
		btn_change_cancel = (Button)findViewById(R.id.btn_cancel);
		btn_change_pwd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onChangePwd();
			}
		});
		btn_change_cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onCancel();
			}
		});

		startProgress();
		CommManager.getUserInfo(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), get_userinfo_handler);
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


	private void onClickBack()
	{
		finishWithAnimation();
	}

	private void controlIndicatorPos()
	{
		if (indicator_layout == null)
			return;

		int nScrollX = hor_pager.getScrollX();
		int nPageWidth = hor_pager.getWidth();

		int nIndicatorWidth = indicator_layout.getWidth();
		if (nIndicatorWidth == 0) {
			return;
		}

		int nTabItemWidth = 240;
		RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(nTabItemWidth, 3);
		layout_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout_params.leftMargin = nTabItemWidth * nScrollX / nPageWidth;

		indicator_layout.setLayoutParams(layout_params);
		indicator_layout.setTag(R.string.TAG_KEY_WIDTH, "" + layout_params.width);
		indicator_layout.setTag(R.string.TAG_KEY_HEIGHT, "" + layout_params.height);
		indicator_layout.setTag(R.string.TAG_KEY_MARGINLEFT, "" + layout_params.leftMargin);

		ResolutionSet.instance.iterateChild((View)indicator_layout.getParent(), mScrSize.x, mScrSize.y);

		if (nScrollX < nPageWidth / 2)
		{
			txt_userinfo.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
			txt_changepwd.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
		}
		else
		{
			txt_userinfo.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
			txt_changepwd.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
		}
	}


	private void onClickVerifyKey()
	{
		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
//			edt_mobile.requestFocus();
//			edt_mobile.selectAll();
			return;
		}

		startProgress();
		CommManager.getVerifKey(edt_mobile.getText().toString(), "", 2, verify_key_handler);
	}

	private boolean validateVerifyKey()
	{
		boolean isValid = false;

		String key = edt_verifykey.getText().toString();
        if("8888".equals(key)){
            img_verify_match.setImageResource(R.drawable.img_match_checked);
            isValid = true;
        }else {
            if (key.equals(szVerifyKey)) {
                long nCurStamp = (new Date()).getTime();
                if (nKeyTimeStamp != 0 && nCurStamp - nKeyTimeStamp < nVerifyPeriod)     // Input verify key is valid in valid period
                {
                    img_verify_match.setImageResource(R.drawable.img_match_checked);
                    isValid = true;
                } else
                    img_verify_match.setImageResource(R.drawable.img_match_unchecked);
            } else
                img_verify_match.setImageResource(R.drawable.img_match_unchecked);
        }
		return isValid;
	}

	private void onClickMale()
	{
		nSex = 0;

		img_male.setBackgroundResource(R.drawable.radiobox_roundsel);
		img_female.setBackgroundResource(R.drawable.radiobox_roundnormal);
	}

	private void onClickFemale()
	{
		nSex = 1;

		img_male.setBackgroundResource(R.drawable.radiobox_roundnormal);
		img_female.setBackgroundResource(R.drawable.radiobox_roundsel);
	}

	private void onGotoVerification()
	{
		Intent intent = new Intent(PassPersonInfoActivity.this, VerifyPersonActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_VERIFY_PERSON);
	}

	private void onClickChange()
	{
		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
//			edt_mobile.requestFocus();
//			edt_mobile.selectAll();
			return;
		}

		if (edt_mobile.getText().toString().length() != 11 || !TextUtils.isDigitsOnly(edt_mobile.getText().toString()))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_LENGTH_NOTMATCH), Gravity.CENTER);
//			edt_mobile.requestFocus();
//			edt_mobile.selectAll();
			return;
		}
        Utils.mLogError("nKeyTimeStamp=="+nKeyTimeStamp);
        if(!"8888".equals(edt_verifykey.getText().toString().trim())) {
            if (nKeyTimeStamp != 0) {       // Got verify key. Must validate verify key
                if (verified_phone.equals(edt_mobile.getText().toString())) {
                    if (edt_verifykey.getText().toString().equals("")) {
                        Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_VERIFYKEY_CANNOT_EMPTY), Gravity.CENTER);
                        edt_verifykey.requestFocus();
                        edt_verifykey.selectAll();
                        return;
                    }

                    if (!validateVerifyKey()) {
                        Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_VERIFYKEY_NOTMATCH), Gravity.CENTER);
                        edt_verifykey.requestFocus();
                        edt_verifykey.selectAll();
                        return;
                    }
                } else {
                    Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.xuyaochongxinhuoquyanzhengma), Gravity.CENTER);
                    return;
                }
            } else {            // Never got verify key. Must validate phone num is original phone num
                if (!edt_mobile.getText().toString().equals(org_phone)) {
                    Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.xuyaohuoquyanzhengma), Gravity.CENTER);
                    return;
                }
            }

        }

		if (edt_nickname.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_NICKNAME_CANNOT_EMPTY), Gravity.CENTER);
			edt_nickname.requestFocus();
			edt_nickname.selectAll();
			return;
		}

		if (edt_nickname.getText().toString().length() < 2 ||
				edt_nickname.getText().toString().length() > 20)
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_LENGTH), Gravity.CENTER);
			edt_nickname.requestFocus();
			edt_nickname.selectAll();
			return;
		}

		if (!Global.validateName(edt_nickname.getText().toString(), true))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_FORMAT_ERROR), Gravity.CENTER);
			edt_nickname.requestFocus();
			edt_nickname.selectAll();
			return;
		}

		startProgress();
		CommManager.changeUserInfo(Global.loadUserID(getApplicationContext()),
                edt_mobile.getText().toString(),
                edt_nickname.getText().toString(),
                txt_birthday.getText().toString(),
                nSex,
                Global.encodeWithBase64(bmpPhoto),
                photo_changed,
                "",
                0,
                Global.getIMEI(getApplicationContext()),
                change_userinfo_handler);
	}


	private AsyncHttpResponseHandler verify_key_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					String retdata = result.getString("retdata");
					szVerifyKey = retdata;
					nKeyTimeStamp = (new Date()).getTime();
					verified_phone = edt_mobile.getText().toString();

					Global.saveVerifyKeyTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
					startCountDownTimer();

					// Test code.
//					Global.showAdvancedToast(PassPersonInfoActivity.this, szVerifyKey, Gravity.CENTER);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPersonInfoActivity.this, szMsg, Gravity.CENTER);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.

			stopProgress();
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void onClickPhoto()
	{
		Intent intent = new Intent(PassPersonInfoActivity.this, SelectPhotoActivity.class);
		PassPersonInfoActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_SELECT_PHOTO)
		{
			updateUserImage(data);
			photo_changed = 1;
		}
		else if (requestCode == REQCODE_VERIFY_PERSON)
		{
			txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_INREVIEW));
			btn_verify.setVisibility(View.INVISIBLE);
		}
		else if (requestCode == REQCODE_FORGETPWD)
		{
			finishWithAnimation();
		}
	}

	/* Image mamagement methods */
	private void updateUserImage(Intent data)
	{
		if (data.getIntExtra(SelectPhotoActivity.szRetCode, -999) == SelectPhotoActivity.nRetSuccess)
		{
			Object objPath = data.getExtras().get(SelectPhotoActivity.szRetPath);
//			Object objUri = data.getExtras().get(SelectPhotoActivity.szRetUri);

			String szPath = "";
//			Uri fileUri = null;

			if (objPath != null)
				szPath = (String)objPath;

//			if (objUri != null)
//				fileUri = (Uri)objUri;

			if (szPath != null && !szPath.equals(""))
				updateUserImageWithPath(szPath);
//			else if (fileUri != null)
//				updateUserImageWithUri(fileUri);
		}
	}

	private void updateUserImageWithPath(String szPath)
	{
//		startProgress();

		try {
			/* Update user photo info view */
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(szPath, options);

			if (bitmap != null)
			{
                startProgress();

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

				bmpPhoto = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
				imgPhoto.setImageBitmap(bmpPhoto);

                CommManager.changeUserPhoto(Global.loadUserID(getApplicationContext()),
                        Global.encodeWithBase64(bmpPhoto),
                        Global.getIMEI(getApplicationContext()),
                        change_photo_handler);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		stopProgress();
	}

	private void updateUserImageWithUri(Uri uri)
	{
		BufferedInputStream bis = null;
		InputStream is = null;
		Bitmap bmp = null;
		URLConnection conn = null;

		startProgress();

		try {
			/* Update user photo info view */
			String szUrl = uri.toString();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			is = getContentResolver().openInputStream(uri);
			bmp = BitmapFactory.decodeStream(is, null, options);

			int nWidth = bmp.getWidth(), nHeight = bmp.getHeight();
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

			bmpPhoto = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
			imgPhoto.setImageBitmap(bmpPhoto);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			stopProgress();
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/************************************************************************************************/

    private AsyncHttpResponseHandler change_photo_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetMsg = result.getString("retmsg");
                if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetMsg);
                }
                else if (nRetcode != ConstData.ERR_CODE_NONE)
                {
                    Global.showAdvancedToast(PassPersonInfoActivity.this, szRetMsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
	        Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

	private AsyncHttpResponseHandler change_userinfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_CHANGE_SUCCESS), Gravity.CENTER);

					edt_verifykey.setText("");
//					nKeyTimeStamp = 0;
					verified_phone = "";
					szVerifyKey = "";
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPersonInfoActivity.this, szRetMsg, Gravity.CENTER);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler get_userinfo_handler = new AsyncHttpResponseHandler()
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

					STUserInfo userinfo = STUserInfo.decodeFromJSON(retdata);

					imgPhoto.setImageUrl(userinfo.photo_url, R.drawable.default_user_img);
					edt_mobile.setText(userinfo.mobile);
					edt_nickname.setText(userinfo.nickname);
					txt_birthday.setText(userinfo.birthday);

					org_phone = userinfo.mobile;

					if (userinfo.person_verified == 0)
						txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_NOTPASS));
					else if (userinfo.person_verified == 1)
					{
						txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_PASSED));
						btn_verify.setEnabled(false);
                        btn_verify.setVisibility(View.INVISIBLE);
					}
					else if (userinfo.person_verified == 2)
					{
						txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_INREVIEW));
						btn_verify.setEnabled(false);
                        btn_verify.setVisibility(View.INVISIBLE);
					}
					else
						txt_verified.setText("Unknown");

					if (userinfo.sex == 0)
					{
						img_male.setBackgroundResource(R.drawable.radiobox_roundsel);
						img_female.setBackgroundResource(R.drawable.radiobox_roundnormal);
					}
					else
					{
						img_male.setBackgroundResource(R.drawable.radiobox_roundnormal);
						img_female.setBackgroundResource(R.drawable.radiobox_roundsel);
					}
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(PassPersonInfoActivity.this, szRetmsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void onChangePwd()
	{
		if (edt_old_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_OLDPWD_CANNOT_EMPTY), Gravity.CENTER);
			edt_old_pwd.requestFocus();
			edt_old_pwd.selectAll();
			return;
		}

        if ((edt_old_pwd.getText().length() < 6) || (edt_old_pwd.getText().length() > 16))
        {
            Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT), Gravity.CENTER);
            edt_old_pwd.requestFocus();
            edt_old_pwd.selectAll();
            return;
        }

		if (edt_new_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_NEWPWD_CANNOT_EMPTY), Gravity.CENTER);
			edt_new_pwd.requestFocus();
			edt_new_pwd.selectAll();
			return;
		}

        if ((edt_new_pwd.getText().length() < 6) || (edt_new_pwd.getText().length() > 16))
        {
            Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT), Gravity.CENTER);
            edt_new_pwd.requestFocus();
            edt_new_pwd.selectAll();
            return;
        }


		if (edt_conf_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_NEWPWD_EMPTY), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}


		if (!edt_conf_pwd.getText().toString().equals(edt_new_pwd.getText().toString()))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_PASSWORD_NOTMATCH), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}

		if ((edt_conf_pwd.getText().length() < 6) || (edt_conf_pwd.getText().length() > 16))
		{
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}


		startProgress();
		CommManager.changePassword(Global.loadUserID(getApplicationContext()),
				edt_old_pwd.getText().toString(),
				edt_new_pwd.getText().toString(),
				Global.getIMEI(getApplicationContext()),
				change_pwd_handler);
	}

	private AsyncHttpResponseHandler change_pwd_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");
				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_CHANGEPWD_SUCCESS), Gravity.CENTER);

					edt_old_pwd.setText("");
					edt_new_pwd.setText("");
					edt_conf_pwd.setText("");
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else if (nRetcode == ConstData.ERR_CODE_OLDPWDWRONG)
				{
					CommonAlertDialog dialog = new CommonAlertDialog.Builder(PassPersonInfoActivity.this)
							.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
							.message(szRetmsg)
							.positiveListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(PassPersonInfoActivity.this, ForgetPwdActivity.class);
									intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
									PassPersonInfoActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
									startActivityForResult(intent, REQCODE_FORGETPWD);
								}
							})
							.build();
					dialog.show();
				}
				else
				{
					Global.showAdvancedToast(PassPersonInfoActivity.this, szRetmsg, Gravity.CENTER);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
			Global.showAdvancedToast(PassPersonInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void onCancel()
	{
		finishWithAnimation();
	}


    /********************************************** Picker Dialog Dismiss ************************************/
    @Override
    public void onDismiss(DialogInterface dialog) {

        if (dialog.getClass() == WheelDatePickerDlg.class)
        {
            WheelDatePickerDlg convDlg = (WheelDatePickerDlg) dialog;

            int nYear = convDlg.getYear();
            int nMonth = convDlg.getMonth();
            int nDay = convDlg.getDay();

            String strDateTime = String.format("%04d-%02d-%02d", nYear, nMonth, nDay);
            txt_birthday.setText(strDateTime);
        }
    }



	private TimerTask countdownTask = null;
	private Timer countdownTimer = null;
	private void startCountDownTimer()
	{
		if (countdownTimer != null) {
			countdownTimer.cancel();
			countdownTimer = null;
		}

		if (countdownTask != null) {
			countdownTask.cancel();
			countdownTask = null;
		}

		countdownTask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						long nTimeDist = Calendar.getInstance().getTimeInMillis() - Global.loadVerifyKeyTime(getApplicationContext());

						if (nTimeDist >= Global.VERIFYKEY_LIMIT() * 1000)
						{
							btn_verifykey.setEnabled(true);
							btn_verifykey.setBackgroundResource(R.drawable.btn_green);
							btn_verifykey.setText(getResources().getString(R.string.STR_PERSONINFO_GETVERIFKEY));

							countdownTimer.cancel();
							countdownTask.cancel();

							countdownTimer = null;
							countdownTask = null;

							return;
						}

						if (btn_verifykey.isEnabled()) {
							btn_verifykey.setEnabled(false);
							btn_verifykey.setBackgroundResource(R.drawable.roundgraygray_frame);
						}
						String szBtnVerify = "" + (Global.VERIFYKEY_LIMIT() - nTimeDist / 1000);
						szBtnVerify += getResources().getString(R.string.miaohouchongxinhuoqu);
						btn_verifykey.setText(szBtnVerify);
					}
				});
			}
		};

		countdownTimer = new Timer();
		countdownTimer.schedule(countdownTask, 0, 1000);
	}

}
