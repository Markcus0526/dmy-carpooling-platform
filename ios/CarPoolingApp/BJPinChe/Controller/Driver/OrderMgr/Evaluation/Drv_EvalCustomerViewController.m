//
//  Cus_EvalDriverViewController.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Drv_EvalCustomerViewController.h"
#import "UIViewController+CWPopup.h"

@interface Drv_EvalCustomerViewController ()

@end


#define MSG_VIEW_HEIGHT                             74


@implementation Drv_EvalCustomerViewController

@synthesize delegate;

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
    
    [self initControls];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


///////////////////////////////////////////////////////////////////////////
#pragma mark - Basic Function

/**
 * Initilaize all ui controls & member variable
 */
- (void) initControls
{
    _txtMessage.delegate = self;
    mLevel = EVAL_HIGH;
}

- (IBAction)onClickedBackground:(id)sender
{
    [self.view endEditing:YES];
}

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

- (void) initData : (id)parent dataInfo:(STPassengerInfo *)dataInfo;
{
    mParent = parent;
    mDataInfo = dataInfo;
}

- (IBAction)onClickedCancel:(id)sender
{
    UIViewController * parent = (UIViewController *)mParent;
    
    // dismiss popup
    if (parent.popupViewController != nil) {
        [parent dismissPopupViewControllerAnimated:YES completion:^{
            NSLog(@"popup view dismissed");
        }];
    }
}

- (IBAction)onClickedSubmit:(id)sender
{
	UIViewController* parent = (UIViewController *)mParent;

	if (_txtMessage.text.length == 0) {
		NSString* szMsg = @"详细评价不得为空";
		[self.view makeToast:szMsg];
		[_txtMessage becomeFirstResponder];
		return;
	}
	
//#warning 此处需要判断是否为空
//    if (_txtMessage.text.length ==0 && mLevel == 1) {
//        _txtMessage.text =@"默认好评";
//    }

	[delegate submitComment:[_txtMessage text] level:mLevel passInfo:mDataInfo];

	// dismiss popup
    if (parent.popupViewController != nil) {
		[parent dismissPopupViewControllerAnimated:YES completion:^{
			NSLog(@"popup view dismissed");
		}];
	}

}

- (IBAction)markArrowClick:(id)sender {
    if (_radioEvalMedium.selected == YES || _radioEvalLow.selected == YES) {
        if (_markArrow.selected == YES) {
            _markArrow.selected = NO;
            [UIView beginAnimations:nil context:nil];
            [UIView setAnimationDuration:0.3];
            
            // change view position & width
            CGRect rcRect = self.view.frame;
            [self.view setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y + MSG_VIEW_HEIGHT / 2, rcRect.size.width, rcRect.size.height - MSG_VIEW_HEIGHT)];
            
            rcRect = _vwUsualMsg.frame;
            [_vwUsualMsg setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y, rcRect.size.width, 0)];
            
            rcRect = _vwBottom.frame;
            [_vwBottom setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y - MSG_VIEW_HEIGHT, rcRect.size.width, rcRect.size.height)];
            
            [_markArrow setSelected:NO];
            
            [UIView commitAnimations];
        }else
        {
            _markArrow.selected = YES;
            [UIView beginAnimations:nil context:nil];
            [UIView setAnimationDuration:0.3];
            
            // change view position & width
            CGRect rcRect = self.view.frame;
            [self.view setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y - MSG_VIEW_HEIGHT / 2, rcRect.size.width, rcRect.size.height + MSG_VIEW_HEIGHT)];
            
            rcRect = _vwUsualMsg.frame;
            [_vwUsualMsg setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y, rcRect.size.width, rcRect.size.height + MSG_VIEW_HEIGHT)];
            
            rcRect = _vwBottom.frame;
            [_vwBottom setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y + MSG_VIEW_HEIGHT, rcRect.size.width, rcRect.size.height)];
            
            [_markArrow setSelected:YES];
            
            [UIView commitAnimations];
        }
    }
}

- (IBAction)onSelectEvalHigh:(id)sender
{
    // change view size
    if (![_radioEvalHigh isSelected]) {
        if (_markArrow.selected) {
            // set animation
            [UIView beginAnimations:nil context:nil];
            [UIView setAnimationDuration:0.3];
            
            // change view position & width
            CGRect rcRect = self.view.frame;
            [self.view setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y + MSG_VIEW_HEIGHT / 2, rcRect.size.width, rcRect.size.height - MSG_VIEW_HEIGHT)];
            
            rcRect = _vwUsualMsg.frame;
            [_vwUsualMsg setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y, rcRect.size.width, 0)];
            
            rcRect = _vwBottom.frame;
            [_vwBottom setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y - MSG_VIEW_HEIGHT, rcRect.size.width, rcRect.size.height)];
            
            [_markArrow setSelected:NO];
            
            [UIView commitAnimations];
        }
        // set default good comment
        [_txtMessage setText:DRV_DEF_GOODEVAL];
    }
    [_radioEvalHigh setSelected:YES];
    [_radioEvalMedium setSelected:NO];
    [_radioEvalLow setSelected:NO];
    
    mLevel = EVAL_HIGH;
}

- (IBAction)onSelectEvalMedium:(id)sender
{
    // change view size
    if ([_radioEvalHigh isSelected]) {
        
        // set animation// change view size
        
        [UIView beginAnimations:nil context:nil];
        [UIView setAnimationDuration:0.3];
        
        // change view position & width
        CGRect rcRect = self.view.frame;
        [self.view setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y - MSG_VIEW_HEIGHT / 2, rcRect.size.width, rcRect.size.height + MSG_VIEW_HEIGHT)];
        
        rcRect = _vwUsualMsg.frame;
        [_vwUsualMsg setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y, rcRect.size.width, rcRect.size.height + MSG_VIEW_HEIGHT)];
        
        rcRect = _vwBottom.frame;
        [_vwBottom setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y + MSG_VIEW_HEIGHT, rcRect.size.width, rcRect.size.height)];
        
        [_markArrow setSelected:YES];
        
        [UIView commitAnimations];
        
        // remove comment
        [_txtMessage setText:@""];
    }
    
    [_radioEvalHigh setSelected:NO];
    [_radioEvalMedium setSelected:YES];
    [_radioEvalLow setSelected:NO];
    
    mLevel = EVAL_MEDIUM;
}

- (IBAction)onSelectEvalLow:(id)sender
{
    // change view size
    if ([_radioEvalHigh isSelected]) {
        
        // set animation
        [UIView beginAnimations:nil context:nil];
        [UIView setAnimationDuration:0.3];
        
        // change view position & width
        CGRect rcRect = self.view.frame;
        [self.view setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y - MSG_VIEW_HEIGHT / 2, rcRect.size.width, rcRect.size.height + MSG_VIEW_HEIGHT)];
        
        rcRect = _vwUsualMsg.frame;
        [_vwUsualMsg setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y, rcRect.size.width, rcRect.size.height + MSG_VIEW_HEIGHT)];
        
        rcRect = _vwBottom.frame;
        [_vwBottom setFrame:CGRectMake(rcRect.origin.x, rcRect.origin.y + MSG_VIEW_HEIGHT, rcRect.size.width, rcRect.size.height)];
        
        [_markArrow setSelected:YES];
        
        [UIView commitAnimations];
        
        // remove comment
        [_txtMessage setText:@""];
    }
    
    [_radioEvalHigh setSelected:NO];
    [_radioEvalMedium setSelected:NO];
    [_radioEvalLow setSelected:YES];
    
    mLevel = EVAL_LOW;
}

- (IBAction)onClickedComment1:(id)sender
{
    NSMutableString *comment = [NSMutableString string];
    [comment appendString:[_txtMessage text]];
    NSRange range = [comment rangeOfString:DRV_DEF_BADEVAL1];
    if (range.length == 0) {
        [comment appendString:DRV_DEF_BADEVAL1];
    }
    
    [_txtMessage setText:comment];
}

- (IBAction)onClickedComment2:(id)sender
{
    NSMutableString *comment = [NSMutableString string];
    [comment appendString:[_txtMessage text]];
    NSRange range = [comment rangeOfString:DRV_DEF_BADEVAL2];
    if (range.length == 0) {
        [comment appendString:DRV_DEF_BADEVAL2];
    }
    
    [_txtMessage setText:comment];
}

- (IBAction)onClickedComment3:(id)sender
{
    NSMutableString *comment = [NSMutableString string];
    [comment appendString:[_txtMessage text]];
    NSRange range = [comment rangeOfString:DRV_DEF_BADEVAL3];
    if (range.length == 0) {
        [comment appendString:DRV_DEF_BADEVAL3];
    }
    [_txtMessage setText:comment];
}

- (IBAction)onClickedComment4:(id)sender
{
    NSMutableString *comment = [NSMutableString string];
    [comment appendString:[_txtMessage text]];
    NSRange range = [comment rangeOfString:DRV_DEF_BADEVAL4];
    if (range.length == 0) {
        [comment appendString:DRV_DEF_BADEVAL4];
    }
    [_txtMessage setText:comment];
}

-(BOOL)textViewShouldBeginEditing:(UITextView *)textView
{
    if ([_txtMessage.text isEqualToString:CUS_DEF_GOODEVAL]) {
        _txtMessage.text =@"";
    }
    return YES;
}
-(BOOL)textViewShouldEndEditing:(UITextView *)textView
{
    if ([_txtMessage.text isEqualToString:@""]&& mLevel == 1) {
        _txtMessage.text =CUS_DEF_GOODEVAL;
    }
    return YES;
}

@end
