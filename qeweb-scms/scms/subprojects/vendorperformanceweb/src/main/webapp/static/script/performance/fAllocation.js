var fAllocation = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="fAllocation.update('+r.id+')">修改</button>';
	},
	add : function(){
		$('#dialog-fAllocation-saveUpadte').dialog();
		$("#id").remove();
		$('#form-fAllocation-saveUpadte').form('reset');
		$('#dialog-fAllocation-saveUpadte').window('open');
	},
	update : function(id){
		/*$.ajax({
			url:ctx+"/manager/vendor/fAllocation/updateFAllocationStart?id="+id,
			type:'POST',
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
		});*/
		$.post(ctx+"/manager/vendor/fAllocation/updateFAllocationStart",{"id":id},function(data){
			$('#dialog-fAllocation-saveUpadte').dialog();
			$('#form-fAllocation-saveUpadte').form('reset');
			var da= data.split(",");
			if($("#id").val()!=''&&$("#id").val()!=null)
			{
				$("#id").val(da[0]);
			}
			else
			{
				$("#form-fAllocation-saveUpadte").append('<input id="id" name="id" value="'+da[0]+'" type="hidden"/>');
			}
			$("#name").textbox('setValue',da[1]);
			$("#describe").textbox('setValue',da[2]);
			$("#fallValue").textbox('setValue',da[3]);
			$('#dialog-fAllocation-saveUpadte').window('open');
		},"text");
	},
	submit : function(){
		var url=ctx;
		if($("#id").val()!=''&&$("#id").val()!=null)
		{
			url=url+'/manager/vendor/fAllocation/updateFAllocation';
		}
		else
		{
			url=url+'/manager/vendor/fAllocation/addFAllocation';
		}
		$('#form-fAllocation-saveUpadte').form('submit',{
			url:url, 
			contentType : 'application/json',
			dataType:"json",
			success:function(data){
				try{
					data = JSON.parse(data);
					if(data.success) {
						$.messager.alert('提示',data.msg,'info');
						$('#dialog-fAllocation-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',data.msg,'info');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},

	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定删除吗？<br/>',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/vendor/fAllocation/deleteFAllocation',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"删除成功",'info');
						}
						else
						{
							$.messager.alert('提示',data,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	}
}