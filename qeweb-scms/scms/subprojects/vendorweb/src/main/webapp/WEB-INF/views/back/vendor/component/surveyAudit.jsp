<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<body>
<div class="container-fluid">
	<div class="container-fluid">
      <div class="pull-left" id="btn-group-audit-${surveyCfgId}">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-application_go'" onclick="VendorAdmittance.audit(${vendorSurveyCfg.id})">审核</a>
      </div>
      <!-- 显示历史 -->
      <div class="pull-right" id="group-change-${surveyCfgId}">
		<table id="survey-his-${surveyCfgId}" class="easyui-datagrid" data-options="title:'版本信息',
		width:500,
		collapsible:true,collapsed:true,rownumbers:true,
		url:'${ctx}/manager/vendor/admittance/getSurveyInfoAuditHis',
		onExpand : function(){VendorAdmittance.getHis('survey-his-${surveyCfgId}');},
		queryParams:{'ctId':${surveyCfgId},'currentId':'${surveyBase.id}'}">
		<thead>
		<tr>
		<th data-options="field:'id',hidden:true">ID</th>
		<th data-options="field:'versionNO',formatter:SurveyView.viewHisFmt">版本号</th>
		<th data-options="field:'auditUser'">审核人</th>
		<th data-options="field:'auditReason'">审核原因</th>
		<th data-options="field:'lastUpdateTime',formatter:SurveyView.viewAuditTime">审核时间</th>
		<th data-options="field:'auditStatus',formatter:function(v){return StatusRender.render(v,'audit',true)}">审核状态</th>
		<th data-options="field:'currentVersion',formatter:function(v){return StatusRender.render(v,'yesOrNo',true);}">当前版本</th>
		</tr>
		</thead>
		</table>
      </div>
    </div>
    <div class="container-fluid">
	<form id="survey-${surveyCfgId}" enctype="multipart/form-data" method="post">
       <input type="hidden" itemId="surveyBase" name="surveyBase"/>
       <input type="hidden" itemId="surveyCfgId" name="surveyCfgId" value="${surveyCfgId}"/>
       <div style="display: none;" itemId="fileCt">
       </div>
    </form>
    <div id="survey-${surveyCfgId}-ct" class="ct">
        <input type="hidden" itemId="id" name="id" value="${surveyBase.id}"/>
        <input type="hidden" itemId="surveyCfgId" value="${vendorSurveyCfg.id}"/>
        <input type="hidden" itemId="submitStatus" value="${vendorSurveyCfg.submitStatus}"/>
        <input type="hidden" itemId="auditStatus" value="${vendorSurveyCfg.auditStatus}"/>
        <input type="hidden" itemId="templateId" value="${templateId}"/>
        <input type="hidden" itemId="templatePath" value="${templatePath}"/>
        <input type="hidden" itemId="templateCode" value="${templateCode}"/>
		<c:forEach items="${ctList}" var="ct" varStatus="ctStatus">
		  <c:if test="${ct.type=='0'}">
              <div class="text-right">
              <fieldset type="frm" name="ct" id="${ct.id}">
              <c:if test="${ct.title!=null && ct.title!=''}">
			  <legend class="text-left"><span class="label label-primary"><samll>${ct.title}</samll></span></legend>
              </c:if>
<!-- 						如果是form,循环字段，3个为一行 -->
			  <c:forEach items="${ct.colList}" var="frmField" varStatus="frmFieldStatus">
			    <c:if test="${frmFieldStatus.first || frmFieldStatus.count%3==1}">
			    <div class="row row-form">
			    </c:if>
			     <div class="col-md-4">
					 <c:if test="${frmField.required=='true'}">
					   <span class="text-required">*</span>
					 </c:if>
					 ${frmField.label}:${frmField.value}
				</div>
				<c:if test="${(frmFieldStatus.count)%3==0 || frmFieldStatus.last}">
				 </div>
				</c:if>
			  </c:forEach>
			  </fieldset>
			  </div>			              
		  
		  </c:if>		
          <c:if test="${ct.type=='1'}">
				<div>
				 <fieldset>
				  <c:if test="${ct.title!=null && ct.title!=''}">
				  <legend class="text-left"><span class="label label-primary"><samll>${ct.title}</samll></span></legend>
	              </c:if>
					<table name="ct" type="tb" class="table table-bordered table-condensed" id="${ct.id}">
					<thead class="datagrid-header">
					<tr class="datagrid-cell">
					  <th>序号</th>
				      <c:forEach items="${ct.colList}" var="item">
						<th  thType="${item.type}" sum="${item.sum}" varName="${item.varName}" quoteFormula="${item.formula}" name="${item.name}" required="${item.required}">
						<c:if test="${item.required=='true'}">
						  <span class="text-required">*</span>
						</c:if>
						<span title="${item.tip}">${item.label}</span>
						</th>
					  </c:forEach>
					</tr>
					</thead>
					<tbody>
						<c:forEach items="${ct.trList}" var="tr" varStatus="trStatus">
						<tr fixed="${tr.fixed}">
						  <td>
						    ${trStatus.count}
						  </td>
							<c:forEach items="${tr.tdList}" var="td" varStatus="tdStatus">
								<td>
								 <c:if test="${ct.colList[tdStatus.index].type=='text' || ct.colList[tdStatus.index].type=='label' || ct.colList[tdStatus.index].type=='day'}">
									${td}
								 </c:if>
								 <c:if test="${ct.colList[tdStatus.index].type=='select'}">
									<input class="select-url" url="${ct.colList[tdStatus.index].url}" type="hidden" value="${td}"/><span class="select-text"></span>
								 </c:if>
								 <c:if test="${ct.colList[tdStatus.index].type=='selectrel'}">
									${td}
								 </c:if>
								 <c:if test="${ct.colList[tdStatus.index].type=='file'}">
								   <c:forEach items="${fn:split(td,'/')}" var="fileName" varStatus="fileStatus">
									  <c:if test="${fileStatus.last}">
									  <c:set var="exsitFileName" value="${fileName}"></c:set>
									  </c:if>
									</c:forEach>
									<label name="${ct.colList[tdStatus.index].name}-label">
									<c:if test="${exsitFileName!='null' && exsitFileName!=''}"><a href="javascript:;" onclick="File.download('${td}')">${exsitFileName}</a></c:if></label>
								 </c:if>
								</td>
							</c:forEach>
						</tr>
						</c:forEach>
					</tbody>
<!-- 						判断是否需要小计  -->
					<c:if test="${matQuoteItemMap.value.tfoot!=null}">
					<tfoot> <tr>
					<c:forEach items="${matQuoteItemMap.value.tfoot}" var="footField">
					  <td 
					  <c:if test="${footField.colspan!=null && footField.colspan!=''}"> colspan="${footField.colspan+1}"</c:if>
					   
					  >
					  <c:if test="${footField.name!=null && footField.name!='' && footField.sumId!=null && footField.sumId!='' && footField.isFee!=null && footField.isFee!=''}">
					    <input name="${footField.name}" sumId="${footField.sumId}" isFee="${footField.isFee}" type="hidden" value="${footField.value}"/>
					    <label name="${footField.name}-label">${footField.value}</label>
					  </c:if>
					  <c:if test="${footField.label!=null && footField.label!=''}">
					    ${footField.label}
					  </c:if>
					  </td>
					</c:forEach>
					</tr></tfoot>
					</c:if>
					
					</table>
					</fieldset>
					</div>
		  </c:if>			
		</c:forEach>
	</div>
  </div>	
</div>
<script>
VendorAdmittance.auditSurveyInit('${surveyCfgId}');
SurveyView.initConvertField('#survey-${surveyCfgId}-ct');
</script>
</body>