//
//  SystemPriceInfoResult.m
//  BJPinChe
//
//  Created by APP_USER on 14-10-29.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import "SystemPriceInfoResult.h"

@implementation SystemPriceInfoResult


+(instancetype)systemPriceInfoResultWithDict:(NSDictionary *)dict
{
    return [[self alloc]initWithDict:dict];
}
-(instancetype)initWithDict:(NSDictionary *)dict
{
    self =[super init];
    if(self)
    {
        self.system_description= [dict objectForKey:@"description"];
        self.aver_price =[[dict objectForKey:@"aver_price"]doubleValue];
        self.distance =[[dict objectForKey:@"distance"]doubleValue];
        self.price_interval =[[dict objectForKey:@"price_interval"]doubleValue];
        self.car_style =[[dict objectForKey:@"car_style"]integerValue];
        self.min_limit =[[dict objectForKey:@"min_limit"]doubleValue];
        
       // [self setValuesForKeysWithDictionary:dict];
    }
    return self;
    
}

@end
