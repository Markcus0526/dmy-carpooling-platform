<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<!-- saved from url=(0073)index/activity_createFx.do?activityDto.userId=46 -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
/* CSS Document清除默认样式 */
.share{width:70%;margin:1em auto;border:1px solid #ccc;height:8em;}
.shuoming{width:70%;margin:1em auto;height:10em;background-image: url('<%=basePath%>images/beijing.png');}
.lkfb{width:70%;height:50px;margin:0em auto;background-image: url('<%=basePath%>images/lkfb.png');font-size: 130%;text-align:center;color: white; margin-bottom:10px}

</style>
</head>
<body>
	  <div class="share">宣传图</div>
	  <div style="color:#666;font-size:200%;text-align: center;">
	  			活动已过期了！
	  </div>
		
	  <div class="lkfb">
		<a href="index/activity_showFx.do?activityDto.faqiId=46">
			立刻发布我的宝箱</a><br>
		</div>
</body></html>
