//
//  ForgetPWViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ForgetPWDVC : SuperViewController <UITextFieldDelegate, UITextViewDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    // user information
    IBOutlet UITextField *              _txtUserName;
    IBOutlet UITextField *              _txtPhoneNum;
    IBOutlet UITextField *              _txtVerifyCode;
    IBOutlet UIButton *                 _markVerify;
    IBOutlet UITextField *              _txtPassword;
    IBOutlet UITextField *              _txtConfirmPW;
    
    IBOutlet UIScrollView *myScrollView;
    
    /********************** Member Variable ***********************/

	/***************** Verify key concerned variables *************/
	/********************** Added by KHM **************************/
	NSString*							mVerifiedPhone;
	long								mKeyTimeStamp;
	NSString *                          mVerifKey;
	NSTimer*							mCountdownTimer;

	IBOutlet UIButton*					btnGetVerifyKey;
    
    int btnEnbled;

}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedVerfiyCode:(id)sender;

- (IBAction)onClickedConfirm:(id)sender;
- (IBAction)onClickedCancel:(id)sender;



@end
