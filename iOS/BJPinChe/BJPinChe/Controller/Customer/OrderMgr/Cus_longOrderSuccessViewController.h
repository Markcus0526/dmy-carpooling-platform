//
//  Cus_longOrderSuccessViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "STDataInfo.h"
@interface Cus_longOrderSuccessViewController : UIViewController<LongWayOrderSvcDelegate, OrderSvcDelegate>



@property (weak, nonatomic) IBOutlet UILabel *password;
@property (weak, nonatomic) IBOutlet UIImageView *driverImg;
@property (weak, nonatomic) IBOutlet UIImageView *gender;
@property (weak, nonatomic) IBOutlet UILabel *age;
@property (weak, nonatomic) IBOutlet UIImageView *carImg;
@property (weak, nonatomic) IBOutlet UILabel *driverName;
@property (weak, nonatomic) IBOutlet UILabel *brand;
@property (weak, nonatomic) IBOutlet UILabel *carType;
@property (weak, nonatomic) IBOutlet UILabel *carColor;
@property (weak, nonatomic) IBOutlet UILabel *startAddr;


- (IBAction)OnBackViewClick:(id)sender;
- (IBAction)OnGotoMyOrderButtonClick:(id)sender;


@property (weak, nonatomic) IBOutlet UILabel *carno;
@property (weak, nonatomic) IBOutlet UILabel *startTime;
@property (nonatomic,strong) STGrabLongOrderSuccessInfo*	successInfo;


@property (nonatomic, weak) IBOutlet UIButton*				btnCancel;
@property (nonatomic, weak) IBOutlet UILabel*				lblRule01;
@property (nonatomic, weak) IBOutlet UILabel*				lblRule02;
@property (nonatomic, weak) IBOutlet UILabel*				lblRule03;
@property (nonatomic, weak) IBOutlet UILabel*				lblRule04;

@property (nonatomic, weak) IBOutlet UIView*				cancelRuleView;
@property (nonatomic, weak) IBOutlet UIView*				cancelRuleMainView;

@property (nonatomic, weak) IBOutlet UIScrollView*			mainScroll;




@end
