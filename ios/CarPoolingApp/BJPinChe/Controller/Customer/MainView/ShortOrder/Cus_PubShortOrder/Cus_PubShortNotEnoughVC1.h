//
//  Cus_PubShortNotEnoughVC.h
//  BJPinChe
//
//  Created by APP_USER on 14-12-4.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>



@protocol Cus_PubShortDelegate <NSObject>

-(void)Cus_OnceOrdernotEnoughViewCharge:(BOOL)chargeOrNot;

@end

@interface Cus_PubShortNotEnoughVC1 : UIViewController

@property (weak, nonatomic) IBOutlet UILabel *leftPriceLabel;
@property (weak, nonatomic) IBOutlet UILabel *needPriceLabel;

@property (strong, nonatomic) NSString *leftPrice;
@property (strong, nonatomic) NSString *needPrice;

- (IBAction)OnCancelClick:(id)sender;
- (IBAction)OnSureClick:(id)sender;

@property(strong, nonatomic) id<Cus_PubShortDelegate> delegate;

@end
