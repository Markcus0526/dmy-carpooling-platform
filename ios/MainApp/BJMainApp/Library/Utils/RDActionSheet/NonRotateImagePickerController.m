//
//  NonRotateImagePickerController.m
//  Showhand
//
//  Created by RyuCJ on 1/29/13.
//  Copyright (c) 2013 AppDevCenter. All rights reserved.
//

#import "NonRotateImagePickerController.h"

@implementation NonRotateImagePickerController

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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#ifdef IOS6
- (BOOL) shouldAutorotate{
    return NO;
}
#endif

@end
