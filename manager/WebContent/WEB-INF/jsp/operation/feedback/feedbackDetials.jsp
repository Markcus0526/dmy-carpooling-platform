<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
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
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>

<script>

$('.easyui-combobox').combobox();
$('.easyui-linkbutton').linkbutton();
$('.easyui-datetimebox').datetimebox();
function click1(){
	var id=$('#id').val();
	var userid=$('#userid').val();
	var username=$('#username').val();
	var city=$('#city').val();
	var verified=$("input[name='verified']:checked").val();
	var begin=$('#begin').datetimebox('getValue');
	var over=$('#over').datetimebox('getValue');
	var phone=$('#phone').val();
	   $('#dg').datagrid('load',{
		      id:id,
		      userid:userid,
		      userName:username,
		      city:city,
		      verified:verified,
		      begin:begin,
		      over:over,
		      phone:phone,
		    });
}	

function deleted(id) {
	var page_path='<%=basePath%>operation/submenu1/';
	   msg='是否删除？';
	   if(window.confirm(msg)) {
	    URL=page_path+"delete.do?id="+id;
	    window.location=URL;
	   }else{
		   
	   }
	
};
$(function(){
	var id=$('#id').val();
	var userid=$('#userid').val();
	var username=$('#username').val();
	var city=$('#city').val();
	var verified=$("input[name='verified']:checked").val();
	var begin=$('#begin').datetimebox('getValue');
	var over=$('#over').datetimebox('getValue');
	var phone=$('#phone').val();
		dataGrid = $('#dg').datagrid({
			url :'<%=basePath%>operation/submenu1/findByCondition.do',
			queryParams:{
			      id:id,
			      userid:userid,
			      userName:username,
			      city:city,
			      verified:verified,
			      begin:begin,
			      over:over,
			      phone:phone,
			},
	         title:'',
	         height: 'auto',
	         pagination: true,
	         fitColumns: true,
	        remoteSort : false,
			   striped : true,
			   singleSelect:true,
         	toolbar : '#content',
			columns : [ [{field : 'operation', title : '操作' , width : 100,	align : 'center',
			      		formatter:function(value,row,index){  
		              	    var btn="";
		              	    btn += '<a class="editcls" onclick="select('+row.id+')" href="javascript:void(0)">详细</a> &nbsp;';  
		              	    btn += '<a class="editcls" onclick="deleted('+row.id+')" href="javascript:void(0)">删除</a> &nbsp;';  
		              	    return btn;  
		         		   }
		       			 },
			              {field : 'id',title : 'ID',width : 50,align : 'center'},
			              {field : 'userid',title : '用户ID' ,width : 100,align : 'center'},
			              {field : 'username',title : '用户名',width : 100,align : 'center'},
			              {field : 'city_cur',title : '所在城市',width : 100,align : 'center'},
			              {field : 'psDate',title : '反馈时间',width : 200,align : 'center',
			            	  formatter:function(value,rowData,rowIndex){
			            		  if(value!=null&&""!=value){
				        			  var JsonDateValue = new Date(value.time);
				            		  var text = JsonDateValue.toLocaleDateString();
				            		                return text;
				        		  }else{
				        			  return "";
				        		  }
			            		         }
			              },
			              {field : 'driver_verified',title : '身份验证',width : 150,align : 'center', 
			            	  formatter:function(value){
			                  if(value==1)
			                      return '车主';
			                  if(value==0)
			                      return '乘客';
			              }},
			              {field : 'phone',title : '联系电话',width : 150,align : 'center'} ,
			              {field : 'content',title : '内容',width : 150, align : 'center',
			            	  formatter:function(value){
			            		  if(value.length >10){
				               var str="";
				               str=value;
				               str=str.substring(0, 10);
				               str+="·····";
				               return str;
			            	  }else{
			            		  return value;
			            	  }
				              }
			              } ,
			              ] ],
			
			onLoadSuccess : function() {
				$('#list table').show();
				parent.$.messager.progress('close');
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
});

	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M);
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

 function select(id){
	   $('#tb').dialog({ 
		   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
		    title:'用户反馈信息详细',
		    width:400,   
		    height:350,   
		    modal:true , 
		    closable:false,
		    draggable:true,
		    border:true,
		    top:100,
		    
		    content:'<div style="padding:10px;margin-right:10px;margin-top:10px;margin-left:10px;margin-button:10px;border:1px solid blue;">'
		    +'<br><span> 详细内容:<br> <textarea id="content1" name="content" cols="" rows="" disabled="disabled"  style="margin-left:10px;width:330px;height:190;" />'
		    +'</span><br><a href="javascript:void(0)" onclick="close1()"style="margin-left:150px;width:50px;" class="easyui-linkbutton">关闭</a>'
		    +'</div>',
		 onOpen:function(){
			 path='<%=basePath%>operation/submenu1/';
			 $.ajax({
				 url :path+'findById.do?id='+id,
				success:function(data){
					 $('#content1').val(data.content);
				}
			 });
			   
		 }
		});  
 }
  function  close1(){
	 $('#tb').dialog("close");
  }
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">
<h2 class="page-title txt-color-blueDark">
			<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;用户反馈管理
		</h2>
	<div id="tab" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px; height: 760">
		
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 150px;">
				<table style="padding: 10px; margin-left: 20px">
					<tr>
						<td style="width: 300">所在城市:<input id="city" name="city"
							type="text" style="margin-left: 15px; width: 150" /></td>
						<td style="width: 450">反馈时间:<input id="begin" name="begin"
							Class="easyui-datetimebox" data-options="formatter:myformatter"
							style="width: 150px" />--<input id="over" name="over"
							Class="easyui-datetimebox" data-options="formatter:myformatter"
							style="width: 150px" />
						</td>
						<td style="width: 300"><span style="margin-left: 18px;"></span>反馈ID:<input
							id="id" style="margin-left: 5px" name="id" type="text" value="0">
						</td>
					</tr>
					<tr>
						<td>用户ID:<input id="userid" type="text" name="userid"
							style="margin-left: 30px; width: 150" value="0" />
						</td>
						<td>联系电话:<input id="phone" name="phone"
							style="margin-left: 5px" type="text" /></td>
						<td style="width: 300"></td>
					</tr>
					<tr>
						<td>用户姓名:<input id="username" type="text"
							style="margin-left: 15px; width: 150" name="username" />
						</td>
						<td>客户身份:<input type="radio" name="verified"
							style="margin-left: 15px;" value="1">车主<input
							type="radio" name="verified" style="margin-left: 15px;" value="2">乘客
						</td>
						<td><a href="javascript:void(0)" onclick="click1()"
							style="margin-left: 50px; width: 80px;" class="easyui-linkbutton"
							iconCls="icon-search">查询</a></td>
					</tr>
				</table>

				<div id="tb"></div>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;" style="height: 550px;">
				<table id="dg" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>

		</div>
		<div id="content" style="padding: 5px; height: 20px">
			<a href="#" onclick="javascript:report();"
				style="width: 100px; float: right;" class="easyui-linkbutton l-btn"
				iconCls="icon-xlx">导出Excel</a>
		</div>
	</div>
</body>

</html>