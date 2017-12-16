function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editProductLine('+r.id+');">编辑</a>';
		}
function searchProductLine(){
	var searchParamArray = $('#form-productLine-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-productLine-list').datagrid('load',searchParams);
}

function addProductLine(){
	$('#win-productLine-addoredit').window({
		iconCls:'icon-add',
		title:'新增产品线'
	});
	$('#code').textbox('enable');
	$('#form-productLine-addoredit').form('clear');
	$('#id').val(0);
	$('#win-productLine-addoredit').window('open');
}

function submitAddorEditproductLine(){
	var url = ctx+'/manager/basedata/productLine/addNewProductLine';
	var sucMeg = '添加产品线成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/productLine/update';
		sucMeg = '编辑产品线成功！';
	}
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	$('#form-productLine-addoredit').form('submit',{
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
				$('#win-productLine-addoredit').window('close');
				$('#datagrid-productLine-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
		
	});
}

function deleteProductLine(){
	var selections = $('#datagrid-productLine-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url:ctx+'/manager/basedata/productLine/deleteProductLine',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
			
				$.messager.show({
					title:'消息',
					msg:'删除产品线成功',
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});

				//$.messager.alert('提示','删除用户成功!','info');
				$('#datagrid-productLine-list').datagrid('reload');
			
		}
	});
}

function editProductLine(id){
	$('#win-productLine-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑产品线'
	});
	$('#code').textbox('disable');
	$('#win-productLine-addoredit').window('open');
	$('#form-productLine-addoredit').form('load',ctx+'/manager/basedata/productLine/getProductLine/'+id);
}