package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
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
import com.damytech.DataClasses.STLongOrderPassInfo;
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

public class DrvLongOrderDetailActivity extends SuperActivity
{
    ImageButton btnBack = null;
    ImageButton btnRefresh = null;

	private TextView txtAddr = null;
	private TextView txtOrderNum = null;
	private TextView txtStartTime = null;
	private TextView txtState = null;
	private TextView txtSysFee = null;
	private TextView txtNoPass = null;

	private ScrollView scrollView = null;
	private LinearLayout passListView = null;

    Button btnQuXiao = null, btnZhiXing = null, btnTouSu = null;
    Button btnQieHuan = null;

	// Order detailed information
	public long orderid = 0;
	public String order_num = "";
	public int total_seat = 0;
	public double price = 0;
	public double sysinfo_fee = 0;
	public String sysinfo_fee_desc = "";
	public String start_addr = "";
	public double start_lat = 0;
	public double start_lng = 0;
	public String end_addr = "";
    public String start_city = "";
    public String end_city = "";
	public double end_lat = 0;
	public double end_lng = 0;
	public String left_days = "";
	public String valid_days = "";
	public String start_time = "";
	public String create_time = "";
	public String accept_time = "";
	public int state = ConstData.ORDER_STATE_PUBLISHED;
	public String state_desc = "";

	public ArrayList<STLongOrderPassInfo> arrPass = new ArrayList<STLongOrderPassInfo>();

	private Dialog dlgCancel = null;
    private Button mInsuranceButton;
    private TextView insuranceNumTxt;
    private TextView insuranceUserTxt;
    private TextView insuranceStartTimeTxt;
    private TextView insuranceEndTimeTxt;
    private TextView insuranceDetialTxt;
    private String insuranceNum;
    private double insuranceDetial;
    private String insuranceStartTime;
    private String insuranceEndTime;
    private String insuranceUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_longorder);

		initControls();
		initResolution();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshOrderDetInf();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent data = new Intent();
            data.putExtra("orderid", orderid);
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showInsuranceDialog() {
        final Dialog InsuranceDialog = new Dialog(DrvLongOrderDetailActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(DrvLongOrderDetailActivity.this);
        View dialogView = mInflater.inflate(R.layout.dlg_insurance_detail, null);
        ResolutionSet.instance.iterateChild(dialogView, mScrSize.x, mScrSize.y);
        InsuranceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        InsuranceDialog.setContentView(dialogView);
        Button btnOK=(Button)InsuranceDialog.findViewById(R.id.btnOk);
        InsuranceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsuranceDialog.dismiss();
            }
        });
        if(insuranceNum == null){
            Global.showAdvancedToast(DrvLongOrderDetailActivity.this,getString(R.string.INSURANCE_IN_PROCESS), Gravity.CENTER);
        }else if(state == ConstData.ORDER_STATE_CLOSED || state == ConstData.ORDER_STATE_CANCELLED ){
            Global.showAdvancedToast(DrvLongOrderDetailActivity.this,getString(R.string.INSURANCE_CLOSED), Gravity.CENTER);
        }else{
            insuranceNumTxt = (TextView)InsuranceDialog.findViewById(R.id.insurance_num);
            insuranceNumTxt.setText("保单单号："+insuranceNum);
            insuranceUserTxt = (TextView)InsuranceDialog.findViewById(R.id.insurance_user);
            insuranceUserTxt.setText("被保人姓名："+ insuranceUser);
            insuranceDetialTxt = (TextView)InsuranceDialog.findViewById(R.id.insurance_detail);
            insuranceDetialTxt.setText(""+insuranceDetial);
            insuranceStartTimeTxt = (TextView)InsuranceDialog.findViewById(R.id.insurance_start_time);
            insuranceStartTimeTxt.setText("保单生效时间: "+insuranceStartTime);
            insuranceEndTimeTxt = (TextView)InsuranceDialog.findViewById(R.id.insurance_end_time);
            insuranceEndTimeTxt.setText("保单中止时间: "+insuranceEndTime);
            InsuranceDialog.show();
        }
    }

	private void initControls()
	{
        mInsuranceButton = (Button)findViewById(R.id.insurance_detail);
        mInsuranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInsuranceDialog();
            }
        });
		orderid = getIntent().getLongExtra("orderid", 0);

        btnBack = (ImageButton)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(onClickListener);

        btnQuXiao = (Button) findViewById(R.id.btnQuXiao);
        btnQuXiao.setOnClickListener(onClickListener);

        btnZhiXing = (Button) findViewById(R.id.btnZhiXing);
        btnZhiXing.setOnClickListener(onClickListener);

        btnTouSu = (Button) findViewById(R.id.btnTouSu);
        btnTouSu.setOnClickListener(onClickListener);

        btnQieHuan = (Button) findViewById(R.id.btnQieHuan);
        btnQieHuan.setOnClickListener(onClickListener);

		btnRefresh = (ImageButton) findViewById(R.id.btn_refresh);
		btnRefresh.setOnClickListener(onClickListener);

		txtAddr = (TextView)findViewById(R.id.lblAddr);
		txtOrderNum = (TextView)findViewById(R.id.lblOrderNo);
		txtStartTime = (TextView)findViewById(R.id.lblStartTime);
		txtState = (TextView)findViewById(R.id.lblState);
		txtSysFee = (TextView)findViewById(R.id.lblSysFee);

		txtNoPass = (TextView)findViewById(R.id.txt_no_pass);
		txtNoPass.setVisibility(View.INVISIBLE);

		scrollView = (ScrollView)findViewById(R.id.scrollView);
		passListView = (LinearLayout)findViewById(R.id.list_layout);
	}


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_back:
                    Intent data = new Intent();
                    data.putExtra("orderid", orderid);
                    setResult(RESULT_OK, data);
                    finishWithAnimation();
                    break;
                case R.id.btnQuXiao:
	                cancelOrder();
                    break;
                case R.id.btnZhiXing:
	                executeOrder();
                    break;
                case R.id.btnTouSu:
	                declareOrder();
                    break;
                case R.id.btnQieHuan:
	                changeToExecuteState();
                    break;
                case R.id.btn_refresh:
	                refreshOrderDetInf();
                    break;
            }
        }
    };


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


	private void refreshOrderDetInf()
	{
		if (orderid == 0)
			return;

		startProgress();
		CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
				orderid,
				ConstData.ORDER_TYPE_LONG,
				Global.getIMEI(getApplicationContext()),
				order_handler);
	}


	private AsyncHttpResponseHandler order_handler = new AsyncHttpResponseHandler()
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
					JSONObject retdata = result.getJSONObject("retdata");

					order_num = retdata.getString("order_num");

					JSONArray passList = retdata.getJSONArray("pass_list");

					arrPass.clear();
					for (int i = 0; i < passList.length(); i++)
					{
						arrPass.add(STLongOrderPassInfo.decodeFromJSON(passList.getJSONObject(i)));
					}

					if (arrPass.size() == 0)
						txtNoPass.setVisibility(View.VISIBLE);
					else
						txtNoPass.setVisibility(View.INVISIBLE);

					total_seat = retdata.getInt("total_seat");
					price = retdata.getDouble("price");
					sysinfo_fee = retdata.getDouble("sysinfo_fee");
					sysinfo_fee_desc = retdata.getString("sysinfo_fee_desc");
					start_addr = retdata.getString("start_addr");
					start_lat = retdata.getDouble("start_lat");
					start_lng = retdata.getDouble("start_lng");
					end_addr = retdata.getString("end_addr");
                    start_city = retdata.getString("start_city");
                    end_city = retdata.getString("end_city");
					end_lat = retdata.getDouble("end_lat");
					end_lng = retdata.getDouble("end_lng");
					left_days = retdata.getString("left_days");
					valid_days = retdata.getString("valid_days");

					start_time = retdata.getString("start_time");
					create_time = retdata.getString("create_time");
					accept_time = retdata.getString("accept_time");
					state = retdata.getInt("state");
					state_desc = retdata.getString("state_desc");
                    if(passList.length() != 0){
                        insuranceNum = retdata.getString("appl_no");
                        insuranceDetial = retdata.getDouble("total_amount");
                        insuranceEndTime = retdata.getString("insexpr_date");
                        insuranceStartTime = retdata.getString("effect_time");
                        insuranceUser = retdata.getString("isd_name");
                    }

					updateUI();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvLongOrderDetailActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvLongOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void updateUI()
	{
		txtAddr.setText(start_city + ":" + start_addr + getResources().getString(R.string.addr_separator) + end_city + ":" + end_addr);

		txtOrderNum.setText(getResources().getString(R.string.STR_LONGDISTANCEDETAIL_DINGDANBIANHAO) + " : " + order_num);
		txtStartTime.setText(getResources().getString(R.string.STR_LONGDISTANCEDETAIL_YUYUESHIJIAN) + " : " + start_time);

		String szState = "";
		szState = "" + price + getResources().getString(R.string.STR_LONGDISTANCE_DIANMEIZUO);
		szState += " " + getResources().getString(R.string.STR_LONGDISTANCE_GONG) + total_seat + getResources().getString(R.string.STR_LONGDISTANCE_ZUO) + "\n";
		szState += state_desc;
		txtState.setText(szState);
		txtSysFee.setText(sysinfo_fee_desc);

		addPassInfoToList();

		// Show and hide main operation buttons
		switch (state)
		{
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
				btnQuXiao.setVisibility(View.VISIBLE);
				btnZhiXing.setVisibility(View.VISIBLE);
				btnQieHuan.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_STARTED:
			case ConstData.ORDER_STATE_DRV_ARRIVED:
			case ConstData.ORDER_STATE_PASS_GETON:
				btnQuXiao.setVisibility(View.INVISIBLE);
				btnZhiXing.setVisibility(View.INVISIBLE);
				btnQieHuan.setVisibility(View.VISIBLE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_FINISHED:
			case ConstData.ORDER_STATE_PAYED:
			case ConstData.ORDER_STATE_EVALUATED:
			case ConstData.ORDER_STATE_CLOSED:
			default:
				btnQuXiao.setVisibility(View.GONE);
				btnZhiXing.setVisibility(View.GONE);
				btnQieHuan.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
		}
	}


	private void onEvalPassenger(long passid)
	{
        Intent intent = new Intent(DrvLongOrderDetailActivity.this, EvaluateActivity.class);

		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

        intent.putExtra("driver", Global.loadUserID(getApplicationContext()));
        intent.putExtra("passenger", passid);
        intent.putExtra("order_type", (int)ConstData.ORDER_TYPE_LONG);
        intent.putExtra("orderid", orderid);
        intent.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);                 // Who evaluates? driver or passenger

        intent.putExtra("view_mode", false);                                         // Is view mode or send mode
        intent.putExtra("level", ConstData.EVALUATE_GOOD);                          // Preset level
        intent.putExtra("msg", "");                                                   // Preset content

        DrvLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
	}


	private void onViewEvalInfo(long passid)
	{
		STLongOrderPassInfo passInfo = null;
		for (int i = 0; i < arrPass.size(); i++)
		{
			STLongOrderPassInfo tmpInfo = arrPass.get(i);
			if (tmpInfo.uid == passid)
			{
				passInfo = tmpInfo;
				break;
			}
		}

		if (passInfo == null)
			return;

		Intent intent = new Intent(DrvLongOrderDetailActivity.this, EvaluateActivity.class);

		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intent.putExtra("driver", Global.loadUserID(getApplicationContext()));
		intent.putExtra("passenger", passid);
		intent.putExtra("order_type", (int)ConstData.ORDER_TYPE_LONG);
		intent.putExtra("orderid", orderid);
		intent.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);                // Who evaluates? driver or passenger

		intent.putExtra("view_mode", true);                                         // Is view mode or send mode
		intent.putExtra("level", passInfo.evaluated);                               // Preset level
		intent.putExtra("msg", passInfo.eval_content);                              // Preset content

		DrvLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void onCallPhone(long passid)
	{
		STLongOrderPassInfo passInfo = null;
		for (int i = 0; i < arrPass.size(); i++)
		{
			STLongOrderPassInfo tmpInfo = arrPass.get(i);
			if (tmpInfo.uid == passid)
			{
				passInfo = tmpInfo;
				break;
			}
		}

		if (passInfo == null)
			return;

		Global.callPhone(passInfo.phone, DrvLongOrderDetailActivity.this);
	}


	private void onSelectPassenger(long passid)
	{
		Intent intent = new Intent(DrvLongOrderDetailActivity.this, PassEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("passid", passid);
		DrvLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void cancelOrder()
	{
		dlgCancel = new Dialog(DrvLongOrderDetailActivity.this);
		dlgCancel.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlgCancel.setContentView(R.layout.dlg_pass_longorderfinish);
		dlgCancel.setCancelable(false);

		ResolutionSet.instance.iterateChild(dlgCancel.findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);

		if (arrPass.size() == 0) {
			TextView txtMsg = (TextView) dlgCancel.findViewById(R.id.lblTitle);
			txtMsg.setText("还没有拼友抢座呢，不再等等吗？");
		}

		Button btnOK = (Button)dlgCancel.findViewById(R.id.btnOk);
		Button btnCancel = (Button)dlgCancel.findViewById(R.id.btnCancel);

		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickCancel();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dlgCancel.dismiss();
			}
		});

		dlgCancel.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgCancel.show();
	}

	private void executeOrder()
	{
//		Intent intent = new Intent(DrvLongOrderDetailActivity.this, LongDistanceRunActivity.class);
//		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//		intent.putExtra("orderid", orderid);
//		intent.putExtra("ordertype", ConstData.ORDER_TYPE_LONG);
//		DrvLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//		startActivity(intent);

        // check passenger count
        if (arrPass.size() > 0)
        {
            changeToExecuteState();
        }
        else
        {
            // show no passenger alert
            Global.showAdvancedToast(DrvLongOrderDetailActivity.this, getString(R.string.STR_DRVLONG_NO_PASSENGER), Gravity.CENTER);
        }

	}

	private void declareOrder()
	{
		Global.callPhone(Global.getServiceCall(), DrvLongOrderDetailActivity.this);
	}

	private void changeToExecuteState()
	{
		Intent intent = new Intent(DrvLongOrderDetailActivity.this, DrvLongOrderExeActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("orderid", orderid);
		intent.putExtra("ordertype", ConstData.ORDER_TYPE_LONG);
		DrvLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void onClickCancel()
	{
		startProgress();
		CommManager.cancelLongOrder(Global.loadUserID(getApplicationContext()), orderid, Global.getIMEI(getApplicationContext()), cancel_handler);
	}


	private AsyncHttpResponseHandler cancel_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(DrvLongOrderDetailActivity.this, getResources().getString(R.string.STR_QUXIAODINGDANCHENGGONG), Gravity.CENTER);

                    Intent data = new Intent();
                    data.putExtra("orderid", orderid);
                    setResult(RESULT_OK, data);

					finish();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvLongOrderDetailActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvLongOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void addPassInfoToList()
	{
		passListView.removeAllViews();

		for (int i = 0; i < arrPass.size(); i++)
		{
			View v = getView(i);
			passListView.addView(v);
		}
	}


	public View getView(int position)
	{
		STLongOrderPassInfo passItem = arrPass.get(position);

		int nWidth = 460, nHeight = 160, nYMargin = 10;
		RelativeLayout itemLayout = new RelativeLayout(passListView.getContext());
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight + nYMargin * 2);
		itemLayout.setLayoutParams(layoutParams);

		RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_driver_longdistancedetailitem, null);
		RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
		RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
		item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		itemView.setLayoutParams(item_layoutParams);
		itemLayout.addView(itemView);

		ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

		// Passenger information
		SmartImageView imgPhoto = (SmartImageView)itemLayout.findViewById(R.id.viewPhoto);;
		imgPhoto.isCircular = true;
		imgPhoto.setImageUrl(passItem.img, R.drawable.icon_appprice_over);

		ImageView imgSex = (ImageView)itemLayout.findViewById(R.id.imgSex);
		TextView txtAge = (TextView)itemLayout.findViewById(R.id.lblAge);
		TextView txtName = (TextView)itemLayout.findViewById(R.id.lblName);
		ImageView imgVerified = (ImageView)itemLayout.findViewById(R.id.imgSecurity);
		TextView txtVerified = (TextView)itemLayout.findViewById(R.id.lblSecurity);
		TextView txtSeatCount = (TextView)itemLayout.findViewById(R.id.lblSeatCount);
		TextView txtSeatCount2 = (TextView)itemLayout.findViewById(R.id.lblSeatCount2);
		TextView txtEvGood = (TextView)itemLayout.findViewById(R.id.lblRatio);
		TextView txtCarpoolCnt = (TextView)itemLayout.findViewById(R.id.lblCarpoolCount);
		ImageView imgState = (ImageView)itemLayout.findViewById(R.id.imgState);
		ImageButton btnState = (ImageButton)itemLayout.findViewById(R.id.btnState);

		if (passItem.gender == ConstData.GENDER_MALE) {
			imgSex.setImageResource(R.drawable.bk_manmark);
			txtAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
		} else {
			imgSex.setImageResource(R.drawable.bk_womanmark);
			txtAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		}

		txtAge.setText("" + passItem.age);
		txtName.setText(passItem.name);
		if (passItem.verified == 1)
			imgVerified.setImageResource(R.drawable.bk_person_verified);
		else
			imgVerified.setImageResource(R.drawable.bk_person_notverified);
		txtVerified.setText(passItem.verified_desc);
		txtEvGood.setText(getResources().getString(R.string.STR_HAOPINGLV) + " " + passItem.evgood_rate_desc);
		txtCarpoolCnt.setText(getResources().getString(R.string.STR_PINCHECISHU) + " " + passItem.carpool_count_desc);

		txtSeatCount.setText(passItem.seat_count_desc);
		txtSeatCount2.setText(passItem.seat_count_desc);

		final long passid = passItem.uid;

		ImageButton btnPhoto = (ImageButton)itemLayout.findViewById(R.id.btnPhoto);
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectPassenger(passid);
			}
		});


		btnState.setImageResource(R.drawable.btn_frame);

		if (state == ConstData.ORDER_STATE_CLOSED)
		{
			imgState.setVisibility(View.INVISIBLE);
			btnState.setVisibility(View.INVISIBLE);
			txtSeatCount.setVisibility(View.INVISIBLE);
			txtSeatCount2.setVisibility(View.VISIBLE);
		}
		else
		{
			if (passItem.state == ConstData.PASS_STATE_PAYED)
			{
				imgState.setVisibility(View.VISIBLE);
				btnState.setVisibility(View.VISIBLE);
				btnState.setBackgroundResource(R.drawable.bk_empty);
				imgState.setImageResource(R.drawable.bk_passevalsel);
				btnState.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onEvalPassenger(passid);
					}
				});
				txtSeatCount.setVisibility(View.VISIBLE);
				txtSeatCount2.setVisibility(View.INVISIBLE);
			}
			else if (passItem.state == ConstData.PASS_STATE_EVALUATED)
			{
				imgState.setVisibility(View.VISIBLE);
				btnState.setVisibility(View.VISIBLE);
				btnState.setBackgroundResource(R.drawable.bk_empty);
				txtSeatCount.setVisibility(View.VISIBLE);
				txtSeatCount2.setVisibility(View.INVISIBLE);
				if (passItem.evaluated == ConstData.EVALUATE_GOOD)
				{
					imgState.setImageResource(R.drawable.btn_goodeval);
				}
				else if (passItem.evaluated == ConstData.EVALUATE_NORMAL)
				{
					imgState.setImageResource(R.drawable.btn_normaleval);
				}
				else if (passItem.evaluated == ConstData.EVALUATE_BAD)
				{
					imgState.setImageResource(R.drawable.btn_badeval);
				}
				btnState.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onViewEvalInfo(passid);
					}
				});
			}
			else if (passItem.state == ConstData.PASS_STATE_GIVEUP)
			{
				imgState.setVisibility(View.INVISIBLE);
				btnState.setVisibility(View.INVISIBLE);
				txtSeatCount.setVisibility(View.INVISIBLE);
				txtSeatCount2.setVisibility(View.VISIBLE);
			}
			else
			{
				imgState.setVisibility(View.VISIBLE);
				btnState.setVisibility(View.VISIBLE);
				btnState.setBackgroundResource(R.drawable.bk_empty);
				imgState.setImageResource(R.drawable.bk_passphone);
				btnState.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onCallPhone(passid);
					}
				});
				txtSeatCount.setVisibility(View.VISIBLE);
				txtSeatCount2.setVisibility(View.INVISIBLE);
			}
		}

		return itemLayout;
	}
}
