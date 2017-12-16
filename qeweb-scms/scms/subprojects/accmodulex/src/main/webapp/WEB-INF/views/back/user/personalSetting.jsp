<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<head>
	<title><spring:message code="sys.person.message"/></title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	  var ctx1  =<shiro:principal property="orgRoleType" />;
	</script>
	
</head>
<body>
<link href="${ctx}/static/style/user/css/style.css" rel="stylesheet" type="text/css" />
<div id="p" class="easyui-panel"    
        style="background:#fafafa;"   
        data-options="iconCls:'icon-save'">   
    <table width="100%" class="table" style="padding:15px;border-bottom:0px solid #ddd;">
		<tr>
			<td style="font-weight: 900;width: 15%"><spring:message code="sys.user.account"/>：</td>
			<td>${userEntity.loginName }</td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td style="font-weight: 900;"><spring:message code="sys.user.name"/>：</td>
			<td><div id="d1">${userEntity.name }</div><input type="text" id="s1" name="name" value="${userEntity.name }"></td>
			<td style="font-weight: 900;width: 15%"><spring:message code="sys.user.permission"/>：</td>
			<td>${userEntity.roles }</td>
		</tr>
		<tr>
			<td style="font-weight: 900;"><spring:message code="sys.cellPhone.number"/>：</td>
			<td><div id="d2">${userEntity.mobile }</div><input type="text" id="s2" name="mobile" value="${userEntity.mobile }"></td>
			<td style="font-weight: 900;"><spring:message code="sys.user.email"/>：</td>
			<td><div id="d3">${userEntity.email }</div><input type="text" id="s3" name="email" value="${userEntity.email }"></td>
		</tr>
		<tr>
			<td style="font-weight: 900;"><spring:message code="sys.user.department"/>：</td>
			<td>${userEntity.department.name }</td>
			<td style="font-weight: 900;"><spring:message code="sys.user.company"/>：</td>
			<td>${userEntity.company.name }</td>
		</tr>
		<tr>
			<td style="font-weight: 900;"><spring:message code="sys.registration.time"/>：</td>
			<td><fmt:formatDate value="${userEntity.registerDate}" pattern="yyyy-MM-dd"/></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
		<td></td>
		<td></td>
	   <td id="upass" class='div_submit_button'>
			<a  onclick="updatpass()"class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >
			<spring:message code="sys.change.password"/>
			</a>
		</td>
		<td id="bu1" class='div_submit_button'>
			<a  onclick="update()"class="easyui-linkbutton" data-options="iconCls:'icon-add'" >
			<spring:message code="sys.change.save"/>
			</a>
		</td>


		<td id="bu2" class='div_submit_button'>
			<a  onclick="noupdate()"class="easyui-linkbutton" data-options="iconCls:'icon-undo'" >
			<spring:message code="sys.change.cancel"/>
			</a>
		</td>
		

		
		<td  id="bu0">
			<a onclick="updateStart()" class="easyui-linkbutton"  data-options="iconCls:'icon-reload',width:'180'" >
			<spring:message code="sys.change.information"/>
			</a>
		</td>

		</tr>
	</table> 
</div>  

<div id="win" class="easyui-window" title="<spring:message code="sys.change.password"/>" style="width:65%;height:350px"   
        data-options="iconCls:'icon-save',modal:true,closed:true">   
<div class='body_main'> 
	<div class='box_main'>
		<div id="register" class="register">
				<div id="form_submit" class="form_submit">
					<div class="fieldset">
						<div class="field-group">
							<label class="required title"><spring:message code="sys.old.password"/></label>
							<span class="control-group" id="mobile_input">
								<div class="input_add_long_background">
									<input class="register_input" type="password" id="passwords" name="passwords"  value="">
								</div>
							</span>
							<label id="t1" class="tips"><spring:message code="sys.old.password.required"/></label>
						</div>
						
						<div class="field-group">
							<label class="required title"><spring:message code="sys.new.password"/></label>
							<span class="control-group" id="email_input">
								<div class="input_add_long_background">
									<input class="register_input" type="password" id="newpassword" name="newpassword" value="">
								</div>
							</span>
							<label id="t2" class="tips"><spring:message code="sys.new.password.required"/></label>
						</div>

						
						<div class="field-group">
							<label class="required title"><spring:message code="sys.confirm.new.password"/></label>
							<span class="control-group" id="password1_input">
								<div class="input_add_long_background">
									<input class="register_input" type="password" id="newpassword1" name="newpassword1"  value=""/>
								</div>
							</span>
							<label id="t3" class="tips"><spring:message code="sys.same.with.password"/></label>
						</div>

						
					</div>
					
				</div>
				
				<div id="div_submit" class="div_submit">
					<div class='div_submit_button'>
						<a id="btnup" onclick="updatePassword()"class="easyui-linkbutton" data-options="iconCls:'icon-add'" >
							<spring:message code="sys.user.confirm"/>
						</a>
					</div>
				</div>
		</div>
	 
	</div>
	

</div>
</div>  
<script type="text/javascript">
$(function(){
	
	if(ctx1 == "0" && ctx2 !="admin"){
		document.getElementById('upass').innerHTML='';
	}
	
	
	$('#win').window({    
	    modal:true   
	});  
	$('#win').window('close');
	$("#s1").hide();
	$("#s2").hide();
	$("#s3").hide();
	$("#bu1").hide();
	$("#bu2").hide();
})
function updatpass()
{
	$('#win').window('open'); 
	$.parser.parse($('#btnup').parent());
}
function update(){
	var s1=$("#s1").val();
	var s2=$("#s2").val();
	var s3=$("#s3").val();
$.post(ctx+"/manager/vendor/personal/updatePersonal",{"name":s1,"mobile":s2,"email":s3},function(data){
	if(data=="1")
	{
		$("#s1").hide();
		$("#s2").hide();
		$("#s3").hide();
		$("#bu1").hide();
		$("#bu2").hide();
		$("#upass").show();
		$("#bu0").show();
		$("#d1").html(s1);
		$("#d2").html(s2);
		$("#d3").html(s3);
		$("#d1").show();
		$("#d2").show();
		$("#d3").show();
		//$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message4'),'error');
		$.messager.alert($.i18n.prop('label.change.remind'),$.i18n.prop('label.change.success'),'info');
	}
	else
	{
		$.messager.alert($.i18n.prop('label.change.remind'),$.i18n.prop('label.change.field'),'error');
	}
	
},"text");
}
function updateStart(){
	$("#s1").show();
	$("#s2").show();
	$("#s3").show();
	$("#bu1").show();
	$("#bu2").show();
	$("#upass").hide();
	$("#bu0").hide();
	$("#d1").hide();
	$("#d2").hide();
	$("#d3").hide();
}
function noupdate(){
	$("#s1").hide();
	$("#s2").hide();
	$("#s3").hide();
	$("#bu1").hide();
	$("#bu2").hide();
	$("#upass").show();
	$("#bu0").show();
	$("#d1").show();
	$("#d2").show();
	$("#d3").show();
}
function updatePassword(){
	var passwords=$("#passwords").val();
	var newpassword=$("#newpassword").val();
	var newpassword1=$("#newpassword1").val();
	var s1="0";
	var s2="0";
	var s3="0";
	var s4="0";
	if(null==passwords||passwords=="")
	{
		s1=1;
	}
	if(null==newpassword||newpassword=="")
	{
		s2=1;
	}
	if(null==newpassword1||newpassword1=="")
	{
		s3=1;
	}
	if(newpassword!=newpassword1)
	{
		s4=1;
	}
    if(s1==1||s2==1||s3==1)
	{
		
		if(s1==1)
		{
			//$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('order.message4'),'error');
			$("#t1").html("<font color='#F50B0B'>"+$.i18n.prop('label.old.password.required')+"</font>");
		}
		if(s2==1)
		{
			$("#t2").html("<font color='#F50B0B'>"+$.i18n.prop('label.new.password.required')+"</font>");
		}
		if(s3==1)
		{
			$("#t3").html("<font color='#F50B0B'>"+$.i18n.prop('label.confirm.new.password')+"</font>");
		}
		if(s4==1)
		{
			$("#t3").html("<font color='#F50B0B'>"+$.i18n.prop('label.different.password')+"</font>");
		}
	}
    else
    {
    	$("#t1").html("");
    	$("#t2").html("");
    	$("#t3").html("");
    	$.post(ctx+"/manager/vendor/personal/updatePassword",{"passwords":passwords,"newpassword":newpassword,"newpassword1":newpassword1},function(data){
    		if(data=="1")
    		{
    			
    			$('#win').window('close');
    			//$.messager.alert('修改密码提示','修改密码成功','info');
    			$.messager.alert($.i18n.prop('label.change.remind'),$.i18n.prop('label.change.success'),'info');
    		}
    		else if(data=="0")
    		{
    			$("#t1").html("<font color='#F50B0B'>"+$.i18n.prop('label.old.password.error')+"</font>");
    		}
    		else if(data=="4")
    		{
    			$("#t2").html("<font color='#F50B0B'>"+'<spring:message code="vendor.user.8bits"/>'+"</font>");/* 密码必须包含字母数字符号且长度大于等于8位 */
    		}
    		else
    		{
    			$('#win').window('close');
    			//$.messager.alert('修改密码提示','修改密码失败','error');
    			$.messager.alert($.i18n.prop('label.change.remind'),$.i18n.prop('label.change.field'),'error');
    		}
    	},"text");
    }
	
}
</script>
</body>

</html>