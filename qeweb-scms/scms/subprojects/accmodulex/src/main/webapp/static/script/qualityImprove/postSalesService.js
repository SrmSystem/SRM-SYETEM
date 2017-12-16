var postSalesService ={
	search : function(){
		var searchParamArray = $('#form-postSalesService-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load',searchParams);
	},
	search1 : function(){
		var searchParamArray = $('#formsearch1').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid1').datagrid({url: ctx+'/manager/vendor/vendorInfor/',
			queryParams : searchParams
		});
	},
	search2 : function(){
		var searchParamArray = $('#formsearch2').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid2').datagrid({url: ctx+'/manager/basedata/material',
			queryParams : searchParams
		});
	},
	searchRf: function(){
		var searchParamArray = $('#form-psReportForms-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-psReportForms-list').datagrid('load',searchParams);	
		},
	add : function(){
		$('#id').val("0");
		$('#addform').form('reset')
		$('#add').window('open');
	},
	add1 : function(){
		$('#datagrid1').datagrid('reload',ctx+'/manager/vendor/vendorInfor/');
		$('#add1').window('open');
	},
	add2 : function(){
		$('#datagrid2').datagrid('reload',ctx+'/manager/basedata/material');
		$('#add2').window('open');
	},
	addsumbil1 :function(){
		var selections1 = $('#datagrid1').datagrid('getSelections');
		if(selections1.length==0){
			$.messager.alert('提示','没有选择任何供应商记录！','info');
			return false;
		}
		if(selections1.length!=1){
			$.messager.alert('提示','只能选择一条供应商记录！','info');
			return false;
		}
		$("#vendorId").val(selections1[0]['id']);
		$("#vendorCode").textbox('setText',selections1[0].org.code);
		$("#vendorName").textbox('setText',selections1[0]['name']);
		$('#add1').window('close');
	},
	addsumbil2 :function(){
		var selections2 = $('#datagrid2').datagrid('getSelections');
		if(selections2.length==0){
			$.messager.alert('提示','没有选择任何物料记录！','info');
			return false;
		}
		if(selections2.length!=1){
			$.messager.alert('提示','只能选择一条物料记录！','info');
			return false;
		}
		$("#materialId").val(selections2[0]['id']);
		$("#materialCode").textbox('setText',selections2[0]['code']);
		$("#materialName").textbox('setText',selections2[0]['name']);
		$('#add2').window('close');
	},
	update : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何用户记录！','info');
			return false;
		}
		if(selections.length!=1){
			$.messager.alert('提示','只能选取一条记录！','info');
			return false;
		}
		if(selections[0]['qualityStatus']==1)
		{
			$.messager.alert('提示','存在已经发布的信息！','info');
			return false;
		}
		$("#addform").form('load',selections[0]);
		$('#id').val(selections[0]['id']);
		$("#vendorId").val(selections[0]['vendorId']);
		$("#materialId").val(selections[0]['materialId']);
		
		$("#vendorCode").textbox('setValue',selections[0].vendorBaseInfoEntity.code);
		$("#vendorName").textbox('setValue',selections[0].vendorBaseInfoEntity.name);

		$("#materialCode").textbox('setValue',selections[0].materialEntity.code);
		$("#materialName").textbox('setValue',selections[0].materialEntity.name);
		
		
		$('#add').window('open');
	},
	addsumbil :function(){
		var vendorId=$("#vendorId").val();
		var materialId=$("#materialId").val();
		if(vendorId==null||vendorId=='')
		{
			$.messager.alert('提示',"请选择供应商" ,'error');
			return false;
		}
		if(materialId==null||materialId=='')
		{
			$.messager.alert('提示',"请选择物料" ,'error');
			return false;
		}
		$.messager.progress();
		$('#addform').form('submit',{
			dataType:"json",
			contentType : 'application/json',
			url:ctx+'/manager/qualityassurance/PostSalesService/updateSave/add', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				data = JSON.parse(data);
				try{
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$('#add').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示',data.msg  ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	imp : function(){
		$('#winImport').window('open');
	},
	impSumbim : function(){
		formImport
		$.messager.progress();
		$('#formImport').form('submit',{
			dataType:"json",
			contentType : 'application/json',
			url:ctx+'/manager/qualityassurance/PostSalesService/filesUpload', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				data = JSON.parse(data);
				try{
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$('#winImport').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示',data.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + data.log + "\")'><b>日志文件</b></a>" ,'error');
					}
				}catch (e) {  
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
		for(var i=0;i<selections.length;i++)
		{
			if(selections[i]['qualityStatus']==1)
			{
				$.messager.alert('提示','存在已经发布的信息！','info');
				return false;
			}
		}
		var params = $.toJSON(selections);
        $.messager.confirm('提示','确定发布吗？',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/qualityassurance/PostSalesService/publishItem',
					type:'POST',
					data:params,
					dataType:"json",
					contentType : 'application/json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',data.msg,'info');
							$('#datagrid').datagrid('reload');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
						}
						
					}
				});
			}
        })
	},
	calcul : function(){
		$.messager.confirm('提示','确定计算吗？',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/qualityassurance/PostSalesService/calculate',
					type:'POST',
					dataType:"json",
					contentType : 'application/json',
					success:function(data){
						if(data.success){
							$.messager.alert('提示',data.message,'info');
							$('#datagrid').datagrid('reload');
						}
						else{
							$.messager.alert('提示',data.msg,'error');
						}
					}
				});
			}
		});
	},
	reportForms : function(){
		$('#psReportForms').window('open');
	},
}