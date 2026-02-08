package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;

/**
 * 拼友主界面
 */
public class PassOnceOrderSuccessActivity extends SuperActivity
{
    /*
     *static references part
     */
    //log tag
    private static final String TAG = "erik_debug";
    //应用状态码
    private int REQCODE_TEST = 0;

    //UI instances
    private Button orderDetailButton;
    private ImageButton btn_back;
    //driver info ui
    private SmartImageView driverImage;
    private ImageView imageSex;
    private TextView ageView;
    private TextView driverName;
    private TextView password;
    private TextView time;
    private TextView location;
    private TextView carInfo;
    private TextView distance;
    private TextView carLogo;
    private TextView carName;
    private TextView carColor;
    private SmartImageView carImage;
    //others
    private SharedPreferences sharedPreferences;

	/*
	 *method part
	 */




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.act_pass_order_success);

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
    private void initControls() {
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });
        orderDetailButton = (Button)findViewById(R.id.to_detail);
        orderDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to Order list
                gotoOrderList();
            }
        });
        initOrderDetail();

    }

    private void initOrderDetail() {
        time = (TextView)findViewById(R.id.time);
        location = (TextView)findViewById(R.id.location);
        carInfo = (TextView)findViewById(R.id.car_info);
        distance = (TextView)findViewById(R.id.distance);
        password = (TextView)findViewById(R.id.password);
        password.setText(getIntent().getStringExtra("passwords"));
        driverImage = (SmartImageView)findViewById(R.id.img_photo);
        driverImage.isCircular = true;
	    driverImage.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_user_img);
		    }
	    });

        imageSex = (ImageView)findViewById(R.id.imgSex);
        ageView = (TextView)findViewById(R.id.lblAge);
        driverName = (TextView)findViewById(R.id.driver_name);
        carLogo = (TextView)findViewById(R.id.car_logo);
        carName =(TextView)findViewById(R.id.car_name);
        carColor =(TextView)findViewById(R.id.car_color);

        carImage = (SmartImageView)findViewById(R.id.img_car);
        carImage.isCircular = true;
	    carImage.setImage(new SmartImage() {
		    @Override
		    public Bitmap getBitmap(Context context) {
			    return BitmapFactory.decodeResource(getResources(), R.drawable.default_car_img);
		    }
	    });

        setDriverUIInfo();
    }

    private void setDriverUIInfo() {
        sharedPreferences = getSharedPreferences("single_order_detail", Context.MODE_PRIVATE);
        driverImage.setImageUrl(sharedPreferences.getString("img", ""), R.drawable.default_user_img);
        if(0 == sharedPreferences.getInt("gender",-1)){
            imageSex.setImageResource(R.drawable.bk_manmark);
        } else {
            imageSex.setImageResource(R.drawable.bk_womanmark);
        }
        ageView.setText(""+sharedPreferences.getInt("age",0));
        driverName.setText(sharedPreferences.getString("name",""));
        carLogo.setText(sharedPreferences.getString("brand", ""));
        carName.setText(sharedPreferences.getString("style", ""));
        carColor.setText(sharedPreferences.getString("color", ""));
        carImage.setImageUrl(sharedPreferences.getString("carimg", ""), R.drawable.default_car_img);
        time.setText(sharedPreferences.getString("start_time", "")+" 出发");
        location.setText(sharedPreferences.getString("start_addr", ""));
        carInfo.setText(Global.getConcealedCarNo(getApplication(), sharedPreferences.getString("carno", "")));
        distance.setText(sharedPreferences.getString("dist", ""));
    }


    private void onClickBack()
    {
        finishWithAnimation();
    }

    private void gotoOrderList() {
        Intent intent = new Intent(PassOnceOrderSuccessActivity.this, PassMainActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        intent.putExtra(PassMainActivity.TAB_EXTRA_NAME, PassMainActivity.TAB_DINGDAN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PassOnceOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
        finish();
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

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQCODE_TEST)
            ;
    }



}

