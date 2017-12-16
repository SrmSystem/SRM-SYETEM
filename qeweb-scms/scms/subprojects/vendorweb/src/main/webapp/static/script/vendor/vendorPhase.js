var VendorPhase = {
	managerFmt : function(v,r,i){
	  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="VendorPhase.edit('+r.id+','+i+');">编辑</a>';
	},
	search : function(){
		var searchParamArray = $('#form-vendorPhase-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-vendorPhase-list').datagrid('load',searchParams);
	},
	edit : function(id,index){
		$('#win-vendorPhase-addoredit').window({
			iconCls:'icon-edit',
			title:'编辑供应商阶段'
		});
		
//		var row = $('#datagrid-vendorPhase-list').datagrid('getData').rows[index];
		
//		$('#code').textbox('setValue',row.code);
//		$('#name').textbox('setValue',row.name);
//		$('#id').val(row.id);
		
		$('#win-vendorPhase-addoredit').window('open');
		$('#form-vendorPhase-addoredit').form('load',ctx+'/manager/vendor/vendorPhase/getVendorPhase/'+id);
	},
	add : function(){
		$('#win-vendorPhase-addoredit').window({
			iconCls:'icon-add',
			title:'新增供应商阶段'
		});
		$('#code').textbox('enable');
		$('#form-vendorPhase-addoredit').form('clear');
		$('#id').val(0);
		$('#win-vendorPhase-addoredit').window('open');
	},
	del : function(){
		var selections = $('#datagrid-vendorPhase-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/vendor/vendorPhase/deleteVendorPhase',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				
					$.messager.show({
						title:'消息',
						msg:'删除供应商阶段成功',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});

					//$.messager.alert('提示','删除用户成功!','info');
					$('#datagrid-vendorPhase-list').datagrid('reload');
				
			}
		});
	},
	submit : function(){
		var url = ctx+'/manager/vendor/vendorPhase/addNewVendorPhase';
		var sucMeg = '添加供应商阶段成功！';
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/vendor/vendorPhase/update';
			sucMeg = '编辑供应商阶段成功！';
		}
		$.messager.progress();
		$('#form-vendorPhase-addoredit').form('submit',{
			ajax:true,
			url:url,
			queryParams : {},
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
					$('#win-vendorPhase-addoredit').window('close');
					$('#datagrid-vendorPhase-list').datagrid('reload');
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