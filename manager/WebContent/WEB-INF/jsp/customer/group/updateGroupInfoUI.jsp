<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<title>修改集团信息</title>

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
				console.info(jsonObject);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#groupid').val(jsonObject.groupid);
				$('#group_name').val(jsonObject.group_name);
				$('#create_date').val(jsonObject.create_date);
				$('#linkname').val(jsonObject.linkname);
				$('#group_property').val(jsonObject.group_property);
				if(jsonObject.group_property == '0'){
					  $("#group_property option[value='0']").attr("selected",true);
				}else{
					  $("#group_property option[value='1']").attr("selected",true);
				}
				$('#linkphone').val(jsonObject.linkphone);
				$('#contract_no').val(jsonObject.contract_no);
				$('#fix_phone').val(jsonObject.fix_phone);
				$('#email').val(jsonObject.email);
				$('#fax').val(jsonObject.fax);
				$('#group_address').val(jsonObject.group_address);
				//$('#sign_time').val(jsonObject.sign_time);
				$('#sign_time').datebox('setValue', jsonObject.sign_time);
				$('#invitecode_self').val(jsonObject.invitecode_self);
				if(jsonObject.active_as_driver_self==0){
				$('#radioSelect').combobox('setValues', '0');
				$('#radioSelectInput').val(jsonObject.integer_as_driver_self);
				}else{
				$('#radioSelect').combobox('setValues', '1');
				$('#radioSelectInput').val(jsonObject.ratio_as_driver_self);
				}
				$('#remark').val(jsonObject.remark);
			}
		});

 		radioSelect = $('#radioSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'active_as_driver_self',
			data : [ {
				label : '点',
				value : '0'
			}, {
				label : '%',
				value : '1'
			} ]

		}); 


	}); 

function updateGroupInfo(){
		$.ajax({
		    type: 'POST',
		    url: '<%=basePath%>customer/updateGroupInfo.do?id=${id}',
		    data:$('#groupInfoForm').serialize(),
		    dataType:'json',
		    success:function(resultObject){
//  			   if(resultObject.returnCode>0){
				    $.messager.alert('提示',"修改成功！",'info',function(){
				    	window.history.go(-1);
				    });  
// 			   }else{
				   
// 				    $.messager.alert('提示',resultObject.msg,'info',function(){
// 				    	window.history.go(-1);
// 				    });
// 			   } 
		   }
/* 		   success: function(data){
		   $.messager.alert('提示','修改成功！','info',function(){
		    	window.history.go(-1);
		    	
		    });	
 		  // console.info($('#groupInfoForm').serialize());
		    // alert( "修改成功！ "  ); 
		   } */
		});

}


function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
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
		if((value>=0)&&(value<=100))
			return true;
		return false;
	},
	message: '请输入0~100的正整数！'
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
	<div id="tt" class="easyui-tabs" style="width: 800px; height: 450px;"
		fit=true>

		<div title="修改集团信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<form id="groupInfoForm">
				<table border=0>
					<tr align="center">
						<td align=right>集&nbsp;团&nbsp;标&nbsp;识：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="groupid" name="groupid" readOnly="readonly"></td>
						<td align=right>合&nbsp;同&nbsp;编&nbsp;号：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="contract_no" name="contract_no"></td>

					</tr>
					<tr align="center">

						<td align=right>集&nbsp;团&nbsp;名&nbsp;称：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
						class="easyui-validatebox textbox" data-options="required:true"
							id="group_name" name="group_name"></td>
						<td align=right>集&nbsp;团&nbsp;性&nbsp;质：</td>
						<td style="width: 100px; height: 15px;">
						   <select name="group_property" id="group_property">
						    	<option value="0" selected="true">集团车主</option>
						    	<option value="1">集团乘客</option>
					    	</select>
						</td>
					</tr>
					<tr align="center">
						<td align=right>联&nbsp;系&nbsp;人：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="linkname" name="linkname"></td>
						<td align=right>联&nbsp;系&nbsp;电&nbsp;话：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
						class="easyui-validatebox textbox" data-options="required:true"
							id="linkphone" name="linkphone"></td>

					</tr>
					<tr align="center">

						<td align=right>固&nbsp;定&nbsp;电&nbsp;话：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="fix_phone" name="fix_phone"></td>
						<td align=right>Email：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="email" name="email"></td>
					</tr>
					<tr align="center">
						<td align=right>传&nbsp;真&nbsp;号&nbsp;码：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="fax" name="fax"></td>
						<td align=right>集&nbsp;团&nbsp;地&nbsp;址：</td>
						<td style="width: 100px; height: 15px;"><input type="text"
							id="group_address" name="group_address"></td>
					</tr>
					<tr align="center">
						<td align=right>签&nbsp;约&nbsp;时&nbsp;间：</td>
						<td style="width: 100px; height: 15px;"><input type="text" class="easyui-datetimebox" data-options="formatter:myformatter,required:true" 
							id="sign_time" name="sign_time">
						</td>
						<td align=right>默认分成比例：</td>
						<td align=left style="width: 100px; height: 15px;"><input
							type="text" id="radioSelectInput" name="radioSelectInput" class="easyui-validatebox textbox"
						validType="TimeRange" data-options="required:true"
							style="width: 75px;"><input id="radioSelect"
							name="radioSelect" style="width: 75px;"></td>
					</tr>
					<tr align="center">
						<td align=right>邀&nbsp;请&nbsp;码：</td>
						<td style="width: 100px; height: 15px;"><input type="text" class="easyui-validatebox textbox" 
							data-options="required:true" validType="num"
							id="invitecode_self" name="invitecode_self">
						</td>
					</tr>
					<tr>
						<td align=right>备&nbsp;&nbsp;注：</td>
						<td style="width: 100px; height: 15px;" colspan=3><textarea
								rows="2" cols="50" id="remark" name="remark"></textarea></td>

					</tr>
				</table>
			</form>
			<a id="save" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'" onclick="updateGroupInfo()">保存</a>
			<a id="cancel" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'"
				onclick="javascript:history.go(-1)">关闭</a>

		</div>
	</div>
</body>
</html>
