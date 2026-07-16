//
//  KeyboardHelper.h
//  Showhand
//
//  Created by GyongMin Om on 12. 10. 20..
//  Copyright 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

extern UIView	*curTextField;
extern bool		bTyping;
extern CGRect	keyboardBounds;
extern CGRect	applicationFrame;


@interface KeyboardHelper : NSObject {

}

+ (void)moveScrollView:(UIView*)theView scrollView:(UIScrollView*)scrollView;

@end

