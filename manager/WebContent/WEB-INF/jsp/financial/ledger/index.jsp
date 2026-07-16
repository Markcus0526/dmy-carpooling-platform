<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"financial/ledger/";
System.out.println(basePath);
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"
	charset="UTF-8"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/combobox.css"
	type="text/css"></link>

<style type="text/css">
</style>
<script type="text/javascript" charset="UTF-8">
//平台总账查询方法
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
function searchledger(){
	var inp=$("#inp").get(0).checked == true ? 1 : 0;//"进账"
	var out=$("#out").get(0).checked == true ? 1 : 0;//"出账"
	var pin=$("#pin").get(0).checked == true ? 1 : 0;//"拼车"
	var buscity=($("#buscity").val()=="")?0:$("#buscity").val();//交易城市
	var begintime=($("#begintime").datebox("getValue")=="")?0:$("#begintime").datebox("getValue");//交易起始时间
	var endtime=($("#endtime").datebox("getValue")=="")?0:$("#endtime").datebox("getValue");//交易结束时间
	var orderinput=($("#orderinput").val()=="")?0:$("#orderinput").val();//"订单编号"
	var tsid=($("#tsid").val()=="")?0:$("#tsid").val();//交易编号
	var khtype = $("#khtype").combobox("getValue");

	var khinfo = $("#khinfo").combobox("getValue");
	var tstype = $("#accounttype").combobox("getValue");
	var xianginput = ($("#xianginput").val()=="")?0:$("#xianginput").val();
	//alert(out);
	$('#dg').datagrid('load',{
		inp :inp,
		pin:pin,
		out:out,
		buscity:buscity,
		begintime:begintime,
		endtime:endtime,
		orderinput:orderinput,
		tsid:tsid,
		khtype:khtype,
		khinfo:khinfo,
		xianginput:xianginput,
		tstype:tstype,
	
		
		

				});	
}

//查看详细信心

function orderview (order_cs_id){
	path='<%=basePath%>';
	window.open(path+'info.do?orderid1='+order_cs_id,"_blank");
	
}

//页面加载数据表格
$(function(){
	
	$('#dg').datagrid(
	
		
			{   
	    url:'<%=basePath%>findledger.do',
		onLoadSuccess:function(jsonObject){  
			$("#balance").val(jsonObject.count);
	        
	      },
				method : 'post',
				pageNumber : 1,
				iconCls : 'icon-show',
				pagination : true,
				singleSelect:true,
				pageSize : 10,
				pageList : [10,20,30 ],
				nowrap : false,
				border : false,
				idFiled : 'id',
				rownumbers : true,
				autoRowHeight:false,
				title:"客户信息表",
				toolbar: '#toolbar',
		

				columns : [ [ {
					title : '交易编号',
					field : 'ts_id',
					width : 50
				}, {
					field : 'order_cs_id',
					title : '相关执行订单编号',
					width : 100,
					align:'center',
					formatter:function(value,row,index){  
						
		          	    var btn="";
		          	    btn += '<a href="" class="editcls" onclick="orderview('+row.order_cs_id+');return false;">'+row.order_cs_id+'</a>'; 
		          	 
		          	    return btn;  }
					
					
				}, {
					field : 'passenger_id',
					title : '乘客ID',
					width : 100,
					align:'center',
				}, {
					field : 'passenger_name',
					title : '乘客姓名',
					width : 100,
					align:'center',
				},
				{
					field : 'passenger_invitecode_regist',
					title : '乘客方邀请码',
					width : 100,
					align:'center',
				},
				{
					field : 'invitname',
					title : '乘客方邀请名称',
					width : 100,
					align:'center',
				},
				{
					field : 'driver_id',
					title : '车主id',
					width : 100,
					align:'center',
				}, {
					field : 'driver_name',
					title : '车主姓名',
					width : 100,
					align:'center',
				},
				{
					field : 'driver_invitecode_regist',
					title : '车主方邀请码',
					width : 100,
					align:'center',
				},
				{
					field : 'invitnamed',
					title : '车主方邀请名称',
					width : 100,
					align:'center',
				},
				{
					field : 'order_balance',
					title : '订单金额',
					width : 100,
					align:'center',
				}, {
					field : 'city',
					title : '所在城市',
					width : 100,
					align:'center',
				}, {
					field : 'date',
					title : '时间',
					width : 100,
					align:'center',
				}
				, {
					field : 'application',
					title : '业务来源',
					width : 100,
					align:'center',
				} 
				, {
					field : 'transaction_type',
					title : '进出账',
					width : 100,
					align:'center',
				} 
				, {
					field : 'status_type',
					title : '账目类型',
					width : 100,
					align:'center',
					formatter:function(value,row,index){ 
			       		  if(value=="tx_code_001"){
					        	  return '注册新用户';
					          } if(value=="tx_code_002"){
					        	  return '返还绿点';
					          } if(value=="tx_code_003"){
					        	  return '乘客扣款';
					          } if(value=="tx_code_004"){
					        	  return '平台信息费';
					          } if(value=="tx_code_005"){
					        	  return '分成';
					          } if(value=="tx_code_006"){
					        	  return '车主收入';
					          } if(value=="tx_code_007"){
					        	  return '个人充值';
					          } if(value=="tx_code_008"){
					        	  return '个人提现';
					          } if(value=="tx_code_009"){
					        	  return '冻结绿点';
					          }
					          if(value=="tx_code_010"){
					        	  return '解冻绿点';
					          }
					          if(value=="tx_code_011"){
					        	  return '充值';
					          }
					          if(value=="tx_code_012"){
					        	  return '提现';
					          }
					          if(value=="tx_code_013"){
					        	  return '点券费用';
					          }
					          if(value=="tx_code_014"){
					        	  return '车主邀请费';
					          }
					          if(value=="tx_code_015"){
					        	  return '乘客邀请费';
					          }
					          if(value=="tx_code_016"){
					        	  return '游戏发送绿点';
					          }
					         
					         
			       	 }
				} 
				, {
					field : 'sum',
					title : '金额',
					width : 100,
					align:'center',
				} 
				, {
					field : 'balance',
					title : '余额',
					width : 100,
					align:'center',
				} 
				, {
					field : 'remark',
					title : '备注',
					width : 100,
					align:'center',
				} 
				
				] ],
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

			});

		});
</script>

</head>
<body onload="loaddone()">



	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 2px solid; padding: 5px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'平台总账',border:true"
				style="height: 147px;">




				<div style="margin-top: 5px">
					<lable style="margin-left:20px">交易城市：</lable>
					<input id="buscity" type="text" class="textbox" /> <label
						style="margin-left: 30px">交易时间：</label><input id="begintime"
						type="text" class="easyui-datebox"></input>--<input id="endtime"
						type="text" class="easyui-datebox"></input> <label
						style="margin-left: 20px">相关客户：</label><select id="khtype"
						name="khtype" class="easyui-combobox" style="width: 80px;">
						<option value="0">-全部-</option>
						<option value="1">个人客户</option>
						<option value="2">集团客户</option>
						<option value="3">合作单位</option>
					</select> <select id="khinfo" name="khinfo" class="easyui-combobox"
						style="width: 80px;">
						<option value="0">-全部-</option>
						<option value="1">客户id</option>
						<option value="2">客户账号</option>
						<option value="3">客户名称</option>
						<option value="4">手机号码</option>
					</select> <input id="xianginput" type="text" class="textbox" />
				</div>
				<div style="margin-top: 5px">
					<label style="margin-left: 20px">进出账：</label> <input
						type="checkbox" id="inp" name="inp" checked="checked" /> <label
						for="chkuser">进账</label> <input type="checkbox" id="out"
						name="out" checked="checked" /> <label for="chkgroup">出账</label> <label
						style="margin-left: 20px">业务来源：</label> <input type="checkbox"
						id="pin" name="pin" checked="checked" /> <label for="chkuser">拼车</label>
					<input type="checkbox" id="out" name="out" checked="checked" /> <label
						for="chkgroup">代驾（保留）</label> <label style="margin-left: 20px">订单编号：</label><input
						id="orderinput" type="text" class="textbox" /> <label
						style="margin-left: 20px">交易编号：</label><input id="tsid"
						type="text" class="textbox" />
				</div>
				<div style="margin-top: 5px">
					<label style="margin-left: 20px">账目类型：</label> <select
						id="accounttype" name="accounttype" class="easyui-combobox"
						style="width: 100px;">
						<option value="0">-- - -全部- - --</option>
						<option value="1">注册新用户</option>
						<option value="2">返还绿点</option>
						<option value="3">乘客扣款</option>
						<option value="4">平台信息费</option>
						<option value="5">分成</option>
						<option value="6">车主收入</option>
						<option value="7">个人充值</option>
						<option value="8">个人提现</option>
						<option value="9">冻结绿点</option>
						<option value="10">解冻绿点</option>
						<option value="11">充值</option>
						<option value="12">提现</option>
						<option value="13">点券费用</option>
						<option value="14">车主邀请费</option>
						<option value="15">乘客邀请费</option>
						<option value="16">游戏发送绿点</option>
					</select> <a onclick="searchledger()"
						style="margin-left: 900px; width: 80px;" class="easyui-linkbutton"
						iconCls="icon-search">查 询</a>
				</div>
				<div id="toolbar">
					<label style="margin-left: 15px; font-size: 14px">余额：</label> <input
						id="balance" type="text" class="textbox"height:21px;"></input> <a
						onclick="outxlx()"
						style="border: green 1px solid; width: 100px; margin-left: 700px"
						class="easyui-linkbutton" iconCls="icon-xls">导出到excel</a>
				</div>
			</div>



			<div data-options="region:'center',title:''"
				style="border: #95B8E7 1px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
</body>
</html>
