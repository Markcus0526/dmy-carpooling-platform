package com.webapi.notifyperson.action;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.notifyperson.service.SVCNotifyPersonService;
import com.webapi.structure.SVCResult;

import net.sf.json.JSONObject;

public class SVCNotifyPersonAction {
	// Input parameters
	private String source					= "";
	private long userid						= 0;
	private long limitid					= 0;
	private String devtoken					= "";
	private int pageno						= -1;
	private long personnotifid				= -1;
	private String idarray					= "";

	// Output parameters
	private JSONObject result = new JSONObject();


	// Service variable
	private SVCNotifyPersonService notifyperson_service = new SVCNotifyPersonService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		devtoken = ApiGlobal.fixEncoding(devtoken);
		idarray = ApiGlobal.fixEncoding(idarray);
	}


	public String latestNotifications()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyperson_service.getLatestNotifications(source, userid, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedNotifications()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyperson_service.getPagedNotifications(source, userid, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String readPersonNotifs()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyperson_service.readPersonNotifs(source, userid, personnotifid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String checkPersonNotifRead()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyperson_service.checkPersonNotifRead(source, userid, idarray, devtoken);

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


	public void setPersonnotifid(long personnotifid) {
		this.personnotifid = personnotifid;
	}


	public void setIdarray(String idarray) {
		this.idarray = idarray;
	}

}
