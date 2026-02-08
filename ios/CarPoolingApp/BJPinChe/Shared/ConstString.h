//
//  ServiceParams.h
//  BodyWear
//
//  Created by ChungJin.Sim on 9/4/13.
//  Copyright (c) 2013 damytech. All rights reserved.
//
//来源
#define CHANNEL_FLAG                   @"oo"

// 网络请求返回状态
#define SVCERR_SUCCESS					0                       // 성공
#define SVCERR_FAILURE					-1
#define SVCERR_DUPLICATE_USER			-2
#define NotEnoughLvDian					-5
#define SVCERR_ALREADY_ACCEPTED			-6
#define SVCERR_ALREADY_CANCELLED		-7
#define SVCERR_ALREADY_OLDPWDWORNG		-8


#define DEF_DELAY                       2

#define DEF_WAITING_DELAY               10

#define COUNTDOWN_PROGRESS_MAX          1

#define iFlyMSC_APPID                   @"540fc211"
#define iFlyMSC_TIMEOUT_VALUE           @"20000"


#define LOGIN_OPTION_CUSTOMER           @"0"
#define LOGIN_OPTION_DRIVER             @"1"

#define CUR_PLATFORM                    @"1"                    // iOS
#define CUR_SOURCE                      @"11"                    // PinChe App
#define CUSTOMER_SIDE                   1
#define DRIVER_SIDE                     2

#define SVCERR_MSG_ERROR                @"跟服务器连接失败"        // HttpClient오유

#define SVCERR_MSG_SUCCESS              @""

#define MSG_PLEASE_WAIT                 @"请稍候..."
#define MSG_SUCCESS                     @"操作成功!"

#define YUANSYMBOL                      @"￥"
#define YUAN                            @"元"
#define DIAN                            @"点"

#define TEXT_GONGLI                     @"公里"
#define TEXT_FENZHONG                   @"分钟"

// Company Information
#define COMPLAIN_TEL_NUM                @"4008700111"

// Common Text
#define STR_BUTTON_CONFIRM              @"确定"
#define STR_BUTTON_CANCEL               @"取消"
#define STRING_DATAMANAGER_PHOTOSIZE	@"头像图片大小不可超过 256x256"
#define MSG_PLEASE_WAIT                 @"请稍候..."
#define MSG_CORRECT_SECPWD              @"密码不一致"
#define MSG_SELECT_IMAGE                @"请选择图片"

#define STR_NOTVERIFIED                 @"未通过"
#define STR_VERIFIED                    @"已通过"
#define STR_VERI_WAITING                @"审核中"

#define MSG_REMOVE_BIND                 @"是否确定解除绑定？"
#define MSG_REMOVE_BIND_SUCCESS         @"已解除账户绑定！"
#define MSG_NO_BIND_BANK                @"没有绑定银行卡"
#define MSG_BIND_INCORRECT_PWD          @"登录密码有误，请检查后重新提交"

// Usual Route
#define MSG_NO_STARTADDR                @"请输入出发地"
#define MSG_NO_ENDADDR                  @"请输入目的地"
#define MSG_NO_STARTTIME                @"请选择出发时间"

// Customer Long Route
#define MSG_NO_STARTCITY                @"请输入起始城市"
#define MSG_NO_ENDCITY                  @"请输入目的城市"

// Pull to refresh table
#define MSG_TBLHEADER_PULL              @"下拉可以刷新了"
#define MSG_TBLHEADER_RELEASE           @"松开马上刷新了"
#define MSG_TBLHEADER_REFRESHING        @"正在刷新中..."

#define MSG_TBLFOOTER_PULL              @"上拉可以加载更多数据了"
#define MSG_TBLFOOTER_RELEASE           @"松开马上加载更多数据了"
#define MSG_TBLFOOTER_REFRESHING        @"正在加载中..."

// FenXiang
#define IMAGE_EXT                       @"png"
#define IMAGE_NAME                      @"demo_qrcode"
#define CONTENT                         @"分享内容！！！"
#define SHARE_TITLE                     @"请关注 OO车生活 "

#define SHARE_URL                       @"http://www.weiwins.com"

// Feedback
#define FEEDBACK_HINT                   @"有什么话想对OO说吗？在这里写下来吧"

///////////////////////////////// Evaluation String //////////////////////////////
#define CUS_DEF_GOODEVAL                @"默认好评"
#define CUS_DEF_BADEVAL1                @"不经充许抽烟"
#define CUS_DEF_BADEVAL2                @"语言骚扰"
#define CUS_DEF_BADEVAL3                @"迟到或不发车"
#define CUS_DEF_BADEVAL4                @"不经充许绕路"

#define DRV_DEF_GOODEVAL                @"默认好评"
#define DRV_DEF_BADEVAL1                @"车上吃东西"
#define DRV_DEF_BADEVAL2                @"损坏座椅靠垫"
#define DRV_DEF_BADEVAL3                @"长时间迟到"
#define DRV_DEF_BADEVAL4                @"待补充... ..."


///////////////////////////////// Basic Service Value //////////////////////////////
#define SVCC_RESULT                     @"result"
#define SVCC_RET                        @"retcode"
#define SVCC_RETMSG                     @"retmsg"
#define SVCC_DATA                       @"retdata"


///////////////////////////////// Car Verifying Image Paths ////////////////////////
#define CAR_VERIFY_DRIVER_LICENSE_FORE_IMAGE    @"cached_driver_license_fore"
#define CAR_VERIFY_DRIVER_LICENSE_BACK_IMAGE    @"cached_driver_license_back"
#define CAR_VERIFY_CAR_IMAGE                    @"cached_car"
#define CAR_VERIFY_DRIVING_LICENSE_FORE_IMAGE   @"cached_driving_license_fore"
#define CAR_VERIFY_DRIVING_LICENSE_BACK_IMAGE   @"cached_driving_license_back"


///////////////////////////////// longOrder message String ////////////////////////

#define LONG_ORDER_SEAT_PRICE_HIGH_ERROR @"订单金额最大为9999"
#define LONG_ORDER_SEAT_PRICE_LOW_ERROR @"订单金额最小为1"
#define LONG_ORDER_SEAT_NUMBER_HIGH_ERROR @"座位数最大为30"
#define LONG_ORDER_SEAT_NUMBER_LOW_ERROR @"座位数最小为1"
#define LONG_ORDER_REAMARK_LENGTH_ERROR @"备注字数最多为200字"

#define LONG_ORDER_INFO_FEE_ERROR @"获取信息费计算方式错误"

#define GET_DRIVER_POS_ERROR @"获取司机位置失败"
