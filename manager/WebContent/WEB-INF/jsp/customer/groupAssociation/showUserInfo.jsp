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

<title>查看用户信息</title>

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

/* 		var _date = jsonObject.birthday;
		var newdate = new Date(_date);
		var str="";     
		str+=newdate.getYear()+"-";     
		str+=(newdate.getMonth()+1)+"-";     
		str+=newdate.getDate();    
		alert(str);   */
		
		
 $(function(){

	$.ajax({
		url : '<%=basePath%>customer/findUserAllInfo.do',
			data : 'id=${id}',
			dataType : "json",
			success : function(jsonObject) {
				console.info(jsonObject);
				//console.info($('#Name'));
				//--------客户基本信息---------------
				$('#id').val(jsonObject.id);
				$('#usercode').val(jsonObject.usercode);
				$('#nickname').val(jsonObject.nickname);
				$('#username').val(jsonObject.username);
				$('#phone').val(jsonObject.phone);
				if(jsonObject.sex==1){
				$('#sex').val("男");
				$('#person_sex').val("男");
				}else if(jsonObject.sex==0){
				$('#sex').val("女");
				$('#person_sex').val("女");
				}
				$('#phone').val(jsonObject.phone);
				$('#img').attr('src',jsonObject.img);
				$('#remark').val(jsonObject.remark);
				//---------个人认证信息----------------
				$('#person_username').val(jsonObject.username);
				$('#nation').val(jsonObject.nation);
				$('#birthday').val(jsonObject.birthday);
				$('#id_card_num').val(jsonObject.id_card_num);
				$('#address').val(jsonObject.address);
				//------------身份认证图片---------------
				$('#id_card1').attr('src',jsonObject.id_card1);
				$('#id_card2').attr('src',jsonObject.id_card2);
				//------------车主认证信息-------------------------
				$('#drivinglicence_num').val(jsonObject.drivinglicence_num);
				$('#drlicence_ti').val(jsonObject.drlicence_ti);
				$('#brand').val(jsonObject.brand);
				$('#style').val(jsonObject.style);
				$('#color').val(jsonObject.color);
				$('#plate_num').val(jsonObject.plate_num);
				//$('#plate_num_last3').val(jsonObject.plate_num_last3);
				$('#vin').val(jsonObject.vin);
				$('#eno').val(jsonObject.eno);
				$('#vehicle_owner').val(jsonObject.vehicle_owner);
				if(jsonObject.is_oper_vehicle =1){
					$('#is_oper_vehicle').val("运营车辆");
					}else if(jsonObject.is_oper_vehicle =0){
					$('#is_oper_vehicle').val("非运营车辆");
					}
				
				//--------车辆相关图片--------------
				$('#car_img').val(jsonObject.car_img);
				$('#driver_license1').attr('src',jsonObject.driver_license1);
				$('#driver_license2').attr('src',jsonObject.driver_license2);
				$('#driving_license1').attr('src',jsonObject.driving_license1);
				$('#driving_license2').attr('src',jsonObject.driving_license2);
				$('#uc_id').val(jsonObject.uc_id);
				$('#group_name').val(jsonObject.group_name);
/* 				if(jsonObject.person_verified=1){
					$('#person_verified').val("待认证");
					}
				if(jsonObject.driver_verified=1){
					$('#driver_verified').val("待认证");
					} */
			}
		});
	}); 
	    function showIdCardImg() {
            $("#dlg").dialog("open").dialog('setTitle', '查看身份认证照片'); ;
            $("#idfm").form("clear");
            <%-- url = "<%=basePath%>newGroup.action"; --%>
            //document.getElementById("hidtype").value="submit";
        }
	    function showdriverImg() {
            $("#diverImg").dialog("open").dialog('setTitle', '查看车主认证照片'); ;
            $("#driverfm").form("clear");
            <%-- url = "<%=basePath%>newGroup.action"; --%>
            //document.getElementById("hidtype").value="submit";
        }
	    
	    
	    function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		}
</script>
</head>

<body onload="loaddone()">
	<div id="tt" class="easyui-tabs" style="width: 800px; height: 450px;"
		fit=true>
		<div title="客户基本信息"
			style="width: 800px; height: 450px; padding: 20px;">

			<table border=0>
				<tr>
					<td width="80px" align=left valign="middle"><font>I&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;D：</font></td>
					<td width="80px"><input type="text" id="id" name="id"
						disable="true"></td>
					<td width="80px" align=right valign="middle">头&nbsp;&nbsp;&nbsp;&nbsp;像：</td>
					<td rowspan=4 align=center valign="middle" width="80px"><img
						src="" id="img"></td>
				</tr>
				<tr align="left">
					<td>用户名称：</td>
					<td align=left><input type="text" id="usercode"
						name="usercode" disable="true"></td>
				</tr>
				<tr align="left">
					<td>昵&nbsp;&nbsp;&nbsp;&nbsp;称：</td>
					<td align=left><input type="text" id="nickname"
						name="nickname"></td>
				</tr>
				<tr align="left">
					<td>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
					<td align=left><input type="text" id="username"
						name="username"></td>
				</tr>
				<tr align="left">
					<td>手机号码2：</td>
					<td valign="middle" align=left><input type="text" id="phone"
						name="phone"></td>
					<td align=right>性&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td align=right width="90px"><input type="text" id="sex"
						name="sex"></td>
				</tr>
				<tr>
					<td>备&nbsp;&nbsp;&nbsp;&nbsp;注：</td>
					<td valign="middle" colspan=3><textarea rows="2" cols="50"
							id="remark" name="remark"></textarea></td>

				</tr>
			</table>
		</div>
		<div title="身份认证信息" data-options="closable:true"
			style="overflow: auto; padding: 20px;">
			<table>
				<tr align="center">
					<td>姓&nbsp;&nbsp;&nbsp;&nbsp;名：</td>
					<td align=right><input type="text" id="person_username"
						name="person_username"></td>
					<td>性&nbsp;&nbsp;&nbsp;&nbsp;别：</td>
					<td align=right><input type="text" id="person_sex"
						name="person_sex"></td>
				</tr>
				<tr align="center">
					<td>民&nbsp;&nbsp;&nbsp;&nbsp;族：</td>
					<td align=right><input type="text" id="nation" name="nation">
					</td>
					<td>出生日期：</td>
					<td align=right><input type="text" id="birthday"
						name="birthday"></td>
				</tr>
				<tr align="center">
					<td>身份证号：</td>
					<td align=right><input type="text" id="id_card_num"
						name="id_card_num"></td>
					<td>地&nbsp;&nbsp;&nbsp;&nbsp;址11：</td>
					<td align=right><input type="text" id="address" name="address">
					</td>
				</tr>

			</table>

			<a id="btn" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="showIdCardImg()">查看身份认证照片</a>

			<div id="dlg" class="easyui-dialog"
				style="width: 600px; height: 380px; padding: 10px 20px; top: 30px"
				closed="true" buttons="#dlg-buttons">

				<form id="idfm" method="post">
					<table>
						<tr align=left>
							<td align=right><font>身份证正面：</font></td>
							<td align=center valign="middle"><img src="" id="id_card1"></td>
						</tr>
						<tr>
							<td align=right><font>身份证反面：</font></td>
							<td align=center valign="middle"><img src="" id="id_card2"></td>
						</tr>
					</table>
				</form>
			</div>


			<div id="dlg-buttons">
				<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveGroup()" iconcls="icon-save">保存</a>  -->
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dlg').dialog('close')"
					iconcls="icon-cancel">关闭</a>
			</div>
		</div>


		<div title="车主认证信息" data-options="iconCls:'icon-reload',closable:true"
			style="padding: 20px;">
			<table>


				<tr align="center">
					<td>驾照号码：</td>
					<td align=right><input type="text" id="drivinglicence_num"
						name="drivinglicence_num"></td>
					<td>驾照获取时间：</td>
					<td align=right><input type="text" id="drlicence_ti"
						name="drlicence_ti"></td>
				</tr>
				<tr align="center">
					<td>车辆品牌：</td>
					<td align=right><input type="text" id="brand" name="brand">
					</td>
					<td>车辆型号：</td>
					<td align=right><input type="text" id="style" name="style">
					</td>
				</tr>
				<tr align="center">
					<td>车辆型号：</td>
					<td align=right><input type="text" id="color" name="color">
					</td>
					<td>车辆号牌：</td>
					<td align=right><input type="text" id="plate_num"
						name="plate_num"></td>
				</tr>
				<tr align="center">
					<td>车&nbsp;架&nbsp;号：</td>
					<td align=right><input type="text" id="vin" name="vin">
					</td>
					<td>发&nbsp;动机&nbsp;号：</td>
					<td align=right><input type="text" id="eno" name="eno">
					</td>
				</tr>
				<tr align="center">
					<td>车&nbsp;主姓&nbsp;名：</td>
					<td align=right><input type="text" id="vehicle_owner"
						name="vehicle_owner"></td>
					<td>使&nbsp;用性&nbsp;质：</td>
					<td align=right><input type="text" id="is_oper_vehicle"
						name="is_oper_vehicle"></td>
				</tr>
			</table>

			<a id="driverbtn" class="easyui-linkbutton"
				data-options="iconCls:'icon-search'" onclick="showdriverImg()">查看车主照片</a>

			<div id="diverImg" class="easyui-dialog"
				style="width: 600px; height: 380px; padding: 10px 20px; top: 30px"
				closed="true" buttons="#diverImg-buttons">

				<form id="diverfm" method="post">
					<table>
						<tr align=right>
							<td align=center valign="middle"><font>驾驶证正面：</font></td>
							<td align=left><img src="" id="driver_license1"></td>
						</tr>
						<tr>
							<td align=center valign="middle"><font>驾驶证反面：</font></td>
							<td align=left><img src="" id="driver_license2"></td>
						</tr>
						<tr align=right>
							<td align=center valign="middle"><font>行驶证正面：</font></td>
							<td align=left><img src="" id="driving_license1"></td>
						</tr>
						<tr>
							<td align=center valign="middle"><font>行驶证反面：</font></td>
							<td align=left><img src="" id="driving_license2"></td>
						</tr>
						<tr align=right>
							<td align=center valign="middle"><font>车辆照片：</font></td>
							<td align=left><img src="" id="car_img"></td>
						</tr>

					</table>
				</form>
			</div>


			<div id="diverImg-buttons">
				<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="saveGroup()" iconcls="icon-save">保存</a>  -->
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#diverImg').dialog('close')"
					iconcls="icon-cancel">关闭</a>
			</div>

		</div>
		<div title="返利信息" data-options="iconCls:'icon-reload',closable:true"
			style="padding: 20px;"></div>
	</div>
</body>
</html>
