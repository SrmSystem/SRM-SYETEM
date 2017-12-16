var mapped = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	add : function(){
		$("#id").remove();
		$('#form-mapped-saveUpadte').form('reset');
		$('#dialog-mapped-saveUpadte').window('open');
	},
	update : function(){
			var selections = $('#datagrid').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			if(selections.length>1){
				$.messager.alert('提示','只能选取一条数据！','info');
				return false;
			}
			$("#form-mapped-saveUpadte").append('<input id="id" name="id" value="'+selections[0]["id"]+'" type="hidden"/>');
			$('#form-mapped-saveUpadte').form("load",selections[0]);
			$('#dialog-mapped-saveUpadte').window('open');
	},
	submit : function(){
		var url = "";
		if($("#id").val()!=''&&$("#id").val()!=null) {
			url = ctx+'/manager/vendor/mapped/updatemapped';
		} else {
			url = ctx+'/manager/vendor/mapped/addmapped';
		}
		$('#form-mapped-saveUpadte').form('submit',{
			url:url, 
			dataType:"json",
			success:function(data){
				try{
					var obj = JSON.parse(data);
					if(obj.success) {
						$.messager.alert('提示',obj.msg,'info');
						$('#dialog-mapped-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',obj.msg,'info');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		});
	}
}