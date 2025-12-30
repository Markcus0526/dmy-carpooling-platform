//
//  UserDetailViewController.h
//  BJMainApp
//
//  Created by KimOC on 8/7/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_UserDetailViewController : SuperViewController <UIScrollViewDelegate, UITextFieldDelegate, UITextViewDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    // main control
    IBOutlet UILabel *                  _lblTitle1;
    IBOutlet UILabel *                  _lblTitle2;
    IBOutlet UILabel *                  _ctrlIndicator;
    
    IBOutlet UIScrollView *             _scrollMain;
    IBOutlet UIView *                   _vwUserInfo;
    IBOutlet UIView *                   _vwChangePW;
    
    // user information
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgCar;
    IBOutlet UITextField *              _txtPhoneNum;
    IBOutlet UITextField *              _txtVerifyCode;
    IBOutlet UIButton *                 _markVerify;
    IBOutlet UITextField *              _txtName;
    IBOutlet UIButton *                 _chkMale;
    IBOutlet UIButton *                 _chkFemale;
    IBOutlet UILabel *                  _lblBirthday;
    IBOutlet UILabel *                  _lblValidState;
    
    __weak IBOutlet UIButton *btnDriverValidState;
    __weak IBOutlet UIButton *btnValidState;
    IBOutlet UILabel *_driverValidState;
    IBOutlet UIView *                   _pickerView;
    IBOutlet UIDatePicker *             _datePicker;
    
    // change password
    IBOutlet UITextField *              _txtOldPW;
    IBOutlet UITextField *              _txtNewPW;
    IBOutlet UITextField *              _txtConfirmPW;
    
    __weak IBOutlet UIScrollView *myScrollView;
    
    /********************** Member Variable ***********************/
    int                                 mCurTab;
	
    UIImage *                           mBmpUserPhoto;
    UIImage *                           mBmpCarPhoto;
    NSInteger                           mPhotoSelect;


	/***************** Verify key concerned variables *************/
	/********************** Added by KHM **************************/
	NSString*							mVerifiedPhone;
	long								mKeyTimeStamp;
	NSString *                          mVerifKey;
	NSString *							mOrgPhone;
	NSTimer*							mCountdownTimer;

	IBOutlet UIButton*					btnGetVerifyKey;

}

@property (nonatomic, retain) UIImageView *     _imgUser;

// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedView1:(id)sender;
- (IBAction)onClickedView2:(id)sender;
- (IBAction)btnClick:(id)sender;

// User information tab event
- (IBAction)onClickedUserImg:(id)sender;
- (IBAction)onClickedCarImg:(id)sender;
- (IBAction)onClickedChkMale:(id)sender;
- (IBAction)onClickedChkFemale:(id)sender;
- (IBAction)onClickedVerfiyCode:(id)sender;
- (IBAction)onClickedChange:(id)sender;
- (IBAction)onClickedBirthday:(id)sender;

// change pasword tab event
- (IBAction)onClickedConfirm:(id)sender;
- (IBAction)onClickedCancle:(id)sender;

- (IBAction)onClickedPickerOK:(id)sender;
- (IBAction)onClickedPickerCancel:(id)sender;

@end
