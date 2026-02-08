//
//  MainSvcMgr.m
//  Yilebang
//
//  Created by KimOC on 12/18/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "ServiceMethod.h"
#import "TradeSvcMgr.h"
//#import "SBJson.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"


//////////////////////////////////////////////////////////
#pragma mark - Trade Info Manager Interface

@implementation TradeSvcMgr

@synthesize delegate;

/**
 * Call GetChildAppList Service
 * @param : IMEI [in], device imei
 */
- (void) GetTsLogsPage : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetTsLogPage]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", pageno], @"pageno",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseTsLogsPageRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getTsLogsPageResult:SVCERR_MSG_ERROR money:0.0 dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


- (void) GetLatestTsLogs : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdLatestTsLogs]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLatestTsLogsRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
         if([self.delegate respondsToSelector:@selector(getLatestTsLogsResult:money:dataList:)])
         {
           [delegate getLatestTsLogsResult:SVCERR_MSG_ERROR money:0.0 dataList:nil];
         }
		
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}
#pragma mark 充值网络请求
- (void) Charge : (long)userid balance:(double)balance devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdCharge]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%f", balance], @"balance",
                            devtoken, @"devtoken",
#warning 充值来源未完成
                             @"4",@"charge_source",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseChargeRes:responseStr];
		 
		 MyLog(@"Charge Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate chargeResult:SVCERR_MSG_ERROR money:0.0];
		 MyLog(@"Charge [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetAccount : (long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetAccount]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAccountRes:responseStr];
		 
		 MyLog(@"GetAccount Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getAccountResult:SVCERR_MSG_ERROR account:nil];
		 MyLog(@"GetAccount [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) Withdraw : (long)userid realname:(NSString *)realname accountname:(NSString *)accountname balance:(double)balance password:(NSString *)password devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdWithdraw]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            realname, @"realname",
                            accountname, @"accountname",
                            [NSString stringWithFormat:@"%f", balance], @"balance",
                            password, @"password",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseWithdrawRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate withdrawResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}


- (void) BindBankCard : (long)userid bankcard:(NSString *)bankcard bankname:(NSString *)bankname subbranch:(NSString *)subbranch devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdBindBankCard]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            bankcard, @"bankcard",
                            bankname, @"bankname",
                            subbranch, @"subbranch",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseBindBankCardRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate bindBankCardResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) ReleaseBankCard : (long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdReleaseBankCard]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseReleaseBankCardRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate releaseBankCardResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetLatestCoupon : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestCoupon]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", limitid], @"limitid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLatestCouponRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getLatestCouponResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetPagedCoupon : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetPagedCoupon]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%d", pageno], @"pageno",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parsePagedCouponRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getLatestCouponResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) AddCoupon : (long)userid active_code:(NSString *)active_code devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdAddCoupon]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            active_code, @"active_code",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAddCouponRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate addCouponResult:SVCERR_MSG_ERROR coupon:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) AdvanceOpinion : (long)userid contents:(NSString *)contents devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdAdvanceOpinion]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            contents, @"contents",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient postPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAdvanceOpinionRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate advanceOpinionResult:SVCERR_MSG_ERROR];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetMoney : (long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetMoney]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
                            devtoken, @"devtoken",
                            nil];
   
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseGetMoneyRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getMoneyResult:SVCERR_MSG_ERROR money:0];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}



///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
- (void)parseTsLogsPageRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    double money = 0;
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
            NSDictionary * jsonDataRes = [tmp1 objectForKey:SVCC_DATA];
            money = [[jsonDataRes objectForKey:@"curbalance"] doubleValue];
            
            NSMutableArray * jsonArray = [jsonDataRes objectForKey:@"logs"];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STAccumHisInfo * stInfo = [[STAccumHisInfo alloc] init];
                stInfo.uid = [[item objectForKey:@"uid"] longValue];
                stInfo.orderNo = [item objectForKey:@"tsid"];
                stInfo.usedAccum = [[item objectForKey:@"operbalance"] doubleValue];
                stInfo.opType = [item objectForKey:@"source"];
                stInfo.leftAccum = [[item objectForKey:@"remainbalance"] doubleValue];
                // add one data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getTsLogsPageResult:retMsg money:money dataList:dataList];
}

- (void)parseLatestTsLogsRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    double money = 0;
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
            NSDictionary * jsonDataRes = [tmp1 objectForKey:SVCC_DATA];
            money = [[jsonDataRes objectForKey:@"curbalance"] doubleValue];
            
            NSMutableArray * jsonArray = [jsonDataRes objectForKey:@"logs"];
            
            // make child app data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STAccumHisInfo * stInfo = [[STAccumHisInfo alloc] init];
                stInfo.uid = [[item objectForKey:@"uid"] longValue];
                stInfo.orderNo = [item objectForKey:@"tsid"];
#warning  要加时间
                stInfo.usedAccum = [[item objectForKey:@"operbalance"] doubleValue];
                stInfo.opType = [item objectForKey:@"source"];
                stInfo.leftAccum = [[item objectForKey:@"remainbalance"] doubleValue];
                stInfo.tstime = [item objectForKey:@"tstime"];
                // add one advert data info
                [dataList addObject:stInfo];
            }
        }
    }
    if([delegate respondsToSelector:@selector(getLatestTsLogsResult:money:dataList:)])
    {
     [delegate getLatestTsLogsResult:retMsg money:money dataList:dataList];
    }
    
}

- (void)parseChargeRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    double money = 0;
    
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
            NSDictionary * jsonDataRes = [tmp1 objectForKey:SVCC_DATA];
            money = [[jsonDataRes objectForKey:@"curbalance"] doubleValue];
            
        }
    }
    
    [delegate chargeResult:retMsg money:money];
}

- (void)parseAccountRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STBindAccount * dataInfo = [[STBindAccount alloc] init];
    
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
            NSDictionary * item = [tmp1 objectForKey:SVCC_DATA];
            
            // make bind account
            dataInfo.realname = [item objectForKey:@"realname"];
            dataInfo.bankcard = [item objectForKey:@"bankcard"];
            dataInfo.bankname = [item objectForKey:@"bankname"];
            dataInfo.subbranch = [item objectForKey:@"subbranch"];
        }
    }
    if([self.delegate respondsToSelector:@selector(getAccountResult:account:)])
    {
     [delegate getAccountResult:retMsg account:dataInfo];
    }
    
}


- (void)parseWithdrawRes:(NSString *)responseStr
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
    
    [delegate withdrawResult:retMsg];
}

- (void)parseBindBankCardRes:(NSString *)responseStr
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
    
    [delegate bindBankCardResult:retMsg];
}

- (void)parseReleaseBankCardRes:(NSString *)responseStr
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
    
    [delegate releaseBankCardResult:retMsg];
}


- (void)parseLatestCouponRes:(NSString *)responseStr
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
            
            // make coupon data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STCouponInfo * stInfo = [[STCouponInfo alloc] init];
                stInfo.uid = [[item objectForKey:@"uid"] longValue];
                stInfo.range = [item objectForKey:@"range"];
                stInfo.contents = [item objectForKey:@"contents"];
                stInfo.usecond = [item objectForKey:@"usecond"];
                stInfo.dateexp = [item objectForKey:@"dateexp"];
                stInfo.dateexp = [item objectForKey:@"dateexp"];
                stInfo.couponcode = [item objectForKey:@"couponcode"];
                stInfo.unitname = [item objectForKey:@"unitname"];
                stInfo.is_goods = [[item objectForKey:@"is_goods"] intValue];
                // add one advert data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getLatestCouponResult:retMsg dataList:dataList];
}


- (void)parsePagedCouponRes:(NSString *)responseStr
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
            
            // make coupon data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STCouponInfo * stInfo = [[STCouponInfo alloc] init];
                stInfo.uid = [[item objectForKey:@"uid"] longValue];
                stInfo.range = [item objectForKey:@"range"];
                stInfo.contents = [item objectForKey:@"contents"];
                stInfo.usecond = [item objectForKey:@"usecond"];
                stInfo.dateexp = [item objectForKey:@"dateexp"];
                stInfo.couponcode = [item objectForKey:@"couponcode"];
                stInfo.unitname = [item objectForKey:@"unitname"];
                stInfo.is_goods = [[item objectForKey:@"is_goods"] intValue];
                // add one advert data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getPagedCouponResult:retMsg dataList:dataList];
}


- (void)parseAddCouponRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    STCouponInfo * dataInfo = [[STCouponInfo alloc] init];
    
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
            NSDictionary * item = [tmp1 objectForKey:SVCC_DATA];
            
            // make coupon info
            dataInfo.uid = [[item objectForKey:@"uid"] longValue];
            dataInfo.range = [item objectForKey:@"range"];
            dataInfo.contents = [item objectForKey:@"contents"];
            dataInfo.usecond = [item objectForKey:@"usecond"];
            dataInfo.dateexp = [item objectForKey:@"subbranch"];
            dataInfo.couponcode = [item objectForKey:@"couponcode"];
            dataInfo.unitname = [item objectForKey:@"unitname"];
            dataInfo.is_goods = [[item objectForKey:@"is_goods"] intValue];
        }
    }
    
    [delegate addCouponResult:retMsg coupon:dataInfo];
}


- (void)parseAdvanceOpinionRes:(NSString *)responseStr
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
    
    [delegate advanceOpinionResult:retMsg];
}


- (void)parseGetMoneyRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
    double money = 0;
    
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
            NSDictionary * item = [tmp1 objectForKey:SVCC_DATA];
            
            money = [[item objectForKey:@"money"] doubleValue];
        }
    }
    if([self.delegate respondsToSelector:@selector(getMoneyResult:money:)])
    {
     [delegate getMoneyResult:retMsg money:money];
    }
    
}


@end