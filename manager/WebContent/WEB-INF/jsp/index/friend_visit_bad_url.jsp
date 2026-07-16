<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<!-- saved from url=(0073)index/activity_createFx.do?activityDto.userId=46 -->
<html><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
 <base href="<%=basePath%>">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<style>

.share{width:94%;margin:3%;}
.lkfb{width:100%;height:50px;margin:0em auto;background: url('images/lkfb.png') no-repeat;font-size: 130%;text-align:center;color: white; margin-bottom:10px}
.bx{color:green;text-align: center;color: red;color:#393939;font-size:15px;font-weight:bold;}
.bx span{color:#f0865a;}
.share_btn{width:100%;overflow:hidden;color:white;position:absolute;bottom:0;left:50%;margin-left:-50%;}
.share_btn .btn1{height:40px;width:85%;background:#3abf52;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:5px;margin:0 auto 2em;}
.share_btn .btn2 span{height:40px;width:45%;background:#53b0fe;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:5px;display:block;float:left;}
.share_btn .btn2 span:nth-child(2){float:right;background:#fd8b4d;}
.share_btn a{color:white;}
.footer{width:100%;height:80px;border:1px solid #ccc;box-sizing:border-box;display:-webkit-box;}
</style>
</head>
<body>
<div class="box">
    	<div class="share"><img width="100%" src="images/four.jpg"></div>
	   <div style="color:#b0b1b0;font-size:2em;text-align: center;font-weight:bold;margin-top:2em">
	  			活动已过期了！
	  </div>
		 
	  <div class="share_btn">
      	<div class="btn1">
        		<a href="index/activity_showFx.do?activityDto.faqiId=${activityDto.faqiId}">
			立刻发布我的宝箱</a>
        </div>

      <div class="footer">appp</div>
    </div>
    
</div>    
</body></html>
