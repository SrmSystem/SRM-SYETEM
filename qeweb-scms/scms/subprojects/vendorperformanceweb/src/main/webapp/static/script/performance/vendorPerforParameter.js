var vendorPerforParameter = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="vendorPerforParameter.update('+r.id+')">修改</button>';
	},
	add : function(){
		$('#dialog-vendorPerforParameter-saveUpadte').dialog();
		$("#id").val("0");
		$('#form-vendorPerforParameter-saveUpadte').form('reset');
		$('#dialog-vendorPerforParameter-saveUpadte').window('open');
	},
	update : function(id){
		$('#dialog-vendorPerforParameter-saveUpadte').dialog();
		$("#id").val(id);
		$('#dialog-vendorPerforParameter-saveUpadte').window('open');
		$('#form-vendorPerforParameter-saveUpadte').form('load',ctx+'/manager/vendor/vendorPerforParameter/updateVendorPerforParameterStart/'+id);
	},
	submit : function(){
		var url=ctx;
		if($("#id").val()!='0') {
			url=url+'/manager/vendor/vendorPerforParameter/updateVendorPerforParameter';
		} else {
			url=url+'/manager/vendor/vendorPerforParameter/addVendorPerforParameter';
		}
		$('#form-vendorPerforParameter-saveUpadte').form('submit',{
			url:url, 
			success:function(data){
				try{
					data = JSON.parse(data);
					if (data.success) {
						$.messager.alert('提示', data.msg, 'info');
						$('#dialog-vendorPerforParameter-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示', data.msg, 'error');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	showImportWin : function() {
		$('#form-param-import').form('clear');   
		$('#win-param-import').window('open');  
	},
	imports : function() {
		$.messager.progress();
		$('#form-param-import').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/vendor/vendorPerforParameter/imp', 
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
						$.messager.alert('提示', result.msg ,'info');
						$('#win-param-import').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示', result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		}); 
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定作废吗？<br/>',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["joinStatus"]=='0'){
						$.messager.alert('提示','存在已作废的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforParameter/delesVendorPerforParameter',
					type:'POST',
					data:params,
					contentType : 'application/json',
					method : 'post',
				    dataType : 'json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',data.msg,'info');
							$('#dialog-vendorPerforParameter-saveUpadte').window('close');
							$('#datagrid').datagrid('load');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	}
}