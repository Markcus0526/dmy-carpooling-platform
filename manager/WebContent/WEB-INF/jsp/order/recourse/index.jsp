<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<html>

<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>

<script>
var page_path = '<%=basePath%>order/recourse/';
	$('.easyui-combobox').combobox();
	$('.easyui-datebox').datebox();
	$('.easyui-datetimebox').datetimebox();

	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+h+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
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
	
	function batchDel()
	{
	var checked = $('#dg').datagrid('getChecked');
	var id=[];
	var str="";
	$.each(checked, function(index, checked){
		id.push(checked.id);
		id.join(",");
		}); 
	if (confirm("你确定要删除？")) {
		str+=id+",";
		$.ajax({
			url:page_path+'batchDel.do?ids='+str,
			success:function(data){
				if (data.err_code == 0) {
					alert("删除成功!");
					window.location=page_path+'index.do';
					
				} else {
					$.messager.alert('Confirm', data.err_msg, 'error');
				}	
			}
		});
	}

	}
	


	
   $(function(){
		$('#dg').datagrid({
			url: page_path + 'show1.do',
			loadMsg : '数据处理中，请稍后....',
		    iconCls : 'icon-show',
			pagination : true,
			pageSize : 10,
			nowrap : false,
			queryParams:{
				   form_type : $('#form_type').combobox('getValue'),
				   bussi_type : $('#bussi_type').combobox('getValue'),
				   identity : $('#identity').combobox('getValue'),
				   city : $('#city').val(),
				   customer_name : $('#customer_name').val(),
				   start_time : $('#start_time').datetimebox('getValue'),
				   end_time : $('#end_time').datetimebox('getValue'),
				   phone_incoming : $('#phone_incoming').val(),
				   process_state : $("input[name='process_state']:checked").val(),
			},
			border : false,
			singleSelect:false,
			height: 'auto',
			idFiled : 'id',
			rownumbers:true,
			fitColumns:true,
			remoteSort : false,
			   striped : true,
			   singleSelect:true,
			 toolbar : '#content',
			columns:[[
			             {field:'check',checkbox:true},
			             {field:'operation',title:'操作',width:250, align:'center',
			        	  formatter:function(value,row,index){  
				               var btn="";
				               btn += '<a class="editcls" onclick="view1('+row.id+')" href="javascript:void(0)">查看</a> |&nbsp;';  
				                btn += '<a class="editcls" onclick="edit1('+row.id+')" href="javascript:void(0)">修改</a> |&nbsp;'; 
				                btn += '<a class="editcls" onclick="process1('+row.id+')" href="javascript:void(0)">处理</a> &nbsp;';
				                btn += '<a class="editcls" onclick="delFunc('+row.id+')" href="javascript:void(0)">删除</a> &nbsp;';
				               return btn;  
			        	  }
			        	  }, 
			             {field:'id',title:'工单编号',width:150, align:'center'},
			             {field:'customer_name',title:'用户姓名',width:150, align:'center'},
			             {field:'phone_incoming',title:'手机号码',width:100,align:'center'},  
			             {field:'identity',title:'用户身份',width:150,align:'center',
			            	 formatter:function(value,row,index){ 
			            		  if(row.id!=null){
			            		  if(row.driver_verified==1&&row.person_verified==1){
							        	  return '乘客,车主';
							       }
			            		  if(row.driver_verified==1){
						        	  return '车主';
						          }
						           if(row.person_verified==1){
						        	  return '乘客';
						          }
						           if(row.driver_verified!=1&&row.person_verified!=1){
							        	  return '非乘客,非车主';
							       }
			            		  }
				        	  }
			             },
			             {field:'wf_date',title:'来电时间',width:150,align:'center',
			            	 formatter:function(value,rowData,rowIndex){
			            		  var JsonDateValue = new Date(value.time);
			            		  var text = JsonDateValue.toLocaleString(); 
			            		                return text;
			            		         }
			             },
			             {field:'city',title:'所在城市',width:150,align:'center'},
			             {field:'bussi_type',title:'业务来源',width:150,align:'center',
			            	  formatter:function(value){
				                  if(value==1)
				                      return '拼车';
				                  if(value==2)
				                      return '代驾';
				              } 	 
			             },
			             {field:'form_type',title:'类型',width:150,align:'center',
			            	 formatter:function(value){
				                  if(value==1)
				                      return '订单情况咨询';
				                  if(value==2)
				                      return '费用咨询';
				                  if(value==3)
				                      return 'App使用咨询';
				                  if(value==4)
				                      return '综合咨询';
				                  if(value==5)
				                      return '投诉';
				                
				              } 
			             },
			             {field:'status',title:'状态',width:150,align:'center',
			            	 formatter:function(value){
				                  if(value==1)
				                      return '未处理';
				                  if(value==2)
				                      return '已处理';
				                  if(value==3)
				                      return '处理中';
				              }  
			             }
			        ]],
			    	//onLoadSuccess : function() {
					//	$('#list table').show();
					//	parent.$.messager.progress('close');
					//},
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


	function delFunc(id) {
		if (confirm("你确定要删除？")) {
			$.ajax({
				url:page_path+'delete.do?id='+id,
				success:function(data){
					if (data.err_code == 0) {
						alert("删除成功!");
						window.location=page_path+'index.do';
						
					} else {
						$.messager.alert('Confirm', data.err_msg, 'error');
					}	
				}
			});
		}
		};
	function add1()
	{
	   window.location=page_path+"add1.do";
	};
	
	function view1(id)
	{
		window.location=page_path+"view1.do?id="+id;
	}
	function edit1(id)
	{
		 window.location=page_path+"edit1.do?id="+id;
	}
	function process1(id)
	{
		  window.location=page_path+"process1.do?id="+id;
	}
	function btnSearch()
	{
		
	       var  form_type = $('#form_type').combobox('getValue');
	       var  bussi_type = $('#bussi_type').combobox('getValue');
	       var  identity = $('#identity').combobox('getValue');
	       var city = $('#city').val();     
	       var customer_name = $('#customer_name').val(); 
	   		var start_time=$('#start_time').datetimebox('getValue');
	 		var end_time=$('#end_time').datetimebox('getValue');
	       var phone_incoming = $('#phone_incoming').val(); 
	       var process_state = $("input[name='process_state']:checked").val();
		   $('#dg').datagrid('load',{
			   form_type:form_type,
			   bussi_type:bussi_type,
			   identity:identity,
			   city:city,
			   customer_name:customer_name,
			   start_time:start_time,
			   end_time:end_time,
			   phone_incoming:phone_incoming,
			   process_state:process_state,   
		   });
	}
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">
	<div class="easyui-panel" title="工单管理" data-options="fit:true"
		style="border: gray 2px solid; padding: 5px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 120px;">
				<table style="margin-left: 50px">
					<tr>
						<td>
							<table>
								<tr>
									<td style="text-align: right">工单类型：</td>
									<td><select id="form_type" name="form_type"
										class="easyui-combobox" style="width: 150px;">
											<option value="1">订单情况查询</option>
											<option value="2">费用咨询</option>
											<option value="3">App使用咨询</option>
											<option value="4">综合咨询</option>
											<option value="5">投诉</option>
									</select></td>
								</tr>
								<tr>
									<td><span style="text-align: right">所在城市：</span></td>
									<td><input id="city" name="city" Class="textbox"
										style="width: 120px" /></td>
								</tr>
								<tr>
									<td style="text-align: left"><span>用户身份：</span></td>
									<td><select id="identity" name="identity"
										class="easyui-combobox" style="width: 150px;">
											<option value="1">乘客</option>
											<option value="2">车主</option>
									</select></td>
								</tr>
							</table>
						</td>
						<td><table style="margin-left: 20px">
								<tr>
									<td style="text-align: right">业务来源：</td>
									<td><select id="bussi_type" name="bussi_type"
										class="easyui-combobox" style="width: 150px;">
											<option value="1">拼车</option>
											<option value="2">代驾</option>
									</select></td>
								</tr>
								<tr>
									<td><span style="text-align: right">用户姓名：</span></td>
									<td><input id="customer_name" name="customer_name"
										Class="textbox" style="width: 120px" /></td>
								</tr>
							</table></td>
						<td><table style="margin-left: 20px">
								<tr>
									<td><input Class="easyui-datetimebox" id="start_time"
										style="width: 120px" name="start_time"
										data-options="formatter:myformatter"> ~ <input
										Class="easyui-datetimebox" id="end_time" style="width: 120px"
										name="end_time" data-options="formatter:myformatter" /></td>
								</tr>
								<tr>
									<td style="margin-left: 20px"><span>用户手机号：</span> <input
										id="phone_incoming" name="phone_incoming" Class="textbox"
										style="width: 172px" /></td>
								</tr>
							</table></td>

						<td><table style="margin-left: 20px">
								<tr>
									<td><a href="javascript:void(0)" onclick="btnSearch()"
										style="margin-left: 100px; width: 80px;"
										class="easyui-linkbutton" iconCls="icon-search">查询 </a></td>
								</tr>
							</table></td>
					</tr>
				</table>
				<div style="margin-left: 50px;">
					<input type="radio" checked="checked" name="process_state"
						value="1" />只显示未处理和处理中工单 <input type="radio" name="process_state"
						value="0" />显示所有工单
				</div>
			</div>
			<div data-options="region:'center',title:''"
				style="border: gray 1px solid;">
				<table id="dg" style="padding: 1px; boder: gray 1px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="content" style="padding: 5px; height: 20px;">
		<div style="margin-bottom: 5px;">
			<a href="javascript:void(0)" onclick="report();"
				class="easyui-linkbutton l-btn" style="float: right;"
				iconCls="icon-xlx"><font style="font-weight: bold">导出Excel</font></a>
			<a href="javascript:void(0)" onclick="batchDel();"
				class="easyui-linkbutton l-btn" style="float: right;"
				iconCls="icon-cancel"><font style="font-weight: bold">&nbsp;删除选中&nbsp;</font></a>
			<a href='javascript:void(0)' onclick="add1();"
				class="easyui-linkbutton l-btn" style="float: right;"
				iconCls="icon-add"><font style="font-weight: bold">&nbsp;新建工单&nbsp;</font></a>
		</div>
	</div>

</body>
</html>
