<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>		
  
  	<table id="datagrid-capacity-list" class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/order/purchaseplanvendor/getCapacityInfo/${poPlanid}/${isVendor}',
		method:'post',singleSelect:false,
		toolbar:'#capacity',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]" 
		>
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'planItem.plan.month'"><spring:message code="vendor.orderplan.versionNumber"/></th>
		<th data-options="field:'planItem.plan.group.name'"><spring:message code="vendor.orderplan.purchasingGroup"/></th>
		<c:if test="${isVendor == false}">
		<th data-options="field:'planItem.vendor.code'"><spring:message code="vendor.orderplan.supplierCode"/></th>
		<th data-options="field:'planItem.vendor.name'"><spring:message code="vendor.orderplan.supplierName"/></th>
		</c:if>
		<th data-options="field:'createTime'"><spring:message code="vendor.orderplan.uploadTime"/></th>
		<th data-options="field:'opt' ,formatter:viewCapacity,width:100 " ><spring:message code="vendor.orderplan.view"/></th>
      </tr>
    </thead>
  </table>
  <div id="capacity">
    <div>
    <c:if test="${isVendor == false}">
	      <form id="form2" method="post">
	            <spring:message code="vendor.orderplan.supplierCode"/>：<input type="text" name="search-LIKE_planItem.vendor.code" class="easyui-textbox" style="width:80px;"/>
				<spring:message code="vendor.orderplan.supplierName"/>：<input type="text" name="search-LIKE_planItem.vendor.name" class="easyui-textbox" style="width:80px;"/>
	       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchCapacityInfo()"><spring:message code="vendor.orderplan.enquiries"/></a> 
	       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form2').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>     
	      </form>
      </c:if>
    </div>
  </div>   

	
<script type="text/javascript">
	function viewCapacity(v,r,i) {
		var a='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openCapacity('+ r.planItem.id +');"><spring:message code="vendor.orderplan.detail"/>详细</a>';;
		return a;
	}

	//打开详细页面
	function openCapacity(id) {
		var url = "/manager/order/purchaseplanvendor/viewCapacityInfo/"+id;
		new dialog().showWin($.i18n.prop('vendor.orderplan.detailProductionTable'), 600, 480, ctx + url);  
	}
	//查询
function searchCapacityInfo(){
	var searchParamArray = $('#form2').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-capacity-list').datagrid('load',searchParams);
}
	

</script>

