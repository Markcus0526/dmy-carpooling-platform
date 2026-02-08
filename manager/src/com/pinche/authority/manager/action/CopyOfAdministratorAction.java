package com.pinche.authority.manager.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.nmdy.customerManage.appSpreadUnit.service.App_Spread_UnitService;
import com.nmdy.customerManage.group.dao.GroupNew;
import com.nmdy.customerManage.group.service.GroupNewService;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.authority.manager.dao.Administrator;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.common.Constants;
import com.pinche.common.Errors;
import com.pinche.common.WebUtil;

public class CopyOfAdministratorAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private AdministratorService administratorService;
	private GroupNewService groupNewService;
	private App_Spread_UnitService app_Spread_UnitService;
	
	private Administrator userInfo = null;
	private List<Administrator> userInfoList = null;

	private String searchType;
	private String searchValue;
	private String oldPassword;
	private String newPassword;

	private String id;
	private String method;
	
	private Integer page;
	private Integer rows;
	private Integer rowLen;
	private JSONObject resultObj = null;

	public CopyOfAdministratorAction() {

	}
	
	private void makeJSONObjFromList() {

		if (userInfoList == null) {
			resultObj = null;
			return;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Administrator info : userInfoList) {

			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", info.getId());
			m.put("usercode", info.getUsercode());
			m.put("username", info.getUsername());
			
			String unitName = info.getGroupname()!=null?info.getGroupname():info.getAppUnitName();
			if (info.getUnitcode().isEmpty())
				unitName = "平台";
				
			m.put("unit", unitName);
			m.put("phoneNum", info.getPhoneNum());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", rowLen);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}

	public String search() {
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("searchType:"+searchType+" searchValue:"+searchValue);
		try {
			if(searchType==null||searchType.trim().equals("")){
				searchType="0";
			}
			switch (Integer.parseInt(searchType)) {
			case Constants.SEARCH_BY_USERCODE:
				map.put("usercode",  searchValue );
				break;
			case Constants.SEARCH_BY_USERNAME:
				map.put("username", "%" + searchValue + "%");
				break;
			case Constants.SEARCH_BY_PHONENUM:
				map.put("phone", "%" + searchValue + "%");
				break;
			case Constants.SEARCH_BY_ID:
				map.put("id", searchValue);
				break;
			case Constants.SEARCH_BY_UNIT:
				map.put("unit", searchValue);
				break;
			}
			
			map.put("page", (page-1)*rows>=0?(page-1)*rows:0);
			map.put("rows", rows);
			map.put("isTotal", 0);
			rowLen = administratorService.search(map).size();
			map.put("isTotal", 1);
			userInfoList = administratorService.search(map);
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = null;
			return ERROR;
		}
		
		WebUtil._setResponseContentType("application/json");
		makeJSONObjFromList();

		return SUCCESS;
	}

	public String index() {
		return SUCCESS;
	}

	public String add() {
//		if (!checkAuthority(module_path+"add"))
//			return "logout";

		// for adding an administrator

		WebUtil._setResponseContentType("application/json");
		
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		
		try {
			Administrator regUser = administratorService.findOneByUsercode(userInfo.getUsercode());
			if (regUser == null) {
				userInfo.setLevel(0);
				int affectedRows = administratorService.addAdministrator(userInfo);
				if (affectedRows == 0) {
					err = Errors.ERR_SQL_ERROR;				
				}
				//logAdminOper(Constants.TB_NAME_ADMIN_USER, "Added user(usercode=" + userInfo.getUsercode() + "). Successfully");
			} else {
				err = Errors.ERR_INVALID_USERCODE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
			//logAdminOper(Constants.TB_NAME_ADMIN_USER, "Added user(usercode=" + userInfo.getUsercode() + "). Fail");
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}

	public String delete() {

		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		
		try {
			int affectedRows = administratorService.deleteAdministrator(Integer.valueOf(id));

			if (affectedRows == 0)
				err = Errors.ERR_SQL_ERROR;
			
			// logging
			//logAdminOper(Constants.TB_NAME_ADMIN_USER, Integer.valueOf(id), "Success");
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
			//logAdminOper(Constants.TB_NAME_ADMIN_USER, Integer.valueOf(id), "Fail: " + e.toString());
		}

		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}

	public String batchDelete() {
//		if (!checkAuthority(module_path + "delete"))
//			return "logout";

		String ids[] = WebUtil._getRequestParameterMap("ids")[0].toString().split(",");

		WebUtil._setResponseContentType("application/json");

		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		
		for (String id : ids) {
			try {
				int affectedRows = administratorService.deleteAdministrator(Integer.valueOf(id));

				if (affectedRows == 0)
					err = Errors.ERR_SQL_ERROR;
				
				//logAdminOper(Constants.TB_NAME_ADMIN_USER, Integer.valueOf(id), "Success");
				
			} catch (Exception e) {
				e.printStackTrace();
				err = Errors.ERR_SQL_ERROR;
				
				//logAdminOper(Constants.TB_NAME_ADMIN_USER, Integer.valueOf(id), "Fail: " + e.toString());
			}
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	public String password() {

		if (method.equals("get"))
			return "forward";

		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		
		try {
			userInfo = administratorService.findOneById(Integer.valueOf(id));
			if (userInfo != null) {
				
				if (method.equals("set")) {
					userInfo.setPassword(newPassword);
					int affectedRows = administratorService.changePassword(userInfo);
					if (affectedRows == 0) {
						err = Errors.ERR_SQL_ERROR;
					}
					
				} else if (method.equals("change")) {
					
					userInfo.setPassword(newPassword);
					if (oldPassword.equals(userInfo.getPassword())) {
						int affectedRows = administratorService.changePassword(userInfo);
						if (affectedRows == 0) {
							err = Errors.ERR_SQL_ERROR;
						}
					} else {
						err = Errors.ERR_INVALID_PASSWORD;
					}	
				}
			} else {
				err = Errors.ERR_UNKNOWN;
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}

		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObj = JSONObject.fromObject(json);
		return SUCCESS;
	}

	public String edit_info() {
		
		WebUtil._setResponseContentType("application/json");
		
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		ArrayList<Map<String, Object>> infos = new ArrayList<Map<String, Object>>(); 
		if (method.equals("get") || method.equals("view")) {
			
			try {
				userInfo = administratorService.findOneById(Integer.valueOf(id));
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", userInfo.getId());
				m.put("usercode", userInfo.getUsercode()==null?"":userInfo.getUsercode());
				m.put("username", userInfo.getUsername()==null?"":userInfo.getUsername());
				m.put("unit", userInfo.getUnitcode());
				m.put("phoneNum", userInfo.getPhoneNum()==null?"":userInfo.getPhoneNum());
				m.put("phoneNum2", userInfo.getPhoneNum2()==null?"":userInfo.getPhoneNum2());
				m.put("qq", userInfo.getQq()==null?"":userInfo.getQq());
				m.put("note", userInfo.getNote()==null?"":userInfo.getNote());
				m.put("email", userInfo.getEmail()==null?"":userInfo.getEmail());
				m.put("sex", userInfo.getSex());
				infos.add(m);
			} catch (Exception e) {
				e.printStackTrace();
				err = Errors.ERR_SQL_ERROR;
			}
		} else if (method.equals("save")) {
//			if (!checkAuthority(module_path+"edit"))
//				return "logout";

			try {
				int affectedRows = administratorService.editAdministrator(userInfo);
				if (affectedRows == 0) {
					err = Errors.ERR_SQL_ERROR;
				}
			} catch (Exception e) {
				e.printStackTrace();
				err = Errors.ERR_SQL_ERROR;
			}
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		json.put("infos", infos);
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	public String edit(){
		userInfo.setId(Integer.parseInt(id));
		System.out.println("userInfo:"+userInfo.getUsercode()+"userName:"+userInfo.getUsername()+" phoneNum:"+userInfo.getPhoneNum()+" userInfo.id"+userInfo.getId());
		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		try {
			int affectedRows = administratorService.editAdministrator(userInfo);
			if (affectedRows == 0) {
				err = Errors.ERR_SQL_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
	
	public String open() {
		String result = SUCCESS;
		switch (method) {
		case "add":
			result = "add_page";
			break;
		case "edit":
			result = "edit_page";
			break;
		case "view":
			result = "view_page";
			break;
		case "setrole":
			result = "setrole_page";
			break;
		}
		
		return result;
	}
	
	public String getUnitList() {

		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> pingtai = new HashMap<String, Object>();
		pingtai.put("code", "0");
		pingtai.put("name", "平台");
		unitList.add(pingtai);
		
		try{
/*			List<GroupNew> groups = this.groupNewService.findAll();
			if (groups.size() > 0) {
				for (GroupNew group : groups) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("code", group.getGroupid());
					m.put("name", group.getGroup_name() + "(集团)");
					unitList.add(m);
				}
			}*/
			
			//modify by czq 2014-09-15
			List<Map<String, Object>> groups = this.groupNewService.findAll();
			for (int i = 0; i < groups.size(); i++) {
				
				
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("code", groups.get(i).get("groupid"));
				m.put("name", groups.get(i).get("group_name") + "(集团)");
				unitList.add(m);
			}
			//modify by czq 2014-09-15
			
			//因为合作单位不会登陆到系统后台，所以下面取合作单位的代码可以注释掉。
			/**
			List<AppSpreadUnit> joinunits = this.app_Spread_UnitService.findAll();
			if (joinunits.size() > 0) {
				for (AppSpreadUnit unit : joinunits) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("code", unit.getId());
					m.put("name", unit.getName() + "(合作)");
					unitList.add(m);
				}
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		json.put("unitList", unitList);
		resultObj = JSONObject.fromObject(json);
		System.out.println("resultObj:"+resultObj.toString());
		WebUtil._setResponseContentType("application/json");

		return SUCCESS;
	}
	
	public String exportExcel() {
		return SUCCESS;
	}

	public Administrator getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Administrator userInfo) {
		this.userInfo = userInfo;
	}

	public List<Administrator> getUserInfoList() {
		return userInfoList;
	}



	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	/*
	 * public Map<Integer, String> getSearchOptions() { return
	 * Constants.getSearchOptions(Constants.MANAGER_SEARCH_PAGE); }
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public AdministratorService getAdministratorService() {
		return administratorService;
	}

	public void setAdministratorService(
			AdministratorService administratorService) {
		this.administratorService = administratorService;
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

	public GroupNewService getGroupNewService() {
		return groupNewService;
	}

	public void setGroupNewService(GroupNewService groupNewService) {
		this.groupNewService = groupNewService;
	}

	public App_Spread_UnitService getApp_Spread_UnitService() {
		return app_Spread_UnitService;
	}

	public void setApp_Spread_UnitService(
			App_Spread_UnitService app_Spread_UnitService) {
		this.app_Spread_UnitService = app_Spread_UnitService;
	}

	/**
	 * 用户管理->查看
	 * @return
	 */
	public String viewAdministrator() {

		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		ArrayList<Map<String, Object>> infos = new ArrayList<Map<String, Object>>(); 
		
		try {
			userInfo = administratorService.findOneById(Integer.valueOf(id));
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("id", userInfo.getId());
			m.put("usercode", userInfo.getUsercode()==null?"":userInfo.getUsercode());
			m.put("username", userInfo.getUsername()==null?"":userInfo.getUsername());
			m.put("unit", userInfo.getUnitcode());
			m.put("phoneNum", userInfo.getPhoneNum()==null?"":userInfo.getPhoneNum());
			m.put("phoneNum2", userInfo.getPhoneNum2()==null?"":userInfo.getPhoneNum2());
			m.put("qq", userInfo.getQq()==null?"":userInfo.getQq());
			m.put("note", userInfo.getNote()==null?"":userInfo.getNote());
			m.put("email", userInfo.getEmail()==null?"":userInfo.getEmail());
			m.put("sex", userInfo.getSex());
			infos.add(m);
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}
		
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		json.put("infos", infos);
		resultObj = JSONObject.fromObject(json);
		
		return SUCCESS;
	}
}
