/** 操作列工具类 */
var OptFmtUtil = {
   /**
    * datagrid的操作单元格式化
    * @fn 调用的函数
    * @text 显示的文本
    */
   datagrid : function(fn,text){
	   return '<a class="row-btn" href="javascript:;" onclick="'+fn+'">'+text+'</a>';
   }
};

/** 提交工具 */
var SubmitUtil = {
	/**
	 * @fId 表单的ID,带#号
	 * @url 跳转的路径，不需要工程前缀
	 * @validateFn 提交时的自定义验证函数
	 * @successFn 成功返回时的自定义函数
	 * @showMsg 是否显示默认消息
	 */
	form : function(fId,url,validateFn,successFn,showMsg){
		var f = $(fId);
		var url = ctx+url;
		$.messager.progress();
		f.form('submit',{
			url : url,
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if(validateFn!=null && $.isFunction(validateFn)){
					isValid = validateFn();
				}
				
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交
			},
			success: function(result){
				$.messager.progress('close');	// 如果提交成功则隐藏进度条
				try{
					var data = $.parseJSON(result);
					if(data.success){
						if(showMsg)
						$.messager.success(data.msg);
						if(successFn!=null && $.isFunction(successFn)){
							successFn(data);
						}
					}else{
						if(showMsg)
					    $.messager.fail(data.msg);
						if(successFn!=null && $.isFunction(successFn)){
							successFn(data);
						}
					}
				}catch(e) {
					$.messager.alert('提示',result,'error');
				}
			}
		});
	},
	ajax : function(url,data,cache,successFn,showMsg){
		$.messager.progress();
		$.ajax({
			type : 'POST',
			data : data,
			cache : cache,
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					if(showMsg)
					$.messager.success(data.msg);
					if(successFn!=null && $.isFunction(successFn)){
						successFn(data);
					}
				}else{
					if(showMsg)
				    $.messager.fail(data.msg);
				}
			},
			error : function(data){
				$.messager.progress('close');
				$.messager.alert('提示',data,'error');
			}
			
		});
	},
	ajaxJson : function(url,data,cache,successFn,showMsg){
		$.messager.progress();
		$.ajax({
			type : 'POST',
			data : $.toJSON(data),
			cache : cache,
			contentType : 'application/json',
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					if(showMsg)
						$.messager.success(data.msg);
					if(successFn!=null && $.isFunction(successFn)){
						successFn(data);
					}
				}else{
					if(showMsg)
						$.messager.fail(data.msg);
				}
			},
			error : function(data){
				$.messager.progress('close');
				$.messager.alert('提示',data,'error');
			}
			
		});
	}
}