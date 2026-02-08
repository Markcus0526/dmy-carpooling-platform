<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/" + "financial/request/";
	//System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>

<script type="text/javascript"src="${pageContext.request.contextPath}/js/jquery.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/themes/icon.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/themes/default/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/themes/default/combobox.css" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css" href="js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css" href="js/themes/icon.css" />
<style type="text/css">
</style>

<script type="text/javascript" charset="UTF-8">
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
function searchcherge(){

	
	var cherge=$("#cherge").get(0).checked == true ? 1 : 0;
	var deposit=$("#deposit").get(0).checked == true ? 1 : 0;
	var notclose= $("input[name='close']:checked").val();
	var all=$("input[name='all']:checked").val();
	var userid=($("#inputid").val()=="")?0:$("#inputid").val();//
	var username=($("#username").val()=="")?0:$("#username").val();//
	var phone=($("#phone").val()=="")?0:$("#phone").val();//
	var begintime = ($("#begintime").datebox("getValue")=="")?0:$("#begintime").datebox("getValue");
	var endtime = ($("#endtime").datebox("getValue")=="")?0:$("#endtime").datebox("getValue");


	$('#dg').datagrid('load',{
		cherge :cherge,
		deposit:deposit,
		
		notclose:notclose,
		
		all:all,
		userid:userid,
		
		username:username,
		begintime:begintime,
		
		endtime:endtime,
		phone:phone,

				});		
	
	
	
}
//时间格式转换
/*  function   gettime(time2){
	  var JsonDateValue = new Date(time2.time);
	  var text = JsonDateValue.toLocaleString(); 
	             return text;
	         }
 */
$(function() {
	var notclose= $("input[name='close']:checked").val();
			 $('#dg').datagrid({
				 
						queryParams:{	    	  
								cherge:$("#cherge").get(0).checked == true ? 1 : 0,
								deposit:$("#deposit").get(0).checked == true ? 1 : 0,
										notclose:notclose,
								
				   				},
							url :'<%=basePath%>findby.do',
							loadMsg : '数据处理中，请稍后....',
							height : 'auto',
							fitColumns : true,
							pagination : true,
							rownumbers:true,
							method:'post',
							toolbar: '#toolbar',
							columns : [ [
									{
										field : 'ck',
										checkbox : true
									},
									{
										field : 'reqid',
										title : 'id',
										width : 0,
										hidden:true,
										align : 'center',
									},
									{
										field : 'operation',
										title : '操作',
										width : 250,
										align : 'center',
										formatter : function(value, row, index) {

											var btn = "";
											if (row.status ==1&&row.oper_type==1) {
												btn += '<a href="" class="editcls" onclick="dealsingle('+row.reqid+');return false">处理</a>';
											}
											
											if (row.status ==5&&row.oper_type==1) {
												btn += '<a href="" class="editcls" onclick="finishsingle('+row.reqid+','+row.ts_id+','+row.oper_type+','+row.user_id+','+row.account_type+','+row.sum+');return false">完成</a>|';
												btn += '<a href="" class="editcls" onclick="closesingle('+row.reqid+','+row.ts_id+','+row.oper_type+','+row.user_id+','+row.account_type+','+row.sum+');return false">关闭</a>';
												
											}
									
			
											return btn;
										}

									},
									{
										field : 'account_type',
										title : '客户类型',
										width : 200,
										align : 'center',
										formatter : function(value, row, index) {
											
											if (value == 1) {
												return '个人客户';
											}
											if (value == 2) {
												return '集团客户';
											}
											if (value == 3) {
												return '合作单位';
											}
										}
									},
									{
										field : 'user_id',
										title : '客户ID',
										width : 200,
										align : 'center'
									},
									{
										field : 'username',
										title : '客户名称',
										width : 200,
										align : 'center'
									},
									{
										field : 'phone',
										title : '联系电话',
										width : 200,
										align : 'center'
									},
									{
										field : 'oper_type',
										title : '操作类型',
										width : 200,
										align : 'center',
										formatter : function(value, row, index) {
											if (value == 1) {
												return '提现';
											}
											if (value == 2) {
												return '充值';
											}
											
										}
									},
									{
										field : 'account',
										title : '客户操作账户',
										width : 200,
										align : 'center'
									},
									{
										field : 'channel',
										title : '渠道',
										width : 200,
										align : 'center'
									},
									{
										field : 'remark',
										title : '开户支行',
										width : 200,
										align : 'center'
									},
									{
										field : 'sum',
										title : '操作金额',
										width : 200,
										align : 'center'
									},
									{
										field : 'req_date',
										title : '操作日期',
										width : 250,
										align : 'center',
										  
									},
									{
										field : 'status',
										title : '状态',
										width : 250,
										align : 'center',
										formatter : function(value, row, index) {
											if (value == 1) {
												return '待处理';
											}
											if (value == 2) {
												return '已处理';
												
											}
											if (value == 3) {
												return '已驳回';
											}
											if (value == 4) {
												return '关闭';
											}
											if (value == 5) {
												return '处理中';
												
											}
											
										}
									}, {
										field : 'auditor',
										title : '处理人',
										width : 250,
										align : 'center'
									} ,
									{
										field : 'ts_id',
										title : 'tsid',
										width : 250,
										align : 'center',
										//hidden:'true',
									} ] ],

						});

	});
	//处理
	function dealsingle(reqid){
		 if(confirm('确定处理？')){
				$.ajax({
					 url :'<%=basePath%>dealsingle.do?reqid='+reqid,
					 method:'post',
						dataType : "json",
					success:function(data){
						
						$('#dg').datagrid('reload');
					}
				 });
			 
			 }
	
	}
	//批量处理
	function deal(){
		
	
		var checked = $('#dg').datagrid('getChecked');
		
		var reqidstr=[];
		var str=$('#receiver').val();
		$.each(checked, function(index, checked){
			if(checked.status==1){
				
				reqidstr.push(checked.reqid);
				reqidstr.join(",");
				
			}
		
		
			}); 	
		str+=reqidstr;
			$('#receiver').val(str);
			
			 if($('#receiver').val()==""){
				 $.messager.alert('警告','请选择至少一个符合条件的选项！！！','warning'); 
				 
			 }else{		 
				 $.messager.confirm('提示信息','确定处理？',function(r){   
					    if (r){   
					   	 if($('#receiver').val()!=""){
							 $.ajax({
								 url :'<%=basePath%>deal.do?reqidstr='+str,
								 method:'post',
									dataType : "json",
								success:function(data){
									$('#receiver').val("");
									$('#dg').datagrid('reload');
								}
							 }); 
						 }
						
					    }   
					}); 
				 
			 }
		
	}
	
	//完成
	function finishsingle(reqid,tsid,oper_type,uid,uaccounttype,sum){
	
		 $.messager.confirm('提示信息','确定完成？',function(r){   
			    if (r){   
			    	$.ajax({
						 url :'<%=basePath%>finishsingle.do?reqid='+reqid+'&&clouserid='+uid+'&&cloaccounttype='+uaccounttype+'&&utsid='+tsid+'&&sum='+sum,
						 method:'post',
							dataType : "json",
						success:function(data){
							
							$('#dg').datagrid('reload');
						}
					 });
			    }   
			});  

	}
	//批量完成
function finish(){
	var checked = $('#dg').datagrid('getChecked');
	
	var reqidstr=[];
	var reqtsidstr=[];//存档前tsid
	var requidstr=[];//存当前的userid
	var reqsumstr=[];
	
	var str=$('#receiver').val();
	var tsidstr=$('#tsids').val();
	var uidstr=$('#uids').val();
	var sumstr=$('#sums').val();
	$.each(checked, function(index, checked){
		if(checked.status==5){
			
			reqidstr.push(checked.reqid);
			reqidstr.join(",");
			reqtsidstr.push(checked.ts_id);
			reqtsidstr.join(",");
			requidstr.push(checked.user_id);
			requidstr.join(",");
			reqsumstr.push(checked.sum);
			reqsumstr.join(",");
			
		}
	
	
		}); 	
	str+=reqidstr;
	tsidstr+=reqtsidstr;
	uidstr+=requidstr;
	sumstr+=reqsumstr;
		$('#receiver').val(str);
		$('#tsids').val(tsidstr);
		$('#uids').val(uidstr);
		$('#sums').val(sumstr);
		
		 if($('#receiver').val()==""){
			 $.messager.alert('警告','请选择至少一个符合条件的选项！！！','warning'); 

		 }else{
		 $.messager.confirm('提示信息','确定完成？',function(r){   
			    if (r){   
			    	 if($('#receiver').val()!=""){
						 $.ajax({
							 url :'<%=basePath%>finish.do?reqidstr='+str+'&&sumstr='+sumstr+'&&clouseridstr='+uidstr+'&&utsidstr='+tsidstr,
							 method:'post',
								dataType : "json",
							success:function(data){
								$('#receiver').val("");
								$('#dg').datagrid('reload');
							}
						 }); 
					 } 
			    }   
			});  
		 }
			 
		 
		
	}
	
	//关闭
		function closesingle(reqid,tsid,oper_type,uid,uaccounttype,sum){
	
		 if(confirm('确定关闭？')){
				$.ajax({
					 url :'<%=basePath%>closesingle.do?reqid='+reqid+'&&clouserid='+uid+'&&cloaccounttype='+uaccounttype+'&&utsid='+tsid+'&&sum='+sum,
					 method:'post',
						dataType : "json",
					success:function(data){
						
						$('#dg').datagrid('reload');
					}
				 });
			 
			 }
	
	}
	
//
function outxlx(){
	
}
	
</script>

</head>
<body onload="loaddone()">


	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 2px solid; padding: 5px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'充值提现管理',border:true"
				style="height: 150px;">
				<div style="margin-top: 10px">
					<label>客户id：</label><input id="inputid" type="text" class="textbox"
						style="height: 21px" /><label style="margin-left: 40px">客户名称：</label><input
						id="username" type="text" class="textbox" style="height: 21px" />
					<label style="margin-left: 40px">联系电话：</label><input id="phone"
						type="text" class="textbox" style="height: 21px" />

				</div>
				<div style="margin-top: 10px">
					<label>操作类型：</label> <input type="checkbox" id="cherge"
						name="cherge" checked="checked" />
					<lable>充值</label> <input type="checkbox" id="deposit"
						name="deposit" checked="checked" /> <label>提现</label> <label
						style="margin-left: 35px">操作时间：</label>
					<input id="begintime" type="text" class="easyui-datebox"></input>--<input
						id="endtime" type="text" class="easyui-datebox"></input>
				</div>
				<div style="margin-top: 10px">
					<label>显示类型：</label><input id="close" type="radio" name="close"
						value=1 checked><lable>未关闭的</lable> <input id="all" type="radio"
						name="close" value=2> <lable>所有的</lable> <a
							onclick="searchcherge()" style="width: 80px; margin-left: 400px"
							class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</div>

				<input type="text" id="receiver" name="receiver" hidden="hidden"></input>
				<input type="text" id="tsids" name="tsids" hidden="hidden"></input>
				<input type="text" id="uids" name="uids" hidden="hidden"></input> <input
					type="text" id="sums" name="sums" hidden="hidden"></input>

			</div>
			<div id="toolbar">
				<a onclick="deal()"
					style="border: green 1px solid; width: 80px; margin-left: 700px"
					data-options="iconCls:'icon-edit'" class="easyui-linkbutton">批量处理</a>
				<a onclick="finish()"
					style="border: green 1px solid; width: 80px; margin-left: 5px"
					class="easyui-linkbutton" data-options="iconCls:'icon-ok'">批量完成</a>
				<a onclick="outxlx()"
					style="border: green 1px solid; width: 100px; margin-left: 5px"
					class="easyui-linkbutton" iconCls="icon-xls">导出到excel</a>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>




</body>
</html>