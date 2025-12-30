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
#import "OrderSvcMgr.h"
#import "OrderExecuteSvcMgr.h"
#import "GlobalSvcMgr.h"
#import "LongWayOrderSvcMgr.h"
/////////////////////////////////////////////////////////////
#pragma mark - Communication Manager Interface
@interface CommManager  :NSObject {
    NSString *      bathServiceUrl;
    MainSvcMgr *    mainSvcMgr;
    AccountSvcMgr * accountSvcMgr;
    TradeSvcMgr *   tradeSvcMgr;
    OrderSvcMgr *   orderSvcMgr;
	OrderExecuteSvcMgr *	orderExecuteSvcMgr;
    GlobalSvcMgr *  globalSvcMgr;
    LongWayOrderSvcMgr * longWayOrderSvcMgr;
    CommManager *   commMgr;
}

+ (CommManager *)getCommMgr;
+ (BOOL)hasConnectivity;
- (void)loadCommModules;

@property (nonatomic, copy) NSString * bathServiceUrl;
@property (nonatomic, strong) MainSvcMgr * mainSvcMgr;
@property (nonatomic, retain) AccountSvcMgr * accountSvcMgr;
@property (nonatomic, retain) TradeSvcMgr * tradeSvcMgr;
@property (nonatomic, retain) OrderSvcMgr * orderSvcMgr;
@property (nonatomic, retain) OrderExecuteSvcMgr * orderExecuteSvcMgr;
@property (nonatomic, retain) GlobalSvcMgr * globalSvcMgr;
@property (nonatomic, retain) LongWayOrderSvcMgr * longWayOrderSvcMgr;
@property (nonatomic, retain) CommManager *commMgr;

@end

