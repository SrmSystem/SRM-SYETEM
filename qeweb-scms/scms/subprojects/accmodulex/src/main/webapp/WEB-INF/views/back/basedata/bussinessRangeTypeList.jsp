<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.basedata.BusinesstypeManagement"/></title><!-- 业务类型管理 -->
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/bussinessRange.js"></script>
</head>

<body>
<div class="easyui-layout" data-options="fit:true">
<div class="easyui-panel" data-options="region:'west',width:205">
  <div class="easyui-panel" title='<spring:message code="purchase.basedata.RelationBusinessScope"/>' data-options="fit:true,border:false"><!-- 所属业务范围 -->
  <ul id="tree-bussinessRange" class="easyui-tree" data-options="
    border:false,
    queryParams:{type:0},
    url:'${ctx}/manager/basedata/bussinessRange/getBussinessRangeEasyuiTree',
    onClick:BussinessRangeType.expandRangeType
  "></ul>
  </div>
</div>
<div class="easyui-panel" data-options="region:'center'">
	<!-- 业务类型列表 --><table id="datagrid-bussinessRange-list" title="<spring:message code="purchase.basedata.BusinesstypeList"/>" class="easyui-datagrid"
		data-options="method:'post',singleSelect:false,border:false,fit:true,
		toolbar:'#bussinessRangeListToolbar',queryParams:{'query-EQ_bussinessType':1},
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="40px" data-options="field:'manager',formatter:BussinessRangeType.operateFmt"><!-- 操作 --><spring:message code="vendor.operation"/></th>
		<th width="130px" data-options="field:'code'"><!-- 业务类型编号 --><spring:message code="purchase.basedata.BusinessTypingNumber"/></th>
		<th width="130px" data-options="field:'name'"><!-- 业务类型名称 --><spring:message code="purchase.basedata.BusinessTypingName"/></th>
		<th width="130px" data-options="field:'rangeCode',formatter:BussinessRangeType.rangeCodeFmt"><!-- 所属公司业务范围编号 --><spring:message code="purchase.basedata.TheBusinessScopeCode"/></th>
		<th width="130px" data-options="field:'rangeName',formatter:BussinessRangeType.rangeNameFmt"><!-- 所属公司业务范围名称 --><spring:message code="purchase.basedata.TheBusinessScopeName"/></th>
		<th width="130px" data-options="field:'remark'"><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/></th>
		<th width="80px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><!-- 生效状态 --><spring:message code="purchase.basedata.EffectiveState"/></th>
		</tr></thead>
	</table>
	<div id="bussinessRangeListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BussinessRangeType.add()"><!-- 新增 --><spring:message code="vendor.new"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="BussinessRangeType.abolish()"><!-- 作废 --><spring:message code="purchase.basedata.ToVoid"/></a>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="BussinessRangeType.del()">删除</a> -->
		</div>
		<div>
			<form id="form-bussinessRange-search" method="post">
			<input name="query-EQ_bussinessType" type="hidden" value="1"/>
			<input id="pidd" name="query-EQ_parentId" type="hidden" value=""/>
			<!-- 业务类型编号 --><spring:message code="purchase.basedata.BusinessTypingNumber"/>：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 业务类型名称 --><spring:message code="purchase.basedata.BusinessTypingName"/>：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BussinessRangeType.query()"><!-- 查询 --><spring:message code="purchase.basedata.Query"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-bussinessRange-search').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
			</form>
		</div>
	</div>
	<div id="win-bussinessRange-addoredit" class="easyui-dialog" title="新增业务类型<spring:message code="purchase.basedata.NewbusinessTypes"/>" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-bussinessRange-addoredit" method="post" data-options="onLoadSuccess:BussinessRangeType.rangeTypeLoadSuc">
				<input id="id" name="id" value="-1" type="hidden"/>
				<input itemId="parentId" name="parentId" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 所属业务范围编号 --><spring:message code="purchase.basedata.TheBusinessScopeCode"/>:</td><td><input class="easyui-textbox" itemId="parentCode" type="text"
						data-options="disabled:true"
					/>
					</td>
				</tr>
				<tr>
					<td><!-- 所属业务范围名称 --><spring:message code="purchase.basedata.TheBusinessScopeName"/>:</td><td><input class="easyui-textbox" itemId="parentName" type="text"
						data-options="disabled:true"
					/>
					</td>
				</tr>
				<tr>
					<td><!-- 业务类型编号 --><spring:message code="purchase.basedata.BusinessTypingNumber"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="prompt:'<spring:message code="purchase.basedata.NumberingIsAutomaticallyGeneratedByTheSystem"/>'"
					/><!-- 编号由系统自动生成 -->
					</td>
				</tr>
				<tr>
					<td><!-- 业务类型名称 --><spring:message code="purchase.basedata.BusinessTypingName"/>:</td><td><input id="bussinessTypeName" class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/>:</td><td><input id="bussinessTypeRemark" class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BussinessRangeType.submit()"><spring:message code="purchase.basedata.vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
function resetForm(){
	$('#bussinessTypeName').textbox('setText','');
	$('#bussinessTypeRemark').textbox('setText','');
}
</script>
</body>
</html>
