//
//  AccountMgrViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "AccountMgrViewController.h"
#import "Config.h"
#import "MainTabViewController.h"

@interface AccountMgrViewController ()

@end

@implementation AccountMgrViewController

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

- (IBAction)onClickedLogout:(id)sender
{
	UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"提示" message:@"您确定要退出吗？" delegate:self cancelButtonTitle:STR_BUTTON_CANCEL otherButtonTitles:STR_BUTTON_CONFIRM, nil];
	[alert show];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 1)
	{
		[self callLogout:[Common getUserID]];
	}
}

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
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        // remove account data
        [Config setLoginName:@""];
        [Config setLoginPassword:@""];
        [Common setUserInfo:nil];
        
        // go to main view controller
        MainTabViewController * main = (MainTabViewController *)[self.navigationController.viewControllers objectAtIndex:0];
        main.selectedViewController = [main.viewControllers objectAtIndex:TAB_APPS];
        [self.navigationController popToRootViewControllerAnimated:YES];

    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
    
}

@end
