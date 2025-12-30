//
//  Cus_OrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_OrderDetViewController : SuperViewController <OrderSvcDelegate>
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
    
    // order info control (wait operate)
    IBOutlet UIView *                   _vwOrderInfo;
    IBOutlet UILabel *                  _lblOrderNum;
    IBOutlet UILabel *                  _lblOrderState;
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblOrderPubTime;
    IBOutlet UILabel *                  _lblMidPoints;
    
    IBOutlet UILabel *                  _lblCarNum;
    IBOutlet UILabel *                  _lblElectricTicket;
    
    // order info control (after operate)
    IBOutlet UIView *                   _vwOrderInfo1;
    IBOutlet UILabel *                  _lblOrderNum1;
    IBOutlet UILabel *                  _lblOrderState1;
    IBOutlet UILabel *                  _lblStartPos1;
    IBOutlet UILabel *                  _lblEndPos1;
    IBOutlet UILabel *                  _lblOrderPubTime1;
    IBOutlet UILabel *                  _lblOrderStartTime1;
    IBOutlet UILabel *                  _lblMidPoints1;
    
    IBOutlet UIButton *                 _btnOperate1;
    IBOutlet UIButton *                 _btnOperate2;
    
    /*********************** Member Variable ********************/
    NSTimer *                           mCountDownTimer;
    
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

// main control event
- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedRefresh:(id)sender;

// wait operate view
- (IBAction)onClickedCallDriver:(id)sender;
- (IBAction)onClickedDriverPos:(id)sender;

- (IBAction)onClickedCancelOrder:(id)sender;
- (IBAction)onClickedComplain:(id)sender;

// after operate view
- (IBAction)onClickedCallDriverAfter:(id)sender;
- (IBAction)onClickedPayOrEval:(id)sender;
- (IBAction)onClickedComplainAfter:(id)sender;
- (IBAction)onClickedInsuranceDetail:(id)sender;

@end
