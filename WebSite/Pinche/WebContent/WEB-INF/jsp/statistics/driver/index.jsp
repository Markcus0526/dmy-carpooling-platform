<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<br />
<h2 style="margin-left: 10px; margin-bottom: 0px">车主统计</h2>
<hr />

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

<form id="staticsForm">
	<div style="padding: 10px 20px 0px 20px">
		<span>统计城市:</span>
		<s:textfield cssClass="textbox" name="city" cssStyle="width:212px;"></s:textfield>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>统计日期:</span> <input id="beginDate" name="beginDate"
			class="easyui-datetimebox" style="width: 100px;"
			data-options="formatter:myformatter" />&nbsp;~ <input id="endDate"
			name="endDate" class="easyui-datetimebox" style="width: 100px;"
			data-options="formatter:myformatter" /> <span
			style="padding-left: 30px;">统计时间:</span> <input id="beginTime"
			name="beginTime" class="easyui-validatebox textbox"
			validType="TimeRange" style="width: 50px;" />&nbsp;~ <input
			id="endTime" name="endTime" class="easyui-validatebox textbox"
			style="width: 50px;" />
		<!--  		<a href="javascript:void(0)" onclick="btnSearch()" style="margin-left: 50px; width:40px" class="easyui-linkbutton" iconCls="icon-search"></a>-->
		<a href="#" onclick="javascript:btnsearch();"
			class="easyui-linkbutton l-btn" iconCls="icon-search"
			style="width: 40px"></a>
	</div>

	<br />
	<hr />

	<div style="padding: 10px 20px 0px 20px">
		<span>统计方式:</span> <a href="#"
			onclick="javascript:showByType('Year');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;年&nbsp;</a> <a
			href="#" onclick="javascript:showByType('Quarter');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;季度&nbsp;</a> <a
			href="#" onclick="javascript:showByType('Month');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;月&nbsp;</a> <a
			href="#" onclick="javascript:showByType('Week');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;周&nbsp;</a> <a
			href="#" onclick="javascript:showByType('Day');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;日&nbsp;</a> <a
			href="#" onclick="javascript:showByType('Time');"
			class="easyui-linkbutton l-btn"
			style="width: 120px; margin-right: 10px">&nbsp;小时&nbsp;</a> <a
			href="#" onclick="javascript:report();"
			class="easyui-linkbutton l-btn" style="float: right;"
			iconCls="icon-xlx">导出Excel</a>
	</div>

	<div style="padding: 10px 0px 30px 0px">
		<table id="dg" class="easyui-datagrid"
			style="width: auto; height: auto" data-options="singleSelect:true">
		</table>
	</div>

</form>



<script>
	var page_path = '${pageContext.request.contextPath}/statistics/driver/';
	var content = "";
	var backupPhoneList = "";
	var column={};
	var type = "showall";
	$('.easyui-validatebox').validatebox();	
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-datetimebox').datetimebox();
	
	$.extend($.fn.validatebox.defaults.rules, {
		TimeRange: {
		validator: function(value){
			if((value>=0)&&(value<=23))
				return true;
			return false;
		},
		message: '请输入0~23'
		}
	});
		
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
	
	function showByType(typeStr){
		type = typeStr;
		dataLoader.load();
	}

	
	function btnsearch(){
		type="showall";
		dataLoader.load();
	}
	var dataLoader = {};
	dataLoader.load=function(){
		createContent();
		strurl = page_path + 'search.do?method='+type;
		$('#dg').datagrid({
	         url: strurl,
	         queryParams:content,
	         loadMsg : '数据处理中，请稍后....',
	         height: 'auto',
	         pagination: true,
			 
			columns:[[
	             {field:'ts_id',width:90,align:'center', title: type == 'Year' ? '年' :
		        		(type == 'Quarter' ? '季度' :
			        		(type == 'Month' ? '月' : 
			        		(type == 'Week' ? '周' : 
			        		(type == 'Day' ? '日' : 
			        		(type == 'Time' ? '小时' : "")))))}, 
	             {field:'countofDriver',width:150,align:'center',title:'车主上线时长'},
	             {field:'countofRegDriver',width:150,align:'center',title:'注册车主数量'}
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
	
	function createContent()
	{
		
		var tags = ["input","select","textarea"];
		var form = $("#staticsForm	");
		var params = [];
		for(var i in tags){
			var collection = form.find(tags[i]);
			for(var j=0;j<collection.length;j++)
			{
				var item = $(collection[j]);
				var name = item.prop("name");
				
				if (name != ""){					
					if (tags[i]=="input") {
						if (item.prop("type")=="checkbox") {							
							if (!item.prop("checked")){
								continue;
							}
						}
					}
					
					var values = params[name];
					if (!values) {
						values = [];
					}					
					values.push(item.val());
					params[name]=values;
					
				}
			}
		}
				
		content = {};
		for(i in params)
		{
			if (params[i].length>0)
			{
				if (params[i].length>1){
					var temp = "";
					for (j=0;j<params[i].length;j++){
						if (j>0){
							temp += ",";
						}
						temp += params[i][j];						
					}
					content[i] = temp;
				} else {
					content[i] = params[i][0];
				}
			}
		}
	}
	
	dataLoader.load();
</script>