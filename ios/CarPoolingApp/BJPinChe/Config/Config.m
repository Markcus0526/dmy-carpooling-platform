//
//  Common.m
//  4S-C
//
//  Created by R CJ on 1/5/13.
//  Copyright (c) 2013 PIC. All rights reserved.
// 利用NSUserDefaults 记录用户名，密码，用户角色选择，上下班车程

#import "Config.h"

#define KEY_LOGIN_OPTION                @"loginOption"
#define KEY_SHORT_ORDER_PERFORM_STATE   @"soperformstate"
#define KEY_Lvdian_Account              @"lvdianaccount"
#define KEY_Push_ChannelID              @"baidupushChannelID"
#define KEY_Push_UserID                 @"baidupushUserID"

@implementation Config

+(void)setLvdianAccount:(NSString *) money
{
    [[NSUserDefaults standardUserDefaults] setObject:money forKey:KEY_Lvdian_Account];
    [[NSUserDefaults standardUserDefaults] synchronize];

  
}
+(NSString *) lvdianAccount
{

  return [[NSUserDefaults standardUserDefaults] stringForKey:KEY_Lvdian_Account];
}


+(void)setBaiDuPushChannelID:(NSString *)channelID
{
    [[NSUserDefaults standardUserDefaults] setObject:channelID forKey:KEY_Push_ChannelID];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    
}
+(NSString *) baiduPushChannelID
{
    
    return [[NSUserDefaults standardUserDefaults] stringForKey:KEY_Push_ChannelID];
}

//推送识别码

+(void)setBaiDuPushUserID:(NSString *)pushUserID
{
    [[NSUserDefaults standardUserDefaults] setObject:pushUserID forKey:KEY_Push_UserID];
    [[NSUserDefaults standardUserDefaults] synchronize];
    
    
}
+(NSString *) baiduPushUserID
{
    
    return [[NSUserDefaults standardUserDefaults] stringForKey:KEY_Push_UserID];
}

+ (void) setLoginOption : (NSString*)option
{
    [[NSUserDefaults standardUserDefaults] setObject:option forKey:KEY_LOGIN_OPTION];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (NSString*) loginOption
{
    return [[NSUserDefaults standardUserDefaults] stringForKey:KEY_LOGIN_OPTION];
}

+ (void) setShortOrderPerformState : (NSInteger)orderId
{
    NSString * sOrder = [NSString stringWithFormat:@"%d", orderId];
    
    [[NSUserDefaults standardUserDefaults] setObject:sOrder forKey:KEY_SHORT_ORDER_PERFORM_STATE];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (NSInteger) shortOrderPerformState
{
    NSInteger retVal = 0;
    NSString * sOrder = [[NSUserDefaults standardUserDefaults] stringForKey:KEY_SHORT_ORDER_PERFORM_STATE];
    
    if (sOrder != nil) {
        retVal = [sOrder intValue];
    }
    
    return retVal;
}


@end
