package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-1
 * Time: 下午11:47
 * To change this template use File | Settings | File Templates.
 */
public class STApplication
{
	public static final int APP_STATE_NOT_DOWNLOADED = 0;
	public static final int APP_STATE_DOWNLOADED_HALF = 1;
	public static final int APP_STATE_DOWNLOADED = 2;
	public static final int APP_STATE_UPDATABLE = 3;
	public static final int APP_STATE_INSTALLED = 4;
	public static final int APP_STATE_NOW_DOWNLOADING = 5;

	public long uid = 0;
	public String name = "";
	public String code = "";
	public String iconurl = "";
	public String packname = "";
	public String urlscheme = "";
//	public String latestver = "";
//	public String curver = "";
	public int latestver_code = 0;
	public int curver_code = 0;
	public String downloadurl = "";
	public int state = APP_STATE_NOT_DOWNLOADED;
	public long downloadedBytes = 0;
	public long totalSize = 0;

	public static STApplication dcodeFromJSON(JSONObject jsonObj)
	{
		STApplication appinfo = new STApplication();

		try { appinfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.code = jsonObj.getString("code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.iconurl = jsonObj.getString("image"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.packname = jsonObj.getString("packname"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.urlscheme = jsonObj.getString("urlscheme"); } catch (Exception ex) { ex.printStackTrace(); }
//		try { appinfo.latestver = jsonObj.getString("latestver"); } catch (Exception ex) { ex.printStackTrace(); }
//		try { appinfo.curver = jsonObj.getString("curver"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.latestver_code = jsonObj.getInt("latestver_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.curver_code = jsonObj.getInt("curver_code"); } catch (Exception ex) { ex.printStackTrace(); }
		try { appinfo.downloadurl = jsonObj.getString("downloadurl"); } catch (Exception ex) { ex.printStackTrace(); }

		return appinfo;
	}
}
