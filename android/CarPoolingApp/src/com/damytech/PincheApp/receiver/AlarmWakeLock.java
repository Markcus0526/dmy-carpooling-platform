package com.damytech.PincheApp.receiver;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class AlarmWakeLock {
	
	private static PowerManager pm;
	private static WakeLock wl;

	public static void acquireCPULock(Context context){
		if(null != wl)
			return;
		pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|
				PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE, "oo");
		wl.acquire();
	}
	public static void releaseCPULock(){
		if(null != wl){
			wl.release();
			wl = null;
		}
			
	}
}
