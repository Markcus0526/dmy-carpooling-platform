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
<title>修改合作单位信息</title>
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
		url : '<%=basePath%>customer/findApp_Spread_UnitInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				console.info(jsonObject.id);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#unit_id').val(jsonObject.unit_id);
				$('#name').val(jsonObject.name);
				$('#create_date').val(jsonObject.create_date);
				$('#linkname').val(jsonObject.linkname);
				$('#group_property').val(jsonObject.group_property);
				$('#linkphone').val(jsonObject.linkphone);
				$('#contract_no').val(jsonObject.contract_no);
				$('#fix_phone').val(jsonObject.fix_phone);
				$('#email').val(jsonObject.email);
				$('#fax').val(jsonObject.fax);
				$('#group_address').val(jsonObject.group_address);
				$('#sign_time').val(jsonObject.sign_time);
				$('#invite_code').val(jsonObject.invite_code);
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
				$('#remark').val(jsonObject.remark);
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


	}); 

function updateApp_Spread_UnitInfo(){
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/updateApp_Spread_UnitInfo.do?id=${id}',
		   data:$('#App_Spread_UnitInfoForm').serialize(),
		   success: function(data){
 		   //console.info($('#App_Spread_UnitInfoForm').serialize());
		     //alert( "修改成功！ "  ); 
		   		$.messager.alert('提示','修改成功！','info',function(){
		    	window.history.go(-1);
		    });
		   }
		});

}
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
}
</script>
</head>

<body onload="loaddone()">
	<div id="tt" class="easyui-tabs" style="width: 800px; height: 450px;"
		fit=true>

		<div title="修改合作单位信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<form id="App_Spread_UnitInfoForm">
				<table>
					<tr align="center">
						<td align=left>合作单位标识：</td>
						<td align=left style="width: 100px; height: 15px;"><input
							type="text" id="unit_id" name="unit_id" readOnly="readonly">
						</td>
						<td align=left>合同编号：</td>
						<td align=left><input type="text" id="contract_no"
							name="contract_no"></td>
						<td></td>

					</tr>
					<tr align="center">

						<td align=left>合作单位名称：</td>
						<td align=left><input type="text" id="name" name="name">
						</td>
						<td align=left>签约时间：</td>
						<td align=left><input type="text" id="create_date"
							name="create_date"></td>
						<td></td>
					</tr>
					<tr align="center">
						<td align=left>合作单位性质：</td>
						<td align=left><input type="text" id="group_property"
							name="group_property"></td>
						<td align=left>联系电话：</td>
						<td align=left><input type="text" id="linkphone"
							name="linkphone"></td>
						<td></td>

					</tr>
					<tr align="center">
						<td align=left>联&nbsp;&nbsp;系&nbsp;&nbsp;人：</td>
						<td align=left><input type="text" id="linkname"
							name="linkname"></td>
						<td align=left>Email：</td>
						<td align=left><input type="text" id="email" name="email">
						</td>
						<td></td>
					</tr>
					<tr align="center">
						<td align=left>固定电话：</td>
						<td align=left><input type="text" id="fix_phone"
							name="fix_phone"></td>
						<td align=left>集团地址：</td>
						<td align=left><input type="text" id="group_address"
							name="group_address"></td>
						<td></td>
					</tr>
					<tr align="center">
						<td align=left>传真号码：</td>
						<td align=left><input type="text" id="fax" name="fax">
						</td>
						<td></td>
						<td></td>
						<!-- 					<td align=right>默认分成比例：<input type="text" id="radioSelectInput"
						name="radioSelectInput"><input id="radioSelect" name="radioSelect">
						
					</td> -->
					</tr>
					<tr align="center">
						<td align=left>邀&nbsp;&nbsp;请&nbsp;&nbsp;码：</td>
						<td align=left style="width: 100px; height: 15px;"><input
							type="text" id="invite_code" name="invite_code"></td>
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
							name="limit_month_as_passenger_self" style="width: 40px;">个月</td>
						<td>乘客方有效次数：</td>
						<td align=left><input id="limit_count_as_passenger_self"
							name="limit_count_as_passenger_self" style="width: 40px;">次</td>
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
							" style="width: 40px;">个月</td>
						<td>车主方有效次数：</td>
						<td align=left><input id="limit_count_as_driver_self"
							name="limit_count_as_driver_self" style="width: 40px;">次</td>
					</tr>
					<tr>
						<td align=left>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
						<td colspan=5><textarea id="remark" name="remark" rows="2"
								cols="75" wrap="hard"></textarea></td>

					</tr>
				</table>
			</form>
			<a id="save" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'"
				onclick="updateApp_Spread_UnitInfo()">保存</a> <a id="cancel"
				class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
				onclick="javascript:history.go(-1)">关闭</a>

		</div>
	</div>
</body>
</html>
