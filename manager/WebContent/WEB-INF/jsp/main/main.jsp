<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%response.setContentType("text/html;charset=UTF-8");%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>OO车生活后台管理系统</title>
<link href="css/default.css" rel="stylesheet" type="text/css" />
<link href="css/myCss.css" rel="stylesheet" type="text/css" />
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
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/outlook2.js'> </script>
<script type="text/javascript"
	src='${pageContext.request.contextPath}/js/dz_util.js'> </script>
<script type="text/javascript">
    var _menus=<%=request.getAttribute("menus")%>;
    /*
	 var _menus = {"menus":[
						{"menuid":"1",
						 "icon":"icon-sys",
						 "menuname":"控件使用",
						 "menus":[
									{"menuid":"12","menuname":"疯狂秀才","icon":"icon-add","url":"http://www.baidu.com"},
									{"menuid":"13","menuname":"用户管理","icon":"icon-users","url":"demo2.html"},
									{"menuid":"14","menuname":"角色管理","icon":"icon-role","url":"demo2.html"},
									{"menuid":"15","menuname":"权限设置","icon":"icon-set","url":"demo.html"},
									{"menuid":"16","menuname":"系统日志","icon":"icon-log","url":"demo1.html"}
								]
						},
						{"menuid":"8",
						 "icon":"icon-sys",
						 "menuname":"员工管理",
						 "menus":[{"menuid":"21","menuname":"员工列表","icon":"icon-nav","url":"demo.html"},
									{"menuid":"22","menuname":"视频监控","icon":"icon-nav","url":"demo1.html"}
								]
						},
						{"menuid":"56",
						 "icon":"icon-sys",
						 "menuname":"部门管理",
						 "menus":[{"menuid":"31","menuname":"添加部门","icon":"icon-nav","url":"demo1.html"},
									{"menuid":"32","menuname":"部门列表","icon":"icon-nav","url":"demo2.html"}
								]
						},
						{"menuid":"28",
						 "icon":"icon-sys",
						 "menuname":"财务管理",
						 "menus":[{"menuid":"41","menuname":"收支分类","icon":"icon-nav","url":"demo.html"},
									{"menuid":"42","menuname":"报表统计","icon":"icon-nav","url":"demo1.html"},
									{"menuid":"43","menuname":"添加支出","icon":"icon-nav","url":"demo2.html"}
								]
						},
						{"menuid":"39",
						 "icon":"icon-sys",
						 "menuname":"商城管理",
						 "menus":[{"menuid":"51","menuname":"商品分类","icon":"icon-nav","url":"demo.html"},
									{"menuid":"52","menuname":"商品列表","icon":"icon-nav","url":"demo1.html"},
									{"menuid":"53","menuname":"商品订单","icon":"icon-nav","url":"demo2.html"}
								]
						}
				]};
    */
        //设置登录窗口
        function openPwd() {
            $('#w').window({
                title: '修改密码',
                width: 300,
                modal: true,
                shadow: true,
                closed: true,
                height: 160,
                resizable:false
            });
        }
        //关闭登录窗口
        function closePwd() {
            $('#w').window('close');
        }

        

        //修改密码
        function serverLogin() {
            var $newpass = $('#txtNewPass');
            var $rePass = $('#txtRePass');

            if ($newpass.val() == '') {
                msgShow('系统提示', '请输入密码！', 'warning');
                return false;
            }
            if ($rePass.val() == '') {
                msgShow('系统提示', '请在一次输入密码！', 'warning');
                return false;
            }

            if ($newpass.val() != $rePass.val()) {
                msgShow('系统提示', '两次密码不一致！请重新输入', 'warning');
                return false;
            }

            $.post('<%=request.getContextPath()%>/editpassword.do?newpass=' + $newpass.val(), function(msg) {
                msgShow('系统提示', '恭喜，密码修改成功！<br>您的新密码为：' + $newpass.val(), 'info');
                $newpass.val('');
                $rePass.val('');
                $('#w').dialog('close');
            });
            
        }

        $(function() {
            openPwd();

            $('#editpass').click(function() {
                $('#w').window('open');
            });

            $('#btnEp').click(function() {
                serverLogin();
            });

			$('#btnCancel').click(function(){closePwd();})

            $('#loginOut').click(function() {
                $.messager.confirm('系统提示', '您确定要退出本次登录吗1?', function(r) {
                    if (r) {
                        location.href = '${pageContext.request.contextPath}/logout.do';
                    }
                });
            })
        });
		/**
        function dd(){
        	alert("onload done.");
        } 
        var fw=document.getElementById("fw"); 
        fw.attachEvent("onload", dd); 
		*/
		
        $(document).ready(function () {

            $('#loginOut').click(function () {
                loginout();
            });
            $('#editPass').click(function () {
                $("#epasswin").window("setTitle", "修改密码");
                $("#epasswin").window("open");
            });
            $("#show_a").show();
            $("#show_b").hide();

            $("#logincall").show();


            $('#callin').click(function () {
                $("#callwin").window("setTitle", "呼叫中心登录");
                $("#callwin").window("open");
            });
            $('#callout').click(function () {
                calllogout();
            });
    
        });		

        var LoginCall = 0;

        function callout(tel) {
            var params = {};
            params.tel = document.getElementById('tel').value;
            params.callType = '3'; //3点击外呼
            document.getElementById("frmPhone").contentWindow.executeAction("doPreviewOutCall", params);
        }
        
        //呼叫中心来电响铃后弹屏
		function callWorkForm(phoneNumber){
			alert("this is phoneNumber :  "+phoneNumber);
			addTab("新建工单","order/recourse/add1.do?phoneNumber="+phoneNumber,"icon-add");
		}
        function calllogin() {//登录
        	//parent.addTab("新建工单","order/recourse/add.do","");
        
            var params = {};
            params.hotLine = document.getElementById('hotLine').value;
            params.cno = document.getElementById('cno').value;
            params.pwd = document.getElementById('pwd').value;
            params.bindTel = document.getElementById('bindTel').value;
            params.bindType = document.getElementById('bindType').value;
            params.initStatus = document.getElementById('initStatus').value;
			console.info("params: "+params);
            document.getElementById("frmPhone").contentWindow.executeAction('doLogin', params); //执行登陆 ccic2里面的js类
            //document.getElementById("toolbar").contentWindow.addEvents('doDebug', cbDebug);//注册debug  ccic2里面的js类
           // var phoneNumber="13245625578";
            //callWorkForm(phoneNumber);
        }
        function calllogout() {//登出
            $("#show_a").show();
            $("#show_b").hide();
            $("#Phone").hide();
            LoginCall = 0;
            var object = {};
            object.type = "1";
            object.removeBinding = "1";
            document.getElementById("frmPhone").contentWindow.executeAction('doLogout', object); //执行登陆 ccic2里面的js类
        }
        function cbLogin(token) {//登陆
            if (token.code == "0") {
                //alert("登录成功");
                util.Alert("提示", "登录成功!", "");
                $('#callwin').window('close');
                $("#show_a").hide();
                $("#show_b").show();
                $("#Phone").show();
                LoginCall = 1;
            } else {
                util.Alert("提示", "登录失败!" + token.msg, "");
            }
        }
        function cbLogout(token) {//退出
            if (token.code == "0") {
                util.Alert("提示", "成功退出!", "");
            }
        }		
		
        $.extend($.fn.validatebox.defaults.rules, {    
            equals: {    
                validator: function(value,param){    
                    return value == $(param[0]).val();    
                },    
                message: '密码不一致.'   
            }    
        });  
        
        $.extend($.fn.validatebox.defaults.rules, {
    		vp: {
    		validator: function(value){
    			if(value == '${sessionScope.admin.password}')
    				return true;
    			return false;
    		},
    		message: '旧密码不正确！'
    		}
    	});
       
    </script>

</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<noscript>
		<div
			style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
			<img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<div region="north" split="true" border="false"
		style="overflow: hidden; height: 30px; background: url(images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%; line-height: 20px; color: #fff; font-family: Verdana, 微软雅黑, 黑体">
		<span style="float: right; padding-right: 20px;" class="head">欢迎 ${sessionScope.admin.usercode}!
			使用&nbsp;&nbsp;&nbsp;<a href="#" id="editpass">修改密码</a> <a href="#"
			id="loginOut">安全退出</a>
		</span> <span style="padding-left: 10px; font-size: 16px;"><img
			src="images/blocks.gif" width="20" height="20" align="absmiddle" />
			OO车生活后台管理系统</span>
	</div>
	<div region="south" split="true"
		style="height: 30px; background: #D2E0F2;">
		<table border="0px"
			style="width: 100%; margin: 0px; padding: 0px; font-weight: bold;">
			<tr id="show_a">
				<td style="width: 180px;" align="left" valign="top">
					<div id="logincall" style="display: none">
						呼叫中心：未登录&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="callin"
							style="height: 14px; padding-right: 5px; text-decoration: none">登录</a>
					</div>
				</td>
				<td style="width: 54%; color: #15428B;" align="center"
					valign="middle">
					<%-- <%=Platform.Utility.Common.GlobalSetting.GetConfigValue("SystemCopyright").Replace("<br />", "")%> --%>
					版权所有：北京昊唐三六五科技有限公司 ©2014
				</td>
				<td style="width: 33%; padding-right: 5px;" align="right"
					valign="top">
					<%--                     <input type="hidden" id="AccountName" value="<%=Platform.Utility.Common.SessionSetting.Instance().AccountName %>" />
                    <input type="hidden" id="AccountUnitNum" value="<%=Platform.Utility.Common.SessionSetting.Instance().AccountUnitNum %>" />
                    <input type="hidden" id="AccountUnitName" value="<%=Platform.Utility.Common.SessionSetting.Instance().AccountUnitName %>" />
                    <input type="hidden" id="MainTitle" value="<%=Platform.Utility.Common.SessionSetting.Instance().MainTitle %>" />
                    <input type="hidden" id="ServicePhone" value="<%=Platform.Utility.Common.SessionSetting.Instance().ServicePhone %>" />
                    <input type="hidden" id="Trusteeship" value="<%=Platform.Utility.Common.SessionSetting.Instance().Trusteeship %>" />
                    <input type="hidden" id="RelayNum" value="<%=Platform.Utility.Common.SessionSetting.Instance().RelayNum %>" />
                    <input type="hidden" id="AreaCode" value="<%=Platform.Utility.Common.SessionSetting.Instance().AreaCode %>" /> --%>
				</td>
			</tr>
			<tr id="show_b">
				<td style="width: 180px;" align="left" valign="top">
					<div id="callinfo">
						呼叫中心：已登录&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" id="callout"
							style="height: 14px; padding-right: 5px; text-decoration: none">退出</a>
					</div>
				</td>
				<td colspan="2">
					<div id="Phone" align="right">
						<iframe scrolling="auto" frameborder="0" id="frmPhone"
							name="frmPhone" src="ccic2toolbarIframe.html"
							style="width: 100%; height: 46px;"></iframe>
					</div>
				</td>
			</tr>
		</table>
	</div>


	<div region="west" hide="true" split="true" title="导航"
		style="width: 180px;" id="west">
		<div id="nav" class="easyui-accordion" fit="true" border="false">
			<!--  导航内容 -->

		</div>

	</div>
	<div id="mainPanle" region="center"
		style="background: #eee; overflow-y: hidden">
		<div id="tabs" class="easyui-tabs" fit="true" border="false"></div>
	</div>

	<div id="hiddenmsg"
		style="position: relative; left: 600px; top: 200px; height: 200px; width: 200px; display: block;">
		<img src="images/loading_circle.gif" alt="请等待..."
			style="margin-left: 20px" /> <br> <font
			style="color: gray; margin-left: 25px">加载中...</font>

	</div>


	<!--修改密码窗口-->
	<div id="w" class="easyui-window" title="修改密码" collapsible="false"
		minimizable="false" maximizable="false" icon="icon-save"
		style="width: 300px; height: 300px; padding: 5px; background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table cellpadding=3>
					<tr>
						<td>旧密码：</td>
						<td><input id="txtOldPass" type="password" class="easyui-validatebox" required="true" missingMessage="旧密码不能为空!" validType="vp"/></td>
					</tr>
					<tr>
						<td>新密码：</td>
						<td><input id="txtNewPass" type="password" class="easyui-validatebox" required="true" missingMessage="新密码不能为空!"/></td>
					</tr>
					<tr>
						<td>确认密码：</td>
						<td><input id="txtRePass" type="password" class="easyui-validatebox" required="true" missingMessage="确认密码不能为空!"  validType="equals['#txtNewPass']"/></td>
					</tr>
				</table>
			</div>
			<div region="south" border="false"
				style="text-align: right; height: 30px; line-height: 30px;">
				<a id="btnEp" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)"> 确定</a> <a id="btnCancel"
					class="easyui-linkbutton" icon="icon-cancel"
					href="javascript:void(0)">取消</a>
			</div>
		</div>
	</div>

	<div id="mm" class="easyui-menu" style="width: 150px;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>

	<div id="callwin" class="easyui-window"
		data-options="closed:true,modal:true,title:'呼叫中心登录'"
		style="width: 300px; height: 280px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false"
				style="background: #fff; padding-left: 10px;">
				<form action="" name="editform" method="post">
					<table class="edittable" cellspacing="1px" cellpadding="4px"
						style="text-align: left; margin-left: auto; margin-right: auto; width: 100%;">
						<tr>
							<td style="width: 25%">热线号码：</td>
							<td style="width: 75%"><input type="text" value="68792305"
								id="hotLine" /></td>
						</tr>
						<tr>
							<td style="width: 25%">座席号：</td>
							<td style="width: 75%"><input type="text" value="" id="cno" />
							</td>
						</tr>
						<tr>
							<td style="width: 25%">密码：</td>
							<td style="width: 75%"><input type="text" value="" id="pwd" />
							</td>
						</tr>
						<tr>
							<td style="width: 25%">绑定电话：</td>
							<td style="width: 75%"><input type="text" value=""
								id="bindTel" /></td>
						</tr>
						<tr>
							<td style="width: 25%">电话类型：</td>
							<td style="width: 75%"><span style="float: left"> <select
									id="bindType">
										<option value="2">分机号码</option>
										<option value="1">电话号码</option>
										<option value="3">软电话</option>
								</select>
							</span><span style="display: none; float: left;" id="sipSpan">软电话IP：<input
									id="sipIp" type="text" value="172.16.203.194" />软电话密码：<input
									id="sipPsw" type="text" value="" /></span></td>
						</tr>
						<tr>
							<td style="width: 25%">初始状态：</td>
							<td style="width: 75%"><select id="initStatus">
									<option value="online">空闲</option>
									<option value="pause">置忙</option>
							</select></td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'south',border:false"
				style="text-align: center; height: 30px; line-height: 30px;">
				<a name="submit" class="easyui-linkbutton" icon="icon-ok"
					href="javascript:void(0)" onclick="calllogin();">登录</a> <a
					class="easyui-linkbutton" icon="icon-undo"
					href="javascript:void(0)" onclick="$('#callwin').window('close');">取消</a>
			</div>
		</div>
	</div>
</body>
</html>