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
	var sms_plan_id="${id}";
	//var datagrid;
	$(function(){
		 var id ="${id}";
		$('#dg').datagrid({
     		url	:'<%=basePath%>spread/sms/getUnSelectUser.do',
				method : 'POST',
				title : '',
				iconCls : 'icon-save',
	     		pagination : true,
	     		queryParams:{"id":id},
	     		pageNumber : 1,
	     		pageSize : 5,
	     		pageList : [ 5, 10, 15, 20],
	     		//fit : true,
	     		height:300,
	     		fitColumns:true,
	     		nowrap :false,
	     		border :true,
	     		idFiled : 'id',
	     		checkOnSelect : true,
	     		striped : true,
	     		toolbar : '#content',
	     		columns : [[
	     	    {field : 'action',title : '操作',width : 80,align : 'center',
				formatter : function(value, row, index) {
				var str = '';
					str += '<a href="javascript:void(0)" onclick="selectid('+index+','+row.id+')"target="_self">选择</a> &nbsp';
					return str;
						if(row.id){return row.id;}
				}
				},
				{field : 'id',width : 40,align : 'center',title : 'id'},
				{field : 'usercode',width : 40,align : 'center',title : '用户名'},
				{field : 'username',width : 50,align : 'center',title : '姓名'}, 
				{field : 'phone',width : 70,align : 'center',title : '电话'},  
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
				} 	
		 	
				] ]
			});
			
		    	$('#dg1').datagrid({
		     		url	:'<%=basePath%>spread/sms/getSMSUserList.do',
		     			queryParams:{"id":id},
						method : 'POST',
						title : '',
						iconCls : 'icon-save',
			     		pagination : true,
			     		pageNumber : 1,
			     		pageSize : 5,
			     		pageList : [ 5, 10, 15 ],
			     		//fit : true,
			     		height:300,
			     		fitColumns:true,
			     		nowrap :false,
			     		border :true,
			     		idFiled : 'id',
			     		checkOnSelect : true,
			     		striped : true,
			     		toolbar : '#content',
			     		columns : [ [
						{field : 'action',title : '操作',width : 80,align : 'center',
						formatter : function(value, row, index) {
						var str = '';
							str += '<a href="javascript:void(0)" onclick="deletedid('+index+','+row.id+')"target="_self">移除</a> &nbsp';
							return str;
								if(row.id){return row.id;}
						}
						},
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
		    		$('#phoneLen').val(ret.client_num);
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
		
	function selectid(index,id){
	var	sms_plan_id=$('#id').val();
	alert(id);
	alert(index);
	 	$.ajax({
			url:'<%=basePath%>spread/sms/addSMSuser.do',
			data:{"userid":id,"sms_plan_id":sms_plan_id},
			success:function(ret){
				if(ret.error==0){
					alert("111111111");
					$('#dg1').datagrid('load');
					$('#dg').datagrid('load');
					var phonetotal=$('#phonetotal').val();
					$('#phonetotal').val(parseInt(phonetotal)+1)
				}else if(ret.error==1){
				}
			}
		}); 
	}
	function deletedid(index,userid){
		var	sms_plan_id=$('#id').val();
			$.ajax({
				url:'<%=basePath%>spread/sms/deleteSMSuser.do',
				data:{
					
					"userid":userid, 
					"sms_plan_id":sms_plan_id,
					},
				method:'post',
				success : function(ret){
					if(ret.error==0){
						alert("111111111");
						$('#dg1').datagrid('load');
						$('#dg').datagrid('load');
						var phonetotal=$('#phonetotal').val();
						$('#phonetotal').val(parseInt(phonetotal)-1);
					  }
				}
			});
	}
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
		var planPrice = Math.round(smsCnt)*smsPrice*parseInt(selCount);
		alert(planPrice);
		$('#price4sms').val(planPrice);
	}
	
	function saveEdit() {
		$('#planPrice').val($('#price4sms').text());
		$('#addPlanForm').ajaxSubmit({
			url: page_path + "edit.do",
			method:'post',
			success: function(result){
					window.location=page_path + "index.do";
			},
			complete:function(){
	
			}
		});
	}

	function btnCancel() {
			window.location=page_path + "index.do";
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
			alert("进入......");
			 var id ="${id}";
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
		    	id:id,
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
		function showMsgcount(){
			var number=0;
			var phoneList=$('#phoneList').val();
			var list=phoneList.split(",");
			 $.each(list,function(index,list1){
				 if(index!=0){
				 if(list1!=null && list1!=""){
					 number=number+1;
				 }
				 }
			 });
		   var num= $('#phoneLen').val();
		   var n=number+parseInt(num);
		   $('#phonetotal').val(n);
		}
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
		<div>
			<table id="dg"></table>
		</div>
			<div>
			<table id="dg1"></table>
		</div>
		</div>
	</div>
	<div  style="float:left;width:50%;height:auto">
		<div>

		<form id="addPlanForm" method="post">
			<input type="hidden" name="smsPrice" id="smsPrice" value="${price}"/> 
			<input type="hidden" name="phoneLen" id="phoneLen" />
				<input type="hidden" name="planPrice" id="planPrice" /> 
				<input 	type="hidden" name="clientNum" id="clientNum" /> 
				<input type="hidden" name="idList" id="idList" />
				<input 	type="hidden" name="sms_plan_id" id="sms_plan_id" value="${id}"/> 
			<table style="padding: 0px; border=0;height:500px">
				<tr>
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >计划编号:</font>
					<input id="id" name="id"  Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
					<font style="font-weight:normal;margin-left:10px" >计划类别:</font>
					<input id="send_mode" name="send_mode"Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
					<font style="font-weight:normal;margin-left:10px"  >已发送次数:</font>
					<input id="has_send_times" name="has_send_times" Class="textbox _textbox" style="height:20px;width:80px"  readOnly="readonly"/></td>				
				</tr>
				<tr>
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >号码列表:</font>
					<textarea id="phoneList" name="phoneList" Class="textbox _textbox"  style="width: 350px; height: 22px;" onChange="showMsgcount()"></textarea></td>
				</tr>
				<tr>
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >电话总数:</font>
					<input id="phonetotal" name="phonetotal" Class="textbox _textbox"  style="width: 80px;height:22px;" readOnly="readonly"/></td>
				</tr>
				<tr >
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >发送内容:</font>
					<textarea id="smsContent" name="smsContent" Class="textbox _textbox" style="width: 350px; height: 50px;" onChange="showMsgPrice()"></textarea>
					</td>
				</tr>
				<tr>
				
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >当前状态:</font>
				<select id="status" name="status" Class="easyui-combobox" Style="width: 150px; height: auto" >
					<option value="1">进行中</option>
					<option value="2">已完成</option>
				</select>
					<font style="font-weight:normal;margin-left:10px" >剩余次数:</font>
					<input id="surnumber" name="surnumber" Class="textbox _textbox" style="width:80px;height: 22px;" onChange="showMsgcount()" readOnly="readonly"></td>
				</tr>
			

				<tr >
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >成功数量:</font>
					<input id="successnumber" name ="successnumber" Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
					<font style="font-weight:normal;margin-left:10px" >失败数量:</font>
					<input id="failnumber" name ="failnumber" Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/>
				<font style="font-weight:normal;margin-left:10px" >消费总额:</font>
					<input id="totalPrice" name ="totalPrice" Class="textbox _textbox" style="height: 20px;width:80px"  readOnly="readonly"/></td>
				</tr>
				<tr >
					<td style="height: 10px;width:10%"><font style="font-weight:normal;margin-left:10px" >备注说明:</font>
					<textarea id="desc" name="desc" Class="textbox _textbox" style="width: 350px; height: 50px;"></textarea></td>
				</tr>
			</table>
		</form>
</div>
		<div style="margin-left: 200px; margin-top: 20px">
			<a href="javascript:void(0);" onclick="javascript:saveEdit();"
				style="margin-left: 20px; width: 120px;" icon="icon-ok"
				class="easyui-linkbutton">保&nbsp;存</a> <a href="javascript:void(0);"
				onclick="javascript:btnCancel();"
				style="margin-left: 50px; width: 80px;" icon="icon-cancel"
				class="easyui-linkbutton">取&nbsp;消</a>
		</div>

	</div>

</body>
</html>