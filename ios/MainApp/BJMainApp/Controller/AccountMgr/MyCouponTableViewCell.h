//
//  NewItemTableViewCell.h
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "XIBTableViewCell.h"

@protocol CouponCellDelegate;


@interface MyCouponTableViewCell : XIBTableViewCell {
    
    IBOutlet UIImageView *          _imgBackground;
    IBOutlet UILabel *              _lblNum;
    IBOutlet UILabel *              _lblType;
    IBOutlet UILabel *              _lblContent;
    IBOutlet UILabel *              _lblRole;
    IBOutlet UILabel *              _lblTime;
    
    NSString    *                   identifier;
    id                              _parent;
    
    STCouponInfo *                  mDataInfo;
}

@property(strong, nonatomic) id<CouponCellDelegate> delegate;

- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent dispno:(int)dispno;
- (NSString *)reuseIdentifier;

- (IBAction)onClickedItem:(id)sender;

@end


// protocol
@protocol CouponCellDelegate <NSObject>

@optional
- (void) cellClickedDelegate:(long)uid;

@end