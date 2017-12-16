<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="padding:5px;">
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-fileCollaboation-search">
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td width="30%"><spring:message code="vendor.file.collaborativeName"/>  :${FileCollaboration.title }</td>
					<td width="30%"><spring:message code="vendor.file.collaborativeType"/>  :${FileCollaboration.collaborationType.name }</td>
					<td width="30%"><spring:message code="vendor.releaseTime"/> :${FileCollaboration.publishTime }</td>
				</tr>
			</table>
		</form>
	</div>

</div>


<!-- 文件协同的明细 -->
<div style="height: 400px">
	<table id="datagrid-feedback-list" class="easyui-datagrid"  fit="true" title="回传文件" 
		data-options="url:' ${ctx}/manager/file/fileCollaboation/getFileItemList/${FileCollaboration.id}',method:'post',singleSelect:false,   
		toolbar:'#feedbackListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="vendor.supplierCode"/></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;} "  ><spring:message code="vendor.supplierName"/></th>
		<th data-options="field:'backStatus',formatter:function(v,r,i){return StatusRender.render(v,'backStatus',false);}"><spring:message code="vendor.file.returnsStatus"/></th> 
		<th data-options="field:'backTime'"   ><spring:message code="vendor.file.backTime"/></th>
		 <th data-options="field:'feedbackFile' ,formatter:Feedback.viewFmt" ><spring:message code="vendor.file.backFile"/></th> 
		</tr></thead>
	</table>
	<div id="feedbackListToolbar" style="padding:5px;">
		<div>
			<form id="form-feedback-search" method="post">
			<spring:message code="vendor.supplierName"/>	：<input type="text" name="query-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.file.backTime"/>： <input type="text" name="query-GT_backTime" data-options="editable:false" class="easyui-datebox" ><spring:message code="vendor.to"/>
					        <input type="text" name="query-LT_backTime" data-options="editable:false" class="easyui-datebox" >
		    <spring:message code="vendor.supplierCode"/>	：<input type="text" name="query-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.file.returnsStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="query-EQ_backStatus"><option value="">-<spring:message code="vendor.all"/>-</option><option value="0"><spring:message code="vendor.file.notBack"/></option><option value="1"><spring:message code="vendor.file.returned"/></option></select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Feedback.query()"><spring:message code="vendor.enquiries"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-feedback-search').form('reset')"><spring:message code="vendor.resetting"/></a> 
			</form>
		</div>
	</div>
</div>


	

