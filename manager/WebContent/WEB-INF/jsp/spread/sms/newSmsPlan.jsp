<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>创建短信计划</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/icon.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>  
<script type="text/javascript" charset="UTF-8">
	var page_path = '${pageContext.request.contextPath}/spread/sms/';
	var selectDlg;
	
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-tabs').tabs();
	$('.easyui-datetimebox').datetimebox();
	$('.easyui-combobox').combobox();
	var num=0;
	var number=0;
	//var datagrid;
	$(function(){
		
	    	$('#dg').datagrid({
	     		url	:'<%=basePath%>spread/sms/getUserList.do',
					method : 'POST',
					title : '',
					iconCls : 'icon-save',
		     		pagination : true,
		     		pageNumber : 1,
		     		pageSize : 10,
		     		pageList : [ 10, 20, 50 ],
		     		//fit : true,
		     		height:400,
		     		fitColumns:true,
		     		nowrap :false,
		     		border :true,
		     		checkOnSelect : true,
		     		striped : true,
		     		toolbar : '#content',
		     		columns : [[
	     		    {field : 'ck',
	     					checkbox : true
					},
					{field : 'id',width : 40,align : 'center',title : 'id'},
					{field : 'usercode',width : 40,align : 'center',title : '用户名'},
					{field : 'username',width : 50,align : 'center',title : '姓名'}, 
					{field : 'phone',width : 70,align : 'center',title : '电话'},  
					{field : 'group_name',width : 70,align : 'center',title : '所属集团'},
					{field : 'has_send_times',width : 50,align : 'center',title : '已发送次数',
						formatter : function(value, row, index) {
					       return row.successCount;
							}
					}, 
					{field : 'reg_date',width : 80,align : 'center',title : '注册时间',
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
					},
					{field : 'last_login_time',width :80,align : 'center',title : '最后登录时间',
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
					} 	
			 	
					] ],
				onCheck : function(index,row){
						
						var listid=$('#idList').val();
						var id =row.id;
						listid+=id+'';
						listid+=",";
						$('#idList').val(listid);
						 num=num+1;
						$('#selUserCnt').val(number+num);
					},
					onUncheck : function(index,row){
						var listid=$('#idList').val();
						var id =row.id;
						var list=listid.split(id+",",1000);
						listid=list[0]+list[1];
						$('#idList').val(listid);
						  num=num-1;
						$('#selUserCnt').val(number+num);
					},
					onCheckAll : function(rows){
						var listid="";
						num=0;
						$.each(rows, function(index, rows){
							listid+=rows.id+",";
							num+=1;
							}); 
						$('#idList').val(listid);
						$('#selUserCnt').val(number+num);
					},
					onUncheckAll : function(rows){
						num=0;
						$('#selUserCnt').val(number+num);
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
				});
		});
	
	onChangeMode({"value":"1", "text":"1"});
	
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
	}
	
	function showMsgPrice() {
		var smsMaxLen = 70;
		var smsPrice = "${price}";
		var selCount = $('#selUserCnt').val();
		var smsLen = document.getElementById("smsContent").value.length;
		if (smsLen == 0) {
			$('#price4sms').text(0);
			return;
		}
		var smsCnt = (parseInt(smsLen)-1)/parseInt(smsMaxLen)+1;
		$('#smsPrice').val(Math.round(smsCnt)*smsPrice);
		var planPrice = Math.round(smsCnt)*smsPrice*parseInt(selCount);
		$('#price4sms').val(planPrice);
	}
	function showMsgcount(){
		number=0;
		var phoneList=$('#phoneList').val();
		var list=phoneList.split(/\，|\,|\n/);
		 $.each(list,function(index,list1){
			 if(list1!=null && list1!=""){
				 number=number+1;
			 }
		 });
	   $('#selUserCnt').val(number+num);
	}
	function btnSend() {
		$('#planPrice').val($('#price4sms').text());
		$('#addPlanForm').ajaxSubmit({
			url: page_path + "add.do",
			method:'post',
			success: function(result){
				if(result.err_code==0){
					alert("添加成功");
					window.location=page_path + "index.do";
				}
				if(result.err_code==1){
					alert("添加失败");
				}
			},
			complete:function(){
	
			}
		});
	}
	
	function selUserAll(){
		var idlist=$('#dg').datagrid('getData','idlist').idlist;
		var total=$('#dg').datagrid('getData','total').total;
		$('#idList').val(idlist);
		$('#selUserCnt').val(total);
	}

	function btnCancel() {
			window.location=page_path + "index.do";
	}

	function onChangeMode(record) {
		var data = [];
		var regularSendMode = record['value'];
		var dayNames = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
		var dayValues = [0, 1, 2, 3, 4, 5, 6];
		$('#time1').combobox('clear');
		$('#time2').combobox('clear');
		
		if (regularSendMode == 1) {
			for (var i=0; i < 24; i++) {
				var row = {};
				row['value'] = i;
				row['text'] = i;
				
				data.push(row);
			}
			$('#time1').combobox('loadData', data);
			
			data = [];
			for (var i=0; i < 60; i+=5) {
				var row = {};
				row['value'] = i;
				row['text'] = i;
				data.push(row);
			}
			$('#time2').combobox('loadData', data);
			$('#time1').combobox('select', 0);
			$('#time2').combobox('select', 0);
			
			$('#forTime1').text('点');
			$('#forTime2').text('分');
		} else if (regularSendMode == 2) {
			data = [];
			for (var i=0; i < 7; i++) {
				var row = {};
				row['value'] = dayValues[i];
				row['text'] = dayNames[i];
				data.push(row);
			}
			$('#time1').combobox('loadData', data);
			
			data = [];
			for (var i=0; i < 24; i++) {
				var row = {};
				row['value'] = i;
				row['text'] = i;
				data.push(row);
			}
			$('#time2').combobox('loadData', data);
			$('#time1').combobox('select', 1);
			$('#time2').combobox('select', 0);
	
			$('#forTime1').text('    ');
			$('#forTime2').text('点');
		} else {
			data = [];
			for (var i=0; i < 31; i++) {
				var row = {};
				row['value'] = i + 1;
				row['text'] = i + 1;
				data.push(row);
			}
			$('#time1').combobox('loadData', data);
			
			data = [];
			for (var i=0; i < 24; i++) {
				var row = {};
				row['value'] = i;
				row['text'] = i;
				data.push(row);
			}
			$('#time2').combobox('loadData', data);
			$('#time1').combobox('select', 1);
			$('#time2').combobox('select', 0);
	
			$('#forTime1').text('日');
			$('#forTime2').text('点');
		}
	}
	
	function onAddSuccess(selCount, phoneList, idList){
		$('#selUserCnt').text(selCount);
		$('#clientNum').val(selCount);
		$('#phoneList').val(phoneList);
		$('#idList').val(idList);
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
	
		function searchUser() {
		    var cityItem = $('#cityItem').combobox('getValue');
		    var cityName = $('#cityName').val();
		    var groupItem = $('#groupItem').combobox('getValue');
		    var groupName = $('#groupName').val();
		    var userItem = $('#userItem').combobox('getValue');
		    var userInfo = $('#userInfo').val();
		    var userType = $('#userType').combobox('getValue');
		    var fromRegTime = $('#fromRegTime').datetimebox('getValue');
		    var toRegTime = $('#toRegTime').datetimebox('getValue');
		    var fromLoginTime = $('#fromLoginTime').datetimebox('getValue');
		    var toLoginTime = $('#toLoginTime').datetimebox('getValue');
		    $('#dg').datagrid('load',{
		    	cityItem :cityItem,
		    	cityName  :cityName,
		    	groupItem :groupItem,
		    	groupName :groupName,
		    	userItem  :userItem,
		    	userInfo : userInfo,
		    	userType :userType,
		    	fromRegTime :fromRegTime,
		    	toRegTime  :toRegTime,
		    	fromLoginTime :fromLoginTime,
		    	toLoginTime :toLoginTime
		    });
			
		}
	
	 	function createQueryParams() {
			var tags = [ "input", "select" ];
			var form = $("#searchform");
			var params = [];
			for ( var i in tags) {
				var collection = form.find(tags[i]);
				for (var j = 0; j < collection.length; j++) {
					var item = $(collection[j]);
					var name = item.prop("name");
					if (name != "") {
						if (tags[i] == "input") {
							if (item.prop("type") == "radio") {
								if (!item.prop("checked")) {
									continue;
								}
							}
						}
						params[name] = item.val();
					}
				}
			}
			return params;
		}
/*  	function countChar(textareaName,a)
 	{  
 	 document.getElementById(a).innerHTML = document.getElementById(textareaName).value.length;
 	}  */
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body  onload="loaddone()">
	<div style="float:left;width:50%;height:auto;">
<!-- 	<div data-options="region:'north',title:' '"
		style="padding:1px; background:#eee;width:45%"> -->
		<!-- 	<div data-options="region:'north',title:' ',split:true"
		style="width: 100px;"> -->
		<div id="selectDlg" style="padding: 0px; height: 250;">
			<form id="searchform">
			<div style="position:relative;border:0px red solid;width:100%;height:100px;margin-left:auto;margin-right:auto;margin-top:5px">
				<div style="position:absolute;top:0px;left: 0%;border:0px red solid;width:30%;height:25px;">
					<select id="cityItem" name="cityItem" class="easyui-combobox" style="width: 100%; height: 20px">
						<option value="1">注册城市</option>
						<option value="2">最后登录城市</option>
						<option value="3">注册或最后登录城市</option>
					</select>
				</div>
				<div style="position:absolute;top:0px;left:30%;border:0px red solid;width:20%;height:25px;">
					<input id="cityName" name="cityName" class="easyui-validatebox textbox" style="width: 90%;height:20px"></input>
				</div>
				<div style="position:absolute;top:0px;left:50%;border:0px red solid;width:30%;height:25px;">
						<select id="groupItem" name="groupItem" class="easyui-combobox"
								style="width: 100%; height: auto">
							<option value="1">按集团/联盟标识查询</option>
							<option value="2">按集团/联盟名称查询</option>

						</select>
				</div>
				<div style="position:absolute;top:0px;left:80%;border:0px red solid;width:20%;height:25px;">
					<input  id="groupName" name="groupName" class="textbox" class="easyui-validatebox textbox" style="width:90%; height: 20px"></input>
				</div>
				
				<div style="position:absolute;top:25px;left: 0%;border:0px red solid;width:30%;height:25px;">
						<select id="userItem" name="userItem" class="easyui-combobox"
								style="width: 100%; height: auto">
									<option value="1">按客户ID查询</option>
									<option value="2">按客户姓名查询</option>
									<option value="3">按客户手机查询</option>
						</select>
				</div>
				<div style="position:absolute;top:25px;left:30%;border:0px red solid;width:20%;height:25px;">
					<input id="userInfo" name="userInfo" class="easyui-validatebox textbox" style="width:90%;height: 20px"></input>
				</div>
				<div style="position:absolute;top:25px;left:50%;border:0px red solid;width:30%;height:25px; text-align:center">
					<font style="font-weight:normal;font-size:80%" >客户身份:</font>
				</div>
				<div style="position:absolute;top:25px;left:80%;border:0px red solid;width:20%;height:25px;">
						<select id="userType"  name="userType" class="easyui-combobox" style="width: 90%; height: 20px">
									<option value="1">乘客</option>
									<option value="2">车主</option>
						</select>
				</div>
				
				<div style="position:absolute;top:50px;left: 0%;border:0px red solid;width:100%;height:25px;">
					<span style="margin-left:0px;width:20%;text-align:center; line-height:25px; float:left"><font style="font-weight:normal;font-size:80%" >注册时间:</font></span>
					<input id="fromRegTime" name="fromRegTime" type="text" class="easyui-datetimebox" style="width:25%;height: 20px"/>&nbsp;~&nbsp;
					<input id="toRegTime" name="toRegTime" type="text" class="easyui-datetimebox" style="width:25%; height: 20px"/>
				</div>
				
				<div style="position:absolute;top:75px;left: 0%;border:0px red solid;width:100%;height:25px;">
					<span style="margin-left:0px;width:20%;text-align:left; line-height:25px; float:left"><font style="font-weight:normal;font-size:80%" >最后登录时间:</font></span>
					<input id="fromLoginTime" name="fromLoginTime" class="easyui-datetimebox" style="width:25%; height: 20px" data-options="formatter:myformatter"/>&nbsp;~&nbsp;
					<input id="toLoginTime" name="toLoginTime" class="easyui-datetimebox" style="width:25%; height: 20px" data-options="formatter:myformatter"/>
					<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="javascript:searchUser();" style="float:right">查&nbsp;询</a>
				</div>
			
			
			</div>
			</form>

			<table id="dg"></table>
		</div>

		<div style="padding: 10px; width: auto; overflow: auto;">
			<!-- <div id="userList" style="padding: 10px"></div> -->
			<font style="font-weight:normal;font-size:80%" >符合条件的用户数:</font> <label id="total_size"
				style="margin-left: 5px; font-size: 13px; color: red"></label> <a
				href="#"  class="easyui-linkbutton"
				onclick="javascript:selUserAll();">选择所有符合条件的客户</a>
		</div>
	</div>
	<div  style="float:left;width:50%;height:auto">
		<div>

		<form id="addPlanForm" method="post">
			<input type="hidden" name="smsPrice" id="smsPrice" value="${price}"/> 
			<input type="hidden" name="smsMaxLen" id="smsMaxLen" />
				<input type="hidden" name="planPrice" id="planPrice" /> 
				<input 	type="hidden" name="clientNum" id="clientNum" /> 
				<input type="hidden" name="idList" id="idList" />

			<table style="padding: 0px" border=0>
				<tr>
					<td style="height: 10px;width:30%"><font style="font-weight:normal;font-size:80%" >客户数量:</font></td>
					<td><input id="selUserCnt" name="selUserCnt" style="font-size: 25px; color: red" readOnly="readonly"/></td>
				</tr>

				<tr>
					<td style="height: 10px;width:30%"><font style="font-weight:normal;font-size:80%" >号码列表:</font></td>
					<td><textarea id="phoneList" name="phoneList"  rows="60" cols="500"  style="width: 500px; height: 60px;" onChange="showMsgcount()"></textarea></td>
				</tr>

				<tr>
					<td style="height: 10px;width:30%"></td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;<font style="font-weight:normal;font-size:80%" >说明：允许分隔符“中英文逗号、回车符”</font></td>
				</tr>

				<tr >
					<td style="height: 10px;width:30%"><font style="font-weight:normal;font-size:80%" >内容:</font></td>
					<td><textarea id="smsContent" name="smsContent" style="width: 500px; height: 60px;" onChange="showMsgPrice()"></textarea>
					</td>
				</tr>
					<!-- onblur='countChar("sinaTempText","counter1");'
					onkeypress='countChar("sinaTempText","counter1");'
					onkeydown='countChar("sinaTempText","counter1");'
					onkeyup='countChar("sinaTempText","counter1");' --> 
				<tr>
					<td style="height: 10px;width:30%"></td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;<font style="font-weight:normal;font-size:80%" >注：每条短信限70字符，超过70字符时按2条计费，超过140字符时按3条计费，以此类推！</font></td>
				</tr>

				<tr >
					<td style="height: 10px;width:30%"><font style="font-weight:normal;font-size:80%" >预计费用:</font></td>
					<td><input id="price4sms" name ="price4sms" style="font-size: 25px; color: red"  readOnly="readonly"/></td>
				</tr>
				<tr>
					<td style="height: 10px;width:30%"><font style="font-weight:normal;font-size:80%" >发送模式:</font></td>
					<td>

						<div class="easyui-tabs rightTd" data-options="tabWidth:112"
							style="width: 500px; height: 150px">

							<div title="单次发送" style="padding: 30px 50px 10px 50px">
								<label >设定时间:</label> <input name="sendTime"
									class="easyui-datetimebox" style="width: 145px"
									data-options="formatter:myformatter"></input> <br /> <br /> <span>说明：输入时间则定时发送，不输入时间则立即发送</span>
							</div>

							<div title="定期发送" style="padding: 10px 50px 10px 50px">

								<div style="padding: 10px 0px 0px 0px">
									<label >发送次数:</label>
									 <input name="limitTimes" id="limitTimes"  class="textbox" style="width: 145px"></input> <span>&nbsp;&nbsp;说明：不输入表示持续发送</span>
								</div>

								<div style="padding: 10px 0px 0px 0px">
									<label >发送时间:</label>
									 <select name="regularSendMode" id="regularSendMode" class="easyui-combobox" style="width: 80px; height: auto" data-options="onSelect:onChangeMode">
										<option value="1">每天</option>
										<option value="2">每周</option>
										<option value="3">每月</option>
									</select>
								</div>

								<div style="padding: 10px 0px 0px 0px; margin-left: 65px">

									<select name="time1" id="time1" class="easyui-combobox"
										style="width: 60px; height: auto">
										<c:forEach var="i" begin="0" end="23" step="1">
											<option value="${i}">${i}</option>
										</c:forEach>
									</select> <label id="forTime1" style="width:40px">点</label> <select
										name="time2" id="time2" class="easyui-combobox"
										style="width: 60px; height: auto">
										<c:forEach var="i" begin="0" end="55" step="5">
											<option value="${i}">${i}</option>
										</c:forEach>
									</select> <label id="forTime2" style="width: 40px">分</label>
								</div>

							</div>
						</div>

					</td>
				</tr>

				<tr >
					<td style="height: 10px;width:30%"><font style="font-weight:normal;font-size:80%" >备注说明:</font></td>
					<td><textarea name="desc" style="width: 500px; height: 60px;"></textarea></td>
				</tr>
			</table>
		</form>
</div>
		<div style="margin-left: 200px; margin-top: 20px">
			<a href="javascript:void(0);" onclick="javascript:btnSend();"
				style="margin-left: 50px; width: 120px;" icon="icon-ok"
				class="easyui-linkbutton">创建计划/发送</a> <a href="javascript:void(0);"
				onclick="javascript:btnCancel();"
				style="margin-left: 50px; width: 80px;" icon="icon-cancel"
				class="easyui-linkbutton">取&nbsp;消</a>
		</div>

	</div>

</body>
</html>