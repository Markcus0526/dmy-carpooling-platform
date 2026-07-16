//
//  ForgetPWViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "ForgetPWViewController.h"

@interface ForgetPWViewController ()

@end

@implementation ForgetPWViewController

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
    mVerifKey = @"";
    
    // register textfield delegate
    _txtUserName.delegate = self;
    _txtPhoneNum.delegate = self;
    _txtVerifyCode.delegate = self;
    _txtPassword.delegate = self;
    _txtConfirmPW.delegate = self;
    
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

- (IBAction)onClickedVerfiyCode:(id)sender
{
    NSString * mobile = [_txtPhoneNum text];
    
    
	if (_txtUserName.text.length == 0)
	{
		SHOW_MSG(@"用户名不能为空");
		[_txtUserName becomeFirstResponder];
		return;
	}
    
	if (_txtUserName.text.length < 2 ||
		_txtUserName.text.length > 20)
	{
		SHOW_MSG(@"用户名长度必须在2-20位之间");
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
    
	[self callGetVerifKey:mobile];
}


- (IBAction)onClickedConfirm:(id)sender
{
    // check input box value
	if (_txtUserName.text.length == 0)
	{
		SHOW_MSG(@"用户名不能为空");
		[_txtUserName becomeFirstResponder];
		return;
	}
	
	if (_txtUserName.text.length < 2 ||
		_txtUserName.text.length > 20)
	{
		SHOW_MSG(@"用户名长度必须在2-20位之间");
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

    
	if (_txtPassword.text.length == 0)
	{
		SHOW_MSG(@"密码不能为空");
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (_txtPassword.text.length < 6 ||
		_txtPassword.text.length > 16)
	{
		SHOW_MSG(@"密码长度必须在6-16位之间");
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (![_txtConfirmPW.text isEqualToString:_txtPassword.text])
	{
		SHOW_MSG(@"确认密码不一致");
		[_txtConfirmPW becomeFirstResponder];
		return;
	}


	if (_txtVerifyCode.text.length == 0)
	{
		SHOW_MSG(@"验证码不能为空");
		[_txtVerifyCode becomeFirstResponder];
		return;
	}
	
	
	NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
	if (_txtPhoneNum.text.length != 11 || [_txtPhoneNum.text rangeOfCharacterFromSet:notDigits].location != NSNotFound)
	{
		SHOW_MSG(@"手机号码需要11位数字");
		[_txtPhoneNum becomeFirstResponder];
		return;
	}
    
    
	NSString* validKey = [self validateVerifyKey];
	if (validKey.length != 0)
	{
		SHOW_MSG(validKey);
		[_txtVerifyCode becomeFirstResponder];
		return;
	}
    
    
    
	[self callForgetPassword];
}

- (IBAction)onClickedCancel:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
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
        
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
		
		[self performSelector:@selector(gotoParent) withObject:nil afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

- (void) gotoParent
{
	// go to previous view controller
	[self.navigationController popViewControllerAnimated:YES];
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
	else if (textField == _txtPassword)
	{
		nMaxLength = 16;
	}
	else if (textField == _txtConfirmPW)
	{
		nMaxLength = 16;
	}
	else
	{
		nMaxLength = 999;			// Others have no limitation
	}
	
	return newLength <= nMaxLength;
}




@end
