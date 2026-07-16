package com.pinche.spread.loan.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.pinche.common.Common;
import com.pinche.common.Constants;
import com.pinche.common.SerialUtil;
import com.pinche.common.action.SecureAction;
import com.pinche.spread.loan.dao.Activities;
import com.pinche.spread.loan.dao.City;
import com.pinche.spread.loan.dao.Syscoupon;
import com.pinche.spread.loan.service.LoanService;

public class LoanspreadActivityAction extends SecureAction {

private static final long serialVersionUID = 1L;
	
	private LoanService loanService;
	
	private JSONObject resultObj = null;
	
	//pagination
	private Integer page;
	private Integer rows;
	private Integer total_size;
	
	private List<Activities> activities;
	private List<Syscoupon> syscoupons;
	
	//search param for activities of activity tab
	private String activityCode;
	private int price=0;
	private String name;
	private int city;
	
	//search param for syscoupons of activity tab
	private String couponCode;
	private Integer point;
	private String validPeriod;
	private int coupontype;
	//insert param for activity creating
	private String activeCode;
	private int limitCount=0;
	private int syscouponId;
	
	// stop activity param
	private String stopActivityCode;

	/*
	 * 通过条件(活动代码和点数) 进行 首页面 的 查询
	 */
	public String search4activity() {
		try {
			activities = loanService.search4activity(activityCode,price);
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			map.put("activityCode", "%" + activityCode + "%");
			map.put("price", price);
			total_size = activities.size();
			activities = loanService.findPagination4activity(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		makeJSONObjFromActivities();

		return SUCCESS;
	}
	/*
	 * search4activity()的 子方法
	 * 将查询到的数据转换 为 JsonObject 格式
	 */
	//convert Syscoupon list to JSON object 
	private void makeJSONObjFromActivities() {
		
		if (activities == null) {
			resultObj = null;
			return;
		}

		try {
			ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
			for (Activities info : activities) {
	
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("code", info.getActiveCode());
				m.put("couponid", info.getSyscouponCode());
				m.put("price", info.getPoint());
				m.put("isenabled", info.getIsenabled());
				m.put("limit_count", info.getLimitCount());
				m.put("deploy_count", info.getDeployCount());
				if(info.getCouponType() == Constants.SyscouponTable.CouponType.NONE) {
					m.put("coupontype", "无");
				}
				else if(info.getCouponType() == Constants.SyscouponTable.CouponType.OVER) {
					m.put("coupontype", "满" + info.getLimit_val() + "点使用1张");
				}
				else if(info.getCouponType() == Constants.SyscouponTable.CouponType.NO_WITH_OTHER) {
					m.put("coupontype", "不与其他点券共享");
				}
				else if(info.getCouponType() == Constants.SyscouponTable.CouponType.ONLY_ONE) {
					m.put("coupontype", "每张订单使用1张");
				}
				if(info.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.DAY) {
					m.put("validperiod", info.getValid_period() + "天");
				}
				else if(info.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.WEEK) {
					m.put("validperiod", info.getValid_period() + "周");
				}
				else if(info.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.WEEK) {
					m.put("validperiod", info.getValid_period() + "月");
				}
				else if(info.getValid_period_unit() == Constants.SyscouponTable.ValidPeriodUnit.WEEK) {
					m.put("validperiod", info.getValid_period() + "年");
				}
	
				m.put("operator", "");
				m.put("date", info.getAtDate());
				
				al.add(m);
			}
	
			Map<String, Object> json = new HashMap<String, Object>();
			json.put("total", total_size);
			json.put("rows", al);
			resultObj = JSONObject.fromObject(json);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 /*
	  * 根据城市查询数据显示的页面上
	  * 城市为空时查询的是全局数据
	  */
	public String findCityParam(){
		System.out.println("city="+city+"  name="+name);
		 City cityParam=null;
		if(city==2){
		 if(!name.equals("")){
			 
		 cityParam = loanService.findCityParam(name);
	
     }else{
			name="";
			  cityParam = loanService.findglobal(); 
			 }
			}else{
				name="";
			  cityParam = loanService.findglobal();
			}
		 resultObj = JSONObject.fromObject(cityParam);
		return "success";
	}

/*	public String search4Coupon() {
		try {
			syscoupons = loanService.search4autoSend(couponCode, point, validPeriod);
				
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			map.put("couponCode", "%" + couponCode + "%");
			map.put("price", point);
			map.put("validPeriod", validPeriod);
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
	}*/
	
	/*
	 * 自动添加点券 页面的 新建活动的 跳转页面
	 */
	public String createActivity() {
		
		activityCode = generateActivityCode();
		ActionContext.getContext().put("activityCode", activityCode);
		return SUCCESS;
	}
	
	/*
	 * 新建活动中的 将新建的数据保存的方法
	 */
	public String doCreateActivity() {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		Map<String, Object> json = new HashMap<String, Object>();
		
		try {
			Activities activity = new Activities();
			activity.setActiveCode(activeCode);
			activity.setLimitCount(limitCount);
			activity.setSyscouponId(syscouponId);
			activity.setAtDate(Common.getCurrentDateTimeString());
			activity.setIsenabled(Constants.ActivitiesTable.Enabled.ENABLED);
			activity.setDeleted(Constants.ActivitiesTable.DELETED.NO);
			
			loanService.insertActivity(activity);
				
			json.put("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	/*
	 * 点券推广中的 新建自助添加点券活动 页面 更换活动代码的 方法
 	 */
	public String genActivityCode() {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		Map<String, Object> json = new HashMap<String, Object>();
		
		activityCode = generateActivityCode();

		json.put("result", "success");
		json.put("code", activityCode);
		
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	/*
	 * 自助添加点券页面 停止活动的方法
	 */
	public String stopActivity() {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setContentType("application/json");
		Map<String, Object> json = new HashMap<String, Object>();
		
		try {
			Activities activity = loanService.findOneActivityByCode(stopActivityCode);
			
			if(activity == null) {
				json.put("result", "error");
			}
			else {
				activity.setIsenabled(Constants.ActivitiesTable.Enabled.DISABLED);
				
				loanService.updateActivity(activity);
				
				json.put("result", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}

	/*
	 * genActivityCode() 和 createActivity() 的子方法
	 *  获取活动代码的方法
	 */
	private String generateActivityCode() {
		String tCode = "";
		try {
			Activities t = new Activities();
			
			while(t != null) {
				
				tCode = SerialUtil.generateSerialNo();
				
				t = loanService.findOneActivityByCode(tCode);
			}
			return tCode;	
			
		} catch (Exception e) {
			e.printStackTrace();
			return tCode;
		}
	}
	
	
	//convert Syscoupon list to JSON object 
/*	private void makeJSONObjFromSyscoupons() {

		if (syscoupons == null) {
			resultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Syscoupon info : syscoupons) {

			Map<String, Object> m = new HashMap<String, Object>();

			String url = String.format("<a href='javascript:selectCoupon(%d, \"%s\");'>选择</a>", info.getId(), info.getSyscoupon_code());

			m.put("operation", url);
			
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
	}*/
	
	/*
	 * -------------------getter 和 setter 方法---------------------
	 */
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
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public int getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}

	public int getSyscouponId() {
		return syscouponId;
	}

	public void setSyscouponId(int syscouponId) {
		this.syscouponId = syscouponId;
	}

	public String getStopActivityCode() {
		return stopActivityCode;
	}

	public void setStopActivityCode(String stopActivityCode) {
		this.stopActivityCode = stopActivityCode;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}
	public int getCoupontype() {
		return coupontype;
	}
	public void setCoupontype(int coupontype) {
		this.coupontype = coupontype;
	}

	
	
}
