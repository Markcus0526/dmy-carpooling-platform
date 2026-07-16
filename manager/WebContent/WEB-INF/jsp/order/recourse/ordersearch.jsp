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
<br />
<h2 class="page-title txt-color-blueDark">
	<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp集团客户管理
</h2>
<hr>
<form id="searchForm">
	<s:hidden name="orderSearchItem.usercode" id="usercode" />
	<s:hidden name="orderSearchItem.userphone" id="userphone" />
	<div style="margin-left: 20px;">
		<table style="width: auto;">
			<tr>
				<td><select name="is_id_phone" id="is_id_phone"
					onchange="javascript:show_id_phone();">
						<option selected="selected" value="1">客户ID</option>
						<option value="2">客户手机</option>
				</select></td>
				<td><s:textfield disabled="true"
						name="orderSearchItem.phone_id" id="phone_id" cssClass="textbox"
						cssStyle="width:200px;" theme="simple"></s:textfield></td>
				<td>客户姓名：</td>
				<td><s:textfield name="orderSearchItem.customer_name"
						id="customer_name" cssClass="textbox" cssStyle="width:200px;"
						disabled="true" theme="simple"></s:textfield></td>
			</tr>
			<tr>
				<td>成交时间：</td>
				<td><s:textfield name="orderSearchItem.start_time"
						cssClass="easyui-datetimebox" data-options="formatter:myformatter"
						style="width:95px" theme="simple"></s:textfield>~ <s:textfield
						name="orderSearchItem.end_time" cssClass="easyui-datetimebox"
						data-options="formatter:myformatter" style="width:95px"
						theme="simple"></s:textfield></td>
				<td>客户身份：</td>
				<td><input type="checkbox" checked="checked"
					name="orderSearchItem.identity" value="1">乘客 <input
					type="checkbox" checked="checked" name="orderSearchItem.identity"
					value="2" style="margin-left: 20px;">车主</td>

			</tr>
		</table>

		<table>
			<tr>
				<td>订单类型：</td>
				<td><input type="checkbox" checked="checked"
					name="orderSearchItem.order_type" value="1">单次拼车 <input
					type="checkbox" checked="checked" name="orderSearchItem.order_type"
					value="2" style="margin-left: 20px;">上下班拼车 <input
					type="checkbox" checked="checked" name="orderSearchItem.order_type"
					value="3" style="margin-left: 20px;">长途拼车</td>
				<td><a href="#" onclick="btnSearch();"
					class="easyui-linkbutton l-btn" iconCls="icon-search">&nbsp;查询&nbsp;</a>
				</td>
			</tr>
			<tr>
				<td>订单状态：</td>
				<td><input type="checkbox" checked="checked"
					name="orderSearchItem.order_state" value="1">已完成 <input
					type="checkbox" checked="checked"
					name="orderSearchItem.order_state" value="2"
					style="margin-left: 31px;">未完成</td>
			</tr>
		</table>
		订单编号：
		<s:textfield name="orderSearchItem.order_num" cssClass="textbox"
			cssStyle="width:200px;" theme="simple"></s:textfield>
	</div>
</form>
<div id="list" style="padding: 10px">
	<table id="dg" class="easyui-datagrid"
		style="width: auto; height: auto"
		data-options="singleSelect:true, pagination:true">
	</table>
</div>

<script>
	var page_path = '${pageContext.request.contextPath}/order/recourse/';
	var content = "";
	$('#phone_id').val($('#usercode').val());
	$('.easyui-linkbutton').linkbutton();
// 	$('.easyui-combobox').combobox();
	$('.easyui-datetimebox').datetimebox();
	
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+h+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
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
	function show_id_phone()
	{
		var flag = document.getElementById("is_id_phone").value;
		if(flag == 1)
		{
			$('#phone_id').val($('#usercode').val());
		}else{
			$('#phone_id').val($('#userphone').val());
		}
	}
	
	var dataLoader = {};
	dataLoader.load=function(){
		createContent();
		strurl = page_path + 'searchOrder.do?' + content;
		//alert(strurl);
		$('#dg').datagrid({
	         url: strurl,
	         loadMsg : '数据处理中，请稍后....',
	         height: 'auto',
	         pagination: true,
	         fitColumns: true,	         	
			 
			columns:[[
	             {field:'operMenu',width:100,align:'center',title:'操作'}, 
	             {field:'orderNum',width:150,align:'center',title:'订单编号'},
	             {field:'startCity',width:150,align:'center',title:'所在城市'},
	             {field:'appointType',width:100,align:'center',title:'订单类型'},  
	             {field:'guestName',width:70,align:'center',title:'乘客姓名'},
	             {field:'guestPhone',width:150,align:'center',title:'乘客手机'},
	             {field:'carName',width:70,align:'center',title:'车主姓名'},
	             {field:'carPhone',width:70,align:'center',title:'车主手机'},
	             {field:'appointStatus',width:70,align:'center',title:'订单状态'},
	             {field:'statusTime',width:200,align:'center',title:'状态时间'},
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
	};

	function createContent()
	{
		content="";
		var tags = ["input","select","textarea"];
		var form = $("#searchForm");
		var params = [];
		for(var i in tags){
			var collection = form.find(tags[i]);
			for(var j=0;j<collection.length;j++)
			{
				var item = $(collection[j]);
				var name = item.prop("name");
				
				if (name != ""){					
					if (tags[i]=="input") {
						if (item.prop("type")=="checkbox") {							
							if (!item.prop("checked")){
								continue;
							}
						}
					}
					
					var values = params[name];
					if (!values) {
						values = [];
					}					
					values.push(encodeURI(item.val()));
					params[name]=values;
					
				}
			}
		}
				
		
		for(i in params)
		{
			if (params[i].length>0)
			{
				if (content != "") content += "&";
				if (params[i].length>1){
					var temp = "";
					for (j=0;j<params[i].length;j++){
						if (j>0){
							temp += ",";
						}
						temp += params[i][j];						
					}
					content += i+"="+temp;
				} else {
					content += i+"="+params[i][0];
				}
			}
		}
	}
	function btnSearch() {
		dataLoader.load();
	}
	
	function select(id)
	{
		var temp = $('#dg').datagrid('getSelected');
	//	alert(temp['orderNum']);
		if(edit_add == 1)
		$('#view').load(page_path+"add.do?orderNum="+temp['orderNum']);
		if(edit_add == 2)
			$('#view').load(page_path+"edit.do?orderNum="+temp['orderNum']);
	}
	
	function viewInDetail(id)
	{
		var customer_name = $('#customer_name').val();
		
		$('#searchForm').ajaxSubmit ({
			url : page_path+"saveDate.do?method=saveSearchpageInfo&orderSearchItem.customer_name="+customer_name,
			success: function(ret) {
				
			},
			error: function() {
				 //alert("error2");
			},
			complete: function(){
				 //alert("complete");
			}
		});
		$('#view').load(page_path + "viewInDetail.do?id="+id);
	}
	
		dataLoader.load();
</script>
