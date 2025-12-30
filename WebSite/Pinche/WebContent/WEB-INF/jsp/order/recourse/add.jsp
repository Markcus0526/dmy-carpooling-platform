<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<html>
<style>
.left_labelstyle {
	width: 90px;
	text-align: right;
}

.right_labelstyle {
	width: 100px;
	text-align: right;
}
</style>
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
<script>
var page_path = '<%=basePath%>order/recourse/';
	$('.easyui-panel').panel();
 	$('.easyui-combobox').combobox();
	$('.easyui-validatebox').validatebox();
	$('.easyui-tabs').tabs();
	
	edit_add = 1;
	var phone_Reg=/^[1-9]+$/i;
	
	$.extend($.fn.validatebox.defaults.rules, {
		CheckPhone: {
		validator: function(value){
			return phone_Reg.test(value);
		},
		message: '请输入电号码'
		}
	});
	
	function searchInterface()
	{	
		$("#dd").dialog({
		   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
		    title:'查询订单',
		    width:1070,   
		    height:700,   
		    modal:true , 
		    closable:false,
		    draggable:true,
		    border:true,
		    top:'50px',
		 	content:'<div style="border: gray 1px solid;"><form id="searchForm">'
				+'<table style="width:auto;"><tr style="height:30px">'
				+'<td>客户电话：</td><td><input name="dduserphone" id="dduserphone" Class="textbox" Style="width:200px;" ></td>'
				+'<td>客户ID:</td><td><input  name="ddusercode" id="ddusercode" Class="textbox" Style="width:200px;" ></td>'
				+'<td>客户姓名：</td><td><input name="ddcustomer_name" id="ddcustomer_name" Class="textbox" Style="width:200px;" ></td></tr>'
				+'<tr style="height:30px"><td>成交时间：</td><td><input id="ddstart_time" name="ddstart_time" Class="easyui-datetimebox" data-options="formatter:myformatter" style="width:95px">~ '
				+'<input id="ddend_time" name="ddend_time" Class="easyui-datetimebox" data-options="formatter:myformatter" style="width:95px" ></td>'
				+'<td>客户身份：</td>'
				+'<td><input type="checkbox" checked="checked" name="ddidentity1" value="1">乘客'
				+'<input type="checkbox" checked="checked" name="ddidentity2" value="2" style="margin-left: 20px;">车主	</td></tr>'
				+'</table><table><tr style="height:30px"><td>订单类型：</td>'
				+'<td><input type="checkbox" checked="checked" name="ddorder_type1" value="1" >单次拼车'
				+'<input type="checkbox" checked="checked" name="ddorder_type2" value="2" style="margin-left: 20px;">上下班拼车'
				+'<input type="checkbox" checked="checked" name="ddorder_type3" value="3" style="margin-left: 20px;">长途拼车</td><td>'
				+'<a href="javascript:void(0)" onclick="btnSearch();" class="easyui-linkbutton l-btn" iconCls="icon-search">&nbsp;查询&nbsp;</a>	</td></tr><tr style="height:30px"><td>订单状态：</td>'
				+'<td><input type="checkbox" checked="checked" name="ddorder_state1" value="1">已完成  <input type="checkbox" checked="checked" name="ddorder_state2" value="2" style="margin-left: 31px;">未完成'
				+'</td></tr></table>订单编号：<input  id="ddorder_num" name="ddorder_num" Class="textbox" Style="width:200px;" /></div></form>'
				+'<div id="list" style="padding: 10px">'
				+'<table id="dg" style="width: auto; height: auto" data-options="singleSelect:true, pagination:true">	</table></div>'
			    +'<div style="height:30px;width:800px"><a href="javascript:void(0)" onclick="closedialog()"style="margin-left:300px;width:100px;" class="easyui-linkbutton">关闭</a></div></div>',
			onOpen : function(){
			    sendAjax();
			load1();
		     }
	});
	}
    function sendAjax(){
    	var userphone=$("#phone_incoming").val();
		$.ajax({
		 url:page_path + 'get_id_phone.do',
		 data:{"userphone":userphone},
		 method:'post',
		success:function(ret){
			alert(ret.usercode);
			alert(ret.phone);
			alert(ret.username);
			$('#ddusercode').val(ret.usercode);
			$('#dduserphone').val(ret.phone);
			$('#ddcustomer_name').val(ret.username);
			load1();
		 }
		});
    }
	function btnSearch() {
			usercode=$('#ddusercode').val();
			userphone=$('#dduserphone').val();
		$('#dg').datagrid('load',{
			usercode:usercode,
	       	userphone:userphone,
	       	customer_name:$('#ddcustomer_name').val(),
	       	order_num:$('#ddorder_num').val(),
	        identity1:$("input[name='ddidentity1']:checked").val(),
	       	identity2:$("input[name='ddidentity2']:checked").val(),
	       	order_type1:$("input[name='ddorder_type1']:checked").val(),
	       	order_type2:$("input[name='ddorder_type2']:checked").val(),
	       	order_type3:$("input[name='ddorder_type3']:checked").val(),
	       	order_state1:$("input[name='ddorder_state1']:checked").val(),
	       	order_state2:$("input[name='ddorder_state2']:checked").val(),
	       	start_time: $('#ddstart_time').datetimebox('getValue'), 
	       	end_time: $('#ddend_time').datetimebox('getValue') 
		});
	}
	 function load1(){
			usercode=$('#ddusercode').val();
			userphone=$('#dduserphone').val();
		$('#dg').datagrid({
	        url: page_path + 'searchOrder.do',
	        queryParams:{
	        	usercode :usercode,
		    userphone:userphone,
	        customer_name:$('#ddcustomer_name').val(),
	       	order_num:$('#ddorder_num').val(),
	        identity1:$("input[name='ddidentity1']:checked").val(),
	       	identity2:$("input[name='ddidentity2']:checked").val(),
	       	order_type1:$("input[name='ddorder_type1']:checked").val(),
	       	order_type2:$("input[name='ddorder_type2']:checked").val(),
	       	order_type3:$("input[name='ddorder_type3']:checked").val(),
	       	order_state1:$("input[name='ddorder_state1']:checked").val(),
	       	order_state2:$("input[name='ddorder_state2']:checked").val(),
	       	start_time: $('#ddstart_time').datetimebox('getValue'), 
	       	end_time: $('#ddend_time').datetimebox('getValue') 
				},
	        loadMsg : '数据处理中，请稍后....',
	        pagination: true,
	        fitColumns: true,	
	        singleSelect:true,
	        height:'440px',
			columns:[[
			            {field:'operMenu',width:100,align:'center',title:'操作',
			             	  formatter:function(value,row,index){  
			  		               var btn="";
			  		              btn+= '<a href="javascript:void(0)" onclick="viewInDetail('+row.id+')">查看</a>|';
			  		     		  btn+='<a href="javascript:void(0)" onclick="select('+row.id+')">选择</a>';
			  		               return btn;  
			  	        	  }
			              }, 
			              {field:'id', width:100,align:'center',title:'订单编号'},
			              {field:'order_city', width:90,align:'center',title:'所在城市'},
			              {field:'order_type', width:100,align:'center',title:'订单类型',
			             	 formatter:function(value,row,index){ 
			             		  if(value==1){
			  			        	  return '单次拼车';
			  			          }
			  			           if(value==2){
			  			        	  return '上下班拼车';
			  			          }
			  			           if(value==3){
			  				        	  return '上下班拼车';
			  				          }
			  			           if(value==4){
			  				          return '长途拼车';
			  				      }	
			             	 }
			              },  
			              {field:'guestName',width:80,align:'center',title:'乘客姓名'},
			              {field:'guestPhone',width:120,align:'center',title:'乘客手机'},
			              {field:'carName',width:80,align:'center',title:'车主姓名'},
			              {field:'carPhone',width:120,align:'center',title:'车主手机'},
			              {field:'status',width:150,align:'center',title:'订单状态',
			             	 formatter:function(value,row,index){ 
			             		  if(value==1){
			  			        	  return '发布';
			  			          } if(value==2){
			  			        	  return '成交/待执行';
			  			          } if(value==3){
			  			        	  return '开始执行';
			  			          } if(value==4){
			  			        	  return '车主到达';
			  			          } if(value==5){
			  			        	  return '乘客上车';
			  			          } if(value==6){
			  			        	  return '执行结束/待支付';
			  			          } if(value==7){
			  			        	  return '已支付/待预约（单拼、长途完结';
			  			          } if(value==8){
			  			        	  return '已销单（非正常完结）';
			  			          } if(value==9){
			  			        	  return '结束服务（上下班完结）';
			  			          }
			             	 }
			              },
	            {field:'statusTime',width:200,align:'center',title:'状态时间',
	           	 formatter:function(value,row,index){ 
	           		  if(row.status==1){
	           			  date = row.cr_date;
				          } if(row.status==2){
				        	  date = row.ti_accept_order;
				          } if(row.status==3){
				        		date =  row.begin_exec_time;
				          } if(row.status==4){
				        		date = row.driverarrival_time;
				          } if(row.status==5){
				        		date = row.beginservice_time;
				          } if(row.status==6){
				        		date =row.stopservice_time;
				          } if(row.status==7){
				        		date =row.pay_time;
				          } if(row.status==8){
				        	  date =  row.pass_cancel_time;
				          } if(row.status==9){
				        		date =row.driver_cancel_time;
				        	
				          }
				          return date;
	           	 }
	            }
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
	}
	function qeee(order_cs_id){
		$.ajax({
			url:page_path+'findOrder_num.do?order_cs_id='+order_cs_id,
			success:function(data){
				alert(data);
				$('#id').val(data.id);
				$('#start_addr').val(data.start_addr);
				$('#order_cs_id').val(data.id);
				$('#order_city').val(data.order_city);
				$('#passagerphone').val(data.passagerphone);
				$('#passagercode').val(data.passagercode);
				$('#passagername').val(data.passagername);
				$('#driverphone').val(data.driverphone);
				$('#drivercode').val(data.drivercode);
				$('#drivername').val(data.drivername);
				$('#id').val(data.id);
				$('#begin_exec_time').val(data.begin_exec_time);
				$('#accept_time').val(data.accept_time);
				$('#price').val(data.price);
				$('#end_addr').val(data.end_addr);
				$('#password').val(data.password);
				  var	order_type=data.order_type;
				if(order_type==1){
					 var  str="单次拼车";
						$('#order_type').val(str);
				}if(order_type==2){
					 var  str="临时拼车";
						$('#order_type').val(str);
				}if(order_type==3){
					 var  str="上下班拼车";
						$('#order_type').val(str);
				}if(order_type==4){
					 var  str="长途拼车";
						$('#order_type').val(str);
				}
				$('#midpoint').val(data.midpoint);
				var  status=data.status;
				$('#status').combobox('select',status);
			},
		});
	}
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+h+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
	}
	function myparser(s){
		if (!s) return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);

		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	}
	function select(id)
	{
		closedialog();
		$('#order_cs_id').val(id).change();
		if(id==0){
			alert("没有值"+id);
		}if(id!=0){
			qeee(id);
		}
	}
	function addAction(){
		$.ajax({
			url : page_path+"add2.do",
			method:'post',
			data:{
				 "form_type":$('#form_type').val(),
				 "bussi_type":1,
				 "phone_incoming":$('#phone_incoming').val(),
				 "order_cs_id":$('#order_cs_id').val(),
				 "customer_name":$('#customer_name').val(),
				 "sex":$('#sex').val(),
				 "reason":$('#reason').val(),
			     "process_result":$('#process_result').val(),
			     "form_agree":0,
			     "city":$('#city_cur').val(),
			     "status":$('#status1').val(),
			},
			 success: function(data){
					if (data.err_code == 0) {
						alert("添加成功!");
						window.location=page_path+'index.do';
						
					} else {
						alert("添加失败!");
					}	 
				   },
			error: function() {
				alert("添加失败");
			},
			
		});
	}
	function change1(phone){
		$.ajax({
			url:page_path+'findNameSex.do?phone='+phone,
		    success:function(data){
		    	$("#customer_name").val(data.username);
		    	$("#sex").combobox('select',data.sex);
		    	$("#city_cur").val(data.city_cur);
		    }
		});
	}
	function viewInDetail(id)
	{  
		//window.location=page_path + "viewInDetail1.do?id="+id;
		window.open(page_path + "viewInDetail1.do?id="+id,'_blank');
		
	}
	function closeAdd() {
		window.location=page_path+"index.do";
	}
	 function closedialog(){
		 $('#dd').dialog("close");	 
	 }
		function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		}
</script>
</head>
<body onload="loaddone()">
	<div style="margin: 50px 100px;"></div>
	<div id="tab" class="easyui-tabs" style="width: 1400px">
		<div title="拼车业务" style="width: 1400px; margin: 0px 10px 10px 10px">
			<div style="float: left; width: 600px">
				<div class="easyui-panel" style="padding: 30px 10px 0px 0px">
					<form id="infoForm" method="post">
						<table>
							<tr style="height: 50px">
								<td style="text-align: right">工单类型：</td>
								<td><select id="form_type" name="form_type"
									class="easyui-combobox" style="width: 150px;">
										<option value="1">订单情况查询</option>
										<option value="2">费用咨询</option>
										<option value="3">App使用咨询</option>
										<option value="4">综合咨询</option>
										<option value="5">投诉</option>
								</select></td>
								<td class="right_labelstyle">订单编号：</td>
								<td><input id="order_cs_id" name="order_cs_id"
									Class="easyui-validatebox textbox" Style="width: 70px;" readOnly="readonly"/> <a
									href="javascript:void(0)" onclick="searchInterface() "
									class="easyui-linkbutton l-btn" iconCls="icon-search"
									style="width: 40px" ></a></td>
							</tr>
							<tr style="height: 50px">
								<td class="left_labelstyle">来电号码：</td>
								<td><input id="phone_incoming" name="phone_incoming"
									onchange="change1(this.value);" Class="textbox"
									style="width: 172px" /></td>

								<td class="right_labelstyle">所在城市：</td>
								<td><input id="city_cur" name="city_cur" Class="textbox"
									style="width: 120px" /></td>
							</tr>
							<tr style="height: 50px">
								<td class="left_labelstyle">客户姓名：</td>
								<td><input id="customer_name" name="customer_name"
									Class="textbox" style="width: 80px" /> <select id="sex"
									name="sex" class="easyui-combobox" style="width: 70px;">
										<option value="0">先生</option>
										<option value="1">女士</option>
								</select></td>
								<td></td>
								<td></td>
							</tr>
						</table>

						<table>
							<tr style="height: 50px">
								<td class="left_labelstyle">问题原因：</td>
								<td style="margin-left: 50px;">&nbsp; <textarea id="reason"
										name="reason" Class="textbox"
										Style="width: 385px; height: 50px;"></textarea>
								</td>
							</tr>

							<tr style="height: 50px">
								<td class="left_labelstyle">处理情况：</td>
								<td style="margin-left: 50px;">&nbsp; <textarea
										id="process_result" name="process_result" Class="textbox"
										Style="width: 385px; height: 50px;"></textarea>
								</td>
							</tr>
							<tr style="height: 50px">
								<td class="left_labelstyle">处理状态：</td>
								<td><select id="status1" name="status1"
									class="easyui-combobox" style="width: 150px;">
										<option value="1">未处理</option>
										<option value="2">已处理</option>
										<option value="3">处理中</option>

								</select></td>
							</tr>
						</table>
					</form>
					<div style="margin-left: 120px; padding: 5px">
						<a href="javascript:void(0)" class="easyui-linkbutton l-btn"
							onclick="addAction();" Style="width: 80px;">保存</a> <a
							href="javascript:void(0)" class="easyui-linkbutton l-btn"
							onclick="closeAdd();" Style="width: 80px; margin-left: 60px">取消</a>
					</div>
					<span style="margin-left: 42%" id="usermsg" class="login_alert"></span>
				</div>
				<span style="margin-left: 42%" id="usermsg" class="login_alert"></span>
			</div>


			<div title="订单信息" class="easyui-panel"
				style="float: right; width: 680px; height: 400;">
				<table>
					<tr style="height: 50px;">
						<td><label>订单编号：&nbsp;<input id="id" Class="textbox"
								disabled="disabled"></label></td>
						<td><label>订单类型：&nbsp;<input id="order_type"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>所在城市：&nbsp;<input id="order_city"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>下单时间：&nbsp;<input id="accept_time"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>执行时间：&nbsp;<input id="begin_exec_time"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>执行轨迹：&nbsp;<a href="javascript:void(0)"
								onclick="processsearch()" class="easyui-linkbutton l-btn"
								iconCls="icon-search" style="width: 131px">&nbsp;查询&nbsp;</a></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>乘客编号：&nbsp;<input id="passagercode"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>乘客姓名：&nbsp;<input id="passagername"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>乘客电话：&nbsp;<input id="passagerphone"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>车主编号：&nbsp;<input id="drivercode"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>车主姓名：&nbsp;<input id="drivername"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>车主电话：&nbsp;<input id="driverphone"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>出发地点：&nbsp;<input id="start_addr"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>目的地点：&nbsp;<input id="end_addr"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>中途点数：&nbsp;<input id="midpoint"
								Class="textbox" disabled="disabled"></label></td>
					</tr>
					<tr style="height: 50px;">
						<td><label>拼车单价：&nbsp;<input id="price"
								Class="textbox" disabled="disabled"></label></td>
						<td><label>电子车票：&nbsp;<input id="password" Class="textbox"
								disabled="disabled"></label></td>
						<td>订单状态：&nbsp; <select id="status" name="status"
							disabled="disabled" Class="easyui-combobox"
							Style="width: 150px; height: auto">
								<option value="1">发布</option>
								<option value="2">成交</option>
								<option value="3">开始执行</option>
								<option value="4">车主到达</option>
								<option value="5">乘客上车</option>
								<option value="6">执行结束</option>
								<option value="7">已结算</option>
								<option value="8">已关闭</option>
						</select>
						</td>
					</tr>
				</table>
			</div>
		</div>

		<div title="代驾业务" style="padding: 10px"></div>
	</div>
	<div id="dd"></div>
</body>
</html>


