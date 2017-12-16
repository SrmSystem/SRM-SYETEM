var StatusRender = {
	allStatus : null,	
	render : function(value,code,isIcon){
		if((value == null || value=='') && value!=0)
			return '';
		/*
		 * 页面渲染调用，formatter:function(v,r,i){return iconFmt(值,类型,是否图标)}
		 * 如果allStatus在初始化未请求到数据，在本次渲染时会再次请求
		 */
		if(StatusRender.allStatus==undefined||StatusRender.allStatus[code]==undefined|| StatusRender.allStatus[code][value]==undefined){
			$.ajax({
				url:ctx+'/manager/database/statusDict/getAllStatusDict',
				type:'POST',
				dataType : "json",
				async : false,
				success:function(data){
					StatusRender.allStatus = data;
				}
			});
		}
		
		if(StatusRender.allStatus==undefined||StatusRender.allStatus[code]==undefined|| StatusRender.allStatus[code][value]==undefined){
			return value;
		}else{
			var displayText = $.i18n.prop(StatusRender.allStatus[code][value].statusCode);
			if(StatusRender.allStatus[code][value].statusCode == null || displayText == '[null]' || displayText == null){
				displayText = StatusRender.allStatus[code][value].statusText;
			}
			if(isIcon){
				return '<img title="' + displayText + '" alt="' + displayText + '('+value+')" src="'+ctx+'/static/style/icons/IconsExtension/'+StatusRender.allStatus[code][value].statusIcon+'" />';
			}else{
				return displayText;
			}
			
		}
		
	}
}

/*
 * 请求后台将所有状态数据存放在allStatus里
 */
$(function(){
	initStatus();
});
function initStatus(){
	$.ajax({
		url:ctx+'/manager/database/statusDict/getAllStatusDict',
		type:'POST',
		dataType : "json",
		success:function(data){
			StatusRender.allStatus = data;
		}
	});
}