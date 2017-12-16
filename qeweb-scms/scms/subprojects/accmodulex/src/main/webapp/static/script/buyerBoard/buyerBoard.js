var editIndexSys = undefined;
var buyerBoard ={
	search : function(){
		var searchParamArray = $('#form-bb-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#buyerBoardDatagrId').datagrid('load',searchParams);
	},
	
	//导出
	exportBP:function(){
        var searchParamArray = $('#form-bb-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
            $.messager.confirm('提示','确认导出吗?',function(result){
                if(result){
                    $.ajax({ 
                    	url : ctx+'/manager/order/buyerBoard/exportExcel', 
                        type : 'post', 
                        dataType:"json",
                        contentType : 'application/json',
                        data : JSON.stringify(searchParams), 
                        success : function(data){ 
                            if(!data.code) {
                                $.messager.alert('提示',data.msg,'info');
                                return false;
                            }else{
                                if(data.fileName == null){
                                    $.messager.alert('提示','导出失败','info');
                                }else{
                                    window.location.href=encodeURI(ctx+'/manager/order/buyerBoard/downloadExcel?fileName='+ data.fileName);
                                }
                            }
                        } 
                    });
                }
            });
            
    },
	//同步采购员看板
	synBuyerBoard : function() {
		var factoryCode = $("#factoryCode").val();
		if(factoryCode==null || factoryCode=='' || factoryCode==undefined){
			$.messager.alert('提示','工厂编码不能为空','info');
			return;
		}
		var materialCode = $("#materialCode").val();
		if(materialCode==null || materialCode=='' || materialCode==undefined){
			$.messager.alert('提示','物料编码不能为空','info');
			return;
		}
		$.messager.progress();
		$.ajax({
         	url:ctx+'/manager/order/buyerBoard/synBuyerBoard',
         		type:'POST',
         		dataType:"json",
         		contentType : 'application/json',
         		data : JSON.stringify({'factoryCode':factoryCode,'materialCode':materialCode}), 
         		success:function(data){
         			$.messager.progress('close');
         			if(data.success){ 
     					$.messager.alert('提示','同步采购员看板成功','info');
     					$('#form-bb-search').datagrid('reload'); 
     				}else{
     					$.messager.alert('提示',data.message,'error');
     				} 
         		}
         	});
	},
    sysOpenMaterialBB:function(){
    	$('#win-sysMaterialBB-detail').window('open');
    	//清除当前列表缓存
    	//清除所有的行项目
    	 $('#datagrid-sysMaterialBB-list').datagrid('loadData', { total: 0, rows: [] });  
    	 var rows = $('#datagrid-sysMaterialBB-list').datagrid("getRows").length;
    	 if(rows < 5){
    		for(var i = 0 ;i<=5 ;i++){
    			buyerBoard.insertSys();
    		}
    	}
    	
    	 $('#datagrid-sysMaterialBB-list').datagrid('clearSelections');
		
	},
	 insertSys:function(){
		if (buyerBoard.endEditingSys()){
			$('#datagrid-sysMaterialBB-list').datagrid('appendRow',{code:''});
			editIndexSys = $('#datagrid-sysMaterialBB-list').datagrid('getRows').length-1;
			$('#datagrid-sysMaterialBB-list').datagrid('selectRow', editIndexSys)
					.datagrid('beginEdit', editIndexSys);
		}
		
	},
	endEditingSys:function (){
		if (editIndexSys == undefined){return true}
		if ($('#datagrid-sysMaterialBB-list').datagrid('validateRow', editIndexSys)){
			var ed = $('#datagrid-sysMaterialBB-list').datagrid('getEditor', {index:editIndexSys,field:'code'});
			$('#datagrid-sysMaterialBB-list').datagrid('beginEdit', editIndexSys);
			editIndexSys = undefined;
			return true;
		} else {
			return false;
		}
	},
	onClickRowSys:function(index){
		if (editIndexSys != index){
			if (buyerBoard.endEditingSys()){
				$('#datagrid-sysMaterialBB-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndexSys = index;
			} else {
				$('#datagrid-sysMaterialBB-list').datagrid('selectRow', editIndexSys);
			}
		}
	},
	removeitSys:function(){
		if (editIndexSys == undefined){return}
		$('#datagrid-sysMaterialBB-list').datagrid('cancelEdit', editIndexSys)
				.datagrid('deleteRow', editIndexSys);
		editIndexSys = undefined;
	},
	resetMaterialSys:function(){
		//清除所有的行项目
		$('#datagrid-sysMaterialBB-list').datagrid('loadData', { total: 0, rows: [] }); 
		editIndexSys = undefined;
	},
	addMaterialSys:function(){
		var arr = handlMaterial();
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-sysMaterialBB-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (buyerBoard.endEditingSys()){
					$('#datagrid-sysMaterialBB-list').datagrid('appendRow',{code:arr[i]});
					editIndexSys = $('#datagrid-sysMaterialBB-list').datagrid('getRows').length-1;
					$('#datagrid-sysMaterialBB-list').datagrid('selectRow', editIndexSys)
							.datagrid('beginEdit', editIndexSys);
				}
			}
		}
	},
	/* 选择带会物料 */
	sysChoiceMaterial:function() {
		buyerBoard.acceptSys();
		var selections = $('#datagrid-sysMaterialBB-list').datagrid('getRows');
		var codes = "";
		for(var i=0;i<selections.length;i++){
			if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
				codes += selections[i].code+",";
			}
		}
		$("#materialCode").textbox("setValue",codes);
		$('#win-sysMaterialBB-detail').window('close');	
	},
	acceptSys:function(){
		if (buyerBoard.endEditingSys()){
			$('#datagrid-sysMaterialBB-list').datagrid('acceptChanges');
		}
	}
	
}