<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title><!-- 动态数据管理 --><spring:message code="purchase.basedata.DynamicDataManagement"/></title>
    <script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
    <script>
    var labelType = [{ "value": "input", "text": "input" }, { "value": "select", "text": "select" }, { "value": "checkbox", "text": "checkbox" }];
    var useWay = [{ "value": "query", "text": "query" }, { "value": "edit", "text": "edit" }];
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-dynamicItem-list').datagrid('validateRow', editIndex)){
			$('#datagrid-dynamicItem-list').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-dynamicItem-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-dynamicItem-list').datagrid('selectRow', editIndex);
			}
		}
	}
    function managerBeanId(v,r,i){
	    return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDynamicDataDetail('+ r.id +');">' + r.beanId + '</a>';       
    }
    function managerFmt(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editDynamicData('+r.id+');"><spring:message code="purchase.orderMain.orderItemList.Editing"/>/* 编辑 */</a>';
	}
    </script>
    <script type="text/javascript" src="${ctx}/static/script/basedata/dynamicDataList.js"></script>

</head>

<body style="margin:0;padding:0;">


<table id="datagrid-dynamicData-list" title='<!-- 动态数据列表 --><spring:message code="purchase.basedata.DynamicDataList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/dynamicData',method:'post',singleSelect:false,
		toolbar:'#dynamicDataListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
		<thead>
		<tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'beanId',formatter:managerBeanId">BeanId</th>
		<th data-options="field:'objectName'"><!-- 对象名称 --><spring:message code="purchase.basedata.ObjectName"/></th>
		<th data-options="field:'remark'"><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/></th>
		<th data-options="field:'enable',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',false);}"><!-- 启用状态 --><spring:message code="purchase.basedata.EnabledState"/></th>
		<th data-options="field:'manager',formatter:managerFmt"><!-- 管理 --><spring:message code="purchase.basedata.Administration"/></th>
		</tr>
		</thead>
	</table>
	<div id="dynamicDataListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addDynamicData()"><!-- 新增 --><spring:message code="vendor.new"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="importDynamic()"><!-- 批量导入 --><spring:message code="purchase.basedata.BatchImport"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="enableDynamic(1)"><!-- 启用 --><spring:message code="vendor.toEnable"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="enableDynamic(0)"><!-- 禁用 --><spring:message code="vendor.disable"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="delDynamic()"><!-- 删除 --><spring:message code="vendor.deleting"/></a>
		</div>
		<div>
			<form id="form-dynamicData-search" method="post">
			BeanId：<input type="text" name="search-LIKE_beanId" class="easyui-textbox" style="width:120px;"/>
			<!-- 对象名称 --><spring:message code="purchase.basedata.ObjectName"/>：<input type="text" name="search-LIKE_objectName" class="easyui-textbox" style="width:120px;"/>
			<!-- 启用状态 --><spring:message code="purchase.basedata.EnabledState"/>：<select class="easyui-combobox" name="search-EQ_enable" style="width:120px">
			<option value="">---<!-- 全部 --><spring:message code="vendor.all"/>---</option><option value="0"><spring:message code="vendor.NO"/></option><option value="1"><spring:message code="vendor.YES"/></option></select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchDynamicData()"><!-- 查询 --><spring:message code="vendor.enquiries"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-dynamicData-search').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
			</form>
		</div>
	</div>
	
	<!-- 导入 -->
	<div id="win-dynamic-import" class="easyui-dialog" title='<!-- 导入配置 --><spring:message code="purchase.basedata.ImportConfiguration"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-dynamic-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/basedata/dynamicData/filesUpload"> 
				<div style="margin-bottom:20px">
					<!-- 文件 --><spring:message code="purchase.basedata.Files"/>：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/template/Dynamic.xls">动态字段模板.xls</a>    --%>
					<!-- 模板 --><spring:message code="purchase.basedata.Template"/>：<a href="javascript:;" onclick="File.download('WEB-INF/templates/Dynamic.xls','Dynamic')"><spring:message code="purchase.basedata.DynamicFieldTemplate"/><!-- 动态字段模板 -->.xls</a> 
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportdynamic();"><!-- 保存 --><spring:message code="purchase.basedata.Preservation"/></a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-dynamic-import').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
				</div>
			</form>  
		</div>
	</div>
		
	<div id="win-dynamicData-addoredit" class="easyui-dialog" title='<!-- 新增动态数据 --><spring:message code="purchase.basedata.NEWDynamicData"/>' style="width:800px;height:500px"
		data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-dynamic-addoredit" method="post" >
				<div id="deliveryListToolbar" style="padding:5px;">
					<input id="id" name="id" type="hidden"/>    
					<div>
						<a href="javascript:;" class="easyui-linkbutton" id="dynamic-save" data-options="iconCls:'icon-add',plain:true" onclick="saveDynamic()"><!-- 保存 --><spring:message code="purchase.basedata.Preservation"/></a>
					</div>
					<div>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>BeanId:<input class="easyui-textbox" id="beanId" name="beanId" type="text" data-options="required:true"/></td>
							<td><!-- 对象名称 --><spring:message code="purchase.basedata.ObjectName"/>:<input class="easyui-textbox" id="objectName" name="objectName" type="text" data-options="required:true"/></td>
							<td><!-- 启用状态 --><spring:message code="purchase.basedata.EnabledState"/>:<select class="easyui-combobox" id="enable" name="enable" data-options="required:true,width:'120px'"><option value="1"><spring:message code="vendor.YES"/></option><option value="0"><spring:message code="vendor.NO"/></option></select></td>
						</tr>
						<tr>  
							<td colspan="3" align="left"><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/>: <input class="easyui-textbox" id="remark" name="remark" type="text" data-options="multiline:true" style="height:40px;width: 90%;"/></td>
						</tr>
					</table>
					</div>
				</div>
			</form>
			<div id="tb" style="padding:5px;height:auto">
				<div style="margin-bottom:5px">
					<a href="#" class="easyui-linkbutton" id="dynamic-item-add" iconCls="icon-add" plain="true" onclick="javascript:appendRow()"></a>  
					<a href="#" class="easyui-linkbutton" id="dynamic-item-del"  iconCls="icon-remove" plain="true" onclick="javascript:deleteRow()"></a> 
				</div>
			</div>
			<table id="datagrid-dynamicItem-list" title='<!-- 字段详情 --><spring:message code="purchase.basedata.FieldDetails"/>' class="easyui-datagrid", 
				data-options="
				iconCls: 'icon-edit',
				singleSelect: true,
				toolbar:'#tb',
				method:'post',
				onClickRow: onClickRow,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]" >
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>      
				<th data-options="field:'colCode',width:80,editor:{type:'textbox', options: {required: true}}"><!-- 字段编码 --><spring:message code="purchase.basedata.FieldEncoding"/></th>
				<th data-options="field:'name',width:80,editor:{type:'textbox', options: {required: true}}"><!-- 显示名称 --><spring:message code="purchase.basedata.LooksNames"/></th>
				<th data-options="field:'type',width:80,formatter:function(value,row){
							 if (value == 0 || value == '') {
						        return;
						    }
							for (var i = 0; i < labelType.length; i++) {
						        if (labelType[i].value == value) {
						            return labelType[i].text;
						        }
						    }
						},editor:{
							type:'combobox',
							options:{
								valueField:'value',
								textField:'text',
								data: labelType,
								required:true
							}
						}"><!-- 标签类型 --><spring:message code="purchase.basedata.TagTypes"/></th>
				<th data-options="field:'way',width:80,formatter:function(value,row){
							 if (value == 0 || value == '') {
						        return;
						    }
							for (var i = 0; i < useWay.length; i++) {
						        if (useWay[i].value == value) {
						            return useWay[i].text;
						        }
						    }
						},editor:{
							type:'combobox',
							options:{
								valueField:'value',
								textField:'text',
								data: useWay,
								required:true
							}
						}"><!-- 使用场景 --><spring:message code="purchase.basedata.UseScenarios"/></th>   
				<th data-options="field:'statusKey',width:80,editor:'textbox'"><!-- 状态编码 --><spring:message code="purchase.basedata.StateEncoding"/></th>   
				<th data-options="field:'range',width:80,editor:'textbox'"><!-- 值范围 --><spring:message code="purchase.basedata.RangeOfValues"/></th>   
				<th data-options="field:'filter',align:'center',formatter:function(value,row){if(value) return '<spring:message code="vendor.YES"/>'; else return '<spring:message code="vendor.NO"/>'},editor:{type:'checkbox',options:{on:'<spring:message code="vendor.YES"/>',off:'<spring:message code="vendor.NO"/>'}}"><spring:message code="purchase.basedata.IsIUsedAsAScreeningCondition"/><!-- 是否作为筛选条件 --></th>
				<th data-options="field:'required',align:'center',formatter:function(value,row){if(value) return '<spring:message code="vendor.YES"/>'; else return '<spring:message code="vendor.NO"/>'},editor:{type:'checkbox',options:{on:'<spring:message code="vendor.YES"/>',off:'<spring:message code="vendor.NO"/>'}}"><!-- 是否必须 --><spring:message code="purchase.basedata.IsItNecessary"/></th>
				<th data-options="field:'show',align:'center',formatter:function(value,row){if(value) return '<spring:message code="vendor.YES"/>'; else return '<spring:message code="vendor.NO"/>'},editor:{type:'checkbox',options:{on:'<spring:message code="vendor.YES"/>',off:'<spring:message code="vendor.NO"/>'}}"><spring:message code="purchase.basedata.WhetherToShow"/><!-- 是否显示 --></th>  
				</tr></thead>
			</table>  
		</div>
	</div>

</body>
</html>