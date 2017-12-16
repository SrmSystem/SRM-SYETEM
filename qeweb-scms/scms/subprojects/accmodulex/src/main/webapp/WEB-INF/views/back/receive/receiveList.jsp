<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.receive.goodsManagement"/><!-- 收货管理 --></title>
	<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
	<script type="text/javascript">
		function receiveCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showReceive('+ r.id +');">' + r.delivery.deliveryCode + '</a>';
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
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-receive-list" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/receive',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#receiveListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
	<!-- 	<th data-options="field:'receiveCode',formatter:receiveCodeFmt">收货单号</th> -->
		<th data-options="field:'deliveryCode',formatter:receiveCodeFmt"><spring:message code="vendor.receive.ASNinvoiceNo"/><!-- ASN发货单号 --></th>
		<th data-options="field:'shipType',formatter:function(v,r,i){if(r.delivery.shipType=='1') return '正常';else if(r.delivery.shipType=='-1') return '补货';}"><spring:message code="vendor.receive.deliveryType"/><!-- 发货类型 --></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return isNULL(isNULL(r.vendor).code);}"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return isNULL(isNULL(r.vendor).name);}"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<!-- <th data-options="field:'buyerCode',formatter:function(v,r,i){return isNULL(isNULL(r.buyer).code);}">采购组织编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return isNULL(isNULL(r.buyer).name);}">采购组织名称</th> -->
		<th data-options="field:'purchasingGroupCode'"><spring:message code="vendor.receive.purchasingGroupCoding"/><!-- 采购组编码 --></th>
		<th data-options="field:'purchasingGroupName'"><spring:message code="vendor.receive.namePurchasingGroup"/><!-- 采购组名称 --></th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt"><spring:message code="vendor.receive.stateGoods"/><!-- 收货状态 --></th>
		<th data-options="field:'receiveTime'"><spring:message code="vendor.receive.sysTime"/><!-- 收货时间 --></th>
		<th data-options="field:'delivery.deliveyTime'"><spring:message code="vendor.receive.deliveryTime"/><!-- 发货时间 --></th>
		</tr></thead>
	</table>
	<div id="receiveListToolbar" style="padding:5px;">
		<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="vendor.synchronizing"/><!-- 同步 --></a> 
		</div>
		<div>
			<form id="form-receive-search" method="post">
			<spring:message code="vendor.receive.ASNinvoiceNo"/><!-- ASN发货单号 -->：<input type="text" name="search-LIKE_delivery.deliveryCode" class="easyui-textbox" style="width:80px;"/>
		<!-- 	采购商编码：<input type="text" name="search-LIKE_buyer.code" class="easyui-textbox" style="width:80px;"/>
			采购商名称：<input type="text" name="search-LIKE_buyer.name" class="easyui-textbox" style="width:80px;"/> -->
			<spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/><br>
			<spring:message code="vendor.receive.goodsTime"/><!-- 收货时间 -->： <input type="text" name="search-GTE_receiveTime" data-options="editable:false" class="easyui-datebox" >~  
					        <input type="text" name="search-LTE_receiveTime" data-options="editable:false" class="easyui-datebox" >
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchReceive()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-receive-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	<!-- 导入收货单 -->
	<div id="win-receive-import" class="easyui-window" title="导入收货单" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-receive-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/receive/filesUpload"> 
				<div style="margin-bottom:20px">
					<spring:message code="vendor.file"/><!-- 文件 -->：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/template/Receives.xls">收货单模版.xls</a> --%>   
					<spring:message code="vendor.formwork"/><!-- 模板 -->：<a href="javascript:;" onclick="File.download('WEB-INF/template/Receives.xls','<spring:message code="vendor.receive.takeTemplate"/><!-- 收货单模版 -->')"><spring:message code="vendor.receive.takeTemplate"/><!-- 收货单模版 -->.xls</a> 
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportreceive();"><spring:message code="vendor.preservation"/><!-- 保存 --></a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-receive-import').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>  
		</div>
	</div>
	
	<!-- <div id="win-receive-detail" class="easyui-window" title="收货单详情" style="width:950px;height:500px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id=receiveItemToolbar" style="padding:5px;">
				<div>
					<form id="form-receiveitem-search">
					<input id="id" name="id" value="-1" type="hidden"/>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>供应商编码:</td><td><input class="easyui-textbox" id="vendor.code" name="vendor.code" type="text" readonly="true"/></td>
							<td>供应商名称:</td><td><input class="easyui-textbox" id="vendor.name" name="vendor.name" type="text" readonly="true"/></td>
							<td>订单类型:</td>
							<td>
								<select class="easyui-combobox" style="width:148px;" id="orderType" name="orderType" type="text" readonly="true">
									<option value="1">国内</option>
									<option value="2">国外</option>
									<option value="3">外协</option>
									<option value="4">反向生成</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>ASN发货单号:</td><td><input class="easyui-textbox" id="delivery.deliveryCode" name="delivery.deliveryCode" type="text" readonly="true"/></td>
							<td>收货人:</td><td><input class="easyui-textbox" id="createUserName" name="createUserName" type="text" readonly="true"/></td>
							<td>发货时间:</td><td><input class="easyui-textbox" id="delivery.deliveyTime" name="delivery.deliveyTime" type="text" readonly="true"/></td>
						</tr>
						<tr>
							<td>收货单号：</td><td><input type="text" name="receiveCode" class="easyui-textbox"  readonly="readonly"/></td>
							<td>收货方:</td><td><input class="easyui-textbox" id="receiveOrg" name="receiveOrg" type="text" readonly="true"/></td>
							<td>收货时间:</td><td><input class="easyui-textbox" id="receiveTime" name="receiveTime" type="text" readonly="true"/></td>
						</tr>
						</table>
					收货时间：<input type="week" name="receiveTime" class="easyui-textbox" style="width:120px;" readonly="readonly"/>
					</form>  
				</div>
			</div>
			<table id="datagrid-receiveitem-list" title="收货单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#receiveItemToolbar',
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'orderCode', formatter:function(v,r,i){return r.orderItem.order.orderCode}">采购订单号</th>
				<th data-options="field:'materialCode'">物料编码</th>
				<th data-options="field:'materialName'">物料名称</th>
				<th data-options="field:'deliveryQty'">发货数量</th>
				<th data-options="field:'receiveQty'">收货数量</th>
				<th data-options="field:'inStoreQty'">入库数量</th>
				<th data-options="field:'returnQty'">退货数量</th>
				<th data-options="field:'unitName'">单位</th>
				<th data-options="field:'rquestTime'">要求到货时间</th>
				<th data-options="field:'deliveryCreateTime'">发货单创建时间</th>
				<th data-options="field:'publishTime'">发货时间</th>
				</tr></thead>
			</table>
		</div>
	</div> -->
	
			 <!-- 同步收发货加护 -->
 	<div id="win-sync-addoredit" class="easyui-window"
		style="width: 500px; height: 150px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder1-time'">
		<form id="form-sync-addoredit" method="post">
			<table style="text-align: right; padding: 5px; margin: auto;" cellpadding="5">
			<tr>
				<td ><spring:message code="vendor.receive.selectDate"/><!-- 选择日期 -->:</td>
				<td >
					<input type="text"  id ="receiveTimeS"  name="receiveTimeS" data-options="editable:false" class="easyui-datebox" >-
					<input type="text"  id ="receiveTimeE"  name="receiveTimeE" data-options="editable:false" class="easyui-datebox" >
				</td>
			</tr>
			</table>
		</form>
		<div id="dialog-adder1-time">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="sycReceive()"><spring:message code="vendor.submit"/><!-- 提交 --></a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-sync-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		</div>
	</div>
	
	
	
	
<script type="text/javascript">
function searchReceive(){
	var searchParamArray = $('#form-receive-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-receive-list').datagrid('load',searchParams);
}

//查看发货单详情
function showReceive(id){
	/* $('#form-receiveitem-search').form('clear');
	$('#win-receive-detail').window('open');
	$('#form-receiveitem-search').form('load','${ctx}/manager/order/receive/getReceive/'+id);
	//详情
	$('#datagrid-receiveitem-list').datagrid({   
    	url:'${ctx}/manager/order/receive/receiveitem/' + id     
	});
	$('#datagrid-receiveitem-list').datagrid('load',{}); */
	var clientWidth = document.body.clientWidth;	
	 var clientHeight = document.body.clientHeight;	
	var title='<spring:message code="vendor.receive.particularsReceipt"/>';	/* 收货单详情 */
	new dialog().showWin(title, 1000, clientHeight, '${ctx}/manager/order/receive/geReceiveDetailt/'+id);
}

//导入收货单
function importReceive() {
	$('#form-receive-import').form('clear');   
	$('#win-receive-import').window('open');  
}

//保存收货单
function saveimportreceive() {
	$.messager.progress();
	$('#form-receive-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/order/receive/filesUpload', 
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
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.receive.importPurchaseSuccess"/>'/* 导入采购订单成功 */,'info');
				$('#win-receive-import').window('close');
				$('#datagrid-receive-list').datagrid('reload');
			}else{
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg + '<br><spring:message code="vendor.receive.referImportLog"/><a href="javascript:;" onclick="File.showLog(\''+ result.log + '\)"><b><spring:message code="vendor.receive.logFile"/></b></a>' ,'error');/* 导入日志请参阅 *//* 日志文件 */
			}
			}catch (e) {  
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
			}
		}
	});
}

//同步要货计划
function sycOrder(){
		$('#win-sync-addoredit').dialog({
			iconCls:'icon-edit',
			title:'<spring:message code="vendor.receive.synchronizeDeliveryDetails"/>'/* 同步收发货明细 */
		});
		$('#form-sync-addoredit').form('clear');
		$('#win-sync-addoredit').dialog('open');
		
		//将时间赋值当前的时间
		$('#receiveTimeS').datebox('setValue', formatterDateS(new Date()));
		$('#receiveTimeE').datebox('setValue', formatterDateE(new Date()));
		
}



//得到当前日期
formatterDateE = function(date) {
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
	+ (date.getMonth() + 1);
	return date.getFullYear() + '-' + month + '-' + day;
};

//得到当前日期
formatterDateS = function(date) {
	date.setDate(date.getDate()-1);
	var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
	var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
	+ (date.getMonth() + 1);
	return date.getFullYear() + '-' + month + '-' + day;
};




function sycReceive(){
	 var timeS =$('#receiveTimeS').datetimebox('getValue');
	 var timeE =$('#receiveTimeE').datetimebox('getValue');
	 debugger;
     if(timeS == ""  || timeS  == null){
    	 $.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.receive.timeCannotEmpty"/>！'/* 时间不能为空 */,'error');
    	 return ;
     }
	 
	$.messager.progress();
	$.ajax({
		url:'${ctx}/manager/order/receive/sycOrder/'+timeS+'/'+timeE,
		type:'POST',
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="vendor.news"/>',/* 消息 */
						msg:  data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-receive-list').datagrid('reload'); 
				}else{
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}
</script>
</body>
</html>
