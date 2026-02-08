//
//  MainTabViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "MainTabViewController.h"
#import "AccountMgrViewController.h"
#import "NewsMgrViewController.h"
#import "LoginViewController.h"
#import "ShareViewController.h"

@interface MainTabViewController ()

@end

@implementation MainTabViewController

@synthesize mCurTab;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
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
    
    [tabBarItem0 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon1_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon1_normal"]];
    [tabBarItem1 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon2_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon2_normal"]];
    [tabBarItem2 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon3_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon3_normal"]];
    [tabBarItem3 setFinishedSelectedImage:[UIImage imageNamed:@"tabIcon4_active"] withFinishedUnselectedImage:[UIImage imageNamed:@"tabIcon4_normal"]];
    
    // change tab by selected index
//    [self.tabBarController.delegate tabBarController:self.tabBarController shouldSelectViewController:[[tabBar viewControllers] objectAtIndex:2]];
//    [self.tabBarController setSelectedIndex:2];
    

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

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    
    //通过tabBarItem的Tag值区分，，拦截未注册情况
    NSInteger index_Tag = viewController.tabBarItem.tag;
    if (index_Tag == 1) {
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
            LoginViewController *viewController = (LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as NewsManage tab
            viewController.mTargetTab = TAB_NEWS;
            
            [self.navigationController pushViewController:viewController animated:YES];
            
            return NO;
        }
    } else if (index_Tag == 2) {
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
            LoginViewController *viewController = (LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as Account tab
            viewController.mTargetTab = TAB_ACCOUNT;
            
//            [self presentViewController:viewController animated:YES completion:nil];
            [self.navigationController pushViewController:viewController animated:YES];
            
            return NO;
        }
    } else if (index_Tag == 3) {
        
        // check login state
        if ([Common getUserInfo] == nil)
        {
            // go to login view controller
            LoginViewController *viewController = (LoginViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"login"];
            
            // set back controller as Account tab
            viewController.mTargetTab = TAB_SHARE;
            
            [self.navigationController pushViewController:viewController animated:YES];
            
            return NO;
        }
    }
    
    return YES;
}





@end
