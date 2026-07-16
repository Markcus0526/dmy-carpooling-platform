<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<title>site</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=basePath%>css/zTreeStyle/zTreeStyle.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.ztree.exedit-3.5.js"></script>

<script language="javascript">
	 $(document).ready(function(){
		 $.fn.zTree.init($("#treeDemo"), setting);
	}); 
	 
	 	var setting = {
			async: {
				enable: true,
			 	url: "<%=basePath%>role/resource_findResourceByRoleId.do?roleId=${roleId}"
			},
			edit: {
				enable: false,
				editNameSelectAll: true
			},
			check: {
				enable: true,
				chkboxType:{ "Y" : "","N":"s"},
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
       
	  function checkFroms(){
		  var i = onCheck();
		  if(i==""){
			top.Dialog.alert("至少选择一个目录！",function(){},250,80);
			return false;
		}
		$("#companyIds").val(i);
		return true;
	  }
	  
	  top.callbackstr="reload";
</script>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
	list-style-type: none;
}

a,img {
	border: 0;
}

body {
	font: 12px/180% Arial, Helvetica, sans-serif, "新宋体";
}

.iptgroup {
	width: 528px;
	height: 40px;
}

.iptgroup li {
	float: left;
	height: 30px;
	line-height: 30px;
	padding: 5px;
}

.iptgroup li .ipticon {
	background: url(<%=basePath%>blue/date_icon.gif) 98% 50% no-repeat;
	padding: 3px;
}

* {
	font-size: 12px;
	font-family: "宋体";
}

body {
	background: #fff;
	margin: 0px;
	padding: 0px;
	font-family: "宋体,Tahoma";
	overflow-x: hidden;
}

#jd_content_main {
	background: #fff;
	overflow: hidden;
	margin: 0 auto;
}

#jd_content_main span,#jd_content_main td {
	color: black;
}

#jd_content_left {
	border: 0px solid red;
	float: left;
	overflow-x: hidden;
	overflow-y: auto;
}

#jd_content_right {
	width: 550px;
	height: 1000px;
	border: 0px solid blue;
	float: right;
	overflow-x: hidden;
	overflow-y: auto;
	padding: 0px;
}

#jd_content_center {
	position: absolute;
	width: 2px;
	overflow: hidden;
	background: #E9F2FE;
	cursor: w-resize;
	border-left: 1px solid #B2D0EA;
	border-right: 1px solid #B2D0EA;
}

.ztree li button.switch.level0 {
	visibility: hidden;
	width: 1px;
}

.ztree li ul.level0 {
	padding: 0;
	background: none;
}
</style>
</head>


<body>
	<div id="jd_content_main">
		<div id="jd_content_left">
			<ul id="treeDemo" class="ztree"></ul>
		</div>

		<div id="jd_content_right">
			<iframe name="FrameContent" id="FrameContent" src="about:blank"
				width="100%" height="100%" scrolling="no" marginheight="0"
				marginwidth="0" frameborder="0"></iframe>
		</div>

		<div id="jd_content_center" style="width: 3px;"></div>
		<form method="post"
			action="<%=basePath%>role/resource_updateResourceByRoleId.do"
			onSubmit="return checkFroms();">
			<input type="hidden" name="codes" id="companyIds" />
			<tr align="center">
				<input type="submit" class="dl" value="提交">
			</tr>
		</form>
	</div>

</body>
</html>
