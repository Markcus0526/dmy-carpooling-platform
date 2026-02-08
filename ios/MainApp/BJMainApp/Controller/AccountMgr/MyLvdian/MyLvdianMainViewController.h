//
//  MyLvdianMainViewController.h
//  BJMainApp
//
//  Created by Kim Ok Chol on 11/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyLvdianMainViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIButton *btnAccum;
@property (weak, nonatomic) IBOutlet UIButton *btnCharge;
@property (weak, nonatomic) IBOutlet UIButton *btnWithdraw;
@property (weak, nonatomic) IBOutlet UIView *containerAccum;
@property (weak, nonatomic) IBOutlet UIView *containerCharge;
@property (weak, nonatomic) IBOutlet UIView *containerWithdraw;


- (IBAction)onTapMenuAccum:(id)sender;
- (IBAction)onTapMenuCharge:(id)sender;
- (IBAction)onTapMenuWithdraw:(id)sender;


@end
