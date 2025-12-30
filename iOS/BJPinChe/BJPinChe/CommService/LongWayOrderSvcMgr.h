//
//  LongWayOrderSvcMgr.h
//  BJPinChe
//
//  Created by CKK on 14-8-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>

// service protocol
@protocol LongWayOrderSvcDelegate <NSObject>
@optional

- (void) executeLongOrderResult : (NSString*)result;
- (void) signLongOrderDriverArrivalResult : (NSString *)result;
- (void) signLongOrderPassengerUpload : (NSString *)result;
- (void) signLongOrderPassengerGiveup : (NSString *)result;
- (void) startLongOrderDriving : (NSString *)result;
- (void) endLongOrder : (NSString *)result;
- (void) evaluateLongOrderPass : (NSString *)result;
- (void) evaluateLongOrderDriver : (NSString *)result;

- (void) GetPagedAcceptableLongOrders : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) GetLatestAcceptableLongOrders : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) PublishLongOrder : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) AcceptLongOrder:(int)retcode retmsg:(NSString *)retmsg dataList:(NSMutableArray *)dataList;
- (void) SetLongOrderPassword : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) GetAcceptableLongOrderDetailInfo : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) GetInfoFeeCalcMethod : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) GetDriverPos : (NSString *)result dataList:(NSMutableArray *)dataList;

- (void) duplicateUser : (NSString *)result;

@end

@interface LongWayOrderSvcMgr : NSObject

@property (nonatomic,retain) id<LongWayOrderSvcDelegate> delegate;

- (void) ExecuteLongOrder : (long)userid orderid:(long)orderid devToken:(NSString *)devToken;
- (void) SignLongOrderDriverArrival : (long)driverid orderid:(long)orderid devToken:(NSString *)devToken;
- (void) SignLongOrderPassengerUpload : (long)driverid orderid:(long)orderid passid:(long)passid password:(NSString *)password devToken:(NSString *)devToken;
- (void) SignLongOrderPassengerGiveup : (long)driverid orderid:(long)orderid passid:(long)passid devToken:(NSString *)devToken;
- (void) StartLongOrderDriving : (long)driverid orderid:(long)orderid devToken:(NSString *)devToken;
- (void) EndLongOrder : (long)driverid orderid:(long)orderid devToken:(NSString *)devToken;
- (void) EvaluateLongOrderPass : (long)driverid passid:(long)passid orderid:(long)orderid level:(int)level msg:(NSString *)msg devToken:(NSString *)devToken;
- (void) EvaluateLongOrderDriver : (long)passid orderid:(long)orderid level:(int)level msg:(NSString *)msg devToken:(NSString *)devToken driverId:(long)driverid;

- (void)GetPagedAcceptableLongOrdersWithUserid:(long)userid PageNo:(int)pageno StartAddr:(NSString *)start_addr  EndAddr:(NSString *)end_addr DevToken:(NSString*)devtoken;

- (void)GetLatestAcceptableLongOrdersWithUserid:(long)userid Limitid:(NSString *)limitid StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr DevToken:(NSString *)devtoken;

- (void)PublishLongOrderWithUserID:(long)userid AndStartCity:(NSString *)startCity AndStartAddr:(NSString *)startAddr AndEndCity:(NSString *)endCity AndEndAddr:(NSString *)endAddr AndStartlat:(double) startLat AndStartLng:(double)startLng AndEndLat:(double)endLat AndEndLng:(double)endLng AndStartTime:(NSString *)startTime AndRemark:(NSString *)remark AndPrice:(NSString *)price AndSeatsCount:(int)seatsCount AndDevtoken:(NSString *)devToken;
//抢单
- (void) AcceptLongOrderWithUserID:(long)userid AndOrderId:(long)orderid AndSeatsCount:(int) seatsCount;
//设定长途订单密码
- (void) SetLongOrderPasswordWithUserID:(long)userid AndOrderId:(long)orderid AndPassword:(NSString *)password;
//长途订单确认
- (void)GetAcceptableLongOrderDetailInfoWithUserId:(long)userid AndOrderId:(NSString *)orderid;
//获取信息费计算方式
- (void)GetInfoFeeCalcMethodWithUserID:(long)userid AndCity:(NSString *)city;
//获取司机当前位置
- (void)GetDriverPosWithUserID:(long)userid AndDriverId:(long)driverId;
@end
