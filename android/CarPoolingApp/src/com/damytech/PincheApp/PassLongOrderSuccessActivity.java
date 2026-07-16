package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import org.json.JSONArray;
import org.json.JSONObject;

public class PassLongOrderSuccessActivity extends SuperActivity
{
	private long orderID = 0;
	private long drv_id = 0;
	private String drv_name = "";
	private String drv_img = "";
	private int drv_age = 0;
	private int drv_gender = ConstData.GENDER_MALE;
	private String car_img = "";
	private int car_style = ConstData.CARSTYLE_JINGJIXING;
	private String car_brand = "";
	private String car_type = "";
	private String car_color = "";
	private String carno = "";
	private String start_time = "";
	private String start_addr = "";
	private String end_addr = "";
	private String password = "";
	private String drv_career_desc = "";
	private String evgood_rate_desc = "";
	private String carpool_count_desc = "";

	private TextView txtPwd = null;
	private SmartImageView imgPhoto = null;
	private ImageView imgGender = null;
	private TextView txtAge = null;
	private TextView txtDrvName = null;
	private SmartImageView imgCar = null;
	private ImageView imgCarType = null;
	private ImageView imgBrand = null;
	private TextView txtBrand = null;
	private TextView txtStyle = null;
	private TextView txtColor = null;
	private TextView txtDrvCareer = null;
	private TextView txtEvGoodRate = null;
	private TextView txtCarpoolCnt = null;

	private TextView txtStartTime = null;
	private TextView txtAddr = null;
	private TextView txtCarNo = null;

	private ImageButton btnBack = null;
	private ImageButton btnPhoto = null;
	private Button btnCancel = null;
	private Button btnConfirm = null;
	private Button btnOrders = null;

	private TextView txtInfo1 = null, txtInfo2 = null, txtInfo3 = null, txtInfo4 = null;
	private RelativeLayout cancel_layout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pass_long_success);

        initControls();//控件示例化
        initResolution();//适配屏幕
    }

    @Override
    protected void onResume() {
        super.onResume();
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


    /*
     * 初始化activity中所有控件
     */
    private void initControls()
    {
	    txtPwd = (TextView)findViewById(R.id.txtPwd);
	    imgPhoto = (SmartImageView)findViewById(R.id.img_photo);
	    imgPhoto.isCircular = true;
	    imgPhoto.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
		    }
	    });

	    imgGender = (ImageView)findViewById(R.id.imgSex);
	    txtAge = (TextView)findViewById(R.id.lblAge);
	    txtDrvName = (TextView)findViewById(R.id.driver_name);
	    imgCar = (SmartImageView)findViewById(R.id.img_car);
	    imgCar.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_car_img);
		    }
	    });

        // 2014-10-21 Bug-281
//        imgCar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PassLongOrderSuccessActivity.this, DisplayCarImgActivity.class);
//                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//                intent.putExtra("img_url", car_img);
//                PassLongOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//                startActivity(intent);
//            }
//        });


	    imgCarType = (ImageView)findViewById(R.id.car_type);
	    imgBrand = (ImageView)findViewById(R.id.car_logo);
	    txtBrand = (TextView)findViewById(R.id.txt_carlogo);
	    txtStyle = (TextView)findViewById(R.id.car_name);
	    txtColor = (TextView)findViewById(R.id.car_color);

	    txtDrvCareer = (TextView)findViewById(R.id.driver_duration);
	    txtEvGoodRate = (TextView)findViewById(R.id.driver_evaluation);
	    txtCarpoolCnt = (TextView)findViewById(R.id.service_time_detail);

	    txtStartTime = (TextView)findViewById(R.id.txt_start_time);
	    txtAddr = (TextView)findViewById(R.id.txt_addr);
	    txtCarNo = (TextView)findViewById(R.id.txt_carno);

	    btnBack = (ImageButton)findViewById(R.id.btn_back);
	    btnPhoto = (ImageButton)findViewById(R.id.btn_photo);
	    btnCancel = (Button)findViewById(R.id.btn_cancel);
	    btnOrders = (Button)findViewById(R.id.btn_my_orders);
	    btnConfirm = (Button)findViewById(R.id.btn_confirm);

	    btnBack.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    onClickBack();
		    }
	    });

        // Bug 281
//		btnPhoto.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				moveToDriverInfoActivity();
//			}
//		});

	    btnCancel.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    onClickCancel();
		    }
	    });

	    btnOrders.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    moveToUserOrdersActivity();
		    }
	    });

	    btnConfirm.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    onClickConfirm();
		    }
	    });

	    txtInfo1 = (TextView)findViewById(R.id.txt_info1);
	    txtInfo2 = (TextView)findViewById(R.id.txt_info2);
	    txtInfo3 = (TextView)findViewById(R.id.txt_info3);
	    txtInfo4 = (TextView)findViewById(R.id.txt_info4);

	    getExtras();

	    if (txtInfo1.getText().toString().equals(""))
	    {
		    startProgress();
	        CommManager.getLongOrderCancelInfo(Global.loadUserID(getApplicationContext()), orderID, Global.getIMEI(getApplicationContext()), longOrderCancelInfo_handler);
	    }

	    cancel_layout = (RelativeLayout)findViewById(R.id.cancel_layout);
	    cancel_layout.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    cancel_layout.setVisibility(View.INVISIBLE);
		    }
	    });
    }


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (cancel_layout.getVisibility() == View.VISIBLE)
		{
			cancel_layout.setVisibility(View.INVISIBLE);
			return true;
		}
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	private void getExtras()
	{
		orderID = getIntent().getLongExtra("orderid", 0);
		drv_id = getIntent().getLongExtra("drv_id", 0);
		drv_name = getIntent().getStringExtra("drv_name");
		drv_img = getIntent().getStringExtra("drv_img");
		drv_age = getIntent().getIntExtra("drv_age", 0);
		drv_gender = getIntent().getIntExtra("drv_gender", ConstData.GENDER_MALE);
		car_img = getIntent().getStringExtra("car_img");
		car_brand = getIntent().getStringExtra("car_brand");
		car_type = getIntent().getStringExtra("car_type");
		car_color = getIntent().getStringExtra("car_color");
		carno = getIntent().getStringExtra("carno");
		start_time = getIntent().getStringExtra("start_time");
		start_addr = getIntent().getStringExtra("start_addr");
		end_addr = getIntent().getStringExtra("end_addr");
		password = getIntent().getStringExtra("password");
		drv_career_desc = getIntent().getStringExtra("drv_career_desc");
		evgood_rate_desc = getIntent().getStringExtra("evgood_rate_desc");
		carpool_count_desc = getIntent().getStringExtra("carpool_count_desc");

		updateUI();
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

        if (resultCode != RESULT_OK)
            return;

    }


	private void onClickCancel()
	{
		cancel_layout.setVisibility(View.VISIBLE);
	}


	private void moveToUserOrdersActivity()
	{
		Intent intent = new Intent(PassLongOrderSuccessActivity.this, PassMainActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra(PassMainActivity.TAB_EXTRA_NAME, PassMainActivity.TAB_DINGDAN);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PassLongOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
		finish();
	}


	private void updateUI()
	{
		txtDrvCareer.setText(drv_career_desc);
		txtEvGoodRate.setText(getResources().getString(R.string.STR_HAOPINGLV) + evgood_rate_desc);
		txtCarpoolCnt.setText(carpool_count_desc);

		txtPwd.setText(password);
		imgPhoto.setImageUrl(drv_img, R.drawable.default_user_img);
		if (drv_gender == ConstData.GENDER_MALE) {
			imgGender.setImageResource(R.drawable.bk_manmark);
			txtAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
		} else {
			imgGender.setImageResource(R.drawable.bk_womanmark);
			txtAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		}
		txtAge.setText("" + drv_age);
		txtDrvName.setText(drv_name);
		imgCar.setImageUrl(car_img, R.drawable.default_car_img);
		switch (car_style)
		{
			case ConstData.CARSTYLE_JINGJIXING:
			default:
				imgCarType.setImageResource(R.drawable.econcar_sel);
				break;
			case ConstData.CARSTYLE_SHUSHIXING:
				imgCarType.setImageResource(R.drawable.safecar_sel);
				break;
			case ConstData.CARSTYLE_HAOHUAXING:
				imgCarType.setImageResource(R.drawable.luxurycar_sel);
				break;
			case ConstData.CARSTYLE_SHANGWUXING:
				imgCarType.setImageResource(R.drawable.businesscar_sel);
				break;
		}

		int nBrandID = Global.getBrandImgFromName(car_brand);
		if (nBrandID > 0)
		{
			imgBrand.setImageResource(nBrandID);
			imgBrand.setVisibility(View.VISIBLE);
			txtBrand.setVisibility(View.GONE);
		}
		else
		{
			imgBrand.setVisibility(View.GONE);
			txtBrand.setVisibility(View.VISIBLE);
			txtBrand.setText(car_brand);
		}

		txtStyle.setText(car_type);
		txtColor.setText(car_color);
		txtStartTime.setText(start_time + getResources().getString(R.string.STR_SINGERSUCCESS_CHUFA));
		txtAddr.setText(start_addr);

		String szCarNo = "";
		try {
	        szCarNo = getResources().getString(R.string.STR_CHEPAIHAO) + " " + (carno.substring(0, 1) + "***" + carno.substring(carno.length() - 3));
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        szCarNo = carno;
	    }
        txtCarNo.setText(szCarNo);

	}



	private void moveToDriverInfoActivity()
	{
		if (drv_id <= 0)
			return;

		Intent intent = new Intent(PassLongOrderSuccessActivity.this, DrvEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("driverid", drv_id);
		PassLongOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


	private void onClickBack()
	{
		finishWithAnimation();
	}


	private AsyncHttpResponseHandler longOrderCancelInfo_handler = new AsyncHttpResponseHandler()
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

					JSONArray arrRules = retdata.getJSONArray("balance_rule");

					JSONObject info1 = arrRules.getJSONObject(0);
					JSONObject info2 = arrRules.getJSONObject(1);
					JSONObject info3 = arrRules.getJSONObject(2);
					JSONObject info4 = arrRules.getJSONObject(3);

					txtInfo1.setText(info1.getString("rule"));
					txtInfo2.setText(info2.getString("rule"));
					txtInfo3.setText(info3.getString("rule"));
					txtInfo4.setText(info4.getString("rule"));
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderSuccessActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderSuccessActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private void onClickConfirm()
	{
        // only hide dialog & not call service
        cancel_layout.setVisibility(View.GONE);

//		startProgress();
//		CommManager.signLongOrderPassengerGiveup(0, orderID, Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), confirm_handler);
	}


	private AsyncHttpResponseHandler confirm_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(PassLongOrderSuccessActivity.this, getResources().getString(R.string.STR_SUCCESS), Gravity.CENTER);

					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassLongOrderSuccessActivity.this, szRetMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassLongOrderSuccessActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

}

