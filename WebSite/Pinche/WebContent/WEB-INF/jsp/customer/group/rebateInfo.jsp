<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>查看返利信息</title>

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

$(function(){
			$.ajax({
			url : '<%=basePath%>customer/findGroupInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				//--------集团基本信息---------------
				$('#groupid').val(jsonObject.groupid);
				$('#group_name').val(jsonObject.group_name);
				$('#invitecode_self').val(jsonObject.invitecode_self);
				$('#group_property').val(jsonObject.group_property);
				}
			});
			$('#dg').datagrid({		
							url	:'<%=basePath%>customer/findGroupAllRebateListInfo.do?id=${id}',
							<%-- url	:'<%=basePath%>getGroupAllRebateListInfo.action?invitecode_self='+$('#invitecode_self').val(), --%>
							method : 'POST',
							title : '返利列表',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							//fit : true,
							//hight:600,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
							singleSelect:true,
							onLoadSuccess: statistics,
							striped : true,
							columns : [ [
									 { field : 'id',
										title : '被邀请客户ID',
										width : 100,
									//checkbox : true
									}, {
										field : 'username',
										title : '被邀请客户姓名',
										width : 100
									}, {
										field : 'rpcount',
										title : '返利次数',
										width : 100,
										align : 'right'
									}, {
										field : 'rpsum',
										title : '累计返利',
										width : 100,
										align : 'right'
									}, {
										field : 'reg_date',
										title : '邀请时间',
										width : 100,
										align : 'right'
									}, {
										field : 'details',
										title : '返利详情',
										width : 100,
										align : 'center',
										formatter : function(value, row, index) {
										var str = '';
  										str += '<a href="" onclick="showFLDG('+row.id+');return false">查看详情</a> &nbsp';
  											return str;
  											if(row.id){return row.id}
										}
									}
									] ],
							toolbar : '#toolbar'

						});

});
	      function statistics() {//计算函数
            var rows = $('#dg').datagrid('getRows')//获取当前的数据行
            var count = 0//计算listprice的总和
            ,sum=0;//统计unitcost的总和
            for (var i = 0; i < rows.length; i++) {
                sum += rows[i]['rpsum'];
                count += rows[i]['rpcount'];
            }
            console.info(count+"----"+sum);
            $('#count').val(count);
            $('#sum').val(sum);
            //新增一行显示统计信息
            //$('#dg').datagrid('appendRow', { itemid: '<b>统计：</b>', listprice: ptotal, unitcost: utotal });
        }

	function showFLDG(id) {
// 		alert("-----"+id);
		$("#idd").val(id);
		$("#FLDG").dialog("open").dialog('setTitle', '查看返利详情');
		$("#FLDGfm").form("clear");
		$('#rebateForm').datagrid({		
	
							url	:'<%=basePath%>customer/findGroupRebateListInfo.do?id='+id,
							method : 'POST',
							title : '返利列表',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 10,
							pageList : [ 10, 20, 50 ],
							//fit : true,
							//hight:600,
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : false,
							striped : true,
							columns : [ [
									
									 {
										field : 'username',
										title : '被邀请客户姓名',
										width : 100,
									}, {
										field : 'order_cs_id',
										title : '订单编号',
										width : 100
									}, {
										field : 'ts_date',
										title : '返利时间',
										width : 100,
										align : 'right'
									}, {
										field : 'identityStatus',
										title : '身份',
										width : 100,
										align : 'right'
									}, {
										field : 'sum',
										title : '返利金额',
										width : 100,
										align : 'right'
									}
									] ],
							toolbar : '#toolbar'

						});

	}
	
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
	
	function excel(){
		location = "<%=basePath%>excel/listGroupMoneyToExcel.do?id="+$('#groupid').val();
	}
	
	function excel1(){
		location = "<%=basePath%>excel/listGroupShowMoneyToExcel.do?id="+$('#idd').val();
		
	}
</script>

</head>

<font style="font-weight: bold">
<a onclick="excel();"  class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"
>导出Excel&nbsp;</a> </font>

<a id="cancel" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'"
				onclick="javascript:history.go(-1)">关闭</a>

<body onload="loaddone()">
	<input type="hidden" id="idd"/>
	<div title="返利信息" data-options="iconCls:'icon-reload',closable:true"
		style="padding: 20px; hight: auto;">

		<table border=0>
			<tr>
				<td>集团标识：</td>
				<td align=right style="width: 100px; height: 15px;"><input
					id="groupid" name="groupid" readOnly="readonly"></td>
				<td>邀请码：</td>
				<td align=right style="width: 100px; height: 15px;"><input
					id="invitecode_self" name="invitecode_self" readOnly="readonly"></td>
			</tr>
			<tr>
				<td>客户名称：</td>
				<td align=left style="width: 100px; height: 15px;"><input
					id="group_name" name="group_name"></td>
				<td>客户性质：</td>
				<td align=left style="width: 100px; height: 15px;"><input
					id="group_property" name="group_property" readOnly="readonly"></td>
			</tr>

			<tr>
				<td>返利&nbsp;总计：</td>
				<td><input id="count" name="count" style="width: 40px;"
					readOnly="readonly">次 &nbsp; <input id="sum" name="sum"
					style="width: 40px;" readOnly="readonly">点</td>
			</tr>
		</table>

		<table id="dg"></table>

		<div id="FLDG" class="easyui-dialog"
			style="width: 600px; height: 400px; padding: 10px 20px; top: 30px; left: 200px;"
			closed="true" buttons="#FLDG-buttons" resizable="true">
			
			<a onclick="excel1();"  class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a>
		
			<form id="FLDGfm" method="post">
				<table id="rebateForm"></table>
			</form>
		</div>
		<div id="FLDG-buttons">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="javascript:$('#FLDG').dialog('close')"
				iconcls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
