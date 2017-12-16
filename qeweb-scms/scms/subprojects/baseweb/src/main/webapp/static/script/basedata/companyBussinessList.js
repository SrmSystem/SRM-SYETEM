function searchCompanyBussiness(){
	var searchParamArray = $('#form-companyBussiness-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-companyBussiness-list').datagrid('load',searchParams);
}

function addCompanyBussiness(){
	$('#win-companyBussiness-addoredit').window({
		iconCls:'icon-add',
		title:'新增公司业务'
	});
	$('#code').textbox('enable');
	$('#form-companyBussiness-addoredit').form('clear');
	$('#id').val(0);
	$('#win-companyBussiness-addoredit').window('open');
}

function submitAddorEditcompanyBussiness(){
	var url = ctx+'/manager/basedata/companyBussiness/addNewCompanyBussiness';
	var sucMeg = '添加公司业务成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/companyBussiness/update';
		sucMeg = '编辑公司业务成功！';
	}
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	$('#form-companyBussiness-addoredit').form('submit',{
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
				$('#win-companyBussiness-addoredit').window('close');
				$('#datagrid-companyBussiness-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
		
	});
}

function deleteCompanyBussiness(){
	var selections = $('#datagrid-companyBussiness-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url:ctx+'/manager/basedata/companyBussiness/deleteCompanyBussiness',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
			
				$.messager.show({
					title:'消息',
					msg:'删除公司业务成功',
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});

				//$.messager.alert('提示','删除用户成功!','info');
				$('#datagrid-companyBussiness-list').datagrid('reload');
			
		}
	});
}

function editCompanyBussiness(id){
	$('#win-companyBussiness-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑公司业务'
	});
	$('#code').textbox('disable');
	$('#win-companyBussiness-addoredit').window('open');
	$('#form-companyBussiness-addoredit').form('load',ctx+'/manager/basedata/companyBussiness/getCompanyBussiness/'+id);
}

$(function(){
	
	
	
	
});

