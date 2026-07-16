package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-13
 * Time: 下午7:17
 * To change this template use File | Settings | File Templates.
 */
public class STPassEvalInfo {
	public long uid = 0;
	public String driver_name = "";
	public int eval = 1;
	public String eval_desc = "";
	public String time = "";
	public String contents = "";

	public static STPassEvalInfo decodeFromJSON(JSONObject jsonObj)
	{
		STPassEvalInfo evalInfo = new STPassEvalInfo();

		try { evalInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evalInfo.driver_name = jsonObj.getString("pass_name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evalInfo.eval = jsonObj.getInt("eval"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evalInfo.eval_desc = jsonObj.getString("eval_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evalInfo.time = jsonObj.getString("time"); } catch (Exception ex) { ex.printStackTrace(); }
		try { evalInfo.contents = jsonObj.getString("contents"); } catch (Exception ex) { ex.printStackTrace(); }

		return evalInfo;
	}
}
