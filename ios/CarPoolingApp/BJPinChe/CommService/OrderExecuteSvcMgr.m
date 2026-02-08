//
//  OrderExecuteSvcMgr.m
//  BJPinChe
//
//  Created by YunSI on 9/6/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import "OrderExecuteSvcMgr.h"
#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "ServiceMethod.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"

@implementation OrderExecuteSvcMgr

@synthesize delegate;

- (void) ExecuteOnceOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdExecuteOnceOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseExecuteOnceOrderRes:responseStr];

     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate executeOnceOrderResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) ExecuteOnOffOrder:(long)userid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdExecuteOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseExecuteOnOffOrderRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate executeOnOffOrderResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) SignOnceOrderDriverArrival:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignOnceOrderDriverArrival]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignOnceOrderDriverArrivalRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate signOnceOrderDriverArrivalResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) SignOnOffOrderDriverArrival:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignOnOffOrderDriverArrival]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignOnOffOrderDriverArrivalRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate signOnOffOrderDriverArrivalResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) SignOnceOrderPassengerUpload:(long)driverid OrderId:(long)orderid PassId:(long)passid Password:(NSString *)password DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignOnceOrderPassengerUpload]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
							password, @"password",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignOnceOrderPassengerUploadRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate signOnceOrderPassengerUploadResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) SignOnOffOrderPassengerUpload:(long)driverid OrderId:(long)orderid PassId:(long)passid Password:(NSString *)password DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdSignOnOffOrderPassengerUpload]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
							password, @"password",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseSignOnOffOrderPassengerUploadRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate signOnOffOrderPassengerUploadResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) EndOnceOrder:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEndOnceOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseEndOnceOrderRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate endOnceOrderResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) EndOnOffOrder:(long)driverid OrderId:(long)orderid DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEndOnOffOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseEndOnOffOrderRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate endOnOffOrderResult:SVCERR_MSG_ERROR];
		 
     }];
}

- (void) EvaluateOnceOrderPass:(long)driverid PassId:(long)passid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEvaluateOnceOrderPass]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", level], @"level",
							msg, @"msg",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseEvaluateOnceOrderPassRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate evaluateOnceOrderPassResult:SVCERR_MSG_ERROR Level:EVAL_NOT];
		 
     }];
}

- (void) EvaluateOnOffOrderPass:(long)driverid PassId:(long)passid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEvaluateOnOffOrderPass]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", level], @"level",
							msg, @"msg",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseEvaluateOnOffOrderPassRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate evaluateOnOffOrderPassResult:SVCERR_MSG_ERROR Level:EVAL_NOT];
		 
     }];
}

- (void) EvaluateOnceOrderDriver:(long)passid DriverId:(long)driverid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken
{
	
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEvaluateOnceOrderDriver]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", level], @"level",
							msg, @"msg",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseEvaluateOnceOrderDriverRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate evaluateOnceOrderDriverResult:SVCERR_MSG_ERROR Level:EVAL_NOT];
     }];
	
}

- (void) EvaluateOnOffOrderDriver:(long)passid DriverId:(long)driverid OrderId:(long)orderid Level:(int)level Msg:(NSString *)msg DevToken:(NSString *)devtoken
{
	
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdEvaluateOnOffOrderDriver]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", passid], @"passid",
							[NSString stringWithFormat:@"%ld", driverid], @"driverid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", level], @"level",
							msg, @"msg",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseEvaluateOnOffOrderDriverRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate evaluateOnOffOrderDriverResult:SVCERR_MSG_ERROR  Level:EVAL_NOT];
     }];
	
}

- (void) PayNormalOrder:(long)userid OrderId:(long)orderid OrderType:(int)order_type Price:(double)price Coupons:(NSString *)coupons DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdPayNormalOrder]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", order_type], @"order_type",
							[NSString stringWithFormat:@"%f", price], @"price",
							coupons, @"coupons",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 MyLog(@"PayNormalOrder success%@",responseObject);
		 [self parsePayNormalOrderRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate payNormalOrderResult:SVCERR_MSG_ERROR];
     }];
}

- (void) CheckOrderCancelledState:(long)userid OrderId:(long)orderid OrderType:(int)order_type Distance:(double)distance DevToken:(NSString *)devtoken
{
	NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdOrderCancelledState]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", orderid], @"orderid",
							[NSString stringWithFormat:@"%d", order_type], @"order_type",
							[NSString stringWithFormat:@"%f", distance], @"distance",
							devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseOrderCancelledStateRes:responseStr];
		 
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         [delegate checkOrderCancelledStateResult:SVCERR_MSG_ERROR status:1];
     }];
}


///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
- (void)parseExecuteOnceOrderRes:(NSString *)responseStr
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
    
    [delegate executeOnceOrderResult:retMsg];
}

- (void)parseExecuteOnOffOrderRes:(NSString *)responseStr
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
    
    [delegate executeOnOffOrderResult:retMsg];
}

- (void)parseSignOnceOrderDriverArrivalRes:(NSString *)responseStr
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
    
    [delegate signOnceOrderDriverArrivalResult:retMsg];
}

- (void)parseSignOnOffOrderDriverArrivalRes:(NSString *)responseStr
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
    
    [delegate signOnOffOrderDriverArrivalResult:retMsg];
}

- (void)parseSignOnceOrderPassengerUploadRes:(NSString *)responseStr
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
    
    [delegate signOnceOrderPassengerUploadResult:retMsg];
}

- (void)parseSignOnOffOrderPassengerUploadRes:(NSString *)responseStr
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
    
    [delegate signOnOffOrderPassengerUploadResult:retMsg];
}

- (void)parseEndOnceOrderRes:(NSString *)responseStr
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
    
    [delegate endOnceOrderResult:retMsg];
}

- (void)parseEndOnOffOrderRes:(NSString *)responseStr
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
    
    [delegate endOnOffOrderResult:retMsg];
}

- (void)parseEvaluateOnceOrderPassRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	int level;
    
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
			level = [[tmp2 objectForKey:@"level"] intValue];
        }
    }
    
    [delegate evaluateOnceOrderPassResult:retMsg Level:level];
}

- (void)parseEvaluateOnOffOrderPassRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	int	level;
    
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
			level = [[tmp2 objectForKey:@"level"] intValue];
        }
    }
    
    [delegate evaluateOnOffOrderPassResult:retMsg Level:level];
}

- (void)parseEvaluateOnceOrderDriverRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	int	level;
    
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
			level = [[tmp2 objectForKey:@"level"] intValue];
        }
    }
    
    [delegate evaluateOnceOrderDriverResult:retMsg Level:level];
}

- (void)parseEvaluateOnOffOrderDriverRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	int	level;
    
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
			level = [[tmp2 objectForKey:@"level"] intValue];
        }
    }
    
    [delegate evaluateOnOffOrderDriverResult:retMsg Level:level];
}

- (void)parsePayNormalOrderRes:(NSString *)responseStr
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
    
    [delegate payNormalOrderResult:retMsg];
}

- (void)parseOrderCancelledStateRes:(NSString *)responseStr
{
	NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    int status = 1;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        NSDictionary * tmp1 = [tmp objectForKey:SVCC_RESULT];
        
        // get service result
        jsonRet = [[tmp1 objectForKey:SVCC_RET] intValue];
        retMsg = [tmp1 objectForKey:SVCC_RETMSG];
        // check result
        if (jsonRet != SVCERR_SUCCESS)
        {
            // check duplicate login state
            if (jsonRet == SVCERR_DUPLICATE_USER) {
                [delegate duplicateUser:retMsg];
                return;
            }
        }
        else
        {
            NSDictionary * tmp2 = [tmp1 objectForKey:SVCC_DATA];
			status = [[tmp2 objectForKey:@"status"] intValue];
        }
    }
    
    [delegate checkOrderCancelledStateResult:retMsg status:status];
}


@end
