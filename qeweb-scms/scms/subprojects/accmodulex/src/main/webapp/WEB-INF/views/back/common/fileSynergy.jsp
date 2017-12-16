<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title></title>



</head>

<body>

   
       <table id="datagrid-fileSynergy-list" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/common/',method:'post',singleSelect:false,
				fit:true,border:false,toolbar:'#fileSynergyToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead>
				<tr>
				<th data-options="field:'operate',formatter:operateFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
				<th data-options="field:'name',width:'100' "><spring:message code="vendor.common.collaborativeName"/><!-- 协同名称 --></th>
				<th data-options="field:'fileSynergyType'"><spring:message code="vendor.common.cooperativeType"/><!-- 协同类型 --></th>
				<th data-options="field:'startTime'"><spring:message code="vendor.common.validStartingtime"/><!-- 有效起始时间段 --></th>
				<th data-options="field:'endTime'"><spring:message code="vendor.common.effectiveEndtime"/><!-- 有效结束时间段 --></th>
				<th data-options="field:'publishStatus'"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th>
				<th data-options="field:'publishTime'"><spring:message code="vendor.releaseTime"/><!-- 发布时间 --></th>
				<th data-options="field:'pulishUserId'"><spring:message code="vendor.releasePeople"/><!-- 发布人 --></th>
				<th data-options="field:'sendBackId'"><spring:message code="vendor.comesBack"/><!-- 回传 --></th>
				</tr>
				</thead>
		</table>
		
		 <div id="fileSynergyToolbar" style="padding:5px;">
		<div>
			<form id="form-fileSynergy-search" method="post">
			<spring:message code="vendor.common.nameSystem"/><!-- 班制名称 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.common.shiftsType"/><!-- 班制类型 -->：<input type="text" name="search-LIKE_billType" class="easyui-textbox" style="width:80px;"/>
			
		   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick=""><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick=""><spring:message code="vendor.new"/><!-- 新增 --></a>
			 
		</form>
		</div>
		</div>

</body>

</html>