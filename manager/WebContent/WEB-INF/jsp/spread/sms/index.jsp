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
		<link rel="stylesheet" type="text/css" href="js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="js/themes/default/linkbutton.css" />
		<link rel="stylesheet" type="text/css" href="js/themes/default/datebox.css" />
		<link rel="stylesheet" type="text/css" href="js/themes/default/tree.css" />
		<link rel="stylesheet" type="text/css" href="js/themes/icon.css" />
		<script type="text/javascript" src="js/jquery.min.js"></script>
		<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="js/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>  	  


<script type="text/javascript" charset="UTF-8">
		var page_path="<%=basePath%>spread/sms/";
	   var datagrid;
	   $(function(){
			var beginTime=$('#fromRegTime').datetimebox('getValue');
			var endTime=$('#toRegTime').datetimebox('getValue');
	        var statusType = $("input[name='statusType']:checked").val();
		    var sendMode1 = $("input[name='sendMode1']:checked").val();
		    var sendMode2 = $("input[name='sendMode2']:checked").val();
	       	datagrid=$('#dg').datagrid({
	        		url	:page_path+'getSmsPlanList.do',
					method : 'POST',
					title : '',
					iconCls : 'icon-save',
	        		pagination : true,
	        		pageNumber : 1,
	        		pageSize : 10,
	        		pageList : [ 10, 20, 50 ],
	        		//fit : true,
	        		height:350,
	        		queryParams:{
	        			beginTime : beginTime,
	        			endTime : endTime,
	        			statusType : statusType,
	        			sendMode1 : sendMode1,
	        			sendMode2 : sendMode2
	        		},
	        		fitColumns : true,
	        		nowrap : false,
	        		border : false,
	        		checkOnSelect : true,
	        		singleSelect:true,
	        		striped : true,
	        		toolbar : '#content',
	        		columns : [ [{field : 'action',title : '操作',width : 120,align : 'center',
						formatter : function(value, row, index) {
						var str = '';
							str += '<a href="'+page_path+'selectPage.do?id='+row.id+'" target="_self">查看</a> &nbsp';
							str += '<a href="'+page_path+'editPage.do?id='+row.id+'" target="_self">修改</a> &nbsp';						
							return str;
								if(row.id){return row.id}
						}
					},{field : 'id',width : 100,align : 'center',title : '计划编号'
					}, {field : 'client_num',width : 40,align : 'center',title : '推广人数'
					}, {field : 'send_mode',width : 100,align : 'center',title : '计划类别',
						formatter : function(value, row, index) {
							if(row.send_mode==1){
								return "一次发送";
							}if(row.send_mode==2){
								return "周期发送";
							}
						}
					
					}, {field : 'status',	width : 80,align : 'center',title : '当前状态'
					}, {field : 'single_time',	width : 120,	align : 'center',title : '发送时间',
						formatter: function(value,row,index){
								var str = "";
								var dayNames = ["每周日", "每周一", "每周二", "每周三", "每周四", "每周五", "每周六"];
								if (row.send_mode == 1){
										var h = value.hours;
										var M = value.minutes;
										var s = value.seconds;
										var y = value.year;
										if (y < 1900)
											y += 1900;
										var m = value.month+1;
										var d = value.date;
									return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
								}
								else {
									regular_send_mode = row.regular_send_mode;
									time1 = row.time1;
									time2 = row.time2;
									if (regular_send_mode == 1) {
										str = "每天 " + time1 + "点 " + time2 + "分";
									} else if(regular_send_mode == 2) {
										str = dayNames[time1] + " " + time2 / 3600 + "点 ";
									} else {
										str = "每月" + time1 + "日" + time2 / 3600 + "点 ";
									}
								}
								
								return str;
							}

					}, {field : 'has_send_times',width : 120,align : 'center',	title : '已发送次数'
					}, {field : 'create_time',width : 140,align : 'center',title : '创建时间',
						formatter: function(value,row,index){
							var h = value.hours;
							var M = value.minutes;
							var s = value.seconds;
							var y = value.year;
							if (y < 1900)
								y += 1900;
							var m = value.month+1;
							var d = value.date;
							return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
							
						}
						
					} ] ],
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
				});
		});
	

	
	function btnSearch() {
		var beginTime=$('#fromRegTime').datetimebox('getValue');
		var endTime=$('#toRegTime').datetimebox('getValue');
        var statusType = $("input[name='statusType']:checked").val();
	    var sendMode1 = $("input[name='sendMode1']:checked").val();
	    var sendMode2 = $("input[name='sendMode2']:checked").val();
		$('#dg').datagrid('load',{
			beginTime : beginTime,
			endTime : endTime,
			statusType : statusType,
			sendMode1 : sendMode1,
			sendMode2 : sendMode2,
		}); 
	} 
	
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body  onload="loaddone()">

	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px; ">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height:15%;">
				<table id="querytable" cellspacing="0" cellpadding="5px" border=0
					style="text-align: left; height: auto; margin: 0px;">
					<tr>
						<td>
							<div>
								<font style="font-weight:normal;font-size:80%" >注册时间:</font>
								<input id="fromRegTime" name="fromRegTime" type="text" class="easyui-datetimebox" style="width:150px;height: 20px"/>&nbsp;~&nbsp;
								<input  id="toRegTime" name="toRegTime" type="text" class="easyui-datetimebox" style="width:150px; height: 20px"/>
							</div>
						</td>
						<td> </td>
						<td> </td>
					</tr>
					<tr>
						<td>
							 <font style="font-weight:normal;font-size:80%" >计划状态:</font>
							 <input id="all" type="radio" name="statusType"  value="1" checked="checked" ><font style="font-weight:normal;font-size:80%" >仅显示进行中 </font>
							 <input id="person_verified" type="radio" name="statusType" value="2"><font style="font-weight:normal;font-size:80%" >显示全部</font>
						</td>
				 		<td>
							 <font style="font-weight:normal;font-size:80%" >计划类别:</font>
						<input type="checkbox" id ="sendMode1" name="sendMode1" style="margin-left: 10px"checked="checked" value="1">
								<font style="font-weight:normal;font-size:80%" >单次发送</font>
							<input type="checkbox" id ="sendMode2" name="sendMode2"  style="margin-left: 10px" checked="checked" value="2">
								<font style="font-weight:normal;font-size:80%" >周期发送</font>
						</td> 
						<td>
								<a href="javascript:void(0)" onclick="btnSearch()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="float:right;">查询</a>
						</td>
					</tr>
					
				</table>
			</div>
			<div data-options="region:'center',title:''" style="border: green 0px solid;height:auto">
				<table id="dg" style="padding: 1px; boder: black 0px solid;" data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
		
			<a href="#" class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a>
			<a href="<%=basePath%>spread/sms/addPage.do" class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">创建短信计划&nbsp;</font></a>


		</div>
	</div>

</body>
</html>

