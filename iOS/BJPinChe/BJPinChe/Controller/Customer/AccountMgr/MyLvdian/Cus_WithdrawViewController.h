//
//  WithdrawViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_WithdrawViewController : SuperViewController <UITextFieldDelegate, UITextViewDelegate, TradeSvcDelegate>
{
    /********************** UI Controls ***************************/
    IBOutlet UILabel *              _lblRealName;
    IBOutlet UILabel *              _lblAccount;
    IBOutlet UITextField *          _txtWithdrawVal;
    IBOutlet UITextField *          _txtPassword;
    
    /********************** Member Variable ***********************/
    STBindAccount *                 mBindAccount;
}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onChangeBindAccount:(id)sender;
- (IBAction)onConfirmWithdraw:(id)sender;

// bottom menu event
- (IBAction)onClickedMenuAccum:(id)sender;
- (IBAction)onClickedMenuCharge:(id)sender;
- (IBAction)onClickedForgetPWD:(id)sender;

@end
