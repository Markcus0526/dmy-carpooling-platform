package com.damytech.MainApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.damytech.Misc.Global;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-7-30
 * Time: 下午6:44
 * To change this template use File | Settings | File Templates.
 */
public class LogoActivity extends SuperActivity
{
	private final int SHOW_DELAY = 1500;        // After 3 seconds delay, automatically move to main activity.

	private TimerTask finishTimerTask = new TimerTask()
	{
		@Override
		public void run() {
			runOnUiThread(new Runnable()
			{
				@Override
				public void run() {
					onClickStart();
				}
			});
		}
	};

	private Timer finishTimer = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.act_logo);

		initControls();
		initResolution();
	}

	// Init member variables and listeners
	private void initControls()
	{
		RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
		parent_layout.setClickable(true);
		parent_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickStart();
			}
		});

		finishTimer = new Timer();
		finishTimer.schedule(finishTimerTask, SHOW_DELAY);
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

	private void onClickStart()
	{
		Intent intent = new Intent(LogoActivity.this, MainActivity.class);
		intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivity(intent);
		LogoActivity.this.finish();
	}
}