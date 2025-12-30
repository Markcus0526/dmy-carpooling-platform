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

<title>车主认证</title>

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
		url : '<%=basePath%>customer/findDriverVerifiedInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				$('#usercode').val(jsonObject.usercode);
// 				$('#username').val(jsonObject.username);
				$('#drivinglicence_num').val(jsonObject.drivinglicence_num);
				$('#drlicence_ti').val(jsonObject.drlicence_ti);
/* 				$('#brand').val(jsonObject.brand);
				$('#style').val(jsonObject.style); */
				$('#brand').combobox('setValue', jsonObject.brand);
				$('#style').combobox('setValue', jsonObject.style);
				$('#color').val(jsonObject.color);
				$('#plate_num').val(jsonObject.plate_num);
				$('#vin').val(jsonObject.vin);
				$('#eno').val(jsonObject.eno);
				$('#vehicle_owner').val(jsonObject.vehicle_owner);
				if(jsonObject.is_oper_vehicle ==1){
					$('#oper_vehicle_Select').combobox('setValues', '1');
					}else{
					$('#oper_vehicle_Select').combobox('setValues', '0');
					}
				//------------身份认证图片---------------
				$('#driver_license1').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.driver_license1);
				$('#driver_license2').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.driver_license2);
				$('#driving_license1').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.driving_license1);
				$('#driving_license2').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.driving_license2);
				$('#car_img').attr('src',"http://124.207.135.69:8080/img/"+jsonObject.car_img);
				
			}
		});
	
  	$('#brand').combobox({   
	    url:'<%=basePath%>customer/findCarBrand.do',   
	    valueField:'brand',   
	    textField:'brand',
	   // value:jsonObject.brand,
		/* onShowPanel:function(){
			
			alert("下拉时触发");
		}, */
        onSelect: function(rec){
        	$.ajax({
        		url : '<%=basePath%>customer/findCarStyle.do',
        			type: 'POST',
        			data : 'brand='+rec.brand,
        			dataType : "json",
        			success : function(jsonObject) {
        				//console.info(jsonObject);
        				 //$('#style').combobox('reload', url);  
        				 $('#style').combobox({
        						data :jsonObject,
        					    valueField:jsonObject.car_style,   
        					    textField:jsonObject.car_style
        				 })
        			}
        	});
        }
	});  


		$('#oper_vehicle_Select').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'is_oper_vehicle',
			data : [ {
				label : '非运营车辆',
				value : '0'
			}, {
				label : '运营车辆',
				value : '1'
			} ]
		
		}); 

	}); 
	
	 function driverVerified(){
		//alert("sdfsdf");
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/driverVerified.do?id=${id}',
		   data:$('#driverVerifiedForm').serialize(),
		   dataType:'json',
		   success:function(resultObject){
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
	 }
	 function driverVerifiedRejected(){
	//alert("sdfsdf");
		$.ajax({
		   type: 'POST',
		   url: '<%=basePath%>customer/driverVerifiedRejected.do?id=${id}',
		   //data:$('#personVerifiedForm').serialize(),
		   dataType:'json',
		   success:function(resultObject){
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
	}
	
		function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		}
</script>
</head>

<body onload="loaddone()">
	<div title="车主认证信息" data-options="closable:true"
		style="overflow: auto; padding: 20px;">
		<form id="driverVerifiedForm">
			<table>
				<tr align="center">
					<td align=right>用户名：<input type="text" id="usercode" name="usercode"></td>
					<td align=right>姓名：<input type="text" id="username" name="username"></td>
				</tr>
				<tr align="center">
					<td align=right>驾照号码：<input type="text"
						id="drivinglicence_num" name="drivinglicence_num"></td>
					<td align=right>驾照获取时间：<input type="text" id="drlicence_ti"
						name="drlicence_ti"></td>
				</tr>
				<tr align="center">
					<td align=right>车辆品牌： <input id="brand" name="brand">
						<%-- 						<input id="brand" class="easyui-combobox" data-options="   
						        textField: 'brand',   
						        url: '<%=basePath%>customer/findCarBrand.do',   
						        onSelect: function(rec){   
						            var url = '<%=basePath%>customer/findCarStyle.do?brand='+rec.brand;   
						            $('#style').combobox('reload', url);   
						        }" />  --%>
					</td>
					<td align=right>车辆型号： <input id="style" name="style"
						class="easyui-combobox"
						data-options="valueField:'car_style',textField:'car_style'">
						<!-- <input id="style" class="easyui-combobox" data-options="textField:'car_style'" />   -->
					</td>
				</tr>
				<tr align="center">
					<td align=right>车辆颜色：<input type="text" id="color"
						name="color"></td>
					<td align=right>车辆号牌：<input type="text" id="plate_num"
						name="plate_num"></td>
				</tr>
				<tr align="center">
					<td align=right>车架号：<input type="text" id="vin" name="vin"></td>
					<td align=right>发动机号：<input type="text" id="eno" name="eno"></td>
				</tr>
				<tr align="center">
					<td align=right>车主姓名：<input type="text" id="vehicle_owner"
						name="vehicle_owner"></td>
					<td align=right>使用性质：<input type="text"
						id="oper_vehicle_Select" name="oper_vehicle_Select"></td>
				</tr>
				<tr align="center">
					<td align=right>驾驶证正面：<img src="" id="driver_license1"></td>
					<td align=right>驾驶证反面：<img src="" id="driver_license2"></td>
				</tr>
				<tr align="center">
					<td align=right>行驶证正面：<img src="" id="driving_license1"></td>
					<td align=right>行驶证反面：<img src="" id="driving_license2"></td>
				</tr>
				<tr align="center">
					<td align=right>车辆照片：<img src="" id="car_img"></td>
				</tr>

			</table>
			<a id="driverVerified" href="" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'"
				onclick="driverVerified();return false;">验证通过</a> <a
				id="driverVerifiedRejected" href="" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancle'"
				onclick="driverVerifiedRejected();return false;">验证不通过</a> <a
				id="close" href="" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancle'"
				onclick="javascript:history.go(-1);">关闭</a>
		</form>
	</div>


</body>
</html>
