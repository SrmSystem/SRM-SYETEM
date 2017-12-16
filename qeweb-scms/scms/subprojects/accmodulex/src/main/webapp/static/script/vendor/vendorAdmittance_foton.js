VendorAdmittance.promote = function(id,name,phaeId,phaseSn){//是vendorBase的ID
	  var baseVendorList = [{id:id}];
	  $.messager.confirm('提示','确认晋级吗?',function(r){
		  if(!r)return;
		  
		  if(phaseSn==2){//如果是体系外晋级，弹出供货关系窗口
//			  $('#dialog-supply-material').dialog('autoSizeMax','body');
//			  $('#dialog-supply-material').dialog('open');
//			  $('#datagrid-supply-material-select').datagrid({
//				 url :  ctx+'/manager/basedata/material', 
//			  });
			  var $form = $('#dialog-qualityreport-form');
			  $form.getCmp('vendorName').html(name);
			  $form.getCmp('id').val(id);
			  $('#dialog-qualityreport').dialog('open');
			  return;
		  }
		  
		  
		  VendorAdmittance.promoteSubmit(baseVendorList,function(data){
			  if(data.success){
	             var $dialog = $('#dialog-audit');
	             var $datagrid = $('#datagrid-'+data.phaseId);
	             $.messager.alert('提示','晋级成功','info');
	             $dialog.dialog('close');
	             $datagrid.datagrid('reload');

//				  window.parent.location.href = window.parent.location.href;
			  }else{
				  var data = decodeURI(data.msg);
				  var reg1=new RegExp("%2F","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"/");

				  reg1=new RegExp("\\+","g"); //创建正则RegExp对象  
				  data=data.replace(reg1," ");

				  reg1=new RegExp("%3D","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"=");

				  reg1=new RegExp("%3A","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,":");

				  reg1=new RegExp("%3B","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,";");

				  reg1=new RegExp("%23","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"#");

				  reg1=new RegExp("%26","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"&");

				  reg1=new RegExp("%3B","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,";");

				  $.messager.alert("提示",data,"error");
				  
			  }
			  
		  });
		  
	  });
	 
	  
  };
VendorAdmittance.promoteSubmit = function(baseVendorList,callBack){
	  $.ajax({
			 url : ctx+'/manager/vendor/vendorBaseInfo/vendorPromote',
			 data : $.toJSON(baseVendorList),
			 method : 'post',
			 contentType : 'application/json',
			 dataType : 'json',
			 success : function(data){
				 callBack(data);
			 }
			 
		 });
  };
VendorAdmittance.promoteWithSupMat = function(id){//带着供货关系的晋级,vendorBase的ID
	 var $dgSelected = $('#datagrid-supply-material-selected');
	 $dgSelected.datagrid('acceptChanges');
	 var baseVendorList = [];
	 var baseVendor = {id:id};
	 var supMatRowList = $dgSelected.datagrid('getRows');
	 if(supMatRowList==null || supMatRowList.length<=0){
		 $.messager.alert('提示','请选择供货物料','warning');
		 return;
	 }
     var venMatRelList = [];
	 $.each(supMatRowList,function(i,n){
		 var venMatRel = {materialId:n.id,materialName:n.name,dataFrom:n.dataFrom};
		 venMatRelList.push(venMatRel);
	 });
	 baseVendor.venMatRelList = venMatRelList;
	 baseVendorList.push(baseVendor);
	 VendorAdmittance.promoteSubmit(baseVendorList,function(data){
		 if(data.success){
			 $('#dialog-supply-material').dialog('close');
             var $dialog = $('#dialog-audit');
             var $datagrid = $('#datagrid-'+data.phaseId);
             $.messager.alert('提示','晋级成功','info');
             $dialog.dialog('close');
             $datagrid.datagrid('reload');

//			  window.parent.location.href = window.parent.location.href;
		  }else{
			  $.messager.alert('提示',data.msg,'warning');
		  }
		 
	 });
}  

VendorAdmittance.Material = {
	search : function(){
		var searchParamArray = $('#form-supply-material-select').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-supply-material-select').datagrid('load',searchParams);
	},
	select : function(){
		var $dgSelect = $('#datagrid-supply-material-select');
		var $dgSelected = $('#datagrid-supply-material-selected');
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
			var index = $(datagridId).datagrid('getRowIndex',n.id);
			$(datagridId).datagrid('deleteRow',index);
		});
	}
};  

/** 质量报告上传的晋级 */
VendorAdmittance.vendorPromoteWithQsReport = function(formId){
	$.messager.progress();
	$(formId).form('submit',{
		url :ctx+'/manager/vendor/vendorBaseInfo/vendorPromoteWithQsReport',
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success : function(data){
			$.messager.progress('close');
			$('#qs-pro-bt').linkbutton('enable');
			data = $.parseJSON(data);
			if(data.success){
	            var $dialog = $('#dialog-audit');
	            var $datagrid = $('#datagrid-'+data.phaseId);
	            $.messager.success('晋级成功');
	            $('#dialog-qualityreport').dialog('close');
	            $dialog.dialog('close');
	            $datagrid.datagrid('reload');
			  }else{
				  var data = decodeURI(data.msg);
				  var reg1=new RegExp("%2F","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"/");

				  reg1=new RegExp("\\+","g"); //创建正则RegExp对象  
				  data=data.replace(reg1," ");

				  reg1=new RegExp("%3D","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"=");

				  reg1=new RegExp("%3A","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,":");

				  reg1=new RegExp("%3B","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,";");

				  reg1=new RegExp("%23","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"#");

				  reg1=new RegExp("%26","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,"&");

				  reg1=new RegExp("%3B","g"); //创建正则RegExp对象  
				  data=data.replace(reg1,";");

				  $.messager.alert("提示",data,"error");
			  }
		}
		
	});
};

VendorAdmittance.qsReportFmt = function(v,r,i){
	if(v==null || v=='')return v;
	var name = v.substring(v.lastIndexOf('/')+1,v.length);
	return '<a href="javascript:;" onclick="File.download(\''+v+'\',\'\')">'+name+'</a>';
	
};