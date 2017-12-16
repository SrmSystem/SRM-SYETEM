var vendorPerforDataIn = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	imp : function(){
		$('#win-vendorPerforDataIn-import').dialog();
		$('#win-vendorPerforDataIn-import').window({
			iconCls : 'icon-disk_upload',
			title : '数据导入'
		});
		$('#form-vendorPerforDataIn-import').form('clear');
		$('#win-vendorPerforDataIn-import').window('open');
	},
	updateStart : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条！','info');
			return false;
		}
		$('#updateapp').dialog();
		$("#useid").val(selections[0]["id"]);
		$('#ff').form('load',selections[0]);
		$('#updateapp').window('open');
	},
	update :function(){
		$.messager.progress();
		$('#ff').form('submit',{
			contentType : 'application/json',
			url:ctx+'/manager/vendor/vendorPerforDataIn/update', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				data = JSON.parse(data);
				try{
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$('#updateapp').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示',data.msg  ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},
	saveImp:function() {
		$.messager.progress();
		$('#form-vendorPerforDataIn-import').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/vendor/vendorPerforDataIn/filesUpload', 
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
						$('#win-vendorPerforDataIn-import').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	}
}
$(function(){
	$('#cc').combobox({    
	    url:ctx+'/manager/vendor/vendorPerforDataIn/getVendorPerforCycle',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='cycleId']").val(rec.id);   
	      }
	})
})