//
//  AccountMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//个人中心界面

#import "Cus_AccountMgrViewController.h"
#import "Config.h"
#import "LVDianTabBarController.h"
#import "BPush.h"
@interface Cus_AccountMgrViewController ()<UIAlertViewDelegate>

@end

@implementation Cus_AccountMgrViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];

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

/**
 *  storyBoard 模式连线下隐藏底部tab
 *
 *  @param segue  <#segue description#>
 *  @param sender <#sender description#>
 */
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    [segue.destinationViewController setHidesBottomBarWhenPushed:YES];
    
}


- (IBAction)onClickedLvDianAcc:(id)sender {
    LVDianTabBarController *lvdianAcc =  [[LVDianTabBarController alloc]init];
    lvdianAcc.hidesBottomBarWhenPushed =YES;
    [self.navigationController pushViewController:lvdianAcc animated:YES];
}


#pragma  mark 点击退出登录
- (IBAction)onClickedLogout:(id)sender
{
	//百度推送解除绑定
	UIAlertView  *logoutAlert =[[UIAlertView alloc]initWithTitle:@" 确认退出登录" message:@"亲，要退出登录吗？" delegate:self cancelButtonTitle:@"确定" otherButtonTitles:@"取消", nil];
	[logoutAlert show];
//	[BPush unbindChannel];
//	[Config setBaiDuPushUserID:nil];
//	[Config setBaiDuPushUserID:nil];
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if(buttonIndex == 0)
	{
		[self callLogout:[Common getUserID]];
	}
	else
	{
	}
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call logout service
 */
- (void) callLogout : (int)userid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] LogoutUser:userid devtoken:[Common getDeviceMacAddress]];
}

- (void) logoutUserResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // remove account data
        [Common setUserInfo:nil];
        
        // go to main view controller
       // UIViewController *controller = [self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
        //SHOW_VIEW(controller);
        
        UINavigationController *nav =self.tabBarController.viewControllers[0];
        [nav popToRootViewControllerAnimated:YES];
        self.tabBarController.selectedIndex =0;
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

@end
