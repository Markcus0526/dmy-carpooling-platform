//
//  Common.m
//  4S-C
//
//  Created by R CJ on 1/5/13.
//  Copyright (c) 2013 PIC. All rights reserved.
//

#import "Config.h"

#define KEY_LOGIN_NAME              @"loginName"
#define KEY_LOGIN_PASSWORD          @"loginPassword"
#define KEY_LOGIN_OPTION            @"loginOption"

#define KEY_UNREADNEWS1				@"unreadnews1"
#define KEY_UNREADNEWS2				@"unreadnews2"
#define KEY_UNREADNEWS3				@"unreadnews3"


@implementation Config


+ (void) setLoginName : (NSString*)newLoginName
{
    [[NSUserDefaults standardUserDefaults] setObject:newLoginName forKey:KEY_LOGIN_NAME];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (NSString*) loginName
{
    return [[NSUserDefaults standardUserDefaults] stringForKey:KEY_LOGIN_NAME];
}

+ (void) setLoginPassword : (NSString*)newLoginPassword
{
    [[NSUserDefaults standardUserDefaults] setObject:newLoginPassword forKey:KEY_LOGIN_PASSWORD];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (NSString*) loginPassword
{
    return [[NSUserDefaults standardUserDefaults] stringForKey:KEY_LOGIN_PASSWORD];
}

+ (void) setUnreadNewsCount : (int)news1 news2:(int)news2 news3:(int)news3
{
    [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%d", news1] forKey:KEY_UNREADNEWS1];
	[[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%d", news2] forKey:KEY_UNREADNEWS2];
	[[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%d", news3] forKey:KEY_UNREADNEWS3];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+ (int) unreadNews1Count
{
    return [[[NSUserDefaults standardUserDefaults] stringForKey:KEY_UNREADNEWS1] intValue];
}

+ (int) unreadNews2Count
{
    return [[[NSUserDefaults standardUserDefaults] stringForKey:KEY_UNREADNEWS2] intValue];
}

+ (int) unreadNews3Count
{
    return [[[NSUserDefaults standardUserDefaults] stringForKey:KEY_UNREADNEWS3] intValue];
}

@end
