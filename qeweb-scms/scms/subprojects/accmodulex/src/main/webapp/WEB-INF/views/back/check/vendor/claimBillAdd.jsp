<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div id="itemtoolbar2" style="padding:5px;">
	<form id="form-bill" method="post" enctype="multipart/form-data">
		<div>
			<input name="check.id" type="hidden" value="${check.id}">
			<spring:message code="vendor.check.invoiceNo."/><!-- 发票号 -->：<input type="text" name="code" class="easyui-textbox" style="width:80px;" data-options="required:true"/>
			<spring:message code="vendor.check.pretaxAmount"/><!-- 税前金额 -->：<input type="text" name="noTaxAmount" class="easyui-textbox" style="width:80px;" data-options="required:true" value="${check.claimAmount}" readonly="readonly"/>
			<spring:message code="vendor.check.invoiceDate"/><!-- 开票日期 -->：<input type="text" name="billTime" class="easyui-datetimebox" style="width:80px;" data-options="required:true,editable:false"/>
			<spring:message code="vendor.check.drawer"/><!-- 开票人 -->：<input type="text" name="col1" class="easyui-textbox" style="width:80px;" data-options="required:true"/>
			<spring:message code="vendor.check.rate"/><!-- 税率 -->：<input type="text" name="taxRate1" class="easyui-textbox" readonly="readonly" value="17%" style="width:80px;"/>
			<spring:message code="vendor.check.invoiceType"/><!-- 发票类型 -->：<input type="text" name="invoiceType" class="easyui-textbox" readonly="readonly" value="增值专用发票" style="width:80px;"/><br>
			<spring:message code="vendor.check.attachment"/><!-- 附件 -->：<input class="easyui-filebox" name="file" data-options="prompt:'选择...'" style="width:200">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.addClaimBill();"><spring:message code="vendor.check.makeoutInvoic"/><!-- 开票 --></a>
		</div>
	</form>
</div>
