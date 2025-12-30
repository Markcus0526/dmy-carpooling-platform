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
<title>Insert title here</title>
<style>
.box{position:relative;}
.share{width:94%;margin:3%;}
.shuoming{width:90%;margin:1em auto 0;height:2em;background: url('<%=basePath%>images/giFtperson.png') no-repeat;background-size:100% 100%;padding-top:3em}
.shuomingtable{width:90%;margin:0em auto 1em;overflow:hidden;}
.shuomingtable td{background:#fff;text-align:center; width:33%;height:3em;vertical-align:middle;}
.shuoming th{background:#ad1e1e;color:white;cell-spacing:0;}
.lkfb{width:100%;height:50px;margin:0em auto;background: url('<%=basePath%>images/lkfb.png') no-repeat;font-size: 130%;text-align:center;color: white; margin-bottom:10px}
.bx{color:green;text-align: center;color: red;color:#393939;font-size:14px;}
.bx span{color:#f0865a;}
.share_btn{width:95%;margin:30px auto;overflow:hidden;color:white;}
.share_btn .btn1{height:40px;width:100%;background:#3abf52;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:8px;margin-bottom:12px;}
.share_btn .btn2 span{height:40px;width:45%;background:#53b0fe;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:5px;display:block;float:left;}
.share_btn .btn2 span:nth-child(2){float:right;background:#fd8b4d;}
.footer{width:100%;height:80px;border:1px solid #ccc;box-sizing:border-box;}
.share_btn a{color:white;}
.share_btn img{vertical-align:middle;}
.shadow{background-color:rgba(0,0,0,.5);position:absolute;top:0;left:0;height:100%;}
</style>
</head>
<body>
	<div class="box">
    	<div class="share"><img width="100%" src="<%=basePath%>images/three.jpg"></div>
	  	<div class="bx">
	  		<p>宝箱有效期：<span class="min" id="showTimes"></span></p>
			<p>宝箱剩余：<span>${activityDto.activity.sharePeople-activityDto.userdNum}/${activityDto.activity.sharePeople}个</span> </p> 	
	  	</div>
         <div class="shuoming">
         		<table  width="100%"  cellspacing="0" cellpadding="0">
            	<tr class="headtr" align="center">
                    <th>编号</td>
                    <th>注册用户</td>
                    <th>渠道</td>
			  </tr>
            </table>
		 </div>	
		  <div class="shuomingtable">
          	
		 	<table width="100%"  cellspacing="1" cellpadding="1" bgcolor="#c0c0c0" >
		 	  <c:forEach items="${activityDto.listActivityUsers}" var="u" varStatus="s">
				  <tr>
	                 <td>${s.count}</td>
	                 <td> ${u.usercode}</td>
	                <td><c:choose>
							<c:when test="${u.type==0}">微信</c:when>
							<c:otherwise>APP</c:otherwise>
							</c:choose></td>
				  </tr>
              </c:forEach>
			</table>
		  </div>	
		 
		 
	  <div class="share_btn">
      	<div class="btn1">
        	<a href="<%=basePath%>index/activity_goonFx.do?activityDto.faqiId=${activityDto.faqiId}"><img width="5%" src="<%=basePath%>images/tubiao.png">分享宝箱给好友</a>
        </div>
        <div class="btn2">
        	<span><a href="<%=basePath%>index/activity_findMyGift.do?activityDto.userId=${activityDto.faqiId}">查看我的奖品</a></span>
            <span><a onclick="javascript:window.history.go(-1);">返回上一页</a></span>
        </div>
      </div>
      </div>
</body>
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
	 location = "<%=basePath%>index/activity_guoqi.do?activityDto.faqiId=${activityDto.faqiId}";
}
</script>