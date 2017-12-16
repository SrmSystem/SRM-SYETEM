<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/static/script/purchase/RequestManage.js"></script>
<!-- 新增要货订单的主表单 -->
<div class="easyui-panel" data-options="fit:true">
		<div id="request-add" style="height:auto;padding-top: 20px">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="RequestManage.saveRequest()">保存</a>
			</div>
			<form id="form-request-addoredit" method="post" >
				<input type="hidden" name="vendorId" id="vendorId"/> 
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
					<tr>
						<td>要货单号:<input id="requestCode" name="requestCode" value="${po.requestCode }"/></td>  
						<td>供应商编码:<input id="vendorCode" name="vendorCode" class="easyui-textbox" data-options="required:true" readonly="readonly" style="width:120px;"/>
						<td>供应商名称:<input id="vendorName" name="vendorName" class="easyui-textbox" data-options="required:true" readonly="readonly" style="width:120px;"/>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="OrgManage.openWin()"></a></td>
						<td>发货方：<select id="senderId2" name="senderId" class="easyui-combobox" style="width:150px;" data-options="editable:false,required:true" >
								 </select></td>
					</tr>
					<tr>
						<td>收货方:<input id="receiveOrg" name="receiveOrg" class="easyui-textbox"  style="width:120px;" data-options="required:true"/></td>
						<td>订购日期：<input name="orderDate" class="easyui-datetimebox" style="width:100px" data-options="required:true"></td>
						<td></td>
					</tr>
				</table>
			</form> 
    	</div>
    	
    	<div id="tb" style="padding:5px;height:auto">
			<div style="margin-bottom:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addoredit')"></a>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addoredit')"></a>
			</div>
		</div>
   		<table id="datagrid-item-addoredit" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
   			singleSelect: true,toolbar: '#tb',method: 'post',onClickCell: RowEditor.onClickCell">
               <thead><tr>
               <th data-options="field:'id',checkbox:true"></th>  
               <th data-options="field:'itemNo',width:60,editor:{type:'numberbox',options:{required:true}}" >行号</th> 
             	<th data-options="field:'material',width:120, 
             		formatter:function(value,row){
                           return row.materialname;
                       },editor:{type:'combobox',
                           options:{
                               url:'${ctx}/manager/basedata/material/getAllMaterial',    
                               method:'post',                 
                               valueField:'id',
                               textField:'name',
                               required:true
                           }
                       }">物料</th> 
               <th data-options="field:'orderQty',width:100,editor:{type:'numberbox',options:{required:true}}">订购数量</th>
               <th data-options="field:'currency',width:80,editor:'textbox'">币种</th>
               <th data-options="field:'unitName',width:80,editor:'textbox'">单位</th>
               <th data-options="field:'requestTime',editor:'datetimebox',required:true">要求到货时间</th>  
          </tr></thead>
   	</table>
</div>

<!-- 组织信息 -->
<div id="win-org-detail" class="easyui-window" title="发货方详情" style="width:600px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<div class="easyui-panel" data-options="fit:true">
				<div id="orgListToolbar" style="padding:5px;">
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="OrgManage.choice()">选择</a>
				</div>
				<div>
				<form id="form-org-search" method="post">
					编码：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
					名称：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
					<input type="hidden" name="search_EQ_roleType" value="1"/>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="OrgManage.searchOrg()">查询</a>
			</form>
		</div>
			</div>
			<table id="datagrid-org-list" title="组织列表" class="easyui-datagrid"
				data-options="fit:true,method:'post',singleSelect:true,
				queryParams:{search_EQ_orgType:0},
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'code'">编码</th>
				<th data-options="field:'name'">名称</th>
				<th data-options="field:'registerTime'">注册时间</th>
				<th data-options="field:'_orgType'">组织级别</th>
				<th data-options="field:'_roleType'">组织类型</th>
				</tr></thead>
			</table>
		</div>
	</div>
