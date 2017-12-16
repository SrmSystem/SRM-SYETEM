$(function(){
	$('#materialTypeDiv').addClass('hidden');
	$('#materialDiv').addClass('hidden');
	
	
});

function changeTemplateType(obj){
	var $type = $(obj);
	var $radioArray = $('input[type="radio"][name="templateType"]');
	$radioArray.each(function(i){
		if($(this).val() == $type.val()){
			$(this).attr('checked',true);
		}else{
			$(this).attr('checked',false);
		}
		
	});
	
	if($type.val()=='0'){
	  $('#materialTypeDiv').addClass('hidden');
	  $('#materialDiv').addClass('hidden');
	}else if($type.val()=='1'){
	  $('#materialTypeDiv').removeClass('hidden');
	  $('#materialDiv').addClass('hidden');
	  reloadDatagrid('datagrid-materialType',ctx+'/manager/basedata/materialType/getMaterialTypeLeafList');
	}else if($type.val()=='2'){
	  $('#materialTypeDiv').addClass('hidden');
	  $('#materialDiv').removeClass('hidden');
	  reloadDatagrid('datagrid-material',ctx+'/manager/basedata/material');
    }
}

function reloadDatagrid(id,url){
	var rows = $('#'+id).datagrid('getRows');
	if(rows.length<=0){
		//判断是否已经加载
		$('#'+id).datagrid({
			url:url
		});
	}
	
}

/**
 * 创建导航类型
 */
function createNavType(){
	var $form = $('#createNavTypeForm');
	//判断选择的类型
	var $checkedRadio = $('input[type="radio"][name="templateType"]:checked');
	if($checkedRadio.val()=='1'){
		$.messager.confirm('提示','确定要使用该模版为默认模版吗？',function(r){
			if(r){
				$form.getCmp('rangeType').val($checkedRadio.val());
				$form.submit();
			}
			
		});
	}
}

/**
 * 返回上一步关联阶段，会清除阶段设置
 */
function createNavPhase(){
	$.messager.confirm('确认对话框','回到上一步将清除阶段的设置，确定吗？',function(r){
		if(r){
			$('#vendorNavTemplateForm').submit();
		}
		
	});
	
}