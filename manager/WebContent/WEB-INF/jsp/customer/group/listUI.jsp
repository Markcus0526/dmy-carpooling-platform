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

<title>集团客户列表</title>

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

	var datagrid;
	$(function(){
	datagrid=$('#dg').datagrid({		
	
							url	:'<%=basePath%>customer/groupPageList.do',
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
							idFiled : 'ID',
							checkOnSelect : true,
							singleSelect:true,
							striped : true,
// 							rownumbers:true,  
							toolbar : '#content',
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
  										str += '<a href="customer/showGroupUI.do?id='+row.id+'" target="_self">查看</a> &nbsp';
  										str += '<a href="customer/updateGroupInfoUI.do?id='+row.id+'" target="_self">修改</a> &nbsp';
  										str += '<a href="customer/personnelAdjustmentUI.do?id='+row.id+'" target="_self">人员调整</a> &nbsp';
  										str += '<a id="aa" href="" onclick="delGroup('+row.id+','+row.memberCount+','+index+');return false">删除</a> &nbsp';
  										str += '<a id="ab" href="customer/rebateInfoUI.do?id='+row.id+'" target="_self" >返利信息</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									},{
										field : 'id',
										title : '序号',
										width : 100,										
									}, {
										field : 'groupid',
										title : '集团标识',
										width : 100,
									//checkbox : true
									}, {
										field : 'group_name',
										title : '集团名称',
										width : 100
									}, {
										field : 'group_property',
										title : '性质',
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
			value : 'groupid',
			data : [ {
				label : '按集团标识查询',
				value : 'groupid'
			}, {
				label : '按集团名称查询',
				value : 'group_name'
			} ]

		}); */
		
		
/* 			$('#searchGroup').keyup(function(event) {
				if (event.keyCode == '13') {
					aa();
				}
			}); */
			
			
	radioSelect = $('#radioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_driver_self',
			data : [ {
				label : '%',
				value : '0'
			}, {
				label : '点',
				value : '1'
			} ]

		}); 

	});
	
	    function newGroup() {
	    	//alert("newGroup");
            $("#dlg").dialog("open").dialog('setTitle', '新建集团客户');
            $("#fm").form("clear");
            $("#group_property option[value='0']").attr("selected",true);
            $.post("<%=basePath%>customer/getGproperty.do",function(data){
            	$("#groupid").val(data);
            },"json");
            <%-- //url = "<%=basePath%>newGroup.action"; --%>
            //document.getElementById("hidtype").value="submit";
        }
	
	  function edituser() {
            var row = $("#dg").datagrid("getSelected");
            if (row) {
                $("#dlg").dialog("open").dialog('setTitle', 'Edit User');
                $("#fm").form("load", row);
               /*  url = "UserManage.aspx?id=" + row.ID; */
            }
        }
        function saveGroup() {
            $("#fm").form("submit", {
                url: '<%=basePath%>customer/saveGroup.do',
                onsubmit: function () {
                    return $(this).form("validate");
                },
     		   dataType:'json',
     		    type: 'POST',
    		   success:function(result){
     			   if(result.returnCode>0){
//     				    $.messager.alert('提示',result.msg,'info',function(){
    				    	//window.history.go(-1);
    				    	$("#dlg").dialog("close");
                            $("#dg").datagrid("load");
//     				    });  
    			   }else{
//     				    $.messager.alert('提示',result.msg,'info',function(){
//     				    });
    					$("#dlg").dialog("close");
                        $("#dg").datagrid("load");
    			   } 
    		   }
/*                 success: function (result) {
                    if (result == "1") {
                        $.messager.alert("提示信息", "操作成功");
                        $("#dlg").dialog("close");
                        $("#dg").datagrid("load");
                    }
                    else {
                        $.messager.alert("提示信息", "操作失败");
                    }
                 $.messager.alert("提示信息", "操作成功");
                 	$('#dg').datagrid('reload');
                    $("#dlg").dialog("close");
                }  */
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
        
      function delGroup(id,memberCount,index){
    		$.messager.confirm('删除', '您确定要删除吗？', function(r) {
    			if (r) {
    				
    				if(memberCount==0){
    	       			$.ajax({
    			   		type: 'POST',
    			   		url: '<%=basePath%>customer/delGroup.do',
    			   		data:'id='+id,
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
	            	}else{
	                     $.messager.confirm('提示', '无法删除旗下有成员的联盟客户');
	           	 		//alert("无法删除旗下有成员的联盟客户");
	            	}
    				
    			} else {
    			}
    		});
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
  	
  	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	function dategridTimeFormat(dateObj) {
		var h = dateObj.hours;
		var M = dateObj.minutes;
		var s = dateObj.seconds;
		var y = dateObj.year;
		if (y < 1900)
			y += 1900;
		var m = dateObj.month+1;
		var d = dateObj.date;
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);		
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
	
	$.extend($.fn.validatebox.defaults.rules, {
		num: {
		validator: function(value){
			var p = /(?!^\d+$)(?!^[a-zA-Z]+$)[0-9a-zA-Z]{6,8}/;
			if(p.test(value)){
				if(value.length>=6&&value.length<=8){
					return true;
				}	
			}
			return false;
		},
		message: '请输入6-8位数字或字母！'
		}
	});
	
	
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
		<div data-options="name:'groupid'">按集团标识查询</div>
		<div data-options="name:'group_name'">按集团名称查询</div>
	</div>
	<!-- 	<form id="hiddenForm" method="post">
		<input id="searchValue" type="hidden" /> <input id="searchType"
			type="hidden" />
	</form> -->
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
			<a class="easyui-linkbutton" iconCls="icon-add" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold" onclick="newGroup();return false;">新建集团&nbsp;</font></a>


		</div>
	</div>

	<!-- 	<table id="dg"></table>

	<div id="toolbar">
		
	<div>
						
						 <input id="conditionSelect" name="conditionSelectName" value="sdfsf">
						 <input id="conditionInput" name="conditionInputName" >
						 <a id="searchGroup" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="aa()">查询</a>
				<a id="newGroup" href="javascript:void(0)"
					class="easyui-linkbutton" style="float:right" iconcls="icon-add" onclick="newGroup()"
					plain="true">新建集团客户</a>
				
			</div>
			
	

	</div> -->

	<div id="dlg" class="easyui-dialog"
		style="width: 600px; height: 380px; padding: 10px 20px; top: 30px"
		closed="true" buttons="#dlg-buttons">

		<form id="fm" method="post">
			<table>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							集&nbsp;团&nbsp;标&nbsp;识：</font></td>
					<td><input id="groupid" name="groupid"
						style="width: 100%; height: 20px" readonly="readonly"/></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							合&nbsp;同&nbsp;编&nbsp;号：</font></td>
					<td><input id="contract_no" name="contract_no"
						style="width: 100%; height: 20px" /></td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							集&nbsp;团&nbsp;名&nbsp;称： </font></td>
					<td><input id="group_name" name="group_name"
						class="easyui-validatebox textbox" data-options="required:true"
						style="width: 100%; height: 20px" /></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							集&nbsp;团&nbsp;性&nbsp;质：</font></td>
					<td> 
					    <select name="group_property" id="group_property">
					    	<option value="0" selected="true">集团车主</option>
					    	<option value="1">集团乘客</option>
					    </select>
					</td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							联&nbsp;系&nbsp;人： </font></td>
					<td><input id="linkname" name="linkname"
						style="width: 100%; height: 20px" /></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							联&nbsp;系&nbsp;电&nbsp;话：</font></td>
					<td><input id="linkphone" name="linkphone"
						class="easyui-validatebox textbox" data-options="required:true"
						style="width: 100%; height: 20px" /></td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							固&nbsp;定&nbsp;电&nbsp;话： </font></td>
					<td><input id="fix_phone" name="fix_phone"
						style="width: 100%; height: 20px" /></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%"> Email：</font></td>
					<td><input id="email" name="email"
						style="width: 100%; height: 20px" /></td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							传&nbsp;真&nbsp;号&nbsp;码： </font></td>
					<td><input id="fax" name="fax"
						style="width: 100%; height: 20px" /></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							集&nbsp;团&nbsp;地&nbsp;址：</font></td>
					<td><input id="group_address" name="group_address"
						style="width: 100%; height: 20px" /></td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							签&nbsp;约&nbsp;时&nbsp;间： </font></td>
					<td><input id="sign_time" name="sign_time" class="easyui-datetimebox" data-options="formatter:myformatter,required:true" 
						style="width: 100%; height: 20px" /></td>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">默认分成比例：</font></td>
					<td align=left style="width: 100px; height: 15px;"><input
						type="text" id="radioSelectInput" name="radioSelectInput" class="easyui-validatebox textbox" 
						data-options="required:true" validType="TimeRange"
						style="width: 30%;"> <input id="radioSelect"
						name="radioSelect" style="width: 50%;"></td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%">
							邀&nbsp;请&nbsp;码： </font></td>
					<td><input id="invitecode_self" name="invitecode_self"
						class="easyui-validatebox textbox" data-options="required:true" validType="num"
						style="width: 100%; height: 20px" /></td>
					<td colspan=2><font size="2" color="red">说明：该分成为扣除信息费后的分成</font></td>
				</tr>
				<tr align=right>
					<td align=right><font
						style="font-weight: bold; font-size: 80%"> 备&nbsp;&nbsp;注：</font></td>
					<td colspan=3><textarea rows="2" cols="50" id="remark"
							name="remark" style="width: 100%; height: 40px"></textarea>
					</td>
				</tr>

			</table>
		</form>
	</div>


	<div id="dlg-buttons">
		<a id="saveGroup" href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveGroup()" iconcls="icon-save">保&nbsp;存</a> <a
			id="cancelGroup" href="javascript:void(0)" class="easyui-linkbutton"
			onclick="javascript:$('#dlg').dialog('close')" iconcls="icon-cancel">取&nbsp;消</a>
	</div>

</body>
</html>
