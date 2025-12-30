//
//  NearDriver.h
//  BJPinChe
//
//  Created by yc on 15-1-13.
//  Copyright (c) 2015年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "STDataInfo.h"
@interface NearDriver : NSObject
@property (nonatomic, strong) NSMutableArray *driverArray;
-(NSMutableArray *)nearDriverFalse:(double)lat :(double)lon;

+ (NearDriver *)falseNearDrivers;
@end
