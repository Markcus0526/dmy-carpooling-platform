//
//  LongRouteCell.h
//  BJPinChe
//
//  Created by YunSI on 11/26/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RMSwipeTableViewCell.h"
#import "Cus_SetRouteViewController.h"

@protocol RMSwipeTableViewCellDelDelegate;

#define BUTTON_THRESHOLD 120

@interface LongRouteCell : RMSwipeTableViewCell<UIAlertViewDelegate>
{
	IBOutlet UILabel *          _lblStartPos;
	IBOutlet UILabel *          _lblEndPos;
	IBOutlet UILabel *          _lblTime;
	IBOutlet UIView *			_contentView;
	IBOutlet UIButton *			_btnItem;

	STRouteInfo *               mDataInfo;
	id							mParent;
}


@property (nonatomic, strong) UIButton *deleteButton;
@property (nonatomic, assign) id <RMSwipeTableViewCellDelDelegate> delDelegate;


- (void)resetContentView;
- (void) initData : (STRouteInfo *)data parent:(id)parent;

- (IBAction)onClickedItem:(id)sender;

@end


@protocol RMSwipeTableViewCellDelDelegate <NSObject>
@optional
- (void)swipeTableViewCellDidDelete:(RMSwipeTableViewCell *)swipeTableViewCell;
@end