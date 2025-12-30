<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/"+"operation/notify/";
	System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
	<h2 class="page-title txt-color-blueDark" style="margin-left: 5px;">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;通知管理

	</h2>
	<HR style="margin-left: 5px"></HR>

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
</head>
<body onload="loaddone()">
	<script type="text/javascript" charset="UTF-8">
	

function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
/**
 * 按注册或登录城市查询 选择所触发的
 */
function s(){
		
	//这里是查询
    var cityselect = $('#cityandre').combobox('getValue');
    var cityinput = $('#cityandre1').val();
    var groupselect = $('#group').combobox('getValue');
    var groupinput = $('#groupinput').val();
   // var yewuselect = $('#yewuselect').combobox('getValue'); 保留功能 揭开注释即可使用
    var userselect = $('#uerselect').combobox('getValue');
    var userinput = $('#userinput').val();
    var khsfselect = $('#khsfselect').combobox('getValue');
	//$('#dg').datagrid({"onLoadSuccess":function(data){ 
		/// var options = $('#dg').datagrid('getPager').data("pagination").options;  
		   /// var totalRowNum = options.total;
			//$('#totle').val(totalRowNum); 
			//}}).
		var reg_date=$("#reg_date").datebox("getValue");
		var reg_date1=$("#reg_date1").datebox("getValue");
		var last_login_time = $("#last_login_time").datebox("getValue");
		var last_login_time1 = $("#last_login_time1").datebox("getValue");

	$('#dg').datagrid('load',{
    	cityselect :cityselect,
    	cityinput:cityinput,
    	groupselect:groupselect,
    	groupinput:groupinput,
    	//yewuselect:yewuselect, 保留
    	userselect:userselect,
    	userinput:userinput,
    	khsfselect:khsfselect,
    	reg_date:reg_date,
    	reg_date1:reg_date1,
    	last_login_time:last_login_time,
    	last_login_time1:last_login_time1,
			});
}
/**
 * 发送选择的确定
 **/
function send(){

	var checked = $('#dg').datagrid('getChecked');
	var len = checked.length;
	var userid=[];
	var str=$('#receiver').val();
	var a =[];
	$.each(checked, function(index, checked){
		userid.push(checked.userid);
		userid.join(",");
		}); 
	if (confirm("你确定选择这"+len+"个用户么？")) {
		str+=userid+",";
		a = str.split(",");
		$('#receiver').val(str);
		$('#sum').val(a.length-1);
	}
	
	console.log(userid.join(","));
}

$(function(){
	
	$('#dg').datagrid({   
	    url:'<%=basePath%>finduser.do',
				method : 'post',
				pageNumber : 1,
				iconCls : 'icon-show',
				pagination : true,
				pageSize : 5,
				pageList : [5,10, 20 ],
				nowrap : false,
				idFiled : 'id',
				rownumbers : true,
				title:"客户信息表",
				fitColumns:true,
				border : true,
				checkOnSelect : true,
				striped : true,
				onLoadSuccess:function(jsonObject){
                    $('#total').val(jsonObject.total);
                    
				},
				columns : [ [ {
					title : '选择',
					field : 'check',
					checkbox : true,
					width : 50
				}, {
					field : 'userid',
					title : '客户ID',
					width : 100,
					align:'center',
				}, {
					field : 'usercode',
					title : '客户账号',
					width : 100,
					align:'center',
				}, {
					field : 'username',
					title : '客户姓名',
					width : 100,
					align : 'right',
					align:'center',
				}, {
					field : 'phone',
					title : '客户手机',
					width : 100,
					align:'center',
				}, {
					field : 'driver_verified',
					title : '客户身份',
					width : 100,
					align:'center',
				}, {
					field : 'group_name',
					title : '所属集团',
					width : 100,
					align:'center',
				}, {
					field : 'reg_date',
					title : '注册时间',
					width : 100,
					align:'center',
				}, {
					field : 'last_login_time',
					title : '最后登录时间',
					width : 100,
					align:'center',
				} ] ]

			});

		});
		/**
		 *发送给全部用户
		 **/
		function submit1() {
	
			var title = $('#title').val();
			var msg = $('#msg').val();
			$('#citys').val($('#cityandre').combobox('getValue'));
			$('#cityi').val($('#cityandre1').val());
			$('#users').val($('#uerselect').combobox('getValue'));
			$('#useri').val($('#userinput').val());
			$('#groups').val($('#group').combobox('getValue'));
			$('#groupi').val($('#groupinput').val());
			$('#khs').val($('#khsfselect').combobox('getValue'));
			$('#t1b').val($("#reg_date").datebox("getValue"));
			$('#t1e').val($("#reg_date1").datebox("getValue"));
			$('#t2b').val($("#last_login_time").datebox("getValue"));
			$('#t2e').val($("#last_login_time1").datebox("getValue"));
			var citys = $('#citys').val();
			var cityi = $('#cityi').val();
			var users = $('#users').val();
			var useri = $('#useri').val();
			var groups = $('#groups').val();
			var froupi = $('#groupi').val();
			var khs = $('#khs').val();
			var t1b = $('#t1b').val();
			var t1e = $('#t1e').val();
			var t2b = $('#t2b').val();
			var t2e = $('#t2e').val();
			if(title!=null&&""!=title&&msg!=null&&""!=msg){
				 $.messager.confirm('提示信息','你确认发送吗？',function(r){   
					    if (r){   
					    	$.ajax({
								url :'<%=basePath%>save.do',
								/*
									private String cityi;
									private String groups;//选择输入集团信息
									private String groupi;
									private String users;//选择输入客户信息
									private String useri;
									private String khs;//客户身份
									private String t1b;//注册查询开始时间
									private String t1e;//注册查询结束时间
									private String t2b;//最后登陆时间查询开始时间
									private String t2e;//最后登陆时间查询结束时间
								
								*/
								data:'title='+title+'&&msg='+msg+'&&citys='+citys+'&&cityi='
								+cityi+'&&users='+users+'&&useri='+useri+
								'&&groups='+groups+'&&groupi='+groupi+'&&khs='+khs+'&&t1b='+t1b+'&&t1e='+t1e+'&&t2b='+t2b+'&&t2e'+t2e,
								
									dataType : "json",
									method:'post',
									success : function(jsonObject) {
										if(jsonObject.ok==1){
											alert("发送成功！！！");
											
										}
										
										
										
								
											
									}
								});
					    	
					    }
					    
					    
					}); 
				
			}
			else{
				
				alert("标题或内容不能为空");
			}
			

		}
		/**
		 *发送给勾选用户
		 **/
		function submit2() {
		var sum = $('#sum').val();
		var receiver = $('#receiver').val();
		if(receiver==""||sum==0){
			alert("请选择要发送的用户");
		}
		else{
			var title = $('#title').val();
			var msg = $('#msg').val();
			
			if(title!=null&&""!=title&&msg!=null&&""!=msg){
				 $.messager.confirm('提示信息','你确认发送吗？',function(r){   
					    if (r){   
					    	$.ajax({
								url :'<%=basePath%>save.do',
								data:'title='+title+'&&msg='+msg+'&&receiver='+receiver,
									dataType : "json",
									method:'post',
									success : function(jsonObject) {
										if(jsonObject.ok==1){
											alert("发送成功！！！");
											
										}
										
								
											
									}
								});
				
					    }   
					}); 
				
			}
			else{
				
				alert("标题或内容不能为空");
			}
				
			
		}
			

			

		}
		//验证方法
	

	</script>

	<div style="margin-left: 30px;">
		<form id="ff"
			style="margin-left: 5px;">
			<table>
				<tr>
					<td>通知标题：</td>
					<td><input type="text" id="title" name="title" class="textbox" style="height:21px"></input></td>
				</tr>
				<tr>
					<td>通知内容：</td>
					<td><textarea id="msg" cols="50" rows="6"
							style="width: 1000; height: 200;" name="msg" class="textbox"></textarea></td>
				</tr>
				<tr>
					<td><input type="text" id="receiver" name="receiver"
						style="display: none;"></input>
						<input type="text" id="citys" name="citys"
						style="display: none;"></input>
						<input type="text" id="cityi" name="cityi"
						style="display: none;"></input>
						<input type="text" id="users" name="users"
						style="display: none;"></input>
						<input type="text" id="useri" name="useri"
						style="display: none;"></input>
						<input type="text" id="groupss" name="groups"
						style="display: none;"></input>
						<input type="text" id="groupi" name="groupi"
						style="display: none;"></input>
						<input type="text" id="khs" name="khs"
						style="display: none;"></input>
						<input type="text" id="t1b" name="t1b"
						style="display: none;"></input>
						<input type="text" id="t1e" name="t1e"
						style="display: none;"></input>
						<input type="text" id="t2b" name="t2b"
						style="display: none;"></input>
						<input type="text" id="t2e" name="t2e"
						style="display: none;"></input>
						
						
						
						</td>
					

				</tr>
				
				<tr>
					<td></td>
					<td></td>
					<td><input id="b1" type="button" value="发送给勾选用户"
						onclick="submit2()"
						style="margin-left: 40px; width: 30; height: 20;"> </input></td>
					<td><input id="b2" type="button" value="发送给所有用户"
						onclick="submit1()"
						style="margin-left: 25px; width: 30; height: 20;" /></td>
				</tr>
			</table>
			<div>
		发送人数：<input type="text" id="sum" name="sum" class="textbox" style="height:21px;margin-left:15px;"></input>
		</div>
	</form>
		
	</div>
	<h4 class="page-title txt-color-blueDark" style="margin-left: 5px;">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;选择要发送的用户
	</h4>
	<HR style="margin-left: 5px;">
	
	
		<table style="margin-left: 80px;">
			<tr>
				<td><select id="cityandre" name="cityselect"
					class="easyui-combobox" style="width: 150px"/>
						<option value="1">注册城市</option>
						<option value="2">最后登录城市</option>
						<option value="3">注册或最后登录城市</option>
				</select></td>
				<td><input type="text" id="cityandre1" name="cityinput" class="textbox" style="height:21px"></input></td>
				<td></td>
				<td></td>
				<td><select id="group" name="groupselect"
					class="easyui-combobox" style="width: 150px;">
						<option value="1">按集团/联盟标识查询</option>
						<option value="2">按集团/联盟名称查询</option>
				</select></td>
				<td><input type="text" id="groupinput" name="groupinput" class="textbox" style="height:21px"></input>
				</td>
			</tr>
			<tr></tr>

			<tr>
				<td><select id="uerselect" name="uerselect"
					class="easyui-combobox" style="width: 150px;">
						<option value="1">按客户ID查询</option>
						<option value="2">按客户手机查询</option>
						<option value="3">按客户姓名查询</option>
				</select></td>
				<td><input type="text" id="userinput" name="userinput" class="textbox" style="height:21px"></input>
				</td>
				<td></td>
				<td></td>
				<td><span id="usersf"
					style="font-size: 14px; margin-left: 15px;">客户身份:</span> <select
					id="khsfselect" name="khsfselect" class="easyui-combobox"
					style="width: 70px;">
					<option value="#" select>全部</option>
						<option value="1">车主</option>
						<option value="0">乘客</option>
				</select></td>
			</tr>
		</table>

		<div style="margin-left: 80px;">
			用户注册日期：<input id="reg_date" type="text" class="easyui-datebox"></input>--<input
				id="reg_date1" type="text" class="easyui-datebox"></input></br> 最后登录日期：<input
				id="last_login_time" type="text" class="easyui-datebox"></input>--<input
				id="last_login_time1" type="text" class="easyui-datebox"></input> <a
				onclick="s()" style="margin-left: 200px; width: 80px;"
				class="easyui-linkbutton" iconCls="icon-search">查 询</a> <a
				onclick="send()" style="margin-left: 30px; width: 80px;"
				class="easyui-linkbutton" iconCls="icon-ok">确定选择</a> <br>
				符合条件的用户数：<input id="total" type="text" style="margin-left: 15px" class="textbox" style="height:21px"></input>
		</div>

		<HR style="margin-left: 5px;">
			<table id="dg" style="margin-left: 60px;"></table>
</body>

</html>