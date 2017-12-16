<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>邀请注册</title>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-vendorInviteReg-list" title="邀请注册" class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/vendor/inviteReg',method:'post',singleSelect:false,
		toolbar:'#toolbar_inviteReg',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',hidden:true"></th>
		<th data-options="field:'vendorName'">企业全称</th>
		<th data-options="field:'buyerCode',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.code}">采购组织编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.name}">采购组织名称</th>
		<th data-options="field:'vendorEmail'">联系邮箱</th>
		<th data-options="field:'inviteName'">邀请人</th>
		<th data-options="field:'createTime'">邀请时间</th>
		<th data-options="field:'expiryDate'">失效时间</th>
		<th data-options="field:'isCheck',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',false);}">是否点击</th>
		<th data-options="field:'isRegister',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',false);}">是否注册</th>
		</tr></thead>
	</table>
	<div id="toolbar_inviteReg" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="invite()">邀请注册</a>
		</div>
		<div>
			<form id="form-vendorInviteReg-search" method="post">
			企业全称：<input type="text" name="search-LIKE_vendorName" class="easyui-textbox" style="width:80px;"/>
			Email：<input type="text" name="search-LIKE_vendorEmail" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendorInvite()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorInviteReg-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-vendorInviteReg-addoredit" class="easyui-dialog" title="邀请注册" style="width:400px;height:230px"
	data-options="iconCls:'icon-add',closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-vendorInviteReg-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>企业全称:</td><td><input class="easyui-textbox" id="vendorName" name="vendorName" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
				<td>采购组织:</td>
				<td>
					<input class="easyui-combobox" name="orgId" 
						data-options="
						url:'${ctx}/manager/vendor/vendorNav/getOrgCombo',
						valueField : 'value',
						textField : 'text',
						editable:false,
						required:true"
					/>
				</td>
				</tr>
				<tr>
					<td>供应商联系邮箱:</td>
					<td>
					<input class="easyui-validatebox" data-options="required:true,validType:'email'"  name="vendorEmail" />
					</td>
				</tr>
				<tr>
					<td>邀请失效日期:</td><td><input class="easyui-datetimebox" name="expiryDate1" data-options="required:true,editable:false"
					/></td>
				</tr>
				<tr>
					<td>邀请人:</td><td><input class="easyui-textbox" id="inviName" name="inviteName" type="text"
					/></td>
				</tr>
				
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitForm(1)">邀请</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitForm()">关闭</a>
				</div>
			</form>
		</div>
	</div>


<div id="dialog-baseInfo" class="easyui-dialog" data-options="closed:true,title:'供应商注册信息'">
<div itemId="ct-max"></div>
</div>
<script type="text/javascript">
var username;
function searchVendorInvite(){
	var searchParamArray = $('#form-vendorInviteReg-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-vendorInviteReg-list').datagrid('load', searchParams);
}

function invite() {
	$('#win-vendorInviteReg-addoredit').window({
		iconCls : 'icon-add',
		title : '邀请注册'
	});
	$('#form-vendorInviteReg-addoredit').form('clear');
	$('#id').val(0);
	$('#inviName').textbox("setValue",username);
	$('#inviName').textbox("readonly",true);
	$('#win-vendorInviteReg-addoredit').window('open');
}
function submitForm(flag){
	if(flag == '1'){
		$.messager.confirm('提示','确定发送邀请吗？',function(r){
			if(r){
				submitAddorEditvendorInviteReg();
			}
		});
	}else{
		$('#win-vendorInviteReg-addoredit').window('close');
	}
}
function submitAddorEditvendorInviteReg(){
	var url = '${ctx}/manager/vendor/inviteReg/addInviteReg';
	var sucMeg = '邀请注册成功！';
	$.messager.progress();
	$('#form-vendorInviteReg-addoredit').form('submit', {
		ajax : true,
		url : url,
		onSubmit : function() {
			var isValid = $(this).form('validate');
			if (!isValid) {
				$.messager.progress('close');
			}
			return isValid;
		},
		success : function(data) {
			$.messager.progress('close');
			try {
				var result = eval('(' + data + ')');
				if (result.success) {
					$.messager.alert('提示', sucMeg, 'info');
					//$('#win-vendorInviteReg-addoredit').window('close');
					$('#datagrid-vendorInviteReg-list').datagrid('reload');
					$('#win-vendorInviteReg-addoredit').window('close');
				} else {
					$.messager.alert('提示', result.msg, 'error');
				}
			} catch (e) {
				$.messager.alert('提示', data, 'error');
			}
		}

	});
}

$(function(){
	
	$.ajax({
		url : '${ctx}/manager/vendor/inviteReg/getUsername',
		type : 'POST',
		contentType : 'application/json',
		success : function(data) {
			username = data;
		}
	});
});
</script>
</body>
</html>
