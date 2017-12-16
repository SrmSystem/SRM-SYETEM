<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>参评设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/reviews.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforReviews/reviewsList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100], 
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true">参评ID</th>
        <th width="10%" data-options="field:'col4'">品牌</th>
        <th width="10%" data-options="field:'col1'">工厂</th>
        <th width="10%" data-options="field:'col2'">物料类别编号</th>
        <th width="10%" data-options="field:'col3'">物料类别名称</th>
        <th width="10%"  data-options="field:'code',formatter:function(v,r){if(r.vendorBaseInfoEntity) {if(r.vendorBaseInfoEntity.org){return r.vendorBaseInfoEntity.org.code;}} else ''}">供应商代码</th>
        <th width="10%" data-options="field:'name',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.name; else ''}">供应商名称</th>
        <th width="10%" data-options="field:'vendorPhaseName',formatter:function(v,r){if(r.vendorBaseInfoEntity) {if(r.vendorBaseInfoEntity.vendorPhase){return r.vendorBaseInfoEntity.vendorPhase.name;}} else ''}">供应商阶段</th>
        <th width="10%" data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName; else ''}">周期</th>
        <th width="10%" data-options="field:'joinStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',true);}">是否参评</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
      <form id="form" method="post">
                             供应商编码:<input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.org.code" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.org.name" type="text" style="width:80px;"/>
                             是否参评:
                <select name="search-EQ_joinStatus" class="easyui-combobox" style="width:80px;">
                	<option value="">请选择</option>
                	<option value="1">是</option>
                	<option value="0">否</option>
                </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="reviews.search()">查询</a>   
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>     
      </form>
  </div>
<form id="form-export">
</form>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
</script>
</body>
</html>
