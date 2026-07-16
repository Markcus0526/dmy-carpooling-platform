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
// 	$('.easyui-combobox').combobox();
	function closeView() {
		window.close();
	}
		$(function(){
		$.ajax({
			url: page_path+"viewInDetail.do?id=${id}",
			success:function(data){
			$('#id').val(data.id);
			var order_type=data.order_type;
			if(order_type==1){
				$('#order_type').combobox('select',1);
			}if(order_type==2){
				$('#order_type').combobox('select',2);
			}if(order_type==3){
				$('#order_type').combobox('select',2);
			}if(order_type==4){
				$('#order_type').combobox('select',3);
			}
			var enableMoney=data.enableMoney;
			if(enableMoney==1){
				document.getElementById("enableMoney").checked="true";
			}{
			}
			$('#order_city').val(data.order_city);
			$('#driverid').val(data.driverid);
			$('#drivercode').val(data.drivercode);
			$('#drivername').val(data.drivername);
			$('#driverphone').val(data.driverphone);
			$('#total_sum').val(data.total_sum);
			$('#price').val(data.price);
			$('#passagerid').val(data.passagerid);
			$('#passagercode').val(data.passagercode);
			$('#passagername').val(data.passagername);
			$('#passagerphone').val(data.passagerphone);
			$('#beginservice_time').val(data.beginservice_time);
			$('#password').val(data.password);
			var ispayed=data.is_payed;
			if(ispayed==1){
				   document.getElementById("isPayed1").checked = "checked";
			}if(ispayed==2){
				   document.getElementById("isPayed2").checked = "checked";
			}
			var isCatchedCar=data.iscatchecar;
			if(isCatchedCar==1){
				   document.getElementById("isCatchedCar1").checked = "checked";
			}if(isCatchedCar==2){
				   document.getElementById("isCatchedCar2").checked = "checked";
			}
			$('#pay_time').val(data.pay_time);
			$('#start_addr').val(data.start_addr);
			$('#end_addr').val(data.end_addr);
			$('#order_num').val(data.order_num);
			
			$('#psgerRemark').val(data.psgerRemark);
			$('#driyerremark').val(data.driyerremark);
			//$('#weekdays').val(data.weekdays);
			$('#orderType').val(data.orderType);
			$('#detailsId').val(data.detailsId);
			$('#addr4midpoint1').val(data.addr4midpoint1);
			$('#addr4midpoint2').val(data.addr4midpoint2);
			$('#addr4midpoint3').val(data.addr4midpoint3);
			$('#addr4midpoint4').val(data.addr4midpoint4);
			$('#ps_date').val(data.ps_date);
			$('#accept_time').val(data.accept_time);
			$('#begin_exec_time').val(data.begin_exec_time);
			$('#driverarrival_time').val(data.driverarrival_time);
			$('#stopservice_time').val(data.stopservice_time);
			$('#pre_time').val(data.pre_time);
			$('#status').combobox("select",data.status);
			$('#reqcarstyle').combobox("select",data.reqcarstyle);
			}
		});
    

	});
   function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>

</head>
<body onload="loaddone()">

	<div id="appoint_tab">
		<div style="margin-top: 60px; margin-left: 35px">
			<span style="width: 80px">订单编号:</span> <input id="order_num"
				name="order_num" disabled="disabled" Class="textbox _textbox"
				style="width: 150px"> <span style="margin-left: 20px">订单类型:</span>
			<select id="order_type" name="order_type" disabled="disabled"
				Class="easyui-combobox" Style="width: 150px; height: auto">
				<option value="1">单次拼车</option>
				<option value="2">上下班拼车</option>
				<option value="3">长途拼车</option>
			</select> <span style="margin-left: 30px">所在城市:</span> <input id="order_city"
				name="order_city" disabled="disabled" Class="textbox _textbox"
				style="width: 150px">
		</div>
		<div style="margin-top: 10px; margin-left: 50px">
			<label>车主ID:</label> <input id="drivercode" name="driverCode"
				disabled="disabled" Class="textbox _textbox" style="width: 150px">
			<span style="margin-left: 20px">车主姓名:</span> <input id="drivername"
				name="driverName" disabled="disabled" Class="textbox _textbox"
				style="width: 150px"> <span style="margin-left: 30px">车主电话:</span>
			<input id="driverphone" name="driverPhone" disabled="disabled"
				Class="textbox _textbox" style="width: 150px"> <span
				style="margin-left: 30px">订单总价:</span> <input id="total_sum"
				name="total_sum" disabled="disabled" Class="textbox _textbox"
				style="width: 150px">
		</div>
		<div style="margin-top: 10px; margin-left: 50px">
			<span>乘客ID:</span> <input id="passagercode" name="passagerCode"
				disabled="disabled" Class="textbox _textbox" style="width: 150px">
			<span style="margin-left: 20px">乘客姓名:</span> <input id="passagername"
				name="passagerName" disabled="disabled" Class="textbox _textbox"
				style="width: 150px"> <span style="margin-left: 30px">乘客电话:</span>
			<input id="passagerphone" name="passagerPhone" disabled="disabled"
				Class="textbox _textbox" style="width: 150px"> <span
				style="margin-left: 30px">乘客支付:</span> <input id="price"
				name="price" disabled="disabled" Class="textbox _textbox"
				style="width: 150px">
		</div>
		<div style="margin-top: 10px; margin-left: 50px">
			<input type="checkbox" id="enableMoney" name="enableMoney"
				disabled="disabled" Style="margin-left: 30px" /> <label
				for="enableMoney">乘客的绿点已冻结</label> <span style="margin-left: 70px">电子密码:</span>
			<input id="password" name="password" disabled="disabled"
				Class="textbox _textbox" style="width: 150px">
		</div>
		<div style="margin-top: 10px; margin-left: 35px">
			<span>订单起点:</span> <input id="start_addr" name="start_addr"
				disabled="disabled" Class="textbox _textbox" style="width: 150px">
			<span style="margin-left: 20px">订单终点:</span> <input id="end_addr"
				name="end_addr" disabled="disabled" Class="textbox _textbox"
				style="width: 150px">
		</div>
		<div style="margin-top: 10px; margin-left: 42px">
			<span>中途点1:</span> <input id="addr4midpoint1" name="addr4midpoint1"
				disabled="disabled" Class="textbox _textbox" style="width: 150px">
			<span style="margin-left: 30px">中途点2:</span> <input
				id="addr4midpoint2" name="addr4midpoint2" disabled="disabled"
				Class="textbox _textbox" style="width: 150px"> <span
				style="margin-left: 37px">中途点3:</span> <input id="addr4midpoint3"
				name="addr4midpoint3" disabled="disabled" Class="textbox _textbox"
				style="width: 150px"> <span style="margin-left: 38px">中途点4:</span>
			<input id="addr4midpoint4" name="addr4midpoint4" disabled="disabled"
				Class="textbox _textbox" style="width: 150px">
		</div>

		<div style="margin-top: 10px; margin-left: 35px">
			<span>发布时间:</span> <input id="ps_date" name="ps_date"
				disabled="disabled" Class="textbox _textbox" style="width: 150px">
			<span style="margin-left: 20px">成交时间:</span> <input id="accept_time"
				name="accept_time" disabled="disabled" Class="textbox _textbox"
				style="width: 150px"> <span style="margin-left: 31px">开始执行:</span>
			<input id="begin_exec_time" name="begin_exec_time"
				disabled="disabled" Class="textbox _textbox" style="width: 150px">
		</div>
				<div style="margin-top: 10px; margin-left: 40px">
			<span>乘客已上车:</span> <input id="isCatchedCar1" type="radio"
				name="isCatchedCar2" disabled="disabled" value="1">是<input
				id="isCatchedCar2" type="radio" name="isCatchedCar2"
				disabled="disabled" value="2">否 <span
				style="margin-left: 94px">乘客上车:</span> <input id="beginservice_time"
				name="beginservice_time" disabled="disabled"
				Class="textbox _textbox" style="width: 150px"> <span
				style="margin-left: 30px">乘客已支付:</span> <input id="isPayed1"
				type="radio" name="isPayed1" value="1" disabled="disabled">是<input
				id="isPayed2" type="radio" name="isPayed2" disabled="disabled"
				value="2">否 <span style="margin-left: 56px">乘客支付时间:</span> <input
				id="pay_time" name="pay_time" disabled="disabled"
				Class="textbox _textbox" style="width: 150px">
		</div>
		<div style="margin-top: 10px; margin-left: 35px">
			<span>车主到达:</span> <input id="driverarrival_time"
				name="driverarrival_time" disabled="disabled"
				Class="textbox _textbox" style="width: 150px"> <span
				style="margin-left: 20px">结束服务:</span> <input id="stopservice_time"
				name="stopservice_time" disabled="disabled" Class="textbox _textbox"
				style="width: 150px"> <span style="margin-left: 32px">预约出发:</span>
			<input id="pre_time" name="pre_time" disabled="disabled"
				Class="textbox _textbox" style="width: 150px"> <span
				style="margin-left: 30px">当前状态:</span> <select id="status"
				name="status" disabled="disabled" Class="easyui-combobox"
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
		</div>

		<div style="margin-top: 20px; margin-left: 35px; margin-bottom: 25px">
			<span>需求车型:</span> <select id="reqcarstyle" name="reqcarstyle"
				disabled="disabled" Class="easyui-combobox"
				Style="width: 150px; height: auto">
				<option value="1">经济型</option>
				<option value="2">舒适性</option>
				<option value="3">豪华型</option>
				<option value="4">商务型</option>
			</select> <span style="margin-left: 20px">乘客备注:</span> <input id="psgerRemark"
				name="psgerRemark" disabled="disabled" Class="textbox _textbox"
				style="width: 150px" /> <span style="margin-left: 33px">车主备注:
			</span> <input id="driyerremark" name="driyerremark" disabled="disabled"
				Class="textbox _textbox" style="width: 150px">

		</div>
		<div style="padding: 20px">
			<a href="javascript:void(0)" style="margin-left: 10px; width: 100px"
				onclick="checkTrace();" class="easyui-linkbutton l-btn">查看执行轨迹</a>
		</div>
		<div style="margin-left: 20px; margin-bottom: 10px">
			<a href="javascript:void(0)" style="margin-left: 10px; width: 100px"
				onclick="closeView();" class="easyui-linkbutton l-btn">取&nbsp;&nbsp;消</a>
		</div>
	</div>
</body>
</html>

	