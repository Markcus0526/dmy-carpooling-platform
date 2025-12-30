package com.damytech.PincheApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.baidu.navisdk.util.common.DateTimeUtils;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.DecimalDigitsInputFilter;
import com.damytech.Utils.ResolutionSet;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DrvPubLongOrderActivity extends SuperActivity
{
	private final int ADD_MILLISECONDS = 15 * 60 * 1000;

	private double fStartLng = 0.0f, fStartLat = 0.0f;
	private double fEndLng = 0.0f, fEndLat = 0.0f;
	private int nYear = 0, nMonth = 0, nDay = 0, nHour = 0, nMinute = 0;
	private String strStartCity = "", strEndCity = "";

	private ImageButton btn_back = null;
	private Button btnDate = null, btnSend = null;
	private String szStartDate = "";
	private TextView lblStartPos = null, lblEndPos = null, lblInfoFee = null;
	private EditText txtStartCity = null, txtEndCity = null, edtSeats = null, edtPrice = null;
	private EditText txtComment = null;
	private ImageView imgSeatPlus = null, imgSeatMinus = null;
	private ImageView imgPricePlus = null, imgPriceMinus = null;
	private ImageButton btnStartCity = null, btnEndCity = null;
    private TextView insuranceFeeTxt;

	private int nSeats = 1;
	private int nPrice = 100;
	private final int FIND_START_ADDR = 0;
	private final int FIND_END_ADDR = 1;

	// System info fee calc method.
	private int calcMethod = 0;
	private double infoFeeVal = 0;
    private double insuranceFee;

    private final int MAX_SEATCNT = 30;
    private final int MAX_PRICE = 9999;

	private RecognizerDialog dlgRec = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_publongorder);

		initControls();
		initResolution();
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DrvMainActivity.isNoGetOrder = true;
    }

    private void initControls()
	{
        DrvMainActivity.isNoGetOrder = false;
		Calendar cal_cur = Calendar.getInstance();
		cal_cur.add(Calendar.MILLISECOND, ADD_MILLISECONDS);
		nYear = cal_cur.get(Calendar.YEAR);
		nMonth = cal_cur.get(Calendar.MONTH) + 1;
		nDay = cal_cur.get(Calendar.DAY_OF_MONTH);
		nHour = cal_cur.get(Calendar.HOUR_OF_DAY);
		nMinute = cal_cur.get(Calendar.MINUTE);

		dlgRec = new RecognizerDialog(this, "appid=50e1b967");

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		btnDate = (Button) findViewById(R.id.btnDate);
		btnDate.setOnClickListener(onClickListener);
		btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(onClickListener);

		imgSeatPlus = (ImageView) findViewById(R.id.imgSeatPlus);
		imgSeatPlus.setOnClickListener(onClickListener);
		imgSeatMinus = (ImageView) findViewById(R.id.imgSeatMinus);
		imgSeatMinus.setOnClickListener(onClickListener);
		imgPricePlus = (ImageView) findViewById(R.id.imgPricePlus);
		imgPricePlus.setOnClickListener(onClickListener);
		imgPriceMinus = (ImageView) findViewById(R.id.imgPriceMinus);
		imgPriceMinus.setOnClickListener(onClickListener);
		btnStartCity = (ImageButton) findViewById(R.id.btnStartCity);
		btnStartCity.setOnClickListener(onClickListener);
		btnEndCity = (ImageButton)findViewById(R.id.btnEndCity);
		btnEndCity.setOnClickListener(onClickListener);

		txtStartCity = (EditText) findViewById(R.id.txtStartCity);
		txtStartCity.setText(Global.loadCityName(getApplicationContext()));
        txtStartCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // clear detailed start address
                lblStartPos.setText("");
                fStartLng = 0;
                fStartLat = 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

		fStartLat = Global.loadLatitude(getApplicationContext());
		fStartLng = Global.loadLongitude(getApplicationContext());

		txtEndCity = (EditText) findViewById(R.id.txtEndCity);
        txtEndCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // clear detailed end address
                lblEndPos.setText("");
                fEndLng = 0;
                fEndLat = 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
		txtComment = (EditText) findViewById(R.id.txtComment);

		lblStartPos = (TextView) findViewById(R.id.lblStartPosVal);
		lblStartPos.setText(Global.loadDetAddress(getApplicationContext()));
		lblStartPos.setOnClickListener(onClickListener);

		lblEndPos = (TextView) findViewById(R.id.lblEndPosVal);
		lblEndPos.setOnClickListener(onClickListener);

		lblInfoFee = (TextView)findViewById(R.id.lblSitePrice);
        insuranceFeeTxt = (TextView)findViewById(R.id.lblInsurance);

		edtSeats = (EditText) findViewById(R.id.lblSeatVal);
        edtSeats.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String charSeats = (charSequence.toString().trim() == null || "".equals(charSequence.toString().trim()) ? "0" : charSequence.toString().trim());
                if(30 < Integer.parseInt(charSeats)){
                    Global.showAdvancedToast(DrvPubLongOrderActivity.this,
                            getResources().getString(R.string.STR_LONGDISTANCE_SEATS_MAX), Gravity.CENTER);
                }
                if(0 >= Integer.parseInt(charSeats)){
                    Global.showAdvancedToast(DrvPubLongOrderActivity.this,
                            getResources().getString(R.string.STR_LONGDISTANCE_SEATS_ZERO), Gravity.CENTER);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
		edtPrice = (EditText)findViewById(R.id.lblPriceVal);
        edtPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3, 2)});
		edtPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
                int fPrice = 1;
                try {
                    fPrice = Integer.parseInt(edtPrice.getText().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fPrice = 1;
                    edtPrice.setText("" + fPrice);
                }

                // check max/min price
                if (fPrice < 1)
                {
                    fPrice = 1;
                    edtPrice.setText("" + fPrice);
                }
                else if (fPrice > Global.PRICE_MAX_LIMIT())
                {
                    fPrice = 9999;
                    edtPrice.setText("" + fPrice);
                }

				double fInfoFee = 0;
				if (calcMethod == 2)
					fInfoFee = fPrice * infoFeeVal / 100;

				lblInfoFee.setText(String.format("%.1f", fInfoFee));
			}
			@Override
			public void afterTextChanged(Editable s) {}
		});


		startProgress();
		CommManager.getInfoFeeCalcMethod(Global.loadUserID(getApplicationContext()), Global.loadCityName(getApplicationContext()), Global.getIMEI(getApplicationContext()), sysInfoFee_handler);
	}


	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			final StringBuilder sb = new StringBuilder();

			switch(v.getId())
			{
				case R.id.btnDate:
					showDateTimeDialog();
					break;
				case R.id.btnSend:
					publishOrder();
					break;
				case R.id.imgSeatMinus:
                    try {
                        nSeats = Integer.parseInt(edtSeats.getText().toString());
                    } catch (Exception ex) {}

					if ( nSeats <= 1 )
						return;
					else
					{
						nSeats--;
						edtSeats.setText(Integer.toString(nSeats));
					}
					break;
				case R.id.imgSeatPlus:
                    try {
                        nSeats = Integer.parseInt(edtSeats.getText().toString());
                    } catch (Exception ex) {}

					if (nSeats >= MAX_SEATCNT)
						return;
					else
					{
						nSeats++;
						edtSeats.setText(Integer.toString(nSeats));
					}
					break;
				case R.id.imgPriceMinus:
                    try {
                        nPrice = Integer.parseInt(edtPrice.getText().toString());
                    } catch (Exception ex) {}

					if ( nPrice <= 5 )
						return;
					else
					{
						nPrice-=5;
						edtPrice.setText(Integer.toString(nPrice));
					}
					break;
				case R.id.imgPricePlus:
                    try {
	                    nPrice = Integer.parseInt(edtPrice.getText().toString());
                    } catch (Exception ex) {}

					if ( nPrice >= MAX_PRICE-5 )
						return;
					else
					{
						nPrice+=5;
						edtPrice.setText(Integer.toString(nPrice));
					}
					break;
				case R.id.btnStartCity:
					strStartCity = txtStartCity.getText().toString();
					if (strStartCity.length() == 0)
					{
						Global.showAdvancedToast(DrvPubLongOrderActivity.this, getString(R.string.STR_INSERT_STARTCITY), Gravity.CENTER);
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
							Intent intentStart = new Intent(DrvPubLongOrderActivity.this, TextAddrSearchActivity.class);
							intentStart.putExtra("City", strStartCity);
							intentStart.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
							intentStart.putExtra("Pos", strAddr);
							intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
							DrvPubLongOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
							startActivityForResult(intentStart, FIND_START_ADDR);
						}
					});
					dlgRec.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dlgRec.show();
					break;
				case R.id.btnEndCity:
					strEndCity = txtEndCity.getText().toString();
					if (strEndCity.length() == 0)
					{
						Global.showAdvancedToast(DrvPubLongOrderActivity.this, getString(R.string.STR_INSERT_ENDCITY), Gravity.CENTER);
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
							Intent intentEnd = new Intent(DrvPubLongOrderActivity.this, TextAddrSearchActivity.class);
							intentEnd.putExtra("City", strEndCity);
							intentEnd.putExtra("Mode", TextAddrSearchActivity.SPEECH_FIND);
							intentEnd.putExtra("Pos", strAddr);
							intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
							DrvPubLongOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
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
						Global.showAdvancedToast(DrvPubLongOrderActivity.this, getString(R.string.STR_INSERT_STARTCITY), Gravity.CENTER);
						return;
					}

					Intent intentStart = new Intent(DrvPubLongOrderActivity.this, TextAddrSearchActivity.class);
					intentStart.putExtra("City", strStartCity);
					intentStart.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
					intentStart.putExtra("Pos", "");
					intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
					DrvPubLongOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					startActivityForResult(intentStart, FIND_START_ADDR);
					break;
				case R.id.lblEndPosVal:
					strEndCity = txtEndCity.getText().toString();
					if (strEndCity.length() == 0)
					{
						Global.showAdvancedToast(DrvPubLongOrderActivity.this, getString(R.string.STR_INSERT_ENDCITY), Gravity.CENTER);
						return;
					}
					Intent intentEnd = new Intent(DrvPubLongOrderActivity.this, TextAddrSearchActivity.class);
					intentEnd.putExtra("City", strEndCity);
					intentEnd.putExtra("Mode", TextAddrSearchActivity.TEXT_FIND);
					intentEnd.putExtra("Pos", "");
					intentEnd.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
					DrvPubLongOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
					startActivityForResult(intentEnd, FIND_END_ADDR);
					break;
			}
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

	private void showDateTimeDialog() {
		WheelDateTimePickerDlg dlgtimepicker = new WheelDateTimePickerDlg(DrvPubLongOrderActivity.this);

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, nYear);
		cal.set(Calendar.MONTH, nMonth - 1);
		cal.set(Calendar.DAY_OF_MONTH, nDay);
		cal.set(Calendar.HOUR_OF_DAY, nHour);
		cal.set(Calendar.MINUTE, nMinute);

		dlgtimepicker.setCurDate(cal.getTime());
		dlgtimepicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				WheelDateTimePickerDlg convDlg = (WheelDateTimePickerDlg) dialog;

				nYear = convDlg.getYear();
				nMonth = convDlg.getMonth();
				nDay = convDlg.getDay();
				nHour = convDlg.getHour();
				nMinute = convDlg.getMinute();

				String strDateTime = String.format("%04d-%02d-%02d %02d:%02d", nYear, nMonth, nDay, nHour, nMinute);
				btnDate.setText(strDateTime);

				szStartDate = String.format("%04d-%02d-%02d %2d:%02d", nYear, nMonth, nDay, nHour, nMinute);
			}
		});
        dlgtimepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dlgtimepicker.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == FIND_START_ADDR) {
			lblStartPos.setText(data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME));
			fStartLng = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0.0f);
			fStartLat = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0.0f);
		} else if (requestCode == FIND_END_ADDR) {
			lblEndPos.setText(data.getStringExtra(TextAddrSearchActivity.OUT_EXTRA_PLACENAME));
			fEndLng = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LONGITUDE, 0.0f);
			fEndLat = data.getDoubleExtra(TextAddrSearchActivity.OUT_EXTRA_LATITUDE, 0.0f);
		}
	}

    private boolean checkCityName()
    {
        String sStartCity = txtStartCity.getText().toString();
        String sEndCity = txtEndCity.getText().toString();
        if (sStartCity.equals(""))
        {
            txtStartCity.requestFocus();
            txtStartCity.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTCITY_EMPTY), Gravity.CENTER);
            Global.showKeyboardFromText(txtStartCity, getApplicationContext());
            return false;
        }

        if (sEndCity.equals(""))
        {
            txtEndCity.requestFocus();
            txtEndCity.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_ENDCITY_EMPTY), Gravity.CENTER);
            Global.showKeyboardFromText(txtEndCity, getApplicationContext());
            return false;
        }

        if (sStartCity.length() > sEndCity.length())
        {
            sEndCity += getString(R.string.cheng_shi);
        }
        else if (sStartCity.length() < sEndCity.length())
        {
            sStartCity += getString(R.string.cheng_shi);
        }

        // check difference of name
        if (sStartCity.equals(sEndCity))
        {
            txtEndCity.requestFocus();
            txtEndCity.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_CITYSAME), Gravity.CENTER);
            Global.showKeyboardFromText(txtEndCity, getApplicationContext());
            return false;
        }


        return true;
    }

	private void publishOrder()
	{
		if (!checkCityName())
            return;

		if (lblStartPos.getText().toString().equals(""))
		{
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTADDR_EMPTY), Gravity.CENTER);
			return;
		}

		if (lblEndPos.getText().toString().equals(""))
		{
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_ENDADDR_EMPTY), Gravity.CENTER);
			return;
		}

		if (edtSeats.getText().toString().equals(""))
		{
			edtSeats.requestFocus();
			edtSeats.selectAll();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_SEATSCOUNT_EMPTY), Gravity.CENTER);
			Global.showKeyboardFromText(edtSeats, getApplicationContext());
			return;
		}

        if (edtSeats.getText().toString().equals("0")||edtSeats.getText().toString().equals("00"))
        {
            edtSeats.requestFocus();
            edtSeats.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_SEATS_ZERO), Gravity.CENTER);
            Global.showKeyboardFromText(edtSeats, getApplicationContext());
            return;
        }

        if (30 < Integer.parseInt(edtSeats.getText().toString().trim()))
        {
            edtSeats.requestFocus();
            edtSeats.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_SEATS_MAX), Gravity.CENTER);
            Global.showKeyboardFromText(edtSeats, getApplicationContext());
            return;
        }

		if (edtPrice.getText().toString().equals(""))
		{
			edtPrice.requestFocus();
			edtPrice.selectAll();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_PRICE_EMPTY), Gravity.CENTER);
			Global.showKeyboardFromText(edtPrice, getApplicationContext());
			return;
		}

        if (edtPrice.getText().toString().equals("0"))
        {
            edtPrice.requestFocus();
            edtPrice.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_PRICE_ZERO), Gravity.CENTER);
            Global.showKeyboardFromText(edtPrice, getApplicationContext());
            return;
        }

		try {
            nPrice = Integer.parseInt(edtPrice.getText().toString());
		} catch (Exception ex) {
			edtPrice.requestFocus();
			edtPrice.selectAll();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_PRICE_FORMATERROR), Gravity.CENTER);
			Global.showKeyboardFromText(edtPrice, getApplicationContext());
			ex.printStackTrace();
			return;
		}

        try {
            nSeats = Integer.parseInt(edtSeats.getText().toString());
        } catch (Exception ex) {
            edtSeats.requestFocus();
            edtSeats.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_SEATS_FORMATERROR), Gravity.CENTER);
            Global.showKeyboardFromText(edtSeats, getApplicationContext());
            ex.printStackTrace();
            return;
        }

		if (nSeats > 30)
		{
			edtSeats.requestFocus();
			edtSeats.selectAll();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_SEATS_TOOMUCH), Gravity.CENTER);
			Global.showKeyboardFromText(edtSeats, getApplicationContext());
			return;
		}

		if (szStartDate.equals(""))
		{
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_STARTTIME_EMPTY), Gravity.CENTER);
			return;
		}

		if (nSeats > Global.SEATS_MAX_LIMIT())
		{
			edtSeats.requestFocus();
			edtSeats.selectAll();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_SEATS_OVER100), Gravity.CENTER);
			Global.showKeyboardFromText(edtSeats, getApplicationContext());
			return;
		}

        if (nPrice > Global.PRICE_MAX_LIMIT() || nPrice < 1)
        {
            edtPrice.requestFocus();
            edtPrice.selectAll();
            Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_LONGDISTANCE_PRICE_OVERFLOW), Gravity.CENTER);
            Global.showKeyboardFromText(edtPrice, getApplicationContext());
            return;
        }


		Date dtVal = Global.String2Date(szStartDate);
		if (dtVal == null)
		{
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_FAILURE), Gravity.CENTER);
			return;
		}

        // make tomorrow date
        Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_MONTH, 1);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		Date tomorrow = cal.getTime();
		if (dtVal.before(cal.getTime()))
		{
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_TOAST_LATEMESSAGE), Gravity.CENTER);
			return;
		}


		startProgress();
		CommManager.publishLongOrder(Global.loadUserID(getApplicationContext()),
													lblStartPos.getText().toString(), txtStartCity.getText().toString(), fStartLat, fStartLng,
													lblEndPos.getText().toString(), txtEndCity.getText().toString(), fEndLat, fEndLng,
													szStartDate,
													txtComment.getText().toString(),
													nPrice,
													nSeats,
													Global.getIMEI(getApplicationContext()),
													publish_handler);
	}


	private AsyncHttpResponseHandler sysInfoFee_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try
			{
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					calcMethod = retdata.getInt("calc_method");
					infoFeeVal = retdata.getDouble("value");
                    nPrice = Integer.parseInt(edtPrice.getText().toString());
                    double fInfoFee = nPrice * infoFeeVal / 100;
                    lblInfoFee.setText(String.format("%.1f", fInfoFee));
                    insuranceFee = retdata.getDouble("insu_fee");
                    insuranceFeeTxt.setText("" + insuranceFee);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvPubLongOrderActivity.this, szRetMsg, Gravity.CENTER);
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler publish_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");
				int nRetcode = result.getInt("retcode");
				String szMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)		  // Success
				{
					CommonAlertDialog dialog = new CommonAlertDialog.Builder(DrvPubLongOrderActivity.this)
							.type(CommonAlertDialog.DIALOGTYPE_ALERT)
							.message(getResources().getString(R.string.publongorder_success))
							.positiveListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(DrvPubLongOrderActivity.this, DrvMainActivity.class);
									intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
									intent.putExtra(DrvMainActivity.TAB_EXTRA_NAME, DrvMainActivity.TAB_DINGDAN);
                                    intent.putExtra("order_type","long");
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									DrvPubLongOrderActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
									startActivity(intent);
									finish();
								}
							})
							.build();
					dialog.show();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szMsg);
				}
				else
				{
					Global.showAdvancedToast(DrvPubLongOrderActivity.this, szMsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvPubLongOrderActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};
}
