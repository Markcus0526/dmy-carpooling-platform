//
//  DetailButton.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "DetailButton.h"
#define ButtonRatio  0.3
@implementation DetailButton

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
       
    }
    return self;
}

// 控制器图片的位置
// contentRect 就是当前按钮的frame
- (CGRect)imageRectForContentRect:(CGRect)contentRect
{
    CGFloat imageX = 2;
    CGFloat imageY = 1;
    CGFloat imageW = self.frame.size.width*ButtonRatio;
    CGFloat imageH = self.frame.size.height-2;
    return CGRectMake(imageX, imageY, imageW, imageH);
}
// 控制器标题的位置
- (CGRect)titleRectForContentRect:(CGRect)contentRect
{
    CGFloat titleX = self.frame.size.width*(ButtonRatio+0.1);
    CGFloat titleY = 0;
    CGFloat titleW = self.frame.size.width*(0.9-ButtonRatio);
    CGFloat titleH = self.frame.size.height;
    
    return CGRectMake(titleX, titleY, titleW, titleH);
}

@end
