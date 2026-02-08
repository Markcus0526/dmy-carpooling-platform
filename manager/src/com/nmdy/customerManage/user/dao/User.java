package com.nmdy.customerManage.user.dao;

import java.math.BigDecimal;
import java.util.Date;

public class User {


	//---------客户基本信息----------
	private Long 	   id          ;//numeric(8,0) comment '主键',
	private String 	   usercode  ;// text comment '用户名',
	private String     password  ;// text comment '登录密码',
	private String     nickname  ;// text comment '昵称',
	private String     phone     ;// text comment '电话，忘记密码时用',
	private String     remark           ;//text comment  '备注',
	private String     img              ;// text comment '用户图像',
	private int        person_verified  ;//  int comment '个人身份通过验证,0未认证，1已认证，2待认证',
	private int        driver_verified  ;//  int comment '车主身份通过验证,0未认证，1已认证，2待认证',
	
	//----------身份证相关信息-----------
	private String     username  ;// text comment '姓名',
	private String     nation           ;//text comment '民族',
	private String     id_card_num     ;//text comment  '身份证号',
	private String     sex              ;//text comment '性别',
	private String     address          ;//text comment '住址',
	private Date 	   birthday        ;//date comment '出生日期',年龄不用单独存，用当前日期-出生日期计算得出
	private String     id_card1         ;// text comment '身份证 正面',
	private String     id_card2         ;// text,comment '身份证 反面',
	
	

	
	//----------车辆相关信息--------------
	private String     driver_lisence1  ;// text comment '驾驶证 正面',
	private String     driving_lisence1 ;// text comment '行驶证 正面',
	private String     driver_lisence2  ;// text,comment '驾驶证 反面',
	private String     driving_lisence2 ;// text,comment '行驶证 反面',
	//private String     car_img          ;// text comment '爱车照片',已移至user_car表中
	//private String     plate_num        ;// text comment '车牌号',已移至user_car表中
	//private String     plate_num_last3  ;// text comment '车牌号后三位',已移至user_car表中
	
	
	//-----------分成相关信息-----------------
	private BigDecimal balance_ts         ;//decimal(8,2) comment '账户余额，对应ts表该用户最新的记录',
	private String     invitecode_self  ;//text comment '自己的邀请码，注册的时候系统自动生成',
	private String     invitecode_regist;//text comment '用哪个邀请码注册的',
	private String     app_register     ;//text comment '从哪个APP注册的,1mainApp,2pinche,3others',
	//private String     invitecode_source;//text comment '邀请码来源，是来自个人1，还是群组2，还是来自酒店等商业实体3',
	//private String     account          ;//text comment '账户',
	
	//private int        provide_profitsharing_way ;// int comment '当前用户向其邀请人贡献分成的方式：1按时间，2按次数，3方式1和方式2最先到的',
	private int     provide_profitsharing_time_as_passenger  ;//text comment '贡献分成方式按时间的话，是多长时间，此记录一个时间段。此变量在用户注册的时候从全局配置表里读取',
	private int     provide_profitsharing_time_as_driver     ;//char(10) comment '当前用户作为乘客的情况下向邀请人贡献的分成期间',
	private int     provide_profitsharing_count_as_passenger ;//text comment '贡献分成按次数的话，是多少次。比变量在用户注册的时候从全局配置表里读取，这里起到记录全局配置表历史的作用，防止全局记录表修改后无法对应',
	private int     provide_profitsharing_count_as_driver    ;//char(10) comment '当前用户作为车主的情况下向邀请人贡献的分成次数',
	private String     city_register                            ;//text comment '用户注册地',
	private String     city_cur                                 ;//text comment '用户最后一次登录的城市，短信推广之用',
	private BigDecimal ratio_as_passenger   ;//   decimal(8,2) comment '当前用户作为乘客的时候给邀请人的分成比例',
	private BigDecimal integer_as_passenger ;//   decimal(8,2) comment '当前用户作为车主的时候给邀请人的分成金额',
	private int     activeway_as_passenger     ;  //text comment '当前用户作为乘客时，给邀请人分成采取哪种方式',
	private BigDecimal ratio_as_driver              ;//decimal(8,2) comment '当前用户作为车主的时候给邀请人的分成比例',
	private BigDecimal integer_as_driver            ;//decimal(8,2) comment '当前用户作为车主的时候给邀请人的分成金额',
	private int     activeway_as_driver     ;  //text comment '当前用户作为乘客时，给邀请人分成采取哪种方式',
	private int     inviter_type               ;  //text comment '邀请人类型：1个人，2群组，3商业实体',
	private int        is_platform                   ;  //  int comment '是否是平台用户，默认为0，平台用户为1',
	
	private BigDecimal     ratio_as_passenger_self        ;  //  char(10) comment '乘客分成比例',             
	private BigDecimal     integer_as_passenger_self      ;  //  char(10) comment '乘客分成金额',             
	private int     	   active_as_passenger_self       ;  //  char(10) comment '被邀请人作为乘客的时候的分成方式',   
	private BigDecimal     ratio_as_driver_self           ;  //  char(10) comment '车主分成比例',             
	private BigDecimal     integer_as_driver_self         ;  //  char(10) comment '车主分成金额',             
	private int     	   active_as_driver_self          ;  //  char(10) comment '被邀请人作为车主的时候的分成方式',   
	private int     	   limit_month_as_passenger_self  ;  //  char(10) comment '被邀请人作为乘客分成的有效期',     
	private int    		   limit_month_as_driver_self     ;  //  char(10) comment '被邀请人作为车主分成的有效期',     
	private int     	   limit_count_as_passenger_self  ;  //  char(10) comment '被邀请人作为乘客分成的次数',      
	private int     	   limit_count_as_driver_self     ;  //  char(10) comment '被邀请人作为车主分成的次数'       
	private String reallocateProfit;//分成比例（由两个字段组合而成）
	private  String subbranch;//开户账户
	private String bankname;
	private String bankcard;
	
	
	
	
	//---------getters和setters方法----------------------
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getPerson_verified() {
		return person_verified;
	}
	public void setPerson_verified(int person_verified) {
		this.person_verified = person_verified;
	}
	public int getDriver_verified() {
		return driver_verified;
	}
	public void setDriver_verified(int driver_verified) {
		this.driver_verified = driver_verified;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getId_card_num() {
		return id_card_num;
	}
	public void setId_card_num(String id_card_num) {
		this.id_card_num = id_card_num;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getId_card1() {
		return id_card1;
	}
	public void setId_card1(String id_card1) {
		this.id_card1 = id_card1;
	}
	public String getId_card2() {
		return id_card2;
	}
	public void setId_card2(String id_card2) {
		this.id_card2 = id_card2;
	}
	public String getDriver_lisence1() {
		return driver_lisence1;
	}
	public void setDriver_lisence1(String driver_lisence1) {
		this.driver_lisence1 = driver_lisence1;
	}
	public String getDriving_lisence1() {
		return driving_lisence1;
	}
	public void setDriving_lisence1(String driving_lisence1) {
		this.driving_lisence1 = driving_lisence1;
	}
	public String getDriver_lisence2() {
		return driver_lisence2;
	}
	public void setDriver_lisence2(String driver_lisence2) {
		this.driver_lisence2 = driver_lisence2;
	}
	public String getDriving_lisence2() {
		return driving_lisence2;
	}
	public void setDriving_lisence2(String driving_lisence2) {
		this.driving_lisence2 = driving_lisence2;
	}
	public BigDecimal getBalance_ts() {
		return balance_ts;
	}
	public void setBalance_ts(BigDecimal balance_ts) {
		this.balance_ts = balance_ts;
	}
	public String getInvitecode_self() {
		return invitecode_self;
	}
	public void setInvitecode_self(String invitecode_self) {
		this.invitecode_self = invitecode_self;
	}
	public String getInvitecode_regist() {
		return invitecode_regist;
	}
	public void setInvitecode_regist(String invitecode_regist) {
		this.invitecode_regist = invitecode_regist;
	}
	public String getApp_register() {
		return app_register;
	}
	public void setApp_register(String app_register) {
		this.app_register = app_register;
	}


	public int getProvide_profitsharing_time_as_passenger() {
		return provide_profitsharing_time_as_passenger;
	}
	public void setProvide_profitsharing_time_as_passenger(
			int provide_profitsharing_time_as_passenger) {
		this.provide_profitsharing_time_as_passenger = provide_profitsharing_time_as_passenger;
	}
	public int getProvide_profitsharing_time_as_driver() {
		return provide_profitsharing_time_as_driver;
	}
	public void setProvide_profitsharing_time_as_driver(
			int provide_profitsharing_time_as_driver) {
		this.provide_profitsharing_time_as_driver = provide_profitsharing_time_as_driver;
	}
	public int getProvide_profitsharing_count_as_passenger() {
		return provide_profitsharing_count_as_passenger;
	}
	public void setProvide_profitsharing_count_as_passenger(
			int provide_profitsharing_count_as_passenger) {
		this.provide_profitsharing_count_as_passenger = provide_profitsharing_count_as_passenger;
	}
	public int getProvide_profitsharing_count_as_driver() {
		return provide_profitsharing_count_as_driver;
	}
	public void setProvide_profitsharing_count_as_driver(
			int provide_profitsharing_count_as_driver) {
		this.provide_profitsharing_count_as_driver = provide_profitsharing_count_as_driver;
	}
	public String getCity_register() {
		return city_register;
	}
	public void setCity_register(String city_register) {
		this.city_register = city_register;
	}
	public String getCity_cur() {
		return city_cur;
	}
	public void setCity_cur(String city_cur) {
		this.city_cur = city_cur;
	}

	
	
	public BigDecimal getRatio_as_passenger() {
		return ratio_as_passenger;
	}
	public void setRatio_as_passenger(BigDecimal ratio_as_passenger) {
		this.ratio_as_passenger = ratio_as_passenger;
	}
	public BigDecimal getInteger_as_passenger() {
		return integer_as_passenger;
	}
	public void setInteger_as_passenger(BigDecimal integer_as_passenger) {
		this.integer_as_passenger = integer_as_passenger;
	}
	public int getActiveway_as_driver() {
		return activeway_as_driver;
	}
	public void setActiveway_as_driver(int activeway_as_driver) {
		this.activeway_as_driver = activeway_as_driver;
	}
	public int getActiveway_as_passenger() {
		return activeway_as_passenger;
	}
	public void setActiveway_as_passenger(int activeway_as_passenger) {
		this.activeway_as_passenger = activeway_as_passenger;
	}
	public BigDecimal getRatio_as_driver() {
		return ratio_as_driver;
	}
	public void setRatio_as_driver(BigDecimal ratio_as_driver) {
		this.ratio_as_driver = ratio_as_driver;
	}
	public BigDecimal getInteger_as_driver() {
		return integer_as_driver;
	}
	public void setInteger_as_driver(BigDecimal integer_as_driver) {
		this.integer_as_driver = integer_as_driver;
	}
	public int getInviter_type() {
		return inviter_type;
	}
	public void setInviter_type(int inviter_type) {
		this.inviter_type = inviter_type;
	}
	public int getIs_platform() {
		return is_platform;
	}
	public void setIs_platform(int is_platform) {
		this.is_platform = is_platform;
	}
	public BigDecimal getRatio_as_passenger_self() {
		return ratio_as_passenger_self;
	}
	public void setRatio_as_passenger_self(BigDecimal ratio_as_passenger_self) {
		this.ratio_as_passenger_self = ratio_as_passenger_self;
	}
	public BigDecimal getInteger_as_passenger_self() {
		return integer_as_passenger_self;
	}
	public void setInteger_as_passenger_self(BigDecimal integer_as_passenger_self) {
		this.integer_as_passenger_self = integer_as_passenger_self;
	}
	public int getActive_as_passenger_self() {
		return active_as_passenger_self;
	}
	public void setActive_as_passenger_self(int active_as_passenger_self) {
		this.active_as_passenger_self = active_as_passenger_self;
	}
	public BigDecimal getRatio_as_driver_self() {
		return ratio_as_driver_self;
	}
	public void setRatio_as_driver_self(BigDecimal ratio_as_driver_self) {
		this.ratio_as_driver_self = ratio_as_driver_self;
	}
	public BigDecimal getInteger_as_driver_self() {
		return integer_as_driver_self;
	}
	public void setInteger_as_driver_self(BigDecimal integer_as_driver_self) {
		this.integer_as_driver_self = integer_as_driver_self;
	}
	public int getActive_as_driver_self() {
		return active_as_driver_self;
	}
	public void setActive_as_driver_self(int active_as_driver_self) {
		this.active_as_driver_self = active_as_driver_self;
	}
	public int getLimit_month_as_passenger_self() {
		return limit_month_as_passenger_self;
	}
	public void setLimit_month_as_passenger_self(int limit_month_as_passenger_self) {
		this.limit_month_as_passenger_self = limit_month_as_passenger_self;
	}
	public int getLimit_month_as_driver_self() {
		return limit_month_as_driver_self;
	}
	public void setLimit_month_as_driver_self(int limit_month_as_driver_self) {
		this.limit_month_as_driver_self = limit_month_as_driver_self;
	}
	public int getLimit_count_as_passenger_self() {
		return limit_count_as_passenger_self;
	}
	public void setLimit_count_as_passenger_self(int limit_count_as_passenger_self) {
		this.limit_count_as_passenger_self = limit_count_as_passenger_self;
	}
	public int getLimit_count_as_driver_self() {
		return limit_count_as_driver_self;
	}
	public void setLimit_count_as_driver_self(int limit_count_as_driver_self) {
		this.limit_count_as_driver_self = limit_count_as_driver_self;
	}
	public String getReallocateProfit() {
		return reallocateProfit;
	}
	public void setReallocateProfit(String reallocateProfit) {
		this.reallocateProfit = reallocateProfit;
	}
	public String getSubbranch() {
		return subbranch;
	}
	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	

	                      
	
}
