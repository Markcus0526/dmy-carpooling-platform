//
//  LVDianTabBar.h
//  BJPinChe
//
//  Created by APP_USER on 14-11-15.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LVDianTabBar;

@protocol LVDianTabBarDelegate <NSObject>

//- (void)tabBar:(IWTabBar *)tabBar selectedButtonTag:(NSInteger)tag;
/**
 *  当自定义tabBar的按钮被点击之后的监听方法
 *
 *  @param tabBar 触发事件的控件
 *  @param from   上一次选中的按钮的tag
 *  @param to     当前选中按钮的tag
 */
- (void)tabBar:(LVDianTabBar *)tabBar selectedButtonfrom:(NSInteger)from to:(NSInteger)to;

@end

@interface LVDianTabBar : UIView
/**
 *  根据模型创建对应控制器的对应按钮
 *
 *  @param item 数据模型(图片/选中图片/标题)
 */
- (void)addTabBarButton:(UITabBarItem *)item;
- (void)setClickedIndex:(NSInteger )index;

@property (nonatomic, weak) id<LVDianTabBarDelegate>  delegate;

@end

