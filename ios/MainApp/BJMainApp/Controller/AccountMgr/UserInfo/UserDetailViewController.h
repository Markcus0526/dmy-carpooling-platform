//
//  UserDetailViewController.h
//  BJMainApp
//
//  Created by KimOC on 8/7/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CustomDatePickerViewController.h"

@interface UserDetailViewController : UIViewController <UIScrollViewDelegate, UITextFieldDelegate, UITextViewDelegate, AccountSvcDelegate, CustomDatePickerDelegate>
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
    IBOutlet UITextField *              _txtPhoneNum;
    IBOutlet UITextField *              _txtVerifyCode;
    IBOutlet UIButton *                 _markVerify;
    IBOutlet UITextField *              _txtName;
    IBOutlet UIButton *                 _chkMale;
    IBOutlet UIButton *                 _chkFemale;
    IBOutlet UILabel *                  _lblBirthday;
    IBOutlet UILabel *                  _lblValidState;
    
    // change password
    IBOutlet UITextField *              _txtOldPW;
    IBOutlet UITextField *              _txtNewPW;
    IBOutlet UITextField *              _txtConfirmPW;
    
    IBOutlet UIButton*					btnGetVerifyKey;
    IBOutlet UIButton*					btnDoVerify;
    
    /********************** Member Variable ***********************/
    int                                 mCurTab;
    BOOL                                keyboardVisible;
    
    NSString*							mVerifiedPhone;
	long								mKeyTimeStamp;
	NSString *                          mVerifKey;
	NSString *							mOrgPhone;
	NSTimer*							mCountdownTimer;
    
    UIImage *                           mBmpUserPhoto;
}


// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedView1:(id)sender;
- (IBAction)onClickedView2:(id)sender;

// User information tab event
- (IBAction)onClickedUserImg:(id)sender;
- (IBAction)onClickedChkMale:(id)sender;
- (IBAction)onClickedChkFemale:(id)sender;
- (IBAction)onClickedVerfiyCode:(id)sender;
- (IBAction)onClickedChange:(id)sender;
- (IBAction)onClickedBirthday:(id)sender;

// change pasword tab event
- (IBAction)onClickedConfirm:(id)sender;
- (IBAction)onClickedCancle:(id)sender;

@end
