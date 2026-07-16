//
//  WithdrawViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WithdrawViewController : UIViewController <UITextFieldDelegate, UITextViewDelegate, TradeSvcDelegate>
{
    /********************** UI Controls ***************************/
    IBOutlet UILabel *              _lblRealName;
    IBOutlet UILabel *              _lblAccount;
    IBOutlet UITextField *          _txtWithdrawVal;
    IBOutlet UITextField *          _txtPassword;
    
    /********************** Member Variable ***********************/
    BOOL                            keyboardVisible;
    STBindAccount *                 mBindAccount;
}

- (void) updateData;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onChangeBindAccount:(id)sender;
- (IBAction)onConfirmWithdraw:(id)sender;
- (IBAction)onTapForgetPwd:(id)sender;


@end
