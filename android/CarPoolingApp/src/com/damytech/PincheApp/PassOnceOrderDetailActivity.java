
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
import android.util.Log;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 拼友主界面
 */
public class PassOnceOrderDetailActivity extends SuperActivity
{
    /*
     *static references part
     */
    //log tag
    private static final String TAG = "erik_debug";

    //应用状态码
    private final int REQCODE_EVALUATE = 1;
	private final int REQCODE_PAYORDER = 2;
    //other static
    private int ORDER_TYPE_SINGLE = 1;
    //car type list
    private static final int NORMAL_CAR = 1;
    private static final int COMFORT_CAR = 2;
    private static final int LUXURY_CAR = 3;
    private static final int BUSSINESS_CAR = 4;

    //UI instances
    private TextView orderStateTxt;
    private Button cancelButton;
    private Button callButton;
    private Button leftFunction;
    private Button rightFunction;
    private ImageButton btn_back;
    private ImageButton btn_refresh;
    //driver info ui
    private SmartImageView driverImage;
    private ImageView imageSex;
    private TextView ageView;
    private TextView driverName;
    private TextView driverDuration;
    private TextView driverEvaluation;
    private TextView serviceTime;
    private ImageView carType;
    private TextView carLogo;
    private TextView carName;
    private TextView carColor;
    private SmartImageView carImage;
    private String carImgUrl = "";
    //order display
    private TextView address;
    private TextView workTime;
    private TextView points;
    private TextView carInfo;
    private TextView ticket;
    private TextView orderNum;
    private ImageButton btnPhoto = null;
    private ImageButton btnCar = null;

    private LinearLayout rlCarNo = null;

    //others
    private long orderId;
    private String drvPhone;
    private long drvID;
    private int evaluated;
    private String evalContent;
    private double price;
    private int orderState;

    //orderState static
    private static final int WAITFOR_PROCESS = 2;
    private static final int IN_PROCESS = 3;
    private static final int DRIVER_ARRIVE = 4;
    private static final int PASS_IN_CAR = 5;
    private static final int PROCESS_DONE = 6;
    private static final int PAY_DONE = 7;
    private static final int EVALUE_DONE = 8;
    private static final int ORDER_CLOSE = 9;
	private static final int ORDER_CANCELLED = 10;

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

	/*
	 *method part
	 */




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.act_pass_single_detail);

        initControls();//控件示例化
        initResolution();//适配屏幕

    }

    @Override
    protected void onResume() {
        super.onResume();

	    getDetailedPassengerOrderInfo();
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
            data.putExtra("orderid", orderId);
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * 初始化activity中所有控件
     */
    private void initControls() {
	    getOrderDetail();
        initUIComponents();
    }

    private void cancelOrder() {
	    CommonAlertDialog alertDialog = new CommonAlertDialog.Builder(PassOnceOrderDetailActivity.this)
			    .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
			    .message(getResources().getString(R.string.STR_QUEDING_QUXIAODINGDAN))
			    .positiveListener(new View.OnClickListener() {
				    @Override
				    public void onClick(View v) {
					    startProgress();
					    CommManager.cancelOnceOrder(Global.loadUserID(getApplicationContext()), orderId,
							    Global.getIMEI(getApplicationContext()), cancelOrderHandler);
				    }
			    })
			    .build();
	    alertDialog.show();
    }

    private AsyncHttpResponseHandler cancelOrderHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);

            stopProgress();
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");
                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                //Log.d(TAG, "code:message ------->"+ nRetcode+":"+jsonMsg);
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    //JSONObject retdata = jsonResult.getJSONObject("retdata");
                    Log.d(TAG,"success.....................");
                    refreshInformation();
                    rightFunction.setClickable(false);
                    rightFunction.setBackgroundResource(R.drawable.btn_goodeval);

//                    Intent data = new Intent();
//                    data.putExtra("orderid", orderId);
//                    setResult(RESULT_OK, data);
//                    finish();
				}
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassOnceOrderDetailActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
	        Global.showAdvancedToast(PassOnceOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void showInsuranceDialog() {
        final Dialog InsuranceDialog = new Dialog(PassOnceOrderDetailActivity.this);
        LayoutInflater mInflater = LayoutInflater.from(PassOnceOrderDetailActivity.this);
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
            Global.showAdvancedToast(PassOnceOrderDetailActivity.this,getString(R.string.NO_INSURARANCE), Gravity.CENTER);
        }else if(orderState == ConstData.ORDER_STATE_CLOSED || orderState == ConstData.ORDER_STATE_CANCELLED ){
            Global.showAdvancedToast(PassOnceOrderDetailActivity.this,getString(R.string.INSURANCE_CLOSED), Gravity.CENTER);
        }else{
            InsuranceDialog.show();
        }
    }


    private void initUIComponents() {
        mInsuranceButton = (Button)findViewById(R.id.insurance_detail);
        mInsuranceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInsuranceDialog();
            }
        });
        btn_refresh = (ImageButton)findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDetailedPassengerOrderInfo();
            }
        });
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });
        orderStateTxt = (TextView)findViewById(R.id.order_state);
        cancelButton = (Button)findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder();
            }
        });
        callButton = (Button)findViewById(R.id.call);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.callPhone(Global.getServiceCall(), PassOnceOrderDetailActivity.this);
            }
        });
        leftFunction = (Button)findViewById(R.id.left_function);
        rightFunction = (Button)findViewById(R.id.right_function);
        initDriverInfo();
        initDetail();
    }

    private void initDetail() {
        orderNum = (TextView)findViewById(R.id.order_num);
        address = (TextView)findViewById(R.id.address);
        workTime = (TextView)findViewById(R.id.work_time);
        points = (TextView)findViewById(R.id.points);
        carInfo = (TextView)findViewById(R.id.car_info);
        ticket = (TextView)findViewById(R.id.ticket);
    }

    private void initDriverInfo() {
        driverImage = (SmartImageView)findViewById(R.id.img_photo);
        driverImage.isCircular = true;
	    driverImage.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
		    }
	    });

        btnPhoto = (ImageButton)findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDriver();
            }
        });

        imageSex = (ImageView)findViewById(R.id.imgSex);
        ageView = (TextView)findViewById(R.id.lblAge);
        driverName = (TextView)findViewById(R.id.driver_name);
        driverDuration = (TextView)findViewById(R.id.driver_duration);
        driverEvaluation = (TextView)findViewById(R.id.driver_evaluation);
        serviceTime =(TextView)findViewById(R.id.service_time_detail);
        carType = (ImageView)findViewById(R.id.car_type);
        carLogo = (TextView)findViewById(R.id.car_logo);
        carName =(TextView)findViewById(R.id.car_name);
        carColor =(TextView)findViewById(R.id.car_color);

        carImage = (SmartImageView)findViewById(R.id.img_car);
        carImage.isCircular = true;
	    carImage.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
		    }
	    });

        btnCar = (ImageButton)findViewById(R.id.btnCar);
        btnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCar();
            }
        });

        rlCarNo = (LinearLayout) findViewById(R.id.rlCarNo);
    }

    private void setDriverUIInfo(JSONObject retdata) throws Exception{
        driverImage.setImageUrl(retdata.getString("img"), R.drawable.icon_appprice_over);
        if(0 == retdata.getInt("gender")){
            imageSex.setImageResource(R.drawable.bk_manmark);
        } else {
            imageSex.setImageResource(R.drawable.bk_womanmark);
        }
        ageView.setText(""+retdata.getInt("age"));
        driverName.setText(retdata.getString("name"));
        driverDuration.setText("驾龄 "+retdata.getInt("drv_career")+"年");
        driverEvaluation.setText("好评率："+retdata.getInt("evgood_rate")+"%");
        serviceTime.setText(retdata.getInt("carpool_count")+"次");
        chooseCarType(retdata.getInt("type"));
        carLogo.setText(retdata.getString("brand"));
        carName.setText(retdata.getString("style"));
        carColor.setText(retdata.getString("color"));
        carImage.setImageUrl(retdata.getString("carimg"), R.drawable.icon_appprice_over);
        carImgUrl = retdata.getString("carimg");

        carInfo.setText(Global.getConcealedCarNo(getApplication(), retdata.getString("carno")));
        drvPhone = retdata.getString("phone");
        drvID = retdata.getLong("uid");
    }


    private void chooseCarType(int type) {
        switch (type){
            case NORMAL_CAR:
                carType.setBackgroundResource(R.drawable.first_car_green);
                break;
            case COMFORT_CAR:
                carType.setBackgroundResource(R.drawable.second_car_green);
                break;
            case LUXURY_CAR:
                carType.setBackgroundResource(R.drawable.third_car_green);
                break;
            case BUSSINESS_CAR:
                carType.setBackgroundResource(R.drawable.fourth_car_green);
                break;
            default:
                break;
        }
    }

    private void onClickBack()
    {
        Intent data = new Intent();
        data.putExtra("orderid", orderId);
        setResult(RESULT_OK, data);

        finishWithAnimation();
    }

    private void getOrderDetail() {
        orderId = getIntent().getLongExtra("orderid", 0);
        Utils.mLogError("传过来的订单号："+orderId);
        //Log.d(TAG, "orderId----->"+ orderId);
    }

    private void getDetailedPassengerOrderInfo() {
	    startProgress();
        CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
                orderId, ORDER_TYPE_SINGLE, Global.getIMEI(getApplicationContext()), orderDetailHandler);
    }


    private AsyncHttpResponseHandler orderDetailHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

            stopProgress();
            Utils.mLogError("订单详情："+content);
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                Log.d(TAG, "nRetcode----->"+nRetcode);
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONObject retdata = jsonResult.getJSONObject("retdata");
                    JSONObject driverInfo = retdata.getJSONObject("driver_info");
                    orderState = retdata.getInt("state");
                    Log.d(TAG, "orderState---->" + orderState);
	                int evaluated = retdata.getInt("evaluated");
					insuranceNum = retdata.getString("appl_no");
                    insuranceDetial = retdata.getDouble("total_amount");
                    insuranceEndTime = retdata.getString("insexpr_date");
                    insuranceStartTime = retdata.getString("effect_time");
                    insuranceUser = retdata.getString("isd_name");
                    UpdateDetailInfo(orderState);
                    try{
                        setDriverUIInfo(driverInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    setDetailInfo(retdata);
                    UpdateEvaluationDetail();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassOnceOrderDetailActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
	        Global.showAdvancedToast(PassOnceOrderDetailActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void UpdateEvaluationDetail() {
        switch (evaluated)
        {
            case ConstData.EVALUATE_GOOD:
                rightFunction.setBackgroundResource(R.drawable.btn_goodeval);
                break;
            case ConstData.EVALUATE_NORMAL:
                rightFunction.setBackgroundResource(R.drawable.btn_normaleval);
                break;
            case ConstData.EVALUATE_BAD:
                rightFunction.setBackgroundResource(R.drawable.btn_badeval);
                break;
            default:
                break;
        }

    }

    private void setDetailInfo(JSONObject retdata)  throws Exception{
        orderNum.setText("订单编号： "+ retdata.getString("order_num"));
        address.setText(retdata.getString("start_addr") + getResources().getString(R.string.addr_separator) + retdata.getString("end_addr"));
        //orderTime.setText("出单时间： "+ retdata.getString("create_time"));
        workTime.setText("预约时间： "+ retdata.getString("start_time"));
        JSONArray pointsArray = retdata.getJSONArray("mid_points");

        StringBuilder pointList = new StringBuilder("中途点：");
        for(int i = 0; i < pointsArray.length(); i++){
            if (i != 0)
                pointList.append(",");
            pointList.append(pointsArray.getJSONObject(i).getString("addr"));
        }

        if (pointsArray.length() == 0) {
            points.setVisibility(View.GONE);
        } else {
            points.setText(pointList);
        }

        ticket.setText("拼车码：" + retdata.getString("password"));
        evaluated = retdata.getInt("evaluated");
        evalContent = retdata.getString("eval_content");
        price = retdata.getDouble("price");
    }

    private void UpdateDetailInfo(int orderState) {
         switch (orderState) {
	         case WAITFOR_PROCESS:
		         leftFunction.setBackgroundResource(R.drawable.bk_driverphone);
		         leftFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         callDriver();
			         }
		         });
		         rightFunction.setBackgroundResource(R.drawable.bk_driverpos);
		         rightFunction.setEnabled(true);
                 rightFunction.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         displayDrivePos();
                     }
                 });
		         cancelButton.setVisibility(View.VISIBLE);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_WAITFOR_PROCESS);
		         rlCarNo.setVisibility(View.VISIBLE);
		         break;
	         case IN_PROCESS:
		         leftFunction.setBackgroundResource(R.drawable.bk_driverphone);
		         leftFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         callDriver();
			         }
		         });
		         rightFunction.setBackgroundResource(R.drawable.bk_driverpos);
		         rightFunction.setEnabled(true);
		         rightFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         displayDrivePos();
			         }
		         });
		         cancelButton.setVisibility(View.VISIBLE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_IN_PROCESS);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         rlCarNo.setVisibility(View.VISIBLE);
		         break;
	         case DRIVER_ARRIVE:
		         leftFunction.setBackgroundResource(R.drawable.bk_driverphone);
		         leftFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         callDriver();
			         }
		         });
		         rightFunction.setBackgroundResource(R.drawable.bk_driverpos);
		         rightFunction.setEnabled(true);
		         rightFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         displayDrivePos();
			         }
		         });
		         cancelButton.setVisibility(View.VISIBLE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_IN_PROCESS);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         rlCarNo.setVisibility(View.VISIBLE);
		         break;
	         case PASS_IN_CAR:
		         leftFunction.setBackgroundResource(R.drawable.bk_driverphone);
		         leftFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         callDriver();
			         }
		         });
		         rightFunction.setBackgroundResource(R.drawable.bk_driverpos);
		         rightFunction.setEnabled(true);
		         rightFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         displayDrivePos();
			         }
		         });
		         cancelButton.setVisibility(View.GONE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_IN_PROCESS);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         rlCarNo.setVisibility(View.VISIBLE);
		         break;
	         case PROCESS_DONE:
		         leftFunction.setBackgroundResource(R.drawable.bk_driverphone);
		         leftFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         callDriver();
			         }
		         });
		         rightFunction.setBackgroundResource(R.drawable.bk_pay);
		         rightFunction.setEnabled(true);
		         rightFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         payBill();
			         }
		         });
		         cancelButton.setVisibility(View.GONE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_PROCESS_DONE);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         rlCarNo.setVisibility(View.GONE);
		         break;
	         case PAY_DONE:
		         leftFunction.setBackgroundResource(R.drawable.bk_completeorder);
                 leftFunction.setEnabled(false);
		         rightFunction.setBackgroundResource(R.drawable.bk_driverevalsel);
		         rightFunction.setEnabled(true);
		         rightFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         evaluateDriver();
			         }
		         });
		         cancelButton.setVisibility(View.GONE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_PAY_DONE);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         rlCarNo.setVisibility(View.GONE);
		         break;
	         case EVALUE_DONE:
		         leftFunction.setBackgroundResource(R.drawable.bk_completeorder);
                 leftFunction.setEnabled(false);
		         if (evaluated == ConstData.EVALUATE_GOOD)
			         rightFunction.setBackgroundResource(R.drawable.btn_goodeval);
		         else if (evaluated == ConstData.EVALUATE_NORMAL)
			         rightFunction.setBackgroundResource(R.drawable.btn_normaleval);
		         else
			         rightFunction.setBackgroundResource(R.drawable.btn_badeval);
		         rightFunction.setEnabled(true);
		         rightFunction.setOnClickListener(new View.OnClickListener() {
			         @Override
			         public void onClick(View view) {
				         displayEvaluationDetail();
			         }
		         });
		         cancelButton.setVisibility(View.GONE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_EVALUE_DONE);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		         rlCarNo.setVisibility(View.GONE);
		         break;
	         case ORDER_CLOSE:
		         leftFunction.setBackgroundResource(R.drawable.bk_exitorder);
                 leftFunction.setEnabled(false);
		         rightFunction.setBackgroundResource(R.drawable.bk_driverevalsel);
		         rightFunction.setEnabled(true);
		         if (evaluated > 0)
		         {
			         rightFunction.setOnClickListener(new View.OnClickListener() {
				         @Override
				         public void onClick(View view) {
					         displayEvaluationDetail();
				         }
			         });
                 } else {
		             rightFunction.setOnClickListener(new View.OnClickListener() {
			             @Override
			             public void onClick(View view) {
				             evaluateDriver();
			             }
		             });
	             }
                 cancelButton.setVisibility(View.GONE);
                 orderStateTxt.setText(R.string.STR_SINGLEDETAIL_ORDER_CLOSE);
                 orderStateTxt.setTextColor(this.getResources().getColor(R.color.GRAY_COLOR));
                 rlCarNo.setVisibility(View.GONE);
                 break;
	         case ORDER_CANCELLED:
		         leftFunction.setBackgroundResource(R.drawable.bk_exitorder);
                 leftFunction.setEnabled(false);
		         rightFunction.setBackgroundResource(R.drawable.bk_driverevalnormal);
		         rightFunction.setEnabled(false);
		         cancelButton.setVisibility(View.GONE);
		         orderStateTxt.setText(R.string.STR_SINGLEDETAIL_ORDER_CLOSE);
		         orderStateTxt.setTextColor(this.getResources().getColor(R.color.GRAY_COLOR));
		         rlCarNo.setVisibility(View.GONE);
		         break;
             default:
                 break;
         }
    }

    private void displayDrivePos() {
        Intent intent = new Intent(PassOnceOrderDetailActivity.this, DrvRealTimePosActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("driverid", drvID);
        PassOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    private void payBill() {
        Intent intent = new Intent(PassOnceOrderDetailActivity.this, PassPayOrderActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

        intent.putExtra("orderid", orderId);
        intent.putExtra("ordertype", ConstData.ORDER_TYPE_ONCE);
        intent.putExtra("price", price);
        intent.putExtra("reserve", false);

        PassOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivityForResult(intent, REQCODE_PAYORDER);
    }

    private void displayEvaluationDetail() {
        Intent intentEval = new Intent(PassOnceOrderDetailActivity.this, EvaluateActivity.class);
        intentEval.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intentEval.putExtra("driver", drvID);
        intentEval.putExtra("passenger", Global.loadUserID(getApplicationContext()));
        intentEval.putExtra("order_type", ConstData.ORDER_TYPE_ONCE);
        intentEval.putExtra("orderid", orderId);
        intentEval.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);
        intentEval.putExtra("view_mode", true);
        intentEval.putExtra("level", evaluated);
        intentEval.putExtra("msg", evalContent);
        PassOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intentEval);
    }

    private void evaluateDriver() {
        Intent intentEval = new Intent(PassOnceOrderDetailActivity.this, EvaluateActivity.class);

        intentEval.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

        intentEval.putExtra("driver", drvID);
        intentEval.putExtra("passenger", Global.loadUserID(getApplicationContext()));
        intentEval.putExtra("order_type", ConstData.ORDER_TYPE_ONCE);
        intentEval.putExtra("orderid", orderId);
        intentEval.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);
        intentEval.putExtra("view_mode", false);
        intentEval.putExtra("level", 0);
        intentEval.putExtra("msg", "");

        PassOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivityForResult(intentEval, REQCODE_EVALUATE);
    }

    private void callDriver() {
        Global.callPhone(drvPhone, PassOnceOrderDetailActivity.this);
    }

    private void refreshInformation() {
        if (orderId <= 0)
            return;

        startProgress();
        CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
                orderId, ConstData.ORDER_TYPE_ONCE, Global.getIMEI(getApplicationContext()), orderDetailHandler);
    }


	/*
	 * 适配不同屏幕
	 */

    private void initResolution()
    {
        LinearLayout parent_layout = (LinearLayout)findViewById(R.id.parent_layout);
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
     * 恢复应用时状态匹配
     */
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
	        else if (requestCode == REQCODE_PAYORDER)
            {
	            refreshInformation();
            }
        }
    }


    private void selectDriver()
    {
        Intent intent = new Intent(PassOnceOrderDetailActivity.this, DrvEvalInfoActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("driverid", drvID);
        PassOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    private void selectCar()
    {
        Intent intent = new Intent(PassOnceOrderDetailActivity.this, DisplayCarImgActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("img_url", carImgUrl);
        PassOnceOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }
}

