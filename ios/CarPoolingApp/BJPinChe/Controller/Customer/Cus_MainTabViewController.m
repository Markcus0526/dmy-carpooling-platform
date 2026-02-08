//
//  MainTabViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_MainTabViewController.h"
#import "Cus_AccountMgrViewController.h"
#import "Cus_OrderMgrViewController.h"
#import "Cus_ShareViewController.h"
#import "MMLocationManager.h"
#import "LoginVC.h"
#import "Cus_ShareViewController.h"
#import "OONavigationController.h"

@interface Cus_MainTabViewController ()

@end

@implementation Cus_MainTabViewController

@synthesize mCurTab;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

//- (void)loadView
//{
//    [super loadView];
//     OONavigationController *navi = self.childViewControllers[3];
//   
//    Cus_ShareViewController *shareVC =[[Cus_ShareViewController alloc]initWithNibName:@"ShareView" bundle:nil];
//    [navi addChildViewController:shareVC];
//    OONavigationController *navi =  (OONavigationController *)viewController;
//    [navi initWithRootViewController:shareVC];
//
//}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
	// Get current city  定位用户城市
	[[MMLocationManager shareLocation] getCity:^(NSString *cityString) {
        [Common setCurrentCity:cityString];
    }];
//    if([Common getCurrentCity]!= nil)
//    {
//        [[MMLocationManager shareLocation]stopLocation];
//    }
	
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
    
    
//    tabBarItem1.image = [UIImage imageNamed:@"tabIcon2_normal"];
//    tabBarItem1.selectedImage  =[UIImage imageNamed:@"tabIcon2_active"];
//    
//    tabBarItem2.image = [UIImage imageNamed:@"tabIcon2_normal"];
//    tabBarItem2.selectedImage  =[UIImage imageNamed:@"tabIcon3_active"];
//    
//    tabBarItem3.image = [UIImage imageNamed:@"tabIcon3_normal"];
//    tabBarItem3.selectedImage  =[UIImage imageNamed:@"tabIcon4_active"];
    
    
//    [tabBarItem0 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon1_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon1_normal"]];
//    [tabBarItem1 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon2_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon2_normal"]];
//    [tabBarItem2 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon3_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon3_normal"]];
//    [tabBarItem3 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon4_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon4_normal"]];

//    self.selectedViewController = [self.viewControllers objectAtIndex:mCurTab];
    
}


- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    // change tab by selected index
//    if ([Common getUserInfo] != nil)
//    {
//        self.selectedViewController = [self.viewControllers objectAtIndex:mCurTab];
//    }
    self.selectedViewController = [self.viewControllers objectAtIndex:mCurTab];

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
/*
- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    if(self.selectedIndex == 1)
    {
       // [[viewController childViewControllers]objectAtIndex:0];
        viewController.tabBarItem.tag =0;
        
    }
    
    viewController  = (UINavigationController *)viewController;
    
    if ([viewController isKindOfClass:[Cus_OrderMgrViewController class]] == YES) {
        
        mCurTab = TAB_ORDER;
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
//            Cus_LoginViewController *viewController = (Cus_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            [self presentViewController:loginVC animated:YES completion:nil];
            // set back controller as NewsManage tab
           // viewController.mTargetTab = TAB_ORDER;
            
//            [self presentViewController:viewController animated:YES completion:nil];
            
            return NO;
        }
    } else if ([viewController isKindOfClass:[Cus_AccountMgrViewController class]] == YES) {
        
        mCurTab = TAB_ACCOUNT;
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
            //Cus_LoginViewController *viewController = (Cus_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            // set back controller as Account tab
           // viewController.mTargetTab = TAB_ACCOUNT;
            
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            
            [self presentViewController:loginVC animated:YES completion:nil];
            
            return NO;
        }
    } else if ([viewController isKindOfClass:[Cus_ShareViewController class]] == YES) {
        
        mCurTab = TAB_SHARE;
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
           // Cus_LoginViewController *viewController = (Cus_LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as Account tab
            //viewController.mTargetTab = TAB_SHARE;
           
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
*/

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    
    //通过tabBarItem的Tag值区分，，拦截未注册情况
    NSInteger index_Tag = viewController.tabBarItem.tag;
    
    if(index_Tag == 1)
    {
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            [self presentViewController:loginVC animated:YES completion:nil];
            return NO;
          }
        mCurTab = TAB_ORDER;
    } else if (index_Tag == 2) {
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            
            [self presentViewController:loginVC animated:YES completion:nil];
            return NO;
        }
        mCurTab = TAB_ACCOUNT;
    } else if (index_Tag == 3) {
        // check login state
        if ([Common getUserInfo] == nil)
        {
            LoginVC *loginVC =[[LoginVC alloc]initWithNibName:@"LoginView" bundle:nil];
            loginVC.mTargetTab =mCurTab;
            
            [self presentViewController:loginVC animated:YES completion:nil];
            return NO;
        }else
        {
       
//            viewController
//            [(UINavigationController *)viewController pushViewController:shareVC animated:YES];
           // return YES;
        }
        mCurTab = TAB_SHARE;
    } else {
        mCurTab = TAB_APPS;
            }
    return YES;
}


@end
