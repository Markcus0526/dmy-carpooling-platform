//
//  Common.h
//  4S-C
//
//  Created by ChungJin.Sim on 9/6/13.
//  Copyright (c) 2013 PIC. All rights reserved.
//

@interface Config : NSObject {
}

+ (void) setLoginName : (NSString*)newLoginName;
+ (NSString*) loginName;

+ (void) setLoginPassword : (NSString*)newLoginPassword;
+ (NSString*) loginPassword;

+ (void) setUnreadNewsCount : (int)news1 news2:(int)news2 news3:(int)news3;
+ (int) unreadNews1Count;
+ (int) unreadNews2Count;
+ (int) unreadNews3Count;

@end
