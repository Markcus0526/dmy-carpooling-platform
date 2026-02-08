<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"financial/myform/";
System.out.println(basePath);
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

</head>
<body>
	<script type="text/javascript" charset="UTF-8">
//选择
function select(ordernum){
	var o =ordernum;
	alert(o);
	
}
//查询方法
function ordersearch(){
	var chkusertype=$("#chkusertype").get(0).checked == true ? 1 : 0;
	var chkusertype1=$("#chkusertype1").get(0).checked == true ? 1 : 0;
	var ordertype1=$("#ordertype1").get(0).checked == true ? 1 : 0;
	var ordertype2=$("#ordertype2").get(0).checked == true ? 1 : 0;
	var ordertype3=$("#ordertype3").get(0).checked == true ? 1 : 0;
	var orderstatus1=$("#orderstatus1").get(0).checked == true ? 1 : 0;
	var orderstatus2=$("#orderstatus2").get(0).checked == true ? 1 : 0;
	var ordernum=$("#ordercsid").val();
	var userorderselect=$("#userorderselect").combobox('getValue');
	var begintime = $("#date1").datebox("getValue");
	var endtime = $("#date2").datebox("getValue");
	var orderusername = $("#orderusername").val();

	$('#dg').datagrid('load',{
		chkusertype :chkusertype,
		chkusertype1:chkusertype1,
		ordertype1:ordertype1,
		ordertype2:ordertype2,
		ordertype3:ordertype3,
		orderstatus1:orderstatus1,
		orderstatus2:orderstatus2,
		userorderselect:userorderselect,
		orderusername:orderusername,
		begintime:begintime,
		endtime:endtime,
		ordernum:ordernum,
				});		
}
//加载表格中的全部数据

$(function(){
	var id=${usersid};
	
	$('#usersid').val(id);
	$('#dg').datagrid({
		queryParams:{	    	  
			chkusertype:$("#chkusertype").get(0).checked == true ? 1 : 0,
			chkusertype1:$("#chkusertype1").get(0).checked == true ? 1 : 0,
			ordertype1:$("#ordertype1").get(0).checked == true ? 1 : 0,	
			ordertype2:$("#ordertype2").get(0).checked == true ? 1 : 0,	
			ordertype3:$("#ordertype3").get(0).checked == true ? 1 : 0,	
			orderstatus1:$("#orderstatus1").get(0).checked == true ? 1 : 0,
			orderstatus2:$("#orderstatus2").get(0).checked == true ? 1 : 0,
   },
		title:"个人客户",
	    url:'<%=basePath%>findorder.do?usersid='+id, 
	    method:'post',
	    pageNumber:'1',
	    iconCls : 'icon-show',
		pagination : true,
		pageSize : 10,
		nowrap : false,
		border : false,
		idFiled : 'id',
		rownumbers:true,
		singleSelect:true,
		fitColumns:true,
		showFooter:true,
		columns : [ [ {
			field : 'op',
			title : '操作',
			width : 50,
			formatter:function(value,row,index){  
				
	          	    var btn="";
	          	    btn += '<a class="editcls" onclick="modify()">查看</a>|'; 
	          	  btn += '<a class="editcls" onclick="select('+row.ordernum.toString()+')">选择</a> &nbsp;'; 
	          	    return btn;  }
     		   
		}, {
			field : 'ordernum',
			title : '订单编号',
			width : 100,
			align:'center'
		}, 
		{
			field : 'ordertype',
			title : '订单类型',
			width : 100,
			align:'center'
		}, 
		{
			field : 'pname',
			title : '乘客姓名',
			width : 100,
			align:'center'
		}, {
			field : 'pphone',
			title : '乘客手机',
			width : 100,
			align:'center'
		}, {
			field : 'dname',
			title : '车主姓名',
			width : 100,
			align:'center'
		},{
			field : 'dphone',
			title : '车主手机',
			width : 100,
			align:'center'
		}, {
			field : 'orderstatus',
			title : '订单状态',
			width : 100,
			align:'center',
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

			
		}
		, {
			field : 'statustime',
			title : '状态时间',
			width : 100,
			align:'center',
			formatter:function(value,row,index){ 
      		  if(row.orderstatus==1){
      			  date = row.cr_date;
		          } if(row.orderstatus==2){
		        	  date = row.ti_accept_order;
		          } if(row.orderstatus==3){
		        		date =  row.begin_exec_time;
		          } if(row.orderstatus==4){
		        		date = row.driverarrival_time;
		          } if(row.orderstatus==5){
		        		date = row.beginservice_time;
		          } if(row.orderstatus==6){
		        		date =row.stopservice_time;
		          } if(row.orderstatus==7){
		        		date =row.pay_time;
		          } if(row.orderstatus==8){
		        	  date =  row.pass_cancel_time;
		          } if(row.orderstatus==9){
		        		date =row.driver_cancel_time;
		        	
		          }
		          return date;

				}}
		
		] ]
			});
	
	//待我审核
	
});

</script>
	<br />
	<h2 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp选择单个订单
	</h2>
	<HR>
	<div>
		<select id="userorderselect" name="searchType" class="easyui-combobox">
			<option value="1">客户ID</option>
			<option value="2">客户手机</option>
		</select> <input id="usersid" type="text" name="usersid" /> <label
			style="margin-left: 20px;">客户姓名：</label> <input id="orderusername"
			name="orderusername" type="text" />
	</div>
	<div style="margin-top: 10px;">
		成交时间：</label><input id="date1" type="text" class="easyui-datebox">--<input
			id="date2" type="text" class="easyui-datebox"> <label
			style="margin-left: 20px;">客户身份：</label> <input type="checkbox"
			id="chkusertype" name="chkusertype" checked="checked" /> <label>乘客</label>

		<input type="checkbox" id="chkusertype1" name="chkusertype1"
			checked="checked" /> <label>车主</label>

	</div>
	<div style="margin-top: 10px;">
		<label> 订单类型：</label> <input type="checkbox" id="ordertype1"
			name="ordertype1" checked="checked" /> <label>单次拼车</label> <input
			type="checkbox" id="ordertype2" name="ordertype2" checked="checked" />
		<label>上下班拼车</label> <input type="checkbox" id="ordertype3"
			name="ordertype3" checked="checked" /> <label>长途拼车</label>
	</div>
	<div style="margin-top: 10px;">

		<label> 订单状态：</label> <input type="checkbox" id="orderstatus1"
			name="orderstatus1" checked="checked" /> <label>已完成</label>
		<input type="checkbox" id="orderstatus2" name="orderstatus2"
			checked="checked" /> <label>未完成</label>
	</div>
	<div style="margin-top: 10px;">
		<label>订单编号</label><input type="text" id="ordercsid"> <a
			onclick="ordersearch()" style="width: 80px; margin-left: 330px;"
			class="easyui-linkbutton" iconCls="icon-search">查询</a>
	</div>
	<HR>
	<div style="margin-left: 10px;">
		<table id="dg"></table>

	</div>
</body>
</html>
