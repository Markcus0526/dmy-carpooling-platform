package com.damytech.PincheApp;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-21
 * Time: 下午9:47
 * To change this template use File | Settings | File Templates.
 */
public class PassCancelLongOrderActivity extends SuperActivity
{
	private long orderID = 0;

	private TextView txtAddr = null;
	private TextView txtOrderNum = null;
	private TextView txtBalance = null;
	private TextView txtCreateTime = null;
	private TextView txtStartTime = null;
	private TextView txtInfo1 = null, txtInfo2 = null, txtInfo3 = null, txtInfo4 = null;
	private TextView txtDaysDist = null;
	private TextView txtCancelBal = null;
	private Button btnCancel = null, btnOK = null;
	private ImageButton btnBack = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_pass_cancellongorder);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		orderID = getIntent().getLongExtra("orderid", 0);

		txtAddr = (TextView)findViewById(R.id.txt_addr);
		txtOrderNum = (TextView)findViewById(R.id.txt_order_num);
		txtBalance = (TextView)findViewById(R.id.txt_balance);
		txtCreateTime = (TextView)findViewById(R.id.txt_create_time);
		txtStartTime = (TextView)findViewById(R.id.txt_start_time);
		txtInfo1 = (TextView)findViewById(R.id.txt_info1);
		txtInfo2 = (TextView)findViewById(R.id.txt_info2);
		txtInfo3 = (TextView)findViewById(R.id.txt_info3);
		txtInfo4 = (TextView)findViewById(R.id.txt_info4);
		txtDaysDist = (TextView)findViewById(R.id.txt_days_dist);
		txtCancelBal = (TextView)findViewById(R.id.txt_cancelled_bal);

		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		btnOK = (Button)findViewById(R.id.btn_ok);
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickCancelOrder();
			}
		});

		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		startProgress();
		CommManager.getLongOrderCancelInfo(Global.loadUserID(getApplicationContext()), orderID, Global.getIMEI(getApplicationContext()), longOrderCancelInfo_handler);
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
		finish();
	}


	private void onClickCancelOrder()
	{
		startProgress();
		CommManager.signLongOrderPassengerGiveup(0, orderID, Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), cancel_order_handler);
	}

	private AsyncHttpResponseHandler longOrderCancelInfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					updateUI(retdata);
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassCancelLongOrderActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassCancelLongOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler cancel_order_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassCancelLongOrderActivity.this, getResources().getString(R.string.STR_SUCCESS), Gravity.CENTER);
                    onClickBack();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassCancelLongOrderActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassCancelLongOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void updateUI(JSONObject retdata)
	{
		try {
			txtAddr.setText(retdata.getString("start_addr") + getResources().getString(R.string.addr_separator) + retdata.getString("end_addr"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			txtOrderNum.setText(getResources().getString(R.string.STR_LONGDISTANCEDETAIL_DINGDANBIANHAO) + retdata.getString("order_num"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			txtBalance.setText("" + retdata.getDouble("price") + getResources().getString(R.string.STR_BALANCE_DIAN));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			txtCreateTime.setText(getResources().getString(R.string.STR_CHUDANSHIJIAN) + retdata.getString("create_time"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			txtStartTime.setText(getResources().getString(R.string.STR_YUYUESHIJIAN) + retdata.getString("start_time"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			JSONArray arrRules = retdata.getJSONArray("balance_rule");

			JSONObject info1 = arrRules.getJSONObject(0);
			JSONObject info2 = arrRules.getJSONObject(1);
			JSONObject info3 = arrRules.getJSONObject(2);
			JSONObject info4 = arrRules.getJSONObject(3);

			txtInfo1.setText(info1.getString("rule"));
			txtInfo2.setText(info2.getString("rule"));
			txtInfo3.setText(info3.getString("rule"));
			txtInfo4.setText(info4.getString("rule"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			txtDaysDist.setText(retdata.getString("time_interval_desc"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			txtCancelBal.setText(retdata.getString("balance_desc"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}


}

