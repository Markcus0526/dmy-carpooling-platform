package com.pinche.spread.loan.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.pinche.common.action.SecureAction;
import com.pinche.spread.loan.dao.City;
import com.pinche.spread.loan.dao.GlobalParams;
import com.pinche.spread.loan.dao.Syscoupon;
import com.pinche.spread.loan.service.LoanService;

public class LoanspreadAutoSendAction extends SecureAction {

	private static final long serialVersionUID = 1L;
	
	private LoanService loanService;
	
	private JSONObject resultObj = null;
	
	//pagination
	private Integer page;
	private Integer rows;
	private Integer total_size;
	
	//search param for syscoupon of autosend tab
	private String couponCode;
	private Integer point;
	private String validPeriod;
	
	// search param for autosend params of autosend tab
	private int globalOrCity;
	private String city;
	private GlobalParams tmpParam;
	
	private String registerSyscouponId;
	private String verifiedSyscouponId;
	private String finishordersSyscouponId;
	private String registermonthSyscouponId;
	private int numOrderFinished;
	private int numRegistermonth;
	private List<Syscoupon> syscoupons;
	
	/*
	 * 点券推广 中的   自动发放点券页面数据的查询显示  
	 *  	和  
	 * 自动添加点券页面中的新建活动页面的 的查询显示
	 */
	public String search4autoSend() {
		try {
			syscoupons = loanService.search4autoSend(couponCode,point,validPeriod);
				
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			map.put("couponCode", "%" + couponCode + "%");
			map.put("point", point);
			if(validPeriod!=null && !"".equals(validPeriod)){
			int validPeriod1 = Integer.parseInt(validPeriod.substring(0,validPeriod.length()-1));
				String class1=validPeriod.substring(validPeriod.length()-1);
			map.put("validPeriod", validPeriod1);
			int valid_period_unit=0;
			if(class1.equals("日")){
				valid_period_unit=1;
			}if(class1.equals("周")){
				valid_period_unit=2;
			}if(class1.equals("月")){
				valid_period_unit=3;
			}if(class1.equals("年")){
				valid_period_unit=4;
			}	
			map.put("valid_period_unit", valid_period_unit);
			}else{
				map.put("validPeriod", validPeriod);
				map.put("valid_period_unit", 0);
			}
			total_size = syscoupons.size();
			syscoupons = loanService.findPagination4autoSend(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		makeJSONObjFromSyscoupons();

		return SUCCESS;
	}
	/*
	 * search4autoSend() 的子方法
	 */
	//convert Syscoupon list to JSON object 
	private void makeJSONObjFromSyscoupons() {

		if (syscoupons == null) {
			resultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Syscoupon info : syscoupons) {

			Map<String, Object> m = new HashMap<String, Object>();

		//	String url = String.format("", info.getId(), info.getSyscoupon_code());
			m.put("id", info.getId());
			m.put("syscouponCode", info.getSyscoupon_code());
			
			m.put("couponid", info.getSyscoupon_code());
			
			m.put("price", info.getSum());
			
			if(info.getCoupon_type() == 0)
				m.put("coupontype", "无");
			else if(info.getCoupon_type() == 1)
				m.put("coupontype", "满" + info.getLimit_val() + "点使用1张");
			else if(info.getCoupon_type() == 2)
				m.put("coupontype", "不与其他点券共享");
			else if(info.getCoupon_type() == 3)
				m.put("coupontype", "每张订单使用1张");
			else
				m.put("coupontype", "unknown");
			
			String unit = "";
			if(info.getValid_period_unit() == 1)
				unit = "天";
			else if(info.getValid_period_unit() == 2)
				unit = "周";
			else if(info.getValid_period_unit() == 3)
				unit = "月";
			else if(info.getValid_period_unit() == 4)
				unit = "年";
			m.put("validperiod", info.getValid_period() + unit);
			m.put("useCondition", info.getUseCondition());
			
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}
	
	/*
	 * 点券推广 中的   自动发放点券页面数据的保存
	 */
	public String saveParam() {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		Map<String, Object> json = new HashMap<String, Object>();
	//	GlobalParams gParams = loanService.getGlobalParam();
		Map<String,Object> params=new HashMap<String,Object>();
		try {
	if("全局".equals(city)){	// global param
				params.put("registerSyscouponId", registerSyscouponId);
				params.put("verifiedSyscouponId", verifiedSyscouponId);
				params.put("numOrderFinished", numOrderFinished);
				params.put("finishordersSyscouponId", finishordersSyscouponId);
				params.put("numRegistermonth", numRegistermonth);
				params.put("registermonthSyscouponId", registermonthSyscouponId);
				loanService.updateGlobalParam(params);
				json.put("result", "success");
			}else {
					params.put("city", city);
				params.put("registerSyscouponId",registerSyscouponId);
				params.put("verifiedSyscouponId",verifiedSyscouponId);
				params.put("numOrderFinished",numOrderFinished);
				params.put("finishordersSyscouponId",finishordersSyscouponId);
				params.put("numRegistermonth",numRegistermonth);
				params.put("registermonthSyscouponId",registermonthSyscouponId);
						
						loanService.updateCity(params);
						json.put("result", "success");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		resultObj = JSONObject.fromObject(json);

		return SUCCESS;
	}
	

	
	public LoanService getLoanService() {
		return loanService;
	}
	public void setLoanService(LoanService loanService) {
		this.loanService = loanService;
	}
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getTotal_size() {
		return total_size;
	}
	public void setTotal_size(Integer total_size) {
		this.total_size = total_size;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}



	public String getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(String validPeriod) {
		this.validPeriod = validPeriod;
	}

	public int getGlobalOrCity() {
		return globalOrCity;
	}

	public void setGlobalOrCity(int globalOrCity) {
		this.globalOrCity = globalOrCity;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public GlobalParams getTmpParam() {
		return tmpParam;
	}

	public void setTmpParam(GlobalParams tmpParam) {
		this.tmpParam = tmpParam;
	}

	public String getRegisterSyscouponId() {
		return registerSyscouponId;
	}

	public void setRegisterSyscouponId(String registerSyscouponId) {
		this.registerSyscouponId = registerSyscouponId;
	}

	public String getVerifiedSyscouponId() {
		return verifiedSyscouponId;
	}

	public void setVerifiedSyscouponId(String verifiedSyscouponId) {
		this.verifiedSyscouponId = verifiedSyscouponId;
	}

	public String getFinishordersSyscouponId() {
		return finishordersSyscouponId;
	}

	public void setFinishordersSyscouponId(String finishordersSyscouponId) {
		this.finishordersSyscouponId = finishordersSyscouponId;
	}

	public String getRegistermonthSyscouponId() {
		return registermonthSyscouponId;
	}

	public void setRegistermonthSyscouponId(String registermonthSyscouponId) {
		this.registermonthSyscouponId = registermonthSyscouponId;
	}

	public int getNumOrderFinished() {
		return numOrderFinished;
	}

	public void setNumOrderFinished(int numOrderFinished) {
		this.numOrderFinished = numOrderFinished;
	}

	public int getNumRegistermonth() {
		return numRegistermonth;
	}

	public void setNumRegistermonth(int numRegistermonth) {
		this.numRegistermonth = numRegistermonth;
	}
	
}
