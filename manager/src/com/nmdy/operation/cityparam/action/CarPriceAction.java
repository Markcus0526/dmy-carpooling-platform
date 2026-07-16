package com.nmdy.operation.cityparam.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.mysql.jdbc.MysqlDataTruncation;
import com.nmdy.operation.cityparam.service.CityParamService;
import com.nmdy.operation.cityparam.dao.CarPriceRule;
import com.nmdy.operation.cityparam.dao.LowSpeedFeeRule;
import com.opensymphony.xwork2.ModelDriven;

public class CarPriceAction implements ModelDriven<CarPriceRule> {
	JSONObject resultObject;
	CarPriceRule carPriceRule = new CarPriceRule();

	@Resource
	CityParamService cityParamService;

	public String edit() {
		int ret = 0;
		String vret = validate(carPriceRule);
		if (null != vret) {
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", vret);
			return "success";
		}

		try {
			if (0 != carPriceRule.getId()) {
				ret = cityParamService.editCarPriceRule(carPriceRule);
			} else {
				ret = cityParamService.addCarPriceRule(carPriceRule);
			}
		} catch (Exception ee) {
			if (ee.getCause() instanceof MysqlDataTruncation) {
				resultObject = new JSONObject();
				resultObject.put("count", 0);
				resultObject.put("msg", "数字太大");
				return "success";
			} else {
				throw ee;
			}
		}

		resultObject = new JSONObject();
		resultObject.put("count", ret);
		resultObject.put("id", carPriceRule.getId());
		return "success";
	}

	public String load() {
		if (!StringUtils.isBlank(carPriceRule.getCityCode())) {
			List ret = cityParamService.loadCarPriceRuleByCity(carPriceRule
					.getCityCode());
			Map map = new HashMap();
			map.put("rows", ret);
			map.put("total", ret.size() + 1);
			if (null != ret) {
				resultObject = JSONObject.fromObject(map);
			}
		}
		return "success";
	}

	public String remove() {
		if (carPriceRule.getId() > 0) {
			int ret = cityParamService.removeCarPriceRule(carPriceRule.getId());
			resultObject = new JSONObject();
			resultObject.put("count", ret);
			resultObject.put("id", carPriceRule.getId());
		}

		return "success";
	}

	private String validate(CarPriceRule carPriceRule) {
		if (!carPriceRule.getBeginTime().matches("\\d{2}:\\d{2}:\\d{2}")) {
			return "开始时间格式不对:" + carPriceRule.getBeginTime();
		}

		if (!carPriceRule.getEndTime().matches("\\d{2}:\\d{2}:\\d{2}")) {
			return "结束时间格式不对:" + carPriceRule.getEndTime();
		}

		String[] ary1 = carPriceRule.getBeginTime().split(":");
		String[] ary2 = carPriceRule.getEndTime().split(":");
		if (ary1[0].compareTo(ary2[0]) < 0) {
		} else if (ary1[0].compareTo(ary2[0]) == 0
				&& ary1[1].compareTo(ary2[1]) < 0) {
		} else if (ary1[0].compareTo(ary2[0]) == 0
				&& ary1[1].compareTo(ary2[1]) == 0
				&& ary1[2].compareTo(ary2[2]) < 0) {
		} else {
			return "开始时间应小于结束时间";
		}

		return null;
	}

	@Override
	public CarPriceRule getModel() {
		return carPriceRule;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
}
