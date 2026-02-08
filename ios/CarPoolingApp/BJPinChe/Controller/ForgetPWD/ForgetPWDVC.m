//
//  ForgetPWViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "ForgetPWDVC.h"

@interface ForgetPWDVC ()

@end

@implementation ForgetPWDVC

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
    
    btnEnbled =1;
    
    [self initControls];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    CGRect rect = [[UIScreen mainScreen] bounds];
    CGSize size = rect.size;
    CGFloat width = size.width;
    CGFloat height = size.height;
    myScrollView.scrollEnabled =YES;
    myScrollView.contentSize =CGSizeMake(width, height+50);

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
    //BACK_VIEW;
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickedVerfiyCode:(id)sender
{
    if (btnEnbled !=1) {
        return;
    }
    NSString * mobile = [_txtPhoneNum text];


	if (_txtUserName.text.length == 0)
	{
		[self.view makeToast:@"用户名不能为空" duration:2 position:@"center"];
		[_txtUserName becomeFirstResponder];
		return;
	}

	if (_txtUserName.text.length < 2 ||
		_txtUserName.text.length > 20)
	{
		[self.view makeToast:@"用户名长度必须在2-20位之间" duration:2 position:@"center"];
		[_txtUserName becomeFirstResponder];
		return;
	}

	if (![Common validateName:_txtUserName.text allowChinese:NO])
	{
		[self.view makeToast:@"用户名格式不准确" duration:2 position:@"center"];
		[_txtUserName becomeFirstResponder];
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

	[self callGetVerifKey:mobile];
}


- (IBAction)onClickedConfirm:(id)sender
{
    // check input box value
	if (_txtUserName.text.length == 0)
	{
		[self.view makeToast:@"用户名不能为空" duration:2 position:@"center"];
		[_txtUserName becomeFirstResponder];
		return;
	}
	
	if (_txtUserName.text.length < 2 ||
		_txtUserName.text.length > 20)
	{
		[self.view makeToast:@"昵称长度必须在2-20位之间" duration:2 position:@"center"];
		[_txtUserName becomeFirstResponder];
		return;
	}
	
	if (![Common validateName:_txtUserName.text allowChinese:NO])
	{
		[self.view makeToast:@"昵称格式不准确" duration:2 position:@"center"];
		[_txtUserName becomeFirstResponder];
		return;
	}


	if (_txtPassword.text.length == 0)
	{
		[self.view makeToast:@"密码不能为空" duration:2 position:@"center"];
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (_txtPassword.text.length < 6 ||
		_txtPassword.text.length > 16)
	{
		[self.view makeToast:@"密码长度必须在6-16位之间" duration:2 position:@"center"];
		[_txtPassword becomeFirstResponder];
		return;
	}


	if (![_txtConfirmPW.text isEqualToString:_txtPassword.text])
	{
		[self.view makeToast:@"密码输入得不正确" duration:2 position:@"center"];
		[_txtConfirmPW becomeFirstResponder];
		return;
	}

	if (_txtVerifyCode.text.length == 0)
	{
		[self.view makeToast:@"验证码不能为空" duration:2 position:@"center"];
		[_txtVerifyCode becomeFirstResponder];
		return;
	}
	
	
	NSCharacterSet* notDigits = [[NSCharacterSet decimalDigitCharacterSet] invertedSet];
	if (_txtPhoneNum.text.length != 11 || [_txtPhoneNum.text rangeOfCharacterFromSet:notDigits].location != NSNotFound)
	{
		[self.view makeToast:@"手机号码需要11位数字" duration:2 position:@"center"];
		[_txtPhoneNum becomeFirstResponder];
		return;
	}


	NSString* validKey = [self validateVerifyKey];
	if (validKey.length != 0)
	{
		[self.view makeToast:validKey duration:2 position:@"center"];
		[_txtVerifyCode becomeFirstResponder];
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
    [[[CommManager getCommMgr] accountSvcMgr] GetVerifKey:mobile andRegistered:2];
}

- (void) getVerifKeyResult:(NSString *)result keydata:(NSString *)keydata
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
        mVerifKey = keydata;


		mKeyTimeStamp = [NSDate timeIntervalSinceReferenceDate];
		mVerifiedPhone = _txtPhoneNum.text;

//		btnGetVerifyKey.enabled = NO;
        btnEnbled =2;
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
        
        [SVProgressHUD dismiss];
        
        // go to previous view controller
        [self onClickedBack:nil];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
}



- (void)startCountDownTimer
{
	[self stopCountDownTimer];
	mCountdownTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(onCountDownTick:) userInfo:nil repeats:YES];
}


- (void)stopCountDownTimer
{
    btnEnbled = 1;
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
		if (btnEnbled ==1)
		{
            btnEnbled = 2;
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