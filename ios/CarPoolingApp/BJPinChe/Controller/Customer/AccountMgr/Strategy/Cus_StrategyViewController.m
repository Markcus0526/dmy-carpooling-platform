//
//  Cus_StrategyViewController.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  拼车攻略界面


#import "Cus_StrategyViewController.h"

@interface Cus_StrategyViewController ()

@end

@implementation Cus_StrategyViewController

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
#warning 拼车攻略界面 未完成

///////////////////////////////////////////////////////////////////////////
#pragma mark - UI Button clicked event implementation

/**
 * Back to parent view controller
 */
- (IBAction)onClickedBack:(id)sender
{
    BACK_VIEW;
}


@end
