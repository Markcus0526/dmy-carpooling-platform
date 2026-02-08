//
//  MoveFromRight.m
//  BJMainApp
//
//  Created by KimOC on 7/28/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "MoveFromRight.h"
#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@implementation MoveFromRight

- (void) perform
{
    UIViewController *sourceViewController = (UIViewController*)[self sourceViewController];
    UIViewController *destinationController = (UIViewController*)[self destinationViewController];
    
//    CATransition* transition = [CATransition animation];
//    transition.duration = .25;
//    transition.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseInEaseOut];
//    transition.type = kCATransitionPush; //kCATransitionMoveIn; //, kCATransitionPush, kCATransitionReveal, kCATransitionFade
//    transition.subtype = kCATransitionFromLeft; //kCATransitionFromLeft, kCATransitionFromRight, kCATransitionFromTop, kCATransitionFromBottom
//    
//    
//    
//    [sourceViewController.navigationController.view.layer addAnimation:transition
//                                                                forKey:kCATransition];
//    
//    [sourceViewController.navigationController pushViewController:destinationController animated:NO];

    
//    CATransition *animation = [CATransition animation]; \
//    [animation setDuration:0.3]; \
//    [animation setType:kCATransitionPush]; \
//    [animation setSubtype:kCATransitionFromLeft]; \
//    [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]]; \
//    [[sourceViewController.view.superview layer] addAnimation:animation forKey:@"SwitchToView"];
//    [sourceViewController presentViewController:destinationController animated:NO completion:nil];
//    
    
    
//    CATransition *animation = [CATransition animation]; \
//    [animation setDuration:0.3]; \
//    [animation setType:kCATransitionMoveIn]; \
//    [animation setSubtype:kCATransitionFromLeft]; \
//    [animation setTimingFunction:[CAMediaTimingFunction functionWithName: kCAMediaTimingFunctionEaseInEaseOut]];
//    
//    [[destinationController.view layer] addAnimation:animation forKey:kCATransition];
//    [sourceViewController presentViewController:destinationController animated:NO completion:nil];

    
    
//    [sourceViewController.view addSubview:destinationController.view];
//    
//    destinationController.view.transform = CGAffineTransformMakeScale(0.05, 0.05);
//    
//    CGPoint originalCenter = destinationController.view.center;
//    
//    [UIView animateWithDuration:0.5
//                          delay:0.0
//                        options:UIViewAnimationOptionCurveEaseInOut
//                     animations:^{
//                         destinationController.view.transform = CGAffineTransformMakeScale(1.0, 1.0);
//                         destinationController.view.center = originalCenter;
//                     }
//                     completion:^(BOOL finished) {
//                         [destinationController.view removeFromSuperview];
//                         [sourceViewController presentViewController:destinationController animated:NO completion:nil];
//                     }];
    
    
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
                         [sourceViewController presentViewController:destinationController animated:NO completion:nil];
                     }];
}




@end
