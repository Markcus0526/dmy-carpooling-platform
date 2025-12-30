//
//  Cus_NotEnoughViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Cus_NotEnoughViewController.h"

@interface Cus_NotEnoughViewController ()

@end

@implementation Cus_NotEnoughViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.leftPriceLabel.text = self.leftPrice;
    self.needPriceLabel.text = self.needPrice;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}





- (IBAction)OnCancelClick:(id)sender {
    [_delegate Cus_notEnoughViewChargeOrNot:NO];
}

- (IBAction)OnSureClick:(id)sender {
    [_delegate Cus_notEnoughViewChargeOrNot:YES];
}
@end
