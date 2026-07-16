package com.pinche.common.action;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.authority.manager.dao.OperLog;
import com.pinche.authority.manager.service.OperLogService;
import com.pinche.common.WebUtil;
import com.pinche.common.interceptor.CheckAuthWare;

public class SecureAction extends ActionSupport implements ServletRequestAware, CheckAuthWare {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected HttpServletRequest request;
	protected OperLogService operLogService;
	
	public boolean isLogged() {
		Object user = WebUtil._session("user");
		if (user == null) {
			return false;
		} else {
		   return true;
		}
	}


	protected void logAdminOper(String table_name, String desc) {
		try {
			OperLog operLog = new OperLog();
			operLog.setUid(WebUtil._getLoggedUserId());
			operLog.setTableName(table_name);
			operLog.setActionUrl(WebUtil._getActionUrl());
			operLog.setDesc(desc);
			
			Timestamp curTime = new Timestamp(System.currentTimeMillis());
			operLog.setOperTime(curTime);
			
			operLogService.append(operLog);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	protected void logAdminOper(String table_name, int row_number, String desc) {
		String str = "\"Row Number\": " + String.valueOf(row_number);
		str += "; \"Desc\": \"" + desc +"\"";
		
		logAdminOper(table_name, str);
	}

	protected boolean checkAuthority(String path) {
		return WebUtil._session("resourceList").toString().contains(path);
	}
	
	public void setOperLogService(OperLogService operLogService){
		this.operLogService = operLogService;
	}
	
	public OperLogService getOperLogService() {
		return operLogService;
	}

	@Override
	public void setServletRequest(HttpServletRequest hsr) {
		this.request = hsr;
		try {
            this.request.setCharacterEncoding("BIG5");
        } catch (UnsupportedEncodingException ex) {
        	ex.printStackTrace();
        }
	}	
}
