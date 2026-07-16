<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<style>
.info {
	position: absolute;
	left: 600px;
	top: 160px;
}

.err_msg {
	border-color: #dd0000;
	color: #bb0000;
}
</style>


<div class="info" style="text-align: center;">
	<span id="err_msg" class="err_msg">错误发生</span> <br /> <br /> <a
		href="${pageContext.request.contextPath}/index.do">返&nbsp;&nbsp;回</a>
</div>

