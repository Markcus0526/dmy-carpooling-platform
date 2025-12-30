//
//  ServiceMethod.m
//  4S-C
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 . All rights reserved.
//

#import "ServiceMethod.h"

@implementation ServiceMethod

// Service Addr
+ (NSString *) getBaseServiceAddress
{
	return @"http://124.207.135.69:8080/PincheService/webservice/";
//    return @"http://192.168.1.79:10100/PincheService/webservice/";
    //return @"http://192.168.1.79:10100/PincheService/mainwebservice/";
//    return @"http://192.168.1.38:10470/PincheService/mainwebservice/";
}

+ (NSString *) cmdGetChildAppList
{
    return @"getChildAppList.action";
}

+ (NSString *) cmdGetLatestAnnounce
{
    return @"getLatestAnnouncements.action";
}

+ (NSString *) cmdGetAnnouncePage
{
    return @"getAnnouncementsPage.action";
}

+ (NSString *) cmdGetLatestOrderInfos
{
    return @"getLatestOrderInfos.action";
}

+ (NSString *) cmdGetOrderInfosPage
{
    return @"getOrderInfosPage.action";
}

+ (NSString *) cmdGetLatestNotis
{
    return @"getLatestNotifications.action";
}

+ (NSString *) cmdGetNotiPage
{
    return @"getNotificationsPage.action";
}

+ (NSString *) cmdGetCouponDetails
{
    return @"getCouponDetails.action";
}

+ (NSString *) cmdRegisterUser
{
    return @"registerUser.action";
}

+ (NSString *) cmdGetVerifKey
{
   // return @"getVerifkey.action";
    return @"getVerifKey.action";
}

+ (NSString *) cmdLoginUser
{
    return @"loginUser.action";
}

+ (NSString *) cmdForgetPassword
{
    return @"forgetPassword.action";
}

+ (NSString *) cmdGetUserInfo
{
    return @"getUserInfo.action";
}

+ (NSString *) cmdChangeUserInfo
{
    return @"changeUserInfo.action";
}

+ (NSString *) cmdChangePassword
{
    return @"changePassword.action";
}

+ (NSString *) cmdVerifyPersonInfo
{
    return @"verifyPersonInfo.action";
}

+ (NSString *) cmdHasNews
{
    return @"hasNews.action";
}

+ (NSString *) cmdReadOrderNotifs
{
    return @"readOrderNotifs.action";
}

+ (NSString *) cmdReadPersonNotifs
{
    return @"readPersonNotifs.action";
}

+ (NSString *) cmdGetTsLogPage
{
    return @"getTsLogsPage.action";
}

+ (NSString *) cmdLatestTsLogs
{
    return @"getLatestTsLogs.action";
}

+ (NSString *) cmdCharge
{
    return @"charge.action";
}

+ (NSString *) cmdGetAccount
{
    return @"getAccount.action";
}

+ (NSString *) cmdWithdraw
{
    return @"withdraw.action";
}

+ (NSString *) cmdBindBankCard
{
    return @"bindBankCard.action";
}

+ (NSString *) cmdReleaseBankCard
{
    return @"releaseBankCard.action";
}

+ (NSString *) cmdGetLatestCoupon
{
    return @"getLatestCoupon.action";
}

+ (NSString *) cmdGetPagedCoupon
{
    return @"getPagedCoupon.action";
}

+ (NSString *) cmdAddCoupon
{
    return @"addCoupon.action";
}

+ (NSString *) cmdAdvanceOpinion
{
    return @"advanceOpinion.action";
}

+ (NSString *) cmdDefShareContents
{
    return @"defaultShareContents.action";
}

+ (NSString *) cmdGetMoney
{
    return @"getMoney.action";
}

+ (NSString *) cmdGetDefShareContents
{
    return @"defaultShareContents.action";
}

+ (NSString *) cmdGetLoginFromDevToken
{
    return @"getLoginInfoFromDevToken.action";
}

+ (NSString *) cmdUpdateUserPhoto
{
    return @"changeUserPhoto";
}


+ (NSString *) cmdLogoutUser
{
    return @"logoutUser";
}

@end
