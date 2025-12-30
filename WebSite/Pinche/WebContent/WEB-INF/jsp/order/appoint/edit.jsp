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
	var page_path = '${pageContext.request.contextPath}/order/appoint/';
	$('.easyui-combobox').combobox();
	$('.easyui-linkbutton').linkbutton();
    var choose="${choose}";
$(function(){
		$.ajax({
			url:page_path+'view.do?id=${id}',
			success:function(data){
				var order_type=data.order_type;
				if(order_type==1){
					$('#order_type').combobox('select',1);
				}if(order_type==2){
					$('#order_type').combobox('select',2);
				}if(order_type==3){
					$('#order_type').combobox('select',3);
				}if(order_type==4){
					
					$('#order_type').combobox('select',3);
				}
				var enableMoney=data.enableMoney;
				if(enableMoney==1){
					document.getElementById("enableMoney").checked="true";
				}{
				}
					$('#order_city').val(data.order_city);
					$('#driverId').val(data.driverid);
					$('#drivercode').val(data.drivercode);
					$('#drivername').val(data.drivername);
					$('#driverphone').val(data.driverphone);
					$('#total_sum').val(data.total_sum);
					$('#price').val(data.price);
					$('#reqcarstyle').combobox('select',data.reqcarstyle);
					$('#status').combobox('select',data.status);
					if(data.order_type!=3){
					$('#passagerId').val(data.passagerid);
					$('#passagercode').val(data.passagercode);
					$('#passagername').val(data.passagername);
					$('#passagerphone').val(data.passagerphone);
					}
					$('#password').val(data.password);
					$('#beginservice_time').datetimebox('setValue', data.beginservice_time);
						var ispayed=data.is_payed;
						if(ispayed==1){
							   document.getElementById("isPayed1").checked = "checked";
						}if(ispayed==2){
							   document.getElementById("isPayed2").checked = "checked";
						}
						var isCatchedCar=data.iscatchecar;
						alert(isCatchedCar);
						if(isCatchedCar==1){
							alert("000011110000");
							   document.getElementById("isCatchedCar1").checked = "checked";
						}if(isCatchedCar==2){
							   document.getElementById("isCatchedCar2").checked = "checked";
						}
					$('#pay_time').datetimebox('setValue',data.pay_time);
					$('#start_addr').val(data.start_addr);
					$('#end_addr').val(data.end_addr);
					$('#order_num').val(data.order_num);
					$('#reqcarstyle').val(data.reqcarstyle);
					$('#psgerRemark').val(data.psgerRemark);
					$('#driyerremark').val(data.driyerremark);
					//$('#weekdays').val(data.weekdays);
					$('#orderType').val(data.order_type);
					$('#detailsId').val(data.detailsId);
					$('#addr4midpoint1').val(data.addr4midpoint1);
					$('#addr4midpoint2').val(data.addr4midpoint2);
					$('#addr4midpoint3').val(data.addr4midpoint3);
					$('#addr4midpoint4').val(data.addr4midpoint4);
					$('#ps_date').datetimebox('setValue',data.ps_date);
					$('#accept_time').datetimebox('setValue',data.accept_time);
					$('#begin_exec_time').datetimebox('setValue',data.begin_exec_time);
					$('#driverarrival_time').datetimebox('setValue',data.driverarrival_time);
					$('#stopservice_time').datetimebox('setValue',data.stopservice_time);
				
					$('#pre_time').datetimebox('setValue',data.pre_time);
					$('#isBreaked').val(data.isbreaked);
					$('#status').combobox("select",data.status);
			}
	 
		});
	
		datagrid2();
	});
	
   function datagrid2(){
		$('#dg').datagrid({
	         url: page_path + 'showEditHistory.do?' + 'orderExecId=' + $('#orderExecId').val(),
	         loadMsg : '数据处理中，请稍后....',
	         height: 'auto',
	         width:'1100px',
	         pagination: true,
	         singleSelect:true,
	         fitColumns: true,	         	
			columns:[[
	             {field:'modifier',width:100,align:'center',title:'修改人'}, 
	             {field:'md_time',width:200,align:'center',title:'修改时间',
	            	 formatter:function(value,rowData,rowIndex){
	            		 if(value!=null&&value!=""){
		            		  var JsonDateValue = new Date(value.time);
		            		  var text = JsonDateValue.toLocaleString(); 
		            		                return text;
		            		 }else{
			            		 return "";
			            	 }
	            		         }
	             },
	             {field:'md_column',width:150,align:'center',title:'修改字段'},
	             {field:'old_value',width:150,align:'center',title:'原内容'},
	             {field:'new_value',width:150,align:'center',title:'新内容'},
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
	
	
	
	function closeEdit() {
	 	alert(choose);
		if(choose==1){
			var page_path = '${pageContext.request.contextPath}/order/appoint/';
			var status=$('#status').combobox('getValue');
			alert(page_path);
			window.location=page_path +"index.do?status="+status;
		}
		if(choose==2){
			var page_path = '${pageContext.request.contextPath}/order/appointdetail/';
			alert(page_path);
			window.location=page_path + "index.do?status="+1;
		} 
	}
	
	function returnMoney() {
		alert("退还");
		if ($('#status').combobox('getValue') != 8){
			alert("status!=8");
			return ;
		}
		$.messager.defaults = {
			ok : "确  定",
			cancel : "取  消"
		};
		
		$.messager.confirm(
			'Confirm',
			'确定退还乘客 '+ $('#passengerName').val() + '绿点' + '【' + $('#price').val() + '】吗？\n'			
			+ '该操作不可撤销',
			function(r) {
				if (r) {					
					$.ajax({
						url : page_path + 'cancelFreezePoint.do?orderExecId=' + $('#orderExecId').val(),
						success : function (ret) {
							if (ret.err_code == 0) {
								$("#status").combobox('setValue',8);
								$('#status').combobox('select:newValue');
							} else {
								$.messager.alert('Confirm', ret.err_msg, 'error');
							}							
						},
					});
				}
			});
	}
		
	function changeStatus(newValue, oldValue) {
		if ((newValue < oldValue) || (oldValue == 4 && newValue != 8)) {
			$.messager.alert('Info', 'Cannot Change');
			$("#order_status").combobox('setValue',oldValue);
			alert(0);
			$('#order_status').combobox('select:oldValue');			
		}				
	}
	if ($('#isBreaked').val() == 1) {		
		$('#cancelFreeze').addClass('l-btn-disabled');			
	}
	
	function onChangeCatchCar() {
		if ($("#isCatchedCar1").get(0).checked == false) {
			$('#beginservice_time').val('');
			$("#isPayed2").get(0).checked = true;
			$('#pay_time').val('');
		} else {
			if (!$('#beginservice_time').val())
				$('#beginservice_time').val(Dateformat(new Date()));
			$("#isPayed2").get(0).checked = true;
			$('#pay_time').val('');
		}
	}
	
	function onChangePay() {
		if ($("#isPayed1").get(0).checked == false) {
			$('#pay_time').datetimebox('getValue');			
		} else {
			if (!$('#pay_time').datetimebox('getValue'))
				$('#pay_time').val(Dateformat(new Date()));		
			$("#isCatchedCar1").get(0).checked = true;
			if (!$('#beginservice_time').datetimebox('getValue'))
				$('#beginservice_time').val(Dateformat(new Date()));
		}
	}
	
	function Dateformat(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
	}
	
	var oldRecord;
	var changeCnt = 0;
	function selectStatus(newRecord) {
		changeCnt++;
		if (changeCnt == 1) {
			if (newRecord.value < oldRecord.value || (newRecord.value != 8 && oldRecord.value == 4)) {
				$.messager.alert('Warning', "Can't change status", 'info');
				$('#status').combobox('select', oldRecord.value);
			}
		} else if (changeCnt == 2) {
			return;
		}
		
		if (changeCnt == 1) {
			oldRecord = newRecord;
		}
		changeCnt = 0;
		
		if ($('#order_status').combobox('getValue') == 8) {
			$('#cancelFreeze').removeClass('l-btn-disabled');	
		}
	}
	function saveEdit(editform) {
		  msg='确认保存？';
		   if(window.confirm(msg)) {
		   return true;
		   }else{
			   return false;
		   }
		}
	function operat(id){
		var page_path = '${pageContext.request.contextPath}/order/appoint/';
		window.location=page_path + "edit1.do?id="+id;
	}
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>

</head>
<body onload="loaddone()">

	<div id="appoint_tab">
		<form id="editform"
			action="${pageContext.request.contextPath}/order/appoint/edit.do"
			method="post" onsubmit="return saveEdit()">
			<input type="hidden" id="passagerId" name="passagerId" /> <input
				type="hidden" id="driverId" name="driverId" /> <input type="hidden"
				id="orderExecId" name="orderExecId" value="${id}" /> <input
				type="hidden" id="isBreaked" name="isBreaked" /> <input
				type="hidden" id="orderType" name="orderType" /> <input
				type="hidden" id="id" name="id" value="${id}" /> <input
				type="hidden" name="temp_price" />
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
			<% if(request.getAttribute("order_type")!=(Integer)3){ %>
			<div style="margin-top: 10px; margin-left: 50px">
				<span>乘客ID:</span> <input id="passagercode" name="passagerCode"
					disabled="disabled" Class="textbox _textbox" style="width: 150px">
				<span style="margin-left: 20px">乘客姓名:</span> <input
					id="passagername" name="passagerName" disabled="disabled"
					Class="textbox _textbox" style="width: 150px"> <span
					style="margin-left: 30px">乘客电话:</span> <input id="passagerphone"
					name="passagerPhone" disabled="disabled" Class="textbox _textbox"
					style="width: 150px"> <span style="margin-left: 30px">乘客支付:</span>
				<input id="price" name="price" disabled="disabled"
					Class="textbox _textbox" style="width: 150px">

			</div>
			<%} %>
			<div style="margin-top: 10px; margin-left: 50px">
				<% if(request.getAttribute("order_type")==(Integer)1){ %>
				<input type="checkbox" id="enableMoney" name="enableMoney"
					disabled="disabled" Style="margin-left: 10px" /> <label
					for="enableMoney">乘客的绿点已冻结</label>
				<%} %>
				<span style="margin-left: 70px">电子密码:</span> <input id="password"
					name="password" Class="textbox _textbox" style="width: 150px">
				<% if(request.getAttribute("order_type")==(Integer)1){ %>
				<a href="javascript:void(0)" id="cancelFreeze"
					style="margin-left: 60px" class="easyui-linkbutton l-btn"
					onclick="returnMoney();">退还乘客1冻结的绿点</a>
				<%} %>
			</div>
			<div style="margin-top: 10px; margin-left: 35px">
				<span>订单起点:</span> <input id="start_addr" name="start_addr"
					Class="textbox _textbox" style="width: 150px"> <span
					style="margin-left: 20px">订单终点:</span> <input id="end_addr"
					name="end_addr" Class="textbox _textbox" style="width: 150px">
			</div>
			<div style="margin-top: 10px; margin-left: 42px">
				<span>中途点1:</span> <input id="addr4midpoint1" name="addr4midpoint1"
					Class="textbox _textbox" style="width: 150px"> <span
					style="margin-left: 30px">中途点2:</span> <input id="addr4midpoint2"
					name="addr4midpoint2" Class="textbox _textbox" style="width: 150px">
				<span style="margin-left: 37px">中途点3:</span> <input
					id="addr4midpoint3" name="addr4midpoint3" Class="textbox _textbox"
					style="width: 150px"> <span style="margin-left: 38px">中途点4:</span>
				<input id="addr4midpoint4" name="addr4midpoint4"
					Class="textbox _textbox" style="width: 150px">
			</div>
			<div style="margin-top: 10px; margin-left: 35px">
				<span>发布时间:</span> <input id="ps_date" name="ps_date"
					Class="easyui-datetimebox" style="width: 150px"> <span
					style="margin-left: 20px">成交时间:</span> <input id="accept_time"
					name="accept_time" Class="easyui-datetimebox" style="width: 150px">
				<span style="margin-left: 31px">开始执行:</span> <input
					id="begin_exec_time" name="begin_exec_time"
					Class="easyui-datetimebox" style="width: 150px">
			</div>
			<div style="margin-top: 10px; margin-left: 35px">
				<span>车主到达:</span> <input id="driverarrival_time"
					name="driverarrival_time" Class="easyui-datetimebox"
					style="width: 150px"> <span style="margin-left: 20px">结束服务:</span>
				<input id="stopservice_time" name="stopservice_time"
					Class="easyui-datetimebox" style="width: 150px"> <span
					style="margin-left: 32px">预约出发:</span> <input id="pre_time"
					name="pre_time" Class="easyui-datetimebox" style="width: 150px">
				<span style="margin-left: 30px">当前状态:</span> 
				<select id="status"
					name="status" Class="easyui-combobox"
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
			<div style="margin-top: 10px; margin-left: 18px">
				<span>乘客已上车:</span> <input id="isCatchedCar1" type="radio"
					name="isCatchedCar" value="1">是<input id="isCatchedCar2"
					type="radio" name="isCatchedCar" value="2">否 <span
					style="margin-left: 94px">乘客上车:</span> <input
					id="beginservice_time" name="beginservice_time"
					Class="easyui-datetimebox" style="width: 150px"> <span
					style="margin-left: 30px">乘客已支付:</span> <input id="isPayed1"
					type="radio" name="isPayed" value="1">是<input
					id="isPayed2" type="radio" name="isPayed" value="2">否 <span
					style="margin-left: 56px">乘客支付时间:</span> <input id="pay_time"
					name="pay_time" Class="easyui-datetimebox" style="width: 150px">
			</div>
			<div style="margin-top: 20px; margin-left: 35px; margin-bottom: 25px">
				<span>需求车型:</span> <select id="reqcarstyle" name="reqcarstyle"
					Class="easyui-combobox" Style="width: 150px; height: auto">
							<option value="0">无</option>
					<option value="1">经济型</option>
					<option value="2">舒适性</option>
					<option value="3">豪华型</option>
					<option value="4">商务型</option>
				</select> <span style="margin-left: 20px">乘客备注:</span> <input
					id="psgerRemark" name="psgerRemark" Class="textbox _textbox"
					style="width: 150px" /> <span style="margin-left: 33px">
					车主备注: </span> <input id="driyerremark" name="driyerremark"
					Class="textbox _textbox" style="width: 150px">
			</div>
			<div style="margin-left: 10px; margin-bottom: 10px">
				<input type="submit"
					style="margin-left: 10px; width: 100px; hight: 20px"
					value="保&nbsp;&nbsp;存"> <a href="javascript:void(0)"
					style="margin-left: 10px; width: 100px"
					onclick="javascript:closeEdit();" class="easyui-linkbutton l-btn">取&nbsp;&nbsp;消</a>
			</div>
		</form>
		<div style="margin-left: 10px">
			<h2 style="margin-bottom: 0px;">&nbsp;修改记录</h2>
			<table id="dg" style="width: 700px; height: auto"
				data-options="rownumbers:true,singleSelect:false">
			</table>
		</div>
	</div>

</body>
</html>


