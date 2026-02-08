package com.nmdy.operation.cityparam.action;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.nmdy.operation.cityparam.dao.LowSpeedOn;
import com.nmdy.operation.cityparam.service.CityParamService;
import com.opensymphony.xwork2.ModelDriven;

public class LowSpeedOnAction implements ModelDriven<LowSpeedOn>{
	JSONObject resultObject;
	LowSpeedOn lowSpeedOn = new LowSpeedOn();
	
	@Resource
	CityParamService cityParamService;
	
	public String load(){
		if(StringUtils.isBlank(lowSpeedOn.getCityCode())){
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", "城市代码不能为空");
			return "success";
		}
		
		Integer ret = cityParamService.queryLowSpeedOn(lowSpeedOn);
		resultObject = new JSONObject();
		resultObject.put("value", ret);
		return "success";
	}
	
	public String update(){
		if(StringUtils.isBlank(lowSpeedOn.getCityCode())){
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", "城市代码不能为空");
			return "success";
		}
		if(lowSpeedOn.getLowSpeedOn() != 0 && lowSpeedOn.getLowSpeedOn() != 1){
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", "lowSpeedOn只能为0或1");
			return "success";
		}
		
		int ret = cityParamService.updateLowSpeedOn(lowSpeedOn);
		resultObject = new JSONObject();
		resultObject.put("count", ret);
		resultObject.put("id", lowSpeedOn.getId());
		return "success";
		
	}
	
	@Override
	public LowSpeedOn getModel() {
		return lowSpeedOn;
	}

	public LowSpeedOn getLowSpeedOn() {
		return lowSpeedOn;
	}

	public void setLowSpeedOn(LowSpeedOn lowSpeedOn) {
		this.lowSpeedOn = lowSpeedOn;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

}
