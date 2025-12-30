<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>">

<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="<%=basePath%>js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>js/themes/default/easyui.css"
	type="text/css"></link>
<script>

$(function(){
	open_x();
});
function open_x(){
	var name;
	var city;
	if("全局"!="${name}"){
		name="${name}";
		city=2;
	}else{
		city=$("input[name='city']:checked").val();
		name= $('#name1').val();	
	}

	page_path='<%=basePath%>operation/city_param/';
	$.ajax({
		url:page_path+'find.do',
		data:'city='+city+'&&name='+name,
		method:'post',
		success:function(data){
			$('#total_time').val(data.total_time);
			$('#name').val(data.name);
	    	$('#driver_lock_time').val(data.driver_lock_time);
			$('#range1').val(data.range1);
			$('#range2').val(data.range2);
			$('#range3').val(data.range3);
			$('#turn1_time').val(data.turn1_time);
			$('#turn2_time').val(data.turn2_time);
			$('#a1').val(data.a1);
			$('#a2').val(data.a2);
			$('#t1').val(data.t1);
			$('#t2').val(data.t2);
			$('#d1').val(data.d1);
			$('#d2').val(data.d2);
			$('#b1').val(data.b1);
			$('#b2').val(data.b2);
			$('#b4').val(data.b4);
			$('#c1').val(data.c1);
			$('#c2').val(data.c2);
			$('#e1').val(data.e1);
			$('#e2').val(data.e2);
			$('#e4').val(data.e4);
			$('#f1').val(data.f1);
			$('#f2').val(data.f2);
			$('#g1').val(data.g1);
			$('#g2').val(data.g2);
			$('#g3').val(data.g3);
			$('#g4').val(data.g4);
			$('#g5').val(data.g5);		
			$('#c3').val(data.b4);
			$('#c4').val(data.b4);
			$('#b3').val(data.a2);
			$('#e3').val(data.d2);
			$('#f3').val(data.e4);
			$('#f4').val(data.e4);
			$('#add_price_time1').val(data.add_price_time1);
			$('#add_price_time2').val(data.add_price_time2);
			$('#add_price_time3').val(data.add_price_time3);
			$('#add_price_time4').val(data.add_price_time4);
			$('#add_price_time5').val(data.add_price_time5);
			$('#same_price_time1').val(data.same_price_time1);
			$('#same_price_time2').val(data.same_price_time2);
			$('#same_price_time3').val(data.same_price_time3);
			$('#same_price_time4').val(data.same_price_time4);
			$('#same_price_time5').val(data.same_price_time5);
			var active=data.active;
			if(active==1){
				$('#active').combobox('select',data.active);
				$('#msg').val(data.integer_);
			}if(active==0){
				$('#active').combobox('select',data.active);
				$('#msg').val(data.ratio);
			}
			var price_limit_active=data.price_limit_active;
		   if(price_limit_active==0){
			   $('#price_limit_active').combobox('select',data.price_limit_active);
			   $('#low').val(data.price_limit_ratio);
		   }if(price_limit_active==1){
			   $('#price_limit_active').combobox('select',data.price_limit_active);
			   $('#low').val(data.price_limit_integer);
		   }
			var points_per_add_active=data.points_per_add_active;
			   if(points_per_add_active==0){
				   $('#points_per_add_active').combobox('select',data.points_per_add_active);
				   $('#high').val(data.points_per_add_ratio);
			   }if(points_per_add_active==1){
				   $('#points_per_add_active').combobox('select',data.points_per_add_active);
				  
				   $('#high').val(data.points_per_add_integer);
			   }
			   var name =data.name;
			   if(name==""){
				   document.getElementById("city1").checked = true;
					$('#name').val("全局");
			   }else{
					$('#name').val(data.name);
					$('#name1').val(data.name);
					document.getElementById("city2").checked = true; 
			   }
			   
		}
	});
}
 function change1(){
	  var b4 = $('#b4').val();
	  var a2 = $('#a2').val();
	  var a1 = $('#a1').val();
	  var b2 = $('#b2').val();
	  if (/^(\+|-)?\d+($|\.\d+$)/.test( b4)
			  &&/^(\+|-)?\d+($|\.\d+$)/.test( a2)
					  &&/^(\+|-)?\d+($|\.\d+$)/.test( a1)
							  &&/^(\+|-)?\d+($|\.\d+$)/.test( b2)){
		  $('#b3').val(a2);
		  $('#c3').val(b4);
		  $('#c4').val(b4);
		  $('#b1').val(a1);
		  a1=parseInt(a1);
		 var n=(a1+(b4-a2)*b2);
		  $('#c1').val(n);
	  }   else{
		  alert("请输入数字!!");
	  } 

	 }
 function change2(){
	  var d1 = $('#d1').val();
	  var d2 = $('#d2').val();
	  var e2 = $('#e2').val();
	  var e4 = $('#e4').val();
	  if (/^(\+|-)?\d+($|\.\d+$)/.test( d1)
			  &&/^(\+|-)?\d+($|\.\d+$)/.test( d2)
					  &&/^(\+|-)?\d+($|\.\d+$)/.test( e2)
							  &&/^(\+|-)?\d+($|\.\d+$)/.test( e4)){
		  $('#e1').val(d1);
		  $('#e3').val(d2);
		  $('#f3').val(e4);
		  $('#f4').val(e4);
		   d1 =parseInt(d1);
		  var n=(d1+(e4-d2)*e2); 
		 $('#f1').val(n);
	  }   else{
		  alert("请输入数字!!");
	  } 
	 }
 function submit1(thisForm){
	  msg='确认保存？';
	   if(window.confirm(msg)) {
	   return true;
	   }else{
		   return false;
	   }
 }
	function loaddone() {
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	}
</script>
</head>
<body onload="loaddone()">
	<h2 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;城市参数设定
	</h2>
	<hr>
	<div style="height: 30; padding: 10px;">
		<input id="city1" type="radio" name="city" value="1" checked>全局设置<input
			id="city2" type="radio" name="city" value="2">城市单独设置 <input
			type="text" id="name1" name="name1" /> <a href="javascript:void(0)"
			onclick="open_x()" style="margin-left: 50px; width: 80px;"
			class="easyui-linkbutton" iconCls="icon-search">查询</a>
	</div>
	<hr>
	<div style="margin-left: 20px">
		<form action="<%=basePath%>operation/city_param/save.do" method="post"
			onSubmit="return submit1(this)">
			<h3>
				当前城市:<input type="text" id="name" name="name"
					style="border: 0px; background-color: transparent; font-size: 20px; margin-left: 10px"
					readonly="readonly" />
			</h3>
			<h4>播报设置</h4>

			<table style="padding: 10px; margin-left: 20px">
				<tr>
					<td style="width: 230">订单发布时间 :<input type="text"
						style="width: 35; margin-left: 50" id="total_time"
						name="total_time" />秒
					</td>
					<td style="width: 250">车主锁定时间:<input type="text"
						style="width: 35; margin-left: 65" id="driver_lock_time"
						name="driver_lock_time" />秒
					</td>
					<td style="width: 230"></td>
				</tr>
				<tr>
					<td>第一次播报范围 :<input type="text"
						style="width: 35; margin-left: 35" id="range1" name="range1" />千米
					</td>
					<td>第二次播报范围:<input type="text" id="range2"
						style="width: 35; margin-left: 50" name="range2" />千米
					</td>
					<td>第三次播报范围:<input type="text"
						style="width: 35; margin-left: 15" id="range3" name="range3" />千米
					</td>
				</tr>
				<tr>
					<td>一二次播报时间间隔:<input type="text"
						style="width: 35; margin-left: 5" id="turn1_time"
						name="turn1_time" />秒
					</td>
					<td>二三次播报时间间隔:<input type="text"
						style="width: 35; margin-left: 15" id="turn2_time"
						name="turn2_time" />秒
					</td>
					<td></td>
				</tr>
				<tr>
					<td>加价1次发布时间 :<input type="text" id="add_price_time1"
						style="width: 35; margin-left: 20" name="add_price_time1" />秒
					</td>
					<td>加价2次发布时间 :<input type="text"
						style="width: 35; margin-left: 30" id="add_price_time2"
						name="add_price_time2" />秒
					</td>
					<td>加价3次发布时间 :<input type="text" style="width: 35"
						id="add_price_time3" name="add_price_time3" />秒
					</td>
				</tr>
				<tr>
					<td style="width: 230">加价4次发布时间 :<input type="text"
						style="width: 35; margin-left: 25" id="add_price_time4"
						name="add_price_time4" />秒
					</td>
					<td>加价5次发布时间 :<input type="text" id="add_price_time5"
						style="width: 35; margin-left: 35" name="add_price_time5" />秒
					</td>
				</tr>
				<tr>
					<td>平价1次发布时间 :<input type="text" id="same_price_time1"
						style="width: 35; margin-left: 20" name="same_price_time1" />秒
					</td>
					<td>平价2次发布时间 :<input type="text"
						style="width: 35; margin-left: 30" id="same_price_time2"
						name="same_price_time2" />秒
					</td>
					<td>平价3次发布时间 :<input type="text" style="width: 35"
						id="same_price_time3" name="same_price_time3" />秒
					</td>
				</tr>
				<tr>
					<td>平价4次发布时间 :<input type="text"
						style="width: 35; margin-left: 25" id="same_price_time4"
						name="same_price_time4" />秒
					</td>
					<td>平价5次发布时间 :<input type="text" id="same_price_time5"
						style="width: 35; margin-left: 35" name="same_price_time5" />秒
					</td>
				</tr>
			</table>
			<h4>信息费设置</h4>
			<div style="margin-left: 20px">
				信息费:<input type="text" id="msg" style="width: 30" name="msg" /> <select
					id="active" name="active" class="easyui-combobox" style="width: 80">
					<option value="0">%*总价</option>
					<option value="1">点/订单</option>
				</select>
			</div>
			<h4>平台平均报价</h4>
			<table style="margin-left: 20px">
				<tr>
					<td>平台平均价公式（白天）：</td>
					<td><input type="text" style="width: 35" id="a1" name="a1"
						onblur="change1()" /></td>
					<td>里程&lt;<input type="text" id="a2" style="width: 35"
						name="a2" onblur="change1()" />公里
					</td>
				</tr>
				<tr>
					<td style="width: 200"></td>
					<td style="width: 250"><input type="text" id="b1"
						style="width: 35" name="b1" readonly="readonly" />+<input
						type="text" id="b2" style="width: 35" name="b2"
						onblur="change1( )" />*(里程-<input type="text" style="width: 35"
						id="b3" style="width:35" name="b3" readonly="readonly" />)</td>
					<td style="width: 250">里程&lt;<input type="text" id="b4"
						style="width: 35" name="b4" onblur="change1( )" />公里
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="text" id="c1" style="width: 35" name="c1"
						readonly="readonly" />+<input type="text" id="c2"
						style="width: 35" name="c2" />*(里程-<input type="text" id="c3"
						style="width: 35" name="c3" readonly="readonly" />)</td>
					<td>里程&lt;<input type="text" id="c4" style="width: 35"
						name="c4" readonly="readonly" />公里
					</td>
				</tr>
				<tr>
					<td>平台平均价公式（夜晚）：</td>
					<td><input id="t1" name="t1" style="width: 100px"
						class="easyui-timespinner "
						data-options="value:'23:00:00',showSeconds: true" />--<input
						id="t2" name="t2" style="width: 100px" class="easyui-timespinner "
						data-options="value:'06:00:00',showSeconds: true" /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="text" id="d1" style="width: 35" name="d1"
						onblur="change2( )" /></td>
					<td>里程&lt;<input type="text" id="d2" style="width: 35"
						name="d2" onblur="change2( )" />公里
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="text" id="e1" style="width: 35" name="e1"
						readonly="readonly" />+<input type="text" id="e2"
						style="width: 35" name="e2" onblur="change2( )" />*(里程-<input
						type="text" id="e3" style="width: 35" name="e3"
						readonly="readonly" />)</td>
					<td>里程&lt;<input type="text" id="e4" style="width: 35"
						name="e4" onblur="change2( )" />公里
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="text" id="f1" style="width: 35" name="f1"
						readonly="readonly" />+<input type="text" id="f2"
						style="width: 35" name="f2" />*(里程-<input type="text" id="f3"
						style="width: 35" name="f3" readonly="readonly" />)</td>
					<td>里程&gt;<input type="text" id="f4" style="width: 35"
						name="f4" readonly="readonly" />公里
					</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;整体系数:<input
						type="text" id="g1" style="width: 35;" name="g1" />%
					</td>
					<td>经济型加权系数:<input type="text" id="g2" style="width: 35"
						name="g2" />%
					</td>
				</tr>
				<tr>
					<td>舒适型加权系数:<input type="text" style="width: 35" id="g3"
						name="g3" />%
					</td>
					<td>豪华型加权系数:<input type="text" id="g4" style="width: 35"
						name="g4" />%
					</td>
					<td>商务型加权系数:<input type="text" id="g5" style="width: 35"
						name="g5" />%
					</td>
				</tr>
			</table>
			<h4>报价设置</h4>
			<div style="margin-left: 20px">
				平台推荐价加价幅度:<input type="text" id="high" style="width: 30" name="high" />
				<select id="points_per_add_active" name="points_per_add_active"
					class="easyui-combobox" style="width: 70px;">
					<option value="0">%*总价</option>
					<option value="1">点/订单</option>
				</select> 平台最低限价:<input type="text" id="low" style="width: 30" name="low" />
				<select id="price_limit_active" name="price_limit_active"
					class="easyui-combobox" style="width: 70px;">
					<option value="0">%*总价</option>
					<option value="1">点/订单</option>
				</select>
			</div>
			<div style="padding: 10px; margin-left: 20; height: 30">
				<input type="submit" value="保存"
					style="margin-left: 5px; height: 30; width: 80">
			</div>

		</form>
	</div>
</body>
</html>