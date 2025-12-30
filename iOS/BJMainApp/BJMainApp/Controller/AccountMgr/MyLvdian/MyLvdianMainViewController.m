//
//  MyLvdianMainViewController.m
//  BJMainApp
//
//  Created by Kim Ok Chol on 11/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "MyLvdianMainViewController.h"
#import "ChargeViewController.h"
#import "WithdrawViewController.h"

@interface MyLvdianMainViewController ()

@end

@implementation MyLvdianMainViewController

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
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/


- (IBAction)onTapMenuAccum:(id)sender
{
    // change button state
    [_btnAccum setSelected:YES];
    [_btnCharge setSelected:NO];
    [_btnWithdraw setSelected:NO];
    
    // change view state
    [_containerAccum setHidden:NO];
    [_containerCharge setHidden:YES];
    [_containerWithdraw setHidden:YES];
}

- (IBAction)onTapMenuCharge:(id)sender
{
    [_btnAccum setSelected:NO];
    [_btnCharge setSelected:YES];
    [_btnWithdraw setSelected:NO];
    
    // change view state
    [_containerAccum setHidden:YES];
    [_containerCharge setHidden:NO];
    [_containerWithdraw setHidden:YES];
    
    ChargeViewController * child = (ChargeViewController *)[self.childViewControllers objectAtIndex:1];
    [child updateData];
}

- (IBAction)onTapMenuWithdraw:(id)sender
{
    [_btnAccum setSelected:NO];
    [_btnCharge setSelected:NO];
    [_btnWithdraw setSelected:YES];

	// change view state
    [_containerAccum setHidden:YES];
    [_containerCharge setHidden:YES];
    [_containerWithdraw setHidden:NO];

	WithdrawViewController * child = (WithdrawViewController *)[self.childViewControllers objectAtIndex:2];
    [child updateData];
}

@end
