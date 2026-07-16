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
.share{width:94%%;overflow:hidden;margin:3% auto;}

.bx{color:green;text-align: center;color: red;color:#393939;font-size:16px;font-weight:bold;line-height:40px;}
.bx span{color:#ec0e0e;}
.share_btn{width:95%;margin:30px auto;overflow:hidden;color:white;}
.share_btn .btn1{height:40px;width:100%;background:#3abf52;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:8px;margin-bottom:12px;}
.introduce{width:94%;margin:0 auto;font:bold 12px/20px "Miscrosoft YaHei","微软雅黑";color:#393939;background:#fcffd6;border:dashed 1px #c6bc77;-webkit-border-radius:5px;-moz-border-raius:5px;border-radius:5px;padding:1em 0;}
.introduce dl{padding:0 1em;overflow:hidden;}
.introduce h3{padding-left:1em;font:bold 14px/20px "Miscrosoft YaHei","微软雅黑";color:#358457;}
.introduce dt,.introduce dd{float:left;}
.introduce dt{width:32%;}
.introduce dd{width:68%;}
.share_btn .btn2 span{height:40px;width:45%;background:#53b0fe;text-align:center;font:16px/40px "Microsoft YaHei","微软雅黑";border-radius:5px;display:block;float:left;}
.share_btn .btn2 span:nth-child(2){float:right;background:#fd8b4d;}
.share_btn a{color:white;}
.share_btn img{vertical-align:middle;}
.footer{width:100%;height:80px;border:1px solid #ccc;box-sizing:border-box;}

</style>

</head>
<body>
	<div class="box">
    	<div class="share">
    			<img width="100%" src="<%=basePath%>images/one.jpg">
    	</div>
	  	<div class="bx">
			<p>本次活动截止日期:<span><fmt:formatDate pattern="yyyy年MM月dd日   HH时mm分ss秒" value="${activityDto.activity.endTime}"/></span> </p> 	
	  	</div>
    <div class="introduce">
<!--         <h3>每成功拓展一个客户(拼友或车主皆可),奖励100元!</h3> -->
<!--          <dl> -->
<!--             <dt>【分次返回】: </dt> -->
<!--             <dd>TA每拼一次车, 奖励您0.5元/单, 直至100元。</dd> -->
<!--          </dl> -->
<!--          <dl> -->
<!--             <dt>【庞大客群】: </dt> -->
<!--             <dd>线上分享朋友圈、微博、QQ群;</br>线下推广本社区、写字楼、商场停车场, -->
<!-- </br>千余名客户轻松拥有！</dd> -->
<!--          </dl> -->
<!--          <dl> -->
<!--             <dt>【月入八千】: </dt> -->
<!--             <dd>若TA使用拼车200次/年, 以您最少1000个客户计,</br>每个100元, 累计奖励达10万元！</br>折合每月收入8300元！</dd> -->
<!--          </dl> -->
<!--           <h3>500万归自己,千余万归朋友。</h3> -->
<!--           <dl> -->
<!--             <dd style="width:100%">成功分享3个朋友, 有机会领取500万奖金！</dd> -->
<!--          </dl> -->
      </div>
	  <div class="share_btn">
      	<div class="btn1">
        	<a href="<%=basePath%>index/activity_showFx.do?activityDto.faqiId=${activityDto.userId}">
			<img width="5%" src="<%=basePath%>images/tubiao.png">
			立刻发布我的宝箱</a><br>
        </div>
        <div class="btn2">
        	<span><a href="<%=basePath%>index/activity_findMyGift.do?activityDto.userId=${activityDto.userId}">查看我的奖品</a></span>
        	<span><a href="<%=basePath%>index/activity_appFx.do?activityDto.userId=${activityDto.userId}">返回上一页</a></span>
        </div>
      </div>
      
      <div class="footer">appp</div>
    </div>
</body></html>