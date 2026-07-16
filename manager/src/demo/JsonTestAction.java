package demo;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class JsonTestAction extends ActionSupport {
	// 将会被Struts2序列化为JSON字符串的对象
	private JSONObject resultObj;
	
	public String execute() throws Exception {
		return null;

	}

	/**
	 * 测试通过action以Struts2默认方式返回JSON数据
	 * 
	 * @return
	 */
	public String testJson() {
		//HttpServletResponse resp = ServletActionContext.getResponse();
		//resp.setContentType("application/json");
		System.out.println("test by json");
		JSONArray list=new JSONArray();
		
		User user = new User();
		user.setSex("man");
		user.setUserId(1);
		user.setUserName("sailor");
		list.add(user);
		HashMap hm=new HashMap();
		hm.put("rows", list);
		hm.put("total", list.size());
		//resultObj=new JSONObject();
		resultObj=JSONObject.fromObject(hm);
		// 返回结果
		return "ddd";
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}






}