function Factory(){
	
}

//操作格式化
Factory.operateFmt = function(v,r,i){
	if(r.abolished==0)
	{
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Factory.edit('+r.id+');">编辑</a>';
	}
	else
	{
		return '';
	}
	
}
Factory.vfmt=function(v,r,i){
	return '<button class="btn-link" onclick="Factory.openvfSurvey('+r.id+')">查看</button>';
}
//编辑
Factory.edit = function(id){
	var $win = $('#win-factory-addoredit');
	var $form = $('#factoryTt');
	$form.form({
		onLoadSuccess : function(data){
			if(data.factoryBrandList!=null && data.factoryBrandList.length>0){
				var brandList = [];
				$.each(data.factoryBrandList,function(i,n){
					brandList.push({
						id : n.brand.id,
						brandCode:n.brand.code,
						brandName:n.brand.name
						
					});
					
				});
				var $datagridSelected = $('#datagrid-brand-selected');
				$datagridSelected.datagrid('loadData',{rows:brandList});
				
			}
		}
		
	});
	$win.window({
		iconCls:'icon-edit',
		title:'编辑工厂'
	});
	$form.form('load',ctx+'/manager/basedata/factory/getFactory/'+id);
	$win.window('autoSizeMax');
	$win.window('open');
	
}
Factory.openvfSurvey = function(id) {// org的ID
	$('#win-openvfSurvey').window({
		title:'所拥有的品牌'
	});
	$('#win-openvfSurvey').window('open');
	$('#datagrid-openvfSurvey-list').datagrid('reload',ctx+'/manager/basedata/factory/getBussinessRange/'+id);
}
//查询
Factory.search = function(){
	var searchParamArray = $('#form-factory-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-factory-list').datagrid('load',searchParams);
}

//添加
Factory.add = function(){
	var $win = $('#win-factory-addoredit');
	var $datagrid = $('#datagrid-brand');
	var $datagridSelected = $('#datagrid-brand-selected');
	var $form = $('#factoryTt');
	$win.window({
	    iconCls:'icon-add',
		title:'新增工厂'
	});
	$form.form('clear');
	$datagrid.datagrid('clearSelections');
	$datagridSelected.datagrid('loadData',[]);
	$('#id').val(0);
	$('#code').textbox('enable');
	$win.window('autoSizeMax');
	$win.window('open');
	
}

//提交
Factory.submit = function(){
	var $form = $('#factoryTt');
	var $datagrid = $('#datagrid-brand-selected');
	var url = ctx+'/manager/basedata/factory/addNewFactory';
	var sucMeg = '添加工厂成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/factory/update';
		sucMeg = '编辑工厂成功！';
	}
	//需要组装
	//先验证form的必填
	if(!$form.form('validate'))return false;
	var formParamArray = $form.serializeArray();
	var factory = $.jqexer.formToJson(formParamArray);
	//再拼装品牌集合
	var brandList = [];
	var rowList = $datagrid.datagrid('getRows');
	$.each(rowList,function(i,n){
		brandList.push({brandId:n.id});
	});
	factory.factoryBrandList = brandList;
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	$.ajax({
		url : url,
		method : 'post',
		data : $.toJSON(factory),
		dataType : 'json',
		contentType : 'application/json',
		success : function(data){
			$.messager.progress('close');
			try{
			if(data.success){
				$.messager.alert('提示',sucMeg,'info');
				$('#win-factory-addoredit').window('close');
				$('#datagrid-factory-list').datagrid('reload');
			}else{
				$.messager.alert('提示',data.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data.msg,'error');
			}
		}
	});
	
//	$.messager.progress();
//	$('#form-factory-addoredit').form('submit',{
//		ajax:true,
//		url:url,
//		onSubmit:function(){
//			var isValid = $(this).form('validate');
//			if(!isValid){
//				$.messager.progress('close');
//			}
//			return isValid;
//		},
//		success:function(data){
//			$.messager.progress('close');
//			try{
//			var result = eval('('+data+')');
//			if(result.success){
//				$.messager.alert('提示',sucMeg,'info');
//				$('#win-factory-addoredit').window('close');
//				$('#datagrid-factory-list').datagrid('reload');
//			}else{
//				$.messager.alert('提示',result.msg,'error');
//			}
//			}catch (e) {
//				$.messager.alert('提示',data,'error');
//			}
//		}
//		
//	});
}

//删除
Factory.del = function(){
	var selections = $('#datagrid-factory-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url:ctx+'/manager/basedata/factory/deleteFactory',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
				$.messager.show({
					title:'消息',
					msg:'删除工厂成功',
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
				//$.messager.alert('提示','删除用户成功!','info');
				$('#datagrid-factory-list').datagrid('reload');
		}
	});
}

//选择品牌
Factory.selectBrand = function(){
	var $brandGrid = $('#datagrid-brand');
	var $brandGridSelected = $('#datagrid-brand-selected');
	var brandList = $brandGrid.datagrid('getSelections');
	if(brandList.length<=0){
		$.messager.alert('标题','请选择品牌','warning');
		return false;
	}
	var dataList = [];
	$.each(brandList,function(i,n){
		dataList.push({
			id : n.id,
			brandCode:n.code,
			brandName:n.name
		});
	});
//	$brandGridSelected.datagrid('loadData',dataList);
	$.each(brandList,function(i,n){
		if($brandGridSelected.datagrid('getRowIndex',n.id)==-1){
			$brandGridSelected.datagrid('appendRow',{
				id : n.id,
				brandCode:n.code,
				brandName:n.name
				
			});
		}
		
	});
}

//移除已选择的品牌
Factory.removeSelectdBrandFmt = function(v,r,i){
	return '<button class="btn-link" onclick="Factory.removeSelectdBrand('+r.id+')">移除</button>'
}
//移除已选择的品牌
Factory.removeSelectdBrand = function(i){
	var $brandGridSelected = $('#datagrid-brand-selected');
	var rows = $brandGridSelected.datagrid('getRows');
	for(var j = 0 ; j < rows.length ; j++){
		if(rows[j].id == i){
			var index = $brandGridSelected.datagrid('getRowIndex',rows[j]);
			$brandGridSelected.datagrid('deleteRow',index);	
		}
	}
	
}
Factory.abolish = function(){
	var selections = $('#datagrid-factory-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.messager.confirm('提示','确定要废除该工厂吗？',function(r){
		if(r){
			for(var i=0;i<selections.length;i++){
				if(selections[i]["abolished"]=='1'){
					$.messager.alert('提示','存在已作废的记录！','info');
					return false;
				}
			}
			$.ajax({
				url:ctx+'/manager/basedata/factory/abolishBatch',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:'消息',
							msg:'废除工厂成功',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$('#datagrid-factory-list').datagrid('reload');
				}
			});
		}
		
		
	});
}
