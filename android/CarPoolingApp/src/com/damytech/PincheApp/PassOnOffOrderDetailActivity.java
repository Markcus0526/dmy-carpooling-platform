package com.damytech.PincheApp;

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
import com.damytech.DataClasses.STDetPassOrderInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONObject;

public class PassOnOffOrderDetailActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
	private final int PAY_ORDER = 1;

	long nOrderID = -1;
	int nOrderType = -1;

	ImageButton btn_back = null;
	SmartImageView imgPhoto = null;
	SmartImageView imgCar = null;
	ImageView imgRefresh = null;
    ImageButton btnPhoto = null;
    Button btnCar = null;

	TextView lblName = null;
	ImageView imgGender = null;
	TextView lblAge = null;
	TextView lblCarAge = null;
	TextView lblGoodEvalRatio = null;
	TextView lblCarPoolCount = null;
	ImageView imgCarType = null;
	ImageView imgCarBrand = null;
	TextView txtCarBrand = null;
	TextView lblStyle = null;
	TextView lblColor = null;
	TextView lblOrderNo = null;
	TextView lblRunning = null;
	TextView lblPath = null;
	TextView lblMidPoints = null;
	TextView[] lblWeek = new TextView[7];
	TextView lblTime = null;
	TextView lblCarNum = null;
	TextView lblElecNum = null;
	TextView lblStatus = null;
	TextView lblCarpool = null;
	ImageView imgCallDriver = null;
	ImageView imgOther = null;
	Button btnPause = null;
	Button btnFinish = null;
	Button btnLongFinish = null;
	Button btnTouSu = null;
	RelativeLayout rlSeparate2 = null;

	PassOnOffOrderPauseDialog dlg = null;
	DrvOnoffOrderFinishDialog dlgStop = null;

	STDetPassOrderInfo stOrderInfo = new STDetPassOrderInfo();
	private AsyncHttpResponseHandler handlerDetail = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					stOrderInfo = STDetPassOrderInfo.decodeFromJSON(result.getJSONObject("retdata"));

					if (stOrderInfo != null)
						RefreshPage();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(strMessaage);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, strMessaage, Gravity.CENTER);
					onClickBack();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, ex.getMessage(), Gravity.CENTER);
				onClickBack();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};

	private AsyncHttpResponseHandler handlerPause = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, getString(R.string.STR_SUCCESS), Gravity.CENTER);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(strMessaage);
				}
				else
				{
					Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, strMessaage, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};

	private AsyncHttpResponseHandler handlerStop = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String strMessaage = result.getString("retmsg");
				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
                    Global.g_bPassOrderItemShouldUpgrade = true;
					Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, getString(R.string.STR_SUCCESS), Gravity.CENTER);

                    Intent data = new Intent();
                    data.putExtra("orderid", nOrderID);
                    setResult(RESULT_OK, data);
					finishWithAnimation();
//					CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
//							nOrderID,
//							nOrderType,
//							Global.getIMEI(getApplicationContext()),
//							handlerDetail);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					stopProgress();
					logout(strMessaage);
				}
				else
				{
					stopProgress();
					Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, strMessaage, Gravity.CENTER);
				}
			} catch (Exception ex) {
				stopProgress();
				ex.printStackTrace();
				Global.showAdvancedToast(PassOnOffOrderDetailActivity.this, ex.getMessage(), Gravity.CENTER);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);
			stopProgress();
		}
	};

	 @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pass_normal_detail);

		nOrderID = getIntent().getLongExtra("orderid", -1);
		nOrderType = getIntent().getIntExtra("order_type", -1);

		initControls();
		initResolution();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		refreshInformation();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent data = new Intent();
            data.putExtra("orderid", nOrderID);
            setResult(RESULT_OK, data);
        }
        return super.onKeyDown(keyCode, event);
    }

	private void refreshInformation()
	{
		startProgress();
		CommManager.getDetailedPassengerOrderInfo(Global.loadUserID(getApplicationContext()),
				nOrderID,
				nOrderType,
				Global.getIMEI(getApplicationContext()),
				handlerDetail);
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
				return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
			}
		});
        btnPhoto = (ImageButton)findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassOnOffOrderDetailActivity.this, DrvEvalInfoActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("driverid", stOrderInfo.driver_info.uid);
                PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });



		imgCar = (SmartImageView) findViewById(R.id.viewCar);
		imgCar.isCircular = true;
		imgCar.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.default_car_img);
			}
		});
        btnCar = (Button)findViewById(R.id.btnCar);
        btnCar.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
		        Intent intent = new Intent(PassOnOffOrderDetailActivity.this, DisplayCarImgActivity.class);
		        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		        intent.putExtra("img_url", stOrderInfo.driver_info.carimg);
		        PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		        startActivity(intent);
	        }
        });

		lblName = (TextView) findViewById(R.id.lblName);
		lblAge = (TextView) findViewById(R.id.lblAge);
		imgGender = (ImageView) findViewById(R.id.imgSex);
		lblCarAge = (TextView) findViewById(R.id.lblDriverYears);
		lblGoodEvalRatio = (TextView) findViewById(R.id.lblEval);
		lblCarPoolCount = (TextView) findViewById(R.id.lblHistoryCount);
		lblPath = (TextView) findViewById(R.id.lblPath);
		lblMidPoints = (TextView) findViewById(R.id.lblMidPos);
		lblWeek[0] = (TextView)findViewById(R.id.lblMon);
		lblWeek[1] = (TextView)findViewById(R.id.lblTue);
		lblWeek[2] = (TextView)findViewById(R.id.lblWed);
		lblWeek[3] = (TextView)findViewById(R.id.lblThr);
		lblWeek[4] = (TextView)findViewById(R.id.lblFri);
		lblWeek[5] = (TextView)findViewById(R.id.lblSat);
		lblWeek[6] = (TextView)findViewById(R.id.lblSun);
		lblColor = (TextView) findViewById(R.id.lblColor);
		lblStyle = (TextView) findViewById(R.id.lblBrandName);
		imgCarType = (ImageView) findViewById(R.id.imgCarType);
		imgCarBrand = (ImageView)findViewById(R.id.imgBrand);
		txtCarBrand = (TextView)findViewById(R.id.txt_brand);
		lblOrderNo = (TextView) findViewById(R.id.lblCardno);
		lblRunning = (TextView) findViewById(R.id.lblRunning);
		lblTime = (TextView) findViewById(R.id.lblTime);
		lblCarNum = (TextView) findViewById(R.id.lblCarNo);
		lblElecNum = (TextView) findViewById(R.id.lblElecNo);
		lblStatus = (TextView) findViewById(R.id.lblStatus);
		lblCarpool = (TextView) findViewById(R.id.lblCarpool);

		imgCallDriver = (ImageView) findViewById(R.id.imgCallDriver);
		imgCallDriver.setOnClickListener(onClickListener);
		imgOther = (ImageView) findViewById(R.id.imgOther);
		imgOther.setOnClickListener(onClickListener);
		btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(onClickListener);
		btnFinish = (Button) findViewById(R.id.btnFinish);
		btnFinish.setOnClickListener(onClickListener);
		btnLongFinish = (Button) findViewById(R.id.btnLongFinish);
		btnLongFinish.setOnClickListener(onClickListener);
		btnTouSu = (Button) findViewById(R.id.btnTouSu);
		btnTouSu.setOnClickListener(onClickListener);

		rlSeparate2 = (RelativeLayout) findViewById(R.id.rlSeparate2);
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId())
			{
				case R.id.imgRefresh:
					refreshInformation();
					break;
				case R.id.imgCallDriver:
					switch (stOrderInfo.state)
					{
						case 2:
						case 3:
						case 4:
						case 5:
						case 6:
							Global.callPhone(stOrderInfo.driver_info.phone, PassOnOffOrderDetailActivity.this);
							break;
						case 7:
							Intent intent = new Intent(PassOnOffOrderDetailActivity.this, EvaluateActivity.class);
							intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
							intent.putExtra("driver", stOrderInfo.driver_info.uid);
							intent.putExtra("passenger", Global.loadUserID(getApplicationContext()));
							intent.putExtra("order_type", nOrderType);
							intent.putExtra("orderid", nOrderID);
							intent.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);
							intent.putExtra("view_mode", false);
							intent.putExtra("level", ConstData.EVALUATE_GOOD);
							intent.putExtra("msg", "");
							PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
							startActivity(intent);
							break;
						case 8:
							Intent intentEvaled = new Intent(PassOnOffOrderDetailActivity.this, EvaluateActivity.class);
							intentEvaled.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
							intentEvaled.putExtra("driver", stOrderInfo.driver_info.uid);
							intentEvaled.putExtra("passenger", Global.loadUserID(getApplicationContext()));
							intentEvaled.putExtra("order_type", nOrderType);
							intentEvaled.putExtra("orderid", nOrderID);
							intentEvaled.putExtra("from_mode", EvaluateActivity.FROM_PASSENGER);
							intentEvaled.putExtra("view_mode", true);
							intentEvaled.putExtra("level", stOrderInfo.evaluated);
							intentEvaled.putExtra("msg", stOrderInfo.eval_content);
							PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
							startActivity(intentEvaled);
							break;
					}
					break;
				case R.id.imgOther:
					switch (stOrderInfo.state)
					{
						case 2: {
							// Show driver position
							Intent intent = new Intent(PassOnOffOrderDetailActivity.this, DrvRealTimePosActivity.class);
							intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
							intent.putExtra("driverid", stOrderInfo.driver_info.uid);
							PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
							startActivity(intent);
							break;
						}
						case 3:
						case 4:
						case 5:
						{
							if (stOrderInfo.driver_info.uid > 0)
							{
								Intent intent = new Intent(PassOnOffOrderDetailActivity.this, DrvRealTimePosActivity.class);
								intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
								intent.putExtra("driverid", stOrderInfo.driver_info.uid);
								PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
								startActivityForResult(intent, PAY_ORDER);
							}
							break;
						}
						case 6:
						{
					   		Intent intent = new Intent(PassOnOffOrderDetailActivity.this, PassPayOrderActivity.class);
							intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

							intent.putExtra("orderid", stOrderInfo.uid);
							intent.putExtra("ordertype", ConstData.ORDER_TYPE_ONOFF);
							intent.putExtra("price", stOrderInfo.price);
							intent.putExtra("reserve", false);

							PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
							startActivity(intent);
							break;
						}
						case 7:
						case 8:
							Intent intent = new Intent(PassOnOffOrderDetailActivity.this, PassOnOffOrderReserveActivity.class);
							intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());

							intent.putExtra("orderid", stOrderInfo.uid);

							PassOnOffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
							startActivity(intent);
//							Intent intent = new Intent(PassNormalOrderDetailActivity.this, PayActivity.class);
//							intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
//
//							intent.putExtra("orderid", stOrderInfo.uid);
//							intent.putExtra("ordertype", ConstData.ORDER_TYPE_ONOFF);
//							intent.putExtra("price", stOrderInfo.price);
//							intent.putExtra("reserve", true);
//
//							PassNormalOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
//							startActivity(intent);
							break;
					}
					break;
				case R.id.btnPause:
					dlg = new PassOnOffOrderPauseDialog(PassOnOffOrderDetailActivity.this);
					dlg.setOnDismissListener(PassOnOffOrderDetailActivity.this);
					dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dlg.show();
					break;
				case R.id.btnFinish:
					dlgStop = new DrvOnoffOrderFinishDialog(PassOnOffOrderDetailActivity.this);
					dlgStop.setOnDismissListener(PassOnOffOrderDetailActivity.this);
					dlgStop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dlgStop.show();
					break;
				case R.id.btnLongFinish:
					dlgStop = new DrvOnoffOrderFinishDialog(PassOnOffOrderDetailActivity.this);
					dlgStop.setOnDismissListener(PassOnOffOrderDetailActivity.this);
					dlgStop.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dlgStop.show();
					break;
				case R.id.btnTouSu:
					Global.callPhone(Global.getServiceCall(), PassOnOffOrderDetailActivity.this);
					break;
			}
		}
	};

	private void onClickBack()
	{
        Intent data = new Intent();
        data.putExtra("orderid", nOrderID);
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

	public void RefreshPage()
	{
		if (stOrderInfo != null)
		{
			lblName.setText(stOrderInfo.driver_info.name);
			if (stOrderInfo.driver_info.gender == 0)
				imgGender.setImageResource(R.drawable.bk_manmark);
			else
				imgGender.setImageResource(R.drawable.bk_womanmark);
			lblAge.setText(Integer.toString(stOrderInfo.driver_info.age));
			lblCarAge.setText(stOrderInfo.driver_info.drv_career_desc);
			lblGoodEvalRatio.setText(getString(R.string.STR_HAOPINGLV) + stOrderInfo.driver_info.evgood_rate_desc);
			lblCarPoolCount.setText(getString(R.string.STR_PASSNORMALCONFIRM_FUWUCISHU) + " ："  + stOrderInfo.driver_info.carpool_count_desc);;
			imgPhoto.setImageUrl(stOrderInfo.driver_info.image, R.drawable.default_user_img);
			imgCar.setImageUrl(stOrderInfo.driver_info.carimg, R.drawable.default_car_img);
			lblColor.setText(stOrderInfo.driver_info.color);

			imgCarBrand = (ImageView)findViewById(R.id.imgBrand);
			txtCarBrand = (TextView)findViewById(R.id.txt_brand);

			int nCarBrandID = Global.getBrandImgFromName(stOrderInfo.driver_info.brand);
			if (nCarBrandID > 0)
			{
				imgCarBrand.setImageResource(nCarBrandID);
				imgCarBrand.setVisibility(View.VISIBLE);
				txtCarBrand.setVisibility(View.GONE);
			}
			else
			{
				imgCarBrand.setVisibility(View.GONE);
				txtCarBrand.setVisibility(View.VISIBLE);
				txtCarBrand.setText(stOrderInfo.driver_info.brand);
			}

			lblStyle.setText(stOrderInfo.driver_info.style);
			switch (stOrderInfo.driver_info.type)
			{
				case 1:
					imgCarType.setImageResource(R.drawable.econcar_sel);
					break;
				case 2:
					imgCarType.setImageResource(R.drawable.safecar_sel);
					break;
				case 3:
					imgCarType.setImageResource(R.drawable.luxurycar_sel);
					break;
				case 4:
					imgCarType.setImageResource(R.drawable.businesscar_sel);
					break;
			}

			lblPath.setText(stOrderInfo.start_addr + " - " + stOrderInfo.end_addr);
			WeekItemShow(stOrderInfo.days, stOrderInfo.leftdays);
			if (stOrderInfo.mid_points != null && stOrderInfo.mid_points.size() > 0)
			{
				String strMidPointName = "";
				for (int i = 0; i < stOrderInfo.mid_points.size(); i++)
				{
					if ( i == 0 )
						strMidPointName += stOrderInfo.mid_points.get(i).address;
					else
						strMidPointName += ", " + stOrderInfo.mid_points.get(i).address;
				}

				lblMidPoints.setText(getString(R.string.STR_ZHONGTUDIAN) + strMidPointName);
			};

			lblCarNum.setText(Global.getConcealedCarNo(getApplication(), stOrderInfo.driver_info.carno));
			lblElecNum.setText(getString(R.string.STR_SINGERSUCCESS_DIANZICHEPIAO) + stOrderInfo.password);
			lblStatus.setText(stOrderInfo.state_desc);
			lblOrderNo.setText(getString(R.string.STR_LONGDISTANCEDETAIL_DINGDANBIANHAO) + " ：" + stOrderInfo.order_num);
			lblTime.setText(getString(R.string.STR_SINGERSUCCESS_CHUDANSHIJIAN) + stOrderInfo.start_time);
			lblCarpool.setText(stOrderInfo.total_count_desc + "  " + stOrderInfo.total_income_desc + System.getProperty("line.separator") + stOrderInfo.evgood_count_desc + ", " + stOrderInfo.evnormal_count_desc + ", " + stOrderInfo.evbad_count_desc);

			// Refresh order state
			{
				lblStatus.setVisibility(View.VISIBLE);
				lblCarpool.setVisibility(View.VISIBLE);
				imgOther.setVisibility(View.VISIBLE);
				imgOther.setEnabled(true);
				btnLongFinish.setVisibility(View.VISIBLE);
				lblRunning.setVisibility(View.VISIBLE);
				btnPause.setVisibility(View.VISIBLE);
				btnFinish.setVisibility(View.VISIBLE);
				lblCarNum.setVisibility(View.VISIBLE);
				lblElecNum.setVisibility(View.VISIBLE);
				imgCallDriver.setVisibility(View.VISIBLE);
				imgCallDriver.setImageResource(R.drawable.bk_driverphone);
				rlSeparate2.setVisibility(View.VISIBLE);
                btnLongFinish.setVisibility(View.VISIBLE);
                btnLongFinish.setEnabled(true);
                btnLongFinish.setBackgroundResource(R.drawable.roundgreenwhite_frame);
			}

			switch (stOrderInfo.state)
			{
				case ConstData.ORDER_STATE_GRABBED:
					lblCarpool.setVisibility(View.INVISIBLE);
					imgOther.setImageResource(R.drawable.bk_driverpos_grey);
                    imgOther.setEnabled(false);
					btnLongFinish.setVisibility(View.INVISIBLE);
					break;
				case ConstData.ORDER_STATE_STARTED:
				case ConstData.ORDER_STATE_DRV_ARRIVED:
				case ConstData.ORDER_STATE_PASS_GETON:
					lblRunning.setVisibility(View.INVISIBLE);
					lblCarpool.setVisibility(View.INVISIBLE);
					imgOther.setImageResource(R.drawable.bk_driverpos);
					imgOther.setEnabled(true);
					btnPause.setVisibility(View.INVISIBLE);
					btnFinish.setVisibility(View.INVISIBLE);
					break;
				case ConstData.ORDER_STATE_FINISHED:
					lblRunning.setVisibility(View.INVISIBLE);
					lblCarpool.setVisibility(View.INVISIBLE);
					imgOther.setImageResource(R.drawable.bk_pay);
					imgOther.setEnabled(true);
					btnPause.setVisibility(View.INVISIBLE);
					btnFinish.setVisibility(View.INVISIBLE);
					lblCarNum.setVisibility(View.INVISIBLE);
					lblElecNum.setVisibility(View.INVISIBLE);
                    // must disable finish order button
                    btnLongFinish.setEnabled(false);
                    btnLongFinish.setBackgroundResource(R.drawable.roundgreengray_frame);
					break;
				case ConstData.ORDER_STATE_PAYED:
					lblRunning.setVisibility(View.INVISIBLE);
					lblCarNum.setVisibility(View.INVISIBLE);
					lblElecNum.setVisibility(View.INVISIBLE);
					lblCarpool.setVisibility(View.INVISIBLE);
					imgCallDriver.setImageResource(R.drawable.bk_driverevalsel);
					imgOther.setImageResource(R.drawable.bk_prec);
					imgOther.setEnabled(true);
					btnPause.setVisibility(View.INVISIBLE);
					btnFinish.setVisibility(View.INVISIBLE);
					break;
				case ConstData.ORDER_STATE_EVALUATED:
					lblRunning.setVisibility(View.INVISIBLE);
					lblCarNum.setVisibility(View.INVISIBLE);
					lblElecNum.setVisibility(View.INVISIBLE);
					lblCarpool.setVisibility(View.INVISIBLE);
					switch (stOrderInfo.evaluated)
					{
						case ConstData.EVALUATE_GOOD:
							imgCallDriver.setImageResource(R.drawable.btn_goodeval);
							break;
						case ConstData.EVALUATE_NORMAL:
							imgCallDriver.setImageResource(R.drawable.btn_normaleval);
							break;
						case ConstData.EVALUATE_BAD:
						default:
							imgCallDriver.setImageResource(R.drawable.btn_badeval);
							break;
					}
					imgOther.setImageResource(R.drawable.bk_prec);
					imgOther.setEnabled(true);
					btnPause.setVisibility(View.INVISIBLE);
					btnFinish.setVisibility(View.INVISIBLE);
					break;
				case ConstData.ORDER_STATE_CLOSED:
					lblRunning.setVisibility(View.INVISIBLE);
					lblCarNum.setVisibility(View.INVISIBLE);
					lblElecNum.setVisibility(View.INVISIBLE);
					imgCallDriver.setVisibility(View.INVISIBLE);
					imgOther.setVisibility(View.INVISIBLE);
					btnPause.setVisibility(View.GONE);
					btnFinish.setVisibility(View.GONE);
					btnLongFinish.setVisibility(View.GONE);
					rlSeparate2.setVisibility(View.GONE);
					break;
			}
		}

		return;
	}

	private void WeekItemShow(String strValidDays, String strLeftDays)
	{
		for (int i = 0; i < 7; i++)
		{
			lblWeek[i].setBackgroundResource(R.drawable.bk_normday);
			lblWeek[i].setTextColor(getResources().getColor(R.color.TABCOLOR));
		}

		if (strLeftDays != null)
		{
			for (int i = 0; i < 7; i++)
			{
				String strVal = Integer.toString(i);

				if ( strLeftDays.contains(strVal) == true )
				{
					lblWeek[i].setBackgroundResource(R.drawable.bk_diselday);
					lblWeek[i].setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		}

		if (strValidDays != null)
		{
			for (int i = 0; i < 7; i++)
			{
				String strVal = Integer.toString(i);

				if ( strValidDays.contains(strVal) == true )
				{
					lblWeek[i].setBackgroundColor(getResources().getColor(R.color.TABCOLOR));
					lblWeek[i].setTextColor(getResources().getColor(R.color.WHITE_COLOR));
				}
			}
		}

		return;
	}

	@Override
	public void onStop()
	{
		super.onStop();

		if (dlg != null && dlg.isShowing())
			dlg.dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog)
	{
		if (dlg == dialog)
		{
			int nRes = dlg.IsDeleted();
			if (nRes == 1)
			{
				startProgress();
				CommManager.pauseOnOffOrder(Global.loadUserID(getApplicationContext()),
						nOrderID,
						Global.getIMEI(getApplicationContext()),
						handlerPause);
			}
		}

		if (dlgStop == dialog)
		{
			int nRes = dlgStop.IsDeleted();
			if (nRes == 1)
			{
				startProgress();
                Global.g_lPassChangedOrderId = nOrderID;
                Global.g_nPassChangedOrderType = ConstData.ORDER_TYPE_ONOFF;
				CommManager.cancelOnOffOrder(Global.loadUserID(getApplicationContext()),
						nOrderID,
						Global.getIMEI(getApplicationContext()),
						handlerStop);
			}
		}
	}
}

