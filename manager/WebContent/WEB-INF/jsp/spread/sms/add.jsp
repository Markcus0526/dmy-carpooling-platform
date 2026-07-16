<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<style>
.myFont {
	font-size: 14px;
}

.leftTd {
	text-align: right;
	width: 100px;
}

.rightTd {
	margin-left: 20px;
}
</style>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/datagrid.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/icon.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/outlook2.js'> </script>
<style>
.textbox {
	height: 20px;
	margin: 0;
	padding: 0 2px;
	box-sizing: content-box;
}

* {
	font-size: 12px;
}
</style>

<div id="content">
	<s:form id="addPlanForm" theme="simple">
		<s:hidden name="smsPrice" id="smsPrice" />
		<s:hidden name="smsMaxLen" id="smsMaxLen" />
		<s:hidden name="planPrice" id="planPrice" />
		<s:hidden name="clientNum" id="clientNum" />
		<s:hidden name="idList" id="idList" />

		<table style="padding: 30px">
			<tr style="height: 50px">
				<td class="leftTd"><label class="myFont">目标客户数:</label></td>
				<td><label id="selUserCnt" class="myFont rightTd"
					style="font-size: 25px; color: red"></label> <a href="#"
					onclick="btnSelUser();"
					style="width: 100px; float: center; margin-left: 400px"
					class="easyui-linkbutton l-btn">选取客户</a></td>
			</tr>

			<tr style="height: 50px">
				<td class="leftTd"><label class="myFont">号码列表:</label></td>
				<td><s:textarea id="phoneList" cssClass="rightTd"
						cssStyle="width:500px; height:60px;"></s:textarea></td>
			</tr>

			<tr>
				<td></td>
				<td class="rightTd">&nbsp;&nbsp;&nbsp;&nbsp;说明：允许分隔符“中英文逗号、回车符”</td>
			</tr>

			<tr style="height: 50px">
				<td class="leftTd"><label class="myFont">内容:</label></td>
				<td><textarea id="smsContent" name="msg" class="rightTd"
						style="width: 500px; height: 60px;" onChange="showMsgPrice();"></textarea>
				</td>
			</tr>

			<tr>
				<td></td>
				<td class="rightTd">&nbsp;&nbsp;&nbsp;&nbsp;注：每条短信限70字符，超过70字符时按2条计费，超过140字符时按3条计费，以此类推！</td>
			</tr>

			<tr style="height: 50px">
				<td class="leftTd"><label class="myFont">预计费用:</label></td>
				<td><label id="price4sms" class="myFont rightTd"
					style="font-size: 25px; color: red"></label></td>
			</tr>
			<tr>
				<td class="leftTd"><label class="myFont">发送模式:</label></td>
				<td>

					<div class="easyui-tabs rightTd" data-options="tabWidth:112"
						style="width: 500px; height: 150px">

						<div title="单次发送" style="padding: 30px 50px 10px 50px">
							<label class="myFont">设定时间:</label>
							<s:textfield name="sendTime" cssClass="easyui-datetimebox"
								style="width:145px" data-options="formatter:myformatter"></s:textfield>
							<br />
							<br /> <span>说明：输入时间则定时发送，不输入时间则立即发送</span>
						</div>

						<div title="定期发送" style="padding: 10px 50px 10px 50px">

							<div style="padding: 10px 0px 0px 0px">
								<label class="myFont">发送次数:</label>
								<s:textfield name="limitTimes" id="limitTimes"
									cssClass="textbox" style="width:145px" theme="simple"></s:textfield>
								<span>&nbsp;&nbsp;说明：不输入表示持续发送</span>
							</div>

							<div style="padding: 10px 0px 0px 0px">
								<label class="myFont">发送时间:</label> <select
									name="regularSendMode" id="regularSendMode"
									class="easyui-combobox" style="width: 80px; height: auto"
									data-options="onSelect:onChangeMode">
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
								</select> <label id="forTime1" style="width: 40px">点</label> <select
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

			<tr style="height: 50px">
				<td class="leftTd"><label class="myFont">备注说明:</label></td>
				<td><textarea name="desc" class="rightTd"
						style="width: 500px; height: 60px;"></textarea></td>
			</tr>
		</table>
	</s:form>

	<div style="margin-left: 200px; margin-top: 20px">
		<a href="javascript:void(0);" onclick="javascript:btnSend();"
			style="margin-left: 50px; width: 120px;" icon="icon-ok"
			class="easyui-linkbutton">创建计划/发送</a> <a href="javascript:void(0);"
			onclick="javascript:btnCancel();"
			style="margin-left: 50px; width: 80px;" icon="icon-cancel"
			class="easyui-linkbutton">取&nbsp;消</a>
	</div>

	<div id="selectDlg"></div>
</div>

<script>
	var page_path = '${pageContext.request.contextPath}/spread/sms/';
	var userPhoneList = [];
	var selectDlg;
	
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-tabs').tabs();
	$('.easyui-datetimebox').datetimebox();
	$('.easyui-combobox').combobox();
	$('#selectDlg').dialog('close');
	
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
		var smsMaxLen = $('#smsMaxLen').val();
		var smsPrice = $('#smsPrice').val();
		var selCount = $('#selUserCnt').text();
		
		var smsLen = $('#smsContent').val().length;
		if (smsLen == 0) {
			$('#price4sms').text(0);
			return;
		}
		
		var smsCnt = parseInt((smsLen - 1) / parseInt(smsMaxLen)) + 1;
		var planPrice = smsCnt * smsPrice * selCount;
		
		$('#price4sms').text(parseInt(planPrice * 100)/100);
	}
	
	function btnSelUser() {
		selectDlg = $('#selectDlg');
//		selectDlg.html('');
		selectDlg.load(page_path + "selectPage.do", function(){
			$(this).fadeIn();
		});
	}
	
	function validateAddForm() {
		if ($('#phoneList').val() == '') {
			parent.$.messager.alert('创建短信推广计划', '选取个人客户', 'error');
			return false;
		}

		if ($('#smsContent').val() == '') {
			parent.$.messager.alert('创建短信推广计划', '输入短信内容', 'error');
			return false;
		}
			
		return true;
	}
	
	function btnSend() {
		var valid = validateAddForm();
		if (valid == false) {
			return;
		}

		$('#planPrice').val($('#price4sms').text());
		$('#addPlanForm').ajaxSubmit({
			url: page_path + "add.do",
			success: function(result){
				if (result['err_code'] == 0)
					$('#content').load(page_path + "index.do");
				else
					parent.$.messager.alert('创建短信推广计划', result['err_msg'], 'error');
			},
			error:function(){
				parent.$.messager.alert('创建短信推广计划', result['err_msg'], 'error');
			},
			complete:function(){

			}
		});
	}
	
	function btnCancel() {
		$('#content').load(page_path + "index.do");
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
		
</script>