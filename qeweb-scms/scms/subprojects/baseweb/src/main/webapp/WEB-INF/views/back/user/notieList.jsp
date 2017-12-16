<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<title>公告管理</title>
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
        <th width="50px" data-options="field:'not1',formatter:notieJS.updatess">操作</th>
        <th width="150px" data-options="field:'title'">题目</th>
        <th width="150px" data-options="field:'createUserName'">发布人</th>
        <th width="110px" data-options="field:'commentPower',formatter:function(v,r){if(r.commentPower==0) return '允许'; else return '不允许'}">是否允许评论</th>
        <th width="80px" data-options="field:'noticeType',formatter:function(v,r){if(r.noticeType==1) return '置顶'; else return '不置顶'}">是否置顶</th>
        <th width="80px" data-options="field:'lookNumber',formatter:notieJS.getlookNumber">浏览次数</th>
        <th width="80px" data-options="field:'spkeaNumber',formatter:notieJS.getspkeaNumber">评论次数</th>
        <th width="130px" data-options="field:'validStartTime'">有效起始日期</th>
        <th width="130px" data-options="field:'validEndTime'">有效结束日期</th>
        <th width="130px" data-options="field:'createTime'">创建日期</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="notieJS.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="notieJS.del()">删除</a>
      <form id="form" method="post">
                             题目:<input class="easyui-textbox" name="search-LIKE_title" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="notieJS.search()">查询</a>
        <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>      
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
<div id="kk" class="easyui-dialog" title="添加浏览权限---组织" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,toolbar:'#orgListToolbar',queryParams:{search_EQ_orgType:0},
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
  ">
    <thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="130px" data-options="field:'code'">编码</th>
		<th width="100px" data-options="field:'name'">名称</th>
		<th width="130px" data-options="field:'registerTime'">注册时间</th>
		<th width="100px" data-options="field:'_orgType'">组织级别</th>
		<th width="100px" data-options="field:'_roleType'">组织类型</th>
		</tr></thead>
  </table>
  <div id="orgListToolbar">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()">选择带回</a> 
      <div>
			<form id="form2" method="post">
			编码：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			名称：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
			组织类型：<input name="search_EQ_roleType" class="easyui-combobox" style="width:120px" value=""
				data-options="url:'${ctx}/common/easyuiselect/getOrgRoleType/true',panelHeight:'auto'"
			/>
			<input type="hidden" name="search_EQ_orgType" value="0"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()">查询</a>
			</form>
		</div>   
    </div>
  </div>   
</div>
<div id="kk222" class="easyui-dialog" title="添加浏览权限---角色" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss222" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,toolbar:'#orgListToolbar222',
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
  ">
    <thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="130px" data-options="field:'code',width:100,formatter:function(v,r,i){if(r.roleType==1) 
			return '<font color=red>'+v+'</font>'; else return v;}">角色编码</th>
		<th width="130px" data-options="field:'name',width:100">角色名</th>
		<th width="130px" data-options="field:'remark',width:100">备注</th>
		<th width="130px"  data-options="field:'roleType',hidden:true">角色类型</th>
		</tr></thead>
  </table>
  <div id="orgListToolbar222">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe222()">选择带回</a> 
      <div>
			<form id="form222" method="post">
			角色编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			角色名：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch222()">查询</a>
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
        <th data-options="field:'id',checkbox:true">浏览记录ID</th>
        <th data-options="field:'createUserName'">浏览人名称</th>
        <th data-options="field:'createTime'">浏览时间</th>
      </tr>
    </thead>
  </table>
  <div id="ttt3">
    <div>
      <form id="form3" method="post">
                             浏览人名称:<input class="easyui-textbox" name="search-LIKE_createUserName" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch3()">查询</a>      
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
        <th width="30px"  data-options="field:'id',checkbox:true">评论记录ID</th>
        <th width="130px"  data-options="field:'createUserName'">评论供应商名称</th>
        <th width="130px"  data-options="field:'content'">评论内容</th>
        <th width="130px"  data-options="field:'createTime'">评论供应商时间</th>
      </tr>
    </thead>
  </table>
  <div id="ttt4">
    <div>
      <form id="form3" method="post">
                             浏览人名称:<input class="easyui-textbox" name="search-LIKE_createUserName" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch3()">查询</a>      
      </form>
    </div>
  </div>
</div> 
<script type="text/javascript">
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
function xuanzhe()
{
	var addids=$("#addnotiess").contents().find("#addids").val();
	var su="";
	var sn="";
	var selections = $('#datagridss').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(var i=0;i<selections.length;i++){
		if(addids.indexOf(selections[i]["id"])!=-1){
			$.messager.alert('提示','已经有选中的记录','info');
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
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(var i=0;i<selections.length;i++){
		if(addids.indexOf(selections[i]["id"])!=-1){
			$.messager.alert('提示','已经有选中的记录','info');
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
	$.messager.alert('添加提示','操作成功','info');
}
function error()
{
	$.messager.alert('添加提示','操作失败','error');
}
function datagridreload()
{
	$('#datagrid').datagrid('reload');
}
function lookUser()
{
	$('#kk').window('open');
	var ntype = $("#addnotiess").contents().find("#ntype").val();
	if(ntype!=1)
	{
		$("#addnotiess").contents().find("#addids").val('')
		$("#addnotiess").contents().find("#lookpower").val('');
	}
	$("#addnotiess").contents().find("#ntype").val('1');
	$('#datagridss').datagrid({url: ctx+'/manager/admin/org'});
}
function lookR()
{
	$('#kk222').window('open');
	var ntype = $("#addnotiess").contents().find("#ntype").val();
	if(ntype!=2)
	{
		$("#addnotiess").contents().find("#addids").val('')
		$("#addnotiess").contents().find("#lookpower").val('');
	}
	$("#addnotiess").contents().find("#ntype").val('2');
	$('#datagridss222').datagrid({url: ctx+'/manager/admin/role'});
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
		$.messager.alert('更新提示','请填写标题','error');
	}else if(content=='')
	{
		$.messager.alert('更新提示','请填写内容','error');
	}else if(validStartTime=='')
	{
		$.messager.alert('更新提示','开始时间不能为空','error');
	}
	else if(validEndTime=='')
	{
		$.messager.alert('更新提示','结束时间不能为空','error');
	}else{
		$.messager.confirm("保存并发布", "确定执行此操作吗？", function (r) {
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
		$.messager.alert('添加提示','请填写标题','error');
	}else if(content=='')
	{
		$.messager.alert('添加提示','请填写内容','error');
	}else if(validStartTime=='')
	{
		$.messager.alert('添加提示','开始时间不能为空','error');
	}
	else if(validEndTime=='')
	{
		$.messager.alert('添加提示','开始时间不能为空','error');
	}else{
		$.messager.confirm("保存并发布", "确定执行此操作吗？", function (r) {
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
