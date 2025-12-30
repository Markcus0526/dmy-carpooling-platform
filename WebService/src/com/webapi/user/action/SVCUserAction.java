package com.webapi.user.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.structure.SVCResult;
import com.webapi.user.service.SVCUserService;

public class SVCUserAction
{
	// User concerned service parameters
	private String		source = "";
	private long		userid = -1;
	private String		username = "";
	private String		mobile = "";
	private String		nickname = "";
	private String		password = "";
	private String		invitecode = "";
	private int			sex = -1;
	private String		city = "";
	private String		devtoken = "";
	private int			platform = -1;
	private String		birthday = "";
	private String		photo = "";
	private int			photo_changed = -1;
	private String		carimg = "";
	private int			carimg_changed = -1;
	private String		oldpwd = "";
	private String		newpwd = "";
	private String		idforeimage = "";
	private String		idbackimage = "";
	private double		balance = -1;
	private int			charge_source = -1;
	private String		realname = "";
	private String		accountname = "";
	private String		bankcard = "";
	private String		bankname = "";
	private String		subbranch = "";
	private String		driver_licence_fore;
	private String		driver_licence_back;
	private String		brand = "";
	private String		type = "";
	private String		color = "";
	private String		driving_licence_fore= "";
	private String		driving_licence_back ="";
	private double		lat = 0;
	private double		lng = 0;
	private String		userphoto = "";
	private String		pushnotif_token = "";
	private String		extra = "";
	private double		total_amount = 0;
	private int			pay_result = 0;
	private int			registered = 0;
	private String		order_no = "";
	private String      channel_flag="";


	// Result parameter
	private JSONObject result = new JSONObject();

	// Service instance which processes all of the needed operations
	SVCUserService user_service = new SVCUserService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		username = ApiGlobal.fixEncoding(username);
		mobile = ApiGlobal.fixEncoding(mobile);
		nickname = ApiGlobal.fixEncoding(nickname);
		password = ApiGlobal.fixEncoding(password);
		invitecode = ApiGlobal.fixEncoding(invitecode);
		city = ApiGlobal.fixEncoding(city);
		devtoken = ApiGlobal.fixEncoding(devtoken);
		birthday = ApiGlobal.fixEncoding(birthday);
		oldpwd = ApiGlobal.fixEncoding(oldpwd);
		newpwd = ApiGlobal.fixEncoding(newpwd);
		realname = ApiGlobal.fixEncoding(realname);
		accountname = ApiGlobal.fixEncoding(accountname);
		bankcard = ApiGlobal.fixEncoding(bankcard);
		bankname = ApiGlobal.fixEncoding(bankname);
		subbranch = ApiGlobal.fixEncoding(subbranch);
		userphoto = ApiGlobal.fixEncoding(userphoto);
		pushnotif_token = ApiGlobal.fixEncoding(pushnotif_token);
	}



	// User register action
	public String registerUser()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.registerUser(source,
				username,
				mobile,
				nickname,
				password,
				invitecode,
				sex,
				city,
				devtoken,
				pushnotif_token,
				platform,
				channel_flag);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	// User login action
	public String loginUser()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.loginUser(source,
				username,
				password,
				city,
				devtoken,
				pushnotif_token,
				platform);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	// Verify code action
	public String verifyKey()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.getVerifyKey(source, mobile, username, registered);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	// Forget user password action
	public String forgetPassword()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.forgetPassword(source, username, mobile, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	// Return user information
	public String userInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.getUserInfo(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	// Change user info
	public String changeUserInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.changeUserInfo(source, userid, mobile, nickname, birthday, sex, photo, photo_changed, carimg, carimg_changed, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String changePassword()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.changePassword(source, userid, oldpwd, newpwd, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String verifyPersonInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.verifyPersonInfo(source, userid, idforeimage, idbackimage, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String verifyDriver()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.verifyDriver(source, userid, driver_licence_fore, driver_licence_back, brand, type, color, carimg, driving_licence_fore, driving_licence_back, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String charge()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.charge(source, userid, balance, devtoken, charge_source);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String accountInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.accountInfo(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String withdraw()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.withdraw(source, userid, realname, accountname, balance, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String bindBankCard()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.bindBankCard(source, userid, bankcard, bankname, subbranch, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String releaseBankCard()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.releaseBankCard(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String loginInfoFromDevtoken()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.loginInfoFromDevtoken(source, devtoken, pushnotif_token);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String latestBalance()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.latestBalance(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String nearbyDrivers()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.nearbyDrivers(source, userid, lat, lng, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String logoutUser()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.logoutUser(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String changeUserPhoto()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.changeUserPhoto(source, userid, userphoto, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String changeCarImage()
	{
		convertParamsToUTF8();

		SVCResult stResult = user_service.changeCarImage(source, userid, carimg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String chargeWithBaidu()
	{
		convertParamsToUTF8();

		if (pay_result == 1)			// Pay Success
		{
			SVCResult stResult = user_service.chargeWithBaidu(order_no, extra, total_amount / 100.0);

			if (stResult == null)
			{
				stResult = new SVCResult();

				stResult.retcode = ConstMgr.ErrCode_Exception;
				stResult.retmsg = ConstMgr.ErrMsg_Exception;
			}

			result = stResult.encodeToJSON();
		}

		return Action.SUCCESS;
	}





	public void setSource(String source) {
		this.source = source;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setInvitecode(String invitecode) {
		this.invitecode = invitecode;
	}


	public void setSex(int sex) {
		this.sex = sex;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public void setPlatform(int platform) {
		this.platform = platform;
	}


	public void setUserid(long userid) {
		this.userid = userid;
	}


	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}


	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public void setPhoto_changed(int photo_changed) {
		this.photo_changed = photo_changed;
	}


	public void setCarimg(String carimg) {
		this.carimg = carimg;
	}


	public void setCarimg_changed(int carimg_changed) {
		this.carimg_changed = carimg_changed;
	}


	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}


	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}


	public JSONObject getResult() {
		return result;
	}


	public void setIdforeimage(String idforeimage) {
		this.idforeimage = idforeimage;
	}


	public void setIdbackimage(String idbackimage) {
		this.idbackimage = idbackimage;
	}


	public void setBalance(double balance) {
		this.balance = balance;
	}


	public void setCharge_source(int charge_source) {
		this.charge_source = charge_source;
	}


	public void setRealname(String realname) {
		this.realname = realname;
	}


	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}


	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}


	public void setBankname(String bankname) {
		this.bankname = bankname;
	}


	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}


	public void setDriver_licence_fore(String driver_licence_fore) {
		this.driver_licence_fore = driver_licence_fore;
	}


	public void setDriver_licence_back(String driver_licence_back) {
		this.driver_licence_back = driver_licence_back;
	}




	public void setBrand(String brand) {
		this.brand = brand;
	}


	public void setType(String type) {
		this.type = type;
	}



	public void setColor(String color) {
		this.color = color;
	}



	public void setDriving_licence_fore(String driving_licence_fore) {
		this.driving_licence_fore = driving_licence_fore;
	}



	public void setDriving_licence_back(String driving_licence_back) {
		this.driving_licence_back = driving_licence_back;
	}


	public void setLat(double lat) {
		this.lat = lat;
	}


	public void setLng(double lng) {
		this.lng = lng;
	}


	public void setUserphoto(String userphoto) {
		this.userphoto = userphoto;
	}


	public void setPushnotif_token(String pushnotif_token) {
		this.pushnotif_token = pushnotif_token;
	}



	public void setExtra(String extra) {
		this.extra = extra;
	}



	public void setRegistered(int registered) {
		this.registered = registered;
	}



	public void setPay_result(int pay_result) {
		this.pay_result = pay_result;
	}



	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}



	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}



	public void setChannel_flag(String channel_flag) {
		this.channel_flag = channel_flag;
	}





}
