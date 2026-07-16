var ajax = false;try {ajax = new ActiveXObject("Msxml2.XMLHTTP");}catch (e){
try {ajax = new ActiveXObject("Microsoft.XMLHTTP");} catch (e2) {ajax = false;}}
if (!ajax && typeof XMLHttpRequest != 'undefined') {ajax = new XMLHttpRequest();}

function $(str){return document.getElementById(str);}

function $$(str){return document.getElementsByName(str);}

var ff = (navigator.userAgent.indexOf("Firefox")!=-1);//Firefox

var regexEnum = 
{
	intege:"^-?[1-9]\\d*$",					//整数
	intege1:"^[1-9]\\d*$",					//正整数
	intege2:"^-[1-9]\\d*$",					//负整数
	intege3:"^[0-9]\\d*$",					//正整数包括0
	num:"^([+-]?)\\d*\\.?\\d+$",			//数字
	num1:"^[1-9]\\d*|0$",					//正数（正整数 + 0）
	num2:"^-[1-9]\\d*|0$",					//负数（负整数 + 0）
	decmal:"^([+-]?)\\d*\\.\\d+$",			//浮点数
	decmal1:"^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*$",//正浮点数
	decmal2:"^-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*)$",//负浮点数
	decmal3:"^-?([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0)$",//浮点数
	decmal4:"^[1-9]\\d*.\\d*|0.\\d*[1-9]\\d*|0?.0+|0$",//非负浮点数（正浮点数 + 0）
	decmal5:"^(-([1-9]\\d*.\\d*|0.\\d*[1-9]\\d*))|0?.0+|0$",//非正浮点数（负浮点数 + 0）

	email:"^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$", //邮件
	color:"^[a-fA-F0-9]{6}$",				//颜色
	url:"^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$",	//url
	chinese:"^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$",					//仅中文
	ascii:"^[\\x00-\\xFF]+$",				//仅ACSII字符
	zipcode:"^\\d{6}$",						//邮编
	mobile:"^(1|1)[0-9]{10}$",				//手机
	ip4:"^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$",	//ip地址
	notempty:"^\\S+$",						//非空
	picture:"(.*)\\.(jpg|bmp|gif|ico|pcx|jpeg|tif|png|raw|tga)$",	//图片
	rar:"(.*)\\.(rar|zip|7zip|tgz)$",								//压缩文件
	date:"^\\d{4}(\\-|\\/|\.)\\d{1,2}\\1\\d{1,2}$",					//日期
	qq:"^[1-9]*[1-9][0-9]*$",				//QQ号码
	tel:"^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$",	//电话号码的函数(包括验证国内区号,国际区号,分机号)
	username:"^\\w+$",						//用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
	letter:"^[A-Za-z]+$",					//字母
	letter_u:"^[A-Z]+$",					//大写字母
	letter_l:"^[a-z]+$",					//小写字母
	personCard:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
	idcard:"^[1-9]([0-9]{14}|[0-9]{17})$",	//身份证
	username:"^[a-zA-Z0-9_]{1,}$",         //只能输入由数字、26个英文字母或者下划线组成的字符串
	wjcphone:"^(13|14|15|16|17|18|19)[0-9]{9}$",               //手机号码验证
	strRegex:"^((https|http|ftp|rtsp|mms)?://)" 
		 + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
         + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184  
         + "|" // 允许IP和DOMAIN（域名） 
         + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.  
         + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名  
       + "[a-z]{2,6})" // first level domain- .com or .museum  
        + "(:[0-9]{1,4})?" // 端口- :80  
        + "((/?)|" // a slash isn't required if there is no file name  
       + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$"
}

//唯一值验证
function isOnly(url,method){
	var rand=Math.random();
	url=url+"&r="+rand;
	ajax.open("GET",encodeURI(url),true);
	ajax.send(null);
	ajax.onreadystatechange=function(){
	if (ajax.readyState==4){
		if (ajax.status==200){
			eval(method+"('"+ ajax.responseText +"')");
		}else{
			eval(method+"('error')");
			}
		}
	}
}
//中国身份证校验
var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"} 

function isCardID(sId){
	var iSum=0 ;
	var info="" ;
	if(!/^\d{17}(\d|x)$/i.test(sId)) return "你输入的身份证长度或格式错误"; 
	sId=sId.replace(/x$/i,"a"); 
	if(aCity[parseInt(sId.substr(0,2))]==null) return "你的身份证地区非法"; 
	sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2)); 
	var d=new Date(sBirthday.replace(/-/g,"/")) ;
	if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return "身份证上的出生日期非法";
	for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) ;
	if(iSum%11!=1) return "你输入的身份证号非法"; 
	return true;//aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女") 
}
//短时间，形如 (13:04:06)
function isTime(str)
{
	var a = str.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
	if (a == null) {return false}
	if (a[1]>24 || a[3]>60 || a[4]>60)
	{
		return false;
	}
	return true;
}

//短日期，形如 (2003-12-05)
function isDate(str){
	var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
	if(r==null)return false; 
	var d= new Date(r[1], r[3]-1, r[4]); 
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);}

//长时间，形如 (2003-12-05 13:04:06)
function isDateTime(str){
	var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; 
	var r = str.match(reg); 
	if(r==null) return false; 
	var d= new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]); 
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]&&d.getSeconds()==r[7]);}
	
//透明DIV显示隐藏
function formsDiv(is){
	if(is==true){
		if($("postAP")!=null) $("postAP").style.display="";
		if($("postAPs")!=null) $("postAPs").style.display="";
		if($("button")!=null) $("button").disabled=true;
	}else{
		if($("postAP")!=null) $("postAP").style.display="none";
		if($("postAPs")!=null) $("postAPs").style.display="none";
		if($("button")!=null) $("button").disabled=false;
	}}

function getAbsolutePos(el){
		//var el=$(id); 
        var r = {x:el.offsetLeft,y:el.offsetTop};   
        if(el.offsetParent){
            var tmp = getAbsolutePos(el.offsetParent);
            r.x += tmp.x;
            r.y += tmp.y;
        }
        return r;   
    }
	
function errormessageClose(id){
	$(id + "_errormessagebox").style.display="none";
	}
	
//出错样式
function falseStyle(id,msg){
	//if($(id+"TR")!=null) $(id+"TR").className="falseTR";
	//if($(id+"TD")!=null) $(id+"TD").className="falseTD";
	if($(id+"TD")!=null) $(id+"TD").innerHTML="<font color=red>&nbsp; "+msg.replace("　  ","")+"</font><span style='font-size:16px;font-weight:bold;color:#ff0000;'>!</span>";
	}
	
//正确样式
function trueStyle(id,msg){
	//if($(id+"TR")!=null) $(id+"TR").className="trueTR";
	//if($(id+"TD")!=null) $(id+"TD").className="trueTD";
	if($(id+"TD")!=null) $(id+"TD").innerHTML="<span style='color:green;font-size:14px;'>&nbsp;√&nbsp;</span><font color=green>正确</font>";
	}
	
//是否必填
function cmust(id,must){
	if($(id).value.length==0){
		if(must==0){
			trueStyle(id,"　  此项可不填");
			return true;
		}
		if(must==1){
			falseStyle(id,"　  不能为空");
			return false;
		}
	}else{
		trueStyle(id,"");
		return 0;
	}}

	/* 验证字符串长度
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小长度
	* maxs 最大长度
	*/
function chkText(id,must,mins,maxs){
	 
	if(cmust(id,must)!=0){
		return cmust(id,must);
	}
	var idlen=jmz.GetLength($(id).value);
	if(idlen<mins){
		falseStyle(id,"　  不能小于"+mins+"个字符");
		return false;
		}
	if(idlen>maxs){
//		if(chinese.test($(id).value)){
//			falseStyle(id,"　  不能大于"+maxs/3+"个汉字");
//		}else{
			falseStyle(id,"　  不能大于"+maxs+"个字符");	
//		}
		return false;
		}
	trueStyle(id,"");
	return true;
	}



	/*
	* 验证数字
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小长度
	* maxs 最大长度
	*/
function chkNum(id,must,mins,maxs){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.num).test($(id).value)){
		falseStyle(id,"　  只能是数字");
		return false;
		}
	if($(id).value.length<mins){
		falseStyle(id,"　  数值不能小于 "+mins);
		return false;
		}
	if($(id).value.length>maxs){
		falseStyle(id,"　  数值不能大于 "+maxs);
		return false;
		}
		trueStyle(id,"");
		return true;
	}

//正整数或正浮点数
function chkPrice(id,must,mins,maxs){
if(cmust(id,must)!=0){
return cmust(id,must);
}
if(!(RegExp(regexEnum.intege1).test($(id).value) || RegExp(regexEnum.decmal1).test($(id).value))){
		falseStyle(id,"　  只能是正整数或浮点数");
		return false;
		}
	if($(id).value<mins){
		falseStyle(id,"　  数值不能小于 "+mins);
		return false;
		}
	if($(id).value>maxs){
		falseStyle(id,"　  数值不能大于 "+maxs);
		return false;
		}
		trueStyle(id,"");
		return true;
	}
		/* 验证字符串只能由数字，字母和下划线
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小长度
	* maxs 最大长度
	*/
function chkUserName(id,must,mins,maxs){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.username).test($(id).value)){
		falseStyle(id,"　  只能是数字,字母获下划线");
		return false;
		}
	if($(id).value.length<mins){
		falseStyle(id,"　  数值不能小于 "+mins);
		return false;
		}
	if($(id).value.length>maxs){
		falseStyle(id,"　  数值不能大于 "+maxs);
		return false;
		}
		trueStyle(id,"");
		return true;
	}

	/*
	* 验证整数
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小数
	* maxs 最大数
	*/
function chkIntege(id,must,mins,maxs){
	if(cmust(id,must)!=0){
		return cmust(id,must);
	}
	if(!RegExp(regexEnum.intege).test($(id).value)){
		falseStyle(id,"　  只能是整数");
		return false;
		}
	if($(id).value<mins){
		falseStyle(id,"　  数值不能小于 "+mins);
		return false;
		}
	if($(id).value>maxs){
		falseStyle(id,"　  数值不能大于 "+maxs);
		return false;
		}
		trueStyle(id,"");
		return true;
	}
	
	/*
	* 验证正整数
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小数
	* maxs 最大数
	*/
function chkIntege1(id,must,mins,maxs){
	if(cmust(id,must)!=0){
		return cmust(id,must);
	}
	if(!RegExp(regexEnum.intege3).test($(id).value)){
		falseStyle(id,"　  只能是非负整数");
		return false;
		}
	if($(id).value<mins){
		falseStyle(id,"　  数值不能小于 "+mins);
		return false;
		}
	if($(id).value>maxs){
		falseStyle(id,"　  数值不能大于 "+maxs);
		return false;
		}
		trueStyle(id,"");
		return true;
	}

	/*
	* 验证浮点数
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小数
	* maxs 最大数
	*/
function chkFloat(id,must,mins,maxs){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.decmal4).test($(id).value)){
		falseStyle(id,"　  只能是浮点数");
		return false;
		}
	if($(id).value<mins){
		falseStyle(id,"　  数值不能小于 "+mins);
		return false;
		}
	if($(id).value>maxs){
		falseStyle(id,"　  数值不能大于 "+maxs);
		return false;
		}
		trueStyle(id,"");
		return true;
	}
	
	/* 验证手机
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkMobile(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.mobile).test($(id).value)){
		falseStyle(id,"　  手机格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}

	/* WJC验证手机
	* must 是否必填, 0 不必填 , 1 必填
	*/
function wjcchkMobile(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.wjcphone).test($(id).value)){
		falseStyle(id,"　  手机格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}


	/* 验证email
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkEmail(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.email).test($(id).value)){
		falseStyle(id,"　  邮件格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}
	
	/*验证网
	 * add by loki
	 * @return {TypeName} 
	 */
	function IsURL(id){ 
        if(!RegExp(regexEnum.strRegex).test($(id).value)){
		falseStyle(id,"　  网址格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
    } 

	/* 验证邮编
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkZipcode(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.zipcode).test($(id).value)){
		falseStyle(id,"　  邮编格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}
	
	/* 验证中文
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkChinese(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.chinese).test($(id).value)){
		falseStyle(id,"　  只能为中文字符");
		return false;
		}
		trueStyle(id,"");
		return true;
	}
/**
 * 验证大写字母
 */
function chkDaXie(id,must){
	if(cmust(id,must)!=0){
	return cmust(id,must);
	}
		if(!RegExp(regexEnum.letter_u).test($(id).value)){
			falseStyle(id,"只能为大写字母");
			return false;
			}
			trueStyle(id,"");
			return true;
		}

	/* 验证电话
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkTel(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.tel).test($(id).value)){
		falseStyle(id,"　  电话格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}
	
	/* 验证QQ号码
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkQQ(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.qq).test($(id).value)){
		falseStyle(id,"　  QQ号格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}
	
	/* 验证MSN
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkMSN(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.email).test($(id).value)){
		falseStyle(id,"　  MSN格式不正确");
		return false;
		}
		trueStyle(id,"");
		return true;
	}

	/* 验证日期
	* must 是否必填, 0 不必填 , 1 必填
	*/
function chkDate(id,must){
if(cmust(id,must)!=0){
return cmust(id,must);
}
	if(!RegExp(regexEnum.date).test($(id).value)){
		falseStyle(id,"　  日期格式不正确");
		return false;
	}
		trueStyle(id,"");
		return true;
	}

	/* 
	* 验证复选框
	*/
function chkCheckbox(name,s){
	if(cmust(name,must)!=0){
		return cmust(name,must);
	}
	var checkBoxs = $$(name).length;
	var c=0;
	for(var i=0;i<checkBoxs;i++){
		if($$(name).item(i).checked){
			
			c++;
			
			}
		}
		
	if(c>=s){
			trueStyle(name,"");
			return true;
		}else{
			falseStyle(name,"　  至少选择"+ s +"个");
			return false;
		}
	}
	/* 
	* 验证单选按钮
	*/
function chkRadio(name,must){
	var rad=document.getElementsByName(name);
	var Radios =document.getElementsByName(name).length;
	var c=0;
	for(var i=0;i<Radios;i++){
		if(rad[i].checked){
			c++;
			}
		}
	if(c==0){
		if(must==0){
			trueStyle(name,"　  此项可不选");
			return true;
		}
		if(must==1){
			falseStyle(name,"　  请选择");
			return false;
			}
		}
	else{
			trueStyle(name,"");
			return true;
		}
	}
	
	/* 
	* 验证下拉框 ID 和 默认值
	*/
function chkSelect(id,muste,defaults){

	if($(id).value!=defaults){
			trueStyle(id,"");
			return true;
		}else if(muste==0){
			trueStyle(id,"　  此项可不选");
			return true;
		}else{
			falseStyle(id,"　  请选择");
			return false;
		}
	}

 function check(name){        
	 alert(1);
      var ids = document.getElementsByName("name");                  
      var flag = false ;                
      for(var i=0;i<ids.length;i++){  
        if(ids[i].checked){ 
        	   trueStyle(ids,"");
                flag = true ;  
                 break ;  
                  }  
             }  
              if(!flag){  
            	  falseStyle(name,"请最少选择一项！");  
                   return false ;  
             }  
        }  
	/* 
	* 验证下拉框 ID 和 默认值
	*/
function chkCardID(id){
	if(isCardID($(id).value)==true){
			trueStyle(id,"");
			return true;
		}else{
			falseStyle(id,"　  "+isCardID($(id).value));
			return false;
		}
	}
/**
**/

var jmz = {}
jmz.GetLength = function(str) {
    ///<summary>获得字符串实际长度，中文2，英文1</summary>
    ///<param name="str">要获得长度的字符串</param>
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) realLength += 1;
        else realLength += 3;
    }
    return realLength;
}    

function checkSelect(){
	var listValue = document.all.form1.barid.value;
	
	if (listValue == "")
	{
    falseStyle("请选择");
	return false;
	}
	}

		/* 验证字符串只能由数字，字母和下划线
	* must 是否必填, 0 不必填 , 1 必填
	* mins 最小长度
	* maxs 最大长度
	*/
function chkEmptity(id){

	if(!RegExp(regexEnum.notempty).test($(id).value)){
		falseStyle(id,"　  不能为空");
		return false;
		}
	 return true;
}