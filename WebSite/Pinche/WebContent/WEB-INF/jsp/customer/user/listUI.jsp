<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>用户列表</title>
<link rel="stylesheet" type="text/css"
	href="js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css" href="js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css" href="js/themes/icon.css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>


<script type="text/javascript" charset="UTF-8">

/* 	function aa(){
	
		    var conditionSelect = $('#conditionSelect').combobox('getValue');
		    var conditionInput = $('#conditionInput').val();
		    var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
		    //console.info($('#conditionSelect').combobox('getValue'));
		    console.info(conditionSelect+"**"+conditionInput+"**"+verifiedStaus);
		    $('#dg').datagrid('load',{
		      conditionSelect :conditionSelect,
		      conditionInput  :conditionInput,
		      verifiedStaus   :verifiedStaus
		    });
			
		} */	
		
		$(function(){
			$('#btn').keyup(function(event) {
				if (event.keyCode == '13') {
					aa();
				}
			});
		});
		
	var datagrid;
	$(function(){
	datagrid=$('#dg').datagrid({		
							url	:'<%=basePath%>customer/pageList.do',
							method : 'POST',
							title : '',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							fit : true,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
							singleSelect:true,
							striped : true,
							toolbar : '#content',
							columns : [ [
									{
										field : 'bianhao',
										title : '编号',
										width : 100,
										checkbox : true
									},
									{
										field : 'action',
										title : '操作',
										width : 220,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
										//str += '<input type="button" value="查看" onclick="show()"/> &nbsp';
  										str += '<a href="customer/showUI.do?id='+row.id+'&car_type_id='+row.car_type_id+'" target="_self">查看</a> &nbsp';
  										str += '<a href="customer/updateUserBaseInfoUI.do?id='+row.id+'&car_type_id='+row.car_type_id+'" target="_self">修改</a> &nbsp';
  										if(row.person_verified=='待认证'){
  										
  										str += '<a href="customer/personVerifiedUI.do?id='+row.id+'" target="_self">认证身份证</a> &nbsp';
  										}
  										if(row.driver_verified=='待认证'){
  										
  										str += '<a href="customer/driverVerifiedUI.do?id='+row.id+'&car_type_id='+row.car_type_id+'" target="_self">认证车主</a> &nbsp';
  										}
  											return str;
  											if(row.id){return row.id}
										}
									}, {
										field : 'id',
										title : '用户id',
										width : 100,
									}, {
										field : 'usercode',
										title : '用户名',
										width : 100
									}, {
										field : 'username',
										title : '姓名',
										width : 100,
										align : 'right'
									}, {
										field : 'group_name',
										title : '所属集团',
										width : 100,
										align : 'right'
									}, {
										field : 'phone',
										title : '手机号码',
										width : 100,
										align : 'right'
									}, {
										field : 'person_verified',
										title : '个人信息认证状态',
										width : 100,
										align : 'right',
										formatter : function(value, row, index) {
										var str = '';
  										if(row.person_verified=='待认证'){
  										str += '<font size="2" color="red">'+value+'</font>';
  										}else{
  										str += '<font size="2">'+value+'</font>'
  										}
  											return str;
  											if(row.id){return row.id}
										}
									} ,{
										field : 'driver_verified',
										title : '车主信息认证状态',
										width : 100,
										align : 'right',
										formatter : function(value, row, index) {
										var str = '';
  										if(row.driver_verified=='待认证'){
  										str += '<font size="2" color="red">'+value+'</font>';
  										}else{
  										str += '<font size="2">'+value+'</font>'
  										}
  											return str;
  											if(row.id){return row.id}
										}
									}
									] ],
							//toolbar : '#toolbar'
							onLoadSuccess : function() {
								$('#list table').show();
								parent.$.messager.progress('close');
							},

							onHeaderContextMenu : function(e, field) {
								e.preventDefault();
								if (!cmenu) {
									createColumnMenu();
								}
								cmenu.menu('show', {
									left : e.pageX,
									top : e.pageY
								});
							}

						}

				);

/* 		var conditionSelect;

		conditionSelect = $('#conditionSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'usercode',
			data : [ {
				label : '按用户名查询',
				value : 'usercode'
			}, {
				label : '按姓名查询',
				value : 'username'
			}, {
				label : '按手机号查询',
				value : 'phone'
			}, {
				label : '按id查询',
				value : 'id'
			} ]

		}); */

	});
	
	function show(){
	$('#infoForm').show();
	
	}
	
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
	
	function doSearch(value, name) {
		var conditionInput = $('#ss').searchbox('getValue');
		var conditionSelect = $('#ss').searchbox('getName');
		var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
		$('#dg').datagrid('load',{
			conditionInput:conditionInput,
			conditionSelect:conditionSelect,
			verifiedStaus:verifiedStaus
		});
	}
	
	function excel(){
		var conditionInput = $('#ss').searchbox('getValue');
		var conditionSelect = $('#ss').searchbox('getName');
		var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
		location = "<%=basePath%>excel/pointDetailsToExcel.do?conditionInput="+conditionInput+"&conditionSelect="+conditionSelect+"&verifiedStaus="+verifiedStaus;
	}
</script>
</head>

<body onload="loaddone()">

	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 35px;">
				<table id="querytable" cellspacing="0" cellpadding="5px"
					style="text-align: left; height: auto; margin: 0px;">
					<tr>
						<td><input id="ss" class="easyui-searchbox"
							data-options="prompt:'输入',menu:'#mm',searcher:doSearch"
							style="width: 500px; height: 25px" /></td>
						<td><input id="all" type="radio" name="verifiedStaus"
							value="all"><font
							style="font-weight: normal; font-size: 80%">全部 </font> <input
							id="person_verified" type="radio" name="verifiedStaus"
							value="person_verified"><font
							style="font-weight: normal; font-size: 80%">个人信息待认证客户</font> <input
							id="driver_verified" type="radio" name="verifiedStaus"
							value="driver_verified"><font
							style="font-weight: normal; font-size: 80%">车主信息待认证客户</font></td>
					</tr>

				</table>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="mm">
		<div data-options="name:'0'">全部查询</div>
		<div data-options="name:'usercode'">按用户名查询</div>
		<div data-options="name:'username'">按姓名查询</div>
		<div data-options="name:'phone'">按手机号查询</div>
		<div data-options="name:'id'">按ID查询</div>
	</div>

	<!-- 						 <input id="all" type="radio" name="verifiedStaus"  value="all">全部 
						 <input id="person_verified" type="radio" name="verifiedStaus" value="person_verified">个人信息待认证客户
						 <input id="driver_verified" type="radio" name="verifiedStaus" value="driver_verified">车主信息待认证客户	 -->

	<!-- 	<form id="hiddenForm" method="post">
		<input id="searchValue" type="hidden" /> <input id="searchType"
			type="hidden" />
	</form> -->
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
			<a class="easyui-linkbutton" iconCls="icon-xls" plain="true" onclick="excel();"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a>


		</div>
	</div>

	<!-- 	<table id="dg"></table>

	<div id="toolbar">
		<form id="searchForm" class="">
			<table>
				<tr>
					<td>
						<input id="ss"></input>  
						<div id="mm" style="width:120px">  
						    <div data-options="name:'usercode'">按用户名查询</div>  
						    <div data-options="name:'username'">按姓名查询</div>  
						    <div data-options="name:'phone'">按手机号查询</div>  
						    <div data-options="name:'id'">按ID查询</div>  
						</div> 
						
						 <input id="conditionSelect" name="conditionSelectName" value="sdfsf">
						 <input id="conditionInput" name="conditionInputName" >
						 <input id="all" type="radio" name="verifiedStaus"  value="all">全部 
						 <input id="person_verified" type="radio" name="verifiedStaus" value="person_verified">个人信息待认证客户
						 <input id="driver_verified" type="radio" name="verifiedStaus" value="driver_verified">车主信息待认证客户
						 <a id="btn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="float:right;" onclick="aa()">查询</a></td>
				</tr>
			</table>
		</form>

	</div>
	
	<div id="infoForm">
	
	
	
	</div> -->

</body>
</html>
