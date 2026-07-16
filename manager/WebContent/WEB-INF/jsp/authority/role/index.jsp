<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String msg = (String)request.getAttribute("msg");
%>
<html>
	<head>
		<title></title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/linkbutton.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/datebox.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/default/tree.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/themes/icon.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>  	  
		<link href="${pageContext.request.contextPath}/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.excheck-3.5.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ztree.exedit-3.5.js"></script>

	</head>
	<body onload="loaddone()">
	
	<input type="hidden" name="did" id="did" />
	
		<div id="role_tab" class="easyui-tabs" data-options="tabWidth:112" style="width:auto;height:520px">
			<div title="操作角色管理" style="padding:10px">
<!-- 				<div style="padding:5px">
					<div>
					角色名称：
						<input class="easyui-searchbox" data-options="prompt:'输入',searcher:doSearchOp" style="width:200px" value=""></input>
						
						<a href="javascript:void(0)" onclick="showAdd(0)" class="easyui-linkbutton" style="float:right;margin-right:0px">&nbsp&nbsp&nbsp添加操作角色&nbsp&nbsp&nbsp</a>
					</div>
				</div>
				<table id="tb_op" class="easyui-datagrid" style="width: auto; height: auto"
					data-options="rownumbers:true,singleSelect:true,onClickRow: onClickRow, locales:'ca'">
				</table> -->
				
					<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 35px;">
				<table id="querytable" cellspacing="0" cellpadding="5px"
					style="text-align: left; height: auto; margin: 0px;">
					<tr>
						<td><input class="easyui-searchbox"
							data-options="prompt:'输入',menu:'#mm',searcher:doSearch"
							style="width: 500px; height: 25px" /></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="tb_op" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="mm">
		<!-- <div data-options="name:'all',iconCls:'icon-ok'">All News</div>  -->
<!-- 		<div data-options="name:'0'">全部查询</div> -->
		<div data-options="name:'1'">按名称查询</div>
<!-- 		<div data-options="name:'2'">按姓名查询</div> -->
<!-- 		<div data-options="name:'3'">按手机号查询</div> -->
<!-- 		<div data-options="name:'4'">按公司查询</div> -->
<!-- 		<div data-options="name:'5'">按用户编号查询</div> -->
	</div>
	<form id="hiddenForm" method="post">
		<input id="searchValue" type="text" /> <input id="searchType"
			type="text" />
	</form>
	<div id="content" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">

			<c:if
				test="${fn:contains(session.resourceList, '/authority/manager/add.do')}">
				<a class="easyui-linkbutton" iconCls="icon-add"
					plain="true" style="float: right" onclick="javascript:add(0);"><font
					style="font-weight: bold">添加角色&nbsp;</font></a>
			</c:if>
 				
		</div>
	</div>
			</div>
			<div title="数据角色管理" style="padding:10px">
									<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 35px;">
				<table id="querytable" cellspacing="0" cellpadding="5px"
					style="text-align: left; height: auto; margin: 0px;">
					<tr>
						<td><input class="easyui-searchbox"
							data-options="prompt:'输入',menu:'#mm1',searcher:doSearch1"
							style="width: 500px; height: 25px" /></td1>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',title:''"
				style="border: green 0px solid;">
				<table id="tb_data" style="padding: 1px; boder: black 0px solid;"
					data-options="fit:true,border:true">
				</table>
			</div>
		</div>
	</div>
	<div id="mm1">
		<!-- <div data-options="name:'all',iconCls:'icon-ok'">All News</div>  -->
<!-- 		<div data-options="name:'0'">全部查询</div> -->
		<div data-options="name:'1'">按名称查询</div>
<!-- 		<div data-options="name:'2'">按姓名查询</div> -->
<!-- 		<div data-options="name:'3'">按手机号查询</div> -->
<!-- 		<div data-options="name:'4'">按公司查询</div> -->
<!-- 		<div data-options="name:'5'">按用户编号查询</div> -->
	</div>
	<form id="hiddenForm" method="post">
		<input id="searchValue1" type="text" /> <input id="searchType1"
			type="text" />
	</form>
	<div id="content1" style="padding: 5px; height: 20px">
		<div style="margin-bottom: 5px;">
			<c:if test="${fn:contains(session.resourceList, '/authority/manager/add.do')}">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add"
					plain="true" style="float: right" onclick="javascript:add(1);"><font
					style="font-weight: bold">添加角色&nbsp;</font></a>
			</c:if>
 				
		</div>
	</div>
			</div>
		</div>
		
		<div id="dlg" class="easyui-dialog" closed="true" buttons="#dlg-buttons">
		<form id="viewform" method="post">
				<input type="hidden" name="juese.id" id="id" />
				<input type="hidden" name="type" id="type"/>
				<table style="width:480px;height:100px;" >
					<tr >
						<td class="left_labelstyle" style="width:70px;" >角色名称：</td>
						<td style="width:130px;">
							<input id="name" name="juese.name" style="width:130px;float:left" class="easyui-validatebox" data-options="required:true"/>
						</td>
					</tr>
					<tr >
						<td class="left_labelstyle" style="width:70px;" >备注：</td>
						<td style="width:130px;">
							<input id="remark" name="juese.remark" class="easyui-validatebox textbox" style="width:130px;float:left"/>
						</td>
					</tr>
				</table>
				
					<div style="position:absolute;top:350px;left: 0%;border:0px red solid;width:170%;height:180px;">
						<td colspan="4" style="text-align:center">
							<a href='javascript:void(0)' onclick="saveJuese()" class="easyui-linkbutton l-btn" style="width:90px" >&nbsp;确定&nbsp;</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href='javascript:void(0)' onclick="closeViewDlg()" class="easyui-linkbutton l-btn" style="width:90px">&nbsp;取消&nbsp;</a>
						</td> 
				</div>
		</form>
		</div>
		
<!-- 		<div id="dlg-buttons"> -->
<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 			onclick="saveJuese();" iconcls="icon-save">保存</a>  -->
<!-- 		<a href="javascript:void(0)" class="easyui-linkbutton" -->
<!-- 			onclick="javascript:$('#dlg').dialog('close')" iconcls="icon-cancel">取消</a> -->
<!-- 	</div> -->
	
	
	
		<div id="dataTree"></div>
		
		<div id="do" class="easyui-dialog" closed="true" buttons="#dlg-buttons">
				  <div id="jd_content_main">
	        <div id="jd_content_left">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		
           <div id="jd_content_center" style="width:3px;"></div>
           			 <form method="post" action="<%=basePath%>role/resource_updateResourceByRoleId.do" onSubmit="return checkFroms();">
           			 	<input type="hidden" name = "codes" id = "companyIds" />
           				<tr align="center"> <input type="submit"  class="dl" value="提交"><input type="button"  class="dl" value="全选" onclick="checkall();"> <input type="button"  class="dl" value="全不选" onclick="checknoall();"></tr>
           			</form>
       		 </div>
		</div>
		
		<div id="data" class="easyui-dialog" closed="true" buttons="#dlg-buttons">
			 <div id="jd_content_main1">
	        <div id="jd_content_left1">
				<ul id="treeDemo1" class="ztree"></ul>
			</div>
		
           <div id="jd_content_center1" style="width:3px;"></div>
           			 <form method="post" action="<%=basePath%>customer/updateRoleGroup.do" onSubmit="return checkFroms1();" id="dataform">
           			 	<input type="hidden" name = "roleId" value="${id}" id="dataId"/>
          			 	<input type="hidden" name = "groupIds" id = "companyIds1" />
           				<tr align="center"> <input type="submit"  class="dl" value="提交"> </tr>
           			</form>
       		 </div>
		</div>
		<script>
		function loaddone() {
			var hiddenmsg = parent.document.getElementById("hiddenmsg");
			hiddenmsg.style.display = "none";
		}
			var tb_index = -1;
			$('.easyui-tree').tree();
			$('.easyui-tabs').tabs();
			$('.easyui-datebox').datebox();
			$('.easyui-linkbutton').linkbutton();
			$('.easyui-combobox').combobox();
			$('.easyui-searchbox').searchbox();
			$('.easyui-validatebox').validatebox();
			var addDlg;
			var editDlg;
			var viewDlg;
			var opDlg;
			var dataDlg;
			var type = 0;
			var id = 0;
			var roleInfo = {};

			var dataLoader = {};
			var dataLoader1 = {};
			var page_path = '${pageContext.request.contextPath}/authority/role/';
		// 	$(function(){
				dataLoader.load=function(searchValue){
					var searchValue = $("#searchValue").val();
					str_url = page_path + 'search.do?type=0';
					$('#tb_op').datagrid({
						url: str_url,
						queryParams: {searchValue:searchValue},
						loadMsg : '数据处理中，请稍后....',
						height: 'auto',
						fitColumns: true,
						pagination: true,
						fit:true,
						toolbar : '#content',
						columns:[[
						          {field:'operation',title:'操作',width:100, align:'center'},
						          {field:'name',title:'角色名称',width:100, align:'center'},
						          {field:'remark',title:'备注',width:250,align:'center'},
						]],
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
				};
				
				dataLoader1.load=function(searchValue){
					var searchValue = $("#searchValue1").val();
					str_url = page_path + 'search.do?type=1&searchValue=' + searchValue;
					$('#tb_data').datagrid({
						url: str_url,
						height: 'auto',
						fitColumns: true,
						pagination: true,
						toolbar : '#content1',
						columns:[[
						          {field:'operation',title:'操作',width:100, align:'center'},
						          {field:'name',title:'角色名称',width:100, align:'center'},
						          {field:'remark',title:'备注',width:250,align:'center'},
						]],
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
				};
		// 	});
		
			function showAdd(type) {
				this.type = type;
				this.id = 0;
				addDlg = $('#dlg');
				addDlg.load(page_path + "open.do?method=add");
			}
			
			function doSearch(value, type) {
				alert(1);
				$('#searchValue').val(value);
				$('#searchType').val(type);
				dataLoader.load();
			
			}
			
			function doSearch1(value, type) {
				$('#searchValue1').val(value);
				$('#searchType1').val(type);
				dataLoader1.load();
			}
			
			function showView(id, type) {
				$.getJSON(page_path + 'viewJuese.do', {
					id : id,
					type :type
				}, function(result) {
				 	$('#dlg #name').val(result.name);
					$('#dlg #remark').val(result.remark);
					$("#dlg #name").attr("disabled",true);
					$("#dlg #remark").attr("disabled",true);
						$('#dlg').dialog({
							title:'查看角色',
							width: 550,																																																																																																																																																																	
							height: 330,
							modal:true,
							closed: false,    
							buttons: [{
								text:'关闭',
								iconCls:'icon-ok',
								handler:closeViewDlg
							}]
						});
				});
			}
			
			function showEdit(id, type) {
				$.getJSON(page_path + 'viewJuese.do', {
					id : id,
					type :type
				}, function(result) {
				 	$('#dlg #name').val(result.name);
					$('#dlg #remark').val(result.remark);
					$('#dlg #id').val(result.id);
					$('#dlg #type').val(type);
					$("#dlg #name").attr("disabled",false);
					$("#dlg #remark").attr("disabled",false);
					$('#dlg').dialog({ 
						title:'修改角色',
						width: 550,																																																																																																																																																																	
						height: 330,
					    modal: true,
					    closed:false,
					    buttons: [{
							text:'保存',
							iconCls:'icon-ok',
							handler:saveRole,
						}, {
							text:'取消',
							iconCls:'icon-cancel',
							handler:closeViewDlg
						}],
					});    
				});
			}
			
			function closeViewDlg() {
				$('#dlg').dialog('close');
			}
			
			  function addRole(type) {
		          $("#viewform").form("submit", {
		              url: '<%=basePath%>authority/manager/addJuese.do',
		              onsubmit: function () {
		                  return $(this).form("validate");
		              },
		              success: function (result) {
		               $.messager.alert("提示信息", "操作成功");
		                  $("#dlg").dialog("close");
// 		                  $('#dg').datagrid('load');
		              } 
		          });
		      }
			  
			function saveRole(){
				var isValid_name = $('#dlg #name').validatebox('isValid');
				var isValid_remark = $('#dlg #remark').validatebox('isValid');
				if (!isValid_name) {
					alert("名称输入不合法！");
					return false;
				}
				if ( !isValid_remark ) {
					alert("备注输入不合法！");
					return false;
				}						
				$('#viewform').ajaxSubmit({
					url: page_path + 'updateJuese.do',
					type: "post", 
					dataType: "json", 
					contentType: "application/x-www-form-urlencoded; charset=utf-8", 
					success: function(result) {
						if (result.state != 0) {
							$.messager.show({
								title : '提示',
								msg : '修改用户信息成功。',
								showType:'fade',
								style:{
									right:'',
									bottom:''
								}
							});
							closeViewDlg();
							if($('#dlg #type').val()==0){
								dataLoader.load();
							}else{
								dataLoader1.load();
							}
						} else {
							$.messager.alert('修改用户信息失败');
						}
					},
					error: function() {

					},
					complete: function() {

					}
				});
			}
			
			function deleteRole(id, type) {
				$.messager.defaults = {
						ok : "确  定",
						cancel : "取  消"
					};
				$.messager.confirm('删除', '您确定要删除吗？', function(r){
					if (r){
						$.ajax({
							url: page_path + "delete.do?id=" + id+"&type="+type,
				            success : function(data){
								try {
					            	ret = data;
					                if (ret.err_code == 94) {
					                   parent.$.messager.alert('删除', ret.err_msg, 'error');
					                }
					            } finally {
// 					            	closeForm();
// 					            	refreshList();
									window.location.reload();
					            }
				            }
						});
					}
				});
			};
			
			function onClickRow(index) {
				tb_index = index;
			}
		
			function doSearchOp(value){
				dataLoader.load(value);
			}
			
			function doSearchData(value){
				dataLoader1.load(value);
			}
			
			function refreshList() {
				dataLoader.load();
				dataLoader1.load();
			}
			
			var node_list = [];
			var chk_list = [];
			var item_list = [];
			var rid = 0;
			
			function showItems(id, type) {
				this.type = type;
				this.id = id;
				node_list = [];
				chk_list = [];
				item_list = [];

				if (type == 0) {
					$('#do').dialog({
						title:'权限分配',
						width: 550,																																																																																																																																																																	
						height: 330,
						closed: false,    
						modal:true
					});
					createDoTree(id);
				} else {
					$('#data').dialog({
						title:'数据权限分配',
						width: 550,																																																																																																																																																																	
						height: 330,
						closed: false,    
						modal:true
					});
				  createDataTree(id);
				}
				rid = id;
			}
			
			function showTree() {
				var innerhtml = '<ul class="easyui-tree" id="trees" data-options="lines:true, animate:true, checkbox:true';
				if (type == 0) {
					innerhtml += ', onCheck:checkNode';
				}
				innerhtml += '">';
				var temp = -1;
				for (var i = 0; i < item_list.length; i++) {
					if (temp < item_list[i].level && i != 0) {
						innerhtml += "<ul>";
					}

					if (temp == item_list[i].level) {
						innerhtml += "</li>";
					}
					
					if (temp > item_list[i].level) {
						innerhtml += "</li>";
						for (var j = 0; j < temp - item_list[i].level; j++) {
							innerhtml += "</ul></li>";
						}
					}

					innerhtml += '<li ';
					if (item_list[i].chk == 1)
						innerhtml += 'data-options="checked:true"';
					
					innerhtml += '><span>' + item_list[i].name + '</span>';
					
					
					temp = item_list[i].level;
				}

				for (var i = 0; i < temp; i++) {
					innerhtml += "</li>";
					if (i != temp - 1)
						innerhtml += "</ul>";
				}
				
				innerhtml += "</ul>";
				$("#opers").html(innerhtml);
				$('.easyui-tree').tree();
				$('#trees').tree('collapseAll');
				$(".tree-icon").removeClass("tree-folder");
				$(".tree-icon").removeClass("tree-file");
				refreshTree();
			}
			
			function refreshTree() {
				getNodeList();
				for (var i = 0; i < node_list.length; i++) {
					var nodeid = "#" + node_list[i].domId + " .tree-checkbox";
					$(nodeid).removeClass("tree-checkbox0");
					$(nodeid).removeClass("tree-checkbox1");
					$(nodeid).removeClass("tree-checkbox2");
					if (item_list[i].chk == 1) {
						$(nodeid).addClass("tree-checkbox1");
					} else if (item_list[i].chk == 2) {
						$(nodeid).addClass("tree-checkbox2");
					} else {
						$(nodeid).addClass("tree-checkbox0");
					}	
				}
			}
			
			function getNodeList() {
				var roots = $("#trees").tree("getRoots");
				for (var i = 0; i < roots.length; i++) {
					var nodes = $("#trees").tree("getChildren", parent.target);
					for (var i = 0; i < nodes.length; i++) {
						node_list[node_list.length] = nodes[i];
					}
				}
			}
			
			refreshList();
			
			function add(type) {
				$('#dlg').dialog({ 
					title:'添加角色',
					width: 550,																																																																																																																																																																	
					height: 330,
				    closed: false,    
				    modal: true,
				    buttons: [{
						text:'保存',
						iconCls:'icon-ok',
						handler:saveJuese,
					}, {
						text:'取消',
						iconCls:'icon-cancel',
						handler:closeViewDlg
					}],
				});    
			 	$('#dlg #name').val('');
				$('#dlg #remark').val('');
				$('#dlg #type').val(type);
			};
			
			  function saveJuese() {
		          $("#viewform").form("submit", {
		              url: '<%=basePath%>authority/manager/addJuese.do',
		              onsubmit: function () {
		                  return $(this).form("validate");
		              },
		              success: function (result) {
		            	  if(result==1){
		            		  alert("已经有相同的角色名!");
		            	  }else{
		            		   $.messager.alert("提示信息", "操作成功");
				                  $("#dlg").dialog("close");
//		 		                  $('#dg').datagrid('load');
									if($('#dlg #type').val()==0){
										dataLoader.load();
									}else{
										dataLoader1.load();
									}
				              } 
		            	  }
		          });
		      }
		
		function createDoTree(id){
			$("#did").val(id);
			setting.async.url=getUrl();
			$.fn.zTree.init($("#treeDemo"), setting);
		}
		
		function getUrl(){
			var u = "<%=basePath%>role/resource_findResourceByRoleId.do?roleId="+$("#did").val();
			return u;
		}
		
	 	var setting = {
			async: {
				enable: true,
			 	url: getUrl()
			},
			edit: {
				enable: false,
				editNameSelectAll: true
			},
			check: {
				enable: true,
				chkboxType:{ "Y" : "s","N":"ps"},
				chkStyle:"checkbox"
			},
			data: {
				key: {
					name:"funcName"
				},
				simpleData: {
					enable: true,
					idKey: 'code',
					pIdKey: 'parent'
				}
			},
			callback: {
				onCheck:onCheck
			}
		};
		
	  function onCheck(){
            var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
            nodes=treeObj.getCheckedNodes(true),
            v="";
            for(var i=0;i<nodes.length;i++){
            v+=nodes[i].code + ",";
            }
			return v;            
       }
	  
	  function checknoall(){
		   var treeObj=$.fn.zTree.getZTreeObj("treeDemo");
		   treeObj.checkAllNodes(false);
	  }
       
	  function checkall(){
		  var treeObj=$.fn.zTree.getZTreeObj("treeDemo");
		   treeObj.checkAllNodes(true);
	  }
	  
	  function checkFroms(){
		  var i = onCheck();
		  alert(i);
		  if(i==""){
			  alert("至少选择一个目录！");
			return false;
			}
		$("#companyIds").val(i);
		return true;
	  }
	  
	  function createDataTree(id){
			$("#dataId").val(id);
			setting1.async.url=getUrl1();
			$.fn.zTree.init($("#treeDemo1"), setting1);
		}
		
		function getUrl1(){
			var u = "<%=basePath%>customer/findRoleGroup.do?roleId="+$("#dataId").val();
			return u;
		}
		
		var setting1 = {
				async: {
					enable: true,
					url: getUrl1()
				},
				edit: {
					enable: false,
					editNameSelectAll: true
				},
				check: {
					enable: true,
					chkboxType:{ "Y" : "s","N":"ps"},
					chkStyle:"checkbox"
				},
				data: {
					key: {
						name:"group_name"
					},
					simpleData: {
						enable: true,
						idKey: 'id',
						pIdKey: 'deleted'
					}
				},
				callback: {
					onCheck:onCheck
				}
			};
			
		  function onCheck1(){
	            var treeObj=$.fn.zTree.getZTreeObj("treeDemo1"),
	            nodes=treeObj.getCheckedNodes(true),
	             v="";
	            for(var i=0;i<nodes.length;i++){
	            	v+=nodes[i].id + ",";
	            }
				return v;            
	       }																																			
	       
		  function checkFroms1(){
			  var i = onCheck1();
			  if(i==""){
				  alert("至少选择一个目录！");
				return false;
			}
			$("#companyIds1").val(i);
			
// 			return true;
				$("#dataform").ajaxSubmit({
					 url: '<%=basePath%>customer/updateRoleGroup.do',
		            success : function(){
		            	alert(result);
						  $.messager.alert("提示信息", "操作成功");
		                $("#data").dialog("close");
		                dataLoader1.load();
		            }
		        });
		  }
		  
		 if(${state}==1){
			alert(1);			 
			 doSearch1("",1);
		 }
		</script>
	</body>
</html>