package com.damytech.PincheApp;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.adapter.PointsListAdapter;
import com.damytech.DataClasses.MiddlePoint;

import java.util.*;

/**
 * 设置中途点界面
 */
public class PassSetPointsActivity extends SuperActivity
{
    /*
     *static references part
     */
    //log tag
    private static final String TAG = "erik_debug";
    //应用状态码
    private final int FIND_ADDR = 0;

    //UI instances
    private ImageButton btn_back = null;
    private ListView pointList = null;
    private Button CancelButton = null;
    private Button SetButton  = null;

    //others
    PointsListAdapter mAdapter = null;

    HashMap<Integer, MiddlePoint> pointsRecord = new HashMap<Integer, MiddlePoint>() ;

	/*
	 *method part
	 */




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_pass_set_points);

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
    private void initControls()
    {
        initTitleBar();
        initContent();
    }

    private void initContent() {
        decodePointSting(getIntent().getStringExtra("points_string"));
        pointList = (ListView)findViewById(R.id.point_list);
        mAdapter = new PointsListAdapter(PassSetPointsActivity.this, pointList, pointsRecord);
        setList();
        pointList.setAdapter(mAdapter);
        CancelButton = (Button)findViewById(R.id.btn_cancel);
        CancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });
        SetButton = (Button)findViewById(R.id.btn_set);
        SetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                setPoints();

            }
        });
    }

    private void decodePointSting(String pointSting){
        String[] points = pointSting.split(",");
        if(points.length == 1) return;
        MiddlePoint p = new MiddlePoint();
        for(int i=0; i<points.length; i++){
            if(i%3 == 0){
                p.setLatitude(Double.parseDouble(points[i]));
            }else if (i%3 ==1){
                p.setLongitude(Double.parseDouble(points[i]));
            }else if (i%3 ==2){
                p.setLocation(points[i]);
                pointsRecord.put(i/3, p);
                p = new MiddlePoint();
            }
        }
    }

    private void setList() {
        if(!pointsRecord.isEmpty()) {
            //get points
            ArrayList<String> points = new ArrayList<String>();
            for (int i = 0; i < pointsRecord.size(); i++) {
               points.add(pointsRecord.get(i).getLocation());
            }
            //set list to adapter
            mAdapter.setList(points);
        }
    }

    private void setPoints() {
//        SharedPreferences sharedPreferences = getSharedPreferences("points", Context.MODE_PRIVATE); //私有数据
//        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
//        editor.clear();
//        editor.commit();
//        //save points
//        ArrayList<String> points = mAdapter.getList();
//        int counter = 0;
//        for(int i=0; i<4; i++){
//            if(!((points.get(i)).equals("中途点"+(i+1)+"详细地址"))){
//                editor.putString(""+counter, points.get(i));
//                counter++;
//            }
//        }
//
//        editor.commit();//提交修改
        StringBuilder pointsString = new StringBuilder("");
        for(MiddlePoint p : pointsRecord.values()){
            pointsString.append(p.getLatitude()+",");
            pointsString.append(p.getLongitude()+",");
            pointsString.append(p.getLocation()+",");
        }

	    String szPoints = pointsString.toString();
	    if (szPoints.length() > 0) {
		    if (szPoints.charAt(szPoints.length() - 1) == ',')
			    szPoints = szPoints.substring(0, szPoints.length() - 1);
	    }

        //跳转
        //Log.d(TAG,"----------------------------start jump");
        Intent intent = new Intent(PassSetPointsActivity.this, PassPubCityOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pointsNum", pointsRecord.size());
        bundle.putString("points_string",szPoints);
        intent.putExtras(bundle);
        PassSetPointsActivity.this.setResult(RESULT_OK, intent);
        PassSetPointsActivity.this.finish();
        //Log.d(TAG,"------------------------end jump");
    }


    private void initTitleBar() {
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });
    }

    private void onClickBack()
    {
        finishWithAnimation();
    }


	/*
	 * 适配不同屏幕
	 */

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


    /*
     * 获取地址搜索界面传过来的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

	    if (resultCode != RESULT_OK)
		    return;


        MiddlePoint mPoint = new MiddlePoint();
        mPoint.setLocation(data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME));//得到地点
        mPoint.setLatitude(data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0));
        mPoint.setLongitude(data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0));
        //更新数据
        Log.d(TAG, "requestCode------->"+requestCode);
        pointsRecord.put(requestCode, mPoint);
        mAdapter.setData(requestCode, mPoint.getLocation());
        mAdapter.notifyDataSetChanged();

    }

}

