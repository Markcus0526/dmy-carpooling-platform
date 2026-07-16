//
//  RegisterViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Cus_AgreementViewController.h"
@interface RegisterVC : SuperViewController <UITextFieldDelegate, UITextViewDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    // user information
    IBOutlet UITextField *              _txtUserName;
    IBOutlet UITextField *              _txtPhoneNum;
    IBOutlet UITextField *              _txtVerifyCode;
    IBOutlet UIButton *                 _markVerify;
    IBOutlet UITextField *              _txtRealName;
    IBOutlet UIButton *                 _chkMale;
    IBOutlet UIButton *                 _chkFemale;
    IBOutlet UITextField *              _txtPassword;
    IBOutlet UITextField *              _txtConfirmPW;
    IBOutlet UITextField *              _txtInviteCode;
    
  
    IBOutlet UIScrollView *myScrollView;
    
    /********************** Member Variable ***********************/
    enum MAIN_TAB_ID                    mTargetTab;


	
	/***************** Verify key concerned variables *************/
	/********************** Added by KHM **************************/
	NSString*							mVerifiedPhone;
	long								mKeyTimeStamp;
	NSString *                          mVerifKey;
	NSTimer*							mCountdownTimer;

	IBOutlet UIButton*					btnGetVerifyKey;
    
    BOOL xieyiFlage;
    
    NSString *btnEnable;

}

@property (nonatomic, readwrite) enum MAIN_TAB_ID        mTargetTab;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedChkMale:(id)sender;
- (IBAction)onClickedChkFemale:(id)sender;
- (IBAction)onClickedVerfiyCode:(id)sender;

- (IBAction)onClickedConfirm:(id)sender;
- (IBAction)onClickedCancel:(id)sender;

- (IBAction)xieyiClick:(id)sender;


@end
