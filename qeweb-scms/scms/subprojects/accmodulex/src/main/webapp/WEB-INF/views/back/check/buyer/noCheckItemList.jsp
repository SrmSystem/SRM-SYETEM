<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.check.notlistAccounts"/><!-- 未对账明细列表 --></title>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			var t = new Date(v);
			return t.toLocaleString();
		}
		
		
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-list" title="未对账明细列表" class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/check/nocheckeditems/getList',method:'post',singleSelect:false,
		toolbar:'#noCheckItemsListToolbar',multiSort:true,
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'recCode'"><spring:message code="vendor.check.receiptNo"/><!-- 收货单号 --></th>
		<th data-options="field:'dlvCode'"><spring:message code="vendor.check.ASNInvoice"/><!-- ASN发货单号 --></th>
		<th data-options="field:'returnCode'"><spring:message code="vendor.check.returnsNumber"/><!-- 退货单号 --></th>
		<th data-options="field:'returnTime'"><spring:message code="vendor.check.returnTime"/><!-- 退货时间 --></th>
		<th data-options="field:'recQty'"><spring:message code="vendor.check.goodsQuantity"/><!-- 收货数量 --></th>
		<th data-options="field:'orderCode'"><spring:message code="vendor.orderNumber"/><!-- 订单号 --></th>
		<th data-options="field:'itemNo'"><spring:message code="vendor.check.orderLine"/><!-- 订单行 --></th>
		<th data-options="field:'buyerCode'"><spring:message code="vendor.procurementOrganizationCode"/><!-- 采购组织编码 --></th>
		<th data-options="field:'buyerName'"><spring:message code="vendor.namePurchasingOrganization"/><!-- 采购组织名称 --></th>
		<th data-options="field:'vendorCode'"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'vendorName'"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<th data-options="field:'materialCode'"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
		<th data-options="field:'materialName'"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
		<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.creationTime"/><!-- 创建时间 --></th>
		</tr></thead>
	</table>
	<div id="noCheckItemsListToolbar" style="padding:5px;">
		
		<div>
			<form id="form-search" method="post">
			<spring:message code="vendor.orderNumber"/><!-- 订单号 -->：<input type="text" name="search-LIKE_orderCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.check.receiptNo"/><!-- 收货单号 -->：<input type="text" name="search-LIKE_recCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：<input type="text" name="search-LIKE_vendorCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：<input type="text" name="search-LIKE_vendorName" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.check.ASNInvoice"/><!-- ASN发货单号 -->：<input type="text" name="search-LIKE_dlvCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.materialCode"/><!-- 物料编码 -->：<input type="text" name="search-LIKE_materialCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.creationTime"/><!-- 创建时间 -->：<input type="text" id="startTime" name="search-GTE_createTime" class="easyui-datebox" data-options="editable:false" style="width:100px;"/>
		- <input type="text" id="endTime" name="search-LTE_createTime" class="easyui-datebox" data-options="editable:false" style="width:100px;"/>
		<spring:message code="vendor.check.statementStatus"/><!-- 对账单状态 -->：<select class="easyui-combobox" name="search-EQ_type"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.normal"/><!-- 正常 --></option><option value="1"><spring:message code="vendor.getRid"/><!-- 去除 --></option></select>
			
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
					<tr>
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'"  onclick="createCheckOpt()"><spring:message code="vendor.check.generateStatements"/><!-- 生成对账单 --></a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete'"  onclick="deleteCheckOpt()"><spring:message code="vendor.check.removeLedgerDetails"/><!-- 去除对账明细 --></a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'"  onclick="sycNocheckeditems()"><spring:message code="vendor.synchronizing"/><!-- 同步 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>

<script type="text/javascript">

function doSearch(){
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-list').datagrid('load',searchParams);
}

/**
 * 选择数据生成对账单
 */
function createCheckOpt(){
	var startTime = $("#startTime").datetimebox("getValue");
	var endTime = $("#endTime").datetimebox("getValue");
	if(startTime==null || startTime==''){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.check.startCreationTime"/>'/* 请填写创建时间的开始时间 */,'info');
		return false;
	}else if(endTime==null || endTime==''){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.check.endCreationTime"/>'/* 请填写创建时间的结束时间 */,'info');
		return false;
	}
	$.messager.progress();
	$.ajax({
		url:'${ctx}/manager/check/nocheckeditems/createCheck',
		type:'POST',
		data:JSON.stringify({"GTE_createTime":startTime,"LTE_createTime":endTime}),
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 

					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.message,'info');
					
					$('#datagrid-list').datagrid('reload'); 
				}else{
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}

function sycNocheckeditems(){
	$.messager.progress();
	$.ajax({
		url:'${ctx}/manager/check/nocheckeditems/sycNocheckeditems',
		type:'POST',
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code=""/>',/* 消息 */
						msg:  data.message, 
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
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}

function deleteCheckOpt(){
	
	var selections = $("#datagrid-list").datagrid('getSelections');
	
	var ids="";
	$.each(selections,function (i,n){
		if(i==0){
			ids=n.id
		}else{
			ids+=','+n.id
		}
	});
	if(ids!=""){
		$.ajax({
			url:'${ctx}/manager/check/nocheckeditems/deleteCheckOpt',
			type:'POST',
			data:JSON.stringify({"ids":ids}),
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				try{
					if(data.code){ 
						$.messager.show({
							title:'<spring:message code=""/>',/* 消息 */
							msg:  data.msg, 
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
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.msg,'error');
					}
				}catch (e) {
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
				} 
			}
		});
	}else{
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.check.noUnaccountedInformation"/>'/* 没有未对账信息 */,'error'); 
	}
	
}
</script>
</body>
</html>
