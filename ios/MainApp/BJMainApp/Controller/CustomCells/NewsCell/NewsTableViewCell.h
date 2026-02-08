//
//  NewItemTableViewCell.h
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "XIBTableViewCell.h"

@protocol NewsCellDelegate;

@interface NewsTableViewCell : XIBTableViewCell {
	IBOutlet UILabel *			_lblTitle;
	IBOutlet UILabel *			_lblContent;
	IBOutlet UILabel *			_lblTime;
	IBOutlet UIImageView*		_imgNewBadge;

	NSString					*identifier;
	id							_parent;

	STNewsInfo*					mDataInfo;
}

@property(strong, nonatomic) id<NewsCellDelegate> delegate;


- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent;
- (NSString *)reuseIdentifier;

- (IBAction)onClickedItem:(id)sender;
- (void) hasRead;

@end


// protocol
@protocol NewsCellDelegate <NSObject>

@optional
- (void) cellClickedDelegate:(long)uid withType:(int)news_type;

@end
