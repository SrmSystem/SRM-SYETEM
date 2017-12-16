<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div  style="height: 400px">
<table id="datagrid-list4" title="对账单明细" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/check/checks/getItemsByPks/${bill_checkItem_pks}', method:'post',singleSelect:false,
	toolbar:'#itemtoolbar2',pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],fit:true,onClickCell: RowEditor.onClickCell" >   
	<thead><tr>
	<th data-options="field:'check.code'"><spring:message code="vendor.check.claimAmount"/><!-- 对账单 --></th>
	<th data-options="field:'recItem.receive.createTime'"><spring:message code="vendor.check.goodsTime"/><!-- 收货时间 --></th>
	<th data-options="field:'recItem.receive.receiveCode'"><spring:message code="vendor.check.receiptNo"/><!-- 收货单号 --></th>
	<th data-options="field:'invoice.code'"><spring:message code="vendor.check.invoiceNo."/><!-- 发票号 --></th>
	<th data-options="field:'recItem.orderItem.material.code'"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
	<th data-options="field:'recItem.orderItem.material.name'"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
	<th data-options="field:'recItem.orderItem.unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
	<th data-options="field:'recItem.receiveQty'"><spring:message code="vendor.check.quantity"/><!-- 数量 --></th>
	<th data-options="field:'vendorCheckPrice'"><spring:message code="vendor.check.supplierUnitPrice"/><!-- 供方填写单价 --></th>
	<th data-options="field:'buyerCheckPrice'"><spring:message code="vendor.check.buyerChecksAmount"/><!-- 采购员核对金额 --></th>
	<th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'exception',true);}"><spring:message code="vendor.check.differencesState"/><!-- 差异状态 --></th>
	<th data-options="field:'exDiscription'"><spring:message code="vendor.check.differencesThat"/><!-- 差异说明 --></th>
	<th data-options="field:'exDealStatus',formatter:function(v,r,i){return StatusRender.render(v,'deal',true);}"><spring:message code="vendor.check.differentialTreatmentStatus"/><!-- 差异处理状态 --></th>
	<th data-options="field:'exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.differentialConfirmation"/><!-- 差异确认状态 --></th>
	</tr></thead>
</table>
</div>
<div id="itemtoolbar2" style="padding:5px;">
	<form id="form-bill" method="post" enctype="multipart/form-data">
		<div>
			<input name="checkItemIds" type="hidden" value="${bill_checkItem_pks}">
			<spring:message code="vendor.check.invoiceNo."/><!-- 发票号 -->：<input type="text" name="code" class="easyui-textbox" style="width:80px;" data-options="required:true" value="${invoice.code}"/>
			<spring:message code="vendor.check.pretaxAmount"/><!-- 税前金额 -->：<input type="text" name="noTaxAmount" class="easyui-textbox" style="width:80px;" data-options="required:true" value="${invoice.noTaxAmount}" readonly="readonly"/>
			<spring:message code="vendor.check.invoiceDate"/><!-- 开票日期 -->：<input type="text" name="billTime" class="easyui-datetimebox" style="width:100px;" data-options="required:true,editable:false" value="${invoice.billTime}"/>
			<spring:message code="vendor.check.drawer"/><!-- 开票人 -->：<input type="text" name="col1" class="easyui-textbox" style="width:80px;" data-options="required:true" value="${invoice.col1}"/>
			<spring:message code="vendor.check.rate"/><!-- 税率 -->：<input type="text" name="taxRate1" class="easyui-textbox" readonly="readonly" value="17%" style="width:80px;"/>
			<spring:message code="vendor.check.invoiceType"/><!-- 发票类型 -->：<input type="text" name="invoiceType" class="easyui-textbox" readonly="readonly" value="增值专用发票" style="width:80px;"/><br>
			<spring:message code="vendor.check.attachment"/><!-- 附件 -->：<input class="easyui-filebox" name="file" data-options="prompt:'选择...'" style="width:200" value="" />
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.saveInvoice(${invoice.id}, this);"><spring:message code="vendor.preservation"/><!-- 保存 --></a>
		</div>
	</form>
</div>
