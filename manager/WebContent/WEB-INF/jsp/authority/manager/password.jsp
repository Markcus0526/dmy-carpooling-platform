<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div style="padding: 30px 50px 30px 50px">
	<form id="pwdForm" method="post">
		<table>
			<tr>
				<td style="width: 100px; text-align: right; height: 35px">用户名:</td>
				<td><input id="username" name="userInfo.username"
					class="easyui-validatebox textbox" style="width: 150px;"
					type="text" disabled="disabled"></td>
			</tr>
			<tr>
				<td style="width: 100px; text-align: right; height: 35px">新密码:</td>
				<td><input type="password" id="pwd1" name="newPassword"
					class="easyui-validatebox textbox" type="text"
					style="width: 150px;" data-options="required:true"></td>
			</tr>
			<tr>
				<td style="width: 100px; text-align: right; height: 35px">重复新密码:</td>
				<td><input type="password" id="pwd2"
					class="easyui-validatebox textbox" type="text" name="pwd2"
					style="width: 150px;" data-options="required:true"></input></td>
			</tr>
		</table>
	</form>
</div>

<script>
 
// 	$(function() {
		pwdDlg.dialog({
			title:'重置密码',
			width: 350,
			height: 260,
			modal:true,
			buttons: [{
				text:'确 定',
				iconCls:'icon-ok',
				handler:submitForm,
			}, {
				text:'取消',
				iconCls:'icon-cancel',
				handler:closePwdForm				
			}]
		});
		
		$('.easyui-linkbutton').linkbutton();
		$('.easyui-validatebox').validatebox();
		$('#username').val(userInfo.username);
		$('#user_id').val(userInfo.id);
// 	});
	
	function submitForm() {
		if ($('#pwd1').val() != $('#pwd2').val()) { 
			parent.$.messager.alert('重置密码', '密码不一致.', 'error');
			return;
		} else if (!$('#pwd1').validatebox('isValid')
				|| !$('#pwd1').validatebox('isValid')) {
			return;
		} else {
			$('#pwdForm').ajaxSubmit({
				url : page_path + "password.do?method=set&id=" + userInfo['id'],
				success : function(result) {
					try {
						if (result.err_code == 0) {
							parent.$.messager.show({
								title: '重置密码',
								msg:  '密码修改成功',
								showType:'fade',
								style:{
									right:'',
									bottom:''
								}								
							});
							closePwdForm();
						} else {
							parent.$.messager.alert('重置密码', '密码修改失败', 'error');
						}
					} finally {
					}
				},
				error : function() {
					parent.$.messager.alert('重置密码', '未知的错误.', 'error');
				},
				complete : function() {
				}
			});
		}
	}

	function closePwdForm() {
 		pwdDlg.dialog('close');
 		pwdDlg.html("");
	}

</script>
</HTML>
