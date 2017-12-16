var Model = {
	optFmt : function(v,r,i){
		var text = '禁用';
		if(r.enableStatus==0){
			text = '启用';
		}
		
		var html = '<a href="javascript:;" class="row-btn" onclick="Model.enable('+r.id+','+r.enableStatus+')">'+text+'</a>';
		if(r.enableStatus==0)
			return html;
		html+='<a href="javascript:;" class="row-btn" onclick="Model.add('+r.id+')">编辑</a>';
		return html;
	},
	enable : function(id,enableStatus){
		var enable = 1;
		if(enableStatus==1){
			enable = 0;
		}
		$.messager.progress();
		$.ajax({
			type : 'post',
			url : ctx+'/manager/vendor/performance/model/update',
			data :{id:id,enableStatus:enable},
			dataType : 'json',
			success : function(result){
				$.messager.progress('close');
				if(result.success){
					$.messager.success();
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.fail(result.msg);
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	add : function(id){
		   $('#dg-form').form('clear');
		if(id!=null){
			$('#dg').dialog('setTitle','编辑');
			$('#dg-form').form('load',ctx+'/manager/vendor/performance/model/get/'+id);
		}else{
			$('#dg-form').getCmp('enableStatus').combobox('setValue',1);
		}
		$('#dg').dialog('open').dialog('autoSize');
	},
	submit : function(){
		var isValid = $("#dg-form").form('validate');
		if(!isValid){
			return false;
		}
		var id = $('#dg-form').getCmp('id').val();
		var url = ctx+'/manager/vendor/performance/model/add';
		if(id!=null && id!=''){
			url = ctx+'/manager/vendor/performance/model/update';
		}else{
			$('#dg-form').getCmp('id').val(0);
		}
		$.messager.progress();
		$('#dg-form').form('submit',{
			url : url,
			success : function(data){
				$.messager.progress('close');
				try{
					var result = $.parseJSON(data);
					if(result.success){
						$.messager.success();
						$('#datagrid').datagrid('reload');
						$('#dg').dialog('close');
					}else{
						$.messager.fail(result.msg);
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		})
		
	},
	batchDelete : function(){
		var rs = $('#datagrid').datagrid('getSelections');
		if(rs.length<=0){
			$.messager.alert('提示','没有选择记录','warning');
			return false;
		}
		var ids = [];
		$.each(rs,function(i,n){
			ids.push(n.id);
		});
		
		$.messager.confirm('提示', '确定执行此操作吗？', function (data) {
			if (data) {
				$.messager.progress();
				$.ajax({
					type : 'post',
					url : ctx+'/manager/vendor/performance/model/batchDelete',
					data :{ids:ids},
					dataType : 'json',
					success : function(result){
						$.messager.progress('close');
						if(result.success){
							$.messager.success();
							$('#datagrid').datagrid('reload');
						}else{
							$.messager.fail(result.msg);
						}
					},
					error:function(data) {
						$.messager.progress('close');
						$.messager.fail(data.responseText);
					}
				});
			}
		});
	}
		
};