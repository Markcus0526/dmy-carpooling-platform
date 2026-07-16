<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>司机补贴</title>
    
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"
charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.easyui.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=basePath%>js/dz_util.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="<%=basePath%>js/locale/easyui-lang-zh_CN.js" charset="UTF-8"></script>
<link rel="stylesheet" href="<%=basePath%>js/themes/icon.css"
	type="text/css"></link>
<link rel="stylesheet" href="<%=basePath%>js/themes/default/easyui.css"
	type="text/css"></link>	
<style>
.rule_box {
	display: none;
	background-color: white;
	border: 1px solid #ccc;
	padding: 5px;
	box-shadow: 5px 5px 10px #ccc;
	-moz-box-shadow: 5px 5px 10px #ccc;
	-webkit-box-shadow: 5px 5px 10px #ccc;
	line-height: 30px;
	margin-bottom: 10px;
	height: 120px;
	min-width: 680px;
	_width: 680px;
}

.inputbox {
	-moz-border-radius: 5px 5px 5px 5px;
	-webkit-border-radius: 5px 5px 5px 5px;
	border-radius: 5px 5px 5px 5px;
	border: 1px solid #95B8E7;
}

.sbutton {
	margin-left: 5px;
	height: 20px;
	width: 60px;
}

.timespinner{
   width: 85px;
}
</style>	
<script>
	//拥堵费部分的script
	var RulesManager = function(mainDivSelector, editAction, removeAction, loadAction, addBtn) {
		this.divLen = 0;
		this.mainDiv = $(mainDivSelector);//$("#crowd-fee");
		this.dummyDivHtml = $("#dummy", this.mainDiv)[0].outerHTML;
		this.pagePath = '<%=basePath%>operation/allowance/';
		this.hlBorder = "5px solid red";//高亮时的border
		this.oriBorder = $("#dummy", this.mainDiv).css("border");//平时的Border
    
    //给所有按钮绑定事件
    var me = this;
    $(document).delegate(addBtn, "click", me, function(){
       var div = me.addDiv();
       $(".timespinner", div).timespinner({
			   showSeconds : true,
			   highlight : 0
		       });
	   $(".easyui-validatebox", div).validatebox();   
	   $("form", div).form();    
	   return false;
    });
   
	$(this.mainDiv).delegate(".edit", "click", me, function() {
	     var button = $(this);
	     var div = button.parentsUntil(button, ".fee_rule");
		 me.highlight(div);
         top.$.messager.confirm("提示", "确认要保存吗", function(data){
         if(data){
			var form = button.parentsUntil(".fee_rule", "form");
			var param = form.form2json();
			if (!form.form("validate")) {
			    me.removeHighlight(div);
				return;
			}
			
			param.cityCode = RulesManager.cityCode;
			$.ajax({
				url : me.pagePath + editAction,//"editCrowdFee.do",
				data : param,
				method : 'post',
				success : function(data) {
					if ("1" == data.count) {
						$("[name='id']", form).val(data.id);
						top.$.messager.alert("提示","保存成功","info", function(){ me.removeHighlight(div);});
					} else if (parseInt(data.count) < 0) {
						top.$.messager.alert("提示","保存失败：时间段不可重叠","info", function(){ me.removeHighlight(div);});
					} else if (data.msg){
						top.$.messager.alert("提示","保存失败 :" + data.msg,"info", function(){ me.removeHighlight(div);});
					} else if (data.indexOf && data.indexOf("<title>OO车生活后台管理系统</title") > 0){//重新登录
					    top.location.href = "<%=basePath%>";
					}
				},
				error : function(data) {
				    me.removeHighlight(div);
					top.$.messager.alert("错误:", data.responseText, "error", function(){ me.removeHighlight(div);});
				}
			});
			}else{
			  me.removeHighlight(div);
			}
			});
			return false;
		});

		$(this.mainDiv).delegate(".remove", "click", function() {
		    var button = $(this);
		    var div = button.parentsUntil(button, ".fee_rule");
		    me.highlight(div);
			top.$.messager.confirm("提示", "确认要删除吗", function(data){
            if(data){
			  var form = button.parentsUntil(".fee_rule", "form");
			  var param = form.form2json();
			  if(!param.id){//还没有保存,直接删除
				  me.removeDiv(div);
				  return false;
			  }
			  
			  $.ajax({
				url : me.pagePath + removeAction,
				data : param,
				method : 'post',
				success : function(data) {
				    if (data.indexOf && data.indexOf("<title>OO车生活后台管理系统</title") > 0){//重新登录
					    top.location.href = "<%=basePath%>";
					}else if ("1" == data.count) {
						me.removeDiv(div);
					} else {
						top.$.messager.alert("删除失败");
					} 
				},
				error : function(data) {
					top.$.messager.alert("错误", data.responseText, "error");
					me.removeHighlight(div);
				}
			});
			}else{
			  me.removeHighlight(div);
			}
			});
			return false;
		});
		
		this.getLoadAction = function(){return loadAction;}
	}
	
	RulesManager.prototype.highlight = function(div){
	  div.css("border", this.hlBorder);
	}
	
    RulesManager.prototype.removeHighlight = function(div){
      div.css("border", this.oriBorder);
	}

	RulesManager.prototype.addDiv = function(duration) {
	    var _duration = duration ? duration : 500; 
		this.mainDiv.prepend(this.dummyDivHtml);
		var curDiv = $(".fee_rule:first", this.mainDiv).attr("id",
				++this.divLen);//添加id
	    
	    var oriHeight = curDiv.css("height");
	    curDiv.css("height", "0");
		curDiv.css("display", "block");//可见
		curDiv.animate({height:oriHeight}, _duration);
		return curDiv;
	}

	RulesManager.prototype.removeDiv = function(div, duration) {
	    var _duration = duration ? duration : 500; 
	    div.animate({height:"0px"}, _duration, function(){
	      div.remove();
	    });
	}

	RulesManager.prototype.empty = function() {
		this.mainDiv.empty();
	}

	RulesManager.prototype.loadRules = function() {
		var me = this;
		$.ajax({
			url : this.pagePath + this.getLoadAction(),//"loadCrowdFee.do",
			data : {
				cityCode : RulesManager.cityCode
			},
			method : 'post',
			success : function(data) {
			    if (data.indexOf && data.indexOf("<title>OO车生活后台管理系统</title") > 0){//重新登录
					    top.location.href = "<%=basePath%>";
			    }
					
				//添加div,显示数据
				var index = 0;
				var duration = 0;
				data.rows.length && (RulesManager.cityCode=data.rows[0].cityCode) 
				      && setTimeout(function a() {
				    if(!data.rows[0].beginTime){
				     return;
				    } 
				      
					var div = me.addDiv(duration);
					var row = data.rows[index];
					for ( var field in row) {
					    var element = $("[name='" + field + "']", div);
					    if(element.hasClass("timespinner")){
					       element.timespinner({
								value : row[field],
								showSeconds : true,
								highlight : 0
							});
					    }else if(element.attr("type")=="radio"){
					       for(var i=0; i < element.length; i ++){
					         element.eq(i).val()==row[field] && element.eq(i).attr("checked", true);
					       }
					    }else{
					       element.val(row[field]);
					    }	
					}
					$(".easyui-validatebox", div).validatebox();
					++index < data.rows.length && setTimeout(a, duration);
				}, duration);
			},
			error : function(data) {
				$.messager.alert("错误:", data.responseText, "error");
			}
		});
	}

	RulesManager.hideAll = function() {
		$("#city_rules").hide();
	};

	RulesManager.showAll = function() {
		$("#city_rules").show();
	};
	
	RulesManager.allCityCode = "all";
	RulesManager.allCityName = "全局";
	RulesManager.reload = function(isVisible, cityCode){
	   RulesManager.cityCode = cityCode;
	   allowM.empty();
	   isVisible && (RulesManager.showAll()||true) && allowM.loadRules();   
	   !isVisible && RulesManager.hideAll();
	   
	   (cityCode == RulesManager.allCityCode && $("#cityName").html(RulesManager.allCityName))
	     || $("#cityName").html(cityCode);
	};

	$(function() {
		$.extend($.fn.validatebox.defaults.rules, {
			min_max : {
				validator : function(value, param) {
					return Number(value) >= Number(param[0])
							&& Number(value) <= Number(param[1]);
				},
			message : "取值范围:{0}-{1}"
			},
		    str_len : {
				validator : function(value, param) {
					return value && value.length && value.length < param[0];
				},
			message : "长度应小于{0}"
			}
		});

        $("#search").click(function(e){
           var cityCode = $("#cityCode").val();
           ($("#scope1").prop("checked")) && (cityCode = RulesManager.allCityCode);
           ($("#scope2").prop("checked")) && (!cityCode) && (cityCode = RulesManager.allCityCode);
           RulesManager.reload(true, cityCode);  
        });
        $("#scope1").click(function(e){
           RulesManager.reload(true, RulesManager.allCityCode);
           $("#cityName").html(RulesManager.allCityName);
        });
        $("#scope2").click(function(e){
          $("#cityCode").val() && $("#search").click() || RulesManager.hideAll();
        });
      
		allowM = new RulesManager("#driver-allowance", "editAllowance.do",
				"removeAllowance.do", "loadAllowance.do", "#add_allowance");		
		RulesManager.reload(true, RulesManager.allCityCode);		
		
		var hiddenmsg = parent.document.getElementById("hiddenmsg");
		hiddenmsg.style.display = "none";
	});
</script>
  </head>
  
  <body>
   <div>城市参数设定</div>
   <hr/>
   <div style="height:40px;font-size:12px;">
      <input id="scope1" type="radio" name="scope" value="global" checked="checked"/>全局设定  
      <input id="scope2" type="radio" name="scope" value="city"/>城市单独设定
      <input id="cityCode" type="text" name="cityCode"/>
      <a id="search" class="easyui-linkbutton" iconcls="icon-search">查询</a>
   </div>
   <div style="height:40px;">
      <h3 style="display:inline;">当前城市：<span id="cityName"></span></h3><span style="color:red;margin-left:20px;">注释：如果是全局设定就显示“当前城市：全局”</span>
   </div>
    <hr/>
     <div id="city_rules">
     	<b><h3>补贴规则</h3></b>
		<div style="line-height:40px; text-align:right; margin-bottom:5px;">
			<a href="javascript:void(0)" id="add_allowance" style="width: 70px;"
				class="easyui-linkbutton" iconCls="icon-add">添加</a>
			<!--  <a href="" >查看补贴详情</a>  -->
		</div>
		<div id="driver-allowance">
			<div id="dummy" class="fee_rule rule_box">
				<form>
				    <div style="font-size:12px;">
				     <input type="radio" name="type" value="0" checked="checked"/>加价后补贴
				     <input type="radio" name="type" value="1"/>直接补贴
				    </div>
					<div>
						<input name="id" style="display:none;" /> <span
							style="padding-right:20px;color:green;"> 补贴时段 <span
							style="padding-right:5px;color:red;"><input
							name="beginTime" class="timespinner" value="00:00:00" /></span>至<input
							name="endTime" class="timespinner" value="00:00:00" /></span> <span
							style="float:right;padding-top:5px;"> <input type="button"
							class="edit sbutton" value="保存" style="background-color:#33CC00;">
						</span>
					</div>
					<div>
						<span style="padding-right:20px;">补贴对象 <select
							name="driverLevel" class="inputbox">
								<option value=0>普通司机</option>
								<option value=1>核心司机</option>
								<option value=2>全部司机</option>
						</select>
						</span> 
						<span style="padding-right:20px;color:orange;">目标补贴额<input
							name="allowance" class="easyui-validatebox inputbox" size="7"
							data-options="required:true,validType:['v_double','min_max[0,999999]']" />元/公里
						</span>
						<span style="padding-right:20px;color:orange;">补贴公里数 ><input
							name="minMileage" class="easyui-validatebox inputbox" size="7"
							data-options="required:true,validType:['v_double','min_max[0,999999]']" />公里
						</span>
						<span style="float:right; padding-top:5px;"> <input
							type="button" class="remove sbutton" value="删除"
							style="background-color:red;">
						</span>
					</div>
						<span style="padding-right:20px;color:red;">平台补贴上限<input
							name="maxAllowance" class="easyui-validatebox inputbox" size="7"
							data-options="required:true,validType:['v_double','min_max[0,999999]']" />元
						</span>
					<div>
					</div>
				</form>
			</div>
		</div>
	 </div>	
  </body>
</html>
