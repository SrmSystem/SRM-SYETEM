var EpModuleMaterialRel = {
	operateFmt : function(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="EpModuleMaterialRel.edit('+r.id+');">编辑</a>';
	},
	query : function(){
		var searchParamArray = $('#form-epModuleMaterialRel-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-epModuleMaterialRel-list').datagrid('load',searchParams);
	},
	add : function(){
		$('#win-epModuleMaterialRel-addoredit').window({
			iconCls:'icon-add',
			title:'新增物料报价模型关系'
		});
		$('#form-epModuleMaterialRel-addoredit').form('clear');
		$('#id').val(0);
		$('#win-epModuleMaterialRel-addoredit').window('open');
		$('#win-epModuleMaterialRel-addoredit').window('autoSize');
	},
	submit : function(){
		var url = ctx+'/manager/ep/epModuleMaterialRel/saveRel';
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$('#form-epModuleMaterialRel-addoredit').form('submit',{
			ajax:true,
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
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示',"保存成功",'info');
					$('#win-epModuleMaterialRel-addoredit').dialog('close');
					$('#datagrid-epModuleMaterialRel-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	},
	del : function(){
		var selections = $('#datagrid-epModuleMaterialRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','该操作不可恢复,确定要删除所选的物料报价模型关系吗？',function(r){
			if(r){
				$.messager.progress({
					title:'提示',
					msg : '提交中...'
				});
				$.ajax({
					url:ctx+'/manager/ep/epModuleMaterialRel/batchDelete',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:'消息',
							msg:'删除物料报价模型关系成功',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-epModuleMaterialRel-list').datagrid('reload');
					}
				});
			}
		});
	},
	openWinMaterial : function() {
		$.parser.parse($('#win-material-detail'));
		$('#win-material-detail').dialog('open'); 
		$('#datagrid-material-list').datagrid({   
	    	url: ctx +  '/manager/ep/epModuleMaterialRel/getMaterialList'
		});
	},
	searchMaterial: function() {
		var searchParamArray = $('#form-material-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-material-list').datagrid('load',searchParams);
	},
	choiceMaterial: function(){
		var selections = $('#datagrid-material-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
	
		var name = selections[0]["name"];
		var id = selections[0]["id"];
		
		$("#materialId").val(id);
		$("#materialName").textbox('setValue', name);
		
		$('#win-material-detail').dialog('close');	
	},
	openWinModule : function() {
		$.parser.parse($('#win-module-detail'));
		$('#win-module-detail').dialog('open'); 
		$('#datagrid-module-list').datagrid({   
	    	url: ctx + '/manager/ep/epModuleMaterialRel/getModuleList'
		});
	},
	searchModule: function() {
		var searchParamArray = $('#form-module-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-module-list').datagrid('load',searchParams);
	},
	choiceModule: function(){
		var selections = $('#datagrid-module-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
	
		var name = selections[0]["name"];
		var id = selections[0]["id"];
		
		$("#moduleId").val(id);
		$("#moduleName").textbox('setValue', name);
		
		$('#win-module-detail').dialog('close');	
	},
	openImportWin : function() {
		$('#win-rel-import').dialog({
			iconCls:'icon-add',
			title:'导入物料报价模型关系'
		});
		$('#form-rel-import').form('clear');
		$('#win-rel-import').dialog('open');
	},
	saveimport : function(){
		var url = ctx +'/manager/ep/epModuleMaterialRel/filesUpload';
		$('#form-rel-import').form('submit',{
			ajax:true,
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
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示',result.msg,'info');
					$('#win-rel-import').dialog('close');
					$('#datagrid-epModuleMaterialRel-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
};