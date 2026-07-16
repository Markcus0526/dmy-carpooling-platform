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

+ (NSString *) cmdReportDriverPos;
+ (NSString *) cmdGetNearbyDrivers;
+ (NSString *) cmdInsertDriverAcceptableOrders;
+ (NSString *) cmdGetLatestAcceptableOnceOrders;
+ (NSString *) cmdGetPagedAcceptableOnceOrders;
+ (NSString *) cmdGetLatestAcceptableOnOffOrders;
+ (NSString *) cmdGetPagedAcceptableOnOffOrders;
+ (NSString *) cmdGetDetailedDriverOrderInfo;
+ (NSString *) cmdGetDetailedPassengerOrderInfo;
+ (NSString *) cmdGetDriverInfo;
+ (NSString *) cmdGetDriverLatestEvalInfo;
+ (NSString *) cmdGetDriverPagedEvalInfo;
+ (NSString *) cmdGetPassengerInfo;
+ (NSString *) cmdGetPassengerLatestEvalInfo;
+ (NSString *) cmdGetPassengerPagedEvalInfo;
+ (NSString *) cmdGetBrandsAndColors;
+ (NSString *) cmdLogoutUser;
+ (NSString *) cmdPublishOnceOrder;
+ (NSString *) cmdPublishOnceOrderAddPrice;
+ (NSString *) cmdPublishOnOffOrder;
+ (NSString *) cmdAcceptOnceOrder;
+ (NSString *) cmdAcceptOnOffOrder;
+ (NSString *) cmdGetPagedPassengerOrders;
+ (NSString *) cmdGetLatestPassengerOrders;
+ (NSString *) cmdGetAcceptableInCityOrderDetailInfo;
+ (NSString *) cmdExecuteOnceOrder;
+ (NSString *) cmdExecuteOnOffOrder;
+ (NSString *) cmdSignOnceOrderDriverArrival;
+ (NSString *) cmdSignOnOffOrderDriverArrival;
+ (NSString *) cmdSignOnceOrderPassengerUpload;
+ (NSString *) cmdSignOnOffOrderPassengerUpload;
+ (NSString *) cmdEndOnceOrder;
+ (NSString *) cmdEndOnOffOrder;
+ (NSString *) cmdCheckOnceOrderAcceptance;
+ (NSString *) cmdCheckOnceOrderAgree;
+ (NSString *) cmdConfirmOnceOrder;
+(NSString *)setOnceOrderPassword; //MA
+ (NSString *) cmdConfirmOnOffOrder;
+ (NSString *) cmdCancelOnceOrder;
+ (NSString *) cmdCancelOnOffOrder;
+ (NSString *) cmdRefuseOnOffOrder;
+ (NSString *) cmdPauseOnOffOrder;
+ (NSString *) cmdGetDriverPos;
+ (NSString *) cmdEvaluateOnceOrderPass;
+ (NSString *) cmdEvaluateOnOffOrderPass;
+ (NSString *) cmdEvaluateOnceOrderDriver;
+ (NSString *) cmdEvaluateOnOffOrderDriver;
+ (NSString *) cmdPayNormalOrder;
+ (NSString *) cmdOrderCancelledState;



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
+ (NSString *) cmdGetLoginInfoFromDevToken;
+ (NSString *) cmdForgetPassword;
+ (NSString *) cmdGetUserInfo;
+ (NSString *) cmdChangeUserInfo;
+ (NSString *) cmdChangePassword;
+ (NSString *) cmdVerifyPersonInfo;
+ (NSString *) cmdVerifyDriver;

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
+ (NSString *) cmdGetUsualRoutes;
+ (NSString *) cmdAddRoute;
+ (NSString *) cmdDelUsualRoute;
+ (NSString *) cmdChangeUsualRoute;

+ (NSString *) cmdExecuteLongOrder;
+ (NSString *) cmdSignLongOrderDriverArrival;
+ (NSString *) cmdSignLongOrderPassengerUpload;
+ (NSString *) cmdSignLongOrderPassengerGiveup;
+ (NSString *) cmdStartLongOrderDriving;
+ (NSString *) cmdEndLongOrder;
+ (NSString *) cmdEvaluateLongOrderPass;

+ (NSString *) cmdEvaluateLongOrderDriver;

+ (NSString *) cmdGetDefShareContents;
+ (NSString *) cmdGetLatestAppVersion;
+ (NSString *) cmdGetPagedDriverOrders;
+ (NSString *) cmdGetLatestDriverOrders;

+ (NSString *) cmdGetLatestAcceptableLongOrders;
+ (NSString *) cmdGetPagedAcceptableLongOrders;
+ (NSString *) cmdPublishLongOrder;
+ (NSString *) cmdAcceptLongOrder;
+ (NSString *) cmdSetLongOrderPassword;
+ (NSString *) cmdGetAcceptableLongOrderDetailInfo;
+ (NSString *) cmdGetInfoFeeCalcMethod;


//yc
//车主取消订单
+ (NSString *) cmdGetCancelLongOrder;
//乘客退票
+ (NSString *) cmdGetLongOrderCancelInfo;
//单次获取车主位置
+ (NSString *) cmdGetOnceOrderDriverPos;
//乘客点击绿点
+ (NSString *) cmdClickchargingbtn;
+ (NSString *) cmdHas_clickedchargingbtn;
@end
