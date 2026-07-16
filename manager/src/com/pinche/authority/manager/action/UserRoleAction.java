package com.pinche.authority.manager.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.nmdy.entity.DataJuese;
import com.nmdy.entity.Juese;
import com.nmdy.sysManage.role.service.DataJueseService;
import com.nmdy.sysManage.role.service.JueseService;
import com.opensymphony.xwork2.ActionContext;
import com.pinche.authority.manager.dao.UserRole;
import com.pinche.authority.manager.service.AdministratorServiceImpl;
import com.pinche.authority.manager.service.UserRoleServiceImpl;
import com.pinche.authority.role.service.RoleServiceImpl;
import com.pinche.common.Constants;
import com.pinche.common.Errors;
import com.pinche.common.WebUtil;
import com.pinche.common.action.SecureAction;

public class UserRoleAction extends SecureAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/* properties for parameter */
	private String method = null;
	private String id = null; 
	private AdministratorServiceImpl administratorService;
	private UserRoleServiceImpl userRoleService;
	private RoleServiceImpl roleService;
	private JueseService jueseService;
	private DataJueseService dataJueseService;
	private JSONObject resultObj = null;
	private List<Juese> list;
	private Juese juese;
	private int type;
	private Object jsonObj;
	private String msg;
	
	/**
	 * 角色ids
	 */
	private String roleIds;
	
	
	public String assign() {
		return SUCCESS;
	}
	
	/**
	 * 添加角色
	 * @return
	 */
	public String addJuese(){
		System.out.println("type:"+type);
		if(juese.getType()==1||type==1){
			DataJuese j = new DataJuese();
			j.setId(juese.getId());
			j.setName(juese.getName());
			j.setRemark(juese.getRemark());
			this.dataJueseService.addJuese(j);
		}else{
			if(this.jueseService.findByName(juese.getName())!=0){
				jsonObj = 1;
				return "addJueseError";
			}
			this.jueseService.addJuese(juese);
		}
		return "addJuese";
	}
	
	public String getRoles(){
		int user_id = Integer.parseInt(id); //userInfo.getId();
		System.out.println("id:"+id);
		list = jueseService.listAllCheckedRole(user_id);
		jsonObj = list;
		return "getRoles";
	}
	
	public String updateRoles(){
		int user_id = Integer.parseInt(id); //userInfo.getId();
		this.jueseService.updateUserRoles(user_id, roleIds);
		ActionContext.getContext().put("msg", Constants.UPDATE_SUCCESS);
		this.setId(id);
		return "updateRoles";
	}
	
	public String setRoles() {
		String user_role_ids[] = WebUtil._getRequestParameterMap("user_role_ids")[0].toString().split(",");
		WebUtil._setResponseContentType("application/json");
		
		int user_id = Integer.parseInt(id);
		
		List<UserRole> assignedList = null;
		Map<Integer, Integer> user_roles_map = new HashMap<Integer, Integer>();

		Map<String, Object> json = new HashMap<String, Object>();
		int err = Errors.ERR_OK;
		
		try {
			if (user_id != 0) {
				assignedList = userRoleService.findByUserId(user_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			err = Errors.ERR_SQL_ERROR;
		}

		if (assignedList != null) {
			for (UserRole user_role : assignedList) {
				user_roles_map.put(user_role.getRole_id(), user_role.getRole_id());
			}
		}
		
		for (String role_id : user_role_ids) {
			try {
				Integer rid = Integer.valueOf(role_id.split("_")[1]);
				Integer assigned_rid = user_roles_map.get(rid);
				if (assigned_rid == null) {
					UserRole role = new UserRole();
					role.setRole_id(rid);
					role.setUser_id(user_id);
					
					int affectedRows = userRoleService.addRole(role);
					if (affectedRows == 0) {
						err = Errors.ERR_SQL_ERROR;
						break;
					}
				}
				
				user_roles_map.remove(rid);
			} catch (Exception e) {
				e.printStackTrace();
				err = Errors.ERR_SQL_ERROR;
				break;
			}
		}
		
		// if there are removed user roles (remains in user_roles_map)
		if (user_roles_map.size() > 0) {
			for (Iterator<Integer> i = user_roles_map.keySet().iterator(); i.hasNext();) {
				Integer removed_id = i.next();
				try {
					int affectedRows = userRoleService.deleteRole(removed_id);
					if (affectedRows == 0) {
						err = Errors.ERR_SQL_ERROR;
						break;
					}					
				} catch (Exception e) {
					e.printStackTrace();
					err = Errors.ERR_SQL_ERROR;
					break;
				}
			};
		}
				
		json.put("err_code", err);
		resultObj = JSONObject.fromObject(json);
		return SUCCESS;
	}


	
	public String delete() {
		return SUCCESS;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AdministratorServiceImpl getAdministratorService() {
		return administratorService;
	}

	public void setAdministratorService(AdministratorServiceImpl administratorService) {
		this.administratorService = administratorService;
	}

	public UserRoleServiceImpl getUserRoleService() {
		return userRoleService;
	}

	public void setUserRoleService(UserRoleServiceImpl userRoleService) {
		this.userRoleService = userRoleService;
	}

	public RoleServiceImpl getRoleService() {
		return roleService;
	}
	
	public void setRoleService(RoleServiceImpl roleService) {
		this.roleService = roleService;
	}
	
	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	public JueseService getJueseService() {
		return jueseService;
	}

	public void setJueseService(JueseService jueseService) {
		this.jueseService = jueseService;
	}

	public List<Juese> getList() {
		return list;
	}

	public void setList(List<Juese> list) {
		this.list = list;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public Juese getJuese() {
		return juese;
	}

	public void setJuese(Juese juese) {
		this.juese = juese;
	}

	public DataJueseService getDataJueseService() {
		return dataJueseService;
	}

	public void setDataJueseService(DataJueseService dataJueseService) {
		this.dataJueseService = dataJueseService;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Object getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(Object jsonObj) {
		this.jsonObj = jsonObj;
	}
	
	
}
