package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-6
 * Time: 上午9:37
 * To change this template use File | Settings | File Templates.
 */
public class STUserInfo
{
	public long userid = 0;
	public String photo_url = "";
	public String mobile = "";
	public String nickname = "";
	public int sex = 0;
	public String birthday = "";
	public int person_verified = 0;
	public int driver_verified = 0;
	public String carimg = "";
	public String invitecode = "";
	public String baiduak = "";

	public static STUserInfo decodeFromJSON(JSONObject jsonObj)
	{
		STUserInfo userinfo = new STUserInfo();

		try
		{
			userinfo.userid = jsonObj.getLong("userid");
			userinfo.photo_url = jsonObj.getString("photo");
			userinfo.mobile = jsonObj.getString("mobile");
			userinfo.nickname = jsonObj.getString("nickname");
			userinfo.sex = jsonObj.getInt("sex");
			userinfo.birthday = jsonObj.getString("birthday");
			userinfo.person_verified = jsonObj.getInt("person_verified");
			userinfo.driver_verified = jsonObj.getInt("driver_verified");
			userinfo.carimg = jsonObj.getString("carimg");
			userinfo.invitecode = jsonObj.getString("invitecode");
			userinfo.baiduak = jsonObj.getString("baiduak");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return userinfo;
	}

	public JSONObject encodeToJSON()
	{
		JSONObject obj = new JSONObject();

		try
		{
			obj.put("userid", userid);
			obj.put("photo", photo_url);
			obj.put("mobile", mobile);
			obj.put("nickname", nickname);
			obj.put("sex", sex);
			obj.put("birthday", birthday);
			obj.put("person_verified", person_verified);
			obj.put("driver_verified", driver_verified);
			obj.put("carimg", carimg);
			obj.put("invitecode", invitecode);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return obj;
	}
}

