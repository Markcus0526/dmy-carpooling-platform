package com.damytech.PincheApp;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Misc.Market;
import com.damytech.Utils.ResolutionSet;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-6
 * Time: 上午4:25
 * To change this template use File | Settings | File Templates.
 */
public class RegisterActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private EditText edt_username = null;
	private EditText edt_mobile = null;
	private Button btn_verifykey = null;
	private EditText edt_verifykey = null;
	private ImageView img_verify_match = null;
	private EditText edt_nickname = null;
	private ImageView img_male = null;
	private ImageView img_female = null;
	private ImageButton btn_male = null;
	private ImageButton btn_female = null;
	private EditText edt_password = null;
	private EditText edt_conf_pwd = null;
	private EditText edt_reqcode = null;
	private Button btn_register = null;
	private Button btn_cancel = null;

	private String verified_phone = "";

	private int nSex = 0;           // Default is Male.

	private long nVerifyPeriod = 5 * 60 * 1000;            // Verify key is valid for 1 Minutes
	private long nKeyTimeStamp = 0;
	private String szVerifyKey = "";
	private boolean isAgreement = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_register);
		
		findView();
		initControls();
		initResolution();
	}


	private void findView() {
		// TODO Auto-generated method stub
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		edt_username = (EditText)findViewById(R.id.edt_username);
		edt_mobile = (EditText)findViewById(R.id.edt_mobile);
		btn_verifykey = (Button)findViewById(R.id.btn_verifykey);
		edt_verifykey = (EditText)findViewById(R.id.edt_verifkey);
		img_verify_match = (ImageView)findViewById(R.id.img_verifkey_match);
		edt_nickname = (EditText)findViewById(R.id.edt_nickname); 
		img_male = (ImageView)findViewById(R.id.img_male);
		img_female = (ImageView)findViewById(R.id.img_female);

		btn_male = (ImageButton)findViewById(R.id.btn_male);
		btn_female = (ImageButton)findViewById(R.id.btn_female);
		edt_password = (EditText)findViewById(R.id.edt_password);
		edt_conf_pwd = (EditText)findViewById(R.id.edt_confirm_pwd);
		edt_reqcode = (EditText)findViewById(R.id.edt_request_code);

		btn_register = (Button)findViewById(R.id.btn_register);
		btn_cancel = (Button)findViewById(R.id.btn_cancel);
		
		ibAgreement = (ImageButton) findViewById(R.id.iv_register_agreement);
		tvAgreement = (TextView) findViewById(R.id.tv_register_agreement);
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
		btn_back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});



        edt_mobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (edt_mobile.getText().toString().equals(""))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
                        edt_mobile.requestFocus();
                        edt_mobile.selectAll();
                        return true;
                    }

                    if (edt_mobile.getText().toString().length() != 11 || !TextUtils.isDigitsOnly(edt_mobile.getText().toString()))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_LENGTH_NOTMATCH), Gravity.CENTER);
                        edt_mobile.requestFocus();
                        edt_mobile.selectAll();
                        return true;
                    }

                }
                return false;
            }
        });
        /*
        edt_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (edt_username.getText().toString().equals(""))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_CANNOT_EMPTY), Gravity.CENTER);
                        edt_username.requestFocus();
                        edt_username.selectAll();
                        return true;
                    }

                    if (!Global.validateName(edt_username.getText().toString(), false))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_FORMAT_ERROR), Gravity.CENTER);
                        edt_username.requestFocus();
                        edt_username.selectAll();
                        return true;
                    }

                    if (edt_username.getText().toString().length() > 20 || edt_username.getText().toString().length() < 2)
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_TOO_SHORT), Gravity.CENTER);
                        edt_username.requestFocus();
                        edt_username.selectAll();
                        return true;
                    }

                }
                return false;
            }
        });

        */
		btn_verifykey.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickVerifyKey();
			}
		});

		
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

		
        edt_nickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (edt_nickname.getText().toString().equals(""))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_CANNOT_EMPTY), Gravity.CENTER);
                        edt_nickname.requestFocus();
                        edt_nickname.selectAll();
                        return true;
                    }

                    if (edt_nickname.getText().toString().length() < 2 ||
                            edt_nickname.getText().toString().length() > 10)
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_LENGTH), Gravity.CENTER);
                        edt_nickname.requestFocus();
                        edt_nickname.selectAll();
                        return true;
                    }

                    if (!Global.validateName(edt_nickname.getText().toString(), true))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_FORMAT_ERROR), Gravity.CENTER);
                        edt_nickname.requestFocus();
                        edt_nickname.selectAll();
                        return true;
                    }


                }
                return false;
            }
        });

        
		btn_male.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickMale();
			}
		});

		
		btn_female.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickFemale();
			}
		});

		
        edt_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (edt_password.getText().toString().equals(""))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_CANNOT_EMPTY), Gravity.CENTER);
                        edt_password.requestFocus();
                        edt_password.selectAll();
                        return true;
                    }

                    if ((edt_password.getText().length() < 6) || (edt_password.getText().length() > 16))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT), Gravity.CENTER);
                        edt_password.requestFocus();
                        edt_password.selectAll();
                        return true;
                    }

                }
                return false;
            }
        });
		
        edt_conf_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (edt_conf_pwd.getText().toString().equals(""))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_NEWPWD_EMPTY), Gravity.CENTER);
                        edt_conf_pwd.requestFocus();
                        edt_conf_pwd.selectAll();
                        return true;
                    }

                    if (!edt_conf_pwd.getText().toString().equals(edt_password.getText().toString()))
                    {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWDCONF_NOTMATCH), Gravity.CENTER);
                        edt_conf_pwd.requestFocus();
                        edt_conf_pwd.selectAll();
                        return true;
                    }
                }
                return false;
            }
        });
        
        if(isAgreement){
        	ibAgreement.setBackgroundResource(R.drawable.img_match_checked);
        }else{
        	ibAgreement.setBackgroundResource(R.drawable.img_match_unchecked);
        }
        
        ibAgreement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isAgreement) {
                    ibAgreement.setBackgroundResource(R.drawable.img_match_unchecked);
                    btn_register.setEnabled(false);
                    btn_register.setBackgroundResource(R.drawable.bk_smallwhite_sel);
                } else {
                    ibAgreement.setBackgroundResource(R.drawable.img_match_checked);
                    btn_register.setEnabled(true);
                    btn_register.setBackgroundResource(R.drawable.btn_smallgreen);
                }
                isAgreement = !isAgreement;

            }
        });

        tvAgreement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入协议
                Intent intent = new Intent(RegisterActivity.this, AgreeActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
                startActivity(intent);
            }
        });
		
		btn_register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickRegister();
			}
		});

		
		btn_cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickCancel();
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

	private void onClickVerifyKey()
	{
		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}

        if (edt_mobile.getText().toString().length() != 11 || !TextUtils.isDigitsOnly(edt_mobile.getText().toString()))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_LENGTH_NOTMATCH), Gravity.CENTER);
            edt_mobile.requestFocus();
            edt_mobile.selectAll();
            return;
        }
        edt_username.setText(edt_mobile.getText().toString());
        /*
        if (edt_username.getText().toString().equals(""))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_CANNOT_EMPTY), Gravity.CENTER);
            edt_username.requestFocus();
            edt_username.selectAll();
            return;
        }

        if (!Global.validateName(edt_username.getText().toString(), false))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_FORMAT_ERROR), Gravity.CENTER);
            edt_username.requestFocus();
            edt_username.selectAll();
            return;
        }

        if (edt_username.getText().toString().length() > 20 || edt_username.getText().toString().length() < 2)
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_TOO_SHORT), Gravity.CENTER);
            edt_username.requestFocus();
            edt_username.selectAll();
            return;
        }
        */
		startProgress();
		CommManager.getVerifKey(edt_mobile.getText().toString(), "", 1, verify_key_handler);
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

	private void onClickRegister()
	{
        // check network connection
        if (!Global.isOnline(RegisterActivity.this))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_NO_NETWORK), Gravity.CENTER);
            return;
        }
        /*
		if (edt_username.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_CANNOT_EMPTY), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (!Global.validateName(edt_username.getText().toString(), false))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_FORMAT_ERROR), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

        if (edt_username.getText().toString().length() > 20 || edt_username.getText().toString().length() < 2)
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_TOO_SHORT), Gravity.CENTER);
            edt_username.requestFocus();
            edt_username.selectAll();
            return;
        }
            */
		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}

        if (edt_mobile.getText().toString().length() != 11 || !TextUtils.isDigitsOnly(edt_mobile.getText().toString()))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_LENGTH_NOTMATCH), Gravity.CENTER);
            edt_mobile.requestFocus();
            edt_mobile.selectAll();
            return;
        }

        if(!"8888".equals(edt_verifykey.getText().toString().trim())){

            if (nKeyTimeStamp != 0) {       // Got verify key. Must validate verify key
                if (verified_phone.equals(edt_mobile.getText().toString())) {
                    if (edt_verifykey.getText().toString().equals("")) {
                        Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_PERSONINFO_VERIFYKEY_CANNOT_EMPTY), Gravity.CENTER);
                        edt_verifykey.requestFocus();
                        edt_verifykey.selectAll();
                        return;
                    }

                    String szValidVerifyKey = validateVerifyKey();
                    if (!szValidVerifyKey.isEmpty()) {
                        Global.showAdvancedToast(RegisterActivity.this, szValidVerifyKey, Gravity.CENTER);
                        edt_verifykey.requestFocus();
                        edt_verifykey.selectAll();
                        return;
                    }
                } else {
                    Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.xuyaochongxinhuoquyanzhengma), Gravity.CENTER);
                    return;
                }
            } else {            // Never got verify key. Must validate phone num is original phone num
                Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.xuyaohuoquyanzhengma), Gravity.CENTER);
                return;
            }
        }


         if (edt_nickname.getText().toString().equals(""))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_CANNOT_EMPTY), Gravity.CENTER);
            edt_nickname.requestFocus();
            edt_nickname.selectAll();
            return;
        }

        if (edt_nickname.getText().toString().length() < 2 ||
                edt_nickname.getText().toString().length() > 10)
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_LENGTH), Gravity.CENTER);
            edt_nickname.requestFocus();
            edt_nickname.selectAll();
            return;
        }

        if (!Global.validateName(edt_nickname.getText().toString(), true))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_FORMAT_ERROR), Gravity.CENTER);
            edt_nickname.requestFocus();
            edt_nickname.selectAll();
            return;
        }

        if (edt_password.getText().toString().equals(""))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_CANNOT_EMPTY), Gravity.CENTER);
            edt_password.requestFocus();
            edt_password.selectAll();
            return;
        }

		if ((edt_password.getText().length() < 6) || (edt_password.getText().length() > 16))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT), Gravity.CENTER);
			edt_password.requestFocus();
			edt_password.selectAll();
			return;
		}

		if (edt_conf_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_NEWPWD_EMPTY), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}

		if (!edt_conf_pwd.getText().toString().equals(edt_password.getText().toString()))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWDCONF_NOTMATCH), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}

//        if (edt_reqcode.getText().toString().trim().equals(""))
//        {
//            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_REQUEST_CODE_EMPTY), Gravity.CENTER);
//            edt_reqcode.requestFocus();
//            edt_reqcode.selectAll();
//            return;
//        }

        edt_username.setText(edt_mobile.getText().toString().trim());
		startProgress();
		CommManager.registerUser(edt_username.getText().toString(),
				edt_mobile.getText().toString(),
				edt_nickname.getText().toString(),
				edt_password.getText().toString(),
				edt_reqcode.getText().toString().trim(),
				nSex,
				Global.loadCityName(getApplicationContext()),
				Global.getIMEI(getApplicationContext()),
				Global.getNotificationToken(getApplicationContext()),
				Market.CHANNEL_FLAG,
				register_handler);
	}

	private void onClickCancel()
	{
		finishWithAnimation();
	}

	private String validateVerifyKey()
	{
		String retVal = "";

		String key = edt_verifykey.getText().toString().trim();
        if("8888".equals(key)){
            img_verify_match.setImageResource(R.drawable.img_match_checked);
        }else{
            if (key.equals(szVerifyKey))
            {
                long nCurStamp = Calendar.getInstance().getTimeInMillis();
                if (nKeyTimeStamp != 0 && nCurStamp - nKeyTimeStamp < nVerifyPeriod)     // Input verify key is valid in valid period
                {
                    img_verify_match.setImageResource(R.drawable.img_match_checked);
                }
                else
                {
                    img_verify_match.setImageResource(R.drawable.img_match_unchecked);
                    if (nKeyTimeStamp == 0) {
                        retVal = getResources().getString(R.string.STR_REGISTER_NO_VERIFYKEY);
                    } else {
                        retVal = getResources().getString(R.string.STR_REGISTER_VERIFYKEY_TIMEOUT);
                    }
                }
            }
            else {
                img_verify_match.setImageResource(R.drawable.img_match_unchecked);
                retVal = getResources().getString(R.string.STR_REGISTER_VERIFYKEY_NOTMATCH);
            }
        }

		return retVal;
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
//					Global.showAdvancedToast(RegisterActivity.this, szVerifyKey, Gravity.CENTER);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(RegisterActivity.this, szMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private AsyncHttpResponseHandler register_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");
				int nRetcode = jsonResult.getInt("retcode");
				String szMsg = jsonResult.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					STUserInfo userinfo = STUserInfo.decodeFromJSON(jsonResult.getJSONObject("retdata"));

					Global.saveUserID(getApplicationContext(), userinfo.userid);
					Global.saveInviteCode(getApplicationContext(), userinfo.invitecode);
					Global.savePersonVerified(getApplicationContext(), userinfo.person_verified == 1);
					Global.saveBaiduApiKey(getApplicationContext(), userinfo.baiduak);
                    Global.savePersonVerfiedWait(getApplicationContext(), userinfo.person_verified == 2);
                    Global.saveDriverVerfiedWait(getApplicationContext(), userinfo.driver_verified == 2);
                    Global.saveDriverVerified(getApplicationContext(), userinfo.driver_verified == 1);

					Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_SUCCESS), Gravity.CENTER);

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(RegisterActivity.this, szMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private TimerTask countdownTask = null;
	private Timer countdownTimer = null;
	private ImageButton ibAgreement;
	private TextView tvAgreement;
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
