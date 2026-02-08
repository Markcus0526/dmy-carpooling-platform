//
//  Cus_STOrderConfirmViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MDRadialProgressView.h"
#import "MDRadialProgressTheme.h"
#import "MDRadialProgressLabel.h"
#import "BMapKit.h"
#import "PasswordCheckModal.h"
#import "UIViewController+CWPopup.h"

@interface Cus_STOrderConfirmViewController : SuperViewController <OrderSvcDelegate, pwdPopupDelegate, UIAlertViewDelegate,BMKMapViewDelegate>
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
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblMidPoints;
    IBOutlet UILabel *                  _lblDistance;
    
    // count down control
    IBOutlet MDRadialProgressView *     _pvCountDown;
    IBOutlet UILabel *                  _lblCountDown;
    
    IBOutlet BMKMapView *               _mapView;
    
    /*********************** Member Variable ********************/
    NSTimer *                           countTimer;
    
}

@property (nonatomic, readwrite)long mOrderId;
@property (nonatomic, retain)NSString *soure;//来源 1正常发单 2通知跳转

@property (nonatomic, retain) STDriverInfo *			mDriverInfo;
@property (nonatomic, retain) STSingleTimeOrderInfo *	mOrderInfo;
@property (atomic, readwrite) int						mWaitTime;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedConfirm:(id)sender;

@end
