<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/static/script/basedata/Feedback.js"></script>
<div align="center">
	<div style="padding-top: 10px">
		<table width="90%" style="text-align: left;">
			<tr>
				<td width="30%"><b><spring:message code="purchase.order.orderCode"/>：</b>${po.order.orderCode }</td>
				<td width="30%"><b><spring:message code="purchase.order.itemNo"/>：</b>${po.itemNo }</td>
			</tr>   
			<tr>
				<td><b><spring:message code="base.vendor.code"/>：</b>${po.order.vendor.code }</td>
				<td colspan="2"><b><spring:message code="base.vendor.name"/>：</b>${po.order.vendor.name }</td>     
			</tr>
		</table>
	</div>
<hr>
	<table id="feedbackTable" width="98%" border="0" align="center" cellpadding="4" cellspacing="1" maxRows="10" autoAdjustHeight="true"
		style="width: 550px">
		<c:forEach items="${feedList }" var="feedback">
		<c:if test="${feedback.roleType == 1 }">
			<tr>
				<td style="text-align: left">&nbsp;<b> 
						${feedback.feedbackOrgName }&nbsp; ${feedback.createUserName} &nbsp;&nbsp; <fmt:formatDate value="${feedback.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></b>
				</td>
			</tr>
			<tr>
				<td style="white-space: normal;   color: blue; font-weight: 600;    text-align: left ;" nowrap     >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${feedback.feedbackContent}  
				</td>
			</tr>
			<tr>
			<td  style="white-space: normal; text-align: right" nowrap>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:;"    style="margin-right: 10% "    onclick="File.download('${feedback.feedbackFileUrl}','${feedback.feedbackFileName}')">${feedback.feedbackFileName}</a>
				</td>
			</tr>
		</c:if>
		
		<c:if test="${feedback.roleType == 0 }">
			<tr>
				<td style="text-align: left">&nbsp;<b> 
						${feedback.feedbackOrgName }&nbsp; ${feedback.createUserName} &nbsp;&nbsp; <fmt:formatDate value="${feedback.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></b>
				</td>
			</tr>
			<tr>
				<td style="white-space: normal;  text-align: left" nowrap     >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${feedback.feedbackContent}  
				</td>
			</tr>
			<tr>
			<td style="white-space: normal; text-align: right" nowrap>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="javascript:;"  style="margin-right: 10% " onclick="File.download('${feedback.feedbackFileUrl}' ,'${feedback.feedbackFileName}')">${feedback.feedbackFileName}</a>
				</td>
			</tr>
		</c:if>

			
		</c:forEach>
	</table><br>

	<form id="form-addfeedback" method="post" style="text-align: left; margin-left: 20px"
		action="${ctx }/manager/basedata/feedback/addFeedback"  enctype="multipart/form-data">
		<input id="billId" name="billId" value="${po.id }" type="hidden" />
		<input id="billType" name="billType" value="2" type="hidden" />
		<input id="vendorId" name="vendorId" value="${po.order.vendor.id }" type="hidden" />
		<input id="buyerId" name="buyerId" value="${po.order.buyer.id }" type="hidden" />
		<textarea id="feedbackContent" name="feedbackContent" cols="60" rows="3" class="textarea easyui-validatebox" data-options="required:true"></textarea><br>
		<spring:message code="purchase.order.FileUpload"/>：<input class="easyui-filebox" name="feedbackFile" data-options="buttonText:'<spring:message code='purchase.order.browse'/>'"/><br>
		<a href="javascript:;" class="easyui-linkbutton" onclick="new Feedback().feedback('form-addfeedback','feedbackTable');"><spring:message code="button.submit"/></a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-addfeedback').form('reset')"><spring:message code="button.reset"/></a>
	</form>

</div>

