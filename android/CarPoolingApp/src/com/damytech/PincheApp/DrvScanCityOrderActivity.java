package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STDrvOnceOrder;
import com.damytech.DataClasses.STDrvOnoffOrder;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KimHM
 * Date: 14-8-21
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class DrvScanCityOrderActivity extends SuperActivity
{
	private long lastRefreshTime = 0;

	private ImageButton btn_back = null;
//	private HorizontalPager hor_pager = null;
//	private RelativeLayout indicator_layout = null;
    private RelativeLayout onceOrdersLayout = null;
//    private RelativeLayout onoffOrdersLayout = null;
//    private TextView lblOnce = null, lblCommute = null;
//    private ImageButton btnDanCi = null, btnShangXiaBan = null;

    private final int SORT_DISTANCE = 1;
    private final int SORT_PRICE = 2;
    private final int SORT_TIME = 3;
    private int nSortMode = SORT_DISTANCE;
    private ImageView imgDistance = null, imgPrice = null, imgTime = null;
    private TextView lblDistance = null, lblPrice = null, lblTime = null;
//	private EditText edtStartAddr = null, edtEndAddr = null;
//	private ImageButton btnSearch = null;

//	private String szStartAddr = "", szEndAddr = "";

	private PullToRefreshListView onceOrdersList = null;
	private ArrayList<STDrvOnceOrder> arrOnceOrders = new ArrayList<STDrvOnceOrder>();
    private ArrayList<Long> UidList = new ArrayList<Long>();
	private OnceOrderAdapter onceOrderAdapter = null;
	private int onceOrder_pageno = 0;

//	private PullToRefreshListView onoffOrdersList = null;
//	private ArrayList<STDrvOnoffOrder> arrOnoffOrders = new ArrayList<STDrvOnoffOrder>();
//	private OnoffOrderAdapter onoffOrderAdapter = null;
//	private int onoffOrder_pageno = 0;

	Timer countTimer = null;
	private int wait_time = 240;

	private long tmpOrdID = 0;
	private String pass_photo = "";
	private String pass_name = "";
	private int pass_gender = 0;
	private int pass_age = 0;
	private String start_time = "";
	private String start_addr = "";
	private String end_addr = "";
    private boolean firstRefresh = false;
    private boolean sortModeModified = false;

	private Timer acceptableOrdersTimer = null;
	private TimerTask acceptableOrdersTask = null;

	private final int REQCODE_ACCEPT_ONCEORDER = 1;
	private final int REQCODE_ACCEPT_ONOFFORDER = 2;
    private SoundUtil soundUtil;
    private MWaitingDialog mwaitingdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_driver_scancityorder);

		initControls();
		initResolution();
	}


	@Override
	protected void onResume() {
		super.onResume();

//		if (hor_pager.getCurrentScreen() == 0)
		{
			startProgress();
			CommManager.insertDriverAcceptableOrders(Global.loadUserID(getApplicationContext()),
					Global.getIMEI(getApplicationContext()),
					insert_handler);
		}

		startAcceptableOrdersTimer();
	}


	@Override
	protected void onPause() {
		super.onPause();
		stopAcceptableOrdersTimer();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DrvMainActivity.isNoGetOrder = true;
    }

    private void startAcceptableOrdersTimer()
	{
		stopAcceptableOrdersTimer();

		acceptableOrdersTask = new TimerTask() {
			@Override
			public void run() {
				if (System.currentTimeMillis() - lastRefreshTime > Global.REPORT_INTERVAL()) {
					// Periodically get acceptable orders
//					if (hor_pager.getCurrentScreen() == 0) {
						lastRefreshTime = System.currentTimeMillis();

						Log.d("scan_city_order", "Auto_Timer_Occurred : " + System.currentTimeMillis());
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								getLatestOnceOrders(true);
							}
						});
//					}
				}
			}
		};

		acceptableOrdersTimer = new Timer();
		acceptableOrdersTimer.schedule(acceptableOrdersTask, 0, 1000);
	}


	private void stopAcceptableOrdersTimer()
	{
		if (acceptableOrdersTask != null)
		{
			acceptableOrdersTask.cancel();
			acceptableOrdersTask = null;
		}

		if (acceptableOrdersTimer != null)
		{
			acceptableOrdersTimer.cancel();
			acceptableOrdersTimer = null;
		}
	}



	private void initControls()
	{
        DrvMainActivity.isNoGetOrder = false;
        soundUtil = new SoundUtil(this);
        soundUtil.loadFx(R.raw.new_order_sound, 1);
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

//		hor_pager = (HorizontalPager)findViewById(R.id.hor_pager);
//		hor_pager.setVisibility(View.VISIBLE);
//		hor_pager.setScrollChangeListener(new HorizontalPager.OnHorScrolledListener()
//		{
//			@Override
//			public void onScrolled() {
//				controlIndicatorPos();
//			}
//		});

        onceOrdersLayout = (RelativeLayout) findViewById(R.id.rlOnce);
        onceOrdersLayout.setVisibility(View.VISIBLE);

//        onoffOrdersLayout = (RelativeLayout) findViewById(R.id.rlCommute);
//        onoffOrdersLayout.setVisibility(View.VISIBLE);

//		ViewGroup parentView = null;
//		parentView = (ViewGroup) onceOrdersLayout.getParent();
//		parentView.removeView(onceOrdersLayout);
//
//		parentView = (ViewGroup) onoffOrdersLayout.getParent();
//		parentView.removeView(onoffOrdersLayout);

//		hor_pager.addView(onceOrdersLayout);
//		hor_pager.addView(onoffOrdersLayout);
//		hor_pager.setCurrentScreen(0, true);

//		indicator_layout = (RelativeLayout)findViewById(R.id.page_indicator);

//        lblOnce = (TextView) findViewById(R.id.txt_danci);
//        lblCommute = (TextView) findViewById(R.id.txt_shangxiaban);

        imgDistance = (ImageView) findViewById(R.id.imgPaiXu_Distance);
        imgDistance.setOnClickListener(onClickListener);
        imgPrice = (ImageView) findViewById(R.id.imgPaiXu_Price);
        imgPrice.setOnClickListener(onClickListener);
        imgTime = (ImageView) findViewById(R.id.imgPaiXu_Time);
        imgTime.setOnClickListener(onClickListener);
        lblDistance = (TextView) findViewById(R.id.lblPaiXu_Distance);
        lblDistance.setOnClickListener(onClickListener);
        lblPrice = (TextView) findViewById(R.id.lblPaiXu_Price);
        lblPrice.setOnClickListener(onClickListener);
        lblTime = (TextView) findViewById(R.id.lblPaiXu_Time);
        lblTime.setOnClickListener(onClickListener);

//        btnDanCi = (ImageButton) findViewById(R.id.btn_tab_danci);
//        btnDanCi.setOnClickListener(onClickListener);
//        btnShangXiaBan = (ImageButton) findViewById(R.id.btn_tab_shangxiaban);
//        btnShangXiaBan.setOnClickListener(onClickListener);
//
//		edtStartAddr = (EditText)findViewById(R.id.txtStartPos);
//		edtEndAddr = (EditText)findViewById(R.id.txtEndPos);
//
//		btnSearch = (ImageButton)findViewById(R.id.btn_search_onofforders);
//		btnSearch.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				onClickSearch();
//			}
//		});

		onceOrdersList = (PullToRefreshListView)findViewById(R.id.viewOnce_Data);
		{
			onceOrderAdapter = new OnceOrderAdapter(DrvScanCityOrderActivity.this, arrOnceOrders);

			onceOrdersList.setMode(PullToRefreshBase.Mode.BOTH);
			onceOrdersList.setOnRefreshListener(onceOrderListListener);
			onceOrdersList.setAdapter(onceOrderAdapter);
			onceOrdersList.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFEEEEEE")));
			onceOrdersList.getRefreshableView().setCacheColorHint(Color.parseColor("#FFEEEEEE"));
		}

//		onoffOrdersList = (PullToRefreshListView)findViewById(R.id.viewCommute_Data);
//		{
//			onoffOrderAdapter = new OnoffOrderAdapter(DrvScanCityOrderActivity.this, arrOnoffOrders);
//
//			onoffOrdersList.setMode(PullToRefreshBase.Mode.BOTH);
//			onoffOrdersList.setOnRefreshListener(onoffOrderListListener);
//			onoffOrdersList.setAdapter(onoffOrderAdapter);
//			onoffOrdersList.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFEEEEEE")));
//			onoffOrdersList.getRefreshableView().setCacheColorHint(Color.parseColor("#FFEEEEEE"));
//		}
//
//		getPagedOnoffOrders();
	}


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
	        int nOldMode = nSortMode;

            switch(v.getId())
            {
                case R.id.imgPaiXu_Distance:
	            case R.id.lblPaiXu_Distance:
                    nSortMode = SORT_DISTANCE;
                    break;
                case R.id.imgPaiXu_Price:
	            case R.id.lblPaiXu_Price:
                    nSortMode = SORT_PRICE;
                    break;
                case R.id.imgPaiXu_Time:
                case R.id.lblPaiXu_Time:
                    nSortMode = SORT_TIME;
                    break;
//                case R.id.btn_tab_danci:
//                    hor_pager.setCurrentScreen(0, true);
//                    break;
//                case R.id.btn_tab_shangxiaban:
//                    hor_pager.setCurrentScreen(1, true);
//                    break;
            }

	        if (nOldMode != nSortMode)
	        {
                sortModeModified = true;
                setSortModeUI();

				arrOnceOrders.clear();
				onceOrderAdapter.notifyDataSetChanged();
				onceOrder_pageno = 0;
				getPagedOnceOrders();
			}
		}
	};

	private void setSortModeUI()
	{
		switch(nSortMode)
		{
			case SORT_DISTANCE:
				imgDistance.setImageResource(R.drawable.radiobox_roundsel);
				imgPrice.setImageResource(R.drawable.radiobox_roundnormal);
				imgTime.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case SORT_PRICE:
                imgDistance.setImageResource(R.drawable.radiobox_roundnormal);
                imgPrice.setImageResource(R.drawable.radiobox_roundsel);
                imgTime.setImageResource(R.drawable.radiobox_roundnormal);
                break;
            case SORT_TIME:
                imgDistance.setImageResource(R.drawable.radiobox_roundnormal);
                imgPrice.setImageResource(R.drawable.radiobox_roundnormal);
                imgTime.setImageResource(R.drawable.radiobox_roundsel);
                break;
        }
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

//	private void controlIndicatorPos()
//	{
//		if (indicator_layout == null)
//			return;
//
//		int nScrollX = hor_pager.getScrollX();
//		int nPageWidth = hor_pager.getWidth();
//
//		int nIndicatorWidth = indicator_layout.getWidth();
//		if (nIndicatorWidth == 0) {
//			return;
//		}
//
//		int nTabItemWidth = 240;
//		RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(nTabItemWidth, 3);
//		layout_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		layout_params.leftMargin = nTabItemWidth * nScrollX / nPageWidth;
//
//		indicator_layout.setLayoutParams(layout_params);
//		indicator_layout.setTag(R.string.TAG_KEY_WIDTH, "" + layout_params.width);
//		indicator_layout.setTag(R.string.TAG_KEY_HEIGHT, "" + layout_params.height);
//		indicator_layout.setTag(R.string.TAG_KEY_MARGINLEFT, "" + layout_params.leftMargin);
//
//		ResolutionSet.instance.iterateChild((View)indicator_layout.getParent(), mScrSize.x, mScrSize.y);
//
//		if (nScrollX < nPageWidth / 2)
//		{
//			lblOnce.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
//			lblCommute.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
//		}
//		else
//		{
//            lblOnce.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
//            lblCommute.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
//		}
//	}


	private PullToRefreshBase.OnRefreshListener onceOrderListListener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
			{
                for(STDrvOnceOrder order : arrOnceOrders){
                    UidList.add(order.uid);
                }
				arrOnceOrders.clear();
				onceOrderAdapter.notifyDataSetChanged();
				onceOrder_pageno = 0;

				getPagedOnceOrders();
			}
			else
				getPagedOnceOrders();
		}
	};


//	private PullToRefreshBase.OnRefreshListener onoffOrderListListener = new PullToRefreshBase.OnRefreshListener() {
//		@Override
//		public void onRefresh(PullToRefreshBase refreshView) {
//			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
//			if (mode == PullToRefreshBase.Mode.PULL_FROM_START) {
//				arrOnoffOrders.clear();
//				onoffOrderAdapter.notifyDataSetChanged();
//				onoffOrder_pageno = 0;
//				getPagedOnoffOrders();
//			}
//			else
//				getPagedOnoffOrders();
//		}
//	};


	private class STOnceOrderViewHolder
	{
		TextView lblDest = null;
        TextView lblMileage = null;
		TextView lblPrice = null;
		Button lblSysInfoFee = null;
		TextView lblDate = null;
		TextView lblTime = null;
		TextView lblMidPoints = null;
		TextView lblDist = null;
		SmartImageView imgPassPhoto = null;
		ImageView imgGender = null;
		TextView lblAge = null;
		TextView lblName = null;
		ImageButton btnAccept = null;
		ImageButton btnPhoto = null;
		ImageButton btnBackground = null;
        RelativeLayout rlItembg = null;
        ImageView ivGrab = null;
	}


	private class OnceOrderAdapter extends ArrayAdapter<STDrvOnceOrder>
	{
		public OnceOrderAdapter(Context context, List<STDrvOnceOrder> objects) {
			super(context, 0, objects);    //To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final STDrvOnceOrder orderItem = arrOnceOrders.get(position);
			STOnceOrderViewHolder holder = null;
			if (convertView == null)
			{
				int nWidth = 460, nHeight = 250, nYMargin = 10;
				RelativeLayout itemLayout = new RelativeLayout(parent.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight + nYMargin * 2);
				itemLayout.setLayoutParams(layoutParams);

				RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_driver_oncecarpoolitem, null);
				RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
				RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
				item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				itemView.setLayoutParams(item_layoutParams);
				itemLayout.addView(itemView);

				ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

				convertView = itemLayout;

				holder = new STOnceOrderViewHolder();
				convertView.setTag(holder);
			}
			else
			{
				holder = (STOnceOrderViewHolder)convertView.getTag();
			}

            RelativeLayout rlItembg;
            if(holder.rlItembg == null){
                holder.rlItembg = (RelativeLayout)convertView.findViewById(R.id.rl_orderitem_bg);
            }
            rlItembg = holder.rlItembg;

            ImageView ivGrab;
            if(holder.ivGrab == null)
                holder.ivGrab = (ImageView)convertView.findViewById(R.id.imgOrder);
            ivGrab = holder.ivGrab;
            if("1".equals(orderItem.status.trim())){
                rlItembg.setBackgroundResource(R.drawable.roundgraywhite_frame);
                ivGrab.setImageResource(R.drawable.btn_order);
            }else{
                rlItembg.setBackgroundResource(R.drawable.roundlightgray);
                ivGrab.setImageResource(R.drawable.bk_gradorder);
            }
			// Set address
			TextView lblDest = null;
			if (holder.lblDest == null)
				holder.lblDest = (TextView)convertView.findViewById(R.id.lblDest);
			lblDest = holder.lblDest;
			lblDest.setText(orderItem.start_addr + getResources().getString(R.string.addr_separator) + orderItem.end_addr);

			// Set price
			TextView lblPrice = null;
			if (holder.lblPrice == null)
				holder.lblPrice = (TextView)convertView.findViewById(R.id.lblMark);
			lblPrice = holder.lblPrice;
			lblPrice.setText(orderItem.price + getResources().getString(R.string.STR_BALANCE_DIAN));

			// Set date
			TextView lblDate = null;
			if (holder.lblDate == null)
				holder.lblDate = (TextView)convertView.findViewById(R.id.lblDate);
			lblDate = holder.lblDate;
			lblDate.setText(orderItem.start_time);

			// Set time as empty
			TextView lblTime = null;
			if (holder.lblTime == null)
				holder.lblTime = (TextView)convertView.findViewById(R.id.lblTime);
			lblTime = holder.lblTime;
			lblTime.setText("");


			// Set mid points
//			TextView lblMidPoints = null;
//			if (holder.lblMidPoints == null)
//				holder.lblMidPoints = (TextView)convertView.findViewById(R.id.lblMiddlePos);
//			lblMidPoints = holder.lblMidPoints;
//			lblMidPoints.setText(getResources().getString(R.string.STR_ZHONGTUDIAN) + " " + orderItem.midpoints);

			// Set distance
			TextView lblDist = null;
			if (holder.lblDist == null)
				holder.lblDist = (TextView)convertView.findViewById(R.id.lblDistance);
			lblDist = holder.lblDist;
			lblDist.setText("距您"+orderItem.distance_desc);
            //set milleage
            TextView lblMileage = null;
            if (holder.lblMileage == null)
                holder.lblMileage = (TextView)convertView.findViewById(R.id.tv_destance);
            lblMileage = holder.lblMileage;
            lblMileage.setText("总距离"+orderItem.mileage+"KM");

			// Set passenger photo
			SmartImageView imgPassPhoto = null;
			if (holder.imgPassPhoto == null)
				holder.imgPassPhoto = (SmartImageView)convertView.findViewById(R.id.imgPhoto);
			imgPassPhoto = holder.imgPassPhoto;
			imgPassPhoto.setImageUrl(orderItem.pass_img, R.drawable.default_user_img);
			imgPassPhoto.isCircular = true;
//			imgPassPhoto.setImage(new SmartImage() {
//				@Override
//				public Bitmap getBitmap(Context context) {
//					return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
//				}
//			});

			// Set passenger gender
			ImageView imgGender = null;
			if (holder.imgGender == null)
				holder.imgGender = (ImageView)convertView.findViewById(R.id.imgSex);
			imgGender = holder.imgGender;
			if (orderItem.pass_gender == 0)
				imgGender.setImageResource(R.drawable.bk_manmark);
			else
				imgGender.setImageResource(R.drawable.bk_womanmark);

			// Set passenger age
			TextView lblAge = null;
			if (holder.lblAge == null)
				holder.lblAge = (TextView)convertView.findViewById(R.id.lblAge);
			lblAge = holder.lblAge;
			lblAge.setText("" + orderItem.pass_age);

			// Set passenger passenger name
			TextView lblName = null;
			if (holder.lblName == null)
				holder.lblName = (TextView)convertView.findViewById(R.id.lblName);
			lblName = holder.lblName;
			lblName.setText(orderItem.pass_name);

			// Set button handler
			final long orderid = orderItem.uid;
			final long passid = orderItem.pass_id;
            final double mileage = orderItem.mileage;

			ImageButton btnAccept = null;
			if (holder.btnAccept == null)
				holder.btnAccept = (ImageButton)convertView.findViewById(R.id.btn_accept);
			btnAccept = holder.btnAccept;
            if("1".equals(orderItem.status.trim())){
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptOnceOrder(orderid);
                    }
                });
            }


			ImageButton btnPhoto = null;
			if (holder.btnPhoto == null)
				holder.btnPhoto = (ImageButton)convertView.findViewById(R.id.btnPhoto);
			btnPhoto = holder.btnPhoto;
			btnPhoto.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectUser(passid);
				}
			});

			ImageButton btnBackground = null;
			if (holder.btnBackground == null)
				holder.btnBackground = (ImageButton)convertView.findViewById(R.id.btn_background);
			btnBackground = holder.btnBackground;
			btnBackground.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectOnceOrder(orderid,mileage,orderItem.status.trim());
				}
			});

            // Set system information fee
            Button lblSysInfoFee = null;
            if (holder.lblSysInfoFee == null)
                holder.lblSysInfoFee = (Button)convertView.findViewById(R.id.btn_fee_detail);
            lblSysInfoFee = holder.lblSysInfoFee;
            final double serviceFee = orderItem.sysinfo_fee;
            final double insurance_fee = orderItem.insuranceFee;
            lblSysInfoFee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFeeDialog(serviceFee, insurance_fee);
                }
            });

			return convertView;
		}
	}

    private void showFeeDialog(double serviceFee, double insuranceFee) {
        final Dialog sysFeeDialog = new Dialog(DrvScanCityOrderActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(DrvScanCityOrderActivity.this);
        View dialogView = mInflater.inflate(R.layout.dlg_driver_fee_detail, null);
        ResolutionSet.instance.iterateChild(dialogView, mScrSize.x, mScrSize.y);
        sysFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sysFeeDialog.setContentView(dialogView);
        Button btnOK=(Button)sysFeeDialog.findViewById(R.id.btnOk);
        sysFeeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


//	private class STOnoffOrderViewHolder
//	{
//		TextView lblDest = null;
//		TextView lblPrice = null;
//		TextView lblSysInfoFee = null;
//		TextView lblDate = null;
//		TextView lblMidPoints = null;
//		TextView imgMon = null;
//		TextView imgTue = null;
//		TextView imgWed = null;
//		TextView imgThu = null;
//		TextView imgFri = null;
//		TextView imgSat = null;
//		TextView imgSun = null;
//		SmartImageView imgPassPhoto = null;
//		ImageView imgGender = null;
//		TextView lblAge = null;
//		TextView lblName = null;
//		ImageButton btnPhoto = null;
//		ImageButton btnBackground = null;
//	}


//	private class OnoffOrderAdapter extends ArrayAdapter<STDrvOnoffOrder>
//	{
//		public OnoffOrderAdapter(Context context, List<STDrvOnoffOrder> objects) {
//			super(context, 0, objects);    //To change body of overridden methods use File | Settings | File Templates.
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent)
//		{
//			STDrvOnoffOrder orderItem = arrOnoffOrders.get(position);
//
//			STOnoffOrderViewHolder holder = null;
//			if (convertView == null)
//			{
//				int nWidth = 460, nHeight = 250, nYMargin = 10;
//				RelativeLayout itemLayout = new RelativeLayout(onceOrdersList.getContext());
//				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight + nYMargin * 2);
//				itemLayout.setLayoutParams(layoutParams);
//
//				RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_driver_commutecarpoolitem, null);
//				RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
//				RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
//				item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//				itemView.setLayoutParams(item_layoutParams);
//				itemLayout.addView(itemView);
//
//				ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);
//
//				convertView = itemLayout;
//
//				holder = new STOnoffOrderViewHolder();
//				convertView.setTag(holder);
//			}
//			else
//			{
//				holder = (STOnoffOrderViewHolder)convertView.getTag();
//			}
//
//
//			// Set address
//			TextView lblDest = null;
//			if (holder.lblDest == null)
//				holder.lblDest = (TextView)convertView.findViewById(R.id.lblDest);
//			lblDest = holder.lblDest;
//			lblDest.setText(orderItem.start_addr + "-" + orderItem.end_addr);
//
//			// Set price
//			TextView lblPrice = null;
//			if (holder.lblPrice == null)
//				holder.lblPrice = (TextView)convertView.findViewById(R.id.lblMark);
//			lblPrice = holder.lblPrice;
//			lblPrice.setText(orderItem.price + getResources().getString(R.string.STR_BALANCE_DIAN));
//
//
//			// Set system information fee
//			TextView lblSysInfoFee = null;
//			if (holder.lblSysInfoFee == null)
//				holder.lblSysInfoFee = (TextView)convertView.findViewById(R.id.lblSiteMark);
//			lblSysInfoFee = holder.lblSysInfoFee;
//			lblSysInfoFee.setText(orderItem.sysinfo_fee_desc);
//
//			// Set date
//			TextView lblDate = null;
//			if (holder.lblDate == null)
//				holder.lblDate = (TextView)convertView.findViewById(R.id.lblTime);
//			lblDate = holder.lblDate;
//			lblDate.setText(orderItem.start_time_desc);
//
//			// Set mid points
//			TextView lblMidPoints = null;
//			if (holder.lblMidPoints == null)
//				holder.lblMidPoints = (TextView)convertView.findViewById(R.id.lblMiddlePos);
//			lblMidPoints = holder.lblMidPoints;
//			lblMidPoints.setText(getResources().getString(R.string.STR_ZHONGTUDIAN) + " " + orderItem.midpoints);
//
//            // Set day info
//			if (holder.imgMon == null) {
//				holder.imgMon = (TextView) convertView.findViewById(R.id.lblMon);
//				holder.imgTue = (TextView) convertView.findViewById(R.id.lblTue);
//				holder.imgWed = (TextView) convertView.findViewById(R.id.lblWed);
//				holder.imgThu = (TextView) convertView.findViewById(R.id.lblThu);
//				holder.imgFri = (TextView) convertView.findViewById(R.id.lblFri);
//				holder.imgSat = (TextView) convertView.findViewById(R.id.lblSat);
//				holder.imgSun = (TextView) convertView.findViewById(R.id.lblSun);
//			}
//
//			TextView imgMon = holder.imgMon;
//			TextView imgTue = holder.imgTue;
//			TextView imgWed = holder.imgWed;
//			TextView imgThu = holder.imgThu;
//			TextView imgFri = holder.imgFri;
//			TextView imgSat = holder.imgSat;
//			TextView imgSun = holder.imgSun;
//
//			imgMon.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//			imgTue.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//			imgWed.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//			imgThu.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//			imgFri.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//			imgSat.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//			imgSun.setBackgroundResource(R.drawable.rectgreenwhite_frame);
//
//			imgMon.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//			imgTue.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//			imgWed.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//			imgThu.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//			imgFri.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//			imgSat.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//			imgSun.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
//
//			String[] days = orderItem.days.split(",");
//			for (int i = 0; i < days.length; i++)
//			{
//				String szDay = days[i];
//				if (szDay.equals("0"))
//				{
//					imgMon.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgMon.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//				else if (szDay.equals("1"))
//				{
//					imgTue.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgTue.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//				else if (szDay.equals("2"))
//				{
//					imgWed.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgWed.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//				else if (szDay.equals("3"))
//				{
//					imgThu.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgThu.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//				else if (szDay.equals("4"))
//				{
//					imgFri.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgFri.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//				else if (szDay.equals("5"))
//				{
//					imgSat.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgSat.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//				else if (szDay.equals("6"))
//				{
//					imgSun.setBackgroundResource(R.drawable.rectgreengreen_frame);
//					imgSun.setTextColor(getResources().getColor(R.color.WHITE_COLOR));
//				}
//			}
//
//			// Set passenger photo
//			SmartImageView imgPassPhoto = null;
//			if (holder.imgPassPhoto == null)
//				holder.imgPassPhoto = (SmartImageView)convertView.findViewById(R.id.imgPhoto);
//			imgPassPhoto = holder.imgPassPhoto;
//			imgPassPhoto.isCircular = true;
//			imgPassPhoto.setImageUrl(orderItem.pass_img, R.drawable.default_user_img);
//
//			// Set passenger gender
//			ImageView imgGender = null;
//			if (holder.imgGender == null)
//				holder.imgGender = (ImageView)convertView.findViewById(R.id.imgSex);
//			imgGender = holder.imgGender;
//			if (orderItem.pass_gender == 0)
//				imgGender.setImageResource(R.drawable.bk_manmark);
//			else
//				imgGender.setImageResource(R.drawable.bk_womanmark);
//
//			// Set passenger age
//			TextView lblAge = null;
//			if (holder.lblAge == null)
//				holder.lblAge = (TextView)convertView.findViewById(R.id.lblAge);
//			lblAge = holder.lblAge;
//			lblAge.setText("" + orderItem.pass_age);
//
//			// Set passenger passenger name
//			TextView lblName = null;
//			if (holder.lblName == null)
//				holder.lblName = (TextView)convertView.findViewById(R.id.lblName);
//			lblName = holder.lblName;
//			lblName.setText(orderItem.pass_name);
//
//			// Button operation
//			final long passid = orderItem.pass_id;
//			final long orderid = orderItem.uid;
//
//			ImageButton btnPhoto = null;
//			if (holder.btnPhoto == null)
//				holder.btnPhoto = (ImageButton)convertView.findViewById(R.id.btnPhoto);
//			btnPhoto = holder.btnPhoto;
//			btnPhoto.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					selectUser(passid);
//				}
//			});
//
//			ImageButton btnBackground = null;
//			if (holder.btnBackground == null)
//				holder.btnBackground = (ImageButton)convertView.findViewById(R.id.btn_background);
//			btnBackground = holder.btnBackground;
//			btnBackground.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					selectOnoffOrder(orderid);
//				}
//			});
//
//			return convertView;
//		}
//	}


	private void getLatestOnceOrders(boolean isAuto)
	{
		if (isAuto && mwaitingdialog != null && mwaitingdialog.isShowing())
			return;

        // check network connection
        if (!Global.isOnline(DrvScanCityOrderActivity.this))
        {
            Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_NO_NETWORK), Gravity.CENTER);
            return;
        }

		if (isAuto)
			onceOrdersList.setRefreshing();

		arrOnceOrders.clear();
		CommManager.getPagedAcceptableOnceOrders(Global.loadUserID(getApplicationContext()),
												onceOrder_pageno,
												nSortMode,
												Global.getIMEI(getApplicationContext()),
												pagedOnceOrderHandler);
	}


	private void getPagedOnceOrders()
	{
		CommManager.getPagedAcceptableOnceOrders(Global.loadUserID(getApplicationContext()),
												onceOrder_pageno,
												nSortMode,
												Global.getIMEI(getApplicationContext()),
												pagedOnceOrderHandler);
	}


//	private void getLatestOnoffOrders()
//	{
//		if (arrOnoffOrders.isEmpty())
//			CommManager.getPagedAcceptableOnOffOrders(Global.loadUserID(getApplicationContext()),
//													onoffOrder_pageno,
//													szStartAddr,
//													szEndAddr,
//													Global.getIMEI(getApplicationContext()),
//													pagedOnoffOrderHandler);
//		else
//			CommManager.getLatestAcceptableOnOffOrders(Global.loadUserID(getApplicationContext()),
//													arrOnoffOrders.get(0).uid,
//													szStartAddr,
//													szEndAddr,
//													Global.getIMEI(getApplicationContext()),
//													latestOnoffOrderHandler);
//	}
//
//
//	private void getPagedOnoffOrders()
//	{
//		CommManager.getPagedAcceptableOnOffOrders(Global.loadUserID(getApplicationContext()),
//												onoffOrder_pageno,
//												szStartAddr,
//												szEndAddr,
//												Global.getIMEI(getApplicationContext()),
//												pagedOnoffOrderHandler);
//	}


    private static final String TAG = "erik_debug";
    private AsyncHttpResponseHandler pagedOnceOrderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			onceOrdersList.onRefreshComplete();
			stopProgress();
            Utils.mLogError("订单内容："+content);
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");
                int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");
                Log.d(TAG, "result: "+result);
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");
                    boolean alreadyNotified = false;
					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STDrvOnceOrder onceOrder = STDrvOnceOrder.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrOnceOrders.size(); j++)
						{
							STDrvOnceOrder orgItem = arrOnceOrders.get(j);
							if (orgItem.uid == onceOrder.uid)
							{
								isExist = true;
								existIndex = j;
								break;
							}
						}

                        if (isExist) {
                            arrOnceOrders.set(existIndex, onceOrder);
                        }else {
                            if (!firstRefresh && !sortModeModified &&
                                    !UidList.contains(onceOrder.uid) && !alreadyNotified) {
                                soundUtil.play(1, 1);
                                alreadyNotified = true;
                            }else if (firstRefresh){
                                firstRefresh = false;
                            }else if (sortModeModified){
                                sortModeModified = false;
                            }
                            arrOnceOrders.add(onceOrder);
                        }
						onceOrderAdapter.notifyDataSetChanged();//erik check
					}
                    UidList.clear();
					onceOrder_pageno = arrOnceOrders.size() / Global.getPageItemCount();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(DrvScanCityOrderActivity.this, szRetmsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			onceOrdersList.onRefreshComplete();
			stopProgress();
			Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


    private AsyncHttpResponseHandler latestOnceOrderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			onceOrdersList.onRefreshComplete();
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");
                    boolean alreadyNotified = false;
					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STDrvOnceOrder onceOrder = STDrvOnceOrder.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrOnceOrders.size(); j++)
						{
							STDrvOnceOrder orgItem = arrOnceOrders.get(j);
							if (orgItem.uid == onceOrder.uid)
							{
								isExist = true;
								existIndex = j;
								break;
							}
						}

						if (isExist)
							arrOnceOrders.set(existIndex, onceOrder);
						else {
                            if (!alreadyNotified) {
                                soundUtil.play(1, 1);
                                alreadyNotified = true;
                            }
                            arrOnceOrders.add(0, onceOrder);
                        }
						onceOrderAdapter.notifyDataSetChanged();
					}

					onceOrder_pageno = arrOnceOrders.size() / Global.getPageItemCount();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(DrvScanCityOrderActivity.this, szRetmsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    // To change body of overridden methods use File | Settings | File Templates.
			onceOrdersList.onRefreshComplete();
			stopProgress();
			Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


//	private AsyncHttpResponseHandler pagedOnoffOrderHandler = new AsyncHttpResponseHandler()
//	{
//		@Override
//		public void onSuccess(String content) {
//			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
//			onoffOrdersList.onRefreshComplete();
//			stopProgress();
//
//			try {
//				JSONObject result = new JSONObject(content).getJSONObject("result");
//
//				int nRetcode = result.getInt("retcode");
//				String szRetmsg = result.getString("retmsg");
//
//				if (nRetcode == ConstData.ERR_CODE_NONE)
//				{
//					JSONArray retdata = result.getJSONArray("retdata");
//
//					for (int i = 0; i < retdata.length(); i++)
//					{
//						JSONObject jsonItem = retdata.getJSONObject(i);
//						STDrvOnoffOrder onoffOrder = STDrvOnoffOrder.decodeFromJSON(jsonItem);
//
//						boolean isExist = false;
//						int existIndex = 0;
//
//						for (int j = 0; j < arrOnoffOrders.size(); j++)
//						{
//							STDrvOnoffOrder orgItem = arrOnoffOrders.get(j);
//							if (orgItem.uid == onoffOrder.uid)
//							{
//								isExist = true;
//								existIndex = j;
//								break;
//							}
//						}
//
//						if (isExist)
//							arrOnoffOrders.set(existIndex, onoffOrder);
//						else
//							arrOnoffOrders.add(onoffOrder);
//						onoffOrderAdapter.notifyDataSetChanged();
//					}
//
//					onoffOrder_pageno = arrOnoffOrders.size() / Global.getPageItemCount();
//
//				}
//				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
//				{
//					logout(szRetmsg);
//				}
//				else
//				{
//					Global.showAdvancedToast(DrvScanCityOrderActivity.this, szRetmsg, Gravity.CENTER);
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onFailure(Throwable error, String content) {
//			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
//			onoffOrdersList.onRefreshComplete();
//			stopProgress();
//		}
//	};
//
//
//	private AsyncHttpResponseHandler latestOnoffOrderHandler = new AsyncHttpResponseHandler()
//	{
//		@Override
//		public void onSuccess(String content) {
//			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
//			onoffOrdersList.onRefreshComplete();
//			stopProgress();
//
//			try {
//				JSONObject result = new JSONObject(content).getJSONObject("result");
//
//				int nRetcode = result.getInt("retcode");
//				String szRetmsg = result.getString("retmsg");
//
//				if (nRetcode == ConstData.ERR_CODE_NONE)
//				{
//					JSONArray retdata = result.getJSONArray("retdata");
//
//					for (int i = 0; i < retdata.length(); i++)
//					{
//						JSONObject jsonItem = retdata.getJSONObject(i);
//						STDrvOnoffOrder onoffOrder = STDrvOnoffOrder.decodeFromJSON(jsonItem);
//
//						boolean isExist = false;
//						int existIndex = 0;
//
//						for (int j = 0; j < arrOnoffOrders.size(); j++)
//						{
//							STDrvOnoffOrder orgItem = arrOnoffOrders.get(j);
//							if (orgItem.uid == onoffOrder.uid)
//							{
//								isExist = true;
//								existIndex = j;
//								break;
//							}
//						}
//
//						if (isExist)
//							arrOnoffOrders.set(existIndex, onoffOrder);
//						else
//							arrOnoffOrders.add(0, onoffOrder);
//						onoffOrderAdapter.notifyDataSetChanged();
//					}
//
//					onoffOrder_pageno = arrOnoffOrders.size() / Global.getPageItemCount();
//				}
//				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
//				{
//					logout(szRetmsg);
//				}
//				else
//				{
//					Global.showAdvancedToast(DrvScanCityOrderActivity.this, szRetmsg, Gravity.CENTER);
//				}
//
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onFailure(Throwable error, String content) {
//			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
//			onoffOrdersList.onRefreshComplete();
//			stopProgress();
//		}
//	};
//
//
//	private void onClickSearch()
//	{
//		szStartAddr = edtStartAddr.getText().toString();
//		szEndAddr = edtEndAddr.getText().toString();
//
//		arrOnoffOrders.clear();
//		onoffOrderAdapter.notifyDataSetChanged();
//		onoffOrder_pageno = 0;
//		getPagedOnoffOrders();
//	}


	private void selectUser(long passid)
	{
		Intent intent = new Intent(DrvScanCityOrderActivity.this, PassEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("passid", passid);
		DrvScanCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void selectOnceOrder(long orderid, double mileage, String status)
	{
		Intent intent = new Intent(DrvScanCityOrderActivity.this, DrvConfOnceOrdActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("orderid", orderid);
        intent.putExtra("mileage", mileage);
        intent.putExtra("status",status);
		DrvScanCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_ACCEPT_ONCEORDER);
	}

	private void selectOnoffOrder(long orderid)
	{
		Intent intent = new Intent(DrvScanCityOrderActivity.this, DrvConfOnoffOrdActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("orderid", orderid);
		DrvScanCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_ACCEPT_ONOFFORDER);
	}


	private void acceptOnceOrder(long orderid)
	{
		tmpOrdID = orderid;

		startProgress("抢单成功，请耐心等待拼友确认！");
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

                    Utils.mLogError("--开始等待时间="+wait_time);


					startCounting();
//                    showHintMessage();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else if (nRetcode == ConstData.ERR_CODE_CANCELLED)
				{
                    DialogUtil.cancelDialog();
                    Utils.mLogError("accept_handler fail:"+szRetmsg);
					showFailDialog(szRetmsg);
				}
				else
				{
                    Utils.mLogError("accept_handler:"+szRetmsg);
					Global.showAdvancedToast(DrvScanCityOrderActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

    private void startWaiting(String msg){
        if (mwaitingdialog != null && mwaitingdialog.isShowing()){
            mwaitingdialog.setMessage(msg);
            return;
        }


        if (mwaitingdialog == null)
        {
            mwaitingdialog = new MWaitingDialog(this);
            mwaitingdialog.setMessage(msg);
            mwaitingdialog.setCancelable(false);
        }

        mwaitingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mwaitingdialog.show();
    }
    private void stopWaiting(){
        if (mwaitingdialog != null)
        {
            mwaitingdialog.dismiss();
            mwaitingdialog = null;
        }
    }

    private void showChargeMessage(){
//        ToastUtil.showToast(DrvScanCityOrderActivity.this,"拼友正在充值，请稍等",Gravity.CENTER,Toast.LENGTH_LONG);
        DialogUtil.cancelDialog();
        DialogUtil.showDialog(this,"拼友正在充值，请耐心等待",Gravity.CENTER);
    }
    private void showHintMessage(){
        DialogUtil.cancelDialog();
        DialogUtil.showDialog(this,"拼友确认中，请耐心等待",Gravity.CENTER);
    }
    TimerTask countTask = null;
	private void startCounting()
	{
//		startProgress();
        startWaiting("拼友确认中，请耐心等待");
        mwaitingdialog.setCancelable(false);

		if (countTimer != null)
			countTimer.cancel();

        countTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (wait_time > 0)
                {
                    if (wait_time >= Global.DRV_LOCKTIME_INTERVAL) {
	                    CommManager.checkOnceOrderAgree(Global.loadUserID(getApplicationContext()),
			                    tmpOrdID,
			                    Global.getIMEI(getApplicationContext()),
			                    agree_handler);
                    }

                    wait_time--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                if(mwaitingdialog != null)
                                    mwaitingdialog.setCount("" + wait_time);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
                else                    // Time count finished. Failed to accept
                {
//                    DialogUtil.cancelDialog();
                    CommManager.has_clickedchargingbtn(Global.loadUserID(getApplicationContext()),
                                tmpOrdID,
                                Global.getIMEI(getApplicationContext()),
                                add_time_handlar);

                    if (countTimer != null)
                    {
                        countTimer.cancel();
                        countTimer = null;
                        countTask.cancel();
                    }

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            stopProgress();
//                            showFailDialog(getResources().getString(R.string.STR_CHENGKEMEIYOUQUEREN));
//                        }
//                    });
                }
            }
        };

		countTimer = new Timer();
		countTimer.schedule(countTask, 0, 1000);
	}


    private void startCountingAgain()
    {
//        startProgress();
        startWaiting("拼友正在充值，请耐心等待");
        mwaitingdialog.setCancelable(false);

        if (countTimer != null)
            countTimer.cancel();

        countTask = new TimerTask()
        {
            @Override
            public void run()
            {
                if (wait_time > 0)
                {
                    if (wait_time >= Global.DRV_LOCKTIME_INTERVAL) {
                        CommManager.checkOnceOrderAgree(Global.loadUserID(getApplicationContext()),
                                tmpOrdID,
                                Global.getIMEI(getApplicationContext()),
                                agree_handler);
                    }

                    wait_time--;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                if(mwaitingdialog != null)
                                    mwaitingdialog.setCount("" + wait_time);
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
                else                    // Time count finished. Failed to accept
                {
//                    DialogUtil.cancelDialog();
                    if (countTimer != null)
                    {
                        countTimer.cancel();
                        countTimer = null;
                        countTask.cancel();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            stopProgress();
//                            DialogUtil.cancelDialog();
                            stopWaiting();
                            showFailDialog(getResources().getString(R.string.STR_CHENGKEMEIYOUQUEREN));
                        }
                    });
                }
            }
        };

        countTimer = new Timer();
        countTimer.schedule(countTask, 0, 1000);
    }

    private AsyncHttpResponseHandler add_time_handlar = new AsyncHttpResponseHandler()
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

                    JSONObject retdata = result.getJSONObject("retdata");
                    wait_time = retdata.getInt("waittime_when_charging");
                    Utils.mLogError("---等待充值时间="+wait_time);
//                    showChargeMessage();
                    startCountingAgain();

                }
                else if (nRetCode == ConstData.ERR_CODE_CANCELLED)
                {
                    if (countTimer != null)
                    {
                        countTimer.cancel();
                        countTimer = null;
                        countTask.cancel();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            stopProgress();
//                            DialogUtil.cancelDialog();
                            stopWaiting();
                            showFailDialog(szRetMsg);

                            getLatestOnceOrders(false);
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
            Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };



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

                    DialogUtil.cancelDialog();
					moveToSuccessActivity();
				}
				else if (nRetCode == ConstData.ERR_CODE_CANCELLED)
				{
					if (countTimer != null)
					{
						countTimer.cancel();
						countTimer = null;
                        countTask.cancel();
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
//							stopProgress();
//                            DialogUtil.cancelDialog();
                            stopWaiting();
							showFailDialog(szRetMsg);

							getLatestOnceOrders(false);
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
			Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void showFailDialog(String szMsg)
	{
		Dialog dlgFail = new Dialog(DrvScanCityOrderActivity.this);
		dlgFail.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlgFail.setContentView(R.layout.dlg_driver_orderfail);
		dlgFail.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				getLatestOnceOrders(false);
			}
		});

		View contRootView = dlgFail.findViewById(R.id.parent_layout);
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
		STDrvOnceOrder drvOnceOrder = null;
		for (int i = 0; i < arrOnceOrders.size(); i++)
		{
			STDrvOnceOrder orderItem = arrOnceOrders.get(i);
			if (orderItem.uid == tmpOrdID)
			{
				drvOnceOrder = orderItem;
				break;
			}
		}

		if (drvOnceOrder == null)
			return;

		Intent retIntent = new Intent();
		retIntent.putExtra("orderid", tmpOrdID);
		setResult(RESULT_OK, retIntent);

		Intent intent = new Intent(DrvScanCityOrderActivity.this, DrvOnceOrderSuccessActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("pass_id", drvOnceOrder.pass_id);
		intent.putExtra("pass_photo", pass_photo);
		intent.putExtra("pass_gender", pass_gender);
		intent.putExtra("pass_age", pass_age);
		intent.putExtra("pass_name", pass_name);
		intent.putExtra("start_time", start_time);
		intent.putExtra("start_addr", start_addr);
		intent.putExtra("end_addr", end_addr);

		DrvScanCityOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivity(intent);
		finish();
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (resultCode != RESULT_OK || data == null)
			return;

		long orderid = data.getLongExtra("orderid", 0);
		if (requestCode == REQCODE_ACCEPT_ONCEORDER)
		{
			for (int i = 0; i < arrOnceOrders.size(); i++)
			{
				STDrvOnceOrder onceOrder = arrOnceOrders.get(i);
				if (onceOrder.uid == orderid)
				{
					arrOnceOrders.remove(i);
					break;
				}
			}

			onceOrderAdapter.notifyDataSetChanged();
		}
//		else if (requestCode == REQCODE_ACCEPT_ONOFFORDER)
//		{
//			for (int i = 0; i < arrOnoffOrders.size(); i++)
//			{
//				STDrvOnoffOrder onoffOrder = arrOnoffOrders.get(i);
//				if (onoffOrder.uid == orderid)
//				{
//					arrOnoffOrders.remove(i);
//					break;
//				}
//			}
//
//			onoffOrderAdapter.notifyDataSetChanged();
//		}
	}



	private AsyncHttpResponseHandler insert_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					lastRefreshTime = System.currentTimeMillis();
					getLatestOnceOrders(true);
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
                    Utils.mLogError("insert_handler:"+szRetMsg);
					Global.showAdvancedToast(DrvScanCityOrderActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
			Global.showAdvancedToast(DrvScanCityOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

}



