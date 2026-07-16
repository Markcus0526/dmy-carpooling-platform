<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/"+"financial/manage/";
System.out.println(basePath);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/locale/easyui-lang-zh_CN.js"
	charset="UTF-8"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/easyui.css"
	type="text/css"></link>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/themes/default/combobox.css"
	type="text/css"></link>
<script type="text/javascript" charset="UTF-8">
function orderview1(order_cs_id){
	
	path='<%=basePath%>';
		window.location=path+'info.do?orderid='+order_cs_id;
	
}
function viewclose(){
	window.close();
	
}
$(function(){		
	$.ajax({
		url :'<%=basePath%>orderview.do',
			data : 'orderid=${orderid}',
			method:'post',
			success : function(jsonObject) {
				$('#ordernum').val(jsonObject.id);
				var type;//订单类型
				var status;//订单状态
			
				//console.info($('#Name'));
				$('#driverid').val(jsonObject.driver);
				$('#passengerid').val(jsonObject.passenger);
				//订单类型的判断
				if(jsonObject.order_type==1 ||jsonObject.order_type==2){
					type="临时订单";
				
					
					
				}
				if(jsonObject.order_type==3){
					type="上下班执行订单";
	
				
				}
				if(jsonObject.order_type==4){
					type="长途执行订单";
				
				}
			
		
				if(jsonObject.freeze==1){
					$("#tscheck").get(0).checked = true;
					
				}
				$('#ordertype').val(type);
				$('#oncity').val(jsonObject.order_city);	
				$('#price').val(jsonObject.price);
				$('#price1').val(jsonObject.price);
				$('#from').val(jsonObject.from_);
				$('#to').val(jsonObject.to_);
				$('#mid1').val(jsonObject.mid1);
				$('#mid2').val(jsonObject.mid2);
				$('#mid3').val(jsonObject.mid3);
				$('#mid4').val(jsonObject.mid4);
				$('#passengername').val(jsonObject.passengername);
				$('#pphone').val(jsonObject.pphone);
				$('#drivername').val(jsonObject.drivername);
				$('#dphone').val(jsonObject.pphone);
				$('#dremark').val(jsonObject.dremark);
				$('#premark').val(jsonObject.premark);
			
				var type = "";
				if(jsonObject.car==0){
					 type="无";
			
				}
				if(jsonObject.car==1){
					 type="经济型";
					
				} if(jsonObject.car==2){
					 type="舒适型";
				}if(jsonObject.car==3){
					var type="豪华型";
				} if(jsonObject.car==4){
					 type="商务型";
			
				}
				$('#carstyle').val(type);
			//订单状态的判断
			if(jsonObject.status==1){
				var status;
				status="发布";
				
			}
			if(jsonObject.status==2){
				var status;
				status="成交/待执行";
				
			}
			if(jsonObject.status==3){
				var status;
				status="开始执行";
				
			}
			if(jsonObject.status==4){
				var status;
				status="车主到达";
				
			}
			if(jsonObject.status==5){
				var status;
				status="乘客上车";
				
			}
			if(jsonObject.status==6){
				var status;
				status="执行结束/待支付";
				
			}
			if(jsonObject.status==7){
				var status;
				status="已支付/待预约";
				
			}
			if(jsonObject.status==8){
				var status;
				status="已销单（非正常完结）";
				
			}
			if(jsonObject.status==9){
				var status;
				status="结束服务";
				
			}
			$('#status').val(status);
			if(null!=jsonObject.pay_time){
				 var pay_time=gettime(jsonObject.pay_time);//乘客支付时间
					$('#pay_time').val(pay_time);
			}
			if(null!=jsonObject.stopservice_time){
				 var stopservice_time=gettime(jsonObject.stopservice_time);//结束服务时间
					$('#stopservice_time').val(stopservice_time);
			}
			if(null!=jsonObject.begin_exec_time){
				 var begin_exec_time=gettime(jsonObject.begin_exec_time);//开始执行
					$('#begin_exec_time').val(begin_exec_time);
			}
			if(null!=jsonObject.ti_accept_order){
			    var ti_accept_order=gettime(jsonObject.ti_accept_order);//成交 接单

				$('#ti_accept_order').val(ti_accept_order);
			}
			if(null!=jsonObject.cr_date){
			    var	cr_date=gettime(jsonObject.cr_date);//发布时间
				$('#cr_date').val(cr_date);
			}
			if(null!=jsonObject.driverarrival_time){
			    var driverarrival_time = gettime(jsonObject.driverarrival_time); //车主到达时间
			    $('#driverarrival_time').val(driverarrival_time);
			}
			if(null!=jsonObject.pre_time){
				 var pre_time = gettime(jsonObject.pre_time);
					$('#pre_time').val(pre_time);
			}
			    
			    
			
			
			
			
				
		
			}
		}); 
	});
 function   gettime(t){
	  var JsonDateValue = new Date(t.time);
	  var text = JsonDateValue.toLocaleString(); 
	             return text;
	         }

	</script>
</head>
<body>
	<h2 class="page-title txt-color-blueDark">
		<i class="fa-fw fa fa-pencil-square-o"></i> &nbsp;查看单次/执行订单详情
	</h2>
	<hr>
	<table style="padding: 15px; margin-top: 30px">
		<tr>
			<td>
				<table>
					<tr>
						<td style="text-align: righ; font-size: 12px">订单编号:</td>
						<td><input id="ordernum" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: righ; font-size: 12px"><span>车主ID:</span>
						</td>
						<td><input id="driverid" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: right"><span>乘客ID:</span></td>
						<td><input id="passengerid" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>

					</tr>
				</table>
			</td>
			<td>
				<table style="margin-left: 5px">
					<tr>
						<td style="text-align: righ; font-size: 12px">订单类型:</td>
						<td><input id="ordertype" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td><span style="text-align: righ; font-size: 12px">车主姓名:</span>
						</td>
						<td><input id="drivername" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: righ; font-size: 12px"><span>乘客姓名:</span>
						</td>
						<td><input id="passengername" type="text"
							style="width: 100px" class="textbox"></input></td>
					</tr>
					<tr>
					</tr>
				</table>
			</td>
			<td>
				<table style="margin-left: 5px">
					<tr>
						<td style="text-align: right">所在城市:</td>
						<td><input id="oncity" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: right"><span>车主电话:</span></td>
						<td><input id="dphone" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: right"><span>乘客电话:</span></td>
						<td><input id="pphone" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
				</table>
			</td>
			<td>
				<table style="margin-left: 5px;">
					<tr>
						<td style="text-align: right"><span>订单总价:</span></td>
						<td><input id="price" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: right"><span>乘客支付:</span></td>
						<td><input id="price1" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
					<tr>
						<td style="text-align: right"><span>乘客支付时间:</span></td>
						<td><input id="pay_time" type="text" style="width: 100px"
							class="textbox"></input></td>
					</tr>
				</table>
			</td>
			<td>
				<table style="margin-left: 5px; margin-top: 25px">
					<tr>
						<td><input id="tscheck" type="checkbox" style="width: 100px"
							class="textbox"></input><label for="enableMoney">乘客的绿点已冻结</label>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<div style="margin-top: 25px; margin-left: 35px">
		<span>订单起点:</span> <input id="from" type="text" style="width: 100px"
			class="textbox"> </input> <span style="margin-left: 20px">订单终点:</span>
		<input id="to" type="text" style="width: 100px" class="textbox"></input>
	</div>

	<div style="margin-top: 5px; margin-left: 38px">
		<span>中途点&nbsp;1:</span> <input id="mid1" type="text"
			style="width: 100px" class="textbox"></input> <span
			style="margin-left: 25px">中途点&nbsp;2:</span> <input id="mid2"
			type="text" style="width: 100px" class="textbox"></input> <span
			style="margin-left: 35px">中途点&nbsp;3:</span> <input id="mid3"
			type="text" style="width: 100px" class="textbox"></input> <span
			style="margin-left: 47px">中途点&nbsp;4:</span> <input id="mid4"
			type="text" style="width: 100px" class="textbox"></input>
	</div>

	<div style="margin-top: 5px; margin-left: 35px">
		<span>发布时间:</span> <input id="cr_date" type="text"
			style="width: 100px" class="textbox"></input> <span
			style="margin-left: 20px">成交时间:</span> <input id="ti_accept_order"
			type="text" style="width: 100px" class="textbox"></input> <span
			style="margin-left: 30px">开始执行:</span> <input id="begin_exec_time"
			type="text" style="width: 100px" class="textbox"></input>
	</div>
	<div style="margin-top: 5px; margin-left: 35px">
		<span>车主到达:</span> <input id="driverarrival_time" type="text"
			style="width: 100px" class="textbox"></input> <span
			style="margin-left: 20px">结束服务:</span> <input id="stopservice_time"
			type="text" style="width: 100px" class="textbox"></input> <span
			style="margin-left: 30px">预约出发:</span> <input id="pre_time"
			type="text" style="width: 100px" class="textbox"></input> <span
			style="margin-left: 45px">当前状态:</span> <input id="status" type="text"
			style="width: 100px" class="textbox"></input>
	</div>
	<div style="margin-top: 20px; margin-left: 35px; margin-bottom: 25px">
		<span>需求车型:</span> <input id="carstyle" type="text"
			style="width: 100px" class="textbox"></input> <span
			style="margin-left: 20px">乘客备注:</span> <input id="premark"
			type="text" style="width: 100px" class="textbox"></input> <span
			style="margin-left: 30px">车主备注:</span> <input id="dremark"
			type="text" style="width: 100px" class="textbox"></input> <input
			id="test" type="text" style="width: 100px" class="textbox"></input>
	</div>
	<div style="padding: 20px">
		<a href="#" style="margin-left: 10px; width: 100px"
			onclick="javascript:checkTrace();" class="easyui-linkbutton l-btn">查看执行轨迹</a>
		<a href="#" style="margin-left: 10px; width: 100px"
			onclick="viewclose();" class="easyui-linkbutton l-btn">关闭</a>
	</div>
</body>
</html>