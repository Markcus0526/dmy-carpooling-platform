<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<head>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/linkbutton.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/datebox.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/default/tree.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/myCss.css" />
<!-- 	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css" />  -->

<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.form.js"></script>

<style>
.textbox {
	height: 20px;
	margin: 0;
	padding: 0 2px;
	box-sizing: content-box;
}

* {
	font-size: 12px;
}

body {
	font-family: verdana, helvetica, arial, sans-serif;
	padding: 20px;
	font-size: 12px;
	margin: 0;
}

h2 {
	font-size: 18px;
	font-weight: bold;
	margin: 0;
	margin-bottom: 15px;
}

.footer {
	text-align: center;
	color: #15428B;
	margin: 0px;
	padding: 0px;
	line-height: 23px;
	font-weight: bold;
}
</style>
</head>

<body class="easyui-layout">

	<!-- jsp:include page="/jsp/layouts/header.jsp"/ -->

	<div class="panel-noscroll" data-options="region:'north',border:false"
		style="height: 60px; background: #00002F; padding: 10px">
		<img src="${pageContext.request.contextPath}/easyui/themes/img/">
		<a href="${pageContext.request.contextPath}/logout"
			style="float: right; color: #ffffff">退出系统</a>
	</div>

	<!-- jsp:include page="/jsp/layouts/menu.jsp"/ -->
	<div data-options="region:'west',split:true,title:'OO拼车管理系统'"
		style="width: 200px">
		<div class="easyui-accordion"
			data-options="fit:true,border:false,selected:false"></div>
	</div>

	<!-- jsp:include page="/jsp/layouts/body.jsp"/ -->
	<div id="view" data-options="region:'center',title:'View'"></div>

	<!-- jsp:include page="/jsp/layouts/footer.jsp"/  -->
	<div data-options="region:'south',border:false,split:true"
		style="height: 50px; background: #FFFFFF; padding: 10px;">
		<div class="footer">PinChe (2014-08-30)</div>
	</div>
	<script>
			var menu_list = [];
			var selectedMenu = '${session.menucode}';
			var selectedSubmenu = '${session.submenucode}';
// 			alert(selectedMenu);
// 			alert(selectedSubmenu);

			$(function(){
				refreshMenu();
			});
			
			function refreshMenu() {
				var menu_url = '${pageContext.request.contextPath}/menulist';
				$.ajax({
					url: menu_url,
					success: function(data) {
						menu_list = data.rows;
						getMenuList();
						$('#view').load('${pageContext.request.contextPath}/${session.url}'); //first page
					},
					error: function() {
						alert("error");
					},
					complete: function() {}
				});
			}
			
			function getMenuList() {
				var menucode = [];
				var submenu = [];
				var k = 0;
				for (var i = 0; i < menu_list.length; i++) {
					if (menu_list[i].submenu == 0) {
						menucode[k] = i;
						k++;
					}
				}
				
//				k = 0;
//				var selMenuIndex = -1;
				
				for (var j = 0; j < menucode.length; j++) {
					submenu[j] = '<div class="btn-left">';
					for (var i = 0; i < menu_list.length; i++) {
						if (menu_list[i].submenu != 0 && (menu_list[i].parent == 0 || menu_list[i].parent == null)) {
							if (menu_list[menucode[j]].menucode == menu_list[i].menucode) {
								submenu[j] += '<a url="${pageContext.request.contextPath}' + menu_list[i].url + '" ' +
								'class="easyui-linkbutton show-view" menucode="' + menu_list[i].submenu + '" ' +
								'submenu="true" data-options="iconCls:\'icon-pagifile\',toggle:true,group:\'g' + menu_list[menucode[j]].menucode + '\',plain:true">' + menu_list[i].name + '</a>';
							}
						}
					}
					submenu[j] += '</div>';
				
					$('.easyui-accordion').accordion('add',{
						title:menu_list[menucode[j]].name,
						content:submenu[j]
					});
				}
				
				$('.easyui-accordion').accordion('unselect', k-1);

				$('.show-view').click(function() {
					var url = $(this).attr('url');
					$("#view").html("");
					$("#view").load(url);
				});
				
/*				if (selMenuIndex == -1) {
					$('.easyui-accordion').accordion('unselect', k-1);
				} else {
					alert("selMenuIndex" + selMenuIndex);
					$('.easyui-accordion').accordion('select', selMenuIndex);
				}
				
				$('.show-view').click(function() {
					var url = $(this).attr('url');
					var submenucode = $(this).attr('menucode');
					var menuIndex = $('.easyui-accordion').accordion('getSelected').attr('menuIndex');
					alert("submenucode" + submenucode);
					alert("url" + url);
					$("#view").load(url);
					param = "menucode=" + menuIndex 
							+ "&submenucode=" + submenucode
							+ "&url=" + url;
					alert(param);

					$.ajax({
						url : '${pageContext.request.contextPath}/setPageParam?' + param
					});
				});	
				*/
			}


			function post_subpage()
			{
				if (window["dataLoader"]!= undefined && window["dataLoader"]!="undefined" && window["dataLoader"]!=null)
				{
					window["dataLoader"].load();
				}
			}
			
			function showPath() {
				var url = document.URL.split("${pageContext.request.contextPath}/")[1].split("?")[0];
				var urls = url.split("/");
				var s = "";
				var innerhtml = "", parent = "";
				for (var i = 0; i < urls.length - 1; i++) {
					if (i > 0)
						parent = urls[i - 1];
					if (i < urls.length - 2) {
						s += urls[i] + "/";
						if (urls[i] == "authority") {
							s += "manager/";
						} else if (urls[i] == "order") {
							s += "recourse/";
						}
						innerhtml += '<a href="/nmdy/' + s + 'index">' + replaceStr(urls[i], parent) + '</a>->';
					} else {
						if (urls[urls.length - 1] == "index") {
							innerhtml += '<a>' + replaceStr(urls[i], parent) + '</a>';
						} else {
							s += urls[i] + "/";
							innerhtml += '<a href="/nmdy/' + s + 'index">' + replaceStr(urls[i], parent) + '</a>-><a>' + replaceStr(urls[urls.length - 1], urls[urls.length - 2]) + '</a>';
						}
					}
				}
				$(".layout-panel-center .panel-header .panel-title").html(innerhtml);
			}
			
			function replaceStr(page, parent) {
				var s = "";
				switch(page) {
				case "authority":
					s = "权限管理";
					break;
				case "manager":
					s = "后台用户管理";
					break;
				case "role":
					s = "后台角色管理";
					break;
				case "assignOp":
					s = "权限分配";
					break;
				case "assignData":
					s = "权限分配";
					break;
				case "add":
					if (parent == "manager")
						s = "添加用户";
					else if (parent == "recourse")
						s = "新建工单";
					break;
				case "edit":
					s = "修改";
					break;
				case "view":
					s = "查看";
					break;
				case "assign":
					s = "分配角色";
					break;
				case "order":
					s = "订单/工单管理";
					break;
				case "recourse":
					s = "工单管理";
					break;
				case "appoint":
					s = "拼车订单管理";
					break;
				case "process":
					s = "处理";
					break;
				}
				return s;
			}
			
		</script>
</body>
</html>