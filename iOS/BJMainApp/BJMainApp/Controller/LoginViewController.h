//
//  LoginViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    // user information
    IBOutlet UITextField *              _txtUsername;
    IBOutlet UITextField *              _txtPassword;
    IBOutlet UIButton *                 _chkAutoLogin;
    IBOutlet UIButton *                 _chkSavePW;
    
    /********************** Member Variable ***********************/
    BOOL                                keyboardVisible;
    enum MAIN_TAB_ID                         mTargetTab;
    
}

@property (nonatomic, readwrite) enum MAIN_TAB_ID        mTargetTab;


- (IBAction)onClickedBack:(id)sender;

- (IBAction)onClickedLogin:(id)sender;



@end
