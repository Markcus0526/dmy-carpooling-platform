package com.damytech.BaiduQianbao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.text.TextUtils;
import com.damytech.BaiduQianbao.codec.MD5;

/**
 * 对签名进行验签
 * 
 */
public class ResultChecker {
    public static final int RESULT_INVALID_PARAM = 0;
    public static final int RESULT_CHECK_SIGN_FAILED = 1;
    public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

    /**
     * 对签名进行验签
     * 
     * @return
     */
    public static int checkSign(String result) {
        int retVal = RESULT_CHECK_SIGN_SUCCEED;
        if(TextUtils.isEmpty(result)){
            return RESULT_INVALID_PARAM;
        }
        try {
            JSONObject object = converStringToJSON(result, ";");
            String notify = object.getString("notify");
            notify = notify.substring(1, notify.length() - 1);
            
            JSONObject objResult =converStringToJSON(notify, "&");
         // 获取签名
            String signContent = objResult.getString("sign");
            objResult.remove("sign");
         // 获取签名方式
            String signType = objResult.getString("sign_method");
            // 进行验签 返回验签结果
            if (signType.equalsIgnoreCase("1")) {
                Iterator  iterator =objResult.keys();
                List<String> list= new ArrayList();
                String key="";
                while(iterator.hasNext()){
                    key = String.valueOf(iterator.next());
                    list.add(key+"="+objResult.getString(key));
                }
                Collections.sort(list, new ParamComparator());
                StringBuffer sb = new StringBuffer();
                for (String s : list) {
                    sb.append(s);
                    sb.append("&");
                }
                sb.append("key=");
                sb.append(PartnerConfig.MD5_PRIVATE);
                String sign=MD5.toMD5(sb.toString());
                if(!sign.equalsIgnoreCase(signContent)){
                    retVal = RESULT_INVALID_PARAM;
                }
            }
           
        } catch (Exception e) {
            retVal = RESULT_INVALID_PARAM;
            e.printStackTrace();
        }
        return retVal;
    }
    /**
     * 字符串转json对象
     * @param str
     * @param split
     * @return
     */
    public static JSONObject converStringToJSON(String str, String split) {
        JSONObject json = new JSONObject();
        try {
            String[] arrStr = str.split(split);
            for (int i = 0; i < arrStr.length; i++) {
                String[] arrKeyValue = arrStr[i].split("=");
                json.put(arrKeyValue[0],
                        arrStr[i].substring(arrKeyValue[0].length() + 1));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
    private  static class ParamComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }
}