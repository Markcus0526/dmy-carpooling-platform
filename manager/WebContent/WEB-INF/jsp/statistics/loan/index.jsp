<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
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
</head>

<body>


	<br />
	<h2 style="margin-left: 10px; margin-bottom: 0px">点券统计</h2>
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
			<!--  		<a href="javascript:void(0)" onclick="btnSearch()" style="margin-left: 50px; width:40px" class="easyui-linkbutton" iconCls="icon-search"></a>-->
		</div>
		<div style="padding: 10px 20px 0px 20px">
			<span>订单类型:</span> <input type="checkbox" id="single"
				name="searchItem.singlePinche" style="margin-left: 10px"
				checked="checked" /><span>单次拼车</span> <input type="checkbox"
				id="dim" name="searchItem.dimPinche" style="margin-left: 10px"
				checked="checked" /><span>上下班拼车总订单</span> <input type="checkbox"
				id="ddiv" name="searchItem.divPinche" style="margin-left: 10px"
				checked="checked" /><span>上下班拼车执行订单</span> <input type="checkbox"
				id="long" name="searchItem.longPinche" style="margin-left: 10px"
				checked="checked" /><span>长途拼车</span>

			<!-- 		<a href="#" onclick="javascript:add();" class="easyui-linkbutton l-btn" style="float: right; margin-right: 10px">&nbsp;创建短信推广计划&nbsp;</a> -->
		</div>

		<div style="padding: 10px 20px 0px 20px">
			<%-- 		<span>发行渠道：</span> --%>
			<!-- 		<input type="text" id="releaseChannel" name="searchItem.rChannel" style="margin-left:10px; width:120px;"> -->

			<span>发行渠道：</span> <select id="releaseChannel"
				name="searchItem.rChannel" style="margin-left: 10px; width: 120px;">
				<option value=0>所有</option>
				<option value=2>合作单位</option>
				<option value=1>App</option>
			</select> <span style="margin-left: 20px;">点券类别：</span> <input type="checkbox"
				id="chk_nocondcash" name="searchItem.nocondcash" checked="checked" />无条件绿点
			<input type="checkbox" id="chk_condcash" name="searchItem.condcash"
				checked="checked" />有条件绿点 <input type="checkbox" id="chk_good"
				name="searchItem.good" checked="checked" />实物 <a href="#"
				onclick="javascript:showCoupon();" class="easyui-linkbutton l-btn"
				style="float: right; margin-right: 10px">&nbsp;检索点券&nbsp;</a>
		</div>

		<div style="padding: 10px 0px 30px 0px">
			<table id="loanList" class="easyui-datagrid"
				style="width: auto; height: auto" data-options="singleSelect:true">
			</table>
		</div>

		<a href="#" onclick="javascript:startStatics();"
			class="easyui-linkbutton l-btn"
			style="float: right; margin-right: 10px">&nbsp;开始统计&nbsp;</a> <br />
		<br />
		<hr />

		<div style="padding: 10px 20px 0px 20px">
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
				href="#" onclick="javascript:report();"
				class="easyui-linkbutton l-btn" style="float: right;"
				iconCls="icon-xlx">导出Excel</a>
		</div>

		<div style="padding: 10px 0px 30px 0px">
			<table id="staList" class="easyui-datagrid"
				style="width: auto; height: auto" data-options="singleSelect:true">
			</table>
		</div>

	</form>

	<hr />

	<script>
	var page_path = '${pageContext.request.contextPath}/statistics/loan/';
	
	$('.easyui-combobox').combobox();
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-datetimebox').datetimebox();

	var dataLoader = {};
	var contetns = {};
	var staLoader = {};
	var staUnit="";
	var Unit=1;
	var selectedId=0;
	var date_type="year";
	
//	$(function(){
		dataLoader.load = function(){
			createQueryParams();
			$('#loanList').datagrid({
				url : page_path + 'search.do',
				queryParams: contents,
				loadMsg : '数据处理中，请稍后....',
				pagination: true,
				fitColumns : true,
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				idField : 'id',
	
				columns:[[
				          {field:'view',title:'统计',width:60, align:'center'},
				          {field:'couponid',title:'点券ID',width:120, align:'center'},
				          {field:'releasechannel',title:'发放渠道',width:120,align:'center'},
				          {field:'content',title:'内容',width:150,align:'center'},
				          {field:'goodsorcash',title:'类别',width:150,align:'center'},
				          {field:'coupontype',title:'使用条件',width:150,align:'center'},
				          {field:'validperiod',title:'有效期',width:150,align:'center'},
				          {field:'deploycount',title:'发放次数',width:70,align:'center'},
				          {field:'usecount',title:'使用次数',width:70,align:'center'},
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
//	});
	
	function createQueryParams() {
// 		var tags = [ "input", "select" ];
// 		var form = $("#staticsForm");
// 		var params = [];
// 		for ( var i in tags) {
// 			var collection = form.find(tags[i]);
// 			for (var j = 0; j < collection.length; j++) {
// 				var item = $(collection[j]);
// 				var name = item.prop("name");
// 				if (name != "") {
// 					if (tags[i] == "input") {
// 						if (item.prop("type") == "radio") {
// 							if (!item.prop("checked")) {
// 								continue;
// 							}
// 						}
// 					}
// 					params[name] = item.val();
// 				}
// 			}
// 		}
// 		return params;
		var start_date = document.getElementsByName("searchItem.beginDate")[0].value;
		var end_date = document.getElementsByName("searchItem.endDate")[0].value;
		var start_time = document.getElementsByName("searchItem.beginTime")[0].value;
		var end_time = document.getElementsByName("searchItem.endTime")[0].value;
		
		contents = {"searchItem.dateType" : date_type,
				"searchItem.selectedId" : selectedId,
				"searchItem.staticsCity" : $("#staticsCity").val(),
				"searchItem.beginDate" : start_date,
				"searchItem.endDate" : end_date,
				"searchItem.beginTime" : start_time,
				"searchItem.endTime" : end_time,
				"searchItem.singlePinche" : $("#single").get(0).checked == true ? 1 : 0,
				"searchItem.dimPinche" : $("#dim").get(0).checked == true ? 1 : 0,
				"searchItem.divPinche" : $("#ddiv").get(0).checked == true ? 1 : 0,
				"searchItem.longPinche" : $("#long").get(0).checked == true ? 1 : 0,
				"searchItem.nocondcash" : $("#chk_nocondcash").get(0).checked == true ? 1 : 0,
				"searchItem.condcash" : $("#chk_condcash").get(0).checked == true ? 1 : 0,
				"searchItem.good" : $("#chk_good").get(0).checked == true ? 1 : 0,
				"searchItem.RChannel" : $("#releaseChannel").val()};
 	}
	
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
	
	function showCoupon(){
		 dataLoader.load();
	}
	
	function view(id){
		//alert("selectedRow is" + id);
		selectedId=id;
		staLoader.load ();
	}
	
	function showByDate(type)
	{
		date_type=type;
		staLoader.load();
	}

	dataLoader.load();
	
	staLoader.load = function(){
		createQueryParams();
		$('#staList').datagrid({
			url : page_path + 'viewDetail.do',
			queryParams: contents,
			loadMsg : '数据处理中，请稍后....',
			pagination: true,
			fitColumns : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			idField : 'id',

			columns:[[
			          {field:'view',
			        	  title: date_type == 'year' ? '年' :
			        		(date_type == 'quarter' ? '季度' :
			        		(date_type == 'month' ? '月' : 
			        		(date_type == 'week' ? '周' : 
			        		(date_type == 'day' ? '日' : 
			        		(date_type == 'hour' ? '小时' : ""))))),
			        	  width:60, align:'center'
				      },
			          {field:'issuedCount',title:'发行数量',width:100, align:'center'},
			          {field:'usedCount',title:'使用次数',width:100,align:'center'},
			          {field:'deltatime',title:'发行使用平均间隔',width:100,align:'center'},
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
</script>
</body>
</html>

