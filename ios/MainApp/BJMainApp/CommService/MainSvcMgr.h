//
//  MainSvcMgr.h
//  Yilebang
//
//  Created by KimOC on 12/18/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol MainSvcDelegate;

@interface MainSvcMgr : NSObject {
}

@property(strong, nonatomic) id<MainSvcDelegate> delegate;

- (void) GetChlidAppList : (NSString *)imei;
- (void) GetLatestAnnounce : (NSString *)city driververif:(NSString *)driververif limitid:(NSString *)limitid userid:(long)userid devtoken:(NSString *)devtoken;
- (void) GetAnnouncePage : (NSString *)city driververif:(NSString *)driververif pageno:(int)pageno userid:(long)userid devtoken:(NSString *)devtoken;
- (void) GetLatestOrderInfos : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken;
- (void) GetOrderInfoPage : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken;
- (void) GetLatestNoti : (long)userid limitid:(long)limitid devtoken:(NSString *)devtoken;
- (void) GetNotiPage : (long)userid pageno:(int)pageno devtoken:(NSString *)devtoken;
- (void) GetCouponDetail : (long)userid couponid:(long)couponid devtoken:(NSString *)devtoken;

- (void) hasNewsWithUserID:(long)userid city:(NSString*)city driververif:(int)driververif lastannounceid:(long)lastannounceid lastordernotifid:(long)lastordernotifid lastpersonnotifid:(long)lastpersonnotifid devtoken:(NSString*)devtoken;
- (void) readOrderNotifsWithUserID:(long)userid orderNotifyID:(long)ordernotifid devtoken:(NSString*)devtoken;
- (void) readPersonNotifsWithUserID:(long)userid personNotifyID:(long)personnotifid devtoken:(NSString*)devtoken;


@end

// service protocol
@protocol MainSvcDelegate <NSObject>

@optional
- (void) getChildAppListResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestAnnounceResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getAnnouncePageResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestOrderinfosResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getOrderInfoPageResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getLatestNotiResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getNotiPageResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getCouponDetailResult : (NSString *)result dataInfo:(STCouponInfo *)dataInfo;

- (void) hasNewsResultCode:(int)retcode retmsg:(NSString*)retmsg announcement:(int)announcement ordernotif:(int)ordernotif personnotif:(int)personnotif;
- (void) readOrderNotifyResultCode:(int)retcode retmsg:(NSString*)retmsg uid:(long)ordernotifid;
- (void) readPersonNofityResultCode:(int)retcode retmsg:(NSString*)retmsg uid:(long)personnotifid;

@end
