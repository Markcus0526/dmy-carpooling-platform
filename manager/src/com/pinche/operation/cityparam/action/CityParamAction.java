package com.pinche.operation.cityparam.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.tomcat.jni.Time;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.pinche.operation.cityparam.dao.CityParam;
import com.pinche.operation.cityparam.service.CityParamService;
/**
 * 此类是对城市拼车参数进行设定的Action
 * @author 肖娜娜(xcnana)
 *
 */
public class CityParamAction {
   
	private CityParamService cityParamService;
	private JSONObject resultObject;
	private String name=null;
	private long id;
	private String code;
	private String prov;
	private int level;
	private long platform;
	private double ratio;
	private double integer;
	private int active;
	private int branch;
	private int turn1_time;
	private int turn2_time;
	private int total_time;
	private int driver_lock_time;
	private double range1;
	private double  range2;
	private double range3;
	private int points_per_add_ratio;
	private int points_per_add_integer;
	private int points_per_add_active;
	private int price_limit_ratio;
	private int price_limit_integer;
	private int price_limit_active;
	private double a1;
	private double a2;
	private double b1;
	private double b2;
	private double b4;
	private double c1;
	private double c2;
	private String t1="00:00:00";
	private String t2="00:00:00";
	private double d1;
	private double d2;
	private double e1;
	private double e2;
	private double e4;
	private double f1;
	private double f2;
	private double g1;
	private double g2;
	private double g3;
	private double g4;
	private double g5;
	private int add_price_time1;
	private int add_price_time2;
	private int add_price_time3;
	private int add_price_time4;
	private int add_price_time5;
	private int same_price_time1;
	private int same_price_time2;
	private int same_price_time3;
	private int same_price_time4;
	private int same_price_time5;
    private double msg;
    private int high=0;
    private int low=0;
	private int city;
	/*
	 * 对城市参数设定的首页面的跳转
	 */
	public String index(){
		ActionContext.getContext().put("name", "全局");
		return "success";
	}
	 /*
	  * 根据城市查询数据显示的页面上
	  * 城市为空时查询的是全局数据
	  * 如果 城市查询的数据参数 为 -1 时 此参数就 用全局参数进行替代
	  */
	public String find(){
		System.out.println("city="+city+"  name="+name);
	
		 CityParam cityParam=null;
		 
		if(city==2){
		 if(!name.equals("")){
			 
		 cityParam = cityParamService.find(name);
		 CityParam cityParam2 = cityParamService.findglobal(); 
		 if(cityParam.getA1()==-1){
			 cityParam.setA1(cityParam2.getA1());
		 }
		 if(cityParam.getA2()==-1){
			 cityParam.setA2(cityParam2.getA2());
		 }
		 if(cityParam.getActive()==-1){
			 cityParam.setActive(cityParam2.getActive());
		 }
		 if(cityParam.getB1()==-1){
			 cityParam.setB1(cityParam2.getB1());
		 }
		 if(cityParam.getB2()==-1){
			 cityParam.setB2(cityParam2.getB2());
		 }
		 if(cityParam.getB4()==-1){
			 cityParam.setB4(cityParam2.getB4());
		 }
		 if(cityParam.getBranch()==-1){
			 cityParam.setBranch(cityParam2.getBranch());
		 }
		 if(cityParam.getC1()==-1){
			 cityParam.setC1(cityParam2.getC1());
		 }
		 if(cityParam.getC2()==-1){
			 cityParam.setC2(cityParam2.getC2());
		 }
		 if(cityParam.getD1()==-1){
			 cityParam.setD1(cityParam2.getD1());
		 }
		 if(cityParam.getD2()==-1){
			 cityParam.setD2(cityParam2.getD2());
		 }
		 if(cityParam.getDriver_lock_time()==-1){
			 cityParam.setDriver_lock_time(cityParam2.getDriver_lock_time());
		 }
		 if(cityParam.getE1()==-1){
			 cityParam.setE1(cityParam2.getE1());
		 }
		 if(cityParam.getE2()==-1){
			 cityParam.setE2(cityParam2.getE2());
		 }
		 if(cityParam.getE4()==-1){
			 cityParam.setE4(cityParam2.getE4());
		 }
		 if(cityParam.getF1()==-1){
			 cityParam.setF1(cityParam2.getF1());
		 }
		 if(cityParam.getF2()==-1){
			 cityParam.setF2(cityParam2.getF2());
		 }
		 if(cityParam.getG1()==-1){
			 cityParam.setG1(cityParam2.getG1());
		 }
		 if(cityParam.getG2()==-1){
			 cityParam.setG2(cityParam2.getG2());
		 }
		 if(cityParam.getG3()==-1){
			 cityParam.setG3(cityParam2.getG3());
		 }
		 if(cityParam.getG4()==-1){
			 cityParam.setG4(cityParam2.getG4());
		 }
		 if(cityParam.getG5()==-1){
			 cityParam.setG5(cityParam2.getG5());
		 }
		 if(cityParam.getRange1()==-1){
			 cityParam.setRange1(cityParam2.getRange1());
		 }
		 if(cityParam.getRange2()==-1){
			 cityParam.setRange2(cityParam2.getRange2());
		 }
		 if(cityParam.getRange3()==-1){
			 cityParam.setRange3(cityParam2.getRange3());
		 }
		 
		 if(cityParam.getInteger_()==-1){
			 cityParam.setInteger_(cityParam2.getInteger_());
		 }
		 if(cityParam.getRatio()==-1){
			 cityParam.setRatio(cityParam2.getRatio());
		 }
		 if(cityParam.getPoints_per_add_active()==-1){
			 cityParam.setPoints_per_add_active(cityParam2.getPoints_per_add_active());
		 }
		 if(cityParam.getPoints_per_add_integer()==-1){
			 cityParam.setPoints_per_add_integer(cityParam2.getPoints_per_add_integer());
		 }
		 if(cityParam.getPoints_per_add_ratio()==-1){
			 cityParam.setPoints_per_add_ratio(cityParam2.getPoints_per_add_ratio());
		 }
		 if(cityParam.getPrice_limit_active()==-1){
			 cityParam.setPrice_limit_active(cityParam2.getPrice_limit_active());
		 }
		 if(cityParam.getPrice_limit_integer()==-1){
			 cityParam.setPrice_limit_integer(cityParam2.getPrice_limit_integer());
		 }
		 if(cityParam.getPrice_limit_ratio()==-1){
			 cityParam.setPrice_limit_ratio(cityParam2.getPrice_limit_ratio());
		 }
		 if(cityParam.getTotal_time()==-1){
			 cityParam.setTotal_time(cityParam2.getTotal_time());
		 }
		 if(cityParam.getTurn1_time()==-1){
			 cityParam.setTurn1_time(cityParam2.getTurn1_time());
		 }
		 if(cityParam.getTurn2_time()==-1){
			 cityParam.setTurn2_time(cityParam2.getTurn2_time());
		 }
		 }else{
			name="";
		  cityParam = cityParamService.findglobal(); 
		 }
		}else{
			name="";
		  cityParam = cityParamService.findglobal();
		}
      if(cityParam.getCode()==null){
    	  cityParam.setCode("");
      }
      if(cityParam.getName()==null){
    	  cityParam.setName("");;
      }
	resultObject = JSONObject.fromObject(cityParam);
		return "success";
	}
	/*
	 *  对前台页面修改的数据进行保存
	 *  本质是修改数据库中的数据 update
	 */
	public String save1(){
		System.out.println(low+"     "+high);
		
			Map<String,Object> params=new HashMap<String ,Object>();
			params.put("name", name);
			params.put("total_time", total_time);
			params.put("driver_lock_time", driver_lock_time);
			params.put("range1", range1);
			params.put("range2", range2);
			params.put("range3", range3);
			params.put("turn1_time", turn1_time);
			params.put("turn2_time", turn2_time);
			params.put("active", active);
			params.put("a1", a1);
			params.put("a2", a2);
			params.put("b1", b1);
			params.put("b2", b2);
			params.put("b4", b4);
			params.put("c1", c1);
			params.put("c2", c2);
			params.put("e1", e1);
			params.put("e2", e2);
			params.put("e4", e4);
			params.put("g1", g1);
			params.put("g2", g2);
			params.put("g3", g3);
			params.put("g4", g4);
			params.put("g5", g5);
			params.put("t1", t1);
			params.put("t2", t2);
			params.put("d1", d1);
			params.put("d2", d2);
			params.put("f1", f1);
			params.put("f2", f2);
			params.put("add_price_time1", add_price_time1);
			params.put("add_price_time2", add_price_time2);
			params.put("add_price_time3", add_price_time3);
			params.put("add_price_time4", add_price_time4);
			params.put("add_price_time5", add_price_time5);
			params.put("same_price_time1", same_price_time1);
			params.put("same_price_time2", same_price_time2);
			params.put("same_price_time3", same_price_time3);
			params.put("same_price_time4", same_price_time4);
			params.put("same_price_time5", same_price_time5);
			params.put("points_per_add_active", points_per_add_active);
			params.put("price_limit_active", price_limit_active);
			if(points_per_add_active==0){
				params.put("points_per_add_ratio", high);
			}if(points_per_add_active==1){
				params.put("points_per_add_integer", high);
			}	
			if(price_limit_active==0){
				params.put("price_limit_ratio", low);
			}if(price_limit_active==1){
				params.put("price_limit_integer", low);
			}
			if(active==0){
				params.put("ratio", msg);
			}if(active==1){
				params.put("integer_", msg);
			}
			System.out.println(params);
			if(name.equals("全局")){
				cityParamService.saveGloble(params);
			}else{
	
		 cityParamService.save(params);
			}
	  ActionContext.getContext().put("name", name);
		return "success";
	}
	
	/*
	 * getter 和 setter 方法
	 */
	public CityParamService getCityParamService() {
		return cityParamService;
	}
	public void setCityParamService(CityParamService cityParamService) {
		this.cityParamService = cityParamService;
	}
	public JSONObject getResultObject() {
		return resultObject;
	}
	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getPlatform() {
		return platform;
	}
	public void setPlatform(long platform) {
		this.platform = platform;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public double getInteger() {
		return integer;
	}
	public void setInteger(double integer) {
		this.integer = integer;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getBranch() {
		return branch;
	}
	public void setBranch(int branch) {
		this.branch = branch;
	}
	public int getTurn1_time() {
		return turn1_time;
	}
	public void setTurn1_time(int turn1_time) {
		this.turn1_time = turn1_time;
	}
	public int getTurn2_time() {
		return turn2_time;
	}
	public void setTurn2_time(int turn2_time) {
		this.turn2_time = turn2_time;
	}
	public int getTotal_time() {
		return total_time;
	}
	public void setTotal_time(int total_time) {
		this.total_time = total_time;
	}
	public int getDriver_lock_time() {
		return driver_lock_time;
	}
	public void setDriver_lock_time(int driver_lock_time) {
		this.driver_lock_time = driver_lock_time;
	}
	public double getRange1() {
		return range1;
	}
	public void setRange1(double range1) {
		this.range1 = range1;
	}
	public double getRange2() {
		return range2;
	}
	public void setRange2(double range2) {
		this.range2 = range2;
	}
	public double getRange3() {
		return range3;
	}
	public void setRange3(double range3) {
		this.range3 = range3;
	}
	public int getPoints_per_add_ratio() {
		return points_per_add_ratio;
	}
	public void setPoints_per_add_ratio(int points_per_add_ratio) {
		this.points_per_add_ratio = points_per_add_ratio;
	}
	public int getPoints_per_add_integer() {
		return points_per_add_integer;
	}
	public void setPoints_per_add_integer(int points_per_add_integer) {
		this.points_per_add_integer = points_per_add_integer;
	}
	public int getPoints_per_add_active() {
		return points_per_add_active;
	}
	public void setPoints_per_add_active(int points_per_add_active) {
		this.points_per_add_active = points_per_add_active;
	}
	public int getPrice_limit_ratio() {
		return price_limit_ratio;
	}
	public void setPrice_limit_ratio(int price_limit_ratio) {
		this.price_limit_ratio = price_limit_ratio;
	}
	public int getPrice_limit_integer() {
		return price_limit_integer;
	}
	public void setPrice_limit_integer(int price_limit_integer) {
		this.price_limit_integer = price_limit_integer;
	}
	public int getPrice_limit_active() {
		return price_limit_active;
	}
	public void setPrice_limit_active(int price_limit_active) {
		this.price_limit_active = price_limit_active;
	}
	public double getA1() {
		return a1;
	}
	public void setA1(double a1) {
		this.a1 = a1;
	}
	public double getA2() {
		return a2;
	}
	public void setA2(double a2) {
		this.a2 = a2;
	}
	public double getB1() {
		return b1;
	}
	public void setB1(double b1) {
		this.b1 = b1;
	}
	public double getB2() {
		return b2;
	}
	public void setB2(double b2) {
		this.b2 = b2;
	}
	public double getB4() {
		return b4;
	}
	public void setB4(double b4) {
		this.b4 = b4;
	}
	public double getC1() {
		return c1;
	}
	public void setC1(double c1) {
		this.c1 = c1;
	}
	public double getC2() {
		return c2;
	}
	public void setC2(double c2) {
		this.c2 = c2;
	}
	public double getE1() {
		return e1;
	}
	public void setE1(double e1) {
		this.e1 = e1;
	}
	public double getE2() {
		return e2;
	}
	public void setE2(double e2) {
		this.e2 = e2;
	}
	public double getE4() {
		return e4;
	}
	public void setE4(double e4) {
		this.e4 = e4;
	}
	public double getG1() {
		return g1;
	}
	public void setG1(double g1) {
		this.g1 = g1;
	}
	public double getG2() {
		return g2;
	}
	public void setG2(double g2) {
		this.g2 = g2;
	}
	public double getG3() {
		return g3;
	}
	public void setG3(double g3) {
		this.g3 = g3;
	}
	public double getG4() {
		return g4;
	}
	public void setG4(double g4) {
		this.g4 = g4;
	}
	public double getG5() {
		return g5;
	}
	public void setG5(double g5) {
		this.g5 = g5;
	}
	public double getMsg() {
		return msg;
	}
	public void setMsg(double msg) {
		this.msg = msg;
	}

	public int getHigh() {
		return high;
	}
	public void setHigh(int high) {
		this.high = high;
	}
	public int getLow() {
		return low;
	}
	public void setLow(int low) {
		this.low = low;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}

	public String getT1() {
		return t1;
	}
	public void setT1(String t1) {
		this.t1 = t1;
	}
	public String getT2() {
		return t2;
	}
	public void setT2(String t2) {
		this.t2 = t2;
	}
	public double getD1() {
		return d1;
	}
	public void setD1(double d1) {
		this.d1 = d1;
	}
	public double getD2() {
		return d2;
	}
	public void setD2(double d2) {
		this.d2 = d2;
	}
	public double getF1() {
		return f1;
	}
	public void setF1(double f1) {
		this.f1 = f1;
	}
	public double getF2() {
		return f2;
	}
	public void setF2(double f2) {
		this.f2 = f2;
	}
	public int getAdd_price_time1() {
		return add_price_time1;
	}
	public void setAdd_price_time1(int add_price_time1) {
		this.add_price_time1 = add_price_time1;
	}
	public int getAdd_price_time2() {
		return add_price_time2;
	}
	public void setAdd_price_time2(int add_price_time2) {
		this.add_price_time2 = add_price_time2;
	}
	public int getAdd_price_time3() {
		return add_price_time3;
	}
	public void setAdd_price_time3(int add_price_time3) {
		this.add_price_time3 = add_price_time3;
	}
	public int getAdd_price_time4() {
		return add_price_time4;
	}
	public void setAdd_price_time4(int add_price_time4) {
		this.add_price_time4 = add_price_time4;
	}
	public int getAdd_price_time5() {
		return add_price_time5;
	}
	public void setAdd_price_time5(int add_price_time5) {
		this.add_price_time5 = add_price_time5;
	}
	public int getSame_price_time1() {
		return same_price_time1;
	}
	public void setSame_price_time1(int same_price_time1) {
		this.same_price_time1 = same_price_time1;
	}
	public int getSame_price_time2() {
		return same_price_time2;
	}
	public void setSame_price_time2(int same_price_time2) {
		this.same_price_time2 = same_price_time2;
	}
	public int getSame_price_time3() {
		return same_price_time3;
	}
	public void setSame_price_time3(int same_price_time3) {
		this.same_price_time3 = same_price_time3;
	}
	public int getSame_price_time4() {
		return same_price_time4;
	}
	public void setSame_price_time4(int same_price_time4) {
		this.same_price_time4 = same_price_time4;
	}
	public int getSame_price_time5() {
		return same_price_time5;
	}
	public void setSame_price_time5(int same_price_time5) {
		this.same_price_time5 = same_price_time5;
	}

	
	
}
