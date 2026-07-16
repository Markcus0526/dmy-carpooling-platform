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

<div style="padding: 50px 30px 50px 30px;">
	<s:form id="addform" theme="simple">
		<table>
			<tr>
				<td class="left_labelstyle" style="height: 35px">用户名：</td>
				<td><s:textfield id="usercode" name="userInfo.usercode"
						cssStyle="width:130px;" cssClass="easyui-validatebox textbox"
						required="true" validType="text">
					</s:textfield></td>
				<td class="right_labelstyle">所属机构：</td>
				<td><select id="unit" class="easyui-combobox"
					style="width: 130px;" name="userInfo.unitcode">
				</select></td>
			</tr>
			<tr>
				<td class="left_labelstyle" style="height: 35px">姓名：</td>
				<td><s:textfield id="username" name="userInfo.username"
						cssClass="easyui-validatebox textbox" data-options="required:true"
						cssStyle="width:130px;">
					</s:textfield></td>

				<td class="right_labelstyle">手机号码：</td>
				<td><s:textfield id="phone" name="userInfo.phoneNum"
						cssClass="easyui-numberbox" cssStyle="width:130px;"
						data-options="required:true">
					</s:textfield></td>
			</tr>
			<tr>
				<td class="left_labelstyle" style="height: 35px">性别：</td>
				<td><s:select id="sex" name="userInfo.sex"
						cssClass="easyui-combobox" cssStyle="width:130px;"
						list="#{'男':'男','女':'女'}">
					</s:select></td>

				<td class="right_labelstyle">联系电话：</td>
				<td><s:textfield id="phone2" name="userInfo.phoneNum2"
						cssClass="easyui-validatebox textbox" cssStyle="width:130px;">
					</s:textfield></td>
			</tr>
			<tr>
				<td class="left_labelstyle" style="height: 35px">QQ：</td>
				<td><s:textfield id="qq" name="userInfo.qq"
						cssClass="easyui-validatebox textbox" cssStyle="width:130px;">
					</s:textfield></td>

				<td class="right_labelstyle">Email：</td>
				<td><s:textfield id="email" name="userInfo.email"
						cssClass="easyui-validatebox textbox" validType="email"
						cssStyle="width:130px;">
					</s:textfield></td>
			</tr>
			<tr>
				<td class="left_labelstyle">备注说明：</td>
				<td colspan="3"><s:textarea id="note" name="userInfo.note"
						cssStyle="width: 380px; height: 100px;"></s:textarea></td>
			</tr>
		</table>
	</s:form>
</div>

<script>
// 	$(function() {
		$('.easyui-linkbutton').linkbutton();
		$('.easyui-validatebox').validatebox();
		$('.easyui-numberbox').numberbox();
		$('#addform #unit').combobox({
			data: unitList,
			valueField: 'code',
			textField: 'name'
		});
		$('#addform #sex').combobox();
		
		addUserDlg.dialog({
			title:'添加用户',
			width: 600,
			height: 445,
			modal:true,
			buttons: [{
				text:'保存',
				iconCls:'icon-ok',
				handler:addManager,
			}, {
				text:'取消',
				iconCls:'icon-cancel',
				handler:closeAddDialog		
			}]
		});
// 	});
	
	function addManager(){
		if (validForm() == false)
			return;

		$('#addform').ajaxSubmit({
			url : page_path + "add.do?method=add",
			success: function(ret) {
				try {
	                if (ret.err_code == 0) {
	                	$('#content').load(page_path + "index");
	                	closeAddDialog();
	                } else {
	                    $.messager.alert('添加用户', ret.err_code + ': ' + ret.err_msg, 'error');
	                }
	            } finally {}
			},
			error: function() {
			},
			complete: function(){
			}
		});
	}
	
	function validForm() {
		
		var isValid_userCode = $('#addform #usercode').validatebox('isValid');
		var isValid_userName = $('#addform #username').validatebox('isValid');
		var isValid_phoneNum = $('#addform #phone').validatebox('isValid');
		var isValid_email = true;		
		
		if ($('#addform #email').val() != '') {
			isValid_email = $('#addform #email').validatebox('isValid');
		}		

		if (!isValid_userCode || !isValid_userName || !isValid_phoneNum || !isValid_email) {
			return false;
		}
		
		return true;
	}
	
	function closeAddDialog() {
		addUserDlg.dialog('close');
		addUserDlg.html("");
	}
	
//	showPath();
</script>

