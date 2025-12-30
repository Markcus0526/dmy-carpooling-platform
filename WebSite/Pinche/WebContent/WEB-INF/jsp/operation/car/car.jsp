<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/"+"operation/car/";
	System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
	<h2 class="page-title txt-color-blueDark"
		style="margin-left: 25px; margin-right: 10px">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;车辆管理
	</h2>
	<HR style="margin-left: 25px; margin-right: 10px"></HR>
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
</head>
<body onload="loaddone()">

	<script type="text/javascript" charset="UTF-8">
	//动态添加 车牌 与颜色的下拉列表
	
	$(function(){		
		$.ajax({
			url :'<%=basePath%>getbrand.do',
				dataType : "json",
				success : function(jsonObject) {
					var brand = jsonObject.brand;
					$.each(brand, function(i,obj) {
						
						$("#brandselect").append("<option value="+obj.brand+">"+obj.brand+"</option>");
					});						
				}
			});
		$.ajax({
			url :'<%=basePath%>getcolordesc.do',
				dataType : "json",
				success : function(jsonObject) {
					var color_desc = jsonObject.color_desc;
					$.each(color_desc, function(i,obj) {
						$("#color").append("<option value="+obj.color_desc+">"+obj.color_desc+"</option>");
					});						
				}
			});
		});
	
	function a(car_style){
		 $('#s1').val(car_style);
		 var brand = $('#b1').val();
		 //触发查询级别的函数
		 $.ajax({
				url :'<%=basePath%>findtype.do',
				method:'post',
				data:'car_style='+car_style+'&&brand='+brand,
					
					success : function(jsonObject) {
						 if(jsonObject.type==1){
							 document.getElementById('type1').checked=true;
						 }
						 if(jsonObject.type==2){
							 document.getElementById('type2').checked=true;
						 }
						 if(jsonObject.type==3){
							 document.getElementById('type3').checked=true;
						 }
						 if(jsonObject.type==4){
							 document.getElementById('type4').checked=true;
						
						 }
					}
				});	
	}
	
	function d(color_desc){
		 $('#c1').val(color_desc);
	}

	function c(brand){
		 document.getElementById("style").options.length=0;

	     $('#b1').val(brand);


	path ='<%=basePath%>';
		$.ajax({
			url :path+"getstylebybrand.do",
				data :"brand="+brand,
				dataType : "json",
				method:'post',
				success : function(jsonObject) {
					var style = jsonObject.car_style;
					$.each(style, function(i,obj) {
						if(obj.car_style!=""){
						$("#style").append("<option value="+obj.car_style+">"+obj.car_style+"</option>");
						}
					});						
				}
			});
		
	}
	//添加颜色
	function addc(){
		 $('#addc').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'添加颜色',
			    width:250,   
			    height:150,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:80,
			    content:	   
			    '<span>请输入颜色：</span><input type="text" id="color_desc" name="color_desc"/>'
				+' <input type="button" value="确定"style="width:50px;height:30px;" onclick="cl()"/>'
			    +'<input type="button" value="取消"style="width:50px;height:30px;" onclick="closec()"/>'
			 
		 });
		
		 
	}
	function cl(){

			
		
		if(confirm("确定添加？"))
		 {             
			var color_desc= $("#color_desc").val();
			$.ajax({
				url :'<%=basePath%>addcolor.do',
				method:'post',
				data:'color_desc='+color_desc,
					dataType : "json",
					success : function(jsonObject) {
						if(jsonObject.insame==1){
							alert("该颜色已经存在，请重新输入！！！");
						}else{
							 document.getElementById("color").options.length=0;//不做清空 会累加到页面上
								$.ajax({
									
									url :'<%=basePath%>getcolordesc.do',
										dataType : "json",
										success : function(jsonObject) {
											var color_desc = jsonObject.color_desc;
											$.each(color_desc, function(i,obj) {
												$("#color").append("<option value="+obj.color_desc+">"+obj.color_desc+"</option>");
											});						
										}
									});	
								closec();
							
						}
						
					}
				});	
		 }    
	}
	//添加品牌
	function addb(){
		 $('#addb').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'添加品牌',
			    width:250,   
			    height:150,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:80,
			    content:	   
			    '<span>请输入品牌：</span><input type="text" id="brand" name="brand"/>'
				+' <input type="button" value="确定"style="width:50px;height:30px;" onclick="b()"/>'
			    +'<input type="button" value="取消"style="width:50px;height:30px;" onclick="closeb()"/>'
			 
		 });
		 
	}
//确定添加品牌执行的函数
	function b(){
		 if(confirm("确定添加？"))
		 {             
			 var brand= $("#brand").val();
				$.ajax({
					url :'<%=basePath%>addbrand.do',
					method:'post',
					data:'brand='+brand,
						dataType : "json",
						success : function(jsonObject) {
							if(jsonObject.insame==1){
								alert("该品牌已经存在，请重新输入！！！");
							}else{
								 document.getElementById("brandselect").options.length=0;//不做清空 会累加到页面上
								 $.ajax({
										url :'<%=basePath%>getbrand.do',
									dataType : "json",
									success : function(jsonObject) {
										var brand = jsonObject.brand;
										$.each(brand, function(i, obj) {

											$("#brandselect").append(
													"<option value="+obj.brand+">"
															+ obj.brand + "</option>");
										});
									}
								});
								closeb();
								alert("添加成功！");
							}
							
						}
					});
				
		 }    
	 
		}
	//添加车型弹出窗口
	function adds(){
		 $('#adds').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'添加车型',
			    width:250,   
			    height:150,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:80,
			    content:	   
			    '<span>请输入车型：</span><input type="text" id="car_style" name="car_style"/>'
				+' <input type="button" value="确定"style="width:50px;height:30px;" onclick="s()"/>'
			    +'<input type="button" value="取消"style="width:50px;height:30px;" onclick="closes()"/>'
			 
		 });
		 
	}
	//确定添加车型时执行的函数
	function s(){
		 if(confirm("确定添加？"))
		 {             
			 var brand= $("#b1").val();
				var car_style= $("#car_style").val();
				path ='<%=basePath%>';
				$.ajax({
					url :'<%=basePath%>addstyle.do',
					data:'brand='+brand+'&&car_style='+car_style,
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
							if(jsonObject.insame==1){
								alert("该车型已经存在，请重新输入！！！");
							}else{
								 document.getElementById("style").options.length=0;//不做清空 会累加到页面上
								 $.ajax({
										url :path+"getstylebybrand.do",
											data :"brand="+brand,
											dataType : "json",
											method:'post',
											success : function(jsonObject) {
												var style = jsonObject.car_style;
												$.each(style, function(i,obj) {
													if(obj.car_style!=""){
														$("#style").append("<option value="+obj.car_style+">"+obj.car_style+"</option>");
														}
												});						
											}
										});
								 alert("添加成功！");
								closes();
								
							}
						
						}
					});
				
		 }    
		 else {         
			 alert("取消删除");         
			 return false;     
			 } 
		}
	//删除品牌
	function delb(){
		 if(confirm("确定删除？"))
		 {             
			 var brand= $("#b1").val();
				path ='<%=basePath%>';
				$.ajax({
					url :'<%=basePath%>delbrand.do',
					data:'brand='+brand,
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
							if(jsonObject.same==1){
								alert("当前品牌已被用户注册使用，不能删除!");
							}else{
								 document.getElementById("brandselect").options.length=0;//不做清空 会累加到页面上
									$.ajax({
										url :'<%=basePath%>getbrand.do',
											dataType : "json",
											success : function(jsonObject) {
												var brand = jsonObject.brand;
												$.each(brand, function(i,obj) {
													
													$("#brandselect").append("<option value="+obj.brand+">"+obj.brand+"</option>");
												});						
											}
										});
									 c(brand);
									 alert("已删除！");
								closes();
							}
							
						}
					});
				
		 }    
		 else {         
			 alert("取消删除");         
			 return false;     
			 } 
	}
	//删除颜色
	function delc(){
		 if(confirm("确定删除？"))
		 {             
			 var color_desc= $("#c1").val();
				$.ajax({
					url :'<%=basePath%>delcolor.do',
					data:'color_desc='+color_desc,
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
							if(jsonObject.same==1){
								alert("当前颜色已被用户注册使用，不能删除!");
							}else{
								 document.getElementById("color").options.length=0;//不做清空 会累加到页面上
								 $.ajax({
										url :'<%=basePath%>getcolordesc.do',
											dataType : "json",
											success : function(jsonObject) {
												var color_desc = jsonObject.color_desc;
												$.each(color_desc, function(i,obj) {
													$("#color").append("<option value="+obj.color_desc+">"+obj.color_desc+"</option>");
												});						
											}
										});
								 alert("已删除！");
									closes();
									
								
							}
							
						
						
						}
					});
				
		 }    
		 else {         
			 alert("取消删除");         
			 return false;     
			 } 
	}
	//删除车型
	function dels(){
		 if(confirm("确定删除？"))
		 {             
			 var brand= $("#b1").val();
				var car_style= $("#s1").val();
				$.ajax({
					url :'<%=basePath%>delstyle.do',
					data:'brand='+brand+'&&car_style='+car_style,
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
							if(jsonObject.same==1){
								alert("当前车型已被用户注册使用，不能删除!");
							}else{
								 document.getElementById("style").options.length=0;//不做清空 会累加到页面上
								 $.ajax({
										url :path+"getstylebybrand.do",
											data :"brand="+brand,
											dataType : "json",
											method:'post',
											success : function(jsonObject) {
												var style = jsonObject.car_style;
												$.each(style, function(i,obj) {
													$("#style").append("<option value="+obj.car_style+">"+obj.car_style+"</option>");
												});						
											}
										});
								 document.getElementById("brandselect").options.length=0;
								 $.ajax({
										url :'<%=basePath%>getbrand.do',
											dataType : "json",
											success : function(jsonObject) {
												var brand = jsonObject.brand;
												$.each(brand, function(i,obj) {
													
													$("#brandselect").append("<option value="+obj.brand+">"+obj.brand+"</option>");
												});						
											}
										});
									alert("已删除！");
								 closes();
								
							}
							
							
							
								
							
						}
					});
			
		 }    
		 else {         
			 alert("取消删除");         
			 return false;     
			 } 
	}
//保存
	function save(){
		 if(confirm("确定保存？"))
		 {   
			 var type = $("input[name='type']:checked").val();
			 var brand= $("#b1").val();
			 var car_style= $("#s1").val();
				$.ajax({
					url :'<%=basePath%>save.do',
					data:'brand='+brand+'&&car_style='+car_style+'&&type='+type,
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
							alert("保存成功");
						}
					});
		 }    
		 else {         
			 alert("取消保存");         
			 return false;     
			 } 
	}
		//关闭弹出窗
		function closec() {
			$('#addc').dialog("close");

		}
		function closeb() {

			$('#addb').dialog("close");
		}
		function closes() {

			$('#adds').dialog("close");
		}
		function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		} 
	</script>

	<table style="margin-left: 80px; margin-right: 15px" id="mytable">
		<tbody style="border-style: hidden;">
			<tr>
				<td>选择品牌</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>选择车型</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>设置级别</td>
			</tr>

			<tr>
				<td><select id="brandselect" name="brandselect" size="8"
					multiple="multiple" style="width: 120px;" onchange="c(this.value);"></select><br />

					<a id="1" onclick="addb()" style="width: 90px;"
					class="easyui-linkbutton" iconCls="icon-add">添加品牌</a> <br> <a
						id="2" onclick="return delb();" style="width: 90px;"
						class="easyui-linkbutton" iconCls="icon-remove">删除品牌</a></td>
				<td><a></a></td>
				<td><select id="style" name="style" size=8 multiple="multiple"
					style="width: 120px;" onchange="a(this.value);">

				</select> <br /> <a id="3" onclick="adds()" style="width: 90px;"
					class="easyui-linkbutton" iconCls="icon-add">添加车型</a> </br> <a id="4"
					onclick="return dels();" style="width: 90px;"
					class="easyui-linkbutton" iconCls="icon-remove">删除车型</a>
					</td>


					<td><a></a></td>
					<td><input id="type1" type="radio" name="type" value=1>经济型</br>
							<input id="type2" type="radio" name="type" value=2>舒适型</br> 
							<input id="type3" type="radio" name="type" value=3>豪华型</br>
							 <input  id="type4" type="radio" name="type" value=4>商务型</br></td>
					<td><a></a></td>
					<td><a id="5" onclick="save()"
						style="width: 70px; margin: 80px" class="easyui-linkbutton"
						iconCls="icon-save">保存</a> <a id="6"
						href="<%=basePath%>gotest2.do"
						style="width: 120px; margin-left: 100px;"
						class="easyui-linkbutton" iconCls="icon-xls">导出所有车型</a></td>
			</tr>
			<tr>
				<td><br> </br> <br> </br> <br> </br> <br> </br> </td>
			</tr>
			<tr>
				<td>颜色</td>
			</tr>
			<tr>
				<td><select id="color" name="color" size=8 multiple="multiple"
					style="width: 120px;" onchange="d(this.value)"></select><br />
					<div>
						<a id="7" onclick="addc()" style="width: 90px;"
							class="easyui-linkbutton" iconCls="icon-add">添加颜色</a> </br> <a id="8"
							onclick="return delc();" style="width: 90px;"
							class="easyui-linkbutton" iconCls="icon-remove">删除颜色</a>
					</div></td>
			</tr>
		</tbody>
	</table>
	<input type="text" id="b1" name="b1" style="visibility: hidden" />
	<input type="text" id="s1" name="s1" style="visibility: hidden" />
	<input type="text" id="c1" name="c1" style="visibility: hidden" />
	<input type="text" id="t1" name="t1" style="visibility: hidden" />
	<div id="addc"></div>
	<div id="addb"></div>
	<div id="adds"></div>
</body>

</html>