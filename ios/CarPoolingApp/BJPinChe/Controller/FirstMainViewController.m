//
//  FirstMainViewController.m
//  BJPinChe
//
//  Created by KimOC on 8/11/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//APP 第一界面  用户选择

#import "FirstMainViewController.h"
#import "Config.h"

@interface FirstMainViewController ()

@end

@implementation FirstMainViewController

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

- (void) gotoCustomerView
{
    // getting the customer storyboard
    UIStoryboard * customerStroyboard = [UIStoryboard storyboardWithName:@"CustomerStoryboard" bundle:nil];
    
    // initializing view controller
    UIViewController * controller = [customerStroyboard instantiateInitialViewController];
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

- (void) gotoDriverView
{
    // getting the driver storyboard
    UIStoryboard * driverStroyboard = [UIStoryboard storyboardWithName:@"DriverStoryboard" bundle:nil];
    
    // initializing view controller
    UIViewController * controller = [driverStroyboard instantiateInitialViewController];
//    [self presentModalViewController:controller animated:YES];
    [self presentViewController:controller animated:YES completion:nil];
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - UI Relation

- (IBAction)onClickedCustomer:(id)sender {
    
    // save selected option  设定登录属性
    [Config setLoginOption:LOGIN_OPTION_CUSTOMER];
    
    [self gotoCustomerView];
}

- (IBAction)onClickedDriver:(id)sender {
    
    // save selected option  设定登录属性
    [Config setLoginOption:LOGIN_OPTION_DRIVER];
 
    [self gotoDriverView];
}

@end
