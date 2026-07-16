<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"operation/evaluate/";
System.out.println(basePath);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

		

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
//加载页面时 上个页面的信息根据userid传过来
function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
} 
	$(function(){		
		$.ajax({
			url :'<%=basePath%>action2.do',
			
				data : 'userid=${userid}',
				dataType : "json",
				success : function(jsonObject) {
					//alert(jsonObject.Name);
					//console.info($('#Name'));
					$('#userid').val(jsonObject.userid);
					$('#userid1').val(jsonObject.userid);
					$('#username').val(jsonObject.username);
					$('#phone').val(jsonObject.phone);
					$('#group_name').val(jsonObject.group_name);
					$('#passnum').val(jsonObject.passnum);
					$('#drinum').val(jsonObject.drinum);
					$('#ckpl').val(jsonObject.ckpl);
					$('#czpl').val(jsonObject.czpl);						
				}
			});
		 $('#dg').datagrid({ 
			
				queryParams:{
					level1:$("#top").get(0).checked == true ? 1 : 0,
						level2:$("#mid").get(0).checked == true ? 1 : 0,
							level3:$("#las").get(0).checked == true ? 1 : 0,
					
				
		   },
			 title:"评价详情表格",
			    url:'<%=basePath%>action3.do?userid=<%=request.getParameter("userid")%>', 
			    method:'get',
			    pageNumber:1,
			    iconCls : 'icon-show',
				pagination : true,
				pageSize : 10,
				nowrap : false,
				border : false,
				idFiled : 'id',
				rownumbers:true,
				fitColumns:true,
				singleSelect:true,
				toolbar: '#toolbar',
				columns : [ [ {
					field : 'op',
					title : '操作',
					width : 80,
					formatter:function(value,row,index){  
						if(row.deleted=='有效'){
							 var btn="";
				          	    btn += '<a href="" class="editcls" onclick="m('+row.order_cs_id+','+row.level+','+row.eid+') ;return false">修改</a>|'; 
				          	  btn += '<a href="" class="editcls" onclick="p('+row.order_cs_id+') ;return false">屏蔽</a> '; 
				          	    return btn; 
						}else{
							 var btn="";
				          
				          	  btn += '<a href="" class="editcls" onclick="p1('+row.order_cs_id+') ;return false">还原</a> '; 
				          	    return btn;  
						}
		          	   
		     		   },
				}, {
					field : 'order_cs_id',
					title : '订单编号',
					width : 100,
					align : 'center',
				}, 
				{
					field : 'eid',
					title : '号',
					width : 100,
					align : 'center',
				},
				{
					field : 'ps_date',
					title : '评价时间',
					width : 100,
					align : 'center',
				}, 
				 {
					field : 'usertype',
					title : '客户订单身份',
					width : 100,
					align : 'center',
				},
				{
					field : 'from_userid',
					title : '评价人id',
					width : 100,
					align : 'center',
				},				
				 {
					field : 'level',
					title : ' 评价',
					width : 100,
					align : 'center',
					formatter:function(value,row,index){  
					if(value==1){
						return '好评';						
					}
					if(value==2){
						return '中评';						
					}
					if(value==3){
						return '差评';						
					}		
		          	   
		     		   },
				},
				 {
					field : 'msg',
					title : ' 详细评价',
					width : 100,
					align : 'center',
				},
				 {
					field : 'deleted',
					title : '有效性',
					width : 100,
					align : 'center',
				}
				] ]
					}); 
		
		 
		});
	//这里是查询的方法 点击之后显示表格
	function s(){  
	   
		var khsfselect = $('#khsfselect').combobox('getValue');
		var level1 =$("#top").get(0).checked == true ? 1 : 0;
		var level2 =$("#mid").get(0).checked == true ? 1 : 0;
		var level3 =$("#las").get(0).checked == true ? 1 : 0;
		var deleted = $("input[name='deleted']:checked").val();
		//var deleted = $("input[name='deleted']:checked").val();	
	    $('#dg').datagrid('load',{
	    	khsfselect:khsfselect,
	    	level1:level1, 	
	    	level2:level2,    	
	    	level3:level3,
	    	deleted:deleted
				});	

				
	}
	//修改方法
	function up(){
		var level = $("input[name='level1']:checked").val();
		var msg = $('#msg').val();
		var order_cs_id = $('#order_cs_id').val();
		if(confirm("确定修改？"))
		 {          
				path ='<%=basePath%>';
				$.ajax({
					url :'<%=basePath%>action4.do',
					data:'order_cs_id='+order_cs_id+'&&msg='+msg+'&&level='+level,
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
							alert("修改成功！！！");
							closem();
							 window.location.reload();
					
								
						}
					});
		 }    
		 else {         
			 alert("取消删除");         
			 return false;     
			 } 
		
	}
	//这里是修改的弹出窗口
	function m(order_cs_id,level,eid){
		
		
		
		 $('#modify').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'修改评价信息',
			    width:400,   
			    height:350,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:100,
			    content:' <form action="" method="post">'
			    +'<table id="addTable" style="width:400px;height:280px;margin-left:30px;">' 
			    +' <input type="text" id="order_cs_id" name="order_cs_id" style="display:none"/>'
			    +'  <tr><td> 评价:<input id="level1" type="radio" name="level1" value="1">好评<input id="level2" type="radio" name="level1" value="2">中评<input id="level3" type="radio" name="level1" value="3">差评</td></tr>'
			    +'<tr><td> 详细评价:<textarea  id="msg"  cols="" rows=""style="margin-left:5px;width:260px;height:220;" name="msg"/></td></tr>'
			    +'<tr> <td> <input type="button" value="确定" onclick="up()" style="width:40px;height:30px;margin-left:50px;">  <input type="button" value="取消" onclick="closem()" style="width:40px;height:30px;"> </td> </tr></table>  </form>'
			    ,
			    onOpen:function(){
			    	$('#order_cs_id').val(order_cs_id);
			    	$.ajax({
						url :'<%=basePath%>findmsg.do',
						data:'eid='+eid,
							dataType : "json",
							method:'post',
							success : function(jsonObject) {
							$('#msg').val(jsonObject.msg);
						
									
							}
						});
			    	//alert(order_cs_id);
			    	if(level==1){
			    		 document.getElementById('level1').checked=true;
			    	}
			    	if(level==2){
			    		 document.getElementById('level2').checked=true;
			    	}
			    	if(level==3){
			    		 document.getElementById('level3').checked=true;
			    	}
			    	
			 
			    	
			    }
		 });  		
	}

	 function  closem(){
		 $('#modify').dialog("close");
	  }
	 function p(order_cs_id){
		 if(confirm("确定屏蔽？"))
		 {          
				path ='<%=basePath%>';
				$.ajax({
					url :'<%=basePath%>updatedel.do',
					data:'order_cs_id='+order_cs_id+'&&deleted=1',
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
    
		
			 s();
								
						}
					});
		 }    
		 else {         
			 alert("取消屏蔽");         
			 return false;     
			 } 
	 }
	 function p1(order_cs_id){
		 if(confirm("确定还原？"))
		 {          
				path ='<%=basePath%>';
				$.ajax({
					url :'<%=basePath%>updatedel.do',
					data:'order_cs_id='+order_cs_id+'&&deleted=0',
						dataType : "json",
						method:'post',
						success : function(jsonObject) {
          
			
		s();
								
						}
					});
		 }    
		 else {         
			 alert("取消还原");         
			 return false;     
			 } 
	 }
	 
	</script>
	<div class="easyui-panel" data-options="fit:true" style="height:700px;border: gray 2px solid; padding: 5px; margin: 0px;">
	<div class="easyui-layout" data-options="fit:true,border:true"style="border: red 0px solid;">
	<div data-options="region:'north',title:'',border:true" style="height:195px;">
	<h2 class="page-title txt-color-blueDark"
		style="margin-left: 80px; margin-right: 15px">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;评价详情
	</h2>
	<HR style="margin-left: 80px; margin-right: 15px"></HR>
				<table style="margin-left: 80px; margin-top: 20px">
		<tr>
			<td><span>客户ID：</span><input type="text" id="userid"
				name="userid" readonly="readonly" style="color: #aaa;" /></td>
			<td>&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;</td>
			<td><span>客户姓名：</span><input type="text" id="username"
				name="username" readonly="readonly" style="color: #aaa;" /></td>
		</tr>
		<tr></tr>
		<tr>
			<td><span>客户手机：</span><input type="text" id="phone" name="phone"
				style="color: #aaa; width: 120px" /></td>
			<td>&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;</td>
			<td><span>所属集团：</span><input type="text" id="group_name"
				name="group_name" readonly="readonly" style="color: #aaa;" /></td>
		</tr>
		<tr></tr>
		<tr>
			<td><span>乘客身份订单数：</span><input type="text" id="passnum"
				name="passnum" readonly="readonly" style="color: #aaa; width: 70px" /></td>
			<td>&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td><span>乘客身份好评率：</span><input type="text" id="ckpl"
				name="ckpl" readonly="readonly" style="color: #aaa; width: 89px" /></td>
		</tr>
		<tr></tr>
		<tr>
			<td><span>车主身份订单数：</span><input type="text" id="drinum"
				name="drinum" readonly="readonly" style="color: #aaa; width: 70px" /></td>
			<td>&nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;</td>
			<td><span>车主身份好评率：</span><input type="text" id="czpl"
				name="czpl" readonly="readonly" style="color: #aaa; width: 89px" /></td>
		</tr>
	</table>
	
			</div>
		<div data-options="region:'center',title:''" style="border: green 0px solid;">
					<table id="dg" style="padding: 1px; boder: black 0px solid;" data-options="fit:true,border:true"></table>
					</div>
			<br>
				<div id="modify" method="post"></div>
				</div>
				</div>
				
				<div id="toolbar">
		
		<span id="usersf" style="font-size: 12px;margin-left:300px">客户订单身份:</span> <select
			id="khsfselect" name="khsfselect" class="easyui-combobox"
			style="width: 70px;">
			<option value="0">请选择</option>
			<option value="1">乘客</option>
			<option value="2">车主</option>
		</select> &nbsp; &nbsp; &nbsp;评价：<input type="checkbox" id="top" name="top"
			checked="checked" />
		<lable>好评</label> <input type="checkbox" id="mid" name="mid"
			checked="checked" /> <lable>中评</label> <input type="checkbox"
			id="las" name="las" checked="checked" /> <lable>差评</label> &nbsp;
		&nbsp; &nbsp;有效性:<input id="deleted" type="radio" name="deleted"
			value=0 checked>有效 &nbsp; &nbsp; &nbsp;<input id="deleted"
			type="radio" name="deleted" value=1>屏蔽 &nbsp;&nbsp; <a
				onclick="s()" style="margin-left: 30px; width: 60px;"
				class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp; <a
				href="<%=basePath%>index.do"
				style="margin-left: 30px; width: 100px;">返回上一页</a>

			</div>	
				
</body>

</html>