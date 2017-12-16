<%@page import="com.qeweb.scm.basemodule.entity.UserEntity"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title></title>
    <%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/css.css"> --%>
</head>
<body>

    <c:if test="${needRefresh}">
        <script type="text/javascript">
            top.document.getElementById("username").innerText = "${user.name}";
        </script>
    </c:if>

    <div>${msg}</div>

    <h4>切换身份</h4>
	当前身份：${cuser.name} 
    <c:if test="${isRunas}">|
        上一个身份：${previousUsername}
        |
        <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true" onclick="switchBack()">切换回该身份</a>
        <%-- <a href="${pageContext.request.contextPath}/runas/switchBack">切换回该身份</a> --%>
    </c:if>

    <h5>切换到其他身份：</h5>

    <c:choose>
        <c:when test="${empty fromUsers}">无</c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                <tr>
                    <th>用户名</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${fromUsers}" var="fuser">
                    <tr>
                        <td>${fuser.name}</td>
                        <td>
                        <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true" onclick="switchTo(${fuser.id})">切换到该身份</a>
        
                            <%-- <a href="${pageContext.request.contextPath}/runas/switchTo/${id}">切换到该身份</a> --%>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <h5>授予身份给其他人：</h5>
    <table class="table">
        <thead>
        <tr>
            <th>用户名</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%List<UserEntity> toUsers = (List<UserEntity>)request.getAttribute("toUsers");
        List<UserEntity> allUsers = (List<UserEntity>)request.getAttribute("allUsers");
        for(UserEntity u:allUsers){
	        boolean isTo = false;
            for(UserEntity toUser:toUsers){
               	if(toUser.getId()==u.getId()){
               		isTo = true;
               	}
            }
            %><tr>
                <td><%=u.getName() %></td>
                <td><%
            if(isTo){
            	%><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true" onclick="revoke(<%=u.getId()%>)">回收身份</a>
		        <%
            }else{
            	%><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true" onclick="grant(<%=u.getId()%>)">授予身份</a>
		        <%
            }
                %><c:if test="${cuser.loginName eq 'ADMIN'}">
		        	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true" onclick="gain(<%=u.getId()%>)">获得身份</a>
        		</c:if></td>
            </tr><%
        }
                %>
        
        </tbody>
    </table>
<script type="text/javascript">
function switchBack(){
	$.ajax({
	 	url: ctx + '/manager/common/runas/switchBack', 
        type: 'post',
        dataType:"json",
        contentType : 'application/json',
        success: function (data) {
			window.location.reload();
       	}
      });
}
function switchTo(id){
	$.ajax({
	 	url: ctx + '/manager/common/runas/switchTo/'+id, 
        type: 'post',
        dataType:"json",
        contentType : 'application/json',
        success: function (data) {
        	window.location.reload();
       	}
      });
}
function revoke(id){
	$.ajax({
	 	url: ctx + '/manager/common/runas/revoke/'+id, 
        type: 'post',
        dataType:"json",
        contentType : 'application/json',
        success: function (data) {
        	window.location.reload();
       	}
      });
}
function grant(id){
	$.ajax({
	 	url: ctx + '/manager/common/runas/grant/'+id, 
        type: 'post',
        dataType:"json",
        contentType : 'application/json',
        success: function (data) {
        	window.location.reload();
       	}
      });
}
function gain(id){
	$.ajax({
	 	url: ctx + '/manager/common/runas/gain/'+id,
        type: 'post',
        dataType:"json",
        contentType : 'application/json',
        success: function (data) {
        	window.location.reload();
       	}
      });
}
</script>
</body>
</html>