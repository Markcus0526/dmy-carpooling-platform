package com.damytech.DataClasses;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-20
 * Time: 上午12:43
 * To change this template use File | Settings | File Templates.
 */
public class STLongOrderPassInfo
{
	public long uid = 0;
	public String img = "";
	public String name = "";
	public int gender = 0;
	public int age = 0;
	public int state = 0;
	public String state_desc = "";
	public int seat_count = 0;
	public String seat_count_desc = "";
	public String phone = "";
	public double evgood_rate = 0;
	public String evgood_rate_desc = "";
	public int carpool_count = 0;
	public String carpool_count_desc = "";
	public int verified = 0;
	public String verified_desc = "";
	public int evaluated = 0;
	public String eval_content = "";
	public String evaluated_desc = "";


	public static STLongOrderPassInfo decodeFromJSON(JSONObject jsonObj)
	{
		STLongOrderPassInfo passInfo = new STLongOrderPassInfo();

		try { passInfo.uid = jsonObj.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.img = jsonObj.getString("img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.name = jsonObj.getString("name"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.gender = jsonObj.getInt("gender"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.age = jsonObj.getInt("age"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.state = jsonObj.getInt("state"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.state_desc = jsonObj.getString("state_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.seat_count = jsonObj.getInt("seat_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.seat_count_desc = jsonObj.getString("seat_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.phone = jsonObj.getString("phone"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.evgood_rate = jsonObj.getLong("evgood_rate"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.evgood_rate_desc = jsonObj.getString("evgood_rate_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.carpool_count = jsonObj.getInt("carpool_count"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.carpool_count_desc = jsonObj.getString("carpool_count_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.verified = jsonObj.getInt("verified"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.verified_desc = jsonObj.getString("verified_desc"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.evaluated = jsonObj.getInt("evaluated"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.eval_content = jsonObj.getString("eval_content"); } catch (Exception ex) { ex.printStackTrace(); }
		try { passInfo.evaluated_desc = jsonObj.getString("evaluated_desc"); } catch (Exception ex) { ex.printStackTrace(); }

		return passInfo;
	}

}
