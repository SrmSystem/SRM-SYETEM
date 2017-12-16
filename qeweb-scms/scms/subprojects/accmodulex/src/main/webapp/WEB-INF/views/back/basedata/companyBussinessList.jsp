<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 公司业务管理 --><spring:message code="purchase.basedata.CompanyBusinessManagement"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editCompanyBussiness('+r.id+');"><spring:message code="purchase.orderMain.orderItemList.Editing"/>/* 编辑 */</a>';
		}
		
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/companyBussinessList.js"></script>
</head>

<body style="margin:0;padding:0;">
<div>
	<!-- 公司业务列表 --><table id="datagrid-companyBussiness-list" title="<spring:message code="purchase.basedata.CompanyBusinessList"/>" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/companyBussiness',method:'post',singleSelect:false,
		toolbar:'#companyBussinessListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'"><!-- 公司业务编号 --><spring:message code="purchase.basedata.CompanyBusinessCode"/></th>
		<th data-options="field:'name'"><!-- 公司业务名称 --><spring:message code="purchase.basedata.CompanyBusinessName"/></th>
		<th data-options="field:'manager',formatter:managerFmt"><!-- 管理 --><spring:message code="purchase.basedata.Administration"/></th>
		</tr></thead>
	</table>
	<div id="companyBussinessListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addCompanyBussiness()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteCompanyBussiness()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
		</div>
		<div>
			<form id="form-companyBussiness-search" method="post">
			<!-- 公司业务编号 --><spring:message code="purchase.basedata.CompanyBusinessCode"/>：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 公司业务名称 --><spring:message code="purchase.basedata.CompanyBusinessName"/>：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchCompanyBussiness()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-companyBussiness-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	<div id="win-companyBussiness-addoredit" class="easyui-window" title='<!-- 新增公司业务 --><spring:message code="purchase.basedata.NewBusiness"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-companyBussiness-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 公司业务编号 --><spring:message code="purchase.basedata.CompanyBusinessCode"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td><!-- 公司业务名称 --><spring:message code="purchase.basedata.CompanyBusinessName"/>:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/>:</td><td><input class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditcompanyBussiness()"><!-- 提交 --><spring:message code="vendor.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-companyBussiness-addoredit').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
				</div>
			</form>
		</div>
	</div>
</div>


</body>
</html>
