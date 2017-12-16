var PerTemplate = {
	/** 列表操作列格式化 */
	optFmt : function(v,r,i){
		return '<a class="btn-link" href="'+ctx+'/manager/vendorperformance/template/toEdit?templateId='+r.id+'">编辑</a>';
	},
	sourceTypeFmt : function(v,r,i){
		if(v=='0'){
			return '维度';
		}else if(v=='1'){
			return '指标';
		}else if(v=='2'){
			return '模版';
		}
	},
	/** 模版设置的操作列格式化 */
	setEditorFmt : function(v,r,index){
		var optHtml = '';
		var enableText = "禁用";
		if(r.enableStatus == 0){
			enableText = "启用";
		}
		var weightType = $('#form-template').getCmp('weight').combobox('getValue');
		//未被禁用和为手动模式才能编辑
		if(r.enableStatus == 1 && (weightType==1||weightType==3)){
		optHtml += '<span class="settingOpt">';
		if(r.editing){
			optHtml+='<button class="btn-link" onclick="TreeGridEditor.rowEditSave(this,\'#treegrid-setting\','+r.id+')">保存</button>';
			optHtml+='<button class="btn-link" onclick="TreeGridEditor.rowEditCancel(this,\'#treegrid-setting\','+r.id+')">取消</button>';
		}else{
			optHtml+='<button class="btn-link" onclick="TreeGridEditor.rowEdit(this,\'#treegrid-setting\','+r.id+')">编辑</button>';
		}
		optHtml+='</span>';
		}
		if(!r.editing){
		var enableOpt = '<button class="btn-link enableOpt" onclick="PerTemplate.enableSetting(this,'+r.id+')">'+enableText+'</button>';
		optHtml += enableOpt;
		}
		return optHtml;
	},
	/** 禁用和启用设置项 */
	enableSetting : function(optObj,id){
		var node = $('#treegrid-setting').treegrid('find',id);
		//校验该层级不允许全部禁用
		var isAllUn = true;
		if(node._parentId==null){
			var roots = $('#treegrid-setting').treegrid('getRoots');
			$.each(roots,function(i,n){
				if((n.id != id && node.enableStatus==1 && n.enableStatus==1) || node.enableStatus==0){
					isAllUn = false;
					return false;
				}
				
			});
		}else{
			var children = $('#treegrid-setting').treegrid('getChildren',node._parentId);
			$.each(children,function(i,n){
				if((n.id != id && node.enableStatus==1 && n.enableStatus==1) || node.enableStatus==0){
					isAllUn = false;
					return false;
				}
			});
		}
		if(isAllUn){
			$.messager.alert('提示','该层级不允许全部禁用','warning');
			return;
		}
		var enableV = 0;
		var enableText = '启用';
		if(node.enableStatus==0){
			enableV = 1;
			enableText = '禁用';
		}
		node.enableStatus = enableV;
		PerTemplate.enableTog(node);
		$(optObj).html(enableText);
		//如果有上级，又是做启用操作，如果上级为禁用，必须递归开启启用状态
		if(node._parentId!=null && node.enableStatus==1){
			PerTemplate.enableParent(node);
		}
		
		var weightType = $('#form-template').getCmp('weight').combobox('getValue');
		if(weightType==0){
			PerTemplate.weightSelect({value:weightType});
		}
	},
	/** 递归启用父节点 */
	enableParent : function(children){
		var parent = $('#treegrid-setting').treegrid('find',children._parentId);
		if(parent.enableStatus==0){
			$('#treegrid-setting').treegrid('update',{
				id : parent.id,
				row : {
					enableStatus : 1			
				}
			});
			if(parent._parentId!=null){
				PerTemplate.enableParent(parent);
			}
		}
	},
	/** 禁用启用都是递归处理 */
	enableTog : function(parent){
		var children = $('#treegrid-setting').treegrid('getChildren',parent.id);
		if(children.length>0){
			$.each(children,function(i,n){
				n.enableStatus = parent.enableStatus;
				PerTemplate.enableTog(n);
			});
		}
		$('#treegrid-setting').treegrid('update',{
			id : parent.id,
			row : {
				enableStatus : parent.enableStatus			
			}
		});
	},
	/** 设置行编辑之前 */
	setRowBeforEdit : function(row){
		row.editing = true;
		PerTemplate.updateSetActions(row);
	},
	/** 设置行编辑之后 */
	setRowAfterEdit : function(row){
		row.editing = false;
		PerTemplate.updateSetActions(row);
	},
	/** 设置行编辑取消 */
	setRowCancelEdit : function(row){
		row.editing = false;
		PerTemplate.updateSetActions(row);
	},
	/** 更新行操作 */
	updateSetActions : function(row){
		$('#treegrid-setting').treegrid('update',{
			id : row.id,
			row : {}
		});
	},
	/** 进入新增界面 */
	addNew : function(targetId){
		$(targetId).dialog({
			onOpen:function(){
				//$(targetId).dialog('autoSize',{w:30});
			}
		});
		$(targetId).dialog('open');
		$('#template-form').form('reset');
	},
	/** 新增的提交 */
	submit : function(){
		var $dialog = $('#dialog-adder');
		var $form = $dialog.getCmp('dialog-adder-form');
		$.messager.progress();
		$form.form('submit',{
			url : ctx+'/manager/vendorperformance/template/addNew',
			success : function(data){
				$.messager.progress('close');
				try{
					data  = $.parseJSON(data);
					if(data.success){
						window.location.href = ctx+'/manager/vendorperformance/template/toEdit?templateId='+data.templateId
					}else{
						$.messager.alert('提示',data.msg,'error');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			},
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},
	/** 权重改变时的处理0-平均1-手动 */
	weightSelect : function(r){
		var rootsAll = $('#treegrid-setting').treegrid('getRoots');
  		var roots = [];
  		$.each(rootsAll,function(i,n){
  			if(n.enableStatus==1)
  				roots.push(n);
  		});
	  	if(r.value==0){
	  		PerTemplate.setAverageWeight(roots);
	  	}else if(r.value==1){
	  		PerTemplate.setAverageWeight(roots);
	  	}
	  	else if(r.value==2)
  		{
	  		PerTemplate.setAverageWeight2(roots);
  		}
	  	else
	  	{
	  		PerTemplate.setAverageWeight3(roots);
	  	}
	},
	/** 设置平均权重 */
	setAverageWeight : function(parents){		
  		var size = parents.length;
  		var weight = 100/size;
  		//第一层的代码更新
  		$.each(parents,function(i,n){
  			var childsAll = $('#treegrid-setting').treegrid('getChildren',n.id);
  			var childs = [];
  			$.each(childsAll,function(i,n){
	  			if(n.enableStatus==1)
	  				childs.push(n);
	  		});
  			if(childs.length>0){
  				PerTemplate.setAverageWeight(childs);
  			}
  			$('#treegrid-setting').treegrid('update',{
  				id : n.id,
  				row : {
  					weightNumber : weight
  				}
  			});
  		});
	},
	setAverageWeight2 : function(parents){		
		var size = parents.length;
		var weight = 100/size;
		//第一层的代码更新
		$.each(parents,function(i,n){
			var childsAll = $('#treegrid-setting').treegrid('getChildren',n.id);
			var childs = [];
			$.each(childsAll,function(i,n){
				if(n.enableStatus==1)
					childs.push(n);
			});
			if(childs.length>0){
				PerTemplate.setAverageWeight2(childs);
			}
			$('#treegrid-setting').treegrid('update',{
				id : n.id,
				row : {
					weightNumber : 100
				}
			});
			
		});
	},
	setAverageWeight3 : function(parents){		
		var size = parents.length;
		var weight = 100/size;
		//第一层的代码更新
		$.each(parents,function(i,n){
			var childsAll = $('#treegrid-setting').treegrid('getChildren',n.id);
			var childs = [];
			$.each(childsAll,function(i,n){
				if(n.enableStatus==1)
					childs.push(n);
			});
			if(childs.length>0){
				PerTemplate.setAverageWeight3(childs);
			}
			$('#treegrid-setting').treegrid('update',{
				id : n.id,
				row : {
					weightNumber : 100
				}
			});
			
		});
	},
	/** 弹出维度设置的对话框 */
	openDim : function(targetId){
		$(targetId).dialog('open');
		$(targetId).dialog('autoSizeMax');
	},
	/** 模版设置的提交 */
	updateSubmit : function(){
		//基本信息校验
		var validFlag = $('#form-template').form('validate');
		if(!validFlag){
			return false;
		}
		$('#treegrid-setting').treegrid('selectAll');
		var nodesTemp = $('#treegrid-setting').treegrid('getSelections');
		$.each(nodesTemp,function(i,n){
			if(n.editing){
				$('#treegrid-setting').treegrid('endEdit',n.id);
			};
			
		});
		var nodes = $.map(nodesTemp, function (n) {n.template=null;n.children=null; return n;});
		$('#treegrid-setting').treegrid('unselectAll');
		//校验权限比重。如果为手动时才校验
		var validText = PerTemplate.validateWeight();
		if(validText!=''){
			$.messager.alert('提示',validText,'warning');
			return false;
		}
		var templateFields = $('#form-template').serializeArray();
		var template = $.jqexer.formToJson(templateFields);
		
		$.each(nodes,function(i,n){
			for(var f in n){
	        if(f!='template')
			template['settingList['+i+'].'+f] = n[f];
			}
			
		});
		$.messager.progress();
		$.ajax({
			url : ctx+'/manager/vendorperformance/template/settingUpdate',
		    data : template,
		    method : 'post',
		    dataType : 'json',
		    success : function(data){
		    	$.messager.progress('close');
		    	if(data.success){
		    		$.messager.success();
		    		window.location.href = ctx+'/manager/vendorperformance/template';
		    	}else{
		    		$.messager.fail(data.msg);
		    	}
		    },
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	/** 校验权重 */
	validateWeight : function(){
		var weightType = $('#form-template').getCmp('weight').combobox('getValue');
		if(weightType==0||weightType==2||weightType==3)return '';
		//如果为手动，开始从根节点检查起。
		var roots = $('#treegrid-setting').treegrid('getRoots');
	    var validText = PerTemplate.reValidateWeight(roots);
	    return validText;
		
	},
	/** 递归判断权重 */
	reValidateWeight : function(items){
		var weight = 0;
		var msg = '';
		var ki=0;
		$.each(items,function(i,n){
			if(n.enableStatus==1){
				ki=ki+1;
				weight+=(n.weightNumber==null?0:parseFloat(n.weightNumber));
				//得到子集
				var childrens = $('#treegrid-setting').treegrid('getChildren',n.id);
				if(childrens.length>0){
					msg = PerTemplate.reValidateWeight(childrens);
					if(msg!='')return false;
				}
			}
		});
		var sso=ki*100;
		if(msg!='')return msg;
		if(sso!=weight)
		{
			if(weight!=100){
		    	if(items[0]._parentId == null){
		    		return "最上级维度权重和必须为100";
		    	}else{
		    		var pNode = $('#treegrid-setting').treegrid('find',items[0]._parentId);
		    		return pNode.name+"下的子集权重必须为100";
		    	}
		    }
		}
	    
	    return '';
	},
	deleteList : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length<=0){
			$.messager.alert('提示','请选择记录','warning');
			return;
		}
	    $.ajax({
	    	url : ctx+'/manager/vendorperformance/template/deleteList',
	    	method : 'post',
	    	data : $.toJSON(selections),
	    	contentType : 'application/json',
	    	dataType : 'json',
	    	success : function(data){
	    		if(data.success){
	    			$.messager.success('删除成功');
	    			$('#datagrid').datagrid('reload');
	    		}else{
	    			$.messager.fail(data.msg);
	    		}
	    	},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
	    	
	    });
	},
	abolish:function(abolished){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length<=0){
			$.messager.alert('提示','请选择记录','warning');
			return;
		}
		$.each(selections,function(i,n){
			n.abolished = abolished;
		});
	    $.ajax({
	    	url : ctx+'/manager/vendorperformance/template/' + (abolished == 0 ? 'enableList' : 'cancelList'),
	    	method : 'post',
	    	data : $.toJSON(selections),
	    	contentType : 'application/json',
	    	dataType : 'json',
	    	success : function(data){
	    		if(data.success){
	    			if(abolished==0)
	    			$.messager.success('启用成功');
	    			if(abolished==1)
	    			$.messager.success('废除成功');
	    			$('#datagrid').datagrid('reload');
	    		}else{
	    			$.messager.fail(data.msg);
	    		}
	    	},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
	    	
	    });
	}
	
		
		
}