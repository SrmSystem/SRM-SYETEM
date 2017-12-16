<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
	<title>用户注册</title>
	<script src="${ctx}/static/base/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
</head>

<body>
	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/base.css" />
    <link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/layout.css"/>
	<div id="wrapper">
		<header id="header">

			<div id="headBox">
				<div class="w960 oh">
					<nav id="navs" class="fr">
						<a href="${ctx}">登录界面</a>
						<a class="active">找回密码</a>
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
							<h4>填写信息</h4>
						</div>
					</li>
					<li>
						<div class="step_inner">
							<span class="icon_step"></span>
							<h4></h4>
						</div>
					</li>
					<li  class="current">
						<div class="step_inner fr">
							<span class="icon_step">3</span>
							<h4>找回结果</h4>
						</div>
					</li>
				</ol>
				<div class="step_line"></div>
			</div>
			<div class="content">
				<div id="step1" class="step hide">
					
					<div style="font-size: 24px;font-weight: 900;text-align: center;">${successBoor }<br/><br/><k id="agreeb" style="color:#EC2020">5</k>秒后，自动跳转</div>
					<div class="toolBar">
					     <input id="nextBtn" class="btn btn_primary" type="button" value="立即跳转"/>&nbsp;	
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
		<script type="text/javascript">
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
    </script>
</body>
</html>
