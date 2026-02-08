package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-9
 * Time: 上午9:07
 * To change this template use File | Settings | File Templates.
 */
public class STCoupon
{
	public long uid = 0;
	public String range = "";
	public String contents = "";
	public String usecond = "";
	public String dateexp = "";
	public String couponcode = "";
	public String unitname = "";
	public int is_goods = 0;

	public static STCoupon decodeFromJSON(JSONObject jsonObj)
	{
		STCoupon coupon = new STCoupon();

		try { coupon.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.range = jsonObj.getString("range"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.contents = jsonObj.getString("contents"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.usecond = jsonObj.getString("usecond"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.dateexp = jsonObj.getString("dateexp"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.couponcode = jsonObj.getString("couponcode"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.unitname = jsonObj.getString("unitname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.is_goods = jsonObj.getInt("is_goods"); } catch (Exception ex) { ex.printStackTrace(); }

		return coupon;
	}
}
