var PerformanceSourceData = {
	search : function() {
		$('#datagrid').datagrid('load', $.jqexer.formIdToJson('#form-source-search'));
	},
	optFmt : function(v,r,i){
		return OptFmtUtil.datagrid('PerformanceSourceData.edit('+r.id+')','编辑');
	},
	add : function(){
		$('#dialog-edit').dialog('setTitle','新增');
		var $form = $('#dialog-edit').getCmp('form');
		$form.form('reset');
		$form.getCmp('id').val('0');
		$('#dialog-edit').dialog('open');
		$('#dialog-edit').dialog('autoSize');
	},
	edit : function(id){
		$('#dialog-edit').dialog('setTitle','编辑');
		var $form = $('#dialog-edit').getCmp('form');
		$('#dialog-edit').dialog('open');
		$('#dialog-edit').dialog('autoSize');
		$form.form('load',ctx+'/manager/vendor/performance/sourcedata/get/'+id);
		
		setTimeout(function (){
			var dimId = $('#dimId').combobox('getValue');  
			$.ajax({
				type : "POST",  
				url : ctx+ "/manager/vendor/performance/index/getList/" + dimId,
				cache : false,   
				dataType : "json",
				success : function(data) {
					$("#indexId").combobox("loadData", data);
				}
			});  
		}, 200);
		
		setTimeout(function (){
			var indexId = $('#indexId').combobox('getValue');
			$.ajax({
				type : "POST",
				url : ctx + "/manager/vendor/performance/index/getkey/" + indexId,
				cache : false,
				dataType : "json",
				success : function(data) {
					$("#keyName").combobox("loadData", data);
				}
			});
		}, 400);
	},
	imp : function(){
		$('#dialog-imp').dialog('open');
		$('#dialog-imp').dialog('autoSize');
	},
	imppj : function(){
		$('#dialog-imppj').dialog('open');
		$('#dialog-imppj').dialog('autoSize');
	},
	impSubmit : function(){
		var url = '/manager/vendor/performance/sourcedata/imp';
	
		SubmitUtil.form('#dialog-imp-form',url,null,function(data){
			$('#dialog-imp-form').getCmp('logDiv').removeClass('hide');
			$('#dialog-imp-form').getCmp('logFile').unbind('click').bind('click',function(){
				File.showLog(data.logPath);
			});
			$('#dialog-imp').dialog('autoSize');
			if(data.success){
				$('#datagrid').datagrid('reload');
				$('#dialog-imp').dialog('close');
			}
		},true);
	},
	impSubmitpj : function(){
		var url = '/manager/vendor/performance/sourcedata/imppj';
		
		SubmitUtil.form('#dialog-imp-formpj',url,null,function(data){
			$('#dialog-imp-formpj').getCmp('logDiv').removeClass('hide');
			$('#dialog-imp-formpj').getCmp('logFile').unbind('click').bind('click',function(){
				File.showLog(data.logPath);
			});
			$('#dialog-formpj').dialog('autoSize');
			if(data.success){
				$('#datagrid').datagrid('reload');
				$('#dialog-formpj').dialog('close');
			}
		},true);
	},
	submit : function(){
		var performanceModelId = $('#performanceModelId').combobox('getValues');
		if(performanceModelId==0)
		{
			$.messager.alert('提示','请选择绩效类型！','error');
			return false
		}
		var $form = $('#dialog-edit').getCmp('form');
		var id = $form.getCmp('id').val();
		var url = '/manager/vendor/performance/sourcedata/add';
		if(id!=0){
			url = '/manager/vendor/performance/sourcedata/update';
		}
		SubmitUtil.form('#dialog-edit-form',url,null,function(){
			$('#datagrid').datagrid('reload');
			$('#dialog-edit').dialog('close');
		},true);
	},
	del : function() {
		var ids = [];
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$.each(selections,function(i,n){
			ids.push(n.id);
		});
		$.messager.progress();  
		$.ajax({
			url : ctx + '/manager/vendor/performance/sourcedata/delete',
			data:{ids:ids},
			dataType : 'json',
			method : 'post',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success(data.msg);
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.fail(data.msg);
					$('#datagrid').datagrid('reload');
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	tb : function() {
		var ids = [];
		$.messager.progress();  
		$.ajax({
			url : ctx + '/manager/vendor/performance/sourcedata/tb',
			data:{ids:ids},
			dataType : 'json',
			method : 'post',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success(data.msg);
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.fail(data.msg);
					$('#datagrid').datagrid('reload');
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	tb2 : function() {
		var ids = [];
		$.messager.progress();  
		$.ajax({
			url : ctx + '/manager/vendor/performance/sourcedata/tb2',
			data:{ids:ids},
			dataType : 'json',
			method : 'post',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success(data.msg);
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.fail(data.msg);
					$('#datagrid').datagrid('reload');
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	}
};



