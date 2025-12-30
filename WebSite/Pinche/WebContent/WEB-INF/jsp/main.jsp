<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/icon.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>  
</head>

<body onload="loaddone();">
	<h1>首页</h1>
</body>
</html>
<script>
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
}
</script>
