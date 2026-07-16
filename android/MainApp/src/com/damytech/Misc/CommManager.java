package com.damytech.Misc;

import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
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
	public static int getTimeOut() { return 5 * 1000; }
//	public static String getServiceBaseUrl() { return "http://sypic.oicp.net:10100/PincheService/webservice/"; }
//	public static String getServiceBaseUrl() { return "http://192.168.1.79:10100/PincheService/webservice/"; }
	public static String getServiceBaseUrl() { return "http://218.25.54.28:30100/PincheService/webservice/"; }
//	public static String getServiceBaseUrl() { return "http://192.168.0.88:8080/PincheService/webservice/"; }
//	public static String getServiceBaseUrl() { return "http://124.207.135.69:8080/PincheService/webservice/"; }

    private static String getSource()
    {
        return "0";
    }

    private static String getPlatform()
    {
        return "2";
    }

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

        String url = "http://traffic.kakamobi.com/weather.ashx";

        try
        {
            RequestParams params = new RequestParams();

            params.put("city", strCityName);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.get(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

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
		}
	}

	/*****************************************************************************************/
	/******************************* Main web api call methods *******************************/
	/*****************************************************************************************/

	public static void getChildAppList(AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getChildAppList";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("platform", getPlatform());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}

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
            params.put("city", "" + city);
            if(isDriverVerif)
                params.put("driververif", "1");
            else
                params.put("driververif", "0");
            params.put("limitid", "" + limitid);
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
            params.put("city", "" + city);
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
        }
	}



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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


	public static void recordDownload(String packname, String version, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "recordDownload";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("platformid", getPlatform());
            params.put("packname", packname);
            params.put("version", version);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}


	public static void registerUser(String username,
	                                String mobile,
	                                String nickname,
	                                String password,
	                                String invitecode,
	                                int sex,
	                                String city,
	                                String devtoken,
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
            params.put("devtoken", devtoken);
            params.put("platform", getPlatform());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}

	public static void getVerifKey(String mobile, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getVerifKey";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("mobile", mobile);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}


	public static void loginUser(String username, String password, String city, String devtoken, AsyncHttpResponseHandler handler)
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
            params.put("platform", getPlatform());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}


	public static void forgetPassword(String username, String mobile, String password, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "forgetPassword";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("username", username);
            params.put("password", "" + password);
            params.put("mobile", "" + mobile);
            params.put("devtoken", "" + devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}


	public static void getUserInfo(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getUserInfo";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
            params.put("userid", "" + userid);
            params.put("devtoken", "" + devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}


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

			params.put("source", "0");
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
		}
	}


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
            params.put("devtoken", "" + devtoken);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(getTimeOut());
            client.post(url, params, handler);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
	}


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

			params.put("source", "0");
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
		}
	}


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
            if(driververif)
                params.put("driververif", "1");
            else
                params.put("driververif", "0");
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
        }
	}


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
        }
	}


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
        }
	}

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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


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
        }
	}


	public static void advanceOpinion(long userid, String contents, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "advanceOpinion";

		try
		{
			RequestParams params = new RequestParams();

			params.put("source", "0");
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
		}
	}


	public static void defaultShareContents(long userid, String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "defaultShareContents";

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


	public static void getLoginInfoFromDevToken(String devtoken, AsyncHttpResponseHandler handler)
	{
		String url = getServiceBaseUrl() + "getLoginInfoFromDevToken";

        try
        {
            RequestParams params = new RequestParams();

            params.put("source", getSource());
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
        }
	}


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


	public static void changeUserPhoto(long userid, String userphoto, String devtoken, AsyncHttpResponseHandler handler)
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


}
