<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"financial/manage/";
String basePath1 = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
//System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/defaultnkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
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

<style type="text/css">
</style>
<script type="text/javascript" charset="UTF-8">
function excel(){
	   var chkwait=$("#chkwait").get(0).checked == true ? 1 : 0;
		var chkfinish=$("#chkfinish").get(0).checked == true ? 1 : 0;
		 var chkrefuse=$("#chkrefuse").get(0).checked == true ? 1 : 0;
		  var chkcancel=$("#chkcancel").get(0).checked == true ? 1 : 0;
		var chkuser=$("#chkuser").get(0).checked == true ? 1 : 0;
		 var chkgroup=$("#chkgroup").get(0).checked == true ? 1 : 0;
		 var chkunit=$("#chkunit").get(0).checked == true ? 1 : 0;
		 var userselect=$("#userselect").combobox('getValue');
		 var input = $("#selectinput").val();
	location ="<%=basePath1%>excel/listMyFormToExcel.do?chkwait="+chkwait+"&chkfinish="+chkfinish+"&chkrefuse="+chkrefuse+"&chkcancel="+chkcancel+"&chkuser="+chkuser+"&chkgroup="+chkgroup+"&chkunit="+chkunit+"&userselect="+userselect+"&input="+input;
}

//查询订单 选择的方法
function select(num){

	$('#orderid').val(num);
	closetb3();
}
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
//查看详情
function orderview(ordercsid){
	path='<%=basePath%>';
		window.open(path+'info.do?orderid='+ordercsid,"_blank");
	
}
function orderview1(order_cs_id){
	
	path='<%=basePath%>';
		window.open(path+'info.do?orderid='+order_cs_id,"_blank");
	
}
//查询方法
function ordersearchtb3(){
	var chkusertype=$("#tb3chkusertype").get(0).checked == true ? 1 : 0;
	var chkusertype1=$("#tb3chkusertype1").get(0).checked == true ? 1 : 0;
	var ordertype1=$("#tb3ordertype1").get(0).checked == true ? 1 : 0;
	var ordertype2=$("#tb3ordertype2").get(0).checked == true ? 1 : 0;
	var ordertype3=$("#tb3ordertype3").get(0).checked == true ? 1 : 0;
	var orderstatus1=$("#tb3orderstatus1").get(0).checked == true ? 1 : 0;
	var orderstatus2=$("#tb3orderstatus2").get(0).checked == true ? 1 : 0;
	var ordernum=$("#tb3ordercsid").val();
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
//加载表格中的全部数据

function loaddata(){
	
	var id=$('#usersid').val();
	if(id==""){
		
		id=$('#usersid1').val();
		
	}
	
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
		title:"个人客户",
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
		striped:true,
		columns : [ [ {
			field : 'op',
			title : '操作',
			width : 60,
			formatter:function(value,row,index){  
				
	          	    var btn="";
	          	    btn += '<a  href="" onclick="orderview('+row.ordercsid.toString()+');return false">查看</a>|'; 
	          	  btn += '<a  href="" onclick="select('+row.ordercsid.toString()+');return false">选择</a> &nbsp;'; 
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
      			  date = row.cr_date;
		          } if(row.orderstatus==2){
		        	  date = row.ti_accept_order;
		          } if(row.orderstatus==3){
		        		date =  row.begin_exec_time;
		          } if(row.orderstatus==4){
		        		date = row.driverarrival_time;
		          } if(row.orderstatus==5){
		        		date = row.beginservice_time;
		          } if(row.orderstatus==6){
		        		date =row.stopservice_time;
		          } if(row.orderstatus==7){
		        		date =row.pay_time;
		          } if(row.orderstatus==8){
		        	  date =  row.pass_cancel_time;
		          } if(row.orderstatus==9){
		        		date =row.driver_cancel_time;
		        	
		          }
		          return date;

				}}
		
		] ]
			});
	

	
}
//ordersearch 查询相关订单
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
			 loaddata();
			 var tb3id=$('#usersid').val();
			 var tb3name=$('#name').val();
			 $('#tb3usersid').val(tb3id);
			 $('#tb3orderusername').val(tb3name);
			   
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
			 loaddata();
			 var tb3id=$('#usersid1').val();
			 var tb3name=$('#name1').val();
			 $('#tb3usersid').val(tb3id);
			 $('#tb3orderusername').val(tb3name);
			   
		 }
		});  
	
}
//根据条件查询
	function s(){
		        var chkwait=$("#chkwait").get(0).checked == true ? 1 : 0;
				var chkfinish=$("#chkfinish").get(0).checked == true ? 1 : 0;
				 var chkrefuse=$("#chkrefuse").get(0).checked == true ? 1 : 0;
				  var chkcancel=$("#chkcancel").get(0).checked == true ? 1 : 0;
				var chkuser=$("#chkuser").get(0).checked == true ? 1 : 0;
				 var chkgroup=$("#chkgroup").get(0).checked == true ? 1 : 0;
				 var chkunit=$("#chkunit").get(0).checked == true ? 1 : 0;
				 var userselect=$("#userselect").combobox('getValue');
				 var input = $("#selectinput").val();
		 $('#dg').datagrid('load',{
			 chkwait :chkwait,
			 chkfinish:chkfinish,
			 chkrefuse:chkrefuse,
			 chkcancel:chkcancel,
		    	chkuser:chkuser,
		    	chkgroup:chkgroup,
		    	chkunit:chkunit,
		    	userselect:userselect,
		    	input:input,
		    	
					});		
	}
	//待我审核查询
	function s2(){
				var chkuser=$("#chkuser1").get(0).checked == true ? 1 : 0;
				 var chkgroup=$("#chkgroup1").get(0).checked == true ? 1 : 0;
				 var chkunit=$("#chkunit1").get(0).checked == true ? 1 : 0;
				 var userselect=$("#userselect1").combobox('getValue');
				 var input = $("#selectinput1").val();
		 $('#dg1').datagrid('load',{
		    	chkuser:chkuser,
		    	chkgroup:chkgroup,
		    	chkunit:chkunit,
		    	userselect:userselect,
		    	input:input,
		    	
					});		
	}
	
//修改表单-充值 确定
	function up(){
		 var chergeid =$('#usersid').val();//客户id
		 var chergeformid =$('#formid').val();//客户id
		 var chergeremark=$('#remark').val();//详细说明
		 var chergesum =$('#sum1').val();//操作金额
		 var chergebalance=$('#balance1').val();//余额
		 var chergeorder=$('#orderid').val();
		 if(chergeorder==''){
			 chergeorder=0;
		 }
		 var req_cause=$('#reqselect').combobox('getText');		
			
	if(req_cause=="其他"&&chergeremark==""){
		
		alert("请输入详细说明！");
		
	}else{
		 if(!isNaN(chergesum)){
			 if(chergesum>0){
				 
				 $.messager.confirm('提示信息','确定修改？',function(r){   
					    if (r){   
					    	 $.ajax({
								 url :path+'cherge.do',
								 method:'post',
								 data : 'chergeid='+chergeid+'&&chergebalance='+chergebalance+'&&chergeremark='+chergeremark+'&&chergesum='+chergesum+'&&chergeorder='+chergeorder+'&&req_cause='+req_cause+'&&chergeformid='+chergeformid,
									dataType : "json",
								success:function(data){
									$.messager.alert('提示信息','修改成功！');  
									$('#dg').datagrid('reload');
								}
							 });
							closec();	 
					    }   
					});  

				 
				 
			 }else{
				 alert("请输入大于0的数字")
			 }
			   
			}else{
			   alert("请输入正确格式！");
			}
			
		
		
	}
			
		 
			
	
	
	
}
	function up1(){
		 var chergeid1 =$('#usersid1').val();//客户id
		 var chergeformid1 =$('#formid1').val();//客户id
		 var chergeremark1=$('#remark1').val();//详细说明
		 var chergesum1 =$('#sum2').val();//操作金额
		 var chergebalance1=$('#balance2').val();//余额
		 var chergeorder1=$('#orderid1').val();
		 if(chergeorder1==''){
			 chergeorder1=0;
		 }
		 var req_cause1=$('#reqselect1').combobox('getText');
		 if(req_cause1=="其他"&&chergeremark1==""){
			 
			 alert("请输入详细说明");
			 
		 }else{
			 
			 if(!isNaN(chergesum1)){
				 if(chergesum1<0){
					 
					 if(chergesum1+chergebalance1<0){
						 
						 $.messager.confirm('提示信息','确定修改？',function(r){   
							    if (r){   
							    	 $.ajax({
										 url :path+'cherge.do',
										 method:'post',
										 data : 'chergeid='+chergeid1+'&&chergebalance='+chergebalance1+'&&chergeremark='+chergeremark1+'&&chergesum='+chergesum1+'&&chergeorder='+chergeorder1+'&&req_cause='+req_cause1+'&&chergeformid='+chergeformid1,
											dataType : "json",
										success:function(data){
											$.messager.alert('提示信息','修改成功！');  
											$('#dg').datagrid('reload');
										}
									 });
									closec();	 
							    }   
							});  

						 
						 
					 }else{
						 alert("扣点后余额小于0");
					 }
				 }else{alert("请输入小于0的数字！")}
				
				   
				}else{
				   alert("请输入正确格式！");
				}
				
			 
		 }
		
			 
		 
			
	
	
	
}
//撤销功能
function recolse(formid){
	path='<%=basePath%>';
	 if(confirm('确定撤销'+formid+'？')){
	 $.ajax({
		 method:'post',
		 url :path+'colse.do?colseformid='+formid,
		success:function(data){
			alert("撤销成功");
		
		
			
			
		}
	 });
	 $('#dg').datagrid('reload');
	 }
}
//点击修改重提弹出的窗口
	function modify(formid,sum){
	if(sum>0){
		$('#tb').dialog({ 
		   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
		    title:'修改表单——充值',
		    width:400,   
		    height:500,   
		    modal:true , 
		    closable:true,
		    draggable:true,
		    border:true,
		    top:80,
		 onOpen:function(){
			 path='<%=basePath%>';
			 $.ajax({
				 url :path+'findbyid.do?id='+formid,
				success:function(data){
					 $('#name').val(data.rows.username);
					 $('#balance1').val(data.rows.balance);
					 $('#remark').val(data.rows.remark);
					 $('#sum1').val(data.rows.sum);
					 $('#type').val(data.rows.account_type);
					 $('#usersid').val(data.rows.userid);
					
					 $('#formid').val(formid);
				}
			 });
			   
		 }
		});  
		
	}else {
		
		//审核金额为负数
		
		$('#tb1').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'修改表单——扣点',
			    width:400,   
			    height:500,   
			    modal:true , 
			    closable:true,
			    draggable:true,
			    border:true,
			    top:80,
			 onOpen:function(){
				 path='<%=basePath%>';
				 $.ajax({
					 url :path+'findbyid.do?id='+formid,
					success:function(data){
						 $('#name1').val(data.rows.username);
						 $('#balance2').val(data.rows.balance);
						 $('#remark1').val(data.rows.remark);
						 $('#sum2').val(data.rows.sum);
						 $('#type1').val(data.rows.account_type);
						 $('#usersid1').val(data.rows.userid);
						 $('#formid1').val(formid);
					}
				 });
				   
			 }
			});  
		
		
	}
		
	 }
	 //关闭弹出窗
	  function  closec(){
		 $('#tb').dialog("close");
		
	}
	  function  closec1(){
			 $('#tb1').dialog("close");
			
		}
	 function closeu(){
		 $('#tb2').dialog("close");
		 
	 }
	 
	 function closetb3(){
		 $('#tb3').dialog("close");
		 
		 
	 }
	//关闭
	//function del(formid,b){
		//alert(formid+''+b);
		
		
	//}
	
	//驳回
	function unpass(formid){
			$('#tb2').dialog({ 
				   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
				    title:'驳回',
				    width:250,   
				    height:250,   
				    modal:true , 
				    closable:true,
				    draggable:true,
				    border:true,
				    top:100,
				
				    onOpen:function(){						
								var formidun= $('#formidunpass').val(formid);			   
					 }
				
				});  
		
	}
	//确定驳回
	function unpassup(){
		var formidun= $('#formidunpass').val();
		var reject_cause= $('#unpasscause').val();
		if(reject_cause==""||reject_cause==null){
			
			alert("请输入驳回理由！")
			
		}else{
			
			$.messager.confirm('提示信息','确定驳回？',function(r){   
			    if (r){   
			    	 path='<%=basePath%>';
					 $.ajax({
						 method:'post',
						 url :path+'unpass.do',
						 data : 'unformid='+formidun+'&&reject_cause='+reject_cause,
						success:function(data){
							
							$.messager.alert('提示信息','操作成功!','info');


							closeu();
							s2();
							$('#dg').datagrid('reload');
						}
					 });
			    }   
			});  
			
			
		}
		 
		
		
	}
	
	//通过审核
	function pass(formid,passbalance,passsum,tsorderid,userid,accounttype){
		var formidun= formid;
		 path='<%=basePath%>';
		 $.messager.confirm('提示信息','操作金额为：'+(passsum),function(r){   
			    if (r){   
			    	 $.ajax({
						 method:'post',
						 url :path+'pass.do?unformid='+formidun+'&&passbalance='+passbalance+'&&passsum='+passsum+'&&tsorderid='+tsorderid+'&&uid='+userid+'&&uaccounttype='+accounttype,
						success:function(data){
							$.messager.alert('提示信息','操作成功!','info');
						
							s2();
							
						}
					 }); 
			    }   
			});  
		
	}
	//加载表格中的全部数据
$(function(){
	$('#formandshenhe').tabs({ 
		border:true, 
		onSelect:function(title){ 
			$('#dg1').datagrid({
				title:" 待我审核",
			    url:'<%=basePath%>findwait.do', 
			    queryParams:{	    	  
						 chkuser:$("#chkuser1").get(0).checked == true ? 1 : 0,
						  chkgroup:$("#chkgroup1").get(0).checked == true ? 1 : 0,
						 chkunit:$("#chkunit1").get(0).checked == true ? 1 : 0,	 
			    },
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
				striped:true,
				columns : [ [ {
					field : 'op',
					title : '操作',
					width : 150,
					formatter:function(value,row,index){  
						
			          	    var btn="";
			          	    btn += '<a href=""  onclick="pass('+row.formid+','+row.balance+','+row.sum+','+row.order_cs_id+','+row.userid+','+row.account_type+');return false;">通过</a>|'; 
			          	  btn += '<a href=""  onclick="unpass('+row.formid+');return false;">驳回</a>'; 
			          	    return btn;  }
		     		   
				}, {
					field : 'formid',
					title : '表单号',
					width : 100,
					align:'center',
						
				}, 
				
				{
					field : 'userid',
					title : '客户ID',
					width : 100,
					align:'center'
				}, 
				{
					field : 'account_type',
					title : '客户类别',
					width : 100,
					align:'center',
					formatter:function(value,row,index){  
						if(value==3){
							return '合作单位';
						}
						if(value==2){ 
							return '集团客户';
							
						}
						if(value==1){}
						
						return '个人客户';
						
		          	     }
				}, {
					field : 'username',
					title : '客户名称',
					width : 100,
					align:'center'
				}, {
					field : 'phone',
					title : '联系电话',
					width : 100,
					align:'center'
				},{
					field : 'balance',
					title : '余额',
					width : 100,
					align:'center'
				}, {
					field : 'sum',
					title : '审核金额',
					width : 100,
					align:'center',
					formatter:function(value,row,index){  
						if(value>0){
						    var btn="+"+value;
				          	   
			          	    
			          	 
			          	    return btn; 
						}else{
							
							return value;
							
						}
			          	 }
				}
				, {
					field : 'req_cause',
					title : '操作原因',
					width : 100,
					align:'center'
				}
				, {
					field : 'order_cs_id',
					title : '相关订单',
					width : 100,
					align:'center',
					formatter:function(value,row,index){  
						
		          	    var btn="";
		          	    if(value!=0){
		          	    btn += '<a  href="" class="editcls" onclick=" orderview1('+row.order_cs_id+');return false;">'+row.order_cs_id+'</a>'; 
		          	    }
		          	    return btn;  }}
				, {
					field : 'remark',
					title : '详细说明',
					width : 100,
					align:'center'
				}
				, {
					field : 'bname',
					title : '操作员',
					width : 100,
					align:'center'
				}
				] ]
					});
		    
		} 
		}); 
	$('#dg').datagrid({
		title:"我的表单表格",
	    url:'<%=basePath%>find.do', 
	    queryParams:{
	    	  chkwait:$("#chkwait").get(0).checked == true ? 1 : 0,
				 chkfinish:$("#chkfinish").get(0).checked == true ? 1 : 0,
				 chkrefuse:$("#chkrefuse").get(0).checked == true ? 1 : 0,
				   chkcancel:$("#chkcancel").get(0).checked == true ? 1 : 0,
				 chkuser:$("#chkuser").get(0).checked == true ? 1 : 0,
				  chkgroup:$("#chkgroup").get(0).checked == true ? 1 : 0,
				 chkunit:$("#chkunit").get(0).checked == true ? 1 : 0,	 
	    },
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
		striped:true,
		columns : [ [ {
			field : 'op',
			title : '操作',
			width : 150,
			formatter:function(value,row,index){  
				if(row.status=='待审核'||row.status=='已驳回'){
				    var btn="";
		          	   
	          	    btn += '<a href=""  onclick="modify('+row.formid+','+row.sum+');return false;">修改重提</a>| &nbsp;'; 
	          	 if(row.status=='已撤销'){
		          	   
	          		 btn += ''; 
	          	 }
	          	 else{
		          	    btn += '<a href=""  onclick="recolse('+row.formid+')"; return false;>撤销</a> &nbsp;'; 

	          		 
	          	 }
	          	    return btn; 
				}
	          	 }
     		   
		}, {
			field : 'formid',
			title : '表单号',
			width : 100,
			align:'center',
			
			
		}, 
		{
			field : 'userid',
			title : '客户ID',
			width : 100,
			align:'center'
		}, 
		{
			field : 'account_type',
			title : '客户类别',
			width : 100,
			align:'center'
		}, {
			field : 'username',
			title : '客户名称',
			width : 100,
			align:'center'
		}, {
			field : 'phone',
			title : '联系电话',
			width : 100,
			align:'center'
		},{
			field : 'balance',
			title : '余额',
			width : 100,
			align:'center'
		}, {
			field : 'sum',
			title : '审核金额',
			width : 100,
			align:'center',
			formatter:function(value,row,index){  
				if(value>0){
				    var btn="+"+value;
		          	   
	          	    
	          	 
	          	    return btn; 
				}else{
					
					return value;
					
				}
	          	 }
		}
		, {
			field : 'req_cause',
			title : '操作原因',
			width : 100,
			align:'center'
		}
		, {
			field : 'order_cs_id',
			title : '相关订单',
			width : 100,
			align:'center',
formatter:function(value,row,index){  
	var btn="";
				if(value!=0){
					 btn += '<a  href="" class="editcls" onclick=" orderview1('+row.order_cs_id+');return false;">'+row.order_cs_id+'</a> &nbsp;'; 
				}
          	    
          	   
    
          	    return btn;  }
		
		}
		, {
			field : 'remark',
			title : '详细说明',
			width : 100,
			align:'center'
		}
		, {
			field : 'status',
			title : '状态',
			width : 100,
			align:'center'
		}
		, {
			field : 'reject_cause',
			title : '驳回理由',
			width : 100,
			align:'center'
		}
		, {
			field : 'aname',
			title : '管理员',
			width : 100,
			align:'center'
		}
		] ]
			});
	
	//待我审核
	
});
	



</script>
</head>
<body onload="loaddone()">
	
	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 2px solid; padding: 5px; margin: 0px;">
		<div id="formandshenhe" class="easyui-tabs" plain="true"
			style="margin-left: 5px; height: 760px">
			<div title="我提交的">
				<div class="easyui-panel" data-options="fit:true"
					style="border: gray 2px solid; padding: 5px; margin: 0px;">
					<div class="easyui-layout" data-options="fit:true,border:true"
						style="border: red 0px solid;">
						<div data-options="region:'north',title:'',border:true"
							style="height: 100px;">
							
								<a onclick="excel();" class="easyui-linkbutton" iconCls="icon-xls" plain="true"
				style="float: right; margin-right: 5px;"><font
				style="font-weight: bold">导出Excel&nbsp;</font></a>
				

							<div style="margin-top: 19px; margin-left: 15px;">

								<label>表单状态：</label> <input type="checkbox" id="chkwait"
									name="chkwait" checked="checked" /> <label for="chkwait">待审核</label>

								<input type="checkbox" id="chkfinish" name="chkfinish"
									checked="checked" /> <label for="chkfinish">已通过</label> <input
									type="checkbox" id="chkrefuse" name="chkrefuse"
									checked="checked" /> <label for="chkrefuse">已驳回</label> <input
									type="checkbox" id="chkcancel" name="chkcancel"
									checked="checked" /> <label for="chkcancel">已关闭</label>

							</div>

							<div style="margin-top: 19px; margin-left: 15px;">
								<label>客户类别：</label> <input type="checkbox" id="chkuser"
									name="chkuser" checked="checked" /> <label for="chkuser">个人客户</label>
								<input type="checkbox" id="chkgroup" name="chkgroup"
									checked="checked" /> <label for="chkgroup">集团客户</label> <input
									type="checkbox" id="chkunit" name="chkunit" checked="checked" />
								<label for="chkunit">合作单位</label> <label for="searchType"
									style="margin-left: 20px">查找:</label> <select id="userselect"
									name="searchType" class="easyui-combobox">
									<option value="0">客户ID</option>
									<option value="1">客户名称</option>
									<option value="2">联系电话</option>
									<option value="3">金额</option>
									<option value="4">相关订单编号</option>
								</select> <input id="selectinput" type="text"
									style="width: 200px; height: 22px" class="easyui-searchbox"
									data-options="searcher:s"></input>

							</div>
						</div>
						<div data-options="region:'center',title:''"
							style="border: green 0px solid;">
							<table id="dg" style="padding: 1px; boder: black 0px solid;"
								data-options="fit:true,border:true"></table>
							<!-- 修改重提弹出窗 -->
							<div id="tb">
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
										name="balance1" readonly="readonly" /></span><br></br> <span>修改金额:<input
										id="sum1"
										style="width: 100px; border-radius: 5px; border: 1px solid #95B8E7;"
										name="sum1" /></span><br></br> <span> 操作原因:<select
										id="reqselect" name="searchType" class="easyui-combobox">
											<option value="0">线下交费充值</option>
											<option value="1">系统出错补偿</option>
											<option value="2">其他</option>
									</select></span><br></br> <span>相关订单:</span><input id="orderid"
										style="width: 100px;" name="orderid"
										data-options="required:true" class="textbox" /><a
										onclick="ordersearch()" style="width: 30px;"
										class="easyui-linkbutton" iconCls="icon-search"></a><br></br>
									<span style="color: red;">详细说明(操作原因为“其他”时，在里面输入原因):</span></br><textarea
											id="remark" cols="" rows=""
											style="margin-left: 10px; width: 330px; height: 190;"
											name="remark"></textarea>
									</br><a href="javascript:void(0)" onclick="up()"
										style="margin-left: 75px; width: 80px;"
										class="easyui-linkbutton" iconCls="icon-ok">确定</a> <a
										href="javascript:void(0)" onclick="closec()"
										style="margin-left: 10px; width: 80px;"
										class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
								</div>
								<input type="hidden" id="formid"/>
							</div>
							<div id="tb1">
								<div
									style="heigth: 900px; padding: 10px; margin-right: 10px; margin-top: 10px; margin-left: 10px; margin-button: 10px; border: 1px solid #EDF4FF;">
									<span>客户ID:<input id="usersid1"
										style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
										name="usersid1" readonly="readonly" /></span><br></br> <span>客户姓名:<input
										id="name1"
										style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
										name="name1" readonly="readonly" /></span> <br></br> <input id="type1"
										hidden="hidden"
										style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
										name="type1" readonly="readonly" /> <span>客户余额:<input
										id="balance2"
										style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
										name="balance2" readonly="readonly" /></span><br></br> <span>修改金额:<input
										id="sum2"
										style="width: 100px; border-radius: 5px; border: 1px solid #95B8E7;"
										name="sum2" /></span><br></br> <span> 操作原因:<select
										id="reqselect1" name="searchType" class="easyui-combobox">
											<option value="3">违约处罚</option>
											<option value="4">系统出错补扣</option>
											<option value="5">其他</option>
									</select></span><br></br> <span>相关订单:</span><input id="orderid1"
										style="width: 100px;" name="orderid"
										data-options="required:true" class="textbox" /><a
										onclick="ordersearch1()" style="width: 30px;"
										class="easyui-linkbutton" iconCls="icon-search"></a><br></br>
									<span style="color: red;">详细说明(操作原因为“其他”时，在里面输入原因):</span></br><textarea
											id="remark1" cols="" rows=""
											style="margin-left: 10px; width: 330px; height: 190;"
											name="remark"></textarea>
									</br><a href="javascript:void(0)" onclick="up1()"
										style="margin-left: 75px; width: 80px;"
										class="easyui-linkbutton" iconCls="icon-ok">确定</a> <a
										href="javascript:void(0)" onclick="closec1()"
										style="margin-left: 10px; width: 80px;"
										class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
								</div>
								<input type="hidden" id="formid1"/>
							</div>
						</div>
					</div>

				</div>
			</div>
			<div title="待我审核">
				<div class="easyui-panel" data-options="fit:true"
					style="border: gray 2px solid; padding: 5px; margin: 0px;">
					<div class="easyui-layout" data-options="fit:true,border:true"
						style="border: red 0px solid;">
						<div data-options="region:'north',title:'',border:true"
							style="height: 60px;">
							<div style="margin-top: 19px; margin-left: 15px;">
								<label>客户类别：</label> <input type="checkbox" id="chkuser1"
									name="chkuser1" checked="checked" /> <label for="chkuser">个人客户</label>
								<input type="checkbox" id="chkgroup1" name="chkgroup1"
									checked="checked" /> <label for="chkgroup">集团客户</label> <input
									type="checkbox" id="chkunit1" name="chkunit1" checked="checked" />
								<label for="chkunit">合作单位</label> <label for="searchType"
									style="margin-left: 20px">查找:</label> <select id="userselect1"
									name="searchType1" class="easyui-combobox">
									<option value="0">客户ID</option>
									<option value="1">客户名称</option>
									<option value="2">联系电话</option>
									<option value="3">金额</option>
									<option value="4">相关订单编号</option>
								</select> <input id="selectinput1" type="text"
									style="width: 200px; height: 21px" class="easyui-searchbox"
									data-options="searcher:s2"></input>

							</div>
						</div>

						<div data-options="region:'center',title:''"
							style="border: green 0px solid;">
							<table id="dg1" style="padding: 1px; boder: black 0px solid;"
								data-options="fit:true,border:true"></table>
							<div id="tb2">

								<div
									style="heigth: 900px; padding: 10px; margin-right: 10px; margin-top: 10px; margin-left: 10px; margin-button: 10px; border: 1px solid #EDF4FF;">
									<input id="formidunpass" hidden="hidden"
										style="width: 100px; color: #aaa; border-radius: 5px; border: 1px solid #95B8E7;"
										name="formidunpass" /> <span>驳回理由:</span><br><textarea
											id="unpasscause" cols="5" rows="5"
											style="margin-left: 10px; width: 150px; height: 150;"
											name="remark"></textarea>
									<br><a href="javascript:void(0)" onclick="unpassup()"
										style="width: 80px;" class="easyui-linkbutton"
										iconCls="icon-ok">确定驳回</a><a href="javascript:void(0)"
										onclick="closeu()" style="margin-left: 10px; width: 70px;"
										class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
								</div>


							</div>
							<div id="tb3">

								<h2 class="page-title txt-color-blueDark">

									<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp选择单个订单
								</h2>
								<HR>

									<div>
										<select id="tb3userorderselect" name="searchType"
											class="easyui-combobox">
											<option value="1">客户ID</option>
											<option value="2">客户手机</option>
										</select> <input id="tb3usersid" type="text" name="tb3usersid" /> <label
											style="margin-left: 20px;">客户姓名：</label><input
											id="tb3orderusername" name="tb3orderusername" type="text" />
									</div>
									<div style="margin-top: 10px;">
										成交时间：</label><input id="tb3date1" type="text" class="easyui-datebox">--<input
											id="tb3date2" type="text" class="easyui-datebox"> <label
												style="margin-left: 20px;">客户身份：</label> <input
												type="checkbox" id="tb3chkusertype" name="tb3chkusertype"
												checked="checked" /> <label>乘客</label> <input
												type="checkbox" id="tb3chkusertype1" name="tb3chkusertype1"
												checked="checked" /> <label>车主</label>
									</div>
									<div style="margin-top: 10px;">
										<label> 订单类型：</label> <input type="checkbox"
											id="tb3ordertype1" name="tb3ordertype1" checked="checked" />
										<label>单次拼车</label> <input type="checkbox" id="tb3ordertype2"
											name="tb3ordertype2" checked="checked" /> <label>上下班拼车</label>
										<input type="checkbox" id="tb3ordertype3" name="tb3ordertype3"
											checked="checked" /> <label>长途拼车</label>
									</div>
									<div style="margin-top: 10px;">
										<label> 订单状态：</label> <input type="checkbox"
											id="tb3orderstatus1" name="tb3orderstatus1" checked="checked" />
										<label>已完成</label> <input type="checkbox" id="tb3orderstatus2"
											name="tb3orderstatus2" checked="checked" /> <label>未完成</label>
									</div>
									<div style="margin-top: 10px;">
										<label>订单编号</label><input type="text" id="tb3ordercsid">
											<a onclick="ordersearchtb3()"
											style="width: 80px; margin-left: 330px;"
											class="easyui-linkbutton" iconCls="icon-search">查询</a>
									</div>
									<HR>
										<div style="margin-left: 10px;">
											<table id="tb3dg"></table>
										</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
</body>
</html>
