package com.damytech.MainApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.damytech.DataClasses.*;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.HorProgressor;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImageView;
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
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-7-30
 * Time: 下午9:40
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends SuperActivity
{
	private final int REQCODE_TAB_NEWS = 1;
	private final int REQCODE_TAB_PERSONINFO = 2;
	private final int REQCODE_TAB_SHARE = 3;

	private RelativeLayout tab_app_layout = null;
	private RelativeLayout tab_news_layout = null;
	private RelativeLayout tab_person_layout = null;
	private RelativeLayout tab_share_layout = null;

	private ImageView imgTabApp = null;
	private ImageView imgTabNews = null;
	private ImageView imgTabPerson = null;
	private ImageView imgTabShare = null;

	private TextView txtTabApp = null;
	private TextView txtTabNews = null;
	private TextView txtTabPerson = null;
	private TextView txtTabShare = null;

	private ImageButton btnTabApp = null;
	private ImageButton btnTabNews = null;
	private ImageButton btnTabPerson = null;
	private ImageButton btnTabShare = null;

	//////////////////////////////////////// Apps tab controls /////////////////////////////////////////
	private final int PAGE_APP_COUNT = 3;
	private int ICON_WIDTH = 90, ICON_HEIGHT = 90;
	private int ICON_FONTSIZE = 21;

	private LinearLayout installed_apps_layout = null;
	private LinearLayout notinstalled_apps_layout = null;

	private ArrayList<STAppControlHolder> arrHolders = new ArrayList<STAppControlHolder>();
	private ArrayList<STApplication> arrApps = new ArrayList<STApplication>();
	////////////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////// News tab controls /////////////////////////////////////////
	private RelativeLayout page_indicator_layout = null;
	private TextView txt_title_gonggao = null, txt_title_dingdanxiaoxi = null, txt_title_tongzhi = null;
	private Button btn_title_gonggao = null, btn_title_dingdanxiaoxi = null, btn_title_tongzhi = null;
	private HorizontalPager news_list_pager = null;

	private PullToRefreshListView announce_listview = null;
	private PullToRefreshListView ordernotif_listview = null;
	private PullToRefreshListView personnotif_listview = null;

	private AnnounceAdapter announce_adapter = new AnnounceAdapter();
	private OrdernotifAdapter ordernotif_adapter = new OrdernotifAdapter();
	private PersonnotifAdapter personnotif_adapter = new PersonnotifAdapter();

	private ArrayList<STAnnouncement> arrAnnouncements = new ArrayList<STAnnouncement>();
	private ArrayList<STNotifyOrder> arrOrderNotifs = new ArrayList<STNotifyOrder>();
	private ArrayList<STNotifyPerson> arrPersonNotifs = new ArrayList<STNotifyPerson>();

	private ArrayList<Object> arrTempData = new ArrayList<Object>();

	private int anc_page = 0, order_notif_page = 0, person_notif_page = 0;

	private ImageView imgBadgeTab = null, imgBadgeTitle1 = null, imgBadgeTitle2 = null, imgBadgeTitle3 = null;

	private RelativeLayout coupon_container_layout = null;
	private Button btn_coupon_content = null;
	private ImageView img_coupon = null;
	private TextView txt_coupon_code = null;
	private TextView txt_coupon_content = null;
	private TextView txt_spreadunit_name = null;

	private RelativeLayout news_content_layout = null;
	private TextView txt_news_details = null;
	////////////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////// Person tab controls /////////////////////////////////////////
	private ImageButton btnPersonInfo = null, btnMoney = null, btnCoupon = null, btnAbout = null;
	private Button btnLogout = null;
	////////////////////////////////////////////////////////////////////////////////////////////////////


	//////////////////////////////////////// Share tab controls /////////////////////////////////////////
	private WebView shareWebView = null;
	private ImageButton btnShare = null;
	private final UMSocialService mShareController = UMServiceFactory.getUMSocialService("com.umeng.share");
	private boolean initShareListener = false;

    private String outputUrl;
    private String outputContent;
	////////////////////////////////////////////////////////////////////////////////////////////////////


	public void onCreate(Bundle savedInstanceState) {
        Log.d("erik_debug", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);

		initControls();
		initResolution();

        Global._sharedContext = getApplicationContext();
	}

	@Override
	protected void onResume() {
		super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
		updateAppsOnUI();

		//txtInvitecode.setText(Global.loadInviteCode(getApplicationContext()));
		//getDefaultShareContents();
	}

	@Override
	protected void onPause() {
		super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
	}

	private void initControls()
	{
		if (!initShareListener) {
			mShareController.registerListener(share_listener);
			initShareListener = true;
		}

		tab_app_layout = (RelativeLayout)findViewById(R.id.tab_app_layout);
		tab_news_layout = (RelativeLayout)findViewById(R.id.tab_news_layout);
		tab_person_layout = (RelativeLayout)findViewById(R.id.tab_person_layout);
		tab_share_layout = (RelativeLayout)findViewById(R.id.tab_share_layout);

		tab_app_layout.setVisibility(View.VISIBLE);
		tab_news_layout.setVisibility(View.INVISIBLE);
		tab_person_layout.setVisibility(View.INVISIBLE);
		tab_share_layout.setVisibility(View.INVISIBLE);

		imgTabApp = (ImageView)findViewById(R.id.img_app);
		imgTabNews = (ImageView)findViewById(R.id.img_news);
		imgTabPerson = (ImageView)findViewById(R.id.img_person);
		imgTabShare = (ImageView)findViewById(R.id.img_share);

		txtTabApp = (TextView)findViewById(R.id.txt_app);
		txtTabNews = (TextView)findViewById(R.id.txt_news);
		txtTabPerson = (TextView)findViewById(R.id.txt_person);
		txtTabShare = (TextView)findViewById(R.id.txt_share);

		btnTabApp = (ImageButton)findViewById(R.id.btn_tab_app);
		btnTabNews = (ImageButton)findViewById(R.id.btn_tab_news);
		btnTabPerson = (ImageButton)findViewById(R.id.btn_tab_person);
		btnTabShare = (ImageButton)findViewById(R.id.btn_tab_share);

		btnTabApp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabApp();
			}
		});
		btnTabNews.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabNews();
			}
		});
		btnTabPerson.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabPerson();
			}
		});
		btnTabShare.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectTabShare();
			}
		});

		//////////////////////////////////////////////////////////////////////////////////
		// Application tab controls
		//////////////////////////////////////////////////////////////////////////////////
		installed_apps_layout = (LinearLayout)findViewById(R.id.installed_apps_layout);
		notinstalled_apps_layout = (LinearLayout)findViewById(R.id.notinstalled_apps_layout);

		CommManager.getLoginInfoFromDevToken(Global.getIMEI(getApplicationContext()), logininfo_handler);

		startProgress();
		CommManager.getChildAppList(child_apps_handler);

		//////////////////////////////////////////////////////////////////////////////////
		// News tab controls
		//////////////////////////////////////////////////////////////////////////////////
		page_indicator_layout = (RelativeLayout)findViewById(R.id.indicator_layout);

		txt_title_gonggao = (TextView)findViewById(R.id.txt_title_gonggao);
		txt_title_dingdanxiaoxi = (TextView)findViewById(R.id.txt_title_order_notif);
		txt_title_tongzhi = (TextView)findViewById(R.id.txt_title_person_notif);

		txt_title_gonggao.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
		txt_title_dingdanxiaoxi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
		txt_title_tongzhi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));

		btn_title_gonggao = (Button)findViewById(R.id.btn_title_gonggao);
		btn_title_dingdanxiaoxi = (Button)findViewById(R.id.btn_title_dingdanxiaoxi);
		btn_title_tongzhi = (Button)findViewById(R.id.btn_title_tongzhi);

		btn_title_gonggao.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickTitleGongGao();
			}
		});

		btn_title_dingdanxiaoxi.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickTitleDingDanXiaoXi();
			}
		});

		btn_title_tongzhi.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickTitleTongZhi();
			}
		});

		news_list_pager = (HorizontalPager)findViewById(R.id.news_list_pager);
		news_list_pager.setScrollChangeListener(horScrolledListener);

		news_list_pager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener()
		{
			@Override
			public void onScreenSwitched(int screen) {
				if (screen == 0)
				{
					if (imgBadgeTitle1.getVisibility() == View.VISIBLE)
					{
						TimerTask task = new TimerTask()
						{
							@Override
							public void run() {
								runOnUiThread(new Runnable()
								{
									@Override
									public void run() {
										imgBadgeTitle1.setVisibility(View.INVISIBLE);
										if (imgBadgeTitle2.getVisibility() == View.INVISIBLE &&
												imgBadgeTitle3.getVisibility() == View.INVISIBLE)
										{
											imgBadgeTab.setVisibility(View.INVISIBLE);
										}
									}
								});
							}
						};

						Timer timer = new Timer();
						timer.schedule(task, 500);
					}
				}
				else if (screen == 1)
				{
					if (imgBadgeTitle2.getVisibility() == View.VISIBLE)
					{
						TimerTask task = new TimerTask()
						{
							@Override
							public void run() {
								runOnUiThread(new Runnable()
								{
									@Override
									public void run() {
										imgBadgeTitle2.setVisibility(View.INVISIBLE);
//										CommManager.readOrderNotifs(Global.loadUserID(getApplicationContext()), Global.loadLastOrderNotificationID(getApplicationContext()), Global.getIMEI(getApplicationContext()), readordernotifs_handler);

										if (imgBadgeTitle1.getVisibility() == View.INVISIBLE &&
												imgBadgeTitle3.getVisibility() == View.INVISIBLE)
										{
											imgBadgeTab.setVisibility(View.INVISIBLE);
										}
									}
								});
							}
						};

						Timer timer = new Timer();
						timer.schedule(task, 500);
					}
				}
				else if (screen == 2)
				{
					if (imgBadgeTitle3.getVisibility() == View.VISIBLE)
					{
						TimerTask task = new TimerTask()
						{
							@Override
							public void run() {
								runOnUiThread(new Runnable()
								{
									@Override
									public void run() {
										imgBadgeTitle3.setVisibility(View.INVISIBLE);
//										CommManager.readPersonNotifs(Global.loadUserID(getApplicationContext()), Global.loadLastPersonNotificationID(getApplicationContext()), Global.getIMEI(getApplicationContext()), readpersonnotifs_handler);
										if (imgBadgeTitle2.getVisibility() == View.INVISIBLE &&
												imgBadgeTitle1.getVisibility() == View.INVISIBLE)
										{
											imgBadgeTab.setVisibility(View.INVISIBLE);
										}
									}
								});
							}
						};

						Timer timer = new Timer();
						timer.schedule(task, 500);
					}
				}
			}
		});

		announce_listview = new PullToRefreshListView(news_list_pager.getContext());
		{
			RelativeLayout.LayoutParams temp_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			announce_listview.setLayoutParams(temp_layout_params);
			announce_listview.setMode(PullToRefreshBase.Mode.BOTH);
			announce_listview.setOnRefreshListener(announcement_list_refresh_listener);
			announce_listview.setAdapter(announce_adapter);
            announce_listview.setBackgroundColor(Color.WHITE);
		}
		news_list_pager.addView(announce_listview);

		ordernotif_listview = new PullToRefreshListView(news_list_pager.getContext());
		{
			RelativeLayout.LayoutParams temp_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			ordernotif_listview.setLayoutParams(temp_layout_params);
			ordernotif_listview.setMode(PullToRefreshBase.Mode.BOTH);
			ordernotif_listview.setOnRefreshListener(ordernotif_list_refresh_listener);
			ordernotif_listview.setAdapter(ordernotif_adapter);
            ordernotif_listview.setBackgroundColor(Color.WHITE);
		}
		news_list_pager.addView(ordernotif_listview);

		personnotif_listview = new PullToRefreshListView(news_list_pager.getContext());
		{
			RelativeLayout.LayoutParams temp_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			personnotif_listview.setLayoutParams(temp_layout_params);
			personnotif_listview.setMode(PullToRefreshBase.Mode.BOTH);
			personnotif_listview.setOnRefreshListener(personnotif_list_refresh_listener);
			personnotif_listview.setAdapter(personnotif_adapter);
            personnotif_listview.setBackgroundColor(Color.WHITE);
		}
		news_list_pager.addView(personnotif_listview);

		imgBadgeTab = (ImageView)findViewById(R.id.img_tab_new_mark);
		imgBadgeTitle1 = (ImageView)findViewById(R.id.img_new_mark_gonggao);
		imgBadgeTitle2 = (ImageView)findViewById(R.id.img_new_mark_dingdanxiaoxi);
		imgBadgeTitle3 = (ImageView)findViewById(R.id.img_new_mark_tongzhi);
		imgBadgeTab.setVisibility(View.INVISIBLE);
		imgBadgeTitle1.setVisibility(View.INVISIBLE);
		imgBadgeTitle2.setVisibility(View.INVISIBLE);
		imgBadgeTitle3.setVisibility(View.INVISIBLE);

		// Items for coupon content
		coupon_container_layout = (RelativeLayout)findViewById(R.id.coupon_contain_layout);

		btn_coupon_content = (Button)findViewById(R.id.btn_coupon_content);
		btn_coupon_content.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				coupon_container_layout.setVisibility(View.INVISIBLE);
			}
		});

		coupon_container_layout.setVisibility(View.INVISIBLE);
		img_coupon = (ImageView)findViewById(R.id.img_coupon);
		txt_coupon_code = (TextView)findViewById(R.id.txt_coupon_code);
		txt_coupon_content = (TextView)findViewById(R.id.txt_coupon_content);
		txt_spreadunit_name = (TextView)findViewById(R.id.txt_spreadunit_name);


		news_content_layout = (RelativeLayout)findViewById(R.id.news_content_layout);
		txt_news_details = (TextView)findViewById(R.id.txt_news_detail);
		news_content_layout.setVisibility(View.INVISIBLE);
		news_content_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content_layout.setVisibility(View.INVISIBLE);
			}
		});



		//////////////////////////////////////////////////////////////////////////////////
		// Person tab controls
		//////////////////////////////////////////////////////////////////////////////////
		btnPersonInfo = (ImageButton)findViewById(R.id.btn_wodegerenxinxi);
		btnMoney = (ImageButton)findViewById(R.id.btn_wodejianpailvdian);
		btnCoupon = (ImageButton)findViewById(R.id.btn_wodedianquan);
		btnAbout = (ImageButton)findViewById(R.id.btn_guanyuoo);
		btnLogout = (Button)findViewById(R.id.btn_logout);

		btnPersonInfo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickPersonInfo();
			}
		});

		btnMoney.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickMoney();
			}
		});

		btnCoupon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickCoupon();
			}
		});

		btnAbout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickAbout();
			}
		});

		btnLogout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickLogout();
			}
		});


		//////////////////////////////////////////////////////////////////////////////////
		// Person tab controls
		//////////////////////////////////////////////////////////////////////////////////
        initShareTabControls();
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
        btnShare = (ImageButton)findViewById(R.id.btn_show_share);
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
        String[] urlList = originUrl.split(".do");
        getDefaultShareContents(urlList[0]+".do", 0);
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

	public void onSelectTabApp()
	{
		txtTabApp.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
		imgTabApp.setBackgroundResource(R.drawable.tab_app_sel);

		txtTabNews.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
		imgTabNews.setBackgroundResource(R.drawable.tab_news_normal);

		txtTabPerson.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
		imgTabPerson.setBackgroundResource(R.drawable.tab_person_normal);

		txtTabShare.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
		imgTabShare.setBackgroundResource(R.drawable.tab_share_normal);

		tab_app_layout.setVisibility(View.VISIBLE);
		tab_news_layout.setVisibility(View.INVISIBLE);
		tab_person_layout.setVisibility(View.INVISIBLE);
		tab_share_layout.setVisibility(View.INVISIBLE);

		btnShare.setVisibility(View.INVISIBLE);
	}


	public void onSelectTabNews()
	{
		if (!Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_NEWS);
		}
		else
		{
			txtTabApp.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabApp.setBackgroundResource(R.drawable.tab_app_normal);

			txtTabNews.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
			imgTabNews.setBackgroundResource(R.drawable.tab_news_sel);

			txtTabPerson.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabPerson.setBackgroundResource(R.drawable.tab_person_normal);

			txtTabShare.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabShare.setBackgroundResource(R.drawable.tab_share_normal);

			tab_app_layout.setVisibility(View.INVISIBLE);
			tab_news_layout.setVisibility(View.VISIBLE);
			tab_person_layout.setVisibility(View.INVISIBLE);
			tab_share_layout.setVisibility(View.INVISIBLE);

			if (arrAnnouncements.size() == 0)
				getOlderAnnouncements();

			btnShare.setVisibility(View.INVISIBLE);
		}
	}

	public void onSelectTabPerson()
	{
		if (!Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_PERSONINFO);
		}
		else
		{
			txtTabApp.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabApp.setBackgroundResource(R.drawable.tab_app_normal);

			txtTabNews.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabNews.setBackgroundResource(R.drawable.tab_news_normal);

			txtTabPerson.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
			imgTabPerson.setBackgroundResource(R.drawable.tab_person_sel);

			txtTabShare.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabShare.setBackgroundResource(R.drawable.tab_share_normal);

			tab_app_layout.setVisibility(View.INVISIBLE);
			tab_news_layout.setVisibility(View.INVISIBLE);
			tab_person_layout.setVisibility(View.VISIBLE);
			tab_share_layout.setVisibility(View.INVISIBLE);

			btnShare.setVisibility(View.INVISIBLE);
		}
	}

	public void onSelectTabShare()
	{
		if (!Global.isLoggedIn(getApplicationContext()))
		{
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
			startActivityForResult(intent, REQCODE_TAB_SHARE);
		}
		else
		{
			txtTabApp.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabApp.setBackgroundResource(R.drawable.tab_app_normal);

			txtTabNews.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabNews.setBackgroundResource(R.drawable.tab_news_normal);

			txtTabPerson.setTextColor(getResources().getColor(R.color.TAB_NORMAL_COLOR));
			imgTabPerson.setBackgroundResource(R.drawable.tab_person_normal);

			txtTabShare.setTextColor(getResources().getColor(R.color.TAB_SEL_COLOR));
			imgTabShare.setBackgroundResource(R.drawable.tab_share_sel);

			tab_app_layout.setVisibility(View.INVISIBLE);
			tab_news_layout.setVisibility(View.INVISIBLE);
			tab_person_layout.setVisibility(View.INVISIBLE);
			tab_share_layout.setVisibility(View.VISIBLE);

			//getDefaultShareContents();
			btnShare.setVisibility(View.VISIBLE);
		}
	}

    private void getDefaultShareContents(String inputUrl, int shareFlag)
    {
        if (Global.isLoggedIn(getApplicationContext()))
        {
            CommManager.defaultShareContents(Global.loadUserID(getApplicationContext()),
                    /**inputUrl, shareFlag, */Global.getIMEI(getApplicationContext()),
                    default_contents_handler);
        }
    }

	private AsyncHttpResponseHandler child_apps_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.

			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);

				JSONObject jsonResult = jsonObj.getJSONObject("result");
				int nRetCode = jsonResult.getInt("retcode");
				String szMsg = jsonResult.getString("retmsg");
				JSONArray arrAppItems = jsonResult.getJSONArray("retdata");

				if (nRetCode < 0)
				{
					Global.showAdvancedToast(MainActivity.this, szMsg, Gravity.CENTER);
				}
				else
				{
					arrApps.clear();
					arrHolders.clear();

					for (int i = 0; i < arrAppItems.length(); i++)
					{
						JSONObject appItem = arrAppItems.getJSONObject(i);
						STApplication application = STApplication.dcodeFromJSON(appItem);
						application.state = Global.getApplicationState(getApplicationContext(), application);

						if (appStateIsInstalled(application.state))
							Global.removeApksForApp(getApplicationContext(), application);

						arrApps.add(application);
					}

					displayAppsToUI();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				Global.showAdvancedToast(MainActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.

			stopProgress();
			Global.showAdvancedToast(MainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void updateAppsOnUI()
	{
//        notinstalled_apps_layout.removeAllViews();
//        installed_apps_layout.removeAllViews();

		for (int i = 0; i < arrApps.size(); i++)
		{
			STApplication app = arrApps.get(i);

			int nOldState = app.state, nNewState;
			if (nOldState == STApplication.APP_STATE_NOW_DOWNLOADING)
				continue;

			nNewState = Global.getApplicationState(getApplicationContext(), app);
			if (nOldState == nNewState)
				continue;

			STAppControlHolder holder = getHolderFromAppID(app.uid);
			app.state = nNewState;

			if (!appStateIsInstalled(nNewState) && appStateIsInstalled(nOldState))
			{
                LinearLayout mLayout=(LinearLayout)holder.containerView.getParent();
                installed_apps_layout.removeView(mLayout);
                //mLayout.removeView(holder.containerView);

//                LinearLayout tempLayout = new LinearLayout(MainActivity.this);
//                tempLayout.setOrientation(LinearLayout.VERTICAL);
//                LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                tempLayout.setLayoutParams(layout_params);
//                tempLayout.addView(holder.containerView);
                notinstalled_apps_layout.addView(mLayout);
			}
			else if (appStateIsInstalled(nNewState) && !appStateIsInstalled(nOldState))
			{
                LinearLayout mLayout=(LinearLayout)holder.containerView.getParent();
                mLayout.removeView(holder.containerView);

                LinearLayout tempLayout = new LinearLayout(MainActivity.this);
                tempLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                tempLayout.setLayoutParams(layout_params);
                tempLayout.addView(holder.containerView);
                installed_apps_layout.addView(tempLayout);
			}

			updateControlHolderForApp(app);
		}
	}

	private void displayAppsToUI()
	{
		installed_apps_layout.removeAllViews();
		notinstalled_apps_layout.removeAllViews();

		LinearLayout itemLayout = null;
		boolean isNewItem = true;

		// Add not installed applications
		for (int i = 0; i < arrApps.size(); i++)
		{
			STApplication app = arrApps.get(i);
			if (appStateIsInstalled(app.state))
				continue;

			if (itemLayout == null) {
				itemLayout = new LinearLayout(MainActivity.this);
				itemLayout.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
				itemLayout.setLayoutParams(layout_params);

				isNewItem = true;
			} else {
				isNewItem = false;
			}

			RelativeLayout subitemLayout = createAppLayout(app);
			LinearLayout.LayoutParams subitem_layoutParams = (LinearLayout.LayoutParams)subitemLayout.getLayoutParams();
			subitem_layoutParams.height = 0;
			subitem_layoutParams.weight = 1;

			itemLayout.addView(subitemLayout);

			if (!isNewItem) {
				notinstalled_apps_layout.addView(itemLayout);
				itemLayout = null;
			}
		}

		if (itemLayout != null)
			notinstalled_apps_layout.addView(itemLayout);


		itemLayout = null;
		isNewItem = true;


		// Add installed applications
		for (int i = 0; i < arrApps.size(); i++)
		{
			STApplication app = arrApps.get(i);
			if (!appStateIsInstalled(app.state))
				continue;

			if (itemLayout == null) {
				itemLayout = new LinearLayout(MainActivity.this);
				itemLayout.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
				itemLayout.setLayoutParams(layout_params);

				isNewItem = true;
			} else {
				isNewItem = false;
			}

			RelativeLayout subitemLayout = createAppLayout(app);
			LinearLayout.LayoutParams subitem_layoutParams = (LinearLayout.LayoutParams)subitemLayout.getLayoutParams();
			subitem_layoutParams.height = 0;
			subitem_layoutParams.weight = 1;

			itemLayout.addView(subitemLayout);

			if (!isNewItem) {
				installed_apps_layout.addView(itemLayout);
				itemLayout = null;
			}
		}

		if (itemLayout != null)
			installed_apps_layout.addView(itemLayout);

		ResolutionSet.instance.iterateChild(findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);
	}


	private RelativeLayout createAppLayout(STApplication application)
	{
		RelativeLayout itemLayout = new RelativeLayout(MainActivity.this);
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
				ResolutionSet.getBaseWidth() / PAGE_APP_COUNT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		itemLayout.setLayoutParams(layout_params);

		SmartImageView iconView = new SmartImageView(itemLayout.getContext());
		RelativeLayout.LayoutParams icon_layout_params = new RelativeLayout.LayoutParams(ICON_WIDTH, ICON_HEIGHT);
		icon_layout_params.setMargins(0, 10, 0, 0);
		icon_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		iconView.setLayoutParams(icon_layout_params);
		iconView.setBackgroundResource(R.drawable.demoicon);
		iconView.setImageUrl(application.iconurl);
		iconView.setId(1);
		itemLayout.addView(iconView);

		TextView appNameView = new TextView(itemLayout.getContext());
		RelativeLayout.LayoutParams name_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 30);
		name_layout_params.setMargins(0, 10, 0, 0);
		name_layout_params.addRule(RelativeLayout.BELOW, iconView.getId());
		appNameView.setLayoutParams(name_layout_params);
		appNameView.setGravity(Gravity.CENTER);
		appNameView.setTextColor(Color.rgb(134, 134, 134));
		appNameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ICON_FONTSIZE);
		appNameView.setText(application.name);
		itemLayout.addView(appNameView);

		// Create items and show according to app's state
		STAppControlHolder holder = new STAppControlHolder();
		{
			holder.uid = application.uid;

			holder.containerView = itemLayout;

			ImageView imgNew = new ImageView(itemLayout.getContext());
			RelativeLayout.LayoutParams imgnew_layout_params = new RelativeLayout.LayoutParams(ICON_WIDTH / 3, ICON_HEIGHT/ 3);
			imgnew_layout_params.addRule(RelativeLayout.ALIGN_TOP, iconView.getId());
			imgnew_layout_params.addRule(RelativeLayout.ALIGN_RIGHT, iconView.getId());
			imgNew.setLayoutParams(imgnew_layout_params);
			imgNew.setBackgroundResource(R.drawable.new_badge);
			holder.imgNew = imgNew;
			itemLayout.addView(holder.imgNew);

			ImageView imgPlay = new ImageView(itemLayout.getContext());
			RelativeLayout.LayoutParams imgplay_layout_params = new RelativeLayout.LayoutParams(ICON_WIDTH / 2, ICON_HEIGHT / 2);
			imgplay_layout_params.setMargins(ICON_WIDTH / 4, ICON_HEIGHT / 4, 0, 0);
			imgplay_layout_params.addRule(RelativeLayout.ALIGN_LEFT, iconView.getId());
			imgplay_layout_params.addRule(RelativeLayout.ALIGN_TOP, iconView.getId());
			imgPlay.setId(3);
			imgPlay.setLayoutParams(imgplay_layout_params);
			imgPlay.setBackgroundResource(R.drawable.bk_play);
			holder.imgPlay = imgPlay;
			itemLayout.addView(imgPlay);

			ImageView imgPause = new ImageView(itemLayout.getContext());
			RelativeLayout.LayoutParams imgpause_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			imgpause_layout_params.addRule(RelativeLayout.ALIGN_LEFT, imgPlay.getId());
			imgpause_layout_params.addRule(RelativeLayout.ALIGN_TOP, imgPlay.getId());
			imgpause_layout_params.addRule(RelativeLayout.ALIGN_RIGHT, imgPlay.getId());
			imgpause_layout_params.addRule(RelativeLayout.ALIGN_BOTTOM, imgPlay.getId());
			imgPause.setLayoutParams(imgpause_layout_params);
			imgPause.setBackgroundResource(R.drawable.bk_pause);
			holder.imgPause = imgPause;
			itemLayout.addView(imgPause);

			ImageView imgInstall = new ImageView(itemLayout.getContext());
			RelativeLayout.LayoutParams imginstall_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			imginstall_layout_params.addRule(RelativeLayout.ALIGN_LEFT, imgPlay.getId());
			imginstall_layout_params.addRule(RelativeLayout.ALIGN_TOP, imgPlay.getId());
			imginstall_layout_params.addRule(RelativeLayout.ALIGN_RIGHT, imgPlay.getId());
			imginstall_layout_params.addRule(RelativeLayout.ALIGN_BOTTOM, imgPlay.getId());
			imgInstall.setLayoutParams(imginstall_layout_params);
			imgInstall.setBackgroundResource(R.drawable.bk_install);
			holder.imgInstall = imgInstall;
			itemLayout.addView(imgInstall);

			HorProgressor progressor = new HorProgressor(itemLayout.getContext(), Color.GREEN, Color.RED);
			RelativeLayout.LayoutParams progressor_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 7);
			progressor_params.setMargins(0, 10, 0, 0);
			progressor_params.addRule(RelativeLayout.BELOW, iconView.getId());
			progressor_params.addRule(RelativeLayout.ALIGN_LEFT, iconView.getId());
			progressor_params.addRule(RelativeLayout.ALIGN_RIGHT, iconView.getId());
			progressor.setLayoutParams(progressor_params);
			progressor.setId(2);
			holder.progressor = progressor;
			itemLayout.addView(progressor);

			TextView prog_label = new TextView(itemLayout.getContext());
			RelativeLayout.LayoutParams prog_label_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 21);
			prog_label_params.setMargins(0, 3, 0, 0);
			prog_label_params.addRule(RelativeLayout.BELOW, iconView.getId());
			prog_label_params.addRule(RelativeLayout.ALIGN_LEFT, progressor.getId());
			prog_label_params.addRule(RelativeLayout.ALIGN_RIGHT, progressor.getId());
			prog_label.setPadding(0, 0, 0, 0);
			prog_label.setLayoutParams(prog_label_params);
			prog_label.setGravity(Gravity.CENTER);
			prog_label.setTextColor(Color.BLACK);
			prog_label.setTextSize(TypedValue.COMPLEX_UNIT_PX, 15);
			holder.prog_label = prog_label;
			itemLayout.addView(prog_label);

			arrHolders.add(holder);

			updateControlHolderForApp(application);
		}

		ImageButton btnItem = new ImageButton(itemLayout.getContext());
		RelativeLayout.LayoutParams btn_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		btn_layout_params.addRule(RelativeLayout.ALIGN_LEFT, iconView.getId());
		btn_layout_params.addRule(RelativeLayout.ALIGN_RIGHT, iconView.getId());
		btn_layout_params.addRule(RelativeLayout.ALIGN_TOP, iconView.getId());
		btn_layout_params.addRule(RelativeLayout.ALIGN_BOTTOM, iconView.getId());
		btnItem.setLayoutParams(btn_layout_params);
		btnItem.setBackgroundResource(R.drawable.btn_empty);
		btnItem.setTag("" + application.uid);
		btnItem.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				try {
					String szUid = (String) v.getTag();
					long nUid = Long.parseLong(szUid);
					selectApp(nUid);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnItem.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v) {
				try
				{
					String szUid = (String) v.getTag();
					final long nUid = Long.parseLong(szUid);
					STApplication app = getApplicationFromID(nUid);
					if (app.state == STApplication.APP_STATE_NOW_DOWNLOADING ||
							app.state == STApplication.APP_STATE_INSTALLED ||
							app.state == STApplication.APP_STATE_UPDATABLE ||
							app.state == STApplication.APP_STATE_NOT_DOWNLOADED)
					{
						return false;
					}

					CommonAlertDialog dialog = new CommonAlertDialog.Builder(MainActivity.this)
							.message(getResources().getString(R.string.STR_CONFIRM_TO_REMOVE))
							.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
							.positiveTitle(getResources().getString(R.string.STR_SHI))
							.positiveListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									STApplication app = getApplicationFromID(nUid);
									if (app == null)
										return;

									Global.removeApksForApp(getApplicationContext(), app);
									if (Global.IsAppInstalled(getApplicationContext(), app.packname))
										app.state = STApplication.APP_STATE_UPDATABLE;
									else
										app.state = STApplication.APP_STATE_NOT_DOWNLOADED;

									app.downloadedBytes = 0;
									app.totalSize = -1;

									updateControlHolderForApp(app);
								}
							})
							.negativeTitle(getResources().getString(R.string.STR_FOU))
							.build();
					dialog.show();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

				return false;
			}
		});
		itemLayout.addView(btnItem);

		return itemLayout;
	}

	private void updateControlHolderForApp(STApplication application)
	{
		STAppControlHolder holder = getHolderFromAppID(application.uid);

		switch (application.state)
		{
			case STApplication.APP_STATE_INSTALLED:
			case STApplication.APP_STATE_NOT_DOWNLOADED:
				holder.imgNew.setVisibility(View.INVISIBLE);
				holder.imgPlay.setVisibility(View.INVISIBLE);
				holder.imgPause.setVisibility(View.INVISIBLE);
				holder.imgInstall.setVisibility(View.INVISIBLE);
				holder.progressor.setVisibility(View.INVISIBLE);
				holder.prog_label.setVisibility(View.INVISIBLE);
				break;
			case STApplication.APP_STATE_DOWNLOADED_HALF:
				holder.imgNew.setVisibility(View.INVISIBLE);
				holder.imgPlay.setVisibility(View.VISIBLE);
				holder.imgPause.setVisibility(View.INVISIBLE);
				holder.imgInstall.setVisibility(View.INVISIBLE);

				if (application.totalSize < 0)
				{
					holder.progressor.setVisibility(View.INVISIBLE);
					holder.prog_label.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.progressor.setVisibility(View.VISIBLE);
					holder.prog_label.setVisibility(View.INVISIBLE);
				}
				break;
			case STApplication.APP_STATE_NOW_DOWNLOADING:
				holder.imgNew.setVisibility(View.INVISIBLE);
				holder.imgPlay.setVisibility(View.INVISIBLE);
				holder.imgPause.setVisibility(View.VISIBLE);
				holder.imgInstall.setVisibility(View.INVISIBLE);

				if (application.totalSize < 0)
				{
					holder.progressor.setVisibility(View.INVISIBLE);
					holder.prog_label.setVisibility(View.VISIBLE);
				}
				else
				{
					holder.progressor.setVisibility(View.VISIBLE);
					holder.prog_label.setVisibility(View.INVISIBLE);
				}
				break;
			case STApplication.APP_STATE_DOWNLOADED:
				holder.imgNew.setVisibility(View.INVISIBLE);
				holder.imgPlay.setVisibility(View.INVISIBLE);
				holder.imgPause.setVisibility(View.INVISIBLE);
				holder.imgInstall.setVisibility(View.VISIBLE);
				holder.progressor.setVisibility(View.INVISIBLE);
				holder.prog_label.setVisibility(View.INVISIBLE);
				break;
			case STApplication.APP_STATE_UPDATABLE:
				holder.imgNew.setVisibility(View.VISIBLE);
				holder.imgPlay.setVisibility(View.INVISIBLE);
				holder.imgPause.setVisibility(View.INVISIBLE);
				holder.imgInstall.setVisibility(View.INVISIBLE);
				holder.progressor.setVisibility(View.INVISIBLE);
				holder.prog_label.setVisibility(View.INVISIBLE);
				break;
		}

		updateAppDownloadProgress(application.uid);
	}

	private STApplication getApplicationFromID(long appid)
	{
		for (int i = 0; i < arrApps.size(); i++)
		{
			STApplication app = arrApps.get(i);
			if (app.uid == appid)
				return app;
		}

		return null;
	}

	private STAppControlHolder getHolderFromAppID(long id)
	{
		for (int i = 0; i < arrHolders.size(); i++)
		{
			STAppControlHolder holder = arrHolders.get(i);
			if (holder.uid == id)
				return holder;
		}

		return null;
	}

	private void selectApp(long appid)
	{
		STApplication selApp = getApplicationFromID(appid);
		if (selApp == null)
			return;

		STAppControlHolder holder = getHolderFromAppID(selApp.uid);
		if (holder == null)
			return;

		String apkPath = "";
		switch (selApp.state)
		{
			case STApplication.APP_STATE_NOT_DOWNLOADED:
			case STApplication.APP_STATE_DOWNLOADED_HALF:
			case STApplication.APP_STATE_UPDATABLE:
				downloadApp(selApp);
				break;
			case STApplication.APP_STATE_DOWNLOADED:
				apkPath = Global.getDirPathForApplication() + Global.getFileNameForApplication(selApp, STApplication.APP_STATE_DOWNLOADED, -1);
				InstallApp(apkPath);
				break;
			case STApplication.APP_STATE_INSTALLED:
				Global.startOtherApp(getApplicationContext(), selApp.packname);
				break;
			case STApplication.APP_STATE_NOW_DOWNLOADING:
				if (holder.downloadProgress != null)
				{
					holder.downloadProgress.cancel(true);
					holder.imgPlay.setVisibility(View.VISIBLE);
					holder.imgPause.setVisibility(View.INVISIBLE);

					selApp.state = STApplication.APP_STATE_DOWNLOADED_HALF;
				}
				break;
		}
	}

	private void downloadApp(final STApplication app)
	{
		CommonAlertDialog dialog = new CommonAlertDialog.Builder(MainActivity.this)
				.type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
				.positiveTitle(getResources().getString(R.string.STR_SHI))
				.positiveListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						STAppControlHolder holder = getHolderFromAppID(app.uid);
						if (holder == null)
							return;

						DownloadApp appProgress = new DownloadApp();
						appProgress.execute(app);
						holder.downloadProgress = appProgress;
					}
				})
				.negativeTitle(getResources().getString(R.string.STR_FOU))
				.build();

		if (app.state == STApplication.APP_STATE_DOWNLOADED_HALF) {
			dialog.setMessage(getResources().getString(R.string.STR_CONFIRM_TO_CONTINUE));
		} else {
			Global.removeApksForApp(getApplicationContext(), app);
			dialog.setMessage(getResources().getString(R.string.STR_CONFIRM_TO_DOWNLOAD));
		}

		dialog.show();
	}


	private class DownloadApp extends AsyncTask<STApplication, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		protected String doInBackground(STApplication... f_app) {
			if (f_app == null || f_app.length == 0)
				return null;

			int count;
			try {
				STApplication app = f_app[0];
				final long appid = app.uid;

				app.state = STApplication.APP_STATE_NOW_DOWNLOADING;

				URL url = new URL(app.downloadurl);
				URLConnection conection = url.openConnection();
				conection.connect();

				// getting file length
				final int lengthOfFile = conection.getContentLength();
				app.totalSize = lengthOfFile;

				runOnUiThread(new Runnable()
				{
					@Override
					public void run() {
						STApplication app = getApplicationFromID(appid);
						updateControlHolderForApp(app);
					}
				});

				InputStream stream = url.openStream();

				Global.skipInputStream(stream, app.downloadedBytes);

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(stream, 8192);

				// Output file path
				String outpath = Global.getDirPathForApplication() + Global.getFileNameForApplication(app, STApplication.APP_STATE_DOWNLOADED_HALF, lengthOfFile);

				// Output stream to write file
				FileOutputStream output = null;
				try {
					output = new FileOutputStream(outpath, true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				byte data[] = new byte[1024];

				long total = app.downloadedBytes;

				STAppControlHolder holder = getHolderFromAppID(appid);
				boolean isCancelled = false;
				while ((count = input.read(data)) != -1) {
					total += count;

					app.downloadedBytes = total;

					runOnUiThread(new Runnable()
					{
						@Override
						public void run() {
							updateAppDownloadProgress(appid);
						}
					});

					// writing data to file
					output.write(data, 0, count);

					if (holder.downloadProgress != null && holder.downloadProgress.isCancelled())
					{
						isCancelled = true;
						break;
					}
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

				if (isCancelled)
					return null;

				// Must tell the web service download is completed
				CommManager.recordDownload(app.packname, app.latestver, null);

				String apkDirPath = Global.getDirPathForApplication();
				String completedName = Global.getFileNameForApplication(app, STApplication.APP_STATE_DOWNLOADED, -1);
				final String fullPath = apkDirPath + completedName;

				File apkFile = new File(outpath);
				if (apkFile != null && apkFile.exists())
				{
					File newFile = new File(fullPath);
					newFile.deleteOnExit();
					apkFile.renameTo(newFile);
				}

				app.state = STApplication.APP_STATE_DOWNLOADED;

				runOnUiThread(new Runnable()
				{
					@Override
					public void run() {
						STAppControlHolder holder2 = getHolderFromAppID(appid);
						holder2.imgPlay.setVisibility(View.INVISIBLE);
						holder2.imgPause.setVisibility(View.INVISIBLE);
						holder2.imgNew.setVisibility(View.INVISIBLE);
						holder2.imgInstall.setVisibility(View.VISIBLE);
						InstallApp(fullPath);
					}
				});
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			if (progress == null || progress.length == 0)
				return;

			Log.d("beijing_pinche", progress[0]);

			super.onProgressUpdate(progress);    //To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		protected void onPostExecute(String fileUrl) {
			super.onPostExecute(fileUrl);    //To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		protected void onCancelled(String s) {
			super.onCancelled();    //To change body of overridden methods use File | Settings | File Templates.
		}
	}

	private void InstallApp(String path)
	{
		File apkfile = new File(path);
		if (!apkfile.exists()) {
			return;
		}

		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		startActivity(installIntent);
	}

	private void updateAppDownloadProgress(long appid)
	{
		STApplication app = getApplicationFromID(appid);
		if (app == null)
			return;

		STAppControlHolder holder = getHolderFromAppID(appid);
		if (holder == null)
			return;

		if (app.totalSize <= 0)
		{
			String szSize = "";
			if (app.downloadedBytes < 1024)
			{
				szSize = app.downloadedBytes + "B";
			}
			else if (app.downloadedBytes < 1024 * 1024)
			{
				szSize = String.format("%.2fKB", app.downloadedBytes / 1024.f);
			}
			else if (app.downloadedBytes < 1024 * 1024 * 1024)
			{
				szSize = String.format("%.2fMB", app.downloadedBytes / (1024.f * 1024.f));
			}
			else
			{
				szSize = String.format("%.2fGB", app.downloadedBytes / (1024.f * 1024.f * 1024.f));
			}

			holder.prog_label.setText(szSize);
		}
		else
		{
			holder.progressor.setProgress((int)(app.downloadedBytes * 100 / app.totalSize));
		}
	}

	private class STAppControlHolder
	{
		public long uid = 0;

		public RelativeLayout containerView = null;

		public ImageView imgNew = null;
		public ImageView imgPlay = null;
		public ImageView imgPause = null;
		public ImageView imgInstall = null;
		public HorProgressor progressor = null;
		public TextView prog_label = null;

		public DownloadApp downloadProgress = null;
	}

	private boolean appStateIsInstalled(int state)
	{
		return (state == STApplication.APP_STATE_INSTALLED || state == STApplication.APP_STATE_UPDATABLE);
	}

	private HorizontalPager.OnHorScrolledListener horScrolledListener = new HorizontalPager.OnHorScrolledListener()
	{
		@Override
		public void onScrolled() {
			controlIndicatorPos();
		}
	};

	private void controlIndicatorPos()
	{
		int nScrollX = news_list_pager.getScrollX();
		int nPageWidth = news_list_pager.getWidth();

		int nIndicatorWidth = page_indicator_layout.getWidth();
		if (nIndicatorWidth == 0)
			return;

		int nTabItemWidth = 160;
		RelativeLayout.LayoutParams layout_params = new RelativeLayout.LayoutParams(nTabItemWidth, 3);
		layout_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout_params.leftMargin = nTabItemWidth * nScrollX / nPageWidth;

		page_indicator_layout.setLayoutParams(layout_params);
		page_indicator_layout.setTag(R.string.TAG_KEY_WIDTH, "" + layout_params.width);
		page_indicator_layout.setTag(R.string.TAG_KEY_HEIGHT, "" + layout_params.height);
		page_indicator_layout.setTag(R.string.TAG_KEY_MARGINLEFT, "" + layout_params.leftMargin);

		ResolutionSet.instance.iterateChild((View)page_indicator_layout.getParent(), mScrSize.x, mScrSize.y);

		if (nScrollX < nPageWidth / 2)
		{
			txt_title_gonggao.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
			txt_title_dingdanxiaoxi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
			txt_title_tongzhi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
		}
		else if (nScrollX < nPageWidth * 3 / 2)
		{
			txt_title_gonggao.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
			txt_title_dingdanxiaoxi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
			txt_title_tongzhi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
		}
		else
		{
			txt_title_gonggao.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
			txt_title_dingdanxiaoxi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_NORMAL_COLOR));
			txt_title_tongzhi.setTextColor(getResources().getColor(R.color.NEWS_TITLE_SEL_COLOR));
		}
	}


	private void onClickTitleGongGao()
	{
		news_list_pager.setCurrentScreen(0, true);
	}

	private void onClickTitleDingDanXiaoXi()
	{
		news_list_pager.setCurrentScreen(1, true);
	}

	private void onClickTitleTongZhi()
	{
		news_list_pager.setCurrentScreen(2, true);
	}

	private class STAncControlHolder
	{
		public long uid = 0;
		public View itemLayout = null;
		public TextView titleView = null;
		public TextView contentsView = null;
		public TextView timeView = null;
	}

	private class STOrderNotifControlHolder
	{
		public long uid = 0;
		public View itemLayout = null;
		public TextView titleView = null;
		public TextView contentsView = null;
		public TextView timeView = null;
		public ImageView imgBadge = null;
		public ImageButton btnItem = null;
	}

	private class STPersonNotifControlHolder
	{
		public long uid = 0;
		public Button btnItem = null;
		public View itemLayout = null;
		public TextView titleView = null;
		public TextView contentsView = null;
		public TextView timeView = null;
		public ImageView imgBadge = null;
	}

	private PullToRefreshBase.OnRefreshListener announcement_list_refresh_listener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestAnnouncements();
			else
				getOlderAnnouncements();
		}
	};

	private void getOlderAnnouncements()
	{
		CommManager.getAnnouncementsPage(Global.loadUserID(getApplicationContext()),
				Global.loadCityName(getApplicationContext()),
				false,
				anc_page,
				Global.getIMEI(getApplicationContext()),
				older_anc_handler);
	}

	private void getLatestAnnouncements()
	{
		long limitid = 0;
		if (arrAnnouncements.size() > 0)
			limitid = arrAnnouncements.get(0).uid;

		CommManager.getLatestAnnouncements(Global.loadUserID(getApplicationContext()),
				Global.loadCityName(getApplicationContext()),
				false,
				limitid,
				Global.getIMEI(getApplicationContext()),
				latest_anc_handler);
	}

	private PullToRefreshBase.OnRefreshListener ordernotif_list_refresh_listener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestOrderNotifs();
			else
				getOlderOrderNotifs();
		}
	};

	private void getOlderOrderNotifs()
	{
		CommManager.getOrderNotifPage(Global.loadUserID(getApplicationContext()), order_notif_page, Global.getIMEI(getApplicationContext()), older_ordernotif_handler);
	}

	private void getLatestOrderNotifs()
	{
		long limitid = 0;
		if (arrOrderNotifs.size() > 0)
			limitid = arrOrderNotifs.get(0).uid;

		CommManager.getLatestOrderNotif(Global.loadUserID(getApplicationContext()), limitid, Global.getIMEI(getApplicationContext()), latest_ordernotif_handler);
	}

	private void getOlderPersonNotifs()
	{
		CommManager.getNotificationsPage(Global.loadUserID(getApplicationContext()), person_notif_page, Global.getIMEI(getApplicationContext()), older_personnotif_handler);
	}

	private void getLatestPersonNotifs()
	{
		long limitid = 0;
		if (arrPersonNotifs.size() > 0)
			limitid = arrPersonNotifs.get(0).uid;

		CommManager.getLatestNotifications(Global.loadUserID(getApplicationContext()), limitid, Global.getIMEI(getApplicationContext()), latest_personnotif_handler);
	}


	private PullToRefreshBase.OnRefreshListener personnotif_list_refresh_listener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestPersonNotifs();
			else
				getOlderPersonNotifs();
		}
	};

	private AsyncHttpResponseHandler older_anc_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			announce_listview.onRefreshComplete();

			try
			{
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STAnnouncement ancItem = STAnnouncement.dcodeFromJSON(itemObj);
						arrTempData.add(ancItem);
					}

					addOlderAncm();
				}
				else if (nRetCode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szRetMsg, Gravity.CENTER);
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
			announce_listview.onRefreshComplete();
		}
	};


	private AsyncHttpResponseHandler latest_anc_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			announce_listview.onRefreshComplete();

			try
			{
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STAnnouncement ancItem = STAnnouncement.dcodeFromJSON(itemObj);
						arrTempData.add(ancItem);
					}

					insertLatestAncm();
				}
				else if (nRetCode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szRetMsg, Gravity.CENTER);
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
			announce_listview.onRefreshComplete();
		}
	};


	private void insertLatestAncm()
	{
		boolean hasUpdate = false;
		for (int i = arrTempData.size() - 1; i >= 0; i--)
		{
			STAnnouncement announcement = (STAnnouncement)arrTempData.get(i);
			boolean isExist = false;
			for (STAnnouncement ancitem : arrAnnouncements)
			{
				if (ancitem.uid == announcement.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrAnnouncements.add(0, announcement);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			anc_page = arrAnnouncements.size() / Global.getPageItemCount();
			announce_adapter.notifyDataSetChanged();

			String szTime = arrAnnouncements.get(0).time;
			long uid = arrAnnouncements.get(0).uid;

			Global.saveLastAnnouncementID(getApplicationContext(), uid);
			Global.saveLastAnnouncementTime(getApplicationContext(), szTime);
		}
	}

	private void addOlderAncm()
	{
		boolean hasUpdate = false;
		for (int i = 0; i < arrTempData.size(); i++)
		{
			STAnnouncement announcement = (STAnnouncement)arrTempData.get(i);
			boolean isExist = false;
			for (STAnnouncement ancitem : arrAnnouncements)
			{
				if (ancitem.uid == announcement.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrAnnouncements.add(announcement);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			anc_page = arrAnnouncements.size() / Global.getPageItemCount();
			announce_adapter.notifyDataSetChanged();
		}

		if (arrAnnouncements.size() > 0)
		{
			STAnnouncement announcement = arrAnnouncements.get(0);
			String szTime = announcement.time;
			String szOldTime = Global.loadLastAnnouncementTime(getApplicationContext());

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dtNew = null, dtOld = null;
				if (szOldTime.equals(""))
				{
					Global.saveLastAnnouncementID(getApplicationContext(), announcement.uid);
					Global.saveLastAnnouncementTime(getApplicationContext(), szTime);
				}
				else
				{
					dtNew = dateFormat.parse(szTime);
					dtOld = dateFormat.parse(szOldTime);
					if (dtOld.before(dtNew))
					{
						Global.saveLastAnnouncementID(getApplicationContext(), announcement.uid);
						Global.saveLastAnnouncementTime(getApplicationContext(), szTime);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (arrOrderNotifs.size() == 0)
		{
			TimerTask task = new TimerTask()
			{
				@Override
				public void run() {
					getOlderOrderNotifs();
				}
			};

			Timer timer = new Timer();
			timer.schedule(task, 300);
		}
	}


	private AsyncHttpResponseHandler older_ordernotif_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			ordernotif_listview.onRefreshComplete();

			try
			{
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					String idarray = "";
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyOrder notifyitem = STNotifyOrder.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);

						if (notifyitem.orderid <= 0)
						{
							if (!idarray.equals(""))
								idarray += ",";
							idarray += notifyitem.uid;
						}
					}

					if (!idarray.equals(""))
					{
						CommManager.checkOrderNotifRead(Global.loadUserID(getApplicationContext()),
								idarray, Global.getIMEI(getApplicationContext()), null);
					}

					addOlderNotifyOrders();
				}
				else if (nRetCode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szRetMsg, Gravity.CENTER);
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
			ordernotif_listview.onRefreshComplete();
		}
	};


	private AsyncHttpResponseHandler latest_ordernotif_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			ordernotif_listview.onRefreshComplete();

			try
			{
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					String idarray = "";
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyOrder notifyitem = STNotifyOrder.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);

						if (notifyitem.orderid <= 0)
						{
							if (!idarray.equals(""))
								idarray += ",";
							idarray += notifyitem.uid;
						}
					}

					if (!idarray.equals(""))
					{
						CommManager.checkOrderNotifRead(Global.loadUserID(getApplicationContext()),
								idarray, Global.getIMEI(getApplicationContext()), null);
					}

					insertLatestNotifyOrders();
				}
				else if (nRetCode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szRetMsg, Gravity.CENTER);
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
			ordernotif_listview.onRefreshComplete();
		}
	};


	private void insertLatestNotifyOrders()
	{
		boolean hasUpdate = false;
		for (int i = arrTempData.size() - 1; i >= 0; i--)
		{
			STNotifyOrder notifyorder = (STNotifyOrder)arrTempData.get(i);
			boolean isExist = false;
			for (STNotifyOrder notifyitem : arrOrderNotifs)
			{
				if (notifyitem.uid == notifyorder.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrOrderNotifs.add(0, notifyorder);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			order_notif_page = arrOrderNotifs.size() / Global.getPageItemCount();
			ordernotif_adapter.notifyDataSetChanged();

			String szTime = arrOrderNotifs.get(0).time;
			long uid = arrOrderNotifs.get(0).uid;

			Global.saveLastOrderNotificationID(getApplicationContext(), uid);
			Global.saveLastOrderNotificationTime(getApplicationContext(), szTime);
		}
	}

	private void addOlderNotifyOrders()
	{
		boolean hasUpdate = false;
		for (int i = 0; i < arrTempData.size(); i++)
		{
			STNotifyOrder notifyorder = (STNotifyOrder)arrTempData.get(i);
			boolean isExist = false;
			for (STNotifyOrder notifyitem : arrOrderNotifs)
			{
				if (notifyitem.uid == notifyorder.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrOrderNotifs.add(notifyorder);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			order_notif_page = arrOrderNotifs.size() / Global.getPageItemCount();
			ordernotif_adapter.notifyDataSetChanged();
		}

		if (arrOrderNotifs.size() > 0)
		{
			STNotifyOrder notifyorder = arrOrderNotifs.get(0);
			String szTime = notifyorder.time;
			String szOldTime = Global.loadLastOrderNotificationTime(getApplicationContext());

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dtNew = null, dtOld = null;
				dtNew = dateFormat.parse(szTime);
				if (szOldTime.equals(""))
				{
					Global.saveLastOrderNotificationID(getApplicationContext(), notifyorder.uid);
					Global.saveLastOrderNotificationTime(getApplicationContext(), szTime);
				}
				else
				{
					dtOld = dateFormat.parse(szOldTime);
					if (dtOld.before(dtNew))
					{
						Global.saveLastOrderNotificationID(getApplicationContext(), notifyorder.uid);
						Global.saveLastOrderNotificationTime(getApplicationContext(), szTime);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (arrPersonNotifs.size() == 0)
		{
			TimerTask task = new TimerTask()
			{
				@Override
				public void run() {
					getOlderPersonNotifs();
				}
			};

			Timer timer = new Timer();
			timer.schedule(task, 200);
		}
	}


	private AsyncHttpResponseHandler older_personnotif_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			personnotif_listview.onRefreshComplete();

			try
			{
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					String idarray = "";
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyPerson notifyitem = STNotifyPerson.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);

						if (notifyitem.couponid <= 0)
						{
							if (!idarray.equals(""))
								idarray += ",";
							idarray += notifyitem.uid;
						}
					}

					if (!idarray.equals(""))
					{
						CommManager.checkPersonNotifRead(Global.loadUserID(getApplicationContext()),
								idarray, Global.getIMEI(getApplicationContext()), null);
					}

					addOlderNotifyPerson();
				}
				else if (nRetCode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szRetMsg, Gravity.CENTER);
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
			personnotif_listview.onRefreshComplete();
		}
	};


	private AsyncHttpResponseHandler latest_personnotif_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			personnotif_listview.onRefreshComplete();

			try
			{
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");
				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == 0)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					String idarray = "";
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyPerson notifyitem = STNotifyPerson.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);

						if (notifyitem.couponid <= 0)
						{
							if (!idarray.equals(""))
								idarray += ",";
							idarray += notifyitem.uid;
						}
					}

					if (!idarray.equals(""))
					{
						CommManager.checkPersonNotifRead(Global.loadUserID(getApplicationContext()),
								idarray, Global.getIMEI(getApplicationContext()), null);
					}


					insertLatestNotifyPerson();
				}
				else if (nRetCode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szRetMsg, Gravity.CENTER);
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
			personnotif_listview.onRefreshComplete();
		}
	};


	private void insertLatestNotifyPerson()
	{
		boolean hasUpdate = false;
		for (int i = arrTempData.size() - 1; i >= 0; i--)
		{
			STNotifyPerson notifyperson = (STNotifyPerson)arrTempData.get(i);
			boolean isExist = false;
			for (STNotifyPerson notifyitem : arrPersonNotifs)
			{
				if (notifyitem.uid == notifyperson.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrPersonNotifs.add(0, notifyperson);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			person_notif_page = arrPersonNotifs.size() / Global.getPageItemCount();
			personnotif_adapter.notifyDataSetChanged();

			long uid = arrPersonNotifs.get(0).uid;
			String szTime = arrPersonNotifs.get(0).time;

			Global.saveLastPersonNotificationID(getApplicationContext(), uid);
			Global.saveLastPersonNotificationTime(getApplicationContext(), szTime);
		}
	}

	private void addOlderNotifyPerson()
	{
		boolean hasUpdate = false;
		for (int i = 0; i < arrTempData.size(); i++)
		{
			STNotifyPerson notifyperson = (STNotifyPerson)arrTempData.get(i);
			boolean isExist = false;
			for (STNotifyPerson notifyitem : arrPersonNotifs)
			{
				if (notifyitem.uid == notifyperson.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrPersonNotifs.add(notifyperson);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			person_notif_page = arrPersonNotifs.size() / Global.getPageItemCount();
			personnotif_adapter.notifyDataSetChanged();
		}


		if (arrPersonNotifs.size() > 0)
		{
			STNotifyPerson notifyperson = arrPersonNotifs.get(0);
			String szTime = notifyperson.time;
			String szOldTime = Global.loadLastPersonNotificationTime(getApplicationContext());

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dtNew = null, dtOld = null;
				if (szOldTime.equals(""))
				{
					Global.saveLastPersonNotificationID(getApplicationContext(), notifyperson.uid);
					Global.saveLastPersonNotificationTime(getApplicationContext(), szTime);
				}
				else
				{
					dtNew = dateFormat.parse(szTime);
					dtOld = dateFormat.parse(szOldTime);
					if (dtOld.before(dtNew))
					{
						Global.saveLastPersonNotificationID(getApplicationContext(), notifyperson.uid);
						Global.saveLastPersonNotificationTime(getApplicationContext(), szTime);
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	private class AnnounceAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			return arrAnnouncements.size();
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
			return arrAnnouncements.get(position);
		}

		@Override
		public boolean isEmpty() {
			if (arrAnnouncements == null)
				return true;

			return arrAnnouncements.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final STAnnouncement anc = arrAnnouncements.get(position);

			if (convertView == null)
			{
				int nItemHeight = 80;
				int nLeft = 10;
				int nForeColor = Color.rgb(176, 176, 176);

				convertView = new RelativeLayout(parent.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, nItemHeight);
				convertView.setLayoutParams(layoutParams);
				convertView.setBackgroundColor(Color.WHITE);

				// top border
				RelativeLayout top_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams top_border_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
				top_border.setLayoutParams(top_border_params);
				top_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(top_border);

				// left border
				RelativeLayout left_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams left_border_params = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.MATCH_PARENT);
				left_border.setLayoutParams(left_border_params);
				left_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(left_border);

				// bottom border
				RelativeLayout bottom_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams bottom_border_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
				bottom_border_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				bottom_border.setLayoutParams(bottom_border_params);
				bottom_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(bottom_border);

				// right border
				RelativeLayout right_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams right_border_params = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.MATCH_PARENT);
				right_border_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				right_border.setLayoutParams(right_border_params);
				right_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(right_border);

				// Time view
				TextView timeView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams time_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, nItemHeight / 2);
				time_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				time_layoutparams.setMargins(0, 0, nLeft, 0);
				timeView.setTextColor(nForeColor);
				timeView.setGravity(Gravity.CENTER_VERTICAL);
				timeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				timeView.setId(1);
				timeView.setLayoutParams(time_layoutparams);
				timeView.setText(anc.time);
				((RelativeLayout)convertView).addView(timeView);

				// Title view
				TextView titleView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams title_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, nItemHeight / 2);
				title_layoutparams.setMargins(nLeft, 0, 0, 0);
				title_layoutparams.addRule(RelativeLayout.LEFT_OF, timeView.getId());
				title_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				titleView.setLayoutParams(title_layoutparams);
				titleView.setTextColor(nForeColor);
				titleView.setGravity(Gravity.CENTER_VERTICAL);
				titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				titleView.setText(anc.title);
				((RelativeLayout)convertView).addView(titleView);

				// Content view
				TextView contentView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams content_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, nItemHeight / 2);
				content_layoutparams.setMargins(nLeft, 0, nLeft, 0);
				content_layoutparams.addRule(RelativeLayout.BELOW, timeView.getId());
				contentView.setLayoutParams(content_layoutparams);
				contentView.setTextColor(nForeColor);
				contentView.setGravity(Gravity.CENTER_VERTICAL);
				contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				contentView.setText(anc.contents);
				contentView.setSingleLine();
				contentView.setEllipsize(TextUtils.TruncateAt.END);
				((RelativeLayout)convertView).addView(contentView);

				STAncControlHolder holder = new STAncControlHolder();
				holder.itemLayout = contentView;
				holder.contentsView = contentView;
				holder.timeView = timeView;
				holder.titleView = titleView;
				holder.uid = anc.uid;

				convertView.setTag(holder);

				ResolutionSet.instance.iterateChild(convertView, mScrSize.x, mScrSize.y);
			}
			else
			{
				STAncControlHolder viewHolder = (STAncControlHolder)convertView.getTag();
				viewHolder.contentsView.setText(anc.contents);
				viewHolder.timeView.setText(anc.time);
				viewHolder.titleView.setText(anc.title);
				viewHolder.uid = anc.uid;
			}
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // show alert dialog
                    final CommonAlertDialog dialog = new CommonAlertDialog.Builder(MainActivity.this)
                            .message(anc.contents)
                            .type(CommonAlertDialog.DIALOGTYPE_ALERT)
                            .build();
                    dialog.show();
                }
            });
			return convertView;
		}
	}

	private class OrdernotifAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			return arrOrderNotifs.size();
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
			return arrOrderNotifs.get(position);
		}

		@Override
		public boolean isEmpty() {
			if (arrOrderNotifs == null)
				return true;

			return arrOrderNotifs.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final STNotifyOrder notify = arrOrderNotifs.get(position);

			if (convertView == null)
			{
				int nItemHeight = 80;
				int nLeft = 10;
				int nForeColor = Color.rgb(176, 176, 176);

				convertView = new RelativeLayout(parent.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, nItemHeight);
				convertView.setLayoutParams(layoutParams);
				convertView.setBackgroundColor(Color.WHITE);

				// top border
				RelativeLayout top_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams top_border_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
				top_border.setLayoutParams(top_border_params);
				top_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(top_border);

				// left border
				RelativeLayout left_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams left_border_params = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.MATCH_PARENT);
				left_border.setLayoutParams(left_border_params);
				left_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(left_border);

				// bottom border
				RelativeLayout bottom_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams bottom_border_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
				bottom_border_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				bottom_border.setLayoutParams(bottom_border_params);
				bottom_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(bottom_border);

				// right border
				RelativeLayout right_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams right_border_params = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.MATCH_PARENT);
				right_border_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				right_border.setLayoutParams(right_border_params);
				right_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(right_border);

				// Time view
				TextView timeView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams time_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, nItemHeight / 2);
				time_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				time_layoutparams.setMargins(0, 0, nLeft, 0);
				timeView.setTextColor(nForeColor);
				timeView.setGravity(Gravity.CENTER_VERTICAL);
				timeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				timeView.setId(1);
				timeView.setLayoutParams(time_layoutparams);
				timeView.setText(notify.time);
				((RelativeLayout)convertView).addView(timeView);

				// Title view
				TextView titleView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams title_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, nItemHeight / 2);
				title_layoutparams.setMargins(nLeft, 0, 0, 0);
				title_layoutparams.addRule(RelativeLayout.LEFT_OF, timeView.getId());
				title_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				titleView.setLayoutParams(title_layoutparams);
				titleView.setTextColor(nForeColor);
				titleView.setGravity(Gravity.CENTER_VERTICAL);
				titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				titleView.setText(notify.title);
				((RelativeLayout)convertView).addView(titleView);

				// Content view
				TextView contentView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams content_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				content_layoutparams.setMargins(nLeft, 0, nLeft, 0);
				content_layoutparams.addRule(RelativeLayout.BELOW, timeView.getId());
				contentView.setLayoutParams(content_layoutparams);
				contentView.setTextColor(nForeColor);
				contentView.setGravity(Gravity.CENTER_VERTICAL);
				contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				contentView.setText(notify.contents);
				contentView.setSingleLine();
				contentView.setEllipsize(TextUtils.TruncateAt.END);
				((RelativeLayout)convertView).addView(contentView);


				ImageView imgBadge = new ImageView(convertView.getContext());
				imgBadge.setBackgroundResource(R.drawable.new_badge);
				RelativeLayout.LayoutParams badge_layoutparams = new RelativeLayout.LayoutParams(20, 20);
				imgBadge.setLayoutParams(badge_layoutparams);
				((RelativeLayout)convertView).addView(imgBadge);

				if (notify.orderid > 0 && notify.hasread == 0) {
					imgBadge.setVisibility(View.VISIBLE);
				} else {
					imgBadge.setVisibility(View.INVISIBLE);
				}
				final ImageView final_badgeView = imgBadge;

				ImageButton btnItem = new ImageButton(convertView.getContext());
				RelativeLayout.LayoutParams btn_layoutparams =
						new RelativeLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.MATCH_PARENT);

				btnItem.setLayoutParams(btn_layoutparams);
				btnItem.setBackgroundResource(R.drawable.btn_empty);
				btnItem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (notify.hasread == 0) {
							CommManager.checkOrderNotifRead(Global.loadUserID(getApplicationContext()),
									"" + notify.uid,
									Global.getIMEI(getApplicationContext()),
									null);
							notify.hasread = 1;
						}

						if (final_badgeView != null)
							final_badgeView.setVisibility(View.INVISIBLE);

						onSelectOrderNotify(notify);
					}
				});
				((RelativeLayout)convertView).addView(btnItem);

				STOrderNotifControlHolder holder = new STOrderNotifControlHolder();
				holder.itemLayout = contentView;
				holder.contentsView = contentView;
				holder.timeView = timeView;
				holder.titleView = titleView;
				holder.uid = notify.uid;
				holder.imgBadge = imgBadge;
				holder.btnItem = btnItem;

				convertView.setTag(holder);

				ResolutionSet.instance.iterateChild(convertView, mScrSize.x, mScrSize.y);
			}
			else
			{
				final STOrderNotifControlHolder viewHolder = (STOrderNotifControlHolder)convertView.getTag();
				viewHolder.contentsView.setText(notify.contents);
				viewHolder.timeView.setText(notify.time);
				viewHolder.titleView.setText(notify.title);
				viewHolder.uid = notify.uid;

				if (notify.orderid > 0 && notify.hasread == 0)
					viewHolder.imgBadge.setVisibility(View.VISIBLE);
				else
					viewHolder.imgBadge.setVisibility(View.INVISIBLE);

				viewHolder.btnItem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (notify.hasread == 0) {
							CommManager.checkOrderNotifRead(Global.loadUserID(getApplicationContext()),
									"" + notify.uid,
									Global.getIMEI(getApplicationContext()),
									null);
							notify.hasread = 1;
						}

						if (viewHolder.imgBadge != null)
							viewHolder.imgBadge.setVisibility(View.INVISIBLE);
						onSelectOrderNotify(notify);
					}
				});
			}

			return convertView;
		}
	}



    private void onSelectOrderNotify(STNotifyOrder notify)
    {
        CommonAlertDialog dialog = new CommonAlertDialog.Builder(MainActivity.this)
                .message(notify.contents)
                .type(CommonAlertDialog.DIALOGTYPE_ALERT)
                .build();
        dialog.show();
    }



	private class PersonnotifAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			return arrPersonNotifs.size();
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
			return arrPersonNotifs.get(position);
		}


		@Override
		public boolean isEmpty() {
			if (arrPersonNotifs == null)
				return true;

			return arrPersonNotifs.isEmpty();
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final STNotifyPerson notify = arrPersonNotifs.get(position);

			if (convertView == null)
			{
				int nLeft = 10;
				int nForeColor = Color.rgb(176, 176, 176);

				convertView = new RelativeLayout(parent.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
				convertView.setLayoutParams(layoutParams);
				convertView.setBackgroundColor(Color.WHITE);

				// top border
				RelativeLayout top_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams top_border_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 1);
				top_border.setLayoutParams(top_border_params);
				top_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(top_border);

				// left border
				RelativeLayout left_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams left_border_params = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.MATCH_PARENT);
				left_border.setLayoutParams(left_border_params);
				left_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(left_border);

				// right border
				RelativeLayout right_border = new RelativeLayout(convertView.getContext());
				RelativeLayout.LayoutParams right_border_params = new RelativeLayout.LayoutParams(1, RelativeLayout.LayoutParams.MATCH_PARENT);
				right_border_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				right_border.setLayoutParams(right_border_params);
				right_border.setBackgroundColor(nForeColor);
				((RelativeLayout)convertView).addView(right_border);

				// Time view
				TextView timeView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams time_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				time_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				time_layoutparams.setMargins(0, 0, nLeft, 0);
				timeView.setTextColor(nForeColor);
				timeView.setGravity(Gravity.CENTER_VERTICAL);
				timeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				timeView.setId(1);
				timeView.setLayoutParams(time_layoutparams);
				timeView.setPadding(0, 5, 0, 5);
				timeView.setText(notify.time);
				((RelativeLayout)convertView).addView(timeView);

				// Title view
				TextView titleView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams title_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				title_layoutparams.setMargins(nLeft, 0, 0, 0);
				title_layoutparams.addRule(RelativeLayout.LEFT_OF, timeView.getId());
				title_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				titleView.setLayoutParams(title_layoutparams);
				titleView.setPadding(0, 5, 0, 5);
				titleView.setTextColor(nForeColor);
				titleView.setGravity(Gravity.CENTER_VERTICAL);
				titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				titleView.setText(notify.title);
				((RelativeLayout)convertView).addView(titleView);

				// Content view
				TextView contentView = new TextView(convertView.getContext());
				RelativeLayout.LayoutParams content_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				content_layoutparams.setMargins(nLeft, 0, nLeft, 0);
				content_layoutparams.addRule(RelativeLayout.BELOW, timeView.getId());
				contentView.setLayoutParams(content_layoutparams);
				contentView.setTextColor(nForeColor);
				contentView.setGravity(Gravity.CENTER_VERTICAL);
				contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				contentView.setText(notify.contents);
				contentView.setSingleLine(false);
//				contentView.setEllipsize(TextUtils.TruncateAt.END);
				contentView.setPadding(0, 5, 0, 5);
				((RelativeLayout)convertView).addView(contentView);


				// Image badge view
				ImageView imgBadge = new ImageView(convertView.getContext());
				imgBadge.setBackgroundResource(R.drawable.new_badge);
				RelativeLayout.LayoutParams badge_layoutparams = new RelativeLayout.LayoutParams(20, 20);
				imgBadge.setLayoutParams(badge_layoutparams);
				((RelativeLayout)convertView).addView(imgBadge);

				if (notify.couponid > 0 && notify.hasread == 0)
					imgBadge.setVisibility(View.VISIBLE);
				else
					imgBadge.setVisibility(View.INVISIBLE);

				final ImageView final_badgeView = imgBadge;


				// Item button
				Button button = new Button(convertView.getContext());
				RelativeLayout.LayoutParams btn_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				button.setBackgroundResource(R.drawable.btn_empty);
				button.setLayoutParams(btn_layoutparams);
				button.setTag("" + notify.uid);
				button.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						try {
							if (final_badgeView != null)
								final_badgeView.setVisibility(View.INVISIBLE);

							notify.hasread = 1;
							onSelectPersonNotif(notify);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
				((RelativeLayout)convertView).addView(button);


				STPersonNotifControlHolder holder = new STPersonNotifControlHolder();
				holder.itemLayout = contentView;
				holder.contentsView = contentView;
				holder.timeView = timeView;
				holder.titleView = titleView;
				holder.uid = notify.uid;
				holder.btnItem = button;
				holder.imgBadge = imgBadge;

				convertView.setTag(holder);

				ResolutionSet.instance.iterateChild(convertView, mScrSize.x, mScrSize.y);
			}
			else
			{
				STPersonNotifControlHolder viewHolder = (STPersonNotifControlHolder)convertView.getTag();
				viewHolder.contentsView.setText(notify.contents);
				viewHolder.timeView.setText(notify.time);
				viewHolder.titleView.setText(notify.title);
				viewHolder.uid = notify.uid;
				viewHolder.btnItem.setTag("" + viewHolder.uid);
				if (notify.couponid > 0 && notify.hasread == 0)
					viewHolder.imgBadge.setVisibility(View.VISIBLE);
				else
					viewHolder.imgBadge.setVisibility(View.INVISIBLE);

				final ImageView final_badgeView = viewHolder.imgBadge;

				viewHolder.btnItem.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						try {
							if (final_badgeView != null)
								final_badgeView.setVisibility(View.INVISIBLE);

							notify.hasread = 1;
							onSelectPersonNotif(notify);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});
			}

			return convertView;
		}
	}

    private void onSelectPersonNotif(STNotifyPerson notify) {
        if (notify.couponid <= 0)
            return;

        if (notify.couponid > 0) {
            CommManager.getCouponDetails(notify.couponid, Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), coupon_detail_handler);
        } else {
            CommonAlertDialog dialog = new CommonAlertDialog.Builder(MainActivity.this)
                    .type(CommonAlertDialog.DIALOGTYPE_ALERT)
                    .message(notify.contents)
                    .build();
            dialog.show();
        }
    }


	private AsyncHttpResponseHandler coupon_detail_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");
				int nRetcode = result.getInt("retcode");
				String szMsg = result.getString("retmsg");
				if (nRetcode == 0)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					STSingleCoupon coupon = STSingleCoupon.dcodeFromJSON(retdata);
					showCouponInfo(coupon);
				}
				else if (nRetcode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, szMsg, Gravity.CENTER);
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
			stopProgress();
		}
	};

	private void showCouponInfo(STSingleCoupon coupon)
	{
		txt_coupon_content.setText(coupon.product_name);
		txt_coupon_code.setText(coupon.usecond);
		txt_spreadunit_name.setText(coupon.dateexp);

		coupon_container_layout.setVisibility(View.VISIBLE);
	}


	private AsyncHttpResponseHandler has_news_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject jsonResult = jsonObj.getJSONObject("result");

				int nRetcode = jsonResult.getInt("retcode");
				String jsonMsg = jsonResult.getString("retmsg");

				if (nRetcode == 0)
				{
					JSONObject retdata = jsonResult.getJSONObject("retdata");

					int nHasAnnouncement = retdata.getInt("announcement");
					int nHasOrderNotification = retdata.getInt("ordernotif");
					int nHasPersonNotification = retdata.getInt("personnotif");

					if (nHasAnnouncement > 0 || nHasOrderNotification > 0 || nHasPersonNotification > 0)
					{
						imgBadgeTab.setVisibility(View.VISIBLE);

						if (nHasAnnouncement > 0)
							imgBadgeTitle1.setVisibility(View.VISIBLE);

						if (nHasOrderNotification > 0)
							imgBadgeTitle2.setVisibility(View.VISIBLE);

						if (nHasPersonNotification > 0)
							imgBadgeTitle3.setVisibility(View.VISIBLE);
					}
				}
				else if (nRetcode == Global.AUTO_LOGOUT_CODE())
				{
					logout(jsonMsg);
				}
				else
				{
					Global.showAdvancedToast(MainActivity.this, jsonMsg, Gravity.CENTER);
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


	private AsyncHttpResponseHandler readordernotifs_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
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


	private AsyncHttpResponseHandler readpersonnotifs_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == Global.AUTO_LOGOUT_CODE())
				{
					logout(szRetMsg);
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


	private AsyncHttpResponseHandler logininfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szMsg = result.getString("retmsg");

				if (nRetcode == 0)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					STUserInfo userinfo = STUserInfo.decodeFromJSON(retdata);

					Global.saveUserID(getApplicationContext(), userinfo.userid);
					Global.saveInviteCode(getApplicationContext(), userinfo.invitecode);
					Global.savePersonVerified(getApplicationContext(), userinfo.person_verified == 1);
//					txtInvitecode.setText(userinfo.invitecode);
                    shareWebView.loadUrl("http://124.207.135.69:8080/bk/index/activity_appFx.do?activityDto.userId="
                            +Global.loadUserID(getApplicationContext()));
					CommManager.hasNews(Global.loadUserID(getApplicationContext()),
							"",
							Global.isDriverVerified(getApplicationContext()),
							Global.loadLastAnnouncementID(getApplicationContext()),
							Global.loadLastOrderNotificationID(getApplicationContext()),
							Global.loadLastPersonNotificationID(getApplicationContext()),
							Global.getIMEI(getApplicationContext()),
							has_news_handler);
				}
				else
				{
					Global.clearUserInfo(getApplicationContext());
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		/**使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mShareController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_TAB_NEWS)
		{
			onSelectTabNews();
		}
		else if (requestCode == REQCODE_TAB_PERSONINFO)
		{
			onSelectTabPerson();
		}
		else if (requestCode == REQCODE_TAB_SHARE)
		{
			onSelectTabShare();
		}
	}


	// Person tab methods
	private void onClickPersonInfo()
	{
		Intent intent = new Intent(MainActivity.this, PersonInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void onClickMoney()
	{
		Intent intent = new Intent(MainActivity.this, BalanceActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void onClickCoupon()
	{
		Intent intent = new Intent(MainActivity.this, CouponActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void onClickAbout()
	{
		Intent intent = new Intent(MainActivity.this, AboutActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void onClickLogout()
	{
		CommonAlertDialog dialog = new CommonAlertDialog.Builder(MainActivity.this)
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

	private AsyncHttpResponseHandler default_contents_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szMsg = result.getString("retmsg");

				if (nRetcode == 0)
				{
					JSONObject retdata = result.getJSONObject("retdata");
                    outputUrl = retdata.getString("output_url");
                    outputContent = retdata.getString("output_content");
//                    outputContent = outputContent.replace("$invitecode",
//                            Global.loadInviteCode(getApplicationContext()));
                    setShareContent(outputUrl, outputContent);
                    stopProgress();
                    showShareActionSheet();
				}
				else
				{
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
					Global.showAdvancedToast(MainActivity.this,
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
			Global.showAdvancedToast(MainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	/*
	 Share methods
	*/
	private void showShareActionSheet() {
		if (!outputUrl.equals("")) {
			mShareController.openShare(MainActivity.this, false);
		} else {
			Global.showAdvancedToast(MainActivity.this,
					getResources().getString(R.string.STR_CONN_ERROR),
					Gravity.CENTER);
		}
	}


	private SocializeListeners.SnsPostListener share_listener = new SocializeListeners.SnsPostListener() {
		@Override
		public void onStart() {
			SHARE_MEDIA media = mShareController.getConfig().getSelectedPlatfrom();
			if (media == SHARE_MEDIA.QZONE ||
					media == SHARE_MEDIA.QQ)
			{
				if (!Global.IsQQInstalled(getApplicationContext()))
				{
					Global.showTextToast(MainActivity.this, getResources().getString(R.string.STR_QQ_NOTINSTALLED));
					return;
				}
			}
		}

		@Override
		public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {}
	};


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
        // 配置SSO
        mShareController.getConfig().setSsoHandler(new SinaSsoHandler());

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MainActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qZoneSsoHandler.addToSocialSDK();

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MainActivity.this,
                "100424468", "c7394704798a158208a74ab60104f0ba");
        qqSsoHandler.addToSocialSDK();

        mShareController.setShareContent(content);			   // Default share contents

        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx483a2a994d4e4f4b";
        String appSecret = "b4d8f4e8c491273e9c0b18be39034ebf";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(MainActivity.this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();

//		UMImage urlImage = new UMImage(PassMainActivity.this,
//				"http://www.umeng.com/images/pic/home/social/img-1.png");
        UMImage urlImage = new UMImage(MainActivity.this, R.drawable.icon);

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


}

