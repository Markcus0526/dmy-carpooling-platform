//
//  Cus_NotEnoughViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol Cus_NotEnoughViewDelegate <NSObject>

-(void)Cus_notEnoughViewChargeOrNot:(BOOL)chargeOrNot;

@end

@interface Cus_NotEnoughViewController : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *leftPriceLabel;
@property (weak, nonatomic) IBOutlet UILabel *needPriceLabel;

@property (strong, nonatomic) NSString *leftPrice;
@property (strong, nonatomic) NSString *needPrice;

- (IBAction)OnCancelClick:(id)sender;
- (IBAction)OnSureClick:(id)sender;

@property(strong, nonatomic) id<Cus_NotEnoughViewDelegate> delegate;

@end
