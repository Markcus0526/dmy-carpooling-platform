package com.webapi.crypto.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.Action;
import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.crypto.service.SVCCryptoPayService;
import com.webapi.structure.SVCResult;


/**
 * Struts2 action for the crypto payment path. Endpoints:
 *
 *   /webservice/payOrderCrypto     — verify + record an on-chain deposit
 *   /webservice/getCryptoPayment   — read the recorded on-chain state for an order
 *
 * These sit alongside the existing payNormalOrder / payReserveOrder endpoints
 * without replacing them; the client picks which one to call based on the
 * user's chosen payment method.
 */
public class SVCCryptoPayAction
{
	// --- Input parameters (Struts2 populates these from POST/GET) ---
	private String source        = "";
	private long   userid        = -1;
	private long   orderid       = -1;
	private int    order_type    = -1;
	private double price         = -1;
	private String tx_hash       = "";
	private String wallet_addr   = "";
	private String devtoken      = "";

	// --- Output ---
	private JSONObject result = new JSONObject();

	private SVCCryptoPayService svc = new SVCCryptoPayService();


	private void convertParamsToUTF8()
	{
		source      = ApiGlobal.fixEncoding(source);
		tx_hash     = ApiGlobal.fixEncoding(tx_hash);
		wallet_addr = ApiGlobal.fixEncoding(wallet_addr);
		devtoken    = ApiGlobal.fixEncoding(devtoken);
	}


	public String payOrderCrypto()
	{
		convertParamsToUTF8();

		SVCResult r = svc.payOrderCrypto(source, userid, orderid, order_type,
				price, tx_hash, wallet_addr, devtoken);

		if (r == null)
		{
			r = new SVCResult();
			r.retcode = ConstMgr.ErrCode_Exception;
			r.retmsg  = ConstMgr.ErrMsg_Exception;
		}
		result = r.encodeToJSON();
		return Action.SUCCESS;
	}


	public String getCryptoPayment()
	{
		SVCResult r = svc.getCryptoPayment(orderid);

		if (r == null)
		{
			r = new SVCResult();
			r.retcode = ConstMgr.ErrCode_Exception;
			r.retmsg  = ConstMgr.ErrMsg_Exception;
		}
		result = r.encodeToJSON();
		return Action.SUCCESS;
	}


	// --- Getters/setters (Struts2 requires bean-style access) ---
	public String getSource()          { return source; }
	public void   setSource(String s)  { this.source = s; }
	public long   getUserid()          { return userid; }
	public void   setUserid(long v)    { this.userid = v; }
	public long   getOrderid()         { return orderid; }
	public void   setOrderid(long v)   { this.orderid = v; }
	public int    getOrder_type()      { return order_type; }
	public void   setOrder_type(int v) { this.order_type = v; }
	public double getPrice()           { return price; }
	public void   setPrice(double v)   { this.price = v; }
	public String getTx_hash()         { return tx_hash; }
	public void   setTx_hash(String v) { this.tx_hash = v; }
	public String getWallet_addr()     { return wallet_addr; }
	public void   setWallet_addr(String v) { this.wallet_addr = v; }
	public String getDevtoken()        { return devtoken; }
	public void   setDevtoken(String v){ this.devtoken = v; }
	public JSONObject getResult()      { return result; }
	public void   setResult(JSONObject v) { this.result = v; }
}
