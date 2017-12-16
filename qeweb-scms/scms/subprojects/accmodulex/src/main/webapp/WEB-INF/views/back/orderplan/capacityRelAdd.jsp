<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<!-- 新增 -->
		<div id="win-vendorCapacity-addoredit" >
		<div data-options="fit:true">
			<form id="form-vendorCapacity-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5"  id="myTable_id">
				<tr>
					<input id="companyId" name="companyId" type="hidden" value=""/>
					<input id="capacityId" name="capacityId" type="hidden" value="${vc.id}"/>
					<td> <spring:message code="vendor.orderplan.suppliers"/>: </td><td><input id="companyName"  class="easyui-textbox" value="${vc.vendor.name}"
					     style="width:200px;" data-options="required:true,editable:false,disabled:true"/>
					<c:if test="${  empty vc}">
						<a href="javascript:;" class="easyui-linkbutton" onclick="lookUser()"><spring:message code="vendor.orderplan.select"/></a>
					</c:if>
					</td>
				</tr>
				 <c:forEach items="${cvList}" var="cv"  varStatus="status"  >
					 <tr>
					    <c:choose>
						   <c:when test="${status.index == 0}">  
						   <td><spring:message code="vendor.orderplan.capacityInformation"/>:</td>
						   </c:when>
						   <c:otherwise> 
						    <td></td>
						   </c:otherwise>
						</c:choose>
						 <td><span style='display:block;float:left;padding:1px;'><input type="checkbox"  ${cv.checked}  name="${cv.code}" style="">${cv.name} </span></td>
					 </tr>
				</c:forEach>
				<tr >
					<td><spring:message code="vendor.orderplan.validState"/>:</td>
					<td style="text-align: left;">
						<select class="easyui-combobox" data-options="editable:false" id="abolished" name="abolished"  >
							<option value="0"   <c:if test="${ vc.abolished eq 0  }">selected</c:if>     ><spring:message code="vendor.orderplan.takeEffect"/></option> 
							<option value="1"    <c:if test="${ vc.abolished eq 1  }">selected</c:if>    ><spring:message code="vendor.orderplan.invalid"/></option>
						</select>
					</td>
				</tr>
				</table>
				<c:if test="${  empty vc}">
					<div style="text-align: center;padding:5px;">
						<a href="javascript:;" class="easyui-linkbutton" onclick="submitCapacity('add')"><spring:message code="vendor.orderplan.submit"/></a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorCapacity-addoredit').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
					</div>
				</c:if>
				<c:if test="${not  empty vc}">
					<div style="text-align: center;padding:5px;">
						<a href="javascript:;" class="easyui-linkbutton" onclick="submitCapacity('update')"><spring:message code="vendor.orderplan.submit"/></a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorCapacity-addoredit').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
					</div>
				</c:if>

			</form>
		</div>
	</div>
		
	<div id="kk" class="easyui-dialog" title="添加组织" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#ttt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'"><spring:message code="vendor.orderplan.coding"/></th>
		<th data-options="field:'name'"><spring:message code="vendor.orderplan.appellation"/></th>
		<th data-options="field:'registerTime'"><spring:message code="vendor.orderplan.registrationTime"/></th>
		<th data-options="field:'_orgType'"><spring:message code="vendor.orderplan.organizational"/></th>
		<th data-options="field:'_roleType'"><spring:message code="vendor.orderplan.tissueTypes"/></th>
      </tr>
    </thead>
  </table>
  <div id="ttt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()"><spring:message code="vendor.orderplan.chooseBack"/></a>    
      <form id="form2" method="post">
           <spring:message code="vendor.orderplan.coding"/>：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.orderplan.appellation"/>：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()"><spring:message code="vendor.orderplan.enquiries"/></a> 
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form2').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>     
      </form>
    </div>
  </div>   
</div> 
	
<script type="text/javascript">
//查询
function searchPurchasePlan(){
	var searchParamArray = $('#form-vendorCapacity-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#vendorCapacity').datagrid('load',searchParams);
}
</script>

