<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<body>
	<div class="container-fluid">
	  <div class="pull-left" id="btn-group-save-${surveyCfgId}">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="Survey.save('survey-${surveyCfgId}')">保存</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Survey.submit('survey-${surveyCfgId}')">提交</a>
      </div>
      <!-- 变更按钮组 -->
      <div class="pull-left hidden" id="btn-group-change-${surveyCfgId}">
        <a class="easyui-linkbutton" itemId="change-apply" data-options="iconCls:'icon-bullet_wrench_red'" onclick="Survey.change('${surveyCfgId}')">变更</a>
      </div>
      <!-- 显示历史 -->
      <div class="pull-right" id="group-change-${surveyCfgId}">
		<table id="survey-his-${surveyCfgId}" class="easyui-datagrid" data-options="title:'信息版本',
		width:500,
		collapsible:true,collapsed:true,rownumbers:true,
		url:'${ctx}/manager/vendor/admittance/getSurveyInfoHis',
		onExpand : function(){Survey.getHis('survey-his-${surveyCfgId}');},
		queryParams:{'ctId':${vendorSurveyCfg.id},'currentId':'${surveyBase.id}'}">
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
       <input type="hidden" itemId="rootId" name="rootId" value="${rootId}"/>
       <input type="hidden" itemId="surveyBase" name="surveyBase"/>
       <input type="hidden" itemId="surveyCfgId" name="surveyCfgId" value="${surveyCfgId}"/>       
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
<!-- 如果是form,循环字段，3个为一行 -->
			  <c:forEach items="${ct.colList}" var="frmField" varStatus="frmFieldStatus">
			    <c:if test="${frmFieldStatus.first || frmFieldStatus.count%3==1}">
			    <div class="row row-form">
			    </c:if>
			       <div class="col-md-4">
							${frmField.label}:
							<c:choose>
								<c:when test="${frmField.type=='checkbox'}">
									<c:forEach items="${frmField.dataSource}" var="dataMap">
									<c:forEach items="${dataMap}" var="data">
										<label class="checkbox inline">
										<input 
										<c:if test="${frmField.required=='true'}">
										 required="${frmField.required}" 
										</c:if>
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
									<c:if test="${frmField.required=='true'}">
									required="${frmField.required}" 
									</c:if>
									varName="${frmField.varName}"
									<c:if test="${frmField.formula!=null && frmField.formula!=''}">
									formula="${frmField.formula}"
									pType="frm"
									</c:if>
									sum="${frmField.sum}"
									 type="${frmField.type}" name="${frmField.name}" value="${frmField.value}"/><label name="${frmField.name}-label" class="label">${frmField.value}</label>
								</c:when>
								<c:otherwise>
								<span class="textbox">
									<input
									style="width:140px;height:22px;"
									<c:if test="${frmField.required=='true'}">
									required="${frmField.required}" 
									</c:if>
									varName="${frmField.varName}"
									formula="${frmField.formula}"
									pType="frm"
									sum="${frmField.sum}"
									<c:if test="${frmField.id!=null && frmField.id!=''}">
									id="${frmField.id}" 
									</c:if>
									<c:if test="${frmField.tip!=null && frmField.tip!=''}">
									title="${frmField.tip}" 
									</c:if>
									type="${frmField.type}" name="${frmField.name}" class="textbox-text textbox-text-ex" value="${frmField.value}"/>
								</span>	
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
					<caption>
					<div class="pull-left tb-btn-group-${surveyCfgId}">
					  <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" href="javascript:;" onclick="addTbRow('${ct.id}','${surveyCfgId}')"></a>
					  <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-delete'" href="javascript:;" onclick="delTbRow('${ct.id}')"></a>
					</div>
					</caption>
					<thead class="datagrid-header">
					<tr class="datagrid-cell">
					  <th><input id="tableCheck${ct.id}" type="checkbox" class="tableCheck"/></th>
				      <c:forEach items="${ct.colList}" var="item">
						<th  thType="${item.type}" url="${item.url}" dataSource="${item.dataSource}" sum="${item.sum}" varName="${item.varName}" formula="${item.formula}" name="${item.name}" 
						<c:if test="${item.required=='true'}">
							required="${item.required}" 
						</c:if>
						">
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
						    <c:if test="${tr.fixed==null || tr.fixed!='true'}">
						      <input name="rowCk" type="checkbox" id="${tr.id}-<%=System.currentTimeMillis()%>" onchange="tableChecks('${ct.id}')"/>
						    </c:if>
						  
						  </td>
							<c:forEach items="${tr.tdList}" var="td" varStatus="tdStatus">
								<td>
								<c:if test="${ct.colList[tdStatus.index].type=='text'}">
								<span style="width:140px;height:22px;"  class="textbox">
									<input name="${ct.colList[tdStatus.index].name}"
									<c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if> 
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									pType="tr"
									sum="${ct.colList[tdStatus.index].sum}"
									class="textbox-text textbox-text-ex"
									style="width:140px;"
									 type="text" value="${td}" />
								</span>
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='label'}">
									<input type="hidden" 
									name="${ct.colList[tdStatus.index].name}"
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									sum="${ct.colList[tdStatus.index].sum}"
									pType="tr"
									value = "${td}"
									/><label name="${ct.colList[tdStatus.index].name}-label">${td}</label>
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='day'}">
								  <span class="textbox">
									<input name="${ct.colList[tdStatus.index].name}" 
									<c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if> 
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									pType="tr"
									sum="${ct.colList[tdStatus.index].sum}"
									style="width:140px;line-height: normal;" class="Wdate textbox-text" onFocus="WdatePicker({readOnly:true,dateFmt:'${ct.colList[tdStatus.index].dayFmt}'})" type="text" value="${td}" />
								  </span>
								  
								</c:if>
								<c:if test="${ct.colList[tdStatus.index].type=='file'}">
								   <c:forEach items="${fn:split(td,'/')}" var="fileName" varStatus="fileStatus">
									  <c:if test="${fileStatus.last}">
									  <c:set var="exsitFileName" value="${fileName}"></c:set>
									  </c:if>
									</c:forEach>
									<span class="textbox" style="width:200px;height:22px;">
									<input type="hidden" 
									id = "trFiles-${tr.id}-v-${trStatus.index}-${tdStatus.index}"
									name="${ct.colList[tdStatus.index].name}"
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									sum="${ct.colList[tdStatus.index].sum}"
									pType="tr"
									value = "${exsitFileName}"
									/>
									<input type="file" style="height: 22px;"
									<c:if test="${exsitFileName=='null' && exsitFileName==''}">
										<c:if test="${ct.colList[tdStatus.index].required=='true'}">
								          required="${ct.colList[tdStatus.index].required}" 
							            </c:if> 
						            </c:if> 
									onchange="setTrFileV(this)" vid="trFiles-${tr.id}-v-${trStatus.index}-${tdStatus.index}" name="trFiles"
									/>
									<label name="${ct.colList[tdStatus.index].name}-label">
									</label>
								    </span>
								    <c:if test="${exsitFileName!='null' && exsitFileName!=''}">
								    <span id="span-${tr.id}-v-${trStatus.index}-${tdStatus.index}"><a href="javascript:;" onclick="File.download('${td}')">${exsitFileName}</a></span>
								    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-delete'" href="javascript:;" onclick="reTbRow('${tr.id}-v-${trStatus.index}-${tdStatus.index}')"></a>
								    </c:if>
								</c:if>
								<!-- 下载类型开始 -->
								<c:if test="${ct.colList[tdStatus.index].type=='down'}">
								   <c:forEach items="${fn:split(td,'/')}" var="fileName" varStatus="fileStatus">
									  <c:if test="${fileStatus.last}">
									  <c:set var="exsitFileName" value="${fileName}"></c:set>
									  </c:if>
									</c:forEach>
									<input type="hidden" 
									name="${ct.colList[tdStatus.index].name}"
									<c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if> 
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									sum="${ct.colList[tdStatus.index].sum}"
									pType="tr"
									value = "${td}"
									/>
									<label name="${ct.colList[tdStatus.index].name}-label">
									<c:if test="${exsitFileName!='null' && exsitFileName!=''}"><a href="javascript:;" onclick="File.download('${td}')">${exsitFileName}</a></c:if></label>
								</c:if>
								<!-- 下载类型结束 -->
								<!-- 下拉数据库类型 -->
								<c:if test="${ct.colList[tdStatus.index].type=='select'}">
								  <span style="width:140px;height:22px;"  class="textbox combo">
								   <select name="${ct.colList[tdStatus.index].name}" 
									<c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if>  
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									pType="tr"
									url="${ct.colList[tdStatus.index].url}" 
									sum="${ct.colList[tdStatus.index].sum}"
									style="width:140px;line-height: normal;"
									class="select-url textbox-text" type="select" value="${td}">
									</select>
							      </span>
									
								</c:if>
								<!-- 下拉数据库类型结束 -->
								<!-- 下拉关联其他容器类型 -->
								<c:if test="${ct.colList[tdStatus.index].type=='selectrel'}">
								  <span style="width:140px;height:22px;"  class="textbox combo">
								   <select name="${ct.colList[tdStatus.index].name}" 
									<c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if>  
									varName="${ct.colList[tdStatus.index].varName}"
									formula="${ct.colList[tdStatus.index].formula}"
									pType="tr"
									url="${ct.colList[tdStatus.index].url}" 
									sum="${ct.colList[tdStatus.index].sum}"
									style="width:140px;line-height: normal;"
									class="select-rel textbox-text" type="selectrel"
									>
									<option value="${td}">${td}</option>
									</select>
								  </span>
								</c:if>
								<!-- 下拉关联其他容器类型 -->
								<!-- 下拉数据库类型结束 -->
								<!-- 纯数字类型 -->
								<c:if test="${ct.colList[tdStatus.index].type=='number'}">
								  <span style="width:140px;height:22px;"  class="textbox combo">
								  <input name="${ct.colList[tdStatus.index].name}" type="text" class="easyui-numberbox"
								  <c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if>  
								   varName="${ct.colList[tdStatus.index].varName}"
								   formula="${ct.colList[tdStatus.index].formula}"
								   pType="tr"
								   url="${ct.colList[tdStatus.index].url}" 
								   sum="${ct.colList[tdStatus.index].sum}"
								   style="width:140px;line-height: normal;"
								    value = "${td}"/>
								  </span>
								</c:if>
								<!-- 纯数字2位类型 -->
								<!-- 纯数字2位类型 -->
								<c:if test="${ct.colList[tdStatus.index].type=='number2'}">
								  <span style="width:140px;height:22px;"  class="textbox combo">
								  <input name="${ct.colList[tdStatus.index].name}" type="text" class="easyui-numberbox"
								  <c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if>  
								   varName="${ct.colList[tdStatus.index].varName}"
								   formula="${ct.colList[tdStatus.index].formula}"
								   pType="tr"
								   url="${ct.colList[tdStatus.index].url}" 
								   sum="${ct.colList[tdStatus.index].sum}"
								   style="width:140px;line-height: normal;"
								   data-options="precision:2" value = "${td}"/>
								  </span>
								</c:if>
								<!-- 纯数字2位类型 -->
								<!-- 纯数字4位类型 -->
								<c:if test="${ct.colList[tdStatus.index].type=='number4'}">
								  <span style="width:140px;height:22px;"  class="textbox combo">
								  <input name="${ct.colList[tdStatus.index].name}" type="text" class="easyui-numberbox"
								  <c:if test="${ct.colList[tdStatus.index].required=='true'}">
							          required="${ct.colList[tdStatus.index].required}" 
						            </c:if>  
								   varName="${ct.colList[tdStatus.index].varName}"
								   formula="${ct.colList[tdStatus.index].formula}"
								   pType="tr"
								   url="${ct.colList[tdStatus.index].url}" 
								   sum="${ct.colList[tdStatus.index].sum}"
								   style="width:140px;line-height: normal;"
								   data-options="precision:4" value = "${td}"/>
								  </span>
								</c:if>
								<!-- 纯数字4位类型结束 -->
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
<script type="text/javascript">
Survey.initComponent('${surveyCfgId}');
Survey.init('${surveyCfgId}','${surveyBase.versionNO}','${vendorSurveyCfg.surveyName}');
</script>

