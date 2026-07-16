//
//  OrderSvcMgr.h
//  BJPinChe
//
//  Created by YunSI on 8/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "STDataInfo.h"
#import"SystemPriceInfoResult.h"
@protocol OrderSvcDelegate;

@interface OrderSvcMgr : NSObject
{
    
}

@property(strong, nonatomic) id<OrderSvcDelegate> delegate;

-(void)GetPagedDriverOrders:(long)userid OrderType:(int)order_type LimitTime:(NSString *)limit_time DevToken:(NSString *)devtoken;
-(void)GetLatestDriverOrders:(long)userid OrderType:(int)order_type OrderNum:(NSString *)order_num LimitedType:(int)limited_type DevToken:(NSString *)devtoken;
-(void)GetLatestPassengerOrders:(long)userid OrderType:(int)order_type OrderNum:(NSString *)order_num LimitedType:(int)limited_type DevToken:(NSString *)devtoken;
-(void)GetPagedPassengerOrders:(long)userid OrderType:(int)order_type LimitTime:(NSString *)limit_time DevToken:(NSString *)devtoken;


-(void)insertDriverAcceptableOrders:(long)userid DevToken:(NSString *)devtoken;
-(void)GetLatestAcceptableOnceOrders:(long)userid LimitId:(long)limitid OrderType:(int)order_type DevToken:(NSString *)devtoken;
-(void)GetPagedAcceptableOnceOrders:(long)userid PageNo:(int)pageno OrderType:(int)order_type DevToken:(NSString *)devtoken;
-(void)GetLatestAcceptableOnOffOrders:(long)userid LimitId:(long)limitid StartAddr:(NSString *)start_addr EndAddr:(NSString*)end_addr DevToken:(NSString *)devtoken;
-(void)GetPagedAcceptableOnOffOrders:(long)userid PageNo:(int)pageno StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr DevToken:(NSString *)devtoken;
-(void)GetDetailedDriverOrderInfo:(long)userid OrderId:(long)orderid OrderType:(int)order_type DevToken:(NSString *)devtoken;
-(void)GetDetailedCustomerOrderInfo:(long)userid OrderId:(long)orderid OrderType:(int)order_type DevToken:(NSString *)devtoken;

-(void)GetDriverInfo:(long)userid DriverId:(long)driverid DevToken:(NSString *)devtoken;
- (void)GetDriverLatestEvalInfo:(long)userid DriverId:(long)driverid LimitId:(long)limitid DevToken:(NSString *)devtoken;
- (void)GetDriverPagedEvalInfo:(long)userid DriverId:(long)driverid PageNo:(long)pageno DevToken:(NSString *)devtoken;

//车主身份取消订单
- (void)GetDetailedDriverCancelLongOrderInfo:(int)source DriverId:(long)driverid OrderID:(long)orderid DevToken:(NSString *)devtoken;
//乘客退票
- (void)GetLongOrderCancelInfo:(long)userid OrderID:(long)orderid DevToken:(NSString*)devtoken;
//乘客点击充值绿点操作
- (void)clickchargingbtn:(long)userid OrderID:(long)orderid DevToken:(NSString*)devtoken;
//车主抢单是否继续等待
- (void)has_clickedchargingbtn:(long)userid OrderID:(long)orderid DevToken:(NSString*)devtoken;

//乘客发布市内订单查询 时间接口
- (void)time_left :(long) userid OrderID:(long)orderid DevToken:(NSString *)devtoken;
//加价后发布
- (void)publishOnceOrderAddPrice:(long)userid OrderID:(long)orderid DevToken:(NSString *)devtoken Price:(double)price;

-(void)GetPassengerInfo:(long)userid PassId:(long)passid DevToken:(NSString *)devtoken;
- (void)GetPassengerLatestEvalInfo:(long)userid PassId:(long)passid LimitId:(long)limitid DevToken:(NSString *)devtoken;
- (void)GetPassengerPagedEvalInfo:(long)userid PassId:(long)passid PageNo:(long)pageno DevToken:(NSString *)devtoken;
- (void)publishOnceOrder:(long)userid StartAddr:(NSString *)start_addr StartLat:(double)start_lat StartLng:(double)start_lng EndAddr:(NSString *)end_addr EndLat:(double)end_lat EndLng:(double)end_lng StartTime:(NSString *)start_time MidPoints:(NSString *)mid_points Remark:(NSString *)remark ReqStyle:(int)reqstyle Price:(double)price City:(NSString *)city WaitTime:(int)wait_time DevToken:(NSString *)devtoken;
- (void)publishOnOffOrder:(long)userid StartAddr:(NSString *)start_addr StartLat:(double)start_lat StartLng:(double)start_lng EndAddr:(NSString *)end_addr EndLat:(double)end_lat EndLng:(double)end_lng StartTime:(NSString *)start_time MidPoints:(NSString *)mid_points Remark:(NSString *)remark ReqStyle:(int)reqstyle Price:(double)price Days:(NSString *)days City:(NSString *)city DevToken:(NSString *)devtoken;

- (void)getSystemPriceInfo:(STOnceOrderPubData *)pubData;

- (void)acceptOnceOrder:(long)userid OrderID:(int)orderid Latitude:(double)lat Longitude:(double)lng DevToken:(NSString *)devtoken;
- (void)acceptOnOffOrder:(long)userid OrderID:(int)orderid Days:(NSString *)days DevToken:(NSString *)devtoken;
- (void)getAcceptableInCityOrderDetailInfo:(long)userid OrderID:(long)orderid OrderType:(int)ordertype DevToken:(NSString *)devtoken;
- (void)CheckOnceOrderAcceptance:(long)userid OrderId:(long)orderid Latitude:(double)lat Longitude:(double)lng DevToken:(NSString *)devtoken;
- (void)CheckOnceOrderAgree:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void)ConfirmOnceOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void)SetOnceOrderPassword:(long)userid OrderId:(long)orderid Password:(NSString *)password DevToken:(NSString *)devtoken;//Ma

- (void)ConfirmOnOffOrder:(long)userid OrderId:(long)orderid Password:(NSString *)password DevToken:(NSString *)devtoken;
- (void)CancelOnceOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void)CancelOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void)RefuseOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void)PauseOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void)GetDriverPos:(long)userid driverid:(long)driverid DevToken:(NSString *)devtoken;


@end


// service protocol
@protocol OrderSvcDelegate <NSObject>

@optional
- (void) getPagedDriverOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestDriverOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getPagedPassengerOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestPassengerOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;

- (void) getLatestAcceptableOnceOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getPagedAcceptableOnceOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestAcceptableOnOffOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getPagedAcceptableOnOffOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestAcceptableLongOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getPagedAcceptableLongOrdersResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getDetailedDriverOrderInfoResult : (NSString *)result OrderInfo:(STDetailedDrvOrderInfo *)order_info;
- (void) getDetailedCustomerOrderInfoResult : (NSString *)result OrderInfo:(STDetailedCusOrderInfo *)order_info;

- (void) getDriverInfoResult:(NSString *)result DriverInfo:(STDriverInfo *)driver_info;
- (void) getDriverLatestEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getDriverPagedEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList;

- (void) getPassengerInfoResult:(NSString *)result PassengerInfo:(STPassengerInfo *)passenger_info;
- (void) getPassengerLatestEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getPassengerPagedEvalInfoResult:(NSString *)result dataList:(NSMutableArray *)dataList;
- (void) publishOnceOrderResult:(NSString *)result RetData:(STPubOnceOrderRet *)retData;
- (void) publishOnceOrderAddPrice:(NSString *)result retData:(NSDictionary *)dic;
- (void) getsystemPriceInfoResult:(NSString *)result RetData: (NSMutableArray *) retData;
-(void) getTime_left:(NSString *)result RetData:(NSDictionary *)retData;

- (void) publishOnOffOrderResult:(NSString *)result;
- (void) acceptOnceOrderResult:(int)result retmsg:(NSString*)retmsg wait_time:(int)wait_time;
- (void) acceptOnOffOrderResult:(NSString *)result;
- (void) getAcceptableInCityOrderDetailInfoResult:(NSString *)result OrderInfo:(STDetailedDrvInCityOrderInfo *)order_info;
- (void) checkOnceOrderAcceptanceResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo;
- (void) checkOnceOrderAgreeResult:(NSString *)result PassImg:(NSString *)pass_img PassName:(NSString *)pass_name PassGender:(int)pass_gender PassAge:(int)pass_age StartTime:(NSString *)start_time StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr;
- (void) confirmOnceOrderResult:(NSString *)result;
- (void) setOnceOrderPasswordResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STSingleTimeOrderInfo *)orderInfo;
- (void) confirmOnOffOrderResult:(NSString *)result DriverInfo:(STDriverInfo *)driverInfo OrderInfo:(STWorkTimeOrderInfo *)orderInfo;
- (void) cancelOnceOrderResult:(NSString *)result;
- (void) cancelOnOffOrderResult:(NSString *)result;
- (void) refuseOnOffOrderResult:(NSString *)result;
- (void) pauseOnOffOrderResult:(NSString *)result;
- (void) getDriverPosResult:(NSString *)result driver_name:(NSString *)driver_name lat:(double)lat lng:(double)lng time:(NSString *)time;


- (void) duplicateUser : (NSString *)result;

-(void) getDriverCancelResult:(NSString *)result RetData:(NSDictionary *)RetData;
-(void) getLongOrderCancelInfo:(NSString *)result RetData:(NSDictionary *)RetData;
-(void) getClickchargingbtn:(NSString *)result RetData:(NSDictionary *)RetData;
-(void) getHas_clickedchargingbtn:(NSString *)result RetData:(NSDictionary *)RetData;
@end



