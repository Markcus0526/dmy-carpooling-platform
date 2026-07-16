//
//  Drv_LongOrderPerformPassengerCell.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/4/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PasswordCheckModal.h"
#import "SureViewController.h"
@interface Drv_LongOrderPerformPassengerCell : UITableViewCell <pwdPopupDelegate,SureViewControllerDelegate>
{
    IBOutlet UIImageView *              _imgUser;
    IBOutlet UIImageView *              _imgSex;
    IBOutlet UILabel *                  _lblAge;
    IBOutlet UILabel *                  _lblName;
    IBOutlet UILabel *                  _lblPassCnt;
    
    IBOutlet UIView *                   _vwButtonView;
    IBOutlet UIView *                   _vwStateView;
    IBOutlet UILabel *                  _lblState;
    
    STPassengerInfo *                   mDataInfo;
    id                                  mParent;

}
@property (assign,nonatomic) int stateDriver;
- (void) initData : (STPassengerInfo *)dataInfo parent:(id)parent;

- (IBAction)onClickedCheckUser:(id)sender;
- (IBAction)onClickedGiveupUser:(id)sender;

@end
