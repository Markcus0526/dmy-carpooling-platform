//
//  Drv_StrategyViewController.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-19.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface Drv_StrategyViewController : SuperViewController


- (IBAction)onClickedBack:(id)sender;
@property(nonatomic, weak) IBOutlet UIScrollView*		scrollView;
@property(nonatomic, weak) IBOutlet UIView*				backView;
@property(nonatomic, weak) IBOutlet UILabel*			lblContent;
@end
