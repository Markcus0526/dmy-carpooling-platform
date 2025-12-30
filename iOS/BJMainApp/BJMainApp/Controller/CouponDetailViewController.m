//
//  CouponDetailViewController.m
//  BJMainApp
//
//  Created by KimOC on 8/10/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "CouponDetailViewController.h"

@interface CouponDetailViewController ()

@end

@implementation CouponDetailViewController

@synthesize mCode;
@synthesize mContents;
@synthesize mUnitname;

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
    
    [_lblCode setText:mCode];
    [_lblUnitname setText:mUnitname];
    [_lblContents setText:mContents];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
