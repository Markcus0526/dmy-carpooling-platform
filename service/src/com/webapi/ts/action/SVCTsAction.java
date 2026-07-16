package com.webapi.ts.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.structure.SVCResult;
import com.webapi.ts.service.SVCTsService;

public class SVCTsAction
{
	// Input parameters
	private String source = "";
	private long userid = -1;
	private int pageno = -1;
	private String devtoken = "";
	private long limitid = -1;

	// Output parameters
	private JSONObject result = new JSONObject();


	SVCTsService ts_service = new SVCTsService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String pagedTsLogs()
	{
		convertParamsToUTF8();

		SVCResult stResult = ts_service.pagedTsLogs(source, userid, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String latestTsLogs()
	{
		convertParamsToUTF8();

		SVCResult stResult = ts_service.latestTsLogs(source, userid, limitid, devtoken);

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

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}

	public void setLimitid(long limitid) {
		this.limitid = limitid;
	}

	public JSONObject getResult() {
		return result;
	}

}
