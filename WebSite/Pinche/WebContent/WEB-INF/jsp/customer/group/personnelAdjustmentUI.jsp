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

<title>人员调整</title>

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
// 		    var gid = $('#gid').val();
		    //console.info($('#conditionSelect').combobox('getValue'));
		   // console.info(conditionSelect+"**"+conditionInput+"**"+verifiedStaus);
		    $('#dg').datagrid('load',{
		      conditionSelect :conditionSelect,
		      conditionInput  :conditionInput,
		      //verifiedStaus   :verifiedStaus,
// 		      gid:gid
		    });
		    //alert("has benn here.");
			
		}	
		

		
	var datagrid;
	$(function(){
	datagrid=$('#dg').datagrid({		
	
							url	:'<%=basePath%>customer/findGroupDriversByCondition.do?gid=${gid}',
							method : 'POST',
							title : '列表',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							fit : true,
							//pagePosition:'both',
							//width:'auto',
							//height:'auto',
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
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
  										str += '<a href="customer/showGroupUserUI.do?id='+row.id+'" target="_self">查看</a> &nbsp';
  										str += '<a href="" onclick="removeDriver('+row.id+','+index+');return false">移除</a> &nbsp';
  										str += '<a href=""  onclick="reallocateProfitUI('+row.id+','+row.activeway_as_driver+','+row.integer_as_driver+','+row.ratio_as_driver+');return false">更改分成比例</a> &nbsp';
  										//str += '<a href="driverVerifiedUI.action?id='+row.id+'" target="_blank">认证车主</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}, {
										field : 'id',
										title : 'id',
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
										title : '手机号码',
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
		
		
			$('#searchGroup').keyup(function(event) {
				if (event.keyCode == '13') {
					aa();
				}
			});

	});
	
	    function addDriver() {
	   //alert("addDriver");
	    window.location.href="<%=basePath%>customer/addDriverUI.do?gid=${gid}";
        }
	


	  function edituser() {
            var row = $("#dg").datagrid("getSelected");
            if (row) {
                $("#dlg").dialog("open").dialog('setTitle', 'Edit User');
                $("#fm").form("load", row);
            }
        }
<%--         function saveGroup() {
            $("#fm").form("submit", {
                url: '<%=basePath%>saveGroup.action',
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
                } 
            });
        } --%>
function removeDriver(id,index){
	$.messager.confirm('删除', '您确定要删除吗？', function(r) {
		if (r) {
			$.ajax({
			    type: 'POST',
			    url: '<%=basePath%>customer/removeDriverFromGroup.do',
			    data:'id='+id+'&gid=${gid}',
				dataType:'json',
				success:function(resultObject){
					   if(resultObject.returnCode>0){
				 	    $.messager.alert('提示',resultObject.msg,'info',function(){
					    	//window.history.go(-1);
						   $('#dg').datagrid('deleteRow',index);
					    });   
				   }else{
					    $.messager.alert('提示',resultObject.msg,'info',function(){
					    	window.history.go(-1);
					    });
				   } 
				}
			});
		} else {
		}
	});
} 

 	function setReallocateProfit(){
 		var isValid_rpRadioSelectInput = $('#rpRadioSelectInput').validatebox('isValid');
 		if(isValid_rpRadioSelectInput==true){
 			$.ajax({
 				type : 'POST',
 				url : '<%=basePath%>customer/editGroupReallocateProfit.do',
 			   	data:$('#reallocateProfitForm').serialize(),
 			    dataType:'json',
 			    success:function(resultObject){
 	 			   if(resultObject.returnCode>0){
 					    $.messager.alert('提示',resultObject.msg,'info',function(){
 					    	//window.history.go(-1);
 					    	$('#setReallocateProfit').dialog('close');
 							$('#dg').datagrid('reload');
 					    });  
 				   }else{
 					   
 					    $.messager.alert('提示',resultObject.msg,'info',function(){
 					    	window.history.go(-1);
 					    });
 				   } 
 			   }
 			});
 		}
 		return false;
	}
	
 	
	function setReallocateProfitDefault(){
		//alert("default");
		$.ajax({
			type : 'POST',
			url : '<%=basePath%>customer/editGroupReallocateProfitDefault.do?id=${gid}',
		   	data:$('#reallocateProfitForm').serialize(),
		    dataType:'json',
		    success:function(resultObject){
 			   if(resultObject.returnCode>0){
				    $.messager.alert('提示',resultObject.msg,'info',function(){
				    	//window.history.go(-1);
				    	$('#setReallocateProfit').dialog('close');
						$('#dg').datagrid('reload');
				    });  
			   }else{
				   
				    $.messager.alert('提示',resultObject.msg,'info',function(){
				    	window.history.go(-1);
				    });
			   } 
		   }
			/*success: function(data){
		    $.messager.alert('提示','修改成功！','info',function(){
		    	//window.history.go(-1);
		    	$('#setReallocateProfit').dialog('close');
				$('#dg').datagrid('reload');
		    });	
		   } */
		});		
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
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
	
	function excel(){
	    var conditionSelect = $('#conditionSelect').combobox('getValue');
	    var conditionInput = $('#conditionInput').val();
	    var gid = $('#gid').val();
	    location = "<%=basePath%>excel/listGroupToExcel.do?conditionSelect="+conditionSelect+"&conditionInput="+conditionInput+"&gid="+gid;
	}
	
	$.extend($.fn.validatebox.defaults.rules, {
		TimeRange: {
		validator: function(value){
			if((value>0)&&(value<=100))
				return true;
			return false;
		},
		message: '请输入1~100的正整数！'
		}
	});
</script>
</head>

<body onload="loaddone()">

	<!--   <div style="color:#00FF00;width:1000px;hight:900px;margin-bottom:20px" >

</div>  -->
	<table id="dg"></table>
	<div id="toolbar">
		<form id="searchForm" class="">

			<table border=0>
				<tr>

					<td align=right style="width: 100px"><label>集&nbsp;团&nbsp;标&nbsp;识：</label></td>
					<td align=left><input id="groupid" name="groupid"
						value="${groupid}" style="disable: true"
						class="easyui-validatebox" width="100px" readOnly="readonly" /></td>
					<td align=left><label>集&nbsp;团&nbsp;名&nbsp;称：</label></td>
					<td align=left><input id="group_name" name="group_name"
						value="${group_name}" class="easyui-validatebox" width="100px"
						readOnly="readonly" /></td>
					<!-- <td></td><td></td> -->
				</tr>
				<tr>
					<td style="width: 100px">
						<input type="hidden" id="gid" value="<%=request.getAttribute("gid") %>">
						<input id="conditionSelect" name="conditionSelectName" value=""
						style="width: 120px">
					</td>
					<td><input id="conditionInput" name="conditionInputName"></td>
					<td colspan=2><a id="searchGroup" class="easyui-linkbutton"
						style="float: right" data-options="iconCls:'icon-search'"
						onclick="aa()">查询</a></td>

				</tr>
				<tr align=right>
						<a onclick="excel();"  class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a>
				</tr>

			</table>

		</form>
		<div style="float: right">
			<a id="newGroup" class="easyui-linkbutton"
				iconcls="icon-cancel" onclick="javascript:history.go(-1)"
				plain="true">关闭</a>
		</div>
		<div style="float: right">
			<a id="newGroup" href="javascript:void(0)" class="easyui-linkbutton"
				iconcls="icon-add" onclick="addDriver()" plain="true">添加车主</a>
		</div>

	</div>
	<div id="setReallocateProfit" class="easyui-dialog"
		style="width: 600px; height: 380px; padding: 10px 20px; top: 30px"
		closed="true" buttons="#setReallocateProfit-buttons">

		<form id="reallocateProfitForm" method="post">
			<table>
				<tr align=right>
					<td align=right><label> 更改为： </label><input type="hidden"
						id="userid" name="userid">
						</td>
				</tr>
				<tr>
					<td>
					<input
						type="text" id="rpRadioSelectInput" name="rpRadioSelectInput" class="easyui-validatebox textbox" 
						data-options="required:true" validType="TimeRange"> 
						
<!-- 					<input type="text" id="rpRadioSelectInput" name="rpRadioSelectInput" -->
<!-- 						class="easyui-validatebox textbox"  data-options="required:true" validType="TimeRange"> -->
					</td>
					<td><input id="rpRadioSelect" name="rpRadioSelect"
						class="easyui-validatebox" required="true" /></td>
				</tr>

			</table>
		</form>
	</div>
	<div id="setReallocateProfit-buttons">
		<a class="easyui-linkbutton"
			onclick="setReallocateProfit()" iconcls="icon-save">确定</a> 
<!-- 			<input type="reset" value="恢复默认"/> -->
	 		<a class="easyui-linkbutton"
			onclick="setReallocateProfitDefault()" iconcls="icon-save">恢复默认</a>
	</div>
	<a id="cancelGroup" href="javascript:void(0)" class="easyui-linkbutton"
		onclick="javascript:history.go(-1);" iconcls="icon-cancel">返&nbsp;回</a>
</body>
</html>
