<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>业务品牌管理</title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/bussinessRange.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/bussinessRangeBrandList.js"></script>
</head>

<body style="margin:0;padding:0;">
<div class="easyui-layout" data-options="fit:true">
<div class="easyui-panel" data-options="region:'west',width:205,title:'公司业务范围'">
  <ul id="tree-bussinessRange" class="easyui-tree" data-options="
    url:'${ctx}/manager/basedata/bussinessRange/getBussinessRangeEasyuiTree',
    queryParams:{type:1},
    onClick:BussinessRangeBrand.expandRangeBrand,
    onBeforeSelect:BussinessRangeBrand.selectRangeType,
  "></ul>
</div>
<div class="easyui-panel" data-options="region:'center'">
	<table id="datagrid-bussinessRange-list" title="品牌列表" class="easyui-datagrid"
		data-options="fit:true,border:false,
		url:'${ctx}/manager/basedata/bussinessRange',method:'post',singleSelect:false,
		toolbar:'#bussinessRangeListToolbar',queryParams:{'query-EQ_bussinessType':2},
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manager',formatter:BussinessRangeBrand.operateFmt">操作</th>
		<th data-options="field:'code'">品牌编号</th>
		<th data-options="field:'name'">品牌名称</th>
		<th data-options="field:'rangeCode',formatter:BussinessRangeBrand.rangeCodeFmt">所属业务类型编号</th>
		<th data-options="field:'rangeName',formatter:BussinessRangeBrand.rangeNameFmt">所属业务类型名称</th>
		<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
		</tr></thead>
	</table>
	<div id="bussinessRangeListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BussinessRangeBrand.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="BussinessRangeBrand.abolish()">作废</a>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="BussinessRangeBrand.del()">删除</a>
 -->		</div>
		<div>
			<form id="form-bussinessRange-search" method="post">
			<input name="query-EQ_bussinessType" type="hidden" value="2"/>
			品牌编号：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			品牌名称：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BussinessRangeBrand.query()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-bussinessRange-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-bussinessRange-addoredit" class="easyui-window" title="新增品牌" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-bussinessRange-addoredit" method="post" data-options="onLoadSuccess:BussinessRangeBrand.rangeBrandLoadSuc">
				<input id="id" name="id" value="-1" type="hidden"/>
				<input itemId="parentId" name="parentId" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>所属业务类型编号:</td><td><input class="easyui-textbox" itemId="parentCode" type="text"
						data-options="disabled:true"
					/>
					</td>
				</tr>
				<tr>
					<td>所属业务类型名称:</td><td><input class="easyui-textbox" itemId="parentName" type="text"
						data-options="disabled:true"
					/>
					</td>
				</tr>
				<tr>
					<td>品牌编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="prompt:'编号由系统自动生成'"
					/>
					</td>
				</tr>
				<tr>
					<td>品牌名称:</td><td><input class="easyui-textbox" id="bussinessBrandName" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" id="bussinessBrandRemark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BussinessRangeBrand.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()">重置</a>
				</div>
			</form>
		</div>
	</div>
</div>
</div>

</body>
</html>
