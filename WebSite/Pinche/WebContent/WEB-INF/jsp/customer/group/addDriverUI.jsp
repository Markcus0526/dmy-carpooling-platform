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

<title>添加车主</title>

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

	function aa(){
	
		    var conditionSelect = $('#conditionSelect').combobox('getValue');
		    var conditionInput = $('#conditionInput').val();
		    var gid = $('#gid').val();
		    $('#dg').datagrid('load',{
		      conditionSelect :conditionSelect,
		      conditionInput  :conditionInput,
		      gid:gid
		    });
		}	

	var datagrid;
	var datagrid1;
	$(function(){
	datagrid=$('#dg').datagrid({		
							url	:'<%=basePath%>customer/diverList.do',
							method : 'POST',
							title : '备选车主列表',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							fit : true,
							width:'auto',
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
							striped : true,
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
										width : 120,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
  										str += '<a href="customer/showUserUI.do?id='+row.id+'" target="_self">查看</a> &nbsp';
  										//str += '<a href="addDriverToGroup.action?gid=${gid}&id='+row.id+'">添加</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}, {
										field : 'id',
										title : 'id',
										width : 100,
										//checkbox : true
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
										field : 'phone',
										title : '手机号码',
										width : 100,
										align : 'right'
									}
									] ],
							toolbar : '#toolbar'
						}
				);
	datagrid1=$('#dg1').datagrid({		
							url	:'<%=basePath%>customer/findGroupDriversByCondition.do?gid=${gid}',
							method : 'POST',
							title : '选定车主列表',
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
							striped : true,
							columns : [ [
									{
										field : 'bianhao',
										title : '编号',
										width : 100,
										checkbox : true
									},
									/* {
										field : 'action',
										title : '操作',
										width : 100,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
  										//str += '<a href="removeDriverFromGroup.action?gid=${gid}&id='+row.id+'">移除</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									} ,*/ {
										field : 'id',
										title : 'id',
										width : 100,
										//checkbox : true
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
										field : 'phone',
										title : '手机号码',
										width : 100,
										align : 'right'
									}
									] ]
							//toolbar : '#toolbar'
						}
				);

		var conditionSelect;
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
		});
		
			$('#searchGroup').keyup(function(event) {
				if (event.keyCode == '13') {
					aa();
				}
			});
	});
	
<%-- 	    function addDriverUI() {
	    alert("addDriverUI");
	    window.location.href="<%=basePath%>addDriverUI.action?gid=${gid}";
        } --%>
	

  
  
<%--   function addDriver(){
  
  		var rowIndex=$('#dg').datagrid('getRowIndex',$('#dg').datagrid('getSelected')) 
  		var row = $('#dg').datagrid('getSelected');
  		console.info(row.id);
  		var id = row.id;
  		alert("gid=====${gid}++"+id);
 
		$.post("<%=basePath%>addDriverToGroup.action", { id: id, gid: "${gid}" },
   		function(data){
     	alert("Data Loaded: " + data);
   		});
  } --%>
  
  function addDriver(){
  //alert("addDriver");
  var row = $('#dg').datagrid('getSelected');
  var id= row.id;
  var rowIndex=$('#dg').datagrid('getRowIndex',$('#dg').datagrid('getSelected')); 
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/addDriverToGroup.do',
		   data:'id='+id+'&gid=${gid}',
		   success: function(resultObject){
			   if(resultObject.returnCode>0){
		   $('#dg').datagrid('deleteRow',rowIndex);
		   $('#dg1').datagrid('appendRow',row);
			   }else{
				    $.messager.alert('提示',resultObject.msg,'info',function(){
				    	window.history.go(-1);
				    }); 
				   
			   }
		   //location.reload(); 
 		   //console.info(data);
		     //alert( "修改成功！ "  ); 
		   }
		});

}
  function removeDriver(){
  
    var row = $('#dg1').datagrid('getSelected');
  	var id= row.id;
    var rowIndex=$('#dg1').datagrid('getRowIndex',$('#dg1').datagrid('getSelected')); 
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/removeDriverFromGroup.do',
		   data:'id='+id+'&gid=${gid}',
		   success: function(resultObject){
			   if(resultObject.returnCode>0){
		   
				   $('#dg1').datagrid('deleteRow',rowIndex);
				   $('#dg').datagrid('appendRow',row);
			   }else{
				    $.messager.alert('提示',resultObject.msg,'info',function(){
				    	window.history.go(-1);
				    }); 
			   }
		   //location.reload() ;
 		   //console.info(data);
		     //alert( "修改成功！ "  ); 
		   }
		});
  
  }
  function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>

<body onload="loaddone()">
	<!-- 	<div id="div1"style="position:absolute;left:5px;top:5px;right:5px;bottom:5px;">

		<div id="toolbar1" 
			style="position:absolute;left:2%;top:2%;width:20%;hight:10%;">
			<form id="searchForm">
				<table border=0>
					<tr>
						<td><input id="conditionSelect" name="conditionSelectName"
							value="">
						</td>
						<td><input id="conditionInput" name="conditionInputName">
						</td>
						<td><a id="searchGroup" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="aa()">查询</a></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dgs" style="position:relative;left:2%;top:10%;width:auto;hight:90%;">
			<div id="dgs1" 
				style="position:absolute;left:0%;top:4%;width:40%;hight:auto;">
				<table id="dg"></table>
			</div>
			<div id="dgs2" style="position:relative;left:42%;top:4%;width:2%;hight:auto;">
				<table border=0>
					<tr>
						<td><a id="searchGroup" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="addDriver()">添加</a>
						</td>
					</tr>
					<tr>
						<td><a id="searchGroup" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'" onclick="removeDriver()">移除</a></td>

					</tr>

				</table>
			</div>
			<div id="dgs3" 
				style="position:relative;left:45%;top:0%;width:50%;hight:780px;">
				<table id="dg1"></table>

			</div>
		</div>
	</div> -->
	<div id="cc" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',title:''" style="height: 50px;">
			<div>
				<input id="conditionSelect" name="conditionSelectName" value="">
				<input id="conditionInput" name="conditionInputName"> <a
					id="searchGroup" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="aa()">查询</a> <a
					id="cancelGroup" href="javascript:void(0)"
					class="easyui-linkbutton" onclick="javascript:history.go(-1);"
					iconcls="icon-cancel" style="float: true">返&nbsp;回</a>
			</div>



		</div>
		<div data-options="region:'east',iconCls:'icon-reload',title:''"
			style="width: 47%;">
			<table id="dg1"></table>
		</div>
		<div data-options="region:'west',title:''" style="width: 47%;">
			<table id="dg"></table>
		</div>
		<div data-options="region:'center',title:''"
			style="padding: 5px; background: #eee;">

			<div style="margin-top: 150px; align: center;">
				<a id="searchGroup" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'" onclick="addDriver()">添加</a> <a
					id="searchGroup" class="easyui-linkbutton"
					data-options="iconCls:'icon-remove'" onclick="removeDriver()">移除</a>
			</div>
		</div>
	</div>
</body>
</html>
