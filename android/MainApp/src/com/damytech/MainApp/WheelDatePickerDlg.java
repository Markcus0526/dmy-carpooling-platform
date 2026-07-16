package com.damytech.MainApp;

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
import com.damytech.Utils.WheelPicker.NumericWheelAdapter;
import com.damytech.Utils.WheelPicker.OnWheelChangedListener;
import com.damytech.Utils.WheelPicker.WheelView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by RiGSNote on 14-9-21.
 */
public class WheelDatePickerDlg extends Dialog {

    Context mContext;

    WheelView years;
    WheelView months;
    WheelView days;

    private Date mCurDate = null;

    int defTextSize = 20;
    OnDismissListener dismissListener = null;

    private int nYear = 0;
    private int nMonth = 0;
    private int nDay = 0;

    private final int START_YEAR = 1900;
    private final int END_YEAR = 2050;

    public WheelDatePickerDlg(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_wheel_datepicker);

        // add temp textview to get resoluted text size
        TextView temp = new TextView(mContext);
        temp.setTextSize(defTextSize);
        temp.setVisibility(View.GONE);

        RelativeLayout pL = (RelativeLayout) findViewById(R.id.parent_layout);
        pL.addView(temp);

        Point ptScreen = Global.getScreenSize(mContext.getApplicationContext());
        ResolutionSet.instance.iterateChild(((RelativeLayout)findViewById(R.id.parent_layout)).getChildAt(0), ptScreen.x, ptScreen.y);

        // get resoluted text size
        defTextSize = (int)temp.getTextSize();

        initControl();
    }

    private void initControl()
    {
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onClickListener);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(onClickListener);

        years = (WheelView) findViewById(R.id.year);
        years.setDefTextSize(defTextSize);
        years.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));
        years.setLabel(mContext.getString(R.string.STR_YEAR));

        months = (WheelView) findViewById(R.id.month);
        months.setDefTextSize(defTextSize);
        months.setAdapter(new NumericWheelAdapter(1, 12));
        months.setLabel(mContext.getString(R.string.STR_MONTH));

        days = (WheelView) findViewById(R.id.day);
        days.setDefTextSize(defTextSize);
        days.setAdapter(new NumericWheelAdapter(1, 31));
        days.setLabel(mContext.getString(R.string.STR_DAY));

        months.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                int maxDay = 30;
                switch (newValue)
                {
                    case 1:
                    {
                        int curYear = years.getCurrentItem() + START_YEAR;
                        if (curYear % 4 == 0)
                        {
                            maxDay = 29;
                        }
                        else
                        {
                            maxDay = 28;
                        }
                        break;
                    }
                    case 0:
                    case 2:
                    case 4:
                    case 6:
                    case 7:
                    case 9:
                    case 11:
                        maxDay = 31;
                        break;
                    default:
                        maxDay = 30;
                        break;
                }
                days.setAdapter(new NumericWheelAdapter(1, maxDay));
            }
        });

        // set current date
        try
        {
	        Calendar cal = Calendar.getInstance();
            if (mCurDate != null)
            {
	            cal.setTime(mCurDate);

                years.setCurrentItem(cal.get(Calendar.YEAR) - START_YEAR);
                months.setCurrentItem(cal.get(Calendar.MONTH));
                days.setCurrentItem(cal.get(Calendar.DAY_OF_MONTH) - 1);
            }
            else
            {
                years.setCurrentItem(cal.get(Calendar.YEAR) - START_YEAR);
                months.setCurrentItem(cal.get(Calendar.MONTH));
                days.setCurrentItem(cal.get(Calendar.DATE) - 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCurDate(Date date)
    {
        mCurDate = date;
    }

    public void setOnDismissListener(OnDismissListener listener)
    {
        dismissListener = listener;
    }

    public int getYear() { return nYear + START_YEAR; }
    public int getMonth() { return nMonth + 1; }
    public int getDay() { return nDay + 1; }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.btnSave)
            {
                nYear = years.getCurrentItem();
                nMonth = months.getCurrentItem();
                nDay = days.getCurrentItem();

                dismissListener.onDismiss(WheelDatePickerDlg.this);
                dismiss();

            }
            else if (v.getId() == R.id.btnCancel)
            {
                dismiss();
            }

        }
    };
}
