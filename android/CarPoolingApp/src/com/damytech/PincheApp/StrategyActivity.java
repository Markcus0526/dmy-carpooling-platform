package com.damytech.PincheApp;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.damytech.Utils.ResolutionSet;

/**
 * Created with IntelliJ IDEA.
 * User: KimHM
 * Date: 14-8-22
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
public class StrategyActivity extends SuperActivity
{
	private ImageButton btn_back = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_strategy);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
        WebView wvStrategy = (WebView)findViewById(R.id.wb_strategy);
        wvStrategy.loadUrl("file:///android_asset/strategy.html");
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
