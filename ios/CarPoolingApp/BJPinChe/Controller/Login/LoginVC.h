//
//  LoginViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface LoginVC : SuperViewController <UITextFieldDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    // user information
    IBOutlet UITextField *              _txtUsername;
    IBOutlet UITextField *              _txtPassword;
    
    /********************** Member Variable ***********************/
    enum MAIN_TAB_ID                         mTargetTab;
    
}
@property (weak, nonatomic) IBOutlet UIView *backView;
@property (weak, nonatomic) IBOutlet UIButton *passBtn;

@property (nonatomic, readwrite) enum MAIN_TAB_ID        mTargetTab;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedLogin:(id)sender;

- (IBAction)onClickedRegister:(id)sender;
- (IBAction)onClickedForgetPW:(id)sender;


@end
