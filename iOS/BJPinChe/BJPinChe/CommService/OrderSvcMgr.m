//
//  OrderSvcMgr.m
//  BJPinChe
//
//  Created by YunSI on 8/22/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "OrderSvcMgr.h"
#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "ServiceMethod.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"
#import "SystemPriceInfoResult.h"
@implementation OrderSvcMgr

@synthesize delegate;
#pragma mark 车主身份 获得订单
-(void)GetLatestDriverOrders:(long)userid OrderType:(int)order_type OrderNum:(NSString *)order_num LimitedType:(int)limited_type DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestDriverOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            order_num, @"order_num",
                            [NSString stringWithFormat:@"%d", limited_type], @"limited_type",
                            devtoken, @"devtoken",
                            nil];
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetLatestDriverOrdersRes:responseStr];
		 
		 MyLog(@"GetLatestDriverOrders Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getLatestDriverOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"GetLatestDriverOrders [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetPagedDriverOrders:(long)userid OrderType:(int)order_type LimitTime:(NSString *)limit_time DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedDriverOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            limit_time, @"limit_time",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPagedDriverOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPagedDriverOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma mark 乘客获取最新订单网络请求
-(void)GetLatestPassengerOrders:(long)userid OrderType:(int)order_type OrderNum:(NSString *)order_num LimitedType:(int)limited_type DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestPassengerOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                          [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            order_num, @"order_num",
                           [NSString stringWithFormat:@"%d", limited_type] , @"limitid_type",
                            devtoken, @"devtoken",
                            nil];
    //  [NSString stringWithFormat:@"%d", order_type]
    //[NSString stringWithFormat:@"%d", limited_type]
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetLatestPassengerOrdersRes:responseStr];
		 
		 MyLog(@"GetLatestPassengerOrders Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getLatestPassengerOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"GetLatestPassengerOrders[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma mark 乘客获得 之前的订单请求
-(void)GetPagedPassengerOrders:(long)userid OrderType:(int)order_type LimitTime:(NSString *)limit_time DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedPassengerOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            limit_time, @"limit_time",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPagedPassengerOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPagedPassengerOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
-(void)insertDriverAcceptableOrders:(long)userid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdInsertDriverAcceptableOrders]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
//         [self parseGetLatestAcceptableOnceOrdersRes:responseStr];
         
         MyLog(@"GetLatestAcceptableOnceOrders Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
//         [delegate getLatestAcceptableOnceOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
-(void)GetLatestAcceptableOnceOrders:(long)userid LimitId:(long)limitid OrderType:(int)order_type DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestAcceptableOnceOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetLatestAcceptableOnceOrdersRes:responseStr];
		 
		 MyLog(@"GetLatestAcceptableOnceOrders Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getLatestAcceptableOnceOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
 }

-(void)GetPagedAcceptableOnceOrders:(long)userid PageNo:(int)pageno OrderType:(int)order_type DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedAcceptableOnceOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", pageno], @"pageno",
                            [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPagedAcceptableOnceOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPagedAcceptableOnceOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetLatestAcceptableOnOffOrders:(long)userid LimitId:(long)limitid StartAddr:(NSString *)start_addr EndAddr:(NSString*)end_addr DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestAcceptableOnOffOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            start_addr, @"start_addr",
                            end_addr, @"end_addr",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetLatestAcceptableOnOffOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getLatestAcceptableOnOffOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetPagedAcceptableOnOffOrders:(long)userid PageNo:(int)pageno StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedAcceptableOnOffOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", pageno], @"pageno",
                            start_addr, @"start_addr",
                            end_addr, @"end_addr",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPagedAcceptableOnOffOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPagedAcceptableOnOffOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetLatestAcceptableLongOrders:(long)userid LimitId:(long)limitid StartAddr:(NSString *)start_addr EndAddr:(NSString*)end_addr DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestAcceptableLongOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            start_addr, @"start_addr",
                            end_addr, @"end_addr",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetLatestAcceptableLongOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getLatestAcceptableLongOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetPagedAcceptableLongOrders:(long)userid PageNo:(int)pageno StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedAcceptableLongOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", pageno], @"pageno",
                            start_addr, @"start_addr",
                            end_addr, @"end_addr",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPagedAcceptableLongOrdersRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPagedAcceptableLongOrdersResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetDetailedDriverOrderInfo:(long)userid OrderId:(long)orderid OrderType:(int)order_type DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDetailedDriverOrderInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [NSString stringWithFormat:@"%d", order_type], @"order_type",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 MyLog(@"GetDetailedDriverOrderInfo Request Successful, response '%@'", responseStr);
		 [self parseGetDetailedDriverOrderInfoRes:responseStr];
		 
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getDetailedDriverOrderInfoResult:SVCERR_MSG_ERROR OrderInfo:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

-(void)GetDetailedCustomerOrderInfo:(long)userid OrderId:(long)orderid OrderType:(int)order_type DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
	[method appendString:[[CommManager getCommMgr] bathServiceUrl]];
	[method appendString:[ServiceMethod cmdGetDetailedPassengerOrderInfo]];

	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
	//httpClient.parameterEncoding = AFJSONParameterEncoding;

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
							[NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", order_type], @"order_type",
							devtoken, @"devtoken",
							nil];


	[httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
	 {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];

		 [self parseGetDetailedCustomerOrderInfoRes:responseStr];

		 MyLog(@"Request Successful, response '%@'", responseStr);
	 } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getDetailedCustomerOrderInfoResult:SVCERR_MSG_ERROR OrderInfo:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
	 }];
}

-(void)GetDriverInfo:(long)userid DriverId:(long)driverid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDriverInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetDriverInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getDriverInfoResult:SVCERR_MSG_ERROR DriverInfo:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma mark  车主取消订单
- (void)GetDetailedDriverCancelLongOrderInfo:(int)source DriverId:(long)driverid OrderID:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetCancelLongOrder]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self parseGetDetailedDriverCancelOrderInfoRes:responseStr];
         
         MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getDriverInfoResult:SVCERR_MSG_ERROR DriverInfo:NULL];
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma mark  乘客退票
- (void)GetLongOrderCancelInfo:(long)userid OrderID:(long)orderid DevToken:(NSString *)devtoken{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLongOrderCancelInfo]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self parseGetLongOrderCancelInfoRes:responseStr];
         
         MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getDriverInfoResult:SVCERR_MSG_ERROR DriverInfo:NULL];
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma mark 乘客点击充值绿点
- (void)clickchargingbtn:(long)userid OrderID:(long)orderid DevToken:(NSString*)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdClickchargingbtn]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self Clickchargingbtn:responseStr];
         
         MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ([delegate respondsToSelector:@selector(getDriverInfoResult:DriverInfo:)]) {
             [delegate getDriverInfoResult:SVCERR_MSG_ERROR DriverInfo:NULL];
         }
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
- (void)has_clickedchargingbtn:(long)userid OrderID:(long)orderid DevToken:(NSString*)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdHas_clickedchargingbtn]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self has_clickedchargingbtn:responseStr];
         
         MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getHas_clickedchargingbtn:SVCERR_MSG_ERROR RetData:nil];
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
- (void)GetDriverLatestEvalInfo:(long)userid DriverId:(long)driverid LimitId:(long)limitid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDriverLatestEvalInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetDriverLatestEvalInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getDriverLatestEvalInfoResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];

}

- (void)GetDriverPagedEvalInfo:(long)userid DriverId:(long)driverid PageNo:(long)pageno DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDriverPagedEvalInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", pageno], @"pageno",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetDriverPagedEvalInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getDriverPagedEvalInfoResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}

-(void)GetPassengerInfo:(long)userid PassId:(long)passid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPassengerInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", passid], @"passid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPassengerInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPassengerInfoResult:SVCERR_MSG_ERROR PassengerInfo:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


- (void)time_left :(long) userid OrderID:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:@"time_left"];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self parseGetTime_left:responseStr];
         
         MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getTime_left:SVCERR_MSG_ERROR RetData:nil];
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)GetPassengerLatestEvalInfo:(long)userid PassId:(long)passid LimitId:(long)limitid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPassengerLatestEvalInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", passid], @"passid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPassengerLatestEvalInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPassengerLatestEvalInfoResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}

- (void)GetPassengerPagedEvalInfo:(long)userid PassId:(long)passid PageNo:(long)pageno DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPassengerPagedEvalInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", passid], @"passid",
                            [NSString stringWithFormat:@"%ld", pageno], @"pageno",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPassengerPagedEvalInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getPassengerPagedEvalInfoResult:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}
-(void)publishOnceOrderAddPrice:(long)userid OrderID:(long)orderid DevToken:(NSString *)devtoken Price:(double)price
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdPublishOnceOrderAddPrice]];
    
    NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
    AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld",orderid],@"orderid",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%0.2f", price], @"new_price",
                            @"180",@"wait_time",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         [self parsePublishOnceOrderAddPrice:responseStr];
         
         MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate publishOnceOrderAddPrice:SVCERR_MSG_ERROR retData:nil];
         MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
    
//    devtoken = "187F7AD2-E4C2-4720-BD21-E3C6AE2E46D0";
//    orderid = 1579;
//    price = "80.000000";
//    source = 11;
//    userid = 4;
}

- (void)publishOnceOrder:(long)userid StartAddr:(NSString *)start_addr StartLat:(double)start_lat StartLng:(double)start_lng EndAddr:(NSString *)end_addr EndLat:(double)end_lat EndLng:(double)end_lng StartTime:(NSString *)start_time MidPoints:(NSString *)mid_points Remark:(NSString *)remark ReqStyle:(int)reqstyle Price:(double)price City:(NSString *)city WaitTime:(int)wait_time DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdPublishOnceOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            start_addr, @"start_addr",
                            [NSString stringWithFormat:@"%f", start_lat], @"start_lat",
                            [NSString stringWithFormat:@"%f", start_lng], @"start_lng",
                            end_addr, @"end_addr",
                            [NSString stringWithFormat:@"%f", end_lat], @"end_lat",
                            [NSString stringWithFormat:@"%f", end_lng], @"end_lng",
                            start_time, @"start_time",
                            mid_points, @"mid_points",
                            remark, @"remark",
                            [NSString stringWithFormat:@"%d", reqstyle], @"reqstyle",
                            [NSString stringWithFormat:@"%f", price], @"price",
                            city, @"city",
                            [NSString stringWithFormat:@"%d", wait_time], @"wait_time",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parsePublishOnceOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate publishOnceOrderResult:SVCERR_MSG_ERROR RetData:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}
/**
 *  APP_USER
 *
 *  @param pubData <#pubData description#>
 */
- (void)getSystemPriceInfo:(STOnceOrderPubData *)pubData
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:@"getSystemPriceInfo"];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", [Common getUserID]], @"userid",
                            pubData.start_addr, @"start_addr",
                            [NSString stringWithFormat:@"%f", pubData.start_lat], @"start_lat",
                            [NSString stringWithFormat:@"%f", pubData.start_lng], @"start_lng",
                            pubData.end_addr, @"end_addr",
                            [NSString stringWithFormat:@"%f", pubData.end_lat], @"end_lat",
                            [NSString stringWithFormat:@"%f", pubData.end_lng], @"end_lng",
                            pubData.start_time, @"start_time",
                            pubData.mid_points, @"mid_points",
                          
                          
                            pubData.city, @"city",
                         
                            [Common getDeviceMacAddress], @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetSystemPriceInfo:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ([delegate respondsToSelector:@selector(publishOnceOrderResult:RetData:)]) {
             [delegate publishOnceOrderResult:SVCERR_MSG_ERROR RetData:nil];
         }
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}

- (void)publishOnOffOrder:(long)userid StartAddr:(NSString *)start_addr StartLat:(double)start_lat StartLng:(double)start_lng EndAddr:(NSString *)end_addr EndLat:(double)end_lat EndLng:(double)end_lng StartTime:(NSString *)start_time MidPoints:(NSString *)mid_points Remark:(NSString *)remark ReqStyle:(int)reqstyle Price:(double)price Days:(NSString *)days City:(NSString *)city DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdPublishOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            start_addr, @"start_addr",
                            [NSString stringWithFormat:@"%f", start_lat], @"start_lat",
                            [NSString stringWithFormat:@"%f", start_lng], @"start_lng",
                            end_addr, @"end_addr",
                            [NSString stringWithFormat:@"%f", end_lat], @"end_lat",
                            [NSString stringWithFormat:@"%f", end_lng], @"end_lng",
                            start_time, @"start_time",
                            mid_points, @"mid_points",
                            remark, @"remark",
                            [NSString stringWithFormat:@"%d", reqstyle], @"reqstyle",
                            [NSString stringWithFormat:@"%f", price], @"price",
                            days, @"days",
                            city, @"city",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parsePublishOnOffOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate publishOnOffOrderResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}

- (void)acceptOnceOrder:(long)userid OrderID:(int)orderid Latitude:(double)lat Longitude:(double)lng DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdAcceptOnceOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", orderid], @"orderid",
                            [NSString stringWithFormat:@"%f", lat], @"lat",
                            [NSString stringWithFormat:@"%f", lng], @"lng",
                            devtoken, @"devtoken",
                            nil];


	[httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];

		 [self parseAcceptOnceOrderRes:responseStr];

		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate acceptOnceOrderResult:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR wait_time:0];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)acceptOnOffOrder:(long)userid OrderID:(int)orderid Days:(NSString *)days DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdAcceptOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", orderid], @"orderid",
                            days, @"days",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAcceptOnOffOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate acceptOnOffOrderResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * get acceptable order detail information 
 * @param : userid [in], user index
 * @param : orderid [in], order index
 * @param : ordertype [in], order type : 1:ST, 2:WT
 */
- (void)getAcceptableInCityOrderDetailInfo:(long)userid OrderID:(long)orderid OrderType:(int)ordertype DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetAcceptableInCityOrderDetailInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [NSString stringWithFormat:@"%d", ordertype], @"order_type",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetAcceptableInCityOrderDetailInfoRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate getAcceptableInCityOrderDetailInfoResult:SVCERR_MSG_ERROR OrderInfo:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)CheckOnceOrderAcceptance:(long)userid OrderId:(long)orderid Latitude:(double)lat Longitude:(double)lng DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdCheckOnceOrderAcceptance]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%f", lat], @"lat",
							[NSString stringWithFormat:@"%f", lng], @"lng",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseCheckOnceOrderAcceptanceRes:responseStr];
		 
		 MyLog(@"CheckOnceOrderAcceptance  Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ([delegate respondsToSelector:@selector(checkOnceOrderAcceptanceResult:DriverInfo:OrderInfo:)]) {
             [delegate checkOnceOrderAcceptanceResult:SVCERR_MSG_ERROR DriverInfo:nil OrderInfo:nil];
         }
		 MyLog(@"CheckOnceOrderAcceptance  [HTTPClient Error]: %@", error.localizedDescription);
     }];
}



- (void)CheckOnceOrderAgree:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdCheckOnceOrderAgree]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseCheckOnceOrderAgreeRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate checkOnceOrderAgreeResult:SVCERR_MSG_ERROR PassImg:nil PassName:nil PassGender:0 PassAge:0 StartTime:nil StartAddr:nil EndAddr:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
/**
 *  确认订单 网络接口
 *
 *  @param userid   <#userid description#>
 *  @param orderid  <#orderid description#>
 *  @param devtoken <#devtoken description#>
 */
- (void)ConfirmOnceOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdConfirmOnceOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {    
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseConfirmOnceOrderRes:responseStr];
		
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		// [delegate confirmOnceOrderResult:SVCERR_MSG_ERROR ];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
/**
 *  setOnceOrderPassword  与confirmOnceorder 混淆
 *
 *  @param userid   <#userid description#>
 *  @param orderid  <#orderid description#>
 *  @param password <#password description#>
 *  @param devtoken <#devtoken description#>
 */

-(void)SetOnceOrderPassword:(long)userid OrderId:(long)orderid Password:(NSString *)password DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod setOnceOrderPassword]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							password, @"password",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSetOnceOrderPasswordRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)ConfirmOnOffOrder:(long)userid OrderId:(long)orderid Password:(NSString *)password DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdConfirmOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							password, @"password",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseConfirmOnOffOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate confirmOnOffOrderResult:SVCERR_MSG_ERROR DriverInfo:nil OrderInfo:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)CancelOnceOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdCancelOnceOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseCancelOnceOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if ([delegate respondsToSelector:@selector(cancelOnceOrderResult:)]) {
             [delegate cancelOnceOrderResult:SVCERR_MSG_ERROR];
         }
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)CancelOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdCancelOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseCancelOnOffOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate cancelOnOffOrderResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)RefuseOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdRefuseOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseRefuseOnOffOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate refuseOnOffOrderResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void)PauseOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdPauseOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"passid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parsePauseOnOffOrderRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate pauseOnOffOrderResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma 获取车主位置
- (void)GetDriverPos:(long)userid driverid:(long)driverid DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDriverPos]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", driverid], @"driverid",
							devtoken, @"devtoken",
                            nil];
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseDriverPosRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getDriverPosResult:SVCERR_MSG_ERROR driver_name:nil lat:0 lng:0 time:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
- (void)parseGetLatestDriverOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STOrderInfo* stOrder = [[STOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.type = [[item objectForKey:@"type"] intValue];
                stOrder.type_desc = [item objectForKey:@"type_desc"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.customerNum = [[item objectForKey:@"pass_count"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.price_desc = [item objectForKey:@"price_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.state = [[item objectForKey:@"state"] intValue];
                stOrder.state_desc = [item objectForKey:@"state_desc"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.start_city = [item objectForKey:@"start_city"];
                stOrder.end_city = [item objectForKey:@"end_city"];
                
                
                if(dataList.count  >0)
                {
                    STOrderInfo *last =[dataList lastObject];
                    if( last.uid !=stOrder.uid)
                    {
                        [dataList addObject:stOrder];
                    }
                }else
                {
                    [dataList addObject:stOrder];
                    
                }

               
            }
        }
    }
    
    [delegate getLatestDriverOrdersResult:retMsg dataList:dataList];
}

- (void)parseGetPagedDriverOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
               
                STOrderInfo* stOrder = [[STOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.type = [[item objectForKey:@"type"] intValue];
                stOrder.type_desc = [item objectForKey:@"type_desc"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.customerNum = [[item objectForKey:@"pass_count"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.price_desc = [item objectForKey:@"price_desc"];
                stOrder.insun_fee =[[item objectForKey:@"insu_fee"]doubleValue];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.state = [[item objectForKey:@"state"] intValue];
                stOrder.state_desc = [item objectForKey:@"state_desc"];
                stOrder.create_time = [item objectForKey:@"create_time"];
				stOrder.evaluated = [[item objectForKey:@"evaluated"] intValue];
                stOrder.evaluated_desc = [item objectForKey:@"evaluated_desc"];
                stOrder.eval_content = [item objectForKey:@"eval_content"];
                stOrder.start_city = [item objectForKey:@"start_city"];
                stOrder.end_city = [item objectForKey:@"end_city"];
                stOrder.sysinfo_fee = [[item objectForKey:@"sysinfo_fee"] doubleValue];
                
                if(dataList.count  >0)
                {
                    STOrderInfo *last =[dataList lastObject];
                    if( last.uid !=stOrder.uid)
                    {
                        [dataList addObject:stOrder];
                    }
                }else
                {
                    [dataList addObject:stOrder];

                }
				
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getPagedDriverOrdersResult:dataList:)]) {
        [delegate getPagedDriverOrdersResult:retMsg dataList:dataList];
    }
    
}

- (void)parseGetLatestPassengerOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STOrderInfo* stOrder = [[STOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.type = [[item objectForKey:@"type"] intValue];
                stOrder.type_desc = [item objectForKey:@"type_desc"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.customerNum = [[item objectForKey:@"pass_count"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.price_desc = [item objectForKey:@"price_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.evaluated = [[item objectForKey:@"evaluated"] intValue];
                stOrder.evaluated_desc = [item objectForKey:@"evaluated_desc"];
                stOrder.eval_content = [item objectForKey:@"eval_content"];
                stOrder.state = [[item objectForKey:@"state"] intValue];
                stOrder.state_desc = [item objectForKey:@"state_desc"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.start_city = [item objectForKey:@"start_city"];
                stOrder.end_city = [item objectForKey:@"end_city"];
                
                
                if(dataList.count  >0)
                {
                    STOrderInfo *last =[dataList lastObject];
                    if( last.uid !=stOrder.uid)
                    {
                        [dataList addObject:stOrder];
                    }
                }else
                {
                    [dataList addObject:stOrder];
                    
                }


            }
        }
    }
    
    [delegate getLatestPassengerOrdersResult:retMsg dataList:dataList];
}

- (void)parseGetPagedPassengerOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make order info list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STOrderInfo* stOrder = [[STOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.type = [[item objectForKey:@"type"] intValue];
                stOrder.type_desc = [item objectForKey:@"type_desc"];
                stOrder.driver_id = [[item objectForKey:@"driver_id"] intValue];
                stOrder.image = [item objectForKey:@"driver_img"];
                stOrder.name = [item objectForKey:@"driver_name"];
                stOrder.sex = [[item objectForKey:@"driver_gender"] intValue];
                stOrder.age = [[item objectForKey:@"driver_age"] intValue];
                stOrder.customerNum = [[item objectForKey:@"pass_count"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.price_desc = [item objectForKey:@"price_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.evaluated = [[item objectForKey:@"evaluated"] intValue];
                stOrder.evaluated_desc = [item objectForKey:@"evaluated_desc"];
                stOrder.eval_content = [item objectForKey:@"eval_content"];
                stOrder.state = [[item objectForKey:@"state"] intValue];
                stOrder.state_desc = [item objectForKey:@"state_desc"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.start_city = [item objectForKey:@"start_city"];
                stOrder.end_city = [item objectForKey:@"end_city"];
                
                if(dataList.count  >0)
                {
                    STOrderInfo *last =[dataList lastObject];
                    if( last.uid !=stOrder.uid)
                    {
                        [dataList addObject:stOrder];
                    }
                }else
                {
                    [dataList addObject:stOrder];
                    
                }
            }
        }
    }
    if([delegate respondsToSelector:@selector(getPagedPassengerOrdersResult:dataList:)])
    {
      [delegate getPagedPassengerOrdersResult:retMsg dataList:dataList];
    }
  
}


- (void)parseGetLatestAcceptableOnceOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STSingleTimeOrderInfo *stOrder = [[STSingleTimeOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.sysinfo_fee = [[item objectForKey:@"sysinfo_fee"] doubleValue];
                stOrder.sysinfo_fee_desc = [item objectForKey:@"sysinfo_fee_desc"];
                stOrder.insun_fee =[[item objectForKey:@"insun_fee"]doubleValue];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.distance = [[item objectForKey:@"distance"] doubleValue];
                stOrder.distance_desc = [item objectForKey:@"distance_desc"];
                stOrder.midpoints = [[item objectForKey:@"midpoints"] intValue];
                stOrder.midpoints_desc = [item objectForKey:@"midpoints_desc"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.mileage = [[item objectForKey:@"mileage"] doubleValue];
                
                [dataList addObject:stOrder];
            }
        }
    }
    
    if([delegate respondsToSelector:@selector(getLatestAcceptableOnceOrdersResult:dataList:)])
    {
      [delegate getLatestAcceptableOnceOrdersResult:retMsg dataList:dataList];
    }
    
}

- (void)parseGetPagedAcceptableOnceOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STSingleTimeOrderInfo *stOrder = [[STSingleTimeOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.sysinfo_fee = [[item objectForKey:@"sysinfo_fee"] doubleValue];
                stOrder.sysinfo_fee_desc = [item objectForKey:@"sysinfo_fee_desc"];
                stOrder.insun_fee =[[item objectForKey:@"insun_fee"]doubleValue];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.distance = [[item objectForKey:@"distance"] doubleValue];
                stOrder.distance_desc = [item objectForKey:@"distance_desc"];
                stOrder.midpoints = [[item objectForKey:@"midpoints"] intValue];
                stOrder.midpoints_desc = [item objectForKey:@"midpoints_desc"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.status = [item objectForKey:@"status"];
                stOrder.mileage = [[item objectForKey:@"mileage"] doubleValue];
                [dataList addObject:stOrder];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getPagedAcceptableOnceOrdersResult:dataList:)]) {
        [delegate getPagedAcceptableOnceOrdersResult:retMsg dataList:dataList];
    }
    
}


- (void)parseGetLatestAcceptableOnOffOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STWorkTimeOrderInfo* stOrder = [[STWorkTimeOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.sysinfo_fee = [[item objectForKey:@"sysinfo_fee"] doubleValue];
                stOrder.sysinfo_fee_desc = [item objectForKey:@"sysinfo_fee_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.midpoints = [[item objectForKey:@"midpoints"] intValue];
                stOrder.midpoints_desc = [item objectForKey:@"midpoints_desc"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.start_time_desc = [item objectForKey:@"start_time_desc"];
                stOrder.days = [item objectForKey:@"days"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                
                [dataList addObject:stOrder];
            }
        }
    }
    
    [delegate getLatestAcceptableOnOffOrdersResult:retMsg dataList:dataList];
}

- (void)parseGetPagedAcceptableOnOffOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STWorkTimeOrderInfo* stOrder = [[STWorkTimeOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.sysinfo_fee = [[item objectForKey:@"sysinfo_fee"] doubleValue];
                stOrder.sysinfo_fee_desc = [item objectForKey:@"sysinfo_fee_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.midpoints = [[item objectForKey:@"midpoints"] intValue];
                stOrder.midpoints_desc = [item objectForKey:@"midpoints_desc"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.start_time_desc = [item objectForKey:@"start_time_desc"];
                stOrder.days = [item objectForKey:@"days"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                
                [dataList addObject:stOrder];
            }
        }
    }
    
    [delegate getPagedAcceptableOnOffOrdersResult:retMsg dataList:dataList];
}

- (void)parseGetLatestAcceptableLongOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STOrderInfo* stOrder = [[STOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.type = [[item objectForKey:@"type"] intValue];
                stOrder.type_desc = [item objectForKey:@"type_desc"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.customerNum = [[item objectForKey:@"pass_count"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.price_desc = [item objectForKey:@"price_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.state = [[item objectForKey:@"state"] intValue];
                stOrder.state_desc = [item objectForKey:@"state_desc"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.start_city = [item objectForKey:@"start_city"];
                stOrder.end_city = [item objectForKey:@"end_city"];
                
                [dataList addObject:stOrder];
            }
        }
    }
    
    [delegate getLatestAcceptableLongOrdersResult:retMsg dataList:dataList];
}

- (void)parseGetPagedAcceptableLongOrdersRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STOrderInfo* stOrder = [[STOrderInfo alloc] init];
                stOrder.uid = [[item objectForKey:@"uid"] longValue];
                stOrder.order_num = [item objectForKey:@"order_num"];
                stOrder.type = [[item objectForKey:@"type"] intValue];
                stOrder.type_desc = [item objectForKey:@"type_desc"];
                stOrder.pass_id = [[item objectForKey:@"pass_id"] intValue];
                stOrder.image = [item objectForKey:@"pass_img"];
                stOrder.name = [item objectForKey:@"pass_name"];
                stOrder.sex = [[item objectForKey:@"pass_gender"] intValue];
                stOrder.age = [[item objectForKey:@"pass_age"] intValue];
                stOrder.customerNum = [[item objectForKey:@"pass_count"] intValue];
                stOrder.price = [[item objectForKey:@"price"] doubleValue];
                stOrder.price_desc = [item objectForKey:@"price_desc"];
                stOrder.startPos = [item objectForKey:@"start_addr"];
                stOrder.endPos = [item objectForKey:@"end_addr"];
                stOrder.start_time = [item objectForKey:@"start_time"];
                stOrder.state = [[item objectForKey:@"state"] intValue];
                stOrder.state_desc = [item objectForKey:@"state_desc"];
                stOrder.create_time = [item objectForKey:@"create_time"];
                stOrder.start_city = [item objectForKey:@"start_city"];
                stOrder.end_city = [item objectForKey:@"end_city"];
                
                [dataList addObject:stOrder];
            }
        }
    }
    
    [delegate getPagedAcceptableLongOrdersResult:retMsg dataList:dataList];
}

- (void)parseGetDetailedDriverOrderInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STDetailedDrvOrderInfo *orderInfo = [[STDetailedDrvOrderInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            orderInfo.uid = [[tmp2 objectForKey:@"uid"] longValue];
            orderInfo.order_num = [tmp2 objectForKey:@"order_num"];
            //乘客列表
            NSArray * tmp3 = [tmp2 objectForKey:@"pass_list"];
            NSMutableArray * arrPass = [[NSMutableArray alloc] init];
            for(NSDictionary * passInfo in tmp3)
            {
                STPassengerInfo *passenger = [[STPassengerInfo alloc] init];
                passenger.state_desc = [passInfo objectForKey:@"state_desc"];
                passenger.uid = [[passInfo objectForKey:@"uid"] longValue];
                passenger.pverified = [[passInfo objectForKey:@"verified"] intValue];
                passenger.pverified_desc = [passInfo objectForKey:@"verified_desc"];
                passenger.age = [[passInfo objectForKey:@"age"] intValue];
                passenger.carpool_count = [[passInfo objectForKey:@"carpool_count"] intValue];
                passenger.carpool_count_desc = [passInfo objectForKey:@"carpool_count_desc"];
                passenger.eval_content = [passInfo objectForKey:@"eval_content"];
                
                passenger.evaluated = [[passInfo objectForKey:@"evaluated"] intValue];
                passenger.evaluated_desc = [passInfo objectForKey:@"evaluated_desc"];
                passenger.goodeval_rate = [[passInfo objectForKey:@"evgood_rate"] doubleValue];
                passenger.goodeval_rate_desc = [passInfo objectForKey:@"evgood_rate_desc"];
                passenger.sex = [[passInfo objectForKey:@"gender"] intValue];
                passenger.image = [passInfo objectForKey:@"img"];
                passenger.name = [passInfo objectForKey:@"name"];
                passenger.phone = [passInfo objectForKey:@"phone"];
                passenger.seat_count = [[passInfo objectForKey:@"seat_count"] intValue];
                passenger.seat_count_desc = [passInfo objectForKey:@"seat_count_desc"];
                passenger.state = [[passInfo objectForKey:@"state"] intValue];

                [arrPass addObject:passenger];
            }
            orderInfo.total_distance = [tmp2 objectForKey:@"total_distance"];
            orderInfo.pass_list = arrPass;
            orderInfo.left_seat = [[tmp2 objectForKey:@"left_seat"] intValue];
            orderInfo.total_seat =[[tmp2 objectForKey:@"total_seat"] intValue];
            orderInfo.price = [[tmp2 objectForKey:@"price"] doubleValue];
            orderInfo.sysinfo_fee = [[tmp2 objectForKey:@"sysinfo_fee"] doubleValue];
            orderInfo.sysinfo_fee_desc = [tmp2 objectForKey:@"sysinfo_fee_desc"];
            //保险部分
            orderInfo.insu_fee =[[tmp2 objectForKey:@"insu_fee"]doubleValue];
            orderInfo.insu_id =[[tmp2 objectForKey:@"insu_id"]longValue];
            orderInfo.appl_no =[tmp2 objectForKey:@"appl_no"];
            orderInfo.effect_time =[tmp2 objectForKey:@"effect_time"];
            orderInfo.insexpr_date =[tmp2 objectForKey:@"insexpr_date"];
            orderInfo.total_amount =[[tmp2 objectForKey:@"total_amount"]doubleValue];
            orderInfo.isd_name =[tmp2 objectForKey:@"isd_name"];
            orderInfo.isd_id =[[tmp2 objectForKey:@"isd_id" ]longValue];
            orderInfo.insu_status =[[tmp2 objectForKey:@"insu_status"]intValue];
            /***/
            orderInfo.start_city =[tmp2 objectForKey:@"start_city"];
            orderInfo.end_city =[tmp2 objectForKey:@"end_city"];
            orderInfo.start_addr = [tmp2 objectForKey:@"start_addr"];
            orderInfo.start_lat = [[tmp2 objectForKey:@"start_lat"] doubleValue];
            orderInfo.start_lng =[[tmp2 objectForKey:@"start_lng"] doubleValue];
            orderInfo.end_addr = [tmp2 objectForKey:@"end_addr"];
            orderInfo.end_lat =[[tmp2 objectForKey:@"end_lat"] doubleValue];
            orderInfo.end_lng =[[tmp2 objectForKey:@"end_lng"] doubleValue];
            orderInfo.left_days =[tmp2 objectForKey:@"left_days"];
            orderInfo.valid_days = [tmp2 objectForKey:@"valid_days"];
            
            NSArray * tmp4 = [tmp2 objectForKey:@"mid_points"];
            NSMutableArray * arrMidPoint = [[NSMutableArray alloc] init];
            for(NSDictionary * midInfo in tmp4)
            {
                STMidPoint *midPoint = [[STMidPoint alloc] init];
                midPoint.index = [[midInfo objectForKey:@"index"] intValue];
                midPoint.latitude = [[midInfo objectForKey:@"lat"] doubleValue];
                midPoint.longitude = [[midInfo objectForKey:@"lng"] doubleValue];
                midPoint.address = [midInfo objectForKey:@"addr"];
                [arrMidPoint addObject:midPoint];
            }
            orderInfo.mid_points = arrMidPoint;
            orderInfo.start_time = [tmp2 objectForKey:@"start_time"];
            orderInfo.create_time = [tmp2 objectForKey:@"create_time"];
            orderInfo.accept_time = [tmp2 objectForKey:@"accept_time"];
            orderInfo.state = [[tmp2 objectForKey:@"state"] intValue];
            orderInfo.state_desc = [tmp2 objectForKey:@"state_desc"];
            
            STOrderStatistics *statistics = [[STOrderStatistics alloc] init];
            NSDictionary *staInfo = [tmp2 objectForKey:@"statistics"];
            statistics.total_count = [[staInfo objectForKey:@"total_count"] intValue];
            statistics.total_count_desc = [staInfo objectForKey:@"total_count_desc"];
            statistics.total_income = [[staInfo objectForKey:@"total_income"] intValue];
            statistics.total_income_desc = [staInfo objectForKey:@"total_income_desc"];
            statistics.evgood_count = [[staInfo objectForKey:@"evgood_count"] intValue];
            statistics.evgood_count_desc = [staInfo objectForKey:@"evgood_count_desc"];
            statistics.evnormal_count = [[staInfo objectForKey:@"evnormal_count"] intValue];
            statistics.evnormal_count_desc = [staInfo objectForKey:@"evnormal_count_desc"];
            statistics.evbad_count = [[staInfo objectForKey:@"evbad_count"] intValue];
            statistics.evbad_count_desc = [staInfo objectForKey:@"evbad_count_desc"];
            orderInfo.order_statistics = statistics;
            
            
        }
    }
    if ([delegate respondsToSelector:@selector(getDetailedDriverOrderInfoResult:OrderInfo:)]){
         [delegate getDetailedDriverOrderInfoResult:retMsg OrderInfo:orderInfo];
    }
   
}

- (void)parseGetDetailedCustomerOrderInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STDetailedCusOrderInfo *orderInfo = [[STDetailedCusOrderInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            orderInfo.uid = [[tmp2 objectForKey:@"uid"] longValue];
            orderInfo.order_num = [tmp2 objectForKey:@"order_num"];
            
            NSDictionary *tmp3 = [tmp2 objectForKey:@"driver_info"];
            STDriverInfo *driver_info = [[STDriverInfo alloc] init];
            driver_info.uid = [[tmp3 objectForKey:@"uid"] longValue];
            driver_info.image = [tmp3 objectForKey:@"img"];
            driver_info.name = [tmp3 objectForKey:@"name"];
            driver_info.sex = [[tmp3 objectForKey:@"gender"] intValue];
            driver_info.age = [[tmp3 objectForKey:@"age"] intValue];
            driver_info.phone = [tmp3 objectForKey:@"phone"];
            driver_info.carimg = [tmp3 objectForKey:@"carimg"];
            driver_info.drv_career = [[tmp3 objectForKey:@"drv_career"] intValue];
            driver_info.drv_career_desc = [tmp3 objectForKey:@"drv_career"];
            driver_info.goodeval_rate = [[tmp3 objectForKey:@"evgood_rate"] intValue];
            driver_info.goodeval_rate_desc = [tmp3 objectForKey:@"evgood_rate_desc"];
            driver_info.carpool_count = [[tmp3 objectForKey:@"carpool_count"] intValue];
            driver_info.carpool_count_desc = [tmp3 objectForKey:@"carpool_count_desc"];
            driver_info.carno = [tmp3 objectForKey:@"carno"];
            driver_info.car_brand = [tmp3 objectForKey:@"brand"];
            driver_info.car_style = [tmp3 objectForKey:@"style"];
            driver_info.car_type = [[tmp3 objectForKey:@"type"] intValue];
            driver_info.color = [tmp3 objectForKey:@"color"];
            orderInfo.driver_info = driver_info;
            
            orderInfo.price = [[tmp2 objectForKey:@"price"] doubleValue];
            orderInfo.start_addr = [tmp2 objectForKey:@"start_addr"];
            orderInfo.end_addr = [tmp2 objectForKey:@"end_addr"];
            orderInfo.start_lat = [[tmp2 objectForKey:@"start_lat"] doubleValue];
            orderInfo.start_lng = [[tmp2 objectForKey:@"start_lng"] doubleValue];
            orderInfo.end_lat = [[tmp2 objectForKey:@"end_lat"] doubleValue];
            orderInfo.end_lng = [[tmp2 objectForKey:@"end_lng"] doubleValue];
            orderInfo.valid_days = [tmp2 objectForKey:@"days"];
            orderInfo.left_days = [tmp2 objectForKey:@"leftdays"];
			
			NSArray * tmp4 = [tmp2 objectForKey:@"mid_points"];
            NSMutableArray * arrMidPoint = [[NSMutableArray alloc] init];
            for(NSDictionary * midInfo in tmp4)
            {
                STMidPoint *midPoint = [[STMidPoint alloc] init];
                midPoint.index = [[midInfo objectForKey:@"index"] intValue];
                midPoint.latitude = [[midInfo objectForKey:@"lat"] doubleValue];
                midPoint.longitude = [[midInfo objectForKey:@"lng"] doubleValue];
                midPoint.address = [midInfo objectForKey:@"addr"];
                [arrMidPoint addObject:midPoint];
            }
            orderInfo.mid_points = arrMidPoint;
            
            //保险部分
            orderInfo.insu_fee =[[tmp2 objectForKey:@"insu_fee"]doubleValue];
            orderInfo.insu_id =[[tmp2 objectForKey:@"insu_id"]longValue];
            orderInfo.appl_no =[tmp2 objectForKey:@"appl_no"];
            orderInfo.effect_time =[tmp2 objectForKey:@"effect_time"];
            orderInfo.insexpr_date =[tmp2 objectForKey:@"insexpr_date"];
            orderInfo.total_amount =[[tmp2 objectForKey:@"total_amount"]doubleValue];
            orderInfo.isd_name =[tmp2 objectForKey:@"isd_name"];
            orderInfo.isd_id =[[tmp2 objectForKey:@"isd_id" ]longValue];
            orderInfo.insu_status =[[tmp2 objectForKey:@"insu_status"]intValue];
            /***/


            orderInfo.start_time = [tmp2 objectForKey:@"start_time"];
            orderInfo.accept_time = [tmp2 objectForKey:@"accept_time"];
            orderInfo.startsrv_time = [tmp2 objectForKey:@"startsrv_time"];
            orderInfo.state = [[tmp2 objectForKey:@"state"] intValue];
            orderInfo.state_desc = [tmp2 objectForKey:@"state_desc"];
            orderInfo.password = [tmp2 objectForKey:@"password"];
            
            
            orderInfo.cancelled_balance = [[tmp2 objectForKey:@"cancelled_balance"] longValue];
            orderInfo.cancelled_balance_desc = [tmp2 objectForKey:@"cancelled_balance_desc"];
            orderInfo.time_interval_desc = [tmp2 objectForKey:@"time_interval_desc"];
			orderInfo.balance_desc = [tmp2 objectForKey:@"balance_desc"];

			orderInfo.evaluated = [[tmp2 objectForKey:@"evaluated"] intValue];
			orderInfo.eval_content = [tmp2 objectForKey:@"eval_content"];
		}
	}

	if([delegate respondsToSelector:@selector(getDetailedCustomerOrderInfoResult:OrderInfo:)])
	{
		[delegate getDetailedCustomerOrderInfoResult:retMsg OrderInfo:orderInfo];
	}
}

- (void)parseGetDriverInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STDriverInfo *driverInfo = [[STDriverInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            driverInfo.uid = [[tmp2 objectForKey:@"id"] longValue];
            driverInfo.name = [tmp2 objectForKey:@"name"];
            driverInfo.image = [tmp2 objectForKey:@"img"];
            driverInfo.sex = [[tmp2 objectForKey:@"gender"] intValue];
            driverInfo.age = [[tmp2 objectForKey:@"age"] intValue];
            driverInfo.drv_career = [[tmp2 objectForKey:@"drv_career"] intValue];
            driverInfo.drv_career_desc = [tmp2 objectForKey:@"drv_career_desc"];
            driverInfo.goodeval_rate = [[tmp2 objectForKey:@"goodeval_rate"] intValue];
            driverInfo.goodeval_rate_desc = [tmp2 objectForKey:@"goodeval_rate_desc"];
            driverInfo.carpool_count = [[tmp2 objectForKey:@"carpool_count"] intValue];
            driverInfo.carpool_count_desc = [tmp2 objectForKey:@"carpool_count_desc"];
            
            STCarInfo *carInfo = [[STCarInfo alloc] init];
            NSDictionary * tmp3 = [tmp2 objectForKey:@"carinfo"];
            carInfo.carimg = [tmp3 objectForKey:@"carimg"];
            carInfo.brand = [tmp3 objectForKey:@"brand"];
            carInfo.type = [tmp3 objectForKey:@"type"];
            carInfo.style = [[tmp3 objectForKey:@"style"] intValue];
            carInfo.color = [tmp3 objectForKey:@"color"];
            driverInfo.carinfo = carInfo;
            
            NSMutableArray *arrEval = [[NSMutableArray alloc] init];
            NSArray *tmp4 = [tmp2 objectForKey:@"eval"];
            for(NSDictionary *dicEval in tmp4)
            {
                STDriverEvalInfo *eval_info = [[STDriverEvalInfo alloc] init];
                eval_info.uid = [[dicEval objectForKey:@"uid"] longValue];
                eval_info.pass_name = [dicEval objectForKey:@"pass_name"];
                eval_info.eval = [[dicEval objectForKey:@"eval"] intValue];
                eval_info.eval_desc = [dicEval objectForKey:@"eval_desc"];
                eval_info.time = [dicEval objectForKey:@"time"];
                eval_info.contents = [dicEval objectForKey:@"contents"];
                [arrEval addObject:eval_info];
            }
            driverInfo.evals = arrEval;
        }
    }
    
    [delegate getDriverInfoResult:retMsg DriverInfo:driverInfo];
}

- (void)parseGetDriverLatestEvalInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray *dataList = [[NSMutableArray alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSArray * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            for(NSDictionary *dicEval in tmp2)
            {
                STDriverEvalInfo *eval_info = [[STDriverEvalInfo alloc] init];
                eval_info.uid = [[dicEval objectForKey:@"uid"] longValue];
                eval_info.pass_name = [dicEval objectForKey:@"pass_name"];
                eval_info.eval = [[dicEval objectForKey:@"eval"] intValue];
                eval_info.eval_desc = [dicEval objectForKey:@"eval_desc"];
                eval_info.time = [dicEval objectForKey:@"time"];
                eval_info.contents = [dicEval objectForKey:@"contents"];
                [dataList addObject:eval_info];
            }
        }
    }
    
    [delegate getDriverLatestEvalInfoResult:retMsg dataList:dataList];
}

- (void)parseGetDriverPagedEvalInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray *dataList = [[NSMutableArray alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSArray * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            for(NSDictionary *dicEval in tmp2)
            {
                STDriverEvalInfo *eval_info = [[STDriverEvalInfo alloc] init];
                eval_info.uid = [[dicEval objectForKey:@"uid"] longValue];
                eval_info.pass_name = [dicEval objectForKey:@"pass_name"];
                eval_info.eval = [[dicEval objectForKey:@"eval"] intValue];
                eval_info.eval_desc = [dicEval objectForKey:@"eval_desc"];
                eval_info.time = [dicEval objectForKey:@"time"];
                eval_info.contents = [dicEval objectForKey:@"contents"];
                [dataList addObject:eval_info];
            }
        }
    }
    
    [delegate getDriverPagedEvalInfoResult:retMsg dataList:dataList];
}
- (void)parseGetTime_left:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSDictionary *dic;
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        if (jsonRet !=SVCERR_SUCCESS) {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        }else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            dic=tmp2;
        }
    }
    [delegate getTime_left:retMsg RetData:dic];
}
- (void)parseGetPassengerInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STPassengerInfo *passengerInfo = [[STPassengerInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            passengerInfo.uid = [[tmp2 objectForKey:@"id"] longValue];
            passengerInfo.name = [tmp2 objectForKey:@"name"];
            passengerInfo.image = [tmp2 objectForKey:@"img"];
            passengerInfo.sex = [[tmp2 objectForKey:@"gender"] intValue];
            passengerInfo.age = [[tmp2 objectForKey:@"age"] intValue];
            passengerInfo.pverified = [[tmp2 objectForKey:@"pverified"] intValue];
            passengerInfo.pverified_desc = [tmp2 objectForKey:@"pverified_desc"];
            passengerInfo.goodeval_rate = [[tmp2 objectForKey:@"goodeval_rate"] intValue];
            passengerInfo.goodeval_rate_desc = [tmp2 objectForKey:@"goodeval_rate_desc"];
            passengerInfo.carpool_count = [[tmp2 objectForKey:@"carpool_count"] intValue];
            passengerInfo.carpool_count_desc = [tmp2 objectForKey:@"carpool_count_desc"];
            
            NSMutableArray *arrEval = [[NSMutableArray alloc] init];
            NSArray *tmp4 = [tmp2 objectForKey:@"eval"];
            for(NSDictionary *dicEval in tmp4)
            {
                STPassengerEvalInfo *eval_info = [[STPassengerEvalInfo alloc] init];
                eval_info.uid = [[dicEval objectForKey:@"uid"] longValue];
                eval_info.driver_name = [dicEval objectForKey:@"driver_name"];
                eval_info.eval = [[dicEval objectForKey:@"eval"] intValue];
                eval_info.eval_desc = [dicEval objectForKey:@"eval_desc"];
                eval_info.time = [dicEval objectForKey:@"time"];
                eval_info.contents = [dicEval objectForKey:@"contents"];
                [arrEval addObject:eval_info];
            }
            passengerInfo.evals = arrEval;
        }
    }
    if ([delegate respondsToSelector:@selector(getPassengerInfoResult:PassengerInfo:)]) {
        [delegate getPassengerInfoResult:retMsg PassengerInfo:passengerInfo];
    }
    
}

- (void)parseGetPassengerLatestEvalInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray *dataList = [[NSMutableArray alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSArray * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            for(NSDictionary *dicEval in tmp2)
            {
                STPassengerEvalInfo *eval_info = [[STPassengerEvalInfo alloc] init];
                eval_info.uid = [[dicEval objectForKey:@"uid"] longValue];
                eval_info.driver_name = [dicEval objectForKey:@"driver_name"];
                eval_info.eval = [[dicEval objectForKey:@"eval"] intValue];
                eval_info.eval_desc = [dicEval objectForKey:@"eval_desc"];
                eval_info.time = [dicEval objectForKey:@"time"];
                eval_info.contents = [dicEval objectForKey:@"contents"];
                [dataList addObject:eval_info];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getPassengerLatestEvalInfoResult:dataList:)]) {
        [delegate getPassengerLatestEvalInfoResult:retMsg dataList:dataList];
    }
    
}

- (void)parseGetPassengerPagedEvalInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray *dataList = [[NSMutableArray alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSArray * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            for(NSDictionary *dicEval in tmp2)
            {
                STPassengerEvalInfo *eval_info = [[STPassengerEvalInfo alloc] init];
                eval_info.uid = [[dicEval objectForKey:@"uid"] longValue];
                eval_info.driver_name = [dicEval objectForKey:@"driver_name"];
                eval_info.eval = [[dicEval objectForKey:@"eval"] intValue];
                eval_info.eval_desc = [dicEval objectForKey:@"eval_desc"];
                eval_info.time = [dicEval objectForKey:@"time"];
                eval_info.contents = [dicEval objectForKey:@"contents"];
                [dataList addObject:eval_info];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getPassengerPagedEvalInfoResult:dataList:)]) {
        [delegate getPassengerPagedEvalInfoResult:retMsg dataList:dataList];
    }
    
}
-(void)parsePublishOnceOrderAddPrice:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:@"retmsg"];
        }
    }
    if ([delegate respondsToSelector:@selector(publishOnceOrderAddPrice:retData:)]) {
        [delegate publishOnceOrderAddPrice:retMsg retData:nil];
    }
    
}
- (void)parsePublishOnceOrderRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STPubOnceOrderRet *pubOnceOrderRet = [[STPubOnceOrderRet alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];

			// check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }			
        }
        else
        {
            NSDictionary *retData = [tmp1 objectForKey:SVCC_DATA];
			
			pubOnceOrderRet.order_id = [[retData objectForKey:@"order_id"] longValue];
			pubOnceOrderRet.wait_time = [[retData objectForKey:@"wait_time"] intValue];
			pubOnceOrderRet.driver_lock_time = [[retData objectForKey:@"driver_lock_time"] intValue];
			pubOnceOrderRet.add_price_time1 = [[retData objectForKey:@"add_price_time1"] intValue];
			pubOnceOrderRet.add_price_time2 = [[retData objectForKey:@"add_price_time2"] intValue];
			pubOnceOrderRet.add_price_time3 = [[retData objectForKey:@"add_price_time3"] intValue];
			pubOnceOrderRet.add_price_time4 = [[retData objectForKey:@"add_price_time4"] intValue];
			pubOnceOrderRet.add_price_time5 = [[retData objectForKey:@"add_price_time5"] intValue];
			pubOnceOrderRet.same_price_time1 = [[retData objectForKey:@"same_price_time1"] intValue];
			pubOnceOrderRet.same_price_time2 = [[retData objectForKey:@"same_price_time2"] intValue];
			pubOnceOrderRet.same_price_time3 = [[retData objectForKey:@"same_price_time3"] intValue];
			pubOnceOrderRet.same_price_time4 = [[retData objectForKey:@"same_price_time4"] intValue];
			pubOnceOrderRet.same_price_time5 = [[retData objectForKey:@"same_price_time5"] intValue];
            pubOnceOrderRet.drvcount = [[retData objectForKey:@"drvcount"] intValue];
            pubOnceOrderRet.add_price_default = [[retData objectForKey:@"add_price_default"] intValue];
            pubOnceOrderRet.add_price_range = [[retData objectForKey:@"add_price_range"] intValue];
            pubOnceOrderRet.prompt_1st = [retData objectForKey:@"prompt_1st"];
            pubOnceOrderRet.prompt = [retData objectForKey:@"prompt"];
            pubOnceOrderRet.add_price_min = [[retData objectForKey:@"add_price_min"] intValue];
            pubOnceOrderRet.cut_price_range = [[retData objectForKey:@"cut_price_range"] intValue];
        }
    }
    if ([delegate respondsToSelector:@selector(publishOnceOrderResult:RetData:)]) {
        [delegate publishOnceOrderResult:retMsg RetData:pubOnceOrderRet];
    }
    
}
//////  car_style" : 0  是出租车价格
- (void)parseGetSystemPriceInfo:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;

    NSMutableArray *arryM =[[NSMutableArray alloc]init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSArray *temp2 =[tmp1 objectForKey:SVCC_DATA];
            //car_style" : 0  是出租车价格
            for(NSDictionary *item in temp2)
            {
            SystemPriceInfoResult   *priceInfor =[[SystemPriceInfoResult alloc]initWithDict:item];
                [arryM addObject:priceInfor];
            }
			
        }
    }
    if ([delegate respondsToSelector:@selector(getsystemPriceInfoResult:RetData:)]) {
        [delegate getsystemPriceInfoResult:retMsg RetData:arryM];
    }
    
}

- (void)parsePublishOnOffOrderRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [delegate publishOnOffOrderResult:retMsg];
}

- (void)parseAcceptOnceOrderRes:(NSString *)responseStr
{
	NSError * e;
	NSString * retMsg = SVCERR_MSG_SUCCESS;
	NSInteger jsonRet = SVCERR_FAILURE;
	int wait_time = 0;

	if (responseStr)
	{
		NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];

		NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];

		// get service result
		jsonRet = [[tmp1 objectForKey:@"retcode"] intValue];

		// check result
		if (jsonRet == SVCERR_SUCCESS)
        {
			NSDictionary* retdata = [tmp1 objectForKey:SVCC_DATA];
			wait_time = [[retdata objectForKey:@"wait_time"] intValue];
		} else {
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
		}
    }


	if ([delegate respondsToSelector:@selector(acceptOnceOrderResult:retmsg:wait_time:)])
	{
		[delegate acceptOnceOrderResult:jsonRet retmsg:retMsg wait_time:wait_time];
	}

}

- (void)parseAcceptOnOffOrderRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [delegate acceptOnOffOrderResult:retMsg];
}

- (void)parseGetAcceptableInCityOrderDetailInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STDetailedDrvInCityOrderInfo *orderInfo = [[STDetailedDrvInCityOrderInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            NSDictionary * tmp3 = [tmp2 objectForKey:@"pass_info"];
            STPassengerInfo *passenger = [[STPassengerInfo alloc] init];
            passenger.uid = [[tmp3 objectForKey:@"pass_id"] longValue];
            passenger.name = [tmp3 objectForKey:@"pass_name"];
            passenger.sex = [[tmp3 objectForKey:@"pass_gender"] intValue];
            passenger.age = [[tmp3 objectForKey:@"pass_age"] intValue];
            passenger.image = [tmp3 objectForKey:@"pass_img"];
            passenger.pverified = [[tmp3 objectForKey:@"verified"] intValue];
            passenger.pverified_desc = [tmp3 objectForKey:@"verified_desc"];
            passenger.goodeval_rate = [[tmp3 objectForKey:@"evgood_rate"] doubleValue];
            passenger.goodeval_rate_desc = [tmp3 objectForKey:@"evgood_rate_desc"];
            passenger.carpool_count = [[tmp3 objectForKey:@"carpool_count"] intValue];
            passenger.carpool_count_desc = [tmp3 objectForKey:@"carpool_count_desc"];
            orderInfo.pass_info = passenger;

            orderInfo.start_addr = [tmp2 objectForKey:@"start_addr"];
            orderInfo.start_lat =[[tmp2 objectForKey:@"start_lat"]doubleValue];
            orderInfo.start_lng =[[tmp2 objectForKey:@"start_lng"]doubleValue];
            orderInfo.end_addr = [tmp2 objectForKey:@"end_addr"];
            orderInfo.end_lat =[[tmp2 objectForKey:@"end_lat"]doubleValue];
            orderInfo.end_lng =[[tmp2 objectForKey:@"end_lng"]doubleValue];
            
            orderInfo.start_time = [tmp2 objectForKey:@"start_time"];
            
            NSArray * tmp4 = [tmp2 objectForKey:@"mid_points"];
            NSMutableArray * arrMidPoint = [[NSMutableArray alloc] init];
            for(NSDictionary * midInfo in tmp4)
            {
                STMidPoint *midPoint = [[STMidPoint alloc] init];
                midPoint.index = [[midInfo objectForKey:@"index"] intValue];
                midPoint.latitude = [[midInfo objectForKey:@"lat"] doubleValue];
                midPoint.longitude = [[midInfo objectForKey:@"lng"] doubleValue];
                midPoint.address = [midInfo objectForKey:@"addr"];
                [arrMidPoint addObject:midPoint];
            }
            orderInfo.mid_points = arrMidPoint;
            
            orderInfo.price = [[tmp2 objectForKey:@"price"] doubleValue];
            orderInfo.price_desc = [tmp2 objectForKey:@"price_desc"];
            orderInfo.sysinfo_fee = [[tmp2 objectForKey:@"sysinfo_fee"] doubleValue];
            orderInfo.sysinfo_fee_desc = [tmp2 objectForKey:@"sysinfo_fee_desc"];
            orderInfo.insu_fee =[[tmp2 objectForKey:@"insu_fee"]doubleValue];
            orderInfo.valid_days = [tmp2 objectForKey:@"valid_days"];
            
        }
    }
    
    [delegate getAcceptableInCityOrderDetailInfoResult:retMsg OrderInfo:orderInfo];
}


- (void)parseCheckOnceOrderAcceptanceRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STDriverInfo * driverInfo = [[STDriverInfo alloc] init];
	STSingleTimeOrderInfo * orderInfo = [[STSingleTimeOrderInfo alloc] init];
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
            driverInfo.image = [tmp2 objectForKey:@"img"];
            driverInfo.name = [tmp2 objectForKey:@"name"];
            driverInfo.sex = [[tmp2 objectForKey:@"gender"] intValue];
            driverInfo.age = [[tmp2 objectForKey:@"age"] intValue];
            driverInfo.carimg = [tmp2 objectForKey:@"carimg"];
            driverInfo.drv_career = [[tmp2 objectForKey:@"drv_career"] intValue];
            driverInfo.drv_career_desc = [tmp2 objectForKey:@"drv_career"];
            driverInfo.goodeval_rate = [[tmp2 objectForKey:@"evgood_rate"] intValue];
            driverInfo.goodeval_rate_desc = [tmp2 objectForKey:@"evgood_rate_desc"];
            driverInfo.carpool_count = [[tmp2 objectForKey:@"carpool_count"] intValue];
            driverInfo.carpool_count_desc = [tmp2 objectForKey:@"carpool_count_desc"];
            driverInfo.carno = [tmp2 objectForKey:@"carno"];
            driverInfo.car_brand = [tmp2 objectForKey:@"brand"];
            driverInfo.car_style = [tmp2 objectForKey:@"style"];
            driverInfo.car_type = [[tmp2 objectForKey:@"type"] intValue];
            driverInfo.uid = [[tmp2 objectForKey:@"drvid"] longValue];
            
			orderInfo.distance = [[tmp2 objectForKey:@"distance"] doubleValue];
			orderInfo.distance_desc = [tmp2 objectForKey:@"distance_desc"];
            orderInfo.startPos = [tmp2 objectForKey:@"start_addr"];
			orderInfo.start_lat = [[tmp2 objectForKey:@"start_lat"] doubleValue];
			orderInfo.start_lng = [[tmp2 objectForKey:@"start_lng"] doubleValue];
            orderInfo.endPos = [tmp2 objectForKey:@"end_addr"];
			orderInfo.end_lat = [[tmp2 objectForKey:@"end_lat"] doubleValue];
			orderInfo.end_lng = [[tmp2 objectForKey:@"end_lng"] doubleValue];
            orderInfo.price =[[tmp2 objectForKey:@"price"]doubleValue];

			NSArray * tmp4 = [tmp2 objectForKey:@"midpoints"];
            NSMutableArray * arrMidPoint = [[NSMutableArray alloc] init];
            for(NSDictionary * midInfo in tmp4)
            {
                STMidPoint *midPoint = [[STMidPoint alloc] init];
                midPoint.index = [[midInfo objectForKey:@"index"] intValue];
                midPoint.latitude = [[midInfo objectForKey:@"lat"] doubleValue];
                midPoint.longitude = [[midInfo objectForKey:@"lng"] doubleValue];
                midPoint.address = [midInfo objectForKey:@"addr"];
                [arrMidPoint addObject:midPoint];
            }
            orderInfo.mid_points = arrMidPoint;
			
        }
        [[NSNotificationCenter defaultCenter] postNotificationName:@"checkOnceOrderAcceptanceResult" object:self userInfo:tmp];
    }
    
    if([self.delegate respondsToSelector:@selector(checkOnceOrderAcceptanceResult:DriverInfo:OrderInfo:)])
    {
      [delegate checkOnceOrderAcceptanceResult:retMsg DriverInfo:driverInfo OrderInfo:orderInfo];
    }
   
}

- (void)parseCheckOnceOrderAgreeRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSString * pass_img;
	NSString * pass_name;
	int  	pass_gender;
	int  	pass_age;
	NSString * start_time;
	NSString * start_addr;
	NSString * end_addr;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == -7) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
			pass_img = [tmp2 objectForKey:@"pass_img"];
			pass_name = [tmp2 objectForKey:@"pass_name"];
			pass_gender = [[tmp2 objectForKey:@"pass_gender"] intValue];
			pass_age = [[tmp2 objectForKey:@"pass_age"] intValue];
			start_time = [tmp2 objectForKey:@"start_time"];
			start_addr = [tmp2 objectForKey:@"start_addr"];
			end_addr = [tmp2 objectForKey:@"end_addr"];
        }
    }
    
    if([delegate respondsToSelector:@selector(checkOnceOrderAgreeResult:PassImg:PassName:PassGender:PassAge:StartTime:StartAddr:EndAddr:)])
    {
      [delegate checkOnceOrderAgreeResult:retMsg PassImg:pass_img PassName:pass_name PassGender:pass_gender PassAge:pass_age StartTime:start_time StartAddr:start_addr EndAddr:end_addr];
    }
    
	
}
/**
 *  确认订单  ma
 *
 *  @param responseStr <#responseStr description#>
 */
- (void)parseConfirmOnceOrderRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            //
            if(jsonRet == NotEnoughLvDian)
            {
                [delegate confirmOnceOrderResult:@"绿点不足"];
                return;
            }
         
            [delegate duplicateUser:retMsg];
            return;
            
        }
        else
        {
            
        }
    }

	[delegate confirmOnceOrderResult:retMsg ];
	
}

/**
 *  <#Description#>
 *
 *  @param responseStr <#responseStr description#>
 */
- (void)parseSetOnceOrderPasswordRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STDriverInfo *driverInfo = [[STDriverInfo alloc] init];
	STSingleTimeOrderInfo *orderInfo = [[STSingleTimeOrderInfo alloc] init];

    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
			driverInfo.uid = [[tmp2 objectForKey:@"drv_id"] longValue];
			driverInfo.image = [tmp2 objectForKey:@"drv_img"];
            driverInfo.name = [tmp2 objectForKey:@"drv_name"];
            driverInfo.sex = [[tmp2 objectForKey:@"drv_gender"] intValue];
            driverInfo.age = [[tmp2 objectForKey:@"drv_age"] intValue];
            driverInfo.carimg = [tmp2 objectForKey:@"car_img"];
            driverInfo.carno = [tmp2 objectForKey:@"carno"];
            driverInfo.car_brand = [tmp2 objectForKey:@"car_brand"];
            driverInfo.car_type = [[tmp2 objectForKey:@"car_type"] intValue];
            
			orderInfo.distance = [[tmp2 objectForKey:@"dist"] doubleValue];
			orderInfo.distance_desc = [tmp2 objectForKey:@"dist_desc"];
            orderInfo.startPos = [tmp2 objectForKey:@"start_addr"];
            orderInfo.endPos = [tmp2 objectForKey:@"end_addr"];
			orderInfo.start_time = [tmp2 objectForKey:@"start_time"];
			orderInfo.password = [tmp2 objectForKey:@"password"];
        }
    }

	[delegate setOnceOrderPasswordResult:retMsg DriverInfo:driverInfo OrderInfo:orderInfo];
	
}

- (void)parseConfirmOnOffOrderRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STDriverInfo *driverInfo = [[STDriverInfo alloc] init];
	STWorkTimeOrderInfo *orderInfo = [[STWorkTimeOrderInfo alloc] init];
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
			driverInfo.uid = [[tmp2 objectForKey:@"drv_id"] longValue];
			driverInfo.image = [tmp2 objectForKey:@"drv_img"];
            driverInfo.name = [tmp2 objectForKey:@"drv_name"];
            driverInfo.sex = [[tmp2 objectForKey:@"drv_gender"] intValue];
            driverInfo.age = [[tmp2 objectForKey:@"drv_age"] intValue];
            driverInfo.carimg = [tmp2 objectForKey:@"car_img"];
            driverInfo.carno = [tmp2 objectForKey:@"carno"];
            driverInfo.car_brand = [tmp2 objectForKey:@"car_brand"];
            driverInfo.car_type = [[tmp2 objectForKey:@"car_type"] intValue];
            
			orderInfo.distance = [[tmp2 objectForKey:@"dist"] doubleValue];
			orderInfo.distance_desc = [tmp2 objectForKey:@"dist_desc"];
            orderInfo.startPos = [tmp2 objectForKey:@"start_addr"];
            orderInfo.endPos = [tmp2 objectForKey:@"end_addr"];
			orderInfo.start_time = [tmp2 objectForKey:@"start_time"];
        }
    }
	
	[delegate confirmOnOffOrderResult:retMsg DriverInfo:driverInfo OrderInfo:orderInfo];
	
}

- (void)parseCancelOnceOrderRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    if([self.delegate respondsToSelector:@selector(cancelOnceOrderResult:)])
    {
        [delegate cancelOnceOrderResult:retMsg];

    }
}

- (void)parseCancelOnOffOrderRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
	
	[delegate cancelOnOffOrderResult:retMsg];
}

- (void)parseRefuseOnOffOrderRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
	
	[delegate refuseOnOffOrderResult:retMsg];
}

- (void)parsePauseOnOffOrderRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
	
	[delegate pauseOnOffOrderResult:retMsg];
}

- (void)parseDriverPosRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSString * name = @"";
    double lat = 0.0;
    double lng = 0.0;
    NSString * time = @"";
	
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
            
			name = [tmp2 objectForKey:@"driver_name"];
            lat = [[tmp2 objectForKey:@"lat"] doubleValue];
			lng = [[tmp2 objectForKey:@"lng"] doubleValue];
			time = [tmp2 objectForKey:@"time"];
        }
    }
	
	[delegate getDriverPosResult:retMsg driver_name:name lat:lat lng:lng time:time];
}




- (void)parseGetDetailedDriverCancelOrderInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSDictionary * tmp2 ;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            tmp2 = [tmp1 objectForKey:SVCC_DATA];
        }
    }
    
    [delegate getDriverCancelResult:retMsg RetData:tmp2];
}
-(void)parseGetLongOrderCancelInfoRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSDictionary * tmp2 ;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            tmp2 = [tmp1 objectForKey:SVCC_DATA];
        }
    }
    
    [delegate getLongOrderCancelInfo:retMsg RetData:tmp2];
}
-(void)Clickchargingbtn:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSDictionary * tmp2 ;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            tmp2 = [tmp1 objectForKey:SVCC_DATA];
        }
    }
    [delegate getClickchargingbtn:retMsg RetData:tmp2];
}
-(void)has_clickedchargingbtn:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSDictionary * tmp2 ;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
            
            // check duplicate login state
            [delegate duplicateUser:retMsg];
        }
        else
        {
            tmp2 = [tmp1 objectForKey:SVCC_DATA];
        }
    }
    if ([delegate respondsToSelector:@selector(getHas_clickedchargingbtn:RetData:)]) {
        [delegate getHas_clickedchargingbtn:retMsg RetData:tmp2];
    }
    
}
@end
