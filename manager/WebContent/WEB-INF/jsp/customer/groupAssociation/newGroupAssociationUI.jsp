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

<title>新建集团联盟用户</title>

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
function saveGroup(){
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/newGroupAssociation.do',
		   data:$('#groupAssociationInfoForm').serialize(),
		   success: function(data){
 		   console.info($('#groupAssociationInfoForm').serialize());
		     alert( "修改成功！ "  ); 
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

		<div title="新建集团信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<form id="groupAssociationInfoForm">
				<table>
					<tr align="center">
						<td align=right>集团标识：<input type="text" id="groupid"
							name="groupid">
						</td>
						<td align=right>合同编号：<input type="text" id="contract_no"
							name="contract_no">
						</td>

					</tr>
					<tr align="center">

						<td align=right>集团名称：<input type="text" id="group_name"
							name="group_name">
						</td>
						<td align=right>集团性质：<input type="text" id="group_property"
							name="group_property">
						</td>
					</tr>
					<tr align="center">
						<td align=right>联系人：<input type="text" id="linkname"
							name="linkname">
						</td>
						<td align=right>联系电话：<input type="text" id="linkphone"
							name="linkphone">
						</td>

					</tr>
					<tr align="center">

						<td align=right>固定电话：<input type="text" id="fix_phone"
							name="fix_phone">
						</td>
						<td align=right>Email：<input type="text" id="email"
							name="email">
						</td>
					</tr>
					<tr align="center">
						<td align=right>传真号码：<input type="text" id="fax" name="fax">
						</td>
						<td align=right>集团地址：<input type="text" id="group_address"
							name="group_address">
						</td>
					</tr>
					<tr align="center">
						<td align=right>签约时间：<input type="text" id="sign_time"
							name="sign_time">
						</td>
						<!-- 					<td align=right>默认分成比例：<input type="text" id="radioSelectInput"
						name="radioSelectInput"><input id="radioSelect" name="radioSelect">
						
					</td> -->
					</tr>
					<tr align="center">
						<td align=right>邀请码：<input type="text" id="invitecode_self"
							name="invitecode_self">
						</td>
					</tr>
					<tr>
						<td align=right>备注：<input type="text" id="remark"
							name="remark">
						</td>

					</tr>
				</table>
			</form>
			<a id="save" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'" onclick="saveGroup()">保存</a> <a
				id="cancel" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'"
				onclick="javascript:window.close()">关闭</a>

		</div>
	</div>
</body>
</html>
