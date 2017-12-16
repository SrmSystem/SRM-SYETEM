var vendorPerforPurchasedatain = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	imp : function(){
		$('#win-vendorPerforPurchasedatain-import').dialog();
		$('#win-vendorPerforPurchasedatain-import').window({
			iconCls : 'icon-disk_upload',
			title : '数据导入'
		});
		$('#form-vendorPerforPurchasedatain-import').form('clear');
		$('#win-vendorPerforPurchasedatain-import').window('open');
	},
	saveImp:function() {
		$.messager.progress();
		$('#form-vendorPerforPurchasedatain-import').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/vendor/vendorPerforPurchasedatain/filesUpload', 
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
						$('#win-vendorPerforPurchasedatain-import').window('close');
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
$(function(){
	$('#cc').combobox({    
	    url:ctx+'/manager/vendor/vendorPerforReviews/getVendorPerforCycle',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='cycleId']").val(rec.id);   
	      }
	})
})