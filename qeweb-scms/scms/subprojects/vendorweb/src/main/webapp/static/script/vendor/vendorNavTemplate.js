var VendorNavTemplate = {
	defaultFlagFmt : function(v,r,i){
		return StatusRender.render(v,'yesOrNo',true);
	},
	finishStatusFmt : function(v,r,i){
		return StatusRender.render(v,'yesOrNo',true);
	},
	viewFmt : function(v,r,i){
		return '<a href="javascript:;" class="btn-link" onclick="VendorNavTemplate.view('+r.id+')">'+v+'</a>';
	},
	detailFormatter : function(index,row){
		return '<div class="ddv" style="padding:5px 0"></div>';
	},
	phaseSetFmt : function(v,r,i){//重新设置阶段和调查表
		return '<button class="btn-link" onclick="VendorNavTemplate.reset('+r.id+')">重设</button>';
	},
	reset : function(id){
		$('#win-reset').window('open');
		$('#win-reset').window('center');
		$('#win-reset').getCmp('datagrid-reset').datagrid({
			url:ctx+'/manager/vendor/vendorTemplatePhase/getByTemplate',
			idField:'id',
			onClickCell: CellEditor.onClickCell,
			queryParams:{
			  templateId : id
			},
			fitColumns:true,
			toolbar:[{
				text:'重设',
				iconCls:'icon-save',
				handler:function(){
					//重设
					$.messager.confirm('提示','重设后仅对新注册的供应商有效，重设会清空原有调查表配置，请重新选择调查表！您确认要重设吗?',function(r){
						if(!r)return false;
						VendorNavTemplate.resetSubmit();
						
					});
					
				}
			}]
		});
	},
	resetSubmit : function(){
    	//获得所有的阶段，并将该阶段的所有关联调查表赋值
    	//要组装成调查表分配配置
		var $datagrid = $('#win-reset').getCmp('datagrid-reset');
    	var templatePhaseArray = $datagrid.datagrid('getRows');
    	var flag = true;//如果校验不通过，不能提交
    	$.each(templatePhaseArray,function(i){
    		var templateSurveyArray = [];
    		var templatePhase = this;
    		//需要校验阶段顺序是否填写了
    		if(templatePhase.phaseSn==null || templatePhase.phaseSn==''){
    			$.messager.alert('提示','阶段顺序不能为空','warning');
    			flag = false;
    			return false;
    		}
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
    		templatePhase.itemList = templateSurveyArray;
    	});
    	if(!flag)return false;
    	$.messager.progress({
    		title : '提示',
    		msg : '提交中...'
    		
    	});
    	$.ajax({
    		url : ctx+'/manager/vendor/vendorNavTemplate/resetTemplate',
    		method : 'post',
    		data : $.toJSON(templatePhaseArray),
    		contentType : 'application/json',
    		dataType : 'json',
    		success : function(data){
    			$.messager.progress('close');
    			if(data.success){
    				$.messager.alert('提示','重设完成','info');
    				$('#win-reset').window('close');
    			}else{
    				$.messager.alert('提示',data.msg,'error');
    				$('#win-reset').window('close');
    			}
    			
    		}
    		
    	});		
	},
	viewSurvey : function(index,row){
		var $ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
		//用ajax
		$.ajax({
			url : ctx+'/manager/vendor/vendorNav/getTemplateSurveyByPhase',
			data : {id:row.id},
			dataType : 'json',
			success : function(data){
				if(!data.success){
					$.messager.alert('提示','获取数据失败','error');
					return false;
				}
				var dataList = data.dataList;
				var html = '';
				for(var i=0;i<dataList.length;i++){
					var survey = dataList[i];
					html += '<button type="button" class="btn btn-link" onclick="SurveyTemplate.openSurvey('+survey.surveyTemplateId+')">'+survey.surveyName+'</button>';
				}
				html+='';
				$ddv.html(html);
				$('#datagrid-vendorTemplatePhase').datagrid('fixDetailRowHeight',index);
			}
			
		});
//		$(this).datagrid('fixDetailRowHeight',index);
	},
	view : function(id){
		$('#win-vendorNavTemplate-view').window('open');
		$('#datagrid-vendorTemplatePhase').datagrid({
			url:ctx+'/manager/vendor/vendorTemplatePhase/getByTemplate',
			queryParams:{
			  templateId : id
			},
			view:detailview,
			detailFormatter:VendorNavTemplate.detailFormatter,
			onExpandRow:VendorNavTemplate.viewSurvey,
			fitColumns:true
		});
	},
	del : function(){
		var $datagrid = $('#datagrid-vendorNavTemplate-list');
		var delList = $datagrid.datagrid('getSelections');
		if(delList.length<=0){
			$.messager.alert('提示','没有选择任何数据','info');
			return;
		}
		var validateFlag = true;
		$.each(delList,function(i,n){
			if(n.defaultFlag==1){
				$.messager.alert('提示','默认模版只能重设和替换，不能删除！','warning');
				validateFlag = false;
			}
		});
		if(!validateFlag)return;
		$.messager.progress();
		$.ajax({
			url : ctx+'/manager/vendor/vendorNavTemplate/deleteVendorNavTemplate',
			method : 'post',
			data : $.toJSON(delList),
			contentType : 'application/json',
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$datagrid.datagrid('reload');
				}else{
					$.messager.alert('提示',data.msg,'warning');
				}
			}
			
		});
	}
		
}