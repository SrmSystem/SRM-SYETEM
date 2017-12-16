<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="padding:5px;">
	<div>
			<%-- <c:if test="${check.vConfirmStatus eq 0 }">
				<a href="javascript:;" class="easyui-linkbutton" id="vconfirm_btn" data-options="iconCls:'icon-ok'" onclick="Check.vConfirm(${check.id },this)">确认</a>
			</c:if>
			<c:if test="${check.bConfirmStatus eq 1 and check.billStatus eq 0 }">
				
			</c:if> --%>
			
	</div>
	
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-invoice" class="easyui-form" data-options="fit:true" method="post">
			<input id="id" name="id" value="-1" type="hidden"/>
			<table style="text-align: left;margin:auto;width:100%" >
				<tr>
					<td ><spring:message code="vendor.check.invoiceNo."/><!-- 发票号 -->：<input type="text" id="invoiceCode" name="code" class="easyui-textbox" value="${invoice.code }"/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.invoiceDate"/><!-- 开票日期 -->：<input type="text" id="invoiceDate" name="billTime" class="easyui-datebox" value="${invoice.billTime }"/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.check.pretaxAmount"/><!-- 税前金额 -->：<input type="text"  id="invoiceMoney" name="noTaxAmount" class="easyui-textbox" value="${invoice.noTaxAmount }"/></td>
				</tr>
			</table>
			<a href="javascript:;" class="easyui-linkbutton" id="modInvoice_btn" data-options="iconCls:'icon-ok'" onclick="Check.saveInvoice(${invoice.id },this)"><spring:message code=""/><!-- 保存 --></a>
		</form>
	</div>
</div>

</div>
