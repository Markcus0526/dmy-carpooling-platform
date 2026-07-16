//
//  FeedbackViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_FeedbackViewController.h"

@interface Drv_FeedbackViewController ()

@end

@implementation Drv_FeedbackViewController

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
    [_txtComment setBackgroundColor:[UIColor clearColor]];
    _txtComment.placeholder = FEEDBACK_HINT;
    
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




//////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - Web Service Relation

/**
 * call advance opinion
 */
- (void) callAdvanceOpinion
{
    [SVProgressHUD showWithStatus:MSG_PLEASE_WAIT maskType:SVProgressHUDMaskTypeClear];
    
    TEST_NETWORK_RETURN;
    
    // Call the GetMoeny service routine.
    [[CommManager getCommMgr] tradeSvcMgr].delegate = self;
    [[[CommManager getCommMgr] tradeSvcMgr] AdvanceOpinion:[Common getUserID] contents:[_txtComment text] devtoken:[Common getDeviceMacAddress]];
}

- (void) advanceOpinionResult:(NSString *)result
{
    if ([result isEqualToString:SVCERR_MSG_SUCCESS])
    {
        [SVProgressHUD dismissWithSuccess:MSG_SUCCESS afterDelay:DEF_DELAY];

		// back to parent
		_txtComment.text = @"";
        [self performSelector:@selector(onClickedBack:) withObject:nil afterDelay:DEF_DELAY];
    }
    else
    {
        [SVProgressHUD dismissWithError:result afterDelay:DEF_DELAY];
    }
    
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

/**
 * Implementation of Next button clicked event
 * @author : kimoc
 * @return : N/A
 */
- (IBAction)onNextClicked:(id)sender
{
    if ([_txtComment isUsingPlaceholder]) {
        [_txtComment becomeFirstResponder];
        return;
    }
    
    [self callAdvanceOpinion];
}


@end
