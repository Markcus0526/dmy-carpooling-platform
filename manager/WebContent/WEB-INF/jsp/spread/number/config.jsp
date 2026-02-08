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
		<s:hidden id="id" />
		<table>
			<tr>
				<td>
					<div class="fix_group_large">
						<label style="float: left;">乘客方返利：</label> <input type="text"
							id="profit_passenger"
							class="easyui-validatebox textbox text_mini"
							data-options="required: true" /> <select id="active_passenger"
							class="easyui-combobox">
							<option value="1" selected>%*信息费</option>
							<option value="2">点</option>
						</select>
					</div>
				</td>
				<td>
					<div class="fix_group">
						<label style="float: left;">乘客方有效期：</label> <input type="text"
							id="limit_passenger" class="easyui-validatebox textbox text_mini"
							data-options="required: true" />个月
					</div>
				</td>
				<td>
					<div class="fix_group">
						<label style="float: left;">乘客方有效次数：</label> <input type="text"
							id="count_passenger" class="easyui-validatebox textbox text_mini"
							data-options="required: true" />
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="fix_group_large">
						<label style="float: left;">车主方返利：</label> <input type="text"
							id="profit_driver" class="easyui-validatebox textbox text_mini"
							data-options="required: true" /> <select id="active_driver"
							class="easyui-combobox">
							<option value="1" selected>%*信息费</option>
							<option value="2">点</option>
						</select>
					</div>
				</td>
				<td>
					<div class="fix_group">
						<label style="float: left;">车主方有效期：</label> <input type="text"
							id="limit_driver" class="easyui-validatebox textbox text_mini"
							data-options="required: true" />个月
					</div>
				</td>
				<td>
					<div class="fix_group">
						<label style="float: left;">车主方有效次数：</label> <input type="text"
							id="count_driver" class="easyui-validatebox textbox text_mini"
							data-options="required: true" />
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>
<script>
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-validatebox').validatebox();
	$('.easyui-numberbox').numberbox();
	
	confDlg.dialog({
		title:'邀请码参数设置',
		width: 800,
		height: 180,
		modal:true,
		buttons: [{
			text:'保存',
			iconCls:'icon-ok',
			handler:setConfig,
		}, {
			text:'取消',
			iconCls:'icon-cancel',
			handler:closeForm		
		}]
	});

	$.ajax({
		url: page_path + "getConfig.do?id=" + user_id,
		success: function(data) {
			conf = data.infos[0];
			
			$("#profit_passenger").val(conf.profit_passenger);
			$("#active_passenger").val(conf.active_passenger);
			$("#limit_passenger").val(conf.limit_passenger);
			$("#count_passenger").val(conf.count_passenger);

			$("#profit_driver").val(conf.profit_driver);
			$("#active_driver").val(conf.active_driver);
			$("#limit_driver").val(conf.limit_driver);
			$("#count_driver").val(conf.count_driver);
		}
	});

	function setConfig() {
		if (!$('#profit_passenger').validatebox('isValid')) {
			return;
		}

		if (!$('#limit_passenger').validatebox('isValid')) {
			return;
		}

		if (!$('#count_passenger').validatebox('isValid')) {
			return;
		}

		if (!$('#profit_driver').validatebox('isValid')) {
			return;
		}

		if (!$('#limit_driver').validatebox('isValid')) {
			return;
		}

		if (!$('#count_driver').validatebox('isValid')) {
			return;
		}

		var s_data = "";
		s_data = "id=" + user_id + 
			"&profit_passenger=" + $("#profit_passenger").val() +
			"&active_passenger=" + $("#active_passenger").val() +
			"&limit_passenger=" + $("#limit_passenger").val() +
			"&count_passenger=" + $("#count_passenger").val() +
			"&profit_driver=" + $("#profit_driver").val() +
			"&active_driver=" + $("#active_driver").val() +
			"&limit_driver=" + $("#limit_driver").val() +
			"&count_driver=" + $("#count_driver").val();

		$.ajax({
			url: page_path + "config.do?" + s_data,
			success: function(data) {
				refreshHeader();
				refreshList();
			}
		});
	}
	
	function closeForm() {
		confDlg.dialog('close');
		confDlg.html("");
	}

</script>