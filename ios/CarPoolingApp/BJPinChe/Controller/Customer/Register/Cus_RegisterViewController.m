//
//  RegisterViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_RegisterViewController.h"
#import "Cus_MainTabViewController.h"
#include "Config.h"

@interface Cus_RegisterViewController ()

@end

@implementation Cus_RegisterViewController

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
    mVerifKey = @"qq";
    
    // register textfield delegate
    _txtUserName.delegate = self;
    _txtPhoneNum.delegate = self;
    _txtVerifyCode.delegate = self;
    _txtRealName.delegate = self;
    _txtPassword.delegate = self;
    _txtConfirmPW.delegate = self;
    _txtInviteCode.delegate = self;
    
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

- (IBAction)onClickedChkMale:(id)sender
{
    [_chkMale setSelected:YES];
    [_chkFemale setSelected:NO];
}

- (IBAction)onClickedChkFemale:(id)sender
{
    [_chkMale setSelected:NO];
    [_chkFemale setSelected:YES];
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
/**
 *  注册界面 确定按钮事件
 *  读取界面上用户输入的信息 初始化 STRegUserInfo
 *  @param sender <#sender description#>
 */
- (IBAction)onClickedConfirm:(id)sender
{
    STRegUserInfo * userinfo = [[STRegUserInfo alloc] init];
    
    userinfo.username = [_txtUserName text];
    userinfo.mobile = [_txtPhoneNum text];
    userinfo.nickname = [_txtRealName text];
    userinfo.password = [_txtPassword text];

    userinfo.city = [Common getCurrentCity];
    MyLog(@"%@",userinfo.city);
    userinfo.sex = ([_chkMale isSelected] == YES) ? SEX_MALE_C : SEX_FEMALE_C;
    userinfo.invitecode = [_txtInviteCode text];
    
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
    if ([[_txtRealName text] isEqualToString:@""] == YES) {
        [_txtUserName becomeFirstResponder];
        return;
    }
    if ([[_txtPassword text] isEqualToString:@""] == YES) {
        [_txtUserName becomeFirstResponder];
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
    
    [self callRegisterUser:userinfo];
}

- (IBAction)onClickedCancel:(id)sender
{
    BACK_VIEW;
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/** 获取验证码
 * call service to get verify key
 * @param : mobile [in], current user's phone number
 */
- (void) callGetVerifKey : (NSString *)mobile
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetVerifKey:mobile andRegistered:0];
}

- (void) getVerifKeyResult:(NSString *)result keydata:(NSString *)keydata
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
        mVerifKey = keydata;
        //-- option show key
#warning 测试状态下 校验码自动赋值
        [_txtVerifyCode setText:mVerifKey];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}


/**
 * call service to register user
 * @param : userinfo [in], 
 */
- (void) callRegisterUser : (STRegUserInfo *)userinfo
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
   //判断网络情况  无网络连接进行提示
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] RegisterUser:userinfo];
}
/**
 *  注册结果显示 处理
 *  代理方法
 *  @param result <#result description#>
 */
- (void) registerUserResult:(NSString *)result userinfo:(STUserInfo *)userinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // save user information
        [Common setUserInfo:userinfo];
        
        Cus_MainTabViewController *controller = (Cus_MainTabViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"maintab"];
        
        // set target tab
        controller.mCurTab = mTargetTab;
        
        SHOW_VIEW(controller);
    }
    else //注册失败 显示错误信息
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}



@end
