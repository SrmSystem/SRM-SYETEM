<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商物料管理</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforVendorMat.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforVendorMat',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true">ID</th>
        <th width="10%" data-options="field:'vcode',formatter:function(v,r){if(r.re) {return r.re.orgCode;} else ''}">供应商编号</th>
        <th width="10%" data-options="field:'vname',formatter:function(v,r){if(r.re) {return r.re.orgName;} else ''}">供应商名称</th>
        <th width="15%" data-options="field:'mcode',formatter:function(v,r){if(r.mt) {return r.mt.code;} else ''}">物料类别编号</th>
        <th width="15%" data-options="field:'mname',formatter:function(v,r){if(r.mt) {return r.mt.name;} else ''}">物料类别名称</th>
        <th width="15%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorMat.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorMat.update()">修改</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="vendorMat.release()">启用</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="vendorMat.dels()">作废</a>   
      <form id="form" method="post">
                             供应商编号:<input class="easyui-textbox" name="search-LIKE_re.orgCode" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_re.orgName" type="text" style="width:80px;"/>
                             物料类别编号:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             物料类别名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
                             生效状态:
                         <select name="search-EQ_abolished" class="easyui-combobox" style="width:80px;">
                         	<option value="">请选择</option>
                         	<option value="0">生效</option>
                         	<option value="1">作废</option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorMat.search()">查询</a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>        
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-vendormaterialType-saveUpadte" title="供应商物料"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-vendormaterialType-saveUpadte" method="post" >
			    <input type="hidden" id="id" name="id" value="0"/>
			    <input type="hidden" id="abolished" name="abolished" value=""/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<input type="hidden" id="reviewsId" name="reviewsId" value=""/>
					<td>供应商:</td><td><input class="easyui-textbox" id="vName" name="vName" type="text"
					data-options="required:true,readonly:true"
					/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="vendorMat.seer('1')">选择供应商</a>
					</td>
				</tr>
				<tr>
					<input type="hidden" id="materialtypeId" name="materialtypeId" value=""/>
					<td>物料类别:</td><td><input class="easyui-textbox" id="matypeName" name="matypeName" type="text"
					data-options="required:true,readonly:true"
					/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="vendorMat.seer('2')">选择物料类别</a>
					</td>
				</tr>
				</table>
			</form>
			<div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="vendorMat.submit()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-vendormaterialType-saveUpadte').form('reset')">重置</a>
			</div>
	</div>

<div id="dialog-reviews-vendor" title="选择供应商带回" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 90%;height: 90%">
  <table id="datagrid1" data-options="
    fit:true,
    method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100], 
    toolbar:'#tt1'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true">参评ID</th>
        <th width="15%" data-options="field:'code',formatter:function(v,r){if(r.vendorBaseInfoEntity) {if(r.vendorBaseInfoEntity.org){return r.vendorBaseInfoEntity.org.code;}} else ''}">供应商代码</th>
        <th width="15%" data-options="field:'name',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.name; else ''}">供应商名称</th>
        <th width="15%" data-options="field:'vendorPhaseName',formatter:function(v,r){if(r.vendorBaseInfoEntity) {if(r.vendorBaseInfoEntity.vendorPhase){return r.vendorBaseInfoEntity.vendorPhase.name;}} else ''}">供应商阶段</th>
        <th width="15%" data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName; else ''}">周期</th>
        <th width="15%" data-options="field:'joinStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',true);}">是否参评</th>
      </tr>
    </thead>
  </table>
  <div id="tt1">
    <div>
	 <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorMat.xuanzhe1()">确认</a>
      <form id="form1" method="post">
                             供应商编码:<input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.code" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.name" type="text" style="width:80px;"/>
                             是否参评:
                <select name="search-EQ_joinStatus" class="easyui-combobox" style="width:80px;">
                	<option value="">请选择</option>
                	<option value="1">是</option>
                	<option value="0">否</option>
                </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="reviews.search()">查询</a>   
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form1').form('reset')">重置</a>     
      </form>
    </div>
  </div>
</div>
<div id="dialog-reviews-materialType" title="选择物料类别带回" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 90%;height: 90%">
 <table id="datagrid2" data-options="
    fit:true,
    method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt2'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true">ID</th>
        <th width="15%" data-options="field:'code'">物料类别编号</th>
        <th width="15%" data-options="field:'name'">物料类别名称</th>
        <th width="15%" data-options="field:'remarks'">备注</th>
        <th width="15%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt2">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorMat.xuanzhe2()">确认</a>
      <form id="form2" method="post">
                             物料类别编号:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             物料类别名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
                             生效状态:
                         <select name="search-EQ_abolished" class="easyui-combobox" style="width:80px;">
                         	<option value="">请选择</option>
                         	<option value="0">生效</option>
                         	<option value="1">作废</option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="materialType.search()">查询</a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form2').form('reset')">重置</a>        
      </form>
    </div>
  </div>
</div>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
</script>
</body>
</html>
