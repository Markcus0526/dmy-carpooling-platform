package com.pinche.spread.number.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.Errors;
import com.pinche.common.WebUtil;
import com.pinche.spread.number.dao.Profitconfig;
import com.pinche.spread.number.service.ProfitconfigService;

public class NumberspreadAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProfitconfigService profitconfigService;
	//private ProfitconfigService customerService;

	private List<Profitconfig> userList = null;
	private Profitconfig userInfo = null;
	private String searchColumn;
	
	private String module_path = "spread/number/";

	public String index() {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		request.setAttribute("menucode", "2");
		request.setAttribute("submenucode", "1");
		request.setAttribute("content_url", module_path + "getListAction");

		return SUCCESS;
	}
	
	public String getListAction() {
		try {
			userList = profitconfigService.findAll();
		} catch (Exception e) {
			return ERROR;
		}

		return SUCCESS;
	}
	
	public List<Profitconfig> getUserList() {
		return userList;
	}

	public Profitconfig getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Profitconfig userInfo) {
		this.userInfo = userInfo;
	}

	public String getSearchColumn() {
		return searchColumn;
	}

	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getModule_path() {
		return module_path;
	}

	public void setModule_path(String module_path) {
		this.module_path = module_path;
	}
	//config params
	private int id;
	private float profit_passenger;
	private int active_passenger;
	private int limit_passenger;
	private int count_passenger;

	private float profit_driver;
	private int active_driver;
	private int limit_driver;
	private int count_driver;
	
	//search paramters
	private int searchType;
	private String searchValue;
	
	//pagination parameters
	private int rows;
	private int page;
	private int total_size;
	private String method;


	//result params
	private List<Profitconfig> customerlist;
	private JSONObject resultObj;
	
	public void setId(int id) {
		this.id = id;
	}
	

	
	public float getProfit_passenger() {
		return profit_passenger;
	}
	
	public void setProfit_passenger(float profit_passenger) {
		this.profit_passenger= profit_passenger;
	}

	public int getActive_passenger() {
		return active_passenger;
	}
	
	public void setActive_passenger(int active_passenger) {
		this.active_passenger = active_passenger;
	}

	public int getLimit_passenger() {
		return limit_passenger;
	}
	
	public void setLimit_passenger(int limit_passenger) {
		this.limit_passenger = limit_passenger;
	}

	public int getCount_passenger() {
		return count_passenger;
	}
	
	public void setCount_passenger(int count_passenger) {
		this.count_passenger = count_passenger;
	}

	public float getProfit_driver() {
		return profit_driver;
	}
	
	public void setProfit_driver(float profit_driver) {
		this.profit_driver = profit_driver;
	}

	public int getActive_driver() {
		return active_driver;
	}
	
	public void setActive_driver(int active_driver) {
		this.active_driver = active_driver;
	}

	public int getLimit_driver() {
		return limit_driver;
	}
	
	public void setLimit_driver(int limit_driver) {
		this.limit_driver = limit_driver;
	}

	public int getCount_driver() {
		return count_driver;
	}
	
	public void setCount_driver(int count_driver) {
		this.count_driver = count_driver;
	}

	public void setTotal_size(int total_size) {
		this.total_size = total_size;
	}
	
	public int getTotal_size() {
		return this.total_size;
	}
	
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	
	public int getSearchType() {
		return this.searchType;
	}
	
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public String getSearchValue(String searchValue) {
		return this.searchValue;
	}
	
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	public JSONObject getResultObj() {
		return this.resultObj;
	}
	
	public void setCustomerlist(List<Profitconfig> customerlist) {
		this.customerlist = customerlist;
	}
	
	public List<Profitconfig> getCustomerlist() {
		return this.customerlist;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPage() {
		return this.page;
	}
	
/*	public void setCustomerService(ProfitconfigService customerService) {
		this.customerService = customerService;
	}
	
	public ProfitconfigService getCustomerService() {
		return this.customerService;
	}*/
	
	public void setProfitconfigService(ProfitconfigService profitconfigService) {
		this.profitconfigService = profitconfigService;
	}
	
	public ProfitconfigService getProfitconfigService() {
		return this.profitconfigService;
	}
	
	public String search() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("searchType", searchType);
			map.put("searchValue", "%" + searchValue + "%");
			customerlist = profitconfigService.searchNumberspread(map);
			total_size = customerlist.size();
			
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
			customerlist = profitconfigService.searchNumberspread(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		WebUtil._setResponseContentType("application/json");
		makeJSONObjFromList();
		return SUCCESS;
	}
	
	public void makeJSONObjFromList() {
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Profitconfig info : customerlist) {
			Map<String, Object> m = new HashMap<String, Object>();
			
			String url = String.format("<a href=\"javascript:detail(%d)\">查看</a>|", info.getId());
			url += String.format("<a href=\"javascript:showConfig(%d)\">设置</a>", info.getId());

			m.put("operation", url);
			m.put("userid", info.getId());
			m.put("username", info.getUsername());
			m.put("invitecode", info.getInvitecode_self());
			
			if (info.getActiveway_as_passenger() == 1)
				m.put("profit_passenger", info.getRatio_as_passenger());
			else
				m.put("profit_passenger", info.getInteger_as_passenger());
			
			m.put("limit_passenger", info.getProvide_profitsharing_time_as_passenger());
			m.put("count_passenger", info.getProvide_profitsharing_count_as_passenger());

			if (info.getActiveway_as_driver() == 1)
				m.put("profit_driver", info.getRatio_as_driver());
			else
				m.put("profit_driver", info.getInteger_as_driver());
			
			m.put("limit_driver", info.getProvide_profitsharing_time_as_driver());
			m.put("count_driver", info.getProvide_profitsharing_count_as_driver());

			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		
		try {
			resultObj = JSONObject.fromObject(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String genInfo() {
		WebUtil._setResponseContentType("application/json");
		
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		ArrayList<Map<String, Object>> infos = new ArrayList<Map<String, Object>>();
		Profitconfig info = profitconfigService.findOneById(Integer.valueOf(id));

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", info.getId());
		m.put("usercode", info.getUsercode() == null ? "" : info.getUsercode());
		m.put("username", info.getUsername() == null ? "" : info.getUsername());
		m.put("nickname", info.getNickname() == null ? "" : info.getNickname());
		m.put("phone", info.getPhone() == null ? "" : info.getPhone());
		m.put("sex", info.getSex());

		infos.add(m);
		System.out.print("infos   "+info);
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		json.put("infos", infos);
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	public String view() {
		return SUCCESS;
	}
	
	public String getConfig() {
		WebUtil._setResponseContentType("application/json");
		
		int err = Errors.ERR_OK;
		ArrayList<Map<String, Object>> infos = new ArrayList<Map<String, Object>>();
		Map<String, Object> m = new HashMap<String, Object>();
		
		if (id > 0) {
			Profitconfig info = profitconfigService.findOneById(Integer.valueOf(id));

			m.put("active_passenger", info.getActiveway_as_passenger());
			if (info.getActiveway_as_passenger() == 1) {
				m.put("profit_passenger", info.getRatio_as_passenger());
			} else {
				m.put("profit_passenger", info.getInteger_as_passenger());
			}
			m.put("limit_passenger", info.getProvide_profitsharing_time_as_passenger());
			m.put("count_passenger", info.getProvide_profitsharing_count_as_passenger());

			m.put("active_driver", info.getActiveway_as_driver());
			if (info.getActiveway_as_driver() == 1) {
				m.put("profit_driver", info.getRatio_as_driver());
			} else {
				m.put("profit_driver", info.getInteger_as_driver());
			}
			m.put("limit_driver", info.getProvide_profitsharing_time_as_driver());
			m.put("count_driver", info.getProvide_profitsharing_count_as_driver());
		} else {
			Profitconfig info = profitconfigService.findLast();

			m.put("active_passenger", info.getActive_as_passenger());
			if (info.getActive_as_passenger() == 1) {
				m.put("profit_passenger", info.getRatio_as_passenger());
			} else {
				m.put("profit_passenger", info.getInteger_as_passenger());
			}
			m.put("limit_passenger", info.getLimit_month_as_passenger());
			m.put("count_passenger", info.getLimit_count_as_passenger());

			m.put("active_driver", info.getActive_as_driver());
			if (info.getActive_as_driver() == 1) {
				m.put("profit_driver", info.getRatio_as_driver());
			} else {
				m.put("profit_driver", info.getInteger_as_driver());
			}
			m.put("limit_driver", info.getLimit_month_as_driver());
			m.put("count_driver", info.getLimit_count_as_driver());
		}
		
		infos.add(m);
		
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		json.put("infos", infos);
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	public String config() {
		try {
			int affectedRows = 0;
			if (id > 0) {
				Profitconfig info = profitconfigService.findOneById(Integer.valueOf(id));
	
				info.setActiveway_as_passenger(active_passenger);
				if (active_passenger == 1) {
					info.setRatio_as_passenger(profit_passenger);
				} else {
					info.setInteger_as_passenger(profit_passenger);
				}
				info.setProvide_profitsharing_time_as_passenger(limit_passenger);
				info.setProvide_profitsharing_count_as_passenger(count_passenger);
	
				info.setActiveway_as_driver(active_driver);
				if (active_driver == 1) {
					info.setRatio_as_driver(profit_driver);
				} else {
					info.setInteger_as_driver(profit_driver);
				}
				info.setProvide_profitsharing_time_as_driver(limit_driver);
				info.setProvide_profitsharing_count_as_driver(count_driver);
				affectedRows = profitconfigService.updateNumberspread(info);
			} else {
				Profitconfig info = profitconfigService.findLast();
	
				info.setActive_as_passenger(active_passenger);
				if (active_passenger == 1) {
					info.setRatio_as_passenger(profit_passenger);
				} else {
					info.setInteger_as_passenger(profit_passenger);
				}
				info.setLimit_month_as_passenger(limit_passenger);
				info.setLimit_count_as_passenger(count_passenger);
	
				info.setActive_as_driver(active_driver);
				if (active_driver == 1) {
					info.setRatio_as_driver(profit_driver);
				} else {
					info.setInteger_as_driver(profit_driver);
				}
				info.setLimit_month_as_driver(limit_driver);
				info.setLimit_count_as_driver(count_driver);
				affectedRows = profitconfigService.update(info);
			}
			if (affectedRows > 0)
				return SUCCESS;
			else
				return ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String open() {
		String result = SUCCESS;
		switch (method) {
		case "config":
			result = "config_page";
			break;
		case "view":
			result = "view_page";
			break;
		}
		
		return result;
	}
}
