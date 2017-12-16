var StatusDict = {
	operateFmt : function (v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="StatusDict.edit('+r.id+');">编辑</a>';
	},
	iconFmt : function(v,r,i){
		return '<img alt="'+r.statusText+'" src="'+ctx+'/static/style/icons/IconsExtension/'+r.statusIcon+'" />';
	},
	search : function(){
		var searchParamArray = $('#form-statusDict-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-statusDict-list').datagrid('load',searchParams);
	},
    add:function(){
		$('#win-statusDict-addoredit').window({
			iconCls:'icon-add',
			title:'新增状态'
		});
		$('#form-statusDict-addoredit').form('clear');
		$('#id').val(0);
		$('#win-statusDict-addoredit').window('open');
		$('#win-statusDict-addoredit').window('autoSize');
	},
    submit : function(){
		var url = ctx+'/manager/database/statusDict/addNewStatusDict';
		var sucMeg = '添加状态成功！';
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/database/statusDict/update';
			sucMeg = '编辑状态成功！';
		}
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$('#form-statusDict-addoredit').form('submit',{
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
					$('#win-statusDict-addoredit').window('close');
					$('#datagrid-statusDict-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
				//$('#datagrid-statusDict-list').datagrid('reload');
			}
			
		});
	},
    del : function(){
		var selections = $('#datagrid-statusDict-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/database/statusDict/deleteStatusDict',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				
					$.messager.show({
						title:'消息',
						msg:'删除状态成功',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});

					//$.messager.alert('提示','删除用户成功!','info');
					$('#datagrid-statusDict-list').datagrid('reload');
				
			}
		});
	},
    edit : function(id){
		$('#win-statusDict-addoredit').window({
			iconCls:'icon-edit',
			title:'编辑状态'
		});
		$('#win-statusDict-addoredit').window('open');
		$('#form-statusDict-addoredit').form('load',ctx+'/manager/database/statusDict/getStatusDict/'+id);
		$('#win-statusDict-addoredit').window('autoSize');
    }
		
		
}