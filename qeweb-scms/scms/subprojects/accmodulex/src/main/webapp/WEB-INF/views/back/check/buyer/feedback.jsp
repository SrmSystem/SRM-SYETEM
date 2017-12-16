<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/static/script/check/check.js"></script>
<div align="center">
	<div style="padding-top: 10px;"align="center">
		<table width="90%" style="text-align: left;">
			<tr><td><b><spring:message code="vendor.check.statementNo"/><!-- 对账单号 -->：</b>${check.code }</td></tr>   
			<tr><td><b><spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：</b>${check.vendor.code }</td></tr>	
			<tr><td><b><spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：</b>${check.vendor.name }</td></tr>
		</table>
	</div>
<hr>
	<table id="feedbackTable" width="98%" border="0" align="center" cellpadding="4" cellspacing="1" maxRows="10" autoAdjustHeight="true"
		style="width: 550px">
		<c:forEach items="${feedList }" var="feedback">
			<tr>
				<td style="text-align: left">&nbsp;<b> 
						${feedback.feedbackOrgName }&nbsp; ${feedback.createUserName} &nbsp;&nbsp; <fmt:formatDate value="${feedback.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></b>
				</td>
			</tr>
			<tr>
				<td style="white-space: normal; text-align: left" nowrap>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${feedback.feedbackContent}</td>
			</tr>
		</c:forEach>
	</table><br>

	<form id="form-addfeedback" method="post" style="text-align: left; margin-left: 20px"
		action="${ctx }/manager/basedata/feedback/addFeedback" >
		<input id="billId" name="billId" value="${check.id }" type="hidden" />
		<input id="billType" name="billType" value="4" type="hidden" />
		<input id="vendorId" name="vendorId" value="${check.vendor.id }" type="hidden" />
		<input id="buyerId" name="buyerId" value="${check.buyer.id }" type="hidden" />
		<textarea id="feedbackContent" name="feedbackContent" cols="60" rows="3" class="textarea easyui-validatebox" data-options="required:true"></textarea>
		<a href="javascript:;" class="easyui-linkbutton" onclick="Check.feedback('form-addfeedback','feedbackTable');"><spring:message code="button.submit"/></a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="javascript:$('#feedbackContent').val('');"><spring:message code="button.reset"/></a>
	</form>

</div>

