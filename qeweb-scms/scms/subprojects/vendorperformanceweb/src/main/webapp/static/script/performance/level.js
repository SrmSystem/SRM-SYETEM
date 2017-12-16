var level = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="level.update('+r.id+')">修改</button>';
	},
	add : function(){
		$('#dialog-level-saveUpadte').dialog();
		$.ajax({
			url:ctx+"/manager/vendor/level/getminAndmax",
			type:'POST',
			contentType : 'application/json',
			success:function(data){
				$("#id").remove();
				$("#tis").hide();
				$("#tidd").show();
				$('#form-level-saveUpadte').form('reset');
				var da= data.split(",");
				var s=parseInt(da[0]);
				$("#fatherId").val(da[1]);
				$('#lowerLimit').numberbox({    
					min:s,
				    max:s  
				}); 
				$('#upperLimit').numberbox({    
					min:s,
				    max:100  
				});  
				$("#lowerLimit").numberbox('setValue',s);
				$('#dialog-level-saveUpadte').window('open');
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},
	update : function(id){
		$('#dialog-level-saveUpadte').dialog();
		
		$.ajax({
			url:ctx+"/manager/vendor/level/updateLevelStart?id="+id,
			type:'POST',
			contentType : 'application/json',
			success:function(data){
				$('#form-level-saveUpadte').form('reset');
				$("#tidd").hide();
				$("#tis").show();
				var da= data.split(",");
				var datsa = $('#abolished').combobox('getData');
				if(da[7]=='0')
				{
					$('#abolished').combobox('select',"0");
				}
				else if(da[7]=='1')
				{
					$('#abolished').combobox('select',"1");
				}
				if($("#id").val()!=''&&$("#id").val()!=null)
				{
					$("#id").val(da[0]);
				}
				else
				{
					$("#form-level-saveUpadte").append('<input id="id" name="id" value="'+da[0]+'" type="hidden"/>');
				}
				$("#code").textbox('setValue',da[1]);
				$("#levelName").textbox('setValue',da[2]);
				$("#quadrant").textbox('setValue',da[5]);
				$("#remarks").textbox('setValue',da[6]);
				$("#fatherId").val(da[9]);
				var s=parseInt(da[3]);
				var ss=parseInt(da[4]);
				var sss=parseInt(da[8]);
				$('#lowerLimit').numberbox({    
					min:s,
				    max:s  
				}); 
				$('#upperLimit').numberbox({    
					min:s,
				    max:sss  
				});  
				$("#upperLimit").numberbox('setValue',ss);
				$("#lowerLimit").numberbox('setValue',s);
				$('#dialog-level-saveUpadte').window('open');
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},
	submit : function(){
		var url;
		if($("#id").val()!=''&&$("#id").val()!=null) {
			url = ctx+'/manager/vendor/level/updateLevel';
		} else {
			url = ctx+'/manager/vendor/level/addLevel';
		}
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});	
		$('#form-level-saveUpadte').form('submit',{
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
					data= JSON.parse(data);
					if(data.success) {
						$.messager.alert('提示',data.msg,'info');
						$('#dialog-level-saveUpadte').window('close');
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
	release : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
        $.messager.confirm('提示','确定启用吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
						$.messager.alert('提示','存在已启用的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/level/releaseLevel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"启用成功",'info');
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
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定作废吗？<br/><font style="color:#F00">将一并作废它的下一级</font>',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert('提示','存在已作废的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/level/delsLevel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"作废成功",'info');
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