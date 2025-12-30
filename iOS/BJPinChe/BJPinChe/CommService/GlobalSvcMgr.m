//
//  CommonSvcMgr.m
//  BJPinChe
//
//  Created by Kimoc on 14-8-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "GlobalSvcMgr.h"

#import <sys/socket.h>
#import <netinet/in.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import "CommManager.h"
#import "ServiceMethod.h"
//#import "SBJson.h"
#import "AFHTTPClient.h"
#import "AFJSONRequestOperation.h"
#import "BaiduSuggestionAdr.h"


//////////////////////////////////////////////////////////
#pragma mark - Global Service Manager Interface

#define BAIDUSVC_TAG_STATUS                 @"status"
#define BAIDUSVC_TAG_RESULT                 @"results"
#define BAIDUSVC_STATUS_OK                  @"OK"

@implementation GlobalSvcMgr

@synthesize delegate;


/**
 * search address using baidu api
 
 http://api.map.baidu.com/place/v2/suggestion/
 http://api.map.baidu.com/place/search?query=奥体中心&region=沈阳市&output=json&ak=BC2405e7fa0abaeda8f246d8f1b50923
 */
#pragma mark 百度api地址 请求
- (void) BaiduSuggestionAddr : (NSString *)keyword region:(NSString *)region apikey:(NSString *)apikey
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:@"http://api.map.baidu.com/place/v2/suggestion"];
    
	NSURL *url = [NSURL URLWithString:@"http://api.map.baidu.com/"];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							keyword, @"q",
							region, @"region",
                            @"json", @"output",
                            apikey, @"ak",
                            nil];
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseBaiduSuggestionAddrRes:responseStr];
		 
		 MyLog(@"BaiduSuggestionAddr Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate baiduSearchAddrResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"BaiduSuggestionAddr [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * search address using baidu api
 
 
 http://api.map.baidu.com/place/search?query=奥体中心&region=沈阳市&output=json&ak=BC2405e7fa0abaeda8f246d8f1b50923
 */
#pragma mark 百度api地址 请求
- (void) BaiduSearchAddr : (NSString *)keyword region:(NSString *)region apikey:(NSString *)apikey;
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:@"http://api.map.baidu.com/place/search"];
    
	NSURL *url = [NSURL URLWithString:@"http://api.map.baidu.com/"];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							keyword, @"q",
							region, @"region",
                            @"json", @"output",
                            apikey, @"ak",
                            nil];
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseBaiduSearchAddrRes:responseStr];
		 
		 MyLog(@"BaiduSearchAddr Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate baiduSearchAddrResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"BaiduSearchAddr [HTTPClient Error]: %@", error.localizedDescription);
     }];
}

/**
 * convert google point to baidu point
 * lat, lng : [in], google point
 */
- (void) ConvertGoogle2Baidu : (double)lat lng:(double)lng apikey:(NSString *)apikey
{
    NSMutableString *method = [NSMutableString string];
    [method appendString:@"http://api.map.baidu.com/geoconv/v1"];
    
	NSURL *url = [NSURL URLWithString:@"http://api.map.baidu.com/"];
	AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:url];
    //httpClient.parameterEncoding = AFJSONParameterEncoding;
	
	NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
							[NSString stringWithFormat:@"%f,%f", lng, lat], @"coords",
							//region, @"region",
                            @"json", @"output",
                            apikey, @"ak",
                            nil];
    
    
    [httpClient getPath:method parameters:params success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
		 NSString *responseStr = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
		 
		 [self parseBaiduSearchAddrRes:responseStr];
		 
		 MyLog(@"Request Successful, response '%@'", responseStr);
     } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
		 [delegate baiduSearchAddrResult:SVCERR_MSG_ERROR dataList:nil];
		 MyLog(@"[HTTPClient Error]: %@", error.localizedDescription);
     }];
}



///////////////////////////////// event implementation //////////////////////
#pragma mark - Service Event Implementation
- (void) parseBaiduSearchAddrRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = SVCERR_MSG_SUCCESS;
    NSString * jsonRet = @"";
    NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        // get service result
        jsonRet = [tmp objectForKey:BAIDUSVC_TAG_STATUS];
        // check result
        if (![jsonRet isEqualToString:BAIDUSVC_STATUS_OK])
        {
            retMsg = SVCERR_MSG_ERROR;
        }
        else
        {
            NSMutableArray * jsonArray = [tmp objectForKey:BAIDUSVC_TAG_RESULT];
            
            // make address data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                STBaiduAddrInfo * stInfo = [[STBaiduAddrInfo alloc] init];
                stInfo.uid = [item objectForKey:@"uid"];
                stInfo.name = [item objectForKey:@"name"];
                NSDictionary * location = [item objectForKey:@"location"];
                stInfo.lat = [[location objectForKey:@"lat"] doubleValue];
                stInfo.lng = [[location objectForKey:@"lng"] doubleValue];
                stInfo.address = [item objectForKey:@"address"];
                stInfo.detail_url = [item objectForKey:@"detail_url"];
                // add one data info
                [dataList addObject:stInfo];
            }
        }
    }
    [delegate baiduSearchAddrResult:retMsg dataList:dataList];    
}

#pragma mark - 百度地址 suggetion
- (void) parseBaiduSuggestionAddrRes:(NSString *)responseStr
{
    NSError * e;
    NSString * retMsg = @"ok";
    NSNumber  *status ;
    NSMutableArray * dataList = nil;
    
    if (responseStr)
    {
        NSDictionary *tmp = [NSJSONSerialization JSONObjectWithData:[responseStr dataUsingEncoding:NSUTF8StringEncoding] options: NSJSONReadingMutableContainers                                                             error: &e];
        
        // get service result
        status = [tmp objectForKey:BAIDUSVC_TAG_STATUS];
        // check result
        BOOL success = (status == @0);
        if (!(success))
        {
            retMsg = SVCERR_MSG_ERROR;
        }
        else
        {
            NSMutableArray * jsonArray = [tmp objectForKey:@"result"];
            
            // make address data list
            dataList = [[NSMutableArray alloc] init];
            for (NSDictionary * item in jsonArray) {
                BaiduSuggestionAdr* stInfo = [[BaiduSuggestionAdr alloc] initWithDict:item];

                [dataList addObject:stInfo];
            }
        }
    }
    
    [delegate baiduSuggestionAddrResult:retMsg dataList:dataList];
}
@end
