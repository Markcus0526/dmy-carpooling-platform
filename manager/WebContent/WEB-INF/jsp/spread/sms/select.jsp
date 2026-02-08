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
		   var id ="${id}";
	    	$('#dg').datagrid({
	     		url	:'<%=basePath%>spread/sms/getSMSUserList.do',
	     		queryParams:{"id":id},
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
		     		columns : [ [
					{field : 'id',width : 40,align : 'center',title : 'id'},
					{field : 'usercode',width : 40,align : 'center',title : '用户名'},
					{field : 'username',width : 50,align : 'center',title : '姓名'}, 
					{field : 'phone',width : 70,align : 'center',title : '电话'}, 
					{field : 'isdriver',width : 70,align : 'center',title : '是否是车主',
						formatter: function(value,row,index){
							if(row.driver_verified==1){
								return "是车主";
							}else{
								return "不是车主";
							}
						},
					},
					{field : 'group_name',width : 70,align : 'center',title : '所属集团'},
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
					} ,	
					{field : 'scnt',width : 70,align : 'center',title : '发送成功次数'}, 
					{field : 'fcnt',width : 70,align : 'center',title : '发送失败次数'},  
					] ],
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
	    	$.ajax({
	    		url :'<%=basePath%>spread/sms/show.do',
	    		data:{"id":id},
	    		method : 'POST',
	    		success:function(ret){
	    		$('#id').val(ret.id);
	    		if(ret.send_mode==1){
					$('#send_mode').val("一次发送");
				}if(ret.send_mode==2){
					$('#send_mode').val("周期发送");
				}
	    		
	    		$('#has_send_times').val(ret.has_send_times);
	    		$('#phoneList').val(ret.phoneList);
	    		$('#phonetotal').val(ret.client_num);
	    		$('#smsContent').val(ret.msg);
	    		if(ret.send_mode==1){
	    			$('#status').combobox('select',2);
	    			$('#surnumber').val(0);
	    			$('#totalPrice').val(ret.price*ret.successnumber);
	    		}if(ret.send_mode==2){
	    			if(ret.limit_times>ret.has_send_times){
	    				$('#status').combobox('select',1);
	    			}else{
	    			$('#status').combobox('select',2);
	    			}
	    			$('#surnumber').val(ret.limit_times-ret.has_send_times);
	    			$('#totalPrice').val(ret.price*ret.successnumber);
	    		}
	    		$('#successnumber').val(ret.successnumber);
	    		$('#failnumber').val(ret.failnumber);
	    		
	    		$('#desc').val(ret.remark);
	    			
	    		}
	    	});
		});
	
	
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
	}

	function btnCancel() {
			window.location=page_path + "index.do";
	}

	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">

	<div  style="float:roght;height:auto">
		<div>

		<form id="addPlanForm" method="post">
			<input type="hidden" name="smsPrice" id="smsPrice" value="${price}"/> 
			<input type="hidden" name="smsMaxLen" id="smsMaxLen" />
				<input type="hidden" name="planPrice" id="planPrice" /> 
				<input 	type="hidden" name="clientNum" id="clientNum" /> 
				<input type="hidden" name="idList" id="idList" />

			<table style="padding: 0px; border=0;height:400px">
				<tr>
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >计划编号:</font>
					<input id="id" name="id"  Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
					<font style="font-weight:normal;margin-left:30px" >计划类别:</font>
					<input id="send_mode" name="send_mode"Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
					<font style="font-weight:normal;margin-left:30px"  >已发送次数:</font>
					<input id="has_send_times" name="has_send_times" Class="textbox _textbox" style="height:20px;width:80px"  readOnly="readonly"/></td>				
				</tr>
				<tr>
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >号码列表:</font>
					<textarea id="phoneList" name="phoneList" Class="textbox _textbox"  style="width: 500px; height: 22px;" readOnly="readonly"></textarea></td>
				</tr>
				<tr>
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >电话总数:</font>
					<input id="phonetotal" name="phonetotal" Class="textbox _textbox"  style="width: 80px;height:22px;" readOnly="readonly"/></td>
				</tr>
				<tr >
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >发送内容:</font>
					<textarea id="smsContent" name="smsContent" Class="textbox _textbox" style="width: 500px; height: 40px;" readOnly="readonly"></textarea>
					</td>
				</tr>
				<tr>
				
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >当前状态:</font>
				<select id="status" name="status" Class="easyui-combobox"Style="width: 150px; height: auto" readOnly="readonly">
					<option value="1">进行中</option>
					<option value="2">已完成</option>
				</select>
					<font style="font-weight:normal;margin-left:20px" >剩余次数:</font>
					<input id="surnumber" name="surnumber" Class="textbox _textbox" style="width:80px;height: 22px;" readOnly="readonly"></td>
				</tr>
			

				<tr >
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >成功数量:</font>
					<input id="successnumber" name ="successnumber" Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
					<font style="font-weight:normal;margin-left:20px" >失败数量:</font>
					<input id="failnumber" name ="failnumber" Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
				<font style="font-weight:normal;margin-left:20px" >消费总额:</font>
					<input id="totalPrice" name ="totalPrice" Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/></td>
				</tr>
				<tr >
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:20px" >备注说明:</font>
					<textarea id="desc" name="desc" Class="textbox _textbox" style="width: 500px; height: 40px;" readOnly="readonly"></textarea></td>
				</tr>
			</table>
		</form>
</div>
<div>
  <table id="dg">
  
  </table>
</div>
		<div style="margin-left: 200px; margin-top: 20px">
		<a href="javascript:void(0);" onclick="javascript:btnCancel();" style="margin-left: 10px; width: 80px;" icon="icon-cancel" class="easyui-linkbutton">关&nbsp;闭</a>
		</div>

	</div>

</body>
</html>