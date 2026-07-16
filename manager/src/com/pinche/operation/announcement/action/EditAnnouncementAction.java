package com.pinche.operation.announcement.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.pinche.common.action.SecureAction;
import com.pinche.operation.announcement.dao.Announcement;
import com.pinche.operation.announcement.service.AnnouncementServiceImpl;
import java.sql.Timestamp;

/**
* @ClassName: AnnouncementAction.java
* @Description: announcement
* @author haotang365  
* @date 2014-8-18 10:16:33
* @version V1.0   
*/

public class EditAnnouncementAction extends SecureAction {

	private static final long serialVersionUID = 1L;
	
	// 属性--信息对象
	private Announcement announcement;
	// 属性--判断编辑信息的类型
	private String editType;
	// 属性--信息service对象
	private AnnouncementServiceImpl announcementService;
	
	
	// 属性--announcement 字段
	private int announcementId;
	//属性：announcement 字段
	private String title;
	//属性：announcement 字段
	private String content;
	//属性：announcement 字段
	private String ps_city;
	//属性：announcement 字段
	private String validate;
	//属性：announcement 字段
	private String publisher;
	//属性：announcement 字段
	private String range;
	//属性：announcement 字段
	private String validateBool;
	/**
	 * 
	 * 跳转到编辑信息页面
	 * 
	 * @return
	 */

	public String toEditAnnouncementPage() {
		
		
		
		// 根据传入的id获取一个project对象。
		if (announcementId == 0 && editType.equals("add")) {
			announcement = new Announcement();
		} else {
			announcement = announcementService.getAnnouncementById(announcementId);
		}
	

		return SUCCESS;
	}

	/**
	 * 保存或修改信息。
	 * 
	 * @return
	 */
	public String editAnnouncement() {
		
		// 设置值 
			announcement = new Announcement();
			//id
			announcement.setId(announcementId);
            // 标题
		    announcement.setTitle(title);
            // 公告内容
		    announcement.setContent(content);
            
            // 发布者，来自于管理员表的主键 TODO
		    announcement.setPublisher(1);
            // 每个城市平台信息费分成不一样
		    announcement.setPs_city(ps_city);
            // 范围:全部，车主
		    announcement.setRange(range);
		    // 更改时间
		    Timestamp curTime = new Timestamp(System.currentTimeMillis());
		    announcement.setPs_date(curTime);
		    //设置有效
		    announcement.setDeleted(0);
		  
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
		    // 有效期
		    try {
		    	if("true".equals(validateBool)){
		    		//格式化时间显示
				   
				    Timestamp timeStamp=null;
					try {
						timeStamp = new Timestamp(simpleDateFormat.parse(validate).getTime());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		announcement.setValidate(timeStamp);
		    	}else{
		    		announcement.setValidate(new Timestamp(simpleDateFormat.parse("0000-00-00").getTime()));
		    	}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    
		    announcementService.insert(announcement);
		    
		return SUCCESS;
	}


	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public AnnouncementServiceImpl getAnnouncementService() {
		return announcementService;
	}

	public void setAnnouncementService(AnnouncementServiceImpl announcementService) {
		this.announcementService = announcementService;
	}

	public int getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(int announcementId) {
		this.announcementId = announcementId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPs_city() {
		return ps_city;
	}

	public void setPs_city(String ps_city) {
		this.ps_city = ps_city;
	}
	
	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getValidateBool() {
		return validateBool;
	}

	public void setValidateBool(String validateBool) {
		this.validateBool = validateBool;
	}
	
	
}

