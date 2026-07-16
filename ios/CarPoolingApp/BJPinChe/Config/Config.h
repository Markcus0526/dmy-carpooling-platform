//
//  Common.h
//  4S-C
//
//  Created by ChungJin.Sim on 9/6/13.
//  Copyright (c) 2013 PIC. All rights reserved.
//

@interface Config : NSObject {
}

+ (void) setLoginOption : (NSString*)option;
+ (NSString*) loginOption;

+ (void) setShortOrderPerformState : (NSInteger)orderId;
+ (NSInteger) shortOrderPerformState;


+(void)setLvdianAccount:(NSString *) money;
+(NSString *) lvdianAccount;


+(void)setBaiDuPushChannelID:(NSString *)channelID;
+(NSString *) baiduPushChannelID;

+(void)setBaiDuPushUserID:(NSString *)pushUserID;
+(NSString *) baiduPushUserID;

@end
