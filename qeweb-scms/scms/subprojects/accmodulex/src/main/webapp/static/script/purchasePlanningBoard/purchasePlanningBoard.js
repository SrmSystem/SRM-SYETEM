var editIndexSys = undefined;
var purchasePlanningBoard ={
	search : function(){
		var searchParamArray = $('#form-ppb-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#purchasePlanningBoardDatagrId').datagrid('load',searchParams);
	},
	
	//导出
	exportPPB:function(){
		var searchParamArray = $('#form-ppb-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
            $.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.purchasePlanningBoardJs.confirmExport')/*'确认导出吗?'*/,function(result){
                if(result){
                    $.ajax({ 
                    	url : ctx+'/manager/order/purchasePlanningBoard/exportExcel', 
                        type : 'post', 
                        dataType:"json",
                        contentType : 'application/json',
                        data : JSON.stringify(searchParams), 
                        success : function(data){ 
                            if(!data.code) {
                                $.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data.msg,'info');
                                return false;
                            }else{
                                if(data.fileName == null){
                                    $.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.purchasePlanningBoardJs.exportFailure')/*'导出失败'*/,'info');
                                }else{
                                    window.location.href=encodeURI(ctx+'/manager/order/purchasePlanningBoard/downloadExcel?fileName='+data.fileName);
                                }
                            }
                        } 
                    });
                }
            });
            
    },
	
	//同步采购计划看板
	synPurchasePlanningBoard : function() {
		var factoryCode = $("#factoryCode").val();
		if(factoryCode==null || factoryCode=='' || factoryCode==undefined){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.purchasePlanningBoardJs.factoryEmpty')/*'工厂编码不能为空'*/,'info');
			return;
		}
		var materialCode = $("#materialCode").val();
		if(materialCode==null || materialCode=='' || materialCode==undefined){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.purchasePlanningBoardJs.materialNull')/*'物料编码不能为空'*/,'info');
			return;
		}
		$.messager.progress();
		$.ajax({
         	url:ctx+'/manager/order/purchasePlanningBoard/synPurchasePlanningBoard',
         		type:'POST',
         		dataType:"json",
         		contentType : 'application/json',
         		data : JSON.stringify({'factoryCode':factoryCode,'materialCode':materialCode}), 
         		success:function(data){
         			$.messager.progress('close');
         			if(data.success){ 
     					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.purchasePlanningBoardJs.synchronousSuccessful')/*'同步采购计划看板成功'*/,'info');
     					$('#form-ppb-search').datagrid('reload'); 
     				}else{
     					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data.message,'error');
     				}
         		}
         	});
	},
    sysOpenMaterialPPB:function(){
    	$('#win-sysMaterialPPB-detail').window('open');
    	//清除当前列表缓存
    	//清除所有的行项目
    	 $('#datagrid-sysMaterialPPB-list').datagrid('loadData', { total: 0, rows: [] });  
    	 var rows = $('#datagrid-sysMaterialPPB-list').datagrid("getRows").length;
    	 if(rows < 5){
    		for(var i = 0 ;i<=5 ;i++){
    			purchasePlanningBoard.insertSys();
    		}
    	}
    	
    	 $('#datagrid-sysMaterialPPB-list').datagrid('clearSelections');
		
	},
	 insertSys:function(){
		if (purchasePlanningBoard.endEditingSys()){
			$('#datagrid-sysMaterialPPB-list').datagrid('appendRow',{code:''});
			editIndexSys = $('#datagrid-sysMaterialPPB-list').datagrid('getRows').length-1;
			$('#datagrid-sysMaterialPPB-list').datagrid('selectRow', editIndexSys)
					.datagrid('beginEdit', editIndexSys);
		}
		
	},
	endEditingSys:function (){
		if (editIndexSys == undefined){return true}
		if ($('#datagrid-sysMaterialPPB-list').datagrid('validateRow', editIndexSys)){
			var ed = $('#datagrid-sysMaterialPPB-list').datagrid('getEditor', {index:editIndexSys,field:'code'});
			$('#datagrid-sysMaterialPPB-list').datagrid('beginEdit', editIndexSys);
			editIndexSys = undefined;
			return true;
		} else {
			return false;
		}
	},
	onClickRowSys:function(index){
		if (editIndexSys != index){
			if (purchasePlanningBoard.endEditingSys()){
				$('#datagrid-sysMaterialPPB-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndexSys = index;
			} else {
				$('#datagrid-sysMaterialPPB-list').datagrid('selectRow', editIndexSys);
			}
		}
	},
	removeitSys:function(){
		if (editIndexSys == undefined){return}
		$('#datagrid-sysMaterialPPB-list').datagrid('cancelEdit', editIndexSys)
				.datagrid('deleteRow', editIndexSys);
		editIndexSys = undefined;
	},
	resetMaterialSys:function(){
		//清除所有的行项目
		$('#datagrid-sysMaterialPPB-list').datagrid('loadData', { total: 0, rows: [] }); 
		editIndexSys = undefined;
	},
	addMaterialSys:function(){
		var arr = handlMaterial();
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-sysMaterialPPB-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (purchasePlanningBoard.endEditingSys()){
					$('#datagrid-sysMaterialPPB-list').datagrid('appendRow',{code:arr[i]});
					editIndexSys = $('#datagrid-sysMaterialPPB-list').datagrid('getRows').length-1;
					$('#datagrid-sysMaterialPPB-list').datagrid('selectRow', editIndexSys)
							.datagrid('beginEdit', editIndexSys);
				}
			}
		}
	},
	/* 选择带会物料 */
	sysChoiceMaterial:function() {
		purchasePlanningBoard.acceptSys();
		var selections = $('#datagrid-sysMaterialPPB-list').datagrid('getRows');
		var codes = "";
		for(var i=0;i<selections.length;i++){
			if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
				codes += selections[i].code+",";
			}
		}
		$("#materialCode").textbox("setValue",codes);
		$('#win-sysMaterialPPB-detail').window('close');	
	},
	acceptSys:function(){
		if (purchasePlanningBoard.endEditingSys()){
			$('#datagrid-sysMaterialPPB-list').datagrid('acceptChanges');
		}
	}
	
}