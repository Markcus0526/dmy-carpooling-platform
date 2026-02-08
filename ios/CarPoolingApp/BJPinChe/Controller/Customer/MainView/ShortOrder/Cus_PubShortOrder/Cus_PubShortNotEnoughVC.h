//
//  Cus_PubShortNotEnoughVC.h
//  BJPinChe
//
//  Created by APP_USER on 14-12-4.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>



@protocol Cus_PubShortNotEnoughDelegate <NSObject>

-(void)Cus_OnceOrdernotEnoughViewChargeOrNot:(BOOL)chargeOrNot;

@end

@interface Cus_PubShortNotEnoughVC : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *leftPriceLabel;
@property (weak, nonatomic) IBOutlet UILabel *needPriceLabel;

@property (strong, nonatomic) NSString *leftPrice;
@property (strong, nonatomic) NSString *needPrice;

- (IBAction)OnCancelClick:(id)sender;
- (IBAction)OnSureClick:(id)sender;

@property(strong, nonatomic) id<Cus_PubShortNotEnoughDelegate> delegate;

@end
