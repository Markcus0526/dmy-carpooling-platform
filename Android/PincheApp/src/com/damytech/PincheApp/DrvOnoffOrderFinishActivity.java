package com.damytech.PincheApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;

public class DrvOnoffOrderFinishActivity extends SuperActivity
{
	private ImageButton btn_back = null;
    private SmartImageView imgPhoto = null;
    private ImageView imgRefresh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_onofforderfinish);

		initControls();
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
	}

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.imgRunCard:
                    break;
                case R.id.imgPassPhone:
                    break;
                case R.id.imgRefresh:
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
		finishWithAnimation();
	}
}
