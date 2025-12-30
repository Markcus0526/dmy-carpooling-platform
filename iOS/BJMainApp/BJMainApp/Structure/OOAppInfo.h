//
//  OOAppInfo.h
//  BJMainApp
//
//  Created by APP_USER on 14-10-21.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 *  子APP 数据模型
 */
@interface OOAppInfo : NSObject


@property (nonatomic, assign) long   uid;
@property (nonatomic, copy) NSString * appName;
@property (nonatomic, copy) NSString * appIcon;
@property (nonatomic, copy) NSString * appScheme;
@property (nonatomic, copy) NSString * appUrl;
@property (nonatomic, copy) NSString * code;
@property (nonatomic, copy) NSString * latestVer;
@property (nonatomic, copy) NSString * curVer;
@property (nonatomic, copy) NSString * packname;

@end
