//
//  Cus_driverInfoViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OrderSvcMgr.h"
@interface Cus_driverInfoViewController : UIViewController<OrderSvcDelegate,UITableViewDataSource,UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableview;
@property (weak, nonatomic) IBOutlet UIImageView *carImg;
@property (weak, nonatomic) IBOutlet UIImageView *driverImg;
@property (weak, nonatomic) IBOutlet UIImageView *gender;
@property (weak, nonatomic) IBOutlet UILabel *age;
@property (weak, nonatomic) IBOutlet UILabel *career;
@property (weak, nonatomic) IBOutlet UILabel *evGoodRate;
@property (weak, nonatomic) IBOutlet UILabel *carPoolTimes;
@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UIImageView *carStyle;
@property (weak, nonatomic) IBOutlet UILabel *brand;
@property (weak, nonatomic) IBOutlet UILabel *type;
@property (weak, nonatomic) IBOutlet UILabel *color;
- (IBAction)OnBackClick:(id)sender;
@property (nonatomic) long driverid;
@end
