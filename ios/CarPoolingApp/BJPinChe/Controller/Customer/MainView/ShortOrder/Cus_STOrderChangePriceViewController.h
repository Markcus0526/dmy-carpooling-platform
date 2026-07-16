//
//  Cus_STOrderChangePriceViewController.h
//  BJPinChe
//
//  Created by YunSI on 9/10/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  乘客改价 加价页面

#import <UIKit/UIKit.h>
@class Cus_PubOrderWaitingViewController;
@interface Cus_STOrderChangePriceViewController : SuperViewController <OrderSvcDelegate>
{
	/****************************** UI Controls ********************************/
	IBOutlet UILabel *				_lblSystemPrice;
	IBOutlet UITextField *			_txtPrice;
	
    __weak IBOutlet UIScrollView *myScrollView;
    __weak IBOutlet UILabel *driverCountLabel;
    __weak IBOutlet UILabel *time_leftLabel; //
    __weak IBOutlet UILabel *average_timeLabel;//平台时间
}
- (IBAction)callClick:(id)sender;
@property (nonatomic, readwrite) long		orderId;
@property (nonatomic, assign) int		mOrderType;
@property (nonatomic, assign) double 	mPrice;
@property (nonatomic, assign) int		mRepubTimes;
@property (nonatomic, assign) int       driverCount;
@property (nonatomic, strong) STOnceOrderPubData * mOrderPubData;
@property (nonatomic,strong) Cus_PubOrderWaitingViewController *pubWaitingVC;
- (IBAction)btnClick:(id)sender;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedMinus:(id)sender;
- (IBAction)onClickedPlus:(id)sender;
- (IBAction)onClickedRepub:(id)sender;

@end
