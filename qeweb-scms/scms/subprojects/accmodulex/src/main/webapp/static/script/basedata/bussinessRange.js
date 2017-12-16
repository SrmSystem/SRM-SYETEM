var BussinessRange = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="BussinessRange.edit('+r.id+');">'+$.i18n.prop('button.edit')+</a>';
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-bussinessRange-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-bussinessRange-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-bussinessRange-addoredit').window({
			iconCls:'icon-edit',
//			title:'编辑业务范围'
			title: $.i18n.prop('basedata.text.ScopeOfEditing')
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-bussinessRange-addoredit').window('open');
		$('#form-bussinessRange-addoredit').form('load',ctx+'/manager/basedata/bussinessRange/getBussinessRange/'+id);
		$('#win-bussinessRange-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
			return false;
		}
		var params = $.toJSON(selections);
//		$.messager.confirm('提示','确定要废除该业务范围吗？<br/><font style="color: #F00;font-weight: 900;"> 废除后将导致相关业务类型、品牌、产品线一并废除</font>',function(r){
		$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.AreYouSureYouWantToCancelThisBusiness')<br/><font style="color: #F00;font-weight: 900;"> $.i18n.prop('basedata.text.AfteTheRepealTheRelatedBusinessTypeBrandAndProductLineWillBeAbolished')</font>',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
	//					$.messager.alert('提示','存在已作废的记录！','info');
						$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Mail.Already.Exists.InvalidMail'),'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/bussinessRange/abolishBatchRange',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						
							$.messager.show({
	//							title:'消息',
	//							msg:'废除业务范围成功',
								title: $.i18n.prop('label.news'),
								msg:  $.i18n.prop('basedata.text.AbolitionOfBusinessScopeSuccess'),
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});

							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	del : function(){
		var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
			return false;
		}
		var params = $.toJSON(selections);
//		$.messager.confirm('提示','该操作不可恢复,确定要删除该业务范围吗？<font style="color: #F00;font-weight: 900;"> 废除后将导致相关业务类型、品牌、产品线一并废除</font>',function(r){
		$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.ThisOperationIsNotRecoverDeleteThisServiceRange')+'<font style="color: #F00;font-weight: 900;">'+ $.i18n.prop('basedata.text.AfterTheRepealTheRelatedBeAbolished')+'</font>',function(r){
			if(r){
				$.messager.progress({
//					title:'提示',
//					msg : '提交中...'
					title: $.i18n.prop('label.remind'),
					msg :  $.i18n.prop('label.insubmit')
				});
				$.ajax({
					url:ctx+'/manager/basedata/bussinessRange/deleteBussinessRange',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
//							title:'消息',
//							msg:'删除业务范围成功',
							title: $.i18n.prop('label.remind'),
							msg: $.i18n.prop('basedata.text.DeleteBusinessSuccessfuly'),
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						
						//$.messager.alert('提示','删除用户成功!','info');
						$('#datagrid-bussinessRange-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-bussinessRange-addoredit').window({
			iconCls:'icon-add',
//			title:'新增业务范围'
			title: $.i18n.prop('basedata.text.NewAddBusiness')
		});
		$('#form-bussinessRange-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-bussinessRange-addoredit').window('open');
		$('#win-bussinessRange-addoredit').window('autoSize');
	},
	submit : function(){
		var url = ctx+'/manager/basedata/bussinessRange/addNewBussinessRange';
//		var sucMeg = '添加业务范围成功！';
		var sucMeg = $.i18n.prop('basedata.text.AddBusinessSuccess');
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/bussinessRange/update';
//			sucMeg = '编辑业务范围成功！';
			sucMeg = $.i18n.prop('basedata.text.EditSeviceSuccess');
		}
		$.messager.progress({
//			title:'提示',
//			msg : '提交中...'
			title: $.i18n.prop('label.remind'),
			msg :  $.i18n.prop('label.insubmit')
		});
		$('#form-bussinessRange-addoredit').form('submit',{
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
					$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
					$('#win-bussinessRange-addoredit').window('close');
					$('#datagrid-bussinessRange-list').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),data,'error');
				}
			}
			
		});
	},
	reset  : function(){
		$('#gongsiname').textbox('setValue','');
		$('#gongsiremark').textbox('setValue','');
	}
};
var BussinessRangeType = {
		operateFmt : function(v,r,i){
			if(r.abolished==0)
			{
				return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="BussinessRangeType.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>'/*编辑*/;
			}
			else{
				return '';
			}
		
		},
		rangeCodeFmt :function(v,r,i){return r.range==null?'':r.range.code},
		rangeNameFmt :function(v,r,i){return r.range==null?'':r.range.name},
		query : function(){
			var searchParamArray = $('#form-bussinessRange-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			var id=$("#pidd").val();
			$('#datagrid-bussinessRange-list').datagrid({url: ctx+'/manager/basedata/bussinessRange',
				queryParams : searchParams
			});
		},
		add : function(){
			//判断是否选择了范围
			var parentNode = $('#tree-bussinessRange').tree('getSelected');
			if(parentNode==null){
//				$.messager.alert('提示','请先在左边树上选择一个业务范围！','warning');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.PleaseChooseLeftBussniess'),'warning');
				return false;
			}
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			
			
			$win.window({
				iconCls:'icon-add',
//				title:'新增业务类型'
				title:$.i18n.prop('basedata.text.AddNewserviceType')
			});
			$form.form('clear');
			$('#id').val(0);
			$form.getCmp('parentId').val(parentNode.id);
			$form.getCmp('parentCode').textbox('setValue',parentNode.attributes.code);
			$form.getCmp('parentName').textbox('setValue',parentNode.text);
			$('#code').textbox('enable');
			$win.window('open');
			$win.window('autoSize');
		},
		edit : function(id){
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			$win.window({
				iconCls:'icon-edit',
//				title:'编辑业务类型'
				title:$.i18n.prop('basedata.text.EditServiceType')
			});
			$win.window('open');
			$form.form('load',ctx+'/manager/basedata/bussinessRange/getBussinessRange/'+id);
			$win.window('autoSize');
		},
		abolish : function(){
			var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var params = $.toJSON(selections);
//			$.messager.confirm('提示','确定要废除该业务类型吗？<font style="color: #F00;font-weight: 900;"> 废除后将导致相关品牌、产品线一并废除</font>',function(r){
			$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.ConfimToDeleteType')<font style="color: #F00;font-weight: 900;"> $.i18n.prop('basedata.text.DeleteMakeCauseALLBeAbolished')</font>,function(r){
				if(r){
					for(var i=0;i<selections.length;i++){
						if(selections[i]["abolished"]=='1'){
		//					$.messager.alert('提示','存在已作废的记录！','info');
							$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Mail.Already.Exists.InvalidMail'),'info');
							return false;
						}
					}
					$.ajax({
						url:ctx+'/manager/basedata/bussinessRange/abolishBatchType',
						type:'POST',
						data:params,
						contentType : 'application/json',
						success:function(data){
							
							$.messager.show({
								title:$.i18n.prop('label.news'),  /*消息*/
								msg:$.i18n.prop('basedata.text.AbolitionBusinessTypeSuccess'),   /*废除业务类型成功*/
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							
							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}
					});
				}
				
				
			});
			
			
		},
		del : function(){
			var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var params = $.toJSON(selections);
//			$.messager.confirm('提示','该操作不可恢复,确定要删除该业务类型吗？<font style="color: #F00;font-weight: 900;"> 废除后将导致相关业务类型、品牌、产品线一并废除</font>',function(r){
			$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.operationCanbeNotRecoverDeleteType')<font style="color: #F00;font-weight: 900;"> $.i18n.prop('basedata.text.AfteTheRepealTheRelatedBusinessTypeBrandAndProductLineWillBeAbolished')</font>,function(r){
				if(r){
					$.messager.progress({
//						title:'提示',
//						msg : '提交中...'
						title: $.i18n.prop('label.remind'),
						msg :  $.i18n.prop('label.insubmit')
					});
					$.ajax({
						url:ctx+'/manager/basedata/bussinessRange/deleteBussinessRange',
						type:'POST',
						data:params,
						contentType : 'application/json',
						success:function(data){
							$.messager.progress('close');
							$.messager.show({
								title:$.i18n.prop('label.news'),  /*消息*/
								msg:$.i18n.prop('basedata.text.DeleteBusinessTypeSuccessfuly'),/*删除业务类型成功*/
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							
							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}
					});
				}
				
				
			});
			
			
		},
		submit : function(){
			var url = ctx+'/manager/basedata/bussinessRange/addNewBussinessRangeType';
//			var sucMeg = '添加业务类型成功！';
			var sucMeg = $.i18n.prop('basedata.text.AddServiceTypeSuccess');
			if($('#id').val()!=0 && $('#id').val()!='0'){
				url = ctx+'/manager/basedata/bussinessRange/updateRangeType';
//				sucMeg = '编辑业务类型成功！';
				sucMeg = $.i18n.prop('basedata.text.EditServiceTypeSuccess');
			}
			$.messager.progress({
//				title:'提示',
//				msg : '提交中...'
				title: $.i18n.prop('label.remind'),
				msg :  $.i18n.prop('label.insubmit')
			});
			$('#form-bussinessRange-addoredit').form('submit',{
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
							$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
							$('#win-bussinessRange-addoredit').window('close');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}else{
							$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
						}
					}catch (e) {
						$.messager.alert($.i18n.prop('label.remind'),data,'error');
					}
				}
				
			});
		},
		expandRangeType : function(node){//来自业务树的点击操作
			$("#pidd").val(node.id);
			$('#datagrid-bussinessRange-list').datagrid('reload',ctx+'/manager/basedata/bussinessRange?query-EQ_parentId='+node.id);
		},
		rangeTypeLoadSuc : function(data){
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			$form.getCmp('parentName').textbox('setValue',data.range.name);
			$form.getCmp('parentCode').textbox('setValue',data.range.code);
		}
		
};
//品牌
var BussinessRangeBrand = {
		operateFmt : function(v,r,i){
			if(r.abolished==0)
			{
				return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="BussinessRangeBrand.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';
			}
			else
			{
				return '';
			}
		},
		rangeCodeFmt :function(v,r,i){return r.range==null?'':r.range.code},
		rangeNameFmt :function(v,r,i){return r.range==null?'':r.range.name},
		query : function(){
			var searchParamArray = $('#form-bussinessRange-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-bussinessRange-list').datagrid('load',searchParams);
		},
		edit : function(id){
			$('#win-bussinessRange-addoredit').window({
				iconCls:'icon-edit',
//				title:'编辑品牌'
				title:$.i18n.prop('basedata.brand.EditorBrand')
			});
			$('#code').textbox('disable');
			$('#win-bussinessRange-addoredit').window('open');
			$('#form-bussinessRange-addoredit').form('load',ctx+'/manager/basedata/bussinessRange/getBussinessRange/'+id);
			$('#win-bussinessRange-addoredit').window('autoSize');
		},
		abolish : function(){
			var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var params = $.toJSON(selections);
//			$.messager.confirm('提示','确定要废除该品牌吗？<font style="color: #F00;font-weight: 900;"> 废除后将导致相关产品线一并废除</font>',function(r){
			$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.AbolishTheBrand')<font style="color: #F00;font-weight: 900;"> $.i18n.prop('label.remind')</font>,function(r){
				if(r){
					for(var i=0;i<selections.length;i++){
						if(selections[i]["abolished"]=='1'){
//							$.messager.alert('提示','存在已作废的记录！','info');
							$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Mail.Already.Exists.InvalidMail'),'info');
							return false;
						}
					}
					$.ajax({
						url:ctx+'/manager/basedata/bussinessRange/abolishBatchBrand',
						type:'POST',
						data:params,
						contentType : 'application/json',
						success:function(data){
							
							$.messager.show({
								title:$.i18n.prop('label.news'),  /*消息*/
//								msg:'废除品牌成功',
								msg:$.i18n.prop('basedata.text.AbolishBrandSuccess'),
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							
							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}
					});
				}
				
				
			});
			
			
		},
		add : function(){
			//判断是否选择了范围
			var parentNode = $('#tree-bussinessRange').tree('getSelected');
			if(parentNode==null || parentNode.attributes.bussinessType!=1){
//				$.messager.alert('提示','请先在左边树上选择一个业务类型！','warning');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.AtLeftChoseOneSevice'),'warning');
				return false;
			}
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			
			
			$win.window({
				iconCls:'icon-add',
//				title:'新增品牌'
				title:$.i18n.prop('basedata.brand.NewBrand')
			});
			$form.form('clear');
			$('#id').val(0);
			$form.getCmp('parentId').val(parentNode.id);
			$form.getCmp('parentCode').textbox('setValue',parentNode.attributes.code);
			$form.getCmp('parentName').textbox('setValue',parentNode.text);
			$('#code').textbox('enable');
			$win.window('open');
			$win.window('autoSize');
		},
		del : function(){
			var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var params = $.toJSON(selections);
//			$.messager.confirm('提示','该操作不可恢复,确定要删除该品牌吗？',function(r){
			$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.OperationNonRecoverableDeleted'),function(r){
				if(r){
					$.messager.progress({
//						title:'提示',
//						msg : '提交中...'
						title: $.i18n.prop('label.remind'),
						msg : $.i18n.prop('label.insubmit')
					});
					$.ajax({
						url:ctx+'/manager/basedata/bussinessRange/deleteBussinessRange',
						type:'POST',
						data:params,
						contentType : 'application/json',
						success:function(data){
							$.messager.progress('close');
							$.messager.show({
								title:$.i18n.prop('label.news'),  /*消息*/
//								msg:'删除品牌成功',
								msg:$.i18n.prop('basedata.brand.DeleteBrandSuccess'),
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							
							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}
					});
				}
				
				
			});
			
			
		},
		submit : function(){
			var url = ctx+'/manager/basedata/bussinessRange/addNewBussinessRangeBrand';
//			var sucMeg = '添加品牌成功！';
			var sucMeg = $.i18n.prop('basedata.brand.AddBrandSuccess');
			if($('#id').val()!=0 && $('#id').val()!='0'){
				url = ctx+'/manager/basedata/bussinessRange/updateRangeBrand';
//				sucMeg = '编辑品牌成功！';
				sucMeg = $.i18n.prop('basedata.brand.EditorsBrandSuccess');
			}
			$.messager.progress({
//				title:'提示',
//				msg : '提交中...'
				title: $.i18n.prop('label.remind'),
				msg : $.i18n.prop('label.insubmit')
			});
			$('#form-bussinessRange-addoredit').form('submit',{
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
							$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
							$('#win-bussinessRange-addoredit').window('close');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}else{
							$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
						}
					}catch (e) {
						$.messager.alert($.i18n.prop('label.remind'),data,'error');
					}
				}
				
			});
		},
		expandRangeBrand : function(node){//业务树展开品牌
			if(node.attributes.bussinessType==1)
			$('#datagrid-bussinessRange-list').datagrid('load',{'query-EQ_parentId':node.id,'query-EQ_bussinessType':2});
			$("#pidd").val(node.id);
		},
		selectRangeType : function(node){
			if(node.attributes.bussinessType!=1)
				return false;
		},
		rangeBrandLoadSuc : function(data){
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			$form.getCmp('parentName').textbox('setValue',data.range.name);
			$form.getCmp('parentCode').textbox('setValue',data.range.code);
		}
		
};
//产品线
var BussinessRangeLine = {
		operateFmt : function(v,r,i){
			if(r.abolished==0)
			{
				return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="BussinessRangeLine.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';
			}
			else{
				return '';
			}
		},
		rangeCodeFmt :function(v,r,i){return r.range==null?'':r.range.code},
		rangeNameFmt :function(v,r,i){return r.range==null?'':r.range.name},
		query : function(){
			var searchParamArray = $('#form-bussinessRange-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-bussinessRange-list').datagrid('load',searchParams);
		},
		edit : function(id){
			$('#win-bussinessRange-addoredit').window({
				iconCls:'icon-edit',
//				title:'编辑产品线'
				title:$.i18n.prop('basedata.text.EditProductLine')
			});
			$('#code').textbox('disable');
			$('#win-bussinessRange-addoredit').window('open');
			$('#form-bussinessRange-addoredit').form('load',ctx+'/manager/basedata/bussinessRange/getBussinessRange/'+id);
			$('#win-bussinessRange-addoredit').window('autoSize');
		},
		abolish : function(){
			var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var params = $.toJSON(selections);
//			$.messager.confirm('提示','确定要废除该产品线吗？',function(r){
			$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.ComfigToDeleteProductLine'),function(r){
				if(r){
					for(var i=0;i<selections.length;i++){
						if(selections[i]["abolished"]=='1'){
//							$.messager.alert('提示','存在已作废的记录！','info');
							$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Mail.Already.Exists.InvalidMail'),'info');
							return false;
						}
					}
					$.ajax({
						url:ctx+'/manager/basedata/bussinessRange/abolishBatchLine',
						type:'POST',
						data:params,
						contentType : 'application/json',
						success:function(data){
							
							$.messager.show({
								title:$.i18n.prop('label.news'),  /*消息*/
		//						msg:'废除产品线成功',
								msg:$.i18n.prop('basedata.text.AbolishProductLineSuccess'),
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							
							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}
					});
				}
				
				
			});
			
			
		},
		add : function(){
			//判断是否选择了范围
			var parentNode = $('#tree-bussinessRange').tree('getSelected');
			if(parentNode==null || parentNode.attributes.bussinessType!=2){
//				$.messager.alert('提示','请先在左边树上选择一个品牌！','warning');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('basedata.text.AtLeftChooseBrand'),'warning');
				return false;
			}
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			
			
			$win.window({
				iconCls:'icon-add',
//				title:'新增产品线'
				title:$.i18n.prop('basedata.text.NewAddLine')
			});
			$form.form('clear');
			$('#id').val(0);
			$form.getCmp('parentId').val(parentNode.id);
			$form.getCmp('parentCode').textbox('setValue',parentNode.attributes.code);
			$form.getCmp('parentName').textbox('setValue',parentNode.text);
			$('#code').textbox('enable');
			$win.window('open');
			$win.window('autoSize');
		},
		del : function(){
			var selections = $('#datagrid-bussinessRange-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var params = $.toJSON(selections);
//			$.messager.confirm('提示','该操作不可恢复,确定要删除该产品线吗？',function(r){
			$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('operationCanbeNotRecoverDeleteLine'),function(r){
				if(r){
					$.messager.progress({
//						title:'提示',
//						msg : '提交中...'
						title: $.i18n.prop('label.remind'),
						msg :  $.i18n.prop('label.insubmit')
					});
					$.ajax({
						url:ctx+'/manager/basedata/bussinessRange/deleteBussinessRange',
						type:'POST',
						data:params,
						contentType : 'application/json',
						success:function(data){
							$.messager.progress('close');
							$.messager.show({
								title:$.i18n.prop('label.news'),  /*消息*/
//								msg:'删除产品线成功',
								msg:$.i18n.prop('basedata.text.DeleteProductLineSuccess'),
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							
							//$.messager.alert('提示','删除用户成功!','info');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}
					});
				}
				
				
			});
			
			
		},
		submit : function(){
			var url = ctx+'/manager/basedata/bussinessRange/addNewBussinessRangeLine';
//			var sucMeg = '添加产品线成功！';
			var sucMeg = $.i18n.prop('basedata.text.AddProductLineSuccess');
			if($('#id').val()!=0 && $('#id').val()!='0'){
				url = ctx+'/manager/basedata/bussinessRange/updateRangeLine';
//				sucMeg = '编辑产品线成功！';
				sucMeg = $.i18n.prop('basedata.text.EditProductLineSuccess');
			}
			$.messager.progress({
//				title:'提示',
//				msg : '提交中...'
				title: $.i18n.prop('label.remind'),
				msg :  $.i18n.prop('label.insubmit')
			});
			$('#form-bussinessRange-addoredit').form('submit',{
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
							$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
							$('#win-bussinessRange-addoredit').window('close');
							$('#datagrid-bussinessRange-list').datagrid('reload');
						}else{
							$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
						}
					}catch (e) {
						$.messager.alert($.i18n.prop('label.remind'),data,'error');
					}
				}
				
			});
		},
		expandRangeLine : function(node){//业务树展开产品线
			if(node.attributes.bussinessType==2)
				$('#datagrid-bussinessRange-list').datagrid('load',{'query-EQ_parentId':node.id,'query-EQ_bussinessType':3});
			
			$("#pidd").val(node.id);
		},
		selectRangeBrand : function(node){
			if(node.attributes.bussinessType!=2)
				return false;
		},
		rangeLineLoadSuc : function(data){
			var $form = $('#form-bussinessRange-addoredit');
			var $win = $('#win-bussinessRange-addoredit');
			$form.getCmp('parentName').textbox('setValue',data.range.name);
			$form.getCmp('parentCode').textbox('setValue',data.range.code);
		}
		
};