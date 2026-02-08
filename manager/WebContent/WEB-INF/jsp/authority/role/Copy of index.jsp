<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/icon.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/outlook2.js'> </script>
</head>

<body>
	<br />
	<h3 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp后台角色管理
	</h3>
	<hr>

	<div id="role_tab" class="easyui-tabs" data-options="tabWidth:112"
		style="width: auto">
		<div title="操作角色管理" style="padding: 10px">
			<div style="padding: 5px">
				<div>
					角色名称： <input class="easyui-searchbox"
						data-options="prompt:'输入',searcher:doSearchOp"
						style="width: 200px" value=""></input> <a
						href="javascript:void(0)" onclick="showAdd(0)"
						class="easyui-linkbutton" style="float: right; margin-right: 0px">&nbsp&nbsp&nbsp添加操作角色&nbsp&nbsp&nbsp</a>
				</div>
			</div>
			<table id="tb_op" class="easyui-datagrid"
				style="width: auto; height: auto"
				data-options="rownumbers:true,singleSelect:true,onClickRow: onClickRow, locales:'ca'">
			</table>
		</div>
		<div title="数据角色管理" style="padding: 10px">
			<div style="padding: 5px">
				<div>
					角色名称： <input class="easyui-searchbox"
						data-options="prompt:'输入',searcher:doSearchData"
						style="width: 200px" value=""></input> <a
						href="javascript:void(0)" onclick="showAdd(1)"
						class="easyui-linkbutton" style="float: right; margin-right: 0px">&nbsp&nbsp&nbsp添加数据角色&nbsp&nbsp&nbsp</a>
				</div>
			</div>
			<table id="tb_data" style="width: auto" class="mytb"
				style="overflow:none;"
				data-options="rownumbers:true,singleSelect:true,onClickRow: onClickRow">
			</table>
		</div>
	</div>

	<div id="dlg"></div>

	<script>
			var tb_index = -1;
			$('.easyui-tree').tree();
			$('.easyui-tabs').tabs();
			$('.easyui-datebox').datebox();
			$('.easyui-linkbutton').linkbutton();
			$('.easyui-combobox').combobox();
			$('.easyui-searchbox').searchbox();
			$('.easyui-validatebox').validatebox();
			var addDlg;
			var editDlg;
			var viewDlg;
			var opDlg;
			var dataDlg;
			var type = 0;
			var id = 0;
			var roleInfo = {};

			var dataLoader = {};
			var dataLoader1 = {};
			var page_path = '${pageContext.request.contextPath}/authority/role/';
		// 	$(function(){
				dataLoader.load=function(searchValue){
					if (searchValue == undefined) searchValue = '';
					str_url = page_path + 'search.do?type=0';
					$('#tb_op').datagrid({
						url: str_url,
						queryParams: {searchValue:searchValue},
						loadMsg : '数据处理中，请稍后....',
						height: 'auto',
						fitColumns: true,
						pagination: true,
			
						columns:[[
						          {field:'operation',title:'操作',width:100, align:'center'},
						          {field:'name',title:'角色名称',width:100, align:'center'},
						          {field:'comment',title:'操作权限',width:200,align:'center'},
						          {field:'remark',title:'备注',width:250,align:'center'},
						]],
						onHeaderContextMenu: function(e, field){
							e.preventDefault();
							if (!cmenu){
								createColumnMenu();
							}
							cmenu.menu('show', {
								left:e.pageX,
								top:e.pageY
							});
						}
					});
				};
				
				dataLoader1.load=function(searchValue){
					if (searchValue == undefined) searchValue = '';
					str_url = page_path + 'search.do?type=1&searchValue=' + searchValue;
					$('#tb_data').datagrid({
						url: str_url,
						height: 'auto',
						fitColumns: true,
						pagination: true,
		
						columns:[[
						          {field:'operation',title:'操作',width:100, align:'center'},
						          {field:'name',title:'角色名称',width:100, align:'center'},
						          {field:'comment',title:'操作权限',width:100,align:'center'},
						          {field:'remark',title:'备注',width:250,align:'center'},
						]],
						onHeaderContextMenu: function(e, field){
							e.preventDefault();
							if (!cmenu){
								createColumnMenu();
							}
							cmenu.menu('show', {
								left:e.pageX,
								top:e.pageY
							});
						}
					});
				};
		// 	});
			function showAdd(type) {
				this.type = type;
				this.id = 0;
				addDlg = $('#dlg');
				addDlg.load(page_path + "open.do?method=add");
			}
			
			function showEdit(id, type) {
				this.type = type;
				this.id = id;
				
				var temp;
				if (type == 0)
					temp = $('#tb_op').datagrid('selectRecord', tb_index + 1).datagrid('getData').rows[tb_index];
				else
					temp = $('#tb_data').datagrid('selectRecord', tb_index + 1).datagrid('getData').rows[tb_index];
				roleInfo.name = temp.name;
				roleInfo.remark = temp.remark;

				editDlg = $('#dlg');
				editDlg.load(page_path + "open.do?method=edit");
			}
			
			function showView(id, type) {
				this.type = type;
				this.id = id;
				
				var temp;
				if (type == 0)
					temp = $('#tb_op').datagrid('selectRecord', tb_index + 1).datagrid('getData').rows[tb_index];
				else
					temp = $('#tb_data').datagrid('selectRecord', tb_index + 1).datagrid('getData').rows[tb_index];
				roleInfo.name = temp.name;
				roleInfo.remark = temp.remark;

				viewDlg = $('#dlg');
				viewDlg.load(page_path + "open.do?method=view");
			}
			
			function deleteRole(id, name) {
				$.messager.defaults = {
						ok : "确  定",
						cancel : "取  消"
					};
				$.messager.confirm('删除', '您确定要删除【' + name + '】角色吗？', function(r){
					if (r){
						$.ajax({
							url: page_path + "delete.do?id=" + id,
				            success : function(data){
								try {
									alert(data);
					            	ret = data;
					                if (ret.err_code == 94) {
					                   parent.$.messager.alert('删除', ret.err_msg, 'error');
					                }
					            } finally {
					            	closeForm();
					            	refreshList();
					            }
				            }
						});
					}
				});
			};
			
			function onClickRow(index) {
				tb_index = index;
			}
		
			function doSearchOp(value){
				dataLoader.load(value);
			}
			
			function doSearchData(value){
				dataLoader1.load(value);
			}
			
			function refreshList() {
				dataLoader.load();
				dataLoader1.load();
			}
			
			var node_list = [];
			var chk_list = [];
			var item_list = [];
			var rid = 0;

			function showItems(id, type) {
				this.type = type;
				this.id = id;
				node_list = [];
				chk_list = [];
				item_list = [];

				if (type == 0) {
					opDlg = $('#dlg');
					opDlg.load(page_path + "open.do?method=setOpRole");
				} else {
					dataDlg = $('#dlg');
					dataDlg.load(page_path + "open.do?method=setDataRole");
				}
				rid = id;
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
			}
			
			function showTree() {
				var innerhtml = '<ul class="easyui-tree" id="trees" data-options="lines:true, animate:true, checkbox:true';
				if (type == 0) {
					innerhtml += ', onCheck:checkNode';
				}
				innerhtml += '">';
				var temp = -1;
				for (var i = 0; i < item_list.length; i++) {
					if (temp < item_list[i].level && i != 0) {
						innerhtml += "<ul>";
					}

					if (temp == item_list[i].level) {
						innerhtml += "</li>";
					}
					
					if (temp > item_list[i].level) {
						innerhtml += "</li>";
						for (var j = 0; j < temp - item_list[i].level; j++) {
							innerhtml += "</ul></li>";
						}
					}

					innerhtml += '<li ';
					if (item_list[i].chk == 1)
						innerhtml += 'data-options="checked:true"';
					
					innerhtml += '><span>' + item_list[i].name + '</span>';
					
					
					temp = item_list[i].level;
				}

				for (var i = 0; i < temp; i++) {
					innerhtml += "</li>";
					if (i != temp - 1)
						innerhtml += "</ul>";
				}
				
				innerhtml += "</ul>";
				$("#opers").html(innerhtml);
				$('.easyui-tree').tree();
				$('#trees').tree('collapseAll');
				$(".tree-icon").removeClass("tree-folder");
				$(".tree-icon").removeClass("tree-file");
				refreshTree();
			}
			
			function refreshTree() {
				getNodeList();
				for (var i = 0; i < node_list.length; i++) {
					var nodeid = "#" + node_list[i].domId + " .tree-checkbox";
					$(nodeid).removeClass("tree-checkbox0");
					$(nodeid).removeClass("tree-checkbox1");
					$(nodeid).removeClass("tree-checkbox2");
					if (item_list[i].chk == 1) {
						$(nodeid).addClass("tree-checkbox1");
					} else if (item_list[i].chk == 2) {
						$(nodeid).addClass("tree-checkbox2");
					} else {
						$(nodeid).addClass("tree-checkbox0");
					}	
				}
			}
			
			function getNodeList() {
				var roots = $("#trees").tree("getRoots");
				for (var i = 0; i < roots.length; i++) {
					var nodes = $("#trees").tree("getChildren", parent.target);
					for (var i = 0; i < nodes.length; i++) {
						node_list[node_list.length] = nodes[i];
					}
				}
			}
			
			refreshList();
		// 	showPath();
		</script>
</body>
</html>