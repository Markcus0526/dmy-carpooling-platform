//
//  NewItemTableViewCell.m
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "Cus_AccumHistoryTableViewCell.h"
#import "Common.h"

@implementation Cus_AccumHistoryTableViewCell

#define ACCUM_PLUS          1
#define ACCUM_MINUS         2

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

- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent
{
    _parent = parent;
    identifier = reuseIdentifier;
    
    if (data != nil)
    {
        mDataInfo = (STAccumHisInfo *)data;
        
        // set accum history data to ui control
        [_lblOrderNum setText:mDataInfo.tstime];//以前是交易号 现在改成交易时间
    
        // check operating flag ( for +/- )
        if (mDataInfo.usedAccum > 0) {
            [_lblUsedAccum setText:[NSString stringWithFormat:@"+%.02f", mDataInfo.usedAccum]];
        } else {
            [_lblUsedAccum setText:[NSString stringWithFormat:@"%.02f", mDataInfo.usedAccum]];
        }
        
        [_lblOpType setText:mDataInfo.opType];
        [_lblLeftAccum setText:[NSString stringWithFormat:@"%.02f", mDataInfo.leftAccum]];
    }
}

- (NSString *)reuseIdentifier
{
    return identifier;
}

//////////////////////////////////////////////////////////////////////////
#pragma mark - User Interaction




@end
