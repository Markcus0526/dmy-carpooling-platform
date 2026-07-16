package com.damytech.PincheApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PassOnOffOrderProcessActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
    long nOrderID = 0;
    int nOrderType = -1;

    ImageButton btn_back = null;
    RelativeLayout rlMapView = null;

    TextView lblPath = null;
    TextView lblMidPoints = null;
    TextView []lblWeek = new TextView[7];
    TextView lblStartTime = null;
    Button btnCancel = null;

    PassOnOffOrderCancelDialog dlgStop = null;

    private MapView mapView = null;
	private BaiduMap baiduMap = null;
    ArrayList<PointF> arrMidpoints = new ArrayList<PointF>();

	private Overlay startOverlay = null, endOverlay = null;
	private ArrayList<Overlay> arrMidOverlays = new ArrayList<Overlay>();

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
                    Global.showAdvancedToast(PassOnOffOrderProcessActivity.this, strMessaage, Gravity.CENTER);
                    onClickBack();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Global.showAdvancedToast(PassOnOffOrderProcessActivity.this, ex.getMessage(), Gravity.CENTER);
                onClickBack();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            stopProgress();
        }
    };

    private AsyncHttpResponseHandler handlerCancel = new AsyncHttpResponseHandler()
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
                    Global.g_bPassOrderItemShouldUpgrade = true;
                    Global.showAdvancedToast(PassOnOffOrderProcessActivity.this, getString(R.string.STR_SUCCESS), Gravity.CENTER);
                    onClickBack();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(strMessaage);
                }
                else
                {
                    Global.showAdvancedToast(PassOnOffOrderProcessActivity.this, strMessaage, Gravity.CENTER);
                    onClickBack();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Global.showAdvancedToast(PassOnOffOrderProcessActivity.this, ex.getMessage(), Gravity.CENTER);
                onClickBack();
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
        setContentView(R.layout.act_pass_normal_process);

        nOrderID = getIntent().getLongExtra("orderid", -1);
        nOrderType = getIntent().getIntExtra("order_type", -1);

        initControls();
        initResolution();
    }

    @Override
    protected void onResume()
    {
        mapView.onResume();

        super.onResume();

        startProgress();
        CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
                nOrderID,
                nOrderType,
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

    private void initBaidu()
    {
	    SDKInitializer.initialize(getApplicationContext());
    }

    private void initControls()
    {
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });

        lblPath = (TextView) findViewById(R.id.lblPath);
        lblMidPoints = (TextView) findViewById(R.id.lblMidStation);
        lblWeek[0] = (TextView) findViewById(R.id.lblMon);
        lblWeek[1] = (TextView) findViewById(R.id.lblTue);
        lblWeek[2] = (TextView) findViewById(R.id.lblWed);
        lblWeek[3] = (TextView) findViewById(R.id.lblThr);
        lblWeek[4] = (TextView) findViewById(R.id.lblFri);
        lblWeek[5] = (TextView) findViewById(R.id.lblSat);
        lblWeek[6] = (TextView) findViewById(R.id.lblSun);
        lblStartTime = (TextView) findViewById(R.id.lblTime);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgStop = new PassOnOffOrderCancelDialog(PassOnOffOrderProcessActivity.this);
                dlgStop.setOnDismissListener(PassOnOffOrderProcessActivity.this);
	            dlgStop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlgStop.show();
            }
        });

        rlMapView = (RelativeLayout) findViewById(R.id.rlMapView);
        rlMapView.setOnClickListener(onClickListener);

        mapView = (MapView) findViewById(R.id.viewMap);
	    mapView.showZoomControls(false);
	    baiduMap = mapView.getMap();
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
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
//                        .position(new LatLng(endlat, endlng))
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.rlMapView:
                    onClickMapCover();
                    break;
                case R.id.btnCancel:
                    break;
            }
        }
    };

    public void RefreshPage()
    {
        if (stOrderInfo != null)
        {
            lblPath.setText(stOrderInfo.start_addr + " - " + stOrderInfo.end_addr);

            WeekItemShow(stOrderInfo.days, stOrderInfo.leftdays);

            if (stOrderInfo.mid_points != null && stOrderInfo.mid_points.size() > 0)
            {
                String strMidPointName = "";
                for (int i = 0; i < stOrderInfo.mid_points.size(); i++)
                {
                    if ( i == 0 )
                        strMidPointName += stOrderInfo.mid_points.get(i).address;
                    else
                        strMidPointName += ", " + stOrderInfo.mid_points.get(i).address;
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dlgStop == dialog)
        {
            int nRes = dlgStop.IsDeleted();
            if (nRes == 1)
            {
                startProgress();
                Global.g_lPassChangedOrderId = nOrderID;
                Global.g_nPassChangedOrderType = ConstData.ORDER_TYPE_ONOFF;
                CommManager.cancelOnOffOrder(Global.loadUserID(getApplicationContext()),
                        nOrderID,
                        Global.getIMEI(getApplicationContext()),
                        handlerCancel);
            }
        }
    }


	private void onClickMapCover()
	{
		// Move to map activity
		Intent intent = new Intent(PassOnOffOrderProcessActivity.this, OrderRouteActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("startlat", stOrderInfo.start_lat);
		intent.putExtra("startlng", stOrderInfo.start_lng);
		intent.putExtra("endlat", stOrderInfo.end_lat);
		intent.putExtra("endlng", stOrderInfo.end_lng);

		String szMidPoints = "";
		for (int i = 0; i < arrMidpoints.size(); i++)
		{
			if (!szMidPoints.equals(""))
				szMidPoints += ",";
			szMidPoints += arrMidpoints.get(i).x + "," + arrMidpoints.get(i).y;
		}

		intent.putExtra("midpoints", szMidPoints);

		PassOnOffOrderProcessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

}

