//
//  DriverEvalTableViewCell.m
//  BJPinChe
//
//  Created by CKK on 14-9-11.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "DriverEvalTableViewCell.h"

@implementation DriverEvalTableViewCell

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

-(void)layoutSubviews
{
    self.passName.text = self.model.pass_name;
    self.eval.text = self.model.eval_desc;
    self.time.text = self.model.time;
    self.content.text = self.model.contents;
}

@end
