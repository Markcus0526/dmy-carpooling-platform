<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
 String path = request.getContextPath();
 String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"operation/car/";
 System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>车辆详情</title>
	<h2 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;车辆详情
	</h2>
	<HR>
</head>
<body onload="loaddone()">
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.min.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/plugins/jquery.window.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"
		charset="UTF-8"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"
		charset="UTF-8"></script>
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/js/themes/icon.css"
		type="text/css"></link>
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/js/themes/default/easyui.css"
		type="text/css"></link>
	<link rel="stylesheet"
		href="${pageContext.request.contextPath}/js/themes/default/combobox.css"
		type="text/css"></link>

	<script type="text/javascript" charset="UTF-8">
//加载全部车辆信息
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
$(function(){
	
	$('#dg').datagrid({   
	    url:'<%=basePath%>getoutto.do', 
	    title:"车辆详情",
	    method:'post',
	    pageNumber:1,
	    iconCls : 'icon-show',
		pagination : true,
		pageSize : 10,
		nowrap : false,
		border : false,
		idFiled : 'id',
		rownumbers:true,
		fitColumns:true,
		singleSelect:true,
		toolbar: '#toolbar',
		columns : [ [ {
			field : 'id',
			title : '编号',
			width : 100,
			align : 'center',
		}, {
			field : 'brand',
			title : '品牌',
			width : 100,
			align : 'center',
		}, {
			field : 'car_style',
			title : '车型',
			width : 100,
			align : 'center',
		},{
			field : 'type',
			title : '级别',
			width : 100,
			align : 'center',
			formatter:function(value){
                if(value==1){
                	 return '经济型';
                }
                   
                if(value==2){
                	return '舒适型';
                }
                if(value==3){
                	return '豪华型';
                }
                if(value==4){
                	return '商务型';
                }
                    
            } 
		}
	
		] ]
			});
});


 
	</script>
	
	<div class="easyui-panel"  style="border: gray 2px solid; padding: 5px; margin: 0px;height:700px">
		<div class="easyui-layout" data-options="fit:true,border:true"style="border: red 0px solid;">
			<div data-options="region:'center',title:''" style="border: green 0px solid;">
					<table id="dg" style="padding: 1px; boder: black 0px solid;" data-options="fit:true,border:true">
					</table>
			</div>
		 </div>
	</div>

<div id="toolbar">
				<a onclick="outxlx()"
					style="border: green 1px solid; width: 100px; margin-left:890px"
					class="easyui-linkbutton" iconCls="icon-xls">导出到excel</a>
			</div>
	      
</body>

</html>