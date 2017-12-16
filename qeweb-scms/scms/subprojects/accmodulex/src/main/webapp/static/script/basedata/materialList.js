var MaterialType = {
	contextMenu : function(e,node){
		e.preventDefault();
		$('#tree-materialType').tree('select',node.target);
		if(node.attributes.orgType==0){
			$('#menu-materialtype-edit').css('display','none');
			$('#menu-materialtype-del').css('display','none');
		}else{
			$('#menu-materialtype-edit').css('display','');
			$('#menu-materialtype-del').css('display','');
		}
		//判断是否有叶子节点控制
		var leafLevel = $('#leafLevel').val();
		var $lowBtn = $('#menu-materialtype-add-low');
		if(leafLevel!='' && leafLevel == node.attributes.levelLayer){
			$lowBtn.css('display','none');
		}else{
			$lowBtn.css('display','');
		}
		
		$('#menu-materialtype').menu('show',{
			left:e.pageX,
			top:e.pageY
		});
	},
	getSelected : function(){
		var selectedNode = $('#tree-materialType').tree('getSelected');
		return selectedNode;
	},
	getSelected_category : function(){
		var selectedNode = $('#tree-materialType-category').tree('getSelected');
		return selectedNode;
	},
	textFmt:function(node){
//		var importanceMap = {'0':'次要','1':'重要'};
		var importanceMap = {'0':$.i18n.prop('vendor.basedataJs.secondary'),'1':$.i18n.prop('vendor.basedataJs.important')};
		if(node.attributes!=null && node.attributes.importance!=null)
		return node.text+'('+importanceMap[node.attributes.importance]+')';
		else
		return node.text;
	},
	nodeClick : function(node){
		//调用grid加载数据
		$('#form-material-search').find('input[name="search-EQ_materialTypeId"]').val(node.id);
		Material.search();
	},
	add : function(type){
		$("#materialType-id").val("0");
		var $form = $('#form-materialType');
		$form.form('reset');
		$('#materialType-code').textbox('enable');
		$('#win-materialType').window({
			title:$.i18n.prop('vendor.basedataJs.NewAdd'),/*'新增'*/
			iconCls:'icon-add'
		})
		//赋值
		var selectedNode = $('#tree-materialType').tree('getSelected');
		//如果为平级节点
		if(type==0){
			$('#materialType-parentId').val(selectedNode.attributes.parentId);
			$('#materialType-beforeId').val(selectedNode.id);
		}else if(type==1){
			$('#materialType-parentId').val(selectedNode.id);
			$('#materialType-beforeId').val(0);
		}
		var $win = $('#win-materialType');
		$win.window('open');
		$win.window('autoSize');
		$win.getCmp('type').val(type);
	},
	edit : function(){
		$('#win-materialType').window({
			title:$.i18n.prop('vendor.basedataJs.Edit'),/*'编辑'*/
			iconCls:'icon-edit'
		})
		
		var selectedNode = $('#tree-materialType').tree('getSelected');
		$('#form-materialType').form('load',{
			id : selectedNode.id,
			name : selectedNode.text,
			code : selectedNode.attributes.code,
			parentId : selectedNode.attributes.parentId
			
		});
		$('#materialType-code').textbox('disable');
		var $win = $('#win-materialType');
		$win.window('open');
		$win.window('autoSize');
		$win.getCmp('type').val(type);
	},
	del : function(){
		var $tree = $('#tree-materialType');
		var selectedNode = $tree.tree('getSelected');
		var pNode = $tree.tree('getParent',selectedNode.target);
		var id = selectedNode.id;
		$.ajax({
			url:ctx+'/manager/basedata/materialType/delete',
			type:'POST',
			data:{id:id},
			dataType:'json',
			success:function(data){
				if(data.success){
					$.messager.show({
						title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
						msg:$.i18n.prop('vendor.basedataJs.DeleteMaterial'),/*'删除物料分类成功'*/
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$tree.tree('reload',pNode.target);
				}
			}
		});
	},
	deleteFalse : function(){
		var $tree = $('#tree-materialType');
		var selectedNode = $tree.tree('getSelected');
		var pNode = $tree.tree('getParent',selectedNode.target);
		var id = selectedNode.id;
		$.ajax({
			url:ctx+'/manager/basedata/materialType/abolishBatch',
			type:'POST',
			data:{id:id},
			dataType:'json',
			success:function(data){
				$('#tree-materialType-category').tree('reload');
				$('#tree-materialType').tree('reload');
				if(data.success){
					$.messager.show({
						title:$.i18n.prop('vendor.basedataJs.News'),/*'消息'*/
						msg:$.i18n.prop('vendor.basedataJs.TovoidMaterialClassSuccess')/*'作废物料分类成功'*/,
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#tree-materialType-category').tree('reload');
				}
			},
			error: function(data) {
				$.messager.progress('close');
			}
			
		});
	},
	submit : function(){
		var url = ctx+'/manager/basedata/materialType/addNewMaterialType';
//		var sucMeg = '添加物料类型成功！';
		var sucMeg = $.i18n.prop('vendor.basedataJs.AddMaterialTypeSuccess');
		var id = $('#materialType-id').val();
		if(id!=0 && id!='0'){
			url = ctx+'/manager/basedata/materialType/update';
//			sucMeg = '编辑物料分类成功！';
			sucMeg = $.i18n.prop('vendor.basedataJs.EditMaterialClassSuccess');
		}
		$.messager.progress({
			title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
			msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
		});
		$('#form-materialType').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
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
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');/*提示*/
					var $win = $('#win-materialType');
					$win.window('close');
					var type = $win.getCmp('type').val();
					var selectedNode = MaterialType.getSelected(); 
					if(type==1){
						$('#tree-materialType').tree('reload',selectedNode.target);
					}else if(type==0){
						var parentNode = $('#tree-materialType').tree('getParent',selectedNode.target);
						if(parentNode.id==0 || parentNode.id=='')
							$('#tree-materialType').tree('reload');
						else	
						   $('#tree-materialType').tree('reload',parentNode.target);
					}
					$('#tree-materialType-category').tree('reload');
				}else{
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');/*提示*/
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
				}
			}
			
		});
	},
	importType : function(dialogId){//导入分类
		var $dialog = $('#'+dialogId);
		$dialog.dialog('open');
		$dialog.dialog('autoSize');
		$dialog.dialog('center');
	},
	importSubmit : function(dialogId,refreshId){//导入提交
		var $dg = $('#'+dialogId);
		var $form = $dg.getCmp('form');
		$.messager.progress();
		$form.form('submit',{
			url : ctx+'/manager/basedata/materialType/import',
			onSubmit : function(){
				var flag = $(this).form('validate');
				if(!flag){
					$.messager.progress('close');
				}
				return flag;
			},
			success : function(data){
				$.messager.progress('close');
				data = $.parseJSON(data);
				if(data.success){
//					$.messager.alert('提示','导入成功','info');
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.ImportSuccess'),'info');
					$dg.dialog('close');
					$('#'+refreshId).tree('reload');
				}else{
//					$.messager.alert('提示',data.msg + "<br>导入日志请参阅<a onclick='File.showLog(\"" + data.log + "\")'><b>日志文件</b></a>" ,'error');
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data.msg + "<br>"+$.i18n.prop('vendor.basedataJs.improtLogRead')+"<a onclick='File.showLog(\"" + data.log + "\")'><b>"+$.i18n.prop('vendor.basedataJs.LogFille')+"</b></a>" ,'error');
				}
			}
		});
		
	}
};


var Material = {
		editPackage:function(){
			var selections = $('#datagrid-material-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
				return false;
			}
			var param='';
			for(var i=0;i<selections.length;i++){
				param=param+selections[i].id+',';
			}
			
			$('#win-package-addoredit').dialog({
				iconCls:'icon-add',
				title:$.i18n.prop('vendor.basedataJs.MaintainPackNumber')/*'维护包装数'*/
			});
			$('#form-package-addoredit').form('clear');
			$('#win-package-addoredit').dialog('open');
			 
			 $('#vendorMaterialRelIds').val(param);
		},
		submitPackage : function(){
			var url = ctx+'/manager/basedata/material/submitPackage/'+ $('#vendorMaterialRelIds').val();
			var sucMeg = $.i18n.prop('vendor.basedataJs.EditSuccess')/*'编辑成功！'*/;
		
			$.messager.progress({
				title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
				msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
			});
			$('#form-package-addoredit').form('submit',{
				ajax:true,
				url:url,
				onSubmit:function(){
					var isValid = $(this).form('validate');
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
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');
						$('#win-package-addoredit').dialog('close');
						$('#datagrid-material-list').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');
					}
					}catch (e) {
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');
					}
				}
				
			});
		},
		
	operateFmt : function(v,r,i){
		var materialTypeName = '';
		if(r.materialType!=null)
			materialTypeName = r.materialType.name;
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Material.edit('+r.id+','+r.materialTypeId+',\''+materialTypeName+'\');">'+$.i18n.prop('vendor.basedataJs.Edit')+'</a>';/*编辑*/
	},	
	search : function(){
		var searchParamArray = $('#form-material-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-material-list').datagrid({url: ctx+'/manager/basedata/material',
			queryParams : searchParams
		});
	},
	search_category:function(){
		var searchParamArray = $('#form-material-search-category').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-material-list-category').datagrid('load',searchParams);
	},
	category : function(){
		//先判断是否有选中的分类节点
		var node = MaterialType.getSelected_category();
		if(node==null || node.id==''){
//			$.messager.alert('提示','请先选择一个分类','info');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.FirstChoiseClass'),'info');
			return;
		}
		//获得选中的物料
		var materialArray = $('#datagrid-material-list-category').datagrid('getSelections');
		if(materialArray==null || materialArray.length<=0){
//			$.messager.alert('提示','请选择要分类的物料','info');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.ChoiseMaterialClass'),'info');
			return;
		}
		var materialTypeId = node.id;
		var materialIds = '';
		$.each(materialArray,function(i,n){
			materialIds+=n.id+',';
		});
		$('#form-category-submit').find('input[name="materialIds"]').val(materialIds);
		$('#form-category-submit').find('input[name="materialTypeId"]').val(materialTypeId);
		$.messager.progress({
			title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
			msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
		});
		$('#form-category-submit').form('submit',{
			url : ctx+'/manager/basedata/material/category',
			ajax:true,
			success:function(data){
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){
//						$.messager.alert('提示','分类成功','info');
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.SuccessfulClass'),'info');
						$('#datagrid-material-list-category').datagrid('reload');
					}else{
//						$.messager.alert('提示','分类失败','error');
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.FaillyClass'),'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'), data,'error');
				}
			}
			
		});
		
	},
	add : function(){
		
		var materialType = MaterialType.getSelected();
		if(materialType==null){
//			$.messager.alert('提示','请先在左边选择一个分类','warning');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.ChoiseLeftClass'),'warning');
			return;
		}
		
		//先判断是否有叶子节点控制
		var leafLevel = $('#leafLevel').val();
		var noleafAllow = $('#noleafAllow').val();
		if(leafLevel!='' && materialType.attributes == null){
//			$.messager.alert('提示','请选择一个'+leafLevel+'级分类','warning');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.PleaseTakeONE')+leafLevel+$.i18n.prop('vendor.basedataJs.classification'),'warning');
			return;
		}
		if(leafLevel!='' && materialType.attributes.levelLayer!=leafLevel && noleafAllow=='false'){
//			$.messager.alert('提示','请选择一个'+leafLevel+'级分类','warning');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.PleaseTakeONE')+leafLevel+$.i18n.prop('vendor.basedataJs.classification'),'warning');
			return;
		}
		//给其他值置空
		$('#form-material-opt').getCmp('code').textbox("setValue","");
		$('#form-material-opt').getCmp('name').textbox("setValue","");
		$('#form-material-opt').getCmp('describe').textbox("setValue","");
		$('#form-material-opt').getCmp('picStatus').textbox("setValue","");
		$('#form-material-opt').getCmp('technician').textbox("setValue","");
		$('#dialog-material-opt').dialog({title:$.i18n.prop('vendor.basedataJs.AddMaterial')});/*新增物料*/
		$('#dialog-material-opt').dialog('open');
		//给分类赋值
		$('#form-material-opt').getCmp('materialTypeId').val(materialType.id);
		$('#form-material-opt').getCmp('materialType').html(materialType.text);
		//给零部件赋值
//		$('#form-material-opt').getCmp('partsCode').textbox("setValue",materialType.attributes["code"]);
//		$('#form-material-opt').getCmp('partsName').textbox("setValue",materialType.text);
		$('#form-material-opt').getCmp('partsCode').val(materialType.attributes["code"]);
		$('#form-material-opt').getCmp('partsName').val(materialType.text);
		//重设对话框高宽
		$('#dialog-material-opt').dialog('autoSize');
	},
	deleteFalse : function(){
		var selections = $('#datagrid-material-list').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
			return false;
		}
		for(var i=0;i<selections.length;i++){
			if(selections[i]["abolished"]=='1'){
//				$.messager.alert('提示','存在已作废的记录！','info');
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.ExistTovoidRecord'),'info');
				return false;
			}
		}
		var params = $.toJSON(selections);
//		$.messager.confirm('提示','确定作废操作？',function(r){
		$.messager.confirm($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.MakeSureTovoidOperate'),function(r){
			if(r){
				$.messager.progress({
					title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
					msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/material/abolishBatchVendorMaterialRel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
							msg:$.i18n.prop('vendor.basedataJs.TovoidOperateSuccess')/*'作废操作成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						
						//$.messager.alert('提示','删除用户成功!','info');
						$('#datagrid-material-list').datagrid('reload');
					},
					error: function(data) {
						$.messager.progress('close');
						$.messager.fail(data.responseText);
					}
				});
			}
			
			
		});
	},
	effect : function(){
		var selections = $('#datagrid-material-list').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
			return false;
		}
		var params = $.toJSON(selections);
//		$.messager.confirm('提示','确定要生效该数据吗？<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){
		$.messager.confirm($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.SureTakeEffectDate')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
//						$.messager.alert('提示','存在生效的记录！无法重复生效 ！','info');
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.RecordOfEexistenceItNoRepeated'),'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/material/effectBatch',
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType:"json",
					success:function(data1){
						$.messager.progress('close');
						try{
							var result = data1;
							if(result.success){
								$.messager.show({
									title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
									msg:$.i18n.prop('vendor.basedataJs.TakeEffectionSuccess'),/*生效数据成功*/
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-material-list').datagrid('reload');
							}else{
								$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');
							}
							}catch (e) {
								$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');
							}	
						
						
					}
				});
			}
			
			
		});
		
		
	},
	
	
	
	
	
	
	
	edit : function(id,typeId,typeName){
		$('#dialog-material-opt').dialog({title:$.i18n.prop('vendor.basedataJs.EditMaterial')});/*"编辑物料"*/
		$('#dialog-material-opt').dialog('open');
		//从后台获取数据
		$('#form-material-opt').form('load',ctx+'/manager/basedata/material/getMaterial/'+id);
		//给分类赋值
		$('#form-material-opt').getCmp('materialTypeId').val(typeId);
		$('#form-material-opt').getCmp('materialType').html(typeName);
		//重设对话框高宽
		$('#dialog-material-opt').dialog('autoSize');
	},
	del : function(){
		var selections = $('#datagrid-material-list').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/basedata/material/deleteMaterial',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
					$.messager.show({
						title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
						msg:$.i18n.prop('vendor.basedataJs.DeleteMaterialOk'),/*删除物料成功*/
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-material-list').datagrid('reload');
			}
		});
	},
	submit : function(){
		var $form = $('#form-material-opt');
		var $id = $form.getCmp('id');
		var url = ctx+'/manager/basedata/material/addNew';
		var tip = $.i18n.prop('vendor.basedataJs.NEWaddOK');/*新增成功*/
		if($id.val()!=-1){
			url = ctx+'/manager/basedata/material/update';
			tip = $.i18n.prop('vendor.basedataJs.UpdataOK');/*更新成功*/
		}
		$.messager.progress({
			title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
			msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
		});
		$form.form('submit',{
			url : url,
			onSubmit : function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success : function(data){
				$.messager.progress('close');
				try{
					data = JSON.parse(data);
					if(data.success){
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),tip,'info');
						$('#datagrid-material-list').datagrid('reload');
						$('#dialog-material-opt').dialog('close');
					}else{
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data.msg  ,'error');
					}
				}catch (e) {  
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');
				}
			}
		});
		
	},
	importMaterial : function(dialogId){//导入分类
		var $dialog = $('#'+dialogId);
		var $form = $dialog.getCmp('form');
		$form.form("clear");
		$dialog.dialog('open');
		$dialog.dialog('autoSize');
		$dialog.dialog('center');
	},
	importSubmit : function(dialogId,refreshId){//导入提交
		var $dg = $('#'+dialogId);
		var $form = $dg.getCmp('form');
		$.messager.progress();
		$form.form('submit',{
			url : ctx+'/manager/basedata/material/import',
			onSubmit : function(){
				var flag = $(this).form('validate');
				if(!flag){
					$.messager.progress('close');
				}
				return flag;
			},
			success : function(data){
				$.messager.progress('close');
				try{
					data = $.parseJSON(data);
					if(data.success){
//						$.messager.alert('提示','导入完成' ,'info');
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.ImprotComplent') ,'info');
						$dg.dialog('close');
						$('#'+refreshId).datagrid('reload');
					}else{
//						$.messager.alert('提示',data.msg + "<br>导入日志请参阅<a onclick='File.showLog(\"" + data.log + "\")'><b>日志文件</b></a>" ,'error');
						$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data.msg + "<br>"+$.i18n.prop('vendor.basedataJs.improtLogRead')+"<a onclick='File.showLog(\"" + data.log + "\")'><b>"+$.i18n.prop('vendor.basedataJs.LogFille')+"</b></a>" ,'error');
					}
/*					if(data.success){
						$.messager.alert('提示','导入完成'+ "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + data.logPath + "\")'><b>日志文件</b></a>" ,'info');
						$.messager.alert('提示','导入完成' ,'info');
						$dg.dialog('close');
						$('#'+refreshId).datagrid('reload');
					}
					else
					{
						$.messager.alert('提示',data.msg,'error');
					}*/
				}catch(e) {
					$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
				}
			}
			
		});
		
	}
};

function searchUser(){
	var searchParamArray = $('#form-user-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-user-list').datagrid('load',searchParams);
}

function addUser(){
	$('#win-user-addoredit').window({
		iconCls:'icon-add',
		title:$.i18n.prop('vendor.basedataJs.NewAddUSER')/*'新增用户'*/
	});
	$('#loginName').textbox('enable');
	$('#form-user-addoredit').form('clear');
	$('#id').val(0);
	$('#win-user-addoredit').window('open');
}

function submitAddorEditUser(){
	var url = ctx+'/manager/admin/user/addNewUser';
//	var sucMeg = '添加用户成功！';
	var sucMeg = $.i18n.prop('vendor.basedataJs.AddUserOK');
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/admin/user/update';
//		sucMeg = '编辑用户成功！';
		sucMeg = $.i18n.prop('vendor.basedataJs.EditUserOK');
	}
	$.messager.progress({
		title:$.i18n.prop('vendor.basedataJs.Promit'),/*'提示'*/
		msg : $.i18n.prop('vendor.basedataJs.Submission')/*'提交中...'*/
	});
	$('#form-user-addoredit').form('submit',{
		ajax:true,
		url:url,
		onSubmit:function(){
			var isValid = $(this).form('validate');
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
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),sucMeg,'info');/*提示*/
				$('#win-user-addoredit').window('close');
				$('#datagrid-user-list').datagrid('reload');
			}else{
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');/*提示*/
			}
			}catch (e) {
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
			}
		}
		
	});
}

function deleteUser(){
	var selections = $('#datagrid-user-list').datagrid('getSelections');
	if(selections.length==0){
//		$.messager.alert('提示','没有选择任何记录！','info');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url:ctx+'/manager/admin/user/deleteUser',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
			if(data.success){
				$.messager.show({
					title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
					msg:$.i18n.prop('vendor.basedataJs.DeleteUserOK')/*'删除用户成功'*/,
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});

				//$.messager.alert('提示','删除用户成功!','info');
				$('#datagrid-user-list').datagrid('reload');
			}
		}
	});
}

function editUser(id){
	$('#win-user-addoredit').window({
		iconCls:'icon-edit',
		title:$.i18n.prop('vendor.basedataJs.EditUser')/*'编辑用户'*/
	});
	$('#loginName').textbox('disable');
	$('#win-user-addoredit').window('open');
	$('#form-user-addoredit').form('load',ctx+'/manager/admin/user/getUser/'+id);
}

$(function(){
	
	
	
	
});
