package com.damytech.PincheApp;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.damytech.DataClasses.ConstData;
import com.damytech.Misc.Global;
import com.damytech.PincheApp.receiver.PreUtils;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.SharedPreferenceUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-11
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class LogoActivity extends SuperActivity
{
    private static final String TAG ="erik_debug_logo";
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
    private SharedPreferenceUtil spUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_logo);

		initControls();
		initResolution();
	}

	private void initControls()
	{
        spUtil = SharedPreferenceUtil.getInstance(this);
		RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
		parent_layout.setClickable(true);
		parent_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickStart();
			}
		});

		Global.startLocationUpdate(getApplicationContext());

		finishTimer = new Timer();
		finishTimer.schedule(finishTimerTask, SHOW_DELAY);

        // bind push service (baidu tuisong)
        autoBindBaiduYunTuiSong();
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

    /**
     * 如果没有绑定百度云，则绑定，并记录在属性文件中
     */
    private void autoBindBaiduYunTuiSong()
    {
//        if (!PreUtils.isBind(getApplicationContext()))
        {
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    ConstData.PUSH_KEY);
        }
    }

	private void onClickStart()
	{
        boolean firstcome = spUtil.getBoolean("firstcome", true);
        if(firstcome){
            gotoGuide();
        }else{
            gotoMain();
        }

        LogoActivity.this.finish();
	}

    private void gotoGuide(){
        Intent intent = new Intent(LogoActivity.this, GuideActivity.class);
        startActivity(intent);
    }

    private void gotoMain(){
        int nIdentify = Global.loadIdentify(getApplicationContext());
        if (nIdentify < 0)
        {
            // First identify selecting activity is not loaded before
            Intent intent = new Intent(this, FirstActivity.class);
            intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
            startActivity(intent);
        }
        else if (nIdentify == Global.IDENTIFY_DRIVER())
        {
            // The last identify is driver. Load driver main activity
            Intent intent = new Intent(this, DrvMainActivity.class);
            intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
            startActivity(intent);
        }
        else if (nIdentify == Global.IDENTIFY_PASSENGER())
        {
            // The last identify is passenger. Load passenger main activity
            Intent intent = new Intent(this, PassMainActivity.class);
            intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
            startActivity(intent);
        }
    }

}
