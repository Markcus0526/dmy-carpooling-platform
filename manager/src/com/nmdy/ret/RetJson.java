package com.nmdy.ret;

import java.util.List;

import com.nmdy.filter.pojo.Function;

/*
 * 返回到页面的Json数据，每个请求均需要返回此对象
 */

public class RetJson {
	private List<Function> subFunctions=null;//当前请求页面中的链接或者按钮能否访问，

	public List<Function> getSubFunctions() {
		return subFunctions;
	}

	public void setSubFunctions(List<Function> subFunctions) {
		this.subFunctions = subFunctions;
	}
	

}
