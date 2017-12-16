var NewProduct = {
	searchInfo : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-newProduct-list').datagrid('load', searchParams);
	},
	operatItem : function(st){
		var selections = $('#datagrid-newProduct-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		for(i = 0; i < selections.length; i ++) {
			if(st == "publish" && selections[i].dataStatus == 1) {
				$.messager.alert('提示','包含已发布记录无法重复发布！','error');
				return false;
			} 
		}
		
		$.messager.progress();
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/qualityassurance/Newproduct/'+st+'Item',
			type:'POST',
			data:params,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.alert('提示',data.message,'info');
						$('#datagrid-newProduct-list').datagrid('reload');
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
			}
		});
	},

	importItem : function(){
		$('#win-newproduct-import').window({
			iconCls:'icon-add',
			title:'新产品开发质量'
		});  
		$('#win-newproduct-import').window('open');
	},
	
	calcul : function(){
		$.messager.confirm('提示','确定计算吗？',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/qualityassurance/Newproduct/calculate',
					type:'POST',
					dataType:"json",
					contentType : 'application/json',
					success:function(data){
						if(data.success){
							$.messager.alert('提示',data.message,'info');
							$('#datagrid').datagrid('reload');
						}
						else{
							$.messager.alert('提示',data.msg,'error');
						}
					}
				});
			}
		});
	},
	saveimport : function(){
		$.messager.progress();
		$('#form-newproduct-import').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/qualityassurance/Newproduct/filesUpload', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示','导入新产品开发质量成功','info');
					$('#win-newproduct-import').window('close');
					$('#datagrid-newProduct-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
				}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});	
	}
}
