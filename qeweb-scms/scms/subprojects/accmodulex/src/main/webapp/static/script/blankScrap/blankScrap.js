var blankScrap ={
		
	search : function(){
		var searchParamArray = $('#form-blankScrap-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load',searchParams);
	},
	
	searchRf: function(){
		var searchParamArray = $('#form-bsReportForms-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-bsReportForms-list').datagrid('load',searchParams);	
		},
		
	searchVen : function(){
		var searchParamArray = $('#formsearchVen').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid1').datagrid({url: ctx+'/manager/vendor/vendorInfor/',
			queryParams : searchParams
		});
	},
	searchMat : function(){
		var searchParamArray = $('#formsearchMat').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid2').datagrid({url: ctx+'/manager/basedata/material',
			queryParams : searchParams
		});
	},
	
	add : function(){
		$('#id').val("0");
		$('#addform').form('reset')
		$('#add').window('open');
	},
	
	choose1 : function(){
		$('#datagrid1').datagrid('reload',ctx+'/manager/vendor/vendorInfor/');
		$('#choose1').window('open');
	},
	
	choose2 : function(){
		$('#datagrid2').datagrid('reload',ctx+'/manager/basedata/material');
		$('#choose2').window('open');
	},
	
	bringBackVen : function(){
		var selections = $('#datagrid1').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何供应商记录！','info');
			return false;
		}
		if(selections.length!=1){
			$.messager.alert('提示','只能选择一条供应商记录！','info');
			return false;
		}
		$("#vendorId").val(selections[0]['id']);
		$("#manufacturerCode").textbox('setValue',selections[0]['code']);
		$("#manufacturer").textbox('setValue',selections[0]['name']);
		$('#choose1').window('close');
	},
	
	bringBackMat : function(){
		var selections = $('#datagrid2').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何物料记录！','info');
			return false;
		}
		if(selections.length!=1){
			$.messager.alert('提示','只能选择一条物料记录！','info');
			return false;
		}
		$("#materialId").val(selections[0]['id']);
		$("#drawingNo").textbox('setValue',selections[0]['code']);
		$("#partsName").textbox('setValue',selections[0]['name']);
		$('#choose2').window('close');
	},
	
	mod : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何用户记录！','info');
			return false;
		}
		if(selections.length!=1){
			$.messager.alert('提示','只能选取一条记录！','info');
			return false;
		}
		if(selections[0]['state']==1){
			$.messager.alert('提示','存在已经发布的信息！','info');
			return false;
		}
		$("#addform").form('load',selections[0]);
		$('#id').val(selections[0]['id']);
		$('#add').window('open');
	},
	
	addsubmit : function(){
		var mCode=$("#manufacturerCode").textbox("getText");
		var dCode=$("#drawingNo").textbox("getText");
		if(mCode==null||mCode==''){
			$.messager.alert('提示',"请选择供应商" ,'error');
			return false;
		}
		if(dCode==null||dCode==''){
			$.messager.alert('提示',"请选择物料" ,'error');
			return false;
		}
		$('#addform').form('submit',{
			dataType:"json",
			contentType : 'application/json',
			url:ctx+'/manager/qualityassurance/blankScrap/updateSave/add', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
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
	
	import : function(){
		$('#winImport').window('open');
	},
	
	saveImp : function(){
		var impF=$('#file').val();
		if(impF == null || impF == ''){
			alert("请导入附件");
			return false;
		}
		$.messager.progress();
		$('#formImport').form('submit',{
			dataType:"json",
			contentType : 'application/json',
			url:ctx+'/manager/qualityassurance/blankScrap/filesUpload', 
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
	
	publish : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		for(var i=0;i<selections.length;i++){
			if(selections[i]['state']==1){
				$.messager.alert('提示','存在已经发布的信息！','info');
				return false;
			}
		}
		var params = $.toJSON(selections);
        $.messager.confirm('提示','确定发布吗？',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/qualityassurance/blankScrap/publishItem',
					type:'POST',
					data:params,
					dataType:"json",
					contentType : 'application/json',
					success:function(data){
						if(data.success){
							$.messager.alert('提示',data.msg,'info');
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
	
	calcul : function(){
		$.messager.confirm('提示','确定计算吗？',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/qualityassurance/blankScrap/calculate',
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
		$('#bsReportForms').window('open');
	},
	
	//导出
	exportExcle : function() {
		$('#form-bsReportForms-search').form('submit',{
			url: ctx+'/manager/qualityassurance/blankScrap/exportExcel',
			success:function(data){
				$.messager.progress('close');
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
}