package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.DBDao;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.Utils;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * 拼友主界面
 */
public class PassPubCityOrderActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
	/*
	 *static references part
	 */
	//log tag
	private static final String TAG = "erik_debug";
	private final int ADD_MILLISECONDS = 25 * 60 * 1000;
	//应用状态码
	private final int FIND_START_ADDR = 0, FIND_END_ADDR = 1;
	private final int SEND_WITHOUT_MONEY = 3;
	private final int SET_POINTS = 2;

	//UI instances
	private ImageButton btn_back = null;
	private Button  btnSend = null;
	private TextView once_startLocation = null, once_endLocation = null;
//	private TextView onoff_startLocation = null, onoff_endLocation = null;
//	private ImageButton singleTab = null;
//	private TextView singleTxt = null;
//	private ImageButton normalTab = null;
//	private TextView normalTxt = null;
//	private HorizontalPager hor_pager = null;
//	private RelativeLayout indicator_layout = null;
	private RelativeLayout rlOnce = null;
//    private RelativeLayout rlCommute = null;
	private TextView memo = null;
	private ImageButton btnStartCity = null;
	private ImageButton btnEndCity = null;
	private Button btnOnceDate = null;
//    private Button btnOnOffDate = null;
	private TextView setPointsView = null;
	private ImageButton firstCar = null;
	private ImageButton secondCar = null;
	private ImageButton thirdCar = null;
	private ImageButton fourthCar = null;

//	private ImageButton onoffFirstCarType = null;
//	private ImageButton onoffSecondCarType = null;
//	private ImageButton onoffThirdCarType = null;
//	private ImageButton onoffFourthCarType = null;

	private ImageButton onceFirstCarType = null;
	private ImageButton onceSecondCarType = null;
	private ImageButton onceThirdCarType = null;
	private ImageButton onceFourthCarType = null;

	private TextView firstCarTxt = null;
	private TextView secondCarTxt = null;
	private TextView thirdCarTxt = null;
	private TextView fourthCarTxt = null;
	private ImageView decreasePriceBtn = null;
	private ImageView increasePriceBtn = null;
	private EditText priceTxt = null;
	private TextView distanceTip;
	private TextView priceTip;
    private ImageView waitTip;
    private TextView tip;
    private TextView tip1;
    private TextView tip2;
    private ImageView firstCarBack;
    private ImageView secondCarBack;
    private ImageView thirdCarBack;
    private ImageView fourthCarBack;
	//work day UI
//	private ImageButton monday;
//	private ImageButton tuesday;
//	private ImageButton wednesday;
//	private ImageButton thursday;
//	private ImageButton friday;
//	private ImageButton saturday;
//	private ImageButton sunday;

	//others
//	private boolean isSingleTab = true;
	private RecognizerDialog dlgRec = null;
	private static final String NUL_STR = "";
	private static String city = "";
	private int mOnceCarType = NORMAL_CAR;
//	private int mOnoffCarType = NORMAL_CAR;
	private String once_memoStr = NUL_STR;
//	private String onoff_memoStr = NUL_STR;
	private double money = 0;
    private int driverNum;
	//location data
	double once_start_lat, onoff_start_lat;
	double once_start_lng, onoff_start_lng;
	double once_end_lat, onoff_end_lat;
	double once_end_lng, onoff_end_lng;
	//更新状态码
	private static final int REFRESH_TIPS = 0;
	/**
	 * 定位SDK的核心类
	 */
	private LocationClient bm_loc_client;

	//car type list
	private static final int NORMAL_CAR = 1;
	private static final int COMFORT_CAR = 2;
	private static final int LUXURY_CAR = 3;
	private static final int BUSSINESS_CAR = 4;
	//2 list to store distances and prices
	private ArrayList<Double> prices = new ArrayList<Double>();
	private ArrayList<Double> minprices = new ArrayList<Double>();
	private ArrayList<Double> distances = new ArrayList<Double>();
	private ArrayList<Double> priceIntervals = new ArrayList<Double>();
	//middle points string
	private String once_midPoints = NUL_STR;
	private String onoff_midPoints = NUL_STR;
	//work day Indicator
//	private boolean mondayIndicator = true;
//	private boolean tuesdayIndicator = true;
//	private boolean wednesdayIndicator = true;
//	private boolean thursdayIndicator = true;
//	private boolean fridayIndicator = true;
//	private boolean saturdayIndicator = false;
//	private boolean sundayIndicator = false;

	int nOnceYear = 0, nOnceMonth = 0, nOnceDay = 0, nOnceHour = 0, nOnceMinute = 0;
//	int nOnOffHour = 0, nOnOffMinute = 0;
	String szOnceTime = "", szOnoffTime = "";

	WheelDateTimePickerDlg dlg_datetimePicker = null;
	WheelTimePickerDlg dlg_timePicker = null;
    private double defaultpoints = 30;
    private TextView tipTaxi;
    private TextView priceTaxi;
    private DBDao dao;


//    PassPubOnoffOrderSuccessDialog dlgPubOnoffSuccess = null;

	/*
	 *method part
	 */




	@Override
	protected void onCreate(Bundle savedInstanceState) {

//		Log.d(TAG,"working onCreate");
		super.onCreate(savedInstanceState);


		setContentView(R.layout.act_pass_pubcityorder);

		initControls();//控件示例化
		initResolution();//适配屏幕


	}

	@Override
	protected void onResume() {
		Global.resetPublishCounter();
		super.onResume();
	}


	@Override
	protected void onPause()
	{

		super.onPause();
	}

	@Override
	protected void onDestroy()
	{

		super.onDestroy();
	}


	/*
	 * 初始化activity中所有控件
	 */
	private void initControls()
	{
		// Initialize some variables
		city = Global.loadCityName(getApplicationContext());
        dao = DBDao.getDaoInstance(this);

		Calendar cal_cur = Calendar.getInstance();
		cal_cur.add(Calendar.MILLISECOND, ADD_MILLISECONDS);
		nOnceYear = cal_cur.get(Calendar.YEAR);
		nOnceMonth = cal_cur.get(Calendar.MONTH) + 1;
		nOnceDay = cal_cur.get(Calendar.DAY_OF_MONTH);
		nOnceHour = cal_cur.get(Calendar.HOUR_OF_DAY);
		nOnceMinute = cal_cur.get(Calendar.MINUTE);

		szOnceTime = String.format("%02d-%02d-%02d %02d:%02d", nOnceYear, nOnceMonth, nOnceDay, nOnceHour, nOnceMinute);
//		szOnoffTime = String.format("2000-01-01 %02d:%02d", nOnOffHour, nOnOffMinute);

		initSupport();
		initTitleBar();
//		initHorPager();
		initSingleContent();
		//initNormalContent();
		perSetComponents();//perset all components after all views be instances


		// For special situation
		btnOnceDate.setText(szOnceTime);

//		btnOnOffDate = (Button) findViewById(R.id.normal_btnDate);
//		btnOnOffDate.setText(String.format("%02d:%02d", nOnOffHour, nOnOffMinute));
		////////////////////////////////////////////////////////////////////////
	}

	private void perSetComponents() {
		//prepare start location
		autoLocate();
		//prepare tips refresh
		//depletion: a lot of spends
		//new Thread(new tipsRefreshThread()).start();

	}

	/*class tipsRefreshThread implements Runnable{

		@Override
		public void run() {
			//getInfoFromWeb
			if(infoCheckBackground()){

			}else{
				return;
			}
			//发送消息
			Message message = new Message();
			message.what = REFRESH_TIPS;
			PassSingleOrderMainActivity.this.myHandler.sendMessage(message);
		}
	}

	// 实例化一个handler
	Handler myHandler   = new Handler(){
		//接收到消息后处理
		public void handleMessage(Message msg){
			switch (msg.what){
				case REFRESH_TIPS:
					//show tips details
					break;
			}
			super.handleMessage(msg);
		}
	};*/

	/*
	*开始定位
	*/
	private void autoLocate() {

		//实例化定位服务，LocationClient类必须在主线程中声明
		bm_loc_client = new LocationClient(PassPubCityOrderActivity.this);
		bm_loc_client.registerLocationListener(new BDLocationListenerImpl());//注册定位监听接口

		/**
		 * LocationClientOption 该类用来设置定位SDK的定位方式。
		 */
		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);

		bm_loc_client.setLocOption(option);  //设置定位参数

		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);

		bm_loc_client.start();  // 调用此方法开始定位
	}

	/**
	 * 定位接口
	 *
	 */
	public class BDLocationListenerImpl implements BDLocationListener {

		/**
		 * 接收异步返回的定位结果，参数是BDLocation类型参数
		 */
		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.d(TAG, "location------>"+location.getAddrStr());
			if (location.getAddrStr() != null) {
				city = location.getCity();

				if (once_startLocation.getText().toString().equals("")) {
					once_start_lat = location.getLatitude();
					once_start_lng = location.getLongitude();
					once_startLocation.setText(location.getAddrStr());
					Global.saveCoordinates(getApplicationContext(), once_start_lat, once_start_lng);
				}

				Global.saveCityName(getApplicationContext(), city);
				Global.saveCoordinates(getApplicationContext(), location.getLatitude(), location.getLongitude());
				Global.saveDetAddress(getApplicationContext(), location.getAddrStr());

				bm_loc_client.stop();
			} else {
				city = Global.loadCityName(getApplicationContext());

				if (once_startLocation.getText().toString().equals("")) {
					once_start_lat = Global.loadLatitude(getApplicationContext());
					once_start_lng = Global.loadLongitude(getApplicationContext());
					once_startLocation.setText(Global.loadDetAddress(getApplicationContext()));
				}
			}
		}
	}

	private void initSupport() {
		dlgRec = new RecognizerDialog(this, "appid=50e1b967");
		clearPointsMemo();
		getMoney();
		Global.resetPublishCounter();
	}

	private void clearPointsMemo() {
		SharedPreferences sharedPreferences = getSharedPreferences("points", Context.MODE_PRIVATE); //私有数据
		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
		editor.clear();
		editor.commit();
	}

	private void initSingleContent() {
//		isSingleTab = true;
        tip = (TextView)findViewById(R.id.tip);
        tip1 = (TextView)findViewById(R.id.tip1);
        tip2 = (TextView)findViewById(R.id.tip2);
        waitTip = (ImageView)findViewById(R.id.wait_tip);
        waitTip.setVisibility(View.GONE);

        firstCarBack = (ImageView)findViewById(R.id.firstCar_back);
        secondCarBack = (ImageView)findViewById(R.id.secondCar_back);
        thirdCarBack = (ImageView)findViewById(R.id.thirdCar_back);
        fourthCarBack = (ImageView)findViewById(R.id.fourthCar_back);
		distanceTip = (TextView)findViewById(R.id.distance_tip);
		priceTip = (TextView)findViewById(R.id.price_tip);
        tipTaxi = (TextView)findViewById(R.id.tiptaxi);
        priceTaxi = (TextView)findViewById(R.id.price_tiptaxi);
        priceTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPriceTipToMain();
            }
        });
		memo = (TextView)findViewById(R.id.memo);
		memo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				//open memo dialog
				memoOpen();
			}
		});

		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				sendOrder();
			}
		});
		once_startLocation = (TextView) findViewById(R.id.txtStartCity);
		once_startLocation.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				bm_loc_client.stop();
				GotoSearchPage(city,NUL_STR, true);
			}
		});

		once_endLocation = (TextView) findViewById(R.id.txtEndCity);
		once_endLocation.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				GotoSearchPage(city,NUL_STR, false);
			}
		});

		btnStartCity = (ImageButton) findViewById(R.id.btnStartCity);
		btnStartCity.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				initRec(city,0);
			}
		});

		btnEndCity = (ImageButton)findViewById(R.id.btnEndCity);
		btnEndCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initRec(city, 1);
			}
		});

		btnOnceDate = (Button) findViewById(R.id.btnDate);
		btnOnceDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDate();
			}
		});

		setPointsView = (TextView)findViewById(R.id.setPointsView);
		setPointsView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoSetPoints();
			}
		});
		initCarIndicator();
		initPirceSetting();
	}

//	private void initNormalContent() {
//		isSingleTab =false;
//		distanceTip = (TextView)findViewById(R.id.normal_tip1);
//		priceTip = (TextView)findViewById(R.id.normal_price_tip);
//		memo = (TextView)findViewById(R.id.normal_memo);
//		memo.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				//open memo dialog
//				memoOpen();
//			}
//		});
//
//		btnSend = (Button) findViewById(R.id.normal_btnSend);
//		btnSend.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				sendOrder();
//			}
//		});
//
//		onoff_startLocation = (TextView) findViewById(R.id.normal_txtStartCity);
//		onoff_startLocation.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				GotoSearchPage(city,NUL_STR,0);
//			}
//		});
//
//		onoff_endLocation = (TextView) findViewById(R.id.normal_txtEndCity);
//		onoff_endLocation.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				GotoSearchPage(city,NUL_STR,1);
//			}
//		});
//
//
//		if (onoff_startLocation.getText().toString().equals("")) {
//			onoff_start_lat = Global.loadLatitude(getApplicationContext());
//			onoff_start_lng = Global.loadLongitude(getApplicationContext());
//			onoff_startLocation.setText(Global.loadDetAddress(getApplicationContext()));
//		}
//
//		btnStartCity = (ImageButton) findViewById(R.id.normal_btnStartCity);
//		btnStartCity.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				initRec(city,0);
//			}
//		});
//
//		btnEndCity = (ImageButton)findViewById(R.id.normal_btnEndCity);
//		btnEndCity.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				initRec(city, 1);
//			}
//		});
//
//		btnOnOffDate = (Button) findViewById(R.id.normal_btnDate);
//		btnOnOffDate.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseDate();
//			}
//		});
//
//		setPointsView = (TextView)findViewById(R.id.normal_setPointsView);
//		setPointsView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				gotoSetPoints();
//			}
//		});
//
//		initCarIndicatorNormal();
//		initPirceSettingNormal();
//		initWorkDaySettingNormal();
//	}

//	private void initWorkDaySettingNormal() {
//		monday = (ImageButton)findViewById(R.id.monday);
//		monday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(mondayIndicator){
//					monday.setBackgroundResource(R.drawable.icon_day1_white);
//					mondayIndicator = false;
//				}else{
//					monday.setBackgroundResource(R.drawable.icon_day1_select);
//					mondayIndicator = true;
//				}
//			}
//		});
//		tuesday = (ImageButton)findViewById(R.id.tuesday);
//		tuesday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(tuesdayIndicator){
//					tuesday.setBackgroundResource(R.drawable.icon_day2_white);
//					tuesdayIndicator = false;
//				}else{
//					tuesday.setBackgroundResource(R.drawable.icon_day2_select);
//					tuesdayIndicator = true;
//				}
//			}
//		});
//		wednesday = (ImageButton)findViewById(R.id.wednesday);
//		wednesday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(wednesdayIndicator){
//					wednesday.setBackgroundResource(R.drawable.icon_day3_white);
//					wednesdayIndicator = false;
//				}else{
//					wednesday.setBackgroundResource(R.drawable.icon_day3_select);
//					wednesdayIndicator = true;
//				}
//			}
//		});
//		thursday = (ImageButton)findViewById(R.id.thursday);
//		thursday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(thursdayIndicator){
//					thursday.setBackgroundResource(R.drawable.icon_day4_white);
//					thursdayIndicator = false;
//				}else{
//					thursday.setBackgroundResource(R.drawable.icon_day4_select);
//					thursdayIndicator = true;
//				}
//			}
//		});
//		friday = (ImageButton)findViewById(R.id.friday);
//		friday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(fridayIndicator){
//					friday.setBackgroundResource(R.drawable.icon_day5_white);
//					fridayIndicator = false;
//				}else{
//					friday.setBackgroundResource(R.drawable.icon_day5_select);
//					fridayIndicator = true;
//				}
//			}
//		});
//		saturday = (ImageButton)findViewById(R.id.saturday);
//		saturday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(saturdayIndicator){
//					saturday.setBackgroundResource(R.drawable.icon_day6_white);
//					saturdayIndicator = false;
//				}else{
//					saturday.setBackgroundResource(R.drawable.icon_day6_select);
//					saturdayIndicator = true;
//				}
//			}
//		});
//		sunday = (ImageButton)findViewById(R.id.sunday);
//		sunday.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(sundayIndicator){
//					sunday.setBackgroundResource(R.drawable.icon_day7_white);
//					sundayIndicator = false;
//				}else{
//					sunday.setBackgroundResource(R.drawable.icon_day7_select);
//					sundayIndicator = true;
//				}
//			}
//		});
//	}

	/*
	 * communicate with server
	 */
	private void publishOrder() {
		//get work day list
//		String dayList = getDayList();
		//debug middle points
		//publish order

		startProgress();

//		if(isSingleTab)
//		{
			Log.d(TAG, "city" + city);

			CommManager.publishOnceOrder(Global.loadUserID(getApplicationContext()),
					once_startLocation.getText().toString(), once_start_lat, once_start_lng,
					once_endLocation.getText().toString(), once_end_lat, once_end_lng, szOnceTime, once_midPoints,
					once_memoStr, mOnceCarType, Double.parseDouble(priceTxt.getText().toString()),city,
					Global.getIMEI(getApplicationContext()),orderHandler);
//		}else{
//			Log.d(TAG, "points----->" + onoff_midPoints);
//
//			CommManager.publishOnOffOrder(Global.loadUserID(getApplicationContext()),
//					onoff_startLocation.getText().toString(), onoff_start_lat, onoff_start_lng,
//					onoff_endLocation.getText().toString(), onoff_end_lat, onoff_end_lng, szOnoffTime, onoff_midPoints,
//					onoff_memoStr, mOnoffCarType, Double.parseDouble(priceTxt.getText().toString()), dayList, city,
//					Global.getIMEI(getApplicationContext()), normalorderHandler);
//		}
	}

//	private String getDayList() {
//		StringBuilder dayList = new StringBuilder("");
//		if(mondayIndicator){
//			dayList.append("0");
//		}
//		if(tuesdayIndicator){
//			dayList.append(",1");
//		}
//		if(wednesdayIndicator){
//			dayList.append(",2");
//		}
//		if(thursdayIndicator){
//			dayList.append(",3");
//		}
//		if(fridayIndicator){
//			dayList.append(",4");
//		}
//		if(saturdayIndicator){
//			dayList.append(",5");
//		}
//		if(sundayIndicator){
//			dayList.append(",6");
//		}
//		return dayList.toString();
//
//	}

//	private AsyncHttpResponseHandler normalorderHandler = new AsyncHttpResponseHandler()
//	{
//		@Override
//		public void onSuccess(int statusCode, String content) {
//			super.onSuccess(statusCode, content);	//To change body of overridden methods use File | Settings | File Templates.
//			stopProgress();
//
//			try {
//				JSONObject jsonObj = new JSONObject(content);
//				JSONObject jsonResult = jsonObj.getJSONObject("result");
//
//				int nRetcode = jsonResult.getInt("retcode");
//				String jsonMsg = jsonResult.getString("retmsg");
//
//
//				if (nRetcode == ConstData.ERR_CODE_NONE)
//				{
//					Log.d(TAG, "success...........................");
//					//Global.showAdvancedToast(PassPubCityOrderActivity.this,
//					//		getResources().getString(R.string.STR_SUCCESS),
//					//		Gravity.CENTER);
//
//                    dlgPubOnoffSuccess = new PassPubOnoffOrderSuccessDialog(PassPubCityOrderActivity.this);
//                    dlgPubOnoffSuccess.setOnDismissListener(PassPubCityOrderActivity.this);
//					dlgPubOnoffSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    dlgPubOnoffSuccess.show();
//				}
//				else if (nRetcode == ConstData.ERR_CODE_BALNOTENOUGH)
//				{
//					Intent intent = new Intent(PassPubCityOrderActivity.this, PassNoCounterActivity.class);
//					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//					PassPubCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//					startActivityForResult(intent, SEND_WITHOUT_MONEY);
//				}
//				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
//				{
//					logout(jsonMsg);
//				}
//				else
//				{
//					Global.showAdvancedToast(PassPubCityOrderActivity.this, jsonMsg, Gravity.CENTER);
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onFailure(Throwable error, String content) {
//			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
//			stopProgress();
//		}
//	};

    private AsyncHttpResponseHandler orderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            Utils.mLogError("订单信息："+content);
			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");

				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");


				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = jsonResult.getJSONObject("retdata");
					Log.d(TAG,"driver_lock_time ----->"+retdata.getInt("driver_lock_time"));
					//save all time indicators
					SharedPreferences sharedPreferences = getSharedPreferences("wait_time_list", Context.MODE_PRIVATE); //私有数据
					SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
					editor.clear();
					editor.commit();
					editor.putLong("order_id",retdata.getLong("order_id"));
					editor.putInt("wait_time", retdata.getInt("wait_time"));
                    editor.putInt("pub12_time",retdata.getInt("pub12_time"));
                    editor.putInt("pub23_time",retdata.getInt("pub23_time"));
					editor.putInt("driver_lock_time", retdata.getInt("driver_lock_time"));
					editor.putInt("add_price_time1",retdata.getInt("add_price_time1"));
					editor.putInt("add_price_time2",retdata.getInt("add_price_time2"));
					editor.putInt("add_price_time3",retdata.getInt("add_price_time3"));
					editor.putInt("add_price_time4",retdata.getInt("add_price_time4"));
					editor.putInt("add_price_time5",retdata.getInt("add_price_time5"));
					editor.putInt("same_price_time1",retdata.getInt("same_price_time1"));
					editor.putInt("same_price_time2",retdata.getInt("same_price_time2"));
					editor.putInt("same_price_time3",retdata.getInt("same_price_time3"));
					editor.putInt("same_price_time4",retdata.getInt("same_price_time4"));
					editor.putInt("same_price_time5",retdata.getInt("same_price_time5"));

                    editor.putInt("add_price_default",retdata.getInt("add_price_default"));
                    editor.putInt("add_price_range",retdata.getInt("add_price_range"));
                    editor.putInt("add_price_min",retdata.getInt("add_price_min"));
                    editor.putInt("cut_price_range",retdata.getInt("cut_price_range"));
                    editor.putString("prompt_1st", retdata.getString("prompt_1st"));
                    editor.putString("prompt", retdata.getString("prompt"));
					editor.commit();

//                    //setDriverNum
//                    driverNum = retdata.getInt("drvcount");
//                    Log.d(TAG,"driverNum------>"+driverNum);
					gotoWaitInterface();
					//Log.d(TAG, "same_price_time2----->"+sharedPreferences.getInt("same_price_time2", 0));
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(jsonMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPubCityOrderActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassPubCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void gotoWaitInterface() {
		Intent intent = new Intent(PassPubCityOrderActivity.this, PassWaitOnceOrderAcceptanceActivity.class);
        //intent.putExtra("driverNum",driverNum);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		PassPubCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void getMoney(){
		CommManager.getMoney(Global.loadUserID(getApplicationContext()),Global.getIMEI(getApplicationContext()),counterHandler );
	}

	private AsyncHttpResponseHandler counterHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);
			//test server stable or not
			//CommManager.getMoney(Global.loadUserID(getApplicationContext()),Global.getIMEI(getApplicationContext()),counterHandler );
			//countNum++;
			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");

				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");
				//test server stable or not
				if(nRetcode != ConstData.ERR_CODE_NONE){
					Log.d(TAG,"nRetcode test ----->"+nRetcode);
				}

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = jsonResult.getJSONObject("retdata");
					money = retdata.getDouble("money");
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(jsonMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPubCityOrderActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			Global.showAdvancedToast(PassPubCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void getAveragePrice() {
        if(once_start_lat <= 0 || once_start_lng <= 0 || once_end_lat <= 0 ||
                once_end_lng <= 0 || szOnceTime == null || "".equals(szOnceTime))
            return;
		prices.clear();
		minprices.clear();
		distances.clear();
        waitTip.setVisibility(View.VISIBLE);

//		if (isSingleTab) {
		startProgress();
			CommManager.getAveragePrice(Global.loadUserID(getApplicationContext()),
					city, once_start_lat, once_start_lng, once_end_lat, once_end_lng, szOnceTime, once_midPoints,
					Global.getIMEI(getApplicationContext()),
					averagePriceHandler);
//		} else {
//			CommManager.getAveragePrice(Global.loadUserID(getApplicationContext()),
//					city, onoff_start_lat, onoff_start_lng, onoff_end_lat, onoff_end_lng, szOnoffTime, onoff_midPoints,
//					Global.getIMEI(getApplicationContext()),
//					averagePriceHandler);
//		}
	}

	private AsyncHttpResponseHandler averagePriceHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			//Log.d(TAG,"get data------>"+content);
			super.onSuccess(statusCode, content);	//To change body of overridden methods use File | Settings | File Templates.

            Utils.mLogError("平均价："+content);
			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");

				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");

				//Log.d(TAG,"nRetcode------>"+nRetcode);
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray arr = jsonResult.getJSONArray("retdata");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						prices.add(temp.getDouble("aver_price"));
						distances.add(temp.getDouble("distance"));
						priceIntervals.add(temp.getDouble("price_interval"));
                        minprices.add(temp.getDouble("min_limit"));
					}
					showTips();
                    stopProgress();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(jsonMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPubCityOrderActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
            for(int i=0; i<4; i++){
                priceIntervals.add(1.0);
                minprices.add(10.0);
            }
            stopProgress();
			Global.showAdvancedToast(PassPubCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void showTips() {
//		if (isSingleTab) {
        if(distances.isEmpty() || prices.isEmpty()) return;
            waitTip.setVisibility(View.INVISIBLE);
            distanceTip.setVisibility(View.VISIBLE);
            tip.setVisibility(View.VISIBLE);
            tipTaxi.setVisibility(View.VISIBLE);
            tip1.setVisibility(View.VISIBLE);
			distanceTip.setText( distances.get(mOnceCarType - 1) + "km");
			priceTip.setText(Math.round(prices.get(mOnceCarType - 1)) + "点/次");
			priceTip.setVisibility(View.VISIBLE);
            priceTaxi.setText(Math.round(prices.get(prices.size() - 1)) + "点/次");
            priceTaxi.setVisibility(View.VISIBLE);
            priceTxt.setText(""+Math.round(prices.get(mOnceCarType - 1)));
            defaultpoints = Math.round(prices.get(mOnceCarType - 1));
//		} else {
//			distanceTip.setText("行程: " + distances.get(mOnoffCarType) + "km,平台平均价: ");
//			priceTip.setText(prices.get(mOnoffCarType) + "点/次");
//			priceTip.setVisibility(View.VISIBLE);
//		}
	}

    private void setPriceTipToMain()
    {
        priceTxt.setText("" + Math.round(prices.get(mOnceCarType - 1)));
    }

	private void memoOpen() {
		final Dialog memoDialog = new Dialog(PassPubCityOrderActivity.this, R.style.TransStyle_Theme);

		RelativeLayout memoDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.act_pass_memo, null);
		final EditText memoDetail = (EditText)memoDialogView.findViewById(R.id.memo_text);

        Point ptScreen = Global.getScreenSize(getApplicationContext());
        ResolutionSet.instance.iterateChild(memoDialogView.getChildAt(0), ptScreen.x, ptScreen.y);


//		if (isSingleTab) {
			if (!(once_memoStr.equals(NUL_STR))) {
				memoDetail.setText(once_memoStr);
			}
//		}
//		else
//		{
//			if (!(onoff_memoStr.equals(NUL_STR))) {
//				memoDetail.setText(onoff_memoStr);
//			}
//		}

		Button confirmButton = (Button)memoDialogView.findViewById(R.id.confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				memoDialog.dismiss();

//				if (isSingleTab) {
					once_memoStr = memoDetail.getText().toString();
                   	if (once_memoStr.length() > 10) {
                        //-- 20141105 buglist 23
//						memo.setText(once_memoStr.substring(0, 10) + "...");
                        memo.setText(once_memoStr.substring(0, 5) + "...");
					} else {
						memo.setText(once_memoStr);
					}
//				} else {
//					onoff_memoStr = memoDetail.getText().toString();
//					if (onoff_memoStr.length() > 10) {
//						memo.setText(onoff_memoStr.substring(0, 10) + "...");
//					} else {
//						memo.setText(onoff_memoStr);
//					}
//				}
			}
		});
		Button cancelButton = (Button)memoDialogView.findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				memoDialog.dismiss();
			}
		});
		memoDialog.setContentView(memoDialogView);
		memoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		memoDialog.show();
	}

	private void initPirceSetting() {
		priceTxt = (EditText)findViewById(R.id.lblPriceVal);
		//设定显示两位小数(9999.99-0.01)
		priceTxt.addTextChangedListener(new TextWatcher() {
			private boolean isChanged = false;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

                int fPrice = 0;
                try {
                    fPrice = Integer.parseInt(priceTxt.getText().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fPrice = 0;
                    priceTxt.setText("" + fPrice);
                }

                // check max/min price
                if (fPrice < 0)
                {
                    fPrice = 0;
                    priceTxt.setText("" + fPrice);
                }
                else if (fPrice > Global.PRICE_MAX_LIMIT())
                {
                    fPrice = 9999;
                    priceTxt.setText("" + fPrice);
                }

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				//debugging
				String temp = s.toString();
				int posDot = temp.indexOf(".");
				Log.d(TAG,"string:posDot-------->"+temp+":"+posDot);
				if (posDot <= 0){
					if(temp.length()<=4){
						return;
					}else{
						s.delete(4, 5);
						return;
					}
				}else if (posDot == 1){
					if(temp.length()<=6){
						return;
					}else{
						s.delete(6, 7);
						return;
					}

				}else if (posDot == 2){
					if(temp.length()<=7){
						return;
					}else{
						s.delete(7, 8);
						return;
					}

				}
				if (temp.length() - posDot - 1 > 2)
				{
				   s.delete(posDot + 3, posDot + 4);
				};

			}
		});
		decreasePriceBtn = (ImageView)findViewById(R.id.imgPriceMinus);
		decreasePriceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				priceTxt.clearFocus();
				decreasePrice();
			}
		});
		increasePriceBtn = (ImageView)findViewById(R.id.imgPricePlus);
		increasePriceBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				priceTxt.clearFocus();
				increasePrice();
			}
		});
	}

//	private void initPirceSettingNormal() {
//		priceTxt = (EditText)findViewById(R.id.normal_lblPriceVal);
//		//设定显示两位小数(9999.99-0.01)
//		priceTxt.addTextChangedListener(new TextWatcher() {
//			private boolean isChanged = false;
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//									  int count) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//										  int after) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				//debugging
//				String temp = s.toString();
//				int posDot = temp.indexOf(".");
//				Log.d(TAG,"string:posDot-------->"+temp+":"+posDot);
//				if (posDot <= 0){
//					if(temp.length()<=4){
//						return;
//					}else{
//						s.delete(4, 5);
//						return;
//					}
//				}else if (posDot == 1){
//					if(temp.length()<=6){
//						return;
//					}else{
//						s.delete(6, 7);
//						return;
//					}
//
//				}else if (posDot == 2){
//					if(temp.length()<=7){
//						return;
//					}else{
//						s.delete(7, 8);
//						return;
//					}
//
//				}
//				if (temp.length() - posDot - 1 > 2)
//				{
//					s.delete(posDot + 3, posDot + 4);
//				};
//
//			}
//		});
//		decreasePriceBtn = (ImageView)findViewById(R.id.normal_imgPriceMinus);
//		decreasePriceBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				priceTxt.clearFocus();
//				decreasePrice();
//			}
//		});
//		increasePriceBtn = (ImageView)findViewById(R.id.normal_imgPricePlus);
//		increasePriceBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				priceTxt.clearFocus();
//				increasePrice();
//			}
//		});
//	}

	private void increasePrice() {
		if (priceTxt.getText().toString().equals("")) {
			priceTxt.setText("1");
		} else {
			Integer originPrice = Integer.parseInt(priceTxt.getText().toString());
			if (originPrice < 9995) {
				priceTxt.setText(Integer.toString(originPrice + 5));
			}
		}
	}

	private void decreasePrice() {
        String pointsStr = "".equals(priceTxt.getText().toString().trim()) ? "0" : priceTxt.getText().toString().trim();
        if(Double.parseDouble(pointsStr)-1 <= defaultpoints * 0.8){
            Global.showAdvancedToast(this,"不能再低了，车主也不容易呢。",Gravity.CENTER);
            return;
        }

		if (priceTxt.getText().toString().equals("")) {
			priceTxt.setText("1");
		} else {
			Integer originPrice = Integer.parseInt(priceTxt.getText().toString());

				priceTxt.setText(Integer.toString(originPrice - 1));

		}

	}

	private void initCarIndicator() {
		firstCar = (ImageButton) findViewById(R.id.firstCar);
		secondCar = (ImageButton) findViewById(R.id.secondCar);
		thirdCar = (ImageButton) findViewById(R.id.thirdCar);
		fourthCar = (ImageButton) findViewById(R.id.fourthCar);

		onceFirstCarType = (ImageButton) findViewById(R.id.btn_once_cartype1);
		onceSecondCarType = (ImageButton) findViewById(R.id.btn_once_cartype2);
		onceThirdCarType = (ImageButton) findViewById(R.id.btn_once_cartype3);
		onceFourthCarType = (ImageButton) findViewById(R.id.btn_once_cartype4);

		firstCarTxt = (TextView) findViewById(R.id.firstCar_txt);
		secondCarTxt = (TextView) findViewById(R.id.secondCar_txt);
		thirdCarTxt = (TextView) findViewById(R.id.thirdCar_txt);
		fourthCarTxt = (TextView) findViewById(R.id.fourthCar_txt);

        chooseCar(NORMAL_CAR);
		firstCar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(NORMAL_CAR);
			}
		});
		secondCar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(COMFORT_CAR);
			}
		});
		thirdCar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(LUXURY_CAR);
			}
		});
		fourthCar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(BUSSINESS_CAR);
			}
		});

		onceFirstCarType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(NORMAL_CAR);
			}
		});
		onceSecondCarType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(COMFORT_CAR);
			}
		});
		onceThirdCarType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(LUXURY_CAR);
			}
		});
		onceFourthCarType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseCar(BUSSINESS_CAR);
			}
		});
	}

//	private void initCarIndicatorNormal() {
//		firstCar = (ImageButton) findViewById(R.id.normal_firstCar);
//		secondCar = (ImageButton) findViewById(R.id.normal_secondCar);
//		thirdCar = (ImageButton) findViewById(R.id.normal_thirdCar);
//		fourthCar = (ImageButton) findViewById(R.id.normal_fourthCar);
//		firstCarTxt = (TextView) findViewById(R.id.normal_firstCar_txt);
//		secondCarTxt = (TextView) findViewById(R.id.normal_secondCar_txt);
//		thirdCarTxt = (TextView) findViewById(R.id.normal_thirdCar_txt);
//		fourthCarTxt = (TextView) findViewById(R.id.normal_fourthCar_txt);
//
//		onoffFirstCarType = (ImageButton) findViewById(R.id.btn_first_cartype);
//		onoffSecondCarType = (ImageButton) findViewById(R.id.btn_second_cartype);
//		onoffThirdCarType = (ImageButton) findViewById(R.id.btn_third_cartype);
//		onoffFourthCarType = (ImageButton) findViewById(R.id.btn_forth_cartype);
//
//		firstCar.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(NORMAL_CAR);
//			}
//		});
//		secondCar.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(COMFORT_CAR);
//			}
//		});
//		thirdCar.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(LUXURY_CAR);
//			}
//		});
//		fourthCar.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(BUSSINESS_CAR);
//			}
//		});
//
//		onoffFirstCarType.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(NORMAL_CAR);
//			}
//		});
//		onoffSecondCarType.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(COMFORT_CAR);
//			}
//		});
//		onoffThirdCarType.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(LUXURY_CAR);
//			}
//		});
//		onoffFourthCarType.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				chooseCar(BUSSINESS_CAR);
//			}
//		});
//
//	}

	private void chooseCar(int cartype) {
//		if (isSingleTab)
			mOnceCarType = cartype;
//		else
//			mOnoffCarType =cartype;
//        switch (cartype) {
//            case NORMAL_CAR:
//                firstCar.setBackgroundResource(R.drawable.first_car_green);
//                firstCarTxt.setTextColor(this.getResources().getColor(R.color.TITLE_BACKCOLOR));
//                secondCar.setBackgroundResource(R.drawable.second_car_black);
//                secondCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                thirdCar.setBackgroundResource(R.drawable.third_car_black);
//                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                fourthCar.setBackgroundResource(R.drawable.fourth_car_black);
//                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                break;
//            case COMFORT_CAR:
//                firstCar.setBackgroundResource(R.drawable.first_car_black);
//                firstCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                secondCar.setBackgroundResource(R.drawable.second_car_green);
//                secondCarTxt.setTextColor(this.getResources().getColor(R.color.TITLE_BACKCOLOR));
//                thirdCar.setBackgroundResource(R.drawable.third_car_black);
//                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                fourthCar.setBackgroundResource(R.drawable.fourth_car_black);
//                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                break;
//            case LUXURY_CAR:
//                firstCar.setBackgroundResource(R.drawable.first_car_black);
//                firstCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                secondCar.setBackgroundResource(R.drawable.second_car_black);
//                secondCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                thirdCar.setBackgroundResource(R.drawable.third_car_green);
//                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.TITLE_BACKCOLOR));
//                fourthCar.setBackgroundResource(R.drawable.fourth_car_black);
//                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                break;
//            case BUSSINESS_CAR:
//                firstCar.setBackgroundResource(R.drawable.first_car_black);
//                firstCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                secondCar.setBackgroundResource(R.drawable.second_car_black);
//                secondCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                thirdCar.setBackgroundResource(R.drawable.third_car_black);
//                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.BLACK_COLOR));
//                fourthCar.setBackgroundResource(R.drawable.fourth_car_green);
//                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.TITLE_BACKCOLOR));
//                break;
//            default:
//                break;
		switch (cartype) {
			case NORMAL_CAR:
                firstCarBack.setBackgroundResource(R.drawable.green_circle);
                secondCarBack.setBackgroundResource(R.drawable.white_red_circle);
                thirdCarBack.setBackgroundResource(R.drawable.white_brown_circle);
                fourthCarBack.setBackgroundResource(R.drawable.white_blue_circle);
                firstCar.setBackgroundResource(R.drawable.first_car_white);
                secondCar.setBackgroundResource(R.drawable.second_car_color);
                thirdCar.setBackgroundResource(R.drawable.third_car_color);
                fourthCar.setBackgroundResource(R.drawable.fourth_car_color);
                firstCarTxt.setTextColor(this.getResources().getColor(R.color.WHITE_COLOR));
                secondCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_RED_COLOR));
                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_BROWN_COLOR));
                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_BLUE_COLOR));
				break;
			case COMFORT_CAR:
                firstCarBack.setBackgroundResource(R.drawable.white_green_circle);
                secondCarBack.setBackgroundResource(R.drawable.red_circle);
                thirdCarBack.setBackgroundResource(R.drawable.white_brown_circle);
                fourthCarBack.setBackgroundResource(R.drawable.white_blue_circle);
                firstCar.setBackgroundResource(R.drawable.first_car_color);
                secondCar.setBackgroundResource(R.drawable.second_car_white);
                thirdCar.setBackgroundResource(R.drawable.third_car_color);
                fourthCar.setBackgroundResource(R.drawable.fourth_car_color);
                firstCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_GREEN_COLOR));
                secondCarTxt.setTextColor(this.getResources().getColor(R.color.WHITE_COLOR));
                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_BROWN_COLOR));
                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_BLUE_COLOR));
				break;
			case LUXURY_CAR:
                firstCarBack.setBackgroundResource(R.drawable.white_green_circle);
                secondCarBack.setBackgroundResource(R.drawable.white_red_circle);
                thirdCarBack.setBackgroundResource(R.drawable.brown_circle);
                fourthCarBack.setBackgroundResource(R.drawable.white_blue_circle);
                firstCar.setBackgroundResource(R.drawable.first_car_color);
                secondCar.setBackgroundResource(R.drawable.second_car_color);
                thirdCar.setBackgroundResource(R.drawable.third_car_white);
                fourthCar.setBackgroundResource(R.drawable.fourth_car_color);
                firstCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_GREEN_COLOR));
                secondCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_RED_COLOR));
                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.WHITE_COLOR));
                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_BLUE_COLOR));
				break;
			case BUSSINESS_CAR:
                firstCarBack.setBackgroundResource(R.drawable.white_green_circle);
                secondCarBack.setBackgroundResource(R.drawable.white_red_circle);
                thirdCarBack.setBackgroundResource(R.drawable.white_brown_circle);
                fourthCarBack.setBackgroundResource(R.drawable.blue_circle);
                firstCar.setBackgroundResource(R.drawable.first_car_color);
                secondCar.setBackgroundResource(R.drawable.second_car_color);
                thirdCar.setBackgroundResource(R.drawable.third_car_color);
                fourthCar.setBackgroundResource(R.drawable.fourth_car_white);
                firstCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_GREEN_COLOR));
                secondCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_RED_COLOR));
                thirdCarTxt.setTextColor(this.getResources().getColor(R.color.BTN_BROWN_COLOR));
                fourthCarTxt.setTextColor(this.getResources().getColor(R.color.WHITE_COLOR));
				break;
			default:
				break;
		}
		if(infoCheckBackground()){
			showTips();
		}
	}
	/*
	 * go to PassWaitForOrderActivity
	 */


	private void sendOrder() {
		if (infoCheck())
		{
			saveUsefulData();

			if (money < Double.parseDouble(priceTxt.getText().toString())) {
				Intent intent = new Intent(PassPubCityOrderActivity.this, PassNoCounterActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassPubCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivityForResult(intent, SEND_WITHOUT_MONEY);
			} else {
                String pointsStr = "".equals(priceTxt.getText().toString().trim()) ? "0" : priceTxt.getText().toString().trim();
                if(Double.parseDouble(pointsStr) <= defaultpoints * 0.8){
                    Global.showAdvancedToast(this,"不能再低了，车主也不容易呢。",Gravity.CENTER);
                    return;
                }
				publishOrder();
			}
		}
	}

	private void saveUsefulData() {
		SharedPreferences sharedPreferences = getSharedPreferences("single_order_data", Context.MODE_PRIVATE); //私有数据
		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
		editor.clear();
		editor.commit();
		editor.putFloat("cost_detail", (float) Double.parseDouble(priceTxt.getText().toString()));
        editor.putFloat("once_start_lat", (float)once_start_lat);
        editor.putFloat("once_end_lat", (float)once_end_lat);
        editor.putFloat("once_start_lng", (float)once_start_lng);
        editor.putFloat("once_end_lng", (float)once_end_lng);
        editor.putString("once_midPoints", once_midPoints);

//		if (isSingleTab) {
		double price_interval = 10;
		double price = Double.parseDouble(priceTxt.getText().toString());
        double minPrice = 10.0;

		if (priceIntervals.size() >= mOnceCarType)
		{
			price_interval = priceIntervals.get(mOnceCarType - 1);
		}

		if (prices.size() >= mOnceCarType)
		{
			price = prices.get(mOnceCarType - 1);
		}

        if (minprices.size() >= mOnceCarType)
        {
            minPrice = minprices.get(mOnceCarType - 1);
        }

		editor.putFloat("price_interval", (float)price_interval);
		editor.putFloat("average_price", (float)price);
        editor.putFloat("min_price", (float)minPrice);
//		} else {
//			editor.putFloat("price_interval", priceIntervals.get(mOnoffCarType).floatValue());
//			editor.putFloat("average_price", prices.get(mOnoffCarType).floatValue());
//		}
        editor.commit();
	}


	/*
	 *check startInput, endInput process background
	 */
	private boolean infoCheckBackground() {
//		if (isSingleTab) {
			if ((once_startLocation.getText()).equals(NUL_STR)) {
				return false;
			}

			if ((once_endLocation.getText()).equals(NUL_STR)) {
				return false;
			}
//		} else {
//			if ((onoff_startLocation.getText()).equals(NUL_STR)) {
//				return false;
//			}
//
//			if ((onoff_endLocation.getText()).equals(NUL_STR)) {
//				return false;
//			}
//
//		}

		return true;
	}
	/*
	 *check time, startInput, endInput, PriceInput
	 */
	private boolean infoCheck() {
		double price = 0;

		try {
            String pointsStr = "".equals(priceTxt.getText().toString().trim()) ? "0" : priceTxt.getText().toString().trim();
			price = Double.parseDouble(pointsStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		double minprice = defaultpoints * 0.8;
//		if (isSingleTab)
//		{
//			if (minprices.size() >= mOnceCarType - 1 && minprices.size() != 0)
//				minprice = minprices.get(mOnceCarType - 1);
//		}
//		else
//		{
//			if (minprices.size() >= mOnoffCarType && minprices.size() != 0)
//				minprice = minprices.get(mOnoffCarType);
//		}

		if (price <= minprice)
		{
			String szMsg = getResources().getString(R.string.STR_PRICE_TOOLOW);
			szMsg += (int)minprice;
			szMsg += getResources().getString(R.string.STR_BALANCE_DIAN);

			priceTxt.selectAll();
			priceTxt.requestFocus();

//			CommonAlertDialog dialog = new CommonAlertDialog.Builder(PassPubCityOrderActivity.this)
//					.message(szMsg)
//					.type(CommonAlertDialog.DIALOGTYPE_ALERT)
//					.build();
//			dialog.show();
            Global.showAdvancedToast(PassPubCityOrderActivity.this,szMsg,Gravity.CENTER);

			return false;
		}

//		if (isSingleTab) {
			if ((once_startLocation.getText()).equals(NUL_STR)) {
				Global.showAdvancedToast(PassPubCityOrderActivity.this,
						getResources().getString(R.string.STR_TOAST_NOSTARTMESSAGE), Gravity.CENTER);
				return false;
			}

			if ((once_endLocation.getText()).equals(NUL_STR)) {
				Global.showAdvancedToast(PassPubCityOrderActivity.this,
						getResources().getString(R.string.STR_TOAST_NOENDTEMESSAGE), Gravity.CENTER);
				return false;
			}
//		} else {
//			if ((onoff_startLocation.getText()).equals(NUL_STR)) {
//				Global.showAdvancedToast(PassPubCityOrderActivity.this,
//						getResources().getString(R.string.STR_TOAST_NOSTARTMESSAGE), Gravity.CENTER);
//				return false;
//			}
//
//			if ((onoff_endLocation.getText()).equals(NUL_STR)) {
//				Global.showAdvancedToast(PassPubCityOrderActivity.this,
//						getResources().getString(R.string.STR_TOAST_NOENDTEMESSAGE), Gravity.CENTER);
//				return false;
//			}
//		}


//		if (isSingleTab) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date settingDate = sdf.parse(szOnceTime);
				if (settingDate.before(date)) {
					Global.showAdvancedToast(PassPubCityOrderActivity.this,
							getResources().getString(R.string.STR_TOAST_LATEMESSAGE), Gravity.CENTER);
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
//		}

		//check price
		if((priceTxt.getText().toString()).equals(NUL_STR)) {
			Global.showAdvancedToast(PassPubCityOrderActivity.this,
					getResources().getString(R.string.STR_TOAST_NOPRICEMESSAGE), Gravity.CENTER);
			return false;
		}

		return true;
	}

	private void gotoSetPoints() {
		Intent intent = new Intent(PassPubCityOrderActivity.this, PassSetPointsActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//		if (isSingleTab)
			intent.putExtra("points_string", once_midPoints);
//		else
//			intent.putExtra("points_string", onoff_midPoints);
		startActivityForResult(intent, SET_POINTS);
	}

	private String getCurrentTime() {
		StringBuilder mStr = new StringBuilder();
		Date date = new Date();
		date.setTime(date.getTime() + ADD_MILLISECONDS);
		SimpleDateFormat mDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return mDateFormat.format(date);
	}

	private void chooseDate() {
//		if (isSingleTab) {
			showDateTimeDialog();
//		} else {
//			showTimeDialog();
//		}

	}

	private void initRec(final String city, final int startEnd) {
		dlgRec.setEngine("sms", null, null);
		dlgRec.setSampleRate(SpeechConfig.RATE.rate16k);
		dlgRec.setListener(new RecognizerDialogListener() {
			private StringBuilder mStr = new StringBuilder();
			@Override
			public void onResults(ArrayList<RecognizerResult> result, boolean b) {
				for (RecognizerResult recognizerResult : result)
				{
					//Log.d(TAG, "RecResult----->"+recognizerResult.text);
                    mStr.append(recognizerResult.text);
				}
			}

			@Override
			public void onEnd(SpeechError speechError) {
                //Log.d(TAG, "OnEnd str----->"+mStr.toString());
                if (speechError == null)
                {
				    String strAddr = Global.eatChinesePunctuations(mStr.toString());
                    //Log.d(TAG, "OnEnd str----->"+strAddr);
                    GotoSearchPageFromMic(city, strAddr, startEnd);
                }
			}
		});
		dlgRec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgRec.show();
	}

	private void GotoSearchPage(String city, String addr, boolean isStart) {
		Intent intentStart = new Intent(PassPubCityOrderActivity.this, TextAddrSearchActivity.class);
		intentStart.putExtra("City", city);
		intentStart.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
		intentStart.putExtra("Pos", addr);

		intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		PassPubCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());

		if (isStart)
			startActivityForResult(intentStart, FIND_START_ADDR);
		else{
            intentStart.putExtra("startend",1);
            startActivityForResult(intentStart, FIND_END_ADDR);
        }

	}

    private void GotoSearchPageFromMic(String city, String addr, int startEnd) {
        Intent intentStart = new Intent(PassPubCityOrderActivity.this, TextAddrSearchActivity.class);
        intentStart.putExtra("City", city);
        intentStart.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
        intentStart.putExtra("Pos", addr);
        intentStart.putExtra("startend",startEnd);
        intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        PassPubCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());

	    if (startEnd == 0)          // Start address
            startActivityForResult(intentStart, FIND_START_ADDR);
	    else
		    startActivityForResult(intentStart, FIND_END_ADDR);
    }

//	private void initHorPager() {
//		hor_pager = (HorizontalPager)findViewById(R.id.hor_pager);
//		hor_pager.setVisibility(View.VISIBLE);
//		hor_pager.setScrollChangeListener(new HorizontalPager.OnHorScrolledListener()
//		{
//			@Override
//			public void onScrolled() {
//				controlIndicatorPos();
//			}
//		});
//
//		rlOnce = (RelativeLayout) findViewById(R.id.content_layout);
//		rlOnce.setVisibility(View.VISIBLE);
//		rlCommute = (RelativeLayout) findViewById(R.id.normal_layout);
//		rlCommute.setVisibility(View.VISIBLE);
//
//		ViewGroup parentView = null;
//		parentView = (ViewGroup)rlOnce.getParent();
//		parentView.removeView(rlOnce);
//
//		parentView = (ViewGroup)rlCommute.getParent();
//		parentView.removeView(rlCommute);
//
//		hor_pager.addView(rlOnce);
//		hor_pager.addView(rlCommute);
//
//		indicator_layout = (RelativeLayout)findViewById(R.id.pageIndicator);
//
//		singleTxt = (TextView) findViewById(R.id.singleTxt);
//		normalTxt = (TextView) findViewById(R.id.normalTxt);
//
//		singleTab = (ImageButton) findViewById(R.id.singleTab);
//		singleTab.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				hor_pager.setCurrentScreen(0, true);
//			}
//		});
//		normalTab = (ImageButton) findViewById(R.id.normalTab);
//		normalTab.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				hor_pager.setCurrentScreen(1, true);
//			}
//		});
//
//	}

	private void initTitleBar() {
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});
	}



	/*
	 * 划条控制
	 */

//	private void controlIndicatorPos()
//	{
//		if (indicator_layout == null)
//			return;
//
//		int nScrollX = hor_pager.getScrollX();
//		int nPageWidth = hor_pager.getWidth();
//
//		int nIndicatorWidth = indicator_layout.getWidth();
//		if (nIndicatorWidth == 0) {
//			return;
//		}
//
//		int nTabItemWidth = 240;
//		RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(nTabItemWidth, 3);
//		layout_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		layout_params.leftMargin = nTabItemWidth * nScrollX / nPageWidth;
//
//		indicator_layout.setLayoutParams(layout_params);
//		indicator_layout.setTag(R.string.TAG_KEY_WIDTH, "" + layout_params.width);
//		indicator_layout.setTag(R.string.TAG_KEY_HEIGHT, "" + layout_params.height);
//		indicator_layout.setTag(R.string.TAG_KEY_MARGINLEFT, "" + layout_params.leftMargin);
//
//		ResolutionSet.instance.iterateChild((View)indicator_layout.getParent(), mScrSize.x, mScrSize.y);
//
//		if (nScrollX < nPageWidth / 2)
//		{
//			singleTxt.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
//			normalTxt.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
//			initSingleContent();
//		}
//		else
//		{
//			singleTxt.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
//			normalTxt.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
//			initNormalContent();
//		}
//	}

	/*
	 * 适配不同屏幕
	 */

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
	 * 获取地址搜索界面传过来的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == FIND_START_ADDR)
		{
//				if (isSingleTab) {
					once_startLocation.setText(data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME));
					once_start_lat = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0);
					once_start_lng = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0);
//				} else {
//					onoff_startLocation.setText(data.getStringExtra("Name"));
//					onoff_start_lat = data.getDoubleExtra("lat", 0);
//					onoff_start_lng = data.getDoubleExtra("lon", 0);
//				}
		}
		else if (requestCode == FIND_END_ADDR)
		{
//			if (isSingleTab) {
            String addr = data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME);
			once_endLocation.setText(addr);
			once_end_lat = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0);
			once_end_lng = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0);
            dao.add(addr,once_end_lat+"",once_end_lng+"",System.currentTimeMillis()+"","1");
//		    } else {
//		    	onoff_endLocation.setText(data.getStringExtra("Name"));
//		    	onoff_end_lat = data.getDoubleExtra("lat", 0);
//		    	onoff_end_lng = data.getDoubleExtra("lon", 0);
//		    }
		} else if (requestCode == SET_POINTS) {
			//Log.d(TAG, "set interval points---------------------");
			setPointsView.setText("中途点："+(data.getIntExtra("pointsNum", 0)));
//			if (isSingleTab)
				once_midPoints = data.getStringExtra("points_string");
//			else
//				onoff_midPoints = data.getStringExtra("points_string");
		} else if (requestCode == SEND_WITHOUT_MONEY) {
			publishOrder();
		}

		if(infoCheckBackground()){
			getAveragePrice();
		}
	}



	private void showDateTimeDialog() {
		if (dlg_datetimePicker != null && dlg_datetimePicker.isShowing()) {
			return;
		}

		dlg_datetimePicker = new WheelDateTimePickerDlg(PassPubCityOrderActivity.this);
		dlg_datetimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, nOnceYear);
		cal.set(Calendar.MONTH, nOnceMonth - 1);
		cal.set(Calendar.DAY_OF_MONTH, nOnceDay);
		cal.set(Calendar.HOUR_OF_DAY, nOnceHour);
		cal.set(Calendar.MINUTE, nOnceMinute);

		dlg_datetimePicker.setCurDate(cal.getTime());
		dlg_datetimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				WheelDateTimePickerDlg convDlg = (WheelDateTimePickerDlg) dialog;
				nOnceYear = convDlg.getYear();
				nOnceMonth = convDlg.getMonth();
				nOnceDay = convDlg.getDay();
				nOnceHour = convDlg.getHour();
				nOnceMinute = convDlg.getMinute();

				szOnceTime = String.format("%04d-%02d-%02d %2d:%02d", nOnceYear, nOnceMonth, nOnceDay, nOnceHour, nOnceMinute);
				btnOnceDate.setText(szOnceTime);
                getAveragePrice();
			}
		});
		dlg_datetimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlg_datetimePicker.show();
	}


//	private void showTimeDialog() {
//		if (dlg_timePicker != null && dlg_timePicker.isShowing())
//			return;
//
//		dlg_timePicker = new WheelTimePickerDlg(PassPubCityOrderActivity.this);
//		dlg_timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.YEAR, 2000);
//		cal.set(Calendar.MONTH, 1);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		cal.set(Calendar.HOUR_OF_DAY, nOnOffHour);
//		cal.set(Calendar.MINUTE, nOnOffMinute);
//
//		dlg_timePicker.setCurDate(cal.getTime());
//		dlg_timePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				WheelTimePickerDlg convDlg = (WheelTimePickerDlg) dialog;
//
//				nOnOffHour = convDlg.getHour();
//				nOnOffMinute = convDlg.getMinute();
//
//				szOnoffTime = String.format("2000-01-01 %02d:%02d", nOnOffHour, nOnOffMinute);
//				btnOnOffDate.setText(String.format("%02d:%02d", nOnOffHour, nOnOffMinute));
//			}
//		});
//		dlg_timePicker.show();
//	}

    @Override
    public void onDismiss(DialogInterface dialog)
    {
//        if (dlgPubOnoffSuccess == dialog)
//        {
//            int nRes = dlgPubOnoffSuccess.IsDeleted();
//            if (nRes == 1)
//            {
//                setResult(RESULT_OK);
//                finishWithAnimation();
//            }
//        }
    }

}

