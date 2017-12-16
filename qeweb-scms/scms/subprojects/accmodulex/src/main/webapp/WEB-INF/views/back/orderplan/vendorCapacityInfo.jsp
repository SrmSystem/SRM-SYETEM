<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- 产能表的提交 -->
<div id="win-vendorCapacity-addoredit" >
		<div data-options="fit:true">
			<form id="form-vendorCapacity-addoredit" method="post" >
				<table style="text-align: right;padding:5px;margin:auto;margin-top:20px" cellpadding="5"  id="myTable_id">
				<tr>
					<input id="poPlanid" name="poPlanid" type="hidden" value="${poPlanid}"/>
					<input id="vendorPlanid" name="vendorPlanid" type="hidden" value="${vendorPlanid}"/>
				</tr>
				 <c:forEach items="${cvList}" var="cv"  varStatus="status"  >
					 <tr>
						 <td>${cv.name} :<span style='display:block;float:right;padding:1px;'><input type="text" name="${cv.code}"  ${cv.checked} class="easyui-textbox" > </span></td>
					 </tr>
				</c:forEach>
				</table>
					<div style="text-align: center;padding:5px;">
						<a href="javascript:;" class="easyui-linkbutton" onclick="submitCapacity()" ><spring:message code="vendor.orderplan.submit"/></a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorCapacity-addoredit').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
					</div>
			</form>
		</div>
</div>

<script type="text/javascript">
function submitCapacity(){
	$.messager.progress({
		title:'<spring:message code="vendor.orderplan.prompting"/>',
		msg : '<spring:message code="vendor.orderplan.submission"/>...'
	});
	$('#form-vendorCapacity-addoredit').form('submit',{
		   url:'${ctx}/manager/order/purchaseplanvendor/addShowCapacityInfo',
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
		   success:function(data){
			   $('#datagrid-purchaseplan-list').datagrid('reload');
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.uploadedSuccessfully"/>！','info');
				$.messager.progress('close');
				$dialog.dialog('close');
			}
	});
}

</script>

