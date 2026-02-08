package com.webapi.evaluation.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.evaluation.service.SVCEvalService;
import com.webapi.structure.SVCResult;


public class SVCEvalAction
{
	private String source = "";
	private long userid = -1;
	private long passid = -1;
	private long driverid = -1;
	private long limitid = -1;
	private int pageno = -1;
	private String devtoken = "";


	// Output parameters
	private JSONObject result = new JSONObject();


	private SVCEvalService eval_service = new SVCEvalService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}



	public String passengerLatestEvalInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = eval_service.passengerLatestEvalInfo(source, userid, passid, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String passengerPagedEvalInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = eval_service.passengerPagedEvalInfo(source, userid, passid, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String driverLatestEvalInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = eval_service.driverLatestEvalInfo(source, userid, driverid, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}




	public String driverPagedEvalInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = eval_service.driverPagedEvalInfo(source, userid, driverid, pageno, devtoken);

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


	public void setUserid(long userid) {
		this.userid = userid;
	}


	public void setPassid(long passid) {
		this.passid = passid;
	}


	public void setDriverid(long driverid) {
		this.driverid = driverid;
	}


	public void setLimitid(long limitid) {
		this.limitid = limitid;
	}


	public void setPageno(int pageno) {
		this.pageno = pageno;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public JSONObject getResult() {
		return result;
	}

	
}
