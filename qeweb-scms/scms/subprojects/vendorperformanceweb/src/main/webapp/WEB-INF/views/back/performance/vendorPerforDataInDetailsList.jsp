<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>数据准备情况</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforDataInDetails.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforDataInDetails',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'cycleName'">周期</th>
        <th width="10%" data-options="field:'assessDate'">评估时间</th>
        <th width="10%" data-options="field:'vendorName'">参评供应商名称</th>
        <th width="9%" data-options="field:'brandName'">品牌名称</th>
        <th width="9%" data-options="field:'factoryName'">工厂名称</th>
        <th width="9%" data-options="field:'materialName'">物料类别名称</th>
        <th width="9%" data-options="field:'dimensionsName'">维度</th>
        <th width="9%" data-options="field:'indexName'">指标名称</th>
        <th width="9%" data-options="field:'mustImportElements'">应导入要素</th>
        <th width="9%" data-options="field:'noImportElements'">未导入要素</th>
        <th width="9%" data-options="field:'noImportElementsName'">未导入要素名称</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
      <form id="form" method="post">
      	<table>
      		<tr>
      			<td>开始评估时间:</td>
      			<td><input class="easyui-datebox" name="search-GTE_assessDate" data-options="showSeconds:true" value="" style="width:150px"></td>
      			<td>结束评估时间:</td>
      			<td><input class="easyui-datebox" name="search-LTE_assessDate" data-options="showSeconds:true" value="" style="width:150px"></td>
      			<td>周期:</td>
      			<td><input class="easyui-combobox" id="search-EQ_cycleId" name="search-EQ_cycleId" style="width:150px"/></td>
      		</tr>
      		<tr>
      			<td>指标名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_indexName" type="text" style="width:150px;"/></td>
      			<td>品牌:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:150px;"/></td>
      			<td>工厂名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_element" type="text"  style="width:150px;"/></td>
      			<td>维度:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_dimensionsName" type="text"  style="width:150px;"/></td>
      		    <td rowspan="2"><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorPerforDataIn.search()">查询</a><a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>  </td>
      		</tr>
      	</table>
      </form>
    </div>
  </div>
  <script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
			$('#search-EQ_cycleId').combobox({    
				url:ctx+'/manager/vendor/performance/index/getVendorPerforCycle',    
				valueField:'id',    
				textField:'text' ,
				onSelect: function(rec){    
					$("input[name='search-EQ_cycleId']").val(rec.id);
				}
			});
		})
	</script>
</body>
</html>
