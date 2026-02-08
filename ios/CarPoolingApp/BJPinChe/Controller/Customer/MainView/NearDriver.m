//
//  NearDriver.m
//  BJPinChe
//
//  Created by yc on 15-1-13.
//  Copyright (c) 2015年 KimOC. All rights reserved.
//

#import "NearDriver.h"
static NearDriver *expressSingleton = nil;
@implementation NearDriver
@synthesize driverArray = _driverArray;
+ (NearDriver*)falseNearDrivers
{
    if (expressSingleton == nil) {
        @synchronized(self){
            expressSingleton = [[[self class] alloc] init];
        }
    }
    return expressSingleton;
    
}

- (id)init {
    @synchronized(self) {
        self = [super init];//往往放一些要初始化的变量.
        if (_driverArray ==nil) {
            _driverArray = [[NSMutableArray alloc]init];
        }
        return self;
    }
}

//限制当前对象创建多实例
#pragma mark - sengleton setting
+ (id)allocWithZone:(NSZone *)zone {
    @synchronized(self) {
        if (expressSingleton == nil) {
            expressSingleton = [super allocWithZone:zone];
        }
    }
    return expressSingleton;
}

+ (id)copyWithZone:(NSZone *)zone {
    return self;
}


-(NSMutableArray *)nearDriverFalse:(double)lat :(double)lon
{
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSDate *  senddate=[NSDate date];
    
    NSDateFormatter  *dateformatter=[[NSDateFormatter alloc] init];
    
    [dateformatter setDateFormat:@"YYYYMMddhh"];
    
    NSString *  locationString=[dateformatter stringFromDate:senddate];
    if([defaults objectForKey:@"dateNow"]==nil)
    {
        [defaults setObject:locationString forKey:@"dateNow"];
        [self falseRandomDriver:lat :lon];
    }
    if ([locationString integerValue]-[[defaults objectForKey:@"dateNow"] integerValue]>60) {
        [self falseRandomDriver:lat :lon];
        [defaults setObject:locationString forKey:@"dateNow"];
    }
    if (_driverArray.count ==0) {
        [self falseRandomDriver:lat :lon];
        [defaults setObject:locationString forKey:@"dateNow"];
    }
    
    return _driverArray;
}
-(void)falseRandomDriver:(double)lat :(double)lon
{
    [_driverArray removeAllObjects];
    for (int i=0; i<20; i++) {
        int rlat = arc4random()%10;
        int rlon = arc4random()%10;
        NSString *strLat=nil;
        NSString *strLon=nil;
        if(i%2 ==0 )
        {
            strLat = [NSString stringWithFormat:@"0.%.3d",rlat];
            strLon = [NSString stringWithFormat:@"0.%.3d",rlon];
        }else if(i%3 ==0 )
        {
            strLat = [NSString stringWithFormat:@"0.%.3d",rlat];
            strLon = [NSString stringWithFormat:@"-0.%.3d",rlon];
        }else
        {
            strLat = [NSString stringWithFormat:@"-0.%.3d",rlat];
            strLon = [NSString stringWithFormat:@"-0.%.3d",rlon];
        }
        
        double newlat =lat- [strLat doubleValue];
        double newlon =lon- [strLon doubleValue];
        
        STNearbyFalseDriverInfo *posInfo = [[STNearbyFalseDriverInfo alloc]init];
        posInfo.latitude = newlat;
        posInfo.longitude = newlon;
        posInfo.driverid = i+1000;
        [_driverArray addObject:posInfo];
    }
}
@end
