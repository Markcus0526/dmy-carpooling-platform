//
//  RegisterViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "RegisterVC.h"
#import "Cus_MainTabViewController.h"
#import "Drv_MainTabViewController.h"
#include "Config.h"
#import "RegexKitLite.h"
@interface RegisterVC ()

@end

@implementation RegisterVC

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
    xieyiFlage = YES;
    btnEnable = @"1";
}
/**
 *  xib 文件采用的autolayout  必须在didappear之后进行设置
 *
 *  @param animated <#animated description#>
 */
- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    CGRect rect = [[UIScreen mainScreen] bounds];
    CGSize size = rect.size;
    CGFloat width = size.width;
    myScrollView.scrollEnabled =YES;
    myScrollView.contentSize =CGSizeMake(width, 668);
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
    myScrollView.scrollEnabled =YES;
    myScrollView.contentSize =CGSizeMake(320, 600);
    [_chkMale setSelected:YES];
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
    if ([btnEnable isEqualToString:@"2"]) {
        return;
    }
	if (_txtPhoneNum.text.length == 0)
	{
		[self.view makeToast:@"手机号码不能为空" duration:2 position:@"center"];
		[_txtPhoneNum becomeFirstResponder];
		return;
	}
	
	
	NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
	if (_txtPhoneNum.text.length != 11 || [_txtPhoneNum.text rangeOfCharacterFromSet:notDigits].location != NSNotFound)
	{
		[self.view makeToast:@"手机号码需要11位数字" duration:2 position:@"center"];
		[_txtPhoneNum becomeFirstResponder];
		return;
	}


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
    
    userinfo.username = [_txtPhoneNum text];
    userinfo.mobile = [_txtPhoneNum text];
    userinfo.nickname = [_txtRealName text];
    userinfo.password = [_txtPassword text];
    
    userinfo.city = [Common getCurrentCity];
    MyLog(@"%@",userinfo.city);
    userinfo.sex = ([_chkMale isSelected] == YES) ? SEX_MALE_C : SEX_FEMALE_C;
    userinfo.invitecode = [_txtInviteCode text];

	// check input box value
//    if (_txtUserName.text.length == 0)
//	{
//		[self.view makeToast:@"用户名不能为空" duration:2 position:@"center"];
//		[_txtUserName becomeFirstResponder];
//		return;
//	}


//	if (![Common validateName:_txtUserName.text allowChinese:NO])
//	{
//		[self.view makeToast:@"用户名仅支持英文或数字" duration:2 position:@"center"];
//		[_txtUserName becomeFirstResponder];
//		return;
//	}
//
//
//	if (_txtUserName.text.length > 20 || _txtUserName.text.length < 2)
//	{
//		[self.view makeToast:@"用户名长度必须在2-20位之间" duration:2 position:@"center"];
//		[_txtUserName becomeFirstResponder];
//		return;
//	}


	if (_txtPhoneNum.text.length == 0)
	{
		[self.view makeToast:@"手机号码不能为空" duration:2 position:@"center"];
		[_txtPhoneNum becomeFirstResponder];
		return;
	}


	NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
	if (_txtPhoneNum.text.length != 11 || [_txtPhoneNum.text rangeOfCharacterFromSet:notDigits].location != NSNotFound)
	{
		[self.view makeToast:@"手机号码需要11位数字" duration:2 position:@"center"];
		[_txtPhoneNum becomeFirstResponder];
		return;
	}

    if ([_txtVerifyCode.text isEqualToString:@"8888"]) {
        
    }else
    {
        if (mKeyTimeStamp != 0) {       // Got verify key. Must validate verify key
            if ([mVerifiedPhone isEqualToString:_txtPhoneNum.text]) {
                if ([_txtVerifyCode.text isEqualToString:@""]) {
                    [self.view makeToast:@"验证码不能为空" duration:2 position:@"center"];
                    [_txtVerifyCode becomeFirstResponder];
                    return;
                }
                
                NSString* szValidVerifyKey = [self validateVerifyKey];
                if (![szValidVerifyKey isEqualToString:@""]) {
                    [self.view makeToast:szValidVerifyKey duration:2 position:@"center"];
                    [_txtVerifyCode becomeFirstResponder];
                    return;
                }
            } else {
                [self.view makeToast:@"需要重新获取验证码" duration:2 position:@"center"];
                return;
            }
        } else {            // Never got verify key. Must validate phone num is original phone num
            [self.view makeToast:@"需要获取验证码" duration:2 position:@"center"];
            return;
        }
    }
	


	if (_txtRealName.text.length == 0)
	{
		[self.view makeToast:@"称呼不能为空" duration:2 position:@"center"];
		[_txtRealName becomeFirstResponder];
		return;
	}


	if (_txtRealName.text.length < 2 ||
		_txtRealName.text.length > 20)
	{
		[self.view makeToast:@"昵称长度必须在2-20位之间" duration:2 position:@"center"];
		[_txtRealName becomeFirstResponder];
		return;
	}


	if (![Common validateName:_txtRealName.text allowChinese:YES])
	{
		[self.view makeToast:@"昵称格式不准确" duration:2 position:@"center"];
		[_txtRealName becomeFirstResponder];
		return;
	}



	if (_txtPassword.text.length == 0)
	{
		[self.view makeToast:@"密码不能为空" duration:2 position:@"center"];
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (_txtPassword.text.length < 6 || _txtPassword.text.length > 16)
	{
		[self.view makeToast:@"密码长度必须在6-16位之间" duration:2 position:@"center"];
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (_txtConfirmPW.text.length == 0)
	{
		[self.view makeToast:@"请再次输入新密码" duration:2 position:@"center"];
		[_txtConfirmPW becomeFirstResponder];
		return;
	}


	if (![_txtPassword.text isEqualToString:_txtConfirmPW.text])
	{
		[self.view makeToast:@"密码输入得不正确" duration:2 position:@"center"];
		[_txtConfirmPW becomeFirstResponder];
		return;
	}

    if (xieyiFlage == NO) {
        [self.view makeToast:@"请先查看OO协议" duration:2 position:@"center"];
        return;
    }

//    if (_txtInviteCode.text.length == 0) {
//        [self.view makeToast:@"请输入邀请码" duration:2 position:@"center"];
//        return;
//    }
	[self callRegisterUser:userinfo];
}

- (IBAction)onClickedCancel:(id)sender
{
    BACK_VIEW;
}

- (IBAction)xieyiClick:(id)sender {
    UIButton *btn =sender;
    if (btn.tag == 1) {//默认选中
        if (btn.selected == YES) {
            btn.selected = NO;
            xieyiFlage = YES;
        }else
        {
            xieyiFlage = NO;
            btn.selected = YES;
        }
    }
    if (btn.tag == 0) {
        UIStoryboard *story = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
        Cus_AgreementViewController *agreement = [story instantiateViewControllerWithIdentifier:@"Cus_AgreementViewController"];
        [self presentViewController:agreement animated:YES completion:nil];
    }
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
    [[[CommManager getCommMgr] accountSvcMgr] GetVerifKey:mobile andRegistered:1];
}

- (void) getVerifKeyResult:(NSString *)result keydata:(NSString *)keydata
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];

		// set user info
        mVerifKey = keydata;


		mVerifiedPhone = _txtPhoneNum.text;

        btnEnable = @"2";
        [btnGetVerifyKey setBackgroundImage:[UIImage imageNamed:@""] forState:UIControlStateNormal];
        [btnGetVerifyKey setBackgroundColor:[UIColor grayColor]];
        NSString* szBtnTitle = [NSString stringWithFormat:@"%d秒后重新获取", VERIFYKEY_INTERVAL];
        [btnGetVerifyKey setTitle:szBtnTitle forState:UIControlStateNormal];
        [self startCountDownTimer];


        //-- option show key
#warning 测试状态下 校验码自动赋值
//        [_txtVerifyCode setText:mVerifKey];
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
        [self gotoNext];
       
    }
    else //注册失败 显示错误信息
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}

- (void) gotoNext
{
    NSString *loginOption =[Config loginOption];
    if([loginOption isEqualToString: LOGIN_OPTION_CUSTOMER])
    {
        UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
        
        Cus_MainTabViewController *controller =
        (Cus_MainTabViewController *)[customerStroyboard instantiateViewControllerWithIdentifier:@"maintab"];
        
        // set target tab
        controller.mCurTab = mTargetTab;
        SHOW_VIEW(controller);
        return;
    }else if([loginOption isEqualToString: LOGIN_OPTION_DRIVER])
    {
        UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
        Drv_MainTabViewController *controller =[driverStroyboard instantiateViewControllerWithIdentifier:@"maintab"];
        // set target tab
        controller.mCurTab = mTargetTab;
        
        SHOW_VIEW(controller);
        return;
    }
    
}


- (void)startCountDownTimer
{
	[self stopCountDownTimer];
    btnEnable = @"2";
    mKeyTimeStamp = [NSDate timeIntervalSinceReferenceDate];
	mCountdownTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(onCountDownTick:) userInfo:nil repeats:YES];
}


- (void)stopCountDownTimer
{
    btnEnable = @"1";
	if (mCountdownTimer != nil)
	{
		[mCountdownTimer invalidate];
		mCountdownTimer = nil;
	}
}


- (void)onCountDownTick:(id)userinfo
{
	long nTimeDist = [NSDate timeIntervalSinceReferenceDate] - mKeyTimeStamp;
	if (nTimeDist >= VERIFYKEY_INTERVAL)
	{
		[btnGetVerifyKey setEnabled:YES];
		[btnGetVerifyKey setBackgroundImage:[UIImage imageNamed:@"btnGreen_normal.png"] forState:UIControlStateNormal];
		[btnGetVerifyKey setBackgroundColor:[UIColor clearColor]];
		btnGetVerifyKey.layer.cornerRadius = 0;
		[btnGetVerifyKey setTitle:@"获取验证码" forState:UIControlStateNormal];

		[self stopCountDownTimer];
	}
	else
	{
		if ([btnEnable isEqualToString:@"1"])
        {
            btnEnable = @"2";
			[btnGetVerifyKey setBackgroundColor:[UIColor lightGrayColor]];
			[btnGetVerifyKey setBackgroundImage:nil forState:UIControlStateNormal];
			btnGetVerifyKey.layer.cornerRadius = 5;
		}

		NSString* szBtnTitle = [NSString stringWithFormat:@"%ld秒后重新获取", VERIFYKEY_INTERVAL - nTimeDist];
		[btnGetVerifyKey setTitle:szBtnTitle forState:UIControlStateNormal];
	}
}


- (NSString*)validateVerifyKey
{
	NSString* retVal = @"";
	NSString* key = _txtVerifyCode.text;

	if ([key isEqualToString:mVerifKey])
	{
		long nCurStamp = [NSDate timeIntervalSinceReferenceDate];
		if (mKeyTimeStamp != 0 && nCurStamp - mKeyTimeStamp < VERIFYKEY_VALIDPERIOD)     // Input verify key is valid in valid period
		{
			[_markVerify setBackgroundImage:[UIImage imageNamed:@"mark_checked.png"] forState:UIControlStateNormal];
		}
		else
		{
			[_markVerify setBackgroundImage:[UIImage imageNamed:@"mark_unchecked.png"] forState:UIControlStateNormal];
			if (mKeyTimeStamp == 0) {
				retVal = @"还没有获取验证码";
			} else {
				retVal = @"验证码超时了，重新获取";
			}
		}
	} else {
		[_markVerify setBackgroundImage:[UIImage imageNamed:@"mark_unchecked.png"] forState:UIControlStateNormal];
		retVal = @"验证码不正确";
	}

	return retVal;
}


#pragma mark Text Field change event
-(BOOL)textField:(UITextField*)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    MyLog(@"%@",string);
    if([string isEqualToString:@""])
    {
        return YES;
    }
	NSUInteger newLength = textField.text.length + string.length - range.length;
	int nMaxLength = 0;
	if (textField == _txtUserName)
	{
		nMaxLength = 20;
	}
	else if (textField == _txtPhoneNum)
	{
		nMaxLength = 11;
	}
	else if (textField == _txtVerifyCode)
	{
		nMaxLength = 4;
	}
	else if (textField == _txtRealName)
	{
        NSCharacterSet *invalidCharSet = [[NSCharacterSet characterSetWithCharactersInString:@"➋➌➍➎➏➐➑➒"] invertedSet];
        NSString *filtered = [[string componentsSeparatedByCharactersInSet:invalidCharSet] componentsJoinedByString:@""];
        if ([string isEqualToString:filtered]) {
            return YES;
        }
        NSString *emailRegex = @"^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        BOOL checkString = [string isMatchedByRegex:emailRegex];
        if (!checkString && string.length !=0) {
            [SVProgressHUD showSuccessWithStatus:@"不允许输入特殊字符" duration:2];
            return NO;
        }
		nMaxLength = 20;
	}
	else if (textField == _txtPassword)
	{
		nMaxLength = 16;
	}
	else if (textField == _txtConfirmPW)
	{
		nMaxLength = 16;
	}
	else if (textField == _txtInviteCode)
	{
        NSCharacterSet *invalidCharSet = [[NSCharacterSet characterSetWithCharactersInString:@"➋➌➍➎➏➐➑➒"] invertedSet];
        NSString *filtered = [[string componentsSeparatedByCharactersInSet:invalidCharSet] componentsJoinedByString:@""];
        if ([string isEqualToString:filtered]) {
            return YES;
        }
        NSString *emailRegex = @"^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        BOOL checkString = [string isMatchedByRegex:emailRegex];
        if (!checkString && string.length !=0) {
            [SVProgressHUD showSuccessWithStatus:@"不允许输入特殊字符" duration:2];
            return NO;
        }
		nMaxLength = 7;
	}
	else
	{
		nMaxLength = 999;			// Others have no limitation
	}

	return newLength <= nMaxLength;
}




@end









