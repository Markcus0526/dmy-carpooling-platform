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
.richBox{text-align:center;font:italic bold 20px/1em "'Microsoft YaHei','微软雅黑'";color:#393939; }
.richBox i{font:italic bold 30px/1em "'Microsoft YaHei','微软雅黑'";color:#c01717;}
.richBox span{color:#c01717;}
.find{color:#268800;font: bold 16px/1.6em "'Microsoft YaHei','微软雅黑'";position:relative;width:80%;margin:1em auto;}
.find:before{display:block;content:'';border-style:dashed solid dashed dashed;border-color:transparent #2b9a00 transparent transparent;border-width:0.6em;position:absolute;left:-1.4em;top:2px;width:0;height:0;}
.find span{color:#c01717;}
.login{width:100%;background:url(images/getgiftbj.png) 0 bottom;background-repeat:no-repeat;background-size:100% 55%;padding-bottom:3em;text-align:center;}
.login img{display:inline-block;}
.app{padding-bottom:1.5em;width:55%;margin:0 auto;}
</style>
</head>
<body>
    <div class="share"><img width="100%" src="images/four.jpg"></div>
    <div class="richBox">
    	<img width="30%" src="images/baox.png"/><br/>
    	你来晚了，宝箱被<i>抢光</i><span>啦！</span>
   	</div>
    	
    	 <div class="find">
         	登陆APP或关注QQ快拼微信公众号<br><span>发布属于你自己的抢宝活动！</span> 
    	</div>
    	<div class="login" style="color:red;font-size:200%;text-align: center;">
             <div class="app">
                <img width="100%" src="images/getGift.png" />
            </div>
            <img width="35%" src="images/downloadbtn_03.png" />
        </div>
</body>
</html>