//
//  Drv_LongOrderDetPassCell.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/5/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_LongOrderDetPassCell : UITableViewCell
{
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblEvalPro;
    IBOutlet UILabel *                  _lblCarpoolCnt;
    IBOutlet UILabel *                  _lblSeatCnt;
    IBOutlet UIButton *                 _markVerif;
    IBOutlet UILabel *                  _lblDetail;
    
    IBOutlet UIButton *                 _btnOperate;
    
    STOrderInfo *                       mOrderInfo;
    STPassengerInfo *                   mDataInfo;
    id                                  mParent;
    
    __weak IBOutlet UIView *headerView;
}

- (void) initData : (STPassengerInfo *)dataInfo orderInfo:(STOrderInfo *)orderInfo parent:(id)parent;

- (IBAction)onClickedBtnOperate:(id)sender;

@end
