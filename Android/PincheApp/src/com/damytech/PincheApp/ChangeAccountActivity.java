package com.damytech.PincheApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-9
 * Time: 上午1:23
 * To change this template use File | Settings | File Templates.
 */
public class ChangeAccountActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private TextView txt_account = null;
	private EditText edt_new_account = null;
	private TextView txt_bankname = null;
	private ImageButton btn_selbank = null;
	private EditText edt_subbranch = null;
	private Button btn_bind = null;
	private Button btn_release = null;

	private RelativeLayout selbank_layout = null;
	private ScrollView selbank_scrollview = null;
	private LinearLayout selbank_listlayout = null;

	private String[] arrBankNames = null;
	private String szOldAccount = "", szBank = "", szSubbranch = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_changeaccount);

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
				finishWithAnimation();
			}
		});

		txt_account = (TextView)findViewById(R.id.txt_old_account);
		edt_new_account = (EditText)findViewById(R.id.edt_account);
		txt_bankname = (TextView)findViewById(R.id.txt_bank);
		btn_selbank = (ImageButton)findViewById(R.id.btn_select_bank);
		btn_selbank.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectBank();
			}
		});
		edt_subbranch = (EditText)findViewById(R.id.edt_subbranch);

		btn_bind = (Button)findViewById(R.id.btn_bind);
		btn_bind.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBind();
			}
		});
		btn_release = (Button)findViewById(R.id.btn_release);
		btn_release.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickRelease();
			}
		});

		szOldAccount = getIntent().getStringExtra("account");
		szBank = getIntent().getStringExtra("bankname");
		szSubbranch = getIntent().getStringExtra("subbranch");

		if (szOldAccount != null)
			txt_account.setText(szOldAccount);
		else
			txt_account.setText("");

		if (szBank != null)
			txt_bankname.setText(szBank);
		if (szSubbranch != null)
			edt_subbranch.setText(szSubbranch);

		String szBanks = getResources().getString(R.string.STR_CHANGEACCOUNT_BANKNAMES);
		arrBankNames = szBanks.split(",");

		selbank_layout = (RelativeLayout)findViewById(R.id.select_banks_layout);
		selbank_layout.setVisibility(View.GONE);
		selbank_layout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				selbank_layout.setVisibility(View.GONE);
			}
		});

		selbank_scrollview = (ScrollView)findViewById(R.id.scrollView1);
		selbank_listlayout = (LinearLayout)findViewById(R.id.bank_item_layout);
		initializeBankItems();
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

	private void initializeBankItems()
	{
		int nItemHeight = 70;
		for (int i = 0; i < arrBankNames.length; i++)
		{
			String szBankname = arrBankNames[i];

			RelativeLayout itemLayout = new RelativeLayout(selbank_listlayout.getContext());
			RelativeLayout.LayoutParams item_layoutparams = new RelativeLayout.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, nItemHeight);
			itemLayout.setLayoutParams(item_layoutparams);
			itemLayout.setBackgroundColor(Color.WHITE);

			TextView txtView = new TextView(itemLayout.getContext());
			RelativeLayout.LayoutParams txt_layoutParams = new RelativeLayout.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
			txtView.setLayoutParams(txt_layoutParams);
			txtView.setTextColor(Color.rgb(0x60, 0x60, 0x60));
			txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 23);
			txtView.setGravity(Gravity.CENTER_VERTICAL);
			txtView.setPadding(20, 0, 0, 0);
			txtView.setText(szBankname);
			itemLayout.addView(txtView);


			if (i == 0)
			{
				ImageView top_sep_view = new ImageView(itemLayout.getContext());
				RelativeLayout.LayoutParams topsep_view_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
				topsep_view_params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				top_sep_view.setLayoutParams(topsep_view_params);
				top_sep_view.setBackgroundColor(Color.DKGRAY);
				itemLayout.addView(top_sep_view);
			}


			ImageView sep_view = new ImageView(itemLayout.getContext());
			RelativeLayout.LayoutParams sep_view_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
			sep_view_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			sep_view.setLayoutParams(sep_view_params);
			sep_view.setBackgroundColor(Color.DKGRAY);
			itemLayout.addView(sep_view);

			ImageButton btnItem = new ImageButton(itemLayout.getContext());
			btnItem.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			btnItem.setBackgroundResource(R.drawable.btn_empty);
			btnItem.setTag(szBankname);
			btnItem.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					String szBankname = (String)(v.getTag());
					onBankSelected(szBankname);
				}
			});
			itemLayout.addView(btnItem);

			ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

			selbank_listlayout.addView(itemLayout);
		}

		if (arrBankNames.length > 0)
			txt_bankname.setText(arrBankNames[0]);
	}

	private void onBankSelected(String szBankname)
	{
		txt_bankname.setText(szBankname);
		selbank_layout.setVisibility(View.GONE);
	}

	private void onClickBind()
	{
		if (edt_new_account.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_NEWACCOUNT_CANNOTEMPTY), Gravity.CENTER);
			edt_new_account.requestFocus();
			edt_new_account.selectAll();
			return;
		}

		if (edt_new_account.getText().toString().length() < 16 ||
			edt_new_account.getText().toString().length() > 19)
		{
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_ACCOUNTLEN), Gravity.CENTER);
			edt_new_account.requestFocus();
			edt_new_account.selectAll();
			return;
		}

		if (txt_bankname.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_BANK_CANNOTEMPTY), Gravity.CENTER);
			return;
		}

		if (edt_subbranch.getText().toString().equals(""))
		{
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_SUBBRANCH_CANNOTEMPTY), Gravity.CENTER);
			edt_subbranch.requestFocus();
			edt_subbranch.selectAll();
			return;
		}

		if (edt_subbranch.getText().toString().length() > 100)
		{
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_SUBBRANCH_TOOLONG), Gravity.CENTER);
			edt_subbranch.requestFocus();
			edt_subbranch.selectAll();
			return;
		}

		startProgress();
		CommManager.bindBankCard(Global.loadUserID(getApplicationContext()), edt_new_account.getText().toString(), txt_bankname.getText().toString(), edt_subbranch.getText().toString(), Global.getIMEI(getApplicationContext()), bind_handler);
	}

	private void onClickRelease()
	{
		CommonAlertDialog dialog = new CommonAlertDialog.Builder(ChangeAccountActivity.this)
				.message(getResources().getString(R.string.STR_CHANGEACCOUNT_CONFIRM_RELEASE))
				.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
				.positiveListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (szOldAccount == null || szOldAccount.equals(""))
						{
							Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_NOBINDACCOUNT), Gravity.CENTER);
							return;
						}

						startProgress();
						CommManager.releaseBankCard(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), release_handler);
					}
				})
				.build();
		dialog.show();
	}

	private void onSelectBank()
	{
		selbank_layout.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (selbank_layout.getVisibility() == View.VISIBLE)
			{
				selbank_layout.setVisibility(View.GONE);
			}
			else
			{
				finishWithAnimation();
			}

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}


	private AsyncHttpResponseHandler bind_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_BANKSUCCESS), Gravity.CENTER);

					Intent data = new Intent();
					data.putExtra("account", edt_new_account.getText().toString());
					data.putExtra("bankname", txt_bankname.getText().toString());
					data.putExtra("subbranch", edt_subbranch.getText().toString());
					setResult(RESULT_OK, data);
					finishWithAnimation();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(ChangeAccountActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler release_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CHANGEACCOUNT_RELEASESUCCESS), Gravity.CENTER);

					Intent data = new Intent();
					data.putExtra("account", "");
					data.putExtra("bankname", "");
					data.putExtra("subbranch", "");

					setResult(RESULT_OK, data);
					finishWithAnimation();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(ChangeAccountActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(ChangeAccountActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};
}

