//
//  STDataInfo.h
//  FourService
//
//  Created by RyuCJ on 24/11/2012.
//  Copyright (c) 2012 . All rights reserved.
//

#import <Foundation/Foundation.h>

#define  SEX_MALE           0
#define  SEX_FEMALE         1

#define  STR_SEX_MALE       @"0"
#define  STR_SEX_FEMALE     @"1"

#define  EVAL_HIGH          2
#define  EVAL_MEDIUM        1
#define  EVAL_LOW           0


enum MAIN_TAB_ID
{
    TAB_APPS = 0,
    TAB_NEWS,
    TAB_ACCOUNT,
    TAB_SHARE
};

enum VERIFY_STATE
{
    STATE_NOTVERIFIED = 0,
    STATE_VERIFIED,
    STATE_WATING
};

/********************************* App Data Info ********************************/
@interface STAppInfo : NSObject {
    long            uid;
    NSString *      appName;
    NSString *      appIcon;
    NSString *      appScheme;
    NSString *      appUrl;
    NSString *      code;
    NSString *      latestVer;
    NSString *      curVer;
    NSString *     packname;
}

@property (nonatomic, readwrite) long   uid;
@property (nonatomic, retain) NSString * appName;
@property (nonatomic, retain) NSString * appIcon;
@property (nonatomic, retain) NSString * appScheme;
@property (nonatomic, retain) NSString * appUrl;
@property (nonatomic, retain) NSString * code;
@property (nonatomic, retain) NSString * latestVer;
@property (nonatomic, retain) NSString * curVer;
@property(nonatomic,copy)NSString *packname;

@end



/********************************* App Data Info ********************************/

#pragma mark News type constants

#define		NEWSTYPE_ANNOUNCEMENT			0
#define		NEWSTYPE_ORDERNOTIFY			1
#define		NEWSTYPE_PERSONNOTIFY			2


@interface STNewsInfo : NSObject {
    long            uid;
    NSString *      title;
    NSString *      contents;
    NSString *      time;
    long            couponid;
	int				hasread;
	long			orderid;
	int				ordertype;
	int				state;
	int				user_role;
	int				news_type;			// News type. 0:announcement   1:order information   2:personal notification
}

@property (nonatomic, readwrite) long   uid;
@property (nonatomic, retain) NSString * title;
@property (nonatomic, retain) NSString * contents;
@property (nonatomic, retain) NSString * time;
@property (nonatomic, readwrite) long   couponid;
@property (atomic, readwrite) int hasread;
@property (atomic, readwrite) long orderid;
@property (atomic, readwrite) int ordertype;
@property (atomic, readwrite) int state;
@property (atomic, readwrite) int user_role;
@property (atomic, readwrite) int news_type;

@end

/********************************* LvDian History Info ********************************/
@interface STAccumHisInfo : NSObject {
    long            uid;
    NSString *      orderNo;
    double          usedAccum;
    NSInteger       opFlag;
    NSString *      opType;
    double          leftAccum;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    orderNo;
@property (nonatomic, readwrite) double     usedAccum;
@property (nonatomic, readwrite) NSInteger  opFlag;
@property (nonatomic, retain) NSString *    opType;
@property (nonatomic, readwrite) double     leftAccum;

@end
/**
 *  注册数据模型
 */
@interface STRegUserInfo : NSObject {
    NSString *      username;
    NSString *      mobile;
    NSString *      nickname;
    NSString *      password;
    NSString *      invitecode;
    NSString *      sex;
    NSString *      city;
}

@property (nonatomic, retain) NSString *    username;
@property (nonatomic, retain) NSString *    mobile;
@property (nonatomic, retain) NSString *    nickname;
@property (nonatomic, retain) NSString *    password;
@property (nonatomic, retain) NSString *    invitecode;
@property (nonatomic, retain) NSString *    sex;
@property (nonatomic, retain) NSString *    city;

@end

/**
 *
 */
@interface STUserInfo : NSObject {
    long            userid;
    NSString *      photo;
    NSString *      mobile;
    NSString *      nickname;
    int             sex;
    NSString *      birthday;
    int             person_verified;
    int             driver_verified;
    NSString *      carimg;
    NSString *      invitecode;
}

@property (nonatomic, readwrite) long       userid;
@property (nonatomic, retain) NSString *    photo;
@property (nonatomic, retain) NSString *    mobile;
@property (nonatomic, retain) NSString *    nickname;
@property (nonatomic, retain) NSString *    birthday;
@property (nonatomic, readwrite) int        sex;
@property (nonatomic, readwrite) int        person_verified;
@property (nonatomic, readwrite) int        driver_verified;
@property (nonatomic, retain) NSString *    carimg;
@property (nonatomic, retain) NSString *    invitecode;

@end


/********************************* Bind Account Info ********************************/
@interface STBindAccount : NSObject {
    NSString *      realname;
    NSString *      bankcard;
    NSString *      bankname;
    NSString *      subbranch;
}

@property (nonatomic, retain) NSString *    realname;
@property (nonatomic, retain) NSString *    bankcard;
@property (nonatomic, retain) NSString *    bankname;
@property (nonatomic, retain) NSString *    subbranch;

@end


/********************************* Coupon Info ********************************/
@interface STCouponInfo : NSObject {
    long            uid;
    NSString *      range;
    NSString *      contents;
    NSString *      usecond;
    NSString *      dateexp;
    NSString *      couponcode;
    NSString *      unitname;
    int             is_goods;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    range;
@property (nonatomic, retain) NSString *    contents;
@property (nonatomic, retain) NSString *    usecond;
@property (nonatomic, retain) NSString *    dateexp;
@property (nonatomic, retain) NSString *    couponcode;
@property (nonatomic, retain) NSString *    unitname;
@property (nonatomic, readwrite) int        is_goods;


@end


