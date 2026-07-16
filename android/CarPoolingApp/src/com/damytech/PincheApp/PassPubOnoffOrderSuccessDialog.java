package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;

public class PassPubOnoffOrderSuccessDialog extends Dialog {
    Context mContext;
    Button btnOk;
    Button btnCancel;
    OnDismissListener dismissListener = null;

    int nRes = 0;

    public PassPubOnoffOrderSuccessDialog(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_pass_pub_onoff_success);

        Point ptScreen = Global.getScreenSize(mContext.getApplicationContext());
        ResolutionSet.instance.iterateChild(((RelativeLayout)findViewById(R.id.parent_layout)).getChildAt(0), ptScreen.x, ptScreen.y);

        initControl();
    }

    private void initControl()
    {
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btnOk:
                    nRes = 1;
                    dismissListener.onDismiss(PassPubOnoffOrderSuccessDialog.this);
                    dismiss();
                    break;
            }
        }
    };

    public void setOnDismissListener(OnDismissListener listener)
    {
        dismissListener = listener;
    }

    public int IsDeleted()
    {
        return nRes;
    }
}
