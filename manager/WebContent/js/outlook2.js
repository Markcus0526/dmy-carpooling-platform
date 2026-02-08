/***********************************************************************

modified by zyl 2013-07-09

**********************************************************/



$(function(){
	InitLeftMenu();//初始化左侧所有的菜单项
	
	tabCloseEven();//绑定右键菜单事件

	$('#tabs').tabs('add',{
		title:'首页',
		content:createFrame('index/main.do'),
		closable:false,
		icon:'icon icon-nav'
	});
	
	$('#tabs').tabs({
        onSelect: function (title) {
            var currTab = $('#tabs').tabs('getTab', title);
            var iframe = $(currTab.panel('options').content);
        	//显示加载条开始
    		document.getElementById("hiddenmsg").style.display="block";
    		//显示加载条完毕
			var src = iframe.attr('src');
			if(src)
				$('#tabs').tabs('update', { tab: currTab, options: { content: createFrame(src)} });
        }
    });
	
	tabClose();//为所有tabs绑定双击关闭事件函数和右键菜单
});

//初始化左侧所有菜单项
function InitLeftMenu() {
	$("#nav").accordion({animate:false});//设置是否应用动画效果，zyl

	//将_menus变量里定义的所有的导航菜单项添加到id为nav的div标签下
    $.each(_menus.menus, function(i, n) {//_menus变量是在index.html中13行定义的，对于每一个_menus.menus里的子元素执行function(i,n)
		var menulist ='';
		menulist +='<ul>';
        $.each(n.menus, function(j, o) {
			menulist += '<li><div><a ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div></li> ';
        });
		menulist += '</ul>';

		$('#nav').accordion('add', {
            title: n.menuname,
            content: menulist,
            iconCls: 'icon ' + n.icon
        });

    });
    //为导航菜单里的每一个菜单项设置事件响应:
    //1,点击后就将当前菜单项添加到tabs 
    //2,当鼠标移动移出当前元素时的事件响应
	$('.easyui-accordion li a').click(function(){//这里的a就是44行的a标签
		var tabTitle = $(this).children('.nav').text();//取得44行a标签的文本内容，例如"系统日志"等。这里的.nav是44行定义的<span class="nav"... ,是个css class的名字
		var url = $(this).attr("rel");//取得a标签的rel属性值
		var menuid = $(this).attr("ref");//取得a标签的ref属性值，就是menuid的值，即菜单项的编号
		var icon = getIcon(menuid,icon);//根据menuid取出icon的值
		//显示加载条开始
		document.getElementById("hiddenmsg").style.display="block";
		//显示加载条完毕
		addTab(tabTitle,url,icon);//将当前菜单项添加到tabs上
		$('.easyui-accordion li div').removeClass("selected");
		$(this).parent().addClass("selected");
	}).hover(function(){//当鼠标移动到a元素时，添加hover class 。注意hover是jquery的函数 zyl
		$(this).parent().addClass("hover");
	},function(){//当鼠标移出a标签时，去除hover class
		$(this).parent().removeClass("hover");
	});

	//选中第一个菜单项
	var panels = $('#nav').accordion('panels');
	var t = panels[0].panel('options').title;
    $('#nav').accordion('select', t);
}
//获取左侧导航的图标
function getIcon(menuid){
	var icon = 'icon ';
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				icon += o.icon;
			}
		 });
	});

	return icon;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){//如果tabs不包含当前的菜单项，就添加
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{//如果tabs包含了当前的菜单项，就只选中它
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
		
	}
	tabClose();
}

function createFrame(url)
{
	
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;" id="fw" ></iframe>';
	//s=s+'<script type="text/javascript">function dd(){alert("onload done.");} var fw=document.getElementById("fw"); fw.attachEvent("onload", dd); </script>';
	return s;
}
//为所有tabs绑定双击关闭事件，和右键菜单。这个函数应该是扩展easyui的功能，算是一个比较基础性的开发吧，不算应用层面的开发
function tabClose()
{
	/*双击关闭TAB选项卡，这里有个bug，刚进入首页的时候，双击tab不关闭，必须把刚开始的tab全关闭后再打开才可以，现在已经更改，是因为在没有执行tabClose之前添加了一个tab zyl*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	});
	/*为选项卡绑定右键，右键弹出菜单，诸如关闭tab，全部关闭tab等 zyl*/
	$(".tabs-inner").bind('contextmenu',function(e){//这里contextmenu就代表右键菜单
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}
//绑定右键菜单事件
function tabCloseEven()
{
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		$('#tabs').tabs('update',{
			tab:currTab,
			options:{
				content:createFrame(url)
			}
		});
	});
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	});
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			$('#tabs').tabs('close',t);
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		$('#mm-tabcloseright').click();
		$('#mm-tabcloseleft').click();
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==0){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	});
}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}
