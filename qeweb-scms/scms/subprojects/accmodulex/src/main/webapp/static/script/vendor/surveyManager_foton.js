//针对关键二级供应商审核重写
Survey.submit = function(formId){
	  //校验必填项
	  var flag = surveyValidation(formId);
	  if(!flag)return false;
	  //校验附件大小
	  var ctId = formId+'-ct';
	  flag = validateAttachmentSize(ctId);
	  if(!flag)return false;
	  var valMsg = validateSecVen(formId)
	  if(valMsg!=''){//加入对关键二级供应商的校验
		  $.messager.alert('提示',valMsg,'warning');
		  return false;
	  }
	  
	  
	  //构造报价对象
	  flag = Survey.setSurveyValue(formId,'surveyBase');
	  if(!flag)return false;
	  $.messager.progress();
	  $('#'+formId).form('submit',{
		  url : ctx+'/manager/vendor/admittance/submitVendorSurvey',
		  dataType : 'json',
		  success : function(data){
			$.messager.progress('close');
			data = $.parseJSON(data);
			//通过之后，需要将按钮隐藏，改变树的状态。将所有input变为disabled
			if(data.success){
				$.messager.alert('提示','提交成功','info');
				var surveyCfgId = data.surveyBase.vendorCfgId;
				var node = $('#tree-survey').tree('find',surveyCfgId);
				if(node){
					$('#tree-survey').tree('update',{
						target : node.target,
						iconCls : 'icon-hourglass'
					});
				}
				Survey.setStatusValue(surveyCfgId,data);
				Survey.initComponent(surveyCfgId);
			    Survey.refreshHis(surveyCfgId,surveyCfgId,data.currentId);
			}else{
				$.messager.alert('提示',data.msg,'error');
			}
			
		  }
	  });

	}

function validateSecVen(formId){
	var rootId = $('#'+formId).getCmp('rootId').val();
	var msg = '';
	if(rootId=='supplier'){
		var $ct = $('#'+formId+'-ct');
		var $selectedArray = $ct.find('option:selected');
		var orgId = $('#orgId').val();
		var mainProIds = '';
		$selectedArray.each(function(){
			mainProIds+=','+$(this).val();
		});
		$.ajax({
			url : ctx+'/manager/vendor/admittance/validateSecVen',
			async:false,
			data : {orgId:orgId,mainProIds:mainProIds},
			dataType : 'json',
			success : function(data){
				if(data.success){
					msg = '';
				}else{
					msg = data.msg;
				}
			}
		});
	}
	return msg;
}
