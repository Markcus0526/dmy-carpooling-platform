package com.damytech.PincheApp;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STCoupon;
import com.damytech.DataClasses.STTsInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.CouponCodeFilter;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-9
 * Time: 上午3:23
 * To change this template use File | Settings | File Templates.
 */
public class CouponActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private ImageButton btn_plus = null;
	private EditText edt_coupon_code = null;

	CouponAdapter coupon_adapter = new CouponAdapter();

	public ArrayList<STCoupon> arrCoupons = new ArrayList<STCoupon>();
	public ArrayList<STCoupon> arrTemp = new ArrayList<STCoupon>();

	private PullToRefreshListView listView = null;
	private int pageno = 0;

	private Button btn_coupon_detail = null;
	private RelativeLayout coupon_detail_layout = null;
	private TextView txt_coupon_code = null;
	private TextView txt_coupon_content = null;
	private TextView txt_unit_name = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_coupon);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		listView = (PullToRefreshListView)findViewById(R.id.scroll_view);
		{
			listView.setMode(PullToRefreshBase.Mode.BOTH);
			listView.setOnRefreshListener(coupon_log_refreshhandler);
			listView.setAdapter(coupon_adapter);
		}

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_plus = (ImageButton)findViewById(R.id.btn_add_coupon);

		edt_coupon_code = (EditText)findViewById(R.id.edt_coupon_code);
		edt_coupon_code.setFilters(new InputFilter[] {new CouponCodeFilter()});

		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		btn_plus.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickPlus();
			}
		});

		coupon_detail_layout = (RelativeLayout)findViewById(R.id.coupon_detail_layout);
		coupon_detail_layout.setVisibility(View.GONE);

		txt_coupon_code = (TextView)findViewById(R.id.txt_coupon_code);
		txt_coupon_content = (TextView)findViewById(R.id.txt_coupon_content);
		txt_unit_name = (TextView)findViewById(R.id.txt_spreadunit_name);
		btn_coupon_detail = (Button)findViewById(R.id.btn_coupon_content);
		btn_coupon_detail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				coupon_detail_layout.setVisibility(View.GONE);
			}
		});

		startProgress();
		getOlderLogs();
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

	private PullToRefreshBase.OnRefreshListener coupon_log_refreshhandler = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestLogs();
			else
				getOlderLogs();
		}
	};

	private void getLatestLogs()
	{
		if (arrCoupons == null || arrCoupons.size() == 0)
		{
			pageno = 0;
			CommManager.getPagedCoupon(Global.loadUserID(getApplicationContext()), pageno, Global.getIMEI(getApplicationContext()), older_coupon_logs_handler);
		}
		else
			CommManager.getLatestCoupon(Global.loadUserID(getApplicationContext()), arrCoupons.get(0).uid, Global.getIMEI(getApplicationContext()), latest_coupon_logs_handler);
	}

	private void getOlderLogs()
	{
		CommManager.getPagedCoupon(Global.loadUserID(getApplicationContext()), pageno, Global.getIMEI(getApplicationContext()), older_coupon_logs_handler);
	}

	private AsyncHttpResponseHandler older_coupon_logs_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

			stopProgress();
			listView.onRefreshComplete();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					arrTemp.clear();
					JSONArray arrLogs = result.getJSONArray("retdata");
					for (int i = 0; i < arrLogs.length(); i++)
					{
						JSONObject logitem = arrLogs.getJSONObject(i);

						STCoupon info = STCoupon.decodeFromJSON(logitem);
						arrTemp.add(info);
					}

					addOldLogs();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(CouponActivity.this, szRetmsg, Gravity.CENTER);
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
			listView.onRefreshComplete();
			stopProgress();
		}
	};


	private AsyncHttpResponseHandler latest_coupon_logs_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(int statusCode, String content) {
			super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.
			listView.onRefreshComplete();
			stopProgress();

			try
			{
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					arrTemp.clear();
					JSONArray arrLogs = result.getJSONArray("retdata");
					for (int i = 0; i < arrLogs.length(); i++)
					{
						JSONObject logitem = arrLogs.getJSONObject(i);

						STCoupon info = STCoupon.decodeFromJSON(logitem);
						arrTemp.add(info);
					}

					insertLatestLogs();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(CouponActivity.this, szRetmsg, Gravity.CENTER);
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
			listView.onRefreshComplete();
			stopProgress();
		}
	};


	private void addOldLogs()
	{
		boolean hasUpdate = false;

		for (int i = 0; i < arrTemp.size(); i++)
		{
			STCoupon new_item = arrTemp.get(i);

			boolean isExist = false;
			for (int j = 0; j < arrCoupons.size(); j++)
			{
				STCoupon old_item = arrCoupons.get(j);
				if (old_item.uid == new_item.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrCoupons.add(new_item);

			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			pageno = arrCoupons.size() / Global.getPageItemCount();
		}

		coupon_adapter.notifyDataSetChanged();
	}


	private void insertLatestLogs()
	{
		boolean hasUpdate = false;

		for (int i = 0; i < arrTemp.size(); i++)
		{
			STCoupon new_item = arrTemp.get(i);

			boolean isExist = false;
			for (int j = 0; j < arrCoupons.size(); j++)
			{
				STCoupon old_item = arrCoupons.get(j);
				if (old_item.uid == new_item.uid)
				{
					isExist = true;
					break;
				}
			}

			if (isExist)
				continue;

			arrCoupons.add(0, new_item);

			if (!hasUpdate)
				hasUpdate = true;
		}

		if (hasUpdate)
		{
			pageno = arrCoupons.size() / Global.getPageItemCount();
		}

		coupon_adapter.notifyDataSetChanged();
	}


	private class CouponAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			return arrCoupons.size();
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
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public Object getItem(int position) {
			return arrCoupons.get(position);
		}

		@Override
		public boolean isEmpty() {
			if (arrCoupons == null)
				return true;

			return arrCoupons.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			STCoupon coupon = arrCoupons.get(position);

			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.coupon_layout, null);
				convertView.setBackgroundColor(Color.WHITE);

				STCouponHolder holder = new STCouponHolder();

				holder.txtNumber = (TextView)convertView.findViewById(R.id.txt_coupon_id);
				holder.txtNumber.setText("" + (position + 1));

				holder.txtApplyRange = (TextView)convertView.findViewById(R.id.txt_apply_range);
				holder.txtApplyRange.setText(coupon.range);

				holder.txtContent = (TextView)convertView.findViewById(R.id.txt_content);
				holder.txtContent.setText(coupon.contents);

				holder.txtUseCond = (TextView)convertView.findViewById(R.id.txt_use_cond);
				holder.txtUseCond.setText(coupon.usecond);

				holder.txtDateExp = (TextView)convertView.findViewById(R.id.txt_exp_date);
				holder.txtDateExp.setText(coupon.dateexp);

				holder.btnItem = (ImageButton)convertView.findViewById(R.id.btn_coupon);
				if (coupon.is_goods == 1)
				{
					holder.btnItem.setBackgroundResource(R.drawable.btn_empty);
					holder.btnItem.setTag(coupon);
					holder.btnItem.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v) {
							STCoupon coupon = (STCoupon)v.getTag();
							showCouponDetail(coupon);
						}
					});
				}
				else
				{
					holder.btnItem.setBackgroundResource(R.drawable.bk_empty);
					holder.btnItem.setOnClickListener(null);
				}

				holder.uid = coupon.uid;

				convertView.setTag(holder);

				ResolutionSet.instance.iterateChild(convertView, mScrSize.x, mScrSize.y);
			}
			else
			{
				STCouponHolder viewHolder = (STCouponHolder)convertView.getTag();

				viewHolder.txtApplyRange.setText(coupon.range);
				viewHolder.txtContent.setText(coupon.contents);
				viewHolder.txtUseCond.setText(coupon.usecond);
				viewHolder.txtDateExp.setText(coupon.dateexp);
				viewHolder.txtNumber.setText("" + (position + 1));
				viewHolder.uid = coupon.uid;

				if (coupon.is_goods == 1)
				{
					viewHolder.btnItem.setBackgroundResource(R.drawable.btn_empty);
					viewHolder.btnItem.setTag(coupon);
					viewHolder.btnItem.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v) {
							STCoupon coupon = (STCoupon)v.getTag();
							showCouponDetail(coupon);
						}
					});
				}
				else
				{
					viewHolder.btnItem.setBackgroundResource(R.drawable.bk_empty);
					viewHolder.btnItem.setOnClickListener(null);
				}
			}

			return convertView;
		}
	}

	private class STCouponHolder
	{
		public long uid = 0;
		public TextView txtApplyRange = null;
		public TextView txtContent = null;
		public TextView txtUseCond = null;
		public TextView txtDateExp = null;
		public TextView txtNumber = null;
		public ImageButton btnItem = null;
	}


	private void onClickPlus() {
		if (edt_coupon_code.getText().toString().equals(""))
		{
			Global.showAdvancedToast(CouponActivity.this, getResources().getString(R.string.STR_INPUT_COUPON_PASSWORD), Gravity.CENTER);
			return;
		}

		startProgress();
		CommManager.addCoupon(Global.loadUserID(getApplicationContext()), edt_coupon_code.getText().toString(), Global.getIMEI(getApplicationContext()), add_handler);
	}

	private AsyncHttpResponseHandler add_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONObject result = jsonObject.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(CouponActivity.this, getResources().getString(R.string.STR_COUPON_ADDED), Gravity.CENTER);

					JSONObject retdata = result.getJSONObject("retdata");
					STCoupon coupon = STCoupon.decodeFromJSON(retdata);

					showCouponDetail(coupon);

					arrCoupons.add(0, coupon);
					coupon_adapter.notifyDataSetChanged();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(CouponActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(CouponActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void showCouponDetail(STCoupon coupon)
	{
		coupon_detail_layout.setVisibility(View.VISIBLE);
		txt_coupon_content.setText(coupon.contents);
		txt_coupon_code.setText(coupon.usecond);
		txt_unit_name.setText(coupon.dateexp);
	}
}