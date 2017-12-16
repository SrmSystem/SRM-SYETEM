function createVendorNavTemplate(){
	var flag = $('#vendorNavTemplateForm').form('validate');
	if(flag){
	  $('#vendorNavTemplateForm').submit();
	}
}


//选择阶段
function selectPhase(){
  var selectionArray = $('#selectingPhase').datagrid('getSelections');
  if(selectionArray.length<=0){
	  $.messager.alert('提示','请悬着左边的待选阶段!');
  }
  $('#selectedPhase').datagrid('loadData',{rows:selectionArray});
}

//移除阶段
function unSelectPhase(){
  var selectionArray = $('#selectedPhase').datagrid('getSelections');
  if(selectionArray.length<=0){
	  $.messager.alert('提示','请悬着右边边的已选阶段!');
  }
  $.each(selectionArray,function(i,n){
	  var rowIndex = $('#selectedPhase').datagrid('getRowIndex',n);
	  $('#selectedPhase').datagrid('deleteRow',rowIndex);
  });
  
}

//创建模版和阶段关系
function createTemplatePhase(){
  //校验是否已指定阶段	
  $('#selectedPhase').datagrid('acceptChanges');
  var selectionArray = $('#selectedPhase').datagrid('getRows');

  if(selectionArray.length<=0){
    $.messager.alert('提示','已选阶段不能为空!','info');
    return false;
  }
  var snFlag = true;
  //校验是否已经指定了晋级顺序
  $.each(selectionArray,function(i,n){
    if(n.phaseSn=='' || n.phaseSn==null){
      snFlag = false;
    }
  });
  if(!snFlag){
	  $.messager.alert('提示','必须指定完整的晋级顺序','info');
	  return false;
  }
  
  $.each(selectionArray,function(i,n){
	  for(var p in n){
		  var name = 'phaseList['+i+'].'+p;
		  $('#setTemplatePhaseForm').append('<input type="hidden" name="'+name+'" value="'+n[p]+'"/>');
	  }
  });
  $('#setTemplatePhaseForm').submit();
//  var navTemplate = {};
//  $.each(selectionArray,function(i,n){
//	  for(var p in n){
//		  navTemplate['phaseList['+i+'].'+p] = n[p];
//	  }
//  });

//  $('#setTemplatePhaseForm').form('submit',{
//	url:ctx+'/manager/vendor/vendorNav/createTemplatePhase',
//	queryParams:navTemplate,
//	ajax : false
//  }  
//  );
}

var MaterialType = {
	textFmt	: function(node){return node.text}
}

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
	}else if($type.val()=='2'){
	  $('#materialTypeDiv').addClass('hidden');
	  $('#materialDiv').removeClass('hidden');
    }
}





