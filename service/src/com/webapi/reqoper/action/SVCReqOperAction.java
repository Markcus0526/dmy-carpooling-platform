package com.webapi.reqoper.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.reqoper.service.SVCReqOperService;
import com.webapi.structure.SVCResult;

public class SVCReqOperAction
{
	// Input parameters
	private String source				= "";
	private long userid					= -1;
	private long limitid				= -1;
	private String devtoken				= "";
	private int pageno					= -1;
	private long req_id					= -1;

	// Output parameters
	private JSONObject result = new JSONObject();

	SVCReqOperService oper_service = new SVCReqOperService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String latestWithdrawLogs()
	{
		convertParamsToUTF8();

		SVCResult stResult = oper_service.latestWithdrawLogs(source, userid, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedWithdrawLogs()
	{
		convertParamsToUTF8();

		SVCResult stResult = oper_service.pagedWithdrawLogs(source, userid, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String cancelWithdraw()
	{
		convertParamsToUTF8();

		SVCResult stResult = oper_service.cancelWithdraw(source, userid, req_id, devtoken);

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


	public void setLimitid(long limitid) {
		this.limitid = limitid;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public void setPageno(int pageno) {
		this.pageno = pageno;
	}


	public JSONObject getResult() {
		return result;
	}


	public void setReq_id(long req_id) {
		this.req_id = req_id;
	}


}
