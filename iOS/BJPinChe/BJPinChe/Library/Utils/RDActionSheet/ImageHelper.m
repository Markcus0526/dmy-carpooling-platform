//
//  ImageHelper.m
//  Showhand
//
//  Created by GyongMin Om on 12. 12. 13..
//  Copyright (c) 2012년 AppDevCenter. All rights reserved.
//

#import "ImageHelper.h"

@implementation UIImage (Extras)

#pragma mark -
#pragma mark Scale and crop image

- (UIImage*)imageByScalingAndCroppingForSize:(CGSize)targetSize
{
    UIImage *sourceImage = self;
    UIImage *newImage = nil;    
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = targetSize.width;
    CGFloat targetHeight = targetSize.height;
    CGFloat scaleFactor = 0.0;
    CGFloat scaledWidth = targetWidth;
    CGFloat scaledHeight = targetHeight;
    
    if (CGSizeEqualToSize(imageSize, targetSize) == NO) 
    {
        CGFloat widthFactor = targetWidth / width;
        CGFloat heightFactor = targetHeight / height;
        
        if (widthFactor > 1.0f && heightFactor > 1.0f)
            scaleFactor = 1.0f;
        else {
            if (widthFactor > heightFactor) 
            {
                scaleFactor = widthFactor; // scale to fit height
            }
            else
            {
                scaleFactor = heightFactor; // scale to fit width
            }
        }        
        scaledWidth  = width * scaleFactor;
        scaledHeight = height * scaleFactor;        
    }   
    
    UIGraphicsBeginImageContext(targetSize); // this will crop
    
    CGRect thumbnailRect = CGRectMake(0, 0, scaledWidth, scaledHeight);    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    if(newImage == nil)
    {
        NSLog(@"could not scale image");
    }
    
    //pop the context to get back to the default
    UIGraphicsEndImageContext();
    
    return newImage;
}

@end