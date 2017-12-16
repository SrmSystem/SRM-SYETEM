<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>角色管理</title>
	<script type="text/javascript" src="${ctx}/static/script/backlog/backlog.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-backlogcfg" title="待办配置列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/backlog',method:'post',singleSelect:false,
		toolbar:'#datagrid-backlogcfg-tt',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manager',formatter:BacklogCfg.managerFmt">管理</th>
		<th data-options="field:'viewId'">模块</th>
		<th data-options="field:'content'">待办内容</th>
		<th data-options="field:'orgType'">待办范围</th>
		</tr></thead>
	</table>
	<div id="datagrid-backlogcfg-tt" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BacklogCfg.add('#dialog-editor')">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="BacklogCfg.del()">删除</a>
		</div>
		<div>
			<form id="form-role-search" method="post">
			待办内容：<input type="text" name="search-LIKE_content" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BacklogCfg.query('#datagrid-backlogcfg','#form-role-search')">查询</a>
			</form>
		</div>
	</div>
	<!-- 编辑窗口 -->
	<div id="dialog-editor" class="easyui-dialog" data-options="
	closed:true,modal:true,
	iconCls:'icon-application',
	buttons:'#dialog-editor-bt'
	
	">
	  <div class="easyui-layout" fit="true">
	    <div class="easyui-panel" data-options="region:'west',width:300,title:'填写说明'">
	        <ul class="list-group">
	          <li class="list-group-item">模块必须选择叶子节点</li>
	          <li class="list-group-item">hql和sql可选填其中之一，如果都填写，不执行sql。查询主要用来查询待办条数</li>
	          <li class="list-group-item">范围确定了哪一类型组织将展示该待办</li>
	          <li class="list-group-item">scriptlet：脚本片段，在跳转页面后将执行改脚本</li>
	          <li class="list-group-item"><p>系统里的变量参数列表如下(用于查询语句中)</p>
	           <p>当前用户Id：#currentUserId#</p>
	           <p>当前组织Id：#currentOrgId#</p>
	          </li>
	        </ul>
	    </div>
	    <div class="easyui-panel" data-options="region:'center'">
	      <form class="baseform" id="dialog-editor-form" method="post">
	        <input type="hidden" name="id" id="id" /> 
	        <div>
	          <label>模块</label>
	          <input  class="easyui-combotree" data-options="
	            url:'${ctx}/manager/admin/menu/getMenuEasyuiTree',
	            onSelect:BacklogCfg.onSelectView
	          "
	          id="combotree-viewId"
	          name="viewId"
	          />
	        </div>
	        <div>
	          <label>范围</label>
	          <select class="easyui-combobox" name="orgRoleType">
	            <option value="0">采购商</option>
	            <option value="1">供应商</option>
	          </select>
	        </div>
	        <div>
	          <label>内容</label>
	          <input class="easyui-textbox" name="content"/>
	        </div>
	        <div>
	          <label>hql</label>
	          <textarea style="width:300px;height: 100px;" name="queryHql"></textarea>
	        </div>
	        <div>
	          <label>sql</label>
	          <textarea style="width:300px;height: 100px;" name="querySql"></textarea>
	        </div>
	      
	      </form>
	    </div>
	    
	    <div id="dialog-editor-bt">
	      <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:;" onclick="BacklogCfg.addSubmit('#dialog-editor-form')">保存</a>
	    </div>
	  </div>
	
	</div>
</body>
</html>
