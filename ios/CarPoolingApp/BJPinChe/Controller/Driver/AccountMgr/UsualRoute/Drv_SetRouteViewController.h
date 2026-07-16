//
//  Drv_SetRouteViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-20.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddrSelectorViewController.h"

@interface Drv_SetRouteViewController : SuperViewController <AddrSelDelegate, AccountSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *              _lblStartPos;
    IBOutlet UILabel *              _lblEndPos;
    IBOutlet UILabel *              _lblTime;
    
    IBOutlet UIButton *             _radioWorkType1;
    IBOutlet UIButton *             _radioWorkType2;
    IBOutlet UIButton *             _radioWorkType3;
    
    IBOutlet UIView *               _pickerView;
    IBOutlet UIDatePicker *         _timePicker;
    
    /********************** Member Variable ***********************/
    enum ROUTE_DAYTYPE              mCurDayType;
    NSDate *                        mStartTime;
    STBaiduAddrInfo *               mStartAddr;
    STBaiduAddrInfo *               mEndAddr;
}

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedNext:(id)sender;
- (IBAction)onClickedCancel:(id)sender;

- (IBAction)onClickedType1:(id)sender;
- (IBAction)onClickedType2:(id)sender;
- (IBAction)onClickedType3:(id)sender;

- (IBAction)onClickedSP:(id)sender;
- (IBAction)onClickedSPVoice:(id)sender;
- (IBAction)onClickedEP:(id)sender;
- (IBAction)onClickedEPVoice:(id)sender;

- (IBAction)onClickedTime:(id)sender;
- (IBAction)onClickedPickerOK:(id)sender;
- (IBAction)onClickedPickerCancel:(id)sender;

@end
