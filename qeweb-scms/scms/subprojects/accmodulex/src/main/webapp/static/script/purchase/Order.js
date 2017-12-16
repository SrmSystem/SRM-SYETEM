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
			var s=' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="feedback.showFeedback(\'/manager/order/purchaseorder/feedback/'+ r.id +'\');">反馈【'+r.feedbackCount+'】</a>';
			return  s;
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
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var reject_ids="";
			for(i = 0; i < selections.length; i ++) {
				reject_ids=reject_ids+selections[i].id+",";
				if(st == "publish" && selections[i].publishStatus == 1) {
					$.messager.alert('提示','包含已发布记录无法重复发布！','error');
					return false;
				} else if(st == "confirm" && selections[i].confirmStatus == 1) {
					$.messager.alert('提示','包含已确认记录无法重复确认！','error');
					return false;
				} else if(st == "reject" && selections[i].confirmStatus == -1) {
					$.messager.alert('提示','包含已驳回记录无法重新驳回！','error');
					return false;
				} else if(st == "close" && selections[i].closeStatus == 1) {
					$.messager.alert('提示','包含已关闭记录无法重复关闭！','error');
					return false;
				} else if(st == "unpublish" && selections[i].confirmStatus == 1){
					$.messager.alert('提示','包含已确认记录无法取消发布！','error');
					return false;
				} else if(st == "reject" && selections[i].confirmStatus != 0) {
					$.messager.alert('提示','包含已确认记录无法重复驳回！','error');
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
							//alert(data);
							debugger;
							$.messager.progress('close');
							try{
								if(data.success){ 
									$.messager.show({
										title:'消息',
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
									$.messager.alert('提示',data.message,'error');
								}
							}catch (e) {
								$.messager.alert('提示',e,'error'); 
							} 
						}
					});
			}
		},showOrderPlanDetail:function(id,vendor) {//查看供货计划详情
			 $dialog = $('<div/>').dialog({     
			        title: '查看采购订单详情',     
			        iconCls : 'pag-search',    
			        closed: true,     
			        cache: false,     
			        href: ctx + '/manager/order/purchaseorder/viewItemDetail/' + id + "/"+vendor,     
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
							title:'消息',
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
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
			}
		});
	},displayOrderViewReject:function(orderItemId,vendor){
		document.getElementById("reject_ids").value=orderItemId;
		document.getElementById("reject_type").value='orderItemView';
		$('#win-reject').window('open'); 
	}
	
}
