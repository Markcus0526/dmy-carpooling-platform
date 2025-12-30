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
.login{width:100%;background:url(images/getgiftbj.png) 0 bottom;background-repeat:no-repeat;background-size:100% 55%;position:absolute;bottom:0;left:0;padding-bottom:3em;text-align:center;}
.login img{display:inline-block;}
.app{padding-bottom:1.5em;width:55%;margin:0 auto;}
.prize{width:50%;margin:2em auto 0;}
.prize img,.prize span{display:block;float:left;}
.prize span{width:85%;text-align:center;color:#393939;font-size:15px;}
.prize span i{color:#c01717;font:italic bold 25px/1.5em "'Microsoft YaHei','微软雅黑'";}
</style>
</head>
<body>
<div class="share"><img width="100%" src="images/two.jpg"></div>
    <div class="prize">
    	<img width="15%" src="images/smile.png"><span>恭喜成功抢到奖品:<i>${activityDto.giftName}:${activityDto.tip_name}</i></span>
   	</div>
    	 <div class="login" style="color:red;font-size:200%;text-align: center;">
	    	 <div class="app">
	   			<img width="100%" src="images/getGift.png" />
	   		</div>
    	 	<img width="35%" src="images/downloadbtn_03.png" />
    	</div>
    	
</body>
</html>