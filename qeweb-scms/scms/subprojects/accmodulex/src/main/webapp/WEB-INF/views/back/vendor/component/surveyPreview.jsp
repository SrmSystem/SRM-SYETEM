<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<body>
	<div class="container-fluid">
	<form id="survey-${surveyId}" enctype="multipart/form-data" method="post">
       <input type="hidden" itemId="surveyBase" name="surveyBase"/>
       <input type="hidden" itemId="surveyCfgId" name="surveyCfgId" value="${surveyId}"/>
    <div id="survey-${surveyId}-ct" class="ct">
        <input type="hidden" itemId="surveyBaseId" name="id" value="${surveyBase.id}"/>
        <input type="hidden" itemId="surveyCfgId" value="${vendorSurveyCfg.id}"/>
        <input type="hidden" itemId="submitStatus" value="${vendorSurveyCfg.submitStatus}"/>
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
<!-- 如果是form,循环字段，3个为一行 -->
			  <c:forEach items="${ct.colList}" var="frmField" varStatus="frmFieldStatus">
			    <c:if test="${frmFieldStatus.first || frmFieldStatus.count%3==1}">
			    <div class="row row-form">
			    </c:if>
			       <div class="col-md-4">
					<c:if test="${frmField.required=='true'}">
							  <span class="required">*</span>
							</c:if>
							${frmField.label}:
							<c:choose>
								<c:when test="${frmField.type=='checkbox'}">
									<c:forEach items="${frmField.dataSource}" var="dataMap">
									<c:forEach items="${dataMap}" var="data">
										<label class="checkbox inline">
										<input 
										required="${frmField.required}" 
										type="checkbox" name="${frmField.name}" value="${data.key}"
										<c:if test="${frmField.value!=null && frmField.value!=''}">
										<c:forEach items="${fn:split(frmField.value,';')}" var="cklb">
										  <c:if test="${cklb==data.value}">
										   checked = "true"
										 </c:if>
										</c:forEach>
										</c:if>
										
										/>${data.value}
										</label>
									</c:forEach>
									</c:forEach>
								</c:when>
								<c:when test="${frmField.type=='label'}">
									<input type="hidden"
									required="${frmField.required}"
									varName="${frmField.varName}"
									<c:if test="${frmField.formula!=null && frmField.formula!=''}">
									quoteFormula="${frmField.formula}"
									pType="frm"
									</c:if>
									sum="${frmField.sum}"
									 type="${frmField.type}" name="${frmField.name}" value="${frmField.value}"/><label name="${frmField.name}-label" class="label">${frmField.value}</label>
								</c:when>
								<c:otherwise>
									<input
									class="textbox textbox-text"
									style="width:160px;height:22px;"
									required="${frmField.required}" 
									varName="${frmField.varName}"
									quoteFormula="${frmField.formula}"
									pType="frm"
									sum="${frmField.sum}"
									<c:if test="${frmField.id!=null && frmField.id!=''}">
									id="${frmField.id}" 
									</c:if>
									<c:if test="${frmField.tip!=null && frmField.tip!=''}">
									title="${frmField.tip}" 
									</c:if>
									type="${frmField.type}" name="${frmField.name}" class="input1" value="${frmField.value}"/>
								</c:otherwise>
							</c:choose>
							</div>
						<c:if test="${(frmFieldStatus.count)%3==0 || frmFieldStatus.last}">
						</div>
						</c:if>
					</c:forEach>
					</filedset>
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
					<tr class="datagrid-header-row">
					  <th class="datagrid-cell"><input type="checkbox" class="tableCheck"/></th>
				      <c:forEach items="${ct.colList}" var="item">
						<th class="datagrid-cell"  thType="${item.type}" sum="${item.sum}" varName="${item.varName}" quoteFormula="${item.formula}" name="${item.name}" required="${item.required}">
						<c:if test="${item.required=='true'}">
						  <span class="required">*</span>
						</c:if>
						<span title="${item.tip}">${item.label}</span>
						</th>
					  </c:forEach>
					</tr>
					</thead>
					<tbody>
						<c:forEach items="${ct.trList}" var="tr">
						<tr fixed="${tr.fixed}">
						  <td>
						    <c:if test="${tr.fixed==null || tr.fixed!='1'}">
						      <input name="rowCk" type="checkbox" id="${tr.id}-<%=System.currentTimeMillis()%>"/>
						    </c:if>
						  
						  </td>
							<c:forEach items="${tr.tdList}" var="td" varStatus="tdStatus">
								<td>
								<c:if test="${ct.colList[tdStatus.index].type=='text'}">
									<input name="${ct.colList[tdStatus.index].name}" 
									required="${ct.colList[tdStatus.index].required}" 
									varName="${ct.colList[tdStatus.index].varName}"
									quoteFormula="${ct.colList[tdStatus.index].formula}"
									pType="tr"
									sum="${ct.colList[tdStatus.index].sum}"
									class="input1" style="width:98%!important" type="text" value="${td}" />
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='label'}">
									<input type="hidden" 
									name="${ct.colList[tdStatus.index].name}"
									required="${ct.colList[tdStatus.index].required}" 
									varName="${ct.colList[tdStatus.index].varName}"
									quoteFormula="${ct.colList[tdStatus.index].formula}"
									sum="${ct.colList[tdStatus.index].sum}"
									pType="tr"
									value = "${td}"
									/><label name="${ct.colList[tdStatus.index].name}-label">${td}</label>
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='day'}">
									<input name="${ct.colList[tdStatus.index].name}" 
									required="${ct.colList[tdStatus.index].required}" 
									varName="${ct.colList[tdStatus.index].varName}"
									quoteFormula="${ct.colList[tdStatus.index].formula}"
									pType="tr"
									sum="${ct.colList[tdStatus.index].sum}"
									style="height:30px;" class="Wdate form-control input-sm" onFocus="WdatePicker({isShowClear:false,readOnly:true})" type="text" value="${td}" />
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='file'}">
								   <c:forEach items="${fn:split(td,'/')}" var="fileName" varStatus="fileStatus">
									  <c:if test="${fileStatus.last}">
									  <c:set var="exsitFileName" value="${fileName}"></c:set>
									  </c:if>
									</c:forEach>
									<input type="hidden" 
									id = "trFiles-${tr.id}-v"
									name="${ct.colList[tdStatus.index].name}"
									required="${ct.colList[tdStatus.index].required}" 
									varName="${ct.colList[tdStatus.index].varName}"
									quoteFormula="${ct.colList[tdStatus.index].formula}"
									sum="${ct.colList[tdStatus.index].sum}"
									pType="tr"
									value = "${exsitFileName}"
									/>
									<input type="file" 
									onchange="setTrFileV(this)" vid="trFiles-${tr.id}-v" name="trFiles"
									/>
									<label name="${ct.colList[tdStatus.index].name}-label">
									<a href="javascript:;" onclick="File.download('${td}')">${exsitFileName}</a></label>
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='down'}">
								   <c:forEach items="${fn:split(td,'/')}" var="fileName" varStatus="fileStatus">
									  <c:if test="${fileStatus.last}">
									  <c:set var="exsitFileName" value="${fileName}"></c:set>
									  </c:if>
									</c:forEach>
									<input type="hidden" 
									id = "trFiles-${tr.id}-v"
									name="${ct.colList[tdStatus.index].name}"
									required="${ct.colList[tdStatus.index].required}" 
									varName="${ct.colList[tdStatus.index].varName}"
									quoteFormula="${ct.colList[tdStatus.index].formula}"
									sum="${ct.colList[tdStatus.index].sum}"
									pType="tr"
									value = "${td}"
									/>
									<label name="${ct.colList[tdStatus.index].name}-label">
									<a href="javascript:;" onclick="File.download('${td}')">${exsitFileName}</a></label>
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
	  </form>
	</div>


