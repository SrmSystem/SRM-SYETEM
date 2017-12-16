<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>绩效分数查看页面</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
</head>
<body>
<style type="text/css">

</style>
<div id="layout" class="easyui-layout" fit="true">   
 <div data-options="region:'center'">
    <table id="datagrid-his" data-options="
    fit:true,border:false,
    url:'${ctx}/manager/vendor/vendorPerforScores/viewScoreHis',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    queryParams:{'search-EQ_scoresId':${id},'search-EQ_orgId':${orgId },'search-EQ_brandId':${brandId }},
    toolbar:'#datagrid-tt-his'
    ">
    <thead>
      <tr>
        <th width="100px" data-options="field:'id',checkbox:true"></th>
        <th width="100px" data-options="field:'adjustReason'">调分原因</th>
        <c:forEach items="${mappingScore}" var="mapping">
          	<th width="100px" data-options="field:'${mapping.key}',formatter:function(v,r,i){return ''+v+'';}">${mapping.value}</th>
        </c:forEach>
      </tr>
    </thead>
  </table>
  <div id="datagrid-tt-his">
    <div>
    </div>
    <div>
      <form id="form" method="post">
         <input itemId="id" name="id" type="hidden" value="${id}"/>
                           周期:${cycle}
      	   评估时间:${assessDate}
      </form>
    </div>
  </div>
   </div>
</div> 
 <script type="text/javascript">
	$(function(){
		$('#datagrid-his').datagrid();
	});
</script>

</body>
</html>
