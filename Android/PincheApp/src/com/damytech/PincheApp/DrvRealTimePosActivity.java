package com.damytech.PincheApp;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-21
 * Time: 上午12:34
 * To change this template use File | Settings | File Templates.
 */
public class DrvRealTimePosActivity extends SuperActivity
{
	private final int INTERVAL = 1000;

	private boolean isInit = false;
	private boolean hasPos = true;

	private long driverid = 0;
	private double curLat = 0;
	private double curLng = 0;

	private Overlay usrOverlay = null, drvOverlay = null;

	private ImageButton btnBack = null;

	Timer timer = null;
	TimerTask task = null;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		initBaidu();
		setContentView(R.layout.act_driver_pos);

		initControls();
		initResolution();
	}


	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();

		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				getDriverPos();
			}
		};

		timer.schedule(task, 0, INTERVAL);
	}


	@Override
	protected void onPause() {
		mapView.onPause();

		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}

		super.onPause();
	}


	@Override
	protected void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}


	private void initBaidu()
	{
		SDKInitializer.initialize(getApplicationContext());
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


	private void initControls()
	{
		driverid = getIntent().getLongExtra("driverid", 0);

		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mapView = (MapView)findViewById(R.id.mapview);
		baiduMap = mapView.getMap();
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
	}


	private void getDriverPos()
	{
		CommManager.getDriverPos(Global.loadUserID(getApplicationContext()), driverid, Global.getIMEI(getApplicationContext()), drvPosHandler);
	}

	private void refreshOverlays()
	{
		if (usrOverlay != null)
			usrOverlay.remove();

		if (drvOverlay != null)
			drvOverlay.remove();

		double psgLat = Global.loadLatitude(getApplicationContext());
		double psgLng = Global.loadLongitude(getApplicationContext());

		// Add start overlay
		OverlayOptions start_item = new MarkerOptions()
				.position(new LatLng(psgLat, psgLng))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.main_location));
		usrOverlay = baiduMap.addOverlay(start_item);

		// Add end overlay
		OverlayOptions end_item = new MarkerOptions()
				.position(new LatLng(curLat, curLng))
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.driver_mark));
		drvOverlay = baiduMap.addOverlay(end_item);

		// Find out min, max latitude and longitude
		double minLat = psgLat, maxLat = psgLat, minLng = psgLng, maxLng = psgLng;

		if (minLat > curLat)
			minLat = curLat;
		if (maxLat < curLat)
			maxLat = curLat;
		if (minLng > curLng)
			minLng = curLng;
		if (maxLng < curLng)
			maxLng = curLng;

		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(new LatLng(maxLat + (maxLat - minLat), maxLng + (maxLng - minLng)))
				.include(new LatLng(minLat - (maxLat - minLat), minLng - (maxLng - minLng)))
				.build();
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds);
		baiduMap.setMapStatus(update);
	}

	private AsyncHttpResponseHandler drvPosHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					curLat = retdata.getDouble("lat");
					curLng = retdata.getDouble("lng");

					updateUserPosition();
					refreshOverlays();

					hasPos = true;
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					if (hasPos)
					{
						Global.showAdvancedToast(DrvRealTimePosActivity.this, szRetMsg, Gravity.CENTER);
						hasPos = false;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
		}
	};


	private void updateUserPosition()
	{
		if (isInit)
			return;

		isInit = true;

		double lat = Global.loadLatitude(getApplicationContext());
		double lng = Global.loadLongitude(getApplicationContext());

		double maxLat = Math.max(lat, curLat);
		double minLat = Math.min(lat, curLat);

		double maxLng = Math.max(lng, curLng);
		double minLng = Math.min(lng, curLng);

		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(new LatLng(maxLat + (maxLat - minLat), maxLng + (maxLng - minLng)))
				.include(new LatLng(minLat - (maxLat - minLat), minLng - (maxLng - minLng)))
				.build();
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds);
		baiduMap.setMapStatus(update);
	}

}
