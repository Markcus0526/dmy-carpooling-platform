package com.webapi.feedback.action;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.feedback.service.SVCFeedbackService;
import com.webapi.structure.SVCResult;

import net.sf.json.JSONObject;

public class SVCFeedbackAction
{
	// Input parameters
	private String source				= "";
	private long userid					= -1;
	private String contents				= "";
	private String devtoken				= "";

	// Return parameter
	private JSONObject result = new JSONObject();

	SVCFeedbackService fb_service = new SVCFeedbackService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		contents = ApiGlobal.fixEncoding(contents);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String advanceOpinion()
	{
		convertParamsToUTF8();

		SVCResult stResult = fb_service.advanceOpinion(source, userid, contents, devtoken);

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


	public void setContents(String contents) {
		this.contents = contents;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public JSONObject getResult() {
		return result;
	}
}
