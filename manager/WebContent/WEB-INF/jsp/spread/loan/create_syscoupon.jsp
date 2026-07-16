<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%-- <%@ taglib uri="/struts-tags" prefix="s"%> --%>
<html>
<head>
<base href="<%=basePath%>">
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
<script>
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-validatebox').validatebox();
	$('.easyui-numberbox').numberbox();
	
	
	function save() {
		var limit=$('#couponType4').val();
		var validPeriod=$('#validPeriod').val();
		alert(document.getElementById("couponType3").checked);
		if(document.getElementById("couponType3").checked==true){
		if(limit==null && limit==""){
			alert("点数不能为空!!");
			return false;
		}if(parseInt(limit)==0){
			alert("点数不能为 0 !!");
			return false;
		}
		}
			if(validPeriod==null && validPeriod==""){
				alert("期限不能为空!!");
				return false;
			}if(parseInt(validPeriod)==0){
				alert("期限不能为 0 !!");
				return false;
			}
		return true;
	}
	
	function cancel() {
		//var page_path = "${pageContext.request.contextPath}/spread/loan/";
		window.history.go(-1);
	}
	
	function retrieveNewCouponCode() {
		var page_path = "${pageContext.request.contextPath}/spread/loan/";
		$.ajax({
			url: page_path + "retrieveNewCouponCode.do",
            success : function(r){
            	$('#sysCouponCode').val(r.newCouponCode);
            }
		});
	}
	
	
	function enableUseCondition(enable) {
		var disabl;
		if(enable == true) {
			disabl = false;
		}
		else {
			disabl = true;
		}
		$('[name=coupontype]').each(function(index) {
			$(this).prop('disabled', disabl);
			$(this).prop('checked', false);
		});
		if(enable == true) {
			$('[name=coupontype]').eq(0).prop('checked', true);
		}
	}
	function Changed(){
		//alert(document.getElementsByName("couponType"));
		alert(document.getElementById("couponType2"));
		document.getElementById("couponType2").disabled=true;
		document.getElementById("couponType3").disabled=true;
		document.getElementById("couponType4").disabled=true;
		document.getElementById("couponType5").disabled=true;
	}
	function Changed1(){
		//alert(document.getElementsByName("couponType"));
		alert(document.getElementById("couponType2"));
		document.getElementById("couponType2").disabled=false;
		document.getElementById("couponType3").disabled=false;
		document.getElementById("couponType4").disabled=false;
		document.getElementById("couponType5").disabled=false;
	}
	function releaseChannelChanged() {
		$('#appSpreadUnitId').prop("disabled",true);
	}
	retrieveNewCouponCode();
	
</script>
</head>
<body>
	<br />
	<h2 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;&nbsp;&nbsp;新建点券
	</h2>
	<hr>
	<div id="new_syscoupon" style="padding: 5px; margin-left: 50px">
		<form id="infoForm"	action="${pageContext.request.contextPath}/spread/loan/doCreateSyscoupon.do" method="post" onsubmit="return save()">
			<div>
				<table>
					<tr style="height: 40px">
						<td style="text-align: right">点券ID：</td>
						<td width=300>
							<%-- <s:textfield name="newCoupon.sysCouponCode" id="sysCouponCode" theme="simple" cssClass="textbox" disabled="true"></s:textfield> --%>
							<input name="sysCouponCode" id="sysCouponCode" Class="textbox"
							style="width: 150px; height: 22px" value="${sysCouponCode }">
						</td>
						<td>适用业务：</td>
						<td>
							<%-- 	<s:select name="newCoupon.applySource" id="applySource" theme="simple" cssClass="easyui-combobox" cssStyle="width:133px;" list="#{0:'通用',1:'拼车',2:'代驾'}"></s:select> --%>
							<select name="applySource" id="applySource"
							class="easyui-combobox" style="width: 100px;">
								<option value="1">通用</option>
								<option value="2">拼车</option>
								<option value="3">代驾</option>
						</select>
						</td>
					</tr>
					<tr style="height: 40px">
						<td style="text-align: right">发放渠道 :</td>
						<td id="releaseChannel"><input type="radio"
							name="releaseChannel" value="1"
							onchange="releaseChannelChanged()" checked />APP <input
							type="radio" name="releaseChannel" value="2" />合作单位 <%-- <s:radio name="newCoupon.releaseChannel" id="releaseChannel" theme="simple" list="#{1:'APP', 2:'合作单位' }" value="1" onchange="releaseChannelChanged()"></s:radio> --%>
						</td>
						<td>内容类别 :</td>
						<td id="goodsOrCash">
							<%-- <s:radio name="newCoupon.goodsOrCash" id="goodsOrCash" theme="simple" list="#{1:'绿点', 2:'实物' }" value="1" ></s:radio> --%>
							<input type="radio" name="goodsOrCash" value="1" checked onchange="Changed1()" />绿点
							 <input type="radio" name="goodsOrCash" value="2" onchange="Changed()"/>实物
						</td>
					</tr>
					<tr style="height: 40px">
						<td style="text-align: right">合作单位ID：</td>
						<td>
							<%-- <s:textfield name="newCoupon.appSpreadUnitId" id="appSpreadUnitId" theme="simple" cssClass="textbox" onkeypress="return isNumericKey(event);"></s:textfield> --%>
							<input name="appSpreadUnitId" id="appSpreadUnitId"
							Class="textbox" style="width: 150px; height: 22px" />
						</td>
						<td>具体内容：</td>
						<td>
							<%-- <s:textfield name="newCoupon.sumOrGoodsname" id="sumOrGoodsname" theme="simple" cssClass="textbox"></s:textfield> --%>
							<input name="sumOrGoodsname" id="sumOrGoodsname" Class="textbox"
							style="width: 150px; height: 22px" />
						</td>
					</tr>
					<tr style="height: 40px">
						<td style="text-align: right">使用条件 :</td>
						<td colspan="3" id="couponType1">
							<%-- <s:radio name="newCoupon.couponType" id="couponType1" theme="simple" list="#{1:'无&nbsp;&nbsp;&nbsp;&nbsp;', 2:'满' }" value="1" ></s:radio> --%>
							<input id="couponType2" type="radio" name="couponType" value="0" checked />无&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="couponType3" type="radio" name="couponType" value="1" />满 
							<input id="couponType4" name="limit_val"  Class="textbox"
							style="width: 50px; height: 22px" />点使用1张&nbsp;&nbsp;&nbsp;&nbsp;
							<%-- <s:radio name="newCoupon.couponType" id="couponType2" theme="simple" list="#{3:'不与其他点券共享&nbsp;&nbsp;&nbsp;&nbsp;', 4:'每张订单使用1张' }" value="1" ></s:radio> --%>
							<input type="radio" id="couponType5" name="couponType" value="2" />不与其他点券共享&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" id="couponType6" name="couponType" value="3" />每张订单使用1张
						</td>
					</tr>
					<tr style="height: 40px">
						<td style="text-align: right">有效期:</td>
						<td colspan="3" id="validPeriodUnit"><input
							name="validPeriod" id="validPeriod" Class="textbox"
							style="width: 50px; height: 22px" width="10px" /> <%-- <s:radio name="newCoupon.validPeriodUnit" id="validPeriodUnit" theme="simple" list="#{1:'天', 2:'周', 3:'月', 4:'年' }" value="1" ></s:radio> --%>
							<input type="radio" name="validPeriodUnit" value="1" checked />天
							<input type="radio" name="validPeriodUnit" value="2" />周 <input
							type="radio" name="validPeriodUnit" value="3" />月 <input
							type="radio" name="validPeriodUnit" value="4" />年</td>
					</tr>
					<tr style="height: 50px">
						<td colspan="4" style="text-align: center">
							<button type="submit" class="easyui-linkbutton l-btn"
								iconCls="icon-save">
								<font style="font-weight: bold">&nbsp;保存&nbsp;</font>
							</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href='javascript:void(0)'
							onclick="cancel();" class="easyui-linkbutton l-btn"
							iconCls="icon-cancel"><font style="font-weight: bold">&nbsp;取消&nbsp;</font></a>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>
