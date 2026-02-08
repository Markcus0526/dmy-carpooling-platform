<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"financial/account/";
%>
<html>
<head>
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
<script>
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
//按条件查询
function searchaccount(){
	var khtype = $("#khtype").combobox("getValue");
	var khinfo = $("#khinfo").combobox("getValue");
	var xianginput = ($("#xianginput").val()=="")?0:$("#xianginput").val();
	$('#dg').datagrid('load',{
		khtype:khtype,
		khinfo:khinfo,
		xianginput:xianginput,
				});	
	
	
	
}
function   gettime(time2){
	  var JsonDateValue = new Date(time2.time);
	  var text = JsonDateValue.toLocaleString(); 
	             return text;
	         }
//数据表格加载
	$(function(){
		
		var page_path = '${pageContext.request.contextPath}/financial/account/';

		strurl = page_path + 'search1.do';
		$('#dg').datagrid({
	         url: strurl,
	         loadMsg : '数据处理中，请稍后....',
	         method:'post',
	 	    pageNumber:1,
	 	    iconCls : 'icon-show',
	 		pagination : true,
	 		pageSize : 10,
	 		
	 		nowrap : false,
	 		border : false,
	 		idFiled : 'id',
	 		rownumbers:true,
	 		singleSelect:true,
	 		fitColumns:true,
	 		showFooter:true,
	 		toolbar: '#toolbar',
	         columns:[[
	             {field:'operation',title:'操作',width:250, align:'center',
	            	 formatter:function(value,row,index){  
	     				
	 	          	    var btn="";
	 	          	    btn += '<a href="" onclick="javascript:charge('+row.userid+','+row.user_type+','+row.balance+','+row.ts_id+'); return false">充值</a>|'; 
	 	          	  btn += '<a href="" onclick="javascript:discharge('+row.userid+','+row.user_type+','+row.balance+','+row.ts_id+');return false">扣点</a>|'; 
	 	          	 btn += '<a href="" onclick="viewList('+row.userid+','+row.user_type+','+row.balance+','+row.userphone+'); return false">查看明细</a>'; 
	 	          	    return btn; }          
	         
	           }, 
	             {field:'userid',title:'客户ID',width:150, align:'center'},
	             {field:'user_type',title:'客户类别',width:150, align:'center',
	            	 formatter:function(value,row,index){ 
	             		  if(value==1){return '个人客户';}
	             		  if(value==2){
	             			 
	             			  return '集团客户';}
	             		 if(value==3){
	             		
	             			return '合作单位';}

	       				}},
	             {field:'usercode',title:'客户账号',width:100,align:'center',
	       				 formatter:function(value,row,index){ 
		             		  if(row.user_type==2||row.user_type==3){return '-';}
		             		  else{
		             			  return value;
		             		  }
		             		 
		             

		       				}
	       			
	  	       				},  
	             {field:'username',title:'客户姓名',width:150,align:'center'},
	             {field:'userphone',title:'手机号码',width:150,align:'center'},
	             {field:'balance',title:'绿点余额',width:150,align:'center'},
	             {field:'fb',title:'冻结绿点',width:150,align:'center',
	            	 formatter:function(value,row,index){ 
	             		  if(row.user_type==2||row.user_type==3){return '-';}
	             		  else{
	             			  return value;
	             		  }
	             		 
	             

	       				}
	             
	             },
	             {field:'transaction',title:'待审核交易',width:150,align:'center'},
	             {field:'ts_id',title:'tsid',width:150,align:'center'},
	         ]],
	     });
		
	});
	

//
function select(num){

	$('#orderid').val(num);
	$('#orderid1').val(num);
	closetb3();
}
	function charge(id1,usertype1,balance1,tsid){
		$('#tb1').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'充值',
			    width:400,   
			    height:450,   
			    modal:true , 
			    closable:true,
			    draggable:true,
			    border:true,
			    top:100,
			
			 onOpen:function(){
				 $('#type').val(usertype1);
				 $('#usersid').val(id1);
				 $('#balance1').val(balance1);
				 $('#tsid').val(tsid);
				 path='<%=basePath%>';
				 $.ajax({
					 url :path+'findusername.do?userid='+id1+'&&acctype='+usertype1,
					success:function(data){
						 $('#name').val(data.username);
						
						
					}
				 });
				   
			 }
			});  
			
		
	}
	//确定充值
	
	function up(){
		 path='<%=basePath%>';
		 var chergeid =$('#usersid').val();//客户id
		 var chergeremark=$('#remark').val();//详细说明
		 var chergesum =$('#sum1').val();//操作金额
		 var chergeorder=$('#orderid').val();
		 if(chergeorder==""){
			 chergeorder=0;
		 }
		 var req_cause=$('#reqselect').combobox('getText');
		 var account_type=$('#type').val();
		 var tsid=$('#tsid').val();
	if(chergesum==""){
		 $.messager.alert('警告','操作金额不能为空！！！','warning');  
	}else{
		 $.messager.confirm('提示信息','确定充值？',function(r){   
			    if (r){   
			    	 $.ajax({
						 url :path+'charge.do',
						 method:'post',
						 data : 'chergeid='+chergeid+'&&account_type='+account_type+'&&chergeremark='+chergeremark+'&&chergesum='+chergesum+'&&chergeorder='+chergeorder+'&&req_cause='+req_cause+'&&tsid='+tsid+'&&oper=2',
							dataType : "json",
						success:function(data){
							
						}
					 });
			    	 searchaccount();
			    	 closedg1(); 
			    }   
			});  

		
	}
	
	
}
	//扣点
	
	function discharge(id,usertype,balance,tsid1){
	
		$('#tb2').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'扣点',
			    width:400,   
			    height:450,   
			    modal:true , 
			    closable:true,
			    draggable:true,
			    border:true,
			    top:100,
		
				 onOpen:function(){
					 $('#type1').val(usertype);
					 $('#usersid1').val(id);
					 $('#balance').val(balance);
					 $('#tsid1').val(tsid1);
					 path='<%=basePath%>';
					 $.ajax({
						 url :path+'findusername.do?userid='+id+'&&acctype='+usertype,
						success:function(data){
							 $('#name1').val(data.username);
							
							
						}
					 });
					   
				 }
			});  
	}
	//确定扣点
	function up1(){
		 path='<%=basePath%>';
		 var chergeid1 =$('#usersid1').val();//客户id
		 var chergeremark1=$('#remark1').val();//详细说明
		 var chergesum1 =$('#sum').val();//操作金额
		 var chergeorder1=$('#orderid1').val();
		 if(chergeorder1==""){
			 chergeorder1=0;
		 }
		 
		 var req_cause1=$('#reqselect1').combobox('getText');
		 var account_type1=$('#type1').val();
		 var tsid1=$('#tsid1').val();
			if(chergesum1==""){
				 $.messager.alert('警告','操作金额不能为空！！！','warning');  
			}else{
				chergesum1 = 0-chergesum1;
				 $.messager.confirm('提示信息','确定扣点？',function(r){   
					    if (r){   
					    	 $.ajax({
								 url :path+'charge.do',
								 method:'post',
								 data : 'chergeid='+chergeid1+'&&account_type='+account_type1+'&&chergeremark='+chergeremark1+'&&chergesum='+chergesum1+'&&chergeorder='+chergeorder1+'&&req_cause='+req_cause1+'&&tsid='+tsid1+'&&oper=1',
									dataType : "json",
								success:function(data){
									
								}
							 });
					    	 searchaccount();
					    	 closedg2(); 
					    }   
					});  
				
			}
	
	
}
	//点击选择 相关订单的 探出
	function ordersearch(){
		 $('#tb3').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'查询订单',
			    width:1070,   
			    height:1100,   
			    modal:true , 
			    closable:true,
			    draggable:true,
			    border:true,
			    top:50,
			 onOpen:function(){
				
				 var tb3id=$('#usersid').val();
				 var tb3name=$('#name').val();
				 $('#tb3usersid').val(tb3id);
				 $('#tb3orderusername').val(tb3name);
				 loaddata(tb3id);
				   
			 }
			});  
		
	}
	function ordersearch1(){
		 $('#tb3').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'查询订单',
			    width:1070,   
			    height:1100,   
			    modal:true , 
			    closable:true,
			    draggable:true,
			    border:true,
			    top:50,
			   
			 onOpen:function(){
				 
				 var tb3id=$('#usersid1').val();
				 var tb3name=$('#name').val();
				 $('#tb3usersid').val(tb3id);
				 $('#tb3orderusername').val(tb3name);
				 loaddata(tb3id);
				   
			 }
			});  
		
	}
	//弹出页面 数据表格的加载
	function loaddata(id){
	
	
	$('#tb3dg').datagrid({
		queryParams:{	    	  
			chkusertype:$("#tb3chkusertype").get(0).checked == true ? 1 : 0,
			chkusertype1:$("#tb3chkusertype1").get(0).checked == true ? 1 : 0,
			ordertype1:$("#tb3ordertype1").get(0).checked == true ? 1 : 0,	
			ordertype2:$("#tb3ordertype2").get(0).checked == true ? 1 : 0,	
			ordertype3:$("#tb3ordertype3").get(0).checked == true ? 1 : 0,	
			orderstatus1:$("#tb3orderstatus1").get(0).checked == true ? 1 : 0,
			orderstatus2:$("#tb3orderstatus2").get(0).checked == true ? 1 : 0,
   },
		title:"客户信息",
	    url:'<%=basePath%>findorder.do?usersid='+id, 
	    method:'post',
	    pageNumber:1,
	    iconCls : 'icon-show',
		pagination : true,
		pageSize : 10,
		nowrap : false,
		border : false,
		idFiled : 'id',
		rownumbers:true,
		singleSelect:true,
		fitColumns:true,
		showFooter:true,
		columns : [ [ {
			field : 'op',
			title : '操作',
			width : 100,
			formatter:function(value,row,index){  
				
	          	    var btn="";
	          	    btn += '<a href="" class="editcls" onclick="orderview('+row.ordercsid+');return false">查看</a>|'; 
	          	  btn += '<a href="" class="editcls" onclick="select('+row.ordercsid+');return  false">选择</a> &nbsp;'; 
	          	    return btn;  }
     		   
		},
		{
			field : 'ordercsid',
			title : '执行订单号',
			width : 100,
			align:'center'
		}, 
		{
			field : 'ordertype',
			title : '订单类型',
			width : 100,
			align:'center',
			formatter:function(value,row,index){  
				if(value==1||value==2){
					return '单次拼车';
				}
				if(value==3){
					return '上下班拼车';
				}
				if(value==4){
					return '长途拼车';
				}
          	  }
		}, 
		{
			field : 'pname',
			title : '乘客姓名',
			width : 100,
			align:'center'
		}, {
			field : 'pphone',
			title : '乘客手机',
			width : 100,
			align:'center'
		}, {
			field : 'dname',
			title : '车主姓名',
			width : 100,
			align:'center'
		},{
			field : 'dphone',
			title : '车主手机',
			width : 100,
			align:'center'
		}, {
			field : 'orderstatus',
			title : '订单状态',
			width : 100,
			align:'center',
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
		        	  return '已支付/待预约（单拼、长途完结)';
		          } if(value==8){
		        	  return '已销单（非正常完结）';
		          } if(value==9){
		        	  return '结束服务（上下班完结）';
		          }
       	 }

			
		}
		, {
			field : 'statustime',
			title : '状态时间',
			width : 100,
			align:'center',
			formatter:function(value,row,index){ 
      		  if(row.orderstatus==1){
      			  if(row.cr_date!=null){
      				  date = gettime(row.cr_date);
      			  }else
      				  {
      				  date="-";
      				  }
      			
		          } 
      		  
      		  
      		  if(row.orderstatus==2){
		        	
		        	  if(row.ti_accept_order!=null){
	      				  date = gettime(row.ti_accept_order);
	      			  }else
	      				  {
	      				  date="-";
	      				  }
		          } 
      		  if(row.orderstatus==3){
      			  if(row.begin_exec_time!=null){
      				  date = gettime(row.begin_exec_time);
      			  }else
      				  {
      				  date="-";
      				  }
		        		
		          } 
      		  if(row.orderstatus==4){
      			  if(row.driverarrival_time!=null){
      				  date = gettime(row.driverarrival_time);
      			  }else
      				  {
      				  date="-";
      				  }
		        		
		          } 
      		  if(row.orderstatus==5){
      			  if(row.beginservice_time!=null){
      				  date = gettime(row.beginservice_time);
      			  }else
      				  {
      				  date="-";
      				  }
		        		
		          } 
      		  if(row.orderstatus==6){
      			  if(row.stopservice_time!=null){
      				  date = gettime(row.stopservice_time);
      			  }else
      				  {
      				  date="-";
      				  }
		        		
		          }
      		  if(row.orderstatus==7){
      			  if(row.pay_time!=null){
      				  date = gettime(row.pay_time);
      			  }else
      				  {
      				  date="-";
      				  }
		     
		          } 
      		  if(row.orderstatus==8){
      			  if(row.pass_cancel_time!=null){
      				  date = gettime(row.pass_cancel_time);
      			  }else
      				  {
      				  date="-";
      				  }
		        	  
		          } 
      		  if(row.orderstatus==9){
      			  if(row.driver_cancel_time!=null){
      				  date = gettime(row.driver_cancel_time);
      			  }else
      				  {
      				  date="-";
      				  }
		        		
		        	
		          }
		          return date;

				}}
		
		] ]
			});
	

	
}
	//订单选择 查询方法
	//查询方法
function ordersearchtb3(){
	var chkusertype=$("#tb3chkusertype").get(0).checked == true ? 1 : 0;
	var chkusertype1=$("#tb3chkusertype1").get(0).checked == true ? 1 : 0;
	var ordertype1=$("#tb3ordertype1").get(0).checked == true ? 1 : 0;
	var ordertype2=$("#tb3ordertype2").get(0).checked == true ? 1 : 0;
	var ordertype3=$("#tb3ordertype3").get(0).checked == true ? 1 : 0;
	var orderstatus1=$("#tb3orderstatus1").get(0).checked == true ? 1 : 0;
	var orderstatus2=$("#tb3orderstatus2").get(0).checked == true ? 1 : 0;
	var ordernum=$("#tb3ordercs").val();
	var userorderselect=$("#tb3userorderselect").combobox('getValue');
	var begintime = $("#tb3date1").datebox("getValue");
	var endtime = $("#tb3date2").datebox("getValue");
	var orderusername = $("#tb3orderusername").val();


	$('#tb3dg').datagrid('load',{
		chkusertype :chkusertype,
		chkusertype1:chkusertype1,
		ordertype1:ordertype1,
		ordertype2:ordertype2,
		ordertype3:ordertype3,
		orderstatus1:orderstatus1,
		orderstatus2:orderstatus2,
		userorderselect:userorderselect,
		orderusername:orderusername,
		begintime:begintime,
		endtime:endtime,
		ordernum:ordernum,

				});		
}
	//查看详情

function orderview(ordercsid){

	path='<%=basePath%>';
		window.open(path+'info.do?orderid1='+ordercsid,"_blank");
	
}
	
	
	
	//关闭
	 function closedg1(){
		 $('#tb1').dialog("close");
		 
	 }
	 
	 function closedg2(){
		 $('#tb2').dialog("close");
		 
		 
	 }
	 
	 function closetb3(){
		 $('#tb3').dialog("close");
		 
		 
	 }
	 //
	function viewList(id,account_type,balance,phone){
		
		path='<%=basePath%>';
		window.location=path+'view.do?viewaccounttype='+account_type+'&&viewuserid='+id+'&&viewbalance='+balance+'&&viewphone='+phone;

		
	}	
</script>
</head>
<body onload="loaddone()">
	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 2px solid; padding: 15px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'客户账户管理',border:true"
				style="height: 1px;">
				<form id="searchForm">
					<div id="toolbar" style="margin-top: 5px">
						<select id="khtype" name="khtype" class="easyui-combobox"
							style="width: 90px;">
							<option value="1">个人客户</option>
							<option value="2">集团客户</option>
							<option value="3">合作单位</option>
						</select> <select id="khinfo" name="khinfo" class="easyui-combobox"
							style="width: 70px;">
							<option value="1">客户id</option>
							<option value="2">客户账号</option>
							<option value="3">客户名称</option>
							<option value="4">手机号码</option>
						</select> <input id="xianginput" type="text" class="easyui-searchbox"
							data-options="searcher:searchaccount" style="height: 22px" />
					</div>
				</form>
			</div>
			<div data-options="region:'center',title:''"
				style="border: #95B8E7 1px solid;">
				<table id="dg" style="padding: 1px; boder: #95B8E7 1px solid;"
					data-options="fit:true,border:true">
				</table>
				<div id="tb1">

					<div
						style="heigth: 900px; padding: 10px; margin-right: 10px; margin-top: 10px; margin-left: 10px; margin-button: 10px; border: 1px solid #EDF4FF;">
						<span>客户ID:<input id="usersid"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="usersid" readonly="readonly" /></span><br></br> <span>客户姓名:<input
							id="name"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="name" readonly="readonly" /></span> <br></br> <input id="type"
							hidden="hidden"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="type" readonly="readonly" /> <span>客户余额:<input
							id="balance1"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="balance1" readonly="readonly" /></span><br></br> <span>充值金额:<input
							id="sum1" style="width: 100px;" data-options="required:true"
							class="easyui-validatebox textbox" name="sum1" /></span><br></br> <span>
							操作原因:<select id="reqselect" name="searchType"
							class="easyui-combobox">'
								<option value="0">线下交费充值</option>
								<option value="1">系统出错补偿</option>
								<option value="2">其他</option>
						</select>
						</span><br></br> <span>相关订单:</span><input id="orderid"
							data-options="required:true" class="textbox"
							style="width: 100px;" name="orderid" /><a
							onclick="ordersearch()" style="width: 30px;"
							class="easyui-linkbutton" iconCls="icon-search"></a><br></br> <span
							style="color: red;">详细说明(操作原因为“其他”时，在里面输入原因):</span><br>
						<textarea id="remark" cols="5" rows="6"
							style="margin-left: 10px; width: 160px; height: 95;"
							name="remark"></textarea>
						<br>
						<a href="javascript:void(0)" onclick="up()"
							style="margin-left: 35px; width: 80px;" class="easyui-linkbutton"
							iconCls="icon-ok">确定</a> <a href="javascript:void(0)"
							onclick="closedg1()" style="margin-left: 10px; width: 80px;"
							class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
					</div>
					<input type="hidden" id="formid"> <input type="hidden"
						id="tsid">
				</div>

				<div id="tb2">




					<div
						style="heigth: 900px; padding: 10px; margin-right: 10px; margin-top: 10px; margin-left: 10px; margin-button: 10px; border: 1px solid #EDF4FF;">
						<span>客户ID:<input id="usersid1"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="usersid1" readonly="readonly" /></span><br></br> <span>客户姓名:<input
							id="name1"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="name" readonly="readonly" /></span> <br></br> <input id="type1"
							hidden="hidden"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="type" readonly="readonly" /> <span>客户余额:<input
							id="balance"
							style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
							name="balance1" readonly="readonly" /></span><br></br> <span>扣点金额:<input
							id="sum" style="width: 100px;" class="easyui-validatebox textbox"
							data-options="required:true" name="sum1" /></span><br></br> <span>
							操作原因:<select id="reqselect1" name="searchType"
							class="easyui-combobox">
								<option value="0">违约处罚</option>
								<option value="1">系统出错补扣</option>
								<option value="2">其他</option>
						</select>
						</span><br></br> <span>相关订单:</span><input id="orderid1"
							style="width: 100px;" name="orderid" class="textbox"
							data-options="required:true" /><a onclick="ordersearch1()"
							style="width: 30px;" class="easyui-linkbutton"
							iconCls="icon-search"></a><br></br> <span style="color: red;">详细说明(操作原因为“其他”时，在里面输入原因):</span><br>
						<textarea id="remark1" cols="5" rows="6"
							style="margin-left: 10px; width: 170px; height: 95;"
							name="remark"></textarea>
						<br>
						<a href="javascript:void(0)" onclick="up1()"
							style="margin-left: 35px; width: 80px;" class="easyui-linkbutton"
							iconCls="icon-ok">确定</a> <a href="javascript:void(0)"
							onclick="closedg2()" style="margin-left: 10px; width: 80px;"
							class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
					</div>
					<input type="hidden" id="formid"> <input type="hidden"
						id="tsid1">
				</div>

				<div id="tb3">




					<h2 class="page-title txt-color-blueDark">
						<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp选择单个订单
					</h2>
					<HR>
					'
					<div>
						<select id="tb3userorderselect" name="searchType"
							class="easyui-combobox">
							<option value="1">客户ID</option>
							<option value="2">客户手机</option>
						</select>' <input id="tb3usersid" type="text" name="tb3usersid"
							class="textbox" style="height: 23px" /> <label
							style="margin-left: 20px;">客户姓名：</label><input
							id="tb3orderusername" name="tb3orderusername" type="text"
							class="textbox" style="height: 23px" />
					</div>
					<div style="margin-top: 10px;">
						成交时间：</label><input id="tb3date1" type="text" class="easyui-datebox">--<input
							id="tb3date2" type="text" class="easyui-datebox"> <label
							style="margin-left: 20px;">客户身份：</label> <input type="checkbox"
							id="tb3chkusertype" name="tb3chkusertype" checked="checked" /> <label>乘客</label>
						<input type="checkbox" id="tb3chkusertype1" name="tb3chkusertype1"
							checked="checked" /> <label>车主</label>
					</div>
					<div style="margin-top: 10px;">
						<label> 订单类型：</label> <input type="checkbox" id="tb3ordertype1"
							name="tb3ordertype1" checked="checked" /> <label>单次拼车</label> <input
							type="checkbox" id="tb3ordertype2" name="tb3ordertype2"
							checked="checked" /> <label>上下班拼车</label> <input type="checkbox"
							id="tb3ordertype3" name="tb3ordertype3" checked="checked" /> <label>长途拼车</label>
					</div>
					<div style="margin-top: 10px;">
						' <label> 订单状态：</label>' <input type="checkbox"
							id="tb3orderstatus1" name="tb3orderstatus1" checked="checked" />
						<label>已完成</label> <input type="checkbox" id="tb3orderstatus2"
							name="tb3orderstatus2" checked="checked" /> <label>未完成</label>
					</div>
					<div style="margin-top: 10px;">
						' <label>订单编号：</label><input style="height: 23px" type="text"
							id="tb3ordercs" class="textbox"> <a
							onclick="ordersearchtb3()"
							style="width: 80px; margin-left: 330px;"
							class="easyui-linkbutton" iconCls="icon-search">查询</a>
					</div>
					<HR>
					<div style="margin-left: 10px;">
						<table id="tb3dg"></table>
					</div>
					<a href="javascript:void(0)" onclick="closetb3()"
						style="margin-left: 10px; width: 50px;" class="easyui-linkbutton">关闭</a>





				</div>
			</div>
		</div>
	</div>




</body>

</html>