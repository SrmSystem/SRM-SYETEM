var feedback = new Feedback();
var $dialog=null;
/**
 * 订单管理
 */
var Order = {
		/**
		 * 订单行操作列
		 */
		  operateFmt :function(v,r,i) {
			  var s="";
              if(r.zlock == 1){
            	  return  s;
			  }else{
				  s=' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="feedback.showFeedback(\'/manager/order/purchaseorder/feedback/'+ r.id +'\');">'+$.i18n.prop('button.feedback')+'【'+r.feedbackCount+'】</a>';/*反馈*/
				  s=s+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="">'+$.i18n.prop('vendor.purchaseJs.DetailedDeliveryPlan')+'</a>';/*详细送货计划*/
				  //确认的订单可以编辑（供应商不可编辑）
				  if(r.confirmStatus == 1){
					  s=s+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editOrderQry('+ r.id +');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
				  }
				  return  s;
			  }
		},
		/**
		 * 保存拆分的供货计划
		 * @param id 订单明细ID
		 */
		saveSplitItemPlan : function() {
			RowEditorX.accept("datagrid-orderitemplan-list");
			var rows = $('#datagrid-orderitemplan-list').datagrid('getRows');
			var total = 0;
			for(i = 0;i < rows.length;i++) {
				var orderQty = rows[i].orderQty;
				if(typeof(orderQty) != 'undefined' && orderQty == 0){//数量不能为0!
					$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('order.message26'),'error');
					return;
				}
				
				total += parseFloat(rows[i].orderQty);
			}
			var itemTotal = $("#item_order_qty").val();
			var itemId=$("#item_id").val();
			if(total != itemTotal) {
				$.messager.alert($.i18n.prop('label.remind'), $.i18n.prop('order.message12'),'error');
				return;
			}
			$.messager.progress();
			var o =$('#datagrid-orderitemplan-list').datagrid('getData'); 
			var datas = encodeURI(JSON.stringify(o)); 
		  	$.ajax({
			 	url: ctx + '/manager/order/purchaseorder/saveOrderItemPlan',
		        type: 'post',
		        data:  "datas=" + datas +"&itemId=" + itemId + "&orderType=1" , 
		        dataType:"json",
		        success: function (data) {   
					$.messager.progress('close');
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
							
							$('#datagrid-order-list').datagrid('reload');
							$('#datagrid-orderitemplan-list').datagrid('reload');
							
						}else{
							$.messager.alert($.i18n.prop('label.remind'),data.msg,'error', function(){
									$('#datagrid-order-list').datagrid('reload');
							});
						}
					}catch (e) {
						$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
						$.messager.progress('close');
							$('#datagrid-order-list').datagrid('reload');
					} 
		       	}
		      });
		},
		itemPlanOper:function(st){
			var selections = $('#datagrid-orderitemplan-list').datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
				return false;
			}
			var reject_ids="";
			for(i = 0; i < selections.length; i ++) {
				reject_ids=reject_ids+selections[i].id+",";
				if(st == "publish" && selections[i].publishStatus == 1) {
//					$.messager.alert('提示','包含已发布记录无法重复发布！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message5'),'error');
					return false;
				} else if(st == "confirm" && selections[i].confirmStatus == 1) {
//					$.messager.alert('提示','包含已确认记录无法重复确认！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message6'),'error');
					return false;
				}else if(st == "reject" && selections[i].confirmStatus == -1) {
//					$.messager.alert('提示','包含已驳回记录无法重新驳回！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageNotReject0'),'error');
					return false;
				}
				else if(st == "close" && selections[i].closeStatus == 1) {
//					$.messager.alert('提示','包含已关闭记录无法重复关闭！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageColse1'),'error');
					return false;
				} else if(st == "unpublish" && selections[i].confirmStatus == 1){
//					$.messager.alert('提示','包含已确认记录无法取消发布！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageCancelRelease2'),'error');
					return false;
				}
			}
			
			
			//1、驳回  则打开驳回窗口
			if(st == "reject"){
				document.getElementById("reject_ids").value=reject_ids;
				document.getElementById("reject_type").value='orderPlan';
				$('#win-reject').window('open');   
			}else{
					$.messager.progress();
					var params = $.toJSON(selections);
					$.ajax({
						url:ctx + '/manager/order/purchaseorder/' + st + 'ItemPlan',
						type:'POST',
						data:params,
						dataType:"json",
						contentType : 'application/json',
						success:function(data){
							$.messager.progress('close');
							try{
								if(data.success){ 
									$.messager.show({
										title:$.i18n.prop('label.news')/*'消息'*/,
										msg:  data.message, 
										timeout:2000,
										showType:'show',
										style:{
											right:'',
											top:document.body.scrollTop+document.documentElement.scrollTop,
											bottom:''
										}
									});
									$('#datagrid-order-list').datagrid('reload');
									$('#datagrid-orderitemplan-list').datagrid('reload');
								}else{
			//						$.messager.alert('提示',data.message,'error');
									$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
								}
							}catch (e) {
			//					$.messager.alert('提示',e,'error'); 
								$.messager.alert($.i18n.prop('label.remind'),e,'error');
							} 
						}
					});
			}
		},showOrderPlanDetail:function(id,vendor) {//查看供货计划详情
			 $dialog = $('<div/>').dialog({     
//			        title: '查看采购订单供货计划详情',
				    title: $.i18n.prop('vendor.purchaseJs.MessageLookDetail3'),   
			        iconCls : 'pag-search',    
			        closed: true,     
			        cache: false,     
			        href: ctx + '/manager/order/purchasemainorder/viewItemDetail/' + id + "/"+vendor,     
			        modal: true,  
			        maximizable:true,
			        maximized:true,
			        onLoad:function(){  
			        	
			        },               
			        onClose:function(){
			            $(this).dialog('destroy');
			        },
			        buttons : [ 
			         ]  

			   });    
			  $dialog.dialog('open');
		
	},orderViewOperOrderItem:function(orderItemId,vendor,type){//采购单详情--采购订单明细单个操作-发布，取消发布，确认，驳回
		var url="";
		if("publish"==type){//发布
			url=ctx +'/manager/order/purchaseorder/publishSingleOrderItem?orderItemId='+orderItemId;
		}else if("unpublish"==type){
			url=ctx +'/manager/order/purchaseorder/unpublishSingleOrderItem?orderItemId='+orderItemId;
		}else if("confirm"==type){
			url=ctx +'/manager/order/purchaseorder/confirmSingleOrderItem?orderItemId='+orderItemId;
		}
		$.messager.progress();
		$.ajax({
			url: url, 
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:  data.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						
						$dialog.dialog('destroy');//关闭弹出框
						Order.showOrderPlanDetail(orderItemId,vendor);//重新打开，刷新页面
					}else{
						$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
					}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
				} 
			}
		});
	},displayOrderViewReject:function(orderItemId,vendor){
		document.getElementById("reject_ids").value=orderItemId;
		document.getElementById("reject_type").value='orderItemView';
		$('#win-reject').window('open'); 
	},showOrderItemList:function(id,vendor) {//查看供货明细
		 $dialog = $('<div/>').dialog({     
//		        title: '采购订单列表',
			    title: $.i18n.prop('vendor.purchaseJs.MessageOrderList4'), 
		        iconCls : 'pag-search',    
		        closed: true,     
		        cache: false,     
		        href: ctx + '/manager/order/purchasemainorder/viewItemList/' + id + "/"+vendor,     
		        modal: true,  
		        maximizable:true,
		        maximized:true,
		        onLoad:function(){  
		        	
		        },               
		        onClose:function(){
		            $(this).dialog('destroy');
		        },
		        buttons : [ 
		         ]  

		   });    
		  $dialog.dialog('open');
	
}
	
}


//订单的操作
var orderOpt={
		//发布、确认、关闭
		operateOrder:function(tableId,st,orderType){
			var _tableId = "#"+tableId;
			var selections = $(_tableId).datagrid('getSelections');
			if(selections.length==0){
//				$.messager.alert('提示','没有选择任何记录！','info');
				$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
				return false;
			}
			var reject_ids="";
			for(i = 0; i < selections.length; i ++) {
				reject_ids=reject_ids+selections[i].id;
				if(st == "publish" && selections[i].publishStatus == 1) {
//					$.messager.alert('提示','包含已发布记录无法重复发布！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message5'),'error');
					return false;
				} else if(st == "confirm" && selections[i].confirmStatus == 1) {
	//				$.messager.alert('提示','包含已确认记录无法重复确认！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message6'),'error');
					return false;
				}
				
				else if(st == "confirm" && selections[i].loekz	 == "X") {
//					$.messager.alert('提示','包含删除标识数据无法确认！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageDeleteMarkData5'),'error');
					return false;
				}else if(st == "confirm" && selections[i].lockStatus == 1) {
//					$.messager.alert('提示','包含冻结标识数据无法确认！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.freezeIdentificationData6'),'error');
					return false;
				}else if(st == "confirm" && selections[i].zlock	 == "X") {
//					$.messager.alert('提示','包含锁定标识数据无法确认！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageLocked7'),'error');
					return false;
				}else if(st == "confirm" && selections[i].elikz == "X") {
//					$.messager.alert('提示','包含交货已完成数据无法确认！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageComplent8'),'error');
					return false;
				}else if(st == "confirm" && selections[i].bstae == "X") {
//					$.messager.alert('提示','存在非内向交货单标识无法确认','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageNoIntrovert9'),'error');
					return false;
				}
				
				
				
				
				else if(st == "reject" && selections[i].confirmStatus == -1) {
//					$.messager.alert('提示','订单只允许驳回一次！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageRejectONE10'),'error');
					return false;
				}else if(st == "close" && selections[i].closeStatus == 1) {
//					$.messager.alert('提示','包含已关闭记录无法重复关闭！','error');
					$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.MessageColse1'),'error');
					return false;
				}
			}
			
			var url="";
			var reject_type="";
			if(orderType == "order"){
				url=ctx + '/manager/order/purchasemainorder/' + st + 'Order';
				reject_type='order';
			}else if(orderType == "orderItem"){
				url=ctx + '/manager/order/purchasemainorder/' + st + 'OrderItem';
				reject_type='orderItem';
			}
			
			
			//1、驳回  则打开驳回窗口
			if(st == "reject"){
				document.getElementById("reject_ids").value=reject_ids;
				document.getElementById("reject_type").value=reject_type;
				$('#win-reject').window('open');   
			}else{
					//2、其他走后台方法
					$.messager.progress();
					var params = $.toJSON(selections);
					$.ajax({
						url:url,
						type:"POST",
						data:params,
						dataType:"json",
						contentType : 'application/json',
						success:function(data){
							$.messager.progress('close');
							try{
								if(data.success){ 
									$.messager.show({
										title:$.i18n.prop('label.news')/*'消息'*/,
										msg:  data.message, 
										timeout:2000,
										showType:'show',
										style:{
											right:'',
											top:document.body.scrollTop+document.documentElement.scrollTop,
											bottom:''
										}
									});
									$(_tableId).datagrid('reload'); 
								}else{
									$.messager.alert($.i18n.prop('label.remind'),data.message,'error');
								}
							}catch (e) {
								$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
							} 
						}
					});
			}
		},
		rejectOper:function (){ //驳回订单
			var reject_type=document.getElementById("reject_type").value;
			if("orderItem"==reject_type || "orderItemView"==reject_type){

//				$.messager.confirm('提示','确定要驳回？<font style="color: #F00;font-weight: 900;"></font>',function(r){
				$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.ConfimReject11')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){
					if(r){
						$.messager.progress({
							title:$.i18n.prop('label.remind')/*'提示'*/,
							msg : $.i18n.prop('base.Mail.Submiting')/*'提交中...'*/
						});
						$('#form-reject').form('submit',{
							ajax:true,
							iframe: true,    
							url:ctx+'/manager/order/purchasemainorder/rejectOrderItem', 
							success:function(data){
								$.messager.progress('close');
								var obj = JSON.parse(data);
								try{
									if(obj.success){ 
										$.messager.show({
											title:$.i18n.prop('label.news')/*'消息'*/,
											msg:  obj.message, 
											timeout:2000,
											showType:'show',
											style:{
												right:'',
												top:document.body.scrollTop+document.documentElement.scrollTop,
												bottom:''
											}
										});
										$('#win-reject').window('close');
										if("orderItemView"==reject_type){
											//订单详情驳回后刷新dialog
										$dialog.dialog('destroy');//关闭弹出框
										   Order.showOrderPlanDetail(document.getElementById("reject_ids").value,'true');//重新打开，刷新页面
										}
										$('#datagrid-order-list').datagrid('reload');
										$('#datagrid-orderItem-list').datagrid('reload');
									}else{
										$.messager.alert($.i18n.prop('label.remind'),obj.message,'error');
									}
								}catch (e) {
									$.messager.alert($.i18n.prop('label.remind'),e,'error'); //提示
								} 
							}
						});
					}
				});

			}else if("orderPlan"==reject_type){
				$.messager.progress();
				$('#form-reject').form('submit',{
					ajax:true,
					iframe: true,    
					url:ctx+'/manager/order/purchasemainorder/rejectItemPlan', 
					success:function(data){
						$.messager.progress('close');
						var obj = JSON.parse(data);
						try{
							if(obj.success){ 
								$.messager.show({
									title:$.i18n.prop('label.news')/*'消息'*/,
									msg:  obj.message, 
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#win-reject').window('close');
								$('#datagrid-order-list').datagrid('reload');
								$('#datagrid-orderItem-list').datagrid('reload');
								$('#datagrid-orderitemplan-list').datagrid('reload');
							}else{
								$.messager.alert($.i18n.prop('label.remind'),obj.message,'error');
							}
						}catch (e) {
							$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
						} 
					}
				});
			}else if("order"==reject_type){
				
//				$.messager.confirm('提示','确定要驳回？<font style="color: #F00;font-weight: 900;"></font>',function(r){
				$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.ConfimReject11')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){
					if(r){
						$.messager.progress({
							title:$.i18n.prop('label.remind'),/*提示*/
							msg : $.i18n.prop('base.Mail.Submiting')/*'提交中...'*/
						});
						$('#form-reject').form('submit',{
							ajax:true,
							iframe: true,    
							url:ctx+'/manager/order/purchasemainorder/rejectOrder', 
							success:function(data){
								$.messager.progress('close');
								var obj = JSON.parse(data);
								try{
									if(obj.success){ 
										$.messager.show({
											title:$.i18n.prop('label.news')/*'消息'*/,
											msg:  obj.message, 
											timeout:2000,
											showType:'show',
											style:{
												right:'',
												top:document.body.scrollTop+document.documentElement.scrollTop,
												bottom:''
											}
										});
										$('#win-reject').window('close');
										$('#datagrid-order-list').datagrid('reload'); 
									}else{
										$.messager.alert($.i18n.prop('label.remind'),obj.message,'error');/*提示*/
									}
								}catch (e) {
									$.messager.alert($.i18n.prop('label.remind'),e,'error'); /*提示*/
								} 
							}
						});
					}
				});

			}
		}
}
