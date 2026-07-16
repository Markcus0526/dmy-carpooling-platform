<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<script>
	var tb_index = -1;
	$('.easyui-dialog').dialog();
	$('.easyui-dialog').dialog('close');
	$('.easyui-tree').tree();
	$('.easyui-tabs').tabs();
	$('.easyui-datebox').datebox();			
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-combobox').combobox();
	$('.easyui-searchbox').searchbox();
	$('.easyui-validatebox').validatebox();
	var page_path = '<%=basePath%>operation/abbrecord/';
$(function(){
	  $('#tb_2').datagrid({
			url: page_path + 'findblack.do',
			loadMsg : '数据处理中，请稍后....',
			width:'1050',
			height: 'auto',
			fitColumns: true,
			pagination: true,
			columns:[[
			          {field:'opreation1',title:'操作',width:100, align:'center',
			        	  formatter:function(value,row,index){  
				               var btn="";
				                btn += '<a class="editcls" onclick="removeBlack('+row.userid+')" href="javascript:void(0)">解除黑名单</a> ';  
				               return btn;  
				         }
			          },
			          {field:'userid',title:'违约客户ID',width:100, align:'center'},
			          {field:'username',title:'违约客户姓名',width:100, align:'center'},
			          {field:'phone',title:'违约客户手机',width:100,align:'center'},
			          {field:'abb_type',title:'黑名单原因',width:150,align:'center',
			        	  formatter:function(value){
			                  if(value==4)
			                      return '车主逃单';
			                  if(value!=3)
			                      return '乘客逃单';
			              } 
			          },
			          {field:'limit_days_begin',title:'黑名单时间',width:100,align:'center',
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
			          {field:'limit_days',title:'剩余天数',width:100,align:'center',
			        	  formatter:function(value){
			                  if(value<0)
			                      return '永久';
			                  if(value>=0)
			                      return value;
			              } 
			        	  }
			]],
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

	function select(){
	       var  uerselect = $('#uerselect1').combobox('getValue');
	       var inputvalue = $('#inputvalue1').val();     
		   $('#tb_2').datagrid('load',{
			   uerselect:uerselect,
			   inputvalue:inputvalue
			   });
	}
   function removeBlack(userid){
	   msg='是否解除该客户 的黑名单处罚？';
	   if(window.confirm(msg)) {
	    URL=page_path + 'removeBlack.do?userid='+userid;
	    window.location=URL;
	   }else{
		   
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
		<h2 class="page-title txt-color-blueDark">
			<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;黑名单管理
		</h2>
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 50px;">
				<div style="height: 50; padding: 5px; margin-left: 30px">
					<select id="uerselect1" name="uerselect1" class="easyui-combobox"
						style="width: 150px;"" >
						<option value="1">按ID查询</option>
						<option value="2">按手机查询</option>
						<option value="3">按姓名查询</option>
					</select> <input id="inputvalue1" name="inputvalue1" style="width: 170">
					<a href="javascript:void(0)" onclick="select()"
						style="margin-left: 100px; width: 80px;" class="easyui-linkbutton"
						iconCls="icon-search">查询</a>
				</div>
				<div id="tb"></div>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="tb_2" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
</body>
</html>