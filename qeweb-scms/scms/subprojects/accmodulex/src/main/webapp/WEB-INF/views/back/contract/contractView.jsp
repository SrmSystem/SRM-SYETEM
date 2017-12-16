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
<script type="text/javascript">
function optFmt(v,r,i){
	return ''; 	
	}




</script>
			<form id="form-lingshi" method="post">
			</form>
			
<input id="moduleId" type="hidden" value="${moduleId}">
<div style="overflow-y:auto">
			<form id="form-contract-addoredit" method="post" enctype="multipart/form-data">
			<input id="contractId" type="hidden" name="id" value="${contract.id}">
				<div id="contractToolbar" style="padding:5px;">
					<div>
						<c:if test="${moduleId gt 0}">
								<a href="javascript:;" onclick="ContractContent.openItemCtp(${contract.id})" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">合同预览</a>
<%-- 								<a href="javascript:;" onclick="ContractModuleItem.createPdf(${module.id},1)" class="easyui-linkbutton" data-options="plain:true">生成PDF</a> --%>
						</c:if>
						<c:if test="${contract.auditStatus eq 0}">
							<a href="javascript:;" onclick="ContractManage.checkContract('${contract.id}','${contract.isPdf}')" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">提交审核</a>
						</c:if>
						<c:if test="${contract.publishStatus eq 0}">
							<c:if test="${contract.auditStatus eq 1}">
								<a href="javascript:;" onclick="ContractManage.publishContract('${contract.id}')" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">发布</a>
							</c:if>
						</c:if>
						<c:if test="${contract.hasSealAttchement eq 1}">
							<c:if test="${contract.sealAttConfirmStatus eq 0}">
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="ContractManage.effectiveContract('${contract.id}','effectivePass')">确认生效</a>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="ContractManage.effectiveContract('${contract.id}','effectiveReject')">盖章附件退回</a>
							</c:if>
						</c:if>
						
					</div>
					<div>
					
					<input id="contractType" name="contractType" type="hidden" value="${contract.contractType}">
						<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>合同编码:</td>
							<td><input class="easyui-textbox" id="code" name="code" value="${contract.code}" type="text" data-options="required:true" disabled="disabled"/></td>
							<td>合同名称:</td>
							<td><input class="easyui-textbox" id="contractName" name="contractName" value="${contract.contractName}" type="text" data-options="required:true" disabled="disabled"/></td>
							<td>供应商:</td>
							<td>
							<input class="easyui-textbox" name="vendorName" type="text" value="${contract.vendorName}"  required="required" id="vendorName" readonly="readonly" disabled="disabled"/>
							</td>
						</tr>
						<tr>
							<td>签订人:</td>
							<td><input class="easyui-textbox" id="signUser" name="signUser" value="${contract.signUser}" type="text" data-options="required:true" disabled="disabled"/></td>
							<td>签订日期:</td>
							<td><input class="easyui-datebox" id="signDateStr" name="signDateStr" value="${contract.signDate}" type="text" data-options="required:true,formatter:df" disabled="disabled"/></td>
							<td>签订地址:</td>
							<td><input class="easyui-textbox" id="signAddress" name="signAddress" value="${contract.signAddress}" type="text" data-options="required:false" disabled="disabled"/></td>
						</tr>
						<tr>
							<td>签订人电话:</td>
							<td><input class="easyui-textbox" id="signUserPhone" name="signUserPhone" value="${contract.signUserPhone}" type="text" data-options="required:true" disabled="disabled"/></td>
							<td>有效期开始:</td>
							<td><input class="easyui-datebox" id="effrctiveDateStartStr" name="effrctiveDateStartStr" value="${contract.effrctiveDateStart}"  type="text" data-options="required:true,formatter:df" disabled="disabled"/></td>
							<td>有效期结束:</td>
							<td><input class="easyui-datebox" id="effrctiveDateEndStr" name="effrctiveDateEndStr" value="${contract.effrctiveDateEnd}"  type="text" data-options="required:true,formatter:df" disabled="disabled"/></td>
						</tr>
						
						<tr>
							<td>申请人:</td>
							<td><input class="easyui-textbox" id="applyUserName" name="applyUserName" value="${contract.applyUserName}" type="text" data-options="required:true" disabled="disabled"/></td>
							<td>质保金:</td>
							<td><input class="easyui-textbox" id="attr_2" name="attr_2" value="${contract.attr_2}" type="text" data-options="required:false" disabled="disabled"/></td>
							<td>返利:</td>
							<td><input class="easyui-textbox" id="attr_3" name="attr_3" value="${contract.attr_3}" type="text" data-options="required:false" disabled="disabled"/></td>
						</tr>
						
						<tr>
							<td>结算方式 :</td>
							<td><input class="easyui-textbox" id="attr_10" name="attr_10" value="${contract.attr_10}" type="text" data-options="required:false" disabled="disabled"/></td>
							<td>委托代理人:</td>
							<td><input class="easyui-textbox" id="attr_15" name="attr_15" value="${contract.attr_2}" type="text" data-options="required:false" disabled="disabled"/></td>
							<td>合同终止时间       :</td>
							<td><input class="easyui-datebox" id="stopContractTimeStr" name="stopContractTimeStr" value="${contract.stopContractTime}"  type="text" data-options="required:false,formatter:df" disabled="disabled"/></td>
						</tr>
	
						<tr>
							<td colspan="6" align="left">合同终止备注 : <input class="easyui-textbox" id="stopContractRemark" value="${contract.stopContractRemark}" name="stopContractRemark" disabled="disabled" type="text" data-options="multiline:true" style="height:40px;width: 90%;"/></td>
						</tr>
						<tr>
							<td colspan="6" align="left">备注: <input class="easyui-textbox" id="remarks" name="remarks" value="${contract.remarks}"  type="text" disabled="disabled" data-options="multiline:true" style="height:40px;width: 90%;"/></td>
						</tr>	
						<tr>
							<td>附件:</td>
							<td></td>
							
							<td>附件下载:</td>
							<td><a href="javascript:;" onclick="ContractManage.downFile('${contract.fileName}','${contract.fileUrl}')">  ${contract.fileName}</a></td>
							
							<td>合同金额</td>
							<td><input class="easyui-textbox" id="contractPrice" name="contractPrice" value="${contract.contractPrice}" type="text" readonly="readonly" disabled="disabled"/></td>	
						</tr>
						<c:if test="${contract.contractType eq 10}">
								<tr style="visibility:hidden">
									<td>合同款项方向:</td>
									<td><input class="easyui-textbox" id="attr_1" name="attr_1" value="${contract.attr_1}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>用印种类:</td>
									<td><input class="easyui-textbox" id="attr_4" name="attr_4" value="${contract.attr_4}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>招投标:</td>
									<td><input class="easyui-textbox" id="attr_5" name="attr_5" value="${contract.attr_5}" type="text" data-options="required:false" disabled="disabled"/></td>
								</tr>
								<tr style="visibility:hidden">
									<td>预算及立项:</td>
									<td><input class="easyui-textbox" id="attr_6" name="attr_6" value="${contract.attr_6}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>办理授权:</td>
									<td><input class="easyui-textbox" id="attr_7" name="attr_7" value="${contract.attr_7}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>合同变更:</td>
									<td><input class="easyui-textbox" id="attr_8" name="attr_8" value="${contract.attr_8}" type="text" data-options="required:false" disabled="disabled"/></td>
								</tr>
								<tr style="visibility:hidden">
									<td>仓储费:</td>
									<td><input class="easyui-textbox" id="attr_9" name="attr_9" value="${contract.attr_9}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>付款方式:</td>
									<td><input class="easyui-textbox" id="attr_16" name="attr_16" value="${contract.attr_16}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>驳回建议:</td>
									<td><input class="easyui-textbox" id="attr_11" name="attr_11" value="${contract.attr_11}" type="text" data-options="required:false" disabled="disabled"/></td>

								</tr>
								<tr style="visibility:hidden">
									<td>收支条件:</td>
									<td><input class="easyui-textbox" id="attr_12" name="attr_12" value="${contract.attr_12}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>收支时间:</td>
									<td><input class="easyui-textbox" id="attr_13" name="attr_13" value="${contract.attr_13}" type="text" data-options="required:false" disabled="disabled"/></td>
									<td>收支比例:</td>
									<td><input class="easyui-textbox" id="attr_14" name="attr_14" value="${contract.attr_14}" type="text" data-options="required:false" disabled="disabled"/></td>
														
								</tr>
						</c:if>
						
						<c:if test="${contract.contractType eq 20}">
							<tr style="visibility:hidden">
								<td>产地:</td>
								<td><input class="easyui-textbox" id="place" name="place" value="${contract.place}" type="text" data-options="required:false" disabled="disabled"/></td>
								<td>交货时间       :</td>
								<td><input class="easyui-datebox" id="deliveryTimeStr" name="deliveryTimeStr" value="${contract.deliveryTime}"  type="text" data-options="required:true,formatter:df" disabled="disabled"/></td>
								<td>保修标准:</td>
								<td><input class="easyui-textbox" id="attr_20" name="attr_20" value="${contract.attr_20}" type="text" data-options="required:false" disabled="disabled"/></td>
							</tr>
						</c:if>
						
						
						<tr style="visibility:hidden">
							<td>
							<c:if test="${contract.contractType eq 10}">
							会签人1:
							</c:if>
							<c:if test="${contract.contractType eq 20}">
							采购主管:
							</c:if>
							</td>
							<td><input class="easyui-textbox" id="signUserOneName" name="signUserOneName" value="${contract.signUserOneName}" type="text" disabled="disabled" data-options="required:false"/>
							</td>
														<td>
							<c:if test="${contract.contractType eq 10}">
							会签人2:
							</c:if>
							<c:if test="${contract.contractType eq 20}">
							核价员:
							</c:if>
							</td>
							<td><input class="easyui-textbox" id="signUserTwoName" name="signUserTwoName" value="${contract.signUserTwoName}" disabled="disabled" type="text" data-options="required:false"/>
							</td>
														<td>
							<c:if test="${contract.contractType eq 10}">
							会签人3:
							</c:if>
							<c:if test="${contract.contractType eq 20}">
							采购部长:
							</c:if>
							</td>
							<td><input class="easyui-textbox" id="signUserThreeName" name="signUserThreeName" value="${contract.signUserThreeName}" type="text" disabled="disabled" data-options="required:false"/>
							</td>
						</tr>
						
						
						
							<tr style="visibility:hidden">
							<td>交货期 :</td>
							<td><input class="easyui-datebox" id="attr_18" name="attr_18" value="${contract.attr_18}"  type="text" data-options="required:false,formatter:df" disabled="disabled"/></td>
							
							<c:if test="${contract.contractType eq 10}">
							<td>运费：</td>
							<td><input class="easyui-textbox" id="attr_19" name="attr_19" value="${contract.attr_19}" type="text" data-options="required:false" disabled="disabled"/></td>
							<td>合同执行时间：</td>
							<td><input class="easyui-datebox" id="attr_17" name="attr_17" value="${contract.attr_17}"  type="text" data-options="required:false,formatter:df" disabled="disabled"/></td>
							</c:if>
							<c:if test="${contract.contractType eq 20}">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							</c:if>
						</tr>
					</table>
					</div>
				</div>
				
			<c:if test="${moduleId ge 1}">
				
				<input type="hidden" name="tableData" id="tableData" />
			</form>
			
				<table id="datagrid-contractItem-list" title="合同明细" class="easyui-datagrid", 
					data-options="fit:false,url:'${ctx}/manager/contract/contractItem/getContractItemList/${contract.id}',
					iconCls: 'icon-edit',
					singleSelect: false, pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
					method:'post'" >
					<thead><tr>
					<th width="50px" data-options="field:'id',checkbox:true"></th>   
					<th width="140px" data-options="field:'materialCode'">物料编码</th>
                    <th width="140px" data-options="field:'materialName'">物料名称</th>
                                         <th data-options="field:'sourceBillCode'">询价单号</th>
                     <th width="140px" data-options="field:'sourceItemPrice'">询价价格</th>
                           <th width="140px" data-options="field:'taxRate',width:100,editor:{type:'numberbox', options: {required: false}}">税率</th>


					<th width="140px" data-options="field:'itemQty',width:80,align:'right',editor:{type:'numberbox', options: {required: false}}">数量</th>   
					<th width="140px" data-options="field:'taxPrice',width:80,align:'right',editor:{type:'numberbox', options: {required: false}}">单价</th>   
					<th width="140px" data-options="field:'totalPrice',width:80,align:'right',editor:{type:'numberbox', options: {required: false,readonly:true}}">总价</th>   
					
					<c:if test="${contract.contractType eq 10}">
	                     <th width="140px" data-options="field:'attr_1',width:100,editor:{type:'textbox', options: {required: false}}">年差价</th>
	                      <th width="140px" data-options="field:'attr_2',width:100,editor:{type:'textbox', options: {required: false}}">上一年度流量</th>
	                       <th width="140px" data-options="field:'attr_3',width:100,editor:{type:'textbox', options: {required: false}}">上一年度价格</th>
	                        <th width="140px" width="140px" data-options="field:'attr_4',width:100,editor:{type:'textbox', options: {required: false}}">本年度结算价格</th>
	                         <th width="140px" data-options="field:'attr_5',width:100,editor:{type:'textbox', options: {required: false}}">实际降幅</th>
	                          <th width="140px" data-options="field:'attr_6',width:100,editor:{type:'textbox', options: {required: false}}">上一年度供货源</th>
                   			  <th width="140px" data-options="field:'attr_7',width:100,editor:{type:'textbox', options: {required: false}}">订单号</th>
                    </c:if>
                    
                           <th width="140px" data-options="field:'remarks',width:100,editor:{type:'textbox', options: {required: false}}">备注</th>
					<th width="140px" data-options="field:'contractId',hidden:true"></th>   
					</tr></thead>
				</table>  
			
			</c:if>
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
