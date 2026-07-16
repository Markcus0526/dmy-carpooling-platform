<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
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

<br />
<h2 style="margin-left: 10px; margin-bottom: 0px">财务统计</h2>
<hr />

<form id="staticsForm">
	<div style="padding: 10px 20px 0px 20px">
		<span>统计城市:</span> <select id="staticsCity"
			name="searchItem.staticsCity" class="easyui-combobox">
			<option value=0>地理区分</option>
			<option value=1>级别区分</option>
		</select>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>统计日期:</span> <input id="beginDate" name="searchItem.beginDate"
			class="easyui-datetimebox" style="width: 145px;"
			data-options="formatter:mydateformatter" />&nbsp;~ <input
			id="endDate" name="searchItem.endDate" class="easyui-datetimebox"
			style="width: 145px;" data-options="formatter:mydateformatter" /> <span
			style="padding-left: 30px;">统计时间:</span> <input id="beginTime"
			name="searchItem.beginTime" class="easyui-datetimebox"
			style="width: 145px;" data-options="formatter:mytimeformatter" />&nbsp;~
		<input id="endTime" name="searchItem.endTime"
			class="easyui-datetimebox" style="width: 145px;"
			data-options="formatter:mytimeformatter" />
	</div>

	<div style="padding: 10px 20px 0px 20px">
		<span>订单类型:</span> <input type="checkbox" id="order_1"
			name="searchItem.order_1" style="margin-left: 10px" checked="checked"
			value="1" /><label for="order_1">单次拼车</label> <input type="checkbox"
			id="order_2" name="searchItem.order_2" style="margin-left: 10px"
			checked="checked" value="2"><label for="order_2">上下班拼车执行订单</label>
		<input type="checkbox" id="order_3" name="searchItem.order_3"
			style="margin-left: 10px" checked="checked" value="1" /><label
			for="order_3">上下班拼车拆分订单</label> <input type="checkbox" id="order_4"
			name="searchItem.order_4" style="margin-left: 10px" checked="checked"
			value="2"><label for="order_4">长途拼车</label> <a href="#"
			onclick="javascript:doSearch();" class="easyui-linkbutton l-btn"
			iconCls="icon-search" style="width: 40px"></a>
	</div>
	<br />
	<hr />

	<div style="padding: 10px 20px 0px 20px">
		<span>显示方式:</span> <a href="#"
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
			href="#" onclick="javascript:report();"
			class="easyui-linkbutton l-btn" style="float: right;"
			iconCls="icon-xlx">导出Excel</a>
	</div>

	<div style="padding: 10px 0px 30px 0px">
		<table id="financeList" class="easyui-datagrid"
			style="width: auto; height: auto" data-options="singleSelect:true">
		</table>
	</div>

</form>

<hr />

<script>
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-datetimebox').datetimebox();
	
	var page_path = '${pageContext.request.contextPath}/statistics/finance/';
	var dataLoader = {};
	var contents = "";
	var date_type = "year";
	
// 	$(function(){
		dataLoader.load = function(){
			createQueryParams();
			$('#financeList').datagrid({
				url : page_path + 'search.do',
				queryParams: contents,
				loadMsg : '数据处理中，请稍后....',
				pagination: true,
				fitColumns : true,
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				idField : 'id',
	
				columns:[[
				          {
				        	  field:'date',
				        	  title: date_type == 'year' ? '年' :
				        		(date_type == 'quarter' ? '季度' :
				        		(date_type == 'month' ? '月' : 
				        		(date_type == 'week' ? '周' : 
				        		(date_type == 'day' ? '日' : 
				        		(date_type == 'hour' ? '小时' : ""))))),
				        	  width:200, align:'center'
				          },
				          {field:'money_all',title:'实收金额',width:200, align:'center'},
				          {field:'money_loan',title:'代金券抵扣金额',width:200,align:'center'},
				          {field:'money_info',title:'信息费收入',width:150,align:'center'},
				          {field:'money_person',title:'个人邀请码返利',width:150,align:'center'},
				          {field:'money_group',title:'集团客户分成',width:250,align:'center'},
				          {field:'money_unit',title:'合作单位分成',width:150,align:'center'}
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
	
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
	}
	
	function mydateformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}

	function mytimeformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		return (h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
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
	
	function createQueryParams() {
		var start_date = document.getElementsByName("searchItem.beginDate")[0].value;
		var end_date = document.getElementsByName("searchItem.endDate")[0].value;
		var start_time = document.getElementsByName("searchItem.beginTime")[0].value;
		var end_time = document.getElementsByName("searchItem.endTime")[0].value;
		
		contents = {"searchItem.date_type" : date_type,
				"searchItem.staticsCity" : $("#staticsCity").val(),
				"searchItem.beginDate" : start_date,
				"searchItem.endDate" : end_date,
				"searchItem.beginTime" : start_time,
				"searchItem.endTime" : end_time,
				"searchItem.order_1" : $("#order_1").get(0).checked == true ? 1 : 0,
				"searchItem.order_2" : $("#order_2").get(0).checked == true ? 1 : 0,
				"searchItem.order_3" : $("#order_3").get(0).checked == true ? 1 : 0,
				"searchItem.order_4" : $("#order_4").get(0).checked == true ? 1 : 0};
	}
	
	function showByDate(type) {
		date_type = type;
		dataLoader.load();
	}
	
	function doSearch() {
		dataLoader.load();
	}

	dataLoader.load();
</script>