//
//  UserDetailViewController.m
//  BJMainApp
//
//  Created by KimOC on 8/7/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "UserDetailViewController.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ImageHelper.h"


@interface UserDetailViewController ()

@end


#define TABCOUNT                    2
#define PHOTO_CHANGED               1
#define PHOTO_UNCHANGED             0


@implementation UserDetailViewController

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


- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];

	[self callGetUserInfo:[Common getUserID]];
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
    mCurTab = 0;
    mBmpUserPhoto = nil;
    
    // register textfield delegate
    _txtName.delegate = self;
    _txtPhoneNum.delegate = self;
    _txtVerifyCode.delegate = self;
    
    // initialize news view frame
    CGRect rcFrame = _vwUserInfo.frame;
	rcFrame = CGRectMake(_vwUserInfo.frame.size.width, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	_vwChangePW.frame = rcFrame;
    
    // initialize main scroll content size
    [_scrollMain setContentSize:CGSizeMake(_vwUserInfo.frame.size.width + _vwChangePW.frame.size.width, _vwUserInfo.frame.size.height - 64)];   // waring -- 64 is TitleBar size (problem of scrollView container)
    
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
#pragma mark - Scroll Delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == _scrollMain)
	{
		CGPoint ptOffset = scrollView.contentOffset;
		int nIndicatorWidth = _ctrlIndicator.frame.size.width;
		int nLeftMargin = (self.view.frame.size.width - nIndicatorWidth * TABCOUNT) / (TABCOUNT - 1);
		int nPageWidth = _scrollMain.frame.size.width;
        
		CGRect rcIndFrame = _ctrlIndicator.frame;
		
		if (ptOffset.x * nIndicatorWidth / nPageWidth < nIndicatorWidth / 2)
		{
            // change indicator position
            rcIndFrame = CGRectMake(ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
			// first tab selected
            mCurTab = 0;
			_lblTitle1.textColor = MYCOLOR_GREEN;
			_lblTitle2.textColor = [UIColor blackColor];
            
        }
		else
		{
            // change indicator position
            rcIndFrame = CGRectMake(nLeftMargin + ptOffset.x * nIndicatorWidth / nPageWidth, rcIndFrame.origin.y, rcIndFrame.size.width, rcIndFrame.size.height);
            
			// second tab selected
            mCurTab = 1;
			_lblTitle1.textColor = [UIColor blackColor];
            _lblTitle2.textColor = MYCOLOR_GREEN;
			
		}
        
        _ctrlIndicator.frame = rcIndFrame;
	}
}

- (void) updateUI : (STUserInfo *)userInfo
{
    [_txtPhoneNum setText:userInfo.mobile];
    [_txtName setText:userInfo.nickname];
    [_lblBirthday setText:userInfo.birthday];
    
    switch ((enum VERIFY_STATE)userInfo.person_verified) {
        case STATE_NOTVERIFIED:
            [_lblValidState setText:STR_NOTVERIFIED];
            [btnDoVerify setHidden:NO];
            break;
        case STATE_VERIFIED:
            [_lblValidState setText:STR_VERIFIED];
            [btnDoVerify setHidden:YES];
            break;
        case STATE_WATING:
            [_lblValidState setText:STR_VERI_WAITING];
            [btnDoVerify setHidden:YES];
            break;
            
        default:
            [_lblValidState setText:STR_NOTVERIFIED];
            [btnDoVerify setHidden:NO];
            break;
    }
    
    if (userInfo.sex == SEX_MALE) {
        [_chkMale setSelected:YES];
        [_chkFemale setSelected:NO];
    } else {
        [_chkMale setSelected:NO];
        [_chkFemale setSelected:YES];
    }
    
    // set user image
    if ([userInfo.photo length] > 0) {
        [_imgUser setImageWithURL:[NSURL URLWithUnicodeString:userInfo.photo] placeholderImage:[UIImage imageNamed:@"demo_userimg.png"]];
    }
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

/**
 * Clicked first tab ( user info edit tab )
 */
- (IBAction)onClickedView1:(id)sender
{
    [_scrollMain scrollRectToVisible:CGRectMake(0, 0, _vwUserInfo.frame.size.width, _vwUserInfo.frame.size.height) animated:YES];
}

/**
 * Clicked second tab ( change password tab )
 */
- (IBAction)onClickedView2:(id)sender
{
    [_scrollMain scrollRectToVisible:CGRectMake(_vwUserInfo.frame.size.width, 0, _vwUserInfo.frame.size.width, _vwUserInfo.frame.size.height) animated:YES];
}

#pragma mark -  User information event
/**
 * clicked change user image
 */
- (IBAction)onClickedUserImg:(id)sender
{
    RDActionSheet *actionSheet = [RDActionSheet alloc];
    if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
        [actionSheet initWithCancelButtonTitle:@"取消"
                            primaryButtonTitle:nil
                            destroyButtonTitle:nil
                             otherButtonTitles:@"相册", @"拍照", nil];
    }
    else {
        [actionSheet initWithCancelButtonTitle:@"取消"
                            primaryButtonTitle:nil
                            destroyButtonTitle:nil
                             otherButtonTitles:@"相册",nil];
    }
    actionSheet.callbackBlock = ^(RDActionSheetResult result, NSInteger buttonIndex) {
        
        switch (result) {
            case RDActionSheetButtonResultSelected:
            {
                NSLog(@"Pressed %i", buttonIndex);
                
                UIImagePickerController *picker = [[NonRotateImagePickerController alloc] init];
                //                picker.delegate = [HuiYuanZhongXinViewController sharedInstance];
                picker.delegate = self;
                picker.allowsEditing = YES;
                
                if (buttonIndex == 0)
                    picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                else
                    picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                
                if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad) {
                    //picker.modalInPopover = YES;
                    UIPopoverController *popover = [[UIPopoverController alloc] initWithContentViewController:picker];
                    //                    popover.delegate = [HuiYuanZhongXinViewController sharedInstance];
                    popover.delegate = self;
                    [popover presentPopoverFromRect:CGRectMake(90, 150, 270, 300)
                                             inView:self.view
                           permittedArrowDirections:UIPopoverArrowDirectionLeft
                                           animated:YES];
                    //[popover setPopoverContentSize:CGSizeMake(500, 500)];
                } else {
                    [self presentModalViewController:picker animated:YES];
                }
                break;
            }
            case RDActionSheetResultResultCancelled:
                NSLog(@"Sheet cancelled");
                break;
        }
    };
    [actionSheet showFrom:self.view];
}


- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
	
    UIImage *srcImage = [info objectForKey:UIImagePickerControllerEditedImage];
	UIImage *orgImage = [info objectForKey:UIImagePickerControllerOriginalImage];
    if (srcImage == nil)
        srcImage = orgImage;
    
    //UIImage *image = [srcImage imageByScalingAndCroppingForSize:CGSizeMake(128, 128)];
    UIImage * image = srcImage;
    
    if (image != nil)
    {
        mBmpUserPhoto = image;
        [_imgUser setImage:mBmpUserPhoto];
        
        NSData *data = UIImageJPEGRepresentation(mBmpUserPhoto, 1);
        NSString * base64Image = [Common base64forData:data];
        
        [self callUpdateUserPhoto:[Common getUserID] photo:base64Image];
    }
    else
	{
        NSString * result = STRING_DATAMANAGER_PHOTOSIZE;
		SHOW_MSG(STRING_DATAMANAGER_PHOTOSIZE);
	}
	
	[picker dismissModalViewControllerAnimated:YES];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
	[picker dismissModalViewControllerAnimated:YES];
}

- (BOOL)popoverControllerShouldDismissPopover:(UIPopoverController *)popoverController {
    return YES;
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

- (IBAction)onClickedBirthday:(id)sender
{
    CustomDatePickerViewController *viewController = (CustomDatePickerViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"CustomDatePicker"];
    viewController.delegate = self;
    viewController.view.backgroundColor = [UIColor clearColor];
//    self.modalPresentationStyle = UIModalPresentationCurrentContext;
    self.navigationController.modalPresentationStyle = UIModalPresentationCurrentContext;
    self.navigationController.parentViewController.modalPresentationStyle = UIModalPresentationCurrentContext;
    [self presentViewController:viewController animated:YES completion:nil];
}

- (IBAction)onClickedChange:(id)sender
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
    
    
	if (mKeyTimeStamp != 0) {       // Got verify key. Must validate verify key
		if ([mVerifiedPhone isEqualToString:_txtPhoneNum.text]) {
			if (_txtVerifyCode.text.length == 0) {
				SHOW_MSG(@"验证码不能为空");
				[_txtVerifyCode becomeFirstResponder];
				return;
			}
            
			if (![self validateVerifyKey]) {
				SHOW_MSG(@"验证码不正确");
				[_txtVerifyCode becomeFirstResponder];
				return;
			}
		} else {
			SHOW_MSG(@"需要重新获取验证码");
			return;
		}
	} else {            // Never got verify key. Must validate phone num is original phone num
		if (![_txtPhoneNum.text isEqualToString:mOrgPhone])
		{
			SHOW_MSG(@"需要获取验证码");
			return;
		}
	}
    
	if (_txtName.text.length == 0)
	{
		SHOW_MSG(@"称呼不能为空");
		[_txtName becomeFirstResponder];
		return;
	}
    
    
	if (_txtName.text.length < 2 || _txtName.text.length > 20)
	{
		SHOW_MSG(@"称呼长度必须在2-20位之间");
		[_txtName becomeFirstResponder];
		return;
	}
    
	int validateResult = [Common validateName:_txtName.text allowChinese:NO];
	if (validateResult == -2)
	{
		SHOW_MSG(@"用户名有空格");
		[_txtName becomeFirstResponder];
		return;
	}
	else if (validateResult == 3)
	{
		SHOW_MSG(@"用户名必须以英文开头");
		[_txtName becomeFirstResponder];
		return;
	}
	else if (validateResult < 0)
	{
		SHOW_MSG(@"用户名仅支持英文或数字");
		[_txtName becomeFirstResponder];
		return;
	}


	[self callChangeUserInfo];
}

#pragma mark - change pasword tab event
- (IBAction)onClickedConfirm:(id)sender
{
	// check input box value
	if (_txtOldPW.text.length == 0)
	{
		SHOW_MSG(@"原密码不能为空");
		[_txtOldPW becomeFirstResponder];
		return;
	}
	
	if (_txtOldPW.text.length < 6 || _txtOldPW.text.length > 16)
	{
		SHOW_MSG(@"原密码长度必须在6-16位之间");
		[_txtOldPW becomeFirstResponder];
		return;
	}
	
	
	if (_txtNewPW.text.length == 0)
	{
		SHOW_MSG(@"新密码不能为空");
		[_txtNewPW becomeFirstResponder];
		return;
	}
	
	
	if (_txtNewPW.text.length < 6 || _txtNewPW.text.length > 16)
	{
		SHOW_MSG(@"新密码长度必须在6-16位之间");
		[_txtNewPW becomeFirstResponder];
		return;
	}
	
	
	if (_txtConfirmPW.text.length == 0)
	{
		SHOW_MSG(@"密码输入框不能为空");
		[_txtConfirmPW becomeFirstResponder];
		return;
	}
	
	
	if (![_txtNewPW.text isEqualToString:_txtConfirmPW.text])
	{
		SHOW_MSG(@"确认密码不一致");
		[_txtConfirmPW becomeFirstResponder];
		return;
	}

	
	
    // check verify code
    if ([[_txtNewPW text] isEqualToString:[_txtConfirmPW text]] != YES) {
        [_txtConfirmPW becomeFirstResponder];
        // show toast
		SHOW_MSG(MSG_CORRECT_SECPWD);
        return;
    }
    
    [self callChangePassword];
}

- (IBAction)onClickedCancle:(id)sender
{
    if (self.navigationController == nil)
	{
		BACK_VIEW
	}
	else
	{
		[self.navigationController popoverPresentationController];
	}
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Custom Date Picker Delegate
- (void) didChangedCustomDatePicker:(BOOL)changed date:(NSString *)date
{
    if (changed) {
        [_lblBirthday setText:date];
    }
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

- (void) callUpdateUserPhoto : (int)userid photo:(NSString *)photo
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] UpdateUserPhoto:userid userphoto:photo devtoken:[Common getDeviceMacAddress]];
}

- (void) updateUserPhotoResult:(NSString *)result photourl:(NSString *)photourl
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // change photo url
        STUserInfo * userinfo = [Common getUserInfo];
        userinfo.photo = photourl;
        [Common setUserInfo:userinfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

/**
 * call service to get all user information
 * @param : mobile [in], current user's phone number
 */
- (void) callGetUserInfo : (int)userid
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] GetUserInfo:userid devtoken:[Common getDeviceMacAddress]];
}

- (void) getUserInfoResult : (NSString *)result userinfo:(STUserInfo *)userinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        [Common setUserInfo:userinfo];
        // set data to controls
        [self updateUI:userinfo];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


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
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}


/**
 * call change user info service
 * @param : userinfo [in],
 */
- (void) callChangeUserInfo
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    

    // make service param
    NSString * sex =[NSString stringWithFormat:@"%d",([_chkMale isSelected] == YES) ? SEX_MALE : SEX_FEMALE];
        
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] ChangeUserInfo:[Common getUserID] mobile:[_txtPhoneNum text] nickname:[_txtName text] birthday:[_lblBirthday text] sex:sex devtoken:[Common getDeviceMacAddress]];
}

- (void) changeUserInfoResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:2];
        
//        // make new user info
//        STUserInfo * userinfo = [Common getUserInfo];
//        int photoState = (mBmpUserPhoto == nil) ? PHOTO_UNCHANGED : PHOTO_CHANGED;
//        if (photoState == PHOTO_CHANGED) {
//            NSData *data = UIImageJPEGRepresentation(mBmpUserPhoto, 0.5);
//            userinfo.photo = [Common base64forData:data];
//        }
//        userinfo.mobile = [_txtPhoneNum text];
//        userinfo.nickname = [_txtName text];
//        userinfo.sex = ([_chkMale isSelected] == YES) ? SEX_MALE : SEX_FEMALE;
//        userinfo.birthday = [_lblBirthday text];
//        // set user info
//        [Common setUserInfo:userinfo];
        
        [self callGetUserInfo:[Common getUserID]];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
}

/**
 * call change password service
 * @param : userinfo [in],
 */
- (void) callChangePassword
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] ChangePassword:[Common getUserID] oldpwd:[_txtOldPW text] newpwd:[_txtNewPW text] devtoken:[Common getDeviceMacAddress]];
}

- (void) changePasswordResult : (NSString *)result;
{
	if ([result isEqualToString:SVCERR_MSG_SUCCESS])
	{
		[SVProgressHUD dismissWithSuccess:@"您的密码已经重置，请牢记！" afterDelay:DEF_DELAY];
	}
	else
	{
		[SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
	}
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
	if (textField == _txtName)
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
	else if (textField == _txtOldPW)
	{
		nMaxLength = 16;
	}
	else if (textField == _txtNewPW)
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
