//
//  STDataInfo.h
//  FourService
//
//  Created by RyuCJ on 24/11/2012.
//  Copyright (c) 2012 . All rights reserved.
// 所有的数据模型全在这一个文件中

#import <Foundation/Foundation.h>

#define  SEX_MALE           0
#define  SEX_FEMALE         1

#define  SEX_MALE_C           0
#define  SEX_FEMALE_C         1

#define  ROUTETYPE_LONG     1
#define  ROUTETYPE_SHORT    2

// tab id of main view controller
enum MAIN_TAB_ID
{
    TAB_APPS = 0,
    TAB_ORDER,
    TAB_ACCOUNT,
    TAB_SHARE
};

enum EVAL_STATE
{
    EVAL_NOT = 0,
    EVAL_HIGH,
    EVAL_MEDIUM,
    EVAL_LOW
};

// verifyed state
enum VERIFY_STATE
{
    STATE_NOTVERIFIED = 0,
    STATE_VERIFIED,
    STATE_WATING
};

// day type of usual route
enum ROUTE_DAYTYPE
{
    TYPE_NORMAL = 1,
    TYPE_WEEKEND,
    TYPE_EVERYDAY
};

enum ORDER_TYPE
{
    TYPE_SINGLE_ORDER = 1,
    TYPE_WORK_ORDER,
    TYPE_LONG_ORDER
};

// passenger state of order detail
enum PASSENGER_STATE
{
    PASS_STATE_UPLOAD = 1,
    PASS_STATE_NOT_CHECKED,
    PASS_STATE_GIVEUP,
    PASS_STATE_WEIJIESUAN,
    PASS_STATE_PAYED,//已结算
    PASS_STATE_WEIPINGJIA
};

enum ORDER_STATE
{
    ORDER_STATE_DRIVER_ACCEPTED = 0,//接单
    ORDER_STATE_PUBLISHED,//发布
    ORDER_STATE_GRABBED,//成交/待执行
    ORDER_STATE_STARTED,//开始执行
    ORDER_STATE_DRIVER_ARRIVED,//车主到达
    ORDER_STATE_PASSENGER_GETON,// 乘客上车
    ORDER_STATE_FINISHED,//执行结束
    ORDER_STATE_PAYED,//结算完成/未评价
    ORDER_STATE_EVALUATED,//已评价
    ORDER_STATE_CLOSED,//已关闭
    ORDER_STATE_Ticket_REFUND,//退票
//    ORDER_STATE_REFUND//已退票
};

/********************************* Baidu Address Info ********************************/
#pragma mark 百度地址返回模型
@interface STBaiduAddrInfo : NSObject {
    NSString *      uid;
    NSString *      name;
    double          lat;
    double          lng;
    NSString *      detail_url;
    NSString *      address;
}

@property (nonatomic, retain) NSString * uid;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, readwrite) double  lat;
@property (nonatomic, readwrite) double  lng;
@property (nonatomic, retain) NSString * detail_url;
@property (nonatomic, retain) NSString * address;

@end

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
}

@property (nonatomic, readwrite) long   uid;
@property (nonatomic, retain) NSString * appName;
@property (nonatomic, retain) NSString * appIcon;
@property (nonatomic, retain) NSString * appScheme;
@property (nonatomic, retain) NSString * appUrl;
@property (nonatomic, retain) NSString * code;
@property (nonatomic, retain) NSString * latestVer;
@property (nonatomic, retain) NSString * curVer;

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
    NSString  *tstime;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    orderNo;
@property (nonatomic, readwrite) double     usedAccum;
@property (nonatomic, readwrite) NSInteger  opFlag;
@property (nonatomic, retain) NSString *    opType;
@property (nonatomic, readwrite) double     leftAccum;
@property (nonatomic,copy)NSString *tstime;

@end

/********************************* Register User Info ********************************/
@interface STRegUserInfo : NSObject {
    NSString *      username;
    NSString *      mobile;
    NSString *      nickname;
    NSString *      password;
    NSString *      invitecode;
    NSInteger            sex;
    NSString *      city;
}

@property (nonatomic, retain) NSString *    username;
@property (nonatomic, retain) NSString *    mobile;
@property (nonatomic, retain) NSString *    nickname;
@property (nonatomic, retain) NSString *    password;
@property (nonatomic, retain) NSString *    invitecode;
@property (nonatomic, assign) NSInteger        sex;
@property (nonatomic, retain) NSString *    city;

@end

/********************************* User Info ********************************/
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


/********************************* Executing Order Info ********************************/
@interface STExeOrderInfo : NSObject {
    long            orderId;
    enum ORDER_TYPE      orderType;
    int             runTime;
    double          runDistance;
}

@property (nonatomic, readwrite) long       orderId;
@property (nonatomic, readwrite) enum ORDER_TYPE orderType;
@property (nonatomic, readwrite) int        runTime;
@property (nonatomic, readwrite) double     runDistance;

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



/********************************* Order Info ********************************/
@interface STOrderInfo : NSObject {
    long            uid;
    NSString *      order_num;
    NSString *      startPos;
    NSString *      endPos;
    NSString *      name;
    NSInteger       customerNum;
	NSInteger 		driver_id;
    NSInteger       pass_id;
    NSInteger       sex;
    NSInteger       age;
    NSString *      image;
    double          price;
    NSString *      price_desc;
    
    double          sysinfo_fee;

	enum ORDER_TYPE      type;
    NSString *      type_desc;
    NSString *      start_time;
    NSString *      contents;
    NSString *      days;
    NSInteger       state;
    NSString *      state_desc;
    NSInteger       evaluated;
    NSString *      evaluated_desc;
    NSString *      eval_content;
    NSString *      create_time;
    NSString *      start_city;
    NSString *      end_city;
}
@property (nonatomic, readwrite) double     sysinfo_fee;
@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    order_num;
@property (nonatomic, retain) NSString *    startPos;
@property (nonatomic, retain) NSString *    endPos;
@property (nonatomic, retain) NSString *    name;
@property (nonatomic, readwrite) NSInteger  customerNum;
@property (nonatomic, readwrite) NSInteger	driver_id;
@property (nonatomic, readwrite) NSInteger  pass_id;
@property (nonatomic, readwrite) NSInteger  sex;
@property (nonatomic, readwrite) NSInteger     age;
@property (nonatomic, retain) NSString *    image;
@property (nonatomic, readwrite) double     price;
@property (nonatomic, retain) NSString *    price_desc;
@property (nonatomic, assign) double        insun_fee;
@property (nonatomic, readwrite) enum ORDER_TYPE  type;
@property (nonatomic, retain) NSString *    type_desc;
@property (nonatomic, retain) NSString *    start_time;
@property (nonatomic, retain) NSString *    contents;
@property (nonatomic, readwrite) NSInteger  state;
@property (nonatomic, retain) NSString *    days;
@property (nonatomic, readwrite) NSInteger  evaluated;
@property (nonatomic, retain) NSString *    evaluated_desc;
@property (nonatomic, retain) NSString *    eval_content;
@property (nonatomic, retain) NSString *    state_desc;
@property (nonatomic, retain) NSString *    create_time;
@property (nonatomic, retain) NSString *    start_city;
@property (nonatomic, retain) NSString *    end_city;

@end


/********************************* Single Time Order Info ********************************/
@interface STSingleTimeOrderInfo : NSObject {
    long            uid;
    NSString *      order_num;
    NSString *      startPos;
	double			start_lat;
	double			start_lng;
    NSString *      endPos;
	double			end_lat;
	double			end_lng;
    long            pass_id;
    NSString *      image;
    NSString *      name;
    NSInteger       sex;
    NSInteger       age;
    double          price;
    double          sysinfo_fee;
    NSString *      sysinfo_fee_desc;
    double          distance;
    NSString *      distance_desc;
    int             midpoints;
    NSString *      midpoints_desc;
	NSMutableArray * mid_points;
    NSString *      start_time;
    NSString *      create_time;
	NSString *		password;
    NSString *      status;
    double          mileage;
}

@property (nonatomic, readwrite) double     mileage;
@property (nonatomic, retain) NSString *    status;
@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    order_num;
@property (nonatomic, retain) NSString *    startPos;
@property (nonatomic, readwrite) double		start_lat;
@property (nonatomic, readwrite) double		start_lng;
@property (nonatomic, retain) NSString *    endPos;
@property (nonatomic, readwrite) double		end_lat;
@property (nonatomic, readwrite) double		end_lng;
@property (nonatomic, readwrite) long       pass_id;
@property (nonatomic, retain) NSString *    name;
@property (nonatomic, readwrite) NSInteger  sex;
@property (nonatomic, readwrite) NSInteger  age;
@property (nonatomic, retain) NSString *    image;
@property (nonatomic, readwrite) double     price;
@property (nonatomic, readwrite) double     sysinfo_fee;
@property (nonatomic, retain) NSString *    sysinfo_fee_desc;
@property(nonatomic,assign) double  insun_fee;
@property (nonatomic, readwrite) double     distance;
@property (nonatomic, retain) NSString *    distance_desc;
@property (nonatomic, readwrite) int        midpoints;
@property (nonatomic, retain) NSString *    midpoints_desc;
@property (nonatomic, retain) NSMutableArray * mid_points;
@property (nonatomic, retain) NSString *    start_time;
@property (nonatomic, retain) NSString *    create_time;
@property (nonatomic, retain) NSString *	password;

@end


/********************************* Work Time Order Info ********************************/
@interface STWorkTimeOrderInfo : NSObject {
    long            uid;
    NSString *      order_num;
    NSString *      startPos;
    NSString *      endPos;
    long            pass_id;
    NSString *      image;
    NSString *      name;
    NSInteger       sex;
    NSInteger       age;
    double          price;
    double          sysinfo_fee;
    NSString *      sysinfo_fee_desc;
    double          distance;
    NSString *      distance_desc;
    int             midpoints;
    NSString *      midpoints_desc;
    NSString *      start_time;
    NSString *      start_time_desc;
    NSString *      days;
    NSString *      create_time;
	NSString *		password;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, copy) NSString *    order_num;
@property (nonatomic, copy) NSString *    startPos;
@property (nonatomic, copy) NSString *    endPos;
@property (nonatomic, assign) long       pass_id;
@property (nonatomic, copy) NSString *    name;
@property (nonatomic, assign) NSInteger  sex;
@property (nonatomic, assign) NSInteger  age;
@property (nonatomic, copy) NSString *    image;
@property (nonatomic, assign) double     price;
@property (nonatomic, assign) double     sysinfo_fee;
@property (nonatomic, copy) NSString *    sysinfo_fee_desc;
@property (nonatomic, assign) double     distance;
@property (nonatomic, copy) NSString *    distance_desc;
@property (nonatomic, assign) int        midpoints;
@property (nonatomic, copy) NSString *    midpoints_desc;
@property (nonatomic, copy) NSString *    start_time;
@property (nonatomic, copy) NSString *    start_time_desc;
@property (nonatomic, copy) NSString *    days;
@property (nonatomic, copy) NSString *    create_time;
@property (nonatomic, copy) NSString *	password;


@end


/********************************* Mid Point Info ********************************/
@interface STMidPoint : NSObject {
    NSInteger       index;
    double          latitude;
    double          longitude;
    NSString *      address;
}

@property (nonatomic, readwrite) NSInteger      index;
@property (nonatomic, readwrite) double         latitude;
@property (nonatomic, readwrite) double         longitude;
@property (nonatomic, retain) NSString *        address;

@end


/********************************* Order Statistics Info ********************************/
@interface STOrderStatistics : NSObject {
    NSInteger       total_count;
    NSString *      total_count_desc;
    double          total_income;
    NSString *      total_income_desc;
    NSInteger       evgood_count;
    NSString *      evgood_count_desc;
    NSInteger       evnormal_count;
    NSString *      evnormal_count_desc;
    NSInteger       evbad_count;
    NSString *      evbad_count_desc;
}

@property (nonatomic, readwrite) NSInteger      total_count;
@property (nonatomic, retain) NSString *        total_count_desc;
@property (nonatomic, readwrite) double         total_income;
@property (nonatomic, retain) NSString *        total_income_desc;
@property (nonatomic, readwrite) NSInteger      evgood_count;
@property (nonatomic, retain) NSString *        evgood_count_desc;
@property (nonatomic, readwrite) NSInteger      evnormal_count;
@property (nonatomic, retain) NSString *        evnormal_count_desc;
@property (nonatomic, readwrite) NSInteger      evbad_count;
@property (nonatomic, retain) NSString *        evbad_count_desc;

@end


/********************************* Detailed Driver My Order Info ********************************/
@interface STDetailedDrvOrderInfo : NSObject {
    long                uid;
    NSString *          order_num;
    NSMutableArray *    pass_list;
    NSInteger           left_seat;
    double              price;
    double              sysinfo_fee;
    NSString *          sysinfo_fee_desc;
    NSString *          start_addr;
    NSString *          end_addr;
    NSString *          valid_days;
    NSMutableArray *    mid_points;
    NSString *          start_time;
    NSString *          create_time;
    NSString *          accept_time;
    NSInteger           state;
    NSString *          state_desc;
    STOrderStatistics * order_statistics;
    
    NSString *          total_distance;
}

@property (nonatomic, retain) NSString *        total_distance;
@property (nonatomic, readwrite) long           uid;
@property (nonatomic, retain) NSString *        order_num;
@property (nonatomic, retain) NSMutableArray *  pass_list;
@property (nonatomic, readwrite) NSInteger      left_seat;
@property (nonatomic,assign)NSInteger        total_seat;//Ma
@property (nonatomic, readwrite) double         price;
@property (nonatomic, readwrite) double         sysinfo_fee;
@property (nonatomic, retain) NSString *        sysinfo_fee_desc;
@property(nonatomic,copy)NSString      *        start_city;
@property(nonatomic,copy)NSString      *        end_city;
@property (nonatomic, retain) NSString *        start_addr;
@property(nonatomic,assign)double               start_lat;
@property(nonatomic,assign)double               start_lng;
@property (nonatomic, retain) NSString *        end_addr;
@property(nonatomic,assign)double               end_lat;
@property(nonatomic,assign)double               end_lng;
@property (nonatomic, copy) NSString *        left_days;
@property (nonatomic, retain) NSString *        valid_days;
@property (nonatomic, retain) NSMutableArray *  mid_points;
@property (nonatomic, copy) NSString *        start_time;
@property (nonatomic, copy) NSString *        create_time;
@property (nonatomic, copy) NSString *        accept_time;
@property (nonatomic, readwrite) NSInteger      state;
@property (nonatomic, copy) NSString *        state_desc;
@property (nonatomic, retain) STOrderStatistics *    order_statistics;


/*保险
insu_fee:保险费
insu_id : 保单id(long)，
appl_no,投保单号,
effect_time,保单生效期,
insexpr_date,保单失效期
total_amount,保单金额
isd_name：被保人姓名
isd_id：被保人id
insu_status,保单状态0有效，1无效,s  */
@property(nonatomic,assign)double  insu_fee;
@property(nonatomic,assign)long insu_id;

@property(nonatomic,copy)NSString *appl_no;
@property(nonatomic,copy)NSString *effect_time;
@property(nonatomic,copy)NSString *insexpr_date;
@property(nonatomic,assign)double  total_amount;
@property(nonatomic,copy)NSString *isd_name;
@property(nonatomic,assign)long  isd_id;
@property(nonatomic,assign)NSInteger  insu_status;

@end


/********************************* Car Info ********************************/
@interface STCarInfo : NSObject {
    NSString *      carimg;
    NSString *      brand;
    NSString *      type;
    NSInteger       style;
    NSString *      color;
}

@property (nonatomic, retain) NSString *    carimg;
@property (nonatomic, retain) NSString *    brand;
@property (nonatomic, retain) NSString *    type;
@property (nonatomic, readwrite) NSInteger  style;
@property (nonatomic, retain) NSString *    color;

@end


/********************************* Car Type Info ********************************/
@interface STCarTypeInfo : NSObject {
    long            uid;
    NSString *      name;
    NSInteger       style;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    name;
@property (nonatomic, readwrite) NSInteger  style;

@end


/********************************* Car Brand Info ********************************/
@interface STCarBrandInfo : NSObject {
    long                uid;
    NSString *          name;
    NSMutableArray *    types;
}

@property (nonatomic, readwrite) long           uid;
@property (nonatomic, retain) NSString *        name;
@property (nonatomic, retain) NSMutableArray *  types;

@end


/********************************* Car Color Info ********************************/
@interface STCarColorInfo : NSObject {
    long            uid;
    NSString *      name;
    NSString *      code;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    name;

@property (nonatomic, retain) NSString *    code;

@end


/********************************* Driver Info ********************************/
@interface STDriverInfo : NSObject {
    long            uid;
    NSString *      name;
    NSString *      image;
    int             sex;
    int             age;
    NSString *      color;
    NSString *      phone;
    NSString *      carimg;
    NSInteger       drv_career;
    NSString *      drv_career_desc;
    int             goodeval_rate;
    NSString *      goodeval_rate_desc;
    int             carpool_count;
    NSString *      carpool_count_desc;
    NSString *      carno;
    STCarInfo *     carinfo;
    NSString *      car_brand;
    NSString *      car_style;
    NSInteger       car_type;    NSMutableArray *    evals;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    name;
@property (nonatomic, retain) NSString *    image;
@property (nonatomic, readwrite) NSInteger  sex;
@property (nonatomic, readwrite) NSInteger  age;
@property (nonatomic, retain) NSString *    phone;
@property (nonatomic, retain) NSString *    carimg;
@property (nonatomic, readwrite) NSInteger  drv_career;
@property (nonatomic, retain) NSString *    drv_career_desc;
@property (nonatomic, readwrite) NSInteger  goodeval_rate;
@property (nonatomic, retain) NSString *    goodeval_rate_desc;
@property (nonatomic, readwrite) NSInteger  carpool_count;
@property (nonatomic, retain) NSString *    carpool_count_desc;
@property (nonatomic, retain) NSString *    carno;
@property (nonatomic, retain) STCarInfo *   carinfo;
@property (nonatomic, retain) NSString *    car_brand;
@property (nonatomic, retain) NSString *    car_style;
@property (nonatomic, readwrite) NSInteger  car_type;
@property (nonatomic, retain) NSMutableArray *  evals;
@property (nonatomic,retain) NSString * color;
@end


/********************************* Driver Eval Info ********************************/
@interface STDriverEvalInfo : NSObject {
    long            uid;
    NSString *      pass_name;
    int             eval;
    NSString *      eval_desc;
    NSString *      time;
    NSString *      contents;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    pass_name;
@property (nonatomic, readwrite) NSInteger  eval;
@property (nonatomic, retain) NSString *    eval_desc;
@property (nonatomic, retain) NSString *    time;
@property (nonatomic, retain) NSString *    contents;

@end


/********************************* Detailed Customer Order Info ********************************/
@interface STDetailedCusOrderInfo : NSObject {
	long                uid;
	NSString *          order_num;
	STDriverInfo *      driver_info;
	double              price;
	NSString *          start_addr;
	NSString *          end_addr;
	double              start_lat;
	double              start_lng;
	double              end_lat;
	double              end_lng;
	NSString *          valid_days;
	NSString *          left_days;
	NSMutableArray *    mid_points;
	NSString *          start_time;
	NSString *          create_time;
	NSString *          accept_time;
	NSString *          startsrv_time;
	NSInteger           state;
	NSString *          state_desc;
	NSString *          password;
	NSString *          cancelled_balance_desc;
	long                cancelled_balance;
	NSString *          balance_desc;
	NSString *          time_interval_desc;
	int					evaluated;
	NSString*			eval_content;
}

@property (nonatomic, readwrite) long           uid;
@property (nonatomic, retain) NSString *        order_num;
@property (nonatomic, retain) STDriverInfo *    driver_info;
@property (nonatomic, readwrite) double         price;
@property (nonatomic, retain) NSString *        start_addr;
@property (nonatomic, retain) NSString *        end_addr;
@property (nonatomic, readwrite) double         start_lat;
@property (nonatomic, readwrite) double         start_lng;
@property (nonatomic, readwrite) double         end_lat;
@property (nonatomic, readwrite) double         end_lng;
@property (nonatomic, retain) NSString *        valid_days;
@property (nonatomic, retain) NSString *        left_days;
@property (nonatomic, retain) NSMutableArray *  mid_points;
@property (nonatomic, retain) NSString *        start_time;
@property (nonatomic, retain) NSString *        create_time;
@property (nonatomic, retain) NSString *        accept_time;
@property (nonatomic, retain) NSString *        startsrv_time;
@property (nonatomic, readwrite) NSInteger      state;
@property (nonatomic, retain) NSString *        state_desc;
@property (nonatomic, retain) NSString *        password;
@property (nonatomic,retain) NSString *         cancelled_balance_desc;
@property (nonatomic,readwrite) long            cancelled_balance;
@property (nonatomic,retain) NSString *         balance_desc;
@property (nonatomic,retain) NSString *         time_interval_desc;
@property (atomic, readwrite) int				evaluated;
@property (nonatomic, retain) NSString*			eval_content;


/**保险*/
/*保险
 insu_fee:保险费
 insu_id : 保单id(long)，
 appl_no,投保单号,
 effect_time,保单生效期,
 insexpr_date,保单失效期
 total_amount,保单金额
 isd_name：被保人姓名
 isd_id：被保人id
 insu_status,保单状态0有效，1无效,s  */
@property(nonatomic,assign)double  insu_fee;
@property(nonatomic,assign)long insu_id;

@property(nonatomic,copy)NSString *appl_no;
@property(nonatomic,copy)NSString *effect_time;
@property(nonatomic,copy)NSString *insexpr_date;
@property(nonatomic,assign)double  total_amount;
@property(nonatomic,copy)NSString *isd_name;
@property(nonatomic,assign)long  isd_id;
@property(nonatomic,assign)NSInteger  insu_status;


@end


/********************************* 
 pverified : 是否身份认证(int)   0:未认证  1:已认证  2:待审核
 pverified_desc : 是否身份认证说明(String),
 ********************************/
@interface STPassengerInfo : NSObject {
    long            uid;
    NSString *      name;
    NSString *      image;
    int             sex;
    int             age;
    int             pverified;
    NSString *      pverified_desc;
    int             goodeval_rate;
    NSString *      goodeval_rate_desc;
    int             carpool_count;
    NSString *      carpool_count_desc;
    enum PASSENGER_STATE             state;
    NSString *      state_desc;
    int             seat_count;
    NSString *      seat_count_desc;
    NSString *      phone;
    int             evaluated;
    NSString *      evaluated_desc;
    NSString *      eval_content;
    NSMutableArray *    evals;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    name;
@property (nonatomic, retain) NSString *    image;
@property (nonatomic, readwrite) NSInteger  sex;
@property (nonatomic, readwrite) NSInteger  age;
@property (nonatomic, readwrite) NSInteger  pverified;
@property (nonatomic, retain) NSString *    pverified_desc;
@property (nonatomic, readwrite) NSInteger  goodeval_rate;
@property (nonatomic, retain) NSString *    goodeval_rate_desc;
@property (nonatomic, readwrite) NSInteger  carpool_count;
@property (nonatomic, retain) NSString *    carpool_count_desc;
@property (nonatomic, readwrite) enum PASSENGER_STATE  state;
@property (nonatomic, retain) NSString *    state_desc;
@property (nonatomic, readwrite) NSInteger  seat_count;
@property (nonatomic, retain) NSString *    seat_count_desc;
@property (nonatomic, retain) NSString *    phone;
@property (nonatomic, readwrite) NSInteger  evaluated;
@property (nonatomic, retain) NSString *    evaluated_desc;
@property (nonatomic, retain) NSString *    eval_content;
@property (nonatomic, retain) NSMutableArray *  evals;
@end



/********************************* Passenger Eval Info ********************************/
@interface STPassengerEvalInfo : NSObject {
    long            uid;
    NSString *      driver_name;
    int             eval;
    NSString *      eval_desc;
    NSString *      time;
    NSString *      contents;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    driver_name;
@property (nonatomic, readwrite) NSInteger  eval;
@property (nonatomic, retain) NSString *    eval_desc;
@property (nonatomic, retain) NSString *    time;
@property (nonatomic, retain) NSString *    contents;

@end


/********************************* Publish Once Order Return Data ********************************/
@interface STPubOnceOrderRet : NSObject {
	long 		order_id;
	int			wait_time;

	int			driver_lock_time;

	int			add_price_time1;
	int			add_price_time2;
	int			add_price_time3;
	int			add_price_time4;
	int			add_price_time5;
	int			same_price_time1;
	int			same_price_time2;
	int			same_price_time3;
	int			same_price_time4;
	int			same_price_time5;
    int			drvcount;
    int         add_price_default;
    int         add_price_range;
    int         add_price_min;
    int         cut_price_range;
    
    NSString *prompt_1st;
    NSString *prompt;
    
}
@property (nonatomic, retain) NSString *prompt_1st;
@property (nonatomic, retain) NSString *prompt;
@property (nonatomic, readwrite) long	order_id;
@property (nonatomic, readwrite) int	wait_time;
@property (nonatomic, readwrite) int	driver_lock_time;
@property (nonatomic, readwrite) int	add_price_time1;
@property (nonatomic, readwrite) int	add_price_time2;
@property (nonatomic, readwrite) int	add_price_time3;
@property (nonatomic, readwrite) int	add_price_time4;
@property (nonatomic, readwrite) int	add_price_time5;
@property (nonatomic, readwrite) int	same_price_time1;
@property (nonatomic, readwrite) int	same_price_time2;
@property (nonatomic, readwrite) int	same_price_time3;
@property (nonatomic, readwrite) int	same_price_time4;
@property (nonatomic, readwrite) int	same_price_time5;
@property (nonatomic, readwrite) int	drvcount;
@property (nonatomic, readwrite) int    add_price_default;
@property (nonatomic, readwrite) int    add_price_range;
@property (nonatomic, readwrite) int    add_price_min;
@property (nonatomic, readwrite) int    cut_price_range;
@end


/********************************* Once Order Publish Data **************************************/
@interface STOnceOrderPubData : NSObject {
	NSString *		start_addr;
	double			start_lat;
	double 			start_lng;
	NSString *		end_addr;
	double			end_lat;
	double			end_lng;
	NSString *		start_time;
	NSString * 		mid_points;
	NSString *		remark;
	int				req_style;
	double			price;
	NSString *		city;
	int 			wait_time;
}

@property (nonatomic, retain) NSString *	start_addr;
@property (nonatomic, readwrite) double		start_lat;
@property (nonatomic, readwrite) double		start_lng;
@property (nonatomic, retain) NSString *	end_addr;
@property (nonatomic, readwrite) double		end_lat;
@property (nonatomic, readwrite) double 	end_lng;
@property (nonatomic, retain) NSString *	start_time;
@property (nonatomic, retain) NSString *	mid_points;
@property (nonatomic, retain) NSString *	remark;
@property (nonatomic, readwrite) int		req_style;
@property (nonatomic, readwrite) double		price;
@property (nonatomic, retain) NSString *	city;
@property (nonatomic, readwrite) int		wait_time;

@end



/********************************* Detailed Driver InCity Order Info ********************************/
@interface STDetailedDrvInCityOrderInfo : NSObject {
    STPassengerInfo *   pass_info;
    NSString *          start_addr;
    NSString *          end_addr;
    NSString *          start_time;
    NSMutableArray *    mid_points;
    double              price;
    NSString *          price_desc;
    double              sysinfo_fee;
    NSString *          sysinfo_fee_desc;
    NSString *          valid_days;
}

@property (nonatomic, retain) STPassengerInfo * pass_info;
@property (nonatomic, copy) NSString *        start_addr;
@property(nonatomic ,assign)double            start_lat;
@property(nonatomic ,assign)double            start_lng;
@property (nonatomic, copy) NSString *        end_addr;
@property(nonatomic ,assign)double            end_lng;
@property(nonatomic ,assign)double            end_lat;
@property (nonatomic, copy) NSString *        start_time;
@property (nonatomic, retain) NSMutableArray *  mid_points;
@property (nonatomic, readwrite) double         price;
@property (nonatomic, copy) NSString *        price_desc;
@property (nonatomic, readwrite) double         sysinfo_fee;
@property (nonatomic, copy) NSString *        sysinfo_fee_desc;
@property (nonatomic, copy) NSString *        valid_days;
@property (nonatomic, assign) double            insu_fee;

@end


/********************************* Usual Route Info ********************************/
@interface STRouteInfo : NSObject {
    long            uid;
	NSString *		startcity;
	NSString *		endcity;
    NSString *      startaddr;
    NSString *      endaddr;
	double			startlat;
	double			startlng;
	double			endlat;
	double			endlng;
    enum ROUTE_DAYTYPE daytype;
    NSString *      time;
}

@property (nonatomic, readwrite) long       uid;
@property (nonatomic, retain) NSString *    startcity;
@property (nonatomic, retain) NSString *    endcity;
@property (nonatomic, retain) NSString *    startaddr;
@property (nonatomic, retain) NSString *    endaddr;
@property (nonatomic, readwrite) double 	startlat;
@property (nonatomic, readwrite) double 	startlng;
@property (nonatomic, readwrite) double 	endlat;
@property (nonatomic, readwrite) double 	endlng;
@property (nonatomic, readwrite) enum ROUTE_DAYTYPE    daytype;
@property (nonatomic, retain) NSString *    time;


@end


/********************************* Car Verifying Info ********************************/
@interface STCarVerifyingInfo : NSObject {
    NSInteger       userid;
    NSString *      driver_license_fore;
    NSString *      driver_license_back;
    NSString *      brand;
    NSString *      type;
    NSString *      color;
    NSString *      carimg;
    NSString *      driving_license_fore;
    NSString *      driving_license_back;
}

@property (nonatomic, readwrite) NSInteger  userid;
@property (nonatomic, retain) NSString *    driver_license_fore;
@property (nonatomic, retain) NSString *    driver_license_back;
@property (nonatomic, retain) NSString *    brand;
@property (nonatomic, retain) NSString *    type;
@property (nonatomic, retain) NSString *    color;
@property (nonatomic, retain) NSString *    carimg;
@property (nonatomic, retain) NSString *    driving_license_fore;
@property (nonatomic, retain) NSString *    driving_license_back;

@end


/********************************* LongWay Order ********************************/
/**
 *  长途订单接口返回数据模型
 */

@interface STAcceptableLongOrderInfo : NSObject

@property (nonatomic) long uid;
@property (nonatomic,strong) NSString *order_num;
@property (nonatomic) long driver_id;
@property (nonatomic,strong) NSString *driver_img;
@property (nonatomic,strong) NSString *driver_name;
@property (nonatomic) int driver_gender;
@property (nonatomic) int driver_age;
@property (nonatomic) double price;
@property (nonatomic,strong) NSString *price_desc;
@property (nonatomic,strong) NSString *start_addr;
@property (nonatomic,strong) NSString *end_addr;
@property (nonatomic,copy) NSString *start_city;
@property (nonatomic,copy) NSString *end_city;
@property (nonatomic,strong) NSString *start_time;
@property (nonatomic,strong) NSString *start_time_desc;
@property (nonatomic) int leftseats;
@property (nonatomic,strong) NSString *leftseats_desc;
@property (nonatomic,strong) NSString *create_time;

@end



/********************************* Nearby Driver Info ********************************/
@interface STNearbyDriverInfo : NSObject {
    long            driverid;
    double          latitude;
    double          longitude;
}

@property (nonatomic, readwrite) long       driverid;
@property (nonatomic, readwrite) double     latitude;
@property (nonatomic, readwrite) double     longitude;


@end
//*******************************************
@interface STNearbyFalseDriverInfo : NSObject {
    long            driverid;
    double          latitude;
    double          longitude;
}

@property (nonatomic, readwrite) long       driverid;
@property (nonatomic, readwrite) double     latitude;
@property (nonatomic, readwrite) double     longitude;


@end
//*******************************************
/********************************* drive info for Acceptable Long Order Detail Info ********************************/

@interface STDriverInfoForAcceptableLongOrderDetailInfo : NSObject

@property (nonatomic,strong) NSString * brand;
@property (nonatomic,strong) NSString * car_img;
@property (nonatomic) int carpool_count;
@property (nonatomic,strong) NSString * carpool_count_desc;
@property (nonatomic) int driver_age;
@property (nonatomic) int driver_gender;
@property (nonatomic) long driver_id;
@property (nonatomic,strong) NSString * driver_name;
@property (nonatomic,strong) NSString * driver_img;
@property (nonatomic,strong) NSString * car_color;
@property (nonatomic) int drv_career;
@property (nonatomic,strong) NSString * drv_career_desc;
@property (nonatomic,strong) NSString * evgood_rate;
@property (nonatomic,strong) NSString * evgood_rate_desc;
@property (nonatomic,strong) NSString * type;
@property (nonatomic) int style;

@end






/********************************* Acceptable Long Order Detail Info ********************************/

@interface STAcceptableLongOrderDetailInfo : NSObject

@property (nonatomic,strong) STDriverInfoForAcceptableLongOrderDetailInfo * driver_info;
@property (nonatomic,strong) NSString * start_city;
@property (nonatomic,strong) NSString * end_city;
@property (nonatomic,strong) NSString * start_addr;
@property (nonatomic) double start_lat;
@property (nonatomic) double start_lng;
@property (nonatomic,strong) NSString * end_addr;
@property (nonatomic) double end_lat;
@property (nonatomic) double end_lng;
@property (nonatomic,strong) NSString * start_time;
@property (nonatomic) double price;
@property (nonatomic,strong) NSString * price_desc;
@property (nonatomic) int left_seats;

- (instancetype)initWithDictionary:(NSDictionary *)dictionary;
@end



/********************************* Grab Long Order Success Info ********************************/
#pragma mark 长途抢单密码设定成功后，服务器返回结果数据模型
@interface STGrabLongOrderSuccessInfo : NSObject
@property (nonatomic,copy) NSString * car_brand;
@property (nonatomic,copy) NSString * car_color;
@property (nonatomic,copy) NSString * car_img;
@property (nonatomic,copy) NSString * car_type;
@property (nonatomic,copy)  NSString * car_style;
@property (nonatomic,copy) NSString * carno;
@property (nonatomic,copy) NSString * dist;
@property (nonatomic,copy) NSString * dist_desc;
@property (nonatomic,assign) int drv_age;
@property (nonatomic,assign) int drv_gender;
@property (nonatomic,assign) int drv_id;
@property (nonatomic,copy) NSString * drv_img;
@property (nonatomic,copy) NSString * drv_name;
@property (nonatomic,copy) NSString * end_addr;
@property (nonatomic,copy) NSString * password;
@property (nonatomic,copy) NSString * start_addr;
@property (nonatomic,copy) NSString * start_time;
@property(nonatomic,assign)int drv_career;
@property(nonatomic,copy)NSString *drv_career_desc;
@property(nonatomic,assign)int evgood_rate;
@property(nonatomic,copy)NSString *evgood_rate_desc;

@property(nonatomic,assign)int carpool_count;
@property(nonatomic,copy)NSString *carpool_count_desc;
@property (nonatomic) long uid;

@end


/********************************* Grab Long Order fail Info ********************************/

@interface STGrabLongOrderFailInfo : NSObject

@property (nonatomic) long  orderID;
@property (nonatomic,strong) NSString * rembal; //剩余绿点
@property (nonatomic,strong) NSString * total_fee; //抢座位数 * 每个座位的价格

@end


/********************************* Info Fee Calc Method Info ********************************/

@interface InfoFeeCalcMethodInfo : NSObject

@property (nonatomic) int calc_method;
@property (nonatomic) int value;
@property (nonatomic,assign)double insu_fee;
//@property (nonatomic) double ins

@end
/********************************* Driver Postion Info ********************************/
@interface STdriverPosInfo : NSObject
@property (strong ,nonatomic) NSString * driver_name;
@property (nonatomic) double lat;
@property (nonatomic) double lng;
@property (strong ,nonatomic) NSString * time;

@end



