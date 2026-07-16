package com.webapi.routes.action;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.routes.service.SVCRoutesService;
import com.webapi.structure.SVCResult;

import net.sf.json.JSONObject;

public class SVCRoutesAction
{
	// Input parameters
	private String source					= "";
	private long userid						= -1;
	private int type						= -1;
	private int daytype						= -1;
	private String startcity				= "";
	private String endcity					= "";
	private String startaddr				= "";
	private String endaddr					= "";
	private double startlat					= 0;
	private double startlng					= 0;
	private double endlat					= 0;
	private double endlng					= 0;
	private String city						= "";
	private String start_time				= "";
	private String devtoken					= "";
	private long route_id					= -1;


	// Output parameters
	private JSONObject result = new JSONObject();


	SVCRoutesService routes_service = new SVCRoutesService();


	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		startaddr = ApiGlobal.fixEncoding(startaddr);
		endaddr = ApiGlobal.fixEncoding(endaddr);
		startcity = ApiGlobal.fixEncoding(startcity);
		endcity = ApiGlobal.fixEncoding(endcity);
		city = ApiGlobal.fixEncoding(city);
		start_time = ApiGlobal.fixEncoding(start_time);
		devtoken = ApiGlobal.fixEncoding(devtoken);
	}


	public String addRoute()
	{
		convertParamsToUTF8();

		SVCResult stResult = routes_service.addRoute(source,
				userid,
				type,
				daytype,
				startcity,
				endcity,
				startaddr,
				endaddr,
				startlat,
				startlng,
				endlat,
				endlng,
				city,
				start_time,
				devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}




	public String routesList()
	{
		convertParamsToUTF8();

		SVCResult stResult = routes_service.getRoutes(source, userid, type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String removeRoute()
	{
		convertParamsToUTF8();

		SVCResult stResult = routes_service.removeRoute(source, userid, route_id, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String changeRoute()
	{
		convertParamsToUTF8();


		SVCResult stResult = routes_service.changeRoute(source,
				userid,
				route_id,
				type,
				daytype,
				startcity,
				endcity,
				startaddr,
				endaddr,
				startlat,
				startlng,
				endlat,
				endlng,
				city,
				start_time,
				devtoken);

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


	public void setType(int type) {
		this.type = type;
	}


	public void setDaytype(int daytype) {
		this.daytype = daytype;
	}


	public void setStartaddr(String startaddr) {
		this.startaddr = startaddr;
	}


	public void setEndaddr(String endaddr) {
		this.endaddr = endaddr;
	}


	public void setStartlat(double startlat) {
		this.startlat = startlat;
	}

	public void setStartlng(double startlng) {
		this.startlng = startlng;
	}


	public void setEndlat(double endlat) {
		this.endlat = endlat;
	}


	public void setEndlng(double endlng) {
		this.endlng = endlng;
	}



	public void setCity(String city) {
		this.city = city;
	}


	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public void setRoute_id(long route_id) {
		this.route_id = route_id;
	}

	public JSONObject getResult() {
		return result;
	}


	public void setStartcity(String startcity) {
		this.startcity = startcity;
	}


	public void setEndcity(String endcity) {
		this.endcity = endcity;
	}

}
