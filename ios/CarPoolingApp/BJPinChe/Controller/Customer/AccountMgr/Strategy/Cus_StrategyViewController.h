//
//  Cus_StrategyViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//  拼车攻略界面

#import <UIKit/UIKit.h>

@interface Cus_StrategyViewController : SuperViewController


- (IBAction)onClickedBack:(id)sender;

@property(nonatomic, weak) IBOutlet UIScrollView*		scrollView;
@property(nonatomic, weak) IBOutlet UIView*				backView;
@property(nonatomic, weak) IBOutlet UILabel*			lblContent;

@end
