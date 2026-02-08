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
import com.damytech.DataClasses.STDetDriverOrderInfo;
import com.damytech.DataClasses.STMidPoint;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

public class DrvOnceOrderDetailActivity extends SuperActivity
{
    private int REQCODE_EVAL_PASS = 1;
    private int REQCODE_RUN_ORDER = 2;

    private long mOrderId;
    private STDetDriverOrderInfo mDetOrderInfo = new STDetDriverOrderInfo();

	private ImageButton btn_back = null;
    private SmartImageView imgPhoto = null;
    private ImageView   imgGender;
    private TextView    txtAge;
    private TextView    txtName;
    private ImageView   imgPassPhone = null;
    private ImageView   imgRunOrder = null;
    private ImageView   imgRefresh = null;
    private ImageView   imgOrderCompleted = null;
    private ImageView   imgOrderExited = null;
    private ImageView   imgEvalOrder = null;
    private TextView    lblOrderNum = null;
    private TextView    lblEvalGood = null;
    private TextView    lblCarpoolCount = null;
    private TextView    lblStartAddres = null;
    private TextView    lblReserveDate = null;
    private TextView    lblMidpoints = null;
    private TextView    lblPrice = null;
    private TextView    lblState;
    private TextView    lblSysPrice = null;
    private ImageButton btnPhoto;
    private Button      btnComplain;
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
    private ImageView verifiedImage;
	private TextView txt_verified;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_onceorderdetail);

		initControls();
		initResolution();

        callGetOrderDetailInfo();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent data = new Intent();
            data.putExtra("orderid", mOrderId);
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
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
        mOrderId = getIntent().getLongExtra("orderid", 0);

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

        imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(onClickListener);

        imgPhoto = (SmartImageView) findViewById(R.id.viewPhoto);
        imgPhoto.isCircular = true;
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});
		imgPhoto.setOnClickListener(onClickListener);


        imgGender = (ImageView)findViewById(R.id.imgSex);
        txtAge = (TextView)findViewById(R.id.lblAge);
        txtName = (TextView)findViewById(R.id.lblName);
        lblOrderNum = (TextView)findViewById(R.id.lblOrderNo);
        verifiedImage = (ImageView)findViewById(R.id.is_verified);
		txt_verified = (TextView)findViewById(R.id.txt_verified);
        lblEvalGood = (TextView)findViewById(R.id.lblRatio);
        lblCarpoolCount = (TextView)findViewById(R.id.lblCarpoolCount);
        lblStartAddres = (TextView)findViewById(R.id.lblStartAddress);
        lblReserveDate = (TextView)findViewById(R.id.lblPrecDate);
        lblMidpoints = (TextView)findViewById(R.id.lblMidStation);
        lblPrice = (TextView)findViewById(R.id.lblMark);
        lblState = (TextView)findViewById(R.id.lblState);
        lblSysPrice = (TextView)findViewById(R.id.lblSiteMark);

        imgRunOrder = (ImageView) findViewById(R.id.imgRunCard);
        imgRunOrder.setOnClickListener(onClickListener);
        imgPassPhone = (ImageView) findViewById(R.id.imgPassPhone);
        imgPassPhone.setOnClickListener(onClickListener);
        imgOrderCompleted = (ImageView)findViewById(R.id.imgOrderCompleted);
        imgOrderExited = (ImageView)findViewById(R.id.imgOrderExited);
        imgEvalOrder = (ImageView)findViewById(R.id.imgEvalPass);
        imgEvalOrder.setOnClickListener(onClickListener);

        btnPhoto = (ImageButton)findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(onClickListener);
        btnComplain = (Button)findViewById(R.id.btnComplain);
        btnComplain.setOnClickListener(onClickListener);
	}

    private void showInsuranceDialog() {
        final Dialog InsuranceDialog = new Dialog(DrvOnceOrderDetailActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(DrvOnceOrderDetailActivity.this);
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
        insuranceStartTimeTxt = (TextView)InsuranceDialog.findViewById
                (R.id.insurance_start_time);
        insuranceStartTimeTxt.setText("保单生效时间: "+insuranceStartTime);
        insuranceEndTimeTxt = (TextView)InsuranceDialog.findViewById(R.id.insurance_end_time);
        insuranceEndTimeTxt.setText("保单中止时间: "+insuranceEndTime);
        if(insuranceNum == null){
            Global.showAdvancedToast(DrvOnceOrderDetailActivity.this,getString(R.string.INSURANCE_IN_PROCESS), Gravity.CENTER);
        }else if(mDetOrderInfo.state == ConstData.ORDER_STATE_CLOSED || mDetOrderInfo.state == ConstData.ORDER_STATE_CANCELLED ){
            Global.showAdvancedToast(DrvOnceOrderDetailActivity.this,getString(R.string.INSURANCE_CLOSED), Gravity.CENTER);
        }else{
            InsuranceDialog.show();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnPhoto:
                    selectPassenger();
                    break;
                case R.id.imgRunCard:
	                CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvOnceOrderDetailActivity.this)
			                .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
			                .message(getResources().getString(R.string.quedingzhixing))
			                .positiveListener(new View.OnClickListener() {
				                @Override
				                public void onClick(View v) {
					                runOrder();
				                }
			                })
			                .build();
	                dialog.show();
                    break;
                case R.id.imgPassPhone:
                    Global.callPhone(mDetOrderInfo.pass_list.get(0).phone, DrvOnceOrderDetailActivity.this);
                    break;
                case R.id.imgRefresh:
                    callGetOrderDetailInfo();
                    break;
                case R.id.imgEvalPass:
                    evalPassenger();
                    break;
                case R.id.btnComplain:
                    Global.callPhone(Global.getServiceCall(), DrvOnceOrderDetailActivity.this);
                    break;
            }
        }
    };

    private void selectPassenger()
    {
        Intent intent = new Intent(DrvOnceOrderDetailActivity.this, PassEvalInfoActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("passid", mDetOrderInfo.pass_list.get(0).uid);
        DrvOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    private void runOrder()
    {
	    if (Global.isOpenGPS(DrvOnceOrderDetailActivity.this)) {
		    Intent intent = new Intent(DrvOnceOrderDetailActivity.this, DrvCityOrderExeActivity.class);
		    intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		    intent.putExtra("orderid", mDetOrderInfo.uid);
		    intent.putExtra("ordertype", ConstData.ORDER_TYPE_ONCE);
            intent.putExtra("pass_phone", mDetOrderInfo.pass_list.get(0).phone);
		    DrvOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		    startActivityForResult(intent, REQCODE_RUN_ORDER);
	    } else {
		    CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvOnceOrderDetailActivity.this)
				    .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
				    .message(getResources().getString(R.string.mustopengps))
				    .positiveTitle(getResources().getString(R.string.mustopengps_ok))
				    .negativeTitle(getResources().getString(R.string.mustopengps_cancel))
				    .positiveListener(new View.OnClickListener() {
					    @Override
					    public void onClick(View v) {
						    Global.openGPSSetting(DrvOnceOrderDetailActivity.this);
					    }
				    })
				    .build();
		    dialog.show();
	    }
    }

    private void evalPassenger()
    {
        if (mDetOrderInfo.pass_list.size() > 0)
        {
	        if (mDetOrderInfo.pass_list.get(0).evaluated == 0)
	        {
	            Intent intent = new Intent(DrvOnceOrderDetailActivity.this, EvaluateActivity.class);
	            intent.putExtra("driver", Global.loadUserID(getApplicationContext()));
	            intent.putExtra("passenger", mDetOrderInfo.pass_list.get(0).uid);
	            intent.putExtra("order_type", ConstData.ORDER_TYPE_ONCE);
	            intent.putExtra("orderid", mDetOrderInfo.uid);

		        intent.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);
		        intent.putExtra("view_mode", false);
		        intent.putExtra("level", mDetOrderInfo.pass_list.get(0).evaluated);
		        intent.putExtra("msg", mDetOrderInfo.pass_list.get(0).eval_content);

		        DrvOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
	            startActivityForResult(intent, REQCODE_EVAL_PASS);
	        }
	        else
	        {
		        Intent intent = new Intent(DrvOnceOrderDetailActivity.this, EvaluateActivity.class);
		        intent.putExtra("driver", Global.loadUserID(getApplicationContext()));
		        intent.putExtra("passenger", mDetOrderInfo.pass_list.get(0).uid);
		        intent.putExtra("order_type", ConstData.ORDER_TYPE_ONCE);
		        intent.putExtra("orderid", mDetOrderInfo.uid);

		        intent.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);
		        intent.putExtra("view_mode", true);
		        intent.putExtra("level", mDetOrderInfo.pass_list.get(0).evaluated);
		        intent.putExtra("msg", mDetOrderInfo.pass_list.get(0).eval_content);

		        DrvOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		        startActivityForResult(intent, REQCODE_EVAL_PASS);
	        }
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
        Intent data = new Intent();
        data.putExtra("orderid", mOrderId);
        setResult(RESULT_OK, data);

        finishWithAnimation();
	}

    private void callGetOrderDetailInfo()
    {
        startProgress();

        CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()), mOrderId, ConstData.ORDER_TYPE_ONCE,
                Global.getIMEI(getApplicationContext()), detDriverOrderInfoHandler);
    }

    private void UpdateUI()
    {
        // set ui control info
        if (mDetOrderInfo.pass_list.get(0).pverified == 1) {
            verifiedImage.setImageResource(R.drawable.bk_person_verified);
	        txt_verified.setText("已通过身份认证");
        } else {
            verifiedImage.setImageResource(R.drawable.bk_person_notverified);
	        txt_verified.setText("未通过身份认证");
        }
        imgPhoto.setImageUrl(mDetOrderInfo.pass_list.get(0).image, R.drawable.icon_appprice_over);
        imgGender.setImageResource(mDetOrderInfo.pass_list.get(0).sex == 0 ? R.drawable.bk_manmark : R.drawable.bk_womanmark);
        txtAge.setText(mDetOrderInfo.pass_list.get(0).age + "");
        txtName.setText(mDetOrderInfo.pass_list.get(0).name);
        lblOrderNum.setText("订单编号： "+mDetOrderInfo.order_num);
        lblEvalGood.setText(getResources().getString(R.string.STR_HAOPINGLV)
                + mDetOrderInfo.pass_list.get(0).goodeval_rate_desc);
        lblCarpoolCount.setText(getResources().getString(R.string.STR_PINCHECISHU)
                + mDetOrderInfo.pass_list.get(0).carpool_count_desc);
        lblStartAddres.setText(mDetOrderInfo.start_addr + getResources().getString(R.string.addr_separator) + mDetOrderInfo.end_addr);
        lblReserveDate.setText(getResources().getString(R.string.STR_YUYUESHIJIAN)
                + mDetOrderInfo.start_time);
        String strMidPoints = "";
        for(int i = 0; i < mDetOrderInfo.mid_points.size(); i++)
        {
            STMidPoint midPoint = mDetOrderInfo.mid_points.get(i);
            if(i > 0)
            {
                strMidPoints += ",";
            }
            strMidPoints += midPoint.address;
        }
        if(strMidPoints.length() > 0)
        {
            lblMidpoints.setText(getResources().getString(R.string.STR_ZHONGTUDIAN)
                    + strMidPoints);
        }
        else
        {
            lblMidpoints.setVisibility(View.GONE);
        }

        lblPrice.setText(mDetOrderInfo.price + getResources().getString(R.string.STR_BALANCE_DIAN));
        lblState.setText(mDetOrderInfo.state_desc);
        lblSysPrice.setText(mDetOrderInfo.sysinfo_fee_desc);

        switch (mDetOrderInfo.state)
        {
            case ConstData.ORDER_STATE_DRV_ACCEPTED:
            case ConstData.ORDER_STATE_PUBLISHED:
            case ConstData.ORDER_STATE_GRABBED:
                imgPassPhone.setVisibility(View.VISIBLE);
                imgOrderCompleted.setVisibility(View.GONE);
                imgOrderExited.setVisibility(View.GONE);
                imgRunOrder.setVisibility(View.VISIBLE);
                imgEvalOrder.setVisibility(View.GONE);
                break;
            case ConstData.ORDER_STATE_STARTED:
            case ConstData.ORDER_STATE_DRV_ARRIVED:
            case ConstData.ORDER_STATE_PASS_GETON:
                imgPassPhone.setVisibility(View.VISIBLE);
                imgOrderCompleted.setVisibility(View.GONE);
                imgOrderExited.setVisibility(View.GONE);
                imgRunOrder.setVisibility(View.GONE);
                imgEvalOrder.setVisibility(View.VISIBLE);
                imgEvalOrder.setImageResource(R.drawable.bk_passevalnormal);
                imgEvalOrder.setEnabled(false);
                break;
            case ConstData.ORDER_STATE_FINISHED:
                imgEvalOrder.setVisibility(View.VISIBLE);
                imgEvalOrder.setImageResource(R.drawable.bk_passevalnormal);
                imgEvalOrder.setEnabled(false);
                break;
            case ConstData.ORDER_STATE_PAYED:
                imgPassPhone.setVisibility(View.GONE);
                imgOrderCompleted.setVisibility(View.VISIBLE);
                imgOrderExited.setVisibility(View.GONE);
                imgRunOrder.setVisibility(View.GONE);
                imgEvalOrder.setVisibility(View.VISIBLE);
                imgEvalOrder.setImageResource(R.drawable.bk_passevalsel);
                imgEvalOrder.setEnabled(true);
                break;
            case ConstData.ORDER_STATE_EVALUATED:
                imgPassPhone.setVisibility(View.GONE);
                imgOrderCompleted.setVisibility(View.VISIBLE);
                imgOrderExited.setVisibility(View.GONE);
                imgRunOrder.setVisibility(View.GONE);
                imgEvalOrder.setVisibility(View.VISIBLE);
                switch (mDetOrderInfo.pass_list.get(0).evaluated)
                {
	                case 1:
		                imgEvalOrder.setImageResource(R.drawable.btn_goodeval);
		                break;
	                case 2:
		                imgEvalOrder.setImageResource(R.drawable.btn_normaleval);
		                break;
	                case 3:
	                default:
		                imgEvalOrder.setImageResource(R.drawable.btn_badeval);
		                break;
                }
                break;
            case ConstData.ORDER_STATE_CLOSED:
	        case ConstData.ORDER_STATE_CANCELLED:
                imgPassPhone.setVisibility(View.GONE);
                imgOrderCompleted.setVisibility(View.GONE);
                imgOrderExited.setVisibility(View.VISIBLE);
                imgRunOrder.setVisibility(View.GONE);
                imgEvalOrder.setVisibility(View.VISIBLE);
	            switch (mDetOrderInfo.pass_list.get(0).evaluated)
	            {
		            case 1:
			            imgEvalOrder.setImageResource(R.drawable.btn_goodeval);
			            break;
		            case 2:
			            imgEvalOrder.setImageResource(R.drawable.btn_normaleval);
			            break;
		            case 3:
			            imgEvalOrder.setImageResource(R.drawable.btn_badeval);
			            break;
		            default:
			            imgEvalOrder.setVisibility(View.VISIBLE);
			            imgEvalOrder.setImageResource(R.drawable.bk_passevalsel);
			            imgEvalOrder.setEnabled(true);
			            break;
	            }

                break;
        }

    }




    private AsyncHttpResponseHandler detDriverOrderInfoHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
//            Utils.mLogError("订单详情：" + content);
            try {
                JSONObject result = new JSONObject(content).getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetMsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONObject retdata = result.getJSONObject("retdata");
                    // parse result
                    mDetOrderInfo = STDetDriverOrderInfo.decodeFromJSON(retdata);
                    insuranceNum = retdata.getString("appl_no");
                    insuranceDetial = retdata.getDouble("total_amount");
                    insuranceEndTime = retdata.getString("insexpr_date");
                    insuranceStartTime = retdata.getString("effect_time");
                    insuranceUser = retdata.getString("isd_name");
                    // udpate ui using detail order information
                    UpdateUI();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetMsg);
                }
                else
                {
                    Global.showAdvancedToast(DrvOnceOrderDetailActivity.this, szRetMsg, Gravity.CENTER);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
	        Global.showAdvancedToast(DrvOnceOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQCODE_EVAL_PASS ||
                requestCode == REQCODE_RUN_ORDER)
            callGetOrderDetailInfo();
    }
}
