<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title><spring:message code="vendor.check.setAccountDate"/><!-- 对账日期设置 --></title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/setting/dateSetting.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagridx" data-options="fit:true,
    url:'${ctx}/manager/check/checks/dateSetting',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'id',checkbox:true"><spring:message code="vendor.check.classID"/><!-- 等级ID --></th>
        <th width="10%" data-options="field:'nop',formatter:dateSetting.vfmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
        <th width="15%" data-options="field:'vendor.code'"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
        <th width="15%" data-options="field:'vendor.name'"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
        <th width="10%" data-options="field:'day'"><spring:message code="vendor.check.statementGenerationDay"/><!-- 对账单生成日 --></th>
        <th width="10%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="vendor.check.effectiveState"/><!-- 生效状态 --></th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <%-- <shiro:hasPermission name="data:setting:add">  --%>
    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="dateSetting.add()"><spring:message code="vendor.new"/><!-- 新增 --></a>
   <%--  </shiro:hasPermission> --%>
   <%--  <shiro:hasPermission name="data:setting:release">  --%>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="dateSetting.release()"><spring:message code="vendor.toEnable"/><!-- 启用 --></a>  
	<%-- </shiro:hasPermission> --%>
	<%-- <shiro:hasPermission name="data:setting:dels">  --%>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="dateSetting.dels()"><spring:message code="vendor.nullify"/><!-- 作废 --></a> 
	<%-- </shiro:hasPermission> --%>
	<%-- <shiro:hasPermission name="data:setting:deleteDatesetting">  --%>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="dateSetting.deleteDatesetting()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>  
	<%-- </shiro:hasPermission> --%>
      <form id="form" method="post">
                             <spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->:<input class="easyui-textbox" name="search-LIKE_vendor.code" type="text" style="width:80px;"/>
                           <spring:message code="vendor.supplierName"/><!-- 供应商名称 -->:<input class="easyui-textbox" name="search-LIKE_vendor.name" type="text" style="width:80px;"/>
                            <spring:message code="vendor.check.effectiveState"/> <!-- 生效状态 -->:
                         <select name="search-EQ_abolished" data-options="editable:false" class="easyui-combobox" style="width:80px;">
                         	<option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option>
                         	<option value="0"><spring:message code="vendor.takeEffect"/><!-- 生效 --></option>
                         	<option value="1"><spring:message code="vendor.nullify"/><!-- 作废 --></option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="dateSetting.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>        
      </form>
    </div>
     <div style="margin-top: 5px;margin-bottom: 5px;color: red">
     <form id="formDefault" method="post">
                          <spring:message code="vendor.check.defaultAccountDay"/><!-- 默认对账日 -->:<input class="easyui-textbox" name="day" data-options="required:true,width:200" value="${defaultDateSetting.day}"/>
         <%-- <shiro:hasPermission name="data:setting:add"> --%> 
        	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="dateSetting.updateDefaultDateSetting()"><spring:message code="vendor.modification"/><!-- 修改 --></a>
        <%-- </shiro:hasPermission> --%>
      </form>
     </div>
  </div>
  
  <div id="dialog-edit" class="easyui-dialog" title="新增"
		data-options="modal:true,closed:true,buttons:'#dialog-edit-bt'" style="width: 40%;height: 90%">
		<div itemId="ct">
		 <form method="post" itemId="form" id="dialog-edit-form" class="baseform" enctype="multipart/form-data">
		 <input id="id" name="id" value="0" type="hidden"/>
		 	<div>
	          <label><spring:message code="vendor.check.supplier"/><!-- 供应商 -->：</label>
	          <input class="easyui-textbox" data-options="required:true,width:200,editable:false" id="orgName" name="orgName"/>
	          <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="showVendor()"><spring:message code="vendor.check.selectSupplier"/><!-- 选择供应商 --></a>
	        </div>
	        <div style="display: none;">
	        	<input class="easyui-textbox" id="orgId" name="vendorId"/>
	        </div>
		 	<div>
	          <label><spring:message code="vendor.check.statementGenerationDay"/><!-- 对账单生成日 -->：</label>
	          <input class="easyui-numberbox" data-options="required:true,width:200" id = "day"name="day"/>
	        </div>
	        <div>
				<label><spring:message code="vendor.check.effectiveState"/><!-- 生效状态 -->:</label>
					<select id="abolished" name="abolished" class="easyui-combobox" style="width:30%" data-options="required:true,editable:false">
						<option value='0'><spring:message code="vendor.toEnable"/><!-- 启用 --></option>
						<option value='1'><spring:message code="vendor.disable"/><!-- 禁用 --></option>
					</select>
			</div>
		 </form>
		 </div>
		 <div id="dialog-edit-bt">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="dateSetting.submit()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
		    <a href="javascript:;" class="easyui-linkbutton" onclick="$('#dialog-edit-form').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		 </div>
	</div>
	
	<div id="org" class="easyui-dialog" title="添加供应商" style="width:60%;height:98%"   
	        data-options="iconCls:'icon-add',modal:true,closed:true">   
	    <table id="datagridss"  data-options="
	    fit:true,method:'post',singleSelect:false,
   		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
	    toolbar:'#ttt'
	  ">
	    <thead>
	      <tr>
	        <th width="30px" data-options="field:'id',checkbox:true"><spring:message code="vendor.check.vendorCode"/><!-- 供应商代码 --></th>
	        <th width="40%" data-options="field:'code'"><spring:message code="vendor.check.vendorCode"/><!-- 供应商代码 --></th>
	        <th width="40%" data-options="field:'name'"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
	      </tr>
	    </thead>
	  </table>
	  <div id="ttt">
	    <div>
	      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="choseVendor('org')"><spring:message code="vendor.confirm"/><!-- 确认 --></a>    
	      <form id="formdatagridss" method="post">
                             <spring:message code="vendor.check.vendorCode"/><!-- 供应商代码 -->:<input class="easyui-textbox" name="search_LIKE_code" type="text" style="width:80px;"/>
                             <spring:message code="vendor.supplierName"/><!-- 供应商名称 -->:<input class="easyui-textbox" name="search_LIKE_name" type="text" style="width:80px;"/>
	       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch('datagridss')"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>      
	       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#formdatagridss').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
	      </form>
	    </div>
	  </div>   
	</div>
  
	<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
		function showVendor() {
			$("#orgName").textbox("setValue", '');
			$("#orgId").textbox("setValue", '');
			$('#org').window('open');
			$('#datagridss').datagrid({url:'${ctx}/manager/admin/org?search_EQ_roleType=1'});
		};
		function addsearch(name) {
			var searchParamArray = $('#form'+name).serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#'+name).datagrid('load',searchParams);
		}
		function  choseVendor(id) {
			var selections = $('#datagridss').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.check.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
				return false;
			}
			if(selections.length>1){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.check.onlyOneRecord"/>！'/* 只能选取一条记录 */,'info');
				return false;
			}
				$("#"+id+"Name").textbox('setValue',selections[0].name);
				$("#"+id+"Id").textbox('setValue',selections[0].id);
				$('#'+id).window('close');
		}
	</script>
</body>
</html>
