//
//  CommonSvcMgr.h
//  BJPinChe
//
//  Created by Kimoc on 14-8-23.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol GlobalSvcDelegate;

@interface GlobalSvcMgr : NSObject

@property(strong, nonatomic) id<GlobalSvcDelegate> delegate;

- (void) BaiduSearchAddr : (NSString *)keyword region:(NSString *)region apikey:(NSString *)apikey;
- (void) BaiduSuggestionAddr : (NSString *)keyword region:(NSString *)region apikey:(NSString *)apikey;
- (void) ConvertGoogle2Baidu : (double)lat lng:(double)lng apikey:(NSString *)apikey;

@end

// service protocol
@protocol GlobalSvcDelegate <NSObject>

@optional

- (void) baiduSearchAddrResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) baiduSuggestionAddrResult:(NSString *)result dataList:(NSMutableArray *)dataList;
- (void) convertGoogle2BaiduResult : (NSString *)result lat:(double)lat lng:(double)lng;
@end
