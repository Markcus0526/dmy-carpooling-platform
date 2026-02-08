package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
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
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrvConfOnoffOrdActivity extends SuperActivity
{
	private final int ONOFF_ORDER_INDEX = 2;

	private ImageButton btnBack = null;
	private SmartImageView imgPassPhoto = null;
	private TextView txtPassAge = null;
	private ImageView imgPassGender = null;
	private ImageButton btnPassPhoto = null;
	private TextView txtPassName = null;
	private ImageView imgVerified = null;
	private TextView txtVerified = null;
	private TextView txtEvalGood = null;
	private TextView txtCarpoolCnt = null;
	private TextView txtAddress = null;
	private TextView txtStartTime = null;
	private TextView txtMidPoints = null;
	private TextView txtPrice = null;
	private TextView txtSysFee = null;

	private TextView imgMon = null, imgTue = null, imgWed = null, imgThu = null, imgFri = null, imgSat = null, imgSun = null;
	private boolean selMon = false, selTue = false, selWed = false, selThu = false, selFri = false, selSat = false, selSun = false;
	private boolean acceptMon = false, acceptTue = false, acceptWed = false, acceptThu = false, acceptFri = false, acceptSat = false, acceptSun = false;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private RelativeLayout mapCoverLayout = null;

	private Button btnAccept = null;

	private RelativeLayout sucLayout = null;

	private long orderid = 0;
	private long passid = 0;

	ArrayList<PointF> arrMidpoints = new ArrayList<PointF>();
	double startlat, startlng, endlat, endlng;

	private Overlay startOverlay = null, endOverlay = null;
	private ArrayList<Overlay> arrMidOverlays = new ArrayList<Overlay>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		initBaidu();

		setContentView(R.layout.act_driver_commuteorderconfirm);

		initControls();
		initResolution();
	}

	@Override
	protected void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mapView.onPause();
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

	private void initControls()
	{
		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});

		imgPassPhoto = (SmartImageView)findViewById(R.id.viewPhoto);
		imgPassPhoto.isCircular = true;
		imgPassPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
			}
		});

		txtPassAge = (TextView)findViewById(R.id.lblAge);
		imgPassGender = (ImageView)findViewById(R.id.imgSex);

		btnPassPhoto = (ImageButton)findViewById(R.id.btnPhoto);
		btnPassPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPassenger();
			}
		});

		txtPassName = (TextView)findViewById(R.id.lblName);
		imgVerified = (ImageView)findViewById(R.id.imgSecurity);
		txtVerified = (TextView)findViewById(R.id.lblSecurity);
		txtEvalGood = (TextView)findViewById(R.id.lblRatio);
		txtCarpoolCnt = (TextView)findViewById(R.id.lblCarpoolCount);
		txtAddress = (TextView)findViewById(R.id.lblPath);
		txtStartTime = (TextView)findViewById(R.id.lblTime);
		txtMidPoints = (TextView)findViewById(R.id.lblMidStation);
		txtPrice = (TextView)findViewById(R.id.lblMark);
		txtSysFee = (TextView)findViewById(R.id.lblSiteMark);

		mapCoverLayout = (RelativeLayout)findViewById(R.id.rlMapView);
		mapCoverLayout.setClickable(true);
		mapCoverLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickMapCover();
			}
		});

		mapView = (MapView)findViewById(R.id.viewMap);
		mapView.showZoomControls(false);
		baiduMap = mapView.getMap();
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));

		btnAccept = (Button)findViewById(R.id.btnOrder);
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptOrder();
			}
		});

		imgMon = (TextView)findViewById(R.id.lblMon);
		imgMon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selMon)
					return;

				acceptMon = !acceptMon;
				if (acceptMon)
				{
					imgMon.setBackgroundResource(R.drawable.bk_selday);
					imgMon.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgMon.setBackgroundResource(R.drawable.rectgreengreen_frame);
					imgMon.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		});
		imgTue = (TextView)findViewById(R.id.lblTue);
		imgTue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selTue)
					return;

				acceptTue = !acceptTue;
				if (acceptTue)
				{
					imgTue.setBackgroundResource(R.drawable.bk_selday);
					imgTue.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgTue.setBackgroundResource(R.drawable.rectgreengreen_frame);
					imgTue.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		});
		imgWed = (TextView)findViewById(R.id.lblWed);
		imgWed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selWed)
					return;

				acceptWed = !acceptWed;
				if (acceptWed)
				{
					imgWed.setBackgroundResource(R.drawable.bk_selday);
					imgWed.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgWed.setBackgroundResource(R.drawable.rectgreengreen_frame);
					imgWed.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		});
		imgThu = (TextView)findViewById(R.id.lblThu);
		imgThu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selThu)
					return;

				acceptThu = !acceptThu;
				if (acceptThu)
				{
					imgThu.setBackgroundResource(R.drawable.bk_selday);
					imgThu.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgThu.setBackgroundResource(R.drawable.rectgreengreen_frame);
					imgThu.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		});
		imgFri = (TextView)findViewById(R.id.lblFri);
		imgFri.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selFri)
					return;
				acceptFri = !acceptFri;
				if (acceptFri)
				{
					imgFri.setBackgroundResource(R.drawable.bk_selday);
					imgFri.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgFri.setBackgroundResource(R.drawable.rectgreengreen_frame);
					imgFri.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		});
		imgSat = (TextView)findViewById(R.id.lblSat);
		imgSat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selSat)
					return;

				acceptSat = !acceptSat;
				if (acceptSat)
				{
					imgSat.setBackgroundResource(R.drawable.bk_selday);
					imgSat.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgSat.setBackgroundResource(R.drawable.rectgreengreen_frame);
					imgSat.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		});
		imgSun = (TextView)findViewById(R.id.lblSun);
		imgSun.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!selSun)
					return;

				acceptSun = !acceptSun;
				if (acceptSun)
				{
					imgSun.setBackgroundResource(R.drawable.bk_selday);
					imgSun.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
				else
				{
					imgSun.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgSun.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
				}
			}
		});

		orderid = getIntent().getLongExtra("orderid", 0);

		sucLayout = (RelativeLayout)findViewById(R.id.success_layout);
		sucLayout.setVisibility(View.GONE);
		sucLayout.setClickable(true);
		sucLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});

		startProgress();
		CommManager.getAcceptableInCityOrderDetailInfo(Global.loadUserID(getApplicationContext()),
				orderid,
				ONOFF_ORDER_INDEX,
				Global.getIMEI(getApplicationContext()),
				order_detail_handler);
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


	private AsyncHttpResponseHandler order_detail_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					// Set passenger information
					JSONObject pass_info = retdata.getJSONObject("pass_info");

					passid = pass_info.getLong("pass_id");
					imgPassPhoto.setImageUrl(pass_info.getString("pass_img"), R.drawable.default_user_img);

					if (pass_info.getInt("pass_gender") == 0)
						imgPassGender.setImageResource(R.drawable.bk_manmark);
					else
						imgPassGender.setImageResource(R.drawable.bk_womanmark);

					txtPassAge.setText("" + pass_info.getInt("pass_age"));
					txtPassName.setText(pass_info.getString("pass_name"));

					if (pass_info.getInt("verified") == 1)
						imgVerified.setImageResource(R.drawable.bk_person_verified);
					else
						imgVerified.setImageResource(R.drawable.bk_person_notverified);
					txtVerified.setText(pass_info.getString("verified_desc"));

					txtEvalGood.setText(getResources().getString(R.string.STR_HAOPINGLV) + " " + pass_info.getString("evgood_rate_desc"));
					txtCarpoolCnt.setText(getResources().getString(R.string.STR_PINCHECISHU) + " " + pass_info.getString("carpool_count_desc"));

					txtAddress.setText(retdata.getString("start_addr") + "-" + retdata.getString("end_addr"));
					txtStartTime.setText(retdata.getString("start_time"));


					// Set day info
					imgMon.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgTue.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgWed.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgThu.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgFri.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgSat.setBackgroundResource(R.drawable.rectgreenwhite_frame);
					imgSun.setBackgroundResource(R.drawable.rectgreenwhite_frame);

					imgMon.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					imgTue.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					imgWed.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					imgThu.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					imgFri.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					imgSat.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					imgSun.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));

					selMon = selTue = selWed = selThu = selFri = selSat = selSun = false;

					String[] days = retdata.getString("valid_days").split(",");
					for (int i = 0; i < days.length; i++)
					{
						String szDay = days[i];
						if (szDay.equals("0"))
						{
                            imgMon.setBackgroundResource(R.drawable.bk_selday);
							imgMon.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selMon = true;
                            acceptMon = true;
						}
						else if (szDay.equals("1"))
						{
							imgTue.setBackgroundResource(R.drawable.bk_selday);
							imgTue.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selTue = true;
                            acceptTue = true;
						}
						else if (szDay.equals("2"))
						{
							imgWed.setBackgroundResource(R.drawable.bk_selday);
							imgWed.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selWed = true;
                            acceptWed = true;
						}
						else if (szDay.equals("3"))
						{
							imgThu.setBackgroundResource(R.drawable.bk_selday);
							imgThu.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selThu = true;
                            acceptThu = true;
						}
						else if (szDay.equals("4"))
						{
							imgFri.setBackgroundResource(R.drawable.bk_selday);
							imgFri.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selFri = true;
                            acceptFri = true;
						}
						else if (szDay.equals("5"))
						{
							imgSat.setBackgroundResource(R.drawable.bk_selday);
							imgSat.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selSat = true;
                            acceptSat = true;
						}
						else if (szDay.equals("6"))
						{
							imgSun.setBackgroundResource(R.drawable.bk_selday);
							imgSun.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
							selSun = true;
                            acceptSun = true;
						}
					}



					JSONArray arrMidPoints = retdata.getJSONArray("mid_points");
                    if (arrMidPoints != null && arrMidPoints.length() > 0)
                    {
                        String szMidPoints = getResources().getString(R.string.STR_ZHONGTUDIAN) + " ";
                        for (int i = 0; i < arrMidPoints.length(); i++)
                        {
                            JSONObject midPoint = arrMidPoints.getJSONObject(i);
                            if (i > 0)
                                szMidPoints += ",";
                            szMidPoints += midPoint.getString("addr");

                            double lat = midPoint.getDouble("lat");
                            double lng = midPoint.getDouble("lng");

                            arrMidpoints.add(new PointF((float)lat, (float)lng));
                        }
                        txtMidPoints.setText(szMidPoints);
                    }

					txtPrice.setText(retdata.getString("price_desc"));
					txtSysFee.setText(retdata.getString("sysinfo_fee_desc"));

					startlat = retdata.getDouble("start_lat");
					startlng = retdata.getDouble("start_lng");
					endlat = retdata.getDouble("end_lat");
					endlng = retdata.getDouble("end_lng");

					refreshOverlays(startlat, startlng, endlat, endlng, arrMidpoints);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(DrvConfOnoffOrdActivity.this, szRetmsg, Gravity.CENTER);
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


	private void onClickMapCover()
	{
		// Move to map activity
		Intent intent = new Intent(DrvConfOnoffOrdActivity.this, OrderRouteActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("startlat", startlat);
		intent.putExtra("startlng", startlng);
		intent.putExtra("endlat", endlat);
		intent.putExtra("endlng", endlng);

		String szMidPoints = "";
		for (int i = 0; i < arrMidpoints.size(); i++)
		{
			if (!szMidPoints.equals(""))
				szMidPoints += ",";
			szMidPoints += arrMidpoints.get(i).x + "," + arrMidpoints.get(i).y;
		}

		intent.putExtra("midpoints", szMidPoints);

		DrvConfOnoffOrdActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void selectPassenger()
	{
		Intent intent = new Intent(DrvConfOnoffOrdActivity.this, PassEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("passid", passid);
		DrvConfOnoffOrdActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void acceptOrder()
	{
		String sel_days = "";
		if (acceptMon)
		{
			if (sel_days.equals(""))
				sel_days += "0";
			else
				sel_days += ",0";
		}

		if (acceptTue)
		{
			if (sel_days.equals(""))
				sel_days += "1";
			else
				sel_days += ",1";
		}

		if (acceptWed)
		{
			if (sel_days.equals(""))
				sel_days += "2";
			else
				sel_days += ",2";
		}

		if (acceptThu)
		{
			if (sel_days.equals(""))
				sel_days += "3";
			else
				sel_days += ",3";
		}

		if (acceptFri)
		{
			if (sel_days.equals(""))
				sel_days += "4";
			else
				sel_days += ",4";
		}

		if (acceptSat)
		{
			if (sel_days.equals(""))
				sel_days += "5";
			else
				sel_days += ",5";
		}

		if (acceptSun)
		{
			if (sel_days.equals(""))
				sel_days += "6";
			else
				sel_days += ",6";
		}

		if (sel_days.equals(""))
		{
			Global.showAdvancedToast(DrvConfOnoffOrdActivity.this, getResources().getString(R.string.STR_NOTSELECTDAYS), Gravity.CENTER);
			return;
		}

		startProgress();
		CommManager.acceptOnOffOrder(Global.loadUserID(getApplicationContext()),
				orderid,
				sel_days,
				Global.getIMEI(getApplicationContext()),
				accept_handler);
	}


	private AsyncHttpResponseHandler accept_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					sucLayout.setVisibility(View.VISIBLE);

					Intent intent = new Intent();
					intent.putExtra("orderid", orderid);
					setResult(RESULT_OK, intent);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(DrvConfOnoffOrdActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			Global.showAdvancedToast(DrvConfOnoffOrdActivity.this, content, Gravity.CENTER);
		}
	};
}
