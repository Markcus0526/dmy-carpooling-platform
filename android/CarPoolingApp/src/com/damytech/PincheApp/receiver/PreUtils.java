package com.damytech.PincheApp.receiver;

import android.content.Context;
import android.content.SharedPreferences;

public class PreUtils
{

	private static final String BIND__FLAG = "bind_flag";
    private static final String BIND_USERID_FLAG = "bind_userid_flag";
    private static final String BIND_CHANNELID_FLAG = "bind_channelid_flag";

	public static boolean isBind(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
				Context.MODE_PRIVATE);
		return sp.getBoolean(BIND__FLAG, false);
	}

	public static void bind(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(BIND__FLAG, true).commit();
	}
	
	
	public static void unbind(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(BIND__FLAG, false).commit();
	}

    public static void savePushUserId(Context context, String userid)
    {
        SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
                Context.MODE_PRIVATE);
        sp.edit().putString(BIND_USERID_FLAG, userid).commit();
    }

    public static String loadPushUserId(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
                Context.MODE_PRIVATE);
        return sp.getString(BIND_USERID_FLAG, "");
    }

    public static void savePushChannelId(Context context, String channelid)
    {
        SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
                Context.MODE_PRIVATE);
        sp.edit().putString(BIND_CHANNELID_FLAG, channelid).commit();
    }

    public static String loadPushChannelId(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("pre_tuisong",
                Context.MODE_PRIVATE);
        return sp.getString(BIND_CHANNELID_FLAG, "");
    }
}
