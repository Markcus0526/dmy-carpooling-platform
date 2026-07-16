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
<div style="padding: 10px 20px 10px 10px">
	<form id="viewform" method="post">
		<s:hidden id="id" name="role.id" />
		<table cellpadding="5">
			<tr>
				<td style="width: 100px; text-align: right">角色名称：</td>
				<td style="width: 200px"><input id="name" name="role.name"
					class="easyui-validatebox textbox" type="text"
					data-options="required:true" disabled></input></td>
			</tr>
			<tr>
				<td style="width: 100px; text-align: right">备注说明：</td>
				<td style="width: 200px"><textarea id="remark"
						name="role.remark" data-options="required:true"
						style="width: 250px; height: 100px" disabled></textarea></td>
			</tr>
		</table>
	</form>
</div>
<script>
// 	$(function() {
		$('.easyui-linkbutton').linkbutton();
		$('.easyui-validatebox').validatebox();
		$('.easyui-numberbox').numberbox();
		
		viewDlg.dialog({
			title:'操作角色添加',
			width: 500,
			height: 265,
			modal:true,
			buttons: [{
				text:'取消',
				iconCls:'icon-cancel',
				handler:closeForm		
			}]
		});
// 	});
	
	refresh();
	
	function refresh() {
		$("#viewform #id").val(id);
		$("#viewform #name").val(roleInfo.name);
		$("#viewform #remark").val(roleInfo.remark);
	}
	
	function closeForm() {
		viewDlg.dialog('close');
		viewDlg.html("");
	}
	
//	showPath();
</script>

