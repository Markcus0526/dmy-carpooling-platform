//
//  Cus_PubOrderWaitingViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/1/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客发布单次拼车 等待车主接单界面

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import "MDRadialProgressView.h"
#import "MDRadialProgressTheme.h"
#import "MDRadialProgressLabel.h"
#import "BMapKit.h"
#import "Cus_PubOrderAnnotation.h"

@interface Cus_PubOrderWaitingViewController : SuperViewController <OrderSvcDelegate,AccountSvcDelegate,BMKMapViewDelegate>
{
    /********************** UI Controls **********************/
    IBOutlet UILabel *                  _lblDriverCnt;
    IBOutlet MDRadialProgressView *     _pvCountDown;
    IBOutlet UILabel *                  _lblCountDown;
    
    IBOutlet BMKMapView *               _mapView;
    
    __weak IBOutlet UIImageView *loadingImage;
    /*********************** Member Variable ********************/
    NSTimer *   mCountDownTimer;
	int			mAddPriceTime1;
	int			mAddPriceTime2;
	int			mAddPriceTime3;
	int			mAddPriceTime4;
	int			mAddPriceTime5;
	int 		mSamePriceTime1;
	int 		mSamePriceTime2;
	int 		mSamePriceTime3;
	int 		mSamePriceTime4;
	int 		mSamePriceTime5;
	
	STDriverInfo *			mAcceptedDriver;
	STSingleTimeOrderInfo *	mAcceptedOnceOrder;
	STWorkTimeOrderInfo *	mAcceptedOnOffOrder;
    __weak IBOutlet UILabel *piceLable;
    __weak IBOutlet UILabel *tishilabel1;
//    __weak IBOutlet UILabel *tishilabel2;
    __weak IBOutlet UIView *fabuView;
    __weak IBOutlet UIView *jishiView;
    
    
    //判断是否加价
    NSString *jiajiaStr1;
}

@property (nonatomic, readwrite) long		mOrderId;
@property (nonatomic, readwrite) double 	mPrice;
@property (nonatomic, readwrite) int		mWaitTime;
@property (nonatomic, readwrite) int		mDriverLockTime;
@property (nonatomic, readwrite) int		mRepubTimes;
@property (nonatomic, readwrite) int        price_add;
@property (nonatomic, readwrite) int        price_addrange;
@property (nonatomic, readwrite) int        cut_price_range;
@property (nonatomic, readwrite) int        add_price_min;

@property (nonatomic, retain) STOnceOrderPubData * mOrderPubData;
@property(nonatomic,strong)BMKUserLocation *userLocation;
@property (nonatomic,assign)int  drvCount;
@property (nonatomic , retain) NSString *tishi1;
@property (nonatomic , retain) NSString *tishi2;

- (void)onClickedBack;
- (IBAction)upMapView:(id)sender;
- (IBAction)jiajiaClick:(id)sender;

@end
