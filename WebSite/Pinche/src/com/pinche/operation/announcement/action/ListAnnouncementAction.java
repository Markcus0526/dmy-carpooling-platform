package com.pinche.operation.announcement.action;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;










import com.pinche.common.action.SecureAction;
import com.pinche.operation.announcement.dao.Announcement;
import com.pinche.operation.announcement.service.AnnouncementService;
import com.pinche.operation.announcement.service.AnnouncementServiceImpl;

/**
* @ClassName: AnnouncementAction.java
* @Description: announcement
* @author haotang365  
* @date 2014-8-18 10:16:33
* @version V1.0   
*/

public class ListAnnouncementAction extends SecureAction {

	private static final long serialVersionUID = 1L;


	// 属性--集合
	private List<Map<String,Object>> resultList;

	// 属性--信息service对象
	private AnnouncementService announcementService;
	private Announcement announcement;


	// 属性--页面数据总记录数
	private int allRowsCount;
	
	//--------easyUI分页信息参数--------------
	private int page;
	private int rows;
	
	// 属性--保存的当前页
	private int savePageCurrent;
	// 属性--页面数据当前显示行数
	private int pageSize;

	// 属性--信息启用禁用状态
	private boolean disable;
	
	// 属性--页面传入的需要删除的数据
	private String checkedRows;
	// 属性--页面传入的某列的排序方式
	private String orderSort;
	// 属性--页面传入的排序列
	private String orderColumn;

	//-------JSON数据格式参数-----------
	private JSONObject jsonObject;
	private JSONArray jsonArray;
	//-------------页面查询条件------------
	//属性：页面查询条件关键字
	private String keyword;
	//属性：页面查询条件关键字
	private String searchRange;
	//属性：页面查询条件关键字
	private String ps_city;
	//属性：页面查询条件关键字
	private String validate;
	//属性：页面查询条件关键字
	private String id;
	//属性：页面查询条件关键字
	private String publisher;
	//属性：页面查询条件关键字
	private String range;
	
	
	
	
	public ListAnnouncementAction(){
		//初始化页面排序
		orderSort = "asc";
		// 初始化页面显示行数
		pageSize = 10;
	}
	

	/**
	 * 此方法为页面加载事件 功能为初始化页面数据
	 */
	public String loadAnnouncement() {
		return SUCCESS;
	}
		

	/**
	 * 获取数据库中的数据 可由用户指定数据行以及数据列 可将当前页面数据导出
	 */
	public String searchAnnouncement() {
		
		
		// 查询条件
		Map<String, Object> searchConditionMap = getSearchMap(); 
		
		// 获取所有数据总条数
		allRowsCount = announcementService.selectCount(searchConditionMap);

		try {
			// 获取信息集合
			resultList = announcementService.selectListMap(searchConditionMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// 需要显示数据的总数
		jsonMap.put("total", allRowsCount);
		// 需要实现数据的总记录数
		jsonMap.put("rows", resultList);
		
		try {
			jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		return SUCCESS;
	}


	/**
	 * 此方法用于封装查询条件
	 * @return返回查询所需集合
	 */
	private Map<String, Object> getSearchMap() {
		
		Map<String, Object> searchConditionMap = new HashMap<String, Object>();

		// 根据信息传入的列进行排序
		searchConditionMap.put("OrderColumn", "id");
		searchConditionMap.put("order", "asc");
		
		
		//页面查询条件添加
		//公告范围
		if(ps_city!=null && !"".equals(ps_city)){
			if(!"all".equals(ps_city)){
				searchConditionMap.put("ps_cityEqual", ps_city);
			}
		}
		//公告有效性
		if(validate!=null && !"".equals(validate)){
			if("valid".equals(validate)){
				searchConditionMap.put("validateBegin", new Date());
			}
			if("invalid".equals(validate)){
				searchConditionMap.put("validateEnd", new Date());
			}
		}
		//公告编号
		if(id!=null && !"".equals(id)){
			searchConditionMap.put("idEqual", id);
		}
		//发布人
		if(publisher!=null && !"".equals(publisher)){
			searchConditionMap.put("publisherLike", publisher);
		}
		
		//公告对象
		if(range!=null && !"".equals(range)){
			if("all".equals(range)){
				searchConditionMap.put("rangeEqual", 0);
			}
			if("driver".equals(range)){
				searchConditionMap.put("rangeEqual", 1);
			}
			
		}
		//检索范围
		if(searchRange!=null && !"".equals(searchRange)){
			if("title".equals(searchRange)){
				searchConditionMap.put("titleLike", keyword);
			}
			if("content".equals(searchRange)){
				searchConditionMap.put("contentLike", keyword);
			}	
			
		}
		
		
		
		// 将当前页传入sql
		searchConditionMap.put("LimitFromRowNum",  (page - 1) * rows);
		// 将当前显示数据行数传入sql
		searchConditionMap.put("LimitRowSize", rows);
		
		

		// 处理综合模糊查询
		if(!"".equals(searchConditionMap.get("collectionSearch")) && searchConditionMap.get("collectionSearch")!= null ){
			// 综合查询条件
			searchConditionMap.put("advanceColumnLike", "%" + searchConditionMap.get("collectionSearch") + "%");
		}
		
            // 主键 Equal
			if(searchConditionMap.get("idEqual") != null) {
		       searchConditionMap.put("idEqual", searchConditionMap.get("idEqual"));
		    }
		    // 主键 Like
		    if(searchConditionMap.get("idLike") != null) {
		       searchConditionMap.put("idLike","%" + searchConditionMap.get("idLike") + "%");
		    }

            // 标题 Equal
			if(searchConditionMap.get("titleEqual") != null) {
		       searchConditionMap.put("titleEqual", searchConditionMap.get("titleEqual"));
		    }
		    // 标题 Like
		    if(searchConditionMap.get("titleLike") != null) {
		       searchConditionMap.put("titleLike","%" + searchConditionMap.get("titleLike") + "%");
		    }

            // 公告内容 Equal
			if(searchConditionMap.get("contentEqual") != null) {
		       searchConditionMap.put("contentEqual", searchConditionMap.get("contentEqual"));
		    }
		    // 公告内容 Like
		    if(searchConditionMap.get("contentLike") != null) {
		       searchConditionMap.put("contentLike","%" + searchConditionMap.get("contentLike") + "%");
		    }

            // 更改时间 Equal
			if(searchConditionMap.get("ps_dateEqual") != null) {
		       searchConditionMap.put("ps_dateEqual", searchConditionMap.get("ps_dateEqual"));
		    }
		    // 更改时间 Like
		    if(searchConditionMap.get("ps_dateLike") != null) {
		       searchConditionMap.put("ps_dateLike","%" + searchConditionMap.get("ps_dateLike") + "%");
		    }

            // 发布者，来自于管理员表的主键 Equal
			if(searchConditionMap.get("publisherEqual") != null) {
		       searchConditionMap.put("publisherEqual", searchConditionMap.get("publisherEqual"));
		    }
		    // 发布者，来自于管理员表的主键 Like
		    if(searchConditionMap.get("publisherLike") != null) {
		       searchConditionMap.put("publisherLike","%" + searchConditionMap.get("publisherLike") + "%");
		    }

            // 每个城市平台信息费分成不一样 Equal
			if(searchConditionMap.get("ps_cityEqual") != null) {
		       searchConditionMap.put("ps_cityEqual", searchConditionMap.get("ps_cityEqual"));
		    }
		    // 每个城市平台信息费分成不一样 Like
		    if(searchConditionMap.get("ps_cityLike") != null) {
		       searchConditionMap.put("ps_cityLike","%" + searchConditionMap.get("ps_cityLike") + "%");
		    }

            // 范围:全部，车主 Equal
			if(searchConditionMap.get("rangeEqual") != null) {
		       searchConditionMap.put("rangeEqual", searchConditionMap.get("rangeEqual"));
		    }
		    // 范围:全部，车主 Like
		    if(searchConditionMap.get("rangeLike") != null) {
		       searchConditionMap.put("rangeLike","%" + searchConditionMap.get("rangeLike") + "%");
		    }

            // 有效期 Equal
			if(searchConditionMap.get("validateEqual") != null) {
		       searchConditionMap.put("validateEqual", searchConditionMap.get("validateEqual"));
		    }
		    // 有效期 Like
		    if(searchConditionMap.get("validateLike") != null) {
		       searchConditionMap.put("validateLike","%" + searchConditionMap.get("validateLike") + "%");
		    }
		    
		return searchConditionMap;
	}
	




	/**
	 * 撤销公告
	 * @return
	 */
	public String deleteAnnouncementById() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// 删除 调用视图的主表  
		int temp = announcementService.updateAnnouncementDisable(id);
		jsonMap.put("num", temp);
		try {
			jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}


	
	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}
	
	public int getAllRowsCount() {
		return allRowsCount;
	}

	public void setAllRowsCount(int allRowsCount) {
		this.allRowsCount = allRowsCount;
	}


	public int getSavePageCurrent() {
		return savePageCurrent;
	}

	public void setSavePageCurrent(int savePageCurrent) {
		this.savePageCurrent = savePageCurrent;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isDisable() {
		return disable;
	}

	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	public String getCheckedRows() {
		return checkedRows;
	}

	public void setCheckedRows(String checkedRows) {
		this.checkedRows = checkedRows;
	}


	public String getOrderSort() {
		return orderSort;
	}

	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
	}


	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}



	
	public AnnouncementService getAnnouncementService() {
		return announcementService;
	}


	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}


	public Announcement getAnnouncement() {
		return announcement;
	}
	
	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getRows() {
		return rows;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getSearchRange() {
		return searchRange;
	}


	public void setSearchRange(String searchRange) {
		this.searchRange = searchRange;
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


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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
	
	
	
}

