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
	<s:form id="editform" theme="simple">
		<s:hidden id="id" name="userInfo.id" />
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
						cssClass="easyui-validatebox textbox" cssStyle="width:130px;"
						data-options="required:true">
					</s:textfield></td>

				<td class="right_labelstyle">手机号码：</td>
				<td><s:textfield id="phone" name="userInfo.phoneNum"
						cssClass="easyui-validatebox textbox" cssStyle="width:130px;"
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
						cssStyle="width:130px;" cssClass="easyui-validatebox textbox">
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
		editUserDlg.dialog({
			title:'修改用户信息',
			width: 600,
			height: 445,
			modal:true,
			buttons: [{
				text:'保存',
				iconCls:'icon-ok',
				handler:saveInfo,
			}, {
				text:'取消',
				iconCls:'icon-cancel',
				handler:closeEditDlg
			}]
		});

		$('#editform #unit').combobox({
			data: unitList,
			valueField: 'code',
			textField: 'name'
		});

		$('#editform #sex').combobox();
		dispInfo(userInfo);

		$('.easyui-validatebox').validatebox();
		$('.easyui-linkbutton').linkbutton();
// 	});

	function dispInfo(user_info) {
		$('#editform #id').val(user_info.id);
		$('#editform #usercode').val(user_info.usercode);
		$('#editform #username').val(user_info.username);
		$('#editform #unit').combobox('select', user_info.unit);
		$('#editform #sex').combobox('select', user_info.sex);
		$('#editform #phone').val(user_info.phoneNum);
		$('#editform #phone2').val(user_info.phoneNum2);
		$('#editform #qq').val(user_info.qq);
		$('#editform #email').val(user_info.email);
		$('#editform #note').val(user_info.note);
	};

	function closeEditDlg() {
		editUserDlg.dialog('close');
		editUserDlg.html("");
	}

	function saveInfo(){

		if (validEditForm() == 0)
			return;

		$('#editform').ajaxSubmit({
			url: page_path + 'edit_info.do?method=save&id=' + $('#editform #id').val(),
			success: function(result) {
				if (result.err_code == 0) {
					$.messager.show({
						title : '提示',
						msg : '修改用户信息成功。',
						showType:'fade',
						style:{
							right:'',
							bottom:''
						}
					});
					closeEditDlg();
				} else {
					$.messager.alert('修改用户信息', result.err_code + ': ' + result.err_msg, 'error');
				}
			},
			error: function() {

			},
			complete: function() {

			}
		});
	}

	function validEditForm() {
		var isValid_userCode = $('#editform #usercode').validatebox('isValid');
		var isValid_userName = $('#editform #username').validatebox('isValid');
		var isValid_phoneNum = $('#editform #phone').validatebox('isValid');
		var isValid_email = true;

		if ($('#editform #email').val() != '') {
			isValid_email = $('#editform #email').validatebox('isValid');
		}

		if (!isValid_userCode || !isValid_userName || !isValid_phoneNum || !isValid_email) {
			return false;
		}

		return true;
	}

//	showPath();
</script>
