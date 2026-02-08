<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String basePath1 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() +"/img/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>身份认证</title>

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
		url : '<%=basePath%>customer/findPersonVerifiedInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				console.info(jsonObject);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#usercode').val(jsonObject.usercode);
				$('#username').val(jsonObject.username);
				if(jsonObject.sex==0){
					$('#sexSelect').combobox('setValues', '0');
				}else if(jsonObject.sex==1){
					$('#sexSelect').combobox('setValues', '1');
				}
				//---------个人认证信息----------------
				$('#nation').val(jsonObject.nation);
				$('#birthday').val(jsonObject.birthday);
				$('#id_card_num').val(jsonObject.id_card_num);
				$('#address').val(jsonObject.address);
				//------------身份认证图片---------------
				$('#id_card1').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.id_card1);
				$('#id_card2').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.id_card2);
				
			}
		});
	 $('#sexSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'sex',
			data : [ {
				label : '男',
				value : '0'
			}, {
				label : '女',
				value : '1'
			} ]

		});


	}); 
	
	 function personVerified(){
		var oo = isCardNo($("#id_card_num").val());
		if(oo){
			 $.ajax({
				   type: 'POST',
				   url: '<%=basePath%>customer/personVerified.do?id=${id}',
				   data:$('#personVerifiedForm').serialize(),
				   dataType:'json',
				   success:function(resultObject){
					   alert(resultObject);
		 			   if(resultObject.returnCode>0){
						    $.messager.alert('提示',resultObject.msg,'info',function(){
						    	window.history.go(-1);
						    });  
					   }else{
						   
						    $.messager.alert('提示',resultObject.msg,'info',function(){
						    	window.history.go(-1);
						    });
					   } 
				   } 
				});
		}else{
			return false;
		}
	}
	
	 function personVerifiedRejected(){
		 location = "<%=basePath%>customer/personVerifiedRejected.do?id=${id}";
// 		$.ajax({
// 		   type: 'POST',
<%-- 		   url: '<%=basePath%>customer/personVerifiedRejected.do?id=${id}', --%>
// 		   //data:$('#personVerifiedForm').serialize(),
// 		   dataType:'json',
// 		   success:function(resultObject){
//  			   if(resultObject.returnCode>0){
//  				   $.messager.alert('提示',resultObject.msg,'info',function(){
// 				    	window.history.go(-1);
// 				    });  
// 			   }else{
// 				    $.messager.alert('提示',resultObject.msg,'info',function(){
// 				    	window.history.go(-1);
// 				    });
// 			   } 
// 		   }
// 		});
	}
	
		function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		}
		
	    function isCardNo(card){  
	       // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
	       var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
	       if(reg.test(card) === false)  {  
	           alert("身份证输入不合法");  
	           return  false;  
	       }  
	       return true;
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
</script>
</head>

<body onload="loaddone()">
<input type="hidden" id="img11"/>

	<div title="身份认证信息" data-options="closable:true"
		style="overflow: auto; padding: 20px;">
		<form id="personVerifiedForm">
			<table>
				<tr align="center">
					<td>用&nbsp;户&nbsp;&nbsp;名：</td>
					<td align=left><input width="243px" type="text" id="usercode"
						name="usercode"></td>
				</tr>
				<tr align="center">
					<td>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
					<td align=left><input type="text" id="username" name="username"></td>
					<td>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td align=left><input type="text" id="sexSelect"
						name="sexSelect"></td>
				</tr>
				<tr align="center">
					<td>民&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;族：</td>
					<td align=left><input type="text" id="nation" name="nation"></td>
					<td>出生&nbsp;日期：</td>
					<td align=left><input type="text" id="birthday" Class="easyui-datetimebox" data-options="formatter:myformatter"
						name="birthday"></td>
				</tr>
				<tr align="center">
					<td>身份&nbsp;证号：</td>
					<td align=left><input type="text" id="id_card_num"
						name="id_card_num"></td>
					<td>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>
					<td align=left><input type="text" id="address" name="address"></td>
				</tr>
				<tr align="center">
					<td>身份证正面：</td>
					<td align=left><img src="" id="id_card1"></td>

					<td>身份证反面：</td>
					<td align=left><img src="" id="id_card2"></td>
				</tr>

			</table>
			<a id="personVerified" href="" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'"
				onclick="personVerified();return false;">验证通过</a> <a
				id="personVerifiedRejected" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancle'"
				onclick="personVerifiedRejected();return false;">验证不通过</a> <a
				id="close" href="" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancle'"
				onclick="javascript:history.go(-1);">关闭</a>
		</form>
	</div>
</body>
</html>
