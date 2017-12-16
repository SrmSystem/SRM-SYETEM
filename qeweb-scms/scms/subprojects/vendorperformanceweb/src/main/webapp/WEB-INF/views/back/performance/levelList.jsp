<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>等级设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/level.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagridx" data-options="fit:true,
    url:'${ctx}/manager/vendor/level',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="10%" data-options="field:'id',checkbox:true">等级ID</th>
        <th width="15%" data-options="field:'nop',formatter:level.vfmt">操作</th>
        <th width="10%" data-options="field:'code'">等级编码</th>
        <th width="10%" data-options="field:'levelName'">等级名称</th>
        <th width="10%" data-options="field:'lowerLimit'">等级下限</th>
        <th width="10%" data-options="field:'upperLimit'">等级上限</th>
        <th width="10%" data-options="field:'quadrant'">象限</th>
        <th width="10%" data-options="field:'remarks'">备注</th>
        <th width="10%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="level.add()">新增</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="level.release()">启用</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="level.dels()">作废</a>   
      <form id="form" method="post">
                             等级编码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             等级名称:<input class="easyui-textbox" name="search-LIKE_levelName" type="text" style="width:80px;"/>
                             生效状态:
                         <select name="search-EQ_abolished" class="easyui-combobox" style="width:80px;">
                         	<option value="">请选择</option>
                         	<option value="0">生效</option>
                         	<option value="1">作废</option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="level.search()">查询</a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>        
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-level-saveUpadte" title="等级"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-level-saveUpadte" method="post" >
			    <input type="hidden" id="fatherId" name="fatherId" value="0"/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>等&nbsp;级&nbsp;名&nbsp;称:</td><td><input class="easyui-textbox" id="levelName" name="levelName" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>等&nbsp;级&nbsp;下&nbsp;限&nbsp;:</td><td><input class="easyui-numberbox" id="lowerLimit" name="lowerLimit" type="text"
						data-options="required:true,readonly:true"
					/></td>
				</tr>
				<tr>
					<td>等&nbsp;级&nbsp;上&nbsp;限&nbsp;:</td><td><input class="easyui-numberbox" id="upperLimit" name="upperLimit" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr id="tidd">
					<td> 生&nbsp;效&nbsp;状&nbsp;态&nbsp;:</td>
					<td>
						<select id="abolished" name="abolished" class="easyui-combobox" style="width:80%" data-options="required:true,editable:false">
							<option value=''>请选择</option>
							<option value='0'>启用</option>
							<option value='1'>禁用</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>象&nbsp;限:</td><td><input class="easyui-numberbox" id="quadrant" name="quadrant" type="text"
						data-options="required:true,min:1,max:2"
					/></td>
				</tr>
				<tr><td> 备注:</td>
					<td>
						<input class="easyui-textbox" id="remarks" name="remarks" type="text"/>
					</td>
				</tr>
				<tr ><td> 提示1:</td>
					<td style="color: #F00">
						等级编号不可填写，自动产生
					</td>
				</tr>
				<tr id="tis1"><td> 提示2:</td>
					<td style="color: #F00">
						“等级下限”自动获取，无法改动
					</td>
				</tr>
				<tr id="tis"><td> 提示3:</td>
					<td style="color: #F00">
						修改“等级上限”时候,会修改其他等级上限
					</td>
				</tr>
				</table>
			</form>
			<div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="level.submit()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-level-saveUpadte').form('reset')">重置</a>
			</div>
	</div>
	<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
	</script>
</body>
</html>
