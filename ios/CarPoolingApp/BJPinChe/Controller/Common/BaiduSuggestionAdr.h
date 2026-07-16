//
//  BaiduSuggestionAdr.h
//  BJPinChe
//
//  Created by APP_USER on 14-10-24.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
/**
 *  百度地图api suggestion 返回地址数据模型
 */
@interface BaiduSuggestionAdr : NSObject

/**
 *       
 "name":"西直门-地铁站",
 "city":"北京市",
 "district":"西城区",
 "business":"",
 "cityid":"131"
 },
 */

@property(nonatomic,copy)NSString *name;
@property(nonatomic,copy)NSString *city;
@property(nonatomic,copy)NSString *district;
@property(nonatomic,copy)NSString *business;
@property(nonatomic,copy)NSString *cityid;

+(instancetype)baiduSuggestionWithDict:(NSDictionary *)dict;
-(instancetype)initWithDict:(NSDictionary *)dict;



@end
