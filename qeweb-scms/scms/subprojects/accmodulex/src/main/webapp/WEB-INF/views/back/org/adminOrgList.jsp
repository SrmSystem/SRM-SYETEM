<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.org.supplierManagement"/><!-- 供应商管理 --></title>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editOrg('+r.id+');"><spring:message code="vendor.edit"/></a>';/* 编辑 */
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-org-list" title="供应商列表" class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/admin/org/getVendorList',method:'post',singleSelect:false,
		toolbar:'#orgListToolbar',queryParams:{search_EQ_orgType:0},
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'"><spring:message code="vendor.coding"/><!-- 编码 --></th>
		<th data-options="field:'midCode'"><spring:message code="vendor.org.shortCode"/><!-- 短编码 --></th>
		<th data-options="field:'name'"><spring:message code="vendor.appellation"/><!-- 名称 --></th>
		<!-- <th data-options="field:'parentOrgName'">上级组织名称</th> -->
		<th data-options="field:'registerTime'"><spring:message code="vendor.RegistrationTime"/><!-- 注册时间 --></th>
		<th data-options="field:'_orgType'"><spring:message code="vendor.organizational"/><!-- 组织级别 --></th>
		<th data-options="field:'_roleType'"><spring:message code="vendor.tissueTypes"/><!-- 组织类型 --></th>
		</tr></thead>
	</table>
	<div id="orgListToolbar" style="padding:5px;">
		<div>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addOrg()">新增</a> -->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteOrg()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
		
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="openVendor()"><spring:message code="vendor.org.openSupplier"/><!-- 开通供应商 --></a>
		
		</div>
		<div>
			<form id="form-org-search" method="post">
			<spring:message code="vendor.coding"/><!-- 编码 -->：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/><!-- 名称 -->：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<input type="hidden" name="search_EQ_orgType" value="0"/>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrg()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-org-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	<div id="win-org-addoredit" class="easyui-dialog" title="新增组织" style="width:400px;height:260px"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-org-addoredit" method="post"  class="baseform" >
				<input id="id" name="id" value="0" type="hidden"/>
				<input name="orgType" value="0" type="hidden"/>
				<input name="parentId" value="0" type="hidden"/>
				<input name="abolished" value="0" type="hidden"/>
				<div>
			          <label><spring:message code="vendor.coding"/><!-- 编码 -->:</label>
			          <input class="easyui-textbox" name="code" type="text"
							data-options="disabled:true"
						/>
			        </div>
					<div>
			          <label><spring:message code="vendor.appellation"/><!-- 名称 -->:</label>
			          <input class="easyui-textbox" id="name" name="name" type="text"
							data-options="required:true"
						/>
			        </div>
					<div>
			          <label><spring:message code="vendor.tissueTypes"/><!-- 组织类型 -->:</label>
			          <input id="win-org-addoredit-roleType" name="roleType" class="easyui-combobox"
						data-options="url:'${ctx}/common/easyuiselect/getOrgRoleType/false',panelHeight:'auto',required:true,editable:false"
						/>
			        </div>
			</form>
		<div id="dialog-adder-bb">
			<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitAddorEditOrg()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
			<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton"  onclick="crel()"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		</div>
	</div>
	
	
		<div id="win-org-open" class="easyui-dialog" title="开通供应商" style="width:400px;height:160px"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-open-bb'">
			<form id="form-org-open" method="post"  class="baseform" >
				<input id="id" name="id" value="0" type="hidden"/>
				<input name="orgType" value="0" type="hidden"/>
				<input name="parentId" value="0" type="hidden"/>
				<input name="abolished" value="0" type="hidden"/>
				<div>
			          <label><spring:message code="vendor.coding"/><!-- 编码 -->:</label>
			          <input class="easyui-textbox" name="code" type="text" id="code"
							data-options="required:true"
						/>
			        </div>
	
			</form>
		<div id="dialog-open-bb">
			<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitOpenVendor()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
			<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton"  onclick="resetOpenVendor()"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		</div>
	</div>
<%-- 	<div id="pp" class="easyui-pagination" data-options="total:${fn:length(users)},pageSize:10" style="background:#efefef;border:1px solid #ccc;"></div> --%>
<script type="text/javascript">
function searchOrg(){
	var searchParamArray = $('#form-org-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-org-list').datagrid('load',searchParams);
}
function crel(){
	$('#name').textbox('setValue','');
	$('#legalPerson').textbox('setValue','');
	$('#win-org-addoredit-roleType').textbox('setValue','');
}
function resetOpenVendor(){
	$('#code').textbox('setValue','');
}
function addOrg(){
	$('#win-org-addoredit').window({
		iconCls:'icon-add',
		title:'<spring:message code="vendor.org.newOrganization"/>'/* 新增组织 */
	});
	$('#tr-org-code').css('display','none');
	$('#form-org-addoredit').form('reset');
	$('#id').val(0);
	$('#win-org-addoredit').window('open');
	$('#win-org-addoredit-roleType').combobox('setValue','0');
}

function openVendor(){
	$('#win-org-open').window({
		iconCls:'icon-add',
		title:'<spring:message code="vendor.org.openSupplier"/>'/* 开通供应商 */
	});
	$('#tr-org-code').css('display','none');
	$('#form-org-open').form('reset');
	$('#id').val(0);
	$('#win-org-open').window('open');
	$('#win-org-open-roleType').combobox('setValue','1');
}

function submitOpenVendor(){
	var url = '${ctx}/manager/admin/org/openVendor';
	var sucMeg = '<spring:message code="vendor.org.openSupplierSuccess"/>！';/* 开通供应商成功 */

	$.messager.progress({title:'<spring:message code="vendor.submission"/>'/* 提交中 */,msg:'<spring:message code="vendor.org.waitFor"/>......'});/* 请等待 */
	$('#form-org-open').form('submit',{
		ajax:true,
		url:url,
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			$.messager.progress('close');
			try{
			var result = eval('('+data+')');
			if(result.success){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,sucMeg,'info');
				$('#win-org-open').window('close');
				$('#datagrid-org-list').datagrid('reload');
			}else{
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
			}
		}
		
	});
}

function submitAddorEditOrg(){
	var url = '${ctx}/manager/admin/org/addNewOrg';
	var sucMeg = '<spring:message code="vendor.org.addOrganizationSuccess"/>！';/* 添加组织成功 */
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = '${ctx}/manager/admin/org/update';
		sucMeg = '<spring:message code="vendor.org.editorialOrganizationSuccess"/>！';/* 编辑组织成功 */
	}
	$.messager.progress({title:'<spring:message code="vendor.submission"/>'/* 提交中 */,msg:'<spring:message code="vendor.org.waitFor"/>......'});/* 请等待 */
	$('#form-org-addoredit').form('submit',{
		ajax:true,
		url:url,
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			$.messager.progress('close');
			try{
			var result = eval('('+data+')');
			if(result.success){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,sucMeg,'info');
				$('#win-org-addoredit').window('close');
				$('#datagrid-org-list').datagrid('reload');
			}else{
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
			}
		}
		
	});
}

function deleteOrg(){
	var selections = $('#datagrid-org-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.org.noRecord"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	var params = $.toJSON(selections); 
	$.messager.confirm('<spring:message code="vendor.org.operatingHints"/>'/* 操作提示 */, '<spring:message code="vendor.org.areYouWantdo"/>？'/* 确定要执行操作吗 */, function (data) {
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/admin/org/deleteOrg',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			try{
						var result = data;
						if(result.success){
							$.messager.show({
	        					title:'<spring:message code="vendor.news"/>',/* 消息 */
	        					msg:'<spring:message code="vendor.org.deletionOrganizationSuccess"/>',/* 删除组织成功 */
	        					timeout:2000,
	        					showType:'show',
	        					style:{
	        						right:'',
	        						top:document.body.scrollTop+document.documentElement.scrollTop,
	        						bottom:''
	        					}
	        				});
	        				$('#datagrid-org-list').datagrid('reload');
						}else{
							$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg,'error');
						}
						}catch (e) {
							$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
						}	
        		}
        	});
        }
	});
}

function editOrg(id){
	$('#win-org-addoredit').window({
		iconCls:'icon-edit',
		title:'<spring:message code="vendor.org.editingGroup"/>'/* 编辑组织 */
	});
	$('#tr-org-code').css('display','');
	$('#win-org-addoredit').window('open');
	$('#form-org-addoredit').form('load','${ctx}/manager/admin/org/getOrg/'+id);
}

function selectOrgType(r){
	if(r.value=='1'){
		var parentId = $('#form-org-addoredit-parentId').combotree('getValue');
		if(parentId==null || parentId==''){
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.org.needChooseOrganization"/>'/* 需要选择上级组织 */,'info');
			$('#win-org-addoredit-orgType').combobox('setValue','0');
			return false;
		}
	}
}

$(function(){
	
	
	
	
});

</script>
</body>
</html>
