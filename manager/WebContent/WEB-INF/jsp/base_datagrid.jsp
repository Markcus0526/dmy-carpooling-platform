<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
<head>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath %>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Basic DataGrid - jQuery EasyUI Demo</title>
<link href="css/default.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"
	href="js/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="js/themes/icon.css" />
<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.1.2.2.js"></script>

<script type="text/javascript">

function rdd(){
	alert("will load...");
	//$('#dg').datagrid('reload');
	alert("done");

}
</script>
</head>
<body onload="rdd()">
	<h2>Basic DataGrid</h2>
	<table class="easyui-datagrid" style="width: 400px; height: 250px"
		data-options="url:'json/testJson.action',fitColumns:true,singleSelect:true">
		<thead>
			<tr>
				<th data-options="field:'userId',width:100">userId</th>
				<th data-options="field:'userName',width:100">userName</th>
				<th data-options="field:'sex',width:100,align:'right'">sex</th>
			</tr>
		</thead>
	</table>
</body>
</html>