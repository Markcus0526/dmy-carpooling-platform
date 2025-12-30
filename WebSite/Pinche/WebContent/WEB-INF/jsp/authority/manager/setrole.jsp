<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div style="padding: 50px 30px 50px 30px">
	<table>
		<tbody style="border-style: hidden;">
			<tr>
				<td>备选操作权限角色</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>已选角色</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>备选数据权限角色</td>
			</tr>

			<tr>
				<td><select name="sel_op_roles[]" id="op_roles" size=10
					multiple="multiple" style="width: 180px;">
				</select></td>
				<td><img id="addOpRole"
					src="${pageContext.request.contextPath}/images/right_arrow.png" />
				</td>
				<td>
					<div id="user_role_list"
						style="width: 180px; height: 155px; overflow-y: scroll; border: 1px solid;">
					</div>

				</td>
				<td><img id="addDataRole"
					src="${pageContext.request.contextPath}/images/left_arrow.png" /></td>
				<td><select name="sel_data_roles[]" id="data_roles" size=10
					multiple="multiple" style="width: 180px;">
				</select></td>
			</tr>
		</tbody>
	</table>

	<div>
		<br />
	</div>
	<div>
		<label style="margin-left: 25%;" for="text-field">至少应选择一个操作权限角色和一个数据权限角色</label>
	</div>
	<div>
		<br /> <label id="errmsg" class="err_msg">&nbsp;</label>
	</div>

</div>

<script>

// 	$(function() {
		$('#addOpRole').click(addOpRole);
		$('#addDataRole').click(addDataRole);
		$('#op_roles').dblclick(addOpRole);
		$('#data_roles').dblclick(addDataRole);

		$('#user_role_list').empty();
		var role_list;
		$.ajax({
			url: page_path + "setrole.do?method=get&id=" + uid,
			success: function(data) {
				role_list = data.user_role_list;
				for(var i=0;i<role_list.length;i++ )
					$('<div class="item" id="' + role_list[i].value + "_" + role_list[i].text + '">' + role_list[i].text + '<img  class="img" src="${pageContext.request.contextPath}/images/remove.png" style="float:right;width:13px;height:13px;"/></div>').appendTo($("#user_role_list"));
				$('.img').click(removeRole);
				$('.item').click(onItemClick);
			}
		});
		
		showRoleData(roleData);
		
		function addOpRole() {
			$('#op_roles option:selected').each(
				function() {
					if (!$('#user_role_list option[value=' + $(this).val() + ']').length)
						$('<div class="item" id="' + $(this).val() + "_" + $(this).text() + '">' + $(this).text() + '<img  class="img" src="images/remove.png" style="float:right;width:13px;height:13px;"/></div>').appendTo($("#user_role_list"));
					$(this).remove();
				});
			$('.img').click(removeRole);
			$('.item').click(onItemClick);
		};
		
		function addDataRole() {
			$('#data_roles option:selected').each(
				function() {
					if (!$('#user_role_list option[value=' + $(this).val() + ']').length)
						$('<div class="item" id="' + $(this).val() + "_" + $(this).text() + '">' + $(this).text() + '<img class="img" src="images/remove.png" style="float:right;width:13px;height:13px;"/></div>').appendTo($("#user_role_list"));
					$(this).remove();
				});
			$('.img').click(removeRole);
			$('.item').click(onItemClick);
		};
		
		function onItemClick()
		{
			var id = "#" + $(this).attr("id");
			$(".item").removeClass("meselected");
			$(id).addClass("meselected");
		}
		
		function removeRole() {
			var cid = $(this).parent().attr("id");
			var id = cid.split("_");
			role_type = id[0]; role_id = id[1];
			itext = id[2];
			
			if (role_type == 'data') {
				if (!$('#data_roles option[value=' + cid + ']').length)	{
					$('<option />').val(cid).text(itext).appendTo($('#data_roles'));
				}
			} else if (role_type == 'op') {
				if (!$('#op_roles option[value=' + cid + ']').length) {
					$('<option />').val(cid).text(itext).appendTo($('#op_roles'));
				}
			}
			
			$('#'+cid).remove();
		}
// 	});

	function save() {
		var user_role_ids = [];
		var user_role_list= $('#user_role_list').children();
		
		if (user_role_list.length == 0) {
			$('#errmsg').html("选择的角色没有。");
			return;
		}

		var selectedOpRole = false;
		var selectedDataRole = false;
		for(var i=0;i<user_role_list.length;i++)
		{
			var eachdiv=user_role_list[i];
			var cid=eachdiv.id;
			var id=cid.split('_');
			
			if (id[0] == 'op') {
				selectedOpRole = true;
			} else {
				selectedDataRole = true;
			}
			
			var role=id[0]+ "_" + id[1];
			
			user_role_ids.push(role);
		}

		if (!selectedOpRole || !selectedDataRole) {
			$('#errmsg').html("您要选择操作角色和数据角色。");
			return;
		}
		
		$.getJSON(
				page_path + 'setrole.do?method=add&id=' + uid, {
				user_role_ids : user_role_ids.join(',')
				}, function(result) {
					if (result.err_code == 0) {
						parent.$.messager.show({
							title:'权限角色',
							msg:'权限角色失败。',
							showType:'fade',
							style:{
								right:'',
								bottom:''
							}
						});
						setRoleDlg.dialog('close');
						setRoleDlg.html('');
					} else {
						parent.$.messager.alert('权限角色', '权限角色失败。', 'error');
					}
				}
			);
	};
	
	function cancel() {
		setRoleDlg.dialog('close');
		setRoleDlg.html('');
	}

	function showRoleData(result) {
		$('#op_roles').empty();
        $.each(result['op_role_list'], function(i, op_role) {
             $('<option />').val(op_role['value']).text(op_role['text']).appendTo($('#op_roles'));
        });
         
		$('#data_roles').empty();
        $.each(result['data_role_list'], function(i, data_role) {
            $('<option />').val(data_role['value']).text(data_role['text']).appendTo($('#data_roles'));
        });

        setRoleDlg.dialog({
			title:'分配角色',
			width: 720,
			height: 430,
			buttons: [{
				text:'保存',
				iconCls:'icon-ok',
				handler:save,
			}, {
				text:'取消',
				iconCls:'icon-cancel',
				handler:cancel				
			}]
		});
	}

</script>
