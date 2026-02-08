package com.damytech.PincheApp;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;

/**
 * Created with IntelliJ IDEA.
 * User: KimHM
 * Date: 14-8-21
 * Time: 上午12:18
 * To change this template use File | Settings | File Templates.
 */
public class FirstActivity extends SuperActivity
{
	private Button btn_driver = null, btn_pass = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_first_layout);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		btn_driver = (Button)findViewById(R.id.btn_driver);
		btn_driver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickDriver();
			}
		});

		btn_pass = (Button)findViewById(R.id.btn_passenger);
		btn_pass.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickPassenger();
			}
		});
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

	private void onClickDriver()
	{
		Intent intent = new Intent(FirstActivity.this, DrvMainActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		startActivity(intent);
		FirstActivity.this.finish();
	}

	private void onClickPassenger()
	{
        Intent intent = new Intent(FirstActivity.this, PassMainActivity.class);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        startActivity(intent);
        FirstActivity.this.finish();
	}
}
