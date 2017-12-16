/** 日期工具类 */
//限制日期只能选择系统时间以后的
 Date.prototype.format = function(fmt) { 
		    var o = { 
		       "M+" : this.getMonth()+1,                 //月份 
		       "d+" : this.getDate(),                    //日 
		       "h+" : this.getHours(),                   //小时 
		       "m+" : this.getMinutes(),                 //分 
		       "s+" : this.getSeconds(),                 //秒 
		       "q+" : Math.floor((this.getMonth()+3)/3), //季度 
		       "S"  : this.getMilliseconds()             //毫秒 
		   }; 
		   if(/(y+)/.test(fmt)) {
		           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
		   }
		    for(var k in o) {
		       if(new RegExp("("+ k +")").test(fmt)){
		            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		        }
		    }
		   return fmt; 
		}
		 
		 $.extend($.fn.validatebox.defaults.rules, {
			    isTimeRight: {
			        validator: function (value, param) {//param为默认值
			        var nowDate = new Date().format("yyyy-MM-dd");
			        var d1 = new Date(nowDate.replace(/\-/g, "\/"));  
			        var d2 = new Date(value.replace(/\-/g, "\/"));  
			         if(nowDate!=""&&value!=""&& d1 > d2){  
			         	return false;  
			          }else{
			        	  return true;
			          }
			        },
			        message: '只能选择当前系统时间之后的时间，请重新选择!'
			    }
			});
