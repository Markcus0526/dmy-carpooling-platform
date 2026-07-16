//
//  AppDelegate.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginViewController.h"
#import "RegisterViewController.h"
#import "UMSocial.h"
#import "UMSocialYixinHandler.h"
#import "UMSocialWechatHandler.h"
#import "UMSocialQQHandler.h"
#import "UMSocialSinaHandler.h"
#import "UMSocialTencentWeiboHandler.h"
#import "UMSocialRenrenHandler.h"

@implementation AppDelegate

//@synthesize viewDelegate = _viewDelegate;
//
//- (id)init
//{
//    if(self = [super init])
//    {
//        _scene = WXSceneSession;
//        _viewDelegate = [[AGViewDelegate alloc] init];
//    }
//    return self;
//}

- (void)initializePlat
{
    //打开调试log的开关
    [UMSocialData openLog:YES];
    
    //如果你要支持不同的屏幕方向，需要这样设置，否则在iPhone只支持一个竖屏方向
    //[UMSocialConfig setSupportedInterfaceOrientations:UIInterfaceOrientationMaskAll];
    
    //设置友盟社会化组件appkey
    [UMSocialData setAppKey:UMAppKey];
    
    //设置微信AppId，设置分享url，默认使用友盟的网址
    [UMSocialWechatHandler setWXAppId:@"wxd930ea5d5a258f4f" appSecret:@"db426a9829e4b49a0dcac7b4162da6b6" url:@"http://www.umeng.com/social"];
    
    //打开新浪微博的SSO开关
    [UMSocialSinaHandler openSSOWithRedirectURL:@"http://sns.whalecloud.com/sina2/callback"];
    
    //打开腾讯微博SSO开关，设置回调地址
    [UMSocialTencentWeiboHandler openSSOWithRedirectUrl:@"http://sns.whalecloud.com/tencent2/callback"];
    
    //打开人人网SSO开关
    [UMSocialRenrenHandler openSSO];
    
    //    //设置分享到QQ空间的应用Id，和分享url 链接
    [UMSocialQQHandler setQQWithAppId:@"100424468" appKey:@"c7394704798a158208a74ab60104f0ba" url:@"http://www.umeng.com/social"];
    //    //设置支持没有客户端情况下使用SSO授权
    [UMSocialQQHandler setSupportWebView:YES];
    //
    //    //设置易信Appkey和分享url地址
    [UMSocialYixinHandler setYixinAppKey:@"yx35664bdff4db42c2b7be1e29390c1a06" url:@"http://www.umeng.com/social"];
    //

}



- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
   
    [self initializePlat];
    
    //如果使用服务器中配置的app信息，请把初始化平台代码改为下面的方法
    //    [self initializePlatForTrusteeship];
  
    
    
//    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
//    // Override point for customization after application launch.
//    self.window.backgroundColor = [UIColor whiteColor];
//    [self.window makeKeyAndVisible];
    
    self.storyboard = [UIStoryboard storyboardWithName:@"Storyboard" bundle:[NSBundle mainBundle]];
    
    self.window.rootViewController = [self.storyboard instantiateInitialViewController];
    //self.window.rootViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"Recommendation"];
    
    [self.window makeKeyAndVisible];

    
	// APNS register
    [[UIApplication sharedApplication] registerForRemoteNotificationTypes:
     (UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert)];
    //被其他应用调用
    NSURL *url = [launchOptions objectForKey:UIApplicationLaunchOptionsURLKey];
    if(url) {//做出相应的判断
      
        if ([url.scheme hasPrefix:@"mainappregist"])
        {
            //跳转注册
            // go to login view controller
            RegisterViewController *viewController = (RegisterViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"Register"];
            
            // set back controller as NewsManage tab
            viewController.mTargetTab = TAB_APPS;
            
            [self.window.rootViewController presentViewController:viewController animated:YES completion:nil];
        }

    }
    
    return YES;
}


- (void)application:(UIApplication*)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData*)deviceToken
{
    NSString *devToken = [[deviceToken description] stringByTrimmingCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@"<>"]];
    devToken = [devToken stringByReplacingOccurrencesOfString:@" " withString:@""];
    
    [Common setDeviceToken:devToken];
    
	MyLog(@"My token is: %@", deviceToken);
	
}

- (void)application:(UIApplication*)application didFailToRegisterForRemoteNotificationsWithError:(NSError*)error
{
	MyLog(@"Failed to get token, error: %@", error);
}


/**
 这里处理新浪微博SSO授权之后跳转回来，和微信分享完成之后跳转回来
 */
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    //游戏跳转注册处理
    if ([url.scheme hasPrefix:@"mainappregist"])
    {
        //跳转注册
        // go to login view controller
        RegisterViewController *viewController = (RegisterViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"Register"];
        
        // set back controller as NewsManage tab
        viewController.mTargetTab = TAB_APPS;
        
        [self.window.rootViewController presentViewController:viewController animated:YES completion:nil];
        
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"被调用了" message:@"aifna" delegate:self cancelButtonTitle:@"取消"otherButtonTitles:@"确定", nil];
        [alert show];
        return YES;
    }
    
   
//  这里处理新浪微博SSO授权之后跳转回来，和微信分享完成之后跳转回来
    return  [UMSocialSnsService handleOpenURL:url wxApiDelegate:nil];
    
}

/**
 这里处理新浪微博SSO授权进入新浪微博客户端后进入后台，再返回原来应用
 */
- (void)applicationDidBecomeActive:(UIApplication *)application
{
    [UMSocialSnsService  applicationDidBecomeActive];
}


- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    MyLog(@"application handleOpenURL-%@",url);
    
    if ([url.scheme hasPrefix:@"mainappregist"])
    {
        //跳转注册
        // go to login view controller
        LoginViewController *viewController = (LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
        
        // set back controller as NewsManage tab
        viewController.mTargetTab = TAB_APPS;
        
        [self.window.rootViewController presentViewController:viewController animated:YES completion:nil];
    }
    
    UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"被调用了" message:@"aifna" delegate:self cancelButtonTitle:@"取消"otherButtonTitles:@"确定", nil];
    [alert show];
    
    return YES;


}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
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
