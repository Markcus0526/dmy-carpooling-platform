package com.damytech.PincheApp;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STCommonRoute;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 拼友主界面
 */
public class PassLongSetRouteActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
    double fStartLon = 0.0f, fStartLat = 0.0f;
    double fEndLon = 0.0f, fEndLat = 0.0f;

    private int nYear = 0, nMonth = 0, nDay = 0;
    private String strStartCity = "", strEndCity = "";

    private String szStartDate = "";

    final int FIND_START_ADDR = 0, FIND_END_ADDR = 1;

    int nIsUpdated = 0;
    STCommonRoute stRoute = new STCommonRoute();

    ImageButton btn_back = null;
    Button btnCancel = null;
    Button btnSave = null;
    ImageView imgDate = null;
    TextView lblDate = null, lblDateVal = null;
    ImageView imgMicStart = null, imgMicEnd = null;
    TextView lblStartPos = null, lblEndPos = null;

    EditText txtStartCity = null;
    EditText txtEndCity = null;

    RecognizerDialog dlgRec = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_pass_long_set_route);

        initControls();
        initResolution();

        nIsUpdated = getIntent().getIntExtra("IsUpdate", 0);
        if (nIsUpdated == 1)
        {
            stRoute = getIntent().getParcelableExtra("Data");

            if (stRoute != null)
            {
                lblStartPos.setText(stRoute.startaddr);
                lblEndPos.setText(stRoute.endaddr);
                fStartLon = stRoute.startlon;
                fStartLat = stRoute.startlat;
                fEndLon = stRoute.endlon;
                fEndLat = stRoute.endlat;
                szStartDate = stRoute.create_time;
                lblDateVal.setText(stRoute.create_time);
                strStartCity = stRoute.startcity;
                strEndCity = stRoute.endcity;
                txtStartCity.setText(strStartCity);
                txtEndCity.setText(strEndCity);
            }
        }
    }


    private void initControls()
    {
        dlgRec = new RecognizerDialog(this, "appid=50e1b967");

        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(onClickListener);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);

        lblDate = (TextView) findViewById(R.id.lblDate);
        lblDate.setOnClickListener(onClickListener);
        lblDateVal = (TextView) findViewById(R.id.lblDateVal);
        lblDateVal.setOnClickListener(onClickListener);
        imgDate = (ImageView) findViewById(R.id.imgDate);
        imgDate.setOnClickListener(onClickListener);

        imgMicStart = (ImageView) findViewById(R.id.imgMicStart);
        imgMicStart.setOnClickListener(onClickListener);
        imgMicEnd = (ImageView) findViewById(R.id.imgMicEnd);
        imgMicEnd.setOnClickListener(onClickListener);

        lblStartPos = (TextView) findViewById(R.id.lblStartPosVal);
        lblStartPos.setOnClickListener(onClickListener);
        lblEndPos = (TextView) findViewById(R.id.lblEndPosVal);
        lblEndPos.setOnClickListener(onClickListener);

        txtStartCity = (EditText) findViewById(R.id.txtStartCity);
        txtEndCity = (EditText) findViewById(R.id.txtEndCity);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final StringBuilder sb = new StringBuilder();

            switch (v.getId())
            {
                case R.id.btnCancel:
                    finishWithAnimation();
                    break;
                case R.id.btnSave:
                    if (nIsUpdated == 0)
                        uploadRoute();
                    else
                        updateRoute();
                    break;
                case R.id.lblDate:
                case R.id.lblDateVal:
                {
                    WheelDatePickerDlg dlgtimepicker = new WheelDatePickerDlg(PassLongSetRouteActivity.this, true);
                    dlgtimepicker.setOnDismissListener(PassLongSetRouteActivity.this);
	                dlgtimepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                Date dtCur = new Date();
	                try {
		                dtCur = sdf.parse(szStartDate);
	                } catch (Exception ex) {
		                ex.printStackTrace();
	                }
	                dlgtimepicker.setCurDate(dtCur);

                    dlgtimepicker.show();
                    break;
                }
                case R.id.imgDate:
                {
                    WheelDatePickerDlg dlgtimepicker = new WheelDatePickerDlg(PassLongSetRouteActivity.this ,false);
                    dlgtimepicker.setOnDismissListener(PassLongSetRouteActivity.this);
	                dlgtimepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlgtimepicker.show();
                    break;
                }
                case R.id.imgMicStart:
                    strStartCity = txtStartCity.getText().toString();
                    if (strStartCity.length() == 0)
                    {
                        Global.showAdvancedToast(PassLongSetRouteActivity.this, getString(R.string.STR_INSERT_STARTCITY), Gravity.CENTER);
                        return;
                    }
                    dlgRec.setEngine("sms", null, null);
                    dlgRec.setSampleRate(SpeechConfig.RATE.rate16k);
                    dlgRec.setListener(new RecognizerDialogListener() {
                        @Override
                        public void onResults(ArrayList<RecognizerResult> result, boolean b) {
                            for (RecognizerResult recognizerResult : result)
                            {
                                sb.append(recognizerResult.text);
                            }
                        }

                        @Override
                        public void onEnd(SpeechError speechError) {
                            if (speechError != null)        return;

                            String strAddr = Global.eatChinesePunctuations(sb.toString());
                            Intent intentStart = new Intent(PassLongSetRouteActivity.this, TextAddrSearchActivity.class);
                            intentStart.putExtra("City", strStartCity);
                            intentStart.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
                            intentStart.putExtra("Pos", strAddr);
                            intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                            PassLongSetRouteActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                            startActivityForResult(intentStart, FIND_START_ADDR);
                        }
                    });
	                dlgRec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlgRec.show();
                    break;
                case R.id.imgMicEnd:
                    strEndCity = txtEndCity.getText().toString();
                    if (strEndCity.length() == 0)
                    {
                        Global.showAdvancedToast(PassLongSetRouteActivity.this, getString(R.string.STR_INSERT_ENDCITY), Gravity.CENTER);
                        return;
                    }
                    dlgRec.setEngine("sms", null, null);
                    dlgRec.setSampleRate(SpeechConfig.RATE.rate16k);
                    dlgRec.setListener(new RecognizerDialogListener() {
                        @Override
                        public void onResults(ArrayList<RecognizerResult> result, boolean b) {
                            for (RecognizerResult recognizerResult : result)
                            {
                                sb.append(recognizerResult.text);
                            }
                        }

                        @Override
                        public void onEnd(SpeechError speechError) {
                            String strAddr = Global.eatChinesePunctuations(sb.toString());
                            Intent intentEnd = new Intent(PassLongSetRouteActivity.this, TextAddrSearchActivity.class);
                            intentEnd.putExtra("City", strEndCity);
                            intentEnd.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
                            intentEnd.putExtra("Pos", strAddr);
                            intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                            PassLongSetRouteActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                            startActivityForResult(intentEnd, FIND_END_ADDR);
                        }
                    });
	                dlgRec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlgRec.show();
                    break;
                case R.id.lblStartPosVal:
                    strStartCity = txtStartCity.getText().toString();
                    if (strStartCity.length() == 0)
                    {
                        Global.showAdvancedToast(PassLongSetRouteActivity.this, getString(R.string.STR_INSERT_STARTCITY), Gravity.CENTER);
                        return;
                    }
                    Intent intentStart = new Intent(PassLongSetRouteActivity.this, TextAddrSearchActivity.class);
                    intentStart.putExtra("City", strStartCity);
                    intentStart.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
                    intentStart.putExtra("Pos", "");
                    intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    PassLongSetRouteActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivityForResult(intentStart, FIND_START_ADDR);
                    break;
                case R.id.lblEndPosVal:
                    strEndCity = txtEndCity.getText().toString();
                    if (strEndCity.length() == 0)
                    {
                        Global.showAdvancedToast(PassLongSetRouteActivity.this, getString(R.string.STR_INSERT_ENDCITY), Gravity.CENTER);
                        return;
                    }
                    Intent intentEnd = new Intent(PassLongSetRouteActivity.this, TextAddrSearchActivity.class);
                    intentEnd.putExtra("City", strEndCity);
                    intentEnd.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
                    intentEnd.putExtra("Pos", "");
                    intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    PassLongSetRouteActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivityForResult(intentEnd, FIND_END_ADDR);
                    break;
            }
        }
    };

    private void uploadRoute()
    {
        if (txtStartCity.getText().toString().equals(""))
        {
            txtStartCity.requestFocus();
            txtStartCity.selectAll();
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTCITY_EMPTY), Gravity.CENTER);
            Global.showKeyboardFromText(txtStartCity, getApplicationContext());
            return;
        }

        if (txtEndCity.getText().toString().equals(""))
        {
            txtEndCity.requestFocus();
            txtEndCity.selectAll();
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_ENDCITY_EMPTY), Gravity.CENTER);
            Global.showKeyboardFromText(txtEndCity, getApplicationContext());
            return;
        }

        if(txtEndCity.getText().toString().equals(txtStartCity.getText().toString()))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_STARTENDCITY_CANNOT_EQUAL), Gravity.CENTER);
            return;
        }


        if (lblStartPos.getText().toString().equals(""))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTADDR_EMPTY), Gravity.CENTER);
            return;
        }

        if (lblEndPos.getText().toString().equals(""))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_ENDADDR_EMPTY), Gravity.CENTER);
            return;
        }

        if (szStartDate.equals(""))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTTIME_EMPTY), Gravity.CENTER);
            return;
        }

        String strStartCity = txtStartCity.getText().toString();
        String strEndCity = txtEndCity.getText().toString();
        String strStartPos = lblStartPos.getText().toString();
        String strEndPos = lblEndPos.getText().toString();

        startProgress();
        CommManager.addRoute(Global.loadUserID(getApplicationContext()),
                1,
                1,
                strStartCity,
                strEndCity,
                strStartPos,
                strEndPos,
                fStartLat,
                fStartLon,
                fEndLat,
                fEndLon,
                //Global.loadCityName(getApplicationContext()),
                "沈阳",
                szStartDate,
                Global.getIMEI(getApplicationContext()),
                addroute_handler);

        return;
    }

    private void updateRoute()
    {
        if (txtStartCity.getText().toString().equals(""))
        {
            txtStartCity.requestFocus();
            txtStartCity.selectAll();
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTCITY_EMPTY), Gravity.CENTER);
            Global.showKeyboardFromText(txtStartCity, getApplicationContext());
            return;
        }

        if (txtEndCity.getText().toString().equals(""))
        {
            txtEndCity.requestFocus();
            txtEndCity.selectAll();
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_ENDCITY_EMPTY), Gravity.CENTER);
            Global.showKeyboardFromText(txtEndCity, getApplicationContext());
            return;
        }

        if (lblStartPos.getText().toString().equals(""))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTADDR_EMPTY), Gravity.CENTER);
            return;
        }

        if (lblEndPos.getText().toString().equals(""))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_ENDADDR_EMPTY), Gravity.CENTER);
            return;
        }

        if (szStartDate.equals(""))
        {
            Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTTIME_EMPTY), Gravity.CENTER);
            return;
        }

        String strStartCity = txtStartCity.getText().toString();
        String strEndCity = txtEndCity.getText().toString();
        String strStartPos = lblStartPos.getText().toString();
        String strEndPos = lblEndPos.getText().toString();

        startProgress();
        CommManager.changeRoute(Global.loadUserID(getApplicationContext()),
                stRoute.uid,
                1,
                1,
                strStartCity,
                strEndCity,
                strStartPos,
                strEndPos,
                fStartLat,
                fStartLon,
                fEndLat,
                fEndLon,
                //Global.loadCityName(getApplicationContext()),
                "沈阳",
                szStartDate,
                Global.getIMEI(getApplicationContext()),
                updateroute_handler);

        return;
    }

    private AsyncHttpResponseHandler addroute_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            stopProgress();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");
                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_COMMONROUTEADD_SUCCESSADD), Gravity.CENTER);
                    finishWithAnimation();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(PassLongSetRouteActivity.this, szRetmsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            stopProgress();
	        Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private AsyncHttpResponseHandler updateroute_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            stopProgress();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");
                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_COMMONROUTEADD_SUCCESSUPDATE), Gravity.CENTER);
                    finishWithAnimation();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(PassLongSetRouteActivity.this, szRetmsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            stopProgress();
	        Global.showAdvancedToast(PassLongSetRouteActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

	    if (resultCode != RESULT_OK)
		    return;

        if (requestCode == FIND_START_ADDR)
        {
            lblStartPos.setText(data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME));
            fStartLon = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0.0f);
            fStartLat = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0.0f);
        }
        else if (requestCode == FIND_END_ADDR)
        {
            lblEndPos.setText(data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME));
            fEndLon = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0.0f);
            fEndLat = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0.0f);
        }
    }

    /********************************************** Picker Dialog Dismiss ************************************/
    @Override
    public void onDismiss(DialogInterface dialog) {

        if (dialog.getClass() == WheelDatePickerDlg.class)
        {
            WheelDatePickerDlg convDlg = (WheelDatePickerDlg) dialog;

            nYear = convDlg.getYear();
            nMonth = convDlg.getMonth();
            nDay = convDlg.getDay();

            String strDateTime = String.format("%04d-%02d-%02d", nYear, nMonth, nDay);
            lblDateVal.setText(strDateTime);

            szStartDate = String.format("%04d-%02d-%02d", nYear, nMonth, nDay);
        }
    }

}

