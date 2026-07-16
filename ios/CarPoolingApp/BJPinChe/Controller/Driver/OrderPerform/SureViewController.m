//
//  SureViewController.m
//  BJPinChe
//
//  Created by CKK on 14-9-17.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "SureViewController.h"

@interface SureViewController ()

@end

@implementation SureViewController

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
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)OnSureClick:(id)sender {
    [self.delegate SureViewResuslt:YES];
}

- (IBAction)OnCancelClick:(id)sender {
    [self.delegate SureViewResuslt:NO];
}
@end
