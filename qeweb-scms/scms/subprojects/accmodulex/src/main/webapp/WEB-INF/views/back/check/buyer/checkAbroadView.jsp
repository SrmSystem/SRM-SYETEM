<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	//导出
	function exportCheckItemList(checkId) {
		var url = '${ctx}/manager/check/checks/exportCheckItemList/2/false/' + checkId;
		jQuery('<form action="'+ url +'" method="post"></form>')
	    .appendTo('body').submit().remove();
	}
</script>
<div style="padding:5px;">
	<div>
	<c:if test="${check.exStatus eq 1 && check.exDealStatus eq 0}">
		<a href="javascript:;" class="easyui-linkbutton" id="publish_btn" data-options="iconCls:'icon-ok'" onclick="Check.dealEx(${check.id })"><spring:message code="vendor.check.differentialHandlingSubmission"/><!-- 差异处理提交 --></a>
		</c:if>
	</div>
	
	<c:if test="${check.backStatus eq 'F' }">
		<a href="javascript:;" class="easyui-linkbutton" id="sendAgain" data-options="iconCls:'icon-ok'" onclick="ajaxSendAgain(${check.id});"><spring:message code="vendor.check.submitAgain"/><!-- 再次提交 --></a>
	</c:if>
	
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-check" class="easyui-form" method="post" data-options="fit:true"> 
			<input id="id" name="id" value="${check.id }" type="hidden"/>
			<table style="text-align: left;margin:auto;width:100%" >
				<tr>
					<td width="30%"><spring:message code="vendor.check.certificateNumber"/><!-- 凭证号 -->：${check.qadVoucher}</td>
				</tr>
				<tr>
					<td width="30%"><spring:message code="vendor.check.statementNo"/><!-- 对账单号 -->：${check.code }</td>
					<td  width="30%"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：${check.vendor.code }</td>
					<td  width="30%"><spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：${check.vendor.name }</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.year"/><!-- 年 -->：${check.year }</td>
					<td><spring:message code="vendor.months"/><!-- 月 -->：${check.month }</td>
					<td><spring:message code="vendor.check.closedPosition"/><!-- 关闭状态 -->：<c:if test="${check.closeStatus eq 0 }"><spring:message code="vendor.notClosed"/><!-- 未关闭 --></c:if> <c:if test="${check.closeStatus eq 1 }"><spring:message code="vendor.closed"/><!-- 已关闭 --></c:if></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.abnormalState"/><!-- 异常状态 -->：<c:if test="${check.exStatus eq 0 }"><spring:message code="vendor.noAbnormal"/><!-- 无异常 --></c:if> <c:if test="${check.exStatus eq 1 }"><spring:message code="vendor.abnormal"/><!-- 有异常 --></c:if></td>
					<td><spring:message code="vendor.check.exceptionHandlingState"/><!-- 异常处理状态 -->：<c:if test="${check.exDealStatus eq 0 }"><spring:message code="vendor.untreated"/><!-- 未处理 --></c:if> <c:if test="${check.exDealStatus eq 1 }"><spring:message code="vendor.processed"/><!-- 已处理 --></c:if></td>
<%-- 					<td>核价总金额：${check.checkAmount}</td> --%>
					<td><spring:message code="vendor.check.totalNuclearPrice"/><!-- 核价总金额 -->：${allCheckPrice}</td>
				</tr>
				<tr>
				<c:choose>
					<c:when test="${check.exStatus eq 1 && check.exDealStatus eq 0}">
						<td><spring:message code="vendor.check.differencePrice"/><!-- 差异价格 -->：<input id="col1" name="col1" class="easyui-numberbox" precision="5" data-options="required:true" value="${check.col1 }"/></td>
					</c:when>
					<c:otherwise>
						<td><spring:message code="vendor.check.differencePrice"/><!-- 差异价格 -->：${check.col1}</td>
					</c:otherwise>
				</c:choose>
<%-- 					<td>发票总金额：${check.billAmount}</td> --%>
					<td><spring:message code="vendor.check.totalInvoiceValue"/><!-- 发票总金额 -->：${check.billAmount}</td>
					<td><spring:message code="vendor.check.estimatedTimePayment"/><!-- 预计付款时间 -->：<fmt:formatDate value="${check.dueDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				
				<%--
				<tr>
					<td>供方确认人名：${check.vConfirmUser.name}</td>
					<td>采方核对员：${check.bConfirmUser.name}</td>
					<td>扣减说明：${check.claimDescription}</td>
					<td>质量索赔扣减金额：${check.claimAmount}</td>
					<td>开票驳回原因：${check.billRejectReason}</td>
				</tr>
				<tr>
					<td>供方确认时间：${check.vConfirmTime}</td>
					<td>采方核对时间：${check.bConfirmTime}</td>
				</tr>
				<tr>
					<td>供应商核对总金额：${check.vCheckAmount }</td>
					<td>供方确认状态：<c:if test="${check.vConfirmStatus eq 0 }">未确认</c:if> <c:if test="${check.vConfirmStatus eq 1 }">已确认</c:if><c:if test="${check.vConfirmStatus eq 2 }">部分确认</c:if></td>
				</tr>
				<tr>
					<td>采购核对状态：<c:if test="${check.bConfirmStatus eq 0 }">未核对</c:if> <c:if test="${check.bConfirmStatus eq 1 }">已核对</c:if><c:if test="${check.bConfirmStatus eq 2 }">部分核对</c:if></td>
					<td>开票状态：<c:if test="${check.billStatus eq 0 }">未开票</c:if> <c:if test="${check.billStatus eq 1 }">已开票</c:if></td>
				</tr>
				<tr>
					<td>采购核对总金额：${check.bCheckAmount }</td>
				</tr>
				 --%>
			</table>
		</form>
	</div>
</div>
<div  style="height: 200px">
	<a href="javascript:;" class="easyui-linkbutton" id="" data-options="iconCls:'icon-download'" onclick="exportCheckItemList(${check.id});"><spring:message code="vendor.exportSubsidiary"/><!-- 导出明细 --></a>
	
<table id="datagrid-list1" title="对账单明细" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/check/checks/getItemsByCheckId/${check.id }', method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],fit:true,onClickCell: CellEditor.onClickCell">   
	<thead><tr>
	<th data-options="field:'id',checkbox:true"></th>
	<!-- <th data-options="field:'check.code'">对账单</th> -->
	<!-- <th data-options="field:'recItem.receive.createTime'">收货时间</th>
	<th data-options="field:'recItem.attr9'">QAD收货单号</th>
	<th data-options="field:'invoice.code'">发票号</th>
	<th data-options="field:'recItem.orderItem.material.code'">零件号</th>
	<th data-options="field:'recItem.orderItem.material.name'">零件描述</th>
	<th data-options="field:'recItem.receiveQty'">数量</th>
	<th data-options="field:'recItem.orderItem.unitName'">单位</th>
	<th data-options="field:'checkPrice'">核价单价</th>
	<th data-options="field:'exPrice',editor:{type:'numberbox',options:{required:true,precision:'2'}}">差异价格</th>
	<th data-options="field:'recItem.deliveryItem.delivery.deliveryCode'">ASN号</th>
	<th data-options="field:'recItem.deliveryItem.col7'">供应商批次号</th>
	<th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'differenceStatus',true);}">差异状态</th>
	<th data-options="field:'exDiscription',editor:{type:'textbox'}">差异说明</th>
	<th data-options="field:'exDealStatus'">差异处理状态</th> -->
	
	<th data-options="field:'receiveTime'"><spring:message code="vendor.check.goodsTime"/><!-- 收货时间 --></th>
	<th data-options="field:'qadCode'"><spring:message code="vendor.check.QADNo"/><!-- QAD收货单号 --></th>
	<th data-options="field:'invoice.code',formatter:function(v,r,i){return r.invoice.code}"><spring:message code="vendor.check.invoiceNo."/><!-- 发票号 --></th>
	<th data-options="field:'materialCode'"><spring:message code="vendor.check.partNumber"/><!-- 零件号 --></th>
	<th data-options="field:'materialName'"><spring:message code="vendor.check.partDescription"/><!-- 零件描述 --></th>
	<th data-options="field:'receiveQty'"><spring:message code="vendor.check.quantity"/><!-- 数量 --></th>
	<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
	<!-- <th data-options="field:'checkPrice'">核价单价</th> -->
	<th data-options="field:'recItem.attr3'"><spring:message code="vendor.check.nuclearPrice"/><!-- 核价单价 --></th>
	<!-- 
	<th data-options="field:'exPrice',editor:{type:'numberbox',options:{required:true,precision:'2'}}">差异价格</th>
	 -->
	<th data-options="field:'deliveryCode'"><spring:message code="vendor.check.ASNInvoice"/>ASN发货单号</th>
	<th data-options="field:'vendorBatchNum'"><spring:message code="vendor.check.supplierBatchNumber"/><!-- 供应商批次号 --></th>
	<!-- <th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'differenceStatus',true);}">差异状态</th> -->
	<!-- <th data-options="field:'exDiscription',editor:{type:'textbox'}">差异说明</th> -->
	<!-- <th data-options="field:'exDealStatus'">差异处理状态</th> -->
	
	<%--
	<th data-options="field:'vendorCheckPrice'">供方填写单价</th>
	<th data-options="field:'buyerCheckPrice'">采购员核对金额</th>
	<th data-options="field:'exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}">差异确认状态</th>
	 --%>
	</tr></thead>
</table>
</div>
<div  style="height: 200px">
<%-- <table id="datagrid-list2" title="发票" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/check/checks/getInvoicesByCheckId/${check.id }', method:'post',singleSelect:false,
	toolbar:'#billtoolbar',pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],fit:true" >   
	<thead><tr>
	<th data-options="field:'id',checkbox:true"></th>
	<th data-options="field:'code'">发票号</th>
	<!-- <th data-options="field:'receiveStatus'">接收状态</th> -->
	<th data-options="field:'billTime'">开票日期</th>
	<!-- <th data-options="field:'noTaxAmount'">税前金额</th>
	<th data-options="field:'taxRate'">税率(%)</th>
	<th data-options="field:'tax'">税金</th>
	<th data-options="field:'taxAmount'">税后金额</th>
	<th data-options="field:'billUser'">开票人</th> -->
	<th data-options="field:'invoiceFileName'">附件</th>
	<!-- <th data-options="field:'col1'">开票人</th>
	<th data-options="field:'invoiceFileName',formatter:function(v,r,i){return downLoadAttachment(v,r,i);}">附件</th> -->
	</tr></thead>
</table> --%>
	<table id="datagrid-list2" title="发票" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/check/checks/findDeliveryInvoiceInfoByCheckId/${check.id}', method:'post',singleSelect:true">   
		<thead><tr>
		<th data-options="field:'col16'"><spring:message code="vendor.check.invoiceNo."/><!-- 发票号 --></th>
		<th data-options="field:'col17'"><spring:message code="vendor.check.invoiceDate"/><!-- 开票日期 --></th>
		<th data-options="field:'col10', formatter:function(v,r,i){return downLoadAttachment(r.col9, r.col10, i);}"><spring:message code="vendor.check.attachment"/><!-- 附件 --></th>
		<th data-options="field:'col18'"><spring:message code="vendor.check.invoiceAmount"/><!-- 发票金额 --></th>
		</tr></thead>
	</table>
</div>
<div id="billtoolbar" style="padding:5px;">
	<div>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.confirmInvoice()">确认</a> -->
	</div>
</div>
