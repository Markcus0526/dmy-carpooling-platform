<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>site</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=basePath%>css/zTreeStyle/zTreeStyle.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
</head>
<body>

	<form action="<%=basePath%>authority/manager/updateRoles.do"
		method="post">
		<input type="hidden" name="id" value="${id}" />
		<p>
			<label class="vtwo">请选择角色</label>&nbsp;
			<c:forEach items="${list}" var="juese">
				<input type="checkbox" class="dcfour" name="roleIds"
					value="${juese.id}" <c:if test="${juese.checked}">checked</c:if>>&nbsp;${juese.name}&nbsp;&nbsp;</input>
			</c:forEach>
		</p>
		<input type="submit" value="确定" />
	</form>
</body>
</html>
