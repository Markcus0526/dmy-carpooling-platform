//
//  Drv_MyWTOrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_MyWTOrderDetViewController : SuperViewController <OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;
    
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblPubTime;
    IBOutlet UILabel *                  _lblAcceptTime;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblSysInfoPrice;
    IBOutlet UILabel *                  _lblMidPoint;
    IBOutlet UILabel *                  _lblState;
    
    IBOutlet UIButton *                 _btnOperate1;
    IBOutlet UIButton *                 _btnOperate2;
    
    // stop serve state controls
    IBOutlet UIView *                   _vwStopServeInfo;
    IBOutlet UILabel *                  _lblStopServeInfo1;
    IBOutlet UILabel *                  _lblStopServeInfo2;
    IBOutlet UILabel *                  _lblStopServeInfo3;
    
    /********************** Member Variable ***********************/
    
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedRefresh:(id)sender;

- (IBAction)onClickedOPButton1:(id)sender;
- (IBAction)onClickedOPButton2:(id)sender;
- (IBAction)onClickedStopServe:(id)sender;
- (IBAction)onClickedComplain:(id)sender;

@end
