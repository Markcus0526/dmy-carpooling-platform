package com.nmdy.activity.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.nmdy.customerManage.appSpreadUnit.dao.App_Spread_UnitMapper;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.customerManage.user.dao.UserNewMapper;
import com.nmdy.financial.account.dao.TransactionDetail;
import com.nmdy.login.service.ILoginService;
import com.nmdy.spread.loan.dao.SingleCoupon;
import com.nmdy.spread.loan.dao.Syscoupon;
import com.nmdy.spread.loan.dao.Syscoupon1;
import com.nmdy.spread.loan.service.LoanService;
import com.nmdy.sysManage.administrator.pojo.Administrator;
import com.opensymphony.xwork2.ActionContext;

public class GameServiceImpl implements GameService{
	private  UserNewMapper userNewMapper;
	private App_Spread_UnitMapper app_Spread_UnitMapper;
	private LoanService loanService;
	private ILoginService loginService;
	
	
	public ILoginService getLoginService() {
		return loginService;
	}
	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}
	public void setUserNewMapper(UserNewMapper userNewMapper) {
		this.userNewMapper = userNewMapper;
	}
	public void setApp_Spread_UnitMapper(App_Spread_UnitMapper app_Spread_UnitMapper) {
		this.app_Spread_UnitMapper = app_Spread_UnitMapper;
	}
	
	public LoanService getLoanService() {
		return loanService;
	}
	public void setLoanService(LoanService loanService) {
		this.loanService = loanService;
	}
	public UserNewMapper getUserNewMapper() {
		return userNewMapper;
	}
	public App_Spread_UnitMapper getApp_Spread_UnitMapper() {
		return app_Spread_UnitMapper;
	}
	public int findLoginAdminId(){
		if(ActionContext.getContext().getSession().get("admin")==null) return 0;
		Administrator admin = (Administrator) ActionContext.getContext().getSession().get("admin");
		return admin.getId();
	}
	
	public List<User> listByCondition(String usercode,String username,Long id,String phone) throws UnsupportedEncodingException{
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(usercode)){
			map.put("usercode",usercode);
		}
		if(StringUtils.isNotEmpty(username)){
			username = new String(username.getBytes("ISO-8859-1"),"UTF-8");
			map.put("username",username);
		}
		if(null!=id&&id!=0){
			map.put("id",id);
		}
		if(StringUtils.isNotEmpty(phone)){
			map.put("phone",phone);
		}
		return userNewMapper.findUsersByConditionForGame(map);
	}
	
	public List<User> listByBatchUserIds(Long[] listIds){
		if(null==listIds) return null;
		List<Long> list = Arrays.asList(listIds);
		return userNewMapper.listByBatchUserIds(list);
	}
	
	public long countCityUserByTime(String cityName,String startTime,String endTime) throws UnsupportedEncodingException{
		Map<String,Object> map = new HashMap<String,Object>();
		cityName = new String(cityName.getBytes("ISO-8859-1"),"UTF-8");
		map.put("cityName",cityName);
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		return userNewMapper.countCityUserByTime(map);
	}
	
	public long countCityUserByCode(String cityName) throws UnsupportedEncodingException{
		cityName = new String(cityName.getBytes("ISO-8859-1"),"UTF-8");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cityName",cityName);
		return userNewMapper.countCityUserByTime(map);
	}
	
	public boolean giveUserMoney(Integer[] listUserId,float balance,int id){
		if(null==listUserId) return false;
		if(balance!=0){
			for(Integer userid:listUserId){
				try {
					TransactionDetail t = app_Spread_UnitMapper.findByMaxUserId(userid);
					if(t==null){
						t.setUserid(userid);
						t.setBalance(balance);
						t.setSum(t.getSum()+balance);
						app_Spread_UnitMapper.insertTransactionDetail(t);
					}else{
						TransactionDetail tt = new TransactionDetail();
						tt.setUserid(userid);
						tt.setBalance(balance);
						tt.setSum(balance);
						app_Spread_UnitMapper.insertTransactionDetail(tt);
					}
				} catch (Exception e) {
					return false;
				}
				//向notify_persion表里插入数据
				Map<String,Object> mm1=new HashMap<String,Object>();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				mm1.put("msg", "发放绿点");
				mm1.put("id", id);
				mm1.put("reciver", userid);
				Date date1=new Date();
				mm1.put("ps_date", sdf.format(date1));
				loanService.insertNotifyPersion(mm1);
			}
			return true;
		}
	
		
		if(id!=0){
			Syscoupon1 syscoupon=loanService.findSyscouponById(id);
			
			for(Integer uid:listUserId){
				String userid = String.valueOf(uid);
				System.out.println("userid is:"+userid);
				SingleCoupon singleCoupon = new SingleCoupon();
				Map<String,Object> params=new HashMap<String,Object>();
				params.put("syscoupon_id", id);
				singleCoupon.setSyscouponId(id);
				params.put("userid",userid);
				singleCoupon.setUserid(Long.parseLong(userid));
				Date date =new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				params.put("ps_date", sdf.format(date));
				singleCoupon.setPsDate(sdf.format(date));
				if(syscoupon.getValidPeriodUnit()==1){
					 String time= getTime(1,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==2){
					 String time= getTime(2,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==3){
					 String time= getTime(3,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}if(syscoupon.getValidPeriodUnit()==4){
					 String time= getTime(4,syscoupon.getValidPeriod(),date);
					 params.put("date_expired", time);
					 singleCoupon.setDateExpired(time);
				}
				singleCoupon.setSum(syscoupon.getSum());
				SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMddhhmmssSSS");
				params.put("coupon_code", "SICP"+sdf1.format(date));
				singleCoupon.setSingleCouponCode("SICP"+sdf1.format(date));
				System.out.println(params);
				loanService.insertSingleCoupon(singleCoupon);
				Map<String,Object> mm=new HashMap<String,Object>();
				
				//向notify_persion表里插入数据
				Map<String,Object> mm1=new HashMap<String,Object>();
				mm1.put("msg", "发放点卷");
				mm1.put("id", id);
				mm1.put("reciver", userid);
				Date date1=new Date();
				mm1.put("ps_date", sdf.format(date1));
				loanService.insertNotifyPersion(mm1);
			}
		}
		return true;
	}
	
	
	public boolean sendMsg(long userId,String tip_name){
		if(0==userId) return false;
		//向notify_persion表里插入数据
		Map<String,Object> mm1=new HashMap<String,Object>();
		mm1.put("msg", "恭喜您！您获取的彩票是"+tip_name);
		mm1.put("id", 0);
		mm1.put("reciver", String.valueOf(userId));
		Date date1=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mm1.put("ps_date", sdf.format(date1));
		loanService.insertNotifyPersion(mm1);
		return true;
	}
	
	
	public List<Syscoupon> findSyscoupon1Condition(int id,int num,int month,String code){
		Map<String,Object> map = new HashMap<>();
		map.put("id", id);
		map.put("num", num);
		map.put("month", month);
		map.put("code", code);
		return loanService.findByConditionForGame(map);
	}
	
	public boolean login(String username,String password){
		return loginService.isUserPassword(username, password);
	}
	
	
	/*
	 * 获取有效期限的方法
	 */
	private String getTime(int data, int validPeriod,Date date) {
		String time="";
		 Calendar c = Calendar.getInstance();         
		 c.setTime(date);           
		 int dDay   =  c.get(Calendar.DATE);     
		 int dYear  =  c.get(Calendar.YEAR);      
		 int dMonth =  c.get(Calendar.MONTH) + 1;  //注意month是从0开始计的我们加1    
		 int days=0;             int[] months={0,31,28,31,30,31,30,31,31,30,31,30,31};     
		 if(dYear%400==0||dYear%4==0&&dYear%100!=0){       
			 months[2]=29;           
			 }     
		 if(data==1){
		 int day=dDay+validPeriod;
		 int month=dMonth;
		 int year=dYear;
		 if(day>months[dMonth]){
			 day=day-months[dMonth];
			 month=dMonth+1;
		 }if(month>12){
			 month=month-1;
			 year=dYear+1;
		 }	
		  time=year+"-"+month+"-"+day;
		 } if(data==2){
			 int day=dDay+validPeriod*7;
			 int month=dMonth;
			 int year=dYear;
			 if(day>months[dMonth]){
				 day=day-months[dMonth];
				 month=dMonth+1;
			 }if(month>12){
				 month=month-12;
				 year=dYear+1;
			 } 
			  time=year+"-"+month+"-"+day;
		 }if(data==2){
			 int day=dDay;
			 int year=dYear;
			 int month=dMonth+validPeriod;
			 if(month>12){
				 month=month-12;
				 year=dYear+1;
			 } 
			  time=year+"-"+month+"-"+day;
		 }if(data==2){
			 int day=dDay;
			 int month=dMonth;
			 int year=dYear+validPeriod;
			 if(month>12){
				 month=month-12;
				 year=dYear+1;
			 } 
			  time=year+"-"+month+"-"+day;
		 }
		
		 return time;
	}
}
