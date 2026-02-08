//
//  UsualRouteCell.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/25/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RMSwipeTableViewCell.h"

@protocol RMSwipeTableViewCellDelDelegate;

#define BUTTON_THRESHOLD 120

@interface UsualRouteCell : RMSwipeTableViewCell
{
	IBOutlet UILabel *          _lblStartPos;
	IBOutlet UILabel *          _lblEndPos;
	IBOutlet UILabel *          _lblType;
	IBOutlet UILabel *          _lblTime;
	IBOutlet UIView *			_contentView;
	IBOutlet UIButton *			_btnItem;

	STRouteInfo *               mDataInfo;
}


@property (nonatomic, strong) UIButton *deleteButton;
@property (nonatomic, assign) id <RMSwipeTableViewCellDelDelegate> delDelegate;


- (void)resetContentView;

- (void) initData : (STRouteInfo *)data;

@end


@protocol RMSwipeTableViewCellDelDelegate <NSObject>
@optional
- (void)swipeTableViewCellDidDelete:(RMSwipeTableViewCell *)swipeTableViewCell;
@end
