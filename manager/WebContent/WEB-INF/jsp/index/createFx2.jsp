<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<!-- saved from url=(0073)<%=basePath%>index/activity_createFx.do?activityDto.userId=46 -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>分享</title>
<script type="text/javascript"  src="${pageContext.request.contextPath}/js/jquery-1.4.2.min.js"></script>
<link id="skin" rel="stylesheet" href="${pageContext.request.contextPath}/Pink/jbox.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jBox.src.js"></script>

<style>
.share{width:94%%;overflow:hidden;margin:3% auto;}
.all{width:94%%;overflow:hidden;margin:5% auto;}

.bx{color:green;text-align: center;color: red;color:#393939;font-size:16px;font-weight:bold;line-height:40px;}
.bx span{color:#ec0e0e;}
.share_btn{width:95%;margin:30px auto;overflow:hidden;color:white;}
.share_btn .btn1{height:40px;width:100%;background:#2E8B57;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:8px;margin-bottom:12px;}
.introduce{width:94%;margin:0 auto;font:bold 12px/20px "Miscrosoft YaHei","微软雅黑";color:#393939;background:#fcffd6;border:dashed 1px #c6bc77;-webkit-border-radius:5px;-moz-border-raius:5px;border-radius:5px;padding:1em 0;}
.introduce dl{padding:0 1em;overflow:hidden;}
.introduce h3{padding-left:1em;font:bold 14px/20px "Miscrosoft YaHei","微软雅黑";color:#358457;}
.introduce dt,.introduce dd{float:left;}
.introduce dt{width:32%;}
.introduce dd{width:68%;}
.share_btn .btn2 span{height:40px;width:100%;background:#FFA500;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:5px;display:block;float:left;}
/* .share_btn .btn2 span:nth-child(2){float:right;background:#FFA500;} */
.share_btn a{color:white;}
.share_btn img{vertical-align:middle;}
.footer{width:100%;height:80px;border:1px solid #ccc;box-sizing:border-box;}

</style>
<script type="text/javascript">
window.onload = function() {
	var btn = document.getElementById('btn1');
	btn.onclick = function(e) {
		
			jBox.tip("Loading...", 'loading');
			location='<%=basePath%>index/activity_showFx.do?activityDto.faqiId=${activityDto.userId}';
			//return false;
		}
} 

</script>

</head>
<body>
	<div class="box">
    	<div class="all">
    			<img width="100%" src="<%=basePath%>images/createFx.jpg">
    	</div>
	  <div class="share_btn">
      	<div id="btn1" class="btn1" style="cursor:pointer;">
      		
        	 <!-- <a onclick="go();">  -->
        	 <a>
			<%-- <img width="5%" src="<%=basePath%>images/tubiao.png"> --%>
			继续</a><br>
        </div>
			<div class="btn2" style="cursor:pointer;">
        	<!-- <span><a onclick="go1();">查看我的奖品</a></span> -->
        	<span><a onclick="go2();">返回上一页</a></span>
        </div>
      </div>
      
    </div>
</body></html>
<script>
function go() {
    jBox.tip("Loading...", 'loading');
/*     var id = ${activityDto.userId};
     alert(id); */
    	 location='<%=basePath%>index/activity_showFx.do?activityDto.faqiId=${activityDto.userId}'; 
    	<%-- location='<%=basePath%>index/activity_showFx.do?activityDto.userId=${activityDto.userId}'; --%>
}

function go1() {
    jBox.tip("Loading...", 'loading');
    	location='<%=basePath%>index/activity_findMyGift.do?activityDto.userId=${activityDto.userId}';
}

function go2() {
    jBox.tip("Loading...", 'loading');
    	location='<%=basePath%>index/activity_appFx.do?activityDto.userId=${activityDto.userId}';
}
</script>