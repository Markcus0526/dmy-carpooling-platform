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
#import "MainSvcMgr.h"
//#import "SBJson.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"


//////////////////////////////////////////////////////////
#pragma mark - User Info Manager Interface

@implementation MainSvcMgr

@synthesize delegate;

/**
 * Call GetChildAppList Service
 * @param : source [in], app type ( 0 : main app, 11 : pinche app )
 * @param : IMEI [in], device imei
 */
- (void) GetChlidAppList : (NSString *)imei
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetChildAppList]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							imei, @"IMEI",
                            CUR_PLATFORM, @"platform",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseChildAppListRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getChildAppListResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * Call GetHomeProducts Service
 * @author : kimoc
 * @param : source [in], app type ( 0 : main app, 11 : pinche app )
 * @param : city [in], current city name
 * @param : driververif [in], verify state ( 0 : not verified, 1 : verified )
 * @param : limitid [in], first item id ( to refresh & get new one )
 * @return : N/A
 */
- (void) GetLatestAnnounce : (NSString *)city driververif:(NSString *)driververif limitid:(NSString *)limitid userid:(long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestAnnounce]];

	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
							CUR_SOURCE, @"source",
							city, @"city",
                            driververif, @"driververif",
                            limitid, @"limitid",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseLatestAnnounceRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getLatestAnnounceResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
    
}

/**
 * Call GetProductOfComment Service
 * @author : kimoc
 * @param : source [in], app type ( 0 : main app, 11 : pinche app )
 * @param : city [in], current city name
 * @param : driververif [in], verify state ( 0 : not verified, 1 : verified )
 * @param : pageno [in], current page number ( to refresh & get new one )
 */
- (void) GetAnnouncePage : (NSString *)city driververif:(NSString *)driververif pageno:(int)pageno userid:(long)userid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetAnnouncePage]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
							CUR_SOURCE, @"source",
							city, @"city",
                            driververif, @"driververif",
                            [NSString stringWithFormat:@"%d", pageno], @"pageno",
                            devtoken, @"devtoken",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseAnnouncePageRes:responseStr];
		 
		 NSLog(@"GetAnnouncePage Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getAnnouncePageResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"GetAnnouncePage [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetLatestOrderInfos : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestOrderInfos]];
    
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
		 
		 [self parseLatestOrdersRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getLatestOrderinfosResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetOrderInfoPage : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetOrderInfosPage]];
    
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
		 
		 [self parseOrderInfoPageRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getOrderInfoPageResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetLatestNoti : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetLatestNotis]];
    
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
		 
		 [self parseLatestNotiRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getLatestNotiResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetNotiPage : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetNotiPage]];
    
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
		 
		 [self parseNotiPageRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getNotiPageResult:SVCERR_MSG_ERROR dataList:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}

- (void) GetCouponDetail : (long)userid couponid:(long)couponid devtoken:(NSString *)devtoken
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:[[CommManager getCommMgr] bathServiceUrl]];
    [method appendString:[ServiceMethod cmdGetCouponDetails]];
    
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
                            [NSString stringWithFormat:@"%ld", userid], @"userid",
                            [NSString stringWithFormat:@"%ld", couponid], @"couponid",
                            devtoken, @"devtoken",
                            nil];
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseCouponDetailRes:responseStr];
		 
		 NSLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate getCouponDetailResult:SVCERR_MSG_ERROR dataInfo:nil];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}



- (void) hasNewsWithUserID:(long)userid city:(NSString*)city driververif:(int)driververif lastannounceid:(long)lastannounceid lastordernotifid:(long)lastordernotifid lastpersonnotifid:(long)lastpersonnotifid devtoken:(NSString*)devtoken
{
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
							city, @"city",
							[NSNumber numberWithInteger:driververif], @"driververif",
							[NSNumber numberWithLong:lastannounceid], @"lastannounceid",
							[NSNumber numberWithLong:lastordernotifid], @"lastordernotifid",
							[NSNumber numberWithLong:lastpersonnotifid], @"lastpersonnotifid",
							devtoken, @"devtoken",
							nil];

	[httpClient postPath:@"hasNews" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
	 {
		 if (delegate != nil && [delegate respondsToSelector:@selector(hasNewsResultCode:retmsg:announcement:ordernotif:personnotif:)])
		 {
			 NSDictionary* responseData = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];

			 NSDictionary* result = [responseData objectForKey:SVCC_RESULT];

			 int nRetCode = [Common getIntValueWithKey:SVCC_RET defaultValue:SVCERR_FAILURE Dict:result];
			 NSString* szRetMsg = [Common getStringValueWithKey:SVCC_RETMSG defaultValue:@"" Dict:result];

			 if (nRetCode == SVCERR_SUCCESS)
			 {
				 NSDictionary* retdata = [result objectForKey:SVCC_DATA];

				 int ann_count = [Common getIntValueWithKey:@"announcement" defaultValue:0 Dict:retdata];
				 int order_count = [Common getIntValueWithKey:@"ordernotif" defaultValue:0 Dict:retdata];
				 int person_count = [Common getIntValueWithKey:@"personnotif" defaultValue:0 Dict:retdata];

				 [delegate hasNewsResultCode:nRetCode retmsg:MSG_SUCCESS announcement:ann_count ordernotif:order_count personnotif:person_count];
			 }
			 else
			 {
				 [delegate hasNewsResultCode:nRetCode retmsg:szRetMsg announcement:-1 ordernotif:-1 personnotif:-1];
			 }
		 }
	 } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate hasNewsResultCode:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR announcement:-1 ordernotif:-1 personnotif:-1];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
	 }];
}


- (void) readOrderNotifsWithUserID:(long)userid orderNotifyID:(long)ordernotifid devtoken:(NSString*)devtoken
{
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
							[NSString stringWithFormat:@"%ld", ordernotifid], @"ordernotifid",
							devtoken, @"devtoken",
							nil];

	[httpClient getPath:@"readOrderNotifs" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
	 {
		 if (delegate != nil && [delegate respondsToSelector:@selector(readOrderNotifyResultCode:retmsg:uid:)])
		 {
		 	NSDictionary* responseData = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];

		 	NSDictionary* result = [responseData objectForKey:SVCC_RESULT];

			 int nRetCode = [Common getIntValueWithKey:SVCC_RET defaultValue:SVCERR_FAILURE Dict:result];
			 NSString* szRetMsg = [Common getStringValueWithKey:SVCC_RETMSG defaultValue:@"" Dict:result];

			 if (nRetCode == SVCERR_SUCCESS)
			 {
				 [delegate readOrderNotifyResultCode:nRetCode retmsg:MSG_SUCCESS uid:ordernotifid];
			 }
			 else
			 {
				 [delegate readOrderNotifyResultCode:nRetCode retmsg:szRetMsg uid:-1];
			 }
		 }
	 } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate readOrderNotifyResultCode:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR uid:-1];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
	 }];
}



- (void) readPersonNotifsWithUserID:(long)userid personNotifyID:(long)personnotifid devtoken:(NSString*)devtoken
{
	NSURL *url = [NSURL URLWithString:[[CommManager getCommMgr] bathServiceUrl]];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];

	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							CUR_SOURCE, @"source",
							[NSString stringWithFormat:@"%ld", userid], @"userid",
							[NSString stringWithFormat:@"%ld", personnotifid], @"personnotifid",
							devtoken, @"devtoken",
							nil];

	[httpClient getPath:@"readPersonNotifs" parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
	 {
		 if (delegate != nil && [delegate respondsToSelector:@selector(readPersonNofityResultCode:retmsg:uid:)])
		 {
			 NSDictionary* responseData = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];

			 NSDictionary* result = [responseData objectForKey:SVCC_RESULT];

			 int nRetCode = [Common getIntValueWithKey:SVCC_RET defaultValue:SVCERR_FAILURE Dict:result];
			 NSString* szRetMsg = [Common getStringValueWithKey:SVCC_RETMSG defaultValue:@"" Dict:result];

			 if (nRetCode == SVCERR_SUCCESS)
			 {
				 [delegate readOrderNotifyResultCode:nRetCode retmsg:MSG_SUCCESS uid:personnotifid];
			 }
			 else
			 {
				 [delegate readOrderNotifyResultCode:nRetCode retmsg:szRetMsg uid:personnotifid];
			 }
		 }
	 } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate readOrderNotifyResultCode:SVCERR_FAILURE retmsg:SVCERR_MSG_ERROR uid:personnotifid];
		 NSLog(@"[HTTPClient Error]: %@", error.localizedDescription);
	 }];
}



///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
- (void)parseChildAppListRes:(NSString *)responseStr
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
                STAppInfo * stInfo = [[STAppInfo alloc] init];
                stInfo.uid = [[item objectForKey:@"uid"] longValue];
                stInfo.appName = [item objectForKey:@"name"];
                stInfo.code = [item objectForKey:@"code"];
                stInfo.appIcon = [item objectForKey:@"image"];
                stInfo.appScheme = [item objectForKey:@"urlscheme"];
                stInfo.latestVer = [item objectForKey:@"latestver"];
                stInfo.curVer = [item objectForKey:@"curver"];
                stInfo.appUrl = [item objectForKey:@"downloadurl"];
                // add one advert data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getChildAppListResult:retMsg dataList:dataList];
}


- (void)parseLatestAnnounceRes:(NSString *)responseStr
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
                STNewsInfo * stInfo = [[STNewsInfo alloc] init];

				stInfo.uid = [Common getLongValueWithKey:@"uid" defaultValue:0 Dict:item];
                stInfo.title = [Common getStringValueWithKey:@"title" defaultValue:@"" Dict:item];
				stInfo.contents = [Common getStringValueWithKey:@"contents" defaultValue:@"" Dict:item];
				stInfo.time = [Common getStringValueWithKey:@"time" defaultValue:@"" Dict:item];
				stInfo.hasread = [Common getIntValueWithKey:@"hasread" defaultValue:1 Dict:item];
				stInfo.couponid = [Common getLongValueWithKey:@"couponid" defaultValue:0 Dict:item];
				stInfo.orderid = [Common getLongValueWithKey:@"orderid" defaultValue:0 Dict:item];
				stInfo.ordertype = [Common getIntValueWithKey:@"ordertype" defaultValue:0 Dict:item];
				stInfo.news_type = NEWSTYPE_ANNOUNCEMENT;

				// add one news data info
				[dataList addObject:stInfo];
			}
		}
	}

    
	[delegate getLatestAnnounceResult:retMsg dataList:dataList];
}

- (void)parseAnnouncePageRes:(NSString *)responseStr
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
                STNewsInfo * stInfo = [[STNewsInfo alloc] init];

				stInfo.uid = [Common getLongValueWithKey:@"uid" defaultValue:0 Dict:item];
				stInfo.title = [Common getStringValueWithKey:@"title" defaultValue:@"" Dict:item];
				stInfo.contents = [Common getStringValueWithKey:@"contents" defaultValue:@"" Dict:item];
				stInfo.time = [Common getStringValueWithKey:@"time" defaultValue:@"" Dict:item];
				stInfo.hasread = [Common getIntValueWithKey:@"hasread" defaultValue:1 Dict:item];
				stInfo.couponid = [Common getLongValueWithKey:@"couponid" defaultValue:0 Dict:item];
				stInfo.orderid = [Common getLongValueWithKey:@"orderid" defaultValue:0 Dict:item];
				stInfo.ordertype = [Common getIntValueWithKey:@"ordertype" defaultValue:0 Dict:item];
				stInfo.news_type = NEWSTYPE_ANNOUNCEMENT;

				// add one news data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getAnnouncePageResult:retMsg dataList:dataList];
}


- (void)parseLatestOrdersRes:(NSString *)responseStr
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
                STNewsInfo * stInfo = [[STNewsInfo alloc] init];

				stInfo.uid = [Common getLongValueWithKey:@"uid" defaultValue:0 Dict:item];
				stInfo.title = [Common getStringValueWithKey:@"title" defaultValue:@"" Dict:item];
				stInfo.contents = [Common getStringValueWithKey:@"contents" defaultValue:@"" Dict:item];
				stInfo.time = [Common getStringValueWithKey:@"time" defaultValue:@"" Dict:item];
				stInfo.hasread = [Common getIntValueWithKey:@"hasread" defaultValue:1 Dict:item];
				stInfo.couponid = [Common getLongValueWithKey:@"couponid" defaultValue:0 Dict:item];
				stInfo.orderid = [Common getLongValueWithKey:@"orderid" defaultValue:0 Dict:item];
				stInfo.ordertype = [Common getIntValueWithKey:@"ordertype" defaultValue:0 Dict:item];
				stInfo.news_type = NEWSTYPE_ORDERNOTIFY;

				// add one news data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getLatestOrderinfosResult:retMsg dataList:dataList];
}

- (void)parseOrderInfoPageRes:(NSString *)responseStr
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
                STNewsInfo * stInfo = [[STNewsInfo alloc] init];

				stInfo.uid = [Common getLongValueWithKey:@"uid" defaultValue:0 Dict:item];
				stInfo.title = [Common getStringValueWithKey:@"title" defaultValue:@"" Dict:item];
				stInfo.contents = [Common getStringValueWithKey:@"contents" defaultValue:@"" Dict:item];
				stInfo.time = [Common getStringValueWithKey:@"time" defaultValue:@"" Dict:item];
				stInfo.hasread = [Common getIntValueWithKey:@"hasread" defaultValue:1 Dict:item];
				stInfo.couponid = [Common getLongValueWithKey:@"couponid" defaultValue:0 Dict:item];
				stInfo.orderid = [Common getLongValueWithKey:@"orderid" defaultValue:0 Dict:item];
				stInfo.ordertype = [Common getIntValueWithKey:@"ordertype" defaultValue:0 Dict:item];
				stInfo.news_type = NEWSTYPE_ORDERNOTIFY;

				// add one news data info
                [dataList addObject:stInfo];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getOrderInfoPageResult:dataList:)]) {
        [delegate getOrderInfoPageResult:retMsg dataList:dataList];
    }
    
}

- (void)parseLatestNotiRes:(NSString *)responseStr
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
                STNewsInfo * stInfo = [[STNewsInfo alloc] init];

				stInfo.uid = [Common getLongValueWithKey:@"uid" defaultValue:0 Dict:item];
				stInfo.title = [Common getStringValueWithKey:@"title" defaultValue:@"" Dict:item];
				stInfo.contents = [Common getStringValueWithKey:@"contents" defaultValue:@"" Dict:item];
				stInfo.time = [Common getStringValueWithKey:@"time" defaultValue:@"" Dict:item];
				stInfo.hasread = [Common getIntValueWithKey:@"hasread" defaultValue:1 Dict:item];
				stInfo.couponid = [Common getLongValueWithKey:@"couponid" defaultValue:0 Dict:item];
				stInfo.orderid = [Common getLongValueWithKey:@"orderid" defaultValue:0 Dict:item];
				stInfo.ordertype = [Common getIntValueWithKey:@"ordertype" defaultValue:0 Dict:item];
				stInfo.news_type = NEWSTYPE_PERSONNOTIFY;

				// add one news data info
                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate getLatestNotiResult:retMsg dataList:dataList];
}

- (void)parseNotiPageRes:(NSString *)responseStr
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
                STNewsInfo * stInfo = [[STNewsInfo alloc] init];

				stInfo.uid = [Common getLongValueWithKey:@"uid" defaultValue:0 Dict:item];
				stInfo.title = [Common getStringValueWithKey:@"title" defaultValue:@"" Dict:item];
				stInfo.contents = [Common getStringValueWithKey:@"contents" defaultValue:@"" Dict:item];
				stInfo.time = [Common getStringValueWithKey:@"time" defaultValue:@"" Dict:item];
				stInfo.hasread = [Common getIntValueWithKey:@"hasread" defaultValue:1 Dict:item];
				stInfo.couponid = [Common getLongValueWithKey:@"couponid" defaultValue:0 Dict:item];
				stInfo.orderid = [Common getLongValueWithKey:@"orderid" defaultValue:0 Dict:item];
				stInfo.ordertype = [Common getIntValueWithKey:@"ordertype" defaultValue:0 Dict:item];
				stInfo.news_type = NEWSTYPE_PERSONNOTIFY;

				// add one news data info
                [dataList addObject:stInfo];
            }
        }
    }
    if ([delegate respondsToSelector:@selector(getNotiPageResult:dataList:)]) {
        [delegate getNotiPageResult:retMsg dataList:dataList];
    }
    
}


- (void)parseCouponDetailRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSInteger jsonRet = SVCERR_FAILURE;
	STCouponInfo * stInfo = nil;
    
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
            
            stInfo = [[STCouponInfo alloc] init];
            stInfo.uid = [[item objectForKey:@"uid"] longValue];
            stInfo.contents = [item objectForKey:@"product_name"];
            stInfo.couponcode = [item objectForKey:@"coupon_code"];
            stInfo.unitname = [item objectForKey:@"unitname"];
        }
    }
    
    [delegate getCouponDetailResult:retMsg dataInfo:stInfo];
}


@end