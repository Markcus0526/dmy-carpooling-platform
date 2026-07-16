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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STDetDriverOrderInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONObject;

public class DrvOnoffOrderDetailActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
    long nOrderID = 0;
    int nOrderType = 0;
    STDetDriverOrderInfo stOrderInfo = new STDetDriverOrderInfo();

    DrvOnoffOrderFinishDialog dlg = null;

	private ImageButton btn_back = null;
    private ImageView imgPassPhone = null;
    private ImageView imgRunOrder = null;
    private ImageView imgRefresh = null;

    TextView lblOrderNo = null;
    SmartImageView imgPhoto = null;
    ImageButton btnPhoto = null;
    ImageView imgGender = null;
    TextView lblAge = null;
    TextView lblName = null;
    TextView lblGoodEval = null;
    TextView lblCarPoolCount = null;
    TextView lblPath = null;
    TextView []lblWeek = new TextView[7];
    TextView lblCardOutDate = null;
    TextView lblPrecDate = null;
    TextView lblMidPoint = null;
    TextView lblMark = null;
    TextView lblSiteMark = null;
    TextView lblTotalCarpool = null;
    TextView lblEval = null;
    RelativeLayout rlMid = null;
    RelativeLayout rlSerparate = null;
    Button btnCancel = null;
    Button btnOk = null;

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
                    JSONObject retdata = result.getJSONObject("retdata");
                    stOrderInfo = STDetDriverOrderInfo.decodeFromJSON(retdata);

                    RefreshPage();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(strMessaage);
                }
                else
                {
                    Global.showAdvancedToast(DrvOnoffOrderDetailActivity.this, strMessaage, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Global.showAdvancedToast(DrvOnoffOrderDetailActivity.this, ex.getMessage(), Gravity.CENTER);
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
                    CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
                                                            nOrderID,
                                                            nOrderType,
                                                            Global.getIMEI(getApplicationContext()),
                                                            handlerDetail);

                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(strMessaage);
                }
                else
                {
                    Global.showAdvancedToast(DrvOnoffOrderDetailActivity.this, strMessaage, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Global.showAdvancedToast(DrvOnoffOrderDetailActivity.this, ex.getMessage(), Gravity.CENTER);
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
		setContentView(R.layout.act_driver_onofforderdetail);

        nOrderID = getIntent().getLongExtra("orderid", 0);
        nOrderType = getIntent().getIntExtra("ordertype", 0);

		initControls();
		initResolution();
	}

	private void initControls()
	{
        lblOrderNo = (TextView) findViewById(R.id.lblCardNo);
        lblName = (TextView) findViewById(R.id.lblName);
        imgGender = (ImageView) findViewById(R.id.imgSex);
        lblAge = (TextView) findViewById(R.id.lblAge);
        lblGoodEval = (TextView) findViewById(R.id.lblRatio);
        lblCarPoolCount = (TextView) findViewById(R.id.lblCarpoolCount);
        lblPath = (TextView) findViewById(R.id.lblPath);
        lblWeek[0] = (TextView) findViewById(R.id.lblMon);
        lblWeek[1] = (TextView) findViewById(R.id.lblTue);
        lblWeek[2] = (TextView) findViewById(R.id.lblWed);
        lblWeek[3] = (TextView) findViewById(R.id.lblThr);
        lblWeek[4] = (TextView) findViewById(R.id.lblFri);
        lblWeek[5] = (TextView) findViewById(R.id.lblSat);
        lblWeek[6] = (TextView) findViewById(R.id.lblSun);
        lblCardOutDate = (TextView) findViewById(R.id.lblCardOutDate);
        lblPrecDate = (TextView) findViewById(R.id.lblPrecDate);
        lblMidPoint = (TextView) findViewById(R.id.lblMidStation);
        lblMark = (TextView) findViewById(R.id.lblMark);
        lblSiteMark = (TextView) findViewById(R.id.lblSiteMark);
        lblTotalCarpool = (TextView) findViewById(R.id.lblTotalCarPool);
        lblEval = (TextView) findViewById(R.id.lblEval);
        rlMid = (RelativeLayout) findViewById(R.id.rlSeparate1);
        rlSerparate = (RelativeLayout) findViewById(R.id.rlBottom);

        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(onClickListener);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(onClickListener);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(onClickListener);

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

        imgRunOrder = (ImageView) findViewById(R.id.imgRunCard);
        imgRunOrder.setOnClickListener(onClickListener);
        imgPassPhone = (ImageView) findViewById(R.id.imgPassPhone);
        imgPassPhone.setOnClickListener(onClickListener);
	}

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.imgRunCard:
                    if (stOrderInfo != null)
                    {
                        switch (stOrderInfo.state)
                        {
                            case 2:
                                Intent intent = new Intent(DrvOnoffOrderDetailActivity.this, DrvCityOrderExeActivity.class);
                                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                                intent.putExtra("orderid", nOrderID);
                                intent.putExtra("ordertype", nOrderType);
                                DrvOnoffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                                startActivity(intent);
                                break;
                            case 6:
                                break;
                            case 7:
                                Intent intentEval = new Intent(DrvOnoffOrderDetailActivity.this, EvaluateActivity.class);
                                intentEval.putExtra("driver", Global.loadUserID(getApplicationContext()));
                                intentEval.putExtra("passenger", stOrderInfo.pass_list.get(0).uid);
                                intentEval.putExtra("order_type", nOrderType);
                                intentEval.putExtra("orderid", nOrderID);
                                intentEval.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);
                                intentEval.putExtra("view_mode", false);
                                intentEval.putExtra("level", stOrderInfo.pass_list.get(0).evaluated);
                                intentEval.putExtra("msg", stOrderInfo.pass_list.get(0).eval_content);
                                DrvOnoffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                                startActivity(intentEval);
                                break;
                            case 8:
                                Intent intentEvaled = new Intent(DrvOnoffOrderDetailActivity.this, EvaluateActivity.class);
                                intentEvaled.putExtra("driver", Global.loadUserID(getApplicationContext()));
                                intentEvaled.putExtra("passenger", stOrderInfo.pass_list.get(0).uid);
                                intentEvaled.putExtra("order_type", nOrderType);
                                intentEvaled.putExtra("orderid", nOrderID);
                                intentEvaled.putExtra("from_mode", EvaluateActivity.FROM_DRIVER);
                                intentEvaled.putExtra("view_mode", true);
                                intentEvaled.putExtra("level", stOrderInfo.pass_list.get(0).evaluated);
                                intentEvaled.putExtra("msg", stOrderInfo.pass_list.get(0).eval_content);
                                DrvOnoffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                                startActivity(intentEvaled);
                                break;
                        }
                    }
                    break;
                case R.id.imgPassPhone:
                    if (stOrderInfo != null)
                    {
                        Global.callPhone(stOrderInfo.pass_list.get(0).phone, DrvOnoffOrderDetailActivity.this);
                        return;
                    }
                    break;
                case R.id.imgRefresh:
                    startProgress();
                    CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
                            nOrderID,
                            nOrderType,
                            Global.getIMEI(getApplicationContext()),
                            handlerDetail);
                    break;
                case R.id.btnOk:
                    Global.callPhone(Global.getServiceCall(), DrvOnoffOrderDetailActivity.this);
                    break;
                case R.id.btnCancel:
                    dlg = new DrvOnoffOrderFinishDialog(DrvOnoffOrderDetailActivity.this);
                    dlg.setOnDismissListener(DrvOnoffOrderDetailActivity.this);
	                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlg.show();
                    break;
                case R.id.btnPhoto:
                    Intent intent = new Intent(DrvOnoffOrderDetailActivity.this, PassEvalInfoActivity.class);
                    intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    intent.putExtra("passid", stOrderInfo.pass_list.get(0).uid);
                    DrvOnoffOrderDetailActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivity(intent);
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

	private void onClickBack()
	{
        Intent data = new Intent();
        data.putExtra("orderid", nOrderID);
        setResult(RESULT_OK, data);

		finishWithAnimation();
	}

    @Override
    public void onResume()
    {
        super.onResume();

        startProgress();
        CommManager.getDetailedDriverOrderInfo(Global.loadUserID(getApplicationContext()),
                nOrderID,
                nOrderType,
                Global.getIMEI(getApplicationContext()),
                handlerDetail);
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (dlg != null && dlg.isShowing())
            dlg.dismiss();
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

    private void RefreshPage()
    {
        if (stOrderInfo == null)
            return;

        lblOrderNo.setText(getString(R.string.STR_LONGDISTANCEDETAIL_DINGDANBIANHAO) + " ：" + stOrderInfo.order_num);

        imgPhoto.setImageUrl(stOrderInfo.pass_list.get(0).image, R.drawable.default_user_img);
        lblName.setText(stOrderInfo.pass_list.get(0).name);
        lblAge.setText(Integer.toString(stOrderInfo.pass_list.get(0).age));
        if (stOrderInfo.pass_list.get(0).sex == 0)
            imgGender.setImageResource(R.drawable.bk_manmark);
        else
            imgGender.setImageResource(R.drawable.bk_womanmark);
        lblGoodEval.setText(getString(R.string.STR_PASSENGERINFO_EVALRATIO) + " ：" + stOrderInfo.pass_list.get(0).goodeval_rate_desc);
        lblCarPoolCount.setText(getString(R.string.STR_PASSENGERINFO_CARPOOLCOUNT) + " ：" + stOrderInfo.pass_list.get(0).carpool_count_desc);

        lblPath.setText(stOrderInfo.start_addr + getResources().getString(R.string.addr_separator) + stOrderInfo.end_addr);

        WeekItemShow(stOrderInfo.valid_days, stOrderInfo.left_days);

        lblCardOutDate.setText(getString(R.string.STR_LONGDISTANCEDETAIL_CHUDANSHIJIAN) + " ：" + stOrderInfo.accept_time);
        lblPrecDate.setText(getString(R.string.STR_LONGDISTANCEDETAIL_YUYUESHIJIAN) + " ：" + stOrderInfo.start_time);

	    String strMidPointName = "";
	    if (stOrderInfo.mid_points != null && stOrderInfo.mid_points.size() > 0)
        {
            for (int i = 0; i < stOrderInfo.mid_points.size(); i++)
            {
                if ( i == 0 )
                    strMidPointName = stOrderInfo.mid_points.get(i).address;
                else
                    strMidPointName = ", " + stOrderInfo.mid_points.get(i).address;
            }
        }
        if(strMidPointName.length() > 0)
        {
            lblMidPoint.setText(getString(R.string.STR_ZHONGTUDIAN) + strMidPointName);
        }
        else
        {
            lblMidPoint.setVisibility(View.GONE);
        }


        lblMark.setText(Double.toString(stOrderInfo.price) + getString(R.string.STR_BALANCE_DIAN) + "   " + stOrderInfo.state_desc);
        lblSiteMark.setText(stOrderInfo.sysinfo_fee_desc);
        lblTotalCarpool.setText(stOrderInfo.total_count_desc + "  " + stOrderInfo.total_income_desc);
        try { lblEval.setText(stOrderInfo.evgood_count_desc + ", " + stOrderInfo.evnormal_count_desc + ", " + stOrderInfo.evbad_count_desc); } catch(Exception ex) {}

        switch (stOrderInfo.state)
        {
            case ConstData.ORDER_STATE_GRABBED:
                imgRunOrder.setImageResource(R.drawable.bk_runorder);
                imgRunOrder.setEnabled(true);
                imgRunOrder.setVisibility(View.VISIBLE);
                imgPassPhone.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setBackgroundResource(R.drawable.roundgreenwhite_frame);
                lblTotalCarpool.setVisibility(View.GONE);
                lblEval.setVisibility(View.GONE);
                rlMid.setVisibility(View.GONE);
                rlSerparate.setVisibility(View.GONE);
                break;
            case ConstData.ORDER_STATE_FINISHED:
                imgRunOrder.setImageResource(R.drawable.bk_passevalnormal);
                imgRunOrder.setEnabled(false);
                imgRunOrder.setVisibility(View.VISIBLE);
                imgPassPhone.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                btnCancel.setEnabled(false);
                btnCancel.setBackgroundResource(R.drawable.roundgreengray_frame);
                lblTotalCarpool.setVisibility(View.GONE);
                lblEval.setVisibility(View.GONE);
                rlMid.setVisibility(View.GONE);
                rlSerparate.setVisibility(View.GONE);
                break;
            case ConstData.ORDER_STATE_PAYED:
                imgRunOrder.setImageResource(R.drawable.bk_passevalsel);
                imgRunOrder.setEnabled(true);
                imgRunOrder.setVisibility(View.VISIBLE);
                imgPassPhone.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                btnCancel.setEnabled(false);
                btnCancel.setBackgroundResource(R.drawable.roundgreengray_frame);
                lblTotalCarpool.setVisibility(View.GONE);
                lblEval.setVisibility(View.GONE);
                rlMid.setVisibility(View.GONE);
                rlSerparate.setVisibility(View.GONE);
                break;
            case ConstData.ORDER_STATE_EVALUATED:
                imgRunOrder.setEnabled(true);
                switch (stOrderInfo.pass_list.get(0).evaluated)
                {
                    case ConstData.EVALUATE_NONE:
                        imgRunOrder.setImageResource(R.drawable.bk_driverevalsel);
                        imgRunOrder.setEnabled(false);
                        break;
                    case ConstData.EVALUATE_GOOD:
                        imgRunOrder.setImageResource(R.drawable.btn_goodeval);
                        break;
                    case ConstData.EVALUATE_NORMAL:
                        imgRunOrder.setImageResource(R.drawable.btn_normaleval);
                        break;
                    case ConstData.EVALUATE_BAD:
                        imgRunOrder.setImageResource(R.drawable.btn_badeval);
                        break;
                }
                imgRunOrder.setVisibility(View.VISIBLE);
                imgPassPhone.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setBackgroundResource(R.drawable.roundgreenwhite_frame);
                lblTotalCarpool.setVisibility(View.GONE);
                lblEval.setVisibility(View.GONE);
                rlMid.setVisibility(View.GONE);
                rlSerparate.setVisibility(View.GONE);
                break;
            case ConstData.ORDER_STATE_CLOSED:
                imgPassPhone.setVisibility(View.GONE);
                imgRunOrder.setVisibility(View.GONE);
                btnCancel.setEnabled(false);
                btnCancel.setBackgroundResource(R.drawable.roundgreengray_frame);
                lblTotalCarpool.setVisibility(View.VISIBLE);
                lblEval.setVisibility(View.VISIBLE);
                rlMid.setVisibility(View.VISIBLE);
                rlSerparate.setVisibility(View.VISIBLE);
                break;
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
    public void onDismiss(DialogInterface dialog)
    {
        dlg = (DrvOnoffOrderFinishDialog) dialog;
        int nRes = dlg.IsDeleted();

        if (nRes  == 1)
        {
            CommManager.stopOnOffOrder(Global.loadUserID(getApplicationContext()),
                                        nOrderID,
                                        Global.getIMEI(getApplicationContext()),
                                        handlerStop);
        }
    }
}
