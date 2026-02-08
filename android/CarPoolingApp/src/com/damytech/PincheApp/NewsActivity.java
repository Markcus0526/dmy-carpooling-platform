package com.damytech.PincheApp;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.*;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KimHM
 * Date: 14-8-21
 * Time: 上午10:12
 * To change this template use File | Settings | File Templates.
 */
public class NewsActivity extends SuperActivity
{
	private ImageButton btn_back = null;

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

	private TextView txtBadge1 = null, txtBadge2 = null, txtBadge3 = null;

	private RelativeLayout coupon_container_layout = null;
	private Button btn_coupon_content = null;
	private ImageView img_coupon = null;
	private TextView txt_coupon_code = null;
	private TextView txt_coupon_content = null;
	private TextView txt_spreadunit_name = null;

	private int announce_count = 0, order_notif_count = 0, person_notif_count = 0;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_news);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		announce_count = getIntent().getIntExtra("announcements", 0);
		order_notif_count = getIntent().getIntExtra("ordernotif_count", 0);
		person_notif_count = getIntent().getIntExtra("personnotif_count", 0);

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

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

		announce_listview = new PullToRefreshListView(news_list_pager.getContext());
		{
			RelativeLayout.LayoutParams temp_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			announce_listview.setLayoutParams(temp_layout_params);
			announce_listview.setMode(PullToRefreshBase.Mode.BOTH);
			announce_listview.setOnRefreshListener(announcement_list_refresh_listener);
			announce_listview.setAdapter(announce_adapter);
		}
		news_list_pager.addView(announce_listview);

		ordernotif_listview = new PullToRefreshListView(news_list_pager.getContext());
		{
			RelativeLayout.LayoutParams temp_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			ordernotif_listview.setLayoutParams(temp_layout_params);
			ordernotif_listview.setMode(PullToRefreshBase.Mode.BOTH);
			ordernotif_listview.setOnRefreshListener(ordernotif_list_refresh_listener);
			ordernotif_listview.setAdapter(ordernotif_adapter);
		}
		news_list_pager.addView(ordernotif_listview);

		personnotif_listview = new PullToRefreshListView(news_list_pager.getContext());
		{
			RelativeLayout.LayoutParams temp_layout_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			personnotif_listview.setLayoutParams(temp_layout_params);
			personnotif_listview.setMode(PullToRefreshBase.Mode.BOTH);
			personnotif_listview.setOnRefreshListener(personnotif_list_refresh_listener);
			personnotif_listview.setAdapter(personnotif_adapter);
		}
		news_list_pager.addView(personnotif_listview);

		txtBadge1 = (TextView)findViewById(R.id.txt_badge1);
		txtBadge2 = (TextView)findViewById(R.id.txt_badge2);
		txtBadge3 = (TextView)findViewById(R.id.txt_badge3);

//		if (announce_count > 0) {
//			txtBadge1.setVisibility(View.VISIBLE);
//			TimerTask task = new TimerTask() {
//				@Override
//				public void run() {
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							txtBadge1.setVisibility(View.INVISIBLE);
//							hasAnnouncements = false;
//						}
//					});
//				}
//			};
//			Timer timer = new Timer();
//			timer.schedule(task, 700);
//		}

		if (announce_count > 0)
			txtBadge1.setVisibility(View.VISIBLE);
		else
			txtBadge1.setVisibility(View.GONE);

		if (order_notif_count > 0)
			txtBadge2.setVisibility(View.VISIBLE);
		else
			txtBadge2.setVisibility(View.GONE);

		if (person_notif_count > 0)
			txtBadge3.setVisibility(View.VISIBLE);
		else
			txtBadge3.setVisibility(View.GONE);

		txtBadge1.setText("" + announce_count);
		txtBadge2.setText("" + order_notif_count);
		txtBadge3.setText("" + person_notif_count);

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

		getOlderAnnouncements();
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
                    final CommonAlertDialog dialog = new CommonAlertDialog.Builder(NewsActivity.this)
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
							order_notif_count--;
							if (order_notif_count < 0)
								order_notif_count = 0;
							txtBadge2.setText("" + order_notif_count);
							updateCurrentOrderNotifyNewsState();
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

				ViewGroup.LayoutParams params = viewHolder.btnItem.getLayoutParams();
				params.height = ViewGroup.LayoutParams.MATCH_PARENT;
				ViewGroup parentView = (ViewGroup)viewHolder.btnItem.getParent();
				parentView.removeView(viewHolder.btnItem);
				viewHolder.btnItem.setLayoutParams(params);
				parentView.addView(viewHolder.btnItem);
				viewHolder.btnItem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (notify.hasread == 0) {
							CommManager.checkOrderNotifRead(Global.loadUserID(getApplicationContext()),
									"" + notify.uid,
									Global.getIMEI(getApplicationContext()),
									null);
							notify.hasread = 1;
							order_notif_count--;
							if (order_notif_count < 0)
								order_notif_count = 0;
							txtBadge2.setText("" + order_notif_count);
							updateCurrentOrderNotifyNewsState();
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
        CommonAlertDialog dialog = new CommonAlertDialog.Builder(NewsActivity.this)
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
				RelativeLayout.LayoutParams content_layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				content_layoutparams.setMargins(nLeft, 0, nLeft, 0);
				content_layoutparams.addRule(RelativeLayout.BELOW, timeView.getId());
				contentView.setLayoutParams(content_layoutparams);
				contentView.setTextColor(nForeColor);
				contentView.setGravity(Gravity.CENTER_VERTICAL);
				contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				contentView.setText(notify.contents);
				contentView.setSingleLine(true);
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
				RelativeLayout.LayoutParams btn_layoutparams =
						new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
								RelativeLayout.LayoutParams.MATCH_PARENT);
				button.setBackgroundResource(R.drawable.btn_empty);
				button.setLayoutParams(btn_layoutparams);
				button.setTag("" + notify.uid);
				button.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						try {
							if (notify.hasread == 0) {
								CommManager.checkPersonNotifRead(Global.loadUserID(getApplicationContext()),
										"" + notify.uid,
										Global.getIMEI(getApplicationContext()),
										null);
								notify.hasread = 1;
								person_notif_count--;
								if (person_notif_count < 0)
									person_notif_count = 0;
								txtBadge3.setText("" + person_notif_count);
								updateCurrentPersonNotifyNewsState();
							}

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
							if (notify.hasread == 0) {
								CommManager.checkPersonNotifRead(Global.loadUserID(getApplicationContext()),
										"" + notify.uid,
										Global.getIMEI(getApplicationContext()),
										null);
								notify.hasread = 1;
								person_notif_count--;
								if (person_notif_count < 0)
									person_notif_count = 0;
								txtBadge3.setText("" + person_notif_count);
								updateCurrentPersonNotifyNewsState();
							}

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
			CommonAlertDialog dialog = new CommonAlertDialog.Builder(NewsActivity.this)
					.type(CommonAlertDialog.DIALOGTYPE_ALERT)
					.message(notify.contents)
					.build();
			dialog.show();
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


	private HorizontalPager.OnHorScrolledListener horScrolledListener = new HorizontalPager.OnHorScrolledListener()
	{
		@Override
		public void onScrolled() {
			controlIndicatorPos();
		}
	};

	private void controlIndicatorPos()
	{
		if (page_indicator_layout == null)
			return;

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

				if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

				if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


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
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");
					STSingleCoupon coupon = STSingleCoupon.dcodeFromJSON(retdata);
					showCouponInfo(coupon);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void showCouponInfo(STSingleCoupon coupon)
	{
		txt_coupon_content.setText(coupon.product_name);
		txt_coupon_code.setText(getResources().getString(R.string.shiyongxianzhi) + coupon.usecond);
		txt_spreadunit_name.setText(getResources().getString(R.string.youxiaoqizhi) + coupon.dateexp);

		coupon_container_layout.setVisibility(View.VISIBLE);
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
		arrAnnouncements.clear();
		anc_page = 0;
		getOlderAnnouncements();

//		long limitid = 0;
//		if (arrAnnouncements.size() > 0)
//			limitid = arrAnnouncements.get(0).uid;
//
//		CommManager.getLatestAnnouncements(Global.loadUserID(getApplicationContext()), Global.loadCityName(getApplicationContext()), false, limitid, Global.getIMEI(getApplicationContext()), latest_anc_handler);
	}


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

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					String szIDs = "";

					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STAnnouncement ancItem = STAnnouncement.dcodeFromJSON(itemObj);
						arrTempData.add(ancItem);

						if (!szIDs.equals(""))
							szIDs += ",";
						szIDs += ancItem.uid;
					}

					if (!szIDs.equals(""))
						CommManager.readAnnouncement(Global.loadUserID(getApplicationContext()),
								szIDs,
								Global.getIMEI(getApplicationContext()),
								null);

					addOlderAncm();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

				if (nRetCode == ConstData.ERR_CODE_NONE)
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
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

			announce_count--;

			arrAnnouncements.add(0, announcement);
			if (!hasUpdate)
				hasUpdate = true;
		}

		if (announce_count < 0)
			announce_count = 0;

		if (announce_count == 0)
			txtBadge1.setVisibility(View.GONE);

		txtBadge1.setText("" + announce_count);

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
			announce_count--;

			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
			anc_page = arrAnnouncements.size() / Global.getPageItemCount();

		announce_adapter.notifyDataSetChanged();

		if (announce_count < 0)
			announce_count = 0;

		if (announce_count == 0)
			txtBadge1.setVisibility(View.GONE);

		txtBadge1.setText("" + announce_count);

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
		arrOrderNotifs.clear();
		order_notif_page = 0;
		getOlderOrderNotifs();

//		long limitid = 0;
//		if (arrOrderNotifs.size() > 0)
//			limitid = arrOrderNotifs.get(0).uid;
//
//		CommManager.getLatestOrderNotif(Global.loadUserID(getApplicationContext()), limitid, Global.getIMEI(getApplicationContext()), latest_ordernotif_handler);
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

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyOrder notifyitem = STNotifyOrder.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);
					}

					addOlderNotifyOrders();

					updateCurrentOrderNotifyNewsState();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyOrder notifyitem = STNotifyOrder.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);
					}

					insertLatestNotifyOrders();

					updateCurrentOrderNotifyNewsState();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

			String szTime = arrOrderNotifs.get(0).time;
			long uid = arrOrderNotifs.get(0).uid;

			Global.saveLastOrderNotificationID(getApplicationContext(), uid);
			Global.saveLastOrderNotificationTime(getApplicationContext(), szTime);
		}

		ordernotif_adapter.notifyDataSetChanged();
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
			order_notif_page = arrOrderNotifs.size() / Global.getPageItemCount();

		ordernotif_adapter.notifyDataSetChanged();

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


	private void getOlderPersonNotifs()
	{
		CommManager.getNotificationsPage(Global.loadUserID(getApplicationContext()), person_notif_page, Global.getIMEI(getApplicationContext()), older_personnotif_handler);
	}

	private void getLatestPersonNotifs()
	{
		arrPersonNotifs.clear();
		person_notif_page = 0;
		getOlderPersonNotifs();
//		long limitid = 0;
//		if (arrPersonNotifs.size() > 0)
//			limitid = arrPersonNotifs.get(0).uid;

//		CommManager.getLatestNotifications(Global.loadUserID(getApplicationContext()), limitid, Global.getIMEI(getApplicationContext()), latest_personnotif_handler);
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

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyPerson notifyitem = STNotifyPerson.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);
					}

					addOlderNotifyPerson();

					updateCurrentPersonNotifyNewsState();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					arrTempData.clear();
					JSONArray arrItems = result.getJSONArray("retdata");
					for (int i = 0; i < arrItems.length(); i++)
					{
						JSONObject itemObj = arrItems.getJSONObject(i);
						STNotifyPerson notifyitem = STNotifyPerson.dcodeFromJSON(itemObj);
						arrTempData.add(notifyitem);
					}

					insertLatestNotifyPerson();

					updateCurrentPersonNotifyNewsState();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(NewsActivity.this, szRetMsg, Gravity.CENTER);
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
			Global.showAdvancedToast(NewsActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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


	private void onClickBack()
	{
		finishWithAnimation();
	}


	private void updateCurrentOrderNotifyNewsState() {
		if (order_notif_count <= 0) {
			txtBadge2.setVisibility(View.INVISIBLE);
		} else {
			boolean hasNews = false;
			for (int i = 0; i < arrOrderNotifs.size(); i++) {
				STNotifyOrder order_notify = arrOrderNotifs.get(i);
				if (order_notify.hasread == 0) {
					hasNews = true;
					break;
				}
			}

			if (hasNews)
				txtBadge2.setVisibility(View.VISIBLE);
			else
				txtBadge2.setVisibility(View.INVISIBLE);
		}
	}

	private void updateCurrentPersonNotifyNewsState() {
		if (person_notif_count <= 0) {
			txtBadge3.setVisibility(View.INVISIBLE);
		} else {
			boolean hasNews = false;
			for (int i = 0; i < arrPersonNotifs.size(); i++) {
				STNotifyPerson person_notify = arrPersonNotifs.get(i);
				if (person_notify.hasread == 0) {
					hasNews = true;
					break;
				}
			}

			if (hasNews)
				txtBadge3.setVisibility(View.VISIBLE);
			else
				txtBadge3.setVisibility(View.INVISIBLE);
		}
	}
}

