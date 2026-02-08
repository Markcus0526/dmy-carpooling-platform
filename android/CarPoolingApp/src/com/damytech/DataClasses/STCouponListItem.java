package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-21
 * Time: 上午11:15
 * To change this template use File | Settings | File Templates.
 */
public class STCouponListItem
{
	public long id = 0;
	public double price = 0;
	public String desc = "";
	public int cond_type = ConstData.COUPON_COND_NO;
	public double cond_value = 0;
	public long syscoupon = 0;
	public boolean isSelected = false;
	public boolean isEnabled = true;

	public static STCouponListItem decodeFromJSON(JSONObject jsonObj)
	{
		STCouponListItem coupon = new STCouponListItem();

		try { coupon.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.price = jsonObj.getDouble("price"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.desc = jsonObj.getString("desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.cond_type = jsonObj.getInt("cond_type"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.cond_value = jsonObj.getDouble("cond_value"); } catch (Exception ex) { ex.printStackTrace(); }
		try { coupon.syscoupon = jsonObj.getLong("syscoupon"); } catch (Exception ex) { ex.printStackTrace(); }

		coupon.isSelected = false;
		coupon.isEnabled = true;

		return coupon;
	}

}
