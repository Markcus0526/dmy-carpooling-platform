package com.nmdy.operation.cityparam.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.mysql.jdbc.MysqlDataTruncation;
import com.nmdy.operation.cityparam.service.CityParamService;
import com.nmdy.operation.cityparam.dao.LowSpeedFeeRule;
import com.opensymphony.xwork2.ModelDriven;

public class LowSpeedFeeAction implements ModelDriven<LowSpeedFeeRule>{
	JSONObject resultObject;
	LowSpeedFeeRule lowSpeedFeeRule = new LowSpeedFeeRule();
	
	@Resource
	CityParamService cityParamService;
	
	public String edit(){
		int ret = 0;
		String vret = validate(lowSpeedFeeRule);
		if(null != vret){
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", vret);
			return "success";
		}
		
		try{
		if(0 != lowSpeedFeeRule.getId()){
			ret = cityParamService.editLowSpeedFeeRule(lowSpeedFeeRule);
		}else{
			ret = cityParamService.addLowSpeedFeeRule(lowSpeedFeeRule);
		}
		}catch(Exception ee){
			if (ee.getCause() instanceof MysqlDataTruncation) {
				resultObject = new JSONObject();
				resultObject.put("count", 0);
				resultObject.put("msg", "数字太大");
				return "success";
			}else{
				throw ee;
			}
		}
		
		resultObject = new JSONObject();
		resultObject.put("count", ret);
		resultObject.put("id", lowSpeedFeeRule.getId());
		return "success";
	}
	
	public String load(){
		if(!StringUtils.isBlank(lowSpeedFeeRule.getCityCode())){
			List ret = cityParamService.loadLowSpeedFeeRuleByCity(lowSpeedFeeRule.getCityCode());
			Map map = new HashMap();
			map.put("rows", ret);
			map.put("total", ret.size() + 1);
			if(null != ret){
				resultObject = JSONObject.fromObject(map);
			}
		}
		return "success";
	}

	public String remove(){
		if(lowSpeedFeeRule.getId() > 0){
		   int ret = cityParamService.removeLowSpeedFeeRule(lowSpeedFeeRule.getId());
			resultObject = new JSONObject();
			resultObject.put("count", ret);
			resultObject.put("id", lowSpeedFeeRule.getId());
		}
		
		return "success";
	}
	
	private String validate(LowSpeedFeeRule lowSpeedFeeRule){
		if(lowSpeedFeeRule.getRushHour() != 0 && lowSpeedFeeRule.getRushHour() != 1){
			return "流量值错误:" + lowSpeedFeeRule.getRushHour();
		}
		
		if(!lowSpeedFeeRule.getBeginTime().matches("\\d{2}:\\d{2}:\\d{2}")){
			return "开始时间格式不对:" + lowSpeedFeeRule.getBeginTime();
		}
		
		if(!lowSpeedFeeRule.getEndTime().matches("\\d{2}:\\d{2}:\\d{2}")){
			return "结束时间格式不对:" + lowSpeedFeeRule.getEndTime();
		}
		
		String[] ary1 = lowSpeedFeeRule.getBeginTime().split(":");
		String[] ary2 = lowSpeedFeeRule.getEndTime().split(":");
		if(ary1[0].compareTo(ary2[0]) < 0){
		}else if(ary1[0].compareTo(ary2[0]) == 0 && ary1[1].compareTo(ary2[1]) < 0){
	    }else if(ary1[0].compareTo(ary2[0]) == 0 && ary1[1].compareTo(ary2[1]) == 0 
	       && ary1[2].compareTo(ary2[2]) < 0){
	    }else{
	    	return "开始时间应小于结束时间";
	    }
		
		
		return null;
	}

	@Override
	public LowSpeedFeeRule getModel() {
		return lowSpeedFeeRule;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
}
