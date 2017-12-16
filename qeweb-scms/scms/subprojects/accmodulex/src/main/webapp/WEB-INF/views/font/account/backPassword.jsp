<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><spring:message code="vendor.register.backpassword.forgotpassword" /></title>
	<script type="text/javascript">
		  var ctx = '${pageContext.request.contextPath}';
    </script>
</head>

<body>
	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/base.css" />
    <link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/layout.css"/>
	<div id="wrapper">
		<header id="header">

			<div id="headBox">
				<div class="w960 oh">
					<nav id="navs" class="fr">
						<a href="${ctx}"><spring:message code="vendor.register.backpassword.login" /></a>
						<a class="active"><spring:message code="vendor.register.backpassword.forgotpassword" /></a>
					</nav>
				</div>
			</div>
		</header><!-- // header end -->
		<div class="container w960 mt20">
			<div id="processor" >
				<ol class="processorBox oh">
					<li  class="current">
						<div class="step_inner fl">
							<span class="icon_step">1</span>
							<h4><spring:message code="vendor.register.backpassword.info" /></h4>
						</div>
					</li>
					<li>
					    <div class="step_inner">
							<span class="icon_step"></span>
							<h4></h4>
						</div>
					</li>
					<li>
						<div class="step_inner fr">
							<span class="icon_step">2</span>
							<h4><spring:message code="vendor.register.backpassword.result" /></h4>
						</div>
					</li>
				</ol>
				<div class="step_line"></div>
			</div>
			<div class="content">
				<div id="step1" class="step hide" style="  margin-left: 20%;">
						<form id="inputForm" action="${ctx}/public/register" method="post">
						<k id="back"></k>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.register.backpassword.loginname" /></label>
							<div class="frm_controls">
								<input type="text" id="loginName" name="loginName" class="frm_input required" minlength="3"/>
								<p class="frm_tips"><spring:message code="vendor.register.backpassword.nametip" /></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.register.backpassword.email" /></label>
							<div class="frm_controls">
							    <input type="text" id="orgEmail" name="orgEmail" class="frm_input required email"/>
								<p class="frm_tips"><spring:message code="vendor.register.backpassword.emailtip" /></p>
							</div>
						</div>
						<div class="toolBar">
						    <%-- <input id="nextBtn" class="btn btn_primary" type="button" onclick="tijiao()" value='<spring:message code="button.next" />'/>&nbsp;
				            <input id="cancel_btn" class="btn" type="button" value='<spring:message code="button.back" />' onclick="history.back()"/>
				            --%>	
				            <button id="nextBtn" class="btn btn_primary" type="button" onclick="tijiao()"><spring:message code="button.next"/> </button>&nbsp;
				            <button id="cancel_btn" class="btn" type="button" onclick="history.back()"><spring:message code="button.back"/> </button>&nbsp;
						</div>
					</form>
				</div><!-- // step1 end -->
				<div id="step2" class="step hide">
				</div>
				<div id="step3" class="step hide">
				</div>
			</div>
		</div><!-- // container end -->
		<footer id="footer" class="w960 oh">
			<span class="fl">Copyright &copy; 2008-2017 <a href="http://www.aactechnologies.com">Aactechnologies.com</a></span>
		</footer><!-- // footer end -->
	</div><!-- // wrapper end -->
		<script>
		function tijiao(){
			$.post(ctx+"/public/register/backPasswordTIJ",{"loginName":$("#loginName").val(),"email":$("#orgEmail").val()},function(data){
				if(data==0)
				{
					window.location.href=ctx+"/public/register/successbackPass";
				}
				else if(data==1)
				{
					$("#back").html("<span for='loginName' class='error'<font style='color: #F00;'><spring:message code="vendor.register.backpassword.loginnameerrortip" /></font></span>");
				}
				else if(data==2)
				{
					$("#back").html("<span for='loginName' class='error'<font style='color: #F00;'><spring:message code="vendor.register.backpassword.emailerrortip" /></font></span>");
				}
			},"text");
		}
		
	</script>
</body>
</html>
