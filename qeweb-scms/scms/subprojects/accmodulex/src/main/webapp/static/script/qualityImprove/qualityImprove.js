var qualityImprove = {
		
		chooseVenCode:function(){
			$('#kk').window('open');
			$('#datagrid-qualityImproveVendor-list').datagrid({url: ctx+'/manager/vendor/vendorInfor'
			});			
		},			
		chooseVenCode1:function(){
			$('#kk1').window('open');
			$('#datagrid-qualityImproveVendor-list1').datagrid({url: ctx+'/manager/vendor/vendorInfor'
			});			
		},	
		informFileDownLoad:function(v,r,i){
			if(r.informFileName==null){
				return "";
				}else{
			return '<a href="javascript:;" onclick="File.download(\''+r.informFilePath+'\',\'\')">'+r.informFileName+'</a>';}
		},
		improveFileDownLoad:function(v,r,i){
			if(r.improveFileName==null){
				return "";
				}else{
			return '<a href="javascript:;" onclick="File.download(\''+r.improveFilePath+'\',\'\')">'+r.improveFileName+'</a>';}
		},
		search : function(){
			var searchParamArray = $('#form-qualityImprove-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-qualityImprove-list').datagrid('load',searchParams);
		},
		
		informSearch : function(){
			var searchParamArray = $('#form-qualityImproveVendor-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-qualityImproveVendor-list').datagrid('load',searchParams);	
			},
		informSearch1 : function(){
			var searchParamArray = $('#form-qualityImproveVendor-search1').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-qualityImproveVendor-list1').datagrid('load',searchParams);	
			},
		
		addInform:function(){
			$('#win-qualityImprove-addInform').window({
				iconCls:'icon-add',
				title:'新增通知'
			});
			$('#form-qualityImprove-addInform').form('clear');
			$('#addInform').val(0);
			$('#win-qualityImprove-addInform').window('open');
		},
		
		saveInform:function(){
			$.messager.progress();
			$('#form-qualityImprove-addInform').form('submit',{
				ajax:true,
				url:ctx+'/manager/qualityassurance/qualityImprove/informUpload',
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
							$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'info');
							$('#win-qualityImprove-addInform').window('close');
							$('#datagrid-qualityImprove-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});		
		},
		
		modInform:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length !=1){
				$.messager.alert('提示','没有选择任何记录或选择多个！','info');
				return false;
			}
			$('#win-qualityImprove-addInform').window({
				iconCls:'icon-add',
				title:'修改通知'
			});
			var id = selections[0]['id'];
			var vId = selections[0]['vendor']['id'];
			var vName = selections[0]['vendor']['name'];
			$('#form-qualityImprove-addInform').form('clear');
			$('#win-qualityImprove-addInform').window('open');
			$('#win-qualityImprove-addInform').form('load',ctx+'/manager/qualityassurance/qualityImprove/getQualityImprove/'+id);
			$("#mid").val(id);
			$("#vendorid").val(vId);
			$('#form-user-addoredit-parentId').textbox('setText',vName);
		},
	
		
		publish:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			$('#win-qualityImprove-publishInform').window({
				iconCls:'icon-add',
				title:'发布通知'
			});
			$('#form-qualityImprove-publishInform').form('clear');			
			$('#win-qualityImprove-publishInform').window('open');
		    $('#pVendCode').textbox('setValue',selections[0].vendor.code);
		    $('#pVendName').textbox('setValue',selections[0].vendor.name);
			$('#pInformFile').textbox('setValue',selections[0].informFileName);
		},
		
		publishInform:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			for(i = 0;i < selections.length;i++) {
				 if(selections[i].dataStatus != null && selections[i].dataStatus == '1') {
					 $.messager.alert('提示','包含已发布记录无法重复发布！','error');
					return false;
				}   
		    } 
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/qualityassurance/qualityImprove/publishInform',
				type:'POST',
				data:params,
				dataType:"json",
				contentType : 'application/json',
				success:function(data){					
					$('#win-qualityImprove-publishInform').window('close');
					$('#datagrid-qualityImprove-list').datagrid('reload');
				}   
			});
		},	
		
		
		
		close:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			for(i = 0;i < selections.length;i++) {
				 if(selections[i].dataStatus != null && selections[i].dataStatus == '-1') {
					 $.messager.alert('提示','包含已关闭记录无法重复关闭！','error');
					return false;
				}   
		    } 
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/qualityassurance/qualityImprove/closeInform',
				type:'POST',
				data:params,
				dataType:"json",
				contentType : 'application/json',
				success:function(data){					
					$('#datagrid-qualityImprove-list').datagrid('reload');
				}   
			});
		},
		
		
		
		abolishInform:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var params = $.toJSON(selections);
			
			$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
				if (data) {
					$.ajax({
						url:ctx+'/manager/qualityassurance/qualityImprove/abolishInform',
						type:'POST',
						data:params,
						dataType:"json",
						contentType : 'application/json',
						success:function(data){					
							$('#datagrid-qualityImprove-list').datagrid('reload');
						}   
					});
				} else {
					$.messager.progress('close');
				}
			});
		},
		
		
		
		
		
		
		addImprove: function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length !=1 ){
				$.messager.alert('提示','没有选择任何记录或选择多个！','info');
				return false;
			}
			$('#win-qualityImprove-addImprove').window({
				iconCls:'icon-add',
				title:'上传/修改方案'
			});
			var id = selections[0]['id'];
			$('#form-qualityImprove-addImprove').form('clear');			
			$('#win-qualityImprove-addImprove').window('open');
			$('#win-qualityImprove-addImprove').form('load',ctx+'/manager/qualityassurance/qualityImprove/getQualityImprove/'+id);

		},
		saveImprove: function(){
			$.messager.progress();
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length !=1 ){
				$.messager.alert('提示','没有选择任何记录或选择多个！','info');
				return false;
			}
			$('#win-qualityImprove-addImprove').window({
				iconCls:'icon-add',
				title:'上传/修改方案'
			});
			var id = selections[0]['id'];
			$('#form-qualityImprove-addImprove').form('submit',{
				ajax:true,
				url:ctx+'/manager/qualityassurance/qualityImprove/saveImprove/'+id,
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
							$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'info');
							$('#win-qualityImprove-addImprove').window('close');
							$('#datagrid-qualityImprove-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});		
		},
		
		
		
		
		resultHandle:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');
			if(selections.length !=1){
				$.messager.alert('提示','没有选择任何记录或选择多个！','info');
				return false;
			}
			$('#win-qualityImprove-resHandle').window({
				iconCls:'icon-add',
				title:'审核处理'
			});
			$('#form-qualityImprove-resHandle').form('clear');			
			$('#win-qualityImprove-resHandle').window('open');
			$('#aVendCode').textbox('setValue',selections[0].vendor.code);
			$('#aVendName').textbox('setValue',selections[0].vendor.name);
			$('#aInformFile').textbox('setValue',selections[0].informFileName);
			$('#aImproveFile').textbox('setValue',selections[0].improveFileName);
			
		},
		
		
		
		
		conImprove:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');			
						
			$('#form-qualityImprove-resHandle').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/qualityassurance/qualityImprove/conImprove/'+selections[0].id,
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){	
					
						$('#win-qualityImprove-resHandle').window('close');
						$('#datagrid-qualityImprove-list').datagrid('reload');
						
				}
			});		
			
		},
		
		finishImprove:function(){
			var selections = $('#datagrid-qualityImprove-list').datagrid('getSelections');	
			$('#form-qualityImprove-resHandle').form('submit',{
				ajax:true,
				url:ctx+'/manager/qualityassurance/qualityImprove/finishImprove/'+selections[0].id,
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){	
						$('#win-qualityImprove-resHandle').window('close');
						$('#datagrid-qualityImprove-list').datagrid('reload');
				}
			});		
		},
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
