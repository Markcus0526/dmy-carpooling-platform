//
//  InsuranceDetailModal.h
//  BJPinChe
//
//  Created by APP_USER on 14-11-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
// service protocol
@protocol insuranceDetailDelegate <NSObject>

@optional

- (void) tappedDone;

@end


@interface InsuranceDetailModal : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *appl_num_Label;
@property (weak, nonatomic) IBOutlet UILabel *isd_name_Label;
@property (weak, nonatomic) IBOutlet UILabel *effect_time_Label;
@property (weak, nonatomic) IBOutlet UILabel *insexpr_date_Label;
@property (weak, nonatomic) IBOutlet UILabel *total_amount_Label;

@property(nonatomic,strong) STDetailedDrvOrderInfo * mOrderInfo;
@property(weak, nonatomic) id<insuranceDetailDelegate> delegate;
- (IBAction)onClickedDone:(id)sender;

@end
