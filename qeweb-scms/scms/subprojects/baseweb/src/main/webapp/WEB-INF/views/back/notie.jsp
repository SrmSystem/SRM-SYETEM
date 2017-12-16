<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公告</title>
</head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>
<body>
<div style="position:absolute; height:100%; overflow:auto;width: 100%;">
		<ol style="    padding: 5px;">
			<c:forEach items="${list }" var="list">
				<li>
					<p>
					<c:if test="${list.noticeType==1 }">
					<font style="color: #F00;">[<spring:message code="sys.notice.sticky"/>]</font>
					</c:if>
					<c:if test="${list.noticeType!=1 }">
					<font style="color: #7890E6;">[<spring:message code="sys.notice.common"/>]</font>
					</c:if>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a style=" cursor:pointer;" onclick="window.parent.lookNotice(${list.id})" title="${list.title}">${list.title}</a><span class="num" style="float: right;"><fmt:formatDate value="${list.createTime}" pattern="yyyy-MM-dd"/></span></p>
				</li>
			</c:forEach>
		</ol>
		<div class="clear"></div>
</div>
</body>
</html>