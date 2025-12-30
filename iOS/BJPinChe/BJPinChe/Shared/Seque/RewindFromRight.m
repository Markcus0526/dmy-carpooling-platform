//
//  RewindFromRight.m
//  BJMainApp
//
//  Created by KimOC on 7/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "RewindFromRight.h"

@implementation RewindFromRight

- (void) perform
{
    UIViewController *sourceViewController = (UIViewController*)[self sourceViewController];
    UIViewController *destinationController = (UIViewController*)[self destinationViewController];
    
    [sourceViewController.view addSubview:destinationController.view];
    
    CGRect rtDest = destinationController.view.frame;
    destinationController.view.frame = CGRectMake(rtDest.size.width, rtDest.origin.y, rtDest.size.width, rtDest.size.height);
    
    [UIView animateWithDuration:0.3
                          delay:0.0
                        options:UIViewAnimationOptionCurveEaseInOut
                     animations:^{
                         destinationController.view.frame = rtDest;
                     }
                     completion:^(BOOL finished) {
                         [destinationController.view removeFromSuperview];
                         [sourceViewController dismissViewControllerAnimated:NO completion:nil];
                     }];
}


@end
