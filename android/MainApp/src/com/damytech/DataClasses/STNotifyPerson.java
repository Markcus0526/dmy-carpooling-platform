package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-1
 * Time: 下午11:54
 * To change this template use File | Settings | File Templates.
 */
public class STNotifyPerson
{
	public long uid = 0;
	public String title = "";
	public String contents = "";
	public String time = "";
	public long couponid = 0;
	public int hasread = 0;

	public static STNotifyPerson dcodeFromJSON(JSONObject jsonObj)
	{
		STNotifyPerson notifyPerson = new STNotifyPerson();

		try { notifyPerson.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyPerson.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyPerson.contents = jsonObj.getString("contents"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyPerson.time = jsonObj.getString("time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyPerson.couponid = jsonObj.getLong("couponid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { notifyPerson.hasread = jsonObj.getInt("hasread"); } catch (Exception ex) { ex.printStackTrace(); }

		return notifyPerson;
	}

}
