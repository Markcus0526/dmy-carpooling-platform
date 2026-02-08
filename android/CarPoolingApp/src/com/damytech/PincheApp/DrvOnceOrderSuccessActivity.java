package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;

public class DrvOnceOrderSuccessActivity extends SuperActivity
{
	private ImageButton btn_back = null;

	private long pass_id = 0;
	private String pass_photo = "";
	private int pass_gender = 0;
	private int pass_age = 0;
	private String pass_name = "";
	private String start_time = "";
	private String start_addr = "";
	private String end_addr = "";

	private SmartImageView imgPhoto = null;
	private ImageButton btnPhoto = null;
	private ImageView imgSex = null;
	private TextView txtAge = null;
	private TextView txtName = null;
	private TextView txtStartTime = null;
	private TextView txtAddr = null;
	private Button btnUserOrders = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_ordersuccess);

		initControls();
		initExtras();
		initResolution();
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
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToPassInfoActivity();
			}
		});

		imgSex = (ImageView)findViewById(R.id.imgSex);
		txtAge = (TextView)findViewById(R.id.lblAge);
		txtName = (TextView)findViewById(R.id.lblName);
		txtStartTime = (TextView)findViewById(R.id.lblDateTime);
		txtAddr = (TextView)findViewById(R.id.lblDistination);

		btnUserOrders = (Button)findViewById(R.id.btnOK);
		btnUserOrders.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DrvOnceOrderSuccessActivity.this, DrvMainActivity.class);
				intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				intent.putExtra(DrvMainActivity.TAB_EXTRA_NAME, DrvMainActivity.TAB_DINGDAN);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				DrvOnceOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivity(intent);
				finish();
			}
		});
	}


	private void initExtras()
	{
		Intent intent = DrvOnceOrderSuccessActivity.this.getIntent();

		pass_id = intent.getLongExtra("pass_id", 0);
		pass_photo = intent.getStringExtra("pass_photo");
		pass_gender = intent.getIntExtra("pass_gender", 0);
		pass_age = intent.getIntExtra("pass_age", 0);
		pass_name = intent.getStringExtra("pass_name");
		start_time = intent.getStringExtra("start_time");
		start_addr = intent.getStringExtra("start_addr");
		end_addr = intent.getStringExtra("end_addr");

		initContents();
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


	private void initContents()
	{
		imgPhoto.setImageUrl(pass_photo, R.drawable.default_user_img);

		if (pass_gender == ConstData.GENDER_MALE) {
			imgSex.setImageResource(R.drawable.bk_manmark);
			txtAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
		} else {
			imgSex.setImageResource(R.drawable.bk_womanmark);
			txtAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
		}

		txtAge.setText("" + pass_age);

		txtName.setText(pass_name);

		txtStartTime.setText(start_time);

		txtAddr.setText(start_addr);
	}


	private void onClickBack()
	{
		finishWithAnimation();
	}

	private void moveToPassInfoActivity()
	{
		if (pass_id <= 0)
			return;

		Intent intent = new Intent(DrvOnceOrderSuccessActivity.this, PassEvalInfoActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		intent.putExtra("passid", pass_id);
		DrvOnceOrderSuccessActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		startActivity(intent);
	}


}
