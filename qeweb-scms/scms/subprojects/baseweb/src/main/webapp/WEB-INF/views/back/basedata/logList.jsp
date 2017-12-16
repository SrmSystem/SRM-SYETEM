<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title>日志管理</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorExample.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/basedata/log',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'userLogin'"><spring:message code="base.loginAccount"/><!-- 登录帐号 --></th>
        <th data-options="field:'orgName'"><spring:message code="base.OrganizationName"/><!-- 组织名称 --></th>
        <th data-options="field:'operationName'"><spring:message code="base.OperationName"/><!-- 操作名称 --></th>
        <th data-options="field:'content'"><spring:message code="base.Content"/><!-- 内容 --></th>
        <th data-options="field:'ip'"><spring:message code="base.UserIP"/><!-- 用户IP --></th>
        <th data-options="field:'os'"><spring:message code="base.UserOperatingSystem"/><!-- 用户操作系统 --></th>
        <th data-options="field:'createTime'"><spring:message code="base.OperatingTime"/><!-- 操作时间 --></th>
        <th data-options="field:'requestUrl'"><spring:message code="base.URLRequest"/><!-- 请求的URL --></th>
<!--         <th data-options="field:'requestPage'">请求Page</th> -->
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
      <form id="form" method="post">
      	<spring:message code="base.loginAccount"/><!-- 登录帐号 -->:<input class="easyui-textbox" name="search-LIKE_userLogin" type="text" style="width:80px;"/>
        <spring:message code="base.OrganizationName"/><!-- 组织名称 -->:<input class="easyui-textbox" name="search-LIKE_orgName" type="text" style="width:80px;"/>
        <spring:message code="base.OperationName"/><!-- 操作名称 -->:<input class="easyui-textbox" name="search-LIKE_operationName" type="text" style="width:80px;"/>
        <spring:message code="base.UserOperatingSystem"/><!-- 用户操作系统 -->:<input class="easyui-textbox" name="search-LIKE_os" type="text" style="width:80px;"/>
        <spring:message code="base.URLRequest"/><!-- 请求的URL -->:<input class="easyui-textbox" name="search-LIKE_requestUrl" type="text" style="width:80px;"/>
        <!-- 请求Page:<input class="easyui-textbox" name="search-LIKE_requestPage" type="text" style="width:80px;"/></br> -->
      	<spring:message code="base.UserIP"/><!-- 用户IP -->:<input class="easyui-textbox" name="search-LIKE_ip" type="text" style="width:80px;"/>
        <spring:message code="base.OperatingTime"/><!-- 操作时间 -->:<input type="text" id="startTime" name="search-GTE_createTime" class="easyui-datetimebox" style="width:100px;"/>
		- <input type="text" id="endTime" name="search-LTE_createTime" class="easyui-datetimebox" style="width:100px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorExamples.search()"><spring:message code="button.query"/><!-- 查询 --></a>  
      </form>
    </div>
  </div>
</body>
</html>
