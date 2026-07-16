package com.webapi.coupon.singlecoupon.action;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.coupon.singlecoupon.service.SVCSingleCouponService;
import com.webapi.structure.SVCResult;

import net.sf.json.JSONObject;

public class SVCSingleCouponAction
{
	// Input parameters
	private String source		= "";
	private long couponid		= -1;
	private long userid			= -1;
	private String devtoken		= "";
	private long limitid		= -1;
	private int pageno			= -1;
	private String active_code	= "";


	// Output parameter
	private JSONObject result = new JSONObject();


	SVCSingleCouponService singlecoupon_service = new SVCSingleCouponService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		active_code = ApiGlobal.fixEncoding(active_code);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}

	public String couponDetails()
	{
		convertParamsToUTF8();

		SVCResult stResult = singlecoupon_service.getCouponDetails(source, couponid, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String latestCoupon()
	{
		convertParamsToUTF8();

		SVCResult stResult = singlecoupon_service.latestCoupon(source, userid, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedCoupon()
	{
		convertParamsToUTF8();

		SVCResult stResult = singlecoupon_service.pagedCoupon(source, userid, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String addCoupon()
	{
		convertParamsToUTF8();

		SVCResult stResult = singlecoupon_service.addCoupon(source, userid, active_code, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public void setSource(String source) {
		this.source = source;
	}

	public void setCouponid(long couponid) {
		this.couponid = couponid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public void setLimitid(long limitid) {
		this.limitid = limitid;
	}



	public void setPageno(int pageno) {
		this.pageno = pageno;
	}


	public JSONObject getResult() {
		return result;
	}


	public void setActive_code(String active_code) {
		this.active_code = active_code;
	}

}
