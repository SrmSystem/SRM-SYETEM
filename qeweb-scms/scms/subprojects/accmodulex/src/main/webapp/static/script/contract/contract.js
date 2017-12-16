var contractStatus={
		
		getContractType :function(status) {
				if(status == 10)
					return '年度合同';
				else if(status == 20 )
					return '临时合同';
				else 
					return '';
			},
		 getAuditStatus :function(status) {
			if(status == 0)
				return '未审核';
			else if(status == 1)
				return '通过';
			else if(status == -1)
				return '驳回';
			else if(status == -8)
				return '审核中';
			else 
				return '';
		},
		getPublishStatus :function(status) {
				if(status == 0)
					return '未发布';
				else if(status == 1)
					return '已发布';
				else 
					return '';
		},
		 getConfirmStatus :function(status) {
				if(status == 0)
					return '未确认';
				else if(status == 1)
					return '已确认';
				else if(status == -1)
					return '已驳回';
				else 
					return '';
			},
			 getFileConfirmStatus :function(status) {
					if(status == 0)
						return '未确认';
					else if(status == 1)
						return '已确认';
					else if(status == -1)
						return '已退回';
					else 
						return '';
				},
				 getEffectiveStatus :function(status) {
						if(status == 0)
							return '未生效';
						else if(status == 1)
							return '已生效';
						else if(status == -1)
							return '已失效';
						else 
							return '';
					},
					getFileStatus :function(status) {
						if(status == 0)
							return '未上传';
						else if(status == 1)
							return '已上传';
						else 
							return '';
				}
}

var ContractManage = {

		 deleteContract:function(){
			var selections = $('#datagrid-contract-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			for(var i=0;i<selections.length;i++){
			
			}
			var params = $.toJSON(selections);
			$.messager.confirm('提示','确定要删除吗？',function(r){
				if(r){
					$.ajax({
						url:ctx+'/manager/contract/contract/deleteContract',
						type:'POST',
						data:params,
						contentType : 'application/json',
						dataType:"json",
						success:function(data){
							if(data.success){
								$.messager.alert('提示',"删除成功",'info');
							}else{
								$.messager.alert('提示',"删除失败",'error');
							}
							$('#datagrid-contract-list').datagrid('reload');
							
						},
						error:function(data) {
							$.messager.fail(data.responseText);
						}
					});
				}
			});
		},
		displayUploadConfirm : function (contractId){
			$('#win-filex-import').window('open');
			$('#cid').val(contractId);
		},
		 submitConfirmFile : function() {
			
			 if($('#file').val()==null){
				 $.messager.alert('提示','文件为空！','info');
			 }
			$.messager.progress();
			$('#form-filex-import').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/contract/contract/filesUpload/'+$('#cid').val(),
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
						$.messager.alert('提示','上传成功','info');
						$('#win-filex-import').window('close');
						 $('#dialog-contract').dialog('destroy');
						$('#datagrid-contract-list').datagrid('reload');
					}else{
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});
		},
		displayUploadSeal : function (contractId){
			$('#win-file-import').window('open');
			$('#id').val(contractId);
		},
		 submitSealFile : function() {
			 if($('#file').val()==null){
				 $.messager.alert('提示','文件为空！','info');
			 }
			$.messager.progress();
			$('#form-file-import').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/contract/vendorContract/filesUpload/'+$('#id').val(),
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
						$.messager.alert('提示','上传成功','info');
						$('#win-file-import').window('close');
						$('#datagrid-contract-list').datagrid('reload');
					}else{
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});
		},
		effectiveContract : function(contractId,confirmType){
			$.ajax({
				url:ctx+'/manager/contract/contract/'+confirmType+'/'+contractId,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:'消息',
							msg:'操作成功',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						 $('#dialog-contract').dialog('destroy');
						// window.location.reload();
						
						 $('#datagrid-contract-list').datagrid('reload'); 
					
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		},
		confirmContract : function(contractId,confirmType){
			$.ajax({
				url:ctx+'/manager/contract/vendorContract/'+confirmType+'/'+contractId,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:'消息',
							msg:'操作成功',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						 $('#dialog-contract').dialog('destroy');
						// window.location.reload();
						
						 $('#datagrid-contract-list').datagrid('reload'); 
					
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		},
		publishContract : function(contractId){
			$.ajax({
				url:ctx+'/manager/contract/contract/publishContract/'+contractId,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:'消息',
							msg:'发布成功',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						 $('#dialog-contract').dialog('destroy');
						// window.location.reload();
						
						 $('#datagrid-contract-list').datagrid('reload'); 
					
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		},
		checkContract : function(contractId,isPdf){
			
	/*		if(isPdf==null||isPdf==''){
				$.messager.alert('提示','请先预览合同生产PDF合同文件','info');
				return ;
			}*/
			$.messager.progress();

			$('#form-lingshi').form('submit',{
				ajax:true,
			
				url:ctx+'/manager/contract/contract/checkContract/'+contractId,
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){
					data = $.evalJSON(data);
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
					}else{
						$.messager.alert('提示',data.msg,'error');
					}
					$.messager.progress('close');
					
					 $('#dialog-contract').dialog('destroy');
					 $('#datagrid-contract-list').datagrid('reload'); 
					
				}
			});
		},
		downFile : function (fileName,fileUrl){
		
			var url = ctx+'/manager/contract/contract/downloadFile';
			var inputs = '<input type="hidden" name="fileUrl" value="'+fileUrl+'">'+'<input type="hidden" name="fileName" value="'+fileName+'">';
			
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
		    .appendTo('body').submit().remove();
		} ,
		downSealFile : function (contractId){
			
			var url = ctx+'/manager/contract/vendorContract/downloadSealFile';
			var inputs = '<input type="hidden" name="contractId" value="'+contractId+'">';
			
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
		    .appendTo('body').submit().remove();
		} ,
		downConfirmFile : function (contractId){
			
			var url = ctx+'/manager/contract/vendorContract/downloadConfirmFile';
			var inputs = '<input type="hidden" name="contractId" value="'+contractId+'">';
			
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
		    .appendTo('body').submit().remove();
		} ,
		
		//弹出选择框	
		openSelModuleWin : function(contractType) {
			
			$('#win-module-detail').dialog({maximizable:true});
			$('#win-module-detail').dialog('open'); 
			$('#datagrid-module-list').datagrid({   
		    	url: ctx + '/manager/contract/contract/getModuleList/'+contractType
			});
			$("#contractType").val(contractType);
		},
		searchModule: function() {
			var searchParamArray = $('#form-module-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-module-list').datagrid('load',searchParams);
		},
		//选择
		choiceModule : function() {
			 var clientWidth = document.body.clientWidth;	
			 var clientHeight = document.body.clientHeight;	
			var selections = $('#datagrid-module-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			$('#win-module-detail').dialog('close');
			 var title="合同新增";
			 new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contract/contract/toContractAdd/' + selections[0].id+','+$("#contractType").val(),'dialog-contract');
		},
		choiceNoModule : function(){
			 var clientWidth = document.body.clientWidth;	
			 var clientHeight = document.body.clientHeight;	
			$('#win-module-detail').dialog('close');
			 var title="合同新增";
			 new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contract/contract/toContractAdd/' + -100+','+$("#contractType").val(),'dialog-contract');
		},
		
		//弹出选择框	
		openSelMaterialWin : function() {
		
			//$('#win-material-detail').dialog({maximizable:true});
			$('#win-material-detail').dialog('open'); 
			$('#datagrid-material-list').datagrid({   
		    	url: ctx + '/manager/contract/contract/getMaterialList'
			});
		},
		searchMaterial: function() {
			var searchParamArray = $('#form-material-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-module-list').datagrid('load',searchParams);
		},
		//选择
		choiceMaterial : function() {
			var selections = $('#datagrid-material-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			$('#win-material-detail').dialog('close');
			
			var $dgSelected = $('#datagrid-contractItem-list');
			
			var datas=$dgSelected.datagrid('getData').rows;
			for(var i=0;i<selections.length;i++){
				var bool=true;
				for(var j=0;j<datas.length;j++){
					if(selections[i].id==datas[j].materialIdFk){
						bool=false;
						break;
					}
				}
				if(bool){
					var m = {};
				 	m.materialIdFk = selections[i].id;
				 	m.materialCode=selections[i].code;
				 	m.materialName=selections[i].name;
				 	m.itemQty=1;
				 	m.taxPrice=null;
				 	m.totalPrice=null;
				 	m.contractId=null;
					m.remarks=null;
					m.taxRate=null;
					m.attr_1=null;
					m.attr_2=null;
					m.attr_3=null;
					m.attr_4=null;
					m.attr_5=null;
					m.attr_6=null;
					m.attr_7=null;
					$dgSelected.datagrid('appendRow',m);
				}
			}
			

		},
		deleteMaterial : function(datagridId){
			var selectedArray = $(datagridId).datagrid('getSelections');
			if(selectedArray.length<=0){
				$.messager.alert('提示','请选择记录!','warning');
			}
			for(var i=0;i<selectedArray.length;i++){
				var index = $(datagridId).datagrid('getRowIndex',selectedArray[i]);
				$(datagridId).datagrid('deleteRow',index);
			}

		},
		//弹出选择框	
		openSelQuoWin : function() {
		if($("#vendorIdFk").val()==null||$("#vendorIdFk").val()==''){
			$.messager.alert('提示','请先选择供应商信息！','info');
			return false;
		}
			//$('#win-material-detail').dialog({maximizable:true});
			$('#win-quo-detail').dialog('open'); 
			$('#datagrid-quo-list').datagrid({   
		    	url: ctx + '/manager/contract/selEp/getList/'+$("#vendorIdFk").val()
			});
			
		},
		searchQuo: function() {
			var searchParamArray = $('#form-quo-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-quo-list').datagrid('load',searchParams);
		},
		//选择
		choiceQuo : function() {
			var selections = $('#datagrid-quo-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			$('#win-quo-detail').dialog('close');
			
			var $dgSelected = $('#datagrid-contractItem-list');
			
			var datas=$dgSelected.datagrid('getData').rows;
			for(var i=0;i<selections.length;i++){
				var bool=true;
				for(var j=0;j<datas.length;j++){
					if(selections[i].id==datas[j].materialIdFk){
						bool=false;
						break;
					}
				}
				if(bool){
					var m = {};
				 	m.materialIdFk = selections[i].epMaterial.materialId;
				 	m.materialCode=selections[i].epMaterial.materialCode;
				 	m.materialName=selections[i].epMaterial.materialName;
				 	m.itemQty=1;
				 	m.taxPrice=null;
				 	m.totalPrice=null;
				 	m.contractId=null;
					m.remarks=null;
					m.taxRate=null;
					m.attr_1=null;
					m.attr_2=null;
					m.attr_3=null;
					m.attr_4=null;
					m.attr_5=null;
					m.attr_6=null;
					m.sourceBillCode=selections[i].epPrice.enquirePriceCode;
					m.sourceItemPrice=selections[i].negotiatedPrice;
					m.sourceItemId=selections[i].id;
					
					$dgSelected.datagrid('appendRow',m);
				}
			}
			

		},
		saveContract : function(){
			var start=$('#effrctiveDateStartStr').datebox('getValue');
			var end=$('#effrctiveDateEndStr').datebox('getValue');
			if(start != "" && end != "" && end<=start){
				$.messager.progress('close');
				$.messager.alert('提示','有效期开始时间不能大于结束时间','info');
				return false;
			}
			var contractType=$("#contractType").val();
			$.messager.progress();
			var isValid = $('#form-contract-addoredit').form('validate');
			if(!isValid){
				$.messager.progress('close');
				$.messager.alert('提示','数据项不能为空','error');
				return false;
			}
			if(contractType==10){
				var effrctiveDateStartStr=$("#effrctiveDateStartStr").textbox('getValue');
				if(effrctiveDateStartStr==null){
					$.messager.alert('提示','有效期开始不能为空','error');
				}
				var effrctiveDateEndStr=$("#effrctiveDateEndStr").textbox('getValue');
				if(effrctiveDateEndStr==null){
					$.messager.alert('提示','有效期结束不能为空','error');
				}
			}
			
			/*if (endEditing()){
				$('#datagrid-dynamicItem-list').datagrid('acceptChanges');  
			}*/
		
			
			if($('#moduleId').val()>0){
				  $('#datagrid-contractItem-list').datagrid('acceptChanges'); 
				  
			    var rows = $('#datagrid-contractItem-list').datagrid('getRows');
			  
				if(rows == null || rows.length == 0) {
					if(contractType==20){
						$.messager.progress('close');
						$.messager.alert('提示','请添加合同明细','error');
						return false;
					}
			    } 
				
				
				
				var o =$('#datagrid-contractItem-list').datagrid('getData'); 
				var datas = JSON.stringify(o);   
				$("#tableData").val(datas);
			}
			
			
			$('#form-contract-addoredit').form('submit',{
				ajax:true,
				url:ctx+'/manager/contract/contract/saveContract/'+$('#moduleId').val(),  
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
							$.messager.alert('提示',"操作成功",'info');
							 $('#dialog-contract').dialog('destroy');
								// window.location.reload();
								
							 $('#datagrid-contract-list').datagrid('reload'); 
							
						}else{
							$.messager.alert('提示',result.msg,'error');
						}
						}catch (e) {
							$.messager.alert('提示',data,'error');
						}
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
				
				
			});
		}
		
		
	}
var contractUser={
		
		openWin : function(userType,roleType) {
			$("#userType").val(userType);
			$('#win-user-detail').dialog('open'); 
			$('#datagrid-user-list').datagrid({   
		    	//url: ctx + '/manager/contract/contract/getUserList'
				url: ctx + '/manager/contract/contract/getUserListByRoleType/'+roleType
			});
		},
		searchUser: function() {
			var searchParamArray = $('#form-user-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-user-list').datagrid('load',searchParams);
		},
		choice: function(){
			var selections = $('#datagrid-user-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var userType=$("#userType").val();
		
			var name = selections[0]["name"];
			var id = selections[0]["id"];
			if(1==userType){
				$("#signUserOne").val(id);
				$("#signUserOneName").textbox('setValue', name);
			}else if(2==userType){
				$("#signUserTwo").val(id);
				$("#signUserTwoName").textbox('setValue', name);
			}else if(3==userType){
				$("#signUserThree").val(id);
				$("#signUserThreeName").textbox('setValue', name);
			}else if(4==userType){
				$("#applyUser").val(id);
				$("#applyUserName").textbox('setValue', name);
			}
			$('#win-user-detail').dialog('close');	
		}
		
}

var contractVendor={
		
		openWin : function() {
			
			$.parser.parse($('#win-proVendor-detail'));
			$('#link-vendorSel-choice').linkbutton();
			$('#link-vendorSel-search').linkbutton();
			$('#win-proVendor-detail').dialog('open'); 
			$('#datagrid-proVendor-list').datagrid({   
		    	url: ctx + '/manager/contract/contract/getOrgList'
			});
		},
		searchVendor: function() {
			var searchParamArray = $('#form-proVendor-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-proVendor-list').datagrid('load',searchParams);
		},
		choice: function(){
			var selections = $('#datagrid-proVendor-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
		
			var p = selections[0]["name"];
			var pv = selections[0]["id"];
			$("#vendorIdFk").val(pv);
			$("#vendorName").textbox('setValue', p);
			
			$('#win-proVendor-detail').dialog('close');	
		}
		
}