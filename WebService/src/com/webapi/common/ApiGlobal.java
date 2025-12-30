package com.webapi.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.w3c.dom.Document;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.webapi.announcement.action.SVCAnnouncementAction;
import com.webapi.structure.STPushNotificationData;
import com.webapi.structure.SVCAbbRecord;
import com.webapi.structure.SVCAnnouncement;
import com.webapi.structure.SVCAppSpreadUnit;
import com.webapi.structure.SVCAppVersion;
import com.webapi.structure.SVCCity;
import com.webapi.structure.SVCEvaluationCS;
import com.webapi.structure.SVCFreezePoints;
import com.webapi.structure.SVCGlobalParams;
import com.webapi.structure.SVCGroup;
import com.webapi.structure.SVCNotifyOrder;
import com.webapi.structure.SVCNotifyPerson;
import com.webapi.structure.SVCOrderExecCS;
import com.webapi.structure.SVCOrderLongDistanceDetails;
import com.webapi.structure.SVCOrderOnOffDutyDetails;
import com.webapi.structure.SVCOrderOnOffDutyGrab;
import com.webapi.structure.SVCOrderTempDetails;
import com.webapi.structure.SVCOrderTempGrab;
import com.webapi.structure.SVCProfitSharingConfigPerson;
import com.webapi.structure.SVCTs;
import com.webapi.structure.SVCUser;
import com.webapi.structure.SVCUserCar;
import com.webapi.structure.SVCUserLogin;

// Class for all of the global functions
public class ApiGlobal {
	// Order acceptance flags and methods
	private static ArrayList<Long> arrOnceOrderIDs = new ArrayList<Long>();
	private static ArrayList<Long> arrLongOrderIDs = new ArrayList<Long>();

	public static synchronized boolean lockOnceOrderAcceptance(long orderid) {
		System.out.println("lockOnceOrderAcceptance : " + orderid + "  "
				+ (new Date()).toString());

		if (arrOnceOrderIDs.contains(orderid)) {
			System.out.println("failure\n\n");
			return false;
		}

		arrOnceOrderIDs.add(orderid);
		System.out.println("success\n\n");
		return true;
	}

	public static synchronized void unlockOnceOrderAcceptance(long orderid) {
		if (!arrOnceOrderIDs.contains(orderid))
			return;

		arrOnceOrderIDs.remove(orderid);
	}

	public static synchronized boolean lockLongOrderAcceptance(long orderid) {
		if (arrLongOrderIDs.contains(orderid))
			return false;

		arrLongOrderIDs.add(orderid);
		return true;
	}

	public static synchronized void unlockLongOrderAcceptance(long orderid) {
		if (!arrLongOrderIDs.contains(orderid))
			return;

		arrLongOrderIDs.remove(orderid);
	}

	public static int getPageItemCount() {
		return 10;
	}

	public static int defaultBalance() {
		return 0;
	}

	public static String inviteCodeCharSet() {
		// return "RSTUVWXYZ0123456789ABCDEFGHJKLMNPQ";
		return "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	}

	public static String getServiceCall() {
		return "02968792305";
	}

	public static int userInviteCodeLen() {
		return 6;
	}

	public static int appSpreadUnitInviteCodeLen() {
		return 7;
	}

	public static int reqOperNumLen() {
		return 12;
	}

	public static int groupInviteCodeLen() {
		return 8;
	}

	public static int singleCouponCodeLen() {
		return 7;
	}

	public static String getValueFromConnectionXML(String nodeName) {
		String szResult = "";
		try {
			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = docBF.newDocumentBuilder();
			String szPath = "";
			try {
				szPath = DBManager.class.getResource("/").getPath();
				szPath = szPath.substring(1);
				szPath = "/" + URLDecoder.decode(szPath, "utf-8")
						+ "connection.xml";// 在linux环境下，需要加一条“/”，不然找不到文件
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			Document doc = db.parse(new FileInputStream(szPath));

			szResult = doc.getElementsByTagName(nodeName).item(0)
					.getFirstChild().getNodeValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return szResult;

	}

	public static String getBasePath() {
		return getValueFromConnectionXML("BasePath");
		// return "E:/Work/22_BJPC/Source/img/";
	}

	public static String getBaseURL() {
		return getValueFromConnectionXML("BaseUrl");
		// return "http://124.207.135.69:8080/img/";
	}

	// Function to check if source is available
	public static boolean IsValidSource(String source) {
		if (source == null)
			return false;

		return source.equals(ConstMgr.SRC_MAINAPP)
				|| source.equals(ConstMgr.SRC_PINCHEAPP)
				|| source.equals(ConstMgr.SRC_OOHUNTING);// 增加了游戏端登录源
	}

	// Function to compare two dates
	public static boolean isSameDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		cal1.setTime(date1);
		cal2.setTime(date2);

		if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR))
			return false;

		if (cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH))
			return false;

		if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH))
			return false;

		if (cal1.get(Calendar.HOUR_OF_DAY) != cal2.get(Calendar.HOUR_OF_DAY))
			return false;

		if (cal1.get(Calendar.MINUTE) != cal2.get(Calendar.MINUTE))
			return false;

		if (cal1.get(Calendar.SECOND) != cal2.get(Calendar.SECOND))
			return false;

		if (cal1.get(Calendar.MILLISECOND) != cal2.get(Calendar.MILLISECOND))
			return false;

		return true;
	}

	// Date to String conversion
	public static String Date2String(Date dtvalue, boolean withTime) {
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

	// Date to String conversion
	public static String Date2StringWithoutSeconds(Date dtvalue) {
		String szResult = "";
		if (dtvalue == null)
			return "";

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		szResult = df.format(dtvalue);

		return szResult;
	}

	// Date to Time conversion
	public static String Date2Time(Date dtvalue) {
		String szResult = "";
		if (dtvalue == null)
			return "";

		DateFormat df = new SimpleDateFormat("HH:mm");
		szResult = df.format(dtvalue);

		return szResult;
	}

	// String to date conversion
	public static Date String2Date(String szTime) {
		if (szTime == null || szTime.equals("")
				|| szTime.contains("0000-00-00"))
			return null;

		DateFormat df = null;
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dtValue = null;
		try {
			dtValue = df.parse(szTime);
		} catch (Exception ex) {
			df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dtValue = df.parse(szTime);
			} catch (Exception ex2) {
				ex.printStackTrace();
			}
		}

		return dtValue;
	}

	// Method to return profit sharing config info
	public static SVCProfitSharingConfigPerson getGlobalProfitSharingConfigPerson(
			Connection conn) {
		SVCProfitSharingConfigPerson config_info = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = conn.createStatement();
			szQuery = "SELECT * FROM profitsharing_config_person where deleted = 0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				config_info = SVCProfitSharingConfigPerson
						.decodeFromResultSet(rs);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return config_info;
	}

	// Function to remove character-'shi' at the end
	public static String removeCityCharacter(String szCityName) {
		String szCity = szCityName;

		if (szCity.length() == 0)
			return szCity;

		if (szCityName.charAt(szCityName.length() - 1) == ConstMgr.STR_CITY
				.charAt(0)) // The last character is 'shi'
		{
			szCity = szCityName.substring(0, szCityName.length() - 1);
		}

		return szCity;
	}

	// Function to remove string-'xiansheng' at the end
	public static String removeNickNameSuffix(String szNickName) {
		String szResult = szNickName;

		if (szResult.length() > 2
				&& (szResult.substring(szResult.length() - 2).equals(
						ConstMgr.STR_NICKNAME_MALE) || szResult.substring(
						szResult.length() - 2).equals(
						ConstMgr.STR_NICKNAME_FEMALE))) {
			szResult = szResult.substring(0, szResult.length() - 2);
		}

		return szResult;
	}

	// Method to get city code from name
	public static String cityName2Code(Connection conn, String szCityName) {
		if (szCityName == null || szCityName.equals(""))
			return "";

		String szCode = "";
		String szCity = ApiGlobal.removeCityCharacter(szCityName);

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = conn.createStatement();

			szQuery = "SELECT code FROM city where deleted = 0 AND name LIKE \"%"
					+ szCity + "%\"";

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				szCode = rs.getString("code");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

		return szCode;
	}

	// Method to get city code from name
	public static String cityCode2Name(Connection conn, String szCityCode) {
		if (szCityCode == null || szCityCode.equals(""))
			return "";

		String szName = "";

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = conn.createStatement();

			szQuery = "SELECT name FROM city where deleted = 0 AND code = \""
					+ szCityCode + "\"";

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				szName = rs.getString("name");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return szName;
	}

	// Get the city name in which the phone is belonged
	public static String getCityOfPhone(String mobile) {
		String szCity = null;
		String url_str = "http://api.k780.com:88/?app=phone.get&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json&phone="
				+ mobile;

		try {
			URL url = new URL(url_str);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty("Accept-Charset", "UTF-8");

			InputStream response = conn.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response, "UTF-8"));
			String szResult = "", szLine = "";
			while ((szLine = reader.readLine()) != null) {
				szResult += szLine;
			}

			JSONObject jsonObj = JSONObject.fromObject(szResult);
			JSONObject result = jsonObj.getJSONObject("result");

			String szAddrComponents = result.getString("att");
			int nIndex = szAddrComponents.lastIndexOf(',');
			szCity = szAddrComponents.substring(nIndex + 1);
		} catch (Exception ex) {
			ex.printStackTrace();
			szCity = null;
		}

		return szCity;
	}

	// Function to generate user invite code(Random)
	public static String generateUserInviteCode() {
		StringBuffer buffer = new StringBuffer();
		String characters = inviteCodeCharSet();
		int totalLen = characters.length();
		int reqLen = userInviteCodeLen();

		for (int i = 0; i < reqLen; i++) {
			double index = Math.random() * totalLen;
			buffer.append(characters.charAt((int) index));
		}

		return buffer.toString();
	}

	// Function to generate once order number
	public static String generateOnceOrderNum() {
		String ordernum = "";

		ordernum = "Order" + (new Date()).getTime() + genRand(3);

		return ordernum;
	}

	// Function to generate on off order number
	public static String generateOnOffOrderNum() {
		String ordernum = "";

		ordernum = "OnOff" + (new Date()).getTime() + genRand(3);

		return ordernum;
	}

	// Function to generate long order number
	public static String generateLongOrderNum() {
		String ordernum = "";

		ordernum = "Long" + (new Date()).getTime() + genRand(3);

		return ordernum;
	}

	// Function to generate app spread unit invite code(Random)
	public static String generateAppSpreadUnitInviteCode() {
		StringBuffer buffer = new StringBuffer();
		String characters = inviteCodeCharSet();
		int totalLen = characters.length();
		int reqLen = appSpreadUnitInviteCodeLen();

		for (int i = 0; i < reqLen; i++) {
			double index = Math.random() * totalLen;
			buffer.append(characters.charAt((int) index));
		}

		return buffer.toString();
	}

	// Function to generate User Request Operation code(Random)
	public static String generateReqOperNum() {
		StringBuffer buffer = new StringBuffer();
		String characters = inviteCodeCharSet();
		int totalLen = characters.length();
		int reqLen = reqOperNumLen();

		for (int i = 0; i < reqLen; i++) {
			double index = Math.random() * totalLen;
			buffer.append(characters.charAt((int) index));
		}

		return buffer.toString();
	}

	// Function to generate group invite code(Random)
	public static String generateGroupInviteCode() {
		StringBuffer buffer = new StringBuffer();
		String characters = inviteCodeCharSet();
		int totalLen = characters.length();
		int reqLen = groupInviteCodeLen();

		for (int i = 0; i < reqLen; i++) {
			double index = Math.random() * totalLen;
			buffer.append(characters.charAt((int) index));
		}

		return buffer.toString();
	}

	// Function to generate single coupon code(Random)
	public static String generateSingleCouponCode() {
		String szResult = "";

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		szResult = df.format(new Date());

		return "SICP" + szResult + genRand(3);
	}

	// Function to generate random number series with specified length
	public static String genRand(int nLen) {
		StringBuffer buffer = new StringBuffer();
		String characters = "0123456789";
		int totalLen = characters.length();
		int reqLen = nLen;

		for (int i = 0; i < reqLen; i++) {
			double index = Math.random() * totalLen;
			buffer.append(characters.charAt((int) index));
		}

		return buffer.toString();
	}

	// /**
	// * 此方法可能会导致数字重复，输入0和输入34返回同一个值，此方法废弃
	// * @param value
	// * @param len
	// * @return
	// */
	// // Function to convert long value to string with specific length
	// public static String long2NLenString(long value, int len) {
	// String result = "";
	// char[] arrResult = new char[len];
	//
	// long nDiv = 0;
	// int nMod = 0;
	// //String charset = inviteCodeCharSet();
	// String charset="RSTUVWXYZ0123456789ABCDEFGHJKLMNPQ";
	// int nCharCount = charset.length();
	// int nCurLen = 0;
	//
	// nMod = (int) (value % nCharCount);
	// nDiv = value / nCharCount;
	//
	// while (nDiv > 0 && nCurLen < len) {
	// arrResult[nCurLen] = charset.charAt(nMod);
	// nCurLen++;
	//
	// nDiv = nDiv / nCharCount;
	// nMod = (int) (nDiv % nCharCount);
	// }
	//
	// while (nCurLen < len) {
	// arrResult[nCurLen] = charset.charAt(nMod);
	// nCurLen++;
	// nMod = 0;
	// }
	//
	// for (int i = nCurLen - 1; i >= 0; i--) {
	// result += arrResult[i];
	// }
	//
	// return result;
	// }

	/**
	 * 将长整型数字转换为34进制数字,这个在jdk8下能用
	 * 
	 * @param userid
	 * @return
	 * 
	 *         public static String longTo6LenString(long userid) { String
	 *         result = "";
	 * 
	 *         long value=userid;
	 * 
	 *         long p6 = value / (34 * 34 * 34 * 34 * 34);
	 * 
	 *         value = value - p6 * 34 * 34 * 34 * 34 * 34; long p5 = value /
	 *         (34 * 34 * 34 * 34);
	 * 
	 *         value = value - p5 * 34 * 34 * 34 * 34; long p4 = value / (34 *
	 *         34 * 34);
	 * 
	 *         value = value - p4 * 34 * 34 * 34; long p3 = value / (34 * 34);
	 * 
	 *         value = value - p3 * 34 * 34; long p2 = value / 34;
	 * 
	 *         value = value - p2 * 34; long p1 = value;
	 * 
	 *         //System.out.println(" "+p6+" "+p5+" "+p4+" "+p3+" "+p2+" "+p1+
	 *         " ");
	 *         result=get34Char(p6)+get34Char(p5)+get34Char(p4)+get34Char(p3
	 *         )+get34Char(p2)+get34Char(p1); return result; }
	 */
	/**
	 * 将长整型数字转换为34进制数字,这个在jdk7下能用
	 * 
	 * @param userid
	 * @return
	 */
	public static String long2NLenString(long userid, int len) {
		String result = "";

		long nVal = userid;
		long nMod = 0;

		while (nVal > 0) {
			nMod = nVal % 34;
			nVal = nVal / 34;

			result = get34Char(BigInteger.valueOf(nMod)) + result;
		}

		if (result.length() > len) {
			result = result.substring(result.length() - len);
		} else {
			int nCurLen = result.length();
			for (int i = 0; i < len - nCurLen; i++) {
				result = get34Char(BigInteger.valueOf(0)) + result;
			}
		}

		// BigInteger value=BigInteger.valueOf(userid);
		// BigInteger p6=value.divide(BigInteger.valueOf(34L*34L*34L*34L*34L));
		//
		// value=value.subtract(BigInteger.valueOf(34L*34L*34L*34L*34L).multiply(p6));
		// if(value.compareTo(BigInteger.valueOf(0))<=0){
		// value=BigInteger.valueOf(0);
		// }
		// BigInteger p5=value.divide(BigInteger.valueOf(34L*34L*34L*34L));
		//
		//
		// value=value.subtract(BigInteger.valueOf(34L*34L*34L*34L).multiply(p5));
		// if(value.compareTo(BigInteger.valueOf(0))<=0){
		// value=BigInteger.valueOf(0);
		// }
		// BigInteger p4=value.divide(BigInteger.valueOf(34L*34L*34L));
		//
		//
		// value=value.subtract(BigInteger.valueOf(34L*34L*34L).multiply(p4));
		// if(value.compareTo(BigInteger.valueOf(0))<=0){
		// value=BigInteger.valueOf(0);
		// }
		// BigInteger p3=value.divide(BigInteger.valueOf(34L*34L));
		//
		// value=value.subtract(BigInteger.valueOf(34L*34L).multiply(p3));
		// if(value.compareTo(BigInteger.valueOf(0))<=0){
		// value=BigInteger.valueOf(0);
		// }
		// BigInteger p2=value.divide(BigInteger.valueOf(34L));
		//
		// value=value.subtract(BigInteger.valueOf(34L).multiply(p2));
		// if(value.compareTo(BigInteger.valueOf(0))<=0){
		// value=BigInteger.valueOf(0);
		// }
		// BigInteger p1=value.divide(BigInteger.valueOf(1));
		//
		// //System.out.println(" "+p6+" "+p5+" "+p4+" "+p3+" "+p2+" "+p1+" ");
		// result=get34Char(p6)+get34Char(p5)+get34Char(p4)+get34Char(p3)+get34Char(p2)+get34Char(p1);
		return result;
	}

	public static String get34Char(BigInteger value) {
		int val = value.intValue();
		switch (val) {
		case 0:
			return "0";
		case 1:
			return "1";
		case 2:
			return "2";
		case 3:
			return "3";
		case 4:
			return "4";
		case 5:
			return "5";
		case 6:
			return "6";
		case 7:
			return "7";
		case 8:
			return "8";
		case 9:
			return "9";
		case 10:
			return "A";
		case 11:
			return "B";
		case 12:
			return "C";
		case 13:
			return "D";
		case 14:
			return "E";
		case 15:
			return "F";
		case 16:
			return "G";
		case 17:
			return "H";
		case 18:
			return "J";
		case 19:
			return "K";
		case 20:
			return "L";
		case 21:
			return "M";
		case 22:
			return "N";
		case 23:
			return "P";
		case 24:
			return "Q";
		case 25:
			return "R";
		case 26:
			return "S";
		case 27:
			return "T";
		case 28:
			return "U";
		case 29:
			return "V";
		case 30:
			return "W";
		case 31:
			return "X";
		case 32:
			return "Y";
		case 33:
			return "Z";

		}
		return null;
	}

	public static void main(String args[]) {
		String dd = long2NLenString(34L * 34L * 34L * 34L * 34L * 34L - 1, 6);

		System.out.println(dd);
	}

	// MD5 hash encoding method
	public static String MD5Hash(String value) {
		String result = "";

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(value.getBytes());

			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16)
						.substring(1));
			}

			result = sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	// Function to get full URL of image saved on the server
	public static String getAbsoluteURL(String relPath) {
		if (relPath == null || relPath.equals(""))
			return "";

		if (relPath.contains("http://") || relPath.contains("https://"))
			return relPath;

		while (relPath.length() > 0 && relPath.charAt(0) == '/') {
			relPath = relPath.substring(1);
		}

		return getBaseURL() + relPath;
	}

	// Function to save image to server with path and file name specified
	private static String saveImage(String image, String relPath,
			String filename) {
		String szResult = null;

		try {
			byte[] imageBuf = Base64.decode(image);

			File pathDir = new File(getBasePath() + relPath);
			if (!pathDir.exists())
				pathDir.mkdir();

			File newFile = new File(getBasePath() + relPath + filename);
			if (!newFile.exists())
				newFile.createNewFile();

			FileOutputStream ostream = new FileOutputStream(newFile);
			ostream.write(imageBuf);
			ostream.flush();
			ostream.close();

			szResult = relPath + filename;
		} catch (Exception ex) {
			ex.printStackTrace();
			szResult = null;
		}

		return szResult;
	}

	// Function to save user photo in the fixed path
	public static String saveUserPhoto(String image) {
		String szRelPath = "image/UserImage/";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss_");
		String fileName = df.format(new Date()) + ApiGlobal.genRand(3) + ".png";

		try {
			return saveImage(image, szRelPath, fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Function to save id card image in the fixed path
	public static String saveIdCardImage(String image, boolean foreImage) {
		String szRelPath = "image/idcard/";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss_");
		String fileName = "";
		if (foreImage)
			fileName = "idfore_" + df.format(new Date()) + ApiGlobal.genRand(3)
					+ ".png";
		else
			fileName = "idback_" + df.format(new Date()) + ApiGlobal.genRand(3)
					+ ".png";

		try {
			return saveImage(image, szRelPath, fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Function to save driver license image in the fixed path
	public static String saveDriverLicenceImage(String image, boolean foreImage) {
		String szRelPath = "image/licenceImage/";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss_");
		String fileName = "";
		if (foreImage)
			fileName = "driverlicence_fore_" + df.format(new Date())
					+ ApiGlobal.genRand(3) + ".png";
		else
			fileName = "driverlicence_back_" + df.format(new Date())
					+ ApiGlobal.genRand(3) + ".png";

		try {
			return saveImage(image, szRelPath, fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Function to save driving license image in the fixed path
	public static String saveDrivingLicenceImage(String image, boolean foreImage) {
		String szRelPath = "image/licenceImage/";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss_");
		String fileName = "";
		if (foreImage)
			fileName = "drivinglicence_fore_" + df.format(new Date())
					+ ApiGlobal.genRand(3) + ".png";
		else
			fileName = "drivinglicence_back_" + df.format(new Date())
					+ ApiGlobal.genRand(3) + ".png";

		try {
			return saveImage(image, szRelPath, fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Function to save car image in the fixed path
	public static String saveCarImage(String image) {
		String szRelPath = "image/carimage/";
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss_");
		String fileName = "carimg_" + df.format(new Date())
				+ ApiGlobal.genRand(3) + ".png";

		try {
			return saveImage(image, szRelPath, fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Function to correct the encoding of string to utf-8
	public static String fixEncoding(String latin) {
		// try {
		// if (!isMessyCode(latin))
		// return latin;
		// else
		// return new String(latin.getBytes("ISO-8859-1"), "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // Impossible, throw unchecked
		// throw new IllegalStateException("No Latin1 or UTF-8: "
		// + e.getMessage());
		// }

		return latin;
	}

	// Function to discern if character is Chinese character or not
	public static boolean isNumber(char c) {
		return (c >= '0' && c <= '9');
	}

	// Function to discern if character is Chinese character or not
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

	// Function to discern if character is messy code or not
	public static boolean isMessyCode(String strName) {
		Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
		Matcher m = p.matcher(strName);

		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");

		char[] ch = temp.trim().toCharArray();

		float chLength = ch.length;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
					System.out.print(c);
				}
			}
		}

		float result = count / chLength;
		if (result > 0.4)
			return true;
		else
			return false;
	}

	// Return distance between two coordinates with Baidu API
	public static double calcDistWithBaidu(Connection dbConn, long userid,
			double lat1, double lng1, double lat2, double lng2,
			String org_city, String dst_city) {
		double fResult = 0;

		String szUrl = "http://api.map.baidu.com/direction/v1?mode=driving&output=json";

		szUrl += "&origin=" + lat1 + "," + lng1 + "&destination=" + lat2 + ","
				+ lng2 + "&origin_region=" + org_city + "&destination_region="
				+ dst_city;

		szUrl += "&ak=" + ApiGlobal.getBaiduAKFromUserID(dbConn, userid);

		try {
			URL url = new URL(szUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty("Accept-Charset", "UTF-8");

			InputStream response = conn.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response, "UTF-8"));
			String szResult = "", szLine = "";
			while ((szLine = reader.readLine()) != null) {
				szResult += szLine;
			}

			JSONObject jsonObj = JSONObject.fromObject(szResult);

			int status = jsonObj.getInt("status");
			if (status == 0) {
				JSONObject result = jsonObj.getJSONObject("result");

				JSONArray routes = result.getJSONArray("routes");
				JSONObject first_route = routes.getJSONObject(0);

				fResult = (first_route.getInt("distance") / 100);
				fResult = fResult / 10.0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fResult = calcDist(lat1, lng1, lat2, lng2);
		}
		System.out.println("ApiGlobal.calcDistWithBaidu()==="+"fRessult===="+fResult);
		return fResult;
	}

	// Return distance between two coordinates in KM
	public static double calcDist(double lat1, double lon1, double lat2,
			double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;
System.out.println("ApiGlobal.calcDist()======dist"+(dist * 10) / 10.0);
		return (int) (dist * 10) / 10.0;
	}

	// Function to convert from degrees to radian
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// Function to convert from radian to degrees
	private static double rad2deg(double rad) {
		return (rad / Math.PI * 180.0);
	}

	// Function to convert from double to string with decimal part length
	// specified
	public static String Double2String(double d, int max_declen) {
		if (d == (int) d)
			return String.format("%d", (int) d);
		else
			return String.format("%." + max_declen + "f", d);
	}

	// Get difference of year between two date values
	public static long getDiffYear(Date dt_start, Date dt_end) {
		if (dt_start == null || dt_end == null)
			return 0;

		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		cal_start.setTime(dt_start);
		cal_end.setTime(dt_end);

		cal_start.set(Calendar.DAY_OF_MONTH, 0);
		cal_start.set(Calendar.HOUR_OF_DAY, 0);
		cal_start.set(Calendar.MINUTE, 0);
		cal_start.set(Calendar.SECOND, 0);

		cal_end.set(Calendar.DAY_OF_MONTH, 0);
		cal_end.set(Calendar.HOUR_OF_DAY, 0);
		cal_end.set(Calendar.MINUTE, 0);
		cal_end.set(Calendar.SECOND, 0);

		long nDays = 0;

		long diff = cal_end.getTimeInMillis() - cal_start.getTimeInMillis();
		nDays = diff / 1000 / 60 / 60 / 24;

		long nMonths = nDays / 30;

		return nMonths;
	}

	// Get difference of day between two date values
	public static long getDiffDay(Date dt_start, Date dt_end) {
		if (dt_start == null || dt_end == null)
			return 0;

		Calendar cal_start = Calendar.getInstance();
		Calendar cal_end = Calendar.getInstance();
		cal_start.setTime(dt_start);
		cal_end.setTime(dt_end);

		cal_start.set(Calendar.MINUTE, 0);
		cal_start.set(Calendar.SECOND, 0);

		cal_end.set(Calendar.MINUTE, 0);
		cal_end.set(Calendar.SECOND, 0);

		long nHours = 0;

		long diff = cal_end.getTimeInMillis() - cal_start.getTimeInMillis();
		nHours = diff / 1000 / 60 / 60;

		long nDay = nHours / 24;

		return nDay;
	}

	// Convert SQL.Time value to string
	public static String Time2String(Time timevalue) {
		if (timevalue == null)
			return "";

		return timevalue.toString();
	}

	// Convert String value to SQL.Time
	public static Time String2Time(String szValue) {
		Time time_value = null;

		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

		try {
			long timetick = formatter.parse(szValue).getTime();
			time_value = new Time(timetick);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return time_value;
	}

	// Get environment value from code
	public static String getEnvValueFromCode(Connection dbConn, String code) {
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		String szValue = "";

		try {
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM environment WHERE code=\"" + code
					+ "\" AND deleted = 0";

			rs = stmt.executeQuery(szQuery);

			if (rs.next())
				szValue = rs.getString("value");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return szValue;
	}

	// Return announcement with specified id value
	public static SVCAnnouncement getAnnouncementFromID(Connection dbConn,
			long announcement_id) {
		SVCAnnouncement result = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		// Create database entity
		try {
			stmt = dbConn.createStatement();
			szQuery = "SELECT * FROM announcement WHERE deleted = 0 AND id = "
					+ announcement_id + " AND validate >= \""
					+ ApiGlobal.Date2String(new Date(), true) + "\"";

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				result = SVCAnnouncement.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return result;
	}

	// Return latest application version info from code
	public static SVCAppVersion getLatestVersionOfApp(Connection dbConn,
			String appcode) {
		SVCAppVersion app_version = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM app_version WHERE deleted = 0 AND app_code = \""
					+ appcode + "\" ORDER BY upload_time DESC";
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				app_version = SVCAppVersion.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return app_version;
	}

	// Return evaluation info from id value
	public static SVCEvaluationCS getEvaluationInfoFromID(Connection dbConn,
			long id) {
		SVCEvaluationCS eval_info = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			// Get evaluation info
			szQuery = "SELECT * FROM evaluation_cs WHERE deleted=0 AND id="
					+ id;

			rs = stmt.executeQuery(szQuery);

			if (rs.next())
				eval_info = SVCEvaluationCS.decodeFromResultSet(rs);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return eval_info;
	}

	// Function to insert a user to group
	public static long insertNewuserToGroup(Connection dbConn, String source,
			String group_num, long userid) {
		if (userIsInGroup(dbConn, group_num, userid))
			return 0;

		long result = -1;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			String application = "";
			if (source.equals(ConstMgr.SRC_MAINAPP))
				application = ConstMgr.SRC_CHN_MAINAPP;
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				application = ConstMgr.SRC_CHN_PINCHEAPP;

			stmt = dbConn.createStatement();
			szQuery = "INSERT INTO group_details (groupid, userid, group_type, application, deleted) VALUES (\""
					+ group_num
					+ "\", "
					+ userid
					+ ", 0, \""
					+ application
					+ "\", 0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs != null && rs.next())
					result = rs.getLong(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return result;
	}

	// Function to discern if user belongs to group
	public static boolean userIsInGroup(Connection dbConn, String group_num,
			long userid) {
		boolean result = false;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();
			szQuery = "SELECT COUNT(*) AS count FROM group_details WHERE deleted=0 AND groupid=\""
					+ group_num + "\" AND userid=" + userid;

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				if (rs.getInt("count") == 1)
					result = true;
				else
					result = false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return result;
	}

	// Function to get notify_order information from id
	public static SVCNotifyOrder getNotifyOrderFromID(Connection dbConn,
			long notifyorder_id) {
		SVCNotifyOrder result = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		// Create database entity
		try {
			stmt = dbConn.createStatement();
			szQuery = "SELECT * FROM notify_order WHERE deleted = 0 AND id = "
					+ notifyorder_id;

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				result = SVCNotifyOrder.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return result;
	}

	// Function to get notify_person information from specified id value
	public static SVCNotifyPerson getNotifyPersonFromID(Connection dbConn,
			long notifyperson_id) {
		SVCNotifyPerson result = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		// Create database entity
		try {
			stmt = dbConn.createStatement();
			szQuery = "SELECT * FROM notify_person WHERE deleted = 0 AND id = "
					+ notifyperson_id;

			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				result = SVCNotifyPerson.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = null;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return result;
	}

	// Insert a ts log for the newly registered user
	public static long insertNewUserTs(Connection dbConn, String source,
			long userid, double balance) {
		long result = -1;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		// Create database entity
		try {

			String application = "";
			if (source.equals(ConstMgr.SRC_MAINAPP))
				application = ConstMgr.SRC_CHN_MAINAPP;
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				application = ConstMgr.SRC_CHN_PINCHEAPP;

			stmt = dbConn.createStatement();
			szQuery = "INSERT INTO ts (oper, ts_way, balance, ts_date, userid, groupid, unitid, remark, account, account_type, application, ts_type, sum, deleted"
					+ ") VALUES ("
					+ "2, -1, "
					+ balance
					+ ", \""
					+ ApiGlobal.Date2String(new Date(), true)
					+ "\", "
					+ userid
					+ ", 0, 0, \""
					+ ApiGlobal.getCommentFromTxCode(dbConn,
							ConstMgr.txcode_userRegister)
					+ "\", '', -1, \""
					+ application
					+ "\", \""
					+ ConstMgr.txcode_userRegister
					+ "\", " + balance + ", 0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs != null && rs.next())
					result = rs.getLong(1);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

		}

		return result;
	}

	// Get user info info from code
	public static SVCUser getUserInfoFromCode(Connection dbConn, String usercode) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND usercode LIKE \"%"
					+ usercode
					+ "%\" AND CHAR_LENGTH(usercode)="
					+ usercode.length();
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return userinfo;
	}

	// Get system id from user table
	public static long getSystemID(Connection dbConn) {
		long sysID = 0;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT id FROM user WHERE deleted = 0 AND is_platform=1";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				sysID = rs.getLong("id");
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return sysID;
	}

	// Authenticate user.
	public static SVCUser authUser(Connection dbConn, String usercode,
			String password) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND user = \""
					+ usercode + "\" AND password = \""
					+ ApiGlobal.MD5Hash(password) + "\"";
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return userinfo;
	}

	// Get user info info from phone number
	public static SVCUser getUserInfoFromMobile(Connection dbConn, String mobile) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND phone LIKE \"%"
					+ mobile + "%\" AND CHAR_LENGTH(phone)=" + mobile.length();
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return userinfo;
	}

	// Get user info info from real name
	public static SVCUser getUserInfoFromRealName(Connection dbConn,
			String realname) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND username LIKE \"%"
					+ realname
					+ "%\" AND CHAR_LENGTH(username)="
					+ realname.length();
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return userinfo;
	}

	// Get user info info from user id
	public static SVCUser getUserInfoFromUserID(Connection dbConn, long userid) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND id = " + userid;
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

		}

		return userinfo;
	}

	// Get user info info from user id
	public static SVCUser getUserInfoByUserID(Connection dbConn, long userid) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND id = " + userid;
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return userinfo;
	}

	// Get user info info from nick name
	public static SVCUser getUserInfoFromNickName(Connection dbConn,
			String nickname) {
		SVCUser userinfo = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user WHERE deleted = 0 AND nickname LIKE \"%"
					+ nickname
					+ "%\" AND CHAR_LENGTH(nickname)="
					+ nickname.length();
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				userinfo = SVCUser.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return userinfo;
	}

	// Get user login info info from device token
	public static SVCUserLogin getUserLoginFromDevToken(Connection dbConn,
			String devtoken) {
		SVCUserLogin user_login = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user_login WHERE deleted = 0 AND devtoken = \""
					+ devtoken + "\"";
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				user_login = SVCUserLogin.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return user_login;
	}

	// Get user car info from user id
	public static SVCUserCar getUserCarInfoFromUserID(Connection dbConn,
			long userid) {
		SVCUserCar user_car = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM user_car WHERE deleted = 0 AND userid = "
					+ userid;
			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				user_car = SVCUserCar.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return user_car;
	}

	// Function to discern if user is in black list.
	// Return value
	// 0 : no
	// 1 : yes
	// -1 : error
	public static int recordedInBlackList(Connection dbConn, long userid) {
		int nResult = 0;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			SVCAbbRecord abb_record = null;

			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * " + "FROM abb_record " + "WHERE "
					+ "		deleted = 0 AND " + "		userid = " + userid + " AND "
					+ "		status = 4"; // In black list condition

			rs = stmt.executeQuery(szQuery);

			while (rs.next()) {
				abb_record = SVCAbbRecord.decodeFromResultSet(rs);

				Date dtbegin = abb_record.limit_days_begin;
				int nDays = abb_record.limit_days;
				Date dtend = null;

				if (dtbegin == null) {
					continue;
				} else {
					Calendar cal_temp = Calendar.getInstance();
					cal_temp.setTime(dtbegin);
					cal_temp.add(Calendar.DAY_OF_YEAR, nDays);

					dtend = cal_temp.getTime();

					if (dtend.after(new Date())) // Still in black list
					{
						nResult = 1;
						break;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			nResult = -1;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return nResult;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get user balance
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static double getUserBalance(Connection dbConn, long userid) {
		double balance = 0;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT " + "		user.*," + "		ts.balance " + "FROM user "
					+ "INNER JOIN ts " + "ON ts.id=user.balance_ts " + "WHERE "
					+ "		user.deleted = 0 AND " + "		user.id = " + userid;

			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				balance = rs.getDouble("balance");
			} else {
				balance = -1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			balance = -1;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return balance;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get once order information
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static SVCOrderTempDetails getOnceOrderInfo(Connection dbConn,
			long orderid) {
		SVCOrderTempDetails order_info = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM order_temp_details WHERE id=" + orderid;

			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				order_info = SVCOrderTempDetails.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return order_info;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get order status if order is accepted by driver and the passenger is not
	// agreed yet
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static SVCOrderTempGrab getLatestGrabInfoFromOrderID(
			Connection dbConn, long orderid) {
		SVCOrderTempGrab grab_info = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM order_temp_grab WHERE deleted=0 AND order_id="
					+ orderid + " ORDER BY grab_time DESC LIMIT 0,1";

			rs = stmt.executeQuery(szQuery);

			if (rs.next()) // Exist a log. This order is already accepted.
			{
				grab_info = SVCOrderTempGrab.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return grab_info;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get order status if order is time expired or not
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean onceOrderIsExpiredOrNot(
			SVCOrderTempDetails order_details) {
		Date dt_exp = null;
		Calendar create_cal = Calendar.getInstance();
		create_cal.setTime(order_details.ps_date);
		create_cal.add(Calendar.SECOND, order_details.wait_time);
		dt_exp = create_cal.getTime();

		return dt_exp.before(new Date());
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get on off order information
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static SVCOrderOnOffDutyDetails getOnOffOrderInfo(Connection dbConn,
			long orderid) {
		SVCOrderOnOffDutyDetails order_info = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM order_onoffduty_details WHERE deleted=0 AND id="
					+ orderid;

			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				order_info = SVCOrderOnOffDutyDetails.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return order_info;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get order status if order is accepted by driver and the passenger is not
	// agreed yet
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static SVCOrderOnOffDutyGrab onoffOrderIsAcceptedByDriver(
			Connection dbConn, long orderid) {
		SVCOrderOnOffDutyGrab grab_info = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM order_onoffduty_grab WHERE status=0 AND deleted=0 AND order_id="
					+ orderid;

			rs = stmt.executeQuery(szQuery);

			if (rs.next()) // Exist a log. This order is already accepted.
			{
				grab_info = SVCOrderOnOffDutyGrab.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return grab_info;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Get long order information
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static SVCOrderLongDistanceDetails getLongOrderInfo(
			Connection dbConn, long orderid) {
		SVCOrderLongDistanceDetails order_info = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create statement
			stmt = dbConn.createStatement();

			szQuery = "SELECT * FROM order_longdistance_details WHERE deleted=0 AND id="
					+ orderid;

			rs = stmt.executeQuery(szQuery);

			if (rs.next()) {
				order_info = SVCOrderLongDistanceDetails
						.decodeFromResultSet(rs);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return order_info;
	}

	public static JSONObject getDistAndSystemAveragePrice(Connection dbConn,
			long userid, String city, String time, double start_lat,
			double start_lng, double end_lat, double end_lng, String midpoints,
			int carstyle) throws Exception {

		JSONObject result = new JSONObject();

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		SVCCity city_info = null;
		SVCGlobalParams global_params = null;

		double a1, a2, b1, b2, b4, c1, c2, d1, d2, e1, e2, e4, f1, f2, g1, g2, g3, g4, g5;
		Time t1 = null, t2 = null;

		stmt = dbConn.createStatement();

		// Global parameters
		szQuery = "SELECT * FROM global_params WHERE deleted = 0 LIMIT 0,1";
		rs = stmt.executeQuery(szQuery);
		rs.next(); // Global params always exist. If not exist, it is abnormal
		global_params = SVCGlobalParams.decodeFromResultSet(rs);
		rs.close();

		// City parameters
		city = ApiGlobal.removeCityCharacter(city);
		szQuery = "SELECT * FROM city WHERE deleted=0 AND name LIKE \"%" + city
				+ "%\"";
		rs = stmt.executeQuery(szQuery);
		if (rs.next())
			city_info = SVCCity.decodeFromResultSet(rs);
		rs.close();

		if (city_info == null || city_info.a1 < 0) // This means city parameters
													// are not set
		{
			a1 = global_params.a1;
			a2 = global_params.a2;
			b1 = global_params.b1;
			b2 = global_params.b2;
			b4 = global_params.b4;
			c1 = global_params.c1;
			c2 = global_params.c2;
			t1 = global_params.t1;
			t2 = global_params.t2;
			d1 = global_params.d1;
			d2 = global_params.d2;
			e1 = global_params.e1;
			e2 = global_params.e2;
			e4 = global_params.e4;
			f1 = global_params.f1;
			f2 = global_params.f2;
			g1 = global_params.g1;
			g2 = global_params.g2;
			g3 = global_params.g3;
			g4 = global_params.g4;
			g5 = global_params.g5;
		} else {
			a1 = city_info.a1;
			a2 = city_info.a2;
			b1 = city_info.b1;
			b2 = city_info.b2;
			b4 = city_info.b4;
			c1 = city_info.c1;
			c2 = city_info.c2;
			t1 = city_info.t1;
			t2 = city_info.t2;
			d1 = city_info.d1;
			d2 = city_info.d2;
			e1 = city_info.e1;
			e2 = city_info.e2;
			e4 = city_info.e4;
			f1 = city_info.f1;
			f2 = city_info.f2;
			g1 = city_info.g1;
			g2 = city_info.g2;
			g3 = city_info.g3;
			g4 = city_info.g4;
			g5 = city_info.g5;
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// 计算夜里的开始和结束时间
		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		Date dt_night_start = null, dt_night_end = null;
		Date pre_time = ApiGlobal.String2Date(time);
		Calendar pre_cal = Calendar.getInstance();
		Calendar cal_temp = Calendar.getInstance();
		int nHour = 0, nMinute = 0;
		pre_cal.setTime(pre_time);

		cal_temp.setTime(t1);
		nHour = cal_temp.get(Calendar.HOUR_OF_DAY);
		nMinute = cal_temp.get(Calendar.MINUTE);

		pre_cal.set(Calendar.HOUR_OF_DAY, nHour);
		pre_cal.set(Calendar.MINUTE, nMinute);

		dt_night_start = pre_cal.getTime();

		cal_temp.setTime(t2);
		nHour = cal_temp.get(Calendar.HOUR_OF_DAY);
		nMinute = cal_temp.get(Calendar.MINUTE);
		;

		pre_cal.set(Calendar.HOUR_OF_DAY, nHour);
		pre_cal.set(Calendar.MINUTE, nMinute);
		if (t2.before(t1)) {// 如果t2小于t1,则夜里结束时间是pre_cal时间的第二天了
			pre_cal.add(Calendar.DATE, 1);
		}

		dt_night_end = pre_cal.getTime();
		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////////////////////////

		boolean isNight = false; // Night or day
		isNight = pre_time.after(dt_night_start)
				&& pre_time.before(dt_night_end);
		// System.out.println("isNight:"+isNight+" dt_night_start:"+dt_night_start+" dt_night_end:"+dt_night_end);

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Get mid points coordinates and calculate distance
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		double dist = 0;
		double averPrice = 0;
		double final_lat = start_lat, final_lng = start_lng;

		String[] arrMidPoints = midpoints.split(",");
		int nLength = arrMidPoints.length / 3;

		for (int i = 0; i < nLength; i++) {
			String szLat = arrMidPoints[i * 3];
			String szLng = arrMidPoints[i * 3 + 1];

			double lat = 0;
			double lng = 0;

			try {
				lat = Double.parseDouble(szLat);
				lng = Double.parseDouble(szLng);
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}

			dist += ApiGlobal.calcDistWithBaidu(dbConn, userid, final_lat,
					final_lng, lat, lng, city, city);

			final_lat = lat;
			final_lng = lng;
		}

		dist += ApiGlobal.calcDistWithBaidu(dbConn, userid, final_lat,
				final_lng, end_lat, end_lng, city, city);
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (!isNight) // Daytime calculation
		{
			if (dist < a2)
				averPrice = a1;
			else if (dist < b4)
				averPrice = b1 + b2 * (dist - a2);
			else
				// (distance >= b4)
				averPrice = c1 + c2 * (dist - b4);
		} else {
			if (dist < d2)
				averPrice = d1;
			else if (dist < e4)
				averPrice = e1 + e2 * (dist - d2);
			else
				// (distance > e4)
				averPrice = f1 + f2 * (dist - e4);
		}

		if (carstyle == 1) // Economic car style result
			averPrice = averPrice * g2 / 100 * g1 / 100;
		else if (carstyle == 2) // Comfortable car style result
			averPrice = averPrice * g3 / 100 * g1 / 100;
		else if (carstyle == 3) // Luxury car style result
			averPrice = averPrice * g4 / 100 * g1 / 100;
		else if (carstyle == 4) // Business car style result
			averPrice = averPrice * g5 / 100 * g1 / 100;

		stmt.close();
		rs.close();

		result.put("price", averPrice);
		result.put("dist", dist);

		return result;
	}

	// Freeze user points and return ts ID.
	public static long freezePointsWithType(Connection dbConn, String source,
			long userid, long order_exec_id, long orderid, int order_type,
			double balance, String freeze_type) throws Exception {
		long nResult = -1;

		double curbalance = ApiGlobal.getUserBalance(dbConn, userid);
		if (curbalance < balance)
			return nResult;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			// Insert new log in ts table
			String szApp = "";
			int nApp = 1;
			if (source.equals(ConstMgr.SRC_MAINAPP)) {
				szApp = ConstMgr.SRC_CHN_MAINAPP;
				nApp = 1;
			} else if (source.equals(ConstMgr.SRC_PINCHEAPP)) {
				szApp = ConstMgr.SRC_CHN_PINCHEAPP;
				nApp = 2;
			} else {
				szApp = ConstMgr.STR_OTHERS;
				nApp = 3;
			}

			szQuery = "INSERT INTO ts (" + "order_cs_id," + "order_id,"
					+ "order_type," + "oper," + "ts_way," + "balance,"
					+ "ts_date," + "userid," + "groupid," + "unitid,"
					+ "remark," + "account," + "account_type," + "application,"
					+ "ts_type," + "sum," + "deleted" + ") VALUES ("
					+ order_exec_id + "," + orderid + "," + order_type
					+ ",1,0," + (curbalance - balance) + ",\""
					+ ApiGlobal.Date2String(new Date(), true) + "\"," + userid
					+ ",0,0,\""
					+ ApiGlobal.getCommentFromTxCode(dbConn, freeze_type)
					+ "\",'',1,\"" + szApp + "\",\"" + freeze_type + "\","
					+ balance + ",0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				ResultSet genKeys = stmt.getGeneratedKeys();
				if (genKeys.next()) {
					long new_ts_id = genKeys.getLong(1);
					genKeys.close();

					// Insert freeze points log
					szQuery = "INSERT INTO freeze_points (" + "userid,"
							+ "source," + "adminid," + "balance," + "state,"
							+ "freeze_ts_id," + "release_ts_id," + "deleted"
							+ ") VALUES (" + userid + "," + nApp + ",0,"
							+ balance + ",0," + new_ts_id + ",0,0)";

					if (stmt.executeUpdate(szQuery) == 1) {
						// Update user balance info
						szQuery = "UPDATE user SET balance_ts=" + new_ts_id
								+ " WHERE deleted=0 AND id=" + userid;

						if (stmt.executeUpdate(szQuery) == 1) {
							nResult = new_ts_id;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return nResult;
	}

	// Release user points and return ts ID.
	public static long releasePointsForUser(Connection dbConn, String source,
			long userid, long freezeid) throws Exception {
		long nResult = -1;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();
			// 1,获取用户最新余额
			double cur_balance = ApiGlobal.getUserBalance(dbConn, userid);
			SVCFreezePoints freeze_info = null;
			SVCTs freeze_ts_info = null;

			szQuery = "SELECT * FROM freeze_points WHERE deleted=0 AND state=0 AND id="
					+ freezeid;
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				freeze_info = SVCFreezePoints.decodeFromResultSet(rs);
			rs.close();

			if (freeze_info != null) {
				szQuery = "SELECT * FROM ts WHERE deleted=0 AND id="
						+ freeze_info.freeze_ts_id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					freeze_ts_info = SVCTs.decodeFromResultSet(rs);
				rs.close();
			}

			if (freeze_ts_info != null) {
				// Insert new release log in ts table
				String szApp = "";
				if (source.equals(ConstMgr.SRC_MAINAPP)) {
					szApp = ConstMgr.SRC_CHN_MAINAPP;
				} else if (source.equals(ConstMgr.SRC_PINCHEAPP)) {
					szApp = ConstMgr.SRC_CHN_PINCHEAPP;
				} else {
					szApp = ConstMgr.STR_OTHERS;
				}
				// 2,将解冻金额插入ts表
				szQuery = "INSERT INTO ts ("
						+ "order_cs_id,"
						+ "order_id,"
						+ "order_type,"
						+ "oper,"
						+ "ts_way,"
						+ "balance,"
						+ "ts_date,"
						+ "userid,"
						+ "groupid,"
						+ "unitid,"
						+ "remark,"
						+ "account,"
						+ "account_type,"
						+ "application,"
						+ "ts_type,"
						+ "sum,"
						+ "deleted"
						+ ") VALUES ("
						+ freeze_ts_info.order_cs_id
						+ ","
						+ freeze_ts_info.order_id
						+ ","
						+ freeze_ts_info.order_type
						+ ",2,4,"
						+ (cur_balance + freeze_ts_info.sum)
						+ ",\""
						+ ApiGlobal.Date2String(new Date(), true)
						+ "\","
						+ userid
						+ ",0,0,\""
						+ ApiGlobal.getCommentFromTxCode(dbConn,
								ConstMgr.txcode_userReleaseBalance)
						+ "\",'',1,\"" + szApp + "\",\""
						+ ConstMgr.txcode_userReleaseBalance + "\","
						+ freeze_ts_info.sum + ",0)";

				if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
					ResultSet genKeys = stmt.getGeneratedKeys();
					if (genKeys.next()) {
						long release_ts_id = genKeys.getLong(1);
						genKeys.close();
						// 3,将新插入ts表里的解冻金额更新到freeze_points表中
						// Update freeze points log
						szQuery = "UPDATE freeze_points SET state=1,release_ts_id="
								+ release_ts_id + " WHERE id=" + freeze_info.id;

						if (stmt.executeUpdate(szQuery) == 1) {
							// 4,更新用户的最新余额信息
							// Update user balance info
							szQuery = "UPDATE user SET balance_ts="
									+ release_ts_id
									+ " WHERE deleted=0 AND id=" + userid;
							if (stmt.executeUpdate(szQuery) == 1) {
								nResult = release_ts_id;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return nResult;
	}

	// Get ts information of user from order info
	public static SVCFreezePoints getLatestFreezeLogForOrder(Connection dbConn,
			long userid, long orderid, int ordertype, long order_exec_id)
			throws Exception {
		SVCFreezePoints freezePoints = null;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			SVCTs freeze_ts = null;

			stmt = dbConn.createStatement();
			// 1,获取冻结的ts记录
			if (order_exec_id != 0) {
				szQuery = "SELECT * " + "FROM ts " + "WHERE"
						+ "		deleted=0 AND " + "		userid=" + userid + " AND "
						+ "		oper=1 AND " + "		ts_type=\""
						+ ConstMgr.txcode_userFreezeBalance + "\" AND "
						+ "		order_id=" + orderid + " AND " + "		order_type="
						+ ordertype + " AND " + "		order_cs_id="
						+ order_exec_id;
			} else {
				szQuery = "SELECT * " + "FROM ts " + "WHERE"
						+ "		deleted=0 AND " + "		userid=" + userid + " AND "
						+ "		oper=1 AND " + "		ts_type=\""
						+ ConstMgr.txcode_userFreezeBalance + "\" AND "
						+ "		order_id=" + orderid + " AND " + "		order_type="
						+ ordertype + " AND " + "		order_cs_id=0";
			}

			szQuery += " ORDER BY ts_date DESC LIMIT 0,1";

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				freeze_ts = SVCTs.decodeFromResultSet(rs);
			rs.close();
			// 2,根据冻结的ts记录查询出freeze_points表中相关的记录
			if (freeze_ts != null) {
				szQuery = "SELECT * FROM freeze_points WHERE state=0 AND deleted=0 AND freeze_ts_id="
						+ freeze_ts.id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					freezePoints = SVCFreezePoints.decodeFromResultSet(rs);
				rs.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return freezePoints;
	}

	// Function to convert Calendar.DAY_OF_WEEK to custom day.
	// Custom day : Monday-0 ... Sunday-6
	public static int convertCalDay2SysDay(int nCalDay) {
		int nSysDay = 0;
		switch (nCalDay) {
		case Calendar.MONDAY:
			nSysDay = 0;
			break;
		case Calendar.TUESDAY:
			nSysDay = 1;
			break;
		case Calendar.WEDNESDAY:
			nSysDay = 2;
			break;
		case Calendar.THURSDAY:
			nSysDay = 3;
			break;
		case Calendar.FRIDAY:
			nSysDay = 4;
			break;
		case Calendar.SATURDAY:
			nSysDay = 5;
			break;
		case Calendar.SUNDAY:
			nSysDay = 6;
			break;
		}
		return nSysDay;
	}

	// Get next valid day and time from cur_pre_time.
	// If cur_pre_time is null, calculate from today
	// pre_time is only to reference time values, not day values
	public static Date nextValidDay(String[] valid_days, Date cur_pre_time,
			Date pre_time) {
		if (valid_days == null || valid_days.length == 0)
			return null;

		if (cur_pre_time == null) {
			cur_pre_time = new Date();
		}

		Calendar cal_temp = Calendar.getInstance();
		cal_temp.setTime(cur_pre_time);

		int nCurDay = convertCalDay2SysDay(cal_temp.get(Calendar.DAY_OF_WEEK));
		int nClosetDay = -1;

		for (int i = 0; i < valid_days.length; i++) {
			int nDay = 0;
			try {
				nDay = Integer.parseInt(valid_days[i]);

				if (nDay > nCurDay) {
					nClosetDay = nDay;
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}

			if (nClosetDay > nCurDay)
				break;
		}

		if (nClosetDay < 0) {
			try {
				nClosetDay = Integer.parseInt(valid_days[0]);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		int nDiff = nClosetDay - nCurDay;
		while (nDiff < 0) {
			nDiff += 7;
		}

		Calendar cal_pre = Calendar.getInstance();
		cal_pre.setTime(pre_time);

		cal_temp.add(Calendar.DAY_OF_MONTH, nDiff);
		cal_temp.set(Calendar.HOUR_OF_DAY, cal_pre.get(Calendar.HOUR_OF_DAY));
		cal_temp.set(Calendar.MINUTE, cal_pre.get(Calendar.MINUTE));
		cal_temp.set(Calendar.SECOND, cal_pre.get(Calendar.SECOND));

		return cal_temp.getTime();
	}

	public static int payToDriver_new(Connection dbConn, String source,
			double balance, double cpnPrice, long passid, long driverid,
			long order_exec_id) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";
		SVCOrderExecCS ordExeInf = null;
		SVCUser passinfo = ApiGlobal.getUserInfoFromUserID(dbConn, passid);
		SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn, driverid);
		long sys_id = ApiGlobal.getSystemID(dbConn);// 平台用户

		SVCUser psgInvUser = null, drvInvUser = null;// 乘客和车主邀请人(个人)
		SVCAppSpreadUnit psgInvUnit = null, drvInvUnit = null;// 乘客和车主邀请人(合作单位)
		SVCGroup drvInvGrp = null;// 车主邀请人(群组)

		double totalFee = 0; // Total price
		double platformFee = 0, workFee = 0; // platformFee是平台信息费，workFee是劳动报酬
		double passInviterFee = 0;// 乘客邀请人所得分成
		double drvInviterFee = 0;// 车主邀请人所得分成（个人或者合作单位）
		double drvFee = 0;// 车主所得

		try {
			stmt = dbConn.createStatement();
			// 0,先从乘客的账户里扣除当前订单的钱,然后把这些钱分给各个参与方
			SVCTs userTsInfo = null;
			szQuery = "SELECT * " + "FROM ts " + "WHERE " + "		deleted=0 AND "
					+ "		id=" + passinfo.balance_ts;

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				userTsInfo = SVCTs.decodeFromResultSet(rs);
			rs.close();
			szQuery = "INSERT INTO ts (" + "order_cs_id," + "order_id,"
					+ "order_type," + "oper," + "ts_way," + "balance,"
					+ "ts_date," + "userid," + "groupid," + "unitid,"
					+ "remark," + "account," + "account_type," + "application,"
					+ "ts_type," + "sum," + "deleted" + ") VALUES("
					+ order_exec_id + ",0,0,1," + "4,"
					+ (userTsInfo.balance - balance) + ",now()," + passid
					+ ",0,0,'乘客拼车付费',''," + "1,'拼车','tx_code_003',"
					+ (-balance) + "," + "0) ";
			long newTsID = 0;
			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs.next())
					newTsID = rs.getLong(1);
				rs.close();
			}
			if (newTsID > 0) {
				// Update user info
				szQuery = "UPDATE user SET balance_ts=" + newTsID
						+ " WHERE id=" + passid;
				stmt.executeUpdate(szQuery);
			}

			/*
			 * 1, 乘客付出totalFee。 输入：无 输出：无
			 */
			totalFee = balance;

			/*
			 * 2,计算平台所得(平台信息费)platformFee
			 * (2)公式:platformFee=totalFee*plRate或者=plFix (1)求plRate和plFix:
			 * 输入：order.city 输出：platformFee,workFee
			 */
			szQuery = "SELECT * FROM order_exec_cs WHERE id=" + order_exec_id;// Get

			// order

			// execute

			// information
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				ordExeInf = SVCOrderExecCS.decodeFromResultSet(rs);
			rs.close();

			SVCCity cityPrm = null;
			SVCGlobalParams gblPrm = null;
			int nActWay = -1;// 平台信息费计算方式:固定值或者百分比
			double fPrfRatio = 0, fPrfFix = 0;// fPrfRatio:平台信息费按百分比方式计算；fPrfFix平台信息费按固定金额方式计算
			
			szQuery = "SELECT * FROM city WHERE code LIKE \"%"
					+ ordExeInf.order_city + "%\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				cityPrm = SVCCity.decodeFromResultSet(rs);

				nActWay = cityPrm.active;
				fPrfRatio = cityPrm.ratio;
				fPrfFix = cityPrm.integer_;
			}
			rs.close();
			if (nActWay < 0) {// No city parameters or not set. Must use global
								// parameters
				szQuery = "SELECT * FROM global_params WHERE deleted=0";
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					gblPrm = SVCGlobalParams.decodeFromResultSet(rs);
					nActWay = gblPrm.active;
					fPrfRatio = gblPrm.ratio;
					fPrfFix = gblPrm.integer_;
					
				}
				rs.close();
			}
			// 计算平台信息费platformFee和劳动报酬workFee
			if (nActWay == 0) // Ratio calculation
				platformFee = totalFee * fPrfRatio / 100;
			else
				platformFee = Math.min(totalFee, fPrfFix);// Fixed value
			workFee = totalFee - platformFee;// Calculate work fee
			/*
			 * 3,获得乘客邀请人(个人)psgInvUser，乘客邀请人(合作单位)psgInvUnit,
			 * 计算乘客邀请人分成所得passInviterFee
			 * (2)公式:passInviterFee=platformFee*piRate或者=piFix(
			 * 注意乘客邀请人的返利是平台信息费的百分比而不是乘客付款总数的百分比，这个参考后台管理->客户管理部
			 * 
			 * 分的返利设置) (1)求piRate和piFix: (0)首先验证乘客向邀请人上供的期限或者次数是否已到，如果到了则不再上供
			 * 输入： 乘客信息:passinfo； 乘客注册用的邀请码：passinfo.invitecode_regist;
			 * 乘客给邀请人上供方式：passinfo.activeway_as_passenger;
			 * 乘客给邀请人上供比例:passinfo.ratio_as_passenger;
			 * 乘客给邀请人上供固定比例:passinfo.integer_as_passenger
			 * 乘客给邀请人上供种类：passinfo.provide_profitsharing_way
			 * 乘客给邀请人上供次数：passinfo.provide_profitsharing_count_as_passenger
			 * 乘客给邀请人上供期限：passinfo.provide_profitsharing_time_as_passenger
			 * 输出：psgInvUser,psgInvUnit,passInviterFee
			 */
			while (true) {
				int nLimitCount = passinfo.provide_profitsharing_count_as_passenger;// 乘客给邀请人上供的最长期限
				int nLimitMonth = passinfo.provide_profitsharing_time_as_passenger;// 乘客给邀请人上供的最大次数

				// Date condition
				Calendar cal_exp = null;// 3.1计算用户给邀请方贡献分成的时间限制。cal_exp是上供到期日期。
				// provide_profitsharing_way是用户给邀请方分成的方式，1按时间，2按次数，3都考虑(先到者)
				if (passinfo.provide_profitsharing_way == 1
						|| passinfo.provide_profitsharing_way == 3) {
					cal_exp = Calendar.getInstance();
					cal_exp.setTime(passinfo.reg_date);
					cal_exp.add(Calendar.MONTH, nLimitMonth);
				}

				// 3.2,获得乘客已经做了多少单
				// Count condition
				int nProfitCount = -1;
				if (passinfo.provide_profitsharing_way == 2
						|| passinfo.provide_profitsharing_way == 3) {// provide_profitsharing_way是用户给邀请方分成的方式，1按时间，2按次数，3都考虑(先到者)
					szQuery = "SELECT " + "		COUNT(*) AS profit_count "
							+ "FROM order_exec_cs " + "WHERE "
							+ "		deleted=0 AND " + "		passenger=" + passid
							+ " AND " + "		status=7"; // Paid state

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						nProfitCount = rs.getInt("profit_count");
					rs.close();
				}

				// 3.3,如果上供期限超期，或者上供次数已经够了，则跳出循环
				// Invite relation is not alive.
				// Break from main loop
				if ((cal_exp != null && cal_exp.after(new Date()))
						|| (nProfitCount >= 0 && nLimitCount <= nProfitCount))
					break;
				// 3.4如果邀请人是个人
				if (passinfo.inviter_type == 1) {
					szQuery = "SELECT * " + "FROM user " + "WHERE "
							+ "		deleted=0 AND " + "		invitecode_self=\""
							+ passinfo.invitecode_regist + "\"";// 根据乘客注册的邀请码，查询出邀请人

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						psgInvUser = SVCUser.decodeFromResultSet(rs);
					rs.close();
				} else if (passinfo.inviter_type == 3) {// 3.5如果邀请人是合作单位
					szQuery = "SELECT * " + "FROM app_spread_unit " + "WHERE "
							+ "		deleted=0 AND " + "		invite_code=\""
							+ passinfo.invitecode_regist + "\"";// 根据乘客注册的邀请码，查询出邀请人

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						psgInvUnit = SVCAppSpreadUnit.decodeFromResultSet(rs);
					rs.close();
				}
				// Information fee share
				// 3.5,计算向乘客的邀请人贡献多少分成，passInviterFee
				if (passinfo.activeway_as_passenger == 0) // Calculate with
															// ratio
					passInviterFee = platformFee * passinfo.ratio_as_passenger
							/ 100;
				else
					// Calculate with fixed value
					passInviterFee = Math.min(platformFee,
							passinfo.integer_as_passenger);
				break; // Must break from here
			}
			/*
			 * 4,获得车主邀请人(个人)drvInvUser，车主邀请人(合作单位)drvInvUnit,车主邀请人(群组)drvInvGrp,
			 * 计算车主邀请人分成所得drvInviterFee
			 * (2)公式:drvInviterFee=platformFee*diRate或者=
			 * diFix(注意车主邀请人的返利是平台信息费的百分比而不是乘客付款总数的百分比，这个参考后台管理->客户
			 * 
			 * 管理部分的返利设置) (1)求diRate和diFix: (0)首先验证车主向邀请人上供的期限或者次数是否已到，如果到了则不再上供
			 * 输入： 车主信息:drvinfo； 车主注册用的邀请码：drvinfo.invitecode_regist;
			 * 车主给邀请人上供方式：drvinfo.activeway_as_passenger;
			 * 车主给邀请人上供比例:drvinfo.ratio_as_passenger;
			 * 车主给邀请人上供固定比例:drvinfo.integer_as_passenger
			 * 车主给邀请人上供种类：drvinfo.provide_profitsharing_way
			 * 车主给邀请人上供次数：drvinfo.provide_profitsharing_count_as_passenger
			 * 车主给邀请人上供期限：drvinfo.provide_profitsharing_time_as_passenger
			 * 输出：drvInvUser,drvInvUnit,drvInvGrp,drvInviterFee
			 */
			while (true) {
				int nLimitCount = drvinfo.provide_profitsharing_count_as_passenger;// 车主给邀请人上供的最大次数
				int nLimitMonth = drvinfo.provide_profitsharing_time_as_passenger;// 车主给邀请人上供的最长期限

				// Date condition
				// 4.1，计算用户给邀请方贡献分成的时间限制。cal_exp是上供到期日期。
				Calendar cal_exp = Calendar.getInstance();
				cal_exp.setTime(drvinfo.reg_date);
				cal_exp.add(Calendar.MONTH, nLimitMonth);

				// 4.2,计算车主做了多少单了
				// Count condition
				int nProfitCount = 0;
				szQuery = "SELECT " + "		COUNT(*) AS profit_count "
						+ "FROM order_exec_cs " + "WHERE " + "		deleted=0 AND "
						+ "		driver=" + driverid + " AND " + "		status=7";

				// Paid
				// state

				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					nProfitCount = rs.getInt("profit_count");
				rs.close();

				// 4.3，车主上供的最大期限已经到了，或者已经达到最大次数了，跳出循环
				// Invite relation is not alive.
				// Break from main loop
				if (cal_exp.after(new Date()) || nLimitCount <= nProfitCount)
					break;

				if (drvinfo.inviter_type == 1) {// 4.4，如果邀请人是个人，根据邀请码查出邀请人的信息
					szQuery = "SELECT * " + "FROM user " + "WHERE "
							+ "		deleted=0 AND " + "		invitecode_self=\""
							+ drvinfo.invitecode_regist + "\"";

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						drvInvUser = SVCUser.decodeFromResultSet(rs);
					rs.close();
				} else if (drvinfo.inviter_type == 2) {// 4.5，如果邀请人是群组，根据邀请码查出邀请人的信息
					szQuery = "SELECT * " + "FROM group_ " + "WHERE "
							+ "		deleted=0 AND " + "		invitecode_self=\""
							+ drvinfo.invitecode_regist + "\"";

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						drvInvGrp = SVCGroup.decodeFromResultSet(rs);
					rs.close();
				} else if (drvinfo.inviter_type == 3) {// 4.6，如果邀请人是合作单位，根据邀请码查出邀请人的信息
					szQuery = "SELECT * " + "FROM app_spread_unit " + "WHERE "
							+ "		deleted=0 AND " + "		invite_code=\""
							+ drvinfo.invitecode_regist + "\"";

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						drvInvUnit = SVCAppSpreadUnit.decodeFromResultSet(rs);
					rs.close();
				}
				// 4.7,计算向车主的邀请人贡献多少分成，fDrvInvFee
				if (drvInvGrp == null) {// 如果邀请人不是群组
					if (drvinfo.activeway_as_passenger == 0) // Calculate with

						// ratio
						drvInviterFee = platformFee
								* drvinfo.ratio_as_passenger / 100;
					else
						// Calculate with fixed value
						drvInviterFee = Math.min(platformFee,
								drvinfo.integer_as_passenger);
				}

				break; // Must break from here
			}
			/*
			 * 5，workFee的分配：如果车主邀请人是群组，则workFee分给群组否则分给车主
			 */
			if (drvInvGrp != null) {// 5.1 如果群组不为空，则劳动报酬要给群组，否则要给车主
				// 5.1.1，将劳动报酬的一部分分给车主
				if (drvinfo.activeway_as_driver == 0) // Calculate with
														// ratio.
				{
					drvInviterFee = workFee * drvinfo.ratio_as_driver / 100;// 计算群组应该得到多少劳动报酬
				} else // Calculate with fixed value
				{
					drvInviterFee = Math
							.min(workFee, drvinfo.integer_as_driver);// 计算群组应该得到多少劳动报酬
				}
				// 5.1.2，劳动报酬中，群组拿剩下的是车主的
				drvFee = workFee - drvInviterFee;// 车主应该拿到多少报酬

			} else {// 5.2如果群组为空，则把劳动报酬给车主
				drvFee = workFee;
			}
			// 6,生成各个方的费用，分两种情况：车主是否属于群组
			if (drvInvGrp != null) {// 如果车主属于群组

				// (1)车主所得drvFee,
				ApiGlobal.addBalance(dbConn, source, driverid,
						ConstMgr.USERTYPE_USER, drvFee,
						ConstMgr.txcode_driverFee, order_exec_id, 0, 0);
				// (2)车主邀请人所得drvInviterFee,(群组邀请人)
				ApiGlobal.addBalance(dbConn, source, drvInvGrp.id,
						ConstMgr.USERTYPE_GROUP, drvInviterFee,
						ConstMgr.txcode_driverInvFee, order_exec_id, 0, 0);
				// (3)乘客邀请人所得passInviterFee，
				if (psgInvUser != null) {
					ApiGlobal.addBalance(dbConn, source, psgInvUser.id,
							ConstMgr.USERTYPE_USER, passInviterFee,
							ConstMgr.txcode_passengerInvFee, order_exec_id, 0,
							0);
				} else if (psgInvUnit != null) {
					ApiGlobal.addBalance(dbConn, source, psgInvUnit.id,
							ConstMgr.USERTYPE_UNIT, passInviterFee,
							ConstMgr.txcode_passengerInvFee, order_exec_id, 0,
							0);
				}
				// (4)平台信息费所得platformFee=platformFee-passInviterFee
				platformFee = platformFee - passInviterFee;// 平台信息费重新计算，只减去乘客邀请人的分成
				ApiGlobal.addBalance(dbConn, source, sys_id,
						ConstMgr.USERTYPE_USER, platformFee,
						ConstMgr.txcode_systemInfo, order_exec_id, 0, 0);
			} else {// 如果车主不属于群组

				// (1)车主所得drvFee,
				ApiGlobal.addBalance(dbConn, source, driverid,
						ConstMgr.USERTYPE_USER, drvFee,
						ConstMgr.txcode_driverFee, order_exec_id, 0, 0);
				// (2)车主邀请人所得drvInviterFee,(个人邀请人或者是合作单位邀请人)
				if (drvInvUser != null) {
					ApiGlobal.addBalance(dbConn, source, drvInvUser.id,
							ConstMgr.USERTYPE_USER, drvInviterFee,
							ConstMgr.txcode_driverInvFee, order_exec_id, 0, 0);
				} else if (drvInvUnit != null) {
					ApiGlobal.addBalance(dbConn, source, drvInvUnit.id,
							ConstMgr.USERTYPE_USER, drvInviterFee,
							ConstMgr.txcode_driverInvFee, order_exec_id, 0, 0);
				}
				// (3)乘客邀请人所得passInviterFee，
				if (psgInvUser != null) {
					ApiGlobal.addBalance(dbConn, source, psgInvUser.id,
							ConstMgr.USERTYPE_USER, passInviterFee,
							ConstMgr.txcode_passengerInvFee, order_exec_id, 0,
							0);
				} else if (psgInvUnit != null) {
					ApiGlobal.addBalance(dbConn, source, psgInvUnit.id,
							ConstMgr.USERTYPE_UNIT, passInviterFee,
							ConstMgr.txcode_passengerInvFee, order_exec_id, 0,
							0);
				}
				// (4)平台信息费所得platformFee=platformFee-passInviterFee-drvInviterFee
				platformFee = platformFee - passInviterFee - drvInviterFee;// 平台信息费重新计算，减去乘客邀请人和车主邀请人的分成
				ApiGlobal.addBalance(dbConn, source, sys_id,
						ConstMgr.USERTYPE_USER, platformFee,
						ConstMgr.txcode_systemInfo, order_exec_id, 0, 0);
			}
			
			// 从车主酬劳中减去每个执行订单所要缴纳的1元保险的费用（保险费*2） modify by chuzhiqiang
			// parameters
			szQuery = "SELECT * FROM global_params WHERE deleted=0";
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				gblPrm = SVCGlobalParams.decodeFromResultSet(rs);
			}
			if(rs!=null){
				rs.close();
			}
			ApiGlobal.addBalance(dbConn, source, driverid,
					ConstMgr.USERTYPE_USER, -gblPrm.insu_fee * 2,
					ConstMgr.txcode_insuFee, order_exec_id, 0, 0);
			
			// 把保险费加到平台总账中 modify by chuzhiqiang
			ApiGlobal.addBalance(dbConn, source, sys_id,
					ConstMgr.USERTYPE_USER, gblPrm.insu_fee * 2,
					ConstMgr.txcode_insuFee, order_exec_id, 0, 0);
			
			
			// 7,更新乘客邀请人，车主，车主邀请人，平台用户的最新余额
			long maxId = 0;
			// 更新乘客邀请人的最新余额
			if (psgInvUser != null) {// 乘客邀请人是用户
				szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
						+ "		deleted=0 AND " + "		userid=" + passinfo.id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					maxId = rs.getLong("id");
					szQuery = "UPDATE user SET balance_ts=" + maxId
							+ " WHERE id=" + passinfo.id;
					stmt.executeUpdate(szQuery);
				}

			} else if (psgInvUnit != null) {// 乘客邀请人是合作单位
				szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
						+ "		deleted=0 AND " + "		unit=" + psgInvUnit.id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					maxId = rs.getLong("id");
					szQuery = "UPDATE app_spread_unit SET balance_ts=" + maxId
							+ " WHERE id=" + psgInvUnit.id;
					stmt.executeUpdate(szQuery);
				}
			}
			if (rs != null)
				rs.close();
			// 更新车主的最新余额
			szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
					+ "		deleted=0 AND " + "		userid=" + drvinfo.id;
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				maxId = rs.getLong("id");
				szQuery = "UPDATE user SET balance_ts=" + maxId + " WHERE id="
						+ drvinfo.id;
				stmt.executeUpdate(szQuery);
			}
			if (rs != null)
				rs.close();
			// 更新车主邀请人的最新余额
			if (drvInvUser != null) {// 车主邀请人是用户
				szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
						+ "		deleted=0 AND " + "		userid=" + drvInvUser.id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					maxId = rs.getLong("id");
					szQuery = "UPDATE user SET balance_ts=" + maxId
							+ " WHERE id=" + drvInvUser.id;
					stmt.executeUpdate(szQuery);
				}

			} else if (drvInvGrp != null) {// 车主邀请人是群组
				szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
						+ "		deleted=0 AND " + "		groupid=" + drvInvGrp.id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					maxId = rs.getLong("id");
					szQuery = "UPDATE group_ SET balance_ts=" + maxId
							+ " WHERE id=" + drvInvGrp.id;
					stmt.executeUpdate(szQuery);
				}

			} else if (drvInvUnit != null) {// 车主邀请人是合作单位
				szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
						+ "		deleted=0 AND " + "		unitid=" + drvInvUnit.id;
				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					maxId = rs.getLong("id");
					szQuery = "UPDATE app_spread_unit SET balance_ts=" + maxId
							+ " WHERE id=" + drvInvUnit.id;
					stmt.executeUpdate(szQuery);
				}

			}
			if (rs != null)
				rs.close();
			// 更新平台用户的最新余额
			szQuery = "SELECT max(id) id " + "FROM ts " + "WHERE "
					+ "		deleted=0 AND " + "		userid=" + sys_id;
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				maxId = rs.getLong("id");
				szQuery = "UPDATE user SET balance_ts=" + maxId + " WHERE id="
						+ sys_id;
				stmt.executeUpdate(szQuery);
			}
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
		}
		return 0;
	}

	/**
	 * 该方法实际生成结算树 payToDriver方法没有严格按照当初的设计来存储分成树和向ts表里插入数据，这个函数以后需要改进
	 * 
	 * 1,获取乘客和车主的信息 2,获得指定执行订单信息 3.获取乘客给邀请人上供的方式
	 * 3.1计算用户给邀请方贡献分成的时间限制。cal_exp是上供到期日期。 3.2,获得乘客已经做了多少单
	 * 3.3,如果上供期限超期，或者上供次数已经够了，则跳出循环 3.4如果邀请人是个人 3.5如果邀请人是合作单位 4，获取车主给邀请人上供的方式
	 * 4.1，计算用户给邀请方贡献分成的时间限制。cal_exp是上供到期日期。 4.2,计算车主做了多少单了
	 * 4.3，车主上供的最大期限已经到了，或者已经达到最大次数了，跳出循环 4.4，如果邀请人是个人，根据邀请码查出邀请人的信息
	 * 4.5，如果邀请人是群组，根据邀请码查出邀请人的信息 4.6，如果邀请人是合作单位，根据邀请码查出邀请人的信息 5，生成分成树
	 * 5,获取平台信息费计算方式，百分比或者固定金额 6,计算平台信息费fInfFee和劳动报酬fWrkFee 6.1,将平台信息费插入到分成树表里
	 * 注意这里有个错误，数据库里profitsharing_tree里的node1和node2应该为bigint型，而不应该是字符型
	 * 这里还有个错误，没有往ts表里插入平台信息费 6.2，将劳动报酬插入到分成树表中，同时向ts表中加入群组的报酬
	 * 6.2.1将劳动报酬插入到分成树表中 6.2.2,在ts表中将劳动报酬给群组 6.2.3，将劳动报酬的一部分分给车主
	 * 6.2.4，劳动报酬中，群组拿剩下的是车主的 6.2.5,将车主的报酬分给车主(插入到分成树中，并且插入到ts表中)
	 * 6.2.6,从群组所拿到的金额当中，扣除车主的部分 6.3如果群组为空，则把劳动报酬给车主:插入生成树，并且在ts表里给车主加上fWrkFee
	 * 7,计算向乘客的邀请人贡献多少分成，fPsgInvFee 8,计算向车主的邀请人贡献多少分成，fDrvInvFee 9,计算平台信息费
	 * 9.1将平台信息费插入到分成树，并将平台信息费赋给平台用户sys_id,插入到ts表里
	 * 10,从平台信息费中扣除乘客邀请人的分成（插入到分成树，插入到ts表中） 11,从平台信息费中扣除车主邀请人的分成（插入到分成树，插入到ts表中）
	 * 12,乘客邀请人是个人，为邀请人贡献分成（插入分成树，插入ts表） 12.1乘客邀请人是合作单位，为邀请人贡献分成(插入分成树，插入ts表)
	 * 13,车主邀请人是个人，为邀请人贡献分成信息(插入分成树，插入ts表)
	 * 13.1，车主邀请人是合作单位，为邀请人贡献分成信息(插入分成树，插入ts表) 14，如果乘客用了点券，那么点券的钱需要系统来出，这里较难理解。
	 * 首先点券要真正使用，必须把他转换为真正的绿点，这里从sys_id中减去点券的金额就相当于把他转换成了真正的绿点(也就是sys_id出了这笔钱)
	 * 
	 * @param dbConn
	 * @param source
	 * @param balance
	 * @param cpnPrice
	 * @param passid
	 * @param driverid
	 * @param order_exec_id
	 * @return
	 */
	// Function to pay balance to driver and some related users
	// Parameter order_type : 1-Once 2-OnOff 3-Long
	// Return value : 0 - Success -1 - Failure
	public static int payToDriver(Connection dbConn, String source,
			double balance, double cpnPrice, long passid, long driverid,
			long order_exec_id) {
		int nResult = 0;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";
		SVCTs userTsInfo = null;
		try {
			stmt = dbConn.createStatement();
			// /////////////////////////////////////////////////////////////////////////////////////////////////
			// 0,先从乘客的账户里扣除当前订单的钱,然后把这些钱分给各个参与方
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn, passid);
			szQuery = "SELECT * " + "FROM ts " + "WHERE " + "		deleted=0 AND "
					+ "		id=" + userinfo.balance_ts;

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				userTsInfo = SVCTs.decodeFromResultSet(rs);
			rs.close();
			szQuery = "INSERT INTO ts (" + "order_cs_id," + "order_id,"
					+ "order_type," + "oper," + "ts_way," + "balance,"
					+ "ts_date," + "userid," + "groupid," + "unitid,"
					+ "remark," + "account," + "account_type," + "application,"
					+ "ts_type," + "sum," + "deleted" + ") VALUES("
					+ order_exec_id + ",0,0,1," + "4,"
					+ (-userTsInfo.balance - balance) + ",now()," + passid
					+ ",0,0,'乘客拼车付费',''," + "1,'拼车','tx_code_003',"
					+ (-balance) + "," + "0) ";
			long newTsID = 0;
			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs.next())
					newTsID = rs.getLong(1);
				rs.close();
			}
			if (newTsID > 0) {
				// Update user info
				szQuery = "UPDATE user SET balance_ts=" + newTsID
						+ " WHERE id=" + passid;
				stmt.executeUpdate(szQuery);
			}

			// 1,获取乘客和车主的信息
			SVCUser passinfo = ApiGlobal.getUserInfoFromUserID(dbConn, passid);
			SVCUser drvinfo = ApiGlobal.getUserInfoFromUserID(dbConn, driverid);

			SVCUser psgInvUser = null, drvInvUser = null;
			SVCAppSpreadUnit psgInvUnit = null, drvInvUnit = null;
			SVCGroup drvInvGrp = null;

			SVCOrderExecCS ordExeInf = null;
			// 2,获得指定执行订单信息
			// Get order execute information
			szQuery = "SELECT * FROM order_exec_cs WHERE id=" + order_exec_id;

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				ordExeInf = SVCOrderExecCS.decodeFromResultSet(rs);
			rs.close();
			// ///////////////////////////////////////////////////////////////////////////////////////////////////

			/*****************************************************************************************
			 * 3.获取乘客给邀请人上供的方式 Loop to get passenger invite info
			 ******************************************************************************************/
			while (true) {
				int nLimitCount = passinfo.provide_profitsharing_count_as_passenger;// 乘客给邀请人上供的最长期限
				int nLimitMonth = passinfo.provide_profitsharing_time_as_passenger;// 乘客给邀请人上供的最大次数

				// Date condition
				Calendar cal_exp = null;// 3.1计算用户给邀请方贡献分成的时间限制。cal_exp是上供到期日期。
				// provide_profitsharing_way是用户给邀请方分成的方式，1按时间，2按次数，3都考虑(先到者)
				if (passinfo.provide_profitsharing_way == 1
						|| passinfo.provide_profitsharing_way == 3) {
					cal_exp = Calendar.getInstance();
					cal_exp.setTime(passinfo.reg_date);
					cal_exp.add(Calendar.MONTH, nLimitMonth);
				}

				// 3.2,获得乘客已经做了多少单
				// Count condition
				int nProfitCount = -1;
				if (passinfo.provide_profitsharing_way == 2
						|| passinfo.provide_profitsharing_way == 3) {// provide_profitsharing_way是用户给邀请方分成的方式，1按时间，2按次数，3都考虑(先到者)
					szQuery = "SELECT " + "		COUNT(*) AS profit_count "
							+ "FROM order_exec_cs " + "WHERE "
							+ "		deleted=0 AND " + "		passenger=" + passid
							+ " AND " + "		status=7"; // Paid state

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						nProfitCount = rs.getInt("profit_count");
					rs.close();
				}

				// 3.3,如果上供期限超期，或者上供次数已经够了，则跳出循环
				// Invite relation is not alive.
				// Break from main loop
				if ((cal_exp != null && cal_exp.after(new Date()))
						|| (nProfitCount >= 0 && nLimitCount <= nProfitCount))
					break;
				// 3.4如果邀请人是个人
				if (passinfo.inviter_type == 1) {
					szQuery = "SELECT * " + "FROM user " + "WHERE "
							+ "		deleted=0 AND " + "		invitecode_self=\""
							+ passinfo.invitecode_regist + "\"";// 根据乘客注册的邀请码，查询出邀请人

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						psgInvUser = SVCUser.decodeFromResultSet(rs);
					rs.close();
				} else if (passinfo.inviter_type == 3) {// 3.5如果邀请人是合作单位
					szQuery = "SELECT * " + "FROM app_spread_unit " + "WHERE "
							+ "		deleted=0 AND " + "		invite_code=\""
							+ passinfo.invitecode_regist + "\"";// 根据乘客注册的邀请码，查询出邀请人

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						psgInvUnit = SVCAppSpreadUnit.decodeFromResultSet(rs);
					rs.close();
				}

				break; // Must break from here
			}

			/*****************************************************************************************
			 * Loop to get driver invite info 4，获取车主给邀请人上供的方式
			 ******************************************************************************************/
			while (true) {
				int nLimitCount = drvinfo.provide_profitsharing_count_as_passenger;// 车主给邀请人上供的最大次数
				int nLimitMonth = drvinfo.provide_profitsharing_time_as_passenger;// 车主给邀请人上供的最长期限

				// Date condition
				// 4.1，计算用户给邀请方贡献分成的时间限制。cal_exp是上供到期日期。
				Calendar cal_exp = Calendar.getInstance();
				cal_exp.setTime(drvinfo.reg_date);
				cal_exp.add(Calendar.MONTH, nLimitMonth);

				// 4.2,计算车主做了多少单了
				// Count condition
				int nProfitCount = 0;
				szQuery = "SELECT " + "		COUNT(*) AS profit_count "
						+ "FROM order_exec_cs " + "WHERE " + "		deleted=0 AND "
						+ "		driver=" + driverid + " AND " + "		status=7"; // Paid
				// state

				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					nProfitCount = rs.getInt("profit_count");
				rs.close();

				// 4.3，车主上供的最大期限已经到了，或者已经达到最大次数了，跳出循环
				// Invite relation is not alive.
				// Break from main loop
				if (cal_exp.after(new Date()) || nLimitCount <= nProfitCount)
					break;

				if (drvinfo.inviter_type == 1) {// 4.4，如果邀请人是个人，根据邀请码查出邀请人的信息
					szQuery = "SELECT * " + "FROM user " + "WHERE "
							+ "		deleted=0 AND " + "		invitecode_self=\""
							+ drvinfo.invitecode_regist + "\"";

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						drvInvUser = SVCUser.decodeFromResultSet(rs);
					rs.close();
				} else if (drvinfo.inviter_type == 2) {// 4.5，如果邀请人是群组，根据邀请码查出邀请人的信息
					szQuery = "SELECT * " + "FROM group_ " + "WHERE "
							+ "		deleted=0 AND " + "		invitecode_self=\""
							+ drvinfo.invitecode_regist + "\"";

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						drvInvGrp = SVCGroup.decodeFromResultSet(rs);
					rs.close();
				} else if (drvinfo.inviter_type == 3) {// 4.6，如果邀请人是合作单位，根据邀请码查出邀请人的信息
					szQuery = "SELECT * " + "FROM app_spread_unit " + "WHERE "
							+ "		deleted=0 AND " + "		invite_code=\""
							+ drvinfo.invitecode_regist + "\"";

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						drvInvUnit = SVCAppSpreadUnit.decodeFromResultSet(rs);
					rs.close();
				}

				break; // Must break from here
			}

			/****************************************************************
			 * Getting driver and passenger information is finished. Must decide
			 * profit sharing tree. This loop is for decide every possible
			 * profit values 5，生成分成树
			 ****************************************************************/

			double fTtlFee = balance; // Total price
			double fInfFee = 0, fWrkFee = 0; // Level 1 price
												// fInfFee是平台信息费，fWrkFee是劳动报酬

			while (true) {
				SVCCity cityPrm = null;
				SVCGlobalParams gblPrm = null;
				int nActWay = -1;// 平台信息费计算方式:固定值或者百分比
				double fPrfRatio = 0, fPrfFix = 0;// fPrfRatio:平台信息费按百分比方式计算；fPrfFix平台信息费按固定金额方式计算
				double insu_fee = 0;// 保险费 modify by chuzhiqiang
				// 5,获取平台信息费计算方式，百分比或者固定金额
				// Get city parameters
				szQuery = "SELECT * FROM city WHERE code LIKE \"%"
						+ ordExeInf.order_city + "%\"";

				rs = stmt.executeQuery(szQuery);
				if (rs.next()) {
					cityPrm = SVCCity.decodeFromResultSet(rs);

					nActWay = cityPrm.active;
					fPrfRatio = cityPrm.ratio;
					fPrfFix = cityPrm.integer_;

				}
				rs.close();
				// /////////////////////////////////////////////////////////////////////////////////////////

				if (nActWay < 0) {
					// No city parameters or not set. Must use global parameters
					szQuery = "SELECT * FROM global_params WHERE deleted=0";

					rs = stmt.executeQuery(szQuery);
					if (rs.next()) {
						gblPrm = SVCGlobalParams.decodeFromResultSet(rs);

						nActWay = gblPrm.active;
						fPrfRatio = gblPrm.ratio;
						fPrfFix = gblPrm.integer_;
						insu_fee = gblPrm.insu_fee;// 从全局获取保险费的数值，modify by
													// chuzhiqiang
					}
					rs.close();
				}
				// 6,计算平台信息费fInfFee和劳动报酬fWrkFee
				// Calculate information fee
				if (nActWay == 0) // Ratio calculation
					fInfFee = fTtlFee * fPrfRatio / 100;
				else
					// Fixed value
					fInfFee = Math.min(fTtlFee, fPrfFix);

				// Calculate work fee
				fWrkFee = fTtlFee - fInfFee;
				// 6.1,将平台信息费插入到分成树表里
				// 注意这里有个错误，数据库里profitsharing_tree里的node1和node2应该为bigint型，而不应该是字符型
				// 这里还有个错误，没有往ts表里插入平台信息费
				ApiGlobal.shareTreeLog(dbConn,
						order_exec_id,
						"1", // Branch no
						passid, 0, nActWay == 0 ? fPrfRatio : fPrfFix, nActWay,
						fInfFee, ConstMgr.USERTYPE_USER,
						ConstMgr.USERTYPE_OTHER);

				// 6.2，将劳动报酬插入到分成树表中，同时向ts表中加入群组的报酬
				// Work fee share
				if (drvInvGrp != null) {// 如果群组不为空，则劳动报酬要给群组，否则要给车主
					ApiGlobal.shareTreeLog(dbConn, order_exec_id,
							"2.1", // Branch no
							passid, drvInvGrp.id, -1, -1, fWrkFee,
							ConstMgr.USERTYPE_USER, ConstMgr.USERTYPE_GROUP);// 6.2.1将劳动报酬插入到分成树表中
					// 6.2.2,在ts表中将劳动报酬给群组
					ApiGlobal.addBalance(dbConn, source, drvInvGrp.id,
							ConstMgr.USERTYPE_GROUP, fWrkFee,
							ConstMgr.txcode_inviteFee, order_exec_id, 0, 0);

					// 6.2.3，将劳动报酬的一部分分给车主
					// Give some of the work fee to driver
					double fDrvInvFee = 0;// 群组应该得到多少劳动报酬
					if (drvinfo.activeway_as_driver == 0) // Calculate with
															// ratio.
					{
						fDrvInvFee = fWrkFee * drvinfo.ratio_as_driver / 100;// 计算群组应该得到多少劳动报酬
					} else // Calculate with fixed value
					{
						fDrvInvFee = Math.min(fWrkFee,
								drvinfo.integer_as_driver);// 计算群组应该得到多少劳动报酬
					}
					// 6.2.4，劳动报酬中，群组拿剩下的是车主的
					// Remaining value is for driver
					double fDrvFee = fWrkFee - fDrvInvFee;// 车主应该拿到多少报酬

					// 6.2.5,将车主的报酬分给车主(插入到分成树中，并且插入到ts表中)
					ApiGlobal.shareTreeLog(dbConn, order_exec_id,
							"2.1.1", // Branch no
							drvInvGrp.id, driverid, -1, -1, fDrvFee,
							ConstMgr.USERTYPE_GROUP, ConstMgr.USERTYPE_USER);

					// Add balance for driver
					ApiGlobal.addBalance(dbConn, source, driverid,
							ConstMgr.USERTYPE_USER, fDrvFee,
							ConstMgr.txcode_driverFee, order_exec_id, 0, 0);
					// 6.2.6,从群组所拿到的金额当中，扣除车主的部分
					// Decrease balance for group
					ApiGlobal.addBalance(dbConn, source, drvInvGrp.id,
							ConstMgr.USERTYPE_GROUP, -fDrvFee,
							ConstMgr.txcode_inviteFee, order_exec_id, 0, 0);

				} else {// 6.3如果群组为空，则把劳动报酬给车主:插入生成树，并且在ts表里给车主加上fWrkFee
					ApiGlobal.shareTreeLog(dbConn, order_exec_id,
							"2.2", // Branch no
							passid, driverid, -1, -1, fWrkFee,
							ConstMgr.USERTYPE_USER, ConstMgr.USERTYPE_USER);

					ApiGlobal.addBalance(dbConn, source, driverid,
							ConstMgr.USERTYPE_USER, fWrkFee,
							ConstMgr.txcode_driverFee, order_exec_id, 0, 0);
				}

				double fPsgInvFee = 0;// 向乘客邀请人上供多少分成
				double fDrvInvFee = 0;// 向车主邀请人上供多少分成
				double fSiteFee = 0;// 平台应该得到的信息费
				long sys_id = ApiGlobal.getSystemID(dbConn);// 平台用户

				// Information fee share
				// 7,计算向乘客的邀请人贡献多少分成，fPsgInvFee
				if (passinfo.activeway_as_passenger == 0) // Calculate with
															// ratio
					fPsgInvFee = fInfFee * passinfo.ratio_as_passenger / 100;
				else
					// Calculate with fixed value
					fPsgInvFee = Math.min(fInfFee,
							passinfo.integer_as_passenger);
				// 8,计算向车主的邀请人贡献多少分成，fDrvInvFee
				if (drvinfo.activeway_as_passenger == 0) // Calculate with ratio
					fDrvInvFee = fInfFee * drvinfo.ratio_as_passenger / 100;
				else
					// Calculate with fixed value
					fDrvInvFee = Math
							.min(fInfFee, drvinfo.integer_as_passenger);
				// 9,计算平台信息费
				// Calculate site fee
				fSiteFee = fInfFee;
				{
					// 9.1将平台信息费插入到分成树，并将平台信息费赋给平台用户sys_id,插入到ts表里
					// Record total site fee.
					ApiGlobal.shareTreeLog(dbConn, order_exec_id,
							"", // Branch no
							0, sys_id, 0, 0, fSiteFee, ConstMgr.USERTYPE_OTHER,
							ConstMgr.USERTYPE_USER);
					ApiGlobal.addBalance(dbConn, source, sys_id,
							ConstMgr.USERTYPE_USER, fSiteFee,
							ConstMgr.txcode_systemInfo, order_exec_id, 0, 0);
				}
				// 10,从平台信息费中扣除乘客邀请人的分成（插入到分成树，插入到ts表中）
				if (psgInvUser != null || psgInvUnit != null) {
					fSiteFee -= fPsgInvFee;

					// Record passenger invite fee minus
					ApiGlobal.shareTreeLog(dbConn, order_exec_id,
							"", // Branch no
							0, sys_id, 0, 0, -fPsgInvFee,
							ConstMgr.USERTYPE_OTHER, ConstMgr.USERTYPE_USER);

					ApiGlobal.addBalance(dbConn, source, sys_id,
							ConstMgr.USERTYPE_USER, -fPsgInvFee,
							ConstMgr.txcode_passengerInvFee, order_exec_id, 0,
							0);
				}
				// 11,从平台信息费中扣除车主邀请人的分成（插入到分成树，插入到ts表中）
				if (drvInvUser != null || drvInvUnit != null) {
					fSiteFee -= fDrvInvFee;

					// Record driver invite fee minus
					ApiGlobal.shareTreeLog(dbConn, order_exec_id,
							"", // Branch no
							0, sys_id, 0, 0, -fDrvInvFee,
							ConstMgr.USERTYPE_OTHER, ConstMgr.USERTYPE_USER);

					ApiGlobal.addBalance(dbConn, source, sys_id,
							ConstMgr.USERTYPE_USER, -fDrvInvFee,
							ConstMgr.txcode_driverInvFee, order_exec_id, 0, 0);
				}

				// 12,乘客邀请人是个人，为邀请人贡献分成（插入分成树，插入ts表）
				// User or spread unit which invited passenger
				if (psgInvUser != null) {
					ApiGlobal
							.shareTreeLog(
									dbConn,
									order_exec_id,
									"1.11", // Branch no
									0,
									psgInvUser.id,
									(passinfo.activeway_as_passenger == 0 ? passinfo.ratio_as_passenger
											: passinfo.integer_as_passenger),
									passinfo.activeway_as_passenger,
									fPsgInvFee, ConstMgr.USERTYPE_OTHER,
									ConstMgr.USERTYPE_USER);

					ApiGlobal.addBalance(dbConn, source, psgInvUser.id,
							ConstMgr.USERTYPE_USER, fPsgInvFee,
							ConstMgr.txcode_inviteFee, order_exec_id, 0, 0);
				} else if (psgInvUnit != null) {// 12.1乘客邀请人是合作单位，为邀请人贡献分成(插入分成树，插入ts表)
					ApiGlobal
							.shareTreeLog(
									dbConn,
									order_exec_id,
									"1.21", // Branch no
									0,
									psgInvUnit.id,
									(passinfo.activeway_as_passenger == 0 ? passinfo.ratio_as_passenger
											: passinfo.integer_as_passenger),
									passinfo.activeway_as_passenger,
									fPsgInvFee, ConstMgr.USERTYPE_OTHER,
									ConstMgr.USERTYPE_UNIT);

					ApiGlobal.addBalance(dbConn, source, psgInvUnit.id,
							ConstMgr.USERTYPE_UNIT, fPsgInvFee,
							ConstMgr.txcode_inviteFee, order_exec_id, 0, 0);
				}
				// 13,车主邀请人是个人，为邀请人贡献分成信息(插入分成树，插入ts表)
				// User or spread unit which invited passenger
				if (drvInvUser != null) {
					ApiGlobal
							.shareTreeLog(
									dbConn,
									order_exec_id,
									"1.21", // Branch no
									0,
									drvInvUser.id,
									(drvinfo.activeway_as_passenger == 0 ? drvinfo.ratio_as_passenger
											: drvinfo.integer_as_passenger),
									drvinfo.activeway_as_passenger, fDrvInvFee,
									ConstMgr.USERTYPE_OTHER,
									ConstMgr.USERTYPE_USER);

					ApiGlobal.addBalance(dbConn, source, drvInvUser.id,
							ConstMgr.USERTYPE_USER, fDrvInvFee,
							ConstMgr.txcode_inviteFee, order_exec_id, 0, 0);
				} else if (drvInvUnit != null) {// 13.1，车主邀请人是合作单位，为邀请人贡献分成信息(插入分成树，插入ts表)
					ApiGlobal
							.shareTreeLog(
									dbConn,
									order_exec_id,
									"1.22", // Branch no
									0,
									drvInvUnit.id,
									(drvinfo.activeway_as_passenger == 0 ? drvinfo.ratio_as_passenger
											: drvinfo.integer_as_passenger),
									drvinfo.activeway_as_passenger, fDrvInvFee,
									ConstMgr.USERTYPE_OTHER,
									ConstMgr.USERTYPE_UNIT);

					ApiGlobal.addBalance(dbConn, source, drvInvUnit.id,
							ConstMgr.USERTYPE_UNIT, fDrvInvFee,
							ConstMgr.txcode_inviteFee, order_exec_id, 0, 0);
				}

				// 14，如果乘客用了点券，那么点券的钱需要系统来出，这里较难理解。
				// 首先点券要真正使用，必须把他转换为真正的绿点，这里从sys_id中减去点券的金额就相当于把他转换成了真正的绿点(也就是sys_id出了这笔钱)
				/*
				 * 此段代码转移到了SVCOrderService的payNormalOrder方法的6.4步骤 double
				 * fCpnMinusPrice = 0; if (cpnPrice > balance) fCpnMinusPrice =
				 * balance; else fCpnMinusPrice = cpnPrice;
				 * 
				 * ApiGlobal.shareTreeLog(dbConn, order_exec_id, "", // Branch
				 * no 0, sys_id, 0, 0, -fCpnMinusPrice, ConstMgr.USERTYPE_OTHER,
				 * ConstMgr.USERTYPE_USER);
				 * 
				 * ApiGlobal.addBalance(dbConn, source, sys_id,
				 * ConstMgr.USERTYPE_USER, -fCpnMinusPrice,
				 * ConstMgr.txcode_couponPrice, order_exec_id, 0, 0);
				 */

				// 从车主酬劳中减去每个执行订单所要缴纳的1元保险的费用（保险费*2） modify by chuzhiqiang
				ApiGlobal.addBalance(dbConn, source, driverid,
						ConstMgr.USERTYPE_USER, -insu_fee * 2,
						ConstMgr.txcode_insuFee, order_exec_id, 0, 0);
				// 把保险费加到平台总账中 modify by chuzhiqiang
				ApiGlobal.addBalance(dbConn, source, sys_id,
						ConstMgr.USERTYPE_USER, insu_fee * 2,
						ConstMgr.txcode_insuFee, order_exec_id, 0, 0);

				break;

			}
		} catch (Exception ex) {
			ex.printStackTrace();

			nResult = -1;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return nResult;
	}

	// Function to record log in profitsharing_tree table
	public static int shareTreeLog(Connection dbConn, long order_exec_id,
			String branch_no, long node1, long node2, double share_value,
			int share_type, double value, int node1_type, int node2_type) {

		int nResult = -1;
		String szQuery = "";
		Statement stmt = null;

		try {
			stmt = dbConn.createStatement();

			szQuery = "INSERT INTO profitsharing_tree ("

			+ "order_cs_id," + "branch_no," + "node1," + "node2," + "ps_value,"
					+ "ps_type," + "value," + "node1_source," + "node2_source"

					+ ") VALUES ("

					+ order_exec_id + ",\"" + branch_no + "\"," + node1 + ","
					+ node2 + "," + share_value + "," + share_type + ","
					+ value + "," + node1_type + "," + node2_type + ")";

			if (stmt.executeUpdate(szQuery) == 1) {
				nResult = 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return nResult;
	}

	// Add balance to user's ts
	// user_type : 1-user 2-group 3-unit
	// order_type : 1-once 2-onoff 3-long
	public static long addBalance(Connection dbConn, String source,
			long userid, int user_type, double balance, String type,
			long ord_exec_id, long order_id, int order_type) {
		long nNewTsID = 0;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			SVCUser userinfo = null;
			SVCGroup grpinfo = null;
			SVCAppSpreadUnit unitinfo = null;

			if (user_type == ConstMgr.USERTYPE_USER) {
				userinfo = ApiGlobal.getUserInfoFromUserID(dbConn, userid);
			} else if (user_type == ConstMgr.USERTYPE_GROUP) {
				szQuery = "SELECT * FROM group_ WHERE id=" + userid;

				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					grpinfo = SVCGroup.decodeFromResultSet(rs);
				rs.close();
			} else if (user_type == ConstMgr.USERTYPE_UNIT) {
				szQuery = "SELECT * FROM app_spread_unit WHERE id=" + userid;

				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					unitinfo = SVCAppSpreadUnit.decodeFromResultSet(rs);
				rs.close();
			}

			// Get old ts info
			SVCTs ts_info = null;
			{
				long ts_id = 0;
				if (userinfo != null) {
					ts_id = userinfo.balance_ts;
				} else if (grpinfo != null) {
					ts_id = grpinfo.balance_ts;
				} else if (unitinfo != null) {
					ts_id = unitinfo.balance_ts;
				}

				szQuery = "SELECT * FROM ts WHERE id=" + ts_id;

				rs = stmt.executeQuery(szQuery);
				if (rs.next())
					ts_info = SVCTs.decodeFromResultSet(rs);
				rs.close();
			}

			String szApp = "";
			if (source.equals(ConstMgr.SRC_MAINAPP))
				szApp = ConstMgr.SRC_CHN_MAINAPP;
			else if (source.equals(ConstMgr.SRC_PINCHEAPP))
				szApp = ConstMgr.SRC_CHN_PINCHEAPP;
			else
				szApp = ConstMgr.STR_OTHERS;

			int ts_oper = ConstMgr.TSOPER_TYPE_OUT;
			if (balance > 0)
				ts_oper = ConstMgr.TSOPER_TYPE_IN;

			// Insert new ts log
			szQuery = "INSERT INTO ts ("

			+ "order_cs_id," + "order_id," + "order_type," + "oper,"
					+ "ts_way," + "balance," + "ts_date," + "userid,"
					+ "groupid," + "unitid," + "remark," + "account,"
					+ "account_type," + "application," + "ts_type," + "sum,"
					+ "deleted"

					+ ") VALUES ("

					+ ord_exec_id
					+ ","
					+ order_id
					+ ","
					+ order_type
					+ ","
					+ ts_oper
					+ ","
					+ "0,"
					+ (ts_info == null ? balance : (ts_info.balance + balance))
					+ ",\""
					+ ApiGlobal.Date2String(new Date(), true)
					+ "\","
					+ (user_type == 1 ? userid : 0)
					+ ","
					+ (user_type == 2 ? userid : 0)
					+ ","
					+ (user_type == 3 ? userid : 0)
					+ ",\""
					+ ApiGlobal.getCommentFromTxCode(dbConn, type)
					+ "\","
					+ "'',"
					+ user_type
					+ ",\""
					+ szApp
					+ "\",\""
					+ type
					+ "\"," + Math.abs(balance) + "," + "0)";

			if (stmt.executeUpdate(szQuery, Statement.RETURN_GENERATED_KEYS) == 1) {
				rs = stmt.getGeneratedKeys();
				if (rs.next())
					nNewTsID = rs.getLong(1);
				rs.close();

				if (nNewTsID > 0) {
					szQuery = "";

					if (userinfo != null) {
						szQuery = "UPDATE user SET balance_ts=" + nNewTsID
								+ " WHERE id=" + userinfo.id;
					} else if (grpinfo != null) {
						szQuery = "UPDATE group_ SET balance_ts=" + nNewTsID
								+ " WHERE id=" + grpinfo.id;
					} else if (unitinfo != null) {
						szQuery = "UPDATE app_spread_unit SET balance_ts="
								+ nNewTsID + " WHERE id=" + unitinfo.id;
					}

					if (!szQuery.equals("")) {
						if (stmt.executeUpdate(szQuery) != 1) {
							nNewTsID = 0; // Failed to update database
						}
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}
		}

		return nNewTsID;
	}

	public static JSONObject getExecuteOrderInfo(Connection dbConn, long userid) {
		JSONObject result = new JSONObject();

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			SVCOrderExecCS exec_info = null;

			stmt = dbConn.createStatement();

			szQuery = "SELECT "
					+ "		*,"
					+ "		TIMESTAMPDIFF(MINUTE, beginservice_time, CURRENT_TIMESTAMP()) AS total_time "
					+ "FROM " + "		order_exec_cs " + "WHERE "
					+ "		deleted=0 AND "
					+ "		(order_type=1 OR order_type=2 OR order_type=3) AND "
					+ "		(status=3 OR status=4 OR status=5) AND " + "		driver="
					+ userid + " " + "ORDER BY " + "		cr_date " + "LIMIT 0,1";

			int total_time = 0;
			rs = stmt.executeQuery(szQuery);
			if (rs.next()) {
				exec_info = SVCOrderExecCS.decodeFromResultSet(rs);
				total_time = rs.getInt("total_time");
			}
			rs.close();

			int ordertype = 0;
			if (exec_info != null) {
				long orderid = 0;
				if (exec_info.order_type == ConstMgr.EXEC_ORDERTYPE_ONCE
						|| exec_info.order_type == ConstMgr.EXEC_ORDERTYPE_ONCE_FROM_ONOFF) {
					ordertype = 1;

					szQuery = "SELECT id " + "FROM " + "		order_temp_details "
							+ "WHERE " + "		order_cs_id=" + exec_info.id;

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						orderid = rs.getLong("id");
					rs.close();
				} else {
					ordertype = 2;

					szQuery = "SELECT order_onoffduty_details.id "

							+ "FROM "
							+ "		order_onoffduty_exec_details "

							+ "INNER JOIN order_onoffduty_divide "
							+ "ON order_onoffduty_divide.id = order_onoffduty_exec_details.onoffduty_divide_id "

							+ "INNER JOIN order_onoffduty_details "
							+ "ON order_onoffduty_details.id = order_onoffduty_divide.orderdetails_id "

							+ "WHERE "
							+ "		order_onoffduty_exec_details.order_cs_id="
							+ exec_info.id;

					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						orderid = rs.getLong("id");
					rs.close();
				}

				result.put("orderid", orderid);
				result.put("ordertype", ordertype);
				result.put("time", total_time);
				result.put("distance", exec_info.total_distance);
			} else {
				result.put("orderid", 0);
				result.put("ordertype", ordertype);
				result.put("time", 0);
				result.put("distance", 0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

		}

		return result;
	}

	public static boolean sendSMS(String phoneNum, String szMsg) {
		boolean result = true;

		String username = "haotang";
		String password = "haotang123";
		String szUrl = "http://121.52.221.108/send/gsend.aspx?" + "name="
				+ username + "&pwd=" + password + "&dst=" + phoneNum + "&msg="
				+ szMsg + "&sequeid=12345";

		try {
			URL url = new URL(szUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestProperty("Accept-Charset", "UTF-8");

			conn.connect();
			// InputStream response = url.op
			//
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// response, "UTF-8"));
			//
			// String szResult = "", szLine = "";
			// while ((szLine = reader.readLine()) != null) {
			// szResult += szLine;
			// }
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		}

		return result;
	}

	public static Object ignoreNull(Object szValue) {
		if (szValue == null)
			return "";
		return szValue;
	}

	// Get user car type
	// Return user car type.
	// If user has not car information, this is abnormal. Return ShangWuXing(4).
	// 1:JingJiXing
	// 2:ShuShiXing
	// 3:HaoHuaXing
	// 4:ShangWuXing
	public static int getUserCarType(Connection dbConn, long userid) {
		int nType = 4;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		SVCUserCar user_car = null;

		try {
			stmt = dbConn.createStatement();

			szQuery = "SELECT * " + "FROM user_car " + "WHERE " + "		userid="
					+ userid + " AND " + "		deleted=0";

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				user_car = SVCUserCar.decodeFromResultSet(rs);
			rs.close();

			long cartype_id = user_car.car_type_id;
			szQuery = "SELECT type FROM car_type WHERE id=" + cartype_id;
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				nType = rs.getInt("type");
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

		}

		return nType;
	}

	public static String getCommentFromTxCode(Connection dbConn, String ts_code) {
		String szComment = "";

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			szQuery = "SELECT comment_mobile " + "FROM ts_type " + "WHERE "
					+ "		tx_code=\"" + ts_code + "\" AND " + "		deleted=0";

			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				szComment = rs.getString("comment_mobile");
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

		}

		return szComment;
	}

	public static boolean sendPushNotifToUser(Connection dbConn, long userid,
			STPushNotificationData data) {
		ArrayList<Long> arrUserIDs = new ArrayList<Long>();
		arrUserIDs.add(userid);

		return sendPushNotifToUser(dbConn, arrUserIDs, data);
	}

	public static boolean sendPushNotifToUser(Connection dbConn,
			ArrayList<Long> arrUserIDs, STPushNotificationData data) {
		boolean result = false;

		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		SVCUser userinfo = null;
		String pushnotif_token = "";

		String szIDs = "";
		ArrayList<String> arrPNotifIDs = new ArrayList<String>();
		ArrayList<String> arrChannelIDs = new ArrayList<String>();

		try {
			stmt = dbConn.createStatement();

			for (int i = 0; i < arrUserIDs.size(); i++) {
				if (!szIDs.equals(""))
					szIDs += ",";
				szIDs += arrUserIDs.get(i);
			}

			if (!szIDs.equals("")) {
				szQuery = "SELECT " + "		user.*,"
						+ "		user_login.pushnotif_token "

						+ "FROM user "

						+ "INNER JOIN user_login "
						+ "ON user.device_token=user_login.devtoken "

						+ "WHERE " + "		user.id IN (" + szIDs + ") AND "
						+ "		user.loggedout=0 AND "
						+ "		user_login.loggedout=0 AND "
						+ "		user_login.deleted=0";

				rs = stmt.executeQuery(szQuery);
				while (rs.next()) {
					userinfo = SVCUser.decodeFromResultSet(rs);
					pushnotif_token = rs.getString("pushnotif_token");
					// if (userinfo.phone_system == 1) // iOS phone
					// {
					// } else // Android phone
					{
						String[] arrItems = pushnotif_token.split(",");
						if (arrItems.length != 2)
							continue;

						arrPNotifIDs.add(arrItems[0]);
						arrChannelIDs.add(arrItems[1]);
					}
				}
				rs.close();

				// Send to android
				for (int i = 0; i < arrPNotifIDs.size(); i++) {
					sendPushNotificationToAndroid(arrPNotifIDs.get(i),
							arrChannelIDs.get(i), data);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
		} finally {
			// Close result set
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

			// Close normal statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception sql_ex) {
					sql_ex.printStackTrace();
				}
			}

		}

		return result;
	}

	public static boolean sendPushNotificationToAndroid(final String userid,
			final String channelid, final STPushNotificationData data) {
		boolean result = false;

		TimerTask task = new TimerTask() {
			public void run() {
				try {
					// Constant variables
					String apiKey = "RzKyPYoXGQwEq9BSWv3CaHPf";
					String secretKey = "2e6dPQgF1PSFAFLDFhPG5oZ7kTaO18X4";

					ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

					BaiduChannelClient channelClient = new BaiduChannelClient(
							pair);

					// 3. 若要了解交互细节，请注册YunLogHandler类
					channelClient.setChannelLogHandler(new YunLogHandler() {
						@Override
						public void onHandle(YunLogEvent event) {
							System.out.println(event.getMessage());
						}
					});

					PushUnicastMessageRequest request = new PushUnicastMessageRequest();

					request.setDeviceType(3); // device_type => 1:Web 2:PC
												// 3:android
												// 4:iOS 5:WinPhone
					request.setChannelId(Long.parseLong(channelid));
					request.setUserId(userid);

					request.setMessageType(1);
					request.setMessage(data.encodeToJSON().toString());
					PushUnicastMessageResponse response = channelClient
							.pushUnicastMessage(request);

					System.out.println("push amount : "
							+ response.getSuccessAmount());
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
				}
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, 0);

		return result;
	}

	public static boolean addOrderNotification(Connection dbConn, long userid,
			long orderid, String szMsg) {
		boolean bResult = false;

		Statement stmt = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			szQuery = "INSERT INTO notify_order (" + "notification_type,"
					+ "ps_date," + "order_cs_id," + "receiver," + "msg,"
					+ "title," + "has_read," + "deleted) VALUES (" + "5,'"
					+ Date2String(new Date(), true) + "'," + orderid + ","
					+ userid + ",'" + szMsg + "'," + "''," + "0,0)";

			if (stmt.executeUpdate(szQuery) == 1)
				bResult = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return bResult;
	}

	public static boolean addPersonNotification(Connection dbConn, long userid,
			long couponid, String szMsg) {
		boolean bResult = false;

		Statement stmt = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			szQuery = "INSERT INTO notify_person (" + "title," + "msg,"
					+ "ps_date," + "couponid," + "receiver" + ") VALUES ("
					+ "\"" + ConstMgr.STR_FAFANGDIANQUAN + "\"," + "\"" + szMsg
					+ "\"," + "\"" + ApiGlobal.Date2String(new Date(), true)
					+ "\"," + couponid + "," + userid + ")";

			if (stmt.executeUpdate(szQuery) == 1)
				bResult = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return bResult;
	}

	public static boolean logMessage(Connection dbConn, String operno,
			String tblNames, String contents, String remark) {
		boolean bResult = false;
		Statement stmt = null;
		String szQuery = "";

		try {
			stmt = dbConn.createStatement();

			szQuery = "INSERT INTO daemon_logs (" + "oper_no," + "table_names,"
					+ "oper_contents," + "remark," + "deleted" + ") VALUES ('"
					+ operno + "','" + tblNames + "','" + contents + "','"
					+ remark + "'," + "0)";

			if (stmt.executeUpdate(szQuery) == 1) {
				bResult = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return bResult;
	}

	public static String getBaiduAKFromUserID(Connection dbConn, long userid) {
		String szResult = "", szKeyName = "";
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";
		int nKeyCount = 10;

		try {
			stmt = dbConn.createStatement();

			szKeyName = "baiduak" + (userid % nKeyCount);

			szQuery = "SELECT value FROM environment WHERE code=\"" + szKeyName
					+ "\"";
			rs = stmt.executeQuery(szQuery);
			if (rs.next())
				szResult = rs.getString("value");
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		return szResult;
	}

	// Date to Chinese String conversion
	public static String Date2ChnStr(Date dtvalue) {
		String szResult = "";
		if (dtvalue == null)
			return "";

		DateFormat df = null;
		String szFormat = "yyyy" + ConstMgr.STR_NIAN + "MM" + ConstMgr.STR_YUE
				+ "dd" + ConstMgr.STR_RI + " HH" + ConstMgr.STR_SHI + "mm"
				+ ConstMgr.STR_FEN;
		df = new SimpleDateFormat(szFormat);
		szResult = df.format(dtvalue);

		return szResult;
	}

	// Date to Chinese time string conversion
	public static String Date2ChnTimeStr(Date dtvalue) {
		String szResult = "";
		if (dtvalue == null)
			return "";

		DateFormat df = null;
		String szFormat = "HH" + ConstMgr.STR_SHI + "mm" + ConstMgr.STR_FEN;
		df = new SimpleDateFormat(szFormat);
		szResult = df.format(dtvalue);

		return szResult;
	}

	public static long getAbsDiffMinute(Date dt_start, Date dt_end) {
		if (dt_start == null || dt_end == null)
			return 0;

		long nMinute = 0;

		long diff = Math.abs(dt_end.getTime() - dt_start.getTime());
		nMinute = diff / (60 * 1000);

		return nMinute;
	}
	
	
	public void buyInsurance(){
	}



	private static Logger g_logger = null;
	public static Logger getLogger() {
		if (g_logger == null)
		{
			PatternLayout layout = new PatternLayout();
			String conversionPattern = "[%p] %d %c %M - %m%n";
			layout.setConversionPattern(conversionPattern);

			// creates daily rolling file appender
			DailyRollingFileAppender rollingAppender = new DailyRollingFileAppender();
			rollingAppender.setFile("C:/logs/app.log");
			rollingAppender.setDatePattern("'.'yyyy-MM-dd");
			rollingAppender.setLayout(layout);
			rollingAppender.activateOptions();

			// creates a custom logger and log messages
			g_logger = Logger.getLogger(ApiGlobal.class);
			g_logger.addAppender(rollingAppender);
		}

		return g_logger;
	}


	public static void logMessage(String szMsg) {
		Logger logger = getLogger();
		logger.info(szMsg);
	}

	

	
	/**
	 * 根据用户生日计算年龄 
	 * @param birthday
	 * @return
	 */
    public static int getAgeByBirthday(Date birthday) {
    	Calendar cal = Calendar.getInstance();

    	if (cal.before(birthday)) {
    		throw new IllegalArgumentException(
    				"The birthDay is before Now.It's unbelievable!");
    	}

    	int yearNow = cal.get(Calendar.YEAR);
    	System.out.println("yearNow====="+yearNow);
    	int monthNow = cal.get(Calendar.MONTH) + 1;
    	System.out.println("monthNow====="+monthNow);
    	int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
    	System.out.println("dayOfMonthNow====="+dayOfMonthNow);

    	cal.setTime(birthday);
    	int yearBirth = cal.get(Calendar.YEAR);
    	System.out.println("yearBirth===="+yearBirth);
    	int monthBirth = cal.get(Calendar.MONTH)+1;
    	System.out.println("monthBirth===="+monthBirth);
    	int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
    	System.out.println("dayOfMonthBirth====="+dayOfMonthBirth);

    	int age = yearNow - yearBirth;

    	if (monthNow <= monthBirth) {
    		if (monthNow == monthBirth) {
    			// monthNow==monthBirth 
    			if (dayOfMonthNow < dayOfMonthBirth) {
    				age--;
    			}
    		} else {
    			// monthNow>monthBirth 
    			age--;
    		}
    	}
    	System.out.println("age====="+age);
    	return age;
    }
    
}
