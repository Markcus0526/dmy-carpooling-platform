package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.DecimalDigitsInputFilter;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

/**
 * 拼友主界面
 */
public class PassUpPriceActivity extends SuperActivity
{
    /*
     *static references part
     */
    //log tag
    private static final String TAG = "erik_debug";
    //time tags
    private static final int FIRSTTIME = 1;
    private static final int SECONDTIME = 2;
    private static final int THIRDTIME = 3;
    private static final int FOURTHTIME = 4;
    private static final int FIFTHTIME = 5;

    //应用状态码
    private int REQCODE_TEST = 0;
    private int SEND_WITHOUT_MONEY = 1;

    //UI instances
    private Button sendButton;
    private EditText priceTxt;
    private ImageView decreasePriceBtn;
    private ImageView increasePriceBtn;
    private Button cancelOrder;
    private TextView tipText;


    //others
    private static final String NUL_STR = "";
    private double money;
    private long orderId;
    private int waitTime;
    private int cost;
    private int interval;
    private int average;
    private int driverNum;
    private float minPrice;




	/*
	 *method part
	 */




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.act_pass_up_price);

        initControls();//控件示例化
        initResolution();//适配屏幕

    }

    @Override
    protected void onResume() {
        //Log.d(TAG,"onResume................");
        getCostAndAccountDetail();
        sendButton.setClickable(true);
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        //cancelOrder();
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
        driverNum =  getIntent().getIntExtra("driverNum", 0);
        cancelOrder = (Button)findViewById(R.id.btn_cancel_order);
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
            }
        });
        sendButton = (Button)findViewById(R.id.btn_send_again);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
                sendButton.setClickable(false);
            }
        });
        tipText = (TextView)findViewById(R.id.tip1);
        initPirceSetting();
        getCostAndAccountDetail();
        getOrderDetail();
    }

    private void cancelDialog() {
        String msg = "不急，我们继续为您寻找，若有车主接单，会有短消息通知您。";
        CommonAlertDialog alertDialog = new CommonAlertDialog.Builder(PassUpPriceActivity.this)
                .type(CommonAlertDialog.DIALOGTYPE_ALERT)
                .positiveTitle("知道了")
                .message(msg)
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoMain();

                    }
                })
                .build();
        alertDialog.show();
    }

    private void lastSendOrder(){
        CommManager.changeOnceOrderPrice(Global.loadUserID(getApplicationContext()),
                orderId, waitTime, Double.parseDouble(priceTxt.getText().toString()),
                Global.getIMEI(getApplicationContext()), null);
        gotoMain();
    }

    private void gotoMain(){
        Intent mIntent = new Intent(this, PassMainActivity.class);
        startActivity(mIntent);
        mIntent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        PassUpPriceActivity.this.finish();
    }
    private void getOrderDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("wait_time_list", Context.MODE_PRIVATE);
        orderId = sharedPreferences.getLong("order_id", 0);
    }

    private void cancelOrder() {
        //server communicate
        CommManager.cancelOnceOrder(Global.loadUserID(getApplicationContext()), orderId,
		        Global.getIMEI(getApplicationContext()), cancelOrderHandler);
    }

    private AsyncHttpResponseHandler cancelOrderHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");
                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                //Log.d(TAG, "code:message ------->"+ nRetcode+":"+jsonMsg);
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    //JSONObject retdata = jsonResult.getJSONObject("retdata");
                    Log.d(TAG,"success.....................");
                    finish();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassUpPriceActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassUpPriceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            cancelDialog();
        }
        return true;
    }

    private void getCostAndAccountDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("single_order_data", Context.MODE_PRIVATE);
        cost = Math.round(sharedPreferences.getFloat("cost_detail", 0));
        interval = Math.round(sharedPreferences.getFloat("price_interval", 0));
        average = Math.round(sharedPreferences.getFloat("average_price", 0));
        minPrice = sharedPreferences.getFloat("min_price", 0);
        priceTxt.setText(""+cost);
        if(cost < average){
            tipText.setText("平台推荐价："+average+"点/次");
        }else{
            tipText.setText("平台推荐价："+(cost+interval)+"点/次");
        }
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
                    saveUsefulData();
                    if(money < (Double.parseDouble(priceTxt.getText().toString())-cost)){
                        Intent intent = new Intent(PassUpPriceActivity.this, PassNoCounterActivity.class);
                        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                        PassUpPriceActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                        startActivityForResult(intent, SEND_WITHOUT_MONEY);
                        finish();
                    }else{
                        publishOrderAgain();
                    }

                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassUpPriceActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassUpPriceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void saveUsefulData() {
        SharedPreferences sharedPreferences = getSharedPreferences("single_order_data", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putFloat("cost_detail", (float) Double.parseDouble(priceTxt.getText().toString()));
        editor.commit();
    }

    /*
     * go to PassWaitForOrderActivity
     */


    private void sendOrder() {
        if(infoCheck()){
            //money = 1;//no counter debug
            //Log.d(TAG,"money----->"+money);
            getMoney();
        }else{
            sendButton.setClickable(true);
        }

    }

    private void setWaitTimeAndOrderId() {
        Global.addPublishCounter();
        //get time storage
        SharedPreferences sharedPreferences = getSharedPreferences("wait_time_list", Context.MODE_PRIVATE);
        switch (Global.publishCounter){
            case FIRSTTIME:
                if(Double.parseDouble(priceTxt.getText().toString()) > cost){
                    waitTime = sharedPreferences.getInt("add_price_time1",0);
                }else{
                    waitTime = sharedPreferences.getInt("same_price_time1",0);
                }
                break;
            case SECONDTIME:
                if(Double.parseDouble(priceTxt.getText().toString()) > cost){
                    waitTime = sharedPreferences.getInt("add_price_time2",0);
                }else{
                    waitTime = sharedPreferences.getInt("same_price_time2",0);
                }
                break;
            case THIRDTIME:
                if(Double.parseDouble(priceTxt.getText().toString()) > cost){
                    waitTime = sharedPreferences.getInt("add_price_time3",0);
                }else{
                    waitTime = sharedPreferences.getInt("same_price_time3",0);
                }
                break;
            case FOURTHTIME:
                if(Double.parseDouble(priceTxt.getText().toString()) > cost){
                    waitTime = sharedPreferences.getInt("add_price_time4",0);
                }else{
                    waitTime = sharedPreferences.getInt("same_price_time4",0);
                }
                break;
            case FIFTHTIME:
                if(Double.parseDouble(priceTxt.getText().toString()) > cost){
                    waitTime = sharedPreferences.getInt("add_price_time5",0);
                }else{
                    waitTime = sharedPreferences.getInt("same_price_time5",0);
                }
                break;
            default:
                if(Double.parseDouble(priceTxt.getText().toString()) > cost){
                    waitTime = sharedPreferences.getInt("add_price_time5",0);
                }else{
                    waitTime = sharedPreferences.getInt("same_price_time5",0);
                }
                break;
        }
        //set OrderId
        orderId = sharedPreferences.getLong("order_id",0);
    }

    private void publishOrderAgain() {

        setWaitTimeAndOrderId();
        //server communicate
        if(Double.parseDouble(priceTxt.getText().toString()) > cost){
            CommManager.changeOnceOrderPrice(Global.loadUserID(getApplicationContext()),
                    orderId, waitTime, Double.parseDouble(priceTxt.getText().toString()),
                    Global.getIMEI(getApplicationContext()), repushishHandler);
        }else{
            finish();
        }

    }

    private AsyncHttpResponseHandler repushishHandler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);
            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject jsonResult = jsonObj.getJSONObject("result");

                int nRetcode = jsonResult.getInt("retcode");
                String jsonMsg = jsonResult.getString("retmsg");
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONObject retdata = jsonResult.getJSONObject("retdata");
                    Log.d(TAG,"success.....................");
                    gotoWaitInterface();
                    finish();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(jsonMsg);
                }
                else
                {
                    Global.showAdvancedToast(PassUpPriceActivity.this, jsonMsg, Gravity.CENTER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
	        Global.showAdvancedToast(PassUpPriceActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void gotoWaitInterface() {
        Intent intent = new Intent(PassUpPriceActivity.this, PassWaitOnceOrderAcceptanceAgainActivity.class);
        intent.putExtra("driverNum",driverNum);
        intent.putExtra("wait_time", waitTime);
        intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
        PassUpPriceActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
        startActivity(intent);
    }

    /*
     *check time, startInput, endInput, PriceInput
     */
    private boolean infoCheck() {

        if((priceTxt.getText().toString()).equals(NUL_STR)){
            Global.showAdvancedToast(PassUpPriceActivity.this,
                    getResources().getString(R.string.STR_TOAST_NOPRICEMESSAGE), Gravity.CENTER);
            return false;
        }


        double price = Double.parseDouble(priceTxt.getText().toString());
        if (price < minPrice)
        {
            String szMsg = getResources().getString(R.string.STR_PRICE_TOOLOW);
            szMsg += Math.round(minPrice);
            szMsg += getResources().getString(R.string.STR_BALANCE_DIAN);

            priceTxt.selectAll();
            priceTxt.requestFocus();

            CommonAlertDialog dialog = new CommonAlertDialog.Builder(PassUpPriceActivity.this)
                    .message(szMsg)
                    .type(CommonAlertDialog.DIALOGTYPE_ALERT)
                    .build();
            dialog.show();

            return false;
        }

        return true;
    }
    /*
     *price settings
     */
    private void initPirceSetting() {

        priceTxt = (EditText)findViewById(R.id.lblPriceVal);
        priceTxt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3, 2)});
        //设定显示两位小数(9999.99-0.01)
//        priceTxt.addTextChangedListener(new TextWatcher() {
//            private boolean isChanged = false;
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //debugging
//                String temp = s.toString();
//                int posDot = temp.indexOf(".");
//                Log.d(TAG, "string:posDot-------->" + temp + ":" + posDot);
//                if (posDot <= 0){
//                    if(temp.length()<=4){
//                        return;
//                    }else{
//                        s.delete(4, 5);
//                        return;
//                    }
//                }else if (posDot == 1){
//                    if(temp.length()<=6){
//                        return;
//                    }else{
//                        s.delete(6, 7);
//                        return;
//                    }
//
//                }else if (posDot == 2){
//                    if(temp.length()<=7){
//                        return;
//                    }else{
//                        s.delete(7, 8);
//                        return;
//                    }
//
//                }
//                if (temp.length() - posDot - 1 > 2)
//                {
//                    s.delete(posDot + 3, posDot + 4);
//                };
//
//            }
//        });
        decreasePriceBtn = (ImageView)findViewById(R.id.imgPriceMinus);
        decreasePriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceTxt.clearFocus();
                decreasePrice();
            }
        });
        increasePriceBtn = (ImageView)findViewById(R.id.imgPricePlus);
        increasePriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceTxt.clearFocus();
                increasePrice();
            }
        });
    }

    private void increasePrice() {
        if(priceTxt.getText().toString().equals("")){
            priceTxt.setText(""+1);
        }else{
            int originPrice = Integer.parseInt(priceTxt.getText().toString());
            if (originPrice < 10000) {
                priceTxt.setText(Integer.toString(originPrice + 1));
            }
        }
    }

    private void decreasePrice() {
        if(priceTxt.getText().toString().equals("")){
            priceTxt.setText(""+1);
        }else{
            int originPrice = Integer.parseInt(priceTxt.getText().toString());
            if(originPrice <= average+1){
                Global.showAdvancedToast(PassUpPriceActivity.this,"不能再低了，车主也不容易呢。", Gravity.CENTER);
                return;
            }
            if (originPrice>1) {
                priceTxt.setText(Integer.toString(originPrice-1));
            }
        }

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
     * 恢复应用时状态匹配
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQCODE_TEST)
            ;
    }



}

