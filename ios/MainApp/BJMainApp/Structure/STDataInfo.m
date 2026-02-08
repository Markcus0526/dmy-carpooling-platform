//
//  STDataInfo.m
//  4S-C
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 PIC. All rights reserved.
//

#import "STDataInfo.h"


@implementation STAppInfo

@synthesize uid;
@synthesize appName;
@synthesize appIcon;
@synthesize appScheme;
@synthesize appUrl;
@synthesize latestVer;
@synthesize curVer;
@synthesize code;
@synthesize packname;

@end



@implementation STNewsInfo

@synthesize uid;
@synthesize title;
@synthesize contents;
@synthesize time;
@synthesize couponid;
@synthesize hasread;
@synthesize orderid;
@synthesize ordertype;
@synthesize state;
@synthesize user_role;
@synthesize news_type;

@end


@implementation STAccumHisInfo

@synthesize uid;
@synthesize orderNo;
@synthesize usedAccum;
@synthesize opFlag;
@synthesize opType;
@synthesize leftAccum;

@end


@implementation STRegUserInfo

@synthesize username;
@synthesize mobile;
@synthesize nickname;
@synthesize password;
@synthesize invitecode;
@synthesize sex;
@synthesize city;

@end


@implementation STUserInfo

@synthesize userid;
@synthesize photo;
@synthesize mobile;
@synthesize nickname;
@synthesize birthday;
@synthesize sex;
@synthesize person_verified;
@synthesize driver_verified;

@synthesize invitecode;

@end



@implementation STBindAccount

@synthesize realname;
@synthesize bankcard;
@synthesize bankname;
@synthesize subbranch;

@end


@implementation STCouponInfo

@synthesize uid;
@synthesize range;
@synthesize contents;
@synthesize usecond;
@synthesize dateexp;
@synthesize couponcode;
@synthesize unitname;
@synthesize is_goods;

@end



