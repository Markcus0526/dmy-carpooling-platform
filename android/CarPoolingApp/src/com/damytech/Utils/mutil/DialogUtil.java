package com.damytech.Utils.mutil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.damytech.PincheApp.R;

/**
 * Created by Administrator on 2014/12/8.
 */
public class DialogUtil {
    private static Dialog mDialog = null;
    public static void showDialog(Context context, String message, int gravity){
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.mytoast, null);
        TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
        tvMessage.setText(message);

        if(mDialog == null)
            mDialog = new Dialog(context);
        mDialog.setCancelable(false);
        mDialog.show();

        Window win = mDialog.getWindow();
        win.setBackgroundDrawable(new ColorDrawable(0));
        win.setGravity(gravity);

        win.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = new DisplayMetrics();
        win.getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = win.getAttributes();
        win.setAttributes(lp);
        win.setContentView(view);
    }

    public static void cancelDialog(){
        if(mDialog != null){
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
