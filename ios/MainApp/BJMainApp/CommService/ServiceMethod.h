//
//  STServiceInfo.h
//  4S-C
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 PIC. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ServiceMethod : NSObject {
}

// Service Addr
+ (NSString *) getBaseServiceAddress;

+ (NSString *) cmdGetChildAppList;
+ (NSString *) cmdGetLatestAnnounce;
+ (NSString *) cmdGetAnnouncePage;
+ (NSString *) cmdGetLatestOrderInfos;
+ (NSString *) cmdGetOrderInfosPage;
+ (NSString *) cmdGetLatestNotis;
+ (NSString *) cmdGetNotiPage;
+ (NSString *) cmdGetCouponDetails;

+ (NSString *) cmdRegisterUser;
+ (NSString *) cmdGetVerifKey;
+ (NSString *) cmdLoginUser;
+ (NSString *) cmdForgetPassword;
+ (NSString *) cmdGetUserInfo;
+ (NSString *) cmdChangeUserInfo;
+ (NSString *) cmdChangePassword;
+ (NSString *) cmdVerifyPersonInfo;

+ (NSString *) cmdHasNews;
+ (NSString *) cmdReadOrderNotifs;
+ (NSString *) cmdReadPersonNotifs;
+ (NSString *) cmdGetTsLogPage;
+ (NSString *) cmdLatestTsLogs;
+ (NSString *) cmdCharge;
+ (NSString *) cmdGetAccount;
+ (NSString *) cmdWithdraw;
+ (NSString *) cmdBindBankCard;
+ (NSString *) cmdReleaseBankCard;
+ (NSString *) cmdGetLatestCoupon;
+ (NSString *) cmdGetPagedCoupon;
+ (NSString *) cmdAddCoupon;
+ (NSString *) cmdAdvanceOpinion;
+ (NSString *) cmdDefShareContents;
+ (NSString *) cmdGetMoney;

+ (NSString *) cmdGetDefShareContents;
+ (NSString *) cmdGetLoginFromDevToken;
+ (NSString *) cmdUpdateUserPhoto;
+ (NSString *) cmdLogoutUser;

@end
