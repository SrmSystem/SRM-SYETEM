
//预测计划总量模板下载
function filesDownload(){
	$('#form-purchaseTotalplan-filesDownload').form('submit',{
		url:ctx+'/manager/order/purchaseTotalPlan/filesDownload', 
		success:function(data){
			$.messager.progress('close');
		}
	});
}
//导入预测计划总量
function saveimportplan(){
	$.messager.progress();
	$('#form-totalPlan-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:ctx+'/manager/order/purchaseTotalPlan/filesUpload', 
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
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('orderPlan.orderTotal.importForecasting')/*'导入预测计划总量成功'*/,'info');
				$('#win-totalPlan-import').window('close');
				$('#datagrid-purchaseTotalplan-list').datagrid('reload');         
			}else{
				//清空数据
				$('#form-totalPlan-import').form('clear'); 
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,result.msg + "<br>"+$.i18n.prop('label.importLogsReferTo')+"<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>"+$.i18n.prop('order.message14')+"</b></a>" ,'error');/*导入日志请参阅*//*日志文件*/
			}
			}catch (e) {  
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data,'error');
			}
		}
	});
} 