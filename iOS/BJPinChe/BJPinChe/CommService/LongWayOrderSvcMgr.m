//
//  LongWayOrderSvcMgr.m
//  BJPinChe
//
//  Created by CKK on 14-8-26.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "LongWayOrderSvcMgr.h"
#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "ServiceMethod.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"

@implementation LongWayOrderSvcMgr
//长途订单可抢分页
- (void) ExecuteLongOrder : (long)userid orderid:(long)orderid devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdExecuteLongOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devToken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseExecuteLongOrderRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate executeLongOrderResult:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


- (void) SignLongOrderDriverArrival : (long)driverid orderid:(long)orderid devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignLongOrderDriverArrival]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devToken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignLongOrderDriverArrivalRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate signLongOrderDriverArrivalResult:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) SignLongOrderPassengerUpload : (long)driverid orderid:(long)orderid passid:(long)passid password:(NSString *)password devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignLongOrderPassengerUpload]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [NSString stringWithFormat:@"%ld", passid], @"passid",
                            password, @"password",
                            devToken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignLongOrderPassUploadRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate signLongOrderPassengerUpload:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) SignLongOrderPassengerGiveup : (long)driverid orderid:(long)orderid passid:(long)passid devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignLongOrderPassengerGiveup]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [NSString stringWithFormat:@"%ld", passid], @"passid",
                            devToken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignLongOrderPassGiveupRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate signLongOrderPassengerGiveup:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) StartLongOrderDriving : (long)driverid orderid:(long)orderid devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdStartLongOrderDriving]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devToken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseStartLongOrderDrivingRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate startLongOrderDriving:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) EndLongOrder : (long)driverid orderid:(long)orderid devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEndLongOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devToken, @"devtoken",
                            nil];
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
	 	 [self parseEndLongOrderRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate endLongOrder:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) EvaluateLongOrderPass : (long)driverid passid:(long)passid orderid:(long)orderid level:(int)level msg:(NSString *)msg devToken:(NSString *)devToken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEvaluateLongOrderPass]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [NSString stringWithFormat:@"%ld", passid], @"passid",
                            [NSString stringWithFormat:@"%d", level], @"level",
                            msg, @"msg",
                            devToken, @"devtoken",
                            nil];
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
	 	 [self parseEvaluateLongOrderPassRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate evaluateLongOrderPass:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) EvaluateLongOrderDriver : (long)passid orderid:(long)orderid level:(int)level msg:(NSString *)msg devToken:(NSString *)devToken driverId:(long)driverid
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEvaluateLongOrderDriver]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            [NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%d", level], @"level",
                            msg, @"msg",
                            devToken, @"devtoken",
                            nil];
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
	 	 [self parseEvaluateLongOrderDriverRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [_delegate evaluateLongOrderDriver:SVCERR_MSG_ERROR];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


- (void) GetPagedAcceptableLongOrdersWithUserid:(long)userid PageNo:(int)pageno StartAddr:(NSString *)start_addr  EndAddr:(NSString *)end_addr DevToken:(NSString*)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedAcceptableLongOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"pageno": [NSString stringWithFormat:@"%d",pageno],
                              @"start_addr": start_addr,
                              @"end_addr": end_addr,
                              @"devtoken": devtoken
                              };
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetPagedAcceptableLongOrdersRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [_delegate GetPagedAcceptableLongOrders:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
    
    
    
}
//最新可抢长途订单
-(void)GetLatestAcceptableLongOrdersWithUserid:(long)userid Limitid:(NSString *)limitid StartAddr:(NSString *)start_addr EndAddr:(NSString *)end_addr DevToken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestAcceptableLongOrders]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"limitid": limitid,
                              @"start_addr": start_addr,
                              @"end_addr": end_addr,
                              @"devtoken": devtoken
                                  };
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetLatestAcceptableLongOrdersRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [_delegate GetLatestAcceptableLongOrders:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

//长途订单发布
- (void)PublishLongOrderWithUserID:(long)userid AndStartCity:(NSString *)startCity AndStartAddr:(NSString *)startAddr AndEndCity:(NSString *)endCity AndEndAddr:(NSString *)endAddr AndStartlat:(double) startLat AndStartLng:(double)startLng AndEndLat:(double)endLat AndEndLng:(double)endLng AndStartTime:(NSString *)startTime AndRemark:(NSString *)remark AndPrice:(NSString *)price AndSeatsCount:(int)seatsCount AndDevtoken:(NSString *)devToken
{
    
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdPublishLongOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"start_addr": startAddr,
                              @"start_city":startCity ,
                              @"start_lat": [NSString stringWithFormat:@"%f",startLat],
                              @"start_lng":[NSString stringWithFormat:@"%f",startLng] ,
                              @"end_addr": endAddr,
                              @"end_city": endCity,
                              @"end_lat": [NSString stringWithFormat:@"%f",endLat],
                              @"end_lng": [NSString stringWithFormat:@"%f",endLng],
                              @"start_time": startTime,
                              @"remark":remark,
                              @"price":price,
                              @"seats_count":[NSString stringWithFormat:@"%d",seatsCount],
                              @"devtoken": devToken
                              };
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parsePublishLongOrder:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         
         [_delegate PublishLongOrder:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];

}
//长途抢单
- (void)AcceptLongOrderWithUserID:(long)userid AndOrderId:(long)orderid AndSeatsCount:(int) seatsCount
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdAcceptLongOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"orderid":[NSNumber numberWithLong:orderid],
                              @"seats_count":[NSString stringWithFormat:@"%d",seatsCount],
                              @"devtoken": [Common getDeviceMacAddress]
                              };
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAcceptLongOrder:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         
		 [_delegate AcceptLongOrder:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];

}

//长途抢单设定密码
#pragma mark 长途抢单 与服务器连接设定密码
- (void)SetLongOrderPasswordWithUserID:(long)userid AndOrderId:(long)orderid AndPassword:(NSString *)password
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSetLongOrderPassword]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"orderid":[NSNumber numberWithLong:orderid],
                              @"password":password,
                              @"devtoken": [Common getDeviceMacAddress]
                              };
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSetLongOrderPassword:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         
         [_delegate SetLongOrderPassword:SVCERR_MSG_ERROR dataList:NULL];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];

}
//长途订单确认
- (void)GetAcceptableLongOrderDetailInfoWithUserId:(long)userid AndOrderId:(NSString *)orderid
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetAcceptableLongOrderDetailInfo]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"orderid":orderid,
                              @"devtoken": [Common getDeviceMacAddress]
                              };
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetAcceptableLongOrderDetailInfoRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [_delegate GetAcceptableLongOrderDetailInfo:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];

}
//信息费计算方式获取
- (void)GetInfoFeeCalcMethodWithUserID:(long)userid AndCity:(NSString *)city
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetInfoFeeCalcMethod]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"city":city,
                              @"devtoken": [Common getDeviceMacAddress]
                              };
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseInfoFeeCalcMethodRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         
         [_delegate GetInfoFeeCalcMethod:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}



//获取司机当前位置
- (void)GetDriverPosWithUserID:(long)userid AndDriverId:(long)driverId
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetDriverPos]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    
    NSDictionary * params = @{@"source": CUR_SOURCE,
                              @"userid": [NSString stringWithFormat:@"%ld",userid],
                              @"driverid":[NSString stringWithFormat:@"%ld",driverId],
                              @"devtoken": [Common getDeviceMacAddress]
                              };
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseDriverPosRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         
         [_delegate GetDriverPos:SVCERR_MSG_ERROR dataList:NULL];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}


///////////////////////////////// Parse Service Result //////////////////////
#pragma mark - Parse Service result


- (void) parseDriverPosRes : (NSString *)responseStr
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
        }
        else
        {
            NSDictionary * jsonDict = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            STdriverPosInfo * driverPosInfo = [[STdriverPosInfo alloc]init];
            [driverPosInfo setValuesForKeysWithDictionary:jsonDict];
            [dataList addObject:driverPosInfo];
        }
    }
    [_delegate GetDriverPos:retMsg dataList:dataList];
    
}


- (void) parseInfoFeeCalcMethodRes : (NSString *)responseStr
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
         }
        else
        {
            NSDictionary * jsonDict = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            InfoFeeCalcMethodInfo * infoFeeCalc = [[InfoFeeCalcMethodInfo alloc]init];
            [infoFeeCalc setValuesForKeysWithDictionary:jsonDict];
            [dataList addObject:infoFeeCalc];
        }
    }
    if([_delegate respondsToSelector:@selector(GetInfoFeeCalcMethod:dataList:)])
    {
      [_delegate GetInfoFeeCalcMethod:retMsg dataList:dataList];
    }
  

}



- (void) parseExecuteLongOrderRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate executeLongOrderResult:retMsg];
}

- (void) parseSignLongOrderDriverArrivalRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate signLongOrderDriverArrivalResult:retMsg];
}

- (void) parseSignLongOrderPassUploadRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate signLongOrderPassengerUpload:retMsg];
}

- (void) parseSignLongOrderPassGiveupRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate signLongOrderPassengerGiveup:retMsg];
}

- (void) parseStartLongOrderDrivingRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate startLongOrderDriving:retMsg];
}

- (void) parseEndLongOrderRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate endLongOrder:retMsg];
}

- (void) parseEvaluateLongOrderPassRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate evaluateLongOrderPass:retMsg];
}

- (void) parseEvaluateLongOrderDriverRes : (NSString *)responseStr
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
                [_delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            
        }
    }
    
    [_delegate evaluateLongOrderDriver:retMsg];
}

#pragma mark - 数据解析

-(void)parseGetAcceptableLongOrderDetailInfoRes:(NSString *)responseStr
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
        }
        else
        {
            NSDictionary * jsonDic = [tmp1 objectForKey:SVCC_DATA];
            NSLog(@"%@",jsonDic);
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            
            STAcceptableLongOrderDetailInfo * acceptableLongOrderDetailInfo = [[STAcceptableLongOrderDetailInfo alloc]initWithDictionary:jsonDic];
            
            [dataList addObject:acceptableLongOrderDetailInfo];
            
        }
    }
    
    [_delegate GetAcceptableLongOrderDetailInfo:retMsg dataList:dataList];

}



//设定长途密码数据解析
#pragma mark 长途抢单  密码设定后，服务器返回数据解析
-(void)parseSetLongOrderPassword:(NSString *)responseStr
{
  
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    NSMutableArray * dataList = nil;

    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers error: &e];
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        } else
        {
            NSDictionary * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            MyLog(@"%@",jsonArray);
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            
            STGrabLongOrderSuccessInfo * grabLongOrderSuccessInfo = [[STGrabLongOrderSuccessInfo alloc]init];
            [grabLongOrderSuccessInfo setValuesForKeysWithDictionary:jsonArray];
            [dataList addObject:grabLongOrderSuccessInfo];
            
        }

    }
    [_delegate SetLongOrderPassword:retMsg dataList:dataList];
}



//设定长途抢单数据解析
- (void)parseAcceptLongOrder:(NSString *)responseStr
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
            NSDictionary * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            dataList = [[NSMutableArray alloc] init];
            
            STGrabLongOrderFailInfo * grabLongOrderSuccessInfo = [[STGrabLongOrderFailInfo alloc]init];
            [grabLongOrderSuccessInfo setValuesForKeysWithDictionary:jsonArray];
            [dataList addObject:grabLongOrderSuccessInfo];
		}
		else
		{
			NSDictionary * jsonArray = [tmp1 objectForKey:SVCC_DATA];

			// make child app data list
			dataList = [[NSMutableArray alloc] init];

			STGrabLongOrderFailInfo * grabLongOrderSuccessInfo = [[STGrabLongOrderFailInfo alloc]init];
			[grabLongOrderSuccessInfo setValuesForKeysWithDictionary:jsonArray];
			[dataList addObject:grabLongOrderSuccessInfo];
		}
	}

	[_delegate AcceptLongOrder:jsonRet retmsg:retMsg dataList:dataList];
}



//长途订单发布数据解析
- (void)parsePublishLongOrder:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
//	NSMutableArray * dataList = nil;
    
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
        }
    }
    if([_delegate respondsToSelector:@selector(PublishLongOrder:dataList:)])
    {
      [_delegate PublishLongOrder:retMsg dataList:nil];
    }

}

//最新的抢单信息解析
-(void)parseGetLatestAcceptableLongOrdersRes:(NSString *)responseStr
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
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STAcceptableLongOrderInfo * acceptableLongOrder = [[STAcceptableLongOrderInfo alloc]init];
                [acceptableLongOrder setValuesForKeysWithDictionary:item];
                [dataList addObject:acceptableLongOrder];
            }
        }
    }
    if([_delegate respondsToSelector:@selector(GetLatestAcceptableLongOrders:dataList:)])
    {
     [_delegate GetLatestAcceptableLongOrders:retMsg dataList:dataList];
    }
    
    
}
//分页抢单信息解析
-(void)parseGetPagedAcceptableLongOrdersRes:(NSString *)responseStr
{
    
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        NSLog(@"%@",tmp1);
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        }
        else
        {
            NSMutableArray * jsonArray = [tmp1 objectForKey:SVCC_DATA];
           
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STAcceptableLongOrderInfo * acceptableLongOrder = [[STAcceptableLongOrderInfo alloc]init];

                [acceptableLongOrder setValuesForKeysWithDictionary:item];
                [dataList addObject:acceptableLongOrder];            }
        }
    }
    if([_delegate respondsToSelector:@selector(GetPagedAcceptableLongOrders:dataList:)])
    {
    [_delegate GetPagedAcceptableLongOrders:retMsg dataList:dataList];
    }
    
    
    
}



@end
