//
//  BindAccountViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/29/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_BindAccountViewController.h"

@interface Drv_BindAccountViewController ()

@end

@implementation Drv_BindAccountViewController

@synthesize mOrgAccount;

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
    [self makeBankData];
    
    [_pickerBank reloadAllComponents];
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


///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    mBankArray = [[NSMutableArray alloc] init];
    mTempBank = @"工商银行";
    
    [_bankSelView setHidden:YES];
    
    // set original bank account;
    [_lblOldAccount setText:mOrgAccount];
    
    
    // make tap recognizer to hide keyboard
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

/**
 * get accum history data ( make temp data list )
 */
- (void) makeBankData
{
    [mBankArray addObject:@"工商银行"];
    [mBankArray addObject:@"建设银行"];
    [mBankArray addObject:@"农业银行"];
    [mBankArray addObject:@"中国银行"];
    [mBankArray addObject:@"招商银行"];
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
 * Clicked bank list
 * @author : kimoc
 * @return : action
 */
- (IBAction)onBankSelect:(id)sender
{
    [_bankSelView setHidden:NO];
}

/**
 * Clicked bind new account
 * @author : kimoc
 * @return : action
 */
- (IBAction)onClickedBind:(id)sender
{
    if ([[_txtNewAccount text] isEqual:@""]) {
        [_txtNewAccount becomeFirstResponder];
        return;
    }
    if ([[_txtOpenBranch text] isEqual:@""]) {
        [_txtOpenBranch becomeFirstResponder];
        return;
    }
    
    [self callChangeBind];
}

/**
 * Clicked release binded account
 * @author : kimoc
 * @return : action
 */
- (IBAction)onClickedDelBind:(id)sender
{
    UIAlertView * alert = [[UIAlertView alloc] initWithTitle:MSG_REMOVE_BIND message:nil delegate:self cancelButtonTitle:STR_BUTTON_CANCEL otherButtonTitles:nil];
    [alert addButtonWithTitle:STR_BUTTON_CONFIRM];
    [alert show];
}



//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call release current bind account
 */
- (void) callChangeBind
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] BindBankCard:[Common getUserID] bankcard:[_txtNewAccount text] bankname:mTempBank subbranch:[_txtOpenBranch text] devtoken:[Common getDeviceMacAddress]];
}

- (void) bindBankCardResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_REMOVE_BIND_SUCCESS afterDelay:DEF_DELAY];
        
        // back to parent
        [self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}


/**
 * call release current bind account
 */
- (void) callReleaseBind
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] ReleaseBankCard:[Common getUserID] devtoken:[Common getDeviceMacAddress]];
}

- (void) releaseBankCardResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_REMOVE_BIND_SUCCESS afterDelay:DEF_DELAY];
        
        // back to parent
        [self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark UIAlertView related stuff

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 1) {
        // on confirm
        [self callReleaseBind];
    }
}

#pragma mark UIPickerView related stuff

//Method to define how many columns/dials to show
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 1;
}

// Method to define the numberOfRows in a component using the array.
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent :(NSInteger)component
{
    return [mBankArray count];
}

- (CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component
{
    return 50;
}

// Method to show the title of row for a component.
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    if ([mBankArray count] <= 0) {
        return @"";
    }
    
    return [mBankArray objectAtIndex:row];
    
}


- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    if ([mBankArray count]>0) {
        mTempBank = [mBankArray objectAtIndex:row];
    }
}

#pragma mark Bank Selection Event handlers

- (IBAction)onCancelBankClicked:(id)sender {
    
    [_bankSelView setHidden:YES];
}

- (IBAction)onDoneBankClicked:(id)sender {
    
    [_lblBank setText:mTempBank];
    
    [_bankSelView setHidden:YES];
}

@end
