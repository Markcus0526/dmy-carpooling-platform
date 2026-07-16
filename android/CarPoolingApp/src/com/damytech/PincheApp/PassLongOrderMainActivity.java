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
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STPassLongOrder;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class PassLongOrderMainActivity extends SuperActivity
{
    private String      mStartCity = "";
    private String      mEndCity = "";
	private ImageButton  btn_back;
	private EditText	txtStartCity;
	private EditText	txtEndCity;
    private Button      btnSearch;

	ConfirmPasswordDialog dlgPwd = null;

	private PullToRefreshListView longOrdersList = null;
	private ArrayList<STPassLongOrder> arrLongOrders = new ArrayList<STPassLongOrder>();
	private LongOrderAdapter longOrderAdapter = null;
	private int pageno = 0;

    private ArrayList<String> arrSeats = new ArrayList<String>();
    private SeatItemAdapter seatItemAdapter = null;
    private ListView listSeats = null;
    public int selectedOrderIndex = 0;
	private RelativeLayout seats_layout = null;

	private String szPwd = "";
	private long orderid = 0;
    private int seats = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pass_long_main);

		initControls();//控件示例化
		initResolution();//适配屏幕
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		getLatestLongOrders();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	private void initControls()
	{
		seats_layout = (RelativeLayout)findViewById(R.id.relativeLayout2);
		seats_layout.setVisibility(View.INVISIBLE);
		seats_layout.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				hideSeatList();
			}
		});

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		txtStartCity = (EditText)findViewById(R.id.txtStartCity);

		txtStartCity.setText(Global.loadCityName(getApplicationContext()));
		mStartCity = txtStartCity.getText().toString();

		txtEndCity = (EditText)findViewById(R.id.txtEndCity);
        btnSearch = (Button)findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                searchLongOrders();
            }
        });

		longOrdersList = (PullToRefreshListView)findViewById(R.id.longOrdersList);
		{
			longOrderAdapter = new LongOrderAdapter(PassLongOrderMainActivity.this, arrLongOrders);

			longOrdersList.setMode(PullToRefreshBase.Mode.BOTH);
			longOrdersList.setOnRefreshListener(orderListListener);
			longOrdersList.setAdapter(longOrderAdapter);
			longOrdersList.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFF1F1F1")));
			longOrdersList.getRefreshableView().setCacheColorHint(Color.parseColor("#FFF1F1F1"));
		}

        listSeats = (ListView)findViewById(R.id.listSeats);
        listSeats.setDividerHeight(0);
        seatItemAdapter = new SeatItemAdapter(PassLongOrderMainActivity.this, R.id.listSeats, arrSeats);
        listSeats.setAdapter(seatItemAdapter);
		listSeats.setDivider(new ColorDrawable(Color.WHITE));
		listSeats.setCacheColorHint(Color.WHITE);

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

    private void searchLongOrders()
    {
        startProgress();

        mStartCity = txtStartCity.getText().toString();
        mEndCity = txtEndCity.getText().toString();
        pageno = 0;
        CommManager.getPagedAcceptableLongOrders(Global.loadUserID(getApplicationContext()),
                pageno,
                mStartCity,
                mEndCity,
                Global.getIMEI(getApplicationContext()),
                searchLongOrderHandler);
    }

	private void getLatestLongOrders()
	{
		pageno = 0;
		arrLongOrders.clear();
		longOrderAdapter.notifyDataSetChanged();

        getPagedLongOrders();
	}

	private void getPagedLongOrders()
	{
		startProgress();
		CommManager.getPagedAcceptableLongOrders(Global.loadUserID(getApplicationContext()),
				pageno,
                mStartCity,
				mEndCity,
				Global.getIMEI(getApplicationContext()),
				pagedLongOrderHandler);
	}


	private class STLongOrderViewHolder
	{
		TextView lblDest = null;
		TextView lblPrice = null;
		TextView lblTime = null;
		SmartImageView imgPassPhoto = null;
		ImageView imgGender = null;
		TextView lblAge = null;
		TextView lblName = null;
		TextView lblRemain = null;
		ImageButton btnAccept = null;
		ImageButton btnPhoto = null;
		ImageButton btnBackground = null;
		TextView lblItemSeat = null;
		Button btnSeatBack = null;
	}


	private class LongOrderAdapter extends ArrayAdapter<STPassLongOrder>
	{
		public LongOrderAdapter(Context context, List<STPassLongOrder> objects) {
			super(context, 0, objects);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
            if(arrLongOrders.size() > 0) {
                STPassLongOrder orderItem = arrLongOrders.get(position);

	            STLongOrderViewHolder holder = null;
                if (convertView == null)
                {
                    int nWidth = 460, nHeight = 250, nYMargin = 10;
                    RelativeLayout itemLayout = new RelativeLayout(parent.getContext());
                    AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight + nYMargin * 2);
                    itemLayout.setLayoutParams(layoutParams);

                    RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_pass_longdistanceitem, null);
                    RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
                    RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
                    item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    itemView.setLayoutParams(item_layoutParams);
                    itemLayout.addView(itemView);

                    ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

                    convertView = itemLayout;

	                holder = new STLongOrderViewHolder();
	                convertView.setTag(holder);
                }
	            else
                {
	                holder = (STLongOrderViewHolder)convertView.getTag();
                }

                // Set address
                TextView lblDest = null;
	            if (holder.lblDest == null)
		            holder.lblDest = (TextView)convertView.findViewById(R.id.lblDest);
	            lblDest = holder.lblDest;
                lblDest.setText(orderItem.start_city + getResources().getString(R.string.addr_separator) + orderItem.end_city);

                // Set price
                TextView lblPrice = null;
	            if (holder.lblPrice == null)
		            holder.lblPrice = (TextView)convertView.findViewById(R.id.lblPrice);
	            lblPrice = holder.lblPrice;
                lblPrice.setText(orderItem.price + getResources().getString(R.string.STR_LONGDISTANCE_DIANZUO));

                // Set time as empty
                TextView lblTime = null;
	            if (holder.lblTime == null)
		            holder.lblTime = (TextView)convertView.findViewById(R.id.lblTime);
	            lblTime = holder.lblTime;
                lblTime.setText(orderItem.start_time);

                // Set passenger photo
                SmartImageView imgPassPhoto = null;
	            if (holder.imgPassPhoto == null)
		            holder.imgPassPhoto = (SmartImageView)convertView.findViewById(R.id.imgPhoto);
	            imgPassPhoto = holder.imgPassPhoto;
	            imgPassPhoto.isCircular = true;
	            imgPassPhoto.setImageUrl(orderItem.driver_img, R.drawable.default_user_img);

                // Set passenger gender
                ImageView imgGender = null;
	            if (holder.imgGender == null)
		            holder.imgGender = (ImageView)convertView.findViewById(R.id.imgSex);
	            imgGender = holder.imgGender;
                if (orderItem.driver_gender == 0)
                    imgGender.setImageResource(R.drawable.bk_manmark);
                else
                    imgGender.setImageResource(R.drawable.bk_womanmark);

                // Set passenger age
                TextView lblAge = null;
	            if (holder.lblAge == null)
		            holder.lblAge = (TextView)convertView.findViewById(R.id.lblAge);
	            lblAge = holder.lblAge;
                lblAge.setText("" + orderItem.driver_age);

                // Set passenger passenger name
                TextView lblName = null;
	            if (holder.lblName == null)
		            holder.lblName = (TextView)convertView.findViewById(R.id.lblName);
	            lblName = holder.lblName;
                lblName.setText(orderItem.driver_name);

                TextView lblRemain = null;
	            if (holder.lblRemain == null)
		            holder.lblRemain = (TextView)convertView.findViewById(R.id.lblRemain);
	            lblRemain = holder.lblRemain;
                if(orderItem.left_seats > 1)
                {
                    lblRemain.setText(getResources().getString(R.string.STR_LONGDISTANCEORDER_SHENGYU) + orderItem.left_seats +
                            getResources().getString(R.string.STR_LONGDISTANCE_ZUO));
                    lblRemain.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
                }
                else
                {
                    lblRemain.setText(getResources().getString(R.string.STR_LONGDISTANCEORDER_JINYU) + orderItem.left_seats +
                            getResources().getString(R.string.STR_LONGDISTANCE_ZUO));
                    lblRemain.setTextColor(getResources().getColor(R.color.RED_COLOR));
                }


                // Set button handler
                final long orderid = orderItem.uid;
                final long driverid = orderItem.driver_id;
                final int leftSeats = orderItem.left_seats;
                ImageButton btnAccept = null;
	            if (holder.btnAccept == null)
		            holder.btnAccept = (ImageButton)convertView.findViewById(R.id.btn_accept);
	            btnAccept = holder.btnAccept;
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCheckOrderDialog(position, leftSeats);
//                        acceptLongOrder(position, leftSeats);
                    }
                });

                ImageButton btnPhoto = null;
	            if (holder.btnPhoto == null)
		            holder.btnPhoto = (ImageButton)convertView.findViewById(R.id.btn_photo);
	            btnPhoto = holder.btnPhoto;
                btnPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectUser(driverid);
                    }
                });

                ImageButton btnBackground = null;
	            if (holder.btnBackground == null)
		            holder.btnBackground = (ImageButton)convertView.findViewById(R.id.btn_background);
	            btnBackground = holder.btnBackground;
                btnBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSeatList();
                        selectLongOrder(orderid);
                    }
                });

                TextView lblItemSeat = null;
	            if (holder.lblItemSeat == null)
		            holder.lblItemSeat = (TextView)convertView.findViewById(R.id.lblSeat);
	            lblItemSeat = holder.lblItemSeat;
                lblItemSeat.setText(orderItem.grab_seats + getResources().getString(R.string.STR_LONGDISTANCE_ZUO));

                Button btnSeatBack = null;
	            if (holder.btnSeatBack == null)
		            holder.btnSeatBack = (Button)convertView.findViewById(R.id.btn_seat);
	            btnSeatBack = holder.btnSeatBack;
                btnSeatBack.setTag(position);
                btnSeatBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedOrderIndex = (Integer)v.getTag();
                        showSeatList();
                    }
                });
            }
			return convertView;
		}
	}


	private class STSeatItemViewHolder
	{
		TextView lblSeatCount = null;
		Button btnEmpty = null;
	}


    public class SeatItemAdapter extends ArrayAdapter<String>
    {
        Context ctx;
        ArrayList<String> list = new ArrayList<String>();

        public SeatItemAdapter(Context ctx, int resourceId, ArrayList<String> list) {
            super(ctx, resourceId, list);
            this.ctx = ctx;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String seat = list.get(position);

            View v = convertView;
	        STSeatItemViewHolder holder = null;
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.view_pass_longorder_seatitem, null);
                Point point = Global.getScreenSize(PassLongOrderMainActivity.this);
                ResolutionSet.instance.iterateChild(v, point.x, point.y);

	            holder = new STSeatItemViewHolder();
	            v.setTag(holder);
            }
	        else
            {
	            holder = (STSeatItemViewHolder)v.getTag();
            }

            TextView lblSeatCount = null;
	        if (holder.lblSeatCount == null)
		        holder.lblSeatCount = (TextView) v.findViewById(R.id.lblSeatCount);
	        lblSeatCount = holder.lblSeatCount;
            lblSeatCount.setText(seat);

            Button btnEmpty = null;
	        if (holder.btnEmpty == null)
		        holder.btnEmpty = (Button)v.findViewById(R.id.btn_empty);
	        btnEmpty = holder.btnEmpty;
            btnEmpty.setTag(position + 1);
            btnEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seatSelected((Integer)v.getTag());
                    hideSeatList();
                }
            });

            return v;
        }
    }

    private void showSeatList()
    {
	    arrSeats.clear();
	    seatItemAdapter.notifyDataSetChanged();

        STPassLongOrder orderItem = arrLongOrders.get(selectedOrderIndex);
        for(int i = 0; i < orderItem.left_seats; i++) {
            arrSeats.add((i + 1) + getResources().getString(R.string.STR_LONGDISTANCE_ZUO));
        }
	    seatItemAdapter.notifyDataSetChanged();

	    startProgress();
	    TimerTask task = new TimerTask() {
		    @Override
		    public void run() {
			    runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					    stopProgress();
					    seats_layout.setVisibility(View.VISIBLE);
				    }
			    });
		    }
	    };
	    Timer timer = new Timer();
	    timer.schedule(task, 500);
    }

    private void hideSeatList()
    {
	    seats_layout.setVisibility(View.GONE);
    }

    private void seatSelected(int count)
    {
		STPassLongOrder orderItem = arrLongOrders.get(selectedOrderIndex);
		orderItem.grab_seats = count;
		longOrderAdapter.notifyDataSetChanged();
    }


    private PullToRefreshBase.OnRefreshListener orderListListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
            if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
                getLatestLongOrders();
            else
                getPagedLongOrders();
        }
    };


    private AsyncHttpResponseHandler latestLongOrderHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
            longOrdersList.onRefreshComplete();
            stopProgress();

            try {
                JSONObject result = new JSONObject(content).getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONArray retdata = result.getJSONArray("retdata");

                    for (int i = 0; i < retdata.length(); i++)
                    {
                        JSONObject jsonItem = retdata.getJSONObject(i);
                        STPassLongOrder longOrder = STPassLongOrder.decodeFromJSON(jsonItem);

                        boolean isExist = false;
                        int existIndex = 0;

                        for (int j = 0; j < arrLongOrders.size(); j++)
                        {
                            STPassLongOrder orgItem = arrLongOrders.get(j);
                            if (orgItem.uid == longOrder.uid)
                            {
                                isExist = true;
                                existIndex = j;
                                break;
                            }
                        }

                        if (isExist)
                            arrLongOrders.set(existIndex, longOrder);
                        else
                            arrLongOrders.add(0, longOrder);

                        longOrderAdapter.notifyDataSetChanged();
                    }
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(PassLongOrderMainActivity.this, szRetmsg, Gravity.CENTER);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
            longOrdersList.onRefreshComplete();
            stopProgress();
	        Global.showAdvancedToast(PassLongOrderMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private AsyncHttpResponseHandler pagedLongOrderHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			longOrdersList.onRefreshComplete();
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");

					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STPassLongOrder longOrder = STPassLongOrder.decodeFromJSON(jsonItem);

						boolean isExist = false;
						int existIndex = 0;

						for (int j = 0; j < arrLongOrders.size(); j++)
						{
							STPassLongOrder orgItem = arrLongOrders.get(j);
							if (orgItem.uid == longOrder.uid)
							{
								isExist = true;
								existIndex = j;
								break;
							}
						}

						if (isExist)
							arrLongOrders.set(existIndex, longOrder);
						else
							arrLongOrders.add(longOrder);
						longOrderAdapter.notifyDataSetChanged();
					}

					pageno = arrLongOrders.size() / Global.getPageItemCount();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderMainActivity.this, szRetmsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			longOrdersList.onRefreshComplete();
			stopProgress();
			Global.showAdvancedToast(PassLongOrderMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

    private AsyncHttpResponseHandler searchLongOrderHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
            longOrdersList.onRefreshComplete();
            Utils.mLogError("长途抢单::"+content);
            stopProgress();

            try {
                JSONObject result = new JSONObject(content).getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                arrLongOrders.clear();

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONArray retdata = result.getJSONArray("retdata");

                    for (int i = 0; i < retdata.length(); i++)
                    {
                        JSONObject jsonItem = retdata.getJSONObject(i);
                        STPassLongOrder longOrder = STPassLongOrder.decodeFromJSON(jsonItem);

                        arrLongOrders.add(longOrder);
                    }
                    pageno = arrLongOrders.size() / Global.getPageItemCount();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(PassLongOrderMainActivity.this, szRetmsg, Gravity.CENTER);
                }

                longOrderAdapter.notifyDataSetChanged();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
            longOrdersList.onRefreshComplete();
            stopProgress();
	        Global.showAdvancedToast(PassLongOrderMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

	private void acceptLongOrder(int position, int seats)
	{
        STPassLongOrder orderItem = arrLongOrders.get(position);
        startProgress();
		orderid = orderItem.uid;
        seats = seats;
		CommManager.acceptLongOrder(Global.loadUserID(getApplicationContext()),
				orderid,
				orderItem.grab_seats,
				Global.getIMEI(getApplicationContext()),
				accept_handler);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (listSeats != null && seats_layout.getVisibility() == View.VISIBLE) {
				seats_layout.setVisibility(View.GONE);
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);    //To change body of overridden methods use File | Settings | File Templates.
	}

	private void selectUser(long userid)
	{
		Intent intent = new Intent(PassLongOrderMainActivity.this, DrvEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("driverid", userid);
		PassLongOrderMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void selectLongOrder(long orderid)
	{
        Intent intent = new Intent(PassLongOrderMainActivity.this, PassLongOrderConfirmActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("orderid", orderid);
        PassLongOrderMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
	}


	private void setPassword()
	{
		dlgPwd = new ConfirmPasswordDialog(PassLongOrderMainActivity.this);
		dlgPwd.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				szPwd = dlgPwd.getPassword();
				startProgress();

				CommManager.setLongOrderPassword(Global.loadUserID(getApplicationContext()),
						orderid,
						szPwd,
						Global.getIMEI(getApplicationContext()),
						setPwd_handler);
			}
		});
		dlgPwd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgPwd.setCancelable(false);
        dlgPwd.setCanceledOnTouchOutside(false);
		dlgPwd.show();

	}


	private AsyncHttpResponseHandler accept_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
            Utils.mLogError("长途抢单结果：："+content);
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					setPassword();
                    stopProgress();
//                    showCheckOrderDialog();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					stopProgress();
					logout(szRetMsg);
				}
				else if (nRetCode == ConstData.ERR_CODE_BALNOTENOUGH)
				{
					stopProgress();
					JSONObject retdata = result.getJSONObject("retdata");
					double rembal = retdata.getDouble("rembal");
					double total_fee = retdata.getDouble("total_fee");
					showChargeDialog(rembal, total_fee);
				}
				else
				{
					stopProgress();
					Global.showAdvancedToast(PassLongOrderMainActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler setPwd_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(PassLongOrderMainActivity.this, getResources().getString(R.string.STR_SUCCESS), Gravity.CENTER);

					JSONObject retdata = result.getJSONObject("retdata");
					{
						long drv_id = retdata.getLong("drv_id");
						String drv_name = retdata.getString("drv_name");
						String drv_img = retdata.getString("drv_img");
						int drv_age = retdata.getInt("drv_age");
						int drv_gender = retdata.getInt("drv_gender");
						String car_img = retdata.getString("car_img");
						int car_style = retdata.getInt("car_style");
						String car_brand = retdata.getString("car_brand");
						String car_type = retdata.getString("car_type");
						String car_color = retdata.getString("car_color");
						String carno = retdata.getString("carno");
						String start_time = retdata.getString("start_time");
						String start_addr = retdata.getString("start_addr");
						String end_addr = retdata.getString("end_addr");
						String password = szPwd;
						String drv_career_desc = retdata.getString("drv_career_desc");
						String evgood_rate_desc = retdata.getString("evgood_rate_desc");
						String carpool_count_desc = retdata.getString("carpool_count_desc");

						Intent intent = new Intent(PassLongOrderMainActivity.this, PassLongOrderSuccessActivity.class);
						intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
						intent.putExtra("orderid", orderid);
						intent.putExtra("drv_id", drv_id);
						intent.putExtra("drv_name", drv_name);
						intent.putExtra("drv_img", drv_img);
						intent.putExtra("drv_age", drv_age);
						intent.putExtra("drv_gender", drv_gender);
						intent.putExtra("car_img", car_img);
						intent.putExtra("car_style", car_style);
						intent.putExtra("car_brand", car_brand);
						intent.putExtra("car_type", car_type);
						intent.putExtra("car_color", car_color);
						intent.putExtra("carno", carno);
						intent.putExtra("start_time", start_time);
						intent.putExtra("start_addr", start_addr);
						intent.putExtra("end_addr", end_addr);
						intent.putExtra("password", password);
						intent.putExtra("drv_career_desc", drv_career_desc);
						intent.putExtra("evgood_rate_desc", evgood_rate_desc);
						intent.putExtra("carpool_count_desc", carpool_count_desc);

						PassLongOrderMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
						startActivity(intent);
					}

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderMainActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

    private void showCheckOrderDialog(final int position, final int seats){
        String msg = "还剩余"+seats+"个座位，抢否？";
        CommonAlertDialog alertDialog = new CommonAlertDialog.Builder(PassLongOrderMainActivity.this)
                .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
                .positiveTitle("抢")
                .message(msg)
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        setPassword();
                        acceptLongOrder(position,seats);
                    }
                }).build();
        alertDialog.show();
    }

	private void showChargeDialog(double rembal, double total_fee)
	{
		final Dialog dlgBalNotEnough = new Dialog(PassLongOrderMainActivity.this);
		dlgBalNotEnough.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlgBalNotEnough.setContentView(R.layout.dlg_pass_notenough_balance);
		dlgBalNotEnough.setCancelable(false);

		ResolutionSet.instance.iterateChild(dlgBalNotEnough.findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);

		TextView txtMyBalance = (TextView)dlgBalNotEnough.findViewById(R.id.txt_my_balance);
		TextView txtOrderPrice = (TextView)dlgBalNotEnough.findViewById(R.id.txt_order_price);

		txtMyBalance.setText("" + rembal + getResources().getString(R.string.STR_BALANCE_DIAN));
		txtOrderPrice.setText("" + total_fee + getResources().getString(R.string.STR_BALANCE_DIAN));

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
				dlgBalNotEnough.dismiss();
				onClickCharge();
			}
		});

		dlgBalNotEnough.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgBalNotEnough.show();
	}


	private void onClickCharge()
	{
		Intent intent = new Intent(PassLongOrderMainActivity.this, BalanceActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra(BalanceActivity.CHARGE_TAB_NAME, BalanceActivity.CHARGE_TAB);
		PassLongOrderMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}



}


