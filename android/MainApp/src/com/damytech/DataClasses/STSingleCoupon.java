package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-1
 * Time: 下午11:56
 * To change this template use File | Settings | File Templates.
 */
public class STSingleCoupon
{
	public long uid = 0;
	public String product_name = "";
	public String coupon_code = "";
	public String time = "";
	public String unitname = "";
	public String usecond = "";
	public String dateexp = "";

	public static STSingleCoupon dcodeFromJSON(JSONObject jsonObj)
	{
		STSingleCoupon singleCoupon = new STSingleCoupon();

		try { singleCoupon.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { singleCoupon.product_name = jsonObj.getString("product_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { singleCoupon.coupon_code = jsonObj.getString("coupon_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { singleCoupon.time = jsonObj.getString("time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { singleCoupon.unitname = jsonObj.getString("unitname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { singleCoupon.usecond = jsonObj.getString("usecond"); } catch (Exception ex) { ex.printStackTrace(); }
		try { singleCoupon.dateexp = jsonObj.getString("dateexp"); } catch (Exception ex) { ex.printStackTrace(); }

		return singleCoupon;
	}

}
