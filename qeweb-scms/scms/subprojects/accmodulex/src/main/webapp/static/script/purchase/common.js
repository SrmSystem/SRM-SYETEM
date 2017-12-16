function isNULL(data){
	if(data){
		return data;
	}
	return "";
}

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
	if(v=="" || v==null){
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