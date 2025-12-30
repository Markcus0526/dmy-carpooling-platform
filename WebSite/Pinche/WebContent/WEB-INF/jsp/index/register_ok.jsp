<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 <!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<base href="<%=basePath%>">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<style>
.share{width:94%;margin:3%;}
.richBox{text-align:center;font:italic bold 20px/1em "'Microsoft YaHei','微软雅黑'";color:#393939;margin-top:2em;}
.richBox i{font:italic bold 20px/1em "'Microsoft YaHei','微软雅黑'";color:#c01717;}
.richBox i,.richBox img{vertical-align:middle; }
.richBox span{color:#c01717;}
.find{color:#268800;font: bold 16px/1.6em "'Microsoft YaHei','微软雅黑'";position:relative;width:80%;margin:2em auto 1em;}
.find:before{display:block;content:'';border-style:dashed solid dashed dashed;border-color:transparent #2b9a00 transparent transparent;border-width:0.6em;position:absolute;left:-1.4em;top:2px;width:0;height:0;}
.find span{color:#c01717;}
.login{width:100%;background:url(images/getgiftbj.png) 0 bottom;background-repeat:no-repeat;background-size:100% 55%;position:absolute;bottom:0;left:0;padding-bottom:3em;text-align:center;padding-top:5.5em;}
.login img{display:inline-block;}
.app{padding-bottom:1.5em;}
</style>
</head>
<body>
    <div class="share"><img width="100%" src="images/four.jpg"></div>
    <div class="richBox">
    	<img width="10%" src="images/zccg.png"/><i> 注册成功！</i>
   	</div>
    	
    	<div class="login" style="color:red;font-size:200%;text-align: center;">
            <img width="35%" src="images/downloadbtn_03.png" />
        </div>
    	 	
    	 	
</body>
</html>