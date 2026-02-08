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
<title>注册成功</title>
<script type="text/javascript"  src="${pageContext.request.contextPath}/js/jquery-1.4.2.min.js"></script>
<link id="skin" rel="stylesheet" href="${pageContext.request.contextPath}/Pink/jbox.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jBox.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<style>
</style>
</head>
<body>
	<a onclick="down();"><img src="<%=basePath%>images/registerSuccess.jpg" style="max-width:100%;"/></a>
</body>
</html>

<script>
function down(){
	  jBox.tip("Loading...", 'loading');
	  location='http://124.207.135.69:8080/applist/PincheApp.apk';
}
</script>