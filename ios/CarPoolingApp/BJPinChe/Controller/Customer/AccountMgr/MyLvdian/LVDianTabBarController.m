//
//  LVDianTabBarController.m
//  BJPinChe
//
//  Created by APP_USER on 14-11-15.
// 

#import "LVDianTabBarController.h"
#import "Cus_ChargeViewController.h"
#import "Cus_AccumViewController.h"
#import "Cus_WithdrawViewController.h"
#import "LVDianTabBar.h"
@interface LVDianTabBarController ()<LVDianTabBarDelegate>

@property (nonatomic, weak)LVDianTabBar *customTabBar;

@end

@implementation LVDianTabBarController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // 1  查询
        Cus_AccumViewController *accumVC =[[Cus_AccumViewController alloc]initWithNibName:@"AccumView" bundle:nil];
        [self addOneChildVc:accumVC imageName:@"btnChaxun_normal.png" selectedImageName:@"btnChaxun_active.png"];
        // 2  充值
        Cus_ChargeViewController *chargeVC =[[Cus_ChargeViewController alloc]initWithNibName:@"ChargeView" bundle:nil];
        [self addOneChildVc:chargeVC imageName:@"btnChongzhi_normal.png" selectedImageName:@"btnChongzhi_active.png"];
        // 3 提现
        Cus_WithdrawViewController *withDraw =[[Cus_WithdrawViewController alloc]initWithNibName:@"WithDrawView" bundle:nil];
        [self addOneChildVc:withDraw imageName:@"btnTixian_normal.png" selectedImageName:@"btnTixian_active.png"];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    // 1.创建自定义tabBar
    LVDianTabBar *customTabBar = [[LVDianTabBar alloc] init];
    // 设置frame
    customTabBar.frame = self.tabBar.bounds;
    [self.tabBar addSubview:customTabBar];
    self.customTabBar = customTabBar;
    // 设置代理
    customTabBar.delegate = self;
    
}

/**
 *  删除系统TabBar的Item
 *
 *  @param animated <#animated description#>
 */
- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    // 遍历系统的tabbar移除不需要的控件
    for (UIView *subView in self.tabBar.subviews) {
        // UITabBarButton私有API, 普通开发者不能使用
        if ([subView isKindOfClass:[UIControl class]]) {
            // 判断如果子控件时UITabBarButton, 就删除
            [subView removeFromSuperview];
        }
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/**
 *  添加一个子控制器
 *
 *  @param childVc           需要添加的子控制器
 *  @param imageName         需要调节自控制器的默认状态的图片
 *  @param selectedImageName 需要调节自控制器的选中状态的图片
 */
- (void)addOneChildVc:(UIViewController *)childVc  imageName:(NSString *)imageName selectedImageName:(NSString *)selectedImageName
{
    
    // 适配IOS67
    UIImage *norImage = [UIImage imageNamed:imageName];
    UIImage *selectedImage = [ UIImage imageNamed:selectedImageName];
    
    childVc.tabBarItem.image = norImage;
    // 在设置tabbar选中图片之前告诉系统, 不要根据tintColor来渲染图片
    if (iOS7) {
        selectedImage = [selectedImage imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    }
    childVc.tabBarItem.selectedImage = selectedImage;
    // 2.将自控制器添加到tabbar控制器中
    [self addChildViewController:childVc];
    
    // 3.调用自定义tabBar的添加按钮方法, 创建一个与当前控制器对应的按钮
    [self.customTabBar addTabBarButton: childVc.tabBarItem];
}
#pragma mark - TabBarDelegate
- (void)tabBar:(LVDianTabBar *)tabBar selectedButtonfrom:(NSInteger)from to:(NSInteger)to
{
    // 切换控制器
    self.selectedIndex = to;
}
/**
 *  重写系统的set方法，改变自定义TabBar相关按钮的属性
 *
 *  @param selectedIndex 选中索引
 */
- (void)setSelectedIndex:(NSUInteger)selectedIndex
{
    [super setSelectedIndex:selectedIndex];
    [self.customTabBar setClickedIndex:selectedIndex];
}
@end
