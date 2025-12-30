<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
   <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML>
<html>
<head>
<script type="text/javascript"  src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.form.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta content="yes" name="apple-mobile-web-app-capable">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<title>注册</title>
<style>
ol, ul {list-style: none;width:100%;}
ul{margin-top:20px;}
.ul_2{width:100%;overflow:hidden;}
#forms{width:100%;}
ul li{height:42px;line-height:42px;width:90%;margin:5px auto;}
ul li span,ul li div{float:left;font-size:14px;line-height:32px;height:32px;}

ul li span{display:inline-block;width:42%;text-align:right;font-weight:bold;font-size:14px;}
ul li div{width:58%;}
ul li input.text{border-radius:4px;height:30px;width:100%;float:left;border:1px solid #c6c6c6;display:block;line-height:30px;}
ul li input.active{border:1px solid #3abf52;box-shadow:0 0 5px #b2ffbf }
.ul_2{font-size:20px;}
.lkfb{width:70%;height:50px;margin:0em auto;background-image: url('<%=basePath%>images/lkfb.png');font-size: 130%;text-align:center;color: white; margin-bottom:10px}
.add{width:80%;margin:1em auto 5%;font-size:20px;height:40px;line-height:40px;background:#3abf52;border-radius:5px;text-align:center;float:right;margin-right:5%}
.add input{background:none;border:none 0;color:#fff;font:bold 15px/40px 'Miscrosoft YaHei','微软雅黑';}
.msg{color:green;font-size:1.7em;text-align: center;}
.msg span,.msg img{vertical-align:middle;}
</style>
</head>
<body>

<div class="box">
<div class="msg">
<img width="10%" src="<%=basePath%>images/OO.png"><span>最后一步,确认完身份,马上领取!</span></div>

<form id="forms">
<input type="hidden" name="activityDto.faqiId" value="${activityDto.faqiId}" id="ffid"/>
<input type="hidden" name="activityDto.huojiangId" value="${activityDto.huojiangId}" id="hjId"/>
<input type="hidden" name="activityDto.validateCode" id="validateCode"/>
<ul class="ul_2">
 <li>
      <span>用户名或手机号：</span>
      <div style="display:inline">
      <input class="text active" type="text" name="activityDto.usercode" maxlength="15" class="text" id="usercode">
       </div>
 </li>
   <li><span></span>
	  <div style="background:#53b0fe;width:40%;height:32px;lin-height:32px;color:white;text-align:center;border-radius:5px;cursor:pointer;" id="validatePhoneCode">
  			获取验证码
		 </div>
 </li>
  <li>
      <span>验证码：</span>
      <div style="display:inline" >
      <input  type="text" name="activityDto.enterValidateCode" maxlength="15" class="text" id="enterValidateCode">
      </div>
 </li>
  <li>
 	  <span></span>
      <div>
      	<input type="radio" name="activityDto.sex" value="0" checked/>男
	  	<input type="radio" name="activityDto.sex" value="1"/>女<br/>
      </div>
 </li>
   <li>
      <span>密码：</span>
      <div style="display:inline" >
      <input type="text" name="activityDto.password" maxlength="15" class="text" id="pass">
      </div>
 </li>
   <li>
      <span>请再次输入：</span>
      <div style="display:inline" >
      <input type="text" name="" maxlength="15" class="text" id="repass">
      </div>
 </li>
</ul>
  <div class="add">
  			<input type="submit" value="注册并参加活动"/>	
 </div>
</form>
</div>


<form action="<%=basePath%>index/insertUser.do" method="post" id="form2">
<input type="text" name="activityDto.faqiId"  id="ffid2"/>
<input type="text" name="activityDto.huojiangId"  id="hjId2"/>
 <s:token></s:token>
    <!---用于显示action的错误，因为设置的是拦截到后再次返回此页，所以设置了这个标签-->
    <s:actionerror/>
</form>
</body>
</html>
<script>


function checkform(){
	 var ecode = $("#enterValidateCode").val();
	 if(ecode=="" || ecode==undefined || ecode==null){
		 alert("请输入验证码！");
		 return false;
	} 
	 if($("#validateCode").val()!=ecode){
		 alert("验证码不一致！");
		 return false;
	 }
	var pass = $("#pass").val();
	 if(pass=='' || pass==undefined || pass==null||pass.length<6){
		 alert("请输入密码,并且密码长度不能小于6！");
		 return false;
	}
	var repass = $("#repass").val();
	if(pass!=repass){
		alert("密码不一致！");
		 return false;
	}
	return true;
}
 
function validatemobile(mobile){
	 if(mobile==""){
         alert('请输入手机号码！');
         return false;
      }    
      if(mobile.length!=11) {
          alert('请输入有效的手机号码！');
          return false;
      } 
      var myreg = /^(((13[0-9]{1})|159|153|156|185|170)+\d{8})$/;
      if(!myreg.test(mobile)){
          alert('请输入有效的手机号码！');
          return false;
      }
      return true;
  }
	 
 $(function() {  
	 $("#forms").submit(function(){  
		 if(checkform()){
			  $(this).ajaxSubmit({  
			         type:"post",  //提交方式  
			         dataType:"json", //数据类型  
			         url: "<%=basePath%>index/activity_registerUser.do",
			         success:function(result){ //提交成功的回调函数  
			        	 $("#ffid2").val($("#ffid").val());
			        	 $("#hjId2").val(result.result.retdata.userid);
			        	 $("#form2").submit();
<%-- 			             location = "<%=basePath%>index/activity_insertUser.do?activityDto.faqiId="+$("#ffid").val()+"&activityDto.huojiangId="+result.result.retdata.userid; --%>
			         }  
			     });  
			     return false; //不刷新页面  
		 }
	 });  
	 
	 $("#validatePhoneCode").click(function(){
			var phone = $("#usercode").val();
			if(validatemobile(phone)){
				$.ajax({
					url: "<%=basePath%>index/activity_getMsg.do",
					data:{'activityDto.usercode':phone},
					type: "post", 
					dataType: "json", 
					success: function(result) {
						if(result.result.retcode==0){
							alert("信息已发送，请注意查收！");
							$("#validateCode").val(result.result.retdata);
						}else{
							alert(result.result.retmsg);
						}
					},
					error: function() {
						alert("服务器连接错误！");
					}
				});
			}
		});
 });  
 
 
</script>