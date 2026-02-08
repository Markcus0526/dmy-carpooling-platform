package com.damytech.MainApp;

import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Date;

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
    private TextView txtSeconds = null;
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


	private int nSex = 0;           // Default is Male.

	private long nVerifyPeriod = 30 * 60 * 1000;            // Verify key is valid for 30 Minutes
	private long nKeyTimeStamp = 0;
	private String szVerifyKey = "";

    private CountDownTimer mCodeTimer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_register);

		initControls();
		initResolution();
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

		edt_username = (EditText)findViewById(R.id.edt_username);
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
        edt_mobile = (EditText)findViewById(R.id.edt_mobile);
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

		edt_password = (EditText)findViewById(R.id.edt_password);
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
		edt_conf_pwd = (EditText)findViewById(R.id.edt_confirm_pwd);
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
		edt_reqcode = (EditText)findViewById(R.id.edt_request_code);

		btn_register = (Button)findViewById(R.id.btn_register);
		btn_register.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickRegister();
			}
		});

		btn_cancel = (Button)findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new View.OnClickListener()
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
        String szPhone = edt_mobile.getText().toString();

        if (szPhone.equals(""))
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
            edt_mobile.requestFocus();
            edt_mobile.selectAll();
            return;
        }

        if (szPhone.length() != Global.PHONE_LENGTH())
        {
            Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_MSG_PHONENUM_LEN), Gravity.CENTER);
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


	private void onClickMale()
	{
		nSex = 0;

		img_male.setBackgroundResource(R.drawable.radiobox_sel);
		img_female.setBackgroundResource(R.drawable.radiobox_normal);
	}

	private void onClickFemale()
	{
		nSex = 1;

		img_male.setBackgroundResource(R.drawable.radiobox_normal);
		img_female.setBackgroundResource(R.drawable.radiobox_sel);
	}

	private void onClickRegister()
	{
		if (edt_username.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_CANNOT_EMPTY), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}

		if (edt_nickname.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_CANNOT_EMPTY), Gravity.CENTER);
			edt_nickname.requestFocus();
			edt_nickname.selectAll();
			return;
		}


		if (edt_password.getText().length() < 6)
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT), Gravity.CENTER);
			edt_password.requestFocus();
			edt_password.selectAll();
			return;
		}


		if (edt_password.getText().toString().equals(""))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWD_CANNOT_EMPTY), Gravity.CENTER);
			edt_password.requestFocus();
			edt_password.selectAll();
			return;
		}

		if (!edt_conf_pwd.getText().toString().equals(edt_password.getText().toString()))
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_PWDCONF_NOTMATCH), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}

		if (!validateVerifyKey())
		{
			Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_VERIFYKEY_NOTMATCH), Gravity.CENTER);
			edt_verifykey.requestFocus();
			edt_verifykey.selectAll();
			return;
		}

		startProgress();
		CommManager.registerUser(edt_username.getText().toString(),
				edt_mobile.getText().toString(),
				edt_nickname.getText().toString(),
				edt_password.getText().toString(),
				edt_reqcode.getText().toString(),
				nSex,
				"",
				Global.getIMEI(getApplicationContext()),
				register_handler);
	}

	private void onClickCancel()
	{
		finishWithAnimation();
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
					Global.showAdvancedToast(RegisterActivity.this, szVerifyKey, Gravity.CENTER);
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

				if (nRetcode == 0)
				{
					STUserInfo userinfo = STUserInfo.decodeFromJSON(jsonResult.getJSONObject("retdata"));

					Global.saveUserID(getApplicationContext(), userinfo.userid);
					Global.saveInviteCode(getApplicationContext(), userinfo.invitecode);
					Global.savePersonVerified(getApplicationContext(), userinfo.person_verified == 1);

					Global.showAdvancedToast(RegisterActivity.this, getResources().getString(R.string.STR_REGISTER_SUCCESS), Gravity.CENTER);

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetcode == Global.AUTO_LOGOUT_CODE())
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
}
