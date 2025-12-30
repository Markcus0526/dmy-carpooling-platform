<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%response.setContentType("text/html;charset=UTF-8");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
<title>OO车生活后台管理系统</title>
<link href="${pageContext.request.contextPath}/css/login/login.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
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
<body style="width: 100%; height: auto; overflow-y: hidden;" scroll="no">
	<div id="body">

		<div class="login2">
			<div class="login2_title">
				<span
					style="padding-left: 10px; font-size: 48px; color: White; font-family: 华文新魏, 宋体; font-weight: bold;">
				</span>
			</div>

			<div style="height: 60px; vertical-align: middle">

				<c:if test="${!empty msg}">
					<h1>${msg}</h1>
				</c:if>

				<div class="login2_Icon"></div>
				<div style="padding: 12px;">
					<span
						style="padding-left: 30px; font-size: 30px; color: White; font-family: 华文新魏, 宋体; color: #2b527b; font-weight: bold;">
						OO车生活后台管理系统</span>
				</div>
			</div>
			<div style="height: 240px;">
				<div class="login2_logo"></div>
				<div class="login2_from">
					<form action="<%=request.getContextPath()%>/login.do"
						id="loginForm" method="post">
						<table width="250" border="0" style="height: 130px;"
							cellpadding="0" cellspacing="0">
							<tr>
								<td width="87" align="right" style="padding-right: 10px">
									用户名称：</td>
								<td><input type="text" id="userName"  
									name="userName" style="width: 130px;" class="easyui-validatebox" required="true" missingMessage="请输入用户名！"/></td>
							</tr>
							<tr>
								<td width="87" align="right" style="padding-right: 10px">
									用户密码：</td>
								<td><input type="password" id="#passWord"
									name="password" style="width: 130px;" class="easyui-validatebox" required="true" missingMessage="请输入密码！"/></td>
							</tr>
							<!-- 
						<tr>
							<td width="87" align="right" style="padding-right: 10px">
								验证码：
							</td>
							<td>
								<input id="txtVerifyCode" tabindex="3" class="easyui-validatebox" name="VerifyCode"
									style="width: 65px;" type="text" autocomplete="off"
<%--									 onfocus="javascript:document.getElementById('ic1').src='../Service/VerifyCode.ashx?Key=AdminVerifyCode&id='+Math.random();return false;"--%>
									 />
								<img style="margin-top: 1px; vertical-align: top; cursor: pointer;" src="../Service/VerifyCode.ashx?Key=AdminVerifyCode"
									height="22px" id="ic1" alt="看不清，刷新验证码？" onclick="javascript:document.getElementById('ic1').src='../Service/VerifyCode.ashx?Key=AdminVerifyCode&id='+Math.random();return false;" />
							</td>
						</tr>
						 -->
							<tr>
								<td width="87" align="right" style="padding-right: 10px"></td>
								<td style="display: block; margin-left: -4px"><input
									type="checkbox" id="usbkey" /><label for="usbkey"
									title="要求使用USB-KEY"
									style="display: block; width: 50px; margin-left: 20px; margin-top: -16px;">USBKEY</label>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div class="login2_foot">
										<div style="text-align: center">
											<input id="btnLogin" class="login2_btn" type="submit"
												value="登录" />&nbsp;&nbsp;<input id="btnReg"
												class="login2_btn" type="reset" value="重置" />
											<!--<input id="btnReset" class="login2_btn" type="reset" value="重置" />-->
										</div>
									</div>
								</td>
							</tr>
						</table>
						<input type="hidden" id="Digest" name="Digest" /> <input
							type="hidden" id="SN_SERAL" name="SN_SERAL" />
					</form>
				</div>
			</div>
			<div class="login2_copyright">Copyright @版权所有:北京昊唐三六五科技有限公司
				@2013</div>
		</div>
	</div>
	<div id="dusb"></div>
</body>
</html>
<script>
//自定义验证规则 名称为name
//$.fn.validatebox.defaults.rules.name.message 动态自定义提示内容
// $.extend($.fn.validatebox.defaults.rules, {
//     minLength: {
//         validator: function(value){
//         	var re = /.+/gi;
//         	if(re.test(value)){
//         		$.fn.validatebox.defaults.rules.message = "用户名不能为空！";
//         	}else{
//         		return true;
//         	}
//         },
//         message: '请输入用户名'
//     }
// });

</script>