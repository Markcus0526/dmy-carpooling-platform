<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

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
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/outlook2.js'> </script>

<body onload="loaddone()">
	<div class="easyui-panel" data-options="fit:true"
		style="border: gray 1px solid; padding: 1px; margin: 0px;">
		<div class="easyui-layout" data-options="fit:true,border:true"
			style="border: red 0px solid;">
			<div data-options="region:'north',title:'',border:true"
				style="height: 195px;">
				<table id="querytable" cellspacing="0" cellpadding="5px"
					style="text-align: left; height: auto; margin: 0px;">
					<h3 class="page-title txt-color-blueDark">
						<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;个人邀请码全局设置
					</h3>
					<div style="height: 45px; padding: 10px;">
						<div style="padding-left: 10px;">
							<div class="fix_group">
								<label>乘客方返利：</label> <label id="lbl_profit_passenger">信息费*5%</label>
							</div>
							<div class="fix_group">
								<label>乘客方有效期：</label> <label id="lbl_limit_passenger">12个月</label>
							</div>
							<div class="fix_group">
								<label>乘客方有效次数：</label> <label id="lbl_count_passenger">无</label>
							</div>
						</div>
						<div style="clear: both; padding: 10px;">
							<div class="fix_group">
								<label>车主方返利：</label> <label id="lbl_profit_driver">信息费*10%</label>
							</div>
							<div class="fix_group">
								<label>乘客方有效期：</label> <label id="lbl_limit_driver">12个月</label>
							</div>
							<div class="fix_group">
								<label>乘客方有效次数：</label> <label id="lbl_count_driver">无</label>
							</div>
							<a href="#" onclick="javascript:showConfig(0);"
								class="easyui-linkbutton l-btn" style="float: right;">修改全局设置</a>
						</div>
					</div>
					<hr>
					<h3 class="page-title txt-color-blueDark">
						<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;非默认设置个人客户表
					</h3>
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
						<table id="dg" style="padding: 1px; boder: black 0px solid;"
							data-options="fit:true,border:true">
						</table>
					</div>
					</div>
				</table>
			</div>


			<div id="mm">
				<!-- <div data-options="name:'all',iconCls:'icon-ok'">All News</div>  -->
				<div data-options="name:'0'">全部查询</div>
				<div data-options="name:'1'">按用户名查询</div>
				<div data-options="name:'2'">按邀请码查询</div>
				<div data-options="name:'3'">按ID查询</div>
			</div>
			<form id="hiddenForm" method="post">
				<input id="searchValue" type="hidden" /> <input id="searchType"
					type="hidden" />
			</form>
			<div id="content" style="padding: 5px; height: 20px">
				<div style="margin-bottom: 5px;">
					<a href="#" class="easyui-linkbutton" iconCls="icon-xls"
						plain="true" style="float: right; margin-right: 5px;"><font
						style="font-weight: bold">导出Excel&nbsp;</font></a>
				</div>
				<div></div>
			</div>
			<div id="dlg"></div>
</body>
<script>
	$('.easyui-searchbox').searchbox();
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-validatebox').validatebox();

	var page_path = '${pageContext.request.contextPath}/spread/number/';
	var confDlg;
	var dataLoader = {};
	var content = {searchType : 0, searchValue : ""};
	//var searchType = 0;
	//var searchValue = "";
	var userInfo, conf, user_id;
	
	function loaddone(){
		var hiddenmsg = parent.document.getElementById("hiddenmsg"); 
		hiddenmsg.style.display="none";
	}
	refreshHeader();
	dataLoader.load = function() {
		params = {
				searchValue : $('#searchValue').val(),
				searchType : $('#searchType').val()
			};
		str_url = page_path + 'search.do';
		$('#dg').datagrid({
			url : str_url,
			queryParams : params,
			loadMsg : '数据处理中，请稍后....',
			pagination : true,
			remoteSort : false,
			striped : true,
			fitColumns : true,
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			idField : 'id',
			singleSelect:true,
			selectOnCheck : $(this).is(':checked'),
			checkOnSelect : $(this).is(':checked'),
			toolbar:'#content',
			
			columns : [ [
							{
								field : 'ck',
								checkbox : true
							},{field : 'operation',
								title : '操作',
								width : 150,
								align : 'center'
							}, 
							/* {
								field : 'action',
								title : '操作',
								width : 220,
								align : 'center',
								formatter : function(value, row, index) {
								var str = '';
									str += '<a href="javascript:detail('+row.id+')" target="_self">查看</a> &nbsp';
									str += '<a href="javascript:showConfig('+row.id+');">设置</a>';
										return str;
										if(row.id){return row.id}
								}
							}, */ {
								field : 'userid',
								title : '客户ID',
								width : 100,
								align : 'center'
							}, {
								field : 'username',
								title : '客户姓名',
								width : 100,
								align : 'center'
							}, {
								field : 'invitecode',
								title : '客户邀请码',
								width : 100,
								align : 'center'
							}, {
								field : 'profit_passenger',
								title : '乘客方返利',
								width : 100,
								align : 'center'
							}, {
								field : 'limit_passenger',
								title : '乘客方有效期',
								width : 100,
								align : 'center'
							}, {
								field : 'count_passenger',
								title : '乘客方有效次数',
								width : 100,
								align : 'center'
							}, {
								field : 'profit_driver',
								title : '车主方返利',
								width : 100,
								align : 'center'
							}, {
								field : 'limit_driver',
								title : '车主方有效期',
								width : 100,
								align : 'center'
							}, {
								field : 'count_driver',
								title : '车主方有效次数',
								width : 100,
								align : 'center'
							} ] ],

							onLoadSuccess : function() {
								$('#list table').show();
								parent.$.messager.progress('close');
							},

							onHeaderContextMenu : function(e, field) {
								e.preventDefault();
								if (!cmenu) {
									createColumnMenu();
								}
								cmenu.menu('show', {
									left : e.pageX,
									top : e.pageY
								});
							}
						});
	};

function doSearch(value, type) {
	//alert("value:"+value+" type:"+type);
	$('#searchValue').val(value);
	$('#searchType').val(type);
	dataLoader.load();
}
/* 	function report() {
		;
	}
 */
	function detail(id) {
		$.getJSON(page_path + 'getInfo.do', {id:id},
			function(result) {
					userInfo = result['infos'][0];
					$('#dlg').load(page_path + "view.do");
			});
	};
	
	function showConfig(id) {
		user_id = id;
		confDlg = $('#dlg');
		confDlg.load(page_path + "open.do?method=config");
	}
	
	function refreshHeader() {
		$.ajax({
			url: page_path + "getConfig.do?id=0",
			success: function(data) {
				conf = data.infos[0];
				if (conf.active_passenger == 1)
					$("#lbl_profit_passenger").text("信息费*" + conf.profit_passenger + "%");
				else
					$("#lbl_profit_passenger").text(conf.profit_passenger + "点");

				$("#lbl_limit_passenger").text(conf.limit_passenger + "个月");
				$("#lbl_count_passenger").text(conf.count_passenger);

				if (conf.active_driver == 1)
					$("#lbl_profit_driver").text("信息费*" + conf.profit_driver + "%");
				else
					$("#lbl_profit_driver").text(conf.profit_driver + "点");

				$("#lbl_limit_driver").text(conf.limit_driver + "个月");
				$("#lbl_count_driver").text(conf.count_driver);
				closeForm();
			}
		});
	}

	function closeForm() {
		confDlg = $('#dlg');
		confDlg.dialog('close');
		//$('#dlg').dialog('close');
		confDlg.html("");
	}

	function refreshList() {
		//dataLoader.load();
	}

	dataLoader.load();

// 	showPath();

	
</script>