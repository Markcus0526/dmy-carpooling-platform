package com.webapi.environment.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.webapi.common.ApiGlobal;
import com.webapi.common.ConstMgr;
import com.webapi.common.DBManager;
import com.webapi.structure.SVCCarType;
import com.webapi.structure.SVCCity;
import com.webapi.structure.SVCColor;
import com.webapi.structure.SVCGlobalParams;
import com.webapi.structure.SVCResult;
import com.webapi.structure.SVCUser;

public class SVCEnvService {
	public SVCResult defaultShareContents(String source, long userid, 
			String inputUrl, int shareFlag, String devtoken) {//增加了分享功能参数
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + inputUrl + "," + shareFlag + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}

		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			// Create database entity
			dbConn = DBManager.getDBConnection();
			SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);

			if (userinfo == null) {
				result.retcode = ConstMgr.ErrCode_Normal;
				result.retmsg = ConstMgr.ErrMsg_AuthenticateError;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			} else if (!userinfo.device_token.equals(devtoken)) {
				result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
				result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;

				ApiGlobal.logMessage(result.encodeToJSON().toString());

				return result;
			}
			

			stmt = dbConn.createStatement();
			szQuery = "SELECT code,value FROM environment WHERE deleted = 0 AND "
					+ "(code = 'app_download_url' OR code = 'app_download_content' OR code = 'share_url' "
					+ "OR code = 'share_content' OR code = 'share_main_url')";
			rs = stmt.executeQuery(szQuery);





			String appDownloadUrl = "";
			String appDownloadContent = "";
			String shareUrl = "";
			String shareContent = "";
			String ShareMainUrl = "";
			JSONObject retdata = new JSONObject();
			while (rs.next()) {
				String szCode = rs.getString("code");
				String szValue = rs.getString("value");





				if (szCode.equals("app_download_url")) {
					appDownloadUrl = szValue;
				} else if (szCode.equals("app_download_content")) {
					appDownloadContent = szValue;
				} else if (szCode.equals("share_url")) {

					shareUrl = szValue;
				}else if (szCode.equals("share_content")) {

					shareContent = szValue;
				}else if (szCode.equals("share_main_url")) {

					ShareMainUrl = szValue;
				}
			}

			if(inputUrl.equals(ShareMainUrl)){
				retdata.put("output_url", appDownloadUrl);
				retdata.put("output_content", appDownloadContent);
			} else{
				retdata.put("output_url", shareUrl);
				retdata.put("output_content", shareContent);
			}
			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = retdata;

		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (dbConn != null)
					dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
/**
 * 乘客传进来开始地点和结束地点以及城市，中途地点到后台，后台返回一个参考价格
 * 1，乘客传进来开始地点和结束地点，后台返回经济型轿车平台平均价
 * 2，乘客传进来开始地点和结束地点，后台返回舒适型型轿车平台平均价
 * 3，乘客传进来开始地点和结束地点，后台返回豪华型轿车平台平均价
 * 4，乘客传进来开始地点和结束地点，后台返回商务型轿车平台平均价
 * 5，计算开始地点和结束地点的距离，采用的是调用百度接口的方式
 * @param source
 * @param userid
 * @param city
 * @param start_lat
 * @param start_lng
 * @param end_lat
 * @param end_lng
 * @param start_time
 * @param midpoints
 * @param devtoken
 * @return
 */
	public SVCResult sysPriceInfo(String source, long userid, String city,
			double start_lat, double start_lng, double end_lat, double end_lng,
			String start_time, String midpoints, String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + city + "," + start_lat + "," + start_lng + "," + end_lat + "," + end_lng + "," + start_time + "," + midpoints + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || city.equals("")
				|| devtoken.equals("") || start_time.equals("")
				|| ApiGlobal.String2Date(start_time) == null) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
				if (userinfo == null) {
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_AuthenticateError;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					SVCCity city_info = null;
					SVCGlobalParams global_params = null;

					double points_per_add_ratio, points_per_add_integer, points_per_add_active, price_limit_ratio, price_limit_integer, price_limit_active;

					

					stmt = dbConn.createStatement();

					// Global parameters
					szQuery = "SELECT * FROM global_params WHERE deleted = 0 LIMIT 0,1";
					rs = stmt.executeQuery(szQuery);
					rs.next(); // Global params always exist. If not exist, it
								// is abnormal
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					// City parameters
					city = ApiGlobal.removeCityCharacter(city);
					szQuery = "SELECT * FROM city WHERE deleted=0 AND name LIKE \"%"
							+ city + "%\"";
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						city_info = SVCCity.decodeFromResultSet(rs);
					rs.close();

					if (city_info == null || city_info.a1 < 0) // This means
																// city
																// parameters
																// are not set
					{
						points_per_add_ratio = global_params.points_per_add_ratio;
						points_per_add_integer = global_params.points_per_add_integer;
						points_per_add_active = global_params.points_per_add_active;
						price_limit_ratio = global_params.price_limit_ratio;
						price_limit_integer = global_params.price_limit_integer;
						price_limit_active = global_params.price_limit_active;
					} else {
						points_per_add_ratio = city_info.points_per_add_ratio;
						points_per_add_integer = city_info.points_per_add_integer;
						points_per_add_active = city_info.points_per_add_active;
						price_limit_ratio = city_info.price_limit_ratio;
						price_limit_integer = city_info.price_limit_integer;
						price_limit_active = city_info.price_limit_active;
					}

					// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					// Get mid points coordinates and calculate distance
					// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					String description = "";
					double price_Interval = 0, limit_Price = 0;
					JSONObject objTemp = null;
					double priceResult = 0, distResult = 0; 

					JSONArray arrResults = new JSONArray();

					//1， Economic car style result，乘客传进来开始地点和结束地点，后台返回经济型轿车平台平均价
					JSONObject jsonEconomic = new JSONObject();

					objTemp = ApiGlobal.getDistAndSystemAveragePrice(dbConn, userid, city,
							start_time, start_lat, start_lng, end_lat, end_lng,
							midpoints, 1);
					priceResult = objTemp.getDouble("price");
					distResult = objTemp.getDouble("dist");

					if (points_per_add_active == 1) // Price interval with fixed
													// value
						price_Interval = points_per_add_integer;
					else
						// Price interval with ratio
						price_Interval = priceResult * points_per_add_ratio / 100;

					if (price_limit_active == 1) // Price min limit with fixed
													// value
						limit_Price = price_limit_integer;
					else
						// Price min limit with ratio
						limit_Price = priceResult * price_limit_ratio / 100;

					jsonEconomic.put("car_style", 1);
					jsonEconomic.put("aver_price",
							ApiGlobal.Double2String(priceResult, 2));
					jsonEconomic.put("price_interval",
							ApiGlobal.Double2String(price_Interval, 2));
					jsonEconomic.put("min_limit",
							ApiGlobal.Double2String(limit_Price, 2));
					jsonEconomic.put("distance",
							ApiGlobal.Double2String(distResult, 2));

					description = ConstMgr.STR_BENCIXINGCHENGWEI
							+ ApiGlobal.Double2String(distResult, 2)
							+ ConstMgr.STR_GONGLI + ", "
							+ ConstMgr.STR_PINGTAIPINGJUNJIAGEWEI
							+ ApiGlobal.Double2String(priceResult, 2)
							+ ConstMgr.STR_DIAN + "/" + ConstMgr.STR_CI;
					jsonEconomic.put("description", description);

					arrResults.add(jsonEconomic);

					//,2，Comfortable car style result，乘客传进来开始地点和结束地点，后台返回舒适性轿车平台平均价
					JSONObject jsonComfort = new JSONObject();

					objTemp = ApiGlobal.getDistAndSystemAveragePrice(dbConn, userid, city,
							start_time, start_lat, start_lng, end_lat, end_lng,
							midpoints, 2);
					priceResult = objTemp.getDouble("price");
					distResult = objTemp.getDouble("dist");

					if (points_per_add_active == 1) // Price interval with fixed
													// value
						price_Interval = points_per_add_integer;
					else
						// Price interval with ratio
						price_Interval = priceResult * points_per_add_ratio / 100;

					if (price_limit_active == 1) // Price min limit with fixed
													// value
						limit_Price = price_limit_integer;
					else
						// Price min limit with ratio
						limit_Price = priceResult * price_limit_ratio / 100;

					jsonComfort.put("car_style", 2);
					jsonComfort.put("aver_price",
							ApiGlobal.Double2String(priceResult, 2));
					jsonComfort.put("price_interval",
							ApiGlobal.Double2String(price_Interval, 2));
					jsonComfort.put("min_limit",
							ApiGlobal.Double2String(limit_Price, 2));
					jsonComfort.put("distance",
							ApiGlobal.Double2String(distResult, 2));

					description = ConstMgr.STR_BENCIXINGCHENGWEI
							+ ApiGlobal.Double2String(distResult, 2)
							+ ConstMgr.STR_GONGLI + ", "
							+ ConstMgr.STR_PINGTAIPINGJUNJIAGEWEI
							+ ApiGlobal.Double2String(priceResult, 2)
							+ ConstMgr.STR_DIAN + "/" + ConstMgr.STR_CI;
					jsonComfort.put("description", description);

					arrResults.add(jsonComfort);

					// 3，Luxury car style result，乘客传进来开始地点和结束地点，后台返回豪华型轿车平台平均价
					JSONObject jsonLuxury = new JSONObject();

					objTemp = ApiGlobal.getDistAndSystemAveragePrice(dbConn, userid, city,
							start_time, start_lat, start_lng, end_lat, end_lng,
							midpoints, 3);
					priceResult = objTemp.getDouble("price");
					distResult = objTemp.getDouble("dist");

					if (points_per_add_active == 1) // Price interval with fixed
													// value
						price_Interval = points_per_add_integer;
					else
						// Price interval with ratio
						price_Interval = priceResult * points_per_add_ratio / 100;

					if (price_limit_active == 1) // Price min limit with fixed
													// value
						limit_Price = price_limit_integer;
					else
						// Price min limit with ratio
						limit_Price = priceResult * price_limit_ratio / 100;

					jsonLuxury.put("car_style", 3);
					jsonLuxury.put("aver_price",
							ApiGlobal.Double2String(priceResult, 2));
					jsonLuxury.put("price_interval",
							ApiGlobal.Double2String(price_Interval, 2));
					jsonLuxury.put("min_limit",
							ApiGlobal.Double2String(limit_Price, 2));
					jsonLuxury
							.put("distance", ApiGlobal.Double2String(distResult, 2));

					description = ConstMgr.STR_BENCIXINGCHENGWEI
							+ ApiGlobal.Double2String(distResult, 2)
							+ ConstMgr.STR_GONGLI + ", "
							+ ConstMgr.STR_PINGTAIPINGJUNJIAGEWEI
							+ ApiGlobal.Double2String(priceResult, 2)
							+ ConstMgr.STR_DIAN + "/" + ConstMgr.STR_CI;
					jsonLuxury.put("description", description);

					arrResults.add(jsonLuxury);

					// 5，Business car style result，乘客传进来开始地点和结束地点，后台返回商务型轿车平台平均价
					JSONObject jsonBusiness = new JSONObject();

					objTemp = ApiGlobal.getDistAndSystemAveragePrice(dbConn, userid, city,
							start_time, start_lat, start_lng, end_lat, end_lng,
							midpoints, 4);
					priceResult = objTemp.getDouble("price");
					distResult = objTemp.getDouble("dist");

					if (points_per_add_active == 1) // Price interval with fixed
													// value
						price_Interval = points_per_add_integer;
					else
						// Price interval with ratio
						price_Interval = priceResult * points_per_add_ratio / 100;

					if (price_limit_active == 1) // Price min limit with fixed
													// value
						limit_Price = price_limit_integer;
					else
						// Price min limit with ratio
						limit_Price = priceResult * price_limit_ratio / 100;

					jsonBusiness.put("car_style", 4);
					jsonBusiness.put("aver_price",
							ApiGlobal.Double2String(priceResult, 2));
					jsonBusiness.put("price_interval",
							ApiGlobal.Double2String(price_Interval, 2));
					jsonBusiness.put("min_limit",
							ApiGlobal.Double2String(limit_Price, 2));
					jsonBusiness.put("distance",
							ApiGlobal.Double2String(distResult, 2));

					description = ConstMgr.STR_BENCIXINGCHENGWEI
							+ ApiGlobal.Double2String(distResult, 2)
							+ ConstMgr.STR_GONGLI + ", "
							+ ConstMgr.STR_PINGTAIPINGJUNJIAGEWEI
							+ ApiGlobal.Double2String(priceResult, 2)
							+ ConstMgr.STR_DIAN + "/" + ConstMgr.STR_CI;
					jsonBusiness.put("description", description);

					arrResults.add(jsonBusiness);

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;
					result.retdata = arrResults;
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ex.getMessage();
			} finally {
				// Close result set
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}

				// Close normal statement
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}

				// Close db connection
				if (dbConn != null) {
					try {
						dbConn.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
/**
 * 1，获取所有车型返回
 * 2，获取所有颜色返回
 * @param source
 * @return
 */
	public SVCResult brandsAndColors(String source) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source);

		// Check parameters
		if (source.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}
		if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;

			ApiGlobal.logMessage(result.encodeToJSON().toString());

			return result;
		}
		Connection dbConn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String szQuery = "";

		try {
			JSONArray arrBrands = new JSONArray(), arrTypes = new JSONArray(), arrColors = new JSONArray();
			JSONObject brandItem = new JSONObject();
			String szBrand = "";

			// Create database entity
			dbConn = DBManager.getDBConnection();

			stmt = dbConn.createStatement();

			// Collecting car brands
			szQuery = "SELECT * FROM car_type WHERE deleted=0 AND car_style <> '' ORDER BY brand ASC";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCCarType cartype = SVCCarType.decodeFromResultSet(rs);

				if (szBrand.equals("") || !szBrand.equals(cartype.brand)) // New
																			// brand.
																			// Add
																			// brand
																			// item
																			// to
																			// <arrBrands>
				{
					if (!szBrand.equals("")) // One brand is completed
					{
						brandItem.put("types", arrTypes);
						arrBrands.add(brandItem);
						arrTypes.clear();
					}

					szBrand = cartype.brand;

					brandItem = new JSONObject();
					brandItem.put("id", cartype.id);
					brandItem.put("name", szBrand);
				}

				JSONObject type_item = new JSONObject();
				type_item.put("id", cartype.id);
				type_item.put("name", cartype.car_style);
				type_item.put("style", cartype.type);
				arrTypes.add(type_item);
			}
			rs.close();

			szQuery = "SELECT * FROM color WHERE deleted = 0 AND code <> ''";

			rs = stmt.executeQuery(szQuery);
			while (rs.next()) {
				SVCColor color = SVCColor.decodeFromResultSet(rs);

				JSONObject colorItem = new JSONObject();

				colorItem.put("id", color.id);
				colorItem.put("name", color.color_desc);
				colorItem.put("code", color.code);

				arrColors.add(colorItem);
			}

			JSONObject retdata = new JSONObject();
			retdata.put("brands", arrBrands);
			retdata.put("colors", arrColors);

			result.retcode = ConstMgr.ErrCode_None;
			result.retmsg = ConstMgr.ErrMsg_None;
			result.retdata = retdata;
		} catch (Exception ex) {
			ex.printStackTrace();

			result.retcode = ConstMgr.ErrCode_Exception;
			result.retmsg = ex.getMessage();
		} finally {
			try {
				if(rs!=null)rs.close();
				if(stmt!=null)stmt.close();
				if(dbConn!=null)dbConn.close();
			} catch (Exception sql_ex) {
				sql_ex.printStackTrace();
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
/**
 * 获取平台信息费的计算方式，简单查询即可。
 * @param source
 * @param userid
 * @param city
 * @param devtoken
 * @return
 */
	public SVCResult infoFeeMethod(String source, long userid, String city,
			String devtoken) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source + "," + userid + "," + city + "," + devtoken);

		// Check parameters
		if (source.equals("") || userid < 0 || city.equals("")
				|| devtoken.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			Statement stmt = null;
			ResultSet rs = null;
			String szQuery = "";

			try {
				// Create database entity
				dbConn = DBManager.getDBConnection();
				SVCUser userinfo = ApiGlobal.getUserInfoFromUserID(dbConn,userid);
				if (userinfo == null) {
					result.retcode = ConstMgr.ErrCode_Normal;
					result.retmsg = ConstMgr.ErrMsg_AuthenticateError;
				} else if (!userinfo.device_token.equals(devtoken)) {
					result.retcode = ConstMgr.ErrCode_DeviceNotMatch;
					result.retmsg = ConstMgr.ErrMsg_DeviceNotMatch;
				} else {
					JSONObject retdata = new JSONObject();

					SVCCity city_info = null;
					SVCGlobalParams global_params = null;

					double ratio = 0, fix_val = 0;
					int method = 0;
					double insu_fee = 0;//保险费  modify  by czq 

					

					stmt = dbConn.createStatement();

					// Global parameters
					szQuery = "SELECT * FROM global_params WHERE deleted = 0 LIMIT 0,1";
					rs = stmt.executeQuery(szQuery);
					rs.next(); // Global params always exist. If not exist, it
								// is abnormal
					global_params = SVCGlobalParams.decodeFromResultSet(rs);
					rs.close();

					insu_fee=global_params.insu_fee;//使用全局参数   modify by chuzhiqiang 
					// City parameters
					city = ApiGlobal.removeCityCharacter(city);
					szQuery = "SELECT * FROM city WHERE deleted=0 AND name LIKE \"%"
							+ city + "%\"";
					rs = stmt.executeQuery(szQuery);
					if (rs.next())
						city_info = SVCCity.decodeFromResultSet(rs);
					rs.close();

					if (city_info == null || city_info.active < 0) // This means
																	// city
																	// parameters
																	// are not
																	// set
					{
						method = global_params.active;
						ratio = global_params.ratio;
						fix_val = global_params.integer_;
						//insu_fee = global_params.insu_fee;
					} else {
						method = city_info.active;
						ratio = city_info.ratio;
						fix_val = city_info.integer_;
						//insu_fee = city_info.insu_fee;
					}

					result.retcode = ConstMgr.ErrCode_None;
					result.retmsg = ConstMgr.ErrMsg_None;

					if (method == 0) {
						retdata.put("calc_method", 2);
						retdata.put("value", ratio);
						retdata.put("insu_fee", insu_fee*2);//保险费*2    一个执行订单对应两个保单（不管乘客是否进行了身份认证） modify  by  chuzhiqiang
					} else {
						retdata.put("calc_method", 1);
						retdata.put("value", fix_val);
						retdata.put("insu_fee", insu_fee*2);//保险费*2    一个执行订单对应两个保单（不管乘客是否进行了身份认证）modify  by  chuzhiqiang
					}

					result.retdata = retdata;
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				result.retcode = ConstMgr.ErrCode_Exception;
				result.retmsg = ex.getMessage();
			} finally {
				// Close result set
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}

				// Close normal statement
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}

				// Close db connection
				if (dbConn != null) {
					try {
						dbConn.close();
					} catch (Exception sql_ex) {
						sql_ex.printStackTrace();
					}
				}
			}
		}
		System.out.println("insu_fee---------"+result.retdata.toString());


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}
/**
 * 简单查询envirment表
 * @param source
 * @return
 */
	public SVCResult shareLinkAddr(String source) {
		SVCResult result = new SVCResult();

		ApiGlobal.logMessage(source);

		// Check parameters
		if (source.equals("")) {
			result.retcode = ConstMgr.ErrCode_Param;
			result.retmsg = ConstMgr.ErrMsg_Param;
		} else if (!ApiGlobal.IsValidSource(source)) {
			result.retcode = ConstMgr.ErrCode_Normal;
			result.retmsg = ConstMgr.ErrMsg_InvalidSource;
		} else {
			Connection dbConn = null;
			try {
				dbConn = DBManager.getDBConnection();
				result.retcode = ConstMgr.ErrCode_None;
				result.retmsg = ConstMgr.ErrMsg_None;
				result.retdata = ApiGlobal
						.getEnvValueFromCode(dbConn, ConstMgr.ENVCODE_SHARELINKADDR);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (dbConn != null)
				{
					try { dbConn.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
			}
		}


		ApiGlobal.logMessage(result.encodeToJSON().toString());

		return result;
	}

}
