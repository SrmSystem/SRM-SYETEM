var materialType = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	add : function(){
		$('#dialog-materialType-saveUpadte').window('open');
		$("#id").val(0);
		$("#abolished").val(0);
		$('#form-materialType-saveUpadte').form('reset');
	},
	update : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条数据','info');
			return false;
		}
		$("#id").val(selections[0].id);
		$("#abolished").val(selections[0].abolished);
		$('#dialog-materialType-saveUpadte').window('open');
		$('#form-materialType-saveUpadte').form('load',selections[0]);
	},
	submit : function(){
		var url=ctx;
		url=url+'/manager/vendor/vendorPerforMaterialType/addUpdateMaterialType';
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});	
		$('#form-materialType-saveUpadte').form('submit',{
			url:url, 
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
					data= JSON.parse(data);
					if(data.success) {
						$.messager.alert('提示',data.msg,'info');
						$('#dialog-materialType-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',data.msg,'info');
					}
				}catch(e){
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	release : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
        $.messager.confirm('提示','确定启用吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
						$.messager.alert('提示','存在已启用的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforMaterialType/releaseMaterialType',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if (data == '1') {
							$.messager.alert('提示', "启用成功", 'info');
						} else {
							$.messager.alert('提示', data, 'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
        })
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定作废吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert('提示','存在已作废的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforMaterialType/delsMaterialType',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if (data == '1') {
							$.messager.alert('提示', "作废成功", 'info');
						} else {
							$.messager.alert('提示', data, 'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	deletes : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定删除吗？并将该物料的关系一并删除！',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforMaterialType/deletesMaterialType',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1') {
							$.messager.alert('提示',"删除成功",'info');
						} else {
							$.messager.alert('提示',data,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	imp : function(){
		$('#win-import').dialog();
		$('#win-import').window({
			iconCls : 'icon-disk_upload',
			title : '数据导入'
		});
		$('#form-import').form('clear');
		$('#win-import').window('open');
	},
	saveImp:function() {
		$.messager.progress();
		$('#form-import').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/vendor/vendorPerforMaterialType/filesUpload', 
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
						$.messager.alert('提示',"导入数据成功",'info');
						$('#win-import').window('close');
						$('#datagrid').datagrid('reload');
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