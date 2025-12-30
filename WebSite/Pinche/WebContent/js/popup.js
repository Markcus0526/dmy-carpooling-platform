var popuptitle="";//保存弹窗的标题
var popupurl="";//保存弹窗的地址
var popupwidth=0;//保存弹窗的宽度
var popupheight=0;//保存弹窗的高度
var winWidth = 0;
var winHeight = 0;

function GetWinWH(){
	if (window.innerWidth){
		winWidth = window.innerWidth;
	}else if ((document.body) && (document.body.clientWidth)){
		winWidth = document.body.clientWidth;
	}
	if (window.innerHeight){
		winHeight = window.innerHeight;
	}else if ((document.body) && (document.body.clientHeight)){
		winHeight = document.body.clientHeight;
	}
	if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth){
		//alert(winHeight +"=="+ document.documentElement.clientHeight);
		if(winHeight>1000 || winHeight < document.documentElement.clientHeight){
			winHeight = document.documentElement.clientHeight;
		}
		winWidth = document.documentElement.clientWidth;
	}
}
var dialog=new Array();
var popupcount=0;
//弹窗
function popup(title,w,h,url){
	var imeshow=true;
	if(title=="前台登陆"){
		imeshow=false;
	}
	if(title!="精度登录"){
		top.popuptitle=title;
		top.popupurl=url;
		top.popupwidth=w;
		top.popupheight=h;
	}
	popupcount=popupcount+1;
	Dialog.open({URL:url,Title:title,Width:w,Height:h});
}
//修改弹窗大小
function resizepop(w,h){
	top.Dialog.setSize(w,h);
	return;
	var objpup=top.document.getElementById('popup'+popupcount);
	if(objpup){
		objpup.style.width=(w)+"px";
		objpup.style.height=(h)+"px";
		var objdiv=objpup.getElementsByTagName('div')[2];
		if(objdiv){
			objdiv.style.height=(h-34)+"px";
			dialog[popupcount].autoPos(w,h);
		}
	}
}
//修改弹窗标题
function poptitle(title){
	top.Dialog.setTitle(title);
	return;
	var obj=top.document.getElementById('popuptitle'+popupcount);
	obj.innerHTML=title;
}
//关闭弹窗
function removepup(e){
	popupcount=popupcount-1;
	top.Dialog.close();
	return;
	var obj=top.document.getElementById('popupclose'+e);
	if(document.all){ 
		obj.click();
	}else{ 
		var evt = document.createEvent("MouseEvents"); 
		evt.initEvent("click", true, true); 
		obj.dispatchEvent(evt); 
	}
	top.showOther=0;
}

function submitSuccess(msg,w,h){
	Dialog.close();
	Dialog.alert(msg,function(){},w,h);
}

function submitResult(msg,w,h){
	top.Dialog.close();
	showMessage(msg,w,h);
}

function showMessage(msg,w,h){
	Dialog.alert(msg,function(){},w,h);
}

function ReturnBrowser(){
    var cb = "Unknown";
    if(window.ActiveXObject){
        cb = "IE";
    }else if(navigator.userAgent.toLowerCase().indexOf("firefox") != -1){
        cb = "Firefox";
    }else if((typeof document.implementation != "undefined") && (typeof document.implementation.createDocument != "undefined") && (typeof HTMLDocument != "undefined")){
        cb = "Mozilla";
    }else if(navigator.userAgent.toLowerCase().indexOf("opera") != -1){
        cb = "Opera";
    }
    return cb;
}