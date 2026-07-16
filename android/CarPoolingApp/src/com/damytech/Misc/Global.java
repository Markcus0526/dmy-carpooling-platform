package com.damytech.Misc;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.util.common.StringUtils;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STApplication;
import com.damytech.PincheApp.R;
import com.damytech.PincheApp.receiver.PreUtils;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.SharedPreferenceUtil;
import com.umeng.socialize.bean.SocializeConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EncodingUtils;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-1-9
 * Time: 上午1:00
 * To change this template use File | Settings | File Templates.
 */

public class Global
{
    //log tag
    private static final String TAG = "erik_debug_global";

	public static final int DRV_LOCKTIME_INTERVAL = 3;

    public static double PRICE_MAX_LIMIT() { return 9999.99; }
	public static int SEATS_MAX_LIMIT() { return 30; }
	public static int REPORT_INTERVAL() { return 20 * 1000; }              // Once 20 seconds
	public static int VERIFYKEY_LIMIT() { return 60 * 3; }
	public static int VERIFYKEY_LIMIT_OFFSET() { return 100; }


	public static int ANIM_COVER_FROM_LEFT() { return 0; }
	public static int ANIM_COVER_FROM_RIGHT() { return 1; }

	public static int ANIM_NONE() { return -1; }
	public static String ANIM_DIRECTION() { return "anim_direction"; }
	public static String getServiceCall() { return "4008700111"; }

	public static int IDENTIFY_PASSENGER() { return 1; }
	public static int IDENTIFY_DRIVER() { return 2; }

	public static int getPageItemCount() { return 10; }

	public static String getPreferenceName() { return "BJPincheApp_info"; }

	public static double getCoordOffset() { return 1; }


	public static String getDownloadCompletedSuffix() { return "_download_completed"; }
	public static String getDownloadNotCompletedSuffix() { return "_download_not_completed"; }
	public static String getFileTotalSizeMark() { return "_totalsize_"; }

	public static Toast gToast = null;

	public static LocationClient gLocClient = null;
	public static MyLocationListener gLocListener = new MyLocationListener();
	private static Thread gThrLocUpload = null;
	public static boolean isReport = false;

    public static boolean g_bPassOrderItemShouldUpgrade = false;
    public static int g_nPassChangedOrderType = ConstData.ORDER_TYPE_ONCE;
    public static long g_lPassChangedOrderId = -1;

	public static Context gContext = null;

    /**
     * 获取当前版本号
     * @param context
     * @return
     */
    public static String getCurrentVersion(Context context){
        String curVersion="0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);// getPackageName()是你当前类的包名，0代表是获取版本信息
            curVersion = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curVersion;
    }

    /**
     *
     * @param urlpath 下载链接URL
     * @param savepath 保存的路径
     * @param filename 保存的文件名
     * @return 保存的文件
     */
    public static File downloadApk(String urlpath, String savepath, String filename){
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlpath);
            HttpResponse response = client.execute(httpGet);
            InputStream is = response.getEntity().getContent();
            File file = new File(savepath, filename);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buff = new byte[ 2 * 1024];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
            is.close();
            fos.close();
            return file;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

	public static void initLocMgr(Context appCtx)
	{
		if (gLocClient != null)
		{
			gLocClient.stop();
			gLocClient = null;
		}

		gContext = appCtx;

		gLocClient = new LocationClient(gContext);
		gLocClient.registerLocationListener(gLocListener);

		LocationClientOption option = new LocationClientOption();
		{
			option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
			option.setCoorType("bd09ll");
			option.setScanSpan(5000);
			option.setIsNeedAddress(true);
			option.setNeedDeviceDirect(true);
			option.setOpenGps(true);
		}

		gLocClient.setLocOption(option);

	}

	public static void startLocationUpdate(Context gContext)
	{
		if (gLocClient == null)
			initLocMgr(gContext);

		gLocClient.start();
	}

	public static void stopLocationUpdate()
	{
		if (gLocClient == null)
			return;

		gLocClient.stop();
	}


	public static void startReportLocation(final Context appCtx)
	{
		stopReportLocation();

		isReport = true;
		gThrLocUpload = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true)
				{
					if (!isReport)
						break;

					double lat = Global.loadLatitude(appCtx);
					double lng = Global.loadLongitude(appCtx);

					if (Global.isLoggedIn(appCtx))
						CommManager.reportDriverPos(Global.loadUserID(appCtx), lat, lng, Global.getIMEI(appCtx), null);

					try { Thread.sleep(REPORT_INTERVAL(), 0); } catch (Exception ex) { ex.printStackTrace(); }
				}
			}
		});

		gThrLocUpload.setPriority(Thread.MAX_PRIORITY);

		gThrLocUpload.start();
	}


	public static void stopReportLocation()
	{
		if (gThrLocUpload == null)
			return;

		try {
			isReport = false;
			gThrLocUpload.interrupt();
			gThrLocUpload = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


    //the counter for publish times
    public static int publishCounter = 0;
    public static void addPublishCounter(){
        publishCounter++;
    }
    public static void resetPublishCounter(){
        publishCounter = 0;
    }

	public static void showTextToast(Activity activity, String toastStr)
	{
		if (gToast == null || gToast.getView().getWindowVisibility() != View.VISIBLE)
		{
			gToast = Toast.makeText(activity, toastStr, Toast.LENGTH_SHORT);
			gToast.show();
		}
	}

	public static void showAdvancedToast(Activity activity, String szText, int gravity)
	{
        //Log.d(TAG,"showAdvanceToast");
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.custom_toast, (ViewGroup)activity.findViewById(R.id.toast_parent_layout));

		TextView txtView = (TextView)view.findViewById(R.id.txt_message);
		txtView.setText(szText);
		showToastWithView(activity, view, gravity);
	}

	public static void showToastWithView(Activity activity, View view, int gravity)
	{
		if (gToast != null)
			gToast.cancel();

		gToast = new Toast(activity);
		gToast.setView(view);

		Point ptSize = getScreenSize(activity);
		ptSize.y -= statusBarHeight(activity);
		ResolutionSet.instance.iterateChild(view, ptSize.x, ptSize.y);

		gToast.setDuration(Toast.LENGTH_SHORT);
		if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM)
			gToast.setGravity(gravity, 0, ptSize.y * 1 / 5);
		else
			gToast.setGravity(gravity, 0, 0);
		gToast.show();
	}

	public static boolean isExternalStorageRemovable()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			return Environment.isExternalStorageRemovable();

		return true;
	}

	public static File getExternalCacheDir(Context context) {
		File retFile = null;
		if (hasExternalCacheDir()) {
			retFile = context.getExternalCacheDir();
		}

		if (retFile == null)
		{
			// Before Froyo we need to construct the external cache dir ourselves
			final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
			retFile = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
		}

		return retFile;
	}

	public static boolean hasExternalCacheDir()
	{
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static void logMessage(String szMsg)
	{
		Log.d("BJ_MainApp", szMsg);
	}

	public static String eatSpaces(String szText)
	{
		return szText.replaceAll(" ", "");
	}

	public static String eatFullStops(String szText)
	{
		return szText.replaceAll(".", "");
	}

	public static String eatChinesePunctuations(String szText)
	{
		String szResult = szText;
		String szPuncs = "。？！，、；：“”‘’（）-·《》〈〉";
		String szPuncs2 = "——";
		String szPuncs3 = "……";

		szResult = szResult.replaceAll(szPuncs2, "");
		szResult = szResult.replaceAll(szPuncs3, "");
		for (int i = 0; i < szPuncs.length(); i++)
		{
			char chrItem = szPuncs.charAt(i);
			String szItem = "" + chrItem;
			szResult = szResult.replaceAll(szItem, "");
		}

		return szResult;
	}

	public static boolean isLoggedIn(Context appContext)
	{
		return loadUserID(appContext) > 0;
	}

	public static void clearUserInfo(Context appContext)
	{
		saveUserName(appContext, "");
		saveUserPwd(appContext, "");
		saveAutoLoginFlag(appContext, false);
		saveUserID(appContext, -1);
	}

	public static boolean saveAutoLoginFlag(Context appContext, boolean bFlag)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("autologinflag", bFlag ? 1 : 0);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static int loadAutoLoginFlag(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getInt("autologinflag", -1);
	}


	public static boolean saveVerifyKeyTime(Context appContext, long nTime)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putLong("verifykey_time", nTime);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static long loadVerifyKeyTime(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getLong("verifykey_time", 0);
	}



	public static boolean saveIdentify(Context appContext, int identify)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("identify_info", identify);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static int loadIdentify(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getInt("identify_info", -1);
	}



	public static boolean saveRememberFlag(Context appContext, boolean bFlag)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("remember_pwd_flag", bFlag ? 1 : 0);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static int loadRememberFlag(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getInt("remember_pwd_flag", -1);
	}

	public static boolean saveUserName(Context appContext, String username)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("username", username);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static String loadUserName(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("username", "");
	}

	public static boolean saveUserID(Context appContext, long userid)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putLong("userid", userid);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static long loadUserID(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getLong("userid", 0);
	}

	public static boolean saveInviteCode(Context appContext, String invitecode)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("invitecode", invitecode);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static String loadInviteCode(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("invitecode", "");
	}

	public static boolean savePersonVerified(Context appContext, boolean isVerified)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean("person_verif", isVerified);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static boolean isPersonVerified(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getBoolean("person_verif", false);
	}

    public static void savePersonVerfiedWait(Context context, boolean flag){
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(context);
        spUtil.saveBoolean("savePersonVerfiedWait", flag);
    }

    public static boolean isPersonVerfiedWait(Context context){
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(context);
        return spUtil.getBoolean("savePersonVerfiedWait",false);
    }

    public static void saveDriverVerfiedWait(Context context, boolean flag){
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(context);
        spUtil.saveBoolean("saveDriverVerfiedWait", flag);
    }

    public static boolean isDriverVerfiedWait(Context context){
        SharedPreferenceUtil spUtil = SharedPreferenceUtil.getInstance(context);
        return spUtil.getBoolean("saveDriverVerfiedWait",false);
    }

	public static boolean saveDriverVerified(Context appContext, boolean isVerified)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putBoolean("driver_verif", isVerified);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static boolean isDriverVerified(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getBoolean("driver_verif", false);
	}

	public static boolean saveUserPwd(Context appContext, String pwd)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("password", pwd);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static long loadLastAnnouncementID(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getLong("last_announcement_id", 0);
	}

	public static boolean saveLastAnnouncementID(Context appContext, long uid)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putLong("last_announcement_id", uid);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static String loadLastAnnouncementTime(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("last_announcement_time", "");
	}

	public static boolean saveLastAnnouncementTime(Context appContext, String time)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("last_announcement_time", time);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}


	public static long loadLastOrderNotificationID(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getLong("last_ordernotification_id", 0);
	}

	public static boolean saveLastOrderNotificationID(Context appContext, long uid)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putLong("last_ordernotification_id", uid);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}


	public static String loadLastOrderNotificationTime(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("last_ordernotification_time", "");
	}

	public static boolean saveLastOrderNotificationTime(Context appContext, String time)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("last_ordernotification_time", time);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}


	public static long loadLastPersonNotificationID(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getLong("last_personnotification_id", 0);
	}

	public static boolean saveLastPersonNotificationID(Context appContext, long uid)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putLong("last_personnotification_id", uid);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}


	public static String loadLastPersonNotificationTime(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("last_personnotification_time", "");
	}

	public static boolean saveLastPersonNotificationTime(Context appContext, String time)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("last_personnotification_time", time);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}


	public static String loadUserPwd(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("password", "");
	}

	public static boolean saveCityName(Context appContext, String szName)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;
        if(szName == null || szName.trim().equals("")) return false;
		try {
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("cur_city", szName);
			edit.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static void clearCityName(Context appContext)
	{
		saveCityName(appContext, "");
	}

	public static String loadCityName(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
//        return appContext.getResources().getString(R.string.STR_SHENYANG);        // coded by yunsi for test
		return prefs.getString("cur_city", appContext.getResources().getString(R.string.STR_DEFAULT_CITY));
	}


	public static boolean saveBaiduApiKey(Context appContext, String szKey)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;
		if (szKey == null || szKey.trim().equals(""))
			return false;

		try {
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("baidu_apikey", szKey);
			edit.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static String loadBaiduApiKey(Context appContext)
	{
        //B08246dbf469c3fd2b10d7de4e171e0e
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("baidu_apikey", "5bd9d89c960d70b9d1184999527c286e");
	}



    public static boolean saveDetAddress(Context appContext, String szName)
    {
        boolean bSuccess = true;
        SharedPreferences prefs = null;
        if(szName == null || szName.trim().equals("")) return false;
        try {
            prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("cur_address", szName);
            edit.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            bSuccess = false;
        }

        return bSuccess;
    }

    public static void clearDetAddress(Context appContext)
    {
        saveDetAddress(appContext, "");
    }

    public static String loadDetAddress(Context appContext)
    {
        SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
        return prefs.getString("cur_address", appContext.getResources().getString(R.string.STR_DEFAULT_CITY));
    }

	public static boolean saveFeedbackContents(Context appContext, String szContents)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try {
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("feedback_contents", szContents);
			edit.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static String loadFeedbackContents(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		return prefs.getString("feedback_contents", "");
	}


	public static Point getScreenSize(Context appContext)
	{
		Point ptSize = new Point(0, 0);

		WindowManager wm = (WindowManager)appContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		if (Global.getSystemVersion(appContext) >= Build.VERSION_CODES.HONEYCOMB_MR2)
			display.getSize(ptSize);
		else
			ptSize = new Point(display.getWidth(), display.getHeight());

		return ptSize;
	}

	public static String getCurDateTime(Calendar calendar, boolean containTime)
	{
		int nYear = calendar.get(Calendar.YEAR);
		int nMonth = calendar.get(Calendar.MONTH) + 1;
		int nDay = calendar.get(Calendar.DAY_OF_MONTH);

		String szDate = String.format("%d-%02d-%02d", nYear, nMonth, nDay);

		if (!containTime)
			return szDate;

		int nHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nMinute = calendar.get(Calendar.MINUTE);
		int nSecond = calendar.get(Calendar.SECOND);

		String szTime = String.format("%02d-%02d-%02d", nHour, nMinute, nSecond);

		return szDate + " " + szTime;
	}

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isOpenGPS(Context context)
    {
        LocationManager locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }

        return false;
    }

    public static void openGPSSetting(Context context)
    {
        Intent callGPSSetting = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(callGPSSetting);
    }

	public static void callPhone(String szPhoneNum, Context context)
	{
		try {
//			Intent intent = new Intent(Intent.ACTION_CALL);
//			intent.setData(Uri.parse("tel:" + szPhoneNum));
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent);

            Intent intent = new Intent();
            intent.setAction("android.intent.action.DIAL");
            intent.setData(Uri.parse("tel:" + szPhoneNum));
            context.startActivity(intent);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendSMSWithDefaultApp(String szText, Context context)
	{
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("sms:"));
		smsIntent.putExtra("sms_body", szText == null ? "" : szText);
		context.startActivity(smsIntent);
	}

	public static int getSystemVersion(Context appContext)
	{
		int nVersion = 0;

		try
		{
			PackageInfo pInfo = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
			nVersion = pInfo.versionCode;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			nVersion = -1;
		}

		return nVersion;
	}

	public static String getSelfAppVersionName(Context appContext)
	{
		String szPack = "";

		try
		{
			PackageInfo pInfo = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
			szPack = pInfo.versionName;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			szPack = "";
		}

		return szPack;
	}

	public static int getSelfAppVersionCode(Context appContext)
	{
		int verCode = 0;

		try
		{
			PackageInfo pInfo = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
			verCode = pInfo.versionCode;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			verCode = 0;
		}

		return verCode;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
        //B08246dbf469c3fd2b10d7de4e171e0e
	public static String getBaiduKey() { return "5bd9d89c960d70b9d1184999527c286e"; }

	public static String encodeWithBase64(Bitmap bitmap)
	{
		if (bitmap == null)
			return "";

//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//		byte[] byteArray = stream.toByteArray();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while(output.toByteArray().length / 1024 > 20){
            output.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);
            options -= 10;
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Base64.encodeToString(result, Base64.NO_WRAP);
	}

	public static void showKeyboardFromText(EditText editText, Context context)
	{
		InputMethodManager mgr = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void hideKeyboardFromText(EditText editText, Context context)
	{
		InputMethodManager mgr = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public static double calcDistanceKM(double lat1, double lng1, double lat2, double lng2)
	{
		double EARTH_RADIUS_KM = 6378.137;

		double radLat1 = Math.toRadians(lat1);
		double radLat2 = Math.toRadians(lat2);
		double radLng1 = Math.toRadians(lng1);
		double radLng2 = Math.toRadians(lng2);
		double deltaLat = radLat1 - radLat2;
		double deltaLng = radLng1 - radLng2;

		double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(deltaLng / 2), 2)));

		distance = distance * EARTH_RADIUS_KM;

		return distance;		// unit : km
	}

	public static String ConvertToLetter(int iCol)
	{
		String szResult = "";
		int iAlpha;
		int iRemainder = 0;
		iAlpha = iCol / 27;
		iRemainder = iCol - (iAlpha * 26);
		if (iAlpha > 0)
			szResult = Character.toString((char)(iAlpha + 64));

		if (iRemainder > 0)
			szResult += Character.toString((char)(iRemainder + 64));

		return szResult;
	}

	public static int getRelativeLeft(View myView) {
		if (myView.getParent() == null)
			return 0;

		if (myView.getParent() == myView.getRootView())
			return myView.getLeft();
		else
			return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}

	public static int getRelativeTop(View myView) {
		if (myView.getParent() == null)
			return 0;

		if (myView.getParent() == myView.getRootView())
			return myView.getTop();
		else
			return myView.getTop() + getRelativeTop((View) myView.getParent());
	}

	public static Rect getGlobalRect(View v)
	{
		int nLeft = getRelativeLeft(v);
		int nTop = getRelativeTop(v);
		return new Rect(nLeft, nTop, nLeft + v.getWidth(), nTop + v.getHeight());
	}

	public static int statusBarHeight(Activity activity) {
		Rect rectgle= new Rect();
		Window window= activity.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int statusBar = rectgle.top;
		return statusBar;
	}

	public static boolean isValidEmail(String email)
	{
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
			isValid = true;

		return isValid;
	}

	public static int getApiVersion()
	{
		return Build.VERSION.SDK_INT;
	}


	public static String getNotificationToken(Context context)
	{
		String szToken = PreUtils.loadPushUserId(context) + "," + PreUtils.loadPushChannelId(context);
		Log.d("push_token", szToken);
		return szToken;
	}


	public static String getIMEI(Context context)
	{
		TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}


	public static boolean IsQQInstalled(Context appContext)
	{
		return IsAppInstalled(appContext, "com.tencent.mobileqq");
	}


	public static boolean IsAppInstalled(Context appContext, String packname)
	{
		return getOtherAppVersionName(appContext, packname) != null;
	}

	public static String getOtherAppVersionName(Context appContext, String packname)
	{
		PackageManager pm = appContext.getPackageManager();
		String versionName = null;

		try
		{
			PackageInfo packageInfo = pm.getPackageInfo(packname, PackageManager.GET_ACTIVITIES);
			versionName = packageInfo.versionName;
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			versionName = null;
		}

		return versionName;
	}

	public static void startOtherApp(Context context, String pack_name)
	{
		PackageManager pm = context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(pack_name);
		context.startActivity(intent);
	}

	public static String getDirPathForApplication()
	{
		return Environment.getExternalStorageDirectory() + "/bj_pinche_apps/";
	}

	public static String getAppFileNamePrefix(STApplication app)
	{
		return "_" + app.code + "_" + app.uid;
	}

	public static String getAppFileNameSuffix(STApplication app, int state, int nTotalSize)
	{
		String suffix = "";

		if (state == STApplication.APP_STATE_DOWNLOADED)
		{
			suffix = getDownloadCompletedSuffix();
		}
		else if (state == STApplication.APP_STATE_DOWNLOADED_HALF)
		{
			suffix = getDownloadNotCompletedSuffix();
			if (nTotalSize > 0)
				suffix = suffix  + getFileTotalSizeMark() + nTotalSize + "_";
		}
		else
		{
			suffix = "";
		}

		suffix += ("_" + app.latestver_code);

		return suffix;
	}

	public static String getFileNameForApplication(STApplication app, int state, int nTotalSize)
	{
		String prefix = getAppFileNamePrefix(app);
		String suffix = getAppFileNameSuffix(app, state, nTotalSize);

		return prefix + suffix + ".apk";
	}

	public static void removeApksForApp(Context appContext, STApplication app)
	{
		try
		{
			String szAppDirPath = getDirPathForApplication();
			File appDir = new File(szAppDirPath);
			if (!appDir.exists())
				return;

			String szAppPrefix = getAppFileNamePrefix(app);
			File[] subFiles = appDir.listFiles();
			if (subFiles == null)
				return;

			for (int i = 0; i < subFiles.length; i++)
			{
				File childFile = subFiles[i];
				String szFileName = childFile.getName();
				if (szFileName.contains(szAppPrefix))
					childFile.delete();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public static boolean saveCoordinates(Context appContext, double lat, double lng)
	{
		boolean bSuccess = true;
		SharedPreferences prefs = null;

		try
		{
			prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putString("latitude", "" + lat);
			edit.putString("longitude", "" + lng);
			edit.commit();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			bSuccess = false;
		}

		return bSuccess;
	}

	public static double loadLatitude(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		String szLat = prefs.getString("latitude", "39");
		if (szLat.equals(""))
			return 0;

		return Double.parseDouble(szLat);
	}


	public static double loadLongitude(Context appContext)
	{
		SharedPreferences prefs = appContext.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
		String szLng = prefs.getString("longitude", "116");
		if (szLng.equals(""))
			return 0;

		return Double.parseDouble(szLng);
	}



	public static Bitmap getCroppedRoundBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawOval(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

    public static String EncodeToUTF8(String str)
    {
        String tmp;

        try {
            tmp = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            tmp = str;
        }

        return tmp;
    }


	// Date to String conversion
	public static String Date2String(Date dtvalue, boolean withTime)
	{
		String szResult = "";
		if (dtvalue == null)
			return "";

		DateFormat df = null;
		if (withTime)
			df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else
			df = new SimpleDateFormat("yyyy-MM-dd");
		szResult = df.format(dtvalue);

		return szResult;
	}


	// String to date conversion
	public static Date String2Date(String szTime)
	{
		if (szTime == null || szTime.equals("") || szTime.contains("0000-00-00"))
			return null;

		DateFormat df = null;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dtValue = null;
		try
		{
			dtValue = df.parse(szTime);
		}
		catch (Exception ex)
		{
            try
            {
                df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dtValue = df.parse(szTime);
            }
            catch (Exception ex1)
            {
                df = new SimpleDateFormat("yyyy-MM-dd");
                try
                {
                    dtValue = df.parse(szTime);
                }
                catch (Exception ex2)
                {
                    ex.printStackTrace();
                }
            }
		}

		return dtValue;
	}


	public static int getBrandImgFromName(String szName)
	{
		return 0;
	}



	// Rotate image clockwise
	public static Bitmap rotateImage(String pathToImage, int nAngle) {
		// 2. rotate matrix by postconcatination
		Matrix matrix = new Matrix();
		matrix.postRotate(nAngle);

		// 3. create Bitmap from rotated matrix
		Bitmap sourceBitmap = BitmapFactory.decodeFile(pathToImage);
		return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
	}



	public static int getImageOrientation(String imagePath){
		int nAngle = 0;
		try {
			File imageFile = new File(imagePath);
			ExifInterface exif = new ExifInterface(
					imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_270:
					nAngle = 270;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					nAngle = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					nAngle = 90;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return nAngle;
	}


	/****************************************************************************
	 * UMeng methods and variables
	 ****************************************************************************/
	private static final Set<WeakReference<SocializeConfig>> wCigs = new HashSet<WeakReference<SocializeConfig>>();
	public static String UMENG_DESCRIPTOR() { return "com.umeng.share"; }
	public final static SocializeConfig getSocialConfig(Context context) {
		SocializeConfig config = SocializeConfig.getSocializeConfig();
		WeakReference<SocializeConfig> ref = new WeakReference<SocializeConfig>(
				config);
		wCigs.add(ref);

		return config;
	}


	public static void openBrowser(Context ctx, String url)
	{
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		ctx.startActivity(intent);
	}

    public static String getConcealedCarNo(Context ctx, String originCarNo)
    {
        String strCarNo = "";
        if (originCarNo.length() > 3)
            strCarNo = ctx.getResources().getString(R.string.STR_SINGERSUCCESS_CARINFO) + originCarNo.substring(0, 1) + "***" + originCarNo.substring(4, originCarNo.length());
        else
            strCarNo = ctx.getResources().getString(R.string.STR_SINGERSUCCESS_CARINFO) + "*******";

        return strCarNo;
    }

    /**
     * Check running process by process name
     * @param context
     * @param processName
     * @return
     */
    public static boolean checkRunningProcessByName(Context context, String processName)
    {
        if (processName == null)    return false;

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo process : processes)
        {
            if (processName.equals(process.processName))
            {
                return true;
            }
        }

        return false;
    }

    public static boolean checkRunningProcessByPackage(Context contex, String packageName)
    {
        ActivityManager am = (ActivityManager) contex.getSystemService(Activity.ACTIVITY_SERVICE);
        String pkgname = am.getRunningTasks(1).get(0).topActivity.getPackageName();
        if (packageName.equals(pkgname))
            return true;

        return false;
    }


	public static class MyLocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation == null ||
				bdLocation.getAddrStr() == null ||
				gContext == null) {
				return;
			}

			double lat = bdLocation.getLatitude();
			double lng = bdLocation.getLongitude();

			String szCityName = bdLocation.getCity();
			String szAddr = bdLocation.getAddrStr();

			Global.saveCoordinates(gContext, lat, lng);
			Global.saveCityName(gContext, szCityName);
			Global.saveDetAddress(gContext, szAddr);
		}
	}


	public static String createChargeUrl()
	{
		return CommManager.getServiceBaseUrl() + "chargeWithBaidu";
	}

    public static String successInChargeUrl()
    {
        return CommManager.getServiceBaseUrl() + "successIncharge";
    }


	public static boolean validateName(String szName, boolean allowChinese) {
		if (szName == null || szName.equals(""))
			return false;

		for (int i = 0; i < szName.length(); i++)
		{
			char chItem = szName.charAt(i);

			if (i == 0)
			{
				if (!Character.isUpperCase(chItem) &&
					!Character.isLowerCase(chItem) &&
					!isChinese(chItem)) {
					return false;
				}
			} else {
				if (!Character.isUpperCase(chItem) &&
						!Character.isLowerCase(chItem) &&
						!Character.isDigit(chItem) &&
						!isChinese(chItem)) {
					return false;
				}

				if (!allowChinese && isChinese(chItem))
					return false;
			}
		}

		return true;
	}


	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
			|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
			|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
			|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
			|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
			|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}

		return false;
	}

}
