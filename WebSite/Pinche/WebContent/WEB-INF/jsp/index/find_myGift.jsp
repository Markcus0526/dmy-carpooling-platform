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
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/common.css">
<style>

.box div{width:90%;margin:0 auto;}
.box .myP{margin:1em auto;}
.box .myP img,.box .myP span{vertical-align:middle;}
.box .myP span{font-weight:bold;color:#2bb243;font-size:200%;}
.box .shuoming{width:90%;margin:1em auto 0;height:2em;background: url('<%=basePath%>images/gift.png') no-repeat;background-size:100% 100%;padding-top:2em}
.box .shuomingtable{width:90%;margin:0em auto 1em;overflow:hidden;}
.shuomingtable td{background:#fff;text-align:center; width:32%;height:3em;vertical-align:middle;}
.shuoming th{background:#ad1e1e;color:white;cell-spacing:0;width:32%}

.box .lkfb{width:70%;height:30px;margin:5em auto 0;background:#3abf52;text-align:center;color: white; margin-bottom:10px;border-radius:5px;line-height:30px;}

</style>
</head>
<body>
<div class="box">
    	<div class="myP" style="text-align: center;">
		<img width="10%" src="<%=basePath%>images/gifttupian.png"/><span>我的奖品</span>
		 </div>
	  	<div style="color:green;text-align: left;color: #242524;">
	  		<img style="float:left;" width="12%" height="12%" src="<%=basePath%>images/shuoming.png"/>所有奖品请进入APP查看我的点卷，点卷
	  		以外的奖品为实物卷形式
	  	</div>
		
		
		 <div class="shuoming">
         		<table  width="100%"  cellspacing="0" cellpadding="0">
            	<tr class="headtr" align="center">
                    <th>编号</td>
                    <th>奖品</td>
                    <th>获奖日期</td>
			  </tr>
            </table>
		 </div>	
		  <div class="shuomingtable">
		 	<table width="100%"  cellspacing="1" cellpadding="1" bgcolor="#c0c0c0" >
		 	  <c:forEach items="${activityDto.listActivityGift}" var="gift" varStatus="status">
			  <tr>
              	 <td>${status.count}</td>
	  			  <td> ${gift.name}</td>
	  			  <td><fmt:formatDate pattern="yyyy年MM月dd日 " value="${gift.addtime}"/></td>
			  </tr>
			    </c:forEach>
			</table>
		  </div>	 
	  <div class="lkfb">
		<a onclick="javascript:history.go(-1);">返回上一页参加活动</a>
		</div>
    </div>
</body>

