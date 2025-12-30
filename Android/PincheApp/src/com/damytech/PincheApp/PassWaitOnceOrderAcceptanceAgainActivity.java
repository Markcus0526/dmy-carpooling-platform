package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.DialogUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 拼友主界面
 */
public class PassWaitOnceOrderAcceptanceAgainActivity extends SuperActivity
{
	private boolean isInit = false;
    /*
     *static references part
     */
    //log tag
    private static final String TAG = "erik_debug";
    //message condition code
    private static final int MESSAGE_TIMER = 0 ;
    private static final int MESSAGE_COMM = 1 ;
    //应用状态码
    private int REQCODE_TEST = 0;

    //UI instances
    private ImageView timerView = null;
    private TextView timerTxt = null;
    private ImageButton locationButton;
    private TextView driverNumTxt;

    //others
    private int timeLimit = 11;
    private long orderId;
    private int driverNum;
    // Baidu variables
    private MapView mapView = null;
	private BaiduMap baiduMap = null;
    private TextView popText;
	/**
	 * 定位SDK的核心类
	 */
	private LocationClient bm_loc_client;
	/**
	 * 用户位置信息
	 */
	private MyLocationData mLocData;
    private OverlayOptions option;
    private Marker mainMarker;
    private LatLng point;
    private long driverId;
	/**
	 * 我的位置图层
	 */
//	private LocationOverlay myLocationOverlay = null;
	/**
	 * 弹出窗口图层
	 */
//	private PopupOverlay mPopupOverlay  = null;



//    MKSearch bm_search = null;
//    MKSearchListener bm_search_listener = null;
    /**
     * 弹出窗口图层的View
     */
    private View mPopupView;
    private BDLocation location;
    private Button cancelOrder;

    //current location data
    private double latitude;
    private double longitude;
    private boolean isShowDialog = false;

    //other
    ArrayList<DriverLocation> driverLocations = new ArrayList<DriverLocation>();

	/*
	 *method part
	 */

    //定位图层实现
//    private class LocationOverlay extends MyLocationOverlay{
//
//        public LocationOverlay(MapView arg0) {
//            super(arg0);
//        }
//
//
//        /**
//         * 在“我的位置”坐标上处理点击事件。
//         */
//        @Override
//        protected boolean dispatchTap() {
//            return super.dispatchTap();
//
//        }
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
	    initBaidu();
        setContentView(R.layout.act_pass_wait_order_again);

        initControls();//控件示例化
        initResolution();//适配屏幕

    }

    @Override
    protected void onResume() {
        bm_loc_client.start();
        //initSearchManager();
        mapView.onResume();
        startCheckBackData();
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
    }


    @Override
    protected void onPause()
    {
        //Log.d(TAG,"onPause()");
        handler.removeMessages(MESSAGE_COMM);
        bm_loc_client.stop();
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        //Log.d(TAG,"onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        //Log.d(TAG,"onDestroy()");
        bm_loc_client.stop();
        mapView.onDestroy();
        super.onDestroy();
    }
    /*
     * back button event
     */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelOrder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	/*
		 * 初始化activity中所有控件
		 */
	private void initControls(){
		initBaidu();
		initMap();
		driverNumTxt = (TextView)findViewById(R.id.notification);
		timerTxt = (TextView)findViewById(R.id.timer_text);
		timerView = (ImageView)findViewById(R.id.timer_image);
		cancelOrder = (Button)findViewById(R.id.btn_cancel_order);
		cancelOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cancelOrder();
			}
		});
		getOrderDetail();
        CommManager.getOnceOrderDriverPos(Global.loadUserID(getApplicationContext()), orderId,
                Global.getIMEI(getApplicationContext()), driverListHandler);
		startTimer();
		startCheckBackData();
	}

    private void cancelOrder() {
        //server communicate
        CommManager.cancelOnceOrder(Global.loadUserID(getApplicationContext()), orderId,
		        Global.getIMEI(getApplicationContext()), cancelOrderHandler);
    }

    private AsyncHttpResponseHandler cancelOrderHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");
                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                //Log.d(TAG, "code:message ------->"+ nRetcode+":"+jsonMsg);
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    //JSONObject retdata = jsonResult.getJSONObject("retdata");
                    Log.d(TAG,"success.....................");
                    handler.removeCallbacksAndMessages(null);
                    finish();

                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassWaitOnceOrderAcceptanceAgainActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassWaitOnceOrderAcceptanceAgainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };


    /*
    * 初始化baidu变量
    */
    private void initBaidu()
    {
	    SDKInitializer.initialize(getApplicationContext());
    }

    private void initMap() {

        //location button
        locationButton = (ImageButton)findViewById(R.id.locate_btn);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bm_loc_client.start();
//                mMapController.setZoom(15);
	            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
            }
        });
        mapView = (MapView)findViewById(R.id.img_map);
	    mapView.showZoomControls(false);
        mapView.showScaleControl(false);
	    baiduMap = mapView.getMap();
        mPopupView = LayoutInflater.from(this).inflate(R.layout.pop_view, null);
        ResolutionSet.instance.iterateChild(mPopupView, mScrSize.x, mScrSize.y);
        //实例化弹出窗口图层
//        mPopupOverlay = new PopupOverlay(mapView,new PopupClickListener() {
//
//            /**
//             * 点击弹出窗口图层回调的方法
//             */
//            @Override
//            public void onClickedPopup(int arg0) {
//                //隐藏弹出窗口图层
//                mPopupOverlay.hidePop();
//            }
//        });
        //自动定位
        autoLocate();
        //定位图层初始化
//        myLocationOverlay = new LocationOverlay(mapView);


        //实例化定位数据，并设置在我的位置图层
//        mLocData = new MyLocationData.Builder()
//		        .latitude()
//        myLocationOverlay.setData(mLocData);

        //添加定位图层
//        mapView.getOverlays().add(myLocationOverlay);

        //修改定位数据后刷新图层生效
//        mapView.refresh();
    }

    /*
    *开始定位
    */
    private void autoLocate() {
//        Log.d(TAG,"mapManger------>"+Global.gMapManager);
//        Log.d(TAG,"mapView------>"+mapView);
//		Log.d(TAG,"MapController------>"+mMapController);
        //实例化定位服务，LocationClient类必须在主线程中声明
        bm_loc_client = new LocationClient(PassWaitOnceOrderAcceptanceAgainActivity.this);
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


        bm_loc_client.start();  // 调用此方法开始定位
    }

    /**
     * 定位接口，需要实现两个方法
     *
     * 异步接受百度查询数据
     */
    public class BDLocationListenerImpl implements BDLocationListener {

        /**
         * 接收异步返回的定位结果，参数是BDLocation类型参数
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null ||
		        location.getAddrStr() == null) {
                return;
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();

	        // Must save coordinates and address information
	        Global.saveCoordinates(getApplicationContext(), latitude, longitude);
	        Global.saveCityName(getApplicationContext(), location.getCity());
	        Global.saveDetAddress(getApplicationContext(), location.getAddrStr());

	        PassWaitOnceOrderAcceptanceAgainActivity.this.location = location;

	        mLocData = new MyLocationData.Builder().latitude(latitude).longitude(longitude).accuracy(location.getRadius()).direction(location.getDirection()).build();
	        baiduMap.setMyLocationData(mLocData);

			if (!isInit) {
				MapStatus status = new MapStatus.Builder().zoom(15).target(new LatLng(latitude, longitude)).build();
				baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(status));
				isInit = true;
			}
            //move to location position
            LatLng point = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
            baiduMap.animateMapStatus(update);
            showPopupOverlay(location);
            bm_loc_client.stop();
		}
	}

    /**
     * 显示弹出窗口图层PopupOverlay
     * @param location
     */
    private void showPopupOverlay(BDLocation location){
        //定义User Maker坐标点
        point = new LatLng(location.getLatitude(),
                location.getLongitude());
        //构建Marker图标
        //ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(mPopupView));
        //构建MarkerOption，用于在地图上添加Marker
        option = new MarkerOptions()
                .position(point)
                .zIndex(5)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        //将marker添加到地图上
        mainMarker = (Marker) (baiduMap.addOverlay(option));
        popText = (TextView)mPopupView.findViewById(R.id.location_tips);
        //binder listeners
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker == mainMarker){
                    Log.d(TAG,"location tip appear..................");
                    mainMarker.remove();
                    if(PassWaitOnceOrderAcceptanceAgainActivity.this.location == null){
                        popText.setText("");
                    }else{
                        popText.setText(PassWaitOnceOrderAcceptanceAgainActivity.this.location.getAddrStr());
                    }
                    popText.setVisibility(View.VISIBLE);
                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(mPopupView, point, 0);
                    //显示InfoWindow
                    baiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        });
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            public void onMapStatusChangeStart(MapStatus status) {
                baiduMap.clear();
                popText.setVisibility(View.GONE);
                point = new LatLng(PassWaitOnceOrderAcceptanceAgainActivity.this.location.getLatitude(),
                        PassWaitOnceOrderAcceptanceAgainActivity.this.location.getLongitude());
                //构建Marker图标
                //ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(mPopupView));
                //构建MarkerOption，用于在地图上添加Marker
                option = new MarkerOptions()
                        .position(point)
                        .zIndex(5)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                //将marker添加到地图上
                mainMarker = (Marker) (baiduMap.addOverlay(option));
                displayDriverLocations();
            }

            public void onMapStatusChangeFinish(MapStatus status) {

            }

            public void onMapStatusChange(MapStatus status) {

            }
        });
    }


    /**
	 * 将View转换成Bitmap的方法
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}


    private void getOrderDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("wait_time_list", Context.MODE_PRIVATE);
        //timeLimit = 30;//erik_debug:just for development
        orderId = sharedPreferences.getLong("order_id", 0);
        timeLimit = getIntent().getIntExtra("wait_time", 0);
    }

    private void startCheckBackData(){
        Message message = handler.obtainMessage(MESSAGE_COMM);     // Message
        handler.sendMessageDelayed(message, 3000);
    }

    private void startTimer() {
        timerViewRotate();
        Message message = handler.obtainMessage(MESSAGE_TIMER);     // Message
        handler.sendMessageDelayed(message, 1000);
    }

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case MESSAGE_TIMER:
                    timeLimit--;
                    timerTxt.setText("" + timeLimit);
                    if(timeLimit > 0){
                        Message message = handler.obtainMessage(MESSAGE_TIMER);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                        if(isShowDialog == true){
                            isShowDialog = false;
                            DialogUtil.cancelDialog();
                        }
                        cancelOrder();
                        gotoAddPrice();
                        //gotoResult();

                    }
                    break;
                case MESSAGE_COMM:
                    CheckBackData();
                    Message message = handler.obtainMessage(MESSAGE_COMM);     // Message
                    handler.sendMessageDelayed(message, 3000);
                    break;
            }

			super.handleMessage(msg);
		}
	};

    /*
	 * get drivers locations
	 */
    private class DriverLocation{
        int driverId;
        double lng;
        double lat;
    }

    private AsyncHttpResponseHandler driverListHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);	//To change body of overridden methods use File | Settings | File Templates.

            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONArray retdata = jsonResult.getJSONArray("retdata");
                    driverLocations.clear();
                    for(int i=0; i<retdata.length();i++){
                        JSONObject retItem = retdata.getJSONObject(i);
                        DriverLocation driverLocation = new DriverLocation();
                        driverLocation.driverId = retItem.getInt("driverid");
                        driverLocation.lat = retItem.getDouble("lat");
                        driverLocation.lng = retItem.getDouble("lng");
                        driverLocations.add(driverLocation);
                    }
                    displayDriverLocations();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassWaitOnceOrderAcceptanceAgainActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassWaitOnceOrderAcceptanceAgainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void displayDriverLocations() {
        driverNumTxt.setText(" "+driverLocations.size()+" ");
        //define driver markers
        for(DriverLocation location : driverLocations){
            //定义User Maker坐标点
            LatLng point = new LatLng(location.lat, location.lng);
            //构建Marker图标
            //ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
            View driverView = LayoutInflater.from(this).inflate(R.layout.driver_pop_view, null);
            ResolutionSet.instance.iterateChild(driverView, mScrSize.x, mScrSize.y);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(driverView);
            //构建MarkerOption，用于在地图上添加Marker
            option = new MarkerOptions()
                    .position(point)
                    .zIndex(1)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            //将marker添加到地图上
            baiduMap.addOverlay(option);
        }
    }

    private void CheckBackData() {

        CommManager.checkOnceOrderAcceptance(Global.loadUserID(getApplicationContext()),
                orderId, latitude, longitude,
                Global.getIMEI(getApplicationContext()), orderCheckHandler);
    }

    private AsyncHttpResponseHandler orderCheckHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                Log.d(TAG, "nRetcode---->" +nRetcode);
                Log.d(TAG, "ret message---->"+jsonMsg);
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    if(isShowDialog == true){
                        DialogUtil.cancelDialog();
                        isShowDialog = false;
                    }
                    JSONObject retdata = jsonResult.getJSONObject("retdata");
                    Log.d(TAG,"detial---->"+ retdata);
					driverId = retdata.getLong("drvid");
                    handler.removeCallbacksAndMessages(null);
                    saveOrderInfo(retdata);
                    gotoConfirm();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    if(isShowDialog == true){
                        DialogUtil.cancelDialog();
                        isShowDialog = false;
                    }
                    logout(jsonMsg);
                }
                else
                {
                    if(isShowDialog == false){
                        isShowDialog = true;
                        DialogUtil.showDialog(PassWaitOnceOrderAcceptanceAgainActivity.this, jsonMsg, Gravity.CENTER);
                    }
//                    Global.showAdvancedToast(PassWaitOnceOrderAcceptanceAgainActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassWaitOnceOrderAcceptanceAgainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void saveOrderInfo(JSONObject retdata) throws Exception{
        SharedPreferences sharedPreferences = getSharedPreferences("single_order_detail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.clear();
        editor.commit();
        editor.putString("img", retdata.getString("img"));
        editor.putString("name", retdata.getString("name"));
        editor.putInt("gender", retdata.getInt("gender"));
        editor.putInt("age", retdata.getInt("age"));
        editor.putInt("drv_career", retdata.getInt("drv_career"));
        editor.putInt("evgood_rate", retdata.getInt("evgood_rate"));
        editor.putInt("carpool_count", retdata.getInt("carpool_count"));
        editor.putInt("type", retdata.getInt("type"));
        editor.putString("brand", retdata.getString("brand"));
        editor.putString("style", retdata.getString("style"));
        editor.putString("color", retdata.getString("color"));
        editor.putString("carimg", retdata.getString("carimg"));
        editor.putString("start_addr", retdata.getString("start_addr"));
        editor.putString("end_addr", retdata.getString("end_addr"));
        StringBuilder middlePoints = new StringBuilder("");
        JSONArray pointsArray = retdata.getJSONArray("midpoints");
        for(int i=0; i<pointsArray.length(); i++ ){
            middlePoints.append(pointsArray.getJSONObject(i).getString("addr"));
            middlePoints.append(" ");
        }
        editor.putString("points", middlePoints.toString());
        editor.putFloat("distance", (float)retdata.getDouble("distance"));
        editor.commit();
    }

    private void gotoAddPrice() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(PassWaitOnceOrderAcceptanceAgainActivity.this, PassUpPriceActivity.class);
        intent.putExtra("driverNum",driverNum);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
        startActivity(intent);
        finish();
    }

    private void gotoConfirm() {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(PassWaitOnceOrderAcceptanceAgainActivity.this, PassOnceOrderConfirmActivity.class);
		intent.putExtra("driverId" ,driverId);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
        startActivity(intent);
        finish();
    }

    private void timerViewRotate() {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.wait_rotation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            timerView.startAnimation(operatingAnim);
        }
    }

    private void gotoResult() {
        handler.removeCallbacksAndMessages(null);
        timerView.clearAnimation();
        Intent intent = new Intent(PassWaitOnceOrderAcceptanceAgainActivity.this, PassUpPriceActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
        startActivity(intent);
    }

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


    /*
     * 恢复应用时状态匹配
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQCODE_TEST)
            ;
    }



}

