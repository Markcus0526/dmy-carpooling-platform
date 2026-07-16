//
//  Drv_SearchOrderDetViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  车主抢市内订单  单次详情界面

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import "BMapKit.h"

@interface Drv_STOrderDetViewController : SuperViewController <BMKMapViewDelegate, OrderSvcDelegate>
{
    __weak IBOutlet UIScrollView *myScrollView;
    /********************** UI Controls **********************/
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    
    IBOutlet UIImageView *_verifiedImgeView;
    
    IBOutlet UILabel *verifiedLabel;
    
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblServeCnt;

    IBOutlet UILabel *_lblStartToEnd;
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblSysInfoPrice;
    IBOutlet UILabel *                  _lblMidPoint;
    
    IBOutlet BMKMapView *               _mapView;
    
    /********************** Member Variable ***********************/
	NSTimer * 			mWaitAgreeTimer;
	int					mWaitCount;
    
     NSString * tishiString;
    
    NSDictionary *falseOrderDic ;
    
}

@property (nonatomic, readwrite) long           mPassID;
@property (nonatomic, readwrite) long           mOrderID;
@property (nonatomic, retain)    NSString      *status;
@property (nonatomic, retain)    NSString      *startTimer;

@property (weak, nonatomic) IBOutlet UIButton *publishBtn;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedPublish:(id)sender;
- (IBAction)onClickedUserImg:(id)sender;

@end
