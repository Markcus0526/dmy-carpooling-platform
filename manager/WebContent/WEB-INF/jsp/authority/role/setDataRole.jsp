<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<s:form theme="simple">
	<div id="content" style="opacity: 1;">
		<div id="opers" style="margin: 20px;"></div>
	</div>
</s:form>
<script>
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-tree').tree();
	
// 	$(function() {
		dataDlg.dialog({
			title:'权限分配',
			width: 600,
			height: 445,
			modal:true,
			buttons: [{
				text:'保存',
				iconCls:'icon-ok',
				handler:saveform,
			}, {
				text:'取消',
				iconCls:'icon-cancel',
				handler:closeForm		
			}]
		});
		
		$.ajax({
			url: page_path + "getRoleitemlist.do?rid=" + rid,
			success: function(data) {
				try {
					item_list = data.rows;
				} catch(e) {}
			},
			error: function() {
				alert("error");
			},
			complete: function() {
				showTree();
			}
		});
// 	});
	
	function hasChild(id, list) {
		for (var i = 0; i < list.length; i++) {
			if (id == list[i].parent) {
				return true;
			}
		}
		return false;
	}
	
	
	function getChildren(list, obj_id, depth) {
		if (hasChild(obj.id, list)) {
			var data = {id:"", text:"", state:"", value:"", parent:"", rid:"", level:"", children:[{}]};
			var datas = [{}];
			var index = 0;
			for (var i = 0; i < list.length; i++) {
				if (list[i].level == depth && list[i].parent == obj_id) {
					data.id = list[k].mid;
					data.text = list[k].name;
					data.value = list[k].chk;
					data.parent = list[k].parent;
					data.rid = list[k].rid;
					data.level = list[k].level;
					data.children = getChildren(list, data.id, depth + 1);
					datas[index] = data;
					index ++;
				}
			}
			return datas;
		}
		return null;
	}

	function getTreeData(list) {
		var datas = getChildren(list, 0, 0);
	}
	
	function checkNode(node) {
		var parent = $("#trees").tree("getParent", node.target);
		if (node.checked == true) {
			var id = "#" + parent.domId;
			$(id + " .tree-checkbox").addClass("tree-checkbox2");
			checkParents(parent);
		}
	}
	
	function checkParents(node) {
		var parent = $("#trees").tree("getParent", node.target);
		if (node.checked == false) {
			var id = "#" + parent.domId;
			$(id + " .tree-checkbox").addClass("tree-checkbox2");
			checkParents(parent);
		}
	}
	
	function saveform() {
		var chks = '';
		chk_list = [];
		getNodeList();

		for(var i = 0; i < node_list.length; i++){
			var nodeid = "#" + node_list[i].domId + " .tree-checkbox";
			var class_str = $(nodeid).attr("class");
			if (class_str.match("tree-checkbox1")) {
				chk_list[i] = 1;
			} else if (class_str.match("tree-checkbox2")) {
				chk_list[i] = 2;
			} else {
				chk_list[i] = 0;
			}
		}
		
		for (var i = 0; i < chk_list.length; i++) {
			if (chks != '') chks += ",";
			chks += chk_list[i];
		}

		$.ajax({
			url: page_path + "assignOp.do?method=edit",
			type: "post",
			data: {"chks": chks, "rid": rid},
			success: function(ret) {
				closeForm();
				refreshList();
			},
			error: function() {alert("error");},
			complete: function() {}
		});
	}

	function closeForm() {
		dataDlg.dialog('close');
		dataDlg.html("");
	}
	
// 	showPath();
</script>