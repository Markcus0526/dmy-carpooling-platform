package com.pinche.common.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SecurityInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation ai) throws Exception {
		Object action = ai.getAction();
		
		if (action instanceof CheckAuthWare) {
            boolean auth = ((CheckAuthWare)action).isLogged();
            if (auth)
            	ai.invoke();
        }
		
		return "logout";
	}
}
