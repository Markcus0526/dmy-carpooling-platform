/**
 * author:王锦成
 */
package test;
import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.nmdy.entity.Juese;
import com.nmdy.sysManage.role.service.JueseService;


public class UserServiceTest extends TestCase {
	public ApplicationContext factory = null;
	private static String[] CONFIG_FILES={"file:WebContent/WEB-INF/config/applicationContext.xml"};
	public void setUp(){
		factory = new FileSystemXmlApplicationContext(CONFIG_FILES);
	}
	
	public void testActivity(){
		JueseService jueseService = (JueseService)factory.getBean("jueseService");
		Juese j = jueseService.findById(13);
		System.out.println(j.getName());
	}
	
	
	
//	public void testHashMatchUser(){
//		GameService gameService = (GameService)factory.getBean("gameService");
//		List<User> list = gameService.listByCondition(null,null,6l,null);
//		System.out.println("list is:"+list);
//		
//		Long[] lists = new Long[]{6l,14l,25l};
//		List<User> list1 = gameService.listByBatchUserIds(lists);
//		System.out.println("list1 is:"+list1);
//		
//		String startTime = "2014-10-23 10:45:46";
//		String endTime = "2014-10-23 15:53:16";
//		Long s = gameService.countCityUserByTime("2301", startTime, endTime);
//		System.out.println("s:"+s);
//		
//		List<Syscoupon> list3 = gameService.findSyscoupon1Condition(13,0,0);
//		System.out.println("list3:"+list3);
//		
//		System.out.println(gameService.login("admin", "123"));
//		
//		Integer[] list5 = new Integer[]{25,26,27,28,29};
//		System.out.println(gameService.giveUserMoney(list5, 50, 0));
//		System.out.println(gameService.giveUserMoney(list5, 0,14));
//		
//	}
	
//	public void testUser(){
//		AdministratorService administratorService= (AdministratorService)factory.getBean("administratorService");
//		List<Administrator> list = new ArrayList<>();
//		list = administratorService.searchByUsercode("");
//		System.out.println(list);
//		
//	}
	
//	public void testArtivity(){
//		ActivityService activityService= (ActivityService)factory.getBean("activityService");
//		Activity activity = activityService.findById(1l);
//		System.out.println(activity.getListActivitySyscoupon().get(1).getActivyityRule().getId());
//	}
//	
//	public void testFindUserByUserName(){
//		UserService userService = (UserService)factory.getBean("userService");
//		User user = userService.finduserByUserName("admin");
//		assertEquals(user.getUserName(),"admin");
//	}
//	
//	public void testAddLoginLog(){
//		UserService userService = (UserService)factory.getBean("userService");
//		User user = userService.finduserByUserName("admin");
//		user.setUserId(1);
//		user.setUserName("admin");
//		user.setLastIp("192.168.1.1");
//		user.setLastVisit(new Date());
//		userService.loginSuccess(user);
//	}
	 
}
