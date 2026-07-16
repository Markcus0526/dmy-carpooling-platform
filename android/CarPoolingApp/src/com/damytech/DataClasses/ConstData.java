package com.damytech.DataClasses;

/**
 * Created by RiGSNote on 14-9-16.
 */
public class ConstData
{
    public static final String PUSH_KEY = "RzKyPYoXGQwEq9BSWv3CaHPf";

    /***************************** Push Type Codes *******************************/
    public static final int PUSH_PASS_ONOFF_CONFIRM = 1;
    public static final int PUSH_PASS_ONOFF_TO_ONCE = 2;
    public static final int PUSH_PASS_LONG_CONFIRM = 3;
    public static final int PUSH_DRV_ONOFF_CONFIRM = 4;
    public static final int PUSH_DRV_ORDER_DETAIL = 5;
    public static final int PUSH_EXTRA = 6;
    public static final int PUSH_PASS_ONCE_CONFRIM = 7;

    /***************************** CommManager Error Codes *******************************/
	public static final int ERR_CODE_NONE = 0;
	public static final int ERR_CODE_NORMAL = -1;
	public static final int ERR_CODE_DEVNOTMATCH = -2;
	public static final int ERR_CODE_PARAMS = -3;
	public static final int ERR_CODE_EXCEPTION = -4;
	public static final int ERR_CODE_BALNOTENOUGH = -5;
	public static final int ERR_CODE_ALREADYACCEPTED = -6;
	public static final int ERR_CODE_CANCELLED = -7;
	public static final int ERR_CODE_OLDPWDWRONG = -8;


    /*********************************** Order Type **************************************/
    public static final int ORDER_TYPE_ONCE = 1;
    public static final int ORDER_TYPE_ONOFF = 2;
    public static final int ORDER_TYPE_LONG = 3;


	/******************************* Evaluate level ***********************************/
    public static final int EVALUATE_NONE = 0;
	public static final int EVALUATE_GOOD = 1;
	public static final int EVALUATE_NORMAL = 2;
	public static final int EVALUATE_BAD = 3;



	/*********************************** Car style **************************************/
	public static final int CARSTYLE_JINGJIXING = 1;
	public static final int CARSTYLE_SHUSHIXING = 2;
	public static final int CARSTYLE_HAOHUAXING = 3;
	public static final int CARSTYLE_SHANGWUXING = 4;



	/*********************************** Coupon condition **************************************/
	public static final int COUPON_COND_NO = 1;
	public static final int COUPON_COND_ORDERPRICEX = 2;
	public static final int COUPON_COND_ONLY = 3;
	public static final int COUPON_COND_ONLY_FORORDER = 4;



	/*********************************** Gender **************************************/
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;


	/*********************************** Order State *************************************/
    public static final int ORDER_STATE_DRV_ACCEPTED = 0;
    public static final int ORDER_STATE_PUBLISHED = 1;
    public static final int ORDER_STATE_GRABBED = 2;
    public static final int ORDER_STATE_STARTED = 3;
    public static final int ORDER_STATE_DRV_ARRIVED = 4;
    public static final int ORDER_STATE_PASS_GETON = 5;
    public static final int ORDER_STATE_FINISHED = 6;
    public static final int ORDER_STATE_PAYED = 7;
    public static final int ORDER_STATE_EVALUATED = 8;
    public static final int ORDER_STATE_CLOSED = 9;
	public static final int ORDER_STATE_CANCELLED = 10;                     // Valid for long distance order. 'tuipiao' status

    /********************************* Passenger State ********************************/
    public static final int PASS_STATE_UPLOAD = 1;
    public static final int PASS_STATE_WAIT = 2;
    public static final int PASS_STATE_GIVEUP = 3;
    public static final int PASS_STATE_NOT_PAY = 4;
    public static final int PASS_STATE_PAYED = 5;
    public static final int PASS_STATE_EVALUATED = 6;

}
