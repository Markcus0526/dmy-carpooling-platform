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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-15
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */
public class DrvConfOnceOrdActivity extends SuperActivity
{
	private final int ONCE_ORDER_INDEX = 1;

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
    private TextView txtMileage = null;
	private Button txtSysFee = null;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private RelativeLayout mapCoverLayout = null;

	private Button btnAccept = null;

	private long orderid = 0;
	private long passid = 0;

	private String pass_photo = "";
	private int pass_gender = 0;
	private int pass_age = 0;
	private String pass_name = "";
	private String start_time = "";
	private String start_addr = "";
	private String end_addr = "";

    private double serviceFee;
    private double insuranceFee;

	ArrayList<PointF> arrMidpoints = new ArrayList<PointF>();
	double startlat, startlng, endlat, endlng;

	private Overlay startOverlay = null, endOverlay = null;
	private ArrayList<Overlay> arrMidOverlays = new ArrayList<Overlay>();

	Timer countTimer = null;
	private int wait_time = 240;
    private double mileage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		initBaidu();

		setContentView(R.layout.act_driver_orderconfirm);

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
		txtStartTime = (TextView)findViewById(R.id.lblDateTime);
		txtMidPoints = (TextView)findViewById(R.id.lblMidStation);
		txtPrice = (TextView)findViewById(R.id.lblMark);
        txtMileage = (TextView)findViewById(R.id.lblMileage);
		txtSysFee = (Button)findViewById(R.id.lblSiteMark);
        txtSysFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeeDialog();
            }
        });

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
        mapView.showScaleControl(false);
		baiduMap = mapView.getMap();
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));

		btnAccept = (Button)findViewById(R.id.btnOrder);
		btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				acceptOrder();
			}
		});
        String status = getIntent().getStringExtra("status");
        if(!"1".equals(status)){
            btnAccept.setVisibility(View.GONE);
        }
		orderid = getIntent().getLongExtra("orderid", 0);

        mileage = getIntent().getDoubleExtra("mileage", 0);

		startProgress();
		CommManager.getAcceptableInCityOrderDetailInfo(Global.loadUserID(getApplicationContext()),
				orderid,
				ONCE_ORDER_INDEX,
				Global.getIMEI(getApplicationContext()),
				order_detail_handler);
	}

    private void showFeeDialog() {
        final Dialog sysFeeDialog = new Dialog(DrvConfOnceOrdActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(DrvConfOnceOrdActivity.this);
        View dialogView = mInflater.inflate(R.layout.dlg_driver_fee_detail, null);
        ResolutionSet.instance.iterateChild(dialogView, mScrSize.x, mScrSize.y);
        sysFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sysFeeDialog.setContentView(dialogView);
        sysFeeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOK=(Button)sysFeeDialog.findViewById(R.id.btnOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sysFeeDialog.dismiss();
            }
        });
        TextView feeTxt = (TextView)sysFeeDialog.findViewById(R.id.info_cost_detial);
        TextView insuranceTxt = (TextView)sysFeeDialog.findViewById(R.id.insurance_detail);
        feeTxt.setText(""+serviceFee);
        insuranceTxt.setText(""+insuranceFee);
        sysFeeDialog.show();
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
					txtMileage.setText("总距离" + mileage+"KM");
					txtPassName.setText(pass_info.getString("pass_name"));

					if (pass_info.getInt("verified") == 1)
						imgVerified.setImageResource(R.drawable.bk_person_verified);
					else
						imgVerified.setImageResource(R.drawable.bk_person_notverified);
					txtVerified.setText(pass_info.getString("verified_desc"));

					txtEvalGood.setText(getResources().getString(R.string.STR_HAOPINGLV) + " " + pass_info.getString("evgood_rate_desc"));
					txtCarpoolCnt.setText(getResources().getString(R.string.STR_PINCHECISHU) + " " + pass_info.getString("carpool_count_desc"));

					txtAddress.setText(retdata.getString("start_addr") + getResources().getString(R.string.addr_separator) + retdata.getString("end_addr"));
					txtStartTime.setText(getResources().getString(R.string.STR_YUYUESHIJIAN) + " " + retdata.getString("start_time"));

					JSONArray arrMidPoints = retdata.getJSONArray("mid_points");
					if (arrMidPoints.length() > 0) {
						String szMidPoints = getResources().getString(R.string.STR_ZHONGTUDIAN) + " ";
						for (int i = 0; i < arrMidPoints.length(); i++) {
							JSONObject midPoint = arrMidPoints.getJSONObject(i);
							if (i > 0)
								szMidPoints += ",";
							szMidPoints += midPoint.getString("addr");

							double lat = midPoint.getDouble("lat");
							double lng = midPoint.getDouble("lng");

							arrMidpoints.add(new PointF((float) lat, (float) lng));
						}
						txtMidPoints.setText(szMidPoints);
					} else {
						txtMidPoints.setVisibility(View.GONE);
					}

					txtPrice.setText(retdata.getString("price_desc"));
					

					startlat = retdata.getDouble("start_lat");
					startlng = retdata.getDouble("start_lng");
					endlat = retdata.getDouble("end_lat");
					endlng = retdata.getDouble("end_lng");

					refreshOverlays(startlat, startlng, endlat, endlng, arrMidpoints);
                    //set the fee and insurance value
                    serviceFee = retdata.getDouble("sysinfo_fee");
                    insuranceFee = retdata.getDouble("insu_fee");
                    System.out.println("insuranceFee is:"+insuranceFee);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else if (nRetcode == ConstData.ERR_CODE_ALREADYACCEPTED)
				{
					showFailDialog(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(DrvConfOnceOrdActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			Global.showAdvancedToast(DrvConfOnceOrdActivity.this, content, Gravity.CENTER);
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
		Intent intent = new Intent(DrvConfOnceOrdActivity.this, OrderRouteActivity.class);
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

		DrvConfOnceOrdActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void selectPassenger()
	{
		Intent intent = new Intent(DrvConfOnceOrdActivity.this, PassEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("passid", passid);
		DrvConfOnceOrdActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void acceptOrder()
	{
		startProgress();
		CommManager.acceptOnceOrder(Global.loadUserID(getApplicationContext()),
				orderid,
				Global.loadLatitude(getApplicationContext()),
				Global.loadLongitude(getApplicationContext()),
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
					JSONObject retdata = result.getJSONObject("retdata");

					wait_time = retdata.getInt("wait_time");

					startCounting();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(DrvConfOnceOrdActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvConfOnceOrdActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	TimerTask countTask = new TimerTask()
	{
		@Override
		public void run()
		{
			if (wait_time > 0)
			{
				if (wait_time >= Global.DRV_LOCKTIME_INTERVAL) {
					CommManager.checkOnceOrderAgree(Global.loadUserID(getApplicationContext()),
							orderid,
							Global.getIMEI(getApplicationContext()),
							agree_handler);
				}

				wait_time--;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						m_dlgProg.setMessage("" + wait_time);
					}
				});
			}
			else if (wait_time == 0)                        // Time count finished. Failed to accept
			{
				countTimer.cancel();
				countTimer = null;

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						stopProgress();
						showFailDialog(getResources().getString(R.string.STR_CHENGKEMEIYOUQUEREN));
					}
				});
			}
		}
	};

	private void startCounting()
	{
		startProgress();
		m_dlgProg.setCancelable(false);

		if (countTimer != null)
			countTimer.cancel();

		countTimer = new Timer();
		countTimer.schedule(countTask, 0, 1000);
	}


	private AsyncHttpResponseHandler agree_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.

			try
			{
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				final String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)            // Accept success
				{
					if (countTimer != null)
					{
						countTimer.cancel();
						countTimer = null;
					}

					JSONObject retdata = result.getJSONObject("retdata");

					pass_photo = retdata.getString("pass_img");
					pass_name = retdata.getString("pass_name");
					pass_gender = retdata.getInt("pass_gender");
					pass_age = retdata.getInt("pass_age");
					start_time = retdata.getString("start_time");
					start_addr = retdata.getString("start_addr");
					end_addr = retdata.getString("end_addr");

					moveToSuccessActivity();
				}
				else if (nRetCode == ConstData.ERR_CODE_CANCELLED)
				{
					if (countTimer != null)
					{
						countTimer.cancel();
						countTimer = null;
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showFailDialog(szRetMsg);
						}
					});
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			Global.showAdvancedToast(DrvConfOnceOrdActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void showFailDialog(String szMsg)
	{
		Dialog dlgFail = new Dialog(DrvConfOnceOrdActivity.this);
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
		dlgFail.show();
	}


	private void moveToSuccessActivity()
	{
		Intent retIntent = new Intent();
		retIntent.putExtra("orderid", orderid);
		setResult(RESULT_OK, retIntent);

		Intent intent = new Intent(DrvConfOnceOrdActivity.this, DrvOnceOrderSuccessActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("pass_id", passid);
		intent.putExtra("pass_photo", pass_photo);
		intent.putExtra("pass_gender", pass_gender);
		intent.putExtra("pass_age", pass_age);
		intent.putExtra("pass_name", pass_name);
		intent.putExtra("start_time", start_time);
		intent.putExtra("start_addr", start_addr);
		intent.putExtra("end_addr", end_addr);

		DrvConfOnceOrdActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivity(intent);
		finish();
	}


}
