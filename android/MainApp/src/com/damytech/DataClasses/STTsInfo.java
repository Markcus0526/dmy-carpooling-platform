package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-8
 * Time: 下午7:16
 * To change this template use File | Settings | File Templates.
 */
public class STTsInfo
{
	public long uid = 0;
	public String tsid = "";
	public double operbalance = 0;
	public String source = "";
	public double remainbalance = 0;
	public String tstime = "";

	public static STTsInfo decodeFromJSON(JSONObject jsonObj)
	{
		STTsInfo info = new STTsInfo();

		try { info.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { info.tsid = jsonObj.getString("tsid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { info.operbalance = jsonObj.getDouble("operbalance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { info.source = jsonObj.getString("source"); } catch (Exception ex) { ex.printStackTrace(); }
		try { info.remainbalance = jsonObj.getDouble("remainbalance"); } catch (Exception ex) { ex.printStackTrace(); }
		try { info.tstime = jsonObj.getString("tstime"); } catch (Exception ex) { ex.printStackTrace(); }

		return info;
	}
}
