package com.damytech.PincheApp.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.damytech.Misc.Global;
import com.damytech.PincheApp.DrvScanCityOrderActivity;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if("android.alarm.action".equals(intent.getAction())){
			AlarmWakeLock.acquireCPULock(context);
			Intent data = new Intent(context, DrvScanCityOrderActivity.class);
            data.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
			data.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			context.startActivity(data);
		}

	}

}
