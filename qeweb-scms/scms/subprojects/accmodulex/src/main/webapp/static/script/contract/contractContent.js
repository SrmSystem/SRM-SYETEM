/**
 * 合同模板-条款
 */
var curSeledItemId = null;  //正在编辑行的id
var ContractContent = {
	onContextMenu : function (e,row){
		e.preventDefault();
		$(this).treegrid('select',row.id);
		$('#module-item-opt').menu('show',{
			left:e.pageX,
			top:e.pageY	
		});
	},
	onHeaderContextMenu : function (e,row){
		e.preventDefault();
		$('#module-item-head').menu('show',{
			left:e.pageX,
			top:e.pageY	
		});
	},
	/**
	 * 打开新增条款页面
	 * @type 0:平级 1:下级
	 */
	addNewItem : function (type){
		//赋值
		var selectedItem = $('#treegrid-item-list').treegrid('getSelected');
		//如果为平级节点
		if(type==0){
			$('#parentId').val(selectedItem._parentId);
			$('#beforeId').val(selectedItem.id);
		}else if(type==1){
			$('#parentId').val(selectedItem.id);
			$('#beforeId').val(0);
		}else if(type==3){
			$('#parentId').val(0);
			$('#beforeId').val(0);
		}

		$('#moduleItemId').val('0');
		var contractId = $('#contractId').val();
		var parentId = $('#parentId').val();
		var beforeId = $('#beforeId').val();
		var contentId = $('#contentId').val();
		$.post(ctx + '/manager/contract/contractContent/createContentCode',
				{"contractId": contractId, "parentId": parentId, "beforeId": beforeId,"contentId":contentId},function(data){
			$('#form-item-add').form("load",data);
		},"json");
		
		$('#win-item-add').window('open');
	},

	/**
	 * 复制新增条款
	 */
	copyItem : function (){
		var selectedItem = $('#treegrid-item-list').treegrid('getSelected');
		delete selectedItem.itemList;
		$.ajax({
			url:ctx+'/manager/contract/contractContent/copyContent',
			type:'POST',
			data:selectedItem,
			dataType : 'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
					$('#treegrid-item-list').treegrid('reload');
				}
			}
		});
	},
	/**
	 * 提交新增条款
	 */
	submitAddItem : function (){
		$('#form-item-add').form('submit',{
			ajax:true,
			iframe: true,   
			url:ctx+'/manager/contract/contractContent/saveContent',
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
					$('#treegrid-item-list').treegrid('reload');
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
				$('#win-item-add').window("close");
			}
		});
	},
	/**
	 * 编辑条款
	 */
	editItem : function(){
		var selected = $('#treegrid-item-list').treegrid('getSelected');
		var selectedId = selected.id;
		//如果不等于正在编辑的行，则询问
		/*if(curSeledItemId!=null && selectedId!=curSeledItemId){
			$.messager.confirm('提示','有未保存的编辑,您要保存后编辑吗？',function(r){
				if(r){
					updateItem();
				}else{
					$('#treegrid-item-list').treegrid('cancelEdit',curSeledItemId);
				}
				$('#treegrid-item-list').treegrid('beginEdit',selectedId);
				curSeledItemId = selectedId;
			});
		}else{
			$('#treegrid-item-list').treegrid('beginEdit',selectedId);
			curSeledItemId = selectedId;
		}*/
		$('#win-item-add').window({
			iconCls:'icon-edit',
			title:'编辑条款'
		});
		$('#win-item-add').window('open');
		$('#form-item-add').form('load',ctx+'/manager/contract/contractContent/getContentById/'+selectedId);
	},
	/*
	 * 更新条款
	 */
	/*updateItem : function(itemId){
		$('#treegrid-item-list').treegrid('endEdit',curSeledItemId);
		var	item = $('#treegrid-item-list').treegrid('find',curSeledItemId)
		if(itemId!=null){
			item = $('#treegrid-item-list').treegrid('find',itemId)
		}
		//删除子集
		delete item.itemList;
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$.ajax({
			url:ctx+'/manager/ContractContent/moduleItem/updateModuleItem',
			type:'POST',
			data:item,
			dataType : 'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
					$('#treegrid-item-list').treegrid('reload');
				}
			}
		});
	},*/
	/**
	 * 删除条款
	 */
	deleteItem : function(){
		var selectedItem = $('#treegrid-item-list').treegrid('getSelected');
		$.messager.confirm('提示','确定要删除数据吗！',function(r){
		if(r){
		$.ajax({
			url :ctx + '/manager/contract/contractContent/deleteContent',
			data : {id:selectedItem.id},
			dataType : 'json',
			type : 'POST',
			success : function(data){
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
					$('#treegrid-item-list').treegrid('reload');
				}
			}
		});
		}
		});
	},
	/*
	 * 移动菜单位置
	 * @id 移动的菜单ID
	 * @moveType 移动类型 0：上移，1：下移
	 */ 
	move : function(id,moveType){
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$.ajax({
			url:ctx+'/manager/contract/contractContent/moveContent',
			type:'POST',
			data:{id:id,moveType:moveType},
			dataType : 'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					//$.messager.alert('提示','更新成功','info');
					$('#treegrid-item-list').treegrid('reload');
				}
			}
		}); 
	},
	/** 上移下移列的渲染 */
	fmt_upAndDown : function(v,r,i){
		return '<a class="wait-up" href="javascript:;" onclick="ContractContent.move('+r.id+',0)"></a><a href="javascript:;" class="wait-down" onclick="ContractContent.move('+r.id+',1)"></a>';
	},		 
	/** 树状grid加载后对上移下移列的渲染 */
	gridLoadSuc : function (data){
		$('.wait-up').linkbutton({
			iconCls:'icon-arrow_up',
			plain:true
		});
		$('.wait-down').linkbutton({
			iconCls:'icon-arrow_down',
			plain:true
		});
	},
	openItemCtp : function(id){
		window.open(ctx + '/manager/contract/contractContent/getContentEasyuiTree?contractId='+ id);
	  },
	//生成pdf
	 createPdf : function(moduleId,type){
		 var sourse ="";
		 $('#form_module_item').form('submit',{  
				url: ctx + '/manager/contractmoduleItem/moduleItem/buildPDF/'+moduleId+'/'+type,
				onSubmit:function(param){
					param.source = sourse;  
				},
				success:function(data){
					$.messager.progress('close');
				}
			});
	  }
}