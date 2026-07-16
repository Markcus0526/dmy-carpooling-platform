package com.damytech.MainApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-7
 * Time: 下午6:09
 * To change this template use File | Settings | File Templates.
 */
public class PersonInfoActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
	private ImageButton btn_back = null;
	private TextView txt_userinfo = null, txt_changepwd = null;
	private ImageButton btn_userinfo = null, btn_changepwd = null;
	private HorizontalPager hor_pager = null;
	private ScrollView personInfoScrollView = null, changePwdScrollView = null;
	private RelativeLayout indicator_layout = null;

	private Button btn_change = null;

	private String old_mobile = "";
	private EditText edt_mobile = null;
	private Button btn_verifykey = null;
	private EditText edt_verifykey = null;
    private TextView txtSeconds = null;
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

	private long nVerifyPeriod = 30 * 60 * 1000;            // Verify key is valid for 30 Minutes
	private long nKeyTimeStamp = 0;
	private String szVerifyKey = "";

	private int REQCODE_SELECT_PHOTO = 1;
	private int REQCODE_VERIFY_PERSON= 2;

    private CountDownTimer mCodeTimer = null;
    private int nYear = 0, nMonth = 0, nDay = 0;
    private int nVerifiedState = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_personinfo);

		initControls();
		initResolution();
	}

    protected void onResume()
    {
        super.onResume();

        startProgress();
        CommManager.getUserInfo(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), get_userinfo_handler);
    }

	private void initControls()
	{
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
		edt_mobile = (EditText)findViewById(R.id.edt_mobile);

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
        txtSeconds = (TextView)findViewById(R.id.txt_seconds);

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

		txt_birthday = (TextView)findViewById(R.id.txt_birthday);

		txt_verified = (TextView)findViewById(R.id.txt_verify_state);

		btn_verify = (Button)findViewById(R.id.btn_verify);
		btn_verify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onGotoVerification();
			}
		});

        txt_birthday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onClickBirthday();
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
        String szPhone = edt_mobile.getText().toString();

        if (szPhone.equals(""))
        {
            Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
            edt_mobile.requestFocus();
            edt_mobile.selectAll();
            return;
        }

        if (szPhone.length() != Global.PHONE_LENGTH())
        {
            Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_MSG_PHONENUM_LEN), Gravity.CENTER);
            edt_mobile.requestFocus();
            edt_mobile.selectAll();
            return;
        }

        btn_verifykey.setBackgroundResource(R.drawable.btn_gray);
        btn_verifykey.setEnabled(false);

        txtSeconds.setVisibility(View.VISIBLE);
        mCodeTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtSeconds.setText("" + millisUntilFinished / 1000 + getResources().getString(R.string.STR_COMMON_MIAO));
            }
            @Override
            public void onFinish() {
                btn_verifykey.setBackgroundResource(R.drawable.btn_green);
                btn_verifykey.setEnabled(true);
                txtSeconds.setVisibility(View.INVISIBLE);
            }
        };
        mCodeTimer.start();

		startProgress();
		CommManager.getVerifKey(edt_mobile.getText().toString(), verify_key_handler);
	}

	private boolean validateVerifyKey()
	{
		boolean isValid = false;

		String key = edt_verifykey.getText().toString();
		if (key.equals(szVerifyKey))
		{
			long nCurStamp = (new Date()).getTime();
			if (nKeyTimeStamp != 0 && nCurStamp - nKeyTimeStamp < nVerifyPeriod)     // Input verify key is valid in valid period
			{
				img_verify_match.setImageResource(R.drawable.img_match_checked);
				isValid = true;
			}
			else
				img_verify_match.setImageResource(R.drawable.img_match_unchecked);
		}
		else
			img_verify_match.setImageResource(R.drawable.img_match_unchecked);

		return isValid;
	}

	private void onClickMale()
	{
        if(nVerifiedState != 0) {
            Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_NO_CHANGE_SEX), Gravity.CENTER);
            return;
        }

		nSex = 0;
		img_male.setBackgroundResource(R.drawable.radiobox_sel);
		img_female.setBackgroundResource(R.drawable.radiobox_normal);
	}

	private void onClickFemale()
	{
        if(nVerifiedState != 0)
        {
            Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_NO_CHANGE_SEX), Gravity.CENTER);
            return;
        }

		nSex = 1;
		img_male.setBackgroundResource(R.drawable.radiobox_normal);
		img_female.setBackgroundResource(R.drawable.radiobox_sel);
	}

	private void onGotoVerification()
	{
		Intent intent = new Intent(PersonInfoActivity.this, VerifyActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_VERIFY_PERSON);
	}

	private void onClickChange()
	{
		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}

		if (!old_mobile.equals(edt_mobile.getText().toString()) &&
			edt_verifykey.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_VERIFYKEY_CANNOT_EMPTY), Gravity.CENTER);
			edt_verifykey.requestFocus();
			edt_verifykey.selectAll();
			return;
		}

		if (edt_nickname.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_NICKNAME_CANNOT_EMPTY), Gravity.CENTER);
			edt_nickname.requestFocus();
			edt_nickname.selectAll();
			return;
		}

		if (txt_birthday.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_BIRTHDAY_CANNOT_EMPTY), Gravity.CENTER);
			return;
		}

		if (!validateVerifyKey())
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_VERIFYKEY_NOTMATCH), Gravity.CENTER);
			edt_verifykey.requestFocus();
			edt_verifykey.selectAll();
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

    private void onClickBirthday()
    {

        if(nVerifiedState != 0)
        {
            Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_NO_CHANGE_BIRTHDAY), Gravity.CENTER);
            return;
        }

        WheelDatePickerDlg dlgtimepicker = new WheelDatePickerDlg(PersonInfoActivity.this);
        dlgtimepicker.setOnDismissListener(PersonInfoActivity.this);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, nYear);
        cal.set(Calendar.MONTH, nMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, nDay);
        dlgtimepicker.setCurDate(cal.getTime());

        dlgtimepicker.show();
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

				if (nRetcode == 0)
				{
					String retdata = result.getString("retdata");
					szVerifyKey = retdata;
					nKeyTimeStamp = (new Date()).getTime();

					// Test code.
					Global.showAdvancedToast(PersonInfoActivity.this, szVerifyKey, Gravity.CENTER);
				}
				else
				{
					Global.showAdvancedToast(PersonInfoActivity.this, szMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void onClickPhoto()
	{
		Intent intent = new Intent(PersonInfoActivity.this, SelectPhotoActivity.class);
		PersonInfoActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (requestCode == REQCODE_SELECT_PHOTO && resultCode == RESULT_OK)
		{
			updateUserImage(data);
			photo_changed = 1;
		}
		else if (requestCode == REQCODE_VERIFY_PERSON && resultCode == RESULT_OK)
		{
			txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_INREVIEW));
		}
	}

	/* Image mamagement methods */
	private void updateUserImage(Intent data)
	{
		if (data.getIntExtra(SelectPhotoActivity.szRetCode, -999) == SelectPhotoActivity.nRetSuccess)
		{
			Object objPath = data.getExtras().get(SelectPhotoActivity.szRetPath);
			String szPath = "";

			if (objPath != null)
				szPath = (String)objPath;

			if (szPath != null && !szPath.equals(""))
				updateUserImageWithPath(szPath);
		}
	}

	private void updateUserImageWithPath(String szPath)
	{
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

				bmpPhoto = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
				imgPhoto.setImage(new SmartImage() {
                    @Override
                    public Bitmap getBitmap(Context context) {
                        return bmpPhoto;
                    }
                });
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		stopProgress();
	}



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
				if (nRetcode == 0)
				{
					Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_CHANGE_SUCCESS), Gravity.CENTER);
				}
				else if (nRetcode == -2)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PersonInfoActivity.this, szRetMsg, Gravity.CENTER);
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

				if (nRetcode == 0)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					STUserInfo userinfo = STUserInfo.decodeFromJSON(retdata);

					imgPhoto.setImageUrl(userinfo.photo_url);
					edt_mobile.setText(userinfo.mobile);
					old_mobile = userinfo.mobile;
					edt_nickname.setText(userinfo.nickname);
					txt_birthday.setText(userinfo.birthday);

                    if(userinfo.birthday.length() > 0)
                    {
                        String[] ssNumber = userinfo.birthday.split("-");
                        if(ssNumber.length >= 3)
                        {
                            nYear = Integer.parseInt(ssNumber[0]);
                            nMonth = Integer.parseInt(ssNumber[1]);
                            nDay = Integer.parseInt(ssNumber[2]);
                        }
                    }

					if (userinfo.person_verified == 0)
						txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_NOTPASS));
					else if (userinfo.person_verified == 1)
					{
						txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_PASSED));
                        btn_verify.setVisibility(View.GONE);
					}
					else if (userinfo.person_verified == 2)
					{
						txt_verified.setText(getResources().getString(R.string.STR_PERSONINFO_STATE_INREVIEW));
                        btn_verify.setVisibility(View.GONE);
					}
					else
						txt_verified.setText("Unknown");

                    nVerifiedState = userinfo.person_verified;

					if (userinfo.sex == 0)
					{
						img_male.setBackgroundResource(R.drawable.radiobox_sel);
						img_female.setBackgroundResource(R.drawable.radiobox_normal);
					}
					else
					{
						img_male.setBackgroundResource(R.drawable.radiobox_normal);
						img_female.setBackgroundResource(R.drawable.radiobox_sel);
					}
				}
				else if (nRetcode == -2)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(PersonInfoActivity.this, szRetmsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
		}
	};


	private void onChangePwd()
	{
		if (edt_old_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_OLDPWD_CANNOT_EMPTY), Gravity.CENTER);
			edt_old_pwd.requestFocus();
			edt_old_pwd.selectAll();
			return;
		}

		if (edt_new_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_NEWPWD_CANNOT_EMPTY), Gravity.CENTER);
			edt_new_pwd.requestFocus();
			edt_new_pwd.selectAll();
			return;
		}

		if (!edt_conf_pwd.getText().toString().equals(edt_new_pwd.getText().toString()))
		{
			Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_PASSWORD_NOTMATCH), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}

        if (edt_conf_pwd.getText().toString().length() < 6 ||
                edt_conf_pwd.getText().toString().length() > 16)
        {
            Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_PASSWORD_LENGTH_ERROR), Gravity.CENTER);
            edt_conf_pwd.requestFocus();
            edt_conf_pwd.selectAll();
            return;
        }

		startProgress();
		CommManager.changePassword(Global.loadUserID(getApplicationContext()), edt_old_pwd.getText().toString(), edt_new_pwd.getText().toString(), Global.getIMEI(getApplicationContext()), change_pwd_handler);
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

				if (nRetcode == 0)
				{
					Global.showAdvancedToast(PersonInfoActivity.this, getResources().getString(R.string.STR_PERSONINFO_CHANGEPWD_SUCCESS), Gravity.CENTER);
				}
				else if (nRetcode == -2)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(PersonInfoActivity.this, szRetmsg, Gravity.CENTER);
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

            nYear = convDlg.getYear();
            nMonth = convDlg.getMonth();
            nDay = convDlg.getDay();

            String strDateTime = String.format("%04d-%02d-%02d", nYear, nMonth, nDay);
            txt_birthday.setText(strDateTime);
        }
    }
}
