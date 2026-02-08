//
//  Drv_SingleTimeOrderCell.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_SingleTimeOrderCell.h"
#import "Drv_PassengerInfoViewController.h"
#import "Drv_STOrderDetViewController.h"
#import "Drv_SuccessOrderViewController.h"
#import "Drv_OrderMgrViewController.h"
#import "Drv_SearchOrderViewController.h"

@implementation Drv_SingleTimeOrderCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)awakeFromNib
{
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
- (IBAction)detaileBtn:(id)sender {
    Drv_SearchOrderViewController * ctrl = (Drv_SearchOrderViewController *)mParent;
    
    STOrderInfo *info =[[STOrderInfo alloc]init];
    info.insun_fee = mOrderInfo.insun_fee;
    info.sysinfo_fee = mOrderInfo.sysinfo_fee;
    
    [ctrl onClickedPerform:info];
    
}

- (void) initData : (STSingleTimeOrderInfo *)data parent:(id)parent
{
    
    mOrderInfo = data;
    mParent = parent;

    [_imgUser setImageWithURL:[NSURL URLWithUnicodeString:mOrderInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
	
	
    [_lblStartPos setText:mOrderInfo.startPos];
    [_lblEndPos setText:mOrderInfo.endPos];
    
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    //_imgSex
    //if ([mOrderInfo.sex isEqualToString:SEX_MALE]) {
    if (mOrderInfo.sex == SEX_MALE) {
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    [_lblAge setText:[NSString stringWithFormat:@"%d", mOrderInfo.age]];
    
    [_lblName setText:mOrderInfo.name];
    [_lblPrice setText:[NSString stringWithFormat:@"%0.2f", mOrderInfo.price]];
//    [_lblSysInfoPrice setText:mOrderInfo.sysinfo_fee_desc];
    [_lblTime setText:mOrderInfo.start_time];
//    if (mOrderInfo.midpoints > 0) {
//        [_lblMidPoint setText:mOrderInfo.midpoints_desc];
//    } else {
//        [_lblMidPoint setText:@""];
//    }

	[_lblDistance setText:mOrderInfo.distance_desc];
    
    [_lblDIs setText:[NSString stringWithFormat:@"总距离%0.2fKM",mOrderInfo.mileage]];
    if(mOrderInfo.sex == 0) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    
    if ([mOrderInfo.status isEqualToString:@"1"] || [mOrderInfo.status isEqualToString:@"2"]) {
//        [_cellBack setHidden:NO];
        _publishBtn.enabled = YES;
        [_publishBtn setBackgroundImage:[UIImage imageNamed:@"btnPublish.png"] forState:UIControlStateNormal];
        [_cellBack setImage:[UIImage imageNamed:@"ordercell_back.png"]];
    }else
    {
        //少一张灰色背景图
        [_cellBack setImage:[UIImage imageNamed:@"ordercellH_back.png"]];
//        [_cellBack setHidden:YES];
        _publishBtn.enabled = NO;
        [_publishBtn setBackgroundImage:[UIImage imageNamed:@"bk_gradorder.png"] forState:UIControlStateNormal];
    }
}

/**
 * Publish button click event implementation
 */
- (IBAction)onClickedPublish:(id)sender
{

    [_delegate drv_singleCell:_indexPathCell];
    
    //UIViewController * ctrl = (UIViewController *)mParent;
    //Drv_SuccessOrderViewController *viewController = (Drv_SuccessOrderViewController *)[ctrl.storyboard instantiateViewControllerWithIdentifier:@"success_order"];
    //SHOW_VIEW_IN_CELL(ctrl, viewController);
}

/**
 * Passenger image click event implementation
 */
- (IBAction)onClickedPassenger:(id)sender
{
    UIViewController * ctrl = (UIViewController *)mParent;
    
    // go to search passenger view controller
    Drv_PassengerInfoViewController *viewController = (Drv_PassengerInfoViewController *)[ctrl.storyboard instantiateViewControllerWithIdentifier:@"passenger_info"];
    if ([mOrderInfo.status isEqualToString:@"2"] || [mOrderInfo.status isEqualToString:@"3"])
    {
        viewController.startTimer = mOrderInfo.start_time;
    }else
    {
        viewController.mPassID = mOrderInfo.pass_id;
    }
    
    
    [ctrl presentViewController:viewController animated:YES completion:nil];
}

/**
 * background click event implementation & show order detail
 */
- (IBAction)onClickedBackground:(id)sender
{
    UIViewController * ctrl = (UIViewController *)mParent;
    
    // go to search passenger view controller
   // Drv_STOrderDetViewController *viewController = (Drv_STOrderDetViewController *)[ctrl.storyboard instantiateViewControllerWithIdentifier:@"st_order_info"];
    Drv_STOrderDetViewController *viewController =[[Drv_STOrderDetViewController alloc]initWithNibName:@"Drv_SearchOnceOrderDetView" bundle:nil];
    viewController.mPassID = mOrderInfo.pass_id;
    viewController.mOrderID = mOrderInfo.uid;
    viewController.status = mOrderInfo.status;
    viewController.startTimer = mOrderInfo.start_time;
    
    [ctrl presentViewController:viewController animated:YES completion:nil];
}



@end







