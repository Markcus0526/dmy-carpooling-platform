//
//  WithdrawViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//  提现界面
#import "Cus_AccumViewController.h"
#import "Cus_WithdrawViewController.h"
#import "Cus_ChargeViewController.h"
#import "Cus_BindAccountViewController.h"
#import "Config.h"
#import "ForgetPWDVC.h"

@interface Cus_WithdrawViewController ()

@end

@implementation Cus_WithdrawViewController

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


	self.automaticallyAdjustsScrollViewInsets = NO;

	[self initControls];
}




- (void)viewDidAppear:(BOOL)animated
{
	[super viewDidAppear:animated];
	[self callGetAccount];
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
	_txtPassword.delegate = self;
	_txtWithdrawVal.delegate = self;

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
    [_lblAccount setText:mBindAccount.bankcard];
    [_lblRealName setText:mBindAccount.realname];
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call get the current bind account
 */
- (void) callGetAccount
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] GetAccount:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void) getAccountResult:(NSString *)result account:(STBindAccount *)account
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
        mBindAccount = account;
        
        [self updateUI];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

/**
 * withdraw service
 */
- (void) callWithdraw
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] Withdraw:[Common getUserID] realname:[_lblRealName text] accountname:[_lblAccount text] balance:[[_txtWithdrawVal text] doubleValue] password:[_txtPassword text] devtoken:[Common getDeviceMacAddress]];
}

- (void) withdrawResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismiss];
        
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
   // BACK_VIEW;
}

- (IBAction)onChangeBindAccount:(id)sender
{
   // Cus_BindAccountViewController * ctrl = (Cus_BindAccountViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"bindaccount"];
    
    Cus_BindAccountViewController *bindAccount =[[Cus_BindAccountViewController alloc]initWithNibName:@"BindAccount" bundle:nil];
    bindAccount.mOrgAccount = mBindAccount.bankcard;
    
    [self.tabBarController presentViewController:bindAccount animated:NO completion:nil];
}

- (IBAction)onConfirmWithdraw:(id)sender
{
    if ([[_lblAccount text] isEqualToString:@""] == YES || [[_lblRealName text] isEqualToString:@""] == YES) {
        [self.view makeToast:MSG_NO_BIND_BANK duration:DEF_DELAY position:@"center"];
        return;
    }
    if ([[_txtWithdrawVal text] isEqualToString:@""]) {
        [_txtWithdrawVal becomeFirstResponder];
        return;
    }
    if ([[_txtPassword text] isEqualToString:@""]) {
        [_txtWithdrawVal becomeFirstResponder];
        return;
    }
    
    [self callWithdraw];
}

#pragma mark - Bottm menu button clicked event implementation

/**
 * Chaxun menu button clicked event implementation
 */
- (IBAction)onClickedMenuAccum:(id)sender
{
//    UIViewController * presentViewCtrl = self.presentingViewController;
//    [self dismissViewControllerAnimated:NO completion:^
//     {
//         UIViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"AccumHistory"];
//         [presentViewCtrl presentViewController:ctrl animated:NO completion:nil];
//     }];
     Cus_AccumViewController * presentViewCtrl = self.presentingViewController;
    
    [self dismissViewControllerAnimated:YES completion:^{
    
     //presentViewCtrl.RefreshOrNot =YES;
    
    }];
    
}

/**
 * Chongzhi menu button clicked event implementation
 */
- (IBAction)onClickedMenuCharge:(id)sender
{
    Cus_AccumViewController * presentViewCtrl = self.presentingViewController;
   // presentViewCtrl.RefreshOrNot =NO;

    [self dismissViewControllerAnimated:NO completion:^
     {
        // Cus_ChargeViewController * ctrl = [self.storyboard instantiateViewControllerWithIdentifier:@"Charge"];
         Cus_ChargeViewController *chargeVC =[[Cus_ChargeViewController alloc]initWithNibName:@"ChargeView" bundle:nil];
         [presentViewCtrl presentViewController:chargeVC animated:YES completion:nil];
     }];
}

#pragma mark  忘记密码按钮点击事件
- (IBAction)onClickedForgetPWD:(id)sender {
    
    ForgetPWDVC *forgetPWD = [[ForgetPWDVC alloc]initWithNibName:@"ForgetPWDView" bundle:nil];
    [self.tabBarController presentViewController:forgetPWD animated:YES completion:nil];
}




- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
	NSUInteger newLength = textField.text.length + string.length - range.length;

	int nMaxLength = 0;
	if (textField == _txtWithdrawVal)
	{
		nMaxLength = 5;
	}

	return newLength <= nMaxLength;
}



@end
