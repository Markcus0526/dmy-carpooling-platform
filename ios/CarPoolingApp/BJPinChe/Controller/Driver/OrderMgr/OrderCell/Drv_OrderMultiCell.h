//
//  Drv_OrderMultiCell.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_OrderMultiCell : UITableViewCell
{
	IBOutlet UILabel *                  _lblStartPos;
	IBOutlet UILabel *                  _lblEndPos;
	IBOutlet UILabel *                  _lblCustomerNum;

	IBOutlet UILabel *                  _lblPrice;
	IBOutlet UILabel *                  _lblType;
	IBOutlet UILabel *                  _lblTime;
	IBOutlet UILabel *                  _lblContent;
	IBOutlet UILabel *                  _lblState;

	IBOutlet UIImageView*				_imgUserPhoto;

	STOrderInfo *                       mOrderInfo;
	id                                  mParent;
	__weak IBOutlet UIButton *_btnOperate;
}


- (void) initData:(STOrderInfo *)data parent:(id)parent;

- (IBAction)onClickedPerformLongOrder:(id)sender;
- (IBAction)onClickedDetailBtn:(id)sender;

@end
