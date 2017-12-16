
var subQuoManage={
		 getEipStatus :function(status) {
				if(status == 0)
					return '未回传';
				else if(status == 1)
					return '成功';
				else if(status == -1)
					return '失败';
				else 
					return '';
			},
		viewCostFmt : function(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="subQuoManage.viewCost(' + r.id +')">'+r.totalQuotePrice+'</a>';
		},
		viewCost : function(subQuoId) {
		
			$('#win-cost-detail').dialog('open'); 
			$("#epQuoSubId").val(subQuoId);
			$('#datagrid-costItem-list').datagrid({url: ctx + '/manager/ep/epSubQuo/getSubQuoCostByQuoSubId/'+subQuoId});
			

		},
		saveSubQuoCost:function(){
			$.messager.progress();
			$('#datagrid-costItem-list').datagrid('acceptChanges'); 
				  
			var rows = $('#datagrid-costItem-list').datagrid('getRows');
			  
			if(rows == null || rows.length == 0) {
						$.messager.progress('close');
						$.messager.alert('提示','请添加明细','error');
						return false;
			} 
			var o =$('#datagrid-costItem-list').datagrid('getData'); 
			var datas = JSON.stringify(o);   
			$("#tableCostDatas").val(datas);
			

			$('#form-subQuoCost-addoredit').form('submit',{
				ajax:true,
				url:ctx+'/manager/ep/epSubQuo/saveSubQuoCost/'+$("#epQuoSubId").val(),  
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){
					data = $.evalJSON(data);
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
					}else{
						$.messager.alert('提示',data.msg,'error');
					}
					$.messager.progress('close');
					$('#win-cost-detail').dialog('close'); 
					$('#datagrid-item-list').datagrid('reload');
					
				}
			});
			},
		saveSubQuo : function(submitStatus){
			$.messager.progress();
			var isValid = $('#form-subQuo-addoredit').form('validate');
			if(!isValid){
				$.messager.progress('close');
				$.messager.alert('提示','数据项不能为空','error');
				return false;
			}
			$('#datagrid-item-list').datagrid('acceptChanges'); 
				  
			var rows = $('#datagrid-item-list').datagrid('getRows');
			  
			if(rows == null || rows.length == 0) {
						$.messager.progress('close');
						$.messager.alert('提示','请添加明细','error');
						return false;
			} 
			for(var j=0;j<rows.length;j++){
				if(rows[j].subtotal==null){
					$.messager.progress('close');
					$.messager.alert('提示','小计不能为空','error');
					return false;
				}
			}
			var o =$('#datagrid-item-list').datagrid('getData'); 
			var datas = JSON.stringify(o);   
			$("#tableDatas").val(datas);
			
			$('#form-subQuo-addoredit').form('submit',{
				ajax:true,
				iframe: true,   
				url:ctx+'/manager/ep/epSubQuo/saveSubQuo/'+submitStatus,  
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){
					data = $.evalJSON(data);
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						parent.closeWindow();
					}else{
						$.messager.alert('提示',data.msg,'error');
					}
					window.location.reload();//刷新当前页面.
				}
			});
		}
}


