package com.nmdy.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.nmdy.filter.pojo.Function;
import com.nmdy.filter.pojo.UserInfo;
import com.nmdy.filter.service.IFilterService;
import com.nmdy.ret.RetJson;

/**
 * ���ڼ���û��Ƿ��½�Ĺ����������δ��¼�����ض���ָ�ĵ�¼ҳ��
 * <p>
 * ���ò���
 * <p>
 * checkSessionKey ������� Session �б���Ĺؼ���<br/>
 * redirectURL ����û�δ��¼�����ض���ָ����ҳ�棬URL������ ContextPath<br/>
 * notCheckURLList ��������URL�б?�Էֺŷֿ������� URL �в����� ContextPath<br/>
 */
public class AuthFilter implements Filter {
	protected FilterConfig filterConfig = null;
	private String redirectURL = null;	
	private String sessionKey = null;

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		
		if(true)filterChain.doFilter(servletRequest, servletResponse);
		if(true)return;
		
		
		//��ȡspring�����bean:filterService
		ServletContext sc=servletRequest.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		IFilterService fi = (IFilterService) ac.getBean("filterService");
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		//System.out.println("requestURI:"+request.getRequestURI());
		//��������û�û�е�¼����������뵽ϵͳ
		if ( session.getAttribute(sessionKey) == null) {//���û�û�е�¼
			if(request.getRequestURI().equals("/bk/login.do")){//����ǵ�¼�������������
				
				
				HashMap<String,Object> hm=new HashMap<String,Object>();
				
				int num=fi.isHasAccessToTheURI(hm);//�ж��û��Ƿ��з��ʸ���Դ��Ȩ�ޣ�num=0û�У�num>0��
				System.out.println("num:"+num);
				
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}else{//����ǵ�¼���ض��򵽵�¼ҳ��
				response.sendRedirect(request.getContextPath() + redirectURL);
			}
			
		}
		
		UserInfo user=(UserInfo)session.getAttribute(sessionKey);
		HashMap<String,Object> hm=new HashMap<String,Object>();
		hm.put("userId", user!=null?user.getUserId():null);
		hm.put("requestURI", request.getRequestURI());
		int num=fi.isHasAccessToTheURI(hm);//�ж��û��Ƿ��з��ʸ���Դ��Ȩ�ޣ�num=0û�У�num>0��

		if(num>0){//�Ը���Դ�з���Ȩ�ޣ����ж϶Ը���Դ������Դ��û�з���Ȩ��
			List<Function> list=fi.getSubFunctions(hm);
			RetJson ret=new RetJson();
			ret.setSubFunctions(list);
			request.setAttribute("ret", ret);
			filterChain.doFilter(servletRequest, servletResponse);
		}else{//�Ը���Դû�з���Ȩ��
			response.sendRedirect(request.getContextPath() + "/noPermission.jsp");
		}
		
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		redirectURL = filterConfig.getInitParameter("redirectURL");
		sessionKey = filterConfig.getInitParameter("sessionKey");
	}

	public void destroy() {
	
	}
}
