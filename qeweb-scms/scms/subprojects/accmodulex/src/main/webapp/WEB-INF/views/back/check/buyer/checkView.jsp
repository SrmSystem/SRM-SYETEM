<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
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
	
	//导出
	function exportCheckItemList(checkId) {
		var url = '${ctx}/manager/check/checks/exportCheckItemList/1/false/' + checkId;
		jQuery('<form action="'+ url +'" method="post"></form>')
	    .appendTo('body').submit().remove();
	}
	
	$(function(){
		/* $("#datagrid-list1").datagrid({
			onLoadSuccess : function(data){
				//$("#datagrid-list2").datagrid({});
			}
		}); */
		$("#datagrid-list1").datagrid({});
		$("#datagrid-list2").datagrid({});
	})
</script>
<div style="padding:5px;">
	<div>
			<c:if test="${check.publishStatus eq 0 }">
				<a href="javascript:;" class="easyui-linkbutton" id="publish_btn" data-options="iconCls:'icon-ok'" onclick="Check.opt('publish',${check.id },this,['datagrid-form'])"><spring:message code="vendor.posted"/><!-- 发布 --></a>
			</c:if>
			<c:if test="${check.vConfirmStatus eq 1 and check.bConfirmStatus eq 0 }">
				<a href="javascript:;" class="easyui-linkbutton" id="b_confirm_btn" data-options="iconCls:'icon-ok'" onclick="Check.opt('bConfirm',${check.id },this,null)"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
			</c:if>
			<c:if test="${check.billStatus eq 1 and check.invoiceReceiveStatus eq 0 }">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" id="confirmInvoice_btn" onclick="Check.confirmInvoice(${check.id })"><spring:message code="vendor.check.confirmInvoice"/><!-- 确认发票 --></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" id="rejectInvoice_btn" onclick="Check.rejectInvoice(${check.id })"><spring:message code="vendor.check.rejectInvoice"/><!-- 驳回发票 --></a>
			</c:if>	
			<c:if test="${check.backStatus eq 'F' }">
				<a href="javascript:;" class="easyui-linkbutton" id="sendAgain" data-options="iconCls:'icon-ok'" onclick="ajaxSendAgain(${check.id});"><spring:message code="vendor.check.submitAgain"/><!-- 再次提交 --></a>
			</c:if>
	</div>
	
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-check" class="easyui-form" data-options="fit:true" method="post">
			<input id="id" name="id" value="-1" type="hidden"/>
			<table style="text-align: left;margin:auto;width:100%" >
				<tr>
					<td width="30%"><spring:message code="vendor.check.certificateNumber"/><!-- 凭证号 -->：${check.qadVoucher}</td>
					<td width="30%"><spring:message code="vendor.procurementOrganizationCode"/><!-- 采购组织编码 -->：${check.buyer.code}</td>
					<td width="30%"><spring:message code="vendor.namePurchasingOrganization"/><!-- 采购组织名称 -->：${check.buyer.name}</td>
				</tr>
				<tr>
					<td width="30%"><spring:message code="vendor.check.statementNo"/><!-- 对账单号 -->：${check.code }</td>
					<td  width="30%"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：${check.vendor.code }</td>
					<td  width="30%"><spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：${check.vendor.name }</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.year"/><!-- 年 -->：${check.year }</td>
					<td><spring:message code="vendor.months"/><!-- 月 -->：${check.month }</td>
<%-- 					<td>实际开票金额：${check.billAmount}</td> --%>
					<td><spring:message code="vendor.check.actualInvoiceValue"/><!-- 实际开票金额 -->：${allBillAmount}</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.supplierConfirmsPerson"/><!-- 供方确认人名称 -->：${check.vConfirmUser.name}</td>
					<td><spring:message code="vendor.check.supplierConfirmsTime"/><!-- 供方确认时间 -->：${check.vConfirmTime}</td>
					<td><spring:message code="vendor.check.supplierConfirmsStatus"/><!-- 供方确认状态 -->：<c:if test="${check.vConfirmStatus eq 0 }"><spring:message code="vendor.unconfirmed"/><!-- 未确认 --></c:if> <c:if test="${check.vConfirmStatus eq 1 }"><spring:message code="vendor.confirmed"/><!-- 已确认 --></c:if><c:if test="${check.vConfirmStatus eq 2 }"><spring:message code="vendor.partConfirmation"/><!-- 部分确认 --></c:if></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.checkNamePerson"/><!-- 采方核对人名称 -->：${check.bConfirmUser.name}</td>
					<td><spring:message code="vendor.check.checkTime"/><!-- 采方核对时间 -->：${check.bConfirmTime}</td>
					<td><spring:message code="vendor.check.sideCheckedStatus"/><!-- 采方核对状态 -->：<c:if test="${check.bConfirmStatus eq 0 }"><spring:message code="vendor.notCheck"/><!-- 未核对 --></c:if> <c:if test="${check.bConfirmStatus eq 1 }"><spring:message code="vendor.check"/><!-- 已核对 --></c:if><c:if test="${check.bConfirmStatus eq 2 }"><spring:message code="vendor.partCheck"/><!-- 部分核对 --></c:if></td>
				</tr>
				<tr>
<%-- 					<td>采购核对总金额：${check.bCheckAmount}</td> --%>
<%-- 					<td>核价总金额：${check.checkAmount}</td> --%>
					<td><spring:message code="vendor.check.checkTotalAmount"/><!-- 采购核对总金额 -->：${allBuyerCheckPrice}</td>
					<td><spring:message code="vendor.check.totalNuclearPrice"/><!-- 核价总金额 -->：${allAttr3}</td>
					<%-- <td>供应商核对总金额：${check.vCheckAmount}</td> --%>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.abnormalState"/><!-- 异常状态 -->：<c:if test="${check.exStatus eq 0 }"><spring:message code="vendor.noAbnormal"/><!-- 无异常 --></c:if> <c:if test="${check.exStatus eq 1 }"><spring:message code="vendor.abnormal"/><!-- 有异常 --></c:if></td>
					<td><spring:message code="vendor.check.exceptionHandlingState"/><!-- 异常处理状态 -->：<c:if test="${check.exDealStatus eq 0 }"><spring:message code="vendor.untreated"/><!-- 未处理 --></c:if> <c:if test="${check.exDealStatus eq 1 }"><spring:message code="vendor.processed"/><!-- 已处理 --></c:if></td>
					<td><spring:message code="vendor.check.stateInvoice"/><!-- 开票状态 -->：<c:if test="${check.billStatus eq 0 }"><spring:message code="vendor.notInvoice"/><!-- 未开票 --></c:if> <c:if test="${check.billStatus eq 1 }"><spring:message code="vendor.makeInvoice"/><!-- 已开票 --></c:if></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.closedPosition"/><!-- 关闭状态 -->：<c:if test="${check.closeStatus eq 0 }"><spring:message code="vendor.notClosed"/><!-- 未关闭 --></c:if> <c:if test="${check.closeStatus eq 1 }"><spring:message code="vendor.closed"/><!-- 已关闭 --></c:if></td>	
					<td><spring:message code="vendor.check.deductionsInstructions"/><!-- 扣减说明 -->：${check.claimDescription}
					<c:if test="${check.publishStatus eq 0 }">
						<input type="text" class="easyui-textbox" id="claimDescription" name="claimDescription"  value="">
					</c:if>
					</td>
					<td><spring:message code="vendor.check.qualityClaimdeduction"/><!-- 质量索赔扣减金额 -->：${check.claimAmount}
						<c:choose>
							<c:when test="${check.publishStatus eq 0 }">
								<input type="text" class="easyui-numberbox" id="claimAmount" name="claimAmount" precision="2" value="">
							</c:when>
							<c:otherwise>
								<div style="display: none;"> 
									<input type="hidden" class="easyui-numberbox" id="claimAmount" name="claimAmount" value="${check.claimAmount}">
								</div>
							</c:otherwise> 
						</c:choose>
					</td>
				</tr>	
				<c:if test="${check.billStatus eq 1 and check.invoiceReceiveStatus eq 0 }">								
				<tr>
					<td colspan="3"><spring:message code="vendor.check.reasonDismissalRejected"/><!-- 开票驳回原因 -->：<textarea id="billRejectReason" name="billRejectReason" cols="60" rows="3" class="easyui-validatebox"></textarea></td>
				</tr>
				</c:if>					

				<%-- <tr>
					<c:if test="${check.publishStatus eq 0 }">
						<td >索赔金额：<input type="text" class="easyui-numberbox" precision="2" data-options="required:true,events:{blur: function(){Check.editClaimAmount(${check.id },this,${check.publishStatus })}}" value="${check.claimAmount }"/></td>
						<td >索赔描述：<input type="text" class="easyui-textbox" value="${check.claimDescription }" data-options="events:{blur: function(){Check.editClaimDescription(${check.id },this,${check.publishStatus })}}"/></td>
						<td ></td>
					</c:if>
					<c:if test="${check.publishStatus eq 1 }">
						<td >索赔金额：${check.claimAmount }</td>
						<td >索赔描述：${check.claimDescription }</td>
						<td ></td>
					</c:if>
					<td>价差总金额：${check.}</td>
				</tr> --%>
			</table>
		</form>
	</div>
</div>
<div style="height: 200px">
	<a href="javascript:;" class="easyui-linkbutton" id="" data-options="iconCls:'icon-download'" onclick="exportCheckItemList(${check.id});"><spring:message code="vendor.exportSubsidiary"/><!-- 导出明细 --></a>
	<table id="datagrid-list1" title="对账单明细" 
		data-options="url:'${ctx}/manager/check/checks/getItemsByCheckId/${check.id }', method:'post',singleSelect:false,
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],fit:true">   
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'itemNo'"><spring:message code="vendor.check.lineNumbers"/><!-- 行号 --></th>
		<th data-options="field:'orderCode'"><spring:message code="vendor.orderNumber"/><!-- 订单号 --></th>
		<th data-options="field:'receiveTime'"><spring:message code="vendor.check.dateReceipt"/><!-- 收货日期 --></th>
		<th data-options="field:'receiveCode'"><spring:message code="vendor.check.receiptNo"/><!-- 收货单号 --></th>
		<th data-options="field:'retrunCode'"><spring:message code="vendor.check.returnsNumber"/><!-- 退货单号 --></th>
		<th data-options="field:'retrunTime'"><spring:message code="vendor.check.returnTime"/><!-- 退货时间 --></th>
		<th data-options="field:'invoice.code'"><spring:message code="vendor.check.correspondingInvoiceNumber"/><!-- 对应发票号 --></th>
		<th data-options="field:'materialCode'"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
		<th data-options="field:'materialName'"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
		<th data-options="field:'receiveQty'"><spring:message code="vendor.check.quantity"/><!-- 数量 --></th>
		<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
		<th data-options="field:'vendorCheckPrice'"><spring:message code="vendor.check.supplierUnitPrice"/><!-- 供方填写单价 --></th>
		<th data-options="field:'buyerCheckPrice'"><spring:message code="vendor.check.buyerChecksAmount"/><!-- 采购员核对金额 --></th>
		<th data-options="field:'checkPrice'"><spring:message code="vendor.check.nuclearPriceprice"/><!-- 核价价格 --></th>
		<th data-options="field:'deliveryCode'"><spring:message code="vendor.check.ASNInvoice"/><!-- ASN发货单号 --></th>
		<th data-options="field:'col1'"><spring:message code="vendor.check.tax"/><!-- 税金 --></th>
		<th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'exception',true);}"><spring:message code="vendor.check.differencesState"/><!-- 差异状态 --></th>
		<th data-options="field:'exDiscription', formatter:function(v,r,i){return checkNull(r.exDiscription);}"><spring:message code="vendor.check.differencesThat"/><!-- 差异说明 --></th>
		<th data-options="field:'exDealStatus',formatter:function(v,r,i){return StatusRender.render(v,'deal',true);}"><spring:message code="vendor.check.differentialTreatmentStatus"/><!-- 差异处理状态 --></th>
		<th data-options="field:'exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.differentialConfirmation"/><!-- 差异确认状态 --></th>
		</tr></thead>
	</table>
</div>

<div  style="height: 200px">
<table id="datagrid-list2" title="发票" 
	data-options="url:'${ctx}/manager/check/checks/getInvoicesByCheckId/${check.id }', method:'post',singleSelect:false,
	toolbar:'#billtoolbar',pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],fit:true" >   
	<thead><tr>
	<th data-options="field:'id',checkbox:true"></th>
	<th data-options="field:'code'"><spring:message code="vendor.check.invoiceNo."/><!-- 发票号 --></th>
	<th data-options="field:'billTime'"><spring:message code="vendor.check.invoiceDate"/><!-- 开票日期 --></th>
	<th data-options="field:'noTaxAmount'"><spring:message code="vendor.check.pretaxAmount"/><!-- 税前金额 --></th>
	<th data-options="field:'taxRate'"><spring:message code="vendor.check.rate"/><!-- 税率 -->(%)</th>
	<th data-options="field:'tax'"><spring:message code="vendor.check.tax"/><!-- 税金 --></th>
	<th data-options="field:'taxAmount'"><spring:message code="vendor.check.pretaxAmount"/><!-- 含税金额 --></th>
	<th data-options="field:'col1'"><spring:message code="vendor.check.drawer"/><!-- 开票人 --></th>
	<th data-options="field:'invoiceFileName',formatter:function(v,r,i){return downLoadAttachment(r.invoiceFilePath, r.invoiceFileName, i);}"><spring:message code="vendor.check.attachment"/><!-- 附件 --></th>
	</tr></thead>
</table>
</div>
<div id="billtoolbar" style="padding:5px;">
	<div>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.confirmInvoice()">确认</a> -->
	</div>
</div>
