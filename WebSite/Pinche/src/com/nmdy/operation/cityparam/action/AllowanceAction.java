package com.nmdy.operation.cityparam.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.mysql.jdbc.MysqlDataTruncation;
import com.nmdy.operation.cityparam.service.CityParamService;
import com.nmdy.operation.cityparam.dao.AllowanceRule;
import com.opensymphony.xwork2.ModelDriven;

public class AllowanceAction implements ModelDriven<AllowanceRule>{
	JSONObject resultObject;
	AllowanceRule allowanceRule = new AllowanceRule();
	
	@Resource
	CityParamService cityParamService;
	
	public String index(){
		return "success";
	}
	
	public String edit(){
		int ret = 0;
		String vret = validate(allowanceRule);
		if(null != vret){
			resultObject = new JSONObject();
			resultObject.put("count", 0);
			resultObject.put("msg", vret);
			return "success";
		}
		
		try{
		if(0 != allowanceRule.getId()){
			ret = cityParamService.editAllowanceRule(allowanceRule);
		}else{
			ret = cityParamService.addAllowanceRule(allowanceRule);
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
		resultObject.put("id", allowanceRule.getId());
		return "success";
	}
	
	public String load(){
		if(!StringUtils.isBlank(allowanceRule.getCityCode())){
			List ret = cityParamService.loadAllowanceByCity(allowanceRule.getCityCode());
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
		if(allowanceRule.getId() > 0){
		   int ret = cityParamService.removeAllowance(allowanceRule.getId());
			resultObject = new JSONObject();
			resultObject.put("count", ret);
			resultObject.put("id", allowanceRule.getId());
		}
		
		return "success";
	}
	
	private String validate(AllowanceRule allowanceRule){
		if(!allowanceRule.getBeginTime().matches("\\d{2}:\\d{2}:\\d{2}")){
			return "开始时间格式不对:" + allowanceRule.getBeginTime();
		}
		
		if(!allowanceRule.getEndTime().matches("\\d{2}:\\d{2}:\\d{2}")){
			return "结束时间格式不对:" + allowanceRule.getEndTime();
		}
		
		String[] ary1 = allowanceRule.getBeginTime().split(":");
		String[] ary2 = allowanceRule.getEndTime().split(":");
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
	public AllowanceRule getModel() {
		return allowanceRule;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
}
