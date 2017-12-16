<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商资质预警</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorQEW.js"></script>
</head>

<body>

  <table id="datagridQWE" class="easyui-datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorQEW',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true">供应商调查</th>
        <th data-options="field:'vendorCode',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.code; else ''}">供应商代码</th>
        <th data-options="field:'vendorName',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.name; else ''}">供应商名称</th>
        <th data-options="field:'col1'">体系证书</th>
        <th data-options="field:'col2'">证书类型</th>
        <th data-options="field:'col3'">附件</th>
        <th data-options="field:'col4'">通过时间</th>
        <th data-options="field:'col5'">到期时间</th>
        <th data-options="field:'col6'">认证机构</th>
        <th data-options="field:'col7'">覆盖产品</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-tab_edit',plain:true" onclick="vendorQEW.toemil()">邮件提醒</a>   
      <form id="form" method="post">
                             供应商代码:<input class="easyui-textbox" name="search-LIKE_organizationEntity.code" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_organizationEntity.name" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorQEW.search()">查询</a>  
      </form>
    </div>
  </div>


<script type="text/javascript">
$(function(){

});  
</script>
</body>
</html>
