//
//  AddrSelTableViewCell.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AddrSelTableViewCell : UITableViewCell
{
    IBOutlet UILabel *                  _lblPosName;
    IBOutlet UILabel *                  _lblPosAddress;
    
    STBaiduAddrInfo *                   mDataInfo;
}

- (void) initData : (STBaiduAddrInfo *)dataInfo;

@end
