package com.damytech.PincheApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.*;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

import java.util.ArrayList;

public class DrvLongOrderExeActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
	Button btnReturn = null;
	ImageButton btnBack = null;
	Button btnDaoDa = null, btnFaChe = null, btnJieShu = null;
	ImageView imgRefresh = null;

	TextView lblTime = null;
	ImageView imgState = null;

	ScrollView scrollView = null;
	LinearLayout passListLayout = null;

	ConfirmPasswordDialog dlgPwd = null;

	private long mOrderId;

	private STDetDriverOrderInfo mDetDriverInfo = new STDetDriverOrderInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_exec_longorder);

		initControls();
		initResolution();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// get detailed order info
		callGetDetDriverInfo(true);
	}

	private void initControls()
	{
		mOrderId = getIntent().getLongExtra("orderid", 0);

		lblTime = (TextView) findViewById(R.id.lblHint);
		imgState = (ImageView) findViewById(R.id.imgState);


		scrollView = (ScrollView)findViewById(R.id.scrollView);
		passListLayout = (LinearLayout)findViewById(R.id.passlist_layout);

		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClickListener);
		btnReturn = (Button) findViewById(R.id.btnReturn);
		btnReturn.setOnClickListener(onClickListener);
		btnDaoDa = (Button) findViewById(R.id.btnDaoDa);
		btnDaoDa.setOnClickListener(onClickListener);
		btnFaChe = (Button) findViewById(R.id.btnFaChe);
		btnFaChe.setOnClickListener(onClickListener);
		btnJieShu = (Button) findViewById(R.id.btnJieshu);
		btnJieShu.setOnClickListener(onClickListener);

		imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
		imgRefresh.setOnClickListener(onClickListener);
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



	/******************************************* Update UI *********************************************/
	private void UpdateUI()
	{
		// set ui control info
		lblTime.setText(mDetDriverInfo.remark);

		// check order state
		switch (mDetDriverInfo.state)
		{
			case ConstData.ORDER_STATE_DRV_ACCEPTED:
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
			{
				// change button state
				btnDaoDa.setEnabled(false);
				btnFaChe.setEnabled(false);
				btnJieShu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnFaChe.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnJieShu.setBackgroundResource(R.drawable.roundgreengray_frame);
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
				btnFaChe.setEnabled(false);
				btnJieShu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundgreenwhite_frame);
				btnFaChe.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnJieShu.setBackgroundResource(R.drawable.roundgreengray_frame);

				stopProgress();

				break;
			}
			case ConstData.ORDER_STATE_DRV_ARRIVED:
			{
				// set progress bar value
				imgState.setImageResource(R.drawable.bk_runstate1);
				// change button state
				btnDaoDa.setEnabled(false);
				btnFaChe.setEnabled(true);
				btnJieShu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnFaChe.setBackgroundResource(R.drawable.roundgreenwhite_frame);
				btnJieShu.setBackgroundResource(R.drawable.roundgreengray_frame);

				stopProgress();

				break;
			}
			case ConstData.ORDER_STATE_PASS_GETON:
			{
				// set progress bar value
				imgState.setImageResource(R.drawable.bk_runstate2);
				// change button state
				btnDaoDa.setEnabled(false);
				btnFaChe.setEnabled(false);
				btnJieShu.setEnabled(true);
				btnDaoDa.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnFaChe.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnJieShu.setBackgroundResource(R.drawable.roundgreenwhite_frame);

				stopProgress();

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
				btnFaChe.setEnabled(false);
				btnJieShu.setEnabled(false);
				btnDaoDa.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnFaChe.setBackgroundResource(R.drawable.roundgreengray_frame);
				btnJieShu.setBackgroundResource(R.drawable.roundgreengray_frame);

				stopProgress();

				break;
			}
		}

		// check alive passenger
		if (isAliveLeftPass() == false)
		{
			// show msg ( no alive passenger )
			String szRetMsg = getString(R.string.STR_NO_ALIVE_PASS);
			Global.showAdvancedToast(DrvLongOrderExeActivity.this, szRetMsg, Gravity.CENTER);
			onClickBack();
			return;
		}

		updatePassList();
	}


	private boolean isAliveLeftPass()
	{
		boolean retVal = false;
		ArrayList<STPassengerInfo> passlist = mDetDriverInfo.pass_list;

		for (STPassengerInfo info : passlist)
		{
			if (info.state != ConstData.PASS_STATE_GIVEUP)
			{
				retVal = true;
				break;
			}
		}

		return retVal;
	}

	/********************************************* Passenger List Adapter **************************************/
	public View getView(int position) {
		STPassengerInfo passInfo = mDetDriverInfo.pass_list.get(position);

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_driver_longdistancerun1item, null);

		Point point = Global.getScreenSize(DrvLongOrderExeActivity.this);
		ResolutionSet.instance.iterateChild(view, point.x, point.y);

		// get passenger control
		SmartImageView psgPhoto = (SmartImageView)view.findViewById(R.id.viewPhoto);
		ImageView imgGender = (ImageView)view.findViewById(R.id.imgSex);
		TextView lblAge = (TextView)view.findViewById(R.id.lblAge);
		TextView lblName = (TextView)view.findViewById(R.id.lblName);
		TextView lblSeatCnt = (TextView)view.findViewById(R.id.lblSeatCount);
        ImageButton btnPhoto = (ImageButton) view.findViewById(R.id.btnPhoto);
		psgPhoto.isCircular = true;
		psgPhoto.setImageUrl(passInfo.image, R.drawable.default_user_img);

		imgGender.setImageResource(passInfo.sex == 0 ? R.drawable.bk_manmark : R.drawable.bk_womanmark);
		lblAge.setText("" + passInfo.age);
		lblName.setText(passInfo.name);
		lblSeatCnt.setText(passInfo.seat_count_desc);

        final String phone = passInfo.phone;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(phone);
            }
        });

		////////////////////////////////// set event
		Button btnUpload = (Button) view.findViewById(R.id.btnYanPiao);
		btnUpload.setTag(passInfo);
		btnUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				STPassengerInfo info = (STPassengerInfo) v.getTag();
				// show password view to check passenger
				dlgPwd = new ConfirmPasswordDialog(DrvLongOrderExeActivity.this);
				dlgPwd.setPassId(info.uid);
				dlgPwd.setOnDismissListener(DrvLongOrderExeActivity.this);
				dlgPwd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dlgPwd.show();
                dlgPwd.setCanceledOnTouchOutside(true);
				dlgPwd.setCancelable(true);
			}
		});

		Button btnGiveup = (Button) view.findViewById(R.id.btnQiPiao);
		btnGiveup.setTag(passInfo);
		btnGiveup.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final STPassengerInfo info = (STPassengerInfo) v.getTag();

				// show confirm dialog
				CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvLongOrderExeActivity.this)
						.message(getResources().getString(R.string.STR_GIVEUP_CONFIRM))
						.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
						.positiveListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								callPassengerGievUp(info.uid);
							}
						})
						.build();
				dialog.show();
			}
		});

		if (mDetDriverInfo.state != ConstData.ORDER_STATE_DRV_ARRIVED)
		{
			btnUpload.setEnabled(false);
			btnGiveup.setEnabled(false);
			btnUpload.setBackgroundResource(R.drawable.roundlightgray);
			btnGiveup.setBackgroundResource(R.drawable.roundlightgray);
		}
		else
		{
			btnUpload.setEnabled(true);
			btnGiveup.setEnabled(true);
			btnUpload.setBackgroundResource(R.drawable.roundgraywhite_frame);
			btnGiveup.setBackgroundResource(R.drawable.roundgraywhite_frame);
		}

		TextView lblPassState = (TextView) view.findViewById(R.id.lblResult);
		lblPassState.setText(passInfo.state_desc);

		// according to state show/hide control
		if (passInfo.state == ConstData.PASS_STATE_WAIT)
		{
			btnUpload.setVisibility(View.VISIBLE);
			btnGiveup.setVisibility(View.VISIBLE);
			lblPassState.setVisibility(View.GONE);
		}
		else
		{
			btnUpload.setVisibility(View.GONE);
			btnGiveup.setVisibility(View.GONE);
			lblPassState.setVisibility(View.VISIBLE);
		}

		return view;
	}

    private void callPhone(String phone){
        Global.callPhone(phone,this);
    }
	/******************************************* UI Control Event ******************************************/

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId())
			{
				case R.id.btn_back:
				case R.id.btnReturn:
					onClickBack();
					break;
				case R.id.btnDaoDa:
					// call driver arrived service
					callSignOrderDrvArrival();
					break;
				case R.id.btnFaChe:
					// call start driving service
					callStartLongOrderDriving();
					break;
				case R.id.btnJieshu:
					// call end order service
					callEndOrder();
					break;
				case R.id.imgRefresh:
					callGetDetDriverInfo(true);
					break;
				case R.id.btnCall:
					Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "1234567890"));
					startActivity(intentCall);
					break;
			}
		}
	};

	private void onClickBack()
	{
		// go to back to main
		Intent data = new Intent();
		data.putExtra("orderid", mOrderId);
		setResult(RESULT_OK, data);

		finishWithAnimation();
	}

	/****************************************** Service Relation ********************************************/
	/**
	 * Call getDetailedDriverInfo service
	 */
	private void callGetDetDriverInfo(boolean startProc)
	{
		if (startProc)
			startProgress();

		CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
				mOrderId,
				ConstData.ORDER_TYPE_LONG,
				Global.getIMEI(getApplicationContext()),
				detDriverOrderInfoHandler);
	}

	private AsyncHttpResponseHandler detDriverOrderInfoHandler = new AsyncHttpResponseHandler()
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
					JSONObject retdata = result.getJSONObject("retdata");
					// parse result
					mDetDriverInfo = STDetDriverOrderInfo.decodeFromJSON(retdata);

					// udpate ui using detail order information
					UpdateUI();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					stopProgress();
					logout(szRetMsg);
				}
				else
				{
					stopProgress();
					Global.showAdvancedToast(DrvLongOrderExeActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(DrvLongOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/**
	 * Call execute order service
	 */
	private void callExecuteOrder()
	{
		CommManager.executeLongOrder(Global.loadUserID(getApplicationContext()),
				mOrderId,
				Global.getIMEI(getApplicationContext()),
				signOrderHandler);
	}

	/**
	 * Call execute order service
	 */
	private void callSignOrderDrvArrival()
	{
		startProgress();
		CommManager.signLongOrderDriverArrival(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), signOrderHandler);
	}

	/**
	 * Call check customer service
	 */
	private void callSignOrderPassUpload(long passId, String password)
	{
		startProgress();
		CommManager.signLongOrderPassengerUpload(Global.loadUserID(getApplicationContext()), mOrderId, passId, password,
				Global.getIMEI(getApplicationContext()), signOrderHandler);
	}

	/**
	 * Call check customer service
	 */
	private void callStartLongOrderDriving()
	{
		startProgress();
		CommManager.startLongOrderDriving(Global.loadUserID(getApplicationContext()), mOrderId,
				Global.getIMEI(getApplicationContext()), signOrderHandler);
	}

	/**
	 * Call passenger upload service
	 */
	private void callPassengerUpload(long passId, String password)
	{
		startProgress();

		CommManager.signLongOrderPassengerUpload(Global.loadUserID(getApplicationContext()), mOrderId, passId, password,
				Global.getIMEI(getApplicationContext()), signOrderHandler);
	}

	/**
	 * Call passenger give up service
	 */
	private void callPassengerGievUp(long passId)
	{
		startProgress();

		CommManager.signLongOrderPassengerGiveup(Global.loadUserID(getApplicationContext()), mOrderId, passId,
				Global.getIMEI(getApplicationContext()), signOrderHandler);
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
					callGetDetDriverInfo(false);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					stopProgress();
					logout(szRetMsg);
				}
				else
				{
					stopProgress();
					Global.showAdvancedToast(DrvLongOrderExeActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				stopProgress();
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvLongOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/**
	 * Call end order service
	 */
	private void callEndOrder()
	{
        CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvLongOrderExeActivity.this)
                .message(getResources().getString(R.string.STR_END_SERVICE_CONFIRM))
                .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startProgress();
                        CommManager.endLongOrder(Global.loadUserID(getApplicationContext()), mOrderId, Global.getIMEI(getApplicationContext()), endOrderHandler);
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
					Global.showAdvancedToast(DrvLongOrderExeActivity.this, szRetMsg, Gravity.CENTER);
					onClickBack();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvLongOrderExeActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvLongOrderExeActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};



	/************************************ Password Check Dialog Result **************************************/
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (dialog.getClass() == ConfirmPasswordDialog.class)
		{
			// get input password
			ConfirmPasswordDialog dlgPwd = (ConfirmPasswordDialog) dialog;
			String strPass = dlgPwd.getPassword();
			long passId = dlgPwd.getPassId();
			// call check customer service ( with password )
			callSignOrderPassUpload(passId, strPass);
		}
	}


	private void updatePassList()
	{
		if (mDetDriverInfo == null || mDetDriverInfo.pass_list == null)
			return;

		passListLayout.removeAllViews();
		for (int i = 0; i < mDetDriverInfo.pass_list.size(); i++)
		{
			View v = getView(i);
			passListLayout.addView(v);
		}
	}
}
