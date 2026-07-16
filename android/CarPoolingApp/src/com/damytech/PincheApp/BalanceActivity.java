package com.damytech.PincheApp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STTsInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.DecimalDigitsInputFilter;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.damytech.BaiduQianbao.*;
import com.damytech.BaiduQianbao.codec.*;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-8
 * Time: 下午6:41
 * To change this template use File | Settings | File Templates.
 */
public class BalanceActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private final String szWapPayUrl = "https://www.baifubao.com/api/0/pay/0/wapdirect?";

	private final int REQCODE_CHANGE_ACCOUNT = 0;
	public static final int CHARGE_TAB = 1;
	public static final String CHARGE_TAB_NAME = "tab";

	// Chaxun tab layout controls
	private TextView txt_balance = null;
	private PullToRefreshListView balance_log_list = null;
	private BalanceAdapter balance_adapter = new BalanceAdapter();

	private ArrayList<STTsInfo> arrTsLogs = new ArrayList<STTsInfo>();
	private ArrayList<STTsInfo> arrTempLogs = new ArrayList<STTsInfo>();
	private int ts_pageno = 0;

	private double cur_balance = 0;

	// Chongzhi tab layout controls
	private TextView txt_balance2 = null;
	private EditText edt_charge_balance = null;
	private Button btn_charge = null;

	// Withdraw tab layout controls
	private TextView edt_realname = null;
	private TextView edt_accountname = null;
	private Button btn_change_account = null;
	private EditText edt_withdraw_balance = null;
	private EditText edt_login_pwd = null;
	private Button btn_forgetpwd = null;
	private Button btn_withdraw = null;
    private Button btnWithdrawLog = null;
	private String szRealname = "";
	private String szBankcard = "";
	private String szBankname = "";
	private String szSubbranch = "";

    private TextView lblRemain = null;

	// Tab controls
	private ImageButton btn_tab_chaxun = null, btn_tab_chongzhi = null, btn_tab_tixian = null;
	private RelativeLayout tab_chaxun_layout = null, tab_chongzhi_layout = null, tab_tixian_layout = null;
	private RelativeLayout bk_chaxun_layout = null, bk_chongzhi_layout = null, bk_tixian_layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_balance);

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

		txt_balance = (TextView)findViewById(R.id.txt_balance);

		balance_log_list = (PullToRefreshListView)findViewById(R.id.chaxun_result_listview);
		{
			balance_log_list.setMode(PullToRefreshBase.Mode.BOTH);
			balance_log_list.setOnRefreshListener(balance_log_list_refresh_listener);
			balance_log_list.setAdapter(balance_adapter);
		}

        startProgress();
        getOlderLogs();

		tab_chaxun_layout = (RelativeLayout)findViewById(R.id.tab_chaxun_layout);
		tab_chaxun_layout.setVisibility(View.VISIBLE);
		tab_chongzhi_layout = (RelativeLayout)findViewById(R.id.tab_chongzhi_layout);
		tab_chongzhi_layout.setVisibility(View.GONE);
		tab_tixian_layout = (RelativeLayout)findViewById(R.id.tab_tixian_layout);
		tab_tixian_layout.setVisibility(View.GONE);

		btn_tab_chaxun = (ImageButton)findViewById(R.id.btn_tab_chaxun);
		btn_tab_chaxun.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabChaxun();
			}
		});
		btn_tab_chongzhi = (ImageButton)findViewById(R.id.btn_tab_chongzhi);
		btn_tab_chongzhi.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabChongZhi();
			}
		});
		btn_tab_tixian = (ImageButton)findViewById(R.id.btn_tab_tixian);
		btn_tab_tixian.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabTixian();
			}
		});

		bk_chaxun_layout = (RelativeLayout)findViewById(R.id.bk_chaxun_layout);
		bk_chongzhi_layout = (RelativeLayout)findViewById(R.id.bk_chongzhi_layout);
		bk_tixian_layout = (RelativeLayout)findViewById(R.id.bk_tixian_layout);

		bk_chaxun_layout.setBackgroundResource(R.drawable.btn_chaxun_active);
		bk_chongzhi_layout.setBackgroundResource(R.drawable.btn_chongzhi_normal);
		bk_tixian_layout.setBackgroundResource(R.drawable.btn_tixian_normal);

		txt_balance2 = (TextView)findViewById(R.id.txt_balance2);

		edt_charge_balance = (EditText)findViewById(R.id.edt_balance);
		edt_charge_balance.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3, 2)});
        edt_charge_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                int fPrice = 1;
                try {
                    fPrice = Integer.parseInt(edt_charge_balance.getText().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fPrice = 1;
                    edt_charge_balance.setText("" + fPrice);
                }

                // check max/min price
                if (fPrice < 1)
                {
                    fPrice = 1;
                    edt_charge_balance.setText("" + fPrice);
                }
                else if (fPrice > Global.PRICE_MAX_LIMIT())
                {
                    fPrice = 9999;
                    edt_charge_balance.setText("" + fPrice);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

		btn_charge = (Button)findViewById(R.id.btn_charge);
		btn_charge.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickCharge();
			}
		});


		edt_realname = (TextView)findViewById(R.id.edt_realname);
		edt_accountname = (TextView)findViewById(R.id.edt_accountname);
		btn_change_account = (Button)findViewById(R.id.btn_change_accountname);
		edt_withdraw_balance = (EditText)findViewById(R.id.edt_withdraw_balance);
        edt_withdraw_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                int fPrice = 1;
                try {
                    fPrice = Integer.parseInt(edt_withdraw_balance.getText().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fPrice = 1;
                    edt_withdraw_balance.setText("" + fPrice);
                }

                // check max/min price
                if (fPrice < 1)
                {
                    fPrice = 1;
                    edt_withdraw_balance.setText("" + fPrice);
                }
                else if (fPrice > Global.PRICE_MAX_LIMIT())
                {
                    fPrice = 9999;
                    edt_withdraw_balance.setText("" + fPrice);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
		edt_login_pwd = (EditText)findViewById(R.id.edt_login_pwd);
		btn_forgetpwd = (Button)findViewById(R.id.btn_forget_pwd);
		btn_withdraw = (Button)findViewById(R.id.btn_withdraw);
        btnWithdrawLog = (Button) findViewById(R.id.btnWithdrawLog);
        btnWithdrawLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalanceActivity.this, WithdrawLogActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                BalanceActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

		btn_change_account.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onChangeAccount();
			}
		});
		btn_forgetpwd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onForgetPwd();
			}
		});
		btn_withdraw.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickWithDraw();
			}
		});

        lblRemain = (TextView) findViewById(R.id.lblRemain);

		if (getIntent().getIntExtra(CHARGE_TAB_NAME, 0) == CHARGE_TAB)
			onSelectTabChongZhi();
	}

    @Override
    public void onResume()
    {
        super.onResume();

        startProgress();
        CommManager.getMoney(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), moneyHandler);
    }

	private void onSelectTabChaxun()
	{
		tab_chaxun_layout.setVisibility(View.VISIBLE);
		tab_chongzhi_layout.setVisibility(View.GONE);
		tab_tixian_layout.setVisibility(View.GONE);

		bk_chaxun_layout.setBackgroundResource(R.drawable.btn_chaxun_active);
		bk_chongzhi_layout.setBackgroundResource(R.drawable.btn_chongzhi_normal);
		bk_tixian_layout.setBackgroundResource(R.drawable.btn_tixian_normal);
	}

	private void onSelectTabChongZhi()
	{
		tab_chaxun_layout.setVisibility(View.GONE);
		tab_chongzhi_layout.setVisibility(View.VISIBLE);
		tab_tixian_layout.setVisibility(View.GONE);

		bk_chaxun_layout.setBackgroundResource(R.drawable.btn_chaxun_normal);
		bk_chongzhi_layout.setBackgroundResource(R.drawable.btn_chongzhi_active);
		bk_tixian_layout.setBackgroundResource(R.drawable.btn_tixian_normal);
	}

	private void onSelectTabTixian()
	{
		if (Global.isPersonVerified(getApplicationContext()))
		{
            tab_chaxun_layout.setVisibility(View.GONE);
            tab_chongzhi_layout.setVisibility(View.GONE);
            tab_tixian_layout.setVisibility(View.VISIBLE);

            bk_chaxun_layout.setBackgroundResource(R.drawable.btn_chaxun_normal);
            bk_chongzhi_layout.setBackgroundResource(R.drawable.btn_chongzhi_normal);
            bk_tixian_layout.setBackgroundResource(R.drawable.btn_tixian_active);

            String strText = getString(R.string.STR_BALANCE_MYBALANCE) + "  " + Double.toString(cur_balance);
            SpannableString ss = new SpannableString(strText);
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.LIGHT_YELLOW_COLOR)), 6, 6 + Double.toString(cur_balance).length(), 0);
            lblRemain.setText(ss);

            startProgress();
            CommManager.getAccount(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), account_handler);
		}else if(Global.isPersonVerfiedWait(getApplicationContext())){
            Global.showAdvancedToast(BalanceActivity.this,getResources().getString(R.string.STR_VERIFYPERSON_APPLY_WAIT),Gravity.CENTER);
        }else
		{
            CommonAlertDialog dialog = new CommonAlertDialog.Builder(BalanceActivity.this)
                    .message(getResources().getString(R.string.STR_BALANCE_PERSON_NOTVERIFY))
                    .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
                    .positiveTitle(getResources().getString(R.string.STR_BALANCE_QURENZHENG))
                    .positiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BalanceActivity.this, VerifyPersonActivity.class);
                            intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                            BalanceActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                            startActivity(intent);
                        }
                    })
                    .build();
            dialog.show();
		}
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

	private PullToRefreshBase.OnRefreshListener balance_log_list_refresh_listener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestLogs();
			else
				getOlderLogs();
		}
	};

	private void getLatestLogs()
	{
		if (arrTsLogs == null || arrTsLogs.size() == 0)
		{
			ts_pageno = 0;
			CommManager.getTsLogsPage(Global.loadUserID(getApplicationContext()), ts_pageno, Global.getIMEI(getApplicationContext()), older_tslogs_handler);
		}
		else
			CommManager.getLatestTsLogs(Global.loadUserID(getApplicationContext()), arrTsLogs.get(0).uid, Global.getIMEI(getApplicationContext()), latest_tslogs_handler);
	}

	private void getOlderLogs()
	{
		CommManager.getTsLogsPage(Global.loadUserID(getApplicationContext()), ts_pageno, Global.getIMEI(getApplicationContext()), older_tslogs_handler);
	}

	private AsyncHttpResponseHandler older_tslogs_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

			stopProgress();
			balance_log_list.onRefreshComplete();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					cur_balance = retdata.getDouble("curbalance");

					txt_balance.setText("" + cur_balance);
					txt_balance2.setText("" + cur_balance);

					arrTempLogs.clear();
					JSONArray arrLogs = retdata.getJSONArray("logs");
					for (int i = 0; i < arrLogs.length(); i++)
					{
						JSONObject logitem = arrLogs.getJSONObject(i);

						STTsInfo info  =STTsInfo.decodeFromJSON(logitem);
						arrTempLogs.add(info);
					}

					addOldLogs();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(BalanceActivity.this, szRetmsg, Gravity.CENTER);
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
			balance_log_list.onRefreshComplete();
			stopProgress();
		}
	};


	private AsyncHttpResponseHandler latest_tslogs_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.
			balance_log_list.onRefreshComplete();
			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					cur_balance = retdata.getDouble("curbalance");

					txt_balance.setText("" + cur_balance);
					txt_balance2.setText("" + cur_balance);

					arrTempLogs.clear();
					JSONArray arrLogs = retdata.getJSONArray("logs");
					for (int i = 0; i < arrLogs.length(); i++)
					{
						JSONObject logitem = arrLogs.getJSONObject(i);

						STTsInfo info  =STTsInfo.decodeFromJSON(logitem);
						arrTempLogs.add(info);
					}

					insertLatestLogs();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(BalanceActivity.this, szRetmsg, Gravity.CENTER);
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
			balance_log_list.onRefreshComplete();
			stopProgress();
		}
	};


	private void addOldLogs()
	{
		boolean hasUpdate = false;

		for (int i = 0; i < arrTempLogs.size(); i++)
		{
			STTsInfo new_item = arrTempLogs.get(i);

			boolean isExist = false;
			for (int j = 0; j < arrTsLogs.size(); j++)
			{
				STTsInfo old_item = arrTsLogs.get(j);
				if (old_item.uid == new_item.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrTsLogs.add(new_item);

			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			ts_pageno = arrTsLogs.size() / Global.getPageItemCount();
		}

		balance_adapter.notifyDataSetChanged();
	}


	private void insertLatestLogs()
	{
		boolean hasUpdate = false;

		for (int i = 0; i < arrTempLogs.size(); i++)
		{
			STTsInfo new_item = arrTempLogs.get(i);

			boolean isExist = false;
			for (int j = 0; j < arrTsLogs.size(); j++)
			{
				STTsInfo old_item = arrTsLogs.get(j);
				if (old_item.uid == new_item.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrTsLogs.add(0, new_item);

			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			ts_pageno = arrTsLogs.size() / Global.getPageItemCount();
		}

		balance_adapter.notifyDataSetChanged();
	}


	private class BalanceAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			return arrTsLogs.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}


		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public Object getItem(int position) {
			return arrTsLogs.get(position);
		}

		@Override
		public boolean isEmpty() {
			if (arrTsLogs == null)
				return true;

			return arrTsLogs.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			STTsInfo tsinfo = arrTsLogs.get(position);

			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.balance_log_layout, null);

				STBalanceViewHolder holder = new STBalanceViewHolder();

				holder.txtTsID = (TextView)convertView.findViewById(R.id.txt_ts_id);
				holder.txtTsID.setText(tsinfo.tstime);

				holder.txtSum = (TextView)convertView.findViewById(R.id.txt_inout_balance);
                if (tsinfo.operbalance > 0)
                {
                    holder.txtSum.setText("+" + tsinfo.operbalance);
                }
                else
                {
                    holder.txtSum.setText("" + tsinfo.operbalance);
                }

				holder.txtSource = (TextView)convertView.findViewById(R.id.txt_source);
				holder.txtSource.setText(tsinfo.source);

				holder.txtBalance = (TextView)convertView.findViewById(R.id.txt_remain_balance);
				holder.txtBalance.setText("" + tsinfo.remainbalance);

				holder.uid = tsinfo.uid;

				convertView.setTag(holder);

				ResolutionSet.instance.iterateChild(convertView, mScrSize.x, mScrSize.y);
			}
			else
			{
				STBalanceViewHolder viewHolder = (STBalanceViewHolder)(convertView.getTag());

				viewHolder.txtTsID.setText(tsinfo.tstime);
                if (tsinfo.operbalance > 0)
                {
                    viewHolder.txtSum.setText("+" + tsinfo.operbalance);
                }
                else
                {
                    viewHolder.txtSum.setText("" + tsinfo.operbalance);
                }
				viewHolder.txtSource.setText(tsinfo.source);
				viewHolder.txtBalance.setText("" + tsinfo.remainbalance);
				viewHolder.uid = tsinfo.uid;
			}

			return convertView;
		}
	}

	private class STBalanceViewHolder
	{
		public long uid = 0;
		public TextView txtTsID = null;
		public TextView txtSum = null;
		public TextView txtSource = null;
		public TextView txtBalance = null;
	}


	private void onClickCharge()
	{
		if (edt_charge_balance.getText().toString().equals(""))
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_CANNOT_EMPTY), Gravity.CENTER);
			edt_charge_balance.requestFocus();
			edt_charge_balance.selectAll();
			return;
		}

		double value = 0;
		String szBalance = edt_charge_balance.getText().toString();
		try {
			value = Double.parseDouble(szBalance);
		} catch (Exception ex) {
			ex.printStackTrace();

			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_FORMAT_ERROR), Gravity.CENTER);
			edt_charge_balance.requestFocus();
			edt_charge_balance.selectAll();
			return;
		}

		if (value == 0)
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_BIGGERTHAN_0), Gravity.CENTER);
			edt_charge_balance.requestFocus();
			edt_charge_balance.selectAll();
			return;
		}


		if (value > 99999.99)
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_CHARGE_TOBIG), Gravity.CENTER);
			edt_charge_balance.requestFocus();
			edt_charge_balance.selectAll();
			return;
		}


        String orderInfo = createOrderInfo(getResources().getString(R.string.STR_ORDER_DESC), szBalance, "1");

		Intent intent = new Intent(BalanceActivity.this, WapPayActivity.class);
		intent.putExtra("url", szWapPayUrl + orderInfo);
		intent.putExtra("balance", Double.parseDouble(szBalance));
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

    /*************************** BaiduQianbao Relation **********************/
    /**
     * 组装订单信息
     *
     * @return
     */
    private String createOrderInfo(String name, String price, String num) {
        BigDecimal bigPrice = new BigDecimal(price); // 创建BigDecimal对象
        BigDecimal bigNum = new BigDecimal(num);
        bigPrice = bigPrice.multiply(new BigDecimal(100));      // 1yuan = 100
        BigDecimal bigInterest = bigPrice.multiply(bigNum);
        StringBuffer orderInfo = new StringBuffer("currency=1&extra=" + Global.loadUserID(getApplicationContext()));
        String orderNo = String.valueOf(System.currentTimeMillis());
        orderInfo
                .append("&goods_desc=")
                .append(new String(getUTF8toGBKString(name)))
                .append("&goods_name=")
                .append(new String(getUTF8toGBKString(name)))
                .append("&goods_url=http://item.jd.com/736610.html")
                .append("&input_charset=1&order_create_time=20130508131702&order_no=" + orderNo
                        + "&pay_type=2&return_url=" + Global.createChargeUrl()
                        )
                .append("&service_code=1&sign_method=1&sp_no=" + PartnerConfig.PARTNER_ID + "&total_amount="
                        + bigInterest + "&transport_amount=0&unit_amount=" + bigPrice.toString() + "&unit_count="
                        + bigNum.toString()+"&version=2");

        StringBuffer orderInfo1 = new StringBuffer("currency=1&extra=" + Global.loadUserID(getApplicationContext()));
        try {
            orderInfo1
                    .append("&goods_desc=")
                    .append(URLEncoder.encode(name, "GBK"))
                    .append("&goods_name=")
                    .append(URLEncoder.encode(name,"GBK"))
                    .append("&goods_url=http://item.jd.com/736610.html")
                    .append("&input_charset=1&order_create_time=20130508131702&order_no=" + orderNo
                            + "&pay_type=2&return_url=" + Global.createChargeUrl()
                            )
                    .append("&service_code=1&sign_method=1&sp_no=" + PartnerConfig.PARTNER_ID + "&total_amount="
                            + bigInterest + "&transport_amount=0&unit_amount=" + bigPrice.toString() + "&unit_count="
                            + bigNum.toString()+"&version=2");

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Utils.mLogError("订单信息："+orderInfo1.toString());
        Utils.mLogError("orderInfo订单信息："+orderInfo.toString());
        String signed = MD5.toMD5(orderInfo.toString() + "&key=" + PartnerConfig.MD5_PRIVATE);
        Utils.mLogError("签名："+signed);

        return orderInfo1.toString() + "&sign=" + signed;
    }

    /**
     * 商户的商品渠道，用来统计不同渠道的交易状况
     *
     * @return
     */
    public String getChannel() {
        ApplicationInfo appInfo;
        String msg;
        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("BaiduMobAd_CHANNEL");
        } catch (Exception e) {
            msg = "";
        }
        return msg;
    }

    /**
     * 字符转换从UTF-8到GBK
     *
     * @param gbkStr
     * @return
     */
    public static byte[] getUTF8toGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }


//    private void testPay(String name, String price, String num) throws Exception{
//        /**
//         * 2、获取 web.xml内的常量值
//         * StringBuffer orderInfo1 = new StringBuffer("currency=1&extra=" + Global.loadUserID(getApplicationContext()));
//         try {
//         orderInfo1
//         .append("&goods_desc=")
//         .append(URLEncoder.encode(name, "GBK"))
//         .append("&goods_name=")
//         .append(URLEncoder.encode(name,"GBK"))
//         .append("&goods_url=http://item.jd.com/736610.html")
//         .append("&input_charset=1&order_create_time=20130508131702&order_no=" + orderNo
//         + "&pay_type=2&return_url=" + Global.successInChargeUrl()
//         )
//         .append("&service_code=1&sign_method=1&sp_no=" + PartnerConfig.PARTNER_ID + "&total_amount="
//         + bigInterest + "&transport_amount=0&unit_amount=" + bigPrice.toString() + "&unit_count="
//         + bigNum.toString()+"&version=2");
//         */
//        //商品分类号
//        String  service_code=
//                "service_code=1";
//        //商户号
//        String sp_no="sp_no=" + PartnerConfig.MD5_PRIVATE;
//        //交易的超时时间,当前时间加2天
//        Calendar c   =   Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH, 2);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String mDateTime=formatter.format(c.getTime());
//        String strExpire=mDateTime.substring(0, 14);//
//        String expire_time=
//                "expire_time=" +strExpire;
//        //订单创建时间
//        String order_create_time1=formatter.format(System.currentTimeMillis());
//        //订单号
//        String order_no="order_no=" +String.valueOf(System.currentTimeMillis());
//        String order_create_time="order_create_time=" +order_create_time1;
//        //币种
//        String currency="currency=1" ;
//        //编码
//        String input_charset="input_charset=1";
//        //版本
//        String version="version=2" ;
//        //加密方式md5或者hash
//        String sign_method="sign_method=1";
//        //秘钥
//        String SP_KEY=PartnerConfig.MD5_PRIVATE;
//        //提交地址
//        String BFB_PAY_WAP_DIRECT_URL=szWapPayUrl;
//
//        /**
//         *3、获取pay_unlogin.html页面post提交的变量值
//         */
//        //商品名称
//        String tempgoods_name=name;
//        String goods_name ="goods_name="+tempgoods_name;
//        String goods_name1="goods_name="+ URLEncoder.encode("http://item.jd.com/736610.html","gbk");
//        //String goods_ame1 ="goods_name="+tempgoods_name;
//        //商品描述
//        String tempgoods_desc=name;
//        String goods_desc ="goods_desc="+tempgoods_desc;
//        String goods_desc1= "goods_desc="+URLEncoder.encode("http://item.jd.com/736610.html","gbk");
//        //String goods_desc1 ="goods_desc="+tempgoods_desc;
//        //商品在商户网站上的URL
//        String goods_url ="goods_url="+"http://item.jd.com/736610.html";
//        String goods_url1="goods_url="+URLEncoder.encode("http://item.jd.com/736610.html","gbk");
//
//        //单价
//        String unit_amount ="1";
//        //数量
//        String unit_count ="1";
//        //运费
//        String transport_amount ="0";
//        //总金额
//        String total_amount =price;
//        //买家在商户网站的用户名
//        String tempSPUserName=request.getParameter("buyer_sp_username");
//        String buyer_sp_username ="buyer_sp_username="+tempSPUserName;
//        String buyer_sp_username1 ="buyer_sp_username="+URLEncoder.encode(tempSPUserName,"gbk");
//        //后台通知地址
//        String return_url ="return_url="+request.getParameter("return_url");
//        String return_url1="return_url="+URLEncoder.encode(request.getParameter("return_url"),"gbk");
//        //前台通知地址
//        String page_url ="page_url="+request.getParameter("page_url");
//        String page_url1="page_url="+URLEncoder.encode(request.getParameter("page_url"),"gbk");
//        //支付方式
//        String pay_type ="pay_type="+request.getParameter("pay_type");
//        //默认银行的编码
//        String bank_no ="bank_no="+request.getParameter("bank_no");
//        //用户在商户端的用户ID
//        String sp_uno ="sp_uno="+request.getParameter("sp_uno");
//        //商户自定义数据
//        String tempextra=request.getParameter("extra");
//        String extra ="extra="+tempextra;
//        String extra1="extra="+URLEncoder.encode(tempextra,"gbk");
//
//        //签名串拼接数组
//        String[]array={
//                service_code,
//                sp_no,
//                order_create_time,
//                order_no,
//                goods_name,
//                goods_desc,
//                goods_url,
//                unit_amount,
//                unit_count,
//                transport_amount,
//                total_amount,
//                currency,
//                buyer_sp_username ,
//                return_url,
//                page_url,
//                pay_type,
//                bank_no,
//                expire_time,
//                input_charset,
//                version,
//                sign_method
//                ,extra
//        };
//        //浏览器参数拼接数组
//        String[]array1={
//                service_code,
//                sp_no,
//                order_create_time,
//                order_no,
//                goods_name1,
//                goods_desc1,
//                goods_url1,
//                unit_amount,
//                unit_count,
//                transport_amount,
//                total_amount,
//                currency,
//                buyer_sp_username1,
//                return_url1,
//                page_url1,
//                pay_type,
//                bank_no,
//                expire_time,
//                input_charset,
//                version,
//                sign_method
//                ,extra1
//        };
//        /**
//         * 4、调用bfb_sdk_comm里create_baifubao_pay_order_url方法生成百付宝即时到账支付接口URL(不需要登录)
//         *   array是待签名串
//         *   array1地址栏拼接串
//         */
//        String getURL=new BfbSdkComm().create_baifubao_pay_order_url(array,array1,BFB_PAY_WAP_DIRECT_URL);
//    }


    /**
     * 支付结果处理

     * @param stateCode
     * @param payDesc
     */
    private void handlepayResult(int stateCode, String payDesc) {
        String tradeStatus = "statecode={";
        try {
            int imemoStart = payDesc.indexOf("statecode=");
            imemoStart += tradeStatus.length();
            int imemoEnd = payDesc.indexOf("};order_no=");
            tradeStatus = payDesc.substring(imemoStart, imemoEnd);
            if ("0".equals(tradeStatus)) {
                if (ResultChecker.checkSign(payDesc) == 2) {
                    Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_PAY_SUCCESS), Gravity.CENTER);

                    double value = 0;
                    String szBalance = edt_charge_balance.getText().toString();
                    try {
                        value = Double.parseDouble(szBalance);
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_FORMAT_ERROR), Gravity.CENTER);
                        edt_charge_balance.requestFocus();
                        edt_charge_balance.selectAll();
                        return;
                    }
                    // call charge service
                    startProgress();
                    CommManager.charge(Global.loadUserID(getApplicationContext()),
                            value,
                            Global.getIMEI(getApplicationContext()),
                            1,                  // Charge source is ZhiFuBao
                            charge_handler);
                }
            }
        } catch (Exception e) {
            Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_PAY_FAIL), Gravity.CENTER);
        }
    }

	private AsyncHttpResponseHandler charge_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_CHARGE_SUCCESS), Gravity.CENTER);

					JSONObject retdata = result.getJSONObject("retdata");

					double last_balance = retdata.getDouble("curbalance");
					txt_balance.setText("" + last_balance);
					txt_balance2.setText("" + last_balance);

					JSONObject newLog = retdata.getJSONObject("newlog");
					STTsInfo tsinfo = STTsInfo.decodeFromJSON(newLog);

					edt_charge_balance.setText("");

					arrTsLogs.add(0, tsinfo);
					balance_adapter.notifyDataSetChanged();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(BalanceActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void onChangeAccount()
	{
		Intent intent = new Intent(BalanceActivity.this, ChangeAccountActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		if (!szBankcard.equals(""))
			intent.putExtra("account", szBankcard);
		if (!szBankname.equals(""))
			intent.putExtra("bankname", szBankname);
		if (!szSubbranch.equals(""))
			intent.putExtra("subbranch", szSubbranch);

		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_CHANGE_ACCOUNT);
	}


	private void onForgetPwd()
	{
		Intent intent  = new Intent(BalanceActivity.this, ForgetPwdActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);

	}


	private void onClickWithDraw()
	{
		if (edt_realname.getText().toString().equals(""))
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_PERSON_NOTVERIFY), Gravity.CENTER);
			return;
		}

		if (edt_accountname.getText().toString().equals(""))
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_NOTBIND), Gravity.CENTER);
			return;
		}

		if (edt_withdraw_balance.getText().toString().equals(""))
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_INPUTBALANCE), Gravity.CENTER);
			edt_withdraw_balance.requestFocus();
			edt_withdraw_balance.selectAll();
			Global.showKeyboardFromText(edt_withdraw_balance, BalanceActivity.this);
			return;
		}

        int bal = Integer.parseInt(edt_withdraw_balance.getText().toString());
        if (bal <= 0)
        {
            Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_INPUTBALANCE), Gravity.CENTER);
            edt_withdraw_balance.requestFocus();
            edt_withdraw_balance.selectAll();
            Global.showKeyboardFromText(edt_withdraw_balance, BalanceActivity.this);
            return;
        }

		if (edt_login_pwd.getText().toString().equals(""))
		{
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_BALANCE_INPUTLOGINPWD), Gravity.CENTER);
			edt_login_pwd.requestFocus();
			edt_login_pwd.selectAll();
			Global.showKeyboardFromText(edt_login_pwd, BalanceActivity.this);
			return;
		}

		double balance = 0;
		try {
			balance = Double.parseDouble(edt_withdraw_balance.getText().toString());

			startProgress();
			CommManager.withdraw(Global.loadUserID(getApplicationContext()),
					szRealname,
					szBankcard,
					balance,
					edt_login_pwd.getText().toString(),
					Global.getIMEI(getApplicationContext()),
					withdraw_handler);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private AsyncHttpResponseHandler account_handler = new AsyncHttpResponseHandler()
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

					szRealname = retdata.getString("realname");
					szBankcard = retdata.getString("bankcard");
					szBankname = retdata.getString("bankname");
					szSubbranch = retdata.getString("subbranch");

					edt_realname.setText(szRealname);
					edt_accountname.setText(szBankcard);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					szRealname = "";
					szBankcard = "";
					szBankname = "";
					szSubbranch = "";

					edt_realname.setText("");
					edt_accountname.setText("");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (resultCode == RESULT_OK && requestCode == REQCODE_CHANGE_ACCOUNT)
		{
			String account = data.getStringExtra("account");
			String bankname = data.getStringExtra("bankname");
			String subbranch = data.getStringExtra("subbranch");

			edt_accountname.setText(account);

			szBankcard = account;
            szBankname = bankname;
			szSubbranch = subbranch;
		}
	}

	private AsyncHttpResponseHandler withdraw_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_WITHDRAW_SUCCESS), Gravity.CENTER);

					JSONObject retdata = result.getJSONObject("retdata");

					double last_balance = retdata.getDouble("curbalance");
					txt_balance.setText("" + last_balance);
					txt_balance2.setText("" + last_balance);
					edt_login_pwd.setText("");

					JSONObject newLog = retdata.getJSONObject("newlog");
					STTsInfo tsinfo = STTsInfo.decodeFromJSON(newLog);

					edt_withdraw_balance.setText("");
                    String strText = getString(R.string.STR_BALANCE_MYBALANCE) + "  " + Double.toString((int)(last_balance * 10) / 10.0);
                    SpannableString ss = new SpannableString(strText);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.LIGHT_YELLOW_COLOR)), 6, 6 + Double.toString(cur_balance).length(), 0);
                    lblRemain.setText(ss);

					arrTsLogs.add(0, tsinfo);
					balance_adapter.notifyDataSetChanged();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(BalanceActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

    private AsyncHttpResponseHandler moneyHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();

            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONObject retdata = jsonResult.getJSONObject("retdata");
                    double money = retdata.getDouble("money");
                    String strText = getString(R.string.STR_BALANCE_MYBALANCE) + "  " + Double.toString((int)(money * 10) / 10.0);
                    SpannableString ss = new SpannableString(strText);
                    ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.LIGHT_YELLOW_COLOR)), 6, 6 + Double.toString(money).length(), 0);
                    lblRemain.setText(ss);
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(BalanceActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
	        Global.showAdvancedToast(BalanceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };
}
