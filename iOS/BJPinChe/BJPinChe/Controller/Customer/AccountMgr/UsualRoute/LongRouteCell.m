//
//  LongRouteCell.m
//  BJPinChe
//
//  Created by YunSI on 11/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "LongRouteCell.h"
#import "Cus_LongRouteViewController.h"

@implementation LongRouteCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
	self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
	if (self) {
		// Initialization code
	}
	return self;
}

- (void)awakeFromNib
{
	// Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
	[super setSelected:selected animated:animated];
	
	// Configure the view for the selected state
}

- (void) initData : (STRouteInfo *)info parent:(id)parent
{
	mDataInfo = info;
	mParent = parent;
	
	[_lblStartPos setText:info.startcity];
	[_lblEndPos setText:info.endcity];
	[_lblTime setText:info.time];
		
	// initialize RMSipeTableViewCell (basic initialize)
	self.backViewbackgroundColor = [UIColor whiteColor];
	self.revealDirection = RMSwipeTableViewCellRevealDirectionRight;
	self.animationType = RMSwipeTableViewCellAnimationTypeEaseOut;
	self.panElasticityStartingPoint = BUTTON_THRESHOLD;

	_contentView.layer.borderColor = [UIColor grayColor].CGColor;
	_contentView.layer.borderWidth = 1;


	UILongPressGestureRecognizer* longPressRecog = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longClickItem:)];
	[_btnItem addGestureRecognizer:longPressRecog];
}


- (void)longClickItem:(UILongPressGestureRecognizer*)sender {
	if (sender.state == UIGestureRecognizerStateBegan) {
		UIAlertView* alertView = [[UIAlertView alloc] initWithTitle:@"" message:@"是否确定删除路线？" delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
		[alertView show];
	}
}


- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
	if (buttonIndex == 1) {
		[self deleteAction];
	}
}


//////////////////////////////////////////////////////////////////////
-(UIButton*)deleteButton {
	if (!_deleteButton) {
		_deleteButton = [UIButton buttonWithType:UIButtonTypeCustom];
		[_deleteButton setBackgroundImage:[UIImage imageNamed:@"DeleteButtonBackground"] forState:UIControlStateNormal];
		[_deleteButton setBackgroundImage:[UIImage imageNamed:@"DeleteButtonBackground"] forState:UIControlStateHighlighted];
		[_deleteButton.titleLabel setFont:[UIFont fontWithName:@"HelveticaNeue-Light" size:18]];
		[_deleteButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
		[_deleteButton setFrame:CGRectMake(CGRectGetMaxX(self.frame) - BUTTON_THRESHOLD, 0, BUTTON_THRESHOLD, CGRectGetHeight(self.contentView.frame))];
		[_deleteButton addTarget:self action:@selector(deleteAction) forControlEvents:UIControlEventTouchUpInside];
		[_deleteButton setAutoresizingMask:UIViewAutoresizingFlexibleHeight];
	}
	return _deleteButton;
}

-(void)didStartSwiping {
	[super didStartSwiping];
//	[self.backView addSubview:self.deleteButton];
}

-(void)deleteAction {
	if (self.delDelegate != nil && [self.delDelegate respondsToSelector:@selector(swipeTableViewCellDidDelete:)]) {
		[self.delDelegate swipeTableViewCellDidDelete:self];
	}
}



-(void)resetContentView {
	[UIView animateWithDuration:0.15f
					 animations:^{
						 self.contentView.frame = CGRectOffset(self.contentView.bounds, 0, 0);
					 }
					 completion:^(BOOL finished) {
						 self.shouldAnimateCellReset = YES;
						 [self cleanupBackView];
					 }];
}

-(void)cleanupBackView {
	[super cleanupBackView];
	[_deleteButton removeFromSuperview];
	_deleteButton = nil;
}

- (IBAction)onClickedItem:(id)sender
{
	Cus_LongRouteViewController *parentVC = (Cus_LongRouteViewController *)mParent;
	Cus_SetRouteViewController *setRouteVC = (Cus_SetRouteViewController *)[parentVC.storyboard instantiateViewControllerWithIdentifier:@"Cus_SetRouteViewController"];
	setRouteVC.mSaveType = 1;
	setRouteVC.mDataInfo = mDataInfo;
	[parentVC.navigationController pushViewController:setRouteVC animated:YES];
}

@end



