package com.nmdy.sysManage.role.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsStatics;

import com.nmdy.entity.Resource;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthorityInterceptor extends AbstractInterceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//拦截Action处理的拦截方法 
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		   // 取得请求相关的ActionContext实例  
        ActionContext ctx=invocation.getInvocationContext();  
        Map session=ctx.getSession();  
        //取出名为user的session属性  
        String userName=(String)session.get("userName");  
        //如果没有登陆，或者登陆所有的用户名不是aumy，都返回重新登陆  
        if(userName==null){  
        	ctx.put("tip","您还没有登录，请登陆系统");  
        	 return Action.LOGIN;
        }  
        HttpServletRequest request = (HttpServletRequest) ctx.get(StrutsStatics.HTTP_REQUEST);
        String url = request.getRequestURI().replaceFirst("/bk","");
        System.out.println("****"+url);
        List<Resource> listAll = new ArrayList<Resource>();
        listAll = (List<Resource>)session.get("allResource");
        if(listAll.isEmpty())  return "authorityDeny";
        boolean result = false;
        for(Resource r:listAll){
        	if(StringUtils.isEmpty(r.getUrl())) continue;
        	if(url.contains(r.getUrl())){
        		result = r.getChecked();
        		break;
        	}
        }
        //没有权限
        if(result==false) return "authorityDeny";
        //没有登陆，将服务器提示设置成一个HttpServletRequest属性  
        return invocation.invoke();       
	}
}
