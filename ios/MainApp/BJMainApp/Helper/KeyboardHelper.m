//
//  KeyboardHelper.m
//  Showhand
//
//  Created by GyongMin Om on 12. 10. 20..
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import "KeyboardHelper.h"

UIView		*curTextField = nil;
bool		bTyping = false;

//---size of keyboard---
CGRect keyboardBounds;

//---size of application screen---
CGRect applicationFrame;


@implementation KeyboardHelper

+ (void)moveScrollView:(UIView*)theView scrollView:(UIScrollView*)scrollView {
    
    applicationFrame = [[UIScreen mainScreen] applicationFrame];
    //---calculate how much the scrollview must scroll---
    CGFloat scrollAmount = 0;
	if (theView != nil)
	{
        // convert sub view center position to parent
		CGPoint ptCenter = theView.center;
        
        /**** appended by kimoc ****/
        ptCenter = [scrollView convertPoint:ptCenter fromView:theView.superview];
    
		//---get the y-coordinate of the view---
        CGFloat viewCenterY = ptCenter.y;
	
        
		//---calculate how much visible space is left---
		CGFloat freeSpaceHeight = applicationFrame.size.height - keyboardBounds.size.height;
		
		scrollAmount = viewCenterY - freeSpaceHeight / 2.0;
	}
	
    if (scrollAmount < 0) scrollAmount = 0;
	if (scrollAmount > keyboardBounds.size.height) scrollAmount = keyboardBounds.size.height;
    
    //---set the new scrollView contentSize---
    //scrollView.contentSize = CGSizeMake(applicationFrame.size.height, applicationFrame.size.width + keyboardBounds.size.height);
    
    //---scroll the ScrollView---
    [scrollView setContentOffset:CGPointMake(0, scrollAmount) animated:YES];
}


@end
