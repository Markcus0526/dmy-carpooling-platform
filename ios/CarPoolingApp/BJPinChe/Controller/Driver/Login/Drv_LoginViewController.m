//
//  LoginViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_LoginViewController.h"
#import "Config.h"
#import "Drv_MainTabViewController.h"
#import "Drv_OrderPerformViewController.h"
#import "Drv_MainTabViewController.h"

@interface Drv_LoginViewController ()

@end

#define OPTION_NONE                         @"-1"
#define OPTION_SAVE_PWD                     @"0"
#define OPTION_AUTO_LOGIN                   @"1"
#define OPTION_SAVE_AUTO_LOGIN              @"2"


@implementation Drv_LoginViewController

@synthesize mTargetTab;

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
    
    [self initControls];
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



///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    // register textfield delegate
    _txtUsername.delegate = self;
    _txtPassword.delegate = self;
    
    // make tap recognizer to hide keyboard
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
}

/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
}


- (void) gotoNext
{
    [self dismissViewControllerAnimated:YES completion:nil];
    
//    
//    Drv_MainTabViewController *controller = (Drv_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
//    
//    // set target tab
//    controller.mCurTab = mTargetTab;
//    
//    SHOW_VIEW(controller);
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickedLogin:(id)sender
{
    NSString *userText = [_txtUsername text];
	NSString *pwdText = [_txtPassword text];
	
    [_txtUsername resignFirstResponder];
    [_txtPassword resignFirstResponder];
    
	if ([userText isEqualToString:@""])
    {
        [_txtUsername becomeFirstResponder];
        return;
    }
    else if ([pwdText isEqualToString:@""])
    {
        [_txtPassword becomeFirstResponder];
        return;
    }
    
    [self callLoginUser];
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call service to get child app list
 */
- (void) callLoginUser
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] LoginUser:_txtUsername.text password:_txtPassword.text city:[Common getCurrentCity] devtoken:[Common getDeviceMacAddress]];
}

- (void) loginUserResult:(NSString *)result userinfo:(STUserInfo *)userinfo exeinfo:(STExeOrderInfo *)exeinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
        [Common setUserInfo:userinfo];
        
        // check executing order info
        if (exeinfo.orderId > 0)
        {
            UIViewController * presentViewCtrl = self.presentingViewController;
            [self dismissViewControllerAnimated:NO completion:^
             {
                 // go to perform view
                 Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"orderperform"];
                 controller.mOrderId = exeinfo.orderId;
                 controller.mOrderType = exeinfo.orderType;
                 controller.mCurRunDistance = exeinfo.runDistance;
                 controller.mTotalTime = exeinfo.runTime;
                 
                 [presentViewCtrl presentViewController:controller animated:NO completion:nil];
             }];
        }
        else
        {
            [self gotoNext];
        }
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}



@end
