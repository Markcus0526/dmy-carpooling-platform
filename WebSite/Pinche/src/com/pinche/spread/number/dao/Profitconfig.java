package com.pinche.spread.number.dao;

public class Profitconfig {
	private int id;
	private double ratio_as_passenger;
	private double integer_as_passenger;
	private int active_as_passenger;
	private double ratio_as_driver;
	private double integer_as_driver;
	private int active_as_driver;
	private int limit_month_as_passenger;
	private int limit_month_as_driver;
	private int limit_count_as_passenger;
	private int limit_count_as_driver;
	
	private String usercode;
	private String username;
	private String nickname;
	private String password;
	private String phone;
	private long balance_ts;
	private String invitecode_self;
	private String invitecode_regist;
	private int app_register;
	private String bankcard;
	private String bankname;
	private String subbranch;
	private String account;
	private int sex;
	private int age;
	private String id_card1;
	private String driver_license1;
	private String driving_license1;
	private String id_card2;
	private String driver_license2;
	private String driving_license2;
	private String img;
	private String car_img;
	private int person_verified;
	private int driver_verified;
	private String plate_num;
	private String plat_num_last3;
	private int provide_profitsharing_time_as_passenger;
	private int provide_profitsharing_count_as_passenger;
	private int provide_profitsharing_time_as_driver;
	private int provide_profitsharing_count_as_driver;
	private String city_register;
	private String city_cur;
	private int activeway_as_passenger;
	private int activeway_as_driver;
	private int inviter_type;
	private int is_platform;
	private float ratio_as_passenger_self;
	private float integer_as_passenger_self;
	private int active_as_passenger_self;
	private float ratio_as_driver_self;
	private float integer_as_driver_self;
	private int active_as_driver_self;
	private int limit_month_as_passenger_self;
	private int limit_month_as_driver_self;
	private int limit_count_as_passenger_self;
	private int limit_count_as_driver_self;
	private int phone_system;
	private String device_token;
	private int deleted;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public double getRatio_as_passenger() {
		return ratio_as_passenger;
	}
	
	public void setRatio_as_passenger(double ratio_as_passenger) {
		this.ratio_as_passenger= ratio_as_passenger;
	}

	public double getInteger_as_passenger() {
		return integer_as_passenger;
	}
	
	public void setInteger_as_passenger(double integer_as_passenger) {
		this.integer_as_passenger = integer_as_passenger;
	}

	public int getActive_as_passenger() {
		return active_as_passenger;
	}
	
	public void setActive_as_passenger(int active_as_passenger) {
		this.active_as_passenger = active_as_passenger;
	}

	public double getRatio_as_driver() {
		return ratio_as_driver;
	}
	
	public void setRatio_as_driver(double ratio_as_driver) {
		this.ratio_as_driver = ratio_as_driver;
	}

	public double getInteger_as_driver() {
		return integer_as_driver;
	}
	
	public void setInteger_as_driver(double integer_as_driver) {
		this.integer_as_driver = integer_as_driver;
	}

	public int getActive_as_driver() {
		return active_as_driver;
	}
	
	public void setActive_as_driver(int active_as_driver) {
		this.active_as_driver = active_as_driver;
	}

	public int getLimit_month_as_passenger() {
		return limit_month_as_passenger;
	}
	
	public void setLimit_month_as_passenger(int limit_month_as_passenger) {
		this.limit_month_as_passenger = limit_month_as_passenger;
	}

	public int getLimit_month_as_driver() {
		return limit_month_as_driver;
	}
	
	public void setLimit_month_as_driver(int limit_month_as_driver) {
		this.limit_month_as_driver = limit_month_as_driver;
	}

	public int getLimit_count_as_passenger() {
		return limit_count_as_passenger;
	}
	
	public void setLimit_count_as_passenger(int limit_count_as_passenger) {
		this.limit_count_as_passenger = limit_count_as_passenger;
	}

	public int getLimit_count_as_driver() {
		return limit_count_as_driver;
	}
	
	public void setLimit_count_as_driver(int limit_count_as_driver) {
		this.limit_count_as_driver = limit_count_as_driver;
	}

	public int getDeleted() {
		return deleted;
	}
	
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getBalance_ts() {
		return balance_ts;
	}

	public void setBalance_ts(long balance_ts) {
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

	public int getApp_register() {
		return app_register;
	}

	public void setApp_register(int app_register) {
		this.app_register = app_register;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getSubbranch() {
		return subbranch;
	}

	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getId_card1() {
		return id_card1;
	}

	public void setId_card1(String id_card1) {
		this.id_card1 = id_card1;
	}

	public String getDriver_license1() {
		return driver_license1;
	}

	public void setDriver_license1(String driver_license1) {
		this.driver_license1 = driver_license1;
	}

	public String getDriving_license1() {
		return driving_license1;
	}

	public void setDriving_license1(String driving_license1) {
		this.driving_license1 = driving_license1;
	}

	public String getId_card2() {
		return id_card2;
	}

	public void setId_card2(String id_card2) {
		this.id_card2 = id_card2;
	}

	public String getDriver_license2() {
		return driver_license2;
	}

	public void setDriver_license2(String driver_license2) {
		this.driver_license2 = driver_license2;
	}

	public String getDriving_license2() {
		return driving_license2;
	}

	public void setDriving_license2(String driving_license2) {
		this.driving_license2 = driving_license2;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
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

	public String getPlate_num() {
		return plate_num;
	}

	public void setPlate_num(String plate_num) {
		this.plate_num = plate_num;
	}

	public String getPlat_num_last3() {
		return plat_num_last3;
	}

	public void setPlat_num_last3(String plat_num_last3) {
		this.plat_num_last3 = plat_num_last3;
	}

	public int getProvide_profitsharing_time_as_passenger() {
		return provide_profitsharing_time_as_passenger;
	}

	public void setProvide_profitsharing_time_as_passenger(
			int provide_profitsharing_time_as_passenger) {
		this.provide_profitsharing_time_as_passenger = provide_profitsharing_time_as_passenger;
	}

	public int getProvide_profitsharing_count_as_passenger() {
		return provide_profitsharing_count_as_passenger;
	}

	public void setProvide_profitsharing_count_as_passenger(
			int provide_profitsharing_count_as_passenger) {
		this.provide_profitsharing_count_as_passenger = provide_profitsharing_count_as_passenger;
	}

	public int getProvide_profitsharing_time_as_driver() {
		return provide_profitsharing_time_as_driver;
	}

	public void setProvide_profitsharing_time_as_driver(
			int provide_profitsharing_time_as_driver) {
		this.provide_profitsharing_time_as_driver = provide_profitsharing_time_as_driver;
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

	public int getActiveway_as_passenger() {
		return activeway_as_passenger;
	}

	public void setActiveway_as_passenger(int activeway_as_passenger) {
		this.activeway_as_passenger = activeway_as_passenger;
	}

	public int getActiveway_as_driver() {
		return activeway_as_driver;
	}

	public void setActiveway_as_driver(int activeway_as_driver) {
		this.activeway_as_driver = activeway_as_driver;
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

	public float getRatio_as_passenger_self() {
		return ratio_as_passenger_self;
	}

	public void setRatio_as_passenger_self(float ratio_as_passenger_self) {
		this.ratio_as_passenger_self = ratio_as_passenger_self;
	}

	public float getInteger_as_passenger_self() {
		return integer_as_passenger_self;
	}

	public void setInteger_as_passenger_self(float integer_as_passenger_self) {
		this.integer_as_passenger_self = integer_as_passenger_self;
	}

	public int getActive_as_passenger_self() {
		return active_as_passenger_self;
	}

	public void setActive_as_passenger_self(int active_as_passenger_self) {
		this.active_as_passenger_self = active_as_passenger_self;
	}

	public float getRatio_as_driver_self() {
		return ratio_as_driver_self;
	}

	public void setRatio_as_driver_self(float ratio_as_driver_self) {
		this.ratio_as_driver_self = ratio_as_driver_self;
	}

	public float getInteger_as_driver_self() {
		return integer_as_driver_self;
	}

	public void setInteger_as_driver_self(float integer_as_driver_self) {
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

	public int getPhone_system() {
		return phone_system;
	}

	public void setPhone_system(int phone_system) {
		this.phone_system = phone_system;
	}

	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	

}
