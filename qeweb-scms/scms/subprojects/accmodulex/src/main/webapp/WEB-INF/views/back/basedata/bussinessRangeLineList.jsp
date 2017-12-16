<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.basedata.BusinessProductLineManagement"/></title><!-- 业务产品线管理 -->
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/bussinessRange.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/bussinessRangeLineList.js"></script>
</head>

<body>
<div class="easyui-layout" data-options="fit:true">
 <div class="easyui-panel" title='<spring:message code="purchase.basedata.OwnedBrand"/>' data-options="region:'west',width:205"><!-- 所属品牌 -->
  <ul id="tree-bussinessRange" class="easyui-tree" data-options="
    title:'<spring:message code="purchase.basedata.BusinessScope"/>',<!-- 公司业务范围 -->
    url:'${ctx}/manager/basedata/bussinessRange/getBussinessRangeEasyuiTree',
    queryParams:{type:2},
    onClick:BussinessRangeLine.expandRangeLine,
    onBeforeSelect:BussinessRangeLine.selectRangeBrand,
  "></ul>
 </div>

<div class="easyui-panel" data-options="region:'center'">
	<!-- 产品线列表 --><table id="datagrid-bussinessRange-list" title='<spring:message code="purchase.basedata.ProductLineList"/>' class="easyui-datagrid"
		data-options="
		fit:true,border:false,
		url:'${ctx}/manager/basedata/bussinessRange',method:'post',singleSelect:false,
		toolbar:'#bussinessRangeListToolbar',queryParams:{'query-EQ_bussinessType':3},
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manager',formatter:BussinessRangeLine.operateFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		<th data-options="field:'code'"><spring:message code="purchase.basedata.ProductLineNumber"/><!-- 产品线编号 --></th>
		<th data-options="field:'name'"><spring:message code="purchase.basedata.ProductLineName"/><!-- 产品线名称 --></th>
		<th data-options="field:'rangeCode',formatter:BussinessRangeLine.rangeCodeFmt"><spring:message code="purchase.basedata.BrandNumber"/><!-- 品牌编号 --></th>
		<th data-options="field:'rangeName',formatter:BussinessRangeLine.rangeNameFmt"><spring:message code="purchase.basedata.BrandName"/><!-- 所属品牌名称 --></th>
		<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.basedata.EffectiveState"/><!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="bussinessRangeListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BussinessRangeLine.add()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="BussinessRangeLine.abolish()"><spring:message code="purchase.basedata.ToVoid"/><!-- 作废 --></a>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="BussinessRangeLine.del()">删除</a>
 -->		</div>
		<div>
			<form id="form-bussinessRange-search" method="post">
			<input name="query-EQ_bussinessType" type="hidden" value="3"/>
			<!-- 产品线编号 --><spring:message code="purchase.basedata.ProductLineNumber"/>：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 产品线名称 --><spring:message code="purchase.basedata.ProductLineName"/>：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BussinessRangeLine.query()"><spring:message code="purchase.basedata.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-bussinessRange-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	<div id="win-bussinessRange-addoredit" class="easyui-window" title='<!-- 新增产品线 --><spring:message code="purchase.basedata.NewProductLining"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-bussinessRange-addoredit" method="post" data-options="onLoadSuccess:BussinessRangeLine.rangeLineLoadSuc">
				<input id="id" name="id" value="-1" type="hidden"/>
				<input itemId="parentId" name="parentId" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 所属品牌编号 --><spring:message code="purchase.basedata.TheBrandCoding"/>:</td><td><input class="easyui-textbox" itemId="parentCode" type="text"
						data-options="disabled:true"
					/>
					</td>
				</tr>
				<tr>
					<td><!-- 所属品牌名称 --><spring:message code="purchase.basedata.TheBrandName"/>:</td><td><input class="easyui-textbox" itemId="parentName" type="text"
						data-options="disabled:true"
					/>
					</td>
				</tr>
				<tr>
					<td><!-- 产品线编号 --><spring:message code="purchase.basedata.ProductLineNumber"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="prompt:'<!-- 编号由系统自动生成 --><spring:message code="purchase.basedata.NumberingIsAutomaticallyGeneratedByTheSystem"/>'"
					/>
					</td>
				</tr>
				<tr>
					<td><!-- 产品线名称 --><spring:message code="purchase.basedata.ProductLineName"/>:</td><td><input class="easyui-textbox" id="bussinessLineName" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/>:</td><td><input class="easyui-textbox" id="bussinessLineRemark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BussinessRangeLine.submit()"><!-- 提交 --><spring:message code="vendor.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
				</div>
			</form>
		</div>
	</div>
</div>

</div>

</body>
</html>
