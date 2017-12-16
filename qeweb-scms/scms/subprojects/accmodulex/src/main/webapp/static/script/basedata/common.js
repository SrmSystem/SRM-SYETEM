function isNULL(data){
	if(data){
		return data;
	}
	return "";
}

/**
 * 获取当前时间往后天数时间
 * @param day
 * @returns {Date}
 */
function getNextDay(day){
var t=new Date();
var iToDay=t.getDate();
var iToMon=t.getMonth();
var iToYear=t.getFullYear();
var newDay = new Date(iToYear,iToMon,(iToDay+day));
return newDay;
}

//两个时间相差天数 
function datedifference(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式  
    var dateSpan,
        tempDate,
        iDays;
    sDate1 = Date.parse(sDate1);
    sDate2 = Date.parse(sDate2);
    dateSpan = sDate2 - sDate1;
    dateSpan = dateSpan;
    iDays = Math.floor(dateSpan / (24 * 3600 * 1000));
    return iDays
};

/**
 * 时间扩展
 * @param 		fmt	转换格式
 * @returns		转换后的时间
 */
Date.prototype.format=function(fmt) {    
    var o = {        
    "M+" : this.getMonth()+1, //月份        
    "d+" : this.getDate(), //日        
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时        
    "H+" : this.getHours(), //小时        
    "m+" : this.getMinutes(), //分        
    "s+" : this.getSeconds(), //秒        
    "q+" : Math.floor((this.getMonth()+3)/3), //季度        
    "S" : this.getMilliseconds() //毫秒        
    };        
    var week = {        
    "0" : "\u65e5",        
    "1" : "\u4e00",        
    "2" : "\u4e8c",        
    "3" : "\u4e09",        
    "4" : "\u56db",        
    "5" : "\u4e94",        
    "6" : "\u516d"       
    };        
    if(/(y+)/.test(fmt)){        
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));        
    }        
    if(/(E+)/.test(fmt)){        
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);        
    }        
    for(var k in o){        
        if(new RegExp("("+ k +")").test(fmt)){        
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));        
        }        
    }        
    return fmt;        
}

//千分位，3位小数
function getNumFmt(v,r,i) {
	if(v=="" || v==null || v == "0.000" || v==0.000){
		return 0;
	}
	var num=v;
    num = num.toString().replace(/\$|\,/g,'');  
    // 获取符号(正/负数)  
    sign = (num == (num = Math.abs(num)));  
    num = Math.floor(num*Math.pow(10,3)+0.50000000001);  // 把指定的小数位先转换成整数.多余的小数位四舍五入  
    cents = num%Math.pow(10,3);              // 求出小数位数值  
    num = Math.floor(num/Math.pow(10,3)).toString();   // 求出整数位数值  
    cents = cents.toString();               // 把小数位转换成字符串,以便求小数位长度  
    // 补足小数位到指定的位数  
    while(cents.length<3)  
      cents = "0" + cents;  
      // 对整数部分进行千分位格式化.  
      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

      return (((sign)?'':'-') + num + '.' + cents);  
   
}

//千分位，整数
function getNumIntegerFmt(v,r,i) {
	if(v=="" || v==null || v == "0.000" || v==0.000){
		return 0;
	}
	var num=v;
    num = num.toString().replace(/\$|\,/g,'');  
    // 获取符号(正/负数)  
    sign = (num == (num = Math.abs(num)));  
    num = Math.floor(num*Math.pow(10,3)+0.50000000001);  // 把指定的小数位先转换成整数.多余的小数位四舍五入  
    cents = num%Math.pow(10,3);              // 求出小数位数值  
    num = Math.floor(num/Math.pow(10,3)).toString();   // 求出整数位数值  
    cents = cents.toString();               // 把小数位转换成字符串,以便求小数位长度  
   
      // 对整数部分进行千分位格式化.  
      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

      return (((sign)?'':'-') + num);  
   
}
//判断浏览器
function myBrowser(){
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    var isIE ="ActiveXObject" in window;
	var isisChrome = userAgent.indexOf("Chrome") > -1
	if(isIE){
		return "IE";
	}else if(isisChrome){
		return "CHROEM";
	}else{
		return "false"
	}
}

//ie剪贴板的处理

function getIEClipboard() {
    if (window.clipboardData) {
        return (window.clipboardData.getData('Text'));
    }
    else if (window.netscape) {
        netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
        var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
        if (!clip) return;
        var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
        if (!trans) return;
        trans.addDataFlavor('text/unicode');
        clip.getData(trans, clip.kGlobalClipboard);
        var str = new Object();
        var len = new Object();
        try {
            trans.getTransferData('text/unicode', str, len);
        }
        catch (error) {
            return null;
        }
        if (str) {
            if (Components.interfaces.nsISupportsWString) str = str.value.QueryInterface(Components.interfaces.nsISupportsWString);
            else if (Components.interfaces.nsISupportsString) str = str.value.QueryInterface(Components.interfaces.nsISupportsString);
            else str = null;
        }
        if (str) {
            return (str.data.substring(0, len.value / 2));
        }
    }
    return null;
}



function getChromeClipboard() {
    netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');  
    var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);  
    if (!clip) return;  
    var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);  
    if (!trans) return;  
    trans.addDataFlavor('text/unicode');  
    clip.getData(trans, clip.kGlobalClipboard);  
    var str = new Object();  
    var len = new Object();  
    try {  
        trans.getTransferData('text/unicode', str, len);  
    } catch(error) {  
        return null;  
    }  
    if (str) {  
        if (Components.interfaces.nsISupportsWString) strstr = str.value.QueryInterface(Components.interfaces.nsISupportsWString);  
        else if (Components.interfaces.nsISupportsString) strstr = str.value.QueryInterface(Components.interfaces.nsISupportsString);  
        else str = null;  
    }  
    if (str) {  
        alert(str.data.substring(0, len.value / 2));  
        return (str.data.substring(0, len.value / 2));  
    }  
    return null; 
}


//物料的获取和处理
function handlMaterial(){
	var brower = myBrowser();
	var str = "";
	var material =  [];
	if(brower == "IE"){
		str = getIEClipboard();
	}else if(brower == "CHROEM"){
//		$.messager.alert('提示',"请选用IE 使用！",'error');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.UserIEChoies'),'error');
	}else{
//		$.messager.alert('提示',"请选用IE 使用！",'error');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.UserIEChoies'),'error');
		return false;
	}
	//获取异常直接return
	try{
		var len = str.split("\r\n");//获取行数
	    if(len == null || len == "" || len == undefined){
	    	return false;
	    }
	}catch (e) {
		return false;
	} 
   
    //获取物料的总数
  	$.ajax({
  		url:ctx + '/manager/basedata/material/getAllMaterial',
        type: 'POST',
    	dataType:"json",
    	async: false,
		contentType : 'application/json',
        success: function (data) {  
        	if(data !=  null){ 
        		for(var i= 0 ; i<len.length ; i++ ){
        			for( var j= 0 ; j<data.length ; j++ ){
            			if(data[j].code == len[i] ){
            				material.push(len[i] );
            				break;
            			}
            		}
        		}

        	}else{
        		return false;
        	}
        }
      });
  	return material;
}

//供应商的获取和处理
function handlVendor(){
	var brower = myBrowser();
	var str = "";
	var vendor =  [];
	if(brower == "IE"){
		str = getIEClipboard();
	}else if(brower == "CHROEM"){
//		$.messager.alert('提示',"请选用IE 使用！",'error');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.UserIEChoies'),'error');
	}else{
//		$.messager.alert('提示',"请选用IE 使用！",'error');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.UserIEChoies'),'error');
		return false;
	}
	//获取异常直接return
	try{
		var len = str.split("\r\n");//获取行数
	    if(len == null || len == "" || len == undefined){
	    	return false;
	    }
	}catch (e) {
		return false;
	} 
   
    //获取物料的总数
  	$.ajax({
  		url:ctx + '/manager/admin/org/getOrgListByType/1',
        type: 'POST',
    	dataType:"json",
    	async: false,
		contentType : 'application/json',
        success: function (data) { 
        	debugger;
        	if(data !=  null){ 
        		for(var i= 0 ; i<len.length ; i++ ){
        			for( var j= 0 ; j<data.length ; j++ ){
            			if(data[j].code == len[i] ){
            				vendor.push(len[i] );
            				break;
            			}
            		}
        		}

        	}else{
        		return false;
        	}
        }
      });
  	return vendor;
}

//input的处理
function handlInputMaterial(val){
	 var str = iGetInnerText(val);
	
	var material =  [];
	//获取异常直接return
	try{
		var len = str.split(" ");//获取行数
	    if(len == null || len == "" || len == undefined){
	    	return false;
	    }
	}catch (e) {
		return false;
	} 
   
    //获取物料的总数
  	$.ajax({
  		url:ctx + '/manager/basedata/material/getAllMaterial',
        type: 'POST',
    	dataType:"json",
    	async: false,
		contentType : 'application/json',
        success: function (data) {  
        	if(data !=  null){ 
        		for(var i= 0 ; i<len.length ; i++ ){
        			for( var j= 0 ; j<data.length ; j++ ){
            			if(data[j].code == len[i] ){
            				material.push(len[i] );
            				break;
            			}
            		}
        		}

        	}else{
        		return false;
        	}
        }
      });
  	return material;
}





//供应商的获取和处理
function handlInputVendor(val){
	 var str = iGetInnerText(val);
	var vendor =  [];
	//获取异常直接return
	try{
		var len = str.split(" ");//获取行数
	    if(len == null || len == "" || len == undefined){
	    	return false;
	    }
	}catch (e) {
		return false;
	} 
   
    //获取物料的总数
  	$.ajax({
  		url:ctx + '/manager/admin/org/getOrgListByType/1',
        type: 'POST',
    	dataType:"json",
    	async: false,
		contentType : 'application/json',
        success: function (data) { 
        	debugger;
        	if(data !=  null){ 
        		for(var i= 0 ; i<len.length ; i++ ){
        			for( var j= 0 ; j<data.length ; j++ ){
            			if(data[j].code == len[i] ){
            				vendor.push(len[i] );
            				break;
            			}
            		}
        		}

        	}else{
        		return false;
        	}
        }
      });
  	return vendor;
}


function iGetInnerText(testStr) {
    var resultStr = testStr.replace(/\ +/g, ""); //去掉空格
    resultStr = testStr.replace(/[ ]/g, "");    //去掉空格
    resultStr = testStr.replace(/[\r\n]/g, ""); //去掉回车换行
    return resultStr;
}




