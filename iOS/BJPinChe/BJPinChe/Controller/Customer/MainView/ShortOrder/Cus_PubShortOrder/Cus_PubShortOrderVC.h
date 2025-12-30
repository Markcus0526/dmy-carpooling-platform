//
//  Cus_PubShortOrderVC.h
//  BJPinChe
//
//  Created by APP_USER on 14-10-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddrSelectorViewController.h"
#import "Cus_SetMidPointViewController.h"
#import "iflyMSC/IFlyRecognizerViewDelegate.h"

@class BMKUserLocation;

@interface Cus_PubShortOrderVC : SuperViewController

<UIScrollViewDelegate, UITextFieldDelegate, UITextViewDelegate, AddrSelDelegate, MidPointSelDelegate, OrderSvcDelegate, IFlyRecognizerViewDelegate,TradeSvcDelegate,GlobalSvcDelegate>
{
    /********************** UI Controls **********************/
    // main control
//    IBOutlet UILabel *                  _lblTitle1;
//    IBOutlet UILabel *                  _lblTitle2;
//    IBOutlet UILabel *                  _ctrlIndicator;
    
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
    
    IBOutlet UILabel *distance_Label;
    
     IBOutlet UILabel *price_Label;
    __weak IBOutlet UILabel *taxiLabel;

    IBOutlet UILabel *taxi_priceLabel;
    IBOutlet UIView *                   _pickerView;
    IBOutlet UIDatePicker *             _datePicker;
    
    IBOutlet UIView *                   _wtAlertView;
    
    
    IBOutlet UIImageView *iconImagView;
    
    IBOutlet UIImageView *calculatingImageView;
    /********************** Member Variable ***********************/
    int                                 mCurTab;

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
    __weak IBOutlet UIView *jisuanView;
    
    NSString *saveUser;
}
@property(nonatomic,strong)BMKUserLocation *userLocation;
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

- (IBAction)onClickedMit:(id)sender;

- (IBAction)onClickedSTCarType1:(id)sender;
- (IBAction)onClickedSTCarType2:(id)sender;
- (IBAction)onClickedSTCarType3:(id)sender;
- (IBAction)onClickedSTCarType4:(id)sender;
- (IBAction)onClickedSTMinus:(id)sender;
- (IBAction)onClickedSTPlus:(id)sender;


- (IBAction)onSelectDay:(id)sender;


- (IBAction)onClickedPickerOK:(id)sender;
- (IBAction)onClickedPickerCancel:(id)sender;

// st alert view background
- (IBAction)onClickedAlertBG:(id)sender;



@end
