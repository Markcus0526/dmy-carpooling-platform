//
//  Cus_PubShortNotEnoughVC.m
//  BJPinChe
//
//  Created by APP_USER on 14-12-4.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_PubShortNotEnoughVC.h"

@implementation Cus_PubShortNotEnoughVC

- (void)viewDidLoad
{
self.leftPriceLabel.text = self.leftPrice;
self.needPriceLabel.text = self.needPrice;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}





- (IBAction)OnCancelClick:(id)sender {
    [_delegate Cus_OnceOrdernotEnoughViewChargeOrNot:NO];
}

- (IBAction)OnSureClick:(id)sender {
    [_delegate Cus_OnceOrdernotEnoughViewChargeOrNot:YES];
}

@end
