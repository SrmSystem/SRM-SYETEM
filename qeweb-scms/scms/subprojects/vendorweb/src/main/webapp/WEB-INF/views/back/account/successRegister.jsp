<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
	<title>用户注册</title>
	<script src="${ctx}/static/base/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/base.css" />
   	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/layout.css"/>
</head>

<body>
	<div id="wrapper">
		<header id="header">

			<div id="headBox">
				<div class="w960 oh">
					<nav id="navs" class="fr">
						<a href="${ctx}/logout">登录界面</a>
						<a class="active">注册界面</a>
					</nav>
				</div>
			</div>
		</header><!-- // header end -->
		<div class="container w960 mt20">
			<div id="processor" >
				<ol class="processorBox oh">
					<li>
						<div class="step_inner fl">
							<span class="icon_step">1</span>
							<h4>注册帐号信息</h4>
						</div>
					</li>
					<li>
						<div class="step_inner">
							<span class="icon_step">2</span>
							<h4>填写基本信息</h4>
						</div>
					</li>
					<li  class="current">
						<div class="step_inner fr">
							<span class="icon_step">3</span>
							<h4>注册结果</h4>
						</div>
					</li>
				</ol>
				<div class="step_line"></div>
			</div>
			<div class="content">
				<div id="step1" class="step hide">
					
					<div style="font-size: 24px;font-weight: 900;text-align: center;"><c:if test="${isInvite eq true}">注册成功，请登陆系统继续完善资料！</c:if><c:if test="${isInvite eq false}">注册成功，请等待确认</c:if><c:if test="${isInvite eq null}">注册成功，请等待确认</c:if><br/><!-- <k id="agreeb" style="color:#EC2020">3</k>秒后，自动跳转 --></div>
					<div class="toolBar">
					    <a class="btn btn_primary" href="${ctx}/logout">退出</a>
					</div>	
				</div><!-- // step1 end -->
				<div id="step2" class="step hide">
				</div>
				<div id="step3" class="step hide">
				</div>
			</div>
		</div><!-- // container end -->
		<footer id="footer" class="w960 oh">
			<span class="fl">Copyright © 2008-2015 <a href="http://www.qeweb.com" target="_bank">Qeweb.com Inc.</a></span>
		</footer><!-- // footer end -->
	</div><!-- // wrapper end -->
<!-- 		<script type="text/javascript">
		  var ctx = '${pageContext.request.contextPath}';
		  var secs = $("#agreeb").html();
		
		  for(i=1;i<=secs;i++) {
		   window.setTimeout("update(" + i + ")", i * 1000);
		  }
		  function update(num) {
		   if(num == secs) {
			   window.location.href=ctx+"/manager"; 
		   }
		  else {
		   printnr = secs-num;
		   $("#agreeb").html(printnr);
		   }
		  }
    </script> -->
</body>
</html>
