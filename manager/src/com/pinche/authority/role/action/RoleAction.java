package com.pinche.authority.role.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.nmdy.entity.DataJuese;
import com.nmdy.entity.Juese;
import com.nmdy.sysManage.role.service.DataJueseService;
import com.nmdy.sysManage.role.service.JueseService;
import com.pinche.authority.manager.service.AdministratorService;
import com.pinche.authority.role.dao.Role;
import com.pinche.authority.role.dao.Roleitem;
import com.pinche.authority.role.service.RoleService;
import com.pinche.authority.role.service.RoleitemService;
import com.pinche.common.Common;
import com.pinche.common.Constants;
import com.pinche.common.Errors;
import com.pinche.common.WebUtil;
import com.pinche.common.action.SecureAction;

public class RoleAction extends SecureAction{
	
	private static final long serialVersionUID = 1L;
	
	private RoleService roleService;
	private RoleitemService roleitemService;
	private AdministratorService administratorService;
	private JueseService jueseService;
	private DataJueseService dataJueseService;
	private List<Juese> listJuese;
	private List<DataJuese> listDataJuese;
	private Role role = null;
	private List<Role> roles;
	private List<Roleitem> roleitems;
	
	private int id;
	private int rid;
	private String chks;
	private int type;
	
	private String method = "";
	
	private String searchValue;
	
	private JSONObject resultObj = null;

	//pagination
	private Integer page;
	private Integer rows;
	private Integer total_size;
	private Juese juese;
	//状态
	private int state;
	
	public RoleAction() {
		
	}
	
	//show list action
	public String index() {
		return SUCCESS;
	}
	
	public String open() {
		this.setId(id);
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
		case "setOpRole":
			result = "setOpRole_page";
			break;
		case "setDataRole":
			result = "setDataRole_page";
			break;
		}
		
		return result;
	}
	
	//search action
//	public String search() {
//		try {
//			if (type == 0)
//				roles = roleService.searchOp("%" + searchValue + "%");
//			else
//				roles = roleService.searchData("%" + searchValue + "%");
//				
//			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
//			map.put("page", (page-1)*rows);
//			map.put("rows", rows);
//			map.put("type", type);
//			map.put("name", "%" + searchValue + "%");
//			total_size = roles.size();
//			roles = roleService.findPagination(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ERROR;
//		}
//
//		WebUtil._setResponseContentType("application/json");
//		makeJSONObjFromRoles();
//
//		return SUCCESS;
//	}
	
	public String search() {
		try {
//			if (type == 0){
//				//	roles = roleService.searchOp("%" + searchValue + "%");
//				listJuese = jueseService.searchOp("%" + searchValue + "%");
//			}else{
////				roles = roleService.searchData("%" + searchValue + "%");
//			}
			Map<String, Object> map = new HashMap<String, Object>();	//search conditions
			map.put("page", (page-1)*rows);
			map.put("rows", rows);
//			map.put("type", type);
			map.put("name", "%" + searchValue + "%");
//			if(listJuese.isEmpty()){
//				total_size=0;
//			}else{
//				total_size = listJuese.size();
//			}
			if(type==1){
				listDataJuese = dataJueseService.findPagination(map);
				total_size = dataJueseService.countPagination(map);
				listJuese = new ArrayList<Juese>();
				for(DataJuese d:listDataJuese){
					Juese j = d;
					listJuese.add(d);
				}
			}else{
				listJuese = jueseService.findPagination(map);
				total_size = jueseService.countPagination(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		WebUtil._setResponseContentType("application/json");
		if(type==1){
			makeJSONObjFromDataJuese();	
		}else{
			makeJSONObjFromJuese();
		}

		return SUCCESS;
	}
	
	public String viewJuese(){
		if(type==1){
			juese = dataJueseService.findById(id);
		}else{
			juese = jueseService.findById(id);
		}
		return "viewJuese";
	}
	
	public String updateJuese(){
		if(type==1){
			DataJuese j = new DataJuese();
			j.setId(juese.getId());
			j.setName(juese.getName());
			j.setRemark(juese.getRemark());
			state=dataJueseService.update(j);
		}else{
			state=jueseService.update(juese);
		}
		return "updateJuese";
	}
	
	public String addJuese(){
		if(juese.getType()==1){
			DataJuese j = new DataJuese();
			j.setId(juese.getId());
			j.setName(juese.getName());
			j.setRemark(juese.getRemark());
			this.dataJueseService.addJuese(j);
		}else{
			this.jueseService.addJuese(juese);
		}
		return "addJuese";
	}
	
	//role view action
	public String view() {
		// for detail view
		try {
			role = roleService.findOneById(id);
		} catch (Exception e) {
			return "fail";
		}
		
		return "success";
	}
	
	//role add action
	public String add() {
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", role.getName());
			m.put("type", type);
			m.put("id", 0);
			List<Role> r = roleService.existName(m);
			Map<String, Object> json = new HashMap<String, Object>();
			if (r.size() > 0) {
				json.put("err_code", Errors.ERR_ALREADY_NAME);
				json.put("err_msg", Errors.getErrorMessage(Errors.ERR_ALREADY_NAME));
				resultObj = JSONObject.fromObject(json);
				return SUCCESS;
			}

			role.setType(type);
			role.setDeleted(Constants.STATUS_AVAILABLE);
			int affectedRows = roleService.addRole(role);
			if (affectedRows > 0) {
            	role = roleService.findLastAdded();
            	int r_id = role.getId();
            	List<Roleitem> items = roleitemService.findAllByRid(r_id);
            	if (items.size() > 0) {
            		affectedRows = roleitemService.deleteItems(r_id);
            	}
            	Map<String, Object> map = new HashMap<String, Object>();
            	map.put("rid", r_id);
            	map.put("type", type);
            	affectedRows = roleitemService.addItems(map);
            	if (affectedRows > 0) {
            		request.setAttribute("content_url", Constants.URL_ROLE_INDEX);
    				json.put("err_code", Errors.ERR_OK);
    				json.put("err_msg", Errors.getErrorMessage(Errors.ERR_OK));
    				resultObj = JSONObject.fromObject(json);
            		return SUCCESS;
            	} else {
            		return ERROR;
            	}
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	//role edit action
	public String edit() {
		try {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("name", role.getName());
			m.put("type", type);
			m.put("id", role.getId());
			List<Role> r = roleService.existName(m);
			Map<String, Object> json = new HashMap<String, Object>();
			if (r.size() > 0) {
				json.put("err_code", Errors.ERR_ALREADY_NAME);
				json.put("err_msg", Errors.getErrorMessage(Errors.ERR_ALREADY_NAME));
				resultObj = JSONObject.fromObject(json);
				return SUCCESS;
			}

			int affectedRows = roleService.editRole(role);
			if (affectedRows > 0) {
				json.put("err_code", Errors.ERR_OK);
				json.put("err_msg", Errors.getErrorMessage(Errors.ERR_OK));
				resultObj = JSONObject.fromObject(json);
				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	//role delete action
	public String delete() {
		if (id <= 0) {
			return ERROR;
		}
		int err = Errors.ERR_OK;
		try {
			if (administratorService.findByRid(id) > 0) {
				err = Errors.ERR_ALREADY_USING;
			} else {
//				int affectedRows = roleService.deleteRole(id);
				int affectedRows = 0;
				if(type==0){
					 affectedRows =  jueseService.delete(id);
				}else{
					 affectedRows =  dataJueseService.delete(id);
				}
				if (affectedRows > 0 ) {
					err = Errors.ERR_OK;
				} else {
					err = Errors.ERR_SQL_ERROR;
				}
			}
		} catch (Exception e) {
			err = Errors.ERR_UNKNOWN;
		}
		WebUtil._setResponseContentType("application/json");
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("err_code", err);
		json.put("err_msg", Errors.getErrorMessage(err));
		resultObj = JSONObject.fromObject(json);
		return SUCCESS;
	}
	
	//assign operations or datas
	public String assignOp() {
		//check method parameter
		if (method == null || !method.equals("edit")) {
			request.setAttribute("content_url", Constants.URL_OP_ROLE + "?rid=" + id);
			return "forward";
		}
		return updateRoleitems();

	}
	
	//assign operations or datas
	public String assignData() {
		if (method == null || !method.equals("edit")) {
			request.setAttribute("content_url", Constants.URL_DATA_ROLE + "?rid=" + id);
			return "forward";
		}
		return updateRoleitems();

	}
	
	//get role items
	public String getRoleitemlist() {
//		try {
			WebUtil._setResponseContentType("application/json");
			roleitems = roleitemService.findAllByRid(Integer.valueOf(rid));
			resultObj = makeJSONObjFromItems();
			return SUCCESS;
//		} catch (Exception e) {
//			return ERROR;
//		}
	}
	
	//update changed roleitems
	public String updateRoleitems() {
		try {
			String item_chks = getChks();
			String chk_list[] = item_chks.split(",");
			int affectedRows = 0;
			List<Roleitem> items = roleitemService.findAllByRid(rid);
			for (int i = 0; i < items.size(); i++) {
				Roleitem item = items.get(i);
				item.setChk(Integer.valueOf(chk_list[i]));
				affectedRows += roleitemService.editItem(item);
			}
			if (affectedRows > 0) {
				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String editItems() {
		return SUCCESS;
	}

	public String exportExcel() {
		
		return "success";
	}
	
	//get Op items' names
	public String getOpNames(int r_id) {
		List<Roleitem> items = roleitemService.findAllByRid(r_id);
		String s = "";
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getLevel() <= 2 && items.get(i).getChk() > 0) {
				if (s != "")
					s += ", ";
				s += items.get(i).getName();
			}
		}
		s = Common.reduceStr(s, 20);
		return s;
	}
	
	//convert role list to JSON object 
	private void makeJSONObjFromRoles() {
		if (roles == null) {
			resultObj = null;
			return;
		}
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Role info : roles) {

			Map<String, Object> m = new HashMap<String, Object>();

			m.put("id", info.getId());
			String url = String.format("<a href='javascript:showView(%d,%d);'>查看</a>|", info.getId(), info.getType());
			url += String.format("<a href='javascript:showEdit(%d,%d);'>修改</a>|", info.getId(), info.getType());
			url += String.format("<a href='javascript:deleteRole(%d,\"%d\");'>删除</a>|", info.getId(), 0);
			url += String.format("<a href='javascript:showItems(%d,%d);'> 权限分配</a>", info.getId(), info.getType());//assign operations or datas 

			m.put("operation", url);
			m.put("name", info.getName());
			m.put("comment", getOpNames(info.getId()));
			m.put("remark", info.getRemark());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}
	
	private void makeJSONObjFromJuese() {
		if (listJuese == null) {
			resultObj = null;
			return;
		}
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Juese info : listJuese) {

			Map<String, Object> m = new HashMap<String, Object>();

			m.put("id", info.getId());
			String url = String.format("<a href='javascript:showView(%d,%d);'>查看</a>|", info.getId(), 0);
			url += String.format("<a href='javascript:showEdit(%d,%d);'>修改</a>|", info.getId(), 0);
			url += String.format("<a href='javascript:deleteRole(%d,\"%d\");'>删除</a>|", info.getId(), 0);
			url += String.format("<a href='javascript:showItems(%d,%d);'> 权限分配</a>", info.getId(),0);//assign operations or datas 

			m.put("operation", url);
			m.put("name", info.getName());
//			m.put("comment", getOpNames(info.getId()));
			m.put("remark", info.getRemark());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}

	private void makeJSONObjFromDataJuese() {
		if (listJuese == null) {
			resultObj = null;
			return;
		}
		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Juese info : listJuese) {

			Map<String, Object> m = new HashMap<String, Object>();

			m.put("id", info.getId());
			String url = String.format("<a href='javascript:showView(%d,%d);'>查看</a>|", info.getId(), 1);
			url += String.format("<a href='javascript:showEdit(%d,%d);'>修改</a>|", info.getId(), 1);
			url += String.format("<a href='javascript:deleteRole(%d,\"%d\");'>删除</a>|", info.getId(), 1);
			url += String.format("<a href='javascript:showItems(%d,%d);'> 权限分配</a>", info.getId(),1);//assign operations or datas 

			m.put("operation", url);
			m.put("name", info.getName());
//			m.put("comment", getOpNames(info.getId()));
			m.put("remark", info.getRemark());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", total_size);
		json.put("rows", al);
		resultObj = JSONObject.fromObject(json);
	}
	//convert roleitems to JSON object
	private JSONObject makeJSONObjFromItems() {

		if (roleitems == null) {
			return null;
		}

		ArrayList<Map<String, Object>> al = new ArrayList<Map<String, Object>>();
		for (Roleitem info : roleitems) {

			Map<String, Object> m = new HashMap<String, Object>();

			m.put("id", info.getId());
			m.put("name", info.getName());
			m.put("rid", info.getRid());
			m.put("mid", info.getMid());
			m.put("level", info.getLevel());
			m.put("parent", info.getParent());
			m.put("associd", info.getAssocid());
			m.put("cid", info.getCid());
			m.put("chk", info.getChk());
			al.add(m);
		}

		Map<String, Object> json = new HashMap<String, Object>();
		json.put("total", roleitems.size());
		json.put("rows", al);
		return JSONObject.fromObject(json);
	}
	
	//get comment
	public String getComment(List<Roleitem> objlist) {
		String s = "";
		for (int i = 0; i < objlist.size(); i++) {
			if (objlist.get(i).getChk() == 1) {
				s += objlist.get(i).getName();
			}
		}
		return Common.reduceStr(s, 20);
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	public RoleitemService getRoleitemService() {
		return roleitemService;
	}

	public void setRoleitemService(RoleitemService roleitemService) {
		this.roleitemService = roleitemService;
	}
	
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Roleitem> getRoleitems() {
		return roleitems;
	}

	public void setRoleitems(List<Roleitem> roleitems) {
		this.roleitems = roleitems;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getRid() {
		return this.rid;
	}
	
	public void setRid(int rid) {
		this.rid = rid;
	}
	
	public String getChks() {
		return this.chks;
	}
	
	public void setChks(String chks) {
		this.chks = chks;
	}
	
	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
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

	public AdministratorService getAdministratorService() {
		return administratorService;
	}

	public void setAdministratorService(
			AdministratorService administratorService) {
		this.administratorService = administratorService;
	}

	public JueseService getJueseService() {
		return jueseService;
	}

	public void setJueseService(JueseService jueseService) {
		this.jueseService = jueseService;
	}

	public List<Juese> getListJuese() {
		return listJuese;
	}

	public void setListJuese(List<Juese> listJuese) {
		this.listJuese = listJuese;
	}

	public Juese getJuese() {
		return juese;
	}

	public void setJuese(Juese juese) {
		this.juese = juese;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public DataJueseService getDataJueseService() {
		return dataJueseService;
	}

	public void setDataJueseService(DataJueseService dataJueseService) {
		this.dataJueseService = dataJueseService;
	}

	public List<DataJuese> getListDataJuese() {
		return listDataJuese;
	}

	public void setListDataJuese(List<DataJuese> listDataJuese) {
		this.listDataJuese = listDataJuese;
	}
}
