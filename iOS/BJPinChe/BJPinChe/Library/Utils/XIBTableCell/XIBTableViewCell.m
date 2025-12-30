//
//  XIBTableViewCell.m
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "XIBTableViewCell.h"
#import "Common.h"

@implementation XIBTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

+ (XIBTableViewCell *)cellFromNibNamed:(NSString *)nibName {
    
	NSString *szXibName = nil;

	if ([Common phoneType] == IPAD)
		szXibName = [nibName stringByAppendingString:@"_ipad"];
	else
		szXibName = nibName;

    NSArray *nibContents = [[NSBundle mainBundle] loadNibNamed:szXibName owner:self options:NULL];

    NSEnumerator *nibEnumerator = [nibContents objectEnumerator];
    XIBTableViewCell *xibBasedCell = nil;
    NSObject* nibItem = nil;
    
    while ((nibItem = [nibEnumerator nextObject]) != nil) {
        if ([nibItem isKindOfClass:[XIBTableViewCell class]]) {
            xibBasedCell = (XIBTableViewCell *)nibItem;
            break; // we have a winner
        }
    }
    
    return xibBasedCell;
}

@end
