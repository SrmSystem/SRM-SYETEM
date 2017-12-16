<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="list" type="java.util.List" required="true"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/spring.tld" prefix="spring"%>

<c:if test="${list != null }">  
	<c:forEach items="${list}" var="item" varStatus="itemStatus">
		<c:if test="${item!=null }">
			<c:choose>
				<c:when test="${item.itemList!=null && fn:length(item.itemList)>0}">
					<li><span><spring:message code="${item.viewName}"/></span>
						<ul>
							<tags:menuRe list="${item.itemList}"></tags:menuRe>	
						</ul>
					</li>
				</c:when>
				<c:otherwise>
					<li data-options="iconCls:'icon-bullet_page_white'">
						<a href="javascript:;" onclick="toManager('<spring:message code="${item.viewName}"/>','${item.viewUrl}')"><spring:message code="${item.viewName}"/></a>
					</li>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:forEach>
</c:if>