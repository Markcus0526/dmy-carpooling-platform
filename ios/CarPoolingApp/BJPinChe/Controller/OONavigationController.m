//
//  OONavigationController.m
//  BJPinChe
//
//  Created by APP_USER on 14-10-8.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "OONavigationController.h"

@interface OONavigationController ()

@end

@implementation OONavigationController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


- (void)loadView
{
    [super loadView];
    
    //修改标题颜色字体属性
    self.navigationBar.titleTextAttributes =
    @{UITextAttributeTextColor: [UIColor redColor],
      UITextAttributeFont : [UIFont systemFontOfSize:10]};
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //  self.navigationBar.tintColor =[UIColor whiteColor];
    
    [self.navigationBar setBackgroundImage:[UIImage imageNamed:@"titlebar_back"] forBarMetrics:UIBarMetricsLandscapePhone];
//#warning 设置navigationBar 默认自动添加的返回item 的颜色
    [self.navigationBar  setTintColor:[UIColor clearColor]];
    //[self.navigationBar  setBarTintColor:[UIColor blackColor]];
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
    // Do any additional setup after loading the view.
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end