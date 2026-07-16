<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>

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
</script>
<style>
.left_labelstyle {
	text-align: left;
	font-size: 12px;
	font-weight: bold;
}

.right_labelstyle {
	text-align: left;
	font-size: 12px;
	font-weight: bold;
}
</style>
</head>

<body onload="loaddone()">
	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 35px;">
				<table id="querytable" cellspacing="0" cellpadding="5px"
					style="text-align: left; height: auto; margin: 0px;">
					<tr>
						<td><input class="easyui-searchbox"
							data-options="prompt:'输入',menu:'#mm',searcher:doSearch"
							style="width: 500px; height: 25px" /></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="mm">
		<!-- <div data-options="name:'all',iconCls:'icon-ok'">All News</div>  -->
<!-- 		<div data-options="name:'10'">全部查询</div> -->
		<div data-options="name:'1'">按用户名查询</div>
		<div data-options="name:'2'">按姓名查询</div>
		<div data-options="name:'3'">按手机号查询</div>
		<div data-options="name:'4'">按公司查询</div>
		<div data-options="name:'5'">按用户编号查询</div>
	</div>
	<form id="hiddenForm" method="post">
		<input id="searchValue" type="hidden" /> <input id="searchType"
			type="hidden" />
	</form>
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
			<a onclick="excel();" class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a>

			<c:if
				test="${fn:contains(session.resourceList, '/authority/manager/batch_delete.do')}">
				<a href="#" class="easyui-linkbutton" iconCls="icon-remove"
					plain="true" style="float: right"
					onclick="javascript:batchDeleteFun()"><font
					style="font-weight: bold">批量删除&nbsp;</font></a>
			</c:if>

			<c:if
				test="${fn:contains(session.resourceList, '/authority/manager/add.do')}">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"
					plain="true" style="float: right" onclick="javascript:add();"><font
					style="font-weight: bold">添加用户&nbsp;</font></a>
			</c:if>

		</div>
	</div>

	<div style="padding: 50px 30px 50px 30px" id="administratorView">

		<form id="viewform">
			<input type="hidden" id="id" name="userInfo.id" value=""/>
			<table style="width: 480px; height: 100px;">
				<tr>
					<td class="left_labelstyle" style="width: 70px;">用户名：</td>
					<td style="width: 130px;">
					<input id="usercode" name="userInfo.usercode"
						class="easyui-validatebox textbox" readonly="readonly"
						 style="width: 130px;" />
					</td>
					<td class="right_labelstyle" style="width: 70px;">所属机构：</td>
					<td style="width: 130px;"><select id="org_"
						class="easyui-combobox" style="width: 130px; float: left"
						name="userInfo.unitcode">
					</select></td>
				</tr>
				<tr>
					<td class="left_labelstyle">姓名：</td>
					<td><input id="username" name="userInfo.username"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 130px;" /></td>

					<td class="right_labelstyle">手机号码：</td>
					<td><input id="phone" name="userInfo.phoneNum"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 130px;" /></td>
				</tr>
				<tr>
					<td class="left_labelstyle"">性别：</td>
					<td><select id="sex" name="userInfo.sex"
						class="easyui-combobox" style="width: 130px;">
							<option value="0">男</option>
							<option value="1">女</option>
					</select></td>

					<td class="right_labelstyle">联系电话：</td>
					<td><input id="phone2" name="userInfo.phoneNum2"
						disabled="disabled"
						style="width: 130px;" /></td>
				</tr>
				<tr>
					<td class="left_labelstyle">QQ：</td>
					<td><input id="qq" name="userInfo.qq" disabled="disabled"
						style="width: 130px;" /></td>

					<td class="right_labelstyle">Email：</td>
					<td><input id="email" name="userInfo.email"
					disabled="disabled"
						validType="email" style="width: 130px;" /></td>
				</tr>
				<tr>
					<td class="left_labelstyle"">备注说明：</td>
					<td colspan="3"><input id="note" name="userInfo.note"
						disabled="disabled" style="width: 380px; height: 50px;" /></td>
				</tr>
			</table>
		</form>
	</div>

	<div style="padding: 50px 30px 50px 30px" id="setRoleView">
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
			<label style="margin-left: 25%;" for="text-field">至少应选择一个操作权限角色和一个数据权限角色</label>
		</div>
		<div>
			<br /> <label id="errmsg" class="err_msg">&nbsp;</label>
		</div>

<div id="do" class="easyui-dialog" closed="true" buttons="#dlg-buttons">
	<form action="<%=basePath%>authority/manager/updateRoles.do" method="post" onsubmit="return checkform();" id="jjjueseform">
	<input type="hidden" name="id" value="${id}" id="doid"/>
	  <p><label class="vtwo">请选择角色</label>&nbsp;
	  <div id="jj">
		</div>
	  </p>
	  <input type="submit" value="确定"/>
	  </form>
	</div>

     <div id="dlg" class="easyui-dialog" style="width:800px; height:500px; padding: 10px 20px; top:30px"
		closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table border=0>
				<tr align="center">
					<td align=left>用户名：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="usercode" name="admin.usercode"
						class="easyui-validatebox" required="true"></td>
					<td align=left>所属机构：</td>
					<td align=left><select name="admin.unitcode" id="grid" class="easyui-combobox" required="true"  validType="selectValueRequired['#grid']">
							<c:forEach items="${listGroup}" var="group">
								<option value="${group.id}">${group.group_name}</option>
							</c:forEach>
					</select></td>
					<td></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>姓名：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="username" name="admin.username"
						class="easyui-validatebox" required="true"></td>
					<td align=left>手机号码：</td>
					<td align=left><input type="text" id="phoneNum"
						name="admin.phoneNum" class="easyui-validatebox" required="true"></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>性别：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="radio" name="admin.sex" value="男" checked id="nanradio">男<input
						type="radio" name="admin.sex" value="女">女</td>
					<td align=left>联系电话：</td>
					<td align=left><input type="text" id="phoneNum2"
						name="admin.phoneNum2"></td>
					<td></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>QQ：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="qq" name="admin.qq"></td>
					<td align=left>Email：</td>
					<td align=left><input type="text" id="email"
						name="admin.email"></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>备注说明：</td>
<!-- 					<td align=left style="width: 100px; height: 15px;"><textarea -->
<!-- 							name="admin.note" class="textarea1" style="height: 105px;"></textarea></td> -->
							
					<td align=left style="width: 100px; height: 30px;"colspan="3"><input id="note" name="admin.note"
						style="width: 380px; height: 50px;" /></td>
					<td></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveUser()" iconcls="icon-save">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="javascript:$('#dlg').dialog('close')" iconcls="icon-cancel">取消</a>
	</div>
</body>

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
function add() {
    $("#dlg").dialog("open").dialog('setTitle', '添加用户');
    $("input:radio[name='admin.sex']".eq(0).attr("checked",'checked'));
    $("#fm").form("clear");
    
};
// $("input:radio[name='sex']").eq(0).attr("checked",'checked');
	var dataGrid;
	var dataLoader = {};
	var userInfo;	
	var roleData;
	var uid;
	var unitList;

	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
	var page_path = "${pageContext.request.contextPath}/authority/manager/";

	dataLoader.load = function() {
		params = {
			searchValue : $('#searchValue').val(),
			searchType : $('#searchType').val()
		};
		dataGrid = $('#dg')
				.datagrid(
						{
							url : page_path + 'search.do',
							queryParams : params,
							loadMsg : '正在加载，请稍后..',
							pagination : true,
							remoteSort : false,
							striped : true,
							fitColumns : true,
							pageSize : 10,
							pageList : [ 10, 20, 30, 40, 50 ],
							idField : 'id',
							selectOnCheck : $(this).is(':checked'),
							checkOnSelect : $(this).is(':checked'),
							toolbar : '#content',
							columns : [ [
									{
										field : 'ck',
										checkbox : true
									},
									{
										field : 'action',
										title : '操作',
										width : 250,
										align : 'center',
										formatter : function(value, row, index) {
											var str = '';
											if ($.canView) {
												str = '<a href="javascript:view('
														+ row.id + ');">查看</a>';
											}

											if ($.canEdit) {
												if ($.canView)
													str += "|";
												str += '<a href="javascript:edit('
														+ row.id + ');">修改</a>';
											}

											if ($.canDel) {
												if ($.canView || $.canEdit)
													str += "|";
												str += '<a href="javascript:delFunc('
														+ row.id
														+ ', \''
														+ row.usercode
														+ '\');">删除</a>';
											}

											if ($.canSetPwd) {
												if ($.canView || $.canEdit
														|| $.canDel)
													str += "|";
												str += '<a href="javascript:getManagerInfo('
														+ row.id
														+ ', \''
														+ row.username
														+ '\');">重置密码</a>';
											}

											if ($.canAssign) {
												if ($.canView || $.canEdit
														|| $.canDel
														|| $.canSetPwd)
													str += "|";
												str += '<a href="javascript:dispSetRole('
														+ row.id
														+ ');">分配角色</a>';
											}

											return str;
										}
									}, {
										field : 'id',
										title : '用户编号',
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

	$.getJSON(page_path + "getUnitList.do", {}, function(result) {
		unitList = result['unitList'];
	});

	function delFunc(id, usercode) {

		var currentUserId = '${session.user.id}';/*当前登录用户的ID*/
		if (currentUserId == id) {
			parent.$.messager.show({
				title : '提示',
				msg : '不可以删除自己！',
				showType : 'fade',
				style : {
					right : '',
					bottom : ''
				}
			});
			return;
		}

		$.messager.defaults = {
			ok : "确  定",
			cancel : "取  消"
		};
		$.messager.confirm('删除', '您确定要删除【' + usercode + '】吗？', function(r) {
			if (r) {
				$.ajax({
					url : page_path + 'delete.do?id=' + id,
					success : function(data) {
						try {
							ret = data;
							if (ret.err_code == 0) {
								dataLoader.load();
							} else {
								parent.$.messager.alert('删除', ret.err_msg,
										'error');
							}
						} finally {
							window.location.reload();
						}
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
			parent.$.messager.confirm('确认', '是否确定删除选择的多个用户？', function(r) {
				if (r) {
					parent.$.messager.progress({
						title : '提示',
						text : '数据处理中，请稍后....'
					});
					var currentUserId = '${session.user.id}';/*当前登录用户的ID*/
					var flag = false;
					for (var i = 0; i < rows.length; i++) {
						if (currentUserId != rows[i].id) {
							ids.push(rows[i].id);
						} else {
							flag = true;
						}
					}
					$.getJSON(page_path + 'batchDelete.do', {
						ids : ids.join(',')
					}, function(result) {
						if (result.err_code == 0) {
							parent.$.messager.show({
								title : '批量删除',
								msg : '删除成功',
								showType : 'fade',
								style : {
									right : '',
									bottom : ''
								}
							});
							dataLoader.load();
							dataGrid.datagrid('uncheckAll').datagrid(
									'unselectAll').datagrid('clearSelections');
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


	function view(id) {
		$.getJSON(page_path + 'viewAdministrator.do', {
			id : id
		}, function(result) {
			if (result.err_code == 0) {
				$('#viewform #org_').combobox({
					data: unitList,
					valueField: 'code',
					textField: 'name'
				});
				
				var user_info = result['infos'][0];
				$('#viewform #usercode').val(user_info.usercode);
				$('#viewform #username').val(user_info.username);
				$('#viewform #org_').combobox('setValue',user_info.unit);
				$('#viewform #sex').combobox('setValue',user_info.sex);
				$('#viewform #phone').val(user_info.phoneNum);
				$('#viewform #phone2').val(user_info.phoneNum2);
				$('#viewform #qq').val(user_info.qq);
				$('#viewform #email').val(user_info.email);
				$('#viewform #note').val(user_info.note);
				$('#viewform #username').attr("disabled",true);
				$('#viewform #phone').attr("disabled",true);
				$('#viewform #phone2').attr("disabled",true);
				$('#viewform #qq').attr("disabled",true);
				$('#viewform #email').attr("disabled",true);
				$('#viewform #note').attr("disabled",true);
				
				$('#administratorView').dialog({
					title:'查看用户',
					width: 550,																																																																																																																																																																	
					height: 330,
					modal:true,
					buttons: [{
						text:'关闭',
						iconCls:'icon-ok',
						handler:closeViewDlg
					}]
				});
																																																																																							
				
				$("#org_").combobox('disable');//可以取值为enable，代表启用
				$("#sex").combobox('disable');


			} else {
				parent.$.messager.alert('查看用户', ret.err_msg, 'error');
			}
		});
	};
	
	function edit(id) {
		$.getJSON(page_path + 'viewAdministrator.do', {
			id : id
		}, function(result) {
			if (result.err_code == 0) {
				$('#viewform #org_').combobox({
					data: unitList,
					valueField: 'code',
					textField: 'name'
				});
				
				var user_info = result['infos'][0];
				$('#viewform #id').val(user_info.id);
				$('#viewform #usercode').val(user_info.usercode);
				$('#viewform #username').val(user_info.username);
				$('#viewform #org_').combobox('setValue',user_info.unit);
				$('#viewform #sex').combobox('setValue',user_info.sex);
				$('#viewform #phone').val(user_info.phoneNum);
				$('#viewform #phone2').val(user_info.phoneNum2);
				$('#viewform #qq').val(user_info.qq);
				$('#viewform #email').val(user_info.email);
				$('#viewform #note').val(user_info.note);
				
				$('#viewform #org_').combo('readonly',true);
				$('#viewform #username').attr("disabled",false);
				$("#org_").combobox('enable');
				$('#sex').combobox("enable");
				$('#viewform #phone').attr("disabled",false);
				$('#viewform #phone2').attr("disabled",false);
				$('#viewform #qq').attr("disabled",false);
				$('#viewform #email').attr("disabled",false);
				$('#viewform #note').attr("disabled",false);
				
				$('#administratorView').dialog({
					title:'修改用户信息',
					width: 550,
					height:330,
					modal:true,
					buttons: [{
						text:'保存',
						iconCls:'icon-ok',
						handler:saveInfo,
					}, {
						text:'取消',
						iconCls:'icon-cancel',
						handler:closeViewDlg
					}]
				});
				
			
			} else {
				parent.$.messager.alert('修改用户', ret.err_msg, 'error');
			}
		});
	};

	function closeViewDlg() {
		$('#administratorView').dialog('close');
	}


	function report() {
		location.href = page_path + "export.do";
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
		pwdDlg.load(page_path + "password.do?method=set&id="+id+"&newPassword=123456");
		alert("重置密码成功！");
	};
	
	function saveInfo(){
		var isValid_userCode = $('#usercode').validatebox('isValid');
		var isValid_userName = $('#username').validatebox('isValid');
		var isValid_phoneNum = $('#phone').validatebox('isValid');
		var isValid_email = true;

// 		if ($('#email').val() != '') {
// 			isValid_email = $('#email').validatebox('isValid');
// 		}

// 		if (!isValid_userCode) {
// 			alert("用户名输入不合法！");
// 			return false;
// 		}
// 		if ( !isValid_userName ) {
// 			alert("用户名称输入不合法！");
// 			return false;
// 		}
// 		if ( !isValid_phoneNum ) {
// 			alert("电话号码输入不合法！");
// 			return false;
// 		}
// 		if ( !isValid_email) {
// 			alert("邮件格式输入不合法！");
// 			return false;
// 		}

			
		$('#viewform').ajaxSubmit({
			url: page_path + 'edit_info.do?method=save',
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
					$('#administratorView').dialog('close');
					dataLoader.load();
// 					$('#viewform').dialog('close');
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
	/**
		以下为权限相关函数
	*/
	
	var role_list;
	function dispSetRole(id) {
		$.getJSON(page_path + '"getRoles.do', {
			id : id
		}, function(data) {
			var result="";
			for(var i=0;i<data.length;i++){
				if(data[i].checked==true){
					result+="<input type=\"checkbox\" name=\"roleIds\" checked value=\""+data[i].id+"\">"+data[i].name+"</input>";
				}else{
					result+="<input type=\"checkbox\" name=\"roleIds\" value=\""+data[i].id+"\">"+data[i].name+"</input>";
				}
			}
			$('#do').dialog({
				title:'分配角色',
				width: 700,
				height:400,
				modal:true,
				closed: false   
			});
			$('#do #doid').val(id);
			$('#jj').html(result);
		});
	};
	
	function checkform(){
		$("input[name='roleIds']").each(function(){
			if($(this).is(":checked")){
				$("#jjjueseform").submit();
			}
		});
		alert("请至少选择一项！");
		return false;
	}
	
	
	function save(uid) {
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
				page_path + 'setRoles.do?id=' + uid, {
				user_role_ids : user_role_ids.join(',')
				}, function(result) {
					if (result.err_code == 0) {
						parent.$.messager.show({
							title:'权限角色',
							msg:'保存成功!',
							showType:'fade',
							style:{
								right:'',
								bottom:''
							}
						});
						$('#setRoleView').dialog('close');
						//$('#setRoleView').html('');
					} else {
						parent.$.messager.alert('权限角色', '权限角色失败。', 'error');
					}
				}
			);
	};
	function showRoleData(result) {
		$('#op_roles').empty();
        $.each(result['op_role_list'], function(i, op_role) {
             $('<option />').val(op_role['value']).text(op_role['text']).appendTo($('#op_roles'));
        });
         
		$('#data_roles').empty();
        $.each(result['data_role_list'], function(i, data_role) {
            $('<option />').val(data_role['value']).text(data_role['text']).appendTo($('#data_roles'));
        });

	}
	
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
//	});



function cancel() {
	setRoleDlg.dialog('close');
	setRoleDlg.html('');
}
	var d = new Date();
	var s = d.getSeconds();
	var cur = d.getMilliseconds();
	dataLoader.load();
	
	  function saveUser() {
          $("#fm").form("submit", {
              url: '<%=basePath%>authority/manager/insert.do',
              onsubmit: function () {
                  return $(this).form("validate");
              },
              success: function (result) {
            	if(result==0){
            		  $.messager.alert("提示信息", "添加用户成功!");
                      $("#dlg").dialog("close");
                      $('#dg').datagrid('load');
            	}else{
            		 $.messager.alert("提示信息", "操作失败，有相同的用户名!");
            	}	  
              }
          });
      }
	  
	  function excel(){
			location ="<%=basePath%>excel/listAdminToExcel.do?searchValue="+$('#searchValue').val()+"&searchType="+ $('#searchType').val();
		}	  
	  
	  $.extend($.fn.validatebox.defaults.rules, {  
		  selectValueRequired: {  
		  validator: function(value,param){  
		  return $(param[0]).find("option:contains('"+value+"')").val() != '';  
		  },  
		  message: 'select value required.'  
		  }  
		  }); 
	  
	</script>
</html>
