package com.damytech.Utils.mutil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;













import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

public class Utils {
	private static boolean isLog = true;
	private static String log_tag = "ht";
	
	public static void mLogError(String msg){
		if(isLog)
			Log.e(log_tag, msg);
	}
	
	public static void mLog(String msg){
		if(isLog)
			Log.i(log_tag, msg);
	}
	public static void mLog_d(String msg){
		if(isLog)
			Log.d(log_tag, msg);
	}
	
	public static void mLog_v(String msg){
		if(isLog)
			Log.v(log_tag, msg);
	}
	
	public static void mLog_w(String msg){
		if(isLog)
			Log.w(log_tag, msg);
	}
	/**
	 * 是否WIFI联网
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkWIFI(Context context) {
		ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = conn.getActiveNetworkInfo();
		 if(null != networkInfo){
			 if(ConnectivityManager.TYPE_WIFI == networkInfo.getType())
				 return true;
		 }
		 return false;
//		String type = networkInfo.getTypeName();
//		return "WIFI".equalsIgnoreCase(type);
	}
	
	/**
	 * 是否网络可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = conn.getActiveNetworkInfo();
		 if(null != networkInfo && networkInfo.isAvailable()){
			return true;
		 }
		 return false;
//		String type = networkInfo.getTypeName();
//		return "WIFI".equalsIgnoreCase(type);
	}
	
	public static int[] getDisplayMetrics(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new int[]{dm.widthPixels, dm.heightPixels};
	}
	
	public static String formatDate(long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(time));
		if(date == null || "".equalsIgnoreCase(date))
			date = "0000-00-00 00:00:00";
		return date;
	}

    public static String formatDateShort(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(time));
        if(date == null || "".equalsIgnoreCase(date))
            date = "0000-00-00";
        return date;
    }
	/**
	 * 把数据写入sd卡
	 * @param log 数据
	 * @param filename 文件名
	 * @param time 时间，系统 时间，单位毫秒
	 */
	public static void writeToSDCard(String log, String filename, long time){
		 
      	String path = Environment.getExternalStorageDirectory().getAbsolutePath() ;
		File dir = new File(path, "haotang");
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File guiji = new File(dir, filename);
		try {
			FileWriter fw = new FileWriter(guiji, true);
			fw.append(formatDate(time)+" ::");
			fw.append(log +"\r\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//计算球面上两个点的距离
	public static double getDistatce(double lat1, double lat2, double lon1,    double lon2) { 
        double R = 6371; 
        double distance = 0.0; 
        double dLat = (lat2 - lat1) * Math.PI / 180; 
        double dLon = (lon2 - lon1) * Math.PI / 180; 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) 
                + Math.cos(lat1 * Math.PI / 180) 
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) 
                * Math.sin(dLon / 2); 
        distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R; 
        return distance; 
    }
	
	//格式化里程和费用
		public static String formatDecimal(String pattern, double decimal){
			DecimalFormat df = new DecimalFormat();
			df.applyPattern(pattern);
			return df.format(decimal);
			
		}
		
		//格式化时间格式
		public static String formatTime(long time){
			long totalss = time / 1000;
			long timess = totalss % 60;
			long totalmm = totalss / 60;
			long timemm = totalmm % 60;
			long totalhh = totalmm / 60;
			long timehh = totalhh % 60;
			StringBuffer sb = new StringBuffer();
			if(timehh < 10){
				sb.append("0");
			}
			sb.append(timehh);
			sb.append(":");
			if(timemm < 10){
				sb.append("0");
			}
			sb.append(timemm);
			sb.append(":");
			if(timess < 10){
				sb.append("0");
			}
			sb.append(timess);
			return sb.toString();
		}
		/**
		 * 格式化以分钟为单位的时间，转化为小时：分钟
		 * @param minutestr
		 * @return
		 */
		public static String formatMinutesToHour(String minutestr){
			int minutes = Integer.parseInt(minutestr);
			int minute = minutes % 60;
			int hour = minutes / 60;
			if(0 == hour){
				return minute + "分钟";
			}else{
				return hour + "小时"+minute + "分钟";
			}
		}
		//格式化时间格式
		public static long TimeToMinutes(long time){
			long totalss = time / 1000;
			long totalmm = totalss / 60;
//			if(totalss % 60 > 0){
//				totalmm += 1;
//			}
			return totalmm;
		}
		
		/**
		 * 转换文件大小格式
		 * 
		 * @param fileS
		 * @return
		 */
		public static String FormetFileSize(long fileS) {
//			if (0l == fileS)
//				return "";
			DecimalFormat df = new DecimalFormat("#.00");
			String fileSizeString = "";
			if(fileS <= 0){
				fileSizeString = "0B";
			}else if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "B";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "K";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "M";
			} else {
				fileSizeString = df.format((double) fileS / 1073741824) + "G";
			}
			return fileSizeString;
		}
		
		/**
		 * 创建并获取文件夹的路径
		 * @param context
		 * @return
		 */
		public static String getDir(Context context){
			File file = null;
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "oo");
			}else{
				file = context.getCacheDir();
			}
			if(!file.exists()){
				file.mkdirs();
			}
			
			return file.getAbsolutePath();
		}
		/**
		 * 创建精度条对话框
		 * @param ctx
		 * @param info
		 * @return
		 */
		public  static ProgressDialog createProcessDialog(Context ctx, String info){
	    	ProgressDialog processDialog = new ProgressDialog(ctx); 
			processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
			processDialog.setTitle(""); 
			processDialog.setMessage(info); 
			processDialog.setIcon(android.R.drawable.ic_dialog_map); 
			processDialog.setIndeterminate(false); 
			processDialog.setCancelable(true); 
			processDialog.setCanceledOnTouchOutside(false);
			processDialog.show(); 
			return processDialog;
	    
	    }
		/**
		 * 吐司
		 * @param context
		 * @param str
		 */
		public static void showTaost(Context context, String str){
			Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		
		/**
		 * 电话验证
		 * 
		 * @param
		 * @return
		 */
		public static boolean checkPhone(Context context, EditText phone_et) {
			String telPattern = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7])|(170))\\d{8}$";
			String phone = phone_et.getText().toString().trim();
			boolean isAvail = startCheck(telPattern, phone);
			if("".equals(phone.trim())){
				showTaost(context, "手机号为空，请重新输入");
				phone_et.requestFocus();
				phone_et.setFocusableInTouchMode(true);
				return false;
			}
			if (!isAvail) {
				showTaost(context, "手机号不正确，请重新输入");
				phone_et.requestFocus();
				phone_et.setFocusableInTouchMode(true);
				return false;
			}
			return true;
		}
		
		/**
		 * 邮箱验证
		 * 
		 * @param context
		 * @param email_et
		 * @return
		 */
		public static boolean checkEmail(Context context, EditText email_et) {
			String emailPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
			String email = email_et.getText().toString().trim();
			boolean isAvail = startCheck(emailPattern, email);
			
			if (!isAvail) {
				showTaost(context, "邮箱格式不正确，请重新输入");
				email_et.requestFocus();
				email_et.setFocusableInTouchMode(true);
				return false;
			}
			if("".equals(email.trim())){
				showTaost(context, "邮箱为空，请重新输入");
				email_et.requestFocus();
				email_et.setFocusableInTouchMode(true);
				return false;
			}
			if(email.length()>50){
				showTaost(context, "邮箱长度不能大于50，请重新输入");
				email_et.requestFocus();
				email_et.setFocusableInTouchMode(true);
				return false;
			}
			return true;
		}
		
		/**
		 * 正则规则验证
		 * @param reg
		 * @param string
		 * @return
		 */
		public static boolean startCheck(String reg, String string) {
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(string);		
			return matcher.matches();
		}
		/**
		 * app是否正在运行
		 * @param context
		 * @return
		 */
		public static boolean isAppAvailable(Context context){
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskinfos = am.getRunningTasks(20);
			boolean result = false;
			for(int i = 0; i < taskinfos.size(); i++){
				if(context.getPackageName().equals(taskinfos.get(i).topActivity.getPackageName())){
					result = true;
					break;
				}
			}
			return result;
		}
		
		public static String getIPAddress(Context context){
			WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if(manager.isWifiEnabled()){
				return getWifiAddress(context);
			}
			return getLocalIPAddress();
			
		}
		//获取wifi地址
		public static String getWifiAddress(Context context){
			WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if(!manager.isWifiEnabled()){
				manager.setWifiEnabled(true);
			}
			WifiInfo info = manager.getConnectionInfo();
			int ip = info.getIpAddress();
			return ipToString(ip);
		}
		public static String ipToString(int ip){
			
			return  (ip & 0xFF ) + "." +     

	        ((ip >> 8 ) & 0xFF) + "." +     

	        ((ip >> 16 ) & 0xFF) + "." +     

	        ( ip >> 24 & 0xFF) ;
		}
		public static String getLocalIPAddress(){
			try{
				for(Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements();){
					NetworkInterface networkInterface = networkInterfaces.nextElement();
					for(Enumeration<InetAddress> addresses = networkInterface.getInetAddresses(); addresses.hasMoreElements();){
						InetAddress address = addresses.nextElement();
						if(!address.isLoopbackAddress() && (address instanceof Inet4Address)){
							return address.getHostAddress().toString();
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return "";
			}
			return "";
		}


}
