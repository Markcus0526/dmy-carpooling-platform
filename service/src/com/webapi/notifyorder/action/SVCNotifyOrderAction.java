package com.webapi.notifyorder.action;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.notifyorder.service.SVCNotifyOrderService;
import com.webapi.structure.SVCResult;

import net.sf.json.JSONObject;

public class SVCNotifyOrderAction {
	// Input parameters
	private String source				= "";
	private long userid					= -1;
	private long limitid				= -1;
	private String devtoken				= "";
	private int pageno					= -1;
	private long ordernotifid			= -1;
	private String idarray				= "";


	// Result parameter
	private JSONObject result = new JSONObject();
	
	SVCNotifyOrderService notifyorder_service = new SVCNotifyOrderService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		idarray = ApiGlobal.fixEncoding(idarray);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String latestNotifyOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyorder_service.getLatestNotifyOrders(source, userid, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedNotifyOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyorder_service.getPagedNotifyOrders(source, userid, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String readOrderNotifs()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyorder_service.readOrderNotifs(source, userid, ordernotifid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String checkOrderNotifRead()
	{
		convertParamsToUTF8();

		SVCResult stResult = notifyorder_service.checkOrderNotifRead(source, userid, idarray, devtoken);

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


	public void setOrdernotifid(long ordernotifid) {
		this.ordernotifid = ordernotifid;
	}


	public void setIdarray(String idarray) {
		this.idarray = idarray;
	}
}
