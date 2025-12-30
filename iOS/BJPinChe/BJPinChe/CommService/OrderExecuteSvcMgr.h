//
//  OrderExecuteSvcMgr.h
//  BJPinChe
//
//  Created by YunSI on 9/6/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "STDataInfo.h"

@protocol OrderExecuteSvcDelegate;

@interface OrderExecuteSvcMgr : NSObject
{
	
}

@property(strong, nonatomic) id<OrderExecuteSvcDelegate> delegate;

- (void) ExecuteOnceOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void) ExecuteOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void) SignOnceOrderDriverArrival:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void) SignOnOffOrderDriverArrival:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void) SignOnceOrderPassengerUpload:(long)driverid OrderId:(long)orderid PassId:(long)passid Password:(NSString *)password DevToken:(NSString *)devtoken;
- (void) SignOnOffOrderPassengerUpload:(long)driverid OrderId:(long)orderid PassId:(long)passid Password:(NSString *)password DevToken:(NSString *)devtoken;
- (void) EndOnceOrder:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void) EndOnOffOrder:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken;
- (void) EvaluateOnceOrderPass:(long)driverid PassId:(long)passid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken;
- (void) EvaluateOnOffOrderPass:(long)driverid PassId:(long)passid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken;
- (void) EvaluateOnceOrderDriver:(long)passid DriverId:(long)driverid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken;
- (void) EvaluateOnOffOrderDriver:(long)passid DriverId:(long)driverid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken;
- (void) PayNormalOrder:(long)userid OrderId:(long)orderid OrderType:(int)order_type Price:(double)price Coupons:(NSString *)coupons DevToken:(NSString *)devtoken;
- (void) CheckOrderCancelledState:(long)userid OrderId:(long)orderid OrderType:(int)ordertype Distance:(double)distance DevToken:(NSString *)devtoken;


@end


// Service Protocol
@protocol OrderExecuteSvcDelegate <NSObject>

@optional
- (void) executeOnceOrderResult:(NSString *)result;
- (void) executeOnOffOrderResult:(NSString *)result;
- (void) signOnceOrderDriverArrivalResult:(NSString *)result;
- (void) signOnOffOrderDriverArrivalResult:(NSString *)result;
- (void) signOnceOrderPassengerUploadResult:(NSString *)result;
- (void) signOnOffOrderPassengerUploadResult:(NSString *)result;
- (void) endOnceOrderResult:(NSString *)result;
- (void) endOnOffOrderResult:(NSString *)result;
- (void) evaluateOnceOrderPassResult:(NSString *)result Level:(int)level;
- (void) evaluateOnOffOrderPassResult:(NSString *)result Level:(int)level;
- (void) evaluateOnceOrderDriverResult:(NSString *)result Level:(int)level;
- (void) evaluateOnOffOrderDriverResult:(NSString *)result Level:(int)level;
- (void) payNormalOrderResult:(NSString *)result;
- (void) checkOrderCancelledStateResult:(NSString *)result status:(int)status;

- (void) duplicateUser : (NSString *)result;

@end