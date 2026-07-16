//
//  Singleton.h
//  音效播放
//
//  Created by maguanghai on 14-7-29.
//  Copyright (c) 2014年 maguanghai. All rights reserved.
//

#define singletonInterface(className)   +(instancetype)shared##className;

#define singletonImplemention(className) \
static className *_instance;\
+(id)allocWithZone:(struct _NSZone *)zone\
{\
    static dispatch_once_t onceToken;\
    dispatch_once(&onceToken, ^{\
        _instance =[super allocWithZone:zone];\
    });\
    return _instance;\
}\
+(instancetype)shared##className\
{\
    static dispatch_once_t onceToken;\
    dispatch_once(&onceToken, ^{\
        _instance =[[className alloc]init];\
    });\
    return _instance;\
}\
-(id)copyWithZone:(NSZone *)zone\
{\
    return _instance;\
}