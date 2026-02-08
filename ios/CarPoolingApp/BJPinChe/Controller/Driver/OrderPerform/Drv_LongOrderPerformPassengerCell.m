//
//  Drv_LongOrderPerformPassengerCell.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/4/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_LongOrderPerformPassengerCell.h"
#import "UIViewController+CWPopup.h"
#import "Drv_longOrderPreformViewController.h"

@implementation Drv_LongOrderPerformPassengerCell

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


- (void) initData : (STPassengerInfo *)dataInfo parent:(id)parent;
{
    mDataInfo = dataInfo;
    mParent = parent;
    
    // set user image
    //_imgUser
    
    // set sex image & change age color according to sex

    [_lblAge setText:[NSString stringWithFormat:@"%d", mDataInfo.age]];
    [_lblName setText:mDataInfo.name];
    [_lblPassCnt setText:mDataInfo.seat_count_desc];
    [_lblState setText:mDataInfo.state_desc];
    if(mDataInfo.sex == SEX_MALE) {
        [_imgSex setImage:[UIImage imageNamed:@"icon_male.png"]];
        _lblAge.textColor = MYCOLOR_GREEN;
    }
    else {
        [_imgSex setImage:[UIImage imageNamed:@"icon_female.png"]];
        _lblAge.textColor = [UIColor orangeColor];
    }
    
    // check passenger state
    if (mDataInfo.state == PASS_STATE_NOT_CHECKED) {
        [_vwButtonView setHidden:NO];
        [_vwStateView setHidden:YES];
    } else {
        [_vwButtonView setHidden:YES];
        [_vwStateView setHidden:NO];
    }
}

////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Password check delegate
- (void) tappedDone:(NSString *)result
{
    Drv_longOrderPreformViewController * viewCtrl = (Drv_longOrderPreformViewController *)mParent;
    
    if (viewCtrl.popupViewController != nil) {
        [viewCtrl dismissPopupViewControllerAnimated:YES completion:^{
            NSLog(@"popup view dismissed");
            
        }];
    }
    
    [viewCtrl SetPassengerUpload:mDataInfo.uid password:result];
}

- (IBAction)onClickedCheckUser:(id)sender
{
    if (_stateDriver != ORDER_STATE_DRIVER_ARRIVED) {
        [SVProgressHUD showSuccessWithStatus:@"您还没有标记到达" duration:2];
        return;
    }
    UIViewController * viewCtrl = (UIViewController *)mParent;
    
    // show check password modal
    PasswordCheckModal *popup = [[PasswordCheckModal alloc] initWithNibName:@"PasswordCheckModal" bundle:nil];
    
    popup.delegate = self;
    
    [viewCtrl presentPopupViewController:popup animated:YES completion:^(void) {
        NSLog(@"popup view presented");
    }];
}

- (IBAction)onClickedGiveupUser:(id)sender
{
    Drv_longOrderPreformViewController * viewCtrl = (Drv_longOrderPreformViewController *)mParent;
    
    SureViewController * sureViewController = [[SureViewController alloc]init];
    
    sureViewController.delegate = self;
    
    [viewCtrl presentPopupViewController:sureViewController animated:YES completion:^{
        NSLog(@"sureViewController is popup");
    }];
    
    
}

-(void)SureViewResuslt:(BOOL)isYES
{
    Drv_longOrderPreformViewController * viewCtrl = (Drv_longOrderPreformViewController *)mParent;
    if (isYES) {
        [viewCtrl callSignLongOrderPassengerGiveup:mDataInfo.uid];
    }else{
        if (viewCtrl.popupViewController != nil) {
            [viewCtrl dismissPopupViewControllerAnimated:YES completion:^{
                NSLog(@"popup view dismissed");
            }];
        }
    }
}



@end
