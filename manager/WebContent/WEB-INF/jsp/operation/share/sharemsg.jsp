<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<%

String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"operation/share/";
System.out.println(basePath);

%>
<base href="<%=basePath%>">
<h2 class="page-title txt-color-blueDark">
	<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;分享文本管理
</h2>
<hr>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"
	charset="UTF-8"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/combobox.css"
	type="text/css"></link>
<script type="text/javascript">
$(function(){
	
		
		$.ajax({			
			url :'<%=basePath%>findvalue.do',
			dataType : "json",
			success : function(jsonObject) {
				$('#sinaTempText').val(jsonObject.rows[0].value);
				$('#smsTampText').val(jsonObject.rows[1].value);
				$('#wechatTempText').val(jsonObject.rows[2].value);
				var length=jsonObject.rows[1].value.length;
				countChar("smsTampText","counter");
				$('#counter').val(length);
				
				var length2 =jsonObject.rows[0].value.length;
				countChar("sinaTempText","counter1");
				$('#counter1').val(length2);
				
						}				
			});	


		
	});
	/**
	点击保存操作所触发的函数
	**/
	
function save(){
	if(confirm("确定保存？")){
		
		
		var smsTampText=$('#smsTampText').val();
		var sinaTempText=$('#sinaTempText').val();
		var wechatTempText=$('#wechatTempText').val();
		$.ajax({			
			url :'<%=basePath%>update.do',
			data:'sinaTempText='+sinaTempText+'&&smsTampText='+smsTampText+'&&wechatTempText='+wechatTempText,
			dataType : "json",
			method:'post',
			success : function(jsonObject) {
				$.ajax({			
					url :'<%=basePath%>findvalue.do',
					dataType : "json",
					success : function(jsonObject) {
						$('#sinaTempText').val(jsonObject.rows[0].value);
						$('#smsTampText').val(jsonObject.rows[1].value);
						$('#wechatTempText').val(jsonObject.rows[2].value);
						
				
						
								}				
					});	
				
						}	
			
			});	
		
		
	}
	alert("保存成功！");
	
	
	
}
/*
 *判断是否超过了70个字 
 */

function countChar(textareaName,a)
{ var num =document.getElementById(textareaName).value.length
	if(num==71){
		alert("字数超过限制，请分条发送！！！");
	}else{
		 document.getElementById(a).innerHTML = num;
	}

} 
/*
 *判断是否超过了140个字 
 */
function countChar1(textareaName,a)
{ var num =document.getElementById(textareaName).value.length
	if(num==141){
		alert("字数超过限制，请分条发送！！！");
	
	}else{
		 document.getElementById(a).innerHTML = num;
	}

} 
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
</script>
</head>
<body onload="loaddone()">
	<table
		style="padding: 10px; margin-right: 10px; margin-top: 10px; margin-left: 10px; margin-button: 10px;">
		<tr>
			<td style="width: 80px; height: 70px;"><span
				style="padding: 10px; margin-left: 10px;">短信:</span></td>
			<td><textarea id="smsTampText" cols="25" rows="3"
					style="margin-left: 10px; width: 700; height: 80;" maxlength="71"
					name="smsTampText" 
					onkeypress='countChar("smsTampText","counter");'
					onkeydown='countChar("smsTampText","counter");'
					onkeyup='countChar("smsTampText","counter");'></textarea>字数:<span
				id="counter" /></span>字<br /></td>
		</tr>
		<tr>
			<td></td>
			<td>说明：70字一条，支持通配符$name，$invitecode，$nickname</td>
		</tr>
		<tr>
			<td style="width: 80px; height: 70px;">新浪微博/腾讯微博:</td>
			<td><span style="width: 50; height: 50;"></span> <textarea
					id="sinaTempText" cols="25" rows="3"
					style="margin-left: 10px; width: 700; height: 80;" maxlength="141"
					name="sinaTempText"
					onkeypress='countChar1("sinaTempText","counter1");'
					onkeydown='countChar1("sinaTempText","counter1");'
					onkeyup='countChar1("sinaTempText","counter1");'></textarea>字数:<span
				id="counter1">140</span>字<br /></td>
		</tr>
		<tr>
			<td></td>
			<td>说明：最多140字，支持通配符$name，$invitecode，$nickname</td>
		</tr>
		<tr>
			<td style="width: 80px; height: 70px;">微信/朋友圈/QQ好友/QQ空间/陌陌/易信/邮件:</td>
			<td><textarea id="wechatTempText" cols="25" rows="3"
					style="margin-left: 10px; width: 700; height: 80;"
					name="wechatTempText"></textarea></td>
		</tr>
		<tr>
			<td></td>
			<td>说明：不限制字数，支持通配符$name，$invitecode，$nickname</td>
		</tr>
		<tr>
			<td style="width: 80px; height: 70px;"></td>
			<td><a href="javascript:void(0)" onclick="save()"
				style="margin-left: 50px; width: 80px;" class="easyui-linkbutton"
				iconCls="icon-save">保存</a></td>
		</tr>
	</table>
</body>
<html>