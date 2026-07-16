//
//  UserDetailViewController.m
//  BJMainApp
//
//  Created by KimOC on 8/7/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_UserDetailViewController.h"
#import "RDActionSheet.h"
#import "NonRotateImagePickerController.h"
#import "ImageHelper.h"
#import "Cus_VerifyUserViewController.h"

@interface Drv_UserDetailViewController ()

@end


#define TABCOUNT                    2
#define PHOTO_CHANGED               1
#define PHOTO_UNCHANGED             0


@implementation Drv_UserDetailViewController

@synthesize _imgUser;

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

	[_pickerView setHidden:YES];

	// initialize news view frame
	CGRect rcFrame = _vwUserInfo.frame;
	rcFrame = CGRectMake(_vwUserInfo.frame.size.width, rcFrame.origin.y, rcFrame.size.width, rcFrame.size.height);
	_vwChangePW.frame = rcFrame;

	// initialize main scroll content size
    [_scrollMain setContentSize:CGSizeMake(_vwUserInfo.frame.size.width + _vwChangePW.frame.size.width, _vwUserInfo.frame.size.height)];
    
    UIScreen *currentScreen = [UIScreen mainScreen];
    
    if (currentScreen.applicationFrame.size.height<500) {
        [myScrollView setContentSize:CGSizeMake(_vwUserInfo.frame.size.width, _vwUserInfo.frame.size.height+90)];
    }

	_scrollMain.panGestureRecognizer.delaysTouchesBegan = _scrollMain.delaysContentTouches;

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
    
    [btnDriverValidState setHidden:NO];
    [btnValidState setHidden:NO];
    
    switch ((enum VERIFY_STATE)userInfo.person_verified) {
        case STATE_NOTVERIFIED:
            [_lblValidState setText:STR_NOTVERIFIED];
            break;
        case STATE_VERIFIED:
            [btnValidState setHidden:YES];
            [_lblValidState setText:STR_VERIFIED];
            break;
        case STATE_WATING:
            [btnValidState setHidden:YES];
            [_lblValidState setText:STR_VERI_WAITING];
            break;
            
        default:
            [_lblValidState setText:STR_NOTVERIFIED];
            break;
    }
    
    switch ((enum VERIFY_STATE)userInfo.driver_verified) {
        case STATE_NOTVERIFIED:
            [_driverValidState setText:STR_NOTVERIFIED];
            break;
        case STATE_VERIFIED:
            [btnDriverValidState setHidden:YES];
            [_driverValidState setText:STR_VERIFIED];
            break;
        case STATE_WATING:
            [btnDriverValidState setHidden:YES];
            [_driverValidState setText:STR_VERI_WAITING];
            break;
            
        default:
            [_driverValidState setText:STR_NOTVERIFIED];
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
        [_imgUser setImageWithURL:[NSURL URLWithUnicodeString:userInfo.photo] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
		[_imgCar setImageWithURL:[NSURL URLWithUnicodeString:userInfo.carimg] placeholderImage:[UIImage imageNamed:@"demo_carimg.png"]];
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

- (IBAction)btnClick:(id)sender {
    UIStoryboard *story =[UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    Cus_VerifyUserViewController *cus = [story instantiateViewControllerWithIdentifier:@"person_verify"];
    [self.navigationController pushViewController:cus animated:YES];
}

#pragma mark -  User information event
/**
 * clicked change user image
 */
- (IBAction)onClickedUserImg:(id)sender
{
    mPhotoSelect = 0;
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
//                    [self presentModalViewController:picker animated:YES];
                    [self presentViewController:picker animated:YES completion:nil];
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


- (IBAction)onClickedCarImg:(id)sender
{
    mPhotoSelect = 1;
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
//                    [self presentModalViewController:picker animated:YES];
                    [self presentViewController:picker animated:YES completion:nil];
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
        if(mPhotoSelect == 0)
        {
            mBmpUserPhoto = image;
            [_imgUser setImage:mBmpUserPhoto];
        }
        else
        {
            mBmpCarPhoto = image;
            [_imgCar setImage:mBmpCarPhoto];
        }
    }
    else
	{
        [self.view makeToast:STRING_DATAMANAGER_PHOTOSIZE duration:DEF_DELAY position:@"center"];
	}
	
//	[picker dismissModalViewControllerAnimated:YES];
    [picker dismissViewControllerAnimated:YES completion:nil];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
//	[picker dismissModalViewControllerAnimated:YES];
    [picker dismissViewControllerAnimated:YES completion:nil];
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

//	if (_txtName.text.length == 0)
//	{
//		[self.view makeToast:@"用户名不能为空" duration:2 position:@"center"];
//		[_txtName becomeFirstResponder];
//		return;
//	}
//	
//	if (_txtName.text.length < 2 ||
//		_txtName.text.length > 20)
//	{
//		[self.view makeToast:@"用户名长度必须在2-20位之间" duration:2 position:@"center"];
//		[_txtName becomeFirstResponder];
//		return;
//	}
	
//	if (![Common validateName:_txtName.text allowChinese:NO])
//	{
//		[self.view makeToast:@"用户名格式不准确" duration:2 position:@"center"];
//		[_txtName becomeFirstResponder];
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
	
	[self callGetVerifKey:mobile];
}

- (IBAction)onClickedBirthday:(id)sender
{
    [_pickerView setHidden:NO];
}

- (IBAction)onClickedChange:(id)sender
{
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
                if (_txtVerifyCode.text.length == 0) {
                    [self.view makeToast:@"验证码不能为空" duration:2 position:@"center"];
                    [_txtVerifyCode becomeFirstResponder];
                    return;
                }
                
                if (![[self validateVerifyKey] isEqualToString:@""]) {
                    [self.view makeToast:[self validateVerifyKey] duration:2 position:@"center"];
                    [_txtVerifyCode becomeFirstResponder];
                    return;
                }
            } else {
                [self.view makeToast:@"需要重新获取验证码" duration:2 position:@"center"];
                return;
            }
        } else {            // Never got verify key. Must validate phone num is original phone num
            //		if (![_txtPhoneNum.text isEqualToString:mOrgPhone])
            //		{
            [self.view makeToast:@"需要获取验证码" duration:2 position:@"center"];
            return;
            //		}
        }
    }

	if (_txtName.text.length == 0)
	{
		[self.view makeToast:@"称呼不能为空" duration:2 position:@"center"];
		[_txtName becomeFirstResponder];
		return;
	}


	if (_txtName.text.length < 2 || _txtName.text.length > 20)
	{
		[self.view makeToast:@"昵称长度必须在2-20位之间" duration:2 position:@"center"];
		[_txtName becomeFirstResponder];
		return;
	}

//	if (![Common validateName:_txtName.text allowChinese:YES])
//	{
//		[self.view makeToast:@"昵称格式不准确" duration:2 position:@"center"];
//		[_txtName becomeFirstResponder];
//		return;
//	}


	[self callChangeUserInfo];
}

#pragma mark - change pasword tab event
- (IBAction)onClickedConfirm:(id)sender
{
    // check input box value
    if ([[_txtOldPW text] isEqualToString:@""] == YES) {
        [_txtOldPW becomeFirstResponder];
        [SVProgressHUD showErrorWithStatus:@"原密码不能为空" duration:1];
        return;
    }
    if ([[_txtNewPW text] isEqualToString:@""] == YES) {
        [_txtNewPW becomeFirstResponder];
         [SVProgressHUD showErrorWithStatus:@"新密码不能为空" duration:1];
        return;
    }
    if ([[_txtConfirmPW text] isEqualToString:@""] == YES) {
        [_txtConfirmPW becomeFirstResponder];
         [SVProgressHUD showErrorWithStatus:@"再次输入不能为空" duration:1];
        return;
    }
    
    // check verify code
    if ([[_txtNewPW text] isEqualToString:[_txtConfirmPW text]] != YES) {
        [_txtConfirmPW becomeFirstResponder];
        // show toast
        [self.view makeToast:MSG_CORRECT_SECPWD duration:2.0 position:@"center"];
        return;
    }
    
    [self callChangePassword];
}

- (IBAction)onClickedCancle:(id)sender
{
    BACK_VIEW;
}


- (IBAction)onClickedPickerOK:(id)sender
{
    NSDate *selected = [_datePicker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *destDateString = [dateFormatter stringFromDate:selected];
    
    [_lblBirthday setText:destDateString];
    
    [_pickerView setHidden:YES];
}

- (IBAction)onClickedPickerCancel:(id)sender
{
    [_pickerView setHidden:YES];
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation


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

		mOrgPhone = userinfo.mobile;

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

		[self startCountDownTimer];

		//-- option show key
//		[_txtVerifyCode setText:mVerifKey];
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
    int sex = ([_chkMale isSelected] == YES) ? SEX_MALE : SEX_FEMALE;
    int photoState = (mBmpUserPhoto == nil) ? PHOTO_UNCHANGED : PHOTO_CHANGED;
	int carImgState = (mBmpCarPhoto == nil) ? PHOTO_UNCHANGED : PHOTO_CHANGED;
    
    NSString * base64Image = @"";
    if (photoState == PHOTO_CHANGED) {
        NSData *data = UIImageJPEGRepresentation(mBmpUserPhoto, 0.5);
        base64Image = [Common base64forData:data];
    }
	
	NSString * base64CarImage = @"";
    if (carImgState == PHOTO_CHANGED) {
        NSData *data = UIImageJPEGRepresentation(mBmpCarPhoto, 0.5);
        base64CarImage = [Common base64forData:data];
    }

	// Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] ChangeUserInfo:[Common getUserID] mobile:[_txtPhoneNum text] nickname:[_txtName text] birthday:[_lblBirthday text] sex:[NSString stringWithFormat:@"%d", sex] photo:base64Image photo_changed:photoState carimg:base64CarImage carimg_changed:carImgState devtoken:[Common getDeviceMacAddress]];
}
-(void)duplicateUser:(NSString *)result
{
    [SVProgressHUD dismissWithError:@"修改失败" afterDelay:DEF_DELAY];
}
- (void) changeUserInfoResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:2];
        
        // make new user info
        STUserInfo * userinfo = [Common getUserInfo];
        
        int photoState = (mBmpUserPhoto == nil) ? PHOTO_UNCHANGED : PHOTO_CHANGED;
        if (photoState == PHOTO_CHANGED) {
            NSData *data = UIImageJPEGRepresentation(mBmpUserPhoto, 0.5);
            userinfo.photo = [Common base64forData:data];
        }
        userinfo.mobile = [_txtPhoneNum text];
        userinfo.nickname = [_txtName text];
        userinfo.sex = ([_chkMale isSelected] == YES) ? SEX_MALE : SEX_FEMALE;
        userinfo.birthday = @"1990-6-29";
        // set user info
        [Common setUserInfo:userinfo];
        
        [self stopCountDownTimer];
        [btnGetVerifyKey setEnabled:YES];
        [btnGetVerifyKey setBackgroundImage:[UIImage imageNamed:@"btnGreen_normal.png"] forState:UIControlStateNormal];
        [btnGetVerifyKey setBackgroundColor:[UIColor clearColor]];
        btnGetVerifyKey.layer.cornerRadius = 0;
        [btnGetVerifyKey setTitle:@"获取验证码" forState:UIControlStateNormal];
        
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
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
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
