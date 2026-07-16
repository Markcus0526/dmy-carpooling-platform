//
//  LoginViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "LoginViewController.h"
#import "Config.h"
#import "MainTabViewController.h"
#import "ResultDlgView.h"

@interface LoginViewController ()

@end

#define OPTION_NONE                         @"-1"
#define OPTION_SAVE_PWD                     @"0"
#define OPTION_AUTO_LOGIN                   @"1"
#define OPTION_SAVE_AUTO_LOGIN              @"2"


@implementation LoginViewController

@synthesize mTargetTab;

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
    
    [self initControls];
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
    // register textfield delegate
    _txtUsername.delegate = self;
    _txtPassword.delegate = self;
    
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
 * check login option & save selected state (save password, auto login)
 */
- (void) SaveLoginOption
{
    // save user account
    [Config setLoginName:[_txtUsername text]];
    [Config setLoginPassword:[_txtPassword text]];
    
}


- (void) gotoNext
{
	MainTabViewController * main = (MainTabViewController *)[self.navigationController.viewControllers objectAtIndex:0];
	main.selectedViewController = [main.viewControllers objectAtIndex:mTargetTab];
	[self.navigationController popToRootViewControllerAnimated:YES];
}



///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */

- (IBAction)onClickedBack:(id)sender
{
    [self.navigationController dismissViewControllerAnimated:YES completion:nil];
}


- (IBAction)onClickedLogin:(id)sender
{
	NSString *userText = [_txtUsername text];
	NSString *pwdText = [_txtPassword text];

	[_txtUsername resignFirstResponder];
	[_txtPassword resignFirstResponder];

	if ([userText isEqualToString:@""])
	{
		[_txtUsername becomeFirstResponder];
		SHOW_MSG(MSG_INPUT_NAME);
		return;
	}
	else if ([pwdText isEqualToString:@""])
	{
		[_txtPassword becomeFirstResponder];
		SHOW_MSG(MSG_INPUT_PASSWORD);
		return;
	}

	[self callLoginUser];
}


//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call service to get child app list
 */
- (void) callLoginUser
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetCurOrderList service routine.
    [[CommManager getCommMgr] accountSvcMgr].delegate = self;
    [[[CommManager getCommMgr] accountSvcMgr] LoginUser:_txtUsername.text password:_txtPassword.text city:[Common getCurrentCity] devtoken:[Common getDeviceMacAddress]];
}

- (void) loginUserResult:(NSString *)result userinfo:(STUserInfo *)userinfo
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS]) {
        
        [SVProgressHUD dismiss];
        
        // set user info
        [Common setUserInfo:userinfo];
        
        [self SaveLoginOption];
        [self gotoNext];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:2];
    }
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
