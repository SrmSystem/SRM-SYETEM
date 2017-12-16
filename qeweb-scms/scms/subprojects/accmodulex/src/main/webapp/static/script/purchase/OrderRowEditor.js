//订单查分供货计划
var RowEditorX = {
   editIndex : undefined,
   endEditing : function(datagridId){
          if (RowEditorX.editIndex == undefined){return true;}
          if ($(datagridId).datagrid('validateRow', RowEditorX.editIndex)){
              var eds = $(datagridId).datagrid('getEditors',RowEditorX.editIndex);
              $.each(eds,function(i,ed){
            	  if(ed.type=='combobox'){
	            	  var text = $(ed.target).combobox('getText');
	            	  //表格列格式化 ： formatter:function(value,row){ return row.field + 'name';}
	                  $(datagridId).datagrid('getRows')[RowEditorX.editIndex][ed.field + 'name'] = text;
	                  $(datagridId).datagrid('endEdit', RowEditorX.editIndex);
            	  }
              });
              RowEditorX.editIndex = undefined;
              return true;
          } else {
              return false;
          }
   },
   onClickCell : function(index, field){//供货计划双击表格可编辑
	   var datagridId = '#'+$(this).attr('id');
	   var rows = $(datagridId).datagrid('getRows');
	   var row = rows[index];
		 //if(row.confirmStatus!=0){
	   if(row.confirmStatus ==1){
			 return;
		 }else{
			 if (RowEditorX.editIndex != index){
	              if (RowEditorX.endEditing(datagridId)){
	                  $(datagridId).datagrid('selectRow', index).datagrid('beginEdit', index);
	                  var ed = $(datagridId).datagrid('getEditor', {index:index,field:field});
	                  if(ed!=null){
	                  ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
	                  }
	                  RowEditorX.editIndex = index;
	              } else {
	                  $(datagridId).datagrid('selectRow', editIndex);
	              }
	          }
		 }
   },
   accept : function(datagridId){
	   datagridId = '#' + datagridId;
         if (RowEditorX.endEditing(datagridId)){
             $(datagridId).datagrid('acceptChanges');
         }
	},
   append : function(datagridId){
	   datagridId = '#' + datagridId;
	   var $datagrid = $(datagridId);
		var data = $datagrid.datagrid('getRows');
		var row = $(datagridId).datagrid('getSelected');
		var confirmStatus = row.confirmStatus;
		//if(confirmStatus != 0){
		if(confirmStatus == 1){
			$.messager.alert('提示', "已确认状态，不能再编辑!", 'info');
			return;
		}
		
		var row_new = clone(row);
		row_new.isAdd = true;
		var itemNo = 0;
		var allQty = 0;
		for(var i in data){
			var obj = data[i];
			var cur_itemNo = obj.itemNo;
			if(cur_itemNo > itemNo){
				itemNo = cur_itemNo;
			}
		}
	    row_new.itemNo = parseInt(itemNo) + 1;
	    row_new.id=null;
		row_new.orderQty = null;
		row_new.publishStatus=0;
		row_new.confirmStatus=0;
		row_new.deliveryStatus=0;
		row_new.receiveStatus=0;
		row_new.closeStatus=0;
		var index_new = data.length;
		$datagrid.datagrid('insertRow', {
			index: index_new,
			row: row_new
		});
		$datagrid.datagrid('clearSelections');
		$datagrid.datagrid('selectRow', index_new).datagrid('beginEdit',index_new);
	},
   removeit : function (datagridId){
		datagridId = '#' + datagridId;
	   	var row = $(datagridId).datagrid('getSelected');
	   	var rows = $(datagridId).datagrid('getRows');
	   	if(row.length == 0){ //当前没有选择任何记录
	   		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			return false;
	   	}else if(rows.length ==1 && row){ //只存在一条数据并且被选择时，不能删除原始数据
	   		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message20'),'error');
	   		 return; 
	   	}
	   	else if(rows.length >1 &&  row.publishStatus !=0){ //当前选中的数据已经发布就不能删除
	   		//包含已发布数据不能删除
	   		$.messager.alert($.i18n.prop('label.remind'),"包含已发布的数据不能删除",'error');
	   		 return;
	   	}else if(rows.length >1 && row){
	   	         var index=$(datagridId).datagrid('getRowIndex',row);
	   	        
	   	      if(null!=row.id){
	   				var params = $.toJSON(row);
	   				//存在除选中的记录之外还有未发布的记录的情况时，可以进行删除
	   				$.messager.confirm('提示','确定要删除吗？',function(r){
						if(r){
							$.ajax({
								url:ctx+'/manager/order/purchaseorder/deletePlan',
								type:'POST',
								data:params,
								dataType:"json",
								contentType : 'application/json',
								success:function(data){
									if(data.success){ 
										$.messager.show({
											title:'消息',
											msg:data.msg,
											timeout:2000,
											showType:'show',
											style:{
												right:'',
												top:document.body.scrollTop+document.documentElement.scrollTop,
												bottom:''
											}
										});
										$('#datagrid-orderitemplan-list').datagrid('reload');
									}else{
										$.messager.alert('提示',data.msg,'error');
								}
								}
							});
						}
					});		
	   			}else{
	   				//新增的直接页面上删除
	   			 $(datagridId).datagrid('deleteRow',index);
	   			}
	   	}
   }
};


function clone(myObj){
	if(typeof(myObj) != 'object') return myObj;  
	if(myObj == null) return myObj;  
	var myNewObj = new Object();  
	for(var i in myObj){
	myNewObj[i] = clone(myObj[i]);  
	} 
	return myNewObj;  
	}