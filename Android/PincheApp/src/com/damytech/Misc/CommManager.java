package com.damytech.Misc;

import com.baidu.navisdk.util.common.net.HttpUtils;
import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EncodingUtils;
import com.damytech.Utils.mutil.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-1-10
 * Time: 上午1:41
 * To change this template use File | Settings | File Templates.
 */
public class CommManager
{
	public static String getSource() { return "11"; }

	public static int getTimeOut() { return 60 * 1000; }

//	public static String getServiceBaseUrl() { return "http://124.207.135.69:8080/PincheService/webservice/"; }
//	public static String getServiceBaseUrl() { return "http://192.168.1.101:8888/PincheService/webservice/"; }
	public static String getServiceBaseUrl() { return "http://218.60.131.41:30101/PincheService/webservice/"; }
//	public static String getServiceBaseUrl() { return "http://mg.ookuaipin.com:8080/PincheService/webservice/"; }
//	public static String getServiceBaseUrl() { return "http://123.57.47.94:8080/PincheService/webservice/"; }

	public static void RequestWeatherInfo(String szCityName, AsyncHttpResponseHandler handler)
	{
		String strCityName = "";

		try
		{
			strCityName = URLEncoder.encode(szCityName, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(getTimeOut());
		client.get("http://traffic.kakamobi.com/weather.ashx?city=" + strCityName, handler);
	}

	public static void google2baidu(double fLatitude, double fLongitude, AsyncHttpResponseHandler handler)
	{
		String url = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x=" + fLongitude + "&y=" + fLatitude;

		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(getTimeOut());
		client.get(url, handler);
	}

	public static void reverseGeocode(double latitude, double longitude, AsyncHttpResponseHandler handler)
	{
		String url = "http://api.map.baidu.com/geocoder?location=" + latitude + "," + longitude + "&output=json&key=" + Global.getBaiduKey();
		try
		{
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut() * 4);
			client.get(url, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}

	public static void nearbySearch(double lat, double lng, String keyword, AsyncHttpResponseHandler handler)
	{
		String url = "http://api.map.baidu.com/place/search?" + "query=" + keyword
				+ "&location=" + lat + "," + lng
				+ "&radius=10000&output=json&ak=" + Global.getBaiduKey();

		try
		{
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}

	public static void searchRoute(double orglat, double orglng, double dstlat, double dstlng, String city, AsyncHttpResponseHandler handler)
	{
		String url = "http://api.map.baidu.com/direction?" + "origin=" + orglat + "," + orglng
				+ "&destination=" + dstlat + "," + dstlng
				+ "&origin_region=" + city
				+ "&destination_region=" + city
				+ "&output=json&ak=" + Global.getBaiduKey();

		try
		{
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.get(url, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	/*
	 Api methods
	*/

	// 1) Register User method
	public static void registerUser(String username,
	                                String mobile,
	                                String nickname,
	                                String password,
	                                String invitecode,
	                                int sex,
	                                String city,
	                                String devtoken,
	                                String pushnotif_token,
                                    String channel,
	                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "registerUser";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("username", username);
			params.put("mobile", mobile);
			params.put("nickname", nickname);
			params.put("password", password);
			params.put("invitecode", invitecode);
			params.put("sex", "" + sex);
			params.put("city", city);
            params.put("channel_flag", channel);
			params.put("devtoken", devtoken);
			params.put("pushnotif_token", pushnotif_token);
			params.put("platform", "2");

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 2) Login User method
	public static void loginUser(String username,
	                             String password,
	                             String city,
	                             String devtoken,
	                             String pushnotif_token,
	                             AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "loginUser";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("username", username);
			params.put("password", password);
			params.put("city", city);
			params.put("devtoken", devtoken);
			params.put("pushnotif_token", pushnotif_token);
			params.put("platform", "2");

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 3) Get child apps list
	public static void getChildAppList(AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getChildAppList";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("platform", "2");

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 4) Get latest announcements
	public static void getLatestAnnouncements(long userid,
	                                          String city,
	                                          boolean isDriverVerif,
	                                          long limitid,
	                                          String devtoken,
	                                          AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestAnnouncements";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("city", city);
			params.put("limitid", "" + limitid);
			params.put("devtoken", devtoken);

			if (isDriverVerif)
				params.put("driververif", "1");
			else
				params.put("driververif", "0");

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 5) Get announcements with specific page number
	public static void getAnnouncementsPage(long userid,
	                                        String city,
	                                        boolean isDriverVerif,
	                                        int pageno,
	                                        String devtoken,
	                                        AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getAnnouncementsPage";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("city", city);
			params.put("pageno", "" + pageno);
			params.put("devtoken", devtoken);

			if (isDriverVerif)
				params.put("driververif", "1");
			else
				params.put("driververif", "0");

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 6) Get latest order notification
	public static void getLatestOrderNotif(long userid, long limitid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestOrderInfos";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 7) Get order notifications with specific page number
	public static void getOrderNotifPage(long userid, int pageno, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getOrderInfosPage";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 8) Get latest personal notifications
	public static void getLatestNotifications(long userid, long limitid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestNotifications";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 9) Get personal notifications with specific page number
	public static void getNotificationsPage(long userid, int pageno, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getNotificationsPage";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 10) Get coupon detail info
	public static void getCouponDetails(long couponid, long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getCouponDetails";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("couponid", "" + couponid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 11) Record app download operation
	public static void recordDownload(String packname, String version, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "recordDownload";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("platform", "2");
			params.put("packname", packname);
			params.put("version", version);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 12) Get phone verification code
	public static void getVerifKey(String mobile, String username, int registered_phone, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getVerifKey";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("mobile", mobile);
			params.put("username", username);
			params.put("registered", "" + registered_phone);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 13) Forget password, reset with new one
	public static void forgetPassword(String username, String mobile, String password, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "forgetPassword";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("username", username);
			params.put("password", password);
			params.put("mobile", mobile);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 14) Get user detailed information
	public static void getUserInfo(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getUserInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 15) Change user detailed information
	public static void changeUserInfo(long userid,
	                                  String mobile,
	                                  String nickname,
	                                  String birthday,
	                                  int sex,
	                                  String photo,
	                                  int photo_changed,
	                                  String carimg,
	                                  int carimg_changed,
	                                  String devtoken,
	                                  AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "changeUserInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("mobile", mobile);
			params.put("nickname", nickname);
			params.put("birthday", birthday);
			params.put("sex", "" + sex);
			params.put("photo", photo);
			params.put("photo_changed", "" + photo_changed);
			params.put("carimg", carimg);
			params.put("carimg_changed", "" + carimg_changed);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 16) Change user password with new one
	public static void changePassword(long userid,
	                                  String oldpwd,
	                                  String newpwd,
	                                  String devtoken,
	                                  AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "changePassword";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("oldpwd", oldpwd);
			params.put("newpwd", newpwd);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 17) Verify personal information
	public static void verifyPersonInfo(long userid,
	                                    String foreimage,
	                                    String backimage,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "verifyPersonInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("idforeimage", foreimage);
			params.put("idbackimage", backimage);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 18) Check if has news
	public static void hasNews(long userid,
	                           String city,
	                           boolean driververif,
	                           long lastannounceid,
	                           long lastordernotifid,
	                           long lastpersonnotifid,
	                           String devtoken,
	                           AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "hasNews";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("city", city);
			params.put("driververif", driververif ? "1" : "0");
			params.put("lastannounceid", "" + lastannounceid);
			params.put("lastordernotifid", "" + lastordernotifid);
			params.put("lastpersonnotifid", "" + lastpersonnotifid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 19) Mark order notifications which are already read
	public static void readOrderNotifs(long userid, long ordernotifid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "readOrderNotifs";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("ordernotifid", "" + ordernotifid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 20) Mark personal notifications which are already read
	public static void readPersonNotifs(long userid,
	                                    long personnotifid,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "readPersonNotifs";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("personnotifid", "" + personnotifid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 21) Get balance changed logs with specific page number
	public static void getTsLogsPage(long userid,
	                                 int pageno,
	                                 String devtoken,
	                                 AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getTsLogsPage";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 22) Get latest logs of changing balance
	public static void getLatestTsLogs(long userid,
	                                   long limitid,
	                                   String devtoken,
	                                   AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestTsLogs";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 23) Charge balance
	public static void charge(long userid,
	                          double balance,
	                          String devtoken,
	                          int charge_source,
	                          AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "charge";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("balance", "" + balance);
			params.put("charge_source", "" + charge_source);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 24) Get bank information
	public static void getAccount(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getAccount";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 25) Withdraw
	public static void withdraw(long userid,
	                            String realname,
	                            String accountname,
	                            double balance,
	                            String password,
	                            String devtoken,
	                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "withdraw";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("realname", realname);
			params.put("accountname", accountname);
			params.put("balance", "" + balance);
			params.put("password", password);
			params.put("devtoken", devtoken);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 26) Newly bind or change original bank information
	public static void bindBankCard(long userid,
	                                String bankcard,
	                                String bankname,
	                                String subbranch,
	                                String devtoken,
	                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "bindBankCard";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("bankcard", bankcard);
			params.put("bankname", bankname);
			params.put("subbranch", subbranch);
			params.put("devtoken", devtoken);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 27) Release bound bank card
	public static void releaseBankCard(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "releaseBankCard";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 28) Get latest coupons for user
	public static void getLatestCoupon(long userid, long limitid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestCoupon";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 29) Get coupons for user with specific page number
	public static void getPagedCoupon(long userid, int pageno, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPagedCoupon";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 30) Get a new coupon from the activity
	public static void addCoupon(long userid, String active_code, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "addCoupon";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("active_code", active_code);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 31) Advance opinion to system
	public static void advanceOpinion(long userid, String contents, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "advanceOpinion";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("contents", contents);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 32) Get default share content templates
	public static void defaultShareContents(long userid, String inputUrl, int shareFlag,  String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "defaultShareContents";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
            params.put("inputUrl", inputUrl);
            params.put("shareFlag", "" + shareFlag);
			params.put("devtoken", devtoken);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 33) Get latest login info with device token
	public static void getLoginInfoFromDevToken(String devtoken,
	                                            String pushnotif_token,
	                                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLoginInfoFromDevToken";

		try
		{
			RequestParams params = new RequestParams();
			params.put("source", getSource());
			params.put("devtoken", devtoken);
			params.put("pushnotif_token", pushnotif_token);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 34) Get latest application version
	public static void getLatestAppVersion(String packname, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestAppVersion";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("packname", packname);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 35) Get remaining balance
	public static void getMoney(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getMoney";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 36) Verify driver and car information
	public static void verifyDriver(long userid,
	                                String drvlic_fore,
	                                String drvlic_back,
	                                String brand,
	                                String type,
	                                String color,
	                                String carimg,
	                                String drvinglic_fore,
	                                String drvinglic_back,
	                                String devtoken,
	                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "verifyDriver";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("driver_licence_fore", drvlic_fore);
			params.put("driver_licence_back", drvlic_back);
			params.put("brand", brand);
			params.put("type", type);
			params.put("color", color);
			params.put("carimg", carimg);
			params.put("driving_licence_fore", drvinglic_fore);
			params.put("driving_licence_back", drvinglic_back);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 37) Get latest withdraw logs
	public static void latestWithdrawLogs(long userid, long limitid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "latestWithdrawLogs";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 38) Get withdraw logs with specific page number
	public static void pagedWithdrawLogs(long userid, int pageno, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "pagedWithdrawLogs";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 39) Cancel withdraw operation
	public static void cancelWithdraw(long userid, long req_id, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "cancelWithdraw";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("req_id", "" + req_id);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 40) Add daily route
	public static void addRoute(long userid,
	                            int type,
	                            int daytype,
                                String startcity,
                                String endcity,
	                            String startaddr,
	                            String endaddr,
	                            double startlat,
	                            double startlng,
	                            double endlat,
	                            double endlng,
	                            String city,
	                            String start_time,
	                            String devtoken,
	                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "addRoute";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("type", "" + type);
			params.put("daytype", "" + daytype);
            params.put("startcity", startcity);
            params.put("endcity", endcity);
			params.put("startaddr", startaddr);
			params.put("endaddr", endaddr);
			params.put("startlat", "" + startlat);
			params.put("startlng", "" + startlng);
			params.put("endlat", "" + endlat);
			params.put("endlng", "" + endlng);
			params.put("city", city);
			params.put("start_time", start_time);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 41) Remove daily route information
	public static void removeRoute(long userid, long route_id, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "removeRoute";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("route_id", "" + route_id);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 42) Change daily route information
	public static void changeRoute(long userid,
	                               long route_id,
	                               int type,
	                               int daytype,
                                   String startcity,
                                   String endcity,
	                               String startaddr,
	                               String endaddr,
	                               double startlat,
	                               double startlng,
	                               double endlat,
	                               double endlng,
	                               String city,
	                               String start_time,
	                               String devtoken,
	                               AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "changeRoute";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("route_id", "" + route_id);
			params.put("type", "" + type);
			params.put("daytype", "" + daytype);
            params.put("startcity", startcity);
            params.put("endcity", endcity);
			params.put("startaddr", startaddr);
			params.put("endaddr", endaddr);
			params.put("startlat", "" + startlat);
			params.put("startlng", "" + startlng);
			params.put("endlat", "" + endlat);
			params.put("endlng", "" + endlng);
			params.put("city", city);
			params.put("start_time", start_time);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 43) Get all the routes information
	public static void getRoutes(long userid, int type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getRoutes";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("type", "" + type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 44) Get latest driver orders
	public static void getLatestDriverOrders(long userid, int order_type, String order_num, int limitid_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestDriverOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("order_type", "" + order_type);
			params.put("order_num", order_num);
			params.put("limitid_type", "" + limitid_type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 45) Get driver orders with specific page number
	public static void getPagedDriverOrders(long userid, int order_type, String limit_time, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPagedDriverOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("order_type", "" + order_type);
			params.put("limit_time", limit_time);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 46) Get latest passenger orders information
	public static void getLatestPassengerOrders(long userid, int order_type, String order_num, int limitid_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestPassengerOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("order_type", "" + order_type);
			params.put("order_num", order_num);
			params.put("limitid_type", "" + limitid_type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 47) Get passenger orders information with specific page number
	public static void getPagedPassengerOrders(long userid, int order_type, String limit_time, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPagedPassengerOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("order_type", "" + order_type);
			params.put("limit_time", limit_time);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 48) Get latest acceptable once orders information
	public static void getLatestAcceptableOnceOrders(long userid, long limitid, int order_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestAcceptableOnceOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("order_type", "" + order_type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 49) Get acceptable once orders with specific page number
	public static void getPagedAcceptableOnceOrders(long userid, int pageno, int order_type,
                                                     String devtoken, AsyncHttpResponseHandler handler) {
        String url = getServiceBaseUrl() + "getPagedAcceptableOnceOrders";

        try {
            RequestParams params = new RequestParams();
            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("pageno", "" + pageno);
            params.put("order_type", "" + order_type);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);

        } catch (Exception ex) {
            ex.printStackTrace();

            if (handler != null)
                handler.onFailure(null, ex.getMessage());
        }
    }

    public static void getPagedAcceptableOnceOrders_get(long userid, int pageno, int order_type,
    String devtoken, AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "getPagedAcceptableOnceOrders";

        try
        {
            RequestParams params = new RequestParams();
            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("pageno", "" + pageno);
            params.put("order_type", "" + order_type);
            params.put("devtoken", devtoken);
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url,params, handler);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            if (handler != null)
                handler.onFailure(null, ex.getMessage());
        }
    }




	// 50) Get latest acceptable on off orders
	public static void getLatestAcceptableOnOffOrders(long userid, long limitid, String start_addr, String end_addr, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestAcceptableOnOffOrders";

		try
		{
			RequestParams params = new RequestParams();
			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("start_addr", start_addr);
			params.put("end_addr", end_addr);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 51) Get acceptable on off orders with specific page number
	public static void getPagedAcceptableOnOffOrders(long userid, int pageno, String start_addr, String end_addr, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPagedAcceptableOnOffOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("start_addr", start_addr);
			params.put("end_addr", end_addr);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}



	// 52) Get latest acceptable long orders
	public static void getLatestAcceptableLongOrders(long userid, long limitid, String start_addr, String end_addr, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLatestAcceptableLongOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("limitid", "" + limitid);
			params.put("start_addr", start_addr);
			params.put("end_addr", end_addr);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 53) Get latest acceptable long orders
	public static void getPagedAcceptableLongOrders(long userid, int pageno, String start_addr, String end_addr, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPagedAcceptableLongOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("pageno", "" + pageno);
			params.put("start_addr", start_addr);
			params.put("end_addr", end_addr);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 54) Get detailed driver order info
	public static void getDetailedDriverOrderInfo(long userid, long orderid, int order_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getDetailedDriverOrderInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("order_type", "" + order_type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 55) Get detailed passenger order info
	public static void getDetailedPassengerOrderInfo(long userid, long orderid, int order_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getDetailedPassengerOrderInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("orderid", Long.toString(orderid));
			params.put("order_type", Integer.toString(order_type));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());

			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 56) report driver position
	public static void reportDriverPos(long userid, double lat, double lng, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "reportDriverPos";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("lat", "" + lat);
			params.put("lng", "" + lng);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 57) get nearby drivers
	public static void getNearbyDrivers(long userid, double lat, double lng, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getNearbyDrivers";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("lat", Double.toString(lat));
			params.put("lng", Double.toString(lng));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 58) Get system average price information
	public static void getAveragePrice(long userid, String city, double start_lat, double start_lng,
                                       double end_lat, double end_lng, String start_time, String midpoints,
                                       String devtoken, AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "getSystemPriceInfo";

        try
        {
			RequestParams params = new RequestParams();

	        params.put("source", getSource());
	        params.put("userid", Long.toString(userid));
	        params.put("city", city);
	        params.put("start_lat", Double.toString(start_lat));
	        params.put("start_lng", Double.toString(start_lng));
	        params.put("end_lat", Double.toString(end_lat));
	        params.put("end_lng", Double.toString(end_lng));
	        params.put("start_time", start_time);
	        params.put("midpoints", midpoints);
	        params.put("devtoken", devtoken);

	        AsyncHttpClient client = new AsyncHttpClient();
	        client.setTimeout(getTimeOut());
	        client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


	// 59) Get all the brands and color information
    public static void getBrandsAndColors(AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "getBrandsAndColors";

        try
        {
	        RequestParams params = new RequestParams();

	        params.put("source", getSource());

	        AsyncHttpClient client = new AsyncHttpClient();
	        client.setTimeout(getTimeOut());
	        client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



	// 60) Get passenger detailed information
	public static void getPassengerInfo(long userid, long passid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPassengerInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("passid", Long.toString(passid));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 61) Get passenger latest evaluation
	public static void getPassengerLatestEvalInfo(long userid, long passid, long limitid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPassengerLatestEvalInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("passid", Long.toString(passid));
			params.put("limitid", Long.toString(limitid));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 62) Get passenger evaluations with specific page number
	public static void getPassengerPagedEvalInfo(long userid, long passid, int pageno, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getPassengerPagedEvalInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("passid", Long.toString(passid));
			params.put("pageno", Integer.toString(pageno));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 63) Get driver information
	public static void getDriverInfo(long userid, long driverid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getDriverInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("driverid", "" + driverid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 64) Get driver latest evaluation information
	public static void getDriverLatestEvalInfo(long userid, long driverid, long limitid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getDriverLatestEvalInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("driverid", Long.toString(driverid));
			params.put("limitid", Long.toString(limitid));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 65) Get driver evaluation information with specific page number
	public static void getDriverPagedEvalInfo(long userid, long driverid, int pageno, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getDriverPagedEvalInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("driverid", Long.toString(driverid));
			params.put("pageno", Integer.toString(pageno));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 66) Get driver position
	public static void getDriverPos(long userid, long driverid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getDriverPos";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("driverid", "" + driverid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 67) Get driver position
	public static void getLongOrderCancelInfo(long userid, long orderid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLongOrderCancelInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 68) Get detailed acceptable city order information
	public static void getAcceptableInCityOrderDetailInfo(long userid, long orderid, int order_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getAcceptableInCityOrderDetailInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("order_type", "" + order_type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 69) Get detailed acceptable long order information
	public static void getAcceptableLongOrderDetailInfo(long userid, long orderid, int order_type, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getAcceptableLongOrderDetailInfo";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
            params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("order_type", "" + order_type);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 70) Publish once order
	public static void publishOnceOrder(long userid,
	                                    String start_addr,
	                                    double start_lat,
	                                    double start_lng,
										String end_addr,
										double end_lat,
										double end_lng,
										String start_time,
										String midpoints,
										String remark,
										int reqstyle,
										double price,
										String city,
										String devtoken,
										AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "publishOnceOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("start_addr", start_addr);
			params.put("start_lat", "" + start_lat);
			params.put("start_lng", "" + start_lng);
			params.put("end_addr", end_addr);
			params.put("end_lat", "" + end_lat);
			params.put("end_lng", "" + end_lng);
			params.put("start_time", start_time);
			params.put("midpoints", midpoints);
			params.put("remark", remark);
			params.put("reqstyle", "" + reqstyle);
			params.put("price", "" + price);
			params.put("city", city);
			params.put("devtoken", devtoken);


			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 71) Publish on off duty order
	public static void publishOnOffOrder(long userid,
	                                    String start_addr,
	                                    double start_lat,
	                                    double start_lng,
	                                    String end_addr,
	                                    double end_lat,
	                                    double end_lng,
	                                    String start_time,
	                                    String midpoints,
	                                    String remark,
	                                    int reqstyle,
	                                    double price,
	                                    String days,
	                                    String city,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "publishOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

            params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("start_addr", start_addr);
			params.put("start_lat", "" + start_lat);
			params.put("start_lng", "" + start_lng);
			params.put("end_addr", end_addr);
			params.put("end_lat", "" + end_lat);
			params.put("end_lng", "" + end_lng);
			params.put("start_time", start_time);
			params.put("midpoints", midpoints);
			params.put("remark", remark);
			params.put("reqstyle", "" + reqstyle);
			params.put("price", "" + price);
			params.put("days", days);
			params.put("city", city);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 72) Publish on off duty order
	public static void publishLongOrder(long userid,
	                                     String start_addr,
	                                     String start_city,
	                                     double start_lat,
	                                     double start_lng,
	                                     String end_addr,
	                                     String end_city,
	                                     double end_lat,
	                                     double end_lng,
	                                     String start_time,
	                                     String remark,
	                                     double price,
	                                     int seats_count,
	                                     String devtoken,
	                                     AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "publishLongOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("start_addr", "" + start_addr);
			params.put("start_city", "" + start_city);
			params.put("start_lat", "" + start_lat);
			params.put("start_lng", "" + start_lng);
			params.put("end_addr", "" + end_addr);
			params.put("end_city", "" + end_city);
			params.put("end_lat", "" + end_lat);
			params.put("end_lng", "" + end_lng);
			params.put("start_time", "" + start_time);
			params.put("remark", "" + remark);
			params.put("price", "" + price);
			params.put("seats_count", "" + seats_count);
			params.put("devtoken", "" + devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();

			if (handler != null)
				handler.onFailure(null, ex.getMessage());
		}
	}


	// 73) Accept once order
	public static void acceptOnceOrder(long userid,
	                                   long orderid,
	                                   double lat,
	                                   double lng,
	                                   String devtoken,
	                                   AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "acceptOnceOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("lat", "" + lat);
			params.put("lng", "" + lng);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 74) Accept on off duty order
	public static void acceptOnOffOrder(long userid,
	                                    long orderid,
	                                    String days,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "acceptOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("days", days);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 75) Accept long order
	public static void acceptLongOrder(long userid,
	                                   long orderid,
	                                   int seats_count,
	                                   String devtoken,
	                                   AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "acceptLongOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("seats_count", "" + seats_count);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 76) Set long order password
	public static void setLongOrderPassword(long userid,
	                                        long orderid,
	                                        String password,
	                                        String devtoken,
	                                        AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "setLongOrderPassword";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 77) Confirm once order
	public static void confirmOnceOrder(long userid,
	                                    long orderid,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "confirmOnceOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 78) Cancel once order
	public static void cancelOnceOrder(long userid,
	                                     long orderid,
	                                     String devtoken,
	                                     AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "cancelOnceOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 79) Confirm on off duty order
	public static void confirmOnOffOrder(long userid,
	                                    long orderid,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "confirmOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 80) refuse on off order method
	public static void refuseOnOffOrder(long userid, long orderid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "refuseOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 81) cancel on off order method
	public static void cancelOnOffOrder(long userid, long orderid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "cancelOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 82) Execute once order
	public static void executeOnceOrder(long userid,
	                                    long orderid,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "executeOnceOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 83) Execute on off duty order
	public static void executeOnOffOrder(long userid,
	                                     long orderid,
	                                     String devtoken,
	                                     AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "executeOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 84) Execute on off duty order
	public static void executeLongOrder(long userid,
	                                    long orderid,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "executeLongOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 85) Check once order acceptance
	public static void checkOnceOrderAcceptance(long userid,
	                                            long orderid,
	                                            double lat,
	                                            double lng,
	                                            String devtoken,
	                                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "checkOnceOrderAcceptance";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("lat", "" + lat);
			params.put("lng", "" + lng);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 86) Check once order agreement
	public static void checkOnceOrderAgree(long userid,
	                                            long orderid,
	                                            String devtoken,
	                                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "checkOnceOrderAgree";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 87) Sign once order driver arrival
	public static void signOnceOrderDriverArrival(long driverid,
	                                              long orderid,
	                                              String devtoken,
	                                              AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signOnceOrderDriverArrival";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 88) Sign on off order driver arrival
	public static void signOnOffOrderDriverArrival(long driverid,
	                                               long orderid,
	                                               String devtoken,
	                                               AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signOnOffOrderDriverArrival";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 89) Sign long order driver arrival
	public static void signLongOrderDriverArrival(long driverid,
	                                              long orderid,
	                                              String devtoken,
	                                              AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signLongOrderDriverArrival";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 90) Sign once order passenger upload
	public static void signOnceOrderPassengerUpload(long driverid,
	                                                long orderid,
	                                                long passid,
	                                                String password,
	                                                String devtoken,
	                                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signOnceOrderPassengerUpload";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("passid", "" + passid);
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 91) Sign once order passenger upload
	public static void signOnOffOrderPassengerUpload(long driverid,
	                                                 long orderid,
	                                                 long passid,
	                                                 String password,
	                                                 String devtoken,
	                                                 AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signOnOffOrderPassengerUpload";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("passid", "" + passid);
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 92) Sign long order passenger upload
	public static void signLongOrderPassengerUpload(long driverid,
	                                                 long orderid,
	                                                 long passid,
	                                                 String password,
	                                                 String devtoken,
	                                                 AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signLongOrderPassengerUpload";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("passid", "" + passid);
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 93) Sign long order passenger give up
	public static void signLongOrderPassengerGiveup(long driverid,
	                                                long orderid,
	                                                long passid,
	                                                String devtoken,
	                                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "signLongOrderPassengerGiveup";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("passid", "" + passid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 94) Sign long order passenger give up
	public static void startLongOrderDriving(long driverid,
	                                         long orderid,
	                                         String devtoken,
	                                         AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "startLongOrderDriving";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 95) End once order information
	public static void endOnceOrder(long driverid,
	                                long orderid,
	                                String devtoken,
	                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "endOnceOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 96) End once order information
	public static void endOnOffOrder(long driverid,
	                                 long orderid,
	                                 String devtoken,
	                                 AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "endOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 97) End once order information
	public static void endLongOrder(long driverid,
	                                long orderid,
	                                String devtoken,
	                                AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "endLongOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 98) Evaluate once order passenger
	public static void evaluateOnceOrderPass(long driverid,
	                                         long passid,
	                                         long orderid,
	                                         int level,
	                                         String msg,
	                                         String devtoken,
	                                         AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "evaluateOnceOrderPass";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("passid", "" + passid);
			params.put("orderid", "" + orderid);
			params.put("level", "" + level);
			params.put("msg", msg);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 99) Evaluate on off duty order passenger
	public static void evaluateOnOffOrderPass(long driverid,
	                                         long passid,
	                                         long orderid,
	                                         int level,
	                                         String msg,
	                                         String devtoken,
	                                         AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "evaluateOnOffOrderPass";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("passid", "" + passid);
			params.put("orderid", "" + orderid);
			params.put("level", "" + level);
			params.put("msg", msg);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 100) Evaluate long order passenger
	public static void evaluateLongOrderPass(long driverid,
	                                         long passid,
	                                         long orderid,
	                                         int level,
	                                         String msg,
	                                         String devtoken,
	                                         AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "evaluateLongOrderPass";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("passid", "" + passid);
			params.put("orderid", "" + orderid);
			params.put("level", "" + level);
			params.put("msg", msg);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 101) Evaluate once order driver
	public static void evaluateOnceOrderDriver(long passid,
	                                           long driverid,
	                                           long orderid,
	                                           int level,
	                                           String msg,
	                                           String devtoken,
	                                           AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "evaluateOnceOrderDriver";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("passid", "" + passid);
			params.put("orderid", "" + orderid);
			params.put("level", "" + level);
			params.put("msg", msg);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 102) Evaluate on off duty order driver
	public static void evaluateOnOffOrderDriver(long passid,
	                                            long driverid,
	                                            long orderid,
	                                            int level,
	                                            String msg,
	                                            String devtoken,
	                                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "evaluateOnOffOrderDriver";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("passid", "" + passid);
			params.put("orderid", "" + orderid);
			params.put("level", "" + level);
			params.put("msg", msg);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 103) Evaluate on off duty order driver
	public static void evaluateLongOrderDriver(long passid,
	                                            long driverid,
	                                            long orderid,
	                                            int level,
	                                            String msg,
	                                            String devtoken,
	                                            AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "evaluateLongOrderDriver";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("passid", "" + passid);
			params.put("orderid", "" + orderid);
			params.put("level", "" + level);
			params.put("msg", msg);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 104) Pause on off order
	public static void pauseOnOffOrder(long passid,
	                                   long orderid,
	                                   String devtoken,
	                                   AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "pauseOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("passid", Long.toString(passid));
			params.put("orderid", Long.toString(orderid));
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 105) Pause on off order
	public static void reserveNextOnOffOrder(long passid,
	                                         long orderid,
	                                         String password,
	                                         String devtoken,
	                                         AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "reserveNextOnOffOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("passid", Long.toString(passid));
			params.put("orderid", Long.toString(orderid));
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}




	// 106) Get valid coupons
	public static void getUsableCoupons(long userid,
	                                      String devtoken,
	                                      AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getUsableCoupons";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 107) Get valid coupons
	public static void changeOnceOrderPrice(long userid,
	                                        long orderid,
	                                        int wait_time,
	                                        double new_price,
	                                        String devtoken,
	                                        AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "changeOnceOrderPrice";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("orderid", Long.toString(orderid));
			params.put("wait_time", "" + wait_time);
			params.put("new_price", "" + new_price);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 108) Normal pay
	public static void payNormalOrder(long userid,
	                                    long orderid,
	                                    int order_type,
	                                    double price,
	                                    String coupons,
	                                    String devtoken,
	                                    AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "payNormalOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("orderid", Long.toString(orderid));
			params.put("order_type", "" + order_type);
			params.put("price", "" + price);
			params.put("coupons", "" + coupons);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 109) Reserve pay
	public static void payReserveOrder(long userid,
	                                   long orderid,
	                                   double price,
	                                   String coupons,
	                                   String devtoken,
	                                   AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "payReserveOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("orderid", Long.toString(orderid));
			params.put("price", "" + price);
			params.put("coupons", "" + coupons);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 110) Check if order is cancelled
	public static void orderCancelled(long userid,
	                                   long orderid,
	                                   int order_type,
                                       double distance,
	                                   String devtoken,
	                                   AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "orderCancelled";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("orderid", Long.toString(orderid));
			params.put("order_type", "" + order_type);
			params.put("distance", "" + distance);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 111) Get system information fee calculate method
	public static void getInfoFeeCalcMethod(long userid,
	                                        String city,
	                                        String devtoken,
	                                        AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getInfoFeeCalcMethod";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", Long.toString(userid));
			params.put("city", city);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 112) Get share link address
	public static void getShareLinkAddr(AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getShareLinkAddr";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 113) Cancel long order
	public static void cancelLongOrder(long driverid, long orderid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "cancelLongOrder";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("driverid", "" + driverid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 114) Stop on off duty order
    public static void stopOnOffOrder(long driverid,
                                     long orderid,
                                     String devtoken,
                                     AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "stopOnOffOrder";

        try
        {
	        RequestParams params = new RequestParams();

	        params.put("source", getSource());
	        params.put("driverid", "" + driverid);
	        params.put("orderid", "" + orderid);
	        params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


	// 115) Set once order password
	public static void setOnceOrderPassword(long userid,
	                                  long orderid,
	                                  String password,
	                                  String devtoken,
	                                  AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "setOnceOrderPassword";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	// 116) Set on off order password
	public static void setOnoffOrderPassword(long userid,
	                                        long orderid,
	                                        String password,
	                                        String devtoken,
	                                        AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "setOnoffOrderPassword";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("password", password);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 117) Send notification to drivers
	public static void sendNotificationToDrivers(long userid,
	                                         long orderid,
	                                         int pub_index,
	                                         String devtoken,
	                                         AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "sendNotificationToDrivers";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("pub_index", "" + pub_index);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	// 118) Insert driver acceptable drivers
	public static void insertDriverAcceptableOrders(long userid,
	                                             String devtoken,
	                                             AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "insertDriverAcceptableOrders";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

    public static void changeUserPhoto(long userid,
                                       String userphoto,
                                       String devtoken,
                                       AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "changeUserPhoto";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("userphoto", userphoto);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public static void changeCarImage(long userid,
                                       String carimg,
                                       String devtoken,
                                       AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "changeCarImage";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("carimg", carimg);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

	public static void logoutUser(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "logoutUser";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public static void getOnceOrderDriverPos(long userid, long orderid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getOnceOrderDriverPos";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("orderid", "" + orderid);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public static void checkOrderNotifRead(long userid, String idarray, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "checkOrderNotifRead";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("idarray", idarray);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}



	public static void checkPersonNotifRead(long userid, String idarray, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "checkPersonNotifRead";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("idarray", idarray);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	public static void readAnnouncement(long userid, String annids, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "readAnnouncement";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", getSource());
			params.put("userid", "" + userid);
			params.put("annids", annids);
			params.put("devtoken", devtoken);

			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(getTimeOut());
			client.post(url, params, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

    public static void clickchargingbtn(long userid, long orderid,String devtoken, AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "clickchargingbtn";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("orderid", "" + orderid);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            if (handler != null)
                handler.onFailure(null, ex.getMessage());
        }
    }

    public static void has_clickedchargingbtn
    (long userid, long orderid, String devtoken, AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "has_clickedchargingbtn";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("orderid", "" + orderid);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            if (handler != null)
                handler.onFailure(null, ex.getMessage());
        }
    }

    public static void readPopUpQueryTime(long userid, String devtoken, AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "pollingTime";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void timeleft(long userid, long orderid,String devtoken, AsyncHttpResponseHandler handler)
    {
        String url = getServiceBaseUrl() + "time_left";

        try
        {
            RequestParams params = new RequestParams();

            params.put("orderid", ""+orderid);
            params.put("userid", "" + userid);
            params.put("devtoken", devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
