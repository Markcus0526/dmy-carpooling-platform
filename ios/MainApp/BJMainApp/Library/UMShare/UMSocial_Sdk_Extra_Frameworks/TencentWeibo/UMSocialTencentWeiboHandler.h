//
//  UMSocialTencentWeiboHandler.h
//  SocialSDK
//
//  Created by yeahugo on 14-5-28.
//  Copyright (c) 2014年 Umeng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface UMSocialTencentWeiboHandler : NSObject

/**
 设置腾讯微博appKey，appSecrete和redirectUrl
 
 @param appKey
 @param secrete
 @param redirectUrl
 */
+(void)openSSOWithRedirectUrl:(NSString *)redirectUrl;

@end
