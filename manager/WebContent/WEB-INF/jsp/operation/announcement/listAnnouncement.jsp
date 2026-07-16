<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<base href="<%=basePath%>">
<!-- 与本页面直接相关的JavaScript脚本放置区域. -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/easyloader.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/roleitem.js" charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"
	charset="UTF-8"></script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css"
	type="text/css"></link>

<script type="text/javascript" charset="UTF-8">
   
	var datagrid;
	var rowid;
	
	//dataformatter
	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
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

	//公告有效性 radio按钮动作
	 function disable(){  
         $('#validateDatePicker').datebox('disable'); 
     }  
     function enable(){  
         $('#validateDatePicker').datebox('enable');  
     }  
     
	function searchButtonClick(){
		
	    var ps_city = $('#ps_citySelect').combobox('getValue');
	    var validate = $('#validateSelect').combobox('getValue');
	    var id = $('#idInput').val();
	    var publisher = $('#publisherInput').val();
	    var keyword = $('#keywordInput').val();
	    var range = $('#rangeSelect').combobox('getValue');
	    var searchRange = $('#searchRangeSelect').combobox('getValue');
	    
	    $('#dg').datagrid('load',{
	    ps_city : ps_city,
	    validate : validate,
	    id : id,
	    publisher : publisher,
	    keyword : keyword,
	    range : range,
	    searchRange : searchRange
	    });
		
	}
	
	
	 function removeAnnomucement(){
		var row = $('#dg').datagrid('getSelected');
		var rowid = row.id;
		var rowIndex=$('#dg').datagrid('getRowIndex',$('#dg').datagrid('getSelected'));
		$.ajax({
		   type: 'POST',
		   dataType:'json',
		   url: '<%=basePath%>operation/announcement/deleteAnnouncementById.do',
		   data:{
			   id:rowid
		   },
		   success: function(data){
			   
		  	//$('#dg').datagrid('deleteRow',rowIndex);
		  	//$('#dg').datagrid('appendRow',row);
		  	$('#dg').datagrid('load');
		  	$('#w').dialog('close');
		 
		   },
		   error:function(){
			   alert('error');
		   }
		});
		  
	}

	function addAnnomucement(){
		$.ajax({
			type:'POST',
			//dataType:'json',
			url:'<%=basePath%>operation/announcement/editAnnouncement.do',
			data:$('#addAnnouncementForm').serialize(),
			success:function(data){
				
				$('#dg').datagrid('load');
				
				$('#gg').dialog('close');
			}
		});
	}
	
	function openSure(){
		$('#w').dialog('open');
	}
	
	function openPost(){
		$('#gg').dialog('open');
	}

	
	$(function(){
		
		datagrid=$('#dg').datagrid({		
	
							url	:'<%=basePath%>operation/announcement/searchAnnouncement.do',
							method : 'POST',
							iconCls : 'icon-save',
							pagination : true,
							pageNumber : 1,
							pageSize : 5,
							//fit:true,
							pageList : [ 5,10, 20, 50 ],
							fitColumns : true,
							nowrap : false,
							border : false,
							idFiled : 'ID',
							checkOnSelect : true,
							striped : true,
							columns : [ [
									{
										field : 'operation',
										title : '操作',
										width : 200,
										align : 'center',
										formatter : function(value, row, index) {
											rowid = row.id;
											var str = '';
											str += '<a href="javascript:openSure()">撤销</a> &nbsp';
											return str;
											if (row.id) {
												return row.id;
											}
										}
									},
									{
										field : 'id',
										title : '公告编号',
										width : 100,
										align : 'center'
									},
									{
										field : 'ps_city',
										title : '范围',
										width : 200,
										align : 'center'
									},
									{
										field : 'range',
										title : '对象',
										width : 200,
										align : 'center'
									},
									{
										field : 'title',
										title : '标题',
										width : 200,
										align : 'center'
									},
									{
										field : 'content',
										title : '内容',
										width : 400,
										align : 'center'
									},
									{
										field : 'validate',
										title : '有效期',
										width : 200,
										align : 'center'
									},
									{
										field : 'publisher',
										title : '发布人',
										width : 200,
										align : 'center'
									},
									{
										field : 'ps_date',
										title : '发布日期',
										width : 200,
										align : 'center'
									} ] ]
						});

		var ps_citySelect = $('#ps_citySelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'all',
			data : [ {
				label : '全局',
				value : 'all'
			}, {
				label : '北京市',
				value : '北京市'
			}, {
				label : '上海市',
				value : '上海市'
			}, {
				label : '广州市',
				value : '广州市'
			} ]
		});

		var validateSelect = $('#validateSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'all',
			data : [ {
				label : '全部',
				value : 'all'
			}, {
				label : '有效',
				value : 'valid'
			}, {
				label : '过期',
				value : 'invalid'
			} ]
		});

		var rangeSelect = $('#rangeSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'all',
			data : [ {
				label : '全部',
				value : 'all'
			}, {
				label : '车主',
				value : 'driver'
			} ]
		});

		var searchRangeSelect = $('#searchRangeSelect').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'all',
			data : [ {
				label : '全部',
				value : 'all'
			}, {
				label : '标题',
				value : 'title'
			}, {
				label : '内容',
				value : 'content'
			} ]
		});
		var ps_citySelect1 = $('#ps_citySelect1').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : 'all',
			data : [ {
				label : '全局',
				value : 'all'
			}, {
				label : '北京市',
				value : '北京市'
			}, {
				label : '上海市',
				value : '上海市'
			}, {
				label : '广州市',
				value : '广州市'
			} ]
		});
		
		
		var rangeSelect1 = $('#rangeSelect1').combobox({
			url : '',
			valueField : 'value',
			textField : 'label',
			value : '0',
			data : [ {
				label : '全部',
				value : '0'
			}, {
				label : '车主',
				value : '1'
			} ]
		});		
	});
	/**
	 * 跳转至添加页面
	 * @param {Object} formName表单名称
	 * @param {Object} editTypeHidden编辑状态隐藏域名称
	 */
/*	function jumpToAddPage(formName, editTypeHidden) {
		var form = document.getElementById(formName);
		document.getElementById(editTypeHidden).value = "add";
		if (form != null) {
			window
					.open(
							"",
							"mkx",
							"height=400,width=600,top=100,left=50,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
			form.target = "mkx";
			form.submit();
		}
	}
*/



function loaddone() {
	var hiddenmsg = parent.document.getElementById("hiddenmsg");
	hiddenmsg.style.display = "none";
}

</script>
</head>

<body onload="loaddone()">
	<form id="searchForm" class="searchForm">
		<table style="margin-left: 50px">
			<tr>
				<td width="300px">公告范围:<input id="ps_citySelect"
					name="announcement.ps_city"></td>
				<td>公告有效性:<input id="validateSelect"
					name="announcement.validate"></td>
			</tr>
			<tr>
				<td>公告编号:<input id="idInput" name="announcement.id"></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;发布人:<input id="publisherInput"
					name="announcement.publisher"></td>
			</tr>
			<tr>
				<td>&nbsp;&nbsp;关键字:<input id="keywordInput" name="keyword"></td>
				<td>&nbsp;&nbsp;公告对象:<input id="rangeSelect"
					name="announcement.range"></td>
			</tr>
			<tr>
				<td>检索范围:<input id="searchRangeSelect" name="searchRange"></td>
				<td align="right"><a id="btn" class="easyui-linkbutton"
					data-options="iconCls:'icon-search'" onclick="searchButtonClick()">查询</a></td>
			</tr>
		</table>
	</form>
	<div style="margin: 5px">
		<div>
			<a href="#" onclick="javascript:report();"
				class="easyui-linkbutton l-btn" style="float: right;"
				iconCls="icon-xlx">导出Excel </a> <a onclick="javascript:openPost();"
				class="easyui-linkbutton l-btn" style="float: right;">&nbsp;发布新公告&nbsp;
			</a>
		</div>
	</div>
	<br>
	<div id="list" style="padding: 10px">
		<table id="dg" class="easyui-datagrid"
			style="width: auto; height: auto"
			data-options="singleSelect:true, pagination:true">
		</table>
	</div>


	<!-- 模式窗口 ：撤销确认窗口-->
	<div id="w" class="easyui-dialog" data-options="modal:true,closed:true"
		style="width: 300px; height: 200px; padding: 10px;">
		<td>该操作不可逆，是否撤销？
		<td>
			<div style="margin: 20px 0;">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:removeAnnomucement();">确定</a> <a
					href="javascript:void(0)" class="easyui-linkbutton"
					onclick="$('#w').dialog('close')">取消</a>
			</div>
	</div>

	<!-- 模式窗口：发布新公告 -->

	<div id="gg" class="easyui-dialog"
		data-options="modal:true,closed:true"
		style="width: 680px; margin: 5px 5px 5px 5px; top: 50PX;">
		<form id="addAnnouncementForm">
			<div>
				<input id="recourseInfo.bussi_type" name="recourseInfo.bussi_type"
					value="1" type="hidden" />
				<table cellpadding="1">
					<!-- <s:hidden name="recourseInfo.bussi_type" value = "1" /> -->

					<!-- <tr>
							<td class="left_labelstyle">公告编号：</td>
							<td><input id="idInput" name="announcementId"></td>
						</tr> -->
					<tr>
						<td class="right_labelstyle">公告有效性:</td>
						<td><input id="validateLong" type="radio" name="validateBool"
							checked="checked" value="false" onclick="javascipt:disable();">长期有效<input
							id="validateShort" type="radio" name="validateBool" value="true"
							onclick="enable();">设定有效期</td>
					</tr>
					<tr>
						<td class="right_labelstyle"></td>
						<td><input id="validateDatePicker" class="easyui-datebox"
							name="validate"
							data-options="formatter:myformatter,parser:myparser,disabled:true"></td>
					</tr>
					<tr>
						<td class="left_labelstyle">公告范围:</td>
						<td><input id="ps_citySelect1" name="ps_city"></td>
					</tr>
					<tr>
						<td class="right_labelstyle">公告对象:</td>
						<td><input id="rangeSelect1" name="range"></td>
					</tr>
					<tr>
						<td class="left_labelstyle">公告标题:</td>
						<td><input id="titleInput" name="title"
							class="easyui-validatebox textbox" data-options="required:true"></td>
					</tr>
					<tr>
						<td class="right_labelstyle">公告内容:</td>
						<td><textarea id="connent" name="content"
								style="width: 385px; height: 50px;" data-options="required:true"></textarea></td>
					</tr>
				</table>
				<div style="text-align: center; padding: 5px">
					<a class="easyui-linkbutton "
						onclick="javascript:addAnnomucement();" style="width: 80px;">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton "
						onclick="$('#gg').dialog('close');">取消</a>
				</div>
			</div>
		</form>
	</div>


	<!-- 表单名称应与功能相对应，一般除特殊情况下不对表单名称做特殊处理，如需特殊处理，应避免名称冲突.-->

	<!-- 删除选中的数据 -->
	<form action="operation/announcement/deleteAnnouncementById.action"
		id="deleteInfoData" method="post">
		<input type="hidden" name="checkedRows" id="deleteCheckedRowsHidden" />
	</form>

	<!-- 跳转至项目信息编辑页面. -->
	<form action="operation/announcement/toEditAnnouncementPage.action"
		id="toEditPage" method="post">
		<input type="hidden" name="editType" id="editTypeHidden" /> <input
			type="hidden" name="announcementId" id="primaryIdHidden" />
	</form>

	<!-- 导出项目信息excel. -->
	<form action="operation/announcement/announcementExcel.action"
		id="exportExcel" method="post">
		<input type="hidden" name="displayColumns" id="displayColumnsHidden" />
	</form>

	<!-- 根据指定的当前页查询信息. -->
	<form action="operation/announcement/searchAnnouncement.action"
		id="searchByPageCurrent" method="post">
		<input type="hidden" name="pageCurrent" id="pageCurrentHidden" />
	</form>


	<!-- 显示所有数据 -->
	<form id="operation/announcement/showAllDataAnnouncementId"
		action="showAllDataAnnouncement.action" method="post"></form>

</body>
</html>

