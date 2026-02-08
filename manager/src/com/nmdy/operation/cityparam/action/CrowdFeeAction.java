package com.nmdy.operation.cityparam.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.mysql.jdbc.MysqlDataTruncation;
import com.nmdy.operation.cityparam.service.CityParamService;
import com.nmdy.operation.cityparam.dao.CrowdedFeeRule;
import com.opensymphony.xwork2.ModelDriven;

public class CrowdFeeAction implements ModelDriven<CrowdedFeeRule>{
	JSONObject resultObject;
	CrowdedFeeRule crowdedFeeRule = new CrowdedFeeRule();
	
	@Resource
	CityParamService cityParamService;
	
	public String edit(){
		int ret = 0;
		String vret = validate(crowdedFeeRule);
		if(null != vret){
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", vret);
			return "success";
		}
		
		try{
		if(0 != crowdedFeeRule.getId()){
			ret = cityParamService.editCrowdedFee(crowdedFeeRule);
		}else{
			ret = cityParamService.addCrowdedFee(crowdedFeeRule);
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
		resultObject.put("id", crowdedFeeRule.getId());
		return "success";
	}
	
	public String load(){
		if(!StringUtils.isBlank(crowdedFeeRule.getCityCode())){
			List ret = cityParamService.loadCrowdedFeeByCity(crowdedFeeRule.getCityCode());
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
		if(crowdedFeeRule.getId() > 0){
		   int ret = cityParamService.removeCrowdedFee(crowdedFeeRule.getId());
			resultObject = new JSONObject();
			resultObject.put("count", ret);
			resultObject.put("id", crowdedFeeRule.getId());
		}
		
		return "success";
	}
	
	private String validate(CrowdedFeeRule crowdedFeeRule){
		if(crowdedFeeRule.getRushHour() != 0 && crowdedFeeRule.getRushHour() != 1){
			return "流量值错误:" + crowdedFeeRule.getRushHour();
		}
		
		if(!crowdedFeeRule.getBeginTime().matches("\\d{2}:\\d{2}:\\d{2}")){
			return "开始时间格式不对:" + crowdedFeeRule.getBeginTime();
		}
		
		if(!crowdedFeeRule.getEndTime().matches("\\d{2}:\\d{2}:\\d{2}")){
			return "结束时间格式不对:" + crowdedFeeRule.getEndTime();
		}
		
		String[] ary1 = crowdedFeeRule.getBeginTime().split(":");
		String[] ary2 = crowdedFeeRule.getEndTime().split(":");
		if(ary1[0].compareTo(ary2[0]) < 0){
		}else if(ary1[0].compareTo(ary2[0]) == 0 && ary1[1].compareTo(ary2[1]) < 0){
	    }else if(ary1[0].compareTo(ary2[0]) == 0 && ary1[1].compareTo(ary2[1]) == 0 
	       && ary1[2].compareTo(ary2[2]) < 0){
	    }else{
	    	return "开始时间应小于结束时间";
	    }
		
		
		return null;
	}
	
	public CrowdedFeeRule getCrowdedFeeRule() {
		return crowdedFeeRule;
	}

	public void setCrowdedFeeRule(CrowdedFeeRule crowdedFeeRule) {
		this.crowdedFeeRule = crowdedFeeRule;
	}

	@Override
	public CrowdedFeeRule getModel() {
		return crowdedFeeRule;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
}
