/** 调查表的模版管理 */
var SurveyTemplate = {
	managerFmt : function(v,r,i){
	  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="SurveyTemplate.edit('+r.id+');">编辑</a>';
	},
	search : function(){
		var searchParamArray = $('#form-vendorSurveyTemplate-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-vendorSurveyTemplate-list').datagrid('load',searchParams);
	},
	add : function(){
		$('#win-vendorSurveyTemplate-addoredit').window({
			iconCls:'icon-add',
			title:'新增调查表模版'
		});
		$('#code').textbox('enable');
		$('#form-vendorSurveyTemplate-addoredit').form('clear');
		$('#id').val(0);
		$('#win-vendorSurveyTemplate-addoredit').window('open');
	},
	edit : function(id){
		$('#win-vendorSurveyTemplate-addoredit').window({
			iconCls:'icon-edit',
			title:'编辑调查表模版'
		});
		$('#win-vendorSurveyTemplate-addoredit').window('open');
		$('#form-vendorSurveyTemplate-addoredit').form('load',ctx+'/manager/vendor/vendorSurveyTemplate/getVendorSurveyTemplate/'+id);
	},
	del : function(){
		var selections = $('#datagrid-vendorSurveyTemplate-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/vendor/vendorSurveyTemplate/deleteVendorSurveyTemplate',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				
					$.messager.show({
						title:'消息',
						msg:'删除调查表模版成功',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});

					//$.messager.alert('提示','删除用户成功!','info');
					$('#datagrid-vendorSurveyTemplate-list').datagrid('reload');
				
			}
		});
	},
	submit : function(){
		var url = ctx+'/manager/vendor/vendorSurveyTemplate/addNewVendorSurveyTemplate';
		var sucMeg = '添加调查表模版成功！';
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/vendor/vendorSurveyTemplate/update';
			sucMeg = '编辑调查表模版成功！';
		}
		$.messager.progress();
		$('#form-vendorSurveyTemplate-addoredit').form('submit',{
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
					$.messager.alert('提示',sucMeg,'info');
					$('#win-vendorSurveyTemplate-addoredit').window('close');
					$('#datagrid-vendorSurveyTemplate-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
		
		
}


