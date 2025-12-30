package com.webapi.announcement.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.announcement.service.SVCAnnouncementService;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.structure.SVCResult;

public class SVCAnnouncementAction
{
	private String source						= "";
	private long userid							= -1;
	private String city							= "";
	private int driververif						= -1;
	private long limitid						= -1;
	private String devtoken						= "";
	private int pageno							= -1;
	private String annids						= "";


	// Result parameter
	private JSONObject result = new JSONObject();


	SVCAnnouncementService anc_service = new SVCAnnouncementService();

	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		city = ApiGlobal.fixEncoding(city);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	// Get latest announcements list
	public String latestAnnouncements()
	{
		convertParamsToUTF8();

		SVCResult stResult = anc_service.getLatestAnnouncements(source, userid, city, driververif, limitid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	// Get paged announcements list
	public String pagedAnnouncements()
	{
		convertParamsToUTF8();

		SVCResult stResult = anc_service.getPagedAnnouncements(source, userid, city, driververif, pageno, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String hasNews()
	{
		convertParamsToUTF8();

		SVCResult stResult = anc_service.hasNews(source, userid, city, driververif, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String readAnnouncement()
	{
		convertParamsToUTF8();

		SVCResult stResult = anc_service.readAnnouncement(source, userid, annids, devtoken);

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


	public void setCity(String city) {
		this.city = city;
	}


	public void setDriververif(int driververif) {
		this.driververif = driververif;
	}


	public void setLimitid(long limitid) {
		this.limitid = limitid;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}

	public JSONObject getResult() {
		return result;
	}



	public void setPageno(int pageno) {
		this.pageno = pageno;
	}


	public void setAnnids(String annids) {
		this.annids = annids;
	}
}
