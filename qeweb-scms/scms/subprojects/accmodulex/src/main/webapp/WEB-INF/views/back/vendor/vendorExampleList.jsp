<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>标杆企业供应商统计</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorExample.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,title:'标杆统计',
    url:'${ctx}/manager/vendor/vendorExample',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'vendorCode',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.code; else ''}">供应商编号</th>
        <th data-options="field:'vendorName'">供应商名称</th>
        <th data-options="field:'bussinessName'">业务类型</th>
        <th data-options="field:'brandName'">品牌</th>
        <th data-options="field:'productLineName'">产品线</th>
        <th data-options="field:'partsName',formatter:function(v,r){if(r.materialEntity) return r.materialEntity.partsName; else ''}">零部件名称</th>
        <th data-options="field:'materialCode',formatter:function(v,r){if(r.materialEntity) return r.materialEntity.code; else ''}">物料图号</th>
        <th data-options="field:'materialName'">物料名称</th>
        <th data-options="field:'supplyCoefficient'">系数</th>
        <th data-options="field:'biaoganSupplyCoefficient'">标杆供应商车型及系数</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="exp()">导出报表</a>
      <form id="form" method="post">
                             品牌:<input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_vendorName" type="text" style="width:80px;"/>
                             产品线:<input class="easyui-textbox" name="search-LIKE_productLineName" type="text" style="width:80px;"/>
                             物料名称:<input class="easyui-textbox" name="search-LIKE_materialName" type="text" style="width:80px;"/>
                             业务类型:<input class="easyui-textbox" name="search-LIKE_bussinessName" type="text" style="width:80px;"/>
                             零部件名称:<input class="easyui-textbox" name="search-LIKE_materialEntity.partsName" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorExamples.search()">查询</a>  
      </form>
    </div>
  </div>
</body>
</html>
