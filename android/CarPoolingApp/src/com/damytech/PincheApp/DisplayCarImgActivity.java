package com.damytech.PincheApp;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImageView;

/**
 * Created by RiGSNote on 14-9-29.
 */
public class DisplayCarImgActivity extends SuperActivity {
	private String carimg_url = "";
	private SmartImageView imgView = null;

	public static final String IMG_EXTRA = "img_url";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_displaycarimage);

        initControl();
	    initResolution();
    }

    private void initControl()
    {
	    carimg_url = getIntent().getStringExtra(IMG_EXTRA);

        RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
	    parent_layout.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    finish();
		    }
	    });


	    imgView = (SmartImageView)findViewById(R.id.img_detail);
	    imgView.setImageUrl(carimg_url, R.drawable.default_car_img);
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

}
