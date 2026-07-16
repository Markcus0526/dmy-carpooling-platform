package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-1
 * Time: 下午11:49
 * To change this template use File | Settings | File Templates.
 */
public class STAnnouncement
{
	public long uid = 0;
	public String title = "";
	public String contents = "";
	public String time = "";

	public static STAnnouncement dcodeFromJSON(JSONObject jsonObj)
	{
		STAnnouncement announcementInfo = new STAnnouncement();

		try { announcementInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { announcementInfo.title = jsonObj.getString("title"); } catch (Exception ex) { ex.printStackTrace(); }
		try { announcementInfo.contents = jsonObj.getString("contents"); } catch (Exception ex) { ex.printStackTrace(); }
		try { announcementInfo.time = jsonObj.getString("time"); } catch (Exception ex) { ex.printStackTrace(); }

		return announcementInfo;
	}
}
