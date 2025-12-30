//
//  Drv_STOrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_MySTOrderDetViewController : SuperViewController <OrderSvcDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UIView *_cusView;
    IBOutlet UILabel *                  _lblName;
    
    IBOutlet UIImageView *_verifiedImgeView;
    
    IBOutlet UILabel *verifiedLabel;
    
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;
    
    IBOutlet UILabel *                  _lblStartToEndPos;
    
    IBOutlet UILabel *                   _orderIDLabel;
    
   
    IBOutlet UILabel *                  _lblPubTime;
    IBOutlet UILabel *                  _lblAcceptTime;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblSysInfoPrice;
    IBOutlet UILabel *                  _lblMidPoint;
    IBOutlet UILabel *                  _lblState;
    
    IBOutlet UIButton *                 _btnOperate1;
    IBOutlet UIButton *                 _btnOperate2;
    
    /********************** Member Variable ***********************/

    __weak IBOutlet UIScrollView *myScrollView;
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;



- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedRefresh:(id)sender;

- (IBAction)onClickedOPButton1:(id)sender;
- (IBAction)onClickedOPButton2:(id)sender;
- (IBAction)onClickedComplain:(id)sender;
- (IBAction)onClickedInsuranceDetail:(id)sender;

@end
