<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="vendor.newproduct.newproductDevelopmentQuality"/><!-- 新产品开发质量 --></title>
	<script type="text/javascript" src="${ctx}/static/script/newProduct/newProductInfor.js"></script>
	<script>
	function statusFmt(v,r,i){
		if(r.dataStatus == 0)
			return '<spring:message code="vendor.toReleased"/>';/* 待发布 */
		else if(r.dataStatus == 1)
			return '<spring:message code="vendor.published"/>';/* 已发布 */
		return '<spring:message code="vendor.toReleased"/>';/* 待发布 */
	}
	function qualifyFmt(v,r,i){
		if(r.qualified == 0)
			return '<spring:message code="vendor.nonconformity"/>';/* 不合格 */
		else if(r.qualified == 1)
			return '<spring:message code="vendor.passMuster"/>';/* 合格 */
		return '<spring:message code="vendor.nonconformity"/>';/* 不合格 */
	}
	</script>
	</head>
<body style="margin: 0;padding: 0;" > 
<table id="datagrid-newProduct-list" title="新产品开发质量" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/qualityassurance/Newproduct/${vendor}',method:'post',singleSelect:false,
		toolbar:'#newProductList',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<c:if test="${vendor==false}">
		<th data-options="field:'seq'"><spring:message code="vendor.newproduct.ordinalNumber"/><!-- 序号 --></th>		
		</c:if>
		<th data-options="field:'month'"><spring:message code="vendor.newproduct.month"/><!-- 月份 --></th>
		<!-- <th data-options="field:'qmTime'">时间</th> -->
		<c:if test="${vendor==false}">
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code}"><spring:message code="vendor.check.vendorCode"/><!-- 供应商代码 --></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name}"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		</c:if>
		<th data-options="field:'matName',formatter:function(v,r,i){return r.matName}"><spring:message code="vendor.newproduct.productName"/><!-- 产品名称 --></th>
		<th data-options="field:'sampleSize'"><spring:message code="vendor.newproduct.smallBatch"/><!-- 样件/小批量 --></th>
		<th data-options="field:'deliverTimes'"><spring:message code="vendor.newproduct.numberDeliveries"/><!-- 第几次交付 --></th>
		<th data-options="field:'qualified',formatter:qualifyFmt"><spring:message code="vendor.passMuster"/><!-- 合格 --></th>
		<c:if test="${vendor==false}">
		<th data-options="field:'dataStatus',formatter:statusFmt"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th>
		</c:if>
		<c:if test="${vendor}">
		<th  data-options="field:'ppap'"><spring:message code="vendor.newproduct.passRate"/><!-- 一次通过率 --></th>
		<th data-options="field:'createUserName',formatter:function(v,r,i){if(r.user) return r.user.name;else '';}"><spring:message code="vendor.newproduct.founder"/><!-- 创建人 --></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.creationTime"/><!-- 创建时间 --></th>
		</c:if>
		</tr>
		</thead>
	</table>
	<div id="newProductList" style="padding:5px;">
	<div>	 
		 <form id="form" style="margin-top: 20px;margin-bottom: 20px;" method="post">
		 	<!-- 时间:<input class="easyui-datebox" name="search-EQ_qmTime" style="width:100px;"/> -->
		 	<spring:message code="vendor.newproduct.month"/><!-- 月份 -->：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:150px;"/>
		 	<c:if test="${vendor == false}">
		 	<spring:message code="vendor.check.vendorCode"/><!-- 供应商代码 -->:<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:100px;"/>
		 	<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->:<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:100px;"/>
			</c:if>
		 	<spring:message code="vendor.newproduct.productName"/><!-- 产品名称 -->:<input type="text" name="search-LIKE_matName" class="easyui-textbox" style="width:100px;"/>
		 	<c:if test="${vendor == false}">
		 	<spring:message code="vendor.postStatus"/><!-- 发布状态 -->:<select class="easyui-combobox" data-options="editable:false" name="search-EQ_dataStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toReleased"/><!-- 待发布 --></option><option value="1"><spring:message code="vendor.published"/><!-- 已发布 --></option></select>
			</c:if>
			<c:if test="${vendor}">
			<spring:message code="vendor.newproduct.founder"/><!-- 创建人 -->：<input type="text" name="search-LIKE_user.name" class="easyui-textbox" style="width:100px;"/>
			<spring:message code="vendor.creationTime"/><!-- 创建时间 -->:<input class="easyui-datebox" data-options="editable:false" name="search-GTE_createTime" style="width:100px;"/><spring:message code="vendor.to"/><!-- 到 --><input class="easyui-datebox" data-options="editable:false" name="search-LTE_createTime" style="width:100px;"/>
			</c:if>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="NewProduct.searchInfo()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a> 
		</form>
	  </div>
	  <div> 
	  <c:if test="${vendor == false }">
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="NewProduct.importItem()"><spring:message code="vendor.newproduct.lead-in"/><!-- 导入 --></a>
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="NewProduct.operatItem('publish')"><spring:message code="vendor.posted"/><!-- 发布 --></a>
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="NewProduct.calcul()"><spring:message code="vendor.newproduct.computations"/><!-- 计算 --></a>
	  </c:if>
	  </div>
	</div>
<!-- 导入页面 -->
	<%@include file="newproductImport.jsp" %>
</body>
</html>