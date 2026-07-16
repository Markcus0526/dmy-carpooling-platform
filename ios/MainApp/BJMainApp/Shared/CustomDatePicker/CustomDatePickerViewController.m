//
//  CustomDatePickerViewController.m
//  DingSheng
//
//  Created by Kim Ok Chol on 11/2/14.
//  Copyright (c) 2014 damy. All rights reserved.
//

#import "CustomDatePickerViewController.h"

@interface CustomDatePickerViewController ()

@end

@implementation CustomDatePickerViewController

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
    // Do any additional setup after loading the view.
    
    switch (mCurStyle) {
        case PICKERSTYLE_DATE:
            [_datePicker setDatePickerMode:UIDatePickerModeDate];
            break;
        case PICKERSTYLE_DATETIME:
        default:
            [_datePicker setDatePickerMode:UIDatePickerModeDateAndTime];
            break;
    }
    
    // initialize title
    [_lblTitle setText:[self getCurPickerString]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSString *) getCurPickerString
{
    NSDate *selected = [_datePicker date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    
    switch (mCurStyle) {
        case PICKERSTYLE_DATE:
            [dateFormatter setDateFormat:@"yyyy-MM-dd"];
            break;
        case PICKERSTYLE_DATETIME:
        default:
            [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
            break;
    }
    
    NSString *destDateString = [dateFormatter stringFromDate:selected];

    return destDateString;
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

/**
 * Set date picker style (date, datetime)
 */
- (void) setDatePickerStyle:(enum CustomDatePickerStyle)style
{
    mCurStyle = style;
}


- (IBAction)onPickerChanged:(id)sender
{
    [_lblTitle setText:[self getCurPickerString]];
}


- (IBAction)onTapCancel:(id)sender
{
    [delegate didChangedCustomDatePicker:NO date:nil];
    
    // dismiss current view controller
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onTapConfirm:(id)sender
{
    NSString *destDateString = [self getCurPickerString];
    
    [delegate didChangedCustomDatePicker:YES date:destDateString];
    
    // dismiss current view controller
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
