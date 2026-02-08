//
//  Drv_WorkTimeOrderCell.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/27/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//近日按敬爱放几哦啊积分

#import <UIKit/UIKit.h>

@interface Drv_WorkTimeOrderCell : UITableViewCell
{
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblSysInfoPrice;
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblMidPoint;
    
    STWorkTimeOrderInfo *               mOrderInfo;
    
    id                                  mParent;
}

- (void) initData : (STWorkTimeOrderInfo *)data parent:(id)parent;

- (IBAction)onClickedPassenger:(id)sender;
- (IBAction)onClickedBackground:(id)sender;

@end
