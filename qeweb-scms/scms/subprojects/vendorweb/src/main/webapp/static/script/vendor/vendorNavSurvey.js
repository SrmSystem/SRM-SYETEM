var VendorNavSurvey = {
	selectSurveyFmt	: function(v,r){
	   return '<a href="javascript:;" onclick="VendorNavSurvey.openSurveyWindow('+r.id+')">选择调查表</a>';
	},
	surTemTypeFmt : function(v,r){
		return Constants.surTemType[v];
	},
	onCheckAll : function(rows){
		$.each(rows,function(i,n){
			var $checkbox = $('#window-phaseSurvey').find(':checkbox[value="'+n.id+'"]:eq(0)');
			if($checkbox.prop('disabled')){
				 var rowIndex =  $('#datagrid-vendorSurveyTemplate-list').datagrid('getRowIndex',n.id);
				  $('#datagrid-vendorSurveyTemplate-list').datagrid('uncheckRow',rowIndex); 
			}
			
		})
	},
    openSurveyWindow : function(templatePhaseId){
       var $win =  $('#window-phaseSurvey');
       var $datagrid = $('#datagrid-vendorSurveyTemplate-list');
       $win.window('open');
       $win.window('center');
       $win.find(':checkbox').prop('disabled',false);
	   $('#templatePhaseId').val(templatePhaseId);
	   $datagrid.datagrid('clearSelections');
	   $datagrid.datagrid('clearChecked');
	   var unSelectedSurveyArray = VendorNavSurvey.getUnSelectedSurvey(templatePhaseId);
	   for(var i=0;i<unSelectedSurveyArray.length;i++){
		   var id = unSelectedSurveyArray[i];
		   var $checkbox = $win.find(':checkbox[value="'+id+'"]:eq(0)');
		   $checkbox.prop('disabled',true);
	   }
	   var $selectedSurvey = $('#templateSurvey'+templatePhaseId);
	   if($selectedSurvey.get(0)!=null){
		   var selectedSurveyIds = $selectedSurvey.val();
		   if(selectedSurveyIds==null || selectedSurveyIds=='')
			   return;
		   var selecteSurveyIdArray = selectedSurveyIds.split(',');
		   for(var i=0;i<selecteSurveyIdArray.length;i++){
			  if(selecteSurveyIdArray[i]!=''){
				  var rowIndex =  $datagrid.datagrid('getRowIndex',selecteSurveyIdArray[i]);
				  $datagrid.datagrid('checkRow',rowIndex); 
			  }
		   }
	   }
    },
    getUnSelectedSurvey : function(templatePhaseId){//获取不能选择的调查表，即已经被其他阶段已经选择了
    	var $dataDiv = $('.selectedSurvey[id!="templateSurvey'+templatePhaseId+'"]');
    	var idArray = [];
    	$dataDiv.each(function(i){
    		var ids = $(this).val();
    		var idArray_temp = ids.split(',');
    		$.each(idArray_temp,function(i,n){
    			if(n!='')
    			idArray.push(n);
    		});
    	});
    	return idArray;
    },
    selectSurvey : function(){
    	var selArray = $('#datagrid-vendorSurveyTemplate-list').datagrid('getChecked');
    	if(selArray.length<=0){
    		$.messager.alert('提示','请选择要关联的调查表！','warning');
    		return;
    	}
    	//当前选中行的ID用来做隐藏input的ID标记
    	var templatePhaseId =  $('#templatePhaseId').val();
    	var templateSurveyIds = '';
		for(var i=0;i<selArray.length;i++){
			templateSurveyIds+=selArray[i].id+',';
		}
    	if($('#templateSurvey'+templatePhaseId).get(0)!=null){
    		
    		$('#templateSurvey'+templatePhaseId).val(templateSurveyIds);
    	}else{
    		$(document.body).append('<input class="selectedSurvey" type="hidden" id="templateSurvey'+templatePhaseId+'" value="'+templateSurveyIds+'"/>');
    	}
    	$('#window-phaseSurvey').window('close');
    },
    createTemplateSurvey : function(){
//    	var flag = VendorNavSurvey.validate();
//    	if(!flag)return false;
    	//获得所有的阶段，并将该阶段的所有关联调查表赋值
    	//要组装成调查表分配配置
    	var templateSurveyArray = [];
    	var templatePhaseArray = $('#datagrid-templatePhase').datagrid('getRows');
    	$.each(templatePhaseArray,function(i){
    		var templatePhase = this;
    		//先查询是否进行了分配
    		var $surveyTemplateIds = $('#templateSurvey'+templatePhase.id);
    		if($surveyTemplateIds.get(0)==null || $surveyTemplateIds.val()=='')return true;
    		var surveyTemplateIds = $surveyTemplateIds.val();
    		var surveyTemplateIdArray = surveyTemplateIds.split(',');
    		$.each(surveyTemplateIdArray,function(){
    			if(this=='')return true;
    			var templateSurvey = {};
        		templateSurvey.vendorTemplateId = templatePhase.templateId;
        		templateSurvey.templatePhaseId = templatePhase.id;
        		templateSurvey.phaseId = templatePhase.phaseId;
        		templateSurvey.phaseCode = templatePhase.code;
        		templateSurvey.phaseName = templatePhase.name;
        		templateSurvey.phaseSn = templatePhase.phaseSn;
        		templateSurvey.surveyTemplateId = this;
        		templateSurveyArray.push(templateSurvey);
    		});
    	});
    	if(templateSurveyArray.length<=0)return false;
    	$.ajax({
    		url : ctx+'/manager/vendor/vendorNav/createTemplateSurvey',
    		method : 'post',
    		data : $.toJSON(templateSurveyArray),
    		contentType : 'application/json',
    		dataType : 'json',
    		success : function(data){
    			if(data.success){
    				$.messager.alert('提示','配置完成','info');
    				window.location.href = ctx+'/manager/vendor/vendorNav';
    			}else{
    				$.messager.alert('提示',data.msg,'error');
    			}
    			
    		}
    		
    	});
    	
    	
    },
    validate : function(){//校验
    	//获得所有阶段，暂时不需要校验，可以有调查表也可以没有
    	return true;
    },
    toPre : function(){//在向导过程中退回上一步
    	$('#createNavTypeForm').submit();
    }
    
    
		
}

