package com.nmdy.operation.app.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import sun.misc.BASE64Encoder;

import com.opensymphony.xwork2.ActionSupport;
	/**
	 * 
	 * 此 Action是对二维码和安装包及尿性下载的类
	 * @author xcnana
	 *
	 */
public class DownloadAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String filename=null;//"desktop.ini";
	private String app_code;
	private String version;
	private String url;
	
	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
     public String execute(){
	    filename=ServletActionContext.getRequest().getParameter("filename");
	    app_code=ServletActionContext.getRequest().getParameter("app_code");
	    version=ServletActionContext.getRequest().getParameter("version");
	    url=ServletActionContext.getRequest().getParameter("url");
		return "success";
    	 
     }
	/*
	 * 提供文件下载流 
	 * 因为 StreamResult中提供inputName="inputStream"
	 * 所以 才写  InputStream getInputStream()
	 */
	public InputStream getInputStream() throws FileNotFoundException{
		File file = new File(url);
		return new FileInputStream(file);
	}
	/*
	 * 根据下载文件名动态获取MIME文件类型
	 */
      public String getContentType(){
	    	 return ServletActionContext.getServletContext().getMimeType(filename);
	  }
      /*
       * 下载附件名${filename} 
       * 此filename是从前台传入的filename
       * 附件名乱码问题(IE和其他浏览器使用的:URL编码  ;火狐浏览器使用的: BASE64 编码)
       */
	  public String getFilename() throws IOException{
	  		String agent = ServletActionContext.getRequest().getHeader("user-agent");
	  		return encodeDownloadFilename(filename,agent);
	  }
	  /**
	   * 下载文件时,针对不同浏览器,进行附件名的编码
	   * @param filename  下载文件名
	   * @param agent      客户端浏览器
	   * @return		   编码后的下载附件名
	   * @throws IOException
	   */
	  private String encodeDownloadFilename(String filename, String agent) throws IOException {
	      if(agent.contains("Firefox")){
	    	  filename="=?UTF-8?B?"+new BASE64Encoder().encode(filename.getBytes("utf-8"))+"?=";
	      }else{
	    	  filename=URLEncoder.encode(filename, "utf-8");
	      }
	      return filename;
	  }
   public void setFilename(String filename) throws IOException {
		this.filename =new String(filename.getBytes("IOS-8859-1"),"utf-8");
	}
	public String getApp_code() {
		return app_code;
	}
	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
