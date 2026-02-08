<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">

<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="<%=basePath%>js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>js/themes/default/easyui.css"
	type="text/css"></link>
<script>

$(function(){
		dataGrid = $('#dg').datagrid({
			url :'<%=basePath%>operation/app/findall.do',
			loadMsg : '正在处理中....',
			title : '列表',
			iconCls : 'icon-show',
			pagination : true,
			pageSize : 10,
			nowrap : false,
			border : true,
			idFiled : 'id',
			width:'1080',
			toolbar : '#content',
			columns : [ [{field : 'operation', title : '操作' , width : 100,	align : 'center',
			      		formatter:function(value,row,index){  
		              	    var btn="";
//	              	    btn += '<a class="editcls" onclick="update('+row.id+')" href="javascript:void(0)">更新</a> &nbsp;';  
		              	    btn += '<a class="editcls" onclick="deleted('+row.id+')" href="javascript:void(0)">删除</a> &nbsp;';  
		              	    return btn;  
		         		   }
		       			 },
			              {field : 'app_code',title : '编号' ,width : 100,align : 'center'},
			              {field : 'app_name',title : '名称',width : 150,align : 'center'},
			              {field : 'bundle_id',title : '平台',width : 150,align : 'center',
			            	  formatter:function(value){
				                  if(value=="")
				                      return 'Android';
				                  if(value!="")
				                      return 'IOS';
				              }  
			              },
			              {field : 'version',title : '版本号',width : 100,align : 'center'},
			              {field : 'size',title : '大小',width : 150,align : 'center'}, 
			              {field : 'qrcode_path',title : '拓展二维码',width : 150,align : 'center',
			            	  formatter:function(value,row,index){  
				              	    var btn="";
				              	    btn += '<a class="editcls" onclick="download1('+row.id+','+row.version+')" href="javascript:void(0)">下载</a> &nbsp;';  
				              	    return btn;  
				         		   }
				       			 },
			              {field : 'url',title : '安装包',width : 150,align : 'center',
				       				formatter:function(value,row,index){  
					              	    var btn="";
					              	    btn += '<a class="editcls" onclick="download2('+row.id+','+row.version+')" href="javascript:void(0)">下载</a> &nbsp;';  
					              	    return btn;  
					         		   }
					       			 }
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
//	function update(id){
//	  $('#tb').dialog({ 
//		   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
//		    title:'更新APP',
//		    width:420,   
//		    height:350,   
//			    modal:true , 
//		    closable:false,
//			    draggable:true,
//			    border:true,
//			    top:100,
//			    content:'
//			    <form action="operation/app/update" method="post" enctype="multipart/form-data">
//			    <table id="addTable" style="width:360px;height:280px;margin-left:40px;">
//			    <input type="text" id="id" name="id" style="display:none"/> <tr>
//			    <td> 版本号:<input type="text" id="version" name="version" style="margin-left:40px;"> </td></tr>
//			    <tr> <td> 二维码:<input type="file" id="QR_code" name="QR_code" style="margin-left:40px;"> </td></tr>
//			    <tr><td> 安装包:<input type="file" id="pack_name" name="pack_name" style="margin-left:40px;"> </td>
//			    </tr>  <tr> <td> <input type="submit" value="更新"  style="width:60px;height:30px;margin-left:130px;"> </td> </tr>   </table>  </form>',
//			    onOpen:function(){
//					 $('#id').val(id);
//					 alert(id);
//				 }
//			});  
//		}

function download1(id,version){
	var page_path='<%=basePath%>operation/app/';
	$.ajax({
		url:page_path+'findById.do',
		data:{'id':id,'version':version},
		method:'post',
		success:function(data){
		var	filename=data.qrcode_path;
		window.location.href=page_path+'download.do?filename='+filename+"&app_code="+data.app_code+"&version="+data.version+"&url="+data.url;
		}
	});	
}
function download2(id,version){
	var page_path='<%=basePath%>operation/app/';
	$.ajax({
		url:page_path+'findById.do',
		data:{'id':id,'version':version},
		method:'post',
		success:function(data){
		var	filename=data.url;
		window.location.href=page_path+'download.do?filename='+filename+"&app_code="+data.app_code+"&version="+data.version+"&url="+data.url;
		}
	});	
}

function deleted(id) {
	var page_path='<%=basePath%>operation/app/';
	   msg='是否删除？';
	   if(window.confirm(msg)) {
	    URL=page_path+"delete.do?id="+id;
	    window.location=URL;
	   }
};
function add(){
	URL='<%=basePath%>operation/app/list.do?list='+1;
	window.location=URL;
}
function update(){
	URL='<%=basePath%>operation/app/list.do?list='+2;
	window.location=URL;
}
function  change1(value){	
	$('#dg').datagrid('load',{
		"redio":value
		});


}
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
}
</script>
</head>

<body onload="loaddone()">
	<div id="tab" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px; height: 720">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 110px;">
				<h2 class="page-title txt-color-blueDark">
					<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;app管理
				</h2>
				<hr>
				<div style="padding: 5px; height: 30">
					<input type="radio" id="test" name="test" value="1"
						onchange="change1(this.value)"  checked="checked"/>显示最新版本 <input type="radio"
						id="test" name="test" value="2" 
						onchange="change1(this.value)" />显示全部版本
				</div>
				<div id="tb"></div>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
		<div id="content" style="padding: 5px; height: 20px">
			<a href="javascript:void(0)" onclick="update()" target="_self"
				style="width: 100px; float: right;" class="easyui-linkbutton"
				iconCls="icon-redo">更新APP</a> <a href="javascript:void(0)"
				onclick="add()" target="_self" style="width: 100px; float: right;"
				class="easyui-linkbutton" iconCls="icon-add">添加APP</a>
		</div>
	</div>
</body>

</html>