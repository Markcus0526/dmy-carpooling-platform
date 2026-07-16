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

<title>查看集团联盟信息</title>

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
		url : '<%=basePath%>customer/findGroupAssociationInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				console.info(jsonObject);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#ga_code').val(jsonObject.ga_code);
				$('#ga_name').val(jsonObject.ga_name);
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
				$('#invitecode_self').val(jsonObject.invitecode_self);
/* 				if(jsonObject.active_as_driver_self==0){
				$('#radioSelect').combobox('setValues', '0');
				$('#radioSelectInput').val(jsonObject.integer_as_driver_self);
				}else{
				$('#radioSelect').combobox('setValues', '1');
				$('#radioSelectInput').val(jsonObject.ratio_as_driver_self);
				} */
				$('#desc_').val(jsonObject.desc_);
			}
		});

/* 		radioSelect = $('#radioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_drivr_self',
			data : [ {
				label : '点',
				value : 'active_as_drivr_self'
			}, {
				label : '百分比',
				value : 'active_as_drivr_self'
			} ]

		}); */


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

		<div title="集团联盟信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<!-- 			<div style=" border-width: 1px; border-style: solid; border-color: blue;>-->
			<div>
				<table>
					<tr>
						<td align=left>集团标识：</td>
						<td><input type="text" id="ga_code" name="ga_code"></td>
						<td align=left>合同编号：</td>
						<td><input type="text" id="contract_no" name="contract_no">
						</td>

					</tr>
					<tr>

						<td align=left>集团名称：</td>
						<td><input type="text" id="ga_name" name="ga_name"></td>
						<td align=left>集团性质：</td>
						<td><input type="text" id="group_property"
							name="group_property"></td>
					</tr>
					<tr>
						<td align=left>联&nbsp;系&nbsp;人：</td>
						<td><input type="text" id="linkname" name="linkname">
						</td>
						<td align=left>联系电话：</td>
						<td><input type="text" id="linkphone" name="linkphone">
						</td>

					</tr>
					<tr>

						<td align=left>固定电话：</td>
						<td><input type="text" id="fix_phone" name="fix_phone">
						</td>
						<td align=left>Email：</td>
						<td><input type="text" id="email" name="email"></td>
					</tr>
					<tr>
						<td align=left>传真号码：</td>
						<td><input type="text" id="fax" name="fax"></td>
						<td align=left>集团地址：</td>
						<td><input type="text" id="group_address"
							name="group_address"></td>
					</tr>
					<tr>
						<td align=left>签约时间：</td>
						<td><input type="text" id="sign_time" name="sign_time">
						</td>
					</tr>
					<tr>
						<td align=left>描&nbsp;&nbsp;述：</td>
						<td colspan=3><textarea rows="2" cols="45" id="desc_"
								name="desc_"></textarea></td>

					</tr>
				</table>

			</div>
			<a id="cancelGroup" href="javascript:void(0)"
				class="easyui-linkbutton" onclick="javascript:history.go(-1);"
				iconcls="icon-cancel">返&nbsp;回</a>
		</div>
	</div>
</body>
</html>
