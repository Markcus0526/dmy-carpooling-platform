<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<style>
.box dl{width:100%;overflow:hidden;text-align:center;}
.box dl dd{width:100%;}
.box dl dd:nth-child(1){margin-top:1em;}
.box dl dd:nth-child(1) img{width:20%;height:20%;}
.box dl dd:nth-child(2){width:70%;height:40px;border-radius:20px;background:#82c437;margin:0 auto;text-align:center;font:18px/40px "寰蒋闆呴粦","Microsoft YaHei" ;color:white;margin:2em auto;}
.share{width:90%;margin:3em auto;text-align:center}
</style>
</head>
<body>
<div class="box">
	<dl>
    	<dd><img src="<%=basePath%>images/ewm_03.png" /></dd>
        <dd>我的邀请码：${activityDto.invitecode_self}</dd>
    </dl>
    <div class="share">
    	<a href="<%=basePath%>index/activity_createFx.do?activityDto.userId=${activityDto.userId}">
    		<img width="100%" src="<%=basePath%>images/share_banner_03.jpg">
    	</a>
    </div>
    
</div>
</body>
</html>