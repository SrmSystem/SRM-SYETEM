var VendorConfirm = {
	managerFmt : function(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="confirmVendor('+r.id+');">确认</a>';
	},
	viewFmt : function(v,r,i){
		return '<button class="btn-link" onclick="VendorConfirm.openBaseInfo('+r.id+')">'+v.name+'</button>';
	},
	openBaseInfo : function(id){
		var $dialog = $('#dialog-baseInfo');
		var url = ctx+'/manager/vendor/vendorBaseInfo/confirmInfoPage/'+id;
		  $dialog.dialog({
			  href : url,
			  onOpen : function(){
			  }
		  });
		  $dialog.dialog('open');
		 
	},
	confirm : function(){
		var selArray = $('#datagrid').datagrid('getSelections');
		if(selArray.length<=0){
			$.messager.alert('提示','请选择需要确认的供应商！','warning');
			return;
		}
		var flag = false;
		for(var i = 0;i < selArray.length ; i++){
			var org = selArray[i]["org"];
			if(org.confirmStatus!=0){
				flag = true;
				break;
			}
		}
		if(flag){
			$.messager.alert('提示','您选择的记录中存在被确认或驳回的供应商!','warning');
			return false;
		}
		$.messager.confirm("操作提示", "确定要执行确认操作吗？", function (data) {
		    if (data) {
				$.ajax({
					url:ctx+'/manager/vendor/vendorBaseInfo/vendorConfirm',
					method:'post',
					data:$.toJSON(selArray),
					contentType:'application/json',
					dataType:'json',
					success:function(data){
						$.messager.progress('close');
						if(data.success){
							$.messager.alert('提示','确认成功','info');
							$('#datagrid').datagrid('reload');
						}
						
					}
					
				});
		   }
		});
		
	},
	confirm2 : function(id){
		var val = $('#confirmStatus_hidden').val()
		
		if(val!=0){
			$.messager.alert('提示','该供应商已被确认或驳回!','warning');
			return false;
		}
		$.messager.confirm("操作提示", "确定要执行确认操作吗？", function (data) {
			if (data) {
				$.ajax({
					url:ctx+'/manager/vendor/vendorBaseInfo/vendorConfirm2/'+id,
					method:'post',
					contentType:'application/json',
					dataType:'json',
					success:function(data){
						$.messager.progress('close');
						if(data.success){
							var $dialog = $('#dialog-baseInfo');
							$dialog.dialog('close');
							$.messager.alert('提示','确认成功','info');
							$('#datagrid').datagrid('reload');
						}
						
					}
				});
			}
		});
		
	},
	confirmReject : function(){
		var selArray = $('#datagrid').datagrid('getSelections');
		if(selArray.length<=0){
			$.messager.alert('提示','请选择需要驳回的供应商！','warning');
			return;
		}
		var flag = false;
		for(var i = 0;i < selArray.length ; i++){
			var org = selArray[i]["org"];
			if(org.confirmStatus!=0){
				flag = true;
				break;
			}
		}
		if(flag){
			$.messager.alert('提示','您选择的记录中存在被确认或驳回的供应商!','warning');
			return false;
		}
		$.messager.confirm("操作提示", "确定要执行驳回操作吗？", function (data) {
			if (data) {
				$.ajax({
					url:ctx+'/manager/vendor/vendorBaseInfo/confirmReject',
					method:'post',
					data:$.toJSON(selArray),
					contentType:'application/json',
					dataType:'json',
					success:function(data){
						$.messager.progress('close');
						if(data.success){
							$.messager.alert('提示','驳回成功','info');
							$('#datagrid').datagrid('reload');
						}
						
					}
				
				});
			}
		});
		
	},
	confirmReject2 : function(id){
		var val = $('#confirmStatus_hidden').val()
		if(val!=0){
			$.messager.alert('提示','该供应商已被确认或驳回!','warning');
			return false;
		}
		$.messager.confirm("操作提示", "确定要执行驳回操作吗？", function (data) {
			if (data) {
				$.ajax({
					url:ctx+'/manager/vendor/vendorBaseInfo/confirmReject2/'+id,
					method:'post',
					contentType:'application/json',
					dataType:'json',
					success:function(data){
						$.messager.progress('close');
						if(data.success){
							$.messager.alert('提示','驳回成功','info');
							$('#datagrid').datagrid('reload');
						}
						
					}
				
				});
			}
		});
		
	}
    
		
		
}