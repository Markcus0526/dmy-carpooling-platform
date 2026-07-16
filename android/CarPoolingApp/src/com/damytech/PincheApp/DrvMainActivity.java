package com.damytech.PincheApp;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.*;
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
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STDrvOrderListItem;
import com.damytech.DataClasses.STUserInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.Rotate3dAnimation;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.SharedPreferenceUtil;
import com.damytech.Utils.mutil.SoundUtil;
import com.damytech.Utils.mutil.Utils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.common.SocializeConstants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-11
 * Time: ����4:55
 * To change this template use File | Settings | File Templates.
 */
public class DrvMainActivity extends SuperActivity {
    private static final String TAG = "erik_debug";
    private int REQCODE_NEWS = 0;
	private int REQCODE_VERIFY_PERSON = 1;
	private int REQCODE_VERIFY_DRIVER = 2;
	private int REQCODE_LONG_ORDER = 3;
	private int REQCODE_CITY_ORDER = 4;
	private int REQCODE_TAB_ORDER = 5;
	private int REQCODE_TAB_PERSONINFO = 6;
	private int REQCODE_TAB_SHARE = 7;
	private int REQCODE_PUBLISH_ORDER = 8;
	private int REQCODE_SCAN_ORDER = 9;
	private int REQCODE_SELECT_ONCE_ORDER = 10;
//	private int REQCODE_SELECT_ONOFF_ORDER = 11;
	private int REQCODE_SELECT_LONG_ORDER = 12;
	private int REQCODE_EXEC_CITYORDER = 13;
	private int REQCODE_EXEC_LONGORDER = 14;
	private int REQCODE_EVALUTE = 15;
	private int REQCODE_SELECT_PHOTO = 16;

	public static final String TAB_EXTRA_NAME = "tab_index";
	public static final int TAB_SHOUYE = 1;
	public static final int TAB_DINGDAN = 2;
	public static final int TAB_GEREN = 3;
	public static final int TAB_FENXIANG = 4;

	Bitmap bmpPhoto = null;

	long mCurSelOrderId = 0;
	int mCurSelOrderType = 0;

	private int announce_count = 0, order_notif_count = 0, person_notif_count = 0;

	// Baidu variables
	public LocationClient locClient = null;
	public MyLocationListener locListener = new MyLocationListener();


	// Tab controls
	private RelativeLayout tab_shouye = null;
	private RelativeLayout tab_dingdan = null;
	private RelativeLayout tab_geren = null;
	private RelativeLayout tab_fenxiang = null;

	private ImageView imgTabShouYe = null;
	private ImageView imgTabDingDan = null;
	private ImageView imgTabGeRen = null;
	private ImageView imgTabFenXiang = null;

	private TextView txtTabShouYe = null;
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

	private RelativeLayout person_layout = null, driver_layout = null;
	private Button btn_verify_person = null, btn_verify_driver = null;

	private SmartImageView imgPhoto = null;
	private ImageButton btnPhoto = null;

	private ImageButton btn_long_order = null, btn_city_order = null;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////


	// Order tab variables
	private final int ALL_MODE = 1;
	private final int REQOPR_MODE = 2;
	private final int ONCE_MODE = 3;
//	private final int COMMUTE_MODE = 4;
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

	private PullToRefreshListView driverOrderList = null;
	private ArrayList<STDrvOrderListItem> arrOrders = new ArrayList<STDrvOrderListItem>();
	private OrderAdapter orderAdapter = new OrderAdapter();
	private TextView lblNoOrders = null;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Person info tab controls
	private ImageButton btnPersonInfo = null;
	private ImageButton btnRouteSetting = null;
	private ImageButton btnMoney = null;
	private ImageButton btnCoupon = null;
	private ImageButton btnStrategy = null;
	private ImageButton btnAbout = null;
	private Button btnLogout = null;
    private TextView txtTitle = null;
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Share info tab controls
	private final UMSocialService mShareController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private WebView shareWebView = null;
	private ImageButton btnShare = null;
    private String outputUrl = null;
    private String outputContent = null;
    private boolean initComplete = false;
    private ActivityManager am;
    private PowerManager pm;
    private Timer getNewOrderTimer;
    private SoundUtil soundUtil;
    private int popupQueryTime = 30;
    private double runDist = 0;
    public static boolean isNoGetOrder = true;//是否在抢单界面
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		initLocationManager();
		setContentView(R.layout.act_driver_main);

		Global.startReportLocation(getApplicationContext());

        initSystemService();
		initControls();
		initResolution();
		initUMeng();
	}
    //初始化电源管理器和应用管理器
    private void initSystemService(){
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        soundUtil = new SoundUtil(this);
        soundUtil.loadFx(R.raw.new_order_sound, 1);
    }
	@Override
	protected void onResume() {
		locClient.start();

		super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

		TimerTask uiTask = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						int nTab = getIntent().getIntExtra(TAB_EXTRA_NAME, -1);
                        String orderType = getIntent().getStringExtra("order_type");
						if (nTab == TAB_SHOUYE) {
							onSelectShouYeTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						} else if (nTab == TAB_DINGDAN) {
							onSelectDingDanTab();
                            if(orderType !=null && orderType.equals("long")){
                                nSortMode = LONGDIST_MODE;
                                showMode();
                            }
							getIntent().removeExtra(TAB_EXTRA_NAME);
						} else if (nTab == TAB_GEREN) {
							onSelectGeRenTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						} else if (nTab == TAB_FENXIANG) {
							onSelectFenXiangTab();
							getIntent().removeExtra(TAB_EXTRA_NAME);
						}
					}
				});
			}
		};

		Timer uiTimer = new Timer();
		uiTimer.schedule(uiTask, 500);

		person_layout.setVisibility(View.GONE);
		driver_layout.setVisibility(View.GONE);

		initAccountInfo();
        if (!Global.isLoggedIn(getApplicationContext()))
            resetUserImage();

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

			getLatestDriverOrders();
		}

	}


	@Override
	protected void onPause() {
		locClient.stop();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		locClient.stop();
        if(getNewOrderTimer != null)
            getNewOrderTimer.cancel();
		super.onDestroy();
	}


	private void initControls() {
		Global.saveIdentify(getApplicationContext(), Global.IDENTIFY_DRIVER());

		initTabMgrControls();
        //获取版本信息
        getVersion();

		initMainTabControls();
		initOrderTabControls();
		initPersonTabControls();
		initShareTabControls();

        getPopUpQueryTime();
	}
    //开启定时查询新订单
    private void initGetNewOrderTask(){
        TimerTask getNewOrderTask = new TimerTask() {
            @Override
            public void run() {
                Utils.mLogError("isNoGetOrder="+isNoGetOrder);
                if(isNoOnTop() || isNoScreenOn() || isNoOnGetOrder()){
                    getPagedOnceOrders();
                }
            }
        };
        getNewOrderTimer = new Timer();
        getNewOrderTimer.schedule(getNewOrderTask,10000,popupQueryTime * 1000);
    }
    //判断应用放到后台或者屏幕为锁屏
    private boolean isNoOnTop(){
        return (!isOnTop() && isEffectiveDre() ) ;
    }

    private boolean isNoScreenOn(){
        return (isEffectiveDre() && !pm.isScreenOn() && isOnTop() );
    }

    private boolean isNoOnGetOrder(){
        return (isEffectiveDre() && isNoGetOrder );
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
                File apkfile = Global.downloadApk(apkurl, Utils.getDir(DrvMainActivity.this), "oonew.apk");
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
	private void initResolution() {
		RelativeLayout parent_layout = (RelativeLayout) findViewById(R.id.parent_layout);
		parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Point ptTemp = getScreenSize();
				boolean bNeedUpdate = false;
				if (mScrSize.x == 0 && mScrSize.y == 0) {
					mScrSize = ptTemp;
					bNeedUpdate = true;
				} else if (mScrSize.x != ptTemp.x || mScrSize.y != ptTemp.y) {
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}

				if (bNeedUpdate) {
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


	private void initUMeng() {
		com.umeng.socialize.utils.Log.LOG = true;
		SocializeConstants.SHOW_ERROR_CODE = true;
		SocializeConstants.APPKEY = "546abf95fd98c56573002d80";
	}


	private void initTabMgrControls() {
		tab_shouye = (RelativeLayout) findViewById(R.id.tab_main_layout);
		tab_dingdan = (RelativeLayout) findViewById(R.id.tab_order_layout);
		tab_geren = (RelativeLayout) findViewById(R.id.tab_person_layout);
		tab_fenxiang = (RelativeLayout) findViewById(R.id.tab_share_layout);

		tab_shouye.setVisibility(View.VISIBLE);
		tab_dingdan.setVisibility(View.INVISIBLE);
		tab_geren.setVisibility(View.INVISIBLE);
		tab_fenxiang.setVisibility(View.INVISIBLE);

		imgTabShouYe = (ImageView) findViewById(R.id.img_shouye);
		imgTabDingDan = (ImageView) findViewById(R.id.img_wodedingdan);
		imgTabGeRen = (ImageView) findViewById(R.id.img_person);
		imgTabFenXiang = (ImageView) findViewById(R.id.img_share);

		txtTitle = (TextView) findViewById(R.id.txt_title);
		txtTabShouYe = (TextView) findViewById(R.id.txt_shouye);
		txtTabDingDan = (TextView) findViewById(R.id.txt_wodedingdan);
		txtTabGeRen = (TextView) findViewById(R.id.txt_person);
		txtTabFenXiang = (TextView) findViewById(R.id.txt_share);

		btnTabShouYe = (ImageButton) findViewById(R.id.btn_tab_shouye);
		btnTabDingDan = (ImageButton) findViewById(R.id.btn_tab_wodedingdan);
		btnTabGeRen = (ImageButton) findViewById(R.id.btn_tab_person);
		btnTabFenXiang = (ImageButton) findViewById(R.id.btn_tab_share);

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


	public void onSelectShouYeTab() {
        txtTitle.setText(R.string.STR_MAIN_TITLE_DRIVER);
		if (tab_shouye.getVisibility() != View.VISIBLE) {
			if (Global.isLoggedIn(getApplicationContext())) {
                if (!Global.isLoggedIn(getApplicationContext()))
                    resetUserImage();

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
				driver_layout.setVisibility(View.GONE);
                resetUserImage();
			}
		}
        isNoGetOrder = true;
		btn_identify.setVisibility(View.VISIBLE);
		btn_news.setVisibility(View.VISIBLE);
		img_hasnews.setVisibility(View.INVISIBLE);
		btnShare.setVisibility(View.GONE);

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
	}

    private void getPopUpQueryTime(){
        CommManager.readPopUpQueryTime(Global.loadUserID(getApplicationContext()),
                Global.getIMEI(getApplicationContext()), getPopUpQueryTimeHandler);
    }

	private void onSelectDingDanTab() {
        txtTitle.setText(R.string.STR_MAIN_TITLE_DRIVER);
		showNewsBadge(false);
        isNoGetOrder = false;
		if (!Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_ORDER);
		} else {
			btn_identify.setVisibility(View.GONE);
			btn_news.setVisibility(View.GONE);
			img_hasnews.setVisibility(View.GONE);
			btnShare.setVisibility(View.GONE);

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

			if (arrOrders.isEmpty()) {
				startProgress();
				getPagedDriverOrders();
			}
		}
	}

	private void onSelectGeRenTab() {
        txtTitle.setText(R.string.STR_MAIN_TITLE_DRIVER);
		showNewsBadge(false);
        isNoGetOrder = false;
		if (!Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_PERSONINFO);
		} else {
			btn_identify.setVisibility(View.GONE);
			btn_news.setVisibility(View.GONE);
			img_hasnews.setVisibility(View.GONE);
			btnShare.setVisibility(View.GONE);

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
		}
	}

	private void onSelectFenXiangTab() {
        txtTitle.setText(R.string.STR_MAIN_TITLE);
		showNewsBadge(false);
        isNoGetOrder = false;
		if (!Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_SHARE);
		} else {
			//if (szDefContent1 == null)
			//	getDefaultShareContents();
//            shareWebView.loadUrl("http://182.92.237.144:9082/bk/index/activity_appFx.do?activityDto.userId="
//                    +Global.loadUserID(getApplicationContext()));
            shareWebView.loadUrl("http://182.92.237.144:8082/bk/index/activity_appFx.do?activityDto.userId="
                    +Global.loadUserID(getApplicationContext()));
			btn_identify.setVisibility(View.GONE);
			btn_news.setVisibility(View.GONE);
			img_hasnews.setVisibility(View.GONE);
			btnShare.setVisibility(View.VISIBLE);

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


	private void initMainTabControls() {
        mContainer = (ViewGroup)findViewById(R.id.content_layout);
        btn_identify = (ImageButton) findViewById(R.id.btn_switch_identify);
        btn_identify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPassMainActivity();
            }
        });
        btn_identify.setVisibility(View.VISIBLE);
        applyRotation(-1, 180, 90);

		btn_news = (ImageButton) findViewById(R.id.btn_news);
		btn_news.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToNewsActivity();
			}
		});
		btn_news.setVisibility(View.VISIBLE);

		img_hasnews = (ImageView) findViewById(R.id.img_has_news);
		img_hasnews.setVisibility(View.INVISIBLE);

		person_layout = (RelativeLayout) findViewById(R.id.person_verify_layout);
		driver_layout = (RelativeLayout) findViewById(R.id.driver_verify_layout);

		btn_verify_person = (Button) findViewById(R.id.btn_person_verify);
		btn_verify_person.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToVerifyPerson();
			}
		});

		btn_verify_driver = (Button) findViewById(R.id.btn_driver_verify);
		btn_verify_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToVerifyDriver();
			}
		});

		imgPhoto = (SmartImageView) findViewById(R.id.img_photo);
		imgPhoto.isCircular = true;
		imgPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});

		btnPhoto = (ImageButton) findViewById(R.id.btn_frame);
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if(!Global.isLoggedIn(getApplicationContext()))
                    return;
				Intent intent = new Intent(DrvMainActivity.this, SelectPhotoActivity.class);
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
				startActivityForResult(intent, REQCODE_SELECT_PHOTO);
			}
		});

		btn_long_order = (ImageButton) findViewById(R.id.btn_send_longorder);
		btn_long_order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToPublishOrderActivity();
			}
		});
		btn_city_order = (ImageButton) findViewById(R.id.btn_view_cityorder);
		btn_city_order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToScanOrderActivity();
			}
		});
	}


	private void moveToPassMainActivity() {
		Global.stopReportLocation();

		Intent intent = new Intent(DrvMainActivity.this, PassMainActivity.class);
		startActivity(intent);
		finish();
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
        rotation.setDuration(500);
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
                    if (!initComplete) startProgress();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            mContainer.startAnimation(rotation);
        }
    }


    private void moveToNewsActivity() {
		if (Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, NewsActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			intent.putExtra("announcements", announce_count);
			intent.putExtra("ordernotif_count", order_notif_count);
			intent.putExtra("personnotif_count", person_notif_count);
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivity(intent);
		} else {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_NEWS);
		}
	}

	private void moveToVerifyPerson() {
		if (Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, VerifyPersonActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivity(intent);
		} else {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_VERIFY_PERSON);
		}
	}

	private void moveToVerifyDriver() {
		if (Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, VerifyDriverActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivity(intent);
		} else {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_VERIFY_PERSON);
		}
	}


	private void moveToPublishOrderActivity() {
		if (!Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_PUBLISH_ORDER);
		} else if (!Global.isDriverVerified(getApplicationContext())) {
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_DRIVER_NOTVERIFIED), Gravity.CENTER);
		} else {
			Intent intent = new Intent(DrvMainActivity.this, DrvPubLongOrderActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivity(intent);
		}
	}


	private void moveToScanOrderActivity() {
		if (!Global.isLoggedIn(getApplicationContext())) {
			Intent intent = new Intent(DrvMainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_SCAN_ORDER);
		} else if (!Global.isDriverVerified(getApplicationContext())) {
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_DRIVER_NOTVERIFIED), Gravity.CENTER);
		} else {
			if (Global.isOpenGPS(DrvMainActivity.this)) {
				Intent intent = new Intent(DrvMainActivity.this, DrvScanCityOrderActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			} else {
				CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvMainActivity.this)
						.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
						.message(getResources().getString(R.string.mustopengps))
						.positiveTitle(getResources().getString(R.string.mustopengps_ok))
						.negativeTitle(getResources().getString(R.string.mustopengps_cancel))
						.positiveListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Global.openGPSSetting(DrvMainActivity.this);
							}
						})
						.build();
				dialog.show();
			}

//			Intent intent = new Intent(DriverMainActivity.this, EvaluateActivity.class);
//			intent.putExtra("driver", (long)21);
//			intent.putExtra("passenger", (long)20);
//			intent.putExtra("order_type", (int)ConstData.ORDER_TYPE_ONCE);
//			intent.putExtra("orderid", (long)1);
//
//			intent.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);			  // Who evaluates? driver or passenger
//			intent.putExtra("view_mode", false);										 // Is view mode or send mode
//			intent.putExtra("level", 1);												  // Preset level
//			intent.putExtra("msg", "");												   // Preset content
//
//			DriverMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//			startActivity(intent);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

        /**ʹ��SSO��Ȩ����������´��� */
        UMSsoHandler ssoHandler = mShareController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_VERIFY_PERSON) {
			getLatestDriverOrders();
			moveToVerifyPerson();
		} else if (requestCode == REQCODE_VERIFY_DRIVER) {
			getLatestDriverOrders();
			moveToVerifyDriver();
		} else if (requestCode == REQCODE_LONG_ORDER) {
			getLatestDriverOrders();
			moveToPublishOrderActivity();
		} else if (requestCode == REQCODE_CITY_ORDER) {
			getLatestDriverOrders();
			moveToScanOrderActivity();
		} else if (requestCode == REQCODE_TAB_ORDER) {
			getLatestDriverOrders();
			onSelectDingDanTab();
		} else if (requestCode == REQCODE_TAB_PERSONINFO) {
			getLatestDriverOrders();
			onSelectGeRenTab();
		} else if (requestCode == REQCODE_TAB_SHARE) {
			getLatestDriverOrders();
			onSelectFenXiangTab();
		} else if (requestCode == REQCODE_PUBLISH_ORDER) {
			getLatestDriverOrders();
			moveToPublishOrderActivity();
		} else if (requestCode == REQCODE_SCAN_ORDER) {
			getLatestDriverOrders();
			moveToScanOrderActivity();
		} else if ((requestCode == REQCODE_SELECT_LONG_ORDER) || (requestCode == REQCODE_EXEC_LONGORDER)) {
			long orderid = data.getLongExtra("orderid", 0);
			updateOrderInfo(orderid, ConstData.ORDER_TYPE_LONG);
		} else if (requestCode == REQCODE_SELECT_ONCE_ORDER) {
			long orderid = data.getLongExtra("orderid", 0);
			updateOrderInfo(orderid, ConstData.ORDER_TYPE_ONCE);
//		} else if (requestCode == REQCODE_SELECT_ONOFF_ORDER) {
//			long orderid = data.getLongExtra("orderid", 0);
//			updateOrderInfo(orderid, ConstData.ORDER_TYPE_ONOFF);
		} else if (requestCode == REQCODE_EXEC_CITYORDER ||
				requestCode == REQCODE_EVALUTE) {
			refreshOrderInfos();
		} else if (requestCode == REQCODE_SELECT_PHOTO) {
			updateUserImage(data);
		}
	}


	/* Image mamagement methods */
	private void updateUserImage(Intent data) {
		if (data.getIntExtra(SelectPhotoActivity.szRetCode, -999) == SelectPhotoActivity.nRetSuccess) {
			Object objPath = data.getExtras().get(SelectPhotoActivity.szRetPath);

			String szPath = "";

			if (objPath != null)
				szPath = (String) objPath;

			if (szPath != null && !szPath.equals(""))
				updateUserImageWithPath(szPath);
		}
	}

    //判断应用是否在前台运行
    private boolean isOnTop(){
        List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
        if(null != infos && 0 < infos.size()){
            if(getPackageName().equals(infos.get(0).topActivity.getPackageName())){
                return true;
            }
        }
        return false;
    }
     //判断车长是否可用抢单
    private boolean isEffectiveDre(){
        return  Global.isLoggedIn(getApplicationContext()) && Global.isDriverVerified(getApplicationContext())&&
                Global.isOpenGPS(DrvMainActivity.this);
    }
    private void getPagedOnceOrders()
    {
        CommManager.getPagedAcceptableOnceOrders(Global.loadUserID(getApplicationContext()),
                0,
                3,
                Global.getIMEI(getApplicationContext()),
                getNewOrdersHandler);
    }

	private void updateUserImageWithPath(String szPath) {
		try {
			/* Update user photo info view */
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(szPath, options);

			if (bitmap != null) {
				startProgress();

				int nWidth = bitmap.getWidth(), nHeight = bitmap.getHeight();
				int nScaledWidth = 0, nScaledHeight = 0;
				if (nWidth > nHeight) {
					nScaledWidth = SelectPhotoActivity.IMAGE_WIDTH;
					nScaledHeight = nScaledWidth * nHeight / nWidth;
				} else {
					nScaledHeight = SelectPhotoActivity.IMAGE_HEIGHT;
					nScaledWidth = nScaledHeight * nWidth / nHeight;
				}

				bmpPhoto = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);

				CommManager.changeUserPhoto(Global.loadUserID(getApplicationContext()),
						Global.encodeWithBase64(bmpPhoto),
						Global.getIMEI(getApplicationContext()),
						change_photo_handler);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

//		stopProgress();
	}

	private void updateOrderInfo(long orderid, int ordertype) {
		if (orderid <= 0) return;

		mCurSelOrderId = orderid;
		mCurSelOrderType = ordertype;

		startProgress();
		CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
				orderid, ordertype, Global.getIMEI(getApplicationContext()), detOrderInfoHandler);
	}

	private void refreshOrderInfos() {
		arrOrders.clear();
		getPagedDriverOrders();
	}

	private void initOrderTabControls() {
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

		driverOrderList = (PullToRefreshListView) findViewById(R.id.driverOrderList);
		{
			driverOrderList.setMode(PullToRefreshBase.Mode.BOTH);
			driverOrderList.setOnRefreshListener(orderListListener);
			driverOrderList.setAdapter(orderAdapter);
			driverOrderList.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFF1F1F1")));
			driverOrderList.getRefreshableView().setCacheColorHint(Color.parseColor("#FFF1F1F1"));
		}
		lblNoOrders = (TextView) findViewById(R.id.lblNoOrders);
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int nOldMode = nSortMode;

			switch (v.getId()) {
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

			if (nSortMode != nOldMode) {
				showMode();

				arrOrders.clear();
				orderAdapter.notifyDataSetChanged();
				getPagedDriverOrders();
			}
		}
	};

	private void checkOrderCnt() {
		// show/hide no order messsage
		if (arrOrders.size() > 0) {
			lblNoOrders.setVisibility(View.GONE);
		} else {
			lblNoOrders.setVisibility(View.VISIBLE);
		}
	}

	private void showMode() {
		switch (nSortMode) {
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


	private void initPersonTabControls() {
		btnPersonInfo = (ImageButton) findViewById(R.id.btn_wodegerenxinxi);
		btnRouteSetting = (ImageButton) findViewById(R.id.btn_route_setting);
		btnMoney = (ImageButton) findViewById(R.id.btn_wodejianpailvdian);
		btnCoupon = (ImageButton) findViewById(R.id.btn_wodedianquan);
		btnStrategy = (ImageButton) findViewById(R.id.btn_pinche_stratigy);
		btnAbout = (ImageButton) findViewById(R.id.btn_guanyuoo);
		btnLogout = (Button) findViewById(R.id.btn_logout);

		btnPersonInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvMainActivity.this, DrvPersonInfoActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		btnRouteSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvMainActivity.this, DrvDailyRouteActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		btnMoney.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvMainActivity.this, BalanceActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		btnCoupon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvMainActivity.this, CouponActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		btnStrategy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvMainActivity.this, StrategyActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});
		btnAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvMainActivity.this, AboutActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
			}
		});

		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvMainActivity.this)
						.message(getResources().getString(R.string.STR_CONFIRM_LOGOUT))
						.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
						.positiveListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								startProgress();
								CommManager.logoutUser(Global.loadUserID(getApplicationContext()),
										Global.getIMEI(getApplicationContext()),
										logout_handler);
							}
						})
						.build();
				dialog.show();
			}
		});
	}

	private void initShareTabControls() {
        shareWebView = (WebView)findViewById(R.id.share_webview);
        shareWebView.getSettings().setJavaScriptEnabled(true);
//        shareWebView.loadUrl("http://www.baidu.com");
        shareWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
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
        Utils.mLogError("分享链接=="+originUrl);
        String[] urlList = originUrl.split(".do");
        getDefaultShareContents(urlList[0]+".do", 0);


    }



    private void initAccountInfo() {
        if (!Global.isLoggedIn(getApplicationContext()))
            resetUserImage();

		startProgress();
		CommManager.getLoginInfoFromDevToken(Global.getIMEI(getApplicationContext()),
				Global.getNotificationToken(getApplicationContext()),
				logininfo_handler);
	}
    //发出广播通知弹屏
    private void sendNewOrderBroadCast(){
        Intent intent = new Intent("android.alarm.action");
        this.sendBroadcast(intent);
    }
    //判断是否符合发送广播条件
    private void judgeSendNewOrderBroadCast(int uid){
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(this);
        int lastUid = spUtil.getInt("lastuid",0);
        if(lastUid < uid){
            spUtil.saveInt("lastuid", uid);
            if(isNoOnTop() || isNoScreenOn()){
                soundUtil.play(1,1);
                sendNewOrderBroadCast();
            }else if(isNoGetOrder){
                soundUtil.play(1,1);
            }

        }
    }
    private void getDefaultShareContents(String inputUrl, int shareFlag)
    {
        if (Global.isLoggedIn(getApplicationContext()))
        {
            CommManager.defaultShareContents(Global.loadUserID(getApplicationContext()),
                    inputUrl, shareFlag, Global.getIMEI(getApplicationContext()),
                    shareContents_handler);
        }
    }
    private AsyncHttpResponseHandler getNewOrdersHandler = new AsyncHttpResponseHandler(){
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            try{
                JSONObject json = new JSONObject(content);
                JSONObject resultJson = json.getJSONObject("result");
                int resultCode = resultJson.getInt("retcode");
                if(resultCode == 0){
                    JSONArray jarr = resultJson.getJSONArray("retdata");
                    if(jarr.length() > 0){
                        JSONObject jItem = jarr.getJSONObject(0);
                        int uid = jItem.getInt("uid");
                        judgeSendNewOrderBroadCast(uid);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private AsyncHttpResponseHandler getPopUpQueryTimeHandler = new AsyncHttpResponseHandler(){
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            try{
                JSONObject json = new JSONObject(content);
                JSONObject resultJson = json.getJSONObject("result");
                int resultCode = resultJson.getInt("retcode");
                if(0 == resultCode){
                    if(resultJson.has("retdata") && null != resultJson.get("retdata")){
                        JSONObject timeJson = resultJson.getJSONObject("retdata");
                        int queryTime = timeJson.getInt("polling_time");
                        Utils.mLogError("获取的时间：："+queryTime);
                        if(queryTime > 0){
                            popupQueryTime = queryTime;
                        }

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                initGetNewOrderTask();
            }
        }
    };
	private AsyncHttpResponseHandler change_photo_handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH) {
					logout(szRetMsg);
				} else if (nRetcode == ConstData.ERR_CODE_NONE) {
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
						driver_layout.setVisibility(View.GONE);

						CommManager.hasNews(Global.loadUserID(getApplicationContext()),
								Global.loadCityName(getApplicationContext()),
								Global.isDriverVerified(getApplicationContext()),
								Global.loadLastAnnouncementID(getApplicationContext()),
								Global.loadLastOrderNotificationID(getApplicationContext()),
								Global.loadLastPersonNotificationID(getApplicationContext()),
								Global.getIMEI(getApplicationContext()),
								has_news_handler);
					}
				} else {
					Global.showAdvancedToast(DrvMainActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler logininfo_handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            initComplete = true;

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				if (nRetcode == ConstData.ERR_CODE_NONE) {
					JSONObject retdata = result.getJSONObject("retdata");
					STUserInfo userinfo = STUserInfo.decodeFromJSON(retdata);

					Global.saveUserID(getApplicationContext(), userinfo.userid);
					Global.saveInviteCode(getApplicationContext(), userinfo.invitecode);
					Global.savePersonVerified(getApplicationContext(), userinfo.person_verified == 1);
					Global.saveDriverVerified(getApplicationContext(), userinfo.driver_verified == 1);
					Global.saveBaiduApiKey(getApplicationContext(), userinfo.baiduak);
                    Global.savePersonVerfiedWait(getApplicationContext(),userinfo.person_verified == 2);
                    Global.saveDriverVerfiedWait(getApplicationContext(), userinfo.driver_verified == 2);

					imgPhoto.setImageUrl(userinfo.photo_url, R.drawable.icon_appprice_over);

					if (userinfo.person_verified == 0)              // Not verified and not required to review
						person_layout.setVisibility(View.VISIBLE);
					else
						person_layout.setVisibility(View.GONE);

					if (userinfo.driver_verified == 0)
						driver_layout.setVisibility(View.VISIBLE);
					else
						driver_layout.setVisibility(View.GONE);

					CommManager.hasNews(Global.loadUserID(getApplicationContext()),
							Global.loadCityName(getApplicationContext()),
							Global.isDriverVerified(getApplicationContext()),
							Global.loadLastAnnouncementID(getApplicationContext()),
							Global.loadLastOrderNotificationID(getApplicationContext()),
							Global.loadLastPersonNotificationID(getApplicationContext()),
							Global.getIMEI(getApplicationContext()),
							has_news_handler);

					// check started order info
					JSONObject exec_info = retdata.getJSONObject("exec_info");
					long runOrderId = exec_info.getLong("orderid");
					int runOrderType = exec_info.getInt("ordertype");
					int runTime = exec_info.getInt("time");
					runDist = exec_info.getDouble("distance");
					if (runOrderId > 0) {
						Intent intent = new Intent(DrvMainActivity.this, DrvCityOrderExeActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
						intent.putExtra("orderid", runOrderId);
						intent.putExtra("ordertype", runOrderType);
						intent.putExtra("runTime", runTime);
						intent.putExtra("runDist", runDist);
						DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
						startActivityForResult(intent, REQCODE_EXEC_CITYORDER);
                        return;
					}

                    checkPushResult();
				} else {
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            initComplete = true;
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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
//                    case ConstData.PUSH_DRV_ONOFF_CONFIRM:
//                    {
//                        // go to order confirm
//                        Intent intent = new Intent(DrvMainActivity.this, DrvOnoffOrderDetailActivity.class);
//                        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//                        intent.putExtra("orderid", orderid);
//                        intent.putExtra("ordertype", ordertype);
//                        DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//                        startActivityForResult(intent, REQCODE_SELECT_ONOFF_ORDER);
//                        break;
//                    }
                    case ConstData.PUSH_DRV_ORDER_DETAIL:
                    {
                        // go to order detail
                        switch (ordertype)
                        {
                            case ConstData.ORDER_TYPE_ONCE:
                            {
                                Intent intent = new Intent(DrvMainActivity.this, DrvOnceOrderDetailActivity.class);
                                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                                intent.putExtra("orderid", orderid);
                                DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                                startActivityForResult(intent, REQCODE_SELECT_ONCE_ORDER);
                                break;
                            }
//                            case ConstData.ORDER_TYPE_ONOFF:
//                            {
//                                Intent intent = new Intent(DrvMainActivity.this, DrvOnoffOrderDetailActivity.class);
//                                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//                                intent.putExtra("orderid", orderid);
//                                intent.putExtra("ordertype", ordertype);
//                                DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//                                startActivityForResult(intent, REQCODE_SELECT_ONOFF_ORDER);
//                                break;
//                            }
                            case ConstData.ORDER_TYPE_LONG:
                            {
                                Intent intent = new Intent(DrvMainActivity.this, DrvLongOrderDetailActivity.class);
                                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                                intent.putExtra("orderid", orderid);
                                DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                                startActivityForResult(intent, REQCODE_SELECT_LONG_ORDER);
                                break;
                            }
                        }

                        break;
                    }
                    case ConstData.PUSH_EXTRA:
                    case ConstData.PUSH_PASS_ONOFF_CONFIRM:
                    case ConstData.PUSH_PASS_ONOFF_TO_ONCE:
                    case ConstData.PUSH_PASS_LONG_CONFIRM:
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


	private AsyncHttpResponseHandler has_news_handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");

				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE) {
					JSONObject retdata = jsonResult.getJSONObject("retdata");

					announce_count = retdata.getInt("announcement");
					order_notif_count = retdata.getInt("ordernotif");
					person_notif_count = retdata.getInt("personnotif");

					if (announce_count > 0 || order_notif_count > 0 || person_notif_count > 0)
						showNewsBadge(true);
					else
						showNewsBadge(false);
				} else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH) {
					Global.showAdvancedToast(DrvMainActivity.this, jsonMsg, Gravity.CENTER);
					Global.clearUserInfo(getApplicationContext());
				} else {
					Global.showAdvancedToast(DrvMainActivity.this, jsonMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private AsyncHttpResponseHandler detOrderInfoHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
//            Utils.mLogError("我的订单："+content);
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE) {
					JSONObject retdata = result.getJSONObject("retdata");
					// parse result
					STDrvOrderListItem changedItem = new STDrvOrderListItem();

					changedItem.uid = retdata.getLong("uid");
					changedItem.order_num = retdata.getString("order_num");
					changedItem.type = mCurSelOrderType;
					if (changedItem.type == ConstData.ORDER_TYPE_ONCE)
						changedItem.type_desc = getResources().getString(R.string.STR_ORDERTYPE_ONCE);
					else if (changedItem.type == ConstData.ORDER_TYPE_ONOFF)
						changedItem.type_desc = getResources().getString(R.string.STR_ORDERTYPE_ONOFF);
					else if (changedItem.type == ConstData.ORDER_TYPE_LONG)
						changedItem.type_desc = getResources().getString(R.string.STR_ORDERTYPE_LONG);

					JSONArray arrPass = retdata.getJSONArray("pass_list");
					if (changedItem.type == ConstData.ORDER_TYPE_LONG || arrPass.length() == 0) {
						changedItem.pass_id = 0;
						changedItem.pass_img = "";
						changedItem.pass_name = "";
						changedItem.pass_gender = 0;
						changedItem.pass_age = 0;
						changedItem.pass_count = arrPass.length();

						changedItem.evaluated = 0;
						changedItem.eval_content = "";
						changedItem.evaluated_desc = "";

					} else {
						JSONObject pass_info = arrPass.getJSONObject(0);
						changedItem.pass_id = pass_info.getLong("uid");
						changedItem.pass_img = pass_info.getString("img");
						changedItem.pass_name = pass_info.getString("name");
						changedItem.pass_gender = pass_info.getInt("gender");
						changedItem.pass_age = pass_info.getInt("age");
						changedItem.pass_count = 1;

						changedItem.evaluated = pass_info.getInt("evaluated");
						changedItem.eval_content = pass_info.getString("eval_content");
						changedItem.evaluated_desc = pass_info.getString("evaluated_desc");
					}

					changedItem.price = retdata.getDouble("price");
					changedItem.sysinfo_fee = retdata.getDouble("sysinfo_fee");

					changedItem.start_city = retdata.getString("start_city");
					changedItem.end_city = retdata.getString("end_city");

					changedItem.start_addr = retdata.getString("start_addr");
					changedItem.end_addr = retdata.getString("end_addr");
					changedItem.start_time = retdata.getString("start_time");
					changedItem.state = retdata.getInt("state");
					changedItem.state_desc = retdata.getString("state_desc");
					changedItem.create_time = retdata.getString("create_time");


					for (int j = 0; j < arrOrders.size(); j++) {
						if (arrOrders.get(j).uid == changedItem.uid && arrOrders.get(j).type == mCurSelOrderType) {
							arrOrders.set(j, changedItem);
							orderAdapter.notifyDataSetChanged();
							break;
						}
					}
				} else if (nRetcode == ConstData.ERR_CODE_NORMAL) {
					for (int k = 0; k < arrOrders.size(); k++) {
						if (arrOrders.get(k).uid == mCurSelOrderId && arrOrders.get(k).type == mCurSelOrderType) {
							arrOrders.remove(k);
							orderAdapter.notifyDataSetChanged();
							// show/hide no orders message
							checkOrderCnt();
							break;
						}
					}
				} else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH) {
					logout(szRetMsg);
				} else {
					Global.showAdvancedToast(DrvMainActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	// Map handlers
	private class MyLocationListener implements BDLocationListener {
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
		}
	}

	private void initLocationManager() {
		locClient = new LocationClient(DrvMainActivity.this);
		locClient.registerLocationListener(locListener);

		LocationClientOption option = new LocationClientOption();

		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(true);
		option.setOpenGps(true);

		locClient.setLocOption(option);
	}


	private AsyncHttpResponseHandler pagedDrvOrdersHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			driverOrderList.onRefreshComplete();
//            Utils.mLogError("1.我的订单："+content);
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE) {
					JSONArray retdata = result.getJSONArray("retdata");
					for (int i = 0; i < retdata.length(); i++) {
						JSONObject jsonItem = retdata.getJSONObject(i);
						STDrvOrderListItem listItem = STDrvOrderListItem.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrOrders.size(); j++) {
							if (arrOrders.get(j).uid == listItem.uid && arrOrders.get(j).type == listItem.type) {
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

					// show/hide no orders message
					checkOrderCnt();

					orderAdapter.notifyDataSetChanged();
				} else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH) {
					logout(szRetMsg);
				} else {
					Global.showAdvancedToast(DrvMainActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			driverOrderList.onRefreshComplete();

//			Global.showAdvancedToast(DrvMainActivity.this, content, Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler latestDrvOrdersHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			driverOrderList.onRefreshComplete();
//            Utils.mLogError("2.我的订单："+content);
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE) {
					JSONArray retdata = result.getJSONArray("retdata");
					for (int i = 0; i < retdata.length(); i++) {
						JSONObject jsonItem = retdata.getJSONObject(i);
						STDrvOrderListItem listItem = STDrvOrderListItem.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrOrders.size(); j++) {
							if (arrOrders.get(j).uid == listItem.uid && arrOrders.get(j).type == listItem.type) {
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

					// show/hide no orders message
					checkOrderCnt();

					orderAdapter.notifyDataSetChanged();
				} else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH) {
					logout(szRetMsg);
				} else {
					Global.showAdvancedToast(DrvMainActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			driverOrderList.onRefreshComplete();
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void getPagedDriverOrders() {
		String szLimitTime = "";
		if (arrOrders.size() != 0)
			szLimitTime = arrOrders.get(arrOrders.size() - 1).create_time;

		CommManager.getPagedDriverOrders(Global.loadUserID(getApplicationContext()),
                nSortMode, szLimitTime, Global.getIMEI(getApplicationContext()),
                pagedDrvOrdersHandler);
	}


	private void getLatestDriverOrders() {
		arrOrders.clear();
		getPagedDriverOrders();

//		String orderNum = "";
//		int orderType = 0;
//
//		Date dtLimit = null;
//		for (int i = 0; i < arrOrders.size(); i++)
//		{
//			STDrvOrderListItem orderItem = arrOrders.get(i);
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
//		CommManager.getLatestDriverOrders(Global.loadUserID(getApplicationContext()), nSortMode, orderNum, orderType, Global.getIMEI(getApplicationContext()), latestDrvOrdersHandler);
	}


	private PullToRefreshBase.OnRefreshListener orderListListener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestDriverOrders();
			else
				getPagedDriverOrders();
		}
	};


	private class OrderAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return arrOrders.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public Object getItem(int position) {
			return arrOrders.get(position);
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public boolean isEmpty() {
			if (arrOrders == null)
				return true;

			return arrOrders.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final STDrvOrderListItem orderItem = arrOrders.get(position);

			STOrderViewHolder viewHolder = null;
			if (convertView == null) {
				int nWidth = 460, nHeight = ViewGroup.LayoutParams.WRAP_CONTENT, nYMargin = 10;
				RelativeLayout itemLayout = new RelativeLayout(driverOrderList.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight);
				itemLayout.setLayoutParams(layoutParams);

				RelativeLayout itemTemplate = (RelativeLayout) getLayoutInflater().inflate(R.layout.view_driver_myorderlistitem, null);
				RelativeLayout itemView = (RelativeLayout) itemTemplate.findViewById(R.id.parent_layout);
				RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
				item_layoutParams.setMargins(0, nYMargin, 0, nYMargin);
				item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				itemView.setLayoutParams(item_layoutParams);
				itemLayout.addView(itemView);

				ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

				convertView = itemLayout;

				viewHolder = new STOrderViewHolder();
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (STOrderViewHolder) convertView.getTag();
			}


			// Set address
			TextView lblAddr = null;
			if (viewHolder.lblAddr == null)
				viewHolder.lblAddr = (TextView) convertView.findViewById(R.id.lbl_address);
			lblAddr = viewHolder.lblAddr;
			if (orderItem.type == ConstData.ORDER_TYPE_LONG) {
				lblAddr.setText(orderItem.start_city + getResources().getString(R.string.addr_separator) + orderItem.end_city);
			} else {
				lblAddr.setText(orderItem.start_addr + getResources().getString(R.string.addr_separator) + orderItem.end_addr);
			}


			// Set price
			TextView lblPrice = null;
			if (viewHolder.lblPrice == null)
				viewHolder.lblPrice = (TextView) convertView.findViewById(R.id.lbl_price);
			lblPrice = viewHolder.lblPrice;
			lblPrice.setText(orderItem.price + getResources().getString(R.string.STR_BALANCE_DIAN));


			// Set order type
			TextView lblOrderType = null;
			if (viewHolder.lblOrderType == null)
				viewHolder.lblOrderType = (TextView) convertView.findViewById(R.id.lbl_ordertype);
			lblOrderType = viewHolder.lblOrderType;
			lblOrderType.setText(orderItem.type_desc);


			// Set Date
			TextView lblDate = null;
			if (viewHolder.lblDate == null)
				viewHolder.lblDate = (TextView) convertView.findViewById(R.id.lbl_date);
			lblDate = viewHolder.lblDate;
			lblDate.setText(orderItem.start_time);


			// Set time as empty
			TextView lblTime = null;
			if (viewHolder.lblTime == null)
				viewHolder.lblTime = (TextView) convertView.findViewById(R.id.lbl_time);
			lblTime = viewHolder.lblTime;
			lblTime.setText("");

			Button lblInfoFee = null;
			if (viewHolder.lblInfoFee == null)
				viewHolder.lblInfoFee = (Button) convertView.findViewById(R.id.txt_info_fee);
			lblInfoFee = viewHolder.lblInfoFee;
            final double serviceFee = orderItem.sysinfo_fee;
            final double insurance_fee = orderItem.insuranceFee;
            lblInfoFee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   showFeeDialog(serviceFee, insurance_fee);
                }
            });


			// Set operation text
			TextView lblOper = null;
			ImageView imgOper = null;
			ImageButton btnOper = null;
			ImageButton btn_background = null;
			ImageButton btn_photo = null;
			if (viewHolder.lblOper == null) {
				viewHolder.lblOper = (TextView) convertView.findViewById(R.id.txt_operation);
				viewHolder.imgOper = (ImageView) convertView.findViewById(R.id.img_operation);
				viewHolder.btnOper = (ImageButton) convertView.findViewById(R.id.btn_operation);
				viewHolder.btn_background = (ImageButton) convertView.findViewById(R.id.btn_background);
				viewHolder.btn_photo = (ImageButton) convertView.findViewById(R.id.btn_psg_photo);
			}

			lblOper = viewHolder.lblOper;
			imgOper = viewHolder.imgOper;
			imgOper.setImageResource(R.drawable.yellowcircle);
			btnOper = viewHolder.btnOper;
			btn_background = viewHolder.btn_background;
			btn_photo = viewHolder.btn_photo;

			final int nState = orderItem.state;
			switch (nState) {
				case ConstData.ORDER_STATE_PUBLISHED: {
					lblOper.setVisibility(View.INVISIBLE);
					imgOper.setVisibility(View.INVISIBLE);
					btnOper.setVisibility(View.INVISIBLE);
					break;
				}
				case ConstData.ORDER_STATE_GRABBED: {
					lblOper.setVisibility(View.VISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.VISIBLE);
					lblOper.setText(getResources().getString(R.string.STR_KAISHIZHIXING));
					break;
				}
				case ConstData.ORDER_STATE_STARTED:
				case ConstData.ORDER_STATE_DRV_ARRIVED:
				case ConstData.ORDER_STATE_PASS_GETON: {
					lblOper.setVisibility(View.VISIBLE);
					imgOper.setVisibility(View.VISIBLE);
					btnOper.setVisibility(View.VISIBLE);
					lblOper.setText(getResources().getString(R.string.STR_ZHIXINGZHONG));
					break;
				}
				case ConstData.ORDER_STATE_FINISHED: {
					lblOper.setVisibility(View.INVISIBLE);
					imgOper.setVisibility(View.INVISIBLE);
					btnOper.setVisibility(View.INVISIBLE);
					lblOper.setText(getResources().getString(R.string.STR_QUPINGJIA));
					break;
				}
				case ConstData.ORDER_STATE_PAYED: {
					if (orderItem.type == ConstData.ORDER_TYPE_LONG) {
						lblOper.setVisibility(View.INVISIBLE);
						imgOper.setVisibility(View.INVISIBLE);
						btnOper.setVisibility(View.INVISIBLE);
						lblOper.setText(getResources().getString(R.string.STR_QUPINGJIA));
					} else {
						lblOper.setVisibility(View.VISIBLE);
						imgOper.setVisibility(View.VISIBLE);
						btnOper.setVisibility(View.VISIBLE);
						lblOper.setText(getResources().getString(R.string.STR_QUPINGJIA));
					}
					break;
				}
				case ConstData.ORDER_STATE_EVALUATED: {
					lblOper.setVisibility(View.INVISIBLE);
                    if (orderItem.type == ConstData.ORDER_TYPE_LONG) {
                        imgOper.setVisibility(View.INVISIBLE);
                    }else{
                        imgOper.setVisibility(View.VISIBLE);
                    }
					btnOper.setVisibility(View.INVISIBLE);
//					lblOper.setText(orderItem.evaluated_desc);
                   	switch (orderItem.evaluated) {
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
				case ConstData.ORDER_STATE_CLOSED: {
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
			if (viewHolder.txtState == null)
				viewHolder.txtState = (TextView) convertView.findViewById(R.id.lbl_state);
			txtState = viewHolder.txtState;
			txtState.setText(orderItem.state_desc);

			// Set passenger information
			SmartImageView psgPhoto = null;
			if (viewHolder.psgPhoto == null) {
				viewHolder.psgPhoto = (SmartImageView) convertView.findViewById(R.id.img_psg_photo);
				viewHolder.psgPhoto.isCircular = true;
			}
			psgPhoto = viewHolder.psgPhoto;
			psgPhoto.setImage(new SmartImage() {
				@Override
				public Bitmap getBitmap(Context context) {
					return BitmapFactory.decodeResource(getResources(), R.drawable.bk_passengers);
				}
			});

			ImageView imgGender = null;
			TextView lblAge = null;
			TextView lblName = null;
			if (viewHolder.imgGender == null) {
				viewHolder.imgGender = (ImageView) convertView.findViewById(R.id.img_gender);
				viewHolder.lblAge = (TextView) convertView.findViewById(R.id.lbl_age);
				viewHolder.lblName = (TextView) convertView.findViewById(R.id.lbl_name);
			}
			imgGender = viewHolder.imgGender;
			lblAge = viewHolder.lblAge;
			lblName = viewHolder.lblName;

			if (orderItem.type == 3)            // Long distance order. No passenger photo
			{
				psgPhoto.setImageResource(R.drawable.bk_passengers);
				imgGender.setVisibility(View.INVISIBLE);
				lblAge.setVisibility(View.INVISIBLE);
//				lblName.setVisibility(View.INVISIBLE);
				btn_photo.setVisibility(View.INVISIBLE);
                lblName.setText("乘客："+orderItem.pass_count);
			} else {
				psgPhoto.setImageUrl(orderItem.pass_img, R.drawable.icon_appprice_over);
				imgGender.setImageResource(orderItem.pass_gender == 0 ? R.drawable.bk_manmark : R.drawable.bk_womanmark);
				lblAge.setText("" + orderItem.pass_age);
				lblName.setText(orderItem.pass_name);
				btn_photo.setVisibility(View.VISIBLE);
			}


			final long passid = orderItem.pass_id;
			final long orderid = orderItem.uid;
			final int ordertype = orderItem.type;
			final int orderstate = orderItem.state;
            final String passPhone = orderItem.pass_phone;
            final double totalDistance = orderItem.totalDistance;
            //final String PassPhone = orderItem.pass_list.get(0).phone;

			btn_background.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectOrder(orderid, ordertype);
				}
			});

			btn_photo.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					selectPassPhoto(passid);
				}
			});

			btnOper.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					operate(orderid, ordertype, orderstate, passPhone,totalDistance);
				}
			});

			return convertView;
		}
	}

    private void showFeeDialog(double serviceFee, double insuranceFee) {
        final Dialog sysFeeDialog = new Dialog(DrvMainActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(DrvMainActivity.this);
        View dialogView = mInflater.inflate(R.layout.dlg_driver_fee_detail, null);
        sysFeeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ResolutionSet.instance.iterateChild(dialogView, mScrSize.x, mScrSize.y);
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

	private class STOrderViewHolder {
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
		SmartImageView psgPhoto = null;
		ImageView imgGender = null;
		TextView lblAge = null;
		TextView lblName = null;
		Button lblInfoFee = null;
	}


	private void selectPassPhoto(long passid) {
		Intent intent = new Intent(DrvMainActivity.this, PassEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("passid", passid);
		DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void operate(final long orderid, final int ordertype, int orderstate, final String passPhone, final double distance) {
		STDrvOrderListItem orderItem = null;
		for (int i = 0; i < arrOrders.size(); i++) {
			STDrvOrderListItem listItem = arrOrders.get(i);
			if (orderid == listItem.uid && ordertype == listItem.type) {
				orderItem = listItem;
				break;
			}
		}

		if (orderItem == null)
			return;

		switch (orderstate) {
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
			case ConstData.ORDER_STATE_STARTED:
			case ConstData.ORDER_STATE_DRV_ARRIVED:
			case ConstData.ORDER_STATE_PASS_GETON: {
				// go to zhixing
				switch (ordertype) {
					case ConstData.ORDER_TYPE_ONCE:
					case ConstData.ORDER_TYPE_ONOFF: {
						CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvMainActivity.this)
								.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
								.message(getResources().getString(R.string.quedingzhixing))
								.positiveListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										if (Global.isOpenGPS(DrvMainActivity.this)) {
											Intent intent = new Intent(DrvMainActivity.this, DrvCityOrderExeActivity.class);
											intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
											intent.putExtra("orderid", orderid);
											intent.putExtra("ordertype", ordertype);
                                            intent.putExtra("distance",distance);
                                            intent.putExtra("pass_phone", passPhone);
											DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
											startActivityForResult(intent, REQCODE_EXEC_CITYORDER);
										} else {
											CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvMainActivity.this)
													.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
													.message(getResources().getString(R.string.mustopengps))
													.positiveTitle(getResources().getString(R.string.mustopengps_ok))
													.negativeTitle(getResources().getString(R.string.mustopengps_cancel))
													.positiveListener(new View.OnClickListener() {
														@Override
														public void onClick(View v) {
															Global.openGPSSetting(DrvMainActivity.this);
														}
													})
													.build();
											dialog.show();
										}
									}
								})
								.build();
						dialog.show();
						break;
					}
					case ConstData.ORDER_TYPE_LONG: {
						Intent intent = new Intent(DrvMainActivity.this, DrvLongOrderExeActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
						intent.putExtra("orderid", orderid);
						intent.putExtra("ordertype", ordertype);
						DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
						startActivityForResult(intent, REQCODE_EXEC_LONGORDER);

						break;
					}
				}
				break;
			}
			case ConstData.ORDER_STATE_FINISHED:
				break;
			case ConstData.ORDER_STATE_PAYED: {
				Intent intentEval = new Intent(DrvMainActivity.this, EvaluateActivity.class);
				intentEval.putExtra("driver", Global.loadUserID(getApplicationContext()));
				intentEval.putExtra("passenger", orderItem.pass_id);
				intentEval.putExtra("order_type", orderItem.type);
				intentEval.putExtra("orderid", orderid);
				intentEval.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);
				intentEval.putExtra("view_mode", false);
				intentEval.putExtra("level", orderItem.evaluated);
				intentEval.putExtra("msg", orderItem.eval_content);
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivityForResult(intentEval, REQCODE_EVALUTE);
				break;
			}
			case ConstData.ORDER_STATE_EVALUATED: {
				Intent intentEvaled = new Intent(DrvMainActivity.this, EvaluateActivity.class);
				intentEvaled.putExtra("driver", Global.loadUserID(getApplicationContext()));
				intentEvaled.putExtra("passenger", orderItem.pass_id);
				intentEvaled.putExtra("order_type", orderItem.type);
				intentEvaled.putExtra("orderid", orderid);
				intentEvaled.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);
				intentEvaled.putExtra("view_mode", true);
				intentEvaled.putExtra("level", orderItem.evaluated);
				intentEvaled.putExtra("msg", orderItem.eval_content);
				DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intentEvaled);
				break;
			}
			case ConstData.ORDER_STATE_CLOSED: {
				break;
			}
		}
	}


	private void selectOrder(long orderid, int ordertype) {
		if (ordertype == ConstData.ORDER_TYPE_ONCE) {
			Intent intent = new Intent(DrvMainActivity.this, DrvOnceOrderDetailActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			intent.putExtra("orderid", orderid);
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_SELECT_ONCE_ORDER);
//		} else if (ordertype == ConstData.ORDER_TYPE_ONOFF) {
//			Intent intent = new Intent(DrvMainActivity.this, DrvOnoffOrderDetailActivity.class);
//			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//			intent.putExtra("orderid", orderid);
//			intent.putExtra("ordertype", ordertype);
//			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//			startActivityForResult(intent, REQCODE_SELECT_ONOFF_ORDER);
		} else if (ordertype == ConstData.ORDER_TYPE_LONG) {
			Intent intent = new Intent(DrvMainActivity.this, DrvLongOrderDetailActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			intent.putExtra("orderid", orderid);
			DrvMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_SELECT_LONG_ORDER);
		}
	}


	/*
	 Share methods
	*/
	private void showShareActionSheet() {
		if (outputUrl != null) {
			mShareController.openShare(DrvMainActivity.this, share_listener);
		} else {
			Global.showAdvancedToast(DrvMainActivity.this,
					getResources().getString(R.string.STR_CONN_ERROR),
					Gravity.CENTER);
		}
	}


    private void setShareContent(String outputUrl, String outputContent)
    {
        if (outputUrl == null || outputContent == null)
            return;
        String title = "";
        String content = "";
        if(outputContent.equals(getResources().getString(R.string.MAIN_DOWNLOAD_CONTENT))){
            title = getResources().getString(R.string.MAIN_DOWNLOAD_TITLE);
            content = getResources().getString(R.string.MAIN_DOWNLOAD_CONTENT);
        }else{
            title = getResources().getString(R.string.SHARE_TITLE);
            content = getResources().getString(R.string.SHARE_CONTENT);
        }
        // ����SSO
        mShareController.getConfig().setSsoHandler(new SinaSsoHandler());
        mShareController.getConfig().setSinaCallbackUrl("http://www.ookuaipin.com");

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(DrvMainActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(DrvMainActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        mShareController.setShareContent(content);			   // Default share contents

        // wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
        String appId = "wx8a669036953334b3";
        String appSecret = "4bf6b20eec181a8af8479ca4514b9291";
        // ���΢��ƽ̨
        UMWXHandler wxHandler = new UMWXHandler(DrvMainActivity.this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // ֧��΢������Ȧ
        UMWXHandler wxCircleHandler = new UMWXHandler(DrvMainActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // ��Ӷ���
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();

//		UMImage urlImage = new UMImage(PassMainActivity.this,
//				"http://www.umeng.com/images/pic/home/social/img-1.png");
        UMImage urlImage = new UMImage(DrvMainActivity.this, R.drawable.icon);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(outputUrl);
        weixinContent.setShareMedia(urlImage);
        mShareController.setShareMedia(weixinContent);

        // ��������Ȧ���������
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(outputUrl);
        mShareController.setShareMedia(circleMedia);

        // ����QQ�ռ��������
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


        // ���ö��ŷ�������
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(content);
        sms.setShareImage(urlImage);
        mShareController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent(urlImage);
        sinaContent.setShareContent(content);
        mShareController.setShareMedia(sinaContent);

        mShareController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
    }


    private AsyncHttpResponseHandler shareContents_handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE) {
					JSONObject retdata = result.getJSONObject("retdata");
                    outputUrl = retdata.getString("output_url");
                    outputContent = retdata.getString("output_content");
//                    outputContent = outputContent.replace("$invitecode",
//                            Global.loadInviteCode(getApplicationContext()));
                    outputUrl = outputUrl.replace("${activityDto.faqiId}",Global.loadUserID(getApplicationContext())+"");
                    Utils.mLogError("分享链接："+outputUrl);
                    Utils.mLogError("分享内容："+outputContent);
                    setShareContent(outputUrl, outputContent);
                    stopProgress();
                    showShareActionSheet();
				} else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH) {
					logout(szRetMsg);
				} else {
					Global.showAdvancedToast(DrvMainActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler logout_handler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0) {
					logout(getResources().getString(R.string.STR_LOGOUT_SUCCESS));
				} else if (nRetCode == -2) {
					logout(szRetMsg);
				} else {
					Global.showAdvancedToast(DrvMainActivity.this,
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
			Global.showAdvancedToast(DrvMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private SocializeListeners.SnsPostListener share_listener = new SocializeListeners.SnsPostListener() {
		@Override
		public void onStart() {
			SHARE_MEDIA media = mShareController.getConfig().getSelectedPlatfrom();
			if (media == SHARE_MEDIA.QZONE ||
					media == SHARE_MEDIA.QQ) {
				if (!Global.IsQQInstalled(getApplicationContext())) {
					Global.showTextToast(DrvMainActivity.this, getResources().getString(R.string.STR_QQ_NOTINSTALLED));
					return;
				}
			}
		}

		@Override
		public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
		}
	};


	private void showNewsBadge(boolean hasNews) {
		if (hasNews) {
			if (tab_shouye.getVisibility() != View.VISIBLE)
				img_hasnews.setVisibility(View.INVISIBLE);
			else
				img_hasnews.setVisibility(View.VISIBLE);
		}
		else
			img_hasnews.setVisibility(View.INVISIBLE);
	}


	public void resetUserImage()
	{
		imgPhoto.setImage(new SmartImage() {
            @Override
            public Bitmap getBitmap(Context context) {
                return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
            }
        });
	}


}
