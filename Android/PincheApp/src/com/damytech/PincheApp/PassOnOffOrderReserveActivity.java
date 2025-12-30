package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
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


/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-22
 * Time: 上午9:54
 * To change this template use File | Settings | File Templates.
 */
public class PassOnOffOrderReserveActivity extends SuperActivity
{
	long nOrderID = -1;

	private ImageButton btn_back = null;
	private SmartImageView imgPhoto = null;
	private SmartImageView imgCar = null;
	private RelativeLayout rlMapView = null;
	private Button btnOk = null;

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
	ImageView imgBrand = null;
	TextView txtBrand = null;
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
				String szRetMsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					stOrderInfo = STDetPassOrderInfo.decodeFromJSON(result.getJSONObject("retdata"));

					if (stOrderInfo != null)
						RefreshPage();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderReserveActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderReserveActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initBaidu();
		setContentView(R.layout.act_pass_onoffreserve);

		nOrderID = getIntent().getLongExtra("orderid", 0);

		initControls();
		initResolution();
	}

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
		imgBrand = (ImageView)findViewById(R.id.imgBrand);
		txtBrand = (TextView)findViewById(R.id.txtBrand);
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

		imgCar = (SmartImageView) findViewById(R.id.viewCar);
		imgCar.isCircular = true;
		imgCar.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.default_car_img);
			}
		});
        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassOnOffOrderReserveActivity.this, DisplayCarImgActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("img_url", stOrderInfo.driver_info.carimg);
                PassOnOffOrderReserveActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

		rlMapView = (RelativeLayout) findViewById(R.id.rlMapView);
		rlMapView.setOnClickListener(onClickListener);
		btnOk = (Button) findViewById(R.id.btnOrder);
		btnOk.setOnClickListener(onClickListener);

		mapView = (MapView) findViewById(R.id.viewMap);
		mapView.showZoomControls(false);

		baiduMap = mapView.getMap();
		baiduMap.setTrafficEnabled(false);
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId())
			{
				case R.id.rlMapView:
					moveToMapActivity();
					break;
				case R.id.btnOrder:
					reserveNextOrder();
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

			int nBrandID = Global.getBrandImgFromName(stOrderInfo.driver_info.brand);
			if (nBrandID > 0)
			{
				imgBrand.setImageResource(nBrandID);
				imgBrand.setVisibility(View.VISIBLE);
				txtBrand.setVisibility(View.GONE);
			}
			else
			{
				txtBrand.setText(stOrderInfo.driver_info.brand);
				imgBrand.setVisibility(View.GONE);
				txtBrand.setVisibility(View.VISIBLE);
			}

			lblPath.setText(stOrderInfo.start_addr + " - " + stOrderInfo.end_addr);
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


	private void reserveNextOrder()
	{
		startProgress();
		CommManager.reserveNextOnOffOrder(Global.loadUserID(getApplicationContext()), stOrderInfo.uid, stOrderInfo.password, Global.getIMEI(getApplicationContext()), reserve_next_handler);
	}


	private void moveToMapActivity()
	{
		// Move to map activity
		Intent intent = new Intent(PassOnOffOrderReserveActivity.this, OrderRouteActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("startlat", stOrderInfo.start_lat);
		intent.putExtra("startlng", stOrderInfo.start_lng);
		intent.putExtra("endlat", stOrderInfo.end_lat);
		intent.putExtra("endlng", stOrderInfo.end_lat);

		String szMidPoints = "";
		for (int i = 0; i < arrMidpoints.size(); i++)
		{
			if (!szMidPoints.equals(""))
				szMidPoints += ",";
			szMidPoints += arrMidpoints.get(i).x + "," + arrMidpoints.get(i).y;
		}

		intent.putExtra("midpoints", szMidPoints);

		PassOnOffOrderReserveActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}



	private AsyncHttpResponseHandler reserve_next_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(PassOnOffOrderReserveActivity.this, getResources().getString(R.string.STR_SUCCESS), Gravity.CENTER);

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderReserveActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
		}
	};

}
