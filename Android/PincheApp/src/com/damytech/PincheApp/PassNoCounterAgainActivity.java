package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

/**
 * no enough money dialog
 */
public class PassNoCounterAgainActivity extends SuperActivity
{
    /*
     *static references part
     */
    //log tag
    private static final String TAG = "erik_debug";
    //private static final int FILLIN = 0;
    //应用状态码
    private int REQCODE_TEST = 0;

    //UI instances
    private TextView accountDetail;
    private TextView costDetail;
    private Button confirmButton;
    private Button fillinButton;

    //others
    private double money;
    private double cost;
    private Long orderID;

	/*
	 *method part
	 */




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pass_no_counter);

        initControls();//控件示例化
        initResolution();//适配屏幕

    }

    @Override
    protected void onResume() {
        getMoney();
        super.onResume();
    }

    /*
         * 初始化activity中所有控件
         */
    private void initControls(){
//	    money = getIntent().getDoubleExtra("main_price", 0);
//	    cost = getIntent().getDoubleExtra("remain_balance", 0);
        orderID = getIntent().getLongExtra("order_id", 0);
        accountDetail = (TextView)findViewById(R.id.account_detail);
        costDetail = (TextView)findViewById(R.id.cost_detail);
        confirmButton = (Button)findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassNoCounterAgainActivity.this.setResult(RESULT_OK);
                PassNoCounterAgainActivity.this.finish();
            }
        });
        fillinButton = (Button)findViewById(R.id.fillin);
        fillinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickchargingbtn();
            }
        });
        getCostAndAccountDetail();
    }

    private void clickchargingbtn() {
        CommManager.clickchargingbtn(Global.loadUserID(getApplicationContext()), orderID,
                Global.getIMEI(getApplicationContext()), chargingHandler);
    }

    private AsyncHttpResponseHandler chargingHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);
            //test server stable or not
            //CommManager.getMoney(Global.loadUserID(getApplicationContext()),Global.getIMEI(getApplicationContext()),counterHandler );
            //countNum++;
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    Intent intent = new Intent(PassNoCounterAgainActivity.this, BalanceActivity.class);
                    intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    PassNoCounterAgainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivity(intent);
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassNoCounterAgainActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            Global.showAdvancedToast(PassNoCounterAgainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void getCostAndAccountDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("single_order_data", Context.MODE_PRIVATE);
        cost = sharedPreferences.getFloat("cost_detail", 0);
        costDetail.setText(""+cost);
        getMoney();
    }

    private void getMoney(){
        CommManager.getMoney(Global.loadUserID(getApplicationContext()), Global.getIMEI(getApplicationContext()), counterHandler);
    }

    private AsyncHttpResponseHandler counterHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);
            //test server stable or not
            //CommManager.getMoney(Global.loadUserID(getApplicationContext()),Global.getIMEI(getApplicationContext()),counterHandler );
            //countNum++;
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                //test server stable or not
                if(nRetcode != ConstData.ERR_CODE_NONE){
                    Log.d(TAG, "nRetcode test ----->" + nRetcode);
                }

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONObject retdata = jsonResult.getJSONObject("retdata");
                    money = retdata.getDouble("money");
                    if(money >= cost){
                        PassNoCounterAgainActivity.this.setResult(RESULT_OK);
                        PassNoCounterAgainActivity.this.finish();
                    }else{
                        accountDetail.setText(""+money);
                    }
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassNoCounterAgainActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassNoCounterAgainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

	/*
	 * 适配不同屏幕
	 */

    private void initResolution()
    {
        //erik test, maybe deep in later
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


}

