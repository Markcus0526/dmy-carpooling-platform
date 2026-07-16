package com.nmdy.operation.share.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;






import com.nmdy.operation.share.dao.ShareEntity;
import com.nmdy.operation.share.service.ShareService;
import com.opensymphony.xwork2.ActionSupport;

public class ShareAction extends ActionSupport{
	private String smsTampText;
	private String sinaTempText;
	private String wechatTempText;
	private String value;
	private ShareService shareService;// 实现数据操作
	private JSONObject jsonObject;// 返回的json
	/*
	 * 页面跳转方法
	 */
	public String index() {
		//System.out.println("index has been here...");
		
		
		return SUCCESS;
	}
	/*
	 * 页面信息查询方法
	 */
	public String findvalue() {
		try {
			List<ShareEntity> share;// 返回的实体类存放处
			share = shareService.findvalue();
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", 3);
			//System.out.println(jsonMap);
			jsonMap.put("rows", share);		
			jsonObject = JSONObject.fromObject(jsonMap);
			//System.out.println(jsonObject);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	
		return SUCCESS;
	}
	/*
	 * 修改保存方法
	 */
	public String update(){
		try {
			if(null!=sinaTempText){
				shareService.update1(sinaTempText);
			
			}
			if(null!=wechatTempText){
				shareService.update2(smsTampText);
				
			}
			if(null!=wechatTempText){
				shareService.update3(wechatTempText);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return SUCCESS;
		
		
		
	}
//-----------------------------------------get set---------------------------------------------------------------------
	
	public String getSmsTampText() {
		return smsTampText;
	}

	public void setSmsTampText(String smsTampText) {
		this.smsTampText = smsTampText;
	}
	public String getSinaTempText() {
		return sinaTempText;
	}
	public void setSinaTempText(String sinaTempText) {
		this.sinaTempText = sinaTempText;
	}
	public String getWechatTempText() {
		return wechatTempText;
	}
	public void setWechatTempText(String wechatTempText) {
		this.wechatTempText = wechatTempText;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ShareService getShareService() {
		return shareService;
	}
	public void setShareService(ShareService shareService) {
		this.shareService = shareService;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

}
