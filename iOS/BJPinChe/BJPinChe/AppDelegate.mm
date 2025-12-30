//
//  AppDelegate.m
//  BJPinChe
//
//  Created by KimOC on 8/11/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "AppDelegate.h"

#import "UMSocial.h"
#import "UMSocialYixinHandler.h"
#import "UMSocialWechatHandler.h"
#import "UMSocialQQHandler.h"
#import "UMSocialSinaHandler.h"
#import "UMSocialTencentWeiboHandler.h"
#import "UMSocialRenrenHandler.h"

//统计
#import "MobClick.h"

//#import "YXApi.h"
#import "Config.h"
#import "iflyMSC/iflySetting.h"
#import "iflyMSC/IFlySpeechUtility.h"

#import "BPush.h"
#import "JSONKit.h"
#import "Cus_MainTabViewController.h"
#import "Drv_MainTabViewController.h"
#import "Drv_OrderPerformViewController.h"
#import "Cus_STOrderConfirmViewController.h"
#import "LoginVC.h"
#import "SoundTools.h"
#import <AudioToolbox/AudioToolbox.h>
#define SUPPORT_IOS8 1  //注意！！！！！！！！！！！！！！！！！！！
@implementation AppDelegate
- (void)initializePlat
{
    
    //打开调试log的开关
    [UMSocialData openLog:YES];
    
    //如果你要支持不同的屏幕方向，需要这样设置，否则在iPhone只支持一个竖屏方向
    //[UMSocialConfig setSupportedInterfaceOrientations:UIInterfaceOrientationMaskAll];
    
    //设置友盟社会化组件appkey
    [UMSocialData setAppKey:UMAppKey];
    
    
    //打开新浪微博的SSO开关  回调地址
    [UMSocialSinaHandler openSSOWithRedirectURL:@"http://www.ookuaipin.com"];
    
    //打开腾讯微博SSO开关，设置回调地址
    [UMSocialTencentWeiboHandler openSSOWithRedirectUrl:@"http://sns.whalecloud.com/tencent2/callback"];
    
    //打开人人网SSO开关
  //  [UMSocialRenrenHandler openSSO];
    
    //    //设置分享到QQ空间的应用Id，和分享url 链接
    [UMSocialQQHandler setQQWithAppId:@"1103298924" appKey:@"zjJYnJSBi614wYFZ" url:@"http://www.ookuaipin.com"];
    //    //设置支持没有客户端情况下使用SSO授权
    [UMSocialQQHandler setSupportWebView:YES];
    //
    //    //设置易信Appkey和分享url地址
  //  [UMSocialYixinHandler setYixinAppKey:@"yx35664bdff4db42c2b7be1e29390c1a06" url:@"http://www.umeng.com/social"];
    //
 
}

/**
 * call login user info by devtoken service
 */
- (void) callLoginByDevToken
{
   // [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call get login user info by devtoken service routine.
    //[[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetLoginInfoFromDevToken:[Common getDeviceMacAddress]];
}
- (void) initiFlyMSC
{
    //设置log等级，此处log为默认在documents目录下的msc.log文件
    [IFlySetting setLogFile:LVL_ALL];
     
    //输出在console的log开关
    [IFlySetting showLogcat:YES];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *cachePath = [paths objectAtIndex:0];
    //设置msc.log的保存路径
    [IFlySetting setLogFilePath:cachePath];
    
    //创建语音配置
    NSString *initString = [[NSString alloc] initWithFormat:@"appid=%@,timeout=%@", iFlyMSC_APPID,iFlyMSC_TIMEOUT_VALUE];
    //所有服务启动前，需要确保执行createUtility
    
    [IFlySpeechUtility createUtility:initString];
}

//友盟统计
-(void)mobclick
{
    [MobClick startWithAppkey:UMAppKey reportPolicy:BATCH channelId:CHANNEL_FLAG];
    NSString *version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"1.0"];
    [MobClick setAppVersion:version];
    [MobClick setEncryptEnabled:NO];
    [MobClick setBackgroundTaskEnabled:YES];
//    [MobClick checkUpdate:@"发现新版本" cancelButtonTitle:@"残忍的拒绝"otherButtonTitles:@"立即去升级"];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    // APNS register  远程推送注册  三种类型都要
#if SUPPORT_IOS8
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:(UIRemoteNotificationTypeBadge
                                                                                             |UIRemoteNotificationTypeSound
                                                                                             |UIRemoteNotificationTypeAlert) categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    }else
#endif
    {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge|UIRemoteNotificationTypeAlert|UIRemoteNotificationTypeSound;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:myTypes];
    }
    //推送注册
    [BPush setupChannel:launchOptions];
    [BPush setDelegate:self]; // 必须。参数对象必须实现onMethod: response:方法，本示例中为self
//////////////////////////////////////////////////////////
    NSDictionary *mydict =  [launchOptions objectForKey:UIApplicationLaunchOptionsRemoteNotificationKey ];
    
    
    [self mobclick];
    
 /////////////////////////////////////////////////////
    [self initiFlyMSC];
    
    //如果使用服务中配置的app信息，请把初始化代码改为下面的初始化方法。
    //    [ShareSDK registerApp:@"iosv1101" useAppTrusteeship:YES];
    [self initializePlat];

    // initialize baidu map  百度地图
    _mapManager = [[BMKMapManager alloc]init];
    //mVAHskqVefXK1jS2Z9E9STa5   @"1u2r8ICcyTEOasdKGojEwA7K" ivkMCmZ5BjHLEQi7lNCun2M7
    
	BOOL ret = [_mapManager start:BaiDuMapKey generalDelegate:self];
	if (!ret) {
		MyLog(@"BMKMapmanager start failed!");
        UIAlertView *baidualert = [[UIAlertView alloc]initWithTitle:@"" message:@"百度地图启动失败" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:nil, nil];
        [baidualert show];
	}

    if ([Config loginOption] != nil && [[Config loginOption] isEqualToString:@""] == NO)
    {
        if ([[Config loginOption] isEqualToString:LOGIN_OPTION_CUSTOMER] == YES)
        {
            // getting the customer storyboard
            UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
            
            // initializing view controller
         Cus_MainTabViewController * controller = [customerStroyboard instantiateInitialViewController];
            
            self.storyboard = customerStroyboard;
            self.window.rootViewController = controller;
            
//            [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(rootControl:) userInfo:controller repeats:NO];

            if(mydict !=nil)
            {
                controller.mCurTab = TAB_ORDER;
                controller.selectedIndex =TAB_ORDER;
            }
            
        }
        else if ([[Config loginOption] isEqualToString:LOGIN_OPTION_DRIVER] == YES)
        {
            // getting the driver storyboard
            UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
            
            // initializing view controller
            Drv_MainTabViewController * controller = [driverStroyboard instantiateInitialViewController];
            
            self.storyboard = driverStroyboard;
            self.window.rootViewController = controller;

            
            if(mydict !=nil)
            {
                controller.mCurTab =TAB_ORDER;
                controller.selectedIndex =TAB_ORDER;
            }
        }
    }
    else
    {
        self.storyboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:[NSBundle mainBundle]];
        
        self.window.rootViewController = [self.storyboard instantiateInitialViewController];
    }
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"NO" forKey:@"Background"];
    [defaults synchronize];
    
    [self.window makeKeyAndVisible];
    
    
    ////////////////////////////////////////////////
    ////////////////////////////////////////////////
    //从推送进入应用 界面跳转
    
    if(mydict !=nil)
    {
        
        int typecode =[[mydict objectForKey:@"typecode"] integerValue];
        switch (typecode) {
            case 1:
            case 2:
            case 3:
            {
                UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
                
                // initializing view controller
                Drv_MainTabViewController * controller = [driverStroyboard instantiateInitialViewController];
                
                self.storyboard = driverStroyboard;
                self.window.rootViewController = controller;
            }
                break;
            case 4:
            case 5:
            {
                UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
                
                // initializing view controller
                Cus_MainTabViewController * controller = [customerStroyboard instantiateInitialViewController];
                self.storyboard = customerStroyboard;
                self.window.rootViewController = controller;
            }
                break;
            case 6:
            {
                LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
                self.window.rootViewController =loginVC;
            }
                break;
            case 7:
            {
                UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
                Cus_STOrderConfirmViewController *viewController = (Cus_STOrderConfirmViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"PubSTOrderConfirm"];
                viewController.soure =@"2";
                viewController.mOrderId = [[mydict objectForKey:@"orderid"] longValue];
                self.window.rootViewController =viewController;
            }
                break;
                
            default:
                break;
        }
    }
    
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:1];
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    for (int i =10000; i>=0; i--) {
    }
    
    return YES;
}

#if SUPPORT_IOS8
- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{
    //register to receive notifications
    [application registerForRemoteNotifications];
}
#endif
/**
 *  通过UDID BUNDLEIDENTIFIER  获取  deviceToken
 *
 *  @param application <#application description#>
 *  @param deviceToken <#deviceToken description#>
 */
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{

    MyLog(@"获得deviceToken %@",deviceToken);
    /**
     *  f1f4719c 0736bae3 b0611989 76ca5c17 84bfbc58 30e69a9e 19763d21 061a0721
     */
  //  [Common setPushDeviceToken:[NSString stringWithFormat:@"%@",deviceToken]];
    [BPush registerDeviceToken: deviceToken];
    [BPush bindChannel];
}

- (void) onMethod:(NSString*)method response:(NSDictionary*)data {
    MyLog(@"On method:%@", method);
    MyLog(@"data:%@", [data description]);
    NSDictionary* res = [[NSDictionary alloc] initWithDictionary:data];
    if ([BPushRequestMethod_Bind isEqualToString:method]) {
        NSString *appid = [res valueForKey:BPushRequestAppIdKey];
        NSString *userid = [res valueForKey:BPushRequestUserIdKey];
        NSString *channelid = [res valueForKey:BPushRequestChannelIdKey];
        //NSString *requestid = [res valueForKey:BPushRequestRequestIdKey];
        int returnCode = [[res valueForKey:BPushRequestErrorCodeKey] intValue];
        
        if (returnCode == BPushErrorCode_Success) {
            [Config setBaiDuPushChannelID:channelid];
            [Config setBaiDuPushUserID:userid];
           
            [self callLoginByDevToken];
        }
    } else if ([BPushRequestMethod_Unbind isEqualToString:method]) {
        int returnCode = [[res valueForKey:BPushRequestErrorCodeKey] intValue];
        if (returnCode == BPushErrorCode_Success) {
            MyLog(@"Baidu Bind failure");
        }
    }
}

/**
 这里处理新浪微博SSO授权之后跳转回来，和微信分享完成之后跳转回来
 */
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    return  [UMSocialSnsService handleOpenURL:url wxApiDelegate:nil];
}

/**
 这里处理新浪微博SSO授权进入新浪微博客户端后进入后台，再返回原来应用
 */
- (void)applicationDidBecomeActive:(UIApplication *)application
{
    [UMSocialSnsService  applicationDidBecomeActive];
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"NO" forKey:@"Background"];
    [defaults synchronize];
}


- (void) loginInfoFromDevTokenResult:(NSString *)result userinfo:(STUserInfo *)userinfo exeinfo:(STExeOrderInfo *)exeinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
//       [Common setUserInfo:userinfo];
//        if (exeinfo.orderId > 0) {
//            // getting the driver storyboard
//            UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
//            // go to perform view
//        Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[driverStroyboard instantiateViewControllerWithIdentifier:@"orderperform"];
//            controller.mOrderId = exeinfo.orderId;
//            controller.mOrderType = exeinfo.orderType;
//            controller.mCurRunDistance = exeinfo.runDistance;
//            controller.mTotalTime = exeinfo.runTime;
//            
//            SHOW_VIEW(controller);
//        }

    
    }
    else
    {
        [SVProgressHUD dismiss];
    }
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
      MyLog(@"%@",userInfo.description);
    NSString *descr =[[userInfo objectForKey:@"aps"] objectForKey:@"alert"];
   // NSString *de1 = [userInfo valueForKey:@"description"];
//    typecode 1，2，3 跳到司机主界面 4，5跳到乘客 6登录 7订单确认

    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    if ([[defaults objectForKey:@"Background"]isEqualToString:@"Background"]) {
        //点击通知进来的
        [defaults setObject:@"NO" forKey:@"Background"];
        int typecode =[[userInfo objectForKey:@"typecode"] integerValue];
        switch (typecode) {
            case 1:
            case 2:
            case 3:
            {
                UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
                
                // initializing view controller
                Drv_MainTabViewController * controller = [driverStroyboard instantiateInitialViewController];
                
                self.storyboard = driverStroyboard;
                self.window.rootViewController = controller;
            }
                break;
            case 4:
            case 5:
            {
                UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
                
                // initializing view controller
                Cus_MainTabViewController * controller = [customerStroyboard instantiateInitialViewController];
                self.storyboard = customerStroyboard;
                self.window.rootViewController = controller;
            }
                break;
            case 6:
            {
                LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
                self.window.rootViewController =loginVC;
            }
                break;
            case 7:
            {
                UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
                Cus_STOrderConfirmViewController *viewController = (Cus_STOrderConfirmViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"PubSTOrderConfirm"];
                viewController.soure =@"2";
                viewController.mOrderId = [[userInfo objectForKey:@"orderid"] longValue];
                self.window.rootViewController =viewController;
            }
                break;
                
            default:
                break;
        }
    }else//在前台通知来了
    {
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        if (![[defaults objectForKey:@"Cus_STorder"]isEqualToString:@"Cus_STorder"]) {
            if ([[userInfo objectForKey:@"typecode"] integerValue]==7) {
                UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
                Cus_STOrderConfirmViewController *viewController = (Cus_STOrderConfirmViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"PubSTOrderConfirm"];
                viewController.soure =@"2";
                viewController.mOrderId = [[userInfo objectForKey:@"orderid"] longValue];
                self.window.rootViewController =viewController;
            }
        }
        [defaults synchronize];
        UIAlertView *apnsAlert =  [[UIAlertView alloc]initWithTitle:@"推送消息" message:descr delegate:self cancelButtonTitle:@"确定" otherButtonTitles:nil,nil];
        [apnsAlert show];
        AudioServicesPlaySystemSound(1106);
    }
    [defaults synchronize];
}
- (void)applicationWillResignActive:(UIApplication *)application
{
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:1];
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:@"Background" forKey:@"Background"];
    [defaults synchronize];
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}



- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
