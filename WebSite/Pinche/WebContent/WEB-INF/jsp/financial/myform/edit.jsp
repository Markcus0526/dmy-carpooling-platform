<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<br />
<h2 class="page-title txt-color-blueDark">
	<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp我的表单
</h2>
<hr>

<div id="dlgManage" class="easyui-dialog" title="修改表单-充值"
	style="width: auto; height: auto; padding: 0px"
	data-options="modal:true">
	<div class="easyui-panel" style="width: 400px">
		<div style="padding: 10px 20px 10px 10px">
			<form id="viewform" method="post">
				<input type="hidden" id="user_id" name="user_id" />
				<table cellpadding="5">
					<tr>
						<td style="width: 100px; text-align: right">客户名称：</td>
						<td style="width: 200px"><input id="username" name="username"
							class="easyui-validatebox textbox" type="text"
							data-options="required:true" disabled></input></td>
					</tr>
					<tr>
						<td style="width: 100px; text-align: right">客户余额：</td>
						<td style="width: 200px"><input id="balance" name="balance"
							class="easyui-validatebox textbox" type="text"
							data-options="required:true" disabled></input></td>
					</tr>
					<tr>
						<td style="width: 100px; text-align: right">修改金额：</td>
						<td style="width: 200px"><input id="sum" name="sum"
							class="easyui-validatebox easyui-numberbox" type="text"
							data-options="required:true" precision="2"></input></td>
					</tr>
					<tr>
						<td style="width: 100px; text-align: right">操作原因：</td>
						<td style="width: 200px"><select id="oper_type"
							name="oper_type" class="easyui-combobox">
								<option value="1" selected>线下交费充值</option>
								<option value="2">系统出错补偿</option>
								<option value="3">其他</option>
						</select></td>
					</tr>
					<tr>
						<td style="width: 100px; text-align: right">相关订单：</td>
						<td style="width: 200px"><input id="order_cs_id"
							name="order_cs_id" class="easyui-validatebox textbox" type="text"
							data-options="required:true" disabled></input><a href="#"
							onclick="javascript:searchInterface();"
							class="easyui-linkbutton l-btn" iconCls="icon-search"
							style="width: 40px"></a></td>
					</tr>
					<tr>
						<td style="width: 100px; text-align: right">详细说明：</td>
						<td style="width: 200px"><textarea id="remark" name="remark"
								data-options="required:true"></textarea></td>
					</tr>
				</table>
			</form>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" id="save" class="easyui-linkbutton"
					onclick="saveForm()" style="width: 80px">确定</a> <a
					href="javascript:void(0)" id="close" class="easyui-linkbutton"
					onclick="closeForm()" style="width: 80px">取 消</a>
			</div>
		</div>
	</div>
</div>
<script>
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-validatebox').validatebox();
	$('.easyui-numberbox').numberbox();

// 	$(function() {
		$.ajax({
			url : page_path + 'getItem.do?id=' + global_id + '&order_id=' + order_id,
			success: function(data) {
				temp = data;
				$("#user_id").val(temp.user_id);
				$("#username").val(temp.username);
				$("#balance").val(temp.balance);
				$("#sum").val(temp.sum);
				$("#sum").numberbox("setValue", temp.sum);
				$("#oper_type").val(temp.oper_type);
				$("#order_cs_id").val(temp.order_cs_id);
				$("#remark").val(temp.remark);
				$('#dlgManage').dialog('open');
			}
		});
// 	});
	
	function searchInterface() {
		$('#div1').load(page_path + "ordersearchPage.do",
				{"orderSearchItem.usercode":$("#user_id").val(),
				"orderSearchItem.customer_name": $("#username").val()},
				function(){});
	}

	function saveForm() {
		if (!$('#sum').validatebox('isValid')) {
			return;
		}

		$("#viewform").ajaxSubmit({
			url: page_path + "edit.do?method=edit&id=" + global_id,
			data: {order_cs_id: $("#order_cs_id").val()},
            success : function(){
            	closeForm();
            }
        });
	}

	function closeForm() {
		$('#div1').load(page_path+"index.do");
	}
</script>