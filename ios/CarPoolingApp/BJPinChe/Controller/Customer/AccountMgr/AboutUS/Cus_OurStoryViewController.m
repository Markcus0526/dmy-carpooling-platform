//
//  OurStoryViewController.m
//  BJMainApp
//
//  Created by KimOC on 7/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "Cus_OurStoryViewController.h"

@interface Cus_OurStoryViewController ()

@end

@implementation Cus_OurStoryViewController

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
    [_lable sizeToFit];
    
    _brackView.frame = CGRectMake(_brackView.frame.origin.x, _lable.frame.origin.y, _lable.frame.size.width + 20, _lable.frame.size.height + 20);
    //    _brackView.layer.cornerRadius = 5;
    //    _brackView.layer.borderColor = [UIColor lightGrayColor].CGColor;
    //    _brackView.layer.borderWidth = 1;
    
    _scrollView.contentSize = CGSizeMake(_brackView.frame.size.width + 20, _brackView.frame.size.height + 20);
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
