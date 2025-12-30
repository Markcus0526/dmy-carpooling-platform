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
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/icon.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>  	  
<script type="text/javascript" src='${pageContext.request.contextPath}/js/outlook2.js'> </script>  --%>

<link rel="stylesheet" type="text/css"
	href="<%=path %>/js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path %>/js/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path %>/js/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path %>/js/themes/default/tree.css" />
<link rel="stylesheet" type="text/css"
	href="<%=path %>/js/themes/icon.css" />
<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=path %>/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>

<script type="text/javascript" charset="UTF-8">
	
        var num=0;
	function loaddone(){
		var hiddenmsg = parent.document.getElementById("hiddenmsg"); 
		hiddenmsg.style.display="none";
	}
	
 	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
	} 
	
	$(function(){
		$('#tb_select').datagrid({
			url: '<%=basePath%>spread/loan/searchUser.do',
		    loadMsg : '数据处理中，请稍后....',
			fitColumns: true,
			pagination : true,
			remoteSort : false,
			striped : true,
			rownumbers:true,
			selectOnCheck : true,
			/* checkOnSelect : true, */
			columns:[[
	             {field:'ck', checkbox:true },
	             {field:'id',width:15,align:'center',title:'客户ID'},
	             {field:'username',width:20,align:'center',title:'客户姓名'},
	             {field:'phone',width:20,align:'center',title:'客户手机号码'},
	             {field:'group_name',width:30,align:'center',title:'所属集团'},
	             {field:'reg_date',width:30,align:'center',title:'注册时间',
	            	formatter: function(value,row,index){
	            		//console.info("value : "+ value);
						var h = value.hours;
						var M = value.minutes;
						var s = value.seconds;
						var y = value.year;
						if (y < 1900)
							y += 1900;
						var m = value.month+1;
						var d = value.date;
						return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
					
	            	 }
				 },{field:'last_login_time',width:30, align:'center', title:'最后登录',
					 formatter: function(value,row,index){
							var h = value.hours;
							var M = value.minutes;
							var s = value.seconds;
							var y = value.year;
							if (y < 1900)
								y += 1900;
							var m = value.month+1;
							var d = value.date;
							return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
						
	            	 } 
	             }
	         ]],
/* 	        onLoadSuccess : function(ret) {
				$('#total_size').text(ret.total);
			}, */
			onCheck : function(index,row){
				
				var listid=$('#idList').val();
				var id =row.id;
				listid+=id+'';
				listid+=",";
				alert(listid);
				$('#idList').val(listid);
				 num=num+1;
				$('#num').val(num);
			},
			onUncheck : function(index,row){
				var listid=$('#idList').val();
				var id =row.id;
				var list=listid.split(id+",",1000);
				listid=list[0]+list[1];
				$('#idList').val(listid);
				  num=num-1;
				$('#num').val(num);
				alert(listid);
			},
			onCheckAll : function(rows){
				var listid="";
				num=0;
				$.each(rows, function(index, rows){
					listid+=rows.id+",";
					num+=1;
					}); 
				$('#idList').val(listid);
				$('#num').val(num);
				alert(listid);
			},
			onUncheckAll : function(rows){
				num=0;
				$('#num').val(num);
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
		
	
		
		//-----下拉框 ----
		$('#cityItemSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'city_register',
			data : [ {
				label : '注册城市',
				value : 'city_register'
			}, {
				label : '最后登录城市',
				value : 'city_cur'
			}, {
				label : '注册/最后登录城市',
				value : 'register_or_cur'
			} ]

		});
		
		
		$('#groupItemSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'group_or_asso_id',
			data : [ {
				label : '集团标识',
				value : 'group_or_asso_id'
			}, {
				label : '集团名称',
				value : 'group_or_asso_name'
			} ]

		});
		
		
		 $('#userItemSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'username',
			data : [{
				label : '按姓名查询',
				value : 'username'
			}, {
				label : '按手机号查询',
				value : 'phone'
			}, {
				label : '按id查询',
				value : 'id'
			} ]

		});
		 $('#userType').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'driver',
			data : [{
				label : '车主',
				value : 'driver'
			}, {
				label : '乘客',
				value : 'passenger'
			}]

		});
		
		
	});	

	function aaa(){
	    var cityItemSelect = $('#cityItemSelect').combobox('getValue');
	    var cityItemInput = $('#cityItemInput').val();
	    
	    var groupItemSelect = $('#groupItemSelect').combobox('getValue');
	    var groupItemInput = $('#groupItemInput').val();
	    
	    var userItemSelect = $('#userItemSelect').combobox('getValue');
	    var userItemInput = $('#userItemInput').val();
	    
	    var userType = $('#userType').combobox('getValue');
	    
	    var fromRegTime = $('#fromRegTime').datetimebox('getValue');
	    var toRegTime = $('#toRegTime').datetimebox('getValue');
	    var fromLoginTime = $('#fromLoginTime').datetimebox('getValue');
	    var toLoginTime = $('#toLoginTime').datetimebox('getValue');
	    
	    //var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
	    //console.info($('#conditionSelect').combobox('getValue'));
	    //console.info(conditionSelect+"**"+conditionInput+"**"+verifiedStaus);
	    $('#tb_select').datagrid('load',{
	    	cityItemSelect :cityItemSelect,
	    	cityItemInput  :cityItemInput,
	    	groupItemSelect:groupItemSelect,
	    	groupItemInput :groupItemInput,
	    	userItemSelect  :userItemSelect,
	    	userItemInput :userItemInput,
	    	userType :userType,
	    	fromRegTime :fromRegTime,
	    	toRegTime  :toRegTime,
	    	fromLoginTime :fromLoginTime,
	    	toLoginTime :toLoginTime
	    });
		
	}
		
	
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
	}

	function deploy(){
		var id="${id}";
		var msg=$('#msg').val();
		var idList=$('#idList').val();
		var num=$('#num').val();
		var remark=$('#remark').val();
	      var selectAll=$("input[name='selectAll']:checked").val();
		$.ajax({
			url:'<%=basePath%>spread/loan/deploy.do',
			data:{"id":id,"msg":msg,"idList":idList,"num":num,"remark":remark,"selectAll":selectAll},
			method:'post',
			success : function(ret){
				var massage=ret.massage;
				if(massage=="error"){
					alert("失败");
				}if(massage=="ok"){
					alert("发送成功");
				window.location='<%=basePath%>spread/loan/index.do';
				}
			}
		});
	}
	function cancel(){
		window.history.go(-1);
	}
function change1(){
	 var selectAll=$("input[name='selectAll']:checked").val();
	 if(selectAll!=null){
	var total=$('#tb_select').datagrid('getData','total').total;
	$('#num').val(total);
	 }else{
		 $('#num').val(0);
	 }
}
	
</script>

</head>
<body onload="loaddone()">
	<div data-options="region:'north'" style="width: 55%">

		<div
			style="position: relative; border: 0px red solid; width: 100%; height: 100px; margin-left: auto; margin-right: auto; margin-top: 5px">
			<div
				style="position: absolute; top: 0px; left: 1%; border: 0px red solid; width: 10%; height: 25px;">
				<font style="font-weight: normal; font-size: 80%">点券ID</font>
			</div>
			<div
				style="position: absolute; top: 0px; left: 12%; border: 0px red solid; width: 20%; height: 25px;">
				<input name="cityName" value="${id}"
					class="easyui-validatebox textbox" style="width: 90%; height: 20px" disabled="disabled"></input>
			</div>
			<div
				style="position: absolute; top: 0px; left: 33%; border: 0px red solid; width: 10%; height: 25px;">
				<font style="font-weight: normal; font-size: 80%">点数</font>
			</div>
			<div
				style="position: absolute; top: 0px; left: 44%; border: 0px red solid; width: 20%; height: 25px;">

				<input name="cityName" value="${pointNumber}"
					class="easyui-validatebox textbox" style="width: 90%; height: 20px" disabled="disabled"></input>
			</div>
			<div
				style="position: absolute; top: 0px; left: 65%; border: 0px red solid; width: 10%; height: 25px;">
				<font style="font-weight: normal; font-size: 80%">有效期</font>
			</div>
			<div
				style="position: absolute; top: 0px; left: 76%; border: 0px red solid; width: 20%; height: 25px;">

				<input name="cityName" value="${valid_period_page}"
					class="easyui-validatebox textbox" style="width: 90%; height: 20px" disabled="disabled"></input>
			</div>

			<div
				style="position: absolute; top: 25px; left: 0%; border: 0px red solid; width: 30%; height: 25px;">
				<input id="cityItemSelect" name="cityItemSelect">
			</div>
			<div
				style="position: absolute; top: 25px; left: 30%; border: 0px red solid; width: 20%; height: 25px;">
				<input id="cityItemInput" name="cityItemInput"
					class="easyui-validatebox textbox" style="width: 90%; height: 20px"></input>
			</div>
			<div
				style="position: absolute; top: 25px; left: 50%; border: 0px red solid; width: 30%; height: 25px;">
				<input id="groupItemSelect" name="groupItemSelect">
			</div>
			<div
				style="position: absolute; top: 25px; left: 80%; border: 0px red solid; width: 20%; height: 25px;">
				<input id="groupItemInput" name="groupItemInput"
					class="easyui-validatebox textbox" style="width: 90%; height: 20px"></input>
			</div>

			<div
				style="position: absolute; top: 50px; left: 0%; border: 0px red solid; width: 30%; height: 25px;">
				<input id="userItemSelect" name="userItemSelect">

			</div>
			<div
				style="position: absolute; top: 50px; left: 30%; border: 0px red solid; width: 20%; height: 25px;">
				<input id="userItemInput" name="userItemInput"
					class="easyui-validatebox textbox" style="width: 90%; height: 20px"></input>
			</div>
			<div
				style="position: absolute; top: 50px; left: 50%; border: 0px red solid; width: 30%; height: 25px; text-align: center">
				<font style="font-weight: normal; font-size: 80%">客户身份:</font>
			</div>
			<div
				style="position: absolute; top: 50px; left: 80%; border: 0px red solid; width: 20%; height: 25px;">
				<input id="userType" name="userType">
			</div>

			<div
				style="position: absolute; top: 75px; left: 0%; border: 0px red solid; width: 100%; height: 25px;">
				<span
					style="margin-left: 0px; width: 20%; text-align: center; line-height: 25px; float: left"><font
					style="font-weight: normal; font-size: 80%">注册时间:</font></span> <input
					id="fromRegTime" name="fromRegTime" type="text"
					class="easyui-datetimebox" style="width: 25%; height: 20px"
					data-options="formatter:myformatter" />&nbsp;~&nbsp; <input
					id="toRegTime" name="toRegTime" type="text"
					class="easyui-datetimebox" style="width: 25%; height: 20px"
					data-options="formatter:myformatter" />
			</div>

			<div
				style="position: absolute; top: 100px; left: 0%; border: 0px red solid; width: 100%; height: 25px;">
				<span
					style="margin-left: 0px; width: 20%; text-align: left; line-height: 25px; float: left"><font
					style="font-weight: normal; font-size: 80%">最后登录时间:</font></span> <input
					id="fromLoginTime" name="fromLoginTime" class="easyui-datetimebox"
					style="width: 25%; height: 20px"
					data-options="formatter:myformatter" />&nbsp;~&nbsp; <input
					id="toLoginTime" name="toLoginTime" class="easyui-datetimebox"
					style="width: 25%; height: 20px"
					data-options="formatter:myformatter" /> <a id="btn"
					class="easyui-linkbutton" data-options="iconCls:'icon-search'"
					onclick="aaa()">查&nbsp;询</a>
			</div>
			<div
				style="position: absolute; top: 125px; left: 0%; border: 0px red solid; width: 100%; height: 25px; margin-left: 30px">
				<input name="selectAll" type="checkbox" onChange="change1()" />
				<font style="font-weight: normal; font-size: 80%">选择所有客户</font>
			</div>
			<div
				style="position: absolute; top: 160px; left: 0%; border: 0px red solid; width: 100%; height: 180px;">
				<table id="tb_select" style="width: auto; height: 180px">
				</table>
			</div>


			<div
				style="position: absolute; top: 345px; left: 0%; border: 0px red solid; width: 100%; height: 60px;">
				<form id="infoForm">
					<table>
						<tr style="height: 40px">
							<td style="text-align: left"><font
								style="font-weight: normal; font-size: 80%">目标客户数：</font></td>
							<td><input name="userIdList" id="idList" type="hidden" /> <input
								name="newSendLog.num" id="num" type="text" width="30px"
								readOnly="readonly"></td>
						</tr>
						<tr style="height: 40px">
							<td style="text-align: left"><font
								style="font-weight: normal; font-size: 80%">通知内容 : </font></td>
							<td>
								<!-- <input name="newSendLog.msg" id="msg" type="text" size="50" value="恭喜，收到一张新点券！" /> -->
								<textarea name="msg" id="msg" rows="2" cols="60"></textarea>
							</td>
						</tr>
						<tr style="height: 40px">
							<td style="text-align: left"><font
								style="font-weight: normal; font-size: 80%">备注说明 :</font></td>
							<td>
								<!-- <input name="newSendLog.remark" id="remark" type="text" size="50"/> -->
								<textarea name="remark" id="remark" rows="2" cols="60"></textarea>
							</td>
						</tr>
						<tr style="height: 40px">
							<td colspan="4" style="text-align: center"><a
								href='javascript:void(0)' onclick="deploy()"
								class="easyui-linkbutton l-btn" style="width: 90px">&nbsp;发放&nbsp;</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href='javascript:void(0)'
								onclick="cancel()" class="easyui-linkbutton l-btn"
								style="width: 90px">&nbsp;取消&nbsp;</a></td>
						</tr>
					</table>
				</form>
			</div>

		</div>
		<%-- 	<table id="querytable" cellspacing="0" cellpadding="5px" border=0
				style="text-align: left; height: auto; margin: 0px;">

			<tr>
				<td ><span style="margin-right:20px">&nbsp;&nbsp;点券ID：</span></td>
				<td><input name="id" id="id"  disabled="disabled" value="${id}" /></td>
				<td><span style="margin-left:50px">点数：</span></td>
				<td><input name="syscoupon_code" id="syscoupon_code"  disabled="disabled" value="${syscoupon_code}" /></td>
				<td><span style="margin-left:50px">有效期：</span></td>
				<td><input id="valid_period_page" name="valid_period_page"  disabled="disabled" value="${valid_period_page}" /></td>
			</tr>
			<tr>
				<td>				
					<select id="cityItem" style="width:150px;">
						<option value="1">注册城市</option>
						<option value="2">最后登录城市</option>
						<option value="3" selected>注册或最后登录城市</option>
					</select>
				</td>
				<td><input id="conditionInput" name="conditionInputName" class="textbox" /></td>
				<td>
					<select id="groupItem" style="width:150px;">
						<option value="1">按集团/联盟标识查询</option>
						<option value="2">按集团/联盟名称查询</option>
					</select>
				</td>
				<td><input id="conditionInput" name="conditionInputName" class="textbox" /></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td>
					<select id="userItem" style="width:100px;">
						<option value="1">按客户ID查询</option>
						<option value="2">按客户姓名查询</option>
						<option value="3">按客户手机查询</option>
					</select>
				</td>
				<td><input id="conditionInput" name="conditionInputName" class="textbox" /></td>
				<td>客户身份</td>
				<td>
					<select id="status" style="width:50px;">
						<option value="1">乘客</option>
						<option value="2">车主</option>
					</select>
				
				</td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td><span>&nbsp;&nbsp;注册时间&nbsp;:&nbsp;</span></td>
				<td><input id="fromRegTime" name="fromRegTime" class="easyui-datetimebox" 
				style="width:145px;" data-options="formatter:myformatter"/>&nbsp;~</td>
				<td><input id="toRegTime" name="toRegTime" class="easyui-datetimebox" 
				style="width:145px;" data-options="formatter:myformatter"/>	</td>
				<td><span>&nbsp;&nbsp;&nbsp;&nbsp;登录时间&nbsp;:&nbsp;</span></td>
				<td><input id="fromLoginTime" name="fromLoginTime" class="easyui-datetimebox" 
				style="width:145px;" data-options="formatter:myformatter"/>&nbsp;~</td>
				<td><input id="toLoginTime" name="toLoginTime" class="easyui-datetimebox" 
				style="width:145px;" data-options="formatter:myformatter"/>	</td>
			</tr>
			<tr>
				<td><a href="javascript:void(0)" onclick="btnSearch()" style="margin-left: 50px; width:40px" class="easyui-linkbutton" iconCls="icon-search"></a></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;符合条件的用户数&nbsp;:&nbsp;&nbsp;</span></td>
				<td><input id=" " style="width:50px;" plain="true"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td></td>
				<td></td>
				<td></td>
				<td><input type="checkbox" id=" " name=" "  checked/><span>&nbsp;&nbsp;选择所有符合条件的用户</span></td>
			</tr>
		</table> --%>

	</div>

	<script type="text/javascript">
function aa(){
	
	alert("1");
    var cityItemSelect = $('#cityItemSelect').combobox('getValue');
    
    alert("2");
    var cityItemInput = $('#cityItemInput').val();
    var groupItemSelect = $('#groupItemSelect').combobox('getValue');
    var groupItemInput = $('#groupItemInput').val();
    alert("3");
    var userItemSelect = $('#userItemSelect').combobox('getValue');
    var userItemInput = $('#userItemInput').val();
    
    var userType = $('#userType').combobox('getValue');
     //console.info(cityItemSelect+"--"+cityItemInput+"---"+groupItemSelect+"--"+groupItemInput+"--"+userItemSelect+"--"+userItemInput+"---"+userType );
      // var fromRegTime = $('#fromRegTime').datetimebox('getValue');	
     //var fromRegTime = $('#fromRegTime').datetimebox('getValue');
     //console.info("fromRegTime : "+fromRegTime);
/*   var toRegTime = $('#toRegTime').datetimebox('getValue');
    var fromLoginTime = $('#fromLoginTime').datetimebox('getValue');
    var toLoginTime = $('#toLoginTime').datetimebox('getValue'); */
    alert("4");
    //var verifiedStaus = $("input[name='verifiedStaus']:checked").val();
    //console.info($('#conditionSelect').combobox('getValue'));
    //console.info(fromRegTime+"**"+fromLoginTime+"**"+toLoginTime);
    $('#tb_select').datagrid('load',{
    	cityItemSelect :cityItemSelect,
    	cityItemInput  :cityItemInput,
    	groupItemSelect:groupItemSelect,
    	groupItemInput :groupItemInput,
    	userItemSelect  :userItemSelect,
    	userItemInput :userItemInput,
    	userType :userType,
    	//fromRegTime :fromRegTime,
    	//toRegTime  :toRegTime,
    	//fromLoginTime :fromLoginTime,
    	//toLoginTime :toLoginTime
    });
	}

</script>
</body>
</html>