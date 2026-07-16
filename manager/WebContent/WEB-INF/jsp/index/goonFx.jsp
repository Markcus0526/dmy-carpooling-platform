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
.box2{width:100%;text-align:center;}
.topArrow{margin:1em 2em 3em 0;}
.transform{-webkit-transform:rotate(-10deg);width:90%;margin:0 auto;font-size:1.5em;line-height:2em;clear:both;text-align:left;margin:3em auto;}
</style>
</head>
<body>
	<div class="box2">
    	<img width="15%" align="right" class="topArrow" src="<%=basePath%>images/toparrow.png">
        <div class="transform">点击右上角的按钮将你的活动页面分享给好友，请好友参加活动即可获取丰厚奖励哦!</div>
        <img width="50%" src="<%=basePath%>images/gifttupian.png" />
    </div>
</body>
</html>