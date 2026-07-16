package com.damytech.PincheApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.damytech.Misc.Global;
import com.damytech.PincheApp.adapter.GuidePageAdapter;
import android.widget.LinearLayout.LayoutParams;
import com.damytech.Utils.mutil.SharedPreferenceUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/12/12.
 */
public class GuideActivity extends SuperActivity {
    private ViewPager guideViewPager;
    private ImageButton ibtJump;
    private ArrayList<View> imageList;
    private SharedPreferenceUtil spUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        findView();
        setView();
    }

    private void findView() {
        spUtil = SharedPreferenceUtil.getInstance(this);
        guideViewPager = (ViewPager) findViewById(R.id.vp_guide_viewpager);
        ibtJump = (ImageButton) findViewById(R.id.ibt_jump);
        imageList = new ArrayList<View>();

    }

    private void setView() {
        imageList.add(addView(R.drawable.guide1));
        imageList.add(addView(R.drawable.guide2));
        imageList.add(addView(R.drawable.guide3));
        imageList.add(addView(R.drawable.guide4));
        imageList.add(addView(R.drawable.guidelast));

        ibtJump.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                gotoLogin();
            }
        });

        guideViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(imageList != null && imageList.size() - 1 == position){
                    ibtJump.setVisibility(View.VISIBLE);
                }else{
                    ibtJump.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        GuidePageAdapter adapter = new GuidePageAdapter(imageList, this);
        guideViewPager.setAdapter(adapter);


    }

    private void gotoLogin(){
        spUtil.saveBoolean("firstcome",false);
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

        this.finish();
    }

    private View addView(int desid){
        ImageView view = new ImageView(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setImageResource(desid);

        return view;
    }
}
