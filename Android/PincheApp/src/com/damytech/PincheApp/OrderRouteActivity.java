package com.damytech.PincheApp;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-16
 * Time: 上午9:52
 * To change this template use File | Settings | File Templates.
 */
public class OrderRouteActivity extends SuperActivity
{
	private boolean isInit = false;
	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	public MyLocationListener myListener = new MyLocationListener();
	public LocationClient mLocClient = null;

	private ImageButton btnBack = null;

	double startlat = 0, startlng = 0, endlat = 0, endlng = 0;
	ArrayList<PointF> arrMidpoints = new ArrayList<PointF>();

	private Overlay startOverlay = null, endOverlay = null;
	private ArrayList<Overlay> arrMidOverlays = new ArrayList<Overlay>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.

		initBaidu();

		setContentView(R.layout.act_order_route);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});

		mapView = (MapView)findViewById(R.id.mapview);
		mapView.showZoomControls(false);
		baiduMap = mapView.getMap();
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
		baiduMap.setMyLocationEnabled(true);

		// extra information
		startlat = getIntent().getDoubleExtra("startlat", 0);
		startlng = getIntent().getDoubleExtra("startlng", 0);
		endlat = getIntent().getDoubleExtra("endlat", 0);
		endlng = getIntent().getDoubleExtra("endlng", 0);

		String szMidPoints = getIntent().getStringExtra("midpoints");

		String[] arrValues = szMidPoints.split(",");
		arrMidpoints = new ArrayList<PointF>();
		for (int i = 0; i < arrValues.length / 2; i++)
		{
			try {
				PointF pt = new PointF(Float.parseFloat(arrValues[i * 2]), Float.parseFloat(arrValues[i * 2 + 1]));
				arrMidpoints.add(pt);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				refreshOverlays(startlat, startlng, endlat, endlng, arrMidpoints);
			}
		};

		Timer timer = new Timer();
		timer.schedule(task, 500);
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

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		mapView.onDestroy();

		super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	protected void onPause() {
		mapView.onPause();
		mLocClient.stop();

		super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	protected void onResume()
	{
		mapView.onResume();
		mLocClient.start();

		super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
	}

	private void initBaidu()
	{
		SDKInitializer.initialize(getApplicationContext());
		initLocationManager();
	}

	private void initLocationManager()
	{
		mLocClient = new LocationClient(OrderRouteActivity.this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);

		mLocClient.setLocOption(option);
	}


	/************************************** Map Relation *********************************/

	private class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || location.getAddrStr() == null)
				return;

			double lng = location.getLongitude();
			double lat = location.getLatitude();

			String szAddr = location.getAddrStr();
			String szCity = location.getCity();

			Global.saveCoordinates(getApplicationContext(), lat, lng);
			Global.saveCityName(getApplicationContext(), szCity);
			Global.saveDetAddress(getApplicationContext(), szAddr);

			if (!isInit) {
				MyLocationData locData = new MyLocationData.Builder().latitude(lat).longitude(lng).accuracy(location.getRadius()).direction(location.getDirection()).build();
				baiduMap.setMyLocationData(locData);
				isInit = true;
			}
		}
	}


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
}
