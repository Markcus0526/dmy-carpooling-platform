<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<br />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<html>

<head>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" 	src="<%=basePath%>js/jquery.easyui.min.js" charset="UTF-8"></script> 
<script type="text/javascript" src="<%=basePath%>js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="<%=basePath%>js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>js/themes/default/easyui.css"
	type="text/css"></link>
	<script type="text/javascript" charset="UTF-8">
	$(function(){
			dataGrid = $('#dg').datagrid({
				url :'<%=basePath%>spread/activity/activity_search.do',
				loadMsg : '正在处理中....',
				title : '列表',
				iconCls : 'icon-save',
				pagination : true,
				pageSize : 10,
				nowrap : false,
				border : true,
				width:'1080',
				idFiled : 'id',
				columns : [ [ {field : 'operation',title : '操作',width : 150,align : 'center'},
				              {field : 'id',title : '活动ID',width : 100,align : 'center'},
				              {field : 'name',title : '活动名称',width : 100,align : 'center'},
				              {field : 'startEndTime',title : '活动时限',width : 400,align : 'center', } ,
				              {field : 'status',title : '活动状态',width : 100,align : 'center', } ,
				              {field : 'joinPeople',title : '已参加人数',width : 100,align : 'center',},
				              {field : 'giveGiftNum',title : '已发放奖品数',width : 100,align : 'center',}
				              ] ],
				onLoadSuccess : function() {
					$('#list table').show();
					parent.$.messager.progress('close');
				},
				onHeaderContextMenu: function(e, field){
					e.preventDefault();
					if (!cmenu){
						createColumnMenu();
					}
					cmenu.menu('show', {
						left:e.pageX,
						top:e.pageY
					});
				}
			});
	});

	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}

	//停止活动
	function stopView(id){
		location = "<%=basePath%>spread/activity/activity_stopActivity.do?id="+id;
	}
	
	//查看活动
	function showView(id){
		location = "<%=basePath%>spread/activity/activity_showActivity.do?id="+id;
	}
	
	//修改活动
	function editView(id){
		location = "<%=basePath%>spread/activity/activity_editActivity.do?id="+id;
	}
	
	//搜索
	function doSearch(){
		var type = $("input:radio:checked").val();
		if(type==undefined) type=0;
		var name = $("#name").val();
		var id = $("#ids").val();
		   $('#dg').datagrid('load',{
			      type:type,
			      name:name,
			      id:id
			    });
	}
	</script>
</head>

<body onload="loaddone()">
	<div id="tab" data-options="fit:true"	style="border: gray 1px solid; padding: 1px; margin: 0px;height:720">
	<div class="easyui-layout" data-options="fit:true,border:true"	style="border: red 0px solid;">
	<div data-options="region:'north',title:'',border:true" style="height: 100px;">
		<h2 class="page-title txt-color-blueDark">
			<h2 style="margin-left:10px;margin-bottom:0px">活动推广管理</h2>
		</h2>
		
		
   <table>
	 <form id="staticsForm"> 
	<div style="padding: 10px 20px 0px 20px">
		<span>活动ID:</span>
		<input id="ids" name="ids" style="width:145px;" data-options="formatter:myformatter"/>
		<span>活动名称:</span>
		<input id="name" name="name" style="width:145px;" data-options="formatter:myformatter"/>
		<a onclick="javascript:doSearch();" class="easyui-linkbutton l-btn" iconCls="icon-search" style="width:40px"></a>
		<input type="button" value="新建活动" onclick="javascript:location='<%=basePath%>spread/activity/activity_add.do'" />
	</div>

   <input id="u73_input" type="radio" value="0" name="type" checked/>查看所有
   <input id="u75_input" type="radio" value="1" name="type"/> 仅活动中
   </form>
	     </table>
	     
	     
        <div id="tb"></div>
     </div>
<div data-options="region:'center',title:''" style="border: green 0px solid;">
<table id="dg"  style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
	</table>
		</div>
		</div>
	</div>
</body>
</html>

