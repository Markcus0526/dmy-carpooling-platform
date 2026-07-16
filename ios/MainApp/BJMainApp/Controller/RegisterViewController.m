//
//  RegisterViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "RegisterViewController.h"
#import "MainTabViewController.h"
#import "ResultDlgView.h"

@interface RegisterViewController ()

@end

@implementation RegisterViewController

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
    [_chkMale setSelected:YES];
    // make tap recognizer to hide keyboard
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
    
    // check verify mark
    [_txtVerifyCode addTarget:self action:@selector(checkVerifyKey) forControlEvents:UIControlEventEditingChanged];
}

/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
}

- (void) checkVerifyKey
{
    [self validateVerifyKey];
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
	if (_txtPhoneNum.text.length == 0)
	{
		SHOW_MSG(@"手机号码不能为空");
		[_txtPhoneNum becomeFirstResponder];
		return;
	}
	
	
	NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
	if (_txtPhoneNum.text.length != 11 || [_txtPhoneNum.text rangeOfCharacterFromSet:notDigits].location != NSNotFound)
	{
		SHOW_MSG(@"手机号码需要11位数字");
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
 *  @param sender
 */
- (IBAction)onClickedConfirm:(id)sender
{
    [self.view endEditing:YES];
    
    
    STRegUserInfo * userinfo = [[STRegUserInfo alloc] init];
    
    userinfo.username = [_txtUserName text];
    userinfo.mobile = [_txtPhoneNum text];
    userinfo.nickname = [_txtRealName text];
    userinfo.password = [_txtPassword text];

	userinfo.city = [Common getCurrentCity];
	MyLog(@"%@",userinfo.city);
	userinfo.sex = ([_chkMale isSelected] == YES) ? STR_SEX_MALE : STR_SEX_FEMALE;
	userinfo.invitecode = [_txtInviteCode text];

	// check input box value
	if (_txtUserName.text.length == 0)
	{
		SHOW_MSG(@"用户名不能为空");
		[_txtUserName becomeFirstResponder];
		return;
	}


	int validateResult = [Common validateName:_txtUserName.text allowChinese:NO];
	if (validateResult == -2)
	{
		SHOW_MSG(@"用户名有空格");
		[_txtUserName becomeFirstResponder];
		return;
	}
	else if (validateResult == 3)
	{
		SHOW_MSG(@"用户名必须以英文开头");
		[_txtUserName becomeFirstResponder];
		return;
	}
	else if (validateResult < 0)
	{
		SHOW_MSG(@"用户名仅支持英文或数字");
		[_txtUserName becomeFirstResponder];
		return;
	}


	if (_txtUserName.text.length > 20 || _txtUserName.text.length < 2)
	{
		SHOW_MSG(@"用户名仅支持英文或数字");
		[_txtUserName becomeFirstResponder];
		return;
	}


	if (_txtPhoneNum.text.length == 0)
	{
		SHOW_MSG(@"手机号码不能为空");
		[_txtPhoneNum becomeFirstResponder];
		return;
	}


	NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
	if (_txtPhoneNum.text.length != 11 || [_txtPhoneNum.text rangeOfCharacterFromSet:notDigits].location != NSNotFound)
	{
		SHOW_MSG(@"手机号码需要11位数字");
		[_txtPhoneNum becomeFirstResponder];
		return;
	}


	if (mKeyTimeStamp != 0) {       // Got verify key. Must validate verify key
		if ([mVerifiedPhone isEqualToString:_txtPhoneNum.text]) {
			if ([_txtVerifyCode.text isEqualToString:@""]) {
				SHOW_MSG(@"验证码不能为空");
				[_txtVerifyCode becomeFirstResponder];
				return;
			}
            
			NSString* szValidVerifyKey = [self validateVerifyKey];
			if (![szValidVerifyKey isEqualToString:@""]) {
				SHOW_MSG(szValidVerifyKey);
				[_txtVerifyCode becomeFirstResponder];
				return;
			}
		} else {
			SHOW_MSG(@"需要重新获取验证码");
			return;
		}
	} else {            // Never got verify key. Must validate phone num is original phone num
		SHOW_MSG(@"需要获取验证码");
		return;
	}


	validateResult = [Common validateName:_txtRealName.text allowChinese:YES];
	if (validateResult == -2)
	{
		SHOW_MSG(@"称呼有空格");
		[_txtRealName becomeFirstResponder];
		return;
	}
	else if (validateResult == 3)
	{
		SHOW_MSG(@"称呼必须以英文开头");
		[_txtRealName becomeFirstResponder];
		return;
	}
	else if (validateResult < 0)
	{
		SHOW_MSG(@"称呼仅支持英文或数字");
		[_txtRealName becomeFirstResponder];
		return;
	}


	if (_txtRealName.text.length == 0)
	{
		SHOW_MSG(@"称呼不能为空");
		[_txtRealName becomeFirstResponder];
		return;
	}
    
    
	if (_txtRealName.text.length < 2 ||
		_txtRealName.text.length > 20)
	{
		SHOW_MSG(@"昵称长度必须在2-20位之间");
		[_txtRealName becomeFirstResponder];
		return;
	}
    
    
	
	if (_txtPassword.text.length == 0)
	{
		SHOW_MSG(@"密码不能为空");
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (_txtPassword.text.length < 6 || _txtPassword.text.length > 16)
	{
		SHOW_MSG(@"密码长度必须在6-16位之间");
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (_txtConfirmPW.text.length == 0)
	{
		SHOW_MSG(@"请再次输入新密码");
		[_txtConfirmPW becomeFirstResponder];
		return;
	}
    
    
	if (![_txtPassword.text isEqualToString:_txtConfirmPW.text])
	{
		SHOW_MSG(@"密码输入不一致");
		[_txtConfirmPW becomeFirstResponder];
		return;
	}


	if ([_txtInviteCode.text isEqualToString:@""]) {
		UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"" message:@"邀请码不存在，是否继续注册？" delegate:self cancelButtonTitle:@"否" otherButtonTitles:@"是", nil];
		[alertView show];
		return;
	}


	[self callRegisterUser:userinfo];
}

- (IBAction)onClickedCancel:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
	[[[CommManager getCommMgr] accountSvcMgr] GetVerifKey:mobile];
}


- (void) getVerifKeyResult:(NSString *)result keydata:(NSString *)keydata
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
		[SVProgressHUD dismiss];

		// set user info
		mVerifKey = keydata;

		mKeyTimeStamp = [NSDate timeIntervalSinceReferenceDate];
		mVerifiedPhone = _txtPhoneNum.text;

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
 *  @param result
 */
- (void) registerUserResult:(NSString *)result userinfo:(STUserInfo *)userinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        NSString* szMsg = [NSString stringWithFormat:@"欢迎您，%@", _txtRealName.text];
		if (userinfo.sex == SEX_MALE) {
			szMsg = [szMsg stringByAppendingString:@"先生!"];
		} else {
			szMsg = [szMsg stringByAppendingString:@"女士!"];
		}

		[ResultDlgView showInController:self success:YES message:szMsg];

		// save user information
        [Common setUserInfo:userinfo];
		[self performSelector:@selector(gotoNext) withObject:nil afterDelay:DEF_DELAY];
        
    }
    else //注册失败 显示错误信息
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) gotoNext
{
    MainTabViewController * main = (MainTabViewController *)[self.navigationController.viewControllers objectAtIndex:0];
    main.selectedViewController = [main.viewControllers objectAtIndex:mTargetTab];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

///////////////////////////////////////////////////////////////////
#pragma mark - Scroll When Keyboard Focus

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    curTextField = textField;
    if (keyboardVisible)
        [KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    curTextField = nil;
    [textField resignFirstResponder];
}

- (void)textViewDidBeginEditing:(UITextView *)textView
{
    curTextField = textView;
    if (keyboardVisible)
        [KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
}

- (void)textViewDidEndEditing:(UITextView *)textView
{
    curTextField = nil;
    [textView resignFirstResponder];
}

- (void)keyboardWillShow:(NSNotification *)notification
{
    //---gets the size of the keyboard---
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];
    [keyboardValue getValue:&keyboardBounds];
    
	[KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
    
    keyboardVisible = true;
}

- (void)keyboardWillHide:(NSNotification *)notification
{
    //---gets the size of the keyboard---
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];
    [keyboardValue getValue:&keyboardBounds];
    
    [KeyboardHelper moveScrollView:nil scrollView:(UIScrollView*)self.view];
    
    keyboardVisible = false;
    
    curTextField = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:self.view.window];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:self.view.window];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}



- (void)startCountDownTimer
{
	[self stopCountDownTimer];
	mCountdownTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(onCountDownTick:) userInfo:nil repeats:YES];
}


- (void)stopCountDownTimer
{
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
		if (btnGetVerifyKey.isEnabled)
		{
			[btnGetVerifyKey setEnabled:NO];
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
            [_markVerify setSelected:YES];
		}
		else
		{
            [_markVerify setSelected:NO];
            
			if (mKeyTimeStamp == 0) {
				retVal = @"还没有获取验证码";
			} else {
				retVal = @"验证码超时了，重新获取";
			}
		}
	} else {
        [_markVerify setSelected:NO];
        
		retVal = @"验证码不正确";
	}
    
	return retVal;
}


#pragma mark Text Field change event
-(BOOL)textField:(UITextField*)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
	NSUInteger newLength = textField.text.length + string.length - range.length;
	int nMaxLength = 0;
	if (textField == _txtUserName)
	{
		for (int i = 0; i < string.length; i++) {
			unichar chItem = [string characterAtIndex:i];
			if (chItem != '_' &&
				(chItem < '0' && chItem > '9') &&
				(chItem < 'a' && chItem > 'z') &&
				(chItem < 'A' && chItem > 'Z'))
			{
				return false;
			}
		}

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
		nMaxLength = 20;
	}
	else
	{
		nMaxLength = 999;			// Others have no limitation
	}

	if (textField.text.length > nMaxLength) {
		textField.text = [textField.text substringToIndex:nMaxLength];
	}

	return newLength <= nMaxLength;
}


- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
	if (buttonIndex == 0) {
		// Cancel button. Do nothing
	} else {
		STRegUserInfo * userinfo = [[STRegUserInfo alloc] init];

		userinfo.username = [_txtUserName text];
		userinfo.mobile = [_txtPhoneNum text];
		userinfo.nickname = [_txtRealName text];
		userinfo.password = [_txtPassword text];

		userinfo.city = [Common getCurrentCity];
		MyLog(@"%@",userinfo.city);
		userinfo.sex = ([_chkMale isSelected] == YES) ? STR_SEX_MALE : STR_SEX_FEMALE;
		userinfo.invitecode = [_txtInviteCode text];

		[self callRegisterUser:userinfo];
	}
}



@end
