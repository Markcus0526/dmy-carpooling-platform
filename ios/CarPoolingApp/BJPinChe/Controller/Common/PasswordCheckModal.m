//
//  Cus_LongOrderPasswordViewController.m
//  BJPinChe
//
//  Created by CKK on 14-8-28.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  电子票页面控制器

#import "PasswordCheckModal.h"
#import "Cus_longOrderSuccessViewController.h"
#import "UIViewController+CWPopup.h"


@interface PasswordCheckModal () {
	NSString* szPwd;
}

@end



@implementation PasswordCheckModal



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    if (self.tips.length>5) {
         [self.tipsLabel setText:self.tips];
    }
    szPwd = @"";
   
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)OnNumClick:(id)sender {
	if (self.ShowLabel.text.length >= 4) {
		return;
	}

	szPwd = [NSString stringWithFormat:@"%@%d",szPwd,[sender tag]%10];
	self.ShowLabel.text = [self.ShowLabel.text stringByAppendingString:@"*"];
}


- (IBAction)OnClearClick:(id)sender {
	szPwd = @"";
    self.ShowLabel.text = @"";
}


- (IBAction)OnDoneClick:(id)sender {
	if (self.ShowLabel.text.length != 4) {
		[self shakeAnimationForView:self.ShowLabel];
		[self.view makeToast:@"电子密码不正确" duration:2 position:@"center"];
		return;
	}


	[self.delegate tappedDone:szPwd];
}


- (void)shakeAnimationForView:(UIView *) view

{
    
    // 获取到当前的View
    
    CALayer *viewLayer = view.layer;
    
    // 获取当前View的位置
    
    CGPoint position = viewLayer.position;
    
    // 移动的两个终点位置
    
    CGPoint x = CGPointMake(position.x + 10, position.y);
    
    CGPoint y = CGPointMake(position.x - 10, position.y);
    
    // 设置动画
    
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"position"];
    
    // 设置运动形式
    
    [animation setTimingFunction:[CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionDefault]];
    
    // 设置开始位置
    
    [animation setFromValue:[NSValue valueWithCGPoint:x]];
    
    // 设置结束位置
    
    [animation setToValue:[NSValue valueWithCGPoint:y]];
    
    // 设置自动反转
    
    [animation setAutoreverses:YES];
    
    // 设置时间
    
    [animation setDuration:.06];
    
    // 设置次数
    
    [animation setRepeatCount:3];
    
    // 添加上动画
    
    [viewLayer addAnimation:animation forKey:nil];
    
    
    
}


@end
