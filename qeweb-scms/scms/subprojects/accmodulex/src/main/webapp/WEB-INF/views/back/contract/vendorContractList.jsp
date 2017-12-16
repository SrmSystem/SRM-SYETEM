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
		var uploadSealFile='';
		if(r.confirmStatus==1&&r.hasSealAttchement==0){
			uploadSealFile='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="ContractManage.displayUploadSeal(' + r.id + ')">'+'上传盖章附件'+'</a>';
		}else{
			uploadSealFile='上传盖章附件';
		}
		var viewContent=''
		if(r.moduleId>0){
			viewContent='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openItem(' + r.id + ')">'+'条款'+'</a>';
		}else{
			viewContent='条款';
		}
		var viewContract='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="viewContract(' + r.id + ')">'+'查看'+'</a>';
		return uploadSealFile+'&nbsp;&nbsp;&nbsp;'+viewContent+'&nbsp;&nbsp;&nbsp;'+viewContract;
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
    fit:true,title:'合同', url:'${ctx}/manager/contract/vendorContract/getList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50], toolbar:'#toolbar-contract'">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
        <th data-options="field:'operate',formatter:optFmt">操作</th>
        <th data-options="field:'code'">编码</th>
        <th data-options="field:'contractName'">合同名称</th>
        <th data-options="field:'buyerCode',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.code;}">采购组织编码</th>
        <th data-options="field:'buyerName',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.name}">采购组织名称</th>
    
        <th data-options="field:'contractPrice'">合同总价</th>
    <!--     <th data-options="field:'addendumCount'">补充协议数量</th> -->
        <th data-options="field:'signUser'">签订人</th>
        <th data-options="field:'signDate'">签订日期</th>
        <th data-options="field:'effrctiveDateStart'">有效期开始</th>
        <th data-options="field:'effrctiveDateEnd'">有效期结束</th>

        <th data-options="field:'confirmStatus',formatter:function(v,r,i){return contractStatus.getConfirmStatus(v);}">确认状态</th>
         <th data-options="field:'operateFile1',formatter:downConfirmFmt">确认附件</th>
        <th data-options="field:'confirmTime'">确认时间</th>
        <th data-options="field:'hasSealAttchement',formatter:function(v,r,i){return contractStatus.getFileStatus(v);}">盖章附件上传状态</th>
         
          <th data-options="field:'operateFile2',formatter:downSealFmt">盖章附件</th>
         
        <th data-options="field:'sealAttConfirmStatus',formatter:function(v,r,i){return contractStatus.getFileConfirmStatus(v);}">盖章附件确认状态</th>
        <th data-options="field:'enabledStatus',formatter:function(v,r,i){return contractStatus.getEffectiveStatus(v);}">生效状态</th>

      </tr>
    </thead>
  </table>
 
<div id="toolbar-contract" style="padding:5px;">
	<div>
			<form id="form-contract-search" method="post">
			合同编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:150px;"/>
			合同名称：<input type="text" name="search-LIKE_contractName" class="easyui-textbox" style="width:150px;"/>
			供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:150px;"/>
		
			盖章附件上传状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_hasSealAttchement" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未上传</option>
      	 			<option value="1">已上传</option>
      	 		</select>
		  </br>
		   </br>	
		        
      		确认状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未确认</option>
      	 			<option value="1">已确认</option>
      	 			<option value="-1">已驳回</option>
      	 		</select>


      		生效状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_enabledStatus" style="width:150px;">
      	 			<option value="">-全部-</option>
      	 			<option value="0">未生效</option>
      	 			<option value="1">已生效</option>
      	 		</select>
      	 供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:150px;"/>
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
  
		<!-- 上传文件 -->
	<div id="win-file-import" class="easyui-window" title="上传文件" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-file-import" method="post" enctype="multipart/form-data" action=""> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
						<input type=hidden id="id" name="id"/>   
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="ContractManage.submitSealFile();">提交</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-file-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
			<div id="win-filex-import" class="easyui-window" title="上传文件" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-filex-import" method="post" enctype="multipart/form-data" action=""> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="filex" name="planfiles" /><br>
						<input type=hidden id="cid" name="id"/>   
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="ContractManage.submitConfirmFile();">提交</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-filex-import').form('reset')">重置</a>
				</div>
			</form>  
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

 function viewContract(id){
	 var title="合同查看";
	 new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contract/contract/toVendorContractView/' + id,'dialog-contract');
 }
 	
  </script>
</body>
</html>
