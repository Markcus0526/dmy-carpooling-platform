//
//  BaiduSuggestionAdr.m
//  BJPinChe
//
//  Created by APP_USER on 14-10-24.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "BaiduSuggestionAdr.h"

@implementation BaiduSuggestionAdr

+(instancetype)baiduSuggestionWithDict:(NSDictionary *)dict
{
    return [[self alloc]initWithDict:dict];
}
-(instancetype)initWithDict:(NSDictionary *)dict
{
    self =[super init];
    if(self)
    {
      [self setValuesForKeysWithDictionary:dict];
    }
    return self;
   
}

@end
