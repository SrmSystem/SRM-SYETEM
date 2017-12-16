var dimensions = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="dimensions.update('+r.id+')">修改</button>';
	},
	release : function(){
		var selectedNode = $('#treegrid-dimen-list').treegrid('getSelected');
		if(selectedNode.abolished==0)
		{
			$.messager.alert('提示',"已经启用",'error');
			return false;
		}
        $.messager.confirm('提示','确定启用吗？',function(r){
			if(r){
				var params = $.toJSON(selectedNode);
				$.ajax({
					url:ctx+'/manager/vendor/performance/dimensions/releaseDimensions',
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType : 'json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',"启用成功",'info');
							$('#treegrid-dimen-list').treegrid('reload');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
							$('#treegrid-dimen-list').treegrid('reload');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
        })
	},
	dels : function(){
		var selectedNode = $('#treegrid-dimen-list').treegrid('getSelected');
		if(selectedNode.abolished==1)
		{
			$.messager.alert('提示',"已经作废",'error');
			return false;
		}
		$.messager.confirm('提示','确定作废吗？<br/><font style="color:#F00">将他下面的子维度和指标一并作废</font>',function(r){
			if(r){
				var params = $.toJSON(selectedNode);
				$.ajax({
					url:ctx+'/manager/vendor/performance/dimensions/delsDimensions',
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType : 'json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',"作废成功",'info');
							$('#treegrid-dimen-list').treegrid('reload');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
							$('#treegrid-dimen-list').treegrid('reload');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	deletes : function(){
		var selectedNode = $('#treegrid-dimen-list').treegrid('getSelected');
		$.messager.confirm('提示','确定删除吗？<br/><font style="color:#F00">将他下面的子维度和指标一并删除</font>',function(r){
			if(r){
				var params = $.toJSON(selectedNode);
				$.ajax({
					url:ctx+'/manager/vendor/performance/dimensions/deleteDimensions',
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType : 'json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',"删除成功",'info');
							$('#treegrid-dimen-list').treegrid('reload');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
							$('#treegrid-dimen-list').treegrid('reload');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	}
}