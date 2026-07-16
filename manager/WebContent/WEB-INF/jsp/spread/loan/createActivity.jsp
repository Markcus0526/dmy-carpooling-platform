<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
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
	var page_path = '${pageContext.request.contextPath}/spread/loan/';
	var selSyscouponId = 0;
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
$(function(){
			   var couponCode=$('#couponCode').val();
			   var price=$('#price').val();
			   var validPeriod=$('#validPeriod').val();
				var limit = $('#limit').combobox('getValue');
			   str_url = page_path + 'search4autoSend.do';
			   $('#tb_coupon').datagrid({
					url: str_url,
					loadMsg : '数据处理中，请稍后....',
					queryParams:{
						couponCode:couponCode,
						point:price,
						validPeriod:validPeriod,
						limit:limit
				},
				height: 'auto',
				fitColumns: true,
				pagination: true,
				remoteSort : false,
				striped : true,
				pageSize : 10,
				pageList : [ 10, 20, 30, 40, 50 ],
				idField : 'id',
				singleSelect:true,
				/*singleSelect:true,
				selectOnCheck : $(this).is(':checked'),
				 checkOnSelect : $(this).is(':checked'), */
				toolbar:'#content',
				columns:[[
				          /* {field:'ck', checkbox:true }, */
				          {field:'operation',title:'操作',width:100, align:'center',
				        		formatter:function(value, row, index) {
									var str = '';
										str += '<a href="javascript:selectCoupon('+row.id+')">新建自助添加活动</a>&nbsp';
										return str;
									}  
				          },
				          {field:'couponid',title:'点券ID',width:300, align:'center'},
				          {field:'price',title:'点数',width:200,align:'center'},
				          {field:'validperiod',title:'有效期',width:150,align:'center'},
				          {field:'coupontype',title:'使用条件',width:250,align:'center'},
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
	});
	function genActivityCode() {
		str_url = page_path + "genActivityCode.do";
		$.ajax ({
			url : str_url,
			success: function(ret) {
				try {
					if (ret.result == "success") {
						$('#activityCode').val(ret.code);
					} else {
						$.messager.alert('', 'Fail', 'error');
					}
	            } finally {}
			},
			error: function() {
				$.messager.alert('', 'Unkwon error', 'error');
			},
			complete: function(){}
		});
	}
	
	function create() {
		if(selSyscouponId == 0) {
			$.messager.alert('', '请选择点券');
			return;
		}
		str_url = page_path + 'doCreateActivity.do?activeCode=' + $('#activityCode').val();
		str_url = str_url + '&limitCount=' + $('#amount').val();
		str_url = str_url + '&syscouponId=' + selSyscouponId;
		$.ajax ({
			url : str_url,
			success: function(ret) {
				try {
					if (ret.result == "success") {
						$.messager.alert('', 'Success', 'error');
						$('#div1').load(page_path + "index");
					} else {
						$.messager.alert('', 'Fail', 'error');
					}
	            } finally {}
			},
			error: function() {
				$.messager.alert('', 'Unkwon error', 'error');
			},
			complete: function(){}
		});
	}
		
	function cancel() {
		$('#div1').load(page_path+"index.do");
	}
	
	function onClickRow() {
		
	}
	
	function selectCoupon(id) {
		selSyscouponId = id;
		$('#tb').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'',
			    width:350,   
			    height:400,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:20,
			    content:'<div><div style="width:300px;margin-left:30px"><h2>是否确定建立如下活动：</h2></div>'
			   +' <div style="width:300px;margin-left:40px">'
			   +' 活动代码:<input type="text" id ="activityCode1" name="activityCode"  style="border:0px;background-color:transparent;margin-left:10px" readonly="readonly"/><br>'
			   +' 点券ID：<input type="text" id ="couponCode1" name="couponCode"  style="border:0px;background-color:transparent;margin-left:10px" readonly="readonly"/><br>'
			   +' 点数：<input type="text" id ="point1" name="point"  style="border:0px;background-color:transparent;margin-left:10px" readonly="readonly"/><br>'
			   +' 领取数量<input type="text" id ="amount1" name="amount"  style="border:0px;background-color:transparent;margin-left:10px" readonly="readonly"/><br>'
			   +' 有效期：<input type="text" id ="validPeriod1" name="validPeriod"  style="border:0px;background-color:transparent;margin-left:10px" readonly="readonly"/><br>'
			   +' 使用条件<input type="text" id ="coupontype1" name="coupontype"  style="border:0px;background-color:transparent;margin-left:10px" readonly="readonly"/><br>'
			   +'</div>'
			   +'<a href="javascript:void(0)" onclick="create();" class="easyui-linkbutton l-btn" iconCls="icon-sure" style="margin-left:20px"><font style="font-weight:bold">确定&nbsp;</font></a>'
			   +'<a href="javascript:void(0)" onclick="closeDio();" class="easyui-linkbutton l-btn" iconCls="icon-close" style="margin-left:20px"><font style="font-weight:bold">取消&nbsp;</font></a>'
			   +'</div>',
			 onOpen:function(){
				    $('#id').val(id);
			 		$.ajax({
						url:page_path+"couponsendlog_page1.do",
						data:'id='+id,
						method:'post',
						success:function(data){
						var activityCode1=$('#activityCode').val();
						var amount=$('#amount').val();
						$('#couponCode1').val(id);
						$('#point1').val(data.sumOrGoodsname);
						$('#amount1').val(amount);
						$('#activityCode1').val(activityCode1);
						$('#validPeriod1').val(data.validPeriodWithUnit);
						$('#coupontype1').val(data.useCondition);
		            }
					}); 
			 }
			});
	}
	function create(){
		var limitCount=$('#amount1').val();
		if(limitCount==""){
			limitCount=0;
		}
		str_url = page_path + 'doCreateActivity.do';
		$.ajax ({
			url : str_url,
			data:{
				"syscouponId":$('#couponCode1').val(),
				"limitCount":limitCount,
				"activeCode":$('#activityCode1').val(),
			
			},
			success: function(ret) {
				try {
					if (ret.result == "success") {
						$("#tb").dialog('close');
						$.messager.alert('', '保存成功', 'error');
						 window.location=page_path+"index.do";
					} else {
						$.messager.alert('', 'Fail', 'error');
					}
	            } finally {}
			},
			error: function() {
				$.messager.alert('', 'Unkwon error', 'error');
			},
			complete: function(){}
		});
	}
	function closeDio(){
		$('#tb').dialog('close');
	}
	function refreshCouponList() {
		var couponCode = $('#couponCode').val();
		var point = $('#point').val();
		var validPeriod = $('#validPeriod').val();
		$('#tb_coupon').datagrid('load',{
			couponCode:couponCode,
			point:point,
			validPeriod:validPeriod
		});
	}
	
	function isNumericKey(e) {
	    var charInp = window.event.keyCode;
	    if (charInp == 190 || charInp == 110 || charInp == 46) {    // "."
	        return true;
	    }
	    if (charInp > 31 && ((charInp < 48) || (charInp > 57))) {
	        return false;
	    }
	    return true;
	}
	
	refreshCouponList();
	
	/* $(document).ready(function() {
		
		refreshCouponList();

	}); */
	
	
</script>
</head>
<body onload="loaddone()">
	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 195px;">
				<h2 class="page-title txt-color-blueDark">
					<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;新建自助添加点券活动
				</h2>
				<hr>
				<div style="padding: 5px; padding-right: 50px; padding-left: 50px">
					<div style="padding-bottom: 7px">
						活动代码:&nbsp;&nbsp; <input name="activityCode" id="activityCode"
							disabled="disabled" value="${activityCode}" Class="textbox"
							style="width: 200px; height: 22px"> <a
							href='javascript:void(0)' onclick="genActivityCode();"
							class="easyui-linkbutton l-btn" iconCls="icon-reload"> <font
							style="font-weight: bold">换一个</font></a>
					</div>
					<div style="padding-bottom: 7px">
						有效数量:&nbsp;&nbsp; <input name="amount" id="amount" Class="textbox"
							style="width: 200px; height: 22px">
					</div>
				</div>
				<hr>
				<div>
					&nbsp;&nbsp;&nbsp;点券ID:&nbsp;<input name="couponCode"
						id="couponCode" Class="textbox" style="width: 100px; height: 22px" />&nbsp;&nbsp;
					点数:&nbsp;<input name="point" id="point" Class="textbox"
						style="width: 100px; height: 22px" /> 有效期:&nbsp;<input
						name="validPeriod" id="validPeriod" Class="textbox"
						style="height: 22px;width:30px"/> 
							<select id="limit" class="easyui-combobox" name="limit"	style="width:60px;">
									<option value="0">日</option>
									<option value="1">周</option>
									<option value="2">月</option>
									<option value="3">年</option>
						       </select>
						<a
						href="javascript:void(0)" onclick="refreshCouponList();"
						iconCls="icon-search" class="easyui-linkbutton l-btn"
						style="width: 40px;"></a>
				</div>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="tb_coupon" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
				<table id="tb"></table>
			</div>
		</div>

		<form id="hiddenForm" method="post">
			<input id="searchValue" type="hidden" /> <input id="searchType"
				type="hidden" />
		</form>
		<div id="content" style="padding: 5px; height: 20px">
			<div style="margin-bottom: 5px;">
				<a href='javascript:void(0)' onclick="create();"
					class="easyui-linkbutton l-btn" iconCls="icon-add" plain="true"
					style="float: right; margin-right: 5px;"><font
					style="font-weight: bold"> &nbsp;创建&nbsp;</font></a>
			</div>
		</div>
	</div>
</body>
</html>
