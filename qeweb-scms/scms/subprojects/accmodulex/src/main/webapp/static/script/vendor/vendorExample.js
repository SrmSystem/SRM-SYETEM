var vendorExamples = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	
}
function exp(){

	$('#form').form('submit',{
		url:ctx+'/manager/vendor/vendorExample/exportExcel', 
		success:function(data){
			$.messager.progress('close');
		}
	});
}