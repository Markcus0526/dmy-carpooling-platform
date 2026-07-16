//
//  Cus_WTOrderDetMainViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/2/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Cus_EvalDriverViewController.h"

@interface Cus_WTOrderDetMainViewController : SuperViewController <OrderSvcDelegate, EvalDrvDelegate, OrderExecuteSvcDelegate>
{
    /********************** UI Controls **********************/
    // driver info control
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UIImageView *              _imgCar;
    
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;
    IBOutlet UIImageView *              _imgCarType;
    IBOutlet UIImageView *              _imgCarBrand;
    IBOutlet UIImageView *              _imgCarSubType;
    IBOutlet UILabel *                  _lblCarColor;
    
    // order info control
    IBOutlet UILabel *                  _lblOrderNum;
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblOrderPubTime;
    IBOutlet UILabel *                  _lblMidPoints;
    
    IBOutlet UIView *                   _vwOrderState;
    IBOutlet UILabel *                  _lblOrderState;
    
    IBOutlet UILabel *                  _lblOrderWait;
    IBOutlet UILabel *                  _lblCarNum;
    IBOutlet UILabel *                  _lblElectricTicket;
    
    // stop serve state controls
    IBOutlet UIView *                   _vwStopServeInfo;
    IBOutlet UILabel *                  _lblStopServeInfo1;
    IBOutlet UILabel *                  _lblStopServeInfo2;
    IBOutlet UILabel *                  _lblStopServeInfo3;
    
    // buttons
    IBOutlet UIButton *                 _btnOperate1;       // circle
    IBOutlet UIButton *                 _btnOperate2;       // circle
    
    IBOutlet UIButton *                 _btnStopOneDay;
    IBOutlet UIButton *                 _btnStopServeSmall;
    IBOutlet UIButton *                 _btnStopServeWide;
    
    /*********************** Member Variable ********************/
    STDetailedCusOrderInfo *            mDetOrderInfo;
}

@property (nonatomic, retain) STOrderInfo *mOrderInfo;

// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedRefresh:(id)sender;

// circle operate button
- (IBAction)onClickedBtnOperate1:(id)sender;
- (IBAction)onClickedBtnOperate2:(id)sender;

- (IBAction)onClickedStopOneDay:(id)sender;
- (IBAction)onClickedCancelServe:(id)sender;
- (IBAction)onClickedComplain:(id)sender;


@end
