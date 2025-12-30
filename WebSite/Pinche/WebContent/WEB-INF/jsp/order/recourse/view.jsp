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
<style>
.left_labelstyle {
	width: 90px;
	text-align: right;
}

.right_labelstyle {
	width: 100px;
	text-align: right;
}
</style>
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
var page_path = '${pageContext.request.contextPath}/order/recourse/';
	$('.easyui-panel').panel();
	$('.easyui-linkbutton').linkbutton();
// 	$('.easyui-combobox').combobox();
	$('.easyui-combobox').combobox({listHeight:'40'});
	$('.easyui-tabs').tabs();
	$(function(){
		$.ajax({
			url: page_path+"findDetails.do?id=${id}",
			success:function(data){
			$('#id').val(data.id);
			$('#adm_id').val(data.adm_id);
			$('#workform_num').val(data.workform_num);
			$('#form_type').val(data.form_type);
			$('#bussi_type').val(data.bussi_type);
			$('#phone_incoming').val(data.phone_incoming);
			$('#order_cs_id').val(data.order_cs_id);
			$('#customer_name').val(data.customer_name);
			$('#sex').val(data.sex);
			$('#reason').val(data.reason);
			$('#process_result').val(data.process_result);
			var form_agree= data.form_agree;
			if(form_agree==0){
				document.getElementById("form_agree").checked=false;
			}if(form_agree==1){
				document.getElementById("form_agree").checked=true;
			}
			$('#status1').combobox('select',data.status);
			var order_cs_id=data.order_cs_id;
			if(order_cs_id==0){
				alert("没有值"+order_cs_id);
			}if(order_cs_id!=0){
				qeee(order_cs_id);
			}
			}
	
		});

	});
	function qeee(order_cs_id){
		$.ajax({
			url:page_path+'findOrder_num.do?order_cs_id='+order_cs_id,
			success:function(data){
				$('#id').val(data.id);
				$('#start_addr').val(data.start_addr);
				$('#order_city').val(data.order_city);
				$('#passagerphone').val(data.passagerphone);
				$('#passagercode').val(data.passagercode);
				$('#passagername').val(data.passagername);
				$('#driverphone').val(data.driverphone);
				$('#drivercode').val(data.drivercode);
				$('#drivername').val(data.drivername);
				$('#id').val(data.id);
				$('#begin_exec_time').val(data.begin_exec_time);
				$('#accept_time').val(data.accept_time);
				$('#price').val(data.price);
				$('#password').val(data.password);
				$('#end_addr').val(data.end_addr);
				  var	order_type=data.order_type;
					if(order_type==1){
						 var  str="单次拼车";
							$('#order_type').val(str);
					}if(order_type==2){
						 var  str="临时拼车";
							$('#order_type').val(str);
					}if(order_type==3){
						 var  str="上下班拼车";
							$('#order_type').val(str);
					}if(order_type==4){
						 var  str="长途拼车";
							$('#order_type').val(str);
					}
					$('#midpoint').val(data.midpoint);
					var  status=data.status;
					$('#status').combobox('select',status);
			},
		});
	}
	function backtoIndex()
	{
		window.location=page_path+"index.do";
	}
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>


</head>
<body onload="loaddone()">

	<div style="margin: 50px 100px;"></div>
	<div style="width: 1300px">
		<form id="processForm">
			<div style="float: left; width: 600px">
				<table style="width: 600px">
					<!-- <tr style="height: 50px;">
						<td class="left_labelstyle">座席员编号：</td>
						<td><input id="adm_id" name="adm_id" disabled="disabled" Class="textbox"/></td> 
						<td class="right_labelstyle">工单编号：</td>
						<td>
							<input id="workform_num"  name="workform_num" disabled="disabled" Class="textbox">
						</td>
					</tr> -->
					<tr style="height: 50px;">
						<td class="left_labelstyle">工单类型：</td>
						<td><select id="form_type" name="form_type"
							disabled="disabled" Class="easyui-combobox" Style="width: 133px;">
								<option value="1">订单情况查询</option>
								<option value="2">费用咨询</option>
								<option value="3">App使用咨询</option>
								<option value="4">综合咨询</option>
								<option value="5">投诉</option>
						</select></td>
						<td class="right_labelstyle">业务来源：</td>
						<td><select id="bussi_type" name="bussi_type"
							disabled="disabled" Class="easyui-combobox" Style="width: 133px;">
								<option value="1">拼车</option>
								<option value="2">代驾</option>
						</select></td>

					</tr>
					<tr style="height: 50px;">
						<td class="left_labelstyle">来电号码：</td>
						<td><input id="phone_incoming" name="phone_incoming"
							Class="easyui-validatebox textbox" Style="width: 133px;"></input>
						</td>
						<td class="right_labelstyle">订单编号：</td>
						<td><input id="order_cs_id" name="order_cs_id"
							disabled="disabled" Class="easyui-validatebox textbox"
							Style="width: 133px;"></input></td>
					</tr>
					<tr style="height: 50px;">
						<td class="left_labelstyle">客户姓名：</td>
						<td><input id="customer_name" name="customer_name"
							disabled="disabled" Class="easyui-validatebox textbox"
							Style="width: 133px;"></input></td>
						<td class="right_labelstyle">性别：</td>
						<td><select id="sex" name="sex" disabled="disabled"
							Class="easyui-combobox" Style="width: 133px;">
								<option value="1">先生</option>
								<option value="2">女士</option>
						</select></td>
					</tr>
				</table>

				<table>
					<tr>
						<td class="left_labelstyle">备注说明：</td>
						<td style="margin-left: 50px;">&nbsp; <textarea id="reason"
								name="reason" disabled="disabled"
								Style="width: 385px; height: 50px;"></textarea>
						</td>
					</tr>

					<tr>
						<td class="left_labelstyle">处理情况：</td>
						<td style="margin-left: 50px;">&nbsp; <textarea
								id="process_result" disabled="disabled" name="process_result"
								Style="width: 385px; height: 50px;"></textarea>
						</td>
					</tr>
					<tr>
						<td></td>
						<td><input id="form_agree" disabled="disabled"
							name="form_agree" type="checkbox" />:认定对方违约</td>
					</tr>

					<tr>
						<td class="left_labelstyle">处理状态：</td>
						<td><select id="status1" name="status1" disabled="disabled"
							Style="width: 133px;" Class="easyui-combobox">
								<option value="1">未处理</option>
								<option value="2">已处理</option>
								<option value="3">处理中</option>
						</select></td>
					</tr>
				</table>

				<div style="margin-left: 80px">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="backtoIndex();" style="width: 80px; margin-left: 50px">关闭</a>
				</div>
			</div>
		</form>
		<div class="easyui-tabs" style="float: right; width: 650px;">
			<div title="拼车订单" style="padding: 10px">
				<table>
					<tr style="height: 50px;">
						<td><label>订单编号：&nbsp;<input id="id" Class="textbox"
								disabled="disabled"></label></td>
						<td><label>订单类型：&nbsp;<input id="order_type"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>所在城市：&nbsp;<input id="order_city"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>下单时间：&nbsp;<input id="accept_time"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>执行时间：&nbsp;<input id="begin_exec_time"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>执行轨迹：&nbsp;<a href="javascript:void(0)"
								onclick="processsearch()" class="easyui-linkbutton l-btn"
								iconCls="icon-search" style="width: 131px">&nbsp;查询&nbsp;</a></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>乘客编号：&nbsp;<input id="passagercode"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>乘客姓名：&nbsp;<input id="passagername"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>乘客电话：&nbsp;<input id="passagerphone"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>车主编号：&nbsp;<input id="drivercode"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>车主姓名：&nbsp;<input id="drivername"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>车主电话：&nbsp;<input id="driverphone"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>出发地点：&nbsp;<input id="start_addr"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>目的地点：&nbsp;<input id="end_addr"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>中途点数：&nbsp;<input id="midpoint"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>拼车单价：&nbsp;<input id="price"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>电子车票：&nbsp;<input id="password" Class="textbox"
								disabled="disabled"></label></td>
						<td>订单状态：&nbsp; <select id="status" name="status"
							disabled="disabled" Class="easyui-combobox"
							Style="width: 150px; height: auto">
								<option value="1">发布</option>
								<option value="2">成交</option>
								<option value="3">开始执行</option>
								<option value="4">车主到达</option>
								<option value="5">乘客上车</option>
								<option value="6">执行结束</option>
								<option value="7">已结算</option>
								<option value="8">已关闭</option>
						</select>
						</td>
					</tr>
				</table>
			</div>
			<div title="代驾订单" style="padding: 10px"></div>
		</div>
	</div>

</body>
</html>


