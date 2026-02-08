//
//  BindAccountViewController.h
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_BindAccountViewController : SuperViewController <UITextFieldDelegate, UITextViewDelegate, UIPickerViewDelegate, UIAlertViewDelegate, TradeSvcDelegate>
{
	/********************** UI Controls ***************************/
	IBOutlet UILabel *          _lblOldAccount;
	IBOutlet UILabel *          _lblBank;
	IBOutlet UITextField *      _txtNewAccount;
	IBOutlet UITextField *      _txtOpenBranch;

	IBOutlet UIView *           _bankSelView;
	IBOutlet UIPickerView  *    _pickerBank;

	/********************** Member Variable ***********************/
	BOOL                        keyboardVisible;

	NSString *                  mOrgAccount;

	NSMutableArray *            mBankArray;
	NSString *                  mTempBank;
}


@property (nonatomic, retain) NSString *        mOrgAccount;


- (IBAction)onClickedBack:(id)sender;

- (IBAction)onBankSelect:(id)sender;
- (IBAction)onClickedBind:(id)sender;
- (IBAction)onClickedDelBind:(id)sender;

- (IBAction)onCancelBankClicked:(id)sender;
- (IBAction)onDoneBankClicked:(id)sender;


@end

