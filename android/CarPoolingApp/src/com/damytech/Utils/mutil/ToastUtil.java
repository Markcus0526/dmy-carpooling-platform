package com.damytech.Utils.mutil;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.damytech.PincheApp.R;

public class ToastUtil {
    private static Toast mToast = null;
	public static void showToast(Context context, String message, int showlocation, int duration){
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.mytoast, null);
		TextView tvMessage = (TextView) view.findViewById(R.id.tv_toast_message);
		tvMessage.setText(message);

        if(mToast == null)
            mToast = new Toast(context);
//            mToast.cancel();
//		mToast = new Toast(context);
		mToast.setGravity(showlocation, 0, 0);
		mToast.setDuration(duration);
		mToast.setView(view);
		mToast.show();
	}


}
