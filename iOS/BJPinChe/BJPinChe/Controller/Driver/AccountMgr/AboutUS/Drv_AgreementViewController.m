//
//  Drv_AgreementViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-20.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "Drv_AgreementViewController.h"

@interface Drv_AgreementViewController ()

@end

@implementation Drv_AgreementViewController

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


	// Initialize UI elements
	[_lblContent sizeToFit];

	_backView.frame = CGRectMake(_backView.frame.origin.x, _lblContent.frame.origin.y, _lblContent.frame.size.width + 20, _lblContent.frame.size.height + 20);
	_backView.layer.cornerRadius = 5;
	_backView.layer.borderColor = [UIColor lightGrayColor].CGColor;
	_backView.layer.borderWidth = 1;

	_scrollView.contentSize = CGSizeMake(_backView.frame.size.width + 20, _backView.frame.size.height + 20);
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
#pragma mark - UI Button clicked event implementation

- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}

@end
