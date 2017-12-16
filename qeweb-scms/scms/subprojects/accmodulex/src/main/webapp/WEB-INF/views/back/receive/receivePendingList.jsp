<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.receive.goodsManagement"/><!-- 收货管理 --></title>
	<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>
	<script type="text/javascript">
		function deliveryCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDelivery('+ r.id +','+r.receiveStatus+');">' + v + '</a>';
		}
		
		function deliveryStatusFmt(v,r,i){
			if(r.deliveryStatus == 0)
				return '<spring:message code="vendor.receive.notDeliverGoods"/>';/* 未发货 */
			else if(r.deliveryStatus == 1)
				return '<spring:message code="vendor.receive.shipped"/>';/* 已发货 */
			else if(r.deliveryStatus == 2)
				return '<spring:message code="vendor.receive.partShipment"/>';/* 部分发货 */
			
			return '<spring:message code="vendor.receive.notDeliverGoods"/>';/* 未发货 */
		}
		
		function receiveStatusFmt(v,r,i){
			if(r.receiveStatus == 0)
				return '<spring:message code="vendor.receive.notReceiving"/>';/* 未收货 */
			else if(r.receiveStatus == 1)
				return '<spring:message code="vendor.receive.goods"/>';/* 已收货 */
			else if(r.receiveStatus == 2)
				return '<spring:message code="vendor.receive.partGoods"/>';/* 部分收货 */
			
			return '<spring:message code="vendor.receive.notReceiving"/>';/* 未收货 */
		}
		
		$.extend($.fn.datagrid.methods, {
			editCell: function(jq,param){
				return jq.each(function(){
					var opts = $(this).datagrid('options');
					var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
						col.editor1 = col.editor;
						if (fields[i] != param.field){
							col.editor = null;
					}
					}
					$(this).datagrid('beginEdit', param.index);
					for(var i=0; i<fields.length; i++){
						var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor = col.editor1;
				}
			});
		}
		});
		
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#datagrid-deliveryitem-list').datagrid('validateRow', editIndex)){
				$('#datagrid-deliveryitem-list').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickCell(index, field){
			if (endEditing()){
				$('#datagrid-deliveryitem-list').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-delivery-list" title="收货看板列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/receive/pending',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#deliveryListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'deliveryCode',formatter:deliveryCodeFmt"><spring:message code="vendor.receive.ASNinvoiceNo"/><!-- ASN发货单号 --></th>
		<th data-options="field:'shipType',formatter:function(v,r,i){if(v=='1') return '正常';else if(v=='-1') return '补货';}"><spring:message code="vendor.receive.deliveryType"/><!-- 发货类型 --></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<!-- <th data-options="field:'buyerCode',formatter:function(v,r,i){return r.buyer.code;}">采购组织编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return r.buyer.name;}">采购组织名称</th> -->
		<th data-options="field:'purchasingGroupCode'"><spring:message code="vendor.receive.purchasingGroupCoding"/><!-- 采购组编码 --></th>
		<th data-options="field:'purchasingGroupName'"><spring:message code="vendor.receive.namePurchasingGroup"/><!-- 采购组名称 --></th>
		<th data-options="field:'deliveryStatus',formatter:deliveryStatusFmt"><spring:message code="vendor.receive.deliveryStatus"/><!-- 发货状态 --></th>
		<th data-options="field:'deliveyUserName',formatter:function(v,r,i){if(r.deliveryUser) return r.deliveryUser.name; return '';}"><spring:message code="vendor.receive.consignor"/><!-- 发货人 --></th>
		<th data-options="field:'deliveyTime'"><spring:message code="vendor.receive.deliveryTime"/><!-- 发货时间 --></th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt"><spring:message code="vendor.receive.stateGoods"/><!-- 收货状态 --></th>   
		</tr></thead>
	</table>
	<div id="deliveryListToolbar" style="padding:5px;">
		<div>
<%-- 		<shiro:hasPermission name="rec:pending:receiveSelect"> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="receiveSelect()">收货</a>
		</shiro:hasPermission> --%>
		</div>
		<div>
			<form id="form-delivery-search" method="post">
			<spring:message code="vendor.receive.ASNinvoiceNo"/><!-- ASN发货单号 -->：<input type="text" name="search-LIKE_deliveryCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.receive.purchasingGroupCoding"/><!-- 采购组编码 -->：<input type="text" name="search-LIKE_purchasingGroup.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.receive.namePurchasingGroup"/><!-- 采购组名称 -->：<input type="text" name="search-LIKE_purchasingGroup.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.receive.stateGoods"/><!-- 收货状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="0"><spring:message code="vendor.receive.notReceiving"/><!-- 未收货 --></option><option value="2"><spring:message code="vendor.receive.partGoods"/><!-- 部分收货 --></option><option value="1"><spring:message code="vendor.receive.goods"/><!-- 已收货 --></option></select>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPending()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-delivery-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	<%-- <div id="win-delivery-detail" class="easyui-window" title="发货单详情" style="width:950px;height:500px"
		data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-deliveryitem-search" method="post" >
				<div id="deliveryListToolbar" style="padding:5px;">
						<input id="id" name="id" value="-1" type="hidden"/>
					<div id="opt">
						<shiro:hasPermission name="rec:pending:receiveSelect"> 
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="receive()">收货</a>
						</shiro:hasPermission>
					</div>
					<div>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>ASN发货单号:</td><td><input class="easyui-textbox" id="deliveryCode" name="deliveryCode" type="text" readonly="true"/></td>
							<td>收货方:</td><td><input class="easyui-textbox" id="receiveOrg" name="receiveOrg" type="text" readonly="true"/></td>
							<td>发货时间:</td><td><input class="easyui-textbox" id="deliveyTime" name="deliveyTime" type="text" readonly="true"/></td>
						</tr>
						<tr>
							<td>供应商编码:</td><td><input class="easyui-textbox" id="vendor.code" name="vendor.code" type="text" readonly="true"/></td>
							<td>供应商名称:</td><td><input class="easyui-textbox" id="vendor.name" name="vendor.name" type="text" readonly="true"/></td>
							<td>订单类型:</td>
							<td>
								<select class="easyui-combobox" style="width:148px;" id="deliveryType" name="deliveryType" type="text" readonly="true">
									<option value="1">国内</option>
									<option value="2">国外</option>
									<option value="3">外协</option>
									<option value="4">反向生成</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>采购组织编码:</td><td><input class="easyui-textbox" id="buyer.code" name="buyer.code" type="text" readonly="true"/></td>
							<td>采购组织名称:</td><td><input class="easyui-textbox" id="buyer.name" name="buyer.name" type="text" readonly="true"/></td>
							<td>发货单创建人:</td><td><input class="easyui-textbox" id="createUserName" name="createUserName" type="text" readonly="true"/></td>
						</tr>
						<tr>
							<td>收货地址:</td><td><input class="easyui-textbox" id="deliveryAddress" name="deliveryAddress" type="text" readonly="true"/></td>
							<td>收货联系人:</td><td><input class="easyui-textbox" id="deliveryContacter" name="deliveryContacter" type="text" readonly="true"/></td>
							<td>收货联系电话:</td><td><input class="easyui-textbox" id="deliveryTel" name="deliveryTel" type="text" readonly="true"/></td>
						</tr>
						<tr>
							<td>物流公司:</td><td><input class="easyui-textbox" id="logisticsCompany" name="logisticsCompany" type="text" readonly="true"/></td>
							<td>物流联系人:</td><td><input class="easyui-textbox" id="logisticsContacter" name="logisticsContacter" type="text" readonly="true"/></td>
							<td>物流联系电话:</td><td><input class="easyui-textbox" id="logisticsTel" name="logisticsTel" type="text" readonly="true"/></td>
						</tr>
						<tr>
							<td>预计到货时间:</td><td><input class="easyui-textbox" id="expectedArrivalTime" name="expectedArrivalTime" type="text" readonly="true"/></td>
							<td>收货人:</td><td><input class="easyui-textbox" id="createUserName" name="createUserName" type="text" readonly="true"/></td>
							<td>收货时间:</td><td><input class="easyui-textbox" id="deliveyTime" name="deliveyTime" type="text" readonly="true"/></td>
						</tr>
						<tr>
							<td>附件:</td>
							<td><a href="javascript:;" onclick="downFile()"><input class="easyui-textbox" id="deliveryFileName" name="deliveryFileName" type="text" readonly="true"/></a></td>
							<input id="deliveryFilePath" name="deliveryFilePath" type="hidden" />
						</tr>
					</table>
					</div>
				</div>
			</form>
			<table id="datagrid-deliveryitem-list" title="发货单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#deliveryItemToolbar',onClickCell: onClickCell,
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'orderCode', formatter:function(v,r,i){return r.orderCode}">采购订单号</th>
				<th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}">物料编码</th>
				<th data-options="field:'materialName', formatter:function(v,r,i){return r.material.name}">物料名称</th>
				<th data-options="field:'deliveryQty'">发货数量</th>
				<th data-options="field:'toreceiveQty',editor:'numberbox',required:true">收货数量</th>  
				<th data-options="field:'toreturnQty',editor:'numberbox'">验退数量</th>  
				<th data-options="field:'receiveStatus',formatter:receiveStatusFmt">收货状态</th>
				<th data-options="field:'unitName'">单位</th>
				<th data-options="field:'requestTime'">要求到货时间</th>
				</tr></thead>
			</table>
		</div>
	</div> --%>
	
<script type="text/javascript">
function searchPending(){
	var searchParamArray = $('#form-delivery-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-delivery-list').datagrid('load',searchParams);
}

//查看发货单详情
function showDelivery(id,receiveStatus){
	/* $('#form-deliveryitem-search').form('clear');
	$('#win-delivery-detail').window('open');
	$('#form-deliveryitem-search').form('load','${ctx}/manager/order/delivery/getDelivery/'+id);
	//详情
	$('#datagrid-deliveryitem-list').datagrid({   
    	url:'${ctx}/manager/order/delivery/deliveryitem/' + id     
	});
	$('#datagrid-deliveryitem-list').datagrid('load',{});
	if(receiveStatus!=1){
		 document.getElementById("opt").style.display = "block";
	}else{
		  document.getElementById("opt").style.display = "none";
	} */
	var clientWidth = document.body.clientWidth;	
	 var clientHeight = document.body.clientHeight;	
	var title='<spring:message code="vendor.receive.detailsInvoice"/>';/* 发货单详情 */
	new dialog().showWin(title, 1000, clientHeight, '${ctx}/manager/order/delivery/getAsnInfo/'+id+'/'+receiveStatus);
	
}

//选择上传文件勾选复选框
function fileChange(thisObj){
	$(thisObj).parent().prev().children().attr("checked", "checked");
	var fileId=$(thisObj).attr("id");
	var checkId="#check_"+fileId;
	$(checkId).attr("checked", "checked");
}

//批量收货
function receiveSelect(t) {
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.receive.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].receiveStatus > 0) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.receive.containsRecordCannotRepeated"/>！'/* 包含已收货记录无法重复收货 */,'error');
			return false;
		}
	}
	var params = $.toJSON(selections);
	$.messager.confirm('<spring:message code="vendor.receive.operatingHints"/>'/* 操作提示 */, '<spring:message code="vendor.receive.areWantDo"/>？'/* 确定要执行操作吗 */, function (data) {
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/order/receive/doreceive',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'<spring:message code="vendor.news"/>',/* 消息 */
        					msg:data.message,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-delivery-list').datagrid('reload');
        			}
        		}
        	});
        }
    });
}

//单张收货
function receive() {
	$.messager.progress();
	var deliveryId = $('#deliveryId').val(); //发货单主表ID
	/* var receiveUser = $('#receiveUser').val(); //收货人
	var receiveTime = $('#receiveTime').val(); //收货时间 */
	var checkdId="";
	var objs=$("#buyerPart :checkbox[name^='checkName']:checked");
	objs.each(function(){
	    	checkdId += $(this).val() + ",";
	});
	
	$('#datagrid-deliveryitem-list').datagrid('acceptChanges');//保存表格中的数据
	var rows = $('#datagrid-deliveryitem-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		var toreceiveQty=rows[i].toreceiveQty+'';
		 if(toreceiveQty == null || toreceiveQty == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.receive.quantityGoodsEmpty"/>'/* 收货数量不能为空 */,'error');
			return false;
		} 
		 var toreturnQty=rows[i].toreturnQty+'';
		 if(toreturnQty == null || toreturnQty == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.receive.checkNumberEmpty"/>'/* 验退数量不能为空 */,'error');
			return false;
		} 
    } 
	var o =$('#datagrid-deliveryitem-list').datagrid('getData'); 
	//var datas = encodeURI(JSON.stringify(o)); 
	var datas = $.toJSON(o); 
	$.messager.confirm('<spring:message code="vendor.receive.operatingHints"/>'/* 操作提示 */, '<spring:message code="vendor.receive.areWantDo"/>？'/* 确定要执行操作吗 */, function (r) {
        if (r) {
        	/* $.ajax({
        		url:'${ctx}/manager/order/receive/doreceivesingle',
        		type:'POST',
        		data:  "datas=" + datas +"&deliveryId=" + deliveryId,
        		dataType:"json",
        		success:function(data){
        			$.messager.progress('close');
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg:data.message,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#win-delivery-detail').window('close');
        				$('#datagrid-delivery-list').datagrid('reload');
        			}
        		}
        	}); */
        	$('#form-deliveryitem-search').form('submit',{
    			ajax:true,
    			iframe: true,    
    			url:'${ctx}/manager/order/receive/doreceivesingle',  
    			onSubmit:function(param){
    			/* 	param.receiveUser = receiveUser,
    				param.receiveTime = receiveTime, */
    				param.checkdId = checkdId, 
    				param.deliveryId = deliveryId,
    				param.datas = datas;
    			},
    			success:function(data){
    				$.messager.progress('close');
    				var result = eval('('+data+')');
    				if(result.success){
	    				$.messager.show({
	    					title:'<spring:message code="vendor.news"/>',/* 消息 */
	    					msg:result.message,
	    					timeout:2000,
	    					showType:'show',
	    					style:{
	    						right:'',
	    						top:document.body.scrollTop+document.documentElement.scrollTop,
	    						bottom:''
	    					}
	    				});
	    				$(".panel-tool-close").click();//执行了关闭的事件
	    				$('#datagrid-delivery-list').datagrid('reload');
    				}
    			}
    		});
        } else {
        	$.messager.progress('close');
        }
    });
}

function downFile(){
	var fileName = $("#deliveryFileName").val(); //附件名称
	var fileUrl = $("#deliveryFilePath").val(); //附件地址
	if(fileName==''||fileUrl==''){
		return ;
	}
	var url = ctx+'/manager/order/delivery/downloadFile';
	var inputs = '<input type="hidden" name="fileUrl" value="'+fileUrl+'">'+'<input type="hidden" name="fileName" value="'+fileName+'">';
	
	jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
    .appendTo('body').submit().remove();
}
</script>
</body>
</html>
