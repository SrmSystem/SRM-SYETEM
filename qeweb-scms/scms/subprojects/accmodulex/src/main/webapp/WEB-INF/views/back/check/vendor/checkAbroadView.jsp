<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<script type="text/javascript">
	//导出
	function exportCheckItemList(checkId) {
		var url = '${ctx}/manager/check/checks/exportCheckItemList/2/true/' + checkId;
		jQuery('<form action="'+ url +'" method="post"></form>')
	    .appendTo('body').submit().remove();
	}
</script>
<div style="padding:5px;">
	<div>

	</div>
	
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-check" class="easyui-form" data-options="fit:true">
			<input id="id" name="id" value="-1" type="hidden"/>
			<table style="text-align: left;margin:auto;width:100%" >
				<tr>
					<td width="30%"><spring:message code="base.Reconciliation.VoucherNO"/><!-- 凭证号 -->：${check.qadVoucher}</td>
				</tr>
				<tr>
					<td width="30%"><spring:message code="base.Reconciliation.CheckNo"/><!-- 对账单号 -->：${check.code }</td>
					<td  width="30%"><spring:message code="base.8D.vendor"/><!-- 供应商代码 -->：${check.vendor.name }</td>
					<td  width="30%"><spring:message code="base.8D.vendorName"/><!-- 供应商名称 -->：${check.vendor.name }</td>
				</tr>
				<tr>
					<td><spring:message code="base.Reconciliation.Year"/><!-- 年 -->：${check.year }</td>
					<td><spring:message code="base.Reconciliation.Month"/><!-- 月 -->：${check.month }</td>
					<td><spring:message code="purchase.order.closeStatus"/><!-- 关闭状态 -->：<c:if test="${check.closeStatus eq 0 }"><spring:message code="status.close.0"/><!-- 未关闭 --></c:if> <c:if test="${check.closeStatus eq 1 }"><spring:message code="status.close.1"/><!-- 已关闭 --></c:if></td>
				</tr>
				<tr>
						<td ><spring:message code="base.Reconciliation.DifferencesInPrice"/><!-- 差异价格 -->：${check.col1}</td>
						<td><spring:message code="base.Reconciliation.InvoiceTotalAmount"/><!-- 发票总金额 -->：${check.billAmount}</td>
						<td><spring:message code="base.Reconciliation.ExpectedDateofpayments"/><!-- 预计付款时间 -->：<fmt:formatDate value="${check.dueDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<div  style="height: 200px">
	<a href="javascript:;" class="easyui-linkbutton" id="" data-options="iconCls:'icon-download'" onclick="exportCheckItemList(${check.id});"><spring:message code="base.Reconciliation.ExportDetail"/><!-- 导出明细 --></a>
<table id="datagrid-list1" title="<spring:message code="Evaluation.kpi.Delivery.Details"/>" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/check/checks/getItemsByCheckId/${check.id }', method:'post',singleSelect:false,
	toolbar:'#itemtoolbar',pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],fit:true,onClickCell: RowEditor.onClickCell" >   
	<thead><tr>
	<th data-options="field:'id',checkbox:true"></th>
	<th data-options="field:'check.code'"><spring:message code="base.Reconciliation.CheckNo"/><!-- 对账单号 --></th>
	<th data-options="field:'recItem.receive.createTime'"><spring:message code="base.Reconciliation.ReceiptDate"/><!-- 收货时间 --></th>
	<th data-options="field:'recItem.receive.receiveCode'"><spring:message code="purchase.receive.code"/><!-- 收货单号 --></th>
	<th data-options="field:'invoice.code'"><spring:message code="base.Reconciliation.invoicenumber"/><!-- 发票号 --></th>
	<th data-options="field:'recItem.orderItem.material.code'"><spring:message code="base.Reconciliation.MaterialCode"/><!-- 物料编码 --></th>
	<th data-options="field:'recItem.orderItem.material.name'"><spring:message code="base.Reconciliation.MaterialName"/><!-- 物料名称 --></th>
<!-- 	<th data-options="field:'recItem.orderItem.material.unit'">单位</th> -->
	<th data-options="field:'recItem.orderItem.unitName'"><spring:message code="purchase.order.unitName"/><!-- 单位 --></th>
<!-- 	<th data-options="field:'checkPrice'">核价价格</th> -->
	<th data-options="field:'recItem.attr3'"><spring:message code="base.Reconciliation.CheckPrice"/><!-- 核价价格 --></th>
<!-- 	<th data-options="field:'vendorCheckPrice',editor:{type:'numberbox',options:{required:true,precision:'2'}}">供方填写单价</th>
	<th data-options="field:'bCheckPrice'">采购员核对金额</th>
	<th data-options="field:'exStatus'">差异状态</th>
	<th data-options="field:'exDiscription'">差异说明</th>
	<th data-options="field:'exDealStatus'">差异处理状态</th>
	<th data-options="field:'exConfirmStatus'">差异确认状态</th> -->
	</tr></thead>
</table>
</div>
<div id="itemtoolbar" style="padding:5px;">
		<div>
		<c:if test="${check.bConfirmStatus eq 1 }">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.tobill(true)"><spring:message code="base.Reconciliation.Invoice"/><!-- 开票 --></a>
			<input value="${check.id}" id="hiddenCheckId" type="hidden"/>
		</c:if>
		</div>
	</div>
<div  style="height: 200px">
<%-- <table id="datagrid-list2" title="发票" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/check/checks/getInvoicesByCheckId/${check.id }', method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],fit:true," >   
	<thead><tr>
	<th data-options="field:'id',checkbox:true"></th>
	<!-- <th data-options="field:'operate',formatter:Check.modAndDel">操作</th> -->
	<th data-options="field:'code'">发票号</th>
	<th data-options="field:'billTime'">开票日期</th>
	<!-- <th data-options="field:'receiveStatus'">接收状态</th> -->
	<!-- <th data-options="field:'noTaxAmount'">税前金额</th>
	<th data-options="field:'taxRate'">税率(%)</th>
	<th data-options="field:'tax'">税金</th>
	<th data-options="field:'taxAmount'">税后金额</th>
	<th data-options="field:'billUser'">开票人</th> -->
	<th data-options="field:'invoiceFileName'">附件</th>
	</tr></thead>
</table> --%>
	<table id="datagrid-list2" title="<spring:message code="base.Reconciliation.Invoice"/>" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/check/checks/findDeliveryInvoiceInfoByCheckId/${check.id}', method:'post',singleSelect:true">   
		<thead><tr>
		<th data-options="field:'col16'"><spring:message code="base.Reconciliation.invoicenumber"/><!-- 发票号 --></th>
		<th data-options="field:'col17'"><spring:message code="base.Reconciliation.Billingdate"/><!-- 开票日期 --></th>
		<th data-options="field:'col10', formatter:function(v,r,i){return downLoadAttachment(r.col9, r.col10, i);}"><spring:message code="purchase.order.vendorUploadFileName"/><!-- 附件 --></th>
		<th data-options="field:'col18'"><spring:message code="purchase.order.invoiceAmount"/><!-- 发票金额 --></th>
		</tr></thead>
	</table>
</div>
