$(function(){
	$("#guide-step .tipSwitch").live("click",function(){
		showSearchTip();
		setSearchTip();
		SetCookie("tStatus",1);
	});
})

function nextStep(next){
	$(".tipbox").css({"visibility":"hidden","display":"none"});
	$(".tipbar").hide();
	$("#step" + next).css({"visibility":"visible","display":"block"});
	$("#tipbar" + (next -1)).show();
	if(next == 2) {
		$("#searchTip").css("top","307px");
	}else if(next == 3) {
		$("#searchTip").css("top","770px");
	}else{
		$("#searchTip").css("top","630px");
	}
	if(next == 4){
		$(".tipSwitchAnimate").css("top","539px");
	}else if(next == 6){
		$(".tipSwitchAnimate").css("top","357px");
	}else {
		$(".tipSwitchAnimate").css("top","352px");	
	}
	$(".tipSwitchAnimate").css("left","410px");
}

//关闭提示框
function hideTip(){
	$("#searchTipBg").hide();
	$("#searchTip").hide();
	$(".tipbar").hide();
	$(".tipbox").css({"visibility":"hidden","display":"none"});
	$("#step1").css({"visibility":"visible","display":"block"});
	SetCookie("tipVisible","no");
	$(".tipSwitchAnimate").show().animate({
		"left":"815px",
		"top":"15px"
	},500,function(){
		$(".tipSwitchAnimate").css({"left":"410px","top":"15px"}).hide();	
	});
}

function setSearchTip(){
	var windowW = $(window).width(),
		windowH = $(window).height(),
		width = $("#searchTip").width(),
		ml = width/2;
	if($("#searchTip").length > 0 && $("#searchTipBg").length > 0){
		if($.browser.msie && $.browser.version == '6.0' && !$.support.style){
		  	var scrollT = $(window).scrollTop(),
			  	scrollL = $(window).scrollLeft();
			$("#searchTipBg").css({"width":windowW + scrollL,"height":windowH + scrollT});
		}else {
			$("#searchTipBg").css({"width":windowW,"height":windowH});
		}
		$("#searchTip").css({"margin-left":-ml});
	}
}
function noShow(){
	if(document.getElementById("notip").checked){
		SetCookie("neverShow","no",{expires:37230});
	}	
}

function showSearchTip(){
	var position = $.browser.msie && $.browser.version == '6.0' && !$.support.style ? "absolute" : "fixed";
	var searchTipBar = "<div class='tipbarwrap'><div class='tipbardiv'>";
	  	searchTipBar += "<div class='tipbar' id='tipbar1'><div class='tipbarInner'><div class='arrow'></div><div class='tipBarword'></div></div></div>";
		searchTipBar += "<div class='tipbar' id='tipbar2'><div class='tipbarInner'><div class='arrow'></div><div class='tipBarword'></div></div></div>";
		searchTipBar += "<div class='tipbar' id='tipbar3'><div class='tipbarInner'><div class='arrow'></div><div class='tipBarword'></div></div></div>";
		searchTipBar += "</div></div>";
	var searchTipInner = "<div class='tipbox' id='step1'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'></span><span class='tipboxNextbtn' onclick='nextStep(2)'></span><ol class='progress'><li class='on'></li><li></li><li></li><li></li></ol></div>";
		searchTipInner += "<div class='tipbox' id='step2'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'></span><a href='#stepflow03' class='tipboxNextbtn' onclick='nextStep(3)'></a><ol class='progress'><li></li><li class='on'></li><li></li><li></li></ol></div>";
		searchTipInner += "<div class='tipbox' id='step3'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'></span><a href='#stepflow04' class='tipboxNextbtn' onclick='nextStep(4)'></a><ol class='progress'><li></li><li></li><li class='on'></li><li></li></ol></div>";
		searchTipInner += "<div class='tipbox' id='step4'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'></span><span class='tipboxNextbtn' onclick='hideTip();noShow()'></span><div class='notip'><input type='checkbox' id='notip' /><label for='notip'>不再提示</label></div></div>";
	var switchBtn = "<div class='tipSwitchAnimate tipSwitch' style='display:none; left:410px; top:353px;'></div>";	
	if($("#searchTip").length == 0){
		$("#guide-step").before("<div id='searchTipBg' style='width:100%; height:100%; left:0px; top:0px; z-index:999; background-color:#000; opacity:0.55; filter:alpha(opacity=55);position:"+ position +"'></div>");
		$("#guide-step").before("<div id='searchTip' style='left:50%; top:270px; z-index:1005; background-color:transparent; position:absolute;'>"+ searchTipInner +"</div>");
		$("#guide-step").before(searchTipBar);
		$(switchBtn).appendTo($(".tipbardiv"));
		$("#step1").css({"visibility":"visible","display":"block"});
		if(GetCookie("tipVisible") == "no" || GetCookie("neverShow") == "no"){
			$("#step4 .notip").hide();
		}
	}
	if($("#searchTip").css("display") == "none"){
		$("#searchTip").css("top","270px").show();
		$("#searchTipBg").show();
		$(".tipbox").css({"visibility":"hidden","display":"none"});
		$("#step1").css({"visibility":"visible","display":"block"});
	}
	if($(".tipbarwrap").css("display") == "none"){
		$(".tipbarwrap").show();
	}
}

function GetCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return decodeURIComponent(arr[2]); return null;
}

function SetCookie(name,value,options){
    var expires = '', path = '', domain = '', secure = ''; 
    if(options)
    {
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var exp;
            if (typeof options.expires == 'number') {
                exp = new Date();
                exp.setTime(exp.getTime() + options.expires*24*60*60*1000);
            }
            else{
                exp = options.expires;
            }
            expires = ';expires=' + exp.toUTCString();
        }
        path = options.path ? '; path=' + options.path : ''; 
        domain = options.domain ? ';domain=' + options.domain : ''; 
        secure = options.secure ? ';secure' : ''; 
    }
    document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
}