//
//  NewItemTableViewCell.h
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "XIBTableViewCell.h"

@interface AccumHistoryTableViewCell : XIBTableViewCell {
    
    IBOutlet UIImageView *          _imgBackground;
    IBOutlet UILabel *              _lblOrderNum;
    IBOutlet UILabel *              _lblUsedAccum;
    IBOutlet UILabel *              _lblOpType;
    IBOutlet UILabel *              _lblLeftAccum;
    
    NSString    *identifier;
    id                          _parent;
    
    STAccumHisInfo *      mDataInfo;
}


- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent;
- (NSString *)reuseIdentifier;


@end
