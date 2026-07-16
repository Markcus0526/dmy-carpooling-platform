package com.webapi.order.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.order.service.SVCOrderService;
import com.webapi.structure.SVCResult;

public class SVCOrderAction
{
	// Input parameters
	private String source = "";
	private long userid = -1;
	private int order_type = -1;
	private long orderid = -1;
	private String order_num = "";
	private String limit_time = "";
	private int limitid_type = -1;
	private long limitid = -1;
	private int pageno = -1;
	private String devtoken = "";
	private String start_addr = "";
	private String end_addr = "";
	private long passid = -1;
	private long driverid = -1;
	private double start_lat = 0;
	private double start_lng = 0;
	private double end_lat = 0;
	private double end_lng = 0;
	private String start_time = "";
	private String midpoints = "";
	private String remark = "";
	private int reqstyle = -1;
	private double price = -1;
	private String days = "";
	private String city = "";
	private String start_city = "";
	private String end_city = "";
	private int seats_count = 0;
	private double lat = 0;
	private double lng = 0;
	private int wait_time = -1;
	private String password = "";
	private String msg = "";
	private int level = -1;
	private double new_price = -1;
	private String coupons = "";
	private double distance = -1; 
	private int pub_index = -1; 


	// Output parameters
	private JSONObject result = new JSONObject();


	private SVCOrderService order_service = new SVCOrderService();

	private void convertParamsToUTF8()
	{
		source = ApiGlobal.fixEncoding(source);
		order_num = ApiGlobal.fixEncoding(order_num);
		limit_time = ApiGlobal.fixEncoding(limit_time);
		start_addr = ApiGlobal.fixEncoding(start_addr);
		end_addr = ApiGlobal.fixEncoding(end_addr);
		start_time = ApiGlobal.fixEncoding(start_time);
		midpoints = ApiGlobal.fixEncoding(midpoints);
		remark = ApiGlobal.fixEncoding(remark);
		days = ApiGlobal.fixEncoding(days);
		city = ApiGlobal.fixEncoding(city);
		start_city = ApiGlobal.fixEncoding(start_city);
		end_city = ApiGlobal.fixEncoding(end_city);
		devtoken = ApiGlobal.fixEncoding(devtoken);
		msg = ApiGlobal.fixEncoding(msg);
	}


	public String latestDriverOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.latestDriverOrders(source, userid, order_type, order_num, limitid_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String pagedDriverOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.pagedDriverOrders(source, userid, order_type, limit_time, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String latestPassengerOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.latestPassengerOrders(source, userid, order_type, order_num, limitid_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedPassengerOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.pagedPassengerOrders(source, userid, order_type, limit_time, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String latestAcceptableOnceOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.latestAcceptableOnceOrders(source, userid, limitid, order_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedAcceptableOnceOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.pagedAcceptableOnceOrders(source, userid, pageno, order_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String latestAcceptableOnOffOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.latestAcceptableOnOffOrders(source, userid, limitid, start_addr, end_addr, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedAcceptableOnOffOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.pagedAcceptableOnOffOrders(source, userid, pageno, start_addr, end_addr, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String latestAcceptableLongOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.latestAcceptableLongOrders(source, userid, limitid, start_addr, end_addr, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String pagedAcceptableLongOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.pagedAcceptableLongOrders(source, userid, pageno, start_addr, end_addr, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String passengerInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.passengerInfo(source, userid, passid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String driverInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.driverInfo(source, userid, driverid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String detailedDriverOrderInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.detailedDriverOrderInfo(source, userid, orderid, order_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String detailedPassengerOrderInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.detailedPassengerOrderInfo(source, userid, orderid, order_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String longOrderCancelInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.longOrderCancelInfo(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String acceptableInCityOrderDetailInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.acceptableInCityOrderDetailInfo(source, userid, orderid, order_type, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String acceptableLongOrderDetailInfo()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.acceptableLongOrderDetailInfo(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String publishOnceOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.publishOnceOrder(source, userid, start_addr, start_lat, start_lng, end_addr, end_lat, end_lng, start_time, midpoints, remark, reqstyle, price, city, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String publishOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.publishOnOffOrder(source, userid, start_addr, start_lat, start_lng, end_addr, end_lat, end_lng, start_time, midpoints, remark, reqstyle, price, days, city, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String publishLongOrder()
	{
		convertParamsToUTF8();	

		SVCResult stResult = order_service.publishLongOrder(source, userid, start_addr, start_city, start_lat, start_lng, end_addr, end_city, end_lat, end_lng, start_time, remark, price, seats_count, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}




	public String acceptOnceOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.acceptOnceOrder(source, userid, orderid, lat, lng, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String acceptOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.acceptOnOffOrder(source, userid, orderid, days, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}




	public String acceptLongOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.acceptLongOrder(source, userid, orderid, seats_count, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String setLongOrderPassword()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.setLongOrderPassword(source, userid, orderid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String confirmOnceOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.confirmOnceOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String cancelOnceOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.cancelOnceOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String confirmOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.confirmOnOffOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String refuseOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.refuseOnOffOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String cancelOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.cancelOnOffOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}




	public String executeOnceOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.executeOnceOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String executeOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.executeOnOffOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String executeLongOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.executeLongOrder(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String checkOnceOrderAcceptance()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.checkOnceOrderAcceptance(source, userid, orderid, lat, lng, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String signOnceOrderDriverArrival()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signOnceOrderDriverArrival(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String signOnOffOrderDriverArrival()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signOnOffOrderDriverArrival(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String signLongOrderDriverArrival()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signLongOrderDriverArrival(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}




	public String signOnceOrderPassengerUpload()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signOnceOrderPassengerUpload(source, driverid, orderid, passid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String signOnOffOrderPassengerUpload()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signOnOffOrderPassengerUpload(source, driverid, orderid, passid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String signLongOrderPassengerUpload()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signLongOrderPassengerUpload(source, driverid, orderid, passid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String signLongOrderPassengerGiveup()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.signLongOrderPassengerGiveup(source, driverid, orderid, passid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String startLongOrderDriving()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.startLongOrderDriving(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String endOnceOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.endOnceOrder(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String endOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.endOnOffOrder(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String endLongOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.endLongOrder(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String evaluateOnceOrderPass()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.evaluateOnceOrderPass(source, driverid, passid, orderid, level, msg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String evaluateOnOffOrderPass()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.evaluateOnOffOrderPass(source, driverid, passid, orderid, level, msg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String evaluateLongOrderPass()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.evaluateLongOrderPass(source, driverid, passid, orderid, level, msg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String evaluateOnceOrderDriver()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.evaluateOnceOrderDriver(source, passid, driverid, orderid, level, msg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String evaluateOnOffOrderDriver()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.evaluateOnOffOrderDriver(source, passid, driverid, orderid, level, msg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}


	public String evaluateLongOrderDriver()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.evaluateLongOrderDriver(source, passid, driverid, orderid, level, msg, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String pauseOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.pauseOnOffOrder(source, passid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String reserveNextOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.reserveNextOnOffOrder(source, passid, orderid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String usableCoupons()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.getUsableCoupons(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String changeOnceOrderPrice()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.changeOnceOrderPrice(source, userid, orderid, wait_time, new_price, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String payNormalOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.payNormalOrder(source, userid, orderid, order_type, price, coupons, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String payReserveOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.payReserveOrder(source, userid, orderid, price, coupons, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String orderCancelled()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.orderCancelled(source, userid, orderid, order_type, distance, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String checkOnceOrderAgree()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.checkOnceOrderAgree(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String cancelLongOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.cancelLongOrder(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String stopOnOffOrder()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.stopOnOffOrder(source, driverid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String setOnceOrderPassword()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.setOnceOrderPassword(source, userid, orderid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String setOnoffOrderPassword()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.setOnoffOrderPassword(source, userid, orderid, password, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String sendOnceOrderNotification()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.sendOnceOrderNotification(source, userid, orderid, pub_index, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String insertDriverAcceptableOrders()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.insertDriverAcceptableOrders(source, userid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}



	public String onceOrderDriverPos()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.onceOrderDriverPos(source, userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}

	//--------------------------------------
	public String clickchargingbtn()
	{
		convertParamsToUTF8();

		SVCResult stResult = order_service.clickchargingbtn( userid, orderid, devtoken);

		if (stResult == null)
		{
			stResult = new SVCResult();

			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}

		result = stResult.encodeToJSON();

		return Action.SUCCESS;
	}
	
	
	
	public String has_clickedchargingbtn()
	{
		convertParamsToUTF8();
		
		SVCResult stResult = order_service.has_clickedchargingbtn( userid, orderid, devtoken);
		
		if (stResult == null)
		{
			stResult = new SVCResult();
			
			stResult.retcode = ConstMgr.ErrCode_Exception;
			stResult.retmsg = ConstMgr.ErrMsg_Exception;
		}
		
		result = stResult.encodeToJSON();
		
		return Action.SUCCESS;
	}
	
	//--------------------------------------
	
	
	
	
	

	public void setSource(String source) {
		this.source = source;
	}


	public void setUserid(long userid) {
		this.userid = userid;
	}


	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}


	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}


	public void setLimit_time(String limit_time) {
		this.limit_time = limit_time;
	}


	public void setDevtoken(String devtoken) {
		this.devtoken = devtoken;
	}


	public void setLimitid_type(int limitid_type) {
		this.limitid_type = limitid_type;
	}

	public void setLimitid(int limitid) {
		this.limitid = limitid;
	}

	public void setPageno(int pageno) {
		this.pageno = pageno;
	}

	public void setStart_addr(String start_addr) {
		this.start_addr = start_addr;
	}

	public void setEnd_addr(String end_addr) {
		this.end_addr = end_addr;
	}

	public void setPassid(long passid) {
		this.passid = passid;
	}

	public void setDriverid(long driverid) {
		this.driverid = driverid;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public void setStart_lat(double start_lat) {
		this.start_lat = start_lat;
	}

	public void setStart_lng(double start_lng) {
		this.start_lng = start_lng;
	}

	public void setEnd_lat(double end_lat) {
		this.end_lat = end_lat;
	}

	public void setEnd_lng(double end_lng) {
		this.end_lng = end_lng;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public void setMidpoints(String midpoints) {
		this.midpoints = midpoints;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setReqstyle(int reqstyle) {
		this.reqstyle = reqstyle;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public void setStart_city(String start_city) {
		this.start_city = start_city;
	}

	public void setEnd_city(String end_city) {
		this.end_city = end_city;
	}

	public void setSeats_count(int seats_count) {
		this.seats_count = seats_count;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setWait_time(int wait_time) {
		this.wait_time = wait_time;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setNew_price(double new_price) {
		this.new_price = new_price;
	}

	public void setCoupons(String coupons) {
		this.coupons = coupons;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}


	public void setPub_index(int pub_index) {
		this.pub_index = pub_index;
	}

}
