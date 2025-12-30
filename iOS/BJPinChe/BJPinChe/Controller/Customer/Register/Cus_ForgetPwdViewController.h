//
//  ForgetPWViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/31/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_ForgetPwdViewController : SuperViewController <UITextFieldDelegate, UITextViewDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    // user information
    IBOutlet UITextField *              _txtUserName;
    IBOutlet UITextField *              _txtPhoneNum;
    IBOutlet UITextField *              _txtVerifyCode;
    IBOutlet UIButton *                 _markVerify;
    IBOutlet UITextField *              _txtPassword;
    IBOutlet UITextField *              _txtConfirmPW;
    
    
    /********************** Member Variable ***********************/
    NSString *                          mVerifKey;
    
}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedVerfiyCode:(id)sender;

- (IBAction)onClickedConfirm:(id)sender;
- (IBAction)onClickedCancel:(id)sender;



@end
