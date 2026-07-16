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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
</head>

<body>


<br/>
		<h2 style="margin-left:10px;margin-bottom:0px">活动参数设置</h2>
<hr/>

<form id="staticsForm" method="post" action="<%=basePath%>/spread/activity/activity_updateActivity.do">
	<input type="hidden" name="id" value="${id}"/>
	<div style="padding: 10px 20px 0px 20px">
		<span>活动名称${activity.name}1:</span>
		<input type="text" name="activity.name" value="${activity.name}"/>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>活动时限:</span>
		<input id="beginDate" name="activity.startTime" class="easyui-datetimebox" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${activity.startTime}"/>"
			style="width:145px;" data-options="formatter:mydateformatter"/>&nbsp;~
		<input id="endDate" name="activity.endTime" class="easyui-datetimebox" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${activity.endTime}"/>"
			style="width:145px;" data-options="formatter:mydateformatter"/>	
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>分享有效时间(小时):</span>
		<input type="text" name="activity.shareTime" value="${activity.shareTime}"/>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>分享有效人数:</span>
		<input type="text" name="activity.sharePeople" value="${activity.sharePeople}"/>
	</div>
	<div style="padding: 10px 20px 0px 20px">
		<span>奖品设置:</span>
		<input type="radio" name="activity.prizeType" value="0" <c:if test="${activity.prizeType==0}">checked="checked"</c:if> id="aaa"/>点卷
		<input type="radio" name="activity.prizeType" value="1" <c:if test="${activity.prizeType==1}">checked="checked"</c:if> id="bbb"/>第三方奖品
	</div>
	<div id="gift" style="display: none;">
		<div style="padding: 10px 20px 0px 20px">
			<span>奖品名称:</span>
			<input type="text" name="activity.lotteryName" value="${activity.lotteryName}"/>
		</div>
		</div>
		
	<div id="dianjuan">
		<div style="padding: 10px 20px 0px 20px">
			<span>奖品设置：</span>
			<input type="button" value="查看点卷" onclick="showGift();"/>	
		</div>
		说明：第0名为活动发起人
			<div style="padding: 10px 20px 0px 20px">
			<span>已选点卷:</span>
			<input type="text" name="giftNames" value="${giftNames}" readonly="readonly"/>
<%-- 			<input type="hidden" name="chooseIds" value="${chooseIds}" /> --%>
			<input type="button" value="添加新规则" onclick="addRule(${id});"/> 
		</div>
		<div style="padding:10px 0px 30px 0px">
			<table id="loanList" class="easyui-datagrid" style="width:auto; height:auto" data-options="singleSelect:true">
			</table>
		</div>
	</div>
	
	<td colspan="4" style="text-align:center">
					<a href='javascript:void(0)' onclick="enterActivity()" class="easyui-linkbutton l-btn" style="width:90px" >&nbsp;确定&nbsp;</a>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href='javascript:void(0)' onclick="cancel()" class="easyui-linkbutton l-btn" style="width:90px">&nbsp;取消&nbsp;</a>
	</td> 
</form>


     <div id="dlg" class="easyui-dialog"
		style="width:800px; height:500px; padding: 10px 20px; top:30px"
		closed="true" buttons="#dlg-buttons">
		
		<form id="fm" method="post" action="<%=basePath%>spread/activity/activity_choseGiftForUpdate.do">
		<div data-options="region:'north'" style="width:55%">
			
				
			<div style="position:relative;border:0px red solid;width:100%;height:100px;margin-left:auto;margin-right:auto;margin-top:5px">
				<div style="position:absolute;top:0px;left:1%;border:0px red solid;width:10%;height:25px;">
					<font style="font-weight:normal;font-size:80%" >点券ID</font>
				</div>
				<div style="position:absolute;top:0px;left:12%;border:0px red solid;width:20%;height:25px;">
					<input  class="easyui-validatebox textbox" style="width: 90%;height:20px" id="id"></input>
				</div>
				<div style="position:absolute;top:0px;left:33%;border:0px red solid;width:10%;height:25px;">
					<font style="font-weight:normal;font-size:80%" >点数</font>
					</div>
				<div style="position:absolute;top:0px;left:44%;border:0px red solid;width:20%;height:25px;">
					<input class="easyui-validatebox textbox" style="width: 90%;height:20px" id="limit_count"></input>
				</div>
				<div style="position:absolute;top:0px;left:65%;border:0px red solid;width:20%;height:25px;">
					<font style="font-weight:normal;font-size:80%" >有效期</font>
					</div>
					<div style="position:absolute;top:0px;left:76%;border:0px red solid;width:50%;height:25px;">
					<input  class="easyui-validatebox textbox" style="width: 40%;height:20px" id="valid_period"></input>
					<a id="btn"  class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="aaa()" style="width: 50%;height:20px">查&nbsp;询</a>  
				</div>
				
				<div style="position:absolute;top:25px;left:1%;border:0px red solid;width:10%;height:25px;">
					<font style="font-weight:normal;font-size:80%" >已选点卷</font>
				</div>
				<div style="position:absolute;top:25px;left:12%;border:0px red solid;width:60%;height:25px;">
					<input type="text" name="giftNames" class="easyui-validatebox textbox" style="width: 300%;height:20px" id="giftNames" value=""></input>
					<input name="giftIds" id="giftIds" type="text" readonly="readonly"/>
					<input name="id" type="hidden" value="${id}" readonly="readonly" id="chooseId"/>
				</div>
				<div style="position:absolute;top:160px;left: 0%;border:0px red solid;width:170%;height:180px;">
					<table id="tb_select"  style="width:auto;height:180px"> </table>
				</div>
			
<!-- 				<div style="position:absolute;top:350px;left: 0%;border:0px red solid;width:170%;height:180px;"> -->
<!-- 						<td colspan="4" style="text-align:center"> -->
<!-- 							<a href='javascript:void(0)' onclick="enter()" class="easyui-linkbutton l-btn" style="width:90px" >&nbsp;确定&nbsp;</a> -->
<!-- 								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
<!-- 							<a href='javascript:void(0)' onclick="cancel()" class="easyui-linkbutton l-btn" style="width:90px">&nbsp;取消&nbsp;</a> -->
<!-- 						</td>  -->
<!-- 				</div> -->
			</div>
			</div>
		</form>
	</div>
	
	
	
	<div id="ruleDiv" class="easyui-dialog"
		style="width:800px; height:500px; padding: 10px 20px; top:30px"
		closed="true" buttons="#dlg-buttons">
		
		<form id="ruleFm" method="post" action="<%=basePath%>spread/activity/activity_insertActivityRule.do">
			<input type="hidden" name="id" id="ruleDivId" value="${id}">
			<div style="position:relative;border:0px red solid;width:100%;height:100px;margin-left:auto;margin-right:auto;margin-top:5px">
						
			<div style="padding: 10px 20px 0px 20px">
				<span>选择点卷:</span>
				<select name="activityRule.activityCouponId">
						<c:forEach items="${activity.listActivitySyscoupon}" var="gift">
							<option value="${gift.id}">${gift.syscouponCode}</option>
						</c:forEach>
				</select>
			</div>
			
			<div style="padding: 10px 20px 0px 20px">
				<span>开始人数:</span>
				<input type="text" name="activityRule.startNum" />
			</div>

			<div style="padding: 10px 20px 0px 20px">
				<span>结束人数:</span>
				<input type="text" name="activityRule.endNum" />
			</div>
			
				<div style="position:absolute;top:350px;left: 0%;border:0px red solid;width:170%;height:180px;">
						<td colspan="4" style="text-align:center">
							<a href='javascript:void(0)' onclick="enterRule()" class="easyui-linkbutton l-btn" style="width:90px" >&nbsp;确定&nbsp;</a>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a href='javascript:void(0)' onclick="cancelRule()" class="easyui-linkbutton l-btn" style="width:90px">&nbsp;取消&nbsp;</a>
						</td> 
				</div>
			</div>
			</div>
		</form>
	</div>
<hr/>
<script>
function mydateformatter(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}

	var page_path = '${pageContext.request.contextPath}/statistics/loan/';
	
	$('.easyui-combobox').combobox();
	$('.easyui-linkbutton').linkbutton();
	$('.easyui-datetimebox').datetimebox();

	var dataLoader = {};
	var staLoader = {};
	
		dataLoader.load = function(){
			$('#loanList').datagrid({
				url: '<%=basePath%>/spread/activity/activity_listActivityRole.do?id=${id}',
				loadMsg : '数据处理中，请稍后....',
				pagination: false,
				fitColumns : true,
				columns:[[
				          {field:'operation',title:'操作',width:100, align:'center'},
				          {field:'ruleNum',title:'奖励对象',width:200,align:'center'},
				          {field:'gift',title:'奖品',width:100,align:'center'},
				]],
				onHeaderContextMenu: function(e, field){
					e.preventDefault();
					if (!cmenu){
						createColumnMenu();
					}
					cmenu.menu('show', {
						left:e.pageX,
						top:e.pageY
					});
				}
			});
		};
//	});
	
	function myformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
	}
	
	function mytimeformatter(date){
		var h = date.getHours();
		var M = date.getMinutes();
		var s = date.getSeconds();
		return (h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);
	}
	
	function dategridTimeFormat(dateObj) {
		var h = dateObj.hours;
		var M = dateObj.minutes;
		var s = dateObj.seconds;
		var y = dateObj.year;
		if (y < 1900)
			y += 1900;
		var m = dateObj.month+1;
		var d = dateObj.date;
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(M<10?('0'+M):M)+':'+(s<10?'0'+s:s);		
	}

	dataLoader.load();
	
	function showGift() {
	    $("#dlg").dialog("open").dialog('setTitle', '添加点卷');
// 	    $("#fm").form("clear");
	}
	
	   var num=0;
		function loaddone(){
			var hiddenmsg = parent.document.getElementById("hiddenmsg"); 
			hiddenmsg.style.display="none";
		}
		
	 	function myformatter(date){
			var h = date.getHours();
			var M = date.getMinutes();
			var s = date.getSeconds();
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
		} 
		
		$(function(){
			var s = $('input:radio:checked').val();
			if(s==0){
				$("#gift").hide();
				$("#dianjuan").show();
			}else if(s==1){
				$("#dianjuan").hide();
				$("#gift").show();	
			}
			$('#tb_select').datagrid({
				url: '<%=basePath%>spread/activity/searchGift.do',
			    loadMsg : '数据处理中，请稍后....',
				fitColumns: true,
				pagination : true,
				remoteSort : false,
				striped : true,
				rownumbers:true,
				selectOnCheck : true,
				checkOnSelect : true, 
				idField : 'id',
				columns:[[
		             {field:'ck', checkbox:true,id:'id',value:'id'},
		             {
		 				field:'syscoupon_code',title:'点券编号',width:200, align:'center'
		 			},{
		 				field:'release_channel',title:'发放渠道',	width:200,	align:'center',
		 				formatter : function(value, row, index) {
		 					var str = '';
		 						if(row.release_channel==1){
		 						str += 'App';
		 						}
		 						if(row.release_channel==2){
		 						str += row.app_spread_unit_name;
		 						}
		 							return str;
		 							if(row.id){return row.id;}
		 					}
		 			},{
		 				field:'content',title:'内容',width:150,align:'center',
		 				formatter : function(value, row, index) {
		 					var str = '';
		 						if(row.goods_or_cash==1){
		 						str += row.sum+'点';
		 						}
		 						if(row.goods_or_cash==1){
		 						str += row.goods;
		 						}
		 							return str;
		 							if(row.id){return row.id;}
		 					}
		 			},{
		 				field:'goods_or_cash',title:'类别',width:150,align:'center',
		 				formatter : function(value, row, index) {
		 					var str = '';
		 						if(value==1){
		 						str += '绿点';
		 						}
		 						if(value==2){
		 						str += '实物';
		 						}
		 							return str;
		 							if(row.id){return row.id;}
		 					}
		 			},{
		 				field:'coupon_type',title:'使用条件',width:250,align:'center',
		 				formatter : function(value, row, index) {
		 					var str = '';
		 						if(row.coupon_type==0){
		 						str += '无条件';
		 						}
		 						if(row.coupon_type==1){
		 						str += '订单金额超过'+row.limit_val+'元就可以用一张';
		 						}
		 						if(row.coupon_type==2){
		 						str += '不能与其他点券并用';
		 						}
		 						if(row.coupon_type==3){
		 						str += '每个订单只能用一张';
		 						}

		 							return str;
		 							if(row.id){return row.id;}
		 					}
		 			},{
		 				field:'valid_period',title:'有效期',width:150,align:'center',
		 				formatter : function(value, row, index) {
		 					var str = '';
		 						if(row.valid_period_unit==1){
		 						str += value+'日';
		 						}
		 						if(row.valid_period_unit==2){
		 						str += value+'周';
		 						}
		 						if(row.valid_period_unit==3){
		 						str += value+'月';
		 						}
		 						if(row.valid_period_unit==4){
		 						str += value+'年';
		 						}
		 							return str;
		 							if(row.id){return row.id;}
		 					}
		 			},{
		 				field:'deploy_cnt',title:'发放次数',width:150,align:'center'
		 			},{
		 				field:'use_cnt',title:'使用次数',width:150,align:'center'
		 			}]],
				onCheck : function(index,row){
					$('#giftNames').val($('#giftNames').val()+row.syscoupon_code+",");
					$('#giftIds').val($('#giftIds').val()+row.id+",");
				},
				onUncheck : function(index,row){
					var giftNames = $('#giftNames').val();
					var giftIds = $('#giftIds').val();
					if(giftNames==row.syscoupon_code+","){
						$('#giftNames').val(yxdj.replace(row.syscoupon_code+",",""));
					}else{
						$('#giftNames').val(yxdj.replace(row.syscoupon_code+",",""));
					}
					if(giftIds==row.id+","){
						$('#giftIds').val(giftIds.replace(row.id+",",""));
					}else{
						$('#giftIds').val(giftIds.replace(row.id+",",""));
					}
// 					var listid=$('#idList').val();
// 					var id =row.id;
// 					var list=listid.split(id+",",1000);
// 					listid=list[0]+list[1];
// 					$('#idList').val(listid);
// 					  num=num-1;
// 					$('#num').val(num);
// 					alert(listid);
				},
				onCheckAll : function(rows){
// 					var listid="";
// 					num=0;
// 					$.each(rows, function(index, rows){
// 						listid+=rows.id+",";
// 						num+=1;
// 						}); 
// 					$('#idList').val(listid);
// 					$('#num').val(num);
// 					alert(listid);
				},
				onUncheckAll : function(rows){
// 					num=0;
// 					$('#num').val(num);
				},
		 		onHeaderContextMenu: function(e, field){
					e.preventDefault();
					if (!cmenu){
						createColumnMenu();
					}
					cmenu.menu('show', {
						left:e.pageX,
						top:e.pageY
					});
				}
		     });
			
			$("#aaa").click(function(){
				$("#dianjuan").show();
				$("#gift").hide();
			});
			$("#bbb").click(function(){
				$("#dianjuan").hide();
				$("#gift").show();
			});
		});	

		function aaa(){
			var id=0,valid_period=0,limit_count=0;
		     id = $('#id').val();
		     valid_period = $('#valid_period').val();
		     limit_count = $('#limit_count').val();
		    $('#tb_select').datagrid('load',{
		    	id :id,
		    	valid_period  :valid_period,
		    	limit_count:limit_count
		    });
			
		}
			
		
		function myformatter(date){
			var h = date.getHours();
			var M = date.getMinutes();
			var s = date.getSeconds();
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+':'+(M<10?('0'+M):M)+':'+(s<10?('0'+s):s);
		}

	
		function enter(){
			$("#fm").submit();
		}
		
		function cancel(){
			
		}
		
		function enterRule(){
			$("#ruleFm").submit();
		}
		
		function enterActivity(){
			$("#staticsForm").submit();
		}
		
		function addRule(id){
			  $("#ruleDiv").dialog("open").dialog('setTitle', '添加规则');
		}
		
		function delRule(ruleId){
			var url = "<%=basePath%>spread/activity/activity_delRule.do?id=${id}&ruleId="+ruleId;
			location = url;
		}
</script>
</body>
</html>

