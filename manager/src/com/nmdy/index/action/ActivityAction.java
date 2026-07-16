
package com.nmdy.index.action;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import util.CommonUtil;

import com.nmdy.activity.service.ActivityService;
import com.nmdy.activity.service.GameService;
import com.nmdy.customerManage.user.dao.User;
import com.nmdy.dto.ActivityDto;
import com.nmdy.entity.Activity;
import com.nmdy.entity.ActivityRule;
import com.nmdy.entity.ActivityUser;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.Constants;

/**
 * APP，微信活动
 */
public class ActivityAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private ActivityService activityService;
	private ActivityDto activityDto = new ActivityDto();
	private GameService gameService;
	private long userId;
	private long viewHitsTime;
	private static final String url =  "http://182.92.237.144:8082/PincheService/webservice/getVerifKey";
	private static final String url1 = "http://182.92.237.144:8082/PincheService/webservice/getVerifKey";
	private static final String url2 = "http://182.92.237.144:8082/PincheService/webservice/registerUserByActivity";
//	private static final String url = "http://124.207.135.69:8080/PincheService/webservice/getVerifKey";
//	private static final String url1 = "http://124.207.135.69:8080/PincheService/webservice/getVerifKey";
//	private static final String url2 = "http://124.207.135.69:8080/PincheService/webservice/registerUserByActivity";
	private JSONObject resultObj;
	
	
	/**
	 * 用户活动
	 * @return
	 * http://localhost:8080/bk/index/activity_appFx.do?activityDto.userId=46
	 */
	public String appFx(){
		System.out.println("userid====="+activityDto.getUserId());
		User user = this.activityService.findUserById(activityDto.getUserId());
		activityDto.setInvitecode_self(user.getInvitecode_self());
		int registerId = this.activityService.countAllUserActivity(activityDto.getUserId());
//		int registerId = this.activityService.countUserFx(user.getInvitecode_self());
		activityDto.setUserdNum(registerId);
		return "appFx";
	}
	
	public String getMsg(){
		System.out.println("*******begin**************");
//		String result = CommonUtil.sendPost(url,"source=11&username="+activityDto.getUsercode()+"&mobile="+activityDto.getUsercode()+"&registered=1");
		String result = CommonUtil.sendPost(url1,"source=11&username="+activityDto.getUsercode()+"&mobile="+activityDto.getUsercode()+"&registered=1");
		resultObj = JSONObject.fromObject(result);
		System.out.println("result:"+resultObj);
		return "getMsg";
	}
	
	/**
	 * 用户创建新分享
	 * @return
	 */
	public String createFx(){
		activityDto.setActivity(this.activityService.findActivity());
		activityDto.setFaqiId(activityDto.getUserId());
		this.setActivityDto(activityDto);
		if(this.activityService.countFaqi(activityDto.getUserId())==0){
			return "createFx";
		}
		return "faqi";
	}
	
	/**
	 * parm:faqiId
	 * 立刻发布我的宝箱
	 * @return
	 */
	public String showFx(){
		activityDto.setActivity(this.activityService.findActivity());
		activityDto.setActivityId(this.activityDto.getActivity().getId());
		long fid = activityService.booleanFaqiActivity(activityDto);
		String retString = "";
		if(fid==0){
			activityDto.setUserId(activityDto.getFaqiId());
			activityDto.setAddtime(new Date());
			this.activityService.insertUserActivityReturnId(activityDto);
			System.out.println("faqid11 ::"+activityDto.getFaqiId());
			Date faqiTime = this.activityService.findFaqiTime(activityDto.getUserId());
			long haomiao = System.currentTimeMillis()-faqiTime.getTime();
			viewHitsTime = this.activityDto.getActivity().getShareTime()*60*60*1000-haomiao;
			
			retString = "faqi";
		
		}else{
			List<ActivityUser> list = this.activityService.findActivityUser(fid);
			activityDto.setListActivityUsers(list);
			int registerId = this.activityService.countAllUserActivity(activityDto.getFaqiId());
			activityDto.setUserdNum(registerId);
			Date faqiTime = this.activityService.findFaqiTime(activityDto.getFaqiId());
			long haomiao = System.currentTimeMillis()-faqiTime.getTime();
			viewHitsTime = this.activityDto.getActivity().getShareTime()*60*60*1000-haomiao;
			retString = "showFx";
		}
		return retString;
	}
	
	/**
	 * 设置我的活动过期
	 */
	public String guoqi(){
		this.activityService.updateUserActivity(activityDto.getFaqiId());
		return "guoqi";
	}
	
	/**
	 * 继续分享页面
	 */
	public String goonFx(){
		this.setActivityDto(activityDto);
		return "goonFx";
	}
	
	/**
	 * 查找我的奖品
	 * @return
	 */
	public String findMyGift(){
		activityDto.setListActivityGift(this.activityService.findMyGift(activityDto.getUserId()));
		return "findMyGift";
	}
	
	/**
	 * 朋友访问URL         activityDto.faqiId=2
	 * @return
	 */
	public String friendVisitUrl(){
		int registerId = this.activityService.countUserActivity(activityDto.getFaqiId());
		activityDto.setUserdNum(registerId);
		activityDto.setActivity(this.activityService.findActivity());
		if(activityDto.getActivity().getShareTime()==0){
			return "friendVisitBadUrl";
		}
		Date faqiTime = this.activityService.findFaqiTime(activityDto.getFaqiId());
		long haomiao = 0;
		if(faqiTime!=null){
			 haomiao = System.currentTimeMillis()-faqiTime.getTime();
		}
		viewHitsTime = this.activityDto.getActivity().getShareTime()*60*60*1000-haomiao;
		return "friendVisitUrl";
	}
	
	/**
	 * 注册页面
	 * @return
	 */
	public String register(){
		this.setActivityDto(activityDto);
		return "register";
	}
	
	public String registerUser(){
		System.out.println("*********egin*************8");
		String invitecode = this.activityService.findRegistCode(activityDto.getFaqiId());
		String param = "source=13&mobile="+this.activityDto.getUsercode()+"&invitecode=" +invitecode+"&sex="+activityDto.getSex()+"&password="+activityDto.getPassword()+"&nickname="+activityDto.getNickname();
		String result = CommonUtil.sendPost(url2,param);
		System.out.println(result);
		resultObj = JSONObject.fromObject(result);
		return "registerUser";
	}
	
	/**
	 * faqiId
	 * 注册
	 * @return
	 */
	public String insertUser(){
		System.out.println("***"+activityDto.getFaqiId());
		activityDto.setAddtime(new Date());
		System.out.println("huojiangId:"+activityDto.getHuojiangId());
		Activity activity = this.activityService.findActivity();
		System.out.println("activity id:"+activity.getId());
		int registerId = this.activityService.countUserActivity(activityDto.getFaqiId());
		registerId++;
		if(activity.getSharePeople()<=registerId){
			this.activityService.updateUserActivity(activityDto.getFaqiId());
			return "insertOver";
		}
		activityDto.setFid(this.activityService.findFaqiId(activityDto.getFaqiId()));
		//说明 是彩票 
		if(activity.getPrizeType()==1){
			activityDto.setStyle(1);
			activityDto.setGiftName(activity.getLotteryName());
			this.activityService.insertActivityHuojiangUser(activityDto);
			System.out.println("huojiangId:"+activityDto.getHuojiangId());
			activityDto.setActivityId(activity.getId());
			activityDto.setType(1);
			activityDto.setAddtime(new Date());
			this.activityService.insertTip(activityDto);
			String tip_name = this.activityService.findAndUpdateTip(activityDto.getHuojiangId(), 0);
			if("error".equals(tip_name)){
				return "insertOver";
			}
			this.gameService.sendMsg(activityDto.getHuojiangId(), tip_name);
			this.activityDto.setTip_name(tip_name);
			return "insertTipUser";
		}else{
			List<ActivityRule> listAr = this.activityService.listActivityRole(activity.getId());
			for(ActivityRule ar:listAr){
				if(registerId>=ar.getStartNum()&&registerId<=ar.getEndNum()){
					activityDto.setActivityCouponId(ar.getCouponId());
					activityDto.setGiftName(ar.getSyscouponCode());
					break;
				}
			}
			activityDto.setStyle(0);
			this.activityService.insertActivityHuojiangUser(activityDto);
			System.out.println("huojiangId:"+activityDto.getHuojiangId());
			Integer[] uuid = new Integer[]{Integer.parseInt(String.valueOf(activityDto.getHuojiangId()))};
			gameService.giveUserMoney(uuid,0,Integer.parseInt(String.valueOf(activityDto.getActivityCouponId())));
		}
		return "insertUser";
	}
	
	/**
	 * 用户登陆
	 * @return
	 */
	public String login(){
		if(!this.activityService.login(activityDto.getUsercode(),activityDto.getPassword())){
			activityDto.setMsg(Constants.LOGIN_ERROR);
			return "loginWeb";
		}
		User u = this.activityService.findUser(activityDto.getUsercode());
		activityDto.setUserId(u.getId());
		return "login";
	}
	
	public String loginWeb(){
		return "loginWeb";
	}
	
	public ActivityDto getActivityDto() {
		return activityDto;
	}
	public void setActivityDto(ActivityDto activityDto) {
		this.activityDto = activityDto;
	}
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getViewHitsTime() {
		return viewHitsTime;
	}

	public void setViewHitsTime(long viewHitsTime) {
		this.viewHitsTime = viewHitsTime;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
}
