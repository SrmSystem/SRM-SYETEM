<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
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
<style>
.top{
float: left;text-align: center;width: 100%;height: 80px;font-size:26px;font-weight:900;color: #079EA7;
}
.left{
float: left;width: 10%;height: 100%;font-size:26px;font-weight:900;
}
.center{
float: left;width: 80%;height: 100%;
}
.ctop{
   background-color: #fff;
    height: 33px;
    line-height: 33px;
    width: 100%;
    padding-left: 28px;
    color: #0E0E0E;
    font-weight: bold;
    margin-top: 10px;
    background: #CEEAFF;
}
.ctopImg{
   margin-top: 5px;
    margin-right: 15px;
    width: 27px;
    height: 24px;
    float: left;
}
.cccdiv{
    width: 100%;padding-left: 27px;
    border: 1px solid #D9EFED;
}
.right{
   float: left;width: 10%;height: 100%;font-size:26px;font-weight:900;
}
</style>
<form id="sumblicUserQuestionsFORM" method="post">

<div class="top">
<br/>
${questionnaire.title }<input type="hidden" name="quesId" value="${questionnaire.id }">
</div>
<div class="left">
&nbsp;
</div>
<div class="center" style="float: left;width: 80%;height: 100%;" >
	<c:forEach items="${subjectAnswer }" var="subjectAnswer">
		<div  class="ctop"><img class="ctopImg" src="/static/style/icons/IconsExtension/comment_edit.png">${subjectAnswer.key.title }</div>
	    <div class="cccdiv">
					<ol>
    					<c:forEach items="${subjectAnswer.value}" var="vele">
    						<c:if test="${vele.type==1 }">
			    				<li style="padding:10px"><input type="radio" name="anr${subjectAnswer.key.id }" value="${vele.id}"/>${vele.title}&nbsp;&nbsp;
			    				<c:if test="${bs==1 }">
			    				(${vele.choiceNumber}人)
			    				</c:if>
			    				</li>
			    			</c:if>
    						<c:if test="${vele.type==2 }">
			    				<li style="padding:10px"><input type="checkbox" name="anc${subjectAnswer.key.id }" value="${vele.id}"/>${vele.title}&nbsp;&nbsp;
			    				<c:if test="${bs==1 }">
			    				(${vele.choiceNumber}人)
			    				</c:if></li>
			    			</c:if>
    						<c:if test="${vele.type==3 }">
			    				<li style="padding:10px">
								<c:if test="${bs==1 }">
				    				共${vele.choiceNumber}人，回答问题<br/>
			<%-- 	    				<br/><br/><br/><c:forEach items="${vele.answerEntitys }" var="answerEntitys">
					    				<div style="width:100%;height: 50px;border: 1px solid #020202;">
					    					${answerEntitys.content }
					    				</div><br/>
				    				</c:forEach> --%>
			    				</c:if>
								<c:if test="${bs!=1 }">
								<input class="easyui-textbox" name="ant${subjectAnswer.key.id }" type="text" style="width:100%;height: 50px" data-options="required:true,height:40,multiline:true" value=""/>
			    				</c:if>
			    			</c:if>
			    			<c:if test="${bs==1 }">
			    				<script type="text/javascript">
				    					$(function(){
				    						$('#addersw').linkbutton('disable');
				    					});
				    				</script>
				    		</c:if>
		    			</c:forEach>
		    		</ol>
		</div>
	</c:forEach>
	<br/>
</div>
<div class="right">
&nbsp;
</div>
  <%--   <div data-options="region:'north',border:false" style="padding:20px;height:80px;font-size:26px;font-weight:900;  text-align: center;">
    	${questionnaire.title }<input type="hidden" name="quesId" value="${questionnaire.id }">
    </div>   
    <div data-options="region:'east',border:false" style="width:5%;"></div>   
    <div data-options="region:'west',border:false" style="width:5%"></div>   
    <div data-options="region:'center',border:false" style="padding:5px;">
    	<ol>
    		<c:forEach items="${subjectAnswer }" var="subjectAnswer">
    			<li style="padding:20px"><font style="font-weight: 900;">${subjectAnswer.key.title }</font><br/>
    				<ol>
    					<c:forEach items="${subjectAnswer.value}" var="vele">
    						<c:if test="${vele.type==1 }">
			    				<li style="padding:10px"><input type="radio" name="anr${subjectAnswer.key.id }" value="${vele.id}"/>${vele.title}&nbsp;&nbsp;
			    				<c:if test="${bs==1 }">
			    				(${vele.choiceNumber}人)
			    				</c:if>
			    				</li>
			    			</c:if>
    						<c:if test="${vele.type==2 }">
			    				<li style="padding:10px"><input type="checkbox" name="anc${subjectAnswer.key.id }" value="${vele.id}"/>${vele.title}&nbsp;&nbsp;
			    				<c:if test="${bs==1 }">
			    				(${vele.choiceNumber}人)
			    				</c:if></li>
			    			</c:if>
    						<c:if test="${vele.type==3 }">
			    				<li style="padding:10px">
								<c:if test="${bs==1 }">
				    				共${vele.choiceNumber}人，回答问题<br/><br/><br/><br/>
				    				<c:forEach items="${vele.answerEntitys }" var="answerEntitys">
					    				<div style="width:100%;height: 50px;border: 1px solid #020202;">
					    					${answerEntitys.content }
					    				</div><br/>
				    				</c:forEach>
			    				</c:if>
								<c:if test="${bs!=1 }">
								<input class="easyui-textbox" name="ant${subjectAnswer.key.id }" type="text" style="width:100%;height: 50px" data-options="required:true,height:40,multiline:true" value=""/>
			    				</c:if>
			    			</c:if>
			    			<c:if test="${bs==1 }">
			    				<script type="text/javascript">
				    					$(function(){
				    						$('#addersw').linkbutton('disable');
				    					});
				    				</script>
				    		</c:if>
		    			</c:forEach>
		    		</ol>
    			</li>
    		</c:forEach>
    	</ol>
    </div>  --%>
    </form>  
    
</body>
</html>