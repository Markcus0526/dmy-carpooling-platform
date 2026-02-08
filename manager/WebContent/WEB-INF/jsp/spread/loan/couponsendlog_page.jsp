<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="/struts-tags" prefix="s"%> --%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript" charset="UTF-8">
	var tb_index = -1;
	$('.easyui-tree').tree();
	$('.easyui-tabs').tabs();
	$('.easyui-datebox').datebox();			
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-combobox').combobox();
	$('.easyui-searchbox').searchbox();
	$('.easyui-validatebox').validatebox();
	//dataLoader.load();

	//couponsendlog_page(syscoupon);
	function couponsendlog_page(syscoupon) {
		$('#sysCouponCode').val(syscoupon.sysCouponCode);
		$('#sumOrGoodsname').val(syscoupon.sumOrGoodsname);
		$('#releaseChannelName').val(syscoupon.releaseChannelName);
		$('#useCondition').val(syscoupon.useCondition);
		$('#validPeriodWithUnit').val(syscoupon.validPeriodWithUnit);
	};
	
	var dataLoader = {};
	var page_path = '${pageContext.request.contextPath}/spread/loan/';
	//$(function(){
		dataLoader.load=function(syscouponCode){
			str_url = page_path + 'searchSendLog.do?syscoupon_code=' + syscouponCode;

			$('#tb_op').datagrid({
				url: str_url,
				loadMsg : '数据处理中，请稍后....',
				height: 'auto',
				fitColumns: true,
				pagination: true,
				pageList : [ 10, 20, 30, 40, 50 ],
				singleSelect:true,
				columns:[[
				          {field:'operator',title:'操作人',width:80, align:'center'},
				          {field:'sendtype',title:'发放方式',width:130, align:'center',
				  			formatter:function(value, row, index) {
									if(value==1){
										return "注册自动发放";
									}
									if(value==1){
										return "订单完成后自动发放";
									}
									if(value==1){
										return "个人认证后自动发放";
									}
									if(value==1){
										return "注册后长期自动发放";
									}
									if(value==1){
										return "手动发放";
									}
									if(value==1){
										return "unknown";
									}
								}
				          },
				          {field:'num',title:'数量',width:100,align:'center'},
				          {field:'sendtime',title:'发放时间',width:250,align:'center'},
				          {field:'msg',title:'备注',width:300,align:'center'},
				]],
				onHeaderContextMenu: function(e, field){
					e.preventDefault();
					if (!cmenu){
						createColumnMenu();
					}
					cmenu.menu('show', {
						left:e.pageX,
						top:e.pageY
					});
				}
			});
  	};
		
//	});
	
	function refreshList() {
		var syscouponCode = $('#sysCouponCode').val();
		
		window["dataLoader"].load(syscouponCode);
	}
	
	function btnClose() {
		/* window.location=page_path + "index.do"; */
		window.history.go(-1);
	}
	
	$(document).ready(function() {
		
		refreshList();
	});
	
	dataLoader.load();
	refreshList();
	function loaddone(){
		var hiddenmsg = parent.document.getElementById("hiddenmsg"); 
		hiddenmsg.style.display="none";
	}
	</script>
</head>
<body onload="loaddone()">
	<br />
	<h2 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;点券发放记录
	</h2>
	<hr>
	<div style="padding: 5px; margin-left: 50px">
		<div>
			<table>
				<tr style="height: 40px">
					<td style="text-align: right">点券CODE：</td>
					<td>
						<%-- <s:hidden name="syscoupon.id" id="id" theme="simple" ></s:hidden> --%>
						<input name="id" id="id" type="hidden" /> <input
						name="sysCouponCode" id="sysCouponCode"
						class="easyui-validatebox textbox" disabled="disabled"
						value="${sysCouponCode}" />
					</td>
					<td style="text-align: right">内容：</td>
					<td><input name="sumOrGoodsname" id="sumOrGoodsname"
						disabled="disabled" class="easyui-validatebox textbox"
						value="${sumOrGoodsname}" /></td>
				</tr>
				<tr style="height: 40px">
					<td style="text-align: right">发放渠道：</td>
					<td><input name="releaseChannelName" id="releaseChannelName"
						class="easyui-validatebox textbox" disabled="disabled"
						value="${releaseChannelName}" /></td>
					<td style="text-align: right">使用条件：</td>
					<td><input name="useCondition" id="useCondition"
						disabled="disabled" class="easyui-validatebox textbox"
						value="${useCondition}" /></td>
				</tr>
				<tr style="height: 40px">
					<td style="text-align: right">有效期：</td>
					<td colspan="3"><input name="validPeriodWithUnit"
						id="validPeriodWithUnit" disabled="disabled"
						class="easyui-validatebox textbox" value="${validPeriodWithUnit}" />
					</td>
				</tr>
			</table>
		</div>
		<div style="clear: both;"></div>
		<br>
		<table id="tb_op">
		</table>
		<br> <a href='javascript:void(0)' onclick="btnClose();"
			class="easyui-linkbutton" iconCls="icon-cancel" style="width: 90px">&nbsp;关闭&nbsp;</a>

	</div>
</body>
</html>