//
//  UsualRouteCell.m
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "UsualRouteCell.h"


#define STR_DAYTYPE_NORMAL              @"工作日"
#define STR_DAYTYPE_WEEKEND             @"周末"
#define STR_DAYTYPE_EVERYDAY            @"每日"


@implementation UsualRouteCell

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

- (void) initData : (STRouteInfo *)info
{
	_contentView.layer.borderColor = [UIColor darkGrayColor].CGColor;
	_contentView.layer.borderWidth = 1;

	UILongPressGestureRecognizer* longPressRecog = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(deleteAction)];
	[_btnItem addGestureRecognizer:longPressRecog];

	mDataInfo = info;

	[_lblStartPos setText:info.startaddr];
    [_lblEndPos setText:info.endaddr];
    [_lblTime setText:info.time];

	if (info.daytype == TYPE_NORMAL) {
        [_lblType setText:STR_DAYTYPE_NORMAL];
    } else if (info.daytype == TYPE_WEEKEND) {
        [_lblType setText:STR_DAYTYPE_WEEKEND];
    } else {
        [_lblType setText:STR_DAYTYPE_EVERYDAY];
    }
    
    
    // initialize RMSipeTableViewCell (basic initialize)
    self.backViewbackgroundColor = [UIColor whiteColor];
    self.revealDirection = RMSwipeTableViewCellRevealDirectionRight;
    self.animationType = RMSwipeTableViewCellAnimationTypeEaseOut;
    self.panElasticityStartingPoint = BUTTON_THRESHOLD;

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
    [self.backView addSubview:self.deleteButton];
}

-(void)deleteAction {
    if ([self.delDelegate respondsToSelector:@selector(swipeTableViewCellDidDelete:)]) {
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



@end
