<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<% 
    response.setHeader("Content-Disposition", "attachment; filename=report.xls"); 
    response.setHeader("Content-Description", "JSP Generated Data"); 
%>
<html>
<head>
<title>后台用户管理</title>
</head>
<body>
	<table class="bbsList2" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<th>操作</th>
			<th>ID</th>
			<th>用户名</th>
			<th>姓名</th>
			<th>所属公司</th>
			<th>手机号码</th>
		</tr>
		<s:iterator value="userInfoList">
			<tr>
				<td>查看 修改| 删除| 重置密码| 分配角色</td>
				<td><s:property value="userCode"></s:property></td>
				<td><s:property value="username"></s:property></td>
				<td><s:property value="sex"></s:property></td>
				<td><s:property value="unit"></s:property></td>
				<td><s:property value="phoneNum1"></s:property></td>
			</tr>
		</s:iterator>
	</table>
</body>
</html>
