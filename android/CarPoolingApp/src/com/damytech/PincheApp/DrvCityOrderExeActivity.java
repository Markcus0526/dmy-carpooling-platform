package com.damytech.PincheApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STDetDriverOrderInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DrvCityOrderExeActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
    private static final String TAG = "erik_debug";
    private final int GPS_STATE_NA = -1;
	private final int GPS_STATE_OFF = 0;
	private final int GPS_STATE_ON = 1;

	private int gps_state = GPS_STATE_NA;

	Button btnDaoDa = null;
	Button btnYanPiao = null;
	Button btnJieshu = null;
	Button btnCall = null;
	Button btnService = null;

	TextView lblPath = null;
	TextView lblPrecTime = null;
	TextView lblTotalTime = null;
	TextView lblDistance = null;

	ImageView imgState = null;

	Timer checkOrderTimer = null;
	private int timer_delay = 1000 * 10;

	private long mOrderId;
	private int mOrderType;
    private String mPassPhone;

	private double mOldLat = 0;
	private double mOldLng = 0;
	private double mCurRunDistance = 0;
    private double distance = 0;
	private long mTotalTime = 0;

	private ConfirmPasswordDialog dlgPwd = null;

	private STDetDriverOrderInfo mDetDriverInfo = new STDetDriverOrderInfo();

	// order cancelled state
	private final int ORDER_NOT_CANCELLED = 0;
	private final int ORDER_CANCELLED = 1;

	private final double DIST_LIMIT_MAX = 0.3;		  // 1h / run 120km
	private final double DIST_LIMIT_MIN = 0.01;		 // 1s / run 1m


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_exec_cityorder);

		initControls();
		initResolution();

		// get detailed order info
		callGetDetDriverInfo();
	}

	private void initControls()
	{
		try { mOrderId = getIntent().getLongExtra("orderid", 0); } catch (Exception ex) { mOrderId = 0; }
		try { mOrderType = getIntent().getIntExtra("ordertype", 0); } catch (Exception ex) { mOrderType = 0; }
		try { mCurRunDistance = getIntent().getDoubleExtra("runDist", 0); } catch (Exception ex) { mCurRunDistance = 0; }
		try { distance = getIntent().getDoubleExtra("distance", 0); } catch (Exception ex) { mCurRunDistance = 0; }
		try { mTotalTime = getIntent().getIntExtra("runTime", 0); } catch (Exception ex) { mTotalTime = 0; }
        mPassPhone = getIntent().getStringExtra("pass_phone");
//        Log.d(TAG, "mPassPhone:"+mPassPhone);

		imgState = (ImageView) findViewById(R.id.imgState);

		btnDaoDa = (Button) findViewById(R.id.btnDaoDa);
		btnDaoDa.setOnClickListener(onClickListener);
		btnYanPiao = (Button) findViewById(R.id.btnYanPiao);
		btnYanPiao.setOnClickListener(onClickListener);
		btnJieshu = (Button) findViewById(R.id.btnJieshu);
		btnJieshu.setOnClickListener(onClickListener);
		btnCall = (Button) findViewById(R.id.btnCall);
		btnCall.setOnClickListener(onClickListener);
		btnService = (Button)findViewById(R.id.btnService);
		btnService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickService();
			}
		});

		lblPath = (TextView) findViewById(R.id.lblPath);
		lblPrecTime = (TextView) findViewById(R.id.lblPrecTime);
		lblTotalTime = (TextView) findViewById(R.id.lblTotalTime);
		lblDistance = (TextView) findViewById(R.id.lblDistance);

        lblDistance.setText(String.format("%s%.02f%s", getString(R.string.STR_PINCHELICHENG), distance,
                getString(R.string.STR_GONGLI)));

		// initialize member variable
		mOldLat = Global.loadLatitude(DrvCityOrderExeActivity.this);
		mOldLng = Global.loadLongitude(DrvCityOrderExeActivity.this);
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

		// init check order time & start
		if (checkOrderTimer != null)		checkOrderTimer.cancel();

		checkOrderTimer = new Timer();
		checkOrderTimer.schedule(checkTimerTask, 1000, timer_delay);
	}

	// Return distance between two coordinates in KM
	public static double calcDist(double lat1, double lon1, double lat2, double lon2)
	{
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;

		return dist;
	}

	// Function to convert from degrees to radian
	private static double deg2rad(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	// Function to convert from radian to degrees
	private static double rad2deg(double rad)
	{
		return (rad / Math.PI * 180.0);
	}

	private void calcRunningDistance()
	{
		double curLat = Global.loadLatitude(DrvCityOrderExeActivity.this);
		double curLng = Global.loadLongitude(DrvCityOrderExeActivity.this);
		double offsetDist = calcDist(mOldLat, mOldLng, curLat, curLng);

		// check offset distance with limit value
		if ((offsetDist > DIST_LIMIT_MIN) && (offsetDist < DIST_LIMIT_MAX))
		{
			mOldLat = curLat;
			mOldLng = curLng;
//			mCurRunDistance += offsetDist;
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				lblDistance.setText(String.format("%s%.02f%s", getString(R.string.STR_PINCHELICHENG), mCurRunDistance, getString(R.string.STR_GONGLI)));
				// calc time
				mTotalTime += timer_delay;	  // add spend time
				long time = mTotalTime / 1000 / 60;
				lblTotalTime.setText(getString(R.string.STR_ZONGYONGSHI) + time + getString(R.string.STR_FENZHONG));
			}
		});

		// call check order state service
		callCheckCancelledState(mCurRunDistance);
	}

	/******************************************* Update UI *********************************************/
	private void UpdateUI()
	{
		// set ui control info
		lblPath.setText(mDetDriverInfo.start_addr + getResources().getString(R.string.addr_separator) + mDetDriverInfo.end_addr);
		lblPrecTime.setText(getString(R.string.STR_YUYUECHUFASHIJIAN) + mDetDriverInfo.start_time);

		// check order state
		switch (mDetDriverInfo.state)
		{
			case ConstData.ORDER_STATE_DRV_ACCEPTED:
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
			{
				// change button state
				btnDaoDa.setEnabled(false);
				btnYanPiao.setEnabled(false);
				btnJieshu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundlightgray);
				btnYanPiao.setBackgroundResource(R.drawable.roundlightgray);
				btnJieshu.setBackgroundResource(R.drawable.roundlightgray);
				// call execute order service
				callExecuteOrder();

				break;
			}
			case ConstData.ORDER_STATE_STARTED:
			{
				// set progress bar value
				//imgState.setImageResource(R.drawable.bk_runstate1);
				// change button state
				btnDaoDa.setEnabled(true);
				btnYanPiao.setEnabled(false);
				btnJieshu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundgreenwhite_frame);
				btnYanPiao.setBackgroundResource(R.drawable.roundlightgray);
				btnJieshu.setBackgroundResource(R.drawable.roundlightgray);

				break;
			}
			case ConstData.ORDER_STATE_DRV_ARRIVED:
			{
				// set progress bar value
				imgState.setImageResource(R.drawable.bk_runstate1);
				// change button state
				btnDaoDa.setEnabled(false);
				btnYanPiao.setEnabled(true);
				btnJieshu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundlightgray);
				btnYanPiao.setBackgroundResource(R.drawable.roundgreenwhite_frame);
				btnJieshu.setBackgroundResource(R.drawable.roundlightgray);

				break;
			}
			case ConstData.ORDER_STATE_PASS_GETON:
			{
				// set progress bar value
				imgState.setImageResource(R.drawable.bk_runstate2);
				// change button state
				btnDaoDa.setEnabled(false);
				btnYanPiao.setEnabled(false);
				btnJieshu.setEnabled(true);
				btnDaoDa.setBackgroundResource(R.drawable.roundlightgray);
				btnYanPiao.setBackgroundResource(R.drawable.roundlightgray);
				btnJieshu.setBackgroundResource(R.drawable.roundgreenwhite_frame);

				break;
			}
			case ConstData.ORDER_STATE_FINISHED:
			case ConstData.ORDER_STATE_PAYED:
			case ConstData.ORDER_STATE_EVALUATED:
			case ConstData.ORDER_STATE_CLOSED:
			{
				// set progress bar value
				imgState.setImageResource(R.drawable.bk_runstate3);
				// change button state
				btnDaoDa.setEnabled(false);
				btnYanPiao.setEnabled(false);
				btnJieshu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundlightgray);
				btnYanPiao.setBackgroundResource(R.drawable.roundlightgray);
				btnJieshu.setBackgroundResource(R.drawable.roundlightgray);

				break;
			}
		}
	}

	/******************************************* UI Control Event ******************************************/

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId())
			{
				case R.id.btnDaoDa:
					// call driver arrived service
					callSignOrderDrvArrival();
					break;
				case R.id.btnYanPiao:
					// show password dialog & check passenger
					dlgPwd = new ConfirmPasswordDialog(DrvCityOrderExeActivity.this);
					dlgPwd.setOnDismissListener(DrvCityOrderExeActivity.this);
					dlgPwd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dlgPwd.setCancelable(true);
					dlgPwd.setCanceledOnTouchOutside(true);
					dlgPwd.show();
					break;
				case R.id.btnJieshu:
					// call end order service
					callEndOrder();
					break;
				case R.id.btnCall:
                    Global.callPhone(mPassPhone, DrvCityOrderExeActivity.this);
					break;
			}
		}
	};

	private void onClickBack()
	{
		finishWithAnimation();
	}

	/****************************************** Service Relation ********************************************/
	/**
	 * Call getDetailedDriverInfo service
	 */
	private void callGetDetDriverInfo()
	{
		CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
				mOrderId,
				mOrderType,
				Global.getIMEI(getApplicationContext()),
				detDriverOrderInfoHandler);
	}

	private AsyncHttpResponseHandler detDriverOrderInfoHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					// parse result
					mDetDriverInfo = STDetDriverOrderInfo.decodeFromJSON(retdata);

					// udpate ui using detail order information
					UpdateUI();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvCityOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/**
	 * Call execute order service
	 */
	private void callExecuteOrder()
	{
		if (mOrderType == ConstData.ORDER_TYPE_ONCE)
		{
			startProgress();
			CommManager.executeOnceOrder(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), signOrderHandler);
		}
		else if (mOrderType == ConstData.ORDER_TYPE_ONOFF)
		{
			startProgress();
			CommManager.executeOnOffOrder(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), signOrderHandler);
		}
	}

	/**
	 * Call execute order service
	 */
	private void callSignOrderDrvArrival()
	{
		if (mOrderType == ConstData.ORDER_TYPE_ONCE)
		{
			startProgress();
			CommManager.signOnceOrderDriverArrival(Global.loadUserID(getApplicationContext()), mOrderId,Global.getIMEI(getApplicationContext()), signOrderHandler);
		}
		else if (mOrderType == ConstData.ORDER_TYPE_ONOFF)
		{
			startProgress();
			CommManager.signOnOffOrderDriverArrival(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), signOrderHandler);
		}
	}

	/**
	 * Call check customer service
	 */
	private void callSignOrderPassUpload(String password)
	{
		long passId = mDetDriverInfo.pass_list.get(0).uid;
		if (mOrderType == ConstData.ORDER_TYPE_ONCE)
		{
			startProgress();
			CommManager.signOnceOrderPassengerUpload(Global.loadUserID(getApplicationContext()), mOrderId, passId, password,
					Global.getIMEI(getApplicationContext()), signOrderHandler);
		}
		else if (mOrderType == ConstData.ORDER_TYPE_ONOFF)
		{
			startProgress();
			CommManager.signOnOffOrderPassengerUpload(Global.loadUserID(getApplicationContext()), mOrderId, passId, password,
					Global.getIMEI(getApplicationContext()), signOrderHandler);
		}
	}


	private AsyncHttpResponseHandler signOrderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					// get again detailed order info & refresh
					callGetDetDriverInfo();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					stopProgress();
					logout(szRetMsg);
				}
				else
				{
					stopProgress();
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				stopProgress();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvCityOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/**
	 * Call end order service
	 */
	private void callEndOrder()
	{
        CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvCityOrderExeActivity.this)
                .message(getResources().getString(R.string.STR_END_SERVICE_CONFIRM))
                .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOrderType == ConstData.ORDER_TYPE_ONCE)
                        {
                            startProgress();
                            CommManager.endOnceOrder(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), endOrderHandler);
                        }
                        else if (mOrderType == ConstData.ORDER_TYPE_ONOFF)
                        {
                            startProgress();
                            CommManager.endOnOffOrder(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), endOrderHandler);
                        }
                    }
                })
                .build();
        dialog.show();
	}

	private AsyncHttpResponseHandler endOrderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					// show success message
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szRetMsg, Gravity.CENTER);
					// go to back to main
					setResult(RESULT_OK);
					onClickBack();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvCityOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/**
	 * Call end order service
	 */
	private String szGpsMsg = "";
	private void callCheckCancelledState(double distance)
	{
		if (!Global.isOpenGPS(getApplicationContext()) && gps_state != GPS_STATE_OFF)
		{
			gps_state = GPS_STATE_OFF;
			szGpsMsg = "GPS信号丢失，无法记录执行轨迹，您的合法权益可能无法保障";
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szGpsMsg, Gravity.CENTER);
				}
			});
		}
		else if (Global.isOpenGPS(getApplicationContext()) && gps_state == GPS_STATE_OFF)
		{
			gps_state = GPS_STATE_ON;
			szGpsMsg = "GPS信号恢复";
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szGpsMsg, Gravity.CENTER);
				}
			});
		}

		Log.d("exec_activity", "order id : " + mOrderId);
		Log.d("exec_activity", "order type : " + mOrderType);
		CommManager.orderCancelled(Global.loadUserID(getApplicationContext()), mOrderId, mOrderType, distance, Global.getIMEI(getApplicationContext()), checkOrderHandler);
	}

	private AsyncHttpResponseHandler checkOrderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					// get order status
					JSONObject retdata = result.getJSONObject("retdata");
					int status = retdata.getInt("status");
					// check status
					if (ORDER_CANCELLED == status)
					{
						Global.showAdvancedToast(DrvCityOrderExeActivity.this, szRetMsg, Gravity.CENTER);
						if (checkOrderTimer != null)
						{
							checkOrderTimer.cancel();
							checkOrderTimer = null;
						}
						setResult(RESULT_OK);
						onClickBack();
					}
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					if (checkOrderTimer != null)
					{
						checkOrderTimer.cancel();
						checkOrderTimer = null;
					}
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvCityOrderExeActivity.this, szRetMsg, Gravity.CENTER);
					if (checkOrderTimer != null)
					{
						checkOrderTimer.cancel();
						checkOrderTimer = null;
					}
					setResult(RESULT_OK);
					onClickBack();
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvCityOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/**************************************** Check Order Timer Task *****************************/
	TimerTask checkTimerTask = new TimerTask()
	{
		@Override
		public void run() {
			// calc running distance
			calcRunningDistance();
		}
	};

	/**************************************** Password Check Dialog Dismiss *****************************/
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (dialog.getClass() == ConfirmPasswordDialog.class)
		{
			// get input password
			dlgPwd = (ConfirmPasswordDialog) dialog;
			String strPass = dlgPwd.getPassword();
			// call check customer service ( with password )
			if (strPass.length() == 4)
				callSignOrderPassUpload(strPass);
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
			return true;

		return super.onKeyDown(keyCode, event);
	}


	private void onClickService()
	{
		CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvCityOrderExeActivity.this)
				.message(getResources().getString(R.string.STR_ABOUT_CONFIRM_CALL))
				.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
				.positiveListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Global.callPhone(Global.getServiceCall(), DrvCityOrderExeActivity.this);
					}
				})
				.build();
		dialog.show();
	}

}
