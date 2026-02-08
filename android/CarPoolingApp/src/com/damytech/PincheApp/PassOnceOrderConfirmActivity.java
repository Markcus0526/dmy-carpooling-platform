package com.damytech.PincheApp;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.MiddlePoint;
import com.damytech.DataClasses.STDetPassOrderInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 拼友主界面
 */
public class PassOnceOrderConfirmActivity extends SuperActivity
{
    /*
     *references part
     */
    //log tag
    private static final String TAG = "erik_debug";
    //static strings
    private static final String NUL_STR = "";
    //应用状态码
    private static final int CONFIRM_WITHOUT_MONEY = 0;
    //message condition code
    private static final int MESSAGE_TIMER = 0 ;
    //car type list
    private static final int NORMAL_CAR = 0;
    private static final int COMFORT_CAR = 1;
    private static final int LUXURY_CAR = 2;
    private static final int BUSSINESS_CAR = 3;
    //UI instances
    private Button confirmButton;
    private ConfirmPasswordDialog ticketInput;
    private Button cancelOrder;
    private TextView timerTxt;
    private ImageView timerView;
    //driver info ui
    private SmartImageView driverImage;
    private ImageView imageSex;
    private TextView ageView;
    private TextView driverName;
    private TextView driverDuration;
    private TextView driverEvaluation;
    private TextView serviceTime;
    private ImageView carType;
    private TextView carLogo;
    private TextView carName;
    private TextView carColor;
    private SmartImageView carImage;
    private String carImgUrl = "";
    //order detail ui
    private TextView addressDetail;
    private TextView addressPoints;
    private TextView relativeDistance;


	private boolean isAccepted = false;

    // Baidu variables
    private MapView mapView = null;
	private BaiduMap baiduMap = null;

    private Overlay startOverlay = null, endOverlay = null;
    private ArrayList<Overlay> arrMidOverlays = new ArrayList<Overlay>();
    ArrayList<PointF> arrMidpoints = new ArrayList<PointF>();
    private STDetPassOrderInfo stOrderInfo = new STDetPassOrderInfo();

    //others
    private long orderId;
    private int timeLimit;
    private int totalTime;
    private String ticket;
    private double money;
    private float cost;
    private long driverId;
    private SharedPreferences sharedPreferences;
    private boolean needShowFailureDialog = true;





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


        setContentView(R.layout.act_pass_order_confirm);

        initControls();//控件示例化
        initResolution();//适配屏幕

    }

    @Override
    protected void onResume() {

        //cancelOrder();
        super.onResume();
        //Log.d(TAG, "orderId------>"+orderId);
        getCostAndAccountDetail();
    }


    @Override
    protected void onPause()
    {
        //handler.removeMessages(MESSAGE_TIMER);
        needShowFailureDialog = false;
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
    private void initControls() {
        initBaidu();
        initMap();
        getOrderDetail();
        startTimer();
        confirmButton = (Button)findViewById(R.id.btn_send);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOnceOrder();
            }
        });
        cancelOrder = (Button)findViewById(R.id.btn_cancel_order);
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });

	    sharedPreferences = getSharedPreferences("single_order_detail", Context.MODE_PRIVATE);

	    initDriverInfo();
        initOrderInfo();
    }

    private void initOrderInfo() {
        addressDetail = (TextView)findViewById(R.id.address_detail);
        addressPoints = (TextView)findViewById(R.id.address_points);
        relativeDistance = (TextView)findViewById(R.id.relative_distance);
        addressDetail.setText(sharedPreferences.getString("start_addr", "")
                +" — "+sharedPreferences.getString("end_addr", ""));
        String middlePoints = sharedPreferences.getString("points", "");
        if(middlePoints.equals("")){
            addressPoints.setVisibility(View.GONE);
        }else{
            addressPoints.setText("中途点： "+ middlePoints);
        }
        relativeDistance.setText(String.format("距您： " + sharedPreferences.getFloat("distance", 0)) + "千米");
    }

    private void initDriverInfo() {
        driverImage = (SmartImageView)findViewById(R.id.img_photo);
        driverImage.isCircular = true;
	    driverImage.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
		    }
	    });

        driverImage.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
		        gotoDriveDetail();
	        }
        });
        imageSex = (ImageView)findViewById(R.id.imgSex);
        ageView = (TextView)findViewById(R.id.lblAge);
        driverName = (TextView)findViewById(R.id.driver_name);
        driverDuration = (TextView)findViewById(R.id.driver_duration); 
        driverEvaluation = (TextView)findViewById(R.id.driver_evaluation); 
        serviceTime =(TextView)findViewById(R.id.service_time_detail);
        carType = (ImageView)findViewById(R.id.car_type);
        carLogo = (TextView)findViewById(R.id.car_logo);
        carName =(TextView)findViewById(R.id.car_name);
        carColor =(TextView)findViewById(R.id.car_color);

        carImage = (SmartImageView)findViewById(R.id.img_car);
        carImage.isCircular = true;
	    carImage.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_car_img);
		    }
	    });
        carImgUrl = sharedPreferences.getString("carimg", "");
        carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassOnceOrderConfirmActivity.this, DisplayCarImgActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("img_url", carImgUrl);
                PassOnceOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

        setDriverUIInfo();
    }

    private void setDriverUIInfo() {
        driverImage.setImageUrl(sharedPreferences.getString("img", ""), R.drawable.default_user_img);
        if(0 == sharedPreferences.getInt("gender",-1)){
            imageSex.setImageResource(R.drawable.bk_manmark);
        } else {
            imageSex.setImageResource(R.drawable.bk_womanmark);
        }
        ageView.setText(""+sharedPreferences.getInt("age",0));
        driverName.setText(sharedPreferences.getString("name",""));
        driverDuration.setText("驾龄 "+sharedPreferences.getInt("drv_career",0)+"年");
        driverEvaluation.setText("好评率："+sharedPreferences.getInt("vgood_rate",0)+"%");
        serviceTime.setText(sharedPreferences.getInt("vgood_rate",0)+"次");
        chooseCarType(sharedPreferences.getInt("type", NORMAL_CAR));
        carLogo.setText(sharedPreferences.getString("brand", ""));
        carName.setText(sharedPreferences.getString("style", ""));
        carColor.setText(sharedPreferences.getString("color", ""));
        carImage.setImageUrl(sharedPreferences.getString("carimg", ""), R.drawable.default_car_img);

        refreshOverlays(stOrderInfo.start_lat, stOrderInfo.start_lng, stOrderInfo.end_lat, stOrderInfo.end_lng, arrMidpoints);
    }


//    private void getDetailedPassengerOrderInfo() {
//        startProgress();
//
//        CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
//                orderId, ConstData.ORDER_TYPE_ONCE, Global.getIMEI(getApplicationContext()), handlerDetail);
//    }
//
//
//    private AsyncHttpResponseHandler handlerDetail = new AsyncHttpResponseHandler()
//    {
//        @Override
//        public void onSuccess(String content) {
//            super.onSuccess(content);
//            stopProgress();
//
//            try {
//                JSONObject jsonObj = new JSONObject(content);
//                JSONObject result = jsonObj.getJSONObject("result");
//
//                int nRetcode = result.getInt("retcode");
//                String strMessaage = result.getString("retmsg");
//                if (nRetcode == ConstData.ERR_CODE_NONE)
//                {
//                    stOrderInfo = STDetPassOrderInfo.decodeFromJSON(result.getJSONObject("retdata"));
//
////                    if (stOrderInfo != null)
////                        RefreshPage();
//                }
//                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
//                {
//                    logout(strMessaage);
//                }
//                else
//                {
//                    Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, strMessaage, Gravity.CENTER);
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
////                Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, ex.getMessage(), Gravity.CENTER);
//            }
//        }
//
//        @Override
//        public void onFailure(Throwable error, String content) {
//            super.onFailure(error, content);
//            stopProgress();
//        }
//    };


    private void refreshOverlays(double startlat, double startlng, double endlat, double endlng, ArrayList<PointF> arrMidpoints)
    {
        if (startOverlay != null)
            startOverlay.remove();

        if (endOverlay != null)
            endOverlay.remove();

        for (int i = 0; i < arrMidOverlays.size(); i++)
            arrMidOverlays.get(i).remove();

        // Add start overlay
        OverlayOptions start_item = new MarkerOptions()
                .position(new LatLng(startlat, startlng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bk_startpos));
        startOverlay = baiduMap.addOverlay(start_item);

        // Add end overlay
        OverlayOptions end_item = new MarkerOptions()
                .position(new LatLng(endlat, endlng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bk_endpos));
        endOverlay = baiduMap.addOverlay(end_item);

        if (arrMidpoints.size() > 0)                // Add mid points overlay
        {
            BitmapDescriptor bmpMarker = BitmapDescriptorFactory.fromResource(R.drawable.bk_midpos);
            for (int i = 0; i < arrMidpoints.size(); i++)
            {
                PointF pt = arrMidpoints.get(i);
                OverlayOptions options = new MarkerOptions()
                        .position(new LatLng(pt.x, pt.y))
                        .icon(bmpMarker);
                arrMidOverlays.add(baiduMap.addOverlay(options));
            }
        }


        // Find out min, max latitude and longitude
        double minLat = startlat, maxLat = startlat, minLng = startlng, maxLng = startlng;

        if (minLat > endlat)
            minLat = endlat;
        if (maxLat < endlat)
            maxLat = endlat;
        if (minLng > endlng)
            minLng = endlng;
        if (maxLng < endlng)
            maxLng = endlng;

        for (int i = 0; i < arrMidpoints.size(); i++)
        {
            PointF pt = arrMidpoints.get(i);

            if (minLat > pt.x)
                minLat = pt.x;
            if (maxLat < pt.x)
                maxLat = pt.x;
            if (minLng > pt.y)
                minLng = pt.y;
            if (maxLng < pt.y)
                maxLng = pt.y;
        }

        // Baidu SDK has an error in this part. Must call twice
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(maxLat + (maxLat - minLat), maxLng + (maxLng - minLng)))
                .include(new LatLng(minLat - (maxLat - minLat), minLng - (maxLng - minLng)))
                .build();
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds);
        baiduMap.setMapStatus(update);
    }

    private void chooseCarType(int type) {
        switch (type){
            case NORMAL_CAR:
                carType.setBackgroundResource(R.drawable.first_car_green);
                break;
            case COMFORT_CAR:
                carType.setBackgroundResource(R.drawable.second_car_green);
                break;
            case LUXURY_CAR:
                carType.setBackgroundResource(R.drawable.third_car_green);
                break;
            case BUSSINESS_CAR:
                carType.setBackgroundResource(R.drawable.fourth_car_green);
                break;
            default:
                break;
        }
    }

    private void gotoDriveDetail() {
        Intent intent = new Intent(PassOnceOrderConfirmActivity.this, DrvEvalInfoActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("driverid", driverId);
        PassOnceOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    private void startTimer() {
        timerTxt = (TextView)findViewById(R.id.timer_text);
        timerView = (ImageView)findViewById(R.id.timer_image);
        timerViewRotate();
        Message message = handler.obtainMessage(MESSAGE_TIMER);     // Message
        handler.sendMessageDelayed(message, 1000);
    }

    private void timerViewRotate() {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.wait_rotation);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            timerView.startAnimation(operatingAnim);
        }
    }

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case MESSAGE_TIMER:
	                if (isAccepted)
		                return;

                    timeLimit--;
                    timerTxt.setText("" + timeLimit);
                    if(totalTime - timeLimit == 60){
                        Global.showAdvancedToast(PassOnceOrderConfirmActivity.this,"亲，尽快确认吧，车主等的花儿都谢了",Gravity.CENTER);
                    }
                    if(timeLimit > 0){
                        Message message = handler.obtainMessage(MESSAGE_TIMER);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                        cancelOrder();
                        // show fail dialog
                        showFailDialog(getString(R.string.STR_TIMEOUT_CONFIRM));
                    }
                    break;
               default:
                   break;
            }

            super.handleMessage(msg);
        }
    };

    private void cancelOrder() {
        //server communicate
        CommonAlertDialog dialog = new CommonAlertDialog.Builder(PassOnceOrderConfirmActivity.this)
                .message(getResources().getString(R.string.STR_ORDERCANCEL_MSG))
                .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommManager.cancelOnceOrder(Global.loadUserID(getApplicationContext()), orderId,
                                Global.getIMEI(getApplicationContext()), cancelOrderHandler);
                    }
                })
                .build();
        dialog.setCancelable(false);
        dialog.show();

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
                    Log.d(TAG,"success11.....................");
//                    JSONObject retdata = jsonResult.getJSONObject("retdata");
//                    SharedPreferences sharedPreferences = getSharedPreferences("single_order_detail", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//                    editor.putString("password", retdata.getString("password"));
//                    editor.commit();
                    handler.removeCallbacksAndMessages(null);

                    finishWithAnimation();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void showFailDialog(String szMsg)
    {
        Dialog dlgFail = new Dialog(PassOnceOrderConfirmActivity.this);
        dlgFail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgFail.setContentView(R.layout.dlg_driver_orderfail);
        dlgFail.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setResult(RESULT_OK);
                finishWithAnimation();
            }
        });

        RelativeLayout contRootView = (RelativeLayout)dlgFail.findViewById(R.id.parent_layout);
        TextView txtErrMsg = null;

        dlgFail.setCanceledOnTouchOutside(true);

        ResolutionSet.instance.iterateChild(contRootView, mScrSize.x, mScrSize.y);

        if (!szMsg.equals(""))
        {
            txtErrMsg = (TextView)dlgFail.findViewById(R.id.lblTitle);
            txtErrMsg.setText(szMsg);
        }

        dlgFail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(needShowFailureDialog) dlgFail.show();
    }


    private void getOrderDetail() {

        SharedPreferences sharedPreferences = getSharedPreferences("wait_time_list", Context.MODE_PRIVATE);
        timeLimit = sharedPreferences.getInt("driver_lock_time", 0);
        totalTime = timeLimit;
        //Log.d(TAG, "driver_lock_time -----> "+ timeLimit);
//        timeLimit = 200;//just for development
        orderId = sharedPreferences.getLong("order_id", 0);
        driverId = getIntent().getLongExtra("driverId", 0);
        sharedPreferences = getSharedPreferences("single_order_data", Context.MODE_PRIVATE);
        stOrderInfo.start_lat = sharedPreferences.getFloat("once_start_lat", 0);
        stOrderInfo.start_lng = sharedPreferences.getFloat("once_start_lng", 0);
        stOrderInfo.end_lat = sharedPreferences.getFloat("once_end_lat", 0);
        stOrderInfo.end_lng = sharedPreferences.getFloat("once_end_lng", 0);

        decodePointSting(sharedPreferences.getString("once_midPoints", ""));

    }

    private void decodePointSting(String pointSting){
        String[] points = pointSting.split(",");
        if(points.length == 1) return;
        PointF p = new PointF();
        for(int i=0; i<points.length; i++){
            if(i%3 == 0){
                p.x =(Float.parseFloat(points[i]));
            }else if (i%3 ==1){
                p.y =(Float.parseFloat(points[i]));
                arrMidpoints.add(p);
                p = new PointF();
            }
        }
    }

    private void getCostAndAccountDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("single_order_data", Context.MODE_PRIVATE);
        cost = sharedPreferences.getFloat("cost_detail", 0);

    }


	private void confirmOrder()
	{
		startProgress();
		CommManager.confirmOnceOrder(Global.loadUserID(getApplicationContext()),
				orderId,
				Global.getIMEI(getApplicationContext()),
				orderConfirmHandler);
	}



    private void confirmOnceOrder() {
        getMoney();
    }

    private void getMoney(){
        CommManager.getMoney(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), counterHandler);
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
                    Log.d(TAG, "nRetcode test ----->" + nRetcode);
                }

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONObject retdata = jsonResult.getJSONObject("retdata");
                    money = retdata.getDouble("money");
                    if(money < cost){
                        Intent intent = new Intent(PassOnceOrderConfirmActivity.this, PassNoCounterAgainActivity.class);
                        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                        intent.putExtra("order_id", orderId);
                        PassOnceOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                        startActivityForResult(intent, CONFIRM_WITHOUT_MONEY);
                    }else{
	                    confirmOrder();
                    }

                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelOrder();
            return true;
        }
		return super.onKeyDown(keyCode, event);    //To change body of overridden methods use File | Settings | File Templates.
	}

	private void setPassword() {
        ticketInput = new ConfirmPasswordDialog(this);
        ticketInput.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                ticket = ticketInput.getPassword();
	            startProgress();
                CommManager.setOnceOrderPassword(Global.loadUserID(getApplicationContext()),
		                orderId,
		                ticket,
		                Global.getIMEI(getApplicationContext()),
		                setPwdHandler);
            }
        });
		ticketInput.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		ticketInput.setCancelable(false);
        ticketInput.setCanceledOnTouchOutside(false);
        ticketInput.show();
    }


	private AsyncHttpResponseHandler setPwdHandler = new AsyncHttpResponseHandler()
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
					//Log.d(TAG, "detial---->" + retdata);
                    SharedPreferences sharedPreferences = getSharedPreferences("single_order_detail", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("start_time", retdata.getString("start_time"));
                    editor.putString("dist", retdata.getString("dist_desc"));
                    editor.putString("carno", retdata.getString("carno"));
                    editor.commit();
					//stop query
					handler.removeCallbacksAndMessages(null);
					gotoSuccess();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(jsonMsg);
				}
				else
				{
					Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};



    private AsyncHttpResponseHandler orderConfirmHandler = new AsyncHttpResponseHandler()
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
	                isAccepted = true;
	                setPassword();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        stopProgress();
	        Global.showAdvancedToast(PassOnceOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void gotoSuccess() {
        Intent intent = new Intent(PassOnceOrderConfirmActivity.this, PassOnceOrderSuccessActivity.class);
        intent.putExtra("passwords", ticket);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
        startActivity(intent);
        finish();
    }

    /*
    * 初始化baidu变量
    */
    private void initBaidu()
    {
	    SDKInitializer.initialize(getApplicationContext());
    }

    private void initMap() {
        mapView = (MapView)findViewById(R.id.img_map);
	    mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(13));

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

        if (requestCode == CONFIRM_WITHOUT_MONEY){
            confirmOrder();
        }

    }



}

