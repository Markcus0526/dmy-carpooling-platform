//
//  Drv_OrderSingleCell.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OrderCellButton.h"


@interface Drv_OrderSingleCell : UITableViewCell <OrderExecuteSvcDelegate>
{
    IBOutlet UILabel *                  _lblStartPos;
    IBOutlet UILabel *                  _lblEndPos;
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblPrice;
    IBOutlet UILabel *                  _lblType;
    IBOutlet UILabel *                  _lblTime;
    IBOutlet UILabel *                  _lblContent;
    IBOutlet UILabel *                  _lblState;
    IBOutlet OrderCellButton *                 _btnOperate;
   // IBOutlet UIButton *    DetailBtn;
    
    STOrderInfo *                       mOrderInfo;
    
    id                                  mParent;
}

- (void) initData : (STOrderInfo *)data parent:(id)parent;

- (IBAction)onClickedPerform:(id)sender;
- (IBAction)onClickedDetailBtn:(id)sender;

@end
