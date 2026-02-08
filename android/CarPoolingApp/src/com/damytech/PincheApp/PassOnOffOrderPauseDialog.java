package com.damytech.PincheApp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;

import java.util.Calendar;

public class PassOnOffOrderPauseDialog extends Dialog
{
    Context mContext;
    Button btnOk;
    Button btnCancel;
    TextView lblTitle;
    OnDismissListener dismissListener = null;

    int nRes = 0;

    public PassOnOffOrderPauseDialog(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_pass_commuteorderpause);

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

        lblTitle = (TextView) findViewById(R.id.lblTitle);
        Calendar calendar = Calendar.getInstance();
        int nTodayMonth = calendar.get(Calendar.MONTH) + 1;
        int nTodayDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(calendar.DATE, 1);
        int nTomorrowMonth = calendar.get(Calendar.MONTH) + 1;
        int nTomorrowDay = calendar.get(Calendar.DAY_OF_MONTH);
        lblTitle.setText(String.format(mContext.getResources().getString(R.string.STR_PASSONOFFORDERPAUSE_TITLE), nTodayMonth, nTodayDay, nTomorrowMonth, nTomorrowDay));
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
                    dismissListener.onDismiss(PassOnOffOrderPauseDialog.this);
                    dismiss();
                    break;
                case R.id.btnCancel:
                    nRes = 0;
                    PassOnOffOrderPauseDialog.this.dismiss();
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