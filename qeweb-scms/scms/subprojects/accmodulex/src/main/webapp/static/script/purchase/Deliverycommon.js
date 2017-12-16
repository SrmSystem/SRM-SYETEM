	//发货单号格式化    
	function deliveryCodeFmt(v,r,i){
    	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDelivery('+ r.id +','+r.deliveryStatus+','+r.auditStatus+',\'view\');">' + v + '</a>';
    }

     function deliveryStatusFmt(v,r,i){
			if(r.deliveryStatus == 0)
				return $.i18n.prop('delivery.unshipped')/*未发货*/;
			else if(r.deliveryStatus == 1)
				return $.i18n.prop('delivery.shipped')/*已发货*/;
			else if(r.deliveryStatus == 2)
				return $.i18n.prop('delivery.partshipped')/*部分发货*/;
			return  $.i18n.prop('delivery.unshipped')/*未发货*/;
		}
		
		function receiveStatusFmt(v,r,i){
			if(r.receiveStatus == 0)
				return $.i18n.prop('receive.unreceived');/*未收货*/
			else if(r.receiveStatus == 1)
				return $.i18n.prop('receive.received');  /*已收货*/
			else if(r.receiveStatus == 2)
				return $.i18n.prop('receive.partreceived');   /*'部分收货'*/
			else if(r.receiveStatus == 3)
				return $.i18n.prop('vendor.purchaseJs.ReturnGoods');    /*'退货'*/
			
			return $.i18n.prop('receive.unreceived');  /*'未收货'*/
		}
		
		function auditStatusFmt(v,r,i){
			if(r.auditStatus == 0)
				return $.i18n.prop('status.auditStatus.0')/*'未审核'*/;
			else if(r.auditStatus == 1)
				return $.i18n.prop('status.correctionStatus.2')/*'审核通过'*/;
			else if(r.auditStatus == -1){
				return $.i18n.prop('order.checkReject')/*'审核驳回'*/;
			}
		}
		
