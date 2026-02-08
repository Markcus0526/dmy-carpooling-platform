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

<title>合作单位列表</title>

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
		    //console.info($('#conditionSelect').combobox('getValue'));
		    console.info(conditionSelect+"**"+conditionInput+"**"+verifiedStaus);
		    $('#dg').datagrid('load',{
		      conditionSelect :conditionSelect,
		      conditionInput  :conditionInput,
		      verifiedStaus   :verifiedStaus
		    });
			
		} */	
		
		$(function(){
			$('#btn').keyup(function(event) {
				if (event.keyCode == '13') {
					aa();
				}
			});
		});
		
	var datagrid;
	$(function(){
	datagrid=$('#dg').datagrid({		
	
							url	:'<%=basePath%>customer/app_Spread_UnitPageList.do',
							method : 'POST',
							title : '',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							fit : true,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'id',
							checkOnSelect : true,
							singleSelect:true,
							striped : true,
							toolbar : '#content',
							columns : [ [
									{
										field : 'id',
										title : 'id',
										width : 100,
										checkbox : true
									},
									{
										field : 'action',
										title : '操作',
										width : 220,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
  										str += '<a href="customer/showApp_Spread_UnitUI.do?id='+row.id+'" target="_self">查看</a> &nbsp';
  										str += '<a href="customer/updateApp_Spread_UnitInfoUI.do?id='+row.id+'" target="_self">修改</a> &nbsp';
  										str += '<a id="aa" href="" onclick="delApp_Spread_Unit('+row.id+','+row.balance+','+index+');return false">删除</a> &nbsp';
  										str += '<a href="customer/ASURebateInfoUI.do?id='+row.id+'" target="_self">返利信息</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}, {
										field : 'unit_id',
										title : '集团标识',
										width : 100,
									//checkbox : true
									}, {
										field : 'name',
										title : '合作单位名称',
										width : 100
									}, {
										field : 'linkname',
										title : '联系人',
										width : 100,
										align : 'right'
									}, {
										field : 'linkphone',
										title : '联系电话',
										width : 100,
										align : 'right'
									}
									] ],
							//toolbar : '#toolbar'
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
			value : 'name',
			data : [ {
				label : '按合作单位标识查询',
				value : 'id'
			}, {
				label : '按合作单位名称查询',
				value : 'name'
			} ]

		}); */
		
			 $('#driverRatioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_drivr_self',
			data : [ {
				label : '点',
				value : '0'
			}, {
				label : '%',
				value : '1'
			} ]

		});
		 
	 $('#passengerRatioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_passenger_self',
			data : [ {
				label : '点',
				value : '0'
			}, {
				label : '%',
				value : '1'
			} ]

		}); 

	});
	
	    function newApp_Spread_Unit() {
            $("#dlg").dialog("open").dialog('setTitle', '新建合作单位'); ;
            $("#fm").form("clear");
            <%-- url = "<%=basePath%>saveApp_Spread_Unit.action"; --%>
            //document.getElementById("hidtype").value="submit";
        }
	

	  function edituser() {
            var row = $("#dg").datagrid("getSelected");
            if (row) {
                $("#dlg").dialog("open").dialog('setTitle', 'Edit User');
                $("#fm").form("load", row);
                //url = "UserManage.aspx?id=" + row.ID;
            }
        }
        function saveApp_Spread_Unit() {
            $("#fm").form("submit", {
                url: '<%=basePath%>customer/saveApp_Spread_Unit.do',
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
                    $("#dlg").dialog("close");
                    $('#dg').datagrid('load');
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
        

        
      function delApp_Spread_Unit(id,balance,index){
      console.info("balance:"+balance);
        	if(balance==0||balance==undefined){
       			$.ajax({
		   		type: 'POST',
		   		url: '<%=basePath%>customer/delApp_Spread_Unit.do',
		   		data:'id='+id,
		   		success: function(data){
		   		$.messager.alert('提示','修改成功！','info',function(){
		    	//window.history.go(-1);
		    		console.info($('#App_Spread_UnitInfoForm').serialize());
		    		$('#dg').datagrid('deleteRow',index);
		    			});
		   		}
			});
        	}else{
       	 		$.messager.alert('提示','该合作单位虚拟账户余额不为0无法删除','warning');
        	}
        }
      
      function loaddone() {
  		var hiddenmsg = parent.document.getElementById("hiddenmsg");
  		hiddenmsg.style.display = "none";
  	}
      
  	function doSearch(value, name) {
		var conditionInput = $('#ss').searchbox('getValue');
		var conditionSelect = $('#ss').searchbox('getName');
		$('#dg').datagrid('load',{
			conditionInput:conditionInput,
			conditionSelect:conditionSelect
		});
	}      
</script>
</head>

<body onload="loaddone()">

	<!-- 	<table id="dg"></table>

	<div id="toolbar">
<div>
						
						
						 <input id="conditionSelect" name="conditionSelectName" value="sdfsf">
						 <input id="conditionInput" name="conditionInputName" >
						 <a id="btn" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="aa()">查询</a>
				
				<a href="javascript:void(0)"
					class="easyui-linkbutton" style="float:right" iconcls="icon-add" onclick="newApp_Spread_Unit()"
					plain="true">添加</a>
	
</div>
	</div> -->

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
		<div data-options="name:'unit_id'">按合作单位标识查询</div>
		<div data-options="name:'name'">按合作单位名称查询</div>
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
				style="font-weight: bold">新建合作单位&nbsp;</font></a>

		</div>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 800px; height: 500px; padding: 10px 20px; top: 30px"
		closed="true" buttons="#dlg-buttons">

		<form id="fm" method="post">
			<table border=0>
				<tr align="center">
					<td align=left>合作单位标识：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="unit_id" name="unit_id" class="easyui-validatebox"
						required="true"></td>
					<td align=left>合同编号：</td>
					<td align=left><input type="text" id="contract_no"
						name="contract_no" class="easyui-validatebox" required="true">
					</td>
					<td></td>
					<td></td>

				</tr>
				<tr align="center">

					<td align=left>合作单位名称：</td>
					<td align=left><input type="text" id="name" name="name"
						class="easyui-validatebox" required="true"></td>
					<td align=left>签约时间：</td>
					<td align=left><input type="text" id="create_date"
						name="create_date" class="easyui-validatebox" required="true">
					</td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>合作单位性质：</td>
					<td align=left><input type="text" id="group_property"
						name="group_property"></td>
					<td align=left>联系电话：</td>
					<td align=left><input type="text" id="linkphone"
						name="linkphone"></td>
					<td></td>
					<td></td>

				</tr>
				<tr align="center">
					<td align=left>联&nbsp;&nbsp;系&nbsp;&nbsp;人：</td>
					<td align=left><input type="text" id="linkname"
						name="linkname"></td>
					<td align=left>Email：</td>
					<td align=left><input type="text" id="email" name="email"
						class="easyui-validatebox"validType:'email'></td>
					<td></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>固定电话：</td>
					<td align=left><input type="text" id="fix_phone"
						name="fix_phone"></td>
					<td>集团地址：</td>
					<td align=left><input type="text" id="group_address"
						name="group_address"></td>
					<td></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>传真号码：</td>
					<td align=left><input type="text" id="fax" name="fax">
					</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<tr align="center">
					<td align=left>邀&nbsp;&nbsp;请&nbsp;&nbsp;码：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="invite_code" name="invite_code"></td>
					<td></td>
					<td></td>
				</tr>

				<tr>
					<td>乘客方返利：</td>
					<td><input id="driverRatioSelectInput"
						name="driverRatioSelectInput" style="width: 50px;"> <input
						id="driverRatioSelect" name="driverRatioSelect"
						style="width: 50px;"></td>
					<td>乘客方有效期：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="limit_month_as_passenger_self"
						name="limit_month_as_passenger_self" style="width: 40px;">&nbsp;个月</td>
					<td>乘客方有效次数：</td>
					<td align=left><input id="limit_count_as_passenger_self"
						name="limit_count_as_passenger_self" style="width: 40px;">&nbsp;次</td>
				</tr>
				<tr>
					<td>车主方返利：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="passengerRatioSelectInput" name="passengerRatioSelectInput"
						style="width: 50px;"> <input id="passengerRatioSelect"
						name="passengerRatioSelect" style="width: 50px;"></td>
					<td>车主方有效期：</td>
					<td align=left style="width: 100px; height: 15px;"><input
						id="limit_month_as_driver_self" name="limit_month_as_driver_self"
						" style="width: 40px;">&nbsp;个月</td>
					<td>车主方有效次数：</td>
					<td align=left><input id="limit_count_as_driver_self"
						name="limit_count_as_driver_self" style="width: 40px;">&nbsp;次</td>
				</tr>
				<tr>
					<td align=left>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td colspan=5><textarea id="remark" name="remark" rows="2"
							cols="75" wrap="hard"></textarea></td>

				</tr>


			</table>
		</form>
	</div>


	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveApp_Spread_Unit()" iconcls="icon-save">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="javascript:$('#dlg').dialog('close')" iconcls="icon-cancel">取消</a>
	</div>

</body>
</html>
