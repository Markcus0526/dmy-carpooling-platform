/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmdy.common;

public class Errors {
    public static final String PINCHE_ERR_CODE_NO_METHOD =  "10704";
    public static final String PINCHE_ERR_CODE_DENY_ACCESS =  "10705";
    public static final int ERR_UNKNOWN = 99;
    public static final int ERR_OK = 0;
    public static final int ERR_SQL_ERROR = 98;
    public static final int ERR_INPUT_NULL = 97;
    public static final int ERR_INVALID_USERCODE = 96;
    public static final int ERR_INVALID_PASSWORD = 95;
    public static final int ERR_ALREADY_USING = 94;
    public static final int ERR_ALREADY_NAME = 93;
    
    
    public static String getErrorMessage(int errCode) {
    	String errMsg = null;
    	switch (errCode) {
    	case ERR_OK:
    		errMsg = "No error";
    		break;
    		
    	case ERR_SQL_ERROR:
    		errMsg = "SQL error";
    		break;
    		
    	case ERR_INPUT_NULL:
    		errMsg = "Input value is null";
    		break;
    		
    	case ERR_INVALID_USERCODE:
    		errMsg = "用户名不能重复。";
    		break;
    		
    	case ERR_INVALID_PASSWORD:
    		errMsg = "您输入的密码不正确。";
    		break;
    		
    	case ERR_ALREADY_USING:
    		errMsg = "因为这项目现在利用中，无法删除。";
    		break;
    	
    	case ERR_ALREADY_NAME:
    		errMsg = "用户名已存在，不能重复！";
    		break;
    	
    	case ERR_UNKNOWN:
    		break;
    	}
    	
    	return errMsg;
    }
}
