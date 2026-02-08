package com.pinche.operation.app.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Session;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pinche.common.UserLoginInfo;
import com.pinche.common.WebUtil;
import com.pinche.operation.app.dao.App;
import com.pinche.operation.app.dao.AppPlatform;
import com.pinche.operation.app.service.AppService;
/**
 * 运营管理 App管理的Action
 * @author xcnana
 *
 */
public class AppAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private Long id;
	private AppService appService;
	private int page;
	private int rows;
	private String version;
	private String app_code;
	private String url_scheme;
	private String app_name;
	private String pack_name;
	private File qrcode_path;
	private JSONObject resultObject=null;
	private String qrcode_pathFileName;
	private String qrcode_pathContentType;
	private File url;
	private int redio;
	private String bundle_id;
	private String urlFileName;
	private String urlContextType;
	private double size;
	private int list;
	   private static float percent = 0;//百分比  
	/*
	 * 对详细App首页面的跳转
	 */
	public String index(){
		return "success";
	}
	/*
	 * 新增App页面 的跳转
	 */
	public String list(){
		ActionContext.getContext().put("list", list);
		if(list==1){
			return "success";
		}else{
			return "update";
		}
		
	}
	/*
	 * 查看全部版本的App 数据信息
	 */
	public String findAll(){
		List<App> appInfo=null;
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		params.put("start", (page - 1) * rows);
		params.put("limit", rows);
		int total;
		if(redio==1){
			appInfo=appService.findNew(params);
			total=appService.getCountNew();
		}else{
			appInfo=appService.findAll(params);	
			total=appService.getCountAll();
		}
		
		if(total==0){
			jsonMap.put("total", 1);
			jsonMap.put("rows", 0);
		}else{
			jsonMap.put("total", total);
			jsonMap.put("rows", appInfo);	
		}

		resultObject = JSONObject.fromObject(jsonMap);
		System.out.println(resultObject);
		return "success";
	}
	
	/*
	 * 对App数据进行修改
	 */
	/*public String update(){
		System.out.println(qrcode_path+"``````"+url+"`````"+version);
		Map<String,Object> params=new HashMap<String,Object>();
		App app=appService.findById(id);
		path=ServletActionContext.getServletContext().getRealPath("/upload");
		path=path+File.separator+app.getApp_code()+File.separator+version;
	   System.out.println(path);
	   File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}else{
			return "error";
		}
		path=file.getAbsolutePath()+File.separator;
		if(QR_code!=null){
			try {
				InputStream is=new FileInputStream(QR_code);
				File destFile=new File(path,QR_codeFileName);
				 copy(is,destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
			if(pack_name!=null){
				try {
					InputStream is=new FileInputStream(pack_name);
					File destFile=new File(path,pack_nameFileName);
				     copy(is,destFile);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
	
		System.out.println(path);
		Date date=new Date();
		Timestamp upload_time = new Timestamp(date.getTime());
		params.put("version", version);
		params.put("path", path);
		params.put("QR_code",QR_codeFileName);
		params.put("pack_name", pack_nameFileName);
		params.put("upload_time", upload_time);
	    params.put("id", id);
		appService.update(params);
		return "success";
	}
	*/
	/*
	 * 对App的一条数据进行 逻辑删除,
	 */
	public String delete(){
		appService.delete(id);
		return "success";
	}

	/*
	 * 对一条App数据 进行更新 将新的版本信息上传 和 存数数据库
	 */
	public String updateApp(){
		 if (percent >= 99.9) {//这里保险起见我们设百分比》99.9就清0，避免进度条到了100%就停在那里不动的尴尬  
             percent = 0;  
          }

		 String path=ServletActionContext.getServletContext().getRealPath("/upload");
  		path=path+File.separator+app_code+File.separator+version;
		 File file=new File(path);
		if(!file.exists()){
			 file.mkdirs();
		 }
		path=file.getAbsolutePath()+File.separator;;
		System.out.println(path);
		System.out.println(file);
		if(qrcode_path!=null){
		try {
			InputStream is=new FileInputStream(qrcode_path);
			File destFile=new File(path,qrcode_pathFileName);
			 copy(is,destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		size=url.length();
		if(url!=null){
			try {
				InputStream is=new FileInputStream(url);
				File destFile=new File(path,urlFileName);
			     copy(is,destFile);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		String path1="/upload"+File.separator+app_code+File.separator+version+File.separator;
		String qrcode=path1+qrcode_pathFileName;
		String url=path1+urlFileName;
		Map<String,Object> params1=new HashMap<String,Object>();
		Map<String,Object> params2=new HashMap<String,Object>();
		Date date=new Date();
		Timestamp upload_time = new Timestamp(date.getTime());
		params1.put("app_code", app_code);
		params1.put("app_name", app_name);
		params1.put("url_scheme", url_scheme);
		params1.put("bundle_id", pack_name);
		params1.put("pack_name",pack_name);
		System.out.println(params1);
		params2.put("url", url);
		params2.put("size", size);
		params2.put("version", version);
		params2.put("qrcode_path",qrcode);
		params2.put("upload_time", upload_time);
		params2.put("app_code", app_code);
		System.out.println(params2);
		appService.updateApp(params1);
		appService.insertAppVersion(params2);
		return "success";
	}
	/*
	 * 增加新的一条App信息 对二维码和安装包进行上传
	 */
	public String addApp(){ 
		  if (percent >= 99.9) {//这里保险起见我们设百分比》99.9就清0，避免进度条到了100%就停在那里不动的尴尬  
	              percent = 0;  
	           }

	   String path=ServletActionContext.getServletContext().getRealPath("/upload");
	   path=path+File.separator+app_code+File.separator+version;
	  File file=new File(path);
	 if(!file.exists()){
		 file.mkdirs();
      }
	 path=file.getAbsolutePath()+File.separator;;
	 System.out.println(path);
	 System.out.println(file);
	if(qrcode_path!=null){
	try {
		InputStream is=new FileInputStream(qrcode_path);
		File destFile=new File(path,qrcode_pathFileName);
		 copy(is,destFile);
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
	size=url.length();
	if(url!=null){
		try {
			InputStream is=new FileInputStream(url);
			File destFile=new File(path,urlFileName);
		     copy(is,destFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	String path1="/upload"+File.separator+app_code+File.separator+version+File.separator;
	String qrcode=path1+qrcode_pathFileName;
	String url=path1+urlFileName;
		Map<String,Object> params1=new HashMap<String,Object>();
		Map<String,Object> params2=new HashMap<String,Object>();
		Date date=new Date();
		Timestamp upload_time = new Timestamp(date.getTime());
			params1.put("app_code", app_code);
			params1.put("app_name", app_name);
			params1.put("url_scheme", url_scheme);
			params1.put("bundle_id", pack_name);
			params1.put("pack_name",pack_name);
			System.out.println(params1);
			params2.put("url", url);
			params2.put("size", size);
			params2.put("version", version);
			params2.put("qrcode_path",qrcode);
			params2.put("upload_time", upload_time);
			params2.put("app_code", app_code);
			System.out.println(params2);
			appService.insertApp(params1);
			appService.insertAppVersion(params2);
		return "success";
	}
	
	/*
	 * addApp() 和 updateApp() 子方法
	 * 将上传的数据放入到指定目录中
	 */
	public void copy(InputStream is,File destFile) throws IOException {
	
		 if (percent >= 99.9) {//这里保险起见我们设百分比》99.9就清0，避免进度条到了100%就停在那里不动的尴尬  
             percent = 0;  
          }
		OutputStream os=new FileOutputStream(destFile);
		byte[] buffer=new byte[40];
		int length=0;
	    float completedSize = 0;//已经上传的大小  
	    float fileSize = 0;//文件总大小   
	    fileSize = is.available();  
		while((length=is.read(buffer))>0){
			os.write(buffer,0,length);
			completedSize += (long)length;
			percent = completedSize / fileSize * 100;//百分比计算  
		}
		is.close();
		os.close();
	}
	/*
	 * app 首页面 通过 id 和 版本号 查询 
	 * 通过ID进行查询一条记录的详细信息
	 * 以方便进行下载
	 */
	public String findById(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		map.put("version", version);
		App app=appService.findById(map);
		String qrcode_path=app.getQrcode_path();
		String url =app.getUrl();
		qrcode_path=qrcode_path.substring(qrcode_path.lastIndexOf(File.separator)+1);
		url=url.substring(url.lastIndexOf(File.separator)+1);
		System.out.println(qrcode_path+"````````"+url);
		app.setQrcode_path(qrcode_path);
		app.setUrl(url);
		resultObject = JSONObject.fromObject(app);
		System.out.println(resultObject);
		
		return "success";
		
	}
	/*
	 * 验证app_code 此数据是否已存在
	 */
	public String checkAppCode(){
		int row=appService.checkAppCode(app_code);
		AppPlatform app=appService.findByCode(app_code);
		Map<String ,Object> map=new HashMap<String,Object>();
		map.put("row", row);
		if(row==1){
			map.put("id", app.getId());
			map.put("app_name", app.getApp_name());
			map.put("pack_name", app.getPack_name());
			if( app.getBundle_id().equals("")){
				map.put("platform",0);
			}else{
				map.put("platform",1);
			}
		}else{
			
		}
		resultObject = JSONObject.fromObject(map);
		return  "success";
	}
	/*
	 * 检验上传进度条
	 */
	public String checkprogress(){
	  Map<String,Object> map=new HashMap<String,Object>();
	  map.put("percent", percent);
		System.out.println(percent);
	  resultObject = JSONObject.fromObject(map);
		return "success";
	}
	//----------getter和setter方法----------
	public AppService getAppService() {
		return appService;
	}

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public int getPage() {
		return page;
	}

	public int getList() {
		return list;
	}
	public void setList(int list) {
		this.list = list;
	}
	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public JSONObject getResultObject() {
		return resultObject;
	}

	public void setResultObject(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String getApp_code() {
		return app_code;
	}

	public void setApp_code(String app_code) {
		this.app_code = app_code;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getUrl_scheme() {
		return url_scheme;
	}
	public void setUrl_scheme(String url_scheme) {
		this.url_scheme = url_scheme;
	}
	public File getQrcode_path() {
		return qrcode_path;
	}
	public void setQrcode_path(File qrcode_path) {
		this.qrcode_path = qrcode_path;
	}
	public String getQrcode_pathFileName() {
		return qrcode_pathFileName;
	}
	public void setQrcode_pathFileName(String qrcode_pathFileName) {
		this.qrcode_pathFileName = qrcode_pathFileName;
	}
	public String getQrcode_pathContentType() {
		return qrcode_pathContentType;
	}
	public void setQrcode_pathContentType(String qrcode_pathContentType) {
		this.qrcode_pathContentType = qrcode_pathContentType;
	}
	public File getUrl() {
		return url;
	}
	public void setUrl(File url) {
		this.url = url;
	}
	public String getUrlFileName() {
		return urlFileName;
	}
	public void setUrlFileName(String urlFileName) {
		this.urlFileName = urlFileName;
	}
	public String getUrlContextType() {
		return urlContextType;
	}
	public void setUrlContextType(String urlContextType) {
		this.urlContextType = urlContextType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPack_name() {
		return pack_name;
	}
	public void setPack_name(String pack_name) {
		this.pack_name = pack_name;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public static float getPercent() {
		return percent;
	}
	public static void setPercent(float percent) {
		AppAction.percent = percent;
	}
	public int getRedio() {
		return redio;
	}
	public void setRedio(int redio) {
		this.redio = redio;
	}
	
}
