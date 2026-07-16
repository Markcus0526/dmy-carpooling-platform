package com.nmdy.operation.evaluate.dao;


//实体类
public class EvaluateEntity {
	private int eid;
	private int groupid;// 集团标识
	private String group_name;// 集团名
	private int userid;// userid
	private String ga_code;
	private String ga_name;
	private String username;// 用户名
	private String phone;// 用户电话号码
	private String city_register;// 注册城市
	private String city_cur;// 最后登录的城市
	private String orcity;//最后登录或注册城市
	private int status;// 状态
	private String level;// 评价等级
	private String msg;// 评价内容
	private String usertype;// 客户身份
	private int order_cs_id;// 订单编号
	private int from_userid;// 评价人id
	private int passnum;//乘客身份订单数
	private String ckpl;//乘客身份好评率
	private int drinum;//车主身份订单数
	private String czpl;//车主身份好评率
	private int bid ;
	private int to_userid;//与user表中id相对应 在evaluate_cs中
	private int cklevelt;//乘客身份好评率
	private int ckhp;//乘客身份好评数
	private int czlevelt;//乘客的评价总数
	private int czhp;//车主

	private String driver_verified;//车主验证 为1是车主 
	private String deleted;
    private String ps_date;//日期 
    private String ckpl1;//乘客身份好评率
	private String czpl1;//车主身份好评率
	
	private int czlevelt1;//
	private int cklevelt1;
    
	public String getCkpl1() {
		return ckpl1;
	}


	public void setCkpl1(String ckpl1) {
		this.ckpl1 = ckpl1;
	}


	public String getCzpl1() {
		return czpl1;
	}


	public void setCzpl1(String czpl1) {
		this.czpl1 = czpl1;
	}


	public String getPs_date() {
		return ps_date;
	}


	public void setPs_date(String ps_date) {
		this.ps_date = ps_date;
	}


	public String getDeleted() {
		return deleted;
	}


	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}


	public int getCklevelt1() {
		return cklevelt1;
	}


	public String getDriver_verified() {
		return driver_verified;
	}


	public void setDriver_verified(String driver_verified) {
		this.driver_verified = driver_verified;
	}


	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setCklevelt1(int cklevelt1) {
		this.cklevelt1 = cklevelt1;
	}

	public int getCklevelt() {
		return cklevelt;
	}

	public int getEid() {
		return eid;
	}


	public void setEid(int eid) {
		this.eid = eid;
	}


	public void setCklevelt(int cklevelt) {
		this.cklevelt = cklevelt;
	}

	public int getCkhp() {
		return ckhp;
	}

	public void setCkhp(int ckhp) {
		this.ckhp = ckhp;
	}

	public int getCzlevelt() {
		return czlevelt;
	}

	public void setCzlevelt(int czlevelt) {
		this.czlevelt = czlevelt;
	}

	public int getCzhp() {
		return czhp;
	}

	public void setCzhp(int czhp) {
		this.czhp = czhp;
	}



	public String getCkpl() {
		return ckpl;
	}

	public void setCkpl(String ckpl) {
		this.ckpl = ckpl;
	}


	public String getCzpl() {
		return czpl;
	}

	public void setCzpl(String czpl) {
		this.czpl = czpl;
	}

	public int getTo_userid() {
		return to_userid;
	}

	public void setTo_userid(int to_userid) {
		this.to_userid = to_userid;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getFrom_userid() {
		return from_userid;
	}

	public int getPassnum() {
		return passnum;
	}

	public void setPassnum(int passnum) {
		this.passnum = passnum;
	}

	public int getDrinum() {
		return drinum;
	}

	public void setDrinum(int drinum) {
		this.drinum = drinum;
	}

	public String getGa_name() {
		return ga_name;
	}

	public void setGa_name(String ga_name) {
		this.ga_name = ga_name;
	}

	public String getGa_code() {
		return ga_code;
	}

	public void setGa_code(String ga_code) {
		this.ga_code = ga_code;
	}

	public String getOrcity() {
		return orcity;
	}

	public void setOrcity(String orcity) {
		this.orcity = orcity;
	}

	public String getCity_cur() {
		return city_cur;
	}

	public void setCity_cur(String city_cur) {
		this.city_cur = city_cur;
	}

	public void setFrom_userid(int from_userid) {
		this.from_userid = from_userid;
	}




	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public int getOrder_cs_id() {
		return order_cs_id;
	}

	public void setOrder_cs_id(int order_cs_id) {
		this.order_cs_id = order_cs_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
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


	public int getCzlevelt1() {
		return czlevelt1;
	}


	public void setCzlevelt1(int czlevelt1) {
		this.czlevelt1 = czlevelt1;
	}

}
