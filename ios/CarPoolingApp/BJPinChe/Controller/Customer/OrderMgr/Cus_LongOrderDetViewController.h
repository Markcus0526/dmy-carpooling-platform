//
//  Cus_LongOrderSureViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Cus_EvalDriverViewController.h"
#import "Cus_EvalDriverLatestViewController.h"

@interface Cus_LongOrderDetViewController : UIViewController<LongWayOrderSvcDelegate, OrderSvcDelegate, EvalDrvDelegate>
{
    /********************** UI Controls **********************/
    // driver info control
    IBOutlet UIImageView *              _imgUser;
    __weak IBOutlet UIView *_imageUesrView;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UIImageView *              _imgCar;
    __weak IBOutlet UILabel *_lblCareer;
    
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;
    IBOutlet UIImageView *              _imgCarType;
    IBOutlet UILabel *                  _lblCarBrand;
    IBOutlet UILabel *                  _lblCarSubType;
    IBOutlet UILabel *                  _lblCarColor;
    
    // order info control
    IBOutlet UILabel *                  _lblOrderNum;
    IBOutlet UILabel *                  _lblOrderStateWait;
    IBOutlet UILabel *                  _lblOrderStateNormal;
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblOrderPubTime;
    IBOutlet UILabel *                  _lblOrderAcceptTime;
    
    IBOutlet UILabel *                  _lblCarNum;
    __weak IBOutlet UILabel *_lblCarNumName;
    IBOutlet UILabel *                  _lblElectricTicket;
    __weak IBOutlet UILabel *_lblEletricTicketName;
    
    IBOutlet UIButton *                 _btnOperate1;
    IBOutlet UIButton *                 _btnOperate2;
    IBOutlet UIButton *                 _btnCancelOrder;
    
    IBOutlet UIView *                   _vwCancelOrder;
    IBOutlet UILabel *                  _lblCancelOrder;
    
    STDetailedCusOrderInfo *            mDetailedInfo;
    __weak IBOutlet UIScrollView *OrederScrollView;
}

@property (nonatomic, retain) STOrderInfo *     mOrderInfo;

- (IBAction)OnBackClick:(id)sender;

- (IBAction)onClickedOperate1:(id)sender;
- (IBAction)onClickedOperate2:(id)sender;
- (IBAction)complaintsClick:(id)sender;

- (IBAction)onClickedCancel:(id)sender;
- (IBAction)onClickedInsuranceDetail:(id)sender;

@end
