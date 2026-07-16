package com.damytech.PincheApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import com.damytech.Misc.Global;

/**
 * Created by Administrator on 2014/12/3.
 */
public class AgreeActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treaty);
        int nIdentify = Global.loadIdentify(getApplicationContext());
        Button buttonReturn = (Button)findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AgreeActivity.this.finish();
            }
        });

        WebView webView = (WebView) findViewById(R.id.wb_treaty);
        if(nIdentify == Global.IDENTIFY_DRIVER()){
            webView.loadUrl("file:///android_asset/agreement.html");
        }else if(nIdentify == Global.IDENTIFY_PASSENGER()){
            webView.loadUrl("file:///android_asset/agreement.html");
        }

    }
}
