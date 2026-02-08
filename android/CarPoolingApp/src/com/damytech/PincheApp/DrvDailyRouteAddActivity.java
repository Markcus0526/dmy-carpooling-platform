package com.damytech.PincheApp;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DrvDailyRouteAddActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
    double fStartLon = 0.0f, fStartLat = 0.0f;
    double fEndLon = 0.0f, fEndLat = 0.0f;

    int nHour = -1, nMinute = -1;
    final int FIND_START_ADDR = 0;
	final int FIND_END_ADDR = 111;
    final int WORKDATE_MODE = 1;
    final int WEEKEND_MODE = 2;
    final int ALLDAY_MODE = 3;
    int SEL_MODE = WORKDATE_MODE;

    int nIsUpdated = 0;
    STCommonRoute stRoute = new STCommonRoute();

	ImageButton btn_back = null;
    Button btnCancel = null;
    Button btnSave = null;
    ImageView imgDate = null;
    TextView lblDate = null, lblDateVal = null;
    ImageView imgWork = null, imgWeek = null, imgAll = null;
    TextView lblWork = null, lblWeek = null, lblAll = null;
    ImageView imgMicStart = null, imgMicEnd = null;
    TextView lblStartPos = null, lblEndPos = null;
	ImageButton btnStartPos = null, btnEndPos = null;
    GregorianCalendar calendar = new GregorianCalendar();

    RecognizerDialog dlgRec = null;

    private String ConvertToTime2(int hour, int minute)
    {
        String strRet = "";
        if (hour > 9)
            strRet = strRet + Integer.toString(hour);
        else
            strRet = strRet + "0" + Integer.toString(hour);

        strRet = strRet + "点 ";

        if (minute > 9)
            strRet = strRet + Integer.toString(minute) + "分";
        else
            strRet = strRet + "0" + Integer.toString(minute) + "分";

        return strRet;
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            nHour = hourOfDay;
            nMinute = minute;

            lblDateVal.setText(ConvertToTime2(nHour, nMinute));
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_adddailyroute);

		initControls();
		initResolution();

        nIsUpdated = getIntent().getIntExtra("IsUpdate", 0);
        if (nIsUpdated == 1)
        {
            stRoute = getIntent().getParcelableExtra("Data");

            if (stRoute != null)
            {
                try
                {
                    lblStartPos.setText(stRoute.startaddr);
                    lblEndPos.setText(stRoute.endaddr);
                    fStartLon = stRoute.startlon;
                    fStartLat = stRoute.startlat;
                    fEndLon = stRoute.endlon;
                    fEndLat = stRoute.endlat;
                    lblDateVal.setText(stRoute.create_time);
                    String[] timees = stRoute.create_time.split("-");
                    nHour = Integer.parseInt(timees[0]);
                    nMinute = Integer.parseInt(timees[1]);
                    switch (stRoute.daytype)
                    {
                        case 1:
                            SEL_MODE = WORKDATE_MODE;
                            break;
                        case 2:
                            SEL_MODE = WEEKEND_MODE;
                            break;
                        case 3:
                            SEL_MODE = ALLDAY_MODE;
                            break;
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                showRadio();
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

        imgWork = (ImageView) findViewById(R.id.imgWorkDay);
        imgWork.setOnClickListener(onClickListener);
        lblWork = (TextView) findViewById(R.id.lblWorkDay);
        lblWork.setOnClickListener(onClickListener);
        imgWeek = (ImageView) findViewById(R.id.imgWeekEnd);
        imgWeek.setOnClickListener(onClickListener);
        lblWeek = (TextView) findViewById(R.id.lblWeekEnd);
        lblWeek.setOnClickListener(onClickListener);
        imgAll = (ImageView) findViewById(R.id.imgAllDay);
        imgAll.setOnClickListener(onClickListener);
        lblAll = (TextView) findViewById(R.id.lblAllDay);
        lblAll.setOnClickListener(onClickListener);

        imgMicStart = (ImageView) findViewById(R.id.imgMicStart);
        imgMicStart.setOnClickListener(onClickListener);
        imgMicEnd = (ImageView) findViewById(R.id.imgMicEnd);
        imgMicEnd.setOnClickListener(onClickListener);
        lblStartPos = (TextView) findViewById(R.id.lblStartPosVal);
        lblEndPos = (TextView) findViewById(R.id.lblEndPosVal);

		btnStartPos = (ImageButton)findViewById(R.id.btn_start_pos);
		btnEndPos = (ImageButton)findViewById(R.id.btn_end_pos);

		btnStartPos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentEnd = new Intent(DrvDailyRouteAddActivity.this, TextAddrSearchActivity.class);
				intentEnd.putExtra("City", Global.loadCityName(getApplicationContext()));
				intentEnd.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
				intentEnd.putExtra("Pos", "");
				intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvDailyRouteAddActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivityForResult(intentEnd, FIND_START_ADDR);
			}
		});

		btnEndPos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentEnd = new Intent(DrvDailyRouteAddActivity.this, TextAddrSearchActivity.class);
				intentEnd.putExtra("City", Global.loadCityName(getApplicationContext()));
				intentEnd.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
				intentEnd.putExtra("Pos", "");
				intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
				DrvDailyRouteAddActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
				startActivityForResult(intentEnd, FIND_END_ADDR);
			}
		});
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
                case R.id.imgDate:
                {
                    WheelTimePickerDlg dlgtimepicker = new WheelTimePickerDlg(DrvDailyRouteAddActivity.this);
                    dlgtimepicker.setOnDismissListener(DrvDailyRouteAddActivity.this);
	                dlgtimepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	                Calendar cal = Calendar.getInstance();
	                if (nHour < 0)
		                nHour = cal.get(Calendar.HOUR_OF_DAY);

	                if (nMinute < 0)
		                nMinute = cal.get(Calendar.MINUTE);

	                cal.set(Calendar.HOUR_OF_DAY, nHour);
	                cal.set(Calendar.MINUTE, nMinute);
	                dlgtimepicker.setCurDate(cal.getTime());

                    dlgtimepicker.show();
                    break;
                }
                case R.id.imgMicStart:
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
                            if (strAddr.length() == 0)
                                return;

                            Intent intentStart = new Intent(DrvDailyRouteAddActivity.this, TextAddrSearchActivity.class);
	                        intentStart.putExtra("City", Global.loadCityName(getApplicationContext()));
	                        intentStart.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
                            intentStart.putExtra("Pos", strAddr);
                            intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                            DrvDailyRouteAddActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                            startActivityForResult(intentStart, FIND_START_ADDR);
                        }
                    });
	                dlgRec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlgRec.show();
                    break;
                case R.id.imgMicEnd:
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
                            if (strAddr.length() == 0)
                                return;
                            Intent intentEnd = new Intent(DrvDailyRouteAddActivity.this, TextAddrSearchActivity.class);
	                        intentEnd.putExtra("City", Global.loadCityName(getApplicationContext()));
	                        intentEnd.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
                            intentEnd.putExtra("Pos", strAddr);
                            intentEnd.putExtra("StartEnd", 1);
                            intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                            DrvDailyRouteAddActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                            startActivityForResult(intentEnd, FIND_END_ADDR);
                        }
                    });
	                dlgRec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlgRec.show();
                    break;
                case R.id.imgWorkDay:
                    SEL_MODE = WORKDATE_MODE;
                    break;
                case R.id.lblWorkDay:
                    SEL_MODE = WORKDATE_MODE;
                    break;
                case R.id.imgWeekEnd:
                    SEL_MODE = WEEKEND_MODE;
                    break;
                case R.id.lblWeekEnd:
                    SEL_MODE = WEEKEND_MODE;
                    break;
                case R.id.imgAllDay:
                    SEL_MODE = ALLDAY_MODE;
                    break;
                case R.id.lblAllDay:
                    SEL_MODE = ALLDAY_MODE;
                    break;
            }

            showRadio();
        }
    };

    private void uploadRoute()
    {
        if (fStartLat == 0.0f || fStartLon == 0.0f)
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getString(R.string.STR_COMMONROUTEADD_INSERTSTARTPOS), Gravity.CENTER);
            return;
        }
        if (fEndLat == 0.0f || fEndLon == 0.0f)
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getString(R.string.STR_COMMONROUTEADD_INSERTENDPOS), Gravity.CENTER);
            return;
        }
        if (nHour == -1 || nMinute == -1)
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getString(R.string.STR_COMMONROUTEADD_INSERTTIME), Gravity.CENTER);
            return;
        }

        String strStartPos = lblStartPos.getText().toString();
        String strEndPos = lblEndPos.getText().toString();

        if(strStartPos.equals(strEndPos))
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getResources().getString(R.string.STR_STARTENDADDR_CANNOT_EQUAL), Gravity.CENTER);
            return;
        }

        String strTime = String.format("2000-01-01 %d:%d:00", nHour, nMinute);

        startProgress();
        CommManager.addRoute(Global.loadUserID(getApplicationContext()),
                2,
                SEL_MODE,
                "",
                "",
                strStartPos,
                strEndPos,
                fStartLat,
                fStartLon,
                fEndLat,
                fEndLon,
                //Global.loadCityName(getApplicationContext()),
                "沈阳",
                strTime,
                Global.getIMEI(getApplicationContext()),
                addroute_handler);

        return;
    }

    private void updateRoute()
    {
        if (fStartLat == 0.0f || fStartLon == 0.0f)
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getString(R.string.STR_COMMONROUTEADD_INSERTSTARTPOS), Gravity.CENTER);
            return;
        }
        if (fEndLat == 0.0f || fEndLon == 0.0f)
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getString(R.string.STR_COMMONROUTEADD_INSERTENDPOS), Gravity.CENTER);
            return;
        }
        if (nHour == -1 || nMinute == -1)
        {
            Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getString(R.string.STR_COMMONROUTEADD_INSERTTIME), Gravity.CENTER);
            return;
        }

        String strStartPos = lblStartPos.getText().toString();
        String strEndPos = lblEndPos.getText().toString();
        String strTime = String.format("2000-01-01 %d:%d:00", nHour, nMinute);

        startProgress();
        CommManager.changeRoute(Global.loadUserID(getApplicationContext()),
                stRoute.uid,
                2,
                SEL_MODE,
                "",
                "",
                strStartPos,
                strEndPos,
                fStartLat,
                fStartLon,
                fEndLat,
                fEndLon,
                //Global.loadCityName(getApplicationContext()),
                "沈阳",
                strTime,
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
                    Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getResources().getString(R.string.STR_COMMONROUTEADD_SUCCESSADD), Gravity.CENTER);
                    finishWithAnimation();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
	                logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(DrvDailyRouteAddActivity.this, szRetmsg, Gravity.CENTER);
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
                    Global.showAdvancedToast(DrvDailyRouteAddActivity.this, getResources().getString(R.string.STR_COMMONROUTEADD_SUCCESSUPDATE), Gravity.CENTER);
                    finishWithAnimation();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
	                logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(DrvDailyRouteAddActivity.this, szRetmsg, Gravity.CENTER);
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
        }
    };

    private void showRadio()
    {
        switch (SEL_MODE)
        {
            case WORKDATE_MODE:
                imgWork.setImageResource(R.drawable.radiobox_roundsel);
                imgWeek.setImageResource(R.drawable.radiobox_roundnormal);
                imgAll.setImageResource(R.drawable.radiobox_roundnormal);
                break;
            case WEEKEND_MODE:
                imgWork.setImageResource(R.drawable.radiobox_roundnormal);
                imgWeek.setImageResource(R.drawable.radiobox_roundsel);
                imgAll.setImageResource(R.drawable.radiobox_roundnormal);
                break;
            case ALLDAY_MODE:
                imgWork.setImageResource(R.drawable.radiobox_roundnormal);
                imgWeek.setImageResource(R.drawable.radiobox_roundnormal);
                imgAll.setImageResource(R.drawable.radiobox_roundsel);
                break;
        }
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
        if (dialog.getClass() == WheelTimePickerDlg.class)
        {
            WheelTimePickerDlg convDlg = (WheelTimePickerDlg) dialog;

            nHour = convDlg.getHour();
            nMinute = convDlg.getMinute();

            lblDateVal.setText(ConvertToTime2(nHour, nMinute));
        }

    }
}
