<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">

<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="<%=basePath%>js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>js/themes/default/easyui.css"
	type="text/css"></link>
<script>
	var flag=false;
	function SetReadOnly(){
		var	platform=$('#platform').combobox('getValue');
			 if (platform == 0){
				//将input值置空
				   $('#url_scheme').val("");
			        //将input置为readOnly，注意是readOnly不是readonly，用后者不起作用的。   
			        document.all.url_scheme.readOnly = true; 
			 }
			 else{
			 document.all.url_scheme.readOnly = false;
			 }
			 }
	function closeApp(){
		window.location='<%=basePath%>operation/app/index.do';
	}
	function checkAppCode(){
		
		var  app_code=$('#app_code').val();
	 $.ajax({
		 url:'<%=basePath%>operation/app/checkAppCode.do?app_code='+app_code,
		 success:function(ret){
			 var row=ret.row;
			 if(row==0){
				 $('#content').val("你输入的APP编号不存在,或者已删除,无法进行更新"); 
				 $("#content").show();
				 flag=false;
			 }else{
				 $("#content").hide();
				 flag=true;
				 $('#app_name').val(ret.app_name);
				 $('#pack_name').val(ret.pack_name);
				 $('#platform').combobox('select',ret.platform);
				 SetReadOnly();
			 }
		 }
	 });
	 return flag;
	}
	 function submit1(thisForm){
		 
		 if(checkAppCode()==false){
			 return false;
		 }
		 aa();
		   if(window.confirm(msg)) {  
		   return true;
		   }else{
			   return false;
		   }
		   
	 }
	 function aa() {   
		
		 var percent;
		     $.post("<%=basePath%>operation/app/checkprogress.do", 
		    		 {}, function(data) {//AJAX方式发送请求到Action的sumPre方法，计算后将百分比data返回来   
		    	 percent=data.percent;
		    	 $('#p').progressbar({   
					    value: percent,
					    color:'LightBlue'
					});  
		         });  
		     if(percent==100){
		    	 
		     }else{
		     window.setTimeout("aa()", 1);//定时执行  
		     }
		 }  

	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">
<h2 class="page-title txt-color-blueDark">
	<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;更新APP
</h2>
	<form action="<%=basePath%>operation/app/updateApp.do" method="post"
		enctype="multipart/form-data" onSubmit="return submit1(this)">
		<table id="addTable"
			style="width: 800px; height: 400px; margin-left: 20px;">
			<tr>
				<td style="width: 150px; margin-left: 20px;"><span
					style="margin-left: 28px;">APP编号:</span></td>
				<td style="width: 600px;"><input type="text" id="app_code"
					name="app_code" Class="textbox" style="width: 150px; height: 22px"
					onblur="checkAppCode()"><input type="text" id="content"
					name="content"
					style="width: 300px; margin-left: 20px; background-color: transparent; border: 0px; display: none; color: red"
					readonly="readonly"></td>
			</tr>

			<tr>
				<td><span style="margin-left: 28px;">APP名称:</span></td>
				<td><input type="text" id="app_name" name="app_name"
					Class="textbox" style="width: 150px; height: 22px"></td>
			</tr>
			<tr>
				<td><span style="margin-left: 56px;">平台:</span></td>
				<td><select id="platform" name="platform"
					style="width: 150px; height: 22px;" disabled="disabled"
					Class="easyui-combobox" onchange="SetReadOnly()">
						<option value="0">Android</option>
						<option value="1">IOS</option>
				</select></td>
			</tr>
			<tr>
				<td><span style="margin-left: 30px;"> APP包名:</span></td>
				<td><input type="text" id="pack_name" name="pack_name"
					Class="textbox" style="width: 150px; height: 22px;"></td>
			</tr>
			<tr>
				<td><span style="margin-left: 40px;"> 版本号:</span></td>
				<td><input type="text" id="version" name="version"
					Class="textbox" style="width: 150px; height: 22px;"></td>
			</tr>
			<tr>
				<td><span style="margin-left: 5px;">URL Scheme:</span></td>
				<td><input type="text" id="url_scheme" name="url_scheme"
					Class="textbox" style="width: 150px; height: 22px;"></td>
			</tr>
			<tr>
				<td><span style="margin-left: 40px;">二维码:</span></td>
				<td><input type="file" id="qrcode_path" name="qrcode_path"
					style="width: 150px; height: 22px;"></td>
			</tr>
			<tr>
				<td><span style="margin-left: 40px;">安装包:</span></td>
				<td><input type="file" id="url" name="url" style="height: 22px">
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="上传" style="margin-left: 32px;"></td>
				<td><input type="reset" value="取消" onclick="closeApp()"
					style="margin-left: 10px; height: 22px"></td>
			</tr>
		</table>
		<div id="p" style="width: 400px; background-color: green"></div>
	</form>
	<script> document.all.url_scheme.readOnly = true; </script>
</body>

</html>