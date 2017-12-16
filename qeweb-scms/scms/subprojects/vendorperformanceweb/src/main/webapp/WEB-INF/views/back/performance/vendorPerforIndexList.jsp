<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>指标设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforIndex.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/performance/index',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'id',checkbox:true">ID</th>
        <th width="5%" data-options="field:'nop',formatter:vendorPerforIndex.vfmt">操作</th>
        <th width="15%" data-options="field:'code'">指标编号</th>
        <th width="15%" data-options="field:'indexName'">指标名称</th>
        <th width="10%" data-options="field:'describe'">指标描述</th>
        <th width="10%" data-options="field:'dimName',formatter:function(v,r,i){if(r.dimensionsEntity)return r.dimensionsEntity.dimName;else return ''}">维度</th>
        <th width="10%" data-options="field:'otvd',formatter:function(v,r,i){if(r.otvd==1)return '是';else return '否'}">一票否决(维度)</th>
        <th width="10%" data-options="field:'otvv',formatter:function(v,r,i){if(r.otvv==1)return '是';else return '否'}">一票否决(供应商)</th>
        <th width="10%" data-options="field:'indexType',formatter:function(v,r,i){if(r.indexType==1)return '自动';else return '手动'}">类型</th>
        <th width="10%" data-options="field:'abolished',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">启用状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorPerforIndex.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="vendorPerforIndex.dels()">作废</a>
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'" href="javascript:;" onclick="vendorPerforIndex.deleteList()">删除</a>
      <form id="form" method="post">
                            指标名称:<input class="easyui-textbox" name="search-LIKE_indexName" type="text" style="width:120px;"/>
                            维度名称:<input id="cc" class="easyui-combobox" name="search-EQ_dimensionsId"  style="width:120px;"/>
                            启用状态:<select name="search-EQ_abolished" class="easyui-combobox" style="width:80px;" data-options="editable:false">
                         	<option value="">请选择</option>
                         	<option value="1">未启用</option>
                         	<option value="0">启用</option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorPerforIndex.search()">查询</a>     
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a> 
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="updateapp" class="easyui-dialog" title=" 指标" style="width: 95%;height:95% "
	data-options="modal:true,closed:true,buttons:'#dialog-adder-bbb'">

<div id="dialog-adder-bbb">
   <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="vendorPerforIndex.submit2()">提交</a>
   <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-reset'" onclick="$('#form-vendorPerforIndex-Upadte').form('reset')">重置</a>
</div>
</div>
<div id="dialog-vendorPerforIndex-saveUpadte" class="easyui-dialog" title=" 指标"  style="width: 95%;height:95% "
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
		<div id="cddc" class="easyui-layout" style="width:100%;height:100%;"> 
		<form id="form-vendorPerforIndex-saveUpadte" method="post" > 
		    <div data-options="region:'west'" style="width:30%;">
				<table style="text-align: left;padding:5px;float: left;" border="1" cellpadding="5">
					<tr>
						<td>指标名称:</td><td><input class="easyui-textbox" id="indexName" name="indexName" type="text"
							data-options="required:true"
						/>
						</td>
					</tr>
					<tr>
						<td>指标描述:</td><td><input class="easyui-textbox" id="describe" name="describe" type="text"/></td>
					</tr>
	 				<tr>
						<td>维度:</td>
						<td>
							<input id="dimensionsId" type="hidden" name="dimensionsId" value=""/>
							<input class="easyui-textbox" id="dimensionsNameA" name="dimensionsNameA"  data-options="required:true,editable:false"/>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="vendorPerforIndex.seer('A')">选择维度</a>
						</td>
					</tr>
					<tr>
						<td>一票否决（维度）:</td><td><input type="radio" id="otvd" name="otvd" value="1" />是&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;<input type="radio" id="otvd" name="otvd" value="0" checked="checked"/>否</td>
					</tr>
					<tr>
						<td>一票否决(供应商) :</td><td><input type="radio" id="otvv" name="otvv" value="1" />是&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;<input type="radio" id="otvv" name="otvv" value="0" checked="checked"/>否</td>
					</tr>
					<tr>
						<td>类型 :</td>
						<td><select name="indexType" class="easyui-combobox" style="width:99%;" data-options="editable:false,required:true">
		                       	<option value="1">自动</option>
		                       	<option value="0">手动</option>
		                    </select> 
		                </td>
					</tr>
					<tr>
						<td>是否启用 :</td><td><input type="radio" id="abolished" name="abolished" value="1"/>未启用&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;    <input type="radio" id="abolished" name="abolished" value="0" checked="checked"/>启用</td>
					</tr>
					</table>
			</div>   
		    <div data-options="region:'east'" style="width:70%;">
		    	<table id="apk" style="text-align:center;padding:5px;float: left;margin-left: 5PX;width: 95%" border="1" cellpadding="10">
					<tr>
						<th style="text-align: left;" colspan="3">
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="vendorPerforIndex.addGS()">新增公式</a>
						</th>
					</tr>
					<tr>
						<th style="text-align: center;width: 20%">公式周期<input type="hidden" id="numbers" value="0" /></th>
						<th style="text-align: center;width: 60%">公式设定<font style="color: #F00">*公式要素以“[&nbsp;]”（中号括）括起来</font></th>
						<th style="text-align: center;width: 20%">操作</th>
					</tr>
					<c:forEach items="${vfs }" var="vfs">
					  <tr id="trid${ vfs.id}"><td><input class="easyui-combobox" id="cycleId${ vfs.id}" name="cycleId" data-options="required:true,editable:false"/></td><td>
					  <input class="easyui-textbox" id="content${ vfs.id}" name="content" type="text" style="width:100%;" data-options="required:true,height:40"/></td>
					  <td><a id="editid${ vfs.id}" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="vendorPerforIndex.edit(${ vfs.id})">导入元素</a>
					  <a id="deltedg${ vfs.id}" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="vendorPerforIndex.deleteGS(${ vfs.id})">删除</a></td>
			          </tr>
					</c:forEach>
					</table>
									
					<div style="text-align: center;padding:5px;width: 100%;float: left;">
						
					</div>
		    </div>
		    </form>	   
		</div> 

		

		 <div id="dialog-adder-bb">
		 	<a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="vendorPerforIndex.submit()">提交</a>
			<a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-reset'" onclick="$('#form-vendorPerforIndex-saveUpadte').form('reset')">重置</a>
	    </div>
			
	</div> 

	
<div id="dialog-vendorPerforIndex-purchase" class="easyui-dialog" title="公式元素选择" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 50%;height: 50%">
    	<input type="hidden" id="numberGS" value="" />
    	<table style="width: 100%;" id="blistssssss"
			data-options="fit:true,method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],toolbar:'#tt2'"
			>
			<thead><tr>
		        <th width="50px" data-options="field:'id',checkbox:true">公式元素ID</th>
		        <th width="120px" data-options="field:'name'">公式元素名称</th>
		        <th width="80px" data-options="field:'fallValue'">公式元素值</th>
		        <th width="120px" data-options="field:'describe'">公式元素描述</th>
		      </tr></thead>
		</table>
		 <div id="tt2">
		    <div>
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vendorPerforIndex.xuanzhe1()">确认</a>
		        <form id="form2" method="post">
                                 公式元素名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
      		 	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorPerforIndex.blistssssss()">查询</a>      
		      </form>
		    </div>
		    <form id="form-export2">
			</form>
		 </div>	 
	</div>
<div id="dialog-weidu-purchase" class="easyui-dialog" title="维度选择" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 50%;height: 50%">
<div class="easyui-panel" region="center" data-options="fit:true">
	<table id="treegrid-dimen-list"
		data-options="fit:true,singleSelect:true,
		idField:'id',treeField:'code',animate:true,onContextMenu:onContextDimen,rownumbers:true,
		toolbar:'#toolbar-dimen',onLoadSuccess:gridLoadSuc,onEndEdit:gridLoadSuc,onCancelEdit:gridLoadSuc
		"
		>
		<thead><tr>
		<th width="20%" data-options="field:'id',hidden:true">序号</th>
		<th width="24%" data-options="field:'code',editor:'text'">维度编码</th>
		<th width="24%" data-options="field:'dimName',editor:'text'">维度名称</th>
		<th width="24%" data-options="field:'remarks',editor:'text'">备注</th>
		<th width="24%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
		</tr></thead>
	</table>
	<div id="toolbar-dimen">
		右键点击子维度的某一行，点击选择带回。
	</div>
  </div>
	<div id="dimen-dimen" class="easyui-menu">
		<div data-options="iconCls:'icon-add'" onclick="weiduxuanzhe()">确认</div>
	</div>
	<div id="dimen-dimen2" class="easyui-menu">
	</div>
</div>
<script type="text/javascript">
$(function(){
	$('#datagrid').datagrid();
});
var curSeledDId=null;
function onContextDimen(e,row){
	if(row.children!=null)
	{
		$.messager.show({
			title:'提示',
			msg:'<font style="color:#F00">请选择他的子节点</font>',
			showType:'show',
			timeout:1000,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
		e.preventDefault();
		$(this).treegrid('select',row.id);
		$('#dimen-dimen2').menu('hide',{
			left:e.pageX,
			top:e.pageY
			
		});
		return false;
	}
	if(row.abolished==1)
	{
		$.messager.show({
			title:'提示',
			msg:'<font style="color:#F00">无效节点</font>',
			showType:'show',
			timeout:1000,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
		e.preventDefault();
		$(this).treegrid('select',row.id);
		$('#dimen-dimen2').menu('hide',{
			left:e.pageX,
			top:e.pageY
			
		});
		return false;
	}
	e.preventDefault();
	$(this).treegrid('select',row.id);
	$('#dimen-dimen').menu('show',{
		left:e.pageX,
		top:e.pageY
		
	});
}
function weiduxuanzhe(){
	var selectedNode = $('#treegrid-dimen-list').treegrid('getSelected');
	$("input[name='dimensionsId']").val(selectedNode.id);
	$("#dimensionsName"+tyu).textbox("setText",selectedNode.dimName);
	$('#dialog-weidu-purchase').window('close');
}


/** 树状grid加载后对上移下移列的渲染 */
function gridLoadSuc(data){
	$('.wait-up').linkbutton({
		iconCls:'icon-arrow_up',
		plain:true
	});
	$('.wait-down').linkbutton({
		iconCls:'icon-arrow_down',
		plain:true
	});
} 
</script>
</body>
</html>
