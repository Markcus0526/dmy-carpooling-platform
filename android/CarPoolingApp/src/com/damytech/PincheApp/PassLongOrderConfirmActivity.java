package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.damytech.DataClasses.*;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼友主界面
 */
public class PassLongOrderConfirmActivity extends SuperActivity
{
	private long mOrderId;
	private int mGrabSeatCount = 1;

	ConfirmPasswordDialog dlgPwd = null;
	private String szPwd = "";

	private STDetPassOrderInfo mDetOrderInfo = new STDetPassOrderInfo();

	private ImageButton btn_back = null;
	private SmartImageView imgPhoto = null;
	private ImageButton btnPhoto;
	private SmartImageView imgCar = null;
	private RelativeLayout rlMapView = null;
	private TextView lblAge = null;
	private ImageView imgGender = null;
	private TextView lblName = null;
	private TextView lblDriverYears = null;
	private TextView lblEval = null;
	private TextView lblHistoryCount = null;
	private ImageView imgCarType = null;
	private ImageView imgCarBrand = null;
	private TextView lblCarName = null;
	private TextView lblCarColor = null;
	private TextView lblCityPath = null;
	private TextView lblAddrPath = null;
	private TextView lblTime = null;
	private TextView lblSeats = null;
	private TextView lblPrice = null;
	private Button btnPriceSeat = null;
	private RelativeLayout seatitem_layout = null;
	private ListView listPriceSeats = null;
	private ArrayList<String> arrPriceSeats = new ArrayList<String>();
	private SeatItemAdapter seatItemAdapter = null;
	private Button btnGrab = null;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private LocationClient mLocClient = null;
	private MyLocationListenner myListener = new MyLocationListenner();

	private Overlay startOverlay = null, endOverlay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		initBaidu();
		setContentView(R.layout.act_pass_long_confirm);

		initControls();//控件示例化
		initResolution();//适配屏幕

		callGetOrderDetailInfo();
	}

	private void initBaidu()
	{
		SDKInitializer.initialize(getApplicationContext());
		initLocationManager();
	}

	private void initLocationManager()
	{
		mLocClient = new LocationClient(PassLongOrderConfirmActivity.this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd0911");
		option.setScanSpan(3000);
		mLocClient.setLocOption(option);
	}

	public class MyLocationListenner implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null || location.getAddrStr() == null)
				return;

			double lng = location.getLongitude();
			double lat = location.getLatitude();

			String szAddr = location.getAddrStr();
			String szCity = location.getCity();

			Global.saveCoordinates(getApplicationContext(), lat, lng);
			Global.saveCityName(getApplicationContext(), szCity);
			Global.saveDetAddress(getApplicationContext(), szAddr);
		}
	}


	@Override
	public void onResume()
	{
		mapView.onResume();
		mLocClient.start();

		super.onResume();
	}

	@Override
	protected void onPause()
	{
		mapView.onPause();
		mLocClient.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		mLocClient.stop();
		mapView.onDestroy();
		super.onDestroy();
	}

	private void initControls()
	{
		mOrderId = getIntent().getLongExtra("orderid", 0);

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
		btnPhoto.setOnClickListener(new View.OnClickListener()
		{
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
        imgCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassLongOrderConfirmActivity.this, DisplayCarImgActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("img_url", mDetOrderInfo.driver_info.carimg);
                PassLongOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

		lblAge = (TextView)findViewById(R.id.lblAge);
		imgGender = (ImageView)findViewById(R.id.imgSex);
		lblName = (TextView)findViewById(R.id.lblName);
		lblAge = (TextView)findViewById(R.id.lblAge);
		lblDriverYears = (TextView)findViewById(R.id.lblDriverYears);
		lblEval = (TextView)findViewById(R.id.lblEval);
		lblHistoryCount = (TextView)findViewById(R.id.lblHistoryCount);
		imgCarType = (ImageView)findViewById(R.id.imgCarType);
		imgCarBrand = (ImageView)findViewById(R.id.imgBrand);
		lblCarName = (TextView)findViewById(R.id.lblBrandName);
		lblCarColor = (TextView)findViewById(R.id.lblColor);
		lblCityPath = (TextView)findViewById(R.id.lblCityPath);
		lblAddrPath = (TextView)findViewById(R.id.lblAddrPath);
		lblTime = (TextView)findViewById(R.id.lblTime);
		lblSeats = (TextView)findViewById(R.id.lblSeats);
		lblPrice = (TextView)findViewById(R.id.lblPrice);
		btnPriceSeat = (Button)findViewById(R.id.btnPriceSeat);
		btnPriceSeat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSeatList();
			}
		});
		seatitem_layout = (RelativeLayout)findViewById(R.id.relativeLayout2);
		seatitem_layout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				hideSeatList();
			}
		});
		seatitem_layout.setVisibility(View.GONE);


		listPriceSeats = (ListView)findViewById(R.id.listPriceSeats);
		listPriceSeats.setDividerHeight(0);
		seatItemAdapter = new SeatItemAdapter(PassLongOrderConfirmActivity.this, R.id.listPriceSeats, arrPriceSeats);
		listPriceSeats.setAdapter(seatItemAdapter);
		listPriceSeats.setDivider(new ColorDrawable(Color.WHITE));
		listPriceSeats.setCacheColorHint(Color.WHITE);


		btnGrab = (Button)findViewById(R.id.btnGrab);
		btnGrab.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				acceptLongOrder();
			}
		});


		rlMapView = (RelativeLayout) findViewById(R.id.rlMapView);
		rlMapView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickMapCover();
			}
		});

		mapView = (MapView) findViewById(R.id.viewMap);
		mapView.showZoomControls(false);
		baiduMap = mapView.getMap();
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
	}

	private void onClickBack()
	{
		finishWithAnimation();
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

	private void UpdateUI()
	{
		// set ui control info
		imgPhoto.setImageUrl(mDetOrderInfo.driver_info.image, R.drawable.default_user_img);
		imgCar.setImageUrl(mDetOrderInfo.driver_info.carimg, R.drawable.default_car_img);
		imgGender.setImageResource(mDetOrderInfo.driver_info.gender == 0 ? R.drawable.bk_manmark : R.drawable.bk_womanmark);
		lblAge.setText(mDetOrderInfo.driver_info.age + "");
		lblName.setText(mDetOrderInfo.driver_info.name);
		lblDriverYears.setText(mDetOrderInfo.driver_info.drv_career_desc);
		lblEval.setText(mDetOrderInfo.driver_info.evgood_rate_desc);
		lblHistoryCount.setText(mDetOrderInfo.driver_info.carpool_count + getResources().getString(R.string.STR_PASSENGERINFO_CI));
		switch (Integer.parseInt(mDetOrderInfo.driver_info.style))
		{
			case 1:
				imgCarType.setImageResource(R.drawable.econcar_sel);
				break;
			case 2:
				imgCarType.setImageResource(R.drawable.safecar_sel);
				break;
			case 3:
				imgCarType.setImageResource(R.drawable.luxurycar_sel);
				break;
			case 4:
				imgCarType.setImageResource(R.drawable.businesscar_sel);
				break;
		}
		lblCarColor.setText(mDetOrderInfo.driver_info.color);
		lblCarName.setText(mDetOrderInfo.driver_info.type + "");
		lblCityPath.setText(mDetOrderInfo.start_city + getResources().getString(R.string.addr_separator) + mDetOrderInfo.end_city);
		lblAddrPath.setText(mDetOrderInfo.start_addr + getResources().getString(R.string.addr_separator) + mDetOrderInfo.end_addr);
		lblTime.setText(getResources().getString(R.string.STR_YUYUECHUFASHIJIAN) + mDetOrderInfo.start_time);
		lblSeats.setText(mDetOrderInfo.price_desc + "  " +
			getResources().getString(R.string.STR_LONGDISTANCEORDER_SHENGYU) +
			mDetOrderInfo.left_seats +
			getResources().getString(R.string.STR_LONGDISTANCE_ZUO));
		lblPrice.setText(getResources().getString(R.string.STR_LONGDISTANCE_QIANG) + "1" +
			getResources().getString(R.string.STR_LONGDISTANCE_ZUO) +
			mDetOrderInfo.price +
			getResources().getString(R.string.STR_BALANCE_DIAN));

		refreshOverlays(mDetOrderInfo.start_lat,
				mDetOrderInfo.start_lng,
				mDetOrderInfo.end_lat,
				mDetOrderInfo.end_lng);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void callGetOrderDetailInfo()
	{
		startProgress();

		CommManager.getAcceptableLongOrderDetailInfo(Global.loadUserID(getApplicationContext()), mOrderId, ConstData.ORDER_TYPE_LONG,
				Global.getIMEI(getApplicationContext()), detPassOrderInfoHandler);
	}


	private AsyncHttpResponseHandler detPassOrderInfoHandler = new AsyncHttpResponseHandler()
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

                    JSONObject dataJsonObj = result.getJSONObject("retdata");
                    try { mDetOrderInfo.price = dataJsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); mDetOrderInfo.price = 0; }
                    try { mDetOrderInfo.price_desc = dataJsonObj.getString("price_desc"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.left_seats = dataJsonObj.getInt("left_seats"); } catch (Exception ex) { ex.printStackTrace(); mDetOrderInfo.left_seats = 0; }
                    try { mDetOrderInfo.start_city = dataJsonObj.getString("start_city"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.end_city = dataJsonObj.getString("end_city"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.start_addr = dataJsonObj.getString("start_addr"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.start_lat = dataJsonObj.getDouble("start_lat"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.start_lng = dataJsonObj.getDouble("start_lng"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.end_addr = dataJsonObj.getString("end_addr"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.end_lat = dataJsonObj.getDouble("end_lat"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.end_lng = dataJsonObj.getDouble("end_lng"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.leftdays = dataJsonObj.getString("leftdays"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { mDetOrderInfo.days = dataJsonObj.getString("days"); } catch (Exception ex) { ex.printStackTrace(); }
					try { mDetOrderInfo.start_time = dataJsonObj.getString("start_time"); } catch (Exception ex) { ex.printStackTrace(); }

                    JSONObject drvlist = dataJsonObj.getJSONObject("driver_info");
                    STDriverInfo driverInfo = new STDriverInfo();
                    try { driverInfo.uid = drvlist.getLong("driver_id"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.image = drvlist.getString("driver_img"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.name = drvlist.getString("driver_name"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.gender = drvlist.getInt("driver_gender"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.age = drvlist.getInt("driver_age"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.carimg = drvlist.getString("car_img"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.drv_career = drvlist.getInt("drv_career"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.drv_career_desc = drvlist.getString("drv_career_desc"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.evgood_rate = drvlist.getInt("evgood_rate"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.evgood_rate_desc = drvlist.getString("evgood_rate_desc"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.carpool_count = drvlist.getInt("carpool_count"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.carpool_count_desc = drvlist.getString("carpool_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.brand = drvlist.getString("brand"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.style = drvlist.getString("style"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.type = drvlist.getInt("type"); } catch (Exception ex) { ex.printStackTrace(); }
                    try { driverInfo.color = drvlist.getString("car_color"); } catch (Exception ex) { ex.printStackTrace(); }

                    mDetOrderInfo.driver_info = driverInfo;

					// udpate ui using detail order information
					UpdateUI();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderConfirmActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void showSeatList()
	{
		arrPriceSeats.clear();
		for(int i = 0; i < mDetOrderInfo.left_seats; i++) {
			arrPriceSeats.add(getResources().getString(R.string.STR_LONGDISTANCE_QIANG)
					+ (i + 1) + getResources().getString(R.string.STR_LONGDISTANCE_ZUO) + " "
					+ (i + 1) * mDetOrderInfo.price + getResources().getString(R.string.STR_BALANCE_DIAN));
		}
		seatitem_layout.setVisibility(View.VISIBLE);
	}

	private void hideSeatList()
	{
		seatitem_layout.setVisibility(View.GONE);
	}

	private void seatSelected(int count)
	{
		mGrabSeatCount = count;
		lblPrice.setText(getResources().getString(R.string.STR_LONGDISTANCE_QIANG) + mGrabSeatCount +
				getResources().getString(R.string.STR_LONGDISTANCE_ZUO) +
				mGrabSeatCount * mDetOrderInfo.price +
				getResources().getString(R.string.STR_BALANCE_DIAN));
	}

	private void selectDriver()
	{
		Intent intent = new Intent(PassLongOrderConfirmActivity.this, DrvEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("driverid", mDetOrderInfo.driver_info.uid);
		PassLongOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void refreshOverlays(double startlat, double startlng, double endlat, double endlng)
	{
		if (startOverlay != null)
			startOverlay.remove();

		if (endOverlay != null)
			endOverlay.remove();

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

		// Baidu SDK has an error in this part. Must call twice
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(new LatLng(maxLat + (maxLat - minLat), maxLng + (maxLng - minLng)))
				.include(new LatLng(minLat - (maxLat - minLat), minLng - (maxLng - minLng)))
				.build();
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(bounds);
		baiduMap.setMapStatus(update);
	}

	private void onClickMapCover()
	{
		// Move to map activity
		Intent intent = new Intent(PassLongOrderConfirmActivity.this, OrderRouteActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("startlat", mDetOrderInfo.start_lat);
		intent.putExtra("startlng", mDetOrderInfo.start_lng);
		intent.putExtra("endlat", mDetOrderInfo.end_lat);
		intent.putExtra("endlng", mDetOrderInfo.end_lng);
		intent.putExtra("midpoints", "");

		PassLongOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void acceptLongOrder()
	{
		startProgress();
		CommManager.acceptLongOrder(Global.loadUserID(getApplicationContext()),
				mOrderId,
				mGrabSeatCount,
				Global.getIMEI(getApplicationContext()),
				accept_handler);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (seatitem_layout.isShown()) {
				hideSeatList();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);	//To change body of overridden methods use File | Settings | File Templates.
	}

	private class STSeatItemViewHolder
	{
		TextView lblSeatCount = null;
		Button btnEmpty = null;
	}

	public class SeatItemAdapter extends ArrayAdapter<String>
	{
		Context ctx;
		ArrayList<String> list = new ArrayList<String>();

		public SeatItemAdapter(Context ctx, int resourceId, ArrayList<String> list) {
			super(ctx, resourceId, list);
			this.ctx = ctx;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String seat = list.get(position);

			View v = convertView;
			STSeatItemViewHolder holder = null;
			if (v == null)
			{
				LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.view_pass_longorder_seatitem, null);
				Point point = Global.getScreenSize(PassLongOrderConfirmActivity.this);
				ResolutionSet.instance.iterateChild(v, point.x, point.y);

				holder = new STSeatItemViewHolder();
				v.setTag(holder);
			}
			else
			{
				holder = (STSeatItemViewHolder)v.getTag();
			}

			TextView lblSeatCount = null;
			if (holder.lblSeatCount == null)
				holder.lblSeatCount = (TextView) v.findViewById(R.id.lblSeatCount);
			lblSeatCount = holder.lblSeatCount;
			lblSeatCount.setText(seat);

			Button btnEmpty = null;
			if (holder.btnEmpty == null)
				holder.btnEmpty = (Button)v.findViewById(R.id.btn_empty);
			btnEmpty = holder.btnEmpty;
			btnEmpty.setTag(position + 1);
			btnEmpty.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					seatSelected((Integer)v.getTag());
					hideSeatList();
				}
			});

			return v;
		}
	}


	private void setPassword()
	{
		dlgPwd = new ConfirmPasswordDialog(PassLongOrderConfirmActivity.this);
		dlgPwd.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				szPwd = dlgPwd.getPassword();
				startProgress();
				CommManager.setLongOrderPassword(Global.loadUserID(getApplicationContext()),
						mOrderId,
						szPwd,
						Global.getIMEI(getApplicationContext()),
						setPwd_handler);
			}
		});
		dlgPwd.setCancelable(false);
        dlgPwd.setCanceledOnTouchOutside(false);
		dlgPwd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgPwd.show();
	}


	private AsyncHttpResponseHandler accept_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					setPassword();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else if (nRetCode == ConstData.ERR_CODE_BALNOTENOUGH)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					double rembal = retdata.getDouble("rembal");
					double total_fee = retdata.getDouble("total_fee");
					showChargeDialog(rembal, total_fee);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderConfirmActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler setPwd_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassLongOrderConfirmActivity.this, getResources().getString(R.string.STR_SUCCESS), Gravity.CENTER);

					JSONObject retdata = result.getJSONObject("retdata");
					{
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
						String password = szPwd;
						String drv_career_desc = retdata.getString("drv_career_desc");
						String evgood_rate_desc = retdata.getString("evgood_rate_desc");
						String carpool_count_desc = retdata.getString("carpool_count_desc");

						Intent intent = new Intent(PassLongOrderConfirmActivity.this, PassLongOrderSuccessActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
						intent.putExtra("orderid", mOrderId);
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
						intent.putExtra("drv_career_desc", drv_career_desc);
						intent.putExtra("evgood_rate_desc", evgood_rate_desc);
						intent.putExtra("carpool_count_desc", carpool_count_desc);

						PassLongOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
						startActivity(intent);
					}

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderConfirmActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderConfirmActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void showChargeDialog(double rembal, double total_fee)
	{
		final Dialog dlgBalNotEnough = new Dialog(PassLongOrderConfirmActivity.this);
		dlgBalNotEnough.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlgBalNotEnough.setContentView(R.layout.dlg_pass_notenough_balance);
		dlgBalNotEnough.setCancelable(false);

		ResolutionSet.instance.iterateChild(dlgBalNotEnough.findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);

		TextView txtMyBalance = (TextView)dlgBalNotEnough.findViewById(R.id.txt_my_balance);
		TextView txtOrderPrice = (TextView)dlgBalNotEnough.findViewById(R.id.txt_order_price);

		txtMyBalance.setText("" + rembal + getResources().getString(R.string.STR_BALANCE_DIAN));
		txtOrderPrice.setText("" + total_fee + getResources().getString(R.string.STR_BALANCE_DIAN));

		Button btnCancel = (Button)dlgBalNotEnough.findViewById(R.id.btn_cancel);
		Button btnCharge = (Button)dlgBalNotEnough.findViewById(R.id.btn_charge);

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlgBalNotEnough.dismiss();
			}
		});

		btnCharge.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlgBalNotEnough.dismiss();
				onClickCharge();
			}
		});

		dlgBalNotEnough.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgBalNotEnough.show();
	}


	private void onClickCharge()
	{
		Intent intent = new Intent(PassLongOrderConfirmActivity.this, BalanceActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra(BalanceActivity.CHARGE_TAB_NAME, BalanceActivity.CHARGE_TAB);
		PassLongOrderConfirmActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


}

