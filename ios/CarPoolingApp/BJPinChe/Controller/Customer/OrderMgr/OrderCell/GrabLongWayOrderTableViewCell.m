//
//  GrabLongWayOrderTableViewCell.m
//  BJPinChe
//
//  Created by CKK on 14-8-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "GrabLongWayOrderTableViewCell.h"
#import "UIImageView+WebCache.h"
#import "UIViewController+CWPopup.h"
#import "Cus_grabLongWayOrderViewController.h"

@implementation GrabLongWayOrderTableViewCell

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)layoutSubviews
{
    
//    self.combox;
}

-(void)reloadData
{
    [self.driverImage setImageWithURL:[NSURL URLWithString:self.model.driver_img] placeholderImage:[UIImage imageNamed:@""]];

	if (self.model.driver_gender == 1) {
		[self.driverGender setImage:[UIImage imageNamed:@"icon_female.png"]];
		self.driverAge.textColor = [UIColor colorWithRed:0.94f green:0.68f blue:0.27f alpha:1.00f];
    } else if(self.model.driver_gender == 0) {
        [self.driverGender setImage:[UIImage imageNamed:@"icon_male.png"]];
        self.driverAge.textColor = [UIColor colorWithRed:0.30f green:0.71f blue:0.49f alpha:1.00f];
    }

	self.driverAge.text = [NSString stringWithFormat:@"%d",self.model.driver_age];
    self.driverName.text = self.model.driver_name;

	if (self.model.leftseats == 1) {
        self.leftseats.textColor = [UIColor colorWithRed:0.98f green:0.25f blue:0.26f alpha:1.00f];
		self.leftseats.text = [NSString stringWithFormat:@"仅余%d座",self.model.leftseats];
	} else {
		self.leftseats.text = [NSString stringWithFormat:@"剩余%d座",self.model.leftseats];
	}

	self.price.text = [NSString stringWithFormat:@"%.0f点/座",self.model.price];
    self.startAddr.text = self.model.start_city;
    self.endAddr.text = self.model.end_city;

	self.startTime.text = [self dateFormatter:self.model.start_time];
}


- (IBAction)onButtonClick:(id)sender {
    NSLog(@"button is onClick");

    
    Cus_grabLongWayOrderViewController * viewCtrl = (Cus_grabLongWayOrderViewController *)self.parentView;
    
    int seatsCount =  [[self.selectNumberLabel.text componentsSeparatedByString:@" "][0] intValue];
    [viewCtrl onAcceptClickWithWithLongOrderInfo:self.model AndSeatsCount:seatsCount];
}

- (IBAction)OnSelectNumberClick:(id)sender {
   
    Cus_grabLongWayOrderViewController * viewCtrl = (Cus_grabLongWayOrderViewController *)self.parentView;
    [viewCtrl OnSelectNumberClickWithNumber:self.model.leftseats WithLabel:self.selectNumberLabel];
    
    
}


-(NSString *)dateFormatter:(NSString *)dateString
{
    NSString * year = [dateString componentsSeparatedByString:@"-"][0];
    NSString * month = [dateString componentsSeparatedByString:@"-"][1];
    NSString * day = [([dateString componentsSeparatedByString:@"-"][2]) componentsSeparatedByString:@" "][0];
    NSString * hour = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][0];
    NSString * minute = [([dateString componentsSeparatedByString:@" "][1]) componentsSeparatedByString:@":"][1];
    
    return [NSString stringWithFormat:@"%@年%@月%@日 %@:%@出发",year,month,day,hour,minute];
}







@end
