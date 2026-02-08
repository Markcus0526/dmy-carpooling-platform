<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"financial/account/";
System.out.println(basePath);
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>

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

<style type="text/css">
</style>

</head>
<body>
	<script type="text/javascript" charset="UTF-8">
//查询方法
function searchview(){
	
	
	var insert=$("#inp").get(0).checked == true ? 1 : 0;
	var out=$("#out").get(0).checked == true ? 1 : 0;
	var pinche=$("#pin").get(0).checked == true ? 1 : 0;
	var daijia=$("#dai").get(0).checked == true ? 1 : 0;
	var viewbegintime = ($("#begintime").datebox("getValue")=="")?0:$("#begintime").datebox("getValue");
	var viewendtime = ($("#endtime").datebox("getValue")=="")?0:$("#endtime").datebox("getValue");
	var viewtsid = ($("#tsid").val()=="")?0:$("#tsid").val();
	var vieworder_cs_id = ($("#orderinput").val()=="")?0:$("#orderinput").val();
	var tstype = $("#accounttypes").combobox("getValue");

	$('#dg').datagrid('load',{
		insert :insert,
		out:out,
		pinche:pinche,
		daijia:daijia,
		viewuserid1:$('#userid').val(),
    	viewaccounttype1:$('#accounttype1').val(),	
		viewbegintime:viewbegintime,
		viewendtime:viewendtime,
		viewtsid:viewtsid,
		vieworder_cs_id :vieworder_cs_id ,
		tstype:tstype,

				});		
}
function orderview(ordercsid){

	path='<%=basePath%>';
		window.open(path+'info.do?orderid1='+ordercsid,"_blank");
	
}

$(function(){
	 path='<%=basePath%>';
	var userid=${viewuserid};
	var balance=${viewbalance};
	var phone=${viewphone};
	var accounttype=${viewuaccounttype};
	var accounttype1=${viewuaccounttype};
	if(accounttype==1){
		accounttype="个人客户";
	}
	if(accounttype==2){
		accounttyp="集团客户";
		
	}
	if(accounttype==3){
		accounttype="合作单位";
	}
	$('#userid').val(userid);
	$('#balance').val(balance);
	$('#phone').val(phone);
	$('#accounttype').val(accounttype);
	$('#accounttype1').val(accounttype1);
	 path='<%=basePath%>';
	 $.ajax({
		 url :path+'findusername.do?userid='+userid,
		success:function(data){
			 $('#username').val(data.username);		
		}
	 });
	
	//
	
	 
	 $('#dg').datagrid({
		 
		
		    url:'<%=basePath%>findview.do', 
		    
		    queryParams:{	    	  
		    	viewuserid1:$('#userid').val(),
		    	viewaccounttype1:$('#accounttype1').val(),	
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
			columns : [ [ {
				field : 'userid',
				title : '客户id',
				width : 100,
				align:'center'
			}, 
			{
				field : 'account_type',
				title : '客户类型',
				width : 100,
				align:'center',
				formatter:function(value,row,index){  
					if(value==1){
						  return '个人客户';
					} if(value==2) {
						 return '集团客户';
					}else{
						return '合作单位';
					}	
					},
			}, 
			{
				field : 'order_cs_id',
				title : '相关订单',
				width : 100,
				align:'center',
					formatter:function(value,row,index){  
						
		          	    var btn="";
						if(value==0){
							btn="";
						}else{
							 btn += '<a href="" class="editcls" onclick="orderview('+row.order_cs_id+');return false">'+row.order_cs_id+'</a>'; 
						}
					
		          	   
		          	   
		          	    return btn;  }
	     		   
			
			}, 
			{
				field : 'ts_id',
				title : '交易编号',
				width : 100,
				align:'center'
			}, {
				field : 'date',
				title : '操作日期',
				width : 100,
				align:'center'
			}, {
				field : 'req_source',
				title : '来源',
				width : 100,
				align:'center',
				formatter:function(value,row,index){  
					if(value==1){
						  return '手机端';
					} if(value==2){
						 return '用户网站';
					}
					 if(value==3){
						 return '管理后台';
					}
						
					
					},
			
			},{
				field : 'oper',
				title : '进出账',
				width : 100,
				align:'center',
				formatter:function(value,row,index){  
				if(value==2){
					  return '进账';
				} if(value==1){
					 return '出账';
				}
					
				
				},
			}, {
				field : 'ts_type',
				title : '账目类型',
				width : 100,
				align:'center',
				formatter:function(value,row,index){ 
		       		  if(value=="tx_code_001"){
				        	  return '注册新用户';
				          } if(value=="tx_code_002"){
				        	  return '返还绿点';
				          } if(value=="tx_code_003"){
				        	  return '乘客扣款';
				          } if(value=="tx_code_004"){
				        	  return '平台信息费';
				          } if(value=="tx_code_005"){
				        	  return '分成';
				          } if(value=="tx_code_006"){
				        	  return '车主收入';
				          } if(value=="tx_code_007"){
				        	  return '个人充值';
				          } if(value=="tx_code_008"){
				        	  return '个人提现';
				          } if(value=="tx_code_009"){
				        	  return '冻结绿点';
				          }
				          if(value=="tx_code_010"){
				        	  return '解冻绿点';
				          }
				          if(value=="tx_code_011"){
				        	  return '充值';
				          }
				          if(value=="tx_code_012"){
				        	  return '提现';
				          }
				          if(value=="tx_code_013"){
				        	  return '点卷费用';
				          }
				          if(value=="tx_code_014"){
				        	  return '车主邀请费';
				          }
				          if(value=="tx_code_015"){
				        	  return '乘客邀请费';
				          }
				          if(value=="tx_code_016"){
				        	  return '游戏发送绿点';
				          }
				        
				         
		       	 }
			}
			, {
				field : 'sum',
				title : '审核金额',
				width : 100,
				align:'center',
			}
			, {
				field : 'balance',
				title : '绿点余额',
				width : 100,
				align:'center',
			}
			, {
				field : 'remark',
				title : '备注说明',
				width : 100,
				align:'center',
			}
			
			] ]
				});
		
	
	
	
	
});
</script>


	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 2px solid; padding: 5px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'客户账户管理',border:true"
				style="height: 190px;">
				<div style="margin-top: 5px">
					<label>客 &nbsp;户 &nbsp;i&nbsp;d：</label><input id="userid"
						type="text" class="textbox" /><label style="margin-left: 20px">
						客户名称：</label><input id="username" type="text" class="textbox" />
				</div>
				<div style="margin-top: 7px">
					<label>客户电话：</label><input id="phone" type="text" class="textbox" /><label
						style="margin-left: 23px">客户类型：</label><input id="accounttype"
						type="text" class="textbox" /><input id="accounttype1" type="text"
						hidden="hidden" /> <label style="margin-left: 20px">余额：</label><input
						id="balance" type="text" class="textbox" />
				</div>
				<div style="margin-top: 7px">
					<label>交易时间：</label><input id="begintime" type="text"
						class="easyui-datebox"></input>--<input id="endtime" type="text"
						class="easyui-datebox"></input> <label>进出账：</label> <input
						type="checkbox" id="inp" name="inp" checked="checked" /> <label
						for="chkuser">进账</label> <input type="checkbox" id="out"
						name="out" checked="checked" /> <label for="chkgroup">出账</label> <label>业务来源：</label>
					<input type="checkbox" id="pin" name="pin" checked="checked" /> <label
						for="chkuser">拼车</label> <input type="checkbox" id="dai"
						name="dai" checked="checked" /> <label for="chkgroup">代驾（保留）</label>
				</div>
				<div style="margin-top: 7px">
					<label>订单编号：</label><input id="orderinput" type="text"
						class="textbox" /> <label>交易编号：</label><input id="tsid"
						type="text" class="textbox" />
				</div>
				<div style="margin-top: 7px">
					<label>账目类型：</label> <select id="accounttypes" name="accounttypes"
						class="easyui-combobox" style="width: 100px;">
						<option value="0">-- - -全部- - --</option>
						<option value="1">注册新用户</option>
						<option value="2">返还绿点</option>
						<option value="3">乘客扣款</option>
						<option value="4">平台信息费</option>
						<option value="5">分成</option>
						<option value="6">车主收入</option>
						<option value="7">个人充值</option>
						<option value="8">个人提现</option>
						<option value="9">冻结绿点</option>
						<option value="10">解冻绿点</option>
						<option value="11">充值</option>
						<option value="12">提现</option>
						<option value="13">点券费用</option>
						<option value="14">车主邀请费</option>
						<option value="15">乘客邀请费</option>
						<option value="16">游戏发送绿点</option>
						
					</select> <a onclick="searchview()" style="margin-left: 600px; width: 80px;"
						class="easyui-linkbutton" iconCls="icon-search">查 询</a> <a
						href="<%=basePath%>index.do"
						style="margin-left: 30px; width: 100px;">返回上一页</a><br /> <label>点击执行订单编号可查看详情</label>
				</div>
			</div>

			<div data-options="region:'center',title:''"
				style="border: #95B8E7 1px solid;">

				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>

	</div>

</body>
</html>
