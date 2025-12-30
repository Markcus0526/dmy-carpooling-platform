<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>查看用户信息</title>

<link rel="stylesheet" type="text/css"
	href="js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css" href="js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css" href="js/themes/icon.css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>


<script type="text/javascript" charset="UTF-8">

 $(function(){
	$.ajax({
		url : '<%=basePath%>customer/findUserAllInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				//console.info(jsonObject.active_as_driver_self);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#usercode').val(jsonObject.usercode);
				$('#nickname').val(jsonObject.nickname);
				$('#username').val(jsonObject.username);
				$('#phone').val(jsonObject.phone);
				if(jsonObject.sex==0){
				$('#sexSelect').combobox('setValues', '0');
				$('#person_sexSelect').combobox('setValues', '0');
				//$('#sex').val("男");
				//$('#person_sexSelect').val("男");
				}else if(jsonObject.sex==1){
				$('#sexSelect').combobox('setValues', '1');
				$('#person_sexSelect').combobox('setValues', '1');
				}
				$('#phone').val(jsonObject.phone);
				$('#img').attr('src',jsonObject.img);
				$('#remark').val(jsonObject.remark);
				//---------个人认证信息----------------
				$('#person_username').val(jsonObject.username);
				$('#nation').val(jsonObject.nation);
				$('#birthday').val(jsonObject.birthday);
				$('#id_card_num').val(jsonObject.id_card_num);
				$('#address').val(jsonObject.address);
				//------------身份认证图片---------------
				$('#id_card1').attr('src',jsonObject.id_card1);
				$('#id_card2').attr('src',jsonObject.id_card2);
				//------------车主认证信息-------------------------
				$('#drivinglicence_num').val(jsonObject.drivinglicence_num);
				$('#drlicence_ti').val(jsonObject.drlicence_ti);
				$('#brand').val(jsonObject.brand);
				$('#style').val(jsonObject.style);
				$('#color').val(jsonObject.color);
				$('#plate_num').val(jsonObject.plate_num);
				//$('#plate_num_last3').val(jsonObject.plate_num_last3);
				$('#vin').val(jsonObject.vin);
				$('#eno').val(jsonObject.eno);
				$('#vehicle_owner').val(jsonObject.vehicle_owner);
				if(jsonObject.is_oper_vehicle ==1){
				
					$('#oper_vehicle_Select').combobox('setValues', '1');
					}else if(jsonObject.is_oper_vehicle ==0){
					$('#oper_vehicle_Select').combobox('setValues', '0');
					}
				
				//--------车辆相关图片--------------
				$('#car_img').val(jsonObject.car_img);
				$('#driver_license1').attr('src',jsonObject.driver_license1);
				$('#driver_license2').attr('src',jsonObject.driver_license2);
				$('#driving_license1').attr('src',jsonObject.driving_license1);
				$('#driving_license2').attr('src',jsonObject.driving_license2);
				$('#uc_id').val(jsonObject.uc_id);
				$('#group_name').val(jsonObject.group_name);
				//---------返利信息相关--------------------
				$('#invitecode_self').val(jsonObject.invitecode_self);
				
				$('#limit_month_as_passenger_self').val(jsonObject.limit_month_as_passenger_self);
				$('#limit_month_as_driver_self').val(jsonObject.limit_month_as_driver_self);
				$('#limit_count_as_passenger_self').val(jsonObject.limit_count_as_passenger_self);
				$('#limit_count_as_driver_self').val(jsonObject.limit_count_as_driver_self);

				if(jsonObject.active_as_driver_self==0){
				$('#driverRatioSelect').combobox('setValues', '0');
				$('#driverRatioSelectInput').val(jsonObject.integer_as_driver_self);
				}else{
				$('#driverRatioSelect').combobox('setValues', '1');
				$('#driverRatioSelectInput').val(jsonObject.ratio_as_driver_self);
				}
				
				
				if(jsonObject.active_as_passenger_self==0){
				$('#passengerRatioSelect').combobox('setValues', '0');
				$('#passengerRatioSelectInput').val(jsonObject.integer_as_passenger_self);
				}else{
				$('#passengerRatioSelect').combobox('setValues', '1');
				$('#passengerRatioSelectInput').val(jsonObject.ratio_as_passenger_self);
				}
				//$('#active_as_driver_self').val(jsonObject.active_as_driver_self);
				//$('#ratio_as_passenger_self').val(jsonObject.ratio_as_passenger_self);
				//$('#integer_as_passenger_self').val(jsonObject.integer_as_passenger_self);
				
				//$('#active_as_passenger_self').val(jsonObject.active_as_passenger_self);
				//$('#ratio_as_driver_self').val(jsonObject.ratio_as_driver_self);
				//$('#integer_as_driver_self').val(jsonObject.integer_as_driver_self);
				
/* 				if(jsonObject.person_verified=1){
					$('#person_verified').val("待认证");
					}
				if(jsonObject.driver_verified=1){
					$('#driver_verified').val("待认证");
					} */
					
					
			}
		});
		
	$('#tt').tabs({
		border : false,
		onSelect : function(返利信息) {
		//console.info($('#invitecode_self').val()+"*-*/*/-*/-/-/-");
		rp();
			//alert("返利信息" + ' is selected');
		}
	});
	
	
		 $('#driverRatioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_drivr_self',
			data : [ {
				label : '点',
				value : '0'
			}, {
				label : '%',
				value : '1'
			} ]

		});
		 
	 $('#passengerRatioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_passenger_self',
			data : [ {
				label : '点',
				value : '0'
			}, {
				label : '%',
				value : '1'
			} ]

		}); 
		
	 $('#sexSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'sex',
			data : [ {
				label : '男',
				value : '0'
			}, {
				label : '女',
				value : '1'
			} ]

		}); 
		
	 $('#person_sexSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'sex',
			data : [ {
				label : '男',
				value : '0'
			}, {
				label : '女',
				value : '1'
			} ]

		}); 
		
		
	 $('#oper_vehicle_Select').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'is_oper_vehicle',
			data : [ {
				label : '非运营车辆',
				value : '0'
			}, {
				label : '运营车辆',
				value : '1'
			} ]

		}); 
		

	}); 
	

	function rp(){
	
		//var datagrid;
		//datagrid=
	$('#dg').datagrid({		
	
							url	:'<%=basePath%>customer/findAllRebateListInfo.do?invitecode_self='+$('#invitecode_self').val(),
							method : 'POST',
							title : '返利列表',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							//fit : true,
							//hight:600,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
							singleSelect:true,
							striped : true,
							columns : [ [
									
									 {
										field : 'id',
										title : '被邀请客户ID',
										width : 100,
									//checkbox : true
									}, {
										field : 'username',
										title : '被邀请客户姓名',
										width : 100
									}, {
										field : 'rpcount',
										title : '返利次数',
										width : 100,
										align : 'right'
									}, {
										field : 'rpsum',
										title : '累计返利',
										width : 100,
										align : 'right'
									}, {
										field : 'reg_date',
										title : '邀请时间',
										width : 100,
										align : 'right'
									}, {
										field : 'details',
										title : '返利详情',
										width : 100,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
  										str += '<a href="" onclick="showFLDG('+row.id+');return false">查看详情</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}
									] ],
							toolbar : '#toolbar'

						});
						

	
	}

	function showIdCardImg() {
		$("#dlg").dialog("open").dialog('setTitle', '查看身份认证照片');
		
		$("#idfm").form("clear");
	}
	function showdriverImg() {
		$("#diverImg").dialog("open").dialog('setTitle', '查看车主认证照片');
		
		$("#driverfm").form("clear");
	}
	function showFLDG(id) {
		//alert("-----"+id);
		
		$("#FLDG").dialog("open").dialog('setTitle', '查看返利详情');
		$("#FLDGfm").form("clear");
		$('#rebateForm').datagrid({		
	
							url	:'<%=basePath%>customer/findRebateListInfo.do?id='+id,
							method : 'POST',
							title : '返利列表',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							//fit : true,
							//hight:600,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : false,
							striped : true,
							columns : [ [
									
									 {
										field : 'username',
										title : '被邀请客户姓名',
										width : 100,
									}, {
										field : 'order_cs_id',
										title : '订单编号',
										width : 100
									}, {
										field : 'ts_date',
										title : '返利时间',
										width : 100,
										align : 'right'
									}, {
										field : 'identityStatus',
										title : '身份',
										width : 100,
										align : 'right'
									}, {
										field : 'sum',
										title : '返利金额',
										width : 100,
										align : 'right'
									}
									] ],
							toolbar : '#toolbar'

						});

	}
	
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
	
	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	function dategridTimeFormat(dateObj) {
		var h = dateObj.hours;
		var M = dateObj.minutes;
		var s = dateObj.seconds;
		var y = dateObj.year;
		if (y < 1900)
			y += 1900;
		var m = dateObj.month+1;
		var d = dateObj.date;
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);		
	}
</script>
</head>

<body onload="loaddone()">
	<div id="tt" class="easyui-tabs"
		style="width: 800px; height: 4500px; border: true;" fit=true>
		<div title="客户基本信息"
			style="width: 1200px; height: 700px; padding: 20px; border: true;">

			<table border=0>
				<tr>
					<td width="80px" align=left valign="middle"><font>I&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;D：</font></td>
					<td width="80px"><input type="text" id="id" name="id"
						disable="true"></td>
					<td width="80px" align=right valign="middle">头&nbsp;&nbsp;&nbsp;&nbsp;像：</td>
					<td rowspan=4 align=center valign="middle" width="80px"><img
						src="" id="img"></td>
				</tr>
				<tr align="left">
					<td>用户名称：</td>
					<td align=left><input type="text" id="usercode"
						name="usercode" disable="true" readOnly="readonly"></td>
				</tr>
				<tr align="left">
					<td>昵&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
					<td align=left><input type="text" id="nickname"
						name="nickname" readOnly="readonly"></td>
				</tr>
				<tr align="left">
					<td>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
					<td align=left><input type="text" id="username"
						name="username" readOnly="readonly"></td>
				</tr>
				<tr align="left">
					<td>手机号码：</td>
					<td valign="middle" align=left><input type="text" id="phone"
						name="phone"></td>
					<td align=right>性&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td align=right width="90px"><input type="text"
						id="sexSelect" name="sexSelect" readOnly="readonly"></td>
				</tr>
				<tr>
					<td>备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td valign="middle" colspan=3><textarea rows="2" cols="50"
							id="remark" name="remark" readOnly="readonly"></textarea></td>

				</tr>
			</table>


		</div>
		<div title="身份认证信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<table>
				<tr align="center">
					<td>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
					<td align=right><input type="text" id="person_username"
						name="person_username" readOnly="readonly"></td>
					<td>性&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td align=right><input type="text" id="person_sexSelect"
						name="person_sexSelect" readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td>民&nbsp;&nbsp;&nbsp;&nbsp;族：</td>
					<td align=right><input type="text" id="nation" name="nation"
						readOnly="readonly"></td>
					<td>出生日期：</td>
					<td align=right><input type="text" id="birthday"
						name="birthday" readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td>身份证号：</td>
					<td align=right><input type="text" id="id_card_num"
						name="id_card_num" readOnly="readonly"></td>
					<td>地&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
					<td align=right><input type="text" id="address" name="address"
						readOnly="readonly"></td>
				</tr>

			</table>

			<a id="btn" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="showIdCardImg()">查看身份认证照片</a>

			<div id="dlg" class="easyui-dialog"
				style="width: 400px; height: auto; padding: 10px 20px; top: 180px; left: 180px;"
				closed="true" buttons="#dlg-buttons">

				<form id="idfm" method="post">
					<table>
						<tr align=left>
							<td align=right><font>身份证正面：</font></td>
							<td align=center valign="middle"><img src="" id="id_card1"></td>
						</tr>
						<tr>
							<td align=right><font>身份证反面：</font></td>
							<td align=center valign="middle"><img src="" id="id_card2"></td>
						</tr>
					</table>
				</form>
			</div>


			<div id="dlg-buttons">
				<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveGroup()" iconcls="icon-save">保存</a>  -->
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dlg').dialog('close')"
					iconcls="icon-cancel">关闭</a>
			</div>
		</div>


		<div title="车主认证信息" data-options="iconCls:'icon-reload',closable:true"
			style="padding: 20px;">
			<table>


				<tr align="center">
					<td>驾照号码：</td>
					<td align=right><input type="text" id="drivinglicence_num"
						name="drivinglicence_num" readOnly="readonly"></td>
					<td>驾照获取时间：</td>
					<td align=right><input type="text" id="drlicence_ti"
						name="drlicence_ti" readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td>车辆品牌：</td>
					<td align=right><input type="text" id="brand" name="brand"
						readOnly="readonly"></td>
					<td>车辆型号：</td>
					<td align=right><input type="text" id="style" name="style"
						readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td>车辆型号：</td>
					<td align=right><input type="text" id="color" name="color"
						readOnly="readonly"></td>
					<td>车辆号牌：</td>
					<td align=right><input type="text" id="plate_num"
						name="plate_num" readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td>车&nbsp;架&nbsp;号：</td>
					<td align=right><input type="text" id="vin" name="vin"
						readOnly="readonly"></td>
					<td>发&nbsp;动机&nbsp;号：</td>
					<td align=right><input type="text" id="eno" name="eno"
						readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td>车&nbsp;主姓&nbsp;名：</td>
					<td align=right><input type="text" id="vehicle_owner"
						name="vehicle_owner" readOnly="readonly"></td>
					<td>使&nbsp;用性&nbsp;质：</td>
					<td align=right><input type="text" id="oper_vehicle_Select"
						name="oper_vehicle_Select" readOnly="readonly"></td>
				</tr>
			</table>

			<a id="driverbtn" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="showdriverImg()">查看车主照片</a>

			<div id="diverImg" class="easyui-dialog"
				style="width: 600px; height: auto; padding: 10px 20px; top: 30px; left: 200px;"
				closed="true" buttons="#diverImg-buttons">

				<form id="diverfm" method="post">
					<table>
						<tr align=right>
							<td align=center valign="middle"><font>驾驶证正面：</font></td>
							<td align=left><img src="" id="driver_license1"></td>
						</tr>
						<tr>
							<td align=center valign="middle"><font>驾驶证反面：</font></td>
							<td align=left><img src="" id="driver_license2"></td>
						</tr>
						<tr align=right>
							<td align=center valign="middle"><font>行驶证正面：</font></td>
							<td align=left><img src="" id="driving_license1"></td>
						</tr>
						<tr>
							<td align=center valign="middle"><font>行驶证反面：</font></td>
							<td align=left><img src="" id="driving_license2"></td>
						</tr>
						<tr align=right>
							<td align=center valign="middle"><font>车辆照片：</font></td>
							<td align=left><img src="" id="car_img"></td>
						</tr>

					</table>
				</form>
			</div>


			<div id="diverImg-buttons">
				<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveGroup()" iconcls="icon-save">保存</a>  -->
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#diverImg').dialog('close')"
					iconcls="icon-cancel">关闭</a>
			</div>

		</div>
		<div title="返利信息" data-options="iconCls:'icon-reload',closable:true"
			style="padding: 20px; hight: auto;">

			<table border=0>
				<tr>
					<td>邀&nbsp;&nbsp;请&nbsp;&nbsp;码：</td>
					<td align=right style="width: 100px; height: 15px;"><input
						id="invitecode_self" name="invitecode_self" readOnly="readonly"></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td>乘客方返利：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="driverRatioSelectInput" name="driverRatioSelectInput"
						style="width: 50px;"> <input id="driverRatioSelect"
						name="driverRatioSelect" style="width: 50px;"></td>
					<td>乘客方有效期：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="limit_month_as_passenger_self"
						name="limit_month_as_passenger_self" readOnly="readonly"
						style="width: 40px;">个月</td>
					<td>乘客方有效次数：</td>
					<td align=left><input id="limit_count_as_passenger_self"
						name="limit_count_as_passenger_self" readOnly="readonly"
						style="width: 40px;">次</td>
				</tr>
				<tr>
					<td>车主方返利：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="passengerRatioSelectInput" name="passengerRatioSelectInput"
						style="width: 50px;"> <input id="passengerRatioSelect"
						name="passengerRatioSelect" style="width: 50px;"></td>
					<td>车主方有效期：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="limit_month_as_driver_self" name="limit_month_as_driver_self"
						readOnly="readonly" style="width: 40px;">个月</td>
					<td>车主方有效次数：</td>
					<td align=left><input id="limit_count_as_driver_self"
						name="limit_count_as_driver_self" readOnly="readonly"
						style="width: 40px;">次</td>
				</tr>
				<tr>
					<td>返利&nbsp;总计：</td>
					<td>200次 &nbsp; 175点</td>
				</tr>
			</table>

			<table id="dg"></table>

			<div id="FLDG" class="easyui-dialog"
				style="width: 600px; height: 400px; padding: 10px 20px; top: 30px; left: 200px;"
				closed="true" buttons="#FLDG-buttons" resizable="true">

				<form id="FLDGfm" method="post">
					<table id="rebateForm"></table>
				</form>
			</div>
			<div id="FLDG-buttons">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#FLDG').dialog('close')"
					iconcls="icon-cancel">关闭</a>
			</div>
		</div>
	</div>
</body>
</html>
