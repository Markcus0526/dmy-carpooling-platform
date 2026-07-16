<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">
<html>
<head>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="<%=basePath%>js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>js/themes/default/easyui.css"
	type="text/css"></link>
<script>
	var tb_index = -1;
	$('.easyui-dialog').dialog();
	$('.easyui-dialog').dialog('close');
	$('.easyui-tree').tree();
	$('.easyui-tabs').tabs();
	$('.easyui-datebox').datebox();			
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-combobox').combobox();
	$('.easyui-searchbox').searchbox();
	$('.easyui-validatebox').validatebox();
	var page_path = '<%=basePath%>operation/abbrecord/';
	function check2(e){
	  var abb_type=e;
		  var flag=check1(abb_type);
	  if(flag==true){
		  abb_type=$("input[name='"+abb_type+"']:checked").val();
	  } if(flag==false){
		  abb_type=null;
	  }
		  return abb_type;
	}
  function open1(){
	  $('#tb_1').datagrid({
			url: page_path +'find.do',
			loadMsg : '数据处理中，请稍后....',
			height: 'auto',
			queryParams: {
				abb_type2: check2("abb_type2"),
				abb_type1: check2("abb_type1")
			},
			fitColumns: true,
			pagination: true,
			width:'1050',
			columns:[[
			          {field:'opreation1',title:'操作',width:200, align:'center',
			        	formatter:function(value,row,index){  
			        		if(row.abb_type==3){
			        			if(row.status==1){
			        		        var btn="";
					                btn += '<a class="editcls" onclick="warning('+row.id+','+row.userid+')" href="javascript:void(0)">警告</a> |&nbsp;';  
					                btn += '<a class="editcls" onclick="forfeit('+row.id+','+row.order_exec_id+')" href="javascript:void(0)">扣款</a> |&nbsp;'; 
					                btn += '<a class="editcls" onclick="addblack('+row.id+','+row.userid+')" href="javascript:void(0)">加入黑名单</a> &nbsp;';
					               return btn;  
				        		}else{
						               return "";
				        		}
			        		}else{
			        			if(row.status==1){
			        				 var btn="";
						                btn += '<a class="editcls" onclick="warning('+row.id+')" href="javascript:void(0)">警告</a> |&nbsp;';
						                btn += '<a class="editcls" onclick="addblack('+row.id+','+row.userid+')" href="javascript:void(0)">加入黑名单</a> &nbsp;';
						               return btn;  
				        		}else{
						               return "";
				        		}
			        		
			        		}
			         	 }
			          },
			          {field:'userid',title:'违约客户ID',width:100, align:'center'},
			          {field:'username',title:'违约客户姓名',width:100, align:'center'},
			          {field:'phone',title:'违约客户手机',width:100,align:'center'},
			          {field:'abb_type',title:'违约行为',width:150,align:'center',
			        	  formatter:function(value){
			                  if(value==1)
			                      return '多次取消订单';
			                  if(value==3)
			                      return '乘客逃单';
			              } 
			          },
			          {field:'abb_time',title:'违约时间',width:150,align:'center',
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
			          {field:'cancel_number',title:'违约层度',width:100,align:'center'},
			          {field:'status',title:'状态',width:200,align:'center',
			        	  formatter:function(value,rowData,rowIndex){
			                  if(rowData.status==1)
			                      return ' 待处理';
			                  if(rowData.status==2)
			                      return '已做警告处罚';
			                  if(rowData.status==3)
			                	  return '已做扣款处罚';
			                  if(rowData.status==5)
			                	  return '已解除黑名单';
			              }
			          },
			          {field:'order_exec_id',title:'相关订单',width:150,align:'center',
			        	  formatter:function(value,row,index){  
			        		  if(row.order_exec_id!=0){
			              	    var btn="";
			              	  btn+= '<a href="javascript:void(0)" onclick="view('+row.order_exec_id+')">'+row.order_exec_id+'</a>';
			              	  return btn;
			        	  }
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
	function open2(){
		$('#tb_2').datagrid({
			url: page_path +'find.do',
			height: 'auto',
			
			queryParams: {
				abb_type2: check2("abb_type4"),
				abb_type1: check2("abb_type3")
			},
			fitColumns: true,
			pagination: true,
			columns:[[
			          {field:'opreation1',title:'操作',width:200, align:'center',
				        	formatter:function(value,row,index){ 
				        		if(row.status==1){
				        			 var btn="";
						                btn += '<a class="editcls" onclick="warning('+row.id+')" href="javascript:void(0)">警告</a> |&nbsp;';
						                btn += '<a class="editcls" onclick="addblack('+row.id+','+row.userid+')" href="javascript:void(0)">加入黑名单</a> &nbsp;';
						               return btn;  
				        		}else{
				        	
						               return "";
				        		}
				         	 }
				          },
			          {field:'userid',title:'违约客户ID',width:150,  align:'center'},
			          {field:'username',title:'违约客户姓名',width:150, align:'center'},
			          {field:'phone',title:'违约客户手机',width:100,align:'center'},
			          {field:'abb_type',title:'违约行为',width:150,align:'center',
			        	  formatter:function(value){
			                  if(value==2)
			                      return ' 车主迟到';
			                  if(value==4)
			                      return '车主逃单';
			              }
			          },
			          {field:'abb_time',title:'违约时间',width:150,align:'center',
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
			          {field:'cancel_number',title:'违约程度',width:250,align:'center'},
			          {field:'status',title:'状态',width:200,align:'center',
			        	  formatter:function(value,rowData,rowIndex){
			                  if(rowData.status==1)
			                      return ' 待处理';
			                  if(rowData.status==2)
			                      return '已做警告处罚';
			                  if(rowData.status==3)
			                	  return '已做扣款处罚';
			                  if(rowData.status==5)
			                	  return '已解除黑名单';
			              }
			          },
			          {field:'order_exec_id',title:'相关订单',width:250,align:'center',
			        	  formatter:function(value,row,index){  
			        		  if(row.order_exec_id!=0){
			              	    var btn="";
			              	  btn+= '<a href="javascript:void(0)" onclick="view('+row.order_exec_id+')">'+row.order_exec_id+'</a>';
			              	  return btn;
			        	  }
			              	     
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
  
	$(function(){
		document.getElementById("abb_type1").checked = true;
		document.getElementById("abb_type2").checked = true;
		document.getElementById("abb_type3").checked = true;
		document.getElementById("abb_type4").checked = true;
		document.getElementById("abb_handle1").checked = true;
		document.getElementById("abb_handle2").checked = true;
		   open3();
		   open1();
		$('#tab').tabs({   
			       border:true,  
			       height:600,
			       width:'auto',
			       onSelect:function(title){ 
			         if(title=="拼车乘客"){
			        	open1();
			           }
			        if(title=="拼车车主"){
						open2();
		           	 }
				},
		 });
	});
	
	function select1(){
		       var status=$("input[name='status']:checked").val();
		       var  uerselect = $('#uerselect').combobox('getValue'); 
		       var inputvalue = $('#inputvalue').val();
			   $('#tb_1').datagrid('load',{
				   status:status,
				   uerselect:uerselect,
				   inputvalue:inputvalue,
				    abb_type2: check2("abb_type2"),
					abb_type1: check2("abb_type1")
				   });
	}
	function select2(){
	       var status=$("input[name='status1']:checked").val();
	       var  uerselect = $('#uerselect1').combobox('getValue');
	       var inputvalue = $('#inputvalue1').val();     
		   $('#tb_2').datagrid('load',{
			   status:status,
			   uerselect:uerselect,
			   inputvalue:inputvalue,
			   abb_type2: check2("abb_type4"),
			   abb_type1: check2("abb_type3"),
			   });
	}
	function check1(id){
		if(document.getElementById(id).type=='checkbox'){
			if(document.getElementById(id).checked==true){
		      document.getElementById(id).checked = true;
			   }
			 if(document.getElementById(id).checked==false){
		      document.getElementById(id).checked = false;
			   }
			  }
		return document.getElementById(id).checked;
	}

	function warning(id,userid){
		$('#tb').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'',
			    width:300,   
			    height:300,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:100,
			    content:'<h1 style="margin-left:80px;">发送警告</h1><span "style="margin-left:30px;width:100px;">警告内容</span>:<textarea id="inner"  cols="" rows=""   style="margin-left:30px;width:220px;height:150;" name="content" Class="textbox _textbox"/>'
			    		+'<a href="javascript:void(0)" onclick="close1(),warn('+id+','+userid+')" style="margin-left:50px;width:60px;" class="easyui-linkbutton">确定</a>'
			    		+'<a href="javascript:void(0)" onclick="close1()"style="margin-left:50px;width:50px;" class="easyui-linkbutton">取消</a>',
			 onOpen:function(){
				    $('#id').val(id);
					$.ajax({
						url:page_path+"findById.do",
						data:'id='+id,
						method:'post',
						success:function(data){
							 var JsonDateValue = new Date(data.abb_time.time);
		            		  var text = JsonDateValue.toLocaleDateString();
		            		text =text.split("/");
						$("#inner").val("您已被发现违约行为“订单违约”，违约时间："+text[0]+"年"+text[1]+"月"+text[2]+"日，请规范个人行为，维护良好拼车秩序。");
						}
					});
			 }
			});  
	
	}
	   function warn(id,userid){
		   alert(id);
		 $.ajax({
			 url:'<%=basePath%>operation/abbrecord/warn.do',
			 method:'post',
			 data:{"msg":$("#inner").val(),"id":id,"userid":userid},
			 success:function(ret){
				 if(ret.error==0){
					 alert("已发出警告");
					 window.location='<%=basePath%>operation/abbrecord/list.do';
				 }
			 }
		 });
	   }
	  function  close1(){
		  $('#tb').dialog('close');
		  }
   function addblack(id,userid){
	   $('#tb').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'',
			    width:380,   
			    height:200,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:200,
			    content:'<form action="<%=basePath%>operation/abbrecord/addblack.do" method="post" enctype="multipart/form-data">  '
			    	 +'<table><br><input type="text" id="id" name="id" style="display:none"/><input type="text" id="userid" name="userid" style="display:none"/>'
			    	 +'<tr><td> <h3 style="margin-left:30px;">是否确定把该用户 加入黑名单？</h3></td></tr>'
			    	 +'<tr><td><span style="margin-left:30px;width:100px;">停封期限:</span></td> </tr>'
			    	+' <tr> <td><span style="margin-left:50px"> </span> <input   type="radio" name="black" checked="checked"  value="1">永久</td><tr>'
					+'<tr> <td><span style="margin-left:50px"><input   type="radio" name="black"  value="2">期限<input type="text" id="limit_days" name="limit_days" Class="textbox _textbox" style=" width:30px;height:22">天</td></tr>'
			    	 +'</tr><tr> <td> <input type="submit" value="确认" style="margin-left:25px;"><input type="reset"  value="取消" onclick="close2()" style="margin-left:50px;"> </td> </tr></table></form> ',
			 onOpen:function(){
				 $('#id').val(id);
				 $('#userid').val(userid);
			 }
			}); 
	   
   }
   function  close2(){
		  $('#tb').dialog('close');
		  }
   function forfeit(id,order_exec_id){
		$('#tb').dialog({ 
			   hide:true, //点击关闭是隐藏,如果不加这项,关闭弹窗后再点就会出错
			    title:'',
			    width:300,   
			    height:200,   
			    modal:true , 
			    closable:false,
			    draggable:true,
			    border:true,
			    top:150,
			    content:'<h1 style="margin-left:80px;">扣款 </h1><span style="font-size:20px">扣款额度 :</span><input  id ="point" style="width:60px;height:27px" name="point" Class="textbox _textbox"/>'
			    		+'<br><br><a href="javascript:void(0)" onclick="close1(),forfeit1('+id+','+order_exec_id+')" style="margin-left:30px;width:60px;" class="easyui-linkbutton">确定</a>'
			    		+'<a href="javascript:void(0)" onclick="close1()"style="margin-left:20px;width:50px;" class="easyui-linkbutton">取消</a>',
			 onOpen:function(){
		
			 }
			});  
   }
   function link(){
	   URL='<%=basePath%>operation/abbrecord/blacklist.do';
	   window.location=URL;
   }
   function forfeit1(id,order_exec_id){
	   alert(id+"         "+order_exec_id);
		 $.ajax({
			 url:'<%=basePath%>operation/abbrecord/forfeit.do',
			 method:'post',
			 data:{"point":$("#point").val(),"order_exec_id":order_exec_id,"id":id},
			 success:function(ret){
				 if(ret.error==0){
					 alert("已扣除");
					 window.location='<%=basePath%>operation/abbrecord/list.do';
				 }	 if(ret.error==2){
					 alert("你扣点的订单不存在");
				 }
			
			 }
		 });
   }
   function open3(){
	  var error="${error}";
	   $.ajax({
		   url:'<%=basePath%>operation/abbrecord/findAbb.do',
		   success:function(data){
               $('#cancel_number1').val(data.code1);  
               $('#cancel_number2').val(data.code2); 
               
		   }
	   });
   }
	function view(id) {
		//var page_path = '${pageContext.request.contextPath}/order/appoint/';
		window.location=page_path + "view1.do?id="+id/* +"&&choose="+4 */ ;
	}
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
	function save1(){
		$.ajax({
			url:'<%=basePath%>operation/abbrecord/addAbb.do',
			data:{
				'abb_handle1':$('#abb_handle1').val(),
				'cancel_number1':$('#cancel_number1').val(),
				'abb_handle2':$('#abb_handle2').val(),
				'cancel_number2':$('#cancel_number2').val(),
			},
			success:function(ret){
				if(ret.error==0){
					alert("添加成功");
					window.location='<%=basePath%>operation/abbrecord/list.do';
				}if(ret.error==1){
					alert("添加失败");
				}
			}
			
		});
	}
</script>
</head>
<body onload="loaddone()">
	<div id="head" style="border: red 0px solid; margin-left: 30px">
		<h2 class="page-title txt-color-blueDark">
			<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp违约处理
		</h2>
		<hr>
		<form action=""
			method="post" enctype="multipart/form-data">
			<h3>自处理违约定义</h3>
			<input type="hidden" id ="error" name="error"   />
			<input type="checkbox" value="1" id="abb_handle1" name="abb_handle1"
				onclick="check1(this.id)" />乘客多次取消服务:一天内取消订单 <input type="text"
				id="cancel_number1" name="cancel_number1" Class="textbox _textbox"
				style="width: 30; height: 22" /> 次<br> <br> <input
				type="checkbox" value="2" id="abb_handle2" name="abb_handle2"
				onclick="check1(this.id)" />车主迟到:单一订单迟到超过 <input type="text"
				id="cancel_number2" name="cancel_number2" Class="textbox _textbox"
				style="width: 30; height: 22" /> 分钟	
				<a href="javascript:void(0)" onclick="save1()"
								style="margin-left: 100px; width: 80px;"
								class="easyui-linkbutton" iconCls="icon-save">保存</a>
		</form>
	</div>
	<div id="tab" class="easyui-tabs" data-options="tabWidth:112"
		style="width: auto">
		<div title="拼车乘客" style="padding: 10px">
			<div class="easyui-layout" data-options="fit:true,border:true"
				style="border: red 0px solid;">
				<div data-options="region:'north',title:'',border:true"
					style="height: 120px;">
					<div style="padding: 5px;">
						<div style="height: 30;">
							<select id="uerselect" name="uerselect" class="easyui-combobox"
								style="width: 150px;">
								<option value="1">按客户ID查询</option>
								<option value="2">按客户手机查询</option>
								<option value="3">按客户姓名查询</option>
							</select> <input id="inputvalue" name="inputvalue"
								Class="textbox _textbox" style="width: 150; height: 22">
						</div>
						<div style="height: 60">
							<input type="checkbox" value="1" id="abb_type1" name="abb_type1"
								onclick="check1(this.id)" />多次取消服务<br> <br> <input
								type="checkbox" value="3" id="abb_type2" name="abb_type2"
								onclick="check1(this.id)" />乘客订单违约 <input type="checkbox"
								value="5" id="status" name="status" style="margin-left: 500px;" />显示已处罚违约行为
							<a href="javascript:void(0)" onclick="select1()"
								style="margin-left: 100px; width: 80px;"
								class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
								href="#" onclick="javascript:report();"
								style="width: 100px; float: right;"
								class="easyui-linkbutton l-btn" iconCls="icon-xlx">导出Excel</a>
						</div>
					</div>
				</div>
				<div data-options="region:'center',title:''"
					style="border: green 0px solid;">
					<table id="tb_1" style="padding: 1px; boder: black 0px solid;"
						data-options="fit:true,border:true">
					</table>
				</div>
			</div>
		</div>
		<div title="拼车车主" style="padding: 10px">
			<div class="easyui-layout" data-options="fit:true,border:true"
				style="border: red 0px solid;">
				<div data-options="region:'north',title:'',border:true"
					style="height: 120px;">
					<div style="padding: 5px">
						<div style="height: 30;">
							<select id="uerselect1" name="uerselect1" class="easyui-combobox"
								style="width: 150px;"" >
								<option value="1">按客户ID查询</option>
								<option value="2">按客户手机查询</option>
								<option value="3">按客户姓名查询</option>
							</select> <input id="inputvalue1" name="inputvalue1"
								Class="textbox _textbox" style="width: 150; height: 22">
						</div>
						<div style="height: 60">
							<input type="checkbox" value="2" id="abb_type3" name="abb_type3"
								onclick="check1(this.id)" />车主迟到<br> <br> <input
								type="checkbox" value="4" id="abb_type4" name="abb_type4"
								onclick="check1(this.id)" />车主订单违约 <input type="checkbox"
								value="5" id="status1" name="status1"
								style="margin-left: 500px;" />显示已处罚违约行为 <a
								href="javascript:void(0)" onclick="select2()"
								style="margin-left: 100px; width: 80px;"
								class="easyui-linkbutton" iconCls="icon-search">查询</a> <a
								href="#" onclick="javascript:report();"
								style="width: 100px; float: right;"
								class="easyui-linkbutton l-btn" iconCls="icon-xlx">导出Excel</a>
						</div>
					</div>
				</div>
				<div data-options="region:'center',title:''"
					style="border: green 0px solid;">
					<table id="tb_2" style="padding: 1px; boder: black 0px solid;"
						data-options="fit:true,border:true">
					</table>
				</div>
			</div>
		</div>

		<div title="代价用户" style="padding: 10px"></div>

		<div title="代价司机" style="padding: 10px"></div>
	</div>
	<div id="tb"></div>
	<div>
		<a href="javascript:void(0)" onclick="link()"
			style="margin-left: 70px; width: 120px; height: 30px;"
			class="easyui-linkbutton">黑名单管理</a>
	</div>
</body>
</html>