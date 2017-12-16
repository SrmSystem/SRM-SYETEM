function resetForm(){
	
}
function searchBrand(){
	var searchParamArray = $('#form-brand-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-brand-list').datagrid('load',searchParams);
}

function addBrand(){
	$('#win-brand-addoredit').window({
		iconCls:'icon-add',
	//	title:'新增品牌'
		title: $.i18n.prop('basedata.brand.NewBrand')
	});
	$('#code').textbox('enable');
	$('#form-brand-addoredit').form('clear');
	$('#id').val(0);
	$('#win-brand-addoredit').window('open');
}

function submitAddorEditbrand(){
	var url = ctx+'/manager/basedata/brand/addNewBrand';
//	var sucMeg = '添加品牌成功！';
	var sucMeg =$.i18n.prop('basedata.brand.AddBrandSuccess');
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/brand/update';
//		sucMeg = '编辑品牌成功！';
		sucMeg = $.i18n.prop('basedata.brand.EditorsBrandSuccess');
	}
	$.messager.progress({
//		title:'提示',
//		msg : '提交中...'
		title:$.i18n.prop('label.remind'),
		msg : $.i18n.prop('base.Mail.Submiting')
	});
	$('#form-brand-addoredit').form('submit',{
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
				$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
				$('#win-brand-addoredit').window('close');
				$('#datagrid-brand-list').datagrid('reload');
			}else{
				$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
			}
			}catch (e) {
				$.messager.alert($.i18n.prop('label.remind'),data,'error');
			}
		}
		
	});
}

function deleteBrand(){
	var selections = $('#datagrid-brand-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url:ctx+'/manager/basedata/brand/deleteBrand',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
			
				$.messager.show({
		//			title:'消息',
		//			msg:'删除品牌成功',
					title: $.i18n.prop('label.news'),
					msg: $.i18n.prop('basedata.brand.DeleteBrandSuccess'),
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});

				//$.messager.alert('提示','删除用户成功!','info');
				$('#datagrid-brand-list').datagrid('reload');
			
		}
	});
}

function editBrand(id){
	$('#win-brand-addoredit').window({
		iconCls:'icon-edit',
//		title:'编辑品牌'
		title: $.i18n.prop('basedata.brand.EditorBrand')
	});
	$('#code').textbox('disable');
	$('#win-brand-addoredit').window('open');
	$('#form-brand-addoredit').form('load',ctx+'/manager/basedata/brand/getBrand/'+id);
}

$(function(){
	
	
	
	
});

