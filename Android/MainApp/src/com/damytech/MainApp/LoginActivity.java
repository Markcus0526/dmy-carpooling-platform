package com.damytech.MainApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-6
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends SuperActivity
{
	private final int REQCODE_REGISTER = 1;
	private final int REQCODE_FORGETPWD = 2;

	private EditText edt_username = null, edt_password = null;
/*
	private ImageButton btn_autologin = null, btn_remempwd = null;
	private ImageView img_autologin = null, img_remempwd = null;
*/
	private Button btn_login = null, btn_register = null, btn_forget_pwd = null;
	private ImageButton btn_back = null;

	private boolean auto_login = true;
	private boolean remem_pwd = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_login);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		edt_username = (EditText)findViewById(R.id.edt_username);
		edt_password = (EditText)findViewById(R.id.edt_password);
/*
		btn_autologin = (ImageButton)findViewById(R.id.btn_autologin);
		btn_remempwd = (ImageButton)findViewById(R.id.btn_remem_pwd);
		img_autologin = (ImageView)findViewById(R.id.img_auto_login);
		img_remempwd = (ImageView)findViewById(R.id.img_remem_pwd);
*/
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_register = (Button)findViewById(R.id.btn_register);
		btn_forget_pwd = (Button)findViewById(R.id.btn_forget_pwd);
		btn_back = (ImageButton)findViewById(R.id.btn_back);

/*
		int nAutoLogin = Global.loadAutoLoginFlag(getApplicationContext());
		int nRememPwd = Global.loadRememberFlag(getApplicationContext());

		if (nAutoLogin < 0 || nRememPwd < 0)
		{
			auto_login = true;
			remem_pwd = true;

			img_autologin.setBackgroundResource(R.drawable.checkbox_selected);
			img_remempwd.setBackgroundResource(R.drawable.checkbox_selected);
		}
		else if (nAutoLogin >= 0)
		{
			auto_login = nAutoLogin == 1;
			if (auto_login)
				remem_pwd = true;
			else
				remem_pwd = nRememPwd == 1;

			if (auto_login)
				img_autologin.setBackgroundResource(R.drawable.checkbox_selected);
			else
				img_autologin.setBackgroundResource(R.drawable.checkbox_normal);

			if (remem_pwd)
				img_remempwd.setBackgroundResource(R.drawable.checkbox_selected);
			else
				img_remempwd.setBackgroundResource(R.drawable.checkbox_normal);

			edt_username.setText(Global.loadUserName(getApplicationContext()));
			if (remem_pwd)
				edt_password.setText(Global.loadUserPwd(getApplicationContext()));
		}
*/

		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});
/*
		btn_autologin.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickAutoLogin();
			}
		});

		btn_remempwd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickRememPwd();
			}
		});
*/
		btn_login.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickLogin();
			}
		});

		btn_register.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickRegister();
			}
		});

		btn_forget_pwd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickForgetPwd();
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
/*
	private void onClickAutoLogin()
	{
		auto_login = !auto_login;

		if (auto_login)
		{
			img_autologin.setBackgroundResource(R.drawable.checkbox_selected);
			btn_remempwd.setEnabled(false);
		}
		else
		{
			img_autologin.setBackgroundResource(R.drawable.checkbox_normal);
			btn_remempwd.setEnabled(true);
		}

		img_remempwd.setBackgroundResource(R.drawable.checkbox_selected);
	}

	private void onClickRememPwd()
	{
		remem_pwd = !remem_pwd;

		if (remem_pwd)
			img_remempwd.setBackgroundResource(R.drawable.checkbox_selected);
		else
			img_remempwd.setBackgroundResource(R.drawable.checkbox_normal);
	}
*/
	private void onClickLogin()
	{
		if (edt_username.getText().toString().equals(""))
		{
			Global.showAdvancedToast(LoginActivity.this, getResources().getString(R.string.STR_LOGIN_USERNAME_CANNOT_EMPTY), Gravity.CENTER);
			edt_username.requestFocus();
			edt_username.selectAll();
			return;
		}

		if (edt_password.getText().toString().equals(""))
		{
			Global.showAdvancedToast(LoginActivity.this, getResources().getString(R.string.STR_LOGIN_PASSWORD_CANNOT_EMPTY), Gravity.CENTER);
			edt_password.requestFocus();
			edt_password.selectAll();
			return;
		}

		startProgress();
		CommManager.loginUser(edt_username.getText().toString(), edt_password.getText().toString(), "", Global.getIMEI(getApplicationContext()), login_handler);
	}

	private void onClickRegister()
	{
		Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_REGISTER);
	}

	private void onClickForgetPwd()
	{
		Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_FORGETPWD);
	}

	private AsyncHttpResponseHandler login_handler = new AsyncHttpResponseHandler()
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
				String retmsg = result.getString("retmsg");
				if (nRetcode == 0)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					STUserInfo userinfo = STUserInfo.decodeFromJSON(retdata);
					Global.saveUserID(getApplicationContext(), userinfo.userid);
					Global.saveInviteCode(getApplicationContext(), userinfo.invitecode);
					Global.savePersonVerified(getApplicationContext(), userinfo.person_verified == 1);
					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else
				{
					Global.showAdvancedToast(LoginActivity.this, retmsg, Gravity.CENTER);
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_REGISTER)
		{
			setResult(RESULT_OK);
			finishWithAnimation();
		}
		else if (requestCode == REQCODE_FORGETPWD)
		{
//			setResult(RESULT_OK);
//			finishWithAnimation();
		}
	}
}
