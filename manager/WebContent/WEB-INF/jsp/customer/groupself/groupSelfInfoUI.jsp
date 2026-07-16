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

<title>集团自助管理</title>

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

	function aa(){
	
		    var conditionSelect = $('#conditionSelect').combobox('getValue');
		    var conditionInput = $('#conditionInput').val();
		    //var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
		    $('#dg').datagrid('load',{
		      conditionSelect :conditionSelect,
		      conditionInput  :conditionInput,
		    });
			
		}	
		

		
	var datagrid;
	$(function(){
			datagrid=$('#dg').datagrid({		
	
							url	:'<%=basePath%>customer/findGroupUserList.do?id=${groupid}',
							method : 'POST',
							title : ' ',
							iconCls : ' ',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							fit : true,
							//width:900,
							hight:1400,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
							singleSelect:true,
							striped : true,
							columns : [ [
									{
										field : 'bianhao',
										title : '编号',
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
  										str += '<a href="customer/showGroupSelfUserInfoUI.do?id='+row.id+'" target="_self">查看</a> &nbsp';
  										str += '<a href="customer/removeUserFromGroup.do?groupid=${groupid}&id='+row.id+'" target="_self">移除</a> &nbsp';
  										str += '<a href=""  onclick="reallocateProfitUI('+row.id+','+row.activeway_as_driver+','+row.integer_as_driver+','+row.ratio_as_driver+');return false">更改分成比例</a> &nbsp';
  										//str += '<a href="driverVerifiedUI.action?id='+row.id+'" target="_blank">认证车主</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}, {
										field : 'id',
										title : 'ID',
										width : 100,
									//checkbox : true
									}, {
										field : 'usercode',
										title : '用户名',
										width : 100
									}, {
										field : 'username',
										title : '姓名',
										width : 100,
										align : 'right'
									}, {
										field : 'phone',
										title : '手机号',
										width : 100,
										align : 'right'
									}, {
										field : 'reallocateProfit',
										title : '分成比例',
										width : 100,
										align : 'right'
									}
									] ],
							toolbar : '#toolbar'

						}

				);

		var conditionSelect;

		conditionSelect = $('#conditionSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'usercode',
			data : [ {
				label : '按用户名查询',
				value : 'usercode'
			}, {
				label : '按姓名查询',
				value : 'username'
			}, {
				label : '按手机号查询',
				value : 'phone'
			}, {
				label : '按id查询',
				value : 'id'
			} ]

		});
		
		$.ajax({
		url : '<%=basePath%>customer/findGroupSelfInfo.do',
			data : 'id=${groupid}',
			dataType : "json",
			success : function(jsonObject) {
				console.info(jsonObject);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#groupid').val(jsonObject.groupid);
				$('#group_name').val(jsonObject.group_name);
				$('#create_date').val(jsonObject.create_date);
				$('#linkname').val(jsonObject.linkname);
				$('#group_property').val(jsonObject.group_property);
				$('#linkphone').val(jsonObject.linkphone);
				$('#contract_no').val(jsonObject.contract_no);
				$('#fix_phone').val(jsonObject.fix_phone);
				$('#email').val(jsonObject.email);
				$('#fax').val(jsonObject.fax);
				$('#group_address').val(jsonObject.group_address);
				$('#sign_time').val(jsonObject.sign_time);
				$('#invitecode_self').val(jsonObject.invitecode_self);
				if(jsonObject.active_as_driver_self==0){
				$('#radioSelect').combobox('setValues', '0');
				$('#radioSelectInput').val(jsonObject.integer_as_driver_self);
				}else{
				$('#radioSelect').combobox('setValues', '1');
				$('#radioSelectInput').val(jsonObject.ratio_as_driver_self);
				}
				$('#remark').val(jsonObject.remark);
			}
		});

 		$('#radioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_drivr_self',
			data : [ {
				label : '%',
				value : '0'
			}, {
				label : '点',
				value : '1'
			} ]

		}); 
		

	});
	
 	function setReallocateProfit(){
		//alert("set");
		$.ajax({
			type : 'POST',
			url : '<%=basePath%>customer/editReallocateProfit.do',
		   	data:$('#reallocateProfitForm').serialize(),
		   	success: function(resultObject){
		   		
	 			   if(resultObject.returnCode>0){
	 				   console.info("returnCode=="+resultObject.returnCode);
					    $.messager.alert('提示',resultObject.msg,function(){


					    });  
				   }else{
					   
					    $.messager.alert('提示',resultObject.msg,'info',function(){
					    	$('#setReallocateProfit').dialog('close');
							$('#dg').datagrid('reload');
					    });
				   } 
			    	$('#setReallocateProfit').dialog('close');
					$('#dg').datagrid('reload');

		   }
		});
/* 		$('#setReallocateProfit').dialog('close');
		$('#dg').datagrid('reload'); */
	
	}
	
	function setReallocateProfitDefault(){
		//alert("default");
		$.ajax({
			type : 'POST',
			url : '<%=basePath%>customer/editReallocateProfitDefault.do?id=${groupid}',
		   	data:$('#reallocateProfitForm').serialize(),
		   	success: function(data){
	 			   if(resultObject.returnCode>0){
					    $.messager.alert('提示','修改成功！','info',function(){
					    	$('#setReallocateProfit').dialog('close');
							$('#dg').datagrid('reload');
					    });  
				   }else{
					   
					    $.messager.alert('提示',resultObject.msg,'info',function(){
					    	window.history.go(-1);
					    });
				   } 
/*  		   	console.info($('#reallocateProfitForm').serialize());
 		   $.messager.alert('提示','修改成功！','info',function(){
		    	window.history.go(-1);
		    	
		    });	 */
 		   	
		     //alert( "修改成功！ "  ); 
		   }
		});		
		$('#setReallocateProfit').dialog('close');
		$('#dg').datagrid('reload');
	}
	
	function reallocateProfitUI(id,activeway_as_driver,integer_as_driver,ratio_as_driver) {

			$('#rpRadioSelect').combobox({
					url : '',
					valueField : 'value',
					textField : 'label',
					value : 'activeway_as_driver',
					data : [ {
						label : '%',
						value : '0'
					}, {
						label : '点',
						value : '1'
					} ]
		
				});
				//alert(id+"---"+active_as_driver_self+"---"+integer_as_driver_self+"----"+ratio_as_driver_self);
				$("#setReallocateProfit").dialog("open").dialog('setTitle', '更改分成比例');
				$("#reallocateProfitForm").form("clear");
				if (activeway_as_driver == 0) {
					$('#rpRadioSelect').combobox('setValues', '0');
					$('#rpRadioSelectInput').val(ratio_as_driver);
					$('#userid').val(id);
				} else {
					$('#rpRadioSelect').combobox('setValues', '1');
					$('#rpRadioSelectInput').val(integer_as_driver);
					$('#userid').val(id);
				}

	}

	function saveGroupInfo() {
		$.ajax({
			type : 'POST',
			url : '<%=basePath%>customer/updateGroupSelfInfo.do?id=${groupid}',
		   data:$('#groupInfoForm').serialize(),
		   success: function(data){
 		   console.info($('#groupInfoForm').serialize());
		     //alert( "修改成功！ "  ); 
		     
		   $.messager.alert('提示','保存成功！','info',function(){
		    	window.history.go(-1);
		    	
		    });	
		   }
		});
        }
        
        

        

	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>

<body class="easyui-layout" onload="loaddone()">


	<div data-options="region:'north'"
		style="width: 100%; height: 120px; background: #eee;">
		<form id=groupInfoForm>
			<table border=0>
				<tr align="center">
					<td align=right><font
						style="font-weight: bold; font-size: 80%">集&nbsp;团&nbsp;标&nbsp;识：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="groupid" name="groupid" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">集团名称：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="group_name" name="group_name"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 100%; height: 20px">
					</td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">集团性质：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="group_property" name="group_property"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 100%; height: 20px">
					</td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">合同编号：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="contract_no" name="contract_no"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 100%; height: 20px">
					</td>

				</tr>
				<tr align="center">
					<td align=right><font
						style="font-weight: bold; font-size: 80%">签约时间：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="sign_time" name="sign_time" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">邀请码：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="invitecode_self" name="invitecode_self"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 100%; height: 20px">
					</td>

					<td align=right><font
						style="font-weight: bold; font-size: 80%">联系人：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="linkname" name="linkname" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">联系电话：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="linkphone" name="linkphone" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
				</tr>
				<tr align="center">

					<td align=right><font
						style="font-weight: bold; font-size: 80%">所在地址：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="group_address" name="group_address"
						class="easyui-validatebox textbox" disabled="disabled"
						data-options="required:true" style="width: 100%; height: 20px">
					</td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">固定电话：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="fix_phone" name="fix_phone" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">Email：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="email" name="email" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">传真号码：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						id="fax" name="fax" class="easyui-validatebox textbox"
						disabled="disabled" data-options="required:true"
						style="width: 100%; height: 20px"></td>
				</tr>
				<!-- 				<tr align="center">

				</tr> -->

				<tr align="center">
					<td align=right><font
						style="font-weight: bold; font-size: 80%">默认分成比例：</font></td>
					<td style="width: 100px; height: 15px;"><input type="text"
						style="width: 30%;" id="radioSelectInput" name="radioSelectInput">
						<input style="width: 50%;" id="radioSelect" name="radioSelect">
					</td>
					<td></td>
					<td></td>
					<td></td>

					<td align=right colspan="2"><a href="javascript:void(0)"
						class="easyui-linkbutton" iconcls="icon-save"
						onclick="saveGroupInfo()" plain="true">保存基本信息</a></td>
				</tr>



			</table>

		</form>
	</div>
	<div data-options="region:'center'">
		<table id="dg"></table>
	</div>
	<div id="toolbar">
		<form id="searchForm" class="">
			<table>
				<tr>
					<td><input id="conditionSelect" name="conditionSelectName"
						value=""> <input id="conditionInput"
						name="conditionInputName"> <a id="btn"
						class="easyui-linkbutton" data-options="iconCls:'icon-search'"
						onclick="aa()">查询</a></td>

				</tr>

			</table>

		</form>

	</div>

	<div id="setReallocateProfit" class="easyui-dialog"
		style="width: 600px; height: 380px; padding: 10px 20px; top: 30px"
		closed="true" buttons="#setReallocateProfit-buttons">

		<form id="reallocateProfitForm" method="post">
			<table>
				<tr align=right>
					<td align=right><label> 更改为： </label><input type="hidden"
						id="userid" name="userid"></td>
				</tr>
				<tr>
					<td><input id="rpRadioSelectInput" name="rpRadioSelectInput"
						class="easyui-validatebox" required="true"></td>
					<td><input id="rpRadioSelect" name="rpRadioSelect"
						class="easyui-validatebox" required="true" /></td>
				</tr>

			</table>
		</form>
	</div>
	<div id="setReallocateProfit-buttons">
		<a href="#" class="easyui-linkbutton"
			onclick="setReallocateProfit();return false;" iconcls="icon-save">确定</a> <a
			href="#" class="easyui-linkbutton"
			onclick="setReallocateProfitDefault();return false;" iconcls="icon-save">恢复默认</a>
	</div>
	</div>
</body>
</html>
