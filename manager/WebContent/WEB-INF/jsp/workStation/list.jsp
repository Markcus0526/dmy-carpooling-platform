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
<%
	String msg = (String)request.getAttribute("msg");
	if(msg!=null){
%>
	  <script>
	  	alert('<%=msg%>');
	  </script>
<%
	}
%>
<style type="text/css">
.one, .two{
	float:left;
	font-size:14px;
	color:#f00;
	width:430px;
	height:235px;
}
.one{
	border:1px solid #000;
}
.two{
	border:1px solid #00f;margin-left: 100px;
}
</style>
</head>

<body onload="loaddone();">


<br/>
		<h2 style="margin-left:10px;margin-bottom:0px">工作台设置</h2>
<hr/>
    <div class="one" id="one">	
    	<table id="dg" style="padding: 1px; boder: black 0px solid;" data-options="fit:true,border:true"></table>
    </div>
	<div class="two" id="two">
		<table id="dg2" style="padding: 1px; boder: black 0px solid;" data-options="fit:true,border:true"></table>
	</div>
	 	<img src="<%=basePath%>upload/book.jpg"/>
</body>
</html>
<script>
var page_path = "${pageContext.request.contextPath}/";
var dataGrid;
var dataLoader = {};
var dataGrid2;
var dataLoader2 = {};



dataLoader.load = function() {
	dataGrid = $('#dg')
			.datagrid(
					{
						url : page_path + 'workstation/selectAll.do',
						loadMsg : '正在加载，请稍后..',
						pagination : true,
						fitColumns : true,
						pageSize : 10,
						pageList : [ 10, 20, 30, 40, 50 ],
						idField : 'id',
						columns : [ [
								 {
									field : 'userId',
									title : '用户ID',
									width : 150,
									align : 'center'
								}, {
									field : 'username',
									title : '用户姓名',
									width : 150,
									align : 'center'
								}, {
									field : 'sum',
									title : '提现数额',
									width : 100,
									align : 'center'
								}, {
									field : 'reqDate',
									title : '请求时间',
									width : 150,
									align : 'center'
								}
								] ] 
					});
};


dataLoader.load();
dataLoader2.load = function() {
	dataGrid2 = $('#dg2')
			.datagrid(
					{
						url : page_path + 'workstation/selectAllUser.do',
						loadMsg : '正在加载，请稍后..',
						pagination : true,
						fitColumns : true,
						pageSize : 10,
						pageList : [ 10, 20, 30, 40, 50 ],
						idField : 'id',
						columns : [ [
								 {
									field : 'id',
									title : '用户ID',
									width : 150,
									align : 'center'
								}, {
									field : 'username',
									title : '用户姓名',
									width : 150,
									align : 'center'
								}, {
									field : 'person_verified',
									title : '待验证内容',
									width : 100,
									align : 'center',
									formatter: function(value,row,index){
										if (row.person_verified==2){
											return "身份证信息";
										} else {
											return "车主信息";
										}
									}
								}
								] ] 
					});
};
dataLoader2.load();

function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>

