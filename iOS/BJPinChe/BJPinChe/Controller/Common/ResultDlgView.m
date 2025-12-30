//
//  ResultDlgView.m
//  BJPinChe
//
//  Created by KHM on 14-12-18.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "ResultDlgView.h"

@implementation ResultDlgView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/


- (id)initWithFrame:(CGRect)frame {
	self = [super initWithFrame:frame];

	if (self) {
		[self initLayout];
	}

	return self;
}


- (void)initLayout {
	self.backgroundColor = [UIColor clearColor];

	CGSize containerSize = CGSizeMake(280, 240);
	CGRect rcFrame = self.bounds;
	CGRect rcContainer = CGRectMake((rcFrame.size.width - containerSize.width) / 2, (rcFrame.size.height - containerSize.height) / 2, containerSize.width, containerSize.height);

	_containerView = [[UIView alloc] initWithFrame:rcContainer];
	_containerView.backgroundColor = [UIColor whiteColor];
	_containerView.layer.cornerRadius = 5;

	[self addSubview:_containerView];


	CGSize imgSize = CGSizeMake(140, 100);
	CGRect rcImage = CGRectMake(containerSize.width / 2 - imgSize.width / 2, 30, imgSize.width, imgSize.height);
	_imgEmoji = [[UIImageView alloc] initWithFrame:rcImage];

	[_containerView addSubview:_imgEmoji];


	int labelLeftMargin = 20;
	int labelTopMargin = 10;

	CGRect rcLabel = CGRectMake(labelLeftMargin, rcImage.origin.y + rcImage.size.height + labelTopMargin, containerSize.width - labelLeftMargin * 2, containerSize.height - labelTopMargin * 2 - rcImage.origin.y - rcImage.size.height);

	_lblMessage = [[UILabel alloc] initWithFrame:rcLabel];
	[_lblMessage setBackgroundColor:[UIColor clearColor]];
	[_lblMessage setFont:[UIFont systemFontOfSize:17]];
	[_lblMessage setTextColor:[UIColor colorWithRed:241/255.0 green:188/255.0 blue:102/255.0 alpha:1]];
	[_lblMessage setTextAlignment:NSTextAlignmentCenter];
	[_lblMessage setNumberOfLines:0];

	[_containerView addSubview:_lblMessage];


	_btnDismiss = [UIButton buttonWithType:UIButtonTypeCustom];
	[_btnDismiss setBackgroundColor:[UIColor clearColor]];
	_btnDismiss.frame = CGRectMake(0, 0, self.bounds.size.width, self.bounds.size.height);
	[_btnDismiss addTarget:self action:@selector(onClickDismiss:) forControlEvents:UIControlEventTouchUpInside];
	[self addSubview:_btnDismiss];
}


- (void)setSuccess:(BOOL)success message:(NSString*)message
{
	NSString* imgName = @"";
	if (success) {
		imgName = @"main_icon_smile.png";
	} else {
		imgName = @"main_icon_cry.png";
	}

	[_imgEmoji setImage:[UIImage imageNamed:imgName]];
	[_lblMessage setText:message];
}


- (void)onClickDismiss:(id)sender {
	[self dismissWithDelay:0];
}

- (void)dismissWithDelay:(int)delay_seconds {
	UIView* superView = self.superview;

	if (superView == nil) {
		return;
	}

	[self performSelector:@selector(removeFromSuperview) withObject:nil afterDelay:delay_seconds];
}


+ (UIView*)showInController:(UIViewController*)controller success:(BOOL)success message:(NSString*)message
{
	ResultDlgView* view = [[ResultDlgView alloc] initWithFrame:controller.view.bounds];
	[view setSuccess:success message:message];
	[controller.view addSubview:view];
	return view;
}


@end
