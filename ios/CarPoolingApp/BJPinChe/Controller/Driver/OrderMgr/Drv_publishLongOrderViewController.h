//
//  Drv_publishLongOrderViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-30.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "SuperViewController.h"
#import "AddrSelectorViewController.h"
#import "iflyMSC/IFlyRecognizerViewDelegate.h"
@interface Drv_publishLongOrderViewController : SuperViewController<UITextFieldDelegate,UITextViewDelegate,AddrSelDelegate,LongWayOrderSvcDelegate,IFlyRecognizerViewDelegate,LongWayOrderSvcDelegate>
@property (weak, nonatomic) IBOutlet UITextField *startCity;
@property (weak, nonatomic) IBOutlet UITextField *startCityDetail;
@property (weak, nonatomic) IBOutlet UITextField *endCity;
@property (weak, nonatomic) IBOutlet UITextField *endCityDetail;
@property (weak, nonatomic) IBOutlet UILabel *infoFee;
- (IBAction)OnStartCityVoiceButtonClick:(id)sender;
- (IBAction)OnEndCityVoiceButtonClick:(id)sender;
- (IBAction)OnSelectStartDateClick:(id)sender;
- (IBAction)OnPublishClick:(id)sender;
- (IBAction)OnSiteNumberminusClick:(id)sender;
- (IBAction)OnPriceMinusClick:(id)sender;
- (IBAction)OnSiteNumberPlusClick:(id)sender;
- (IBAction)OnPricePlusClick:(id)sender;
@property (weak, nonatomic) IBOutlet UIButton *selectStartDateButton;
@property (weak, nonatomic) IBOutlet UITextField *priceTextField;
@property (weak, nonatomic) IBOutlet UITextField *siteNumberTextField;
@property (weak, nonatomic) IBOutlet UITextView *remarkTextView;
@property (weak, nonatomic) IBOutlet UIView *datePickerView;
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollMain;
- (IBAction)OnDatePickerCancelClick:(id)sender;
- (IBAction)OnDatePickerSureClick:(id)sender;
- (IBAction)OnBackView:(id)sender;

@end
