//
//  MainTabViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_MainTabViewController.h"
#import "Config.h"
#import "Drv_AccountMgrViewController.h"
#import "Drv_OrderMgrViewController.h"
//#import "Drv_LoginViewController.h"
//#import "Drv_ShareViewController.h"
#import "MMLocationManager.h"
#import "LoginVC.h"

@interface Drv_MainTabViewController ()

@end

@implementation Drv_MainTabViewController

@synthesize mCurTab;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Drvtom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
        
    // Get current city
//    [[MMLocationManager shareLocation] getCity:^(NSString *cityString) {
//        [Common setCurrentCity:cityString];
//    }];
    
    //self.tabBarController.delegate = self;
    self.delegate = self;
    
    // set tab bar item text color
    [[UITabBarItem appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIColor colorWithRed:52.0/255.0 green:135.0/255.0 blue:91.0/255.0 alpha:1.0], UITextAttributeTextColor, nil] forState:UIControlStateNormal];
    [[UITabBarItem appearance] setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:[UIColor whiteColor], UITextAttributeTextColor, nil] forState:UIControlStateHighlighted];
    
//    // Change the tab bar background
//    UIImage* tabBarBackground = [UIImage imageNamed:@"tabbar.png"];
//    [[UITabBar appearance] setBackgroundImage:tabBarBackground];
//    [[UITabBar appearance] setSelectionIndicatorImage:[UIImage imageNamed:@"tabbar_selected.png"]];
//    
    
    // set tab bar item image
    UITabBar * tabBar = self.tabBar;
    
    // set tab bar item text color
//    tabBar.selectedImageTintColor = [UIColor whiteColor];
//    tabBar.tintColor = [UIColor colorWithRed:52.0/255.0 green:135.0/255.0 blue:91.0/255.0 alpha:1.0];
    
    UITabBarItem *tabBarItem0 = [[tabBar items] objectAtIndex:0];
    UITabBarItem *tabBarItem1 = [[tabBar items] objectAtIndex:1];
    UITabBarItem *tabBarItem2 = [[tabBar items] objectAtIndex:2];
    UITabBarItem *tabBarItem3 = [[tabBar items] objectAtIndex:3];
    
    UIImage *unselectedImage0 = [[UIImage imageNamed:@"tabIcon1_normal"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIImage *selectedImage0 = [[UIImage imageNamed:@"tabIcon1_active"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    tabBarItem0.image = unselectedImage0;
    tabBarItem0.selectedImage  =selectedImage0;
    
    UIImage *unselectedImage1 = [[UIImage imageNamed:@"tabIcon2_normal"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIImage *selectedImage1 = [[UIImage imageNamed:@"tabIcon2_active"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    tabBarItem1.image = unselectedImage1;
    tabBarItem1.selectedImage  =selectedImage1;
    
    UIImage *unselectedImage2 = [[UIImage imageNamed:@"tabIcon3_normal"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIImage *selectedImage2 = [[UIImage imageNamed:@"tabIcon3_active"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    tabBarItem2.image = unselectedImage2;
    tabBarItem2.selectedImage  =selectedImage2;
    
    UIImage *unselectedImage4 = [[UIImage imageNamed:@"tabIcon4_normal"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIImage *selectedImage4 = [[UIImage imageNamed:@"tabIcon4_active"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    tabBarItem3.image = unselectedImage4;
    tabBarItem3.selectedImage  =selectedImage4;
  
}

- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    // change tab by selected index
    if ([Common getUserInfo] != nil)
    {
        self.selectedViewController = [self.viewControllers objectAtIndex:mCurTab];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

//- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item
//{
//    NSLog(@"Selected INDEX OF TAB-ITEM ==> ");
//}

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    //通过tabBarItem的Tag值区分，，拦截未注册情况
    NSInteger index_Tag = viewController.tabBarItem.tag;
    
    if (index_Tag == 1) {
        
        mCurTab = TAB_ORDER;
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
           // Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as Order tab
           // viewController.mTargetTab = TAB_ORDER;
           
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            
            [self presentViewController:loginVC animated:YES completion:nil];
            
            return NO;
        }
    } else if (index_Tag == 2) {
        
        mCurTab = TAB_ACCOUNT;
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
           // Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as Account tab
          //  viewController.mTargetTab = TAB_ACCOUNT;
          
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            
            [self presentViewController:loginVC animated:YES completion:nil];
            
            return NO;
        }
    } else if (index_Tag == 3) {
        
        mCurTab = TAB_SHARE;
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
          //  Drv_LoginViewController *viewController = (Drv_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as Account tab
          //  viewController.mTargetTab = TAB_SHARE;
           
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            
            [self presentViewController:loginVC animated:YES completion:nil];
            
            return NO;
        }
    } else {
        mCurTab = TAB_APPS;
    }
    
    return YES;
}


@end
