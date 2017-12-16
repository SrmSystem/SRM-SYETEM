(function($){
	$.jqexer = {
		formToJson : function(formArray){
			var jsonObj = {};
			$.each(formArray,function(i,n){
				jsonObj[n.name] = n.value;
			});
			return jsonObj;
		},
		formIdToJson : function(formId){
			var searchParamArray = $(formId).serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			return searchParams;
		}
	};
	
}
)(jQuery);

/* easyui后台异常数据抛出的2种解决方式 */
//第一种，在渲染前拦截，因为rows没有，render时会js出错，原因在于render时会调用 "rows.length"
$.extend($.fn.datagrid.defaults.view,{
	//easyui有个问题，如果后台抛出异常，并没有指定rows,那么就不知道出了问题
	//在这里处理下
	onBeforeRender:function(d,r){
		if(r==null){
			$.messager.alert('提示','数据出现异常','error');
			return false;
		}
	}
});

//第二种,重写默认loader,但是有个问题，前台会一直显示加载到超时，是因为，在这个方法前，有前置方法和后置方法
$.extend($.fn.datagrid.defaults,{
		loader:function(_7c5,_7c6,_7c7){
			var opts=$(this).datagrid("options");
			if(!opts.url){
			return false;
			}
			$.ajax({type:opts.method,url:opts.url,data:_7c5,dataType:"json",success:function(data){
				if(data!=null && data.error){
					$.messager.alert('提示',data.msg,'error');
					return false;
				}
				_7c6(data);
			},error:function(){
			_7c7.apply(this,arguments);
			}});
			}
});
