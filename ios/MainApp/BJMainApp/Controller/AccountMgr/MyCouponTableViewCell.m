//
//  NewItemTableViewCell.m
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "MyCouponTableViewCell.h"

@implementation MyCouponTableViewCell

@synthesize delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        mDataInfo = nil;
    }
    return self;
}

/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect
 {
 // Drawing code
 }
 */

- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent dispno:(int)dispno
{
    _parent = parent;
    identifier = reuseIdentifier;
    
    if (data != nil)
    {
        mDataInfo = (STCouponInfo *)data;
		[_lblNum setText:[NSString stringWithFormat:@"%d", dispno]];
        [_lblType setText:mDataInfo.range];
        [_lblContent setText:mDataInfo.contents];
        [_lblRole setText:mDataInfo.usecond];
        [_lblTime setText:mDataInfo.dateexp];
        
    }
}

- (NSString *)reuseIdentifier
{
    return identifier;
}

//////////////////////////////////////////////////////////////////////////
#pragma mark - User Interaction

- (IBAction)onClickedItem:(id)sender
{
    [self.delegate cellClickedDelegate:mDataInfo.uid];
}


@end
