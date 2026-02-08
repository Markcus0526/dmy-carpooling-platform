package com.damytech.PincheApp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.Animation;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResizeAnimation;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;

public class EvaluateActivity extends SuperActivity
{
	final int MODE_GOOD = 1;
	final int MODE_NORMAL = 2;
	final int MODE_BAD = 3;

	int nMode = MODE_GOOD;

	public static final int FROM_PASSENGER = 1;
	public static final int FROM_DRIVER = 2;

	boolean bExpandable = true;

	ImageView imgGood, imgGoodIcon;
	ImageView imgNormal, imgNormalIcon;
	ImageView imgBad, imgBadIcon;
	ImageView imgExpand;
	TextView lblGood, lblNormal, lblBad;
	EditText edtContent = null;
	Button btnCancel, btnOk;
	TextView txtTitle = null;

	RelativeLayout content_layout1 = null, content_layout2 = null;
	TextView txtMsg = null;

	private long drvID = 0;
	private long psgID = 0;
	private int order_type = 0;
	private long orderid = 0;
	private int fromMode = FROM_DRIVER;
	private boolean viewMode = false;
	private int level = ConstData.EVALUATE_GOOD;
	private String szMsg = "";

	private RelativeLayout commWordsLayout = null;
	private LinearLayout commWordsContent = null;

	private ArrayList<String> arrCommWords = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_evaluate);

		initControl();
		initExtras();
		initResolution();
	}


	private void initControl()
	{
		imgGood = (ImageView) findViewById(R.id.imgGoodEval);
		imgGood.setOnClickListener(onClickListener);
		imgGoodIcon = (ImageView) findViewById(R.id.imgGoodEvalIcon);
		imgGoodIcon.setOnClickListener(onClickListener);
		lblGood = (TextView) findViewById(R.id.lblGoodEval);
		lblGood.setOnClickListener(onClickListener);
		imgNormal = (ImageView) findViewById(R.id.imgNormalEval);
		imgNormal.setOnClickListener(onClickListener);
		imgNormalIcon = (ImageView) findViewById(R.id.imgNormalEvalIcon);
		imgNormalIcon.setOnClickListener(onClickListener);
		lblNormal = (TextView) findViewById(R.id.lblNormalEval);
		lblNormal.setOnClickListener(onClickListener);
		imgBad = (ImageView) findViewById(R.id.imgBadEval);
		imgBad.setOnClickListener(onClickListener);
		imgBadIcon = (ImageView) findViewById(R.id.imgBadEvalIcon);
		imgBadIcon.setOnClickListener(onClickListener);
		lblBad = (TextView) findViewById(R.id.lblBadEval);
		lblBad.setOnClickListener(onClickListener);

		imgExpand = (ImageView) findViewById(R.id.imgCommonWordExpand);
		imgExpand.setOnClickListener(onClickListener);

		btnCancel = (Button) findViewById(R.id.btnCanCel);
		btnCancel.setOnClickListener(onClickListener);
		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(onClickListener);

		commWordsLayout = (RelativeLayout)findViewById(R.id.relativeLayout4);
		commWordsContent = (LinearLayout)findViewById(R.id.commWordsCont);

		edtContent = (EditText)findViewById(R.id.txtEvalData);

		content_layout1 = (RelativeLayout)findViewById(R.id.content_layout);
		content_layout2 = (RelativeLayout)findViewById(R.id.content_layout2);

		txtMsg = (TextView)findViewById(R.id.txt_eval_content);

		RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
		parent_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
	}


	private void initExtras()
	{
		Intent intent = getIntent();
		drvID = intent.getLongExtra("driver", 0);
		psgID = intent.getLongExtra("passenger", 0);
		order_type = intent.getIntExtra("order_type", ConstData.ORDER_TYPE_ONCE);
		orderid = intent.getLongExtra("orderid", 0);

		fromMode = intent.getIntExtra("from_mode", FROM_DRIVER);
		viewMode = intent.getBooleanExtra("view_mode", false);
		level = intent.getIntExtra("level", -1);
		szMsg = intent.getStringExtra("msg");

		if (viewMode)
		{
//			commWordsLayout.setVisibility(View.GONE);
//			edtContent.setEnabled(false);
//			edtContent.setText(szMsg);
//			imgExpand.setVisibility(View.INVISIBLE);
//
//			nMode = level;
//			showRadio();
			content_layout1.setVisibility(View.INVISIBLE);
			content_layout2.setVisibility(View.VISIBLE);

			txtMsg.setText(szMsg);

            imgGood.setOnClickListener(null);
            imgGoodIcon.setOnClickListener(null);
            lblGood.setOnClickListener(null);
            imgNormal.setOnClickListener(null);
            imgNormalIcon.setOnClickListener(null);
            lblNormal.setOnClickListener(null);
            imgBad.setOnClickListener(null);
            imgBadIcon.setOnClickListener(null);
            lblBad.setOnClickListener(null);
		}
		else
		{
			content_layout1.setVisibility(View.VISIBLE);
			content_layout2.setVisibility(View.INVISIBLE);
		}


		txtTitle = (TextView)findViewById(R.id.txt_title);

		if (fromMode == FROM_DRIVER)
		{
			arrCommWords.add(getResources().getString(R.string.STR_PASS_EXPRESSION1));
            arrCommWords.add(getResources().getString(R.string.STR_PASS_EXPRESSION2));
            arrCommWords.add(getResources().getString(R.string.STR_PASS_EXPRESSION3));
            arrCommWords.add(getResources().getString(R.string.STR_PASS_EXPRESSION4));

			txtTitle.setText(getResources().getString(R.string.STR_PINGJIACHENGKE));
		}
		else
		{
            arrCommWords.add(getResources().getString(R.string.STR_DRV_EXPRESSION1));
            arrCommWords.add(getResources().getString(R.string.STR_DRV_EXPRESSION2));
            arrCommWords.add(getResources().getString(R.string.STR_DRV_EXPRESSION3));
            arrCommWords.add(getResources().getString(R.string.STR_DRV_EXPRESSION4));

			txtTitle.setText(getResources().getString(R.string.STR_PINGJIACHEZHU));
		}

		final int nMaxWidth = 440;
		final int nBtnWidth = 195;
		final int nBtnHeight = 40;
		final int BTN_TOP = 10;
        final int BTN_LEFT = 20;

		RelativeLayout itemLayout = null;
		for (int i = 0; i < arrCommWords.size(); i++)
		{
			if (i % 2 == 0)
			{
				itemLayout = new RelativeLayout(commWordsContent.getContext());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, nBtnHeight + BTN_TOP + BTN_TOP / 2);
				itemLayout.setLayoutParams(params);
			}

			Button btnItem = createCommWordButton(arrCommWords.get(i), itemLayout, nBtnWidth, nBtnHeight);
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)btnItem.getLayoutParams();

			if (i % 2 == 0)             // Left button
				layoutParams.setMargins(BTN_LEFT, BTN_TOP, 0, 0);
			else
				layoutParams.setMargins(nBtnWidth + BTN_LEFT + 10, BTN_TOP, 0, 0);

			itemLayout.addView(btnItem);

			if (i % 2 == 0)
				commWordsContent.addView(itemLayout);
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



	View.OnClickListener onClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
				case R.id.imgGoodEval:
				case R.id.imgGoodEvalIcon:
				case R.id.lblGoodEval:
					nMode = MODE_GOOD;
                    if (bExpandable == false)
                        onClickMoreWords();
					break;
				case R.id.imgNormalEval:
				case R.id.imgNormalEvalIcon:
				case R.id.lblNormalEval:
					nMode = MODE_NORMAL;
                    if (bExpandable == true)
                        onClickMoreWords();
					break;
				case R.id.imgBadEval:
				case R.id.imgBadEvalIcon:
				case R.id.lblBadEval:
					nMode = MODE_BAD;
                    if (bExpandable == true)
                        onClickMoreWords();
					break;
				case R.id.imgCommonWordExpand:
					onClickMoreWords();
					break;
				case R.id.btnCanCel:
					finish();
					break;
				case R.id.btnOk:
					onClickEvaluate();
					break;
			}

			showRadio();
		}
	};

	private void showRadio()
	{
		switch(nMode)
		{
			case MODE_GOOD:
				imgGood.setImageResource(R.drawable.radiobox_roundsel);
				imgNormal.setImageResource(R.drawable.radiobox_roundnormal);
				imgBad.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case MODE_NORMAL:
				imgGood.setImageResource(R.drawable.radiobox_roundnormal);
				imgNormal.setImageResource(R.drawable.radiobox_roundsel);
				imgBad.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case MODE_BAD:
				imgGood.setImageResource(R.drawable.radiobox_roundnormal);
				imgNormal.setImageResource(R.drawable.radiobox_roundnormal);
				imgBad.setImageResource(R.drawable.radiobox_roundsel);
				break;
		}
	}


	private void onClickEvaluate()
	{
		if (viewMode == true)
        {
			finishWithAnimation();
            return;
        }

		if (nMode != ConstData.EVALUATE_GOOD && edtContent.getText().toString().equals(""))
		{
			Global.showAdvancedToast(EvaluateActivity.this, getResources().getString(R.string.STR_CONTENTEMPTY), Gravity.CENTER);
			edtContent.requestFocus();
			Global.showKeyboardFromText(edtContent, getApplicationContext());
			return;
		}

		startProgress();

		if (fromMode == FROM_DRIVER)
		{
			if (order_type == ConstData.ORDER_TYPE_ONCE)
			{
				CommManager.evaluateOnceOrderPass(drvID, psgID, orderid, nMode, edtContent.getText().toString(), Global.getIMEI(getApplicationContext()), eval_handler);
			}
			else if (order_type == ConstData.ORDER_TYPE_ONOFF)
			{
				CommManager.evaluateOnOffOrderPass(drvID, psgID, orderid, nMode, edtContent.getText().toString(), Global.getIMEI(getApplicationContext()), eval_handler);
			}
			else if (order_type == ConstData.ORDER_TYPE_LONG)
			{
				CommManager.evaluateLongOrderPass(drvID, psgID, orderid, nMode, edtContent.getText().toString(), Global.getIMEI(getApplicationContext()), eval_handler);
			}
			else
			{
				stopProgress();
			}
		}
		else if (fromMode == FROM_PASSENGER)
		{
			if (order_type == ConstData.ORDER_TYPE_ONCE)
			{
				CommManager.evaluateOnceOrderDriver(psgID, drvID, orderid, nMode, edtContent.getText().toString(), Global.getIMEI(getApplicationContext()), eval_handler);
			}
			else if (order_type == ConstData.ORDER_TYPE_ONOFF)
			{
				CommManager.evaluateOnOffOrderDriver(psgID, drvID, orderid, nMode, edtContent.getText().toString(), Global.getIMEI(getApplicationContext()), eval_handler);
			}
			else if (order_type == ConstData.ORDER_TYPE_LONG)
			{
				CommManager.evaluateLongOrderDriver(psgID, drvID, orderid, nMode, edtContent.getText().toString(), Global.getIMEI(getApplicationContext()), eval_handler);
			}
			else
			{
				stopProgress();
			}
		}
	}


	private Button createCommWordButton(final String szWord, RelativeLayout parent, int nWidth, int nHeight)
	{
		Button btnItem = new Button(parent.getContext());
		RelativeLayout.LayoutParams btnLayout = new RelativeLayout.LayoutParams(nWidth, nHeight);
		btnItem.setLayoutParams(btnLayout);
		btnItem.setBackgroundResource(R.drawable.btn_smallwhite);
		btnItem.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
		btnItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, nHeight / 2);
		btnItem.setText(szWord);
		btnItem.setPadding(0, 0, 0, 0);
		btnItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				edtContent.setText(edtContent.getText().toString() + szWord);
			}
		});
		return btnItem;
	}


	private AsyncHttpResponseHandler eval_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetCode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetCode == ConstData.ERR_CODE_NONE)
				{
//					JSONObject retdata = result.getJSONObject("retdata");
					Global.showAdvancedToast(EvaluateActivity.this, getResources().getString(R.string.STR_SUCCESS), Gravity.CENTER);
					setResult(RESULT_OK);
					finish();
				}
				else if (nRetCode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(EvaluateActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}


		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(EvaluateActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private void onClickMoreWords()
	{
		final int nWidth = commWordsLayout.getWidth();
		final int nMaxHeight = 120;
		final int nMinHeight = 0;
		final int nDuration = 300;

		if (bExpandable)
		{
			ResizeAnimation resizeAnimation = new ResizeAnimation(commWordsLayout, nWidth, nMinHeight, nWidth, nMaxHeight);
			resizeAnimation.setDuration(nDuration);
			resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					bExpandable = false;
					imgExpand.setImageResource(R.drawable.bk_uparrow);
				}
				@Override
				public void onAnimationEnd(Animation animation){
					commWordsLayout.clearAnimation();

					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) commWordsLayout.getLayoutParams();
					layoutParams.height = (int)(nMaxHeight * ResolutionSet.instance.getYPro());
					commWordsLayout.setLayoutParams(layoutParams);
					commWordsLayout.setTag(R.string.TAG_KEY_HEIGHT, "" + (int)(nMaxHeight / ResolutionSet.instance.getYPro()));
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}
			});
			resizeAnimation.setFillAfter(true);
			commWordsLayout.startAnimation(resizeAnimation);
		}
		else
		{
			ResizeAnimation resizeAnimation = new ResizeAnimation(commWordsLayout, nWidth, nMaxHeight, nWidth, nMinHeight);
			resizeAnimation.setDuration(nDuration);
			resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					bExpandable = true;
					imgExpand.setImageResource(R.drawable.bk_downarraw);
				}
				@Override
				public void onAnimationEnd(Animation animation){
					commWordsLayout.clearAnimation();

					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) commWordsLayout.getLayoutParams();
					layoutParams.height = 0;
					commWordsLayout.setLayoutParams(layoutParams);
					commWordsLayout.setTag(R.string.TAG_KEY_HEIGHT, "0");
				}
				@Override
				public void onAnimationRepeat(Animation animation) {}
			});
			resizeAnimation.setFillAfter(true);
			commWordsLayout.startAnimation(resizeAnimation);
		}
	}


}
