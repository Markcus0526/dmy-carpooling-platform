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

<title>查看集团用户信息</title>

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
		url : '<%=basePath%>customer/findGroupInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				console.info(jsonObject.active_as_driver_self);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#groupid').val(jsonObject.groupid);
				$('#group_name').val(jsonObject.group_name);
				$('#create_date').val(jsonObject.create_date);
				$('#linkname').val(jsonObject.linkname);
				if(jsonObject.group_property == '0'){
					  $("#group_property option[value='0']").attr("selected",true);
				}else{
					  $("#group_property option[value='1']").attr("selected",true);
				}
				$('#linkphone').val(jsonObject.linkphone);
				$('#contract_no').val(jsonObject.contract_no);
				$('#fix_phone').val(jsonObject.fix_phone);
				$('#email').val(jsonObject.email);
				$('#fax').val(jsonObject.fax);
				$('#group_address').val(jsonObject.group_address);
				//$('#sign_time').val(jsonObject.sign_time);
				$('#sign_time').datebox('setValue', jsonObject.sign_time);
				$('#invitecode_self').val(jsonObject.invitecode_self);
				if(jsonObject.active_as_driver_self==0){
				$('#radioSelect').combobox('setValues', '0');
				$('#radioSelectInput').val(jsonObject.integer_as_driver_self);
				}else{
				$('#radioSelect').combobox('setValues', '1');
				$('#radioSelectInput').val(jsonObject.ratio_as_driver_self);
				}
				$('#remark').val(jsonObject.remark);
			}
		});

 		$('#radioSelect').combobox({
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


	}); 

 function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>

<body onload="loaddone()">
	<div id="tt" class="easyui-tabs" style="width: 800px; height: 450px;"
		fit=true>

		<div title="集团用户信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<table>
				<tr align="center">
					<td align=right>集&nbsp;团&nbsp;标&nbsp;识：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="groupid" name="groupid" readOnly="readonly"></td>
					<td align=right>合&nbsp;同&nbsp;编&nbsp;号：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="contract_no" name="contract_no" readOnly="readonly"></td>

				</tr>
				<tr align="center">

					<td align=right>集&nbsp;团&nbsp;名&nbsp;称：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="group_name" name="group_name" readOnly="readonly"></td>
					<td align=right>集&nbsp;团&nbsp;性&nbsp;质：</td>
					<td style="width: 100px; height: 15px;">
						  <select name="group_property" id="group_property">
						    	<option value="0" selected="true">集团车主</option>
						    	<option value="1">集团乘客</option>
					    	</select>
					</td>
				</tr>
				<tr align="center">
					<td align=right>联&nbsp;系&nbsp;人：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="linkname" name="linkname" readOnly="readonly"></td>
					<td align=right>联&nbsp;系&nbsp;电&nbsp;话：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="linkphone" name="linkphone" readOnly="readonly"></td>

				</tr>
				<tr align="center">

					<td align=right>固&nbsp;定&nbsp;电&nbsp;话：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="fix_phone" name="fix_phone" readOnly="readonly"></td>
					<td align=right>Email：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="email" name="email" readOnly="readonly"></td>
				</tr>
				<tr align="center">
					<td align=right>传&nbsp;真&nbsp;号&nbsp;码：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="fax" name="fax" readOnly="readonly"></td>
					<td align=right>集&nbsp;团&nbsp;地&nbsp;址：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="group_address" name="group_address" readOnly="readonly">
					</td>
				</tr>
				<tr align="center">
					<td align=right>签&nbsp;约&nbsp;时&nbsp;间：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="sign_time" name="sign_time" class="easyui-datebox"
						readOnly="readonly"></td>
					<td align=right>默认分成比例：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="radioSelectInput" name="radioSelectInput"
						style="width: 75px;"><input id="radioSelect"
						name="radioSelect" style="width: 75px;"></td>
				</tr>
				<tr align="center">
					<td align=right>邀&nbsp;请&nbsp;码：</td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="invitecode_self" name="invitecode_self" readOnly="readonly">
					</td>
				</tr>
				<tr>
					<td align=right><label> 备&nbsp;&nbsp;注：</label></td>
					<td colspan=3><textarea rows="2" cols="50" id="remark"
							name="remark" class="easyui-vlidatebox"></textarea></td>

				</tr>
			</table>
			<a id="cancelGroup" href="javascript:void(0)"
				class="easyui-linkbutton" onclick="javascript:history.go(-1);"
				iconcls="icon-cancel">返&nbsp;回</a>
		</div>
	</div>
</body>
</html>
