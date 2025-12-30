//
//  Cus_OrderSingleCell.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//
/**
 *  我的订单界面 订单Cell
 */
#import <UIKit/UIKit.h>
#import "OrderCellButton.h"
@interface Cus_MyOrderCell : UITableViewCell <OrderExecuteSvcDelegate>
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
#warning 状态显示不完全  frame定义有问题
    IBOutlet UILabel *                  _lblState;
    IBOutlet OrderCellButton *                 _btnOperate;
    
    STOrderInfo *                       mOrderInfo;
    
    id                                  mParent;
}

- (void) initData : (STOrderInfo *)data parent:(id)parent;


- (IBAction)onClickedPerform:(id)sender;

@end
