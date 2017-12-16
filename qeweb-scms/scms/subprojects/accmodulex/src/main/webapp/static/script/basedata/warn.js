//combox change事件
window.onload = function(){  
	$('#isWarning').combobox({    
	    onChange : function(newData,old){
	    	if(newData == 0){
	    		//隐藏预警内容
	    		document.getElementById('show').style.display = 'none';
	    		document.getElementById('warnContent').value = '';
	    		$('#warnContent').validatebox({ required:false });
	    	}else if(newData == 1){
	    		//显示预警内容
	    		document.getElementById('show').style.display = '';
	    		$('#warnContent').validatebox({ required:true });

	    	}
	    }  
	});
}



function operateFmt(v,r,i){
	var edit = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editWarnInfo('+r.id+')">'+$.i18n.prop('vendor.basedataJs.Edit')+'</a>';/*编辑*/
	var unable = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="unable('+r.enableStatus+','+r.id+')">'+$.i18n.prop('vendor.basedataJs.Disable')+'</a>'/*禁用*/
	var able ='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="unable('+r.enableStatus+','+r.id+')">'+$.i18n.prop('vendor.basedataJs.Able')+'</a>';/*启用*/
	if(r.isVendor == 0){
	edit += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="warnMessageDetail('+r.id+')">'+$.i18n.prop('vendor.basedataJs.PromotionReminder')+'</a>';/*晋级提醒*/
	}
	
	return r.enableStatus == 1? unable + edit : able;
}

function isVendorFmt(v,r,i){
	var isVendor = r.isVendor;
	if(isVendor == 1){
		return $.i18n.prop('vendor.basedataJs.Supplier');/*'供应方'*/
	}else{
		return $.i18n.prop('vendor.basedataJs.Purchaser');/*'采购方'*/
	}
}

function enableStatusFmt(v,r,i){
	var enableStatus = r.enableStatus;
	if(enableStatus == 0){
		return $.i18n.prop('vendor.basedataJs.Invalid');/*'失效'*/
	}else{
		return $.i18n.prop('vendor.basedataJs.TakeEffect');/*'生效'*/
	}
}

function isWarningFmt(v,r,i){
	var isWarning = r.isWarning;
	if(isWarning == 0){
		return $.i18n.prop('vendor.basedataJs.NO');/*'否'*/
	}else{
		return $.i18n.prop('vendor.basedataJs.YES');/*'是'*/
	}
}

function isMailFmt(v,r,i){
	var isMail = r.isMail;
	if(isMail==0){
		return $.i18n.prop('vendor.basedataJs.NO');/*'否'*/
	}else{
		return $.i18n.prop('vendor.basedataJs.YES');/*'是'*/
	}
}
function resetInfo(){
	$("#isMail").combobox("setValue","");
	$("#isWarning").combobox("setValue","");
	document.getElementById('content').value = '';
	document.getElementById('warnContent').value = '';
}


function unable(enableStatus,id){
	if(enableStatus ==null){
		enableStatus = 0;
	}
//	$.messager.confirm('提示','确定执行此操作！',function(r){
	$.messager.confirm($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.MakeSureOperation'),function(r){
		if(r){
		$.ajax({
			url:ctx+'/manager/common/warning/editEnableStatus/'+enableStatus+'/'+id,
			type:'POST',
			
			success:function(data){
				$("#datagrid-warn-list").datagrid('reload');
				
			}
			
		})
		}
	})
	
	
}


function searchWarn(){
	
	var searchParamArray = $('#form-warn-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-warn-list').datagrid('load',searchParams);
}

function saveWarnMain(){
	
	$.ajax({
		url:"${ctx}/manager/common/warning/save",
		type:'POST',
		success:function(data){
//			$.messager.alert('提示','保存成功','info');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.SaveSuccess'),'info');
		}
	});
}

function editWarnInfo(id){
	roleSelect();
	$('#win-promotion-edit').dialog({
		iconCls:'icon-edit',
		title:$.i18n.prop('vendor.basedataJs.EditPrePareInfo')/*'编辑预警信息'*/
	});
	
	$('#win-warn-edit').dialog('open');
	 $('#name').textbox({disabled:true})
	 $('#isVendorName').textbox({disabled:true})
	$('#form-warn-edit').form('load',ctx+'/manager/common/warning/getMain/'+id);
	
}
function submitEditWarnInfo(){
	var sucMeg = $.i18n.prop('vendor.basedataJs.Complent');/*'完成'*/
	$.messager.progress({
		title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
		msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
	});
	$("#form-warn-edit").form('submit',{
		ajax:true,
		
		url:ctx+'/manager/common/warning/saveEditWarnInfo',
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
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');/*提示*/
					$('#win-warn-edit').dialog('close');
					$('#datagrid-warn-list').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');/*提示*/
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
				}
				}
				
		
	    
	
	})
}

	function warnMessageDetail(mainId){
		var clientWidth = document.body.clientWidth;	
		 var clientHeight = document.body.clientHeight;
		 var title = $.i18n.prop('vendor.basedataJs.PromotionReminder');/*"晋级提醒"*/
		new dialog().showWin(title,700,500,ctx+'/manager/common/warning/promotionWarn/'+mainId);
	}

	function promotionFmt(v,r,i){
		var s = '';
		s= '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editPromotion('+r.id+')">'+$.i18n.prop('vendor.basedataJs.Edit')+'</a>';/*编辑*/
		s+= '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="deletePromotion('+r.id+')">'+$.i18n.prop('vendor.basedataJs.Delete')+'</a>';/*删除*/
		return s;
	}



	function submitEditPromotionSet(){
		var url='';
		var sucMeg='';
		/*if($('#id').val()!=0 && $('#id').val()!='0'){*/
			url = ctx+'/manager/common/warning/updateItem';
			sucMeg = $.i18n.prop('vendor.basedataJs.EditPromotionReminderSuccess');/*'编辑晋级提醒成功！'*/
			
		/*}else{
			alert($('#id').val());
			url = ctx+'/manager/common/warning/addItem';
			sucMeg = '添加晋级提醒成功！';
		}*/
		$.messager.progress({
			title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
			msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
		});
		$("#form-promotion-edit").form('submit',{
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
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');
						$('#win-promotion-edit').dialog('close');
						$('#datagrid-promotion-list').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');
					}
					}catch (e) {
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');
					}
			}
		
		})
	}
	
	function roleSelect(){
		$('#roleOneSelect').combobox({ 
			 url:ctx+'/manager/common/warning/roleSelect',
	        editable:false,
	        cache: false,
	        required:true,
	        valueField:'id',   
	        textField:'name',
	   }); 
		$('#roleTwoSelect').combobox({ 
			 url:ctx+'/manager/common/warning/roleSelect',
	        editable:false,
	        cache: false,
	        required:true,
	        valueField:'id',   
	        textField:'name',
	   }); 
		$('#roleThreeSelect').combobox({ 
			 url:ctx+'/manager/common/warning/roleSelect',
	        editable:false,
	        cache: false,
	        required:true,
	        valueField:'id',   
	        textField:'name',
	   }); 
	}

	function editPromotion(id){
		
		$('#roleSelect').combobox({ 
			 url:ctx+'/manager/common/warning/roleSelect',
	        editable:false,
	        cache: false,
	        required:true,
	        valueField:'id',   
	        textField:'name',
	   }); 
		
		$('#win-promotion-edit').dialog({
			iconCls:'icon-edit',
			title:$.i18n.prop('vendor.basedataJs.EditPromotionReminder')/*'编辑晋级提醒'*/
		});
		
		$('#win-promotion-edit').dialog('open');
		$('#form-promotion-edit').form('load',ctx+'/manager/common/warning/getItem/'+id);
	}

	function addPromotion(){
		
		  $('#roleSelect').combobox({ 
				 url:ctx+'/manager/common/warning/roleSelect',
		        editable:false,
		        required:true,
		        cache: false,
		        valueField:'id',   
		        textField:'name',
		   }); 
		  
		$('#win-promotion-edit').dialog({
			iconCls:'icon-add',
			title:$.i18n.prop('vendor.basedataJs.AddPromotionReminder')/*'新增晋级提醒'*/
		});
		var warnMainId = $('#warnMainId').val();
		$('#form-promotion-edit').form('clear');
		$('#id').val(0);
		$('#main_Id').val(warnMainId);
		$('#win-promotion-edit').dialog('open');
	}
	
	function deletePromotion(itemId){
//		$.messager.confirm('提示','确定删除吗',function(r){
		$.messager.confirm($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.SureDelete'),function(r){
			if(r){
			$.messager.progress({
				title:$.i18n.prop('vendor.basedataJs.Promit')/*'提示'*/,
				msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
			});
			
			$.ajax({
				url:ctx+'/manager/common/warning/deletePromotion/'+itemId,
				type:'POST',
				success:function(data){
					$.messager.progress('close');
					
							
							$('#datagrid-promotion-list').datagrid('reload');
						
				}
			})
			}
		})
		
	}
