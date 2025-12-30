//
//  NewItemTableViewCell.h
//  BodyWear
//
//  Created by RyuCJ on 8/28/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//

#import "XIBTableViewCell.h"

@protocol Cus_NewsCellDelegate;

@interface Cus_NewsTableViewCell : XIBTableViewCell {
    IBOutlet UILabel *          _lblTitle;
    IBOutlet UILabel *          _lblContent;
    IBOutlet UILabel *          _lblTime;
    
    NSString    *identifier;
    id                          _parent;
    
    STNewsInfo *      mDataInfo;
}

@property(strong, nonatomic) id<Cus_NewsCellDelegate> delegate;


- (void) initWithData:(id)data reuseIdentifier:(NSString *)reuseIdentifier parent:(id)parent;
- (NSString *)reuseIdentifier;

- (IBAction)onClickedItem:(id)sender;

@end


// protocol
@protocol Cus_NewsCellDelegate <NSObject>

@optional
- (void) cellClickedDelegate:(long)uid;

@end
