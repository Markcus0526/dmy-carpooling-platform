package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-1
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
public class STNotifyOrder
{
	public long uid = 0;
	public String title = "";
	public String contents = "";
	public String time = "";
	public long orderid = 0;
	public int ordertype = 0;
	public int state = 0;
	public int hasread = 0;
	public int user_role = 0;

	public static STNotifyOrder dcodeFromJSON(JSONObject jsonObj)
	{
		STNotifyOrder notifyOrder = new STNotifyOrder();

		try { notifyOrder.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.contents = jsonObj.getString("contents"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.time = jsonObj.getString("time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.orderid = jsonObj.getLong("orderid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.ordertype = jsonObj.getInt("ordertype"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.hasread = jsonObj.getInt("hasread"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyOrder.user_role = jsonObj.getInt("user_role"); } catch (Exception ex) { ex.printStackTrace(); }

		return notifyOrder;
	}

}
