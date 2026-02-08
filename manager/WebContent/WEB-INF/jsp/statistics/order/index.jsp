<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
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

<h2 style="margin-left: 10px; margin-bottom: 0px">订单统计</h2>
<hr />

<form id="staticsForm">
	<div style="padding: 10px 20px 0px 20px">
		<span>统计城市:</span> <select id="staticsCity"
			name="searchItem.staticsCity">
			<option value=0>地理区分</option>
			<option value=1>级别区分</option>
		</select>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>统计日期:</span> <input id="beginDate" name="searchItem.beginDate"
			class="easyui-datetimebox _textbox" style="width: 100px;"
			data-options="formatter:myformatter" />&nbsp;~ <input id="endDate"
			name="searchItem.endDate" class="easyui-datetimebox _textbox"
			style="width: 100px;" data-options="formatter:myformatter" /> <span
			style="padding-left: 112px;">统计时间:</span> <input id="beginTime"
			name="searchItem.beginTime" class="easyui-datetimebox _textbox"
			style="width: 90px;" data-options="formatter:dategridTimeFormat" />&nbsp;~
		<input id="endTime" name="searchItem.endTime"
			class="easyui-datetimebox _textbox" style="width: 90px;"
			data-options="formatter:dategridTimeFormat" />
	</div>

	<div style="padding: 10px 20px 0px 20px">
		<span>价格区间:</span> <input id="beginPrice" class="textbox _textbox"
			name="searchItem.beginPrice" style="width: 139px;" />&nbsp;~ <input
			id="endPrice" class="textbox _textbox" name="searchItem.endPrice"
			style="width: 139px;" /> <span style="padding-left: 30px;">里程区间:</span>
		<input id="beginDist" name="searchItem.beginDist"
			class="textbox _textbox" style="width: 139px;" />&nbsp;~ <input
			id="endDist" name="searchItem.endDist" class="textbox _textbox"
			style="width: 139px;" />
	</div>

	<div style="padding: 10px 20px 0px 20px">
		<span>用户情况:</span> <input type="checkbox" id="newUser"
			name="searchItem.newUser" style="margin-left: 10px" checked="checked"
			value="1" /><label for="newUser">新用户</label> <input type="checkbox"
			id="oldUser" name="searchItem.oldUser" style="margin-left: 10px"
			checked="checked" value="2" /><label for="oldUser">老用户</label>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>订单类型:</span> <input type="checkbox" id="orderTemp"
			name="searchItem.orderTemp" style="margin-left: 10px"
			checked="checked" /><label for="orderTemp">单次拼车</label> <input
			type="checkbox" id="orderOnoffDetails"
			name="searchItem.orderOnoffDetails" style="margin-left: 10px"
			checked="checked" /><label for="orderOnoffDetails">上下班拼车总订单</label> <input
			type="checkbox" id="orderOnoffExec" name="searchItem.orderOnoffExec"
			style="margin-left: 10px" checked="checked" /><label
			for="orderOnoffExec">上下班拼车执行订单</label> <input type="checkbox"
			id="orderOnoffDivide" name="searchItem.orderOnoffDivide"
			style="margin-left: 10px" checked="checked" /><label
			for="orderOnoffDivide">上下班拼车拆分订单</label> <input type="checkbox"
			id="orderLong" name="searchItem.orderLong" style="margin-left: 10px"
			checked="checked" /><label for="orderLong">长途拼车</label>
	</div>

	<div style="padding: 10px 0px 0px 20px">
		<span>订单来源:</span> <input type="checkbox" id="appMode"
			name="searchItem.appMode" style="margin-left: 10px" checked="checked"
			value="1" /><label for="appMode">APP</label> <input type="checkbox"
			id="siteMode" name="searchItem.siteMode" style="margin-left: 10px"
			checked="checked" value="2" /><label for="siteMode">网路</label> <a
			href="#" onclick="javascript:startOrderStatics();"
			class="easyui-linkbutton l-btn" style="float: right"
			icon="icon-search">&nbsp;开始统计&nbsp;</a>
	</div>
	<hr />

	<div style="padding: 10px 0px 0px 0px">
		<span>统计方式:</span> <a href="#"
			onclick="javascript:showByDate('year');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;年&nbsp;</a> <a
			href="#" onclick="javascript:showByDate('quarter');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;季度&nbsp;</a> <a
			href="#" onclick="javascript:showByDate('month');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;月&nbsp;</a> <a
			href="#" onclick="javascript:showByDate('week');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;周&nbsp;</a> <a
			href="#" onclick="javascript:showByDate('day');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;日&nbsp;</a> <a
			href="#" onclick="javascript:showByDate('hour');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;小时&nbsp;</a> <a
			href="#" onclick="javascript:report('');"
			class="easyui-linkbutton l-btn" style="float: right;"
			iconCls="icon-xlx">导出Excel</a>
	</div>
</form>

<div style="margin-top: 5px">
	<table id="dg" class="easyui-datagrid"
		style="width: auto; height: auto"
		data-options="rownumbers:true,singleSelect:true">
	</table>
</div>

<script>
	var page_path = '${pageContext.request.contextPath}/statistics/order/';
	var date_type = 'year';
	var orderStatisticsParams = "";
	
	$('.easyui-combobox').combobox();
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-datetimebox').datetimebox();
	
	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	function dategridTimeFormat(date) {
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		return (h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);		
	}
	
	var dataLoader = {};
	dataLoader.load=function(){
		createOrderStatisticsParams();
		strurl = page_path + 'startOrderStatistics.do';
		$('#dg').datagrid({
	         url: strurl,
	         queryParams: orderStatisticsParams,
	         loadMsg : '数据处理中，请稍后....',
	         height: 'auto',
	         fitColumns: true,
			 
			columns:[[
	             {
					 field:'date',
					 title: date_type == 'year' ? '年' :
						(date_type == 'quarter' ? '季度' :
						(date_type == 'month' ? '月' : 
						(date_type == 'week' ? '周' : 
						(date_type == 'day' ? '日' : 
						(date_type == 'hour' ? '小时' : ""))))),
					 width:50, align:'center'
		         }, 
	             {field:'pubOrderCnt',width:100,align:'center',title:'发起订单'},
	             {field:'acptOrderCnt',width:100,align:'center',title:'响应订单'},
	             {field:'payedOrderCnt',width:100,align:'center',title:'支付订单'},  
	             {field:'completeOrderCnt',width:70,align:'center',title:'完成订单'},
	             {field:'totalPrice',width:100,align:'center',title:'总价格'},
	             {field:'averagePrice',width:100,align:'center',title:'平均价格'},
	             {field:'totalDist',width:70,align:'center',title:'总里程'},
	             {field:'averageDist',width:70,align:'center',title:'平均里程'},
	             {field:'averagePrice4averageDis',width:70,align:'center',title:'单价'},
	         ]],
	         onLoadSuccess : function(ret) {				
			},
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
	dataLoader.load();
	
	function createOrderStatisticsParams() {
		orderStatisticsParams = {
			"searchItem.date_type" : date_type,
			"searchItem.staticsCity" : $("#staticsCity").val(),
			"searchItem.beginDate" : $('#beginDate').datetimebox('getText'),
			"searchItem.endDate" : $('#endDate').datetimebox('getText'),
			"searchItem.beginTime" : $('#beginTime').datetimebox('getText'),
			"searchItem.endTime" : $('#endDate').datetimebox('getText'),
			"searchItem.beginPrice" : $('#beginPrice').val(),
			"searchItem.endPrice" : $('#endPrice').val(),
			"searchItem.beginDist" : $('#beginDist').val(),
			"searchItem.endDist" : $('#endDist').val(),
			"searchItem.newUser" : $("#newUser").get(0).checked == true ? 1 : 0,
			"searchItem.oldUser" : $("#oldUser").get(0).checked == true ? 1 : 0,
			"searchItem.orderTemp" : $("#orderTemp").get(0).checked == true ? 1 : 0,
			"searchItem.orderOnoffDetails" : $("#orderOnoffDetails").get(0).checked == true ? 1 : 0,
			"searchItem.orderOnoffDivide" : $("#orderOnoffDivide").get(0).checked == true ? 1 : 0,
			"searchItem.orderOnoffExec" : $("#orderOnoffExec").get(0).checked == true ? 1 : 0,
			"searchItem.orderLong" : $("#orderLong").get(0).checked == true ? 1 : 0,
			"searchItem.appMode" : $("#appMode").get(0).checked == true ? 1 : 0, 
			"searchItem.siteMode" : $("#siteMode").get(0).checked == true ? 1 : 0
		};
	}
	
	function showByDate(type) {		
		date_type = type;
		alert(0);
		dataLoader.load();
	}
	
	function startOrderStatics() {
		alert(1);
		date_type = "";
		dataLoader.load();
	}
</script>