
var buyerVendorRel = {
		//弹出选择框	
		openWin : function() {
			$('#win-vendor-detail').dialog(); 
			$('#win-vendor-detail').dialog('open'); 
			$('#datagrid-vendor-list').datagrid({   
		    	url: ctx + '/manager/vendor/buyerVendorRel/getVendorList'
			});
		},
		//选择
		choice : function(buyerId) {
			var selections = $('#datagrid-vendor-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
				return false;
			}
			
			//采购组织菜单，选择供应商，对于重复的数据增加提示
			var rows = $('#datagrid-buyerVendorRel-list').datagrid('getRows');
			for(var i=0;i<rows.length;i++){
				for(var j=0;j<selections.length;j++){
					if(rows[i].vendor.code == selections[j].code && rows[i].vendor.name == selections[j].name){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.vendorJs.selectedAgain')/*'存在已选择的数据，请重新选择！'*/,'info');
						return false;
					}
				}
			}
		 	

			$.messager.progress();
		 	var params = $.toJSON(selections);
         	$.ajax({
         	url:ctx+'/manager/vendor/buyerVendorRel/selVendor/'+buyerId,
         		type:'POST',
         		data:params,
         		dataType:"json",
         		contentType : 'application/json',
         		success:function(data){
         			$.messager.progress('close');
         			try{
         				if(data.success){ 
         					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.vendorJs.selectSuccess')/*'选中成功'*/,'info');
         					$('#win-vendor-detail').window('close');	
         					$('#datagrid-buyerVendorRel-list').datagrid('reload'); 
         				}else{
         					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data.message,'error');
         				}
         			}catch (e) {
         				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,e,'error'); 
         			} 
         		}
         	});
			
		},
		delRel : function(){
			var selections = $('#datagrid-buyerVendorRel-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/vendor/buyerVendorRel/delBuyerVendorRelList',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('vendor.settingJs.DeleteSucessful')/*'删除成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$('#datagrid-buyerVendorRel-list').datagrid('reload');
					
				}
			});
		},
		openMaterialWin : function() {
			$('#win-material-detail').dialog(); 
			$('#win-material-detail').dialog('open'); 
			$('#datagrid-material-list').datagrid({   
		    	url: ctx + '/manager/vendor/buyerVendorRel/getMaterialList'
			});
		},
		//选择
		choiceMaterial : function(buyerId) {
			
			var selections = $('#datagrid-material-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
				return false;
			}
		 	
			//采购组织菜单，选择物料，对于重复的数据增加提示
			var rows = $('#datagrid-buyerMaterialRel-list').datagrid('getRows');
			for(var i=0;i<rows.length;i++){
				for(var j=0;j<selections.length;j++){
					if(rows[i].material.code == selections[j].code && rows[i].material.name == selections[j].name){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.vendorJs.selectedAgain')/*'存在已选择的数据，请重新选择！'*/,'info');
						return false;
					}
				}
			}
			

			$.messager.progress();
		 	var params = $.toJSON(selections);
         	$.ajax({
         	url:ctx+'/manager/vendor/buyerVendorRel/selMaterial/'+buyerId,
         		type:'POST',
         		data:params,
         		dataType:"json",
         		contentType : 'application/json',
         		success:function(data){
         			$.messager.progress('close');
         			try{
         				if(data.success){ 
         					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.vendorJs.selectSuccess')/*'选中成功'*/,'info');
         					$('#win-material-detail').window('close');	
         					$('#datagrid-buyerMaterialRel-list').datagrid('reload'); 
         				}else{
         					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data.message,'error');
         				}
         			}catch (e) {
         				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,e,'error'); 
         			} 
         		}
         	});
			
		},
		delMaterialRel : function(){
			var selections = $('#datagrid-buyerMaterialRel-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/vendor/buyerVendorRel/delBuyerMaterialRelList',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('vendor.settingJs.DeleteSucessful')/*'删除成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$('#datagrid-buyerMaterialRel-list').datagrid('reload');
					
				}
			});
		},
		openMaterialTypeWin : function() {
			$('#win-materialType-detail').dialog(); 
			$('#win-materialType-detail').dialog('open'); 
			$('#datagrid-materialType-list').datagrid({   
		    	url: ctx + '/manager/vendor/buyerVendorRel/getMaterialTypeList'
			});
		},
		//选择
		choiceMaterialType : function(buyerId) {
			
			var selections = $('#datagrid-materialType-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
				return false;
			}
			
			//采购组织菜单，选择物料分类，对于重复的数据增加提示
			var rows = $('#datagrid-buyerMaterialTypeRel-list').datagrid('getRows');
			for(var i=0;i<rows.length;i++){
				for(var j=0;j<selections.length;j++){
					if(rows[i].materialType.code == selections[j].code && rows[i].materialType.name == selections[j].name){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.vendorJs.selectedAgain')/*'存在已选择的数据，请重新选择！'*/,'info');
						return false;
					}
				}
			}			
			
		 	

			$.messager.progress();
		 	var params = $.toJSON(selections);
         	$.ajax({
         	url:ctx+'/manager/vendor/buyerVendorRel/selMaterialType/'+buyerId,
         		type:'POST',
         		data:params,
         		dataType:"json",
         		contentType : 'application/json',
         		success:function(data){
         			$.messager.progress('close');
         			try{
         				if(data.success){ 
         					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.vendorJs.selectSuccess')/*'选中成功'*/,'info');
         					$('#win-materialType-detail').window('close');	
         					$('#datagrid-buyerMaterialTypeRel-list').datagrid('reload'); 
         				}else{
         					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data.message,'error');
         				}
         			}catch (e) {
         				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,e,'error'); 
         			} 
         		}
         	});
			
		},
		delMaterialTypeRel : function(){
			var selections = $('#datagrid-buyerMaterialTypeRel-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/vendor/buyerVendorRel/delBuyerMaterialTypeRelList',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('vendor.settingJs.DeleteSucessful')/*'删除成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$('#datagrid-buyerMaterialTypeRel-list').datagrid('reload');
					
				}
			});
		}
	}

