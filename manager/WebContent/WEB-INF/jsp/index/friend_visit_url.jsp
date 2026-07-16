<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
 <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>

<html><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
 <base href="<%=basePath%>">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<style>

.share{width:94%;margin:3%;}
.lkfb{width:100%;height:50px;margin:0em auto;background: url('images/lkfb.png') no-repeat;font-size: 130%;text-align:center;color: white; margin-bottom:10px}
.bx{color:green;color: red;color:#393939;font-size:15px;font-weight:bold;width:85%;margin:0 auto;}
.bx span{color:#ec0e0e;}
.bx ol{width:100px;display:inline-block;text-align: right;}
.share_btn{width:100%;overflow:hidden;color:white;position:fixed;bottom:0;left:50%;margin-left:-50%;}
.btn1{text-align:center;padding-bottom:8em;}

.redBox{width:100%;background:url('images/bottombg_06.jpg') center bottom no-repeat #f5edde;padding-bottom:4em;}
.footer{width:100%;height:80px;border:1px solid #ccc;box-sizing:border-box;display:-webkit-box;}
</style>
</head>
<body>
		<div class="box">
			<div class="redBox">
				<div class="share"><img width="100%" src="images/hb_03.jpg"></div>
			  	<div class="bx">
			  		<p><ol>宝箱有效期：</ol><span class="min" id="showTimes"></span></p>
					<p><ol>宝箱剩余：</ol><span>${activityDto.userdNum}/${activityDto.activity.sharePeople}个</span> </p> 	
			  	</div>
			</div>
			<div class="btn1">
	        	<a href="index/activity_register.do?activityDto.faqiId=${activityDto.faqiId}"><img width="50%" src="images/qiang_03.jpg"></a>
	        </div>

		</div>
	  <div class="share_btn">
      <div class="footer">appp</div>
    </div>
</body>
</html>
<script>
var t = ${viewHitsTime};
function GetRTime(i){
		t = t- 1000;
		var s = Math.floor(t/1000%60); // 秒
		var mi = Math.floor(t/1000/60%60); // 分钟
		var h = Math.floor(t/1000/60/60%24); // 小时
		var d = Math.floor(t/1000/60/60/24); // 天
		var time= "剩余：" + d + "天" + h + "小时" + mi + "分钟" + s + "秒";
		document.getElementById("showTimes").innerHTML = time;
		if(t<=0){
			updateGift();
		}
}
setInterval("GetRTime()", 1000);
 function updateGift(){
	 location = "index/activity_guoqi.do?activityDto.faqiId=${activityDto.faqiId}";
 }
</script>