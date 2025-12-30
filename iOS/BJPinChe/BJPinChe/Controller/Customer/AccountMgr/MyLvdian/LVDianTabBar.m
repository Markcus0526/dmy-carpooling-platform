//
//  LVDianTabBar.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-15.
//

#import "LVDianTabBar.h"

@interface LVDianTabBar()

/**
 *  保存所有选项卡按钮
 */
@property (nonatomic, strong) NSMutableArray  *buttons;
/**
 *  当前选中的按钮
 */
@property (nonatomic, weak) UIButton  *selectedButton;

@end

@implementation LVDianTabBar

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

#pragma mark - 设置子控件frame
- (void)layoutSubviews
{
#warning 一定要调用父类方法
    [super layoutSubviews];

    // 2.设置选项卡按钮的frame
    [self setupBtnFrame];
}
/**
 *  设置选项卡按钮的frame
 */
- (void)setupBtnFrame
{
    // 遍历数组设置选项卡frame
    // 计算宽度, 获取高度, 获取按钮的个数只需要执行一次即可
    int count = self.buttons.count;
    CGFloat btnWidth  = self.frame.size.width / (count);
    CGFloat btnHeigth = self.frame.size.height;
    for (int i = 0; i < count; i++) {
        UIButton *btn = self.buttons[i];
        btn.frame = CGRectMake(i *btnWidth, 0, btnWidth, btnHeigth);
    }
    
}
#pragma mark - 添加按钮方法
- (void)addTabBarButton:(UITabBarItem *)item
{
    //    UIButton *btn = [[UIButton alloc] init];
    // 1.创建按钮
    UIButton *btn = [[UIButton alloc] init];
    // 设置按钮的tag
    btn.tag = self.buttons.count;

    [btn setImage:item.image forState:UIControlStateNormal];
    [btn setImage:item.selectedImage forState:UIControlStateSelected];
    
     [self addSubview:btn];
    // 3.每次添加完按钮后将按钮存储到数组中
    [self.buttons addObject:btn];
    
    // 4.监听按钮点击事件
    [btn addTarget:self action:@selector(buttonOnClick:) forControlEvents:UIControlEventTouchDown];
    
    //5.设置默认选中的按钮
        if (self.buttons.count == 1)
        {
            // 选中某一个按钮就相当于点击某一个按钮
            [self buttonOnClick:btn];
        }
}
- (void)buttonOnClick:(UIButton *)btn
{
    // 0. 通知代理按钮被点击了
    if ([self.delegate respondsToSelector:@selector(tabBar:selectedButtonfrom:to:)]) {
        [self.delegate tabBar:self selectedButtonfrom:self.selectedButton.tag to:btn.tag];
    }
}
- (void)setClickedIndex:(NSInteger )index
{
    UIButton *btn =  self.buttons[index];
    // 1.取消上一次选中的按钮
    self.selectedButton.selected = NO;
    // 2.选中当前按钮
    btn.selected = YES;
    // 3.记录当前选中的按钮
    self.selectedButton = btn;
}

#pragma mark - 懒加载
- (NSMutableArray *)buttons
{
    if (!_buttons) {
        _buttons = [NSMutableArray array];
    }
    return _buttons;
}

@end
