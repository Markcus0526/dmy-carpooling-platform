<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
<script type="text/javascript">
var page_path="<%=basePath%>spread/loan/";
var choose=0;
var number=0;
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
}


function open1(){
	$('#tb_op').datagrid({
		url: '<%=basePath%>spread/loan/search.do',
		loadMsg : '数据处理中，请稍后....',
		//height: 'auto',
		fitColumns: true,
		pagination : true,
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 50 ],
		remoteSort : false,
		striped : true,
		rownumbers:true,
		singleSelect:true,
		idField : 'id',
		singleSelect:true,
		selectOnCheck : true,
		checkOnSelect : true,
		toolbar:'#content',
		columns:[[{
			field : 'ck',
			checkbox : true
			},{
				field:'operation',title:'操作',width:200, align:'center',
				formatter:function(value, row, index) {
					var str = '';
					if(row.isenabled==1){
					if(row.goods_or_cash==1){
						str += '<a href="spread/loan/manualdeploy_syscoupon_page.do?id='+row.id+'&pointNumber='+row.sum+'&valid_period='+row.valid_period+'&valid_period_unit='+row.valid_period_unit+'" target="_self">发放</a> &nbsp';
						str += '<a href="javascript:stop('+row.id+');">停发</a> &nbsp';
						str += '<a href="spread/loan/couponsendlog_page.do?id='+row.id+'">发放记录</a> &nbsp';
					}else{
						str += '<a href="javascript:stop('+row.id+');">停发</a> &nbsp';
						str += '<a href="spread/loan/couponsendlog_page.do?id='+row.id+'">发放记录</a> &nbsp';
					}
					}else{
						str += '<a href="spread/loan/couponsendlog_page.do?id='+row.id+'">发放记录</a> &nbsp';
					}
					
							return str;
							if(row.id){return row.id;}
					}
			},{
				field:'syscoupon_code',title:'点券编号',width:200, align:'center'
			},{
				field:'release_channel',title:'发放渠道',	width:200,	align:'center',
				formatter : function(value, row, index) {
					var str = '';
						if(row.release_channel==1){
						str += 'App';
						}
						if(row.release_channel==2){
						str += row.app_spread_unit_name;
						}
							return str;
							if(row.id){return row.id;}
					}
			},{
				field:'content',title:'内容',width:150,align:'center',
				formatter : function(value, row, index) {
					var str = '';
						if(row.goods_or_cash==1){
						str += row.sum+'点';
						}
						if(row.goods_or_cash==1){
						str += row.goods;
						}
							return str;
							if(row.id){return row.id;}
					}
			},{
				field:'goods_or_cash',title:'类别',width:150,align:'center',
				formatter : function(value, row, index) {
					var str = '';
						if(value==1){
						str += '绿点';
						}
						if(value==2){
						str += '实物';
						}
							return str;
							if(row.id){return row.id;}
					}
			},{
				field:'coupon_type',title:'使用条件',width:250,align:'center',
				formatter : function(value, row, index) {
					var str = '';
						if(row.coupon_type==0){
						str += '无条件';
						}
						if(row.coupon_type==1){
						str += '订单金额超过'+row.limit_val+'元就可以用一张';
						}
						if(row.coupon_type==2){
						str += '不能与其他点券并用';
						}
						if(row.coupon_type==3){
						str += '每个订单只能用一张';
						}

							return str;
							if(row.id){return row.id;}
					}
			},{
				field:'valid_period',title:'有效期',width:150,align:'center',
				formatter : function(value, row, index) {
					var str = '';
						if(row.valid_period_unit==1){
						str += value+'日';
						}
						if(row.valid_period_unit==2){
						str += value+'周';
						}
						if(row.valid_period_unit==3){
						str += value+'月';
						}
						if(row.valid_period_unit==4){
						str += value+'年';
						}
							return str;
							if(row.id){return row.id;}
					}
			},{
				field:'deploy_cnt',title:'发放次数',width:150,align:'center'
			},{
				field:'use_cnt',title:'使用次数',width:150,align:'center'
			}]],
		onLoadSuccess : function() {
			$('#list table').show();
			parent.$.messager.progress('close');
		},
		onHeaderContextMenu : function(e, field) {
			e.preventDefault();
			if (!cmenu) {
				createColumnMenu();
			}
			cmenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});
	
}
function stop(id) {
	$.messager.defaults = {
			ok : "确  定",
			cancel : "取  消"
		};
	$.messager.confirm('停发', '您确定要停发点券吗？', function(r){
		if (r){
			$.ajax({
				url: page_path + "disableSyscoupon.do?disableSyscoupon.id=" + id,
	            success : function(data){
	            	var  error=data.error;
	            	if(error==0){
	            		open1();
	            	}else{
	            		alert("添 加 失 败 ! 请 重 试..");
	            	}
	            }
			});
		}
	});
}
function open2(){
	refreshAutoSendParam();
        var couponCode=$('#couponCode').val();
        var price=$('#price').val();
        var validPeriod=$('#validPeriod').val();
        var limit =$('#limit').combobox('getValue');
	    str_url = page_path + 'search4autoSend.do';
		
		$('#tb_coupon4autosend').datagrid({
			url: str_url,
			loadMsg : '数据处理中，请稍后....',
			//height: 'auto',
			fitColumns: true,
			queryParams:{
				couponCode:couponCode,
				point:price,
				validPeriod:validPeriod,
				limit:limit
			},
			pagination: true,
			remoteSort : false,
			striped : true,
			pageSize : 5,
			pageList : [ 5, 10, 15, 20, 25 ],
			idField : 'id',
			selectOnCheck : $(this).is(':checked'),
			checkOnSelect : $(this).is(':checked'),
			columns:[[
			          {field:'operation',title:'操作',width:100, align:'center',
						   
							formatter:function(value, row, index) {
								var str = '';
								if(number==0){
									str += '<a href="javascript:selectCoupon('+index+','+row.id+')">选择</a>&nbsp';
								}
								if(number==1){
									str += '<a href="#">选择中</a>&nbsp';
								
								}
								return str;
								}
			          },
			          {field:'couponid',title:'点券ID',width:300, align:'center'},
			          {field:'price',title:'点数',width:200,align:'center'},
			          {field:'validperiod',title:'有效期',width:150,align:'center'},
			          {field:'coupontype',title:'使用条件',width:250,align:'center'},
			]],
			onBeforeLoad : function(date){
				number=0;
			},
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
}

function open3(){
	str_url = page_path + 'search4activity.do';
	var point=$('#activityPrice').val();
	if(point==""){
		price=0;
	}else{
		price=$('#activityPrice').val();
	}
	$('#tb_activity').datagrid({
		url: str_url,
		loadMsg : '数据处理中，请稍后....',
		//height: 'auto'
			fitColumns: true,
			pagination : true,
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 10, 20, 50 ],
			remoteSort : false,
			striped : true,
			rownumbers:true,
			singleSelect:true,
			idField : 'id',
			singleSelect:true,
			selectOnCheck : true,
			checkOnSelect : true,
			toolbar:'#content',
			queryParams:{
				activityCode : $('#activityCode').val(),
				price : price,
			},
		      toolbar : '#content2',
		columns:[[
		          {field:'operation',title:'操作',width:100, align:'center',
		        	  formatter:function(value, row, index) {
		        		  if(row.isenabled==0){
		        			  return "";
		        		  }else{
		        			  return '<a href="javascript:stopActivity('+index+');">停止活动</a>'; 
		        		  }
						
		        	  }
		          },
		          {field:'code',title:'活动代码',width:300, align:'center'},
		          {field:'couponid',title:'点券ID',width:200, align:'center'},
		          {field:'price',title:'点数',width:100,align:'center'},
		          {field:'limit_count',title:'份数',width:100,align:'center'},
		          {field:'deploy_count',title:'已领取',width:100,align:'center'},
		          {field:'coupontype',title:'使用限制',width:250,align:'center'},
		          {field:'validperiod',title:'有效期',width:150,align:'center'},
		          {field:'operator',title:'操作员',width:100,align:'center'},
		          {field:'date',title:'发起日期',width:250,align:'center'},
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

function selectCoupon(index,sid) {
	num =index;
	if(choose!=0){
		//var num=$('#tb_coupon4autosend').datagrid('getRowIndex', sid);
		//alert(num);
		
		if(number==1){
			var couponid=$('#tb_coupon4autosend').datagrid('getRows');
			$.each(couponid ,function(index, couponid1){
				if(couponid1.couponid==$('#registerCouponCode').val())
					;//num=index;
				number=0;
				$('#tb_coupon4autosend').datagrid('refreshRow',index);
			});
		
			
		}
		if(number==0){
			number=1;
			$('#tb_coupon4autosend').datagrid('refreshRow',num);
		}
	}else{
		alert("请 选 择 修 改 选 项!");
	}
	var syscouponCode="";
	var row=$('#tb_coupon4autosend').datagrid('getData','rows').rows;
	$.each(row,function(index,row){
		if(this.id==sid){
			syscouponCode=this.syscouponCode;
		}
	});
	if(choose== 1) {
		$('#registerCouponCode').val(syscouponCode);
	}
	else if( choose== 2) {
		$('#verifyCouponCode').val(syscouponCode);
	}
	else if( choose== 3) {
		$('#orderFinishCouponCode').val(syscouponCode);
	}
	else if( choose== 4) {
		$('#registerMonthCouponCode').val(syscouponCode);
	}
}	
function stopActivity(index) {
	var code;
	var couponid=$('#tb_activity').datagrid('getRows');
	$.each(couponid ,function(index1, couponid1){
		if(index1==index){
			code=couponid1.code;
			alert(code);
		}
	});
	$.messager.defaults = {
			ok : "确  定",
			cancel : "取  消"
		};
	$.messager.confirm('停发', '您确定要停止活动吗？', function(r){
		if (r){
			str_url = page_path + 'stopActivity.do?stopActivityCode=' + code;
			$.ajax ({
				url : str_url,
				success: function(ret) {
					try {
						if (ret.result == "success") {
							$.messager.alert('', 'Success', 'error');
							refreshActivityList();
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
	});
}

$(function(){
	   open1();
	$('#tab').tabs({   
		       border:true,  
		       height:'auto',
		       width:'auto',
		       onSelect:function(title){ 
		         if(title=="点券管理"){
		        	open1();
		           }
		        if(title=="自动发放点券"){
					open2();
	           	 }
		        if(title=="自助添加点券"){
					open3();
	           	 }
			},
	 });
});

function undo() {
	 var couponid=$('#tb_coupon4autosend').datagrid('getRows');
 if(choose==1){
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#registerCouponCode').val())
			num =index;
			number=0;
		});
		$('#tb_coupon4autosend').datagrid('refreshRow',num);
		$('#registerCouponCode').val('');
	 
 } if(choose==2){
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#verifyCouponCode').val())
			num =index;
			number=0;
		});
		$('#tb_coupon4autosend').datagrid('refreshRow',num);
		$('#verifyCouponCode').val('');
 } if(choose==3){
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#orderFinishCouponCode').val())
			num =index;
			number=0;
		});
		$('#tb_coupon4autosend').datagrid('refreshRow',num);
		$('#orderFinishCouponCode').val('');
 } if(choose==4){
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#registerMonthCouponCode').val())
			num =index;
			number=0;
		});
		$('#tb_coupon4autosend').datagrid('refreshRow',num);
		$('#registerMonthCouponCode').val('');
 }
}

function editmsg(value){
	var num=-1 ;
	var couponid=$('#tb_coupon4autosend').datagrid('getRows');
	if(value==1){
	choose=1;	
	$.each(couponid ,function(index, couponid1){
		if(couponid1.couponid==$('#registerCouponCode').val())
		num =index;
		number=0;
		$('#tb_coupon4autosend').datagrid('refreshRow',index);
	});
	
	if(number==1){
		alert("你已选择一条数据");
	}
	if(number==0){
		number=1;
		$('#tb_coupon4autosend').datagrid('refreshRow',num);
	}
	}
	if(value==2){
		choose=2;
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#verifyCouponCode').val())
			num =index;
			number=0;
			$('#tb_coupon4autosend').datagrid('refreshRow',index);
		});
		if(number==1){
			alert("你已选择一条数据");
		}
		if(number==0){
			number=1;
			$('#tb_coupon4autosend').datagrid('refreshRow',num);
		}
		}
	if(value==3){
		choose=3;
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#orderFinishCouponCode').val())
			num =index;
			number=0;
			$('#tb_coupon4autosend').datagrid('refreshRow',index);
		});
		if(number==1){
			alert("你已选择一条数据");
		}
		if(number==0){
			number=1;
			$('#tb_coupon4autosend').datagrid('refreshRow',num);
		}
	}
	if(value==4){
		choose=4;
		$.each(couponid ,function(index, couponid1){
			if(couponid1.couponid==$('#registerMonthCouponCode').val())
			num =index;
			number=0;
			$('#tb_coupon4autosend').datagrid('refreshRow',index);
		});
		if(number==1){
			alert("你已选择一条数据");
		}
		if(number==0){
			number=1;
			$('#tb_coupon4autosend').datagrid('refreshRow',num);
		}
	}
}
function refreshAutoSendParam() {
	city=$("input[name='globalOrCity']:checked").val();
	name= $('#city').val();	
	if(city==1){
		$('#name').val("全局");
	}else{
		$('#name').val(name);
	}
	
	$.ajax({
	url:page_path+'find.do',
	data:'city='+city+'&&name='+name,
	method:'post',
	success:function(data){
		   var name =data.name;
		   if(name==""){
			   document.getElementById("globalOrCity1").checked = true;
		   }else{
				$('#city').val(data.name);
				document.getElementById("globalOrCity2").checked = true; 
		   }
		   $('#registerCouponCode').val(data.registerSyscouponId);
		   $('#verifyCouponCode').val(data.verifiedSyscouponId);
		   $('#numOrderFinished').val(data.numOrderFinished);
		   $('#orderFinishCouponCode').val(data.finishordersSyscouponId);
		   $('#numRegisterMonth').val(data.numRegistermonth);
		   $('#registerMonthCouponCode').val(data.registermonthSyscouponId);
	}
});
}
function refreshSyscouponList() {
	var couponCode = $('#couponCode').val();
	var price = $('#price').val();
	var validPeriod = $('#validPeriod').val();
	var limit =$('#limit').combobox('getValue');
	$('#tb_coupon4autosend').datagrid('load',{
		couponCode : couponCode,
		point : price,
		validPeriod : validPeriod,
		limit:limit,
	});
	
}

function save() {

	var str_url = page_path+'saveParam.do';
	$.ajax ({
		url : str_url,
		data:{
			"globalOrCity":$("input[name='globalOrCity']:checked").val(),
			"city":$('#name').val(),
			"registerSyscouponId":$('#registerCouponCode').val(),
			"verifiedSyscouponId":$('#verifyCouponCode').val(),
			"numOrderFinished":$('#numOrderFinished').val(),
			"finishordersSyscouponId":$('#orderFinishCouponCode').val(),
			"numRegistermonth":$('#numRegisterMonth').val(),
			"registermonthSyscouponId":$('#registerMonthCouponCode').val(),
		},
		method:'post',
		success: function(ret) {
			try {
				if (ret.result == "nocity") {
					$.messager.alert('', 'Such city does not exist', 'error');
				} else if(ret.result == "emptycitybox") {
					$.messager.alert('', 'Input city', 'error');
				} else if(ret.result == "unknown error") {
					
				} else if(ret.result == "success") {
					$.messager.alert('', 'Success', 'error');
					number = 0;
					refreshSyscouponList();
				} else {
					$.messager.alert('', 'error', 'error');
				}
            } finally {}
		},
		error: function() {
			$.messager.alert('', 'Unknwon error', 'error');
		},
		complete: function(){}
	});
}

function refreshActivityList() {
	var activityCode = $('#activityCode').val();
	var price = $('#activityPrice').val();
	$('#tb_activity').datagrid('load',{
		activityCode : activityCode,
		price  : price,
	});
	
}
function newActivity() {
	window.location=page_path + "createActivity.do";
}
function newSyscoupon() {
	window.location=page_path + "create_syscoupon_page.do";
}

function doSearch(value, name) {
	//var conditionInput = $('#ss').searchbox('getValue');
	var conditionSelect = $('#ss').searchbox('getName');
	var bnocondcash = $("input[name='bnocondcash']:checked").val();
	var bcondcash = $("input[name='bcondcash']:checked").val();
	var bgood = $("input[name='bgood']:checked").val();
	$('#tb_op').datagrid('load',{
		//conditionInput:conditionInput,
		conditionSelect:conditionSelect,
		bnocondcash:bnocondcash,
		bcondcash:bcondcash,
		bgood:bgood
	});
}
</script>
</head>
<body onload="loaddone()">

	<div id="tab" class="easyui-tabs" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div title="点券管理">
			<div class="easyui-layout" data-options="fit:true,border:true"
				style="border: red 0px solid;">
				<div data-options="region:'north',title:'',border:true"
					style="height: 35px;">
					<table id="querytable" cellspacing="0" cellpadding="5px"
						style="text-align: left; height: auto; margin: 0px;">
						<tr>
							<td><input id="ss" class="easyui-searchbox"
								data-options="prompt:'输入',menu:'#mm',searcher:doSearch"
								style="width: 500px; height: 25px" /></td>
							<td><font style="font-weight: normal; font-size: 80%">点券类别：</font>
								<input type="checkbox" id="bnocondcash" name="bnocondcash" /><font
								style="font-weight: normal; font-size: 80%">无条件绿点</font> <input
								type="checkbox" id="bcondcash" name="bcondcash" /><font
								style="font-weight: normal; font-size: 80%">有条件绿点</font> <input
								type="checkbox" id="bgood" name="bgood" /><font
								style="font-weight: normal; font-size: 80%">实物</font></td>
						</tr>

					</table>
				</div>
				<div data-options="region:'center',title:''"
					style="border: green 0px solid;">
					<table id="tb_op" style="padding: 1px; boder: black 0px solid;"
						data-options="fit:true,border:true">
					</table>
				</div>
			</div>
		</div>
		<div title="自动发放点券">
			<table>
				<tr>
					<td><input type="radio" name="globalOrCity" id="globalOrCity1"
						value="1" checked>全局设定 <input type="radio"
						name="globalOrCity" id="globalOrCity2" value="2">指定城市 <input
						type="text" name="city" id="city" style="height: 22px"
						class="textbox"></input> <a href="javascript:void(0)"
						onclick="refreshAutoSendParam();" class="easyui-linkbutton l-btn"
						iconCls="icon-search" style="width: 40px"></a></td>
					<td></td>
				</tr>
				<tr>
					<td>所在城市:<input type="text" id="name" name="name"
						style="border: 0px; background-color: transparent; font-size: 20px; margin-left: 10px"
						readonly="readonly" /></td>
					<td id="disable">
						<div>
							点券ID:<input name="couponCode" id="couponCode" Class="textbox"
								style="height: 22px" /> 点数:<input name="price" id="price"
								Class="textbox" style="height: 22px;" /> 有效期:<input
								name="validPeriod" id="validPeriod" Class="textbox"
								style="height: 22px;width:30px" />
								<select id="limit" class="easyui-combobox" name="limit"	style="width: 60px;">
									<option value="0">日</option>
									<option value="1">周</option>
									<option value="2">月</option>
									<option value="3">年</option>
						       </select>
								<a href="javascript:void(0)"	onclick="refreshSyscouponList();"class="easyui-linkbutton l-btn" iconCls="icon-search"	style="width: 40px"></a>
						</div>
					</td>
				</tr>
				<tr>
					<td style="vertical-align: top; width: 400px">
						<table style="margin-left: 40px">
							<tr>
								<td style="text-align: right">注册发放:</td>
								<td><input name="registerCouponCode" Class="textbox"
									style="height: 22px" id="registerCouponCode"
									disabled="disabled" /></td>
								<td><a type="button" onclick="editmsg(1)"
									class="easyui-linkbutton modifybtn" iconCls="icon-edit"
									style="margin-top: 2px; padding-left: 15px; padding-right: 15px; padding-top: 3px; padding-bottom: 3px">修改</a></td>
							</tr>
							<tr>
								<td style="text-align: right">个人认证发放:</td>
								<td><input name="verifyCouponCode" id="verifyCouponCode"
									Class="textbox" style="height: 22px" disabled="disabled" /></td>
								<td><a type="button" onclick="editmsg(2)"
									class="easyui-linkbutton modifybtn" iconCls="icon-edit"
									style="margin-top: 2px; padding-left: 15px; padding-right: 15px; padding-top: 3px; padding-bottom: 3px">修改</a></td>
							</tr>
							<tr>
								<td style="text-align: right">完成<input
									name="numOrderFinished" id="numOrderFinished" Class="textbox"
									style="height: 22px; width: 50px"
									onkeypress="return isNumericKey(event);" />订单:
								</td>
								<td><input name="orderFinishCouponCode"
									id="orderFinishCouponCode" Class="textbox" style="height: 22px"
									disabled="disabled" /></td>
								<td><a type="button" onclick="editmsg(3)"
									class="easyui-linkbutton modifybtn" iconCls="icon-edit"
									style="margin-top: 2px; padding-left: 15px; padding-right: 15px; padding-top: 3px; padding-bottom: 3px">修改</a></td>
							</tr>
							<tr>
								<td style="text-align: right">注册<input
									name="numRegisterMonth" id="numRegisterMonth" Class="textbox"
									style="height: 22px; width: 50px"
									onkeypress="return isNumericKey(event);" />个月:
								</td>
								<td><input name="registerMonthCouponCode"
									id="registerMonthCouponCode" Class="textbox"
									style="height: 22px" disabled="disabled"></td>
								<td><a type="button" onclick="editmsg(4)"
									class="easyui-linkbutton modifybtn" iconCls="icon-edit"
									style="margin-top: 2px; padding-left: 15px; padding-right: 15px; padding-top: 3px; padding-bottom: 3px">修改</a></td>
							</tr>
						</table>
					</td>
					<td id="disable1">

						<div style="width: 600; height: 185">

							<table id="tb_coupon4autosend"
								style="padding: 1px; boder: black 0px solid;"
								data-options="fit:true,border:true">
							</table>
						</div>
					</td>
				</tr>
				<tr>
					<td style="text-align: center"><a href="javascript:void(0)"
						onclick="save()" class="easyui-linkbutton" iconCls="icon-save"
						style="margin-right: 10px">&nbsp;&nbsp;&nbsp;保存&nbsp;&nbsp;&nbsp;</a>
					</td>
					<td><a href="javascript:void(0)" onclick="undo()"
						class="easyui-linkbutton" iconCls="icon-undo"
						style="margin-left: 300px">&nbsp;&nbsp;&nbsp;清除修改中规则&nbsp;&nbsp;&nbsp;</a>
					</td>
					<td style="text-align: right"></td>
				</tr>
			</table>
		</div>
		<div title="自助添加点券">
		<div class="easyui-layout" data-options="fit:true,border:true"
				style="border: red 0px solid;height:900">
			<div data-options="region:'north',title:'',border:true"
					style="height: 45px;">
			活动代码:&nbsp;&nbsp;<input name="activityCode" id="activityCode"
				Class="textbox" style="height: 22px" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			点数:&nbsp;&nbsp;<input name="activityPrice" id="activityPrice"
				Class="textbox" style="height: 22px" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)" onclick="refreshActivityList();"
				class="easyui-linkbutton l-btn" iconCls="icon-search"
				style="width: 40px"></a>

			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
			<table id="tb_activity"	style="padding: 1px; boder: black 0px solid;" 
			        data-options="fit:true,border:true">			
			</table>
		</div>
		</div>
 		<div id="content2" style="padding: 5px; height: 20px">
				<div style="margin-bottom: 5px;">
					<a href="javascript:void(0)" onclick="newActivity()"
						class="easyui-linkbutton" iconCls="icon-add" plain="true"
						style="float: right; margin-right: 10px"><font
						style="font-weight: bold">新建活动&nbsp;&nbsp;&nbsp;</font></a>
				</div>
		</div> 
	</div>
</div>	
	<div id="mm">
		<div data-options="name:'0'">全部查询</div>
		<div data-options="name:'1'">按本平台查询</div>
		<div data-options="name:'2'">按合作单位查询</div>
	</div>
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
			<a href="#" class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"> <font
				style="font-weight: bold">导出Excel&nbsp;</font></a> <a
				href="javascript:void(0)" onclick="newSyscoupon()"
				class="easyui-linkbutton" iconCls="icon-add" plain="true"
				style="float: right; margin-right: 10px"> <font
				style="font-weight: bold">新建点券&nbsp;&nbsp;&nbsp;</font></a>


		</div>
	</div>
	<%-- 	<div class="easyui-panel" data-options="fit:true" 
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<!-- <div class="easyui-layout" data-options="fit:true,border:true" style="border: red 0px solid;"> -->
						<div id="role_tab" class="easyui-tabs" data-options="fit:true,border:true"  >
						<!-- tab 1 -->
							<div title="点券管理" data-options="fit:true,border:true" style="padding:10px">
									<div>
										发行渠道：
										<select id="combo_release_channel" style="width:100px;">
											<option value="0">所有</option>
											<option value="1">App</option>
											<option value="2">合作单位</option>
										</select>
										点券类别：
										<input type="checkbox" id="chk_nocondcash" name="chk_nocondcash" />无条件绿点
										<input type="checkbox" id="chk_condcash" name="chk_condcash" />有条件绿点
										<input type="checkbox" id="chk_good" name="chk_good" />实物
										<a href="javascript:void(0)" onclick="refreshList();" class="easyui-linkbutton l-btn" iconCls="icon-search" style="width:40px"></a>
									</div>
									<div id="content1" style="padding:5px;height:20px">  
										<div style="margin-bottom:5px;">  
       									 	<a href="#" onclick="javascript:report();" class="easyui-linkbutton" iconCls="icon-xls" plain="true" style="float:right;margin-right:5px;"><font style="font-weight:bold">导出Excel&nbsp;</font></a>
   											<a href="javascript:void(0)" onclick="newSyscoupon()" class="easyui-linkbutton" iconCls="icon-add" plain="true" style="float:right;margin-right:10px"><font style="font-weight:bold">新建点券&nbsp;&nbsp;&nbsp;</font></a>
   										</div> 
   									</div>
								<div data-options="region:'center',title:''" style="border: green 0px solid;"></div>
									<table id="tb_op"  style="padding:1px;boder:black 0px solid;height:400px" >
									</table>
								</div>
						
	
						<!-- tab2 -->
						<div title="自动发放点券" data-options="fit:true,border:true" style="padding:10px;">
							<table>
								<tr>
									<td>
										<input type="radio" name="globalOrCity" id="globalOrCity" value="1" checked>全局设定
										<input type="radio" name="globalOrCity" id="globalOrCity" value="2" >指定城市
										<input type="text" name="city" id="city" value="" class="textbox" ></input>
										<a href="javascript:void(0)" onclick="refreshAutoSendParam();" class="easyui-linkbutton l-btn" iconCls="icon-search" style="width:40px"></a>
									</td>
									<td></td>
								</tr>
								<tr>
								<td></td>
								<td>
									点券ID:<input name="couponCode" id="couponCode" />
									点数:<input name="price" id="price" onkeypress="return isNumericKey(event);" />
									有效期:<input name="validPeriod" id="validPeriod" onkeypress="return isNumericKey(event);" />
									<a href="javascript:void(0)" onclick="refreshSyscouponList();" class="easyui-linkbutton l-btn" iconCls="icon-search" style="width:40px"></a>
								</td>
								</tr>
								<tr>
									<td style="vertical-align:top">
										<table>
											<tr>
												<td style="text-align:right">注册发放:</td>
												<td><input name="registerCouponCode" id="registerCouponCode" disabled="true" /></td>
												<td><input type="button" value="修改" kind="1" class="easyui-linkbutton modifybtn" /></td>
											</tr>
											<tr>
												<td style="text-align:right">个人认证发放:</td>
												<td><input name="verifyCouponCode" id="verifyCouponCode" disabled="true" /></td>
												<td><a href="javascript:void(0)" value="修改" kind="2" class="easyui-linkbutton modifybtn" iconCls="icon-edit" plain="true" style="margin-top:2px;padding-left:15px;padding-right:15px;padding-top:3px;padding-bottom:3px">修改</a></td>
											</tr>
											<tr>
												<td style="text-align:right">
													完成<input name="numOrderFinished" id="numOrderFinished"  onkeypress="return isNumericKey(event);" />订单:
												</td>
												<td><input name="orderFinishCouponCode" id="orderFinishCouponCode"  cssClass="textbox" disabled="true" /></td>
												<td><a type="button" value="修改" kind="3" class="easyui-linkbutton modifybtn" iconCls="icon-edit" style="margin-top:2px;padding-left:15px;padding-right:15px;padding-top:3px;padding-bottom:3px">修改</a></td>
											</tr>
											<tr>
												<td style="text-align:right">
													注册<input name="numRegisterMonth" id="numRegisterMonth" onkeypress="return isNumericKey(event);"/>个月:
												</td>
												<td><input name="registerMonthCouponCode" id="registerMonthCouponCode" disabled="true"></td>
												<td><input type="button" value="修改" kind="4" class="easyui-linkbutton modifybtn" /></td>
											</tr>
										</table>
									</td>
									<td>
										<table id="tb_coupon4autosend" style="padding: 1px;boder:black 0px solid;"
											data-options="fit:true,border:true">
										</table>
									</td>
								</tr>
								<tr>
									<td style="text-align:center">
										<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" iconCls="icon-save" style="margin-right:10px">&nbsp;&nbsp;&nbsp;保存&nbsp;&nbsp;&nbsp;</a>
										<a href="javascript:void(0)" onclick="undo()" class="easyui-linkbutton" iconCls="icon-undo" style="margin-right:10px">&nbsp;&nbsp;&nbsp;清除修改中规则&nbsp;&nbsp;&nbsp;</a>
									</td>
									<td style="text-align:right"></td>
								</tr>
							</table>
						</div>
	
						<!-- tab3 -->
						<div title="自助添加点券" data-options="fit:true,border:true" style="padding:10px;">
							活动代码:&nbsp;&nbsp;<input name="activityCode" id="activityCode" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							点数:&nbsp;&nbsp;<input name="activityPrice" id="activityPrice" onkeypress="return isNumericKey(event);">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:void(0)" onclick="refreshActivityList();" class="easyui-linkbutton l-btn" iconCls="icon-search" style="width:40px"></a>
							<div id="content2" style="padding:5px;height:20px">  
								<div style="margin-bottom:5px;"> 
									<a href="javascript:void(0)" onclick="newActivity()" class="easyui-linkbutton" iconCls="icon-add" plain="true" style="float:right;margin-right:10px"><font style="font-weight:bold">新建活动&nbsp;&nbsp;&nbsp;</font></a>
   								</div> 
   							</div>
							<div data-options="region:'center',title:''" style="border: green 0px solid;"></div><br>
								<table id="tb_activity" style="padding: 1px;boder:black 0px solid;"
									data-options="fit:true,border:true">
								</table>
							</div>
						</div>
    </div>  --%>


</body>
</html>
