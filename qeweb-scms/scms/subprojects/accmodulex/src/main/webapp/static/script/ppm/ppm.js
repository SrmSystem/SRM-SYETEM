var ppm ={
	search : function(){
		var searchParamArray = $('#form-ppm-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load',searchParams);
	},
	
	//导出
	exportPPM : function(ppmType) {
		$('#form-ppm-search').form('submit',{
			url: ctx+'/manager/qualityassurance/ppm/exportExcel/'+ppmType,
			success:function(data){
				$.messager.progress('close');
			}
		});
	}
	
}