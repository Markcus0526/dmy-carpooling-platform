package com.damytech.MainApp;

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

public class WithDrawFinishDialog extends Dialog
{
    Context mContext;
    Button btnOk;
    Button btnCancel;

    int nIsDeleted = 0;
    OnDismissListener dismissListener = null;

    public WithDrawFinishDialog(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_driver_withdrawfinish);

        Point ptScreen = Global.getScreenSize(mContext.getApplicationContext());
        ResolutionSet.instance.iterateChild(((RelativeLayout)findViewById(R.id.parent_layout)).getChildAt(0), ptScreen.x, ptScreen.y);

        initControl();
    }

    private void initControl()
    {
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(onClickListener);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(onClickListener);
    }

    public void setOnDismissListener(OnDismissListener listener)
    {
        dismissListener = listener;
    }

    public int IsDeleted()
    {
        return nIsDeleted;
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btnOk:
                    nIsDeleted = 1;
                    dismissListener.onDismiss(WithDrawFinishDialog.this);
                    dismiss();
                    break;
                case R.id.btnCancel:
                    nIsDeleted = 0;
                    dismissListener.onDismiss(WithDrawFinishDialog.this);
                    WithDrawFinishDialog.this.dismiss();
                    break;
            }
        }
    };
}