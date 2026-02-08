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
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<style>
.login{width:80%;margin:0 auto;text-align:center;}
.logo{margin:4em auto; }
.loginBox{border:1px solid #c6c6c6;-ms-border-radius:5px;-webkitborder-radius:5px;-moz-border-radius:5px;-o-border-radius:5px;border-radius:5px;color:#454545;font:16px/4em "'Microsoft YaHei','微软雅黑'";margin-bottom:1.5em;}
.loginBox input{height:4em;width:80%;padding-left:20%;color:#454545;}
.loginBox input:nth-child(1){border-bottom:1px solid #c6c6c6;background:url(<%=basePath%>images/icon1_07.jpg) no-repeat 10% center;background-size:11px 15px;}
.loginBox input:nth-child(2){background:url(<%=basePath%>images/icon2_10.jpg) no-repeat 10% center;background-size:12px 16px;}
.btnBox{width:100%;}
.btnBox input{width:100%;text-align:center;height:2.5em;color:#fff;font:bold 16px/2.5em "'Microsoft YaHei','微软雅黑'";-ms-border-radius:5px;-webkitborder-radius:5px;-moz-border-radius:5px;-o-border-radius:5px;border-radius:5px;}
.btnBox input:nth-child(1){background:#3abf52;margin-bottom:0.5em;}
.btnBox input:nth-child(2){background:#4ea7f2;}
</style>
<%
String msg = (String)request.getAttribute("activityDto.msg");
if(msg!=null){
%>
<script type="text/javascript" language="javascript">
	$("#divMsg").show();
</script>
<%
  }
%>

</head>
<body>
<div style="direction: none;" id="divMsg">
	<h1>您的用户名或密码错误！</h1>
</div>

<form action="<%=basePath%>index/activity_login.do" method="post">
  <div class="login">
  	<img class="logo" width="50%" src="<%=basePath%>images/logo_03.png" />
    <div class="loginBox">
    	<input type="text" placeholder="用户名" name="activityDto.usercode"/>
        <input type="text" placeholder="密码" name="activityDto.password"/>
    </div>
    <div class="btnBox">
    	<input type="submit" value="登录" />
        <input type="button" value="注册" onclick="javascript:location='<%=basePath%>index/activity_register.do'"/>
    </div>
  </div>
</form>    	 	
</body>
</html>