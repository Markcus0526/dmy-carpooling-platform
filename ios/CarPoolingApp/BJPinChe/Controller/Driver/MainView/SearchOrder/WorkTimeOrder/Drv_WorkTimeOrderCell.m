//
//  Drv_WorkTimeOrderCell.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/27/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_WorkTimeOrderCell.h"
#import "Drv_PassengerInfoViewController.h"
#import "Drv_WTOrderDetViewController.h"

@implementation Drv_WorkTimeOrderCell

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


- (void) initData : (STWorkTimeOrderInfo *)data parent:(id)parent
{
    mOrderInfo = data;
    mParent = parent;
    
	[_imgUser setImageWithURL:[NSURL URLWithUnicodeString:mOrderInfo.image] placeholderImage:[UIImage imageNamed:@"stOrderImage.png"]];
	
    [_lblStartPos setText:mOrderInfo.startPos];
    [_lblEndPos setText:mOrderInfo.endPos];
    
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex
    if(mOrderInfo.sex == SEX_MALE) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = [UIColor orangeColor];
    }
    
    
    [_lblAge setText:[NSString stringWithFormat:@"%d", mOrderInfo.age]];
    
    [_lblName setText:mOrderInfo.name];
    [_lblPrice setText:[NSString stringWithFormat:@"%.01f%@", mOrderInfo.price, DIAN]];
    [_lblSysInfoPrice setText:mOrderInfo.sysinfo_fee_desc];
    [_lblTime setText:mOrderInfo.start_time];
    if (mOrderInfo.midpoints > 0) {
        [_lblMidPoint setText:mOrderInfo.midpoints_desc];
    } else {
        [_lblMidPoint setText:@""];
    }
    
    // show selected days
    NSArray * selDays = [mOrderInfo.days componentsSeparatedByString:@","];
    if ([selDays count] > 0) {
        for (int i = 0; i < selDays.count; i++) {
            int dayNum = [selDays[i] intValue];
            UIButton * button = (UIButton *)[self viewWithTag:(100 + dayNum)];
            [button setEnabled:YES];
        }
    }
}

/**
 * Passenger image click event implementation
 */
- (IBAction)onClickedPassenger:(id)sender
{
    UIViewController * ctrl = (UIViewController *)mParent;
    Drv_PassengerInfoViewController *viewController = (Drv_PassengerInfoViewController *)[ctrl.storyboard instantiateViewControllerWithIdentifier:@"passenger_info"];
    viewController.mPassID = mOrderInfo.pass_id;
    [ctrl presentViewController:viewController animated:YES completion:nil];
}


/**
 * background click event implementation & show order detail
 */
- (IBAction)onClickedBackground:(id)sender
{
    UIViewController * ctrl = (UIViewController *)mParent;
    
    // go to search passenger view controller
    Drv_WTOrderDetViewController *viewController = (Drv_WTOrderDetViewController *)[ctrl.storyboard instantiateViewControllerWithIdentifier:@"wt_order_info"];
    
    viewController.mPassID = mOrderInfo.pass_id;
    viewController.mOrderID = mOrderInfo.uid;
    
    [ctrl presentViewController:viewController animated:YES completion:nil];
}

@end
