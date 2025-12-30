package com.pinche.operation.feedback.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.pinche.operation.feedback.dao.FeedBack;
import com.pinche.operation.feedback.service.FeedBackService;

/**
 * 
 * 用户反馈信息Action
 * @author xcnana
 * @date 2014-08-09
 * 
 */
public class FeedBackAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private int page;
	private int rows;
	private JSONObject resultObject=null;

	private String conditionSelect;
	private String conditionInput;
	
	private long id=0;
	private long userid=0;
	private String userName=null;
	private String begin=null;
	private String over=null;
	private short deleted;
    private String city=null;
	private String phone=null;
	private String verified=null;
	private String title=null;

	@Resource
    private FeedBackService feedBackService;
	public FeedBackAction(){
		super();
	}
    /**
     * 页面的跳转
     * @author  xcnana
     * @date 2014-08-09
     */
	public String list(){
		return "success";
	}
	
	/**
	 * 通过条件查询反馈信息
	 * @author xcnana
	 * @date 2014-08-09
	 * 
	 */
	public String findByCondition(){
			List<FeedBack> feedbackinfo = null;
			Map<String, Object> params = new HashMap<String, Object>();
			// 查询结果从第0条开始，查询10条记录
			
			System.out.println(id+"`````````````"+userid);
		   if(id!=0&&(id+"")!="")
			{
			    params.put("id",id);
			}
			if(userid!=0&&(userid+"")!=null)
			{
			    params.put("userid",userid);
			}
		    params.put("userName", '%'+userName+'%');
		    params.put("city", '%'+city+'%');
		    params.put("begin", begin);
		    params.put("over", over);
		    params.put("phone", '%'+phone+'%');
		    params.put("verified", verified);
		    int  total=feedBackService.countByCondition(params);
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			feedbackinfo=feedBackService.findByCondition(params);
			System.out.println(feedbackinfo);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			System.out.println(feedbackinfo.size());
			
			// 需要显示数据的总页数
			 //jsonMap.put("total", list.size());
			if(total==0){
				jsonMap.put("total", 1);
				// 需要实现数据的总记录数
				jsonMap.put("rows", 0);
			}else{
				jsonMap.put("total", total);
				// 需要实现数据的总记录数
				jsonMap.put("rows", feedbackinfo);
			}
			
		    
			resultObject = JSONObject.fromObject(jsonMap);
			System.out.println(resultObject);
			return "success";
	}
	/*
	 * 通过ID查询反馈的详细信息
	 */
	public String findById(){
		System.out.println("findById方法....");
			FeedBack feedback  = feedBackService.findById(id);
			System.out.println(id);

/*			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", 1);
			// 需要实现数据的总记录数
			jsonMap.put("rows", feedback);
		    */
			resultObject = JSONObject.fromObject(feedback);
		System.out.println(resultObject);
		return "success";
	}
	/*
	 * 对反馈信息记录的 逻辑删除
	 */
	  public String delete(){
		  feedBackService.delete(id);
		  return "success";
	  }
	
	/*
	 * -----setter 和 getter 方法-----
	 */


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

	public JSONObject getResultObject() {
		return resultObject;
	}
	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public void setId(long id) {
		this.id = id;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public long getId() {
		return id;
	}
	public String getUserName() {
		return userName;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public short getDeleted() {
		return deleted;
	}

	public void setDeleted(short deleted) {
		this.deleted = deleted;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}
	public FeedBackService getFeedBackService() {
		return feedBackService;
	}

	public void setFeedBackService(FeedBackService feedBackService) {
		this.feedBackService = feedBackService;
	}

	public String getConditionSelect() {
		return conditionSelect;
	}

	public void setConditionSelect(String conditionSelect) {
		this.conditionSelect = conditionSelect;
	}

	public String getConditionInput() {
		return conditionInput;
	}

	public void setConditionInput(String conditionInput) {
		this.conditionInput = conditionInput;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getOver() {
		return over;
	}
	public void setOver(String over) {
		this.over = over;
	}
	
	
	
}
