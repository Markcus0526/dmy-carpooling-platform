//
//  WithdrawViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "WithdrawViewController.h"
#import "BindAccountViewController.h"
#import "Config.h"

@interface WithdrawViewController ()

@end

@implementation WithdrawViewController

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

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [self initControls];
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

- (void) updateData
{
    [self callGetAccount];
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
    BACK_VIEW;
}

- (IBAction)onChangeBindAccount:(id)sender
{
    BindAccountViewController * ctrl = (BindAccountViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"bindaccount"];
    
    ctrl.mOrgAccount = mBindAccount.bankcard;
    
    [self presentViewController:ctrl animated:NO completion:nil];
}

- (IBAction)onConfirmWithdraw:(id)sender
{
    if ([[_lblAccount text] isEqualToString:@""] == YES || [[_lblRealName text] isEqualToString:@""] == YES) {
		SHOW_MSG(MSG_NO_BIND_BANK);
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
    
    // check password
    if ([[_txtPassword text] isEqualToString:[Config loginPassword]] == NO) {
		SHOW_MSG(MSG_BIND_INCORRECT_PWD);
        return;
    }
    
    [self callWithdraw];
}

- (IBAction)onTapForgetPwd:(id)sender
{
    UIViewController * viewCtrl = (UIViewController *)[self.storyboard instantiateViewControllerWithIdentifier:@"forgetPWD"];
    
    [self.navigationController pushViewController:viewCtrl animated:YES];
}


///////////////////////////////////////////////////////////////////
#pragma mark - Scroll When Keyboard Focus

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    curTextField = textField;
    if (keyboardVisible)
        [KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    curTextField = nil;
    [textField resignFirstResponder];
}

- (void)textViewDidBeginEditing:(UITextView *)textView
{
    curTextField = textView;
    if (keyboardVisible)
        [KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
}

- (void)textViewDidEndEditing:(UITextView *)textView
{
    curTextField = nil;
    [textView resignFirstResponder];
}

- (void)keyboardWillShow:(NSNotification *)notification
{
    //---gets the size of the keyboard---
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];
    [keyboardValue getValue:&keyboardBounds];
    
	[KeyboardHelper moveScrollView:curTextField scrollView:(UIScrollView*)self.view];
    
    keyboardVisible = true;
}

- (void)keyboardWillHide:(NSNotification *)notification
{
    //---gets the size of the keyboard---
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardValue = [userInfo objectForKey:UIKeyboardBoundsUserInfoKey];
    [keyboardValue getValue:&keyboardBounds];
    
    [KeyboardHelper moveScrollView:nil scrollView:(UIScrollView*)self.view];
    
    keyboardVisible = false;
    
    curTextField = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:self.view.window];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHide:) name:UIKeyboardWillHideNotification object:self.view.window];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
}



@end
