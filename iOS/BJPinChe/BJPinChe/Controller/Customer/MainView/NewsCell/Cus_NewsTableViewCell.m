//
//  NewItemTableViewCell.m
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "Cus_NewsTableViewCell.h"
#import "Common.h"

@implementation Cus_NewsTableViewCell

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
 // Only override drawRect: if you perform Custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect
 {
 // Drawing code
 }
 */

- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent
{
    _parent = parent;
    identifier = reuseIdentifier;
    
    if (data != nil)
    {
        mDataInfo = (STNewsInfo *)data;
        [_lblTitle setText:mDataInfo.title];
        [_lblContent setText:mDataInfo.contents];
        [_lblTime setText:mDataInfo.time];
        
        [_lblContent sizeToFit];
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
    if (mDataInfo.couponid > 0) {
        [self.delegate cellClickedDelegate:mDataInfo.couponid];
    }
}

@end
