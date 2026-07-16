//
//  AppDelegate.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "WXApi.h"
//#import "WeiboApi.h"
//#import <ShareSDK/ShareSDK.h>
//#import <TencentOpenAPI/QQApi.h>
//#import <TencentOpenAPI/QQApiInterface.h>
////#import <TencentOpenAPI/TencentOAuth.h>
//#import "AGViewDelegate.h"
//WXApiDelegate    AccountSvcDelegate
@interface AppDelegate : UIResponder <UIApplicationDelegate>
{
//    enum WXScene _scene;
//    AGViewDelegate *_viewDelegate;
//    SSInterfaceOrientationMask _interfaceOrientationMask;
}

@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) UIStoryboard *storyboard;
@property (strong, nonatomic) UIViewController *currentViewController;

//@property (nonatomic,readonly) AGViewDelegate *viewDelegate;
//@property (nonatomic) SSInterfaceOrientationMask interfaceOrientationMask;

@end
