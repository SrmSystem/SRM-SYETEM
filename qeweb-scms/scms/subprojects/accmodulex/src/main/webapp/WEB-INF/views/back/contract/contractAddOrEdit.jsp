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


	</script>
</head>

<body>

<script type="text/javascript" src="${ctx}/static/base/util/IsTimeUtil.js"></script>

<script type="text/javascript">
function optFmt(v,r,i){
	return ''; 	
	}
var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#datagrid-contractItem-list').datagrid('validateRow', editIndex)){
		$('#datagrid-contractItem-list').datagrid('endEdit', editIndex);
		editIndex = undefined;
		
		return true;
	} else {
		return false;
	}
}




function onClickRow2(index,data){
	
	$('#datagrid-contractItem-list').datagrid('selectRow', index).datagrid('beginEdit', index);
	editIndex = index;
	 var ed = $('#datagrid-contractItem-list').datagrid('getEditor', { index: index, field: 'itemQty' });
	 var total=$('#datagrid-contractItem-list').datagrid('getEditor', { index: index, field: 'totalPrice' });
	 var taxPricex=$('#datagrid-contractItem-list').datagrid('getEditor', { index: index, field: 'taxPrice' });

     $(ed.target).numberbox({ onChange: function () {
    	 caculate();
    	 caculateTotal();
     }    
     });
     $(taxPricex.target).numberbox({ onChange: function () {
    	 caculate();
    	 caculateTotal();
     }    
     });
 
     
     function caculate(){
          var itemQty=ed.target.val();
          var taxPrice=taxPricex.target.val();;
          $(ed.target).numberbox('setValue',itemQty);    
          $(taxPricex.target).numberbox('setValue',taxPrice);    
          $(total.target).numberbox('setValue',itemQty*taxPrice);    
     }
     
     function caculateTotal(){
    	  var rows = $("#datagrid-contractItem-list").datagrid("getRows");
    	  var tt=0;
    	  for(var i=0;i<rows.length;i++){
    		  var total=$('#datagrid-contractItem-list').datagrid('getEditor', { index: i, field: 'totalPrice' });
    		  tt=Number(tt)+Number(total.target.val());
    		  $("#contractPrice").textbox('setValue',tt);    
    	  }
    	  
     }
}
	$.extend($.fn.validatebox.defaults.rules, {
	    isPhone: {
	        validator: function (value, param) {//param为默认值
	        	//验证手机号
	        	 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
	        	 var temp1 = reg.test(value);
	        	 //验证固定号码
	        	 var re = /^0\d{2,3}-?\d{7,8}$/;
	        	 var temp2 = re.test(value)
	
	        	 if(temp1 || temp2){
	        		 return true;
	        	 }else{
	        		 return false;
	        	 }
	        },
	        message: '请输入正确的联系电话'
	    }
	});

</script>

<input id="moduleId" type="hidden" value="${moduleId}">
<div style="overflow-y:auto">

			<form id="form-contract-addoredit" method="post" enctype="multipart/form-data">
			<input id="contractId" type="hidden" name="id" value="${contract.id}">
				<div id="contractToolbar" style="padding:5px;">
					<div>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="ContractManage.saveContract()">保存</a>
						<c:if test="${moduleId ge 1}">
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="ContractManage.openSelMaterialWin()">选择物料</a>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="ContractManage.openSelQuoWin()">选择报价单</a>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="ContractManage.deleteMaterial('#datagrid-contractItem-list')">删除</a>
						</c:if>
					</div>
					<div>
					
					<input id="contractType" name="contractType" type="hidden" value="${contract.contractType}">
					
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>合同编码:</td>
							<td><input class="easyui-textbox" id="code" name="code" value="${contract.code}" type="text" data-options="required:true"/></td>
							<td>合同名称:</td>
							<td><input class="easyui-textbox" id="contractName" name="contractName" value="${contract.contractName}" type="text" data-options="required:true"/></td>
							<td>供应商:</td>
							<td>
							<input class="easyui-textbox" name="vendorName" type="text" value="${contract.vendorName}"  required="required" id="vendorName" readonly="readonly"/>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="contractVendor.openWin()"></a>
							<input id="vendorIdFk" name="vendorIdFk" type="hidden" value="${contract.vendorIdFk}">
							</td>
						</tr>
						<tr>
							<td>签订人:</td>
							<td><input class="easyui-textbox" id="signUser" name="signUser" value="${contract.signUser}" type="text" data-options="required:false,editable:false"/></td>
							<td>签订日期:</td>
							<td><input class="easyui-datebox" id="signDateStr" name="signDateStr" value="${contract.signDate}" type="text" data-options="required:true,formatter:df,editable:false"/></td>
							<td>签订地址:</td>
							<td><input class="easyui-textbox" id="signAddress" name="signAddress" value="${contract.signAddress}" type="text" data-options="required:false"/></td>
						</tr>
						<tr>
							<td>签订人电话:</td>
							<td><input class="easyui-textbox" id="signUserPhone" name="signUserPhone" value="${contract.signUserPhone}" type="text" data-options="required:false,validType:'isPhone'"/></td>
							<td colspan="2"><p>--格式如：13888888888，0512-88888888 </p></td>
						</tr>
						
						<tr>
							<td>有效期开始:</td>
							<td><input class="easyui-datebox" id="effrctiveDateStartStr" name="effrctiveDateStartStr" value="${contract.effrctiveDateStart}"  type="text" data-options="required:true,formatter:df,editable:false" /></td>
							<td>有效期结束:</td>
							<td><input class="easyui-datebox" id="effrctiveDateEndStr" name="effrctiveDateEndStr" value="${contract.effrctiveDateEnd}"  type="text" data-options="required:true,formatter:df,editable:false" /></td>
						 	<td>采购组织:	</td>
						 	<td>
								<c:choose>
					  				<c:when test="${contract.auditStatus eq 1}">
										${contract.buyer.name}
									</c:when>
									<c:otherwise>
										<input id="buyerId" class="easyui-combobox" name="buyerId" 
										data-options="url:'${ctx}/manager/vendor/vendorNav/getOrgCombo',
										valueField : 'value',
										textField : 'text',
										required:true,
										editable:false"/>
					
									</c:otherwise>
								</c:choose>
							</td>
						</tr>	
						<tr>					
							<td>申请人:</td>
							<input id="applyUser" name="applyUser" type="hidden" value="${contract.applyUser}">
							<td><input class="easyui-textbox" id="applyUserName" name="applyUserName" value="${contract.applyUserName}" type="text" data-options="required:true,editable:false"/>
								<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="contractUser.openWin(4,'ApplyUser')"></a>
							</td>
							<td></td>
							<td></td>
							<td>合同终止时间       :</td>
							<td><input class="easyui-datebox" id="stopContractTimeStr" name="stopContractTimeStr" value="${contract.stopContractTime}"  type="text" data-options="validType:'isTimeRight',required:ture,formatter:df,editable:false,validType:'isTimeRight'" /></td>
						</tr>
	
						<tr>
							<td colspan="6" align="left">合同终止备注 : <input class="easyui-textbox" id="stopContractRemark" value="${contract.stopContractRemark}" name="stopContractRemark" type="text" data-options="multiline:true" style="height:40px;width: 90%;"/></td>
						</tr>
						

						
						<tr>
							<td colspan="6" align="left">备注: <input class="easyui-textbox" id="remarks" name="remarks" value="${contract.remarks}"  type="text" data-options="multiline:true" style="height:40px;width: 90%;"/></td>
						</tr>
						
						
					<tr>
						<td>附件:</td>
						<td><input type=file id="file" name="planfiles"   /></td>
						
						<%-- <td>附件下载:</td>
						<td><a href="javascript:;" onclick="ContractManage.downFile('${contract.fileName}','${contract.fileUrl}')">  ${contract.fileName}</a></td> --%>
						<td></td>
						<td></td>
						
						<td>合同金额</td>
						<td><input class="easyui-textbox" id="contractPrice" name="contractPrice" value="${contract.contractPrice}" type="text" readonly="readonly"/></td>

					</tr>
					</table>
					</div>
				</div>
				
			<c:if test="${moduleId ge 1}">
				<input type="hidden" name="tableData" id="tableData" />
			</form>
				<table id="datagrid-contractItem-list" title="合同明细" class="easyui-datagrid", data-options="fit:false,url:'${ctx}/manager/contract/contractItem/getContractItemList/${contract.id}',
					iconCls: 'icon-edit',singleSelect: false, pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],method:'post',onClickRow: onClickRow2" >
					<thead><tr>
					<th data-options="field:'id',checkbox:true"></th>   
					<th data-options="field:'materialIdFk',hidden:true"></th>   
					<th data-options="field:'materialCode'">物料编码</th>
                    <th data-options="field:'materialName'">物料名称</th>            
                    <th data-options="field:'sourceBillCode'">询价单号</th>
                    <th data-options="field:'sourceItemPrice'">询价价格</th>
                    <th data-options="field:'sourceItemId'">询价ID</th>
                    <th data-options="field:'taxRate',width:100,editor:{type:'numberbox', options: {required: false}}">税率</th>
					<th data-options="field:'itemQty',width:80,align:'right',editor:{type:'numberbox', options: {required: false}}">数量</th>   
					<th data-options="field:'taxPrice',width:80,align:'right',editor:{type:'numberbox', options: {required: false}}">单价</th>   
					<th data-options="field:'totalPrice',width:80,align:'right',editor:{type:'numberbox', options: {required: false,readonly:true}}">总价</th>   
					

                    
                    <th data-options="field:'remarks',width:100,editor:{type:'textbox', options: {required: false}}">备注</th>
					<th data-options="field:'contractId',hidden:true"></th>   
					</tr></thead>
				</table>  
			
			</c:if>
	</div>
	
		<!-- 选择物料 -->
	<div id="win-material-detail" class="easyui-window" title="物料" 
		data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<table id="datagrid-material-list" title="物料列表" data-options="method:'post',singleSelect:false,toolbar:'#materialListToolbar',
			pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="45%" data-options="field:'code'">物料编码</th>
					<th width="45%" data-options="field:'name'">物料名称</th>
				</tr>
			</thead>
		</table>

		<div id="materialListToolbar">
			<div>
				<a id="link-materialSel-choice" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="ContractManage.choiceMaterial()">选择</a>
			</div>
			<div>
				<form id="form-material-search" method="post">
					物料编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width: 80px;" /> 
					物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width: 80px;" /> 
					<a id="link-materialSel-search" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="ContractManage.searchMaterial()">查询</a>
				</form>
			</div>
		</div>
	</div>
	
			<!-- 选择报价单 -->
	<div id="win-quo-detail" class="easyui-window" title="报价单" 
		data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<table id="datagrid-quo-list" title="报价单列表" data-options="method:'post',singleSelect:false,
				toolbar:'#quoListToolbar',pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="20%" data-options="field:'enquirePriceCode',formatter:function(v,r,i){return r.epPrice.enquirePriceCode}">询价单号</th>
					<th width="20%" data-options="field:'projectName',formatter:function(v,r,i){return r.epPrice.projectName}">询价名称</th>
					
					<th width="20%" data-options="field:'materialCode',formatter:function(v,r,i){return r.epMaterial.materialCode}">物料编码</th>
					<th width="20%" data-options="field:'materialName',formatter:function(v,r,i){return r.epMaterial.materialName}">物料名称</th>
					<th width="10%" data-options="field:'negotiatedPrice'">询价价格</th>
					
					<th width="10%" data-options="field:'materialId',formatter:function(v,r,i){return r.epMaterial.materialId}">物料ID</th>
				</tr>
			</thead>
		</table>

		<div id="quoListToolbar">
			<div>
				<a id="link-quo-choice" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="ContractManage.choiceQuo()">选择</a>
			</div>
			<div>
				<form id="form-quo-search" method="post">
					询价单号：<input type="text" name="search-LIKE_epPrice.enquirePriceCode" class="easyui-textbox" style="width: 80px;" /> 
					询价名称：<input type="text" name="search-LIKE_epPrice.projectName" class="easyui-textbox" style="width: 80px;" /> 
					<a id="link-quoSel-search" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="ContractManage.searchQuo()">查询</a>
				</form>
			</div>
		</div>
	</div>
	
	
		<!-- 选择供应商 -->
	<div id="win-proVendor-detail" title="供应商" class="easyui-window" data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<table id="datagrid-proVendor-list" title="供应商列表" data-options="method:'post',singleSelect:true,toolbar:'#proVendorListToolbar',
				fit:true,pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="45%" data-options="field:'code'">供应商编码</th>
					<th width="45%" data-options="field:'name'">供应商名称</th>
				</tr>
			</thead>
		</table>

		<div id="proVendorListToolbar" style="padding: 5px;">
			<div>
				<a id="link-vendorSel-choice" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="contractVendor.choice()">选择</a>
			</div>
			<div>
				<form id="form-proVendor-search" method="post">
					供应商编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width: 80px;" /> 
					供应商名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width: 80px;" /> 
					<a id="link-vendorSel-search" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="contractVendor.searchVendor()">查询</a>
				</form>
			</div>
		</div>
	</div>
	
	<!-- 选择用户 -->
	<div id="win-user-detail" title="用户" class="easyui-window" data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<table id="datagrid-user-list" title="用户列表" data-options="method:'post',singleSelect:true,toolbar:'#userListToolbar',
				fit:true,pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
			<thead>
			<input type="hidden" id="userType" value=""/>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="45%" data-options="field:'loginName'">用户编码</th>
					<th width="45%" data-options="field:'name'">用户名称</th>
				</tr>
			</thead>
		</table>

		<div id="userListToolbar" style="padding: 5px;">
			<div>
				<a  href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="contractUser.choice()">选择</a>
			</div>
			<div>
				<form id="form-user-search" method="post">
					用户编码：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width: 80px;" /> 
					用户名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width: 80px;" /> 
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="contractUser.searchUser()">查询</a>
				</form>
			</div>
		</div>
	</div>

  <script type="text/javascript">
	function df(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+m+'-'+d;
	} 
	
	
	

 	
  </script>
</body>
</html>
