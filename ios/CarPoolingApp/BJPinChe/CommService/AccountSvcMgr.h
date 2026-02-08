//
//  MainSvcMgr.h
//  Yilebang
//
//  Created by KimOC on 12/18/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "STDataInfo.h"

@protocol AccountSvcDelegate;

@interface AccountSvcMgr : NSObject {
}

@property(strong, nonatomic) id<AccountSvcDelegate> delegate;

- (void) RegisterUser : (STRegUserInfo *)reginfo;
- (void) GetVerifKey : (NSString *)mobile andRegistered:(NSInteger )registered;

- (void) LoginUser : (NSString *)username password:(NSString *)password city:(NSString *)city devtoken:(NSString *)devtoken;
- (void) GetLoginInfoFromDevToken : (NSString *)devtoken;
- (void) ForgetPassword : (NSString *)username mobile:(NSString *)mobile newpwd:(NSString *)newpwd;
- (void) ChangeUserInfo : (long)userid mobile:(NSString *)mobile nickname:(NSString *)nickname birthday:(NSString *)birthday sex:(NSString *)sex photo:(NSString *)photo photo_changed:(int)photo_changed carimg:(NSString *)carimg carimg_changed:(int)carimg_changed devtoken:(NSString *)devtoken;
- (void)changeUserPhotoWithUserID:(long)userid photo:(NSString*)userphoto devtoken:(NSString*)devtoken;
- (void) ChangePassword : (long)userid oldpwd:(NSString *)oldpwd newpwd:(NSString *)newpwd devtoken:(NSString *)devtoken;
- (void) VerifyPersonInfo : (long)userid idforeimg:(NSString *)idforeimg idbackimg:(NSString *)idbackimg devtoken:(NSString *)devtoken;
- (void) VerifyDriver;
- (void) GetUserInfo : (long)userid devtoken:(NSString *)devtoken;
- (void) GetLatestAppVersion :(NSString *)devtoken;
- (void) GetDefShareContents : (long)userid devtoken:(NSString *)devtoken;
- (void) GetUsualRoutes : (long)userid type:(int)type devtoken:(NSString *)devtoken;
- (void) DelUsualRoute : (long)userid route_id:(long)route_id devtoken:(NSString *)devtoken;
- (void) ChangeUsualRoute : (long)userid route_id:(long)route_id type:(int)type daytype:(int)daytype startcity:(NSString *)startcity endcity:(NSString *)endcity startaddr:(NSString *)startaddr endaddr:(NSString *)endaddr startlat:(double)startlat startlng:(double)startlng endlat:(double)endlat endlng:(double)endlng city:(NSString *)city start_time:(NSString *)start_time devtoken:(NSString *)devtoken;
- (void) AddRoute : (long)userid type:(int)type daytype:(int)daytype startcity:(NSString*)startcity endcity:(NSString*)endcity startaddr:(NSString *)startaddr endaddr:(NSString *)endaddr startlat:(double)startlat startlng:(double)startlng endlat:(double)endlat endlng:(double)endlng city:(NSString *)city start_time:(NSString *)start_time devtoken:(NSString *)devtoken;
- (void) ReportDriverPos:(long)userid latitude:(double)lat longitude:(double)lng;
- (void) GetNearbyDrivers:(long)userid latitude:(double)lat longitude:(double)lng;
- (void) GetBrandsAndColors;
- (void) LogoutUser : (long)userid devtoken:(NSString *)devtoken;

- (void) getOnceOrderDriverPos:(long)userid	orderid:(long)orderid devtoken:(NSString *)devtoken;

@end

// service protocol
@protocol AccountSvcDelegate <NSObject>

@optional

- (void) registerUserResult : (NSString *)result userinfo:(STUserInfo *)userinfo;
- (void) getVerifKeyResult : (NSString *)result keydata:(NSString *)keydata;
- (void) loginUserResult : (NSString *)result userinfo:(STUserInfo *)userinfo exeinfo:(STExeOrderInfo *)exeinfo;
- (void) loginInfoFromDevTokenResult : (NSString *)result userinfo:(STUserInfo *)userinfo exeinfo:(STExeOrderInfo *)exeinfo;
- (void) forgetPasswordResult : (NSString *)result;
- (void) changeUserInfoResult : (NSString *)result;
- (void) changePasswordResult : (NSString *)result;
- (void) verifyPersonInfoResult : (NSString *)result;
- (void) verifyDriverResult : (NSString *)result;
- (void) getUserInfoResult : (NSString *)result userinfo:(STUserInfo *)userinfo;
- (void) getLatestAppVersion :(NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getDefShareContentsResult : (NSString *)result contents1:(NSString *)contents1 contents2:(NSString *)contents2 contents3:(NSString *)contents3;
- (void) getUsualRoutesResult : (NSString *)reuslt dataList:(NSMutableArray *)dataList;
- (void) delUsualRouteResult : (NSString *)result;
- (void) changeUsualRouteResult : (NSString *)result;
- (void) addRouteResult : (NSString *)reuslt;
- (void) reportDriverPosResult : (NSString *)result;
- (void) getNearbyDriversResult : (NSString *)result dataList:(NSMutableArray *)dataList;
- (void) getBrandsAndColorsResult : (NSString *)result brandList:(NSMutableArray *)brandList colorList:(NSMutableArray *)colorList;
- (void) logoutUserResult : (NSString *)result;

- (void) duplicateUser : (NSString *)result;

-(void) getGetOnceOrderDriverPos :(NSString *)result dataList:(NSMutableArray *)dataList;
- (void)changeUserPhotoResult:(int)retcode retmsg:(NSString*)retmsg photo_url:(NSString*)photo_url;

@end
