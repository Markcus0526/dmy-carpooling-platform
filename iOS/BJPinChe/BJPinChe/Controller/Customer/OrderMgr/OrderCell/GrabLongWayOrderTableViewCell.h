//
//  GrabLongWayOrderTableViewCell.h
//  BJPinChe
//
//  Created by CKK on 14-8-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "STDataInfo.h"
@interface GrabLongWayOrderTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *driverImage;
@property (weak, nonatomic) IBOutlet UIImageView *driverGender;
@property (weak, nonatomic) IBOutlet UILabel *driverAge;
@property (weak, nonatomic) IBOutlet UILabel *driverName;
@property (weak, nonatomic) IBOutlet UILabel *leftseats;
@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet UILabel *startAddr;
@property (weak, nonatomic) IBOutlet UILabel *endAddr;
@property (weak, nonatomic) IBOutlet UILabel *startTime;
@property (nonatomic,strong) STAcceptableLongOrderInfo * model;
@property (nonatomic,assign) UIViewController * parentView;
- (IBAction)onButtonClick:(id)sender;
- (IBAction)OnSelectNumberClick:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *selectNumberLabel;
-(void)reloadData;
@end
