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
<h2 class="page-title txt-color-blueDark">
	<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp违约查询
</h2>
<hr>
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
	function check2(e){
		  var abb_type=e;
			  var flag=check1(abb_type);
		  if(flag==true){
			    abb_type=$("input[name='"+abb_type+"']:checked").val();
		  } if(flag==false){
			    abb_type=null;
		  }
		  return abb_type;
	}
  function open1(){
	  $('#tb_1').datagrid({
			url: page_path +'find.do',
			loadMsg : '数据处理中，请稍后....',
			height: 'auto',
			queryParams: {
				abb_type2: check2("abb_type2"),
				abb_type1: check2("abb_type1")
			},
			fitColumns: true,
			pagination: true,
			width:'1050',
			columns:[[
			          {field:'userid',title:'违约客户ID',width:150, align:'center'},
			          {field:'username',title:'违约客户姓名',width:150, align:'center'},
			          {field:'phone',title:'违约客户手机',width:100,align:'center'},
			          {field:'abb_type',title:'违约行为',width:150,align:'center',
			        	  formatter:function(value){
			                  if(value==1)
			                      return '多次取消订单';
			                  if(value==3)
			                      return '乘客逃单';
			              } 
			          },
			          {field:'abb_time',title:'违约时间',width:150,align:'center',
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
			          {field:'cancel_number',title:'违约层度',width:250,align:'center'},
			          {field:'status',title:'状态',width:200,align:'center',
			        	  formatter:function(value){
			                  if(value==1)
			                      return ' 待处理';
			                  if(value==2)
			                      return '已做警告处罚';
			                  if(value==3)
			                	  return '已做扣款处罚';
			                  if(value==5)
			                	  return '已解除黑名单';
			              }
			          },
			          {field:'order_exec_id',title:'相关订单',width:250,align:'center',
			        	  formatter:function(value,row,index){  
			        		  if(row.order_exec_id!=0){
			              	    var btn="";
			              	  btn+= '<a href="javascript:void(0)" onclick="view('+row.order_exec_id+')">'+row.order_exec_id+'</a>';
			              	  return btn;
			        	  }
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
  }
	function open2(){
		$('#tb_2').datagrid({
			url: page_path + 'find.do',
			height: 'auto',
			queryParams: {
				abb_type2: check2("abb_type4"),
				abb_type1: check2("abb_type3")
			},
			fitColumns: true,
			pagination: true,
			width:'1050',
			columns:[[
			          {field:'userid',title:'违约客户ID',width:150,  align:'center'},
			          {field:'username',title:'违约客户姓名',width:150, align:'center'},
			          {field:'phone',title:'违约客户手机',width:100,align:'center'},
			          {field:'abb_type',title:'违约行为',width:150,align:'center',
			        	  formatter:function(value){
			                  if(value==2)
			                      return ' 车主迟到';
			                  if(value==4)
			                      return '车主逃单';
			              }
			          },
			          {field:'abb_time',title:'违约时间',width:150,align:'center',
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
			          {field:'cancel_number',title:'违约程度',width:250,align:'center'},
			          {field:'status',title:'状态',width:200,align:'center',
			        	  formatter:function(value){
			                  if(value==1)
			                      return ' 待处理';
			                  if(value==2)
			                      return '已做警告处罚';
			                  if(value==3)
			                	  return '已做扣款处罚';
			                  if(value==5)
			                	  return '已解除黑名单';
			              }
			          },
			          {field:'order_exec_id',title:'相关订单',width:250,align:'center',
			        	  formatter:function(value,row,index){  
			        		  if(row.order_exec_id!=0){
			              	    var btn="";
			              	  btn+= '<a href="javascript:void(0)" onclick="view('+row.order_exec_id+')">'+row.order_exec_id+'</a>';
			              	  return btn;
			        	  }
			              	     
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
	}
  
	$(function(){
		document.getElementById("abb_type1").checked = true;
		document.getElementById("abb_type2").checked = true;
		document.getElementById("abb_type3").checked = true;
		document.getElementById("abb_type4").checked = true;
		   open1();
		$('#tab').tabs({   
			       border:true,  
			       height:600,
			       width:'auto',
			       onSelect:function(title){ 
			         if(title=="拼车乘客"){
			        	open1();
			           }
			        if(title=="拼车车主"){
						open2();
		           	 }
				},
		 });
	});
	function c(value){
		alert(value);
	}
	
	function select1(){
	       var status=$("input[name='status']:checked").val();
	       var  uerselect = $('#uerselect').combobox('getValue'); 
	       var inputvalue = $('#inputvalue').val();
		   $('#tb_1').datagrid('load',{
			   status:status,
			   uerselect:uerselect,
			   inputvalue:inputvalue,
			   abb_type2: check2("abb_type2"),
				abb_type1: check2("abb_type1")
			   });
}
	function select2(){
	       var status=$("input[name='status1']:checked").val();
	       var  uerselect = $('#uerselect1').combobox('getValue');
	       var inputvalue = $('#inputvalue1').val();     
		   $('#tb_2').datagrid('load',{
			   status:status,
			   uerselect:uerselect,
			   inputvalue:inputvalue,
			   abb_type2: check2("abb_type4"),
			   abb_type1: check2("abb_type3"),
			   });
	}
	

	function check1(id){
		if(document.getElementById(id).type=='checkbox'){
			if(document.getElementById(id).checked==true){
		      document.getElementById(id).checked = true;
			   }
			 if(document.getElementById(id).checked==false){
		      document.getElementById(id).checked = false;
			   }
			  }
		return document.getElementById(id).checked;
	}
	function view(id) {
		var page_path = '${pageContext.request.contextPath}/order/appoint/';
		window.location=page_path + "view1.do?id="+id+"&&choose="+3 ;
	}
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">
	<div id="tab" class="easyui-tabs" data-options="tabWidth:112"
		style="width: auto">
		<div title="拼车乘客" style="padding: 10px">
			<div class="easyui-layout" data-options="fit:true,border:true"
				style="border: red 0px solid;">
				<div data-options="region:'north',title:'',border:true"
					style="height: 110px;">
					<div style="padding: 5px;">
						<select id="uerselect" name="uerselect" class="easyui-combobox"
							style="width: 150px;">
							<option value="1">按客户ID查询</option>
							<option value="2">按客户手机查询</option>
							<option value="3">按客户姓名查询</option>
						</select> <input id="inputvalue" name="inputvalue" Class="textbox _textbox"
							style="width: 150; height: 22">
					</div>
					<div style="height: 80">
						<input type="checkbox" value="1" id="abb_type1" name="abb_type1"
							onclick="check1(this.id)" />多次取消服务<br> <br> <input
							type="checkbox" value="3" id="abb_type2" name="abb_type2"
							onclick="check1(this.id)" />乘客订单违约 <input type="checkbox"
							value="5" id="status" name="status" style="margin-left: 500px;" />显示已处罚违约行为
						<a href="javascript:void(0)" onclick="select1()"
							style="margin-left: 100px; width: 80px;"
							class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
							href="#" onclick="javascript:report();"
							style="width: 100px; float: right;"
							class="easyui-linkbutton l-btn" iconCls="icon-xlx">导出Excel</a>
					</div>
				</div>
				<div data-options="region:'center',title:''"
					style="border: green 0px solid;">
					<table id="tb_1" style="padding: 1px; boder: black 0px solid;"
						data-options="fit:true,border:true">
					</table>
				</div>
			</div>
		</div>
		<div title="拼车车主" style="padding: 10px">
			<div class="easyui-layout" data-options="fit:true,border:true"
				style="border: red 0px solid;">
				<div data-options="region:'north',title:'',border:true"
					style="height: 110px;">
					<div style="padding: 5px">
						<select id="uerselect1" name="uerselect1" class="easyui-combobox"
							style="width: 150px;"" >
							<option value="1">按客户ID查询</option>
							<option value="2">按客户手机查询</option>
							<option value="3">按客户姓名查询</option>
						</select> <input id="inputvalue1" name="inputvalue1"
							Class="textbox _textbox" style="width: 150; height: 22">
					</div>
					<div style="height: 80">
						<input type="checkbox" value="2" id="abb_type3" name="abb_type3"
							onclick="check1(this.id)" />车主迟到<br> <br> <input
							type="checkbox" value="4" id="abb_type4" name="abb_type4"
							onclick="check1(this.id)" />车主订单违约 <input type="checkbox"
							value="5" id="status1" name="status1" style="margin-left: 500px;" />显示已处罚违约行为
						<a href="javascript:void(0)" onclick="select2()"
							style="margin-left: 100px; width: 80px;"
							class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
							href="#" onclick="javascript:report();"
							style="width: 100px; float: right;"
							class="easyui-linkbutton l-btn" iconCls="icon-xlx">导出Excel</a>
					</div>
				</div>
				<div data-options="region:'center',title:''"
					style="border: green 0px solid;">
					<table id="tb_2" style="padding: 1px; boder: black 0px solid;"
						data-options="fit:true,border:true">
					</table>
				</div>
			</div>
		</div>

		<div title="代价用户" style="padding: 10px"></div>

		<div title="代价司机" style="padding: 10px"></div>
	</div>

</body>
</html>