package com.pinche.common;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String default_pass="haotang365";
	public static final String LOGIN_ERROR = "用户名或密码错误！";
	public static final String UPDATE_SUCCESS="修改成功!";
	public static final String ADD_EMPTY_ERROR="没有选择点卷所对应的张数！";
	public static final String ADD_ERROR="点卷名称与对应的张数不一致！";
	public static final String ADD_RULE_ERROR="请先选择点卷！";
	public static final String ADD_RULE_GIFT_ERROR="规则的数目不能大于点卷的数目！";
	public static final String ADD_ACTIVITY_ERROR="规则与点卷的数目不一致或为空！";
	public static final String ADD_ACTIVITY_EMPTY_ERROR="请确保字段都不为空！";
	public static final String UPDATE_ACTIVITY_ERROR="规则或点卷不能为空!";
	public static final String UPDATE_ACTIVITY1_ERROR="规则或点卷数目不一致!";
	public static final int SUPER_MANAGER = 0x7FFFFFFF;
	public static final int COMMON_MANAGER = 0;
	
	public static final int STATUS_AVAILABLE = 0;
	public static final int STATUS_DELETED = 1;

	public static final int ROLE_TYPE_OPERATION = 0;
	public static final int ROLE_TYPE_DATA = 1;

	public static final int OPERATION_TRUE = 1;
	public static final int OPERATION_FALSE = 0;

	public static final int SEARCH_BY_USERCODE = 1;
	public static final int SEARCH_BY_USERNAME = 2;
	public static final int SEARCH_BY_PHONENUM = 3;
	public static final int SEARCH_BY_UNIT = 4;
	public static final int SEARCH_BY_ID = 5;

	public static final int MANAGER_SEARCH_PAGE = 1;
	public static final int ROLE_SEARCH_PAGE = 2;

	public static final int MODEL_TYPE_ADMIN = 1;
	public static final String URL_BACKEND_FIRST_PAGE = "/WEB-INF/jsp/index.jsp";
	public static final String URL_MANAGER_INDEX = "/WEB-INF/jsp/authority/manager/index.jsp";
	public static final String URL_ADD_MANAGER = "/WEB-INF/jsp/authority/manager/add.jsp";
	public static final String URL_EDIT_MANAGER = "/WEB-INF/jsp/authority/manager/edit.jsp";
	public static final String URL_VIEW_MANAGER = "/WEB-INF/jsp/authority/manager/view.jsp";
	public static final String URL_CHPWD_MANAGER = "/WEB-INF/jsp/authority/manager/password.jsp";
	public static final String URL_ASSIGN_ROLE = "/WEB-INF/jsp/authority/manager/setrole.jsp";
	
	// order\appoint page url
	public static final String URL_ORDER_APPOINT_FIRST_PATE = "/WEB-INF/jsp/order/appoint/index.jsp";
	public static final String URL_ORDER_APPOINTDETAIL_FIRST_PATE = "/WEB-INF/jsp/order/appointdetail/index.jsp";

	public static final String URL_ROLE_INDEX = "/WEB-INF/jsp/authority/role/index.jsp";
	public static final String URL_OP_ROLE = "/WEB-INF/jsp/authority/role/setOpRole.jsp";
	public static final String URL_DATA_ROLE = "/WEB-INF/jsp/authority/role/setDataRole.jsp";
	public static final String URL_ADD_ROLE = "/WEB-INF/jsp/authority/role/add.jsp";
	public static final String URL_EDIT_ROLE = "/WEB-INF/jsp/authority/role/edit.jsp";
	public static final String URL_VIEW_ROLE = "/WEB-INF/jsp/authority/role/view.jsp";

	// order action URL
	public static final String URL_RECOURSE_INDEX = "/WEB-INF/jsp/order/recourse/index.jsp";
	public static final String URL_ADD_RECOURSE = "/WEB-INF/jsp/order/recourse/add.jsp";
	public static final String URL_EDIT_RECOURSE = "/WEB-INF/jsp/order/recourse/edit.jsp";
	public static final String URL_VIEW_RECOURSE = "/WEB-INF/jsp/order/recourse/view.jsp";
	public static Constants instance = null;

	// table names
	public static final String TB_NAME_ADMIN_USER = "administrator";

	// length
	public static final int LENGTH_PLAN_CODE = 16;
	
	// system values
	public static final int SMS_PROGRESS_STATUS = 0;
	public static final int SMS_STOP_STATUS = 1;
	public static final int SMS_COMPLETE_STATUS = 2;
	public static final int SMS_SINGLE_MODE = 1;
	public static final int SMS_PERIOD_MODE = 2;
	
	public static final String DEFALUT_TIME = "1970-01-01 00:00:00";
	
	// for environment code
	public static final String ENV_DAILY_CANCELCOUNT = "dailyCancelCount";
	public static final String ENV_DRIVER_LATE = "driverLate";
	public static final String ENV_SMS_PRICE = "sms_price";
	public static final String ENV_SMS_MAX_LEN = "sms_max_len";
	
	private Constants() {

	}

	public static Map<Integer, String> getSearchOptions(int page) {

		Map<Integer, String> options = new HashMap<Integer, String>();

		switch (page) {

		case MANAGER_SEARCH_PAGE:
			options.put(Constants.SEARCH_BY_USERCODE, "按用户名查询");
			options.put(Constants.SEARCH_BY_USERNAME, "按姓名查询");
			options.put(Constants.SEARCH_BY_PHONENUM, "按手机号查询");
			options.put(Constants.SEARCH_BY_UNIT, "按公司查询");
			options.put(Constants.SEARCH_BY_ID, "按ID查询");
			break;

		default:
			return null;
		}

		return options;
	}

	public static String getOperType(int oper_type) {
		Map<Integer, String> options = new HashMap<Integer, String>();
		options.put(1, "体现");
		options.put(2, "充值");
		options.put(3, "扣点");
		return options.get(oper_type);
	}
	
	public static String getAccountType(int account_type) {
		Map<Integer, String> options = new HashMap<Integer, String>();
		options.put(1, "个人客户");
		options.put(2, "集团客户");
		options.put(3, "合作单位");
		return options.get(account_type);
	}
	
	public static String getReqStatus(int status) {
		Map<Integer, String> options = new HashMap<Integer, String>();
		options.put(1, "待审核");
		options.put(2, "已通过");
		options.put(3, "已驳回");
		options.put(4, "已关闭");
		options.put(5, "处理中");
		options.put(6, "处理");
		return options.get(status);
	}
	
	// session variable
	public static final String MANUAL_DEPLOY_COUPONCODE = "MANUAL_DEPLOY_COUPONCODE";
	public static final String MANUAL_DEPLOY_COUPONID = "MANUAL_DEPLOY_COUPONID";
	
	// sys_coupon table constants
	public class SyscouponTable {
		
		public class ReleaseChannel {
			public static final int APP = 1;
			public static final int APP_SPREAD_UNIT = 2;
		}
		
		public class GoodsOrCash {
			public static final int CASH = 1;
			public static final int GOODS = 2;
		}
		
		public class Enabled {
			public static final int DISABLED = 0;
			public static final int ENABLED = 1;
		}
		
		public class CouponType {
			public static final int NONE = 0;
			public static final int OVER = 1;
			public static final int NO_WITH_OTHER = 2;
			public static final int ONLY_ONE = 3;
		}
		
		public class ValidPeriodUnit {
			public static final int DAY = 1;
			public static final int WEEK = 2;
			public static final int MONTH = 3;
			public static final int YEAR = 4;
		}
	}
	
	
	// coupon_send_log table constants
	public class CouponSendLogTable {
		public class SendType {
			public static final int REGISTER_AUTOSEND = 1;
			public static final int ORDER_AUTOSEND = 2;
			public static final int PERSON_VERIFIED_AUTOSEND = 3;
			public static final int LONG_REGISTER_AUTOSEND = 4;
			public static final int MANUAL_SEND = 5;
		}
	}
	
	// single_coupon table constants
	public class SingleCouponTable {
		public class Enabled {
			public static final int DISABLED = 0;
			public static final int ENABLED = 1;
		}
		public class IsGeneratedByActive {
			public static final int NO = 0;
			public static final int YES = 1;
		}
	}
	
	// activities table constants
	public class ActivitiesTable {
		public class Enabled {
			public static final int DISABLED = 0;
			public static final int ENABLED = 1;
		}
		public class DELETED {
			public static final int NO = 0;
			public static final int YES = 1;
		}
	}
}
