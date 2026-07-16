package com.damytech.PincheApp;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-7
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
public class ForgetPwdActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private EditText edt_username = null;
	private EditText edt_mobile = null;
	private Button btn_verifykey = null;
	private EditText edt_verifykey = null;
	private ImageView img_verify_match = null;
	private EditText edt_password = null;
	private EditText edt_conf_pwd = null;
	private Button btn_register = null;
	private Button btn_cancel = null;

	private long nVerifyPeriod = 5 * 60 * 1000;            // Verify key is valid for 1 Minutes
	private long nKeyTimeStamp = 0;

	private String szVerifyKey = "";
	private String szPhoneNum = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_forgetpwd);

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

//    bug 289
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(countdownTimer != null && countdownTask != null){
//            btn_verifykey.setEnabled(true);
//            btn_verifykey.setBackgroundResource(R.drawable.btn_green);
//            btn_verifykey.setText(getResources().getString(R.string.STR_PERSONINFO_GETVERIFKEY));
//
//            countdownTimer.cancel();
//            countdownTask.cancel();
//
//            countdownTimer = null;
//            countdownTask = null;
//        }
//    }


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

		img_verify_match = (ImageView)findViewById(R.id.img_verifkey_match);

		edt_password = (EditText)findViewById(R.id.edt_password);
		edt_conf_pwd = (EditText)findViewById(R.id.edt_confirm_pwd);

		btn_register = (Button)findViewById(R.id.btn_register);
		btn_register.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickConfirm();
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
        /*
		if (edt_username.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_USERNAME_CANNOTEMPTY), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (edt_username.getText().toString().length() < 2 ||
				edt_username.getText().toString().length() > 20)
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_LENGTH), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (!Global.validateName(edt_username.getText().toString(), false))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_NICKNAME_FORMAT_ERROR), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}
        */
		if (edt_mobile.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}

		if (edt_mobile.getText().toString().length() != 11 || !TextUtils.isDigitsOnly(edt_mobile.getText().toString()))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_LENGTH_NOTMATCH), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}
        edt_username.setText(edt_mobile.getText().toString().trim());
		startProgress();
		CommManager.getVerifKey(edt_mobile.getText().toString(), edt_username.getText().toString(), 2, verify_key_handler);
	}


    private String validateVerifyKey()
    {
        String retVal = "";

        String key = edt_verifykey.getText().toString();
        if (key.equals(szVerifyKey)) {
	        long nCurStamp = (new Date()).getTime();
	        if (nKeyTimeStamp != 0 &&
			    nCurStamp - nKeyTimeStamp < nVerifyPeriod &&
			    szPhoneNum.equals(edt_mobile.getText().toString()))     // Input verify key is valid in valid period
	        {
		        img_verify_match.setImageResource(R.drawable.img_match_checked);
	        } else if (nKeyTimeStamp == 0) {
		        retVal = getResources().getString(R.string.STR_REGISTER_NO_VERIFYKEY);
	        } else if (nCurStamp - nKeyTimeStamp >= nVerifyPeriod) {
		        retVal = getResources().getString(R.string.STR_REGISTER_VERIFYKEY_TIMEOUT);
	        } else if (szPhoneNum.equals(edt_mobile.getText().toString())) {
		        retVal = getResources().getString(R.string.STR_REGISTER_REVERIFY);
	        } else {
                img_verify_match.setImageResource(R.drawable.img_match_unchecked);
                retVal = getResources().getString(R.string.STR_REGISTER_VERIFYKEY_TIMEOUT);
            }
        }
        else {
            img_verify_match.setImageResource(R.drawable.img_match_unchecked);
            retVal = getResources().getString(R.string.STR_REGISTER_VERIFYKEY_NOTMATCH);
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
					szPhoneNum = edt_mobile.getText().toString();

					Global.saveVerifyKeyTime(getApplicationContext(), Calendar.getInstance().getTimeInMillis());
					startCountDownTimer();

					// Test code.
//					Global.showAdvancedToast(ForgetPwdActivity.this, szVerifyKey, Gravity.CENTER);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(ForgetPwdActivity.this, szMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void onClickCancel()
	{
		finishWithAnimation();
	}


	private void onClickConfirm()
	{
        /*
		if (edt_username.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_USERNAME_CANNOTEMPTY), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (edt_username.getText().toString().length() < 2 ||
			edt_username.getText().toString().length() > 20)
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_USERNAME_TOO_SHORT), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (!Global.validateName(edt_username.getText().toString(), false))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_FORMAT_ERROR), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}
        */
		if (edt_password.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this,
					getResources().getString(R.string.STR_FORGETPWD_NEWPWD_CANNOTEMPTY),
					Gravity.CENTER);
			edt_password.requestFocus();
			edt_password.selectAll();
			return;
		}

		if ((edt_password.getText().length() < 6) || (edt_password.getText().length() > 16))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this,
					getResources().getString(R.string.STR_REGISTER_PWD_TOO_SHORT),
					Gravity.CENTER);
			edt_password.requestFocus();
			edt_password.selectAll();
			return;
		}

		if (!edt_conf_pwd.getText().toString().equals(edt_password.getText().toString()))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_PWD_NOTMATCH), Gravity.CENTER);
			edt_conf_pwd.requestFocus();
			edt_conf_pwd.selectAll();
			return;
		}

		if (edt_verifykey.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_VERIFKEY_CANNOTEMPTY), Gravity.CENTER);
			edt_verifykey.requestFocus();
			edt_verifykey.selectAll();
			return;
		}


		if (edt_mobile.getText().toString().length() != 11 || !TextUtils.isDigitsOnly(edt_mobile.getText().toString()))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_LENGTH_NOTMATCH), Gravity.CENTER);
			edt_mobile.requestFocus();
			edt_mobile.selectAll();
			return;
		}


		String validKey = validateVerifyKey();
        if (!validKey.isEmpty())
        {
            Global.showAdvancedToast(ForgetPwdActivity.this, validKey, Gravity.CENTER);
            edt_verifykey.requestFocus();
            edt_verifykey.selectAll();
            return;
        }

		startProgress();
		CommManager.forgetPassword(edt_username.getText().toString(), edt_mobile.getText().toString(), edt_password.getText().toString(), Global.getIMEI(getApplicationContext()), forgetpwd_handler);
	}

	private AsyncHttpResponseHandler forgetpwd_handler = new AsyncHttpResponseHandler()
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
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_SUCCESS), Gravity.CENTER);
					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(ForgetPwdActivity.this, szRetMsg, Gravity.CENTER);
                    nKeyTimeStamp = 0;
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
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


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
