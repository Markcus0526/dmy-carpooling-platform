//
//  ForgetPWViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_ForgetPwdViewController.h"

@interface Cus_ForgetPwdViewController ()

@end

@implementation Cus_ForgetPwdViewController

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
    mVerifKey = @"qq";
    
    // register textfield delegate
    _txtUserName.delegate = self;
    _txtPhoneNum.delegate = self;
    _txtVerifyCode.delegate = self;
    _txtPassword.delegate = self;
    _txtConfirmPW.delegate = self;

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


///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

// main control event
/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

- (IBAction)onClickedVerfiyCode:(id)sender
{
    NSString * mobile = [_txtPhoneNum text];
    
    // check mobile number
    if ([mobile isEqualToString:@""]) {
        [_txtPhoneNum becomeFirstResponder];
        return;
    }
    
    [self callGetVerifKey:mobile];
}

- (IBAction)onClickedConfirm:(id)sender
{
    // check input box value
    if ([[_txtUserName text] isEqualToString:@""] == YES) {
        [_txtUserName becomeFirstResponder];
        return;
    }
    if ([[_txtPhoneNum text] isEqualToString:@""] == YES) {
        [_txtPhoneNum becomeFirstResponder];
        return;
    }
    if ([[_txtVerifyCode text] isEqualToString:@""] == YES) {
        [_txtVerifyCode becomeFirstResponder];
        return;
    }
    if ([[_txtPassword text] isEqualToString:@""] == YES) {
        [_txtUserName becomeFirstResponder];
        return;
    }
    if ([[_txtConfirmPW text] isEqualToString:@""] == YES) {
        [_txtConfirmPW becomeFirstResponder];
        return;
    }
    
    // check verify code
    if ([[_txtVerifyCode text] isEqualToString:mVerifKey] != YES) {
        [_txtVerifyCode becomeFirstResponder];
        return;
    }
    
    // check confirm passowrd
    if ([[_txtPassword text] isEqualToString:[_txtConfirmPW text]] == NO) {
        [_txtConfirmPW becomeFirstResponder];
        return;
    }
    
    [self callForgetPassword];
}

- (IBAction)onClickedCancel:(id)sender
{
    BACK_VIEW;
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call service to get verify key
 * @param : mobile [in], current user's phone number
 */
- (void) callGetVerifKey : (NSString *)mobile
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetVerifKey:mobile andRegistered:1];
}

- (void) getVerifKeyResult:(NSString *)result keydata:(NSString *)keydata
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
        mVerifKey = keydata;
        //-- option show key
        [_txtVerifyCode setText:mVerifKey];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}

/**
 * call change user info service
 * @param : userinfo [in],
 */
- (void) callForgetPassword
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] ForgetPassword:[_txtUserName text] mobile:[_txtPhoneNum text] newpwd:[_txtPassword text]];
}

- (void) forgetPasswordResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // go to previous view controller
        [self onClickedBack:nil];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}



@end
