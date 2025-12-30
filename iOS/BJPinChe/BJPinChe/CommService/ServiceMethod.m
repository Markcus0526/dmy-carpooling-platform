//
//  ServiceMethod.m
//  4S-C
//
//  Created by RyuCJ on 24/10/2012.
//  Copyright (c) 2012 . All rights reserved.
//  网络接口地址

#import "ServiceMethod.h"

@implementation ServiceMethod

// Service Addr基本url地址
+ (NSString *) getBaseServiceAddress
{//10100
    
// return @"http://124.207.135.69:8080/PincheService/webservice/";  //发布
  return @"http://182.92.237.144:8082/PincheService/webservice/";
    //测试
//   return @"http://192.168.1.101:8888/PincheService/webservice/";//刘晨
  //  return @"http://192.168.1.18:10100/PincheService/webservice/";//德铭源
//    return @"http://192.168.1.48:10470/PincheService/webservice/";
//    return @"http://192.168.1.47:8080/PincheService/webservice/";
}

+ (NSString *) cmdReportDriverPos
{
    return @"reportDriverPos";
}

+ (NSString *) cmdGetNearbyDrivers
{
    return @"getNearbyDrivers";
}

+ (NSString *) cmdInsertDriverAcceptableOrders
{
    return @"insertDriverAcceptableOrders";
}

+ (NSString *) cmdGetLatestAcceptableOnceOrders
{
    return @"getLatestAcceptableOnceOrders";
}

+ (NSString *) cmdGetPagedAcceptableOnceOrders
{
    return @"getPagedAcceptableOnceOrders";
}

+ (NSString *) cmdGetLatestAcceptableOnOffOrders
{
    return @"getLatestAcceptableOnOffOrders";
}

+ (NSString *) cmdGetPagedAcceptableOnOffOrders
{
    return @"getPagedAcceptableOnOffOrders";
}

+ (NSString *) cmdGetDetailedDriverOrderInfo
{
    return @"getDetailedDriverOrderInfo";
}

+ (NSString *) cmdGetDetailedPassengerOrderInfo
{
    return @"getDetailedPassengerOrderInfo";
}

+ (NSString *) cmdGetDriverInfo
{
    return @"getDriverInfo";
}

+ (NSString *) cmdGetDriverLatestEvalInfo
{
    return @"getDriverLatestEvalInfo";
}

+ (NSString *) cmdGetDriverPagedEvalInfo
{
    return @"getDriverPagedEvalInfo";
}

+ (NSString *) cmdGetPassengerInfo
{
    return @"getPassengerInfo";
}

+ (NSString *) cmdGetPassengerLatestEvalInfo
{
    return @"getPassengerLatestEvalInfo";
}

+ (NSString *) cmdGetPassengerPagedEvalInfo
{
    return @"getPassengerPagedEvalInfo";
}

+ (NSString *) cmdGetBrandsAndColors
{
    return @"getBrandsAndColors";
}

+ (NSString *) cmdLogoutUser
{
    return @"logoutUser";
}
+ (NSString *)cmdPublishOnceOrderAddPrice
{
//    return @"publishOnceOrderAddPrice";
    return @"changeOnceOrderPrice";
}
+ (NSString *) cmdPublishOnceOrder
{
    return @"publishOnceOrder";
}

+ (NSString *) cmdPublishOnOffOrder
{
    return @"publishOnOffOrder";
}

+ (NSString *) cmdAcceptOnceOrder
{
    return @"acceptOnceOrder";
}

+ (NSString *) cmdAcceptOnOffOrder
{
    return @"acceptOnOffOrder";
}

+ (NSString *) cmdGetAcceptableInCityOrderDetailInfo
{
    return @"getAcceptableInCityOrderDetailInfo";
}

+ (NSString *) cmdExecuteOnceOrder
{
	return @"executeOnceOrder";
}

+ (NSString *) cmdExecuteOnOffOrder
{
	return @"executeOnOffOrder";
}

+ (NSString *) cmdSignOnceOrderDriverArrival
{
	return @"signOnceOrderDriverArrival";
}

+ (NSString *) cmdSignOnOffOrderDriverArrival
{
	return @"signOnOffOrderDriverArrival";
}

+ (NSString *) cmdSignOnceOrderPassengerUpload
{
	return @"signOnceOrderPassengerUpload";
}

+ (NSString *) cmdSignOnOffOrderPassengerUpload
{
	return @"signOnOffOrderPassengerUpload";
}

+ (NSString *) cmdEndOnceOrder
{
	return @"endOnceOrder";
}

+ (NSString *) cmdEndOnOffOrder
{
	return @"endOnOffOrder";
}

+ (NSString *) cmdCheckOnceOrderAcceptance
{
	return @"checkOnceOrderAcceptance";
}

+ (NSString *) cmdCheckOnceOrderAgree
{
	return @"checkOnceOrderAgree";
}

+ (NSString *) cmdConfirmOnceOrder
{
	return @"confirmOnceOrder";
}

+(NSString *)setOnceOrderPassword  //Ma
{
 return @"setOnceOrderPassword";
}
+ (NSString *) cmdConfirmOnOffOrder
{
	return @"confirmOnOffOrder";
}

+ (NSString *) cmdCancelOnceOrder
{
	return @"cancelOnceOrder";
}

+ (NSString *) cmdCancelOnOffOrder
{
	return @"cancelOnOffOrder";
}

+ (NSString *) cmdRefuseOnOffOrder
{
    return @"refuseOnOffOrder";
}

+ (NSString *) cmdPauseOnOffOrder
{
    return @"pauseOnOffOrder";
}

+ (NSString *) cmdGetDriverPos
{
    return @"getDriverPos";
}

+ (NSString *)cmdGetOnceOrderDriverPos
{
    return @"getOnceOrderDriverPos";
}

+ (NSString *) cmdEvaluateOnceOrderPass
{
	return @"evaluateOnceOrderPass";
}

+ (NSString *) cmdEvaluateOnOffOrderPass
{
	return @"evaluateOnOffOrderPass";
}

+ (NSString *) cmdEvaluateOnceOrderDriver
{
	return @"evaluateOnceOrderDriver";
}

+ (NSString *) cmdEvaluateOnOffOrderDriver
{
	return @"evaluateOnOffOrderDriver";
}

+ (NSString *) cmdPayNormalOrder
{
	return @"payNormalOrder";
}

+ (NSString *) cmdOrderCancelledState
{
    return @"orderCancelled";
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
    return @"getVerifKey.action";
}

+ (NSString *) cmdLoginUser
{
    return @"loginUser.action";
}

+ (NSString *) cmdGetLoginInfoFromDevToken
{
    return @"getLoginInfoFromDevToken";
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

+ (NSString *) cmdVerifyDriver
{
    return @"verifyDriver.action";
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

+ (NSString *) cmdGetUsualRoutes
{
    return @"getRoutes.action";
}

+ (NSString *) cmdAddRoute
{
    return @"addRoute.action";
}

+ (NSString *) cmdDelUsualRoute
{
    return @"removeRoute.action";
}

+ (NSString *) cmdChangeUsualRoute
{
    return @"changeRoute.action";
}

+ (NSString *) cmdExecuteLongOrder
{
    return @"executeLongOrder";
}

+ (NSString *) cmdSignLongOrderDriverArrival
{
    return @"signLongOrderDriverArrival";
}

+ (NSString *) cmdSignLongOrderPassengerUpload
{
    return @"signLongOrderPassengerUpload";
}

+ (NSString *) cmdSignLongOrderPassengerGiveup
{
    return @"signLongOrderPassengerGiveup";
}

+ (NSString *) cmdStartLongOrderDriving
{
    return @"startLongOrderDriving";
}

+ (NSString *) cmdEndLongOrder
{
    return @"endLongOrder";
}

+ (NSString *) cmdEvaluateLongOrderPass
{
    return @"evaluateLongOrderPass";
}

+ (NSString *) cmdEvaluateLongOrderDriver
{
    return @"evaluateLongOrderDriver";
}

+ (NSString *) cmdGetDefShareContents
{
    return @"defaultShareContents.action";
}

+ (NSString *) cmdGetLatestAppVersion
{
    return @"getLatestAppVersion";
}


+ (NSString *) cmdGetPagedDriverOrders
{
    return @"getPagedDriverOrders";
}

+ (NSString *) cmdGetLatestDriverOrders
{
    return @"getLatestDriverOrders";
}

+ (NSString *) cmdGetPagedPassengerOrders
{
    return @"getPagedPassengerOrders";
}

+ (NSString *) cmdGetLatestPassengerOrders
{
    return @"getLatestPassengerOrders";
}

+ (NSString *) cmdGetLatestAcceptableLongOrders
{
    return @"getLatestAcceptableLongOrders.action";
}
+ (NSString *) cmdGetPagedAcceptableLongOrders
{
    return @"getPagedAcceptableLongOrders.action";
}
+ (NSString *) cmdPublishLongOrder
{
    return @"publishLongOrder.action";
}
+ (NSString *) cmdAcceptLongOrder
{
    return @"acceptLongOrder.action";
}
+ (NSString *) cmdSetLongOrderPassword
{
    return @"setLongOrderPassword.action";
}
+ (NSString *)cmdGetAcceptableLongOrderDetailInfo
{
    return @"getAcceptableLongOrderDetailInfo.action";
}

+ (NSString *)cmdGetInfoFeeCalcMethod
{
    return @"getInfoFeeCalcMethod.action";
}

+ (NSString *)cmdGetCancelLongOrder
{
    return @"cancelLongOrder";
}
+ (NSString *) cmdGetLongOrderCancelInfo
{
    return @"getLongOrderCancelInfo";
}
+ (NSString *) cmdClickchargingbtn
{
    return @"clickchargingbtn";
}
+ (NSString *) cmdHas_clickedchargingbtn
{
    return @"has_clickedchargingbtn";
}

@end
