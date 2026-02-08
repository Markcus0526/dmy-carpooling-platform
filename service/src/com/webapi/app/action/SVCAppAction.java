package com.webapi.app.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.app.service.SVCAppService;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.structure.SVCResult;

public class SVCAppAction
{
	// Method parameters
	private String source				= "";
	private int platform				= -1;
	private long userid					= -1;
	private String packname				= "";
	private String version				= "";

	// Result parameter
	private JSONObject result = new JSONObject();

	SVCAppService app_service = new SVCAppService();

	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		packname = ApiGlobal.fixEncoding(packname);
		version = ApiGlobal.fixEncoding(version);
	}


	// Get child app list
	public String childAppList()
	{
		convertParamsToUTF8();

		SVCResult stResult = app_service.getChildAppList(source, platform);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	// Record download method
	public String recordDownload()
	{
		convertParamsToUTF8();

		SVCResult stResult = app_service.recordDownload(source, userid, packname, platform, version);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String latestAppVersion()
	{
		convertParamsToUTF8();

		SVCResult stResult = app_service.latestAppVersion(source, packname);

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


	public void setPlatform(int platform) {
		this.platform = platform;
	}


	public JSONObject getResult() {
		return result;
	}


	public void setPackname(String packname) {
		this.packname = packname;
	}


	public void setUserid(long userid) {
		this.userid = userid;
	}


	public void setVersion(String version) {
		this.version = version;
	}


}
