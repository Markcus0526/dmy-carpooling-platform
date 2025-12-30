package com.damytech.PincheApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STDetPassOrderInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PassOnOffOrderConfirmActivity extends SuperActivity
{
	long nOrderID = -1;

	private ImageButton btn_back = null;
	private SmartImageView imgPhoto = null;
	private SmartImageView imgCar = null;
	private RelativeLayout rlMapView = null;
	private Button btnCancel = null, btnOk = null;
    private ImageButton btnPhoto = null;
    private Button btnCar = null;

	TextView lblName = null;
	ImageView imgGender = null;
	TextView lblAge = null;
	TextView lblCarAge = null;
	TextView lblGoodEvalRatio = null;
	TextView lblCarPoolCount = null;
	TextView lblPath = null;
	TextView lblMidPoints = null;
	TextView []lblWeek = new TextView[7];
	ImageView imgType = null;
	TextView lblColor = null;
	TextView lblStyle = null;
	TextView lblStartTime = null;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	ArrayList<PointF> arrMidpoints = new ArrayList<PointF>();

	private Overlay startOverlay = null, endOverlay = null;
	private ArrayList<Overlay> arrMidOverlays = new ArrayList<Overlay>();

	String strPass = "";
	STDetPassOrderInfo stOrderInfo = new STDetPassOrderInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initBaidu();
        setContentView(R.layout.act_pass_normal_confirm);

        nOrderID = getIntent().getLongExtra("orderid", -1);

        initControls();
        initResolution();
    }

	private AsyncHttpResponseHandler handlerDetail = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					stOrderInfo = STDetPassOrderInfo.decodeFromJSON(result.getJSONObject("retdata"));

					if (stOrderInfo != null)
						RefreshPage();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(strMessaage);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, strMessaage, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);	//To change body of overridden methods use File | Settings | File Templates.
	}

	ConfirmPasswordDialog dlg = null;
	private AsyncHttpResponseHandler handlerConfirm = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					setPassword();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(strMessaage);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, strMessaage, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};

	private AsyncHttpResponseHandler handlerRefuse = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, getString(R.string.STR_SUCCESS), Gravity.CENTER);
                    Intent data = new Intent();
                    data.putExtra("orderid", nOrderID);
                    setResult(RESULT_OK, data);
					finishWithAnimation();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(strMessaage);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, strMessaage, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};


	private void initBaidu()
	{
		SDKInitializer.initialize(getApplicationContext());
	}

	private void initControls()
	{
		lblName = (TextView) findViewById(R.id.lblName);
		lblAge = (TextView) findViewById(R.id.lblAge);
		imgGender = (ImageView) findViewById(R.id.imgSex);
		lblCarAge = (TextView) findViewById(R.id.lblDriverYears);
		lblGoodEvalRatio = (TextView) findViewById(R.id.lblEval);
		lblCarPoolCount = (TextView) findViewById(R.id.lblHistoryCount);
		lblPath = (TextView) findViewById(R.id.lblPath);
		lblMidPoints = (TextView) findViewById(R.id.lblMidStation);
		lblWeek[0] = (TextView)findViewById(R.id.lblMon);
		lblWeek[1] = (TextView)findViewById(R.id.lblTue);
		lblWeek[2] = (TextView)findViewById(R.id.lblWed);
		lblWeek[3] = (TextView)findViewById(R.id.lblThr);
		lblWeek[4] = (TextView)findViewById(R.id.lblFri);
		lblWeek[5] = (TextView)findViewById(R.id.lblSat);
		lblWeek[6] = (TextView)findViewById(R.id.lblSun);
		lblColor = (TextView) findViewById(R.id.lblColor);
		lblStyle = (TextView) findViewById(R.id.lblBrandName);
		imgType = (ImageView) findViewById(R.id.imgCarType);
		lblStartTime = (TextView) findViewById(R.id.lblTime);

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		imgPhoto = (SmartImageView) findViewById(R.id.viewPhoto);
		imgPhoto.isCircular = true;
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
			}
		});
        btnPhoto = (ImageButton)findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDriver();
            }
        });

        imgCar = (SmartImageView) findViewById(R.id.viewCar);
        imgCar.isCircular = true;
        imgCar.setImage(new SmartImage() {
            @Override
            public Bitmap getBitmap(Context context) {
                return BitmapFactory.decodeResource(getResources(), R.drawable.default_car_img);
            }
        });
        btnCar = (Button)findViewById(R.id.btnCar);
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCar();
            }
        });

		rlMapView = (RelativeLayout) findViewById(R.id.rlMapView);
		rlMapView.setOnClickListener(onClickListener);

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(onClickListener);
		btnOk = (Button) findViewById(R.id.btnOrder);
		btnOk.setOnClickListener(onClickListener);

		mapView = (MapView) findViewById(R.id.viewMap);
		mapView.showZoomControls(false);
		baiduMap = mapView.getMap();
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId())
			{
				case R.id.rlMapView:
					break;
				case R.id.btnCancel:
					startProgress();
					CommManager.refuseOnOffOrder(Global.loadUserID(getApplicationContext()),
							nOrderID,
							Global.getIMEI(getApplicationContext()),
							handlerRefuse);
					break;
				case R.id.btnOrder:
					confirmOrder();
					break;
			}
		}
	};

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
        Intent data = new Intent();
        data.putExtra("orderid", nOrderID);
        setResult(RESULT_OK, data);
		finishWithAnimation();
	}

	@Override
	public void onResume()
	{
		mapView.onResume();
		super.onResume();

		startProgress();
		CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
				nOrderID,
				ConstData.ORDER_TYPE_ONOFF,
				Global.getIMEI(getApplicationContext()),
				handlerDetail);
	}

	@Override
	protected void onPause()
	{
		mapView.onPause();
		super.onPause();
	}

	@Override
	public void onStop()
	{
		super.onStop();

		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	@Override
	protected void onDestroy()
	{
		mapView.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void RefreshPage()
	{
		if (stOrderInfo != null)
		{
			lblName.setText(stOrderInfo.driver_info.name);
			if (stOrderInfo.driver_info.gender == 0)
				imgGender.setImageResource(R.drawable.bk_manmark);
			else
				imgGender.setImageResource(R.drawable.bk_womanmark);
			lblAge.setText(Integer.toString(stOrderInfo.driver_info.age));
			lblCarAge.setText(stOrderInfo.driver_info.drv_career_desc);
			lblGoodEvalRatio.setText(getString(R.string.STR_HAOPINGLV) + stOrderInfo.driver_info.evgood_rate_desc);
			lblCarPoolCount.setText(getString(R.string.STR_PASSNORMALCONFIRM_FUWUCISHU) + " ："  + stOrderInfo.driver_info.carpool_count_desc);
			imgPhoto.setImageUrl(stOrderInfo.driver_info.image, R.drawable.default_user_img);
			imgCar.setImageUrl(stOrderInfo.driver_info.carimg, R.drawable.default_car_img);
			lblColor.setText(stOrderInfo.driver_info.color);
			lblStyle.setText(stOrderInfo.driver_info.style);
			switch (stOrderInfo.driver_info.type)
			{
				case 1:
					imgType.setImageResource(R.drawable.econcar_sel);
					break;
				case 2:
					imgType.setImageResource(R.drawable.safecar_sel);
					break;
				case 3:
					imgType.setImageResource(R.drawable.luxurycar_sel);
					break;
				case 4:
					imgType.setImageResource(R.drawable.businesscar_sel);
					break;
			}

			lblPath.setText(stOrderInfo.start_addr + getResources().getString(R.string.addr_separator) + stOrderInfo.end_addr);
			WeekItemShow(stOrderInfo.days, stOrderInfo.leftdays);
			if (stOrderInfo.mid_points != null && stOrderInfo.mid_points.size() > 0)
			{
				String strMidPointName = "";
				for (int i = 0; i < stOrderInfo.mid_points.size(); i++)
				{
					if ( i == 0 )
						strMidPointName = stOrderInfo.mid_points.get(i).address;
					else
						strMidPointName = ", " + stOrderInfo.mid_points.get(i).address;
				}

				lblMidPoints.setText(getString(R.string.STR_ZHONGTUDIAN) + strMidPointName);
			};
			lblStartTime.setText(stOrderInfo.start_time);

			if (stOrderInfo.mid_points != null)
			{
				for (int n = 0; n < stOrderInfo.mid_points.size(); n++)
				{
					arrMidpoints.add(new PointF((float)stOrderInfo.mid_points.get(n).lat, (float)stOrderInfo.mid_points.get(n).lng));
				}
			}

			refreshOverlays(stOrderInfo.start_lat, stOrderInfo.start_lng, stOrderInfo.end_lat, stOrderInfo.end_lng, arrMidpoints);
		}

		return;
	}

	private void WeekItemShow(String strValidDays, String strLeftDays)
	{
		for (int i = 0; i < 7; i++)
		{
			lblWeek[i].setBackgroundResource(R.drawable.bk_normday);
			lblWeek[i].setTextColor(getResources().getColor(R.color.TABCOLOR));
		}

		if (strLeftDays != null)
		{
			for (int i = 0; i < 7; i++)
			{
				String strVal = Integer.toString(i);

				if ( strLeftDays.contains(strVal) == true )
				{
					lblWeek[i].setBackgroundResource(R.drawable.bk_diselday);
					lblWeek[i].setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		}

		if (strValidDays != null)
		{
			for (int i = 0; i < 7; i++)
			{
				String strVal = Integer.toString(i);

				if ( strValidDays.contains(strVal) == true )
				{
					lblWeek[i].setBackgroundColor(getResources().getColor(R.color.TABCOLOR));
					lblWeek[i].setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		}

		return;
	}


	private void confirmOrder()
	{
		startProgress();
		CommManager.confirmOnOffOrder(
				Global.loadUserID(getApplicationContext()),
				nOrderID,
				Global.getIMEI(getApplicationContext()),
				handlerConfirm);
	}


	private void setPassword()
	{
		dlg = new ConfirmPasswordDialog(PassOnOffOrderConfirmActivity.this);
		dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				strPass = dlg.getPassword();

				if (strPass != null && strPass.length()  == 4)
				{
					startProgress();
					CommManager.setOnoffOrderPassword(
							Global.loadUserID(getApplicationContext()),
							nOrderID,
							strPass,
							Global.getIMEI(getApplicationContext()),
							setPwd_handler);
				}
			}
		});
		dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
		dlg.show();
	}


	private AsyncHttpResponseHandler setPwd_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					long drv_id = retdata.getLong("drv_id");
					String drv_name = retdata.getString("drv_name");
					String drv_img = retdata.getString("drv_img");
					int drv_age = retdata.getInt("drv_age");
					int drv_gender = retdata.getInt("drv_gender");
					String car_img = retdata.getString("car_img");
					int car_style = retdata.getInt("car_style");
					String car_brand = retdata.getString("car_brand");
					String car_type = retdata.getString("car_type");
					String car_color = retdata.getString("car_color");
					String carno = retdata.getString("carno");
					String start_time = retdata.getString("start_time");
					String start_addr = retdata.getString("start_addr");
					String end_addr = retdata.getString("end_addr");
					String password = retdata.getString("password");
					String days = retdata.getString("days");
					String leftdays = retdata.getString("leftdays");
					String midpoints = retdata.getString("midpoints");

					Intent intent = new Intent(PassOnOffOrderConfirmActivity.this, PassOnOffOrderSuccessActivity.class);

					intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

					intent.putExtra("password", strPass);
					intent.putExtra("orderid", nOrderID);
					intent.putExtra("order_type", ConstData.ORDER_TYPE_ONOFF);

					intent.putExtra("drv_id", drv_id);
					intent.putExtra("drv_name", drv_name);
					intent.putExtra("drv_img", drv_img);
					intent.putExtra("drv_age", drv_age);
					intent.putExtra("drv_gender", drv_gender);
					intent.putExtra("car_img", car_img);
					intent.putExtra("car_style", car_style);
					intent.putExtra("car_brand", car_brand);
					intent.putExtra("car_type", car_type);
					intent.putExtra("car_color", car_color);
					intent.putExtra("carno", carno);
					intent.putExtra("start_time", start_time);
					intent.putExtra("start_addr", start_addr);
					intent.putExtra("end_addr", end_addr);
					intent.putExtra("password", password);
					intent.putExtra("days", days);
					intent.putExtra("leftdays", leftdays);
					intent.putExtra("midpoints", midpoints);

					PassOnOffOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					startActivity(intent);

                    Intent data = new Intent();
                    data.putExtra("orderid", nOrderID);
                    setResult(RESULT_OK, data);
					finish();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(strMessaage);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, strMessaage, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderConfirmActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};


    private void selectDriver()
    {
        Intent intent = new Intent(PassOnOffOrderConfirmActivity.this, DrvEvalInfoActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("driverid", stOrderInfo.driver_info.uid);
        PassOnOffOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    private void selectCar()
    {
        Intent intent = new Intent(PassOnOffOrderConfirmActivity.this, DisplayCarImgActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("img_url", stOrderInfo.driver_info.carimg);
        PassOnOffOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }
}

