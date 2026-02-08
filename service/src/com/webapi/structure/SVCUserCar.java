package com.webapi.structure;

import java.sql.ResultSet;

import net.sf.json.JSONObject;

public class SVCUserCar {
	public long id = 0;
	public long userid = 0;
	public String vin = "";
	public String brand = "";
	public String style = "";
	public String color = "";
	public String eno = "";
	public String car_img = "";
	public String plate_num = "";
	public String plate_num_last3 = "";
	public int is_oper_vehicle = 0;
	public String vehicle_owner = "";
	public long car_type_id = 0;
	public int deleted = 0;


	public JSONObject encodeToJSON()
	{
		JSONObject jsonObj = new JSONObject();

		try { jsonObj.put("id", id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("userid", userid); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("vin", vin); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("brand", brand); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("style", style); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("color", color); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("eno", eno); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("car_img", car_img); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("plate_num", plate_num); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("plate_num_last3", plate_num_last3); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("is_oper_vehicle", is_oper_vehicle); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("vehicle_owner", vehicle_owner); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("car_type_id", car_type_id); } catch (Exception ex) { ex.printStackTrace(); }
		try { jsonObj.put("deleted", deleted); } catch (Exception ex) { ex.printStackTrace(); }

		return jsonObj;
	}


	public static SVCUserCar decodeFromJSON(JSONObject jsonObj)
	{
		if (jsonObj == null)
			return null;

		SVCUserCar user_car = new SVCUserCar();

		try { user_car.id = jsonObj.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.userid = jsonObj.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.vin = jsonObj.getString("vin"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.brand = jsonObj.getString("brand"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.style = jsonObj.getString("style"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.color = jsonObj.getString("color"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.eno = jsonObj.getString("eno"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.car_img = jsonObj.getString("car_img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.plate_num = jsonObj.getString("plate_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.plate_num_last3 = jsonObj.getString("plate_num_last3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.is_oper_vehicle = jsonObj.getInt("is_oper_vehicle"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.vehicle_owner = jsonObj.getString("vehicle_owner"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.car_type_id = jsonObj.getLong("car_type_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.deleted = jsonObj.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return user_car;
	}



	public static SVCUserCar decodeFromResultSet(ResultSet resultSet)
	{
		if (resultSet == null)
			return null;

		SVCUserCar user_car = new SVCUserCar();

		try { user_car.id = resultSet.getLong("id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.vin = resultSet.getString("vin"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.brand = resultSet.getString("brand"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.style = resultSet.getString("style"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.color = resultSet.getString("color"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.eno = resultSet.getString("eno"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.car_img = resultSet.getString("car_img"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.plate_num = resultSet.getString("plate_num"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.plate_num_last3 = resultSet.getString("plate_num_last3"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.is_oper_vehicle = resultSet.getInt("is_oper_vehicle"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.vehicle_owner = resultSet.getString("vehicle_owner"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.car_type_id = resultSet.getLong("car_type_id"); } catch (Exception ex) { ex.printStackTrace(); }
		try { user_car.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return user_car;
	}



}







