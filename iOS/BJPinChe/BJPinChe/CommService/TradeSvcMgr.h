//
//  MainSvcMgr.h
//  Yilebang
//
//  Created by KimOC on 12/18/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol TradeSvcDelegate;

@interface TradeSvcMgr : NSObject {
}

@property(weak, nonatomic) id<TradeSvcDelegate> delegate;

- (void) GetTsLogsPage : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken;
- (void) GetLatestTsLogs : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken;
- (void) Charge : (long)userid balance:(double)balance devtoken:(NSString *)devtoken;
- (void) GetAccount : (long)userid devtoken:(NSString *)devtoken;
- (void) Withdraw : (long)userid realname:(NSString *)realname accountname:(NSString *)accountname balance:(double)balance password:(NSString *)password devtoken:(NSString *)devtoken;
- (void) BindBankCard : (long)userid bankcard:(NSString *)bankcard bankname:(NSString *)bankname subbranch:(NSString *)subbranch devtoken:(NSString *)devtoken;
- (void) ReleaseBankCard : (long)userid devtoken:(NSString *)devtoken;
- (void) GetLatestCoupon : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken;
- (void) GetPagedCoupon : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken;
- (void) AddCoupon : (long)userid active_code:(NSString *)active_code devtoken:(NSString *)devtoken;
- (void) AdvanceOpinion : (long)userid contents:(NSString *)contents devtoken:(NSString *)devtoken;
- (void) GetMoney : (long)userid devtoken:(NSString *)devtoken;

@end

// service protocol
@protocol TradeSvcDelegate <NSObject>

@optional
- (void) getTsLogsPageResult : (NSString *)result money:(double)money dataList:(NSMutableArray *)dataList;
- (void) getLatestTsLogsResult : (NSString *)result money:(double)money dataList:(NSMutableArray *)dataList;
- (void) chargeResult : (NSString *)result money:(double)money;
- (void) getAccountResult : (NSString *)result account:(STBindAccount *)account;
- (void) withdrawResult : (NSString *)result;
- (void) bindBankCardResult : (NSString *)result;
- (void) releaseBankCardResult : (NSString *)result;
- (void) getLatestCouponResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getPagedCouponResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) addCouponResult : (NSString *)result coupon:(STCouponInfo *)coupon;
- (void) advanceOpinionResult : (NSString *)result;
- (void) getMoneyResult : (NSString *)result money:(double)money;

- (void) duplicateUser : (NSString *)result;

@end
