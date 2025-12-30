<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"operation/evaluate/";
System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/jquery.min.js"
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

	<style type="text/css">
</style>
</head>
<body onload="loaddone()">
	<script type="text/javascript" charset="UTF-8">
<!--点击 查询所触发的事件-->
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
function s(){
	
	//这里是表单提交的方法
    var cityselect = $('#cityandre').combobox('getValue');
    var cityinput = $('#cityandre1').val();
    var groupselect = $('#group').combobox('getValue');
    var groupinput = $('#groupinput').val();
   // var yewuselect = $('#yewuselect').combobox('getValue'); 保留功能 揭开注释即可使用
    var userselect = $('#uerselect').combobox('getValue');
    var userinput = $('#userinput').val();
    var khsfselect = $('#khsfselect').combobox('getValue');
    $('#dg').datagrid('load',{
    	cityselect :cityselect,
    	cityinput:cityinput,
    	groupselect:groupselect,
    	groupinput:groupinput,
    	//yewuselect:yewuselect, 保留
    	userselect:userselect,
    	userinput:userinput,
    	khsfselect:khsfselect
    	
			});		
}
//页面加载
$(function(){
	
	$('#dg').datagrid({
		title:"评价管理表格",
	    url:'<%=basePath%>find.do', 
	    method:'post',
	    pageNumber:1,
	    iconCls : 'icon-show',
		pagination : true,
		pageSize : 10,
		nowrap : false,
		idFiled : 'id',
		rownumbers:true,
		singleSelect:true,
		fitColumns:true,
		showFooter:true,
    
		//hight:800,
		width:950,
		autoRowHeight:false,
		border : true,
		checkOnSelect : true,
		striped : true,
		toolbar: '#toolbar',

		columns : [ [ {
			field : 'op',
			title : '操作',
			width : 75,
			formatter:function(value,row,index){  
          	    var btn="";
          	    btn += '<a class="editcls" href="<%=basePath%>action1.do?userid='+row.userid+'">查看详情</a> &nbsp;'; 
          	    return btn;  
     		   },
		}, {
			field : 'userid',
			title : '客户ID',
			width : 100,
			align:'center'
		}, {
			field : 'username',
			title : '客户姓名',
			width : 100,
			align:'center'
		}, {
			field : 'phone',
			title : '客户手机',
			width : 100,
			align:'center'
		},{
			field : 'driver_verified',
			title : '客户身份',
			width : 100,
			align:'center'
		}, {
			field : 'group_name',
			title : '所属集团',
			width : 100,
			align:'center'
		},
		 {
			field : 'cklevelt1',
			title : '乘客身份订单数',
			width : 100,
			align:'center'
		},
		 {
			field : 'ckpl1',
			title : '乘客身份好评率',
			width : 100,
			align:'center'
		}
		, {
			field : 'czlevelt1',
			title : '车主身份订单数',
			width : 100,
			align:'center'
		}
		, {
			field : 'czpl1',
			title : '车主身份好评率',
			width : 100,
			align:'center'
		}
		] ]
			});
});
 
	</script>
		<div id="khsfse1" class="easyui-tabs" plain="true"
			style="border: gray 0px solid;margin-top: 19px; margin-left: 15px; marign-right: 20px">
			<div title="拼车" style="padding: 20px; width:30px;">
				<!-- 数据表格 页面加载时会自动加载 -->
	<div class="easyui-panel" data-options="fit:true" style="height:600px;border: gray 1px solid; padding: 5px; margin: 0px;">
	<div class="easyui-layout" data-options="fit:true,border:true" style="border: red 0px solid;">
	<div data-options="region:'north',title:'',border:true" style="height: 100px;">
	<form id="ff" method="get">
		<table style="margin-left: 60px;">
			<tr>
				<td><select id="cityandre" name="cityselect"
					class="easyui-combobox" style="width:150px;">
						<option value="0">全部</option>
						<option value="1">注册城市</option>
						<option value="2">最后登录城市</option>
						<option value="3">注册或最后登录城市</option>
				</select></td>
				<td><input type="text" id="cityandre1" name="cityandre1"></input></td>
				<td></td>
				<td></td>
				<td><select id="group" name="groupselect"
					class="easyui-combobox" style="width: 150px;">
						<option value="0">全部</option>
						<option value="1">按集团/联盟标识查询</option>
						<option value="2">按集团/联盟名称查询</option>
				</select></td>
				<td><input type="text" id="groupinput" name="groupinput"></input>
				</td>
			</tr>
			<tr></tr>

			<tr>
				<td><select id="uerselect" name="uerselect"
					class="easyui-combobox" style="width: 150px;">
						<option value="0">全部</option>
						<option value="1">按客户ID查询</option>
						<option value="2">按客户手机查询</option>
						<option value="3">按客户姓名查询</option>
				</select></td>
				<td><input type="text" id="userinput" name="userinput"></input>
				</td>
				<td></td>
				<td></td>
				<td><span id="usersf"
					style="font-size: 14px; margin-left: 15px;">客户身份:</span> <select
					id="khsfselect" name="khsfselect" class="easyui-combobox"
					style="width: 70px;">
						<option value="2">全部</option>
						<option value="1">车主</option>
						<option value="0">乘客</option>
				</select></td>
				<td><span id="usersf" style="font-size: 14px;">业务来源:</span> <select
					id="￥" name="#" class="easyui-combobox" style="width: 70px;">
						<option value="1">拼车</option>
						<option value="0">代驾（保留）</option>
				</select></td>
			</tr>



		</table>
		<div>
			<a href="javascript:void(0)" onclick="s()"
				style="margin-left: 740px; width: 80px;" class="easyui-linkbutton"
				iconCls="icon-search">查 询</a> 
		</div>
	</form>
	
</div>	
            <div data-options="region:'center',title:''" style="border: green 0px solid;">
					<table id="dg" style="padding: 1px; boder: black 0px solid;" data-options="fit:true,border:true">
					</table>
			</div>
			</div>
			</div>
			</div>
		
			
			
	
				


			

			<div title="代驾" style="overflow: auto; padding: 20px;">
				<h1>功能保留</h1>
			</div>
		</div>
	
<div id="toolbar">
				<a onclick="outxlx()"
					style="border: green 1px solid; width: 100px; margin-left:890px"
					class="easyui-linkbutton" iconCls="icon-xls">导出到excel</a>
			</div>
		
</body>

</html>