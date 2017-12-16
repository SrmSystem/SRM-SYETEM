<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- 查看单个产能的详细信息 -->
<div id="win-vendorCapacity-view" >
		<div data-options="fit:true">
				<table style="text-align: right;padding:5px;margin:auto;margin-top:20px" cellpadding="5"  id="myTable_id">
				<tr>
				</tr>
				 <c:forEach items="${viewData}" var="data"  varStatus="status"  >
					 <tr>				

					     <td style='padding:3px ;' ><label class="common-label" >${data.code}：</label></td>
					     <td style='padding:3px;' ><label class="common-label"   >${data.name}</label></td>
					 
					 </tr>
				</c:forEach>
				
				</table>
		</div>
</div>

