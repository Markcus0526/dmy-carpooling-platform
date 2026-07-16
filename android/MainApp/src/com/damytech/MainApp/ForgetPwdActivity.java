package com.damytech.MainApp;

import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
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
    private TextView txtSeconds = null;
	private EditText edt_verifykey = null;
	private ImageView img_verify_match = null;
	private EditText edt_password = null;
	private EditText edt_conf_pwd = null;
	private Button btn_register = null;
	private Button btn_cancel = null;

	private long nVerifyPeriod = 30 * 60 * 1000;            // Verify key is valid for 30 Minutes
	private long nKeyTimeStamp = 0;
	private String szVerifyKey = "";

    private CountDownTimer mCodeTimer = null;

//    bug 289
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mCodeTimer != null){
//            mCodeTimer.onFinish();
//            mCodeTimer.cancel();
//        }
//    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_forgetpwd);

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
        String szPhone = edt_mobile.getText().toString();

        if (szPhone.equals(""))
        {
            Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_MOBILE_CANNOT_EMPTY), Gravity.CENTER);
            edt_mobile.requestFocus();
            edt_mobile.selectAll();
            return;
        }

        if (szPhone.length() != Global.PHONE_LENGTH())
        {
            Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_MSG_PHONENUM_LEN), Gravity.CENTER);
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
					Global.showAdvancedToast(ForgetPwdActivity.this, szVerifyKey, Gravity.CENTER);
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
		if (edt_username.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_USERNAME_CANNOTEMPTY), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (edt_password.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_NEWPWD_CANNOTEMPTY), Gravity.CENTER);
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


		if (!validateVerifyKey())
		{
			Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_REGISTER_VERIFYKEY_NOTMATCH), Gravity.CENTER);
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

				if (nRetcode == 0)
				{
					Global.showAdvancedToast(ForgetPwdActivity.this, getResources().getString(R.string.STR_FORGETPWD_SUCCESS), Gravity.CENTER);
					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else
				{
					Global.showAdvancedToast(ForgetPwdActivity.this, szRetMsg, Gravity.CENTER);
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

}
