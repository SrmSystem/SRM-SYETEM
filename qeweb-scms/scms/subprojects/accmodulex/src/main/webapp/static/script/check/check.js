var $$checkDetail1;
var $$checkDetail2;
var Check={
	operateFmt:function(v,r,i){
//		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.showDetail('+ r.id +');">查看</a>'
		return'<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.showDetail('+ r.id +');">' + $.i18n.prop('button.view') + '</a>&nbsp;&nbsp; '
		/*'<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.showFeedback(\'/manager/check/checks/feedback/'+ r.id +'\');">反馈【'+r.feedbackCount+'】</a>';*/
		
		 
	},
	operateFmtAbroad:function(v,r,i){
//		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.showAbroadDetail('+ r.id +');">查看</a>'
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.showAbroadDetail('+ r.id +');">' + $.i18n.prop('button.view') + '</a>'
	},
	modAndDel:function(v,r,i){
//		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.modInvoice('+ r.id +');">修改</a>&nbsp;'+
//		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.modInvoice('+ r.id +', ' + r.col2 + ');">修改</a>&nbsp;'+
//		'<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.delInvoice('+ r.id +', ' + r.check.id + ');">删除</a>'
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.modInvoice('+ r.id +', ' + r.col2 + ');">' + $.i18n.prop('button.update') + '</a>&nbsp;'+
		'<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Check.delInvoice('+ r.id +', ' + r.check.id + ');">' + $.i18n.prop('button.delete') + '</a>'
	},
	delInvoice:function(id, checkId){
		$.ajax({
			url:ctx+'/manager/check/checks/opt/delInvoice/'+id,
			type:'POST',
			//data:params,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
//				$('#datagrid-list1').datagrid('reload');
//				$('#datagrid-list2').datagrid('reload');
				try{
					if(data.success){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  data.msg, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						//this.reload(); 
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.msg,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				}
				$("a.panel-tool-close").click();
				Check.showDetail(checkId);
			},
			error:function(v){
//				alert($.i18n.prop('receive.message32'));
				alert($.i18n.prop('receive.message32'));
				$("a.panel-tool-close").click();
				Check.showDetail(checkId);
			}
		});
	},
	modInvoice:function(id, col2){
//		var title = '修改发票';
//		var gridIds = ['datagrid-list2'];
//		var gridIds2 = [];
//		new dialog().showWin(title, 400, 320, ctx + '/manager/check/checks/modInvoice/' + id,gridIds,false,gridIds2);
	
		var gridIds = ['datagrid-list1','datagrid-list2'];
		var gridIds2 = ['datagrid-list4'];
		info(col2 + "== col2");
		if(col2 && col2 == "1"){
//			var title = '修改索赔金额发票';
			var title = $.i18n.prop('base.Check.UpdateClaimAmountInvoice');
			new dialog().showWin(title, 900, 480, ctx + '/manager/check/checks/modClaimInvoice/' + id, gridIds, false, gridIds2);
			return;
		}
//		var title = '修改发票';
		var title = $.i18n.prop('button.update');
		new dialog().showWin(title, 900, 480, ctx + '/manager/check/checks/modInvoice/' + id, gridIds, false, gridIds2);
	},
	saveInvoice:function(id, btn){
//		$('input[name="billTime"]').val($('input[name="billTime"]').val()+' 00:00:00');
//		$('#form-invoice').form('submit',{
		$.messager.progress();
		$('#form-bill').form('submit',{
			url:ctx+'/manager/check/checks/opt/saveInvoice/'+id, 
			success:function(data){
				data = JSON.parse(data);
				$.messager.alert($.i18n.prop('label.remind'),data.msg,'info');
				$.messager.progress('close');
			},
			error:function(data){
				$.messager.alert($.i18n.prop('label.remind'),data.msg,'info');
			}
		});
	},
	showDetail : function(id) {   
//		var title = '查看对账单明细';
		var title = $.i18n.prop('button.view');
		var gridIds = ['datagrid-list'];
		var gridIds2 = ['datagrid-list1','datagrid-list2'];
		$$checkDetail1 = new dialog();
		$$checkDetail1.showWin(title, 900, 480, ctx + '/manager/check/checks/viewDetail/' + id,gridIds,true,gridIds2);
	},
	showAbroadDetail : function(id) {   
//		var title = '查看对账单明细';
		var title = $.i18n.prop('button.view');
		var gridIds = ['datagrid-list'];
		var gridIds2 = ['datagrid-list1','datagrid-list2'];
		new dialog().showWin(title, 900, 480, ctx + '/manager/check/checks/showAbroadDetail/' + id,gridIds,true,gridIds2);
	},
	editClaimDescription:function(id,field,publishStatus){
		$.messager.progress();
		if(publishStatus==1){
			$.messager.progress('close');
			return false;
		}
		var field_v = field.value;
		if(field_v==null||field_v==""){
			$.messager.progress('close');
			return false;
		}
		$.ajax({
			url:ctx+'/manager/check/checks/opt/editClaimDescription/'+id,
			type:'POST',
			data:JSON.stringify({
				claimDescription:field_v,
			}),
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
			},
			error:function(v){
				$.messager.progress('close');
				alert($.i18n.prop('receive.message32'));
			}
		});
	},
	editClaimAmount:function(id,field,publishStatus){
		$.messager.progress();
		if(publishStatus==1){
			$.messager.progress('close');
			return false;
		}
		var field_v = field.value;
		if(field_v==null||field_v==""){
			$.messager.progress('close');
			return false;
		}
		$.ajax({
			url:ctx+'/manager/check/checks/opt/editClaimAmount/'+id,
			type:'POST',
			data:JSON.stringify({
				claimAmount:field_v,
			}),
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
			},
			error:function(v){
				$.messager.progress('close');
				alert($.i18n.prop('receive.message32'));
			}
		});
	},
	opt:function(opt,id,btn,validates){
		$.messager.progress();
		if(opt == "publish") {
			var reg = /^((-\d+(\.\d+)?)|(0+(\.0+)?))$/;		//匹配非正浮点数（负浮点数 + 0）
//			var claimAmount=$('#claimAmount').numberbox('getValue');
			var claimAmount=$('#claimAmount').val();
			if(claimAmount!=null && isNaN(claimAmount)){
//				$.messager.alert($.i18n.prop('label.remind'),'质量索赔扣减金额只能填写数字','info');
				$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('base.Check.message1'), 'info');
				$.messager.progress('close');
				return false;
			}else if(claimAmount==null || claimAmount==''){
//				$.messager.alert($.i18n.prop('label.remind'),'质量索赔扣减金额需要填写且只能填写数字！','info');
				$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('base.Check.message2'),'info');
				$.messager.progress('close');
				return false;
			}else if(!reg.test(claimAmount)){
//				$.messager.alert($.i18n.prop('label.remind'),'质量索赔扣减金额只能填写负数！','info');
				$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('base.Check.message3'),'info');
				$.messager.progress('close');
				return false;
			}
		}
		if(validates!=null){
			for(var v =0;v<validates.length;v++){
				var isValid = $("#"+validates[v]).form('validate');
				if(!isValid){
					$.messager.progress('close');
					return false;
				}
			}
		}
		
		$.messager.confirm($.i18n.prop('label.remind'), $.i18n.prop('label.confirm.operation'), function (data) {
			if (data) {
				$('#form-check').form('submit',{
					ajax:true,
					iframe: true,    
					url: ctx+'/manager/check/checks/opt/' + opt + '/'+id,
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
							$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.operateOK'),'info');
							$('#datagrid-list').datagrid("reload");
							$(".panel-tool-close").click();//执行了关闭的事件
						}else{
							$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
						}
						}catch (e) {  
							$.messager.alert($.i18n.prop('label.remind'),data,'error');
						}
					},
					error:function(v){
						alert($.i18n.prop('receive.message32'));
					}
				});
			} else {
				$.messager.progress('close');
			}
		});
	},	
	
	confirmInvoice:function(id){
		$.messager.progress();
		$.ajax({
			url:ctx+'/manager/check/checks/opt/confirmInvoice/'+id,
			type:'POST',
			//data:params,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$('#rejectInvoice_btn').hide();
				$('#confirmInvoice_btn').hide();
				$.messager.progress('close');
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						//this.reload(); 
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.msg,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),data.msg,'error'); 
				}
				$('#form-check').parents("div.window").find("a.panel-tool-close").click();
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
				$.messager.progress('close');
				$('#form-check').parents("div.window").find("a.panel-tool-close").click();
			}
		});
	},
	rejectInvoice:function(id){
		var msg=$('#billRejectReason').val();
		if(msg.length==0 || msg==null){
//			$.messager.alert($.i18n.prop('label.remind'),'驳回时需要填写驳回原因!','error');
			$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('base.Check.message4'),'error');
			return false;
		}
		$.messager.progress();
		$('#form-check').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx+'/manager/check/checks/opt/rejectInvoice/'+id,
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
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.operateOK'),'info');
					$('#datagrid-list').datagrid("reload");
				}else{
					$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
				}
				}catch (e) {  
					$.messager.alert($.i18n.prop('label.remind'),data,'error');
				}
				$('#form-check').parents("div.window").find("a.panel-tool-close").click();
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
				$.messager.progress('close');
				$('#form-check').parents("div.window").find("a.panel-tool-close").click();
			}
		});
	},
	/**
	 * 提交差异处理
	 * @param id
	 * @returns {Boolean}
	 */
	dealEx:function(id){
		$.messager.progress();
		var isValid = $("#form-check").form('validate');
		if(!isValid) {
			$.messager.progress('close');
			return false;
		}
		$('#form-check').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/check/checks/opt/dealEx', 
			onSubmit:function(param){
			},
			success:function(data){
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert($.i18n.prop('label.remind'), result.msg, 'info');
						$(".panel-tool-close").click();//执行了关闭的事件
						$('#datagrid-pending-list').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('label.remind'), result.msg, 'error');
					}
				}catch (e) {  
					$.messager.alert($.i18n.prop('label.remind'), e,'error');
				}
			}
		});
	},
	
	vConfirm:function(id,btn){
		RowEditor.accept('datagrid-list1');  
		var rows = $("#datagrid-list1").datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			var oo = rows[i]['vendorCheckPrice'];
			if (oo==null){
//				$.messager.alert($.i18n.prop('label.remind'),'供应商单价不能为空','error');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message5'),'error');
				return false;
			}
		}
		var o =$('#datagrid-list1').datagrid('getData');
		var datas = JSON.stringify(o); 
		$.ajax({
			url:ctx+'/manager/check/checks/opt/vConfirm/'+id,
			type:'POST',
			data:datas,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$('#'+btn.id).hide();
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						//this.reload(); 
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
				$("#datagrid-list1").datagrid('reload');
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
				$("#datagrid-list1").datagrid('reload');
			}
		});
	},
	billOk:function(id,btn){
		RowEditor.accept('datagrid-list1');
		var rows = $('#datagrid-list1').datagrid('getRows');
		var icountTax = 0;		//对账单明细总税金
		for(var i=0; i<rows.length; i++){
			var row = rows[i];
			if(row != null){
				var invoice = row.invoice;
				if(invoice == null){
//					$.messager.alert($.i18n.prop('label.remind'),'存在未开过发票的对账单明细！','info');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message6'),'info');
					return;
				}
			}
			icountTax = parseFloat(row.col1) + icountTax;
		}
		var claimAmount = $("#hiddenClaimAmount").val();	//获取索赔金额
		if(claimAmount && claimAmount.length != 0){
			icountTax += parseFloat(claimAmount) * 0.17;
		}
		
		//发票总税金
		rows = $('#datagrid-list2').datagrid('getRows');
		var inCountTax = 0;
		for(var i=0; i<rows.length; i++) {
//			var col2 = rows[i].col2;			//索赔金额开票状态(1为已开索赔金额发票)
//			info(col2 + " == col2");
//			if(col2 && col2 == "1"){
//				info("索赔金额发票...");
//			}else{
//				inCountTax = parseFloat(rows[i].tax) + inCountTax;
//			}
			inCountTax = parseFloat(rows[i].tax) + inCountTax;
		}
		
		if(icountTax.toFixed(5) != inCountTax.toFixed(5)) {
//			$.messager.alert($.i18n.prop('label.remind'),'发票税金总和:' + inCountTax.toFixed(5) + '与对账单明细税金之和' + icountTax.toFixed(5) + '不等，请调整对账单明细第一条记录税金','error');
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message7') + inCountTax.toFixed(5) + $.i18n.prop('base.Check.message8') + icountTax.toFixed(5) + $.i18n.prop('base.Check.message9'),'error');
			return false;
		}
		var o =$('#datagrid-list1').datagrid('getData');
		var datas = JSON.stringify(o);   
		$.ajax({
			url:ctx+'/manager/check/checks/opt/billOk/'+id,
			type:'POST',
			data: datas,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$('#'+btn.id).hide();
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				}
				$("a.panel-tool-close").click();
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
				$("a.panel-tool-close").click();
			}
		});
	},
	batchModifyPrice:function(){
		RowEditor.accept('datagrid-list');
		var o =$('#datagrid-list').datagrid('getData');
		var datas = JSON.stringify(o); 
		$.ajax({
			url:ctx+'/manager/check/checks/opt/batchModifyPrice',
			type:'POST',
			data:datas,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				//$('#'+btn.id).hide();
				try{
					if(data.success){
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.msg,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
			}
		});
	},
	confirmModifyPrice:function(){
		CellEditor.accept('datagrid-list');
		var selections = $('#datagrid-list').datagrid('getSelections');
		if(selections.length == 0 ){
//			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			return false;
		}
		var datas = JSON.stringify(selections); 
		$.ajax({
			url:ctx+'/manager/check/checks/opt/confirmModifyPrice',
			type:'POST',
			data: "{\"total\":0,\"rows\":" + datas + "}",
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				try{
					if(data.success){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.msg,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
			}
		}); 
	},
	confirmEx:function(){
		$.messager.progress();
		var selections = $('#datagrid-list').datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			$.messager.progress('close');
			return false;
		}
		for(var i=0;i<selections.length;i++){
			if(selections[i].checkItem.exDealStatus==0){
//				$.messager.alert($.i18n.prop('label.remind'),'包含采方差异未处理记录无法确认！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message10'),'info');
				$.messager.progress('close');
				return false;
			}
			if(selections[i].checkItem.exConfirmStatus==1){
//				$.messager.alert($.i18n.prop('label.remind'),'已确认差异的数据不能重复确认！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message11'),'info');
				$.messager.progress('close');
				return false;
			}
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/check/checks/opt/confirmEx',
			type:'POST',
			data:params,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				//$('#'+btn.id).hide();
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list').datagrid('reload');
						$.messager.progress('close');
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
						$.messager.progress('close');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
					$.messager.progress('close');
				} 
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
				$.messager.progress('close');
			}
		});
	},
	bill:function(id, isAbroad){
		var isValid = $("#form-bill").form('validate');
		if(!isValid) {
			return false;
		}
		$.messager.progress();
		$('#form-bill').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/check/checks/opt/bill', 
			onSubmit:function(param){
			},
			success:function(data){
				info(data);
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					info(result);
					info(result.success);
					if(result.success){
//						$.messager.show({
//							title:$.i18n.prop('label.news'),
//							msg:  $.i18n.prop('label.operateOK'), 
//							timeout:2000,
//							showType:'show',
//							style:{
//								right:'',
//								top:document.body.scrollTop+document.documentElement.scrollTop,
//								bottom:''
//							}
//						});
//						$('#datagrid-list').datagrid('reload'); 
						$.messager.alert($.i18n.prop('label.operateOK'), result.msg, 'info', function(){
							reloadWindow(isAbroad, id);		//重载开票画面
						});
					}else{
						$.messager.alert($.i18n.prop('label.remind'), result.msg, 'error', function(){
//							reloadWindow(isAbroad, id);		//重载开票画面
						});
					}
				}catch (e) {  
					$.messager.alert($.i18n.prop('label.remind'),e ,'error', function(){
						reloadWindow(isAbroad, id);			//重载开票画面
					});
				}
//				$('#form-bill').parents("div.window").find("a.panel-tool-close").click();
//				$$checkDetail1.dialog('close');
//				info($$checkDetail1);
//				$$checkDetail1.close();
//				$.parser.parse($('#form-bill').parent());
//				info(id + "==id")
				
//				$("a.panel-tool-close").click();
//				if(isAbroad){						//国外
//					Check.showAbroadDetail(id);
//				}else{								//国内，外协
//					Check.showDetail(id);
//				}
			},
			error : function(data) {
				$.messager.progress('close');
				alert(data);
//				$('#form-bill').parents("div.window").find("a.panel-tool-close").click();
//				Check.showDetail(id);
				reloadWindow(isAbroad, id);			//重载开票画面
			}
		});
	},
	addClaimBill : function(){
		var isValid = $("#form-bill").form('validate');
		if(!isValid) {
			return false;
		}
		$.messager.progress();
		$('#form-bill').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/check/checks/opt/addClaimBill', 
			onSubmit:function(param){
			},
			success:function(data){
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list').datagrid('reload'); 
					}else{
						$.messager.alert($.i18n.prop('label.remind'), result.msg, 'error');
					}
				}catch (e) {  
					$.messager.alert($.i18n.prop('label.remind'),e ,'error');
				}
				$('#form-bill').parents("div.window").find("a.panel-tool-close").click();
			},
			error : function(data) {
				$.messager.progress('close');
				alert(data);
				$('#form-bill').parents("div.window").find("a.panel-tool-close").click();
			}
		});
		$('#claimBill').removeAttr("checked");
		$('#claimBill').attr("disabled", "disabled");
		$('#hiddenCheckCol2').val("1");
	},
	tobill:function(isAbroad){
		debugger;
		var gridIds = ['datagrid-list1','datagrid-list2'];
		var gridIds2 = ['datagrid-list4'];
		var selections = $('#datagrid-list1').datagrid('getSelections');
		var selectionsLength = selections.length;
		if(selectionsLength == 0 ){
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			return false;
		}
		
		var pks = "";
		for(var i=0;i<selections.length;i++){
			//alert(selections[i].id);
			if(i<selections.length-1){
				pks += selections[i].id + ",";
			}else{
				pks += selections[i].id;
			}
			var selection = selections[i];
			if(selection != null){
				var invoice = selection.invoice;
				if(invoice != null){
					var invoiceId = invoice.id;
					if(invoiceId != null && invoiceId != "null" && invoiceId != ""){
//						$.messager.alert($.i18n.prop('label.remind'),'存在已经开过发票的对账单明细！','info');
						$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message12'),'info');
						return;
					}
				}
			}
		}
		//var params = $.toJSON(selections);
		var count = $("#itemtoolbar input[name='claimBill']:checked").length;	//获取是否选中索赔开票
		
		if(!isAbroad){															//非国外
			var rows = $('#datagrid-list1').datagrid('getRows');
			var hasClaim = false;												//已索赔金额开票状态
			var lessCount = 0;													//获取还剩下没被开票的明细数量
			for(var i in rows){
				var row = rows[i];
				var check = row.check;
				if(check != null){
					var col2 = check.col2;
					if(col2 != null && col2 != "null" && col2 != "" && col2 == "1"){
						hasClaim = true;
					}
				}
				var invoice = row.invoice;
				if(invoice != null){
					var invoiceId = invoice.id;
					if(invoiceId != null && invoiceId != "null" && invoiceId != ""){
						info("这个明细已经开过发票");
					}else{
						info("这个明细没有开过发票");
						lessCount++;
					}
				}else{
					info("这个明细没有开过发票");
					lessCount++;
				}
			}
			info(rows);
			info("hasClaim == " + hasClaim);
			info("lessCount == " + lessCount);
			info("selectionsLength == " + selectionsLength);
			info("count == " + count);
			//如果还没索赔开票，并且勾选明细开票数量和还剩下的未被开票明晰数量相等，且没有勾选索赔开票，则提示
			if(!hasClaim && (lessCount == selectionsLength) && count == 0){
//				$.messager.alert($.i18n.prop('label.remind'),'索赔金额必须开票！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message13'),'info');
				return;
			}
		}
		
//		var title = '开票';
		var title = $.i18n.prop('base.Check.message14');
		var hiddenCheckId = $("#hiddenCheckId").val();
		var claimBillFlag = false;
		if(isAbroad){			//国外
			claimBillFlag = false;
		}else{					//国内，外协
			if(count != 0){						//选中了索赔金额开票
				claimBillFlag = true;
			}
		}
		var url = ctx + '/manager/check/checks/tobill/' + pks + "/" + hiddenCheckId + "/" + claimBillFlag+ "/" + isAbroad;
		new dialog().showWin(title, 900, 480, url, gridIds,false,gridIds2);
	},
	/*confirmInvoice:function(){
		var selections = $('#datagrid-list2').datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/check/checks/opt/confirmInvoice',
			type:'POST',
			data:params,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				//$('#'+btn.id).hide();
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list2').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
			}
		});
	},	*/
	close:function(){
		var selections = $('#datagrid-list').datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			return false;
		}
		//edit by zhangjiejun 2015.10.15 start
		var array = [];
		for(var i in selections){
			var row = selections[i];
			if(row.closeStatus == 0){
				array.push(row);
			}
		}
		if(array.length == 0 ){
//			$.messager.alert($.i18n.prop('label.remind'),'没有需要被关闭的记录！','info');
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message15'),'info');
			return;
		}
		var params = $.toJSON(array);
//		var params = $.toJSON(selections);
		//edit by zhangjiejun 2015.10.15 end
		
		$.ajax({
			url:ctx+'/manager/check/checks/opt/close',
			type:'POST',
			data:params,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				//$('#'+btn.id).hide();
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list').datagrid('reload');
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
			}
		});
	},
	create:function(){
		$.messager.progress();
		$.ajax({
			url:ctx+'/manager/check/checks/opt/create',
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				try{
					if(data){ 
						$.messager.show({
							title:$.i18n.prop('label.news'),
							msg:  $.i18n.prop('label.operateOK'), 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-list').datagrid('reload');
						$.messager.progress('close');
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
			},
			error:function(v){
				alert($.i18n.prop('receive.message32'));
			}
		});
	},
	//edit by zhangjiejun 2015.10.16 start
	publish:function(){
		var selections = $('#datagrid-list').datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
			return false;
		}
		var array = [];
		for(var i in selections){
			var row = selections[i];
			if(row.publishStatus == 0){
				array.push(row);
			}
		}
		if(array.length == 0 ){
//			$.messager.alert($.i18n.prop('label.remind'),'没有需要被发布的记录！','info');
			$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message16'),'info');
			return;
		}
		var params = $.toJSON(array);
		$.messager.confirm($.i18n.prop('order.confirm'), $.i18n.prop('label.sureToExecute'), function (r) {
			if(r){
				$.messager.progress();
				$.ajax({
					url:ctx+'/manager/check/checks/opt/publish',
					type:'POST',
					data:params,
					dataType:"json",
					contentType : 'application/json',
					success:function(data){
						//$('#'+btn.id).hide();
						try{
							if(data){ 
								$.messager.show({
									title:$.i18n.prop('label.news'),
									msg:  $.i18n.prop('label.operateOK'), 
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-list').datagrid('reload');
								$.messager.progress('close');
							}else{
								$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
							}
						}catch (e) {
							$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
						} 
					},
					error:function(v){
						alert($.i18n.prop('receive.message32'));
					}
				});
			}
		});
	},
	showFeedback:function(url){
		new dialog().showWin($.i18n.prop('button.feedback'), 600, 480, ctx + url);   
	},
	feedback:function(formId, tableId) {
		$.messager.progress();
		$('#' + formId).form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/basedata/feedback/addFeedback', 
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				$('#feedbackContent').val(''); 
				try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.show({ title:'消息', msg:  result.msg,  timeout:2000, showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						var trHtml = "<tr><td style='text-align: left'> &nbsp;<b> " + result.feedback.feedbackOrgName 
						+ "&nbsp; " + result.feedback.createUserName + " &nbsp;&nbsp; " + result.feedback.createTime
						+ "</b></td></tr><tr><td style='white-space: normal; text-align: left' nowrap>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; " + result.feedback.feedbackContent
						+ "</td></tr>";
						$("#" + tableId).append(trHtml);
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	}
	//edit by zhangjiejun 2015.10.15 end
}

/**
 * 检测null的时候，显示空
 */
function checkNull(field){
	info(field);
	if(field == null || field == "null"){
		field = "";
	}
	return field;
}
//
///**
// * 初始化下载Attachment
// * @param 	value
// * @param 	row
// * @param 	index
// * @returns {String}
// */
//function downLoadAttachment(value, row, index){
////	info(row);
////	info(row.invoiceFileName);
////	info(row.invoiceFilePath);
//	
//	var filePath = row.invoiceFilePath;
//	var fileName = row.invoiceFileName;
//	if(fileName == null){
//		fileName = "";
//	}
//	var filePath_hidden = '<input value="' + filePath + '" id="filePath_hidden' + index + '" type="hidden"/>';
//	var fun = 'download($(\'#filePath_hidden' + index + '\').val(), \'' + fileName + '\');';
//	var download_url = '<a style="float: left;" href="javascript:; ' + fun + '">' + fileName + '</a>';
//	return filePath_hidden + download_url;
//}

/**
 * 初始化下载Attachment
 * @param 	value
 * @param 	row
 * @param 	index
 * @returns {String}
 */
function downLoadAttachment(filePath, fileName, index){
//	info(row);
//	info(row.invoiceFileName);
//	info(row.invoiceFilePath);
	if(fileName == null){
		fileName = "";
	}
	var filePath_hidden = '<input value="' + filePath + '" id="filePath_hidden' + index + '" type="hidden"/>';
	var fun = 'download($(\'#filePath_hidden' + index + '\').val(), \'' + fileName + '\');';
	var download_url = '<a style="float: left;" href="javascript:; ' + fun + '">' + fileName + '</a>';
	return filePath_hidden + download_url;
}

/**
 * 下载文件
 * @param 	filePath 文件路劲
 * @param 	fileName	
 */
function download(filePath, fileName){
	if(filePath==null || filePath==''){
//		$.messager.alert($.i18n.prop('label.remind'),'文件路径不能为空','warning');
		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('base.Check.message17'),'warning');
		return false;
	}
	var url = ctx+'/common/download';
	var fileName = fileName == null ? "" : fileName;
	fileName = fileName.split(".")[0];
//	info("fileName== " + fileName);
//	info("filePath== " + filePath);
//	filePath = $("#filePath_hidden").val();
	var inputs = '<input type="hidden" name="filePath" value="'+filePath+'"><input type="hidden" name="fileName" value="'+fileName+'">';
	jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
    .appendTo('body').submit().remove();
};

/**
 * 确认核对
 */
function ajaxConfirmCheckPrice(){
	$.messager.progress();
	var rows = $('#datagrid-list').datagrid('getChecked');
	info(rows);
	if(rows.length == 0){			//没选中，则终止
//		$.messager.alert($.i18n.prop('label.remind'), "您未选中数据!", 'info');
		$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('delivery.message2'), 'info');
		$.messager.progress('close');
		return;
	}
	
	for(var i in rows){
		var row = rows[i];			//获取单个对象
		if(row.checkItem.exDealStatus == 1){
//			$.messager.alert($.i18n.prop('label.remind'), "包含已确认的数据，不能重复确认!", 'info');
			$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('base.Check.message18'), 'info');
			$.messager.progress('close');
			return;
		}
	}
	
	$.messager.confirm($.i18n.prop('order.confirm'), $.i18n.prop('label.sureToExecute'), function (r) { 
		if (r) {
			RowEditor.accept('datagrid-list');
//			var o = $('#datagrid-list').datagrid('getData');
			var selectRow = $('#datagrid-list').datagrid('getSelections');
			for(var i in selectRow){
				var row = selectRow[i];			//获取单个对象
				var buyerCheckPrice = row.buyerCheckPrice;
				if(buyerCheckPrice == null || buyerCheckPrice == "null"){
//					$.messager.alert($.i18n.prop('label.remind'), "采购商核对单价不能为空!", 'info');
					$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('base.Check.message19'), 'info');
					$.messager.progress('close');
					return;
				}
			}
			var datas = JSON.stringify(selectRow);
			$.ajax({
				url:ctx+'/manager/check/checks/opt/confirmCheckPrice',
				type:'POST',
//				data:datas,
				data: "{\"total\":0,\"rows\":" + datas + "}",
				dataType:"json",
				contentType : 'application/json',
				success:function(data){
					try{
						if(data.success){
							$.messager.show({
								title:$.i18n.prop('label.news'),
								msg:  $.i18n.prop('label.operateOK'), 
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-list').datagrid('reload');
							$.messager.progress('close');
						}else{
							$.messager.progress('close');
							$.messager.alert($.i18n.prop('label.remind'),data.msg,'error');
						}
					}catch (e) {
						$.messager.progress('close');
						$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
					} 
				},
				error:function(v){
					$.messager.progress('close');
					alert($.i18n.prop('receive.message32'));
				}
			});
		}else{
			$.messager.progress('close');
		}
	});
}

/**
 * 检测null的时候，显示空, 并且为外协订单时候，隐藏该列
 */
/*function checkOrderType(r){
	var orderType = r.orderType;
	var recItemAttr5 = r.recItem.attr5;
	if(orderType == 3){
		$('#datagrid-list1').datagrid('hideColumn', 'recItemAttr5'); 
	}else{
		$('#datagrid-list1').datagrid('showColumn', 'recItemAttr5'); 
	}
	if(recItemAttr5 == null || recItemAttr5 == "null"){
		recItemAttr5 = "";
	}
	return recItemAttr5;
}*/

///**
// * 解析同步状态
// * @param 	r	传入的数据
// * @returns		同步状态
// */
//function synicType(r){
//	var backFlag = "";
//	var backStatus = r.backStatus;
//	var html = "";
//	if(backStatus && backStatus.length != 0){								//数据不为空
//		var backStatuses = backStatus.split("|");							//按照|劈开
//		var backStatusesLength = backStatuses.length;
//		info(backStatuses);
//		if(backStatusesLength != 0){										//存在数据，则开始解析
//			info(backStatusesLength);
//			for(var i in backStatuses){										//拼接日志
//				var log = backStatuses[i];
//				if(log && log != null && log.length != 0){
//					var log_arr = log.split("@");
//					if(log_arr && log_arr != null && log_arr.length != 0){
//						html += log_arr[1] + "&nbsp;&nbsp;&nbsp;" + log_arr[2] + "</br>";
//					}
//				}
//			}
//			var lastBackStatus = backStatuses[backStatusesLength - 1];		//获取最后一条数据
//			info(lastBackStatus);
//			if(lastBackStatus && lastBackStatus.length != 0){
//				var lastBackStatuses = lastBackStatus.split("@");
//				if(lastBackStatuses && lastBackStatuses.length != 0){
//					backFlag = lastBackStatuses[0];							//获取最后一条数据的状态
//					if(backFlag == "F"){									//判断同步状态
//						backFlag = "<a href='javascript:; ajaxClickLogs(\"" + html + "\")'><span style='color: red;'>同步失败</span></a>";
////						r.hasError = true;
//					}else if(backFlag == "S"){
//						backFlag = "<a href='javascript:; ajaxClickLogs(\"" + html + "\")'><span style='color: green;'>同步成功</span></a>";
//					}else{
//						backFlag = "<span>暂未同步</span>";
//					}
//				}
//			}
//		}
//	}
//	if(backFlag != null && backFlag == ""){
//		backFlag = "<span>暂未同步</span>";
//	}
//	return backFlag;
//}

/**
 * 解析同步状态
 * @param 	r	传入的数据
 * @returns		同步状态
 */
function synicType(r){
	var backStatus = r.backStatus;
	var col4 = r.col4;
	if(col4 != null && col4.length != 0 && backStatus != null && backStatus.length != 0){
		if(backStatus == "F"){									//判断同步状态
//			backStatus = "<a href='javascript:; ajaxClickLogs(\"" + col4 + "\")'><span style='color: red;'>同步失败</span></a>";
			backStatus = "<a href='javascript:; ajaxClickLogs(\"" + col4 + "\")'><span style='color: red;'>" + $.i18n.prop('base.Check.message20') + "</span></a>";
		}else if(backStatus == "C"){
//			backStatus = "<a href='javascript:; ajaxClickLogs(\"" + col4 + "\")'><span style='color: purple;'>同步中</span></a>";
			backStatus = "<a href='javascript:; ajaxClickLogs(\"" + col4 + "\")'><span style='color: purple;'>" + $.i18n.prop('base.Check.message21') + "</span></a>";
		}else if(backStatus == "S"){
//			backStatus = "<a href='javascript:; ajaxClickLogs(\"" + col4 + "\")'><span style='color: green;'>同步成功</span></a>";
			backStatus = "<a href='javascript:; ajaxClickLogs(\"" + col4 + "\")'><span style='color: green;'>" + $.i18n.prop('base.Check.message22') + "</span></a>";
		}else{
//			backStatus = "<span>暂未同步</span>";
			backStatus = "<span>" + $.i18n.prop('base.Check.message23') + "</span>";
		}
	}else{
//		backStatus = "<span>暂未同步</span>";
		backStatus = "<span>" + $.i18n.prop('base.Check.message23') + "</span>";
	}
	return backStatus;
}

/**
 * 弹出日志
 * @param logs	日志
 */
function ajaxClickLogs(logs){
	$.messager.alert('Info', logs, '');
}

/**
 * 再次提交
 * @param id 对账单id
 */
function ajaxSendAgain(id){
	$.messager.confirm($.i18n.prop('order.confirm'), $.i18n.prop('label.sureToExecute'), function (r) {
		if(r){
			$.messager.progress();
			$.ajax({
				url:ctx+'/manager/check/checks/sendAgain/'+id,
				type:'POST',
				//data:params,
				dataType:"json",
				contentType : 'application/json',
				success:function(data){
					$.messager.progress('close');
					try{
						if(data){ 
							$.messager.alert($.i18n.prop('label.operateOK'),data.msg,'info');
						}else{
							$.messager.alert($.i18n.prop('label.remind'),data.msg,'error');
						}
					}catch (e) {
						$.messager.alert($.i18n.prop('label.remind'),data.msg,'error'); 
					}
					$('#form-check').parents("div.window").find("a.panel-tool-close").click();
				},
				error:function(v){
					$('#form-check').parents("div.window").find("a.panel-tool-close").click();
					$.messager.progress('close');
					$.messager.alert($.i18n.prop('label.operateOK'),data.msg,'error');
				}
			});
		}
	});
}

/**
 * 重载开票画面
 * @param isAbroad	是否国外
 * @param id		重载id
 */
function reloadWindow(isAbroad, id){
	$("a.panel-tool-close").click();
	if(isAbroad){						//国外
		Check.showAbroadDetail(id);
	}else{								//国内，外协
		Check.showDetail(id);
	}
}
