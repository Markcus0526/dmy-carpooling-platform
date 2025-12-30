package com.damytech.PincheApp;

import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.damytech.DataClasses.ConstData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-9
 * Time: 下午5:05
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackActivity extends SuperActivity
{
	private ImageButton btn_back = null;
	private Button btn_send = null;
	private EditText edt_contents = null;

	private String szPreContent = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.act_feedback);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		edt_contents = (EditText)findViewById(R.id.edt_content);
		edt_contents.setText(Global.loadFeedbackContents(getApplicationContext()));
		edt_contents.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (edt_contents.getText().toString().length() >= 1000) {
					Global.showAdvancedToast(FeedbackActivity.this,
							getResources().getString(R.string.STR_CANNOT_INPUTMORE),
							Gravity.CENTER);
					edt_contents.setText(szPreContent);
					edt_contents.setSelection(szPreContent.length());
					return;
				} else {
					szPreContent = edt_contents.getText().toString();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		btn_send = (Button)findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickSend();
			}
		});
	}

	private void onClickBack()
	{
		finishWithAnimation();
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


	private void onClickSend()
	{
		if (edt_contents.getText().toString().equals(""))
		{
			Global.showAdvancedToast(FeedbackActivity.this, getResources().getString(R.string.STR_FEEDBACK_INPUTCONTENT), Gravity.CENTER);
			edt_contents.requestFocus();
			edt_contents.selectAll();
			return;
		}

		if (edt_contents.getText().toString().length() > 1000)
		{
			Global.showAdvancedToast(FeedbackActivity.this, getResources().getString(R.string.STR_FEEDBACK_CONTENTLEN), Gravity.CENTER);
			edt_contents.requestFocus();
			edt_contents.selectAll();
			return;
		}

		startProgress();
		CommManager.advanceOpinion(Global.loadUserID(getApplicationContext()),
				edt_contents.getText().toString(),
				Global.getIMEI(getApplicationContext()),
				submit_handler);
	}

	private AsyncHttpResponseHandler submit_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(FeedbackActivity.this, getResources().getString(R.string.STR_FEEDBACK_SENDSUCCESS), Gravity.CENTER);
					Global.saveFeedbackContents(getApplicationContext(), "");

					edt_contents.setText("");
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(FeedbackActivity.this, szRetmsg, Gravity.CENTER);
					Global.saveFeedbackContents(getApplicationContext(), edt_contents.getText().toString());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			Global.showAdvancedToast(FeedbackActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
			Global.saveFeedbackContents(getApplicationContext(), edt_contents.getText().toString());
		}
	};

}
