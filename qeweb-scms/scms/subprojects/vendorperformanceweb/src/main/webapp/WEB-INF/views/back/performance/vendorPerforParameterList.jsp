<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>参数设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforParameter.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforParameter',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'nop',formatter:vendorPerforParameter.vfmt">操作</th>
        <th data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName; else ''}">阶段</th>
        <th data-options="field:'assessDate'">评估时间</th>
        <th data-options="field:'brandName'">品牌</th>
        <th data-options="field:'fname',formatter:function(v,r){if(r.factoryEntity) return r.factoryEntity.name; else ''}">工厂</th>
        <th data-options="field:'parameter'">参数名称</th>
        <th data-options="field:'parameterValue'">参数值</th>
         <th data-options="field:'joinStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'joinStatus',true);}">状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorPerforParameter.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="vendorPerforParameter.dels()">作废</a>
      <form id="form" method="post">
         <table>
      		<tr>
      			<td>开始评估时间:</td>
      			<td><input class="easyui-datebox" name="search-GTE_assessDate" data-options="showSeconds:false" value="" style="width:150px"></td>
      			<td>结束评估时间:</td>
      			<td><input class="easyui-datebox" name="search-LTE_assessDate" data-options="showSeconds:false" value="" style="width:150px"></td>
      			<td>周期:</td>
      			<td><input class="easyui-combobox" id="search-EQ_cycleId" name="search-EQ_cycleId"
      			data-options="
		          url:'${ctx}/manager/vendor/performance/cycle/getAll',    
				  valueField:'id',    
				  textField:'cycleName',
		          editable:false"
      			 style="width:150px"/></td>
      		</tr>
      		<tr>
      			<td>工厂:</td>
      			<td><input class="easyui-combobox" id="search-EQ_factoryId" name="search-EQ_factoryId"
      			data-options="prompt:'工厂',editable:false,
		          url:'${ctx}/manager/basedata/factory/getFactorySelect'
		          "
      			/></td>
      			<td>品牌:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:150px;"/></td>
      			<td>参数名称:</td>
      			<td><input class="easyui-combobox" id="search-LIKE_parameter" name="search-LIKE_parameter"
      			data-options="
		          url:'${ctx}/manager/vendor/vendorPerforParameter/getParameter',    
		          editable:false"
      			/> </td>
      		    <td rowspan="2"><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorPerforParameter.search()">查询</a><a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>  </td>
      		</tr>
      	</table>   
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-vendorPerforParameter-saveUpadte" title="参数设置"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-vendorPerforParameter-saveUpadte" method="post" class="baseform">
				<input id="id" name="id" type="hidden" value="0">
				<div>
		          <label>周期:</label>
		          <input class="easyui-combobox" name="cycleId" data-options="
		          url:'${ctx}/manager/vendor/performance/cycle/getAll',    
				  valueField:'id',    
				  textField:'cycleName',
		          required:true,editable:false" style="width:150px"/>
		        </div>
				<div>
		          <label>评估时间:</label>
		          <input class="easyui-datetimebox" name="assessDate" data-options="showSeconds:true" value="" style="width:150px">
		        </div>
				<div>
		          <label>品牌:</label>
		          <input class="easyui-combobox" data-options="required:true,prompt:'品牌',
		          url:'${ctx}/manager/basedata/bussinessRange/getBussinessRange/0/2'
		          " name="brandId"/>
		        </div>
				<div>
		          <label>工厂:</label>
		          <input class="easyui-combobox" id="factoryId" name="factoryId"
		          data-options="required:true,prompt:'工厂',editable:false,
		          url:'${ctx}/manager/basedata/factory/getFactorySelect'
		          "
		          />
		        </div>
				<div>
		          <label>参数名称:</label>
		          <input class="easyui-combobox" id="parameter" name="parameter" data-options="
		          url:'${ctx}/manager/vendor/vendorPerforParameter/getParameter',    
		          required:true,editable:false"/> 
		        </div>
				<div>
		          <label>参数值:</label>
		          <input class="easyui-textbox" data-options="required:true,prompt:'参数值'" name="parameterValue"/>
		        </div>
				<div id="dialog-adder-bb">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="vendorPerforParameter.submit()">提交</a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-vendorPerforParameter-saveUpadte').form('reset')">重置</a>
				</div>
			</form>
	</div>
<script type="text/javascript">
	$(function(){
		$('#datagrid').datagrid();
	});
</script>
</body>
</html>
