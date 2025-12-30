//
//  LoginViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//
#define iPhone5 ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO)

#import "LoginVC.h"
#import "Config.h"
#import "Cus_MainTabViewController.h"
#import "Drv_MainTabViewController.h"
#import "Drv_OrderPerformViewController.h"
#import "ForgetPWDVC.h"
#import "RegisterVC.h"

@interface LoginVC ()

@end

#define OPTION_NONE                         @"-1"
#define OPTION_SAVE_PWD                     @"0"
#define OPTION_AUTO_LOGIN                   @"1"
#define OPTION_SAVE_AUTO_LOGIN              @"2"


@implementation LoginVC

@synthesize mTargetTab;

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
    
    if (iPhone5) {
        
    }else
    {
        [_passBtn setFrame:CGRectMake(0, self.passBtn.frame.origin.y - 80, 320, 44)];
    }
}

/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self downTextFoeld];
    [self.view endEditing:YES];
}


- (void) gotoNext
{
//    NSString *loginOption =[Config loginOption];
//    if([loginOption isEqualToString: LOGIN_OPTION_CUSTOMER])
//    {
//        UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
//        
//        Cus_MainTabViewController *controller =
//        (Cus_MainTabViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"maintab"];
//        
//        // set target tab
//        controller.mCurTab = mTargetTab;
//        //SHOW_VIEW(controller);
//        
//        return;
//    }else if([loginOption isEqualToString: LOGIN_OPTION_DRIVER])
//    {
//      UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
//        Drv_MainTabViewController *controller =[driverStroyboard instantiateViewControllerWithIdentifier:@"maintab"];
//        // set target tab
//        controller.mCurTab = mTargetTab;
//        
//        SHOW_VIEW(controller);
//        return;
//    }
    
//    [self dismissViewControllerAnimated:YES completion:nil];

    [Config setLoginOption:LOGIN_OPTION_CUSTOMER];
    
    
    // getting the customer storyboard
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    
    // initializing view controller
    UIViewController * controller = [customerStroyboard instantiateInitialViewController];
//    controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    
    [self presentViewController:controller animated:YES completion:nil];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    [self dismissViewControllerAnimated:NO completion:nil];
}

- (IBAction)onClickedLogin:(id)sender
{
    NSString *userText = [_txtUsername text];
	NSString *pwdText = [_txtPassword text];

	[_txtUsername resignFirstResponder];
    [_txtPassword resignFirstResponder];
    [self downTextFoeld];

	if ([userText isEqualToString:@""])
    {
        [_txtUsername becomeFirstResponder];
		[self.view makeToast:@"用户名不能为空" duration:2 position:@"center"];
        return;
    }
    else if ([pwdText isEqualToString:@""])
    {
        [_txtPassword becomeFirstResponder];
		[self.view makeToast:@"密码不能为空" duration:2 position:@"center"];
        return;
    }

	[self callLoginUser];
}
/**
 *  点击注册 跳转到注册界面
 *
 *  @param sender <#sender description#>
 */
- (IBAction)onClickedRegister:(id)sender
{
    RegisterVC *registerVC =[[RegisterVC alloc]initWithNibName:@"RegisterView" bundle:nil];
    [self presentViewController:registerVC animated:YES completion:nil];
  
}

- (IBAction)onClickedForgetPW:(id)sender
{
    ForgetPWDVC *forgetPWD = [[ForgetPWDVC alloc]initWithNibName:@"ForgetPWDView" bundle:nil];
    [self presentViewController:forgetPWD animated:YES completion:nil];
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
        
        // set user info  设置账号信息，
        [Common setUserInfo:userinfo];
        NSString *loginOption =[Config loginOption];
        
        if([loginOption isEqualToString:LOGIN_OPTION_DRIVER])
        {
            if (exeinfo.orderId > 0)
            {
                UIViewController * presentViewCtrl = self.presentingViewController;
                [self dismissViewControllerAnimated:NO completion:^
                 {
                     // go to perform view
                     UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
                
                     
                     Drv_OrderPerformViewController *controller = (Drv_OrderPerformViewController *)[driverStroyboard instantiateViewControllerWithIdentifier:@"orderperform"];
                     controller.mOrderId = exeinfo.orderId;
                     controller.mOrderType = exeinfo.orderType;
                     controller.mCurRunDistance = exeinfo.runDistance;
                     controller.mTotalTime = exeinfo.runTime;
                     
                     [presentViewCtrl presentViewController:controller animated:NO completion:nil];
                 }];

            }
            [self gotoNext];
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
-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if (textField == _txtUsername) {
        [self upTextField:-60];
    }
    if (textField == _txtPassword) {
        [self upTextField:-80];
    }
    return YES;
}
-(void)upTextField:(float)hieght{
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.2];
    [_backView setFrame:CGRectMake(0, hieght, _backView.frame.size.width, _backView.frame.size.height)];
    [UIView commitAnimations];
}
-(void)downTextFoeld{
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.2];
    
    [_backView setFrame:CGRectMake(0, 66, _backView.frame.size.width, _backView.frame.size.height)];
    
    [UIView commitAnimations];
}
-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self downTextFoeld];
    return YES;
}
-(BOOL)textFieldShouldEndEditing:(UITextField *)textField
{
    [self downTextFoeld];
    return YES;
}

@end
