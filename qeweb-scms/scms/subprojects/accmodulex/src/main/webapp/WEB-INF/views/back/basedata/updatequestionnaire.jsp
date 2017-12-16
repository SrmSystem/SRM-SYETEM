<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><!-- 添加问卷 --><spring:message code="purchase.basedata.AddQuestionnaire"/></title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
</head>

<body class="archive category category-photography category-15">
<link rel="stylesheet" href="${ctx}/static/style/addquestionnaire.css" type="text/css" media="screen" />
<script src="${ctx}/static/questionnaire/questionnaire.js" type="text/javascript"></script>

	<div id="wrapper" class="clearfix" style="overflow: auto;">
	<form id="formsd" action="${ctx}/manager/Questionnaire/updatequestionnairePost" method="post">
		<input id="zongTitleid" name="zongTitleid" type="hidden" value="${questionnaire.id }" >
			<input id="zongTitle" name="zongTitle"
				style="width: 100%; height: 60%; text-align: center; border: 0px; font-size: 24px; font-weight: 900"
				value="${questionnaire.title }" placeholder='<!-- 问卷标题 --><spring:message code="purchase.basedata.QuestionnaireTitle"/>' onfocus="changt('${questionnaire.id }')"/>
		<!-- top bar -->
		<input type="hidden" id="cxlaNumber" name="cxlaNumber" value="1" >
		<div id="main" class="clearfix">
			<div id="content" class="filter-posts">
			<c:forEach items="${subjects}" var="subjects">
				<div data-id="post-81" data-type="photography"
					class="post-81 photography post clearfix project">
					<div class="box">
						<div class="shadow clearfix">
							<div class="frame">
								<h2 class="entry-title">
									<input id="subjects${subjects.id }" name="subjects${subjects.id }"
										style="width: 100%; height: 60%; border: 0px; font-size: 16px; font-weight: 900"
										value="${subjects.title }" placeholder='<!-- 问卷题目 --><spring:message code="purchase.basedata.QuestionnaireTitle"/>' onfocus="changs('${subjects.id }')" />
								</h2>

								<div class="okvideo"></div>
								<ul id="awsc1">
								<c:forEach items="${subjects.answers}" var="answers">
									<c:if test="${answers.type==1}">
										<li><input type="radio" style="margin-right: 10px"><input  id="answer${answers.id }" name="answer${answers.id }" value="${answers.title }" placeholder='<spring:message code="purchase.basedata.QuestionnaireOptions"/><!-- 问卷选项 -->' onfocus="changa('${answers.id }')"/></li>
									</c:if>
									<c:if test="${answers.type==2}">
										<li><input type="checkbox" style="margin-right: 10px"><input  id="answer${answers.id }"  name="answer${answers.id }"   value="${answers.title }" placeholder='<spring:message code="purchase.basedata.QuestionnaireOptions"/><!-- 问卷选项 -->' onfocus="changa('${answers.id }')"/></li>'
									</c:if>
									<c:if test="${answers.type==3}">
										<textarea name="answer${answers.id }" rows="5" cols="30"  onfocus="changa('${answers.id }')"></textarea>
									</c:if>
								</c:forEach>
								</ul>
							</div>
							<!-- frame -->
						</div>
						<!-- shadow -->
					</div>
					<!-- box -->
				</div>
				<!--writing post-->
			</c:forEach>
				
			</div>
			<input type="hidden" id="changT" name="changT" value="">
			<input type="hidden" id="changS" name="changS" value="">
			<input type="hidden" id="changA" name="changA" value="">
			<a  id="addtssg1" onclick="sumits()" style="width: 90%;height: 20%;margin-left: 50px" class="easyui-linkbutton" data-options="iconCls:'icon-add'"><spring:message code="purchase.basedata.SaveQuestionnaire"/><!-- 保存问卷 --></a>
		</div>
		
       </form>
	</div>
<script type="text/javascript" src="${ctx}/static/script/basedata/updatequestionnaire.js"></script>
</body>
</html>