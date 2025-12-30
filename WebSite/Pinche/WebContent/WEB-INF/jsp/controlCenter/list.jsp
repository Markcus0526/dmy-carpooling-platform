<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<%-- <script type="text/javascript" src="http://api.map.baidu.com/api?key=A864a1fb8621b22da00e6434027adab3&v=1.4&services=true"></script> --%>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=B08246dbf469c3fd2b10d7de4e171e0e"></script>
<%
	String msg = (String)request.getAttribute("msg");
	if(msg!=null){
%>
	  <script>
	  	alert('<%=msg%>');
	  </script>
<%
	}
%>
<style type="text/css">
.container, .two{
	float:left;
	font-size:14px;
	color:#f00;

	
}
.container{
	border:1px solid #000;width:700px;height:432px;
}
.two{
	border:1px solid #00f;margin-left: 100px;width:300px;height:432px;margin-left: 0px;
}
</style>
</head>
<body>


<br/>
	<h2 style="margin-left:10px;margin-bottom:0px">监控中心设置</h2>
<hr/>
    <div class="container" id="container">	
    </div>
	<div class="two" id="two">
		<div style="padding: 10px 20px 0px 20px">
			<span>统计城市:</span>
			<select name="city" onchange="city();" id="city">
				<option value="0">北京</option>
				<option value="1">上海</option>
				<option value="2">广州</option>
				<option value="3">深圳</option>
			</select>
		</div>
		<div style="padding: 10px 20px 0px 20px">
			<span>公司标识:</span>
    		<input type="text" value="" id="one1" class="textbox" style="width:212px;">
		</div>
		<div style="padding: 10px 20px 0px 20px">
			<span>车主标识:</span>
    		<input type="text" value="" id="two2" class="textbox" style="width:212px;">
		</div>
		<div style="padding: 10px 20px 0px 20px">
			<span>车主姓名:</span>
    		<input type="text" value="" id="three" class="textbox" style="width:212px;">
		</div>
		<div style="padding: 10px 20px 0px 20px">
			<span>车主电话:</span>
    		<input type="text" value="" id="four" class="textbox" style="width:212px;">
		</div>
		<br/>
		<div style="padding: 10px 20px 0px 20px">
			<span>显示&nbsp;&nbsp;图标&nbsp;&nbsp;状态&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;统计&nbsp;&nbsp;</span>
		</div>
			<div style="padding: 10px 20px 0px 20px">
			<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总数：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${allNum}&nbsp;&nbsp;</span>
			</div>
			<div style="padding: 10px 20px 0px 20px">
			<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在线空闲：&nbsp;&nbsp;&nbsp;&nbsp;${allNum-onlineNum-unlineNum}&nbsp;&nbsp;</span>
			</div>		
			<div style="padding: 10px 20px 0px 20px">
			<span><input type="checkbox" id="online" checked="checked"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;在线忙碌：&nbsp;&nbsp;&nbsp;&nbsp;${onlineNum}&nbsp;&nbsp;</span>
			</div>		
			<div style="padding: 10px 20px 0px 20px">
			<span><input type="checkbox" id="unline" checked="checked"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;离线：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${unlineNum}&nbsp;&nbsp;</span>
			</div>				
	</div>
</body>
</html>
   <script type="text/javascript">
            var map = new BMap.Map("container");//在container容器中创建一个地图,参数container为div的id属性;
            map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
        	map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
        	map.addControl(new BMap.OverviewMapControl());              //添加缩略地图控件
        	map.enableScrollWheelZoom();                            //启用滚轮放大缩小
        	
            var point = new BMap.Point(116.307852, 40.057030);//定位
            map.centerAndZoom(point,15);                //将point移到浏览器中心，并且地图大小调整为15;
			
            function add(){
                $.ajax({
                       url: "<%=basePath%>controlCenter/selectAll.do",
                       data: {"cityType":$("#city").val(), "online":$("#online").is(":checked"),"unline":$("#unline").is(":checked")},
                      type: 'POST', 
                       dataType: 'json',
                       timeout: 100000,
                       cache: false,
                       error: function(){
                    	   alert("程序出错，请联系管理员!");
                       },
                       success: function(data){ 
                    	   map.clearOverlays();
                    	   for(var i=0;i<data.length;i++){
                    		   var marker = new BMap.Marker(new BMap.Point(data[i].lat,data[i].lng));
                    		   map.addOverlay(marker); //标注
                    		   marker.addEventListener('click',function(i){
                    			   return function(){
                    				   		$("#one1").val(data[i].groupName);
                    				   		$("#two2").val(data[i].userid);
                    				   		$("#three").val(data[i].user.username);
                    				   		$("#four").val(data[i].user.phone);
                    				   }
                    			}(i),false);
                    		   marker.setLabel(new BMap.Label(data[i].user.username,{offset:new BMap.Size(20,-10)}));
                    	   }
                       }
                   });   
           }  
            
            function city(){
            	map.centerAndZoom($("#city option:selected").text(),11);      // 用城市名设置地图中心点
            	add();
            }
            
            $(function(){
            	 add();
            	$("#online").click(function(){
            		add();
            	});	
            	$("#unline").click(function(){
            		add();
            	});	
            })
//             var adds = [
//                 		new BMap.Point(116.307852,40.057031),
//                 		new BMap.Point(116.313082,40.047674),
//                 		new BMap.Point(116.328749,40.026922),
//                 		new BMap.Point(116.347571,39.988698),
//                 		new BMap.Point(116.316163,39.997753),
//                 		new BMap.Point(116.345867,39.998333),
//                 		new BMap.Point(116.403472,39.999411),
//                 		new BMap.Point(116.307901,40.05901)
//                 	];
            
//             for(var i = 0; i<adds.length; i++){
//         		var marker = new BMap.Marker(adds[i]);
//         		map.addOverlay(marker); //标注
//         		   marker.addEventListener("click",function(){ //点击标注时出发事件
//                        alert("您点击了标注");
//                    });
//         		marker.setLabel(new BMap.Label("我是商家",{offset:new BMap.Size(20,-10)}));
//         	}
            
             
            //创建信息窗口
            var opts = { 
                width : 250,     // 信息窗口宽度 
                height: 100,     // 信息窗口高度 
                title : "Hello"  // 信息窗口标题 
            } 
            var infoWindow = new BMap.InfoWindow("World", opts);  // 创建信息窗口对象 
            map.openInfoWindow(infoWindow, map.getCenter());      // 打开信息窗口
             
        </script>
