<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>集团联盟列表</title>

<link rel="stylesheet" type="text/css"
	href="js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css" href="js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css" href="js/themes/icon.css" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>

<script type="text/javascript" charset="UTF-8">

/* 	function aa(){
	
		    var conditionSelect = $('#conditionSelect').combobox('getValue');
		    var conditionInput = $('#conditionInput').val();
		    var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
		    $('#dg').datagrid('load',{
		      conditionSelect :conditionSelect,
		      conditionInput  :conditionInput,
		      verifiedStaus   :verifiedStaus
		    });
			
		} */	
		
		
	var datagrid;
	$(function(){
	datagrid=$('#dg').datagrid({		
	
							url	:'<%=basePath%>customer/groupAssociationPageList.do',
							method : 'POST',
							title : '',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							fit:true,
							//hight:800,
							width:950,
							fitColumns:true,
							nowrap : false,
							autoRowHeight:false,
							border : true,
							rownumbers:true,
							idFiled : 'ID',
							checkOnSelect : true,
							singleSelect:true,
							striped : true,
							toolbar : '#content',
							columns : [ [
									/* {
										field : 'id',
										title : '编号',
										width : 100,
										checkbox : true
									}, */{
										field : 'action',
										title : '操作',
										width : 300,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
  										str += '<a href="customer/showGroupAssociationUI.do?id='+row.id+'" target="_self">查看</a> &nbsp';
  										str += '<a href="customer/updateGroupAssociationInfoUI.do?id='+row.id+'" target="_self">修改基本信息</a> &nbsp';
  										str += '<a href="customer/addMemberUI.do?id='+row.id+'" target="_self">成员调整</a> &nbsp';
  										str += '<a id="aa" href="" onclick="delGroupAssociation('+row.id+','+row.memberCount+','+index+');return false">删除</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}, {
										field : 'ga_code',
										title : '集团标识',
										width : 200,
									}, {
										field : 'ga_name',
										title : '集团名称',
										width : 200
									}, {
										field : 'group_property',
										title : '性质',
										width : 200,
										align : 'left'
									}
									] ],
							//toolbar : '#atoolbar'
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

						}

				);

/* 		var conditionSelect;

		conditionSelect = $('#conditionSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'ga_code',
			data : [ {
				label : '按集团标识查询',
				value : 'ga_code'
			}, {
				label : '按集团名称查询',
				value : 'ga_name'
			} ]

		}); */
		
		

	});
	
	function doSearch(value, name) {
		var conditionInput = $('#ss').searchbox('getValue');
		var conditionSelect = $('#ss').searchbox('getName');
		$('#dg').datagrid('load',{
			conditionInput:conditionInput,
			conditionSelect:conditionSelect
		});
	}	
	
	    function newGroupAssociation() {
            $("#dlg").dialog("open").dialog('setTitle', '新建集团客户'); ;
            $("#fm").form("clear");
<%--             url = "<%=basePath%>newGroupAssociation.action";
            document.getElementById("hidtype").value="submit"; --%>
        }
	


	  function edituser() {
            var row = $("#dg").datagrid("getSelected");
            if (row) {
                $("#dlg").dialog("open").dialog('setTitle', 'Edit User');
                $("#fm").form("load", row);
            /*     url = "UserManage.aspx?id=" + row.ID; */
            }
        }
        function saveGroupAssociation() {
            $("#fm").form("submit", {
                url: '<%=basePath%>customer/saveGroupAssociation.do',
                onsubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
/*                     if (result == "1") {
                        $.messager.alert("提示信息", "操作成功");
                        $("#dlg").dialog("close");
                        $("#dg").datagrid("load");
                    }
                    else {
                        $.messager.alert("提示信息", "操作失败");
                    }*/
                 $.messager.alert("提示信息", "操作成功");
                 	$('#dg').datagrid('reload');
                    $("#dlg").dialog("close");
                } 
            });
        }
        function destroyUser() {
            var row = $('#dg').datagrid('getSelected');
            if (row) {
                $.messager.confirm('Confirm', 'Are you sure you want to destroy this user?', function (r) {
                    if (r) {
                        $.post('destroy_user.php', { id: row.id }, function (result) {
                            if (result.success) {
                                $('#dg').datagrid('reload');    // reload the user data 
                            } else {
                                $.messager.show({   // show error message 
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            }
                        }, 'json');
                    }
                });
            }
        }  
        
        
     function delGroupAssociation(id,memberCount,index){
      		//console.info(index);
        	if(memberCount==0){
       			$.ajax({
		   		type: 'POST',
		   		url: '<%=basePath%>customer/delGroupAssociation.do',
		   		data:'id='+id,
		   		success: function(data){
		   		$.messager.alert("提示信息", "删除成功");
		   			$('#dg').datagrid('deleteRow',index);
 		   			//console.info($('#App_Spread_UnitInfoForm').serialize());
		     	//alert( "删除成功！ "  ); 
		   		}
			});
        	}else{
       	 		alert("无法删除旗下有集团客户的集团联盟");
        	}
        }
        
		function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		} 
</script>
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
						<td><input id="ss" class="easyui-searchbox"
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
		<div data-options="name:'0'">全部查询</div>
		<div data-options="name:'ga_code'">按集团标识查询</div>
		<div data-options="name:'ga_name'">按集团名称查询</div>
	</div>
	<!-- 	<form id="hiddenForm" method="post">
		<input id="searchValue" type="hidden" /> <input id="searchType"
			type="hidden" />
	</form> -->
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a> <a href="#"
				class="easyui-linkbutton" iconCls="icon-add" plain="true"
				style="float: right" onclick="newApp_Spread_Unit(); return false;"><font
				style="font-weight: bold"
				onclick="newGroupAssociation();return false;">新集团联盟&nbsp;</font></a>

		</div>
	</div>
	<!-- <div class="easyui-layout"  data-options="fit:true">
	<div data-options="region:'north'" style="height:30px;" id="atoolbar" class="datagrid-toolbar">
	
			<input id="conditionSelect" name="conditionSelectName" value="sdfsf">
			<input id="conditionInput" name="conditionInputName" >
			<a id="btn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="aa()">查询</a>
					<a href="javascript:void(0)"
					class="easyui-linkbutton" iconcls="icon-add" style="float:right" onclick="newGroupAssociation()"
					plain="true" style="float:right;">新建联盟</a>
	</div>
	<div data-options="region:'center'">
		<div id="atoolbar" class="datagrid-toolbar" >
		<a href="javascript:void(0)"
					class="easyui-linkbutton" iconcls="icon-add" onclick="newGroupAssociation()"
					plain="true" style="float:right;">新建联盟</a>
		<div style="margin-bottom:5px">
		<form id="searchForm" class="">
			<table>
				<tr>
					<td>
						 <input id="conditionSelect" name="conditionSelectName" value="sdfsf">
						 <input id="conditionInput" name="conditionInputName" >
						 <a id="btn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="aa()">查询</a>
					</td>
						         
				</tr>
				<tr align =right>
				<td align=right>
				<a href="javascript:void(0)"
					class="easyui-linkbutton" iconcls="icon-add" onclick="newGroupAssociation()"
					plain="true" style="float:right;">新建联盟</a>
				</td>
				</tr>
			</table>
			
		</form> 
		</div>
	<table id="dg"></table>
		</div> -->


	<div id="dlg" class="easyui-dialog"
		style="width: 600px; height: 380px; padding: 10px 20px; top: 30px"
		closed="true" buttons="#dlg-buttons">

		<form id="fm" method="post">
			<table>
				<tr align=right>
					<td align=right><label> 集团标识： </label> <input id="ga_code"
						name="ga_code" class="easyui-validatebox" required="true" /></td>
					<td align=right><label> 合同编号：</label> <input id="contract_no"
						name="contract_no" class="easyui-validatebox" /></td>
				</tr>
				<tr align=right>
					<td align=right><label> 集团名称： </label> <input id="ga_name"
						name="ga_name" class="easyui-validatebox" /></td>
					<td align=right><label> 集团性质：</label> <input
						id="group_property" name="group_property"
						class="easyui-validatebox" /></td>
				</tr>
				<tr align=right>
					<td align=right><label> 联系人： </label> <input id="linkname"
						name="linkname" class="easyui-validatebox" /></td>
					<td align=right><label> 联系电话：</label> <input id="linkphone"
						name="linkphone" class="easyui-validatebox" /></td>
				</tr>
				<tr align=right>
					<td align=right><label> 固定电话： </label> <input id="fix_phone"
						name="fix_phone" class="easyui-validatebox" /></td>
					<td align=right><label> Email：</label> <input id="email"
						name="email" class="easyui-validatebox" /></td>
				</tr>
				<tr align=right>
					<td align=right><label> 传真号码： </label> <input id="fax"
						name="fax" class="easyui-validatebox" /></td>
					<td align=right><label> 集团地址：</label> <input
						id="group_address" name="group_address" class="easyui-validatebox" />
					</td>
				</tr>
				<tr align=right>
					<td align=right><label> 签约时间： </label> <input id="sign_time"
						name="sign_time" class="easyui-validatebox" /></td>
					<!-- 					<td align=right>默认分成比例：</td><td align=left style="width:100px; height:15px;"><input type="text" id="radioSelectInput"
						name="radioSelectInput" style="width:75px;"><input id="radioSelect" name="radioSelect" style="width:75px;">
						
					</td> -->
				</tr>
				<tr align=right>
					<td align=right><label> 备注：</label> <input id="desc_"
						name="desc_" class="easyui-vlidatebox" /></td>
				</tr>

			</table>
		</form>
	</div>


	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveGroupAssociation()" iconcls="icon-save">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="javascript:$('#dlg').dialog('close')" iconcls="icon-cancel">取消</a>
	</div>



</body>
</html>
