var zeroKilometers = {
		
		chooseVenCode:function(){
			$('#kk1').window('open');
			$('#datagrid-zeroKilometersVendor-list').datagrid({url: ctx+'/manager/vendor/vendorInfor'
			});			
		},
		chooseFirstPictureCode:function(){
			$('#kk2').window('open');
			$('#datagrid-zeroKilometersVendor2-list').datagrid({url: ctx+'/manager/basedata/material'
			});			
		},
		mchooseVenCode:function(){
			$('#mkk1').window('open');
			$('#datagrid-mzeroKilometersVendor-list').datagrid({url: ctx+'/manager/vendor/vendorInfor'
			});			
		},
		mchooseFirstPictureCode:function(){
			$('#mkk2').window('open');
			$('#datagrid-mzeroKilometersVendor2-list').datagrid({url: ctx+'/manager/basedata/material'
			});			
		},
		
		search : function(){
			var searchParamArray = $('#form-zeroKilometers-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-zeroKilometers-list').datagrid('load',searchParams);	
			},
		vSearch : function(){
			var searchParamArray = $('#form-zeroKilometers-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-zeroKilometers-list').datagrid('load',searchParams);	
			},
		rfSearch: function(){
			var searchParamArray = $('#form-zkReportForms-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-zkReportForms-list').datagrid('load',searchParams);	
			},
		add:function(){
			$('#win-zeroKilometers-add').window({
				iconCls:'icon-add',
				title:'新增'
			});
			$('#form-zeroKilometers-add').form('clear');
			$('#id').val("0");
			$('#win-zeroKilometers-add').window('open');
		},
		update: function(){
			var selections = $('#datagrid-zeroKilometers-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			if(selections.length!=1){
				$.messager.alert('提示','只能选取一条记录！','info');
				return false;
			}
			if(selections[0]['status']==1)
			{
				$.messager.alert('提示','存在已经发布的信息！','info');
				return false;
			}
			$("#form-zeroKilometers-add").form('load',selections[0]);
			$('#id').val(selections[0]['id']);
			$("#vendorId").val(selections[0]['vendorId']);
			$("#materialId").val(selections[0]['materialId']);
			
			$("#vendorCode").textbox('setValue',selections[0].vendorBaseInfoEntity.code);
			$("#vendorName").textbox('setValue',selections[0].vendorBaseInfoEntity.name);

			$("#materialCode").textbox('setValue',selections[0].material.code);
			$("#materialName").textbox('setValue',selections[0].material.name);
			
			$('#win-zeroKilometers-add').window('open');
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
		
		saveAdd:function(){
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
			$('#form-zeroKilometers-add').form('submit',{
				url:ctx+'/manager/qualityassurance/zeroKilometers/saveAdd',
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
						$.messager.alert('提示',"新增成功",'info');
						$('#win-zeroKilometers-add').window('close');
						$('#datagrid-zeroKilometers-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg,'error');
					}
					}catch (e) {
						$.messager.alert('提示',data,'error');
					}
				}
			});		
		},
		
		publish:function(){
			var selections = $('#datagrid-zeroKilometers-list').datagrid('getSelections');
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
				url:ctx+'/manager/qualityassurance/zeroKilometers/publish',
				type:'POST',
				data:params,
				dataType:"json",
				contentType : 'application/json',
				success:function(data){					
					$('#win-zeroKilometers-publish').window('close');
					$('#datagrid-zeroKilometers-list').datagrid('reload');
				}   
			});
		},	
		
		import:function (){
			$('#win-impzk-import').window({
				iconCls : 'icon-disk_upload',
				title : '导入'
			});
			$('#form-impzk-import').form('clear');
			$('#win-impzk-import').window('open');
		},
		
		saveImport:function() {
			var impF=$('#impFile').val();
			if(impF == null || impF == ''){
				alert("请导入附件");
				return false;
			}
			$.messager.progress();
			$('#form-impzk-import').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/qualityassurance/zeroKilometers/filesUpload/', 
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
						$('#win-impzk-import').window('close');
						$('#datagrid-zeroKilometers-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});
		},
		
		reportForms:function(){
			$('#zkReportForms').window('open');
		},
		
		calcul : function(){
			$.messager.confirm('提示','确定计算吗？',function(r){
				if(r){
					$.ajax({
						url:ctx+'/manager/qualityassurance/zeroKilometers/calculate',
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
