//
//  AddrSelTableViewCell_new.h
//  BJPinChe
//
//  Created by APP_USER on 14-9-29.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
@class BaiduSuggestionAdr;
@protocol cellDelegate <NSObject>
@optional
-(void)btnCellClick;
@end
@interface AddrSelTableViewCell_new : UITableViewCell

@property(nonatomic, assign) id<cellDelegate> delegate;
@property(nonatomic,strong)BaiduSuggestionAdr *mDataInfo;
@property(nonatomic,weak)UILabel *lblPosName;
@property(nonatomic,weak)UILabel *lblPosAddress;
@property(nonatomic, strong) UIButton *deleteBtn;
@property(nonatomic, strong) NSString *btnHidden;
@end