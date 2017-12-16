<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><!-- 用户注册 --><spring:message code="vendor.font.UserRegister"/></title>
	<link href="${ctx}/static/base/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
	<link rel="stylesheet" href="${ctx}/static/base/zTree_v3/css/metroStyle/metroStyle.css" type="text/css">
	<link href="${ctx}/static/styles/register.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
	
	<script>
		$(document).ready(function() {
			jQuery.validator.addMethod("isPhone", function(value,element) { 
				  var length = value.length; 
				  var mobile =/(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\(\d{3}\))|(\d{3}\-))?(1[358]\d{9})$)/;
				  return this.optional(element) || mobile.test(value); 
			}, "<font style='color: red;'><spring:message code="vendor.font.PleaseFillInYourContactNumberCorrectly"/></font>");/* 请正确填写您的联系电话 */
			//聚焦第一个输入框
			$("#loginName").focus();
			$("#buyerId").select();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					'loginName': {
						remote: "${ctx}/public/register/checkLoginName"
					}
					,'orgName' : {
						remote: "${ctx}/public/register/checkOrgName"
					}
					,'orgEmail' : {
						remote: "${ctx}/public/register/checkOrgEmail"
					},
					'password' : {
						rangelength:[6,12]
					},
					'orgPhone':{
						required: true,
						isPhone: true
					}					
				},
				messages: {
					'loginName': {
						remote: "<font style='color: #FF3765;'><spring:message code="vendor.font.TheUserLoginNameHasBeenRegistered"/> </font>"/* 用户登录名已注册 */
					}
					,'orgName' : {
						remote:"<font style='color: #FF3765;'><spring:message code="vendor.font.TheNameOfTheOrganizationHasBeenRegistered"/></font>"/* 该组织名称已注册 */
					}
					,'orgEmail' : {
						remote:"<font style='color: #FF3765;'><spring:message code="vendor.font.TheEmailHasBeenRegistered"/></font>"/* 该email已注册 */
					},
					'password' : {
						rangelength: jQuery.format("<font style='color: #F00;'><spring:message code="vendor.font.TheNumberOfBitsIsWrongFrom6to12Bits"/></font>")/* 密码位数不对（6到12位） */
					},
					'orgPhone':{
						required: "<font style='color: #F00;'><spring:message code="vendor.font.MandatoryField"/></font>"/* 必填字段 */,
						rangelength: jQuery.format("<font style='color: #F00;'><spring:message code="vendor.font.PleaseInputTheCorrectCellPhoneNumber"/></font>")/* 请输入正确的手机号 */
					}
						
				}
			});
		});
	</script>
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
						<a href="${ctx}/public/login"><spring:message code="vendor.font.LoginInterface"/><!-- 登录界面 --></a>
						<a class="active"><spring:message code="vendor.font.RegistrationInInterface"/><!-- 注册界面 --></a>
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
							<h4><spring:message code="vendor.font.RegisteredAccountInformation"/><!-- 注册帐号信息 --></h4>
						</div>
					</li>
					<li>
						<div class="step_inner">
							<span class="icon_step">2</span>
							<h4><spring:message code="vendor.font.FillInTheBasicInformation"/><!-- 填写基本信息 --></h4>
						</div>
					</li>
					<li>
						<div class="step_inner fr">
							<span class="icon_step">3</span>
							<h4><spring:message code="vendor.font.RegistrationResults"/><!-- 注册结果 --></h4>
						</div>
					</li>
				</ol>
				<div class="step_line"></div>
			</div>
			<div class="content">
				<div id="step1" class="step hide" style="  margin-left: 20%;">
						<form id="inputForm" action="${ctx}/public/register" method="post">
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.LoginName"/><!-- 登录名 --></label>
							<div class="frm_controls">
								<input type="text" id="loginName" name="loginName" class="frm_input required" minlength="3"/>
								<p class="frm_tips"><spring:message code="vendor.font.AsALoginAccountPleaseFillInTheNonRegisteredLoginName"/><!-- 作为登录帐号，请填写未被注册的登录名 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.Nickname"/><!-- 昵称 --></label>
							<div class="frm_controls">
							<input type="text" id="name" name="name" class="frm_input required"/>
								<p class="frm_tips"><spring:message code="vendor.font.Nickname"/><!-- 昵称 -->--<spring:message code="vendor.font.Required"/><!-- 必填项 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.PassWorld"/><!-- 密码 --></label>
							<div class="frm_controls">
							    <input type="password" id="password" name="password" class="frm_input passwd required"/>
								<p class="frm_tips"><spring:message code="vendor.font.LettersNumbersOrEnglishSymbolsFrom6to12BitsCaseSensitive"/><!-- 字母、数字或者英文符号，6到12位，区分大小写 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.EnterPasswordAgain"/><!-- 再次输入密码 --></label>
							<div class="frm_controls">
							     <input type="password" id="confirmPassword" name="confirmPassword" class="frm_input passwd2 required" equalTo="#password"/>
							     <p class="frm_tips"><spring:message code="vendor.font.UsedToAllowUsersToReconfirmPasswords"/><!-- 用于让用户再次确认密码 -->------<spring:message code="vendor.font.Required"/><!-- 必填项 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.EnterpriseName"/><!-- 企业全称 --></label>
							<div class="frm_controls">
							    <input type="text" id="orgName" name="orgName" class="frm_input required"/>
								<p class="frm_tips"><spring:message code="vendor.font.NameOfBusinessRegistration"/><!-- 工商注册的名称 -->--<spring:message code="vendor.font.Required"/><!-- 必填项 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.ContactNumber"/><!-- 联系电话 --></label>
							<div class="frm_controls">
							    <input type="text" id="orgPhone" name="orgPhone" class="frm_input required"/>
								<p class="frm_tips">--<spring:message code="vendor.font.FormatSuchAs"/><!-- 格式如 -->：13888888888，0512-88888888 </p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">Email</label>
							<div class="frm_controls">
							    <input type="text" id="orgEmail" name="orgEmail" class="frm_input required email"/>
								<p class="frm_tips">--<spring:message code="vendor.font.Required"/><!-- 必填项 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.PurchasingOrganization"/><!-- 采购组织 --></label>
							<div class="frm_controls">
							    <select id="buyerId" name="buyerId" class="frm_input required">
							    	<c:forEach items="${buyerList }" var="buyer">
								    	<option value="${buyer.id }">${buyer.name }</option>
							    	</c:forEach>
							    </select>
								<p class="frm_tips">--<spring:message code="vendor.font.Required"/><!-- 必填项 --></p>
							</div>
						</div>
						<div class="toolBar">
						    <input id="nextBtn" class="btn btn_primary" readonly="readonly"  onclick="saveSub()" value="下一步"/>&nbsp;	
				            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
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
		function saveSub(){
			var loginName = document.getElementById('loginName').value;
			if(null==loginName || loginName==""){
				alert("<spring:message code="vendor.font.LoginNameCannotBeEmpty"/>");/* 登录名不能为空 */
				return false;
			}
			var name = document.getElementById('name').value;
			if(null==name || name==""){
				alert("<spring:message code="vendor.font.NicknamesCannotBeEmpty"/>");/* 昵称不能为空 */
				return false;
			}
			var password = document.getElementById('password').value;
			if(null==password || password==""){
				alert("<spring:message code="vendor.font.PasswordCannotBeEmpty"/>");/* 密码不能为空 */
				return false;
			}
			var confirmPassword = document.getElementById('confirmPassword').value;
			if(null==confirmPassword || confirmPassword==""){
				alert("<spring:message code="vendor.font.ThePasswordCannotBeEmptyAgain"/>");/* 再次输入密码不能为空 */
				return false;
			}
		    var orgName= document.getElementById('orgName').value;	
		    if(null==orgName || orgName==""){
				alert("<spring:message code="vendor.font.TheFullNameOfTheEnterpriseCanNotBeEmpty"/>");/* 企业全称不能为空 */
				return false;
			}
		    var orgPhone = document.getElementById('orgPhone').value;
		    if(null==orgPhone || orgPhone==""){
				alert("<spring:message code="vendor.font.TheTelephoneCanNotBeEmpty"/>");/* 联系电话不能为空 */
				return false;
			}
		    var orgEmail = document.getElementById('orgEmail').value;
		    if(null==orgEmail || orgEmail==""){
				alert("<spring:message code="vendor.font.EmailCanNotBeEmpty"/>");/* Email不能为空 */
				return false;
			}
			var buyerId = document.getElementById('buyerId').value;
			if(null==buyerId || buyerId==""){
				alert("<spring:message code="vendor.font.PurchasingOrganizationCanNotBeEmpty"/>")/* 采购组织不能为空 */;
				return false;
			}
			$('#inputForm').submit();
		}
</script>
</body>
</html>
