
//供应商物料列表操作列
 function optMaterialVendorFmt(v,r,i){
	 var epPriceId = r.price.id;
	 var isVendor = 1;
	 
	 var epQuoteWay = $('#epQuoteWay').val();
	 if(epQuoteWay==0){
		 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openEpSubQuo('+epPriceId+','+r.id+')">分项报价</a> ';		 
	 }else{
		 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openEpWholeQuo('+epPriceId+','+r.id+','+isVendor+')">整项报价</a>';
	 }
	 if(r.price.quoteWay==1){
		 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openEpWholeQuo('+epPriceId+','+r.id+','+isVendor+')">整项报价</a> '
	 }else{
		 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openEpSubQuo('+epPriceId+','+r.id+')">分项报价</a> '
	 }
 } 
 
//整项报价列表页面操作列
 function seeInfoFmt(v,r,i){
	 var isVendor = 0;
	 var epPriceQuoteStatus = $('#epPriceQuoteStatus').val();
	 var str = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="buyerOpenEpWholeQuoWin('+r.epPrice.id+','+r.epMaterial.id+','+isVendor+','+r.epPrice.quoteWay+')">详情</a> ';
	/* if(r.quoteStatus == 1){
		 str += ' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openEpPriceResetWin('+r.id+','+i+')">重新报价</a> ';
	 }*/
	 if(epPriceQuoteStatus == 2 && r.quoteStatus == 1 && r.negotiatedStatus ==0){
		 str += ' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editRow('+r.id+','+i+')">填写协商价</a> ';
	 }
	 /*if(r.epPrice.quoteWay==1){
		 if(epPriceQuoteStatus == 2 && r.quoteStatus == 1 && r.negotiatedStatus ==0){
			 str += ' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editRow('+r.id+','+i+')">填写协商价</a> ';
		 }
	 }*/
	 return str;
 } 
 
 //报价历史列表操作列
 function optEpWholeHisFmt(v,r,i){
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openEpWholeQuoHis('+r.id+')">第 ' + r.quoteCount + ' 轮报价</a> '
 }

 /**
  * 打开报价页面
  */
 function openEpWholeQuo(epPriceId,epMaterialId,isVendor){
	var epVendorId = $("#epVendorId").val();
	var href = ctx + '/manager/ep/epWholeQuo/openEpWholeQuoWin?epPriceId='+ epPriceId+'&epMaterialId='+epMaterialId+'&epVendorId='+epVendorId+'&isVendor='+isVendor;
	var title = "整项报价";
	//window.open (ctx + '/manager/ep/epWholeQuo/openEpWholeQuoWin?epPriceId='+ epPriceId+'&epMaterialId='+epMaterialId+'&epVendorId='+epVendorId,'newwindow','height=clientHeight,width=clientWidth,margin-top=-100px,margin-left=-100px,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no') ;
	parent.myWidow(title,href);	
 }
 
 function openEpSubQuo(epPriceId,epMaterialId){
		var epVendorId = $("#epVendorId").val();
		var href = ctx + '/manager/ep/epSubQuo/displaySubQuoView?epMaterialId='+epMaterialId;
		var title = "分项报价";
		
		parent.myWidow(title,href);	
	 }
 
 /**
  * 打开整项报价历史页面
  */
 function openEpWholeQuoHis(id){
	var href = ctx + '/manager/ep/epWholeQuoHis/openEpWholeQuoHisWin?epWholeQuoHisId=' +id;
	var title = "报价历史";
	parent.myWidow(title,href);	
 }
 
 /**
  * 采购商打开报价详情页面xyz
  */
/* function buyerOpenEpWholeQuoWin(epPriceId,epMaterialId,isVendor,quoteWay){
	var epVendorId = $("#epVendorId").val();
	var href = ctx + '/manager/ep/epWholeQuo/buyerOpenEpWholeQuoWin?epPriceId='+ epPriceId+'&epMaterialId='+epMaterialId+'&epVendorId='+epVendorId+'&isVendor='+isVendor;
	var title = "";
	if(quoteWay==0){
		title="分项报价详情";
	}else if(quoteWay==1){
		title="整项报价详情";
	}
	parent.myWidow(title,href);	
 }*/
 
 /**
  * 采购商点击重新报价时--打开重新选择报价截止时间页面
  */
 function openEpPriceResetWin(id, index){
	 $('#win-reset-quoteEndTime').window({
			iconCls:'icon-add',
			title:'修改报价截止时间'
	});
	$('#form-reset-quoteEndTime').form('clear');
	//$('#form-reset-quoteEndTime').form('load', ctx + '/manager/ep/epWholeQuo/getById/'+id);
	$.ajax({
        type: "POST",
        url: ctx + '/manager/ep/epWholeQuo/getById/'+id,
        cache: false,
        dataType : "json",
        success: function(data){
        	$('#form-reset-quoteEndTime').form('load',data);
        	$('#materialName').val(data.epMaterial.materialName);
        	$('#vendorName').val(data.epVendor.vendorName);
        }
      });
	$('#win-reset-quoteEndTime').window('open');
 }
 
 /**
  * 供应商打开批量报价页面
  */
 function openMassWin(){
	 var epMaterialIds = "";
	 var epPriceId = $("#priceId").val();
	 var epVendorId = $("#epVendorId").val();
	 var o = $("#datagrid-item-epmaterial").datagrid("getSelections");
	 for(var i=0;i<o.length;i++){
		 if(epMaterialIds ==""){
			 epMaterialIds +=o[i].id;
		 }else{
			 epMaterialIds +=" ";
			 epMaterialIds +=o[i].id;
		 }
	 } 
	 
	//验证是否包含已报价的数据
	 $.ajax({
		 url:ctx + '/manager/ep/epWholeQuo/getByPriceAndVendorAndMaterial',
		 data:{
			 'epPriceId':epPriceId,
			 'epVendorId':epVendorId,
			 'epMaterialIds':epMaterialIds
			 },
		 dataType:'json',
		 type:'POST',
		 success : function(data){
			 for(var i=0;i<data.length;i++){
				 if(data[i].quoteStatus ==1){
						$.messager.alert('提示','包含已报价的数据','info');
						return false;
				}
				 if(data[i].isVendor ==1){
					$.messager.alert('提示','包含已保存的数据','info');
					return false;
				}
			 }
			var href = ctx + '/manager/ep/epWholeQuo/openMassWin?epPriceId='+epPriceId +'&epVendorId='+epVendorId +'&epMaterialIds='+epMaterialIds;
			var title = "批量报价";
			parent.myWidow(title,href);
		 }
	 });
	 
 }
 
 /**
  * 采购商选择是否合作
  */
 function isCooperation(id){
	 var msg = '确定选择合作吗？';
	 $.messager.confirm('提示',msg,function(r){
		 if(r){
			 $.ajax({
				 url:ctx + '/manager/ep/epWholeQuo/changeCooperationStatus',
				 data:{epWholeQuoId:id},
				 dataType:'json',
				 type:'POST',
				 success : function(data){
					 if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$("#datagrid-epEPWholeQuo-list").datagrid('reload');
					 }
				 }
			 });
		 }
	 });
 }
 
 
 /**
  * 供应商保存或提交报价单
  * @param type
  */
 function saveEpWholeQuo(type){
	 var epWholeQuoId =$("#epWholeQuoId").val();
	 $.messager.progress();
	 $('#form-epWholeQuo-offer').form('submit',{
			ajax:true,
			iframe: true,   
			url:ctx+'/manager/ep/epWholeQuo/saveEpWholeQuo',
			onSubmit:function(param){
				param.type = type;
				param.epWholeQuoId = epWholeQuoId;
				return $("#form-epWholeQuo-offer").form('validate');
			},
			success:function(data){
				$.messager.progress('close');
				data = $.evalJSON(data);
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
					parent.closeWindow();
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
				//$dialog.dialog('open');
				window.location.reload();//刷新当前页面.
			}
		});
 }
 
 /**
  * 编辑议价列
  * @param id
  * @param index
  */
 function editRow(id,index){
	 var datagridId = '#datagrid-epEPWholeQuo-list';
	 $(datagridId).datagrid('selectRow', index).datagrid('beginEdit', index);
 }
 
 
 function saveNegotiatedPriceBefore(datagridId){
		$.messager.progress();
		datagridId = "#"+datagridId;
		$(datagridId).datagrid('acceptChanges'); //保存编辑
		
		var rows = $(datagridId).datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			if(rows[i].negotiatedStatus == 1){
				$.messager.progress('close');
				$.messager.alert('提示','包含已提交协商价的数据！','info');
				return false;
			}
		}


	

		var o =$(datagridId).datagrid('getData'); 
		var datas = JSON.stringify(o);  
		$('#form-epPrice-search').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/ep/epWholeQuo/saveNegotiatedPriceBefore',
			onSubmit:function(param){
				param.datas = datas; 
			},
			success:function(data){
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert("提示", result.msg, 'info');
						$(datagridId).datagrid('reload');
					}else{
						$.messager.alert("提示", result.msg, 'error');
					}
				}catch (e) {  
					$.messager.alert("提示", e,'error');
				}
			}
		});
	}
 /**
  * 提交采购商编辑的议价价格
  */
function saveNegotiatedPrice(datagridId){
	$.messager.progress();
	
	datagridId = "#"+datagridId;
	$(datagridId).datagrid('acceptChanges'); //保存编辑
	
    var selections = $(datagridId).datagrid('getSelections');
    
	if(selections.length == 0){
		$.messager.progress('close');
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}


	var new_sels = new Array();
	for(var i=0;i<selections.length;i++){
		if(selections[i].negotiatedStatus == 1){
			$.messager.progress('close');
			$.messager.alert('提示','包含已提交协商价的数据！','info');
			return false;
		}
		var new_sel = {};
	 	new_sel.id = selections[i].id;
	 	new_sels.push(new_sel);
	}
	
	
 	var params = $.toJSON(new_sels);
	$("#tableDatas").val(params);
	
	$('#form-epPrice-search').form('submit',{
		ajax:true,
		iframe: true,    
		url: ctx + '/manager/ep/epWholeQuo/saveNegotiatedPrice',

		onSubmit:function(){
		},
		success:function(data){
			$.messager.progress('close');
			try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert("提示", result.msg, 'info');
					$(datagridId).datagrid('reload');
				}else{
					$.messager.alert("提示", result.msg, 'error');
				}
			}catch (e) {  
				$.messager.alert("提示", e,'error');
			}
		}
	});
}

/**
 * 回传询价结果
 */
function uploadQuotePrice(datagridId){
	$.messager.progress();
	
	datagridId = "#"+datagridId;
	$(datagridId).datagrid('acceptChanges'); //保存编辑
	
   var selections = $(datagridId).datagrid('getSelections');
   
	if(selections.length == 0){
		$.messager.progress('close');
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}


	var new_sels = new Array();
	for(var i=0;i<selections.length;i++){
/*		if(selections[i].eipStatus == 1||selections[i].eipApprovalStatus!=1){
			$.messager.progress('close');
			$.messager.alert('提示','未审核通过的或者上传成功的不能回传！','info');
			return false;
		}*/
		var new_sel = {};
	 	new_sel.id = selections[i].id;
	 	new_sels.push(new_sel);
	}
	
	
	var params = $.toJSON(new_sels);
	$("#tableDatas").val(params);
	
	$('#form-epPrice-search').form('submit',{
		ajax:true,
		iframe: true,    
		url: ctx + '/manager/ep/epWholeQuo/uploadQuotePrice',

		onSubmit:function(){
		},
		success:function(data){
			$.messager.progress('close');
			try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert("提示", result.msg, 'info');
					$(datagridId).datagrid('reload');
				}else{
					$.messager.alert("提示", result.msg, 'error');
				}
			}catch (e) {  
				$.messager.alert("提示", e,'error');
			}
		}
	});
}



/**
 * 采购商详细页面提交议价价格
 * @param type
 */
function submitByItem(type,cooperationVal){
	$.messager.progress();
	
	 $('#datagrid-item-list').datagrid('acceptChanges'); 
	/* if(cooperationVal == '0'){
		 $.messager.alert('提示','未合作数据不能提交协商价！' ,'error');
		 return false;
	 }*/
	 
	 var quoteWayVal = $('#epPriceQuoteWay').val();
	 if(quoteWayVal == 0){
		 var o =$('#datagrid-item-list').datagrid('getData'); 
		 var datas = JSON.stringify(o);   
		 $("#tableDatas").val(datas);
	 }
	 $('#form-epWholeQuo-Item').form('submit',{
			ajax:true,
			iframe: true,   
			url:ctx+'/manager/ep/epWholeQuo/saveNegotiatedPriceByItem',
			onSubmit:function(param){
			},
			success:function(data){
				data = $.evalJSON(data);
				$.messager.progress('close');
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
				
				window.location.reload();//刷新当前页面.
			}
		});
}
/**
 * 供应商分项确认议价(接受、拒绝)
 * accept = 接受议价
 * refuse = 拒绝议价
 * 
 */
function confirmEpWholeSubQuo(type){
	 var formVendSubId="#form-subQuo-addoredit";
	 var epWholeQuoId =$("#epWholeQuoId").val();
	 var message = "";
	 if("accept" == type){
		 message = "确定接受议价吗";
	 }else if("refuse" == type){
		 message = "确定拒绝议价吗";
	 }
	 $.messager.confirm('提示',message,function(r){
			if(r){
			 $(formVendSubId).form('submit',{
					ajax:true,
					iframe: true,   
					url:ctx+'/manager/ep/epWholeQuo/confirmEpWholeSubQuo',
					onSubmit:function(param){
						param.type = type;
						param.epWholeQuoId = epWholeQuoId;
					},
					success:function(data){
						data = $.evalJSON(data);
						if(data.success){
							$.messager.alert('提示',data.msg,'info');
						}else{
							$.messager.alert('提示',data.msg,'error');
						}
						window.location.reload();//刷新当前页面.
					}
				});
			}
		});
}
/**
 * 供应商整项确认议价(接受、拒绝)
 * accept = 接受议价
 * refuse = 拒绝议价
 * 
 */
function confirmEpWholeQuo(type){
	var formId = "#form-epWholeQuo-offer";
	 var epWholeQuoId =$("#epWholeQuoId").val();
	 var message = "";
	 if("accept" == type){
		 message = "确定接受议价吗";
	 }else if("refuse" == type){
		 message = "确定拒绝议价吗";
	 }
	 
	$.messager.confirm('提示',message,function(r){
		if(r){
		 $(formId).form('submit',{
				ajax:true,
				iframe: true,   
				url:ctx+'/manager/ep/epWholeQuo/confirmEpWholeQuo',
				onSubmit:function(param){
					param.type = type;
					param.epWholeQuoId = epWholeQuoId;
				},
				success:function(data){
					data = $.evalJSON(data);
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
					}else{
						$.messager.alert('提示',data.msg,'error');
					}
					window.location.reload();//刷新当前页面.
				}
			});
		}
	});
}

/**
 * 采购商提交修改的报价截止日期
 */
function submitQuoteEndTime(){
	
	$.messager.progress();
	$('#form-reset-quoteEndTime').form('submit',{
		ajax:true,
		iframe:true,
		url:ctx+'/manager/ep/epWholeQuo/submitQuoteEndTime',
		onsubmit:function(){
			var isValid = $(this).from('validate');
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
					$('#win-reset-quoteEndTime').window('close');
					$('#datagrid-epEPWholeQuo-list').datagrid("reload");
				}else{
					$.messager.alert('提示',result.msg ,'error');
				}
			}catch(e){
				$.messager.alert('提示',data,'error');
			}
		}
	});
}


/**
 * 供应商批量报价
 * @param type
 * save =保存
 * submit =提交
 */
function saveEpWholeQuoMass(type){
	$('#datagrid-epWholeQuo-mass').datagrid('acceptChanges'); 
	var o =$('#datagrid-epWholeQuo-mass').datagrid('getData'); 
	var datas = JSON.stringify(o);   
	 $('#form-epWholeQuo-mass').form('submit',{
			ajax:true,
			iframe: true,   
			url:ctx+'/manager/ep/epWholeQuo/saveEpWholeQuoMass',
			onSubmit:function(param){
				var isValid = $(this).form('validate');
				param.datas = datas;
				param.type = type;
				return isValid;
			},
			success:function(data){
				data = $.evalJSON(data);
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
				//window.location.reload();//刷新当前页面.
				parent.closeWindow();
			}
		});
}

//select 标签的Onchange事件(含税单价 = 不含税单价 *(1+税率))
$(function () {
	$("#taxRate").combobox({
		onChange: function (newVal,oldVal) {
			var totalQuotePriceVal = $("#totalQuotePrice").numberbox("getValue");
			if(totalQuotePriceVal !=null && totalQuotePriceVal !=""){
				var quotePriceVal = (parseFloat(1)+parseFloat(newVal))*totalQuotePriceVal;
				$("#quotePrice").numberbox("setValue",quotePriceVal);
			}
			var _quotePriceVal = $("#quotePrice").numberbox("getValue");
			if(_quotePriceVal !=null && _quotePriceVal !=""){
				var _totalQuotePriceVal = _quotePriceVal/(parseFloat(1)+parseFloat(newVal));
				$("#totalQuotePrice").numberbox("setValue",_totalQuotePriceVal);
			}
		}
	});
	
	$("#quotePrice").numberbox({
		onChange: function (newVal,oldVal) {
			var taxRateVal = $("#taxRate").combobox("getValue");
			if(taxRateVal !=null && taxRateVal !=""){
				var totalQuotePriceVal = newVal/(parseFloat(1)+parseFloat(taxRateVal));
				$("#totalQuotePrice").numberbox("setValue",totalQuotePriceVal)
			}
		}
	});
	
	$("#totalQuotePrice").numberbox({
		onChange: function (newVal,oldVal) {
			var taxRateVal = $("#taxRate").combobox("getValue");
			if(taxRateVal !=null && taxRateVal !=""){
				var quotePriceVal = (parseFloat(1)+parseFloat(taxRateVal))*parseFloat(newVal);
				$("#quotePrice").numberbox("setValue",quotePriceVal)
			}
		}
	});

	$("#taxRateMass").combobox({
	onChange: function (newVal,oldVal) {
		$("#taxCodeVal").val(newVal);
		if(newVal==""){
			newVal ="0";
		}
		$('#datagrid-epWholeQuo-mass').datagrid('acceptChanges');
			var rows = $('#datagrid-epWholeQuo-mass').datagrid('getRows');
			if(rows !=null && rows.length>0){
				for(var i=0;i<rows.length;i++){
					/*var ed1 = $('#datagrid-epWholeQuo-mass').datagrid('getEditor', {index:i,field:'totalQuotePrice'});
					var totalQuotePriceVal = $(ed1.target).textbox('getValue');*/
					var totalQuotePriceVal=rows[i]['totalQuotePrice'];
					if(totalQuotePriceVal==null||totalQuotePriceVal=="" ){
						totalQuotePriceVal=0;
					}
					var quotePriceVal = (parseFloat(1)+parseFloat(newVal))*parseFloat(totalQuotePriceVal);
					$('#datagrid-epWholeQuo-mass').datagrid('updateRow',{
						index: i,
						row: {
							totalQuotePrice:totalQuotePriceVal,
							quotePrice: quotePriceVal
						}
					});
					/*var ed = $('#datagrid-epWholeQuo-mass').datagrid('getEditor', {index:i,field:'quotePrice'});
					$(ed.target).textbox('setValue',quotePriceVal);*/
				}
			}
		}
	});

	$("#isIncludeTax").combobox({
		onChange: function (newVal,oldVal) {    	 
	    	 var rows = $("#datagrid-item-list").datagrid("getRows");
	    	  var tt=0;
	    	  for(var i=0;i<rows.length;i++){
	    		  var total=$('#datagrid-item-list').datagrid('getEditor', { index: i, field: 'subtotal' });
	    		  if(total == null){
	    			  tt=Number(tt)+Number(rows[i].subtotal); 
	    		  }else{
	    			  tt=Number(tt)+Number(total.target.val());
	    		  }   
	    	  }
	    	  if(newVal==null || newVal ==""){
	    		  return false;
	    	  }else if(newVal == 0){
	    		  $("#totalQuotePrice").textbox('setValue',tt);   
	    	  }else if(newVal ==1){
	    		  $("#quotePrice").textbox('setValue',tt);   
	    	  }
			}
		});
});
