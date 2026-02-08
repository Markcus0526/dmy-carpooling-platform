package com.nmdy.operation.notify.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;




import com.nmdy.operation.notify.dao.NotifyEntity;
import com.nmdy.operation.notify.service.NotifyService;
import com.opensymphony.xwork2.ActionSupport;

public class NotifyAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int page;
	private int rows;
	private JSONObject jsonObject;// 返回的json
	private int userid;// 此userid可以用在 save action中的 receiver
	private String city_register = "";// 注册城市
	private int level;// 评价
	private String city_cur = "";// 最后登录的城市
	private String ga_code = "";// 集团联盟标识
	private String ga_name = "";// 集团联盟名
	private String username = "";
	private String phone = "";
	private String usertype;
	private String cityselect = "";// 注册或最后登录城市查询
	private String cityinput = "";// 输入的值 根据cityselect的值来判断map存入哪个字段值、
	private String groupselect = "";// 集团 或者联盟标识
	private String groupinput = "";// 根据groupselect 的值来判断存入map的是哪个字段值
	private String yewuselect = "";// 按业务查询 根据返回的数字字符串来判断 是哪种业务
	private String userselect = "";// 根据用户信息查询
	private String userinput = "";// 根据userselect返回的值来判断存入map的是哪个字段值
	private String khsfselect = "";// 按客户身份查询 根据返回的数字字符串来判断客户的身份
	private int order_cs_id;// 订单编号
	private String msg;// 通知内容
	private int driver_verified;
	private int deleted;
	private int totle;
	private String title;// 标题
	private String receiver = null;// 存储客户id
	private String saveid;// 如果不是空的就执行 saveall 发送给所有的
	private String reg_date;
	private String reg_date1;
	private String last_login_time;
	private String last_login_time1;
	private String city_cur1;
	/*
	 * 发送给当前查询的全部客户的
	 * 将属性与查询方法的独立出来了
	 * 
	 */
	private String citys;//选择输入城市信息
	private String cityi;
	private String groups;//选择输入集团信息
	private String groupi;
	private String users;//选择输入客户信息
	private String useri;
	private String khs;//客户身份
	private String t1b;//注册查询开始时间
	private String t1e;//注册查询结束时间
	private String t2b;//最后登陆时间查询开始时间
	private String t2e;//最后登陆时间查询结束时间
	
	/**
	 * output
	 */

	private NotifyService notifyService;// 实现数据操作
/*
 * 页面跳转
 */
	public String index() {
		return SUCCESS;
	}

	
	/*
	 * 页面加载，根据条件查询
	 */
	public String finduser() {
		//System.out.println("find has been here...");
		HttpServletRequest request = ServletActionContext.getRequest();

		try {
			List<NotifyEntity> notify;// 返回的实体类存放处
			//System.out.println("finduser has been here...1");
			// params 需要提交表单之后才能获得
			Map<String, Object> params = new HashMap<String, Object>();

			if ("1".equals(cityselect)&&!"".equals(cityinput)&&null!=cityinput) {
				//System.out.println(cityselect);
				params.put("city_register", "%"+cityinput+"%");
				//System.out.println(cityinput);
			}
			if ("2".equals(cityselect)&&!"".equals(cityinput)&&null!=cityinput) {
				params.put("city_cur", "%"+cityinput+"%");
			}
			if ("3".equals(cityselect)&&!"".equals(cityinput)&&null!=cityinput) {
				// 选择其中的一种
				params.put("city_cur1", "%"+cityinput+"%");

			}
			// if (groupselect.equals("0")) {
			// groupinput=null;
			// }
			if ("1".equals(groupselect)&&!"".equals(groupinput)&&null!=groupinput) {
				//System.out.println(groupselect);
				params.put("ga_code","%"+groupinput+"%");
				//System.out.println(groupinput);
			}
			if ("2".equals(groupselect)&&!"".equals(groupinput)&&null!=groupinput) {
				params.put("ga_name", "%"+groupinput+"%");

			}
			/***
			 * 按照业务查询的方法 
			 * 
			 ***/

			if ("1".equals(userselect)&&!"".equals(userinput)&&null!=userinput) {
				//System.out.println(userselect);
				params.put("userid", "%"+userinput+"%");
				//System.out.println(userinput);
			}
			if ("2".equals(userselect)&&!"".equals(userinput)&&null!=userinput) {
				//System.out.println(userselect);
				params.put("phone", "%"+userinput+"%");
				//System.out.println(userinput);
			}
			if ("3".equals(userselect)&&!"".equals(userinput)&&null!=userinput) {
				//System.out.println(userselect);
				params.put("username", "%"+userinput+"%");
				//System.out.println(userinput);

			}
			if ("1".equals(khsfselect)) {
				params.put("driver_verified", 1);
			}
			if ("0".equals(khsfselect)) {
				params.put("driver_verified", "0");
			}
			if (!"".equals(reg_date) && null != reg_date) {

				params.put("reg_date", reg_date+" 00:00:00");
			}
			if (!"".equals(reg_date1) && null != reg_date1) {

				params.put("reg_date1", reg_date1+" 23:59:59");
			}
			if (!"".equals(last_login_time) && null != last_login_time) {

				params.put("last_login_time", last_login_time+" 00:00:00");
			}
			if (!"".equals(last_login_time1) && null != last_login_time1) {

				params.put("last_login_time1",last_login_time1+" 23:59:59");
			}
			params.put("start", (page - 1) * rows);
			params.put("limit", rows);
			//System.out.println(params);// 输出map里的内容
			notify = notifyService.finduser(params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", notifyService.getTotal(params));
			totle = notifyService.getTotal(params);
			//System.out.println(totle);
			request.setAttribute("totle", totle);
			//System.out.println(jsonMap);
			jsonMap.put("rows", notify);
			jsonObject = JSONObject.fromObject(jsonMap);
			System.out.println(jsonObject);
			//System.out.println("find has been here...tryend");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("find has been here...success");
		return SUCCESS;
	}

	/*
	 * 发送给选中客户 jdbc方法实现
	 */
	public String save() {

		//System.out.println("save has been here...");
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String, Object> params1 = new HashMap<String, Object>();
		Map<String, Object> m= new HashMap<String, Object>();
		int i ;
		
		
		try {

			if (null != receiver && null != title && null != msg
					&& !"".equals(receiver)) {
				
				// System.out.println("title"+title);
				// System.out.println("receiver"+receiver);
				// System.out.println("msg"+msg);
				params1.put("msg",msg);
				params1.put("title",title);
				if(null!=receiver){
					String receivers[] = receiver.split(",");
					for(int j=0;j<receivers.length;j++){
						String userids = receivers[j];
						params1.put("userids",userids);
						i = notifyService.addbatchInsert(params1);
						
								
					}
				}
			
				
				
				//System.out.println(bool);
			}

			if (receiver == null || "".equals(receiver)) {
			
				System.out.println(	request.getParameter("cityandre1"));
				Map<String, Object> params = new HashMap<String, Object>();

				if ("1".equals(citys)&&!"".equals(cityi)&&null!=cityi) {
					//System.out.println(cityselect);
					params.put("city_register", "%"+cityi+"%");
					//System.out.println(cityinput);
				}
				if ("2".equals(citys)&&!"".equals(cityi)&&null!=cityi) {
					params.put("city_cur", "%"+cityi+"%");
				}
				if ("3".equals(citys)&&!"".equals(cityi)&&null!=cityi) {
					// 选择其中的一种
					params.put("city_cur1", "%"+cityi+"%");

				}
				// if (groupselect.equals("0")) {
				// groupinput=null;
				// }
				if ("1".equals(groups)&&!"".equals(groupi)&&null!=groupi) {
					//System.out.println(groupselect);
					params.put("ga_code","%"+groupi+"%");
					//System.out.println(groupinput);
				}
				if ("2".equals(groups)&&!"".equals(groupi)&&null!=groupi) {
					params.put("ga_name", "%"+groupi+"%");

				}
				/***
				 * 按照业务查询的方法 
				 * 
				 ***/

				if ("1".equals(users)&&!"".equals(useri)&&null!=useri) {
					//System.out.println(userselect);
					params.put("userid", "%"+useri+"%");
					//System.out.println(userinput);
				}
				if ("2".equals(users)&&!"".equals(useri)&&null!=useri) {
					//System.out.println(userselect);
					params.put("phone", "%"+useri+"%");
					//System.out.println(userinput);
				}
				if ("3".equals(users)&&!"".equals(useri)&&null!=useri) {
					//System.out.println(userselect);
					params.put("username", "%"+useri+"%");
					//System.out.println(userinput);

				}
				if ("1".equals(khs)) {
					params.put("driver_verified", 1);
				}
				if ("0".equals(khs)) {
					params.put("driver_verified", "0");
				}
				if (!"".equals(t1b) && null != t1b) {

					params.put("reg_date",t1b+" 00:00:00");
				}
				if (!"".equals(t1e) && null != t1e) {

					params.put("reg_date1", t1e+" 23:59:59");
				}
				if (!"".equals(t2b) && null != t2b) {

					params.put("last_login_time", t2b+" 00:00:00");
				}
				if (!"".equals(t2e) && null != t2e) {

					params.put("last_login_time1",t2e+" 23:59:59");
				}

				List<Integer> ids = notifyService.findids(params);
				//System.out.println(ids);
				
				if (ids.size()>0 && !"".equals(title) &&!"".equals(msg)) {
					params1.put("msg",msg);
					params1.put("title",title);
				for(int a=0;a<ids.size();a++){
						int userids = (int) ids.get(a);
						params1.put("userids",userids);
						i = notifyService.addbatchInsert(params1);
				
				}
	
				}

			}
	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.put("ok", 1);
		jsonObject = JSONObject.fromObject(m);
		return SUCCESS;
	}

	/*
	 * 发送给全部客户
	 */
	public String saveall() {
		return SUCCESS;

	}

	// ------------------------------get set
	// -----------------------------------------------------------------

	public int getPage() {
		return page;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}

	public String getLast_login_time1() {
		return last_login_time1;
	}

	public void setLast_login_time1(String last_login_time1) {
		this.last_login_time1 = last_login_time1;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getReg_date1() {
		return reg_date1;
	}

	public void setReg_date1(String reg_date1) {
		this.reg_date1 = reg_date1;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getCity_register() {
		return city_register;
	}

	public void setCity_register(String city_register) {
		this.city_register = city_register;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getCity_cur() {
		return city_cur;
	}

	public void setCity_cur(String city_cur) {
		this.city_cur = city_cur;
	}

	public String getGa_code() {
		return ga_code;
	}

	public void setGa_code(String ga_code) {
		this.ga_code = ga_code;
	}

	public String getGa_name() {
		return ga_name;
	}

	public void setGa_name(String ga_name) {
		this.ga_name = ga_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public String getCityselect() {
		return cityselect;
	}

	public void setCityselect(String cityselect) {
		this.cityselect = cityselect;
	}

	public String getCityinput() {
		return cityinput;
	}

	public void setCityinput(String cityinput) {
		this.cityinput = cityinput;
	}

	public String getGroupselect() {
		return groupselect;
	}

	public void setGroupselect(String groupselect) {
		this.groupselect = groupselect;
	}

	public String getGroupinput() {
		return groupinput;
	}

	public void setGroupinput(String groupinput) {
		this.groupinput = groupinput;
	}

	public String getYewuselect() {
		return yewuselect;
	}

	public void setYewuselect(String yewuselect) {
		this.yewuselect = yewuselect;
	}

	public String getUserselect() {
		return userselect;
	}

	public void setUserselect(String userselect) {
		this.userselect = userselect;
	}

	public String getUserinput() {
		return userinput;
	}

	public void setUserinput(String userinput) {
		this.userinput = userinput;
	}

	public String getKhsfselect() {
		return khsfselect;
	}

	public void setKhsfselect(String khsfselect) {
		this.khsfselect = khsfselect;
	}

	public int getOrder_cs_id() {
		return order_cs_id;
	}

	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getDriver_verified() {
		return driver_verified;
	}

	public void setDriver_verified(int driver_verified) {
		this.driver_verified = driver_verified;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public int getTotle() {
		return totle;
	}

	public void setTotle(int totle) {
		this.totle = totle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSaveid() {
		return saveid;
	}

	public void setSaveid(String saveid) {
		this.saveid = saveid;
	}

	public String getCity_cur1() {
		return city_cur1;
	}

	public void setCity_cur1(String city_cur1) {
		this.city_cur1 = city_cur1;
	}


	public String getCitys() {
		return citys;
	}


	public void setCitys(String citys) {
		this.citys = citys;
	}


	public String getCityi() {
		return cityi;
	}


	public void setCityi(String cityi) {
		this.cityi = cityi;
	}


	public String getGroups() {
		return groups;
	}


	public void setGroups(String groups) {
		this.groups = groups;
	}


	public String getGroupi() {
		return groupi;
	}


	public void setGroupi(String groupi) {
		this.groupi = groupi;
	}


	public String getUsers() {
		return users;
	}


	public void setUsers(String users) {
		this.users = users;
	}


	public String getUseri() {
		return useri;
	}


	public void setUseri(String useri) {
		this.useri = useri;
	}


	public String getKhs() {
		return khs;
	}


	public void setKhs(String khs) {
		this.khs = khs;
	}


	public String getT1b() {
		return t1b;
	}


	public void setT1b(String t1b) {
		this.t1b = t1b;
	}


	public String getT1e() {
		return t1e;
	}


	public void setT1e(String t1e) {
		this.t1e = t1e;
	}


	public String getT2b() {
		return t2b;
	}


	public void setT2b(String t2b) {
		this.t2b = t2b;
	}


	public String getT2e() {
		return t2e;
	}


	public void setT2e(String t2e) {
		this.t2e = t2e;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
