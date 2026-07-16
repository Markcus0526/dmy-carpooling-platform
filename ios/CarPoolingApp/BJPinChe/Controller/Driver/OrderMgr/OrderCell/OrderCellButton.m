//
//  OrderCellButton.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-10.
//  Ma
//

#import "OrderCellButton.h"

@implementation OrderCellButton

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        
    }
    return self;
}
- (void)setTitle:(NSString *)title forState:(UIControlState)state
{
    [super setTitle:title forState:state];
    self.titleLabel.center =self.center;
    self.titleLabel.textAlignment = NSTextAlignmentCenter;
    //        // 设置按钮的字体大小
    self.titleLabel.font = [UIFont systemFontOfSize:13];
    //        // 设置标题颜色
    [self setTitleColor:MyColor(241,189,107)forState:UIControlStateNormal];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

// 控制器图片的位置
//// contentRect 就是当前按钮的frame
//- (CGRect)imageRectForContentRect:(CGRect)contentRect
//{
//    CGFloat imageX = 0;
//    CGFloat imageY = 0;
//    CGFloat imageW = self.frame.size.width;
//    CGFloat imageH = self.frame.size.height ;
//    return CGRectMake(imageX, imageY, imageW, imageH);
//}
//// 控制器标题的位置
//- (CGRect)titleRectForContentRect:(CGRect)contentRect
//{
//    CGFloat titleX = 0;
//    CGFloat titleY = self.frame.size.height*0.5;
//    CGFloat titleW = self.frame.size.width;
//    CGFloat titleH = 44;
//    
//    return CGRectMake(titleX, titleY, titleW, titleH);
//}

@end
