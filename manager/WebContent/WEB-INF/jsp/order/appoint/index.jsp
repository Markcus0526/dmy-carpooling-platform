<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	href="${pageContext.request.contextPath}/css/myCss.css" />
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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>

<script>
	var content = "";
	var page_path = '${pageContext.request.contextPath}/order/appoint/';
	$('.easyui-combobox').combobox();
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-datetimebox').datetimebox();
	
	$(function(){
	var  status="${status}";
	if(status==2){
		document.getElementById("status_2").checked=true;
		//$('#status_2').checked=true;
	}if(status==3){
		document.getElementById("status_3").checked=true;
	}
	if(status==4){
		document.getElementById("status_4").checked=true;
	}	if(status==5){
		document.getElementById("status_5").checked=true;
	}if(status==6){
		document.getElementById("status_6").checked=true;
	}if(status==7){
		document.getElementById("status_7").checked=true;
	}if(status==8){
		document.getElementById("status_8").checked=true;
		//$('#status_8').checked=true;
	}
	});
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
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
	
	
	function btnSearch() {        
		   $('#dg').datagrid('load',{
			   	 order_city : $('#order_city').val(),    
		         order_num : $('#order_num').val(),
		         beginTime : $('#beginTime').datetimebox('getValue'),     
		        endTime : $('#endTime').datetimebox('getValue'),
		        usercode : $('#usercode').val(),     
		         username : $('#username').val(),
		         userphone : $('#userphone').val(),     
		         nickname : $('#nickname').val(),
	        	 status :$('input[name=status]:checked').val(),
        		order_type1:$("input[name='order_type1']:checked").val(),
	        	order_type2:$("input[name='order_type2']:checked").val(),
	        	order_type3:$("input[name='order_type3']:checked").val(),
	         	dept : $('#dept').combobox('getValue'),
		   });
	}
	
	$(function(){
		$('#dg').datagrid({
	         url:page_path + 'search.do',
	         loadMsg : '数据处理中，请稍后....',
	         queryParams:{
	        	 order_city : $('#order_city').val(),    
		         order_num : $('#order_num').val(),
		         beginTime : $('#beginTime').val(),     
		        endTime : $('#endTime').val(),
		        usercode : $('#usercode').val(),     
		         username : $('#username').val(),
		         userphone : $('#userphone').val(),     
		         nickname : $('#nickname').val(),
	        	 status :$('input[name=status]:checked').val(),
        		order_type1:$("input[name='order_type1']:checked").val(),
	        	order_type2:$("input[name='order_type2']:checked").val(),
	        	order_type3:$("input[name='order_type3']:checked").val(),
	         	dept : $('#dept').combobox('getValue'),
	         },
	         title:'',
	         height: 'auto',
	         pagination: true,
	         fitColumns: true,
	        remoteSort : false,
			   striped : true,
			   singleSelect:true,
			   toolbar : '#content',
			columns:[[
	             {field:'operMenu',width:100,align:'center',title:'操作',
	           	  formatter:function(value,row,index){  
		               var btn="";
		              btn+= '<a href="javascript:void(0)" onclick="view('+row.id+')">查看</a>|';
		     		  btn+='<a href="javascript:void(0)" onclick="edit('+row.id+')">修改</a>';
		               return btn;  
	        	  }	 
	             }, 
	             {field:'id',width:150,align:'center',title:'订单编号'},
	             {field:'order_city',width:100,align:'center',title:'所在城市'},
	             {field:'order_type',width:100,align:'center',title:'订单类型',
	             	 formatter:function(value,row,index){ 
	            		  if(value==1){
				        	  return '单次拼车';
				          }
				           if(value==2){
				        	  return '上下班拼车';
				          }  
				           if(value==3){
				        	  return '上下班拼车';
				          }
				           if(value==4){
					          return '长途拼车';
					      }	
	            	 } 
	             },  
	             {field:'guestName',width:70,align:'center',title:'乘客姓名'},
	             {field:'guestPhone',width:120,align:'center',title:'乘客手机'},
	             {field:'carName',width:70,align:'center',title:'车主姓名'},
	             {field:'carPhone',width:120,align:'center',title:'车主手机'},
	             {field:'status',width:100,align:'center',title:'订单状态',
	              	 formatter:function(value,row,index){ 
	            		  if(value==1){
				        	  return '发布';
				          } if(value==2){
				        	  return '成交/待执行';
				          } if(value==3){
				        	  return '开始执行';
				          } if(value==4){
				        	  return '车主到达';
				          } if(value==5){
				        	  return '乘客上车';
				          } if(value==6){
				        	  return '执行结束/待支付';
				          } if(value==7){
				        	  return '已支付/待预约（单拼、长途完结)';
				          } if(value==8){
				        	  return '已销单（非正常完结）';
				          } if(value==9){
				        	  return '结束服务（上下班完结）';
				          }
	            	 }	 
	             },
	             {field:'statusTime',width:150,align:'center',title:'状态时间',
	               	 formatter:function(value,row,index){ 
	            		  if(row.status==1){
	            			  date = row.cr_date;
				          } if(row.status==2){
				        	  date = row.ti_accept_order;
				          } if(row.status==3){
				        		date =  row.begin_exec_time;
				          } if(row.status==4){
				        		date = row.driverarrival_time;
				          } if(row.status==5){
				        		date = row.beginservice_time;
				          } if(row.status==6){
				        		date =row.stopservice_time;
				          } if(row.status==7){
				        		date =row.pay_time;
				          } if(row.status==8){
				        	  date =  row.pass_cancel_time;
				          } if(row.status==9){
				        		date =row.driver_cancel_time;
				        	
				          }
				          return date;
	            	 }	 
	             },
	         ]],
	         onLoadSuccess : function(ret) {
				$('#total_size').text(ret.total);
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

	function view(id) {
		window.location=page_path + "view1.do?id="+id+"&&choose="+1 ;
	}
	
	function edit(id) {
		window.location=page_path + "edit1.do?id="+id+"&&choose="+1;
	}
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">

	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 2px solid; padding: 5px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 175px;">
				<form id="searchForm" action="search">
					<input type="hidden" name="param" />
					<div style="padding: 5px; margin-left: 10px">
						所在城市: <input id="order_city" name="order_city"
							Class="textbox _textbox" /> <span style="margin-left: 50px">检索时间:</span>
						<input id="beginTime" name="beginTime" Class="easyui-datetimebox"
							style="width: 145px" data-options="formatter:myformatter" /> ~ <input
							id="endTime" Class="easyui-datetimebox" style="width: 145px"
							data-options="formatter:myformatter" /> <span
							style="margin-left: 50px">订单编号:</span> <input id="order_num"
							name="order_num" Class="textbox _textbox" />
					</div>
					<div style="padding: 5px; margin-left: 10px">
						订单状态: <input id="status_2" style="margin-left: 20px"
							checked="checked" type="radio" name="status" value="2"><label
							for="status_2">成交</label> <input id="status_3"
							style="margin-left: 20px" type="radio" name="status" value="3"><label
							for="status_3">开始执行</label> <input id="status_4"
							style="margin-left: 20px" type="radio" name="status" value="4"><label
							for="status_4">车主到达</label> <input id="status_5"
							style="margin-left: 20px" type="radio" name="status" value="5"><label
							for="status_5">乘客上车</label> <input id="status_6"
							style="margin-left: 20px" type="radio" name="status" value="6"><label
							for="status_6">执行结束</label> <input id="status_7"
							style="margin-left: 20px" type="radio" name="status" value="7"><label
							for="status_7">已结算</label> <input id="status_8"
							style="margin-left: 20px" type="radio" name="status" value="8"><label
							for="status_8">已关闭</label>
					</div>
					<div style="padding: 5px; margin-left: 10px">
						订单类型: <input type="checkbox" style="margin-left: 30px"
							checked="checked" name="order_type1" value="1">单次拼车 <input
							type="checkbox" style="margin-left: 30px" checked="checked"
							name="order_type2" value="2">上下班拼车 <input type="checkbox"
							style="margin-left: 25px" checked="checked" name="order_type3"
							value="3">长途拼车
					</div>
					<div style="padding: 5px; margin-left: 25px">
						客户ID: <input id="usercode" Class="textbox _textbox"
							name="usercode"> <span style="margin-left: 50px">客户姓名:</span><input
							id="username" style="margin-left: 5px" Class="textbox _textbox"
							name="username" /> <span style="margin-left: 50px">客户手机:</span><input
							id="userphone" style="margin-left: 5px" Class="textbox _textbox"
							name="userphone" />
					</div>
					<div style="padding: 5px; margin-left: 10px">
						客户身份: <select id="dept" class="easyui-combobox" name="dept"
							style="width: 200px;">
							<option value="3">车主或乘客</option>
							<option value="1">车主</option>
							<option value="2">乘客</option>

						</select> <span style="margin-left: 50px">客户昵称:</span><input id="nickname"
							style="margin-left: 5px" Class="textbox _textbox" name="nickname" />
						<a href="javascript:void(0)" onclick="btnSearch()"
							style="margin-left: 50px; width: 80px;" class="easyui-linkbutton"
							iconCls="icon-search">查 询</a>
					</div>
				</form>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="content" style="padding: 5px; height: 20px;">
		<div style="margin-bottom: 5px;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-xlx" plain="true"
				style="float: right; margin-right: 5px;"> <font
				style="font-weight: bold">导出Excel&nbsp;</font></a>
		</div>
	</div>
</body>
</html>

