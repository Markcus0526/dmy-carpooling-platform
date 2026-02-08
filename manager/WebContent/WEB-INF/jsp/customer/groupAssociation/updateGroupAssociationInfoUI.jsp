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

<title>修改集团信息</title>

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
				if(jsonObject.active_as_drivr_self==1){
				$('#radioSelect').val("点");
				$('#radioSelectInput').val(jsonObject.integer_as_driver_self);
				}else{
				$('#radioSelect').val("%");
				$('#radioSelectInput').val(jsonObject.ratio_as_driver_self);
				}
				$('#desc_').val(jsonObject.desc_);
			}
		});



	}); 

function updateGroupAssociationInfo(){
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/updateGroupAssociationInfo.do?id=${id}',
		   data:$('#groupAssociationInfoForm').serialize(),
		   success: function(data){
		    	$.messager.alert('提示','修改成功！','info',function(){
		    	window.history.go(-1);
		    });	
/*  		   console.info($('#groupAssociationInfoForm').serialize());
		     alert( "修改成功！ "  );  */
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

		<div title="修改集团信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<form id="groupAssociationInfoForm">
				<table border=0>
					<tr align="center">
						<td align=right>集团标识：</td>
						<td><input type="text" id="ga_code" name="ga_code"></td>
						<td align=right>合同编号：</td>
						<td><input type="text" id="contract_no" name="contract_no">
						</td>

					</tr>
					<tr align="center">

						<td align=right>集团名称：</td>
						<td><input type="text" id="ga_name" name="ga_name"></td>
						<td align=right>集团性质：</td>
						<td><input type="text" id="group_property"
							name="group_property"></td>
					</tr>
					<tr align="center">
						<td align=right>联系人：</td>
						<td><input type="text" id="linkname" name="linkname">
						</td>
						<td align=right>联系电话：</td>
						<td><input type="text" id="linkphone" name="linkphone">
						</td>

					</tr>
					<tr align="center">

						<td align=right>固定电话：</td>
						<td><input type="text" id="fix_phone" name="fix_phone">
						</td>
						<td align=right>Email：</td>
						<td><input type="text" id="email" name="email"></td>
					</tr>
					<tr align="center">
						<td align=right>传真号码：</td>
						<td><input type="text" id="fax" name="fax"></td>
						<td align=right>集团地址：</td>
						<td><input type="text" id="group_address"
							name="group_address"></td>
					</tr>
					<tr align="center">
						<td align=right>签约时间：</td>
						<td><input type="text" id="sign_time" name="sign_time">
						</td>
						<!-- 					<td align=right>默认分成比例：<input type="text" id="radioSelectInput"
						name="radioSelectInput"><input id="radioSelect" name="radioSelect">
						
					</td> -->
					</tr>
					<!-- 				<tr align="center">
					<td align=right>邀请码：<input type="text" id="invitecode_self" name="invitecode_self">
					</td>
				</tr> -->
					<tr>
						<td align=right>描述：</td>
						<td colspan=3><textarea rows="2" cols="50" id="desc_"
								name="desc_"></textarea> <!-- <input type="text" id="desc_" name="desc"> -->
						</td>

					</tr>
				</table>
			</form>
			<a id="save" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'"
				onclick="updateGroupAssociationInfo()">保存</a> <a id="cancel"
				class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"
				onclick="javascript:history.go(-1)">关闭</a>

		</div>
	</div>
</body>
</html>
