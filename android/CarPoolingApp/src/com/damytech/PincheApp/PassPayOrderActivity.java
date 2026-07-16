package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STCouponListItem;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-21
 * Time: 上午9:20
 * To change this template use File | Settings | File Templates.
 */
public class PassPayOrderActivity extends SuperActivity
{
	private TextView txtBalance = null;
	private ListView lstCoupons = null;
	private TextView txtCashPay = null;
	private TextView txtCouponPay = null;
	private Button btnPay = null;
	private ImageButton btnBack = null;

	private TextView txtReserveNote = null;

	private long orderID = 0;
	private int ordertype = 0;
	private double price = 0;
	private boolean isReserve = false;

	private ArrayList<STCouponListItem> arrCouponItems = new ArrayList<STCouponListItem>();
	private CouponAdapter adapter = null;

	private Dialog dlgBalNotEnough = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_payorder);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		orderID = getIntent().getLongExtra("orderid", 0);
		ordertype = getIntent().getIntExtra("ordertype", 0);
		price = getIntent().getDoubleExtra("price", 0);
		isReserve = getIntent().getBooleanExtra("reserve", false);

		txtBalance = (TextView)findViewById(R.id.txt_balance);
		lstCoupons = (ListView)findViewById(R.id.listView);
		{
			lstCoupons.setDivider(new ColorDrawable(Color.parseColor("#FFFFFF")));
			lstCoupons.setCacheColorHint(Color.parseColor("#FFFFFF"));
		}
		txtCashPay = (TextView)findViewById(R.id.txt_cash_pay);
		txtCouponPay = (TextView)findViewById(R.id.txt_coupon_pay);

		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnPay = (Button)findViewById(R.id.btn_pay);
		btnPay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickPay();
			}
		});

		txtReserveNote = (TextView)findViewById(R.id.txt_reserve_note);

		initializeValues();

		adapter = new CouponAdapter(PassPayOrderActivity.this, arrCouponItems);
		lstCoupons.setAdapter(adapter);

		startProgress();
		CommManager.getUsableCoupons(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), coupons_handler);
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


	private void onClickPay()
	{
		String szIDs = "";
		for (int i = 0; i < arrCouponItems.size(); i++)
		{
			STCouponListItem listItem = arrCouponItems.get(i);
			if (listItem.isEnabled && listItem.isSelected)
			{
				if (!szIDs.equals(""))
					szIDs += ",";
				szIDs += listItem.id;
			}
		}

		if (isReserve)          // Reserve order
		{
			startProgress();
			CommManager.payReserveOrder(Global.loadUserID(getApplicationContext()),
					orderID,
					price,
					szIDs,
					Global.getIMEI(getApplicationContext()),
					pay_handler);
		}
		else
		{
			startProgress();
			CommManager.payNormalOrder(Global.loadUserID(getApplicationContext()),
					orderID,
					ordertype,
					price,
					szIDs,
					Global.getIMEI(getApplicationContext()),
					pay_handler);
		}
	}


	private AsyncHttpResponseHandler coupons_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					JSONArray arrCoupons = result.getJSONArray("retdata");
					for (int i = 0; i < arrCoupons.length(); i++)
					{
						JSONObject couponItem = arrCoupons.getJSONObject(i);
						STCouponListItem cpnValue = STCouponListItem.decodeFromJSON(couponItem);
						cpnValue.isEnabled = isSelectable(cpnValue);
						arrCouponItems.add(cpnValue);
					}
					adapter.notifyDataSetChanged();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPayOrderActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassPayOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private class STCouponViewHolder
	{
		TextView txtDesc = null;
		ImageView imgCheck = null;
		ImageButton btnItem = null;
	}


	private class CouponAdapter extends ArrayAdapter<STCouponListItem>
	{
		public CouponAdapter(Context context, List<STCouponListItem> objects) {
			super(context, 0, objects);    //To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			STCouponListItem couponItem = arrCouponItems.get(position);

			STCouponViewHolder holder = null;
			if (convertView == null)
			{
				int nWidth = 440, nHeight = 70, nYMargin = 0;
				RelativeLayout itemLayout = new RelativeLayout(lstCoupons.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight + nYMargin * 2);
				itemLayout.setLayoutParams(layoutParams);

				RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_coupon_item, null);
				RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
				RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
				item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				itemView.setLayoutParams(item_layoutParams);
				itemLayout.addView(itemView);

				ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

				convertView = itemLayout;

				holder = new STCouponViewHolder();
				convertView.setTag(holder);
			}
			else
			{
				holder = (STCouponViewHolder)convertView.getTag();
			}

			TextView txtDesc = null;
			if (holder.txtDesc == null)
				holder.txtDesc = (TextView)convertView.findViewById(R.id.txt_desc);
			txtDesc = holder.txtDesc;
			txtDesc.setText(couponItem.desc);

			if (holder.imgCheck == null)
				holder.imgCheck = (ImageView)convertView.findViewById(R.id.img_check);
			final ImageView imgCheck = holder.imgCheck;
			if (couponItem.isSelected)
				imgCheck.setBackgroundResource(R.drawable.checkbox_selected);
			else
				imgCheck.setBackgroundResource(R.drawable.checkbox_normal);


			ImageButton btnItem = null;
			if (holder.btnItem == null)
				holder.btnItem = (ImageButton)convertView.findViewById(R.id.btn_item);
			btnItem = holder.btnItem;
			btnItem.setTag("" + couponItem.id);
			btnItem.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					clickItem(v, imgCheck);
				}
			});

			if (couponItem.isEnabled)
			{
				btnItem.setEnabled(true);
		        txtDesc.setTextColor(Color.BLACK);
			}
			else
			{
				btnItem.setEnabled(false);
                txtDesc.setTextColor(Color.LTGRAY);
                imgCheck.setBackgroundResource(R.drawable.checkbox_normal);
			}

			return convertView;
		}
	}


	private void initializeValues()
	{
		txtBalance.setText("" + price + getResources().getString(R.string.STR_BALANCE_DIAN));
		if (isReserve)
		{
			txtReserveNote.setVisibility(View.VISIBLE);
			txtCashPay.setText(getResources().getString(R.string.STR_HAIXUZHIFU) + ":" + price + getResources().getString(R.string.STR_BALANCE_DIAN));
			txtCouponPay.setText(getResources().getString(R.string.STR_DIANQUANZHIFU) + ":0" + getResources().getString(R.string.STR_BALANCE_DIAN));
		}
		else
		{
			txtReserveNote.setVisibility(View.GONE);
			txtCashPay.setText(getResources().getString(R.string.STR_SHIJIZHIFU) + ":" + price + getResources().getString(R.string.STR_BALANCE_DIAN));
			txtCouponPay.setText(getResources().getString(R.string.STR_DIANQUANZHIFU) + ":0" + getResources().getString(R.string.STR_BALANCE_DIAN));
		}
	}


	private void clickItem(View v, ImageView imgCheck)
	{
		String szID = (String)v.getTag();
		long id = Long.parseLong(szID);
		STCouponListItem selItem = null;

		for (int i = 0; i < arrCouponItems.size(); i++)
		{
			STCouponListItem listItem = arrCouponItems.get(i);
			if (listItem.id == id)
			{
				selItem = listItem;
				break;
			}
		}

		if (selItem == null)
			return;


		selItem.isSelected = !selItem.isSelected;
		if (selItem.isSelected)
		{
			imgCheck.setBackgroundResource(R.drawable.checkbox_selected);
		}
		else
		{
			imgCheck.setBackgroundResource(R.drawable.checkbox_normal);
		}


		// Enable or disable other coupons
		ArrayList<STCouponListItem> arrSelectableItems = getEnableItems(selItem);

		if (selItem.isSelected)
		{
			ArrayList<STCouponListItem> arrEnabledItems = new ArrayList<STCouponListItem>();
			for (int i = 0; i < arrCouponItems.size(); i++)
			{
				STCouponListItem couponItem = arrCouponItems.get(i);
				if (couponItem.isEnabled && couponItem.id != selItem.id)
				{
					arrEnabledItems.add(couponItem);
				}
			}


			for (int i = 0; i < arrEnabledItems.size(); i++)
			{
				STCouponListItem itemEnabled = arrEnabledItems.get(i);

				boolean isSelectable = false;
				for (int j = 0; j < arrSelectableItems.size(); j++)
				{
					if (itemEnabled.id == arrSelectableItems.get(j).id)            // This item is selectable
					{
						isSelectable = true;
						break;
					}
				}

				if (!isSelectable)
				{
					itemEnabled.isEnabled = false;
				}
			}
		}
		else
		{
			ArrayList<STCouponListItem> arrSelectedItems = new ArrayList<STCouponListItem>();
			for (int i = 0; i < arrCouponItems.size(); i++)
			{
				STCouponListItem couponItem = arrCouponItems.get(i);
				if (couponItem.isSelected && couponItem.isEnabled)
				{
					arrSelectedItems.add(couponItem);
				}
			}

			ArrayList<STCouponListItem> arrDisabledItems = new ArrayList<STCouponListItem>();
			for (int i = 0; i < arrCouponItems.size(); i++)
			{
				STCouponListItem couponItem = arrCouponItems.get(i);
				if (!couponItem.isEnabled)
				{
					arrDisabledItems.add(couponItem);

				}
			}

			for (int i = 0; i < arrDisabledItems.size(); i++)
			{
				STCouponListItem itemDisabled = arrDisabledItems.get(i);

				boolean isEnabled = isSelectable(itemDisabled);
				for (int j = 0; j < arrSelectedItems.size(); j++)
				{
					STCouponListItem itemSelected = arrSelectedItems.get(j);
					ArrayList<STCouponListItem> selectableItems = getEnableItems(itemSelected);
					boolean isSelectable = false;
					for (int k = 0; k < selectableItems.size(); k++)
					{
						if (selectableItems.get(k).id == itemDisabled.id)
						{
							isSelectable = true;
							break;
						}
					}

					if (!isSelectable)
					{
						isEnabled = false;
						break;
					}
				}

				if (isEnabled)
				{
					itemDisabled.isEnabled = true;
					itemDisabled.isSelected = false;
				}
			}

		}

		adapter.notifyDataSetChanged();

		double couponPrice = curSelValue();
		double fLeftPrice = price - couponPrice;
		if (fLeftPrice < 0)
			fLeftPrice = 0;
		txtCashPay.setText(getResources().getString(R.string.STR_HAIXUZHIFU) + ":" + fLeftPrice + getResources().getString(R.string.STR_BALANCE_DIAN));
		txtCouponPay.setText(getResources().getString(R.string.STR_DIANQUANZHIFU) + ":" + couponPrice + getResources().getString(R.string.STR_BALANCE_DIAN));
	}


	private double curSelValue()
	{
		double fSum = 0;
		for (int i = 0; i < arrCouponItems.size(); i++)
		{
			STCouponListItem listItem = arrCouponItems.get(i);
			if (listItem.isEnabled && listItem.isSelected)
				fSum += listItem.price;
		}
		return fSum;
	}


	private ArrayList<STCouponListItem> getEnableItems(STCouponListItem selItem)
	{
		ArrayList<STCouponListItem> arrItems = new ArrayList<STCouponListItem>();

		for (int i = 0; i < arrCouponItems.size(); i++)
		{
			STCouponListItem listItem = arrCouponItems.get(i);

			if (!isSelectable(listItem))
				continue;

			boolean isSelectable = false;
			if (selItem == null)
			{
				isSelectable = true;
			}
			else
			{
				if (selItem.cond_type == ConstData.COUPON_COND_NO)
				{
					isSelectable = true;
				}
				else if (selItem.cond_type == ConstData.COUPON_COND_ORDERPRICEX)
				{
					isSelectable = price >= selItem.cond_value;
				}
				else if (selItem.cond_type == ConstData.COUPON_COND_ONLY)
				{
					isSelectable = false;
				}
				else if (selItem.cond_type == ConstData.COUPON_COND_ONLY_FORORDER)
				{
					isSelectable = (selItem.syscoupon != listItem.syscoupon);
				}
				else
				{
					isSelectable = true;
				}
			}

			if (isSelectable)
				arrItems.add(listItem);
		}

		return arrItems;
	}


	// Check if coupon can be selected alone or not
	private boolean isSelectable(STCouponListItem item)
	{
		boolean result = false;

		if (item.cond_type == ConstData.COUPON_COND_NO)
		{
			result = true;
		}
		else if (item.cond_type == ConstData.COUPON_COND_ORDERPRICEX)
		{
			result = price >= item.cond_value;
		}
		else if (item.cond_type == ConstData.COUPON_COND_ONLY)
		{
			result = true;
		}
		else if (item.cond_type == ConstData.COUPON_COND_ONLY_FORORDER)
		{
			result = true;
		}

		return result;
	}


	private AsyncHttpResponseHandler pay_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassPayOrderActivity.this, getResources().getString(R.string.STR_ZHIFUCHENGGONG), Gravity.CENTER);

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetCode == ConstData.ERR_CODE_BALNOTENOUGH)           // Not enough
				{
					dlgBalNotEnough = new Dialog(PassPayOrderActivity.this);
					dlgBalNotEnough.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dlgBalNotEnough.setContentView(R.layout.dlg_pass_notenough_balance);
					dlgBalNotEnough.setCancelable(false);

					ResolutionSet.instance.iterateChild(dlgBalNotEnough.findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);

					TextView txtMyBalance = (TextView)dlgBalNotEnough.findViewById(R.id.txt_my_balance);
					TextView txtOrderPrice = (TextView)dlgBalNotEnough.findViewById(R.id.txt_order_price);

					JSONObject retdata = result.getJSONObject("retdata");
					double bal = retdata.getDouble("balance");
					txtMyBalance.setText("" + bal + getResources().getString(R.string.STR_BALANCE_DIAN));
					txtOrderPrice.setText("" + price + getResources().getString(R.string.STR_BALANCE_DIAN));

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
							onClickCharge();
						}
					});

					dlgBalNotEnough.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dlgBalNotEnough.show();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassPayOrderActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassPayOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void onClickCharge()
	{
		Intent intent = new Intent(PassPayOrderActivity.this, BalanceActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra(BalanceActivity.CHARGE_TAB_NAME, BalanceActivity.CHARGE_TAB);
		PassPayOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


}
