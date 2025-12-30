//
//  AddrSelTableViewCell_new.m
//  BJPinChe
//
//  Created by APP_USER on 14-9-29.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//新的自定义的cell 地址选择

#import "AddrSelTableViewCell_new.h"
#import "BaiduSuggestionAdr.h"
@interface AddrSelTableViewCell_new()
@property(nonatomic,weak)UIImageView *background;
@property(nonatomic,weak)UIImageView *icon_search;

@end

@implementation AddrSelTableViewCell_new

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
        self.backgroundColor =[UIColor clearColor];
        UIImageView *background =[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"txtback0.png"]];
        [self.contentView addSubview:background];
        self.background=background;
        UIImageView *icon_search =[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"bk_addr_item"]];
        [self.contentView addSubview:icon_search];
        self.icon_search =icon_search;
        UILabel *lblPosName =[[UILabel alloc]init];
        lblPosName.font =[UIFont systemFontOfSize:16];
        lblPosName.textColor =[UIColor lightGrayColor];
        [self.contentView addSubview:lblPosName];
        self.lblPosName =lblPosName;
        UILabel *lblPosAddress =[[UILabel alloc]init];
        lblPosAddress.font =[UIFont systemFontOfSize:11];
        lblPosAddress.textColor =[UIColor lightGrayColor];
        [self.contentView addSubview:lblPosAddress];
        self.lblPosAddress =lblPosAddress;
        
        self.deleteBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_deleteBtn setTitle:@"清除历史目的地" forState:UIControlStateNormal];
        [_deleteBtn setBackgroundColor:[UIColor clearColor]];
        [_deleteBtn setTitleColor:[UIColor colorWithRed:77/255.0 green:180/255.0 blue:125/255.0 alpha:1] forState:UIControlStateNormal];
        [_deleteBtn setBackgroundImage:[UIImage imageNamed:@"txtback0.png"] forState:UIControlStateNormal];
        [_deleteBtn addTarget:self action:@selector(cellClick:) forControlEvents:UIControlEventTouchUpInside];
        [self.contentView addSubview:_deleteBtn];
        
        if (![_btnHidden isEqualToString:@"1"]) {
            _deleteBtn.hidden = YES;
        }else
        {
            [_deleteBtn setHidden:NO];
        }
        
        [self setupFrame];
    }
    return self;
}
-(void)cellClick:(id)sender
{
    [_delegate btnCellClick];
}
-(void)setupFrame
{
    CGRect background =CGRectMake(0, 0, 280, 50);
    self.background.frame =background;

    CGRect icon_sear =CGRectMake(11, 11, 23, 23);
    self.icon_search.frame =icon_sear;
    CGRect lblPosName =CGRectMake(53, 8, 214, 21);
    self.lblPosName.frame =lblPosName;
    
    CGRect lblPosAddress =CGRectMake(53, 25, 214, 21);
    self.lblPosAddress.frame =lblPosAddress;
    
    CGRect btn = CGRectMake(0, 0, 280, 50);
    _deleteBtn.frame = btn;
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
- (void)setMDataInfo:(BaiduSuggestionAdr *)dataInfo
{
    _mDataInfo = dataInfo;
    
    if ([_btnHidden isEqualToString:@"1"]) {
        [_deleteBtn setHidden:NO];
        return;
    }else
        [_deleteBtn setHidden:YES];
    
    self.lblPosName.text =self.mDataInfo.name;
    self.lblPosAddress.text =[NSString stringWithFormat:@"%@%@",dataInfo.city,dataInfo.district];
    
}
@end
