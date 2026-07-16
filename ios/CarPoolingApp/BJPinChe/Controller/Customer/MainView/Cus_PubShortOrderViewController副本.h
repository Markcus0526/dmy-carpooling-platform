//
//  Cus_PubShortOrderViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddrSelectorViewController.h"
#import "Cus_SetMidPointViewController.h"
#import "iflyMSC/IFlyRecognizerViewDelegate.h"

@interface Cus_PubShortOrderViewController : SuperViewController <UIScrollViewDelegate, UITextFieldDelegate, UITextViewDelegate, AddrSelDelegate, MidPointSelDelegate, OrderSvcDelegate, IFlyRecognizerViewDelegate>
{
    /********************** UI Controls **********************/
    // main control
    IBOutlet UILabel *                  _lblTitle1;
    IBOutlet UILabel *                  _lblTitle2;
    IBOutlet UILabel *                  _ctrlIndicator;
    
    IBOutlet UIScrollView *             _scrollMain;
    IBOutlet UIView *                   _vwSingleOrder;
    IBOutlet UIView *                   _vwWorkOrder;
    
    // single time order
    IBOutlet UILabel *                  _lblSTStartAddr;
    IBOutlet UILabel *                  _lblSTEndAddr;
    IBOutlet UIButton *                 _btnSTTime;
    IBOutlet UILabel *                  _lblSTMidPointCnt;
    IBOutlet UITextField *              _txtSTNote;
    
    IBOutlet UIButton *                 _st_carType1;
    IBOutlet UILabel *                  _st_carType1name;
    IBOutlet UIButton *                 _st_carType2;
    IBOutlet UILabel *                  _st_carType2name;
    IBOutlet UIButton *                 _st_carType3;
    IBOutlet UILabel *                  _st_carType3name;
    IBOutlet UIButton *                 _st_carType4;
    IBOutlet UILabel *                  _st_carType4name;
    
    IBOutlet UILabel *                  _lblSTContents;
    IBOutlet UITextField *              _txtSTValue;
    
    
    // work time order
    IBOutlet UILabel *                  _lblWTStartAddr;
    IBOutlet UILabel *                  _lblWTEndAddr;
    IBOutlet UIButton *                 _btnWTTime;
    IBOutlet UILabel *                  _lblWTMidPointCnt;
    IBOutlet UITextField *              _txtWTNote;
    
    IBOutlet UIButton *                 _wt_carType1;
    IBOutlet UILabel *                  _wt_carType1name;
    IBOutlet UIButton *                 _wt_carType2;
    IBOutlet UILabel *                  _wt_carType2name;
    IBOutlet UIButton *                 _wt_carType3;
    IBOutlet UILabel *                  _wt_carType3name;
    IBOutlet UIButton *                 _wt_carType4;
    IBOutlet UILabel *                  _wt_carType4name;
    
    IBOutlet UILabel *                  _lblWTContents;
    IBOutlet UITextField *              _txtWTValue;
    
    IBOutlet UIView *                   _pickerView;
    IBOutlet UIDatePicker *             _datePicker;
    
    IBOutlet UIView *                   _wtAlertView;
    
    
    /********************** Member Variable ***********************/
    int                                 mCurTab;
    BOOL                                keyboardVisible;
    STBaiduAddrInfo *                   mSTStartAddr;
    STBaiduAddrInfo *                   mSTEndAddr;
    STBaiduAddrInfo *                   mWTStartAddr;
    STBaiduAddrInfo *                   mWTEndAddr;
    
    NSMutableArray *                    mSTMidPoints;
    NSMutableArray *                    mWTMidPoints;
    
    int                                 mSTCarType;
    int                                 mWTCarType;
    
	STOnceOrderPubData *				mOrderPubData;
	
    double                              mSTPrice;
    double                              mWTPrice;
    
    IFlyRecognizerView *                iflyRecognizerView;
    NSInteger                           mSelectedVoiceTag;
}

// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedView1:(id)sender;
- (IBAction)onClickedView2:(id)sender;
- (IBAction)onClickedSTPublish:(id)sender;
- (IBAction)onClickedWTPublish:(id)sender;

// single time order control event
- (IBAction)onClickedSTStartPos:(id)sender;
- (IBAction)onClickedSTEndPos:(id)sender;
- (IBAction)onClickedSTStartPosVoice:(id)sender;
- (IBAction)onClickedSTEndPosVoice:(id)sender;
- (IBAction)onClickedSTTime:(id)sender;
- (IBAction)onClickedSTCarType1:(id)sender;
- (IBAction)onClickedSTCarType2:(id)sender;
- (IBAction)onClickedSTCarType3:(id)sender;
- (IBAction)onClickedSTCarType4:(id)sender;
- (IBAction)onClickedSTMinus:(id)sender;
- (IBAction)onClickedSTPlus:(id)sender;

// work time order control event
- (IBAction)onClickedWTStartPos:(id)sender;
- (IBAction)onClickedWTEndPos:(id)sender;
- (IBAction)onClickedWTStartPosVoice:(id)sender;
- (IBAction)onClickedWTEndPosVoice:(id)sender;
- (IBAction)onClickedWTTime:(id)sender;
- (IBAction)onClickedWTCarType1:(id)sender;
- (IBAction)onClickedWTCarType2:(id)sender;
- (IBAction)onClickedWTCarType3:(id)sender;
- (IBAction)onClickedWTCarType4:(id)sender;
- (IBAction)onClickedWTMinus:(id)sender;
- (IBAction)onClickedWTPlus:(id)sender;
- (IBAction)onSelectDay:(id)sender;


- (IBAction)onClickedPickerOK:(id)sender;
- (IBAction)onClickedPickerCancel:(id)sender;

// st alert view background
- (IBAction)onClickedAlertBG:(id)sender;


@end
