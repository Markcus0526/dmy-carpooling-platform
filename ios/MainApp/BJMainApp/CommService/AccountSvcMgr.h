//
//  MainSvcMgr.h
//  Yilebang
//
//  Created by KimOC on 12/18/13.
//  Copyright (c) 2013 KimOC. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol AccountSvcDelegate;

@interface AccountSvcMgr : NSObject {
}

@property(strong, nonatomic) id<AccountSvcDelegate> delegate;

- (void) RegisterUser : (STRegUserInfo *)reginfo;
- (void) GetVerifKey : (NSString *)mobile;
- (void) LoginUser : (NSString *)username password:(NSString *)password city:(NSString *)city devtoken:(NSString *)devtoken;
- (void) ForgetPassword : (NSString *)username mobile:(NSString *)mobile newpwd:(NSString *)newpwd;
- (void) ChangeUserInfo : (long)userid mobile:(NSString *)mobile nickname:(NSString *)nickname birthday:(NSString *)birthday sex:(NSString *)sex devtoken:(NSString *)devtoken;
- (void) ChangePassword : (long)userid oldpwd:(NSString *)oldpwd newpwd:(NSString *)newpwd devtoken:(NSString *)devtoken;
- (void) VerifyPersonInfo : (long)userid idforeimg:(NSString *)idforeimg idbackimg:(NSString *)idbackimg devtoken:(NSString *)devtoken;
- (void) GetUserInfo : (long)userid devtoken:(NSString *)devtoken;
- (void) GetDefShareContents : (long)userid devtoken:(NSString *)devtoken;
- (void) GetLoginFromDevToken : (NSString *)devtoken;
- (void) UpdateUserPhoto : (long)userid userphoto:(NSString *)userphoto devtoken:(NSString *)devtoken;
- (void)GetLatestAppVersion:(NSString *)devtoken;

- (void) LogoutUser : (long)userid devtoken:(NSString *)devtoken;
@end

// service protocol
@protocol AccountSvcDelegate <NSObject>

@optional

- (void) registerUserResult : (NSString *)result userinfo:(STUserInfo *)userinfo;
- (void) getVerifKeyResult : (NSString *)result keydata:(NSString *)keydata;
- (void) loginUserResult : (NSString *)result userinfo:(STUserInfo *)userinfo;
- (void) forgetPasswordResult : (NSString *)result;
- (void) changeUserInfoResult : (NSString *)result;
- (void) changePasswordResult : (NSString *)result;
- (void) verifyPersonInfoResult : (NSString *)result;
- (void) getUserInfoResult : (NSString *)result userinfo:(STUserInfo *)userinfo;
- (void) getDefShareContentsResult : (NSString *)result contents1:(NSString *)contents1 contents2:(NSString *)contents2 contents3:(NSString *)contents3;
- (void) getLoginFromDevTokenFromResult : (NSString *)result userinfo:(STUserInfo *)userinfo;
- (void) updateUserPhotoResult : (NSString *)result photourl:(NSString *)photourl;
- (void) getLatestAppVersion :(NSString *)result dataList:(NSMutableArray *)dataList;

- (void) logoutUserResult : (NSString *)result;

@end
