//
//  Drv_OrderPerformViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LDProgressView.h"
#import "PasswordCheckModal.h"

@interface Drv_OrderPerformViewController : SuperViewController <pwdPopupDelegate, OrderSvcDelegate, OrderExecuteSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet LDProgressView *               _progressBar;
    IBOutlet UILabel *                      _lblStartAddr;
    IBOutlet UILabel *                      _lblEndAddr;
    IBOutlet UILabel *                      _lblStartTime;
    IBOutlet UILabel *                      _lblUsedTime;
    IBOutlet UILabel *                      _lblUsedDistance;
    
    IBOutlet UIImageView *                  _imgCar;
    
    IBOutlet UIScrollView *_myScroll;
    IBOutlet UIButton *_btnSetCus;
    
    IBOutlet UIButton *_btnCheckCus;
    
     IBOutlet UIButton *_btnFinish;
    
    /********************** Member Variable ***********************/
    NSTimer *                               mCountDownTimer;
    STDetailedDrvOrderInfo *				mOrderInfo;
    CLLocationCoordinate2D                  mOldCoord;
}

@property (nonatomic, readwrite) long	mOrderId;
@property (nonatomic, readwrite) enum ORDER_TYPE	mOrderType;
@property (nonatomic, readwrite) double	mCurRunDistance;
@property (nonatomic, readwrite) long	mTotalTime;

- (IBAction)onClickedBack:(id)sender;

- (IBAction)onClickedCallCustomer:(id)sender;
- (IBAction)onClickedSetCustomer:(id)sender;
- (IBAction)onClickedChkCustomer:(id)sender;
- (IBAction)onClickedFinish:(id)sender;
- (IBAction)onClickedComplain:(id)sender;

@end
