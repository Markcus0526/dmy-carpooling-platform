//
//  InsuranceModal.h
//  BJPinChe
//
//  Created by APP_USER on 14-11-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

// service protocol
@protocol insuranceFeeDelegate <NSObject>

@optional

- (void) tappedDone;

@end

@interface InsuranceModal : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *systemFeeLabel;
@property (weak, nonatomic) IBOutlet UILabel *insuranceFeeLabel;

@property(strong,nonatomic)STOrderInfo *morderInfo;
@property(weak, nonatomic) id<insuranceFeeDelegate> delegate;

- (IBAction)onClickedDone:(id)sender;
@end
