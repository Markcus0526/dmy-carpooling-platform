//
//  STDataInfo.m
//  4S-C
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 PIC. All rights reserved.
//

#import "STDataInfo.h"

@implementation STBaiduAddrInfo

@synthesize uid;
@synthesize name;
@synthesize lat;
@synthesize lng;
@synthesize detail_url;
@synthesize address;

@end

@implementation STAppInfo

@synthesize uid;
@synthesize appName;
@synthesize appIcon;
@synthesize appScheme;
@synthesize appUrl;
@synthesize latestVer;
@synthesize curVer;
@synthesize code;

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
@synthesize carimg;
@synthesize invitecode;

@end


@implementation STExeOrderInfo

@synthesize orderId;
@synthesize orderType;
@synthesize runTime;
@synthesize runDistance;

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


@implementation STOrderInfo

@synthesize sysinfo_fee;
@synthesize uid;
@synthesize order_num;
@synthesize startPos;
@synthesize endPos;
@synthesize name;
@synthesize customerNum;
@synthesize driver_id;
@synthesize pass_id;
@synthesize sex;
@synthesize age;
@synthesize image;
@synthesize price;
@synthesize price_desc;
@synthesize type;
@synthesize type_desc;
@synthesize start_time;
@synthesize contents;
@synthesize state;
@synthesize state_desc;
@synthesize days;
@synthesize evaluated;
@synthesize evaluated_desc;
@synthesize eval_content;
@synthesize create_time;
@synthesize start_city;
@synthesize end_city;

@end

@implementation STSingleTimeOrderInfo


@synthesize mileage;
@synthesize status;
@synthesize uid;
@synthesize order_num;
@synthesize startPos;
@synthesize start_lat;
@synthesize start_lng;
@synthesize endPos;
@synthesize end_lat;
@synthesize end_lng;
@synthesize pass_id;
@synthesize name;
@synthesize sex;
@synthesize age;
@synthesize image;
@synthesize price;
@synthesize sysinfo_fee;
@synthesize sysinfo_fee_desc;
@synthesize distance;
@synthesize distance_desc;
@synthesize midpoints;
@synthesize midpoints_desc;
@synthesize mid_points;
@synthesize start_time;
@synthesize create_time;
@synthesize password;

@end


@implementation STWorkTimeOrderInfo

@synthesize uid;
@synthesize order_num;
@synthesize startPos;
@synthesize endPos;
@synthesize pass_id;
@synthesize name;
@synthesize sex;
@synthesize age;
@synthesize image;
@synthesize price;
@synthesize sysinfo_fee;
@synthesize sysinfo_fee_desc;
@synthesize distance;
@synthesize distance_desc;
@synthesize midpoints;
@synthesize midpoints_desc;
@synthesize start_time;
@synthesize start_time_desc;
@synthesize days;
@synthesize create_time;
@synthesize password;

@end


@implementation STMidPoint

@synthesize index;
@synthesize latitude;
@synthesize longitude;
@synthesize address;

@end


@implementation STOrderStatistics

@synthesize total_count;
@synthesize total_count_desc;
@synthesize total_income;
@synthesize total_income_desc;
@synthesize evgood_count;
@synthesize evgood_count_desc;
@synthesize evnormal_count;
@synthesize evnormal_count_desc;
@synthesize evbad_count;
@synthesize evbad_count_desc;

@end


@implementation STDetailedDrvOrderInfo

@synthesize uid;
@synthesize order_num;
@synthesize pass_list;
@synthesize left_seat;
@synthesize price;
@synthesize sysinfo_fee;
@synthesize sysinfo_fee_desc;
@synthesize start_addr;
@synthesize end_addr;
@synthesize valid_days;
@synthesize mid_points;
@synthesize start_time;
@synthesize create_time;
@synthesize accept_time;
@synthesize state;
@synthesize state_desc;
@synthesize order_statistics;
@synthesize total_distance;

@end


@implementation STDetailedDrvInCityOrderInfo

@synthesize pass_info;
@synthesize start_addr;
@synthesize end_addr;
@synthesize start_time;
@synthesize mid_points;
@synthesize price;
@synthesize price_desc;
@synthesize sysinfo_fee;
@synthesize sysinfo_fee_desc;
@synthesize valid_days;

@end


@implementation STDetailedCusOrderInfo

@synthesize uid;
@synthesize order_num;
@synthesize driver_info;
@synthesize price;
@synthesize start_addr;
@synthesize end_addr;
@synthesize start_lat;
@synthesize start_lng;
@synthesize end_lat;
@synthesize end_lng;
@synthesize valid_days;
@synthesize left_days;
@synthesize mid_points;
@synthesize start_time;
@synthesize create_time;
@synthesize accept_time;
@synthesize startsrv_time;
@synthesize state;
@synthesize state_desc;
@synthesize password;
@synthesize cancelled_balance_desc;
@synthesize cancelled_balance;
@synthesize time_interval_desc;
@synthesize balance_desc;
@synthesize evaluated;
@synthesize eval_content;

@end


@implementation STPassengerInfo

@synthesize uid;
@synthesize name;
@synthesize image;
@synthesize sex;
@synthesize age;
@synthesize pverified;
@synthesize pverified_desc;
@synthesize goodeval_rate;
@synthesize goodeval_rate_desc;
@synthesize carpool_count;
@synthesize carpool_count_desc;
@synthesize state;
@synthesize state_desc;
@synthesize seat_count;
@synthesize seat_count_desc;
@synthesize phone;
@synthesize evaluated;
@synthesize evaluated_desc;
@synthesize eval_content;
@synthesize evals;

@end


@implementation STPassengerEvalInfo

@synthesize uid;
@synthesize driver_name;
@synthesize eval;
@synthesize eval_desc;
@synthesize time;
@synthesize contents;

@end


@implementation STPubOnceOrderRet

@synthesize prompt_1st;
@synthesize prompt;
@synthesize order_id;
@synthesize wait_time;
@synthesize driver_lock_time;
@synthesize add_price_time1;
@synthesize add_price_time2;
@synthesize add_price_time3;
@synthesize add_price_time4;
@synthesize add_price_time5;
@synthesize same_price_time1;
@synthesize same_price_time2;
@synthesize same_price_time3;
@synthesize same_price_time4;
@synthesize same_price_time5;
@synthesize drvcount;
@synthesize add_price_default;
@synthesize add_price_range;
@synthesize add_price_min;
@synthesize cut_price_range;
@end

@implementation STCarInfo

@synthesize carimg;
@synthesize brand;
@synthesize type;
@synthesize style;
@synthesize color;

@end


@implementation STCarTypeInfo

@synthesize uid;
@synthesize name;
@synthesize style;

@end


@implementation STCarBrandInfo

@synthesize uid;
@synthesize name;
@synthesize types;

@end


@implementation STCarColorInfo

@synthesize uid;
@synthesize name;
@synthesize code;

@end


@implementation STDriverInfo

@synthesize uid;
@synthesize image;
@synthesize name;
@synthesize sex;
@synthesize age;
@synthesize phone;
@synthesize carimg;
@synthesize drv_career;
@synthesize drv_career_desc;
@synthesize goodeval_rate;
@synthesize goodeval_rate_desc;
@synthesize carpool_count;
@synthesize carpool_count_desc;
@synthesize carno;
@synthesize carinfo;
@synthesize car_brand;
@synthesize car_style;
@synthesize car_type;
@synthesize evals;
@synthesize color;
@end


@implementation STOnceOrderPubData

@synthesize start_addr;
@synthesize start_lat;
@synthesize start_lng;
@synthesize end_addr;
@synthesize end_lat;
@synthesize end_lng;
@synthesize start_time;
@synthesize mid_points;
@synthesize remark;
@synthesize req_style;
@synthesize price;
@synthesize city;
@synthesize wait_time;

@end


@implementation STDriverEvalInfo

@synthesize uid;
@synthesize pass_name;
@synthesize eval;
@synthesize eval_desc;
@synthesize time;
@synthesize contents;

@end


@implementation STRouteInfo

@synthesize uid;
@synthesize startcity;
@synthesize endcity;
@synthesize startaddr;
@synthesize endaddr;
@synthesize startlat;
@synthesize startlng;
@synthesize endlat;
@synthesize endlng;
@synthesize time;
@synthesize daytype;

@end



@implementation STCarVerifyingInfo

@synthesize userid;
@synthesize driver_license_fore;
@synthesize driver_license_back;
@synthesize brand;
@synthesize type;
@synthesize color;
@synthesize carimg;
@synthesize driving_license_fore;
@synthesize driving_license_back;

@end


@implementation STAcceptableLongOrderInfo


@end

@implementation STNearbyDriverInfo

@synthesize driverid;
@synthesize latitude;
@synthesize longitude;

@end


@implementation STNearbyFalseDriverInfo


@synthesize driverid;
@synthesize latitude;
@synthesize longitude;

@end


@implementation STDriverInfoForAcceptableLongOrderDetailInfo



@end

@implementation STAcceptableLongOrderDetailInfo

- (instancetype)initWithDictionary:(NSDictionary *)dictionary
{
    self = [super init];
    if (self) {
        [self setValuesForKeysWithDictionary:dictionary];
        self.driver_info = [[STDriverInfoForAcceptableLongOrderDetailInfo alloc]init];
        [self.driver_info setValuesForKeysWithDictionary:dictionary[@"driver_info"]];
    }
    return self;
}

@end

@implementation STGrabLongOrderSuccessInfo
@end

@implementation STGrabLongOrderFailInfo


@end
@implementation InfoFeeCalcMethodInfo


@end
@implementation STdriverPosInfo


@end

