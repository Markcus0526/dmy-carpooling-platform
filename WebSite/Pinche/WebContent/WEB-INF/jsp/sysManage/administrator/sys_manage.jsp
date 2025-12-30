<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<br />
<h2 class="page-title txt-color-blueDark">
	<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp后台用户管理
</h2>
<hr>

<form id="hiddenForm" method="post">
	<input id="searchValue" type="hidden" /> <input id="searchType"
		type="hidden" />
</form>

<div id="mm">
	<!-- <div data-options="name:'all',iconCls:'icon-ok'">All News</div>  -->
	<div data-options="name:'0'">全部查询</div>
	<div data-options="name:'1'">按用户名查询</div>
	<div data-options="name:'2'">按姓名查询</div>
	<div data-options="name:'3'">按手机号查询</div>
	<div data-options="name:'4'">按公司查询</div>
	<div data-options="name:'5'">按ID查询</div>
</div>

<div style="padding: 5px">
	<div style="padding: 5px">
		<input class="easyui-searchbox"
			data-options="prompt:'输入',menu:'#mm',searcher:doSearch"
			style="width: 300px" /> <a href="#" onclick="javascript:report();"
			class="easyui-linkbutton l-btn" style="float: right;"
			iconCls="icon-xlx">导出Excel</a>

		<c:if
			test="${fn:contains(session.resourceList, '/authority/manager/delete')}">
			<a href="#" class="easyui-linkbutton l-btn"
				onclick="javascript:batchDeleteFun()"
				style="float: right; margin-right: 10px">&nbsp;批量删除&nbsp;</a>
		</c:if>

		<c:if
			test="${fn:contains(session.resourceList, '/authority/manager/add')}">
			<a href="#" onclick="javascript:add();"
				class="easyui-linkbutton l-btn"
				style="float: right; margin-right: 10px">&nbsp;添加用户&nbsp;</a>
		</c:if>
	</div>
</div>

<div id="list" style="padding: 10px">
	<table id="dg" class="easyui-datagrid"
		style="width: auto; height: auto"
		data-options="singleSelect:true,pagination:true">
	</table>
</div>

<div id="dlg"></div>

<c:if
	test="${fn:contains(session.resourceList, '/authority/manager/edit')}">
	<script>
		$.canEdit = true;
	</script>
</c:if>

<c:if
	test="${fn:contains(session.resourceList, '/authority/manager/view')}">
	<script>
		$.canView = true;
	</script>
</c:if>

<c:if
	test="${fn:contains(session.resourceList, '/authority/manager/delete')}">
	<script>
		$.canDel = true;
	</script>
</c:if>

<c:if
	test="${fn:contains(session.resourceList, '/authority/manager/assign')}">
	<script>
		$.canAssign = true;
	</script>
</c:if>

<c:if
	test="${fn:contains(session.resourceList, '/authority/manager/password')}">
	<script>
		$.canSetPwd = true;
	</script>
</c:if>
<script>
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-combobox').combobox();
	$('.easyui-searchbox').searchbox();
	$('.easyui-validatebox').validatebox();
	
	var addUserDlg;
	var viewUserDlg;
	var editUserDlg;
	var setRoleDlg;
	var pwdDlg;
	
	var dataGrid;
	var dataLoader = {};
	var userInfo;
	var roleData;
	var uid;
	var unitList;
	
	var page_path = "${pageContext.request.contextPath}/authority/manager/";
	$(function(){
		dataLoader.load = function() {
			params = {searchValue:$('#searchValue').val(), searchType: $('#searchType').val()};		
			dataGrid = $('#dg').datagrid({
				url : page_path + 'search',
				queryParams: params,
				loadMsg : '数据处理中，请稍后....',
				pagination: true,
				fitColumns : true,
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				idField : 'id',
				selectOnCheck : $(this).is(':checked'),
				checkOnSelect : $(this).is(':checked'),
	
				columns : [ [ {
					field : 'ck',
					checkbox : true
				}, {
					field : 'action',
					title : '操作',
					width : 250,
					align : 'center',
					formatter: function(value, row, index) {
						var str='';
						if ($.canView) {
							str = '<a href="javascript:view(' + row.id + ');">查看</a>';
						}
						
						if ($.canEdit) {
							if ($.canView)
								str += "|";
							str += '<a href="javascript:edit(' + row.id + ');">修改</a>';
						}
						
						if ($.canDel) {
							if ($.canView || $.canEdit)
								str += "|";
							str += '<a href="javascript:delFunc(' + row.id + ', \'' + row.usercode + '\');">删除</a>';
						}
						
						if ($.canSetPwd) {
							if ($.canView || $.canEdit || $.canDel)
								str += "|";
							str += '<a href="javascript:getManagerInfo(' + row.id + ', \'' + row.username + '\');">重置密码</a>';
						}
						
						if ($.canAssign) {
							if ($.canView || $.canEdit || $.canDel || $.canSetPwd)
								str += "|";
							str += '<a href="javascript:showAssign(' + row.id + ');">分配角色</a>';
						}
						
						return str;
					}
				}, {
					field : 'id',
					title : 'ID',
					width : 150,
					align : 'center'
					
				}, {
					field : 'usercode',
					title : '用户名',
					width : 150,
					align : 'center'
				}, {
					field : 'username',
					title : '姓名',
					width : 100,
					align : 'center'
				}, {
					field : 'unit',
					title : '所属公司',
					width : 150,
					align : 'center'
				}, {
					field : 'phoneNum',
					title : '手机号码',
					width : 150,
					align : 'center'
				} ] ],
				
				onLoadSuccess : function() {
					$('#list table').show();
					parent.$.messager.progress('close');
				},
				
				onHeaderContextMenu : function(e, field) {
					e.preventDefault();
					if (!cmenu) {
						createColumnMenu();
					}
					cmenu.menu('show', {
						left : e.pageX,
						top : e.pageY
					});
				}
			});
		};
		
		$.getJSON(page_path + "getUnitList",
				{}, 
				function(result){
					unitList = result['unitList'];
		});
	});

	function delFunc(id, usercode) {
		
		var currentUserId = '${session.user.id}';/*当前登录用户的ID*/
		if (currentUserId == id) {
			parent.$.messager.show({
				title : '提示',
				msg : '不可以删除自己！',
				showType:'fade',
				style:{
					right:'',
					bottom:''
				}
			});
			return;
		}
		
		$.messager.defaults = {
			ok : "确  定",
			cancel : "取  消"
		};
		$.messager
				.confirm(
						'删除',
						'您确定要删除【' + usercode + '】吗？',
						function(r) {
							if (r) {
								$
										.ajax({
											url : page_path + 'delete?id='
													+ id,
											success : function(data) {
												try {
									            	ret = data;
									                if (ret.err_code == 0) {
									                	dataLoader.load();
									                } else {
									                   parent.$.messager.alert('删除', ret.err_msg, 'error');
									                }
									            } finally {}
									        },
										});
							} else {
							}
						});
	};

	function batchDeleteFun() {
		var rows = dataGrid.datagrid('getChecked');
		var ids = [];
		if (rows.length > 0) {
			parent.$.messager.confirm('确认', '您是否要删除当前选中的项目？', function(r) {
				if (r) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					var currentUserId = '${session.user.id}';/*当前登录用户的ID*/
					var flag = false;
					for ( var i = 0; i < rows.length; i++) {
						if (currentUserId != rows[i].id) {
							ids.push(rows[i].id);
						} else {
							flag = true;
						}
					}
					$.getJSON(page_path + 'batchDelete', {
						ids : ids.join(',')
					}, function(result) {
						if (result.err_code == 0) {
							parent.$.messager.show({
								title:'批量删除',
								msg: '删除成功',
								showType:'fade',
								style:{
									right:'',
									bottom:''
								}
							});
							dataLoader.load();
							dataGrid.datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
						} else {
							parent.$.messager.alert('批量删除', '删除失败', 'error');
						}
						
						if (flag) {
							parent.$.messager.show({
								title : '提示',
								msg : '不可以删除自己！'
							});
						}
						
						parent.$.messager.progress('close');
					});
				}
			});
		} else {
			parent.$.messager.show({
				title : '提示',
				msg : '请勾选要删除的记录！'
			});
		}
	}

	function add() {
		addUserDlg = $('#dlg');
		addUserDlg.load(page_path + "open?method=add");
	};

	function edit(id) {
		$.getJSON(page_path + 'edit_info?method=get', {id:id},
			function(result) {
				if (result.err_code == 0) {
					userInfo = result['infos'][0];
					editUserDlg = $('#dlg');
					editUserDlg.load(page_path + "open?method=edit");
				} else {
					parent.$.messager.alert('批量删除', ret.err_msg, 'error');
				}
			});
	};
	
	function view(id) {
		$.getJSON(page_path + 'edit_info?method=view', {id:id},
			function(result) {
				if (result.err_code == 0) {
					userInfo = result['infos'][0];
					viewUserDlg = $('#dlg');
					viewUserDlg.load(page_path + "open?method=view");
				} else {
					parent.$.messager.alert('批量删除', ret.err_msg, 'error');
				}
			});
	};
	
	function showAssign(id) {
		uid = id;
		$.getJSON(page_path + 'setrole?method=get', {id : id},
			function(result) {
				if (result.err_code == 0) {
					roleData = result;
					setRoleDlg = $('#dlg');
					setRoleDlg.load(page_path + "open?method=setrole");
				} else {
					parent.$.messager.alert('权限角色', result.err_msg, 'error');
				}
		});
	}

	function report() {
		location.href = page_path + "export";
	};

	function doSearch(value, type) {
		$('#searchValue').val(value);
		$('#searchType').val(type);
		dataLoader.load();
	}

	function getManagerInfo(id, username) {
		userInfo = {};
		userInfo['id'] = id;
		userInfo['username'] = username;
		
		pwdDlg = $('#dlg');
		pwdDlg.load(page_path + "password?method=get");
	};

	dataLoader.load();
	showPath();
</script>
