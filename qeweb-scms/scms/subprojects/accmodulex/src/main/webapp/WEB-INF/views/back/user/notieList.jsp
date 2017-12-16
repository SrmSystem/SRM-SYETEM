<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<title><spring:message code="vendor.user.announcementManagement"/><!-- 公告管理 --></title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/notie.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorInfor.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/notice/getNoticeList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="50px" data-options="field:'id',checkbox:true">ID</th>
        <th width="50px" data-options="field:'not1',formatter:notieJS.updatess"><spring:message code="vendor.operation"/><!-- 操作 --></th>
        <th width="150px" data-options="field:'title'"><spring:message code="vendor.user.title"/><!-- 题目 --></th>
        <th width="150px" data-options="field:'createUserName'"><spring:message code="vendor.releasePeople"/><!-- 发布人 --></th>
        <th width="110px" data-options="field:'commentPower',formatter:function(v,r){if(r.commentPower==0) return '允许'; else return '不允许'}"><spring:message code="vendor.user.areAllowedComment"/><!-- 是否允许评论 --></th>
        <th width="80px" data-options="field:'noticeType',formatter:function(v,r){if(r.noticeType==1) return '置顶'; else return '不置顶'}"><spring:message code="vendor.user.IsPlacedTop"/><!-- 是否置顶 --></th>
        <th width="80px" data-options="field:'lookNumber',formatter:notieJS.getlookNumber"><spring:message code="vendor.user.viewed"/><!-- 浏览次数 --></th>
        <th width="80px" data-options="field:'spkeaNumber',formatter:notieJS.getspkeaNumber"><spring:message code="vendor.user.commentNumber"/><!-- 评论次数 --></th>
        <th width="130px" data-options="field:'validStartTime'"><spring:message code="vendor.user.effectiveStartDate"/><!-- 有效起始日期 --></th>
        <th width="130px" data-options="field:'validEndTime'"><spring:message code="vendor.user.effectiveEndDate"/><!-- 有效结束日期 --></th>
        <th width="130px" data-options="field:'createTime'"><spring:message code="vendor.user.dateCreation"/><!-- 创建日期 --></th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="notieJS.add()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="notieJS.del()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
      <form id="form" method="post">
                             <spring:message code="vendor.user.title"/><!-- 题目 -->:<input class="easyui-textbox" name="search-LIKE_title" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="notieJS.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
        <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>      
      </form>
      <form id="form-materialSupplyRel-export" method="post">
	  </form>
    </div>
  </div>
<div id="dd" class="easyui-dialog" title="添加公告" style="width:100%;height:100%"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true,draggable:false">   
        
</div> 
<div id="udd" class="easyui-dialog" title="更新公告" style="width:100%;height:100%"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true,draggable:false">   
        
</div> 

<div id="kkBuyer" class="easyui-dialog" title="添加浏览权限---采购员" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridssBuyer" class="easyui-datagrid" data-options="
    fit:true,method:'post',url:'${ctx}/manager/admin/user/getNoteUserList',singleSelect:false,toolbar:'#orgBuyerListToolbar',
	pagination:true,rownumbers:true,pageSize:300,pageList:[300,350,400],
  ">
    <thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="130px" data-options="field:'loginName'"><spring:message code="vendor.coding"/><!-- 编码 --></th>
		<th width="100px" data-options="field:'name'"><spring:message code="vendor.appellation"/><!-- 名称 --></th>
		<th width="130px" data-options="field:'registerDate'"><spring:message code="vendor.RegistrationTime"/><!-- 注册时间 --></th>
		<th width="100px" data-options="field:'company._orgType'"><spring:message code="vendor.organizational"/><!-- 组织级别 --></th>
		<th width="100px" data-options="field:'company._roleType'"><spring:message code="vendor.tissueTypes"/><!-- 组织类型 --></th>
		</tr></thead>
  </table>
  <div id="orgBuyerListToolbar">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzheBuyer()"><spring:message code="vendor.chooseBack"/><!-- 选择带回 --></a> 
      <div>
			<form id="formBuyer2" method="post">
			<spring:message code="vendor.coding"/><!-- 编码 -->：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/><!-- 名称 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addBuyerSearch()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			</form>
		</div>   
    </div>
  </div>   
</div>

<div id="kk" class="easyui-dialog" title="添加浏览权限--供应商" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,toolbar:'#orgListToolbar',
	pagination:true,rownumbers:true,pageSize:300,pageList:[300,350,400],
  ">
    <thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="130px" data-options="field:'code'"><spring:message code="vendor.coding"/><!-- 编码 --></th>
		<th width="100px" data-options="field:'name'"><spring:message code="vendor.appellation"/><!-- 名称 --></th>
		<th width="130px" data-options="field:'registerTime'"><spring:message code="vendor.RegistrationTime"/><!-- 注册时间 --></th>
		<th width="100px" data-options="field:'_orgType'"><spring:message code="vendor.organizational"/><!-- 组织级别 --></th>
		<th width="100px" data-options="field:'_roleType'"><spring:message code="vendor.tissueTypes"/><!-- 组织类型 --></th>
		</tr></thead>
  </table>
  <div id="orgListToolbar">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()"><spring:message code="vendor.chooseBack"/><!-- 选择带回 --></a> 
      <div>
			<form id="form2" method="post">
			<spring:message code="vendor.coding"/><!-- 编码 -->：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/><!-- 名称 -->：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
			
			<input type="hidden" name="search_EQ_roleType" value="1"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			</form>
		</div>   
    </div>
  </div>   
</div>
<div id="kk222" class="easyui-dialog" title="添加浏览权限---角色" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss222" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,toolbar:'#orgListToolbar222',
	pagination:true,rownumbers:true,pageSize:300,pageList:[300,350,400],
  ">
    <thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="130px" data-options="field:'code',width:100,formatter:function(v,r,i){if(r.roleType==1) 
			return '<font color=red>'+v+'</font>'; else return v;}"><spring:message code="vendor.characterEncoding"/><!-- 角色编码 --></th>
		<th width="130px" data-options="field:'name',width:100"><spring:message code="vendor.roleName"/><!-- 角色名 --></th>
		<th width="130px" data-options="field:'remark',width:100"><spring:message code="vendor.remark"/><!-- 备注 --></th>
		<th width="130px"  data-options="field:'roleType',hidden:true"><spring:message code="vendor.roleType"/><!-- 角色类型 --></th>
		</tr></thead>
  </table>
  <div id="orgListToolbar222">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe222()"><spring:message code="vendor.chooseBack"/><!-- 选择带回 --></a> 
      <div>
			<form id="form222" method="post">
			<spring:message code="vendor.characterEncoding"/><!-- 角色编码 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.roleName"/><!-- 角色名 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch222()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			</form>
		</div>   
    </div>
  </div>   
</div>  
<div id="kk3" class="easyui-dialog" title="浏览人员" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss3" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#ttt3'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"><spring:message code="vendor.user.browsingRecordID"/><!-- 浏览记录ID --></th>
        <th data-options="field:'createUserName'"><spring:message code="vendor.user.browseName"/><!-- 浏览人名称 --></th>
        <th data-options="field:'createTime'"><spring:message code="vendor.user.browsingTime"/><!-- 浏览时间 --></th>
      </tr>
    </thead>
  </table>
  <div id="ttt3">
    <div>
      <form id="form3" method="post">
                             <spring:message code="vendor.user.browseName"/><!-- 浏览人名称 -->:<input class="easyui-textbox" name="search-LIKE_createUserName" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch3()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>      
      </form>
    </div>
  </div>
</div> 
<div id="kk4" class="easyui-dialog" title="查看评论" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss4" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#ttt4'
  ">
    <thead>
      <tr>
        <th width="30px"  data-options="field:'id',checkbox:true"><spring:message code="vendor.user.commentRecordID"/><!-- 评论记录ID --></th>
        <th width="130px"  data-options="field:'createUserName'"><spring:message code="vendor.user.commentSupplierName"/><!-- 评论供应商名称 --></th>
        <th width="130px"  data-options="field:'content'"><spring:message code="vendor.user.commentContent"/><!-- 评论内容 --></th>
        <th width="130px"  data-options="field:'createTime'"><spring:message code="vendor.user.commentSupplierTime"/><!-- 评论供应商时间 --></th>
      </tr>
    </thead>
  </table>
  <div id="ttt4">
    <div>
      <form id="form3" method="post">
                             <spring:message code="vendor.user.browseName"/><!-- 浏览人名称 -->:<input class="easyui-textbox" name="search-LIKE_createUserName" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch3()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>      
      </form>
    </div>
  </div>
</div> 
<script type="text/javascript">
function addBuyerSearch(){
	var searchParamArray = $('#formBuyer2').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagridssBuyer').datagrid("load",searchParams);
}



function addsearch() {
	var searchParamArray = $('#form2').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagridss').datagrid("load",searchParams);
}
function addsearch222() {
	var searchParamArray = $('#form222').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagridss222').datagrid("load",searchParams);
}
function addsearch3() {
	var searchParamArray = $('#form3').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagridss3').datagrid("load",searchParams);
}

function xuanzheBuyer(){
	var addids=$("#addnotiess").contents().find("#addids").val();
	var su="";
	var sn="";
	var selections = $('#datagridssBuyer').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.user.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(var i=0;i<selections.length;i++){
		if(addids.indexOf(selections[i]["id"])!=-1){
			//$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.user.alreadySelectedRecords"/>'/* 已经有选中的记录 */,'info');
			//return false;
			continue;
		}
		if(i!=0)
		{
			su=su+","+selections[i]["id"];
		}
		else
		{
			if(addids!='')
			{
				su=su+","+selections[i]["id"];
			}
			else
			{
				su=selections[i]["id"];
			}
		}
		
		sn=sn+'<span id="span'+selections[i]["id"]+'" class="tag"><span>'+selections[i]["name"]+'</span><button onclick="window.parent.adddelecturls(\''+selections[i]["id"]+'\')">x</button></span>';
	}
	$("#addnotiess").contents().find("#addids").val(addids+su);
	var lookpower='';
	if(addids=='')
	{
		lookpower='';
	}
	else
	{
		var lookpower=$("#addnotiess").contents().find("#lookpower").html();
	}
	$("#addnotiess").contents().find("#lookpower").html(lookpower+sn);
	$('#kkBuyer').window('close');
}

function xuanzhe()
{
	var addids=$("#addnotiess").contents().find("#addids").val();
	var su="";
	var sn="";
	var selections = $('#datagridss').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.user.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(var i=0;i<selections.length;i++){
		if(addids.indexOf(selections[i]["id"])!=-1){
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.user.alreadySelectedRecords"/>'/* 已经有选中的记录 */,'info');
			return false;
		}
		if(i!=0)
		{
			su=su+","+selections[i]["id"];
		}
		else
		{
			if(addids!='')
			{
				su=su+","+selections[i]["id"];
			}
			else
			{
				su=selections[i]["id"];
			}
		}
		
		sn=sn+'<span id="span'+selections[i]["id"]+'" class="tag"><span>'+selections[i]["name"]+'</span><button onclick="window.parent.adddelecturls(\''+selections[i]["id"]+'\')">x</button></span>';
	}
	$("#addnotiess").contents().find("#addids").val(addids+su);
	var lookpower='';
	if(addids=='')
	{
		lookpower='';
	}
	else
	{
		var lookpower=$("#addnotiess").contents().find("#lookpower").html();
	}
	$("#addnotiess").contents().find("#lookpower").html(lookpower+sn);
	$('#kk').window('close');
}
function xuanzhe222()
{
	var addids=$("#addnotiess").contents().find("#addids").val();
	var su="";
	var sn="";
	var selections = $('#datagridss222').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.user.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(var i=0;i<selections.length;i++){
		if(addids.indexOf(selections[i]["id"])!=-1){
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.user.alreadySelectedRecords"/>'/* 已经有选中的记录 */,'info');
			return false;
		}
		if(i!=0)
		{
			su=su+","+selections[i]["id"];
		}
		else
		{
			if(addids!='')
			{
				su=su+","+selections[i]["id"];
			}
			else
			{
				su=selections[i]["id"];
			}
		}
		
		sn=sn+'<span id="span'+selections[i]["id"]+'" class="tag"><span>'+selections[i]["name"]+'</span><button onclick="window.parent.adddelecturls('+selections[i]["id"]+')">x</button></span>';
	}
	$("#addnotiess").contents().find("#addids").val(addids+su);
	var lookpower='';
	if(addids=='')
	{
		lookpower='';
	}
	else
	{
		var lookpower=$("#addnotiess").contents().find("#lookpower").html();
	}
	$("#addnotiess").contents().find("#lookpower").html(lookpower+sn);
	$('#kk222').window('close');
}
function close()
{
	$('#dd').window('close');
}
function close2()
{
	$('#udd').window('close');
}
function info()
{
	$.messager.alert('<spring:message code="vendor.addTip"/>'/* 添加提示 */,'<spring:message code="vendor.operationSuccessful"/>'/* 操作成功 */,'info');
}
function error()
{
	$.messager.alert('<spring:message code="vendor.addTip"/>'/* 添加提示 */,'<spring:message code="vendor.operationFailure"/>'/* 操作失败 */,'error');
}
function datagridreload()
{
	$('#datagrid').datagrid('reload');
}

function lookBuyerUser(){//选择采购商
	$('#kkBuyer').window('open');
	$('#formBuyer2').form('reset');
	var ntype = $("#addnotiess").contents().find("#ntype").val();
	if(ntype!=3)
	{
		$("#addnotiess").contents().find("#addids").val('')
		$("#addnotiess").contents().find("#lookpower").val('');
	}
	$("#addnotiess").contents().find("#ntype").val('3');
	$('#datagridssBuyer').datagrid({url: ctx+'/manager/admin/user/getNoteUserList',queryParams:{}});
}


function lookUser()
{
	$('#kk').window('open');
	$('#form2').form('reset');
	var ntype = $("#addnotiess").contents().find("#ntype").val();
	if(ntype!=1)
	{
		$("#addnotiess").contents().find("#addids").val('')
		$("#addnotiess").contents().find("#lookpower").val('');
	}
	$("#addnotiess").contents().find("#ntype").val('1');
	$('#datagridss').datagrid({url: ctx+'/manager/admin/org',queryParams:{search_EQ_roleType:1}});
}
function lookR()
{
	$('#kk222').window('open');
	$('#form222').form('reset');
	var ntype = $("#addnotiess").contents().find("#ntype").val();
	if(ntype!=2)
	{
		$("#addnotiess").contents().find("#addids").val('')
		$("#addnotiess").contents().find("#lookpower").val('');
	}
	$("#addnotiess").contents().find("#ntype").val('2');
	$('#datagridss222').datagrid({url: ctx+'/manager/admin/role',queryParams:{}});
}
function lookUser3(id)
{
	$('#kk3').window('open');
	$('#datagridss3').datagrid({url: ctx+'/manager/vendor/notice/getNoticeLookList/'+id
	});
}
function lookUser4(id)
{
	$('#kk4').window('open');
	$('#datagridss4').datagrid({url: ctx+'/manager/vendor/notice/getCommentLookList/'+id
	});
}
function adddelecturls(vid){
	var addids=$("#addnotiess").contents().find("#addids").val();
	var str1 = addids.replace(vid+',','');  
	$("#addnotiess").contents().find("#addids").val(str1);
	$("#addnotiess").contents().find("#span"+vid).remove();
};
function addposts(ntype,noticeTypeNames,content,title,commentPower,noticeType,addids,validStartTime,validEndTime){
	if(title=='')
	{
		$.messager.alert('<spring:message code="vendor.updateTip"/>'/* 更新提示 */,'<spring:message code="vendor.user.fillTitle"/>'/* 请填写标题 */,'error');
	}else if(content=='')
	{
		$.messager.alert('<spring:message code="vendor.updateTip"/>'/* 更新提示 */,'<spring:message code="vendor.user.fillContents"/>'/* 请填写内容 */,'error');
	}else if(validStartTime=='')
	{
		$.messager.alert('<spring:message code="vendor.updateTip"/>'/* 更新提示 */,'<spring:message code="vendor.user.startTimeEmpty"/>'/* 开始时间不能为空 */,'error');
	}
	else if(validEndTime=='')
	{
		$.messager.alert('<spring:message code="vendor.updateTip"/>'/* 更新提示 */,'<spring:message code="vendor.user.endTimeEmpty"/>'/* 结束时间不能为空 */,'error');
	}else{
		$.messager.confirm('<spring:message code="vendor.user.savePublish"/>'/* 保存并发布 */, '<spring:message code="vendor.user.determineOperation"/>？'/* 确定执行此操作吗 */, function (r) {
			if(r){
				$.post(ctx+"/manager/vendor/notice/addnotie",{"ntype":ntype,
				"noticeTypeNames":noticeTypeNames,
				"content":content,
				"title":title,
				"commentPower":commentPower,
				"noticeType":noticeType,
				"addids":addids,
				"validStartTime":validStartTime+" 00:00:00",
				"validEndTime":validEndTime+" 00:00:00"},function(data){
					if(data=='1')
					{
						close();
						info();
						datagridreload();
					}
					else
					{
						error();
					}
				},"text");
			}
		});
	}
}
function updateposts(ntype,id,noticeTypeNames,content,title,commentPower,noticeType,addids,validStartTime,validEndTime){
	if(title=='')
	{
		$.messager.alert('<spring:message code="vendor.addTip"/>'/* 添加提示 */,'<spring:message code="vendor.user.fillTitle"/>'/* 请填写标题 */,'error');
	}else if(content=='')
	{
		$.messager.alert('<spring:message code="vendor.addTip"/>'/* 添加提示 */,'<spring:message code="vendor.user.fillContents"/>'/* 请填写内容 */,'error');
	}else if(validStartTime=='')
	{
		$.messager.alert('<spring:message code="vendor.addTip"/>'/* 添加提示 */,'<spring:message code="vendor.user.startTimeEmpty"/>'/* 开始时间不能为空 */,'error');
	}
	else if(validEndTime=='')
	{
		$.messager.alert('<spring:message code="vendor.addTip"/>'/* 添加提示 */,'<spring:message code="vendor.user.startTimeEmpty"/>'/* 开始时间不能为空 */,'error');
	}else{
		$.messager.confirm('<spring:message code="vendor.user.savePublish"/>'/* 保存并发布 */, '<spring:message code="vendor.user.determineOperation"/>？'/* 确定执行此操作吗 */, function (r) {
			if(r){
				$.post(ctx+"/manager/vendor/notice/updateNotie",{"ntype":ntype,
					"id":id,
				"noticeTypeNames":noticeTypeNames,
				"content":content,
				"title":title,
				"commentPower":commentPower,
				"noticeType":noticeType,
				"addids":addids,
				"validStartTime":validStartTime+" 00:00:00",
				"validEndTime":validEndTime+" 00:00:00"},function(data){
					if(data=='1')
					{
						close2();
						info();
						datagridreload();
					}
					else
					{
						error();
					}
				},"text");
			}
		});
	}
}
</script>
</body>
</html>
