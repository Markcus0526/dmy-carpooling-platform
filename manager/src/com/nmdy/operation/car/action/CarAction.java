package com.nmdy.operation.car.action;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.pinche.common.SqlSessionHelper;
import com.nmdy.operation.car.dao.carEntity;
import com.nmdy.operation.car.service.CarService;
import com.opensymphony.xwork2.ActionSupport;

public class CarAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int page;
	private int rows;
	private CarService carService;
	private JSONObject jsonObject;// 返回的json
	private String brand;// 车牌 ，根据车牌来查询车型
	private String car_style;// 车型，添加车型时候使用
	private String color_desc;// 颜色描述 ，添加颜色描述时候用
	private int type;// 级别 用于保存操作
	private int id;
	private int num;
	private String brandString;
	private String car_styleString;
	private String colorsString;

	/*
	 * 页面跳转方法
	 */
	public String go() {
		// System.out.println("go has been here...");
		return SUCCESS;
	}

	/*
	 * 获得所有车的品牌
	 */
	public String gotbrand() {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			List<carEntity> carbrand = carService.findcarbrand();

			// System.out.println("getbrand 开始");

			jsonMap.put("brand", carbrand);

			// System.out.println("getbrand的 jsonobjec" + jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 根据车牌 获得车型
	 */
	public String gotstylebybrand() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			if (null != brand) {

				List<carEntity> carstyle = carService.findstylebybrand(brand);
				// System.out.println("getstyle 开始" + carstyle.size());
				// System.out.println(carstyle);
				jsonMap.put("car_style", carstyle);

				// System.out.println("getstyle的 jsonobjec" + jsonObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 获得所有的颜色
	 */
	public String gotcolordesc() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			List<carEntity> colordesc = carService.findcolordesc();

			// System.out.println("color 开始");

			jsonMap.put("color_desc", colordesc);

			// System.out.println("getcolor的 jsonobjec" + jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 添加颜色
	 * 先校检是否存在颜色--num != 0存在
	 */
	public String addcolor() {
		Map<String, Object> params = new HashMap<String, Object>();

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// System.out.println("sss" + color_desc);
		try {
			if (null != color_desc) {
				params.put("color", color_desc);
				num = carService.insamecolor(params);
				if (num != 0) {
					jsonMap.put("insame", 1);
				} else {
					jsonMap.put("insame", 0);
					carService.addColor(color_desc);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 添加车牌
	 * 先校检是否存在品牌--num != 0存在
	 */
	public String addbrand() {
		// System.out.println("bsss" + brand);
		Map<String, Object> params = new HashMap<String, Object>();

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			if (null != brand) {
				params.put("brand", brand);
				num = carService.insamebrand(params);
				if (num != 0) {
					jsonMap.put("insame", 1);
				} else {
					jsonMap.put("insame", 0);
					carService.addBrand(brand);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 添加车型
	 * 先校检是否存在车型--num != 0存在
	 */
	public String addstyle() {
		// System.out.println("stylebrand:" + brand + car_style);
		Map<String, Object> params = new HashMap<String, Object>();

		Map<String, Object> jsonMap = new HashMap<String, Object>();
	
		try {
			if (null != brand && null != car_style) {
				params.put("brand", brand);
				params.put("car_style", car_style);
				num = carService.insamestyle(params);
				if(num!=0){
					jsonMap.put("insame",1);
				}else {
					jsonMap.put("insame",0);
					carService.addStyle(params);
					
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 删除车牌
	 * 先校检是否有用户使用这个品牌--num != 0存在
	 */
	public String delbrand() {
		// System.out.println("delbrand:" + brand);
		Map<String, Object> params = new HashMap<String, Object>();

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			if (null != brand) {
				params.put("brand", brand);
				num = carService.samebrand(params);
				if (num != 0) {
					jsonMap.put("same", 1);
				} else {
					jsonMap.put("same", 0);
					carService.deleteBrand(brand);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}
		return SUCCESS;
	}

	/*
	 * 删除颜色
	 * 先校检是否有用户的车是这个颜色的--num != 0存在
	 */
	public String delcolor() {
		Map<String, Object> params = new HashMap<String, Object>();
		// System.out.println("delcolor:" + color_desc);
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			if (null != color_desc) {
				params.put("color", color_desc);
				num = carService.samecolor(params);
				
				if (num != 0) {
					jsonMap.put("same", 1);
				} else {
					jsonMap.put("same", 0);
					carService.deleteColor(color_desc);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}

		return SUCCESS;
	}

	/*
	 * 删除车型
	 * 先校检是否有用户的车型是这个车型--num != 0存在
	 */
	public String delstyle() {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {

			if (null != brand && null != car_style) {
				params.put("brand", brand);
				params.put("car_style", car_style);
				num = carService.samestyle(params);
				if (num != 0) {
					jsonMap.put("same", 1);
				} else {
					jsonMap.put("same", 0);
					carService.deleteStyle(params);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jsonObject = JSONObject.fromObject(jsonMap);
		}

		return SUCCESS;
	}

	/*
	 * 保存车型
	 */
	public String save() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			if (null != brand && null != car_style && -1 != type) {
				params.put("brand", brand);
				params.put("car_style", car_style);
				params.put("type", type);
				carService.updateSave(params);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public String gotest2() {

		return SUCCESS;
	}

	/*
	 * 导出车型
	 */
	public String gotoutto() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			List<carEntity> car = carService.outto(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println("out 开始");
			jsonMap.put("rows", car);
			jsonMap.put("total", carService.getTotal());
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println("outto的 jsonobjec" + jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/*
	 * 查询出级别
	 */
	public String findtype() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brand", brand);
		params.put("car_style", car_style);
		carEntity cartype = carService.findtype(params);
		int type = cartype.getType();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("type", type);
		jsonObject = JSONObject.fromObject(jsonMap);
		return SUCCESS;
	}

	// ------------get set -------------

	public CarService getCarService() {
		return carService;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCar_style() {
		return car_style;
	}

	public void setCar_style(String car_style) {
		this.car_style = car_style;
	}

	public String getColor_desc() {
		return color_desc;
	}

	public void setColor_desc(String color_desc) {
		this.color_desc = color_desc;
	}

	public void setCarService(CarService carService) {
		this.carService = carService;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandString() {
		return brandString;
	}

	public void setBrandString(String brandString) {
		this.brandString = brandString;
	}

	public String getCar_styleString() {
		return car_styleString;
	}

	public void setCar_styleString(String car_styleString) {
		this.car_styleString = car_styleString;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getColorsString() {
		return colorsString;
	}

	public void setColorsString(String colorsString) {
		this.colorsString = colorsString;
	}

}
