<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<meta charset="UTF-8">
	<title>合同</title>
	<script type="text/javascript">
	function optFmt(v,r,i){
		var editContent='';
		if(r.moduleId>0){
			editContent='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openItem(' + r.id + ')">'+'条款'+'</a>';
		}else{
			editContent='条款';
		}
		var editContract='';
		if(r.auditStatus==0){
			editContract='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editContract(' + r.id + ','+ i+')">'+'编辑'+'</a>';
		}else{
			editContract='编辑';
		}
		var viewContract='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="viewContract(' + r.id + ','+ i+')">'+'查看'+'</a>';
		var disableContract='';
		
		if(r.isDisable==1&&r.enabledStatus==1){
			var disableContract='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="ContractManage.disableContract(' + r.id +')">'+'失效'+'</a>';
		}
		return editContent+'&nbsp;&nbsp;&nbsp;'+editContract+'&nbsp;&nbsp;&nbsp;'+viewContract+'&nbsp;&nbsp;&nbsp;'+disableContract;
 	}
	function downSealFmt(v,r,i){
		var fileSealName='';
		if(r.fileSealName!=null){
			 fileSealName=r.fileSealName;
		}
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="ContractManage.downSealFile(' + r.id +')">'+fileSealName+'</a>';
	}
	function downConfirmFmt(v,r,i){
		var fileConfirmName='';
		if(r.fileConfirmName!=null){
			 fileConfirmName=r.fileConfirmName;
		}
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="ContractManage.downConfirmFile(' + r.id +')">'+fileConfirmName+'</a>';
	}
	</script>
	<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/contract/contract.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/contract/contractContent.js"></script>
</head>

<body>
  <table id="datagrid-contract-list" data-options="
    fit:true,title:'合同', url:'${ctx}/manager/contract/contract/getList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50], toolbar:'#toolbar-contract'">
    <thead>
      <tr>
      
        <th width="50px" data-options="field:'id',checkbox:true"></th>
        <th width="140px" data-options="field:'operate',formatter:optFmt">操作</th>
        <th width="140px" data-options="field:'code'">编码</th>
        <th width="140px" data-options="field:'contractName'">合同名称</th>
        <th width="140px" data-options="field:'contractType',formatter:function(v,r,i){return contractStatus.getContractType(v);}">合同类型</th>
        
        <th width="140px" data-options="field:'buyerCode',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.code;}">采购组织编码</th>
        <th width="140px" data-options="field:'buyerName',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.name}">采购组织名称</th>
        <th width="140px" data-options="field:'vendorCode',formatter:function(v,r,i){if(r.vendor==null) return '';else return r.vendor.code}">供应商编码</th>
        <th width="140px" data-options="field:'vendorName',formatter:function(v,r,i){if(r.vendor==null) return '';else return r.vendor.name}">供应商名称</th>
        <th width="140px" data-options="field:'contractPrice'">合同总价</th>
      <!--   <th data-options="field:'addendumCount'">补充协议数量</th> -->
        <th width="140px" data-options="field:'signUser'">签订人</th>
        <th width="140px" data-options="field:'signDate'">签订日期</th>
        <th width="140px" data-options="field:'effrctiveDateStart'">有效期开始</th>
        <th width="140px" data-options="field:'effrctiveDateEnd'">有效期结束</th>
        <th width="140px" data-options="field:'auditStatus',formatter:function(v,r,i){return contractStatus.getAuditStatus(v);}">审核状态</th>
        <th width="140px" data-options="field:'auditTime'">审核时间</th>
        <th width="140px" data-options="field:'publishStatus',formatter:function(v,r,i){return contractStatus.getPublishStatus(v);}">发布状态</th>
        <th width="140px" data-options="field:'publishTime'">发布时间</th>
        <th width="140px" data-options="field:'confirmStatus',formatter:function(v,r,i){return contractStatus.getConfirmStatus(v);}">确认状态</th>
         <th width="140px" data-options="field:'operateFile1',formatter:downConfirmFmt">确认附件</th>
        <th width="140px" data-options="field:'confirmTime'">确认时间</th>
        <th width="140px" data-options="field:'hasSealAttchement',formatter:function(v,r,i){return contractStatus.getFileStatus(v);}">盖章附件上传状态</th>
         
          <th width="140px" data-options="field:'operateFile2',formatter:downSealFmt">盖章附件</th>
         
        <th width="140px" data-options="field:'sealAttConfirmStatus',formatter:function(v,r,i){return contractStatus.getFileConfirmStatus(v);}">盖章附件确认状态</th>
        <th width="140px" data-options="field:'enabledStatus',formatter:function(v,r,i){return contractStatus.getEffectiveStatus(v);}">生效状态</th>
      </tr>
    </thead>
  </table>
 
<div id="toolbar-contract" style="padding:5px;">
		<div>
			<a href="javascript:;" onclick="ContractManage.openSelModuleWin(10)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增年度合同</a>
			<a href="javascript:;" onclick="ContractManage.openSelModuleWin(20)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增临时合同</a>
						
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="ContractManage.deleteContract()">删除</a>
		</div>
		<div>
			<form id="form-contract-search" method="post">
			合同编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:150px;"/>
			合同名称：<input type="text" name="search-LIKE_contractName" class="easyui-textbox" style="width:150px;"/>
			供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:150px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:150px;"/>
			盖章附件上传状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_hasSealAttchement" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未上传</option>
      	 			<option value="1">已上传</option>
      	 		</select>
		  </br>
		   </br>	
		        	审核状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_auditStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未审核</option>
      	 			<option value="1">审核通过</option>
      	 			<option value="-1">审核驳回</option>
      	 			<option value="-8">审核中</option>
      	 		</select>
      	发布状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未发布</option>
      	 			<option value="1">已发布</option>
      	 		</select>
      		&nbsp;&nbsp;&nbsp;&nbsp;确认状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未确认</option>
      	 			<option value="1">已确认</option>
      	 			<option value="-1">已驳回</option>
      	 		</select>


      			&nbsp;&nbsp;&nbsp;&nbsp;生效状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_enabledStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未生效</option>
      	 			<option value="1">已生效</option>
      	 			<option value="-1">已失效</option>
      	 		</select>
      	盖章附件确认状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_sealAttConfirmStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未确认</option>
      	 			<option value="1">已确认</option>
      	 			<option value="-1">已退回</option>
      	 		</select>
      	 </br>	
      	  </br>		
		   签订时间:<input class="easyui-datebox" name="search-GTE_signDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">
      	   ~<input class="easyui-datebox" name="search-LTE_signDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">
      	   有效期开始:<input class="easyui-datebox" name="search-GTE_effrctiveDateStart" data-options="showSeconds:false,editable:false" value="" style="width:150px">
      	    ~<input class="easyui-datebox" name="search-LTE_effrctiveDateStart" data-options="showSeconds:false,editable:false" value="" style="width:150px">
      	   		  有效期结束:<input class="easyui-datebox" name="search-GTE_effrctiveDateEnd" data-options="showSeconds:false,editable:false" value="" style="width:150px">
      	   ~:<input class="easyui-datebox" name="search-LTE_effrctiveDateEnd" data-options="showSeconds:false,editable:false" value="" style="width:150px">
      

      	   

			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchContract()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-contract-search').form('reset')">重置</a>
			</form>
		</div>

</div>
  
	<!-- 选择模板 -->
	<div id="win-module-detail" title="模板"
		style="width: 600px; height: 450px"
		data-options="iconCls:'icon-add',modal:true,closed:true">
		<table id="datagrid-module-list" title="模板列表"
			data-options="method:'post',singleSelect:true,
				toolbar:'#moduleListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="30%" data-options="field:'code'">模板编码</th>
					<th width="30%" data-options="field:'name'">模板名称</th>
				</tr>
			</thead>
			
			<input type="hidden" id="contractType" value="" />
		</table>

		<div id="moduleListToolbar" style="padding: 5px;">
			<div>
				<a id="link-moduleSel-choice" href="javascript:;"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true"
					onclick="ContractManage.choiceModule()">选择</a>
					
				<a id="link-moduleSel-choice" href="javascript:;"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true"
					onclick="ContractManage.choiceNoModule()">无模板</a>	
			</div>
			<div>
				<form id="form-module-search" method="post">
					模板编码：<input type="text" name="search-LIKE_code"
						class="easyui-textbox" style="width: 80px;" /> 模板名称：<input
						type="text" name="search-LIKE_name" class="easyui-textbox"
						style="width: 80px;" /> <a id="link-moduleSel-search"
						href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'"
						onclick="ContractManage.searchModule()">查询</a>
				</form>
			</div>
		</div>
	</div>
	


  <script type="text/javascript">
	 var clientWidth = document.body.clientWidth;	
	 var clientHeight = document.body.clientHeight;	
	 
 $(function(){
 	$("#datagrid-contract-list").datagrid({});
 })
 
 function searchContract(){
	var searchParamArray = $('#form-contract-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-contract-list').datagrid('load',searchParams);
}
 
  /**
  * 打开条款页面
  */
 function openItem(id){

	 var title="条款制定";
	new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contract/contract/toDisplayContent?contractId=' + id,'dialog-contract');
 }
 function editContract(id){
	 var title="合同编辑";
	 new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contract/contract/toContractEdit/' + id,'dialog-contract');
 }
 function viewContract(id){
	 var title="合同查看";
	 new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contract/contract/toContractView/' + id,'dialog-contract');
 }
 	
  </script>
</body>
</html>
