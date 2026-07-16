package com.webapi.common;


// Class for manage all of constants to be used in web service
public class ConstMgr
{
	/*****************************************************************************/
	/********************* Error codes for SVCResult.retcode *********************/
	/*****************************************************************************/

	// Error code for success.
	public static int ErrCode_None								= 0;
	// Error code for normal errors. Detailed error cause is in description.
	public static int ErrCode_Normal							= -1;
	// Error code for not matching device. This situation comes when the aimed user account is logged in with other device.
	public static int ErrCode_DeviceNotMatch					= -2;
	// Error code for parameters.
	public static int ErrCode_Param								= -3;
	// Error code for unknown errors and exceptions.
	public static int ErrCode_Exception							= -4;
	// Error code for not enough balance.
	public static int ErrCode_NotEnoughBalance					= -5;
	// Error code for order already accepted.
	public static int ErrCode_AlreadyAccepted					= -6;
	// Error code for passenger disagreed.
	public static int ErrCode_PassDisagreed						= -7;
	// Error code for wrong old password.
	public static int ErrCode_OldPwdWrong						= -8;




	/*****************************************************************************/
	/****************** Error descriptions for SVCResult.retmsg ******************/
	/*****************************************************************************/
	public static String ErrMsg_None						= "操作成功";
	public static String ErrMsg_DeviceNotMatch				= "您的账号已退出登录，可能已在另一地点登录。";
	public static String ErrMsg_Param						= "参数格式不符合. 请查看参数是否符合条件.";
	public static String ErrMsg_Exception					= "例外错误发生了";
	public static String ErrMsg_PassDisagreed				= "抱歉, 乘客取消了该订单";

	// Normal error descriptions
	public static String ErrMsg_Normal						= "操作失败";
	public static String ErrMsg_InvalidSource				= "来源验证失败";
	public static String ErrMsg_DuplicateUserName			= "用户名重复. 请该换您的用户名.";
	public static String ErrMsg_DuplicateMobile				= "手机号重复. 请该换您的手机号码.";
	public static String ErrMsg_DuplicateNickName			= "称呼重复. 请该换您的称呼.";
	public static String ErrMsg_NotRegisteredUser			= "未注册的用户";
	public static String ErrMsg_PasswordWrong				= "密码错误";
	public static String ErrMsg_AuthenticateError			= "未认证的用户";
	public static String ErrMsg_MobileNotCorrect			= "手机号码不准确";
	public static String ErrMsg_OldPwdWrong					= "原密码不正确，尝试找回密码？";
	public static String ErrMsg_AlreadyPersonVerify			= "您的个人信息已经申请好了或者已经审核通过了";
	public static String ErrMsg_AlreadyDriverVerify			= "您的车主信息已经申请好了或者已经审核通过了";
	public static String ErrMsg_NotVerifiedPerson			= "您还没通过个人身份认证";
	public static String ErrMsg_NotVerifiedDriver			= "您还没通过车主身份认证";
	public static String ErrMsg_RealNameNotMatch			= "用户姓名不正确";
	public static String ErrMsg_LoginPwdNotMatch			= "登录密码错误，请检查后重新提交";
	public static String ErrMsg_BankCardNotBound			= "还没绑定银行卡";
	public static String ErrMsg_NotEnoughBalance			= "没有那么多余额啦，邀请好友可以挣绿点哦!";
	public static String ErrMsg_NoCouponForTheActivity		= "您输入的号码无效，请检查后重新提交";
	public static String ErrMsg_NoRemainCoupon				= "您来晚啦，点券已经发光啦";
	public static String ErrMsg_CouponAlreadyGot			= "您已经得到了该点券";
	public static String ErrMsg_NoAppInfo					= "该软件信息不存在";
	public static String ErrMsg_NoPassInfo					= "没有乘客信息";
	public static String ErrMsg_NoDrvInfo					= "没有车主信息";
	public static String ErrMsg_NoCarInfo					= "没有车辆信息";
	public static String ErrMsg_NoOrderInfo					= "没有订单信息";
	public static String ErrMsg_NotTsInfo					= "没有交易记录";
	public static String ErrMsg_NoCouponInfo				= "没有点券信息";
	public static String ErrMsg_NoCoordinateInfo			= "没有位置信息";
	public static String ErrMsg_UserInBlackList				= "抱歉, 您现在载入黑名单. 不会操作.";
	public static String ErrMsg_AlreadyAcceptedOrCancelled	= "手太慢, 该订单已经被抢单了或者取消";
	public static String ErrMsg_OrderExpired				= "抱歉, 该订单已经过时间了";
	public static String ErrMsg_NoRemainSeats				= "手太慢, 该订单已经都抢座了";
	public static String ErrMsg_NotEnoughSeats				= "抱歉, 座位数不够了";
	public static String ErrMsg_StateNotMatch				= "抱歉, 订单状态不合适";
	public static String ErrMsg_NoGrabInfo					= "例外, 没有车主接单记录";
	public static String ErrMsg_PublisherNotMatch			= "该订单不是您发布的";
	public static String ErrMsg_OrderNotAccepted			= "正在拼命推送给车主，请耐心等待~";
	public static String ErrMsg_OrderPwdNotMatch			= "电子密码不正确";
	public static String ErrMsg_OrderAutoClosed				= "所有的乘客都弃票了.该订单自动被关闭";
	public static String ErrMsg_NotAllPassProcessed			= "抱歉,至少一个乘客还没有上车或者没弃票了";
	public static String ErrMsg_NotCancelled				= "未关闭";
	public static String ErrMsg_NotAgreedYet				= "乘客还没同意了";
	public static String ErrMsg_AlreadyCancelled			= "订单已经关闭了";
	public static String ErrMsg_OrderGiveUpLimit1			= "出发时间";
	public static String ErrMsg_OrderGiveUpLimit2			= "分前才可以弃票";
	public static String ErrMsg_AlreadyEvaluated			= "该订单已经评价了";
	public static String ErrMsg_AlreadyStarted				= "抱歉, 订单已经开始执行了";
	public static String ErrMsg_AlreadyAccepted				= "抱歉, 您已经抢座了该订单";
	public static String ErrMsg_AlreadyHasOtherOrder		= "抱歉, 您已经有个订单需要执行";
	public static String ErrMsg_UserNameNotExist			= "您输入的用户名不存在";
	public static String ErrMsg_UserNamePhoneNotPair		= "电话号码不正确，请重新输入";
	public static String ErrMsg_UserNotCharge		        = "用户没有点击充值按钮";



	/*****************************************************************************/
	/***************************** tx code constants *****************************/
	/*****************************************************************************/
	public static final String txcode_userRegister			= "tx_code_001";
	public static final String txcode_returnBalance			= "tx_code_002";
	public static final String txcode_decreaseBalance		= "tx_code_003";
	public static final String txcode_systemInfo			= "tx_code_004";
	public static final String txcode_inviteFee				= "tx_code_005";
	public static final String txcode_driverFee				= "tx_code_006";
	public static final String txcode_userCharge			= "tx_code_007";
	public static final String txcode_userWithdraw			= "tx_code_008";
	public static final String txcode_userFreezeBalance		= "tx_code_009";
	public static final String txcode_userReleaseBalance	= "tx_code_010";
	public static final String txcode_backendCharge			= "tx_code_011";
	public static final String txcode_backendWithdraw		= "tx_code_012";
	public static final String txcode_couponPrice			= "tx_code_013";
	public static final String txcode_driverInvFee			= "tx_code_014";
	public static final String txcode_passengerInvFee		= "tx_code_015";
	public static final String txcode_insuFee				= "tx_code_017";//保险费用


	/*****************************************************************************/
	/*************************** Strings for order state *************************/
	/*****************************************************************************/
	public static String STR_ORDERSTATE_FABU					= "已发布";
	public static String STR_ORDERSTATE_BUTONGYI				= "待确认";
	public static String STR_ORDERSTATE_CHENGJIAO				= "已成交/待执行";
	public static String STR_ORDERSTATE_ZHIXINGZHONG			= "执行中";
	public static String STR_ORDERSTATE_KAISHIZHIXING			= "开始执行";
	public static String STR_ORDERSTATE_CHEZHUDAODA				= "车主到达";
	public static String STR_ORDERSTATE_CHENGKESHANGCHE			= "乘客上车";
	public static String STR_ORDERSTATE_ZHIXINGWANCHENG			= "执行结束/待收绿点";
	public static String STR_ORDERSTATE_YIJIESUAN				= "已收绿点/待评价";
	public static String STR_ORDERSTATE_YIPINGJIA				= "已评价";
	public static String STR_ORDERSTATE_YIGUANBI				= "已关闭";
	public static String STR_ORDERSTATE_YITUIPIAO				= "已退票";
	public static String STR_ORDERSTATE_WEIGUANBI				= "未关闭";
	public static String STR_ORDERSTATE_WEIDING					= "未定";
	public static String STR_ORDERSTATE_YIYUYUE					= "已预约/待执行";
	public static String STR_ORDERSTATE_JIESHUFUWU				= "结束服务";



	/*****************************************************************************/
	/***************************** Strings for source ****************************/
	/*****************************************************************************/
	public static String SRC_MAINAPP						= "0";
	public static String SRC_CHN_MAINAPP					= "主软件";
	public static String SRC_PINCHEAPP						= "11";
	public static String SRC_CHN_PINCHEAPP					= "拼车";
	public static String SRC_OOHUNTING						= "12";//游戏端登录源
	public static String SRC_CHN_OOHUNTING					= "OO抢钱";//游戏端登录源



	/*****************************************************************************/
	/************************** Strings for car pool type ************************/
	/*****************************************************************************/
	public static String STR_ONCE							= "同城";
	public static String STR_ONOFFDUTY						= "上下班";
	public static String STR_LONGDISTANCE					= "长途";


	/*****************************************************************************/
	/*************************** Strings for order type **************************/
	/*****************************************************************************/
	public static int ORDER_ONCE							= 1;
	public static int ORDER_ONOFF							= 2;
	public static int ORDER_LONG							= 3;


	/*****************************************************************************/
	/************************** Constants for user type **************************/
	/*****************************************************************************/
	public static int USERTYPE_USER							= 1;
	public static int USERTYPE_GROUP						= 2;
	public static int USERTYPE_UNIT							= 3;
	public static int USERTYPE_OTHER						= 0;



	/*****************************************************************************/
	/************************** Constants for order type *************************/
	/*****************************************************************************/
	public static int EXEC_ORDERTYPE_ONCE					= 1;
	public static int EXEC_ORDERTYPE_ONCE_FROM_ONOFF		= 2;
	public static int EXEC_ORDERTYPE_ONOFF					= 3;
	public static int EXEC_ORDERTYPE_LONG					= 4;



	/*****************************************************************************/
	/************************** Constants for ts oper_type **************************/
	/*****************************************************************************/
	public static int TSOPER_TYPE_OUT						= 1;
	public static int TSOPER_TYPE_IN						= 2;




	/*****************************************************************************/
	/*************************** Constants for platform **************************/
	/*****************************************************************************/
	public static int PLATFORM_IOS							= 1;
	public static int PLATFORM_ANDROID						= 2;


	/*****************************************************************************/
	/************************** Strings for environment **************************/
	/*****************************************************************************/
	public static String ENVCODE_HEARTBEAT_VALIDDPERIOD		= "heartbeat_validperiod";
	public static String ENVCODE_NEARBYDRVLIMIT				= "nearby_driver_limit";
	public static String ENVCODE_RETORDER_TIME1				= "ret_order_time1";
	public static String ENVCODE_RETORDER_TIME2				= "ret_order_time2";
	public static String ENVCODE_RETORDER_TIME3				= "ret_order_time3";
	public static String ENVCODE_RETORDER_RATIO1			= "ret_order_ratio1";
	public static String ENVCODE_RETORDER_RATIO2			= "ret_order_ratio2";
	public static String ENVCODE_RETORDER_RATIO3			= "ret_order_ratio3";
	public static String ENVCODE_RETORDER_RATIO4			= "ret_order_ratio4";
	public static String ENVCODE_SHARELINKADDR				= "share_link_addr";
	public static String ENVCODE_ORDERGIVEUPLIMIT			= "order_giveup_limit";
	public static String ENVCODE_AUTOPROCLIMITDAYS			= "auto_proc_limitdays";
	public static String ENVCODE_DAILYCANCELCNT				= "dailyCancelCount";
	public static String ENVCODE_DRIVERLATE					= "driverLate";
	public static String ENVCODE_CONSIDER_CANCELCNT			= "considerDailyCancel";
	public static String ENVCODE_CONSIDER_DRIVERLATE		= "considerDriverLate";
	public static String ENVCODE_DAILYEVALCOUNT				= "dailyEvalLimit";
	public static String ENVCODE_MINUTEINTERVAL				= "minuteInterval";
	public static String ENVCODE_SMSPRICE					= "sms_price";
	public static String ENVCODE_SMSMAXLEN					= "sms_max_len";
	public static String ENVCODE_ACCEPT_INVALID_TIME_LBND	= "accept_order_invalid_lbnd";
	public static String ENVCODE_ACCEPT_INVALID_TIME_UBND	= "accept_order_invalid_ubnd";


	/*****************************************************************************/
	/******************************* Normal strings ******************************/
	/*****************************************************************************/
	public static String STR_NICKNAME_MALE					= "先生";
	public static String STR_NICKNAME_FEMALE				= "女士";
	public static String STR_CITY							= "市";
	public static String STR_ALL							= "全部";
	public static String STR_PINCHE							= "拼车";
	public static String STR_DAIJIA							= "代驾";
	public static String STR_OTHERS							= "其他";
	public static String STR_DIAN							= "点";
	public static String STR_ZUO							= "座";
	public static String STR_FEN							= "分";
	public static String STR_WU								= "无";
	public static String STR_WUXIANZHITIAOJIAN				= "无限制条件";
	public static String STR_ZHINENGSHIYONGYIZHANG			= "只能使用一张";
	public static String STR_ZHANG							= "张";
	public static String STR_MAN							= "满";
	public static String STR_SHIYONG						= "使用";
	public static String STR_NOTUSEWITHCOUPON				= "不与其他点券共同使用";
	public static String STR_DINGDAN						= "订单";
	public static String STR_DINGDANXIAOXI					= "订单消息";
	public static String STR_DIANJISHIYONG					= "(点击使用)";
	public static String STR_PINGTAIXINXIFEI				= "平台信息费";
	public static String STR_PINGTAI						= "平台";
	public static String STR_ZHONGTUDIAN					= "中途点";
	public static String STR_CHUFA							= "出发";
	public static String STR_SHENGYU						= "剩余";
	public static String STR_JINYU							= "仅余";
	public static String STR_CI								= "次";
	public static String STR_JIALING						= "驾龄";
	public static String STR_NIAN							= "年";
	public static String STR_YUE							= "月";
	public static String STR_RI								= "日";
	public static String STR_SHI							= "时";
	public static String STR_HAOPING						= "好评";
	public static String STR_ZHONGPING						= "中评";
	public static String STR_CHAPING						= "差评";
	public static String STR_SHENFENYIRENZHENG				= "身份已认证";
	public static String STR_SHENFENWEIRENZHENG				= "身份未认证";
	public static String STR_YIPINGJIA						= "已评价";
	public static String STR_WEIPINGJIA						= "未评价";
	public static String STR_WEIJIESUAN						= "未收绿点";
	public static String STR_GONGCHUCHE						= "共出车";
	public static String STR_ZONGSHOURU						= "总收入";
	public static String STR_ZONGZHICHU						= "总支出";
	public static String STR_SHOUQU							= "收取";
	public static String STR_GE								= "个";
	public static String STR_YIYANPIAOSHANGCHE				= "已验证上车";
	public static String STR_WEIYANPIAO						= "未验证";
	public static String STR_YIQIPIAO						= "已弃票";
	public static String STR_BENCIXINGCHENGWEI				= "本次行程为";
	public static String STR_GONGLI							= "公里";
	public static String STR_PINGTAIPINGJUNJIAGEWEI			= "平台平均价格为";
	public static String STR_FACHEQIAN						= "发车前";
	public static String STR_TIANNEITUIPIAO					= "天内退票";
	public static String STR_TIANYISHANGTUIPIAO				= "天以上退票";
	public static String STR_BUSHOUQUTUIPIAOLVDIAN			= "不收取退票绿点";
	public static String STR_DANGQIANSHIJIANJUFACHE			= "当前时间距发车";
	public static String STR_BCTPJCSTPLD					= "本次退票将产生退票绿点";
	public static String STR_TIAN							= "天";
	public static String STR_FUWUCISHU						= "服务次数";
	public static String STR_JUNIN							= "距您";
	public static String STR_MORENHAOPING					= "默认好评";
	public static String STR_YIZHIFUTUIPIAOLVDIAN			= "已支付退票绿点";
	public static String STR_MEIYOUTUIPIAOLVDIAN			= "没有退票绿点";
	public static String STR_ORDERGIVEUP_MSG1				= "预约出发时间已到，如有乘客仍未上车，请在订单详情页面拨打电话询问，超过15分钟以上可以使用乘客弃票功能";
	public static String STR_ORDERGIVEUP_MSG2				= "乘客已就位，可以发车啦";
	public static String STR_ORDERGIVEUP_MSG3				= "所有乘客验票或弃票后才可以发车，订单详情页面可以拨打乘客电话";
	public static String STR_ORDERGIVEUP_MSG4				= "预约出发时间已超过15分钟，可以弃票";
	public static String STR_ORDERGIVEUP_MSG5				= "出发了，路上注意安全";
	public static String STR_YUYUESHIJIAN					= "预约时间";
	public static String STR_FAFANGDIANQUAN					= "发放点券";

	public static String STR_NIFABUDE   					= "你发布的";
	public static String STR_CONG	  						= "从";
	public static String STR_QIANWANG	 					= "前往";
	public static String STR_DE							 = "的";
	public static String STR_CTPCDDYGZXSJ					= "长途拼车订单已过执行时间,自动关闭,如有疑问请拨打OO客服电话 400-XXXX-XXXX";

	public static String STR_NIN							= "您";
	public static String STR_DDYCGST						= "订单已超过3天,系统已自动结算. 如有疑问请拨打OO客服电话 400-XXXX-XXXX";

	public static String STR_SXBDDZSWRJD					= "上下班订单暂时无人接单，如需发布单次拼车订单，请点击本消息";
	public static String STR_SXBDDYDZXSJ					= "上下班订单已到执行时间，请按时抵达";
}






