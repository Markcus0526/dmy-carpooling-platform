//
//  Cus_SetRouteViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-20.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//   设置日常路线界面

#import <UIKit/UIKit.h>
#import "AddrSelectorViewController.h"
#import "iflyMSC/IFlySpeechConstant.h"
#import "iflyMSC/IFlySpeechUtility.h"
#import "iflyMSC/IFlyRecognizerView.h"


@interface Cus_SetRouteViewController : SuperViewController <AddrSelDelegate, AccountSvcDelegate, UITextFieldDelegate, IFlyRecognizerViewDelegate>
{
	IBOutlet UILabel *				_lblTitle;
	IBOutlet UITextField *			_txtStartCity;
	IBOutlet UITextField *			_txtEndCity;

	IBOutlet UILabel *              _lblStartPos;
	IBOutlet UILabel *              _lblEndPos;
	IBOutlet UILabel *              _lblTime;

	IBOutlet UIView *               _pickerView;
	IBOutlet UIDatePicker *         _timePicker;

	NSDate *                        mStartTime;
	STBaiduAddrInfo *               mStartAddr;
	STBaiduAddrInfo *               mEndAddr;
}


@property(nonatomic, readwrite) NSInteger				mSaveType;
@property(nonatomic, retain) STRouteInfo *              mDataInfo;


- (IBAction)onClickedBack:(id)sender;

- (IBAction)onClickedSP:(id)sender;
- (IBAction)onClickedSPVoice:(id)sender;
- (IBAction)onClickedEP:(id)sender;
- (IBAction)onClickedEPVoice:(id)sender;

- (IBAction)onClickedTime:(id)sender;
- (IBAction)onClickedPickerOK:(id)sender;
- (IBAction)onClickedPickerCancel:(id)sender;

@end
