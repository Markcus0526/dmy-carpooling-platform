//
//  ResultDlgView.h
//  BJPinChe
//
//  Created by KHM on 14-12-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ResultDlgView : UIView

@property (nonatomic, retain) UIView*			containerView;
@property (nonatomic, retain) UIImageView*		imgEmoji;
@property (nonatomic, retain) UILabel*			lblMessage;
@property (nonatomic, retain) UIButton*			btnDismiss;

@property (nonatomic, retain) NSString*			message;
@property (atomic, readwrite) BOOL				success;


- (void)setSuccess:(BOOL)success message:(NSString*)message;
- (void)dismissWithDelay:(int)delay_seconds;


+ (UIView*)showInController:(UIViewController*)controller success:(BOOL)success message:(NSString*)message;


@end
