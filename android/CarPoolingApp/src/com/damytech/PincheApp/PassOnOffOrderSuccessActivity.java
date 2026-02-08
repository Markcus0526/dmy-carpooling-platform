package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;

public class PassOnOffOrderSuccessActivity extends SuperActivity
{
    ImageButton btn_back = null;
    SmartImageView imgPhoto = null;
    SmartImageView imgCar = null;
    TextView lblHint = null;
    TextView lblName = null;
    TextView lblAge = null;
    ImageView imgGender = null;
    ImageView imgCarType = null;
    TextView lblBrand = null;
    TextView lblColor = null;
    TextView[] lblWeek = new TextView[7];
    TextView lblStartTime = null;
    TextView lblPath = null;
    TextView lblMidPoints = null;
    TextView lblCarNo = null;

    private ImageButton btnPhoto = null;
    private Button btnCar = null;

    Button btnToDo = null;


	long nOrderID = -1;
	String strPass = "";
	private long drv_id = 0;
	private String drv_name = "";
	private String drv_img = "";
	private int drv_age = 0;
	private int drv_gender = 0;
	private String car_img = "";
	private int car_style = 0;
	private String car_brand = "";
	private String car_type = "";
	private String car_color = "";
	private String carno = "";
	private String start_time = "";
	private String start_addr = "";
	private String end_addr = "";
	private String password = "";
	private String days = "";
	private String leftdays = "";
	private String midpoints = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pass_normal_success);

        initControls();
	    getExtras();
        initResolution();
    }

	private void getExtras()
	{
		strPass = getIntent().getStringExtra("password");
		nOrderID = getIntent().getLongExtra("orderid", -1);

		drv_id = getIntent().getLongExtra("drv_id", 0);
		drv_name = getIntent().getStringExtra("drv_name");
		drv_img = getIntent().getStringExtra("drv_img");
		drv_age = getIntent().getIntExtra("drv_age", 0);
		drv_gender = getIntent().getIntExtra("drv_gender", ConstData.GENDER_MALE);
		car_img = getIntent().getStringExtra("car_img");
		car_style = getIntent().getIntExtra("car_style", ConstData.CARSTYLE_JINGJIXING);
		car_brand = getIntent().getStringExtra("car_brand");
		car_type = getIntent().getStringExtra("car_type");
		car_color = getIntent().getStringExtra("car_color");
		carno = getIntent().getStringExtra("carno");
		start_time = getIntent().getStringExtra("start_time");
		start_addr = getIntent().getStringExtra("start_addr");
		end_addr = getIntent().getStringExtra("end_addr");
		password = getIntent().getStringExtra("password");
		days = getIntent().getStringExtra("days");
		leftdays = getIntent().getStringExtra("leftdays");
		midpoints = getIntent().getStringExtra("midpoints");

		refreshPage();
	}

    @Override
    protected void onResume()
    {
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

        imgPhoto = (SmartImageView) findViewById(R.id.viewPhoto);
        imgPhoto.isCircular = true;
	    imgPhoto.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
		    }
	    });

        btnPhoto = (ImageButton)findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectDriver();
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
                Intent intent = new Intent(PassOnOffOrderSuccessActivity.this, DisplayCarImgActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("img_url", car_img);
                PassOnOffOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

        lblHint = (TextView) findViewById(R.id.lblHint1);
        lblName = (TextView) findViewById(R.id.lblName);
        lblAge = (TextView) findViewById(R.id.lblAge);
        imgGender = (ImageView) findViewById(R.id.imgSex);
        imgCarType = (ImageView) findViewById(R.id.imgCarType);
        lblBrand = (TextView) findViewById(R.id.lblBrandName);
        lblColor = (TextView) findViewById(R.id.lblColor);
        lblWeek[0] = (TextView) findViewById(R.id.lblMon);
        lblWeek[1] = (TextView) findViewById(R.id.lblTue);
        lblWeek[2] = (TextView) findViewById(R.id.lblWed);
        lblWeek[3] = (TextView) findViewById(R.id.lblThr);
        lblWeek[4] = (TextView) findViewById(R.id.lblFri);
        lblWeek[5] = (TextView) findViewById(R.id.lblSat);
        lblWeek[6] = (TextView) findViewById(R.id.lblSun);
        lblStartTime = (TextView) findViewById(R.id.lblStartTime);
        lblPath = (TextView) findViewById(R.id.lblPath);
        lblMidPoints = (TextView) findViewById(R.id.lblMidPoints);
        lblCarNo = (TextView) findViewById(R.id.lblCarNo);

        btnToDo = (Button) findViewById(R.id.btnToDetail);
        btnToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassOnOffOrderSuccessActivity.this, PassMainActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            intent.putExtra(PassMainActivity.TAB_EXTRA_NAME, PassMainActivity.TAB_DINGDAN);
                PassOnOffOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
	            finish();
            }
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        finishWithAnimation();
    }

    public void refreshPage()
    {
        lblName.setText(drv_name);
        if (drv_gender == ConstData.GENDER_MALE) {
	        imgGender.setImageResource(R.drawable.bk_manmark);
	        lblAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
        } else {
	        imgGender.setImageResource(R.drawable.bk_womanmark);
	        lblAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
        }
        lblAge.setText(Integer.toString(drv_age));
        imgPhoto.setImageUrl(drv_img, R.drawable.default_user_img);
        imgCar.setImageUrl(car_img, R.drawable.default_car_img);
        lblColor.setText(car_color);
        lblBrand.setText(car_brand);
        switch (car_style)
        {
            case ConstData.CARSTYLE_JINGJIXING:
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

        lblPath.setText(start_addr + " - " + end_addr);
        WeekItemShow(days, leftdays);
        if(midpoints.length() > 0)
            lblMidPoints.setText(getString(R.string.STR_ZHONGTUDIAN) + midpoints);
        else
            lblMidPoints.setVisibility(View.GONE);

        lblStartTime.setText(getString(R.string.STR_SINGERSUCCESS_BENCICHUCHE) + start_time + getString(R.string.STR_SINGERSUCCESS_CHUFA));

        String strText = getString(R.string.STR_SINGERSUCCESS_PASSWORDTIP1) + strPass + getString(R.string.STR_SINGERSUCCESS_PASSWORDTIP2);
        SpannableString ss = new SpannableString(strText);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.LIGHT_YELLOW_COLOR)), 10, 14, 0);
        lblHint.setText(ss);

        String strCarNo = Global.getConcealedCarNo(getApplicationContext(), carno);
        lblCarNo.setText(strCarNo);
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

    private void selectDriver()
    {
        Intent intent = new Intent(PassOnOffOrderSuccessActivity.this, DrvEvalInfoActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra("driverid", drv_id);
        PassOnOffOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }
}

