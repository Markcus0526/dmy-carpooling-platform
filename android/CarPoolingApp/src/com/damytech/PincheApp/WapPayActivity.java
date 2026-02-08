package com.damytech.PincheApp;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STTsInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by KimHM on 2014-10-29.
 */
public class WapPayActivity extends SuperActivity {
	private String szUrl = "https://www.baifubao.com/api/0/pay/0/wapdirect?";

	private double balance = 0;
	private WebView webView = null;
	private ImageButton btnBack = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wappay);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishWithAnimation();
			}
		});
		webView = (WebView)findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Utils.mLogError("支付URL：："+url);

                if(url.contains("http://124.207.135.69:8080/PincheService/webservice/chargeWithBaidu?")){
                    String murl = url.replace("chargeWithBaidu","successIncharge");
                    Utils.mLogError("1支付URL：："+murl);
                    view.loadUrl(murl);
                }


// This operation is automatically done when pay is successfully finished and the redirect url is called.
/*
			if (balance == 0)
					return;

				String normalized_url = url.replaceAll(" ", "");
				int nIndex = normalized_url.indexOf("pay_result=1");
				if (nIndex < 0)
					return;

				nIndex = normalized_url.indexOf(Global.createChargeUrl());
				if (nIndex < 0)
					return;

				startProgress();
				CommManager.charge(Global.loadUserID(getApplicationContext()),
						balance,
						Global.getIMEI(getApplicationContext()),
						3,
						charge_handler);
*/
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
//				Global.showAdvancedToast(WapPayActivity.this, url, Gravity.CENTER);
				stopProgress();
			}
		});

		szUrl = getIntent().getStringExtra("url");
		balance = getIntent().getDoubleExtra("balance", 0);

		startProgress();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(szUrl);
	}

	private void initResolution() {
		RelativeLayout parent_layout = (RelativeLayout) findViewById(R.id.parent_layout);
		parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Point ptTemp = getScreenSize();
				boolean bNeedUpdate = false;
				if (mScrSize.x == 0 && mScrSize.y == 0) {
					mScrSize = ptTemp;
					bNeedUpdate = true;
				} else if (mScrSize.x != ptTemp.x || mScrSize.y != ptTemp.y) {
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}

				if (bNeedUpdate) {
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


	private AsyncHttpResponseHandler charge_handler = new AsyncHttpResponseHandler()
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
					Global.showAdvancedToast(WapPayActivity.this,
							getResources().getString(R.string.STR_BALANCE_CHARGE_SUCCESS),
							Gravity.CENTER);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(WapPayActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(WapPayActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

}
