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
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

public class PassLongOrderDetailActivity extends SuperActivity
{
	private final int STATE_COLOR = Color.rgb(245, 175, 68);
	private final int REQCODE_EVALUATE = 1;

	// Driver information
	private long drvID = 0;
	private String drvImg = "";
	private String drvName = "";
	private int drvGender = 0;
	private int drvAge = 0;
	private String drvPhone = "";
	private String carImg = "";
	private int drvCareer = 0;
	private String drvCareerDesc = "";
	private double evGoodRate = 0;
	private String evGoodRateDesc = "";
	private int carpoolCnt = 0;
	private String carpoolCntDesc = "";
	private RelativeLayout carNoLayout = null;
	private String carNo = "";
	private String carBrand = "";
	private String carStyle = "";
	private int carType = 0;
	private String carColor = "";

	// Order information
	private long orderID = 0;
	private String orderNum = "";
	private double price = 0;
	private String startAddr = "";
	private double startLat = 0;
	private double startLng = 0;
	private String endAddr = "";
	private double endLat = 0;
	private double endLng = 0;
	private String leftDays = "";
	private String days = "";
	private String password = "";
	private String startTime = "";
	private String createTime = "";
	private String acceptTime = "";
	private String startSrvTime = "";
	private int state = 0;
	private String stateDesc = "";
	private String cancelledDesc = "";
	private int evaluated = 0;
	private String evalContent = "";
	private String evalDesc = "";


	// UI controls
	private ImageButton btnBack = null;
	private ImageButton btnRefresh = null;

	private SmartImageView imgPhoto = null;
	private ImageButton btnPhoto = null;
	private ImageView imgGender = null;
	private TextView txtAge = null;
	private TextView txtName = null;
	private SmartImageView imgCar = null;
    private Button btnCar = null;
	private TextView txtDrvCareer = null;
	private TextView txtEvGood = null;
	private TextView txtCarpoolCnt = null;
	private ImageView imgCarType = null;
	private ImageView imgCarBrand = null;
	private TextView txtCarBrand = null;
	private TextView txtCarStyle = null;
	private TextView txtCarColor = null;
	private TextView txtOrderNo = null;
	private TextView txtState = null, txtState2 = null;
	private TextView txtStartAddr = null;
    private TextView txtEndAddr = null;
	private TextView txtTime1 = null;
	private TextView txtTime2 = null;
	private TextView txtCarNo = null;
	private TextView txtPassword = null;
	private RelativeLayout operLayout1 = null;
	private RelativeLayout operLayout2 = null;
	private ImageButton btnOper1 = null;
	private ImageView imgOper1 = null;
	private ImageButton btnOper2 = null;
	private ImageView imgOper2 = null;
	private Button btnCancelOrder = null;
	private Button btnTouSu = null;
	private TextView txtCancelBalance = null;

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
	protected void onCreate(Bundle savedInstanceState)
	 {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pass_long_detail);

		initControls();//控件示例化
		initResolution();//适配屏幕
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		refreshInformation();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent data = new Intent();
            data.putExtra("orderid", orderID);
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showInsuranceDialog() {
        final Dialog InsuranceDialog = new Dialog(PassLongOrderDetailActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(PassLongOrderDetailActivity.this);
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
        if(insuranceNum == null){
            Global.showAdvancedToast(PassLongOrderDetailActivity.this,getString(R.string.NO_INSURARANCE), Gravity.CENTER);
        }else if(state == ConstData.ORDER_STATE_CLOSED ||state == ConstData.ORDER_STATE_CANCELLED ){
            Global.showAdvancedToast(PassLongOrderDetailActivity.this,getString(R.string.INSURANCE_CLOSED), Gravity.CENTER);
        }else{
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
		orderID = getIntent().getLongExtra("orderid", 0);

		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		btnRefresh = (ImageButton)findViewById(R.id.btn_refresh);
		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshInformation();
			}
		});

		imgPhoto = (SmartImageView)findViewById(R.id.viewPhoto);
		imgPhoto.isCircular = true;
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});

		btnPhoto = (ImageButton)findViewById(R.id.btnPhoto);
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectDriver();
			}
		});

		imgGender = (ImageView)findViewById(R.id.imgSex);
		txtAge = (TextView)findViewById(R.id.lblAge);
		txtName = (TextView)findViewById(R.id.lblName);

		imgCar = (SmartImageView)findViewById(R.id.viewCar);
		imgCar.isCircular = true;
		imgCar.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});

        btnCar = (Button) findViewById(R.id.btnCar);
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassLongOrderDetailActivity.this, DisplayCarImgActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("img_url", carImg);
                PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

		txtDrvCareer = (TextView)findViewById(R.id.lblDriverYears);
		txtEvGood = (TextView)findViewById(R.id.lblEval);
		txtCarpoolCnt = (TextView)findViewById(R.id.lblHistoryCount);
		imgCarType = (ImageView)findViewById(R.id.imgCarType);
		imgCarBrand = (ImageView)findViewById(R.id.imgBrand);
		txtCarBrand = (TextView)findViewById(R.id.txtBrand);
		txtCarStyle = (TextView)findViewById(R.id.lblBrandName);
		txtCarColor = (TextView)findViewById(R.id.lblColor);
		txtOrderNo = (TextView)findViewById(R.id.lblCardno);
		txtState = (TextView)findViewById(R.id.lblStatus);
		txtState2 = (TextView)findViewById(R.id.lblStatus2);
        txtStartAddr = (TextView)findViewById(R.id.lblStartPath);
        txtEndAddr = (TextView)findViewById(R.id.lblEndPath);
		txtTime2 = (TextView)findViewById(R.id.lblRunTime);
		carNoLayout = (RelativeLayout)findViewById(R.id.carno_layout);
		txtCarNo = (TextView)findViewById(R.id.txtCarNo);
		txtPassword = (TextView)findViewById(R.id.txtPassword);
		operLayout1 = (RelativeLayout)findViewById(R.id.operation_layout);
		operLayout2 = (RelativeLayout)findViewById(R.id.operation_layout2);
		btnOper1 = (ImageButton)findViewById(R.id.btn_oper1);
		imgOper1 = (ImageView)findViewById(R.id.imgButton1);
		btnOper2 = (ImageButton)findViewById(R.id.btn_oper2);
		imgOper2 = (ImageView)findViewById(R.id.imgButton2);
		btnCancelOrder = (Button)findViewById(R.id.btn_CancelTicket);
		btnTouSu = (Button)findViewById(R.id.btn_TouSu);
		txtCancelBalance = (TextView)findViewById(R.id.lblCarpool);

		btnOper1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onOperate1();
			}
		});

		btnOper2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onOperate2();
			}
		});

		btnCancelOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCancelOrder();
			}
		});

		btnTouSu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onTouSu();
			}
		});
	}

	private void onClickBack()
	{
        Intent data = new Intent();
        data.putExtra("orderid", orderID);
        setResult(RESULT_OK, data);
		finishWithAnimation();
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

		if (resultCode == RESULT_OK)
		{
			if (requestCode == REQCODE_EVALUATE)
			{
				refreshInformation();
			}
		}
	}



	private void refreshInformation()
	{
		if (orderID <= 0)
			return;

		startProgress();
		CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
				orderID,
				ConstData.ORDER_TYPE_LONG,
				Global.getIMEI(getApplicationContext()),
				detailinfo_handler);
	}



	private void onSelectDriver()
	{
		Intent intent = new Intent(PassLongOrderDetailActivity.this, DrvEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("driverid", drvID);
		PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private AsyncHttpResponseHandler detailinfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
            Utils.mLogError("长途详情："+content);
			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					orderNum = retdata.getString("order_num");
					price = retdata.getDouble("price");
					startAddr = retdata.getString("start_city") + ":" +  retdata.getString("start_addr");
					startLat = retdata.getDouble("start_lat");
					startLng = retdata.getDouble("start_lng");
					endAddr = retdata.getString("end_city") + ":" +  retdata.getString("end_addr");
					endLat = retdata.getDouble("end_lat");
					endLng = retdata.getDouble("end_lng");
					leftDays = retdata.getString("leftdays");
					days = retdata.getString("days");
					password = retdata.getString("password");
					startTime = retdata.getString("start_time");
					createTime = retdata.getString("create_time");
					acceptTime = retdata.getString("accept_time");
					startSrvTime = retdata.getString("startsrv_time");
					state = retdata.getInt("state");
					stateDesc = retdata.getString("state_desc");
					cancelledDesc = retdata.getString("cancelled_balance_desc");
					evaluated = retdata.getInt("evaluated");
					evalContent = retdata.getString("eval_content");
					evalDesc = retdata.getString("evaluated_desc");

					JSONObject drvInfo = retdata.getJSONObject("driver_info");
					drvID = drvInfo.getLong("uid");
					drvImg = drvInfo.getString("img");
					drvName = drvInfo.getString("name");
					drvGender = drvInfo.getInt("gender");
					drvAge = drvInfo.getInt("age");
					drvPhone = drvInfo.getString("phone");
					carImg = drvInfo.getString("carimg");
					drvCareer = drvInfo.getInt("drv_career");
					drvCareerDesc = drvInfo.getString("drv_career_desc");
					evGoodRate = drvInfo.getDouble("evgood_rate");
					evGoodRateDesc = drvInfo.getString("evgood_rate_desc");
					carpoolCnt = drvInfo.getInt("carpool_count");
					carpoolCntDesc = drvInfo.getString("carpool_count_desc");
					carNo = drvInfo.getString("carno");
					carBrand = drvInfo.getString("brand");
					carStyle = drvInfo.getString("style");
					carType = drvInfo.getInt("type");
					carColor = drvInfo.getString("color");
                    insuranceNum = retdata.getString("appl_no");
                    insuranceDetial = retdata.getDouble("total_amount");
                    insuranceEndTime = retdata.getString("insexpr_date");
                    insuranceStartTime = retdata.getString("effect_time");
                    insuranceUser = retdata.getString("isd_name");

					updateUI();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderDetailActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void updateUI()
	{
		// Control state controlling
		switch (state)
		{
			case ConstData.ORDER_STATE_DRV_ACCEPTED:
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
				txtState2.setVisibility(View.VISIBLE);
				txtState2.setTextColor(STATE_COLOR);

				txtState.setVisibility(View.INVISIBLE);
				carNoLayout.setVisibility(View.VISIBLE);

				txtCancelBalance.setVisibility(View.INVISIBLE);
				btnOper1.setVisibility(View.VISIBLE);
				btnOper2.setVisibility(View.VISIBLE);

				imgOper1.setImageResource(R.drawable.bk_driverphone);
				imgOper2.setImageResource(R.drawable.bk_driverpos);

				btnCancelOrder.setVisibility(View.VISIBLE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_STARTED:
			case ConstData.ORDER_STATE_DRV_ARRIVED:
			case ConstData.ORDER_STATE_PASS_GETON:
                txtState2.setVisibility(View.VISIBLE);
                txtState2.setTextColor(STATE_COLOR);

                txtState.setVisibility(View.INVISIBLE);
                carNoLayout.setVisibility(View.VISIBLE);

                txtCancelBalance.setVisibility(View.INVISIBLE);
                btnOper1.setVisibility(View.VISIBLE);
                btnOper2.setVisibility(View.VISIBLE);

				imgOper1.setImageResource(R.drawable.bk_driverphone);
				imgOper2.setImageResource(R.drawable.bk_pay_disable);

                btnCancelOrder.setVisibility(View.VISIBLE);
                btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_FINISHED:
				txtState2.setVisibility(View.INVISIBLE);

				txtState.setVisibility(View.VISIBLE);
				txtState.setTextColor(STATE_COLOR);
				carNoLayout.setVisibility(View.INVISIBLE);

				txtCancelBalance.setVisibility(View.INVISIBLE);
				btnOper1.setVisibility(View.VISIBLE);
				btnOper2.setVisibility(View.VISIBLE);

				imgOper1.setImageResource(R.drawable.bk_driverphone);
				imgOper2.setImageResource(R.drawable.bk_pay);

				btnCancelOrder.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_PAYED:
				txtState2.setVisibility(View.INVISIBLE);

				txtState.setVisibility(View.VISIBLE);
				txtState.setTextColor(STATE_COLOR);
				carNoLayout.setVisibility(View.INVISIBLE);

				txtCancelBalance.setVisibility(View.INVISIBLE);
				btnOper1.setVisibility(View.INVISIBLE);
				btnOper2.setVisibility(View.VISIBLE);

				imgOper1.setImageResource(R.drawable.bk_completeorder);
				imgOper2.setImageResource(R.drawable.bk_driverevalsel);

				btnCancelOrder.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_EVALUATED:
				txtState2.setVisibility(View.INVISIBLE);

				txtState.setVisibility(View.VISIBLE);
				txtState.setTextColor(STATE_COLOR);
				carNoLayout.setVisibility(View.INVISIBLE);

				txtCancelBalance.setVisibility(View.INVISIBLE);
				btnOper1.setVisibility(View.INVISIBLE);
				btnOper2.setVisibility(View.VISIBLE);

				imgOper1.setImageResource(R.drawable.bk_completeorder);
				{
					switch (evaluated)
					{
						case ConstData.EVALUATE_GOOD:
							imgOper2.setImageResource(R.drawable.btn_goodeval);
							break;
						case ConstData.EVALUATE_NORMAL:
							imgOper2.setImageResource(R.drawable.btn_normaleval);
							break;
						default:
							imgOper2.setImageResource(R.drawable.btn_badeval);
							break;
					}
				}

				btnCancelOrder.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_CANCELLED:
				txtState2.setVisibility(View.INVISIBLE);

				txtState.setVisibility(View.VISIBLE);
				txtState.setTextColor(STATE_COLOR);
				carNoLayout.setVisibility(View.INVISIBLE);

				txtCancelBalance.setVisibility(View.VISIBLE);
				btnOper1.setVisibility(View.INVISIBLE);
				btnOper2.setVisibility(View.INVISIBLE);

				imgOper1.setVisibility(View.INVISIBLE);
				imgOper2.setVisibility(View.INVISIBLE);

				btnCancelOrder.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
			case ConstData.ORDER_STATE_CLOSED:
			default:
				txtState2.setVisibility(View.INVISIBLE);

				txtState.setVisibility(View.VISIBLE);
				txtState.setTextColor(Color.DKGRAY);
				carNoLayout.setVisibility(View.INVISIBLE);

				txtCancelBalance.setVisibility(View.INVISIBLE);
				btnOper1.setVisibility(View.INVISIBLE);
				btnOper2.setVisibility(View.VISIBLE);

				imgOper1.setImageResource(R.drawable.bk_exitorder);
				{
					switch (evaluated)
					{
						case ConstData.EVALUATE_GOOD:
							imgOper2.setImageResource(R.drawable.btn_goodeval);
							break;
						case ConstData.EVALUATE_NORMAL:
							imgOper2.setImageResource(R.drawable.btn_normaleval);
							break;
						case ConstData.EVALUATE_BAD:
							imgOper2.setImageResource(R.drawable.btn_badeval);
							break;
						default:
							imgOper2.setImageResource(R.drawable.bk_passevalsel);
							break;
					}
				}
                imgOper2.setImageResource(R.drawable.bk_driverevalsel);

				btnCancelOrder.setVisibility(View.GONE);
				btnTouSu.setVisibility(View.VISIBLE);
				break;
		}


		// Control setting
		imgPhoto.setImageUrl(drvImg, R.drawable.icon_appprice_over);
		if (drvGender == ConstData.GENDER_MALE) {
			imgGender.setImageResource(R.drawable.bk_manmark);
			txtAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
		} else {
			imgGender.setImageResource(R.drawable.bk_womanmark);
			txtAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		}
		txtAge.setText("" + drvAge);
		txtName.setText(drvName);
		imgCar.setImageUrl(carImg, R.drawable.icon_appprice_over);
		txtDrvCareer.setText(drvCareerDesc);
		txtEvGood.setText(getResources().getString(R.string.STR_HAOPINGLV) + evGoodRateDesc);
		txtCarpoolCnt.setText(getResources().getString(R.string.STR_PASSNORMALCONFIRM_FUWUCISHU) + carpoolCntDesc);
		switch (carType)
		{
			case ConstData.CARSTYLE_JINGJIXING:
				imgCarType.setImageResource(R.drawable.second_car_green);
				break;
			case ConstData.CARSTYLE_SHUSHIXING:
				imgCarType.setImageResource(R.drawable.safecar_sel);
				break;
			case ConstData.CARSTYLE_HAOHUAXING:
				imgCarType.setImageResource(R.drawable.luxurycar_sel);
				break;
			case ConstData.CARSTYLE_SHANGWUXING:
			default:
				imgCarType.setImageResource(R.drawable.third_car_green);
				break;
		}

		int brandID = Global.getBrandImgFromName(carBrand);
		if (brandID > 0)
		{
			imgCarBrand.setImageResource(brandID);
			imgCarBrand.setVisibility(View.VISIBLE);
			txtCarBrand.setVisibility(View.INVISIBLE);
		}
		else
		{
			imgCarBrand.setVisibility(View.INVISIBLE);
			txtCarBrand.setVisibility(View.VISIBLE);
			txtCarBrand.setText(carBrand);
		}
		txtCarStyle.setText(carStyle);
		txtCarColor.setText(carColor);
		txtOrderNo.setText(getResources().getString(R.string.STR_LONGDISTANCEDETAIL_DINGDANBIANHAO) + " : " + orderNum);
		txtState.setText(stateDesc);
		txtState2.setText(stateDesc);
        txtStartAddr.setText(startAddr + getResources().getString(R.string.addr_separator) + endAddr);
//        txtEndAddr.setText(endAddr);

		if (state == ConstData.ORDER_STATE_DRV_ACCEPTED ||
			state == ConstData.ORDER_STATE_PUBLISHED ||
			state == ConstData.ORDER_STATE_GRABBED)
		{
			txtTime2.setText(getResources().getString(R.string.STR_YUYUESHIJIAN) + " " + startTime);
		}
		else
		{
			txtTime2.setText(getResources().getString(R.string.STR_YUYUESHIJIAN) + " " + startTime);
		}

		String szCarNo = "";
		try {
			szCarNo = carNo.substring(0, 1) + "***" + carNo.substring(carNo.length() - 3);
		} catch (Exception ex) {
			ex.printStackTrace();
			szCarNo = carNo;
		}
		txtCarNo.setText(getResources().getString(R.string.STR_CHEPAIHAO) + " " + szCarNo);
		txtPassword.setText(getResources().getString(R.string.STR_DIANZICHEPIAO) + " " + password);
		txtCancelBalance.setText(cancelledDesc);
	}


	private void onOperate1()
	{
		Global.callPhone(drvPhone, PassLongOrderDetailActivity.this);
	}

	private void onOperate2()
	{
		// Control state controling
		switch (state)
		{
			case ConstData.ORDER_STATE_DRV_ACCEPTED:
			case ConstData.ORDER_STATE_PUBLISHED:
			case ConstData.ORDER_STATE_GRABBED:
				showDriverPosRealTime();                            // driver position
				break;
            case ConstData.ORDER_STATE_STARTED:
            case ConstData.ORDER_STATE_DRV_ARRIVED:
            case ConstData.ORDER_STATE_PASS_GETON:
                // disable operate
                break;
			case ConstData.ORDER_STATE_FINISHED:
				moveToPayOrderActivity();                          // pay
				break;
			case ConstData.ORDER_STATE_PAYED:
				evaluateDriver();                                  // evaluate
				break;
			case ConstData.ORDER_STATE_EVALUATED:
				showEvaluation();                                   // show evaluation info
				break;
			case ConstData.ORDER_STATE_CANCELLED:
				break;
			case ConstData.ORDER_STATE_CLOSED:
			default:
				btnOper2.setVisibility(View.VISIBLE);
				switch (evaluated)
				{
					case ConstData.EVALUATE_GOOD:
					case ConstData.EVALUATE_NORMAL:
					case ConstData.EVALUATE_BAD:
						showEvaluation();                                   // show evaluation info
						break;
					default:
						evaluateDriver();                                  // evaluate
						break;
				}
				break;
		}
	}

	private void onCancelOrder()
	{
        // go to cancel order activity
        Intent intent = new Intent(PassLongOrderDetailActivity.this, PassCancelLongOrderActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

        intent.putExtra("orderid", orderID);

        PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);

//		startProgress();
//		CommManager.cancelLongOrder(Global.loadUserID(getApplicationContext()), orderID, Global.getIMEI(getApplicationContext()), cancel_longorder_handler);
	}

	private void onTouSu()
	{
		Global.callPhone(Global.getServiceCall(), PassLongOrderDetailActivity.this);
	}


	private AsyncHttpResponseHandler cancel_longorder_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(PassLongOrderDetailActivity.this, getResources().getString(R.string.STR_QUXIAOCHENGGONG), Gravity.CENTER);

                    Intent data = new Intent();
                    data.putExtra("orderid", orderID);
                    setResult(RESULT_OK, data);
					finish();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderDetailActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void showDriverPosRealTime()
	{
		Intent intent = new Intent(PassLongOrderDetailActivity.this, DrvRealTimePosActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("driverid", drvID);
		PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void moveToPayOrderActivity()
	{
		Intent intent = new Intent(PassLongOrderDetailActivity.this, PassPayOrderActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

	    intent.putExtra("orderid", orderID);
	    intent.putExtra("ordertype", ConstData.ORDER_TYPE_LONG);
	    intent.putExtra("price", price);
	    intent.putExtra("reserve", false);

		PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}

	private void evaluateDriver()
	{
		Intent intentEval = new Intent(PassLongOrderDetailActivity.this, EvaluateActivity.class);

		intentEval.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intentEval.putExtra("driver", drvID);
		intentEval.putExtra("passenger", Global.loadUserID(getApplicationContext()));
		intentEval.putExtra("order_type", ConstData.ORDER_TYPE_LONG);
		intentEval.putExtra("orderid", orderID);
		intentEval.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);
		intentEval.putExtra("view_mode", false);
		intentEval.putExtra("level", 0);
		intentEval.putExtra("msg", "");

		PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivityForResult(intentEval, REQCODE_EVALUATE);
	}

	private void showEvaluation()
	{
		Intent intentEval = new Intent(PassLongOrderDetailActivity.this, EvaluateActivity.class);

		intentEval.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

		intentEval.putExtra("driver", drvID);
		intentEval.putExtra("passenger", Global.loadUserID(getApplicationContext()));
		intentEval.putExtra("order_type", ConstData.ORDER_TYPE_LONG);
		intentEval.putExtra("orderid", orderID);
		intentEval.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);
		intentEval.putExtra("view_mode", true);
		intentEval.putExtra("level", evaluated);
		intentEval.putExtra("msg", evalContent);
		PassLongOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intentEval);
	}


}

