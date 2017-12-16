var VendorMaterialRel = {
	query : function() {
		$("#matRelExpBtn").linkbutton('enable');
		var searchParamArray = $('#form-materialRel-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-materialRel-list').datagrid('load', searchParams);
	},

	add : function() {
		var $dg = $('#dialog-editor');
		$dg.dialog({
			iconCls : 'icon-application',
			title : '维护供货关系'
		});
		$dg.dialog('autoSizeMax');
		$dg.dialog('open');
	},
	imp:function (){
		$('#win-materialRel-import').window({
			iconCls : 'icon-disk_upload',
			title : '导入供货关系'
		});
		$('#form-materialRel-import').form('clear');
		$('#win-materialRel-import').window('open');
	},
	saveImp:function() {
		$.messager.progress();
		$('#form-materialRel-import').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/vendor/materialRel/filesUpload/', 
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
					$('#win-materialRel-import').window('close');
					$('#datagrid-materialRel-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
				}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	submit : function() {
		var $matSelected = $('#dialog-editor-mat-selected');
		var $venSelected = $('#dialog-editor-ven-selected');
		var $buyerSelected = $('#dialog-editor-buyer-selected');
		$matSelected.datagrid('acceptChanges');
		var supMatRowList = $matSelected.datagrid('getRows');
		var vendorList = $venSelected.datagrid('getRows');
		var buyerList = $buyerSelected.datagrid('getRows');
		if(supMatRowList==null || supMatRowList.length<=0){
			 $.messager.alert('提示','请选择供货物料','warning');
			 return;
		}
		if(vendorList==null || vendorList.length<=0){
			$.messager.alert('提示','请选择供应商','warning');
			return;
		}
		if(buyerList==null || buyerList.length<=0){
			$.messager.alert('提示','请选择采购组织','warning');
			return;
		}
	    var venMatRelList = [];
	    $.each(buyerList,function(i,buyer){
		    $.each(vendorList,function(i,vendor){
		    	$.each(supMatRowList,function(j,mat){
					 var venMatRel = {buyerId:buyer.id,orgId:vendor.orgId,vendorId:vendor.id,vendorName:vendor.name,materialId:mat.id,materialName:mat.name,dataFrom:mat.dataFrom,status:1};
					 venMatRelList.push(venMatRel);
				 });
		    });
	    });
		var url = ctx + '/manager/vendor/materialRel/addNewVendorMaterialRel';
		var sucMeg = '维护供货关系成功！';
//		if ($('#id').val() != 0 && $('#id').val() != '0') {
//			url = ctx + '/manager/vendor/materialRel/update';
//			sucMeg = '编辑供货关系成功！';
//		}
		$.messager.progress();
		$.ajax({
			url : url,
			data : $.toJSON(venMatRelList),
			type : 'post',
			contentType : 'application/json',
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$('#dialog-editor').dialog('close');
					$('#datagrid-materialRel-list').datagrid('reload');
					$.messager.success(sucMeg);
				}else{
					$.messager.fail(data.msg);
				}
			}
			
		});
	},

	del : function() {
		var selections = $('#datagrid-materialRel-list').datagrid(
				'getSelections');
		if (selections.length == 0) {
			$.messager.alert('提示', '没有选择任何记录！', 'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.ajax({
			url : ctx + '/manager/vendor/materialRel/deleteVendorMaterialRel',
			type : 'POST',
			data : params,
			contentType : 'application/json',
			success : function(data) {

				$.messager.show({
					title : '消息',
					msg : '删除供货关系成功',
					timeout : 2000,
					showType : 'show',
					style : {
						right : '',
						top : document.body.scrollTop
								+ document.documentElement.scrollTop,
						bottom : ''
					}
				});

				$('#datagrid-materialRel-list').datagrid('reload');

			}
		});
	},

	edit : function(id) {
		$('#win-materialRel-addoredit').window({
			iconCls : 'icon-edit',
			title : '编辑供货关系'
		});
		$('#vendorName').textbox('disable');
		$('#win-materialRel-addoredit').window('open');
		$('#form-materialRel-addoredit').form('load',
				ctx + '/manager/vendor/materialRel/getVendorMaterialRel/' + id);
	},

	stop : function(id) {
				$.ajax({
					url : ctx + '/manager/vendor/materialRel/changeStatus/'
							+ id + '/0',
					type : 'POST',
					contentType : 'application/json',
					success : function(data) {
						$.messager.show({
							title : '消息',
							msg : '操作成功',
							timeout : 2000,
							showType : 'show',
							style : {
								right : '',
								top : document.body.scrollTop
										+ document.documentElement.scrollTop,
								bottom : ''
							}
						});

						$('#datagrid-materialRel-list').datagrid('reload');

					}
				});
	},

	recovery : function(id) {
				$.ajax({
					url : ctx + '/manager/vendor/materialRel/changeStatus/'
							+ id + '/1',
					type : 'POST',
					contentType : 'application/json',
					success : function(data) {
						$.messager.show({
							title : '消息',
							msg : '操作成功',
							timeout : 2000,
							showType : 'show',
							style : {
								right : '',
								top : document.body.scrollTop
										+ document.documentElement.scrollTop,
								bottom : ''
							}
						});

						$('#datagrid-materialRel-list').datagrid('reload');

					}
				});
	}

};

var VendorMaterialSupplyRel ={

		/*
		 * 新增供货关系弹窗
		 */
		add:function (){
			$('#win-materialSupplyRel-addoredit').window({
				iconCls:'icon-add',
				title:'新增供货关系详情'
			});
			$('#vendorName').textbox('enable');
			$('#form-materialSupplyRel-addoredit').form('clear');
			$('#supplyId').val(0);
			$('#materialRelId').val($('#hiddenMaterialRelId').val());
			$('#win-materialSupplyRel-addoredit').window('open');
		},
		/*
		 * 保存新供货关系详情
		 */
		submit:function (){
			var arr = $('#datagrid-materialSupplyRel-list').datagrid('getData');
			var tota = 0;
			tota = parseFloat(arr["totalSuCoe"]);
			tota += parseFloat($('#supplyCoefficient').val());
			if(tota>1 || tota <0){
				$.messager.alert('提示',"供货系数总和需大于0且不大于1",'info');
				return false;
			}
			var url = ctx+'/manager/vendor/materialSupplyRel/addNewVendorMaterialSupplyRel';
			var sucMeg = '添加供货关系详情成功！';
			if($('#supplyId').val()!=0 && $('#supplyId').val()!='0'){
				url = ctx+'/manager/vendor/materialSupplyRel/update';
				sucMeg = '编辑供货关系详情成功！';
			}
			$.messager.progress();
			$('#form-materialSupplyRel-addoredit').form('submit',{
				ajax:true,
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
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert('提示',sucMeg,'info');
						$('#win-materialSupplyRel-addoredit').window('close');
						$('#datagrid-materialSupplyRel-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg,'error');
					}
					}catch (e) {
						$.messager.alert('提示',data,'error');
					}
				}
				
			});
		},
		/*
		 * 删除供货关系详情
		 */
		del:function (){
			var selections = $('#datagrid-materialSupplyRel-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/vendor/materialSupplyRel/deleteVendorMaterialSupplyRel',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					$.messager.progress('close');
					$.messager.alert('提示',"删除供货关系成功",'info');
					$('#datagrid-materialSupplyRel-list').datagrid('reload');
				}
			});
		},

		viewDetails:function (id){
			$('#hiddenMaterialRelId').val(id);
			$('#win-materialSupplyRel').window({
				title:'供货关系详情'
			});
			$('#win-materialSupplyRel').window('open');
			$('#datagrid-materialSupplyRel-list').datagrid('reload',ctx+'/manager/vendor/materialSupplyRel/getMaterialSupplyRelList/'+id);
		},
		imp:function (){
			$('#win-materialSupplyRel-import').window({
				iconCls : 'icon-disk_upload',
				title : '导入供货系数'
			});
			$('#form-materialSupplyRel-import').form('clear');
			$('#win-materialSupplyRel-import').window('open');
		},
		saveImp:function() {
			var materialRelId = $('#hiddenMaterialRelId').val();
			$.messager.progress();
			$('#form-materialSupplyRel-import').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/vendor/materialSupplyRel/filesUpload/'+materialRelId, 
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
						$.messager.alert('提示',result.msg,'info');
						$('#win-materialSupplyRel-import').window('close');
						$('#datagrid-materialSupplyRel-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});
		},
		exp:function(){
			var materialRelId = $('#hiddenMaterialRelId').val();
			
			$.ajax({
				url:ctx+'/manager/vendor/materialSupplyRel/exportExcel/'+materialRelId, 
				type:'POST',
				contentType : 'application/json',
				success:function(data){}
			});
			/*
			//导出
			$('#form-materialSupplyRel-export').form('submit',{
				url:ctx+'/manager/vendor/materialSupplyRel/exportExcel/'+materialRelId, 
				success:function(data){
					$.messager.progress('close');
				}
			});
			*/
		}
};

VendorMaterialRel.Material = {
		search : function(){
			var searchParamArray = $('#form-supply-material-select').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#dialog-editor-mat-select').datagrid('load',searchParams);
		},
		select : function(){
			var $dgSelect = $('#dialog-editor-mat-select');
			var $dgSelected = $('#dialog-editor-mat-selected');
			var selectedArray = $dgSelect.datagrid('getSelections');
			if(selectedArray==null || selectedArray.length<=0){
				$.messager.alert('提示','请选择物料!','warning');
				return;
			}
			$.each(selectedArray,function(i,n){
				if($dgSelected.datagrid('getRowIndex',n.id)==-1){
					$dgSelected.datagrid('appendRow',n);
				}
				
			});
			
		},
		removeSelected : function(datagridId){
			var selectedArray = $(datagridId).datagrid('getSelections');
			if(selectedArray.length<=0){
				$.messager.alert('提示','请选择物料!','warning');
			}
			$.each(selectedArray,function(i,n){
				selectedArray = $(datagridId).datagrid('getSelections');
				var index = $(datagridId).datagrid('getRowIndex',n.id);
				$(datagridId).datagrid('deleteRow',index);
				
			});
		}
	}; 
VendorMaterialRel.Vendor = {
		search : function(){
			var searchParamArray = $('#form-supply-vendor-select').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#dialog-editor-ven-select').datagrid('load',searchParams);
		},
		select : function(){
			var $dgSelect = $('#dialog-editor-ven-select');
			var $dgSelected = $('#dialog-editor-ven-selected');
			var selectedArray = $dgSelect.datagrid('getSelections');
			if(selectedArray==null || selectedArray.length<=0){
				$.messager.alert('提示','请选择供应商!','warning');
				return;
			}
			$.each(selectedArray,function(i,n){
				if($dgSelected.datagrid('getRowIndex',n.id)==-1){
					$dgSelected.datagrid('appendRow',n);
				}
				
			});
			
		},
		removeSelected : function(datagridId){
			var selectedArray = $(datagridId).datagrid('getSelections');
			if(selectedArray.length<=0){
				$.messager.alert('提示','请选择供应商!','warning');
			}
			$.each(selectedArray,function(i,n){
				selectedArray = $(datagridId).datagrid('getSelections');
				var index = $(datagridId).datagrid('getRowIndex',n.id);
				$(datagridId).datagrid('deleteRow',index);
				
			});
		}
}; 

VendorMaterialRel.Buyer = {
		search : function(){
			var searchParamArray = $('#form-supply-buyer-select').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#dialog-editor-buyer-select').datagrid('load',searchParams);
		},
		select : function(){
			var $dgSelect = $('#dialog-editor-buyer-select');
			var $dgSelected = $('#dialog-editor-buyer-selected');
			var selectedArray = $dgSelect.datagrid('getSelections');
			if(selectedArray==null || selectedArray.length<=0){
				$.messager.alert('提示','请选择采购组织!','warning');
				return;
			}
			$.each(selectedArray,function(i,n){
				if($dgSelected.datagrid('getRowIndex',n.id)==-1){
					$dgSelected.datagrid('appendRow',n);
				}
				
			});
			
		},
		removeSelected : function(datagridId){
			var selectedArray = $(datagridId).datagrid('getSelections');
			if(selectedArray.length<=0){
				$.messager.alert('提示','请选择采购组织!','warning');
			}
			$.each(selectedArray,function(i,n){
				selectedArray = $(datagridId).datagrid('getSelections');
				var index = $(datagridId).datagrid('getRowIndex',n.id);
				$(datagridId).datagrid('deleteRow',index);
				
			});
		}
}; 