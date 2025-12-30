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
<script type="text/javascript" charset="UTF-8">

$('.easyui-combobox').combobox();
$('.easyui-linkbutton').linkbutton();
$('.easyui-datetimebox').datetimebox();
function click1(){
	var username=$('#username').val();
	var begin=$('#begin').datetimebox('getValue');
	var end=$('#end').datetimebox('getValue');
	var keyword=$('#keyword').val();

	   $('#dg').datagrid('load',{
		      userName:username,
		      begin:begin,
		      end:end,
		      keyword:keyword
		    });
}	
$(function(){
		dataGrid = $('#dg').datagrid({
			url :'<%=basePath%>operation/operlog/findLogByCondition.do',
			loadMsg : '正在处理中....',
			title : '列表',
			iconCls : 'icon-save',
			pagination : true,
			pageSize : 10,
			nowrap : false,
			border : true,
			width:'1080',
			idFiled : 'id',
			columns : [ [ {field : 'id',title : '记录ID',width : 150,align : 'center'},
			              {field : 'username',title : '操作人',width : 200,align : 'center'},
			              {field : 'opertime',title : '操作时间',width : 200,align : 'center',
			            	  formatter:function(value,rowData,rowIndex){
			            		  if(value!=null&&""!=value){
				        			  var JsonDateValue = new Date(value.time);
				            		  var text = JsonDateValue.toLocaleDateString();
				            		                return text;
				        		  }else{
				        			  return "";
				        		  }
			            		         }
			              },
			              {field : 'desc',title : '操作内容',width : 240,align : 'center', } ,
			              {field : 'desc',title : '原内容',width : 240,align : 'center',}
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

function myformatter(date){
	var h = date.getHours();
	var M = date.getMinutes();
	var s = date.getSeconds();
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M);
}

function myparser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);

	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
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
				style="height: 100px;">
				<h2 class="page-title txt-color-blueDark">
					<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;日志查询
				</h2>
				<table>
					<tr>
						<td style="width: 250">操作人: <input id="username"
							name="username" Class="textbox _textbox"
							style="width: 150px; height: 22px;" />
						</td>
						<td style="width: 350">反馈时间: <input id="begin" name="begin"
							Class="easyui-datetimebox" style="width: 100px; height: 22px;"
							data-options="formatter:myformatter" /> ~ <input id="end"
							name="end" Class="easyui-datetimebox"
							style="width: 100px; height: 22px;"
							data-options="formatter:myformatter" />
						</td>
						<td style="width: 250">操作关键字: <input id="keyword"
							name="keyword" Class="textbox _textbox"
							style="width: 150px; height: 22px;" />
						</td>
						<td><a href="javascript:void(0)" onclick="click1()"
							style="margin-left: 20px; width: 80px; height: 30px;"
							class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
					</tr>
				</table>
				<div id="tb"></div>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
</body>
</html>

