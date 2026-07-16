//
//  ChargeViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_ChargeViewController.h"

@interface Drv_ChargeViewController ()

@end

@implementation Drv_ChargeViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Drvtom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self initControls];
    [self callGetMoney];
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




/////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Initilaize

- (void) initControls
{
    UITapGestureRecognizer * tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapBackGround:)];
    
    [self.view addGestureRecognizer:tapRecognizer];
}

/**
 * Hide keyboard when tapped background
 */
- (void) handleTapBackGround:(id)sender
{
    [self.view endEditing:YES];
}

- (void) updateUI
{
    [_lblLeftAccum setText:[NSString stringWithFormat:@"%.02f", mLeftMoney]];
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get the current money
 */
- (void) callGetMoney
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetMoney:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void) getMoneyResult:(NSString *)result money:(double)money
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        mLeftMoney = money;
        
        // refresh table
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

/**
 * call charge money service
 * @param : money [in], charge value
 */
- (void) callCharge : (double)money
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] Charge:[Common getUserID] balance:money devtoken:[Common getDeviceMacAddress]];
}

- (void) chargeResult:(NSString *)result money:(double)money
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];
        
        mLeftMoney = money;
        
        // refresh table
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

/**
 * Submit charge button clicked event implemenation
 */
- (IBAction)onClickedNext:(id)sender
{
    double money = 0;
    
    if ([[_txtChargeVal text] isEqualToString:@""] == YES) {
        [_txtChargeVal becomeFirstResponder];
        return;
    }
    
    money = [[_txtChargeVal text] doubleValue];
    [self callCharge:money];
}

/**
 * Chaxun menu button clicked event implementation
 */
- (IBAction)onClickedMenuAccum:(id)sender
{
    UIViewController * presentViewCtrl = self.presentingViewController;
    [self dismissViewControllerAnimated:NO completion:^
     {
         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"AccumHistory"];
         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
     }];
    
}

/**
 * Tixian menu button clicked event implementation
 */
- (IBAction)onClickedMenuWithdraw:(id)sender
{
    UIViewController * presentViewCtrl = self.presentingViewController;
    [self dismissViewControllerAnimated:NO completion:^
     {
         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Withdraw"];
         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
     }];
}

@end
