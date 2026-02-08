package com.pinche.filter;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.pinche.common.VCodeBuilder;

public class ContentFilter extends StrutsPrepareAndExecuteFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        String root = request.getContextPath();
		String requestUrl = request.getRequestURI().split(root)[1];

		if (requestUrl != null && requestUrl.equals("/getCaptcha")) {
	        int width = Integer.valueOf(request.getParameter("width"));
	        int height = Integer.valueOf(request.getParameter("height"));
	        String secureCode = request.getSession().getAttribute("vcode").toString();
        	BufferedImage vcode = VCodeBuilder.genCaptcha(width, height, secureCode);
        	ImageIO.write(vcode, "JPEG", response.getOutputStream()); 
		} else {
//			request.setCharacterEncoding("UTF-8");
			super.doFilter(req, res, chain);
		}
	}
}
