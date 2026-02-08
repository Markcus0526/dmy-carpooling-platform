//
//  CommManager.h
//  FourService
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 PIC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "STDataInfo.h"
#import "MainSvcMgr.h"
#import "AccountSvcMgr.h"
#import "TradeSvcMgr.h"

/////////////////////////////////////////////////////////////
#pragma mark - Communication Manager Interface
@interface CommManager  :NSObject {
    NSString *      bathServiceUrl;
    MainSvcMgr *    mainSvcMgr;
    AccountSvcMgr * accountSvcMgr;
    TradeSvcMgr *   tradeSvcMgr;
    
    CommManager *   commMgr;
}

+ (CommManager *)getCommMgr;
+ (BOOL)hasConnectivity;
- (void)loadCommModules;

@property (nonatomic, retain) NSString * bathServiceUrl;
@property (nonatomic, retain) MainSvcMgr * mainSvcMgr;
@property (nonatomic, retain) AccountSvcMgr * accountSvcMgr;
@property (nonatomic, retain) TradeSvcMgr * tradeSvcMgr;

@property (nonatomic, retain) CommManager *commMgr;

@end

