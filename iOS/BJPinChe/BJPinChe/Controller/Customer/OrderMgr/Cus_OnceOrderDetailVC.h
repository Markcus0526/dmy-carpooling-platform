//
//  Cus_OrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Cus_OnceOrderDetailVC : SuperViewController <OrderSvcDelegate, UIAlertViewDelegate>
{
    /********************** UI Controls **********************/
    // driver info control
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UIView *                _driverView;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UIImageView *              _imgCar;
    
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;
    IBOutlet UIImageView *              _imgCarType;
    IBOutlet UILabel *                  _carBrand;
    
    IBOutlet UIButton *                 _carStyle;
    
    
    IBOutlet UILabel *                  _lblCarColor;
    
    // order info control (wait operate)
    IBOutlet UIView *                   _vwOrderInfo;
    IBOutlet UILabel *                  _lblOrderNum;
    IBOutlet UILabel *                  _lblOrderState;
    
    IBOutlet UILabel *                  _labelStartToEndPos_before;
    
    IBOutlet UILabel *                  _lblOrderPubTime;
    IBOutlet UILabel *                  _lblMidPoints;
    
    IBOutlet UILabel *                  _lblCarNum;
    IBOutlet UILabel *                  _lblElectricTicket;
    IBOutlet UIButton *_btnOpertate1_before;
    
    IBOutlet UIButton *_btnOperate2_before;
    // order info control (after operate)
    IBOutlet UIView *                   _vwOrderInfo1;
    IBOutlet UILabel *                  _lblOrderNum1;
    IBOutlet UILabel *                  _lblOrderState1;
    
    
    IBOutlet UILabel *_labelStartToEndPos_after;
    IBOutlet UILabel *                  _lblEndPos1;
    IBOutlet UILabel *                  _lblOrderPubTime1;
    IBOutlet UILabel *                  _lblOrderStartTime1;
    IBOutlet UILabel *                  _lblMidPoints1;
    
    IBOutlet UIButton *                 _btnOperate1;
    IBOutlet UIButton *                 _btnOperate2;
    
    
    IBOutlet UIScrollView *scrollView;
    
    /*********************** Member Variable ********************/
    NSTimer *                           mCountDownTimer;
    
}
@property (weak, nonatomic) IBOutlet UIButton *btnOperate2before;

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
