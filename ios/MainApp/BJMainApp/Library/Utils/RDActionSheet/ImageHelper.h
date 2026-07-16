//
//  ImageHelper.h
//  Showhand
//
//  Created by GyongMin Om on 12. 12. 13..
//  Copyright (c) 2012년 AppDevCenter. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Extras)

- (UIImage*)imageByScalingAndCroppingForSize:(CGSize)targetSize;
@end