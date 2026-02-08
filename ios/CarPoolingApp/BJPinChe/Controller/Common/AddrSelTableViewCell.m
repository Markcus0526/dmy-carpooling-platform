//
//  AddrSelTableViewCell.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "AddrSelTableViewCell.h"

@implementation AddrSelTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        
    }
    return self;
}

- (void)awakeFromNib
{
    // Initialization code
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) initData:(STBaiduAddrInfo *)dataInfo
{
    mDataInfo = dataInfo;
    
    [_lblPosName setText:mDataInfo.name];
    [_lblPosAddress setText:mDataInfo.address];
}

@end
