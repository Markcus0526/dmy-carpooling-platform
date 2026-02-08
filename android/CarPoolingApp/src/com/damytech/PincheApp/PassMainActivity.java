package com.damytech.PincheApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STPsgOrderListItem;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Misc.Market;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;


import com.damytech.Utils.Rotate3dAnimation;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.SharedPreferenceUtil;
import com.damytech.Utils.mutil.Utils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.*;
import com.umeng.socialize.sso.*;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

/**
 * 拼友主界面
 */
public class PassMainActivity extends SuperActivity
{
	private static final String TAG = "erik_debug";

	public static final String TAB_EXTRA_NAME = "tab_index";
	public static final int TAB_SHOUYE = 1;
	public static final int TAB_DINGDAN = 2;
	public static final int TAB_GEREN = 3;
	public static final int TAB_FENXIANG = 4;

	//应用状态码
	private int REQCODE_NEWS = 0;
	private int REQCODE_VERIFY_PERSON = 1;
	private int REQCODE_ONCE_ORDER = 2;
	private int REQCODE_LONG_ORDER = 3;
	private int REQCODE_CITY_ORDER = 4;
	private int REQCODE_TAB_ORDER = 5;
	private int REQCODE_TAB_PERSONINFO = 6;
	private int REQCODE_TAB_SHARE = 7;
	private int REQCODE_ONCE_ORDERDETAIL = 8;
//	private int REQCODE_ONOFF_ORDERDETAIL = 9;
	private int REQCODE_LONG_ORDERDETAIL = 10;
	private int REQCODE_PAY_ORDER = 11;
	private int REQCODE_EVALUATE = 12;
	private int REQCODE_NEWS2 = 13;
	private int REQCODE_VERIFY_PERSON2 = 14;
	private int REQCODE_ONCE_ORDER2 = 15;
	private int REQCODE_LONG_ORDER2 = 16;
	private int REQCODE_CITY_ORDER2 = 17;

	// Order tab variables
	private final int ALL_MODE = 0;
	private final int REQOPR_MODE = 1;
	private final int ONCE_MODE = 3;
	private final int COMMUTE_MODE = 4;
	private final int LONGDIST_MODE = 5;
	private int nSortMode = REQOPR_MODE;

	private ImageView imgAll = null;
	private TextView lblAll = null;
	private ImageView imgReqOpr = null;
	private TextView lblReqOpr = null;
	private ImageView imgOnce = null;
	private TextView lblOnce = null;
//	private ImageView imgCommute = null;
//	private TextView lblCommute = null;
	private ImageView imgLongDist = null;
	private TextView lblLongDist = null;
	private TextView popText;

	private ArrayList<STPsgOrderListItem> arrOrders = new ArrayList<STPsgOrderListItem>();
	private OrderAdapter orderAdapter = null;
	private PullToRefreshListView orderListView = null;
	private TextView lblNoOrders = null;

	private boolean isInit = false;

	long mCurSelOrderId = 0;
	int mCurSelOrderType = 0;

	// Baidu variables
	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	/**
	 * 定位SDK的核心类
	 */
	private LocationClient bm_loc_client;
	/**
	 * 用户位置信息
	 */
	private MyLocationData mLocData;
	/**
	 * 我的位置图层
	 */
//	private LocationOverlay myLocationOverlay = null;
	/**
	 * 弹出窗口图层
	 */

	private OverlayOptions option;
	private Marker mainMarker;
	/**
	 * 弹出窗口图层的View
	 */
	private View mPopupView;
	private BDLocation location;
	private TextView driverNum;
	private LatLng point;


	///////////////////////////////////////////////////////////////////////////////////////////////


	// Tab controls
	private RelativeLayout tab_shouye = null;
	private RelativeLayout tab_dingdan = null;
	private RelativeLayout tab_geren = null;
	private RelativeLayout tab_fenxiang = null;

	private ImageView imgTabShouYe = null;
	private ImageView imgTabDingDan = null;
	private ImageView imgTabGeRen = null;
	private ImageView imgTabFenXiang = null;

	private TextView txtTabShouYe= null;
	private TextView txtTabDingDan = null;
	private TextView txtTabGeRen = null;
	private TextView txtTabFenXiang = null;

	private ImageButton btnTabShouYe = null;
	private ImageButton btnTabDingDan = null;
	private ImageButton btnTabGeRen = null;
	private ImageButton btnTabFenXiang = null;
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Main tab controls
    private ViewGroup mContainer;
	private ImageButton btn_identify = null, btn_news = null;
	private ImageView img_hasnews = null;

	private RelativeLayout person_layout = null;
	private Button btn_verify_person = null;

	private Button btn_long_order = null, btn_city_order = null;
	private ImageButton locationButton;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int announce_count = 0, order_notif_count = 0, person_notif_count = 0;


	// Person info tab controls
	private ImageButton btnPersonInfo = null;
	private ImageButton btnRouteSetting = null;
	private ImageButton btnMoney = null;
	private ImageButton btnCoupon = null;
	private ImageButton btnStrategy = null;
	private ImageButton btnAbout = null;
	private Button btnLogout = null;
    private TextView txtTitle = null;

	//other
	ArrayList<DriverLocation> driverLocations = new ArrayList<DriverLocation>();
    public static ArrayList<DriverLocation> driverLocationsVirtual = new ArrayList<DriverLocation>();


	private final UMSocialService mShareController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private ImageButton btnShare = null;
	private WebView shareWebView = null;
	private String szDefContent1 = null, szDefContent2 = null, szDefContent3 = null;
    private boolean initComplete = false;
    private String outputUrl = null;
    private String outputContent = null;
    private SharedPreferenceUtil spUtil;
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//定位图层实现
//	private class LocationOverlay extends MyLocationOverlay{
//
//		public LocationOverlay(MapView arg0) {
//			super(arg0);
//		}
//
//
//		/**
//		 * 在“我的位置”坐标上处理点击事件。
//		 */
//		@Override
//		protected boolean dispatchTap() {
//			return super.dispatchTap();
//
//		}
//
//	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	//To change body of overridden methods use File | Settings | File Templates.
		initBaidu();//百度功能

		setContentView(R.layout.act_pass_main);

		initControls();//控件示例化
		initResolution();//适配屏幕
		//Log.d("erik_debug_passMian_onCreate", "" + Global.isLoggedIn(this.getApplicationContext()));
	}

	@Override
	protected void onResume() {
		bm_loc_client.start();
		//initSearchManager();
        if(mapView != null)
		    mapView.onResume();
		super.onResume();	//To change body of overridden methods use File | Settings | File Templates.

		TimerTask uiTask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						int nTab = getIntent().getIntExtra(TAB_EXTRA_NAME, -1);
						if (nTab == TAB_SHOUYE)
						{
							onSelectShouYeTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						}
						else if (nTab == TAB_DINGDAN)
						{
							onSelectDingDanTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						}
						else if (nTab == TAB_GEREN)
						{
							onSelectGeRenTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						}
						else if (nTab == TAB_FENXIANG)
						{
							onSelectFenXiangTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						}
					}
				});
			}
		};

		Timer uiTimer = new Timer();
		uiTimer.schedule(uiTask, 500);

		Log.d("erik_debug_passMian_onResume", "" + Global.isLoggedIn(this.getApplicationContext()));

		//账户及邮件查询及返回
		person_layout.setVisibility(View.GONE);
		initAccountInfo();
        CommManager.getLoginInfoFromDevToken(Global.getIMEI(getApplicationContext()),
                Global.getNotificationToken(getApplicationContext()),
                logininfo_handler);

		if (Global.isLoggedIn(getApplicationContext())) {
			CommManager.hasNews(Global.loadUserID(getApplicationContext()),
					Global.loadCityName(getApplicationContext()),
					Global.isDriverVerified(getApplicationContext()),
					Global.loadLastAnnouncementID(getApplicationContext()),
					Global.loadLastOrderNotificationID(getApplicationContext()),
					Global.loadLastPersonNotificationID(getApplicationContext()),
					Global.getIMEI(getApplicationContext()),
					has_news_handler);

			getLatestOrders();
		}

		//update driver numbers
		mLocData = new MyLocationData.Builder()
				.latitude(Global.loadLatitude(getApplicationContext()))
				.longitude(Global.loadLongitude(getApplicationContext()))
				.build();

		if(mLocData.latitude != 0.0 && mLocData.longitude != 0.0){
			getNearbyDrivers();
		}
	}


	@Override
	protected void onPause()
	{
		//Log.d("erik_debug_passMian_onPause", ""+Global.isLoggedIn(this.getApplicationContext()));
		bm_loc_client.stop();
        if(mapView != null)
		    mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		bm_loc_client.stop();
        if(mapView != null)
		    mapView.onDestroy();
		super.onDestroy();
	}


	/*
	 * 初始化activity中所有控件
	 */
	private void initControls()
	{
        spUtil = SharedPreferenceUtil.getInstance(this);
        spUtil.removeData("virtualdrivertime");
		//保存用户状态到sharePreference(BJPincheApp_info)
		Global.saveIdentify(getApplicationContext(), Global.IDENTIFY_PASSENGER());
		//下端tab功能
		initTabMgrControls();
		//中间主页界面功能
		initMainTabControls();
		//中间订单界面功能
		initOrderTabControls();
		//中间个人界面功能
		initPersonTabControls();
		//中间分享界面功能
		initShareTabControls();
        //获取版本升级信息
        getVersion();

	}

    //获取版本升级信息
    private void getVersion(){
        CommManager.getLatestAppVersion(getPackageName(), getVersionHandler);
    }
    //显示更新对话框
    private void showUpdateDialog(final String path){
        CommonAlertDialog alertDialog = new CommonAlertDialog.Builder(this)
                .type(CommonAlertDialog.DIALOGTYPE_ALERT)
                .positiveTitle("立即升级")
                .message("有新版本哦，赶快更新吧")
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        gotoDownload(path);
                    }
                })
                .build();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    //下载apk
    private void gotoDownload(final String apkurl) {
        startProgress("正在下载更新，请稍后", false);
        new Thread(){

            @Override
            public void run() {
                File apkfile = Global.downloadApk(apkurl, Utils.getDir(PassMainActivity.this), "oonew.apk");
                if(null != apkfile && 0 < apkfile.length()){
                    stopProgress();
                    installAPK(apkfile);
                }
            }

        }.start();

    }

    //自动安装一个apk文件
    private void installAPK(File file){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    private boolean isNewVersion(String serviceVersion){
        return Double.parseDouble(serviceVersion.trim()) > Double.parseDouble(Global.getCurrentVersion(this));
    }

    private AsyncHttpResponseHandler getVersionHandler = new AsyncHttpResponseHandler(){
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            Utils.mLogError("版本信息："+content);
            try{
                JSONObject json = new JSONObject(content);
                JSONObject resultJson = json.getJSONObject("result");
                int resultCode = resultJson.getInt("retcode");
                if(0 == resultCode){
                    JSONObject versionJson = resultJson.getJSONObject("retdata");
                    String versionCode = versionJson.getString("latestver");
                    String apkPath = versionJson.getString("downloadurl");
                    if(isNewVersion(versionCode)){
                        showUpdateDialog(apkPath);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

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
	 * 底部控件功能实现
	 */
	private void initTabMgrControls()
	{
		tab_shouye = (RelativeLayout)findViewById(R.id.tab_main_layout);
		tab_dingdan = (RelativeLayout)findViewById(R.id.tab_order_layout);
		tab_geren = (RelativeLayout)findViewById(R.id.tab_person_layout);
		tab_fenxiang = (RelativeLayout)findViewById(R.id.tab_share_layout);

		imgTabShouYe = (ImageView)findViewById(R.id.img_shouye);
		imgTabDingDan = (ImageView)findViewById(R.id.img_wodedingdan);
		imgTabGeRen = (ImageView)findViewById(R.id.img_person);
		imgTabFenXiang = (ImageView)findViewById(R.id.img_share);

		txtTitle = (TextView)findViewById(R.id.txt_title);
		txtTabShouYe = (TextView)findViewById(R.id.txt_shouye);
		txtTabDingDan = (TextView)findViewById(R.id.txt_wodedingdan);
		txtTabGeRen = (TextView)findViewById(R.id.txt_person);
		txtTabFenXiang = (TextView)findViewById(R.id.txt_share);

		btnTabShouYe = (ImageButton)findViewById(R.id.btn_tab_shouye);
		btnTabDingDan = (ImageButton)findViewById(R.id.btn_tab_wodedingdan);
		btnTabGeRen = (ImageButton)findViewById(R.id.btn_tab_person);
		btnTabFenXiang = (ImageButton)findViewById(R.id.btn_tab_share);

		btnTabShouYe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectShouYeTab();
			}
		});
		btnTabDingDan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectDingDanTab();
			}
		});
		btnTabGeRen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectGeRenTab();
			}
		});
		btnTabFenXiang.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectFenXiangTab();
			}
		});
	}

	/*
	 * 底部首页按钮切换功能
	 */
	public void onSelectShouYeTab()
	{
        Utils.mLogError("---------onSelectShouYeTab---------");
        txtTitle.setText(R.string.STR_MAIN_TITLE_PASSENGER);
		if (tab_shouye.getVisibility() == View.GONE) {
			if (Global.isLoggedIn(getApplicationContext())) {
				initAccountInfo();

				CommManager.hasNews(Global.loadUserID(getApplicationContext()),
						Global.loadCityName(getApplicationContext()),
						Global.isDriverVerified(getApplicationContext()),
						Global.loadLastAnnouncementID(getApplicationContext()),
						Global.loadLastOrderNotificationID(getApplicationContext()),
						Global.loadLastPersonNotificationID(getApplicationContext()),
						Global.getIMEI(getApplicationContext()),
						has_news_handler);
			} else {
				person_layout.setVisibility(View.GONE);
			}
		}

		tab_shouye.setVisibility(View.VISIBLE);
		tab_dingdan.setVisibility(View.GONE);
		tab_geren.setVisibility(View.GONE);
		tab_fenxiang.setVisibility(View.GONE);

		imgTabShouYe.setBackgroundResource(R.drawable.tab_shouye_sel);
		imgTabDingDan.setBackgroundResource(R.drawable.tab_wodedingdan_normal);
		imgTabGeRen.setBackgroundResource(R.drawable.tab_person_normal);
		imgTabFenXiang.setBackgroundResource(R.drawable.tab_share_normal);

		txtTabShouYe.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
		txtTabDingDan.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
		txtTabGeRen.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
		txtTabFenXiang.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
		//update driver numbers
		if(mLocData.latitude != 0.0 && mLocData.longitude != 0.0){
			getNearbyDrivers();
		}

		btnShare.setVisibility(View.INVISIBLE);
		btn_identify.setVisibility(View.VISIBLE);
		btn_news.setVisibility(View.VISIBLE);
		img_hasnews.setVisibility(View.GONE);
	}
	/*
	 * 底部订单按钮切换功能
	 */
	private void onSelectDingDanTab()
	{
		showNewsBadge(false);
        txtTitle.setText(R.string.STR_MAIN_TITLE_PASSENGER);
		//用户登陆检测
		if (!Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(PassMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_ORDER);
		}
		else
		{
			tab_shouye.setVisibility(View.GONE);
			tab_dingdan.setVisibility(View.VISIBLE);
			tab_geren.setVisibility(View.GONE);
			tab_fenxiang.setVisibility(View.GONE);

			imgTabShouYe.setBackgroundResource(R.drawable.tab_shouye_normal);
			imgTabDingDan.setBackgroundResource(R.drawable.tab_wodedingdan_sel);
			imgTabGeRen.setBackgroundResource(R.drawable.tab_person_normal);
			imgTabFenXiang.setBackgroundResource(R.drawable.tab_share_normal);

			txtTabShouYe.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabDingDan.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
			txtTabGeRen.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabFenXiang.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));

			if (arrOrders.isEmpty())
			{
				startProgress();
				getPagedOrders();
			}

			btnShare.setVisibility(View.INVISIBLE);
			btn_identify.setVisibility(View.INVISIBLE);
			btn_news.setVisibility(View.INVISIBLE);
			img_hasnews.setVisibility(View.INVISIBLE);
		}
	}
	/*
	 * 底部个人按钮切换功能
	 */
	private void onSelectGeRenTab()
	{
		showNewsBadge(false);
        txtTitle.setText(R.string.STR_MAIN_TITLE_PASSENGER);
		//用户登陆检测
		if (!Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(PassMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_PERSONINFO);
		}
		else
		{
			tab_shouye.setVisibility(View.GONE);
			tab_dingdan.setVisibility(View.GONE);
			tab_geren.setVisibility(View.VISIBLE);
			tab_fenxiang.setVisibility(View.GONE);

			imgTabShouYe.setBackgroundResource(R.drawable.tab_shouye_normal);
			imgTabDingDan.setBackgroundResource(R.drawable.tab_wodedingdan_normal);
			imgTabGeRen.setBackgroundResource(R.drawable.tab_person_sel);
			imgTabFenXiang.setBackgroundResource(R.drawable.tab_share_normal);

			txtTabShouYe.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabDingDan.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabGeRen.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
			txtTabFenXiang.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));

			btnShare.setVisibility(View.INVISIBLE);
			btn_identify.setVisibility(View.INVISIBLE);
			btn_news.setVisibility(View.INVISIBLE);
			img_hasnews.setVisibility(View.INVISIBLE);
		}
	}
	/*
	 * 底部分享按钮切换功能
	 */
	private void onSelectFenXiangTab()
	{
		showNewsBadge(false);
        txtTitle.setText(R.string.STR_MAIN_TITLE);
		//用户登陆检测
		if (!Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(PassMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_SHARE);
		}
		else
		{
//			if (szDefContent1 == null)
//				getDefaultShareContents();
//            shareWebView.loadUrl("file:///android_asset/5.html");
//            shareWebView.loadUrl("http://182.92.237.144:9082/bk/index/activity_appFx.do?activityDto.userId="
//                    +Global.loadUserID(getApplicationContext()));
            shareWebView.loadUrl("http://182.92.237.144:8082/bk/index/activity_appFx.do?activityDto.userId="
                    +Global.loadUserID(getApplicationContext()));
            Utils.mLogError("1加载："+shareWebView.getUrl());
			btnShare.setVisibility(View.VISIBLE);
			btn_identify.setVisibility(View.INVISIBLE);
			btn_news.setVisibility(View.INVISIBLE);
			img_hasnews.setVisibility(View.INVISIBLE);

			tab_shouye.setVisibility(View.GONE);
			tab_dingdan.setVisibility(View.GONE);
			tab_geren.setVisibility(View.GONE);
			tab_fenxiang.setVisibility(View.VISIBLE);

			imgTabShouYe.setBackgroundResource(R.drawable.tab_shouye_normal);
			imgTabDingDan.setBackgroundResource(R.drawable.tab_wodedingdan_normal);
			imgTabGeRen.setBackgroundResource(R.drawable.tab_person_normal);
			imgTabFenXiang.setBackgroundResource(R.drawable.tab_share_sel);

			txtTabShouYe.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabDingDan.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabGeRen.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			txtTabFenXiang.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
		}
	}


	private void getDefaultShareContents(String inputUrl, int shareFlag)
	{
		if (Global.isLoggedIn(getApplicationContext()))
		{
            Utils.mLogError("inputUrl="+inputUrl);
			CommManager.defaultShareContents(Global.loadUserID(getApplicationContext()),
                    inputUrl, shareFlag, Global.getIMEI(getApplicationContext()),
					shareContents_handler);
		}
	}


	private AsyncHttpResponseHandler shareContents_handler = new AsyncHttpResponseHandler()
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

                    outputUrl = retdata.getString("output_url");
                    outputContent = retdata.getString("output_content");
//                    outputContent = outputContent.replace("$invitecode",
//                            Global.loadInviteCode(getApplicationContext()));

                    outputUrl = outputUrl.replace("${activityDto.faqiId}",Global.loadUserID(getApplicationContext())+"");
                    setShareContent(outputUrl, outputContent);
                    stopProgress();
                    showShareActionSheet();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassMainActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


    private void setShareContent(String outputUrl, String outputContent)
    {
        if (outputUrl == null || outputContent == null)
            return;
        Log.d(TAG,"outputUrl:"+outputUrl);
        String title = "";
        String content = "";
        if(outputContent.equals(getResources().getString(R.string.MAIN_DOWNLOAD_CONTENT))){
            title = getResources().getString(R.string.MAIN_DOWNLOAD_TITLE);
            content = getResources().getString(R.string.MAIN_DOWNLOAD_CONTENT);
        }else{
            title = getResources().getString(R.string.SHARE_TITLE);
            content = getResources().getString(R.string.SHARE_CONTENT);
        }
        // 配置SSO
        mShareController.getConfig().setSsoHandler(new SinaSsoHandler());
        mShareController.getConfig().setSinaCallbackUrl("http://www.ookuaipin.com");


        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(PassMainActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(PassMainActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        mShareController.setShareContent(content);			   // Default share contents

        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
//        String appId = "wx2bdbd44d3a1cb0bc";//测试
//        String appSecret = "cd31402fb8ba512cead1a88186d14858";测试
        String appId = "wx8a669036953334b3";
        String appSecret = "4bf6b20eec181a8af8479ca4514b9291";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(PassMainActivity.this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(PassMainActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();

//		UMImage urlImage = new UMImage(PassMainActivity.this,
//				"http://www.umeng.com/images/pic/home/social/img-1.png");
        UMImage urlImage = new UMImage(PassMainActivity.this, R.drawable.icon);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(outputUrl);
        weixinContent.setShareMedia(urlImage);
        mShareController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(outputUrl);
        mShareController.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(outputUrl);
        qzone.setTitle(title);
        qzone.setShareImage(urlImage);
        mShareController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent
                .setShareImage(urlImage);
        qqShareContent.setTargetUrl(outputUrl);
        mShareController.setShareMedia(qqShareContent);


        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(content);
        sms.setShareImage(urlImage);
        mShareController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent(urlImage);
        sinaContent.setShareContent(content);
        mShareController.setShareMedia(sinaContent);

        mShareController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
    }



	/**
	 * 定位接口，需要实现两个方法
	 *
	 * 异步接受百度查询数据
	 */
	public class BDLocationListenerImpl implements BDLocationListener {

		/**
		 * 接收异步返回的定位结果，参数是BDLocation类型参数
		 */
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null || location.getAddrStr() == null) {
				return;
			}

			Global.saveCoordinates(getApplicationContext(), location.getLatitude(), location.getLongitude());
			Global.saveCityName(getApplicationContext(), location.getCity());
			Global.saveDetAddress(getApplicationContext(), location.getAddrStr());

			PassMainActivity.this.location = location;

			mLocData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).accuracy(location.getRadius()).direction(location.getDirection()).build();
			baiduMap.setMyLocationData(mLocData);
			if (!isInit) {
				MapStatus status = new MapStatus.Builder().zoom(13).target(new LatLng(location.getLatitude(), location.getLongitude())).build();
				baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(status));
				isInit = true;
			}
			//move to location position
			LatLng point = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(point);
			baiduMap.animateMapStatus(update);
			showPopupOverlay(location);
			bm_loc_client.stop();
		}
	}

	/**
	 * 显示弹出窗口图层PopupOverlay
	 * @param location
	 */
	private void showPopupOverlay(BDLocation location){
		//定义User Maker坐标点
		point = new LatLng(location.getLatitude(),
				location.getLongitude());
		//构建Marker图标
		//ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(mPopupView));
		//构建MarkerOption，用于在地图上添加Marker
		option = new MarkerOptions()
				.position(point)
				.zIndex(5)
				.icon(bitmap);
		//在地图上添加Marker，并显示
		//将marker添加到地图上
		mainMarker = (Marker) (baiduMap.addOverlay(option));
		//binder listeners
		baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if(marker == mainMarker){
					Log.d(TAG,"location tip appear..................");
					mainMarker.remove();

					if(PassMainActivity.this.location == null){
						popText.setText("");
					}else{
						popText.setText(PassMainActivity.this.location.getAddrStr());
					}
					popText.setVisibility(View.VISIBLE);
					//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
					InfoWindow mInfoWindow = new InfoWindow(mPopupView, point, 0);
					//显示InfoWindow
					baiduMap.showInfoWindow(mInfoWindow);
				}
				return true;
			}
		});
		baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
			public void onMapStatusChangeStart(MapStatus status) {
				baiduMap.clear();
				if (popText != null)
					popText.setVisibility(View.GONE);
				point = new LatLng(PassMainActivity.this.location.getLatitude(), PassMainActivity.this.location.getLongitude());
				//构建Marker图标
				//ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(mPopupView));
				//构建MarkerOption，用于在地图上添加Marker
				option = new MarkerOptions()
						.position(point)
						.zIndex(5)
						.icon(bitmap);
				//在地图上添加Marker，并显示
				//将marker添加到地图上
				mainMarker = (Marker) (baiduMap.addOverlay(option));
				displayDriverLocations();
			}

			public void onMapStatusChangeFinish(MapStatus status) {

			}

			public void onMapStatusChange(MapStatus status) {

			}
		});
	}

	/**
	 * 将View转换成Bitmap的方法
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}


	private void initMainTabControls()
	{
        mContainer = (ViewGroup)findViewById(R.id.content_layout);
        btn_identify = (ImageButton)findViewById(R.id.btn_switch_identify);
        btn_identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToDriverMainActivity();
            }
        });
        applyRotation(-1, 90, 180);
		//location button
		locationButton = (ImageButton)findViewById(R.id.locate_btn);
		locationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				bm_loc_client.start();
//				mMapController.setZoom(15);
				baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(13));
                getNearbyDrivers();
			}
		});

		mapView = (MapView)findViewById(R.id.img_map);
		mapView.showScaleControl(false);
		mapView.showZoomControls(false);

		baiduMap = mapView.getMap();
		mPopupView = LayoutInflater.from(this).inflate(R.layout.pop_view, null);
		ResolutionSet.instance.iterateChild(mPopupView, mScrSize.x, mScrSize.y);
		popText = (TextView)mPopupView.findViewById(R.id.location_tips);

		//showPopupOverlay(location);

		//自动定位
		autoLocate();

		//信息按钮功能
		btn_news = (ImageButton)findViewById(R.id.btn_news);
		btn_news.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToNewsActivity();
			}
		});

		img_hasnews = (ImageView)findViewById(R.id.img_has_news);

		person_layout = (RelativeLayout)findViewById(R.id.person_verify_layout);
		//身份验证按钮功能实现
		btn_verify_person = (Button)findViewById(R.id.btn_person_verify);
		btn_verify_person.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToVerifyPerson();
			}
		});

		//长途抢座按钮功能实现
		btn_long_order = (Button)findViewById(R.id.btn_check_triporder);
		btn_long_order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToLongOrderActivity();
			}
		});
		//室内发单按钮功能实现
		btn_city_order = (Button)findViewById(R.id.btn_send_cityorder);
		btn_city_order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToCityOrderActivity();
			}
		});
		//driver number UI
		driverNum = (TextView)findViewById(R.id.notification);
		
	}



	/*
	 *开始定位
	 */
	private void autoLocate() {
		baiduMap.setMyLocationEnabled(true);
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(13));
		//实例化定位服务，LocationClient类必须在主线程中声明
		bm_loc_client = new LocationClient(PassMainActivity.this);
		bm_loc_client.registerLocationListener(new BDLocationListenerImpl());//注册定位监听接口

		/**
		 * LocationClientOption 该类用来设置定位SDK的定位方式。
		 */
		LocationClientOption option = new LocationClientOption();
		bm_loc_client.setLocOption(option);  //设置定位参数

		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);

		bm_loc_client.start();  // 调用此方法开始定位
	}

	/*
	 * check login or not
	 */
	private void checkLogin(Class targetClass, int targetCode, int targetCode2){
		//Log.d("erik_debug_passMian", ""+Global.isLoggedIn(this.getApplicationContext()));
		if (Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(PassMainActivity.this, targetClass);
			if (targetClass.equals(NewsActivity.class))
			{
				intent.putExtra("announcements", announce_count);
				intent.putExtra("ordernotif_count", order_notif_count);
				intent.putExtra("personnotif_count", person_notif_count);
			}
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, targetCode2);
		}
		else
		{
			Intent intent = new Intent(PassMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, targetCode);
		}
	}
	/*
	 * 身份转换activity
	 */
	private void moveToDriverMainActivity() {
		Intent intent = new Intent(PassMainActivity.this, DrvMainActivity.class);
		startActivity(intent);
//		finish();
	}
    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end) {
        btn_identify.setClickable(false);
        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(2000);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        mContainer.startAnimation(rotation);
    }

    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }
    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            Rotate3dAnimation rotation;

//            if (mPosition > -1) {
//                mPhotosList.setVisibility(View.GONE);
//                mImageView.setVisibility(View.VISIBLE);
//                mImageView.requestFocus();
//
//                rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 310.0f, false);
//            } else {
//                mImageView.setVisibility(View.GONE);
//                mPhotosList.setVisibility(View.VISIBLE);
//                mPhotosList.requestFocus();
//
//                rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
//            }
            rotation = new Rotate3dAnimation(180, 0, centerX, centerY, 310.0f, false);
            rotation.setDuration(1000);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            rotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    btn_identify.setClickable(true);
                    if(!initComplete) startProgress();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            mContainer.startAnimation(rotation);
        }
    }

    /*
	 * 跳转信息activity
	 */
	private void moveToNewsActivity()
	{
		//Log.d("erik_debug_passMian", ""+Global.isLoggedIn(this.getApplicationContext()));
		//是否登陆，若登陆就到news，没有登陆到登陆界面
		checkLogin(NewsActivity.class, REQCODE_NEWS, REQCODE_NEWS2);

	}
	/*
	 * 跳转身份验证activity
	 */
	private void moveToVerifyPerson()
	{
		//是否登陆，若登陆就到身份验证，没有登陆到登陆界面
		checkLogin(VerifyPersonActivity.class, REQCODE_VERIFY_PERSON, REQCODE_VERIFY_PERSON2);
	 }
	/*
	 * 跳转长途抢座activity
	 */
	private void moveToLongOrderActivity() {
		//是否登陆，若登陆就到long order，没有登陆到登陆界面
		checkLogin(PassLongOrderMainActivity.class, REQCODE_LONG_ORDER, REQCODE_LONG_ORDER2);
	}
	/*
	 * 跳转同城快拼activity
	 */
	private void moveToCityOrderActivity() {
		checkLogin(PassPubCityOrderActivity.class, REQCODE_CITY_ORDER, REQCODE_CITY_ORDER2);
	}
	/*
	 * 跳转同城发单activity
	 */
	private void moveToOnceOrderActivity() {
		checkLogin(PassPubCityOrderActivity.class, REQCODE_ONCE_ORDER, REQCODE_ONCE_ORDER2);
	}
	/*
	 * 恢复应用时状态匹配
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);	//To change body of overridden methods use File | Settings | File Templates.
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mShareController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_VERIFY_PERSON) {
			getLatestOrders();
			moveToVerifyPerson();
		} else if (requestCode == REQCODE_LONG_ORDER) {
			getLatestOrders();
			moveToLongOrderActivity();
		} else if (requestCode == REQCODE_CITY_ORDER) {
			getLatestOrders();
			moveToCityOrderActivity();
		} else if (requestCode == REQCODE_ONCE_ORDER ) {
			getLatestOrders();
			moveToOnceOrderActivity();
		} else if (requestCode == REQCODE_TAB_ORDER ||
				requestCode == REQCODE_CITY_ORDER2) {
			getLatestOrders();
			onSelectDingDanTab();
		} else if (requestCode == REQCODE_TAB_PERSONINFO) {
			getLatestOrders();
			onSelectGeRenTab();
		} else if (requestCode == REQCODE_TAB_SHARE) {
			getLatestOrders();
			onSelectFenXiangTab();
		} else if (requestCode == REQCODE_LONG_ORDERDETAIL) {
			long orderid = data.getLongExtra("orderid", 0);
			updateOrderInfo(orderid, ConstData.ORDER_TYPE_LONG);
		} else if (requestCode == REQCODE_ONCE_ORDERDETAIL) {
			long orderid = data.getLongExtra("orderid", 0);
			updateOrderInfo(orderid, ConstData.ORDER_TYPE_ONCE);
		}
//		else if (requestCode == REQCODE_ONOFF_ORDERDETAIL)
//		{
//			long orderid = data.getLongExtra("orderid", 0);
//			updateOrderInfo(orderid, ConstData.ORDER_TYPE_ONOFF);
//		}
		else if (requestCode == REQCODE_PAY_ORDER ||
				requestCode == REQCODE_EVALUATE) {
			getLatestOrders();
		}
	}
	/*
	 *订单界面及功能实现
	 */
	private void initOrderTabControls()
	{
		imgAll = (ImageView) findViewById(R.id.imgAll);
		imgAll.setOnClickListener(onClickListener);
		lblAll = (TextView) findViewById(R.id.lblAll);
		lblAll.setOnClickListener(onClickListener);
		imgReqOpr = (ImageView) findViewById(R.id.imgReqOpr);
		imgReqOpr.setOnClickListener(onClickListener);
		lblReqOpr = (TextView) findViewById(R.id.lblReqOpr);
		lblReqOpr.setOnClickListener(onClickListener);
		imgOnce = (ImageView) findViewById(R.id.imgOnce);
		imgOnce.setOnClickListener(onClickListener);
		lblOnce = (TextView) findViewById(R.id.lblOnce);
		lblOnce.setOnClickListener(onClickListener);
//		imgCommute = (ImageView) findViewById(R.id.imgCommute);
//		imgCommute.setOnClickListener(onClickListener);
//		lblCommute = (TextView) findViewById(R.id.lblCommute);
//		lblCommute.setOnClickListener(onClickListener);
		imgLongDist = (ImageView) findViewById(R.id.imgLongDist);
		imgLongDist.setOnClickListener(onClickListener);
		lblLongDist = (TextView) findViewById(R.id.lblLongDist);
		lblLongDist.setOnClickListener(onClickListener);

		orderListView = (PullToRefreshListView)findViewById(R.id.viewData);
		orderAdapter = new OrderAdapter(PassMainActivity.this, arrOrders);
		{
			orderListView.setMode(PullToRefreshBase.Mode.BOTH);
			orderListView.setOnRefreshListener(orderListListener);
			orderListView.setAdapter(orderAdapter);
			orderListView.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFF1F1F1")));
			orderListView.getRefreshableView().setCacheColorHint(Color.parseColor("#FFF1F1F1"));
		}
		lblNoOrders = (TextView) findViewById(R.id.lblNoOrders);
	}
	/*
	 *个人中心功能及实现
	 */
	private void initPersonTabControls()
	{
		btnPersonInfo = (ImageButton)findViewById(R.id.btn_wodegerenxinxi);
		btnRouteSetting = (ImageButton)findViewById(R.id.btn_route_setting);
		btnMoney = (ImageButton)findViewById(R.id.btn_wodejianpailvdian);
		btnCoupon = (ImageButton)findViewById(R.id.btn_wodedianquan);
		btnStrategy = (ImageButton)findViewById(R.id.btn_pinche_stratigy);
		btnAbout = (ImageButton)findViewById(R.id.btn_guanyuoo);
		btnLogout = (Button)findViewById(R.id.btn_logout);
		//我的个人信息
		btnPersonInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PassMainActivity.this, PassPersonInfoActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		//设置长途线路
		btnRouteSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PassMainActivity.this, PassLongRouteMainActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		//我的减排绿点
		btnMoney.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PassMainActivity.this, BalanceActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		//我的点券
		btnCoupon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PassMainActivity.this, CouponActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		//拼车攻略
		btnStrategy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PassMainActivity.this, StrategyActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		//关于软件
		btnAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PassMainActivity.this, AboutActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		//退出软件
		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonAlertDialog dialog = new CommonAlertDialog.Builder(PassMainActivity.this)
						.message(getResources().getString(R.string.STR_CONFIRM_LOGOUT))
						.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
						.positiveListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								startProgress();
								CommManager.logoutUser(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), logout_handler);
							}
						})
						.build();
				dialog.show();
			}
		});
	}
	/*
	 *分享功能及实现
	 */
	private void initShareTabControls()
	{
		shareWebView = (WebView)findViewById(R.id.share_webview);
        shareWebView.getSettings().setJavaScriptEnabled(true);

//        shareWebView.loadUrl("http://www.baidu.com");
        shareWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Utils.mLogError("加载："+url);
                view.loadUrl(url);
                return true;
            }
        });
		btnShare = (ImageButton)findViewById(R.id.btn_share);
		btnShare.setVisibility(View.INVISIBLE);
		btnShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                getShareInfo();
			}
		});
	}

    private void getShareInfo() {
        startProgress();
        String originUrl = shareWebView.getUrl();
        Utils.mLogError("originUrl："+originUrl);
        String[] urlList = originUrl.split(".do");
        for (int i = 0; i < urlList.length; i++){
            Utils.mLogError(i+":URLsplit："+urlList[i]);
        }
        getDefaultShareContents(urlList[0]+".do", 0);
    }


    private void showShareActionSheet() {
  		if (outputUrl != null) {
            mShareController.openShare(PassMainActivity.this, share_listener);
		} else {
			Global.showAdvancedToast(PassMainActivity.this,
					getResources().getString(R.string.STR_CONN_ERROR),
					Gravity.CENTER);
		}
	}

	private SocializeListeners.SnsPostListener share_listener = new SocializeListeners.SnsPostListener() {
		@Override
		public void onStart() {
			SHARE_MEDIA media = mShareController.getConfig().getSelectedPlatfrom();
			if (media == SHARE_MEDIA.QZONE ||
					media == SHARE_MEDIA.QQ) {
				if (!Global.IsQQInstalled(getApplicationContext())) {
					Global.showTextToast(PassMainActivity.this, getResources().getString(R.string.STR_QQ_NOTINSTALLED));
					return;
				}
			}
		}

		@Override
		public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
		}
	};

	private void updateOrderInfo(long orderid, int ordertype)
	{
		if (orderid <= 0)
			return;

		mCurSelOrderId = orderid;
		mCurSelOrderType = ordertype;

		startProgress();
		CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
				orderid, ordertype, Global.getIMEI(getApplicationContext()), detailinfo_handler);
	}

	/*
	 * get drivers locations
	 */
	public class DriverLocation{
		int driverId;
		double lng;
		double lat;
        public int getDriverId(){
            return driverId;
        }
        public double getLng(){
            return lng;
        }
        public double getLat(){
            return lat;
        }
	}

	private void getNearbyDrivers() {
        Utils.mLogError("-------getNearbyDrivers-----"+ Market.CHANNEL_FLAG);
		//for test
		//mLocData.latitude = 0.0;
		//mLocData.longitude = 0.0;
		CommManager.getNearbyDrivers(Global.loadUserID(getApplicationContext()),
				mLocData.latitude, mLocData.longitude,
				Global.getIMEI(getApplicationContext()), driverListHandler);
	}

	private AsyncHttpResponseHandler driverListHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
//			Log.d("erik_debug_passMian_onSuccess",
//					""+Global.isLoggedIn(PassMainActivity.this.getApplicationContext()));
			//Log.d(TAG, "success..................");
			super.onSuccess(statusCode, content);
			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");

				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");
				Log.d(TAG,"nRetcode------>"+nRetcode);
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = jsonResult.getJSONArray("retdata");
					Log.d(TAG, "retdata------>" + retdata);
					driverLocations.clear();
					for(int i=0; i<retdata.length();i++){
						JSONObject retItem = retdata.getJSONObject(i);
						DriverLocation driverLocation = new DriverLocation();
						driverLocation.driverId = retItem.getInt("driverid");
						driverLocation.lat = retItem.getDouble("lat");
						driverLocation.lng = retItem.getDouble("lng");
						driverLocations.add(driverLocation);
					}
                    if(System.currentTimeMillis() - spUtil.getLong("virtualdrivertime",0) >= 3600 * 1000){
                        spUtil.saveLong("virtualdrivertime",System.currentTimeMillis());
                        driverLocationsVirtual.clear();
                        for(int i = 0; i < 20; i++){
                            DriverLocation driverLocation = new DriverLocation();
                            driverLocation.driverId = 9999+i;
                            double lt = Math.random() > 0.5 ? Math.random() : -1 * Math.random();
                            double lg = Math.random() < 0.5 ? Math.random() : -1 * Math.random();
                            driverLocation.lat = lt * 9 * 0.009;
                            driverLocation.lng = lg * 9 * 0.009;
                            driverLocationsVirtual.add(driverLocation);
                        }
                    }
                    for(int i = 0; i < driverLocationsVirtual.size(); i++){
                        DriverLocation driverLocation = new DriverLocation();
                        driverLocation.driverId = driverLocationsVirtual.get(i).getDriverId();
                        driverLocation.lat = mLocData.latitude + driverLocationsVirtual.get(i).getLat();
                        driverLocation.lng = mLocData.longitude +driverLocationsVirtual.get(i).getLng();
                        driverLocations.add(driverLocation);
                    }
					displayDriverLocations();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(jsonMsg);
				}
				else
				{
//					Global.showAdvancedToast(PassMainActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void displayDriverLocations() {
        Utils.mLogError("-------displayDriverLocations---");
		driverNum.setText(""+driverLocations.size());
        baiduMap.clear();

        point = new LatLng(PassMainActivity.this.location.getLatitude(), PassMainActivity.this.location.getLongitude());
        //构建Marker图标
        //ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(mPopupView));
        //构建MarkerOption，用于在地图上添加Marker
        option = new MarkerOptions()
                .position(point)
                .zIndex(5)
                .icon(bitmap1);
        baiduMap.addOverlay(option);


		//define driver markers
		for(DriverLocation location : driverLocations){
			//定义User Maker坐标点
			LatLng point = new LatLng(location.lat, location.lng);
			//构建Marker图标
			//ImageView mainLocation = (ImageView)mPopupView.findViewById(R.id.main_location);
			View driverView = LayoutInflater.from(this).inflate(R.layout.driver_pop_view, null);
			ResolutionSet.instance.iterateChild(driverView, mScrSize.x, mScrSize.y);
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(driverView);
			//构建MarkerOption，用于在地图上添加Marker
			option = new MarkerOptions()
					.position(point)
					.zIndex(1)
					.icon(bitmap);
			//在地图上添加Marker，并显示
			//将marker添加到地图上
			baiduMap.addOverlay(option);
		}
	}
	/*
	 *通过手机IMEI号查询用户信息
	 */

	private void initAccountInfo()
	{
		startProgress();
		CommManager.getLoginInfoFromDevToken(Global.getIMEI(getApplicationContext()),
				Global.getNotificationToken(getApplicationContext()),
				logininfo_handler);
	}
	/*
	 *通过手机IMEI号查询用户信息后得到信息并部署
	 */

	private AsyncHttpResponseHandler logininfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
//			Log.d("erik_debug_passMian_onSuccess",
//					""+Global.isLoggedIn(PassMainActivity.this.getApplicationContext()));
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            initComplete = true;

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				Log.d(TAG, "logininfo result----->"+result);
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					//得到用户数据
					JSONObject retdata = result.getJSONObject("retdata");
					STUserInfo userinfo = STUserInfo.decodeFromJSON(retdata);

					//存储用户数据
					Global.saveUserID(getApplicationContext(), userinfo.userid);
					Global.saveInviteCode(getApplicationContext(), userinfo.invitecode);
					Global.savePersonVerified(getApplicationContext(), userinfo.person_verified == 1);
                    Global.savePersonVerfiedWait(getApplicationContext(), userinfo.person_verified == 2);
					Global.saveBaiduApiKey(getApplicationContext(), userinfo.baiduak);

					//验证ui可见
					if (userinfo.person_verified == 0)			  // Not verified and not required to review
						person_layout.setVisibility(View.VISIBLE);
					else
						person_layout.setVisibility(View.GONE);

					//查询用户邮件数据
					CommManager.hasNews(Global.loadUserID(getApplicationContext()),
							Global.loadCityName(getApplicationContext()),
							Global.isDriverVerified(getApplicationContext()),
							Global.loadLastAnnouncementID(getApplicationContext()),
							Global.loadLastOrderNotificationID(getApplicationContext()),
							Global.loadLastPersonNotificationID(getApplicationContext()),
							Global.getIMEI(getApplicationContext()),
							has_news_handler);

					checkPushResult();
				}
				else
				{
					Global.clearUserInfo(getApplicationContext());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.d(TAG, "exception------>"+ex.getMessage());
				Global.clearUserInfo(getApplicationContext());
			}
//			Log.d("erik_debug_passMian_onSuccess",
//					""+Global.isLoggedIn(PassMainActivity.this.getApplicationContext()));
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            initComplete = true;
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void checkPushResult()
	{
		try
		{
			String pushResult = getIntent().getStringExtra("pushresult");
			if (TextUtils.isEmpty(pushResult))
				return;

			getIntent().removeExtra("pushresult");

			JSONObject customJson = null;
			try {
				customJson = new JSONObject(pushResult);

				int typecode = customJson.getInt("typecode");
				long orderid = customJson.getLong("orderid");
				int ordertype = customJson.getInt("ordertype");

				switch (typecode)
				{
//					case ConstData.PUSH_PASS_ONOFF_CONFIRM:
//					{
//						Intent intentAgree = new Intent(PassMainActivity.this, PassOnOffOrderConfirmActivity.class);
//						intentAgree.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//						intentAgree.putExtra("orderid", orderid);
//						intentAgree.putExtra("order_type", ordertype);
//						PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//						startActivityForResult(intentAgree, REQCODE_ONOFF_ORDERDETAIL);
//					}
					case ConstData.PUSH_PASS_ONOFF_TO_ONCE:
					{
						moveToOnceOrderActivity();
					}
					case ConstData.PUSH_PASS_LONG_CONFIRM:
					{
						Intent intentDetail = new Intent(PassMainActivity.this, PassLongOrderDetailActivity.class);
						intentDetail.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
						intentDetail.putExtra("orderid", orderid);
						PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
						startActivityForResult(intentDetail, REQCODE_LONG_ORDERDETAIL);
					}
					case ConstData.PUSH_DRV_ONOFF_CONFIRM:
					case ConstData.PUSH_DRV_ORDER_DETAIL:
					case ConstData.PUSH_EXTRA:
					default:
					{
						break;
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	//查询用户邮件结果及部署
	private AsyncHttpResponseHandler has_news_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);	//To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");
                Log.d(TAG, "has_news_handler jsonResult--->"+ jsonResult);
				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = jsonResult.getJSONObject("retdata");

					announce_count = retdata.getInt("announcement");
					order_notif_count = retdata.getInt("ordernotif");
					person_notif_count = retdata.getInt("personnotif");

					if (announce_count > 0 || order_notif_count > 0 || person_notif_count > 0)
						showNewsBadge(true);
					else
						showNewsBadge(false);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(jsonMsg);
				}
				else
				{
//					Global.showAdvancedToast(PassMainActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler correct_google_listener = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.

			try
			{
				JSONObject jsonObj = new JSONObject(content);

				int nRetCode = jsonObj.getInt("error");
				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					String x_base64 = jsonObj.getString("x");
					String y_base64 = jsonObj.getString("y");

					String xStr = new String(Base64.decode(x_base64, Base64.DEFAULT), "UTF-8");
					String yStr = new String(Base64.decode(y_base64, Base64.DEFAULT), "UTF-8");

					double fLng = Double.parseDouble(xStr);
					double fLat = Double.parseDouble(yStr);

					Global.saveCoordinates(getApplicationContext(), fLat, fLng);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	/*
	 * 初始化baidu变量
	 */
	private void initBaidu()
	{
		SDKInitializer.initialize(getApplicationContext());
		//initLocationManager();
		//initSearchManager();
	}

	//useless methods
	/*
	 * 初始化baidu 定位功能
	 */
	private void initLocationManager()
	{
		bm_loc_client = new LocationClient(PassMainActivity.this);

	}


	private class STOrderViewHolder
	{
		TextView lblAddr = null;
		TextView lblPrice = null;
		TextView lblOrderType = null;
		TextView lblDate = null;
		TextView lblTime = null;
		TextView lblOper = null;
		ImageView imgOper = null;
		ImageButton btnOper = null;
		ImageButton btn_background = null;
		ImageButton btn_photo = null;
		TextView txtState = null;
		SmartImageView drvPhoto = null;
		ImageView imgGender = null;
		TextView lblAge = null;
		TextView lblName = null;
	}


	private class OrderAdapter extends ArrayAdapter<STPsgOrderListItem>
	{
		public OrderAdapter(Context context, List<STPsgOrderListItem> objects) {
			super(context, 0, objects);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			STPsgOrderListItem orderItem = arrOrders.get(position);
			STOrderViewHolder holder = null;
			if (convertView == null)
			{
				int nWidth = 460, nHeight = ViewGroup.LayoutParams.WRAP_CONTENT, nYMargin = 10;
				RelativeLayout itemLayout = new RelativeLayout(orderListView.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
				itemLayout.setLayoutParams(layoutParams);

				RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_pass_myorderlistitem, null);
				RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
				RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
				item_layoutParams.setMargins(0, nYMargin, 0, nYMargin);
				item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				itemView.setLayoutParams(item_layoutParams);
				itemLayout.addView(itemView);

				ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

				convertView = itemLayout;

				holder = new STOrderViewHolder();
				convertView.setTag(holder);
			}
			else
			{
				holder = (STOrderViewHolder)convertView.getTag();
			}


			// Set address
			TextView lblAddr = null;
			if (holder.lblAddr == null)
				holder.lblAddr = (TextView)convertView.findViewById(R.id.lblDest);
			lblAddr = holder.lblAddr;
			// check order type & chnage text
			if (orderItem.type == ConstData.ORDER_TYPE_LONG)
			{
				lblAddr.setText(orderItem.start_city + "-" + orderItem.end_city);
			}
			else
			{
				lblAddr.setText(orderItem.start_addr + "-" + orderItem.end_addr);
			}

			// Set price
			TextView lblPrice = null;
			if (holder.lblPrice == null)
				holder.lblPrice = (TextView)convertView.findViewById(R.id.lblMark);
			lblPrice = holder.lblPrice;
			lblPrice.setText(orderItem.price + getResources().getString(R.string.STR_BALANCE_DIAN));


			// Set order type
			TextView lblOrderType = null;
			if (holder.lblOrderType == null)
				holder.lblOrderType = (TextView)convertView.findViewById(R.id.lblSiteMark);
			lblOrderType = holder.lblOrderType;
			lblOrderType.setText(orderItem.type_desc);


			// Set Date
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


			// Set operation text
			TextView lblOper = null;
			ImageView imgOper = null;
			ImageButton btnOper = null;

			ImageButton btn_background = null;
			ImageButton btn_photo = null;

			if (holder.lblOper == null)
			{
				holder.lblOper = (TextView)convertView.findViewById(R.id.lblState);
				holder.imgOper = (ImageView)convertView.findViewById(R.id.imgState);
				holder.btnOper = (ImageButton)convertView.findViewById(R.id.btn_operate);
				holder.btn_background = (ImageButton)convertView.findViewById(R.id.btn_background);
				holder.btn_photo = (ImageButton)convertView.findViewById(R.id.btnPhoto);
			}

			lblOper = holder.lblOper;
			imgOper = holder.imgOper;
			imgOper.setImageResource(R.drawable.yellowcircle);
			btnOper = holder.btnOper;
			btn_background = holder.btn_background;
			btn_photo = holder.btn_photo;

			final int nState = orderItem.state;
			switch (nState)
			{
				case ConstData.ORDER_STATE_DRV_ACCEPTED:
				case ConstData.ORDER_STATE_PUBLISHED:
				case ConstData.ORDER_STATE_GRABBED:
				case ConstData.ORDER_STATE_STARTED:
				case ConstData.ORDER_STATE_DRV_ARRIVED:
				case ConstData.ORDER_STATE_PASS_GETON:
				{
					lblOper.setVisibility(View.INVISIBLE);
					imgOper.setVisibility(View.INVISIBLE);
					btnOper.setVisibility(View.INVISIBLE);
					break;
				}
				case ConstData.ORDER_STATE_FINISHED:
				{
					lblOper.setVisibility(View.VISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.VISIBLE);
					lblOper.setText(getResources().getString(R.string.STR_QUZHIFU));
					break;
				}
				case ConstData.ORDER_STATE_PAYED:
				{
					lblOper.setVisibility(View.VISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.VISIBLE);
					lblOper.setText(getResources().getString(R.string.STR_QUPINGJIA));
					break;
				}
				case ConstData.ORDER_STATE_EVALUATED:
				{
					lblOper.setVisibility(View.INVISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.INVISIBLE);
//					lblOper.setText(orderItem.evaluated_desc);
					switch (orderItem.evaluated)
					{
						case ConstData.EVALUATE_GOOD:
							imgOper.setImageResource(R.drawable.btn_goodeval);
							break;
						case ConstData.EVALUATE_NORMAL:
							imgOper.setImageResource(R.drawable.btn_normaleval);
							break;
						case ConstData.EVALUATE_BAD:
							imgOper.setImageResource(R.drawable.btn_badeval);
							break;
					}
					break;
				}
				case ConstData.ORDER_STATE_CLOSED:
				{
					lblOper.setVisibility(View.VISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.INVISIBLE);
					lblOper.setText(orderItem.state_desc);
					break;
				}
				case ConstData.ORDER_STATE_CANCELLED: {
					lblOper.setVisibility(View.VISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.INVISIBLE);
					lblOper.setText(orderItem.state_desc);
					break;
				}
			}


			// Set state text
			TextView txtState = null;
			if (holder.txtState == null)
				holder.txtState = (TextView) convertView.findViewById(R.id.lblProcess);
			txtState = holder.txtState;
			txtState.setText(orderItem.state_desc);

			// Set passenger information
			SmartImageView drvPhoto = null;
			if (holder.drvPhoto == null)
				holder.drvPhoto = (SmartImageView)convertView.findViewById(R.id.imgPhoto);
			drvPhoto = holder.drvPhoto;
			drvPhoto.isCircular = true;
			drvPhoto.setImage(new SmartImage() {
				@Override
				public Bitmap getBitmap(Context context) {
					return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
				}
			});

			ImageView imgGender = null;
			TextView lblAge = null;
			TextView lblName = null;

			if (holder.imgGender == null) {
				holder.imgGender = (ImageView) convertView.findViewById(R.id.imgSex);
				holder.lblAge = (TextView)convertView.findViewById(R.id.lblAge);
				holder.lblName = (TextView)convertView.findViewById(R.id.lblName);
			}

			imgGender = holder.imgGender;
			lblAge = holder.lblAge;
			lblName = holder.lblName;

			if (orderItem.driver_id <= 0)
			{
//				drvPhoto.setVisibility(View.INVISIBLE);
				imgGender.setVisibility(View.INVISIBLE);
				lblAge.setVisibility(View.INVISIBLE);
				lblName.setVisibility(View.INVISIBLE);
				btn_photo.setVisibility(View.VISIBLE);
				btn_photo.setEnabled(false);
			}
			else
			{
				drvPhoto.setImageUrl(orderItem.driver_img, R.drawable.icon_appprice_over);
				drvPhoto.setVisibility(View.VISIBLE);

				imgGender.setImageResource(orderItem.driver_gender == ConstData.GENDER_MALE ? R.drawable.bk_manmark : R.drawable.bk_womanmark);
				imgGender.setVisibility(View.VISIBLE);

				if (orderItem.driver_gender == ConstData.GENDER_MALE)
					lblAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
				else
					lblAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
				lblAge.setText("" + orderItem.driver_age);
				lblAge.setVisibility(View.VISIBLE);

				lblName.setText(orderItem.driver_name);
				lblName.setVisibility(View.VISIBLE);

				btn_photo.setVisibility(View.VISIBLE);
				btn_photo.setEnabled(true);
			}


			final long driverid = orderItem.driver_id;
			final long orderid = orderItem.uid;
			final int ordertype = orderItem.type;
			final int orderstate = orderItem.state;

			btn_background.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectOrder(orderid, ordertype);
				}
			});

			btn_photo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectDrvPhoto(driverid);
				}
			});

			btnOper.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					operate(orderid, ordertype, orderstate);
				}
			});

			return convertView;
		}
	}


	private void selectOrder(long orderid, int order_type)
	{
		if (order_type == ConstData.ORDER_TYPE_ONCE)
		{
			Intent intentDetail = new Intent(PassMainActivity.this, PassOnceOrderDetailActivity.class);
			intentDetail.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			intentDetail.putExtra("orderid", orderid);
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intentDetail, REQCODE_ONCE_ORDERDETAIL);
		}
//		else if (order_type == ConstData.ORDER_TYPE_ONOFF)
//		{
//			int nState = -1;
//			for (int i = 0; i < arrOrders.size(); i++)
//			{
//				if (arrOrders.get(i).uid == orderid && arrOrders.get(i).type == ConstData.ORDER_TYPE_ONOFF)
//				{
//					nState = arrOrders.get(i).state;
//					break;
//				}
//			}
//
//			if (nState != -1)
//			{
//				switch (nState)
//				{
//					case ConstData.ORDER_STATE_DRV_ACCEPTED:
//						Intent intentAgree = new Intent(PassMainActivity.this, PassOnOffOrderConfirmActivity.class);
//						intentAgree.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//						intentAgree.putExtra("orderid", orderid);
//						intentAgree.putExtra("order_type", order_type);
//						PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//						startActivityForResult(intentAgree, REQCODE_ONOFF_ORDERDETAIL);
//						break;
//					case ConstData.ORDER_STATE_PUBLISHED:
//						Intent intent = new Intent(PassMainActivity.this, PassOnOffOrderProcessActivity.class);
//						intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//						intent.putExtra("orderid", orderid);
//						intent.putExtra("order_type", order_type);
//						PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//						startActivityForResult(intent, REQCODE_ONOFF_ORDERDETAIL);
//						break;
//					default :
//						Intent intentDetail = new Intent(PassMainActivity.this, PassOnOffOrderDetailActivity.class);
//						intentDetail.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//						intentDetail.putExtra("orderid", orderid);
//						intentDetail.putExtra("order_type", order_type);
//						PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//						startActivityForResult(intentDetail, REQCODE_ONOFF_ORDERDETAIL);
//						break;
//				}
//			}
//		}
		else if (order_type == ConstData.ORDER_TYPE_LONG)
		{
			Intent intentDetail = new Intent(PassMainActivity.this, PassLongOrderDetailActivity.class);
			intentDetail.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			intentDetail.putExtra("orderid", orderid);
			PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intentDetail, REQCODE_LONG_ORDERDETAIL);
		}

	}


	private void selectDrvPhoto(long driverid)
	{
		Intent intent = new Intent(PassMainActivity.this, DrvEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("driverid", driverid);
		PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void operate(long orderid, int type, int state)
	{
		STPsgOrderListItem orderItem = null;

		for (int i = 0; i < arrOrders.size(); i++)
		{
			STPsgOrderListItem listItem = arrOrders.get(i);
			if (listItem.uid == orderid && type == listItem.type)
			{
				orderItem = listItem;
				break;
			}
		}


		if (orderItem == null)
			return;


		switch (orderItem.state)
		{
			case ConstData.ORDER_STATE_DRV_ACCEPTED:
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
			case ConstData.ORDER_STATE_STARTED:
			case ConstData.ORDER_STATE_DRV_ARRIVED:
			case ConstData.ORDER_STATE_PASS_GETON:
			case ConstData.ORDER_STATE_FINISHED:
				moveToPayActivity(orderItem);
				break;
			case ConstData.ORDER_STATE_PAYED:
				moveToEvaluateActivity(orderItem);
				break;
			default:
				break;
		}
	}


	private PullToRefreshBase.OnRefreshListener orderListListener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestOrders();
			else
				getPagedOrders();
		}
	};


	private void getPagedOrders()
	{
		String szLimitTime = "";
		if (arrOrders.size() != 0)
			szLimitTime = arrOrders.get(arrOrders.size() - 1).create_time;

		CommManager.getPagedPassengerOrders(Global.loadUserID(getApplicationContext()), nSortMode, szLimitTime, Global.getIMEI(getApplicationContext()), pagedPsgOrdersHandler);
	}


	private void getLatestOrders()
	{
		arrOrders.clear();
		getPagedOrders();
//		String orderNum = "";
//		int orderType = 0;
//
//		Date dtLimit = null;
//		for (int i = 0; i < arrOrders.size(); i++)
//		{
//			STPsgOrderListItem orderItem = arrOrders.get(i);
//			Date dtCreate = Global.String2Date(orderItem.create_time);
//			if (dtCreate == null)
//				continue;
//
//			if (dtLimit == null || dtLimit.before(dtCreate))
//			{
//				dtLimit = dtCreate;
//				orderNum = orderItem.order_num;
//			}
//		}
//
//		CommManager.getLatestPassengerOrders(Global.loadUserID(getApplicationContext()), nSortMode, orderNum, orderType, Global.getIMEI(getApplicationContext()), latestPsgOrdersHandler);
	}


	private AsyncHttpResponseHandler pagedPsgOrdersHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			orderListView.onRefreshComplete();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");
					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STPsgOrderListItem listItem = STPsgOrderListItem.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrOrders.size(); j++)
						{
							if (arrOrders.get(j).uid == listItem.uid && arrOrders.get(j).type == listItem.type)
							{
								isExist = true;
								existIndex = j;
								break;
							}
						}

						if (!isExist)
							arrOrders.add(listItem);
						else
							arrOrders.set(existIndex, listItem);
					}

					// show/hide no order messsage
					checkOrderCnt();

					orderAdapter.notifyDataSetChanged();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
//					Global.showAdvancedToast(PassMainActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			orderListView.onRefreshComplete();
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);

//			Global.showAdvancedToast(PassMainActivity.this, content, Gravity.CENTER);
		}
	};



	private AsyncHttpResponseHandler latestPsgOrdersHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			orderListView.onRefreshComplete();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");
					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STPsgOrderListItem listItem = STPsgOrderListItem.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrOrders.size(); j++)
						{
							if (arrOrders.get(j).uid == listItem.uid && arrOrders.get(j).type == listItem.type)
							{
								isExist = true;
								existIndex = j;
								break;
							}
						}

						if (isExist)
							arrOrders.set(existIndex, listItem);
						else
							arrOrders.add(0, listItem);
					}

					// show/hide no order messsage
					checkOrderCnt();

					orderAdapter.notifyDataSetChanged();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
//					Global.showAdvancedToast(PassMainActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			orderListView.onRefreshComplete();
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);

//			Global.showAdvancedToast(PassMainActivity.this, content, Gravity.CENTER);
		}
	};


	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int nOldMode = nSortMode;

			switch (v.getId())
			{
				case R.id.imgAll:
				case R.id.lblAll:
					nSortMode = ALL_MODE;
					break;
				case R.id.imgReqOpr:
				case R.id.lblReqOpr:
					nSortMode = REQOPR_MODE;
					break;
				case R.id.imgOnce:
				case R.id.lblOnce:
					nSortMode = ONCE_MODE;
					break;
//				case R.id.imgCommute:
//				case R.id.lblCommute:
//					nSortMode = COMMUTE_MODE;
//					break;
				case R.id.imgLongDist:
				case R.id.lblLongDist:
					nSortMode = LONGDIST_MODE;
					break;
			}

			if (nSortMode != nOldMode)
			{
				showMode();

				arrOrders.clear();
				orderAdapter.notifyDataSetChanged();
				getPagedOrders();
			}
		}
	};

	private void checkOrderCnt()
	{
		// show/hide no order messsage
		if (arrOrders.size() > 0)
		{
			lblNoOrders.setVisibility(View.GONE);
		}
		else
		{
			lblNoOrders.setVisibility(View.VISIBLE);
		}
	}

	private void showMode()
	{
		switch (nSortMode)
		{
			case ALL_MODE:
				imgAll.setImageResource(R.drawable.radiobox_roundsel);
				imgReqOpr.setImageResource(R.drawable.radiobox_roundnormal);
				imgOnce.setImageResource(R.drawable.radiobox_roundnormal);
//				imgCommute.setImageResource(R.drawable.radiobox_roundnormal);
				imgLongDist.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case REQOPR_MODE:
				imgAll.setImageResource(R.drawable.radiobox_roundnormal);
				imgReqOpr.setImageResource(R.drawable.radiobox_roundsel);
				imgOnce.setImageResource(R.drawable.radiobox_roundnormal);
//				imgCommute.setImageResource(R.drawable.radiobox_roundnormal);
				imgLongDist.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case ONCE_MODE:
				imgAll.setImageResource(R.drawable.radiobox_roundnormal);
				imgReqOpr.setImageResource(R.drawable.radiobox_roundnormal);
				imgOnce.setImageResource(R.drawable.radiobox_roundsel);
//				imgCommute.setImageResource(R.drawable.radiobox_roundnormal);
				imgLongDist.setImageResource(R.drawable.radiobox_roundnormal);
				break;
//			case COMMUTE_MODE:
//				imgAll.setImageResource(R.drawable.radiobox_roundnormal);
//				imgReqOpr.setImageResource(R.drawable.radiobox_roundnormal);
//				imgOnce.setImageResource(R.drawable.radiobox_roundnormal);
//				imgCommute.setImageResource(R.drawable.radiobox_roundsel);
//				imgLongDist.setImageResource(R.drawable.radiobox_roundnormal);
//				break;
			case LONGDIST_MODE:
				imgAll.setImageResource(R.drawable.radiobox_roundnormal);
				imgReqOpr.setImageResource(R.drawable.radiobox_roundnormal);
				imgOnce.setImageResource(R.drawable.radiobox_roundnormal);
//				imgCommute.setImageResource(R.drawable.radiobox_roundnormal);
				imgLongDist.setImageResource(R.drawable.radiobox_roundsel);
				break;
		}

		return;
	}


	private void moveToPayActivity(STPsgOrderListItem orderItem)
	{
		Intent intent = new Intent(PassMainActivity.this, PassPayOrderActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("orderid", orderItem.uid);
		intent.putExtra("ordertype", orderItem.type);
		intent.putExtra("price", orderItem.price);
		intent.putExtra("reserve", false);

		PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_PAY_ORDER);
	}


	private void moveToEvaluateActivity(STPsgOrderListItem orderItem)
	{
		Intent intent = new Intent(PassMainActivity.this, EvaluateActivity.class);
		intent.putExtra("driver", orderItem.driver_id);
		intent.putExtra("passenger", Global.loadUserID(getApplicationContext()));
		intent.putExtra("order_type", orderItem.type);
		intent.putExtra("orderid", orderItem.uid);

		intent.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);			  // Who evaluates? driver or passenger
		intent.putExtra("view_mode", false);										 // Is view mode or send mode
		intent.putExtra("level", 1);												  // Preset level
		intent.putExtra("msg", "");												   // Preset content

		PassMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intent, REQCODE_EVALUATE);
	}


	private AsyncHttpResponseHandler logout_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					logout(getResources().getString(R.string.STR_LOGOUT_SUCCESS));
				}
				else if (nRetCode == -2)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassMainActivity.this,
							szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private AsyncHttpResponseHandler detailinfo_handler = new AsyncHttpResponseHandler()
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
					JSONObject retdata = result.getJSONObject("retdata");

					STPsgOrderListItem changedItem = new STPsgOrderListItem();

					changedItem.uid = retdata.getLong("uid");
					changedItem.order_num = retdata.getString("order_num");
					changedItem.type = mCurSelOrderType;
					if (changedItem.type == ConstData.ORDER_TYPE_ONCE)
						changedItem.type_desc = getResources().getString(R.string.STR_ORDERTYPE_ONCE);
					else if (changedItem.type == ConstData.ORDER_TYPE_ONOFF)
						changedItem.type_desc = getResources().getString(R.string.STR_ORDERTYPE_ONOFF);
					else
						changedItem.type_desc = getResources().getString(R.string.STR_ORDERTYPE_LONG);
					changedItem.price = retdata.getDouble("price");
					String strStartCity = retdata.getString("start_city");
					if(strStartCity.length() > 0)
						changedItem.start_addr = strStartCity + ":" +  retdata.getString("start_addr");
					else
						changedItem.start_addr = retdata.getString("start_addr");

					String strEndCity = retdata.getString("end_city");
					if(strEndCity.length() > 0)
						changedItem.end_addr = strEndCity + ":" +  retdata.getString("end_addr");
					else
						changedItem.end_addr = retdata.getString("end_addr");

					changedItem.start_time = retdata.getString("start_time");
					changedItem.create_time = retdata.getString("create_time");
					changedItem.state = retdata.getInt("state");
					changedItem.state_desc = retdata.getString("state_desc");
//					if (changedItem.state == ConstData.ORDER_STATE_CANCELLED) {
//						changedItem.state = ConstData.ORDER_STATE_CLOSED;
//						changedItem.state_desc = getResources().getString(R.string.STR_ORDERSTATE_CLOSED);
//					}
					changedItem.evaluated = retdata.getInt("evaluated");
					changedItem.eval_content = retdata.getString("eval_content");
					changedItem.evaluated_desc = retdata.getString("evaluated_desc");
					changedItem.start_city = retdata.getString("start_city");
					changedItem.end_city = retdata.getString("end_city");

					JSONObject drvInfo = retdata.getJSONObject("driver_info");
					changedItem.driver_id = drvInfo.getLong("uid");
					changedItem.driver_img = drvInfo.getString("img");
					changedItem.driver_name = drvInfo.getString("name");
					changedItem.driver_gender = drvInfo.getInt("gender");
					changedItem.driver_age = drvInfo.getInt("age");


					for (int j = 0; j < arrOrders.size(); j++)
					{
						if (arrOrders.get(j).uid == changedItem.uid && arrOrders.get(j).type == mCurSelOrderType)
						{
							arrOrders.set(j, changedItem);
							orderAdapter.notifyDataSetChanged();
							break;
						}
					}

				}
				else if (nRetCode == ConstData.ERR_CODE_NORMAL)
				{
					for (int k = 0; k < arrOrders.size(); k++)
					{
						if (arrOrders.get(k).uid == mCurSelOrderId && arrOrders.get(k).type == mCurSelOrderType)
						{
							arrOrders.remove(k);

							// show/hide no order messsage
							checkOrderCnt();

							orderAdapter.notifyDataSetChanged();
							break;
						}
					}
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassMainActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void showNewsBadge(boolean hasNews) {
		if (hasNews) {
			if (tab_shouye.getVisibility() != View.VISIBLE)
				img_hasnews.setVisibility(View.INVISIBLE);
			else
				img_hasnews.setVisibility(View.VISIBLE);
		} else {
			img_hasnews.setVisibility(View.INVISIBLE);
		}
	}

}
