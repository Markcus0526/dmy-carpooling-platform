<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<style>
.left_labelstyle {
	width: 70px;
	text-align: right;
}

.right_labelstyle {
	width: 100px;
	text-align: right;
}
</style>

<div style="padding: 50px 30px 50px 30px">
	<s:form id="viewform" theme="simple">
		<table>
			<tr>
				<td class="left_labelstyle" style="height: 35px">用户名：</td>
				<td><s:textfield id="usercode" name="userInfo.usercode"
						cssClass="easyui-validatebox textbox" disabled="true"
						cssStyle="width:130px;">
					</s:textfield></td>
				<td class="right_labelstyle">所属机构：</td>
				<td><select id="unit" class="easyui-combobox"
					style="width: 130px;" name="userInfo.unitcode" disabled="disabled">
				</select></td>
			</tr>
			<tr>
				<td class="left_labelstyle" style="height: 35px">姓名：</td>
				<td><s:textfield id="username" name="userInfo.username"
						cssClass="easyui-validatebox textbox" disabled="true"
						data-options="required:true" cssStyle="width:130px;">
					</s:textfield></td>

				<td class="right_labelstyle">手机号码：</td>
				<td><s:textfield id="phone" name="userInfo.phoneNum"
						cssClass="easyui-validatebox textbox" disabled="true"
						data-options="required:true" cssStyle="width:130px;">
					</s:textfield></td>
			</tr>
			<tr>
				<td class="left_labelstyle" style="height: 35px">性别：</td>
				<td><s:select id="sex" name="userInfo.sex"
						cssClass="easyui-combobox" disabled="true" cssStyle="width:130px;"
						list="#{'男':'男','女':'女'}">
					</s:select></td>

				<td class="right_labelstyle">联系电话：</td>
				<td><s:textfield id="phone2" name="userInfo.phoneNum2"
						disabled="true" cssClass="easyui-validatebox textbox"
						cssStyle="width:130px;">
					</s:textfield></td>
			</tr>
			<tr>
				<td class="left_labelstyle" style="height: 35px">QQ：</td>
				<td><s:textfield id="qq" name="userInfo.qq" disabled="true"
						cssClass="easyui-validatebox textbox" cssStyle="width:130px;">
					</s:textfield></td>

				<td class="right_labelstyle">Email：</td>
				<td><s:textfield id="email" name="userInfo.email"
						cssClass="easyui-validatebox textbox" disabled="true"
						validType="email" cssStyle="width:130px;">
					</s:textfield></td>
			</tr>
			<tr>
				<td class="left_labelstyle">备注说明：</td>
				<td colspan="3"><s:textarea id="note" name="userInfo.note"
						disabled="true" cssStyle="width: 380px; height: 100px;">
					</s:textarea></td>
			</tr>
		</table>
	</s:form>
</div>

<script>

		viewUserDlg.dialog({
			title:'查看用户',
			width: 600,
			height: 445,
			modal:true,
			buttons: [{
				text:'关闭',
				iconCls:'icon-ok',
				handler:closeViewDlg
			}]
		});
		/** 	*/	
		$('#viewform #unit').combobox({
			data: unitList,
			valueField: 'code',
			textField: 'name'
		});
		$('#viewform #sex').combobox();
		dispInfo(userInfo);

		$('.easyui-linkbutton').linkbutton();
		$('.easyui-validatebox').validatebox();		

	function dispInfo(user_info) {
		$('#viewform #usercode').val(user_info.usercode);
		$('#viewform #username').val(user_info.username);
		$('#viewform #unit').select(user_info.unit);
		$('#viewform #sex').select(user_info.sex);
		$('#viewform #phone').val(user_info.phoneNum);
		$('#viewform #phone2').val(user_info.phoneNum2);
		$('#viewform #qq').val(user_info.qq);
		$('#viewform #email').val(user_info.email);
		$('#viewform #note').val(user_info.note);
	};

	function closeViewDlg() {
		viewUserDlg.dialog('close');
		viewUserDlg.html('');
	}
	
</script>

